/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
package nebsoa.logserver;

import javax.jms.Connection;
import javax.jms.JMSException;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.pool.PooledConnectionFactory;

import nebsoa.common.log.LogManager;
/*******************************************************************
 * <pre>
 * 1.설명 
 * JMS ConnectionFactory Manager
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
 * $Log: ConnectionFactoryManager.java,v $
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
 * Revision 1.1  2007/12/17 01:48:35  김승희
 * 로그 서버 서버 분리에 따른 수정
 *
 * Revision 1.2  2007/12/07 07:32:41  shkim
 * *** empty log message ***
 *
 * Revision 1.1  2007/12/06 04:57:09  shkim
 * 최초 등록
 *
 *
 *
 * </pre>
 ******************************************************************/
public class ConnectionFactoryManager {
	
	private static ConnectionFactoryManager instance;
    
	private static Object dummy=new Object();
	
	public static ConnectionFactoryManager getInstance(){
	   if(instance==null){
	       synchronized (dummy) {
	    	   if(instance==null) instance = new ConnectionFactoryManager();
	       }
	    }
	    return instance;
	}

	private ActiveMQConnectionFactory connectionFactory;
	
	private PooledConnectionFactory pooledConnectionFactory;
	
	private ConnectionFactoryManager(){}
	
	private boolean isInitialized = false;
	
	private synchronized void createConnectionFactory() {
		if(isInitialized || connectionFactory!=null) {
			LogManager.info(LogProcessorContext.logCategory, "ConnectionFactory already created..");
			return;
		}
		
		String uri = LogProcessorContext.logSenderConecctionUri;
		connectionFactory = new ActiveMQConnectionFactory(uri);
		//connectionFactory.setDispatchAsync(true);
		//connectionFactory.setObjectMessageSerializationDefered(true);
		//connectionFactory.setCopyMessageOnSend(false);
		
		//TODO 속성으로 뺀다.
		int maximumActive = 50;
		
		pooledConnectionFactory = new PooledConnectionFactory(connectionFactory);
		pooledConnectionFactory.setMaximumActive(maximumActive);
						
		LogManager.info(LogProcessorContext.logCategory, "Log queue connectionFactory[" + uri +"] created..");
		
	}
	
	/**
	 * Connection을 획득한다.
	 * @return Connection
	 * @throws JMSException
	 */
	public Connection createConnection() throws JMSException{
		if(pooledConnectionFactory==null){
			LogManager.error(LogProcessorContext.logCategory, "ConnectionFactory가 정상적으로 생성되지 못했습니다.");
		}
		//return connectionFactory.createConnection();
		return pooledConnectionFactory.createConnection();
	}
	
	/**
	 * ConnectionFactory를 생성하고 start 시킨다.
	 */
	public synchronized void start(){
		createConnectionFactory();
		if(pooledConnectionFactory!=null ){
			pooledConnectionFactory.start();
			LogManager.info(LogProcessorContext.logCategory, "Log queue connectionFactory started..");
	    }
	}
	
	/**
	 * ConnectionFactory를 생성하고 stop 시킨다.
	 * @throws Exception 
	 */
	public synchronized void stop() throws Exception{
		if(pooledConnectionFactory!=null){
			pooledConnectionFactory.stop();
	    }
	}
}
