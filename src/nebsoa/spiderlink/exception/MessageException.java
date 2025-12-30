/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.exception;

import nebsoa.common.exception.NestedCheckedException;
import nebsoa.common.exception.SpiderException;
import nebsoa.common.util.DataMap;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 전문처리 로직 수행 중 발생하는 예외상황에 대한 Exception 클래스 입니다.
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
 * $Log: MessageException.java,v $
 * Revision 1.1  2018/01/15 03:39:51  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:54  cvs
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
 * Revision 1.2  2008/08/04 09:46:52  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.3  2008/06/30 00:44:19  김은정
 * constructor에 dataMap추가
 *
 * Revision 1.2  2008/06/23 01:32:40  김은정
 * MessageException의 constructor에  dataMap 추가
 *
 * Revision 1.1  2008/01/22 05:58:26  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:39:01  안경아
 * *** empty log message ***
 *
 * Revision 1.13  2006/11/27 05:00:41  김승희
 * orgUid 필드 추가
 *
 * Revision 1.12  2006/11/15 11:50:47  김성균
 * *** empty log message ***
 *
 * Revision 1.11  2006/11/15 11:44:21  김성균
 * *** empty log message ***
 *
 * Revision 1.10  2006/10/27 10:24:03  김성균
 * *** empty log message ***
 *
 * Revision 1.9  2006/08/22 07:24:04  김성균
 * 생성자 추가
 *
 * Revision 1.8  2006/08/17 12:09:06  김성균
 * Exception 관련 변경
 *
 * Revision 1.7  2006/07/31 11:08:35  김승희
 * 주석추가
 *
 * Revision 1.6  2006/07/31 10:41:48  김승희
 * MessageException 멤버변수 추가
 *
 * Revision 1.5  2006/07/28 09:30:11  김승희
 * exception 처리 변경
 *
 * Revision 1.4  2006/07/12 12:29:20  김승희
 * 주석 처리
 *
 * Revision 1.3  2006/07/06 08:40:21  김승희
 * exception 처리 관련 수정
 *
 * Revision 1.2  2006/07/03 12:36:15  김승희
 * *** empty log message ***
 *
 * Revision 1.1  2006/06/19 13:46:17  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class MessageException extends NestedCheckedException implements SpiderException {
	
	/**
	 *  serialVersionUID
	 */
	private static final long serialVersionUID = -3640307762818727599L;

	/**
	 *  표준 에러 코드
	 */
	protected String stdErrorCode;
	
	/**
	 *  기관 에러 코드
	 */
	protected String orgErrorCode;
	
	/**
	 *  에러 메시지
	 */
	protected String errorMessage;
    
	/**
	 *  주 에러 메시지
	 */
	protected String masterErrorMessage;
    
	/**
	 *  부 에러 메시지
	 */
	protected String subErrorMessage;
	
	/**
	 *  거래ID
	 */
	protected String trxId;
	
	/**
	 *  기관ID
	 */
	protected String orgId;
	
	/**
	 *  UID
	 */
	protected String uid;
	
	/**
	 *  기관 UID
	 */
	protected String orgUid;
	
	protected String orgType;
	/**
	 *  거래 시각
	 */
	protected String trxTime;
		
	private DataMap dataMap;
	
	
	/**
	 * 에러 메시지를 리턴한다.
     * @return Returns the masterErrorMessage.
     */
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * 주 에러 메시지를 리턴한다.
     * @return Returns the masterErrorMessage.
     */
    public String getMasterErrorMessage() {
        return masterErrorMessage;
    }

    /**
     * @param masterErrorMessage The masterErrorMessage to set.
     */
    public void setMasterErrorMessage(String masterErrorMessage) {
        this.masterErrorMessage = masterErrorMessage;
    }

    /**
	 * 부 에러 메시지를 리턴한다.
     * @return Returns the subErrorMessage.
     */
    public String getSubErrorMessage() {
        return subErrorMessage;
    }

    /**
     * @param subErrorMessage The subErrorMessage to set.
     */
    public void setSubErrorMessage(String subErrorMessage) {
        this.subErrorMessage = subErrorMessage;
    }

    /**
	 * 표준에러코드를 리턴한다.
	 * @return 표준에러코드
	 */
	public String getStdErrorCode() {
		return stdErrorCode;
	}

	public void setStdErrorCode(String stdErrorCode) {
		this.stdErrorCode = stdErrorCode;
	}


	public void setOrgErrorCode(String orgErrorCode) {
		this.orgErrorCode = orgErrorCode;
	}

	/**
	 * 생성자
	 * @param stdErrorCode 표준 에러 코드
	 * @param orgErrorCode 기관 에러 코드
	 * @param errorMessage 에러 메시지
	 */
	public MessageException(String stdErrorCode, String orgErrorCode, String errorMessage){
		this(stdErrorCode, orgErrorCode, null, errorMessage, errorMessage, null);
	}
    
	/**
	 * 생성자
	 * @param stdErrorCode 표준 에러 코드
	 * @param orgErrorCode 기관 에러 코드
	 * @param errorMessage 에러 메시지
	 */
	public MessageException(String stdErrorCode, String orgErrorCode, String orgType, String errorMessage, String masterErrorMessage, String subErrorMessage){
		this.stdErrorCode = stdErrorCode;
		this.orgErrorCode = orgErrorCode;
		this.orgType	  = orgType;
		this.errorMessage = errorMessage;
		this.masterErrorMessage = masterErrorMessage;
		this.subErrorMessage = subErrorMessage;
	}
	
	/**
	 * 생성자
	 * @param stdErrorCode 표준 에러 코드
	 * @param errorMessage 에러 메시지
	 */
	public MessageException(String stdErrorCode, String errorMessage){
		this(stdErrorCode, null, errorMessage);
	}
    
	/**
	 * 생성자
	 * @param errorMessage 에러 메시지
	 */
	public MessageException(String errorMessage){
		this(null, null, errorMessage);
	}
	
	/**
	 * 생성자
	 * @param stdErrorCode 표준 에러 코드
	 * @param orgErrorCode 기관 에러 코드
	 * @param errorMessage 에러 메시지
	 * @param datamap 
	 */
	public MessageException(String stdErrorCode, String orgErrorCode, String errorMessage, DataMap dataMap){
		this(stdErrorCode, orgErrorCode, null, errorMessage, errorMessage, null);
		this.dataMap = dataMap;
	}	
	
	public MessageException(String stdErrorCode, String orgErrorCode, String orgType, String errorMessage, DataMap dataMap){
		this(stdErrorCode, orgErrorCode, orgType, errorMessage, errorMessage, null);
		this.dataMap = dataMap;
	}	
	
	public MessageException(String stdErrorCode, String errorMessage, DataMap dataMap){
		this(stdErrorCode, null, errorMessage, dataMap);
	}
    	
	
	
	/**
	 * 에러 메시지를 리턴한다.
	 * @return 에러 메시지
	 */
	public String getMessage(){
		return errorMessage;
	}
	
	/**
	 * 표준 에러코드를 리턴한다.
	 * @return 표준 에러코드
	 */
	public String getErrorCode(){
		return stdErrorCode;
	}
	
	/**
	 * 기관에서 발생시킨 에러코드를 리턴한다.
	 * @return 기관 에러코드
	 */
	public String getOrgErrorCode(){
		return orgErrorCode;
	}
	
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getTrxId() {
		return trxId;
	}

	public void setTrxId(String trxId) {
		this.trxId = trxId;
	}

	public String getTrxTime() {
		return trxTime;
	}

	public void setTrxTime(String trxTime) {
		this.trxTime = trxTime;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getOrgUid() {
		return orgUid;
	}

	public void setOrgUid(String orgUid) {
		this.orgUid = orgUid;
	}
    
	public DataMap getDataMap(){
		return dataMap;
	}	
	public void setDataMap(DataMap dataMap){
		this.dataMap = dataMap;
	}		
	
	public String getOrgType() {
		return orgType;
	}
	
	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}
}
