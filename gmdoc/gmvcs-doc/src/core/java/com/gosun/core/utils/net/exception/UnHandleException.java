package com.gosun.core.utils.net.exception;

/**
 * 未知错误
 * @author houyx
 *
 */
public class UnHandleException extends GSNetBaseException {

    private static final long serialVersionUID = 1875726206925692152L;

    public UnHandleException(int errorCode){
		this.result = errorCode;
		this.message = "发生错误未处理，错误ID为"+this.result;
	}
}
