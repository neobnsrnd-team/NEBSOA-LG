/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.client;

import nebsoa.biz.base.AsyncBiz;
import nebsoa.biz.client.AsyncMessageBizClient;
import nebsoa.biz.client.RPCClient;
import nebsoa.common.async.AsyncCaller;
import nebsoa.common.async.AsyncCallerPool;
import nebsoa.common.async.AsyncCaller.Identifier;
import nebsoa.common.log.LogManager;
import nebsoa.common.startup.StartupContext;
import nebsoa.common.util.DataMap;
import nebsoa.common.util.PropertyManager;
import nebsoa.spiderlink.exception.MessageException;
import nebsoa.spiderlink.exception.MessageSysException;

/*******************************************************************
 * <pre>
 * 1.설명 
 * Async 메시지 처리를 수행시키는 EJB를 호출한다. 
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
 * $Log: AsyncMessageClient.java,v $
 * Revision 1.1  2018/01/15 03:39:51  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:54  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:24  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:27  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.3  2008/08/11 01:07:26  shkim
 * 패키지이름 변경
 *
 * Revision 1.2  2008/08/04 09:29:13  shkim
 * action identifier가 null인 경우 처리
 *
 * Revision 1.1  2008/04/08 04:42:52  김승희
 * 신규추가
 *
 *
 * </pre>
 ******************************************************************/
public class AsyncMessageClient {
	
	static final String ASYNC_MESSAGE_CLIENT = "nebsoa.spiderlink.client.AsyncMessageClient";
	
	private AsyncMessageClient(){};
	
	/**
	 * 요청 전문을 해당 기관으로 전송한다.
	 * caller가 AsyncBiz를 구현한 경우 doResponse 또는 handlerException이 호출되면서 응답 결과가 Async하게 전달된다.
	 * 
	 * @param trxId 거래 ID
	 * @param orgId 기관 ID
	 * @param map 처리할 데이터가 담겨있는 DataMap
	 * @param caller 이 메소드를 호출한 인스턴스 (주로 this)
	 * @throws MessageException
	 */
	public static void doRequest(String trxId, String orgId, DataMap map, Object caller) throws MessageException{
		
		//AsyncBiz 타입인 경우에만 pool에 보관한다.
		if(caller instanceof AsyncBiz){
			AsyncCaller asyncCaller = new AsyncCaller(caller, AsyncCaller.BIZ_TYPE);
			//AsyncCaller Pool 에 담는다.
			AsyncCallerPool.getInstance().put(asyncCaller);
			map.put("_$bizIdentifier", asyncCaller.getIdentifier());
		}
		
		MessageClient.doSyncProcess(trxId, orgId, map);
		
	}
	
	/**
	 * doRequest를 호출한 Biz 인스턴스의 doResponse 또는 handleException을 호출한다.
	 * @param identifiers
	 * @param result
	 * @throws Exception 
	 */
	public static void doLocalResponse(Identifier[] identifiers, Object result) throws Exception{
		
		LogManager.debug("AsyncMessageClient.doLocalResponse() called..");
		AsyncCaller caller = AsyncCallerPool.getInstance().get(identifiers[0].getUid());
		
		if(caller==null){
			//caller 없을 수도 있음..(AsyncBiz가 아닌 경우는 caller를 pool에 담지 않으므로 정상 상황이다.)
			LogManager.debug("asyncCaller is null..");
		
		}else{
		
			//원 호출 인스턴스가 AsyncBiz 타입인 경우 doResponse 또는 handleException(에러 발생 시)을 호출하여 마무리 작업을 할 수 있게 한다.
			if(caller.getCaller() instanceof AsyncBiz){
				
				LogManager.debug("AsyncBiz ::" + caller.getCaller());
				
				if(result instanceof Throwable){
					try {
						result = ((AsyncBiz)caller.getCaller()).handleException((Throwable)result);
					} catch (Throwable e) {
						result = e;
					}
				}else if(result instanceof DataMap){
					result = ((AsyncBiz)caller.getCaller()).doResponse((DataMap)result);
				}else{
					//다른 타입인 경우 무조건 에러처리
					result = new MessageSysException("unknown result type..result=" + result);
				}
			}
		}
		//RPC를 통해 wait하고 있는 action에게 응답을 준다.
		//action identifier가 null인 경우는 여기서 종료한다. 
		if(identifiers[1]!=null){
			AsyncMessageBizClient.doResponse(identifiers[1], result);
		}

	}
	
	/**
	 * 리모트에 존재하는 Biz 인스턴스의 doResponse 또는 handleException을 호출한다.
	 * @param bizIdentifier
	 * @param result
	 * @throws Exception 
	 */
	public static void doResponse(Identifier[] bizIdentifier, Object result) throws Exception{
		
		String ejbJndiName = PropertyManager.getProperty("async", "RPC_EJB_NAME_FOR_RESPONSE", RPCClient.DEFAULT_EJB_NAME);
		
		/*
		*   보통
		*   bizIdentifier[0] --> Biz AsyncCaller의 Identifier가 들어 있고
		*   bizIdentifier[1] --> Action AsyncCaller의 Identifier가 들어 있다.
		*   
		*/
		
		//응답 was를 자동으로 찾을 때
    	if(bizIdentifier[0].isAutoWasSearch()){
    		//현재 was instance id와 bizIdentifier의  was id가 같으면 로컬 call한다..
    		if(StartupContext.getInstanceId().equals(bizIdentifier[0].getWasId())){
    			LogManager.debug("[local] " + ASYNC_MESSAGE_CLIENT + " doLocalResponse() 호출합니다..");
        		AsyncMessageClient.doLocalResponse(bizIdentifier, result);
        	
        	//RESPONSE WAS SEARCH MODE가 manual이면 async.properties.xml에 있는 정보로 호출한다..
        	}else{
        		LogManager.debug("[" + bizIdentifier[0].getWasId() +"] " + ASYNC_MESSAGE_CLIENT + " doLocalResponse() 호출합니다..");
        		RPCClient.doRPC(
					bizIdentifier[0].getWasId(), 
					ejbJndiName,
					ASYNC_MESSAGE_CLIENT, "doLocalResponse", 
					new Class[]{bizIdentifier.getClass(), java.lang.Object.class},
					new Object[]{bizIdentifier, result});
        	}
    		
    	//응답 was를 수동으로 찾을 때 (입력한 값을 사용할 때)
    	//RESPONSE WAS SEARCH MODE가 manual이면 async.properties.xml에 있는 정보로 호출한다..
    	}else{
    		LogManager.debug("[" + bizIdentifier[0].getProviderUrl() +"] " + ASYNC_MESSAGE_CLIENT + " doLocalResponse() 호출합니다..");
    		RPCClient.doRPC(
					bizIdentifier[0].getProviderUrl(),
					bizIdentifier[0].getContextFactory(),
					ejbJndiName,
					ASYNC_MESSAGE_CLIENT, "doLocalResponse", 
					new Class[]{bizIdentifier.getClass(), java.lang.Object.class},
					new Object[]{bizIdentifier, result});
    	}
    			
	}
}
