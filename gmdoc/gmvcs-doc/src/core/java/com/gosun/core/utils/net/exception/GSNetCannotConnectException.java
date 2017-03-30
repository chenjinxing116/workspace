package com.gosun.core.utils.net.exception;


/**
 * 不能与服务器连接异常类
 * @author Abe
 *
 */
public class GSNetCannotConnectException extends GSNetBaseException {

    private static final long serialVersionUID = -5334616236078720797L;

    public GSNetCannotConnectException(){
		this.message = "连接失败,服务端未正常启动或者尝试重新登录,请检查相关配置.";
	}
}