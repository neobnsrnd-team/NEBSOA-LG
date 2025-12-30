/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.logserver.ejb;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import nebsoa.common.base.BaseSessionBean;
import nebsoa.common.log.LogManager;
import nebsoa.logserver.ConnectionFactoryManager;
import nebsoa.logserver.LogProcessorContext;
import nebsoa.logserver.MQBroker;
import nebsoa.logserver.SearchManager;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 로그를 MQ로 전송하는 클래스
 * 
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
 * $Log: RemoteLogSenderBean.java,v $
 * Revision 1.1  2018/01/15 03:39:52  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:13  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:26  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:28  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:54  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:31  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/12/17 01:49:02  김승희
 * 로그 서버 서버 분리에 따른 수정
 *
 * Revision 1.1  2007/12/06 04:57:09  shkim
 * 최초 등록
 *
 *
 * </pre>
 ******************************************************************/
public class RemoteLogSenderBean extends  BaseSessionBean implements RemoteLogSender, ExceptionListener {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -5067565790166274880L;

	/* (non-Javadoc)
	 * @see nebsoa.logserver.ejb.RemoteLogSender#send(java.io.Serializable)
	 */
	public void send(Serializable log) throws RemoteException {
		
		Connection connection = null;
		Session session = null;
		MessageProducer producer = null;
		long startTime = System.currentTimeMillis();
		long startTime2 = startTime;
		try {
			
			//MQBroker가 running일 때에만 다음 작업을 한다. 아니면 그냥 통과
			boolean isMQServiceRunning = MQBroker.getInstance().isRunning();
			
			if(isMQServiceRunning){
				connection = ConnectionFactoryManager.getInstance().createConnection();
				connection.setExceptionListener(this);
				connection.start();
			
				session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
				Destination destination = session.createQueue(LogProcessorContext.logQueueName);
				producer = session.createProducer(destination);
				producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
				
				ObjectMessage message = session.createObjectMessage(log);
				startTime2 = System.currentTimeMillis();
		        producer.send(message);
			}
	        
		} catch (JMSException e) {
			onException(e);
		} catch (Exception e) {
			LogManager.error(LogProcessorContext.logCategory, "RemoteLogSenderBean send 오류", e);
		}finally{
			destroySession(connection, session, producer);
			LogManager.debug(LogProcessorContext.logCategory, "RemoteLogSenderBean send total time: " + (System.currentTimeMillis()-startTime) +", MQ send time: " + (System.currentTimeMillis()-startTime2));
		}
		
	}
	
	/**
	 * jms 자원을 모두 close한다.
	 * @param connection
	 * @param session
	 * @param producer
	 */
	private void destroySession(Connection connection, Session session, MessageProducer producer){
		
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
	
	/* (non-Javadoc)
	 * @see javax.jms.ExceptionListener#onException(javax.jms.JMSException)
	 */
	public void onException(JMSException e) {
		LogManager.error(LogProcessorContext.logCategory, "RemoteLogSenderBean send 오류", e);
		
	}

	/* (non-Javadoc)
	 * @see nebsoa.logserver.ejb.RemoteLogSender#search(java.lang.String)
	 */
	public ArrayList search(String keyword) throws RemoteException, Exception {
		return SearchManager.getInstance().search(keyword);
		
	}
}
