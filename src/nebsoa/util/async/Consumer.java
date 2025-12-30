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
import java.util.Iterator;

import nebsoa.common.log.LogManager;

/*******************************************************************
 * <pre>
 * 1.설명
 * Queue에 있는 데이터를 처리하는 Consumer를 구현 할 때 상속 받을 클래스
 *
 * 2.사용법
 * framework에서 queue에 있는 객체를 꺼내와서 handleObject를 호출 하므로,
 * 개발자는 handleObject 메소드를 구현한다.(default로 출력 하는 로직만 구현 되어 있다0
 * <font color="red">
 * 3.주의사항
 *
 * Consumer객체가 만들어 진 후에는 반드시
 * consumer.setConfig(config);
 * consumer.setQueue(queue);
 * consumer.setIndex(index);
 * 메소드를 호출 하여 실행 가능한 환경으로 만들어 주어야 한다.
 * </font>
 * @author 이종원
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 *
 * $Log: Consumer.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:25  cvs
 * neo cvs init
 *
 * Revision 1.2  2013/02/20 11:59:43  sapiamond
 * yshong
 *
 * Revision 1.4  2013/02/12 05:04:34  jglee
 *

 *
 * </pre>
 ******************************************************************/
public class Consumer implements Runnable, Cloneable {

	private QueueConfig config;
	private BlockingQueue queue;
	private int index;

	public Consumer(){ }
/**
	public Consumer(QueueConfig config, BlockingQueue queue,int index) {
		this.config = config;
      	this.queue = queue;
      	this.index = index;
	}
**/

	public void run() {
		if(config.isBulk()){ //대량 처리
			LogManager.info("Bulk Consumer started.."+this);
			doBulkConsume();
		}else{ //단건 처리
			LogManager.info("Single Consumer started.."+this);
			doSimpleConsume();
		}
		LogManager.info("Stop Consumer "+this);
	}





	private void doSimpleConsume() {
		while (queue.isRunning()){
			Object obj = null;
			try {
				obj = queue.get(config.getWaitTimeout());
			} catch (AsyncQueueTimeoutException e) {
				LogManager.info(">> AsyncQueueTimeoutException.. at "+this); //무시한다.
			} catch (AsyncQueueException e) {
				LogManager.error(">> InterruptedException .. at "+this,e);
			}
			if(obj != null) {
				if(obj==BlockingQueue.NO_MORE_WORK) {
					LogManager.info("break loop "+this);
					break;
				}
				try{
					handleObject(obj);
				}catch(Exception e){
					LogManager.error(this+"에서 처리 되지 않은 에러 발생:"+e.toString(), e);
				}catch(Throwable e){
					LogManager.error(this+"에서 처리 되지 않은 Throwable 발생하여 강제 종료:"+e.toString(), e);
					break;
				}
			}else{
				//로그가 너무많이 남아서 주석으로 막음 2009-01-06 yshong
				//if(queue.isRunning()) LogManager.debug("큐로부터 얻은 객체가 없습니다. ....."+this);
			}
			try {
				Thread.sleep(config.getConsumerSleepTime());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}



	private void doBulkConsume() {
		while (queue.isRunning()){
			ArrayList obj = null;
			try {
				obj = queue.getAsArray(config.getWaitTimeout());
			} catch (AsyncQueueTimeoutException e) {
				LogManager.info(">> AsyncQueueTimeoutException.. at "+this); //무시한다.
			} catch (AsyncQueueException e) {
				LogManager.error(">> InterruptedException .. at "+this,e);
			}
			if(obj != null) {
				if(obj==BlockingQueue.NO_MORE_WORK) {
					LogManager.info("break loop "+this);
					queue.setRunning(false);
					break;
				}

				if(obj.size()==0) continue;

				Iterator i = obj.iterator();

				while(i.hasNext()){
					try{
						if(i.next()==BlockingQueue.NO_MORE_WORK) {
							queue.setRunning(false);
							LogManager.info(this+"에서 종료 시그날을 만났습니다.");
							continue;
						}else{
							try{
								handleObject(i.next());
							}catch(Exception e){
								LogManager.error(this+"에서 처리 되지 않은 에러 발생:"+e.toString(), e);
							}catch(Throwable e){
								LogManager.error(this+"에서 처리 되지 않은 Throwable 발생하여 강제 종료:"+e.toString(), e);
								queue.setRunning(false);
								break;
							}
						}
					}catch(Exception e){}
				}
			}
			try {
				Thread.sleep(config.getConsumerSleepTime());
			} catch (InterruptedException e) {

			}
		}

	}
	/**
	 * 상속 받아 구현 할 메소드 입니다.
	 * default로 해당 객체를 출력하도록 구현 되어 있습니다.
	 * @param obj
	 */
	public void handleObject(Object obj) {
		LogManager.debug(this+" Got Object : "+obj);
	}

	public String toString(){
		return "<<Queue:"+queue+",Consumer:"+index+">>";
	}


	public QueueConfig getConfig() {
		return config;
	}


	public void setConfig(QueueConfig config) {
		this.config = config;
	}


	public BlockingQueue getQueue() {
		return queue;
	}


	public void setQueue(BlockingQueue queue) {
		this.queue = queue;
	}


	public int getIndex() {
		return index;
	}


	public void setIndex(int index) {
		this.index = index;
	}

	public Object clone() throws CloneNotSupportedException{
		return super.clone();
	}

}