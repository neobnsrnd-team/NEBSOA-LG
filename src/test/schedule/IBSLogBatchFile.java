package test.schedule;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.lang.CharUtils;

import nebsoa.common.Context;
import nebsoa.common.batch.BaseBatchJob;
import nebsoa.common.batch.BatchContext;
import nebsoa.common.batch.BatchException;
import nebsoa.common.jdbc.DBHandler;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.DataMap;
import nebsoa.common.util.FormatUtil;
import nebsoa.common.util.PropertyManager;

public class IBSLogBatchFile extends BaseBatchJob {

	ArrayList insertData;

	int insertCnt;
	String executeFile;//실행할 배치 파일명
	
	String exeErrorCode=""; // 오류코드
	String exeErrorReason=""; // 오류 발생원인
	int exePlanCnt=0; // 처리대상건수
	int exeErrorCnt; // 오류 건수
	
	int executeBatchLineCnt;// insert 구문을 실행할 배치 라인수

	FileReader fr = null;

	BufferedReader br = null;

	private final String sql = "INSERT INTO IBSLOG (" 
			  +" 거래발생일 "
			  +" ,거래시간 "
			  +" ,서버구분 "
			  +" ,일련번호 "
			  +" ,송수신구분 "
			  +" ,매체코드 "
			  +" ,부점코드 "
			  +" ,단말번호 "
			  +" ,거래코드 "
			  +" ,에러코드 "
			  +" ,거래로그번호 "
			  +" ,주민등록번호 "
			  +" ,고객구분 "
			  +" ,전자금융아이디 "
			  +" ,IP "
			  +" ,입출력데이터 "
			  +" ,입출력) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'',?)";

	private final String logDir = "D:\\testSpace\\TestSrc\\testfiles\\msgfiles\\";
	
	/**
	 * 선행 작업 수행 여부체크 등의 초기화 작업을 수행 합니다.
	 * @param ctx : BatchContext
	 */
	protected void init(BatchContext ctx) throws BatchException {

		insertData = new ArrayList();
		insertCnt = 0;
		String ectLine;

		try{
			ectLine = (String) ctx.getDataMap().get("executeLine");
			executeFile = (String) ctx.getDataMap().get("executeFile");
		}catch(Exception e){
			throw new BatchException("FRB00003","배치 파라미터가 없습니다.");
		}
		
		if (ectLine == null || ectLine.length() <= 0) {
			executeBatchLineCnt = PropertyManager.getIntProperty("keb",
					"BATCH_DATA_INSERT_CNT", 100);
		} else {
			executeBatchLineCnt = Integer.parseInt(ectLine);
		}

		if (executeFile == null || executeFile.length() <= 0) {
			throw new BatchException("FRB99999","배치 파일이 지정되지 않았습니다.");
		} else {
			executeFile = "MESSAGE_CA21-"+FormatUtil.getToday("yyyy_MM_dd")+"_"+executeFile+".log";
		}
	}

	
	/**
	 * 비지니스 로직을 수행
	 * @param ctx : BatchContext
	 */
	protected void service(BatchContext ctx) throws BatchException {

		
//		File file = new File("D:\\testSpace\\TestSrc\\testfiles\\msgfiles\\MESSAGE_CA21-2006_10_23_10.log");
		File file = new File(logDir+executeFile);
		
		String line = "";

		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);

			while ((line = br.readLine()) != null) {
				
				try {
					// Line 파싱
					addDataArray(line);
				} catch (Exception e) {
					System.out.println("parsing error");
					exeErrorCnt++;//오류 건수
					exeErrorCode = "FRB10001"; // 오류코드
					
					exeErrorReason = e.getMessage(); // 오류 발생원인
					if(exeErrorReason.length()>=4000) exeErrorReason.substring(0,3999);
					
					// 배치 로그에 남기고 계속 진행.
					LogManager.error(logFile,"=========   BATCH ERROR =========" 
									+ "\n\t JobId:"	+ ctx.getBatchId() 
									+ "\n\t 파일 명 :"	+ executeFile
									+ "\n\t 라인 parsing중 에러 에러 줄수 :" + (exePlanCnt)
									+ "\n\t ==========   BATCH ERROR END   =========");
				}

				/**
				 * 강제 종료를 대비해서 필수 구현 요소. 수시로 배치 프로그램의 상태를 체크해서 강제 종료일경우 프로그램 종료
				 * 아래 샘플은 executeBatchLineCnt(기본100개)개의 Line Parsing 체크하도록 함.
				 */

				if ((exePlanCnt % executeBatchLineCnt) == 0) {
					if (!getBatchState())
					{
						stateCode = BATCH_STATE_OF_KILLED;//강제 종료
						break;
					}
				}
				
				if ( (exePlanCnt % executeBatchLineCnt) == 0) {
					try {
						// DB Insert
						executeBatch();
					} catch (Exception e) {
						exeErrorCnt += insertData.size();//오류 건수

						exeErrorCode = "FRB10002"; // 오류코드
						
						exeErrorReason = e.getMessage(); // 오류 발생원인
						if(exeErrorReason.length() >= 4000) exeErrorReason.substring(0,3999);
						
						// 배치 로그에 남기고 계속 진행.
						LogManager.error(logFile,"=========   BATCH ERROR   =========" 
								+ "\n\t JobId:"	+ ctx.getBatchId() 
								+ "\n\t 파일 명 :"	+ executeFile
								+ "\n\t executeBatch() 수행중 에러 " +"\t 메세지="+ e.getMessage()
								+ "\n\t 에러 처리된 Line : " + (exePlanCnt-executeBatchLineCnt+1) + " ~ " + (exePlanCnt)
								+ "\n\t ============   BATCH ERROR END   ===========");

					} finally {
						insertData.clear();
						insertCnt = 0;
					}
				}
			}

			if ((exePlanCnt % executeBatchLineCnt) != 0) {
				try {
					executeBatch();
				} catch (Exception e) {
					exeErrorCnt += insertData.size();//오류 건수
					
					exeErrorCode = "FRB10002"; // 오류코드
					
					exeErrorReason = e.getMessage(); // 오류 발생원인
					if(exeErrorReason.length()>=4000) exeErrorReason.substring(0,3999);

					
					// 배치 로그에 남기고 계속 진행.
					LogManager.error(logFile,"=========   BATCH ERROR   =========" 
							+ "\n\t JobId:"	+ ctx.getBatchId() 
							+ "\n\t 파일 명 :"	+ executeFile
							+ "\n\t executeBatch() 수행중 에러 " 
							+ "\n\t 에러 처리된 Line : " + (exePlanCnt-insertData.size()+1) + " ~ " + (exePlanCnt-1)
							+ "\n\t =============   BATCH ERROR END   ===========");
				}

			}

			stateCode = BATCH_STATE_OF_END_SUCESS;//정상종료
			
		} catch (Exception e) {
			e.printStackTrace();
			stateCode = BATCH_STATE_OF_END_FAIL;//비정상종료
			
			exeErrorCode = "FRB99999"; // 오류코드
			
			exeErrorReason = e.getMessage(); // 오류 발생원인
			if(exeErrorReason.length()>=4000) exeErrorReason.substring(0,3999);
			
			LogManager.error(logFile,"=========   BATCH ERROR   =========" 
					+ "\n\t JobId:"	+ ctx.getBatchId() 
					+ "\n\t 에러메시지:"	+ e.getMessage()
					+ "==========  BATCH ERROR LOG END  ===========");

		} finally {
			try {
				if (fr != null)
					fr.close();
				if (br != null)
					br.close();
			} catch (IOException e) {
			}
		}

	}

	
	/**
	 * 자원 반납 등의 마무리 작업 및 최종 수행 결과 코드를 세팅
	 * @param ctx : BatchContext
	 */
	protected void destroy(BatchContext ctx) throws BatchException {

		/**
		 * 필수 셋팅 사항. 최종 수행 결과 코드를 세팅
		 */
		ctx.setStateCode(stateCode); // 1 상태코드
		ctx.setErrorCode(exeErrorCode); // 2 오류코드
		ctx.setErrorReason(exeErrorReason); // 3 오류 발생원인
		ctx.setRecordCnt(exePlanCnt+""); // 4 처리대상건수
		ctx.setExecuteCnt((exePlanCnt-exeErrorCnt)+""); // 5 총처리건수
		ctx.setSuccessCnt((exePlanCnt-exeErrorCnt)+""); // 6 정상처리건수
		ctx.setErrorCnt(exeErrorCnt+""); // 7 오류 건수

	}

	/**
	 * Line Parsing
	 * 
	 * @param contents : 해석할 Line
	 */
	public void addDataArray(String contents) {

		String[] fromData = contents.split("\\|");
		String[] toData = new String[16];

		//스트링 비교 startdate 인자로 받은것. 인자값은 배치 기준일자이고 이 일자가 해당 배치가 기록된 날짜이다.
		if (fromData[0].length() != 8
				|| !CharUtils.isAsciiNumeric(fromData[0].charAt(0))) {
			return;
		}
		exePlanCnt++;//처리대상건수

		// 3번째 데이타는 쓰레기임.
		// fromData[0] = fromData[0];//거래발생일
		// fromData[1] = fromData[1].length() > 6 ? fromData[1].substring(0,6) :
		// fromData[1];//거래시간
		// fromData[2] = fromData[2].length() > 3 ? fromData[2].substring(0,3) :
		// fromData[2];//서버구분
		// fromData[4] = fromData[4].length() > 8 ? fromData[4].substring(0,8) :
		// fromData[4];//일련번호
		// fromData[5] = fromData[5].length() > 4 ? fromData[5].substring(0,4) :
		// fromData[5];//송수신구분
		// fromData[6] = fromData[6].length() > 2 ? fromData[6].substring(0,2) :
		// fromData[6];//매체코드
		// fromData[7] = fromData[7].length() > 4 ? fromData[7].substring(0,4) :
		// fromData[7];//부점코드
		// fromData[8] = fromData[8].length() > 3 ? fromData[8].substring(0,3) :
		// fromData[8];//단말번호
		// fromData[9] = fromData[9].length() > 11 ? fromData[9].substring(0,11)
		// : fromData[9];//거래코드
		// fromData[10] = fromData[10].length() > 8 ?
		// fromData[10].substring(0,8) : fromData[10];//에러코드
		// fromData[11] = fromData[11].length() > 28 ?
		// fromData[11].substring(0,28) : fromData[11];//거래로그번호
		// fromData[12] = fromData[12].length() > 13 ?
		// fromData[12].substring(0,13) : fromData[12];//주민등록번호
		// fromData[13] = fromData[13].length() > 1 ?
		// fromData[13].substring(0,1) : fromData[13];//고객구분
		// fromData[14] = fromData[14].length() > 10 ?
		// fromData[14].substring(0,10) : fromData[14];//전자금융 ID
		// fromData[15] = fromData[15].length() > 15 ?
		// fromData[15].substring(0,15) : fromData[15];//IP
		// fromData[16] = fromData[16].length() > 4000 ?
		// fromData[16].substring(0,4000) : fromData[16];//입출력데이터

		toData[0] = fromData[0];// 거래발생일
		toData[1] = fromData[1].length() > 6 ? fromData[1].substring(0, 6)
				: fromData[1];// 거래시간
		toData[2] = fromData[2].length() > 3 ? fromData[2].substring(0, 3)
				: fromData[2];// 서버구분
		toData[3] = fromData[4].length() > 8 ? fromData[4].substring(0, 8)
				: fromData[4];// 일련번호
		toData[4] = fromData[5].length() > 4 ? fromData[5].substring(0, 4)
				: fromData[5];// 송수신구분
		toData[5] = fromData[6].length() > 2 ? fromData[6].substring(0, 2)
				: fromData[6];// 매체코드
		toData[6] = fromData[7].length() > 4 ? fromData[7].substring(0, 4)
				: fromData[7];// 부점코드
		toData[7] = fromData[8].length() > 3 ? fromData[8].substring(0, 3)
				: fromData[8];// 단말번호		
		toData[8] = fromData[9].length() > 11 ? fromData[9].substring(0, 11)
				: fromData[9];// 거래코드
		toData[9] = fromData[10].length() > 8 ? fromData[10].substring(0, 8)
				: fromData[10];// 에러코드
		toData[10] = fromData[11].length() > 28 ? fromData[11].substring(0, 28)
				: fromData[11];// 거래로그번호
		toData[11] = fromData[12].length() > 13 ? fromData[12].substring(0, 13)
				: fromData[12];// 주민등록번호
		toData[12] = fromData[13].length() > 1 ? fromData[13].substring(0, 1)
				: fromData[13];// 고객구분
		toData[13] = fromData[14].length() > 10 ? fromData[14].substring(0, 10)
				: fromData[14];// 전자금융 ID
		toData[14] = fromData[15].length() > 15 ? fromData[15].substring(0, 15)
				: fromData[15];// IP

		byte[] msglog = fromData[16].getBytes();
		if( msglog.length > 4000 ) 
		{
			byte[] temp = new byte[4000];
			System.arraycopy(msglog,0,temp,0,3996);
			toData[15] = new String(temp);

//			byte[] temp = new byte[4000];
//			System.arraycopy(msglog,0,msglog,0,4000);
//			toData[15] = new String(msglog);
		
		} else {
			toData[15] = new String(msglog);
		}
		
		insertData.add(toData);
		insertCnt++;
	}

	/**
	 * DBHandler.executeBatch() 실행
	 *
	 */
	public void executeBatch() {
			DBHandler.executeBatch( new Context(), "DEVELOP", sql, insertData,
					executeBatchLineCnt);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long sTime = System.currentTimeMillis();

//		File file = new File("D:\\testSpace\\TestSrc\\testfiles\\msgfiles\\MESSAGE_CA21-2006_10_23_10.log");

		IBSLogBatchFile bf = new IBSLogBatchFile();
		BatchContext bct = new BatchContext();
		DataMap dm = bct.getDataMap();
		dm.put("executeLine","100");
		dm.put("executeFile","10");
//		bct.setDataMap(dm);
		bf.init(bct);
		bf.service(bct);
		bf.destroy(bct);

		System.out.println("#### 처리대상건수 ="+bct.getRecordCnt());
		System.out.println("#### 총처리건수 ="+bct.getExecuteCnt());  
		System.out.println("#### 걸린 시간 ="
				+ (System.currentTimeMillis() - sTime) / 1000);
	}
}
