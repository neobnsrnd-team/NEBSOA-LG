/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package test.collection;

import java.util.ArrayList;

import nebsoa.biz.base.SpiderBaseTestCase;
import nebsoa.common.collection.DataSet;
import nebsoa.common.log.LogManager;

/*******************************************************************
 * <pre>
 * 1.설명 
 * TODO AccountListTest 클래스에 대한 주석 넣기
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
 * $Log: DataSetTest.java,v $
 * Revision 1.1  2018/01/15 03:39:54  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:36  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:27  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:29  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.2  2008/02/20 00:42:49  오재훈
 * *** empty log message ***
 *
 * Revision 1.1  2007/11/26 08:39:12  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2007/07/30 03:11:09  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2006/10/26 02:43:56  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class DataSetTest extends SpiderBaseTestCase {
	
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
	 * Test method for {@link test.account.AccountListBiz#execute(nebsoa.common.util.DataMap)}.
	 */
	public void testExecute() throws Exception {
        String cols[] = {
                "A",
                "B"
        };
        ArrayList data = new ArrayList(5);
        for (int i = 0; i < 5; i++) {
	        String obj[] = {
	                "A"+i,
	                "B"+i
	        };
            data.add(obj);
        }
        
        DataSet ds = new DataSet(cols, data);
        LogManager.debug(ds);
        ds.initRow();
        int i = 0;
        while (ds.next()) {
            ds.setValue(1, "AAA"+i);
            ds.setValue(2, "AAC"+i++);
            ds.setValue("B", "BBB"+i);
            ds.setValue("A", "BBC"+i++);
        }
        ds.initRow();
        LogManager.debug(ds);
	}

}
