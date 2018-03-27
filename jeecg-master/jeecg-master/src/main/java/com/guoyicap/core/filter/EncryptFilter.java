package com.guoyicap.core.filter;

import java.io.IOException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.jeecgframework.core.util.LogUtil;
import org.jeecgframework.core.util.ResourceUtil;

import com.guoyicap.core.util.encrypt.RSAUtils;
import com.guoyicap.core.wrapper.MyHttpServletRequestWrapper;

import jodd.servlet.URLDecoder;

/**
* @ClassName: EncryptFilter
* @Description:request参数解密过滤器
* @author 王振广
* @date 2016年5月3日 下午1:54:00
*
*/ 
public class EncryptFilter implements Filter{
	//private List<String> encryptUrls;//String类型转为List
	
	/**
	* @Fields RSA_PUB_KEY_SERVICE : 服务器端RSA公钥 字符串
	*/ 
	public static final RSAPublicKey RSA_PUB_KEY_SERVER;
	
	/**
	* @Fields RSA_PRI_KEY_SERVICE : 服务器端RSA私钥  字符串
	*/ 
	public static final RSAPrivateKey RSA_PRI_KEY_SERVER; 
	
	/**
	* @Fields RSA_PUB_KEY_ANDROID :android/ios 公钥
	*/ 
	public static final RSAPublicKey RSA_PUB_KEY_CLIENT;
	/**
	* @Fields RSA_PRI_KEY_CLIENT : android/ios 私钥 服务器端不使用
	*/ 
	public static final RSAPrivateKey RSA_PRI_KEY_CLIENT;
	
	
	static {
		// 获取RSA密钥对
		Map<String, Object> keysMap = RSAUtils.getKeys();
		// 生成公钥和私钥
		RSA_PUB_KEY_SERVER = (RSAPublicKey) keysMap.get("public");
		RSA_PRI_KEY_SERVER = (RSAPrivateKey) keysMap.get("private");

		// 获取RSA密钥对
		Map<String, Object> clientKeysMap = RSAUtils.getClientKeys();
		RSA_PUB_KEY_CLIENT = (RSAPublicKey) clientKeysMap.get("public");
		RSA_PRI_KEY_CLIENT=(RSAPrivateKey) clientKeysMap.get("private");//服务器端不使用

	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		//排除过滤url列表		
		//encryptUrls=Arrays.asList(filterConfig.getInitParameter("encryptUrls").split(","));
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		//请求url
		String requestUrl=ResourceUtil.getRequestPath((HttpServletRequest) request);
		//判断是否需要过滤处理
//		for (String url : encryptUrls) {
//			if(requestUrl.contains(url)){
				
				//request对象封装 
				MyHttpServletRequestWrapper encryptRequestWrapper = new MyHttpServletRequestWrapper(
						(HttpServletRequest) request);
				//response 对象封装
				/*MyHttpServletResponseWrapper encryptResponseWrapper = new MyHttpServletResponseWrapper(
						(HttpServletResponse) response);*/

				//request 参数解密
				//原来参数map
				Map<String,Object> parameterMap=encryptRequestWrapper.getParameterMap();
				//解密处理后参数map
				Map<String,String[]> parameterMapNew=new ConcurrentHashMap<String,String[]>();
				for (String key : parameterMap.keySet()) {
					if (parameterMap.get(key) instanceof String[]) {
						try {
							StringBuffer plainDataBuffer=new StringBuffer();
							for(String s: (String[])parameterMap.get(key)){
								//URLDecoder 解决Base64转码后，'+'被替换为 ' '的BUG
								s=URLDecoder.decode(s);
								// 参数解密
								plainDataBuffer.append(RSAUtils.decryptByPrivateKey(s,
										this.RSA_PRI_KEY_SERVER));
							}
							
							
							//URLDecoder
							String plainData=URLDecoder.decode(plainDataBuffer.toString());
							// 移除原来 key-value
							//parameterMap.remove(key);
							// 判断参数是否合法，过滤js注入、sql注入
							//plainData = StringEscapeUtil.escapeSql(StringEscapeUtil.htmlEscape(plainData));
							
							// 更新参数值 为解密后明文参数
							parameterMapNew.put(key, new String[]{plainData});
						} catch (Exception e) {
							e.printStackTrace();
							LogUtil.error("request参数解密失败。" + key + ":" + parameterMap.get(key), e);
						}
					}

				}
				
				//更新request 参数map
				encryptRequestWrapper.setParameterMap(parameterMapNew);
				
				
				//继续执行 其他逻辑
				chain.doFilter(encryptRequestWrapper, response);
				
				
				//封装response  处理返回参数 进行加密
				/*MyOutputStream myOutput = encryptResponseWrapper.getMyOutputStream();
				MyPrintWriter myPrintWriter = (MyPrintWriter) encryptResponseWrapper.getMyWriter();  
			    if(myPrintWriter != null){
			          String content = myPrintWriter.getContent();  
			          System.out.println(content);
			          //URLEncoder
			          content=URLEncoder.encode(content);
			          //参数加密
			          try {
						RSAUtils.encryptByPublicKey(content, RSA_PUB_KEY_CLIENT);
					} catch (Exception e) {
						LogUtil.error("参数加密失败",e);
						content="";
					}
			          
			          encryptResponseWrapper.getWriter().write(content);  
			    }
			    if(myOutput!=null){
			    	
			    }*/
				//output.flush();
			    
			    //终止循环
//			    break;
//			}
//		}
		
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}


}
