/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
package nebsoa.spiderlink.engine.adapter.async;

import nebsoa.common.async.AsyncCaller.Identifier;
import nebsoa.common.log.LogManager;
import nebsoa.common.monitor.ContextLogger;
import nebsoa.common.util.DataMap;
import nebsoa.service.engine.ServiceEngine;
import nebsoa.spiderlink.client.AsyncMessageClient;
import nebsoa.spiderlink.connector.listener.protocol.TcpInputMessageHandler;
import nebsoa.spiderlink.context.MessageContext;
import nebsoa.spiderlink.engine.adapter.tcp.BaseOrgHandler;
import nebsoa.spiderlink.engine.message.proxy.ProxyResponseMessageUtil;
import nebsoa.util.threadpool.ThreadObjectFactory;
import nebsoa.util.threadpool.ThreadPool;
import nebsoa.util.threadpool.ThreadPoolManager;
import nebsoa.util.threadpool.WorkerThread;

/*******************************************************************
 * <pre>
 * 1.설명 
 * Async 메시지 처리 모듈 중 하나이다.
 * 응답 전문을 넘겨 받아 보관된 MessageHandler를 통해 언마샬을 수행한 후 최초 호출한 Biz 인스턴스에게 결과를 전달한다.
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
 * $Log: AsyncMultiResponseMessageWorker.java,v $
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
 * Revision 1.2  2008/09/23 08:21:08  youngseokkim
 * Context를 잃어버리지 않도록 run() 시 ContextLogger에 ctx를 put 해준다.
 *
 * Revision 1.1  2008/09/08 03:04:38  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/08/04 08:54:54  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.2  2008/04/08 05:25:18  김승희
 * 주석 변경
 *
 * Revision 1.1  2008/04/08 04:42:52  김승희
 * 신규추가
 *
 * Revision 1.1  2008/04/08 02:32:43  shkim
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public class AsyncMultiResponseMessageWorker implements Runnable {
	
	private AsyncMessageHandler asyncMessageHandler;
	private TcpInputMessageHandler owner;
    private String ioProtocol;
    private MessageContext messageContext;
    private DataMap dataMap;
    private String serviceId;

    public void init(TcpInputMessageHandler msgHandler
            , String syncType            
            , BaseOrgHandler orgHandler
            , MessageContext ctx
            , DataMap dataMap
            , String serviceId) {
    	
        this.owner = msgHandler;
        this.asyncMessageHandler = (AsyncMessageHandler)orgHandler;
        this.messageContext = ctx;
        this.dataMap = dataMap;
        this.ioProtocol=syncType;
        this.serviceId=serviceId;
    }

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		
		Object result = null;
		// context 를 다른 쓰레드에서도 유지 하기 위해 추가
    	if(dataMap != null){
    		if(dataMap.getContext() != null){
        		// Tier 분리시에도 Context 객체를 유지하기 위해서 ThreadLocal에 넣는다.
                ContextLogger.putContext(dataMap.getContext());
    		}
    	}
		//핸들러를 통한 언마샬 (M 타입 대응답인 경우는 이미 언먀샬된 상태이므로 생략한다.)
		if(! (ProxyResponseMessageUtil.isProxy(messageContext) && ProxyResponseMessageUtil.isProxyTypeMappingXML(messageContext))){
			result = doAsyncMessageHandler();
		}else{
			result = dataMap;
		}
						
		try{
			// 서비스 호출
			sendResponseToBiz();
			
		}catch(Throwable e){

			//여기서 발생하는 익셉션은 대기하고 있는 action에게 보낼 수도 없으니 로그만 남기고 종료해야 할 것이다..
			String webLogSerialNo = ""; //거래추적번호
			if(dataMap!=null) webLogSerialNo = dataMap.getString("_$TRX_SER_NO");
			
			LogManager.error(this.getClass().getName() +"수행 중 오류 발생[거래추적번호:" + webLogSerialNo +"]", e);
			
		}
	}
	
    public void execute() throws Throwable{
    	ThreadPool threadPool = ThreadPoolManager.getInstance().getPool("async_multiress_thread_pool");
		if (threadPool == null) {
			threadPool = ThreadPoolManager.getInstance().makeThreadPool("async_multiress_thread_pool",new ThreadObjectFactory(),5,0);
		}
		
		WorkerThread workerThread = null;
		try {
			workerThread= (WorkerThread) threadPool.borrowObject();
		} catch (Exception e) {
			//쓰레드를 얻지 못했을 때 에러 로그만 남기고 return한다.
			LogManager.error(this.getClass().getName() + ":: 거래["+messageContext.getTrxId() +"] uid " + messageContext.getUid() +"처리중 에러 발생(쓰레드 획득 실패) ", e);
			return;
		}
		
		if(workerThread!=null){
			workerThread.execute(this);
		}else{
			//에러 로그만 남기고 return한다.
			LogManager.error(this.getClass().getName() + ":: 거래["+messageContext.getTrxId() +"] uid " + messageContext.getUid() +"처리중 에러 발생(workerthread is null) ");
			return;
		}
    }

	/**
	 * 응답 결과를 Biz 인스턴에게 전송한다.
	 * @param result 결과(DataMap 또는 Throwable)
	 * @throws Exception 
	 */
	private void sendResponseToBiz() throws Exception {

		LogManager.debug(" ########### SERVICE ID="+serviceId);
		dataMap = ServiceEngine.doProcess(serviceId, dataMap);
		
		LogManager.debug("AsyncMultiResponseMessageWorker:: sendResponseToBiz 호출 완료..");

	}

	/**
	 * 핸들러를 통해 나머지 작업(주로 언마샬을 수행한다.)
	 * @return 결과(DataMap 또는 Throwable)
	 */
	private Object doAsyncMessageHandler() {
		try{
			LogManager.debug(this.getClass().getName() + ".doAsyncMessageHandler() called..");
			// Async 비온라인 일 경우 InputMessageHandler에서 messageContext에 put 하지 않고
			// dataMap에 put하기 때문에 dataMap을 set하는 부분이 필요하여 추가 
			asyncMessageHandler.setDataMap(dataMap);
			
			return asyncMessageHandler.doAfterReceivingResponse(messageContext);
		}catch(Throwable th){
			//MessageHandler 수행 시 발생한 에러는 결과로 리턴한다.
			LogManager.debug("발생한 Exception:"+th);
			return th;
		}
	}
}
