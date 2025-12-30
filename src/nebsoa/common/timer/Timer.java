/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
package nebsoa.common.timer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import nebsoa.common.log.LogManager;

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
 * $Log: Timer.java,v $
 * Revision 1.1  2018/01/15 03:39:49  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:28  cvs
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
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:34  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:39:15  안경아
 * *** empty log message ***
 *
 * Revision 1.5  2007/07/30 09:04:34  김성균
 * *** empty log message ***
 *
 * Revision 1.4  2007/07/30 08:34:41  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class Timer extends Thread {

	private boolean running = false;

	private HashMap runnersMap = null;

	private long timerRepeatTime;

	/**
	 * 지정한 시간을 주기로 Runner를 실행시키는 Timer를 생성합니다.
	 * 
	 * @param timerRepeatTime 주기 (millisecond)
	 */
	Timer(long timerRepeatTime) {
		running = false;
		this.timerRepeatTime = timerRepeatTime;
		runnersMap = new HashMap();
	}

	public void run() {

		if (running)
			return;

		if (runnersMap.size() < 1) {
			return;
		}

		running = true;
		try {
			Set set = runnersMap.entrySet();
			while (running) {
				long currentTime = System.currentTimeMillis();
				Iterator iter = set.iterator();
				while (iter.hasNext()) {
					Entry entry = (Entry) iter.next();
					Object key = entry.getKey();
					TimedRunner runner = (TimedRunner) entry.getValue();
					if (runner == null) {
						runnersMap.remove(key);
						continue;
					}
					runner.tick(currentTime);
				}
				sleep(timerRepeatTime);
			}
		} catch (InterruptedException e) {
			LogManager.debug(e);
		} catch (Throwable t) {
			LogManager.debug(t);
		}
	}

	/**
	 * 현재 Thread가 수행 중인지를 반환
	 * @return 수행 중이면 <code>true</code>, 수행 중이 아니면 <code>false</code>
	 */
	boolean isRunning() {
		return running;
	}

	/**
	 * Thread의 수행을 정지시킵니다.
	 */
	void stopRunning() {
		running = false;
	}

	/**
	 * 키로 지정된 수행 중인 TimedRunner를 반환합니다.
	 * 
	 * @param key Runner의 키 
	 * @return 키에 해당하는 TimedRunner 객체
	 */
	TimedRunner getRunner(String key) {
		return (TimedRunner) runnersMap.get(key);
	}

	/**
	 * Runner를 추가합니다.
	 * 
	 * @param key Runner의 키
	 * @param runnerClassString Runner class 이름
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	void addRunner(String key, String runnerClassString)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		Class runnerClass = Class.forName(runnerClassString);
		TimedRunner runner = (TimedRunner) runnerClass.newInstance();
		runnersMap.put(key, runner);
	}
}
