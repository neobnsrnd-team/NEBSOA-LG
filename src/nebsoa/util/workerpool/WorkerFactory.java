/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.util.workerpool;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.pool.PoolableObjectFactory;

import nebsoa.common.log.LogManager;

/*******************************************************************
 * <pre>
 * 1.설명 
 * Worker를 생성하는 factory이며 org.apache.commons.pool.PoolableObjectFactory를
 * wrapping하여 사용한다.
 * 
 * 2.사용법
 * WorkerFactory factory = new WorkerFactory(ip,port,timeout,"DEBUG");
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
 * $Log: WorkerFactory.java,v $
 * Revision 1.1  2018/01/15 03:39:52  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:09  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
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
public abstract class WorkerFactory implements PoolableObjectFactory {
    /**
     * 이 factory와 연괸된 pool;
     */
    protected WorkerPool pool;
    
    protected Map config=new HashMap();

    public WorkerFactory(){

    }

    public void setWorkerPool(WorkerPool pool){
        this.pool = pool;
    }
    
    public WorkerPool getWorkerPool(){
        if(pool==null){
            LogManager.error("WorkerFactory's Worker pool is null");
        }
        return pool;
    }
 
    /***
     * makeObject
     * @throws IOException 
     * @throws ClassNotFoundException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     */
    public Object makeObject() throws Exception{
       Worker worker=null;
       try {
           worker = createWorker();//new Worker();
           if(pool != null){
               worker.setPool(pool);
               worker.setThreadMode(pool.getThreadMode());
           }
           return worker;
       } catch (Exception e) {
           LogManager.error(e.toString(),e);
           throw e;          
       }     
    }
    /**
     * WorkerFactory를 상속받은 클래스에서 구현해야 한다.
     * 2006. 10. 3.  이종원 작성
     * @return
     */
    public abstract Worker createWorker();

/**
    *  destroyObject
    */
    public void destroyObject(Object obj) {
         ((Worker)obj).destroy();
    }

   /**
    * validate socket
    */
    public boolean validateObject(Object obj) {
       //LogManager.debug("validateObject-----------------");
        if (obj instanceof Worker) {
            Worker worker = (Worker) obj;
            if(worker.getExeTime() > 2000){
                LogManager.info("TUNNING_LIST","수행시간 감시 :"+worker);
            }
          
            return true;
        }
        return false;
    }

   /**
    * activateObject
    */
   public void activateObject(Object obj) {

   }

   /**
    * passivateObject.go into the pool
    */
    public void passivateObject(Object obj) {
        if (obj instanceof Worker) {
            Worker worker=(Worker)obj;
            worker.reset();
        }
    }

}