package com.guoyicap.util;

/**
 * @author guang
 *自定异常类，封装获取的异常，添加异常信息，抛出到上层
 */
public class MyException extends RuntimeException{
	private static final long serialVersionUID = 8387364028419832856L;
	
	/**
	 * 默认构造方法
	 */
	public MyException(){
		super();
	}
	/**
	 * 构造方法
	 * @param message
	 */
	public MyException(String message){
		super(message);
	}
	/**
	 * 构造方法
	 * @param message
	 * @param cause
	 */
	public MyException(String message,Throwable cause){
		super(message,cause);
	}
	/**构造方法
	 * @param cause
	 */
	public MyException(Throwable cause){
		super(cause);
	}
	
}
