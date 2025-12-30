/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.util.async;

import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;


/*******************************************************************
 * <pre>
 * 1.설명 
 * Queue와 관련 된 Producer, Consumer등의 config data 
 * 
 * 2.사용법
 * QueueConfig객체의생성자 참조
 * QueueConfig객체 생성시 가장 중요한 것은 Consumer를 어떻게 생성 할 것인가 이다.
 * 방법은 
 * 1. factory를 사용하는 방법 
 * 2.clone 메소드를 구현한 consumer를 하나 넣어 주는 방법과
 * 3.Consumer class name을 넣어 주어 Class.forName으로 생성하는 방법이 있다.
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author 이종원
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 
 * $Log: QueueConfig.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:24  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:26  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:29  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:54  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/02/26 01:40:53  이종원
 * 최초작성
 *
 * </pre>
 ******************************************************************/
public class QueueConfig {

	
	private String name;
	/**
	* flag of thread controll
    */
	private Flag flag;
	/**
	 * Count of Queue in this Config env
	 */
	private int queueCount;
	/**
	 * Buffer Size of Queue 
	 */	
	private int queueSize;
	/**
	 * producer wait timeout.
	 * 이 변수는 생성자를 통해 값을 세팅한다. 
	 * producer가 queue.put(obj); 를 호출 할 때 wait하는 시간 지정 값이다.
	 * 하지만 개발자가 개발한 producer가 queue.put(Obj, waitTimeout);
	 * 메소드를 통해 timeout값을 임의로 지정 할 수 있다.
	 */
	private long producerWaitTimeout;
	/**
	 * waitTimeout when put and get 
	 */	
	private long consumerWaitTimeout;
	/**
	 * consumer Sleep Time until next get 
	 */	
	private long consumerSleepTime;
	
	private int nextQueueIndex;
	
	private BlockingQueue[] queueArray;
	
	private boolean bulk; //큐에 쌓인 것을 다건씩 처리 할 것인지 여부
	
	public QueueConfig(int queueCount, int queueSize,long producerWaitTimeout,
			long consumerWaitTimeout, 
			long sleepTime, ConsumerFactory factory ,boolean bulk ){
		this.flag = new Flag();
		queueArray =  new BlockingQueue[queueCount];

		this.queueCount = queueCount;
		this.queueSize = queueSize;
		this.producerWaitTimeout = producerWaitTimeout;
		this.consumerWaitTimeout = consumerWaitTimeout;
		this.consumerSleepTime = sleepTime;
		this.bulk = bulk;	
		
		for(int index=0;index<queueCount;index++){
			queueArray[index] = new BlockingQueue(queueSize, index, this);
			Consumer consumer = factory.makeObject();
			consumer.setConfig(this);
			consumer.setQueue(queueArray[index]);
			consumer.setIndex(index);
			new java.lang.Thread(consumer).start();
		}
	}
	
	public QueueConfig(int queueCount, int queueSize, long producerWaitTimeout,
			long consumerWaitTimeout, 
			long sleepTime, Consumer consumer ,boolean bulk ) throws CloneNotSupportedException{
		this.flag = new Flag();
		queueArray =  new BlockingQueue[queueCount];

		this.queueCount = queueCount;
		this.queueSize = queueSize;
		this.producerWaitTimeout = producerWaitTimeout;
		this.consumerWaitTimeout = consumerWaitTimeout;
		this.consumerSleepTime = sleepTime;
		this.bulk = bulk;

		if(!(consumer instanceof Cloneable)){
			SysException e = new SysException("FRS99999",
					"Consumer가 Cloneable해야 합니다");
			LogManager.error(e.toString(),e);
		}
		
		for(int index=0;index<queueCount;index++){
			queueArray[index] = new BlockingQueue(queueSize, index, this);
			Consumer consumer2 = (Consumer) consumer.clone();
			consumer2.setConfig(this);
			consumer2.setQueue(queueArray[index]);
			consumer2.setIndex(index);
			new java.lang.Thread(consumer).start();
		}
	}
	
	public QueueConfig(int queueCount, int queueSize, long producerWaitTimeout,
			long consumerWaitTimeout, 
			long sleepTime, String consumerName,boolean bulk  ) {
		this.flag = new Flag();
		queueArray =  new BlockingQueue[queueCount];

		this.queueCount = queueCount;
		this.queueSize = queueSize;
		this.producerWaitTimeout = producerWaitTimeout;
		this.consumerWaitTimeout = consumerWaitTimeout;
		this.consumerSleepTime = sleepTime;
		this.bulk = bulk;	
		
		for(int index=0;index<queueCount;index++){
			queueArray[index] = new BlockingQueue(queueSize, index, this);
			Consumer consumer = (Consumer) forName(consumerName);
			consumer.setConfig(this);
			consumer.setQueue(queueArray[index]);
			consumer.setIndex(index);
			new java.lang.Thread(consumer).start();
		}
	}
	
	
	
    private Object forName(String className){
        try {
            return  Class.forName(className).newInstance();
        } catch (InstantiationException e1) {
           throw new SysException("[CANN'T MAKE INSTANCE 생성자 체크 :"+className+"]");
        } catch (IllegalAccessException e1) {
           throw new SysException("[CANN'T MAKE INSTANCE 생성자 PUBLIC 인지 체크 :"+className+"]");
        }catch(ClassNotFoundException e){
           throw new SysException("[CLASS_NOT_FOUND:"+e.getMessage()+"]");
        }
    }


	public boolean isRunning() {
		return flag.isRunning();
	}

	public synchronized BlockingQueue getNextQueue() throws AsyncQueueException{
		if(++nextQueueIndex ==queueCount) nextQueueIndex=0;
		if(!isRunning()) throw new AsyncQueueException("Queue state is not Running:"+getName());
		LogManager.debug("return next Queue index:"+nextQueueIndex);
		return queueArray[nextQueueIndex];
	}

	public void setRunning(boolean running) {
		if(flag.isRunning() == running) return;
		flag.setRunning(running);
		if(!running){
			for(int i=0;i<queueCount;i++){
				try {
					queueArray[i].freeAll();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}


	public int getQueueCount() {
		return queueCount;
	}


	public void setQueueCount(int queueCount) {
		this.queueCount = queueCount;
	}


	public int getQueueSize() {
		return queueSize;
	}


	public void setQueueSize(int queueSize) {
		this.queueSize = queueSize;
	}


	public long getWaitTimeout() {
		return consumerWaitTimeout;
	}


	public void setWaitTimeout(long waitTimeout) {
		this.consumerWaitTimeout = waitTimeout;
	}


	public long getConsumerSleepTime() {
		return consumerSleepTime;
	}


	public void setConsumerSleepTime(long consumerSleepTime) {
		this.consumerSleepTime = consumerSleepTime;
	}

	public Flag getFlag() {
		return flag;
	}

	public void setFlag(Flag flag) {
		this.flag = flag;
	}

	public boolean isBulk() {
		return bulk;
	}

	public void setBulk(boolean bulk) {
		this.bulk = bulk;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String toString(){
		return "AsyncQueueConfig[name:"+name+",Q-Count:"+queueCount+",Q-Size:"+queueSize
			+",waitTimeout:"+consumerWaitTimeout+",consumerSleepTime:"+consumerSleepTime;
	}

	public long getProducerWaitTimeout() {
		return producerWaitTimeout;
	}

	public void setProducerWaitTimeout(long producerWaitTimeout) {
		this.producerWaitTimeout = producerWaitTimeout;
	}

	public long getConsumerWaitTimeout() {
		return consumerWaitTimeout;
	}

	public void setConsumerWaitTimeout(long consumerWaitTimeout) {
		this.consumerWaitTimeout = consumerWaitTimeout;
	}

	public BlockingQueue[] getQueueArray() {
		return queueArray;
	}

	public void setQueueArray(BlockingQueue[] queueArray) {
		this.queueArray = queueArray;
	}


}