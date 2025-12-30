/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package test.jdbc;
import java.util.ArrayList;

import junit.framework.TestCase;
import nebsoa.common.Context;
import nebsoa.common.collection.DataSet;
import nebsoa.common.jdbc.DBHandler;

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
 * $Log: DBTest.java,v $
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
 * Revision 1.2  2006/11/21 05:52:23  이종원
 * *** empty log message ***
 *
 * Revision 1.1  2006/07/21 06:30:36  김성균
 * 최초 등록
 *
 * </pre>
 ******************************************************************/
public class DBTest extends TestCase {
	
	Context ctx = null;

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		ctx = new Context();
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	/**
	 * 2006. 07. 21 김성균
	 * 한글테이블 테스트
	 * @throws Exception
	 */
	public void testQuery() throws Exception {
		String insert = "insert into 코드 values ('FRS001', '시스템오류')";
		String truncate = "truncate table 코드";
		String select = "select * from 코드";
		ArrayList param = new ArrayList(0);
		DBHandler.executeUpdate(ctx, truncate);
		DBHandler.executePreparedUpdate(ctx, insert, param);
		DataSet ds = DBHandler.executePreparedQuery(ctx, select, param);
		ds.next();
		assertEquals("FRS001", ds.getString("코드ID"));
		assertEquals("시스템오류", ds.getString("코드값"));
	}

}
