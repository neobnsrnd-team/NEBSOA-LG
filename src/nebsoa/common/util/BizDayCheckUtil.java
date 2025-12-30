/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
package nebsoa.common.util;

import nebsoa.common.exception.SysException;
import nebsoa.service.exception.ServiceException;
import nebsoa.spiderlink.exception.MessageException;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 영업일 체크하는 유틸리티 클래스입니다.  
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
 * $Log: BizDayCheckUtil.java,v $
 * Revision 1.1  2018/01/15 03:39:49  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:32  cvs
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
 * Revision 1.3  2008/10/06 01:38:14  김성균
 * 영업일체크사용여부를 설정에 의해서 관리하는 기능추가
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
public class BizDayCheckUtil {
	
    private static Object dummy = new Object();

    private static BizDayCheckUtil instance;
    
    /**
     * 영업일 정보를 로딩하는 대리 클래스 
     */
    private static BizDayCheckable bizDayCheckDelegator;
    
    /**
     * 싱글톤 처리
     */
    private BizDayCheckUtil() {
        String className = null; 
        try {
            if (isBizDayCheck()) {
                className = PropertyManager.getProperty("default","BIZ_DAY_CHECK_DELEGATOR_CLASS_NAME", "nebsoa.common.util.BizDayChecker");
                bizDayCheckDelegator = (BizDayCheckable) Class.forName(className).newInstance();
            }
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
    public static BizDayCheckUtil getInstance() {
        if (instance == null) {
            synchronized (dummy) {
                instance = new BizDayCheckUtil();
            }
        }
        return instance;
    }
	
    /**
     * 현재 서비스 가능시간인지 여부를 리턴한다.
     * @return
     */
    public boolean isServiceEnableTime(String serviceId) throws ServiceException {
        if (isBizDayCheck()) {
            return bizDayCheckDelegator.isServiceEnableTime(serviceId);
        } else {
            return true;
        }
    }

    /**
     * 현재 거래 가능시간인지 여부를 리턴한다.
     * @return
     */
    public boolean isTrxEnableTime(String trxId) throws MessageException {
        if (isBizDayCheck()) {
            return bizDayCheckDelegator.isTrxEnableTime(trxId);
        } else {
    	    return true;
    	}
    }
    
    /**
     * 영업일체크모드를 리턴한다.
     * @return
     */
    public boolean isBizDayCheck() {
        return PropertyManager.getBooleanProperty("default", "BIZ_DAY_CHECK_MODE", "true");
    }
}
