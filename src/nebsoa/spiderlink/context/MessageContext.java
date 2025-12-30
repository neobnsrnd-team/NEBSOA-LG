/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.context;

import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.UIDGenerator;
import nebsoa.spiderlink.engine.adapter.async.AsyncMessageHandler;
import nebsoa.spiderlink.engine.adapter.async.AsyncResponseMessageManager;
import nebsoa.spiderlink.engine.message.ByteMessageInstance;
import nebsoa.spiderlink.exception.MessageSysException;
import nebsoa.spiderlink.exception.MessageTimeoutException;
import nebsoa.spiderlink.queue.ProcessingMessagePool;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 하나의 거래 발생시에 생성되는 context
 * 
 * 2.사용법
 * 
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author $Author: cvs $
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 
 * $Log: MessageContext.java,v $
 * Revision 1.1  2018/01/15 03:39:49  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:29  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:23  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:26  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.2  2008/10/07 02:20:54  youngseokkim
 * getTimeout(), setTimeout() 메소드
 * timeout 값이999미만일때 1000을 곱하도록 수정
 *
 * Revision 1.1  2008/08/04 08:54:52  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.5  2008/05/29 10:33:35  오재훈
 * *** empty log message ***
 *
 * Revision 1.4  2008/05/19 11:24:17  김승희
 * 대응답데이터 필드(ProxtResData)의 데이터 타입을 String에서 Object로 변경
 *
 * Revision 1.3  2008/04/08 04:44:40  김승희
 * Async거래의 경우 응답결과를 AsyncResponseMessageManager를 통해 쓰레드로 처리하게 하는 로직 추가
 *
 * Revision 1.2  2008/03/04 07:30:50  김승희
 * NoAck 리스너-Connector 매핑 관련 멤버 변수 추가
 *
 * Revision 1.1  2008/01/22 05:58:24  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.3  2007/12/26 11:30:26  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2007/12/24 09:02:31  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2007/11/26 08:38:09  안경아
 * *** empty log message ***
 *
 * Revision 1.33  2007/03/07 05:02:43  김승희
 * errorResult 추가
 *
 * Revision 1.32  2007/03/06 06:16:25  안경아
 * *** empty log message ***
 *
 * Revision 1.31  2007/02/07 02:22:53  김성균
 * 거래정보 및 기관정보 없을경우 MessageSysException 던지도록 처리
 *
 * Revision 1.30  2006/12/14 03:35:15  안경아
 * *** empty log message ***
 *
 * Revision 1.29  2006/12/13 05:52:31  김성균
 * *** empty log message ***
 *
 * Revision 1.28  2006/11/10 05:44:20  김승희
 * MSG_ERROR 로그 --> DEBUG로 수정
 *
 * Revision 1.27  2006/10/24 09:14:26  김성균
 * *** empty log message ***
 *
 * Revision 1.26  2006/10/24 08:41:09  김성균
 * *** empty log message ***
 *
 * Revision 1.25  2006/10/24 08:33:10  김성균
 * *** empty log message ***
 *
 * Revision 1.24  2006/10/20 06:58:55  이종원
 * 기본 timeout값 없앰
 *
 * Revision 1.23  2006/10/17 07:49:49  이종원
 * timeout log수정
 *
 * Revision 1.22  2006/10/13 12:00:53  이종원
 * timeout logging추가
 *
 * Revision 1.21  2006/10/13 11:55:03  이종원
 * *** empty log message ***
 *
 * Revision 1.20  2006/10/13 11:54:09  이종원
 * timeout 설정부분을 이미 설정 된 값이 있으면 설정 안함.
 *
 * Revision 1.19  2006/10/13 11:45:50  이종원
 * timeout logging추가
 *
 * Revision 1.18  2006/10/13 09:14:36  이종원
 * Logging MSG_ERROR로 수정
 *
 * Revision 1.17  2006/10/13 08:56:53  이종원
 * Logging 수정
 *
 * Revision 1.16  2006/10/11 02:02:35  이종원
 * default timeout을 default property에서 읽어 오게 수정
 *
 * Revision 1.15  2006/09/23 02:16:43  이종원
 * *** empty log message ***
 *
 * Revision 1.14  2006/09/22 22:57:06  이종원
 * 변경없음
 *
 * Revision 1.13  2006/09/19 09:13:05  김승희
 * getResult 메소드 변경 result가 exception이면 throw한다.
 *
 * Revision 1.12  2006/09/18 11:56:57  김승희
 * *** empty log message ***
 *
 * Revision 1.11  2006/09/18 07:25:28  김승희
 * GW 속성 추가에 따른 수정
 *
 * Revision 1.10  2006/09/18 02:11:45  김승희
 * response, request Message 필드 추가
 *
 * Revision 1.9  2006/09/16 13:32:53  이종원
 * update
 *
 * Revision 1.8  2006/09/16 12:01:39  이종원
 * toString, setResult,getResult, isError wait 하도록 구현
 *
 * Revision 1.7  2006/09/11 08:11:19  김승희
 * UID 관련 변경
 *
 * Revision 1.6  2006/08/25 01:24:54  김승희
 * 시스템 관련 테이블 변경에 따른 수정
 *
 * Revision 1.5  2006/07/27 05:16:13  김승희
 * trx type 얻어오는 메소드 추가
 *
 * Revision 1.4  2006/06/20 04:13:57  김성균
 * TrxMessage 사용하도록 수정
 *
 * Revision 1.2  2006/06/19 13:45:55  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2006/06/19 07:33:48  김승희
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public class MessageContext {
    
	private TrxMessage trxMessage;
	
	private OrgSystemList orgSystemList;
	
	private int uid;
	
	private long trxTimeMillis;
    
    private long timeout;
    
    /**
     *  응답 결과
     */
    private Object result;
	
    /**
     * 요구,응답 구분
     */
    private String reqResType;
    
	/**
	 *  요청 메시지
	 */
	private ByteMessageInstance requestMessage;
	
	private String ip;
	
	private int port;
	
	/**
     *  에러 결과(리스너에서 사용)
     */
    private Object errorResult;

    /**
     *  NoAck 리스너-Connector 매핑 식별자
     */
    private String listenerConnectorMappingIndentifier;
    
    /**
     *  현재 전문의 수신 또는 전송을 담당하는 Gateway ID 
     */
    private String gatewayId;
    
    /**
     *  현재 전문의 수신 또는 전송을 담당하는 System ID 
     */
    private String systemId;
    
    /**
     * Async 거래의 MessageHandler 
     */
    private AsyncMessageHandler asyncMessageHandler;
    
	public AsyncMessageHandler getAsyncMessageHandler() {
		return asyncMessageHandler;
	}

	public void setAsyncMessageHandler(AsyncMessageHandler asyncMessageHandler) {
		this.asyncMessageHandler = asyncMessageHandler;
	}

	public MessageContext(){
		//UID 생성
		//수정 - 8자리로 생성하는 UID로 변경
		//uid = UIDGenerator.generateUID();
		uid = Integer.parseInt(UIDGenerator.nextDailyUID7L());
		
		//트랜잭션시각 생성
		trxTimeMillis = System.currentTimeMillis();
	}
	
	/**
	 * @return Returns the trxMessage.
	 */
	public TrxMessage getTrxMessage() {
		return trxMessage;
	}

	/**
	 * @param trxMessage The trxMessage to set.
	 */
	public void setTrxMessage(TrxMessage trxMessage) {
		this.trxMessage = trxMessage;
	}
    
	public Trx getTrx() {
        if (trxMessage.getTrx() == null) {
            throw new MessageSysException("FRM00012", "거래정보가 존재하지 않습니다.");
        }
        return trxMessage.getTrx();
	}
    
	public Org getOrg() {
        if (trxMessage.getOrg() == null) {
            throw new MessageSysException("FRM00013", "기관정보가 존재하지 않습니다.");
        }
        return trxMessage.getOrg();
	}

	
	/**
	 * 작성일자 : 2008. 05. 23 
	 * 작성자 : kfetus
	 * 설명 : 기업은행에서 각 업무마다(개인,기업,제휴) 포트를 나누어 전송해달라는 요청에 의해
	 * 관리자에서 기관을 등록하고 OutHandler에서 기관을 바꾸기 위해 추가한 메소드 
	 * @param orgId
	 */
	public void setOrgId(String orgId) {
        if (trxMessage.getOrg() == null) {
            throw new MessageSysException("FRM00013", "기관정보가 존재하지 않습니다.");
        }
        Org org = trxMessage.getOrg();
        org.setOrgId(orgId);
	}
	
	public String getIoType() {
		return trxMessage.getIoType();
	}

	public String getMessageId() {
		return trxMessage.getMessageId();
	}

	public String getOperModeType() {
		return getTrx().getOperModeType();
	}

	public String getOrgId() {
		return getOrg().getOrgId();
	}

	public Object getProxyResData() {
		return trxMessage.getProxyResData();
	}

	public String getProxyResYn() {
		return trxMessage.getProxyResYn();
	}

	public String getResMessageId() {
		return trxMessage.getResMessageId();
	}

	public String getStdMessageId() {
		return trxMessage.getStdMessageId();
	}

	public String getStdResMessageId() {
		return trxMessage.getStdResMessageId();
	}

	public String getTrxId() {
		return getTrx().getTrxId();
	}
    
	public String getTrxName() {
		return getTrx().getTrxName();
	}
	
	public String getTrxType() {
		return getTrx().getTrxType();
	}

	public long getTrxTimeMillis() {
		return trxTimeMillis;
	}

	public void setTrxTimeMillis(long trxTimeMillis) {
		this.trxTimeMillis = trxTimeMillis;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public OrgSystemList getOrgSystemList() {
		return orgSystemList;
	}

	public void setOrgSystemList(OrgSystemList orgSystemList) {
		this.orgSystemList = orgSystemList;
	}

	public ByteMessageInstance getRequestMessage() {
		
		return requestMessage;
	}

	public void setRequestMessage(ByteMessageInstance requestMessage) {
		this.requestMessage = requestMessage;
	}
    
    public String toString(){
        try{
    		return "MessageId:"+getMessageId()+",MessageUid:"+getUid();
    	}catch(Exception e){
    		return null;
    	}
    }

    public synchronized Object getResult() throws Throwable {
        long start=System.currentTimeMillis();
        if(result == null){
            try {
//                LogManager.debug("Wait for :"+toString()+"timeout:["+getTimeout()+"]");
                this.wait(getTimeout());
            } catch (InterruptedException e) {
                //ignore..
                LogManager.error(e.toString());
            }
        }
        long end = System.currentTimeMillis();
        double total = (end-trxTimeMillis)/1000.0;
        LogManager.debug(this+" 총수행시간:"+total+"초,"+
                ((end-start)>20?("Wait time:"+((end-start)/1000.0)+"초"):""));
        if(result ==null){
            ProcessingMessagePool.getInstance().remove(this);
            throw new MessageTimeoutException(
                    "FRM20005","Response wait timeout::"+this+" [경과시간"+((end-start)/1000.0)+"초]");
        }
        
        if(result instanceof Throwable){
        	throw (Throwable)result;
        }
        return result;
    }

    public synchronized void setResult(Object result) {
        if(result == null){
            throw new SysException("NULL Response Message");
        }
        
        this.result = result;
        
        LogManager.debug("Wakeup for :"+toString());
        this.notify();
        
        //Async 거래의 경우..
        if(TrxType.isAsync(getTrxMessage().getTrx().getTrxType())){
        	AsyncResponseMessageManager.getInstance().startResponseProcess(this);
        }
    }
/*    
    *//**
     * 
     * 2006. 9. 16.  KOREAN 작성
     * @return
     * @throws MessageTimeoutException 
     *//*
    public boolean isError() throws MessageTimeoutException{
        return getResult() instanceof Throwable;
    }*/
    /**
     * 해당 전문 transaction의 Timeout값이다.
     * 지정하지 않으면  default.properties에서 RESPONSE_TIMEOUT값을
     * 읽어 세팅 한다. 그 값 조차도 없으면 60초로 세팅된다.
     */
    public long getTimeout() {
//        if(timeout==0) { // default 2분.
//            timeout= PropertyManager.getIntProperty(
//                    "default","RESPONSE_TIMEOUT",180);
//        }
        //초 단위로 세팅했다고 가정하고 1000을 곱한다.
        if(timeout < 999){
            timeout = timeout*1000;
        }
        //LogManager.debug("msg context setted timeout----->"+timeout);
        return timeout;
    }

    public void setTimeout(long timeout) {
//        if(this.timeout>0){
//            LogManager.debug("MSG_ERROR","이미 timeout이 설정되어 있으므로 skip"+timeout);
//            return;        
//        }
        //초 단위로 세팅했다고 가정하고 1000을 곱한다.
        if(timeout < 999){
            timeout = timeout*1000;
        }
        LogManager.debug("DEBUG",toString()+"의 Timeout값 : "+(timeout/1000.0)+"초로 세팅");
        this.timeout = timeout;
    }

	public String getReqResType() {
		return reqResType;
	}

	public void setReqResType(String reqResType) {
		this.reqResType = reqResType;
	}

    public long getDefaultTimeout() {
        return timeout;
    }

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getIp() {
		return ip;
	}

	public int getPort() {
		return port;
	}
	
	public Object getErrorResult() {
		return errorResult;
	}

	public void setErrorResult(Object errorResult) {
		this.errorResult = errorResult;
	}

	public String isHexLogYn() {
		return trxMessage.getHexLogYn();
	}
	
	public String getListenerConnectorMappingIndentifier() {
		return listenerConnectorMappingIndentifier;
	}

	public void setListenerConnectorMappingIndentifier(
			String listenerConnectorMappingIndentifier) {
		this.listenerConnectorMappingIndentifier = listenerConnectorMappingIndentifier;
	}

	public String getGatewayId() {
		return gatewayId;
	}

	public void setGatewayId(String gatewayId) {
		this.gatewayId = gatewayId;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}
}
