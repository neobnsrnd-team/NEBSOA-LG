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

import nebsoa.common.log.LogManager;
import nebsoa.util.threadpool.ThreadPool;
import nebsoa.util.threadpool.ThreadPoolManager;
import nebsoa.util.threadpool.WorkerThread;


/**
 * worker 
 * @author 이종원
 */
/*******************************************************************
 * <pre>
 * 1.설명 
 * Pool 에서 관리되는  객체
 * 
 * 2.사용법
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
 * $Log: Worker.java,v $
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
 * Revision 1.4  2007/02/05 10:57:47  김승희
 * doProcess 에서 exception 발생 시 finally절에서 worker 쓰레드를 풀로 리턴하도록 수정
 *
 * Revision 1.3  2006/11/27 04:35:44  이종원
 * *** empty log message ***
 *
 * Revision 1.2  2006/10/02 16:32:21  이종원
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
public abstract class Worker implements Runnable{

   /**
    * The pool being used. We use this if we need to return the object back to the
    * pool. If this is not set, we assume that the client will take care of returning
    * the object back to the pool.
    */
    private WorkerPool pool = null;
    
    protected boolean threadMode;
   
    long startTime;
   
    public Worker() {
        startTime=System.currentTimeMillis();
    }
   /**
    * @param pool The pool to set.
    */
    public void setPool(WorkerPool pool) {
        this.pool = pool;
    }
    public WorkerPool getPool() {
        return pool;
    }
    
    public void setDone(boolean done){
        if(!done){
            startTime=System.currentTimeMillis();
        }
    }
    
    public long getExeTime(){
        return System.currentTimeMillis()-startTime;
    }
    public boolean isThreadMode() {
        return threadMode;
    }
    public void setThreadMode(boolean sync) {
        this.threadMode = sync;
    }
    /**
     * impelemts destroy logic is subclass.
     * default do nothing....
     * 2006. 10. 2.  이종원 작성
     */
    public void destroy() {
        
    }
    
    public void execute() throws Throwable{
        if(!threadMode){ // non-thread mode
            try{
            	doProcess();
            }finally{
            	this.pool.returnWorker(this);
            }
            
        }else{ // thread mode
            ThreadPool threadpool = ThreadPoolManager.getInstance().getPool("WORKER_POOL",5);
            WorkerThread thread = (WorkerThread) threadpool.borrowObject();
            thread.execute(this);
        }
    }
    
    public void run() {        
        try {
            doProcess();
        } catch (Throwable e) {
            LogManager.error(e.toString(),e);
        } finally{
        	try {
				this.pool.returnWorker(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
    }
    
    /**
     * 상속받은 클래스에서 비지니스 로직을 여기에 구현한다.
     * 2006. 10. 2.  이종원 작성
     * @throws Throwable 
     * @throws IOException 
     */
    public abstract void doProcess() throws  Throwable;
    /**
     * 풀로 돌아가서 다시 일하기 위해 초기화 한다.
     * 하위 클래스에서 구현하여 사용한다.
     * 2006. 10. 2.  이종원 작성
     */
    public void reset() {
        
    }
 
}