/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.batch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 배치를 수행한 상세처리 로그 정보 
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
 * $Log: BatchDetailLogInfo.java,v $
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
 * Revision 1.1  2008/01/22 05:58:28  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:57  안경아
 * *** empty log message ***
 *
 * Revision 1.4  2007/01/25 00:08:24  김성균
 * 배치수행인스턴스ID 추가
 *
 * Revision 1.3  2007/01/12 03:04:12  김성균
 * addErrorMap시 오류수정
 *
 * Revision 1.2  2007/01/12 01:13:38  김성균
 * 직렬화 처리
 *
 * Revision 1.1  2007/01/11 12:00:57  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class BatchDetailLogInfo implements Serializable {
    
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 7974645675867534154L;

    private String 배치아이디;
    
    private String 배치수행인스턴스ID;

    private String 배치설명;

    private String 배치기준일자;

    private String 최초작업일시;

    private String 최종작업일시;

    private String 배치실행회차;

    private String 배치상태;

    private String 처리대상건수;

    private String 총처리건수;

    private String 정상처리건수;

    private String 오류건수;
    
    private ArrayList 오류;

    /**
     * @return Returns the 배치수행인스턴스ID.
     */
    public String getWasInstanceId() {
        return 배치수행인스턴스ID;
    }

    /**
     * @param 배치수행인스턴스id The 배치수행인스턴스ID to set.
     */
    public void setWasInstanceId(String wasInstanceId) {
        this.배치수행인스턴스ID = wasInstanceId;
    }

    /**
     * @return Returns the 배치기준일자.
     */
    public String getStandardDate() {
        return 배치기준일자;
    }

    /**
     * @param 배치기준일자 The 배치기준일자 to set.
     */
    public void setStandardDate(String standardDate) {
        this.배치기준일자 = standardDate;
    }

    /**
     * @return Returns the 배치상태.
     */
    public String getBatchStatus() {
        return 배치상태;
    }

    /**
     * @param 배치상태 The 배치상태 to set.
     */
    public void setBatchStatus(String batchStatus) {
        this.배치상태 = batchStatus;
    }

    /**
     * @return Returns the 배치설명.
     */
    public String getBatchDesc() {
        return 배치설명;
    }

    /**
     * @param 배치설명 The 배치설명 to set.
     */
    public void setBatchDesc(String batchDesc) {
        this.배치설명 = batchDesc;
    }

    /**
     * @return Returns the 배치실행회차.
     */
    public String getExecuteSeq() {
        return 배치실행회차;
    }

    /**
     * @param 배치실행회차 The 배치실행회차 to set.
     */
    public void setExecuteSeq(String executeSeq) {
        this.배치실행회차 = executeSeq;
    }

    /**
     * @return Returns the 배치아이디.
     */
    public String getBatchId() {
        return 배치아이디;
    }

    /**
     * @param 배치아이디 The 배치아이디 to set.
     */
    public void setBatchId(String batchId) {
        this.배치아이디 = batchId;
    }

    /**
     * @return Returns the 오류.
     */
    public ArrayList getErrorList() {
        return 오류;
    }

    /**
     * @param 오류 The 오류 to set.
     */
    public void addErrorMap(Map errorMap) {
        if (오류 == null) {
                    오류 = new ArrayList(10);
        }
                오류.add(errorMap);
    }

    /**
     * @return Returns the 오류건수.
     */
    public String getErrorCnt() {
        return 오류건수;
    }

    /**
     * @param 오류건수 The 오류건수 to set.
     */
    public void setErrorCnt(String errorCnt) {
        this.오류건수 = errorCnt;
    }

    /**
     * @return Returns the 정상처리건수.
     */
    public String getSuccessCnt() {
        return 정상처리건수;
    }

    /**
     * @param 정상처리건수 The 정상처리건수 to set.
     */
    public void setSuccessCnt(String successCnt) {
        this.정상처리건수 = successCnt;
    }

    /**
     * @return Returns the 처리대상건수.
     */
    public String getRecordCnt() {
        return 처리대상건수;
    }

    /**
     * @param 처리대상건수 The 처리대상건수 to set.
     */
    public void setRecordCnt(String recordCnt) {
        this.처리대상건수 = recordCnt;
    }

    /**
     * @return Returns the 총처리건수.
     */
    public String getExecuteCnt() {
        return 총처리건수;
    }

    /**
     * @param 총처리건수 The 총처리건수 to set.
     */
    public void setExecuteCnt(String executeCnt) {
        this.총처리건수 = executeCnt;
    }

    /**
     * @return Returns the 최종작업일시.
     */
    public String getEndDate() {
        return 최종작업일시;
    }

    /**
     * @param 최종작업일시 The 최종작업일시 to set.
     */
    public void setEndDate(String endDate) {
        this.최종작업일시 = endDate;
    }

    /**
     * @return Returns the 최초작업일시.
     */
    public String getStartDate() {
        return 최초작업일시;
    }

    /**
     * @param 최초작업일시 The 최초작업일시 to set.
     */
    public void setStartDate(String startDate) {
        this.최초작업일시 = startDate;
    }
}
