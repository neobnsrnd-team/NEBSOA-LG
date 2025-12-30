/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.service.engine;

import nebsoa.common.util.DataMap;
import nebsoa.service.context.ServiceContext;
import nebsoa.service.exception.ServiceException;
import nebsoa.service.exception.ServiceSysException;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 서비스 요청에 대한 처리를 수행하는 클래스입니다.
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
 * $Log: BaseServiceHandler.java,v $
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
 * Revision 1.6  2008/03/03 04:25:09  김성균
 * 예외처리 변경
 *
 * Revision 1.5  2008/02/15 04:45:15  김성균
 * 서비스 수행 로그 메시지 정리
 *
 * Revision 1.4  2008/02/14 09:27:30  김성균
 * 예외처리 및 프로파일관련 수정
 *
 * Revision 1.3  2008/02/12 11:59:31  이종원
 * 로그 수정
 *
 * Revision 1.2  2008/01/24 05:35:04  최수종
 * Exception 처리 수정
 *
 * Revision 1.1  2008/01/22 05:58:33  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.4  2008/01/08 02:27:05  최수종
 * *** empty log message ***
 *
 * Revision 1.9  2008/01/07 01:44:29  김성균
 * 표준전문 관련 변수 및 로그 삭제
 *
 * Revision 1.8  2008/01/04 02:49:57  김성균
 * 입력값 검증 및 디폴트값 셋팅 옵션널하게 변경
 *
 * Revision 1.7  2007/12/28 05:45:09  최수종
 * *** empty log message ***
 *
 * Revision 1.6  2007/12/27 03:29:07  최수종
 * *** empty log message ***
 *
 * Revision 1.5  2007/12/27 02:51:18  최수종
 * *** empty log message ***
 *
 * Revision 1.4  2007/12/26 07:31:55  최수종
 * *** empty log message ***
 *
 * Revision 1.3  2007/12/24 09:21:05  최수종
 * *** empty log message ***
 *
 * Revision 1.2  2007/12/24 08:50:18  김성균
 * marshall, unmarshall 부분 추가
 *
 * Revision 1.1  2007/12/24 08:22:37  최수종
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public abstract class BaseServiceHandler extends ServiceHandler {
	
	/**
     * 서비스 전처리
     * 
	 * @param serviceContext
	 * @param dataMap
	 * @return dataMap
	 * @throws ServiceException  
     */
	public DataMap doPreProcess(ServiceContext serviceContext, DataMap dataMap) 
		throws ServiceException {
		
        return dataMap;
	}

	/**
	 * 서비스 후처리
	 * 
	 * @param serviceContext
	 * @param dataMap
	 * @return dataMap
	 * @throws ServiceException  
	 */
	public DataMap doPostProcess(ServiceContext serviceContext, DataMap dataMap) 
		throws ServiceException {
	
        return dataMap;
	}
	
	/**
	 * 서비스 처리
	 * 
	 * @param serviceContext
	 * @param dataMap
	 * @return dataMap
	 * @throws ServiceException 
	 */
	public DataMap doProcess(ServiceContext serviceContext, DataMap dataMap) 
		throws ServiceException {
    
		try {
			startProfile(dataMap.getContext(), "입력필드_체크");
			dataMap = marshall(serviceContext, dataMap);
		} catch (Throwable e) {
			throw new ServiceSysException(e);
		} finally {
			endProfile(dataMap.getContext());
		}
		
		try {
			startProfile(dataMap.getContext(), "서비스_컴포넌트 수행");
			
			//대응답여부가 Y이면
//			if(isProxy(serviceContext)){
//				LogManager.info("대응답데이터 처리를 시작합니다." );
//				dataMap.put(MessageConstants.MESSAGE_RECEIVED, getResponseMessage(serviceContext, dataMap));
//				
//			}else{
//				dataMap = sendReceiveService(serviceContext, dataMap);
//			}
			
			dataMap = invokeServiceComponentList(serviceContext, dataMap);
		} finally {
			endProfile(dataMap.getContext());
		}
		
		try {
			startProfile(dataMap.getContext(), "출력필드_체크");
			dataMap = unmarshall(serviceContext, dataMap);
		} finally {
			endProfile(dataMap.getContext());
		}

		return dataMap;
	}
	
	/**
	 * 각 핸들러별로 구현해야할 메소드로써, 데이타를 marshall처리시에 사용한다.
	 * @param serviceContext
	 * @param dataMap
	 * @return dataMap
	 * @throws ServiceException
	 */
	protected abstract DataMap marshall(ServiceContext serviceContext, DataMap dataMap);
	
	/**
	 * 각 핸들러별로 구현해야할 메소드로써, 서비스 처리시에 사용한다.
	 * @param serviceContext
	 * @param dataMap
	 * @return dataMap
	 * @throws ServiceException
	 */
	protected abstract DataMap invokeServiceComponentList(ServiceContext serviceContext, DataMap dataMap) 
		throws ServiceException;

	/**
	 * 각 핸들러별로 구현해야할 메소드로써, 데이타를 unmarshall처리시에 사용한다.
	 * @param serviceContext
	 * @param dataMap
	 * @return dataMap
	 * @throws ServiceException
	 */
	protected abstract DataMap unmarshall(ServiceContext serviceContext, DataMap dataMap);
    
}
