/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.service.util;

import nebsoa.biz.util.BizGroup;
import nebsoa.biz.util.BizGroupManager;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.BizDayCheckUtil;
import nebsoa.common.util.DataMap;
import nebsoa.common.util.StringUtil;
import nebsoa.service.context.ServiceContext;
import nebsoa.service.context.ServiceEngineContext;
import nebsoa.service.exception.ServiceErrorCode;
import nebsoa.service.exception.ServiceException;
import nebsoa.service.exception.ServiceNotFoundException;
import nebsoa.service.info.Service;
import nebsoa.spiderlink.context.MessageEngineContext;
import nebsoa.spiderlink.context.TrxMessage;

import org.apache.commons.collections.map.MultiKeyMap;

/*******************************************************************
 * <pre>
 * 1.설명 
 * serviceId 를 인자로 해당 서비스의 정보를 찾아 오는 유틸리티.
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
 * $Log: ServiceManager.java,v $
 * Revision 1.1  2018/01/15 03:39:50  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:49  cvs
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
 * Revision 1.2  2008/09/19 07:00:55  youngseokkim
 * 불필요 import 제거
 *
 * Revision 1.1  2008/08/04 08:54:51  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.11  2008/06/23 05:28:06  김영석
 * 서비스 접근허용자 로직 추가
 *
 * Revision 1.10  2008/06/12 01:59:05  김영석
 * 24시간 제공 서비스가 아니면 서비스 가능 여부 및 가능시간을 체크 부분 대리 클래스로 로직 이동
 *
 * Revision 1.9  2008/06/10 05:05:34  김영석
 * 24시간 제공 서비스가 아니면 서비스 가능 여부 및 가능시간을 체크하도록 수정
 *
 * Revision 1.8  2008/05/21 05:19:50  최수종
 * 서비스 정보를 초기화(미리 로딩)하는 메소드 추가
 *
 * Revision 1.7  2008/05/21 01:47:52  김은정
 * reload 메소드 추가
 *
 * Revision 1.6  2008/05/19 04:45:22  김영석
 * 서비스 옵션 체크 로직 변경
 *
 * Revision 1.5  2008/03/18 06:48:06  김영석
 * 에러메시지 수정
 *
 * Revision 1.4  2008/03/13 02:05:25  김영석
 * 로그인 상태 체크 로직 추가
 *
 * Revision 1.3  2008/02/18 07:11:56  김성균
 * 로그 정리
 *
 * Revision 1.2  2008/02/14 09:27:30  김성균
 * 예외처리 및 프로파일관련 수정
 *
 * Revision 1.1  2008/01/22 05:58:18  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.7  2008/01/07 03:49:33  김성균
 * 타행상태체크시 오류코드 수정
 *
 * Revision 1.6  2008/01/07 01:35:28  김성균
 * 타행상태 체크기능 추가
 *
 * Revision 1.5  2008/01/04 08:23:54  김성균
 * getService() 로그 삭제
 *
 * Revision 1.4  2008/01/04 02:50:49  김성균
 * 로그인필수여부 체크기능 추가
 *
 * Revision 1.3  2008/01/02 09:46:35  김성균
 * web 관련 기능 제외
 *
 * Revision 1.2  2007/12/24 02:30:30  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2007/12/20 01:36:26  김성균
 * 서비스관련 패키지 최초등록
 *
 * </pre>
 ******************************************************************/
public class ServiceManager {

    private ServiceManager() {  
    }
    
    /**
     * 서비스를 수행하기 위한 ServiceContext 객체를 생성한다. 
     * @param serviceId
     * @return ServiceContext 인스턴스
     */
    public static ServiceContext createServiceContext(String serviceId) {
        LogManager.info(" ====== ServiceContext 생성 ======");
        
        Service service = getService(serviceId);
        ServiceContext serviceContext = new ServiceContext(service);
        
        String trxId = service.getTrxId();
        String orgId = service.getOrgId();
        String ioType = service.getIoType();
        
        TrxMessage trxMessage = getTrxMessage(trxId, orgId, ioType);
        if (trxMessage != null) {
            serviceContext.setTrxMessage(trxMessage);
        }
        
        return serviceContext;
    }

    /**
     * @param serviceContext
     * @param trxId
     * @param orgId
     * @param ioType
     */
    private static TrxMessage getTrxMessage(String trxId, String orgId, String ioType) {
        if (StringUtil.isNull(trxId)  || StringUtil.isNull(orgId) || StringUtil.isNull(ioType)) {
            return null;
        }
        
        LogManager.info(" *** 서비스 trxId : " + trxId);
        LogManager.info(" *** 서비스 orgId : " + orgId);
        LogManager.info(" *** 서비스 ioType : " + ioType);
        
        MultiKeyMap trxMessageInfoMap = MessageEngineContext.getContext().getTrxMessageInfoMap();
        TrxMessage trxMessage = (TrxMessage) trxMessageInfoMap.get(trxId, orgId, ioType);
        
        return trxMessage;
    }

    /**
     * serviceId에 해당하는 Service 정보를 얻어온다.
     * 서비스 정보를 찾지 못했을 경우는 ServiceNotFoundException을 던진다.
     * 
     * @param serviceId
     * @return
     */
    public static Service getService(String serviceId) {
        Service service = ServiceEngineContext.getInstance().getService(serviceId);
        if (service == null) {
            throw new ServiceNotFoundException(serviceId);
        }
        return service;
    }
    
    /**
     * WORK_SPACE_ID를 얻어온다.
     * @param serviceId
     * @return
     */
    public static String getWorkSpaceId(String serviceId) {
        Service service = getService(serviceId);
        String workSpaceId = service.getWorkSpaceId();
        if (StringUtil.isNull(workSpaceId)) {
            String bizGroupId = service.getBizGroupId();
            if (!StringUtil.isNull(bizGroupId)) {
                BizGroup bizGroup = BizGroupManager.getInstance().getBizGroup(bizGroupId);
                if (bizGroup != null) {
                    workSpaceId = bizGroup.getDefaultWorkSpaceId();
                }
            }
        }
        return workSpaceId;
    }
    
    /**
     * 로그인 상태를 체크한다.
     * @param serviceId
     */
    public static void checkLoginStatus(String serviceId, DataMap map) throws ServiceException {
    	Service service = getService(serviceId);
    	
        // 로그인 상태를 체크한다.
        if (service.isLoginOnly() && !"Y".equals(map.get("로그인여부"))) { 
            throw new ServiceException(ServiceErrorCode.LOGIN_CHECK_ERROR, "로그인 하지 않았거나 세션이 종료 되었습니다.");
        }
    }

    /**
     * 영업일, 토요일, 공휴일별 서비스 가능여부와 서비스 가능시간을 체크한다.
     * @param serviceId
     */
    public static void checkServiceEnableTime(String serviceId) throws ServiceException {
    	BizDayCheckUtil.getInstance().isServiceEnableTime(serviceId);
    }
    
    /**
     * 타행상태체크여부가 설정되어 있으면 타행상태를 체크한다.
     * @param serviceId
     */
    public static void checkBankStatus(String serviceId, DataMap map) throws ServiceException {
        Service service = getService(serviceId);
        
        if (service.isBankStatusCheck()) { 
            String bankCode = map.getString(service.getBankCodeField()); 
            if (StringUtil.isNull(bankCode)) {
                throw new ServiceException(ServiceErrorCode.BANK_CODE_ERROR, "은행코드 값이 NULL 입니다.");
            } else {
                // 만약 은행코드값에 해당하는 은행이 stop이면
                if (!BankStatusManager.getInstance().isStable(bankCode)) {
                    throw new ServiceException(ServiceErrorCode.BANK_STATUS_CHECK_ERROR, "해당은행은 현재 서비스 가능상태가 아닙니다.[bankCode=" + bankCode + "]");
                }
            }
        } 
    }

    /**
     * 전자서명로그여부가 설정되어 있으면 전자서명로그를 체크한다.
     * @param serviceId
     */
    public static void checkSecureSignEnable(String serviceId, DataMap map) throws ServiceException {
    	Service service = getService(serviceId);
    	if(service.isSecureSign()) {
    		if(!map.containsKey("signed_msg")) {
    			throw new ServiceException(ServiceErrorCode.SIGNED_MSG_KEY_NOT_FOUND_ERROR, "DataMap에서 전자서명 메시지 키를 찾을 수 없습니다.");
    		}
    		if(StringUtil.isNull(map.getString("signed_msg"))) {
    			throw new ServiceException(ServiceErrorCode.SIGNED_MSG_KEY_NULL_ERROR, "전자서명 메시지 키 값이 NULL입니다.");
    		}
    	}
    }
    
    /**
     * 중지상태인 서비스 인지 체크 및 접근허용자 체크
     * @param serviceId
     */
    public static void checkServiceStopYn(String serviceId, DataMap map) throws ServiceException {
    	Service service = getService(serviceId);
    	
    	if(!service.isUse() && !service.isAccessUser(map.getString("_$USER_ID"))) {
       		throw new ServiceException(ServiceErrorCode.SERVICE_STOPPED_ERROR, service.getServiceName() + " 서비스를 현재 사용할 수 없습니다.");
    	}
    }

    /**
     * 서비스엔진 정보를 리로딩 한다.
     * @param map
     */
    public static void reloadAll(DataMap map) {
        ServiceEngineContext.reloadAll(map);
    }
    
    /**
     * 특정 서비스 정보를 리로딩 한다.
     * @param map
     */
    public static void reload(DataMap map) {
        ServiceEngineContext.reload(map);
    }    
    
    /**
     * 미리 서비스 엔진 정보를 리로딩한다.
     * 
     * 이 메소드는 서블릿 엔진 구동시 init()메소드에 정의하여 
     * 미리 서비스 정보를 로딩하도록 함.
     */
    public static void preload() {
    	ServiceEngineContext.getInstance();
    }
}
