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
 * 전문 처리 핸들러 정보를 담고 있는 클래스
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
 * $Log: MessageHandler.java,v $
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
 * Revision 1.1  2008/08/04 08:54:52  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:25  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:08  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2006/08/25 01:24:54  김승희
 * 시스템 관련 테이블 변경에 따른 수정
 *
 *
 * </pre>
 ******************************************************************/
public class MessageHandler implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -6813938792126995359L;

	private String orgId;
	
	private String trxType;
	
	private String ioType;
	
	private String operModeType;
	
	private String messageHandler;
	
	private String stopYn;
	
	public MessageHandler(){}
	
	public String getStopYn() {
		return stopYn;
	}

	public void setStopYn(String status) {
		this.stopYn = status;
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
	 * @return Returns the trxType.
	 */
	public String getTrxType() {
		return trxType;
	}
	
	/**
	 * @return trxType The trxType to set.
	 */
	public void setTrxType(String trxType) {
		this.trxType = trxType;
	}

	public String getMessageHandler() {
		return messageHandler;
	}

	public void setMessageHandler(String messageHandler) {
		this.messageHandler = messageHandler;
	}

	public String getOperModeType() {
		return operModeType;
	}

	public void setOperModeType(String operModeType) {
		this.operModeType = operModeType;
	}
	
}
