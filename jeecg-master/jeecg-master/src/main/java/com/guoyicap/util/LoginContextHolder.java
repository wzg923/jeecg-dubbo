package com.guoyicap.util;

import org.jeecgframework.core.util.ContextHolderUtils;

import com.guoyicap.core.page.FdLoginModel;


/**
 * 定义要保存到session中的用户登录信息 </p>
 * @author guang
 * @date 2015/9/30
 *@version v1.0
 */

public class LoginContextHolder {
	/**
	 * UserInfo表信息
	 */
	public static final String LOGINUSERINFO="_loginuserinfo";
	public static final String RAND="randCode";//手机验证时，生成的验证码
	/**
	 * 前台线上用户信息</p>
	 * 获得登录用户信息</p>
	 * @return
	 */
	/*public static FdLoginModel getLoginUserinfo(){
		if(ContextHolderUtils.getSession()!=null){
			FdLoginModel userinfo=(FdLoginModel) ContextHolderUtils.getSession().getAttribute(LOGINUSERINFO);
			return userinfo;
		}
		return null;
	}*/
	
	/**
	 * 前台线上用户信息</p>
	 * 设置保存登录用户信息 userInfo</p>
	 */
	/*public static  void setLoginUserinfo(FdLoginModel userInfo){
		if(ContextHolderUtils.getSession()!=null){
			ContextHolderUtils.getSession().setAttribute(LOGINUSERINFO, userInfo);
		}		
	}*/
	
	/**
	* @Title: getRand
	* @author: 王振广
	* @Description: 获取验证码
	* @param @return    设定文件
	* @return String    返回类型
	*/ 
	public static String getRand(){
		String rand=null;
		if(ContextHolderUtils.getSession()!=null){			
			rand=(String) ContextHolderUtils.getSession().getAttribute(RAND);
		}
		return rand;		
	
	}
	
	/**
	* @Title: setRand
	* @author: 王振广
	* @Description: 设置 图片验证码
	* @param @param randCode    设定文件
	* @return void    返回类型
	*/ 
	public static void setRand(String randCode){
		if(ContextHolderUtils.getSession()!=null){
			ContextHolderUtils.getSession().setAttribute(RAND,randCode);
		}
	}
}
