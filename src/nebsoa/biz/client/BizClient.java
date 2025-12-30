/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.biz.client;

import java.rmi.RemoteException;

import javax.ejb.CreateException;

import nebsoa.biz.base.Biz;
import nebsoa.biz.ejb.BizProcessorEJB;
import nebsoa.biz.ejb.BizProcessorHome;
import nebsoa.biz.exception.BizException;
import nebsoa.biz.factory.BizFactory;
import nebsoa.biz.util.BizInfo;
import nebsoa.biz.util.BizManager;
import nebsoa.common.Context;
import nebsoa.common.exception.SysException;
import nebsoa.common.jndi.EJBManager;
import nebsoa.common.log.LogManager;
import nebsoa.common.monitor.ContextLogger;
import nebsoa.common.util.DataMap;
import nebsoa.common.util.PropertyManager;
import nebsoa.common.util.StringUtil;
import nebsoa.spiderlink.engine.adapter.jms.JmsOutputAdapter;
import nebsoa.util.async.AsyncQueueException;
import nebsoa.util.async.AsyncQueueManager;
import nebsoa.util.async.QueueConfig;
import nebsoa.util.lockpool.Lock;
import nebsoa.util.lockpool.LockPoolManager;

/*******************************************************************
 * <pre>
 * 1.설명 
 * Biz Logic 의 호출을 대리해 주기 위한 Client
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
 * $Log: BizClient.java,v $
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
 * Revision 1.2  2008/08/04 09:46:26  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.3  2008/07/04 06:56:20  김은정
 * doBizAsyncQueueProcess추가구현
 *
 * Revision 1.2  2008/03/05 00:24:12  오재훈
 * WebSphere 에서는 RemoteException으로 catch를 하여도 실제 잡힌건 ServerExcetpion이 잡힙니다. 그래서 Exception.getCause()했을때도 RemoteException이면 getCause()해서 던지는 로직을 추가하였습니다.
 *
 * Revision 1.1  2008/01/22 05:58:35  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:37:45  안경아
 * *** empty log message ***
 *
 * Revision 1.43  2007/10/04 04:02:24  김성균
 * *** empty log message ***
 *
 * Revision 1.42  2007/10/02 09:13:50  김성균
 * ContextLogger 적용
 *
 * Revision 1.41  2007/09/17 11:06:05  오재훈
 * *** empty log message ***
 *
 * Revision 1.40  2007/09/17 09:23:28  최수종
 * 리턴받은 map객체에서 context를 얻어서 처리하도록 로직 수정
 *
 * Revision 1.39  2007/09/17 02:10:28  이종원
 * Context Monitor 로직 수정
 *
 * Revision 1.38  2007/09/17 02:05:03  이종원
 * Context Monitor 로직 추가
 *
 * Revision 1.37  2007/06/26 01:07:04  홍윤석
 * *** empty log message ***
 *
 * Revision 1.36  2007/05/03 09:36:10  안경아
 * *** empty log message ***
 *
 * Revision 1.35  2007/05/01 07:39:58  김성균
 * Lock 처리 기능 추가
 *
 * Revision 1.34  2007/03/20 07:42:59  김성균
 * *** empty log message ***
 *
 * Revision 1.33  2007/03/20 07:40:11  김성균
 * BIZ_EJB_NAME 값 없을경우 처리
 *
 * Revision 1.32  2007/03/20 02:46:36  김성균
 * 설정으로 EJB 및 EJB가 수행되는 WAS 변경가능하도록 기능추가
 *
 * Revision 1.31  2007/03/07 04:27:06  김성균
 * 일부 로그 INFO 레벨로 변경
 *
 * Revision 1.30  2007/03/07 02:40:17  김성균
 * BizInfo 못 찾았을 경우 예외처리 통일
 *
 * Revision 1.29  2007/01/30 02:28:11  김성균
 * 오류메시지 수정
 *
 * Revision 1.28  2007/01/09 00:48:44  안경아
 * *** empty log message ***
 *
 * Revision 1.27  2007/01/08 13:01:41  안경아
 * *** empty log message ***
 *
 * Revision 1.26  2007/01/08 12:31:16  안경아
 * *** empty log message ***
 *
 * Revision 1.25  2007/01/08 12:25:03  안경아
 * *** empty log message ***
 *
 * Revision 1.24  2007/01/08 12:24:41  안경아
 * *** empty log message ***
 *
 * Revision 1.23  2006/12/18 06:14:54  김성균
 * *** empty log message ***
 *
 * Revision 1.22  2006/11/10 08:50:53  김성균
 * *** empty log message ***
 *
 * Revision 1.21  2006/10/31 00:29:45  김성균
 * *** empty log message ***
 *
 * Revision 1.20  2006/10/31 00:17:42  김성균
 * *** empty log message ***
 *
 * Revision 1.19  2006/10/09 12:17:38  김성균
 * *** empty log message ***
 *
 * Revision 1.18  2006/10/08 07:28:40  김성균
 * *** empty log message ***
 *
 * Revision 1.17  2006/08/22 02:06:46  김성균
 * *** empty log message ***
 *
 * Revision 1.16  2006/08/19 02:02:59  김성균
 * *** empty log message ***
 *
 * Revision 1.15  2006/08/02 05:35:23  김성균
 * BizInfo 사용하지 않도록...
 *
 * Revision 1.14  2006/08/01 01:41:08  김성균
 * 오류코드 처리
 *
 * Revision 1.13  2006/06/26 19:57:40  김성균
 * *** empty log message ***
 *
 * Revision 1.12  2006/06/22 04:46:50  김성균
 * BIZ_APP_ID 매핑 테이블에서 가져오는 메소드 추가
 *
 * Revision 1.11  2006/06/20 10:57:12  이종원
 * *** empty log message ***
 *
 * Revision 1.9  2006/06/17 10:37:45  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class BizClient {
    /**  
     * non tx EJB 의 JNDI 이름 
     */
    static final String bizEjbName = "nebsoa.biz.ejb.BizProcessorEJB";
    
    /**  
     * tx EJB 의 JNDI 이름 
     */
    static final String txBizEjbName = "nebsoa.biz.ejb.TxBizProcessorEJB";

    /**
     * BIZ 클래스가 수행되는 WAS설정ID
     */
    static String BIZ_WAS_CONFIG_ID = PropertyManager.getInstanceProperty("BIZ_WAS_CONFIG", PropertyManager.getProperty("was_config", "BIZ_WAS_CONFIG", null));
    
    static{                                                                                                                 
    	if("LOCAL".equals(BIZ_WAS_CONFIG_ID)) BIZ_WAS_CONFIG_ID = null;                                                                
    }
    
    /**
     * BIZ요청 QUEUE가  존재하는 WAS설정ID
     */
    static String BIZ_QUEUE_WAS_CONFIG = PropertyManager.getInstanceProperty("BIZ_QUEUE_WAS_CONFIG", 
			PropertyManager.getProperty("was_config", "BIZ_QUEUE_WAS_CONFIG", null));
    static{
    	if("LOCAL".equals(BIZ_QUEUE_WAS_CONFIG)) BIZ_QUEUE_WAS_CONFIG = null;
	}  
    
    /**
     * 비동기적으로 수행할 전문처리 요청을 호출합니다. 비동기적 호출이므로 리턴 값이 없습니다.
     */
    public static void doBizAsyncProcess(String bizId, DataMap map) throws BizException {

        // QueueConnectionFactory 의 JNDI 이름
        String queueConnectionFactoryName = "nebsoa.QueueConnectionFactory";
        // Queue 의 JNDI 이름
        String queueName = "nebsoa.biz.BizAsyncQueue";

        JmsOutputAdapter asyncBean = new JmsOutputAdapter(
                queueConnectionFactoryName, queueName, BIZ_QUEUE_WAS_CONFIG);

        // JMS 를 사용하여 MessageEngine 을 비동기적으로 호출합니다.
        asyncBean.sendObjectMessage(bizId,map);
        asyncBean.destroy();
    }
    
    /**
     * 비동기적으로 수행할 전문처리 요청을 호출합니다. 비동기적 호출이므로 리턴 값이 없습니다.
     */
    public static void doBizAsyncQueueProcess(String configId, DataMap map) throws Exception {

    	LogManager.debug(" ############## [BizClient.doBizAsyncQueueProcess start] #############");
		LogManager.debug(" ############## [AsyncQueueManager start] #############");

        Context ctx = map.getContext();
        if (ctx != null) {
        	LogManager.debug(" ############## [ctx != null start] #############");
        	LogManager.debug(" ### trxSerNo = " + ctx.getTrxSerNo());
        	LogManager.debug(" ### userId = " + ctx.getUserId());
        }
        
		QueueConfig config = AsyncQueueManager.getInstance().getQueueConfig(configId,true);
		LogManager.debug(" ### QueueConfig = " + config);
		config.getNextQueue().put(map);
//		config.setRunning(false);
		LogManager.debug(" ############################################");	
		
    }    
    
    /**
     * non Transactional하게 Business logic 수행을 호출합니다.
     * 
     * @param bizId biz class full name
     * @param DataMap data to handle(서비스(비즈니스)에 대한  데이터를 포함하는 map)
     * @return DataMap 실행이 완료된 결과를 담고 있는 DataMap 객체
     * @throws BizException 
     */
    public static DataMap doBizProcess(String bizId, DataMap map) throws BizException {
        if (StringUtil.isNull(bizId)) {
            throw new SysException(map.getContext(), "bizId is null");
        }
        Lock lock = null;
        try {
	        lock = LockPoolManager.getInstance().getLock("BIZ", bizId);
	        if (BIZ_WAS_CONFIG_ID == null) {
	            return doLocalBizProcess(bizId, map);
	        } else {
	            return doRemoteProcess(bizId, map);
	        }
	    } finally {
            if (lock != null) {
                LockPoolManager.getInstance().returnLock("BIZ", bizId, lock);
            }
        }
    }
    
    /**
     * non Transactional하게 Business logic 수행을 호출합니다.
     * 
     * @param bizId biz class full name
     * @param DataMap data to handle(서비스(비즈니스)에 대한  데이터를 포함하는 map)
     * @return DataMap 실행이 완료된 결과를 담고 있는 DataMap 객체
     * @throws BizException 
     */
    public static DataMap doLocalBizProcess(String bizId, DataMap map) throws BizException {
        LogManager.info("call local biz service...");
        
        // ThreadLocal에 Context 객체를 가져와서 DataMap에 넣는다.
        Context ctx = ContextLogger.getContext();
        map.setContext(ctx);
        
//      start step
        if (ctx != null) {
            ctx.startStep(Context.STEP_BIZ);
        }
        
        try {
            BizInfo bizInfo = BizManager.getInstance().getBizInfo(bizId);
            Biz biz = BizFactory.getInstance().getBiz(bizInfo);

            map = biz.doProcess(map);
            return map;
        } finally {

            Context _ctx = map.getContext();
            
            // ThreadLocal에 Context 객체를 넣는다.
            ContextLogger.putContext(_ctx);
            
            if (_ctx != null) {
                _ctx.stopStep(Context.STEP_BIZ);
            }
        }
    }
        
    public static DataMap doRemoteProcess(String bizId, DataMap map) throws BizException {     
        LogManager.info("call remote biz service...");
        
        BizProcessorEJB bean = null; 

        // ThreadLocal에 Context 객체를 가져와서 DataMap에 넣는다.
        Context ctx = ContextLogger.getContext();
        map.setContext(ctx);
        
        try {
            String wasConfig = PropertyManager.getProperty("was_config", bizId + ".BIZ_WAS_CONFIG", null);
            if (StringUtil.isNull(wasConfig)) {
                wasConfig = BIZ_WAS_CONFIG_ID;
            }
            
            if (ctx != null) {
                ctx.startActivity(Context.ACTIVITY_LOOKUP);
            }
            
            try {
                bean = getEJB(wasConfig, bizId);
                if (bean == null) {
                    throw new SysException("BIZ EJB생성에 실패하였습니다.");
                }
            } finally {
                if (ctx != null) {
                    ctx.stopActivity();
                }
            }
            
            if (ctx != null) {
                ctx.startStep(Context.STEP_BIZ);
            }

            try {
                map = bean.doBizProcess(bizId, map);
            } finally {
                Context _ctx = map.getContext();
                
                // ThreadLocal에 Context 객체를 넣는다.
                ContextLogger.putContext(_ctx);

                if (_ctx != null) {
                    _ctx.stopStep(Context.STEP_BIZ);
                }
            }
        } catch (BizException e) {
            throw e;
        } catch (CreateException e) {
            throw new SysException(map.getContext(), "FRS00005", "CreateException in BIZ - 클래스를 호출할 수 없습니다.");
        } catch (RemoteException e) {
            Throwable th = e.getCause();
            if (th == null) {
                throw new SysException(map.getContext(), "FRS00004", "RemoteException in Biz - BIZ 클래스를 호출 하는 중 오류 발생.");
            } else if (th instanceof RuntimeException) {
            	LogManager.error("RemoteException in Biz-RuntimeException:"+th.toString(),th);
	            throw (RuntimeException) th;
            /**
	         * WebSphere에서는 ServerException이 RemoteException으로 catch가 된다. 그러므로 Exception.getCause()가 
	         * RemoteException일 경우에 대한 처리를 해줘야 한다. 
	         */
            } else if (th instanceof RemoteException) {
            	LogManager.error("RemoteException in MessageClient:"+th.getCause().toString(),th.getCause());
	            throw (RuntimeException)th.getCause();
	    	            

            } else {
            	LogManager.error("RemoteException in Biz-"+th.toString(),th);
            	throw new SysException("FRS00004", th);
            }
        }
        
        return map;
    }

    /**
     * @param wasConfig
     * @return
     * @throws RemoteException
     * @throws CreateException
     * @throws BizException 
     */
    public static BizProcessorEJB getEJB(String wasConfig, String bizId) throws RemoteException, CreateException, BizException{
        BizProcessorHome home = getEJBHome(wasConfig, bizId, true);

        BizProcessorEJB bean;
        try {
            bean = home.create();              
        } catch (CreateException e) {
            home = getEJBHome(wasConfig, bizId, false);
            return home.create();            
        }
        return bean;
    }

    /**
     * @param wasConfig
     * @param useCache
     * @return
     * @throws BizException 
     */
    
    private static BizProcessorHome getEJBHome(String wasConfig, String bizId, boolean useCache) throws BizException {
        
    	BizInfo bizInfo = BizManager.getInstance().getBizInfo(bizId);
    	
    	String ejbName = bizInfo.getQueName();
    	
    	//String ejbName = PropertyManager.getProperty("was_config", bizId + ".BIZ_EJB_NAME", null);
    	
    	if(StringUtil.contains(bizId, "admin")) ejbName = PropertyManager.getProperty("was_config", "admin.BIZ_EJB_NAME", null);        
        if (StringUtil.isNull(ejbName)) {
            ejbName = bizEjbName;
        }
        
        if (!useCache) {
            EJBManager.removeCache(ejbName, wasConfig);
        }
        BizProcessorHome home = 
            (BizProcessorHome) EJBManager.lookup(ejbName, wasConfig);
        return home;
    }
}