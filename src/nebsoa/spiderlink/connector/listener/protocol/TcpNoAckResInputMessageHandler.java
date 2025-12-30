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
import java.net.SocketTimeoutException;

import nebsoa.common.Context;
import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.DataMap;
import nebsoa.spiderlink.connector.GatewayConnector;
import nebsoa.spiderlink.connector.constants.ConnectorConstants;
import nebsoa.spiderlink.connector.constants.IOType;
import nebsoa.spiderlink.context.InputMessageTrxMapper;
import nebsoa.spiderlink.context.MessageContext;
import nebsoa.spiderlink.context.MessageEngineContext;
import nebsoa.spiderlink.context.Trx;
import nebsoa.spiderlink.engine.MessageEngine;
import nebsoa.spiderlink.engine.adapter.async.AsyncMultiResponseMessageWorker;
import nebsoa.spiderlink.engine.adapter.tcp.BaseOrgHandler;
import nebsoa.spiderlink.engine.adapter.tcp.BaseOrgInHandler;
import nebsoa.spiderlink.engine.message.MessageConstants;
import nebsoa.spiderlink.exception.MessageException;
import nebsoa.spiderlink.queue.ProcessingMessagePool;
import nebsoa.util.workerpool.WorkerFactory;

import org.apache.commons.collections.map.MultiKeyMap;

/*******************************************************************
 * <pre>
 * 1.설명 
 * NO-ACK로 요청 보낸 전문에 대하여 응답을 수신한 경우 처리하는 핸들러이다.
 * 상속 받는 클래스에서는 전문을 파싱하여, UID만 추출하여 응답을 기다리고 있는 
 * MessageContext에  setResult(responseMessagebyte)를  하는 로직을 구현한다. 
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
 * $Log: TcpNoAckResInputMessageHandler.java,v $
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
 * Revision 1.2  2008/09/08 03:04:38  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/08/04 08:54:52  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:24  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.2  2008/01/14 09:18:06  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2007/11/26 08:39:14  안경아
 * *** empty log message ***
 *
 * Revision 1.7  2007/07/15 13:46:22  김승희
 * 주석 수정
 *
 * Revision 1.6  2007/07/15 12:43:10  김승희
 * 수신 받은 데이터 debug 레벨로 로깅
 *
 * Revision 1.5  2007/07/15 12:40:33  이종원
 * Integer.parseInt(UID)로직 수정
 *
 * Revision 1.4  2007/03/09 05:58:36  김성균
 * MessagePool에서 UID값 못 찾는 오류 수정
 *
 * Revision 1.3  2007/03/07 06:40:40  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2006/10/11 01:44:39  이종원
 * timeout log MSG_ERROR로 수정
 *
 * Revision 1.1  2006/10/03 09:12:44  이종원
 * refactoring
 *
 * Revision 1.3  2006/10/03 05:24:24  이종원
 * 변수 정리
 *
 * Revision 1.2  2006/10/03 00:08:24  이종원
 * *** empty log message ***
 *
 * Revision 1.1  2006/10/02 23:42:36  이종원
 * 최초작성
 *
 * </pre>
 ******************************************************************/
public abstract class TcpNoAckResInputMessageHandler 
        extends  TcpInputMessageHandler {
    
    public TcpNoAckResInputMessageHandler(){
              
    }
    
    public void handleInputMessage()  {
    	
    	LogManager.debug("### TcpNoAckResInputMessageHandler.handleInputMessage() called..");
    	
        byte[] recvData = null;
        MessageContext ctx =  null;
        
        try {
            recvData = messageReader.readMessage(); 
            headerParser.setData(recvData);
            String messageId = headerParser.getMsgId();
            
            MultiKeyMap mapper = 
                MessageEngineContext.getContext().getMessageTrxMappingMap();
            InputMessageTrxMapper trxMapper = 
                (InputMessageTrxMapper) mapper.get(getListener().getGwId(),messageId.trim());
            

            // Async 비온라인일 경우
            if(mapper != null && trxMapper != null) {
            	LogManager.debug("Async 비온라인 호출됨..");
            	
                String syncType = config.getParameter(ConnectorConstants.IO_PROTOCOL);
                
                if(mapper==null){
                    throw new SysException("전문별 거래 매핑 정보가 없습니다."
                            +"\nMessageEngineContext::getMessageTrxMappingMap is null");
                }
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
                BaseOrgHandler handler = 
                	MessageEngine.createMessageHander(orgId, trx.getTrxType(), IOType.REQUEST_SENDER_TYPE, getListener().getRunningMode().getType());
                
                ctx = MessageEngine.getMessageContext(trxId, orgId, IOType.REQUEST_SENDER_TYPE);
                
                if(ctx.getTrxMessage()==null){
                	throw new MessageException(trxId + "," + orgId +", " + IOType.REQUEST_SENDER_TYPE +"에 해당하는 거래 전문 정보를 찾을 수 없습니다.");
                }
                
                //put recv data to dataMap
                DataMap dataMap = createDataMap(orgId, trxId, messageId);
                dataMap.put(MessageConstants.MESSAGE_RECEIVED, recvData);
                
                AsyncMultiResponseMessageWorker worker = new AsyncMultiResponseMessageWorker();
                worker.init(this, syncType, handler, ctx, dataMap, trxMapper.getBizAppId());
                worker.execute();
            }
            // 온라인일 경우
            else {
            	LogManager.debug("Async 온라인 호출됨..");
	            String uid = headerParser.getMessageUID();
	            uid = Integer.parseInt(uid)+"";
	            ctx = ProcessingMessagePool.getInstance().get(uid);
	            if(ctx==null){
	                LogManager.error("MSG_ERROR","UID:["+uid+"],Data["+new String(recvData)+"]");
	                return;
	            }else{
	                LogManager.debug(getClass().getName()+"응답 수신 ::UID[" + uid +"]:"
	                        +new String(recvData));
	            }
	            ctx.setResult(recvData);
            }
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

    ///////////////// 지원 되지 않는 메소드들..////////////////////
    
    protected BaseOrgInHandler createOrgInHandler()  {
        throw new SysException("Not Support this method ...");
    }
    
    protected GatewayConnector getNoAckResponseConnector() {
        throw new SysException("Not Support this method ...");
    }


    public WorkerFactory getFactory() {
        throw new SysException("Not Support this method ...");
    }

    public void setFactory(WorkerFactory factory) {
        throw new SysException("Not Support this method ...");
    }
}
