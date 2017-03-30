package com.gosun.core.utils.net.codec;


import java.nio.ByteOrder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gosun.core.utils.net.dto.BaseMessage;

/***
 * 网络库协议编码工具类
 * @author houyx
 * @date 2012-05-16
 * 
 */
public class RequestEncoder extends ProtocolEncoderAdapter {
	
    private static final Logger log = LoggerFactory.getLogger(RequestEncoder.class);
    
	/**
	 * 编码工具类重写encode方法来编码协议
	 * @return void
	 */
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		if(message instanceof BaseMessage){
			IoBuffer ioBuffer = IoBuffer.allocate(16,false).setAutoExpand(true);
	 		ioBuffer.setAutoShrink(true);
	 		ioBuffer.order(ByteOrder.LITTLE_ENDIAN);
	 		ioBuffer = ((BaseMessage)message).fillBuffer(ioBuffer);
	 		
 			ioBuffer.flip();
 			
 			log.debug("发送消息,消息内容为：{}",((BaseMessage)message).getData());
 			
			out.write(ioBuffer);
			out.flush();
	 		
		}
	}
}
