package com.gosun.core.utils.net.codec;


import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * @ClassName: HeartBeatFactory
 * @Description: 内部类，实现心跳工厂
 * @author houyx
 * @date 2015-10-16
 * 
 */
public class HeartBeatFactory implements KeepAliveMessageFactory {
    
    private static final Logger log = LoggerFactory.getLogger(HeartBeatFactory.class);
	/**
	 * 发送心跳Request
	 * @return 
	 */
    
    private String heartBeatString;
    
    public HeartBeatFactory(String heartBeatString){
    	this.heartBeatString = heartBeatString;
    }
    
	@Override
	public Object getRequest(IoSession session){
		try {
//            sdkDao.sendHeartBeat(session);
			if(session != null && session.isConnected()){
				session.write(this.heartBeatString);
			}
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
	}
	
	/**
	 * MINA截取心跳包函数
	 * @return 
	 */
	@Override
	public Object getResponse(IoSession session, Object request) {
		return null;
	}
	
	/**
	 * MINA判断收到包是否是发送心跳包的继承函数，返回false即无视此包
	 * @return 
	 */
	@Override
	public boolean isRequest(IoSession session, Object message) {
		return false;
	}
	
	/**
	 * MINA判断收到包是否是收到心跳包的继承函数，返回false即无视此包
	 * @return 
	 */
	@Override
	public boolean isResponse(IoSession session, Object message) {
		return false;
	}

}
