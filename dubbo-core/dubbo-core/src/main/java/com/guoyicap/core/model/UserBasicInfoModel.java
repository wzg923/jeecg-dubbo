package com.guoyicap.core.model;

import java.io.Serializable;

/**
* @ClassName: UserBasicInfoModel
* @Description: 会员基本信息model
* @author 王振广
* @date 2016年10月8日 下午4:05:11
*
*/ 
public class UserBasicInfoModel implements Serializable {

	/**
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;
	private String hid;//会员id 渠道经理id
	private String account;//登录名  手机号或员工号
	private String username;//真实姓名
	private String phone;
	private String email;
	private String idcard;//身份证号
	private String verifystatus;
	private String factory;
	private String province;
	private String city;
	private String area;
	private String chProvince;
	private String chCity;
	private String chArea;
	private String bankid;
	private String bankname;
	private String bankcard;
	private String kefu;
	private String kefucode;
	private String kefunameame;
	private String kefutel;
	private String kefuemail;
	private String logintoken;
	private boolean submitallow;
	private String role="0";//登录用户角色   0：会员   1：渠道经理（公司员工）
	
	public String getHid() {
		return hid;
	}
	public void setHid(String hid) {
		this.hid = hid;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getIdcard() {
		return idcard;
	}
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	public String getVerifystatus() {
		return verifystatus;
	}
	public void setVerifystatus(String verifystatus) {
		this.verifystatus = verifystatus;
	}
	public String getFactory() {
		return factory;
	}
	public void setFactory(String factory) {
		this.factory = factory;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	
	public String getChProvince() {
		return chProvince;
	}
	public void setChProvince(String chProvince) {
		this.chProvince = chProvince;
	}
	public String getChCity() {
		return chCity;
	}
	public void setChCity(String chCity) {
		this.chCity = chCity;
	}
	public String getChArea() {
		return chArea;
	}
	public void setChArea(String chArea) {
		this.chArea = chArea;
	}
	public String getBankid() {
		return bankid;
	}
	public void setBankid(String bankid) {
		this.bankid = bankid;
	}
	public String getBankname() {
		return bankname;
	}
	public void setBankname(String bankname) {
		this.bankname = bankname;
	}
	public String getBankcard() {
		return bankcard;
	}
	public void setBankcard(String bankcard) {
		this.bankcard = bankcard;
	}
	public String getKefu() {
		return kefu;
	}
	public void setKefu(String kefu) {
		this.kefu = kefu;
	}
	public String getKefucode() {
		return kefucode;
	}
	public void setKefucode(String kefucode) {
		this.kefucode = kefucode;
	}
	public String getKefunameame() {
		return kefunameame;
	}
	public void setKefunameame(String kefunameame) {
		this.kefunameame = kefunameame;
	}
	public String getKefutel() {
		return kefutel;
	}
	public void setKefutel(String kefutel) {
		this.kefutel = kefutel;
	}
	public String getKefuemail() {
		return kefuemail;
	}
	public void setKefuemail(String kefuemail) {
		this.kefuemail = kefuemail;
	}
	public String getLogintoken() {
		return logintoken;
	}
	public void setLogintoken(String logintoken) {
		this.logintoken = logintoken;
	}
	/**
	* @return submitallow
	*/ 
	public boolean getSubmitallow() {
		return submitallow;
	}
	/**
	* @param submitallow 要设置的 submitallow
	*/ 
	public void setSubmitallow(boolean submitallow) {
		this.submitallow = submitallow;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}

}
