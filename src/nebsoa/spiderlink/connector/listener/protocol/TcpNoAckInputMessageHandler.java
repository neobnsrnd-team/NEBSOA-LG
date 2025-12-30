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

import nebsoa.common.Context;
import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.DataMap;
import nebsoa.spiderlink.connector.GatewayConnector;
import nebsoa.spiderlink.connector.constants.ConnectorConstants;
import nebsoa.spiderlink.connector.constants.IOType;
import nebsoa.spiderlink.connector.io.writer.TcpMessageWriter;
import nebsoa.spiderlink.connector.listener.util.ListenerConnectorMapper;
import nebsoa.spiderlink.connector.listener.worker.InputMessageWorker;
import nebsoa.spiderlink.context.InputMessageTrxMapper;
import nebsoa.spiderlink.context.MessageContext;
import nebsoa.spiderlink.context.MessageEngineContext;
import nebsoa.spiderlink.context.Trx;
import nebsoa.spiderlink.engine.GatewayManager;
import nebsoa.spiderlink.engine.MessageEngine;
import nebsoa.spiderlink.engine.adapter.tcp.BaseOrgInHandler;
import nebsoa.spiderlink.engine.message.ByteMessageInstance;
import nebsoa.spiderlink.engine.message.MessageConstants;
import nebsoa.spiderlink.exception.MessageException;
import nebsoa.spiderlink.exception.MessageSysException;

import org.apache.commons.collections.map.MultiKeyMap;

/*******************************************************************
 * <pre>
 * 1.설명 
 *  No Ack 리스너의 InputMessageHandler
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
 * $Log: TcpNoAckInputMessageHandler.java,v $
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
 * Revision 1.1  2008/08/04 08:54:52  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.2  2008/03/07 06:17:48  김승희
 * createListenerConnectorMapper : 추상 메소드로 수정
 *
 * Revision 1.1  2008/03/04 07:42:44  김승희
 * 최초 등록
 *
 *
 * </pre>
 ******************************************************************/
public abstract class TcpNoAckInputMessageHandler extends  TcpInputMessageHandler {

    private long workStartTime;
    private long createTime;
    private String createdIndex;
    
    //TODO 이거 static 이어도 되는지... 
    static int socketCreateCount=0;
    static int currentOpenSocketCount;
    static Object dummy = new Object();
    
    public TcpNoAckInputMessageHandler(){
        workStartTime = System.currentTimeMillis();
        createTime=workStartTime;
        
        synchronized(dummy){
            createdIndex=++socketCreateCount +"번째 Listen Socket";
            currentOpenSocketCount++;
        }
    }
    
    protected ListenerConnectorMapper  listenerConnectorMapper;
    
    public void init() {
    	//createListenerConnectorMapper(config.getParameter(ConnectorConstants.LISTENER_CONNECTOR_MAPPER));
    	listenerConnectorMapper = createListenerConnectorMapper();
    }
    
    /*
    protected void createListenerConnectorMapper(String ListenerConnectorMapperClassName){
    	try {
			listenerConnectorMapper=(ListenerConnectorMapper) Class.forName(
					ListenerConnectorMapperClassName).newInstance();
		} catch (Throwable e) {
			LogManager.error("ListenerConnectorMapper 생성 실패"+ e);
		}
    }*/
    
    /**
     * ListenerConnectorMapper를 생성한다.
     * @return ListenerConnectorMapper
     */
    protected abstract ListenerConnectorMapper createListenerConnectorMapper();
    
    
    public void handleInputMessage()  {
        byte[] recvData = null;
        MessageContext ctx =  null;
        try {
            recvData = messageReader.readMessage(); 
            headerParser.setData(recvData);
            String messageId = headerParser.getMsgId();
            //String messageGroupId = headerParser.getMsgGroupId();
            //요청기관 전문 고유번호 : spiderlink내부에서 부여 한 uid가 아니다.
            String messageUid = headerParser.getMessageUID();
            
            String syncType = config.getParameter(ConnectorConstants.IO_PROTOCOL);
            MultiKeyMap mapper = 
                MessageEngineContext.getContext().getMessageTrxMappingMap();
            if(mapper==null){
                throw new SysException("전문별 거래 매핑 정보가 없습니다."
                        +"\nMessageEngineContext::getMessageTrxMappingMap is null");
            }
            InputMessageTrxMapper trxMapper = 
                (InputMessageTrxMapper) mapper.get(getListener().getGwId(),messageId);
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
            BaseOrgInHandler handler = MessageEngine.createInMessageHander(orgId, trx.getTrxType(), getListener().getRunningMode().getType());
            
            //business logic을 처리할 biz app id
            handler.setBizAppId(trxMapper.getBizAppId());
            
            ctx = MessageEngine.getMessageContext(trxId, orgId, IOType.REQUEST_RECEIVER_TYPE);
            
            if(ctx.getTrxMessage()==null){
            	throw new MessageException(trxId + "," + orgId +", " + IOType.REQUEST_RECEIVER_TYPE +"에 해당하는 거래 전문 정보를 찾을 수 없습니다.");
            }
            
            //MessageContext에 추가적인 정보를 셋팅한다.
            setListenerInfo(ctx, recvData);
            
            //put recv data to dataMap
            DataMap dataMap = createDataMap(orgId, trxId, messageId);
            dataMap.put(MessageConstants.MESSAGE_RECEIVED, recvData);
                        
            //execute with worker for support sync, async,no_ack 
            InputMessageWorker worker = (InputMessageWorker) listener.getWorkerPool().borrowWoker();
            worker.init(this,syncType,handler,ctx,dataMap);
            worker.execute();
            
        }catch(SocketTimeoutException e){
            LogManager.debug(logConfig, "SocketTimeout-요청이 없어서 종료 합니다.");
            stop();
        }catch(EOFException e){
            LogManager.info(logConfig, "통신종료 패킷 수신-서비스 종료함");
            stop();
        }catch(IOException e){
            LogManager.info(logConfig, "송수신중 IOException 발생-서비스 종료");
            stop();
        }catch (Throwable e) {
        	//그 외 Exception 인 경우는 쓰레드를 종료하지 않는다.   
        	LogManager.error(logConfig, e.toString(), e);
        	if(ctx==null){
        		ctx = new MessageContext();
        		setListenerInfo(ctx, recvData);
        	}
        	handlerErrorMessage(ctx, e);
            
        } 
    }

	/**
	 * MessageContext에 추가적인 정보를 담는다.
	 * @param ctx
	 * @param recvData
	 */
	private void setListenerInfo(MessageContext ctx, byte[] recvData) {
		//리스너-어댑터 매핑 정보 식별자를 MessageContext에 담는다.
		ctx.setListenerConnectorMappingIndentifier(listenerConnectorMapper.getIndentifier(recvData));
		
		//리스너의 GW ID와 System ID를 담는다.
		ctx.setGatewayId(listener.getGwId());
		ctx.setSystemId(listener.getSystemId());
		
		//MessageContext에 수신받은 전문을 담는다.(차후 에러 발생 시 사용하기 위해)
		ctx.setRequestMessage(new ByteMessageInstance(recvData));
	}
    
    /**
     * 오류 응답 전문 처리를 한다.
     * 
     * @param ctx MessageContext
     * @param e 발생한 exception
     */
    protected abstract void handlerErrorMessage(MessageContext ctx, Throwable e) ;

    
	protected BaseOrgInHandler createOrgInHandler() {
        throw new SysException("Not implemented..");
    }
    
    protected GatewayConnector getNoAckResponseConnector() {
        throw new SysException("Not implemented..");
    }

    /* 
     * NoAck Listener에 적용되지 않는 메소드입니다.
     * (non-Javadoc)
     * @see spider.spiderlink.connector.listener.protocol.TcpInputMessageHandler#sendResponse(byte[])
     */
    public void sendResponse(byte[] data) throws IOException{
    	
    	throw new MessageSysException("No Ack Listener에는 사용할 수 없는 메소드입니다.");
    }
    
    /* 
     * 응답 커넥터를 이용하여 응답 전문을 전송한다.
     * (non-Javadoc)
     * @see spider.spiderlink.connector.listener.protocol.TcpInputMessageHandler#sendResponse(spider.spiderlink.context.MessageContext)
     */
    public void sendResponse(MessageContext ctx) throws Exception{
        
    	GatewayManager.getInstance().sendResponseMessage(ctx);
    }
    
    /* 
     * 수동전문의 unmarshall, 로직 처리, unmarshall 수행 시 에러가 발생 했을 때 쓰레드에 의해 호출되는 메소드이다.
     * (non-Javadoc)
     * @see spider.spiderlink.connector.listener.protocol.TcpInputMessageHandler#handleError(spider.spiderlink.context.MessageContext, spider.common.util.DataMap, java.lang.Throwable)
     */
    public void handleError(MessageContext messageContext, DataMap dataMap, Throwable th) throws IOException{
    	
    	handlerErrorMessage(messageContext, th);
    	
    }

    /* 
     * No Ack 이므로 writer 객체를 생성하지 않는다.
     * (non-Javadoc)
     * @see spider.spiderlink.connector.listener.protocol.TcpInputMessageHandler#setSocket(java.net.Socket)
     */
    public void setSocket(Socket socket) throws IOException {
        this.socket = socket;
        if(socket != null){
            LogManager.debugP(socket+"소켓용 createHeaderParser");
            this.headerParser = createHeaderParser();
            LogManager.debugP(socket+"소켓용 createMessageReader");
            this.messageReader = createMessageReader();
        }else{
            throw new SysException("socket is null");            
        }
    }

    /* 
     * No Ack 이므로 writer 객체를 생성하지 않는다.
     * (non-Javadoc)
     * @see spider.spiderlink.connector.listener.protocol.TcpInputMessageHandler#createMessageWriter()
     */
    public TcpMessageWriter createMessageWriter() throws IOException{
    	return null;
    }
    
    /**
     * DataMap과 Context를 생성하여 DataMap에 Context를 할당한다.
     * 인자로 받은 기관ID, 거래ID, 전문ID등을 적절히 사용하도록 하위 클래스에서 오버라이딩할 수 있다.
     *  
     * @param orgId 기관ID
     * @param trxId 거래ID
     * @param messageId 전문ID
     * @return
     */
    protected DataMap createDataMap(String orgId, String trxId, String messageId){
    	DataMap dataMap = new DataMap();
        Context ctx = new Context();
        dataMap.setContext(ctx);
        
        return dataMap;
    }
    
}