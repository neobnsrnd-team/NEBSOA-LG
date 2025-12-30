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
import java.util.Enumeration;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.TextMessage;

import nebsoa.common.log.LogManager;
/*******************************************************************
 * <pre>
 * 1.설명 
 * MQ로부터 로그 메시지를 수신하여 indexBuider를 호출하는 쓰레드
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
 * $Log: LogReceiveWorker.java,v $
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
 * Revision 1.9  2007/09/21 08:22:38  김승희
 * *** empty log message ***
 *
 * Revision 1.8  2007/09/06 01:25:15  김승희
 * *** empty log message ***
 *
 *
 * </pre>
 ******************************************************************/
public abstract class LogReceiveWorker implements ExceptionListener, MessageListener{
	
	protected LogReceiver logReceiver;
	protected Connection connection;
	protected Session session;
	protected Destination destination;
	protected MessageConsumer consumer;
	protected boolean isRunning;
	protected int index;
		
	
	public LogReceiveWorker(LogReceiver logReceiver, int index){
		this.logReceiver = logReceiver;
		this.index = index;
	}
	
	public void init() throws Exception{
		
        connection = logReceiver.createConnection();
        connection.setExceptionListener(this);
        connection.start();
                
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //session = connection.createSession(false, Session.DUPS_OK_ACKNOWLEDGE );
        destination = session.createQueue(LogProcessorContext.logQueueName);
        consumer = session.createConsumer(destination); 
        consumer.setMessageListener(this);
                
        LogManager.info(LogProcessorContext.logCategory, getName() + ":: initialized..");
	}
	
	public void onMessage(Message message) {
		isRunning = true;
		if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            String text;
			try {
				text = textMessage.getText();
				//Do noting but logging..
				LogManager.debug(getName() + ":: Received: " + text);
			} catch (JMSException e) {
				LogManager.error(LogProcessorContext.logCategory, getName() + ":: Error Occurred while processing message", e);
			}
            
            
        } else if (message instanceof ObjectMessage) {
        	long startTime = System.currentTimeMillis();
        	ObjectMessage objectMessage = (ObjectMessage) message;
        	try {
				Serializable obj = objectMessage.getObject();
				//LogManager.debug(LogProcessorContext.logCategory, getName() + ":: Data Received["+ (++count) +"] time: " + FormatUtil.getFormattedDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
				
				if(obj instanceof Object[]){
					Object[] logs = (Object[])obj;
					if(logs!=null){
						try {
							//buildIndex(logs);
							processLog(logs);
							//buildConcurrentIndex(logs);
						} catch (Exception e) {
							LogManager.error(LogProcessorContext.logCategory, getName() + ":: processLog call failed..", e);
						}
					}
				}
				
			} catch (JMSException e) {
				LogManager.error(LogProcessorContext.logCategory, getName() + ":: Error Occurred while processing message", e);
			}finally{
				isRunning = false;
				long elapsedTime = System.currentTimeMillis() - startTime;
				if(elapsedTime>=3*60*1000)LogManager.info(LogProcessorContext.logCategory, getName() + ":: processLog call took "+ elapsedTime);
				else if(elapsedTime>=300)LogManager.debug(LogProcessorContext.logCategory, getName() + ":: processLog call took "+ elapsedTime);
			}
            					
        }
	}
/*
	private void buildIndex(Object[] logs) throws Exception {
		IndexBuilder indexBuilder = IndexBuilder.getInstance();
		indexBuilder.build(logs);
	}*/
	
	protected abstract void processLog(Object[] logs) throws Exception;
	
		
	/* (non-Javadoc)
	 * @see javax.jms.ExceptionListener#onException(javax.jms.JMSException)
	 */
	public void onException(JMSException e) {
		LogManager.error(LogProcessorContext.logCategory, getName() +":: onException called..", e);
		
	}

	
	/**
	 * LogReceiver를 종료한다.
	 */
	public void destroy(){
		
		LogManager.info(LogProcessorContext.logCategory, getName() + " is now stopping..");
		try{
			waitAndCloseSession();
			flushAndProceeAllLog();
		}finally{
			closeConnection();
		}

		LogManager.info(LogProcessorContext.logCategory, getName() +" stopped.");
	}
	
	/**
	 * QueueBrowser를 생성하여 MQ에 쌓인 모든 로그를 꺼내어 인덱싱처리를 한다.
	 */
	private void flushAndProceeAllLog() {
		
		QueueBrowser queueBrowser = null;
		Session newSession = null;
		Destination newDestination = null;
		
		try{	
			//새로운 세션을 만들어 queue에 담긴 메시지를 일괄 꺼내어 인덱싱한다.
			newSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE );
			newDestination = newSession.createQueue(LogProcessorContext.logQueueName);
			queueBrowser = newSession.createBrowser((Queue)newDestination);
						
			Enumeration enu = queueBrowser.getEnumeration();
			Message message = null;
			
			for(int i=1; enu!=null && enu.hasMoreElements(); i++ ){
				LogManager.info(LogProcessorContext.logCategory, getName() + ":: [" + i + "] queueBrowser가 MQ에 남아 있는 log를 발견하였습니다.");
				message = (Message)enu.nextElement();
				this.onMessage(message);
			}
			try {
				LogManager.info(LogProcessorContext.logCategory, getName() + ":: [2]LogReceiver.isRunning:" + isRunning + ", MQ memory:" + MQBroker.getInstance().getUsage());
			} catch (Exception e) {}	
			
		} catch (JMSException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}finally{
			closeQueueBrowser(queueBrowser);
			closeSession(newSession);
		}
	}

	/**
	 * 현재 처리중인 인덱싱 작업이 끝나길 기다린 후 세션을 종료한다.
	 */
	private void waitAndCloseSession() {
		try {
			
			//Running하는 동안 기다림..최대 3분간..
			long startTime = System.currentTimeMillis();
			while(isRunning && (System.currentTimeMillis()-startTime)<=3*60*1000){
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {}
			}
			try {
				LogManager.info(LogProcessorContext.logCategory, getName() + "::[1]LogReceiver.isRunning:" + isRunning + ", MQ memory:" + MQBroker.getInstance().getUsage());
			} catch (Exception e) {	}
		}finally{	
			closeConsumer();
			closeSession();
		}
	}

	private void closeQueueBrowser(QueueBrowser queueBrowser) {
		if(queueBrowser!=null){
			try {
				queueBrowser.close();
			} catch (JMSException e) {		}
		}
	}

	private void closeSession(Session newSession) {
		if(newSession!=null){
			try {
				newSession.close();
			} catch (JMSException e) {		}
		}
	}

	private void closeConnection() {
		if(connection!=null){
			try {
				connection.close();
			} catch (JMSException e) {}
		}
	}

	private void closeSession() {
		if(session!=null){
			try {
				session.close();
			} catch (JMSException e) {}
		}
	}

	private void closeConsumer() {
		if(consumer!=null){
			try {
				consumer.close();
			} catch (JMSException e) {}
		}
	}
	
	public String getName(){
		return "LogReceiveWorker[" + index + "]";
	}
}
