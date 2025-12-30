/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.engine;

import nebsoa.common.log.LogManager;
import nebsoa.common.util.StringUtil;
import nebsoa.spiderlink.connector.GatewayConnector;
import nebsoa.spiderlink.context.GatewaySystem;
import nebsoa.spiderlink.context.MessageContext;
import nebsoa.spiderlink.context.MessageEngineContext;
import nebsoa.spiderlink.context.OrgSystemList;
import nebsoa.spiderlink.exception.ConnectException;
import nebsoa.spiderlink.exception.MessageSysException;

/*******************************************************************
 * <pre>
 * 1.설명 
 * MessageContext 정보에 따라 사용할 수 있는 시스템의 GatewayConnector를 리턴하는 클래스
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
 * $Log: MessageRequestDispatcher.java,v $
 * Revision 1.1  2018/01/15 03:39:50  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:45  cvs
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
 * Revision 1.3  2008/07/10 06:30:05  김승희
 * OrgSystem 획득 시도가 1이상일 경우 마지막으로 획득한 OrgSystem의 다음 인덱스의 OrgSystem을 리턴하도록 수정함
 *
 * Revision 1.2  2008/03/04 07:33:33  김승희
 * 수동 응답 커넥터 구하는 메소드 추가
 *
 * Revision 1.1  2008/01/22 05:58:28  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:39:11  안경아
 * *** empty log message ***
 *
 * Revision 1.12  2007/07/17 12:24:04  김승희
 * *** empty log message ***
 *
 * Revision 1.11  2007/03/07 06:40:40  안경아
 * *** empty log message ***
 *
 * Revision 1.10  2007/02/21 02:07:05  김승희
 * keb.fwk.spiderlink.exception.ConnectException -->
 * nebsoa.spiderlink.exception.ConnectException 로 수정
 *
 * Revision 1.9  2006/10/13 11:58:50  이종원
 * timeout 설정부분을 이미 설정 된 값이 있으면 설정 안함.
 *
 * Revision 1.8  2006/10/10 12:17:24  안경아
 * *** empty log message ***
 *
 * Revision 1.7  2006/09/28 02:19:00  김승희
 * *** empty log message ***
 *
 * Revision 1.6  2006/09/27 12:05:45  김승희
 * *** empty log message ***
 *
 * Revision 1.5  2006/09/27 11:06:50  김승희
 * *** empty log message ***
 *
 * Revision 1.4  2006/09/26 08:45:56  김승희
 * *** empty log message ***
 *
 * Revision 1.3  2006/09/19 06:12:06  김승희
 * 장애 판단 부분 수정
 *
 * Revision 1.2  2006/09/18 11:12:11  김승희
 * *** empty log message ***
 *
 * Revision 1.1  2006/09/18 07:24:13  김승희
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public class MessageRequestDispatcher {
	
	 private static Object dummy=new Object();
	    
	 private static MessageRequestDispatcher instance;
	 
	 public static MessageRequestDispatcher getInstance(){
	    if(instance==null){
	        synchronized (dummy) {
	            instance = new MessageRequestDispatcher();
	        }
	    }
	    return instance;
	 }
	 
	 private MessageRequestDispatcher(){}
	 
	 public GatewayConnector getGatewayConnector(MessageContext messageContext){
		 
		 OrgSystemList orgSystemList = MessageEngineContext.getContext().getOrgSystemList(messageContext.getOrgId(), messageContext.getTrxType(), messageContext.getIoType(), 
				 	messageContext.getOperModeType(), messageContext.getReqResType());
		 //사용할 수 있는 시스템이 하나도 없을 때
		 if(orgSystemList==null || !orgSystemList.hasAvailSystem()){
			throw new ConnectException(getConnectExceptionMessage(messageContext));
		 }
		 
		 //timeout 셋팅
		 try{
			 String timeoutStr = orgSystemList.getGateway().getProperty("timeout");
			 if(!StringUtil.isNull(timeoutStr) 
                     && messageContext.getDefaultTimeout()==0){ // 세팅한 값이 없으면 시스템  설정 값을 적용 
				 messageContext.setTimeout(Integer.parseInt(timeoutStr));
             }
		 }catch(Throwable th){}
		 
		 //gatewayConnector의 isError와 isStrop이 false일 때 까지 다음 시스템의 gatewayConnector 획득을 반복해서 시도한다.
		 //최대 시도 횟수는 시스템의 총 갯수이다.
		 int tryCount = 0; 
		 GatewayConnector gatewayConnector = null;
		 OrgSystemList.OrgSystem orgSystem = null;
		 
		 do{
			//orgSystem = orgSystemList.getNextSystem();
			 
			//시도횟수가 1이상일 때는 이전에 얻은 orgSystem 인덱스의 다음 인덱스에 해당 하는 orgSystem을 얻도록 수정하였다.
			//이런 처리가 없다면 멀티 쓰레드에 의해서 orgSystemList의 nextIndex 값이 변할 수가 있으므로 시스템 리스트 중
			//하나가 정상, 나머지가 장애인 경우 시스템 리스트 만큼 재시도 하더라도 정상 시스템을 얻지 못할 확율이 발생한다. 
			if(tryCount==0 || orgSystem==null) orgSystem = orgSystemList.getNextSystem();
			else orgSystem = orgSystemList.getNextSystem(orgSystem.getIndex());

			gatewayConnector = GatewayManager.getInstance().getGatewayConnector(
								orgSystemList.getGateway().getGwId(), orgSystem.getSystemId());
			tryCount++;
			LogManager.info(" ## " + orgSystem.getIp() +":" + orgSystem.getPort() +"-" + orgSystem.getSystemId() + "로 접속을 시도합니다. 시도횟수 : " + tryCount);
			if(gatewayConnector!=null) 
			{
				LogManager.info(" ## gatewayConnector : "+gatewayConnector.toString());
				LogManager.info(" ## isError :"+gatewayConnector.isError() +", isStopped: " + gatewayConnector.isStopped());
			}
		 }while( (gatewayConnector==null || gatewayConnector.isError() || gatewayConnector.isStopped()) && tryCount < orgSystemList.size());
			
		 //while 블럭을 나와서도 gatewayConnector가 유효하지 못하면  ConnectException을 던진다.
		 if(gatewayConnector!=null && !gatewayConnector.isError() && !gatewayConnector.isStopped()){
			return gatewayConnector;
		 }else{
			throw new ConnectException(getConnectExceptionMessage(messageContext));
		 }
	 }
	 
	 /**
	  * 응답 gatewayConnector를 구하여 리턴한다.
	  * @param messageContext
	  * @return GatewayConnector
	  */
	 public GatewayConnector getResponseGatewayConnector(MessageContext messageContext){
		String identifier = messageContext.getListenerConnectorMappingIndentifier();
		String listenerGWId = messageContext.getGatewayId();
		String listenerSystemId = messageContext.getSystemId();
		
		return getResponseGatewayConnector(listenerGWId, listenerSystemId, identifier);
	 }
	 
	 /**
	  * 응답 gatewayConnector를 구하여 리턴한다.
	  * @param listenerGWId 리스너 Gateway ID
	  * @param listenerSystemId 리스너 Gateway System ID
	  * @param identifier 맵핑 식별자
	  * @return GatewayConnector
	  */
	public GatewayConnector getResponseGatewayConnector(String listenerGWId, String listenerSystemId, String identifier){
		GatewaySystem responseGatewaySystem = MessageEngineContext.getContext().getResponseGatewaySystem(listenerGWId, listenerSystemId, identifier);
		
		//TODO 어떤 Exception 으로 던져야 하는지 차후 더 고민..
		if(responseGatewaySystem==null) 
			throw new MessageSysException("리스너 Gateway ID:" + listenerGWId +", 리스너 System ID:" + listenerSystemId +", 식별자:" + identifier +"에 해당하는 responseGatewaySystem이 존재하지 않습니다.");
			
		GatewayConnector gatewayConnector = GatewayManager.getInstance().getGatewayConnector(
				responseGatewaySystem.getGwId(), responseGatewaySystem.getSystemId());
			
		if(gatewayConnector!=null && !gatewayConnector.isError() && !gatewayConnector.isStopped()){
			return gatewayConnector;
		}else{
			throw new ConnectException("Gateway[" + responseGatewaySystem.getGwId() +"]의  System[" + responseGatewaySystem.getSystemId()
					+"-" + responseGatewaySystem.getIp() +":" + responseGatewaySystem.getPort() +"로 현재 접속 할 수 없습니다.(중지 상태  또는 장애 발생)"
			);
		}
		
	 }
	 
	 public String getConnectExceptionMessage(MessageContext messageContext){
		 return new StringBuffer("해당 Gateway의 시스템이 존재하지 않거나 모두 중지 또는 장애 상태 입니다. 다음 정보를 확인하세요(기관[")
			.append(messageContext.getOrgId()).append("] 거래유형[")
			.append(messageContext.getTrxType()).append("] 기동수동구분[")
			.append(messageContext.getIoType()).append("] 운영모드구분[")
			.append(messageContext.getOperModeType()).append("] 요청응답구분[")
			.append(messageContext.getReqResType()).append("])").toString();
	 }
	 
}
