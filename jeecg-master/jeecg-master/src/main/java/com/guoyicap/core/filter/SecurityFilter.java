package com.guoyicap.core.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;

import com.guoyicap.core.wrapper.MyHttpServletRequestWrapper;
import com.guoyicap.util.JSONUtils;
import com.guoyicap.util.StringEscapeUtil;

public class SecurityFilter implements Filter{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		//request对象封装 
		MyHttpServletRequestWrapper myRequestWrapper = new MyHttpServletRequestWrapper(
				(HttpServletRequest) request);
		
		String requestPath = ResourceUtil.getRequestPath((HttpServletRequest)request);// 用户访问的资源地址
		requestPath=requestPath.substring(0,requestPath.indexOf("?"));
		if (requestPath.indexOf("rest/") == 0 ) {// url以rest/开头,排除汇付回调
			//request 参数过滤非法字符
			Map<String,Object> parameterMap=myRequestWrapper.getParameterMap();
			for (String key : parameterMap.keySet()) {
				Object obj=parameterMap.get(key);//参数
				if (obj !=null && obj instanceof String[]) {
						int length=((String[])obj).length;//参数数组长度
						// 新参数数组
						//String[] newObj=new String[length];
						for (int i=0;i<length;i++) {
							//数组元素
							String value=((String[])obj)[i];
							if("str".equals(key)){
								
								try{
									//解析后参数map
									Map map= JSONUtils.toHashMap(value);
									Map mapNew=new HashMap();//新map
									//遍历 map的值
									for (Iterator iterator = map.keySet().iterator(); iterator.hasNext();) {
										String key2 = (String) iterator.next();
										Object mapValue=map.get(key2);//参数值
										//如果map的value是字符串类型
										if(mapValue instanceof String){
											//过滤非法字符
											String newValue= StringEscapeUtil.htmlEscape(
													StringEscapeUtil.escapeSql(String.valueOf(mapValue)));
											//赋值到新参数map
											mapNew.put(key2, newValue);									
										}else{
											//其他类型，不需要过滤非法字符，直接 赋值 到新参数map
											mapNew.put(key2, mapValue);
										}
										
									}
									//更新 str 参数的值,此时已经经过非法字符处理
									value=JSONUtils.toJSONString(JSONUtils.toJSONObject(mapNew));
								}catch(Exception e){
									//如果异常,则 value 不可以转为map,值非jsonStr
									value=StringEscapeUtil.htmlEscape(
											StringEscapeUtil.escapeSql(value));
								}
								
							}else{
								value = StringEscapeUtil.htmlEscape(StringEscapeUtil.escapeSql(value));
							}
							
							//value = StringEscapeUtil.escapeSql(value);
							((String[])obj)[i]=value;//重定义
						}
				}		

			}
		}
		
		
		
		//更新request 参数map
		//myRequestWrapper.setParameterMap(parameterMap);
		
		
		//继续执行 其他逻辑
		chain.doFilter(request, response);
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
