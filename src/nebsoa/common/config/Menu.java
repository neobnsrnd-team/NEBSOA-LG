/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.config;


/*******************************************************************
 * <pre>
 * 1.설명 
 * 메뉴 클래스 
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
 * $Log: Menu.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:26  cvs
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
 * Revision 1.1  2008/08/04 08:54:52  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/02/14 10:30:33  오재훈
 * WebAppMapping 클래스가 ap단으로 이동됨
 *
 * Revision 1.1  2008/01/22 05:58:28  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:13  안경아
 * *** empty log message ***
 *
 * Revision 1.8  2007/07/08 09:53:21  김성균
 * 로그여부 설정기능 추가
 *
 * Revision 1.7  2007/07/02 09:20:04  youngseokkim
 * 로그여부 추가
 *
 * Revision 1.6  2006/12/13 07:13:30  김성균
 * *** empty log message ***
 *
 * Revision 1.5  2006/12/07 02:25:06  김성균
 * *** empty log message ***
 *
 * Revision 1.4  2006/11/01 12:03:36  김성균
 * *** empty log message ***
 *
 * Revision 1.3  2006/09/13 13:06:08  오재훈
 * 마이플랫폼에서 request를 비동기적으로 계속해서 호출해야 한다는 요건에 따라서
 * 디비에 메뉴 등록시 플래그를 셋팅하게 하여 비동기 호출이 가능한 URL을 적용함.
 *
 * Revision 1.2  2006/08/02 01:18:12  김성균
 * WEB_APP_ID 추가
 *
 * Revision 1.1  2006/06/21 13:49:25  김성균
 * 최초 등록
 *
 * </pre>
 ******************************************************************/
public class Menu {
	
	/**
	 * 메뉴ID
	 */
	private String menuId;
	
	/**
	 * 메뉴URL 
	 */
	private String menuUrl;
    
	/**
	 * 메뉴명 
	 */
	private String menuName;
    
    /**
     * WEB_APP_ID
     */
    private String webAppId;
	
	/**
	 * WEB_APP 
	 */
	private WebApp webApp;
    
	/**
	 * 시작시간 
	 */
	private String startTime;
    
	/**
	 * 마감시간 
	 */
	private String endTime;
    
	/**
	 * 시간체크여부 
	 */
	private String timeCheckYn;
    
	/**
	 * 은행영업일체크여부 
	 */
	private String bizDayCheckYn;

	/**
	 * 비동기 호출 여부
	 */
	private String aSyncYn;
	
	/**
	 * 로그여부
	 */
	private String logYn;

	/**
	 * @return Returns the menuId.
	 */
	public String getMenuId() {
		return menuId;
	}

	/**
	 * @param menuId The menuId to set.
	 */
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	/**
	 * @return Returns the menuUrl.
	 */
	public String getMenuUrl() {
		return menuUrl;
	}

	/**
	 * @param menuUrl The menuUrl to set.
	 */
	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}

	/**
     * @return Returns the webAppId.
     */
    public String getWebAppId() {
        return webAppId;
    }

    /**
     * @param webAppId The webAppId to set.
     */
    public void setWebAppId(String webAppId) {
        this.webAppId = webAppId;
    }

    /**
	 * @return Returns the webApp.
	 */
	public WebApp getWebApp() {
		return webApp;
	}

	/**
	 * @param webApp The webApp to set.
	 */
	public void setWebApp(WebApp webApp) {
		this.webApp = webApp;
	}

	public boolean isASync() {
		return "Y".equals(aSyncYn);
	}

	public void setASyncYn(String syncYn) {
		aSyncYn = syncYn;
	}

    /**
     * @return Returns the endTime.
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * @param endTime The endTime to set.
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * @return Returns the startTime.
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * @param startTime The startTime to set.
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * @return Returns the timeCheckYn.
     */
    public boolean isTimeCheck() {
        return "Y".equals(timeCheckYn);
    }

    /**
     * @param timeCheckYn The timeCheckYn to set.
     */
    public void setTimeCheckYn(String timeCheckYn) {
        this.timeCheckYn = timeCheckYn;
    }

    /**
     * @return Returns the bizDayCheckYn.
     */
    public boolean isBizDayCheck() {
        return "Y".equals(bizDayCheckYn);
    }

    /**
     * @param bizDayCheckYn The bizDayCheckYn to set.
     */
    public void setBizDayCheckYn(String bizDayCheckYn) {
        this.bizDayCheckYn = bizDayCheckYn;
    }

    /**
     * @return Returns the logYn.
     */
    public boolean isLogEnabled() {
        return "Y".equals(logYn);
    }

    /**
     * @param logYn The logYn to set.
     */
    public void setLogYn(String logYn) {
        this.logYn = logYn;
    }
    
    /**
     * @return Returns the menuName.
     */
    public String getMenuName() {
        return menuName;
    }

    /**
     * @param menuName The menuName to set.
     */
    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }
}
