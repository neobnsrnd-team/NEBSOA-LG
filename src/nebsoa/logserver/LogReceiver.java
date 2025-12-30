/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
package nebsoa.logserver;

import java.util.ArrayList;

import javax.jms.Connection;
import javax.jms.JMSException;

import org.apache.activemq.ActiveMQConnectionFactory;

import nebsoa.common.log.LogManager;
import nebsoa.logserver.dblog.DBLogWorker;
import nebsoa.logserver.index.IndexLogWorker;
/*******************************************************************
 * <pre>
 * 1.설명 
 * Message Queue로부터 로그 데이터를 비동기로 수신하는 클래스. 
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
 * $Log: LogReceiver.java,v $
 * Revision 1.1  2018/01/15 03:39:51  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:58  cvs
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
 * Revision 1.11  2007/09/21 08:22:38  김승희
 * *** empty log message ***
 *
 * Revision 1.10  2007/08/31 05:29:33  김승희
 * *** empty log message ***
 *
 * Revision 1.9  2007/08/28 10:02:33  김승희
 * *** empty log message ***
 *
 * Revision 1.8  2007/08/22 06:33:41  김승희
 * *** empty log message ***
 *
 * Revision 1.7  2007/08/17 11:09:00  김승희
 * *** empty log message ***
 *
 * Revision 1.6  2007/08/16 07:09:45  김승희
 * 성능 향상을 위한 수정
 *
 * Revision 1.5  2007/07/24 07:14:21  김승희
 * *** empty log message ***
 *
 * Revision 1.4  2007/07/17 11:59:08  김승희
 * *** empty log message ***
 *
 * Revision 1.3  2007/07/16 12:34:06  김승희
 * stop() 메소드 추가
 *
 * Revision 1.2  2007/07/10 05:28:39  김승희
 * *** empty log message ***
 *
 * Revision 1.1  2007/06/15 02:05:07  김승희
 * 프로젝트 변경 신규 커밋
 *
 * Revision 1.1  2007/06/13 05:29:29  shkim
 * 패키지 변경
 *
 * Revision 1.3  2007/06/12 02:30:13  shkim
 * 프로퍼티 반영
 *
 * Revision 1.2  2007/06/12 02:24:53  shkim
 * *** empty log message ***
 *
 * Revision 1.1  2007/06/11 08:24:38  shkim
 * 최초 등록
 *
 *
 * </pre>
 ******************************************************************/

public class LogReceiver{
	
	private ActiveMQConnectionFactory connectionFactory;
	
	private static LogReceiver instance;
	private static Object dummy=new Object();
	
	private ArrayList logReceiveWorkerList = new ArrayList();
	
	public static LogReceiver getInstance(){
	     if(instance==null){
	          synchronized (dummy) {
	              if(instance==null) instance = new LogReceiver();
	          }
	     }
	     return instance;
	}
	
	LogReceiver(){}
	
	/**
	 * Log Receiver를 시작시킨다.
	 * @throws JMSException
	 */
	public void start() throws Exception{

		try{
			createConnectionFactory();
			
			createLogReceiveWorker();
		
			LogManager.info(LogProcessorContext.logCategory, "LogReceiver started..");
        
		}catch(Exception e){
			LogManager.error(LogProcessorContext.logCategory, "LogReceiver start failed..");
			throw e;
		}
	}

	/**
	 * ConnectionFactory를 생성한다.
	 */
	private void createConnectionFactory() {
		connectionFactory = new ActiveMQConnectionFactory(LogProcessorContext.logReceiverConecctionUri);
		//connectionFactory.setDispatchAsync(true);
		//connectionFactory.setObjectMessageSerializationDefered(true);
		//connectionFactory.setCopyMessageOnSend(false);
		//connectionFactory.setAlwaysSessionAsync(true);
		
		connectionFactory.getPrefetchPolicy().setQueuePrefetch(2000);
	}
	
	/**
	 * LogReceiveWorker를 생성한다.
	 * @throws JMSException
	 */
	private void createLogReceiveWorker() throws Exception{
		LogReceiveWorker logReceiveWorker = null;
		for(int i=0; i<LogProcessorContext.logReceiveWorkerCount; i++){
			
			if(LogProcessorContext.receiveWorkerType.equalsIgnoreCase(LogProcessorContext.RECEIVE_WORKER_TYPE_DB)){
				logReceiveWorker = new DBLogWorker(this, i);
			}else{
				logReceiveWorker = new IndexLogWorker(this, i);
			}
			
			this.logReceiveWorkerList.add(logReceiveWorker);
			logReceiveWorker.init();
		}
	}
	
	/**
	 * Connection을 생성한다.
	 * @return Connection
	 * @throws JMSException
	 */
	public Connection createConnection() throws JMSException{
		return connectionFactory.createConnection();
	}
	
	/**
	 * LogReceiver를 종료한다.
	 */
	public synchronized void stop(){
		
		LogManager.info(LogProcessorContext.logCategory, "LogReceiver is now stopping..");
		
		waitForBorkerEmpty();
		
		LogManager.info(LogProcessorContext.logCategory, "MQBroker is empty..");
		
		stopLogReceiveWorker();
				
		LogManager.info(LogProcessorContext.logCategory, "LogReceiver stopped.");
	}

	private void stopLogReceiveWorker() {
		LogReceiveWorker logReceiveWorker = null;
		for(int i=0; i<logReceiveWorkerList.size(); i++){
			logReceiveWorker = (LogReceiveWorker)logReceiveWorkerList.get(i);
			logReceiveWorker.destroy();
			LogManager.info(LogProcessorContext.logCategory, "LogReceiveWorker[" + i+ "]를 종료하였습니다.");
		}
		logReceiveWorkerList.clear();
	}

	private void waitForBorkerEmpty() {
		try {
			while(MQBroker.getInstance().getUsage()>0){
				LogManager.info(LogProcessorContext.logCategory, "MQBroker usage is " +MQBroker.getInstance().getUsage() +"bytes");
				Thread.sleep(2000);
			}
		} catch (Exception e) {
			LogManager.error(LogProcessorContext.logCategory, e);
		}
	}
}
