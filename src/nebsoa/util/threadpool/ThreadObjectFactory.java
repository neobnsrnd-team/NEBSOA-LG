/*
 * Spider Framework
 *
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 *
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.util.threadpool;

import org.apache.commons.pool.PoolableObjectFactory;

import nebsoa.common.log.LogManager;

/*******************************************************************
 * <pre>
 * 1.설명
 * Thread를 생성하는 factory이며 org.apache.commons.pool.PoolableObjectFactory를
 * wrapping하여 사용한다.
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
 * $Log: ThreadObjectFactory.java,v $
 * Revision 1.1  2018/01/15 03:39:50  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:45  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.3  2010/01/19 10:49:00  유정수
 * Thread재사용 없이 한번사용후 thread stop하도록 수정
 *
 * Revision 1.2  2010/01/19 09:42:29  유정수
 * *** empty log message ***
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
 * Revision 1.6  2006/10/02 13:19:38  이종원
 * *** empty log message ***
 *
 * Revision 1.5  2006/09/27 08:23:55  이종원
 * *** empty log message ***
 *
 * Revision 1.4  2006/06/17 10:17:16  오재훈
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class ThreadObjectFactory implements PoolableObjectFactory {
    /**
     * 이 factory와 연괸된 pool;
     */
    protected ThreadPool pool;

    public void setThreadPool(ThreadPool pool){
        this.pool = pool;
    }

    public ThreadPool getThreadPool(){
        if(pool==null){;
            LogManager.debug("pool is null");
        }
        return pool;
    }

   /***
    * makeObject
    */
    public Object makeObject() {
        Object obj = new WorkerThread();
        if(pool != null) {
            WorkerThread worker=(WorkerThread)obj;
            worker.setPool(pool);
        }

        LogManager.debug(" makeObject..." + obj);

        return obj;
    }

   /**
    *  destroyObject
    */
    public void destroyObject(Object obj) {
        LogManager.debug(" destroyObject..." + obj);
        if (obj instanceof WorkerThread) {
            WorkerThread rt = (WorkerThread) obj;
            rt.setStopped(true);
            //rt.notify();
        }
    }

   /**
    * validateObject
    */
   public boolean validateObject(Object obj) {
      LogManager.debug(" validateObject..." + obj);
      if (obj instanceof WorkerThread) {
         WorkerThread rt = (WorkerThread) obj;
         if(!rt.isDone()) {  //if the thread is running the previous task, get another one.
            return false;
         }
         if (rt.isRunning()) {
            if (rt.getThreadGroup() == null) {
               return false;
            }
            return true;
         }
      }
      return true;
   }

   /**
    * activateObject
    */
   public void activateObject(Object obj) {
       if(pool != null){
           //pool.increaseRunningThreadCount();
       }
 //     LogManager.debug(" activateObject...");
   }

   /**
    * passivateObject
    */
   public void passivateObject(Object obj) {
       if(pool != null){
           //pool.decreaseRunningThreadCount();
       }
//      LogManager.debug(" passivateObject..." + obj);
      if (obj instanceof WorkerThread) {
         WorkerThread wt = (WorkerThread) obj;
         wt.setResult(null);
      }
   }

}