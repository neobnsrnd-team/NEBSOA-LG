/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
package nebsoa.monitor.client;

import nebsoa.monitor.MonitorLogManager;

import org.apache.mina.common.IdleStatus;
import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;

/*******************************************************************
 * <pre>
 * 1.설명 
 * UDP기반으로 Agent가 모니터링 데이터를 전송. 
 * 
 * 2.사용법
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author 이종원
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 
 * $LogManager: MonitorDataSender.java,v $
 * </pre>
 ******************************************************************/
public class MonitorClientUdpHandler extends IoHandlerAdapter {
	
    public void exceptionCaught(IoSession session, Throwable cause)
            throws Exception {
        cause.printStackTrace();
    }

    public void messageReceived(IoSession session, Object message)
            throws Exception {
        MonitorLogManager.debug("Session recv...");
    }

    public void messageSent(IoSession session, Object message) throws Exception {
        MonitorLogManager.debug("Message sent...");
    }

    /**
     * 일정시간 사용하지 않으면 <code>IoSession</code>은 close된다.
     * <code>IoSession</code>이 종료되어야 쓰레드로 종료된다.
     */
    public void sessionClosed(IoSession session) throws Exception {
        MonitorLogManager.debug("Session closed...");
    }

    public void sessionCreated(IoSession session) throws Exception {
        MonitorLogManager.debug("Session created...");
    }

    public void sessionIdle(IoSession session, IdleStatus status)
            throws Exception {
        MonitorLogManager.debug("Session idle...");
    }

    /**
     * <code>IoSession<code>이 open 되었다고 연결이 된 것은 아니다.
     * 만약 서버에 연결되지 않으면 <code>java.net.PortUnreachableException<code>이 발생한다.
     */
    public void sessionOpened(final IoSession session) throws Exception {
        MonitorLogManager.debug("Session opened...");
    }
    
    
}
