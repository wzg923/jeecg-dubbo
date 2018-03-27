package com.guoyicap.util.HtmlConvertUtil;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 网页抓取 为HTML静态页
 * 工具类
 * @author guang
 *
 */
public class HTMLGenerator {


	/** */
	/**
	 * 通过网站域名URL获取该网站的源码
	 * 
	 * @param url
	 * @param charsetName 字符编码
	 * @return String
	 * @throws Exception
	 */
	public static String getURLSource(URL url ,String charsetName) throws Exception {
		if(null==charsetName || "".equals(charsetName)){
			charsetName="utf-8";
		}
		HttpURLConnection conn = null;
		String respond = "";
		try {
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestProperty("Accept-Charset", charsetName);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charsetName);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			
			//conn.setRequestProperty("Content-type", "text/html");
			//conn.setRequestProperty("Accept-Charset", "utf-8");
			conn.setRequestProperty("contentType", charsetName);
			
			conn.setRequestProperty("User-agent",
					"Mozilla/5.0 (Linux; Android 4.2.1; Nexus 7 Build/JOP40D) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166  Safari/535.19");
			conn.connect();

			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),charsetName));
			String lines = "";
			while ((lines = reader.readLine()) != null) {
				//respond += lines+"\n";
				respond += URLEncoder.encode(lines+"\n",charsetName);//中文编码
			}
			reader.close();
			conn.disconnect();
		} catch (Exception e) {
			respond = e.getMessage();

		} finally {
			if (conn != null) {
				conn.disconnect();
			}
			conn = null;
		}
		return respond;
		// conn.setRequestMethod("GET");
		// conn.setConnectTimeout(5 * 1000);
		// conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE
		// 5.0; Windows NT; DigExt)");
		// conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE
		// 5.0; Windows NT; DigExt)");
		// conn.setRequestProperty("accept", "*/*");
		// conn.setRequestProperty("connection", "Keep-Alive");
		// conn.setRequestProperty("user-agent",
		// "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
		// InputStream inStream = conn.getInputStream(); // 通过输入流获取html二进制数据
		// byte[] data = readInputStream(inStream); // 把二进制数据转化为byte字节数据
		// String htmlSource = new String(data);
		// return htmlSource;
	}
	
	
	/**
	 * 通过网站域名URL获取该网站的源码  
	 * @param url
	 * @param mothod
	 * @param charsetName
	 * @return
	 * @throws Exception
	 */
	public static String getURLSource(URL url ,String mothod,String charsetName) throws Exception {
		if(null==charsetName || "".equals(charsetName)){
			charsetName="utf-8";
		}
		HttpURLConnection conn = null;
		String respond = "";
		try {
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestProperty("Accept-Charset", charsetName);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charsetName);
			conn.setUseCaches(false);
			//GET / POST
			conn.setRequestMethod(mothod);
			conn.setRequestProperty("User-agent",
					"Mozilla/5.0 (Linux; Android 4.2.1; Nexus 7 Build/JOP40D) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166  Safari/535.19");
			conn.connect();

			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String lines = "";
			while ((lines = reader.readLine()) != null) {
				respond += lines+"\n";
			}
			reader.close();
			conn.disconnect();
		} catch (Exception e) {
			respond = e.getMessage();

		} finally {
			if (conn != null) {
				conn.disconnect();
			}
			conn = null;
		}
		return respond;
	}
	
	
	/**
	 * 根据url抓取网页，将返回的HTML流写入文件
	 * @param urlString 访问url
	 * @param charsetName 字符编码 默认utf-8
	 * @param file 写入的文件
	 * @return flag  [success|error]
	 */
	public static String generatorUrlToHtmlFile(String urlString,String charsetName,File file){
		String flag="success";
		 URL url;
		try {
			url = new URL(urlString);//URL对象
			String src = null;
			try {
				src = HTMLGenerator.getURLSource(url, charsetName);//抓取网页
			} catch (Exception e) {
				flag="error";
				e.printStackTrace();
			}
			//add by guang  增加jsp头部编码
			try {
				src=URLDecoder.decode(src,charsetName);
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}//html解码
			
			
			//end
			
			try {
				FileUtils.writeStringToFile(file, src, charsetName);//写入文件
			} catch (IOException e) {
				flag="error";
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			flag="error";
			e.printStackTrace();
		}	
		
		return flag;
	}

	/** */
	/**
	 * 把二进制流转化为byte字节数组
	 * 
	 * @param instream
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] readInputStream(InputStream instream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1204];
		int len = 0;
		while ((len = instream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		instream.close();
		return outStream.toByteArray();
	}

	 
	 
	/**
	 * Test Code Target : http://www.google.cn/
	 * 
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		//URL url = new URL("http://localhost:8080/exam/testOrderEntityController.do?addorupdate&id=6");
		 URL url = new URL("http://192.168.50.182:8080/rest/userService");
		// String url = "http://192.168.10.105:8080/qianba/industryNews.do?content";
		String src = HTMLGenerator.getURLSource(url,"GET", "utf-8");
		System.out.println(src);
		//File file = new File("D:" + File.separator + "index.html");
		//FileUtils.writeStringToFile(file, src, "utf-8");
	}
}
