/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package test.csv;

import nebsoa.biz.base.SpiderBaseTestCase;
import nebsoa.common.collection.DataSet;
import nebsoa.common.util.DataMap;
import nebsoa.spiderlink.engine.message.ByteMessageInstance;
import nebsoa.spiderlink.engine.message.MessageStructure;
import nebsoa.spiderlink.engine.message.MessageStructurePool;
import nebsoa.spiderlink.engine.parser.CsvMessageParser;

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
 * $Log: CsvTest.java,v $
 * Revision 1.1  2018/01/15 03:39:52  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:19  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:26  김성균
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
 * Revision 1.1  2007/11/26 08:39:16  안경아
 * *** empty log message ***
 *
 * Revision 1.3  2007/07/30 03:11:09  김성균
 * *** empty log message ***
 *
 * Revision 1.2  2006/08/31 10:41:15  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2006/08/29 09:38:54  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class CsvTest extends SpiderBaseTestCase {
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
        setUserInfo();
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
        StringBuffer msg = new StringBuffer(); 
        msg.append("18611002463,15716704,논현남지점,19830725,20060419,0,,0,00000,당좌예금\n")
           .append("611016565193,162452126,영업부,20060306,20060823,0,,1,00130,저축예금");
        /*
        msg.append("<KEB_IBS_ERP>")
            .append("<주민사업자번호>")
            .append("1111111111111")
            .append("</주민사업자번호>")
            .append("<IBS응답코드>")
            .append("</IBS응답코드>")
            .append("<IBS응답메시지>")
            .append("</IBS응답메시지>")
            .append("<전자금융ID>")
            .append("GOGOGO")
            .append("</전자금융ID>")
            .append("<전자금융ID>")
            .append("GOGOGO")
            .append("</전자금융ID>")
            .append("AAA,BBB\n")
            .append("CCC,DDD")
            .append("</KEB_IBS_ERP>");
			  <계좌번호>611016565193</계좌번호> 
			  <현재잔액>162452126</현재잔액> 
			  <개설점>영업부</개설점> 
			  <신규일자>20060306</신규일자> 
			  <최종거래일자>20060823</최종거래일자> 
			  <계좌상태코드>0</계좌상태코드> 
			  <계좌상태한글 /> 
			  <계좌출금여부>1</계좌출금여부> 
			  <무통장거래횟수>00130</무통장거래횟수> 
			  <수신상품명>저축예금</수신상품명> 
		*/
        
        MessageStructure structure = MessageStructurePool.getInstance().getMessageStructure("csv_testA@ERP");
        ByteMessageInstance instance = new ByteMessageInstance(msg.toString().getBytes());
        DataMap dataMap = new DataMap();
        CsvMessageParser parser = CsvMessageParser.getInstance();
        
        dataMap = parser.parse(structure, instance, dataMap);
        DataSet dataSet = dataMap.getDataSet("A");
        while (dataSet.next()) {
            System.out.println("-----------------------------------------------------");
            System.out.println("계좌번호=" + dataSet.getString("계좌번호"));
            System.out.println("현재잔액=" + dataSet.getString("현재잔액"));
            System.out.println("개설점=" + dataSet.getString("개설점"));
            System.out.println("신규일자=" + dataSet.getString("신규일자"));
            System.out.println("최종거래일자=" + dataSet.getString("최종거래일자"));
            System.out.println("계좌상태코드=" + dataSet.getString("계좌상태코드"));
            System.out.println("계좌상태한글=" + dataSet.getString("계좌상태한글"));
            System.out.println("계좌출금여부=" + dataSet.getString("계좌출금여부"));
            System.out.println("무통장거래횟수=" + dataSet.getString("무통장거래횟수"));
            System.out.println("수신상품명=" + dataSet.getString("수신상품명"));
            System.out.println("-----------------------------------------------------");
        }
	}

}
