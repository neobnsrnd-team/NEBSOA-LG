/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
 package nebsoa.service.info;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nebsoa.common.log.LogManager;
import nebsoa.common.util.FormatUtil;




/*******************************************************************
 * <pre>
 * 1.설명 
 * 서비스 정보를 로딩하여 담고 있는 클래스
 * 
 * 2.사용법
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author $Author: cvs $
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 
 * $Log: Service.java,v $
 * Revision 1.1  2018/01/15 03:39:52  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:14  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:26  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:28  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:55  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.5  2008/06/23 05:28:06  김영석
 * 서비스 접근허용자 로직 추가
 *
 * Revision 1.4  2008/06/20 02:13:03  김영석
 * 접근허용자 리로딩 로직 추가
 *
 * Revision 1.3  2008/05/19 04:45:22  김영석
 * 서비스 옵션 체크 로직 변경
 *
 * Revision 1.2  2008/02/14 09:26:48  김성균
 * 서비스 정보 로딩 관련 버그 수정
 *
 * Revision 1.1  2008/01/22 05:58:32  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.3  2008/01/07 01:34:38  김성균
 * 타행상태체크관련 컬럼추가
 *
 * Revision 1.2  2008/01/04 02:41:47  김성균
 * 컬럼추가 반영 및 서비스제공시간관련 기능 추가
 *
 * Revision 1.1  2007/12/31 04:35:34  최수종
 * nebsoa.service 패키지 추가
 *
 * </pre>
 ******************************************************************/
public class Service {
    
	/**
     * 서비스ID
     */
    private String serviceId;
    
    /**
     * 서비스명
     */
    private String serviceName;
    
    private String className;
    private String methodName;
    private String serviceType;
    private String preProcessAppId;
    private String postProcessAppId;
    private String timeCheckYn;
    private String startTime;
    private String endTime;
    private String bizDayCheckYn;
    private String useYn;
    private String trxId;
    private String orgId;
    private String ioType;
    private String bizGroupId;
    private String workSpaceId;
    private String loginOnlyYn;
    private String secureSignYn;
    private String reqChannelCode;
    private String svcConf1;
    private String svcConf2;
    private String bankStatusCheckYn;
    private String bankCodeField;
    private String bizdayServiceYn;
    private String bizdayServiceStartTime;
    private String bizdayServiceEndTime;
    private String saturdayServiceYn;
    private String saturdayServiceStartTime;
    private String saturdayServiceEndTime;
    private String holidayServiceYn;
    private String holidayServiceStartTime;
    private String holidayServiceEndTime;
    
    private List serviceComponentList; 

	/**
	 * 접근허용고객사용자ID
	 */
	private List custUserIds;
	
    
    public Service() {
        serviceComponentList = new ArrayList();
		custUserIds = new ArrayList(5);
    }
    
    /**
     * @return Returns the serviceId.
     */
    public String getServiceId() {
        return serviceId;
    }

    /**
     * @param serviceId The serviceId to set.
     */
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    /**
     * @return Returns the serviceName.
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * @param serviceName The serviceName to set.
     */
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
    
    /**
     * @return Returns the className.
     */
    public String getClassName() {
        return className;
    }

    /**
     * @param className The className to set.
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * @return Returns the methodName.
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * @param methodName The methodName to set.
     */
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    /**
     * @return Returns the serviceType.
     */
    public String getServiceType() {
        return serviceType;
    }

    /**
     * @param serviceType The serviceType to set.
     */
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    /**
     * @return Returns the preProcessAppId.
     */
    public String getPreProcessAppId() {
        return preProcessAppId;
    }

    /**
     * @param preProcessAppId The preProcessAppId to set.
     */
    public void setPreProcessAppId(String preProcessAppId) {
        this.preProcessAppId = preProcessAppId;
    }

    /**
     * @return Returns the postProcessAppId.
     */
    public String getPostProcessAppId() {
        return postProcessAppId;
    }

    /**
     * @param postProcessAppId The postProcessAppId to set.
     */
    public void setPostProcessAppId(String postProcessAppId) {
        this.postProcessAppId = postProcessAppId;
    }

    /**
     * @return Returns the timeCheckYn.
     */
    public boolean isTimeCheck() {
        boolean isTimeCheck = "Y".equalsIgnoreCase(timeCheckYn);
        if (isTimeCheck) LogManager.debug("### 서비스가능시간체크 ###");
        return isTimeCheck;
    }
    
    /**
     * 현재시간으로 서비스가능시간을 체크한다.
     * @return 유효시간여부
     */
    public boolean isValidTime() {
        int st = Integer.parseInt(startTime);
        int et = Integer.parseInt(endTime);
        int currTime = Integer.parseInt(FormatUtil.getToday("HHmm"));
        
        if (currTime >= st && currTime <= et) {
            return true;
        } else {
            LogManager.debug("### 서비스가능시간 아님 : " + currTime + "|" + startTime + "|" + endTime);
            return false;
        }
    }

    /**
     * @param timeCheckYn The timeCheckYn to set.
     */
    public void setTimeCheckYn(String timeCheckYn) {
        this.timeCheckYn = timeCheckYn;
    }

    /**
     * @return Returns the startTime.
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * @param startTime The startTime to set.
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * @return Returns the endTime.
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * @param endTime The endTime to set.
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * @return Returns the bizDayCheckYn.
     */
    public boolean isBizDayCheck() {
        boolean isBizDayCheck = "Y".equalsIgnoreCase(bizDayCheckYn);
        if (isBizDayCheck) LogManager.debug("### 영업일체크 ###");
        return isBizDayCheck;
    }

    /**
     * @param bizDayCheckYn The bizDayCheckYn to set.
     */
    public void setBizDayCheckYn(String bizDayCheckYn) {
        this.bizDayCheckYn = bizDayCheckYn;
    }

    /**
     * @return Returns the trxId.
     */
    public String getTrxId() {
        return trxId;
    }

    /**
     * @param trxId The trxId to set.
     */
    public void setTrxId(String trxId) {
        this.trxId = trxId;
    }

    /**
     * @return Returns the orgId.
     */
    public String getOrgId() {
        return orgId;
    }

    /**
     * @param orgId The orgId to set.
     */
    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    /**
     * @return Returns the ioType.
     */
    public String getIoType() {
        return ioType;
    }

    /**
     * @param ioType The ioType to set.
     */
    public void setIoType(String ioType) {
        this.ioType = ioType;
    }

    /**
     * @return Returns the bizGroupId.
     */
    public String getBizGroupId() {
        return bizGroupId;
    }

    /**
     * @param bizGroupId The bizGroupId to set.
     */
    public void setBizGroupId(String bizGroupId) {
        this.bizGroupId = bizGroupId;
    }

    /**
     * @return Returns the workSpaceId.
     */
    public String getWorkSpaceId() {
        return workSpaceId;
    }

    /**
     * @param workSpaceId The workSpaceId to set.
     */
    public void setWorkSpaceId(String workSpaceId) {
        this.workSpaceId = workSpaceId;
    }
     
    /**
     * @return Returns the loginOnlyYn.
     */
    public boolean isLoginOnly() {
        return "Y".equalsIgnoreCase(loginOnlyYn);
    }

    /**
     * @param loginOnlyYn The loginOnlyYn to set.
     */
    public void setLoginOnlyYn(String loginOnlyYn) {
        this.loginOnlyYn = loginOnlyYn;
    }

    /**
     * @return Returns the secureSignYn.
     */
    public boolean isSecureSign() {
        return "Y".equalsIgnoreCase(secureSignYn);
    }

    /**
     * @param secureSignYn The secureSignYn to set.
     */
    public void setSecureSignYn(String secureSignYn) {
        this.secureSignYn = secureSignYn;
    }

    /**
     * @return Returns the reqChannelCode.
     */
    public String getReqChannelCode() {
        return reqChannelCode;
    }

    /**
     * @param reqChannelCode The reqChannelCode to set.
     */
    public void setReqChannelCode(String reqChannelCode) {
        this.reqChannelCode = reqChannelCode;
    }

    /**
     * @return Returns the svcConf1.
     */
    public String getSvcConf1() {
        return svcConf1;
    }

    /**
     * @param svcConf1 The svcConf1 to set.
     */
    public void setSvcConf1(String svcConf1) {
        this.svcConf1 = svcConf1;
    }

    /**
     * @return Returns the svcConf2.
     */
    public String getSvcConf2() {
        return svcConf2;
    }

    /**
     * @param svcConf2 The svcConf2 to set.
     */
    public void setSvcConf2(String svcConf2) {
        this.svcConf2 = svcConf2;
    }
    
    /**
     * @return Returns the bankStatusCheckYn.
     */
    public boolean isBankStatusCheck() {
        return "Y".equalsIgnoreCase(bankStatusCheckYn);
    }

    /**
     * @param bankStatusCheckYn The bankStatusCheckYn to set.
     */
    public void setBankStatusCheckYn(String bankStatusCheckYn) {
        this.bankStatusCheckYn = bankStatusCheckYn;
    }

    /**
     * @return Returns the bankCodeField.
     */
    public String getBankCodeField() {
        return bankCodeField;
    }

    /**
     * @param bankCodeField The bankCodeField to set.
     */
    public void setBankCodeField(String bankCodeField) {
        this.bankCodeField = bankCodeField;
    }

    /**
     * @return Returns the bizdayServiceYn.
     */
    public boolean isBizdayService() {
    	return "Y".equalsIgnoreCase(bizdayServiceYn);
    }
    /**
     * @param bizdayServiceYn The bizdayServiceYn to set.
     */
    public void setBizdayServiceYn(String bizdayServiceYn) {
    	this.bizdayServiceYn = bizdayServiceYn;
    }
    /**
     * @return Returns the bizdayServiceStartTime.
     */
    public String getBizdayServiceStartTime() {
    	return bizdayServiceStartTime;
    }
    /**
     * @param bizdayServiceStartTime The bizdayServiceStartTime to set.
     */
    public void setBizdayServiceStartTime(String bizdayServiceStartTime) {
    	this.bizdayServiceStartTime = bizdayServiceStartTime;
    }
    /**
     * @return Returns the bizdayServiceEndTime.
     */
    public String getBizdayServiceEndTime() {
    	return bizdayServiceEndTime;
    }
    /**
     * @param bizdayServiceEndTime The bizdayServiceEndTime to set.
     */
    public void setBizdayServiceEndTime(String bizdayServiceEndTime) {
    	this.bizdayServiceEndTime = bizdayServiceEndTime;
    }

    /**
     * @return Returns the saturdayServiceYn.
     */
    public boolean isSaturdayService() {
    	return "Y".equalsIgnoreCase(saturdayServiceYn);
    }
    /**
     * @param saturdayServiceYn The saturdayServiceYn to set.
     */
    public void setSaturdayServiceYn(String saturdayServiceYn) {
    	this.saturdayServiceYn = saturdayServiceYn;
    }
    /**
     * @return Returns the saturdayServiceStartTime.
     */
    public String getSaturdayServiceStartTime() {
    	return saturdayServiceStartTime;
    }
    /**
     * @param saturdayServiceStartTime The saturdayServiceStartTime to set.
     */
    public void setSaturdayServiceStartTime(String saturdayServiceStartTime) {
    	this.saturdayServiceStartTime = saturdayServiceStartTime;
    }
    /**
     * @return Returns the saturdayServiceEndTime.
     */
    public String getSaturdayServiceEndTime() {
    	return saturdayServiceEndTime;
    }
    /**
     * @param saturdayServiceEndTime The saturdayServiceEndTime to set.
     */
    public void setSaturdayServiceEndTime(String saturdayServiceEndTime) {
    	this.saturdayServiceEndTime = saturdayServiceEndTime;
    }

    /**
     * @return Returns the holidayServiceYn.
     */
    public boolean isHolidayService() {
    	return "Y".equalsIgnoreCase(holidayServiceYn);
    }
    /**
     * @param holidayServiceYn The holidayServiceYn to set.
     */
    public void setHolidayServiceYn(String holidayServiceYn) {
    	this.holidayServiceYn = holidayServiceYn;
    }
    /**
     * @return Returns the holidayServiceStartTime.
     */
    public String getHolidayServiceStartTime() {
    	return holidayServiceStartTime;
    }
    /**
     * @param holidayServiceStartTime The holidayServiceStartTime to set.
     */
    public void setHolidayServiceStartTime(String holidayServiceStartTime) {
    	this.holidayServiceStartTime = holidayServiceStartTime;
    }
    /**
     * @return Returns the holidayServiceEndTime.
     */
    public String getHolidayServiceEndTime() {
    	return holidayServiceEndTime;
    }
    /**
     * @param holidayServiceEndTime The holidayServiceEndTime to set.
     */
    public void setHolidayServiceEndTime(String holidayServiceEndTime) {
    	this.holidayServiceEndTime = holidayServiceEndTime;
    }

    /**
     * 영업일일 경우 현재시간으로 서비스가능시간을 체크한다.
     * @return 유효시간여부
     */
    public boolean isBizdayServiceEnable() {
        int st = Integer.parseInt(bizdayServiceStartTime);
        int et = Integer.parseInt(bizdayServiceEndTime);
        int currTime = Integer.parseInt(FormatUtil.getToday("HHmm"));
        
        if (currTime >= st && currTime <= et) {
            return true;
        } else {
            LogManager.debug("### 서비스가능시간 아님 : " + currTime + "|" + bizdayServiceStartTime + "|" + bizdayServiceEndTime);
            return false;
        }
    }

    /**
     * 토요일일 경우 현재시간으로 서비스가능시간을 체크한다.
     * @return 유효시간여부
     */
    public boolean isSaturdayServiceEnable() {
        int st = Integer.parseInt(saturdayServiceStartTime);
        int et = Integer.parseInt(saturdayServiceEndTime);
        int currTime = Integer.parseInt(FormatUtil.getToday("HHmm"));
        
        if (currTime >= st && currTime <= et) {
            return true;
        } else {
            LogManager.debug("### 서비스가능시간 아님 : " + currTime + "|" + saturdayServiceStartTime + "|" + saturdayServiceEndTime);
            return false;
        }
    }

    /**
     * 공휴일일 경우 현재시간으로 서비스가능시간을 체크한다.
     * @return 유효시간여부
     */
    public boolean isHolidayServiceEnable() {
        int st = Integer.parseInt(holidayServiceStartTime);
        int et = Integer.parseInt(holidayServiceEndTime);
        int currTime = Integer.parseInt(FormatUtil.getToday("HHmm"));
        
        if (currTime >= st && currTime <= et) {
            return true;
        } else {
            LogManager.debug("### 서비스가능시간 아님 : " + currTime + "|" + holidayServiceStartTime + "|" + holidayServiceEndTime);
            return false;
        }
    }
    /**
     * @param serviceComponent
     */
    public void addServiceComponent(ServiceComponent serviceComponent) {
        serviceComponentList.add(serviceComponent);
    }
    
    /**
     * @param ServiceComponent
     */
    public ServiceComponent getServiceComponent(int serviceIndex) {
        return (ServiceComponent) serviceComponentList.get(serviceIndex);
    }
    
    
    /**
     * @return
     */
    public int getServiceComponentListSize() {
        return serviceComponentList.size();
    }
    
    /**
     * @param serviceIndex
     * @return
     */
    public Component getComponent(int serviceIndex) {
        return getServiceComponent(serviceIndex).getComponent();
    }
    
    /**
     * @param serviceIndex
     * @return
     */
    public Map getParameterMap(int serviceIndex) {
        return getServiceComponent(serviceIndex).getParameterMap();
    }
    
    /**
     * @param serviceIndex
     * @return
     */
    public String getPostCondition(int serviceIndex) {
        return getServiceComponent(serviceIndex).getPostCondition();
    }
    

	/**
	 * @return Returns the custUserIds.
	 */
	public List getCustUserIds() {
		return custUserIds;
	}

	/**
	 * @param custUserId The custUserIds to add.
	 */
	public void addCustUserId(String custUserId) {
		this.custUserIds.add(custUserId);
	}

	/**
	 * 접근허용사용자 인지 검사한다.
	 * @param userId 사용자ID
	 * @return 접근허용사용자여부
	 */
	public boolean isAccessUser(String userId) {
		return custUserIds.contains(userId);
	}

	public String getUseYn() {
		return useYn;
	}

	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}

	public boolean isUse() {
		return "Y".equalsIgnoreCase(useYn);
	}
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString(){
        return ">>> serviceId:"+serviceId
                +"\n serviceName:"+ serviceName
                +"\n preProcessAppId:"+ preProcessAppId
                +"\n postProcessAppId:"+ postProcessAppId
                +"\n useYn:"+ useYn
                +"\n timeCheckYn:"+ timeCheckYn
                +"\n bizDayCheckYn:"+ bizDayCheckYn
                +"\n loginOnlyYn:"+ loginOnlyYn
                +"\n secureSignYn:"+ secureSignYn
                +"\n bankStatusCheckYn:"+ bankStatusCheckYn
                ;
    }

}
