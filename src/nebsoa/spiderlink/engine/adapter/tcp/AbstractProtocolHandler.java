/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.engine.adapter.tcp;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.DataMap;
import nebsoa.common.util.StringUtil;
import nebsoa.spiderlink.context.MessageContext;
import nebsoa.spiderlink.context.MessageEngineContext;
import nebsoa.spiderlink.context.OrgSystemList;
import nebsoa.spiderlink.exception.ConnectException;

/*******************************************************************
 * <pre>
 * 1.설명 
 * ProtocolHandler 인터페이스를 상속받아 대응답처리 로직을 포함한 추상 클래스
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
 * $Log: AbstractProtocolHandler.java,v $
 * Revision 1.1  2018/01/15 03:39:50  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:52  cvs
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
 * Revision 1.1  2008/08/04 08:54:53  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:28  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:39:05  안경아
 * *** empty log message ***
 *
 * Revision 1.14  2007/02/21 02:07:05  김승희
 * keb.fwk.spiderlink.exception.ConnectException -->
 * nebsoa.spiderlink.exception.ConnectException 로 수정
 *
 * Revision 1.13  2006/09/19 06:12:17  김승희
 * 중지된 시스템이 목록에 포함되는 것에 따른 수정
 *
 * Revision 1.12  2006/08/28 01:21:45  김승희
 * 대응답처리 부분 완전 삭제
 *
 * Revision 1.11  2006/08/25 09:28:54  김승희
 * 사용가능한 시스템이 없을 때 처리
 *
 * Revision 1.10  2006/08/25 01:24:54  김승희
 * 시스템 관련 테이블 변경에 따른 수정
 *
 * Revision 1.9  2006/08/21 05:37:14  김승희
 * *** empty log message ***
 *
 * Revision 1.8  2006/08/17 12:08:53  김성균
 * Exception 관련 변경
 *
 * Revision 1.7  2006/07/31 02:48:57  김승희
 * MessageContext  인자로 받는 생성자 추가
 *
 * Revision 1.6  2006/07/04 09:05:47  김승희
 * 패키지 변경
 *
 * Revision 1.5  2006/07/04 08:44:51  김승희
 * 패키지 변경
 *
 * Revision 1.4  2006/06/20 06:20:09  김승희
 * 대응답데이터 관련 수정
 *
 * Revision 1.3  2006/06/17 09:15:10  김승희
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public abstract class AbstractProtocolHandler implements ProtocolHandler {
	
	
	protected URI uri; //URI 정보
	protected MessageContext messageContext;
	
	/**
	 * 프로토콜 핸들러가 메세지 송수신을 할 대상 시스템 정보를 갖고 있는 객체
	 */
	protected OrgSystemList.OrgSystem orgSystem;
	
	protected OrgSystemList orgSystemList;
	
	protected AbstractProtocolHandler(){}
	
	protected AbstractProtocolHandler(MessageContext messageContext){
		this.messageContext = messageContext;
		init();
		
	}
	
	public void init(){
		//추후에 주석처리하도록...
		//orgSystem = MessageEngineContext.getContext().getOrgSystemList(messageContext.getOrgId(), messageContext.getTrxType(), messageContext.getIoType(), "Q", messageContext.getOperModeType()).getNextSystem();
		
		/*Gateway gateway = (Gateway)MessageEngineContext.getContext().getGatewayInfoMap().get(messageContext.getOrgId(), messageContext.getTrxType(), messageContext.getIoType(), "Q");
		orgSystemList = (OrgSystemList)MessageEngineContext.getContext().getSystemInfoMap().get(gateway.getGwId(), messageContext.getOperModeType());
		orgSystem = orgSystemList.getNextSystem();*/
		
		orgSystemList = MessageEngineContext.getContext().getOrgSystemList(messageContext.getOrgId(), messageContext.getTrxType(), messageContext.getIoType(), messageContext.getOperModeType(), "Q");
		//사용할 수 있는 시스템이 하나도 없을 때
		if(orgSystemList==null || !orgSystemList.hasAvailSystem()){
			
			String errMessage = new StringBuffer("해당 기관의 시스템이 존재하지 않거나 모두 중지상태 입니다. 다음 정보를 확인하세요(기관[")
				.append(messageContext.getOrgId()).append("] 거래유형[")
				.append(messageContext.getTrxType()).append("] 기동수동구분[")
				.append(messageContext.getIoType()).append("] 운영모드구분[")
				.append(messageContext.getOperModeType()).append("] 요청응답구분[")
				.append("Q").append("])").toString();
			throw new ConnectException(errMessage);
		}
		//TODO 이 아래 로직은 임시임. /////////////////////////////////////////////////////////////////////////////////////
		int callCount = 0;
		do{
			orgSystem = orgSystemList.getNextSystem();
			
		}while("Y".equals(orgSystem.getStopYn()) && ++callCount < orgSystemList.size());
		
		//while 블럭을 나와서도 orgSystem.getStopYn이 Y이면  ConnectException을 던진다.
		 if("Y".equals(orgSystem.getStopYn())){
			 String errMessage = new StringBuffer("해당 기관의 시스템이 존재하지 않거나 모두 중지상태 입니다. 다음 정보를 확인하세요(기관[")
				.append(messageContext.getOrgId()).append("] 거래유형[")
				.append(messageContext.getTrxType()).append("] 기동수동구분[")
				.append(messageContext.getIoType()).append("] 운영모드구분[")
				.append(messageContext.getOperModeType()).append("] 요청응답구분[")
				.append("Q").append("])").toString();
			throw new ConnectException(errMessage);
		 }
		
		 /////////////////////////////////////////////////////////////////////////////////////////////////
		try {
			this.uri = new URI(new StringBuffer("ap://").append(orgSystem.getIp()).append(":").append(orgSystem.getPort()).toString());
		} catch (URISyntaxException e) {
			SysException ex = new SysException("올바르지 못한 URI 형식입니다.", e);
			throw ex;
		}
		
		LogManager.debug(" *************** Protocol Handler ************** ");
		LogManager.debug(" ** 기관 ID : " + messageContext.getOrgId());
		LogManager.debug(" ** 거래유형 : " + messageContext.getTrxType());
		LogManager.debug(" ** 수동기동 구분 : " + messageContext.getIoType());
		LogManager.debug(" ** 운영모드 : " + messageContext.getOperModeType());
		LogManager.debug(" ** IP: " + orgSystem.getIp());
		LogManager.debug(" ** PORT: " + orgSystem.getPort());
		LogManager.debug(" ** 쓰레드갯수 : " + orgSystem.getGateway().getThreadCount());
		LogManager.debug(" ** 속성 : " + StringUtil.NVL(orgSystem.getGateway().getGwProperties()));
		LogManager.debug(" ***************************************** ");
		
	}
	public MessageContext getMessageContext() {
		return messageContext;
	}

	public void setMessageContext(MessageContext messageContext) {
		this.messageContext = messageContext;
	}

	public void process() {
		// TODO Auto-generated method stub

	}

	public Object handleRequest() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public void handleResponse(Object resMsg) throws IOException {
		// TODO Auto-generated method stub

	}
	public void terminateProcess(){}
	
	public URI getUri() {
		return uri;
	}

	public void setUri(URI uri) {
		this.uri = uri;
	}

	public final DataMap process(DataMap dataMap){
		
		return processDataMap(dataMap);
	}
	
	protected abstract DataMap processDataMap(DataMap dataMap);

	
	public OrgSystemList.OrgSystem getOrgSystem() {
		return orgSystem;
	}

	public void setOrgSystem(OrgSystemList.OrgSystem orgSystem) {
		this.orgSystem = orgSystem;
	}

}
