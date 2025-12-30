/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.acl.impl;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import nebsoa.common.Constants;
import nebsoa.common.acl.Authorizer;
import nebsoa.common.acl.SessionManager;
import nebsoa.common.acl.Role;
import nebsoa.common.acl.UserSession;
import nebsoa.common.exception.AuthFailException;
import nebsoa.common.exception.LoginException;
import nebsoa.common.jdbc.DBManager;
import nebsoa.common.jdbc.DBResultSet;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.PropertyManager;
import nebsoa.common.util.RequestUtil;
import nebsoa.common.config.WebAppMapping;

/*******************************************************************
 * <pre>
 * 1.설명 
 * spider web framework에서 기본적인 권한 체크 로직을 담당하는 클래스입니다. 
 * 
 * 2.사용법
 *   a. 로그인 체크를 하는 페이지 인지를 판단한다.
 *   : getRoleCheckPageList를 호출하여 얻어진 페이지 내에 이번 요청 페이지가 속해 있는지 판단한다.
 *   b. 권한 체크할 대상이 아니면 권한있음(true) 리턴
 *   c. 권한 체크 대상 이면 DB에서 사용자 ROLE이 해당 메뉴에 접근할 권한이 있는지 판단하여 BOOLEAN값 리턴
 *
 * <font color="red">
 * 3.주의사항
 * 현재 구현된 방식은 사용자가 하나의 role만 가지고 있고 그 role안에 필요한 메뉴 접근권한을
 * 부여 하는 구조(1:N)라고 가정하였다. 즉 한 사용자가 여러 role을 가지고 있지 않은 구조(N:N)이다.
 * </font>
 *
 * @author $Author: cvs $
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 
 * $Log: DefaultAuthorizer.java,v $
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
 * Revision 1.3  2008/02/14 10:30:33  오재훈
 * WebAppMapping 클래스가 ap단으로 이동됨
 *
 * Revision 1.2  2008/02/14 04:34:28  오재훈
 * web.login.Authenticator, Authorizor 클래스들과 web.session.SessionManager,UserInfo 들이 삭제되고 common.acl 패키지의 클래스들로 대체되었습니다.
 * web.util.Request.Util 클래스도 common.util.RequestUtil로 옴겨졌습니다.
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
 * Revision 1.3  2007/02/01 06:48:08  이종원
 * *** empty log message ***
 *
 * Revision 1.2  2007/02/01 06:46:19  이종원
 * *** empty log message ***
 *
 * Revision 1.1  2007/01/31 13:26:52  이종원
 * 최초작성
 *
 * Revision 1.6  2006/10/20 07:11:36  김성균
 * *** empty log message ***
 *
 * Revision 1.5  2006/07/18 00:11:19  김성균
 * *** empty log message ***
 *
 * Revision 1.4  2006/07/17 11:42:08  김성균
 * *** empty log message ***
 *
 * Revision 1.3  2006/06/23 07:35:58  김성균
 * 권한체크 로직 수정
 *
 * Revision 1.2  2006/06/17 09:42:09  최수종
 * *** 최초 등록 ***
 *
 * 
 * </pre>
 ******************************************************************/
public class DefaultAuthorizer extends Authorizer{

    /**
     * 권한을 체크한다.
     * @throws LoginException
     * @throws Exception
     * @return true : 권한 있음 / false : 권한 없음
     */
    public boolean isUserInRole(HttpServletRequest request) 
    throws AuthFailException, LoginException{

    	/*
        // 먼저 로그인 체크를 하는 페이지 인지를 판단한다.
		String uri = request.getRequestURI();

		// context path제거
		if (uri.startsWith(request.getContextPath())) {
			uri = uri.substring(request.getContextPath().length());
		}
		if (uri.startsWith("/")) {
			uri = uri.substring(1);
		}
		/*/
        // 먼저 로그인 체크를 하는 페이지 인지를 판단한다.
		String uri = RequestUtil.getUrlPath(request);
		LogManager.debug("권한 체크할 URL : [" + uri + "]");

		
		//2008-02-15 오재훈 ap단 클래스 로더를 사용함
		// 권한 체크할 대상이 아니면 권한있음(true) 리턴
		boolean authCheckMenu = 
			WebAppMapping.getInstance().containsMenuUrl(uri);
		if (!authCheckMenu) {
			LogManager.debug("권한 체크 대상 아님 ==> 권한 있음 :  [" + uri + "]");
			return true;
		}
		
		/*
		ArrayList authCheckPageList = getRoleCheckPageList();
		// 권한 체크할 대상이 아니면 권한있음(true) 리턴
		if (authCheckPageList == null || !authCheckPageList.contains(uri)) {
			LogManager.debug("권한 체크 대상 아님 ==> 권한 있음 :  [" + uri + "]");
			return true;
		}
		*/

		// 그렇지 않다면 권한을 체크 한다.

		// 권한 체크를 하려면 당연히 로그인 해 있는 상태 이어야 한다.
		if (!SessionManager.isLogin(request)) {
			throw new LoginException();
		}

//		String roleId = SessionManager.getRoleId(request);
//		String[] param = new String[] { roleId, uri };

		boolean hasRole = true;

		
		//2008-02-14 오재훈 ap단 클래스 로더를 사용함
		String menuId = WebAppMapping.getInstance().getMenuId(uri);
		String[] param = new String[] { 
				SessionManager.getUserId(request),
				menuId 
		};
		String sql = PropertyManager.getProperty("web_config", "ROLE_CHECK.QUERY");

		// 사용자별 메뉴 테이블에 데이터가 있으면 true 없으면 false;
		DBResultSet rs = DBManager.executePreparedQuery(Constants.SPIDER_DB, sql, param);
		if (rs.next()) {
			hasRole = true;
			request.setAttribute("HAS_ROLE", rs.getString(1));
			LogManager.debug(rs.getString(1) + " 권한 있슴...");
		}
		
		return hasRole;
    }
    
    ArrayList roleCheckPageList;
    
    /**
	 * 권한 체크할 페이지 목록을 리턴한다. 캐쉬된 목록이 있으면 캐쉬된 것을 리턴하고 없으면 loadRoleCheckPageList()
	 * method를 호출 하여 loading한 후 return 한다. 이 목록에 속해 있지 않으면 권한 체크를 하지 않는다.
	 * 
	 * @return ArrayList of roleCheckPageList
	 */
    public ArrayList getRoleCheckPageList(){
        if(roleCheckPageList == null){
            roleCheckPageList = loadRoleCheckPageList(); 
        }
        return roleCheckPageList;
    }

    /**
     * 권한 체크할 페이지 목록을 Database에서 읽어 온다.
     * @return ArrayList
     */
    private ArrayList loadRoleCheckPageList() {
        ArrayList pageList = new ArrayList();
        try{
	        DBResultSet rs = DBManager.executeQuery(Constants.SPIDER_DB,
	    	        PropertyManager.getProperty("web_config","ROLE_CHECK_PAGE_LIST.QUERY"));
	        
	        while(rs.next()){
	            pageList.add(rs.getString(1));
	        }
	        LogManager.debug("권한 체크 대상  페이지  목록: "+pageList);
        }catch(Exception e){
            LogManager.info("권한 체크 대상  페이지  목록 로딩 실패 "+e.getMessage());
        }
        return pageList;
    }

    public boolean isUserInRole(UserSession user, Role role) throws AuthFailException {
        return true;
    }
    
}
