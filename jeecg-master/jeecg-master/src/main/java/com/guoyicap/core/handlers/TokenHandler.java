package com.guoyicap.core.handlers;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jeecgframework.core.util.DateUtils;
import org.jeecgframework.core.util.LogUtil;
import org.jeecgframework.core.util.ResourceUtil;

/**
 * @author guang 服务器口令 handler
 *         <p>
 *         生成服务器口令，验证客户端请求口令
 *         <p>
 * 		@since[2015/11/16]
 *         <p>
 * 		@version[v1.0]
 *         <p>
 */
public class TokenHandler {
	//口令Map集合
	static Map<String, String> springmvc_token = null;
	//默认口令信息返回页面
	public static String DEFAULT_TOKEN_MSG_JSP = "loginController.do?noAuth";
	//口令value值
	public static String TOKEN_VALUE;
	//默认口令名称
	public static String DEFAULT_TOKEN_NAME = "_SPRINGMVC_TOKEN";

	// 生成一个唯一值的token
	@SuppressWarnings("unchecked")
	public synchronized static String generateGUID(HttpSession session) {
		String token = "";//口令
		try {
			//从session中获取口令信息
			Object obj = session.getAttribute(DEFAULT_TOKEN_NAME);
			if (obj != null){//口令非空
				springmvc_token = (Map<String, String>) session.getAttribute(DEFAULT_TOKEN_NAME);
			}
			else{//口令为空，则初始化
				springmvc_token = new HashMap<String, String>();
			}
			//生成口令
			token = new BigInteger(165, new Random()).toString(36).toUpperCase();
			//将口令存储到口令Map中
			springmvc_token.put(DEFAULT_TOKEN_NAME + "." + token, token);
			//将口令信息缓存到session
			session.setAttribute(DEFAULT_TOKEN_NAME, springmvc_token);
			//口令值
			TOKEN_VALUE = token;

		} catch (IllegalStateException e) {
			LogUtil.error(TokenHandler.class + ":generateGUID() mothod find bug,by token session...");
		}
		return token;
	}

	// 验证表单token值和session中的token值是否一致
	@SuppressWarnings("unchecked")
	public static boolean validToken(HttpServletRequest request) {
		//获取表单中的token值
		String inputToken = getInputToken(request);
		//如果口令为空，则验证失败
		if (inputToken == null) {
			LogUtil.warning("token is not valid!inputToken is NULL");
			return false;
		}
		//获取session中缓存口令map
		HttpSession session = request.getSession();
		Map<String, String> tokenMap = (Map<String, String>) session.getAttribute(DEFAULT_TOKEN_NAME);
		//session中口令信息为null
		if (tokenMap == null || tokenMap.size() < 1) {
			LogUtil.warning("token is not valid!sessionToken is NULL");
			return false;
		}
		//获取页面口令
		String sessionToken = tokenMap.get(DEFAULT_TOKEN_NAME + "." + inputToken);
		//比较口令是否相等
		if (!inputToken.equals(sessionToken)) {//口令不相等，验证失败
			LogUtil.warning("token is not valid!inputToken='" + inputToken + "',sessionToken = '" + sessionToken + "'");
			return false;
		}
		//口令验证成功后，移除原始口令，口令失效
		tokenMap.remove(DEFAULT_TOKEN_NAME + "." + inputToken);
		//缓存口令map
		session.setAttribute(DEFAULT_TOKEN_NAME, tokenMap);
		return true;
	}

	// 获取表单中token值
	@SuppressWarnings("unchecked")
	public static String getInputToken(HttpServletRequest request) {
		//获取request参数
		Map params = request.getParameterMap();
		//判断是否存在口令map
		if (!params.containsKey(DEFAULT_TOKEN_NAME)) {
			LogUtil.warning("Could not find token name in params.");
			return null;
		}
		//获取口令数组
		String[] tokens = (String[]) params.get(DEFAULT_TOKEN_NAME);

		if ((tokens == null) || (tokens.length < 1)) {
			LogUtil.warning("Got a null or empty token name.");
			return null;
		}
		//返回第一个口令元素
		return tokens[0];
	}
	
	
	public static boolean validAjaxToken(HttpServletRequest request) {
		String requestPath = ResourceUtil.getRequestPath(request);// 用户访问的资源地址
		
		// 获取session中缓存口令map
		HttpSession session = request.getSession();
		//session 值为空，第一次请求
		if(session.getAttribute(DEFAULT_TOKEN_NAME+"."+requestPath)==null){
			Timestamp timestamp=DateUtils.getTimestamp();//访问时间戳
			//保存访问时间戳
			session.setAttribute(DEFAULT_TOKEN_NAME+"."+requestPath,timestamp);
			
			return true;
		}else{
			//上一次请求时间戳
			System.out.println(session.getAttribute(DEFAULT_TOKEN_NAME+"."+requestPath));
			Timestamp last_timestamp=null;
			//session中缓存的时间戳
			//edit 修改 session中取Timestamp类型数据 类型转换错误  王振广start-》
			Object sessionObj=session.getAttribute(DEFAULT_TOKEN_NAME+"."+requestPath);
			if(sessionObj instanceof Timestamp){
				last_timestamp=(Timestamp) session.getAttribute(DEFAULT_TOKEN_NAME+"."+requestPath);
			}else {
				last_timestamp=DateUtils.str2Timestamp(String.valueOf(sessionObj));
			}
			//edit 修改 session中取Timestamp类型数据 类型转换错误 end 《-
			Timestamp timestamp=DateUtils.getTimestamp();
			//如果上次访问时间差  小于30秒，验证失败，重复访问
			if(timestamp.getTime()-last_timestamp.getTime() < 1000*30 ){
				return false;
			}else{
				//保存访问时间戳
				session.setAttribute(DEFAULT_TOKEN_NAME+"."+requestPath,timestamp);
				return true;
			}
			
		}
		
	}
}
