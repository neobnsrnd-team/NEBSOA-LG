/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.batch;

import java.util.Map;

import nebsoa.common.exception.ParamException;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.FormatUtil;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 배치 처리 flow를 정의한 기본 클래스이며, 업무별 배치 처리를 구현한 
 * 클래스는 이 클래스를 상속 받아 만든다
 * 
 * 2.사용법
 * method document 참조.
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author 이종원
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 
 * $$Log: BaseBatchProcessor.java,v $
 * $Revision 1.1  2018/01/15 03:39:50  cvs
 * $*** empty log message ***
 * $
 * $Revision 1.1  2016/04/15 02:22:47  cvs
 * $neo cvs init
 * $
 * $Revision 1.1  2011/07/01 02:13:51  yshong
 * $*** empty log message ***
 * $
 * $Revision 1.1  2008/11/18 11:27:24  김성균
 * $*** empty log message ***
 * $
 * $Revision 1.1  2008/11/18 11:01:27  김성균
 * $LGT Gateway를 위한 프로젝트로 분리
 * $
 * $Revision 1.1  2008/08/04 08:54:53  youngseokkim
 * $*** empty log message ***
 * $
 * $Revision 1.1  2008/01/22 05:58:28  오재훈
 * $패키지 리펙토링
 * $
 * $Revision 1.1  2007/11/26 08:38:57  안경아
 * $*** empty log message ***
 * $
 * $Revision 1.3  2007/03/07 02:53:45  김성균
 * $일부 로그 INFO 레벨로 변경
 * $
 * $Revision 1.2  2006/09/09 10:31:15  이종원
 * $최초작성
 * $$
 * </pre>
 ******************************************************************/
public abstract class BaseBatchProcessor {
    
    /**
     * 배치 기준 일자의 key
     */
    public static final String BATCH_DATE = "batchDate";
    /**
     * TARGET_ID key
     */
    public static final String TARGET_ID = "targetId";
    /**
     * SOURCE_ID key
     */
    public static final String SOURCE_ID = "sourceId";
    /**
     * BATCH_JOB_ID key
     */
    public static final String BATCH_JOB_ID = "batchJobId";
    /**
     * BATCH_JOB_NAME key
     */
    public static final String BATCH_JOB_NAME = "batchJobName";
        
    /**
     * BATCH_LOG FILE NAME key
     */
    public static final String BATCH_LOG_FILE = "batchLogFile";
    
////////////////////////상태 코드. ////////////////////////////////        
    /**
     * BATCH_STATE_OF_START (수행 시작)
     */
    public static final String BATCH_STATE_OF_START = "1";
    /**
     * BATCH_STATE_OF_END_SUCESSFULLY(정상 종료)
     */
    public static final String BATCH_STATE_OF_END_SUCESS = "2";    
    /**
     * BATCH_STATE_OF_END_FAIL(비정상 종료)
     */
    public static final String BATCH_STATE_OF_END_FAIL = "3";
    /**
     * BATCH_STATE_OF_KILLED(강제 종료)
     */
    public static final String BATCH_STATE_OF_KILLED = "4";    
//////////////////////// 배치 작업별 공통 속성. ////////////////////////////////    
    
    /**
     * batch job을 수행하는데 필요한 파라미터 
     */
	protected Map param;
    
    /**
     * batch job을 수행한 내역 로그 파일 
     */
    protected String logFile="BATCH";

    /**
     * batch job targetId
     */
    protected String targetId;
    /**
     * batch job sourceId
     */
    protected String sourceId;
    /**
     * batch job Id
     */
    protected String batchJobId;
    /**
     * batchJobName
     */
    protected String batchJobName;

    /**
     * batchDate : 배치 작업을 수행한 날짜가 아니라 대상이 되는 날짜.
     * (예: 금요일 데이타를 월요일에 처리 한다면 실제 대상일은 금요일임.
     * 이때 금요일에 해당하는 날짜  데이타를 일컬음)
     */
    protected String batchDate;
    /**
     * startTime
     */
    protected String startTime;
    /**
     * end Time
     */
    protected String endTime;
    /**
     * result code
     */
    protected String resultCode;
    
    /**
     * result message
     */
    protected String resultMessage="성공";
    


	/**
	 * 기본 생성자
	 */
	public BaseBatchProcessor() {
        
	}
    
    /**
     * Framework에서 자동으로 호출 하는 메소드 입니다.
     * 처리 순서는 다음과 같습니다.
     * 1. 필수 입력 내용 추출하여 변수에 저장
     * 2. startLog();
     * 3. init();
     * 4. service();
     * 5. destroy();
     * 6. endLog();
     * 이중에 1,6번은 프레임웍 레벨에서 구현 되며,
     * 3,4,5는 개발자가 구현 해야 할 메소드 입니다.
     */
    public void execute(Map paramData){
        //이 파라미터에는 기본적으로 위에 상수로 정의한 값들이 있어야 함.
        this.param=paramData;        
        this.logFile=getParam(BATCH_LOG_FILE,"BATCH");
        this.sourceId=getParam(SOURCE_ID);
        this.targetId=getParam(TARGET_ID);
        this.batchJobId=getParam(BATCH_JOB_ID);
        this.batchJobName=getParam(BATCH_JOB_NAME,batchJobId);
        this.batchDate=getParam(BATCH_DATE);
        this.startTime=FormatUtil.getToday("yyyyMMddHHmmss");
        long startTimeL=System.currentTimeMillis();
        this.resultCode=BATCH_STATE_OF_START;
        LogManager.info(logFile, "========= START OF BATCH ========="
                + "\n\tJobId:"+batchJobId
                + "\n\tSource:"+sourceId
                + "\n\tTarget:"+targetId
                + "\n\t기준일자 :"+batchDate
                + "================================");
        
        try{
            startLog();
            LogManager.info(logFile,batchJobId+" init() =========");
            init();
            LogManager.info(logFile,batchJobId+" service() ======");
            service();
            LogManager.info(logFile,batchJobId+" destroy() ======");
            destroy();
        }catch(BatchException e){
            LogManager.error(logFile, e.getMessage(),e);
            resultMessage=e.getMessage();
            this.resultCode=BATCH_STATE_OF_END_FAIL;
        }finally{
            double exeTime=(System.currentTimeMillis()-startTimeL)/1000.0;
            this.endTime=FormatUtil.getToday("yyyyMMddHHmmss");
            LogManager.info(logFile, "=========    END OF BATCH ========="
                    + "\n\tJobId:"+batchJobId
                    + "\n\t수행시간 :"+exeTime+"초"
                    + "\n\t결과코드:"+resultCode
                    + "\n\t결과메시지:"+resultMessage
                    + "================================");
            try{
                endLog();
            }catch(Exception e){
                LogManager.error(logFile,
                        "JobId:"+batchJobId+"의 로그를 저장하지 못함.원인["+e.toString()+"]",e);
            }
        }
    }

    private String getParam(String key) {
        String value= (String) param.get(key);
        if(value == null) throw new ParamException(key);
        return value;
    }
    
    private String getParam(String key,String defaultValue) {
        String value= (String) param.get(key);
        if(value == null) return defaultValue;
        return value;
    }

    /**
     * 선행 작업 수행 여부체크 등의 초기화 작업을 수행 합니다  
     * 개발자가 구현 
     */
    protected abstract void init() throws BatchException;    
    /**
     * 비지니스 로직을 수행,
     * 배치 수행 내역을 개발자가 구현  
     */
    protected abstract void service() throws BatchException;;    
    /**
     * 자원 반납 등의 마무리 작업 및 
     * 최종 수행 결과 코드를 세팅  
     * 개발자가 구현 
     */
    protected abstract void destroy() throws BatchException;; 
    
    /**
     * 수행 시작 시간 로깅  
     * (framework레벨에서 구현)
     */
    protected abstract void startLog(); 
    
    /**
     * 수행 종료시간 로깅
     * (framework레벨에서 구현)
     */
    protected abstract void endLog(); 
    
	
}