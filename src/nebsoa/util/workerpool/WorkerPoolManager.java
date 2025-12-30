/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.util.workerpool;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;

/*******************************************************************
 * <pre>
 * 1.설명 
 * <code>WorkerPoolManager</code>는 시스템에서 사용 하는 모든 Worker pool를 관리합니다.
 * <code>WorkerPoolManager</code>는
 * 
 * <code>WorkerPoolManager</code>의 getPool("key이름")을 이용하여
 * Pool객체를  얻을 수 있습니다. 
 * 
 * 2.사용법
 * <blockquote>
 * 
 * WorkerPool pool = WorkerPoolManager.getInstance().getPool(ip+"_"+port);
 *  if(pool==null){
 *      WorkerFactory factory = new WorkerFactory(ip,port,timeout,"DEBUG");
 *      pool=WorkerPoolManager.getInstance().makeWorkerPool(
 *              ip+"_"+port,factory,maxIdle,initCount);
 *  }
 *  <font color='red'>위에서 maxIdle값에 1 이나, 0을 넣으면 pooling을 하지 않고 
 *  매번 생성하게 된다.</font> 
 * WorkerWorker worker =null;
 *  try {
 *
 *
 *    worker = (WorkerWorker) pool.borrowObject();
 *
 *    String testStr="test_text";
 *    // write and receive data ...
 *    testWriteAndRead(testStr,worker.getDataOutputStream(),
 *            worker.getDataInputStream());    
 *    
 * } catch (Exception e) {
 *     if(worker != null){
 *        worker.setError(true);
 *     }
 *     LogManager.error(e.getMessage(), e);
 * <font color='red'>     
 * }finally{
 *     try {
 *         pool.returnObject(worker);
 *     } catch (Exception e) {
 *         System.out.println("fail to return object");
 *     }
 * }
 * </font>
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
 * $Log: WorkerPoolManager.java,v $
 * Revision 1.1  2018/01/15 03:39:52  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:10  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.2  2009/07/16 03:21:49  김보라
 * 폭주시 worker얻는 시간을 1초로 단축시켜서 바로 에러로 떨어지도록 한다. 30초동안 기다리지 않음.
 *
 * Revision 1.1  2008/11/18 11:27:25  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:28  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:52  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:27  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:26  안경아
 * *** empty log message ***
 *
 * Revision 1.4  2007/02/26 11:12:47  이종원
 * *** empty log message ***
 *
 * Revision 1.3  2007/02/26 10:51:11  이종원
 * *** empty log message ***
 *
 * Revision 1.2  2006/10/03 00:26:25  이종원
 * *** empty log message ***
 *
 * Revision 1.1  2006/10/02 15:41:40  이종원
 * 최초작성
 *
 * Revision 1.1  2006/10/02 15:40:48  이종원
 * 기본 기능 구현
 *
 * </pre>
 ******************************************************************/
public class WorkerPoolManager {
    
    public static WorkerPoolManager instance=null;
    
    public static WorkerPoolManager getInstance(){
        if(instance==null){
            instance = new WorkerPoolManager();
        }
        return instance;
    }
    
    
    /**
     * Worker Pool을  캐쉬 해 놓기 위한 HashMap입니다. 
     */
    private HashMap poolHash = new HashMap();
    
    /**
     * 등록된 threadPool 얻어 옵니다 .
     * <font color='red'>만약 등록된 Worker Pool이 없을때는 새로 생성하여  return 합니다.
     * </font>
     */
    public WorkerPool getWorkerPool(String poolId,WorkerFactory factory,
            int maxActive,boolean threadMode) {
        WorkerPool pool = (WorkerPool) poolHash.get(poolId);
        if (pool == null) {
            //pool = makeWorkerPool(poolId,factory,maxActive,30,threadMode);
        	//2009-07-08 폭주시 worker얻는 시간을 1초로 단축시켜서 바로 에러로 떨어지도록 한다. 30초동안 기다리지 않음.
        	pool = makeWorkerPool(poolId,factory,maxActive,1,threadMode);
        }
        return pool;
    }
    

    /**
     * Worker pool을 close합니다.
     */
    public void closeAll() {
        if(poolHash==null) return;
        Set set = poolHash.keySet();
        if (set.isEmpty())
            return;
        Iterator i = set.iterator();
        WorkerPool pool=null;
        while (i.hasNext()) {
            pool = (WorkerPool) poolHash.get(i.next());
            try {
                if(!pool.closed())
                pool.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        poolHash.clear();
    }
    
    /**
     * Worker pool을 close합니다.
     */
    public void close(String poolId) {
        synchronized(this){
            WorkerPool pool= (WorkerPool) poolHash.get(poolId);
            if(pool != null ) {       
                try {
                    if(!pool.closed()) pool.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }finally{
                    try{
                        poolHash.remove(poolId);
                    }catch(Exception e){}
                }
            }
        }
    }
    /**
     * 받은 WorkerObjectFactory를 사용하여 Worker pool을 생성합니다.
     * 지정된 갯수만큼의 Worker pool을 생성합니다.
     * <font color='red'>
     * maxActive:최대 허용 소켓 수 ,
     * maxWaitWhenBusy : 폭주시 최대 기다림 시간 (밀리세컨드),
     * threadMode가 false이면 한번에 한건씩만 처리 가능합니다.
     * (Async일 때는 true로 세팅해야만 async로 동작 할 수 있습니다.)
     */
    public synchronized WorkerPool makeWorkerPool(String poolId,
            WorkerFactory factory,
            int maxActive, int maxWaitWhenBusy,boolean threadMode){
//            boolean testOnBorrow, int intervalThreshold,boolean usePooing ) {
        if (poolId == null) {
            throw new SysException("poolId is null");
        }

        WorkerPool pool = (WorkerPool) poolHash.get(poolId);
 
        if (pool == null) {
            pool = new WorkerPool(factory,maxActive,maxWaitWhenBusy);
            pool.setName(poolId);

            pool.setThreadMode(threadMode);
            
            factory.setWorkerPool(pool);
            poolHash.put(poolId,pool);
            LogManager.debug(poolId +"로  Worker pool을 등록합니다.");
        }
        return pool;
    }
    

    /**
     * 인자로 받은 WorkerObjectFactory를 사용하여 
     * 지정된 maxActive , maxWaitWhenBusy로 세팅된 풀을  생성합니다.
     * <font color='red'>쓰레드 풀을 사용하지 않도록 세팅 됩니다.</font>
     */
    public WorkerPool makeWorkerPool(String poolId,WorkerFactory factory
            , int maxActive, int maxWaitWhenBusy){
        return makeWorkerPool(poolId,factory,maxActive,maxWaitWhenBusy,
                false);
    }
    
    /**
     * 받은 WorkerObjectFactory를 사용하여 Worker pool을 생성합니다.
     * 최대 허용 수 (maxActive)5  ,폭주시 Worker 할당을 못받을 경우 최대 30초 간 
     * 기다리도록 세팅 하여  Worker pool을 생성합니다.
     * <font color='red'>쓰레드로 동작하게 세팅 됩니다..</font>
     */
    public WorkerPool makeWorkerPool(String poolId,WorkerFactory factory
           , int maxActive){
        return makeWorkerPool(poolId,factory,maxActive,30*1000);
    }
    
    /**
     * 받은 WorkerObjectFactory를 사용하여 Worker pool을 생성합니다.
     * 최대 허용 수 (maxActive)5  ,폭주시 Worker 할당을 못받을 경우 최대 30초 간 
     * 기다리도록 세팅 하여  Worker pool을 생성합니다.
     * <font color='red'>쓰레드 풀을 사용하지 않도록 세팅 됩니다.</font>
     */
    public WorkerPool makeWorkerPool(String poolId,WorkerFactory factory){
        return makeWorkerPool(poolId,factory,5);
    }
    /**
     * 받은 WorkerObjectFactory를 사용하여 Worker pool을 생성합니다.
     * 최대 허용 수 (maxActive)5  ,폭주시 Worker 할당을 못받을 경우 최대 30초 간 
     * 기다리도록 세팅 하여  Worker pool을 생성합니다.
     * <font color='red'>쓰레드 풀을 사용하도록 세팅 됩니다.</font>
     */
    public WorkerPool makeThreadedWorkerPool(String poolId,WorkerFactory factory
           , int maxActive){
        return makeWorkerPool(poolId,factory,maxActive,30*1000,true);
    }    
    /**
     * 인자로 받은 WorkerObjectFactory를 사용하여 Worker pool을 생성합니다.
     * 인자로 받은 최대 허용 수 (maxActive)  ,폭주시 Worker 할당을 못받을 경우 최대 30초 간 
     * 기다리도록 세팅 하여  Worker pool을 생성합니다.
     * <font color='red'>쓰레드 풀을 사용하도록 세팅 됩니다.</font>
     */
    public WorkerPool makeThreadedWorkerPool(String poolId,WorkerFactory factory){
        return makeWorkerPool(poolId,factory,5,30*1000,true);
    }
    
    
    public static void main(String args[]){
        
    
    }


    public void logWorkerPoolState() {
        Set set = poolHash.keySet();
        if (set.isEmpty())
            return;
        Iterator i = set.iterator();
        WorkerPool pool=null;
        while (i.hasNext()) {
            pool = (WorkerPool) poolHash.get(i.next());
            try {
                System.out.println(pool.getInfo());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }      
    }

}
