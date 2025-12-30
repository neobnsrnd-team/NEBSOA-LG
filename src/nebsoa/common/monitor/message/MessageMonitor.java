/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
package nebsoa.common.monitor.message;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import nebsoa.common.cache.policy.CacheFactory;
import nebsoa.common.cache.policy.FIFOCache;
import nebsoa.common.util.PropertyManager;
import nebsoa.monitor.MonitorConstants;

import org.shiftone.cache.Cache;


/*******************************************************************
 * <pre>
 * 1.설명 
 * 모니터링 시간, 성공/실패, 수행 시간 등의 모니터링 정보를 처리하는 클래스
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
 * $Log: MessageMonitor.java,v $
 * Revision 1.1  2018/01/15 03:39:52  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:10  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:25  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:28  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:51  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.4  2008/03/21 08:56:50  김성균
 * Cache 저장된 Map을 clone()해서 가져오도록 처리 : java.util.ConcurrentModificationException 때문에...
 *
 * Revision 1.3  2008/03/20 01:52:34  김성균
 * Cache에 addObject()시 동기화 처리 추가
 *
 * Revision 1.2  2008/03/19 06:00:03  김성균
 * 통합 모니터링 관련 수정
 *
 * </pre>
 ******************************************************************/
public class MessageMonitor {
	
    /**
     * 처리상태 - 성공
     */
    public static final int STATUS_SUCCESS = 0;

    /**
     * 처리상태 - 실패
     */
    public static final int STATUS_FAIL = 1;

    private static MessageMonitor instance = new MessageMonitor();
    
    private MonitorMap monitorMap = null;

    private MessageMonitor() {
        monitorMap = new MonitorMap();
    }

    /**
     * Singleton 객체를 반환합니다.
     * <p>
     * 실제로 객체를 직접 사용할 일은 없습니다.
     * </p>
     * @return
     */
    public static MessageMonitor getInstance() {
        if (instance == null) {
            instance = new MessageMonitor();
        }
        return instance;
    }

    /**
     * 데이터를 입력합니다.
     * <p>
     * <code>status</code> 인자는 @{STATUS_SUCCESS}, @{STATUS_FAIL} 중 하나로 결정합니다.
     * </p>
     * 
     * @param info 모니터링 항목
     * @param time 현재 시각 (millisecond)
     * @param execTime 수행 시간 (millisecond)
     * @param status 처리 상태
     */
    public void addData(String category, String info, long time, long execTime, int status) {

    }
    
    /**
     * 수집된 전체 데이터를 반환합니다.
     * <p>
     * 각 모니터링 항목별 <code>MessageStatisticalObject</code>의 배열로 반환됩니다. 
     * </p>
     * 
     * @return 결과 데이터 배열
     */
    public MessageStatisticalObject[] getAllStatisticalObject() {
        return getStat(MonitorConstants.MESSAGE_ORG);
    }
    
    /**
     * 모니터링 통계를 반환합니다.
     * <p>
     * <code>MessageStatisticalObject</code>의 배열로 반환됩니다. 
     * </p>
     * 
     * @return 결과 데이터 배열
     */
    public MessageStatisticalObject[] getStat(String category) {
    	Set set = monitorMap.getCategory(category).keySet();
        MessageStatisticalObject[] mso = new MessageStatisticalObject[set.size()];
        return mso;
    }

    /**
     * 모니터링 통계를 구한다.
     * @param info
     * @return
     */
    private MessageStatisticalObject get(String category, String info) {
    	return null;
    }
    
    protected static boolean isMessageMonitoringChartMode() {
    	return false;    
    }
    
    protected static int getMonitoringChartCacheSize() {
        return PropertyManager.getIntProperty("monitor", "MESSAGE_MONITORING_CHART_CACHE_SIZE", 50 * 60);    
    }
    
    protected static long getMonitoringChartCacheKeepTime() {
        return PropertyManager.getLongProperty("monitor", "MESSAGE_MONITORING_CHART_CACHE_KEEP_TIME",  60 * 1000);
    }
        
    private class MessageMonitorItem {
        
        private Cache cache;
        
        private MessageMonitorItem(String info) {
            cache = CacheFactory.getInstance().getFifoCache(info, getMonitoringChartCacheKeepTime(), getMonitoringChartCacheSize());
        }

        private void add(MessageMonitorData data) {
		}
        
        private Map getCacheMap() {
        	return new Hashtable();
        }
    }
}

    