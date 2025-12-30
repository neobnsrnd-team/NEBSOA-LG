/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
package nebsoa.common.monitor.message;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 전문 처리 한건에 대한 모니터링 데이터
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
 * $Log: MessageMonitorData.java,v $
 * Revision 1.1  2018/01/15 03:39:52  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:11  cvs
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
 * Revision 1.2  2008/03/19 06:00:03  김성균
 * 통합 모니터링 관련 수정
 *
 * Revision 1.1  2008/01/22 05:58:21  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:39:11  안경아
 * *** empty log message ***
 *
 * Revision 1.3  2007/08/02 01:46:31  김성균
 * 저장 시간 관련 로직은 캐쉬에서 해결하므로 삭제한다.
 *
 * Revision 1.2  2007/07/30 08:21:57  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class MessageMonitorData {

	private String info;
	
	private long time;

	private long exeTime;

	private int status;
    
	public MessageMonitorData(String info, long time, long exeTime, int status) {
		this.info = info;
		this.time = time;
		this.exeTime = exeTime;
		this.status = status;
	}
	
	/**
	 * @return Returns the orgId.
	 */
	public String getOrgId() {
		return info;
	}

	/**
	 * @return Returns the time.
	 */
	public long getTime() {
		return time;
	}

	/**
	 * @return Returns the exeTime.
	 */
	public long getExeTime() {
		return exeTime;
	}

	/**
	 * @return Returns the status.
	 */
	public int getStatus() {
		return status;
	}

	public boolean isFailed() {
		return (status == MessageMonitor.STATUS_FAIL);
	}
}
