/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.biz.ejb.async;

import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import nebsoa.biz.base.Biz;
import nebsoa.biz.factory.BizFactory;
import nebsoa.biz.util.BizInfo;
import nebsoa.biz.util.BizManager;
import nebsoa.common.log.LogManager;
import nebsoa.common.monitor.ContextLogger;
import nebsoa.common.util.DataMap;

/*******************************************************************
 * <pre>
 * 1.설명 
 * BIZ_APP 을 위한 Message Queue 에 메시지가 들어올 경우 비동기 방식으로 BizProcessor 을 호출하기 위한 MDB
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
 * $Log: BizAsyncProcessorBean.java,v $
 * Revision 1.1  2018/01/15 03:39:52  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:14  cvs
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
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:35  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:39:16  안경아
 * *** empty log message ***
 *
 * Revision 1.3  2007/11/06 04:08:32  jwlee
 * Async일때도 ContextLogger적용
 *
 * Revision 1.2  2007/03/07 02:40:17  김성균
 * BizInfo 못 찾았을 경우 예외처리 통일
 *
 * Revision 1.1  2006/11/10 08:51:07  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class BizAsyncProcessorBean implements MessageDrivenBean, MessageListener {
	
	/*----------------------------------
	 * 
	 * EJB 관련 Method
	 * 
	 ----------------------------------*/
	
	/**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 2096969982060127700L;
    
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
	}
    
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
     * 메시지로부터 bizId를 꺼내고, 해당 Biz클래스를 수행합니다.
     * 호출합니다.
     * 
     * @param message
     * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
     */
    public void onMessage(Message message) {

        try {
            if (message instanceof ObjectMessage) {
                ObjectMessage objMessage = (ObjectMessage) message;
                LogManager.debug("\t##### asyncProcessorBean.onMessage : ");
                String bizId = objMessage.getStringProperty("bizId");
                DataMap dataMap = (DataMap) objMessage.getObject();
                
                // Tier 분리시에도 Context 객체를 유지하기 위해서 ThreadLocal에 넣는다.
                ContextLogger.putContext(dataMap.getContext());
                
                BizInfo bizInfo = BizManager.getInstance().getBizInfo(bizId);
                Biz biz = BizFactory.getInstance().getBiz(bizInfo);
                biz.doProcess(dataMap);
            }
        } catch (Throwable e) {
            LogManager.error(e);
        } finally{
        	ContextLogger.clear();
        }
    }

}