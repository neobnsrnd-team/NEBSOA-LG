/*
 * Copyright 2004-2005 the original author.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package test.util.async;

import nebsoa.common.log.LogManager;
import nebsoa.util.async.*;

/**
 * @author 이종원
 * Tester class for AsyncQueueProducerTimeoutTest
 */
public class AsyncQueueProducerTimeoutTest {

	
	public static void main(String[] args) {
		LogManager.debug("\n\n********* Starting Test ************************");

		QueueConfig config = AsyncQueueManager.getInstance().getQueueConfig("test"); 
		if(config == null) {
			int queueCount=1;  //100 
			int queueSize=1;   //100 으로 하면 병목이 없이 잘 수행 된다.
			long procuderWaitTimeout=100;
			long consumerWaitTimeout=1000;
			long consumerSleepTime=10;
			config = AsyncQueueManager.getInstance().
      		makeAsyncQueue("test", queueCount,queueSize,procuderWaitTimeout,
      				consumerWaitTimeout,consumerSleepTime,"nebsoa.util.async.Consumer",false);
		}
		
		for(int i=0;i<100;i++){
			Thread t2 = new TestProducer2();
			t2.start();
			Thread t3 = new TestProducer3();
			t3.start();
		}
		try {
			Thread.sleep(15*1000);
		} catch (Exception e) {
		}
		config.setRunning(false);
      
		LogManager.debug("\n\n********* Finished Test *************************");
	}
	
	public static class TestProducer2 extends Thread {
		public void run(){
			QueueConfig config = AsyncQueueManager.getInstance().getQueueConfig("test");
			for(int i=0;i<50;i++){
				try {
					config.getNextQueue().put(""+i); //계속 기다리다.. 기어이 넣고 만다..
				} catch (AsyncQueueTimeoutException e) {
					System.out.println("AsyncQueueTimeoutException >> Timeout..."+i);
					break;
				}catch(Exception e){
					System.out.println("Queue 오류..."+e.toString());
					break;
				}
			}
		}
	}
	public static class TestProducer3 extends Thread {
		public void run(){
			QueueConfig config = AsyncQueueManager.getInstance().getQueueConfig("test");
			for(int i=0;i<50;i++){
				try {
					config.getNextQueue().put(""+i*100, 1);
				} catch (AsyncQueueException e) {
					LogManager.error(e.toString());
				} catch (AsyncQueueTimeoutException e) {
					LogManager.error("AsyncQueueTimeoutException >> Timeout..."+i);
				}
			}
		}
	}
}