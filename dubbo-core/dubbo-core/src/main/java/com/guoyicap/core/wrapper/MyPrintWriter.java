package com.guoyicap.core.wrapper;

import java.io.PrintWriter;
import java.io.Writer;
import java.net.URLDecoder;
import java.net.URLEncoder;

import com.guoyicap.core.filter.EncryptFilter;
import com.guoyicap.core.util.encrypt.RSAUtils;

/**
* @ClassName: MyPrintWriter
* @Description: 封装PrintWriter
* @author 王振广
* @date 2016年5月3日 下午2:29:28
*
*/ 
public class MyPrintWriter extends PrintWriter {
	private StringBuilder buffer;

	public MyPrintWriter(Writer out) {
		super(out);
		buffer = new StringBuilder();
	}

	@Override
	public void write(char[] buf, int off, int len) {
		// super.write(buf, off, len);
		char[] dest = new char[len];
		System.arraycopy(buf, off, dest, 0, len);
		//buffer.append(dest);
		
		//add by guang 增加response内容加密处理
		try {
			buffer.append(RSAUtils.encryptByPublicKey(URLEncoder.encode(new String(dest)), EncryptFilter.RSA_PUB_KEY_CLIENT));
		} catch (Exception e) {			
			e.printStackTrace();
		}
		//add by guang 增加response内容加密处理
		
		
		
	}

	@Override
	public void write(char[] buf) {
		//super.write(buf);
		// add by guang 增加response内容加密处理
		try {
			super.write(RSAUtils.encryptByPublicKey(URLEncoder.encode(new String(buf)), EncryptFilter.RSA_PUB_KEY_CLIENT));
		} catch (Exception e) {
			e.printStackTrace();
		}
		// add by guang 增加response内容加密处理
	}

	@Override
	public void write(int c) {
		super.write(c);
	}

	@Override
	public void write(String s, int off, int len) {
		//super.write(s, off, len);
		// add by guang 增加response内容加密处理
		try {
			super.write(RSAUtils.encryptByPublicKey(URLEncoder.encode(s), EncryptFilter.RSA_PUB_KEY_CLIENT),off,len);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// add by guang 增加response内容加密处理
	}

	@Override
	public void write(String s) {
		//super.write(s);
		// add by guang 增加response内容加密处理
		try {
			String s2=RSAUtils.encryptByPublicKey(URLEncoder.encode(s), EncryptFilter.RSA_PUB_KEY_CLIENT);
			super.write(s2);
			System.out.println(s2);
			String plainData = RSAUtils.decryptByPrivateKey(s2, EncryptFilter.RSA_PRI_KEY_CLIENT);
			plainData=URLDecoder.decode(plainData);
			System.out.println(plainData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// add by guang 增加response内容加密处理		
	}

	/**
	* @Title: getContent
	* @author: 王振广
	* @Description: 获取PrintWriter流的文本内容String
	* @param @return    设定文件
	* @return String    返回类型
	*/ 
	public String getContent() {
		return buffer.toString();
	}

}
