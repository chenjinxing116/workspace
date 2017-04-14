package com.goldmsg.gmomm.controller.response;

/***
 * 基础response类
 * @author Qh
 *
 */
public class BaseResponse<T> {

	//响应头
	private ResponseHeader headers;
	//响应实体
	private T body;
	
	public ResponseHeader getHeaders() {
		return headers;
	}
	public void setHeaders(ResponseHeader headers) {
		this.headers = headers;
	}
	public T getBody() {
		return body;
	}
	public void setBody(T body) {
		this.body = body;
	}
	
	public class ResponseHeader {
		//返回码
		private int ret;
		//返回内容
		private String msg;
		
		public int getRet() {
			return ret;
		}
		public void setRet(int ret) {
			this.ret = ret;
		}
		public String getMsg() {
			return msg;
		}
		public void setMsg(String msg) {
			this.msg = msg;
		}
	}
}
