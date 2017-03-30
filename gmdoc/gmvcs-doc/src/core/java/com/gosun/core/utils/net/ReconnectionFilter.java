package com.gosun.core.utils.net;


import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mina中Session重连接过滤器
 * @author Abe
 *
 */
public class ReconnectionFilter extends IoFilterAdapter {
    
    private static Logger log = LoggerFactory.getLogger(ConnectionThread.class);
    
    private static AtomicBoolean sysadminReconnecting = new AtomicBoolean(false);
    
    private int interval;//重连时间间隔
    private int attempts;//重连次数
    
    public ReconnectionFilter(){
        
    }
    
    public ReconnectionFilter(int interval, int attempts) {
        this.interval = interval*1000;
        this.attempts = attempts;
    }
    
    public void sessionClosed(NextFilter nextFilter, IoSession session) {
        if(session.getAttribute("active") == null){//当ioSession是主动关闭就不用重连了
    		ConnectionThread connectionThread = new ConnectionThread(session);
            connectionThread.start();
        }
        nextFilter.sessionClosed(session);
    }

    /**
     * 重连线程类,一个Session或者长连接一个重连接线程维护
     * @author Abe
     *
     */
    private class ConnectionThread extends Thread {
        
        /**需要重连的Session,但重连后要把原来的清掉把新的IoSession放到对应的HttpSession**/
        private IoSession ioSession;
        
        private ConnectionThread() {
            super();
        }
        private ConnectionThread(IoSession ioSession) {
            super();
            this.ioSession = ioSession;
        }
        /**
         * 线程入口
         */
        public void run() {
        	
        }
    }


    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }
}
