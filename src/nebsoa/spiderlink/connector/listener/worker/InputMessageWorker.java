/*
 * Spider Framework
 *
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 *
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.connector.listener.worker;

import java.io.IOException;

import nebsoa.biz.base.ErrorManageBiz;
import nebsoa.common.Context;
import nebsoa.common.error.ErrorManager;
import nebsoa.common.exception.DBException;
import nebsoa.common.exception.SpiderException;
import nebsoa.common.exception.SysException;
import nebsoa.common.jdbc.DBManager;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.DataMap;
import nebsoa.common.util.FormatUtil;
import nebsoa.common.util.PropertyManager;
import nebsoa.common.util.StringUtil;
import nebsoa.spiderlink.connector.constants.SyncType;
import nebsoa.spiderlink.connector.listener.protocol.TcpInputMessageHandler;
import nebsoa.spiderlink.context.MessageContext;
import nebsoa.spiderlink.engine.adapter.tcp.BaseOrgInHandler;
import nebsoa.spiderlink.exception.ErrorResponseException;
import nebsoa.util.workerpool.Worker;


/**
 * worker
 * @author 이종원
 */
/*******************************************************************
 * <pre>
 * 1.설명
 * Pool 에서 관리되는  객체
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
 * $Log: InputMessageWorker.java,v $
 * Revision 1.1  2018/01/15 03:39:54  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:38  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.3  2009/11/13 12:51:39  jglee
 * import문 정제
 *
 * Revision 1.2  2009/11/13 12:48:05  jglee
 * loggerHandle 수정 - url정보, stacktrace 정보 처리 추가
 *
 * Revision 1.1  2008/11/18 11:27:27  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:29  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.3  2008/10/23 00:03:30  김성균
 * 오류정보 저장부분 수정
 *
 * Revision 1.2  2008/09/25 07:46:16  jglee
 * 수동거래 에러시 DB로그 처리
 *
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.3  2008/03/11 01:26:00  김승희
 * 응답 없는 거래 처리
 *
 * Revision 1.2  2008/03/04 07:39:52  김승희
 * doNoAckProcess 수정
 *
 * Revision 1.1  2008/01/22 05:58:34  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:56  안경아
 * *** empty log message ***
 *
 * Revision 1.3  2007/03/07 05:03:53  김승희
 * Async 리스너의 핸들러 수행 시 에러 전문 전송 기능 추가
 *
 * Revision 1.2  2006/10/10 07:28:00  이종원
 * *** empty log message ***
 *
 * Revision 1.1  2006/10/03 09:12:44  이종원
 * refactoring
 *
 * Revision 1.4  2006/10/03 06:06:34  이종원
 * noack 응답 처리 기능 변경 GatewayManager.sendResponseMessage
 *
 * Revision 1.3  2006/10/03 04:20:36  이종원
 * *** empty log message ***
 *
 * Revision 1.2  2006/10/02 23:44:21  이종원
 * *** empty log message ***
 *
 * Revision 1.1  2006/10/02 19:48:56  이종원
 * 기본기능 구현
 *
 * </pre>
 ******************************************************************/
public class InputMessageWorker extends Worker{

    protected TcpInputMessageHandler owner;
    private String ioProtocol;
    protected BaseOrgInHandler handler;
    protected MessageContext messageContext;
    protected DataMap dataMap;

    //protected GatewayConnector responseConnector;
    private static final String ERROR_HIS_INSERT_SQL = ErrorManageBiz.ERROR_HIS_INSERT_SQL;

    public InputMessageWorker(){

    }

    public void setHandler(BaseOrgInHandler handler){
        this.handler = handler;
    }

    /**
     * 요청 받은 전문을 처리한다.
     * ioProtocol이  SYNC나 ASYNC일 때는 doIOProcess.
     * No Ack일 때는 doNoAckProcess메소드를 호출 한다.
     * 응답 메시지가 없는 경우 doNoResProcess()를 호출한다.
     * 2006. 10. 2.  이종원 작성, 2008. 3. 10 김승희 수정
     * @throws Throwable
     * @throws IOException
     */
    public void doProcess() throws IOException, Throwable {
    	//응답 메시지가 없는 유형인 경우
    	if(messageContext.getTrxMessage().isResMessageNone()){
    		doNoResProcess();

        //응답 메시지가 있는 경우
    	}else{
	        if(SyncType.isSync(this.ioProtocol)
	                || SyncType.isAsync(this.ioProtocol)){
	            doIOProcess();
	        }else if(SyncType.isNoAck(this.ioProtocol)){
	            doNoAckProcess();
	        }
    	}
    }


    /**
     * 응답 메시지가 존재하는 Sync/Async 유형 처리
     * @throws IOException
     * @throws Throwable
     */
    private void doIOProcess() throws IOException, Throwable {
        if(handler ==null) throw new Error("handler is null");
        LogManager.debug(handler+" OrgHandler의 process(msgCtx,dataMap)를  호출.");

        try{
        	dataMap = handler.process(messageContext,  dataMap);
            LogManager.debug(owner+" MsgHandler의 sendResponse (byte[] msg)를  호출.");
            //응답 전송
            owner.sendResponse((byte[]) messageContext.getResult());
            LogManager.debug(owner+" MsgHandler의 sendResponse OK.");

        }catch(Throwable th){
        	LogManager.debug(owner+" handler 수행 시 에러 발생. 에러 전문 전송");

        	/*
        	 * handler 수행 시 발생한 에러(전문 송수신과 무관한 에러)에 대해서는 보통 에러 전문을 보내야 한다.
        	 * 아래는 그 경우를 처리하기 위한 메소드 호출이다.
        	 */

        	owner.handleError(messageContext, dataMap, th);
        	LogManager.debug(owner+" handler 수행 시 에러 발생. 에러 전문 전송 완료");
        	loggerHandle(messageContext, dataMap, th);
        }

    }

    /**
     * 응답 메시지가 존재하는 No Ack 유형 처리
     * @throws Throwable
     */
    private void doNoAckProcess() throws Throwable {
        if(handler ==null) throw new Error("handler is null");
        LogManager.debug(handler+" OrgHandler의 process(msgCtx,dataMap)를  호출.");

        try{
        	dataMap = handler.process( messageContext,  dataMap);
        	LogManager.debug(owner+" responseConnector의 handleMessage(msgCtx)를  호출.");
        	//GatewayManager.getInstance().sendResponseMessage(messageContext);
            //응답 전송
        	owner.sendResponse(messageContext);
            LogManager.debug(owner+" MsgHandler의 sendResponse OK.");
        }catch(Throwable th){
        	LogManager.debug(owner+" handler 수행 시 에러 발생. 에러 전문 전송");
        	owner.handleError(messageContext, dataMap, th);
        	LogManager.debug(owner+" handler 수행 시 에러 발생. 에러 전문 전송 완료");
        	loggerHandle(messageContext, dataMap, th);
        }
    }

    /**
     * 응답 메시지가 없는 거래 처리
     */
    private void doNoResProcess(){
    	LogManager.debug("응답메시지가 없는 유형이므로 handler 호출만 진행됩니다.");
    	if(handler ==null) throw new Error("handler is null");
        LogManager.debug(handler+" OrgHandler의 process(msgCtx,dataMap)를  호출.");

        try{
        	dataMap = handler.process( messageContext,  dataMap);

        }catch(Throwable th){
        	//발생한 exception에 대한 충분한 처리는 handler에서 수행했다고 가정하고 로그만 남긴다.
        	//응답 메시지가 없으므로 응답 전문 전송도 없다.
        	LogManager.error(owner+" handler 수행 시 에러 발생." + th);
        }
    }

    /**
     * 풀로 돌아가서 다시 일하기 위해 초기화 한다.
     * do nothing...
     * 2006. 10. 2.  이종원 작성
     */
    public void reset() {
        handler=null;
        messageContext=null;
        dataMap=null;
    }

    /**
     * do nothing....
     * 2006. 10. 2.  이종원 작성
     */
    public void destroy() {
        reset();
    }

    public DataMap getDataMap() {
        return dataMap;
    }

    public void setDataMap(DataMap dataMap) {
        this.dataMap = dataMap;
    }

    public MessageContext getMessageContext() {
        return messageContext;
    }

    public void setMessageContext(MessageContext messageContext) {
        this.messageContext = messageContext;
    }


    public TcpInputMessageHandler getOwner() {
        return owner;
    }

    public void setOwner(TcpInputMessageHandler owner) {
        this.owner = owner;
    }

    public void init(TcpInputMessageHandler msgHandler
            ,String syncType
            ,BaseOrgInHandler orgHandler, MessageContext ctx,
            DataMap dataMap) {
        this.owner = msgHandler;
        this.handler = orgHandler;
        this.messageContext = ctx;
        this.dataMap = dataMap;
        this.ioProtocol=syncType;
    }

    public String getIoProtocol() {
        return ioProtocol;
    }

    public void setIoProtocol(String ioProtocol) {
        this.ioProtocol = ioProtocol;
    }

//    public GatewayConnector getResponseConnector() {
//        return responseConnector;
//    }
//
//    public void setResponseConnector(GatewayConnector responseConnector) {
//        this.responseConnector = responseConnector;
//    }

    public BaseOrgInHandler getHandler() {
        return handler;
    }

	/**
     * handler 수행중 에러 발생시 FWK_ERROR_HIS 테이블에 등록
     * @param messageContext
     * @param map
     * @param e
     * @throws DBException
     */
    public static void loggerHandle(MessageContext messageContext, DataMap map, Throwable e) throws DBException{
		LogManager.debug("### handler 수행 시 에러 발생. DB 로그 처리 시작 ###");

    	try {
            String errorTime = null;
            String errorCode = null;
            String errorMessage = null;
            String trxSerNo = null;
            String userId = null;
            String errorUrl = null;
            String errorStackTrace = null;

    		errorTime = FormatUtil.getFormattedDate(System.currentTimeMillis(), "yyyyMMddHHmmss");

    		Context ctx = map.getContext();
            if (ctx != null) {
                trxSerNo = ctx.getTrxSerNo();
                userId = ctx.getUserId();
                errorUrl = ctx.getUri();
            }

            errorMessage = e.getMessage();
            if (errorMessage == null) {
                errorMessage = e.toString();
            }

            errorMessage = getLogMsg(messageContext) + "|" + errorMessage;

            if (e instanceof SpiderException) {
                Throwable cause = e.getCause();
                if (cause == null) {
                    LogManager.debug("Nested exception 없을 경우 : " + e);
                    errorCode = ((SpiderException) e).getErrorCode();
                } else if (cause instanceof SpiderException) {
                    LogManager.debug("Nested exception 있을 경우 : " + cause);
                    errorCode = ((SpiderException) cause).getErrorCode();
                } else {
                    LogManager.debug("Nested exception 있지만 SpiderException 아닌 경우 : " + cause);
                    errorCode = ((SpiderException) e).getErrorCode();
                }
                LogManager.debug("### SpiderException errorCode = " + errorCode);
            }

            if (StringUtil.isNull(errorCode)) {
                errorCode = PropertyManager.getProperty("default", "DEFAULT_ERROR_CODE", "FRS99999");
            }

            if (StringUtil.isNull(trxSerNo)) {
                trxSerNo = "NO_DATA";
            }

            if (StringUtil.isNull(userId)) {
                userId = "NO_DATA";
            }

            if (StringUtil.isNull(errorUrl)) {
                errorUrl = "";
            }

            if (errorMessage.getBytes().length > 500) {
                byte[] msg = errorMessage.getBytes();
                errorMessage = new String(msg, 0, 500);
            }

            //기관에서 Business적인 오류는 errorMessage insert되도록 수정 2009.03.19
			if(e instanceof ErrorResponseException ||
					(e instanceof SysException && "FRS00021".equals(((SysException)e).getErrorCode())) ){
				errorStackTrace = errorMessage;
			}else{
				errorStackTrace = ErrorManageBiz.stackTraceToString(e);
			}

			Object[] param = {
                    errorCode
                   ,trxSerNo
                   ,userId
                   ,errorMessage
                   ,errorTime
                   ,errorUrl
                   ,errorStackTrace
			};

            DBManager.executePreparedUpdate(ERROR_HIS_INSERT_SQL, param);

            // 오류별 처리 핸들러 호출 실행 가능 여부
            boolean isErrorSave = PropertyManager.getBooleanProperty("default", "ERROR_HANDLER_CALL", "FALSE");
            if (isErrorSave) {
                // 오류별 처리 핸들러 호출
                DataMap errorMap = new DataMap();
                errorMap.put("errorCode", errorCode);  // 오류코드
                errorMap.put("trxSerNo", trxSerNo);  // 오류 일련번호
                ErrorManager.getInstance().execute(errorMap);
            }

        } catch (Exception ex) {
            LogManager.error("ERROR", e.toString(), ex);
        }
		LogManager.debug("### handler 수행 시 에러 발생. DB 로그 처리 종료 ###");
    }

    /**
	 * MessageId, MessageUid 리턴 처리.
	 * @param messageContext
	 * @return
	 */
    public static String getLogMsg(MessageContext messageContext) {
    	if(messageContext==null) return "";
    	String msg = "MessageId:" + StringUtil.NVL(messageContext.getMessageId(),"");
    	msg += " | MessageUid:" + StringUtil.NVL(messageContext.getUid()+"","");
      	return msg;
    }

}