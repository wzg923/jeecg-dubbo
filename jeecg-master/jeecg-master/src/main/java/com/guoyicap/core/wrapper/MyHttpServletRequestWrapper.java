package com.guoyicap.core.wrapper;

import java.util.Enumeration;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class MyHttpServletRequestWrapper extends HttpServletRequestWrapper{
	/**
	* @Fields parameterMap : request请求参数Map
	*/ 
	private Map<String,String[]> parameterMap;
	
	public MyHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);
		parameterMap=super.getParameterMap();
	}
	
	
	/**
	* @Title: setParameterMap
	* @author: 王振广
	* @Description: 自定义设置 request的参数Map
	* @param @param parameterMap    设定文件
	* @return void    返回类型
	*/ 
	public void setParameterMap(Map<String, String[]> parameterMap) {
		this.parameterMap = parameterMap;
	}


	@Override
	public Map getParameterMap() {
		// TODO Auto-generated method stub
		//return super.getParameterMap();
		return parameterMap;
		
	}
	
	@Override
	public String getParameter(String name) {
		// TODO Auto-generated method stub
		//return super.getParameter(name);
		String result = "";        
        Object v = parameterMap.get(name);  
        if (v == null) {  
            result = null;  
        } else if (v instanceof String[]) {  
            String[] strArr = (String[]) v;  
            if (strArr.length > 0) {  
                result =  strArr[0];  
            } else {  
                result = null;  
            }  
        } else if (v instanceof String) {  
            result = (String) v;  
        } else {  
            result =  v.toString();  
        }  
          
        return result; 
	}
	
	@Override
	public String[] getParameterValues(String name) {
		// TODO Auto-generated method stub
		//return super.getParameterValues(name);
		String[] result = null;  
        
        Object v = parameterMap.get(name);  
        if (v == null) {  
            result =  null;  
        } else if (v instanceof String[]) {  
            result =  (String[]) v;  
        } else if (v instanceof String) {  
            result =  new String[] { (String) v };  
        } else {  
            result =  new String[] { v.toString() };  
        }  
          
        return result;
	}

	@Override
	public Enumeration getParameterNames() {
		// TODO Auto-generated method stub
		return new Vector(parameterMap.keySet()).elements(); 
	}
	
	@Override
	public Object getAttribute(String name) {
		// TODO Auto-generated method stub
		return super.getAttribute(name);
	}
	
	@Override
	public Enumeration getAttributeNames() {
		// TODO Auto-generated method stub
		return super.getAttributeNames();
	}
	
	/*private void renewParameterMap(HttpServletRequest req) {  
		  
        String queryString = req.getQueryString();  
  
        if (queryString != null && queryString.trim().length() > 0) {  
            String[] params = queryString.split("&");  
  
            for (int i = 0; i < params.length; i++) {  
                int splitIndex = params[i].indexOf("=");  
                if (splitIndex == -1) {  
                    continue;  
                }  
                  
                String key = params[i].substring(0, splitIndex);  
  
                if (!this.parameterMap.containsKey(key)) {  
                    if (splitIndex < params[i].length()) {  
                        String value = params[i].substring(splitIndex + 1);  
                        this.parameterMap.put(key, new String[] { value });  
                    }  
                }  
            }  
        }  
    } */ 
}
