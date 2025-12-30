/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.engine;

import java.util.Iterator;
import java.util.Set;

import nebsoa.common.log.LogManager;
import nebsoa.common.util.DataMap;
import nebsoa.common.util.StringUtil;
import nebsoa.spiderlink.connector.GatewayConnector;
import nebsoa.spiderlink.connector.constants.ReqResType;
import nebsoa.spiderlink.connector.constants.RunningModeType;
import nebsoa.spiderlink.context.MessageContext;
import nebsoa.spiderlink.context.MessageEngineContext;
import nebsoa.spiderlink.context.OrgSystemList;
import nebsoa.spiderlink.exception.ConnectException;

import org.apache.commons.collections.map.MultiKeyMap;

/*******************************************************************
 * <pre>
 * 1.설명 
 * GatewayConnector를 관리하고 메시지 전송을 담당하는 클래스
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
 * $Log: GatewayManager.java,v $
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
 * Revision 1.3  2008/07/09 07:52:33  김영석
 * 무조건 연결이 안된다고 에러로 마킹 할 수는 없다.
 * 현재 사용되는 소켓이 것이 하나라도 있으면 에러가 아닌것으로 처리 되도록 수정
 *
 * Revision 1.2  2008/03/04 07:32:58  김승희
 * 수동 응답 전문 전송 메소드 sendResponseMessage 수정
 *
 * Revision 1.1  2008/01/22 05:58:28  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.2  2007/12/28 05:49:02  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2007/11/26 08:39:10  안경아
 * *** empty log message ***
 *
 * Revision 1.14  2007/07/17 12:24:04  김승희
 * *** empty log message ***
 *
 * Revision 1.13  2007/07/15 13:54:35  김승희
 * ConnectException이 발생하면 장애여부를 true로 바꾼다.
 *
 * Revision 1.12  2007/05/11 07:59:32  김승희
 * closeAll 수정
 *
 * Revision 1.11  2007/03/06 06:16:25  안경아
 * *** empty log message ***
 *
 * Revision 1.10  2006/10/03 09:10:48  이종원
 * *** empty log message ***
 *
 * Revision 1.9  2006/10/03 06:45:38  김승희
 * *** empty log message ***
 *
 * Revision 1.8  2006/10/03 06:00:43  김승희
 * sendResponseMessage  추가
 *
 * Revision 1.7  2006/10/03 03:22:43  김승희
 * 로그 잘 못 찍히는 거 수정
 *
 * Revision 1.6  2006/10/02 08:01:18  김승희
 * GWConnector 시작, 중지 메소드 추가
 *
 * Revision 1.5  2006/09/29 08:01:57  김승희
 * *** empty log message ***
 *
 * Revision 1.4  2006/09/27 08:15:57  김승희
 * *** empty log message ***
 *
 * Revision 1.3  2006/09/26 08:45:56  김승희
 * *** empty log message ***
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
public class GatewayManager {

	 private static Object dummy=new Object();
	    
	 private static GatewayManager instance;
	 
	 private MultiKeyMap gatewayConnectorMap;
	 
	 /**
	 * GatewayConnector create 여부
	 */
	private boolean initialized;
	 
	 public static GatewayManager getInstance(){
	    if(instance==null){
	        synchronized (dummy) {
	            instance = new GatewayManager();
	        }
	    }
	    return instance;
	 }
	 
	 private GatewayManager(){
		 gatewayConnectorMap = new MultiKeyMap();
	 }
	 
	 GatewayConnector getGatewayConnector(String gwId, String systemId){
		 return (GatewayConnector)this.gatewayConnectorMap.get(gwId, systemId);
		 
	 }
	 
	 /**
	  * 연계 통신 모듈을 호출하여 전문을 전송한다. (기동 전문)
	  * @param messageContext MessageContext
	  * @throws Exception
	  */
	public void sendMessage(MessageContext messageContext) throws Exception{
		 messageContext.setReqResType(ReqResType.REQUEST_TYPE);
		 GatewayConnector gatewayConnector = MessageRequestDispatcher.getInstance().getGatewayConnector(messageContext);
		 //GatewayConnector의 ip, port를 세팅
		 messageContext.setIp(gatewayConnector.getIp());
		 messageContext.setPort(gatewayConnector.getPort());
		 		 
		 try{
			 gatewayConnector.handleMessage(messageContext);
		 }catch(java.net.ConnectException e){
			 //ksh: ConnectException이 발생하면 error 여부를 true로 바꾼다.
			 //gatewayConnector.setError(true); gatewayConnector 쪽에서 필요하면 에러로 마킹 해 주니까 에러로 마킹하지 말자. 
			 throw new ConnectException(MessageRequestDispatcher.getInstance().getConnectExceptionMessage(messageContext));
		 }
	 }
	
	 /**
	  * 연계 통신 모듈을 호출하여 전문을 전송한다. (수동 전문)
	  * @param messageContext MessageContext
	  * @throws Exception
	  */
	public void sendResponseMessage(MessageContext messageContext) throws Exception{
		 messageContext.setReqResType(ReqResType.RESPONSE_TYPE);
		 GatewayConnector gatewayConnector = MessageRequestDispatcher.getInstance().getResponseGatewayConnector(messageContext);
		 //GatewayConnector의 ip, port를 세팅
		 messageContext.setIp(gatewayConnector.getIp());
		 messageContext.setPort(gatewayConnector.getPort());		 		 
		 gatewayConnector.handleMessage(messageContext);
	 }
	
	/**
	 * 모든 GWConnector를 destroy합니다.
	 */
	public void closeAll(){
		LogManager.info("  ### GWConnector를 destroy합니다.. ###");
		Set gatewayKey = this.gatewayConnectorMap.keySet();
		
		GatewayConnector gwConnector;
		/*
		Iterator iter = gatewayKey.iterator();
		while(iter.hasNext()){
			
				gwConnector = (GatewayConnector)this.gatewayConnectorMap.get(iter.next());
			try{
				if(gwConnector!=null) gwConnector.destroy();
			}catch(Throwable th){
				LogManager.error(gwConnector + " destroy 중 오류가 발생하였습니다.", th);
			}
		}*/
		
		Object[] key = gatewayKey.toArray();
		if(key!=null){
			for(int i=0; i<key.length; i++){
				gwConnector = (GatewayConnector)this.gatewayConnectorMap.get(key[i]);
				try{
					if(gwConnector!=null) gwConnector.destroy();
				}catch(Throwable th){
					LogManager.error(gwConnector + " destroy 중 오류가 발생하였습니다.", th);
				}
			}
		}
		this.gatewayConnectorMap.clear();
	}
	
	 /**
	 * Gateway 테이블에 등록되어 있는 GatewayConnector를 System 갯수만큼 생성한다.
	 * 생성된 GatewayConnector는 gatewayConnectorMap으로 관리한다.
	 * gatewayConnectorMap의 key는 GW ID와 SYSTEM ID이다.
	 */
	public synchronized void createGatewayConnector(){
		
		if(this.initialized){
			 LogManager.info("이미 GatewayConnector가 생성되었습니다.");
			 return;
		 }
		 //GatewayConnector를 생성한다.
		 MultiKeyMap systemInfoMap = MessageEngineContext.getContext().getSystemInfoMap();
		 //gatewayConnectorMap을 clear한다.
		 gatewayConnectorMap.clear();
		 
		 Iterator keyIterator = systemInfoMap.keySet().iterator();
		 OrgSystemList systemList = null;
		 String gwConnectorClassName = null;
		 GatewayConnector gatewayConnector = null;
		 StringBuffer logBuf = null;
		 
		 while(keyIterator.hasNext()){
			 systemList = (OrgSystemList)systemInfoMap.get(keyIterator.next());
			 if(systemList!=null){
				 OrgSystemList.OrgSystem orgSystem = null;
				 for(int i=0, size=systemList.size(); i<size; i++){
					 if(systemList.getGateway()==null){
						 LogManager.error("해당 시스템의 Gateway 정보가 없습니다.");
						 continue;
					 }
					
					 gwConnectorClassName = systemList.getGateway().getGwAppName();
					 
					 orgSystem = (OrgSystemList.OrgSystem)systemList.get(i);
			         
					 logBuf = new StringBuffer();
					 if(!StringUtil.isNull(gwConnectorClassName)){
						 try {
							 //GatewayConnector 정보
							 logBuf.append("** GatewayConnector **").append("\r\n")
								.append(" GW ID : ").append(systemList.getGateway().getGwId()).append("\r\n")
								.append(" 운영모드구분  : ").append(orgSystem.getOperModeType()).append("\r\n")
								.append(" APP NAME : ").append(gwConnectorClassName).append("\r\n")
								.append(" IP : ").append(orgSystem.getIp()).append("\r\n")
								.append(" PORT : ").append(orgSystem.getPort()).append("\r\n")
								.append(" 중지여부 : ").append(orgSystem.getStopYn()).append("\r\n")
								.append(" 속성 : ").append(systemList.getGateway().getGwProperties());
							 							 
							 gatewayConnector = (GatewayConnector)(Class.forName(gwConnectorClassName).newInstance());
							 
							 //생성한 GatewayConnector는 Map으로 관리한다.
							 this.gatewayConnectorMap.put(systemList.getGateway().getGwId(), orgSystem.getSystemId(), gatewayConnector);
							
							 if(systemList.getGateway().isMyConnector(systemList.getGateway(), orgSystem.getSystemId())){
								 gatewayConnector.initialize(systemList.getGateway(), 
										RunningModeType.getType(orgSystem.getOperModeType()),
										orgSystem.getIp(),
										Integer.parseInt(orgSystem.getPort()),
										"N".equals(orgSystem.getStopYn())?false:true);
								 
								 //System ID를 추가로 담는다.(2008.03.03 by ksh)
							 	 gatewayConnector.setSystemId(orgSystem.getSystemId());
							 
								 LogManager.info(gwConnectorClassName + "생성 성공\r\n" + logBuf.toString());
							 }
  					    } catch (Throwable e) {
							 //e.printStackTrace();
							 LogManager.error("다음 GatewayConnector 생성 실패\r\n" + logBuf.toString() , e);
						}
					 }//end of if
				 }//end of for
			 }//end of if(systemList!=null)
		 }//end of while
		 
		 this.initialized = true;
	 }
	
	
    /** 
	 * 특정 시스템의 GWConnector를 stop시킨다
	 * @param gwId GW ID
	 * @param systemId 시스템 ID
	 */
	public void stopGWConnector(String gwId, String systemId){
		 GatewayConnector gatewayConnector = (GatewayConnector)this.gatewayConnectorMap.get(gwId, systemId);
		 if(gatewayConnector!=null){
			 gatewayConnector.setStopped(true);
		 }
	 }
	 
	/** 
	 * 특정 시스템의 GWConnector를 start시킨다
	 * @param gwId GW ID
	 * @param systemId 시스템 ID
	 */
	 public void startGWConnector(String gwId, String systemId){
		 GatewayConnector gatewayConnector = (GatewayConnector)this.gatewayConnectorMap.get(gwId, systemId);
		 if(gatewayConnector!=null){
			 gatewayConnector.setStopped(false);
		 }
	 }
	 
   /**
    * ManagementObject에서 GWConnector의 중지여부를 컨트롤할 때 사용하는 메소드
    * stopYn이 Y이면 stop, stopYn이 N이면 start시킨다..
	* @param dataMap
	*/
	public static void controlGWConnector(DataMap dataMap){
		 String gwId = dataMap.getString("gwId");
		 String systemId = dataMap.getString("systemId");
		 String stopYn = dataMap.getString("stopYn");
		 GatewayManager gatewayManager = GatewayManager.getInstance();
		 if(stopYn.equals("Y")){
			 gatewayManager.stopGWConnector(gwId, systemId);
		 }else{
			 gatewayManager.startGWConnector(gwId, systemId);
		 }
	 }
}
