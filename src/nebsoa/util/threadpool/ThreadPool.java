/*
 * Spider Framework
 *
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 *
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.util.threadpool;

//import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.commons.pool.impl.StackObjectPool;

import nebsoa.common.log.LogManager;

/*******************************************************************
 * <pre>
 * 1.설명
 * Thread pool을 구현한 클래스이며,  org.apache.commons.pool.impl.StackObjectPool을
 * wrapping하여 사용한다.
 *
 * 2.사용법
 *  ThreadPool myPool = ThreadPoolManager.getInstance().getPool("MyPool");
 *  if(myPool==null){
 *      myPool = ThreadPoolManager.getInstance().makeThreadPool(ThreadObjectFactory factory, 최대갯수, 최초 갯수);
 *  }
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
 * $Log: ThreadPool.java,v $
 * Revision 1.1  2018/01/15 03:39:50  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:45  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.6  2010/02/08 06:42:34  유정수
 * Thread Monitor 로그 다시 사용하도록 수정
 *
 * Revision 1.5  2010/01/19 10:49:00  유정수
 * Thread재사용 없이 한번사용후 thread stop하도록 수정
 *
 * Revision 1.2  2009/07/16 03:22:30  김보라
 * 로그보강
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
 * Revision 1.13  2007/06/07 09:22:38  이종원
 * *** empty log message ***
 *
 * Revision 1.12  2007/03/30 14:57:10  이종원
 * thread pool close하는 부분 trace하는 로그 추가
 *
 * Revision 1.11  2007/03/19 07:26:41  김성균
 * Pool로부터 자원얻는 시가 Monitor 로그 추가
 *
 * Revision 1.10  2006/10/03 03:15:16  이종원
 * *** empty log message ***
 *
 * Revision 1.9  2006/10/02 13:19:38  이종원
 * *** empty log message ***
 *
 * Revision 1.8  2006/10/02 02:46:47  이종원
 * *** empty log message ***
 *
 * Revision 1.7  2006/09/29 00:25:30  이종원
 * *** empty log message ***
 *
 * Revision 1.6  2006/09/27 08:23:55  이종원
 * *** empty log message ***
 *
 * Revision 1.5  2006/07/28 16:02:27  이종원
 * *** empty log message ***
 *
 * Revision 1.4  2006/06/17 10:17:16  오재훈
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class ThreadPool extends StackObjectPool{
                                //GenericObjectPool {

    String name;


   /**
    * default로 maxIdle:5 세팅된 thread pool 생성
    */
   public ThreadPool(ThreadObjectFactory objFactory) {
      //this(objFactory,2,5,1000);
       this(objFactory,5,0); //MaxIdle:5
   }

   /**
    *  maxIdle, maxActive, maxWait 값을 인자로 받아 thread pool 생성
    */
   public ThreadPool(ThreadObjectFactory objFactory,int maxIdle,
           int initCapacity){

      //     int maxActive, int maxWait) {
 /**     super(objFactory);
      this.setMinIdle(5); // minimun idle threads
      this.setMaxIdle(maxIdle); // maximum idle threads
      this.setMaxActive(maxActive); // maximum active threads.
      this.setMinEvictableIdleTimeMillis(30000); //Evictor runs every 30 secs.
      this.setTestOnBorrow(true); // check if the thread is still valid
      this.setMaxWait(maxWait); // second wait when threads are not available.
*/

       super(objFactory,maxIdle,initCapacity);

       prepare(initCapacity);
   }

   /**
    *  maxIdle, maxActive, maxWait 값을 인자로 받아 thread pool 생성
    */
   public ThreadPool(ThreadObjectFactory objFactory,int maxIdle){
       this(objFactory,maxIdle,0);
   }

   /* (non-Javadoc)
    * @see org.apache.commons.pool.ObjectPool#borrowObject()
    */
   public synchronized Object borrowObject() throws Exception {
       LogManager.debug(">>> borrowing object..");
       long start = System.currentTimeMillis();
       Object obj=super.borrowObject();
       long end = System.currentTimeMillis()-start;

       LogManager.debug("★★ ThreadPool로부터 Thead얻는시간 : " + (end/1000.0)+"초.필요시 Thread갯수를 늘려주십시오.\n"+getInfo());

       if(end > 500){
           LogManager.info("MONITOR","ThreadPool로부터 Thread얻는시간:"
                   +(end/1000.0)+"초 .필요시 Thread갯수를 늘려주십시오.\n"+getInfo());
       }

       return obj;
   }

   /* (non-Javadoc)
    * @see org.apache.commons.pool.ObjectPool#returnObject(java.lang.Object)
    */
   public synchronized void returnObject(Object obj) throws Exception {
       //LogManager.debug("<<< returning object.." + obj);
       //super.returnObject(obj);
	   super.invalidateObject(obj);
   }

   /**
    * borrowObjects - Helper method, use this carefully since this could be
    * a blocking call when the threads requested may not be avialable based
    * on the configuration being used.
    * @param num
    * @return WorkerThread[]
    */
   public synchronized WorkerThread[] borrowObjects(int num) {
      WorkerThread[] rtArr = new WorkerThread[num];
      for (int i = 0; i < num; i++) {
         WorkerThread rt;
         try {
            rt = (WorkerThread) borrowObject();
            rtArr[i] = rt;
         } catch (Exception e) {
            LogManager.error("thread pool:: borrowObjects failed.. ", e);
         }
      }
      return rtArr;
   }

   public synchronized void returnObjects(WorkerThread[] rtArr) {
       int num=rtArr.length;
       for (int i = 0; i < num; i++) {
          try {
             returnObject( rtArr[i]);
          } catch (Exception e) {
             LogManager.error("thread pool:: returnObjects failed.. ", e);
          }
       }
    }
   /**
    * 미리 사용할 갯수 만큼의 쓰레드를 만든다.
    * 2006. 7. 29.  이종원 작성
    */
   private void prepare(int initCapacity){
       if(initCapacity>0){
           if(initCapacity > 2){
               initCapacity=2;
           }
           returnObjects(borrowObjects(initCapacity));
       }
   }
    public boolean isRunning(){
        return getNumActive() > 0;
    }

    public boolean closed(){
        return super.isClosed();
    }

    public void close() throws Exception{
//        LogManager.error("MONITOR","ThreadPool을 close합니다.",
//                new Exception("이것은 오류가 아닙니다.trace용입니다"));
        super.close();
    }

    public String toString(){
        return getInfo();
    }

    public String getInfo() {
        return "\tThreadPool:["+name
        +"] 수행중인 쓰레드:["+getNumActive()
        +"] 쉬는 쓰레드:["+getNumIdle()+"]";

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}