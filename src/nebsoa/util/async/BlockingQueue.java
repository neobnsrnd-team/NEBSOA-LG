/*
 * Spider Framework
 *
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 *
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.util.async;

import java.util.ArrayList;

import nebsoa.common.log.LogManager;

import org.apache.commons.collections.FastArrayList;

/*******************************************************************
 * <pre>
 * 1.설명
 * Producer & consumer pattern 구현을 위한 Blocking Queue
 *
 * 2.사용법
 *
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author 이종원
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 *
 * $Log: BlockingQueue.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:24  cvs
 * neo cvs init
 *
 * Revision 1.2  2013/02/20 11:59:43  sapiamond
 * yshong
 *
 *
 * </pre>
 ******************************************************************/
public class BlockingQueue {
	static Object NO_MORE_WORK = new FastArrayList();
    Object[] elements;
    Object lock;
    int maxSize;
    int size = 0;
    int head = 0;
    int tail = 0;

    int uid = -1;

    QueueConfig config;

    /** Constructor.
     */
    public BlockingQueue(int maxSize, QueueConfig config) {
        this(maxSize,0, config);
    }

    public BlockingQueue(int maxSize, int uid, QueueConfig config) {
        this(null, maxSize, config);
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
    public BlockingQueue(Object lock, int maxSize,QueueConfig config) {
    	this.config = config;
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
    /**
     * queue에 넣는다. queue가 full인 경우 계속 기다린다.
     * @param o Object
     * @throws AsyncQueueTimeoutException
     */
    public void put(Object o) throws AsyncQueueException, AsyncQueueTimeoutException {
    	try{
	        synchronized (lock) {
	        	if(!config.isRunning()){
	        		freeAll();
	        		return;
	        	}
	        	int i=0;
	        	while (size == maxSize){
	                lock.wait(config.getProducerWaitTimeout());
	                if(i++ > 10){
	                	LogManager.info("MONITOR",this+"에 넣기 위해 ["+i+"]번째 시도 중입니다.!!!");
	                }
	            	if(!config.isRunning()){
	            		freeAll();
	            		return;
	            	}
	        	}
	            elements[tail] = o;
	            if (++tail == maxSize)
	                tail = 0;
	            size++;
	            lock.notify();
	        }
		}catch(InterruptedException e){
			throw new AsyncQueueTimeoutException(e);
		}catch(Exception e){
			throw new AsyncQueueException("FRS99003","Fail to Put to Queue::"+e.toString(), e);
		}
    }

    /**
     * queue에 넣는다. queue가 full인 경우 지정된 시간 만큼만 기다린다.
     * 지정된 시간이 지나도 queue가 여유가 안생기면 InterruptedException이 발생한다.
     * @param timeout If timeout expires, throw InterruptedException
     * @param o Object
     * @throws AsyncQueueTimeoutException
     * @exception InterruptedException Timeout expired or otherwise interrupted
     */
    public void put(Object o, long timeout) throws  AsyncQueueException, AsyncQueueTimeoutException{
    	try{
	        synchronized (lock) {
	        	if(!config.isRunning()){
	        		freeAll();
	        		return ;
	        	}
	            if (size == maxSize) {
	                lock.wait(timeout);
	                //한번 더 시도한다.
	                if (size == maxSize){
	                	try{
	                		throw new InterruptedException(">>>> Fail to Put Data("+o+") to Queue Timed out::"+this);
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
		}catch(InterruptedException e){
			throw new AsyncQueueTimeoutException(e);
    	}catch(Exception e){
    		throw new AsyncQueueException("FRS99003","Fail to Put to Queue::"+e.toString(), e);
    	}
    }

    /** Get object from queue.
     * Block if there are no objects to get.
     * @return The next object in the queue.
     * @throws AsyncQueueException
     * @throws AsyncQueueTimeoutException
     */
    public Object get() throws AsyncQueueException, AsyncQueueTimeoutException {
    	try{
	        synchronized (lock) {
	        	if(!config.isRunning()){
	        		freeAll();
	        		return NO_MORE_WORK;
	        	}
	            while (size == 0 )
	                lock.wait();

	            Object o = elements[head];
	            elements[head] = null;
	            if (++head == maxSize) head = 0;
	//        	if (size == maxSize) { //꽉차서 기다리는 녀석들이 있을 테니...
	        		lock.notifyAll();
	//        	}
	            size--;
	            return o;
	        }
		}catch(InterruptedException e){
			throw new AsyncQueueTimeoutException(e);
    	}catch(Exception e){
    		throw new AsyncQueueException("FRS99002","Fail to Get From Queue::"+e.toString(), e);
    	}
    }

    /** Get from queue.
     * Block for timeout if there are no objects to get.
     * @param timeoutMs the time to wait for a job
     * @return The next object in the queue, or null if timedout.
     * @throws AsyncQueueTimeoutException
     */
    public Object get(long timeoutMs) throws AsyncQueueException, AsyncQueueTimeoutException {
    	try{
	    	if(timeoutMs <= 0) timeoutMs=1000;
	        synchronized (lock) {
	        	if(!config.isRunning()){
	        		freeAll();
	        		return NO_MORE_WORK;
	        	}
	            if (size == 0 )
	                lock.wait((long) timeoutMs);

	            if (size == 0)
	                return null;

	            Object o = elements[head];
	            elements[head] = null;
	            if (++head == maxSize)  head = 0;
	//        	if (size == maxSize) { //꽉차서 기다리는 녀석들이 있을 테니...
	        		lock.notifyAll();
	//        	}
	            size--;
	            return o;
	        }
		}catch(InterruptedException e){
			throw new AsyncQueueTimeoutException(e);
    	}catch(Exception e){
    		throw new AsyncQueueException("FRS99002","Fail to Get From Queue::"+e.toString(), e);
    	}
    }

    /** copy to arraylist  from queue.
     * 성능 향상을 위하여 추가 작성.
     * Block for timeout if there are no objects to get.
     * @param timeoutMs the time to wait for a job
     * @return The next object in the queue, or null if timedout.
     */
    public ArrayList getAsArrayWithoutLock( long timeoutMs) throws AsyncQueueException {
    	ArrayList list = new ArrayList();
    	try{
	    	if(!config.isRunning()){
	    		//freeAll();
	    		return (ArrayList) NO_MORE_WORK;
	    	}

	    	if (size == 0) {
	    		synchronized (lock) {
	    			lock.wait((long) timeoutMs);
	    		}
	    	}

	        if (size == 0)return list;

	        int size2=size; //안전을 위하여 size값을 복제하여 둔다.
	        if(size2>1000)size2=1000; //최대 1000개 까지만 작업
	        //LogManager.debug("####### current Size:"+size2);
	        for(int i=0;i<size2;i++){
	        	list.add(elements[head]);
	        	elements[head] = null;
	        	if (++head == maxSize)
	            head = 0;
	        }
	        synchronized (lock) {
	        	//if (size == maxSize) { //꽉차서 기다리는 녀석들이 있을 테니...
	        		lock.notifyAll();
	        	//}
	        	size = size-size2;
	        }
	        return list;
		}catch(Exception e){
    		throw new AsyncQueueException("FRS99002","Fail to Get From Queue::"+e.toString(), e);
    	}
    }

    /** copy to arraylist  from queue with Lock( synchronized )
     * 성능 향상을 위하여 추가 작성.
     * Block for timeout if there are no objects to get.
     * @param timeoutMs the time to wait for a job
     * @return The next object in the queue, or null if timedout.
     * @throws AsyncQueueTimeoutException
     */
    public ArrayList getAsArray( long timeoutMs) throws AsyncQueueException, AsyncQueueTimeoutException {
    	//LogManager.debug( "##### copy log to Array from queue with lock ...");
    	try{
	    	ArrayList list = new ArrayList();
	    	synchronized (lock) {

		    	if(!config.isRunning()){
		    		freeAll();
		    		return (ArrayList) NO_MORE_WORK;
		    	}

		    	if(timeoutMs <= 0) timeoutMs=1000;
		        if (size == 0) lock.wait((long) timeoutMs);

		        if (size == 0) return list;

		        int size2=size; //안전을 위하여 size값을 복제하여 둔다.

		        if(size2>1000)size2=1000; //최대 1000개 까지만 작업
		        //LogManager.debug("####### current Size:"+size2);
		        for(int i=0;i<size2;i++){
		        	list.add(elements[head]);
		        	elements[head] = null;
		        	if (++head == maxSize)
		            head = 0;
		        }
	        	//if (size == maxSize) { //꽉차서 기다리는 녀석들이 있을 테니...
	        		lock.notifyAll();
	        	//}
	        	size = size-size2;

	        }
	        return list;
		}catch(InterruptedException e){
			throw new AsyncQueueTimeoutException(e);
    	}catch(Exception e){
    		throw new AsyncQueueException("FRS99002","Fail to Get From Queue::"+e.toString(), e);
    	}
    }
    /** Peek at the  queue.
     * Block  if there are no objects to peek.
     * @return The next object in the queue, or null if timedout.
     * @throws AsyncQueueTimeoutException
     */
    public Object peek() throws AsyncQueueException, AsyncQueueTimeoutException {
    	try{
	        synchronized (lock) {
	        	if(!config.isRunning()){
	        		freeAll();
	        		return NO_MORE_WORK;
	        	}
	            while (size == 0)
	                lock.wait();

	            Object o = elements[head];
	            return o;
	        }
		}catch(InterruptedException e){
			throw new AsyncQueueTimeoutException(e);
    	}catch(Exception e){
    		throw new AsyncQueueException("FRS99002","Fail to Peek From Queue::"+e.toString(), e);
    	}
    }

    /** Peek at the  queue.
     * Block for timeout if there are no objects to peek.
     * @param timeoutMs the time to wait for a job
     * @return The next object in the queue, or null if timedout.
     * @throws AsyncQueueTimeoutException
     */
    public Object peek(long timeoutMs) throws AsyncQueueException, AsyncQueueTimeoutException {
        try{
        	synchronized (lock) {

	        	if(!config.isRunning()){
	        		freeAll();
	        		return NO_MORE_WORK;
	        	}
	        	if(timeoutMs <= 0) timeoutMs=1000;
	            if (size == 0)
	                lock.wait(timeoutMs);

	            if (size == 0)
	                return null;

	            Object o = elements[head];
	            return o;
	        }
		}catch(InterruptedException e){
			throw new AsyncQueueTimeoutException(e);
        }catch(Exception e){
    		throw new AsyncQueueException("FRS99002","Fail to Peek From Queue::"+e.toString(), e);
    	}
    }

	public boolean isRunning() {
		return config.isRunning();
	}

	public void setRunning(boolean running) {
		this.config.setRunning(running) ;
	}

	/**
	 * LogBlockingQueue에 wait하고 있는 모든 쓰레드를 notifyAll 한다.
	 */
	public void freeAll(){
		LogManager.debug( "Wakeup Producer & Consumer thread ----- start..");
		synchronized (lock) {
			setRunning(false);
			lock.notifyAll();
		}
		LogManager.debug("free All Producer & Consumer thread ----- end..");
	}

	public String toString(){
		return config.toString()+", Blocking Queue Index:"+uid;
	}
}
