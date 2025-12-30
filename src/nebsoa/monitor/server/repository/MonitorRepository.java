/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.monitor.server.repository;

import nebsoa.monitor.data.MonitorDataVo;

import org.apache.commons.collections.map.MultiKeyMap;


/*******************************************************************
 * <pre>
 * 1.설명 
 * 각 WAS별로 수집된 모니터링 데이터에 대한 통합 저장소이다. 
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
 * $Log: MonitorRepository.java,v $
 * Revision 1.1  2018/01/15 03:39:54  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:40  cvs
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
 * Revision 1.2  2008/04/22 02:54:14  홍윤석
 * 모니터링 데이터 remove method 추가
 *
 * Revision 1.1  2008/03/18 01:08:20  김성균
 * 최초 등록
 *
 * </pre>
 ******************************************************************/
public class MonitorRepository {
	
	private MultiKeyMap monitorDatas;
	
	public MonitorRepository() {
		monitorDatas = new MultiKeyMap();
	}

	/**
	 * @param wasId
	 * @param category
	 * @param vo
	 */
	public void update(String wasId, String category, MonitorDataVo vo) {
		monitorDatas.put(wasId, category, vo);
	}
	
	/**
	 * @param wasId
	 * @param category
	 */
	public MonitorDataVo getMonitorDataVo(String wasId, String category) {
		return (MonitorDataVo) monitorDatas.get(wasId, category);
	}
	
	/**
	 * @param wasId
	 * @param category
	 */
	public void removeMonitorDataVo(String wasId, String category) {
		monitorDatas.remove(wasId, category);
	}
	
}
