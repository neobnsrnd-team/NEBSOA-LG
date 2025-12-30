/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.cache;

import java.util.HashMap;

import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;

/*******************************************************************
 * <pre>
 * 1.설명 
 * cache할 데이타를 저장하는 글로벌한 클래스
 * HashMap사용법과 동일 하다.
 * 단 한 객체를 공유해야 하므로, 싱글톤으로 사용하도록 하였다. 
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
 * $Log: CacheMap.java,v $
 * Revision 1.1  2018/01/15 03:39:49  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:30  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:23  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:26  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:51  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:23  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:37:38  안경아
 * *** empty log message ***
 *
 * Revision 1.3  2006/06/19 04:10:16  안경아
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class CacheMap extends HashMap {

	/**
	 * 싱글톤을 위한 인스턴스 변수
	 */
	private static CacheMap instance;

	static {
		instance = new CacheMap();
	}

	/**
	 * @return	CacheMap 인스턴스
	 */
	public static CacheMap getInstance() {
		synchronized (instance) {
			if (instance == null) {
				instance = new CacheMap();
			}
		}
		return instance;
	}

	/* (non-Javadoc)
	 * @see java.util.HashMap#put(K, V)
	 */
	public Object put(Object key, Object value) {
		if (key == null) {
			throw new SysException("CacheMap.put의 key 값은 Null일 수  없습니다.");
		} else if (value == null) {
			throw new SysException("CacheMap.put의 VALUE 값은 Null일 수  없습니다.");
		} else {
			return super.put(key, value);
		}
	}

	/* (non-Javadoc)
	 * @see java.util.HashMap#get(java.lang.Object)
	 */
	public Object get(Object key) {
		if (key == null) {
			throw new SysException("CacheMap.put의 key 값은 Null일 수  없습니다.");
		} else {
			Object obj = super.get(key);
			if (obj == null) {
				LogManager.debug("CacheMap.get:해당 key 값(" + key
						+ ")으로 등록된 객체 없음.");
			}
			return obj;
		}
	}

	public static void main(String[] args) {
		CacheMap map = CacheMap.getInstance();
		map.put("123", "456");
		map.get("123");
		map.get("456");
		CacheMap map2 = CacheMap.getInstance();
		map2.get("123");
	}
}
