/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.acl;

import java.util.HashMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import nebsoa.common.acl.impl.DefaultAuthenticator;
import nebsoa.common.acl.impl.DefaultAuthorizer;
import nebsoa.common.exception.LoginException;
import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.FormatUtil;
import nebsoa.common.util.PropertyManager;
import nebsoa.common.util.RequestUtil;
import nebsoa.common.util.StringUtil;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 현재 로그인 한 사용자의 정보를 쉽게 가져다 쓸수 있는 메소드의 모음입니다. 
 * 로그인 여부, 사용자 등급, 이름 주민번호, 메일 등등....
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
 * $Log: SessionManager.java,v $
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
 * Revision 1.1  2008/08/04 08:54:51  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.7  2008/07/01 08:37:39  이종원
 * 동시 접속자 체크 로직 추가
 *
 * Revision 1.6  2008/05/30 01:55:12  김재범
 * *** empty log message ***
 *
 * Revision 1.5  2008/05/28 14:12:16  김재범
 * 주민등록 번호로 로그인 하는 함수 추가
 *
 * Revision 1.4  2008/02/21 07:22:32  오재훈
 * *** empty log message ***
 *
 * Revision 1.3  2008/02/14 07:56:04  오재훈
 * *** empty log message ***
 *
 * Revision 1.2  2008/02/14 04:34:29  오재훈
 * web.login.Authenticator, Authorizor 클래스들과 web.session.SessionManager,UserInfo 들이 삭제되고 common.acl 패키지의 클래스들로 대체되었습니다.
 * web.util.Request.Util 클래스도 common.util.RequestUtil로 옴겨졌습니다.
 *
 * Revision 1.1  2008/01/22 05:58:23  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:38  안경아
 * *** empty log message ***
 *
 * Revision 1.3  2007/11/07 06:09:52  김성균
 * 로그인 하지 않았을 경우 getUserId에서 null을 리턴하도록 수정
 *
 * Revision 1.2  2007/06/18 11:13:52  김성균
 * authenticator, authorizer 클래스 키값가져오는 부분 수정
 *
 * Revision 1.1  2007/02/01 06:47:59  이종원
 * *** empty log message ***
 *
 * Revision 1.1  2007/01/31 13:26:40  이종원
 * 최초작성
 *
 * </pre>
 ******************************************************************/
public class SessionManager {
    
    private static final String lock = "dummy";
    
    /**
     * 로그인 사용자 수
     */
    private static int accessUserCnt = 0;
    
    /**
     * 언어코드
     */
    private static final String LOCALE_CODE = "LOCALE_CODE" ;
    
    /**
     * 디폴트언어코드
     */
    public static final String DEFAULT_LOCALE_CODE = "KO" ;
    
	/**
	 * 인증클래스 
	 */
	private static HashMap authenticatorMap = new HashMap();
    

	/**
	 * 작성일자 : 2008. 02. 13 
	 * 작성자 : 오재훈
	 * 설명 : Login처리를 해 주는 Authenticator instance를 얻기 위한 메소드이며
	 * web_config.properties.xml에 Authenticator.class=com.mycom.login.MyAuthenticator 처럼
	 * 각자 구현한 Authenticator.class 바꾸어 등록하여 사용 할 수 있다.
	 * 만약 지정하지 않으면 제공되는 DefaultAuthenticator가 사용된다.
	 * DefaultAuthenticator의 구현부 소스는 web/samples/DefaultAuthenticator.java.txt파일을 참조한다.
	 * @param key
	 * @return
	 */
	private static Authenticator getAuthenticator(String key){
        if (key == null) return null;
//        if (key.endsWith(".")) key = key + ".";
        if (authenticatorMap.get(key) != null) {
            return (Authenticator) authenticatorMap.get(key);
        }
        
	    LogManager.debug("CREATE new Authenticator INSTANCE with key:"+key);
        Authenticator authenticator=null;
        synchronized(lock){            
            String authenticatorClassKey = key + "." + "Authenticator.class";

            LogManager.debug("authenticatorClassKey=" + authenticatorClassKey);
            
	        String authClassName = PropertyManager.getProperty("web_config", authenticatorClassKey, null);
            
            if(!StringUtil.isNull(authClassName)){
	            try {
                    authenticator = (Authenticator) Class.forName(authClassName).newInstance();
                    //web_config에 있을때만 캐쉬 nebsoa.common.acl.impl.DefaultAuthenticator 캐쉬하지 않음.
                    authenticatorMap.put(key, authenticator);
                } catch (InstantiationException e1) {
                   throw new SysException("[CANN'T MAKE INSTANCE 생성자 체크 :"+authClassName+"]");
                } catch (IllegalAccessException e1) {
                   throw new SysException("[CANN'T MAKE INSTANCE 생성자 PUBLIC 인지 체크 :"+authClassName+"]");
                }catch(ClassNotFoundException e){
                   throw new SysException("[CLASS_NOT_FOUND:"+e.getMessage()+"]");
                }
	        }else{
                LogManager.debug("등록된 Authenticator가 없어 DefaultAuthenticator 생성함.");
	            authenticator = new DefaultAuthenticator();
	        }
	    }
        LogManager.debug("Authenticator : "+authenticator.getClass().getName());
	    return authenticator;
	}
	
	/**
	 * 권한클래스 
	 */
	private static HashMap authorizerMap=new HashMap();
	
    /**
     * 작성일자 : 2008. 02. 13 
     * 작성자 : kfetus
     * 설명 : 권한체크를 처리 해 주는 Authorizer instance를 얻기 위한 메소드이며
	 * web_config.properties.xml에 Authorizer.class=com.mycom.login.MyAuthorizer 처럼
	 * 각자 구현한 Authorizer.class로 바꾸어 등록하여 사용 할 수 있다.
	 * 만약 지정하지 않으면 제공되는 DefaultAuthorizer가 사용된다.
	 * DefaultAuthorizer의 구현부 소스는 web/samples/DefaultAuthrizer.java.txt 파일을 참조한다.
     * @param key
     * @return
     */
    private static Authorizer getAuthorizer(String key){
        if (key == null) return null;
//        if (key.endsWith(".")) key = key + ".";
        
	    if (authorizerMap.get(key) != null) {
            return (Authorizer) authorizerMap.get(key);
        }
        LogManager.debug("CREATE Authorizer INSTANCE with key:" + key);
        
        Authorizer authorizer=null;
        
	    synchronized(lock){
        
	        String authorizerClassKey = key + "." + "Authorizer.class";

            LogManager.debug("authorizerClassKey=" + authorizerClassKey);
	        
	        String authClassName = PropertyManager.getProperty("web_config",authorizerClassKey,null);
	        if(!StringUtil.isNull(authClassName)){
	            try {
	                authorizer = (Authorizer) Class.forName(authClassName).newInstance();
	                //web_config에 있을때만 캐쉬 nebsoa.common.acl.impl.DefaultAuthorizer 캐쉬하지 않음.
	                authorizerMap.put(key, authorizer);
	            } catch (InstantiationException e1) {
	               throw new SysException("[CANN'T MAKE INSTANCE 생성자 체크 :"+authClassName+"]");
	            } catch (IllegalAccessException e1) {
	               throw new SysException("[CANN'T MAKE INSTANCE 생성자 PUBLIC 인지 체크 :"+authClassName+"]");
	            }catch(ClassNotFoundException e){
	               throw new SysException("[CLASS_NOT_FOUND:"+e.getMessage()+"]");
	            }
	        }else{
                LogManager.debug("등록된 Authorizer 없어 DefaultAuthorizer 생성함.");
	            authorizer = new DefaultAuthorizer();
	        }
        }
        LogManager.debug("Authorizer : "+authorizer.getClass().getName());
	    return authorizer;
	}	
	
	/**
	 * 로그인 한 사용자 목록
	 */
	static TreeMap userList;
	
	/**
     * 현재 로그인 해 있는 사용자 목록을 리턴합니다.
     * 성능에 문제가 될 수 있으므로 client용 site에서는 사용하지 않습니다. 
     * @return
	 */
	public static TreeMap getUserList() {
		if(userList == null){
			userList =  new TreeMap();
		}
		return userList;
	}
	
	/**
	 *  설명: 로그인한 유저를 등록 합니다.
	 * 이미 같은 ID로 로그인 해 있는 사용자가 있으면 세션에서 삭제 후 신규 등록
	 * @param user
	 */
	public static void addLoginedUser(String domain,UserSession user, HttpSession session){
        if (isSessionMonitorMode()) {
            LogManager.debug("isSessionMonitorMode is ON !!! disable this property on Real env");
		    String userId = user.getUserId();
		    if(getUserList().containsKey(userId)){
		        UserSession oldUser = (UserSession) getUserList().get(userId);
		        if(!session.equals(oldUser.getSession())){
		            LogManager.info("이미 같은 user가 로그인 해 있습니다.:"+oldUser.toString());
		            //killSession(domain,oldUser.getSession());
		            oldUser.markDirty();
		        }
		    }
		    getUserList().put(userId,user); 
        }
        addAccessUserCnt();
	}


    /**
	 * kill user Session
     * @param session
     */
    public static void killSession(String domain,HttpSession session) {
        getAuthenticator(domain).killSession(session);
    }

    /**
	 * 
	 * 2004. 9. 16. 이종원 작성
	 * @param user
	 * 설명: 로그인한 사용자가 로그아웃 할 때 로그아웃된 사용자 정보를 삭제 합니다.
	 */
	public static void removeLoginedUser(String domain,UserSession user){
        if(isSessionMonitorMode()){
            getUserList().remove(domain+"$"+user.getUserId());
        }
        removeAccessUserCnt();
	}
	
  	/**
  	* 사용자 정보를 담고 있는 UserInfo객체를 얻어냅니다.
  	*/
  	public static UserSession getUserSession(HttpServletRequest request){
    	HttpSession session = request.getSession();
    	UserSession user = (UserSession)session.getAttribute(
    	        PropertyManager.getProperty("web_config","LOGIN.SESSION.BIND.NAME"));
    	return user;
  	}

  	/**
  	* 사용자 정보를 담고 있는 UserInfo객체를 얻어냅니다.
  	*/
  	public static UserSession getUserInfo(HttpServletRequest request){
    	HttpSession session = request.getSession();
    	UserSession user = (UserSession)session.getAttribute(
    	        PropertyManager.getProperty("web_config","LOGIN.SESSION.BIND.NAME"));
    	return user;
  	}
  	
  	
  	/**
  	* 로그인여부를 체크.
  	*/
  	public static boolean isLogin(HttpServletRequest request) {

        UserSession user = getUserSession(request);

        if (user == null)
            return false;
        return true;
  	}

    /**
     * 작성일자 : 2008. 02. 11 
     * 작성자 : 오재훈
     * 설명 : 사용자 정보를 담고 있는 UserInfo객체를 얻어냅니다.
     * @param session
     * @return UserInfo
     */
    public static UserSession getUserSession(HttpSession session) {
    	UserSession user = (UserSession)session.getAttribute(
                PropertyManager.getProperty("web_config", "LOGIN.SESSION.BIND.NAME"));
        return user;
    }

    /**
     * 작성일자 : 2008. 02. 11 
     * 작성자 : 오재훈
     * 설명 : 로그인 여부를 체크
     * @param session
     * @return boolean
     */
  	public static boolean isLogin(HttpSession session) {
  		UserSession user = getUserSession(session);
        if (user == null)
            return false;
        return true;
  	}

  	
  	/**
  	* 로그인한 사용자의 id를 얻어 냅니다.
  	 * @throws LoginException 
  	*/
	public static  String getUserId(HttpServletRequest request) {
	    UserSession user = getUserSession(request);
		if(user==null) {
		    return null;
        }
	    return user.getUserId();
	}
	/**
	* 로그인한 사용자의 주민번호를  얻어 냅니다.
	*/
	public static  String getJuminNo(HttpServletRequest request) {
	    UserSession user = getUserSession(request);
		if(user==null) return null;
	    return user.getJuminNo();
	}
	/**
	 * 로그인한 사용자의 주민번호를  얻어 냅니다.
	 */
	public static  String getSsn(HttpServletRequest request) {
	    return getJuminNo(request);
	}

	/**
	 * 로그인한 사용자의 이름을 얻어 냅니다.
	 */
	public static  String getUserName(HttpServletRequest request) {
	    UserSession user = getUserSession(request);
		if(user==null) return null;
	    return user.getUserName();
	}

	/**
	 * 로그인한 사용자의 이메일을 얻어 냅니다.
	 */
	public static  String getEmail(HttpServletRequest request) {
		UserSession user = getUserSession(request);
		if(user==null) return null;
	    return user.getEmail();
	}	
	
    /**
     * 2006. 07. 17 김성균
     * 로그인한 사용자의 언어코드값을 얻어 냅니다.
     * 로그인하지 않았을 경우 세션으로부터 언어코드값을 얻어 옵니다.
     * @param request
     * @return
     */
    public static String getLocaleCode(HttpServletRequest request) {
    	String localeCode = null;
    	/* UserInfo에서 가져올 경우 사용
		UserSession user = getUserSession(request);
		
		if (user == null) {
			localeCode = getSessionAttribute(request, LOCALE_CODE);
		} else {
			localeCode = user.getLocaleCode();
		}
		*/
		localeCode = getSessionAttribute(request, LOCALE_CODE);
		
		if (localeCode == null) localeCode = DEFAULT_LOCALE_CODE;

		return localeCode;
    }



    
    
    
	/**
	 * 로그인한 사용자의 권한(Read,Write)을 얻어 냅니다.
	 */
	public static String getAuthLevel(HttpServletRequest request) {
	    UserSession user = getUserSession(request);
		if(user==null) return null;
	    return user.getAuthLevel();
	}    
    
	
	/**
	 * 로그인한 사용자가 ROOT 관리자  인지를  얻어 냅니다.
	 */
	public static  boolean isRootAdmin(HttpServletRequest request){
		UserSession userInfo = getUserSession(request);
		if(userInfo==null) return false;
		return userInfo.isRootAdmin();
	}

    /**
     * 세션에서 사용자 정보를 제거 한다.
     * @param request
     */
    public static void unbindUserSessionFromHttpSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute(
                PropertyManager.getProperty("web_config","LOGIN.SESSION.BIND.NAME"));
    }

    /**
     * 세션에 사용자 정보를 등록 한다
     * @param request
     * @param user
     * @throws Exception
     */
    public static void bindUserSessionToHttpSession(HttpServletRequest request, UserSession user) throws Exception {
        if (user == null)
            throw new Exception("바인딩할 USER 정보가 NULL입니다.");
        HttpSession session = request.getSession();
        if(session != null) user.setSession(session);
        session.setAttribute(PropertyManager.getProperty("web_config", "LOGIN.SESSION.BIND.NAME"), user);
    }
    
    /**
     * login 처리를 한다. 로그인 처리 모듈을 커스터 마이징 하려면 Authenticator를 상속받아 
     * Authenticator구현한 클래스를 Authenticator.class로 web_config property 에 등록하면 된다. 
     * @param id
     * @param pw
     * @param request
     * @return UserInfo
     * @throws Exception
     */
    public static UserSession executeLogin(String domain, String id, String pw, HttpServletRequest request) throws Exception{
        
        if (isLogin(request)) {
            unbindUserSessionFromHttpSession(request);
        }
        
        UserSession user = getAuthenticator(domain).executeLogin(id, pw, request);
        getAuthenticator(domain).readSessionTimeout(request);
        
        if (user != null) {
            user.setUserIp(request.getRemoteAddr());
            user.setLoginTime(FormatUtil.getToday("yyyyMMddHHmmss"));
            user.setContextName(RequestUtil.getContextName(request) );
            
            bindUserSessionToHttpSession(request, user);
//            addLoginedUser(user, request.getSession());
        }
        
        return user;
    }
    
   
   /**
    * 주민등록 번호로 login 처리를 한다. 로그인 처리 모듈을 커스터 마이징 하려면 Authenticator를 상속받아 
    * Authenticator구현한 클래스를 Authenticator.class로 web_config property 에 등록하면 된다. 
    * @param id
    * @param pw
    * @param request
    * @return UserInfo
    * @throws Exception
    */
   public static UserSession executeLogin(String domain, String userSsn , HttpServletRequest request) throws Exception{
        
        if (isLogin(request)) {
            unbindUserSessionFromHttpSession(request);
        }
        
        UserSession user = getAuthenticator(domain+"_userSsn").executeLogin("", userSsn, request);
        getAuthenticator(domain).readSessionTimeout(request);
        
        if (user != null) {
            user.setUserIp(request.getRemoteAddr());
            user.setLoginTime(FormatUtil.getToday("yyyyMMddHHmmss"));
            user.setContextName(RequestUtil.getContextName(request) );
            
            bindUserSessionToHttpSession(request, user);
//            addLoginedUser(user, request.getSession());
        }
        
        return user;
    }
   
    /**
     * 세션 모니터링 여부
     * @return
     */
    public static boolean isSessionMonitorMode() {
        //운영시에는 성능 문제가 될 수 있으므로 false로 세팅 한다.
        return PropertyManager.getBooleanProperty("web_config","SESSION.MONITOR.MODE","OFF");
    }

    /**
     * logout 처리를 한다. logout 처리 모듈을 커스터 마이징 하려면 Authenticator를 상속받아 
     * Authenticator구현한 클래스를 Authenticator.class로 web_config property 에 등록하면 된다. 
     * @param id
     * @param pw
     * @param request
     * @return UserInfo
     * @throws Exception
     */
    public static void executeLogout(String domain,HttpServletRequest request){
        getAuthenticator(domain).executeLogout(request);
        UserSession user = getUserSession(request);
        if (user != null) {
            removeLoginedUser(domain,user);
        }
    }

    /**
     * 권한 체크를 한다. getAuthorizer().isUserInRole(request)의 결과값을 리턴한다.
     * @param request
     * @return true if hasRole , false if do not has Role
     * @throws Exception
     */
    public static boolean hasRole(String domain,HttpServletRequest request) throws Exception {        
        return getAuthorizer(domain).isUserInRole(request);
    }
    
    /**
     * 2006. 07. 17 김성균
     * @param request
     * @param attr
     * @return
     */
    public static String getSessionAttribute(HttpServletRequest request, String attr){
    	HttpSession session = request.getSession();
    	return (String) session.getAttribute(attr);
  	}

    /**
     * @return Returns the accessUserCnt.
     */
    public static int getAccessUserCnt() {
        return accessUserCnt;
    }

    /**
     * 로그인 사용자 수를 증가시킨다.
     */
    public static void addAccessUserCnt() {
    	synchronized (lock) {
            ++accessUserCnt;			
		}
    }
    
    /**
     * 로그인 사용자 수를 감소시킨다.
     */
    public static void removeAccessUserCnt() {
    	synchronized (lock) {
            --accessUserCnt;
            if(accessUserCnt<0) accessUserCnt=0;
		}
    }
    
    /**
     * 로그인한 사용자의 로그사용여부를 리턴한다. 
     * 
     * @throws LoginException
     */
    public static boolean isLogEnabled(HttpServletRequest request) throws LoginException {
    	UserSession user = getUserSession(request);
        if (user == null) {
            throw new LoginException();
        }
        return user.isLogEnabled();
    }
    
	public void checkDupUser(String domain, String userId,String sessionId ){
		if(userId==null || domain==null ) {
			LogManager.debug("domainId or User id is null --> "+domain+","+userId);
			return;
		}
		UserSession existSession = (UserSession) getUserList().get(domain+"$"+userId);
		if(existSession==null) {
			LogManager.debug("여기에는 존재 하지 않는 사용자 --> "+userId);
		}
		HttpSession session = existSession.getSession();
		if(session != null && session.getId().equals(sessionId)){
			LogManager.debug("내가 보낸 메시지... skip --> "+userId+",session Id:"+sessionId);
		}
		//이놈은 DIRTY...
		existSession.markDirty();
	}
}
