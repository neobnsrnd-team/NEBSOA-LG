/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.util;

import nebsoa.common.jdbc.DBManager;
import nebsoa.common.jdbc.DBResultSet;
import nebsoa.service.context.ServiceEngineContext;
import nebsoa.service.exception.ServiceErrorCode;
import nebsoa.service.exception.ServiceException;
import nebsoa.service.exception.ServiceNotFoundException;
import nebsoa.service.info.Service;
import nebsoa.spiderlink.exception.MessageException;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 공휴일 정보를 로딩하는 클래스 
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
 * $Log: BizDayChecker.java,v $
 * Revision 1.1  2018/01/15 03:39:49  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:31  cvs
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
 * Revision 1.2  2008/09/19 07:00:03  youngseokkim
 * 거래제한 로직 추가
 *
 * Revision 1.1  2008/08/04 08:54:50  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.2  2008/05/19 04:45:22  김영석
 * 서비스 옵션 체크 로직 변경
 *
 * Revision 1.1  2008/01/22 05:58:18  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2008/01/04 02:34:41  김성균
 * 최초 등록
 *
 * </pre>
 ******************************************************************/
public class BizDayChecker implements BizDayCheckable {
    
    private static final String FIND_HOLIDAY = "SELECT 휴일일 FROM 공휴일 WHERE 휴일일 = ?";

    private boolean isServiceEnableTime = true;
    private boolean isTrxEnableTime = true;
    private boolean isHoliday = false;

    public boolean isServiceEnableTime(String serviceId) throws ServiceException {

    	String today = FormatUtil.getToday("yyyyMMdd");
        Object[] params = { today };
        DBResultSet rs = DBManager.executePreparedQuery(FIND_HOLIDAY, params);
        
        // 오늘이 휴일일인지 판단
        if(rs.next()) {
        	isHoliday = true;
        }
        
        Service service = ServiceEngineContext.getInstance().getService(serviceId);
        if (service == null) {
        	throw new ServiceNotFoundException(serviceId);
        }
        
        // 영업일체크여부가 설정되어 있으면 영업일을 체크한다.
        if (service.isBizDayCheck() && isHoliday) {
            throw new ServiceException(ServiceErrorCode.TIME_CHECK_ERROR, "서비스 가능일이 아닙니다.");
        }
        // 서비스제공시간체크여부가 설정되어 있으면 서비스 가능 시간을 체크한다.
        if (service.isTimeCheck() && !service.isValidTime()) { 
            throw new ServiceException(ServiceErrorCode.BIZ_DAY_CHECK_ERROR, "서비스 제공시간이 아닙니다.");
        }
        
    	return isServiceEnableTime;
    }
    
    public boolean isTrxEnableTime(String trxId) throws MessageException {
    	return isTrxEnableTime;
    }
}