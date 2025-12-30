/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.engine.adapter;

import nebsoa.spiderlink.connector.constants.SyncType;
import nebsoa.spiderlink.engine.message.MessageType;

/*******************************************************************
 * <pre>
 * 1.설명 
 * Adpater 정보를 담고 있는 context
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
 * $Log: AdapterContext.java,v $
 * Revision 1.1  2018/01/15 03:39:52  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:12  cvs
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
 * Revision 1.1  2008/08/04 08:54:52  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:24  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:39:08  안경아
 * *** empty log message ***
 *
 * Revision 1.4  2006/10/03 09:12:44  이종원
 * refactoring
 *
 * Revision 1.3  2006/06/17 10:11:43  김승희
 * 참조 클래스 패키지 변경에 따른 import 수정
 *
 * Revision 1.2  2006/06/17 09:15:36  김승희
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public class AdapterContext {
	
	private String orgId;
	private AdapterType adapterType;
	private MessageType messageType;
	private SyncType syncType;
	private String engineJndiName;
	
	/**
	 * adapterType 의 값을 리턴합니다.
	 * 
	 * @return adapterType 의 값
	 */
	public AdapterType getAdapterType() {
		return adapterType;
	}
	/**
	 * adapterType 에 값을 세팅합니다.
	 * 
	 * @param adapterType adapterType 에 값을 세팅하기 위한 인자 값
	 */
	public void setAdapterType(AdapterType adapterType) {
		this.adapterType = adapterType;
	}
	/**
	 * engineJndiName 의 값을 리턴합니다.
	 * 
	 * @return engineJndiName 의 값
	 */
	public String getEngineJndiName() {
		return engineJndiName;
	}
	/**
	 * engineJndiName 에 값을 세팅합니다.
	 * 
	 * @param engineJndiName engineJndiName 에 값을 세팅하기 위한 인자 값
	 */
	public void setEngineJndiName(String engineJndiName) {
		this.engineJndiName = engineJndiName;
	}
	/**
	 * messageType 의 값을 리턴합니다.
	 * 
	 * @return messageType 의 값
	 */
	public MessageType getMessageType() {
		return messageType;
	}
	/**
	 * messageType 에 값을 세팅합니다.
	 * 
	 * @param messageType messageType 에 값을 세팅하기 위한 인자 값
	 */
	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}
	/**
	 * orgId 의 값을 리턴합니다.
	 * 
	 * @return orgId 의 값
	 */
	public String getOrgId() {
		return orgId;
	}
	/**
	 * orgId 에 값을 세팅합니다.
	 * 
	 * @param orgId orgId 에 값을 세팅하기 위한 인자 값
	 */
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	/**
	 * syncType 의 값을 리턴합니다.
	 * 
	 * @return syncType 의 값
	 */
	public SyncType getSyncType() {
		return syncType;
	}
	/**
	 * syncType 에 값을 세팅합니다.
	 * 
	 * @param syncType syncType 에 값을 세팅하기 위한 인자 값
	 */
	public void setSyncType(SyncType syncType) {
		this.syncType = syncType;
	}

}// end of AdapterContext.java