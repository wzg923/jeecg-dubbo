package com.guoyicap.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * service执行结果
 * @author guang
 *
 */
public class ExecuteResult <T> implements Serializable{
	
	/**
	 * 执行结果对象List
	 */
	private T result;

	/**
	 * 错误信息
	 */
	private List<String> errorMessages;
	
	/**
	 * 警告信息
	 */
	private List<String> warningMessages;
	
	/**
	 * 提示信息
	 */
	private List<String> infoMessages;
	
	/**
	 * 是否成功
	 */
	private boolean isSuccess=true;

	/**
	 * 异常信息
	 */
	private Throwable cause; 

	
	/**
	 * 默认构造方法
	 */
	public ExecuteResult(){}
	
	/**
	 * 带result构造方法
	 * @param t
	 */
	public ExecuteResult(T t){
		result=t;
	}

	/**执行结果
	 * @return
	 */
	public T getResult() {
		return result;
	}

	/**执行结果
	 * @param result
	 */
	public void setResult(T result) {
		this.result = result;
	}

	public List<String> getErrorMessages() {
		return errorMessages;
	}

	public void setErrorMessages(List<String> errorMessages) {
		this.errorMessages = errorMessages;
	}

	public List<String> getWarningMessages() {
		return warningMessages;
	}

	public void setWarningMessages(List<String> warningMessages) {
		this.warningMessages = warningMessages;
	}

	public List<String> getInfoMessages() {
		return infoMessages;
	}

	public void setInfoMessages(List<String> infoMessages) {
		this.infoMessages = infoMessages;
	}
	
	/**
	 * 添加错误信息
	 * @param errorMessage
	 */
	public void addErrorMessage(String errorMessage){
		if(null==errorMessages){
			errorMessages=new ArrayList<String>();
		}
		errorMessages.add(errorMessage);
	}
	
	/**
	 * 添加警告信息
	 * @param warningMessage
	 */
	public void addWarningMessage(String warningMessage){
		if(null==warningMessages){
			warningMessages=new ArrayList<String>();
		}
		warningMessages.add(warningMessage);
	}

	/**
	 * 添加提示信息
	 * @param infoMessage
	 */
	public void addInfoMessage(String infoMessage){
		if(null==infoMessages){
			infoMessages=new ArrayList<String>();
		}
		infoMessages.add(infoMessage);
	}
	
	/**
	 * 判断是否成功
	 * @return
	 */
	public boolean isSuccess() {
		if(null!=errorMessages && errorMessages.size()>0){
			isSuccess=false; 
		}
		
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public Throwable getCause() {
		return cause;
	}

	public void setCause(Throwable cause) {
		this.cause = cause;
	}
	
	
	
}
