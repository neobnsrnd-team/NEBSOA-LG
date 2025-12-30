/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
package nebsoa.logserver;

import nebsoa.common.log.LogManager;
import nebsoa.common.startup.StartupContext;
import nebsoa.logserver.index.IndexBuilderManager;
/*******************************************************************
 * <pre>
 * 1.설명 
 * Message Queue, LogStore, LogReceiver 등의 시작과 종료를 담당하는 클래스. 
 * 
 * 2.사용법
 * application 시작시에 start(), 종료시에 stop()을 호출한다.
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
 * $Log: LogProcessor.java,v $
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
 * Revision 1.9  2007/12/07 07:32:41  shkim
 * *** empty log message ***
 *
 * Revision 1.8  2007/12/06 04:52:37  shkim
 * 로그 서버 분리에 따른 start, stop 대상 분기
 *
 * Revision 1.7  2007/10/02 07:56:29  김승희
 * *** empty log message ***
 *
 * Revision 1.6  2007/09/21 08:22:38  김승희
 * *** empty log message ***
 *
 * Revision 1.5  2007/08/30 16:50:05  shkim
 * *** empty log message ***
 *
 * Revision 1.4  2007/08/22 06:32:50  김승희
 * *** empty log message ***
 *
 * Revision 1.3  2007/08/16 07:23:19  김승희
 * 버그 수정
 *
 * Revision 1.2  2007/08/16 07:09:45  김승희
 * 성능 향상을 위한 수정
 *
 * Revision 1.1  2007/06/15 02:05:07  김승희
 * 프로젝트 변경 신규 커밋
 *
 * Revision 1.1  2007/06/13 05:29:29  shkim
 * 패키지 변경
 *
 * Revision 1.4  2007/06/12 06:07:53  shkim
 * 프로퍼티 변경
 *
 * Revision 1.3  2007/06/12 05:25:51  shkim
 * *** empty log message ***
 *
 * Revision 1.2  2007/06/12 02:30:13  shkim
 * 프로퍼티 반영
 *
 * Revision 1.1  2007/06/11 08:24:38  shkim
 * 최초 등록
 *
 *
 * </pre>
 ******************************************************************/

public class LogProcessor {
	
	private static Object dummy=new Object();
	private static LogProcessor instance;
	
	public static LogProcessor getInstance(){
	    if(instance==null){
	        synchronized (dummy) {
	             instance = new LogProcessor();
	        }
	    }
	    return instance;
	}
	
	private boolean isStarted = false;
	
	private LogProcessor(){}
	
	/**
	 * LogProcessor를 시작시킨다.
	 */
	public synchronized void start(){
		//islogProcessorUse 이 true인 경우만 시작시킨다..
		if(LogProcessorContext.islogProcessorUse){
			if(!isStarted){
				boolean isSuccess = false;
				
				String wasId = StartupContext.getInstanceId();
				
				//PropertyManager.
				try{
					
					if(wasId.equals(LogProcessorContext.remoteLogSenderWasId)){
						//MQBroker를 시작시킨다.
						MQBroker.getInstance().start();
						LogManager.info(LogProcessorContext.logCategory, "## MQBroker successfully started..");
						
						//JMS ConnectionFactory를 시작시킨다.
						ConnectionFactoryManager.getInstance().start();
						LogManager.info(LogProcessorContext.logCategory, "## ConnectionFactoryManager successfully started..");
						
						try{
							//LogReceiver를 시작시킨다.
							LogReceiver.getInstance().start();
							LogManager.info(LogProcessorContext.logCategory, "## LogReceiver successfully started..");
						}catch(Throwable e){
							//LogReceiver가 정상적으로 시작되지 못한 경우 ConnectionFactory와 MQBroker를 중지시킨다.
														
							try{
								ConnectionFactoryManager.getInstance().stop();
								LogManager.info(LogProcessorContext.logCategory, "## ConnectionFactoryManager stopped because LogReceiver did not start ..");
							}catch(Exception ex){
								LogManager.error(LogProcessorContext.logCategory, "## ConnectionFactoryManager didn't stopped properly.", ex);
							}
							
							MQBroker.getInstance().stop();
							LogManager.info(LogProcessorContext.logCategory, "## MQBroker stopped because LogReceiver did not start ..");
							
							throw e;
						}
						LogManager.info(LogProcessorContext.logCategory, "## Log Receiver Type:" + LogProcessorContext.receiveWorkerType);
						
						//IndexBuilderManager를 시작시킨다.(receiver 타입이 Index인 경우에만)
						if(LogProcessorContext.receiveWorkerType.equalsIgnoreCase(LogProcessorContext.RECEIVE_WORKER_TYPE_INDEX)){
							IndexBuilderManager.getInstance().start();
							LogManager.info(LogProcessorContext.logCategory, "## IndexBuilderManager successfully started..");
						}
					
					}
					if(!wasId.equals(LogProcessorContext.remoteLogSenderWasId)){
						//LogSender를 시작시킨다.
						RemoteLogSender.getInstance().start();
						LogManager.info(LogProcessorContext.logCategory, "## LogSender successfully started..");
					}
					
					isSuccess = true;
					
				}catch(Throwable e){
					LogManager.error(LogProcessorContext.logCategory, "## MQBroker, LogReceiver or LogSender starting failed.", e);
					isSuccess = false;
					
				}finally{
					
					if(!wasId.equals(LogProcessorContext.remoteLogSenderWasId)){
						//MQBroker와 LogReceiver가 정상적으로 시작되지 못했을 경우 LogStore는 생성하되 stop시킨다.
						LogStore logStore = LogStore.getInstance();
						logStore.setInitialized(true);
						if(isSuccess){
							logStore.start();
							LogManager.info(LogProcessorContext.logCategory, "## LogStore successfully started..");
						}else{
							logStore.stop();
							LogManager.info(LogProcessorContext.logCategory, "## LogStore now stopped because MQBroker or LogReceiver starting failed.");
						}
					}
					isStarted = true;
					
				}
				LogManager.info(LogProcessorContext.logCategory, "## LogProcessor successfully started..");
			}else{
				LogManager.info(LogProcessorContext.logCategory, "## LogProcessor was already started..");
			}
		}
	}
	
	/**
	 * LogProcessor를 종료시킨다. 
	 */
	public synchronized void stop(){
		if(isStarted){
			
			String wasId = StartupContext.getInstanceId();
			
			if(!wasId.equals(LogProcessorContext.remoteLogSenderWasId)){
				try{
					//LogStore를 종료시킨다.
					LogStore.getInstance().destory();
					LogManager.info(LogProcessorContext.logCategory, "## LogStore successfully destroyed..");
				}catch(Throwable e){
					LogManager.error(LogProcessorContext.logCategory, "## LogStore didn't stopped properly.", e);
				}	
				try{
					//LogSender를 종료시킨다.
					RemoteLogSender.getInstance().stop();
					LogManager.info(LogProcessorContext.logCategory, "## LogSender successfully stopped..");
				}catch(Throwable e){
					LogManager.error(LogProcessorContext.logCategory, "## LogSender didn't stopped properly.", e);
				}
			}
			
			if(wasId.equals(LogProcessorContext.remoteLogSenderWasId)){
				try{
					//LogReceiver를 종료시킨다.
					LogReceiver.getInstance().stop();
					LogManager.info(LogProcessorContext.logCategory, "## LogReceiver successfully stopped..");
				}catch(Throwable e){
					LogManager.error(LogProcessorContext.logCategory, "## LogReceiver didn't stopped properly.", e);
				}
				
				//receiver 타입이 Index인 경우 IndexBuilderManager를 종료시킨다.
				if(LogProcessorContext.receiveWorkerType.equalsIgnoreCase(LogProcessorContext.RECEIVE_WORKER_TYPE_INDEX)){
					try{
						//IndexBuilderManager를 종료시킨다.
						IndexBuilderManager.getInstance().destory();
						LogManager.info(LogProcessorContext.logCategory, "## IndexBuilderManager successfully stopped..");
					}catch(Throwable e){
						LogManager.error(LogProcessorContext.logCategory, "## IndexBuilderManager didn't stopped properly.", e);
					}
				}
				
				//ConnectionFactoryManager를 종료시킨다.
				try {
					ConnectionFactoryManager.getInstance().stop();
					LogManager.info(LogProcessorContext.logCategory, "## ConnectionFactoryManager successfully stopped..");
				} catch (Exception e) {
					LogManager.error(LogProcessorContext.logCategory, "## ConnectionFactoryManager didn't stopped properly.", e);
				}
				LogManager.info(LogProcessorContext.logCategory, "## MQBroker successfully stopped..");
				
				//MQBroker를 종료시킨다.
				try{
					MQBroker.getInstance().stop();
					LogManager.info(LogProcessorContext.logCategory, "## MQBroker successfully stopped..");
				}catch(Throwable e){
					LogManager.error(LogProcessorContext.logCategory, "## MQBroker didn't stopped properly.", e);
				}
			}
			isStarted = false;			
			LogManager.info(LogProcessorContext.logCategory, "## LogProcessor stopped..");
		}else{
			LogManager.info(LogProcessorContext.logCategory, "## LogProcessor already stopped..");
		}
	}
	
}
