package com.gosun.core.utils.net.dto;

import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;

import com.gosun.core.utils.net.Constants;

/***
 * @ClassName: RequestHead
 * @Description: 协议请求头部
 * @author houyx
 * @date 2012-05-23
 * 
 */
public class MessageHead {
	
	private int length = 0;//长度为loginInfo的总长度
	private int sessionId = -1;
	private int version;
	
	private long commandId;
	
	private long dataLength;
	
	private boolean isRequestMessage;
	
	private BaseMessage message;
	
	private String messageType;

	public MessageHead(){
		
	}
	
	public MessageHead(BaseMessage message){
		this.message = message;
	}
	
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public int getSessionId() {
		return sessionId;
	}
	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}
	
	public IoBuffer fillBuffer(IoBuffer ioBuffer) throws CharacterCodingException{
		if(this.isRequestMessage()){
			ioBuffer.putString(Constants.gsCommonRequestHead + Constants.gsCommonSeparator1,
					Charset.forName(Constants.CharacterSet).newEncoder());
		}else{
			ioBuffer.putString(Constants.gsCommonReponseHead + Constants.gsCommonSeparator1,
					Charset.forName(Constants.CharacterSet).newEncoder());
		}
		String gsCommonHead1 = Constants.gsCommonHead1+this.getMessageType()+ Constants.gsCommonSeparator1;
		String gsCommonHead2 = Constants.gsCommonHead2 + this.getLength();
		ioBuffer.putString(gsCommonHead1, Charset.forName(Constants.CharacterSet).newEncoder());
		ioBuffer.putString(gsCommonHead2, Charset.forName(Constants.CharacterSet).newEncoder());
		ioBuffer.putString(Constants.gsCommonSeparator2,Charset.forName(Constants.CharacterSet).newEncoder());
		return ioBuffer;
	}
	
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public long getCommandId() {
		return commandId;
	}
	public void setCommandId(long commandId) {
		this.commandId = commandId;
	}
	public long getDataLength() {
		return dataLength;
	}
	public void setDataLength(long dataLength) {
		this.dataLength = dataLength;
	}
	
	public BaseMessage getMessage() {
		return message;
	}

	public void setMessage(BaseMessage message) {
		this.message = message;
	}
	
	public boolean isRequestMessage() {
		return isRequestMessage;
	}

	public void setRequestMessage(boolean isRequestMessage) {
		this.isRequestMessage = isRequestMessage;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
}
