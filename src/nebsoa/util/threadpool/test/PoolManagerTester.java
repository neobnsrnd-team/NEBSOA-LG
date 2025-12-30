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
package nebsoa.util.threadpool.test;

import nebsoa.common.log.LogManager;
import nebsoa.util.threadpool.ThreadObjectFactory;
import nebsoa.util.threadpool.ThreadPool;
import nebsoa.util.threadpool.ThreadPoolManager;
import nebsoa.util.threadpool.WorkerThread;

/**
 * @author 이종원
 * Tester class for testing the ThreadPool
 */
public class PoolManagerTester {
    static int i=0;
    int count;
   private ThreadPool pool = null;

   /**
    * @return Returns the pool.
    */
   public ThreadPool getPool() {
      return pool;
   }

   /**
    * @param socketPool - The pool to set.
    */
   public void setPool(ThreadPool tpool) {
      this.pool = tpool;
   }

   public PoolManagerTester() {
       this.count=i++;
      if (pool == null) {
         //pool = new ThreadPool(new ThreadObjectFactory());
         //pool = new ThreadPool(new ThreadObjectFactory(),10,20,1000); 
          pool = ThreadPoolManager.getInstance().getPool(""+count,true);
      }
   }

   /**
    * shutdown 
    */
   public void shutdown() {
      try {
         pool.close();
      } catch (Exception e) {
         LogManager.error("", e);
      }
   }

   /**
    * runWithNotify - method simulates the use of wait() and notify().
    */
   public void runWithNotify() {
       for(int i=0;i<10;i++){
          try {
             // if(i>10 && i/20==0) pool.close();
             LogManager.debug(count+"**"+i+"************************************");
             WorkerThread rt1 = (WorkerThread) pool.borrowObject();
             Object synObj = new Object();
             rt1.execute("nebsoa.util.threadpool.test.SampleWork",
                   "executeTask", null, null, synObj);
             /*
             synchronized (synObj) {
                synObj.wait();
             }
             */
             Thread.sleep(1);
             //Object result  = rt1.getResult();
             //LogManager.debug(i+"**Result = " + result);
    
             //pool.returnObject(rt1);
          } catch (Exception e) {
             LogManager.error("", e);
          }
          
       }
   }
   
   /**
    * runAsyncTask - method simulates the executing a task without waiting for
    * the results. Please note that  we need to set the pool so that the thread goes
    * back to the pool, upon completion of the task.
    */
   public void runAsyncTask() {
      try {
         LogManager.debug("**** Running runAsyncTask().. ");
         WorkerThread rt1 = (WorkerThread) pool.borrowObject();
         // set the pool now so that the WorkerThread knows how to return itself
         //to the pool.
         rt1.setPool(pool); 

         rt1.execute("nebsoa.common.net.services.pooling.test.SampleWork",
               "executeTask", null, null, null);
      } catch (Exception e) {
         LogManager.error("", e);
      }
   }


   //.
   /**
    * runWithoutNotify - This methods does not use the wait() and notify()
    * semantics, but uses a loop to check if the thread had finished executing
    * the task by invoking the <code>isDone()</code> method.
    */
   public void runWithoutNotify() {
      try {
         LogManager.debug("**** Running runWithTwoWorkers().. ");

         WorkerThread[] rtArr = pool.borrowObjects(2);
         WorkerThread rt1 = rtArr[0];
         WorkerThread rt2 = rtArr[1];

         rt1.execute("nebsoa.util.threadpool.test.SampleWork",
               "executeTask", null, null, null);
         rt2.execute("nebsoa.util.threadpool.test.SampleWork",
               "executeTask", null, null, null);

         while (!rt1.isDone() || !rt2.isDone()) {
            Thread.sleep(100);
         }

         pool.returnObject(rt1);
         pool.returnObject(rt2);
         LogManager.debug("*** Finished Thread " + this);
      } catch (Exception e) {
         LogManager.error("", e);
      }
   }

   /**
    * runMultiple - Simulates multiple threads executing the SampleWork task.
    * @param cnt 
    */
   public void runMultiple(int cnt) {
      try {
         LogManager.debug("**** Running runMultiple().. " + cnt);
         PoolWorker[] poolArr = new PoolWorker[cnt];
         for (int i = 0; i < cnt; i++) {
            poolArr[i] = new PoolWorker(this.getPool(), i);
            new Thread(poolArr[i]).start();
         }
         LogManager.debug("*** Finished runMultiple ");
      } catch (Exception e) {
         LogManager.error("", e);
      }
   }

   public static void main(String[] args) {
      LogManager.debug("\n\n********* Starting Test ************************");
      for(int i=0;i<10;i++){
          PoolManagerTester pt = new PoolManagerTester();    
          pt.runWithNotify(); // test using wait() and notify()
          //if(i%10==1){
              ThreadPoolManager.getInstance().logThreadPoolState();
          //}
      }
   }

}