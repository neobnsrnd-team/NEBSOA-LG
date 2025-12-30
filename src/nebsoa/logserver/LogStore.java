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
/*******************************************************************
 * <pre>
 * 1.설명 
 * 로그 데이터를 설정된 갯수만큼 수집하여 LogSender로 전송하는 클래스. 
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
 * $Log: LogStore.java,v $
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
 * Revision 1.3  2008/01/17 09:40:16  김승희
 * 로그 서버 장애 시 장애 상태 체크 looping 로직에 interval 추가
 *
 * Revision 1.14  2008/01/17 09:33:43  shkim
 * 로그 서버 장애 시 장애 상태 체크 looping 로직에 interval 추가
 *
 * Revision 1.13  2007/12/06 04:53:23  shkim
 * blocking queue 모두 중지 상태의 경우 에러 메시지 출력 시간 term 추가
 *
 * Revision 1.12  2007/10/31 07:00:54  jwlee
 * *** empty log message ***
 *
 * Revision 1.11  2007/10/22 13:27:59  jwlee
 * LOCK을 잡기 위한 경합을 줄이는 로직 추가
 *
 * Revision 1.10  2007/09/06 01:25:15  김승희
 * *** empty log message ***
 *
 * Revision 1.9  2007/09/03 15:36:21  shkim
 * *** empty log message ***
 *
 * Revision 1.8  2007/09/03 11:30:42  김승희
 * *** empty log message ***
 *
 * Revision 1.7  2007/08/31 05:29:33  김승희
 * *** empty log message ***
 *
 * Revision 1.6  2007/08/30 16:50:05  shkim
 * *** empty log message ***
 *
 * Revision 1.5  2007/08/17 11:09:00  김승희
 * *** empty log message ***
 *
 * Revision 1.4  2007/08/16 07:23:19  김승희
 * 버그 수정
 *
 * Revision 1.3  2007/08/16 07:09:45  김승희
 * 성능 향상을 위한 수정
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

public class LogStore {
	
    
    private boolean isStop = true;
    private boolean isDestroyed = false;
    private boolean isInitialized = false;
    
    private static Object dummy=new Object();
    private static LogStore instance;
    private LogBlockingQueue[] queue;
    
    private long lastErrorAlertTime = System.currentTimeMillis();
    
    public static LogStore getInstance(){
        if(instance==null){
            synchronized (dummy) {
                if(instance==null) instance = new LogStore();
            }
        }
        return instance;
    }
    
    /** 
     * Constructor. 
     */
    private LogStore() {
    	init();
    }
    
    protected void init(){
    	queue = new LogBlockingQueue[LogProcessorContext.logSendWorkerCount];
    	for(int i=0; i<queue.length; i++){
    		queue[i] = new LogBlockingQueue(LogProcessorContext.blockingQueueSize, i);
    	}
    	LogManager.info(LogProcessorContext.logCategory, "LogBlockingQueue를 " + queue.length +"개 생성하였습니다..");
    }
    
    private int nextQueueIndex = -1;
    
    /**
     * 다음 queue를 리턴한다.
     * @return
     */
    public synchronized LogBlockingQueue getNextQueue(){
    	
    	nextQueueIndex++;
    	
    	if(nextQueueIndex>=queue.length){
    	   nextQueueIndex = 0;
    	}
    	
    	return getQueue(nextQueueIndex);
    	
    }
    
    /**
     * 운영중인 다음 queue를 리턴한다.
     * @return
     */
    public LogBlockingQueue getNextRunningQueue(){
    	
    	for(int i=0; i<queue.length; i++){
    		LogBlockingQueue q = getNextQueue();
    		if(!q.isRunning()) continue;
    		else return q;
    	}
    	return null;
    }
    /**
     * 해당 인덱스의 BlockingQueue를 리턴한다.
     * @param index
     * @return
     */
    public LogBlockingQueue getQueue(int index){
    	return queue[index];
    }
    
    public void put(Object obj) throws InterruptedException {
        //LogStore 가 중지 상태가 아닌 경우에만..
    	if(!isStop){
    		long startTime = System.currentTimeMillis();
    		LogBlockingQueue q = null; boolean isSuccess = false;
	        try{
	        	if((q=getNextRunningQueue())!=null){
	        		//성능 이슈를 위하여  0.15초만 기다리게 하였다.
	        		q.put(obj,150);
	        		isSuccess = true;

	        	}else{
	        		 //모든 큐가 중지일 때 30초마다 에러 메시지를 남긴다.
	        		 if((System.currentTimeMillis()-lastErrorAlertTime)>30*1000){
	        			 LogManager.error(LogProcessorContext.logCategory, "LogStore:: 현재 모든 BlockingQueue가 중지상태입니다.");
	        		 	 lastErrorAlertTime = System.currentTimeMillis();
	        		 }
	        	}

	        }catch(InterruptedException e){
	        	LogManager.error(LogProcessorContext.logCategory, "LogStore:: put log into BlockingQueue failed.." + e);
	        }catch(Throwable e){
	            LogManager.error(LogProcessorContext.logCategory, "LogStore:: put log into BlockingQueue failed.." + e);
	            		
	        }finally{
	        	if(isSuccess){
	        		long endTime = System.currentTimeMillis();
	        		if(endTime-startTime>=300){LogManager.debug(LogProcessorContext.logCategory, Thread.currentThread().getName()
	        				 + ":LogStore put time:[queueIndex:" + q.getUid() +"]"+ (endTime-startTime));
	        		}
	        		else if(endTime-startTime>=800){
	        			LogManager.info(LogProcessorContext.logCategory, Thread.currentThread().getName() 
	        				 + ":LogStore put time:[queueIndex:" + q.getUid() +"]"+ (endTime-startTime));
	        		}
	        	}
	        }
	
	    }

    } 
    public int size(){
    	return getNextQueue().size();
    }
    public int size(int queueIndex){
    	return getQueue(queueIndex).size();
    }
    public Object get() throws InterruptedException{
    	return getNextQueue().get();
    }
    
    public Object get(int queueIndex, int timeout) throws InterruptedException{
    	return getQueue(queueIndex).get(timeout);
    }
    
    public Object get(int queueIndex) throws InterruptedException{
    	return getQueue(queueIndex).get();
    }
    /**
     * LogStore를 중지시킨다.
     * 
     */
    public synchronized void stop(){
    	this.isStop = true;
    }
    
    public boolean isStop() {
		return isStop;
	}

	public void setStop(boolean isStop) {
		this.isStop = isStop;
		LogManager.info(LogProcessorContext.logCategory, "LogStore 중지 상태:" + isStop +"으로 변경..");
	}

	/**
     * LogStore를 시작시킨다.
     * 
     */
    public synchronized void start(){
    	if(isDestroyed) throw new IllegalStateException("LogStore was destroyed, so cannot start.");
    	this.isStop = false;
    	 
        //LogSender 생성
        //if(logSender==null) logSender = LogSender.getInstance(this);
    }
    
    /**
     * LogStore를 종료시킨다.
     * 
     */
    public void destory(){
    	//logSender.destroy();
    	stop();
    	isDestroyed = true;
    }

	public boolean isDestroyed() {
		return isDestroyed;
	}

	public void setDestroyed(boolean isDestroyed) {
		this.isDestroyed = isDestroyed;
	}

	public boolean isInitialized() {
		return isInitialized;
	}

	public void setInitialized(boolean isInitialized) {
		this.isInitialized = isInitialized;
	}
    
}