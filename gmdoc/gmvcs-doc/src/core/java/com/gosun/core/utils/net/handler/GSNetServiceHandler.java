package com.gosun.core.utils.net.handler;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gosun.core.utils.net.CallBackInterface;
import com.gosun.core.utils.net.dto.BaseMessage;
import com.gosun.core.utils.net.dto.BaseReceivedMessage;

/***
 * 负责处理连接上来的客户机，即消息处理器
 * @author houyx
 * @date 2012-05-22
 * 
 */
public class GSNetServiceHandler extends IoHandlerAdapter{
	
	private final static Logger log = LoggerFactory.getLogger(GSNetServiceHandler.class);
	private CallBackInterface callback;
	
	public GSNetServiceHandler(CallBackInterface callback){
		this.callback = callback;
	}
	
	  
	/**
	 * 消息处理器收到消息处理
	 * @throws Exception
	 */
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		//收到消息不做处理直接抛出
		if(this.callback != null) {
			BaseReceivedMessage receivedMessage = (BaseReceivedMessage)message;
			if(receivedMessage.isRequestMessage()){
				this.callback.onRequestMessageReceived(session,receivedMessage.getData());
			}else{
				this.callback.onResponseMessageReceived(session,receivedMessage.getData());
			}
		}
	}
	
	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
        //发送消息事件
		if(this.callback != null) this.callback.onMessageSent(session,(BaseMessage)message);
    }
	
	/**
	 * 消息处理器会话结束处理
	 * @throws Exception
	 */
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		if(this.callback != null) this.callback.onSessionClosed(session);
	}
	
	/**
	 * 消息处理器会话打开
	 * @throws Exception
	 */
	@Override
	public void sessionOpened(IoSession session) throws Exception {
		if(this.callback != null) this.callback.onSessionOpened(session);
	}
	
	/**
	 * 消息处理器会话创建处理
	 * @throws Exception
	 */
	@Override
	public void sessionCreated(IoSession session) throws Exception {
		if(this.callback != null) this.callback.onSessionConnected(session);
	}
	
	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
            throws Exception {
        //TODO 当session空闲的时候处理
		if(this.callback != null) this.callback.onSessionIdle(session, status);
    }
	
	@Override
    public void exceptionCaught(IoSession session, Throwable cause)
            throws Exception {
        //TODO 处理捕获的异常
		
    }
	
}