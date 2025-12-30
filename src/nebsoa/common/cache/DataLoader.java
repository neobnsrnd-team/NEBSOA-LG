/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.cache;

import nebsoa.common.util.DataMap;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 캐쉬하고 있는 데이터를 로딩하는 클래스
 * 
 * 
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
 * $Log: DataLoader.java,v $
 * Revision 1.1  2018/01/15 03:39:49  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:29  cvs
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
 * Revision 1.3  2006/06/30 08:23:24  오재훈
 * getDataMap() 추가.
 *
 * Revision 1.2  2006/06/19 04:10:16  안경아
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class DataLoader {
	
    protected Loadable loader;
    
	/**
     * 그룹 아이디
     */
    protected String groupId;
    
	/**
	 * 캐쉬하고 있는 데이타
	 */
	protected DataMap dataMap;
    
	/**
	 * 캐쉬 여부
	 */
    protected boolean useCache = true;
    
	/**
	 * 그룹에 해당하는 로더를 생성합니다.
	 * 
	 * @param groupId 그룹 아이디
	 */
	public DataLoader(String groupId, Loadable loader) {
		this.groupId = groupId;
		this.loader = loader;
		this.dataMap = new DataMap();
		load();
	}
	
	public void load() {
		loader.load(groupId, dataMap);
	}
	
	public String getString(String key) {
		return dataMap.getString(key);
	}
	
	/**
	 * @return Returns the useCache.
	 */
	public boolean isUseCache() {
		return useCache;
	}

	/**
	 * @param useCache The useCache to set.
	 */
	public void setUseCache(boolean useCache) {
		this.useCache = useCache;
	}

	/**
	 * 현재 로드된 dataMap 리턴
	 * @return
	 */
	public DataMap getDataMap() {
		return dataMap;
	}
    
}
