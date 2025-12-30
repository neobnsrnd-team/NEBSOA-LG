/*
 * Spider Framework
 *
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 *
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.management.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import nebsoa.common.log.LogManager;
import nebsoa.common.startup.StartupContext;
import nebsoa.common.util.PropertyManager;

/*******************************************************************
 * <pre>
 * 1.설명
 * Management 대상이 되는 서버에서 데몬으로 수행되어야 하는 서버 클래스입니다.
 *
 * 2.사용법
 *
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author $Author: cvs $
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 *
 * $Log: ManagementServer.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:23  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.2  2009/03/09 05:10:20  김보라
 * 프레임웍의 소켓 close 관련 로직 반영
 *
 * Revision 1.1  2008/11/18 11:27:26  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:29  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.5  2008/11/11 06:03:32  youngseokkim
 * MANAGEMENT_SERVER_PORT 오타 수정
 *
 * Revision 1.4  2008/11/05 01:12:56  김성균
 * init 조건 수정
 *
 * Revision 1.3  2008/10/24 09:21:14  김성균
 * 종료시 서버소켓연결하지 않도록 수정
 *
 * Revision 1.2  2008/10/22 08:28:50  김성균
 * 쓰레드로 수행되도록 수정
 *
 * Revision 1.1  2008/10/16 00:08:54  김성균
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class ManagementServer extends Thread {

    private static ManagementServer mserver = new ManagementServer();

    protected ServerSocket serverSocket;

    protected int listenPort;

    protected boolean isStop = false;

    private ManagementServer() {
    }

    public static ManagementServer getInstance() {
        if (mserver == null) {
            mserver = new ManagementServer();
        }
        return mserver;
    }

    public void run() {
        service();
    }

    public void service() {
    	try {
    		init();
            Socket socket = null;
            while (!isStop && serverSocket != null && !serverSocket.isClosed()) {
                try {
                	LogManager.info("ManagementServer> 요청을 기다리고 있습니다.....");
    				socket = serverSocket.accept();
    				socket.setSoTimeout(5*1000); // 5초 타임아웃 설정
    				LogManager.info("ManagementServer> Socket 연결됨:" + socket);
    				new ManagementServerWorker(socket).start();
    				socket=null;
                }catch(SocketTimeoutException e){
                	LogManager.info("ManagementServer> 1시간 동안 요청이 없었습니다......");
    			} catch (IOException e) {
    				init();
    			}
    			if(socket != null){
    				try{
    					socket.close();
    				} catch (Exception e) {
    				}
    			}
            }
            destroy();
		} catch (Exception e) {
		    LogManager.error(e);
		}
    }

    public void init() {

        listenPort = getListenPort();

        try {
            if (!isStop && (serverSocket == null || serverSocket.isClosed())) {
                LogManager.info("ManagementServer> LISTENER OPEN PORT : [" + listenPort + "]");
                serverSocket = new ServerSocket(listenPort);
                LogManager.info("ManagementServer> " + listenPort + "번 포트를 열었습니다.");
            }
        } catch (IOException e) {
            LogManager.error(e.getMessage());
            serverSocket = null;
        }
    }

    public void finalize() {
        destroy();
    }

    /**
	 * close all
	 */
    public void destroy() {
        isStop = true;
        try {
            if (serverSocket != null)
                serverSocket.close();
        } catch (IOException e) {
        }
        serverSocket = null;
    }

    /**
     * @return
     */
    private int getListenPort() {
        return PropertyManager.getIntProperty("was_config", StartupContext.getInstanceId() + ".MANAGEMENT_SERVER_PORT", 50005);
    }
}

