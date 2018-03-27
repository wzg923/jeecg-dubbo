package com.guoyicap.core.util.sms;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.guoyicap.util.WebAPIUtils;
/**
 * @ClassName: SendSmsUtil
 * @Description: 发送短信
 * @author 郭凯
 * @date 2016年10月9日 上午8:25:00
 *
 */
public class SendSmsUtil {
	
	/**
	 * 发送短信
	 * @param phone 手机号码
	 * @param content 信息
	 * @return 
	 */
	public static Map<String, String> send(String phone, String content) {

		Map<String, String> resultMap = new HashMap<>();
		String account = "cf_qiche";//账号

		String password = "qiche518";//密码

		try {
			String mothod = WebAPIUtils.POST_Method;//请求方式post
			HttpURLConnection conn = null;
			String respond = "";
			URL URL = new URL("http://106.ihuyi.cn/webservice/sms.php?method=Submit");//接口地址
			try {
				conn = (HttpURLConnection) URL.openConnection();
				conn.setDoOutput(true);
				conn.setRequestProperty("Accept-Charset", WebAPIUtils.charset);
				conn.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded;charset=" + WebAPIUtils.charset);
				conn.setUseCaches(false);// 缓存false

				conn.setRequestMethod(StringUtils.upperCase(mothod));// 方法大写

				// 参数传递
				conn.setDoOutput(true);// 是否输入参数
				// 表单参数与get形式一样
				
				StringBuilder param = new StringBuilder("account=").append(account).append("&password=")
						.append(password).append("&mobile=").append(phone).append("&content=").append(content);

				byte[] bypes = param.toString().getBytes();
				conn.getOutputStream().write(bypes);// 输入参数
				conn.connect();// 建立连接

				// 读取请求返回信息
				BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String lines = "";
				while ((lines = reader.readLine()) != null) {
					respond += lines;
				}
				reader.close();// 关闭流
				conn.disconnect();// 断开连接
			} catch (Exception e) {
				respond = e.getMessage();

			} finally {
				if (conn != null) {
					conn.disconnect();
				}
				conn = null;
			}
			//截取返回结果
			resultMap.put("code", respond.substring(respond.lastIndexOf("<code>") + 6, respond.lastIndexOf("</code>")));
			resultMap.put("msg", respond.substring(respond.lastIndexOf("<msg>") + 5, respond.lastIndexOf("</msg>")));

		} catch (Exception e) {
			resultMap.put("code", "-1");
			resultMap.put("msg", "短信发送异常");
			
		}
		
		return resultMap;

	}
}
