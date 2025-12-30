/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
package nebsoa.spiderlink.connector.listener.protocol;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.ConfigMap;
import nebsoa.common.util.DataMap;
import nebsoa.spiderlink.connector.GatewayConnector;
import nebsoa.spiderlink.connector.constants.ConnectorConstants;
import nebsoa.spiderlink.connector.io.reader.MessageReader;
import nebsoa.spiderlink.connector.io.reader.TcpMessageReader;
import nebsoa.spiderlink.connector.io.writer.MessageWriter;
import nebsoa.spiderlink.connector.io.writer.TcpMessageWriter;
import nebsoa.spiderlink.connector.listener.DefaultTcpGatewayListener;
import nebsoa.spiderlink.connector.listener.worker.InputMessageWorker;
import nebsoa.spiderlink.connector.util.HeaderParser;
import nebsoa.spiderlink.context.InputMessageTrxMapper;
import nebsoa.spiderlink.context.MessageContext;
import nebsoa.spiderlink.context.MessageEngineContext;
import nebsoa.spiderlink.context.Trx;
import nebsoa.spiderlink.engine.MessageEngine;
import nebsoa.spiderlink.engine.adapter.tcp.BaseOrgInHandler;
import nebsoa.spiderlink.engine.message.MessageConstants;
import nebsoa.spiderlink.exception.MessageException;
import nebsoa.util.workerpool.WorkerFactory;

import org.apache.commons.collections.map.MultiKeyMap;

/*******************************************************************
 * <pre>
 * 1.설명 
 * tcp protocol을 통하여 동시에 여러 전문을 수신 처리를 위한 multi 처리  클래스를
 * 만들 때 상속 받을 클래스 . 소켓이 하나 열릴 때 마다 생성되는 개체이며
 * thread pool을 사용하여 동작한다.
 * 
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
 * $Log: TcpInputMessageHandler.java,v $
 * Revision 1.1  2018/01/15 03:39:51  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:54  cvs
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
 * Revision 1.3  2008/10/10 00:30:05  김성균
 * 핸들러 중복삭제되는 문제 수정
 *
 * Revision 1.2  2008/09/08 03:04:38  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/08/04 08:54:52  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.2  2008/03/04 07:44:14  김승희
 * no ack 관련 수정
 *
 * Revision 1.1  2008/01/22 05:58:24  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:39:14  안경아
 * *** empty log message ***
 *
 * Revision 1.9  2007/03/07 05:03:53  김승희
 * Async 리스너의 핸들러 수행 시 에러 전문 전송 기능 추가
 *
 * Revision 1.8  2007/03/02 11:58:46  김승희
 * handlerUID 초기화
 *
 * Revision 1.7  2007/01/29 07:43:24  이종원
 * *** empty log message ***
 *
 * Revision 1.6  2006/10/13 12:14:20  이종원
 * Logging MSG_ERROR로 수정
 *
 * Revision 1.5  2006/10/11 04:44:43  이종원
 * exe time 체크 로직 추가
 *
 * Revision 1.4  2006/10/10 09:03:29  이종원
 * *** empty log message ***
 *
 * Revision 1.3  2006/10/10 07:26:06  이종원
 * *** empty log message ***
 *
 * Revision 1.2  2006/10/10 05:52:52  이종원
 * exit 변수 추가
 *
 * Revision 1.1  2006/10/03 09:12:44  이종원
 * refactoring
 *
 * Revision 1.19  2006/10/03 07:03:24  이종원
 * 1차 완료
 *
 * Revision 1.18  2006/10/03 06:47:16  이종원
 * messageContext 생성 로직 추가
 *
 * Revision 1.17  2006/10/03 06:36:57  이종원
 * trxId로 trxType찾는 로직 추가
 *
 * Revision 1.16  2006/10/03 06:24:04  이종원
 * *** empty log message ***
 *
 * Revision 1.15  2006/10/03 06:07:45  이종원
 * noack 응답 처리 기능 변경 GatewayManager.sendResponseMessage
 *
 * Revision 1.14  2006/10/03 05:24:24  이종원
 * 변수 정리
 *
 * Revision 1.13  2006/10/03 00:25:51  이종원
 * *** empty log message ***
 *
 * Revision 1.12  2006/10/03 00:08:24  이종원
 * *** empty log message ***
 *
 * Revision 1.11  2006/10/02 23:50:10  이종원
 * *** empty log message ***
 *
 * Revision 1.10  2006/10/02 23:32:46  이종원
 * usePooling이 아닌 경우 1회 서비스 후 소켓을 닫는다.
 *
 * Revision 1.9  2006/10/02 06:39:17  이종원
 * *** empty log message ***
 *
 * Revision 1.8  2006/09/29 09:04:54  이종원
 * *** empty log message ***
 *
 * Revision 1.7  2006/08/28 00:54:39  김승희
 * ap 패키지, 클래스명 변경에 따른 수정
 *
 * Revision 1.6  2006/08/22 10:11:17  안경아
 * *** empty log message ***
 *
 * Revision 1.5  2006/08/18 08:28:40  안경아
 * *** empty log message ***
 *
 * Revision 1.4  2006/08/01 14:00:16  이종원
 * code정리
 *
 * Revision 1.3  2006/08/01 10:25:37  이종원
 * *** empty log message ***
 *
 * Revision 1.2  2006/08/01 10:01:23  이종원
 * *** empty log message ***
 *
 * Revision 1.1  2006/07/31 11:17:12  이종원
 * 최초작성
 *
 * </pre>
 ******************************************************************/
public abstract class TcpInputMessageHandler extends  InputMessageHandler {
    protected DefaultTcpGatewayListener listener;
    protected TcpMessageReader messageReader;
    protected TcpMessageWriter messageWriter;
    protected HeaderParser headerParser;
    protected Socket socket;
    protected String syncType;
    protected String logConfig;
    protected boolean usePooling;
    protected WorkerFactory factory;
    protected ConfigMap config;

    private long workStartTime;
    private long createTime;
    private String createdIndex;
    
    static int socketCreateCount=0;
    static int currentOpenSocketCount;
    static Object dummy = new Object();
    
    public TcpInputMessageHandler(){
        workStartTime = System.currentTimeMillis();
        createTime=workStartTime;
        
        synchronized(dummy){
            createdIndex=++socketCreateCount +"번째 Listen Socket";
            currentOpenSocketCount++;
        }
    }


    
    public void run(){
        init();
        while(!this.exit && !listener.exit && !listener.isStopped()){
            // try
            handleInputMessage();
            //pooling이 아닌 경우 한번만 서비스 하고 socket을 닫는다.
            if(!listener.usePooling()) stop();
            //TODO CATCH.... ERROR..
        }
        //여기서 내부적으로 listener에서 un-regist(handler)하는 로직 수행.
        destroy();        
    }

    public void init() {
        //throw new SysException("not implemented.. yet");
        
    }

    public void handleInputMessage()  {
        byte[] recvData = null;
        
        try {
            recvData = messageReader.readMessage(); 
            headerParser.setData(recvData);
            String messageId = headerParser.getMsgId();
            //String messageGroupId = headerParser.getMsgGroupId();
            //요청기관 전문 고유번호 : spiderlink내부에서 부여 한 uid가 아니다.
            String messageUid = headerParser.getMessageUID();
            
            String syncType = 
                config.getParameter(ConnectorConstants.IO_PROTOCOL);
            MultiKeyMap mapper = 
                MessageEngineContext.getContext().getMessageTrxMappingMap();
            if(mapper==null){
                throw new SysException("전문별 거래 매핑 정보가 없습니다."
                        +"\nMessageEngineContext::getMessageTrxMappingMap is null");
            }
            InputMessageTrxMapper trxMapper = 
                (InputMessageTrxMapper) mapper.get(getListener().getId(),messageId);
            if(trxMapper==null){
                throw new SysException(getListener().getId()
                        +" Listener에 요청 받은 전문("+messageId
                        +")을 처리 할 핸들러에 대한 정보가 없습니다.");
            }
            String orgId = trxMapper.getOrgId();
            String trxId = trxMapper.getTrxId();
            
            //TODO trxId로 trxType찾는 로직 구현 : 검증 필요...
            Trx trx = MessageEngineContext.getContext().getTrx(trxId);
            if (trx == null) {
                throw new MessageException(trxId + "에 해당하는 거래정보를 찾을 수 없습니다.");
            }
            String trxName = trx.getTrxName();
            boolean isTrxStop = trx.isTrxStop();
            if (isTrxStop) {
                throw new MessageException("FRM00010", trxName + " 거래를 현재 사용할 수 없습니다.");
            }
            String trxType = trx.getTrxType();//ONLINE, BATCH. ETC..
                
            String runningMode = getListener().getRunningMode().getType();
            
            //TODO find handler : 검증필요..
            BaseOrgInHandler handler = 
                MessageEngine.createInMessageHander(
                                    orgId,trxType,runningMode);
            //business logic을 처리할 biz app id
            handler.setBizAppId(trxMapper.getBizAppId());
            
            //TODO 구현 검증  필요..
            MessageContext ctx = 
                MessageEngine.getMessageContext(trxId, orgId, "I");
            //put recv data to dataMap
            DataMap dataMap = new DataMap();
            dataMap.put(MessageConstants.MESSAGE_RECEIVED,recvData);
            
            //execute with worker for support sync, async,no_ack 
            InputMessageWorker worker  = 
                (InputMessageWorker) listener.getWorkerPool().borrowWoker();
            worker.init(this,syncType,handler,ctx,dataMap);
//            if(SyncType.isNoAck(syncType)){
//                worker.setResponseConnector(getNoAckResponseConnector());
//            }
            worker.execute();
        }catch(SocketTimeoutException e){
            LogManager.debug(logConfig, "SocketTimeout-요청이 없어서 종료 합니다.");
            stop();
        }catch(EOFException e){
            LogManager.info(logConfig, "통신종료 패킷 수신-서비스 종료함");
            stop();
        } catch (Throwable e) {
            LogManager.error(logConfig, e.toString(), e);
            stop();            
        } 
    }
    
    protected BaseOrgInHandler createOrgInHandler() {
        throw new SysException("Not implemented..");
    }
    
    protected GatewayConnector getNoAckResponseConnector() {
        throw new SysException("Not implemented..");
    }

    /**
     * destroy() 수행여부 
     */
    private boolean isDestroy = false;
    
    public void destroy(){
        // 핸들러 중복삭제를 방지하기 위해서 추가함.
        if (isDestroy) return;
        close();
        listener.removeHandler(this); 
        isDestroy = true;
    }
    
    public void close() {
    	//핸들러 UID를 null로 초기화한다.
    	this.handlerUID = null;
    	
        if(messageReader != null){
            messageReader.close();
            messageReader=null;
        }
        if(messageWriter != null){
            messageWriter.close();
            messageWriter = null;
        }
        if(socket != null){
            try {
                socket.close(); 
                long endTime=System.currentTimeMillis();
                double workTime=(endTime-createTime)/1000.0;
                synchronized(dummy){
                    currentOpenSocketCount--;
                }
                LogManager.info("Socket close...["+socket+"]"+createdIndex
                        +",사용시간:"+workTime+"초, 현재 open된 Listen SocketCount:"+currentOpenSocketCount);
            } catch (IOException e) {
                LogManager.error(logConfig,e.getMessage(),e);
            }
            socket = null;
        }
        long term = System.currentTimeMillis()-workStartTime;
        LogManager.debug(logConfig,getClass().getName()
                +" Handler WorkTime:"+(term/1000.0)+"초");
        
    }



    public void finalize(){
        destroy();
    }
    /**
     * 응답을 전송한다.
     * 비동기로,멀리쓰레드로  처리 되더라도 문제가 없도록 synchronized로 구현 하였다.
     * 2006. 10. 3.  이종원 작성
     * @param data
     * @throws IOException
     */
    public void sendResponse(byte[] data) throws IOException{
        if(messageWriter==null){
            stop();
            throw new SysException(this+"  Response Writer is null");
        }
        synchronized (messageWriter) {
            messageWriter.writeMessage(data);
        }
    }
    
    /**
     * 응답을 전송한다.
     * 비동기로,멀리쓰레드로  처리 되더라도 문제가 없도록 synchronized로 구현 하였다.
     * 2006. 10. 3.  이종원 작성
     * @param data
     * @throws Throwable 
     */
    public void sendResponse(MessageContext messageContext) throws Throwable{
        
    	if(messageWriter==null){
            stop();
            throw new SysException(this+"  Response Writer is null");
        }
    	byte[] data = (byte[])messageContext.getResult();
    	    	
        synchronized (messageWriter) {
            messageWriter.writeMessage(data);
        }
    }
    
    /**
     * 핸들러 수행 시 발생한 에러에 대한 에러 전문을 전송한다. (Async 리스너의 경우)
     *
     * 각 기관에 맞는 에러 전문을 전송하기 위해 이 클래스를 상속받은 클래스에서 이 메소드를 적절히 overriding해야 한다.
     * 에러 메시지는 이 클래스를 상속받은 클래스에서 직접 생성하여 넣어줄 수도 있고 
     * 인자로 넘어오는 messageContext에 미리 errorResult를 셋팅하여 그걸 write할 수도 있다.
     * 이 메소드는 어느 경우를 사용하든 가능하게 인자로 MessageContext를 받도록 설계되었다.
     *
     * @param messageContext
     * @param dataMap 최초에 리스너가 수신받은 전문 byte[]을 갖고 있는 DataMap
     * @param th 발생한 exception
     * @throws IOException 에러전문 전송 시 발생하는 IOException
     */
    public void handleError(MessageContext messageContext, DataMap dataMap, Throwable th) throws IOException{
    	
    	//디폴트로 아래와 같이 구현하였으나 오버라이딩하여 사용하는 것을 권장한다.
    	sendResponse((byte[])messageContext.getErrorResult());
    	
    }
    
    public WorkerFactory getFactory() {
        return factory;
    }

    public void setFactory(WorkerFactory factory) {
        this.factory = factory;
    }

    public HeaderParser getHeaderParser() {
        return headerParser;
    }

    public void setHeaderParser(HeaderParser headerParser) {
        this.headerParser = headerParser;
    }

    public DefaultTcpGatewayListener getListener() {
        return listener;
    }

    public void setListener(DefaultTcpGatewayListener listener) {
        this.listener = listener;
        if(listener != null)
        this.logConfig = listener.getLogConfig();
    }

    public String getLogConfig() {
        return logConfig;
    }

    public void setLogConfig(String logConfig) {
        this.logConfig = logConfig;
    }

    public MessageReader getMessageReader() {
        return messageReader;
    }

    public void setMessageReader(TcpMessageReader messageReader) {
        this.messageReader = messageReader;
    }

    public MessageWriter getMessageWriter() {
        return messageWriter;
    }

    public void setMessageWriter(TcpMessageWriter messageWriter) {
        this.messageWriter = messageWriter;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) throws IOException {
        this.socket = socket;
        if(socket != null){
            LogManager.debugP(socket+"소켓용 createHeaderParser");
            this.headerParser = createHeaderParser();
            LogManager.debugP(socket+"소켓용 createMessageWriter");
            this.messageWriter = createMessageWriter();
            LogManager.debugP(socket+"소켓용 createMessageReader");
            this.messageReader = createMessageReader();
        }else{
            throw new SysException("socket is null");            
        }
    }

    public String getSyncType() {
        return syncType;
    }

    public void setSyncType(String syncType) {
        this.syncType = syncType;
    }

    public boolean isUsePooling() {
        return usePooling;
    }

    public void setUsePooling(boolean usePooling) {
        this.usePooling = usePooling;
    }

    public void setConfig(ConfigMap propertyMap) {
        this.config = propertyMap;
    }
    
    ////////METHOD LIST TO IMPLEMENT IN SUB CLASS/////////

    public abstract TcpMessageWriter createMessageWriter()
        throws IOException ;



    public abstract TcpMessageReader createMessageReader() 
        throws IOException ;



    public abstract  HeaderParser createHeaderParser();
    

}
