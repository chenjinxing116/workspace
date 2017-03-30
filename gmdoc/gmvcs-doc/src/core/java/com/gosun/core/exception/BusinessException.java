package com.gosun.core.exception;

/**
 * 业务类异常
 * @author lwh
 * 
 *
 */
public class BusinessException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BusinessException() {
		
	}
	
	public BusinessException(String msg) {
		super(msg);
	}
	
	public BusinessException(Throwable cause) {
		super(cause);
	}
	
	public BusinessException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	
}
