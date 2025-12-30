/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package test.dbtoxml;

import nebsoa.common.Context;
import nebsoa.common.collection.DataSet;
import nebsoa.common.jdbc.DBManager;
import nebsoa.common.jdbc.DBResultSet;
import nebsoa.common.util.DataMap;
import nebsoa.common.util.DbToXml;
import nebsoa.common.util.Timer;

/*******************************************************************
 * <pre>
 * 1.설명 
 * TODO DbToXmlTest 클래스에 대한 주석 넣기
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
 * $Log: MultyD2XTest.java,v $
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
 * Revision 1.2  2007/07/30 03:11:09  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2006/08/18 22:34:19  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2006/08/01 10:20:46  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2006/07/25 02:28:01  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class MultyD2XTest extends Thread {
    
    static DataMap map = new DataMap();
    static DBResultSet rs = null;
    static int i = 1;
    
    MultyD2XTest(String name) {
        super(name);
    }

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	static void setUp() throws Exception {
//		super.setUp();
    	System.setProperty("SPIDER_LOG_HOME", "/project/nebsoa/logs");
        map.setContext(new Context());
        map.put("주민번호", "84092420177281");
        map.put("고객구분", "1");
        map.put("전자금융ID", "SCHLOAN01");
        map.put("주민사업자번호", "84092420177281");       
        map.put("계좌번호", "84092420177281");
//        map = new AccountListBiz().execute(map);
        DataSet ds = map.getDataSet("계좌목록");
        ds.next();
        for (int i = 0; i < 100; i++) {
            Object[] obj = (Object[]) ds.getCurrentRow();
            Object[] param = new Object[] {
                    obj[0],
                    obj[1],
                    obj[2],
                    obj[3],
                    obj[4],
                    obj[5],
                    obj[6],
                    obj[7],
                    obj[8],
                    obj[9],
            };
            ds.appendData(param);
        }
//        LogManager.debug(map.toString());
	}

    /**
     * 2006. 08. 02 김성균
     */
    private static void select() {
        String sql = "SELECT TRX_ID, TRX_DATA FROM TRX_LOG --WHERE ROWNUM = 1";
        Object[] param = new Object[] {
        };
        rs = DBManager.executePreparedQuery(sql,param);
    }

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
//		super.tearDown();
	}
    
	/**
	 * @throws Exception
	 */
	public void testOneToXml() throws Exception {
        String sql = "INSERT INTO TRX_LOG (TRX_ID, TRX_DATA) VALUES (?, ?)";
        Timer timer = new Timer("XML TO DB 테스트(1건)");
        timer.begin();
        byte[] trxData = DbToXml.toXml(map);
        timer.showTime(" DataMap -> XML 변환");
//        LogManager.debug("### 압축 후의 SIZE = " + trxData.length + " bytes");
        
        Object[] param = new Object[] {
                "1",
                trxData
        };
        DBManager.executePreparedUpdate(sql,param);
        timer.showTime(" DB INSERT 수행");
//        timer.end();
	}
    
	/**
	 * @throws Exception
	 */
	public void testManyToXml() throws Exception {
        String sql = "INSERT INTO TRX_LOG (TRX_ID, TRX_DATA) VALUES (?, ?)";
        
        Timer timer = new Timer(getName() + " : XML TO DB 테스트");
        timer.begin();
        
        byte[] trxData = null;
        
        for (int i = 1; i <= 10; i++) {
            trxData = DbToXml.toXml(map);
	        timer.showTime(" DataMap -> XML 변환 [" + i + "]");
	        Object[] param = new Object[] {
	                "1",
	                trxData
	        };
            DBManager.executePreparedUpdate(sql,param);
            timer.showTime(" DB INSERT 수행 [" + i + "]");
        }
//        timer.end();
	}
    
	public void testDbToXml() throws Exception {
        String sql = "SELECT TRX_ID, TRX_DATA FROM TRX_LOG WHERE ROWNUM = 1";
        Object[] param = new Object[] {
        };
        
        Timer timer = new Timer(getName() + " : DB TO XML 테스트");
        timer.begin();
        DBResultSet rs = DBManager.executePreparedQuery(sql,param);
        timer.showTime(" DB SELECT 수행");
        
        int i = 1;
        while (rs.next()) {
	        map = (DataMap) DbToXml.fromXml(rs.getBytes(2));
            timer.showTime(" XML -> DataMap 변환 [" + i++ + "]");
        }
//        timer.end();
	}
    
    public void testD2X() throws Exception {
        Timer timer = new Timer(getName() + " : DB TO XML 테스트");
        timer.begin();
        while (rs.next()) {
            map = (DataMap) DbToXml.fromXml(rs.getBytes(2));
            timer.showTime(" XML -> DataMap 변환 [" + i++ + "]");
        }
        timer.showTime(" XML -> DataMap 변환 ");
    }
    
    public void run() {
        try {
            testD2X();
//            testManyToXml();
        } catch (InterruptedException e) {
        } catch (Exception e) {}

    }
    
    public static void main(String[] args) throws Exception {
        MultyD2XTest.setUp();
        MultyD2XTest.select();
        MultyD2XTest[] test = new MultyD2XTest[30];
        for (int i = 0; i < 30; i++) {
            test[i] = new MultyD2XTest(i + 1 +"");
        }
        for (int i = 0; i < 30; i++) {
            test[i].start();
        }
        for (int i = 0; i < 30; i++) {
            test[i].join();
        }
    }
}
