package com.guoyicap.util;

import org.jeecgframework.core.util.StringUtil;

/**
 * 字段内容加密
 * 主要用来加密姓名/手机/身份证/邮箱
 * @author 郭凯
 *
 */
public class FieldEncryptUtil{
	public static final String TYPE_NAME="name";//姓名
	public static final String TYPE_PHONE="phone";//电话
	public static final String TYPE_IDCARD="IDCard";//身份证
	public static final String TYPE_IDCARD_2="IDCard_2";//身份证
	public static final String TYPE_MAIL="MAIL";//邮箱
	

    /// <summary>
    /// 字段内容加密
    /// </summary>
    /// <param name="strFieldDetail">字段内容</param>
    /// <param name="strType">加密类型（姓名/手机/身份证/邮箱）</param>
    /// <returns></returns>
	/**
	 * 字段内容加密
	 * @param strFieldDetail //字段内容
	 * @param strType //加密类型
	 * @return 加密后文本
	 */
	 public static String FieldEncrypt(String strFieldDetail, String strType) {
	     
		 String result ="";
		 
		 if(StringUtil.isEmpty(strFieldDetail)){
			 return result;
		 }
		 switch (strType) {
		case TYPE_NAME:
			if (strFieldDetail.length() > 0)
            {
				result = strFieldDetail.substring(0, 1);
                for (int i = 1; i < strFieldDetail.length(); i++)
                {
                	result += "*";
                }
            }
            else
            {
                return "";
            }
			break;
		case TYPE_PHONE:
			for (int i = 0; i < strFieldDetail.length(); i++)
            {
                if (i >= 3 && i <= 6)
                {
                	result += "*";
                }
                else
                {
                	result += strFieldDetail.substring(i,i+1);
                }
            }
			break;
		case TYPE_IDCARD:
			if (strFieldDetail.length() == 18)
            {
				
				result = strFieldDetail.substring(0, 4) + "**" + strFieldDetail.substring(6,10) + "****" + strFieldDetail.substring(14, 16) + "**";
				System.out.println(result);
            }
            else if (strFieldDetail.length() == 15)
            {
            	result = strFieldDetail.substring(0, 4) + "**" + strFieldDetail.substring(6, 10) + "****" + strFieldDetail.substring(14, 16);
            	System.out.println(result);
            }
            else if (strFieldDetail.length() > 0)
            {
            	result = "***";
            }
            else
            {
            	result = "";
            }
			break;
		case TYPE_IDCARD_2:
			if (strFieldDetail.length() > 0)
            {
                if (strFieldDetail.length() == 18)
                {
                	result = strFieldDetail.substring(0, 4) + "**" + strFieldDetail.substring(6, 10) + "****" + strFieldDetail.substring(14, 18);
                }
                else if (strFieldDetail.length() == 15)
                {
                	result = strFieldDetail.substring(0, 4) + "**" + strFieldDetail.substring(6, 10) + "****" + strFieldDetail.substring(14, 15);
                }
                else
                {
                	result = "***";
                }
            }
            else
            {
            	result = "";
            }
			break;
		case TYPE_MAIL:
			if (strFieldDetail.indexOf("@") > -1)
            {
				result = strFieldDetail.split("@")[0].toString().substring(0, 1);
                for (int i = 1; i < strFieldDetail.length() - 1; i++)
                {
                	result += "*";
                }
                result += strFieldDetail.split("@")[0].substring(strFieldDetail.split("@")[0].length() - 1);
                result += "@" + strFieldDetail.split("@")[1];
            }
            else if (StringUtil.isEmpty(strFieldDetail))
            {
            	result = "";
            }
            else
            {
            	result = "";
            }
			break;
		default:
			result = "";
			break;
		}
		 
		 
	     return result;
	 }
	 
	 
	 public static void main(String[] args) {
		System.out.println("name-------"+FieldEncryptUtil.FieldEncrypt("郭凯", FieldEncryptUtil.TYPE_NAME));
		System.out.println("phone------"+FieldEncryptUtil.FieldEncrypt("15618231911", FieldEncryptUtil.TYPE_PHONE));
		System.out.println("idcard-----"+FieldEncryptUtil.FieldEncrypt("370683199309154218", FieldEncryptUtil.TYPE_IDCARD));
		System.out.println("idcard2----"+FieldEncryptUtil.FieldEncrypt("370683199309154218", FieldEncryptUtil.TYPE_IDCARD_2));
		System.out.println("mail-------"+FieldEncryptUtil.FieldEncrypt("277579855@qq.com", FieldEncryptUtil.TYPE_MAIL));
	}
}
