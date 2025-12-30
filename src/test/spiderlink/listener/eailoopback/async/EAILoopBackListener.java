/*******************************************************************
 * 외환은행 CLS 연계 프로젝트
 *
 * Copyright (c) 2003-2004 by COMAS, Inc.
 * All rights reserved.
 * @file : EAIListener.java
 * @author : 이종원
 * @date : 2004. 9. 2.
 * @설명 : 
 * <pre>
 * 
 * </pre>
 *******************************************************************
 * $Id: EAILoopBackListener.java,v 1.1 2018/01/15 03:39:50 cvs Exp $
 * $Log: EAILoopBackListener.java,v $
 * Revision 1.1  2018/01/15 03:39:50  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:45  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:24  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:27  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.2  2008/02/20 00:42:48  오재훈
 * *** empty log message ***
 *
 * Revision 1.1  2007/11/26 08:39:09  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2006/06/29 13:28:44  이종원
 * *** empty log message ***
 *
 * Revision 1.2  2006/06/06 07:59:26  jwlee
 * *** empty log message ***
 *
 * Revision 1.1  2006/03/05 20:27:16  jwlee
 * *** empty log message ***
 *
 * Revision 1.2  2004/10/15 08:37:19  helexis
 * formatting 이후 재 commit
 *
 * Revision 1.11  2004/09/13 10:10:56  ljw
 * *** empty log message ***
 *
 * Revision 1.10  2004/09/10 07:51:24  ljw
 * *** empty log message ***
 *
 * Revision 1.9  2004/09/10 07:02:29  ljw
 * eai sync async service thread 최초 작성
 *
 * Revision 1.8  2004/09/10 06:55:58  ljw
 * thread방식으로 변환
 *
 * Revision 1.7  2004/09/08 01:07:48  ljw
 * *** empty log message ***
 *
 * Revision 1.6  2004/09/06 08:39:49  ljw
 * *** empty log message ***
 *
 * Revision 1.5  2004/09/03 04:43:43  ljw
 * *** empty log message ***
 *
 * Revision 1.4  2004/09/03 00:22:42  ljw
 * *** empty log message ***
 *
 * Revision 1.3  2004/09/02 13:11:21  ljw
 * *** empty log message ***
 *
 * Revision 1.2  2004/09/02 06:23:54  ljw
 * *** empty log message ***
 *
 * Revision 1.1  2004/09/02 06:22:34  ljw
 * *** empty log message ***
 *    
 ******************************************************************/
package test.spiderlink.listener.eailoopback.async;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import nebsoa.common.exception.SysException;
import nebsoa.common.util.PropertyManager;

/**
 * 2004. 9. 2. 이종원
 * 설명 :
 * <pre>
 * eai로부터 전문을 수신 하는 리스너의 기본이 되는 클래스
 * 이 클래스를 상속 받아 KEBEAIListener와 KFTCEAIListener를 만든다.
 * </pre> 
 */
public class EAILoopBackListener {

    protected ServerSocket serverSocket;

    protected int listenPort = 20033;

    protected boolean isSync;

    protected String logConfig="MESSAGE_TEST";

    protected int connectCount;

    protected int resetCount;

    /**
     * 생성자
     */
    public EAILoopBackListener(int listenPort) {
        this.listenPort = listenPort;
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

                new EAILoopBackServiceThread(socket, logConfig).start();

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
     * 
     * 2004. 9. 10. 이종원 작성
     * @param syncType
     * @return
     * 설명: 수신 포트를 프로퍼티 파일에서 얻어 옵니다.
     */
    public static int getPort(boolean syncType) {

        String portStr = null;
        if (syncType) {
            portStr = PropertyManager.getProperty("connector",
                    "KEB_EAI_RECV_PORT");
        } else {
            portStr = PropertyManager.getProperty("connector",
                    "KFTC_EAI_RECV_PORT");
        }
        try {
            return Integer.parseInt(portStr);
        } catch (Exception e) {
            System.out.println("PORT 설정이 잘못 된 것 같습니다" + e.getMessage());
            throw new SysException(e.getMessage());
        }
    }

    /**
     * 
     * 2004. 9. 10. 이종원 작성
     * @param args
     * 설명: Listener를 start 시킨다. 
     * 텐덤은 뒤에 ASYNC, 내부 전문은 SYNC라는 인자를 주어 구분 한다.
     */
    public static void main(String[] args) {
        int port=20033;
        if(args.length >= 1){
            port=Integer.parseInt(args[0]);
        }
        System.out.println("loopback LISTENER를 시작 시킵니다.");
        
        EAILoopBackListener server = new EAILoopBackListener(port);
      
        server.service();
    }

}