/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.service.context;

import java.io.FileNotFoundException;
import java.util.Map;

import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;
import nebsoa.common.startup.StartupContext;
import nebsoa.common.util.DataMap;
import nebsoa.common.util.PropertyManager;
import nebsoa.management.ManagementObject;
import nebsoa.service.info.Component;
import nebsoa.service.info.Service;


/*******************************************************************
 * <pre>
 * 1.설명 
 * 서비스 엔진 정보를 담고 있는 context 클래스
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
 * $Log: ServiceEngineContext.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:27  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.2  2009/09/14 09:01:43  isjoo
 * 컴포넌트 정보 리로드시 버그 수정
 *
 * Revision 1.1  2008/11/18 11:27:26  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:29  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.10  2008/06/20 02:13:03  김영석
 * 접근허용자 리로딩 로직 추가
 *
 * Revision 1.9  2008/05/23 09:00:19  김은정
 * getInstance()구현내용 수정
 * if안에 synchronized가 오도록..
 *
 * Revision 1.8  2008/05/21 05:21:34  최수종
 * WAS_TYPE을 각각의 인스턴스별 프로퍼티('was_' + '인스턴스ID' + '.properties.xml'에서 설정값을 읽어오도록 수정함.
 *
 * Revision 1.7  2008/05/21 01:49:47  김은정
 * reload 메소드 추가
 *
 * Revision 1.6  2008/04/23 11:12:43  이종원
 * servicepool체크 로직 및 synchronized 블럭 추가 수정
 *
 * Revision 1.5  2008/04/23 11:10:20  이종원
 * servicepool체크 로직 및 synchronized 블럭 추가 수정
 *
 * Revision 1.4  2008/04/23 11:06:25  이종원
 * synchronized블럭 수정
 *
 * Revision 1.3  2008/04/03 03:30:55  instructor
 * singletone처리 시 if문과 synchronized문의 순서 번경(널포인터 에러 때문)
 *
 * Revision 1.2  2008/02/14 09:26:48  김성균
 * 서비스 정보 로딩 관련 버그 수정
 *
 * Revision 1.1  2008/01/22 05:58:35  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.4  2008/01/07 03:15:40  김성균
 * WAS_TYPE 디폴트값 변경
 *
 * Revision 1.3  2008/01/07 01:47:08  김성균
 * 웹일 경우 로딩 분리
 *
 * Revision 1.2  2008/01/02 09:46:01  김성균
 * 특정 서비스 리로딩 기능 추가
 *
 * Revision 1.6  2008/01/02 09:30:28  김성균
 * 특정 서비스 리로딩 기능 추가
 *
 * Revision 1.5  2007/12/31 02:55:55  김성균
 * 주석 추가
 *
 * Revision 1.4  2007/12/28 07:10:49  김성균
 * nebsoa.service.info 패키지 변경에 의한 수정
 *
 * Revision 1.3  2007/12/26 04:33:08  김성균
 * ServiceComponentList 관련 변경
 *
 * Revision 1.2  2007/12/24 05:23:35  김성균
 * createServiceContext() 삭제
 *
 * Revision 1.1  2007/12/24 02:10:00  김성균
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class ServiceEngineContext extends ManagementObject {

	private static Object dummy = new Object();

	private static ServiceEngineContext instance;

	/**
	 * 서비스 정보를 가지고 있는 객체들의 풀 
	 */
	private Map servicePool;

	/**
	 * 컴포넌트 정보를 가지고 있는 객체들의 풀 
	 */
	private Map componentPool;
	
	/**
	 * 접근허용자 정보를 가지고 있는 객체들의 풀 
	 */
	private Map accessUserPool;
	

	public static final String SERVICE_ID = "_$SERVICE_ID";

	/**
	 * 싱글톤 처리 
	 */
	private ServiceEngineContext(){
	}

	/**
	 * 싱글톤 객체 얻어오기
	 * @return
	 */
	public static ServiceEngineContext getInstance(){

		if (instance != null) return instance;

		synchronized (dummy) {
			instance = new ServiceEngineContext();
			instance.loadServiceEngineContext();
			instance.toXml();
		}
		return instance;
	}


	/**
	 * ServiceEngineContext 정보를 로딩한다.
	 */
	private  void loadServiceEngineContext() {
		synchronized (dummy) {
			if (isXmlMode()) {
				try {
					fromXml();
				} catch (FileNotFoundException e) {
					throw new SysException("XML 파일을 찾을 수 없습니다.");
				}
			} else {
				// WAS 유형에 따라서 로딩정보를 분리한다.
				//String wasType = PropertyManager.getProperty("was_config", StartupContext.getInstanceId() + ".WAS_TYPE", "ALL");

				/*
				 * 각 was별 properties파일에 .WAS_TYPE 설정을 추가/로딩하도록 수정
				 * modify - 2008.05.20
				 */
				String wasType = PropertyManager.getProperty("was_"+StartupContext.getInstanceId(), "WAS_TYPE", "ALL");

				LogManager.debug("########## 서비스 WAS_TYPE ====> "+wasType);

				setServicePool(ServiceEngineContextLoader.loadServicePool());
				if ("WEB".equals(wasType)) {
					//접근허용자는 웹에서 체크되기 때문에 추가
					ServiceEngineContextLoader.loadAccessUser(instance);
				} else {
					setComponentPool(ServiceEngineContextLoader.loadComponentPool());
					ServiceEngineContextLoader.loadServiceComponent(instance);
					ServiceEngineContextLoader.loadParam(instance);
					ServiceEngineContextLoader.loadAccessUser(instance);
				}
			}
		}
	}

	/**
	 * 특정 서비스 로딩
	 */
	public synchronized void loadService(String serviceId) {
		ServiceEngineContextLoader.loadService(serviceId, instance);
	}

	/**
	 * @return Returns the servicePool.
	 */
	public Map getServicePool() {
		return servicePool;
	}

	/**
	 * @param servicePool The servicePool to set.
	 */
	public void setServicePool(Map servicePool) {
		this.servicePool = servicePool;
	}

	/**
	 * @return Returns the componentPool.
	 */
	public Map getComponentPool() {
		return componentPool;
	}

	/**
	 * @param componentPool The componentPool to set.
	 */
	public void setComponentPool(Map componentPool) {
		LogManager.info("############### set service pool ##############");
		this.componentPool = componentPool;
	}

	/**
	 * @return Returns the accessUserPool.
	 */
	public Map getAccessUserPool() {
		return accessUserPool;
	}

	/**
	 * @param componentId
	 * @return
	 */
	public Component getComponent(String componentId) {
		return (Component) componentPool.get(componentId);
	}

	/**
	 * @param serviceId
	 * @return
	 */
	public Service getService(String serviceId) {
		if(servicePool!=null){
			return (Service) servicePool.get(serviceId);
		}

		synchronized (dummy) {
			if(servicePool==null){
				LogManager.info("########## service pool is null... now loading #######");
				setServicePool(ServiceEngineContextLoader.loadServicePool());
			}else{
				LogManager.info("########## lock을 얻는 동안 로딩 되어 skip합니다. ###########");
			}
		}

		return (Service) servicePool.get(serviceId);
	}

	/**
	 * @param service
	 */
	public void setService(Service service) {
		servicePool.put(service.getServiceId(), service);
	}

	/**
	 * 전체 리로딩
	 */
	public static void reloadAll(DataMap map) {
		getInstance().loadServiceEngineContext();
		getInstance().toXml();
	}

	/* (non-Javadoc)
	 * @see nebsoa.management.ManagementObject#getInstance()
	 */
	public Object getManagementObject() {
		return instance;
	}

	/* (non-Javadoc)
	 * @see nebsoa.management.ManagementObject#setInstance(java.lang.Object)
	 */
	public void setManagementObject(Object obj) {
		instance = (ServiceEngineContext) obj;
	}

	/**
	 * 서비스별  리로딩
	 */
	public static void reload(DataMap map) {
		String serviceId = map.getString(SERVICE_ID);
		if(instance==null) getInstance();
		ServiceEngineContextLoader.loadService(serviceId, instance);
		instance.toXml();
	}
	
	public static void reloadAccessUser(DataMap map) {
		if(instance == null) getInstance();
		ServiceEngineContextLoader.reloadAccessUser(instance);
		instance.toXml();
	}
}
