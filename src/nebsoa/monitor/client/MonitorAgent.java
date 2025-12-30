/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
package nebsoa.monitor.client;


/*******************************************************************
 * <pre>
 * 1.설명 
 * Timer에 등록되어 주기적으로 수행될 클래스의 인터페이스
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
 * Revision 1.1  2016/04/15 02:22:38  cvs
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
 * Revision 1.1  2008/08/04 08:54:51  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.2  2008/04/07 07:19:39  홍윤석
 * setMonitorCategory method 추가
 *
 * Revision 1.1  2008/03/21 01:55:15  김성균
 * 최초 등록
 *
 * </pre>
 ******************************************************************/
public interface MonitorAgent {
	/**
	 * 타이머로부터 지정된 시간에 호출되는 method
	 * 
	 * @param time 현재 시각 (millisecond)
	 */
	public void tick(long time);
	
	/**
	 * monitorCategory 설정한다.
	 * @param monitorCategory
	 */
	public void setMonitorCategory(String monitorCategory);
	
	public void setPeriod(long period);
	
	/**
	 * 현재 Thread가 수행 중인지를 반환
	 * @return 수행 중이면 <code>true</code>, 수행 중이 아니면 <code>false</code>
	 */
	public boolean isRunning();
	
	/**
	 * Thread의 수행을 정지시킵니다.
	 */
	public void stopRunning();
	
	/**
	 * Thread의 수행을 시작시킵니다.
	 */
	public void startRunning();
}
