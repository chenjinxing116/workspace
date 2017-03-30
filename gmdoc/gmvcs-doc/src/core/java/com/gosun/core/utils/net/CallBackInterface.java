package com.gosun.core.utils.net;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.gosun.core.utils.net.dto.BaseMessage;


/***
 * @ClassName: CallBackInterface
 * @Description: 网络库通信回调接口
 * @author houyx
 * @date 2015-10-16
 * 
 */
public interface CallBackInterface{
	
	public void onRequestMessageReceived(IoSession ioSession,byte[] protocolContent);
	
	public void onResponseMessageReceived(IoSession ioSession,byte[] protocolContent);
	
	public void onMessageSent(IoSession ioSession,BaseMessage protocolContent);
	
	public void onSessionOpened(IoSession ioSession);
	
	public void onSessionConnected(IoSession ioSession);
	
	public void onSessionClosed(IoSession ioSession);
	
	public void onSessionIdle(IoSession ioSession,IdleStatus status);
	
}