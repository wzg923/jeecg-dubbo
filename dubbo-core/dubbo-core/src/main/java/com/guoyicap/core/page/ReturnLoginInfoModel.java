package com.guoyicap.core.page;

import java.io.Serializable;

/**
 * @ReturnLoginInfoModel.java
 * @Description 登录页面用的返回信息
 * @author 孙磊
 * @creatDate 2016年4月25日 下午2:48:53
 * @version 
 */
public class ReturnLoginInfoModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 是否成功*/
	private java.lang.String status;
	/** 提示*/
	private java.lang.String msg;
	/** 数据*/
	private java.lang.Object data;
	public java.lang.String getStatus() {
		return status;
	}
	public void setStatus(java.lang.String status) {
		this.status = status;
	}
	public java.lang.String getMsg() {
		return msg;
	}
	public void setMsg(java.lang.String msg) {
		this.msg = msg;
	}
	public java.lang.Object getData() {
		return data;
	}
	public void setData(java.lang.Object data) {
		this.data = data;
	}
	
	

}
