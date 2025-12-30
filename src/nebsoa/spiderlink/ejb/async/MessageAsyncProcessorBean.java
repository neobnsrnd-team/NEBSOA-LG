/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.ejb.async;

import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;


import nebsoa.common.log.LogManager;
import nebsoa.common.util.DataMap;
import nebsoa.spiderlink.engine.MessageEngine;

/*******************************************************************
 * <pre>
 * 1.설명 
 * BPM 을 위한 Message Queue 에 메시지가 들어올 경우 비동기 방식으로 BpmEngine 을 호출하기 위한 MDB
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
 * $Log: MessageAsyncProcessorBean.java,v $
 * Revision 1.1  2018/01/15 03:39:54  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:39  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:27  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:29  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:53  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:28  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:39:05  안경아
 * *** empty log message ***
 *
 * Revision 1.6  2006/06/20 09:47:47  이종원
 * 비동기 처리 로직 구현
 *
 * Revision 1.5  2006/06/19 13:45:55  김성균
 * *** empty log message ***
 *
 * Revision 1.4  2006/06/19 07:34:13  김승희
 * method 변경
 *
 * Revision 1.3  2006/06/17 08:38:33  김승희
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public class MessageAsyncProcessorBean implements MessageDrivenBean, MessageListener {
	
	/*----------------------------------
	 * 
	 * EJB 관련 Method
	 * 
	 ----------------------------------*/
	
	/**
	 * EJB 의 MessageDrivenContext 객체
	 */
	private transient MessageDrivenContext context;

	/**
	 * MessageDrivenContext 를 세팅합니다.
	 * 
	 * @param context MessageDrivenContext 객체
	 * @throws EJBException MessageDrivenContext 객체 설정 중 발생하는 Exception
	 * @see javax.ejb.MessageDrivenBean#setMessageDrivenContext(javax.ejb.MessageDrivenContext)
	 */
	public void setMessageDrivenContext(MessageDrivenContext context) throws EJBException {
		this.context = context;
	}//end of setMessageDriverContext()

	/**
	 * EJB 를 remove 합니다.
	 * 
	 * @throws EJBException EJB 를 remove 하는 중 발생하는 Exception
	 * @see javax.ejb.MessageDrivenBean#ejbRemove()
	 */
	public void ejbRemove() throws EJBException {}//end of ejbRemove()
	
	/**
	 * EJB 를 create 합니다.
	 * 
	 * @throws EJBException EJB 를 create 하는 중 발생하는 Exception
	 * @see javax.ejb.MessageDrivenBean#ejbCreate()
	 */
	public void ejbCreate() throws EJBException {}//end of ejbCreate()

	/**
	 * 메시지 큐에 메시지가 수신되면 실행되는 메소드 입니다.
	 * 
	 * 메시지로부터 ServiceContext 객체를 꺼내고,
	 * 해당 ServiceContext 를 파라미터로 하여 BpmEngine 을 호출합니다.
	 * 
	 * @param message
	 * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
	 */
	public void onMessage(Message message) {
		
		try {
			if (message instanceof ObjectMessage) {
				ObjectMessage objMessage = (ObjectMessage)message;				
				LogManager.debug("\t##### asyncProcessorBean.onMessage : ");
				String trxId=objMessage.getStringProperty("trxId");
                String orgId=objMessage.getStringProperty("orgId");
                String ioType=objMessage.getStringProperty("ioType");
                DataMap dataMap = (DataMap) objMessage.getObject();
				//TODO 아래부분 결정
				MessageEngine.doAsyncProcess(trxId, orgId, ioType, dataMap);
			}//end if
		} catch (Throwable e) {
			LogManager.error(e);
		}//end try catch
		
	}//end of onMessage()

}// end of BpmAsyncProcessorBean.java