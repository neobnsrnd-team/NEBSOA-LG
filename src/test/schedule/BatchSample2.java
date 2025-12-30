package test.schedule;

import nebsoa.common.batch.BatchContext;
import nebsoa.common.batch.BaseBatchJob;
import nebsoa.common.batch.BatchException;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.DataMap;
import nebsoa.common.util.FormatUtil;

public class BatchSample2 extends BaseBatchJob {

	protected void service(BatchContext bInfo) throws BatchException {
		bInfo.setStateCode("OK");	// 1 상태코드
		bInfo.setErrorCode("");		// 2 오류코드
		bInfo.setErrorReason("");	// 3 오류 발생원인
		bInfo.setRecordCnt("");		// 4 처리대상건수 
		bInfo.setExecuteCnt("90");	// 5 총처리건수
		bInfo.setSuccessCnt("90");	// 6 정상처리건수
		bInfo.setErrorCnt("0");		// 7 오류 건수		

		LogManager.debug("★★★★★★★★★★★★★★ 배치2 service() 테스트중 ★★★★★★★★★★★");
	}


	public static void main(String[] args){

		BatchSample2 bf = new BatchSample2();
		BatchContext bCtx = new BatchContext();
		
    	bCtx.setBatchId("IBSLOGBATCH10");//배치 ID
//    	bCtx.setWasInstanceId("CA11");//이 프로그램이 실행하는 WAS의 INSTANCE ID
//    	bCtx.setStartDate(FormatUtil.getToday("yyyyMMddHHmmss"));//시작일시 년월일시분초
    	bCtx.setStandardDate(FormatUtil.getToday("yyyyMMdd"));//기준일자  디폴트로  현재일자
    	bCtx.setExeUserId("SYSTEM");//실행을 요청한 USDER 디폴트로 시스템이한다.
    	
		int ret = bf.execute(bCtx);//프로그램 실행
		System.exit(ret);
		
	}
}
