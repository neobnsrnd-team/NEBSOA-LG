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

import nebsoa.biz.ejb.RPCEJB;
import nebsoa.biz.ejb.RPCHome;
import nebsoa.common.Context;
import nebsoa.common.exception.SysException;
import nebsoa.common.jndi.EJBManager;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.DataMap;
import nebsoa.common.util.MethodInvokerUtil;
import nebsoa.common.util.PropertyManager;
import nebsoa.common.util.StringUtil;

/*******************************************************************
 * <pre>
 * 1.설명 
 * REMOTE API 의 호출을 대리해 주기 위한 Client
 * 
 * 2.사용법
 *     DataMap map = new DataMap();
 *     map.put("이거","그거");
 *     map = RPCClient.doRPC("test", "test.biz.RPCTestTarget","go",map);
 *     LogManager.debug(" 수행 후 받은 맵 데이터 : "+map);
 * <font color="red">
 * 3.주의사항
 *   모든   RPC모듈은 반드시 Default생성자를 가져야 하며, 외부에서 호출 할 메소드는 
 *   public 하며, dataMap을 인자로 받아, DataMap을 리턴하도록 구현하여야 합니다.
 * </font>
 *
 * @author 이종원
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 
 * $Log: RPCClient.java,v $
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
 * Revision 1.3  2008/08/13 07:27:48  youngseokkim
 * 패키지이름 변경
 *
 * Revision 1.2  2008/08/04 09:46:26  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.3  2008/04/08 04:46:07  김승희
 * 메소드 추가
 *
 * Revision 1.2  2008/02/14 09:01:05  김성균
 * MethodInvokerUtil 패키지 리팩토링
 *
 * Revision 1.1  2008/01/22 05:58:35  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:37:45  안경아
 * *** empty log message ***
 *
 * Revision 1.11  2007/06/12 07:52:34  김승희
 * 리턴 타입 관련 변경
 *
 * Revision 1.10  2007/05/09 06:59:24  김승희
 * 로컬 클래스 call 가능하도록 수정
 *
 * Revision 1.9  2007/05/03 10:27:24  이종원
 * *** empty log message ***
 *
 * Revision 1.8  2007/02/05 01:59:35  김승희
 * exception 처리 재변경
 *
 * Revision 1.7  2007/02/05 01:45:52  김승희
 * exception 변경
 *
 * Revision 1.6  2007/02/02 09:39:27  김승희
 * 싱글톤으로 구현한 모듈 호출 가능하도록 수정
 *
 * Revision 1.5  2007/02/01 11:51:56  이종원
 * 완성본 초안 작성
 *
 * Revision 1.4  2007/02/01 11:43:40  이종원
 * *** empty log message ***
 *
 * Revision 1.3  2007/02/01 11:27:03  이종원
 * *** empty log message ***
 *
 * Revision 1.2  2007/02/01 11:25:29  이종원
 * *** empty log message ***
 *
 * Revision 1.1  2007/01/29 09:40:20  이종원
 * 최초작성
 *
 * </pre>
 ******************************************************************/
public class RPCClient {
    /**  
     * rpc EJB 의 JNDI 이름 
     */
	public static final String DEFAULT_EJB_NAME = "nebsoa.biz.ejb.RPCEJB";
    
    /**  
     * rpc was의 default 값 
     */
    public static final String DEFAULT_RPC_WAS_ID="RPC";
    
    /**
     * BIZ 클래스가 수행되는 WAS설정ID
     */
    static String RPC_WAS_CONFIG_ID = PropertyManager.getInstanceProperty("RPC_WAS_CONFIG", PropertyManager.getProperty("was_config", "RPC_WAS_CONFIG", null));
    
    static{                                                                                                                 
    	if("LOCAL".equals(RPC_WAS_CONFIG_ID)) RPC_WAS_CONFIG_ID = null;                                                                
    } 
    
    /**
     * DEFAULT_RPC_WAS_ID값에 정의 된 WAS에서 해당 모듈을 수행후 결과를 dataMap으로 받아 옵니다.
     * 따라서 RPC모듈은 반드시 Default생성자 및 dataMap을 인자로 받아,
     * DataMap을 리턴하도록 구현하여야 합니다.
     * 2007. 2. 1.  이종원 작성
     * @param wasId
     * @param className
     * @param methodName
     * @param map
     * @return
     * @throws Exception
     */
    public static DataMap doRPC(String className,
            String methodName, DataMap map) throws Exception {     
        return doRPC(RPC_WAS_CONFIG_ID, className,
                 methodName, map, false);
    }
    
    /**
     * DEFAULT_RPC_WAS_ID값에 정의 된 WAS에서 해당 모듈을 수행후 결과를 dataMap으로 받아 옵니다.
     * 따라서 RPC모듈은 반드시 Default생성자 및 dataMap을 인자로 받아,
     * DataMap을 리턴하도록 구현하여야 합니다.
     * 2007. 2. 1.  이종원 작성
     * @param wasId
     * @param className
     * @param methodName
     * @param map
     * @param 싱글톤 패턴 여부
     * @return
     * @throws Exception
     */
    public static DataMap doRPC(String className,
            String methodName, DataMap map, boolean isSingleton) throws Exception {     
        return doRPC(RPC_WAS_CONFIG_ID, className,
                 methodName, map, isSingleton);
    }
    
    /**
     * DEFAULT_RPC_WAS_ID값에 정의 된 WAS에서 해당 모듈을 수행후 결과를 dataMap으로 받아 옵니다.
     * 따라서 RPC모듈은 반드시 Default생성자 및 dataMap을 인자로 받아,
     * DataMap을 리턴하도록 구현하여야 합니다.
     * 2007. 2. 1.  이종원 작성
     * @param wasId
     * @param className
     * @param methodName
     * @param map
     * @return
     * @throws Exception 
     */
    public static DataMap doRPC(String wasId, String className,
            String methodName, DataMap map) throws Exception {  
    	return doRPC(wasId, className, methodName, map, false);
    }
    /**
     * 인자로 받은 WAS에서 해당 모듈을 수행후 결과를 dataMap으로 받아 옵니다.
     * 따라서 RPC모듈은 반드시 Default생성자 및 dataMap을 인자로 받아,
     * DataMap을 리턴하도록 구현하여야 합니다.
     * 2007. 2. 1.  이종원 작성
     * @param wasId
     * @param className
     * @param methodName
     * @param map
     * @param 싱글톤 패턴 여부
     * @return
     * @throws Exception
     */
    public static DataMap doRPC(String wasId, String className,
            String methodName, DataMap map, boolean isSingleton) throws Exception {     

    	//wasId가 null이거나 공백이면 ejb를 호출하지 않고 로컬 클래스를 호출한다. 
    	if(StringUtil.isNull(wasId)){
        	map = doLocalCall(className, methodName, map, isSingleton);
        }
    	else{
    		map = doRemoteCall(wasId, className, methodName, map, isSingleton);
    	}
        return map;
    }
    
    /**
     * 리모트 또는 로컬에 있는 클래스의 특정 메소드 호출을 합니다.
     * @param className 클래스명
     * @param methodName 메소드명
     * @param argumentClass 인자 클래스 배열
     * @param argumentValue 인자값 배열
     * @return 결과
     * @throws Exception
     */
    public static Object doRPC(String className,
            String methodName, Class[] argumentClass, Object[] argumentValue) throws Exception {  
    	return doRPC(RPC_WAS_CONFIG_ID, DEFAULT_EJB_NAME, className, methodName, argumentClass, argumentValue);
    }
    
    /**
     * 특정 WAS 클래스의 메소드를 호출합니다.
     * ejbJndiName이 null또는 공백인 경우 DEFAULT_EJB_NAME 을 사용합니다.
     *  
     * @param wasId WAS ID
     * @param ejbJndiName RPC EJB Jndi name 
     * @param className 클래스명
     * @param methodName 메소드명
     * @param argumentClass 인자 클래스 배열
     * @param argumentValue 인자값 배열
     * @return 결과
     * @throws Exception
     */
    public static Object doRPC(String wasId, String ejbJndiName, String className,
            String methodName, Class[] argumentClass, Object[] argumentValue) throws Exception {  
    	
    	//wasId가 null이거나 공백이면 ejb를 호출하지 않고 로컬 클래스를 호출한다. 
    	if(StringUtil.isNull(wasId)){
    		return doLocalCall(className, methodName, argumentClass, argumentValue);
        }
    	else{
    		return doRemoteCall(wasId, ejbJndiName, className, methodName, argumentClass, argumentValue);
    	}
        
    }
    
    /**
     * 특정 WAS 클래스의 메소드를 호출합니다.
     * @param provicerURL 해당 was의 provicerURL
     * @param contextFactory 해당 was의 contextFactory
     * @param ejbJndiName RPC EJB Jndi name 
     * @param className 클래스명
     * @param methodName 메소드명
     * @param argumentClass 인자 클래스 배열
     * @param argumentValue 인자값 배열
     * @return 결과
     * @throws Exception
     */
    public static Object doRPC(String provicerURL, String contextFactory, String ejbJndiName, String className,
            String methodName, Class[] argumentClass, Object[] argumentValue) throws Exception {  
    	return doRemoteCall(provicerURL, contextFactory, ejbJndiName, className, methodName, argumentClass, argumentValue);
     
    }
    
    private static Object doLocalCall(String className,
        String methodName, Class[] argumentClass, Object[] argumentValue) throws Exception {
    	LogManager.debug("local class[" + className +"] " + methodName +" call..");
        return MethodInvokerUtil.invoke(className, methodName, argumentClass, argumentValue);
	    
    }
    
    private static Object doRemoteCall(String wasId, String ejbJndiName, String className,
            String methodName, Class[] argumentClass, Object[] argumentValue) throws Exception {
    	return doRemoteCall(wasId, ejbJndiName, className,
                methodName, argumentClass, argumentValue, 0);
    }
    
	private static Object doRemoteCall(String wasId, String ejbJndiName, String className,
            String methodName, Class[] argumentClass, Object[] argumentValue, int tryCnt) throws Exception {
		LogManager.debug("remote class[" + className +"] " + methodName +" call..");
				
		RPCEJB bean = null; 
        
        try {
            bean = getEJB(wasId, ejbJndiName);
            if(bean ==null) throw new SysException("RPC EJB생성에 실패하였습니다.");
           
            Object obj =  bean.doRPC(className, methodName, argumentClass, argumentValue);
            return obj;
        } catch (CreateException e) {
            throw new SysException("FRS00005", className + "호출에 장애 발생. 클래스를 호출할 수 없습니다.(ejbCreateException)");
        
        } catch(java.rmi.ConnectException e){
        	
        	//ejb home 캐쉬를 제거한다.
        	EJBManager.removeCache(ejbJndiName, wasId);
        	
        	//재시도
        	if(tryCnt==0){
        		LogManager.error("RPCClient 호출 장애:ejb캐쉬를 제거하고 재시도합니다.", e);
        		return doRemoteCall(wasId, ejbJndiName, className, methodName, argumentClass, argumentValue, 1);
        	}else{
        		throw new SysException("FRS00004", className + "호출에 장애 발생." + e.toString(), e);
        	}
        } catch (RemoteException e) {
            Throwable th = e.getCause();
            if (th == null) {
            	//ejb home 캐쉬를 제거한다.
            	EJBManager.removeCache(ejbJndiName, wasId);
                throw new SysException("FRS00004", className + "호출에 장애 발생." + e.toString(), e);
            } else if (th instanceof RuntimeException) {
            	LogManager.error("RemoteException in RPC-RuntimeException:"+th.toString(),th);
	            throw (RuntimeException) th;
            } else {
            	LogManager.error("RemoteException in RPC-"+th.toString(),th);
            	throw new SysException("FRS00004", th);
            }
        } catch (SysException e) {
            throw e;
        } 

	}
    private static DataMap doLocalCall(String className, String methodName, DataMap map, boolean isSingleton) throws Exception {
    	
    	LogManager.debug("local class[" + className +"] " + methodName +" call..");
    	Context ctx = map.getContext();

        if (ctx != null && ctx.getProfiler() != null) {
            ctx.getProfiler().startEvent(methodName+" of "+className);
        }
        
        try{
	    	if(isSingleton){
	        	map = (DataMap)MethodInvokerUtil.invokeSingleton(className, methodName, map);
	        }else{
	        	map = (DataMap)MethodInvokerUtil.invoke(className, methodName, map);
	        }
	    	return map;
	    	
        }finally{
            if (ctx != null && ctx.getProfiler() != null) {
                ctx.getProfiler().stopEvent();
            }
        }
    }
    
	private static DataMap doRemoteCall(String wasId, String className, String methodName, DataMap map, boolean isSingleton) throws Exception {
		
		LogManager.debug("remote class[" + className +"] " + methodName +" call..");
		
		RPCEJB bean = null; 
        Context ctx = map.getContext();

        if (ctx != null && ctx.getProfiler() != null) {
            ctx.getProfiler().startEvent(methodName+" of "+className);
        }
        try {
            bean = getEJB(wasId);
            if(bean ==null) throw new SysException("RPC EJB생성에 실패하였습니다.");
            //싱글톤 패턴 여부에 따라 호출...
            if(isSingleton){
            	map = bean.doSingletonRPC(className, methodName, map);
            }else{
            	map = bean.doRPC(className, methodName, map);
            }
        } catch (CreateException e) {
            throw new SysException("FRS00005", "BIZ 호출에 장애 발생  클래스를 호출할 수 없습니다.(ejbCreateException)");
        } catch (RemoteException e) {
            Throwable th = e.getCause();
            if (th == null) {
                throw new SysException("FRS00004", "RemoteException in Biz-BIZ 클래스를 호출 하는 중 오류 발생");
            } else if (th instanceof RuntimeException) {
            	LogManager.error("RemoteException in RPC-RuntimeException:"+th.toString(),th);
	            throw (RuntimeException) th;
            } else {
            	LogManager.error("RemoteException in RPC-"+th.toString(),th);
            	throw new SysException("FRS00004", th);
            }
        } catch (SysException e) {
            LogManager.error(">>>SysException: "+e.toString(),e);
            throw e;
        } finally{
            ctx = map.getContext();
            if (ctx != null && ctx.getProfiler() != null) {
                ctx.getProfiler().stopEvent();
            }
        }
		return map;
	}
	
	private static Object doRemoteCall(String provicerURL,
			String contextFactory, String ejbJndiName, String className,
			String methodName, Class[] argumentClass, Object[] argumentValue) throws Exception {
		return doRemoteCall(provicerURL, contextFactory, ejbJndiName, className,
						methodName, argumentClass, argumentValue, 0);
	}
	
    private static Object doRemoteCall(String provicerURL,
			String contextFactory, String ejbJndiName, String className,
			String methodName, Class[] argumentClass, Object[] argumentValue, int tryCnt) throws Exception {
		RPCEJB bean = null; 
        try {
            bean = getEJB(provicerURL, contextFactory, ejbJndiName);
            if(bean ==null) throw new SysException("RPC EJB생성에 실패하였습니다.");
            Object obj =  bean.doRPC(className, methodName, argumentClass, argumentValue);
            return obj;
        } catch (CreateException e) {
            throw new SysException("FRS00005", className + "호출에 장애 발생. 클래스를 호출할 수 없습니다.(ejbCreateException)");
       
        }  catch(java.rmi.ConnectException e){
            	
            //ejb home 캐쉬를 제거한다.
            EJBManager.removeCache(ejbJndiName, provicerURL);
            	
            //재시도 (재시도 횟수가 0일 때만)
            if(tryCnt==0){
            	LogManager.error("RPCClient 호출 장애:ejb캐쉬를 제거하고 재시도합니다.", e);
            	return doRemoteCall(provicerURL, ejbJndiName, className, methodName, argumentClass, argumentValue, 1);
            }else{
            	throw new SysException("FRS00004", className + "호출에 장애 발생." + e.toString(), e);
            }
        } catch (RemoteException e) {
            Throwable th = e.getCause();
            if (th == null) {
               //ejb home 캐쉬를 제거한다.
               EJBManager.removeCache(ejbJndiName, provicerURL);
               throw new SysException("FRS00004", className + "호출에 장애 발생." + e.toString(), e);
            } else if (th instanceof RuntimeException) {
               LogManager.error("RemoteException in RPC-RuntimeException:"+th.toString(),th);
    	       throw (RuntimeException) th;
            } else {
               LogManager.error("RemoteException in RPC-"+th.toString(),th);
               throw new SysException("FRS00004", th);
            }
        } catch (SysException e) {
            throw e;
        }
	}
    /**
     * @param wasConfig
     * @return
     * @throws RemoteException
     * @throws CreateException
     */
    public static RPCEJB getEJB(String wasConfig) throws RemoteException, CreateException{
        return getEJB(wasConfig, DEFAULT_EJB_NAME);
    }
   
    
    /**
     * @param wasConfig
     * @param ejbName
     * @return
     * @throws RemoteException
     * @throws CreateException
     */
    public static RPCEJB getEJB(String wasConfig, String ejbName) throws RemoteException, CreateException{
    	if(ejbName==null) ejbName = DEFAULT_EJB_NAME;
        RPCHome home = getEJBHome(wasConfig, ejbName, true);

        RPCEJB bean;
        try {
            bean = home.create();              
        } catch (CreateException e) {
            home=getEJBHome(wasConfig, ejbName, false);
            return home.create();            
        }
        return bean;
    }
    
    private static RPCEJB getEJB(String providerURL, String contextFactory, String ejbName) throws RemoteException, CreateException{
    	if(ejbName==null) ejbName = DEFAULT_EJB_NAME;
        RPCHome home = getEJBHome(providerURL, contextFactory, ejbName, true);

        RPCEJB bean;
        try {
            bean = home.create();              
        } catch (CreateException e) {
            home=getEJBHome(providerURL, contextFactory, ejbName, false);
            return home.create();            
        }
        return bean;
    }
    /**
     * @param wasConfig
     * @param ejbName
     * @param useCache
     * @return
     */
    private static RPCHome getEJBHome(String wasConfig, String ejbName, boolean useCache) {
        if(!useCache){
            EJBManager.removeCache(ejbName,wasConfig);
        }
        RPCHome home =(RPCHome) EJBManager.lookup(ejbName,wasConfig);
        return home;
    }
    
    private static RPCHome getEJBHome(String providerURL, String contextFactory, String ejbName, boolean useCache) {
        if(!useCache){
            EJBManager.removeCache(ejbName,providerURL);
        }
        RPCHome home =(RPCHome) EJBManager.lookup(ejbName, providerURL, contextFactory);
        return home;
    }
}