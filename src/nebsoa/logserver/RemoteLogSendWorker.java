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
import java.rmi.ConnectException;
import java.util.ArrayList;

import nebsoa.common.jndi.EJBManager;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.PropertyManager;
import nebsoa.logserver.ejb.RemoteLogSenderEJB;
/*******************************************************************
 * <pre>
 * 1.설명 
 * ejb를 통하여 리모트의 로그 서버로 로그을 전송하는 클래스
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
 * $Log: RemoteLogSendWorker.java,v $
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
 * Revision 1.2  2008/01/17 09:40:16  김승희
 * 로그 서버 장애 시 장애 상태 체크 looping 로직에 interval 추가
 *
 * Revision 1.4  2008/01/17 09:33:43  shkim
 * 로그 서버 장애 시 장애 상태 체크 looping 로직에 interval 추가
 *
 * Revision 1.3  2007/12/07 07:48:57  shkim
 * *** empty log message ***
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
public class RemoteLogSendWorker implements Runnable{
	
	private Object lock = new Object();
	
	private Object sendWaitlock = new Object();
	
	private RemoteLogSender logSender;

	private ArrayList logList = new ArrayList(1000);
	
	private boolean isStop;
	private boolean isError;
	private boolean isRunning;
	
	private long lastSendTime = System.currentTimeMillis();
	
	/**
	 * 이 쓰레드가 담당하고 있는 queue의 index (로그가 실시간으로 쌓이는 BlockingQueue의 갯수만큼 쓰레드가 생성되며 하나씩 담당한다.)
	 */
	private int logQueueIndex = -1;
	
    
   
    
	public RemoteLogSendWorker(RemoteLogSender logSender, int logQueueIndex) {
		this.logSender = logSender;
		this.logQueueIndex = logQueueIndex;
	
	}
		
	int timeout = 3*1000;
	
	public void run() {

		LogBlockingQueue queue=null;
		
		while(!isStop){
			int blockingQueuePollingPeriod=
				PropertyManager.getIntProperty("logserver","BLOCKING_QUEUE_POLLING_PERIOD",5);
			isRunning = true;

			try{
				
				if(!isError){
										
					long startTime = System.currentTimeMillis();
					
					queue = LogStore.getInstance().getQueue(logQueueIndex);
					if(queue != null){
						
						queue.copyToArray(logList, timeout);
						
					}
					
					//log = LogStore.getInstance().get(logQueueIndex, timeout);
					//if(log!=null) logList.add(log);
					long startTime2 = System.currentTimeMillis();
					if(logList.size()>=LogProcessorContext.logStoreMaxSize || isTimeExceed()){
						sendAllLog();
						long endTime = System.currentTimeMillis();
						if(endTime-startTime2>=10)LogManager.debug(LogProcessorContext.logCategory, getName() +" send time:" +(endTime-startTime) +", " + (endTime-startTime2));
						if(LogStore.getInstance().size(logQueueIndex)>10)LogManager.debug(LogProcessorContext.logCategory, "LogStroe queue size[queueIndex:" + logQueueIndex +"]:" + LogStore.getInstance().size(logQueueIndex));	
					}
					//Thread.sleep(blockingQueuePollingPeriod*1000);
					
					synchronized(sendWaitlock){
						try {
							sendWaitlock.wait(blockingQueuePollingPeriod*1000);
						} catch (InterruptedException e) {}
					}
				}else{
					//에러 상태의 경우 10초 마다 while loop를 반복한다.
					synchronized(sendWaitlock){
						try {
							sendWaitlock.wait(10*1000);
						} catch (InterruptedException e) {}
					}
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
	
	public void sendAllLog(){
		int size = logList.size();
		if(size>0){
			send(logList.toArray());
			//if(size!=logList.size()) LogManager.error(LogProcessorContext.logCategory, "logList의 사이즈가 다릅니다.[" + logList.size() +"]");
			//logList.clear();
		}
	}
		
	/**
	 * MQ로 메시지를 전송한다.
	 * @param obj 전송할 메시지
	 */
	public void send(Serializable log){
		try{
			//에러 상태의 경우 logstore에서 넘어온 데이터는 아무 처리하지 않는다.
			if(isError){
				LogManager.info(LogProcessorContext.logCategory, "LogSender is now in a error state..");
			}else{
		        //ejb를 통해 MQ로 메시지를 전송한다.
				RemoteLogSenderEJB ejb = RemoteLogSender.getEJB();
				ejb.send(log);
			}
			
		}catch(ConnectException e){
			LogManager.error(LogProcessorContext.logCategory, "Exception 발생(LogSendWorker onException) "+ e);
			setError(true);
			EJBManager.removeCache(RemoteLogSender.ejbName, LogProcessorContext.remoteLogSenderWasId);
			
		}catch(Throwable e){
			LogManager.error(LogProcessorContext.logCategory, "Exception 발생(LogSendWorker onException) "+ e);
			setError(true);
		}finally{
			//send의 성공여부와 상관없이 마지막으로 호출된 시각을 셋팅한다.
			setLastSendTime();
			
			//send의 성공여부와 상관없이 log list도 clear한다.
			logList.clear();
		}
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
			
			synchronized(sendWaitlock){
				sendWaitlock.notifyAll();
			}
			
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
	}
	
	public String getName(){
		return "RemoteLogSendWorker[" + logQueueIndex + "]";
	}
	
	/**
	 * 에러상태를 설정한다.
	 * 이 쓰레드가 담당하는 queue의 running 상태도 설정한다.
	 * @param isError
	 */
	public void setError(boolean isError){
		this.isError = isError;
		//logstore 자체를 중지시킨다.
		LogStore.getInstance().setStop(isError);
		LogStore.getInstance().getQueue(logQueueIndex).setRunning(!isError);
		logSender.setError(isError);
		//LogManager.info(LogProcessorContext.logCategory, getName() +" 에러  상태:" + isError +"으로 변경..");
	}
	
	public int getLogQueueIndex() {
		return logQueueIndex;
	}

	public void setLogQueueIndex(int logQueueIndex) {
		this.logQueueIndex = logQueueIndex;
	}
}
