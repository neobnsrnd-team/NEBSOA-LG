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
import nebsoa.common.acl.UserSession;
import nebsoa.common.exception.LoginException;
import nebsoa.common.jdbc.DBManager;
import nebsoa.common.util.PropertyManager;
import nebsoa.common.util.StringUtil;


/*******************************************************************
 * <pre>
 * 1.설명 
 * 관리자사이트에서 주민번호로  로그인 처리를 담당하는 클래스입니다.
 * 
 * 2.사용법
 *
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author 김재범
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 최초작성
 *
 * Revision 1.1  2008/05/28 15:39:54  김재범
 * 최초작성
 *
 * </pre>
 ******************************************************************/
public class UserSsnAdminAuthenticator extends Authenticator{

    /**
     * 로그인 처리를 한다. 
     * @throws Exception
     */
    public UserSession executeLogin(String id, String userSsn,
            HttpServletRequest request) throws Exception {

        Object[] param = {userSsn};
        
        String query = PropertyManager.getProperty("web_config","USERSSN.LOGIN.QUERY");
        
    	UserSession user =(UserSession)DBManager.executePreparedQueryToBean(
    	        Constants.SPIDER_DB, query, param, UserSession.class);

    	if (user == null) {
            throw new Exception("등록되지 않은 사용자 ID 입니다");
        }
        // 비밀번호 체크
        //if (!user.getPassword().equals(StringUtil.digest(pw))) {
        //    throw new LoginException("비밀번호가 올바르지 않습니다");
        //}

        return user;
    }
}
