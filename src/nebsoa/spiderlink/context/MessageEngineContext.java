/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.context;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.map.MultiKeyMap;

import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.DataMap;
import nebsoa.management.ManagementObject;


/*******************************************************************
 * <pre>
 * 1.설명 
 * 메시지 엔진 정보를 담고 있는 context 클래스
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
 * $Log: MessageEngineContext.java,v $
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
 * Revision 1.1  2008/08/04 08:54:52  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.3  2008/06/20 02:13:03  김영석
 * 접근허용자 리로딩 로직 추가
 *
 * Revision 1.2  2008/03/04 07:37:31  김승희
 * 리스너-응답 커넥터 매핑 정보 로딩 관련 추가
 *
 * Revision 1.1  2008/01/22 05:58:24  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.4  2008/01/21 04:02:12  안경아
 * *** empty log message ***
 *
 * Revision 1.3  2008/01/11 10:40:21  안경아
 * 다중응답전문 처리
 *
 * Revision 1.2  2007/12/28 05:49:02  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2007/11/26 08:38:09  안경아
 * *** empty log message ***
 *
 * Revision 1.33  2006/11/23 00:39:49  김승희
 * initialize 메소드 추가
 *
 * Revision 1.32  2006/11/22 12:20:10  김성균
 * *** empty log message ***
 *
 * Revision 1.31  2006/11/22 11:38:12  김성균
 * *** empty log message ***
 *
 * Revision 1.30  2006/11/14 05:41:19  안경아
 * *** empty log message ***
 *
 * Revision 1.29  2006/10/30 04:10:07  김승희
 * 주석 제거
 *
 * Revision 1.28  2006/10/26 00:40:39  안경아
 * *** empty log message ***
 *
 * Revision 1.27  2006/10/21 05:35:23  안경아
 * *** empty log message ***
 *
 * Revision 1.25  2006/10/02 11:38:32  김승희
 * 사용안하는 메소드 제거
 *
 * Revision 1.24  2006/10/02 11:24:01  이종원
 * listener가 전문 수신시 해당 거래 매칭되는 매핑 정보 캐쉬하는 로직 구현
 *
 * Revision 1.23  2006/10/02 08:01:51  김승희
 * MessageHandler, System 중지여부 재로딩 메소드 추가
 *
 * Revision 1.22  2006/09/25 07:05:20  김승희
 * 상수 적용
 *
 * Revision 1.21  2006/09/18 07:25:28  김승희
 * GW 속성 추가에 따른 수정
 *
 * Revision 1.20  2006/09/01 01:37:05  김승희
 * 리로딩 메소드 수정
 *
 * Revision 1.19  2006/08/31 09:12:54  김승희
 * 핸들러, GW, 시스템 정보 리로딩 구현
 *
 * Revision 1.18  2006/08/29 04:27:16  김성균
 * *** empty log message ***
 *
 * Revision 1.17  2006/08/25 01:24:54  김승희
 * 시스템 관련 테이블 변경에 따른 수정
 *
 * Revision 1.16  2006/08/16 02:49:25  김성균
 * reload() 추가
 *
 * Revision 1.15  2006/08/02 08:11:02  김승희
 * ManagementObject  메소드명 변경
 *
 * Revision 1.14  2006/07/31 08:33:40  김성균
 * comment 추가 등...
 *
 * Revision 1.13  2006/07/27 08:02:32  김승희
 * 기관송수신프로토콜, 기관 시스템 테이블 변경에 따른 수정
 *
 * Revision 1.12  2006/07/26 07:59:20  김성균
 * reloadAll() 구현
 *
 * Revision 1.11  2006/07/25 02:26:31  김성균
 * DB to XML 반영
 *
 * Revision 1.10  2006/07/04 08:55:14  김성균
 * *** empty log message ***
 *
 * Revision 1.9  2006/06/21 13:47:57  김성균
 * 거래중지 처리부분 추가
 *
 * Revision 1.8  2006/06/20 13:58:06  김성균
 * reload() 추가
 *
 * Revision 1.7  2006/06/20 08:46:30  김승희
 * 기관 시스템 정보 관련 수정
 *
 * Revision 1.6  2006/06/20 04:51:27  김승희
 * trxInfoMap, orgInfoMap --> HashMap으로 변경
 *
 * Revision 1.5  2006/06/20 04:15:44  김성균
 * Trx, Org, TrxMessage 정보 포함하도록 수정
 *
 * Revision 1.3  2006/06/19 13:45:55  김성균
 * *** empty log message ***
 *
 * Revision 1.2  2006/06/19 06:25:20  김승희
 * *** empty log message ***
 *
 *
 * </pre>
 ******************************************************************/
public class MessageEngineContext extends ManagementObject {
	
    private static Object dummy = new Object();
    
	/**
	 * Comment for <code>messageEngineContext</code>
	 */
	private static MessageEngineContext messageEngineContext;
	
	/**
	 * Comment for <code>trxInfoMap</code>
	 */
	private Map trxInfoMap;
	
	/**
	 * Comment for <code>orgInfoMap</code>
	 */
	private Map orgInfoMap;
	
	/**
	 * Comment for <code>systemInfoMap</code>
	 */
	private MultiKeyMap systemInfoMap;
	
	/**
	 * Comment for <code>trxMessageInfoMap</code>
	 */
	private MultiKeyMap trxMessageInfoMap;
	
	/**
	 * Comment for <code>messageHandlerMap</code>
	 */
	private MultiKeyMap messageHandlerMap;
	
    /**
     * 리스너가 수신한 전문별 관련 거래 정보 찾는 map 
     */
    private MultiKeyMap messageTrxMappingMap;
	
	/**
	 * Comment for <code>gatewayInfoMap</code>
	 */
	private MultiKeyMap gatewayInfoMap;
	
	/**
	 * Comment for <code>wasListenerMap</code>
	 */
	private MultiKeyMap wasListenerMap;
	
	/**
	 * GW_ID를 key로 하여 모든 GateWay정보를 갖고 있는 Map
	 */
	private Map gateWayMap;

	/**
	 * 다중 응답 전문 Map
	 */
	private MultiKeyMap multiResMessageMap;
	
	/**
	 * No Ack 리스너와 응답 커넥터 사이의 맵핑 정보를 담고 있는 Map
	 */
	private MultiKeyMap listenerConnectorMappingMap;
	
	
    /**
     * 싱글톤 처리 
     */
	private MessageEngineContext(){
		trxInfoMap = new HashMap();
		orgInfoMap = new HashMap();
		systemInfoMap = new MultiKeyMap();
		trxMessageInfoMap = new MultiKeyMap();
		messageHandlerMap = new MultiKeyMap();
		gatewayInfoMap = new MultiKeyMap();
		wasListenerMap = new MultiKeyMap();
		gateWayMap = new HashMap();
        //이종원 추가
        messageTrxMappingMap = new MultiKeyMap();
        multiResMessageMap = new MultiKeyMap();
        listenerConnectorMappingMap = new MultiKeyMap();
	}
    
    private static void createContext() {
        synchronized (dummy) {
            messageEngineContext = new MessageEngineContext();
        }
    }
    
    /**
     * 싱글톤 객체 얻어오기
     * @return
     */
	public static MessageEngineContext getContext(){
        if (messageEngineContext == null) {
        	initialize();
        }
        return messageEngineContext;
	}
    
	/**
	 * 메시지엔진컨텍스트를 초기화한다.
	 */
	public static void initialize(){
		createContext();
        messageEngineContext.loadContextData();
        messageEngineContext.toXml();
	}
	
	/**
	 * MessageEngineContext 정보를 로딩한다.
	 */
	private void loadContextData() {
		if (isXmlMode()) {
			try {
				fromXml();
			} catch (FileNotFoundException e) {
				throw new SysException("XML 파일을 찾을 수 없습니다.");
			}
		} else {
			MessageEngineContextLoader.loadTrx(messageEngineContext);
			MessageEngineContextLoader.loadAccessUser(messageEngineContext);
			MessageEngineContextLoader.loadOrg(messageEngineContext);
			MessageEngineContextLoader.loadTrxMessage(messageEngineContext);

			MessageEngineContextLoader.loadMesssageHandler(messageEngineContext);
			//반드시 loadGateway 를 loadSystem 보다 먼저 호출한다.
			MessageEngineContextLoader.loadGateway(messageEngineContext);
			MessageEngineContextLoader.loadSystem(messageEngineContext);
			MessageEngineContextLoader.loadWasListener(messageEngineContext);
            //이종원 추가 : 리스너가 수신한 전문에 대한 거래 정보 찾기.
            MessageEngineContextLoader.loadInputMesssageTrxMapping(messageEngineContext);
            //다중응답전문 정보
            MessageEngineContextLoader.loadMultiResMessage(messageEngineContext);
            //리스너-커넥터 맵핑 정보 (반드시 loadGateway,loadSystem 보다 나중에 호출한다.)
            MessageEngineContextLoader.loadListenerConnectorMapping(messageEngineContext);
		}
	}
	
		/**
	 * 리스너-커넥터 맵핑 정보 Map을 리턴한다.
	 * @return
	 */
	public MultiKeyMap getListenerConnectorMappingMap() {
		return listenerConnectorMappingMap;
	}

	/**
	 * 리스너-커넥터 맵핑 정보 Map을 셋팅한다.
	 * @param listenerConnectorMappingMap
	 */
	public void setListenerConnectorMappingMap(
			MultiKeyMap listenerConnectorMappingMap) {
		this.listenerConnectorMappingMap = listenerConnectorMappingMap;
	}
	
	/**
	 * No ack 리스너의 응답  GatewaySystem 정보를 리턴한다.
	 * @param gatewayId 리스너 gateway ID
	 * @param systemId 리스너system ID
	 * @param identifier 맵핑 식별자
	 * @return
	 */
	public GatewaySystem getResponseGatewaySystem(String gatewayId, String systemId, String identifier){
		return  (GatewaySystem)getListenerConnectorMappingMap().get(gatewayId, systemId, identifier);
	}
	
	/**
	 * @return Returns the messageInoutInfoMap.
	 */
	public MultiKeyMap getMessageHandlerMap() {
		return messageHandlerMap;
	}

	/**
	 * @return Returns the systemInfoMap.
	 */
	public MultiKeyMap getSystemInfoMap() {
		return systemInfoMap;
	}

	/**
	 * @return Returns the trxMessageInfoMap.
	 */
	public MultiKeyMap getTrxMessageInfoMap() {
		return trxMessageInfoMap;
	}
	
	/**
	 * @return Returns the orgInfoMap.
	 */
	public Map getOrgInfoMap() {
		return orgInfoMap;
	}

	/**
	 * @return Returns the trxInfoMap.
	 */
	public Map getTrxInfoMap() {
		return trxInfoMap;
	}
	
	/**
	 * @return Returns the Trx.
	 */
	public Trx getTrx(String trxId) {
		return (Trx) trxInfoMap.get(trxId);
	}
	
	/**
	 * @return Returns the Org.
	 */
	public Org getOrg(String orgId) {
		return (Org) orgInfoMap.get(orgId);
	}


	/*public String getProtocolHandlerClassName(String orgId, String trxType, String ioType) {
		OrgProtocol messageInout = (OrgProtocol) messageHandlerMap.get(orgId, trxType, ioType);
		return messageInout.getProtocolHandler();
	}*/
	
	/**
	 * 전문처리핸들러클래스명을 리턴한다.
	 * @param orgId 기관ID
	 * @param trxType 거래유형
	 * @param ioType 기동수동구분
	 * @param operModeType 운영모드타입
	 * @return
	 */
	public String getHandlerClassName(String orgId, String trxType, String ioType, String operModeType) {
		MessageHandler messageHandler = (MessageHandler) messageHandlerMap.get(orgId, trxType, ioType, operModeType);
		
		return messageHandler.getMessageHandler().trim();
	}
	
	public MessageContext getMessageContext(String trxId, String orgId, String ioType) {
		TrxMessage trxMessage = (TrxMessage) trxMessageInfoMap.get(trxId, orgId, ioType);
		//시스템 정보
		//Gateway gateway = (Gateway)gatewayInfoMap.get(orgId, trxType, ioType, reqResType);
		
		MessageContext messageContext = new MessageContext();
		messageContext.setTrxMessage(trxMessage);
		return messageContext;
	}
	
	/**
	 * 현재 거래가 전송될 시스템 리스트를 리턴한다.
	 * @param orgId 기관ID
	 * @param trxType 거래유형
	 * @param ioType 기동수동구분
	 * @param operModeType 운영모드타입
	 * @return OrgSystemList
	 */
	public OrgSystemList getOrgSystemList(String orgId, String trxType, String ioType, String operModeType, String reqResType){
		Gateway gateway = (Gateway)MessageEngineContext.getContext().getGatewayInfoMap().get(orgId, trxType, ioType, reqResType);
		return (OrgSystemList)MessageEngineContext.getContext().getSystemInfoMap().get(gateway.getGwId(), operModeType);
		
	}
	
	/**
	 * 현재 거래가 전송될 Gateway를 리턴한다.
	 * @param orgId 기관ID
	 * @param trxType 거래유형
	 * @param ioType 기동수동구분
	 * @return Gateway
	 */
	public Gateway getGateway(String orgId, String trxType, String ioType, String reqResType){
		return  (Gateway)getGatewayInfoMap().get(orgId, trxType, ioType, reqResType);
	}
	
	/* (non-Javadoc)
	 * @see nebsoa.management.ManagementObject#getInstance()
	 */
	public Object getManagementObject() {
		return messageEngineContext;
	}

	/* (non-Javadoc)
	 * @see nebsoa.management.ManagementObject#setInstance(java.lang.Object)
	 */
	public void setManagementObject(Object obj) {
		messageEngineContext = (MessageEngineContext) obj;
	}
    
    /**
     * 전체 리로딩
     */
    /*
    public static void reloadAll(DataMap map) {
        getContext().loadContextData();
        getContext().toXml();
    }
    */
    
    /**
     * 거래 리로딩
     */
    public static void reloadTrx(DataMap map){
        if (messageEngineContext == null) {
        	initialize();
        }else{
        	MessageEngineContextLoader.loadTrx(messageEngineContext);
        	MessageEngineContextLoader.loadAccessUser(messageEngineContext);
        	MessageEngineContextLoader.loadTrxMessage(messageEngineContext);
        	getContext().toXml();
        }
    }
    
    /**
     * 접근 허용자 리로딩
     */
    public static void reloadAccessUser(DataMap map) {
    	if(messageEngineContext == null) {
    		initialize();
    	}
    	else {
        	MessageEngineContextLoader.reloadAccessUser(messageEngineContext);
        	getContext().toXml();
    	}
    }
    
    /**
     * 전문처리핸들러, 게이트웨이, 시스템 정보를 리로딩한다.
     * @param dataMap
     */
    public static void reloadHandlerGWSystem(DataMap dataMap){
    	LogManager.info("핸들러, GW, System 정보를 리로딩합니다..");
    	
    	if(messageEngineContext==null){
    		initialize();
    	}else{
    		MessageEngineContextLoader.loadMesssageHandler(messageEngineContext);
    		//반드시 loadGateway 를 loadSystem 보다 먼저 호출한다.
    		MessageEngineContextLoader.loadGateway(messageEngineContext);
    		MessageEngineContextLoader.loadSystem(messageEngineContext);
    		MessageEngineContextLoader.loadWasListener(messageEngineContext);
    	}
    }
    
    /**
     * 전문처리 핸들러의 중지여부를 반영한다.
     * @param dataMap
     */
    public static void reloadMessageHandlerStopYn(DataMap dataMap){
    	LogManager.info("전문처리 핸들러의 중지여부를 반영합니다..");
    	
    	if(messageEngineContext==null){
    		initialize();
    		
    	}else{
    		String orgId = dataMap.getString("orgId");
    		String trxType = dataMap.getString("trxType");
    		String ioType = dataMap.getString("ioType");
    		String operModeType = dataMap.getString("operModeType");
    		String stopYn = dataMap.getString("stopYn");
    		MessageHandler messageHandler = (MessageHandler)messageEngineContext.getMessageHandlerMap().get(orgId, trxType, ioType, operModeType);
    		if(messageHandler!=null){
    			messageHandler.setStopYn(stopYn);
    		}
    	}
    }
    
    /**
     * System의 중지여부를 반영한다.
     * @param dataMap
     */
    public static void reloadSystemStopYn(DataMap dataMap){
    	LogManager.info("System의 중지여부를 반영합니다..");
    	
    	if(messageEngineContext==null){
    		initialize();
    	
    	}else{
    		String gwId = dataMap.getString("gwId");
    		String operModeType = dataMap.getString("operModeType");
    		String systemId = dataMap.getString("systemId");
    		String stopYn = dataMap.getString("stopYn");
    		OrgSystemList orgSystemList = (OrgSystemList)messageEngineContext.getSystemInfoMap().get(gwId, operModeType);
    		if(orgSystemList!=null){
    			for(int i=0; i<orgSystemList.size(); i++){
    				OrgSystemList.OrgSystem orgSystem = (OrgSystemList.OrgSystem)orgSystemList.get(i);
    				if(orgSystem!=null){
    					if(orgSystem.getSystemId().equals(systemId)){
    						orgSystem.setStopYn(stopYn);
    						break;
    					}
    				}
    			}
    		}
    	}
    }
       
	public MultiKeyMap getGatewayInfoMap() {
		return gatewayInfoMap;
	}

	public void setGatewayInfoMap(MultiKeyMap gatewayInfoMap) {
		this.gatewayInfoMap = gatewayInfoMap;
	}

	public Map getGateWayMap() {
		return gateWayMap;
	}

	public void setGateWayMap(Map gateWayMap) {
		this.gateWayMap = gateWayMap;
	}

    public MultiKeyMap getMessageTrxMappingMap() {
        return messageTrxMappingMap;
    }

    public void setMessageTrxMappingMap(MultiKeyMap messageTrxMappingMap) {
        this.messageTrxMappingMap = messageTrxMappingMap;
    }

	public MultiKeyMap getWasListenerMap() {
		return wasListenerMap;
	}

	public void setWasListenerMap(MultiKeyMap wasListenerMap) {
		this.wasListenerMap = wasListenerMap;
	}

	public MultiKeyMap getMultiResMessageMap() {
		return multiResMessageMap;
	}

	public void setMultiResMessageMap(MultiKeyMap multiResMessageMap) {
		this.multiResMessageMap = multiResMessageMap;
	}

}
