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
import javax.servlet.http.HttpSession;

import nebsoa.common.Constants;
import nebsoa.common.acl.Authorizer;
import nebsoa.common.acl.Role;
import nebsoa.common.acl.UserSession;
import nebsoa.common.collection.DataSet;
import nebsoa.common.exception.AuthFailException;
import nebsoa.common.exception.LoginException;
import nebsoa.common.jdbc.DBManager;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.PropertyManager;
import nebsoa.common.util.RequestUtil;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 관리자사이트의 기본적인 권한 체크 로직을 담당하는 클래스입니다. 
 * 
 * 2.사용법
 * 사용자의 권한 체크 로직을 구현한다.
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
 * $Log: DefaultAdminAuthorizer.java,v $
 * Revision 1.1  2018/01/15 03:39:49  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:37  cvs
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
 * Revision 1.2  2008/02/14 01:35:03  오재훈
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
 * Revision 1.1  2007/01/31 13:26:52  이종원
 * 최초작성
 *
 * Revision 1.1  2007/01/22 15:39:54  이종원
 * 최초작성
 *
 * </pre>
 ******************************************************************/
public class DefaultAdminAuthorizer extends Authorizer {

    /**
     * 권한을 체크한다.
     * @throws LoginException
     * @throws Exception
     * @return true : 권한 있음 / false : 권한 없음
     */
    public boolean isUserInRole(HttpServletRequest request){
        boolean hasRole = true;

    	LogManager.debug(request.getRequestURI()+" 관리자 권한체크 시작");
    	
    	HttpSession session = request.getSession();
    	
    	UserSession user = (UserSession)session.getAttribute(
    	        PropertyManager.getProperty("web_config","LOGIN.SESSION.BIND.NAME"));
    	
//    	String url = request.getRequestURI();
//    	String contextName = request.getContextPath();
//    	
//    	if(url.indexOf(contextName)> -1 ){
//    		url = url.substring(url.lastIndexOf(contextName)+contextName.length()+1);
//    	}
  
    	String url = RequestUtil.getUrlPath(request);

    	LogManager.debug("### url="+url);
    	
    	Object[] param = new Object[]{user.getUserId(),url};
    	
    	String sql = PropertyManager.getProperty("web_config","ADMIN.ROLE.QUERY");
    	
    	DataSet ds = DBManager.executePreparedQuery(Constants.SPIDER_DB, sql,param);

    	String auth_code="";
    	if(ds.next()){
    		//읽기 권한(R)인지 쓰기 권한(W)인지.
    		auth_code = ds.getString(1);
    		user.setAuthLevel(auth_code);
    	} else {
    		//사용 불가 메뉴
    		hasRole = false;
    	}

    	LogManager.debug("관리자 권한 여부="+hasRole+",AuthCode:"+auth_code);
        return hasRole;
    }

    public boolean isUserInRole(UserSession user, Role role) throws AuthFailException {
        return true;
    }
}
