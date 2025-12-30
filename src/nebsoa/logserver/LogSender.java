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
/*******************************************************************
 * <pre>
 * 1.설명 
 * Message Queue로 로그 데이터를 송신하는 클래스. 
 * 내부에 ConnectionManager 쓰레드를 갖고 있어 일정 시간 간격으로 세션 체크와 LogStore flush를 처리한다.
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
 * $Log: LogSender.java,v $
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
 * Revision 1.8  2007/09/03 11:30:42  김승희
 * *** empty log message ***
 *
 * Revision 1.7  2007/08/28 10:02:33  김승희
 * *** empty log message ***
 *
 * Revision 1.6  2007/08/22 06:33:41  김승희
 * *** empty log message ***
 *
 * Revision 1.5  2007/08/17 11:09:00  김승희
 * *** empty log message ***
 *
 * Revision 1.4  2007/08/16 07:09:45  김승희
 * 성능 향상을 위한 수정
 *
 * Revision 1.3  2007/07/24 07:14:21  김승희
 * *** empty log message ***
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
 * Revision 1.2  2007/06/12 05:25:51  shkim
 * *** empty log message ***
 *
 * Revision 1.1  2007/06/11 08:24:38  shkim
 * 최초 등록
 *
 *
 * </pre>
 ******************************************************************/
public class LogSender {
	

	private ActiveMQConnectionFactory connectionFactory;
		
	private boolean isError = false;
	
	private ArrayList logSendWorkerList = new ArrayList();
	
	private static Object dummy=new Object();
	private static LogSender instance;
	    
	public static LogSender getInstance(){
	   if(instance==null){
	       synchronized (dummy) {
	    	   if(instance==null) instance = new LogSender();
	       }
	    }
	    return instance;
	}
	    
	private LogSender(){
		
	}
    
	private void createConnectionFactory() {

		String uri = LogProcessorContext.logSenderConecctionUri;
		connectionFactory = new ActiveMQConnectionFactory(uri);
		//connectionFactory.setDispatchAsync(true);
		//connectionFactory.setObjectMessageSerializationDefered(true);
		//connectionFactory.setCopyMessageOnSend(false);
		LogManager.info(LogProcessorContext.logCategory, "Log queue connectionFactory[" + uri +"] created..");
		
	}
	
	public void start(){
		createConnectionFactory();
		
		for(int i=0; i<LogProcessorContext.logSendWorkerCount; i++){
			try {
				LogSendWorker logSendWorker = new LogSendWorker(this, i);
				logSendWorkerList.add(logSendWorker);

				Thread th = new Thread(logSendWorker);
				th.start();
			} catch (Exception e) {
				LogManager.error(LogProcessorContext.logCategory, "LogSendWorker[" + i +"] 시작 중 오류 발생", e);

			}
		}
		
	}
	
	public Connection createConnection() throws JMSException{
		return connectionFactory.createConnection();
	}
	
	public void stop(){
		LogSendWorker logSendWorker = null;
		for(int i=0; i<logSendWorkerList.size(); i++){
			logSendWorker = (LogSendWorker)logSendWorkerList.get(i);
			logSendWorker.destroy();
			LogManager.info(LogProcessorContext.logCategory, i +"번 째 LogSendWorker를 종료하였습니다.");
		}
		logSendWorkerList.clear();
	}

}
