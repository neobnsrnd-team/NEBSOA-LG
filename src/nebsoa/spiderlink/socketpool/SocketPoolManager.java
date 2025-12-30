/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.socketpool;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;

/*******************************************************************
 * <pre>
 * 1.설명 
 * <code>SocketPoolManager</code>는 시스템에서 사용 하는 모든 socket pool를 관리합니다.
 * <code>SocketPoolManager</code>는
 * 
 * <code>SocketPoolManager</code>의 getPool("key이름")을 이용하여
 * Pool객체를  얻을 수 있습니다. 
 * 
 * 2.사용법
 * <blockquote>
 * 
 * SocketPool pool = SocketPoolManager.getInstance().getPool(ip+"_"+port);
 *  if(pool==null){
 *      SocketFactory factory = new SocketFactory(ip,port,timeout,"DEBUG");
 *      pool=SocketPoolManager.getInstance().makeSocketPool(
 *              ip+"_"+port,factory,maxIdle,initCount);
 *  }
 *  <font color='red'>위에서 maxIdle값에 1 이나, 0을 넣으면 pooling을 하지 않고 
 *  매번 생성하게 된다.</font> 
 * WorkerSocket worker =null;
 *  try {
 *
 *
 *    worker = (WorkerSocket) pool.borrowObject();
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
 * $Log: SocketPoolManager.java,v $
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
 * Revision 1.1  2008/01/22 05:58:24  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:37:41  안경아
 * *** empty log message ***
 *
 * Revision 1.5  2007/07/30 03:00:30  김성균
 * closeAll() 동기화 처리
 *
 * Revision 1.4  2007/02/26 11:00:19  이종원
 * *** empty log message ***
 *
 * Revision 1.3  2007/02/26 10:57:32  이종원
 * closeAll로직 수정
 *
 * Revision 1.2  2006/10/16 05:00:50  이종원
 * *** empty log message ***
 *
 * Revision 1.1  2006/09/27 10:22:53  이종원
 * 디렉토리변경
 *
 * Revision 1.8  2006/09/27 07:34:05  이종원
 * test on borrow제거
 *
 * Revision 1.7  2006/09/27 03:33:51  이종원
 * default로 pooling false세팅
 *
 * Revision 1.6  2006/09/27 03:07:01  이종원
 * socket count하는 로직 수정 및 read bug수정
 *
 * Revision 1.5  2006/09/16 13:11:56  이종원
 * update
 *
 * Revision 1.4  2006/09/13 12:28:14  김승희
 * usePooling 속성에 따라 풀링할 지 결정 추가
 *
 * Revision 1.3  2006/06/17 10:16:52  오재훈
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class SocketPoolManager {
    
    public static SocketPoolManager instance=null;
    
    public static SocketPoolManager getInstance(){
        if(instance==null){
            instance = new SocketPoolManager();
        }
        return instance;
    }
    
    
    /**
     * socket Pool을  캐쉬 해 놓기 위한 HashMap입니다. 
     */
    private HashMap poolHash = new HashMap();
    
    /**
     * 등록된 threadPool 얻어 옵니다 .
     * <font color='red'>만약 등록된 socket Pool이 없을때는 null을 return 합니다.
     * </font>
     */
    public SocketPool getPool(String poolId) {
        SocketPool pool = (SocketPool) poolHash.get(poolId);
        if (pool == null) {
            LogManager.debug(poolId +"로 등록된  socket pool이 없습니다.");
        }
        return pool;
    }
    

    /**
     * socket pool을 close합니다.
     */
    public void closeAll() {
        synchronized (this) {
            if (poolHash == null)
                return;
            Set set = poolHash.keySet();
            if (set.isEmpty())
                return;
            LogManager.infoLP("close All Socket Pool start");
            Iterator i = set.iterator();
            SocketPool pool = null;
            while (i.hasNext()) {
                pool = (SocketPool) poolHash.get(i.next());
                try {
                    if (pool != null && !pool.closed())
                        pool.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            poolHash.clear();
            LogManager.infoLP("close All Socket Pool end");
        }        
    }
    
    /**
     * socket pool을 close합니다.
     */
    public void close(String poolId) {
        synchronized(this){
            SocketPool pool= (SocketPool) poolHash.get(poolId);
            if(pool != null ) {       
                try {
                    if(!pool.closed()) pool.close();
                    poolHash.remove(poolId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * 받은 SocketObjectFactory를 사용하여 socket pool을 생성합니다.
     * 지정된 갯수만큼의 socket pool을 생성합니다.
     * <font color='red'>
     * maxActive:최대 허용 소켓 수 ,
     * maxWaitWhenBusy : 폭주시 최대 기다림 시간 (밀리세컨드),
     * usePooing가 false이면 pooing하지 않고 매번 생성 및 close해  버립니다.
     */
    public synchronized SocketPool makeSocketPool(String poolId,
            SocketFactory factory,
            int maxActive, int maxWaitWhenBusy,boolean usePooing){
//            boolean testOnBorrow, int intervalThreshold,boolean usePooing ) {
        if (poolId == null) {
            throw new SysException("poolId is null");
        }

        SocketPool pool = (SocketPool) poolHash.get(poolId);
 
        if (pool == null) {
            pool = new SocketPool(factory,maxActive,maxWaitWhenBusy);
            pool.setName(poolId);
/**
            pool.setTestOnBorrow(testOnBorrow);
            if(testOnBorrow){
                pool.setIntervalThreshold(intervalThreshold);
            }
**/
            pool.setUsePooling(usePooing);
            
            factory.setSocketPool(pool);
            poolHash.put(poolId,pool);
            LogManager.debug(poolId +"로  socket pool을 등록합니다.");
        }
        return pool;
    }
    

    /**
     * 인자로 받은 SocketObjectFactory를 사용하여 
     * 지정된 maxActive , maxWaitWhenBusy로 세팅된 풀을  생성합니다.
     * <font color='red'>풀링을 사용하지 않도록 세팅 됩니다.</font>
     */
    public SocketPool makeSocketPool(String poolId,SocketFactory factory
            , int maxActive, int maxWaitWhenBusy){
        return makeSocketPool(poolId,factory,maxActive,maxWaitWhenBusy,
                false);
    }
    
    /**
     * 받은 SocketObjectFactory를 사용하여 socket pool을 생성합니다.
     * 최대 허용 수 (maxActive)5  ,폭주시 socket 할당을 못받을 경우 최대 30초 간 
     * 기다리도록 세팅 하여  socket pool을 생성합니다.
     * <font color='red'>풀링을 사용하지 않도록 세팅 됩니다.</font>
     */
    public SocketPool makeSocketPool(String poolId,SocketFactory factory
           , int maxActive){
        return makeSocketPool(poolId,factory,maxActive,30*1000);
    }
    
    /**
     * 받은 SocketObjectFactory를 사용하여 socket pool을 생성합니다.
     * 최대 허용 수 (maxActive)5  ,폭주시 socket 할당을 못받을 경우 최대 30초 간 
     * 기다리도록 세팅 하여  socket pool을 생성합니다.
     * <font color='red'>풀링을 사용하지 않도록 세팅 됩니다.</font>
     */
    public SocketPool makeSocketPool(String poolId,SocketFactory factory){
        return makeSocketPool(poolId,factory,5);
    }
    /**
     * 받은 SocketObjectFactory를 사용하여 socket pool을 생성합니다.
     * 최대 허용 수 (maxActive)5  ,폭주시 socket 할당을 못받을 경우 최대 30초 간 
     * 기다리도록 세팅 하여  socket pool을 생성합니다.
     * <font color='red'>풀링을 사용하지 않도록 세팅 됩니다.</font>
     */
    public SocketPool makePoolingSocketPool(String poolId,SocketFactory factory
           , int maxActive){
        return makeSocketPool(poolId,factory,maxActive,30*1000,true);
    }    
    /**
     * 인자로 받은 SocketObjectFactory를 사용하여 socket pool을 생성합니다.
     * 인자로 받은 최대 허용 수 (maxActive)  ,폭주시 socket 할당을 못받을 경우 최대 30초 간 
     * 기다리도록 세팅 하여  socket pool을 생성합니다.
     * <font color='red'>풀링을 사용하도록 세팅 됩니다.</font>
     */
    public SocketPool makePoolingSocketPool(String poolId,SocketFactory factory){
        return makeSocketPool(poolId,factory,5,30*1000,true);
    }
    
    
    /**
     * 종료 될 때 모든 socket pool을 close한다.
     */
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(){
           public void run(){
               //getInstance().logSocketPoolState();
               getInstance().closeAll();
           }
        });
    }

    
    public static void main(String args[]){
        
    
    }


    public void logSocketPoolState() {
        Set set = poolHash.keySet();
        if (set.isEmpty())
            return;
        Iterator i = set.iterator();
        SocketPool pool=null;
        while (i.hasNext()) {
            pool = (SocketPool) poolHash.get(i.next());
            try {
                System.out.println(pool.getInfo());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }      
    }

}
