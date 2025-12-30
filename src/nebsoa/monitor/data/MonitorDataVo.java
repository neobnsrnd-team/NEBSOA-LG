/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
package nebsoa.monitor.data;

import java.nio.ByteBuffer;
import java.util.Hashtable;

import nebsoa.common.log.LogManager;
import nebsoa.common.startup.StartupContext;
import nebsoa.monitor.MonitorConstants;

/*******************************************************************
 * <pre>
 * 1.설명 
 * UDP기반으로 Agent가 모니터링 데이터를 전송할 때 데이터를 저장하는 class. 
 * Hashtable 기반으로 되어 있으므로 무조건 저장한 후에 getBytes만 호출하면 전송
 * 형태의 데이터가 만들어 진다.
 * 2.사용법
 * HashMap과 동일.
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author 이종원
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 
 * $Log: MonitorDataVo.java,v $
 * Revision 1.1  2018/01/15 03:39:54  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:39  cvs
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
 * Revision 1.3  2008/03/18 01:09:24  김성균
 * 모니터링 기본 저장항목 정리
 *
 * Revision 1.2  2008/03/04 01:58:27  이종원
 * WAS_ID를 파라미터로 받는 생성자 추가
 *
 * Revision 1.1  2008/01/22 05:58:35  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:39:14  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2006/09/11 14:12:18  이종원
 * 최초작성
 *
 * </pre>
 ******************************************************************/
public class MonitorDataVo extends Hashtable {
 
	private static final long serialVersionUID = 524645536078717148L;
	
	public MonitorDataVo(String category) {
		this(StartupContext.getInstanceId(), category, null);
	}
	
	public MonitorDataVo(String category, Object obj) {
		this(StartupContext.getInstanceId(), category, obj);
	}
	
	public MonitorDataVo(String wasId, String category, Object obj) {
		put(MonitorConstants.WAS_ID, wasId);
		put(MonitorConstants.MONITOR_CATEGORY, category);
		put(MonitorConstants.MONITOR_OBJECT, obj);
		put(MonitorConstants.TIME, System.currentTimeMillis() + "");
	}
	
	public ByteBuffer getBytes() {
		byte[] data = super.toString().getBytes();
		LogManager.debug("MonitorData:" + new String(data));
		ByteBuffer buffer = ByteBuffer.allocate(data.length);
		buffer.put(data);
		return buffer;
	}
	
	public String getWasId() {
		return getString(MonitorConstants.WAS_ID);
	}
	
	public String getCategory() {
		return getString(MonitorConstants.MONITOR_CATEGORY);
	}
	
	public Object getMonitorObject() {
		return get(MonitorConstants.MONITOR_OBJECT);
	}
	
	public String getString(String key) {
		return (String) get(key);
	}
}
