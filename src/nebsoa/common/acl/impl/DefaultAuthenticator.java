/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.acl.impl;

import javax.servlet.http.HttpServletRequest;

import nebsoa.common.Constants;
import nebsoa.common.acl.Authenticator;
import nebsoa.common.acl.SessionManager;
import nebsoa.common.acl.UserSession;
import nebsoa.common.exception.LoginException;
import nebsoa.common.jdbc.DBManager;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.FormatUtil;
import nebsoa.common.util.PropertyManager;
import nebsoa.common.util.StringUtil;

/*******************************************************************
 * <pre>
 * 1.설명 
 * spider web framework에서 기본적인 로그인 처리를 담당하는 클래스입니다.
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
 * $Log: DefaultAuthenticator.java,v $
 * Revision 1.1  2018/01/15 03:39:49  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:38  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:23  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:27  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:55  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.3  2008/05/30 01:54:54  김재범
 * *** empty log message ***
 *
 * Revision 1.2  2008/05/28 14:12:42  김재범
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:34  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.2  2007/11/30 09:46:53  안경아
 * DB NAME 지정
 *
 * Revision 1.1  2007/11/26 08:38:55  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2007/02/01 06:48:08  이종원
 * *** empty log message ***
 *
 * Revision 1.1  2007/01/31 13:26:52  이종원
 * 최초작성
 *
 * Revision 1.4  2006/07/18 00:11:19  김성균
 * *** empty log message ***
 *
 * Revision 1.3  2006/07/17 11:42:08  김성균
 * *** empty log message ***
 *
 * Revision 1.2  2006/06/17 09:42:09  최수종
 * *** 최초 등록 ***
 *
 * 
 * </pre>
 ******************************************************************/
public class DefaultAuthenticator extends Authenticator{

    /**
     * 로그인 처리를 한다. 
     * @throws Exception
     */
    public UserSession executeLogin(String id, String pw,
            HttpServletRequest request) throws Exception {

        Object[] param = {id };
        
        String query = PropertyManager.getProperty("web_config","LOGIN.QUERY");
        
    	UserSession user =(UserSession)DBManager.executePreparedQueryToBean(
    			Constants.SPIDER_DB, query, param, UserSession.class);

    	if(user == null) {
    		throw new Exception("등록되지 않은 사용자 ID 입니다");
    	}	
    	//비밀번호 체크
    	if(!user.getPassword().equals(StringUtil.digest(pw))){
    	    LogManager.debug("StringUtil.digest(pw):" +StringUtil.digest(pw));
    	    LogManager.debug("user.getPassword():" +user.getPassword());
    		throw new LoginException("비밀번호가 올바르지 않습니다");
    	}

    	if(SessionManager.isLogin(request)){		
            SessionManager.unbindUserSessionFromHttpSession(request);
    	}
    	
    	String loginTime = FormatUtil.getToday("yyyyMMddHHmmss");
    	user.setLoginTime(loginTime);	
    		
    	SessionManager.bindUserSessionToHttpSession(request,user);
    	
		readSessionTimeout(request);
        
    	return user;
    }
}
