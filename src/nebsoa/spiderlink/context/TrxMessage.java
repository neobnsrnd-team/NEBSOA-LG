/*
 * Spider Framework
 *
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 *
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.context;

import nebsoa.common.exception.SysException;


/*******************************************************************
 * <pre>
 * 1.설명
 * 거래별 전문 클래스
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
 * $Log: TrxMessage.java,v $
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
 * Revision 1.2  2008/10/30 01:41:52  jwlee
 * 다중 응답 전문인 경우 멀티Thread의 이슈를 피하기 위해 추가.
 *
 * Revision 1.1  2008/08/04 08:54:52  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.4  2008/05/19 11:24:17  김승희
 * 대응답데이터 필드(ProxtResData)의 데이터 타입을 String에서 Object로 변경
 *
 * Revision 1.3  2008/03/19 02:19:44  김승희
 * FWK_TRX_MESSAGE 테이블의 MULTI_RES_YN  필드 삭제에 따른 변경
 *
 * Revision 1.2  2008/03/11 01:25:08  김승희
 * 응답메시지 타입 (응답 메시지 갯수 구분) 관련 속성 추가
 *
 * Revision 1.1  2008/01/22 05:58:25  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.5  2008/01/11 10:40:21  안경아
 * 다중응답전문 처리
 *
 * Revision 1.4  2007/12/26 11:30:26  안경아
 * *** empty log message ***
 *
 * Revision 1.3  2007/12/24 09:02:31  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2007/12/18 08:31:08  김승희
 * PROXY_RES_TYPE 필드 추가
 *
 * Revision 1.1  2007/11/26 08:38:09  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2006/06/20 04:13:20  김성균
 * 최초 등록
 *
 * </pre>
 ******************************************************************/
public class TrxMessage implements Cloneable {

	/**
	 * 거래ID
	 */
	private Trx trx;

	/**
	 * 기관ID
	 */
	private Org org;

	/**
	 * 기동수동구분
	 */
	private String ioType;

	/**
	 * 전문ID
	 */
	private String messageId;

	/**
	 * 표준전문ID
	 */
	private String stdMessageId;

	/**
	 * 응답전문ID
	 */
	private String resMessageId;

	/**
	 * 표준응답전문ID
	 */
	private String stdResMessageId;

	/**
	 * 대응답여부
	 */
	private String proxyResYn;

	/**
	 * 대응답데이타
	 */
	private Object proxyResData;

	/**
	 * 대응답데이타 타입
	 */
	private String proxyResType;

	/**
	 * 헥사로그 기록 여부
	 */
	private String hexLogYn;

	/**
	 * 다중 응답 전문 구분 필드 ID
	 */
	private String ResTypeFieldId;

	/**
	 * 응답메시지 타입
	 */
	private String multiResType;

	/**
	 * 응답메시지 타입 - 1개
	 */
	public static final String MULTI_RES_TYPE_ONE = "1";

	/**
	 * 응답메시지 타입 - 0개
	 */
	public static final String MULTI_RES_TYPE_ZERO = "0";

	/**
	 * 응답메시지 타입 - 여러개
	 */
	public static final String MULTI_RES_TYPE_MANY = "M";

	public String getProxyResType() {
		return proxyResType;
	}

	public void setProxyResType(String proxyResType) {
		this.proxyResType = proxyResType;
	}

	public String getMultiResType() {
		return multiResType;
	}

	public void setMultiResType(String multiResType) {
		this.multiResType = multiResType;
	}

	public TrxMessage(){
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
	 * @return Returns the messageId.
	 */
	public String getMessageId() {
		return messageId;
	}

	/**
	 * @param messageId The messageId to set.
	 */
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	/**
	 * @return Returns the org.
	 */
	public Org getOrg() {
		return org;
	}

	/**
	 * @param org The org to set.
	 */
	public void setOrg(Org org) {
		this.org = org;
	}

	/**
	 * @return Returns the proxyResData.
	 */
	public Object getProxyResData() {
		return proxyResData;
	}

	/**
	 * @param proxyResData The proxyResData to set.
	 */
	public void setProxyResData(Object proxyResData) {
		this.proxyResData = proxyResData;
	}

	/**
	 * @return Returns the proxyResYn.
	 */
	public String getProxyResYn() {
		return proxyResYn;
	}

	/**
	 * @param proxyResYn The proxyResYn to set.
	 */
	public void setProxyResYn(String proxyResYn) {
		this.proxyResYn = proxyResYn;
	}

	/**
	 * @return Returns the resMessageId.
	 */
	public String getResMessageId() {
		return resMessageId;
	}

	/**
	 * @param resMessageId The resMessageId to set.
	 */
	public void setResMessageId(String resMessageId) {
		this.resMessageId = resMessageId;
	}

	/**
	 * @return Returns the stdMessageId.
	 */
	public String getStdMessageId() {
		return stdMessageId;
	}

	/**
	 * @param stdMessageId The stdMessageId to set.
	 */
	public void setStdMessageId(String stdMessageId) {
		this.stdMessageId = stdMessageId;
	}

	/**
	 * @return Returns the stdResMessageId.
	 */
	public String getStdResMessageId() {
		return stdResMessageId;
	}

	/**
	 * @param stdResMessageId The stdResMessageId to set.
	 */
	public void setStdResMessageId(String stdResMessageId) {
		this.stdResMessageId = stdResMessageId;
	}

	/**
	 * @return Returns the trx.
	 */
	public Trx getTrx() {
		return trx;
	}

	/**
	 * @param trx The trx to set.
	 */
	public void setTrx(Trx trx) {
		this.trx = trx;
	}

	public String getHexLogYn() {
		return hexLogYn;
	}

	public void setHexLogYn(String hexLogYn) {
		this.hexLogYn = hexLogYn;
	}


	public String getResTypeFieldId() {
		return ResTypeFieldId;
	}

	public void setResTypeFieldId(String resTypeFieldId) {
		ResTypeFieldId = resTypeFieldId;
	}

	/**
	 * 응답 메시지가 없는 타입인지 리턴한다.
	 * @return 응답 메시지가 없으면 true, 있으면 false를 리턴한다.
	 */
	public boolean isResMessageNone(){
		return MULTI_RES_TYPE_ZERO.equals(this.getMultiResType());
	}

	/**
	 * 응답 메시지가 여러개(2개 이상) 있는 타입인지 리턴한다.
	 * @return 응답 메시지가 여러개 있으면 true, 아니면 false를 리턴한다.
	 */
	public boolean isMultiResMessage(){
		return MULTI_RES_TYPE_MANY.equals(this.getMultiResType());
	}

	/**
	 * 다중 응답 전문인 경우 멀티Thread의 이슈를 피하기 위해 추가. 20081030
	 */
	public Object clone(){
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			throw new SysException("FRM30009","다중응답 전문 처리를 위하여 clone중 오류입니다."
					+"\n 거래가 처리 되었을 수 있으니 반드시 확인 바랍니다.");
		}
	}

}
