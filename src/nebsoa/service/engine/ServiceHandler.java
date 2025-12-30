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
import nebsoa.common.log.LogManager;
import nebsoa.common.util.DataMap;
import nebsoa.service.context.ServiceContext;
import nebsoa.service.exception.ServiceException;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 서비스 요청에 대한 처리를 수행하는 최상위 클래스입니다.
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
 * $Log: ServiceHandler.java,v $
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
 * Revision 1.4  2008/02/15 04:45:15  김성균
 * 서비스 수행 로그 메시지 정리
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
 * Revision 1.6  2007/12/31 02:50:22  최수종
 * 주석 수정
 *
 * Revision 1.5  2007/12/28 07:11:51  김성균
 * nebsoa.service.info 패키지 변경에 의한 수정
 *
 * Revision 1.4  2007/12/27 03:29:07  최수종
 * *** empty log message ***
 *
 * Revision 1.3  2007/12/26 09:21:52  최수종
 * *** empty log message ***
 *
 * Revision 1.2  2007/12/26 07:31:55  최수종
 * *** empty log message ***
 *
 * Revision 1.1  2007/12/24 08:02:22  최수종
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public abstract class ServiceHandler {
	
	/**
     * 서비스 수행
	 * 
	 * @param serviceContext
	 * @param dataMap
	 * @return dataMap
	 * @throws ServiceException 
	 */
    public DataMap process(ServiceContext serviceContext, DataMap dataMap) 
		throws ServiceException {
    	
    	LogManager.info(" ****************** SERVICE PROCESS START ******************** ");
        LogManager.info(" *** 서비스 ID : " + serviceContext.getServiceId());
        LogManager.info(" ************************************************************* ");
	
		startProfile(dataMap.getContext(), "서비스_" + serviceContext.getServiceId()+" 처리");
		
		dataMap = doPreProcess(serviceContext, dataMap);
		try {
			dataMap = doProcess(serviceContext, dataMap);
		} finally {
			dataMap = doPostProcess(serviceContext, dataMap);
			endProfile(dataMap.getContext());
		}
		
		LogManager.info(" ****************** SERVICE PROCESS END ******************** ");
		
		return dataMap;
    }

	/**
	 * Profiler 시작
	 * @param context
	 * @param eventMsg
	 */
	protected void startProfile(Context context, String eventMsg) {
        if (context != null && context.getProfiler() != null) {
            context.getProfiler().startEvent(eventMsg);
        }
	}
	
	/**
	 * Profiler 종료
	 * @param dataMap
	 */
	protected void endProfile(Context context) {
		if (context != null && context.getProfiler()!= null) {
		    context.getProfiler().stopEvent();
		}
	}
    
    /**
     * 각 핸들러별로 구현해야할 메소드로써, 서비스 전처리 로직을 구현한다.
     * 
     * @param serviceContext
     * @param dataMap
     * @return dataMap
     * @throws ServiceException
     */
    protected abstract DataMap doPreProcess(ServiceContext serviceContext, DataMap dataMap) 
		throws ServiceException;

	/**
	 * 각 핸들러별로 구현해야할 메소드로써, 서비스 후처리 로직을 구현한다.
	 * 
	 * @param serviceContext
	 * @param dataMap
	 * @return dataMap
	 * @throws ServiceException
	 */
    protected abstract DataMap doPostProcess(ServiceContext serviceContext, DataMap dataMap) 
		throws ServiceException;
	
	
    /**
     * 각 핸들러별로 구현해야할 메소드로써, 서비스 처리 로직을 구현한다.
     * 
     * @param serviceContext
     * @param dataMap
     * @return dataMap
     * @throws ServiceException
     */
    protected abstract DataMap doProcess(ServiceContext serviceContext, DataMap dataMap) 
		throws ServiceException;
}
