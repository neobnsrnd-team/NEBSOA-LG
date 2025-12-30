/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.base;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 세션빈을 만들 때 상속받을 클래스 
 * 기본적인 method를 구현하여 개발자는 business logic에 해당하는 메소드만
 * 구현하면 된다.
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
 * $Log: BaseSessionBean.java,v $
 * Revision 1.1  2018/01/15 03:39:50  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:39  cvs
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
 * Revision 1.1  2008/01/22 05:58:32  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:35  안경아
 * *** empty log message ***
 *
 * Revision 1.14  2006/08/19 00:45:39  김성균
 * *** empty log message ***
 *
 * Revision 1.13  2006/06/17 07:32:05  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class BaseSessionBean implements SessionBean {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -9144199077140048419L;

    public BaseSessionBean() {
    }

    public void ejbCreate() {
    }

    public void ejbActivate() {
    }

    public void ejbRemove() {
    }

    public void ejbPassivate() {
    }

    public void setSessionContext(SessionContext sessionContext) {
        context = sessionContext;
    }

    public SessionContext context;
}
