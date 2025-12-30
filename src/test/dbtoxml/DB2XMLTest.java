/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package test.dbtoxml;

import junit.framework.TestCase;
import nebsoa.common.jndi.WasManager;
import nebsoa.common.util.DataMap;
import nebsoa.common.util.Timer;

/*******************************************************************
 * <pre>
 * 1.설명 
 * DbToXmlTest
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
 * $Log: DB2XMLTest.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
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
 * Revision 1.2  2008/02/20 00:42:48  오재훈
 * *** empty log message ***
 *
 * Revision 1.1  2007/11/26 08:38:56  안경아
 * *** empty log message ***
 *
 * Revision 1.5  2006/10/30 01:42:47  김성균
 * *** empty log message ***
 *
 * Revision 1.4  2006/10/23 13:33:14  김성균
 * *** empty log message ***
 *
 * Revision 1.3  2006/10/21 08:30:06  김성균
 * *** empty log message ***
 *
 * Revision 1.2  2006/10/17 02:20:07  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2006/10/16 09:30:16  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class DB2XMLTest extends TestCase {
    
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}
    
	public void testFromXml() throws Exception {
//        String xmlFileName = "message_structure_pool";
        String orgId = "ERP";
        String messageId = "MT004_I";
        String xmlFileName = "message_structure_pool_MT004_I_ERP.xml";
        //return new StringBuffer(getXmlFileName()).append("_").append(messageId).append("_").append(orgId).append(".xml").toString();
        
        Timer timer = new Timer("XML 로딩 테스트");
        timer.begin();
        
        /*
        MessageStructure ms = (MessageStructure) DbToXml.fromXml(xmlFileName);
        timer.showTime("XML 로딩");
        timer.end();
        LogManager.debug("기관ID : " + ms.getOrgId());
        LogManager.debug("전문ID : " + ms.getMessageId());
        LogManager.debug("전문필드이름 : " + ms.getFieldTagNameMap());
        */
       
        DataMap map = new DataMap();
//        WebAppMapping.reloadAll(map);
//        BizManager.reloadAll(map);
//        ErrorCodeUtil.reloadAll(map);
//        I18NUtil.reloadAll(map);
//        LogManager.debug(I18NUtil.getLabelList("KO"));
//        I18NUtil.getInstance().toXml();
//        JSUtil.reloadAll(map);
        WasManager.reloadAll(map);
	}
	
}
