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
package test.spiderlink.socketpool;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import nebsoa.common.log.LogManager;
import nebsoa.spiderlink.socketpool.SocketFactory;
import nebsoa.spiderlink.socketpool.SocketPool;
import nebsoa.spiderlink.socketpool.SocketPoolManager;
import nebsoa.spiderlink.socketpool.WorkerSocket;

/**
 * @author 이종원
 * Tester class for testing the socket Pool
 */
public class SocketPoolManagerTester {
    static int i=0;
    int count;
    
    private SocketPool pool = null;


    public SocketPoolManagerTester(String ip, int port) {
       this(ip,port,30);
    }
    
   public SocketPoolManagerTester(String ip, int port, int timeout) {
       this.count=i++;
       pool = SocketPoolManager.getInstance().getPool(ip+"_"+port);
       if(pool==null){
           SocketFactory factory = new SocketFactory(ip,port,timeout,"DEBUG");
           pool=SocketPoolManager.getInstance().makeSocketPool(
                   ip+"_"+port,factory,1,1);
       }
   }
   
   /**
    * @return Returns the pool.
    */
   public SocketPool getPool() {
      return pool;
   }

   /**
    * @param socketPool - The pool to set.
    */
   public void setPool(SocketPool tpool) {
      this.pool = tpool;
   }

   /**
    * shutdown 
    */
   public void shutdown() {
      try {
          if(pool!=null){pool.close();}
      } catch (Exception e) {
         LogManager.error("", e);
      }
   }

   /**
    * runWithNotify - method simulates the use of wait() and notify().
 * @throws Exception 
    */
   public void run()  {
       WorkerSocket worker =null;
       for(int i=0;i<10;i++){
          try {
             // if(i>10 && i/20==0) pool.close();
             LogManager.debug(count+"**"+i+"************************************");
             worker = (WorkerSocket) pool.borrowObject();
             if(worker.getPool() == null){
                 worker.setPool(pool);
             }
             String testStr="test_"+i;
             testWriteAndRead(testStr,worker.getDataOutputStream(),
                     worker.getDataInputStream());    
             
          } catch (Exception e) {
              if(worker != null)
              worker.stop();
              LogManager.error(e.getMessage(), e);
          }finally{
              try {
                pool.returnObject(worker);
            } catch (Exception e) {
               System.out.println("fail to return object");
            }
          }
       }
   }



   private void testWriteAndRead(String testStr, 
           DataOutputStream dataOutputStream, 
           DataInputStream dataInputStream) throws IOException {
        byte[] buf = testStr.getBytes();
    
        dataOutputStream.writeShort(buf.length);
    
        dataOutputStream.write(buf,0,buf.length);
        dataOutputStream.flush();
        
//        try {
//            Thread.sleep(5*1000);
//        } catch (InterruptedException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        
        int len=dataInputStream.readShort();
        buf=new byte[len];
        dataInputStream.readFully(buf);
        System.out.println(testStr+" write and read ok");
 
    
   }

   public static void main(String[] args) {
      LogManager.debug("\n\n********* Starting Test ************************");
      for(int i=0;i<10;i++){
          SocketPoolManagerTester pt = 
              new SocketPoolManagerTester("127.0.0.1",10001);    
          pt.run(); // test using wait() and notify()
          //if(i%10==1){
              SocketPoolManager.getInstance().logSocketPoolState();
          //}
      }
      
//      for(int i=0;i<1;i++){
//          SocketPoolManagerTester pt = 
//              new SocketPoolManagerTester("127.0.0.1",10001);    
//          pt.run(); // test using wait() and notify()
//          //if(i%10==1){
//              SocketPoolManager.getInstance().logSocketPoolState();
//          //}
//      }
//      //loopback을 재 start시키기 위해 30초간 sleep
//      try {
//        Thread.sleep(30*1000);
//      } catch (InterruptedException e) {
//        // TODO Auto-generated catch block
//        e.printStackTrace();
//      }
//      for(int i=0;i<1;i++){
//          SocketPoolManagerTester pt = 
//              new SocketPoolManagerTester("127.0.0.1",10001);    
//          pt.run(); // test using wait() and notify()
//          //if(i%10==1){
//              SocketPoolManager.getInstance().logSocketPoolState();
//          //}
//      }
      System.out.println("sleep 30초");
      try {
        Thread.sleep(30*1000);
    } catch (InterruptedException e) {
    }
      SocketPoolManager.getInstance().closeAll();
   }

}