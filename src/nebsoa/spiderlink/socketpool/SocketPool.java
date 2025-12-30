/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.socketpool;

import java.net.ConnectException;
import java.net.UnknownHostException;

import org.apache.commons.pool.impl.GenericKeyedObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;

import nebsoa.common.log.LogConstants;
import nebsoa.common.log.LogManager;

/*******************************************************************
 * <pre>
 * 1.설명 
 * Socket pool을 구현한 클래스이며,  org.apache.commons.pool.impl.GenericObjectPool을
 * wrapping하여 사용한다.
 * 
 * 2.사용법
 * SocketPool pool = SocketPoolManager.getInstance().getPool(ip+"_"+port);
 *  if(pool==null){
 *      SocketFactory factory = new SocketFactory(ip,port,timeout,"DEBUG");
 *      pool=SocketPoolManager.getInstance().makeSocketPool(
 *              ip+"_"+port,factory,maxIdle,initCount);
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
 * $Log: SocketPool.java,v $
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
 * Revision 1.1  2008/01/22 05:58:23  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:37:41  안경아
 * *** empty log message ***
 *
 * Revision 1.22  2007/05/01 08:14:40  이종원
 * 기본 wait time 2초 설정으로 변경
 *
 * Revision 1.21  2007/03/19 07:26:41  김성균
 * Pool로부터 자원얻는 시가 Monitor 로그 추가
 *
 * Revision 1.20  2007/02/01 01:55:04  안경아
 * *** empty log message ***
 *
 * Revision 1.19  2007/01/08 06:04:11  이종원
 * socket pool update
 *
 * Revision 1.18  2007/01/03 01:19:04  이종원
 * log level 수정
 *
 * Revision 1.17  2007/01/02 05:06:44  이종원
 * *** empty log message ***
 *
 * Revision 1.16  2006/11/10 04:43:16  이종원
 * LOG CONFIG수정
 *
 * Revision 1.15  2006/10/19 15:07:00  이종원
 * 전문별 타임아웃값 설정 가능하도록 수정
 *
 * Revision 1.14  2006/10/19 14:50:50  이종원
 * 전문별로 별도 timeout값을 설정 할 수 있게 처리
 *
 * Revision 1.13  2006/10/16 04:55:22  이종원
 * *** empty log message ***
 *
 * Revision 1.12  2006/10/13 13:54:20  이종원
 * *** empty log message ***
 *
 * Revision 1.11  2006/10/13 05:11:23  이종원
 * socket refill기능 추가
 *
 * Revision 1.10  2006/10/13 03:38:34  이종원
 * 로그 주석처리
 *
 * Revision 1.9  2006/09/29 00:41:57  이종원
 * *** empty log message ***
 *
 * Revision 1.8  2006/09/29 00:17:10  이종원
 * *** empty log message ***
 *
 * Revision 1.7  2006/09/28 13:53:29  이종원
 * *** empty log message ***
 *
 * Revision 1.6  2006/09/27 14:18:28  이종원
 * *** empty log message ***
 *
 * Revision 1.5  2006/09/27 13:30:05  이종원
 * *** empty log message ***
 *
 * Revision 1.4  2006/09/27 13:23:56  이종원
 * *** empty log message ***
 *
 * Revision 1.3  2006/09/27 13:21:57  이종원
 * *** empty log message ***
 *
 * Revision 1.2  2006/09/27 11:14:02  이종원
 * *** empty log message ***
 *
 * Revision 1.1  2006/09/27 10:22:53  이종원
 * 디렉토리변경
 *
 * Revision 1.17  2006/09/27 09:08:53  이종원
 * *** empty log message ***
 *
 * Revision 1.16  2006/09/27 08:41:18  이종원
 * *** empty log message ***
 *
 * Revision 1.15  2006/09/27 07:34:05  이종원
 * test on borrow제거
 *
 * Revision 1.14  2006/09/27 03:33:51  이종원
 * default로 pooling false세팅
 *
 * Revision 1.13  2006/09/27 03:07:01  이종원
 * socket count하는 로직 수정 및 read bug수정
 *
 * Revision 1.12  2006/09/26 10:25:41  이종원
 * pooling socket update
 *
 * Revision 1.11  2006/09/26 09:01:53  이종원
 * *** empty log message ***
 *
 * Revision 1.10  2006/09/22 01:09:51  이종원
 * UPDATE
 *
 * Revision 1.9  2006/09/21 04:55:49  김승희
 * decreaseSocketCount() 메소드 error 로그 삭제
 *
 * Revision 1.8  2006/09/19 12:47:06  이종원
 * update
 *
 * Revision 1.7  2006/09/18 10:43:54  이종원
 * async 처리를 위해 기능 update
 *
 * Revision 1.6  2006/09/16 13:11:56  이종원
 * update
 *
 * Revision 1.5  2006/09/13 12:28:14  김승희
 * usePooling 속성에 따라 풀링할 지 결정 추가
 *
 * Revision 1.4  2006/06/17 10:16:52  오재훈
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class SocketPool extends //StackObjectPool{
                                GenericObjectPool {
    
    String name;
    
    SocketFactory factory;
    
    boolean usePooling=false;
    /**
     * pool로 부터 할당 받을 때 유효한 socket인지 test 수행 여부
     */
//    boolean testOnBorrow=false;
    /**
     * pool로 부터 할당 받을 때 유효한 socket인지 체크할 때 사용 할 interval값
     * 초 단위로 세팅한다. 기본 15초
     */
//    int intervalThreshold=60*1000;

   /**
    * default로 세팅된 소켓 풀을  리턴.default값은 아래와 같다. 
    * 최대 허용 소켓 수(max ActiveIdle):5, 
    * 요청 폭주시 반응 : 기다림:1 (기다림:1, 에러유발:0, 풀사이즈 증가:2  중 기다림 세팅 )
    * 기다림일 경우 기다리는 시간(밀리세컨드 ): 2초(10,000),
    */
   public SocketPool(SocketFactory objFactory) {
       this(objFactory,5,GenericKeyedObjectPool.WHEN_EXHAUSTED_BLOCK,2*1000);
   }

   /**
    * 최대 허용 소켓 수만 세팅하고 나머지는 default로 세팅된 소켓 풀을  리턴.
    * default값은 아래와 같다. 
    * 최대 허용 소켓 수(max ActiveIdle):인자로 받은 값. 
    * 요청 폭주시 반응 : 기다림:1 (기다림:1, 에러유발:0, 풀사이즈 증가:2  중 기다림 세팅 )
    * 기다림일 경우 기다리는 시간(밀리세컨드 ): 2초(2,000),
    */
   public SocketPool(SocketFactory objFactory,int maxActive) {
       this(objFactory,maxActive,GenericKeyedObjectPool.WHEN_EXHAUSTED_BLOCK,2*1000);
   }

   /**
    *  최대허용 소켓 수(maxActive),폭주시  최대 기다림 시간 ( maxWait) 값을 인자로 받아 
    *  socket pool 생성
    */
   public SocketPool(SocketFactory factory,int maxActive,int maxWaitTime){ 
       this(factory,maxActive,GenericKeyedObjectPool.WHEN_EXHAUSTED_BLOCK,maxWaitTime);
   }
   
   /**
    * 최대 허용 소켓 수(max ActiveIdle):인자로 받은 값. 
    * 요청 폭주시 반응 : 인자로 받은 값 (기다림:1, 에러유발:0, 풀사이즈 증가:2  중 기다림 세팅 )
    * 기다림일 경우 기다리는 시간(밀리세컨드 ): 인자로 받은 값을 세팅하여 소켓 풀을 만든다.
    * <font color='red'><b>만약 기다림 시간이 200(0.2초  보다 작다면 초단위로 세팅했다고 가정하여,
    * 1000을 곱하여 세팅한다.</b></font>
    * 2004.09 이종원 작성.
    */
   public SocketPool(SocketFactory factory, int maxActive, 
           byte when_exhausted_block, int maxWaitTime) {
       super(factory,maxActive,when_exhausted_block,
               (maxWaitTime<200?maxWaitTime*1000:maxWaitTime));
       super.setTestOnReturn(true);
       super.setTestWhileIdle(true);
       this.factory=factory;
   }
   

/* (non-Javadoc)
    * @see org.apache.commons.pool.ObjectPool#borrowObject()
    */
   public Object borrowObject() throws Exception {
       
//       if(!usePooling){
//           LogManager.debug(">>> not pooling.. make new socket ..");
//           return factory.makeObject();
//       }
       //LogManager.debug(">>> borrowing object..");
       Object obj=null;
       try{
           long start = System.currentTimeMillis();
           obj=super.borrowObject();
           long end = System.currentTimeMillis()-start;
           if(end > 500){
               LogManager.info("MONITOR","SocketPool로부터 Socket얻는시간:"
                       +(end/1000.0)+"초.필요시 Thread갯수를 늘려주십시오.\n"+getInfo());
           }
       }catch(ConnectException e){
           throw e;
       }catch(UnknownHostException e){
           throw e;
       }
       
       //TODO
       if (obj instanceof WorkerSocket) {
           WorkerSocket worker = (WorkerSocket) obj;
           if(worker.isStopped()){
               try{
                   super.invalidateObject(worker);
                   LogManager.error(factory.logConfig,
                           worker+" marked as 'STOPPED!!!'");
               }catch(Exception e ){
                   
               }
               return borrowObject();
           }
           
           if(usePooling){
               long now=System.currentTimeMillis();           
               if((now-worker.getWorkStartTime()) > 600*1000){
                   try{
                       super.invalidateObject(worker);
                       LogManager.debug("MONITOR",
                               worker+" 최종 사용시간이 오래 되어 버림 ["
                               +((now-worker.getWorkEndTime())/1000.0)+"초]");                       
                   }catch(Exception e){

                   }
                   return borrowObject();
               }
           }
           worker.setWorkStartTime();
           worker.setDone(false);
       }
       return obj;
   }

   /**
    * socket pool로 socket객체를 return합니다.
    */
    public void returnObject(Object obj) throws Exception {
        
        super.returnObject(obj);
        
//        if (obj instanceof WorkerSocket) {
//            WorkerSocket worker = (WorkerSocket) obj;
//            if(usePooling){
//                if(worker.isStopped()){
//                   super.invalidateObject(worker);
//                }
//            }else{
//                super.returnObject(obj);
//            }
//        }
   }

   /**
    * borrowObjects - Helper method, use this carefully since this could be
    * a blocking call when the threads requested may not be avialable based
    * on the configuration being used.
    * @param num
    * @return WorkerSocket[]
    * @throws Exception 
    */
    public synchronized WorkerSocket[] borrowObjects(int num) throws Exception {
        WorkerSocket[] rtArr = new WorkerSocket[num];
        if(usePooling){
            LogManager.debug(LogConstants.SPIDERLINK,"######worker socket refill 합니다. refill갯수 ["+num+"]개"+this);
        }else{
            num=1;
        }
        for (int i = 0; i < num; i++) {
            WorkerSocket rt; 
            rt = (WorkerSocket) borrowObject();
            rtArr[i] = rt;
        }
        return rtArr;
    }
   
    public synchronized void refillObjects(WorkerSocket[]rtArr ) throws Exception {
        //LogManager.debug("######worker socket을 채워 넣습니다. returnObject ["+rtArr.length+"]개"+this);
        for (int i = 0; i < rtArr.length; i++) {
             returnObject(rtArr[i]);
        }
        if(usePooling){
            LogManager.info(LogConstants.SPIDERLINK,"###### worker socket을 refill 하였습니다.\n"+this.getInfo());
        }
    }
    
    public boolean closed(){
        return super.isClosed();
    }

    public String toString(){
        return getInfo();
    }
    
    public String getInfo() {
        if(isClosed()) return name+" socket pool is closed...";
        return "\tSocketPool IP/Port:["+name
        +"] 수행중인 소켓:["+getNumActive()
        +"] 쉬고있는 소켓:["+getNumIdle()+"]";
    }
    
    /**
     * 열려진 소켓 갯수(active + idle)을 리턴 한다.
     * 2006. 9. 27.  이종원 작성
     * @return num of socket
     */
    public synchronized int getSocketCount(){        
        return getNumActive()+getNumIdle();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isUsePooling() {
        return usePooling;
    }

    public void setUsePooling(boolean usePooling) {
        this.usePooling = usePooling;
    }

    public SocketFactory getFactory() {
        return factory;
    }

    public void setFactory(SocketFactory factory) {
        this.factory = factory;
    }
    

//    public int getIntervalThreshold() {
//        return intervalThreshold;
//    }
//
//    public void setIntervalThreshold(int intervalThreshold) {
//        if(intervalThreshold < 1000){
//            intervalThreshold=intervalThreshold*1000;
//            LogManager.debug("interval Threshold를  초단위로 세팅하여 milli second로 변경 :"
//                    +intervalThreshold);
//        }
//        this.intervalThreshold = intervalThreshold;
//    }

//    public boolean isTestOnBorrow() {
//        return testOnBorrow;
//    }
//
//    public void setTestOnBorrow(boolean testOnBorrow) {
//        this.testOnBorrow = testOnBorrow;
//    }

}