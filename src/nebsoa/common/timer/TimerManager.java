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
import java.util.Map;

import nebsoa.common.exception.PropertyException;
import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;
import nebsoa.common.property.PropertyLoader;
import nebsoa.common.util.PropertyManager;

/*******************************************************************
 * <pre>
 * 1.설명 
 * Timer를 관리하는 클래스
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
 * $Log: TimerManager.java,v $
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
 * Revision 1.5  2007/07/30 08:34:41  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class TimerManager {

	public static final String TIMER_CONFIG_FILE = "timer";

	public static final String KEY_USE_TIMER = "USE_TIMER";

	public static final String KEY_REPEAT_TIME = "REPEAT_TIME";

	public static final String TIMER_CLASS_SUFFIX = "_TIMER_CLASS";

	private static final long DEFAULT_REPEAT_TIME = 1000L;
    
	/** singleton 구현을 위한 자신의 instance */
	private static TimerManager instance = new TimerManager();

	/**
	 * TimerManager 의 인스턴스를 리턴한다.
	 * 
	 * @return TimerManager 의 인스턴스
	 */
	public static final TimerManager getInstance() {
		return instance;
	}

	private Timer timer;

	/**
	 * Singleton 구현을 위한 private constructor
	 * 
	 */
	private TimerManager() {
	}

	/**
	 * Timer를 초기화 합니다.
	 * 
	 */
	public synchronized void init() {
		if (!isTimerUsing())
			return;

		if (timer != null && timer.isRunning()) {
			timer.stopRunning();
			timer = null;
		}
		long timerRepeatTime = loadRepeatTime();

		timer = new Timer(timerRepeatTime);

		registAllRunnersInConfiguration();
		start();
	}

	public boolean isTimerUsing() {
		return PropertyManager.getBooleanProperty(TIMER_CONFIG_FILE, KEY_USE_TIMER, "false");
	}

	public long loadRepeatTime() {
		String repeatTimeString = PropertyManager.getProperty(
				TIMER_CONFIG_FILE, KEY_REPEAT_TIME);

		long repeatTime = DEFAULT_REPEAT_TIME;
		try {
			repeatTime = Long.parseLong(repeatTimeString);
		} catch (Throwable t) {
			LogManager.debug(t);
		}

		LogManager.info("\t##### Successfully read timer repeat time: " + repeatTime);
		return repeatTime;
	}

	/**
	 * 주어진 키값에 해당하는 Runner를 등록합니다.
	 * 
	 * @param propertyKey
	 *            Runner를 식별하기 위한 키
	 */
	public synchronized void regist(String propertyKey) {
		try {
			String timerClass = PropertyManager.getProperty(TIMER_CONFIG_FILE,
					propertyKey + TIMER_CLASS_SUFFIX);
			timer.addRunner(propertyKey, timerClass);
		} catch (PropertyException e) {
			throw new SysException(e.getMessage());
		} catch (ClassNotFoundException e) {
			throw new SysException(e.getMessage());
		} catch (InstantiationException e) {
			throw new SysException(e.getMessage());
		} catch (IllegalAccessException e) {
			throw new SysException(e.getMessage());
		}// end try catch

		LogManager.info("\t##### Successfully regist timer runner[" + propertyKey + "].");
	}// end of regist()

	/**
	 * configuration file (monitor.properties.xml) 에 정의된 모든 Runner를 등록합니다.
	 * 
	 */
	public synchronized void registAllRunnersInConfiguration() {
		LogManager.info("\t##### Regist all timer runners in configuration file.");
		Iterator iter = null;
		try {
			iter = loadAllRunners();
		} catch (PropertyException e) {
			throw new SysException(e.getMessage());
		}// end try catch

		for (; iter.hasNext();) {
			regist((String) iter.next());
		}// end for
		LogManager.info("\t##### Successfully regist all timer runners in configuration file.");
	}

	/**
	 * configuration file 로부터 Runner 리스트를 로드하여 리턴합니다.
	 * 
	 * @return Iterator 형태의 로드된 Runner 리스트
	 */
	private Iterator loadAllRunners() {
		PropertyLoader prop = PropertyManager.getPropertyLoader(TIMER_CONFIG_FILE);

		Map scheduleList = new HashMap();
		Iterator iter = prop.getPropertyCache().keySet().iterator();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			if (key == null)
				continue;

			try {
				String id = key.substring(0, key.lastIndexOf(TIMER_CLASS_SUFFIX));
				scheduleList.put(id, null);
			} catch (IndexOutOfBoundsException e) {
				continue;
			}
		}
		return scheduleList.keySet().iterator();
	}

	/**
	 * Timer를 start 시킵니다.
	 */
	public synchronized void start() {
		if (timer == null) {
			LogManager.info("\t##### Timer did not started.");
			return;
		}
		timer.start();
		LogManager.info("\t##### Successfully start timer.");
	}

	public void stop() {
		if (timer == null) {
			LogManager.info("\t##### Timer was not started.");
			return;
		}
		timer.stopRunning();
		timer = null;
		LogManager.info("\t##### Successfully stop timer.");
	}
}
