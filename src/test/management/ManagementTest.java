/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package test.management;

import junit.framework.TestCase;
import nebsoa.common.util.DataMap;
import nebsoa.management.agent.ManagementAgent;
import nebsoa.management.client.ManagementClient;

/*******************************************************************
 * <pre>
 * 1.설명 
 * ManagementClient를 테스트 한다. 
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
 * $Log: ManagementTest.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:30  cvs
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
 * Revision 1.1  2007/11/26 08:39:16  안경아
 * *** empty log message ***
 *
 * Revision 1.3  2006/08/16 02:47:57  김성균
 * *** empty log message ***
 *
 * Revision 1.2  2006/07/26 08:31:10  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2006/07/21 07:32:50  김성균
 * 최초 등록
 *
 * </pre>
 ******************************************************************/
public class ManagementTest extends TestCase {
    
    DataMap map = null;

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
        map = new DataMap();
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}
    
	public void testMessageAgent() throws Exception {
        map.put("COMMAND", "nebsoa.nebsoalink.context.MessageEngineContext.reloadAll");
		ManagementAgent.doProcess(map);
	}

	public void testReloadAll() throws Exception {
        map.put("COMMAND", "nebsoa.nebsoalink.context.MessageEngineContext.reloadAll");
		ManagementClient.doProcess(map);
	}
    
	public void testReload() throws Exception {
        map.put("COMMAND", "nebsoa.nebsoalink.context.MessageEngineContext.reload");
        map.put("test", "1234");
		ManagementClient.doProcess(map);
	}
    
	public void testRemoteCall() throws Exception {
        map.put("COMMAND", "nebsoa.nebsoalink.context.MessageEngineContext.reloadAll");
		ManagementClient.doProcess("CMS_MESSAGE_AP", map);
	}
}
