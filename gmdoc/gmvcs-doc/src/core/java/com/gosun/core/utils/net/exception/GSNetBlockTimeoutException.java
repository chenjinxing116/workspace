package com.gosun.core.utils.net.exception;


/**
 * 不能与服务器连接异常类
 * @author Abe
 *
 */
public class GSNetBlockTimeoutException extends GSNetBaseException {

    private static final long serialVersionUID = -8289879443165819437L;

    public GSNetBlockTimeoutException(){
		this.message = "服务正忙或服务连接已断开，请稍候再试";
	}
    
    public GSNetBlockTimeoutException(String message){
        this.message = message;
    }
    
}