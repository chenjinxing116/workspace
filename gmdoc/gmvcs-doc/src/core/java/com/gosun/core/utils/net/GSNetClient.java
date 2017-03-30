package com.gosun.core.utils.net;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;
import org.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler;
import org.apache.mina.filter.logging.LogLevel;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gosun.core.utils.SystemConfig;
import com.gosun.core.utils.net.codec.GSNetCodecFactory;
import com.gosun.core.utils.net.codec.HeartBeatFactory;
import com.gosun.core.utils.net.dto.BaseMessage;
import com.gosun.core.utils.net.exception.GSNetCannotConnectException;
import com.gosun.core.utils.net.handler.GSNetServiceHandler;

/**
 * 网络库客户端
 * 
 * @author wx
 *
 */
public class GSNetClient {

	private Logger logger = LoggerFactory.getLogger(GSNetClient.class);

	public static int SessionId = 0;

	private String serverIp;
	private int serverPort;
	private int timeout;// session连接超时
	private int heartBeatInterval;// 心跳时间
	private int retryInterval;// 重连间隔时间
	private int retryAttempts;// 重连次数

	private String heartBeatString = "";
	private boolean sendHeartBeat = true;// 重连次数

	private IoSession ioSession;

	private CallBackInterface callback;

	private static boolean isInited = false;// 是否已初始化，用在服务器启动

	// private

	/**
	 * 网络库客户端构造函数
	 * 
	 * @param serverIp
	 *            服务端ip
	 * @param serverPort
	 *            服务端端口
	 * @param callback
	 *            回调处理接口
	 */
	public GSNetClient(String serverIp, int serverPort, CallBackInterface callback) {
		this.serverIp = serverIp;
		this.serverPort = serverPort;
		this.callback = callback;
		this.timeout = Integer.parseInt(SystemConfig.getProperty("gsnet.timeout"));
		this.heartBeatInterval = Integer.parseInt(SystemConfig.getProperty("gsnet.heartBeatInterval"));
		this.retryInterval = Integer.parseInt(SystemConfig.getProperty("gsnet.retry.interval"));
		this.retryAttempts = Integer.parseInt(SystemConfig.getProperty("gsnet.retry.attempts"));
	}

	/**
	 * 基类获取系统唯一SessionId方法
	 * 
	 * @return uiSessionId
	 */
	public synchronized static int getUniqueSessionId() {
		int tempid;
		if (SessionId < 65535)
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
	public void init() throws Exception {
		NioSocketConnector connector = null;
		if (this.ioSession == null || !this.ioSession.isConnected()) {
			try {
				connector = this.getConnector();
				this.ioSession = this.connect(connector,
						new InetSocketAddress(this.getServerIp(), this.getServerPort()));
				if (ioSession != null && ioSession.isConnected()) {
					logger.info("连接已建立,通信端口为{}", new Object[] { ioSession.getLocalAddress() });
				} else {
					connector.dispose();
					throw new GSNetCannotConnectException();
				}
			} catch (Exception e) {
				if (connector != null) {
					connector.dispose();
				}
				logger.debug("{}", e);
				throw new GSNetCannotConnectException();
			}
		}
	}

	/**
	 * 得到配置好的异步连接器
	 * 
	 * @return
	 */
	private NioSocketConnector getConnector() {
		// 初始化与PMS通信的SDK Client端
		// NioSocketConnector(1)，值设为1的作用是一个用户只需要一条connector连接到服务器，如果不主动设置
		// MINA会主动申请cpu+1条线程，但是实际上到的只是线程池里面第一条，造成的结果是系统生成4条多余的回环，大量浪费系统资源
		NioSocketConnector connector = new NioSocketConnector(1);
		// 创建接收数据的过滤器
		connector.setConnectTimeoutMillis(this.getTimeout());
		DefaultIoFilterChainBuilder clinetChain = connector.getFilterChain();

		clinetChain.addLast("pCommCodec", new ProtocolCodecFilter(new GSNetCodecFactory(false)));
		clinetChain.addLast("threadPool", new ExecutorFilter(Executors.newCachedThreadPool()));

		if (this.sendHeartBeat) {
			KeepAliveMessageFactory heartBeatFactory = new HeartBeatFactory(this.heartBeatString);
			// KeepAliveRequestTimeoutHandler heartBeatHandler = new
			// KeepAliveRequestTimeoutHandlerImpl();
			KeepAliveFilter heartBeat = new KeepAliveFilter(heartBeatFactory, IdleStatus.WRITER_IDLE,
					KeepAliveRequestTimeoutHandler.DEAF_SPEAKER);
			/** 是否回发 */
			heartBeat.setForwardEvent(false);
			/** 发送频率 */
			heartBeat.setRequestInterval(this.getHeartBeatInterval());
			connector.getFilterChain().addLast("heartbeat", heartBeat);

		}
		connector.getSessionConfig().setKeepAlive(false);

		// 创建处理器
		IoHandler handler = new GSNetServiceHandler(callback);
		// 创建IoSession重连过滤器
		/*
		 * ReconnectionFilter reconnectionFilter = new
		 * ReconnectionFilter(this.getRetryInterval(),this.getRetryAttempts());
		 * connector.getFilterChain().addLast("reconnect",reconnectionFilter);
		 */
		connector.setHandler(handler);
		return connector;
	}

	/**
	 * 通过连接器与连接地址连接到服务器并通过心跳机制保存长连接
	 * 
	 * @param connector
	 * @param remoteAddress
	 * @return
	 * @throws Exception
	 */
	private IoSession connect(NioSocketConnector connector, InetSocketAddress remoteAddress) throws Exception {
		IoSession ioSession = null;
		logger.info("开始连接服务器，地址是{}", new Object[] { remoteAddress });
		for (int i = 0; i < 5; i++) {
			try {
				ConnectFuture cf = connector.connect(remoteAddress);
				// 等待创建连接
				cf.awaitUninterruptibly();
				ioSession = cf.getSession();
				if (ioSession != null) {
					return ioSession;
				}
				Thread.sleep(200);
			} catch (Exception e) {
				Thread.sleep(200);
			}
		}
		logger.info("服务器连接失败，地址是{}", new Object[] { remoteAddress });
		return ioSession;
	}

	public boolean isConnected() {
		return (this.ioSession != null && this.ioSession.isConnected());
	}

	public boolean closeSession() {
		this.ioSession.close(true);
		if (this.ioSession.getService() != null)
			this.ioSession.getService().dispose();
		this.ioSession = null;

		return true;
	}

	/**
	 * 发送request消息
	 * 
	 * @param data
	 *            消息内容
	 * @param messageType
	 *            消息类型
	 * @return
	 * @throws Exception
	 */
	public int sendMessage(byte[] data, String messageType) throws Exception {
		BaseMessage messgage = new BaseMessage();
		messgage.getHead().setMessageType(messageType);
		messgage.getHead().setRequestMessage(true);
		messgage.setData(data);
		return this.send(messgage);
	}

	/**
	 * 发送response消息
	 * 
	 * @param data
	 *            消息内容
	 * @param messageType
	 *            消息类型
	 * @return
	 * @throws Exception
	 */
	public int responseMessage(byte[] data, String messageType) throws Exception {
		BaseMessage messgage = new BaseMessage();
		messgage.getHead().setMessageType(messageType);
		messgage.getHead().setRequestMessage(false);
		messgage.setData(data);
		return this.send(messgage);
	}

	private int send(BaseMessage messgage) throws Exception {
		if (ioSession != null && ioSession.isConnected())// 只有connected状态下才能发送
			ioSession.write(messgage);
		else {
			if (ioSession != null) {// 如果为空或断开状态下清理资源,
				ioSession.getService().dispose();
			}
		}
		return 0;
	}

	/**
	 * 服务器启动处理
	 * 
	 * @throws Exception
	 */
	/*
	 * @PostConstruct private void postConstruct() throws Exception{
	 * 
	 * }
	 */

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public LogLevel getLoggerLevel() {
		if (logger.isDebugEnabled())
			return LogLevel.DEBUG;
		else
			return LogLevel.ERROR;
	}

	public int getHeartBeatInterval() {
		return heartBeatInterval;
	}

	public void setHeartBeatInterval(int heartBeatInterval) {
		this.heartBeatInterval = heartBeatInterval;
	}

	public int getRetryInterval() {
		return retryInterval;
	}

	public void setRetryInterval(int retryInterval) {
		this.retryInterval = retryInterval;
	}

	public int getRetryAttempts() {
		return retryAttempts;
	}

	public void setRetryAttempts(int retryAttempts) {
		this.retryAttempts = retryAttempts;
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public String getHeartBeatString() {
		return heartBeatString;
	}

	public void setHeartBeatString(String heartBeatString) {
		this.heartBeatString = heartBeatString;
	}

	public boolean isSendHeartBeat() {
		return sendHeartBeat;
	}

	public void setSendHeartBeat(boolean sendHeartBeat) {
		this.sendHeartBeat = sendHeartBeat;
	}
}
