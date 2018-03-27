package com.guoyicap.util;

import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

import org.apache.commons.httpclient.NoHttpResponseException;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.jeecgframework.core.util.LogUtil;
import org.springframework.web.multipart.MultipartFile;

/**
 * 依赖的jar包有：commons-lang-2.6.jar、httpclient-4.3.2.jar、httpcore-4.3.1.jar、commons
 * -io-2.4.jar
 * 
 * @author 王振广
 * @remark 调用SOAP接口报错，暂时不使用，改用HttpXmlClient
 *
 */
public class HttpClientUtils {

	public static final int connTimeout = 60000;// 连接超时
	public static final int readTimeout = 60000;// 请求超时
	public static final String charset = "UTF-8";// 字符编码
	private static final String APPLICATION_JSON="application/json";//header json
	private static final String CONTENT_TYPE_TEXT_JSON = "text/json";//content json
	private final static Object syncLock = new Object();//锁
	private static CloseableHttpClient httpClient = null;// http客户端
	private static PoolingHttpClientConnectionManager cm;//连接池
	private static HttpRequestRetryHandler httpRequestRetryHandler;//请求重试处理


	/**
	 * @throws GeneralSecurityException   
	* @Title: createHttpClient  
	* @Author:王振广
	* @Description:获取HttpClient对象 
	* @return    设定文件  
	* @return CloseableHttpClient    返回类型  
	* @throws  
	*/
	public static CloseableHttpClient createHttpClient(){
		/*
		 * PoolingHttpClientConnectionManager cm = new
		 * PoolingHttpClientConnectionManager();// http连接池管理
		 * cm.setMaxTotal(128);// 最大连接数 cm.setDefaultMaxPerRoute(128);// 最大路由数
		 * client = HttpClients.custom().setConnectionManager(cm).build();//
		 * 创建连接client
		 */

		ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();
		//LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory.getSocketFactory();
		LayeredConnectionSocketFactory sslsf;
		try {
			sslsf = createSSLConnectionSocketFactory();
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			sslsf = SSLConnectionSocketFactory.getSocketFactory();
		}
		Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory> create()
				.register("http", plainsf).register("https", sslsf).build();
		//PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
		cm = new PoolingHttpClientConnectionManager(registry);
		// 将最大连接数增加到200
		cm.setMaxTotal(200);
		// 将每个路由基础的连接增加到40
		cm.setDefaultMaxPerRoute(40);
		// 将目标主机的最大连接数增加到50
		//HttpHost localhost = new HttpHost(hostname, port);
		//cm.setMaxPerRoute(new HttpRoute(localhost), maxRoute);

		// 请求重试处理
		httpRequestRetryHandler = new HttpRequestRetryHandler() {
			public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
				if (executionCount >= 5) {// 如果已经重试了5次，就放弃
					return false;
				}
				if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
					return true;
				}
				if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
					return false;
				}
				if (exception instanceof InterruptedIOException) {// 超时
					return false;
				}
				if (exception instanceof UnknownHostException) {// 目标服务器不可达
					return false;
				}
				if (exception instanceof ConnectTimeoutException) {// 连接被拒绝
					return false;
				}
				if (exception instanceof SSLException) {// ssl握手异常
					return false;
				}

				HttpClientContext clientContext = HttpClientContext.adapt(context);
				HttpRequest request = clientContext.getRequest();
				// 如果请求是幂等的，就再次尝试
				if (!(request instanceof HttpEntityEnclosingRequest)) {
					return true;
				}
				return false;
			}
		};
		//获取httpClient
		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm)
				.setRetryHandler(httpRequestRetryHandler).build();
		return httpClient;
	}
	
	
	/**  
	* @Title: setCustomerMaxPerRoute  
	* @Author:王振广
	* @Description: 自定义连接池  的目标主机的最大连接数
	* @param host 目标主机host
	* @param port 端口
	* @param maxRoute 目标主机的最大连接数
	* @return void    返回类型  
	* @throws  
	*/
	public static void setCustomerMaxPerRoute(String host,int port,int maxRoute){
		HttpHost localhost = new HttpHost(host, port);
		cm.setMaxPerRoute(new HttpRoute(localhost), maxRoute);
	}
	
	/**  
	* @Title: getHttpClient  
	* @Author:王振广
	* @Description: TODO(这里用一句话描述这个方法的作用)  
	* @return    设定文件  
	* @return CloseableHttpClient    返回类型  
	* @throws  
	*/
	public static CloseableHttpClient getHttpClient(String url) {
		if (httpClient == null) {
            synchronized (syncLock) {
                if (httpClient == null) {
                    httpClient = createHttpClient();
                    //自定义连接池  的目标主机的最大连接数
					if (url != null) {					
						try {
							URL URL = new URL(url);
							String host = URL.getHost();
							int port = URL.getPort() == -1 ? 80 : URL.getPort();
							//设置目标主机的最大连接数
							setCustomerMaxPerRoute(host, port, 100);
						} catch (MalformedURLException e) {
							LogUtil.error("URL解析错误", e);
						}

					}
                    
                }
            }
        }
        return httpClient;
	}
	
	/**  
	* @Title: config  
	* @Author:王振广
	* @Description: 设置HttpConfig
	* @param httpRequestBase    设定文件  
	* @return void    返回类型  
	* @throws  
	*/
	private static void config(HttpRequestBase httpRequestBase) {
		httpRequestBase.setHeader("User-Agent", "Mozilla/5.0");
		httpRequestBase.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		httpRequestBase.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");// "en-US,en;q=0.5");
		httpRequestBase.setHeader("Accept-Charset", "ISO-8859-1,utf-8,gbk,gb2312;q=0.7,*;q=0.7");

		// 配置请求的超时设置
		RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(connTimeout).setConnectTimeout(connTimeout)
				.setSocketTimeout(connTimeout).build();
		httpRequestBase.setConfig(requestConfig);
	}

	/**
	 * 发送post请求
	 * @param url地址
	 * @param parameterStr参数
	 * @return String
	 * @throws ConnectTimeoutException
	 * @throws SocketTimeoutException
	 * @throws Exception
	 */
	public static String postParameters(String url, String parameterStr)
			throws ConnectTimeoutException, SocketTimeoutException, Exception {
		return post(url, parameterStr, "application/x-www-form-urlencoded", charset, connTimeout, readTimeout);
	}

	/**
	 * 发送post请求
	 * @param url地址
	 * @param parameterStr参数
	 * @param charset字符编码
	 * @param connTimeout连接超时
	 * @param readTimeout请求超时
	 * @return String
	 * @throws ConnectTimeoutException
	 * @throws SocketTimeoutException
	 * @throws Exception
	 */
	public static String postParameters(String url, String parameterStr, String charset, Integer connTimeout,
			Integer readTimeout) throws ConnectTimeoutException, SocketTimeoutException, Exception {
		return post(url, parameterStr, "application/x-www-form-urlencoded", charset, connTimeout, readTimeout);
	}

	/**
	 * 发送post请求，post表单提交
	 * @param url地址
	 * @param params参数
	 * @return String
	 * @throws ConnectTimeoutException
	 * @throws SocketTimeoutException
	 * @throws Exception
	 */
	public static String postParameters(String url, Map<String, Object> params)
			throws ConnectTimeoutException, SocketTimeoutException, Exception {
		return postForm(url, params, null, connTimeout, readTimeout);
	}

	/**
	 * 发送post请求，post表单提交
	 * @param url
	 * @param params
	 * @param connTimeout
	 * @param readTimeout
	 * @return String
	 * @throws ConnectTimeoutException
	 * @throws SocketTimeoutException
	 * @throws Exception
	 */
	public static String postParameters(String url, Map<String, Object> params, Integer connTimeout,
			Integer readTimeout) throws ConnectTimeoutException, SocketTimeoutException, Exception {
		return postForm(url, params, null, connTimeout, readTimeout);
	}

	/**
	 * 发送get请求
	 * @param url
	 * @return String
	 * @throws Exception
	 */
	public static String get(String url) throws Exception {
		return get(url, charset, connTimeout, readTimeout);
	}
	
	/**
	 * 发送get请求
	 * @param url
	 * @return String
	 * @throws Exception
	 */
	public static String get(String url,Map<String, String> querys) throws Exception {
		return get(url, querys,null,charset, connTimeout, readTimeout);
	}
	
	/**
	 * 发送get请求
	 * @param url
	 * @return String
	 * @throws Exception
	 */
	public static String get(String url,Map<String, String> querys,Map<String, String> headers) throws Exception {
		return get(url, querys,headers,charset, connTimeout, readTimeout);
	}

	/**发送get请求
	 * @param url
	 * @param charset
	 * @return String
	 * @throws Exception
	 */
	public static String get(String url, String charset) throws Exception {
		return get(url, charset, connTimeout, readTimeout);
	}

	/**
	 * 发送一个 Post 请求, 使用指定的字符集编码.
	 * 
	 * @param url
	 * @param body
	 *            RequestBody
	 * @param mimeType
	 *            例如 application/xml "application/x-www-form-urlencoded"
	 *            a=1&b=2&c=3
	 * @param charset
	 *            编码
	 * @param connTimeout
	 *            建立链接超时时间,毫秒.
	 * @param readTimeout
	 *            响应超时时间,毫秒.
	 * @return ResponseBody, 使用指定的字符集编码.
	 * @throws ConnectTimeoutException
	 *             建立链接超时异常
	 * @throws SocketTimeoutException
	 *             响应超时
	 * @throws Exception
	 */
	public static String post(String url, String body, String mimeType, String charset, Integer connTimeout,
			Integer readTimeout) throws ConnectTimeoutException, SocketTimeoutException, Exception {
		CloseableHttpClient client = getHttpClient(url);
		HttpPost post = new HttpPost(url);
		String result = "";
		CloseableHttpResponse response=null;
		try {
			if (StringUtils.isNotBlank(body)) {
				HttpEntity entity = new StringEntity(body, ContentType.create(mimeType, charset));
				post.setEntity(entity);
			}
			// 设置参数
			config(post);
			/*Builder customReqConf = RequestConfig.custom();
			if (connTimeout != null) {
				customReqConf.setConnectTimeout(connTimeout);
			}
			if (readTimeout != null) {
				customReqConf.setSocketTimeout(readTimeout);
			}
			post.setConfig(customReqConf.build());*/

			response = client.execute(post);
			//result = IOUtils.toString(res.getEntity().getContent(), charset);
			HttpEntity entity=response.getEntity();			
			result = EntityUtils.toString(entity, charset);
			EntityUtils.consume(entity);
		} finally {
			/*post.releaseConnection();
			if (client != null && client instanceof CloseableHttpClient) {
				((CloseableHttpClient) client).close();
			}*/
			if(response !=null){
				response.close();
			}
		}
		return result;
	}

	/**
	 * 提交form表单
	 * 
	 * @param url
	 * @param params
	 * @param connTimeout
	 * @param readTimeout
	 * @return
	 * @throws ConnectTimeoutException
	 * @throws SocketTimeoutException
	 * @throws Exception
	 */
	public static String postForm(String url, Map<String, Object> params, Map<String, String> headers,
			Integer connTimeout, Integer readTimeout)
					throws ConnectTimeoutException, SocketTimeoutException, Exception {

		CloseableHttpClient client = getHttpClient(url);
		HttpPost post = new HttpPost(url);
		CloseableHttpResponse response=null;
		String result="";
		try {
			if (params != null && !params.isEmpty()) {
				List<NameValuePair> formParams = new ArrayList<org.apache.http.NameValuePair>();
				Set<Entry<String, Object>> entrySet = params.entrySet();
				for (Entry<String, Object> entry : entrySet) {
					formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
				}
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParams, Consts.UTF_8);
				post.setEntity(entity);
			}

			if (headers != null && !headers.isEmpty()) {
				for (Entry<String, String> entry : headers.entrySet()) {
					post.addHeader(entry.getKey(), entry.getValue());
				}
			}
			// 设置参数
			config(post);
			/*Builder customReqConf = RequestConfig.custom();
			if (connTimeout != null) {
				customReqConf.setConnectTimeout(connTimeout);
			}
			if (readTimeout != null) {
				customReqConf.setSocketTimeout(readTimeout);
			}
			post.setConfig(customReqConf.build());*/
			//HttpResponse res = null;
			/*if (url.startsWith("https")) {
				// 执行 Https 请求.
				client = createSSLInsecureClient();
				res = client.execute(post);
			} else {
				// 执行 Http 请求.
				client = HttpClientUtils.client;
				res = client.execute(post);
			}*/
			response = client.execute(post);
			//return IOUtils.toString(res.getEntity().getContent(), "UTF-8");
			HttpEntity entity=response.getEntity();
			result= EntityUtils.toString(entity, charset);
			EntityUtils.consume(entity);
			
		} finally {
			/*post.releaseConnection();
			if (url.startsWith("https") && client != null && client instanceof CloseableHttpClient) {
				((CloseableHttpClient) client).close();
			}*/
			if(response !=null){
				response.close();
			}
		}
		return result;
	}
	
	
	/**  
	* @Title: postForm  
	* @Author:王振广
	* @Description: 发送 post请求（带文件） 
	* @param url
	* @param params
	* @param headers
	* @param fileMap
	* @param connTimeout
	* @param readTimeout
	* @return
	* @throws ConnectTimeoutException
	* @throws SocketTimeoutException
	* @throws Exception    设定文件  
	* @return String    返回类型  
	* @throws  
	*/
	public static String postForm(String url, Map<String, Object> params, Map<String, String> headers,
			 Map<String,File> fileMap,
			Integer connTimeout, Integer readTimeout)
					throws ConnectTimeoutException, SocketTimeoutException, Exception {

		CloseableHttpClient client = getHttpClient(url);
		HttpPost post = new HttpPost(url);
		CloseableHttpResponse response=null;
		String result="";
		try {
			// 添加 post的String 和File数据
			// MultipartEntity entity = new MultipartEntity();
			MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
			multipartEntityBuilder=multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			multipartEntityBuilder.setCharset(Charset.forName(charset));
			//必须设置ContentType 为MULTIPART_FORM_DATA ，否则服务器端接收不到文件参数multipartRequest.getFileMap()
			multipartEntityBuilder.setContentType(ContentType.MULTIPART_FORM_DATA);
			//参数params
			if (params != null && !params.isEmpty()) {
				for (String key : params.keySet()) {
					multipartEntityBuilder.addPart(key, new StringBody(params.get(key).toString(), ContentType.TEXT_PLAIN));
				}
			}

			// 发送的文件
			if (fileMap != null) {
				for (String key : fileMap.keySet()) {
					File file = fileMap.get(key);
					multipartEntityBuilder.addBinaryBody(key, fileMap.get(key));

					// FileBody fileBody = new FileBody(file);
					// multipartEntityBuilder.addPart(key, fileBody);
				}
			}

			//设置 请求内容HttpEntity
			post.setEntity(multipartEntityBuilder.build());
			
			//headers
			if (headers != null && !headers.isEmpty()) {
				for (Entry<String, String> entry : headers.entrySet()) {
					post.addHeader(entry.getKey(), entry.getValue());
				}
			}
			// 设置参数
			config(post);
			/*Builder customReqConf = RequestConfig.custom();
			if (connTimeout != null) {
				customReqConf.setConnectTimeout(connTimeout);
			}
			if (readTimeout != null) {
				customReqConf.setSocketTimeout(readTimeout);
			}
			post.setConfig(customReqConf.build());*/
			//HttpResponse res = null;
			/*if (url.startsWith("https")) {
				// 执行 Https 请求.
				client = createSSLInsecureClient();
				// 发送的数据
				res = client.execute(post);
			} else {
				// 执行 Http 请求.
				client = HttpClientUtils.client;
				// 发送的数据
				res = client.execute(post);
			}*/
			response = client.execute(post);
			//return IOUtils.toString(res.getEntity().getContent(), "UTF-8");
			//return EntityUtils.toString(res.getEntity(), "UTF-8");
			HttpEntity entity=response.getEntity();
			result= EntityUtils.toString(entity, charset);
			EntityUtils.consume(entity);
			
		} finally {
			/*post.releaseConnection();
			if (url.startsWith("https") && client != null && client instanceof CloseableHttpClient) {
				((CloseableHttpClient) client).close();
			}*/
			if(response !=null){
				response.close();
			}
		}
		return result;
	}
	
	
	/**
	 * @throws IOException   
	* @Title: postForm2  
	* @Author:王振广
	* @Description: 发送 post请求（带文件MultipartFile） 
	* @param url
	* @param params
	* @param headers
	* @param multipartFileMap  ((MultipartHttpServletRequest)request).getFileMap()
	* @param connTimeout
	* @param readTimeout
	* @return
	* @throws ConnectTimeoutException
	* @throws SocketTimeoutException
	* @throws Exception    设定文件  
	* @return String    返回类型  
	* @throws  
	*/
	public static String postForm2(String url, Map<String, Object> params, Map<String, String> headers,
			Map<String, MultipartFile> multipartFileMap, Integer connTimeout, Integer readTimeout) throws IOException {

		CloseableHttpClient client = getHttpClient(url);
		HttpPost post = new HttpPost(url);
		CloseableHttpResponse response=null;
		String result="";
		try {
			// 添加 post的String 和MultipartHttpServletRequest.getFileMap()数据
			// MultipartEntity entity = new MultipartEntity();
			MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
			multipartEntityBuilder = multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			multipartEntityBuilder.setCharset(Charset.forName(charset));
			//必须设置ContentType 为MULTIPART_FORM_DATA ，否则服务器端接收不到文件参数multipartRequest.getFileMap()
			multipartEntityBuilder.setContentType(ContentType.MULTIPART_FORM_DATA);
			// 参数params
			if (params != null && !params.isEmpty()) {
				for (String key : params.keySet()) {
					multipartEntityBuilder.addPart(key, new StringBody(params.get(key).toString(), ContentType.TEXT_PLAIN));
				}
			}

			// 发送的MultipartHttpServletRequest.getFileMap()
			if (multipartFileMap != null) {
				for (String key : multipartFileMap.keySet()) {
					multipartEntityBuilder.addBinaryBody(key, multipartFileMap.get(key).getInputStream());
				}
			}
			//设置 请求内容HttpEntity
			 // 生成 HTTP 实体
	        HttpEntity httpEntity = multipartEntityBuilder.build();
			post.setEntity(httpEntity);

			// headers
			if (headers != null && !headers.isEmpty()) {
				for (Entry<String, String> entry : headers.entrySet()) {
					post.addHeader(entry.getKey(), entry.getValue());
				}
			}
			// 设置参数
			config(post);
			/*Builder customReqConf = RequestConfig.custom();
			if (connTimeout != null) {
				customReqConf.setConnectTimeout(connTimeout);
			}
			if (readTimeout != null) {
				customReqConf.setSocketTimeout(readTimeout);
			}
			post.setConfig(customReqConf.build());*/
			HttpResponse res = null;
			/*if (url.startsWith("https")) {
				// 执行 Https 请求.
				client = createSSLInsecureClient();
				// 发送的数据
				try {
					res = client.execute(post);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				// 执行 Http 请求.
				client = HttpClientUtils.client;
				// 发送的数据
				res = client.execute(post);
			}*/
			response = client.execute(post);
			// return IOUtils.toString(res.getEntity().getContent(), "UTF-8");
			//return EntityUtils.toString(res.getEntity(), "UTF-8");
			HttpEntity entity=response.getEntity();
			result= EntityUtils.toString(entity, charset);
			EntityUtils.consume(entity);
		} finally {
			/*post.releaseConnection();
			if (url.startsWith("https") && client != null && client instanceof CloseableHttpClient) {
				try {
					((CloseableHttpClient) client).close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}*/
			if(response !=null){
				response.close();
			}
		}
		return result;
	}
	

	/**  
	* @Title: get  
	* @Author:王振广
	* @Description: get 请求
	* @param url
	* @param querys
	* @param headers
	* @param charset
	* @param connTimeout
	* @param readTimeout
	* @return
	* @throws ConnectTimeoutException
	* @throws SocketTimeoutException
	* @throws Exception    设定文件  
	* @return String    返回类型  
	* @throws  
	*/
	public static String get(String url, Map<String, String> querys,Map<String, String> headers,String charset, Integer connTimeout, Integer readTimeout)
			throws ConnectTimeoutException, SocketTimeoutException, Exception {

		CloseableHttpClient client = getHttpClient(url);
		
		String result = "";
		CloseableHttpResponse response=null;
		//querys
		if (null != querys) {
			StringBuilder urlbuilder=new StringBuilder(url);
    		StringBuilder sbQuery = new StringBuilder();
        	for (Map.Entry<String, String> query : querys.entrySet()) {
        		if (0 < sbQuery.length()) {
        			sbQuery.append("&");
        		}
        		if (StringUtils.isBlank(query.getKey()) && !StringUtils.isBlank(query.getValue())) {
        			sbQuery.append(query.getValue());
                }
        		if (!StringUtils.isBlank(query.getKey())) {
        			sbQuery.append(query.getKey());
        			if (!StringUtils.isBlank(query.getValue())) {
        				sbQuery.append("=");
        				sbQuery.append(URLEncoder.encode(query.getValue(), charset));
        			}        			
                }
        	}
        	if (0 < sbQuery.length()) {
        		if(url.indexOf("?")<0){
        			urlbuilder.append("?").append(sbQuery);
        		}else{
        			urlbuilder.append("&").append(sbQuery);
        		}        		
        	}
        	
        	url=urlbuilder.toString();
        }
		
		HttpGet get = new HttpGet(url);
		try {
			// 设置参数
			//config(get);
			Builder customReqConf = RequestConfig.custom();
			if (connTimeout != null) {
				customReqConf.setConnectTimeout(connTimeout);
			}
			if (readTimeout != null) {
				customReqConf.setSocketTimeout(readTimeout);
			}			
			get.setConfig(customReqConf.build());
			
			//设置请求类型
			//get.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
			//Header
			if(headers != null){
				for (Map.Entry<String, String> e : headers.entrySet()) {
					get.addHeader(e.getKey(), e.getValue());
		        }
			}

			//HttpResponse res = null;

			/*if (url.startsWith("https")) {
				// 执行 Https 请求.
				client = createSSLInsecureClient();
				res = client.execute(get);
			} else {
				// 执行 Http 请求.
				client = HttpClientUtils.client;
				res = client.execute(get);
			}*/
			response = client.execute(get);
			//result = IOUtils.toString(res.getEntity().getContent(), charset);
			HttpEntity entity=response.getEntity();
			result = EntityUtils.toString(entity, charset);
			EntityUtils.consume(entity);
		} finally {
			/*get.releaseConnection();
			if (url.startsWith("https") && client != null && client instanceof CloseableHttpClient) {
				((CloseableHttpClient) client).close();
			}*/
			if(response != null){
				response.close();
			}
		}
		return result;
	}
	
	/**
	 * 发送一个 GET 请求
	 * 
	 * @param url
	 * @param charset
	 * @param connTimeout
	 *            建立链接超时时间,毫秒.
	 * @param readTimeout
	 *            响应超时时间,毫秒.
	 * @return
	 * @throws ConnectTimeoutException
	 *             建立链接超时
	 * @throws SocketTimeoutException
	 *             响应超时
	 * @throws Exception
	 */
	public static String get(String url, String charset, Integer connTimeout, Integer readTimeout)
			throws ConnectTimeoutException, SocketTimeoutException, Exception {

		return get(url, null, null, charset, connTimeout, readTimeout);
	}

	/**
	 * 从 response 里获取 charset
	 * 
	 * @param ressponse
	 * @return
	 */
	@SuppressWarnings("unused")
	private static String getCharsetFromResponse(HttpResponse ressponse) {
		// Content-Type:text/html; charset=GBK
		if (ressponse.getEntity() != null && ressponse.getEntity().getContentType() != null
				&& ressponse.getEntity().getContentType().getValue() != null) {
			String contentType = ressponse.getEntity().getContentType().getValue();
			if (contentType.contains("charset=")) {
				return contentType.substring(contentType.indexOf("charset=") + 8);
			}
		}
		return null;
	}

	/**
	 * 创建 SSL连接
	 * 
	 * @return
	 * @throws GeneralSecurityException
	 */
	private static CloseableHttpClient createSSLInsecureClient() throws GeneralSecurityException {
		try {
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					return true;
				}
			}).build();

			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new X509HostnameVerifier() {

				@Override
				public boolean verify(String arg0, SSLSession arg1) {
					return true;
				}

				@Override
				public void verify(String host, SSLSocket ssl) throws IOException {
				}

				@Override
				public void verify(String host, X509Certificate cert) throws SSLException {
				}

				@Override
				public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
				}

			});

			return HttpClients.custom().setSSLSocketFactory(sslsf).build();

		} catch (GeneralSecurityException e) {
			throw e;
		}
	}

	
	/**  
	* @Title: createSSLConnectionSocketFactory  
	* @Author:王振广
	* @Description:构造 SSLConnectionSocketFactory
	* @return
	* @throws GeneralSecurityException    设定文件  
	* @return SSLConnectionSocketFactory    返回类型  
	* @throws  
	*/
	private static SSLConnectionSocketFactory createSSLConnectionSocketFactory() throws GeneralSecurityException {
		try {
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					return true;
				}
			}).build();

			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new X509HostnameVerifier() {

				@Override
				public boolean verify(String arg0, SSLSession arg1) {
					return true;
				}

				@Override
				public void verify(String host, SSLSocket ssl) throws IOException {
				}

				@Override
				public void verify(String host, X509Certificate cert) throws SSLException {
				}

				@Override
				public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
				}

			});

			return sslsf;

		} catch (GeneralSecurityException e) {
			throw e;
		}
	}


	
}
