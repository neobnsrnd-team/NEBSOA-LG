/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.error;

import java.io.FileNotFoundException;

import org.apache.commons.collections.map.MultiKeyMap;

import nebsoa.common.Constants;
import nebsoa.common.exception.SysException;
import nebsoa.common.jdbc.DBManager;
import nebsoa.common.jdbc.DBResultSet;
import nebsoa.common.util.DataMap;
import nebsoa.management.ManagementObject;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 오류코드 정보를 얻어오기 위한 클래스 
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
 * $Log: ErrorCodeUtil.java,v $
 * Revision 1.1  2018/01/15 03:39:54  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:39  cvs
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
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.2  2008/01/25 01:55:25  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:33  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.3  2008/01/21 03:27:58  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2007/11/30 09:46:51  안경아
 * DB NAME 지정
 *
 * Revision 1.1  2007/11/26 08:39:09  안경아
 * *** empty log message ***
 *
 * Revision 1.11  2006/11/16 10:29:51  김성균
 * *** empty log message ***
 *
 * Revision 1.10  2006/11/16 09:22:13  김성균
 * *** empty log message ***
 *
 * Revision 1.9  2006/10/20 11:40:30  김성균
 * *** empty log message ***
 *
 * Revision 1.8  2006/10/20 09:07:16  김성균
 * *** empty log message ***
 *
 * Revision 1.7  2006/08/29 10:53:29  김성균
 * *** empty log message ***
 *
 * Revision 1.6  2006/08/29 08:24:34  최수종
 * Exception처리 수정
 *
 * Revision 1.5  2006/08/22 08:54:05  김성균
 * 도움말PAGE_URL 추가
 *
 * Revision 1.4  2006/08/03 05:17:44  김성균
 * 메소드명 변경
 *
 * Revision 1.3  2006/08/02 08:11:02  김승희
 * ManagementObject  메소드명 변경
 *
 * Revision 1.2  2006/08/01 08:42:41  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2006/07/31 11:26:12  김성균
 * 최초 등록
 *
 * </pre>
 ******************************************************************/
public class ErrorCodeUtil extends ManagementObject {
    
    private static Object dummy = new Object();
    
    private static ErrorCodeUtil instance;
    
	/**
     * 데이타를 캐쉬 해 놓기 위한 저장소 
     */
    protected MultiKeyMap errorCodePool;
    
    /**
     * 싱글톤 처리 
     * 인스턴스 생성하고 데이타 로딩...
     */
    private ErrorCodeUtil() {
        errorCodePool = new MultiKeyMap();
    }
    
    /**
     * 싱글톤 객체 얻어오기
     * @return
     */
    public static ErrorCodeUtil getInstance(){
        if (instance == null) {
            synchronized (dummy) {
                instance = new ErrorCodeUtil();
                instance.loadAll();
                instance.toXml();
            }
        }
        return instance;
    }
    
    /**
     * 오류코드 정보를 로딩한다.
     */
    private void loadAll() {
        if (isXmlMode()) {
            try {
                fromXml();
            } catch (FileNotFoundException e) {
                throw new SysException("XML 파일을 찾을 수 없습니다.");
            }
        } else {
            loadErrorDesc();
        }
    }
    
	/**
     * 오류코드에 대한 오류설명을 로드하기 위한 SQL
     */
    private static final String LOAD_ERROR_DESC = 
    	  "\r\n SELECT ERROR_CODE "
    	+ "\r\n   ,LOCALE_CODE  "
    	+ "\r\n   ,ERROR_TITLE  "
    	+ "\r\n   ,ERROR_CAUSE_DESC  "
    	+ "\r\n   ,ERROR_GUIDE_DESC  "
    	+ "\r\n   ,HELP_PAGE_URL  "
    	+ "\r\n   ,IBS_ERROR_GUIDE_DESC  "
    	+ "\r\n   ,CMS_ERROR_GUIDE_DESC  "
    	+ "\r\n   ,ETC_ERROR_GUIDE_DESC  "
    	+ "\r\n FROM FWK_ERROR_DESC "
        ;

	private static final Object IBS_CHANNEL = "IBS";
	private static final Object CMS_CHANNEL = "CMS";

    /**
     * 오류코드 정보를 로딩합니다.
     */
    private void loadErrorDesc() {
    	Object[] params = {};
        
        DBResultSet rs = DBManager.executePreparedQuery(Constants.SPIDER_DB, LOAD_ERROR_DESC, params);
        String errorCode = null;
        String localeCode = null;
        ErrorDesc errorDesc = null;
        
        while (rs.next()) {
            errorCode = rs.getString("ERROR_CODE");
            localeCode = rs.getString("LOCALE_CODE");
            errorDesc = new ErrorDesc();
            errorDesc.setErrorCode(errorCode);
            errorDesc.setLocaleCode(localeCode);
            errorDesc.setErrorTitle(rs.getString("ERROR_TITLE"));
            errorDesc.setErrorCauseDesc(rs.getString("ERROR_CAUSE_DESC"));
            errorDesc.setErrorGuideDesc(rs.getString("ERROR_GUIDE_DESC"));
            errorDesc.setHelpPageUrl(rs.getString("HELP_PAGE_URL"));
            errorDesc.setIbsErrorGuideDesc(rs.getString("IBS_ERROR_GUIDE_DESC"));
            errorDesc.setCmsErrorGuideDesc(rs.getString("CMS_ERROR_GUIDE_DESC"));
            errorDesc.setEtcErrorGuideDesc(rs.getString("ETC_ERROR_GUIDE_DESC"));
            errorCodePool.put(errorCode, localeCode, errorDesc);
        }
    }
    
    /**
     * 오류코드 정보가 등록되어 있는지 검사
     * @param errorCode
     * @param localeCode
     * @return
     */
    public boolean existErrorCode(String errorCode, String localeCode) {
        return errorCodePool.containsKey(errorCode, localeCode);
    }
    
    /**
     * 오류제목 얻어오기
     * @param errorCode
     * @param localeCode
     * @return
     */
    public String getErrorTitle(String errorCode, String localeCode) {
        ErrorDesc errorDesc = getErrorDesc(errorCode, localeCode);
        if (errorDesc == null) {
            return null;
        }
        return errorDesc.getErrorTitle();
    }

    
    /**
     * 오류원인설명 얻어오기
     * @param errorCode
     * @param localeCode
     * @return
     */
    public String getErrorCauseDesc(String errorCode, String localeCode) {
        ErrorDesc errorDesc = getErrorDesc(errorCode, localeCode);
        if (errorDesc == null) {
            return null;
        }
        return errorDesc.getErrorCauseDesc();
    }
    
    /**
     * 오류조치방법설명 얻어오기
     * @param errorCode
     * @param localeCode
     * @return
     */
    public String getErrorGuideDesc(String errorCode, String localeCode) {
        ErrorDesc errorDesc = getErrorDesc(errorCode, localeCode);
        if (errorDesc == null) {
            return null;
        }
        return errorDesc.getErrorGuideDesc();
    }
    
    /**
     * 채널별 오류조치방법설명 얻어오기
     * @param errorCode
     * @param localeCode
     * @param 채널구분
     * @return
     */
    public String getErrorGuideDesc(String errorCode, String localeCode, String channel) {
        ErrorDesc errorDesc = getErrorDesc(errorCode, localeCode);
        if (errorDesc == null) {
            return null;
        }
        if(IBS_CHANNEL.equals(channel)) return errorDesc.getIbsErrorGuideDesc();
        else if(CMS_CHANNEL.equals(channel)) return errorDesc.getCmsErrorGuideDesc();
        else return errorDesc.getEtcErrorGuideDesc();
        
    }
    
    /**
     * 도움말페이지URL 얻어오기
     * @param errorCode
     * @param localeCode
     * @return
     */
    public String getHelpPageUrl(String errorCode, String localeCode) {
        ErrorDesc errorDesc = getErrorDesc(errorCode, localeCode);
        if (errorDesc == null) {
            return null;
        }
        return errorDesc.getHelpPageUrl();
    }
    
    /**
     * 개인뱅킹 error desc 얻어오기
     * @param errorCode
     * @param localeCode
     * @return
     */
    public String getIbsErrorGuideDesc(String errorCode, String localeCode){
    	ErrorDesc errorDesc = getErrorDesc(errorCode, localeCode);
    	if(errorDesc == null){
    		return null;
    	}
    	return errorDesc.getIbsErrorGuideDesc();
    }

    /**
     * 기업뱅킹 error desc 얻어오기
     * @param errorCode
     * @param localeCode
     * @return
     */
    public String getCmsErrorGuideDesc(String errorCode, String localeCode){
    	ErrorDesc errorDesc = getErrorDesc(errorCode, localeCode);
    	if(errorDesc == null){
    		return null;
    	}
    	return errorDesc.getCmsErrorGuideDesc();
    }
    
    /**
     * 기타 시스템 error desc 얻어오기
     * @param errorCode
     * @param localeCode
     * @return
     */
    public String getEtcErrorGuideDesc(String errorCode, String localeCode){
    	ErrorDesc errorDesc = getErrorDesc(errorCode, localeCode);
    	if(errorDesc == null){
    		return null;
    	}
    	return errorDesc.getEtcErrorGuideDesc();
    }    
    /**
     * 오류설명 객체 얻어오기
     * @param errorCode
     * @param localeCode
     * @return
     */
    private ErrorDesc getErrorDesc(String errorCode, String localeCode) {
        return (ErrorDesc) errorCodePool.get(errorCode, localeCode);
    }
    
    /**
     * 전체리로딩 
     */
    public static void reloadAll(DataMap map) {
        getInstance().loadAll();
        getInstance().toXml();
    }
    
    /* (non-Javadoc)
     * @see nebsoa.management.ManagementObject#getInstance()
     */
    public Object getManagementObject() {
        return instance;
    }
    
    /* (non-Javadoc)
     * @see nebsoa.management.ManagementObject#setInstance(java.lang.Object)
     */
    public void setManagementObject(Object obj) {
        instance = (ErrorCodeUtil) obj;
    }

}
