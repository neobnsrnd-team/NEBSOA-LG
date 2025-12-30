/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.acl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import nebsoa.common.log.LogManager;
import nebsoa.common.util.FormatUtil;
/*******************************************************************
 * <pre>
 * 1.설명 
 * 로그인한 사용자의 정보를 담고 있는 클래스입니다. 
 * 이 객체가 세션에 등록 됩니다.
 * 웹 뿐 아니라, 다른 application에서도 사용하기위해 nebsoa.common.acl로 
 * 정의하였습니다.
 * 
 * 2.사용법
 *
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author 이종원
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 
 * $Log: UserSession.java,v $
 * Revision 1.1  2018/01/15 03:39:51  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:05  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:25  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:28  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.2  2008/10/28 09:03:38  yshong
 * roleId 추가
 *
 * Revision 1.1  2008/08/04 08:54:51  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.5  2008/07/01 08:37:38  이종원
 * 동시 접속자 체크 로직 추가
 *
 * Revision 1.4  2008/02/20 04:24:04  김성균
 * 사용자수 증가/감소 메소드 중복 호출 하지 않도록 수정
 *
 * Revision 1.3  2008/02/14 09:19:52  오재훈
 * *** empty log message ***
 *
 * Revision 1.2  2008/02/14 04:34:29  오재훈
 * web.login.Authenticator, Authorizor 클래스들과 web.session.SessionManager,UserInfo 들이 삭제되고 common.acl 패키지의 클래스들로 대체되었습니다.
 * web.util.Request.Util 클래스도 common.util.RequestUtil로 옴겨졌습니다.
 *
 * Revision 1.1  2008/01/22 05:58:23  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:39  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2007/01/31 13:26:40  이종원
 * 최초작성
 *
 * Revision 1.1  2007/01/29 09:03:24  이종원
 * refactoring
 *
 * Revision 1.1  2007/01/29 08:59:58  이종원
 * 최초작성
 *
 * </pre>
 ******************************************************************/
public class UserSession extends HashMap implements Serializable, HttpSessionBindingListener{

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 8028249488032508129L;

    protected transient HttpSession session;
	
	public static final String USER_ID = "USER_ID";

    public static final String JUMIN_NO = "JUMIN_NO";

    public static final String USER_NAME = "USER_NAME";

    public static final String USER_IP = "USER_IP";

    public static final String LOGIN_TIME = "LOGIN_TIME";

    public static final String PASSWORD = "PASSWORD";

    public static final String LOCALE_CODE = "LOCALE_CODE";

    public static final String AUTH_LEVEL = "AUTH_LEVEL";
    
    public static final String EMAIL = "EMAIL";  // 이메일
        
    public static final String LOG_YN = "LOG_YN";

    public static final String CONTEXT_NAME = "CONTEXT_NAME";
    
    public static final String ROLE_ID = "ROLE_ID";
    
    Role[] roles;
    
    /**
     * 동시 접속자 체크를 위하여 다른 pc에서 로그인한 사용자가 있을 경우 이 세션을 dirty로 
     * 마킹 처리하게 된다.
     */
    boolean dirty=false;
	
	public UserSession() {}
	
	public UserSession(Map map) {
		super(map);
	}
	
	public String getString(String key) {
		return (String) get(key);
	}
    
	public boolean getBoolean(String key) {
        String value = (String) get(key);
		return ("Y".equalsIgnoreCase(value) || "1".equals(value) ||
                "true".equalsIgnoreCase(value) || "yes".equalsIgnoreCase(value));
	}
	
	/**
	 * @return id의 값을 얻어 옵니다.
	 */
	public String getUserId() {
		return getString(USER_ID);
	}

	/**
	 * @param userId The userId를 세팅합니다.
	 */
	public void setUserId(String userId) {
		put(USER_ID, userId);
	}
	
	/**
	 * @return password 값을 얻어 옵니다.
	 */
	public String getPassword() {
		return getString(PASSWORD);
	}

	/**
	 * @param userId The password를 세팅합니다.
	 */
	public void setPassword(String password) {
		put(PASSWORD, password);
	}

	/**
	 * @return juminNo의 값을 얻어 옵니다.
	 */
	public String getJuminNo() {
		return getString(JUMIN_NO);
	}

	/**
	 * @param juminNo The juminNo을 세팅합니다.
	 */
	public void setJuminNo(String juminNo) {
		put(JUMIN_NO, juminNo);
	}

	/**
	 * @return name의 값을 얻어 옵니다.
	 */
	public String getUserName() {
		return getString(USER_NAME);
	}

	/**
	 * @param name The name을 세팅합니다.
	 */
	public void setUserName(String userName) {
		put(USER_NAME, userName);
	}

	/**
	 * @return userIp의 값을 얻어 옵니다.
	 */
	public String getUserIp() {
		return getString(USER_IP);
	}

	/**
	 * @param userIp The userIp을 세팅합니다.
	 */
	public void setUserIp(String userIp) {
		put(USER_IP, userIp);
	}
	
	
	
	/**
	 * @return 언어코드값을 얻어 옵니다.
	 */
	public String getLocaleCode() {
		return getString(LOCALE_CODE);
	}
	
	/**
	 * @param localeCode The localeCode를 세팅합니다.
	 */
	public void setLocaleCode(String localeCode) {
		put(LOCALE_CODE, localeCode);
	}

	/**
	 * @return 해당 Command 에 대한 권한 레벨을 가져옵니다.
	 */
	public String getAuthLevel() {
		return getString(AUTH_LEVEL);
	}
	
	
	/**
	 * @param 해당 Command 에 대한 권한 레벨을 세팅합니다.
	 */
	public void setAuthLevel(String AuthLevel) {
		put(AUTH_LEVEL, AuthLevel);
	}

	
    /**
     * email의 값을 얻어 옵니다.
     * @return email
     */
    public String getEmail() {
        return getString(EMAIL);
    }

    /**
     * email를 세팅합니다.
     * @param email
     */
    public void setEmail(String email) {
        put(EMAIL, email);
    }    
    
    
    /**
     * roleId 값을 얻어 옵니다.
     * @return roleId
     */
    public String getRoleId() {
        return getString(ROLE_ID);
    }

    /**
     * roleId를 세팅합니다.
     * @param roleId
     */
    public void setRoleId(String roleId) {
        put(ROLE_ID, roleId);
    }    
    
    /**
     * @param logYn The logYn을 세팅합니다.
     */
    public void setLogYn(String logYn) {
        put(LOG_YN, logYn);
    }
    
    /**
     * 로그여부를 리턴합니다.
     * @return 로그여부
     */
    public boolean isLogEnabled() {
        return getBoolean(LOG_YN);
    }

    /**
     * CONTEXT_NAME 값을 얻어 옵니다.
     * @return email
     */
    public String getContextName() {
        return getString(CONTEXT_NAME);
    }

    /**
     * CONTEXT_NAME을 세팅합니다.
     * @param email
     */
    public void setContextName(String contextName) {
        put(CONTEXT_NAME, contextName);
    }    

	/**
	 * @return loginTime의 값을 얻어 옵니다.
	 */
	public String getLoginTime() {
		if (getString(LOGIN_TIME) == null) return "";
		return FormatUtil.getFormattedDate(getString(LOGIN_TIME), "yyyyMMddHHmmss",
				"MM월dd일 HH시mm분");
	}

	/**
	 * @param loginTime The loginTime을 세팅합니다.
	 */
	public void setLoginTime(String loginTime) {
		put(LOGIN_TIME, loginTime);
	}
	

    public HttpSession getSession() {
        return session;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public Role[] getRoles() {
        return roles;
    }

    public void setRoles(Role[] roles) {
        this.roles = roles;
    }
    
    /**
     * Root 관리자 권한 가지고 있는지 체크 하는 함수
     */
    public boolean isRootAdmin(){
        /*
        if(role == null ) return false;
        return role.getRoleId().equals(
                PropertyManager.getProperty("web_config","ROLE.ROOT_ROLE_ID",null));
        */
        return false;
    }
    
    /** 
     * 설명 : 세션에 등록 될 때 호출 되는 메소드
     * @see javax.servlet.http.HttpSessionBindingListener#valueBound(javax.servlet.http.HttpSessionBindingEvent)
     */
    public void valueBound(HttpSessionBindingEvent bindEvent) {
        LogManager.debug("USER_ACCESS","LOGIN:"+toString());
        setSession(bindEvent.getSession());
        SessionManager.addLoginedUser(getContextName(),this, bindEvent.getSession());
    }

    /** 
     * 설명 : 세션에서 삭제 될때 호출 되는 메소드
     * @see javax.servlet.http.HttpSessionBindingListener#valueUnbound(javax.servlet.http.HttpSessionBindingEvent)
     */
    public void valueUnbound(HttpSessionBindingEvent unbindEvent) {
        LogManager.debug("USER_ACCESS","LOGOUT:"+toString());
        SessionManager.removeLoginedUser(getContextName(),this);
    }
    
    /* (non-Javadoc)
	 * @see java.util.AbstractMap#toString()
	 */
	public String toString() {
		return "ID:"+getUserId()+",Name:"+getUserName()+",IP:"+getUserIp();
	}
	
	/**
	 * @return
	 */
	public String toHtml(){
		return "<tr bgcolor=white><td>"+getUserId()+"</td><td>"+getUserName()+"</td><td>"+getUserIp()+"</td><td>"+getLoginTime()+"</td></tr>";
	}
	/**
	 * 동시 접속자에 의해 dirty로 마킹된 세션인지 여부 리턴
	 * @return true when dirty
	 */
	public boolean isDirty() {
		return dirty;
	}
	/**
	 * mark as dirty
	 * @param dirty
	 */
	public void markDirty(){
		this.dirty=true;
	}	

/***	
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}
***/	
}

 

