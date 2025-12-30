/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.util.threadpool;

import java.lang.reflect.InvocationTargetException;

import nebsoa.common.log.LogManager;
import nebsoa.common.monitor.ContextLogger;
import nebsoa.common.util.DataMap;

import org.apache.commons.beanutils.MethodUtils;

/*******************************************************************
 * <pre>
 * 1.설명 
 * ThreadPool 에서 관리되는 Thread 객체
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
 * $Log: WorkerThread.java,v $
 * Revision 1.1  2018/01/15 03:39:50  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:45  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:24  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:27  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.2  2008/08/06 11:35:42  youngseokkim
 * Context가 없을 시 나는 에러를 위하여
 * Worker Thread에 dataMap이라는 변수 선언하고
 * run metthod 내에서 ContextLogger에 put하도록 기능 수정
 *
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:34  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:35  안경아
 * *** empty log message ***
 *
 * Revision 1.9  2007/06/07 09:20:21  이종원
 * thread wake up 및 자동 destroy기능 추가
 *
 * Revision 1.8  2007/03/30 14:55:29  이종원
 * thread pool close하는 부분 trace하는 로그 추가
 *
 * Revision 1.7  2006/10/02 13:19:38  이종원
 * *** empty log message ***
 *
 * Revision 1.6  2006/09/27 08:23:55  이종원
 * *** empty log message ***
 *
 * Revision 1.5  2006/08/25 05:11:51  이종원
 * *** empty log message ***
 *
 * Revision 1.4  2006/07/28 16:02:27  이종원
 * *** empty log message ***
 *
 * Revision 1.3  2006/06/17 10:17:16  오재훈
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class WorkerThread extends Thread {

   public WorkerThread() {
      //default constructor
   }

   /**
    * Keeps the thread running when false. When set to true, completes the
    * execution of the thread and stops.
    */
   private boolean stopped = false;

   /**
    * Manages the thread's state. When the thread is first created, running is
    * set to false. When the thread is assigned a task for the first time, the
    * thread continues to be in the running state until stopped.
    */
   private boolean running = false;

   /**
    * 현재 수행중인 job이 있는지 여부,<font color='red'>수행중인 것이 없을 때 true이다.</font> 
    */
   private boolean done = true;

   /**
    * The class name where the method to be execute is defined.
    */
   private String className;

   /**
    * The method name to be executed.
    */
   private String methodName;

   /**
    * The parameters to be passed for the method.
    */
   private Object[] methodParams;

   /**
    * The parameter types for the respective parameters..
    */
   private Class[] parmTypes;

   /**
    * The object to synchronize upon for notifying the completion of task.
    */
   private Object syncObject = null;

   /**
    * The result of our execution.
    */
   private Object result = null;
   
   /**
    * The pool being used. We use this if we need to return the object back to the
    * pool. If this is not set, we assume that the client will take care of returning
    * the object back to the pool.
    */
   private ThreadPool pool = null;
   
  	private DataMap dataMap;
    
   	public DataMap getDataMap() {
   		return dataMap;
   	}
   	public void setDataMap(DataMap dataMap) {
   		this.dataMap = dataMap;
   	}
   /**
    * @param pool The pool to set.
    */
   public void setPool(ThreadPool pool) {
       System.out.println("------- set pool:"+pool);
      this.pool = pool;
   }
   /**
    * @param result The result to set.
    */
   public void setResult(Object result) {
      this.result = result;
   }
   /**
    * @return Returns the result.
    */
   public Object getResult() {
      return result;
   }
   /**
    * @return Returns the className.
    */
   public String getClassName() {
      return className;
   }
   /**
    * @param className The className to set.
    */
   public void setClassName(String className) {
      this.className = className;
   }
   /**
    * @return Returns the done.
    */
   public boolean isDone() {
      return done;
   }
   /**
    * @param done The done to set.
    */
   public void setDone(boolean done) {
      this.done = done;
   }
   /**
    * @return Returns the methodName.
    */
   public String getMethodName() {
      return methodName;
   }
   /**
    * @param methodName The methodName to set.
    */
   public void setMethodName(String methodName) {
      this.methodName = methodName;
   }
   /**
    * @return Returns the methodParams.
    */
   public Object[] getMethodParams() {
      return methodParams;
   }
   /**
    * @param methodParams The methodParams to set.
    */
   public void setMethodParams(Object[] methodParams) {
      this.methodParams = methodParams;
   }
   /**
    * @return Returns the parmTypes.
    */
   public Class[] getParmTypes() {
      return parmTypes;
   }
   /**
    * @param parmTypes The parmTypes to set.
    */
   public void setParmTypes(Class[] parmTypes) {
      this.parmTypes = parmTypes;
   }
   /**
    * @return Returns the running.
    */
   public boolean isRunning() {
      return running;
   }
   /**
    * @param running The running to set.
    */
   public void setRunning(boolean running) {
      this.running = running;
   }
   /**
    * @return Returns the stopped.
    */
   public boolean isStopped() {
      return stopped;
   }
   /**
    * @param stopped The stopped to set.
    */
   public void setStopped(boolean stopped) {
      this.stopped = stopped;
   }
   /**
    * @return Returns the syncObject.
    */
   public Object getSyncObject() {
      return syncObject;
   }
   /**
    * @param syncObject The syncObject to set.
    */
   public void setSyncObject(Object syncObject) {
      this.syncObject = syncObject;
   }
   /**
    * execute
    * @param clsName
    * @param methName
    * @param params
    * @param synObj
    */
   public synchronized void execute(String clsName, String methName,
         Object[] params, Class[] paramTypes, Object synObj) {
      this.className = clsName;
      this.methodName = methName;
      this.methodParams = params;
      this.syncObject = synObj;
      this.parmTypes = paramTypes;
      this.done = false;
      
      if (!running) { //If this is the first time, then kick off the thread.
         this.setDaemon(true);
         //LogManager.debug(">>>>>> start the thread");
         this.start();
      } else { // we already have a thread running so wakeup the waiting thread.
         this.notifyAll();
      }
   }
   
   public synchronized void execute(String clsName, String methName, DataMap dataMap) throws Exception {
	   this.className = clsName;
	   this.methodName = methName;
	   this.methodParams = new Object[]{dataMap};
	   this.syncObject = null;
	   this.parmTypes = new Class[]{Class.forName("nebsoa.common.util.DataMap")};
	   this.done = false;
	   this.dataMap = dataMap;

	   ContextLogger.putContext(dataMap.getContext());

	   if (!running) { //If this is the first time, then kick off the thread.
		   this.setDaemon(true);
		   //LogManager.debug(">>>>>> start the thread");
	    	this.start();
	   } else { // we already have a thread running so wakeup the waiting thread.
		   this.notifyAll();
	   }
	}   
   
   public synchronized void execute(Runnable target) {
        this.syncObject = target;
        this.done = false;
        
        if (!running) { //If this is the first time, then kick off the thread.
           this.setDaemon(true);
           //LogManager.debug(">>>>>> start new thread");
           this.start();
        } else { // we already have a thread running so wakeup the waiting thread.
           this.notifyAll();
        }
   }
   
   public void destroy(){
       try{
           stopped=true;
           this.notifyAll();
       }catch(Exception e){}
   }

   /* (non-Javadoc)
    * @see java.lang.Runnable#run()
    */
   public void run() {
      running = true;
      while (!stopped) {
         try{
             if(pool == null || pool.closed()){
                 stopped=true;
                 return;
             }
         }catch(Exception e){
             return;
         }
         if (done) {
            synchronized (this) {
               try {
                  this.wait(10*60*1000);
               } catch (InterruptedException e) {
                  stopped = true;
                  LogManager.error("InterruptedException", e);
               }
            }
         } else { //there is a task....let us execute it.
            try {
            	// context 를 다른 쓰레드에서도 유지 하기 위해 추가
            	if(dataMap != null){
            		if(dataMap.getContext() != null){
	            		// Tier 분리시에도 Context 객체를 유지하기 위해서 ThreadLocal에 넣는다.
	                    ContextLogger.putContext(dataMap.getContext());
            		}
            	}
               execute();
            } catch (Exception e) {
               LogManager.error("", e);
            } finally {
               if (syncObject != null) {
                  synchronized (syncObject) {
                     syncObject.notify();
                  }
               }
               reset();
               //LogManager.debug("^^^^^returnToPool"+this);
               returnToPool();
            }
         }
      }
      
      LogManager.debug("thread stopped......."+this);
   }
   
   /** For  asynchronous execution, without waiting, the pool should be
    *  set, so that we can return the thread back to the pool. No need to use any
    * <code>wait()</code> or <code>notify()</code> methods.
    * see : <code>runAsyncTask()</code> method in PoolTester class.
    * <pre>
    *  Example : 
    *          ThreadPool pool = new ThreadPool(new ThreadObjectFactory());
    *          WorkerThread wt = (WorkerThread) pool.borrowObject();
    *          wt.setPool(pool);       
    *          wt.execute(.....) ;
    * </pre>
    */
   private void returnToPool() {
       //System.out.println("---------return to the pool---");
      if(pool != null ) {
          //System.out.println("---------return to the pool---not null");
         if(pool.closed()){
//             LogManager.error("MONITOR","try to returnToPool but Thread pool is closed.."+this,
//                     new Exception("이것은 오류는 아닙니다.호출되는 곳 trace용임."));
             this.stopped=true;
             return;
         }
         try {
             //LogManager.debug(">>> return to the pool "+this);
             pool.returnObject(this);
         } catch (Exception e1) {
            LogManager.error(e1);
         }
         //this.pool = null;
      }
   }

   /**
    * reset the memebers to service next request.
    */
   public void reset() {
      this.done = true;
      this.className = null;
      this.methodName = null;
      this.methodParams = null;
      this.parmTypes = null;
      this.syncObject = null;
   }

   /**
    * getClass
    * @param cls
    * @return Class
    * @throws ClassNotFoundException  
    */
   private static Class getClass(String cls) throws ClassNotFoundException {
      ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
      if (classLoader == null) {
         classLoader = WorkerThread.class.getClassLoader();
      }
      return classLoader.loadClass(cls);
   }

   /**
    * execute
    * syncObject가 Runnable이면 해당 객체의 run()을 호출하여 주고 종료하며,
    * 그렇지 않을 경우 class,method, param을 멤버변수로 받아
    * reflection을 사용하여수행한다.( MethodUtils.invokeExactMethod)
    */
   private void execute() {
      long start=System.currentTimeMillis();
      try {
          if(syncObject != null && syncObject instanceof Runnable) {
               Runnable target=(Runnable) syncObject;
               target.run();
               //LogManager.debug("execute thread... finished..");
               return;
          }
        
          try {
             Class cls = getClass(this.getClassName());
             Object obj = cls.newInstance();
             this.result = MethodUtils.invokeExactMethod(obj, this
                   .getMethodName(), this.getMethodParams(), this.getParmTypes());
             //LogManager.debug(" #### Execution Result = " + result + " for : " + this);
    
          } catch (ClassNotFoundException e) {
             LogManager.error("ClassNotFoundException - " + e);
          } catch (NoSuchMethodException e) {
             LogManager.error("NoSuchMethodException - " + e);
          } catch (IllegalAccessException e) {
             LogManager.error("IllegalAccessException - " + e);
          } catch (InvocationTargetException e) {
             LogManager.error("InvocationTargetException - " + e);
          } catch (InstantiationException e) {
             LogManager.error("InstantiationException - " + e);
          }
      }finally{
          exeCount++;
          long end=System.currentTimeMillis();
          totalExeTime = totalExeTime+(end-start);
          exeTime=(end-start);
          //LogManager.debug("thread 수행시간 : "+(exeTime)+"ms");
      }
   }
   
   int exeCount=0;
   long totalExeTime=0;
   long exeTime=0;
   
   public Object getPool() {
       return pool;
   }
   
    public int getExeCount() {
        return exeCount;
    }
    public void setExeCount(int exeCount) {
        this.exeCount = exeCount;
    }
    public long getExeTime() {
        return exeTime;
    }
    public void setExeTime(long exeTime) {
        this.exeTime = exeTime;
    }
    public long getTotalExeTime() {
        return totalExeTime;
    }
    public void setTotalExeTime(long totalExeTime) {
        this.totalExeTime = totalExeTime;
    }
}