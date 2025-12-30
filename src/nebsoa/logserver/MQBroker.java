/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
package nebsoa.logserver;

import java.util.Iterator;

import org.apache.activemq.broker.BrokerService;

import nebsoa.common.log.LogManager;
/*******************************************************************
 * <pre>
 * 1.설명 
 * Message Queue의 생성과 시작을 담당하는 클래스.
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
 * $Log: MQBroker.java,v $
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
 * Revision 1.7  2007/12/06 04:56:05  shkim
 * MQ 사용량이 80%를 초과한 경우 서비스 중지 flag를 변경하는 방식으로 수정
 *
 * Revision 1.6  2007/08/28 10:02:33  김승희
 * *** empty log message ***
 *
 * Revision 1.5  2007/08/22 06:32:50  김승희
 * *** empty log message ***
 *
 * Revision 1.4  2007/08/19 08:22:57  shkim
 * *** empty log message ***
 *
 * Revision 1.3  2007/08/17 11:09:00  김승희
 * *** empty log message ***
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
 * Revision 1.1  2007/06/11 08:24:38  shkim
 * 최초 등록
 *
 *
 * </pre>
 ******************************************************************/

public class MQBroker {
	
	private static Object dummy=new Object();
	private static MQBroker instance;
	private BrokerService broker;
	
	private boolean isStarted = false;
	private MQBorkerMonitor mqBorkerMonitor;
	
	public static MQBroker getInstance() throws Exception{
	     if(instance==null){
	          synchronized (dummy) {
	              if(instance==null) instance = new MQBroker();
	          }
	     }
	     return instance;
	}
	
	//현재 서비스 중인지를 나타냄
	private boolean isRunning = false;
	
	
	private MQBroker() throws Exception{
		broker = new BrokerService();
		broker.setUseJmx(false);
		broker.setPersistent(false);
		broker.getMemoryManager().setLimit(LogProcessorContext.mqMemoryLimit);
			
		Iterator iter = LogProcessorContext.mqBrokerConnectorUriList.iterator();
		if(iter!=null){
			while(iter.hasNext()){
				 broker.addConnector((String)iter.next());
			}
		}
	}
	
	/**
	 * 큐의 현재 메모리 사용량을 리턴한다.(byte)
	 * @return
	 */
	public long getUsage(){
		return broker.getMemoryManager().getUsage();
	}
	
	/**
	 * 전체 큐 메모리 대 현재 메모리 사용량 퍼센트를 리턴한다.(%)
	 * @return
	 */
	public int getPercentUsage(){
		return broker.getMemoryManager().getPercentUsage();
	}
	
	/**
	 * 큐 메모리의 최대치를 리턴한다.(byte)
	 * @return
	 */
	public long getLimit(){
		return broker.getMemoryManager().getLimit();
	}
	
	/**
	 * BrokerService 서비스를 시작시킨다.
	 * @throws Exception
	 */
	public synchronized void start() throws Exception{
		if(!isStarted){
			broker.start();
			isStarted = true;
			isRunning = true;
			startMQBorkerMonitor();
		}else{
			LogManager.info(LogProcessorContext.logCategory, "MQBroker already started.");
		}
		
	}
	
	/**
	 * BrokerService 서비스를 일시적으로 중단한다.
	 * 
	 */
	public synchronized void pause(){
		isRunning = false;
	}
	
	/**
	 * BrokerService 서비스를 재개한다.
	 * 
	 */
	public synchronized void resume(){
		isRunning = true;
	}
	
	/**
	 * BrokerService가 현재 서비스 중인지를 리턴한다.
	 * @return
	 */
	public boolean isRunning(){
		return isRunning;
	}
	
	
	/**
	 * BrokerService 서비스를 종료시킨다.
	 * @throws Exception
	 */
	public synchronized void stop() throws Exception{
		if(isStarted){
			broker.stop();
			stopMQBorkerMonitor();
			isStarted = false;
			isRunning = false;
		}else{
			LogManager.info(LogProcessorContext.logCategory, "MQBroker already stopped.");
		}
	}
	
	private class MQBorkerMonitor implements Runnable{
		
		private boolean exit;
		int threshold = LogProcessorContext.mqMemoryThreshold;
		
		public void run() {
			
			while(!exit && isStarted){
				int percentUsage = getPercentUsage();
				if(percentUsage>=50){
					LogManager.error(LogProcessorContext.logCategory, "LogMQ Memory Monitor:: usage percent[" + percentUsage
							+"%] usage/limit[" + getUsage() +"/" + getLimit() +"(bytes)]");
				}
				
				controlLogStore();
				
				try {
					Thread.sleep(10*1000);
				} catch (InterruptedException e) {}
			}
			LogManager.info(LogProcessorContext.logCategory, "MQBorkerMonitor를 종료합니다.");

		}

		/**
		 * 메모리 사용률에 따라 LogStore를 제어한다.
		 */
		protected void controlLogStore() {
			int percentUsage = getPercentUsage();
			/*LogStore logStore = LogStore.getInstance();
			if(percentUsage>threshold){
				
				if(logStore.isInitialized() && !logStore.isDestroyed() && !logStore.isStop()){
					logStore.stop();
					LogManager.error(LogProcessorContext.logCategory, "LogMQ의 메모리 사용량이 " + threshold +"%를 초과하였으므로 중지합니다.");
				}
			}else{
				
				if(logStore.isInitialized() && !logStore.isDestroyed() && logStore.isStop()){
					logStore.start();
					LogManager.info(LogProcessorContext.logCategory, "LogMQ의 메모리 사용량이  " + threshold +"% 이하이므로 재시작합니다.");
				}
			}*/
			
			if(percentUsage>threshold){
				
				
				if(isRunning()){
					pause();
					LogManager.error(LogProcessorContext.logCategory, "LogMQ의 메모리 사용량이 " + threshold +"%를 초과하였으므로 중지합니다.");
				}
			}else{
				
				if(!isRunning()){
					resume();
					LogManager.info(LogProcessorContext.logCategory, "LogMQ의 메모리 사용량이  " + threshold +"% 이하이므로 재시작합니다.");
				}
			}
			
		}
				
		public void stop(){
			this.exit = true;
		}
	}
	
	/**
	 * MQBorkerMonitor를 시작시킨다.
	 */
	private void startMQBorkerMonitor(){

		try {

			mqBorkerMonitor = new MQBorkerMonitor();
			Thread th = new Thread(mqBorkerMonitor);
			th.start();
			LogManager.info(LogProcessorContext.logCategory, "MQBorkerMonitor를 시작합니다.");
		} catch (Exception e) {
			LogManager.error(LogProcessorContext.logCategory, "MQBorkerMonitor 시작 중 오류 발생", e);

		}
	}
	
	/**
	 * MQBorkerMonitor를 종료시킨다.
	 */
	private void stopMQBorkerMonitor(){
		if(mqBorkerMonitor!=null){
			mqBorkerMonitor.stop();
		}
	}
	
    public static void main(String[] args) throws Exception {
        BrokerService broker = new BrokerService();
		broker.setUseJmx(false);
        broker.addConnector("tcp://localhost:61616?jms.useAsyncSend=true");
        broker.addConnector("vm://localhost?jms.useAsyncSend=true");
        
        broker.start();

        /*Producer producer = new Producer();
    	producer.run();*/
        LogReceiver receiver = new LogReceiver();
        receiver.start();
        
        System.out.println("------------------- EmbeddedBroker started ---------------");
        // now lets wait forever to avoid the JVM terminating immediately
        Object lock = new Object();
        synchronized (lock) {
            lock.wait();
        }
        System.out.println("------------------- EmbeddedBroker end ---------------");
     
    }
}
