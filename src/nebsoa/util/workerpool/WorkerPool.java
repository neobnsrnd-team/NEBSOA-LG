/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.util.workerpool;

import org.apache.commons.pool.impl.GenericKeyedObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;

import nebsoa.common.log.LogManager;

/*******************************************************************
 * <pre>
 * 1.설명 
 * WorkerPool을 구현한 클래스이며,  org.apache.commons.pool.impl.GenericObjectPool을
 * wrapping하여 사용한다.
 * 
 * 2.사용법
 * WorkerPool pool = WorkerPoolManager.getInstance().getPool(ip+"_"+port);
 *  if(pool==null){
 *      SocketFactory factory = new SocketFactory(ip,port,timeout,"DEBUG");
 *      pool=WorkerPoolManager.getInstance().makeWorkerPool(
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
 * $Log: WorkerPool.java,v $
 * Revision 1.1  2018/01/15 03:39:52  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:10  cvs
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
 * Revision 1.2  2007/03/19 07:26:41  김성균
 * Pool로부터 자원얻는 시가 Monitor 로그 추가
 *
 * Revision 1.1  2006/10/02 15:41:40  이종원
 * 최초작성
 *
 * Revision 1.1  2006/10/02 15:40:48  이종원
 * 기본 기능 구현
 *
 * </pre>
 ******************************************************************/
public class WorkerPool extends GenericObjectPool {
    
    String name;
    
    WorkerFactory factory;
    
    boolean threadMode;
    

   /**
    * default로 세팅된 Worker 풀을  리턴.default값은 아래와 같다. 
    * 최대 허용 Worker 수(max Active):5, 
    * 요청 폭주시 반응 : 기다림:1 (기다림:1, 에러유발:0, 풀사이즈 증가:2  중 기다림 세팅 )
    * 기다림일 경우 기다리는 시간(밀리세컨드 ): 10초(10,000),
    */
    public WorkerPool(WorkerFactory objFactory) {
        this(objFactory,5,GenericKeyedObjectPool.WHEN_EXHAUSTED_BLOCK,10*1000);
    }

   /**
    * 최대 허용 Worker 수만 세팅하고 나머지는 default로 세팅된 Worker 풀을  리턴.
    * default값은 아래와 같다. 
    * 최대 허용 Worker 수(max Active):인자로 받은 값. 
    * 요청 폭주시 반응 : 기다림:1 (기다림:1, 에러유발:0, 풀사이즈 증가:2  중 기다림 세팅 )
    * 기다림일 경우 기다리는 시간(밀리세컨드 ): 10초(10,000),
    */
    public WorkerPool(WorkerFactory objFactory,int maxActive) {
        this(objFactory,maxActive,GenericKeyedObjectPool.WHEN_EXHAUSTED_BLOCK,10*1000);
    }

   /**
    *  최대허용 Worker 수(maxActive),폭주시  최대 기다림 시간 ( maxWait) 값을 인자로 받아 
    *  socket pool 생성
    */
    public WorkerPool(WorkerFactory factory,int maxActive,int maxWaitTime){ 
        this(factory,maxActive,GenericKeyedObjectPool.WHEN_EXHAUSTED_BLOCK,maxWaitTime);
    }
   
   /**
    * 최대 허용 Worker 수(max Active):인자로 받은 값. 
    * 요청 폭주시 반응 : 인자로 받은 값 (기다림:1, 에러유발:0, 풀사이즈 증가:2  중 기다림 세팅 )
    * 기다림일 경우 기다리는 시간(밀리세컨드 ): 인자로 받은 값을 세팅하여 Worker 풀을 만든다.
    * <font color='red'><b>만약 기다림 시간이 200(0.2초  보다 작다면 초단위로 세팅했다고 가정하여,
    * 1000을 곱하여 세팅한다.</b></font>
    * 2004.09 이종원 작성.
    */
    public WorkerPool(WorkerFactory factory, int maxActive, 
           byte when_exhausted_block, int maxWaitTime) {
        super(factory,maxActive,when_exhausted_block,
               (maxWaitTime<200?maxWaitTime*1000:maxWaitTime));
        super.setTestOnReturn(true);
        this.factory=factory;
    }
   

    public Worker borrowWoker() throws Exception {
        Worker worker= null;
        long start = System.currentTimeMillis();
        worker=(Worker)super.borrowObject();
        long end = System.currentTimeMillis()-start;
        if(end > 500){
            LogManager.info("MONITOR","WorkerPool로부터 Worker얻는시간:"
                    +(end/1000.0)+"초.필요시 (Worker)Thread갯수를 늘려주십시오.\n"+getInfo());
        }
        worker.setDone(false);
        return worker;
    }

   /* (non-Javadoc)
    * @see org.apache.commons.pool.ObjectPool#returnObject(java.lang.Object)
    */
    public void returnWorker(Worker worker) throws Exception {
        super.returnObject(worker);
        LogManager.debug(this.getInfo());
    }

    public boolean closed(){
        return super.isClosed();
    }

    public String toString(){
        return getInfo();
    }
    
    public String getInfo() {
        if(isClosed()) return name+" socket pool is closed...";
        return "\tWorkerPool :["+name
        +"] 수행중인 Worker:["+getNumActive()
        +"] 쉬고있는 Worker:["+getNumIdle()+"]";
    }
    
    /**
     * 열려진 Worker 갯수(active + idle)을 리턴 한다.
     * 2006. 9. 27.  이종원 작성
     * @return num of socket
     */
    public synchronized int getWorkerCount(){        
        return getNumActive()+getNumIdle();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WorkerFactory getFactory() {
        return factory;
    }

    public void setFactory(WorkerFactory factory) {
        this.factory = factory;
    }



    public void setThreadMode(boolean isSync) {
        this.threadMode=isSync;
    }

    public boolean getThreadMode() {
       return threadMode;
    }
}