/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.monitor.message;

import java.util.HashMap;
import java.util.Map;


/*******************************************************************
 * <pre>
 * 1.설명 
 * 모니터링 카테고리 별로 데이타를 가지고 있다.
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
 * $Log: MonitorMap.java,v $
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
 * Revision 1.1  2008/03/19 05:59:39  김성균
 * 최초 등록
 *
 * </pre>
 ******************************************************************/
public class MonitorMap {
	
	private static Object dummy = new Object();
	
	private HashMap categoryMap;
	
	public MonitorMap() {
		categoryMap = new HashMap();
	}

	public Map getCategory(String category) {
		Map map = (Map) categoryMap.get(category);
		if (map == null) {
			synchronized (dummy) {
    			map = new HashMap();
    			categoryMap.put(category, map);
			}
		}
		return map;
	}
	
	/**
	 * @param category
	 * @param info
	 * @return
	 */
	public Object get(String category, String info) {
		return getCategory(category).get(info);
	}

	/**
	 * @param category
	 * @param info
	 * @param item
	 */
	public void put(String category, String info, Object item) {
		getCategory(category).put(info, item);
	}
	
	/**
	 * @param category
	 * @param info
	 * @return
	 */
	public boolean containsKey(String category, String info) {
		return getCategory(category).containsKey(info);
	}
}
