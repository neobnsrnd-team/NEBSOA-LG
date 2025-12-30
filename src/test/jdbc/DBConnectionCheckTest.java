/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package test.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import junit.framework.TestCase;
import nebsoa.common.exception.DBException;
import nebsoa.common.jdbc.DBManager;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 한글테이블 테스트
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
 * $Log: DBConnectionCheckTest.java,v $
 * Revision 1.1  2018/01/15 03:39:51  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:55  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:24  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:27  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:52  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.2  2008/02/20 00:42:47  오재훈
 * *** empty log message ***
 *
 * Revision 1.1  2007/11/26 08:38:30  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2006/10/23 06:05:29  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2006/07/21 06:30:36  김성균
 * 최초 등록
 *
 * </pre>
 ******************************************************************/
public class DBConnectionCheckTest extends TestCase {
	
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
	 * @throws Exception
	 */
	public void testQuery() throws Exception {
		Connection[] con = null;
        try {
            con = new Connection[50];
            for (int i = 0; i < 50; i++) {
                con[i] = DBManager.getConnection();
                System.out.println("con.getAutoCommit():"+con[i].getAutoCommit());
                con[i].commit();
                con[i].setAutoCommit(true);
            }
        } catch (DBException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        for (int i = 0; i < 50; i++) {
            if (con[i] == null) {
                break; 
            }
            DBManager.close(con[i]);
        }
	}

}
