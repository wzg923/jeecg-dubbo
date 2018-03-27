package com.guoyicap.core.util.encrypt;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jeecgframework.core.util.StringUtil;

/**
 * @author guang
 * 加密工具类
 */
public class SecurityHelper {
	protected final static Log logger = LogFactory.getLog(SecurityHelper.class);
	private final static int ITERATIONS = 20;

	/**加密
	 * 加密方式：key+PBEWithMD5AndDES
	 * @param key
	 * @param plainText
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String key, String plainText) throws Exception {
		String encryptTxt = "";//加密后密文
		try {
			byte[] salt = new byte[8];//加密后key
			//MD5加密
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(key.getBytes());
			byte[] digest = md.digest();
			for (int i = 0; i < 8; i++) {
				salt[i] = digest[i];//加密后key
			}
			//PBEWithMD5AndDES加密
			PBEKeySpec pbeKeySpec = new PBEKeySpec(key.toCharArray());//pbe key密钥
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
			SecretKey skey = keyFactory.generateSecret(pbeKeySpec);//加密后密钥
			PBEParameterSpec paramSpec = new PBEParameterSpec(salt, ITERATIONS);//加密后参数
			Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
			cipher.init(Cipher.ENCRYPT_MODE, skey, paramSpec);
			byte[] cipherText = cipher.doFinal(plainText.getBytes());//待加密文本
			String saltString = new String(Base64.encode(salt));//加密key Base64编码
			String ciphertextString = new String(Base64.encode(cipherText));//加密text Base编码
			encryptTxt=saltString + ciphertextString;//加密后密文=加密key+加密文本
			return encryptTxt;
		} catch (Exception e) {
			throw new Exception("Encrypt Text Error:" + e.getMessage(), e);
		}
	}

	/**
	 * 解密
	 * 解密方式：key+PBEWithMD5AndDES
	 * @param key
	 * @param encryptTxt
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String key, String encryptTxt) throws Exception {
		int saltLength = 12;
		try {
			String salt = encryptTxt.substring(0, saltLength);
			String ciphertext = encryptTxt.substring(saltLength, encryptTxt.length());
			byte[] saltarray = Base64.decode(salt.getBytes());
			byte[] ciphertextArray = Base64.decode(ciphertext.getBytes());
			PBEKeySpec keySpec = new PBEKeySpec(key.toCharArray());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
			SecretKey skey = keyFactory.generateSecret(keySpec);
			PBEParameterSpec paramSpec = new PBEParameterSpec(saltarray, ITERATIONS);
			Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
			cipher.init(Cipher.DECRYPT_MODE, skey, paramSpec);
			byte[] plaintextArray = cipher.doFinal(ciphertextArray);
			return new String(plaintextArray);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	
	
    /** 
     * 加密 :AES算法加密     *  
     * @param content 需要加密的内容 
     * @param password  加密密钥
     * @return 
     */  
	public static String encryptByAES(String content, String password) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(password.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			byte[] byteContent = content.getBytes("utf-8");
			cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(byteContent);
			//sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
			return StringUtil.bytesToHexString(result); // 加密
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}  
    
    
    /**解密 ：AES算法解密
     * @param content  待解密内容 
     * @param password 解密密钥 
     * @return 
     */  
	public static String decryptByAES(String content, String password) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(password.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
			//sun.misc.BASE64Decoder decoder=new sun.misc.BASE64Decoder();
			byte[] result = cipher.doFinal(StringUtil.hexStringToBytes(content));
			return new String(result); // 加密
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}  
    
    
	/**
	* @Title: encryptByMD5
	* @Description: MD5加密
	* @param  content 加密文本
	* @param  key 加密密钥
	* @return String   返回类型
	*/ 
	public static String encryptByMD5(String content,String key) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update((content+key).getBytes()); // 通过使用 update 方法处理数据,使指定的 byte数组更新摘要,要加密的内容加上 key进行混淆加密
			byte[] encryptStr = md.digest(); // 获得密文完成哈希计算,产生128 位的长整数
			char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符
			int k = 0; // 表示转换结果中对应的字符位置
			for (int i = 0; i < 16; i++) { // 从第一个字节开始，对每一个字节,转换成 16 进制字符的转换
				byte byte0 = encryptStr[i]; // 取第 i 个字节
				str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换, >>>
															// 为逻辑右移，将符号位一起右移
				str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
			}
			return new String(str); // 换后的结果转换为字符串
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	
    
    
    public static void main(String[] args) throws Exception{
    	String s1=SecurityHelper.encryptByAES("你好q哈哈哈哈哈呵呵哈哈哈azWSXedcRFVtgbYHNujmIK<ol.P:?0123456789","yj888128********");
    	System.out.println(s1);
    	//new sun.misc.BASE64Encoder();
    	//System.out.println(new sun.misc.BASE64Encoder().encode(s1.getBytes()));
    	
    	String s3=SecurityHelper.decryptByAES( s1,"yj888128********");
    	System.out.println(s3);
    	
    	//String encryptText=SecurityHelper.encryptByAES("123", "yj888128");
    	//String decryptText=SecurityHelper.decryptByAES("123", "yj888128");
    	
    	/*String s1=SecurityHelper.encryptByMD5("111111","kaixindai125369"); //70dcd244905dba0e6370e87cd9069d44
    	System.out.println(s1);*/
    	
    	
    	/*String s1=SecurityHelper.encryptByPBEWithMD5AndDES("9999999999", "abc18563956775");
    	System.out.println(s1);
    	String s2=SecurityHelper.encryptByPBEWithMD5AndDES("9999999999", "abc18563956775");
    	System.out.println(s2);
    	String s3=SecurityHelper.decryptByPBEWithMD5AndDES("9999999999", s1);
    	System.out.println(s3);*/
    	
    	/*String a1="abcdefghijklmnopqrstuvwxyz1234567890";
    	String a2=SecurityHelper.encryptByShitfByte(a1, (byte)3);
    	System.out.println(a2);
    	
    	
    	
    	String a3=SecurityHelper.decryptByShiftByte(a2,(byte)3);
    	System.out.println(a3);*/
    	
    	 /*Scanner objScanner = new Scanner(System.in);
    	   System.out.println("请输入要进行移位的数：");
    	   int pwd = objScanner.nextInt();
    	   System.out.println("请输入需要移的位数：");
    	   int offset = objScanner.nextInt();
    	   System.out.println("移位前："+pwd);
    	   pwd = leftEncrypt(pwd, offset);
    	  
    	   System.out.println("移位后："+pwd);*/
    	
    	/*String ss=SecurityHelper.encryptByMD5("1", "");
    	System.out.println(ss);*/
    	
    }

}
