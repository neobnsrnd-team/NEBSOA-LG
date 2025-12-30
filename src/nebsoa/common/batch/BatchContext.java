/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.batch;

import nebsoa.common.Context;
import nebsoa.common.exception.ParamException;
import nebsoa.common.startup.StartupContext;
import nebsoa.common.util.DataMap;
import nebsoa.common.util.FormatUtil;


/*******************************************************************
 * <pre>
 * 1.설명 
 * Batch 프로그램 로그 정보 클래스
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
 * $Log: BatchContext.java,v $
 * Revision 1.1  2018/01/15 03:39:50  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:46  cvs
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
 * Revision 1.2  2008/09/16 06:47:02  youngseokkim
 * toString() 수정
 *
 * Revision 1.1  2008/08/04 08:54:53  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.5  2008/05/19 07:54:57  오재훈
 * *** empty log message ***
 *
 * Revision 1.3  2008/05/19 07:29:31  신상수
 * finally에서 필수 입력값만을 디폴트로 미리 설정 하도록 수정
 *   setStateCode("0"); // 1 상태코드(0:정상 , -1 : 오류)
 *   setErrorCode("0000"); // 2 오류코드
 * 두 값만 설정하도록 변경
 *
 * Revision 1.2  2008/05/19 06:25:39  오재훈
 * finally에서 필수 입력을 하지 않아도 에러 발생하지 않고 처리되게 수정
 *
 * Revision 1.1  2008/01/22 05:58:28  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:57  안경아
 * *** empty log message ***
 *
 * Revision 1.7  2007/01/12 10:29:54  김성균
 * *** empty log message ***
 *
 * Revision 1.6  2007/01/11 12:00:57  김성균
 * *** empty log message ***
 *
 * Revision 1.5  2007/01/10 08:57:37  김성균
 * DataMap에 put(key, value)하는 메소드 추가
 *
 * Revision 1.4  2006/10/30 05:00:44  오재훈
 * *** empty log message ***
 *
 * Revision 1.3  2006/09/12 09:04:31  오재훈
 * *** empty log message ***
 *
 * Revision 1.2  2006/09/12 00:42:42  오재훈
 * 작업중
 *
 * Revision 1.1  2006/09/11 06:19:02  오재훈
 * *** empty log message ***
 *
 * Revision 1.2  2006/09/09 07:30:16  오재훈
 * *** empty log message ***
 *
 *
 *
 * </pre>
 ******************************************************************/
public class BatchContext {
	/**
     * 배치 프로그램 ID
     */
    public String batchId = "";

    /**
     * 실행될 WAS INSTANCE ID
     */
    public String wasInstanceId = "";
    
    /**
     * standardDate(기준일자) : 배치 작업을 수행한 날짜가 아니라 대상이 되는 날짜.
     * (예: 금요일 데이타를 월요일에 처리 한다면 실제 대상일은 금요일임.
     * 이때 금요일에 해당하는 날짜  데이타를 일컬음)
     */
	public String standardDate=""; 
    
	/**
     * 실행일시
     */
    public String startDate = "";

    /**
     * 수행자 ID
     */
    public String exeUserId = "";

    /**
     * 실행 회차
     */
    public String executeSeq = "";

    /**
     * 종료일시
     */
    public String endDate = "";

    /**
     * 상태 코드
     */
    public String stateCode = "";

    /**
     * 오류 코드
     */
    public String errorCode = "";

    /**
     * 오류 발생 원인
     */
    public String errorReason = "";

    /**
     * 처리대상건수
     */
    public String recordCnt = "";

    /**
     * 총처리건수
     */
    public String executeCnt = "";

    /**
     * 정상처리건수
     */
    public String successCnt = "";

    /**
     * 오류건수
     */
    public String errorCnt = "";
    
    /**
     * 배치상세로그정보
     */
    public BatchDetailLogInfo batchDetailLogInfo = null;

    /**
     * 파라미터 셋팅
     */
    public DataMap dataMap;

    /**
     * 기본 생성자
     */
    public BatchContext() {
        setStartDate(FormatUtil.getToday("yyyyMMddHHmmss"));// 시작시간 설정
        setWasInstanceId(StartupContext.getInstanceId());// 인스턴스 설정
        this.dataMap = new DataMap();
        Context ctx = new Context();
        ctx.setUserId("GUEST");
        this.dataMap.setContext(ctx);
        batchDetailLogInfo = new BatchDetailLogInfo();
        
        //2008-05-19 추가. finally에서 필수 입력을 하지 않아도 에러 발생하지 않고 처리되게 수정 요청.
		setStateCode("0"); // 1 상태코드(0:정상 , -1 : 오류)
		setErrorCode("0000"); // 2 오류코드
		//필수 값만 디폴트값 세팅하게 수정
		//setRecordCnt("0"); // 4 처리대상건수
		//setExecuteCnt("0"); // 5 총처리건수
		//setSuccessCnt("0"); // 6 정상처리건수
		//setErrorCnt("0"); // 7 오류 건수
        
    }

    /**
     * Map에서 데이타 가져오기
     * 
     * @param key :
     *            맵에 저장한 키값
     * @return
     */
    public String getParam(String key) {
        String value = (String) dataMap.get(key);
        if (value == null)
            throw new ParamException(key);
        return value;
    }

    /**
     * Map에서 데이타 가져오기 값이 null이면 defaultValue 리턴
     * 
     * @param key :
     *            맵에 저장한 키값
     * @param defaultValue :
     *            키값이 null이면 리턴
     * @return
     */
    public String getParam(String key, String defaultValue) {
        String value = (String) dataMap.get(key);
        if (value == null)
            return defaultValue;
        return value;
    }

    /**
     * DataMap에 Object type 입력
     * 
     * @param key :
     *            맵에 저장할 키값
     * @param value :
     *            맵에 저장할 데이타
     */
    public void setParam(Object key, Object value) {
        dataMap.put(key, value);
    }

    /**
     * DataMap에 int type 입력
     * 
     * @param key :
     *            맵에 저장할 키값
     * @param value :
     *            맵에 저장할 데이타
     */
    public void setParam(String key, int value) {
        dataMap.put(key, Integer.valueOf(value));
    }

    /**
     * DataMap에 long type 입력
     *
     * @param key :
     *            맵에 저장할 키값
     * @param value :
     *            맵에 저장할 데이타
     */
    public void setParam(String key, long value) {
        dataMap.put(key, Long.valueOf(value));
    }

    /**
     * DataMap에 float type 입력
     *
     * @param key :
     *            맵에 저장할 키값
     * @param value :
     *            맵에 저장할 데이타
     */
    public void setParam(String key, float value) {
        dataMap.put(key, Float.valueOf(value));
    }

    /**
     * DataMap에 double type 입력
     *
     * @param key :
     *            맵에 저장할 키값
     * @param value :
     *            맵에 저장할 데이타
     */
    public void setParam(String key, double value) {
        dataMap.put(key, Double.valueOf(value));
    }

    /**
     * DataMap에 boolean type 입력
     *
     * @param key :
     *            맵에 저장할 키값
     * @param value :
     *            맵에 저장할 데이타
     */
    public void setParam(String key, boolean value) {
        dataMap.put(key, Boolean.valueOf(value));
    }

    public DataMap getDataMap() {
        return dataMap;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getErrorCnt() {
        return errorCnt;
    }

    public void setErrorCnt(String errorCnt) {
        this.errorCnt = errorCnt;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorReason() {
        return errorReason;
    }

    public void setErrorReason(String errorReason) {
        this.errorReason = errorReason;
    }

    public String getExecuteCnt() {
        return executeCnt;
    }

    public void setExecuteCnt(String executeCnt) {
        this.executeCnt = executeCnt;
    }

    public String getExecuteSeq() {
        return executeSeq;
    }

    public void setExecuteSeq(String executeSeq) {
        this.executeSeq = executeSeq;
    }

    public String getExeUserId() {
        return exeUserId;
    }

    public void setExeUserId(String exeUserId) {
        this.exeUserId = exeUserId;
        this.dataMap.getContext().setUserId(exeUserId);// 전문 통신이 필요할 경우를 위해 셋팅
    }

    public String getRecordCnt() {
        return recordCnt;
    }

    public void setRecordCnt(String recordCnt) {
        this.recordCnt = recordCnt;
    }

    public String getStandardDate() {
        return standardDate;
    }

    public void setStandardDate(String standardDate) {
        this.standardDate = standardDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getSuccessCnt() {
        return successCnt;
    }

    public void setSuccessCnt(String successCnt) {
        this.successCnt = successCnt;
    }

    public String getWasInstanceId() {
        return wasInstanceId;
    }

    public void setWasInstanceId(String wasInstanceId) {
        this.wasInstanceId = wasInstanceId;
    }

	/**
     * @return Returns the batchDetailLogInfo.
     */
    public BatchDetailLogInfo getBatchDetailLogInfo() {
        return batchDetailLogInfo;
    }

    /**
     * @param batchDetailLogInfo The batchDetailLogInfo to set.
     */
    public void setBatchDetailLogInfo(BatchDetailLogInfo batchDetailLogInfo) {
        this.batchDetailLogInfo = batchDetailLogInfo;
    }

    public String toString() {

		String ret = "batchId = "+ this.batchId
			+" ,wasInstanceId = "+ this.wasInstanceId
			+" ,standardDate = "+ this.standardDate 
			+" ,startDate = "+ this.startDate
			+" ,exeUserId = "+ this.exeUserId
			+" ,executeSeq = "+ this.executeSeq
			+" ,endDate = "+ this.endDate
			+" ,stateCode = "+ this.stateCode
			+" ,errorCode = "+ this.errorCode
			+" ,errorReason = "+ this.errorReason
			+" ,recordCnt = "+ this.recordCnt
			+" ,executeCnt = "+ this.executeCnt
			+" ,successCnt = "+ this.successCnt
			+" ,errorCnt = "+ this.errorCnt
			+" ,dataMap = "+ this.dataMap;     

		return ret;
	}
}
