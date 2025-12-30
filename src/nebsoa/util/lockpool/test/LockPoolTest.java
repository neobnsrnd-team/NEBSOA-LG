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
package nebsoa.util.lockpool.test;

import nebsoa.common.log.LogManager;
import nebsoa.util.lockpool.Lock;
import nebsoa.util.lockpool.LockPool;
import nebsoa.util.lockpool.LockPoolManager;

/**
 * @author 이종원
 * Tester class for testing the ThreadPool
 */
public class LockPoolTest extends Thread{
    LockPool pool;
    public LockPoolTest(String name){
        pool = LockPoolManager.getInstance().getLockPool(name,3);
    }
    
    public void run(){
        for(int i=0;i<50;i++){
            Lock lock=null;
            try{
                if(i%10==9) pool.setMaxActive(pool.getMaxActive()+1);
                lock = pool.getLock();
                Thread.sleep(100);
                System.out.println("get Lock Ok---------"+lock);
            }catch(Exception e){
                System.out.println(e.getMessage());
            }finally{
                try{
                    if(pool != null && !pool.closed()){
                        pool.returnLock(lock);
                    }
                }catch(Exception e){}
            }
        }
    }
    public static void main(String[] args) {
      LogManager.debug("\n\n********* Starting Test ************************");
      for(int i=0;i<10;i++){
          LockPoolTest pt = new LockPoolTest("test");    
          pt.start();
      }
   }

}