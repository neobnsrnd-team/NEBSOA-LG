/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.monitor.dashboard.data.memory;

import java.io.Serializable;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 메모리 모니터링 데이터 
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
 * $Log: MemoryInfo.java,v $
 * Revision 1.1  2018/01/15 03:39:52  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:09  cvs
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
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.4  2008/03/20 08:51:06  김성균
 * time 설정안하는 버그 수정
 *
 * Revision 1.3  2008/03/20 08:15:25  김성균
 * 메모리 모니터링 시간 필드 추가
 *
 * Revision 1.2  2008/03/19 08:08:00  김성균
 * 동기화 추가
 *
 * Revision 1.1  2008/03/19 06:47:51  김성균
 * 최초 등록
 *
 * </pre>
 ******************************************************************/
public class MemoryInfo implements Serializable {
	
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -6636084715494304069L;
	
	private long free; 
	private long total;
	private long time;
	
	public MemoryInfo(long free, long total, long time) {
		this.free = free;
		this.total = total;
		this.time = time;
	}
	
	/**
	 * 남아있는 메모리
	 * @return Returns the free.(단위 : KB)
	 */
	public long getFree() {
		return free / 1024;
	}
	
	/**
	 * 전체 가상머신 메모리
	 * @return Returns the total.(단위 : KB)
	 */
	public long getTotal() {
		return total / 1024;
	}
	
	/**
	 * 사용중인 메모리
	 * @return Returns the use.(단위 : KB)
	 */
	public long getUse() {
		return (total - free) / 1024;
	}
	
	/**
	 * @return Returns the time.
	 */
	public long getTime() {
		return time;
	}

	/**
	 * @return Returns the usedRatio.
	 */
	public long getUsedRatio() {
		return (total - free) * 100 / total;
	}
	
	/**
	 * @return Returns the unusedRatio.
	 */
	public long getUnusedRatio() {
		return free * 100 / total;
	}
}
