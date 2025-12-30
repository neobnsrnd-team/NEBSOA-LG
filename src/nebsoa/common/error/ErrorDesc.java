/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.error;




/*******************************************************************
 * <pre>
 * 1.설명 
 * 오류설명 클래스 
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
 * $Log: ErrorDesc.java,v $
 * Revision 1.1  2018/01/15 03:39:54  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:38  cvs
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
 * Revision 1.1  2008/01/22 05:58:33  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.2  2008/01/21 03:27:58  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2007/11/26 08:39:09  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2006/07/31 11:26:12  김성균
 * 최초 등록
 *
 *
 * </pre>
 ******************************************************************/
public class ErrorDesc {
	
	/**
	 * 오류코드
	 */
	public String errorCode;
    
	/**
	 * 언어코드
	 */
	public String localeCode;
    
	/**
	 * 오류제목 
	 */
	public String errorTitle;
    
	/**
	 * 오류원인설명 
	 */
	public String errorCauseDesc;
    
	/**
	 * 도움말페이지URL 
	 */
	public String helpPageUrl;
    
	/**
	 *  
	 */
	public String errorGuideDesc;
	public String ibsErrorGuideDesc;
	public String cmsErrorGuideDesc;
	public String etcErrorGuideDesc;

    /**
     * @return Returns the errorCauseDesc.
     */
    public String getErrorCauseDesc() {
        return errorCauseDesc;
    }

    /**
     * @param errorCauseDesc The errorCauseDesc to set.
     */
    public void setErrorCauseDesc(String errorCauseDesc) {
        this.errorCauseDesc = errorCauseDesc;
    }

    /**
     * @return Returns the errorCode.
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * @param errorCode The errorCode to set.
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * @return Returns the errorGuideDesc.
     */
    public String getErrorGuideDesc() {
        return errorGuideDesc;
    }

    /**
     * @param errorGuideDesc The errorGuideDesc to set.
     */
    public void setErrorGuideDesc(String errorGuideDesc) {
        this.errorGuideDesc = errorGuideDesc;
    }

    /**
     * @return Returns the errorTitle.
     */
    public String getErrorTitle() {
        return errorTitle;
    }

    /**
     * @param errorTitle The errorTitle to set.
     */
    public void setErrorTitle(String errorTitle) {
        this.errorTitle = errorTitle;
    }

    /**
     * @return Returns the localeCode.
     */
    public String getLocaleCode() {
        return localeCode;
    }

    /**
     * @param localeCode The localeCode to set.
     */
    public void setLocaleCode(String localeCode) {
        this.localeCode = localeCode;
    }

    /**
     * @return Returns the helpPageUrl.
     */
    public String getHelpPageUrl() {
        return helpPageUrl;
    }

    /**
     * @param helpPageUrl The helpPageUrl to set.
     */
    public void setHelpPageUrl(String helpPageUrl) {
        this.helpPageUrl = helpPageUrl;
    }

	public String getCmsErrorGuideDesc() {
		return cmsErrorGuideDesc;
	}

	public void setCmsErrorGuideDesc(String cmsErrorGuideDesc) {
		this.cmsErrorGuideDesc = cmsErrorGuideDesc;
	}

	public String getEtcErrorGuideDesc() {
		return etcErrorGuideDesc;
	}

	public void setEtcErrorGuideDesc(String etcErrorGuideDesc) {
		this.etcErrorGuideDesc = etcErrorGuideDesc;
	}

	public String getIbsErrorGuideDesc() {
		return ibsErrorGuideDesc;
	}

	public void setIbsErrorGuideDesc(String ibsErrorGuideDesc) {
		this.ibsErrorGuideDesc = ibsErrorGuideDesc;
	}
	
}
