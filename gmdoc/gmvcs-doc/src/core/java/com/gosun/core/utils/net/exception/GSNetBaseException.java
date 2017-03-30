package com.gosun.core.utils.net.exception;

/**
 * GSNet异常处理基类
 * @author Abe
 *
 */
public class GSNetBaseException extends Exception {
	
    private static final long serialVersionUID = -770308121031671539L;
    
    public static int RESULT_SUCCESS = 0;
	public static int RESULT_FAIL = 1;
	public static int RESULT_UNKNOWN = 2;
	public static int RESULT_NOT_IN_BASEEXCEPTION = 3;
	
	protected String message;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public int result = RESULT_UNKNOWN;
	
	public GSNetBaseException(){
    }
	public GSNetBaseException(int result){
	    this.result = result;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}
}
