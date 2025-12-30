/*
 * Spider Framework
 *
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 *
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
package nebsoa.spiderlink.connector.io.reader;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.PropertyManager;
import nebsoa.spiderlink.socketpool.AsyncWorkerSocket;

/*******************************************************************
 * <pre>
 * 1.설명
 * ASYNC 전문을 수신하는 클래스를 만들 때 기본이 되는 클래스.
 * readMessage()라는 메소드가 stoped변수가 true일동안
 * 무한 루프 돌면서 THREAD로 수행 된다.
 * 2.사용법
 * 생성자 참조.
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author $Author: cvs $
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 *
 * $Log: AsyncMessageReader.java,v $
 * Revision 1.1  2018/01/15 03:39:51  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:59  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.3  2010/01/12 10:45:22  유정수
 * error로그 추가
 *
 * Revision 1.2  2010/01/12 10:38:17  유정수
 * eof excption일때 error 로그 찍도록
 *
 * Revision 1.1  2008/11/18 11:27:24  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:28  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:33  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:39:09  안경아
 * *** empty log message ***
 *
 * Revision 1.15  2007/03/29 07:47:44  최수종
 * logConfig 접근지정자를 default -> protected 로 변경(상속받아 쓰기 위함)
 *
 * Revision 1.14  2007/02/01 09:57:42  이종원
 * *** empty log message ***
 *
 * Revision 1.13  2007/01/02 04:15:56  이종원
 * socket close하는 로직 수정
 *
 * Revision 1.12  2006/11/24 09:31:40  이종원
 * log config 수정
 *
 * Revision 1.11  2006/11/10 05:41:19  김승희
 * *** empty log message ***
 *
 * Revision 1.10  2006/10/17 08:52:01  이종원
 * log수정
 *
 * Revision 1.9  2006/10/16 03:46:32  이종원
 * default log file 수정
 *
 * Revision 1.8  2006/10/16 02:06:02  이종원
 * *** empty log message ***
 *
 * Revision 1.7  2006/10/16 02:03:55  이종원
 * 남은 전문이 있는데 close될 경우 이유 판단 로직 추가
 *
 * Revision 1.6  2006/10/16 01:51:41  이종원
 * *** empty log message ***
 *
 * Revision 1.5  2006/10/16 01:26:36  이종원
 * *** empty log message ***
 *
 * Revision 1.4  2006/10/16 00:14:31  이종원
 * 로그 수정
 *
 * Revision 1.3  2006/10/13 08:13:42  이종원
 * *** empty log message ***
 *
 * Revision 1.2  2006/10/13 02:35:16  이종원
 * *** empty log message ***
 *
 * Revision 1.1  2006/10/03 09:12:44  이종원
 * refactoring
 *
 * Revision 1.7  2006/09/29 00:40:47  이종원
 * *** empty log message ***
 *
 * Revision 1.6  2006/09/28 14:04:03  이종원
 * *** empty log message ***
 *
 * Revision 1.5  2006/09/27 10:23:34  이종원
 * *** empty log message ***
 *
 * Revision 1.4  2006/09/23 02:25:28  이종원
 * *** empty log message ***
 *
 * Revision 1.3  2006/09/23 01:26:46  이종원
 * 변경없음
 *
 * Revision 1.2  2006/09/23 00:55:40  이종원
 * 기능update
 *
 * Revision 1.1  2006/09/22 22:57:52  이종원
 * 최초작성
 *
 * </pre>
 ******************************************************************/
public abstract class AsyncMessageReader
            implements Runnable{
    protected String logConfig=PropertyManager.getProperty(
            "spiderlink","MSG_MONITOR_LOG","DEBUG");
    protected AsyncWorkerSocket worker;
    protected DataInputStream dataInputStream;
    long lastWorkTime=System.currentTimeMillis();
    public AsyncMessageReader() {

    }


    public void setInputStream(InputStream in) {
        if(dataInputStream != null){
            throw new SysException("Stream is asigned.. already..");
        }
        if(in instanceof DataInputStream){
            this.dataInputStream = (DataInputStream) in;
        }else{
            dataInputStream = new DataInputStream(in);
        }
    }

    public void run() {
        if(worker==null){
            throw new SysException("Worker socket is null...");
        }
        if(worker.getDataInputStream()==null){
            worker.close();
            throw new SysException("DataInputStream is null...");
        }

        logConfig = worker.getLogConfig();

        try{
            while(!isStpped()){
                try {
                    handleInputMessage();
                    worker.decreaseRemains();
                    lastWorkTime = System.currentTimeMillis();
                } catch (SocketTimeoutException e) {
                    double term = System.currentTimeMillis()-lastWorkTime;
                    //오래 도록 요청이 없었으면... 이젠 종료 시킨다...
                    if(worker.getDestroyThreshold() > 0 &&
                            (term > worker.getDestroyThreshold())){
                        LogManager.info(logConfig,"AsyncReader:: Socket Read Timeout. 남은전문 :"
                                +worker.getRemains()+",쉰 기간 :"+(term/1000.0)+"초");
                        if(worker.getRemains() > 0){
                            worker.stopWithException(new Exception(
                                    "Socket Read Timeout::남은 전문이 있으나, Timeout발생으로 Socket Close함 .남은전문 갯수 : "+worker.getRemains()));
                        }else{
                            worker.stop();
                        }
                    }else{
                        //LogManager.debug("read timeout : threshold 까지는  무시함.");
                    }
                } catch (SocketException e) {
                    LogManager.info(logConfig,"소켓이 닫혀짐:"+e);
                    worker.stopWithException(e);
                } catch (EOFException e) {
                    LogManager.info(logConfig,"소켓 연결 종료 시그날 수신 ");
                    LogManager.error("소켓 연결 종료 시그날 수신"+e,e);
                    worker.stopWithException(e);
                } catch(IOException e) {
                    LogManager.error(logConfig,"소켓 통신 오류 : "+e, e);
                    worker.stopWithException(e);
                } catch (Exception e){
//                    //아직 읽을 전문이 더 있으니... 참자...
//                    if(worker.getRemains() > 0){
//
//                    }
                    LogManager.error(logConfig,"기타  오류 : "+e, e);
                    worker.stopWithException(e);
                }
            }
        }finally{
            if(worker != null){
                worker.destroy();
            }
        }
    }

    public boolean isStpped() {
        if(worker==null) return true;
        return worker.isStopped();
    }





    public DataInputStream getDataInputStream() {
        return dataInputStream;
    }

    public void setDataInputStream(DataInputStream dataInputStream) {
        this.dataInputStream = dataInputStream;
    }



    public void setStpped(boolean stpped) {
        worker.setStopped(stpped);
    }

    /**
     * 상속받는 클래스에서 구현할 메소드 입니다.
     * 무한루푸 돌면서 이 메소드가 계속 호출 됩니다.
     * 2006. 9. 22.  이종원 작성
     */
    public abstract void handleInputMessage() throws IOException;

    public AsyncWorkerSocket getWorker() {
        return worker;
    }

    public void setWorker(AsyncWorkerSocket worker) {
        LogManager.debug("worker-->"+worker);
        this.worker = worker;
    }
}
