/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.batch;

import java.util.HashMap;

import nebsoa.common.exception.SysException;
import nebsoa.common.jdbc.DBManager;
import nebsoa.common.jdbc.DBResultSet;
import nebsoa.common.jdbc.TxManager;
import nebsoa.common.util.DbTypeUtil;
import nebsoa.common.util.QueryManager;

/*******************************************************************
 * <pre>
 * 1.설명 
 * Batch 프로그램 시작, 종료 로그 남기기
 * 
 * 2.사용법
 *
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
 * $Log: BatchLogger.java,v $
 * Revision 1.1  2018/01/15 03:39:50  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:47  cvs
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
 * Revision 1.1  2008/08/04 08:54:53  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.2  2008/03/13 08:35:35  오재훈
 * FWK_BATCH_HIS 테이블에 INSERT 하는 DB2 쿼리 추가.
 *
 * Revision 1.1  2008/01/22 05:58:28  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.2  2008/01/09 10:07:42  홍윤석
 * Mysql일 경우 추가
 *
 * Revision 1.1  2007/11/26 08:38:57  안경아
 * *** empty log message ***
 *
 * Revision 1.10  2006/11/21 04:55:45  오재훈
 * *** empty log message ***
 *
 * Revision 1.9  2006/11/20 04:40:54  오재훈
 * *** empty log message ***
 *
 * Revision 1.8  2006/11/11 06:00:13  오재훈
 * *** empty log message ***
 *
 * Revision 1.7  2006/10/25 02:08:38  오재훈
 * main으로 실행하면 배치 인포를 받지 않기 위해서 재실행가능여부를 parameter로 받게 수정.
 *
 * Revision 1.6  2006/10/16 09:35:41  이종원
 * *** empty log message ***
 *
 * Revision 1.5  2006/09/28 05:40:44  오재훈
 * 재실행 여부 = N이면 배치 기준일에 실행되었으면 Exception발생
 *
 * Revision 1.4  2006/09/22 09:18:50  오재훈
 * *** empty log message ***
 *
 * Revision 1.3  2006/09/12 00:42:42  오재훈
 * 작업중
 *
 * Revision 1.2  2006/09/09 07:30:16  오재훈
 * *** empty log message ***
 *
 *
 *
 * </pre>
 ******************************************************************/
public class BatchLogger {

	//Oracle
	private static final String BATCH_EXECUTE_SEQ_ORA_SQL = 
		"SELECT " +
		" NVL(MAX(BATCH_EXECUTE_SEQ),0) FROM FWK_BATCH_HIS "+
		" WHERE BATCH_APP_ID = ?"+
		" AND INSTANCE_ID = ?"+
		" AND BATCH_DATE = ?";

	//Mysql
	private static final String BATCH_EXECUTE_SEQ_MYSQL_SQL = 
		"SELECT " +
		" IFNULL(MAX(BATCH_EXECUTE_SEQ),0) FROM FWK_BATCH_HIS "+
		" WHERE BATCH_APP_ID = ?"+
		" AND INSTANCE_ID = ?"+
		" AND BATCH_DATE = ?";

	private static final String BATCH_EXECUTE_SEQ_DB2_SQL = 
		"SELECT " +
		" CASE WHEN MAX(BATCH_EXECUTE_SEQ)  IS NULL THEN 0 " +
		" ELSE MAX(BATCH_EXECUTE_SEQ) " +
		" END BATCH_EXECUTE_SEQ " +
		" FROM FWK_BATCH_HIS " +
		" WHERE BATCH_APP_ID = ?"+
		" AND INSTANCE_ID = ?"+
		" AND BATCH_DATE = ?";
	
	private static final String BATCH_START_LOG_SQL =
		" INSERT INTO FWK_BATCH_HIS " +
		" (BATCH_APP_ID , INSTANCE_ID , BATCH_EXECUTE_SEQ , BATCH_DATE, LOG_DTIME , LAST_UPDATE_USER_ID) " +
		" VALUES " +
		"(?,?,?,?,?,?)";
//		"('SAMPLE','CA11','1','20060909','200609092300','JHOH')";
	
	private static final String BATCH_END_LOG_SQL = 
		" UPDATE FWK_BATCH_HIS SET "
		+" BATCH_END_DTIME = ?"//'200609100100' ," +
		+" , RES_RT_CODE = ?"//'OK', " +
		+" , ERROR_CODE = ? "//'NO', " +
		+" , ERROR_REASON = ?"//'NO', " +
		+" , RECORD_COUNT = ?"//'1000', " +
		+" , EXECUTE_COUNT = ?"//'900', " +
		+" , SUCCESS_COUNT = ?"//'900', " +
		+" , FAIL_COUNT = ? "//'0' " +
		+" WHERE BATCH_APP_ID = ?"//'SAMPLE' " 
		+" AND INSTANCE_ID = ?"//'CA11' " +
		+" AND BATCH_EXECUTE_SEQ = ?"//'1'";
		+" AND BATCH_DATE = ?";//'1'";	

	private static HashMap BATCH_EXECUTE_SEQ_SQL_MAP= new HashMap();
	
	static{
		BATCH_EXECUTE_SEQ_SQL_MAP.put(Integer.valueOf(DbTypeUtil.ORACLE).toString(), BATCH_EXECUTE_SEQ_ORA_SQL);
		BATCH_EXECUTE_SEQ_SQL_MAP.put(Integer.valueOf(DbTypeUtil.MY_SQL).toString(), BATCH_EXECUTE_SEQ_MYSQL_SQL);
		BATCH_EXECUTE_SEQ_SQL_MAP.put(Integer.valueOf(DbTypeUtil.DB2).toString(), BATCH_EXECUTE_SEQ_DB2_SQL);

	}
	
	
	/**
	 * 배치 시작 로그 남기기	
	 * @param bInfo: BatchLogInfo 배치 시작 로그 남기기
	 * @return
	 */
	public synchronized String startLog(BatchContext bctx,String retryableYn) {
		
		int executeSeq = 1;//조회해서 없을 경우 1 셋팅

		Object[] param = new Object[]{
				bctx.getBatchId(),
				bctx.getWasInstanceId(),
				bctx.getStandardDate()
		};

		TxManager tx = new TxManager("BATCH");
		try{
			tx.begin();
			
			DBResultSet rs = DBManager.executePreparedTxQuery(tx, QueryManager.getQueryToMap(BATCH_EXECUTE_SEQ_SQL_MAP) ,param);
			
			if(rs.next()) {
				executeSeq = rs.getInt(1);
				
				//재실행 여부가 불가일 경우 실행하지 않음.
				if("N".equals(retryableYn) && executeSeq != 0)
				{
					throw new BatchException("FRB00004","이미 실행한 배치입니다. 재실행 여부가 불가로 셋팅되어 있습니다.");
				}

				executeSeq++;
			}
			
			bctx.setExecuteSeq(executeSeq+"");
			
			Object[] param2 = new Object[]{
					bctx.getBatchId(),//1 BATCH ID
					bctx.getWasInstanceId(),//2 WAS ID
					bctx.getExecuteSeq(),//3 실행회차
					bctx.getStandardDate(),//4 기준일자
					bctx.getStartDate(),//5 시작일시
					bctx.getExeUserId()//6 수행자ID
			};
			
			DBManager.executePreparedTxUpdate(tx,BATCH_START_LOG_SQL,param2);
        	
			tx.commit();
        } catch (Throwable e){
			tx.rollback();
			e.printStackTrace();
			throw new SysException(e.toString());
        } finally {
        	tx.end();
        }

			
		return executeSeq+"";
	}

	/**
	 * 배치 종료 로그 남기기
	 * @param bInfo : BatchLogInfo 종료 배치 로그 남기기
	 */
	public synchronized void endLog(BatchContext bInfo) {

		Object[] param = new Object[]{
				bInfo.getEndDate(),// 1 종료일
				bInfo.getStateCode(),// 2 상태코드
				bInfo.getErrorCode(),// 3 오류코드
				bInfo.getErrorReason(),// 4 오류 발생원인
				bInfo.getRecordCnt(),// 5 처리대상건수 
				bInfo.getExecuteCnt(),// 6 총처리건수
				bInfo.getSuccessCnt(),// 7 정상처리건수
				bInfo.getErrorCnt(),// 8 오류 건수
				bInfo.getBatchId(),//9 BATCH ID
				bInfo.getWasInstanceId(),//10 WAS ID
				bInfo.getExecuteSeq(),//11 실행회차
				bInfo.getStandardDate()//12 기준일자

		};
		
		DBManager.executePreparedUpdate("BATCH",BATCH_END_LOG_SQL,param);
        	
	}

}
