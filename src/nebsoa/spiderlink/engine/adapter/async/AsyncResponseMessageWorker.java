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
import nebsoa.common.util.DataMap;
import nebsoa.common.util.FormatUtil;
import nebsoa.spiderlink.client.AsyncMessageClient;
import nebsoa.spiderlink.context.MessageContext;
import nebsoa.spiderlink.engine.message.proxy.ProxyResponseMessageUtil;

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
 * $Log: AsyncResponseMessageWorker.java,v $
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
public class AsyncResponseMessageWorker implements Runnable {
	
	private AsyncMessageHandler asyncMessageHandler;
	private MessageContext messageContext;
	
	public AsyncResponseMessageWorker(AsyncMessageHandler asyncMessageHandler, MessageContext messageContext){
		this.asyncMessageHandler = asyncMessageHandler;
		this.messageContext = messageContext;

	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		
		//대응답인 경우 요청 쓰레드가 먼저 리턴될 때 까지 시간을 보장하기 위해 잠시 현재 쓰레드를 sleep한다.
		waitForRequestThread();
		
		DataMap dataMap = asyncMessageHandler.getDataMap();
		
		Object result = null;
		//핸들러를 통한 언마샬 (M 타입 대응답인 경우는 이미 언먀샬된 상태이므로 생략한다.)
		if(! (ProxyResponseMessageUtil.isProxy(messageContext) && ProxyResponseMessageUtil.isProxyTypeMappingXML(messageContext))){
			result = doAsyncMessageHandler();
		
		}else{
			result = dataMap;
		}
						
		try{
			sendResponseToBiz(result);
			
		}catch(Throwable e){

			//여기서 발생하는 익셉션은 대기하고 있는 action에게 보낼 수도 없으니 로그만 남기고 종료해야 할 것이다..
			String webLogSerialNo = ""; //거래추적번호
			if(dataMap!=null && dataMap.getContext()!=null) webLogSerialNo = dataMap.getContext().getTrxSerNo();
			
			LogManager.error(this.getClass().getName() +"수행 중 오류 발생[거래추적번호:" + webLogSerialNo +"]", e);
			
		}

	}

	private void waitForRequestThread() {
		//대응답인 경우 요청 쓰레드가 먼저 리턴될 때 까지 시간을 보장하기 위해 잠시 현재 쓰레드를 sleep한다.
		LogManager.debug("#### waitForRequestThread() 호출 시작 전... "+ FormatUtil.getToday("hh:mm:ss SSS"));
		if(ProxyResponseMessageUtil.isProxy(messageContext)){
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {}
		}
		LogManager.debug("#### waitForRequestThread() 호출 완료 후... "+ FormatUtil.getToday("hh:mm:ss SSS"));
	}

	/**
	 * 응답 결과를 Biz 인스턴에게 전송한다.
	 * @param result 결과(DataMap 또는 Throwable)
	 * @throws Exception 
	 */
	private void sendResponseToBiz(Object result) throws Exception {

		//asyncMessageHandler가 정상 수행된다면 result가 DataMap이겠지만, 
		//exception이 발생한 경우를 위해 다음과 같이 dataMap을 꺼낸다.
		//dataMap에서 biz와 action Identifier를 꺼내기 위함이다.
		DataMap dataMap = asyncMessageHandler.getDataMap();
		Identifier bizIdentifier = (Identifier)dataMap.get("_$bizIdentifier");
		Identifier actionIdentifier = (Identifier)dataMap.get("_$actionIdentifier");
		
		LogManager.debug("bizIdentifier:" + (bizIdentifier==null?"null":bizIdentifier.getWasId()));
		LogManager.debug("actionIdentifier:" + (actionIdentifier==null?"null":actionIdentifier.getWasId()));
		
		//어싱크핸들러의 수행이 끝나면 reference를 끊어버린다.
		finalizeAsyncMessageHandler();
					
		//Biz 호출
		AsyncMessageClient.doResponse(new Identifier[]{bizIdentifier, actionIdentifier}, result);
		LogManager.debug("AsyncResponseMessageWorker:: sendResponseToBiz 호출 완료..");

	}

	/**
	 * 핸들러를 통해 나머지 작업(주로 언마샬을 수행한다.)
	 * @return 결과(DataMap 또는 Throwable)
	 */
	private Object doAsyncMessageHandler() {
		try{
			LogManager.debug(this.getClass().getName() + ".doAsyncMessageHandler() called..");
			return asyncMessageHandler.doAfterReceivingResponse(messageContext);
		}catch(Throwable th){
			//MessageHandler 수행 시 발생한 에러는 결과로 리턴한다.
			LogManager.debug("발생한 Exception:"+th);
			return th;
		}
	}

	private void finalizeAsyncMessageHandler() {
		this.asyncMessageHandler = null;
		this.messageContext.setAsyncMessageHandler(null);
	}

}
