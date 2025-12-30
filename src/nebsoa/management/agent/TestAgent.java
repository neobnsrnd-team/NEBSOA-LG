/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.management.agent;

import java.util.Enumeration;

import junit.framework.TestFailure;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import nebsoa.biz.base.SpiderBaseTestCase;
import nebsoa.common.exception.ManagementException;
import nebsoa.common.exception.TestException;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.DataMap;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 관리객체의 Agent 기능을 수행하는 클래스
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
 * $Log: TestAgent.java,v $
 * Revision 1.1  2018/01/15 03:39:49  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:34  cvs
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
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:35  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:45  안경아
 * *** empty log message ***
 *
 * Revision 1.6  2007/05/18 02:04:18  오재훈
 * keb 디펜던시를 제외하기 위해 BaseTestCase를 SpiderBaseTestCase 클래스로 변경
 *
 * Revision 1.5  2006/11/09 00:46:46  김성균
 * *** empty log message ***
 *
 * Revision 1.4  2006/11/08 07:40:39  김성균
 * *** empty log message ***
 *
 * Revision 1.3  2006/11/08 02:34:07  김성균
 * *** empty log message ***
 *
 * Revision 1.2  2006/11/07 11:04:42  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2006/09/28 05:31:29  김성균
 * *** empty log message ***
 *
 * Revision 1.3  2006/08/16 02:48:54  김성균
 * DataMap argument 추가
 *
 * Revision 1.2  2006/07/26 08:01:58  김성균
 * reflection 으로 처리하도록 수정
 *
 * Revision 1.1  2006/07/21 07:36:54  김성균
 * 최초 등록
 *
 * </pre>
 ******************************************************************/
public class TestAgent {

	/**
     * 테스트클래스를 수행한다. 
	 * @param map
	 * @return map
	 * @throws ManagementException
	 */
	public static DataMap doTest(DataMap map) throws TestException {
        try {
            String testClassName = map.getParameter("TEST_CLASS_NAME");
            Class testClass = Class.forName(testClassName);
            TestSuite testSuite =  new TestSuite(testClass);
            TestResult tr = TestRunner.run(testSuite);
            LogManager.debug("### Result = " + tr.wasSuccessful());
            LogManager.debug("### Tests run: " + tr.runCount());
            LogManager.debug("### Failures: " + tr.failureCount());
            LogManager.debug("### Errors: " + tr.errorCount());
            
            SpiderBaseTestCase test = (SpiderBaseTestCase) testSuite.testAt(0);
            map = test.getDataMap();
            map.put("WAS_SUCCESSFUL", tr.wasSuccessful());
            map.put("TESTS_RUN", tr.runCount());
            map.put("FAILURES", tr.failureCount());
            map.put("ERRORS", tr.errorCount());
            
            Enumeration error = tr.errors();
            while (error.hasMoreElements()) {
                TestFailure tf = (TestFailure) error.nextElement();
//                LogManager.debug(tf.exceptionMessage());
//                LogManager.debug(tf.toString());
//                LogManager.debug(tf.trace());
                Throwable e = tf.thrownException();
                LogManager.error(e);
                map.put("EXCEPTION", e);
            }
            
        } catch (ClassNotFoundException e) {
            throw new TestException("해당하는 클래스를 찾지 못했습니다.");
        }
        
		return map;
	}
}
