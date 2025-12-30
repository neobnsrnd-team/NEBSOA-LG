/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.client;

import java.rmi.RemoteException;

import javax.ejb.CreateException;

import nebsoa.common.Context;
import nebsoa.common.exception.SysException;
import nebsoa.common.jndi.EJBManager;
import nebsoa.common.log.LogManager;
import nebsoa.common.monitor.ContextLogger;
import nebsoa.common.util.DataMap;
import nebsoa.common.util.PropertyManager;
import nebsoa.common.util.StringUtil;
import nebsoa.spiderlink.ejb.MessageProcessorEJB;
import nebsoa.spiderlink.ejb.MessageProcessorHome;
import nebsoa.spiderlink.engine.MessageEngine;
import nebsoa.spiderlink.engine.adapter.jms.JmsOutputAdapter;
import nebsoa.spiderlink.exception.MessageException;
import nebsoa.util.lockpool.Lock;
import nebsoa.util.lockpool.LockPoolManager;

/*******************************************************************
 * <pre>
 * 1.설명 
 * BPM을 수행시키는 EJB를 호출한다. 
 * 
 * 2.사용법
 * doAsyncProcess(ServiceContext) : 비동기 호출
 * doSyncProcess(ServiceContext) : 동기 호출
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
 * $Log: MessageClient.java,v $
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
 * Revision 1.4  2008/11/03 10:23:33  최수종
 * *** empty log message ***
 *
 * Revision 1.3  2008/11/03 00:16:09  최수종
 * 에러 trace 추가
 *
 * Revision 1.2  2008/08/04 09:46:38  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.2  2008/03/05 00:24:12  오재훈
 * WebSphere 에서는 RemoteException으로 catch를 하여도 실제 잡힌건 ServerExcetpion이 잡힙니다. 그래서 Exception.getCause()했을때도 RemoteException이면 getCause()해서 던지는 로직을 추가하였습니다.
 *
 * Revision 1.1  2008/01/22 05:58:35  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:39:14  안경아
 * *** empty log message ***
 *
 * Revision 1.38  2007/10/02 09:13:50  김성균
 * ContextLogger 적용
 *
 * Revision 1.37  2007/09/17 12:22:24  최수종
 * *** empty log message ***
 *
 * Revision 1.36  2007/09/17 02:04:39  이종원
 * Context Monitor 로직 추가
 *
 * Revision 1.35  2007/05/01 08:12:57  김성균
 * import 정리
 *
 * Revision 1.34  2007/05/01 08:10:27  김성균
 * Lock 처리 기능 추가
 *
 * Revision 1.33  2007/03/27 05:14:37  김성균
 * 로컬호출일 경우 EJB 사용하지 않도록 수정
 *
 * Revision 1.32  2007/01/30 02:28:45  김성균
 * 오류메시지 수정
 *
 * Revision 1.31  2007/01/09 09:17:44  김성균
 * *** empty log message ***
 *
 * Revision 1.30  2007/01/09 09:13:48  김성균
 * *** empty log message ***
 *
 * Revision 1.29  2006/12/18 06:15:03  김성균
 * *** empty log message ***
 *
 * Revision 1.28  2006/11/09 06:26:33  김승희
 * *** empty log message ***
 *
 * Revision 1.27  2006/11/09 01:43:06  김승희
 * Profiler 복사 추가
 *
 * Revision 1.26  2006/10/31 00:29:53  김성균
 * *** empty log message ***
 *
 * Revision 1.25  2006/10/31 00:17:30  김성균
 * *** empty log message ***
 *
 * Revision 1.24  2006/10/17 02:19:17  김성균
 * *** empty log message ***
 *
 * Revision 1.23  2006/08/19 02:02:59  김성균
 * *** empty log message ***
 *
 * Revision 1.22  2006/08/01 01:42:19  김성균
 * 오류코드 처리
 *
 * Revision 1.21  2006/07/31 08:30:54  김성균
 * reload() 제거
 *
 * Revision 1.20  2006/07/28 09:30:11  김승희
 * exception 처리 변경
 *
 * Revision 1.19  2006/07/25 04:34:56  김성균
 * *** empty log message ***
 *
 * Revision 1.18  2006/07/25 02:32:11  김성균
 * getLocalWasConfig() 변경
 *
 * Revision 1.17  2006/07/10 01:31:24  김성균
 * *** empty log message ***
 *
 * Revision 1.16  2006/07/05 16:35:12  김성균
 * *** empty log message ***
 *
 * Revision 1.15  2006/07/05 13:31:51  김성균
 * getLocalWasConfig() AP서버로 호출하도록 수정
 *
 * Revision 1.14  2006/06/30 00:58:07  김성균
 * EJB 호출로 변경
 *
 * Revision 1.13  2006/06/26 19:52:44  김성균
 * *** empty log message ***
 *
 * Revision 1.12  2006/06/20 10:16:31  이종원
 * *** empty log message ***
 *
 * Revision 1.11  2006/06/20 10:14:35  이종원
 * *** empty log message ***
 *
 * Revision 1.10  2006/06/20 09:57:56  이종원
 * 비동기 처리 로직 구현
 *
 * Revision 1.9  2006/06/20 09:49:02  이종원
 * MESSAGE QUEUE이름 수정
 *
 * Revision 1.8  2006/06/20 09:03:54  김성균
 * 전문헤더 데이타 임시 설정부분 삭제
 *
 * Revision 1.7  2006/06/20 04:30:10  김성균
 * 기동수동구분 관련된 부분 추가
 *
 * Revision 1.6  2006/06/19 13:45:55  김성균
 * *** empty log message ***
 *
 * Revision 1.5  2006/06/17 08:40:03  김승희
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public class MessageClient {
	
	/**
	 * I : 수동(REQUEST-INT) 
	 */
	private static final String REQUEST_IN = "I";
	
	/**
	 * O : 기동(REQUEST-OUT) 
	 */
	private static final String REQUEST_OUT = "O";
	
    /**  
     * 동기 처리 EJB 의 JNDI 이름 
     */
    static String messageEjbName = "nebsoa.spiderlink.ejb.MessageProcessorEJB";
	
    /**
     * Message 클래스가 수행되는 WAS설정ID
     */
    static String MESSAGE_WAS_CONFIG_ID = PropertyManager.getInstanceProperty("MESSAGE_WAS_CONFIG", PropertyManager.getProperty("was_config", "MESSAGE_WAS_CONFIG", null));
    
    static{                                                                                                                 
    	if("LOCAL".equals(MESSAGE_WAS_CONFIG_ID)) MESSAGE_WAS_CONFIG_ID = null;                                                                
    } 
    
	/**
	 * 비동기적으로 수행할 전문처리 요청을 호출합니다. 비동기적 호출이므로 리턴 값이 없습니다.
	 */
	public static void doAsyncProcess(String serverInfo,String trxId, String orgId,String ioType, DataMap map) {

		// QueueConnectionFactory 의 JNDI 이름
		String queueConnectionFactoryName = "nebsoa.QueueConnectionFactory";
		// Queue 의 JNDI 이름
		String queueName = "nebsoa.spiderlink.MessageAsyncQueue";
		// 서버 정보 (was_config.properties.xml 파일에 있는 서버 이름)		

        //TODO 매번 OPEN-CLOSE하는 로직을 변경하고자 한다면 아래 로직을 일부 수정 
		JmsOutputAdapter asyncBean = new JmsOutputAdapter(
				queueConnectionFactoryName, queueName, serverInfo);

		// JMS 를 사용하여 MessageEngine 을 비동기적으로 호출합니다.
		asyncBean.sendObjectMessage(trxId,orgId,ioType,map);
        asyncBean.destroy();
	}
	


	/**
	 * 메세지 처리 엔진이 같은 was instance에서 수행 중일 때 동기적으로 수행할 전문처리 요청을 호출합니다.
	 * @param trxId 거래ID
	 * @param orgId 기관ID
	 * @param map 전문처리 요청에 대한 데이터를 포함하는 DataMap 객체
	 * @return 전문처리에 대한 실행이 완료된 결과 DataMap 객체
	 * @throws MessageException 
	 * @throws MessageException
	 */
	public static DataMap doSyncProcess(String trxId, String orgId, DataMap map) throws MessageException {
		
        return doSyncProcess(trxId, orgId, REQUEST_OUT, map);
	}
	
	/**
	 * 메세지 처리 엔진이 같은 was instance에서 수행 중일 때 동기적으로 수행할 전문처리 요청을 호출합니다.
	 * @param trxId 거래ID
	 * @param orgId 기관ID
	 * @param ioType 기동수동구분 
	 * @param map 전문처리 요청에 대한 데이터를 포함하는 DataMap 객체
	 * @return 전문처리에 대한 실행이 완료된 결과 DataMap 객체
	 * @throws MessageException 
	 * @throws MessageException
	 */
	public static DataMap doSyncProcess(String trxId, String orgId, String ioType, DataMap map) throws MessageException {
        return doSyncProcess(getDefaultMessageWasConfig(), trxId, orgId, ioType,  map);
	}
    
    /**
     * 메세지 처리 엔진이<font color='red'> 다른  was instance</font>에서 수행 중일 때 동기적으로 수행할 전문처리 요청을 호출합니다.
     * @param wasConfigId was Config Id
     * @param trxId 거래ID
     * @param orgId 기관ID
     * @param ioType 기동수동구분 
     * @param map 전문처리 요청에 대한 데이터를 포함하는 DataMap 객체
     * @return 전문처리에 대한 실행이 완료된 결과 DataMap 객체
     * @throws MessageException 
     * @throws MessageException
     */
    public static DataMap doSyncProcess(String wasConfigId, String trxId, String orgId, String ioType, DataMap map) throws MessageException {
        
        if (StringUtil.isNull(trxId)) {
            throw new SysException(map.getContext(), "trxId is null");
        }
        
        // ThreadLocal에 Context 객체를 가져와서 DataMap에 넣는다.
        Context ctx = ContextLogger.getContext();
        map.setContext(ctx);
        
        /////////////////////////
        if (ctx != null) {
            ctx.startActivity(Context.ACTIVITY_MESSAGE);
        }
        ///////////////////////
        
        Lock orgLock = null;
        Lock msgLock = null;
        try {
            orgLock = LockPoolManager.getInstance().getLock("ORG", orgId);
            msgLock = LockPoolManager.getInstance().getLock("MESSAGE", trxId);
	        if (wasConfigId == null) {
	        	
	        	map = doLocalSyncProcess(trxId, orgId, ioType, map);
	            return map;
	        } else {
	        	map = doRemoteSyncProcess(wasConfigId, trxId, orgId, ioType, map);
	            return map;
	        }
        } finally {
            if (orgLock != null) {
                LockPoolManager.getInstance().returnLock("ORG", orgId, orgLock);
            }
            if (msgLock != null) {
                LockPoolManager.getInstance().returnLock("MESSAGE", trxId, msgLock);
            }
            
            Context _ctx = map.getContext();
            
            // ThreadLocal에 Context 객체를 넣는다.
            ContextLogger.putContext(_ctx);

            if (_ctx != null) {
                _ctx.stopActivity();
            }
        }
    }
    
    /**
     * non Transactional하게 Business logic 수행을 호출합니다.
     * 
     * @param bizId biz class full name
     * @param DataMap data to handle(서비스(비즈니스)에 대한  데이터를 포함하는 map)
     * @return DataMap 실행이 완료된 결과를 담고 있는 DataMap 객체
     * @throws MessageException 
     */
    public static DataMap doLocalSyncProcess(String trxId, String orgId, String ioType, DataMap map) throws MessageException {
        LogManager.info("call local message service...");
        
        return MessageEngine.doSyncProcess(trxId, orgId, ioType, map);
    }
    
    /**
     * 메세지 처리 엔진이<font color='red'> 다른  was instance</font>에서 수행 중일 때 동기적으로 수행할 전문처리 요청을 호출합니다.
     * @param wasConfigId was Config Id
     * @param trxId 거래ID
     * @param orgId 기관ID
     * @param ioType 기동수동구분 
     * @param map 전문처리 요청에 대한 데이터를 포함하는 DataMap 객체
     * @return 전문처리에 대한 실행이 완료된 결과 DataMap 객체
     * @throws MessageException 
     * @throws MessageException
     */
    public static DataMap doRemoteSyncProcess(String wasConfigId,String trxId, String orgId, String ioType, DataMap map) throws MessageException {
        LogManager.info("call remote message service...");
        
        MessageProcessorEJB bean = null;
        
        try {
            bean = getEJB(wasConfigId);
            
            DataMap returnMap = bean.doSyncProcess(trxId, orgId, ioType, map);
            
            // Profiler 복사(원본 map에 리턴받은 map의 Profiler를 copy해준다.
            Context context = map.getContext();
            
            if (context != null && returnMap.getContext() != null) {
                
                context.setProfiler(returnMap.getContext().getProfiler());
            }
            return returnMap;
            
        } catch (CreateException e) {
        	LogManager.error(e);
            throw new SysException(map.getContext(), "FRS00006", "CreateException in MessageClient - 전문 처리 엔진을 호출할 수 없습니다.");
        } catch (RemoteException e) {
        	LogManager.error(e);
            Throwable th = e.getCause();
            if (th == null) {
                throw new SysException(map.getContext(), "FRS00004", "RemoteException in MessageClient - 전문 처리 엔진을 호출 하는 중 오류 발생.");
            } else if (th instanceof RuntimeException) {
                throw (RuntimeException) th;
            /**
	         * WebSphere에서는 ServerException이 RemoteException으로 catch가 된다. 그러므로 Exception.getCause()가 
	         * RemoteException일 경우에 대한 처리를 해줘야 한다. 
	         */
            } else if (th instanceof RemoteException) {
            	LogManager.error("RemoteException in MessageClient:"+th.getCause().toString(),th.getCause());
	            throw (RuntimeException)th.getCause();
    	            
            } else {
                throw new SysException(map.getContext(), "FRS00004", th);
            }
        }
    }
    
	/**
     * 전문처리엔진을 호출하기 위한 WAS CONFIG 
	 * @return
	 */
	private static String getDefaultMessageWasConfig() {
        return MESSAGE_WAS_CONFIG_ID;
		//return PropertyManager.getProperty("was_config", "MESSAGE_WAS_CONFIG", null);
    }

    /**
     * MessageProcessorEJB
     * @param wasConfig
     * @return
     * @throws RemoteException
     * @throws CreateException
     */
    public static MessageProcessorEJB getEJB(String wasConfig) throws RemoteException, CreateException{
        MessageProcessorHome home = getEJBHome(wasConfig,true);

        MessageProcessorEJB bean;
        try {
            bean = home.create();
        } catch (CreateException e) {
            home = getEJBHome(wasConfig, false);
            return home.create();
        }
        return bean;
    }


    /**
     * @param wasConfig
     * @param useCache
     * @return
     */
    private static MessageProcessorHome getEJBHome(String wasConfig,boolean useCache) {
        if(!useCache){
            EJBManager.removeCache(messageEjbName,wasConfig);
        }
        MessageProcessorHome home =(MessageProcessorHome)
        EJBManager.lookup(messageEjbName,wasConfig);
        return home;
    }    
}
