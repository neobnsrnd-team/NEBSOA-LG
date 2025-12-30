/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.mail;

import java.util.List;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 메일를 전송하기 위한 정보를 담고 있는 클래스
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
 * $Log: MailInfo.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:28  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:26  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:29  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:34  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:49  안경아
 * *** empty log message ***
 *
 * Revision 1.3  2007/06/20 10:41:46  최수종
 * 오류 핸들러 관련 수정 및 추가
 *
 * Revision 1.1  2007/06/07 05:05:08  홍윤석
 * 수정
 *
 * Revision 1.2  2006/06/17 08:18:35  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class MailInfo {
	/**
	 * Comment for <code>senderEmail</code>
	 */
	private String senderEmail = null;

	/**
	 * Comment for <code>senderName</code>
	 */
	private String senderName = null;

	/**
	 * Comment for <code>receiverEmail</code>
	 */
	private List receiverEmail = null;

	/**
	 * Comment for <code>receiverName</code>
	 */
	private List receiverName = null;

	/**
	 * Comment for <code>subject</code>
	 */
	private String subject = null;

	/**
	 * Comment for <code>content</code>
	 */
	private String content = null;

	public MailInfo() {};
	
	public MailInfo(String sEmail, String sName, List rEmail,
			List rName, String sj, String ct) {
		this.senderEmail = sEmail;
		this.senderName = sName;
		this.receiverEmail = rEmail;
		this.receiverName = rName;
		this.subject = sj;
		this.content = ct;
	}

	public String getContent() {
		return content;
	}

	public String getSubject() {
		return subject;
	}

	public List getReceiverEmail() {
		return receiverEmail;
	}

	public List getReceiverName() {
		return receiverName;
	}

	public String getSenderEmail() {
		return senderEmail;
	}

	public String getSenderName() {
		return senderName;
	}
	
	/**
	 * @param content The content to set.
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @param receiverEmail The receiverEmail to set.
	 */
	public void setReceiverEmail(List receiverEmail) {
		this.receiverEmail = receiverEmail;
	}

	/**
	 * @param receiverName The receiverName to set.
	 */
	public void setReceiverName(List receiverName) {
		this.receiverName = receiverName;
	}

	/**
	 * @param senderEmail The senderEmail to set.
	 */
	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}

	/**
	 * @param senderName The senderName to set.
	 */
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	/**
	 * @param subject The subject to set.
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
}