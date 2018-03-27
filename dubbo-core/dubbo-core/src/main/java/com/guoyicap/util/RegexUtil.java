package com.guoyicap.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName: RegexUtil
 * @Description: 正则表达式 规则匹配验证
 * @author 王振广
 * @date 2016年2月22日 上午9:08:59
 *
 */
public class RegexUtil {

	/**
	* @Title: phoneNo
	* @author: 王振广
	* @Description: 手机号正则匹配
	* @param @param phoneNo
	* @param @return    设定文件
	* @return boolean    返回类型
	*/ 
	public static boolean matchesPhoneNo(String phoneNo) {
		String phonePatternStr = "^1[0-9]{10}$";//手机号规则
		Pattern pattern = Pattern.compile(phonePatternStr);
		Matcher matcher = pattern.matcher(phoneNo);//匹配规则
		return matcher.matches();//是否匹配

	}
	
	/**  
	* @Title: matchesTelephone  
	* @Author:王振广
	* @Description: 固定电话 正则匹配 
	* @param phone
	* @return    设定文件  
	* @return boolean    返回类型  
	* @throws  
	*/
	public static boolean matchesTelephone(String phone){
		String phonePatternStr="^((0\\d{2,3})?(\\-)?\\d{7,8})$";
		Pattern pattern = Pattern.compile(phonePatternStr);
		Matcher matcher = pattern.matcher(phone);//匹配规则
		return matcher.matches();//是否匹配
	}
	
	/**
	* @Title: matchesEmail
	* @author: 王振广
	* @Description:邮箱正则匹配
	* @param @param email
	* @param @return    设定文件
	* @return boolean    返回类型
	*/ 
	public static boolean matchesEmail(String email){
		String emailPatternStr="^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern pattern = Pattern.compile(emailPatternStr);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();//是否匹配
	}
	/**
	* @Title: matchesName
	* @author: 郭凯
	* @Description:姓名正则匹配
	* /// 要求：真实姓名可以是汉字，也可以是字母，但是不能两者都有，也不能包含任何符号和数字
	* /// 注意：1.如果是英文名,可以允许英文名字中出现空格
	* /// 2.英文名的空格可以是多个，但是不能连续出现多个
	* /// 3.汉字不能出现空格
	* @param @param email
	* @param @return    设定文件
	* @return boolean    返回类型
	*/ 
	public static boolean matchesName(String name){
		String emailPatternStr="([\\u4e00-\\u9fa5]+|([a-zA-Z]+\\s?)+|[^·\u4e00-\u9fa5]([·\u4e00-\u9fa5]+?)[^·\u4e00-\u9fa5])$";
        Pattern pattern = Pattern.compile(emailPatternStr);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();//是否匹配
	}
	/**
	* @Title: matchesCarNum
	* @author: 郭凯
	* @Description:车牌号正则匹配
	* @param @param email
	* @param @return    设定文件
	* @return boolean    返回类型
	*/ 
	public static boolean matchesCarNum(String carNum){
		String emailPatternStr="^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$";
        Pattern pattern = Pattern.compile(emailPatternStr);
        Matcher matcher = pattern.matcher(carNum);
        return matcher.matches();//是否匹配
	}
	/**
	* @Title: matchesPassword
	* @author: 郭凯
	* @Description:密码正则匹配
	* @param @param password
	* @param @return    设定文件
	* @return boolean    返回类型
	*/ 
	public static boolean matchesPassword(String password){
		
		String emailPatternStr="^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,20}$";
        Pattern pattern = Pattern.compile(emailPatternStr);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();//是否匹配
		
	}

}
