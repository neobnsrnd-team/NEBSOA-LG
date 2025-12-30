/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.biz.client;

import nebsoa.biz.exception.BizException;
import nebsoa.common.async.AsyncCaller;
import nebsoa.common.async.AsyncCallerPool;
import nebsoa.common.async.AsyncCaller.Identifier;
import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;
import nebsoa.common.startup.StartupContext;
import nebsoa.common.util.DataMap;
import nebsoa.common.util.FormatUtil;
import nebsoa.common.util.PropertyManager;
import nebsoa.spiderlink.exception.MessageException;

/*******************************************************************
 * <pre>
 * 1.설명 
 * Async Message 처리를 수행하는 Biz Logic 호출을 대리해 주기 위한 Client
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
 * $Log: AsyncMessageBizClient.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:27  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:26  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:29  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.2  2008/08/11 01:07:26  shkim
 * 패키지이름 변경
 *
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/04/08 04:42:52  김승희
 * 신규추가
 *
 *
 * </pre>
 ******************************************************************/
public class AsyncMessageBizClient {
	

    private static final String ASYNC_MESSAGE_BIZ_CLIENT = "nebsoa.biz.client.AsyncMessageBizClient";

    private AsyncMessageBizClient(){}
    
	/**
     * 비즈니스 로직을 담고 있는 Biz를 호출합니다.
     * 
     * @param bizId biz class full name
     * @param DataMap Biz를 수행하기 위해 필요한 Data
     * @return DataMap 실행이 완료된 결과를 담고 있는 DataMap 객체
     * @throws BizException 
     */
    public static DataMap doBizProcess(String bizId, DataMap map, Object caller) throws BizException {
    	long defaultTimeout = PropertyManager.getLongProperty("async", "DEFAULT_ACTION_TIMEOUT", 60*1000);
    	return doBizProcess(bizId, map, caller, PropertyManager.getLongProperty("web_config","aync_action_timeout",defaultTimeout));
    }
    
    /**
     * 비즈니스 로직을 담고 있는 Biz를 호출합니다.
     * 
     * @param bizId biz class full name
     * @param map Biz를 수행하기 위해 필요한 Data
     * @param caller 현재 메소드를 호출하는 인스턴스 (this)
     * @param timeoutMillis 응답이 오기 까지 최대 대기 시간
     * @return DataMap 실행이 완료된 결과를 담고 있는 DataMap 객체
     * @throws BizException
     */
    public static DataMap doBizProcess(String bizId, DataMap map, Object caller, long timeoutMillis) throws BizException {
    	//타임아웃 값이 0이상인 경우
    	if(timeoutMillis>0L){
    		return doProcessWithTimeout(bizId, map, caller, timeoutMillis);
    	}else{
    		return doProcessWithoutTimeout(bizId, map, caller);
    	}
		
    }

	/**
	 * 타임아웃 값이 0이하인 경우 처리
	 * 거래 요청 후 바로 리턴한다.
	 * @param bizId
	 * @param map
	 * @return
	 * @throws BizException
	 */
	private static DataMap doProcessWithoutTimeout(String bizId, DataMap map, Object caller) throws BizException{
		
		createAsyncCaller(map, caller);
		
		return requestToBiz(bizId, map);
	}

	/**
	 * 타임아웃 값이 0이상인 경우 처리
	 * 거래 요청 후 타임아웃 값 만큼 wait한다.
	 * @param bizId
	 * @param map
	 * @param caller
	 * @param timeoutMillis
	 * @return
	 * @throws BizException
	 */
	private static DataMap doProcessWithTimeout(String bizId, DataMap map, Object caller, long timeoutMillis) throws BizException {
		
		String webLogSerialNo = ""; //거래추적번호
		if(map!=null && map.getContext()!=null) webLogSerialNo = map.getContext().getTrxSerNo();
		
		//1. AsyncCaller 생성
		AsyncCaller asyncCaller = createAsyncCaller(map, caller);
    	
    	//2. Biz로 요청 데이터을 보낸다.
    	map = requestToBiz(bizId, map);
    	
    	//exception이 아닐 경우 실응답 데이터가 올 때까지 wait한다..
	    synchronized(asyncCaller){
		    try {
		    	LogManager.debug("거래추적번호[" + webLogSerialNo +"]" + caller.getClass().getName() + "::" + timeoutMillis+"millis 동안 대기합니다..");
		    	asyncCaller.wait(timeoutMillis);
			} catch (InterruptedException e) {
				LogManager.error("waiting action thread interrupted..", e);
			}
	    }
	    
	    //AsyncCallerPool로 부터 제거한다..
	    AsyncCallerPool.getInstance().remove(asyncCaller.getUid());
	    
	    //4. 결과 처리
	    Object result = asyncCaller.getResult();
	    if(result==null){
	    	LogManager.error("거래추적번호[" + webLogSerialNo +"]" + caller.getClass().getName() + "::AsyncMessageBizClient timeout..(" + timeoutMillis + ")");
	    	throw new SysException("AP서버로 부터 응답을 받지 못했습니다. timeout:" + timeoutMillis);
	    
	    //결과가  있는 경우
	    }else{
			return handleResult(result);
    	}
	}

	/**
	 * AsyncCaller를 생성한다.
	 * @param map
	 * @param caller
	 * @return
	 */
	private static AsyncCaller createAsyncCaller(DataMap map, Object caller) {
	
		AsyncCaller asyncCaller = new AsyncCaller(caller, AsyncCaller.ACTION_TYPE);
    	AsyncCallerPool.getInstance().put(asyncCaller);
    	
    	//나중에 꺼내기 위해 담아둔다..
    	map.put("_$actionIdentifier", asyncCaller.getIdentifier());
		return asyncCaller;
	}

	/**
	 * Biz로 요청 데이터을 보낸다.
	 * @param bizId
	 * @param map
	 * @return
	 * @throws BizException
	 */
	private static DataMap requestToBiz(String bizId, DataMap map)
			throws BizException {
		try{
    		//biz 호출
    		map = BizClient.doBizProcess(bizId, map);
    		
    	//여기서 발생하는 exception은 biz 수행-->마샬-->요청 전문 전송 까지의 단계에서 발생하는 exception이다.
    	}catch(BizException bizEx){
    		throw bizEx;
    		
    	}catch(RuntimeException e){
    		
    		throw e;
    	}finally{
    		LogManager.debug("#### requestToBiz() 호출 완료... "+ FormatUtil.getToday("hh:mm:ss SSS"));
    	}
		return map;
	}

	/**
	 * 결과 처리를 한다.
	 * @param result
	 * @return
	 * @throws BizException
	 */
	private static DataMap handleResult(Object result) throws BizException {
		if(result instanceof DataMap){
	    	return (DataMap)result;
	    	
	    }else if(result instanceof BizException){
	    	
	    	throw (BizException)result;
	    
	    }else if(result instanceof MessageException){
	    	
	    	throw new BizException((MessageException)result);
	    	
	    }else if(result instanceof SysException){
	    	
	    	throw (SysException)result;
	    
	    }else if(result instanceof Throwable){
	    	throw new SysException((Throwable)result);
	
	    }else{
	    	throw new SysException("허용되지 않는 결과 타입입니다. result=" + result);
	    }
	}
    
    /**
     * doBizProcess()를 호출한 후 대기 중인 action 인스턴스에게 처리결과를 전달하고 notify한다.
     * 
     * @param actionIdentifier 대기중인 Action의 구분자
     * @param result 결과 
     */
    public static void doLocalResponse(Identifier actionIdentifier, Object result){
    	LogManager.debug("AsyncMessageBizClient doLocalResponse() called..");
    	
		AsyncCaller asyncCaller = AsyncCallerPool.getInstance().get(actionIdentifier.getUid());
		if(asyncCaller==null){
			//wait하고 있는 action이 없다는 의미이므로 정상적인 상황은 아니다. 
						
			String webLogSerialNo = ""; //거래추적번호
			if(result!=null && result instanceof DataMap){
				DataMap dataMap = (DataMap)result;
				if(dataMap.getContext()!=null) webLogSerialNo = dataMap.getContext().getTrxSerNo();
			}
			LogManager.error("응답을 전달할 Action 객체가 없습니다. (asyncCaller is null) [거래추적번호:" + webLogSerialNo +"]");
			
			throw new SysException(actionIdentifier.getWasId() + ":: 응답을 전달할 Action 객체가 없습니다. (asyncCaller is null)");
			
		}else{
			//응답을 담는다.
			asyncCaller.setResult(result);
			//wait하고 있는 action을 notify한다..
			synchronized(asyncCaller){
				asyncCaller.notifyAll();
				LogManager.debug("AsyncCaller thread notified..");
			}
		}
	}
    
    /**
     * RPC Client를 통해 리모트에 존재하는 Action 인스턴스에게 처리결과를 전달하고 notify한다.
     * @param actionIdentifier 대기중인 Action의 구분자
     * @param result 결과
     * @throws Exception 
     */
	public static void doResponse(Identifier actionIdentifier, Object result) throws Exception{
		
		String ejbJndiName = PropertyManager.getProperty("async", "RPC_EJB_NAME_FOR_RESPONSE", RPCClient.DEFAULT_EJB_NAME);
		
		//응답 was를 자동으로 찾을 때
    	if(actionIdentifier.isAutoWasSearch()){
    		
    		//현재 was instance id와 bizIdentifier의  was id가 같으면 로컬 call한다..
    		if(StartupContext.getInstanceId().equals(actionIdentifier.getWasId())){
    			LogManager.debug("[local] " + ASYNC_MESSAGE_BIZ_CLIENT + " doLocalResponse() 호출합니다..");
        		AsyncMessageBizClient.doLocalResponse(actionIdentifier, result);
        	
        	}else{
        		LogManager.debug("[" + actionIdentifier.getWasId() +"] " + ASYNC_MESSAGE_BIZ_CLIENT + " doLocalResponse() 호출합니다..");
        		RPCClient.doRPC(
        				actionIdentifier.getWasId(), 
					ejbJndiName,
					ASYNC_MESSAGE_BIZ_CLIENT, "doLocalResponse", 
					new Class[]{actionIdentifier.getClass(), java.lang.Object.class},
					new Object[]{actionIdentifier, result});
        	}
    		
    	//응답 was를 수동으로 찾을 때 (입력한 값을 사용할 때)
    	}else{
    		LogManager.debug("[" + actionIdentifier.getProviderUrl() +"] " + ASYNC_MESSAGE_BIZ_CLIENT + " doLocalResponse() 호출합니다..");
    		RPCClient.doRPC(
    				actionIdentifier.getProviderUrl(),
    				actionIdentifier.getContextFactory(),
					ejbJndiName,
					ASYNC_MESSAGE_BIZ_CLIENT, "doLocalResponse", 
					new Class[]{actionIdentifier.getClass(), java.lang.Object.class},
					new Object[]{actionIdentifier, result});
    	}
    	
		LogManager.debug("AsyncMessageClient:: doResponse() called.. ");
	}
}