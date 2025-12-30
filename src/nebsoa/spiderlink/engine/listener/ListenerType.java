/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.engine.listener;

import java.io.Serializable;

/*******************************************************************
 * <pre>
 * 1.설명 
 * listener의 타입을 상수로 정의하여 표현한 클래스
 * 현재 TCP, JMS, FILE, UDP등이 등록되어 있으며 추가 되는 것이 있으면 반드시 추가하여
 * 사용한다.
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
 * $Log: ListenerType.java,v $
 * Revision 1.1  2018/01/15 03:39:54  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:38  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:27  김성균
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
 * Revision 1.1  2007/11/26 08:39:11  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2006/07/31 10:47:37  이종원
 * 최초작성
 *
 * </pre>
 ******************************************************************/
public class ListenerType implements Serializable {
	
	public static final String TYPE_TCP = "TCP";
	public static final String TYPE_JMS = "JMS";
	public static final String TYPE_UDP = "UDP";
	public static final String TYPE_FILE = "FILE";
	
	private String type;
	
	private ListenerType(String type) {
		this.type = type;
	}//end of constructor
	
	/**
	 * type 의 값을 리턴합니다.
	 * 
	 * @return type 의 값
	 */
	public String getType() {
		return this.type;
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(this.getClass().getName()).append("@{");
		buffer.append("type:[").append(this.type).append("]");
		buffer.append("}");
		return buffer.toString();
	}//end of toString()
	
	public static final ListenerType LISTENER_TCP= new ListenerType(TYPE_TCP);
    public static final ListenerType LISTENER_JMS= new ListenerType(TYPE_JMS);
    public static final ListenerType LISTENER_UDP= new ListenerType(TYPE_UDP);
    public static final ListenerType LISTENER_FILE= new ListenerType(TYPE_FILE);
	
	/**
	 * 주어진 문자열 타입에 해당하는 AdapterType 객체를 리턴합니다.
	 * 
	 * 주어진 문자열 타입을 지원하지 않는다면, 기본으로 PLAIN_TEXT 를 리턴합니다.
	 * 
	 * @param type 문자열 형태의 Adapter-type
	 * @return AdapterType 객체
	 */
	public static ListenerType getType(String type) {
		if (LISTENER_TCP.getType().equals(type)) {
			return LISTENER_TCP;
		} else if (LISTENER_JMS.getType().equals(type)) {
			return LISTENER_JMS;
		} else if (LISTENER_UDP.getType().equals(type)) {
			return LISTENER_UDP;
		} else if (LISTENER_FILE.getType().equals(type)) {
			return LISTENER_FILE;
		} else {
			throw new IllegalArgumentException("지원되지 않는 listener Type 입니다.[" + type + "]");
		}//end of if else
	}//end of getType()

}// end of AdapterType.java