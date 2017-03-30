package com.gosun.core.exception;

/**
 * 系统异常
 * @author lwh
 *
 */
public class SystemException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public SystemException(String msg) {
		super(msg);
	}
	
	public SystemException(Throwable cause) {
		super(cause);
	}
	
	public SystemException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
