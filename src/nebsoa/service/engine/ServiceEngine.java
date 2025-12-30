/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.service.engine;

import nebsoa.common.Context;
import nebsoa.common.log.MessageLoggerManager;
import nebsoa.common.util.DataMap;
import nebsoa.service.context.ServiceContext;
import nebsoa.service.exception.ServiceException;
import nebsoa.service.exception.ServiceSysException;
import nebsoa.service.util.ServiceManager;

/*******************************************************************
 * <pre>
 * 1.설명 
 * ServiceEngine 클래스입니다.
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
 * $Log: ServiceEngine.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:23  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:26  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:29  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.4  2008/05/07 06:29:23  김은정
 * MessageLog 남기는 로직 추가
 *
 * Revision 1.3  2008/02/14 09:27:30  김성균
 * 예외처리 및 프로파일관련 수정
 *
 * Revision 1.2  2008/01/24 05:35:04  최수종
 * Exception 처리 수정
 *
 * Revision 1.1  2008/01/22 05:58:33  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/12/31 04:35:34  최수종
 * nebsoa.service 패키지 추가
 *
 * Revision 1.12  2007/12/31 03:01:08  김성균
 * 서비스ID 인자로 추가
 *
 * Revision 1.11  2007/12/31 02:50:22  최수종
 * 주석 수정
 *
 * Revision 1.10  2007/12/28 08:19:01  최수종
 * 사용하지 않는 import제거
 *
 * Revision 1.9  2007/12/28 05:45:09  최수종
 * *** empty log message ***
 *
 * Revision 1.8  2007/12/27 03:29:07  최수종
 * *** empty log message ***
 *
 * Revision 1.7  2007/12/26 07:31:55  최수종
 * *** empty log message ***
 *
 * Revision 1.6  2007/12/26 02:00:02  최수종
 * *** empty log message ***
 *
 * Revision 1.5  2007/12/24 08:02:22  최수종
 * *** empty log message ***
 *
 * Revision 1.4  2007/12/24 07:40:59  최수종
 * *** empty log message ***
 *
 * Revision 1.2  2007/12/21 05:06:27  김성균
 * serviceId : 메소드 인자에서 제외
 *
 * Revision 1.1  2007/12/18 03:16:00  김성균
 * 서비스관련 패키지 최초등록
 *
 * </pre>
 ******************************************************************/
public class ServiceEngine {

	/**
	 * 서비스 비동기적 호출
	 * @param serviceId
	 * @param dataMap
	 * @throws ServiceException
	 */
	public static void doAsyncProcess(String serviceId, DataMap dataMap) throws ServiceException {

		doProcess(serviceId, dataMap);
	}

	/**
	 * 서비스 동기적 호출
	 * @param serviceId
	 * @param dataMap
	 * @return dataMap 
	 * @throws ServiceException
	 */
	public static DataMap doSyncProcess(String serviceId, DataMap dataMap) throws ServiceException {

		return doProcess(serviceId, dataMap);
	}

	/**
	 * 서비스 처리 진행
	 * @param serviceId
	 * @param dataMap
	 * @return dataMap
	 * @throws ServiceException
	 */
	public static DataMap doProcess(String serviceId, DataMap dataMap) throws ServiceException {
		Context conetxt = dataMap.getContext();

		if (conetxt == null) {
			new ServiceSysException("Context 정보가 존재하지 않습니다.");
		}

		ServiceContext serviceContext = ServiceManager.createServiceContext(serviceId);
		ServiceHandler serviceHandler = createServiceHandler();

		//송신 전문 로그 남긴다.
		MessageLoggerManager.getInstance().serviceReqLog(serviceContext, dataMap);

		dataMap = serviceHandler.process(serviceContext, dataMap);

		//수신 전문 로그 남긴다.
		MessageLoggerManager.getInstance().serviceResLog(serviceContext, dataMap);

		return dataMap;
	}

	/**
	 * 서비스 핸들러 생성
	 * 
	 * @return ServiceHandler
	 */
	public static ServiceHandler createServiceHandler() {

		ServiceHandler serviceHandler = new DefaultServiceHandler();
		return serviceHandler;
	}      

}
