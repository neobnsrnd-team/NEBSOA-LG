/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common;

import java.io.Serializable;

import nebsoa.common.monitor.ContextMonitorData;
import nebsoa.common.monitor.ContextMonitorManager;
import nebsoa.common.monitor.profiler.Profiler;
import nebsoa.common.startup.StartupContext;
import nebsoa.common.util.FormatUtil;
import nebsoa.common.util.UIDGenerator;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 하나의 요청 및 응답을 처리하는 동안의 상태를 나타내는 클래스.
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
 * $Log: Context.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:33  cvs
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
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.2  2008/02/14 08:59:51  김성균
 * 프로파일 관련 메소드 추가
 *
 * Revision 1.1  2008/01/22 05:58:34  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:39:06  안경아
 * *** empty log message ***
 *
 * Revision 1.28  2007/11/07 06:57:27  jwlee
 * setTrxSerno추가
 *
 * Revision 1.27  2007/11/02 09:18:24  jwlee
 * 프로세스 로그를 실시간 on/off및  프로세스 강제 종료 기능 추가
 *
 * Revision 1.26  2007/09/27 08:09:21  이종원
 * ThreadLocal관련 기능 추가
 *
 * Revision 1.25  2007/09/17 05:48:36  최수종
 * *** empty log message ***
 *
 * Revision 1.24  2007/09/17 02:41:12  이종원
 * Context Monitor 로직 수정
 *
 * Revision 1.23  2007/09/17 02:12:00  이종원
 * Context Monitor 로직 추가
 *
 * Revision 1.22  2007/09/17 01:29:52  이종원
 * startActivity,stopActivity, startStep,endStep 메소드 추가
 *
 * </pre>
 ******************************************************************/
public class Context implements Serializable {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -7234459547525603141L;

    /**
     * 성능 모니터링 객체
     */
    protected Profiler prof;

    /**
     * transaction을 시작한 instance id
     */
    protected String instanceId;

    /**
     * 거래 일자
     */
    protected String transactionDate;

    /**
     * 거래 시각(시분초)
     */
    protected String transactionTime;

    /**
     * UID : 일시(14) + InstanceId(3) + UniqueId(10자리)
     */
    protected String uid;
    
    /**
     * 해당 context가 생성된 request uri
     */
    protected String uri;
    
    /**
     * 사용자ID
     */
    protected String userId;
    
    /**
     * 거래일련번호
     */
    protected String trxSerNo;
    
    /**
     * Comment for <code>startTime</code>
     */
    protected long startTime;
    
    /**
     * 사용자별 로그 사용 여부 플래그
     */
    protected boolean logEnabled = false;
    
    /**
     * 기본 Context이며 추후 반드시 setDataMap등으로 DataMap을 넣어 주어야 한다.
     */
    public Context(){
        this(null);
    }
    
    public ContextMonitorData monitor=null;
    /**
     * 강제 종료 시킬 것인지...
     */
	private boolean forceShutdown=false;
    
    /**
     * Context 성성자
     */
    public Context(Profiler prof) {
        this.prof = prof;
        transactionDate = FormatUtil.getToday("yyyyMMdd");
        transactionTime = FormatUtil.getToday("HHmmss");
        instanceId = StartupContext.getInstanceId();
        if (instanceId == null)
            instanceId = "0000";
        uid = String.valueOf(UIDGenerator.generateUID());
        trxSerNo = transactionDate + transactionTime + instanceId + uid;
        startTime = System.currentTimeMillis();
    }
    
    /**
     * spider framework에서 관리하는 거래 일련번호 
     * <font color='red'>(trancation serial number : yyMMddHHmmss(14)+instanceId(4)+UID(10))</font>
     * @return
     */
    public String getTrxSerNo(){
        return trxSerNo;
    }
    
    public Profiler getProfiler() {
        return prof;
    }

    public void setProfiler(Profiler profiler) {
        this.prof = profiler;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * 날짜+시각 값
     * 
     * @return
     */
    public String getTransactionDateTime() {
        return transactionDate + transactionTime;
    }

    public String getUri() {
        return uri;
    }
    //////////////////// 상세 모니터링 로직 추가 ////////////
    // 현재 진행 구간 관련 상수
    static public String STEP_ACTION="Action";
    static public String STEP_BIZ="Biz";    
    static public String STEP_RESPONSE="Response";
    static public String STEP_CONTROLLER="Controller";
    
    
    // activity 관련 상수
    static public String ACTIVITY_DB_READ="DB_READ";
    static public String ACTIVITY_DB_WRITE="DB_WRITE";
    static public String ACTIVITY_MESSAGE="MESSAGE";
    static public String ACTIVITY_LOOKUP="LOOKUP";
    static public String ACTIVITY_LOGIC="LOGIC";
    
    // 모니터 DB명 상수
    static public String MONITOR_DB = "MONITOR_DB";
    
    /*********************************************************
     * URI별 상세 Monitoring 관련 로직 추가.
     * 2007. 9. 17.  이종원 작성
     * @param uri
     */
    public void setUri(String uri) {
        this.uri = uri;
        monitor = new ContextMonitorData(uri);
        monitor.setWasId(instanceId);
        monitor.setMaxtraceid(trxSerNo);
        monitor.setBaseDate(transactionDate);
        monitor.setBaseTime(transactionTime.substring(0,4));
    }
    
    public void startActivity(String activity){
        if(monitor != null){
            monitor.startActivity(activity);
        }
    }
    
    public void stopActivity(){
        if(monitor != null){
            monitor.stopActivity();
        }
    }
    
    public void startStep(String stepId){
        if(monitor != null){
            monitor.startStep(stepId);
        }
    }
    
    public void stopStep(String stepId){
        if(monitor != null){
            monitor.endStep(stepId);
        }
    }
    
    public void updateMonitorData(){
        ContextMonitorManager.getInstance().
        addContextMonitorData(this.monitor);
    }
    
    

    public String getName() {
        if (uri != null)
            return uri;
        return transactionTime;
    }

    /**
     * @return Returns the userId.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId
     *            The userId to set.
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getStartTime() {
        return startTime;
    }
    
    public String toString(){
        long now = System.currentTimeMillis();        
        return  getUserId()+"|"+getTrxSerNo()+"|"
            +getUri()+"|"+((now-startTime)/1000.0)+"초 경과 중";
    }
    
    /**
     * @return Returns the logEnabled.
     */
    public boolean isLogEnabled() {
        return logEnabled;
    }
    
    /**
     * @return Returns the logEnabled.
     */
    public boolean isLogDisabled() {
        return !logEnabled;
    }

    /**
     * @param logEnabled The logEnabled to set.
     */
    public void setLogEnabled(boolean logEnabled) {
        this.logEnabled = logEnabled;
    }
    
    /**
     * @param logEnabled The logEnabled to set.
     */
    public void setLogEnabled() {
        this.logEnabled = true;
    }
    
    public void setLogDisabled(){
    	logEnabled=false;
    }
    
    

	public void setForceShutdown() {
		this.forceShutdown = true;
	}
	public boolean isForceShutdown() {
		return forceShutdown;
	}

    public static void main(String args[]) {

        for (int i = 0; i < 10; i++) {
            Context ctx = new Context();
            System.out.println(ctx.getTrxSerNo());
        }
    }

    public ContextMonitorData getMonitor() {
        return monitor;
    }

    public void setMonitor(ContextMonitorData monitor) {
        this.monitor = monitor;
    }

    public void updateMonitorData(Context ctx__) {
        ContextMonitorData data = ctx__.getMonitor();        
        if(data==null) return;
        
        monitor.setActionStartTime(data.getActionStartTime());
        monitor.setStartTime(data.getStartTime());
        monitor.setBizStartTime(data.getBizStartTime());
    }

	public void setTrxSerNo(String trxSerNo) {
		this.trxSerNo = trxSerNo;
	}
	
	/**
	 * @param eventMessage
	 */
	public void startProfilerEvent(String eventMessage) {
		Profiler profiler = getProfiler();
		if (profiler != null) {
			profiler.startEvent(eventMessage);
        }
	}
	
	public void stopProfilerEvent() {
		Profiler profiler = getProfiler();
		if (profiler != null) {
			profiler.stopEvent();
        }
	}
}
