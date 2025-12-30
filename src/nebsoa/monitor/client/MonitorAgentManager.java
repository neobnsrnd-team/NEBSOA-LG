/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.monitor.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;
import nebsoa.common.property.PropertyLoader;
import nebsoa.common.util.PropertyManager;
import nebsoa.monitor.MonitorConstants;

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
 * $Log: MonitorAgentManager.java,v $
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
 * Revision 1.3  2008/06/05 10:47:41  이종원
 * 코드 정제
 *
 * Revision 1.2  2008/04/07 07:22:52  홍윤석
 * stop method 추가
 * agent 주기 setting 주석처리
 * agent category setting
 *
 * Revision 1.1  2008/03/21 01:55:15  김성균
 * 최초 등록
 *
 * </pre>
 ******************************************************************/
public class MonitorAgentManager {

	public static final String MONITOR_USE_MODE = "MONITOR_USE_MODE";
	
	public static final String KEY_USE_AGENT = ".USE_AGENT";
	
	private static final long DEFAULT_PERIOD = 0L;
	
	/** singleton 구현을 위한 자신의 instance */
	private static MonitorAgentManager instance = new MonitorAgentManager();
	
	private HashMap monitorAgentsMap;

	/**
	 * TimerManager 의 인스턴스를 리턴한다.
	 * 
	 * @return TimerManager 의 인스턴스
	 */
	public static final MonitorAgentManager getInstance() {
		return instance;
	}

	/**
	 * Singleton 구현을 위한 private constructor
	 * 
	 */
	private MonitorAgentManager() {
		monitorAgentsMap = new HashMap();
	}

	/**
	 * Timer를 초기화 합니다.
	 * 
	 */
	public synchronized void init() {
		if (!isMonitorUsing())
			return;

		registAllMonitorAgentsInConfiguration();
		start();
	}

	public boolean isMonitorUsing() {
		return PropertyManager.getBooleanProperty(MonitorConstants.MONITOR_CONFIG_FILE, MONITOR_USE_MODE, "false");
	}

	/**
	 * configuration file (monitor.properties.xml) 에 정의된 모든 Runner를 등록합니다.
	 * 
	 */
	public synchronized void registAllMonitorAgentsInConfiguration() {
		LogManager.info("\t##### Regist all agent runners in configuration file.[" + MonitorConstants.MONITOR_CONFIG_FILE + ".properties.xml]");
		Iterator iter = null;
		iter = loadAllMonitorAgent();

		for (; iter.hasNext();) {
			regist((String) iter.next());
		}
		LogManager.info("\t##### Successfully regist all agent runners in configuration file.");
	}

	/**
	 * configuration file 로부터 Runner 리스트를 로드하여 리턴합니다.
	 * 
	 * @return Iterator 형태의 로드된 Runner 리스트
	 */
	private Iterator loadAllMonitorAgent() {
		PropertyLoader prop = PropertyManager.getPropertyLoader(MonitorConstants.MONITOR_CONFIG_FILE);

		Map agentList = new HashMap();
		Iterator iter = prop.getPropertyCache().keySet().iterator();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			if (key == null)
				continue;

			try {
				String id = key.substring(0, key.lastIndexOf(MonitorConstants.KEY_MONITOR_AGENT_CLASS));
				agentList.put(id, null);
			} catch (IndexOutOfBoundsException e) {
				continue;
			}
		}
		return agentList.keySet().iterator();
	}
	
	/**
	 * 주어진 키값에 해당하는 MonitorAgent를 등록합니다.
	 * 
	 * @param monitorCategory
	 *            MonitorAgent를 식별하기 위한 키
	 */
	public synchronized void regist(String monitorCategory) {
		String monitorAgentClass = PropertyManager.getProperty(MonitorConstants.MONITOR_CONFIG_FILE,
				monitorCategory + MonitorConstants.KEY_MONITOR_AGENT_CLASS);
		MonitorAgent monitorAgent = (MonitorAgent) forName(monitorAgentClass);
		monitorAgent.setMonitorCategory(monitorCategory);
		
		//AbstractMonitorAgent에서 기간을 읽어오도록 변경 2008-04-07
		//long period = loadPeriod(monitorCategory);
		//monitorAgent.setPeriod(period);
		
		monitorAgentsMap.put(monitorCategory, monitorAgent);
		
		LogManager.info("\t##### Successfully regist agent [" + monitorCategory + "].");
	}
	
	public Object forName(String className){
        try {
            return  Class.forName(className).newInstance();
        } catch (InstantiationException e1) {
           throw new SysException("[CANN'T MAKE INSTANCE 생성자 체크 :"+className+"]");
        } catch (IllegalAccessException e1) {
           throw new SysException("[CANN'T MAKE INSTANCE 생성자 PUBLIC 인지 체크 :"+className+"]");
        }catch(ClassNotFoundException e){
           throw new SysException("[CLASS_NOT_FOUND:"+e.getMessage()+"]");
        }
    }
	
	public long loadPeriod(String monitorCategory) {
		String periodString = PropertyManager.getProperty(MonitorConstants.MONITOR_CONFIG_FILE, 
				monitorCategory + MonitorConstants.KEY_PERIOD);

		long period = DEFAULT_PERIOD;
		try {
			period = Long.parseLong(periodString);
		} catch (Throwable t) {
			LogManager.debug(t);
		}

		LogManager.info("\t##### Successfully read agent period time: " + period);
		return period;
	}
	
	public synchronized void start() {

		if (monitorAgentsMap.size() < 1) {
			return;
		}

		try {
			Set set = monitorAgentsMap.keySet();
			Iterator iter = set.iterator();
			while (iter.hasNext()) {
				String key = (String) iter.next();
				start(key);
			}
		} catch (Throwable t) {
			LogManager.debug(t);
		}
	}

	/**
	 * Timer를 start 시킵니다.
	 */
	public synchronized void start(String key) {
		MonitorAgent agent = getMonitorAgent(key);
		if (agent == null) {
			monitorAgentsMap.remove(key);
			LogManager.info("\t##### MonitorAgent did not started.[" + key + "]");
			return;
		}
		if (agent.isRunning()) {
			agent.stopRunning();
		}
		agent.startRunning();
		LogManager.info("\t##### Successfully start " + key + " agent.");
	}

	public void stop(String key) {
		MonitorAgent agent = getMonitorAgent(key);
		if (agent == null) {
			LogManager.info("\t##### MonitorAgent was not started.");
			return;
		}
		agent.stopRunning();
		LogManager.info("\t##### Successfully stop " + key + " agent.");
	}
	
	public void stop() {
		try {
			Set set = monitorAgentsMap.keySet();
			Iterator iter = set.iterator();
			while (iter.hasNext()) {
				String key = (String) iter.next();
				MonitorAgent agent = getMonitorAgent(key);
				if (agent == null) {
					LogManager.info("\t##### MonitorAgent ["+key+"]  was not started.");
					return;
				}
				agent.stopRunning();
				LogManager.info("\t##### Successfully stop " + key + " agent.");
			}
		} catch (Throwable t) {
			LogManager.debug(t);
		}
		
	}
	
	/**
	 * 키로 지정된 수행 중인 MonitorAgent를 반환합니다.
	 * 
	 * @param key  MonitorAgentr의 키 
	 * @return 키에 해당하는  MonitorAgent 객체
	 */
	public MonitorAgent getMonitorAgent(String key) {
		return (MonitorAgent) monitorAgentsMap.get(key);
	}
}
