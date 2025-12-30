/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.batch;

/*******************************************************************
 * <pre>
 * 1.설명 
 * BatchLoader에 의해 로딩되는 배치 APP의 정보 저장
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
 * $Log: BatchInfo.java,v $
 * Revision 1.1  2018/01/15 03:39:50  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:47  cvs
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
 * Revision 1.1  2008/08/04 08:54:53  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:28  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:59  안경아
 * *** empty log message ***
 *
 * Revision 1.5  2006/09/14 04:21:49  오재훈
 * *** empty log message ***
 *
 * Revision 1.4  2006/09/13 13:09:40  오재훈
 * 배치 수동 실행까지 적용.
 *
 * Revision 1.3  2006/09/12 09:04:31  오재훈
 * *** empty log message ***
 *
 * Revision 1.2  2006/09/09 07:30:16  오재훈
 * *** empty log message ***
 *
 *
 *
 * </pre>
 ******************************************************************/
public class BatchInfo {

	/**
	 * 배치 ID
	 */
	private String bathcAppId = "";
	/**
	 * 배치 APP 클래스 풀패키지명
	 */
	private String batchAppFileName = "";
	/**
	 * 선행 배치 ID
	 */
	private String preBatchAppId = "";
	/**
	 * 크론 표현식
	 */
	private String cronText = "";
	/**
	 * 재시도 여부
	 */
	private String retryableYn = "";
	/**
	 * WAS별 실행 여부
	 */
	private String perWasYn = "";
	/**
	 * 중요도
	 */
	private String importantType = "";
	/**
	 * 배치 APP의 파라미터 정보
	 */
	private String properties = "";
	/**
	 * 거래 ID
	 */
	private String trxId = "";
	/**
	 * 기관 ID
	 */
	private String orgId = "";
	/**
	 * 기동,수동 구분
	 */
	private String ioType = "";

	
	public String toString(){
		String ret =
			"["
			+"batch app id = "+this.bathcAppId
			+" , batch app file name = "+this.batchAppFileName
			+" , pre batch app id = "+this.preBatchAppId
			+" , cronTest = "+this.cronText
			+" , retryable yn = "+this.retryableYn
			+" , per was yn = "+this.perWasYn
			+" , important type = "+this.importantType
			+" , properties = "+this.properties
			+" , trx id = "+this.trxId
			+" , org id = "+this.orgId
			+" , iotype = "+this.ioType
			+"]";
		
		return ret;
	}
	
	public String getBatchAppFileName() {
		return batchAppFileName;
	}
	public void setBatchAppFileName(String batchAppFileName) {
		this.batchAppFileName = batchAppFileName;
	}
	public String getBathcAppId() {
		return bathcAppId;
	}
	public void setBathcAppId(String bathcAppId) {
		this.bathcAppId = bathcAppId;
	}
	public String getCronText() {
		return cronText;
	}
	public void setCronText(String cronText) {
		this.cronText = cronText;
	}
	public String getImportantType() {
		return importantType;
	}
	public void setImportantType(String importantType) {
		this.importantType = importantType;
	}
	public String getIoType() {
		return ioType;
	}
	public void setIoType(String ioType) {
		this.ioType = ioType;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getPerWasYn() {
		return perWasYn;
	}
	public void setPerWasYn(String perWasYn) {
		this.perWasYn = perWasYn;
	}
	public String getPreBatchAppId() {
		return preBatchAppId;
	}
	public void setPreBatchAppId(String preBatchAppId) {
		this.preBatchAppId = preBatchAppId;
	}
	public String getProperties() {
		return properties;
	}
	public void setProperties(String properties) {
		this.properties = properties;
	}
	public String getRetryableYn() {
		return retryableYn;
	}
	public void setRetryableYn(String retryableYn) {
		this.retryableYn = retryableYn;
	}
	public String getTrxId() {
		return trxId;
	}
	public void setTrxId(String trxId) {
		this.trxId = trxId;
	}

}
