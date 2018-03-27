package com.guoyicap.core.enums;

import java.util.HashMap;
import java.util.Map;

/**
* @ClassName: LogModuleEnum
* @Description: 日志   业务模块   枚举类。有新业务模块可以自己添加。
* @author 王振广
* @date 2016年2月15日 上午8:29:39
*
*/ 
public enum LogModuleEnum {
	Log_Module_Other((short)0,"其他"),
	Log_Module_RenewLoans((short)1,"续贷标"),
	Log_Module_AutoLoan((short)2,"自动放款"),
	Log_Module_AutoReview((short)3,"自动复审"),
	Log_Module_AutoCharge((short)4,"自动收费"),
	Log_Module_AutoTender((short)5,"自动投标");
	
	//TODO 增加新的枚举 类别
	
	
	private static Map<Short,String> logModuleMap=null;	
	
	private Short value;//日志模块  值
	private String module;//日志模块名称
	LogModuleEnum(Short value,String module){
		this.value=value;
		this.module=module;
	}
	
	/**
	* @Title: getValue
	* @Description: 日志类型 value
	* @return Short    返回类型
	*/ 
	public Short getValue() {
		return value;
	}
	/**
	* @Title: getModule
	* @Description:日志模块 Module
	* @return String    返回类型
	*/ 
	public String getModule() {
		return module;
	} 

	public static String getModule(short value){
		if(logModuleMap==null){//初始化map
			logModuleMap=new HashMap<Short,String>();
			logModuleMap.put(Log_Module_Other.getValue(), Log_Module_Other.getModule());
			logModuleMap.put(Log_Module_RenewLoans.getValue(), Log_Module_RenewLoans.getModule());
			logModuleMap.put(Log_Module_AutoLoan.getValue(), Log_Module_AutoLoan.getModule());
			logModuleMap.put(Log_Module_AutoReview.getValue(), Log_Module_AutoReview.getModule());
			logModuleMap.put(Log_Module_AutoCharge.getValue(), Log_Module_AutoCharge.getModule());
			logModuleMap.put(Log_Module_AutoTender.getValue(), Log_Module_AutoTender.getModule());
			
			//TODO 增加新的枚举 类别
		}
		if(logModuleMap.containsKey(value)){
			return logModuleMap.get(value);
		}
		return String.valueOf(value);
	}
}
