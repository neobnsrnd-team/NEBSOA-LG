/*
 * Nebsoa Framework
 * 
 * Copyright (c) 2008-2009 IBK Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 IBK 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.log;

import nebsoa.common.exception.SysException;
import nebsoa.common.util.DataMap;
import nebsoa.common.util.PropertyManager;
import nebsoa.service.context.ServiceContext;
import nebsoa.spiderlink.context.MessageContext;

/*******************************************************************
 * <pre>
 * 1.설명 
 * MessageLog를 관리하는 클래스입니다.
 * 
 * 2.사용법
 * 
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author : 김은정 
 * @version
 * @작성 일자 : 2008. 05. 06
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 *
 * $Log: MessageLoggerManager.java,v $
 * Revision 1.1  2018/01/15 03:39:48  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:19  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:50  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:23  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:26  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:51  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.3  2008/05/15 02:48:09  김은정
 * *** empty log message ***
 *
 * Revision 1.2  2008/05/08 10:16:41  김은정
 * *** empty log message ***
 *
 * Revision 1.1  2008/05/07 06:20:34  김은정
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class MessageLoggerManager{

	private static Object dummy = new Object();

	private static MessageLoggerManager instance;

	/**
	 * 로그 정보를 로딩하는 delegator
	 */
	private static MessageLogger messageLoggerDelegator;


	/**
	 * 싱글톤 처리
	 */
	private MessageLoggerManager() {
		String className = null; 
		try {
			className = PropertyManager.getProperty("default","MESSAGE_LOGGER_DELEGATOR_CLASS_NAME", "nebsoa.common.log.DefaultMessageLogger");
			messageLoggerDelegator = (MessageLogger) Class.forName(className).newInstance();
		} catch (InstantiationException e1) {
			throw new SysException("[CANN'T MAKE INSTANCE 생성자 체크 :"+className+"]");
		} catch (IllegalAccessException e1) {
			throw new SysException("[CANN'T MAKE INSTANCE 생성자 PUBLIC 인지 체크 :"+className+"]");
		}catch(ClassNotFoundException e){
			throw new SysException("[CLASS_NOT_FOUND:"+e.getMessage()+"]");
		}
	}

	/**
	 * 싱글톤 인스턴스를 생성한다.
	 * @return
	 */
	public static MessageLoggerManager getInstance() {
		synchronized (dummy) {
			if (instance == null){
				instance = new MessageLoggerManager();
			}
		}
		return instance;
	}

	/**
	 * MessageLog를 File로 남긴다. 
	 * @return
	 */
	public void serviceReqLog(ServiceContext serviceContext, DataMap dataMap){
		messageLoggerDelegator.serviceReqLog(serviceContext, dataMap);
	}
	public void serviceResLog(ServiceContext serviceContext, DataMap dataMap){
		messageLoggerDelegator.serviceResLog(serviceContext, dataMap);
	}
    public void messageLog(MessageContext messageContext, DataMap dataMap, byte[] messageBytes, String reqResType, String resultCode, DataMap argMap){
    	messageLoggerDelegator.messageLog(messageContext, dataMap, messageBytes, reqResType, resultCode, argMap);
    }
    public void messageLog(MessageContext messageContext, DataMap dataMap, byte[] messageBytes, String reqResType, String resultCode, String channelGubun){
    	messageLoggerDelegator.messageLog(messageContext, dataMap, messageBytes, reqResType, resultCode, channelGubun);
    }
	


}
