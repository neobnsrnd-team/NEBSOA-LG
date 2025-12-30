package test.schedule;

import nebsoa.common.batch.BatchContext;
import nebsoa.common.batch.BaseBatchJob;
import nebsoa.common.batch.BatchException;
import nebsoa.common.jdbc.DBManager;
import nebsoa.common.jdbc.DBResultSet;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.FormatUtil;

public class BatchSample extends BaseBatchJob {
	
	String errorCode = "정상";
	String errorMsg = "";
	// TODO : 비지니스 로직을 수행
	protected void service(BatchContext ctx) throws BatchException {

		String QUERY_SQL = " SELECT ERROR_CODE , ERROR_TITLE , LAST_UPDATE_DTIME , LAST_UPDATE_USER_ID  FROM FWK_ERROR ";
		
		DBResultSet rs = DBManager.executeQuery(QUERY_SQL);
		
		ctx.setRecordCnt(rs.getColumnCount()+"");//처리 대상 건수
		try {
			int executeCnt=0;
	
			while(rs.next())
			{
				System.out.println("ERROR_CODE="+rs.getString("ERROR_CODE"));
				System.out.println("ERROR_TITLE="+rs.getString("ERROR_TITLE"));
				System.out.println("LAST_UPDATE_DTIME="+FormatUtil.getFormattedText(rs.getString("LAST_UPDATE_DTIME"),"####-##-##"));
				System.out.println("LAST_UPDATE_USER_ID="+rs.getString("LAST_UPDATE_USER_ID"));
				executeCnt++;
				
				/**
				 * TODO : 강제 종료를 대비해서 필수 구현 요소. 수시로 배치 프로그램의 상태를 체크해서 강제 종료일경우 프로그램 종료
				 * 아래 샘플은 10개의 ROW 검색시마다 체크하도록 함.
				 */
				if( executeCnt%10 == 0 ){
					if(!getBatchState()) break;
				}
			}
	
			ctx.setExecuteCnt(executeCnt+"");//정상 처리 건수
		}catch (Exception e){
			e.printStackTrace();
			errorCode = "정의된코드";
			errorMsg = e.getMessage();
		}finally{
			/**
			 * 필수 셋팅 사항. 최종 수행 결과 코드를 세팅
			 */
			ctx.setStateCode("OK");		// 1 상태코드
			ctx.setErrorCode(errorCode);		// 2 오류코드
			ctx.setErrorReason(errorMsg);		// 3 오류 발생원인
//			ctx.setRecordCnt("");		// 4 처리대상건수
//			ctx.setExecuteCnt("");		// 5 총처리건수
			ctx.setSuccessCnt("10");	// 6 정상처리건수
			ctx.setErrorCnt("0");		// 7 오류 건수
			LogManager.debug(logFile,"===== 배치 샘플 수행 테스트중 =====");

		}
	}
	
	

}