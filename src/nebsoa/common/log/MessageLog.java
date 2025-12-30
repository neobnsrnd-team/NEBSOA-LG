/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.log;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 메시지 로그 정보를 가지고 있습니다. 
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
 * $Log: MessageLog.java,v $
 * Revision 1.1  2018/01/15 03:39:48  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:19  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:50  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:23  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:26  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:51  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:19  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:39:07  안경아
 * *** empty log message ***
 *
 * Revision 1.4  2006/06/21 04:33:10  김성균
 * 최초 등록
 *
 * </pre>
 ******************************************************************/
public class MessageLog {
	
	private String messageTrxId;
	
	private String sendReceiveClass;
	
	private String messageId;
	
	private String messageTrxDealingDtime;
	
	private String messageTrxData;
	
	private String orgId;
	
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
	 * @return Returns the messageTrxData.
	 */
	public String getMessageTrxData() {
		return messageTrxData;
	}

	/**
	 * @param messageTrxData The messageTrxData to set.
	 */
	public void setMessageTrxData(String messageTrxData) {
		this.messageTrxData = messageTrxData;
	}

	/**
	 * @return Returns the messageTrxDealingDtime.
	 */
	public String getMessageTrxDealingDtime() {
		return messageTrxDealingDtime;
	}

	/**
	 * @param messageTrxDealingDtime The messageTrxDealingDtime to set.
	 */
	public void setMessageTrxDealingDtime(String messageTrxDealingDtime) {
		this.messageTrxDealingDtime = messageTrxDealingDtime;
	}

	/**
	 * @return Returns the messageTrxId.
	 */
	public String getMessageTrxId() {
		return messageTrxId;
	}

	/**
	 * @param messageTrxId The messageTrxId to set.
	 */
	public void setMessageTrxId(String messageTrxId) {
		this.messageTrxId = messageTrxId;
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
	 * @return Returns the sendReceiveClass.
	 */
	public String getSendReceiveClass() {
		return sendReceiveClass;
	}

	/**
	 * @param sendReceiveClass The sendReceiveClass to set.
	 */
	public void setSendReceiveClass(String sendReceiveClass) {
		this.sendReceiveClass = sendReceiveClass;
	}

	/**
	 * 로그정보를 입력하기 위해서 배열 자료형으로 변환
	 * 
	 * @return Object[]
	 */
	public Object[] toArray() {
		Object[] arr = {
				messageTrxId,
				sendReceiveClass,
				orgId,
				messageId,
				messageTrxDealingDtime,
				messageTrxData
		};
		return arr;
	}

}
