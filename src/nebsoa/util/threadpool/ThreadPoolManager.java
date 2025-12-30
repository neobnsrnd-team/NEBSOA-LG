/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */


package nebsoa.util.threadpool;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;

/**
 * <code>ThreadPoolManager</code>는 시스템에서 사용 하는 모든 thread pool를 관리합니다.
 * <code>ThreadPoolManager</code>는
 * 
 * <code>ThreadPoolManager</code>의 getPool("key이름")을 이용하여
 * Pool객체를  얻을 수 있습니다. 
 * <p>
 * <blockquote>
 * 
 * <pre>
 *  ThreadPool myPool = ThreadPoolManager.getInstance().getPool("MyPool");
 *  if(myPool==null){
 *      myPool = ThreadPoolManager.getInstance().makeThreadPool(ThreadObjectFactory factory, 최대갯수, 최초 갯수);
 *  }
 *  만약 등록된  threadPool이 없을 시자동  생성하게 하려면 
 *  ThreadPool myPool = ThreadPoolManager.getInstance().getPool("MyPool",true);
 *  
 *  Object synObj = new Object();
 *  rt1.execute("nebsoa.util.threadpool.test.SampleWork",
 *            "executeTask", null, null, synObj);
 *  synchronized (synObj) {
 *     synObj.wait(1000);
 *  }
 *  //Object result  = rt1.getResult();
 * </pre>
 * 
 * </blockquote>
 *
 */
/*******************************************************************
 * <pre>
 * 1.설명 
 * <code>ThreadPoolManager</code>는 시스템에서 사용 하는 모든 thread pool를 관리합니다.
 * <code>ThreadPoolManager</code>는
 * 
 * <code>ThreadPoolManager</code>의 getPool("key이름")을 이용하여
 * Pool객체를  얻을 수 있습니다. 
 * 
 * 2.사용법
 * <blockquote>
 * 
 *  ThreadPool myPool = ThreadPoolManager.getInstance().getPool("MyPool");
 *  if(myPool==null){
 *      myPool = ThreadPoolManager.getInstance().makeThreadPool(ThreadObjectFactory factory, 최대갯수, 최초 갯수);
 *  }
 *  만약 등록된  threadPool이 없을 시자동  생성하게 하려면 
 *  ThreadPool myPool = ThreadPoolManager.getInstance().getPool("MyPool",true);
 *  만약 등록된  threadPool이 없을 시  원하는 갯수만큼의 풀을 자동  생성하게 하려면 
 *  ThreadPool myPool = ThreadPoolManager.getInstance().getPool("MyPool",max,initCount);*  
 *  
 *  WorkerThread thread = (WorkerThread)myPool.borrowObject();
 *  Object synObj = new Object();
 *  thread.execute("nebsoa.util.threadpool.test.SampleWork",
 *            "executeTask", null, null, synObj);
 * 또는 Runnable한 객체 수행 시. 
 *  thread.execute(runnableObject); // call run method
 *  기타 사용법.
 *  synchronized (synObj) {
 *     synObj.wait(1000);
 *  }
 *  //Object result  = thread.getResult();
 * 
 * </blockquote>
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
 * $Log: ThreadPoolManager.java,v $
 * Revision 1.1  2018/01/15 03:39:50  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:45  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.2  2008/12/23 01:30:37  김성균
 * ThreadPool close시 threadPoolHash에서도 삭제처리
 *
 * Revision 1.1  2008/11/18 11:27:24  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:27  김성균
 * LGT Gateway를 위한 프로젝트로 분리
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
 * Revision 1.16  2007/06/07 09:20:21  이종원
 * thread wake up 및 자동 destroy기능 추가
 *
 * Revision 1.15  2007/02/27 07:10:57  김승희
 * closeAll 메소드 수정
 *
 * Revision 1.14  2007/02/26 10:59:21  이종원
 * *** empty log message ***
 *
 * Revision 1.13  2006/10/21 09:18:31  이종원
 * closeAll부분에서 threadPoolHash==null부분을 삭제
 *
 * Revision 1.12  2006/10/21 09:16:57  이종원
 * threadPoolHashMap을 얻는 부분을 get Method로 처리--null pointer가 발행하는 현상이 있음
 *
 * Revision 1.11  2006/10/16 04:16:22  이종원
 * *** empty log message ***
 *
 * Revision 1.10  2006/10/16 04:14:44  이종원
 * clsoe로직 정제
 *
 * Revision 1.9  2006/10/02 02:46:47  이종원
 * *** empty log message ***
 *
 * Revision 1.8  2006/09/27 08:23:55  이종원
 * *** empty log message ***
 *
 * Revision 1.7  2006/09/16 13:11:28  이종원
 * update
 *
 * Revision 1.6  2006/08/25 05:11:43  이종원
 * *** empty log message ***
 *
 * Revision 1.5  2006/07/28 16:02:27  이종원
 * *** empty log message ***
 *
 * Revision 1.4  2006/06/17 10:17:16  이종원
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class ThreadPoolManager {
    static Object dummy = new Object();
    public static ThreadPoolManager instance=null;
    /**
     * threadPool을  캐쉬 해 놓기 위한 HashMap입니다. 
     */
    private HashMap threadPoolHash;
    
    
    /**
     * 싱글톤으로 처리되도록 구현한 메소드
     * 2002. 10. 21.  이종원 작성
     * @return
     */
    public static ThreadPoolManager getInstance(){
        if(instance==null){
            synchronized(dummy){
                instance = new ThreadPoolManager();
            }
        }
        return instance;
    }
    
    private ThreadPoolManager(){
        if(threadPoolHash != null){
            try{
                closeAll();
            }catch(Exception e){}
        }
        threadPoolHash = getThreadPoolHash();
    }
    
    private HashMap getThreadPoolHash(){
        if(threadPoolHash == null){
            threadPoolHash = new HashMap();
        }
        return threadPoolHash;
    }

    
    /**
     * 등록된 threadPool 얻어 옵니다 .
     * <font color='red'>만약 등록된 threadPool이 없을때는 null을 return 합니다.
     * 없을경우 자동 생성을 하고 싶다면 boolean인자를 받는 메소드를 사용합니다.
     * </font>
     */
    public ThreadPool getPool(String poolId) {
        ThreadPool pool = (ThreadPool) getThreadPoolHash().get(poolId);
        if (pool == null) {
            LogManager.debug(poolId +" thread pool is null");
        }
        return pool;
    }
    
    /**
     * 등록된 threadPool 얻어 옵니다 .
     * <font color='red'>만약 등록된 threadPool이 없을때는 자동 생성을 하여 줍니다.</font>
     */
    public ThreadPool getPool(String poolId, boolean autoCreate) {
        ThreadPool pool = (ThreadPool) getThreadPoolHash().get(poolId);
        if (pool == null) {
            LogManager.debug(poolId +" thread pool is null .. create new pool..");
            if(autoCreate){
                return makeThreadPool(poolId);
            }
        }
        return pool;
    }
    
    /**
     * 등록된 threadPool 얻어 옵니다 .
     * <font color='red'>만약 등록된 threadPool이 없을때는 자동 생성을 하여 줍니다.</font>
     */
    public ThreadPool getPool(String poolId, int maxIdle) {
        ThreadPool pool = (ThreadPool) getThreadPoolHash().get(poolId);
        if (pool == null) {
            LogManager.debug(poolId +" thread pool is null .. create new pool..");
            return makeThreadPool(poolId,maxIdle);            
        }
        return pool;
    }


    /**
     * thread pool을 close합니다.
     */
    public void closeAll() {
        if(threadPoolHash==null) return;
        
        Set set = getThreadPoolHash().keySet();
        if (set.isEmpty())
            return;
        ThreadPool pool=null;
        Object[] key = set.toArray();
        for(int i=0; i<key.length; i++){
        	pool = (ThreadPool) getThreadPoolHash().get(key[i]);
        	try {
                if(pool != null && !pool.closed()) pool.close();
            } catch (Throwable e) {
                e.printStackTrace();
            }finally{
                //getThreadPoolHash().remove(key[i]);
            }
        }
        
        try{
            getThreadPoolHash().clear();
        }catch(Exception e){}
        //threadPoolHash=null;
        
       /* Iterator i = set.iterator();
        ThreadPool pool=null;
        Object key=null;
        while (i.hasNext()) {
            key=i.next();
            pool = (ThreadPool) getThreadPoolHash().get(key);
            try {
                if(pool != null && !pool.closed()) pool.close();
            } catch (Exception e) {
                e.printStackTrace();
            }finally{
                getThreadPoolHash().remove(key);
            }
        }
        try{
            getThreadPoolHash().clear();
        }catch(Exception e){}
        //threadPoolHash=null;
*/    }
    
    /**
     * thread pool을 close합니다.
     */
    public void close(String poolId) {
      
        synchronized(this){
            ThreadPool pool = (ThreadPool) getThreadPoolHash().get(poolId);
            if (pool != null) {
                try {
                    if (!pool.closed()) {
                        LogManager.infoLP(poolId + " Thread Pool을 close합니다.");
                        pool.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        getThreadPoolHash().remove(poolId);
                    } catch (Exception e) {
                    }
                }
            }
        }
    }
    
    

    /**
     * 받은 ThreadObjectFactory를 사용하여 thread pool을 생성합니다.
     * 지정된 갯수만큼의 thread pool을 생성합니다.
     */
    public ThreadPool makeThreadPool(String poolId,ThreadObjectFactory factory,
            int maxIdle, int minIdle ) {
        if (poolId == null) {
            throw new SysException("poolId is null");
        }

        ThreadPool pool = (ThreadPool) getThreadPoolHash().get(poolId);
 
        if (pool == null) {
            if(minIdle>2){
                minIdle=2;
            }
            pool = new ThreadPool(factory,maxIdle,minIdle);
            pool.setName(poolId);
            factory.setThreadPool(pool);
            getThreadPoolHash().put(poolId,pool);
        }
        
        LogManager.debug(poolId+" thread Pool을 생성하였습니다:"+pool.getInfo());
        return pool;
    }
    
    /**
     * 받은 ThreadObjectFactory를 사용하여 thread pool을 생성합니다.
     * thread maxidle개, min idle 0개  갯수만큼의 thread pool을 생성합니다.
     */
    public ThreadPool makeThreadPool(String poolId,ThreadObjectFactory factory){
        return makeThreadPool(poolId,factory,5,0);
    }
    
    /**
     * default ThreadObjectFactory(nebsoa.util.threadpool.ThreadObjectFactory)를 
     * 사용하여 thread pool을 생성합니다.
     * 지정된 최대 idle 갯수만큼의 thread pool을 생성합니다.
     */
    public ThreadPool makeThreadPool(String poolId
            ,int maxIdle){
        return makeThreadPool(poolId,new ThreadObjectFactory(),maxIdle,0);
    }
    

    
    /**
     * default ThreadObjectFactory(nebsoa.util.threadpool.ThreadObjectFactory)를 
     * 사용하여 thread pool을 생성합니다.
     * thread 최대 idle 2개, min idle  갯수만큼의 thread pool을 생성합니다.
     */
    public ThreadPool makeThreadPool(String poolId){
        return makeThreadPool(poolId,new ThreadObjectFactory(),2,0);
    }
    
    
    /**
     * 종료 될 때 모든 thread pool을 close한다.
     
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(){
           public void run(){
               System.out.println("close all thread pool ###################################");
               getInstance().logThreadPoolState();
               getInstance().closeAll();
           }
        });
    }
    */

    
    public static void main(String args[]){
        
    
    }


    public void logThreadPoolState() {
        Set set = getThreadPoolHash().keySet();
        if (set.isEmpty())
            return;
        Iterator i = set.iterator();
        ThreadPool pool=null;
        while (i.hasNext()) {
            pool = (ThreadPool) getThreadPoolHash().get(i.next());
            try {
                System.out.println(pool.getInfo());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }      
    }
    
    public void finalize(){
        getInstance().closeAll();
    }

}
