package com.gosun.core.utils.net.dto;


/***
 * @ClassName: BaseReceivedMessage
 * @Description: 协议收到数据基类
 * @author houyx
 * @date 2012-05-23
 * 
 */
public class BaseReceivedMessage {
	
	private boolean isRequestMessage;
	private byte[] data;
	
	public boolean isRequestMessage() {
		return isRequestMessage;
	}
	public void setRequestMessage(boolean isRequestMessage) {
		this.isRequestMessage = isRequestMessage;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
}
