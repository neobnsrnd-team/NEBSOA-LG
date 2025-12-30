/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
package nebsoa.logserver;

import java.io.Serializable;
/*******************************************************************
* <pre>
* 1.설명 
* 로그로 남길 데이터를 담는 클래스
* 
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
* $Log: LogContext.java,v $
* Revision 1.1  2018/01/15 03:39:51  cvs
* *** empty log message ***
*
* Revision 1.1  2016/04/15 02:22:58  cvs
* neo cvs init
*
* Revision 1.1  2011/07/01 02:13:51  yshong
* *** empty log message ***
*
* Revision 1.1  2008/11/18 11:27:24  김성균
* *** empty log message ***
*
* Revision 1.1  2008/11/18 11:01:28  김성균
* LGT Gateway를 위한 프로젝트로 분리
*
* Revision 1.1  2008/08/04 08:54:52  youngseokkim
* *** empty log message ***
*
* Revision 1.1  2008/01/22 05:58:27  오재훈
* 패키지 리펙토링
*
* Revision 1.2  2007/12/17 01:48:35  김승희
* 로그 서버 서버 분리에 따른 수정
*
* Revision 1.1  2007/09/21 08:22:30  김승희
* 최초 등록
*
*
* </pre>
******************************************************************/
public class LogContext implements Serializable {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -856862711861971399L;
	
	/**
	 * 거래 ID
	 */
	private String trxId;
	
	/**
	 * 거래추적번호
	 */
	private String trxTraceId;
	
	/**
	 * 기관 ID
	 */
	private String orgId;
	
	/**
	 * 전문 ID
	 */
	private String messageId;
	
	/**
	 * 거래일자
	 */
	private String trxDate;
	
	/**
	 * 거래시간
	 */
	private String trxTime;
	
	/**
	 * 사용자 ID
	 */
	private String userId;
	
	/**
	 * 송수신 전문 로그 1
	 */
	private String logData1;
	
	/**
	 * 송수신 전문 로그 2
	 */
	private String logData2;
	
	/**
	 * 기동수동구분
	 */
	private String ioType;
		
	/**
	 * 요구응답구분
	 */
	private String reqResType;
	
	/**
	 * 결과코드
	 */
	private String resultCode;
	
	/**
	 * 서버ID
	 */
	private String serverId;
	
	/**
	 * 사용자 주민번호
	 */
	private String userSsn;
	
	/**
	 * 포맷된 로그 데이터
	 */
	private String formattedLogData;
	
	/**
	 * 로그 포맷시 사용하는 구분자
	 */
	private String delimeter = "|";
	
	public String getTrxTraceId() {
		return trxTraceId;
	}


	public void setTrxTraceId(String trxTraceId) {
		this.trxTraceId = trxTraceId;
	}


	public String getDelimeter() {
		return delimeter;
	}


	public void setDelimeter(String delimeter) {
		this.delimeter = delimeter;
	}


	public LogContext(){}

	
	public LogContext(String trxTraceNo, String orgId, String trxId,
			String messageId, String trxDate, String trxTime,
			String serverId, String ioType, String reqType, String userId, String ssn, 
			String resultCode, String logData1, String logData2) {
		this();
		this.trxTraceId = trxTraceNo;
		this.orgId = orgId;
		this.trxId = trxId;
		this.messageId = messageId;
		this.trxDate = trxDate;
		this.trxTime = trxTime;
		this.serverId = serverId;
		this.ioType = ioType;
		this.reqResType = reqType;
		this.userId = userId;
		this.userSsn = ssn;
		this.resultCode = resultCode;
		this.logData1 = logData1;
		this.logData2 = logData2;
	}


	public String getTrxId() {
		return trxId;
	}

	public void setTrxId(String trxId) {
		this.trxId = trxId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getTrxDate() {
		return trxDate;
	}

	public void setTrxDate(String trxDate) {
		this.trxDate = trxDate;
	}

	public String getTrxTime() {
		return trxTime;
	}

	public void setTrxTime(String trxTime) {
		this.trxTime = trxTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getIoType() {
		return ioType;
	}

	public void setIoType(String ioType) {
		this.ioType = ioType;
	}

	public String getReqResType() {
		return reqResType;
	}

	public void setReqResType(String reqType) {
		this.reqResType = reqType;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		
		makeformat(sb, trxTraceId);
		makeformat(sb, orgId);
		makeformat(sb, trxId);
		makeformat(sb, trxDate);
		makeformat(sb, trxTime);
		makeformat(sb, serverId);
		makeformat(sb, ioType);
		makeformat(sb, reqResType);
		makeformat(sb, userId);
		makeformat(sb, userId);
		makeformat(sb, resultCode).append(logData1);
		makeformat(sb, logData2);
		
		return sb.toString();
	}
	
	private StringBuffer makeformat(StringBuffer sb, String logToken){
		return sb.append(logToken).append(this.delimeter);
	}

	public String getServerId() {
		return serverId;
	}


	public void setServerId(String serverId) {
		this.serverId = serverId;
	}


	public String getUserSsn() {
		return userSsn;
	}


	public void setUserSsn(String ssn) {
		this.userSsn = ssn;
	}


	public String getFormattedLogData() {
		return formattedLogData;
	}


	public void setFormattedLogData(String formattedLogData) {
		this.formattedLogData = formattedLogData;
	}
	
	/**
	 * 모든 필드의 값을 ""로 셋팅한다.
	 */
	public void clear(){
		this.trxId="";
		this.trxTraceId="";
		this.orgId="";
		this.messageId="";
		this.trxDate="";
		this.trxTime="";
		this.userId="";
		this.logData1="";
		this.logData2="";
		this.ioType="";
		this.reqResType="";
		this.resultCode="";
		this.serverId="";
		this.userSsn="";
	}


	public String getLogData1() {
		return logData1;
	}


	public void setLogData1(String logData1) {
		this.logData1 = logData1;
	}


	public String getLogData2() {
		return logData2;
	}


	public void setLogData2(String logData2) {
		this.logData2 = logData2;
	}
}
