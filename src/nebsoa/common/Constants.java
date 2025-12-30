/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common;

import nebsoa.common.startup.StartupContext;
import nebsoa.common.util.PropertyManager;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 상수를 정의하는 클래스 입니다.
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
 * $Log: Constants.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:33  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:27  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:29  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.2  2008/08/25 13:40:55  jglee
 * SERVICE_RETURN 추가
 *
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:34  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.3  2008/01/21 03:28:18  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2007/11/30 09:46:54  안경아
 * DB NAME 지정
 *
 * Revision 1.1  2007/11/26 08:39:06  안경아
 * *** empty log message ***
 *
 * Revision 1.12  2007/01/22 08:15:49  김성균
 * BEFORE_PAGE(이전페이지) 상수 추가
 *
 * Revision 1.11  2006/09/11 05:26:56  최수종
 * List 상수 추가
 *
 * Revision 1.10  2006/08/22 08:58:12  김성균
 * HELP_PAGE_URL 추가
 *
 * Revision 1.9  2006/08/03 08:29:14  김성균
 * ERROR_EXCEPTION 추가
 *
 * Revision 1.8  2006/07/31 11:26:54  김성균
 * RESPONSE_GUIDE_MESSAGE 추가
 *
 * Revision 1.7  2006/07/26 06:19:18  김성균
 * TARGET_METHOD 추가
 *
 * Revision 1.6  2006/06/17 10:37:45  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class Constants {

    /**
     * 프레임웍의 최상위 디렉토리 
     */
    public static final String APP_HOME_DIR = StartupContext.SPIDER_HOME;
    
    /**
     * 로그정보가 저장되는 디렉토리 
     */
    public static String LOG_HOME_DIR = APP_HOME_DIR+"/logs/";
            
	/**
	 * request에 REQUEST 정보를 담고 있는 MAP 객체를 담는 이름.
	 * <font color=red> 이것이 바뀌면 jsp/include/common.jsp에서 
	 * &lt;jsp:useBean id="dataMap"부분이 바뀌어야 하므로..주의 </font>
	 */
	public static String DATA_MAP="dataMap";
    
	/**
	 * ERROR PAGE에서 ERROR TITLE을 저장한 값
	 */
    public static final String ERROR_TITLE = "ERROR_TITLE";
    
	/**
	 * ERROR PAGE에서 ERROR CODE를 저장한 값
	 */
    public static final String ERROR_CODE = "ERROR_CODE";
    
	/**
	 * ERROR PAGE에서 ERROR EXCEPTION을 저장한 값
	 */
    public static final String ERROR_EXCEPTION = "ERROR_EXCEPTION";
    
	/**
	 * WEB PAGE에서 RESPONSE MESSAGE을 저장한 값
	 */
    public static final String RESPONSE_MESSAGE = "RESPONSE_MESSAGE";
    
	/**
	 * WEB PAGE에서 오류조치방법설명을 저장한 값
	 */
    public static final String RESPONSE_GUIDE_MESSAGE = "RESPONSE_GUIDE_MESSAGE";
    
    /**
	 * WEB PAGE에서 개인뱅킹 오류조치방법설명을 저장한 값
	 */
    public static final String IBS_RESPONSE_GUIDE_MESSAGE = "IBS_RESPONSE_GUIDE_MESSAGE";
    
    /**
	 * WEB PAGE에서 기업뱅킹 오류조치방법설명을 저장한 값
	 */
    public static final String CMS_RESPONSE_GUIDE_MESSAGE = "CMS_RESPONSE_GUIDE_MESSAGE";
    
    /**
	 * WEB PAGE에서 기타시스템 오류조치방법설명을 저장한 값
	 */
    public static final String ETC_RESPONSE_GUIDE_MESSAGE = "ETC_RESPONSE_GUIDE_MESSAGE";
    
	/**
	 * WEB PAGE에서 도움말페이지URL을 저장한 값
	 */
    public static final String HELP_PAGE_URL = "HELP_PAGE_URL";
    
    /**
	 * WEB PAGE에서 TARGET PAGE를 저장한 값
     */
    public static final String TARGET_PAGE = "TARGET_PAGE";
    
    /**
     * WEB PAGE에서 BEFORE PAGE를 저장한 값
     */
    public static final String BEFORE_PAGE = "BEFORE_PAGE";

    /**
     * 수행할 액션 메소드명
     */
    public static final String TARGET_METHOD = "targetMethod";
    
    /**
     * DataMap에 담겨있는 DataSet결과(리스트)의 Key명(ExcelAction에서도 사용함)
     */
    public static final String RESULT_LIST = "$$RESULT_LIST$$";
    
    /**
     * framework 사용하는 Database이름
     */
    public static final String SPIDER_DB = "SPIDER";    

    /**
     * Service에서 Component return 처리 구분값
     */
    public static final String SERVICE_RETURN = "SERVICE_RETURN";    
}

