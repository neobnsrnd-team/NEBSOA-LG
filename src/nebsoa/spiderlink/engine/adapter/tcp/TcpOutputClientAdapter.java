/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.engine.adapter.tcp;

import java.net.URI;
import java.net.URISyntaxException;

import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.DataMap;
import nebsoa.common.util.StringUtil;
import nebsoa.spiderlink.context.MessageContext;
import nebsoa.spiderlink.context.MessageEngineContext;
import nebsoa.spiderlink.context.OrgSystemList;
import nebsoa.spiderlink.engine.adapter.AbstractAdapter;

/*******************************************************************
 * <pre>
 * 1.설명 
 * TCP/IP 통신을 위한 outgoing 클라이언트 형태의 Adapter 클래스
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
 * $Log: TcpOutputClientAdapter.java,v $
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
 * Revision 1.16  2006/08/25 01:24:54  김승희
 * 시스템 관련 테이블 변경에 따른 수정
 *
 * Revision 1.15  2006/08/01 00:30:09  김승희
 * Lifecycle 패키지 변경
 *
 * Revision 1.14  2006/07/27 08:02:32  김승희
 * 기관송수신프로토콜, 기관 시스템 테이블 변경에 따른 수정
 *
 * Revision 1.13  2006/07/25 01:39:31  김승희
 * 주석 정리
 *
 * Revision 1.12  2006/07/03 12:36:15  김승희
 * *** empty log message ***
 *
 * Revision 1.11  2006/06/20 08:53:34  김승희
 * 멤버변수 정리
 *
 * Revision 1.10  2006/06/20 08:46:30  김승희
 * 기관 시스템 정보 관련 수정
 *
 * Revision 1.9  2006/06/20 06:20:09  김승희
 * 대응답데이터 관련 수정
 *
 * Revision 1.8  2006/06/17 09:15:10  김승희
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public class TcpOutputClientAdapter extends AbstractAdapter {
	
	private String handlerClassName;
	
	private MessageContext messageContext;
	
	/**
	 * 프로토콜 핸들러 클래스
	 */
	private Class handlerClass;
	
    public TcpOutputClientAdapter(MessageContext messageContext) {
		
		this.messageContext = messageContext;
    }
    
	/**
	 * 프로토콜 핸들러
	 */
	private AbstractProtocolHandler protocolHandler;
	

	public void init() throws SysException{
		
		try {
			this.handlerClass = Class.forName(this.handlerClassName);
			
			this.protocolHandler = (AbstractProtocolHandler)this.handlerClass.newInstance();
			
			OrgSystemList.OrgSystem orgSystem = MessageEngineContext.getContext().getOrgSystemList(messageContext.getOrgId(), messageContext.getTrxType(), messageContext.getIoType(),  messageContext.getOperModeType(), "Q").getNextSystem();
			URI uri = new URI("ap://" + orgSystem.getIp() +":" + orgSystem.getPort());
			
			LogManager.debug(" *************** TCP CLIENT ************** ");
			LogManager.debug(" ** 기관 ID : " + messageContext.getOrgId());
			LogManager.debug(" ** 거래유형 : " + messageContext.getTrxType());
			LogManager.debug(" ** 수동기동 구분 : " + messageContext.getIoType());
			LogManager.debug(" ** 운영모드 : " + messageContext.getOperModeType());
			LogManager.debug(" ** URI : " + uri);
			LogManager.debug(" ** 쓰레드갯수 : " + orgSystem.getGateway().getThreadCount());
			LogManager.debug(" ** 속성 : " + StringUtil.NVL(orgSystem.getGateway().getGwProperties()));
			LogManager.debug(" ***************************************** ");
						
			protocolHandler.setUri(uri);
			protocolHandler.setMessageContext(this.messageContext);
			
		} catch (ClassNotFoundException e) {
			LogManager.error("프로토콜 핸들러 클래스를 찾을 수 없습니다.", e);
			throw new SysException("프로토콜 핸들러 클래스를 찾을 수 없습니다."+e.getMessage());
		
		} catch (InstantiationException e) {
			e.printStackTrace();
			throw new SysException(e.getMessage());
			
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new SysException(e.getMessage());
			
		} catch(URISyntaxException e) {
            LogManager.error("연결할 URI[" + "ap://localhost:27101" + "] 문법 에러", e);
            throw new SysException(e.getMessage());
        }
		
	}//end of init()

	/**
	 * 서비스를 시작합니다.
	 * 
	 * @see nebsoa.spiderlink.engine.Lifecycle#service()
	 */
	public void service() {
		LogManager.debug("등록된 핸들러를 실행 합니다.");
		this.protocolHandler.process();
	}//end of service()
	
	/**
	 * 서비스를 시작하고 처리한 결과를 리턴합니다.
	 * 
	 * @param dataMap
	 */
	public DataMap service(DataMap dataMap){
		LogManager.debug("등록된 핸들러를 실행 합니다.");
		return this.protocolHandler.process(dataMap);
	}//end of service()

	/**
	 * 자원을 반납하고 종료화 작업을 진행합니다.
	 * 
	 * @see nebsoa.spiderlink.engine.Lifecycle#destroy()
	 */
	public void destroy() {
		
		this.protocolHandler.terminateProcess();
		
		LogManager.debug("\n\n=============== ADAPTER-INFO ===============");
		LogManager.debug("TCP_OUTPUT_CLIENT_ADAPTER 가 성공적으로 종료되었습니다.");
		LogManager.debug("=============== ADAPTER-INFO ===============\n\n");
	}//end of destroy()

	/**
	 * handlerClassName 의 값을 리턴합니다.
	 * 
	 * @return handlerClassName 의 값
	 */
	public String getHandlerClassName() {
		return handlerClassName;
	}

	/**
	 * handlerClassName 에 값을 세팅합니다.
	 * 
	 * @param handlerClassName handlerClassName 에 값을 세팅하기 위한 인자 값
	 */
	public void setHandlerClassName(String handlerClassName) {
		this.handlerClassName = handlerClassName;
	}

}//end of TcpOutputClientAdapter.java