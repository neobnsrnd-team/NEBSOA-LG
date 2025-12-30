/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.engine.adapter.jms;

import java.io.Serializable;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.NamingException;

import nebsoa.common.exception.SysException;
import nebsoa.common.jndi.JNDIManager;
import nebsoa.common.log.LogManager;

/*******************************************************************
 * <pre>
 * 1.설명 
 * JMS 를 사용하여 Queue 에 메시지를 전송하는 클래스
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
 * $Log: JmsOutputAdapter.java,v $
 * Revision 1.1  2018/01/15 03:39:52  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:15  cvs
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
 * Revision 1.1  2008/01/22 05:58:29  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:39:09  안경아
 * *** empty log message ***
 *
 * Revision 1.7  2006/11/10 08:16:51  김성균
 * *** empty log message ***
 *
 * Revision 1.6  2006/06/20 09:57:39  이종원
 * finalize구현
 *
 * Revision 1.5  2006/06/20 09:54:39  이종원
 * *** empty log message ***
 *
 * Revision 1.4  2006/06/17 09:15:10  김승희
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public class JmsOutputAdapter {
	
	private String queueConnectionFactoryName;
	private String queueName;
	private String serverInfo;
	
	private QueueConnectionFactory queueConnectionFactory;
	private Queue queue;
	
	private QueueConnection queueConnection;
	private QueueSession queueSession;
	private QueueSender queueSender;
	
	/**
	 * JmsOutputAdapter 를 지정된 정보로 초기화 합니다.
	 * 
	 * @param queueConnectionFactoryName QueueConnectionFactory 의 JNDI 이름
	 * @param queueName Queue 의 JNDI 이름
	 * @param serverInfo 서버 정보 (was_config.properties.xml 파일에 있는 서버 이름)
	 */
	public JmsOutputAdapter(String queueConnectionFactoryName, String queueName, String serverInfo) {
		this.queueConnectionFactoryName = queueConnectionFactoryName;
		this.queueName = queueName;
		this.serverInfo = serverInfo;
		init();
	}//end of constructor
	
	/**
	 * JmsOutputAdapter 를 초기화 합니다.
	 *
	 */
	public void init() {
        Context context = null;
        try {
            context = JNDIManager.getInitialContext(this.serverInfo);
            LogManager.debug("Context : ["+context + "]") ;
        } catch (Exception e) {
            throw new SysException("JNDIManager.getInitialContext error : " + e.getMessage());
        }//end try catch
        try {
            LogManager.debug("QueueConnectionFactory Name : [" + this.queueConnectionFactoryName + "]") ;
            this.queueConnectionFactory = (QueueConnectionFactory)context.lookup(this.queueConnectionFactoryName);
            
            LogManager.debug("Queue Name : [" + this.queueName + "]") ;
            this.queue = (Queue)context.lookup(this.queueName);
        } catch (NamingException e) {
            throw new SysException("QueueConnectionFactory OR Queue lookup error : " + e.getMessage());
        } finally {
            try{
                if (context != null){
                    context.close();
                    context = null;
                }//end if
            } catch(Exception e) {}//ignored
        }//end try catch finally
		try {
			this.queueConnection = this.queueConnectionFactory.createQueueConnection();
			this.queueSession = this.queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			this.queueSender = this.queueSession.createSender(this.queue);
			this.queueConnection.start();			
		} catch (JMSException e) {
			throw new SysException("Queue Initialize error : " + e.getMessage());
		}//end try catch
	}//end of init()
	
	/**
	 * 메시지를 지정된 큐로 전송합니다.
	 * 
	 * @param message 메시지를 담고 있는 Serializable 타입의 객체
	 */
	public void sendObjectMessage(String trxId, String orgId, String ioType,Serializable message) {
		try {
			
			ObjectMessage objMessage = queueSession.createObjectMessage(message);
            objMessage.setStringProperty("trxId",trxId);
            objMessage.setStringProperty("orgId",orgId);
            objMessage.setStringProperty("ioType",ioType);
            
			this.queueSender.send(objMessage);
			
		} catch (JMSException e) {
			throw new SysException("메시지를 큐에 전송하는 중 오류 발생 : " + e.getMessage());
		}//end try catch
	}//end of sendObjectMessage()
    
    /**
     * 메시지를 지정된 큐로 전송합니다.
     * 
     * @param message 메시지를 담고 있는 Serializable 타입의 객체
     */
    public void sendObjectMessage(String bizId, Serializable message) {
        try {
            
            ObjectMessage objMessage = queueSession.createObjectMessage(message);
            objMessage.setStringProperty("bizId",bizId);
            
            this.queueSender.send(objMessage);
            
        } catch (JMSException e) {
            throw new SysException("메시지를 큐에 전송하는 중 오류 발생 : " + e.getMessage());
        }
    }
	
    boolean finallized;
	/**
	 * JmsOutputAdapter 를 종료화 합니다.
	 *
	 */
	public void destroy() {
        if(finallized) return;
		try {
			if (this.queueSender != null) {
				this.queueSender.close();
			}//end if
		} catch (Exception e) {}//ignored
		try {
			if (this.queueSession != null) {
				this.queueSession.close();
			}//end if
		} catch (Exception e) {}//ignored
		try {
			if (this.queueConnection != null) {
				this.queueConnection.close();
			}//end if
		} catch (Exception e) {}//ignored
        
        finallized=true;
        
	}//end of destroy()
    
    public void finalize(){
        if(!finallized){
            LogManager.error("닫혀지지 않은  queue Adapter... found");
        }
        destroy();
    }

}// end of JmsOutputAdapter.java