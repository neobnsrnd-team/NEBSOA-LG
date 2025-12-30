/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
package nebsoa.spiderlink.engine.adapter.async;

import nebsoa.common.log.LogManager;
import nebsoa.common.util.PropertyManager;
import nebsoa.spiderlink.context.MessageContext;
import nebsoa.util.threadpool.ThreadPool;
import nebsoa.util.threadpool.ThreadPoolManager;
import nebsoa.util.threadpool.WorkerThread;

/*******************************************************************
 * <pre>
 * 1.설명 
 * Async Message 처리 모듈 중 하나.
 * AsyncMessageHandler 호출을 담당한다.
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
 * $Log: AsyncResponseMessageManager.java,v $
 * Revision 1.1  2018/01/15 03:39:52  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:16  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:26  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:28  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:54  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/04/08 04:42:52  김승희
 * 신규추가
 *
 * Revision 1.1  2008/04/08 02:32:43  shkim
 * 최초등록
 *
 * Revision 1.2  2008/04/04 09:08:31  신정섭
 * *** empty log message ***
 *
 * Revision 1.1  2008/04/04 08:44:36  신정섭
 * 신규 추가
 *
 *
 * </pre>
 ******************************************************************/
public class AsyncResponseMessageManager {
	
	private static Object dummy=new Object();
	
	private static AsyncResponseMessageManager instance;
	
	private ThreadPool threadPool;
	
	public static AsyncResponseMessageManager getInstance(){
	    if(instance==null){
	        synchronized (dummy) {
	            if(instance==null) instance = new AsyncResponseMessageManager();
	        }
	    }
	    return instance;
	}
	
	private AsyncResponseMessageManager(){
		makeThreadPool();
	}
	
	/**
	 * AsyncMessageHandler를 새로운 쓰레드로 수행시킨다.
	 * 
	 * @param messageContext
	 */
	public void startResponseProcess(MessageContext messageContext){
		
		AsyncMessageHandler asyncMessageHandler = messageContext.getAsyncMessageHandler();

		if(asyncMessageHandler==null){
			//에러 로그만 남기고 return한다.
			LogManager.error(this.getClass().getName() + ":: 거래["+messageContext.getTrxId() +"] uid " + messageContext.getUid() +"처리중 에러 발생 (AsyncMessageHandler is null)");
			return;
		}

		AsyncResponseMessageWorker worker = new AsyncResponseMessageWorker(asyncMessageHandler, messageContext);

		WorkerThread workerthread = null;
		try {
			workerthread= (WorkerThread) threadPool.borrowObject();
		} catch (Exception e) {
			//쓰레드를 얻지 못했을 때 에러 로그만 남기고 return한다.
			LogManager.error(this.getClass().getName() + ":: 거래["+messageContext.getTrxId() +"] uid " + messageContext.getUid() +"처리중 에러 발생(쓰레드 획득 실패) ", e);
			return;
		}
		
		if(workerthread!=null){
			workerthread.execute(worker);
		}else{
			//에러 로그만 남기고 return한다.
			LogManager.error(this.getClass().getName() + ":: 거래["+messageContext.getTrxId() +"] uid " + messageContext.getUid() +"처리중 에러 발생(workerthread is null) ");
			return;
		}
		LogManager.debug("AsyncReponseMessageManager.startResponseProcess() 수행완료..");
	}
	
    private void makeThreadPool() {
    	int maxIdle = PropertyManager.getIntProperty("async", "RESPONSE_WORKER_THREAD_MAX_IDLE", 10);
        if(threadPool==null){            
            threadPool=ThreadPoolManager.getInstance()
                .getPool(this.getClass().getName()+"_pool", maxIdle);
        }        
    }
	
}
