/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.context;

import java.util.ArrayList;
import java.util.List;


/*******************************************************************
 * <pre>
 * 1.설명 
 * 거래 클래스 
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
 * $Log: Trx.java,v $
 * Revision 1.1  2018/01/15 03:39:49  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:29  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:23  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:26  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.2  2008/09/19 07:01:39  youngseokkim
 * 거래제한 관련 로직 수정
 *
 * Revision 1.1  2008/08/04 08:54:52  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:25  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:09  안경아
 * *** empty log message ***
 *
 * Revision 1.4  2006/12/12 03:38:55  김성균
 * *** empty log message ***
 *
 * Revision 1.3  2006/06/22 04:47:28  김성균
 * bizAppId 필드 추가
 *
 * Revision 1.2  2006/06/21 13:47:30  김성균
 * 접근허용사용자ID 추가
 *
 * Revision 1.1  2006/06/20 04:13:20  김성균
 * 최초 등록
 *
 * </pre>
 ******************************************************************/
public class Trx {
	
	/**
	 * 거래ID
	 */
	private String trxId;
	
	/**
	 * 업무처리APP_ID
	 */
	private String bizAppId;
	
	/**
	 * 운영모드구분 
	 */
	private String operModeType;
	
	/**
	 * 거래중지여부
	 */
	private String trxStopYn;
	
	/**
	 * 거래명
	 */
	private String trxName;
	
	/**
	 * 거래설명
	 */
	private String trxDesc;
	
	/**
	 * 거래유형
	 */
	private String trxType;
	
	/**
	 * 영업일 거래사용여부
	 */
    private String bizdayTrxYn;
    
	/**
	 * 영업일 거래시작시간
	 */
    private String bizdayTrxStartTime;

	/**
	 * 영업일 거래종료시간
	 */
    private String bizdayTrxEndTime;

	/**
	 * 토요일 거래사용여부
	 */
    private String saturdayTrxYn;

	/**
	 * 토요일 거래시작시간
	 */
    private String saturdayTrxStartTime;

	/**
	 * 토요일 거래종료시간
	 */
    private String saturdayTrxEndTime;

	/**
	 * 공휴일 거래사용여부
	 */
    private String holidayTrxYn;

	/**
	 * 공휴일 거래시작시간
	 */
    private String holidayTrxStartTime;

	/**
	 * 공휴일 거래종료시간
	 */
    private String holidayTrxEndTime;
	
	/**
	 * 접근허용고객사용자ID
	 */
	private List custUserIds;
	
	public Trx() {
		custUserIds = new ArrayList(5);
	}

	/**
	 * @return Returns the bizAppId.
	 */
	public String getBizAppId() {
		return bizAppId;
	}

	/**
	 * @param bizAppId The bizAppId to set.
	 */
	public void setBizAppId(String bizAppId) {
		this.bizAppId = bizAppId;
	}

	/**
	 * @return Returns the operModeType.
	 */
	public String getOperModeType() {
		return operModeType;
	}

	/**
	 * @param operModeType The operModeType to set.
	 */
	public void setOperModeType(String operModeType) {
		this.operModeType = operModeType;
	}

	/**
	 * @return Returns the trxDesc.
	 */
	public String getTrxDesc() {
		return trxDesc;
	}

	/**
	 * @param trxDesc The trxDesc to set.
	 */
	public void setTrxDesc(String trxDesc) {
		this.trxDesc = trxDesc;
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
	 * @return Returns the trxName.
	 */
	public String getTrxName() {
		return trxName;
	}

	/**
	 * @param trxName The trxName to set.
	 */
	public void setTrxName(String trxName) {
		this.trxName = trxName;
	}

	/**
	 * @return Returns 거래중지여부 
	 */
	public boolean isTrxStop() {
		return "Y".equalsIgnoreCase(trxStopYn);
	}

	/**
	 * @param trxStopYn The trxStopYn to set.
	 */
	public void setTrxStopYn(String trxStopYn) {
		this.trxStopYn = trxStopYn;
	}

	/**
	 * @return Returns the trxType.
	 */
	public String getTrxType() {
		return trxType;
	}

	/**
	 * @param trxType The trxType to set.
	 */
	public void setTrxType(String trxType) {
		this.trxType = trxType;
	}

    /**
     * @return Returns the bizdayTrxYn.
     */
    public boolean isBizdayTrx() {
    	return "Y".equalsIgnoreCase(bizdayTrxYn);
    }
    /**
     * @param bizdayTrxYn The bizdayTrxYn to set.
     */
    public void setBizdayTrxYn(String bizdayTrxYn) {
    	this.bizdayTrxYn = bizdayTrxYn;
    }
    /**
     * @return Returns the bizdayTrxStartTime.
     */
    public String getBizdayTrxStartTime() {
    	return bizdayTrxStartTime;
    }
    /**
     * @param bizdayTrxStartTime The bizdayTrxStartTime to set.
     */
    public void setBizdayTrxStartTime(String bizdayTrxStartTime) {
    	this.bizdayTrxStartTime = bizdayTrxStartTime;
    }
    /**
     * @return Returns the bizdayTrxEndTime.
     */
    public String getBizdayTrxEndTime() {
    	return bizdayTrxEndTime;
    }
    /**
     * @param bizdayTrxEndTime The bizdayTrxEndTime to set.
     */
    public void setBizdayTrxEndTime(String bizdayTrxEndTime) {
    	this.bizdayTrxEndTime = bizdayTrxEndTime;
    }
    /**
     * @return Returns the saturdayTrxYn.
     */
    public boolean isSaturdayTrx() {
    	return "Y".equalsIgnoreCase(saturdayTrxYn);
    }
    /**
     * @param saturdayTrxYn The saturdayTrxYn to set.
     */
    public void setSaturdayTrxYn(String saturdayTrxYn) {
    	this.saturdayTrxYn = saturdayTrxYn;
    }
    /**
     * @return Returns the saturdayTrxStartTime.
     */
    public String getSaturdayTrxStartTime() {
    	return saturdayTrxStartTime;
    }
    /**
     * @param saturdayTrxStartTime The saturdayTrxStartTime to set.
     */
    public void setSaturdayTrxStartTime(String saturdayTrxStartTime) {
    	this.saturdayTrxStartTime = saturdayTrxStartTime;
    }
    /**
     * @return Returns the saturdayTrxEndTime.
     */
    public String getSaturdayTrxEndTime() {
    	return saturdayTrxEndTime;
    }
    /**
     * @param saturdayTrxEndTime The saturdayTrxEndTime to set.
     */
    public void setSaturdayTrxEndTime(String saturdayTrxEndTime) {
    	this.saturdayTrxEndTime = saturdayTrxEndTime;
    }
    /**
     * @return Returns the holidayTrxYn.
     */
    public boolean isHolidayTrx() {
    	return "Y".equalsIgnoreCase(holidayTrxYn);
    }
    /**
     * @param holidayTrxYn The holidayTrxYn to set.
     */
    public void setHolidayTrxYn(String holidayTrxYn) {
    	this.holidayTrxYn = holidayTrxYn;
    }
    /**
     * @return Returns the holidayTrxStartTime.
     */
    public String getHolidayTrxStartTime() {
    	return holidayTrxStartTime;
    }
    /**
     * @param holidayTrxStartTime The holidayTrxStartTime to set.
     */
    public void setHolidayTrxStartTime(String holidayTrxStartTime) {
    	this.holidayTrxStartTime = holidayTrxStartTime;
    }
    /**
     * @return Returns the holidayTrxEndTime.
     */
    public String getHolidayTrxEndTime() {
    	return holidayTrxEndTime;
    }
    /**
     * @param holidayTrxEndTime The holidayTrxEndTime to set.
     */
    public void setHolidayTrxEndTime(String holidayTrxEndTime) {
    	this.holidayTrxEndTime = holidayTrxEndTime;
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
	
	
}
