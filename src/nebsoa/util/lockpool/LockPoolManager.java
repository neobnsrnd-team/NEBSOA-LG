/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.util.lockpool;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import nebsoa.common.exception.SysException;
import nebsoa.common.exception.TooManyRequestException;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.PropertyManager;

/*******************************************************************
 * <pre>
 * 1.설명 
 * <code>LockPoolManager</code>는 시스템에서 사용 하는 모든 Lock pool를 관리합니다.
 * <code>LockPoolManager</code>는
 * 
 * <code>LockPoolManager</code>의 getPool("key이름")을 이용하여
 * Pool객체를  얻을 수 있습니다. 
 * 
 * 2.사용법
 * <blockquote>
 * 
 *  LockPool pool = LockPoolManager.getInstance().getPool(id);
 *  if(pool==null){
 *      LockFactory factory = new LockFactory();
 *      pool=LockPoolManager.getInstance().makeLockPool(
 *              id,factory,maxLockCount,maxWaitTime);
 *  }
 *
 *  LockLock worker =null;
 *  try {
 *
 *
 *    worker = (LockLock) pool.borrowObject();
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
 * @author 이종원
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 
 * $Log: LockPoolManager.java,v $
 * Revision 1.1  2018/01/15 03:39:50  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:50  cvs
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
 * Revision 1.1  2008/08/04 08:54:52  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:25  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.2  2008/01/02 09:43:11  김성균
 * WORK_SPACE 정보 사용할 수 있도록 수정
 *
 * Revision 1.1  2007/11/26 08:38:38  안경아
 * *** empty log message ***
 *
 * Revision 1.4  2007/05/10 07:30:29  안경아
 * *** empty log message ***
 *
 * Revision 1.3  2007/05/02 01:04:36  이종원
 * 설정에서 max active갯수를 변경하였을 때 바로 반영 되도록 수정
 *
 * Revision 1.2  2007/05/01 07:39:58  김성균
 * Lock 처리 기능 추가
 *
 * Revision 1.1  2007/05/01 06:40:35  이종원
 * 최초작성
 *
 *
 * </pre>
 ******************************************************************/
public class LockPoolManager {
    
    static Object dummy = new Object();
    
    public static LockPoolManager instance=null;
    
    private static final String LOCKPOOL_REPOSITORY_TYPE = PropertyManager.getProperty("lockpool", "LOCKPOOL_CONFIG_REPOSITORY_TYPE", "XML");

    private static final String DB_TYPE = "DB";
    
    /**
     * WORK_SPACE 정보를 가지고 있는 객체들의 풀 
     */
    private Map workSpacePool;
    
    public static LockPoolManager getInstance(){
        if(instance==null){
            synchronized (dummy) {
                if(instance==null){
                    instance = new LockPoolManager();
                }
            }
        }
        return instance;
    }
    
    
    /**
     * Lock Pool을  캐쉬 해 놓기 위한 HashMap입니다. 
     */
    private HashMap poolHash = new HashMap();
    
    /**
     * Lock이 없을시 wait하지 않는  Lock Pool 얻어 옵니다 .
     * <font color='red'>만약 등록된 Lock Pool이 없을때는 새로 생성하여  return 합니다.
     * </font>
     */
    public LockPool getLockPool(String poolId, int maxActive) {
        LockPool pool = (LockPool) poolHash.get(poolId);
        if (pool == null) {
            pool = makeLockPool(poolId,new LockFactory(),maxActive,0);
        }
        return pool;
    }
    
    /**
     * Lock이 없을시 지정된 시간 만큼 기다리는  Lock Pool 얻어 옵니다 .
     * <font color='red'>만약 등록된 Lock Pool이 없을때는 새로 생성하여  return 합니다.
     * </font>
     */
    public LockPool getLockPool(String poolId,int maxActive,int waitSecond) {
        LockPool pool = (LockPool) poolHash.get(poolId);
        if (pool == null) {
            pool = makeLockPool(poolId,new LockFactory(),maxActive,waitSecond);
        }
        if(pool.getMaxActive() < maxActive){
            pool.setMaxActive(maxActive);
        }else if(pool.getMaxActive() > maxActive){
        	close(poolId);
            pool = makeLockPool(poolId,new LockFactory(),maxActive,waitSecond);             
        }        
        return pool;
    }
    /**
     * Lock pool을 close합니다.
     */
    public void closeAll() {
        if(poolHash==null) return;
        Set set = poolHash.keySet();
        if (set.isEmpty())
            return;
        Iterator i = set.iterator();
        LockPool pool=null;
        while (i.hasNext()) {
            pool = (LockPool) poolHash.get(i.next());
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
     * Lock pool을 close합니다.
     */
    public void close(String poolId) {
        synchronized(this){
            LockPool pool= (LockPool) poolHash.get(poolId);
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
     * 받은 LockObjectFactory를 사용하여 Lock pool을 생성합니다.
     * 지정된 갯수만큼의 Lock pool을 생성합니다.
     * <font color='red'>
     * maxActive:최대 허용 소켓 수 ,
     * maxWaitWhenBusy : 폭주시 최대 기다림 시간 (밀리세컨드),
     * threadMode가 false이면 한번에 한건씩만 처리 가능합니다.
     * (Async일 때는 true로 세팅해야만 async로 동작 할 수 있습니다.)
     */
    public synchronized LockPool makeLockPool(String poolId,
            LockFactory factory,
            int maxActive, int maxWaitWhenBusy){
//            boolean testOnBorrow, int intervalThreshold,boolean usePooing ) {
        if (poolId == null) {
            throw new SysException("poolId is null");
        }

        LockPool pool = (LockPool) poolHash.get(poolId);
 
        if (pool == null) {
            if(maxWaitWhenBusy==0){
                pool = new LockPool(factory,maxActive);
            }else{
                pool = new LockPool(factory,maxActive,maxWaitWhenBusy);
            }
            
            pool.setName(poolId);
            poolHash.put(poolId,pool);
            LogManager.debug(poolId +"로  Lock pool을 등록합니다."+pool.getInfo());
        }
        return pool;
    }
 
    public void logLockPoolState() {
        Set set = poolHash.keySet();
        if (set.isEmpty())
            return;
        Iterator i = set.iterator();
        LockPool pool=null;
        while (i.hasNext()) {
            pool = (LockPool) poolHash.get(i.next());
            try {
                System.out.println(pool.getInfo());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }      
    }
    
    public Lock getLock(String poolConfigPrefix, String id) {
        String poolId = poolConfigPrefix + "@" + id; 
        int maxActive = getThreadCount(poolId);
        
        Lock lock = null;
        if (maxActive > 0) {
            try {
                lock = getLockPool(poolId, maxActive, 0).getLock();
            } catch (TooManyRequestException e) {
                throw e;
            } catch (Exception e) {
                LogManager.error("ERROR", "LockPool 부터 Lock 객체를 얻지 못함;"+e.toString());
                throw new SysException("FRS99999", e.toString());
            }
        }
        return lock;
    }
    
    /**
     * 동시에 실행가능한 쓰레드수를 얻어온다.
     * @param workSpaceId
     * @return 쓰레드수
     */
    private int getThreadCount(String workSpaceId) {
        int threadCount = 0;
        if (DB_TYPE.equalsIgnoreCase(LOCKPOOL_REPOSITORY_TYPE)) {
            WorkSpace workSpace = (WorkSpace) workSpacePool.get(workSpaceId);
            if (workSpace != null) {
                threadCount = workSpace.getThreadCount();
            }
        } else {
            threadCount = PropertyManager.getIntProperty("lockpool", workSpaceId, 0);
        }
        return threadCount;
    }
    
    public void returnLock(String poolConfigPrefix, String id, Lock lock) {
        if (lock == null) return;
        
        String poolId = poolConfigPrefix + "@" + id; 
        int maxActive = PropertyManager.getIntProperty("lockpool", poolId, 0);
        
        if (maxActive > 0) {
            try {
                getLockPool(poolId, maxActive).returnLock(lock);
            } catch (Exception e) {
                LogManager.error("ERROR", "LockPool 에 Lock 객체를 반납하지 못함;"+e.toString());
                throw new SysException("FRS99999", e.toString());
            }
        }
    }

}
