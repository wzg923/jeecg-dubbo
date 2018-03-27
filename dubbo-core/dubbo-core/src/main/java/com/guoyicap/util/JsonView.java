package com.guoyicap.util;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecgframework.core.util.StringUtil;
import org.springframework.web.servlet.view.AbstractView;

public class JsonView extends AbstractView {  
	 @Override  
	 protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,  
	         HttpServletResponse response) throws Exception {  
	  String jsonStr=JSONUtils.toJSONString(model);//转换为JSON 字符串
	  PrintWriter out = response.getWriter(); //输出 
	  out.print(StringUtil.URLEncode(jsonStr));//中文转码，写入流  
	
	 }  
	 	 
}
