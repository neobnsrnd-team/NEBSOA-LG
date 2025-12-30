/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.monitor.server;

import nebsoa.monitor.data.MonitorDataVo;
import nebsoa.monitor.server.repository.MonitorRepository;

/*******************************************************************
 * <pre>
 * 1.설명 
 * TODO MonitorDao 클래스에 대한 주석 넣기
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
 * $Log: MonitorDao.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:27  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:26  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:29  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.3  2008/04/22 02:54:35  홍윤석
 * 모니터링 데이터 remove method 추가
 *
 * Revision 1.2  2008/03/18 05:43:10  김성균
 * 기관별 전문처리 모니터링 데이타 꺼내오는 메소드 추가
 *
 * Revision 1.1  2008/03/18 01:08:20  김성균
 * 최초 등록
 *
 * </pre>
 ******************************************************************/
public class MonitorDao {
	
	private MonitorRepository mr;
	
	MonitorDao() {
	}
	
	MonitorDao(MonitorRepository mr) {
		this.mr = mr;
	}
	
	void setMonitorRepository(MonitorRepository mr) {
		this.mr = mr;
	}

	/**
	 * @param monitorDataVo
	 */
	void update(MonitorDataVo monitorDataVo) {
		String wasId = monitorDataVo.getWasId();
		String category = monitorDataVo.getCategory();
		mr.update(wasId, category, monitorDataVo);
	}
	
	MonitorDataVo select(String wasId, String category) {
		return mr.getMonitorDataVo(wasId, category);
	}
	
	void remove(String wasId, String category) {
		mr.removeMonitorDataVo(wasId, category);
	}
	
}
