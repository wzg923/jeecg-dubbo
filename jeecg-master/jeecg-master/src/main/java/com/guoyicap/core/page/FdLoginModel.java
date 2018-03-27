package com.guoyicap.core.page;

import java.io.Serializable;
import java.util.Date;

/**
 * 前台用户登录 bean
 * @author guang
 *
 */
public class FdLoginModel implements Serializable{
	
	  private static final long serialVersionUID = -2375035973316726330L;
      private String id;
      private String userName;//用户名
      private String password;//密码
      private String openid;//微信openid
      private String logintoken;//口令
      private String realname;//真实姓名
      
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getLogintoken() {
		return logintoken;
	}
	public void setLogintoken(String logintoken) {
		this.logintoken = logintoken;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
     
      
      
}
