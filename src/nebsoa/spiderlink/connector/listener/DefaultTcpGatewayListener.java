/*
 * Spider Framework
 *
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 *
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.connector.listener;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Iterator;

import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.StringUtil;
import nebsoa.spiderlink.connector.GatewayConnector;
import nebsoa.spiderlink.connector.constants.ConnectorConstants;
import nebsoa.spiderlink.connector.listener.protocol.InputMessageHandler;
import nebsoa.spiderlink.connector.listener.protocol.TcpInputMessageHandler;
import nebsoa.spiderlink.connector.listener.worker.InputMessageWorkerFactory;
import nebsoa.util.threadpool.ThreadPool;
import nebsoa.util.threadpool.ThreadPoolManager;
import nebsoa.util.threadpool.WorkerThread;
import nebsoa.util.workerpool.WorkerPool;
import nebsoa.util.workerpool.WorkerPoolManager;

/*******************************************************************
 * <pre>
 * 1.설명
 * 타기관으로부터 전문 전송 수신을 처리하는 class만들 때 상속 받을 클래스
 * <font color='red'>
 * thread pool을 얻어 오는 로직을  여기서 구현하였다.
 * 따라서 상속만 받으면 thread pooling은 해결 된다.
 * </font>
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
 * $Log: DefaultTcpGatewayListener.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:34  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.7  2009/12/29 11:20:37  jglee
 * 로그 수정
 *
 * Revision 1.6  2009/12/29 11:07:38  jglee
 * ThreadMonitor 로그에 MAX값 추가
 *
 * Revision 1.5  2009/12/29 07:38:40  jglee
 * THREAD_MONITOR 로 모티터링 로그 파일 변경
 *
 * Revision 1.4  2009/11/13 13:45:34  jglee
 * 리스너 상태 업데이트 처리 삭제
 *
 * Revision 1.2  2009/07/16 03:23:27  김보라
 * 로그보강
 *
 * Revision 1.1  2008/11/18 11:27:27  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:29  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.2  2008/10/07 23:54:22  김성균
 * timeout이 999보다 작을경우 1000 곱하도록 변경
 *
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.2  2008/07/09 07:52:33  김영석
 * 무조건 연결이 안된다고 에러로 마킹 할 수는 없다.
 * 현재 사용되는 소켓이 것이 하나라도 있으면 에러가 아닌것으로 처리 되도록 수정
 *
 * Revision 1.1  2008/01/22 05:58:34  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.2  2007/12/10 09:46:01  이종원
 * error -> info로 낮춤
 *
 * Revision 1.1  2007/11/26 08:39:16  안경아
 * *** empty log message ***
 *
 * Revision 1.16  2007/06/07 09:19:42  이종원
 * Thread pool max idle 갯수 조정
 *
 * Revision 1.15  2007/05/11 08:41:02  이종원
 * accept(요청접속) timeout발생시 기 수행 중인 소켓을 닫는 버그 수정
 *
 * Revision 1.14  2007/05/11 07:59:01  김승희
 * ServerSocket Timeout 시에 client socket close 하는 로직 제거
 *
 * Revision 1.13  2007/02/26 10:43:20  이종원
 * serversocket reset 주기 읽어 오기 로직의 위치 변경
 *
 * Revision 1.12  2007/02/21 06:35:47  이종원
 * daemon_reset_threshold 파라미터 주기로 reset하도록 수정
 *
 * Revision 1.11  2007/01/03 01:17:50  이종원
 * log level 수정
 *
 * Revision 1.10  2006/10/16 00:56:44  이종원
 * Serversocket Reset time 10분으로 변경
 *
 * Revision 1.9  2006/10/13 12:14:20  이종원
 * Logging MSG_ERROR로 수정
 *
 * Revision 1.8  2006/10/13 12:06:22  이종원
 * Logging MSG_ERROR로 수정
 *
 * Revision 1.7  2006/10/13 05:40:26  이종원
 * log 설정 파일 변경
 *
 * Revision 1.6  2006/10/10 07:21:52  이종원
 * addHandler추가
 *
 * Revision 1.5  2006/10/10 05:52:52  이종원
 * exit 변수 추가
 *
 * Revision 1.4  2006/10/10 02:15:52  이종원
 * 에러시 socket close로직 추가
 *
 * Revision 1.3  2006/10/10 02:13:40  이종원
 * exit변수 추가
 *
 * Revision 1.2  2006/10/10 01:50:29  이종원
 * service메소드 수정
 *
 * Revision 1.1  2006/10/03 09:12:44  이종원
 * refactoring
 *
 * Revision 1.13  2006/10/03 05:56:20  이종원
 * isBound->isClosed로 수정...
 *
 * Revision 1.12  2006/10/03 05:52:21  이종원
 * *** empty log message ***
 *
 * Revision 1.11  2006/10/03 05:23:56  이종원
 * wait for die serversocket
 *
 * Revision 1.10  2006/10/03 05:19:22  이종원
 * *** empty log message ***
 *
 * Revision 1.9  2006/10/03 04:48:20  이종원
 * *** empty log message ***
 *
 * Revision 1.8  2006/10/03 03:23:02  이종원
 * *** empty log message ***
 *
 * Revision 1.7  2006/10/03 03:17:49  이종원
 * *** empty log message ***
 *
 * Revision 1.6  2006/10/03 00:16:59  이종원
 * 주석 달기
 *
 * Revision 1.5  2006/10/03 00:03:23  이종원
 * socket timeout setting
 *
 * Revision 1.4  2006/10/02 23:42:55  이종원
 * *** empty log message ***
 *
 * Revision 1.3  2006/10/02 06:40:34  이종원
 * *** empty log message ***
 *
 * Revision 1.2  2006/09/29 08:56:35  이종원
 * *** empty log message ***
 *
 * Revision 1.1  2006/09/29 08:49:37  이종원
 * *** empty log message ***
 *
 *
 * </pre>
 ******************************************************************/
public  class DefaultTcpGatewayListener
    extends GatewayConnector implements Runnable {

    protected ServerSocket serverSocket;

    protected ThreadPool threadPool;

    protected WorkerPool workerPool;

    protected long lastWorkTime=System.currentTimeMillis();

    /**
     * MessageHanlder들을 관리하는 ArrayList
     */
    protected ArrayList handlerList;
    /**
     * 서비스 종료 여부
     */
    public boolean exit=false;


    /**
     * ServerSocket Reset Threshold-초단위로 세팅
     */
    int daemonResetThreshold=3600;

    /**
     * 생성자.
     */
    public DefaultTcpGatewayListener() {
        handlerList = new ArrayList();
    }

    /**
     * server socket을 새로 연다..
     */
    public synchronized void reset(){
        //LogManager.debug(this+" reset--------------------------");
        if(isStopped()) {
            LogManager.info(logConfig,this+"-marked  stopped... so skip reset @@@@");
            return;
        }
        try {
            if (serverSocket == null ) {
                LogManager.infoLP(logConfig,getId()+ " LISTENER try to OPEN PORT : ["
                        + port + "]");
                // port and max num of queue(100개)
                serverSocket = new ServerSocket(port,100);//,InetAddress.getLocalHost());
                serverSocket.setSoTimeout(10*60*1000); //10분 동안 요청이 없으면 reset serversocket
                serverSocket.setReuseAddress(true);
                lastWorkTime=System.currentTimeMillis();
                LogManager.infoP(logConfig,getId()+" Listener가 IP:localhost(IP:"+ip
                        +"무시),Port:"+ port + "를 열었습니다.");
                // set exit flag false
                exit=false;
            }
        } catch (IOException e) {
            LogManager.error(logConfig,getId()+"Listener를 start하지 못하였습니다\n"+e, e);
            serverSocket = null;
            setError(true);
        }
    }

    public void init() {

        if(isStopped()){
            LogManager.debug(this+"listener marked 'STOP'. skip init..");
            return;
        }
        /////////////// serversocket reset 주기 읽어 오기 //////////
        try{
            daemonResetThreshold =
                super.getIntProperty("daemon_reset_threshold",3600);
        }catch(Exception e){
            daemonResetThreshold=3600;
            LogManager.error(logConfig, e.toString(),e);
        }
        /////////////// serversocket reset 주기 읽어 오기  끝 //////////

        try {
            //open serversocket
            reset();
            //make thread pool
            makeThreadPool();

            //make worker(Executor) pool
            makeWorkerPool();

        //이 listener자체도 thread이다...
            WorkerThread thread =
            (WorkerThread) ThreadPoolManager.getInstance()
            .getPool("DaemonThreadPool",1).borrowObject();
            thread.execute(this); // call run method..
            LogManager.infoP(getId()+"-Listener를 정상적으로 start하였습니다.");
        } catch (Exception e) {
            LogManager.error(e.getMessage(),e);
            setError(true);
        }
    }

    private void makeThreadPool() {
        if(threadPool==null){
            threadPool=ThreadPoolManager.getInstance()
                .getPool(getId()+"-Listener", 2);//
                   //     getOwner().getThreadCount());

            LogManager.infoP("ThreadPool생성:"+threadPool);
        }
    }
    /**
     * async일 때는 usePooling이 true이어야만 async로 동작 가능하다.
     * 2006. 10. 3.  이종원 작성
     */
    private void makeWorkerPool() {
        if(workerPool==null){
            workerPool=WorkerPoolManager.getInstance().getWorkerPool(
                    getId()+"-Listener",
                    new InputMessageWorkerFactory(getOwner().getPropertyMap()),
                    getOwner().getThreadCount(),
                    isAsync()?true:(usePooling()));

            LogManager.infoP(this+" Listener "+workerPool);
        }
    }


    /**
     * Listener Daemon을 start시킨다.
     */
    public void run(){
        try{
            service();
        }finally{
            closeAll();
        }
    }

    public void service() {

        Socket socket = null;

        while (!exit && !isStopped() && serverSocket != null ) {
            try {
                LogManager.debug(logConfig,"★★ " + getId()+ " Listener가 요청을 기다리고 있습니다.");
                //accept client connection
                socket = serverSocket.accept();
                if(!exit){
                    LogManager.debug(logConfig,"★★ " + getId()+ " Listener가 소켓을 열었습니다.");

                    // expire time  재 설정을 위해
                    lastWorkTime=System.currentTimeMillis();
                    //set socket read timeout
                    int timeout = getOwner().getPropertyMap().getIntParameter("timeout");
                    if(timeout<=999){
                        timeout = timeout*1000;
                    }
                    LogManager.debug(logConfig,"★★ " + getId()+ " Listener가 소켓 timeout:"+timeout);
                    socket.setSoTimeout(timeout);

                    //asign  thread
                    WorkerThread thread= (WorkerThread) threadPool.borrowObject();
                    LogManager.info("THREAD_MONITOR","★★" + thread.getPool().toString() + " MAX:[" +getOwner().getThreadCount()+"] ");

                    //[input_message_handler] 속성 값을 찾아 객체 생성.
                    TcpInputMessageHandler handler= createHandler(socket);
                    addHandler(handler);
                    thread.execute(handler);
                    //crear variable for safe
                    socket=null;

                }else{
                    LogManager.infoLP(this+" Listener Shutting down.......");
                    closeSocket(socket);
                }

            } catch (SocketTimeoutException e) {
                //LogManager.debug(logConfig,"read timeout-->요청이 없음.");
                //daemon_reset_threshold 파라미터 주기로 reset함.
                if(System.currentTimeMillis()-lastWorkTime > daemonResetThreshold*1000){
                    closeServerSocket();
                    reset();
                }
            } catch (Exception e) {
                closeSocket(socket);
                LogManager.error(logConfig,e.toString(),e);
                closeServerSocket();
                reset();
            } catch (Throwable e) {
                closeSocket(socket);
                stop();
                LogManager.error(logConfig,e.toString(),e);
            }
        }
    }

    private void closeSocket(Socket socket) {
        if(socket != null){
            try {
                socket.close();
            } catch (IOException ex) {

            }
        }
    }
    /**
     * [input_message_handler]속성 값으로 customizing된 핸들러를 생성한 후
     * socket, listener, configMap을 세팅 한다.
     * 2006. 10. 3.  KOREAN 작성
     * @param socket
     * @return
     * @throws Exception
     */
    private TcpInputMessageHandler createHandler(
             Socket socket) throws Exception {

        TcpInputMessageHandler handler=null;
        try {

            handler=(TcpInputMessageHandler) Class.forName(
                    StringUtil.NVL(
                            getProperty(ConnectorConstants.INPUT_MESSAGE_HANDLER),
                            TcpInputMessageHandler.class.getClass().getName()))
                    .newInstance();
            handler.setListener(this);
            handler.setSocket(socket);
            handler.setConfig(getOwner().getPropertyMap());
            handler.setLogConfig(logConfig);

        } catch (Exception e) {
            LogManager.error(logConfig,e.toString(),e);
            throw e;
        }
        return handler;
    }


    /**
     * close all resource
     */
    public void destroy() {
        exit=true;
        shutdown();
    }
    /**
     * 현재 accept에서 blocking되어 있는 데몬의 blocking을 풀기 위해 한번
     * 소켓연결을 해 준다.
     * 2006. 10. 10.  이종원 작성
     */
    private void shutdown() {
        LogManager.infoLP(logConfig,this+" Listener shutdown start");
        try {
            Socket socket = new Socket("localhost",port);
//            LogManager.infoP(this+" Listener shutdown socket opened..");
            socket.close();
            LogManager.infoP(logConfig,this+" Listener shutdown requested successfully...");
        } catch (Exception e) {
            LogManager.error(e.getMessage(),e);
        }

    }
    public void closeAll() {

        LogManager.infoLP(logConfig,getId()+" Listener를 종료합니다.");
        //마지막에 accept하며 기다리는 녀석을 없앤다.
//        if(shutdownCommander != null){
//            shutdownCommander.shutdown();
//        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e2) {
            LogManager.error(e2.toString());
        }

        closeServerSocket();

        if(workerPool != null){
            synchronized(workerPool){
                int count=0;
                //쓰레드가 현재 수행을 마쳤으면..
                while(count<3 && !workerPool.closed() && workerPool.getNumActive()>0){
                    LogManager.info(logConfig,"★★ " + getId()
                            +" Listener에 아직 "
                            + workerPool.getNumActive()
                            +"개의 수행중인 쓰레드가 있어 기다립니다..최대 "
                            +(3-count)+"초 까지 기다림.");
                    count++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }

                try {
                    if(!workerPool.closed()){
                        LogManager.debug(logConfig,workerPool.getName()+"thread Pool을 close합니다.");
                        WorkerPoolManager.getInstance().close(workerPool.getName());
                        LogManager.debug(logConfig,workerPool.getName()+"thread Pool을 workerPoolManager에서 제거합니다.");
                    }
                    workerPool=null;
                } catch (Throwable e1) {
                    LogManager.error(logConfig,e1.getMessage(),e1);
                }
            }
        }

        if(threadPool != null){
            synchronized(threadPool){
                int count=0;
                //쓰레드가 현재 수행을 마쳤으면..
                while(count<3 && !threadPool.closed() && threadPool.isRunning()){
                    LogManager.info(logConfig,getId()
                            +" Listener에 아직 "
                            + threadPool.getNumActive()
                            +"개의 수행중인 쓰레드가 있어 기다립니다..최대 "
                            +(3-count)+"초 까지 기다림.");
                    count++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }

                try {
                    if(!threadPool.closed()){
                        LogManager.debug(logConfig,threadPool.getName()+"thread Pool을 close합니다.");
                        ThreadPoolManager.getInstance().close(threadPool.getName());
                        LogManager.debug(logConfig,threadPool.getName()+"thread Pool을 ThreadPoolManager에서 제거합니다.");
                    }
                    threadPool=null;
                } catch (Throwable e1) {
                    LogManager.error(logConfig,e1.getMessage(),e1);
                }
            }
        }

        destroyHandlers();

    }

    private void closeServerSocket() {
        //set exit flag true
        exit=true;
        try {
            if (serverSocket != null) {
                LogManager.info(logConfig,getId()+" Listener ServerSocket을 close 시작.");
                serverSocket.close();
                int count=0;
                while(count++<3 && !serverSocket.isClosed()){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {

                    }
                    LogManager.debug(logConfig,count+"th wait for closing ServerSocket ...");
                }

                serverSocket = null;
                LogManager.info(logConfig,getId()+" Listener ServerSocket을 close 종료.OK ");
            }
        } catch (IOException e) {
            LogManager.error(logConfig,e);
        }
    }
    /**
     * 해당 listener에 딸려 있는 thread를 종료 시킨다.
     * 2006. 8. 1. 이종원 작성
     */
    protected void destroyHandlers() {
        if(handlerList==null){
            return;
        }
        if(handlerList.isEmpty()) return;
        LogManager.debug(logConfig,
                getId()+" Listener에 딸려 있는 thread를 종료 시킵니다");
        Iterator i = handlerList.iterator();
        InputMessageHandler handler=null;
        while(i.hasNext()){
            handler=(InputMessageHandler) i.next();
            //removeHandler(handler);
            handler.stop();
        }
        LogManager.debug(logConfig,
                getId()+" Listener에 딸려 있는 thread를 종료 완료");
    }

    /**
     * 이 리스터에 인자로 전달 받은 핸들러를 등록 한다.
     * 이미 등록된 핸들러 일 때 에러를 발생한다.
     * 2004. 8. 1.  이종원 작성
     */
    public void addHandler(InputMessageHandler handler){
        if(handlerList==null){
            handlerList = new ArrayList();
        }
        if(handlerList.contains(handler)){
            throw new SysException("이미 등록되어 있는 Handler입니다"+handler);
        }
        handlerList.add(handler);
    }

    /**
     * 이 리스너에 인자로 전달 받은 핸들러를 제거 한다.
     * 대상이 등록된 핸들러가 아닐 때에는 skip 한다.
     * 2004. 8. 1.  이종원 작성
     */
    public void removeHandler(InputMessageHandler handler){
        if(handlerList==null){
            handlerList = new ArrayList();
        }
        if(handler == null) return;
        handler.stop();
        if(!handlerList.contains(handler)){
        	LogManager.info(logConfig,"등록되어 있지 않는 Handler입니다"+handler);
        }else{
            handlerList.remove(handler);
        }
    }
    public WorkerPool getWorkerPool() {
        return workerPool;
    }
    public void setWorkerPool(WorkerPool workerPool) {
        this.workerPool = workerPool;
    }
}// end