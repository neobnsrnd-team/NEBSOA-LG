/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.service.engine;


import java.util.Map;

import nebsoa.biz.base.Biz;
import nebsoa.common.Constants;
import nebsoa.common.exception.DBException;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.DataMap;
import nebsoa.common.util.DbTxUtil;
import nebsoa.common.util.MethodInvokerUtil;
import nebsoa.common.util.PropertyManager;
import nebsoa.common.util.StringUtil;
import nebsoa.service.context.ServiceContext;
import nebsoa.service.exception.ServiceException;
import nebsoa.service.exception.ServiceSysException;
import nebsoa.service.info.Component;
import nebsoa.service.info.ServiceComponent;
import nebsoa.spiderlink.context.TrxMessage;
import nebsoa.spiderlink.engine.message.MessageValueSetter;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 서비스 요청에 대한 처리를 수행하는 클래스입니다.
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
 * $Log: DefaultServiceHandler.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:23  cvs
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
 * Revision 1.6  2008/09/10 07:48:36  jglee
 * 트랜잭션 file로그처리에서 db로그처리로 수정
 *
 * Revision 1.5  2008/09/08 13:19:07  jglee
 * rollbackTransaction시 에러 처리 수정
 *
 * Revision 1.4  2008/09/08 12:24:08  jglee
 * service exception시 Transaction 로그 처리
 *
 * Revision 1.3  2008/09/01 13:02:26  jglee
 * 정상리턴 및 서비스 종료시 transaction 체크 로직 수정(commit 처리)
 *
 * Revision 1.2  2008/08/25 13:42:15  jglee
 * Component 수행후 return 처리 추가
 *
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.6  2008/03/03 04:24:31  김성균
 * SysException -> ServiceSysException으로 변경
 *
 * Revision 1.5  2008/02/15 04:57:04  김성균
 * 로그 수정
 *
 * Revision 1.4  2008/02/15 04:45:15  김성균
 * 서비스 수행 로그 메시지 정리
 *
 * Revision 1.3  2008/02/14 09:27:30  김성균
 * 예외처리 및 프로파일관련 수정
 *
 * Revision 1.2  2008/01/24 05:35:04  최수종
 * Exception 처리 수정
 *
 * Revision 1.1  2008/01/22 05:58:33  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.4  2008/01/09 07:22:08  안경아
 * MessageValueSetter가 상위전문도 체크 하므로 수정됨
 *
 * Revision 1.3  2008/01/08 02:27:05  최수종
 * *** empty log message ***
 *
 * Revision 1.14  2008/01/04 02:49:57  김성균
 * 입력값 검증 및 디폴트값 셋팅 옵션널하게 변경
 *
 * Revision 1.13  2007/12/28 08:46:18  최수종
 * 버그 수정
 *
 * Revision 1.12  2007/12/28 08:20:08  최수종
 * Service정보를 load하는 로직 변경에 따른 수정
 *
 * Revision 1.11  2007/12/28 05:45:09  최수종
 * *** empty log message ***
 *
 * Revision 1.10  2007/12/27 08:36:29  최수종
 * *** empty log message ***
 *
 * Revision 1.9  2007/12/27 07:57:02  최수종
 * *** empty log message ***
 *
 * Revision 1.8  2007/12/27 05:50:25  최수종
 * *** empty log message ***
 *
 * Revision 1.7  2007/12/27 05:20:25  김성균
 * 버그 수정
 *
 * Revision 1.6  2007/12/26 09:21:52  최수종
 * *** empty log message ***
 *
 * Revision 1.5  2007/12/26 07:35:07  최수종
 * *** empty log message ***
 *
 * Revision 1.4  2007/12/26 07:31:55  최수종
 * *** empty log message ***
 *
 * Revision 1.3  2007/12/24 08:50:18  김성균
 * marshall, unmarshall 부분 추가
 *
 * Revision 1.2  2007/12/24 08:22:37  최수종
 * *** empty log message ***
 *
 * Revision 1.1  2007/12/24 08:02:22  최수종
 * *** empty log message ***
 *
 * 
 *
 * </pre>
 ******************************************************************/
public class DefaultServiceHandler extends BaseServiceHandler {
    
	private static final String TRAN_PREFIX = "$TRAN_";
	/**
     * 각 핸들러별로 구현해야할 메소드로써, 데이타를 marshall처리시에 사용한다.
     * 
     * @param serviceContext
     * @param dataMap
     * @return dataMap
     */
    protected DataMap marshall(ServiceContext serviceContext, DataMap dataMap) {
    	
    	TrxMessage trxMessage = serviceContext.getTrxMessage();
        if (trxMessage != null) {
        	String orgId = trxMessage.getOrg().getOrgId();
        	String messageId = trxMessage.getMessageId();
        	String resMessageId = trxMessage.getResMessageId();
        	
            LogManager.info(" ****************** 입력필드_체크 ******************** ");
            LogManager.info(" *** 기관 ID : " + orgId);
            LogManager.info(" *** 요청 메시지 ID : " + messageId);
    		LogManager.info(" *** 응답 메시지 ID : " + resMessageId);
            LogManager.info(" ************************************************ ");
    
            // 입력값 검사 및 디폴트 값 셋팅
            if (messageId != null && orgId != null) {
                LogManager.debug(" ====== 입력값 검사 및 디폴트 값 셋팅 [헤더 포함]  ======");
                dataMap = MessageValueSetter.checkMessageFieldValue(messageId, orgId, dataMap, true);
            }
        }
        
        return dataMap;
    } 
	
	/**
	 * 각 핸들러별로 구현해야할 메소드로써, 서비스 처리시에 사용한다.
	 * @param serviceContext
	 * @param dataMap
	 * @return dataMap
	 * @throws Throwable
	 */
	protected DataMap invokeServiceComponentList(ServiceContext serviceContext, DataMap dataMap) 
		throws ServiceException 	
	{
        LogManager.debug(" ====== 서비스_컴포넌트 수행 시작 ======");
        
        Component component = null;
        Object clazz = null;
        String methodName = null;
        String postCondition = null;

        // 순차적으로 서비스별 컴포넌트 처리 수행
        int index = 1;
		while (serviceContext.hasNextComponent()) {
			LogManager.debug(" ====== " + index + "번째 서비스_컴포넌트 수행 시작 ======");
			
        	// 컴포넌트
        	component = serviceContext.nextComponent();
        	if (component == null) {
				throw new ServiceSysException("FRS50001", "시스템 오류:[" + serviceContext.getServiceId() + "]서비스의 컴포넌트 설정 오류");
        	}

        	// 메소드명
        	methodName = component.getComponentMethodName(); 
        	if (StringUtil.isNull(methodName)) {
				throw new ServiceSysException("FRS50002", "시스템 오류:[" + serviceContext.getServiceId() + "]서비스의 컴포넌트 설정 오류");
        	}

        	// 컴포넌트 파라미터 설정
        	Map paramMap = serviceContext.getCurrentParameterMap();
        	dataMap.putAll(paramMap, false);

        	// 컴포넌트 후행 조건식
        	postCondition = serviceContext.getCurrentPostCondition();
        	
            LogManager.info(" *** 컴포넌트 ID : " + component.getComponentId());
            LogManager.info(" *** 컴포넌트 유형 : " + component.getComponentType());
            LogManager.info(" *** 컴포넌트 생성유형 : " + component.getComponentCreateType());
            LogManager.info(" *** 컴포넌트 클래스명 : " + component.getComponentClassName());
            LogManager.info(" *** 컴포넌트 메소드명 : " + methodName);
            LogManager.info(" *** 서비스별 컴포넌트 후행조건식 : " + postCondition);
            LogManager.info(" *** 컴포넌트 파라미터 : " + paramMap);
        	
            //컴포넌트 정보 dataMap에 추가 (logging용)
            setTransactionLog(dataMap, serviceContext, component);
        	// 현재 컴포넌트 Class의 인스턴스 얻기
        	clazz = component.getComponentClass();
        	
        	try {
				dataMap = doMethodInvoke(clazz, methodName, dataMap);
			} catch (Throwable e) {
				LogManager.error("서비스 컴포넌트 호출 중 오류 발생:" + e.toString(), e);
				LogManager.debug("트랜잭션 모드를 체크 [" + dataMap.isTransactionMode() +"]");
				if(dataMap.isTransactionMode()){
					if(dataMap.getTransactionStatus().equals("BEGIN_TRAN")){
						//getTransactionLog(dataMap);	//rollbackTran에서 logging 처리
						rollbackTran(dataMap, e);
					}
				}
				exceptionHandling(e, postCondition);
			}
			LogManager.debug(" ====== " + index + "번째 서비스_컴포넌트 수행 종료 ======");
			
			// 현재 컴포넌트의 return 처리 체크
			LogManager.debug(" ====== " + index + "번째 서비스_컴포넌트 RETURN 값 체크 시작 ======");
			LogManager.debug(" ====== " + index + "번째 서비스_컴포넌트 RETURN 처리값 : [" + dataMap.getBoolean(Constants.SERVICE_RETURN) + "] ======");
			if(dataMap.getBoolean(Constants.SERVICE_RETURN)){
				LogManager.debug(" ====== " + index + "번째 서비스_컴포넌트에서 RETURN 처리 ======");
				if(dataMap.isTransactionMode()){
					LogManager.debug(" ====== " + index + "번째 서비스_컴포넌트에서 commitTransaction 처리 ======");
					DbTxUtil.commitTransaction(dataMap);
				}
				break;
			}
			index++;
		}
        LogManager.debug(" ====== 서비스_컴포넌트 수행 종료 ======");

        // service 처리후 transaction이 남아 있으면 commit 처리
        LogManager.debug(" ====== 서비스_컴포넌트 수행 종료 후 남아있는 트랜잭션을 체크 시작 ======");
    	if(dataMap.isTransactionMode()){
    		if(dataMap.getTransactionStatus().equals("BEGIN_TRAN")){
    	        LogManager.debug(" ====== 서비스_컴포넌트 수행 종료 후 남아있는 트랜잭션을 commit 합니다. ======");
    			DbTxUtil.commitTransaction(dataMap);
    		}
    	}
        LogManager.debug(" ====== 서비스_컴포넌트 수행 종료 후 남아있는 트랜잭션을 체크 종료 ======");
      
        return dataMap;
	}

    /**
     * 컴포넌트정보 dataMap에 저장(Transaction처리용)
     * @param dataMap
     * @param serviceContext
     * @param component
     * @param idx
     * @return
     * @throws DBException
     */
    protected DataMap setTransactionLog(DataMap dataMap, ServiceContext serviceContext, Component component) throws DBException {
    	return DbTxUtil.setTransactionLog(dataMap, serviceContext, component);
    }
    
	/**
	 * Transaction Log 파일저장처리.
     * @param dataMap
     * @throws DBException
     */
    protected void getTransactionLog(DataMap dataMap) throws DBException {
		DbTxUtil.getTransactionLog(dataMap);
    }
    
	/**
	 * DataMap의 IbatisTxManager를 rollback 처리한다.
	 * 
	 * @param serviceContext
	 * @param dataMap
	 * @return dataMap
	 */
    protected DataMap rollbackTran(DataMap dataMap) throws DBException {
		DbTxUtil.rollbackTransaction(dataMap);
        return dataMap;
    }
    
    /**
	 * DataMap의 IbatisTxManager를 rollback 처리한다.
	 * 
	 * @param serviceContext
	 * @param dataMap
	 * @return dataMap
	 */
    protected DataMap rollbackTran(DataMap dataMap, Throwable e) throws DBException {
		DbTxUtil.rollbackTransaction(dataMap, e);
        return dataMap;
    }
    
	/**
	 * 각 핸들러별로 구현해야할 메소드로써, 데이타를 unmarshall처리시에 사용한다.
	 * 
	 * @param serviceContext
	 * @param dataMap
	 * @return dataMap
	 */
    protected DataMap unmarshall(ServiceContext serviceContext, DataMap dataMap) {

        return dataMap;
    }
    
    /**
     * 컴포넌트 클래스에 정의된 해당 Method 호출
     * 
     * @param clazz
     * @param methodName
     * @param dataMap
     * @param componentType
     * @return
     */
    protected DataMap doMethodInvoke(Object clazz, String methodName, DataMap dataMap) throws Exception {
    	if (clazz instanceof Biz) {
			dataMap.put(Constants.TARGET_METHOD, methodName);
			dataMap = ((Biz) clazz).doProcess(dataMap);
		} else {
			dataMap = (DataMap) MethodInvokerUtil.invoke(clazz, methodName, dataMap);
		}
    	return dataMap;
    }
    
    /**
     * 컴포넌트의 후행조건식에 따라서 예외처리를 한다.
     * @param exception
     * @param postCondition
     * @throws ServiceException
     */
    protected void exceptionHandling(Throwable exception, String postCondition) throws ServiceException {
		if (ServiceComponent.EXCEPTION_SKIP.equals(postCondition)) {
			LogManager.info("오류 무시하고 진행합니다.[" + postCondition + "]");
		} else {
			if (exception instanceof RuntimeException) {
				throw (RuntimeException) exception;
			} else if (exception instanceof Exception) {
				throw new ServiceException(exception);
			} else if (exception instanceof Throwable) {
				throw new ServiceSysException(exception);
			}
		}
    }
 
}
