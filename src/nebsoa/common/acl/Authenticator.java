/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.acl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import nebsoa.common.log.LogManager;
import nebsoa.common.util.PropertyManager;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 인증처리 모듈을 만들때 기본적으로 상속받을 클래스입니다. 
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
 * $Log: Authenticator.java,v $
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
 * Revision 1.3  2008/05/30 01:55:12  김재범
 * *** empty log message ***
 *
 * Revision 1.2  2008/05/28 14:12:28  김재범
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:23  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:38  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2007/01/31 13:26:40  이종원
 * 최초작성
 *
 *
 * 
 * </pre>
 ******************************************************************/
public abstract class Authenticator {
    /**
     * 업무에 맞게 구현해야 할 메소드    
     * @param request
     * @return UserInfo
     */
    public abstract UserSession executeLogin(String id, String password,
            HttpServletRequest request) throws Exception;
    
    /** 
     * 현재 사용자의 session.invalidate()메소드를 호출하도록 구현되어 있다.
     * 필요하다면 재정의 가능하다. 
     * @author 이종원
     */
    public void executeLogout(HttpServletRequest request){
		HttpSession session= request.getSession();
		session.removeAttribute(
		        PropertyManager.getProperty("web_config",
		        	"LOGIN.SESSION.BIND.NAME"));
		session.invalidate();	
    }
    
    /** 
     * 인자로 넘겨진 세션을 사용하는 사용자의 session.invalidate()메소드를 호출하도록 구현되어 있다.
     * 필요하다면 재정의 가능하다. 
     * @author 이종원
     */
    public void killSession(HttpSession session){
        try{
			session.removeAttribute(
			        PropertyManager.getProperty("web_config",
			        	"LOGIN.SESSION.BIND.NAME"));
			session.invalidate();	
        }catch(Exception e){
            
        }
    }
    
	/**
	 * 세션 타임아웃 시간을 읽어온다.
	 */
    public void readSessionTimeout(HttpServletRequest request) throws Exception {
		int sessionTimeout = PropertyManager.getIntProperty("web_config",
				"LOGIN.SESSION.TIMEOUT", 30);
		LogManager.debug("Session Timeout : " + sessionTimeout + " min.");
		sessionTimeout *= 60;
		request.getSession().setMaxInactiveInterval(sessionTimeout);
	}
}
