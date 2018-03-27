package com.guoyicap.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.guoyicap.util.MyException;  
  
  
/**
 * @author 王振广
 * @describe 请求SOAP协议web service,返回数据XML格式/JSON格式/其他
 * @date 2015-12-09
 * @version v1.0
 */
public class HttpXmlClient {  
    private static Logger log = Logger.getLogger(HttpXmlClient.class);  
      
    /**
     * POST请求
     * @param url
     * @param params
     * @param charset
     * @return
     * @throws MyException 
     */
    public static String post(String url, Map<String, String> params,String charset) throws MyException { 
    	CloseableHttpClient httpclient = HttpClients.createDefault();
        String body = null; 
        if(charset==null || "".equals(charset)){
        	charset="UTF-8";
        }          
        log.info("create httppost:" + url); 
        //封装post请求
        HttpPost post = postForm(url, params,charset);  
        //执行请求  
        try {
			body = invoke(httpclient, post);
		} catch (IOException e) {
			log.error("执行post请求失败",e);
			throw new MyException("执行post请求失败",e);
		}  
        return body;  
    }  
      
    /**
     * GET方式请求
     * @param url
     * @return
     * @throws MyException 
     */
    public static String get(String url,String charset) throws MyException {  
    	CloseableHttpClient httpclient = HttpClients.createDefault();  
        String body = null;  
        if(charset==null || "".equals(charset)){
        	charset="UTF-8";
        }  
        log.info("create httppost:" + url); 
        //HttpGet请求
        HttpGet get = new HttpGet(url); 
        //执行请求
        try {
			body = invoke(httpclient, get);
		} catch (IOException e) {
			log.error("执行post请求失败",e);
			throw new MyException("执行post请求失败",e);
		}  
          
        return body;  
    }  
          
      
    /**
     * 执行请求连接
     * @param httpclient
     * @param httpost
     * @return
     * @throws IOException 
     * @throws ClientProtocolException 
     */
	private static String invoke(CloseableHttpClient httpclient, HttpUriRequest request)
			throws ClientProtocolException, IOException {
		String body = null;
		// 执行httpPost请求
		CloseableHttpResponse response = sendRequest(httpclient,request);
		//解析response 信息
		body=paseResponse(response);
		return body;
	}  
  
    /**
     * 解析response body
     * @param response
     * @return
     * @throws IOException 
     * @throws ParseException 
     */
    private static String paseResponse(CloseableHttpResponse response) throws ParseException, IOException {  
    	String body = null; 
    	if (response != null) {
			try {
				// response内容Entity
				HttpEntity entity = response.getEntity();
				// 如果请求响应成功
				if (response.getStatusLine().getReasonPhrase().equals("OK")
						&& response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					// response 返回body
					body = EntityUtils.toString(entity, "UTF-8");
				}
				// 关闭资源，流
				EntityUtils.consume(entity);
			} finally {
				// 关闭response连接
				response.close();
			}
		}
          
        return body;  
    }  
  
    /**
     * 发送请求
     * @param httpclient
     * @param httpost
     * @return
     */
    private static CloseableHttpResponse sendRequest(CloseableHttpClient httpclient,  
            HttpUriRequest request) {  
        log.info("execute post...");  
        CloseableHttpResponse response = null;  
        try {  
        	//执行请求
            response = httpclient.execute(request);  
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return response;  
    }  
  
    /**
     * 封装post请求参数
     * @param url
     * @param params
     * @param charset
     * @return
     */
    private static HttpPost postForm(String url, Map<String, String> params,String charset){  
        //创建HttpPost  
        HttpPost httpost = new HttpPost(url); 
        //参数封装
        List<NameValuePair> nvps = new ArrayList <NameValuePair>();  
        //解析参数params map  
        Set<String> keySet = params.keySet();  
        for(String key : keySet) { 
        	//封装NameValuePair
            nvps.add(new BasicNameValuePair(key, params.get(key)));  
        }  
          
        try {  
            log.info("set "+charset+" form entity to httppost");
         	//设置post参数
            httpost.setEntity(new UrlEncodedFormEntity(nvps, charset));  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        }  
          
        return httpost;  
    } 
    
    
    /**
     * 测试
     * @param args
     */
    public static void main (String[] args){
    	Map<String,String> param=new HashMap<String,String>();
    	param.put("sdate", "{'begindate':'2014-11-01 00:00:00','enddate':'2015-12-01 00:00:00'}");
    	String str = null;
		try {
			str = post("http://192.168.7.111:8084/xiaodai.asmx/getfangkuantongji1",
					param,"UTF-8");
		} catch (MyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	System.out.println(str);
    	
    }
} 
