/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.engine;

import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.DataMap;
import nebsoa.spiderlink.context.MessageContext;
import nebsoa.spiderlink.context.MessageEngineContext;
import nebsoa.spiderlink.context.MessageHandler;
import nebsoa.spiderlink.context.Trx;
import nebsoa.spiderlink.engine.adapter.tcp.BaseOrgHandler;
import nebsoa.spiderlink.engine.adapter.tcp.BaseOrgInHandler;
import nebsoa.spiderlink.exception.MessageException;
import nebsoa.spiderlink.util.MessageManager;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 메시지 파싱, 변환, 송수신을 관장하는 엔진
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
 * $Log: MessageEngine.java,v $
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
 * Revision 1.3  2008/09/19 07:01:55  youngseokkim
 * 거래제한 관련 로직 수정
 *
 * Revision 1.2  2008/08/06 11:34:04  youngseokkim
 * doProcess() 내 Exception 처리 수정
 *
 * Revision 1.1  2008/08/04 08:54:52  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:28  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:39:11  안경아
 * *** empty log message ***
 *
 * Revision 1.23  2007/03/07 06:40:40  안경아
 * *** empty log message ***
 *
 * Revision 1.22  2007/02/07 02:25:40  김성균
 * 거래정보없을 경우 오류코드 추가
 *
 * Revision 1.21  2006/10/11 08:51:10  김성균
 * *** empty log message ***
 *
 * Revision 1.20  2006/10/03 08:47:12  이종원
 * HandlerFactory제거
 *
 * Revision 1.19  2006/10/03 06:22:41  이종원
 * 요청 전문을 처리 할 수 있는 핸들러 찾는 메소드 추가
 *
 * Revision 1.18  2006/10/03 06:21:18  이종원
 * 요청 전문을 처리 할 수 있는 핸들러 찾는 메소드 추가
 *
 * Revision 1.17  2006/10/02 23:46:05  이종원
 * *** empty log message ***
 *
 * Revision 1.16  2006/10/02 16:32:37  이종원
 * *** empty log message ***
 *
 * Revision 1.15  2006/09/26 08:45:56  김승희
 * *** empty log message ***
 *
 * Revision 1.14  2006/08/25 08:55:59  김승희
 * 기관시스템 중지여부 체크 추가
 *
 * Revision 1.13  2006/08/25 01:24:54  김승희
 * 시스템 관련 테이블 변경에 따른 수정
 *
 * Revision 1.12  2006/08/22 07:24:38  김성균
 * *** empty log message ***
 *
 * Revision 1.11  2006/07/28 12:53:37  이종원
 * *** empty log message ***
 *
 * Revision 1.10  2006/07/28 09:30:11  김승희
 * exception 처리 변경
 *
 * Revision 1.9  2006/07/27 08:02:32  김승희
 * 기관송수신프로토콜, 기관 시스템 테이블 변경에 따른 수정
 *
 * Revision 1.8  2006/07/27 06:47:53  김성균
 * 거래중지 오류코드 수정
 *
 * Revision 1.7  2006/07/04 09:31:29  김승희
 * 패키지 변경
 *
 * Revision 1.6  2006/07/04 08:39:03  김승희
 * 참조 클래스 패키지 변경
 *
 * Revision 1.5  2006/06/27 00:27:29  안경아
 * *** empty log message ***
 *
 * Revision 1.4  2006/06/21 13:48:19  김성균
 * 거래중지처리부분 추가
 *
 * Revision 1.3  2006/06/20 00:41:26  김승희
 * createContext-->getContext 호출로 변경
 *
 * Revision 1.2  2006/06/19 13:45:55  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2006/06/19 07:34:43  김승희
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public class MessageEngine {
	
	/**
	 * 비동기적으로 수행할 프로세스를 호출합니다.
	 * 
	 * 비동기적 호출이므로 리턴 값이 없습니다.
	 *
	 */
	public static void doAsyncProcess(String trxId, String orgId, String ioType, DataMap dataMap) throws MessageException {
		doProcess(trxId, orgId, ioType, dataMap);
	}
	
	/**
	 * 동기적으로 수행할 프로세스를 호출합니다.
	 * 
	 * @param messageContext 요청 데이터를 포함하는 MessageContext 객체
	 * @return 처리가 완료된 MessageContext 객체
	 * @throws MessageException
	 */
	public static DataMap doSyncProcess(String trxId, String orgId, String ioType, DataMap dataMap) throws MessageException {
		return doProcess(trxId, orgId, ioType, dataMap);
	}
	
	/**
	 * 서비스를 처리합니다.
	 * 
	 * @param messageContext 서비스에 대한 설정 및 데이터를 포함하는 ServiceContext 객체
	 * @return 서비스에 대한 실행이 완료된 ServiceContext 객체
	 * @throws Exception 
	 */
	public static DataMap doProcess(String trxId, String orgId, String ioType, DataMap dataMap) throws MessageException {
	    if (dataMap.getContext() == null) {
            throw new SysException("FRM00008", "Context 정보가 존재하지 않습니다.");
        }
		String userId = dataMap.getContext().getUserId();
		//거래중지 여부 체크
		Trx trx = checkTrxStopYn(trxId, userId);
		//기관시스템 송수신 가능 여부 체크
		checkOrgGwSystemStopYn(orgId, trx.getTrxType(), ioType, trx.getOperModeType());
		//영업일 및 거래 제공시간을 체크한다.
		MessageManager.checkTrxEnableTime(trxId);
		
		BaseOrgHandler handler = null;
		MessageContext messageContext = null;
		messageContext = getMessageContext(trxId, orgId, ioType);
		//handler = createProtocolHander(orgId, messageContext.getTrxType(), ioType);
		handler = createMessageHander(orgId, messageContext.getTrxType(), ioType, messageContext.getOperModeType());
		dataMap = handler.process(messageContext, dataMap);
		
		return dataMap;
	}
    
	/**
	 * 해당 기관으로의 전문송수신 가능여부를 체크한다.
	 * @param orgId 기관ID
	 * @param trxType 거래유형
	 * @param ioType 기동수동구분
	 * @param operModeType 운영모드구분
	 * @throws MessageException 해당 기관으로의 전문송수신이 중지 상태일 때
	 */
	private static void checkOrgGwSystemStopYn(String orgId, String trxType, String ioType, String operModeType) throws MessageException {
				
		MessageHandler messageHandler = (MessageHandler)MessageEngineContext.getContext().getMessageHandlerMap().get(orgId, trxType, ioType, operModeType);
		if(messageHandler==null){
			String errMessage = new StringBuffer("기관[").append(orgId).append("] 거래유형[")
								.append(trxType).append("] 기동수동구분[").append(ioType).append("] 운영모드구분[")
								.append(operModeType).append("]에 해당하는 전문처리핸들러 정보가 없습니다.").toString();
			throw new SysException(errMessage);
		}
		
		if("Y".equals(messageHandler.getStopYn())){
			String errMessage = new StringBuffer("해당 기관의 전문 송수신이 중지상태입니다. 다음 정보를 확인하세요(기관[").append(orgId).append("] 거래유형[")
				.append(trxType).append("] 기동수동구분[").append(ioType).append("] 운영모드구분[")
				.append(operModeType).append("])").toString();
			throw new MessageException("FRM00011", errMessage);
		}
	}
	
	/**
	 * 거래중지여부를 체크한다.
	 * @param trxId 거래ID
	 * @param userId 사용자ID
	 * @throws MessageException 
	 * @return 거래 정보
	 */
	private static Trx checkTrxStopYn(String trxId, String userId) throws MessageException {
		Trx trx = MessageEngineContext.getContext().getTrx(trxId);
        if (trx == null) {
            throw new MessageException("FRM00012", trxId + "에 해당하는 거래정보를 찾을 수 없습니다.");
        }
		String trxName = trx.getTrxName();
		boolean isTrxStop = trx.isTrxStop();
		boolean isAccessUser = trx.isAccessUser(userId);
		if (isTrxStop && !isAccessUser) {
			throw new MessageException("FRM00010", trxName + " 거래를 현재 사용할 수 없습니다.");
		}
		
		return trx;
	}

	/**
	 * MessageContext 정보를 얻어온다. 
	 * @param trxId 거래ID
	 * @param orgId 기관ID
	 * @param ioType 기동수동구분
	 * @return 전문처리를 위한 MessageContext 정보 객체
	 */
	public static MessageContext getMessageContext(String trxId, String orgId, String ioType) {
		return MessageEngineContext.getContext().getMessageContext(trxId, orgId, ioType);
	}

/*	*//**
	 * 기관ID와 기동수동구분값에 대한 프로토콜핸들러 클래스를 생성하여 리턴합니다.
	 * @param orgId 기관ID
	 * @param ioType 기동수동구분
	 * @return 프로토콜핸들러 클래스 인스턴스
	 *//*
	private static BaseOrgHandler createProtocolHander(String orgId, String trxType, String ioType) {
		String protocolHandlerClassName = MessageEngineContext.getContext().getProtocolHandlerClassName(orgId, trxType, ioType);
		
		LogManager.debug("\t##### 프로토콜핸들러 : " + protocolHandlerClassName);
		
		BaseOrgHandler handler = 
			ProtocolHandlerFactory.getInstance().getProtocolHander(protocolHandlerClassName); 
        
        if (handler == null) {
            LogManager.error("["+protocolHandlerClassName+"]의 클래스를 찾을 수 없습니다.");
            throw new SysException("FRM00020","프로토콜핸들러 클래스를 찾을 수 없습니다. [" + protocolHandlerClassName + "]");
        }
		return handler;
	}*/
	
	/**
	 * 기관ID와 기동수동구분값에 대한 전문처리핸들러 클래스를 생성하여 리턴합니다.
	 * @param orgId 기관ID
	 * @param trxType 거래유형
	 * @param ioType 기동수동구분
	 * @param operModeType 운영모드구분
	 * @return
	 */
	public static BaseOrgHandler createMessageHander(String orgId, String trxType, String ioType, String operModeType) {
		String handlerClassName = MessageEngineContext.getContext().getHandlerClassName(orgId, trxType, ioType, operModeType);
		
		LogManager.info("\t##### 전문처리핸들러 : " + handlerClassName);
		
		BaseOrgHandler handler = 
			getProtocolHander(handlerClassName); 
        
        if (handler == null) {
            LogManager.error("["+handlerClassName+"]의 클래스를 찾을 수 없습니다.");
            throw new SysException("FRM00020","프로토콜핸들러 클래스를 찾을 수 없습니다. [" + handlerClassName + "]");
        }
		return handler;
	}
    
    /**
     * BaseOrgHandler class instance를 얻어온다.
     * @param protocolHandlerClassName class full name
     * @return 해당하는 biz class instance 
     */
    private static BaseOrgHandler getProtocolHander(String protocolHandlerClassName) {
        return (BaseOrgHandler) forName(protocolHandlerClassName);
    }
    
    private static Object forName(String className){
        try {
            return  Class.forName(className).newInstance();
        } catch (InstantiationException e1) {
           throw new SysException("[CANN'T MAKE INSTANCE 생성자 체크 :"+className+"]");
        } catch (IllegalAccessException e1) {
           throw new SysException("[CANN'T MAKE INSTANCE 생성자 PUBLIC 인지 체크 :"+className+"]");
        }catch(ClassNotFoundException e){
           throw new SysException("[CLASS_NOT_FOUND:"+e.getMessage()+"]");
        }
    } 
    
    /**
     * 기관ID으로 부터 수동 전문을 받아 파싱 처리 하는 핸들러를 리턴 합니다.
     * 내부적으로 기동 수동 구분에 수동(I)값을 넣어 createMessageHander
     * 메소드를 호출 하며, 리턴 받은 객체를 BaseOrgInHandler type으로 
     * casting하여 리턴 합니다.
     * @param orgId 기관ID
     * @param trxType 거래유형
     * @param operModeType 운영모드구분
     * @return
     */
    public static BaseOrgInHandler createInMessageHander(String orgId, 
            String trxType, String operModeType) {
        
        
        BaseOrgHandler handler = 
            createMessageHander(orgId,trxType,"I",operModeType);
        
        if(!(handler instanceof BaseOrgInHandler)) {
            throw new SysException("FRM00020",
                    "OrgHandler가 BaseOrgInHandler type이 아닙니다.");   
        }
        return (BaseOrgInHandler)handler;
    }
}
