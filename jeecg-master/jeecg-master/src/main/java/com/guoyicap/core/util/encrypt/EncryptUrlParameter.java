package com.guoyicap.core.util.encrypt;

import java.net.URLEncoder;

import org.jasypt.util.text.BasicTextEncryptor;
import org.jeecgframework.core.util.PropertiesUtil;


/**
 * @author guang
 * URL参数加密
 */
public class EncryptUrlParameter {
	//读取配置文件，获得加密后的密钥key
	private static final String encryptKey=new PropertiesUtil("security/security.properties").getProperties().getProperty("security.key");
	/**
	 * 加密
	 * @param text
	 * @return
	 * @throws Exception 
	 */
	public static String encrypt(String text) throws Exception{
		
		//解密key
		String key=EncryptUrlParameter.getDecryptKey(encryptKey);
		//根据key，加密text		
		String encryptString=SecurityHelper.encrypt(key, text);
		
		//URL 加密，处理特殊字符
		encryptString=URLEncoder.encode(encryptString);
		return encryptString;
	}
	
	/**
	 * 解密
	 * @param encryptTxt
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String encryptTxt) throws Exception{
		//解密key
		String key=EncryptUrlParameter.getDecryptKey(encryptKey);
		//根据key，解密text	
		String text=SecurityHelper.decrypt(key, encryptTxt);
		return text;
	}
	
	
	/**
	 * 获取解密后的key
	 * @param encryptKey
	 * @return
	 */
	public static String getDecryptKey(String encryptKey){
		 //加密工具
		BasicTextEncryptor encryptor = new BasicTextEncryptor();
		 encryptor.setPassword("qianbaKey");//密钥，固定不可变，否则之前加密后的密码不可逆
        //StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        //加密配置
//        EnvironmentStringPBEConfig config = new EnvironmentStringPBEConfig();
//        config.setAlgorithm("PBEWithMD5AndDES");
//        //自己在用的时候更改此密码
//        config.setPassword("qianbaKey");
        //应用配置
        //encryptor.setConfig(config);
        //解密
        String plainKey=encryptor.decrypt(encryptKey);
		return plainKey;
	}
	
	/**
	 * 获取加密后的key
	 * @param key 明文
	 * @return
	 */
	public static String getEncryptKey(String key){
		 //加密工具
		 BasicTextEncryptor encryptor = new BasicTextEncryptor();
		 encryptor.setPassword("qianbaKey");
		 
		//StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
       //加密配置
		/*EnvironmentStringPBEConfig config = new EnvironmentStringPBEConfig();
		config.setAlgorithm("PBEWithMD5AndDES");
       //自己在用的时候更改此密码
		config.setPassword("qianbaKey");*/
       //应用配置
		//encryptor.setConfig(config);
       //加密
		String encryptKey=encryptor.encrypt(key);
		return encryptKey;
	}
	
	
	/**
	 * 手动执行main方法，生成新的加密后的key，更新到security配置文件
	 * @param a
	 * @throws Exception 
	 */
	public static void main(String[] a) throws Exception{
		/*String key="kaixindai125369";
		String encryptKey=EncryptUrlParameter.getEncryptKey(key);
		System.out.println(encryptKey);
		
		System.out.println(EncryptUrlParameter.getDecryptKey(encryptKey));*/
		
		String text="Zcx012866";
		String s1 = EncryptUrlParameter.encrypt(text);
		System.out.println(s1);
		String s2=EncryptUrlParameter.decrypt("zmyjtF0KnsQ=JRMX0Wrz7B8Ee/ubTYiw+w==");
		System.out.println(s2);//#EDC3edc
		
		
		
	}
		

}
