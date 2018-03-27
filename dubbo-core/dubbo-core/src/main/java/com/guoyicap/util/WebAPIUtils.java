package com.guoyicap.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;

import com.guoyicap.core.util.https.HttpsUtil;

/**
 * @describe:WebAPI接口调用工具类
 * @author 王振广
 * @date 2015-12-09
 * @version v1.0
 */
public class WebAPIUtils {
	public static final String GET_Method="GET";
	public static final String POST_Method="POST";
	public static final String PUT_Method="PUT";
	public static final String DELETE_Method="DELETE";
	
	public static final int connTimeout = 60000;// 连接超时 毫秒
	public static final int readTimeout = 60000;// 请求超时 毫秒
	public static final String charset = "UTF-8";// 字符编码

	/**
	 * <ul>调用restful类型接口 HTTP协议
	 * <li>请求方法：GET/POST/PUT/DELETE</li>
	 * <li>字符编码：UTF-8/GBK</li>
	 * </ul>
	 * @param url
	 * @param mothod
	 * @param charsetName
	 * @return
	 * @throws Exception
	 */
	public static String connectRestfulAPI(String url ,String method,Map<String,Object> paramMap,String charsetName) throws Exception {
		if(null==charsetName || "".equals(charsetName)){
			charsetName=charset;
		}
		if(null==method || "".equals(method)){
			method=WebAPIUtils.GET_Method;
		}
		HttpURLConnection conn = null;
		String respond = "";
		BufferedReader reader = null;
		URL URL=new URL(url);
		try {
			conn = (HttpURLConnection) URL.openConnection();
			conn.setDoOutput(true);
			conn.setRequestProperty("Accept-Charset", charsetName);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charsetName);
			conn.setUseCaches(false);//缓存false
			conn.setConnectTimeout(connTimeout);//连接超时 单位毫秒
			conn.setReadTimeout(readTimeout);//读取超时 单位毫秒

			//GET / POST/put/delete
			conn.setRequestMethod(StringUtils.upperCase(method));//方法大写
			conn.setRequestProperty("User-agent",
					"Mozilla/5.0 (Linux; Android 4.2.1; Nexus 7 Build/JOP40D) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166  Safari/535.19");
			
			
			//参数传递
			if(paramMap!=null &&!paramMap.isEmpty()){
				conn.setDoOutput(true);// 是否输入参数
				StringBuffer params = new StringBuffer();
				// 表单参数与get形式一样
				for(String key: paramMap.keySet()){
					params.append(key).append("=").append(paramMap.get(key)).append("&");
				}
				
				byte[] bypes = params.toString().getBytes();
				conn.getOutputStream().write(bypes);// 输入参数
			}
			conn.connect();//建立连接
			
			//读取请求返回信息
			reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String lines = "";
			while ((lines = reader.readLine()) != null) {
				respond += lines;
			}
			
			conn.disconnect();//断开连接
		} catch (Exception e) {
			respond = e.getMessage();

		} finally {
			if(reader!=null){
				reader.close();//关闭流
			}
			if (conn != null) {
				conn.disconnect();
			}
			conn = null;
		}
		return respond;
	}
	
	public static String connectHttpsAPI(String url ,String method,Map<String,Object> paramMap,String charsetName) throws Exception{
		return HttpsUtil.Do(url, paramMap, method, charsetName);
	}
	
	/**
	 * <ul>调用SOAP类型web service
	 * <li>请求方法：GET/POST/PUT/DELETE</li>
	 * <li>字符编码：默认UTF-8</li>
	 * </ul>
	 * @param url
	 * @param method
	 * @param params
	 * @return
	 * @throws MyException 
	 */
	public static String connectSoapAPI(String url,String method,Map<String,String> params) throws MyException{
		String body=null;
		if(method.equalsIgnoreCase(GET_Method)){
			//请求GET方式
			body=HttpXmlClient.get(url, charset);
		}else if(method.equalsIgnoreCase(POST_Method)){
			//请求post方式
			body=HttpXmlClient.post(url, params, charset);
		}else if(method.equalsIgnoreCase(PUT_Method)){
			//请求PUT方式
			//TODO
			
		}else if(method.equalsIgnoreCase(DELETE_Method)){
			//请求Delete方式
			//TODO
		}	
		
		return body;
	}
	
	
	
	/**
	 * 测试方法
	 * @param args
	 */
	public static void main(String[] args){
		//Restful webservice 测试
		String url1="http://localhost:8080/qianba/rest/userService/post?id=123123";		
		try {
			String str1=WebAPIUtils.connectRestfulAPI(url1, WebAPIUtils.POST_Method,null, "UTF-8");
			System.out.println(str1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//SOAP webservice 测试
		String url2="http://192.168.7.111:8084/xiaodai.asmx/getfangkuantongji1";
		Map<String,String> param=new HashMap<String,String>();
		param.put("sdate", "{'begindate':'2014-11-01 00:00:00','enddate':'2015-12-01 00:00:00'}");
		try {
			String xmlStr;
			xmlStr=WebAPIUtils.connectSoapAPI(url2, WebAPIUtils.POST_Method, param);
			//System.out.println(xmlStr);
			StringBuffer sb=new StringBuffer();
			//char[] b=new char[1024];
			//new FileInputStream(new File("D:/test.xml"));
			/*BufferedReader fr = new BufferedReader(
					new InputStreamReader(new FileInputStream(new File("D:/test.xml")), "GB2312"));
			while (fr.read()!=-1){
				//sb.append(b);
			}
			xmlStr=sb.toString();*/
			
			//Map<String, Object> map = new HashMap<String, Object>();
			//将xml格式的字符串转换成Document对象
			Document doc = DocumentHelper.parseText(xmlStr);
			//获取根节点
			Element root = doc.getRootElement();
			//获取根节点下的所有元素
			//List children = root.elements();
			//循环所有子元素
			if(root != null && root.hasContent()) {
				for(int i = 0; i < root.content().size(); i++) {
					Object obj=root.content().get(i);
					if(obj.getClass().equals(Namespace.class)){
						System.err.println(((org.dom4j.Namespace)obj).getURI());
					}else if(obj.getClass().equals(org.dom4j.tree.DefaultText.class)){
						org.dom4j.tree.DefaultText defaultText=(org.dom4j.tree.DefaultText)obj;
						String text=defaultText.getText();//内容
						net.sf.json.JSONArray jsonArray=net.sf.json.JSONArray.fromObject(text);
						//读取内容转换为list
						List list=net.sf.json.JSONArray.toList(jsonArray);
						
					}
				}
			}
			//System.out.println(map.toString());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		

		

		
	}
	
}
