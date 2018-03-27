package com.guoyicap.core.util.https;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.StringUtils;

public class HttpsUtil {
	public static final String GET_Method="GET";
	public static final String POST_Method="POST";
	public static final String PUT_Method="PUT";
	public static final String DELETE_Method="DELETE";
	
	public static final int connTimeout = 60000;// 连接超时 毫秒
	public static final int readTimeout = 60000;// 请求超时 毫秒
	public static final String charset = "UTF-8";// 字符编码
	
	
	private static class TrustAnyTrustManager implements X509TrustManager{

		@Override
		public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
        	//TODO  增加可以信任的host
            return true;
        }
    }

	public static String Do(String url, Map<String, Object> paramMap, String method, String charsetName) throws Exception {
		if (null == charsetName || "".equals(charsetName)) {
			charsetName = charset;
		}
		if (null == method || "".equals(method)) {
			method = GET_Method;
		}
		HttpsURLConnection connection = null;
		String result = "";// 响应信息
		BufferedReader in = null;
		try {

			SSLContext sc = SSLContext.getInstance("SSL");
			//sc.init(null, new TrustManager[] { new MyX509TrustManager() }, new java.security.SecureRandom());
			sc.init(new KeyManager[0], new TrustManager[] {new TrustAnyTrustManager()}, new SecureRandom());
		    SSLContext.setDefault(sc);
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			connection = (HttpsURLConnection) realUrl.openConnection();
			// 设置https相关属性
			connection.setSSLSocketFactory(sc.getSocketFactory());
			connection.setHostnameVerifier(new TrustAnyHostnameVerifier());
			connection.setDoOutput(true);

			// 设置通用的请求属性
			connection.setRequestProperty("Accept-Charset", charsetName);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charsetName);
			connection.setUseCaches(false);// 缓存false
			connection.setConnectTimeout(connTimeout);// 连接超时 单位毫秒
			connection.setReadTimeout(readTimeout);// 读取超时 单位毫秒
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// GET / POST/put/delete
			connection.setRequestMethod(StringUtils.upperCase(method));// 方法大写
			// 参数传递
			if (paramMap != null && !paramMap.isEmpty()) {
				// connection.setDoOutput(true);// 是否输入参数
				StringBuffer params = new StringBuffer();
				// 表单参数与get形式一样
				for (String key : paramMap.keySet()) {
					params.append(key).append("=").append(paramMap.get(key)).append("&");
				}

				byte[] bypes = params.toString().getBytes();
				connection.getOutputStream().write(bypes);// 输入参数
			}
			// 建立实际的连接
			connection.connect();

			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			// System.out.println("获取的结果为："+result);
		} catch (Exception e) {
			e.printStackTrace();
			result = e.getMessage();
			throw e;
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
				if (connection != null) {
					connection.disconnect();
				}
				connection = null;
			} catch (Exception e2) {
				e2.printStackTrace();
				throw e2;
			}
		}
		return result;

	}

   /* private String getParamStr()
    {
        String paramStr="";
        // 获取所有响应头字段
        Map<String, String> params = this._params;
        // 获取参数列表组成参数字符串
        for (String key : params.keySet()) {
            paramStr+=key+"="+params.get(key)+"&";
        }
        //去除最后一个"&"
        paramStr=paramStr.substring(0, paramStr.length()-1);
        
        return paramStr;
    }*/
	/*public static void main(String[] args) {
		// 创建SSLContext对象，并使用我们指定的信任管理器初始化
		TrustManager[] tm = { new MyX509TrustManager() };
		SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
		sslContext.init(null, tm, new java.security.SecureRandom());
		// 从上述SSLContext对象中得到SSLSocketFactory对象
		SSLSocketFactory ssf = sslContext.getSocketFactory();
		// 创建URL对象
		URL myURL = new URL("https://ebanks.gdb.com.cn/sperbank/perbankLogin.jsp");
		// 创建HttpsURLConnection对象，并设置其SSLSocketFactory对象
		HttpsURLConnection httpsConn = (HttpsURLConnection) myURL.openConnection();
		httpsConn.setSSLSocketFactory(ssf);
		// 取得该连接的输入流，以读取响应内容
		InputStreamReader insr = new InputStreamReader(httpsConn.getInputStream());
		// 读取服务器的响应内容并显示
		int respInt = insr.read();
		while (respInt != -1) {
			System.out.print((char) respInt);
			respInt = insr.read();
		}
	}*/
}
