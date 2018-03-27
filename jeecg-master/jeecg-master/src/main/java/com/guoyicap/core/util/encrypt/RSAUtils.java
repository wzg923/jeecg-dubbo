package com.guoyicap.core.util.encrypt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import org.jeecgframework.core.util.LogUtil;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * @ClassName: RSAUtil
 * @Description:RSA加密算法，非对称加密
 * @author 王振广
 * @date 2016年4月28日 上午8:56:11
 *
 */
public class RSAUtils {

	/**
	 * String to hold name of the encryption algorithm.
	 */
	public static final String ALGORITHM = "RSA";

	/**
	 * String to hold name of the encryption padding.
	 */
	public static final String PADDING = "RSA/NONE/NoPadding";
	//public static final String PADDING = "RSA/ECB/PKCS1Padding";
	/**
	 * String to hold name of the security provider.
	 */
	public static final String PROVIDER = "BC";

	/**
	 * @Fields KEY_SIZE : 生成密钥 长度
	 */
	public static final Integer KEY_SIZE = 1024;
	/**
	 * String to hold the name of the private key file.
	 */
	public static final String PRIVATE_KEY_FILE = RSAUtils.class.getResource("/").getFile().substring(1,
			RSAUtils.class.getResource("/").getFile().length()) + "security/RSA/private.key";

	/**
	 * String to hold name of the public key file.
	 */
	public static final String PUBLIC_KEY_FILE = RSAUtils.class.getResource("/").getFile().substring(1,
			RSAUtils.class.getResource("/").getFile().length()) + "security/RSA/public.key";

	public static final String CLIENT_PUBLIC_KEY_FILE = RSAUtils.class.getResource("/").getFile().substring(1,
			RSAUtils.class.getResource("/").getFile().length()) + "security/RSA/client/public.key";
	public static final String CLIENT_PRIVATE_KEY_FILE = RSAUtils.class.getResource("/").getFile().substring(1,
			RSAUtils.class.getResource("/").getFile().length()) + "security/RSA/client/private.key";


	/**
	 * 生成公钥和私钥
	 * @throws Exception 
	 *
	 */
	public static HashMap<String, Object> getKeys() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		// 私钥文件
		File privateKeyFile = new File(PRIVATE_KEY_FILE);
		// 公钥文件
		File publicKeyFile = new File(PUBLIC_KEY_FILE);
		// 如果密钥文件不存在，则重新生成密钥文件
		if (!privateKeyFile.exists() || !publicKeyFile.exists()) {
			try {
				RSAUtils.generateKeys();// 生成密钥文件
			} catch (NoSuchAlgorithmException | NoSuchProviderException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				LogUtil.error("生成RSA密钥失败",e);
			}
		}
		try{
			// 公钥读取
			FileReader publicKeyReader = new FileReader(publicKeyFile);
			BufferedReader publicKeyBR = new BufferedReader(publicKeyReader);
			String pubKeyStr = "";// 公钥字符串
			String tmpStr;// 读取文件临时字符串
			while ((tmpStr = publicKeyBR.readLine()) != null) {
				pubKeyStr += tmpStr;// 读取 公钥字符串
			}
			publicKeyBR.close();
			// 私钥读取
			FileReader privateKeyReader = new FileReader(privateKeyFile);
			BufferedReader privateKeyBR = new BufferedReader(privateKeyReader);
			String priKeyStr = "";// 公钥字符串
			while ((tmpStr = privateKeyBR.readLine()) != null) {
				priKeyStr += tmpStr;// 读取 公钥字符串
			}
			privateKeyBR.close();

			/*
			 * Security.addProvider(new
			 * org.bouncycastle.jce.provider.BouncyCastleProvider());
			 * KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(ALGORITHM,
			 * PROVIDER); // KeyPairGenerator keyPairGen
			 * =KeyPairGenerator.getInstance("RSA");
			 * keyPairGen.initialize(KEY_SIZE); KeyPair keyPair =
			 * keyPairGen.generateKeyPair(); RSAPublicKey publicKey = (RSAPublicKey)
			 * keyPair.getPublic(); RSAPrivateKey privateKey = (RSAPrivateKey)
			 * keyPair.getPrivate();
			 */
			//公钥
			RSAPublicKey publicKey = (RSAPublicKey)RSAUtils.getPublicKeyFromEncodedKeySpec(pubKeyStr);
			//私钥
			RSAPrivateKey privateKey = (RSAPrivateKey)RSAUtils.getPrivateKeyFromEncodedKeySpec(priKeyStr);
			map.put("public", publicKey);
			map.put("private", privateKey);
		}catch(Exception e1){
			e1.printStackTrace();
			LogUtil.error("生成RSA密钥对失败",e1);
		}		

		return map;
	}
	
	/**
	* @Title: getAndroidKeys
	* @author: 王振广
	* @Description: 获取客户端 公钥
	* @param @return    设定文件
	* @return HashMap<String,Object>    返回类型
	*/ 
	public static HashMap<String, Object> getClientKeys() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		// 公钥文件
		File publicKeyFile = new File(CLIENT_PUBLIC_KEY_FILE);
		File privateKeyFile = new File(CLIENT_PRIVATE_KEY_FILE);
		// 如果密钥文件不存在，则重新生成密钥文件
		if (!publicKeyFile.exists() || !privateKeyFile.exists()) {
			return null;
		}
		try {
			// 公钥读取
			FileReader publicKeyReader = new FileReader(publicKeyFile);
			BufferedReader publicKeyBR = new BufferedReader(publicKeyReader);
			String pubKeyStr = "";// 公钥字符串
			String tmpStr;// 读取文件临时字符串
			while ((tmpStr = publicKeyBR.readLine()) != null) {
				pubKeyStr += tmpStr;// 读取 公钥字符串
			}
			publicKeyBR.close();
			// 私钥读取
			FileReader privateKeyReader = new FileReader(privateKeyFile);
			BufferedReader privateKeyBR = new BufferedReader(privateKeyReader);
			String priKeyStr = "";// 私钥字符串
			while ((tmpStr = privateKeyBR.readLine()) != null) {
				priKeyStr += tmpStr;// 读取 私钥字符串
			}
			privateKeyBR.close();

			// 公钥
			RSAPublicKey publicKey = (RSAPublicKey) RSAUtils.getPublicKeyFromEncodedKeySpec(pubKeyStr);
			// 私钥
			RSAPrivateKey privateKey = (RSAPrivateKey) RSAUtils.getPrivateKeyFromEncodedKeySpec(priKeyStr);

			map.put("public", publicKey);
			map.put("private", privateKey);
		}catch(Exception e1){
			e1.printStackTrace();
			LogUtil.error("生成RSA密钥对失败",e1);
		}		

		return map;
	}

	
	
	/**
	 * @Title: generateKeys
	 * @author: 王振广
	 * @Description: 生成RSA公钥私钥，写入文件
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 *             设定文件
	 * @return void 返回类型
	 */
	public static void generateKeys() throws IOException, NoSuchAlgorithmException, NoSuchProviderException {

		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(ALGORITHM, PROVIDER);
		// KeyPairGenerator keyPairGen =KeyPairGenerator.getInstance("RSA");
		keyPairGen.initialize(KEY_SIZE);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

		sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
		// 公钥 Base64转码
		byte[] publicKeyEncodedByte = publicKey.getEncoded();
		String publicKeyEncoded = encoder.encode(publicKeyEncodedByte);
		// System.out.println("转码后公钥：\n"+publicKeyEncoded);
		publicKeyEncoded = "-----BEGIN PUBLIC KEY-----\n" + publicKeyEncoded + "\n-----END PUBLIC KEY-----";
		// System.out.println("转码后公钥：\n"+publicKeyEncoded);

		// 私钥 转码
		byte[] privateKeyEncodedByte = privateKey.getEncoded();
		String privateKeyEncoded = encoder.encode(privateKeyEncodedByte);
		// System.out.println("转码后私钥：\n"+privateKeyEncoded);
		privateKeyEncoded = "-----BEGIN PRIVATE KEY-----\n" + privateKeyEncoded + "\n-----END PRIVATE KEY-----";
		// System.out.println("转码后私钥：\n"+privateKeyEncoded);
		// 私钥文件
		File privateKeyFile = new File(PRIVATE_KEY_FILE);
		// 公钥文件
		File publicKeyFile = new File(PUBLIC_KEY_FILE);

		// Create files to store public and private key
		if (privateKeyFile.getParentFile() != null) {
			privateKeyFile.getParentFile().mkdirs();
		}
		privateKeyFile.createNewFile();

		if (publicKeyFile.getParentFile() != null) {
			publicKeyFile.getParentFile().mkdirs();
		}
		publicKeyFile.createNewFile();

		// Saving the Public key in a file
		FileWriter publicKeyOS = new FileWriter(publicKeyFile);
		publicKeyOS.write(publicKeyEncoded);
		publicKeyOS.close();

		// Saving the Private key in a file
		FileWriter privateKeyOS = new FileWriter(privateKeyFile);
		privateKeyOS.write(privateKeyEncoded);
		privateKeyOS.close();
	}

	/**
	 * 使用模和指数生成RSA公钥
	 * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA
	 * /None/NoPadding】
	 * 
	 * @param modulus
	 *            模
	 * @param exponent
	 *            指数
	 * @return
	 */
	public static RSAPublicKey getPublicKey(String modulus, String exponent) {
		try {
			BigInteger b1 = new BigInteger(modulus);
			BigInteger b2 = new BigInteger(exponent);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);
			return (RSAPublicKey) keyFactory.generatePublic(keySpec);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 使用模和指数生成RSA私钥
	 * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA
	 * /None/NoPadding】
	 * 
	 * @param modulus
	 *            模
	 * @param exponent
	 *            指数
	 * @return
	 */
	public static RSAPrivateKey getPrivateKey(String modulus, String exponent) {
		try {
			BigInteger b1 = new BigInteger(modulus);
			BigInteger b2 = new BigInteger(exponent);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(b1, b2);
			return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 公钥加密
	 * 
	 * @param data
	 * @param publicKey
	 * @return
	 * @throws Exception
	 */
	public static String encryptByPublicKey(String data, RSAPublicKey publicKey) throws Exception {
		// Cipher cipher = Cipher.getInstance("RSA");
		// cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		final Cipher cipher = Cipher.getInstance(PADDING, PROVIDER);

		// encrypt the plain text using the public key
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);

		// 模长
		int key_len = publicKey.getModulus().bitLength() / 8;
		// 加密数据长度 <= 模长-11
		String[] datas = splitString(data, key_len - 11);
		String mi = "";
		// 如果明文长度大于模长-11则要分组加密
		for (String s : datas) {
			//mi += bcd2Str(cipher.doFinal(s.getBytes()));//TODO 
			mi += new BASE64Encoder().encode(cipher.doFinal(s.getBytes()));//Sum Base64转码
			//mi += new String(cipher.doFinal(s.getBytes()));//TODO 
		}
			
		//String mi = bcd2Str(cipher.doFinal(data.getBytes()));
		/*@SuppressWarnings("restriction")
		sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
		@SuppressWarnings("restriction")
		String mi=encoder.encode(cipher.doFinal(data.getBytes()));*/
		
		return mi;
	}

	/**
	 * 私钥解密
	 * 
	 * @param data
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	public static String decryptByPrivateKey(String data, RSAPrivateKey privateKey) throws Exception {
		// Cipher cipher = Cipher.getInstance("RSA");
		// cipher.init(Cipher.DECRYPT_MODE, privateKey);

		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		final Cipher cipher = Cipher.getInstance(PADDING, PROVIDER);
		cipher.init(Cipher.DECRYPT_MODE, privateKey);

		// 模长
		int key_len = privateKey.getModulus().bitLength() / 8;
		//byte[] bytes = data.getBytes();//TODO 
		//byte[] bcd = ASCII_To_BCD(bytes, bytes.length);//TODO del
		byte[] bcd=new BASE64Decoder().decodeBuffer(data);//Sun Base64解码
		//System.out.println(bcd.length);
		// 如果密文长度大于模长则要分组解密
		String ming = "";
		byte[][] arrays = splitArray(bcd, key_len);//TODO edit
		for (byte[] arr : arrays) {
			//System.out.println(arr.length);
			ming += new String(cipher.doFinal(arr));
		}
				
		
		//RSA/ECB/PKCS1Padding  方式解密
		/*@SuppressWarnings("restriction")
		sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
		byte[] bytes = decoder.decodeBuffer(data);
		String ming =new String(cipher.doFinal(bytes));*/
		
		return ming;
	}

	/**
	 * ASCII码转BCD码
	 * 
	 */
	public static byte[] ASCII_To_BCD(byte[] ascii, int asc_len) {
		byte[] bcd = new byte[asc_len / 2];
		int j = 0;
		for (int i = 0; i < (asc_len + 1) / 2; i++) {
			bcd[i] = asc_to_bcd(ascii[j++]);
			bcd[i] = (byte) (((j >= asc_len) ? 0x00 : asc_to_bcd(ascii[j++])) + (bcd[i] << 4));
		}
		return bcd;
	}

	public static byte asc_to_bcd(byte asc) {
		byte bcd;

		if ((asc >= '0') && (asc <= '9'))
			bcd = (byte) (asc - '0');
		else if ((asc >= 'A') && (asc <= 'F'))
			bcd = (byte) (asc - 'A' + 10);
		else if ((asc >= 'a') && (asc <= 'f'))
			bcd = (byte) (asc - 'a' + 10);
		else
			bcd = (byte) (asc - 48);
		return bcd;
	}

	/**
	 * BCD转字符串
	 */
	public static String bcd2Str(byte[] bytes) {
		char temp[] = new char[bytes.length * 2], val;

		for (int i = 0; i < bytes.length; i++) {
			val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
			temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');

			val = (char) (bytes[i] & 0x0f);
			temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
		}
		return new String(temp);
	}

	/**
	 * 拆分字符串
	 */
	public static String[] splitString(String string, int len) {
		int x = string.length() / len;
		int y = string.length() % len;
		int z = 0;
		if (y != 0) {
			z = 1;
		}
		String[] strings = new String[x + z];
		String str = "";
		for (int i = 0; i < x + z; i++) {
			if (i == x + z - 1 && y != 0) {
				str = string.substring(i * len, i * len + y);
			} else {
				str = string.substring(i * len, i * len + len);
			}
			strings[i] = str;
		}
		return strings;
	}

	/**
	 * 拆分数组
	 */
	public static byte[][] splitArray(byte[] data, int len) {
		int x = data.length / len;
		int y = data.length % len;
		int z = 0;
		if (y != 0) {
			z = 1;
		}
		byte[][] arrays = new byte[x + z][];
		byte[] arr;
		for (int i = 0; i < x + z; i++) {
			arr = new byte[len];
			if (i == x + z - 1 && y != 0) {
				System.arraycopy(data, i * len, arr, 0, y);
			} else {
				System.arraycopy(data, i * len, arr, 0, len);
			}
			arrays[i] = arr;
		}
		return arrays;
	}

	/**
	 * Return public RSA key modulus
	 * 
	 * @param keyPair
	 *            RSA keys
	 * @return modulus value as hex string
	 */
	public static String getPublicKeyModulus(RSAPublicKey publicKey) {
		// RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		return publicKey.getModulus().toString(16);
	}

	/**
	 * Return public RSA key exponent
	 * 
	 * @param keyPair
	 *            RSA keys
	 * @return public exponent value as hex string
	 */
	public static String getPublicKeyExponent(RSAPublicKey publicKey) {
		// RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		return publicKey.getPublicExponent().toString(16);
	}

	/**
	 * Max block size with given key length
	 * 
	 * @param keyLength
	 *            length of key
	 * @return numeber of digits
	 */
	public static int getMaxDigits(int keyLength) {
		return ((keyLength * 2) / 16) + 3;
	}

	/**
	 * Convert byte array to hex string
	 * 
	 * @param bytes
	 *            input byte array
	 * @return Hex string representation
	 */
	public static String byteArrayToHexString(byte[] bytes) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			result.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
		}
		return result.toString();
	}

	/**
	 * Convert hex string to byte array
	 * 
	 * @param data
	 *            input string data
	 * @return bytes
	 */
	public static byte[] hexStringToByteArray(String data) {
		int k = 0;
		byte[] results = new byte[data.length() / 2];
		for (int i = 0; i < data.length();) {
			results[k] = (byte) (Character.digit(data.charAt(i++), 16) << 4);
			results[k] += (byte) (Character.digit(data.charAt(i++), 16));
			k++;
		}
		return results;
	}

	/**
	 * @Title: toPublicKeyString
	 * @author: 王振广
	 * @Description: 公钥 转为String
	 * @param @return
	 * @param @throws
	 *            Exception 设定文件
	 * @return String 返回类型
	 */
	public static String toPublicKeyString(RSAPublicKey publicKey) throws Exception {
		StringBuffer out = new StringBuffer();

		String e = getPublicKeyExponent(publicKey);
		String n = getPublicKeyModulus(publicKey);
		String md = String.valueOf(getMaxDigits(1024));

		String encodedString = String.valueOf(publicKey.getEncoded());
		String format = publicKey.getFormat();

		out.append("{\"e\":\"");
		out.append(e);
		out.append("\",\"n\":\"");
		out.append(n);
		out.append("\",\"maxdigits\":\"");
		out.append(md);

		out.append("\",\"encodedString\":\"");
		out.append(encodedString);
		out.append("\",\"format\":\"");
		out.append(format);

		out.append("\"}");

		return out.toString();
	}

	public static String toPrivateKeyString(RSAPrivateKey privateKey) throws Exception {
		StringBuffer out = new StringBuffer();

		String n = privateKey.getModulus().toString(16);
		String d = privateKey.getPrivateExponent().toString(16);
		String md = String.valueOf(getMaxDigits(1024));

		String encodedString = String.valueOf(privateKey.getEncoded());
		String format = privateKey.getFormat();

		out.append("{\"d\":\"");
		out.append(d);
		out.append("\",\"n\":\"");
		out.append(n);
		out.append("\",\"maxdigits\":\"");
		out.append(md);

		out.append("\",\"encodedString\":\"");
		out.append(encodedString);
		out.append("\",\"format\":\"");
		out.append(format);

		out.append("\"}");

		return out.toString();
	}

	/**
	 * @Title: getPublicKeyFromEncodedKeySpec
	 * @author: 王振广
	 * @Description: 根据Base64方式转码后的公钥生成RSA公钥
	 *               <p>
	 *               适用于openssl IOS 生成的公钥
	 * @return RSAPublicKey 返回类型
	 */
	public static RSAPublicKey getPublicKeyFromEncodedKeySpec(String publicKeyString) throws Exception {
		// Base64转码后公钥
		//String publicKey = "-----BEGIN PUBLIC KEY-----\nMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI2bvVLVYrb4B0raZgFP60VXY\ncvRmk9q56QiTmEm9HXlSPq1zyhyPQHGti5FokYJMzNcKm0bwL1q6ioJuD4EFI56D\na+70XdRz1CjQPQE3yXrXXVvOsmq9LsdxTFWsVBTehdCmrapKZVVx6PKl7myh0cfX\nQmyveT/eqyZK1gYjvQIDAQAB\n-----END PUBLIC KEY——";

		@SuppressWarnings("restriction")
		sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();

		// X.509
		@SuppressWarnings("restriction")
		byte[] decodedKeyData = decoder
				.decodeBuffer(publicKeyString.replaceAll("-----\\w+ PUBLIC KEY-----", "").replace("\n", ""));
		RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA")
				.generatePublic(new X509EncodedKeySpec(decodedKeyData));
		// 打印公钥
		//System.out.println(RSAUtils.toPublicKeyString(pubKey));

		return pubKey;
	}

	/**
	 * @Title: getPrivateKeyFromEncodedKeySpec
	 * @author: 王振广
	 * @Description: 根据Base64方式转码后的私钥生成RSA私钥
	 *               <p>
	 *               适用于openssl IOS 生成的私钥
	 * @return RSAPrivateKey 返回类型
	 */
	public static RSAPrivateKey getPrivateKeyFromEncodedKeySpec(String PrivateKeyString) throws Exception {
		// Base64转码后私钥
		//String privateKey = "-----BEGIN PRIVATE KEY-----\nMIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMMjZu9UtVitvgHS\ntpmAU/rRVdhy9GaT2rnpCJOYSb0deVI+rXPKHI9Aca2LkWiRgkzM1wqbRvAvWrqK\ngm4PgQUjnoNr7vRd1HPUKNA9ATfJetddW86yar0ux3FMVaxUFN6F0KatqkplVXHo\n8qXubKHRx9dCbK95P96rJkrWBiO9AgMBAAECgYBO1UKEdYg9pxMX0XSLVtiWf3Na\n2jX6Ksk2Sfp5BhDkIcAdhcy09nXLOZGzNqsrv30QYcCOPGTQK5FPwx0mMYVBRAdo\nOLYp7NzxW/File//169O3ZFpkZ7MF0I2oQcNGTpMCUpaY6xMmxqN22INgi8SHp3w\nVU+2bRMLDXEc/MOmAQJBAP+Sv6JdkrY+7WGuQN5O5PjsB15lOGcr4vcfz4vAQ/uy\nEGYZh6IO2Eu0lW6sw2x6uRg0c6hMiFEJcO89qlH/B10CQQDDdtGrzXWVG457vA27\nkpduDpM6BQWTX6wYV9zRlcYYMFHwAQkE0BTvIYde2il6DKGyzokgI6zQyhgtRJ1x\nL6fhAkB9NvvW4/uWeLw7CHHVuVersZBmqjb5LWJU62v3L2rfbT1lmIqAVr+YT9CK\n2fAhPPtkpYYo5d4/vd1sCY1iAQ4tAkEAm2yPrJzjMn2G/ry57rzRzKGqUChOFrGs\nlm7HF6CQtAs4HC+2jC0peDyg97th37rLmPLB9txnPl50ewpkZuwOAQJBAM/eJnFw\nF5QAcL4CYDbfBKocx82VX/pFXng50T7FODiWbbL4UnxICE0UBFInNNiWJxNEb6jL\n5xd0pcy9O2DOeso=\n-----END PRIVATE KEY——";

		@SuppressWarnings("restriction")
		sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();

		// PKCS#8
		@SuppressWarnings("restriction")
		byte[] decodedKeyData2 = decoder
				.decodeBuffer(PrivateKeyString.replaceAll("-----\\w+ PRIVATE KEY-----", "").replace("\n", ""));
		RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA")
				.generatePrivate(new PKCS8EncodedKeySpec(decodedKeyData2));

		// 打印私钥
		//System.out.println(RSAUtils.toPrivateKeyString(priKey));

		return priKey;
	}

	/**
	 * The method checks if the pair of public and private key has been
	 * generated.
	 * 
	 * @return flag indicating if the pair of keys were generated.
	 */
	public static boolean areKeysPresent() {

		File privateKey = new File(PRIVATE_KEY_FILE);
		File publicKey = new File(PUBLIC_KEY_FILE);

		if (privateKey.exists() && publicKey.exists()) {
			return true;
		}
		return false;
	}

	/**
	 * @Title: getEncodedKeys
	 * @author: 王振广
	 * @Description: 生成RSA公钥 ，返回转码后公钥字符串
	 * @param @throws
	 *            Exception
	 * @param @throws
	 *            NoSuchProviderException 设定文件
	 * @return void 返回类型
	 */
	public static String getEncodedPublicKeys() throws Exception {

		// TODO Auto-generated method stub
		HashMap<String, Object> map = RSAUtils.getKeys();
		// 生成公钥和私钥
		RSAPublicKey publicKey = (RSAPublicKey) map.get("public");
		//RSAPrivateKey privateKey = (RSAPrivateKey) map.get("private");
		// 打印公钥 和私钥
		//System.out.println(RSAUtils.toPublicKeyString(publicKey));
		//System.out.println(RSAUtils.toPrivateKeyString(privateKey));

		sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
		// 公钥 转码
		byte[] publicKeyEncodedByte = publicKey.getEncoded();

		String publicKeyEncoded = encoder.encode(publicKeyEncodedByte);
		
		return publicKeyEncoded;
		// System.out.println("转码后公钥：\n"+publicKeyEncoded);
		//publicKeyEncoded = "-----BEGIN PUBLIC KEY-----\n" + publicKeyEncoded + "\n-----END PUBLIC KEY-----";
		//System.out.println("转码后公钥：\n" + publicKeyEncoded);

		// 私钥 转码
		//byte[] privateKeyEncodedByte = privateKey.getEncoded();

		//String privateKeyEncoded = encoder.encode(privateKeyEncodedByte);

		// System.out.println("转码后私钥：\n"+privateKeyEncoded);
		//privateKeyEncoded = "-----BEGIN PRIVATE KEY-----\n" + privateKeyEncoded + "\n-----END PRIVATE KEY-----";
		//System.out.println("转码后私钥：\n" + privateKeyEncoded);
		
	}
	
	
	/**
	* @Title: getEncodedPrivateKeys
	* @author: 王振广
	* @Description:生成RSA私钥，返回转码后私钥字符串
	* @throws Exception
	* @throws NoSuchProviderException    设定文件
	* @return String    返回类型
	*/ 
	public static String getEncodedPrivateKeys() throws Exception {

		// TODO Auto-generated method stub
		HashMap<String, Object> map = RSAUtils.getKeys();
		// 生成公钥和私钥
		//RSAPublicKey publicKey = (RSAPublicKey) map.get("public");
		RSAPrivateKey privateKey = (RSAPrivateKey) map.get("private");
		// 打印公钥 和私钥
		//System.out.println(RSAUtils.toPublicKeyString(publicKey));
		//System.out.println(RSAUtils.toPrivateKeyString(privateKey));

		sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
		// 公钥 转码
		//byte[] publicKeyEncodedByte = publicKey.getEncoded();

		//String publicKeyEncoded = encoder.encode(publicKeyEncodedByte);
		
		
		// System.out.println("转码后公钥：\n"+publicKeyEncoded);
		//publicKeyEncoded = "-----BEGIN PUBLIC KEY-----\n" + publicKeyEncoded + "\n-----END PUBLIC KEY-----";
		//System.out.println("转码后公钥：\n" + publicKeyEncoded);

		// 私钥 转码
		byte[] privateKeyEncodedByte = privateKey.getEncoded();

		String privateKeyEncoded = encoder.encode(privateKeyEncodedByte);

		return privateKeyEncoded;
		// System.out.println("转码后私钥：\n"+privateKeyEncoded);
		//privateKeyEncoded = "-----BEGIN PRIVATE KEY-----\n" + privateKeyEncoded + "\n-----END PRIVATE KEY-----";
		//System.out.println("转码后私钥：\n" + privateKeyEncoded);
		
	}
	
	
	/**
	 * @Title: getEncodedKeys
	 * @author: 王振广
	 * @Description: 生成RSA公钥 ，返回转码后公钥字符串
	 * @param @param publicKey
	 * @param @throws
	 *            Exception
	 * @param @throws
	 *            NoSuchProviderException 设定文件
	 * @return void 返回类型
	 */	
	public static String getEncodedPublicKeys(RSAPublicKey publicKey) throws Exception{

		sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
		// 公钥 转码
		byte[] publicKeyEncodedByte = publicKey.getEncoded();

		String publicKeyEncoded = encoder.encode(publicKeyEncodedByte);
		
		return publicKeyEncoded;
		
	}
	
	
	/**
	* @Title: getEncodedPrivateKeys
	* @author: 王振广
	* @Description:生成RSA私钥，返回转码后私钥字符串
	* @param privateKey
	* @throws Exception
	* @throws NoSuchProviderException    设定文件
	* @return String    返回类型
	*/ 
	public static String getEncodedPrivateKeys(RSAPrivateKey privateKey) throws Exception, NoSuchProviderException {
		sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();

		// 私钥 转码
		byte[] privateKeyEncodedByte = privateKey.getEncoded();

		String privateKeyEncoded = encoder.encode(privateKeyEncodedByte);

		return privateKeyEncoded;
		
	}
	
	
	public static void main(String[] args) throws Exception{
		/*Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("hid", "4633");
		paramMap.put("email", "zhangmeng@kaixinwealth.com");
		paramMap.put("name", "张萌测");
		paramMap.put("province", "370000");
		paramMap.put("city", "370200");
		paramMap.put("area", "370203");
		paramMap.put("idcard", "370306199103190011");
		paramMap.put("bankid", "1");
		paramMap.put("bankcard", "09876543211234567890");
		paramMap.put("factory", "青岛钱吧金融信息服务有限公司");
		paramMap.put("logintoken", "4f0f7af1e87a0f55fa1304f1c7203542");
		
		String content=JSONUtils.toJSONString(JSONUtils.toJSONObject(paramMap));
		content=URLEncoder.encode(content);
		System.out.println("明文："+content);
		
		
		// 获取RSA密钥对
		Map<String, Object> keysMap = RSAUtils.getKeys();
				// 生成公钥和私钥
		RSAPublicKey RSA_PUB_KEY_SERVER = (RSAPublicKey) keysMap.get("public");
		RSAPrivateKey RSA_PRI_KEY_SERVER = (RSAPrivateKey) keysMap.get("private");
		String mi=RSAUtils.encryptByPublicKey(content, RSA_PUB_KEY_SERVER);
		
		System.out.println("密文："+mi);*/
		
		// 获取RSA密钥对
		Map<String, Object> keysMap2 = RSAUtils.getKeys();
						// 生成公钥和私钥
		RSAPublicKey RSA_PUB_KEY_SERVER2 = (RSAPublicKey) keysMap2.get("public");
		RSAPrivateKey RSA_PRI_KEY_SERVER2 = (RSAPrivateKey) keysMap2.get("private");
//		String content2=URLEncoder.encode("Hello World!你好！");
		String content2="Hello+World!%e4%bd%a0%e5%a5%bd%ef%bc%81";
		String mi2=RSAUtils.encryptByPublicKey(content2, RSA_PUB_KEY_SERVER2);
		System.out.println("加密后："+mi2);
		//String test2="SrCDcP2GofSHeKvm7o+oq2UuwAJnD1O9bKKUFFD7CMRlHFqiJhmcJMWbo8T+0leH6fd+MjmuYQDrAqD1cglsXV4xfO8CSlHF5PjtHkRjJyhGmpVz/2nVtfCJetClx/l6fLXJP90ADUK0W/NzKCX9S7fnlW3WvR4d6Hd6MmOxVXA=";
		String plaintext2=RSAUtils.decryptByPrivateKey(mi2, RSA_PRI_KEY_SERVER2);
		plaintext2=URLDecoder.decode(plaintext2);
		System.out.println("解密后："+plaintext2);
		
		
		
	}

}
