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

import nebsoa.common.exception.AuthFailException;
import nebsoa.common.exception.LoginException;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 권한 체크 처리 모듈을 만들때 기본적으로 상속받을 클래스입니다. 
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
 * $Log: Authorizer.java,v $
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
 * Revision 1.1  2008/01/22 05:58:23  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:39  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2007/01/31 13:26:40  이종원
 * 최초작성
 *
 * </pre>
 ******************************************************************/
public abstract class Authorizer {
    /**
     * 권한 체크 로직을 업무에 맞게 구현해야 할 메소드    
     * @param request
     * @return true if user has the role, false not has the role
     * @throws LoginException 
     * @throws Exception
     */
    public abstract boolean isUserInRole(HttpServletRequest request) 
    throws AuthFailException, LoginException;
    /**
     * 권한 체크 로직을 업무에 맞게 구현해야 할 메소드    
     * @param usersession
     * @param resourceId
     * @return true if user has the role, false not has the role
     * @throws Exception
     */
    public abstract boolean isUserInRole(UserSession user, Role role) 
    throws AuthFailException,LoginException;    
}
