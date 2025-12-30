/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.context;

import java.io.Serializable;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 리스너로 수신한 전문에 대한 거래 매핑 정보를 담고 있는 클래스
 * 
 * 2.사용법
 * 
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author 이종원
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 
 * $Log&
 * </pre>
 ******************************************************************/
public class InputMessageTrxMapper implements Serializable {

    private String gwId;
    
    private String reqIdCode;
    
    private String orgId;
	
	private String trxId;
	
	private String ioType;
	
	private String bizAppId ;
	
	public InputMessageTrxMapper(){}
	

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

    public String getGwId() {
        return gwId;
    }

    public void setGwId(String gatewayId) {
        this.gwId = gatewayId;
    }

    public String getReqIdCode() {
        return reqIdCode;
    }

    public void setReqIdCode(String reqIdCode) {
        this.reqIdCode = reqIdCode;
    }


    public String getTrxId() {
        return trxId;
    }


    public void setTrxId(String trxId) {
        this.trxId = trxId;
    }


    public String getBizAppId() {
        return bizAppId;
    }


    public void setBizAppId(String bizAppId) {
        this.bizAppId = bizAppId;
    }
	
}
