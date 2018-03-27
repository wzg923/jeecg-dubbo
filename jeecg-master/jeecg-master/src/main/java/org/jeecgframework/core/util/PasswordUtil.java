package org.jeecgframework.core.util;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import com.guoyicap.core.util.encrypt.EncryptUrlParameter;
import com.guoyicap.core.util.encrypt.SecurityHelper;
public class PasswordUtil {

	/**
	 * JAVA6支持以下任意一种算法 PBEWITHMD5ANDDES PBEWITHMD5ANDTRIPLEDES
	 * PBEWITHSHAANDDESEDE PBEWITHSHA1ANDRC2_40 PBKDF2WITHHMACSHA1
	 * */

	/**
	 * 定义使用的算法为:PBEWITHMD5andDES算法
	 */
	public static final String ALGORITHM = "PBEWithMD5AndDES";//加密算法
	//public static final String Salt = "63293188";//密钥
	public static final String Salt = ResourceUtil.getCustomerConfigByName("security/security","password.bg.key");//密钥
	
	//读取配置文件，获得加密后的密钥key
	public static final String MD5_KEY=ResourceUtil.getCustomerConfigByName("security/security","security.MD5.key");//MD5密钥

	/**
	 * 定义迭代次数为1000次
	 */
	private static final int ITERATIONCOUNT = 1000;

	/**
	 * 获取加密算法中使用的盐值,解密中使用的盐值必须与加密中使用的相同才能完成操作. 盐长度必须为8字节
	 * 
	 * @return byte[] 盐值
	 * */
	public static byte[] getSalt() throws Exception {
		// 实例化安全随机数
		SecureRandom random = new SecureRandom();
		// 产出盐
		return random.generateSeed(8);
	}

	public static byte[] getStaticSalt() {
		// 产出盐
		return Salt.getBytes();
	}

	/**
	 * 根据PBE密码生成一把密钥
	 * 
	 * @param password
	 *            生成密钥时所使用的密码
	 * @return Key PBE算法密钥
	 * */
	public static Key getPBEKey(String password) {
		// 实例化使用的算法
		SecretKeyFactory keyFactory;
		SecretKey secretKey = null;
		try {
			keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
			// 设置PBE密钥参数
			PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
			// 生成密钥
			secretKey = keyFactory.generateSecret(keySpec);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return secretKey;
	}

	/**
	 * 加密明文字符串
	 * 
	 * @param plaintext
	 *            待加密的明文字符串
	 * @param password
	 *            生成密钥时所使用的密码
	 * @param salt
	 *            盐值
	 * @return 加密后的密文字符串
	 * @throws Exception
	 */
	public static String encrypt(String plaintext, String password, byte[] salt) {

		Key key = getPBEKey(password);
		byte[] encipheredData = null;
		PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, ITERATIONCOUNT);
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);

			cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);

			encipheredData = cipher.doFinal(plaintext.getBytes());
		} catch (Exception e) {
			LogUtil.error("密码加密失败",e);
		}
		return bytesToHexString(encipheredData);
	}

	/**
	 * 解密密文字符串
	 * 
	 * @param ciphertext
	 *            待解密的密文字符串
	 * @param password
	 *            生成密钥时所使用的密码(如需解密,该参数需要与加密时使用的一致)
	 * @param salt
	 *            盐值(如需解密,该参数需要与加密时使用的一致)
	 * @return 解密后的明文字符串
	 * @throws Exception
	 */
	public static String decrypt(String ciphertext, String password, byte[] salt) {

		Key key = getPBEKey(password);
		byte[] passDec = null;
		PBEParameterSpec parameterSpec = new PBEParameterSpec(getStaticSalt(), ITERATIONCOUNT);
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);

			cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);

			passDec = cipher.doFinal(hexStringToBytes(ciphertext));
		}

		catch (Exception e) {
			// TODO: handle exception
		}
		return new String(passDec);
	}

	/**
	 * 将字节数组转换为十六进制字符串
	 * 
	 * @param src
	 *            字节数组
	 * @return
	 */
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	/**
	 * 将十六进制字符串转换为字节数组
	 * 
	 * @param hexString
	 *            十六进制字符串
	 * @return
	 */
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	
	/**
	* @Title: encryptWithMD5
	* @Description: 采用MD5算法进行加密
	* @param plaintext
	* @return String    返回类型
	*/ 
	public static String encryptWithMD5(String plaintext){
		String ciphertext=null;
		if(StringUtil.isNotEmpty(MD5_KEY)){
			//采用MD5方式加密
			ciphertext=SecurityHelper.encryptByMD5(plaintext, MD5_KEY);//加密后密文
		}else{
			ciphertext=SecurityHelper.encryptByMD5(plaintext, "");//加密后密文
		}
		return ciphertext;
	}
	
	public static String encryptWithMD5(String plaintext,String key){
		String ciphertext=null;
		if(StringUtil.isNotEmpty(key)){
			//采用MD5方式加密
			ciphertext=SecurityHelper.encryptByMD5(plaintext, key);//加密后密文
		}else{
			ciphertext=SecurityHelper.encryptByMD5(plaintext, "");//加密后密文
		}
		return ciphertext;
	}
	
	/**
	* @Title: encryptTwo
	* @author: 王振广
	* @Description: 新增加密算法
	* @param @param plaintext
	* @param @param password
	* @param @return
	* @param @throws Exception    设定文件
	* @return String    返回类型
	*/ 
	public static String encryptTwo(String plaintext, String password) throws Exception{
		//第一次加密
		String encryptText=SecurityHelper.encrypt(password,plaintext);//加密用户名，以password 作为密钥
		
		PropertiesUtil pro = new PropertiesUtil("security/security.properties");
		//读取配置文件，获得加密后的密钥key
		String encryptKey=pro.getProperties().getProperty("security.MD5.key");//密钥
		//解密key,获得明文密码key
		String key=EncryptUrlParameter.getDecryptKey(encryptKey);
		//采用MD5方式加密
		//将第一次加密的密文 ，再次加密
		String ciphertext=SecurityHelper.encryptByMD5(encryptText, key);//加密后密文
		return ciphertext;
	}
	
	public static void main(String[] args) {
		int i=10;
		for (int j = 0; j < i; j++) {
			if((j)%3==0)
			{
				System.out.print("<br>");
			}
			else {
				System.out.print(j);
			}
		}
		System.out.print(-1%2==0);
		String str = "admin";
		String password = "123456";

		org.jeecgframework.core.util.LogUtil.info("明文:" + str);
		org.jeecgframework.core.util.LogUtil.info("密码:" + password);

		try {
			byte[] salt = PasswordUtil.getStaticSalt();
			String ciphertext = PasswordUtil.encrypt(str, password, salt);
			org.jeecgframework.core.util.LogUtil.info("密文:" + ciphertext);
			String plaintext = PasswordUtil.decrypt(ciphertext, password, salt);
			org.jeecgframework.core.util.LogUtil.info("明文:" + plaintext);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		String s1=PasswordUtil.encryptWithMD5("111111"); //70dcd244905dba0e6370e87cd9069d44
    	System.out.println(s1);
	}
}