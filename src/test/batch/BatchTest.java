/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package test.batch;

import java.util.ArrayList;

import nebsoa.common.Context;
import nebsoa.common.collection.DataSet;
import nebsoa.common.jdbc.DBHandler;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.DataMap;
import nebsoa.common.util.Timer;
import nebsoa.spiderlink.OrgConstants;
import nebsoa.spiderlink.client.MessageClient;
import nebsoa.spiderlink.exception.ErrorResponseException;
import nebsoa.spiderlink.exception.InvalidResponseException;
import nebsoa.spiderlink.exception.MessageException;
import nebsoa.spiderlink.exception.MessageTimeoutException;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 배치프로그램 샘플 예제
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
 * $Log: BatchTest.java,v $
 * Revision 1.1  2018/01/15 03:39:51  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:06  cvs
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
 * Revision 1.2  2008/02/20 00:42:49  오재훈
 * *** empty log message ***
 *
 * Revision 1.1  2007/11/26 08:39:15  안경아
 * *** empty log message ***
 *
 * Revision 1.3  2007/07/30 03:11:09  김성균
 * *** empty log message ***
 *
 * Revision 1.2  2006/10/13 09:46:16  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2006/10/11 09:56:38  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class BatchTest {
    
    public static final String BATCH_SQL = 
          "\r\n\t INSERT INTO FWK_ERROR_HIS " 
        + "\r\n\t VALUES ('01', ?, 'test', TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'))";
                
	
	public static void main(String[] args) throws Exception {
        Timer timer = new Timer("$$$$$ 배치테스트 시작");
        timer.begin();
            
        //Context정보 생성
        Context ctx = new Context();
        DataMap map = new DataMap();
        /*try {
            //XML로 부터 전문요청정보를 로딩한다.
            //위치정보는 config/properties/management.properties.xml 참조
            map = (DataMap) DbToXml.fromXml("batch" + File.separator + "BatchTest.xml");
        } catch (FileNotFoundException e) {
        //TODO : 오류처리부분을 구현한다. 
            e.printStackTrace();
        }*/
        map.setContext(ctx);
            
        //전문데이타 입력
        
        map.put("주민번호", "84092420177281");
        map.put("헤더고객구분", "1");
        map.put("전자금융ID", "SCHLOAN01");
        map.put("주민사업자번호", "84092420177281");       
            
        //DataMap을 XML로 저장할 경우 사용할 수 있다.
//        DbToXml.toXml("batch" + File.separator + "BatchTest.xml", map);
        
            
        //전문처리요청
        try {
            map = MessageClient.doSyncProcess("account_list", OrgConstants.HOST, map);
            timer.showTime(">>> 전문처리요청");
        } catch (ErrorResponseException e) {
        //TODO : 오류처리부분을 구현한다. 
            e.printStackTrace();
        } catch (InvalidResponseException e) {
        //TODO : 오류처리부분을 구현한다. 
            e.printStackTrace();
        } catch (MessageTimeoutException e) {
        //TODO : 오류처리부분을 구현한다. 
            e.printStackTrace();
        } catch (MessageException e) {
        //TODO : 오류처리부분을 구현한다. 
            e.printStackTrace();
        }
        LogManager.debug(map.toString());
            
        DataSet ds = DBHandler.executeQuery(ctx, "SELECT MAX(TO_NUMBER(ERROR_SER_NO)) ERROR_SER_NO FROM FWK_ERROR_HIS");
        timer.showTime(">>> DB 최대값 SELECT");
        int start = 1;
        if (ds != null && ds.next()) {
            start = ds.getInt("ERROR_SER_NO") + 1;
        }
            
        //DB배치수행
        ArrayList paramsList = new ArrayList();
        for (int i = start; i < start + 20; i++) {
            Object[] params = {
                    i + ""
            };
            paramsList.add(params);
        }
        //한번에 10건씩 배치수행을 한다.
        //TODO : 오류발생건에 대한 처리 부분을 고려해야 한다.
        DBHandler.executeBatch(ctx, BATCH_SQL, paramsList);
        timer.showTime(">>> DB배치입력수행");
        timer.end();
        LogManager.debug("배치수행완료");
    }
}

