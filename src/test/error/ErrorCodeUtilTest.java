/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package test.error;

import junit.framework.TestCase;
import nebsoa.common.error.ErrorCodeUtil;

/*******************************************************************
 * <pre>
 * 1.설명 
 * TODO ErrorCodeUtilTest 클래스에 대한 주석 넣기
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
 * $Log: ErrorCodeUtilTest.java,v $
 * Revision 1.1  2018/01/15 03:39:52  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:10  cvs
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
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.2  2008/02/20 00:42:48  오재훈
 * *** empty log message ***
 *
 * Revision 1.1  2007/11/26 08:39:13  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2006/08/03 05:17:03  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2006/07/31 11:25:07  김성균
 * 최초 등록
 *
 * </pre>
 ******************************************************************/
public class ErrorCodeUtilTest extends TestCase {
    
    String localeCode = "KO";

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test method for {@link nebsoa.common.error.ErrorCodeUtil#getErrorTitle(java.lang.String, java.lang.String)}.
     */
    public void testGetErrorTitle() {
        String title = ErrorCodeUtil.getInstance().getErrorTitle("FRM00001", localeCode);
        assertEquals("필수 입력 정보 누락 오류", title);
    }

    /**
     * Test method for {@link nebsoa.common.error.ErrorCodeUtil#getErrorCauseDesc(java.lang.String, java.lang.String)}.
     */
    public void testGetErrorCauseDesc() {
        String desc = ErrorCodeUtil.getInstance().getErrorCauseDesc("FRM00001", localeCode);
        assertEquals("필수 입력 정보 누락 오류", desc);
    }

    /**
     * Test method for {@link nebsoa.common.error.ErrorCodeUtil#getErrorGuideDesc(java.lang.String, java.lang.String)}.
     */
    public void testGetErrorGuideDesc() {
        String desc = ErrorCodeUtil.getInstance().getErrorGuideDesc("FRM00001", localeCode);
        assertEquals("필수 입력 정보 누락 오류", desc);
    }

}
