/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.socketpool;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;
import nebsoa.util.threadpool.ThreadPool;
import nebsoa.util.threadpool.ThreadPoolManager;
import nebsoa.util.threadpool.WorkerThread;
/**
 * worker Socket
 * @author 이종원
 */
/*******************************************************************
 * <pre>
 * 1.설명 
 * SocketPool 에서 관리되는 Socket 객체.
 * pooling이 되므로 socket pool안에 있을 동안 상대방(원격 호스트)에서
 * socket을 닫은 경우나, 오랜 시간 사용되지 않은 소켓을 자동으로 닫아 버린다.
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
 * $Log: PoolableWorkerSocket.java,v $
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
 * Revision 1.1  2008/08/04 08:54:51  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.2  2008/06/27 10:23:50  이종원
 * PipedStream buffer size를 16k로 늘림
 *
 * Revision 1.1  2008/01/22 05:58:24  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:37:39  안경아
 * *** empty log message ***
 *
 * Revision 1.21  2007/06/07 09:19:42  이종원
 * Thread pool max idle 갯수 조정
 *
 * Revision 1.20  2007/05/11 08:25:09  이종원
 * last read time값 변경
 *
 * Revision 1.19  2007/02/01 08:50:59  이종원
 * *** empty log message ***
 *
 * Revision 1.18  2007/01/29 07:23:01  이종원
 * *** empty log message ***
 *
 * Revision 1.17  2007/01/09 07:53:41  안경아
 * *** empty log message ***
 *
 * Revision 1.16  2007/01/08 02:56:56  안경아
 * *** empty log message ***
 *
 * Revision 1.15  2007/01/08 02:42:11  안경아
 * AsyncReader update
 *
 * Revision 1.14  2007/01/02 08:25:10  이종원
 * *** empty log message ***
 *
 * Revision 1.13  2007/01/02 08:10:14  이종원
 * WorkerSocket안에 Thread reset하는 로직 제거
 *
 * Revision 1.12  2007/01/02 05:21:09  이종원
 * socket close하는 로직 수정
 *
 * Revision 1.11  2006/10/19 14:40:30  이종원
 * *** empty log message ***
 *
 * Revision 1.10  2006/10/17 08:44:08  이종원
 * evict log추가
 *
 * Revision 1.9  2006/10/13 02:27:56  이종원
 * *** empty log message ***
 *
 * Revision 1.8  2006/10/02 02:46:38  이종원
 * *** empty log message ***
 *
 * Revision 1.7  2006/10/02 00:53:29  이종원
 * *** empty log message ***
 *
 * Revision 1.6  2006/09/29 08:38:19  이종원
 * *** empty log message ***
 *
 * Revision 1.5  2006/09/29 08:36:15  이종원
 * 소켓 풀링 일 경우 바로 다는 부분에 대한 보완
 *
 * Revision 1.4  2006/09/28 14:03:40  이종원
 * *** empty log message ***
 *
 * Revision 1.3  2006/09/28 13:59:54  이종원
 * *** empty log message ***
 *
 * Revision 1.2  2006/09/28 13:57:55  이종원
 * *** empty log message ***
 *
 * Revision 1.1  2006/09/27 10:22:53  이종원
 * 디렉토리변경
 *
 * Revision 1.8  2006/09/27 09:08:53  이종원
 * *** empty log message ***
 *
 * Revision 1.7  2006/09/27 08:08:24  이종원
 * log양 줄임
 *
 * Revision 1.6  2006/09/27 03:33:51  이종원
 * default로 pooling false세팅
 *
 * Revision 1.5  2006/09/27 03:08:24  이종원
 * read bug수정
 *
 * Revision 1.4  2006/09/27 03:07:01  이종원
 * socket count하는 로직 수정 및 read bug수정
 *
 * Revision 1.3  2006/09/26 10:25:41  이종원
 * pooling socket update
 *
 * Revision 1.2  2006/09/26 09:16:48  이종원
 * 풀링 사용할 경우 close하는 로직 보완
 *
 * Revision 1.1  2006/09/23 00:52:33  이종원
 * *** empty log message ***
 *
 * Revision 1.2  2006/09/19 12:47:06  이종원
 * update
 *
 * Revision 1.1  2006/09/18 10:44:10  이종원
 * 최초작성
 *
 * </pre>
 ******************************************************************/
public class PoolableWorkerSocket extends WorkerSocket {
   
    
    AsyncReader async;
   
    public PoolableWorkerSocket(Socket socket,String logConfig) {
        super(socket,logConfig);
    }
   /**
    * @param done The done to set.
    
   public void setDone(boolean done) {
      this.done = done;
      if(done && destroyOnDone){
          stopped=true;
          LogManager.debug("Destroy on done setted... so close me..."+this);
          if(pool != null && !pool.closed()) {
              pool.decreaseSocketCount();
              try {
                  pool.invalidateObject(this);
              } catch (Exception e) {
                
              }
          }
      }
   }
 * @throws Exception 
 */
    /**
     * SpiderPipedStream을 이용한 input Stream을 open한다.
     */
    public void openInputStream() throws IOException {
       if(dataInputStream == null){
           try {
               InputStream in = socket.getInputStream();
               PipedOutputStream pos=new PipedOutputStream();
               PipedInputStream pis=new SpiderPipedInputStream(pos);
               dataInputStream = new DataInputStream(pis);
               async = new AsyncReader(this,in,pos);
               ThreadPool threadPool = ThreadPoolManager.getInstance().getPool(
                       "ASYNC_READ_SOCKET_POOL",2);
               WorkerThread thread = (WorkerThread) threadPool.borrowObject();
               thread.execute(async);
               LogManager.debug("OPEN ASYNC INPUT STREAM OK---------");
           } catch (IOException e) {
               LogManager.error(logConfig,e.toString(),e);
               throw e;
           } catch (Exception e) {
               throw new SysException (e);
           }
       }    
   }
   
   	/**
   	 * 버퍼 용량이 큰 Pipe stream이다. jdk 기본 1024 byte를 socket 기본 버퍼의 2배(16K)로 교체
   	 */
   	static class SpiderPipedInputStream extends PipedInputStream{
   	
   		public SpiderPipedInputStream(){
   		
   		}
   	
   		public SpiderPipedInputStream(PipedOutputStream source) throws IOException{
   			super(source);
   			// buffer가 큰 놈으로 교체 
   			super.buffer = new byte[1024*16];
   		}
   	}
   
   /**
    * 비동기 통신이기 때문에 아직 읽어 가지 않은 부분이 있으면 기다려 준 후 close한 다. 
    * 2006. 9. 29. 이종원 작성
    * @see nebsoa.spiderlink.socketpool.WorkerSocket#closeStream() 재정의
    */
   public void closeStream(){

       if(dataOutputStream != null){
           //LogManager.debug("close dataOutputStream");
           try {
               dataOutputStream.close(); 
               dataOutputStream=null;
           } catch (Exception e) {
               LogManager.error(logConfig,e.getMessage(),e);
           }
       }
       
       if(dataInputStream != null){
           int i=0;
           try {
               while(dataInputStream.available()>0 && i++ <20){                   
                   Thread.sleep(100);
               }
           } catch (IOException e1) {
               LogManager.error(e1.getMessage(),e1);
           } catch (InterruptedException e1) {
               LogManager.error(e1.getMessage(),e1);
           }
           //LogManager.debug("close dataInputStream");
           try {
               dataInputStream.close();      
               dataInputStream=null;
           } catch (Exception e) {
               LogManager.error(logConfig,e.getMessage(),e);
           }
       }
   }
   
   public void close(){
       super.close();
       if(async != null){
    	   try{	
    		   async.closePipedStream();
    	   }catch(Exception e){
    		   
    	   }finally{	
    		   async = null;
    	   }
       }
   }
   
   public String toString(){
       return "PoolableWorkerSocket@"+super.toString();
   }
   
   class AsyncReader implements Runnable{
       PoolableWorkerSocket worker;
       InputStream in;
       PipedOutputStream pos;
       public AsyncReader(PoolableWorkerSocket worker,
               InputStream in, PipedOutputStream pos) {
           this.worker = worker;
           this.in = in;
           this.pos = pos;
       }

       public void run(){
           byte[] buf = new byte[1024*5];
           int len=-1;
           long lastReadTime=System.currentTimeMillis();
           try {
               while(!worker.isStopped()){
                   try{
//                       LogManager.infoLP(new Date()+"before read...");
                       len=in.read(buf);
                       if(len != -1){
                           lastReadTime=System.currentTimeMillis();
                           pos.write(buf,0,len);
                           //LogManager.debug("PoolableSocketReader Read:"+new String(buf,0,len));
                       }else{
                           LogManager.infoLP("READ EOF.... SO STOP READER");
                           worker.stop();
                       }
                   } catch (SocketTimeoutException e) {
//                       LogManager.info("Socket Timeout : "+e.toString());
//                       LogManager.debug("CurrentMillis\t:"+System.currentTimeMillis());
//                       LogManager.debug("getWorkStartTime\t:"+worker.getWorkStartTime());
//                       LogManager.debug("getDestroyThreshold()\t:"+worker.getDestroyThreshold());
                       //LogManager.debug("Wait time:"+
                       //        (System.currentTimeMillis()-worker.getWorkStartTime())/1000.0+"초");
                       
                       if( worker.getDestroyThreshold()> 0 && 
                               (System.currentTimeMillis() - lastReadTime >
                           worker.getDestroyThreshold())){
                           LogManager.debug("Socket getDestroyThreshold 보다 긴 시간동안 쉬었으므로 destroy["
                                   +worker.getDestroyThreshold()+"]");
                           worker.stop();
                       }else{
                           //LogManager.debug("--------- timeout ---- 무시함.");
                       }
                   } catch (EOFException e) {
                       LogManager.info("Socket 종료 패킷 수신 : "+e.toString());
                       worker.stop(); 
                   } catch (IOException e) {
                       LogManager.error("Socket read 오류  : "+e.toString());
                       worker.stop();
                   } catch (Exception e){
                       worker.stop();
                       LogManager.error("EXCEPTION  : "+e.toString());//end of try-catch
                   }
               } // end of while
           } finally{
        	   try{
        		   worker.stop();
        	   }catch(Exception e){
        		   LogManager.error(logConfig,e.toString(),e);
        	   }
        	   
        	   try{
        		   worker.close();
        	   }catch(Exception e){
        		   LogManager.error(logConfig,e.toString(),e);
        	   }
               
               try{
                   closePipedStream();
               }catch(Exception e){
                   LogManager.error(logConfig,e.toString(),e);
               }
               
               //stop으로 마킹 했으니.. 이후 자동 close될 것이다...
               
               try {
                   SocketPool pool = worker.getPool();
                   if(pool != null && !pool.closed() ){
                       pool.evict();
                       LogManager.info(logConfig,">>> evict Socket Poolable Socket:"+worker
                    		   +",\nPoolStatus:"+pool.getInfo());
                   }
               } catch (Exception e) {
                    //LogManager.error(e.toString(),e);
               }
           }
       }

        public void closePipedStream() {
            if(in != null){
                LogManager.debug("close Original input Stream ..."+this);
                try {
                    in.close();
                } catch (Exception e) {
                 
                }finally{
                    in=null;
                }
            }
            if(pos != null){
                LogManager.debug("close Pipe Stream ...");
                try {
                    pos.flush();
                } catch (Exception e) {
                 
                }
                try {
                    pos.close();
                } catch (Exception e) {
                 
                }finally{
                    pos=null;
                }
            }
       }
   }
}