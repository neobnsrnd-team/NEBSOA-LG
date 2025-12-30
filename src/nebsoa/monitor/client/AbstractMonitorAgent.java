/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
package nebsoa.monitor.client;

import nebsoa.common.log.LogManager;
import nebsoa.common.startup.StartupContext;
import nebsoa.common.util.PropertyManager;
import nebsoa.monitor.MonitorConstants;
import nebsoa.monitor.util.MonitorUtil;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 주기적으로 작업을 수행할 Timer 클래스
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
 * $Log: AbstractMonitorAgent.java,v $
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
 * Revision 1.4  2008/06/05 10:47:41  이종원
 * 코드 정제
 *
 * Revision 1.3  2008/04/07 07:24:05  홍윤석
 * 모니터링 항목별 사용여부 로직 추가
 * instance 별 사용여부 추가
 * 실행주기를 property 파일에서 읽어오도록 수정
 *
 * Revision 1.2  2008/03/21 08:40:35  김성균
 * error log로 수정
 *
 * Revision 1.1  2008/03/21 01:55:15  김성균
 * 최초 등록
 *
 * </pre>
 ******************************************************************/
abstract public class AbstractMonitorAgent extends Thread implements MonitorAgent {

	public static final String MONITOR_CONFIG_FILE = "monitor";
	
	boolean running = false;

	private long period;
	
	private String monitorCategory;
	
	/**
	 * MonitorAgent를 생성합니다.
	 */
	public AbstractMonitorAgent() {
		this(60*1000);
	}
	
	/**
	 * 지정한 시간을 주기로 수행하는 MonitorAgent를 생성합니다.
	 * 
	 * @param executeTerm 수행주기 (millisecond)
	 */
	public AbstractMonitorAgent(long period) {
		running = false;
		setPeriod(period);
	}

	public void run() {
		if (isRunning()) {
			return;
		}

		running = true;
		try {
			while (isRunning()) {
				//모니터링 사용여부
				if(MonitorUtil.isUseMode(monitorCategory)){
					//등록된 INSTANCE만 데이터를 전송한다.
					if(MonitorUtil.isGatheringMode(monitorCategory + MonitorConstants.KEY_INSTANCE_LIST, 
							StartupContext.getInstanceId())){
						long currentTime = System.currentTimeMillis();
						tick(currentTime);
					}
				}
				sleep(loadPeriod());
			}
		} catch (InterruptedException e) {
			LogManager.error(e.toString(), e);
		} catch (Throwable t) {
			LogManager.error(t.toString(), t);
		}
	}
	
	public void setMonitorCategory(String monitorCategory){
		this.monitorCategory = monitorCategory;
	}
	
	public long loadPeriod() {
		String periodString = PropertyManager.getProperty(MONITOR_CONFIG_FILE, 
				this.monitorCategory + MonitorConstants.KEY_PERIOD);

		long period = this.period;
		try {
			period = Long.parseLong(periodString);
			setPeriod(period);
		} catch (Throwable t) {
			LogManager.debug(t);
		}

		return period;
	}
	
	public void setPeriod(long period) {
		if(period<1000) period = period*1000;
		this.period = period;
	}

	/**
	 * 현재 Thread가 수행 중인지를 반환
	 * @return 수행 중이면 <code>true</code>, 수행 중이 아니면 <code>false</code>
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * Thread의 수행을 정지시킵니다.
	 */
	public void stopRunning() {
		running = false;
	}
	
	/**
	 * Thread의 수행을 시작시킵니다.
	 */
	public void startRunning() {
		start();
	}
}
