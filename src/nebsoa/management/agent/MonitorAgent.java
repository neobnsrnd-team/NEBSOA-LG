/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.management.agent;

import java.util.ArrayList;
import java.util.HashMap;

import nebsoa.common.monitor.ContextMonitorManager;
import nebsoa.common.monitor.SysMonitor;
import nebsoa.common.monitor.WorkingThreadMonitorManager;
import nebsoa.common.monitor.message.MessageMonitor;
import nebsoa.common.monitor.message.MessageStatisticalObject;
import nebsoa.common.util.DataMap;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 모니터링객체의 Agent 기능을 수행하는 클래스
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
 * $Log: MonitorAgent.java,v $
 * Revision 1.1  2018/01/15 03:39:49  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:34  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:23  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:27  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.4  2008/04/07 00:34:16  오재훈
 * 모니터링 정보 추가(외환은행버전과 병합)
 *
 * Revision 1.3  2008/03/20 06:07:30  김성균
 * 성능모니터링 데이타를 ArrayList로 가져오는 기능 추가
 *
 * Revision 1.2  2008/03/19 06:01:28  김성균
 * MessageMonitor 싱글톤으로 변경
 *
 * Revision 1.1  2008/01/22 05:58:35  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:44  안경아
 * *** empty log message ***
 *
 * Revision 1.4  2007/09/17 05:51:17  안경아
 * *** empty log message ***
 *
 * Revision 1.3  2007/06/06 10:12:03  최수종
 * 성능모니터링 결과를 ArrayList형식으로 리턴하는 메소드 추가
 *
 * Revision 1.2  2006/10/10 08:27:10  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2006/10/08 07:28:10  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class MonitorAgent {

	/**
     * 성능 모니터링 정보를 수집한다.
     * 
     * @param map
     * @return HTML 결과스트링
     */
    public static String getHtmlReport(DataMap map) {
    	return "";
    }
    
    
    /**
     * 성능 모니터링 정보를 수집한다.
	 * @param map
	 * @return <code>MonitorData</code> ArrayList
	 */
	public static ArrayList getMonitorDatas(DataMap map) {
		return new ArrayList();
	}
    
	/**
     * 모니터링 카테고리 정보를 수집한다.
     * 
     * @return Iterator
     */
    public static Object[] getCategorys() {
        return SysMonitor.getCategorys();
    }
    
    
	/**
     * 모니터링 정보를 수집한다.
     * 
     * @param map
     * @return ArrayList
     */
    public static ArrayList getObjectReport(DataMap map) {
    	return new ArrayList();
    }    
    
    /**
     * 수집된 전체 데이터를 반환합니다.
     * <p>
     * 각 기관별 <code>MessageStatisticalObject</code>의 배열로 반환됩니다. 
     * </p>
     * 
     * @return 결과 데이터 배열
     * @see MessageStatisticalObject
     */
    public static MessageStatisticalObject[] getAllStatisticalObject() {
        return null;
    }

    
    /**
     * 수집된 전체 데이터를 삭제합니다.
     * <p>
     * 결과값이 string으로 반환됩니다. 
     * </p>
     * 
     * @return 결과 데이터 배열
     * @see MessageStatisticalObject
     */
    public static String doMonitorClear() {
    	int result = 0;
    	SysMonitor.clearAllCategoryData();
    	result = 1;
        return result+"";
    }    
    
    /**
     * 모니터링 정보를 수집한다.
     * 
     * @param map
     * @return ArrayList
     */
    public static HashMap getContextMonitorData(DataMap map) {
    	return new HashMap();
    }  
    
	/**
     * 모니터링 정보를 수집한다.
     * 
     * @param map
     * @return HashMap
     */
    public static HashMap getContextMonitorData() {
    	return new HashMap();
    }  
    
    /**
     * 수집된 Context monitor 데이터를 가져온 후 삭제합니다.
     * <p>
     * 결과값이 string으로 반환됩니다. 
     * </p>
     * 
     * @return 결과 데이터 배열
     * @see MessageStatisticalObject
     */
    public static HashMap getSaveContextMonitorData() {
    	return new HashMap();
    }  
    
	/**
     * 모니터링 정보를 수집한다.
     * 
     * @param map
     * @return HashMap
     */
    public static HashMap getWorkingThreadData(String threshold) {
    	return new HashMap();
    }


	public static String getContextMonitorToStringData(String trxId) {
		return "";
	}


	public static String stopContextProcess(String trxId) {
		String result = "success";
		return result;
	}


	public static String setLogEnabled(String trxId) {
		String result = "success";
		return result;
	}


	public static String setLogDisabled(String trxId) {
		String result = "success";
		return result;
	}     
    
}
