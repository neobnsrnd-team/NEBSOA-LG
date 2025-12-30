/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.monitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import nebsoa.common.Context;
import nebsoa.common.jdbc.DBManager;
import nebsoa.common.util.FormatUtil;
import nebsoa.common.util.StringUtil;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 특정 모듈의 수행 로그(총 수행 횟수, 평균시간, 최대 시간등) 통계를 Cache 및 
 * 출력 하는 클래스
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
 * $Log: ContextMonitorManager.java,v $
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
 * Revision 1.1  2008/08/04 08:54:55  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.2  2008/01/31 09:22:19  김성균
 * Report 저장모드 설정으로 처리하도록 수정
 *
 * Revision 1.1  2008/01/22 05:58:33  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.3  2007/12/05 07:19:08  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2007/12/05 05:31:03  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2007/11/26 08:38:42  안경아
 * *** empty log message ***
 *
 * Revision 1.9  2007/09/17 11:12:55  최수종
 * MONTYPE -> MON_TYPE 변경
 *
 * Revision 1.8  2007/09/17 07:47:33  최수종
 * *** empty log message ***
 *
 * Revision 1.7  2007/09/17 05:51:05  최수종
 * DBManager호출시 매개변수에 Context.MONITOR_DB 추가
 *
 * Revision 1.6  2007/09/17 05:38:19  최수종
 * *** empty log message ***
 *
 * Revision 1.5  2007/09/17 05:15:55  이종원
 * report maker thread추가
 *
 * Revision 1.4  2007/09/17 04:56:45  이종원
 * *** empty log message ***
 *
 * Revision 1.3  2007/09/17 04:46:15  이종원
 * *** empty log message ***
 *
 * Revision 1.2  2007/09/17 03:40:00  최수종
 * 수행로그 db등록 메소드 추가
 *
 * Revision 1.1  2007/09/17 01:09:32  이종원
 * 최초작성
 *
 * </pre>
 ******************************************************************/ 
public class ContextMonitorManager {

    static ContextMonitorManager instance = new ContextMonitorManager();
    
    public static ContextMonitorManager getInstance(){
        if(instance==null)instance = new ContextMonitorManager();
        return instance;
    }
    
    private ContextMonitorManager(){
        cache = new HashMap();
    }
    
    HashMap cache ;
    
    /**
     * clear cached data.
     * 2007. 9. 17.  이종원 작성
     */
    public void clear(){
        HashMap newBuffer = new HashMap();
        HashMap old = cache;
        cache = newBuffer;
        if(old != null) old.clear();
    }
    
    /**
     * 해당 카테고리에서 해당 통계 데이타 update
     */
    public void addContextMonitorData(ContextMonitorData data) {
        ContextMonitorData existData = 
            (ContextMonitorData) cache.get(data.getContextId());
        if (existData == null) {
            existData = new ContextMonitorData(data.getContextId());
            cache.put(data.getContextId(), existData);
        }
        existData.update(data);
        if (ContextReportTimeChecker.isSaveReportMode() 
        		&& ContextReportTimeChecker.isReportCreateTime()) {
			new ContextReportMakeThread().start();
		}
    }
    
    
    class ContextReportMakeThread extends Thread {
        public void run(){
            ContextMonitorManager.getInstance().saveReport();
        }
    }
    
    
    public HashMap getData(){
        return cache;
    }
    
    /**
     * 캐쉬된 수행로그 정보들을 얻기
     * (수행로그 정보들을 얻은후, 기존의 캐쉬된 수행로그는 초기화)
     * 
     * @return cache
     */
    public HashMap getReportData(){
    	HashMap newBuffer = new HashMap();
        HashMap old = cache;
        cache = newBuffer;
        return old;
    }
    
    /**
     * 수행로그 정보들을 DB에 저장
     *
     */
    public void saveReport(){
    	
        String baseDate = FormatUtil.getToday("yyyyMMdd");  // 현재 년월일
        String baseTime = FormatUtil.getToday("HHmm");  // 현재 시분
        
        // 분값이 '0'단위가 아니라면 무조건 '0'단위로 변경
        // ex. 1102 (11시 02분) -> 1100 (11시 00분)
        if(!baseTime.endsWith("0"))
        {
            baseTime = (baseTime.substring(0, baseTime.length()-1)).concat("0");
        }
        
        
    	HashMap data = getReportData();
    	Set set = data.keySet();
    	Iterator i = set.iterator();
    	
        Object key = null;
        ContextMonitorData contextMonitorData = null;
        while (i.hasNext()) {
            key = i.next();
            contextMonitorData = (ContextMonitorData)data.get(key);
            
            saveReportData(contextMonitorData,baseDate,baseTime);
        }
        //Object[] param = {baseDate,"Context",baseTime,StartupContext.getInstanceId()}; 
        //DBManager.executePreparedUpdate(Context.MONITOR_DB, insertSQl, param);
    }
    
    static String insertSQl=
        "INSERT INTO FWK_MONITOR_HISTORY (MON_DATE, MON_TYPE, TIME_LINE,WAS_ID) values "
        +"(?,?,?,?)";
    
    // 수행로그 정보 Insert쿼리
    private static final String INSERT_CONTEXT_MONITOR_DATA = 
    	"INSERT INTO FWK_CONTEXT_MONITOR ( " +
		"   BASE_DATE, BASE_TIME, CONTEXT_ID, " + 
		"   ACTION_TOT_TIME, ACTION_DBIO_TIME, ACTION_DBIO_COUNT, " + 
		"   LOOKUP_TIME, LOOKUP_COUNT, BIZ_TOT_TIME,  " +
		"   BIZ_DB_R_TIME, BIZ_DB_R_COUNT, BIZ_DB_W_TIME,  " +
		"   BIZ_DB_W_COUNT, BIZ_MSG_TIME, BIZ_MSG_COUNT, " + 
		"   RES_TOT_TIME, TOT_TIME, EXE_COUNT, " + 
		"   MIN_TIME, MAX_TIME, MAX_TRACE_ID, " + 
		"   WAS_ID)  " +
		"VALUES ( ?, ?, ?, " +
		"    ?, ?, ?, " +
		"    ?, ?, ?, " +
		"    ?, ?, ?, " +
		"    ?, ?, ?, " +
		"    ?, ?, ?, " +
		"    ?, ?, ?, ?) ";    	
    
    /**
     * 수행로그 정보(1건)를 DB에 저장
     * 
     * @param contextMonitorData
     */
    private void saveReportData(
            ContextMonitorData contextMonitorData
            ,String baseDate, String baseTime)
    {
    	
    	String contextId = contextMonitorData.getContextId();
    	long actionTotTime = contextMonitorData.getActionTotTime();
    	long actionDBIOTime = contextMonitorData.getActionDBIOTime();
    	int actionDBIOCount = contextMonitorData.getActionDBIOCount();
    	long lookupTime = contextMonitorData.getLookupTime();
    	int lookupCount = contextMonitorData.getLookupCount();
    	long bizTotTime = contextMonitorData.getBizTotTime();
    	long bizDBReadTime = contextMonitorData.getBizDBReadTime();
    	int bizDBReadCount = contextMonitorData.getBizDBReadCount();
    	long bizDBWriteTime = contextMonitorData.getBizDBWriteTime();
    	int bizDBWriteCount = contextMonitorData.getBizDBWriteCount();
    	long bizMsgTime = contextMonitorData.getBizMsgTime();
    	int bizMsgCount = contextMonitorData.getBizMsgCount();
    	long resTotTime = contextMonitorData.getResTotTime();
    	long totTime = contextMonitorData.getTotTime();
    	int exeCount = contextMonitorData.getExeCount();
    	long minTime = contextMonitorData.getMinTime();
    	long maxTime = contextMonitorData.getMaxTime();
    	String maxTraceId = contextMonitorData.getMaxtraceid();
    	String wasId = contextMonitorData.getWasId();
    	
    	ArrayList arrayList = new ArrayList();
    	arrayList.add(baseDate);
    	arrayList.add(baseTime);
    	arrayList.add(StringUtil.NVL(contextId));
    	arrayList.add(Long.valueOf(actionTotTime));
    	arrayList.add(Long.valueOf(actionDBIOTime));
    	arrayList.add(Integer.valueOf(actionDBIOCount));
    	arrayList.add(Long.valueOf(lookupTime));
    	arrayList.add(Integer.valueOf(lookupCount));
    	arrayList.add(Long.valueOf(bizTotTime));
    	arrayList.add(Long.valueOf(bizDBReadTime));
    	arrayList.add(Integer.valueOf(bizDBReadCount));
    	arrayList.add(Long.valueOf(bizDBWriteTime));
    	arrayList.add(Integer.valueOf(bizDBWriteCount));
    	arrayList.add(Long.valueOf(bizMsgTime));
    	arrayList.add(Integer.valueOf(bizMsgCount));
    	arrayList.add(Long.valueOf(resTotTime));
    	arrayList.add(Long.valueOf(totTime));
    	arrayList.add(Integer.valueOf(exeCount));
    	arrayList.add(Long.valueOf(minTime));
    	arrayList.add(Long.valueOf(maxTime));
    	arrayList.add(StringUtil.NVL(maxTraceId));
    	arrayList.add(StringUtil.NVL(wasId));
    	
    	DBManager.executePreparedUpdate(
    			Context.MONITOR_DB, 
    			INSERT_CONTEXT_MONITOR_DATA, 
    			arrayList.toArray());
    }
}
