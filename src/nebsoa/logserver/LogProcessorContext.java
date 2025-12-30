/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
package nebsoa.logserver;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

import nebsoa.common.util.PropertyManager;
/*******************************************************************
 * <pre>
 * 1.설명 
 * Message Queue, LogStore, LogReceiver 의 설정 정보를 담고 있는 클래스. 
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
 * $Log: LogProcessorContext.java,v $
 * Revision 1.1  2018/01/15 03:39:51  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:59  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:24  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:28  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:52  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:27  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.2  2007/12/17 01:48:35  김승희
 * 로그 서버 서버 분리에 따른 수정
 *
 * Revision 1.10  2007/12/06 04:52:01  shkim
 * remoteLogSenderWasId 추가
 *
 * Revision 1.9  2007/10/02 07:56:29  김승희
 * *** empty log message ***
 *
 * Revision 1.8  2007/09/21 08:22:38  김승희
 * *** empty log message ***
 *
 * Revision 1.7  2007/09/03 15:33:18  shkim
 * *** empty log message ***
 *
 * Revision 1.6  2007/09/02 13:17:36  shkim
 * *** empty log message ***
 *
 * Revision 1.5  2007/08/27 10:39:19  김승희
 * *** empty log message ***
 *
 * Revision 1.4  2007/08/22 06:32:50  김승희
 * *** empty log message ***
 *
 * Revision 1.3  2007/08/17 11:09:00  김승희
 * *** empty log message ***
 *
 * Revision 1.2  2007/08/16 07:09:45  김승희
 * 성능 향상을 위한 수정
 *
 * Revision 1.1  2007/06/15 02:05:07  김승희
 * 프로젝트 변경 신규 커밋
 *
 * Revision 1.1  2007/06/13 05:29:29  shkim
 * 패키지 변경
 *
 * Revision 1.4  2007/06/12 06:07:53  shkim
 * 프로퍼티 변경
 *
 * Revision 1.3  2007/06/12 05:25:51  shkim
 * *** empty log message ***
 *
 * Revision 1.2  2007/06/12 02:30:13  shkim
 * 프로퍼티 반영
 *
 * Revision 1.1  2007/06/11 08:24:38  shkim
 * 최초 등록
 *
 *
 * </pre>
 ******************************************************************/

public class LogProcessorContext implements Serializable {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 25142578012567267L;
	
	/**
	 * 로그 저장 방식-DB
	 */
	public static final String RECEIVE_WORKER_TYPE_DB = "DB";
	
	/**
	 * 로그 저장 방식-파일 INDEX
	 */
	public static final String RECEIVE_WORKER_TYPE_INDEX = "INDEX";
	
	
	/**
	 * MQBroker의 Connector URI Set
	 */
	public static Set mqBrokerConnectorUriList = new TreeSet();
	
	/**
	 * 로그 큐 이름
	 */
	public static String logQueueName = PropertyManager.getProperty("logserver", "LOG_QUEUE_NAME", "LOG_QUEUE");
	
	/**
	 * 로그 큐 세션을 점검하고 LogStore에 쌓인 로그 데이터를 flush 해주는 쓰레드의 작동 interval 시간(단위 : millisecond)
	 */
	public static long logSenderConnectionCheckInterval = PropertyManager.getIntProperty("logserver", "LOG_SENDER_CONNECTION_CHECK_INTERVAL", 20)*1000;
	
	/**
	 * 로그 큐로 로그를 마지막으로 전송한 후 이 시간이 지났으면  LogStore에 쌓인 로그 데이터를 강제로 flush한다. (단위 : millisecond)
	 */
	public static long logStoreFlushInterval = PropertyManager.getIntProperty("logserver", "LOG_STORE_FLUSH_INTERVAL", 30)*1000;
	
	/**
	 * LogSender가 MQ로 커넥션을 맺을 때 사용하는 URI
	 */
	public static String logSenderConecctionUri = PropertyManager.getProperty("logserver", "LOG_SENDER_CONNECTION_URI", "vm://localhost?jms.useAsyncSend=true&jms.persistent=false&jms.useJmx=false");
	
	/**
	 * LogReceiver가 MQ로 커넥션을 맺을 때 사용하는 URI
	 */
	public static String logReceiverConecctionUri = PropertyManager.getProperty("logserver", "LOG_RECEIVER_CONNECTION_URI", "vm://localhost?jms.useAsyncSend=true&jms.persistent=false&jms.useJmx=false");
	
	/**
	 * 로그 큐로 한번에 전송하는 로그의 MAX 갯수
	 */
	public static int logStoreMaxSize = PropertyManager.getIntProperty("logserver", "LOG_STORE_MAX_SIZE", 1000);
	
	/**
	 * LogProcessor 사용여부
	 */
	public static boolean islogProcessorUse = "Y".equals(PropertyManager.getProperty("logserver", "LOG_PROCESSOR_USE", "N"))?true:false;
	
	/**
	 * log server 내부에서 남기는 로그의 카테고리 
	 */
	public static String logCategory = PropertyManager.getProperty("logserver", "LOG_CATEGORY", "LOG_SVR");
	
	/**
	 * blocking queue의 사이즈 
	 */
	public static int blockingQueueSize = PropertyManager.getIntProperty("logserver", "BLOCKING_QUEUE_SIZE", 500);
	
	/**
	 * log send worker 쓰레드 갯수 (=blocking queue 갯수) 
	 */
	public static int logSendWorkerCount = PropertyManager.getIntProperty("logserver", "LOG_SEND_WORKER_COUNT", 5);
	
	/**
	 * log receiver worker instance 갯수 
	 */
	public static int logReceiveWorkerCount = PropertyManager.getIntProperty("logserver", "LOG_RECEIVE_WORKER_COUNT", 10);

	/**
	 * MQ memory 사용량에 따라 Lostore를 중지 또는 재시작 시킬 때 사용하는 기준 값. 
	 */
	public static int mqMemoryThreshold = PropertyManager.getIntProperty("logserver", "MQ_MEMORY_THRESHOLD", 80);
	
	/**
	 * MQ memory Limit(단위:byte)
	 */
	public static long mqMemoryLimit = PropertyManager.getLongProperty("logserver", "MQ_MEMORY_LIMIT", (50*1024*1024));
	
	/**
	 * Memory에 쌓인 index가 file로 마지막으로 옮겨진 후 이 시간이 경과되면 IndexBuilderManager 쓰레드에 의해 강제로 file로 flush된다.(단위 : millisecond)   
	 */
	public static long indexBuilderManagerFlushTimeThreshold = PropertyManager.getLongProperty("logserver", "INDEX_FLUSH_TIME_THRESHOLD", (2*60*1000));
	
	/**
	 * log receiver worker 타입 (DB 또는 INDEX)
	 */
	public static String receiveWorkerType = PropertyManager.getProperty("logserver", "RECEIVE_WORKER_TYPE", "INDEX"); 

	/**
	 * log receiver worker 타입이 DB인 경우 DBLogger 클래스명
	 */
	public static String dbLogger = PropertyManager.getProperty("logserver", "DB_LOGGER", "nebsoa.logserver.DefaultDBLogger"); 

	
	/**
	 * DB Logger에서 사용하는 DB name(db.properties.xml의 DB name)을 리턴한다.
	 * @return
	 */
	public static String getDBName(){
		return PropertyManager.getProperty("logserver", "DB_NAME", "LOG_DB");
	}
	
	/**
	 * 로그 저장 방식이 DB인 경우 DB에 insert하는 시간이 이 시간을 초과하면 info 레벨의 로그를 남긴다. (단위 : millisecond)
	 */
	public static long dbInsertThreshold = PropertyManager.getLongProperty("logserver", "DB_INSERT_THRSHOLD", 5*1000); 
	
	/**
	 * Remote Log Sender ejb가 디플로이된 was id
	 */
	public static String remoteLogSenderWasId = PropertyManager.getProperty("was_config", "LOG_SERVER_WAS_CONFIG", "CA00");
	
	
	static{
		mqBrokerConnectorUriList.add(logSenderConecctionUri);
		mqBrokerConnectorUriList.add(logReceiverConecctionUri);
	}
	
}
