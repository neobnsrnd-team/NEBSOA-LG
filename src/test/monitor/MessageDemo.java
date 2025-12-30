/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package test.monitor;

import java.util.Random;

import nebsoa.common.Context;
import nebsoa.common.log.LogManager;
import nebsoa.common.monitor.WorkingThreadMonitorManager;
import nebsoa.common.monitor.message.MessageMonitor;
import nebsoa.monitor.MonitorConstants;

/*******************************************************************
 * <pre>
 * 1.설명 
 * TODO MonitorAgent 클래스에 대한 주석 넣기
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
 * $Log: MessageDemo.java,v $
 * Revision 1.1  2018/01/15 03:39:54  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:37  cvs
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
 * Revision 1.1  2008/08/04 08:54:54  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.2  2008/03/21 09:04:27  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/03/21 01:59:38  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class MessageDemo extends Thread {
	
	boolean running = true;

	private long period = 1L;
	
	public void run() {
		long count = 0L;
		try {
			while (running) {
				count++;
				long start = System.currentTimeMillis();
				messageOrgDataGeneration();
				messageTrxDataGeneration();
				messageBankDataGeneration();
				threadMonitorDataGeneration();
				long exeTime = System.currentTimeMillis() - start;
				if (exeTime > 0) {
					LogManager.debug("\t### 수행시간 : " + exeTime + "[" + count +"]");
				}
				sleep(period);
			}
		} catch (InterruptedException e) {
			LogManager.debug(e);
		} catch (Throwable t) {
			LogManager.debug(t);
		}
	}
	
	/**
	 * 기관별 데이터 생성
	 */
	public void messageOrgDataGeneration() {
		Random random = new Random(System.currentTimeMillis());
		long value = random.nextLong();
		value = Math.abs(value % 1000);

		int statusValue = random.nextInt();

		int status = (statusValue % 10 != 0)? MessageMonitor.STATUS_SUCCESS : MessageMonitor.STATUS_FAIL;
		String info = "HOST";
		switch((int) (value % 10)) {
		case 0:
			info = "HOST";
			break;
		case 1:
			info = "TANDEM";
			break;
		case 2:
			info = "IVOSTRO";
			break;
		case 3:
			info = "FEX";
			break;
		case 4:
			info = "KIBS";
			break;
		case 5:
			info = "SMS";
			break;
		case 6:
			info = "OTP";
			break;
		case 7:
			info = "CRM";
			break;
		case 8:
			info = "EUC";
			break;
		case 9:
			info = "OTP2";
			break;
		}
		try {
			MessageMonitor.getInstance().addData(MonitorConstants.MESSAGE_ORG, info, System.currentTimeMillis(), value, status);
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 주요거래 데이터 생성
	 */
	public void messageTrxDataGeneration() {
		Random random = new Random(System.currentTimeMillis());
		long value = random.nextLong();
		value = Math.abs(value % 1000);

		int statusValue = random.nextInt();

		int status = (statusValue % 10 != 0)? MessageMonitor.STATUS_SUCCESS : MessageMonitor.STATUS_FAIL;
		String info = "HOST";
		switch((int) (value % 5)) {
		case 0:
			info = "5160GGE510";
			break;
		case 1:
			info = "5060GGE500";
			break;
		case 2:
			info = "5201KN5201";
			break;
		case 3:
			info = "9820GGN980";
			break;
		case 4:
			info = "7650GGN760";
			break;
		}
		try {
			MessageMonitor.getInstance().addData(MonitorConstants.MESSAGE_MAIN_TRX, info, System.currentTimeMillis(), value, status);
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 타행수취 은행별 데이터 생성
	 */
	public void messageBankDataGeneration() {
		Random random = new Random(System.currentTimeMillis());
		long value = random.nextLong();
		value = Math.abs(value % 1000);

		int statusValue = random.nextInt();

		int status = (statusValue % 10 != 0)? MessageMonitor.STATUS_SUCCESS : MessageMonitor.STATUS_FAIL;
		String info = "HOST";
		switch((int) (value % 10)) {
		case 0:
			info = "01";
			break;
		case 1:
			info = "02";
			break;
		case 2:
			info = "03";
			break;
		case 3:
			info = "04";
			break;
		case 4:
			info = "05";
			break;
		case 5:
			info = "06";
			break;
		case 6:
			info = "07";
			break;
		case 7:
			info = "08";
			break;
		case 8:
			info = "09";
			break;
		case 9:
			info = "10";
			break;
		}
		try {
			MessageMonitor.getInstance().addData(MonitorConstants.MESSAGE_BANK, info, System.currentTimeMillis(), value, status);
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 쓰레드 모니터 데이터 생성
	 */
	public void threadMonitorDataGeneration() {
		Random random = new Random(System.currentTimeMillis());
		boolean inc = (random.nextInt() % 2 == 0);
		if (inc) {
			WorkingThreadMonitorManager.getInstance().addContext(new Context());
		}
	}
}
