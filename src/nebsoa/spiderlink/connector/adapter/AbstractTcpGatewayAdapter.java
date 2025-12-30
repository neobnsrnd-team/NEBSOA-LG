/*
 * Spider Framework
 *
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 *
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.connector.adapter;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.NoSuchElementException;

import nebsoa.common.exception.ParamException;
import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.StringUtil;
import nebsoa.spiderlink.connector.GatewayConnector;
import nebsoa.spiderlink.connector.constants.ConnectorConstants;
import nebsoa.spiderlink.socketpool.AsyncSocketFactory;
import nebsoa.spiderlink.socketpool.PoolableSocketFactory;
import nebsoa.spiderlink.socketpool.SocketFactory;
import nebsoa.spiderlink.socketpool.SocketPool;
import nebsoa.spiderlink.socketpool.SocketPoolManager;
import nebsoa.spiderlink.socketpool.WorkerSocket;
import nebsoa.util.threadpool.ThreadPool;
import nebsoa.util.threadpool.ThreadPoolManager;
import nebsoa.util.threadpool.WorkerThread;

/*******************************************************************
 * <pre>
 * 1.설명
 * 타기관으로 TCP 전문 전송을 처리하는 class만들 때 상속 받을 클래스
 * <font color='red'>
 * socket pool을 얻어 오는 로직 및 주기적으로 열고 닫는 것을  여기서 구현하였다.
 * 따라서 상속만 받으면 socket pooling은 해결 된다.
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
 * $Log: AbstractTcpGatewayAdapter.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:26  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.5  2009/12/29 07:50:54  jglee
 * refillInteval 1000 보다 작을경우 처리
 *
 * Revision 1.4  2009/11/27 07:28:09  jglee
 * socket_reset_interval 속성값을 sleepTerm 값으로 처리하도록 수정
 *
 * Revision 1.3  2009/11/13 13:44:21  jglee
 * 모니터링 처리 삭제
 *
 * Revision 1.2  2009/11/12 03:56:57  jglee
 * reset시 checkErrorMonitoring 처리-실시간 연결 정보를 업데이트 하기위한 처리
 *
 * Revision 1.1  2008/11/18 11:27:26  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:29  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.3  2008/10/31 11:01:38  ejkim
 * sleepTerm값 수정 & checkStatus에서 !usePooling일 경우 return하도록 수정
 *
 * Revision 1.2  2008/09/09 08:45:10  김성균
 * 리프레쉬 주기를 속성으로 관리하게 수정.
 *
 * Revision 1.1  2008/08/04 08:54:52  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.3  2008/07/09 12:00:24  김영석
 * checkErrorState()에 Socket Trace 적용
 *
 * Revision 1.2  2008/07/09 07:38:33  김영석
 * 무조건 연결이 안된다고 에러로 마킹 할 수는 없다.
 * 현재 사용되는 소켓이 것이 하나라도 있으면 에러가 아닌것으로 처리 되도록 수정
 *
 * Revision 1.1  2008/01/22 05:58:25  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.4  2008/01/15 05:30:26  이종원
 * 에러 일 때 reset 주기를 줄임
 *
 * Revision 1.3  2008/01/15 05:14:24  이종원
 * 해당 시스템에 소켓을 열지 못할 때 에러로 마킹
 *
 * Revision 1.4  2007/10/11 08:22:34  신정섭
 * 특정 사용자 로그 출력 하도록 기능 수정
 *
 * Revision 1.3  2007/06/28 02:07:51  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2007/06/15 04:57:57  안경아
 * *** empty log message ***
 *
 * Revision 1.21  2007/05/11 05:09:48  이종원
 * socket connect timeout기능 추가(default 5초)
 *
 * Revision 1.20  2007/03/23 08:34:26  최수종
 * 접근지시자 기존 소스의(default)로 수정.
 * 필요한 변수만 protected로 변경함.
 *
 * Revision 1.19  2007/03/23 07:00:23  최수종
 * private createSocketPool() -> protected createSocketPool()로 수정
 *
 * Revision 1.18  2007/03/22 09:31:57  최수종
 * 명시적으로 public 선언으로 수정
 *
 * Revision 1.17  2007/02/01 01:54:59  안경아
 * *** empty log message ***
 *
 * Revision 1.16  2007/01/29 07:06:00  이종원
 * *** empty log message ***
 *
 * Revision 1.15  2007/01/08 06:05:33  이종원
 * *** empty log message ***
 *
 * Revision 1.14  2007/01/08 05:36:16  이종원
 * synchronized( socketpool )주석 처리
 *
 * Revision 1.13  2007/01/08 05:28:18  이종원
 * *** empty log message ***
 *
 * Revision 1.12  2007/01/02 08:56:20  이종원
 * *** empty log message ***
 *
 * Revision 1.11  2006/10/19 15:06:42  이종원
 * 전문별 타임아웃값 설정 가능하도록 수정
 *
 * Revision 1.10  2006/10/13 13:57:11  이종원
 * *** empty log message ***
 *
 * Revision 1.9  2006/10/13 09:07:59  이종원
 * Logging MSG_ERROR로 수정
 *
 * Revision 1.8  2006/10/13 09:03:13  이종원
 * Logging MSG_ERROR로 수정
 *
 * Revision 1.7  2006/10/13 05:35:10  이종원
 * *** empty log message ***
 *
 * Revision 1.6  2006/10/13 05:31:30  이종원
 * *** empty log message ***
 *
 * Revision 1.5  2006/10/13 05:16:11  이종원
 * refill로직 수정
 *
 * Revision 1.4  2006/10/13 02:51:32  이종원
 * *** empty log message ***
 *
 * Revision 1.3  2006/10/13 02:51:08  이종원
 * *** empty log message ***
 *
 * Revision 1.2  2006/10/13 02:34:08  이종원
 * *** empty log message ***
 *
 * Revision 1.1  2006/10/03 09:12:44  이종원
 * refactoring
 *
 * Revision 1.26  2006/10/02 20:13:27  이종원
 * method정리
 *
 * Revision 1.25  2006/10/02 02:45:52  이종원
 * *** empty log message ***
 *
 * Revision 1.24  2006/09/28 14:23:23  이종원
 * *** empty log message ***
 *
 * Revision 1.23  2006/09/28 12:52:16  이종원
 * *** empty log message ***
 *
 * Revision 1.22  2006/09/28 12:39:59  이종원
 * *** empty log message ***
 *
 * Revision 1.21  2006/09/27 13:02:55  이종원
 * *** empty log message ***
 *
 * Revision 1.20  2006/09/27 12:35:13  이종원
 * *** empty log message ***
 *
 * Revision 1.19  2006/09/27 12:30:31  이종원
 * *** empty log message ***
 *
 * Revision 1.18  2006/09/27 12:03:33  이종원
 * SOCKET_MAX_COUNT변수 추가
 *
 * Revision 1.17  2006/09/27 12:00:01  이종원
 * *** empty log message ***
 *
 * Revision 1.16  2006/09/27 11:44:08  이종원
 * *** empty log message ***
 *
 * Revision 1.15  2006/09/27 11:14:32  이종원
 * *** empty log message ***
 *
 * Revision 1.14  2006/09/27 11:13:50  이종원
 * *** empty log message ***
 *
 * Revision 1.13  2006/09/27 10:23:19  이종원
 * socket pool 디렉토리변경
 *
 * Revision 1.12  2006/09/27 09:20:25  이종원
 * *** empty log message ***
 *
 * Revision 1.11  2006/09/27 08:25:03  이종원
 * *** empty log message ***
 *
 * Revision 1.10  2006/09/27 07:40:46  이종원
 * update
 *
 * Revision 1.9  2006/09/26 09:08:39  이종원
 * reset logic구현
 *
 * Revision 1.7  2006/09/23 01:42:43  이종원
 * config map으로 변경
 *
 * Revision 1.6  2006/09/19 12:02:02  이종원
 * sleep 시간 1000 보다 작으면 1000 곱하기 함.
 *
 * Revision 1.5  2006/09/19 11:49:04  이종원
 * reset 주석 처리
 *
 * Revision 1.4  2006/09/19 06:10:10  김승희
 * async_socket_mode 얻어오는 위치 수정
 *
 * Revision 1.3  2006/09/19 02:16:23  김승희
 * sync_socket_mode 속성 GW 프로퍼티에서 읽어오도록 수정
 *
 * Revision 1.2  2006/09/18 10:57:23  이종원
 * async socket factory 사용가능하게 기능 추가
 *
 * Revision 1.1  2006/09/16 13:10:55  이종원
 * 어느정도 완성된 초안 작성
 *
 * </pre>
 ******************************************************************/
public abstract class AbstractTcpGatewayAdapter
    extends GatewayConnector implements Runnable {

    /**
     * 생성자.
     */
    public AbstractTcpGatewayAdapter() {

    }

    protected boolean unknownHostError=false;
    protected int skipCount=0;
    protected SocketPool socketPool;

    /**
     * SocketPool을 refresh한다. 아직 SocketPool을 얻지 않았다면, 얻어 내는 로직 수행.
     */
    public void reset(){
        LogManager.fwkDebug(this+" reset--------------------------");
        //if(isStopped()||isError()) return;
        //error인경우 복구 작업을 해야 하니.. stopped인 경우만 skip한다.
        if(isStopped()) {
            LogManager.fwkDebug(this+"-marked  stopped... so skip reset @@@@");
            return;
        }

        if(unknownHostError) {
            LogManager.error(this+"- check ip/port and call init... @@@@");
            return;
        }

        if(socketPool==null){
            socketPool = createSocketPool();
        }

        if( socketPool!=null ){
//            synchronized (socketPool) {

                //모자란 소켓 갯수를 계산합니다.
                int refillCount=1;
                //error상태이거나 풀링을 사용하지 않을 경우는 1개만 접속 시도 해 본다
                if(isError() || ! socketPool.isUsePooling()){
                    //LogManager.debug(this+"Error 상태 이므로... 하나만 복구 해 보려 함...");
                    //error상태이면 1개만 try해 보자...
                    refillCount=1;
                }else{ // 풀링을 사용하는 경우라면 모자란 갯수만큼 복구한다.
                    //부족한 갯수=전체갯수-현재 만들어진 갯수.
                    int shortageCount=getOwner().getThreadCount()
                        -(socketPool.getNumActive()+socketPool.getNumIdle());
                    if(shortageCount==0){ //부족한 것이 없으니...skip
                        return;
                    }
                    //새로 생성할 소켓이  너무 많으면 부담이므로... 조금씩(2개씩)만 refill 하자...
                    if(shortageCount>2){
                        shortageCount=2;
                    }
                    //생성할 갯수=부족한 갯수+idle갯수.
                    refillCount=shortageCount+socketPool.getNumIdle();
//                    if(shortageCount==0 && socketPool.isUsePooling()){
//                        shortageCount=1;
//                    }
                }
                if(refillCount > 0){ //모자란게 있다면.. 채워 넣습니다.

                    try {
                        WorkerSocket[] refillArray=
                            socketPool.borrowObjects(refillCount);
                        //소켓을 열었으니..더이상 ... 에러는 아니지...
                        setError(false);
                        socketPool.refillObjects(refillArray);
                        checkErrorMonitoring(false);
                    } catch (UnknownHostException e) {
                        LogManager.error(logConfig,this+":알수없는 시스템명:"+e.toString(),e);
                        setError(true);
                        unknownHostError=true;
                        LogManager.error(logConfig,this+"connector refresh를 종료 합니다.");
                        checkErrorMonitoring(true);
                    } catch (ConnectException e) {
                        LogManager.error(logConfig,this+":접속이 되지 않습니다.ip/port 설정정보를 확인 하거나,연계 기관에 문의 해 주십시오"+e.toString());
                        //무조건 연결이 안된다고 에러로 마킹 할 수는 없다.
                        //현재 가용한 것이 하나도 없을 경우에만 에러로 마킹하도록 한다.
                        checkErrorState();
                        checkErrorMonitoring(true);
                    } catch (Exception e) {
                        //무조건 연결이 안된다고 에러로 마킹 할 수는 없다.
                        //현재 가용한 것이 하나도 없을 경우에만 에러로 마킹하도록 한다.
                        checkErrorState();
                        checkErrorMonitoring(true);
                        LogManager.error(logConfig,this+":" +e.toString(),e);
                    }
                }
 //           }
        }
    }

    private synchronized void closePool() {
        if(socketPool != null){
            SocketPoolManager.getInstance().close(getId());
            socketPool=null;
        }
    }

    /**
     * 주기적으로 Coneector의 현재 상태를 체크(override하여 사용)
     * @param isErr
     */
    public void checkErrorMonitoring(boolean isErr){

    }

    /**
     * <font color='red'>socket pool 생성.</font>
     * 2006. 9. 16. 이종원 작성
     * @see nebsoa.spiderlink.engine.Lifecycle#init() 재정의
     */
    public synchronized void init(){
        LogManager.fwkDebug(this+" init--------------------------");
        unknownHostError=false;

        closePool();

        if(socketPool==null){
            socketPool = createSocketPool();
        }

        if(socketPool.closed()){
            try{
                SocketPoolManager.getInstance().close(getId());
            }catch(Exception e){
                LogManager.error(logConfig,"Ignore this:"+e.toString());
            }
            socketPool = createSocketPool();
        }
        service();
    }

    // 리프레쉬 주기를 속성으로 관리하게 수정.
    //2008.10.31 kimeunjung 5*60*1000에서 10*1000(10초) 으로 변경
    protected long sleepTerm = 10*1000;
    /**
     * <font color='red'>주기적으로 reconnect하는 로직을 수행 합니다.</font>
     * 2006. 9. 16. 이종원 작성
     * @see java.lang.Runnable#run() 재정의
     */
    public void run(){
        while(!isStopped()){ // && socketPool!= null && !socketPool.closed()){
            try{
                reset();
            }catch(Exception e){
                LogManager.error(logConfig,"Connector Socket RefreshError:"+e.toString(),e);
            }

            try {
            	if(isError()){
            		//30초 간격으로 체크 하던 것을 10초 단위로 하도록 수정
            		Thread.sleep(sleepTerm);
            	}else{
            		Thread.sleep(sleepTerm);
            	}
            } catch (InterruptedException e) {

            }
        }
    }

    /**
     * Socket Pool을 주기적으로 reset하는 thread를 기동.
     * 2006. 9. 16. 이종원 작성
     * @throws Exception
     * @see nebsoa.spiderlink.engine.Lifecycle#service() 재정의
     */
    public void service()  {
        LogManager.fwkDebug(this+" service-----------------");
        ThreadPool pool = ThreadPoolManager.getInstance().getPool(
                "TCP_CONNECTOR_RESET_THREAD",1);
        WorkerThread thread;
        try {
            thread = (WorkerThread) pool.borrowObject();
        } catch (Exception e) {
            LogManager.error(logConfig,"Connector에서 RefreshThread를 얻지 못함:"
                    + e.toString());
           throw new SysException(e);
        }
        thread.execute(this);
    }


    /**
     * <font color='red'>socket close.</font>
     * 2006. 9. 16. 이종원 작성
     * @see nebsoa.spiderlink.engine.Lifecycle#destroy() 재정의
     */
    public void destroy(){
        LogManager.fwkDebug(this+" destroy-----------------");
        stop();
        closePool();
    }

    public void finalize(){
        LogManager.fwkDebug(this+" finalize-----------------");
        if(!isStopped()) destroy();
    }

	/**
	 * 소켓 풀을 리턴한다.
	 * @return SocketPool
	 */
	protected SocketPool createSocketPool() {
        LogManager.fwkDebug(this+" createSocketPool-----------------");

        if(!isInitialized()){
            throw new SysException("FRS91111",
                    "GatewayConnector is not initialized");
        }

        if(isStopped()) {
            LogManager.info("GatewayConnector is stopped.. "
                    +"\n Do not create SocketPool");
            return null;
        }

        if(unknownHostError){
            LogManager.error(logConfig,"Unknown Host error.please check ip/port. ");
            return null;
        }

		String socketKey = getId();
		SocketPool pool = SocketPoolManager.getInstance().getPool(socketKey);
		if(pool==null){
            String ip = super.getIp();
            int port = super.getPort();
            LogManager.debugLP("IP:"+ip+",Port:"+port+"로 SocketPool을 생성합니다.");

            //최대 허용 갯수를 쓰레드 갯수와 같게 한다.
            int maxActive = getOwner().getThreadCount();
//                StringUtil.parseInt(getProperty(
//                        ConnectorConstants.SOCKET_MAX_COUNT),5);
            if(maxActive<=0){
                LogManager.fwkDebug("ThreadCount(MaxActiveNum) is invalid. set '1'");
                maxActive=1;
            }
            int maxWaitWhenBusy =
                StringUtil.parseInt(getProperty(
                        ConnectorConstants.MAX_WAIT_WHEN_BUSY),0);
//            boolean testOnBorrow=
//                StringUtil.getBoolean(getProperty(
//                        ConnectorConstants.TEST_ON_BORROW),true);
            int refillInterval =
                StringUtil.parseInt(getProperty(
                        ConnectorConstants.SOCKET_RESET_INTERVAL),10);
            if(refillInterval < 1000){
            	refillInterval = refillInterval*1000;
            }
            setSleepTerm(refillInterval);

            int timeout=StringUtil.parseInt(
                    getProperty(ConnectorConstants.TIMEOUT),120*1000);

            int connectTimeout=StringUtil.parseInt(
                    getProperty(ConnectorConstants.CONNECT_TIMEOUT),3*1000);

            LogManager.debugP("Timeout :"+timeout);

            String inputMessageHandler="nebsoa.util.socketpool.SocketClosingMonitorHandler";

            LogManager.debugP("Pooling여부 :"+usePooling());

            if(usePooling()){

                String ioProtocol = getProperty(ConnectorConstants.IO_PROTOCOL);
                if(StringUtil.isNull(ioProtocol)){
                    throw new ParamException(
                            "SocketPoolingMode일 때는 [io_protocol]속성을 지정하여야 합니다.");
                }

                String inputMessageHandlerTmp = getProperty(ConnectorConstants.INPUT_MESSAGE_HANDLER);
                if(isAsync() && StringUtil.isNull(inputMessageHandler)){
                    throw new ParamException(
                            "ASYNC Mode일 때는 [input_message_handler]속성을 지정하여야 합니다.");
                }

                if(!StringUtil.isNull(inputMessageHandlerTmp)){
                    inputMessageHandler=inputMessageHandlerTmp;
                }

                LogManager.debugP("InputMessageHandler:"+inputMessageHandler);

            }

            SocketFactory factory = null;

            if(!usePooling()){
                LogManager.fwkDebug("풀링을 사용하지 않는 SocketFactory를 생성합니다..[" + ip +":" + port +"]");
                factory = new SocketFactory(ip, port, timeout);
            }else if(!isAsync()){
        	    LogManager.fwkDebug("풀링을 사용하는 SYNC SocketFactory를 생성합니다..[" + ip +":" + port +"]");
                factory = new PoolableSocketFactory(ip, port, timeout);
            }else{
        	    LogManager.fwkDebug("ASYNC SocketFactory를 생성합니다..[" + ip +":" + port +"]");
                factory = new AsyncSocketFactory(ip, port, timeout);
            }
            //socket connect timeout
            factory.setConnectTimeout(connectTimeout);

	        pool=SocketPoolManager.getInstance().makeSocketPool(
	    		  socketKey, factory, maxActive, maxWaitWhenBusy,usePooling());
            factory.setConfig(getOwner().getPropertyMap());
		}
		return pool;
	}

    public Object forName(String className){
        try {
            return  Class.forName(className).newInstance();
        } catch (InstantiationException e1) {
           throw new SysException("[CANN'T MAKE INSTANCE 생성자 체크 :"+className+"]");
        } catch (IllegalAccessException e1) {
           throw new SysException("[CANN'T MAKE INSTANCE 생성자 PUBLIC 인지 체크 :"+className+"]");
        }catch(ClassNotFoundException e){
           throw new SysException("[CLASS_NOT_FOUND:"+e.getMessage()+"]");
        }
    }

    public synchronized SocketPool getSocketPool() {
        //LogManager.debug(this+" getSocketPool-----------------");
        if(socketPool == null) throw
            new NullPointerException("socket pool is null");
        return socketPool;
    }

    public void setSocketPool(SocketPool socketPool) {
        //LogManager.debug(this+" setSocketPool-----------------");
        this.socketPool = socketPool;
    }

    protected WorkerSocket getWorkerSocket() throws Exception {
        //LogManager.debug(this+" getWorkerSocket-----------------");
        WorkerSocket socket=null;
        if(isStopped()){
            return null;
        }
        try {
            socket = (WorkerSocket)getSocketPool().borrowObject();
            setError(false);
        }catch(NoSuchElementException e){
            throw e;
        }catch(java.lang.IllegalStateException e){
        	try{
        		SocketPool pool = SocketPoolManager.getInstance().getPool(getId());
        		if(pool != null ){
        			if(!pool.closed()) pool.close();
        			SocketPoolManager.getInstance().close(getId());
        		}
        		createSocketPool();
        	}catch(Exception ex){

        	}
        }catch(java.net.SocketException e){
        	checkErrorState();
        	throw e;
        } catch (Exception e) {
            LogManager.error(logConfig,"SocketPool 부터 Socket을 얻지 못함;"+e.toString());
            checkErrorState();
            throw e;
            //return;
        }
        return socket;
    }

    protected void checkErrorState() {
    	//2008.10.31 kimeunjung !usePooling일 경우 return처리 하도록 수정.
    	if(socketPool==null|| socketPool.closed() || !socketPool.isUsePooling()) return;
    	int active=socketPool.getNumActive();
    	int idle = socketPool.getNumIdle();
        //무조건 연결이 안된다고 에러로 마킹 할 수는 없다.
        //현재 가용한 것이 하나도 없을 경우에만 에러로 마킹하도록 한다.
        //pooling을 사용하지 않는 경우도 일단 에러로 마킹하고, 3초 뒤에 재 연결해 보도록 한다.
        Exception e = new Exception(" >>> Socket Trace용 입니다. \n >>> 현재 Socket 정보 IP: " + ip + ", PORT : " + port+",Active:"+active+ ", idle : " + idle);
    	LogManager.error(e.toString(), e);
        if(active+idle==0){
        	setError(true);
        }
	}

	protected WorkerSocket getWorkerSocketWithTimeout(long timeout) throws Exception {
        //LogManager.debug(this+" getWorkerSocket-----------------");
        WorkerSocket socket=null;
        if(isStopped()){
            return null;
        }
        SocketPool pool=null;
        try {
            //전문마다 타임아웃을 달리 하고 싶은 경우의 처리.
            pool = getSocketPool();
            if(!pool.isUsePooling()){ //풀링이 아닌경우만 처리해 준다.
                pool.getFactory().setTempTimeout(timeout);
            }
            socket = (WorkerSocket)pool.borrowObject();
            pool.getFactory().clearTempTimeout();
            setError(false);
        }catch(NoSuchElementException e){
            LogManager.error(logConfig,e.getMessage()
                    +"\n >>> (해당 시스템으로의 요청이 많거나,Gateway 설정 정보에 \n >>> Thread갯수가 값이 너무 적을 수 있습니다-현재 설정된 쓰레드 갯수 :"
                    +getOwner().getThreadCount()+")", e);
            throw e;
        } catch (Exception e) {
            LogManager.error(logConfig,"SocketPool 부터 Socket을 얻지 못함;"+e.toString());
            //무조건 연결이 안된다고 에러로 마킹 할 수는 없다.
            //현재 가용한 것이 하나도 없을 경우에만 에러로 마킹하도록 한다.
            checkErrorState();
            throw e;
            //return;
        }finally{
            if(pool != null){
                try{
                    pool.getFactory().clearTempTimeout();
                }catch(Exception e){
                    LogManager.error(e.toString(),e);
                }
            }
        }
        return socket;
    }

    public void setSleepTerm(long sleepTerm) {
        if (sleepTerm < 1000) {
            sleepTerm = sleepTerm * 1000;
        }
        this.sleepTerm = sleepTerm;
    }
}// end
