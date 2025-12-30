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
import java.util.ArrayList;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import nebsoa.common.log.LogManager;
import nebsoa.common.util.PropertyManager;
/*******************************************************************
 * <pre>
 * 1.설명 
 * LogBlockingQueue에서 실시간으로 쌓이는 로그를 async로 수집하여 MQ로 전송하는 쓰레드
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
 * $Log: LogSendWorker.java,v $
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
 * Revision 1.9  2007/10/31 07:00:54  jwlee
 * *** empty log message ***
 *
 * Revision 1.8  2007/10/25 04:12:41  jwlee
 * blockingQueuePollingPeriod 속성 추가
 *
 * Revision 1.7  2007/10/22 13:27:59  jwlee
 * LOCK을 잡기 위한 경합을 줄이는 로직 추가
 *
 * Revision 1.6  2007/09/06 01:27:11  김승희
 * *** empty log message ***
 *
 * Revision 1.5  2007/09/03 11:30:42  김승희
 * *** empty log message ***
 *
 * Revision 1.4  2007/08/31 05:29:33  김승희
 * *** empty log message ***
 *
 * Revision 1.3  2007/08/28 10:02:33  김승희
 * *** empty log message ***
 *
 * Revision 1.2  2007/08/17 11:09:00  김승희
 * *** empty log message ***
 *
 * Revision 1.1  2007/08/16 07:10:04  김승희
 * 최초 등록
 *
 *
 * </pre>
 ******************************************************************/
public class LogSendWorker implements ExceptionListener, Runnable{
	
	private Object lock = new Object();
	private LogSender logSender;
	
	private Connection connection;
	private Session session;
	private Destination destination;
	private MessageProducer producer;
	
	private ConnectionManager connectionManager;

	private ArrayList logList = new ArrayList(1000);
	
	private boolean isStop;
	private boolean isError;
	private boolean isRunning;
	
	private long lastSendTime = System.currentTimeMillis();
	
	/**
	 * 이 쓰레드가 담당하고 있는 queue의 index (로그가 실시간으로 쌓이는 BlockingQueue의 갯수만큼 쓰레드가 생성되며 하나씩 담당한다.)
	 */
	private int logQueueIndex = -1;
	
	public LogSendWorker(LogSender logSender, int logQueueIndex) throws JMSException{
		this.logSender = logSender;
		this.logQueueIndex = logQueueIndex;
		
		try {
			init();
		} catch (JMSException e) {
			setError(true);
			LogManager.error(LogProcessorContext.logCategory, "LogSendWorker initialization failed.", e);
		}
		
		//ConnectionManager 시작
		startConnectionManager();
	}
	
	protected void init() throws JMSException{

		connection = logSender.createConnection();
		connection.setExceptionListener(this);
		connection.start();
	
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		destination = session.createQueue(LogProcessorContext.logQueueName);
		producer = session.createProducer(destination);
		producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		
		setError(false);
		LogManager.info(LogProcessorContext.logCategory, "LogSendWorker[" + logQueueIndex +"] initialized..");
	}
	
	int timeout = 3*1000;
	
	public void run() {
		Object log = null;
		String jsmMessageId = null;
		LogBlockingQueue queue=null;
		
		while(!isStop){
			int blockingQueuePollingPeriod=
				PropertyManager.getIntProperty("logserver","BLOCKING_QUEUE_POLLING_PERIOD",5);
			isRunning = true;
			jsmMessageId = null;log = null;
			try{
				
				if(!isError){
										
					long startTime = System.currentTimeMillis();
					
					queue = LogStore.getInstance().getQueue(logQueueIndex);
					if(queue != null){
						queue.copyToArray(logList, timeout);
					}
					
					//log = LogStore.getInstance().get(logQueueIndex, timeout);
					//log = LogStore2.getInstance().get(logQueueIndex);
					//if(log!=null) logList.add(log);
					long startTime2 = System.currentTimeMillis();
					if(logList.size()>=LogProcessorContext.logStoreMaxSize || isTimeExceed()){
						//LogManager.debug(LogProcessorContext.logCategory, getName() +"########## send log to MQ");
						jsmMessageId = sendAllLog();
						long endTime = System.currentTimeMillis();
						if(endTime-startTime2>=10)LogManager.debug(LogProcessorContext.logCategory, getName() +"[messageId:" +jsmMessageId +"] send time:" +(endTime-startTime) +", " + (endTime-startTime2));
						if(LogStore.getInstance().size(logQueueIndex)>10)LogManager.debug(LogProcessorContext.logCategory, "LogStroe queue size[queueIndex:" + logQueueIndex +"]:" + LogStore.getInstance().size(logQueueIndex));	
					}
					Thread.sleep(blockingQueuePollingPeriod*1000);
				}else{
					//에러 상태인 경우 5초마다 상태를 재 check한다.
					Thread.sleep(5*1000);
				}
			}catch(InterruptedException e){}
		}
		isRunning = false;
	}
	
	
	private boolean isTimeExceed(){
		return (System.currentTimeMillis()-lastSendTime>=LogProcessorContext.logStoreFlushInterval);
	}
	
	private void setLastSendTime(){
		lastSendTime = System.currentTimeMillis();
	}
	
	public String sendAllLog(){
		String jmsMessageId = null;
		int size = logList.size();
		if(size>0){
			jmsMessageId = send(logList.toArray());
			if(size!=logList.size()) LogManager.error(LogProcessorContext.logCategory, "logList의 사이즈가 다릅니다.[" + logList.size() +"]");
			logList.clear();
		}
		return jmsMessageId;
	}
		
	/**
	 * MQ로 메시지를 전송한다.
	 * @param obj 전송할 메시지
	 */
	public String send(Serializable obj){
		long startTime = System.currentTimeMillis();
		String messageId = null;
		try{
			//에러 상태의 경우 logstore에서 넘어온 데이터는 아무 처리하지 않는다.
			if(isError){
				LogManager.info(LogProcessorContext.logCategory, "LogSender is now in a error state..");
			}else{
		        ObjectMessage message = session.createObjectMessage(obj);
		        messageId = Integer.toString(message.hashCode());
		        producer.send(message);
			}
			
		}catch(JMSException e){
			onException(e);
		}finally{
			//send의 성공여부와 상관없이 마지막으로 호출된 시각을 셋팅한다.
			setLastSendTime();
		}
		return messageId;
	}
	
	/**
	 * LogSender를 완전히 종료한다.
	 * logQueue에 쌓인 메시지를 모두 처리한 후 
	 * connection, session, producer, connectionManager를 종료한다.
	 */
	public void destroy(){
		this.isStop = true;
		try{
			
			LogStore.getInstance().getQueue(this.logQueueIndex).freeAll();
			int tryCount=0;
			while(!isError && isRunning && tryCount++<5){
				Thread.sleep(1000);
				LogManager.debug(LogProcessorContext.logCategory, getName() +":: waiting to stop...");
			}
			
		}catch(InterruptedException e){}
		
		//종료 전에 LogStore에 쌓인 메시지가 있는지 확인하고 있으면 log MQ로 보낸다. 
		//logstore의 사이즈가 0보다 크면서 flush 시도가 3번 이하인 경우에만 flush 호출..
		int tryCount=0;
		while(logList.size()>0 && tryCount++<3){
			LogManager.debug(LogProcessorContext.logCategory, getName() +":: sendAllLog.. current LogList size:" + logList.size());
			sendAllLog();
		}
		
		stopConnectionManager();
		
		destroySession();
	}
	
	public String getName(){
		return "LogSendWorker[" + logQueueIndex + "]";
	}
	
	/**
	 * 현재 생성된 connection, session, producer을 모두 종료한다.
	 * 주로 jms queue와 connection이 끊어진 후 세션을 초기화하기 위해 호출된다. 
	 */
	private void destroySession(){
		
		if(producer!=null){
			try {
				producer.close();
			} catch (JMSException e) {}
		}
		if(session!=null){
			try {
				session.close();
			} catch (JMSException e) {}
		}
		if(connection!=null){
			try {
				connection.close();
			} catch (JMSException e) {}
		}

	}
	/**
	 * @author ksh
	 * 커넥션 재생성, 메시지 flush를 담당하는 쓰레드
	 */
	private class ConnectionManager implements Runnable{
		
		private boolean exit;

		public void run() {
			
			while(!exit){
				
				//에러 상태일 때는 커넥션을  재생성한다.
				if(isError){
					try {
						init();
					} catch (JMSException e) {
						LogManager.error(LogProcessorContext.logCategory, getName() +" initialization failed..[" + e +"]");
					}
				}
				synchronized(lock){
					try {
						//Thread.sleep(LogProcessorContext.logSenderConnectionCheckInterval);
						lock.wait(LogProcessorContext.logSenderConnectionCheckInterval);
					} catch (InterruptedException e) {}
				}
			}
			LogManager.info(LogProcessorContext.logCategory, getName() +"의 ConnectionManager를 종료하였습니다.");

		}
		
		public void stop(){
			LogManager.info(LogProcessorContext.logCategory, getName()+"의 ConnectionManager를 종료합니다..");
			this.exit = true;
			synchronized(lock){
				lock.notifyAll();
			}
		}
	}
	/**
	 * ConnectionManager 쓰레드를 시작시킨다.
	 */
	private void startConnectionManager(){
		try {
			connectionManager = new ConnectionManager();

			Thread th = new Thread(connectionManager);
			th.start();			
			LogManager.info(LogProcessorContext.logCategory, getName() + "ConnectionManager를 시작합니다.");
			
		} catch (Exception e) {
			LogManager.error("ConnectionManager 시작 중 오류 발생", e);
			//if(thread!=null){
			//	thread.setDone(true);
			//}
		}
	}
	private void stopConnectionManager(){
		if(connectionManager!=null){
			connectionManager.stop();
		}
		
	}
	
	/* (non-Javadoc)
	 * @see javax.jms.ExceptionListener#onException(javax.jms.JMSException)
	 */
	public synchronized void onException(JMSException exception) {
		LogManager.error(LogProcessorContext.logCategory, "JMSException 발생(LogSendWorker onException) ", exception);
		
		setError(true);
		destroySession();
				
	}
	
	/**
	 * 에러상태를 설정한다.
	 * 이 쓰레드가 담당하는 queue의 running 상태도 설정한다.
	 * @param isError
	 */
	public void setError(boolean isError){
		this.isError = isError;
		LogStore.getInstance().getQueue(logQueueIndex).setRunning(!isError);
	}
	
	public int getLogQueueIndex() {
		return logQueueIndex;
	}

	public void setLogQueueIndex(int logQueueIndex) {
		this.logQueueIndex = logQueueIndex;
	}

	public Object getLock() {
		return lock;
	}

}
