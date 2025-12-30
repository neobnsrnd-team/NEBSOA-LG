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

import nebsoa.common.log.LogManager;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 실시간 로그 Queue
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
 * $Log: LogBlockingQueue.java,v $
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
 * Revision 1.7  2007/12/08 02:28:22  jwlee
 * synchronized block 수정
 *
 * Revision 1.6  2007/10/31 07:02:11  jwlee
 * 한번에 1000 개까지 가져 오도록 수정
 *
 * Revision 1.5  2007/10/26 08:41:44  안경아
 * *** empty log message ***
 *
 * Revision 1.4  2007/10/22 13:27:59  jwlee
 * LOCK을 잡기 위한 경합을 줄이는 로직 추가
 *
 * Revision 1.3  2007/08/31 05:29:33  김승희
 * *** empty log message ***
 *
 * Revision 1.2  2007/08/21 03:59:58  김승희
 * 주석 정리
 *
 * Revision 1.1  2007/08/16 07:10:04  김승희
 * 최초 등록
 *
 *
 * </pre>
 ******************************************************************/
public class LogBlockingQueue {
    Object[] elements;
    Object lock;
    int maxSize;
    int size = 0;
    int head = 0;
    int tail = 0;
    
    boolean running = true;
    
    int uid = -1;
    
    /** Constructor. 
     */
    public LogBlockingQueue(int maxSize) {
        this(null, maxSize);
    }
    
    public LogBlockingQueue(int maxSize, int uid) {
        this(null, maxSize);
        this.uid = uid;
    }
    
    public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	/** Constructor. 
     */
    public LogBlockingQueue(Object lock, int maxSize) {
        this.maxSize = maxSize;
        if (maxSize == 0)
            this.maxSize = 255;
        elements = new Object[this.maxSize];
        this.lock = lock == null ? elements : lock;
    }

    public void clear() {
        synchronized (lock) {
            size = 0;
            head = 0;
            tail = 0;
        }
    }

    public int size() {
        return size;
    }

    public int maxSize() {
        return maxSize;
    }
    
    public boolean isFull(){
    	return (size > maxSize-2);
    }
    /** Put object in queue.
     * @param o Object
     */
    public void put(Object o) throws InterruptedException {
        synchronized (lock) {
        	while (size == maxSize)
                lock.wait();
            elements[tail] = o;
            if (++tail == maxSize)
                tail = 0;
            size++;
            lock.notify();
        }
    }

    /** Put object in queue.
     * @param timeout If timeout expires, throw InterruptedException
     * @param o Object
     * @exception InterruptedException Timeout expired or otherwise interrupted
     */
    public void put(Object o, int timeout) throws InterruptedException {
        synchronized (lock) {
            if (size == maxSize) {
                lock.wait(timeout);
                if (size == maxSize){
                	try{
                		throw new InterruptedException("Put Log to Blocking Queue Timed out");
                	}finally{
                		lock.notify();
                	}
                }
            }

            elements[tail] = o;
            if (++tail == maxSize)
                tail = 0;
            size++;
            lock.notify();
        }
    }

    /** Get object from queue.
     * Block if there are no objects to get.
     * @return The next object in the queue.
     */
    public Object get() throws InterruptedException {
        synchronized (lock) {
            while (size == 0 && running)
                lock.wait();

            Object o = elements[head];
            elements[head] = null;
            if (++head == maxSize)
                head = 0;
            synchronized (lock) {
	            size--;
            	if (size == maxSize) { //꽉차서 기다리는 녀석들이 있을 테니...
            		lock.notifyAll();
            	}
            }
            return o;
        }    
    }

    /** Get from queue.
     * Block for timeout if there are no objects to get.
     * @param timeoutMs the time to wait for a job
     * @return The next object in the queue, or null if timedout.
     */
    public Object get(int timeoutMs) throws InterruptedException {
        synchronized (lock) {
            if (size == 0 && timeoutMs != 0)
                lock.wait((long) timeoutMs);
            	wait(timeoutMs);

            if (size == 0)
                return null;

            Object o = elements[head];
            elements[head] = null;
            if (++head == maxSize)
                head = 0;
            synchronized (lock) {
            	if (size == maxSize) { //꽉차서 기다리는 녀석들이 있을 테니...
            		lock.notifyAll();
            	}
	            size--;
            }

            return o;
        }
    }
    
    /** copy to arraylist  from queue.
     * 성능 향상을 위하여 추가 작성.
     * Block for timeout if there are no objects to get.
     * @param timeoutMs the time to wait for a job
     * @return The next object in the queue, or null if timedout.
     */
    public void copyToArray(ArrayList list, int timeoutMs) throws InterruptedException {
    	//LogManager.debug("LOG_SVR", "##### copy log to Array from queue ...");
//            if (size == 0 && timeoutMs != 0)
//                lock.wait((long) timeoutMs);

        if (size == 0)
            return ;
        
        if(list==null) return;
        int size2=size; //안전을 위하여 size값을 복제하여 둔다.
        if(size2>1000)size2=1000; //최대 1000개 까지만 작업
        //LogManager.debug("LOG_SVR","####### current Size:"+size2);
        for(int i=0;i<size2;i++){
        	list.add(elements[head]);
        	elements[head] = null;
        	if (++head == maxSize)
            head = 0;
        }
        synchronized (lock) {            
        	if (size == maxSize) { //꽉차서 기다리는 녀석들이 있을 테니...
        		lock.notifyAll();
        	}
        	size = size-size2;
            
        }
    	if((size*100)/maxSize>50){
    		LogManager.info("LOG_SVR","####### clear after 줄어든  Size:"+size);
    	}else{
    		LogManager.debug("LOG_SVR","####### clear after 줄어든  Size:"+size);
    	}
        
    }

    /** Peek at the  queue.
     * Block  if there are no objects to peek.
     * @return The next object in the queue, or null if timedout.
     */
    public Object peek() throws InterruptedException {
        synchronized (lock) {
            if (size == 0)
                lock.wait();

            if (size == 0)
                return null;

            Object o = elements[head];
            return o;
        }
    }

    /** Peek at the  queue.
     * Block for timeout if there are no objects to peek.
     * @param timeoutMs the time to wait for a job
     * @return The next object in the queue, or null if timedout.
     */
    public Object peek(int timeoutMs) throws InterruptedException {
        synchronized (lock) {
            if (size == 0)
                lock.wait((long) timeoutMs);

            if (size == 0)
                return null;

            Object o = elements[head];
            return o;
        }
    }

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
	
	/**
	 * LogBlockingQueue에 wait하고 있는 모든 쓰레드를 notifyAll 한다.
	 */
	public void freeAll(){
		LogManager.debug(LogProcessorContext.logCategory, "LogBlockingQueue freeAll call start..");
		synchronized (lock) {
			running = false;
			lock.notifyAll();
		}
		LogManager.debug(LogProcessorContext.logCategory, "LogBlockingQueue freeAll call end..");
	}
}
