/*******************************************************************
 * 외환은행 CLS 연계 프로젝트
 *
 * Copyright (c) 2003-2004 by COMAS, Inc.
 * All rights reserved.
 * @file : RoutingListener.java
 * @author : 이종원
 * @date : 2004. 9. 2.
 * @설명 : 
 * <pre>
 * 
 * </pre>
 *******************************************************************
 * $Id: RoutingListener.java,v 1.1 2018/01/15 03:39:54 cvs Exp $
 * $Log: RoutingListener.java,v $
 * Revision 1.1  2018/01/15 03:39:54  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:40  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:27  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:29  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:51  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2007/11/26 08:37:37  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2007/07/15 10:04:19  이종원
 * *** empty log message ***
 *
 ******************************************************************/
package test.spiderlink.listener.router;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 설명 :
 * <pre>
 * 라우팅 서비스 리스너 데몬
 * </pre> 
 */
public class RoutingListener {

    protected ServerSocket serverSocket;

    protected int listenPort = 60001;

    protected boolean isSync;

    protected String logConfig="CONSOLE";

    protected int connectCount;

    protected int resetCount;
    
    String targetIp = "localhost";
    
    int targetPort = 10001;

    /**
     * 생성자
     */
    public RoutingListener(int listenPort) {
        this.listenPort = listenPort;
    }
    
    /**
     * 생성자
     */
    public RoutingListener(int listenPort,
            String targetIp, int targetPort) {
        this.listenPort = listenPort;
        this.targetIp = targetIp;
        this.targetPort = targetPort;
    }

    public void init() {

        try {
            if (serverSocket == null || serverSocket.isClosed()) {
                System.out.println( "LISTENER OPEN PORT : ["
                        + listenPort + "]");
                serverSocket = new ServerSocket(listenPort);
                System.out.println( listenPort + "번 포트를 열었습니다.");
                resetCount++;
                if (resetCount > 1) {
                    System.out.println( resetCount
                            + "번째 RESET 작업이 수행되었습니다.");
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            serverSocket = null;
        }
    }

    public void service() {
        init();
        Socket socket = null;
        while (serverSocket != null && !serverSocket.isClosed()) {
            try {
                socket = serverSocket.accept();
                connectCount++;
                System.out.println( connectCount + "번째 소켓을 열었습니다.");

                new RouterServiceThread(socket,targetIp, targetPort,logConfig).start();

            } catch (IOException e) {
                init();
            }
        }
        destroy();
    }

    /**
     * close all
     */
    public void destroy() {
        try {
            if (serverSocket != null)
                serverSocket.close();
        } catch (IOException e) {
        }
        serverSocket = null;
    }



    /**
     */
    public static void main(String[] args) {
        int port=10001;
        if(args.length >= 1){
            port=Integer.parseInt(args[0]);
        }
        
        String targetIp="localhost";
        int targetPort=20001;
        
        System.out.println("RoutingListener를 시작 시킵니다.port="+port);
        
        RoutingListener server = new RoutingListener(port,targetIp,targetPort);
      
        server.service();
    }

}