package com.guoyicap.core.enums;

import java.util.HashMap;
import java.util.Map;

/**
* @ClassName: LogTypeEnum
* @Description: 日志类别
* @author 王振广
* @date 2015年12月29日 下午7:05:17
*
*/ 
public enum LogTypeEnum {
	Log_Type_LOGIN((short)1,"登录"),//登陆
	Log_Type_EXIT((short)2,"退出"), //退出
	Log_Type_INSERT((short)3,"插入"),//插入
	Log_Type_DEL((short)4,"删除"),//删除
	Log_Type_UPDATE((short)5,"更新"),//更新
	Log_Type_UPLOAD((short)6,"上传"),//上传
	Log_Type_OTHER((short)7,"其他"),//其他
	Log_Type_QUERY((short)8,"查询"),//查询
	Log_Type_EXEC_Procudure((short)9,"执行过程"),//执行过程	
	
	Log_Type_HuiFu((short)10,"汇付接口"),//汇付接口
	Log_Type_XiaoDai((short)11,"小贷接口"),//小贷接口
	Log_Type_DiZhiYa((short)12,"抵质押接口"),//抵质押接口
	Log_Type_WebAccess((short)13,"前台页面访问"),//前台页面访问
	Log_Type_ConsoleAccess((short)14,"后台页面访问"),//后台页面访问
	Log_Type_Redis((short)14,"Redis服务器"),//Redis服务器
	Log_Type_Export((short)15,"导出"),//导出
	Log_Type_Download((short)16,"下载"),//下载
	
	
	Log_Type_Default((short)17,"默认"),//默认
	Log_Type_AutoService((short)18,"自动服务"),
	Log_Type_Transfer((short)19,"交易"),
	Log_Type_Work((short)20,"定时作业"),
	
	Log_Type_Youxuan12((short)21,"手工加入优选12期"),
	Log_Type_WebService((short)22,"WebService接口");
	
	//TODO 增加新的枚举 类别
	
	private static Map<Short,String> logTypeMap=null;	
	
	private Short value;//日志类型 value
	private String type;//日志类型  type
	LogTypeEnum(Short value,String type){
		this.value=value;
		this.type=type;
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
	* @Title: getType
	* @Description:日志类型 type
	* @return String    返回类型
	*/ 
	public String getType() {
		return type;
	} 
	
	public static String getType(short value){
		if(logTypeMap==null){
			logTypeMap=new HashMap<Short,String>();
			logTypeMap.put(Log_Type_LOGIN.getValue(), Log_Type_LOGIN.type);
			logTypeMap.put(Log_Type_EXIT.getValue(), Log_Type_EXIT.type);
			logTypeMap.put(Log_Type_INSERT.getValue(), Log_Type_INSERT.type);
			logTypeMap.put(Log_Type_DEL.getValue(), Log_Type_DEL.type);
			logTypeMap.put(Log_Type_UPDATE.getValue(), Log_Type_UPDATE.type);
			logTypeMap.put(Log_Type_UPLOAD.getValue(), Log_Type_UPLOAD.type);
			logTypeMap.put(Log_Type_OTHER.getValue(), Log_Type_OTHER.type);
			logTypeMap.put(Log_Type_QUERY.getValue(), Log_Type_QUERY.type);
			logTypeMap.put(Log_Type_EXEC_Procudure.getValue(), Log_Type_EXEC_Procudure.type);
			
			logTypeMap.put(Log_Type_XiaoDai.getValue(), Log_Type_XiaoDai.type);
			logTypeMap.put(Log_Type_HuiFu.getValue(), Log_Type_HuiFu.type);
			logTypeMap.put(Log_Type_DiZhiYa.getValue(), Log_Type_DiZhiYa.type);
			logTypeMap.put(Log_Type_WebAccess.getValue(), Log_Type_WebAccess.type);
			logTypeMap.put(Log_Type_ConsoleAccess.getValue(), Log_Type_ConsoleAccess.type);
			logTypeMap.put(Log_Type_Redis.getValue(), Log_Type_Redis.type);
			logTypeMap.put(Log_Type_Export.getValue(), Log_Type_Export.type);
			logTypeMap.put(Log_Type_Download.getValue(), Log_Type_Download.type);
			
			logTypeMap.put(Log_Type_Default.getValue(),Log_Type_Default.type);
			logTypeMap.put(Log_Type_AutoService.getValue(),Log_Type_AutoService.type);
			logTypeMap.put(Log_Type_Transfer.getValue(),Log_Type_Transfer.type);
			logTypeMap.put(Log_Type_Work.getValue(),Log_Type_Work.type);
			logTypeMap.put(Log_Type_Youxuan12.getValue(),Log_Type_Youxuan12.type);
			logTypeMap.put(Log_Type_WebService.getValue(),Log_Type_WebService.type);
			
			//TODO 增加新的枚举 类别
		}
		if(logTypeMap.containsKey(value)){
			return logTypeMap.get(value);
		}
		return String.valueOf(value);
	}
	
	
}
