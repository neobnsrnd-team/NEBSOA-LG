/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.service.client;

import java.rmi.RemoteException;

import javax.ejb.CreateException;

import nebsoa.common.Context;
import nebsoa.common.acl.UserSession;
import nebsoa.common.exception.SysException;
import nebsoa.common.jndi.EJBManager;
import nebsoa.common.log.LogManager;
import nebsoa.common.monitor.ContextLogger;
import nebsoa.common.startup.StartupContext;
import nebsoa.common.util.DataMap;
import nebsoa.common.util.PropertyManager;
import nebsoa.service.ejb.ServiceProcessorEJB;
import nebsoa.service.ejb.ServiceProcessorHome;
import nebsoa.service.engine.ServiceEngine;
import nebsoa.service.exception.ServiceException;
import nebsoa.service.session.SessionValueSetterFactory;
import nebsoa.service.util.ServiceManager;
import nebsoa.util.lockpool.Lock;
import nebsoa.util.lockpool.LockPoolManager;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 서비스 호출을 대리해 주기 위한 Client
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
 * $Log: ServiceClient.java,v $
 * Revision 1.1  2018/01/15 03:39:54  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:39  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:27  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:29  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.2  2008/08/04 09:46:33  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.13  2008/06/23 05:27:48  김영석
 * 서비스 접근허용자 로직 추가
 *
 * Revision 1.12  2008/05/19 04:45:22  김영석
 * 서비스 옵션 체크 로직 변경
 *
 * Revision 1.11  2008/03/13 02:05:25  김영석
 * 로그인 상태 체크 로직 추가
 *
 * Revision 1.10  2008/03/05 02:41:31  김성균
 * Websphere 일 경우 예외처리 원복
 *
 * Revision 1.9  2008/03/03 05:24:02  김성균
 * RemoteException() 처리 수정
 *
 * Revision 1.8  2008/03/03 04:26:51  김성균
 * 예외처리 수정
 *
 * Revision 1.6  2008/02/21 07:23:48  오재훈
 * *** empty log message ***
 *
 * Revision 1.5  2008/02/14 09:27:30  김성균
 * 예외처리 및 프로파일관련 수정
 *
 * Revision 1.4  2008/02/14 04:34:29  오재훈
 * web.login.Authenticator, Authorizor 클래스들과 web.session.SessionManager,UserInfo 들이 삭제되고 common.acl 패키지의 클래스들로 대체되었습니다.
 * web.util.Request.Util 클래스도 common.util.RequestUtil로 옴겨졌습니다.
 *
 * Revision 1.3  2008/01/25 01:46:53  김영석
 * *** empty log message ***
 *
 * Revision 1.2  2008/01/24 05:35:04  최수종
 * Exception 처리 수정
 *
 * Revision 1.1  2008/01/22 05:58:33  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.5  2008/01/07 01:36:19  김성균
 * 타행상태 체크기능 추가
 *
 * Revision 1.4  2008/01/04 08:24:32  김성균
 * 같은 WAS 인스턴스에 대한 요청일 경우 로컬호출을 하도록 변경
 *
 * Revision 1.3  2008/01/04 02:54:25  김성균
 * 서비스제공시간 및 영업일 체크기능 추가
 *
 * Revision 1.2  2008/01/02 09:45:43  김성균
 * WORK_SPACE 기능 추가
 *
 * Revision 1.1  2007/12/18 04:55:33  김성균
 * 서비스관련 패키지 최초등록
 *
 * </pre>
 ******************************************************************/
public class ServiceClient {
	/**  
     * Service EJB 의 JNDI 이름 
     */
    static final String SERVICE_EJB_JNDI_NAME = "nebsoa.service.ejb.ServiceProcessorEJB";
    
    /**
     * 서비스가 수행되는 WAS_ID
     */
    static String SERVICE_WAS_CONFIG_ID = PropertyManager.getInstanceProperty("SERVICE_WAS_CONFIG_ID", PropertyManager.getProperty("was_config", "SERVICE_WAS_CONFIG_ID", null));
    
    static{                                                                                                                 
    	if("LOCAL".equals(SERVICE_WAS_CONFIG_ID)) SERVICE_WAS_CONFIG_ID = null;                                                                
    } 
    /**
     * 서비스 수행을 호출합니다.
     * 
     * @param serviceId 
     * @param DataMap data to handle(서비스에 대한  데이터를 포함하는 map)
     * @return DataMap 실행이 완료된 결과를 담고 있는 DataMap 객체
     * @throws ServiceException 
     */
    public static DataMap doServiceProcess(String serviceId, DataMap map) throws ServiceException {
    	
    	// 중지상태인 서비스 인지 체크 및 접근허용자 체크
    	ServiceManager.checkServiceStopYn(serviceId, map);
    	
    	// 로그인 체크한다.
    	ServiceManager.checkLoginStatus(serviceId, map);
    	
    	// 영업일 및 서비스 제공시간을 체크한다.
   		ServiceManager.checkServiceEnableTime(serviceId);

        // 타행상태체크여부가 설정되어 있으면 타행상태를 체크한다.
        ServiceManager.checkBankStatus(serviceId, map);
        
        // 전자서명로그여부가 설정되어 있으면 전자서명로그를 체크한다.
        ServiceManager.checkSecureSignEnable(serviceId, map);

        Lock lock = null;
        String workSpaceId = null;
        try {
            workSpaceId = ServiceManager.getWorkSpaceId(serviceId);
	        lock = LockPoolManager.getInstance().getLock("SERVICE", workSpaceId);
	        if (SERVICE_WAS_CONFIG_ID == null || StartupContext.getInstanceId().equals(SERVICE_WAS_CONFIG_ID)) {
	            return doLocalServiceProcess(serviceId, map);
	        } else {
	            return doRemoteServiceProcess(serviceId, map);
	        }
	    } finally {
            if (lock != null) {
                LockPoolManager.getInstance().returnLock("SERVICE", workSpaceId, lock);
            }
        }
    }

    /**
     * 서비스 수행을 호출합니다.
     * 
     * @param serviceId 
     * @param session HttpRequest에 있는 HttpSession 정보
     * @param DataMap data to handle(서비스에 대한  데이터를 포함하는 map)
     * @return DataMap 실행이 완료된 결과를 담고 있는 DataMap 객체
     * @throws ServiceException 
     */
    public static DataMap doServiceProcess(String serviceId, UserSession userInfo, DataMap map) throws ServiceException {
            	
        //세션 헤더 정보 셋팅
        map = SessionValueSetterFactory.getInstance().getSessionValueSetter(map.getContextName()).setSystemKeyword(userInfo, map);
        return doServiceProcess(serviceId, map);
    }
    
    /**
     * 서비스 수행을 호출합니다.
     * 
     * @param serviceId 서비스ID 
     * @param DataMap data to handle(서비스(비즈니스)에 대한  데이터를 포함하는 map)
     * @return DataMap 실행이 완료된 결과를 담고 있는 DataMap 객체
     * @throws ServiceException 
     */
    protected static DataMap doLocalServiceProcess(String serviceId, DataMap map) throws ServiceException {
        LogManager.info("call local service...");
        
        // ThreadLocal에 Context 객체를 가져와서 DataMap에 넣는다.
        Context ctx = ContextLogger.getContext();
        map.setContext(ctx);
        
        try {
        	if (ctx != null) {
                ctx.startStep(Context.STEP_BIZ);
            }
            map = ServiceEngine.doSyncProcess(serviceId, map);
        } finally {
            ctx = map.getContext();
            
            // ThreadLocal에 Context 객체를 넣는다.
            ContextLogger.putContext(ctx);
            
            if (ctx != null) {
                ctx.stopStep(Context.STEP_BIZ);
            }
        }
        
        return map;
    }
    
    /**
     * @param serviceId
     * @param map
     * @return
     * @throws ServiceException
     */
    protected static DataMap doRemoteServiceProcess(String serviceId, DataMap map) throws ServiceException {     
        LogManager.info("call remote service...");
        
        ServiceProcessorEJB bean = null; 

        // ThreadLocal에 Context 객체를 가져와서 DataMap에 넣는다.
        Context ctx = ContextLogger.getContext();
        map.setContext(ctx);
        
        try {
            String wasConfig = SERVICE_WAS_CONFIG_ID;
            
            try {
            	if (ctx != null) {
                    ctx.startActivity(Context.ACTIVITY_LOOKUP);
                }
                bean = getEJB(wasConfig, serviceId);
                if (bean == null) {
                    throw new SysException("SERVICE EJB생성에 실패하였습니다.");
                }
            } finally {
                if (ctx != null) {
                    ctx.stopActivity();
                }
            }
            
            try {
            	if (ctx != null) {
                    ctx.startStep(Context.STEP_BIZ);
                }
                map = bean.doServiceProcess(serviceId, map);
            } finally {
                ctx = map.getContext();
                
                // ThreadLocal에 Context 객체를 넣는다.
                ContextLogger.putContext(ctx);

                if (ctx != null) {
                    ctx.stopStep(Context.STEP_BIZ);
                }
            }
        } catch (CreateException e) {
            throw new SysException(map.getContext(), "FRS00005", "CreateException in Service - 서비스를 호출할 수 없습니다.");
        } catch (RemoteException e) {
            Throwable th = e.getCause();
            if (th == null) {
                throw new SysException(map.getContext(), "FRS00004", "RemoteException in Service - 서비스를 호출 하는 중 오류 발생.");
            } else if (th instanceof RuntimeException) {
            	LogManager.error("RemoteException in Service-RuntimeException:"+th.toString(),th);
	            throw (RuntimeException) th;
            /**
	         * WebSphere에서는 ServerException이 RemoteException으로 catch가 된다. 그러므로 Exception.getCause()가 
	         * RemoteException일 경우에 대한 처리를 해줘야 한다. 
	         */
            } else if (th instanceof RemoteException) {
            	LogManager.error("RemoteException in MessageClient:"+th.getCause().toString(),th.getCause());
	            throw (RuntimeException)th.getCause();
            } else {
            	LogManager.error("RemoteException in Service-"+th.toString(),th);
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
     */
    protected static ServiceProcessorEJB getEJB(String wasConfig, String serviceId) 
    throws RemoteException, CreateException {
        ServiceProcessorHome home = getEJBHome(wasConfig, serviceId, true);

        ServiceProcessorEJB bean;
        try {
            bean = home.create();              
        } catch (CreateException e) {
            home = getEJBHome(wasConfig, serviceId, false);
            return home.create();            
        }
        return bean;
    }

    /**
     * @param wasConfig
     * @param useCache
     * @return
     */
    protected static ServiceProcessorHome getEJBHome(String wasConfig, String serviceId, boolean useCache) {
        
    	String ejbName = SERVICE_EJB_JNDI_NAME;
    	
        if (!useCache) {
            EJBManager.removeCache(ejbName, wasConfig);
        }
        
        ServiceProcessorHome home = (ServiceProcessorHome) EJBManager.lookup(ejbName, wasConfig);
        
        return home;
    }
}