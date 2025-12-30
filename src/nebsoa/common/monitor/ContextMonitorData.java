/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.monitor;

import java.io.Serializable;

import nebsoa.common.Context;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.PropertyManager;


/*******************************************************************
 * <pre>
 * 1.설명 
 * url별 상세 모니터링 데이터를 담는 클래스
 * 
 * 2.사용법
 * 
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author 이종원
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 
 * $Log: ContextMonitorData.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:29  cvs
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
 * Revision 1.2  2008/10/29 01:31:11  김성균
 * 로그출력을 LogManager 사용하도록 변경
 *
 * Revision 1.1  2008/08/04 08:54:55  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:33  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:43  안경아
 * *** empty log message ***
 *
 * Revision 1.18  2007/11/02 07:27:20  안경아
 * *** empty log message ***
 *
 * Revision 1.17  2007/10/31 06:44:26  안경아
 * *** empty log message ***
 *
 * Revision 1.16  2007/10/31 05:31:30  안경아
 * *** empty log message ***
 *
 * Revision 1.15  2007/09/27 08:09:21  이종원
 * ThreadLocal관련 기능 추가
 *
 * Revision 1.14  2007/09/18 11:32:28  이종원
 * toString에 시작 시간 디버깅 추가
 *
 * Revision 1.13  2007/09/18 02:30:28  김승희
 * *** empty log message ***
 *
 * Revision 1.12  2007/09/18 02:23:55  김승희
 * min time 버그 수정
 *
 * Revision 1.11  2007/09/17 11:29:24  최수종
 * update()에서 minTime, maxTime 수정
 *
 * Revision 1.10  2007/09/17 11:22:50  최수종
 * *** empty log message ***
 *
 * Revision 1.9  2007/09/17 09:23:43  최수종
 * 직렬화 변수 추가
 *
 * Revision 1.8  2007/09/17 08:34:16  최수종
 * *** empty log message ***
 *
 * Revision 1.7  2007/09/17 07:47:33  최수종
 * *** empty log message ***
 *
 * Revision 1.6  2007/09/17 05:35:45  최수종
 * *** empty log message ***
 *
 * Revision 1.5  2007/09/17 04:42:03  이종원
 * Context Monitor 데이터 산출 로직 수정
 *
 * Revision 1.4  2007/09/17 03:06:56  이종원
 * Context Monitor 로직 수정
 *
 * Revision 1.3  2007/09/17 02:18:28  이종원
 * implements Serializable 추가
 *
 * Revision 1.2  2007/09/17 01:40:27  이종원
 * try~catch추가
 *
 * Revision 1.1  2007/09/17 01:09:32  이종원
 * 최초작성
 *
 * Revision 1.1  2007/09/15 13:09:49  이종원
 * 최초작성
 *
 * </pre>
 ******************************************************************/
public class ContextMonitorData implements Serializable{
	
	/*
	 * 직렬화 키
	 */
	private static final long serialVersionUID = 7307603630134947872L;
	
	///////// 통계 Data Field ////////////////////
    String  baseDate;
    String  baseTime;
    String  contextId;
    long  actionTotTime;
    long  actionDBIOTime;
    int  actionDBIOCount;
    long  lookupTime;
    int  lookupCount;
    long  bizTotTime;
    long  bizDBReadTime;
    int  bizDBReadCount;
    long  bizDBWriteTime;
    int  bizDBWriteCount;
    long  bizMsgTime;
    int  bizMsgCount;
    long  resTotTime;
    long  totTime;
    int  exeCount;
    long  minTime = -1; //최초에는 -1로 할당한다.
    long  maxTime;
    String  maxtraceid="";
    String  wasId;
    
    
    ////////////// Data 출을 위한 알고리즘 Field /////////////
    String currentStep=STEP_ACTION; //현재 진행 구간

    // 현재 진행 구간 관련 상수
    static String STEP_ACTION=Context.STEP_ACTION; //"Action"
    static String STEP_BIZ=Context.STEP_BIZ;//"Biz";    
    static String STEP_RESPONSE=Context.STEP_RESPONSE;//"Response";
    static String STEP_CONTROLLER=Context.STEP_CONTROLLER;//"Controller";
    
    //DBIO,전문 처리 , Logic처리등의 현재 진행 중인 ACTIVITY.
    String currentActivity=ACTIVITY_LOGIC;
    // activity 관련 상수
    static String ACTIVITY_DB_READ=Context.ACTIVITY_DB_READ;//"DB_READ";
    static String ACTIVITY_DB_WRITE=Context.ACTIVITY_DB_WRITE;//"DB_WRITE";
    static String ACTIVITY_MESSAGE=Context.ACTIVITY_MESSAGE;//"MESSAGE";
    static String ACTIVITY_LOOKUP=Context.ACTIVITY_LOOKUP;//"LOOKUP";
    static String ACTIVITY_LOGIC=Context.ACTIVITY_LOGIC;//"LOGIC";
    
    long actionStartTime = 0; // System.currentTimeMillis();
    long bizStartTime=0;
    long responseStartTime=0;    
    
    long startTime=now();
    long currentActivityStartTime=0;
    
    static long now(){
        return System.currentTimeMillis();
    }
    

    public void update(ContextMonitorData data) {
       

    }
    
    
    public void startStep(String stepId){

    }
    
    String referer=STEP_CONTROLLER;
    
    public void endStep(String stepId){

    }
    
    public void finish(){
    }
    
    public void startActivity(String activityId){
    }
    
    public void stopActivity(){
    }
    
    ///////////////// Constructors       /////////////////////
    public ContextMonitorData(String contextId){
        this.contextId = contextId;
    }
    ///////////////// getter ans setters /////////////////////
    
    public int getActionDBIOCount() {
        return actionDBIOCount;
    }
    public void setActionDBIOCount(int actionDBIOCount) {
        this.actionDBIOCount = actionDBIOCount;
    }
    public long getActionDBIOTime() {
        return actionDBIOTime;
    }
    public void setActionDBIOTime(long actionDBIOTime) {
        this.actionDBIOTime = actionDBIOTime;
    }
    public long getActionTotTime() {
        return actionTotTime;
    }
    public void setActionTotTime(long actionTotTime) {
        this.actionTotTime = actionTotTime;
    }
    public String getBaseDate() {
        return baseDate;
    }
    public void setBaseDate(String baseDate) {
        this.baseDate = baseDate;
    }
    public String getBaseTime() {
        return baseTime;
    }
    public void setBaseTime(String baseTime) {
        this.baseTime = baseTime;
    }
    public int getBizDBReadCount() {
        return bizDBReadCount;
    }
    public void setBizDBReadCount(int bizDBReadCount) {
        this.bizDBReadCount = bizDBReadCount;
    }
    public long getBizDBReadTime() {
        return bizDBReadTime;
    }
    public void setBizDBReadTime(long bizDBReadTime) {
        this.bizDBReadTime = bizDBReadTime;
    }
    public int getBizDBWriteCount() {
        return bizDBWriteCount;
    }
    public void setBizDBWriteCount(int bizDBWriteCount) {
        this.bizDBWriteCount = bizDBWriteCount;
    }
    public long getBizDBWriteTime() {
        return bizDBWriteTime;
    }
    public void setBizDBWriteTime(long bizDBWriteTime) {
        this.bizDBWriteTime = bizDBWriteTime;
    }
    public int getBizMsgCount() {
        return bizMsgCount;
    }
    public void setBizMsgCount(int bizMsgCount) {
        this.bizMsgCount = bizMsgCount;
    }
    public long getBizMsgTime() {
        return bizMsgTime;
    }
    public void setBizMsgTime(long bizMsgTime) {
        this.bizMsgTime = bizMsgTime;
    }
    public long getBizTotTime() {
        return bizTotTime;
    }
    public void setBizTotTime(long bizTotTime) {
        this.bizTotTime = bizTotTime;
    }
    public String getContextId() {
        return contextId;
    }
    public void setContextId(String contextId) {
        this.contextId = contextId;
    }
    public int getExeCount() {
        return exeCount;
    }
    public void setExeCount(int exeCount) {
        this.exeCount = exeCount;
    }
    public int getLookupCount() {
        return lookupCount;
    }
    public void setLookupCount(int lookupCount) {
        this.lookupCount = lookupCount;
    }
    public long getLookupTime() {
        return lookupTime;
    }
    public void setLookupTime(long lookupTime) {
        this.lookupTime = lookupTime;
    }
    public long getMaxTime() {
        return maxTime;
    }
    public void setMaxTime(long maxTime) {
        this.maxTime = maxTime;
    }
    public String getMaxtraceid() {
        return maxtraceid;
    }
    public void setMaxtraceid(String maxtraceid) {
        this.maxtraceid = maxtraceid;
    }
    public long getMinTime() {
        return minTime;
    }
    public void setMinTime(long minTime) {
        this.minTime = minTime;
    }
    public long getResTotTime() {
        return resTotTime;
    }
    public void setResTotTime(long resTotTime) {
        this.resTotTime = resTotTime;
    }
    public long getTotTime() {
        return totTime;
    }
    public void setTotTime(long totTime) {
        this.totTime = totTime;
    }
    public String getWasId() {
        return wasId;
    }
    public void setWasId(String wasId) {
        this.wasId = wasId;
    }

    public String getCurrentStep() {
        return currentStep;
    }
    public void setCurrentStep(String currentStep) {
        this.currentStep = currentStep;
    }

    public long getActionStartTime() {
        return actionStartTime;
    }

    public void setActionStartTime(long actionStartTime) {
        this.actionStartTime = actionStartTime;
    }

    public long getBizStartTime() {
        return bizStartTime;
    }

    public void setBizStartTime(long bizStartTime) {
        this.bizStartTime = bizStartTime;
    }

    public long getResponseStartTime() {
        return responseStartTime;
    }

    public void setResponseStartTime(long responseStartTime) {
        this.responseStartTime = responseStartTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(String currentActivity) {
        this.currentActivity = currentActivity;
    }

    public long getCurrentActivityStartTime() {
        return currentActivityStartTime;
    }

    public void setCurrentActivityStartTime(long currentActivityStartTime) {
        this.currentActivityStartTime = currentActivityStartTime;
    }
    
    public void updateActionTotTime(long time){
        actionTotTime =+ time ;
    }
    
    public void updateActionDBIOTime(long time){
        actionDBIOTime=+ time ;
        actionDBIOCount++;
    }
    
    public void updateLookupTime(long time){
        lookupTime=+ time ;
        lookupCount++;
    }
    
    public void updateBizTotTime(long time){
        bizTotTime=+ time ;
    }

    public void updateBizDBReadTime(long time){
        bizDBReadTime=+ time ;
        bizDBReadCount++;
    }
    
    public void updateBizDBWriteTime(long time){
        bizDBWriteTime=+ time ;
        bizDBWriteCount++;
    }
    
    public void updateBizMsgTime(long time){
        bizMsgTime=+ time ;
        bizMsgCount++;
    }

    public void updateResTotTime(long time){
        resTotTime=+ time ;
//        finish();
    }
    
    
    public String toString(){
        return new String();
    }
    public String toStringBiz(){
        return new StringBuffer(1024)        
        .append("\r\n\t contextId:"+contextId )
        .append("\r\n\t bizTotTime:"+(System.currentTimeMillis()-bizStartTime) )
        .append("\r\n\t    bizDBReadTime:"+bizDBReadTime )
        .append("(bizDBReadCount:"+bizDBReadCount +"회)")
        .append("\r\n\t    bizDBWriteTime:"+bizDBWriteTime )
        .append("(bizDBWriteCount:"+bizDBWriteCount +"회)")
        .append("\r\n\t    bizMsgTime:"+bizMsgTime )
        .append("(bizMsgCount:"+bizMsgCount +"회)").toString().trim();
    }    
    public String getTimeMark(){
    	if(getTotTime()>50*1000) return "*****";
    	if(getTotTime()>40*1000) return "****";
    	if(getTotTime()>30*1000) return "***";
    	if(getTotTime()>20*1000) return "**";
    	if(getTotTime()>10*1000) return "*";
    	return "";
    }
    
    
    public static void main(String[] args){
        ContextMonitorData data = new ContextMonitorData("login.jsp");
        

        data.startStep(STEP_ACTION);
            sleep(100);
            data.startActivity(ACTIVITY_LOOKUP);
                sleep(10);
            data.stopActivity();
            //BIZ-1
            data.startStep(STEP_BIZ);
                sleep(200);
                data.startActivity(ACTIVITY_DB_READ);
                    sleep(20);
                data.stopActivity();
            data.endStep(STEP_BIZ);
            
            data.startActivity(ACTIVITY_LOOKUP);
                sleep(100);
            data.stopActivity();
        
        
            //BIZ-2
            data.startStep(STEP_BIZ);
                data.startActivity(ACTIVITY_DB_READ);
                sleep(5000);
                data.stopActivity();
                data.startActivity(ACTIVITY_DB_WRITE);
                    sleep(100);
                data.stopActivity();
                data.startActivity(ACTIVITY_MESSAGE);
                    sleep(1000);
                data.stopActivity();
            data.endStep(STEP_BIZ);
            
            
            data.startActivity(ACTIVITY_DB_WRITE);
                sleep(1000);
            data.stopActivity();
        data.endStep(STEP_ACTION);
        
        data.startStep(STEP_RESPONSE);
            sleep(300);
        data.endStep(STEP_RESPONSE);
        
        data.finish();
        
        System.out.println(data);
    }
    
    static void sleep(long time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
        }
    }


}