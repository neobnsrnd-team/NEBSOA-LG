/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.socketpool;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;
import nebsoa.spiderlink.connector.io.reader.AsyncMessageReader;
import nebsoa.util.threadpool.ThreadPool;
import nebsoa.util.threadpool.ThreadPoolManager;
import nebsoa.util.threadpool.WorkerThread;
/**
 * worker Socket
 * @author 이종원
 */
/*******************************************************************
 * <pre>
 * 1.설명 
 * SocketPool 에서 관리되는 Socket 객체.
 * Async 로 read하기 위해서 생성자에  AsyncMessageReader를 넣어 생성하야야 한다.
 * 넣어 주기만 하면 내부적으로 thread pool로 부터 thread를 할당 받아 
 * AsyncMessageReader를 실행 시킨다.
 * 
 * 2.사용법
 * 
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author 이종원
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 
 * $Log: AsyncWorkerSocket.java,v $
 * Revision 1.1  2018/01/15 03:39:51  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:58  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:24  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:28  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:52  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:24  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:37:39  안경아
 * *** empty log message ***
 *
 * Revision 1.11  2007/06/07 09:19:42  이종원
 * Thread pool max idle 갯수 조정
 *
 * Revision 1.10  2007/03/24 04:52:24  최수종
 * 상속받은 클래스에서 접근 가능하도록 하기 위해
 * 멤버변수의 접근지정자를 default -> protected 로 변경
 *
 * Revision 1.9  2007/02/01 08:46:53  이종원
 * toString추가
 *
 * Revision 1.8  2007/01/02 08:25:37  이종원
 * *** empty log message ***
 *
 * Revision 1.7  2007/01/02 08:10:14  이종원
 * WorkerSocket안에 Thread reset하는 로직 제거
 *
 * Revision 1.6  2007/01/02 04:25:35  이종원
 * setError 수정
 *
 * Revision 1.5  2007/01/02 04:15:25  이종원
 * socket close하는 로직 추가
 *
 * Revision 1.4  2006/10/16 01:48:41  이종원
 * *** empty log message ***
 *
 * Revision 1.3  2006/10/03 09:12:44  이종원
 * refactoring
 *
 * Revision 1.2  2006/10/02 02:46:38  이종원
 * *** empty log message ***
 *
 * Revision 1.1  2006/09/27 10:22:53  이종원
 * 디렉토리변경
 *
 * Revision 1.5  2006/09/27 08:08:24  이종원
 * log양 줄임
 *
 * Revision 1.4  2006/09/27 03:07:01  이종원
 * socket count하는 로직 수정 및 read bug수정
 *
 * Revision 1.3  2006/09/23 00:52:33  이종원
 * *** empty log message ***
 *
 *
 * </pre>
 ******************************************************************/
public class AsyncWorkerSocket extends WorkerSocket {
   
	protected AsyncMessageReader reader;
	protected WorkerThread thread;
   
    public AsyncWorkerSocket(Socket socket,String logConfig) {
        super(socket,logConfig);
    }

    public AsyncWorkerSocket(Socket socket) {
        this(socket,null);
    }
    
    
    public void openInputStream() throws IOException {
       if(dataInputStream == null){
           try {
               dataInputStream = new DataInputStream(socket.getInputStream());
               //set datainputstream to read thread
               reader.setDataInputStream(dataInputStream);
               //start read thread
               ThreadPool threadPool = ThreadPoolManager.getInstance().getPool(
                       "ASYNC_READ_SOCKET_POOL",2);
               thread = (WorkerThread) threadPool.borrowObject();
               thread.execute(reader);
               //LogManager.debug("START ASYNC READER OK---------");
           } catch (IOException e) {
               LogManager.error(logConfig,e.getMessage(),e);
               throw e;
           } catch (Exception e) {
               throw new SysException (e);
           }
       }   
    }

    public AsyncMessageReader getReader() {
        return reader;
    }

    public void setReader(AsyncMessageReader reader) {
        this.reader = reader;
    }
    
    /**
     * @param stopped The stopped to set.
     */
    public void setError(boolean flag) {
        if(flag==true){
            setStopped(flag);
            destroy();
        }
    }

    public void destroy() {
        if(getRemains()>0){
            LogManager.infoLP(logConfig,this+"남은 전문 : "+getRemains());
            if(getCause() != null){
                LogManager.infoP(logConfig,this+" close Reason:"+getCause());
            }
        }
        
        close();

        try {
            SocketPool pool = getPool();
            if(pool != null && !pool.closed() ){
                pool.evict();   
                LogManager.debug(logConfig,"evict Async Reader Socket");
            }
        } catch (Exception e) {
             //LogManager.error(logConfig,e.toString(),e);
        }
    }
    
    public String toString(){
        return "AsyncWorkerSocket@"+super.toString();
    }
}