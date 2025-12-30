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
import nebsoa.util.threadpool.ThreadPool;
import nebsoa.util.threadpool.WorkerThread;

/**
 * @author Murali Kosaraju
 * This class actually does the work in its own thread.
 */
public class PoolWorker implements Runnable {
    private ThreadPool pool = null;

   private int id = 0;

   public PoolWorker(ThreadPool tpool, int tid) {
      this.pool = tpool;
      this.id = tid;
   }

   public void run() {
      try {
//         log.debug("**** Running Tester Thread = " + id);
         WorkerThread rt1 = (WorkerThread) pool.borrowObject();

         Object synObj = new Object();
         Object[] params = new Object[] { "Hello", Integer.valueOf(id) };
         Class[] parmTypes = new Class[] { String.class, int.class };

         rt1.execute("serverside.common.net.services.pooling.test.SampleWork",
               "executeTask", params, parmTypes, synObj);
         //			rt1.execute("serverside.common.net.services.pooling.test.SampleWork",
         // "executeTask", null, null, synObj);
         synchronized (synObj) {
            synObj.wait();
         }

         pool.returnObject(rt1);
//         log.debug("*** Finished Thread " + id);
      } catch (Exception e) {
         LogManager.error("", e);
      }
   }

}