/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.config;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import nebsoa.common.Constants;
import nebsoa.common.exception.SysException;
import nebsoa.common.jdbc.DBManager;
import nebsoa.common.jdbc.DBResultSet;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.FormatUtil;
import nebsoa.management.ManagementObject;

/*******************************************************************************
 * <pre>
 *  1.설명 
 *  WEB_APP 매핑 클래스 
 *  
 *  2.사용법
 *  
 *  &lt;font color=&quot;red&quot;&gt;
 *  3.주의사항
 *  &lt;/font&gt;
 * 
 *  @author $Author: cvs $
 *  @version
 * ******************************************************************
 *  - 변경이력 (버전/변경일시/작성자)
 *  
 *  $Log: WebAppMapping.java,v $
 *  Revision 1.1  2018/01/15 03:39:53  cvs
 *  *** empty log message ***
 *
 *  Revision 1.1  2016/04/15 02:23:26  cvs
 *  neo cvs init
 *
 *  Revision 1.1  2011/07/01 02:13:51  yshong
 *  *** empty log message ***
 *
 *  Revision 1.1  2008/11/18 11:27:26  김성균
 *  *** empty log message ***
 *
 *  Revision 1.1  2008/11/18 11:01:29  김성균
 *  LGT Gateway를 위한 프로젝트로 분리
 *
 *  Revision 1.1  2008/08/04 08:54:52  youngseokkim
 *  *** empty log message ***
 *
 *  Revision 1.1  2008/02/14 10:30:33  오재훈
 *  WebAppMapping 클래스가 ap단으로 이동됨
 *
 *  Revision 1.1  2008/01/22 05:58:28  오재훈
 *  패키지 리펙토링
 *
 *  Revision 1.2  2007/11/30 09:46:55  안경아
 *  DB NAME 지정
 *
 *  Revision 1.1  2007/11/26 08:38:18  안경아
 *  *** empty log message ***
 *
 *  Revision 1.35  2007/10/01 04:06:25  최수종
 *  synchronized 구문을 다시 if(instance == null) 문장안으로 이동(동기화 구문의 수행성능에 대한 이슈가 발생할 가능성이 있기때문)
 *
 *  Revision 1.34  2007/09/28 01:36:15  최수종
 *  synchronized 구문을 if(instance == null) 문장 밖으로 이동
 *
 *  Revision 1.33  2007/07/08 09:52:15  김성균
 *  로그여부 설정기능 추가
 *
 *  Revision 1.32  2007/07/02 09:20:04  youngseokkim
 *  로그여부 추가
 *
 *  Revision 1.31  2007/04/24 09:23:39  이종원
 *  메뉴명이 없더라도 오류는 나지 않도록 수정
 *
 *  Revision 1.30  2007/01/31 07:03:27  김성균
 *  *** empty log message ***
 *
 *  Revision 1.29  2007/01/31 01:02:50  김성균
 *  관리자일 경우 모든 메뉴정도 로딩하도록 변경
 *
 *  Revision 1.28  2007/01/09 13:27:08  김성균
 *  메뉴정보 없을경우 URL도 에러메시지에 추가
 *
 *  Revision 1.27  2006/12/18 06:37:38  김성균
 *  *** empty log message ***
 *
 *  Revision 1.26  2006/12/14 06:15:10  김성균
 *  *** empty log message ***
 *
 *  Revision 1.25  2006/12/13 07:13:30  김성균
 *  *** empty log message ***
 *
 *  Revision 1.24  2006/12/07 02:25:06  김성균
 *  *** empty log message ***
 *
 *  Revision 1.23  2006/11/16 10:23:05  김성균
 *  *** empty log message ***
 *
 *  Revision 1.22  2006/11/16 09:22:13  김성균
 *  *** empty log message ***
 *
 *  Revision 1.21  2006/11/08 01:03:18  김성균
 *  *** empty log message ***
 *
 *  Revision 1.20  2006/11/04 08:35:14  김성균
 *  *** empty log message ***
 *
 *  Revision 1.19  2006/11/01 12:03:42  김성균
 *  *** empty log message ***
 * 
 *  Revision 1.18  2006/10/30 01:43:45  김성균
 *  *** empty log message ***
 * 
 *  Revision 1.17  2006/10/20 11:40:15  김성균
 *  *** empty log message ***
 * 
 *  Revision 1.16  2006/10/20 09:07:37  김성균
 *  *** empty log message ***
 * 
 *  Revision 1.15  2006/10/20 07:11:25  김성균
 *  *** empty log message ***
 * 
 *  Revision 1.14  2006/09/13 13:06:08  오재훈
 *  마이플랫폼에서 request를 비동기적으로 계속해서 호출해야 한다는 요건에 따라서
 *  디비에 메뉴 등록시 플래그를 셋팅하게 하여 비동기 호출이 가능한 URL을 적용함.
 * 
 *  Revision 1.13  2006/08/21 07:36:39  김성균
 *  *** empty log message ***
 * 
 *  Revision 1.12  2006/08/02 04:09:51  김성균
 *  WEB_APP 매핑 테이블 사용하지 않도록 수정
 * 
 *  Revision 1.11  2006/08/02 01:51:03  김성균
 *  WEB_APP 클래스명 가져오는 부분 수정
 * 
 *  Revision 1.10  2006/07/31 05:22:19  김성균
 *  *** empty log message ***
 * 
 *  Revision 1.9  2006/07/10 13:28:34  김성균
 *  *** empty log message ***
 * 
 *  Revision 1.8  2006/07/10 08:55:40  김성균
 *  메뉴/WEB_APP/BIZ_APP 정보 존재하지 않으면 리로딩 하도록 임시 수정
 * 
 *  Revision 1.7  2006/06/27 12:26:19  오재훈
 *  메뉴테이블에 web_app_id가 없어도 동작하게 수정.
 * 
 *  Revision 1.6  2006/06/26 20:20:39  김성균
 *  *** empty log message ***
 * 
 *  Revision 1.5  2006/06/23 07:40:31  김성균
 *  메뉴 처리 부분 추가
 * 
 *  Revision 1.4  2006/06/22 05:36:44  김성균
 *  *** empty log message ***
 * 
 *  Revision 1.3  2006/06/22 05:18:08  김성균
 *  *** empty log message ***
 * 
 *  Revision 1.2  2006/06/22 04:46:11  김성균
 *  getBizAppId() 추가
 * 
 *  Revision 1.1  2006/06/21 13:49:25  김성균
 *  최초 등록
 * 
 * </pre>
 ******************************************************************************/
public class WebAppMapping extends ManagementObject {

    private static Object dummy = new Object();

    private static WebAppMapping instance;
    
    private static boolean isAdmin = false;

    /**
     * MENU 정보들을 가지고 있는 맵
     */
    private Map menuInfoMap;

    /**
     * 싱글톤 처리
     */
    private WebAppMapping() {
        menuInfoMap = new HashMap();
    }

    public static WebAppMapping getInstance() {
    	
        if (instance == null) {
            synchronized (dummy) {	        	
	            instance = new WebAppMapping();
	            instance.loadAll();
	            if (!isAdmin) {
	                instance.toXml();
	            }
            }
        }
        return instance;
    }

    /**
     * 메뉴매핑정보를 로딩한다.
     */
    private void loadAll() {
        if (isXmlMode()) {
            try {
                fromXml();
            } catch (FileNotFoundException e) {
                throw new SysException("XML 파일을 찾을 수 없습니다.");
            }
        } else {
            if (isAdmin) {
                loadAdminMenu();
                loadCustMenu();
            } else {
                loadCustMenu();
            }
        }
    }

    /**
     * @return Returns the menuInfoMap.
     */
    public Map getMenuInfoMap() {
        return menuInfoMap;
    }
    
    /**
     * 관리자메뉴 정보를 로드하기 위한 SQL
     */
    private static final String LOAD_ADMIN_MENU = 
          "\r\n SELECT MENU_ID "
        + "\r\n   ,MENU_URL  "
        + "\r\n   ,WEB_APP_ID  "
        + "\r\n   ,MENU_NAME  "
        + "\r\n FROM FWK_MENU "
        + "\r\n WHERE USE_YN = 'Y' "
        ;

    /**
     * 관리자용 Menu 정보를 로딩한다.
     */
    private void loadAdminMenu() {
        Object[] params = {};
        String menuUrl = null;
        
        DBResultSet rs = DBManager.executePreparedQuery(Constants.SPIDER_DB, LOAD_ADMIN_MENU, params);
//        LogManager.debug(rs);
        
        if (rs.getRowCount() > 0) {
            if (menuInfoMap == null) {
                menuInfoMap = new HashMap();
            }
        }
        
        Menu menu = null;
        while (rs.next()) {
            menuUrl = rs.getString("MENU_URL");
            
            menu = new Menu();
            menu.setMenuUrl(menuUrl);
            menu.setMenuId(rs.getString("MENU_ID"));
            menu.setMenuName(rs.getString("MENU_NAME"));
            menu.setWebAppId(rs.getString("WEB_APP_ID"));
            
            menuInfoMap.put(menuUrl, menu);
        }
    }
    
    /**
     * 고객용메뉴 정보를 로드하기 위한 SQL
     */
    private static final String LOAD_CUST_MENU = 
          "\r\n SELECT MENU_URL "
        + "\r\n   ,WEB_APP_ID  "
        + "\r\n   ,MENU_ID  "
        + "\r\n   ,MENU_NAME  "
        + "\r\n   ,ASYNC_YN  "
        + "\r\n   ,START_TIME  "
        + "\r\n   ,END_TIME  "
        + "\r\n   ,TIME_CHECK_YN  "
        + "\r\n   ,BIZ_DAY_CHECK_YN  "
        + "\r\n   ,LOG_YN  "
        + "\r\n FROM FWK_CUST_MENU_APP "
        ;

    /**
     * 고객용 Menu 정보를 로딩한다.
     */
    private void loadCustMenu() {
        Object[] params = {};
        String menuUrl = null;
        String webAppId = null;
        
        DBResultSet rs = DBManager.executePreparedQuery(Constants.SPIDER_DB, LOAD_CUST_MENU, params);
//        LogManager.debug(rs);
        
        if (rs.getRowCount() > 0) {
            if (menuInfoMap == null) {
                menuInfoMap = new HashMap();
            }
        }
        
        Menu menu = null;
        while (rs.next()) {
            menuUrl = rs.getString("MENU_URL");
            webAppId = rs.getString("WEB_APP_ID");
            
            menu = new Menu();
            menu.setMenuUrl(menuUrl);
            menu.setMenuId(rs.getString("MENU_ID"));
            menu.setMenuName(rs.getString("MENU_NAME"));
            menu.setWebAppId(webAppId);
            menu.setASyncYn(rs.getString("ASYNC_YN"));
            menu.setStartTime(rs.getString("START_TIME"));
            menu.setEndTime(rs.getString("END_TIME"));
            menu.setTimeCheckYn(rs.getString("TIME_CHECK_YN"));
            menu.setBizDayCheckYn(rs.getString("BIZ_DAY_CHECK_YN"));
            menu.setLogYn(rs.getString("LOG_YN"));
            menuInfoMap.put(menuUrl, menu);
        }
    }

    /**
     * url에 해당하는 web_app 클래스명을 얻어온다.
     * 
     * @param url
     *            URL
     * @return WEB_APP명
     */
    public String getWebAppName(String url) {
        return getMenu(url).getWebAppId();
    }

    /**
     * url에 해당하는 web_app 클래스의 비동기 호출 여부를 가져온다.
     * 
     * @param url :
     *            URL
     * @return 비동기 호출 여부
     */
    public boolean isAsync(String url) {
        return getMenu(url).isASync();
    }
    
    /**
     * url에 해당하는 web_app 클래스의 은행영업일체크여부를 가져온다.
     * 
     * @param url :
     *            URL
     * @return 은행영업일체크여부
     */
    public boolean isBizDayCheck(String url) {
        Menu menu = getMenu(url);
        boolean isBizDayCheck = menu.isBizDayCheck();
        LogManager.debug("### 영업일체크여부 : " + isBizDayCheck);
        return isBizDayCheck;
    }

    /**
     * 메뉴에 시간체크여부가 설정되어 있으면 서비스 가능 시간을 체크한다.
     * 
     * @param url :
     *            URL
     * @return 서비스가능여부
     */
    public boolean isServiceEnableTime(String url) {
        Menu menu = getMenu(url);

        if (menu.isTimeCheck()) {
            LogManager.debug("### 서비스가능시간체크 ###");
            int startTime = Integer.parseInt(menu.getStartTime());
            int endTime = Integer.parseInt(menu.getEndTime());
            int currTime = Integer.parseInt(FormatUtil.getToday("HHmm"));
            return isValidTime(startTime, endTime, currTime);
        }
        return true;
    }

    /**
     * 2006. 11. 04 김성균
     * @param startTime
     * @param endTime
     * @param currTime
     */
    private boolean isValidTime(int startTime, int endTime, int currTime) {
        if (currTime >= startTime && currTime <= endTime) {
            return true;
        } else {
            LogManager.debug("### 서비스가능시간 아님 : " + currTime + "|" + startTime + "|" + endTime);
            return false;
        }
    }
    
    /**
     * url에 해당하는 web_app 클래스의 로그여부를 가져온다.
     * @param url
     * @return
     */
    public boolean isLogEnabled(String url) {
        Menu menu = getMenu(url);
        return menu.isLogEnabled();
    }

    /**
     * 메뉴정보를 얻어온다.
     * 
     * @param url
     * @return
     */
    private Menu getMenu(String url) {
        Menu menu = (Menu) this.menuInfoMap.get(url);
        if (menu == null) {
            throw new SysException("FRS00009", url + "에 해당하는 메뉴정보가 존재하지 않습니다.");
        }
        return menu;
    }

    /**
     * URL에 해당하는 등록된 메뉴가 있는지 검사
     * 
     * @param uri
     *            메뉴URL
     * @return
     */
    public boolean containsMenuUrl(String uri) {
        return menuInfoMap.containsKey(uri);
    }

    /**
     * 메뉴URL에 해당하는 메뉴ID를 얻어온다.
     * 
     * @param uri
     * @return
     */
    public String getMenuId(String uri) {
        return getMenu(uri).getMenuId();
    }
    
    /**
     * 메뉴URL에 해당하는 메뉴명을 얻어온다.
     * 
     * @param 메뉴명
     * @return
     */
    public String getMenuName(String uri) {
        try{
            return getMenu(uri).getMenuName();
        }catch(Exception e){
            LogManager.error(e.toString());
            return uri;
        }
        
    }

    /**
     * 전체리로딩
     */
    public void reloadAll() {
        if (menuInfoMap == null) {
            menuInfoMap = new HashMap();
        } else {
            menuInfoMap.clear();
        }
        loadAll();
        toXml();
    }

    /*
     * (non-Javadoc)
     * 
     * @see nebsoa.management.ManagementObject#getInstance()
     */
    public Object getManagementObject() {
        return instance;
    }

    /*
     * (non-Javadoc)
     * 
     * @see nebsoa.management.ManagementObject#setInstance(java.lang.Object)
     */
    public void setManagementObject(Object obj) {
        instance = (WebAppMapping) obj;
    }

    /**
     * @return Returns the isAdmin.
     */
    public static boolean isAdmin() {
        return isAdmin;
    }

    /**
     * @param isAdmin The isAdmin to set.
     */
    public static void setAdmin(boolean isAdmin) {
        WebAppMapping.isAdmin = isAdmin;
    }
}
