/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.batch;

import org.apache.commons.lang.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;
import nebsoa.common.schedule.JobContext;
import nebsoa.common.startup.StartupContext;
import nebsoa.common.util.DataMap;
import nebsoa.common.util.FormatUtil;
import nebsoa.common.util.StringUtil;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 개발자가 개발하려는 Batch프로그램이 상속받아야 하는 클래스
 * 수행중 중지 명령에 대한 실행에 대비해서 수시로 중지 플래그 검사.
 * 
 * 2.사용법
 * public class BatchSample extends BaseBatchJob {
 * 
 * 	String errorCode = "정상코드";	
 * 	String errorMsg = "";
 *
 *  //비지니스 로직을 수행
 *	protected void service(BatchContext ctx) throws BatchException {
 *		try {
 *			...
 *			...	
 *		}catch (Exception e){
 * 			e.printStackTrace();
 * 			errorCode = "정의된코드";
 * 			errorMsg = e.getMessage();
 *	    }finally{
 *		
 *			// 필수 셋팅 사항. 최종 수행 결과 코드를 세팅
 *			ctx.setStateCode("OK");	  // 1 상태코드
 *			ctx.setErrorCode(errorCode);     // 2 오류코드
 *			ctx.setErrorReason(errorMsg);   // 3 오류 발생원인
 *			ctx.setRecordCnt(cnt+""); // 4 처리대상건수 
 *			ctx.setExecuteCnt("10");  // 5 총처리건수
 *			ctx.setSuccessCnt("10");  // 6 정상처리건수
 *			ctx.setErrorCnt("0");     // 7 오류 건수
 *	    	LogManager.info("※※※※※※※※※※※※※ 배치 service() ※※※※※※※※※※※※");
 *		}
 *
 *	}	
 *	
 *  public static void main(String[] args) {
 *		BatchSample bf = new BatchSample();
 *		BatchContext bCtx = new BatchContext();
// *		DataMap dm = new DataMap();//배치 프로그램에서 parameter로 받을 데이타가 있으면 DataMap에 셋팅해서 넘긴다.
// *		dm.put("logdir","D:\\testSpace\\TestSrc\\testfiles\\msgfiles\\");
// *		dm.put("executeLine","100");
// *		dm.put("executeFile","18");
 *		
 *    	bCtx.setBatchId("IBSLOGBATCH10");//배치 ID
 *    	bCtx.setStandardDate(FormatUtil.getToday("yyyyMMdd"));//기준일자  디폴트로  현재일자
 *    	bCtx.setExeUserId("SYSTEM");//실행을 요청한 USDER 디폴트로 시스템이한다.
 *
 *		bf.execute(bCtx);//프로그램 실행
 *
 *	}
 * }
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author $Author: cvs $
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 
 * $Log: BaseBatchJob.java,v $
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
 * Revision 1.2  2008/09/16 06:46:46  youngseokkim
 * property 값 저장 부분 수정
 *
 * Revision 1.1  2008/08/04 08:54:53  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.2  2008/05/15 01:01:58  오재훈
 * SPIDER_INSTANCE_ID -> StartupContext.getInstanceId()로 교체
 *
 * Revision 1.1  2008/01/22 05:58:28  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:58  안경아
 * *** empty log message ***
 *
 * Revision 1.20  2007/07/16 11:31:14  오재훈
 * *** empty log message ***
 *
 * Revision 1.19  2007/03/07 02:53:45  김성균
 * 일부 로그 INFO 레벨로 변경
 *
 * Revision 1.18  2007/02/23 09:45:57  김성균
 * service() 에서 설정한 상태코드값이 존재하면 그 값을 리턴하도록 수정
 *
 * Revision 1.17  2007/02/23 07:14:53  김성균
 * service() 메소드에서 executeState 값 리턴하도록 수정
 *
 * Revision 1.16  2007/01/19 05:35:06  김성균
 * FAIL_CODE 수정
 *
 * Revision 1.15  2007/01/11 09:27:57  김성균
 * SUCCESS_CODE,FAIL_CODE 접근제어자 수정
 *
 * Revision 1.14  2006/11/23 00:52:35  오재훈
 * *** empty log message ***
 *
 * Revision 1.13  2006/11/21 05:09:10  오재훈
 * *** empty log message ***
 *
 * Revision 1.12  2006/11/01 04:09:30  오재훈
 * *** empty log message ***
 *
 * Revision 1.11  2006/10/30 12:24:06  오재훈
 * *** empty log message ***
 *
 * Revision 1.10  2006/10/30 05:00:44  오재훈
 * *** empty log message ***
 *
 * Revision 1.9  2006/10/25 12:42:02  오재훈
 * abstract 메소드인 init(),destroy()를 빈 메소드로 변경
 *
 * Revision 1.8  2006/10/25 10:21:34  오재훈
 * *** empty log message ***
 *
 * Revision 1.7  2006/10/25 02:08:38  오재훈
 * main으로 실행하면 배치 인포를 받지 않기 위해서 재실행가능여부를 parameter로 받게 수정.
 *
 * Revision 1.6  2006/09/28 05:40:44  오재훈
 * 재실행 여부 = N이면 배치 기준일에 실행되었으면 Exception발생
 *
 * Revision 1.5  2006/09/15 06:17:23  오재훈
 * *** empty log message ***
 *
 * Revision 1.4  2006/09/14 04:21:49  오재훈
 * *** empty log message ***
 *
 * Revision 1.3  2006/09/13 13:09:40  오재훈
 * 배치 수동 실행까지 적용.
 *
 * Revision 1.2  2006/09/12 09:04:31  오재훈
 * *** empty log message ***
 *
 *
 *
 * </pre>
 ******************************************************************/

public abstract class BaseBatchJob implements Job {

	
////////////////////////상태 코드. ////////////////////////////////        
    /**
     * BATCH_STATE_OF_START (수행 시작)
     */
    public static final String BATCH_STATE_OF_START = "1";
    /**
     * BATCH_STATE_OF_END_SUCESSFULLY(정상 종료)
     */
    public static final String BATCH_STATE_OF_END_SUCESS = "0";    
    /**
     * BATCH_STATE_OF_END_FAIL(비정상 종료)
     */
    public static final String BATCH_STATE_OF_END_FAIL = "-1";
    /**
     * BATCH_STATE_OF_KILLED(강제 종료)
     */
    public static final String BATCH_STATE_OF_KILLED = "4";    
    
    /**
     * stateCode(배치 상태코드)
     */
    protected String stateCode;
    
    //프로그램 종료시 main()에서 exit()에 셋팅할 성공 정보
    protected static final int SUCCESS_CODE = 0;
    
    //프로그램 종료시 main()에서 exit()에 셋팅할 실패 정보    
    protected static final int FAIL_CODE_OF_BATCHEXCEPTION = -98;
    
    //프로그램 종료시 main()에서 exit()에 셋팅할 실패 정보    
    protected static final int FAIL_CODE_OF_EXCEPTION = -99;
    
    /**
     * 현재 수행중인 배치의 상태. 강제 종료 상태일 경우 false 리턴.
     * @return
     */
    public boolean getBatchState(){
    	if(stateCode == "4") return false;
    	return true;
    }
    /**
     * 현재 프로그램 상태 코드 셋팅(강제 종료시 BATCH_STATE_OF_KILLED 셋팅)
     * @param sCode : 상태 코드
     */
    public void setStateCode(String sCode){
    	this.stateCode = sCode;
    }
    
    /**
     * BATCH_LOG FILE NAME key
     */
    public static final String BATCH_LOG_FILE = "batchLogFile";
    
    /**
     * batch job을 수행한 내역 로그 파일 
     */
    protected String logFile="BATCH";
    
    /**
     * result message
     */
    protected String resultMessage="성공";
    
    
    /**
     * 스케줄러에 의해서 실행되는 실제 구현부를 포함하는 메소드
     * configuration 에 정의된 클래스를 실행시킵니다.
     * 
     * @param context 스케줄러 실행에 필요한 정보를 포함하는 JobExecutionContext
     * @throws org.quartz.JobExecutionException : 스케줄러 실행 중 발생하는 Exception
     * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
     */
    public void execute(JobExecutionContext context) throws JobExecutionException {
        
        try {
        	JobContext jContext = new JobContext();
        	
        	jContext.setJobName(context.getJobDetail().getName());
        	jContext.setJobClass(context.getJobDetail().getJobClass());
        	jContext.setDescription(context.getJobDetail().getDescription());
        	
        	jContext.setPreviousFireTime(context.getPreviousFireTime());
        	jContext.setFireTime(context.getFireTime());
        	jContext.setNextFireTime(context.getNextFireTime());
        	
        	
            execute(jContext);
        } catch (Exception e) {
            LogManager.error("\t##### 다음의 이유로 스케줄 실행 중 오류가 발생하였습니다. [" + e.getMessage() + "]");
        }//end try catch
        
    }//end of execute()

	/**
	 * 개발자가 작성한 배치 프래그램을 호출하고 로그를 기록합니다.
	 * 스케쥴용 배치가 실행될때 수행합니다.
	 * @param context
	 */
    public void execute(JobContext context) {
    	LogManager.info("@@@@@@@@@@@@@@@ BaseBatchJob start "+context.getJobName()+"@@@@@@@@@@@@@@@");
    	
    	BatchContext bCtx = new BatchContext();
    	
    	bCtx.setBatchId(context.getJobName());
//    	bCtx.setWasInstanceId(System.getProperty("SPIDER_INSTANCE_ID"));
    	bCtx.setWasInstanceId(StartupContext.getInstanceId());
    	bCtx.setStartDate(FormatUtil.getToday("yyyyMMddHHmmss"));//시작일시
    	
    	bCtx.setExeUserId("SYSTEM");//디폴트로 시스템이한다.

    	/**
    	 * 배치 파라미터가 붙는경우셋팅해서 넘기기.
    	 */
//    	BatchInfo bInfo = new BatchInfo();
		
    	String properties = "";
    	BatchInfo bInfo = BatchLoader.getBatchInfo(context.getJobName());
    	DataMap batchMap = bCtx.getDataMap();    	

    	if(bInfo.getProperties().trim().length()!=0 && bInfo.getProperties() != null)
    	{
        	properties = bInfo.getProperties();
        	
        	String[] tokenString = StringUtils.splitByWholeSeparator(properties, ";");
    		String[] keyValueUnit = null;

    		for(int i=0; tokenString!=null && i<tokenString.length; i++){
    			keyValueUnit = StringUtils.splitByWholeSeparator(tokenString[i], "=");
    			
    			if(keyValueUnit!=null && keyValueUnit.length>=2){
    				bCtx.setParam(keyValueUnit[0], keyValueUnit[1]);
    			}
    		}
    	}
    	
//    	기준일자 디폴트는 현재일자. -1일 경우 하루 전날. 1일 경우 다음날.
    	bCtx.setStandardDate(FormatUtil.getEvalDate(FormatUtil.getToday(),Integer.parseInt(batchMap.getParameter("StandardDate","0"))));
    	
//    	bCtx.setDataMap(batchMap);

    	try{
    		//1.만약 실행중인 배치일경우 실행중 메세지 리턴. 실행중이 아닐경우 실행인스턴스 맵에 add
        	BatchManager.getInstance().setExecuteBatch(context.getJobName(),this);

    		execute(bCtx,bInfo.getRetryableYn());

    	}catch(Exception e ) {
    		 LogManager.info(logFile, "========= BATCH ERROR MESSAGE ========="
    	                + "\n\t JobId:"+bCtx.getBatchId()
    	                + "\n\t WasInstanceId:"+bCtx.getWasInstanceId()
    	                + "\n\t Execute User Id:" + bCtx.getExeUserId()
    	                + "\n\t 시작 시간:" + bCtx.getStartDate()
    	                + "\n\t 결과메시지:"+e.getMessage()
    	                + "================================");
    		 String errorCode = "";
    		 if(e instanceof BatchException){
    			 errorCode = ((BatchException) e.getCause()).getErrorCode();
    		 }
    		 
    		 throw new SysException(errorCode,e.getCause());
    		 
    	}finally {
			//add했던 인스턴스 해제
    		BatchManager.getInstance().removeExecuteBatch(context.getJobName());
    	}

    	LogManager.info("@@@@@@@@@@@@@@@ BaseBatchJob end ---"+context.getJobName()+"@@@@@@@@@@@@@@@");
    }

    /**
     * main()에서 실행시 호출하는 메소드
     * @param ctx
     */
    public int execute(BatchContext bctx) {
    	return execute(bctx, "");
    }
    
    
    /**
     * 실제 배치 프로그램 실행
     * @param ctx
     */
    public int execute(BatchContext ctx, String retryableYn){
    	int executeState = SUCCESS_CODE;
    	
        this.stateCode=BATCH_STATE_OF_START;
    	long startTimeL=System.currentTimeMillis();
        LogManager.info(logFile, "========= START OF BATCH ========="
                + "\n\t JobId:"+ctx.getBatchId()
                + "\n\t WasInstanceId:"+ctx.getWasInstanceId()
                + "\n\t Execute User Id:" + ctx.getExeUserId()
                + "\n\t 기준일자 :"+ ctx.getStandardDate()
                + "\n\t 시작 시간:" + ctx.getStartDate()
                + "================================");
        
        String ctxStateCode = null;
        
        try {
            startLog(ctx, retryableYn);

            init(ctx);

            service(ctx);
            ctxStateCode = ctx.getStateCode();
            if (!StringUtil.isNull(ctxStateCode)) {
                executeState = Integer.parseInt(ctxStateCode);
            }

            destroy(ctx);
            
        } catch (BatchException e) {
            LogManager.error(logFile, e.getMessage(), e);
            resultMessage = e.getMessage();
            this.stateCode = BATCH_STATE_OF_END_FAIL;
            executeState = FAIL_CODE_OF_BATCHEXCEPTION;
            
			if("".equals(ctx.getStateCode())) {
				ctx.setStateCode(BATCH_STATE_OF_END_FAIL); // 1 상태코드(0:정상 , -1 : 오류)
			}

			if("".equals(ctx.getErrorCode())) {
				ctx.setErrorCode(e.getErrorCode()); // 2 오류코드
			}

			if("".equals(ctx.getErrorReason())) {
				ctx.setErrorReason(e.getMessage()); // 3 오류 발생원인
			}
            
        } catch (Exception e) {
            LogManager.error(logFile, e.getMessage(), e);
            resultMessage = e.getMessage();
            this.stateCode = BATCH_STATE_OF_END_FAIL;
            ctxStateCode = ctx.getStateCode();
            if (!StringUtil.isNull(ctxStateCode)) {
                executeState = Integer.parseInt(ctxStateCode);
            } else {
                executeState = FAIL_CODE_OF_EXCEPTION;
            }
			
            if("".equals(ctx.getStateCode())) {
				ctx.setStateCode(BATCH_STATE_OF_END_FAIL); // 1 상태코드(0:정상 , -1 : 오류)
			}

			if("".equals(ctx.getErrorCode())) {
				ctx.setErrorCode("FRS99999"); // 2 오류코드
			}

			if("".equals(ctx.getErrorReason())) {
				ctx.setErrorReason(e.getMessage()); // 3 오류 발생원인
			}
        } finally {
            double exeTime = (System.currentTimeMillis() - startTimeL) / 1000.0;
            ctx.setEndDate(FormatUtil.getToday("yyyyMMddHHmmss"));
            LogManager.info(logFile, "=========    END OF BATCH ========="
                    + "\n\t JobId:"+ctx.getBatchId()
                    + "\n\t 수행시간 :"+exeTime+"초"
                    + "\n\t 결과코드:"+stateCode
                    + "\n\t 종료 시간:"+ctx.getEndDate()
                    + "\n\t 결과메시지:"+resultMessage
                    + "================================");
            try {
                endLog(ctx);
            } catch (Exception e) {
                LogManager.error(logFile,
                        "JobId:"+ctx.getBatchId()+"의 로그를 저장하지 못함.원인["+e.toString()+"]",e);
            }            
        }    
        return executeState;
    }

    /**
     * 수행 시작 로깅  
     */
    protected void startLog(BatchContext bCtx,String retryableYn){
    	BatchLogger bLoger = new BatchLogger();
    	String executeSeq = bLoger.startLog(bCtx,retryableYn);//배치 시작 로그 남기기.
    	bCtx.setExecuteSeq(executeSeq);//실행회차셋팅
    	
    }

    /**
     * 선행 작업 수행 여부체크 등의 초기화 작업을 수행 합니다.
     * 개발자가 구현 
     */
    protected void init(BatchContext bInfo) throws BatchException {
    	
    }
    
    /**
     * 비지니스 로직을 수행,
     * 배치 수행 내역을 개발자가 구현  
     */
    protected abstract void service(BatchContext bInfo) throws BatchException;
    /**
     * 자원 반납 등의 마무리 작업 및 
     * 최종 수행 결과 코드를 세팅  
     * 개발자가 구현 
     */
    protected void destroy(BatchContext bInfo) throws BatchException {
    	
    }
    
    
    /**
     * 수행 종료 로깅
     */
    protected void endLog(BatchContext bCtx){
    	
    	BatchLogger bLoger = new BatchLogger();
    	bLoger.endLog(bCtx);//종료로그 남기기
    }

}
