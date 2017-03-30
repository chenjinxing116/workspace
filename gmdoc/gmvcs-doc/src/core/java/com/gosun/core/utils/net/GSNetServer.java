package com.gosun.core.utils.net;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LogLevel;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gosun.core.utils.net.codec.GSNetCodecFactory;
import com.gosun.core.utils.net.dto.BaseMessage;
import com.gosun.core.utils.net.handler.GSNetServiceHandler;


/**
 * 网络库服务端
 * 
 * @author wx
 *
 */
public class GSNetServer {
    
    private Logger logger = LoggerFactory.getLogger(GSNetServer.class);

    public static int SessionId = 0;
    
    private int serverPort;
    private SocketAcceptor acceptor = null;
    private CallBackInterface callback;
    
	/**
	 * 网络库服务端构造函数
	 * @param serverPort 服务端监听端口
	 * @param callback 回调处理接口
	 */
	public GSNetServer(int serverPort,CallBackInterface callback){
    	this.serverPort = serverPort;
    	this.callback = callback;
    }
    
    /**
     * 基类获取系统唯一SessionId方法
     * @return uiSessionId
     */
    public synchronized static int getUniqueSessionId(){
        int tempid;
        if(SessionId<65535)
            SessionId++;
        else
            SessionId = 1;
        tempid = SessionId;
        return tempid;
    }
    
    /**
     * @param ioSession
     * @param httpSession
     * @throws Exception
     */
    public void init() throws Exception{
    	//创建一个非阻塞的serever端socket，用Nio
        SocketAcceptor acceptor = new NioSocketAcceptor(Runtime.getRuntime().availableProcessors()*2);
        //创建接收数据的过滤器
        DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
        
        chain.addLast("pCommCodec",new ProtocolCodecFilter(new GSNetCodecFactory(true)));
        chain.addLast("threadPool", new ExecutorFilter(Executors.newCachedThreadPool()));
        acceptor.getSessionConfig().setReadBufferSize( 65535 );
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
        
        //创建处理器
        IoHandler handler = new GSNetServiceHandler(callback);
        acceptor.setHandler(handler);
        
        acceptor.bind( new InetSocketAddress(this.serverPort));
        
        this.acceptor = acceptor;
    }
    
    public void unInit(){
    	this.acceptor.unbind();
    	this.acceptor = null;
    }
    
    public boolean closeSession(IoSession ioSession){
    	ioSession.getService().dispose();
    	ioSession.close(true);
    	return true;
    }
    
    /**
     * 发送request消息
     * @param data 消息内容
     * @param messageType 消息类型
     * @param ioSession
     * @return
     * @throws Exception
     */
    public int sendMessage(byte[] data, String messageType, IoSession ioSession) throws Exception{
    	BaseMessage messgage = new BaseMessage();
    	messgage.getHead().setMessageType(messageType);
    	messgage.getHead().setRequestMessage(true);
    	messgage.setData(data);
        return this.send(messgage,ioSession);
    }
    
    /**
     * 发送response消息
     * @param data 消息内容
     * @param messageType 消息类型
     * @param ioSession
     * @return
     * @throws Exception
     */
    public int responseMessage(byte[] data, String messageType, IoSession ioSession) throws Exception{
    	BaseMessage messgage = new BaseMessage();
    	messgage.getHead().setMessageType(messageType);
    	messgage.getHead().setRequestMessage(false);
    	messgage.setData(data);
        return this.send(messgage,ioSession);
    }
    
    /**
     * 根据ioSession 发送Object方法,异步处理
     * @param request
     * @return
     * @throws Exception
     */
    private int send(BaseMessage message,IoSession ioSession) throws Exception{
        if(ioSession != null && ioSession.isConnected())//只有connected状态下才能发送
            ioSession.write(message);
        else{
            if(ioSession != null){//如果为空或断开状态下清理资源,
                ioSession.getService().dispose();
            }
        }
    	return 0;
    }

    public LogLevel getLoggerLevel() {
        if(logger.isDebugEnabled())
            return LogLevel.DEBUG;
        else
            return LogLevel.ERROR;
    }
    
    public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public CallBackInterface getCallback() {
		return callback;
	}

	public void setCallback(CallBackInterface callback) {
		this.callback = callback;
	}
}
