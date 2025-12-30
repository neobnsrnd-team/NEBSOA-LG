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
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

import nebsoa.common.Context;
import nebsoa.common.jdbc.DBManager;
import nebsoa.common.log.LogManager;
import nebsoa.common.startup.StartupContext;
import nebsoa.common.util.FormatUtil;
import nebsoa.common.util.PropertyManager;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 현재 request 처리를 위해 수행되고 있는 thread를 실시간 보여주기 위한 메니져 클래스
 * 
 * 2.사용법
 *  싱글톤으로 되어있음.
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author 이종원
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 
 * $Log: WorkingThreadMonitorManager.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:29  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.2  2009/07/16 03:23:27  김보라
 * 로그보강
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
 * Revision 1.2  2008/04/07 06:34:12  오재훈
 * 디버깅 로그 추가.
 *
 * Revision 1.1  2008/01/22 05:58:33  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.2  2007/11/30 09:46:53  안경아
 * DB NAME 지정
 *
 * Revision 1.1  2007/11/26 08:38:44  안경아
 * *** empty log message ***
 *
 * Revision 1.3  2007/11/02 09:18:24  jwlee
 * 프로세스 로그를 실시간 on/off및  프로세스 강제 종료 기능 추가
 *
 * Revision 1.2  2007/11/02 05:24:44  jwlee
 * 가비지 콜렉션 될 수 있도록 보완
 *
 * Revision 1.1  2007/10/25 08:21:19  jwlee
 * 현재 수행중인 Thread모니터링 클래스 최초 작성
 *
 * </pre>
 ******************************************************************/ 
public class WorkingThreadMonitorManager {

    static WorkingThreadMonitorManager instance = new WorkingThreadMonitorManager();
    
    public static WorkingThreadMonitorManager getInstance(){
        if(instance==null)instance = new WorkingThreadMonitorManager();
        return instance;
    }
    
    private WorkingThreadMonitorManager(){
        cache = new ConcurrentHashMap();
    }
    
    ConcurrentHashMap cache ;
    
    /**
     * 해당 카테고리에서 해당 통계 데이타 update
     */
    public void addContext(Context ctx ) {
    	return;
    }
    
    /**
     * 해당 카테고리에서 해당 통계 데이타 update
     */
    public void removeContext(Context ctx ) {
    	return;
    }
    
    
    public void stopContextProcess(String trxId){
    	return;
    }
    
    public void setLogEnabled(String trxId){
    	return;
    }
    
    public void setLogDisabled(String trxId){
    	return;
    }
    
    public String getContexMonitorData(String trxId){
    	return "No data";
    }
    
//    
//    class WorkingThreadReportMaker implements Runnable {
//        public void run(){
//            getInstance().saveReport();
//        }
//    }
//    
    
    public Map getCachedData(){
    	return cache;
    }
    /**
     * 특정 시간 이상 수행 된 것만 추출하여 리턴한다.
     * @param threshold
     * @return
     */
    public Map getMonitorData(int threshold){
    	
    	return cache;
    }
    
    
    /**
     * 로그 정보들을 DB에 저장
     *
     */
    public void saveReport(){
    }
    
    
 
}
