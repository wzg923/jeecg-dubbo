package com.guoyicap.util;

import org.jeecgframework.core.util.DateUtils;



public class LDCarOrderUtil {
	
	public enum Type {  
	    YJ, HB, CZ,TX  
	} 
	
	public static  String  TrafficLight (Type type ) {  
	        switch (type) {  
	        case YJ:  
	        	return  new StringBuffer("YJ").append(DateUtils.date2Str(DateUtils.yyyymmddhhmmss)).toString();  
	        case HB:  
	        	return  new StringBuffer("HB").append(DateUtils.date2Str(DateUtils.yyyymmddhhmmss)).toString();  
	        case CZ:  
	        	return  new StringBuffer("CZ").append(DateUtils.date2Str(DateUtils.yyyymmddhhmmss)).toString();  
	        case TX:  
	        	return  new StringBuffer("TX").append(DateUtils.date2Str(DateUtils.yyyymmddhhmmss)).toString();  
	        default:  
	        	return "";
	        }  
	}
	
	
	
	
	
	
	
	
	
	
}
