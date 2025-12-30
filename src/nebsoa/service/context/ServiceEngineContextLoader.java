/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.service.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nebsoa.common.Constants;
import nebsoa.common.collection.DataSet;
import nebsoa.common.jdbc.DBManager;
import nebsoa.common.jdbc.DBResultSet;
import nebsoa.common.log.LogManager;
import nebsoa.common.startup.StartupContext;
import nebsoa.common.util.PropertyManager;
import nebsoa.service.info.Component;
import nebsoa.service.info.Service;
import nebsoa.service.info.ServiceComponent;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 서비스 엔진 정보를 로딩하는 클래스
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
 * $Log: ServiceEngineContextLoader.java,v $
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
 * Revision 1.2  2008/10/08 13:19:37  ejkim
 * loadParam부분 수정.
 *
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.9  2008/06/23 05:27:48  김영석
 * 서비스 접근허용자 로직 추가
 *
 * Revision 1.8  2008/06/20 02:13:03  김영석
 * 접근허용자 리로딩 로직 추가
 *
 * Revision 1.7  2008/05/23 08:33:08  김은정
 * wasType에 따라 component의 로딩이 수행되도록 수정
 *
 * Revision 1.6  2008/05/21 01:51:18  김은정
 * 특정서비스 로드시 service는 WEB, AP에 반영, compoment는 AP에만 반영되도록 수정
 *
 * Revision 1.5  2008/05/19 04:45:22  김영석
 * 서비스 옵션 체크 로직 변경
 *
 * Revision 1.4  2008/03/10 09:14:20  김영석
 * ALL_SERVICE_LOAD_SQL, SERVICE_LOAD_SQL 수정
 *
 * Revision 1.3  2008/03/10 09:12:11  김영석
 * ALL_SERVICE_LOAD_SQL, SERVICE_LOAD_SQL에
 * 빠진 컬럼 추가
 *
 * Revision 1.2  2008/02/14 09:26:48  김성균
 * 서비스 정보 로딩 관련 버그 수정
 *
 * Revision 1.1  2008/01/22 05:58:35  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.3  2008/01/04 02:43:24  김성균
 * 컬럼추가 반영 및 서비스제공시간관련 기능 추가
 *
 * Revision 1.2  2008/01/02 09:46:01  김성균
 * 특정 서비스 리로딩 기능 추가
 *
 * Revision 1.1  2007/12/24 02:10:00  김성균
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class ServiceEngineContextLoader {

	/**
	 * 서비스 로딩 쿼리
	 */
	private static String ALL_SERVICE_LOAD_SQL =
		"SELECT SERVICE_ID "
		+"\n, SERVICE_NAME "
		+"\n, CLASS_NAME "
		+"\n, METHOD_NAME "
		+"\n, SERVICE_TYPE "
		+"\n, PRE_PROCESS_APP_ID "
		+"\n, POST_PROCESS_APP_ID "
		+"\n, TIME_CHECK_YN "
		+"\n, START_TIME "
		+"\n, END_TIME "
		+"\n, BIZ_DAY_CHECK_YN "
		+"\n, USE_YN "
		+"\n, TRX_ID "
		+"\n, ORG_ID "
		+"\n, IO_TYPE "
		+"\n, BIZ_GROUP_ID "
		+"\n, WORK_SPACE_ID "
		+"\n, LOGIN_ONLY_YN "
		+"\n, SECURE_SIGN_YN "
		+"\n, REQ_CHANNEL_CODE "
		+"\n, SVC_CONF_1 "
		+"\n, SVC_CONF_2 "
		+"\n, BANK_STATUS_CHECK_YN "
		+"\n, BANK_CODE_FIELD "
		+"\n, BIZDAY_SERVICE_YN "
		+"\n, BIZDAY_SERVICE_START_TIME "
		+"\n, BIZDAY_SERVICE_END_TIME "
		+"\n, SATURDAY_SERVICE_YN "
		+"\n, SATURDAY_SERVICE_START_TIME "
		+"\n, SATURDAY_SERVICE_END_TIME "
		+"\n, HOLIDAY_SERVICE_YN "
		+"\n, HOLIDAY_SERVICE_START_TIME "
		+"\n, HOLIDAY_SERVICE_END_TIME "
		+"\n FROM FWK_SERVICE ";

	// 서비스 접근 허용자 기능 추가로 인한 where 조건 삭제
	//+"\n WHERE USE_YN = 'Y' ";

	/**
	 * 서비스 정보를 로딩한다.
	 */
	public static Map loadServicePool() {

		ArrayList list = DBManager.executePreparedQueryToBeanList(
				ALL_SERVICE_LOAD_SQL, new Object[]{}, nebsoa.service.info.Service.class);

		Map map = new HashMap();
		Service service = null;

		for (int index = 0, size = list.size(); index < size; index++) {
			service = (Service) list.get(index);
			map.put(service.getServiceId(), service);
		}
		return map;
	}

	/**
	 * 컴포넌트 로딩 쿼리
	 */
	private static String ALL_COMPONENT_LOAD_SQL =
		"SELECT COMPONENT_ID "
		+"\n, COMPONENT_NAME "
		+"\n, COMPONENT_DESC "
		+"\n, COMPONENT_TYPE "
		+"\n, COMPONENT_CLASS_NAME "
		+"\n, COMPONENT_METHOD_NAME "
		+"\n, COMPONENT_CREATE_TYPE "
		+"\n FROM FWK_COMPONENT ";

	/**
	 * 컴포넌트 정보를 로딩한다.
	 */
	public static Map loadComponentPool() {

		ArrayList list = DBManager.executePreparedQueryToBeanList(
				ALL_COMPONENT_LOAD_SQL, new Object[]{}, nebsoa.service.info.Component.class);

		Map map = new HashMap();
		Component component = null;

		for (int index = 0, size = list.size(); index < size; index++) {
			component = (Component) list.get(index);
			map.put(component.getComponentId(), component);
		}
		return map;
	}

	/**
	 * 서비스별 컴포넌트 로딩 쿼리
	 */
	private static String ALL_SERVICE_COMPONENT_LOAD_SQL =
		"SELECT A.SERVICE_ID "
		+"\n, A.SERVICE_SEQ_NO "
		+"\n, A.COMPONENT_ID "
		+"\n, A.POST_CONDITION "
		+"\n FROM FWK_SERVICE_RELATION A "
/**
		+"\n FROM FWK_SERVICE_RELATION A, FWK_SERVICE B "
		+"\n WHERE A.SERVICE_ID = B.SERVICE_ID "
		//서비스 접근 허용자 기능 추가로 인한 where 조건 삭제
		+"\n AND B.USE_YN = 'Y' "
**/
		+"\n ORDER BY A.SERVICE_ID, A.SERVICE_SEQ_NO ";

	/**
	 * 서비스별 컴포넌트 정보를 로딩한다. 
	 */
	public static void loadServiceComponent(ServiceEngineContext sec) {

		DataSet ds = DBManager.executePreparedQuery(ALL_SERVICE_COMPONENT_LOAD_SQL, new Object[]{});

		ServiceComponent serviceComponent = null;

		String serviceId = null;
		int serviceSeqNo = 0;
		String componentId = null;
		String postCondition = null;
		Service service = null;

		while (ds.next()) {
			serviceId = ds.getString("SERVICE_ID");
			serviceSeqNo = ds.getInt("SERVICE_SEQ_NO");
			componentId = ds.getString("COMPONENT_ID");
			postCondition = ds.getString("POST_CONDITION");

			serviceComponent = new ServiceComponent();
			serviceComponent.setServiceSeqNo(serviceSeqNo);
			serviceComponent.setComponent(sec.getComponent(componentId));
			serviceComponent.setPostCondition(postCondition);

			service = sec.getService(serviceId);
			if(service != null) {
				service.addServiceComponent(serviceComponent);
			}
		}
	}

	/**
	 * 서비스별 파라미터 정보 로딩 쿼리
	 */
	private static String ALL_PARAM_LOAD_SQL =
		"SELECT A.SERVICE_ID "
		+"\n, A.SERVICE_SEQ_NO "
		+"\n, B.COMPONENT_ID "
		+"\n, B.PARAM_SEQ_NO "
		+"\n, C.PARAM_KEY "
		+"\n, B.PARAM_VALUE "
		+"\n, C.DEFAULT_PARAM_VALUE "
		+"\n FROM FWK_SERVICE_RELATION A, FWK_RELATION_PARAM B, FWK_COMPONENT_PARAM C" //, FWK_SERVICE D "
		+"\n WHERE A.SERVICE_ID = B.SERVICE_ID "
		+"\n AND   A.SERVICE_SEQ_NO = B.SERVICE_SEQ_NO "
		+"\n AND   B.COMPONENT_ID = C.COMPONENT_ID "
		+"\n AND   B.PARAM_SEQ_NO = C.PARAM_SEQ_NO "
		//+"\n AND   A.SERVICE_ID = D.SERVICE_ID "
		// 서비스 접근 허용자 기능 추가로 인한 where 조건 삭제
		//+"\n AND   D.USE_YN = 'Y' "
		+"\n ORDER BY A.SERVICE_ID, A.SERVICE_SEQ_NO ";


	/**
	 * 서비스별 컴포넌트의 파라미터 정보를 로딩한다. 
	 */
	public static void loadParam(ServiceEngineContext sec) {
		DataSet ds = DBManager.executePreparedQuery(ALL_PARAM_LOAD_SQL, new String[]{});

		String serviceId = null;
//		String beforeServiceId = null;
		int serviceSeqNo = 0;

		String paramKey = null;
		String paramValue = null;
//		int beforeServiceSeqNo = -1;
//		int serviceIndex = -1;

		while (ds.next()) {
			serviceId = ds.getString("SERVICE_ID");
			serviceSeqNo = ds.getInt("SERVICE_SEQ_NO");
//			if (!serviceId.equals(beforeServiceId)) {
//			beforeServiceSeqNo = -1; 
//			serviceIndex = -1; 
//			}
//			if (serviceSeqNo != beforeServiceSeqNo) serviceIndex++;
			paramKey = ds.getString("PARAM_KEY");
			paramValue = ds.getString("PARAM_VALUE");

			for(int serviceIndex=0; serviceIndex< sec.getService(serviceId).getServiceComponentListSize(); serviceIndex++){
				if(sec.getService(serviceId).getServiceComponent(serviceIndex).getServiceSeqNo() == serviceSeqNo){
					sec.getService(serviceId).getServiceComponent(serviceIndex).putParameter(paramKey, paramValue);
				}
			}

//			sec.getService(serviceId).getServiceComponent(serviceIndex).putParameter(paramKey, paramValue);
//			beforeServiceSeqNo = serviceSeqNo;
//			beforeServiceId = serviceId;
		}
	}

	/**
	 * 특정 서비스 로딩 쿼리
	 */
	private static String SERVICE_LOAD_SQL =
		ALL_SERVICE_LOAD_SQL +"\n WHERE SERVICE_ID = ? ";

	/**
	 * 특정 서비스별 컴포넌트 로딩 쿼리
	 */
	private static String SERVICE_COMPONENT_LOAD_SQL =
		"SELECT SERVICE_ID "
		+"\n, SERVICE_SEQ_NO "
		+"\n, COMPONENT_ID "
		+"\n, POST_CONDITION "
		+"\n FROM FWK_SERVICE_RELATION "
		+"\n WHERE SERVICE_ID = ? "
		+"\n ORDER BY SERVICE_SEQ_NO ";

	/**
	 * 특정 서비스별 컴포넌트 파라미터 로딩 쿼리
	 */
	private static String PARAM_LOAD_SQL =
		"SELECT B.SERVICE_ID "
		+"\n, B.SERVICE_SEQ_NO "
		+"\n, B.COMPONENT_ID "
		+"\n, B.PARAM_SEQ_NO "
		+"\n, C.PARAM_KEY "
		+"\n, B.PARAM_VALUE "
		+"\n, C.DEFAULT_PARAM_VALUE "
		+"\n FROM FWK_RELATION_PARAM B, FWK_COMPONENT_PARAM C "
		+"\n WHERE B.SERVICE_ID = ? "
		+"\n AND   B.COMPONENT_ID = C.COMPONENT_ID "
		+"\n AND   B.PARAM_SEQ_NO = C.PARAM_SEQ_NO "
		+"\n ORDER BY B.SERVICE_SEQ_NO ";

	/**
	 * 특정 서비스 및 관련 정보를 로딩한다.
	 */
	public static void loadService(String serviceId, ServiceEngineContext sec) {


		LogManager.info("###### 특정 서비스를 리로드 :: Service :"+serviceId);
		Object[] params = {
				serviceId
		};

		// 1. 서비스 로딩
		Service service = (Service) DBManager.executePreparedQueryToBean(
				SERVICE_LOAD_SQL, params, nebsoa.service.info.Service.class);

		//String wasType = PropertyManager.getProperty("was_config", StartupContext.getInstanceId() + ".WAS_TYPE", "ALL");
		/*
		 * 각 was별 properties파일에 .WAS_TYPE 설정을 추가/로딩하도록 수정
		 * modify - 2008.05.22
		 */
		String wasType = PropertyManager.getProperty("was_"+StartupContext.getInstanceId(), "WAS_TYPE", "ALL");

		LogManager.info("###### 특정 서비스를 리로드 :: WAS_TYPE ====> "+wasType);



		if (!"WEB".equals(wasType)) {

			//2.1 먼제 전체 컴포넌트를 로딩 해야 겠다... 2009.03.16 이종원
			LogManager.info("###### 특정 서비스를 리로드 :: 전체 컴포넌트를 reload 합니다. ");
			sec.setComponentPool(ServiceEngineContextLoader.loadComponentPool());

			LogManager.info("###### 특정 서비스를 리로드 :: 관련 컴포넌트 및 실행순서를 reload 합니다. ");
			// 2.2 서비스에  매핑된 컴포넌트 로딩
			DataSet ds = DBManager.executePreparedQuery(Constants.SPIDER_DB, SERVICE_COMPONENT_LOAD_SQL, params);
			ServiceComponent serviceComponent = null;
			int serviceSeqNo = 0;
			String componentId = null;
			String postCondition = null;

			while (ds.next()) {
				serviceSeqNo = ds.getInt("SERVICE_SEQ_NO");
				componentId = ds.getString("COMPONENT_ID");
				postCondition = ds.getString("POST_CONDITION");

				serviceComponent = new ServiceComponent();
				serviceComponent.setServiceSeqNo(serviceSeqNo);
				serviceComponent.setComponent(sec.getComponent(componentId));
				serviceComponent.setPostCondition(postCondition);

				service.addServiceComponent(serviceComponent);
			}
			ds=null;

			LogManager.info("###### 특정 서비스를 리로드 :: 관련 컴포넌트의 파라미터 reload 합니다. ");
			// 3. 서비스별 컴포넌트 파라미터 로딩
			ds = DBManager.executePreparedQuery(Constants.SPIDER_DB, PARAM_LOAD_SQL, params);

			String paramKey = null;
			String paramValue = null;
			while (ds.next()) {
				serviceSeqNo = ds.getInt("SERVICE_SEQ_NO");
				//if (serviceSeqNo != beforeServiceSeqNo) serviceIndex++;
				paramKey = ds.getString("PARAM_KEY");
				paramValue = ds.getString("PARAM_VALUE");
				for(int serviceIndex=0; serviceIndex< service.getServiceComponentListSize(); serviceIndex++){
					if(service.getServiceComponent(serviceIndex).getServiceSeqNo() == serviceSeqNo){
						service.getServiceComponent(serviceIndex).putParameter(paramKey, paramValue);
					}
				}
			}
		}

		//4. 경우 접근 허용된 사용자를 리로딩하는 로직.
		LogManager.info("###### 특정 서비스를 리로드 :: 접근 허용자 정보를  reload 합니다. ");
		reloadAccessUser(sec);


		// 5. 서비스엔진에 설정
		LogManager.info("###### 특정 서비스를 리로드 :: 서비스 엔진에  새로 로딩된 서비스로 바꿔넣기 합니다.\n"
				+service);
		LogManager.info("MONITOR","###### 특정 서비스를 리로드 :: 서비스 엔진에  새로 로딩된 서비스로 바꿔넣기 합니다.\n"
				+service);
		sec.setService(service);
	}

	/**
	 * DB로부터 서비스 접근허용사용자ID 정보를 로드하기 위한 SQL
	 */
	private static final String LOAD_ALL_ACCESS_USER = 
		"\r\n SELECT TRX_ID "
		+ "\r\n   ,CUST_USER_ID  "
		+ "\r\n FROM FWK_ACCESS_USER "
		+ "\r\n WHERE USE_YN = 'Y' AND GUBUN_TYPE = 'S' "
		;
	/**
	 * 서비스 일시 중지 일 경우 접근 허용된 사용자를 로딩하는 로직. 
	 * 2008. 6. 17.  김영석 작성
	 * @param context
	 */
	public static void loadAccessUser(ServiceEngineContext sec) {

		Map servicePool = sec.getServicePool();
		DBResultSet rs = DBManager.executeQuery(Constants.SPIDER_DB, LOAD_ALL_ACCESS_USER);

		Service service = null;
		String serviceId = null;        

		while (rs.next()) {
			//data가 잘못 등록된 경우 오류 발생하는 로직 때문에 try~catch로 처리
			try{
				serviceId = rs.getString("TRX_ID");
				if(serviceId != null){
					service = (Service) servicePool.get(serviceId);
					if(service != null){
						service.addCustUserId(rs.getString("CUST_USER_ID"));
					}
				}
			}catch(Exception e){
				LogManager.error(e.toString(),e);
			}
		}
	}
	/**
	 * 서비스 일시 중지 일 경우 접근 허용된 사용자를 리로딩하는 로직. 
	 * 2008. 6. 20.  김영석 작성
	 * @param context
	 */
	public static void reloadAccessUser(ServiceEngineContext sec) {

		Map servicePool = sec.getServicePool();
		DBResultSet rs = DBManager.executeQuery(Constants.SPIDER_DB, LOAD_ALL_ACCESS_USER);

		Service service = null;
		String serviceId = null;

		Set keySet = servicePool.keySet();
		Iterator itr = keySet.iterator();

		// 리로드 하기 위해 현재 서비스 접근허용자 정보 clear
		while (itr.hasNext()) {
			String childKey = (String) itr.next();
			service = (Service) servicePool.get(childKey);
			List custUserIds = service.getCustUserIds();
			custUserIds.clear();
		}

		while (rs.next()) {
			//data가 잘못 등록된 경우 오류 발생하는 로직 때문에 try~catch로 처리
			try{
				serviceId = rs.getString("TRX_ID");
				if(serviceId != null){
					service = (Service) servicePool.get(serviceId);
					if(service != null){
						List custUserIds = service.getCustUserIds();

						// 중복일 경우 skip
						if(!custUserIds.contains(rs.getString("CUST_USER_ID"))) {                    	
							service.addCustUserId(rs.getString("CUST_USER_ID"));
						}
					}
				}
			}catch(Exception e){
				LogManager.error(e.toString(),e);
			}
		}
	}

}
