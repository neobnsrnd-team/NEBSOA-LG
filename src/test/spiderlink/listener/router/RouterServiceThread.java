/*******************************************************************
 * 외환은행 CLS 연계 프로젝트
 *
 * Copyright (c) 2003-2004 by COMAS, Inc.
 * All rights reserved.
 * @file : EAIServiceThread.java
 * @author : 이종원
 * @date : 2004. 9. 2.
 * @설명 : 
 * <pre>
 * 
 * </pre>
 *******************************************************************
 * $Id: RouterServiceThread.java,v 1.1 2018/01/15 03:39:54 cvs Exp $
 * $Log: RouterServiceThread.java,v $
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
 * Revision 1.2  2007/07/15 10:04:19  이종원
 * *** empty log message ***
 *
 * Revision 1.1  2006/07/04 11:34:26  이종원
 * *** empty log message ***
 *
 * Revision 1.3  2006/06/06 09:57:40  jwlee
 * *** empty log message ***
 *
 * Revision 1.2  2006/03/06 07:36:33  jwlee
 * *** empty log message ***
 *
 * Revision 1.1  2006/03/05 20:27:16  jwlee
 * *** empty log message ***
 *
 * Revision 1.4  2004/10/29 05:31:20  ljw
 * 에러 유형별로 응답 전문 전송하는 로직 수정
 *
 * Revision 1.3  2004/10/25 05:19:12  ljw
 * EOF처리 추가
 *
 * Revision 1.2  2004/10/15 08:37:19  helexis
 * formatting 이후 재 commit
 *
 * Revision 1.2  2004/09/17 06:35:30  helexis
 * EAI 전문 헤더 포맷 오류 체크로직 관련 로직 추가
 *
 * Revision 1.1  2004/09/10 07:51:24  ljw
 * *** empty log message ***
 *
 ******************************************************************/
package test.spiderlink.listener.router;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;




/**
 * 2004. 9. 2. 이종원
 * 설명 :
 * <pre>
 * 접속하는 client로 부터소켓을 할당 받아 스트림을 열고 read하여 routing한다.
 * 응답처리(client로 전송)는 ResponseThread를 생성하여 처리한다.
 * </pre> 
 */
public class RouterServiceThread extends Thread {

    boolean exit;

    protected RouteConnector connector;

    protected String logConfig="MESSAGE_TEST";

    static int totalThreadCount;

    int myRunningCount;

    static int runningThreadCount;
    
    String targetIp;
    int targetPort;

    /**
     * 생성자
     * @throws IOException 
     * @throws UnknownHostException 
     */
    public RouterServiceThread(Socket socket,
            String targetIp, int targetPort,
            String logConfig) throws UnknownHostException, IOException {
        this.logConfig=logConfig;
        this.targetIp = targetIp;
        this.targetPort = targetPort;
        Socket target = new Socket(targetIp, targetPort);
        myRunningCount = ++totalThreadCount;
        runningThreadCount++;
        System.out.println( "토탈 THREAD[" + totalThreadCount
                + "],현재 수행중인 쓰레드[" + runningThreadCount + "]");
        connector=new RouteConnector(socket,target,logConfig);
    }

    /**
     *  
     * 부모 클래스로 부터 물려 받은 함수 재정의
     * @see java.lang.Runnable#run()
     * 설명 : Sync processing serivce를 하는 부분 
     * 전문을 읽어서 처리 processor에게 넘긴다.
     */
    public void run() {
        while (!exit) {
            try {
                try{
                    connector.route();
                }catch(EOFException e){
                    System.out.println( "통신종료 패킷 수신-서비스 종료함");
                    exit = true;
                    //break;
                } catch (Exception e) {
                    System.out.println( "EAI SERVICE ERROR:"
                            + e.getMessage());
                    exit = true;
                   // break;
                }

 
            } catch (Exception e) {
                String errorMessage = e.getMessage();
                System.out.println( " ERROR : "
                        + errorMessage);
               //break;
                exit = true;
            }//end try catch
        }

        runningThreadCount--;

        connector.close();
    }
}