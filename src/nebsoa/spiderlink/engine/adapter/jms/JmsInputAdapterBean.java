/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.engine.adapter.jms;

import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import nebsoa.common.log.LogManager;

/*******************************************************************
 * <pre>
 * 1.설명 
 * Message Queue 에 메시지가 들어올 경우 처리하기 위한 MDB
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
 * $Log: JmsInputAdapterBean.java,v $
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
 * Revision 1.3  2006/06/17 09:15:10  김승희
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public class JmsInputAdapterBean implements MessageDrivenBean, MessageListener {
	
	
	private transient MessageDrivenContext context;

	/**
	 * (non-Javadoc)
	 * 
	 * @param context
	 * @throws EJBException
	 * @see javax.ejb.MessageDrivenBean#setMessageDrivenContext(javax.ejb.MessageDrivenContext)
	 */
	public void setMessageDrivenContext(MessageDrivenContext context) throws EJBException {
		this.context = context;
	}//end of setMessageDriverContext()

	/**
	 * (non-Javadoc)
	 * 
	 * @throws EJBException
	 * @see javax.ejb.MessageDrivenBean#ejbRemove()
	 */
	public void ejbRemove() throws EJBException {
	}//end of ejbRemove()
	
	public void ejbCreate() throws EJBException {		
	}//end of ejbCreate()

	/**
	 * (non-Javadoc)
	 * 
	 * @param message
	 * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
	 */
	public void onMessage(Message message) {
		
		try {
			if (message instanceof ObjectMessage) {
				ObjectMessage objMessage = (ObjectMessage)message;
//				nebsoa.spiderlink.engine.message.Message msg = (nebsoa.spiderlink.engine.message.Message)objMessage.getObject();
//				
//				// TODO business-delegate 를 호출하는 부분이 들어가야 한다.
//				
//				Map mapMsg = msg.getFieldValue();
//				
//				System.out.println("\t##### JmsInputAdapterBean.onMessage : " + mapMsg);
				
			}//end if
		} catch (Exception e) {
			LogManager.error(e);
		}//end try catch
		
	}//end of onMessage()

}// end of JmsInputAdapterBean.java