package com.gosun.core.utils.net.dto;

import java.nio.ByteOrder;

import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gosun.core.utils.net.Constants;

/***
 * @ClassName: BaseRequest
 * @Description: 协议请求数据基类
 * @author houyx
 * @date 2012-05-23
 * 
 */
public class BaseMessage {
    
    public static final Logger log = LoggerFactory.getLogger(BaseMessage.class);
	
	/**协议请求头*/
    private MessageHead head = new MessageHead(this);
    private byte[] dataByte;
	
	/**
	 * 填写发送缓冲字节流
	 * @param ioBuffer
	 * @return
	 * @throws Exception
	 */
	public IoBuffer fillBuffer(IoBuffer ioBuffer)  throws Exception{
 		this.getHead().setLength(dataByte.length);
		
		this.getHead().fillBuffer(ioBuffer);
		
		ioBuffer.put(dataByte);
		
		return ioBuffer;
	}

	public MessageHead getHead() {
		return head;
	}

	public void setHead(MessageHead head) {
		this.head = head;
	}
	
	public byte[] getData() {
		return dataByte;
	}
	public void setData(byte[] dataByte) {
		this.dataByte = dataByte;
	}
}
