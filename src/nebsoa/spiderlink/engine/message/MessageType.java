/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.engine.message;

import java.io.Serializable;

/*******************************************************************
 * <pre>
 * 1.설명 
 * message 의 타입을 나타내는 type-safed-enumeration 클래스
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
 * $Log: MessageType.java,v $
 * Revision 1.1  2018/01/15 03:39:48  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:16  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:50  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:22  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:26  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:51  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:20  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:05  안경아
 * *** empty log message ***
 *
 * Revision 1.7  2007/02/14 11:07:44  김승희
 * HTML 타입 추가
 *
 * Revision 1.6  2007/01/22 08:39:01  김승희
 * Message Type 추가
 *
 * Revision 1.5  2006/08/28 06:39:27  김성균
 * CSV 추가
 *
 * Revision 1.4  2006/06/17 10:12:09  김승희
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public class MessageType implements Serializable {
	
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -8509917429286953812L;
	
	/* 
	 * ksh 주석 처리
	public static final String PLAIN_TEXT_STRING = "PLAIN_TEXT";
	public static final String XML_STRING = "XML";
	public static final String OBJECT_STRING = "OBJECT";
	public static final String MAP_STRING = "MAP";
    public static final String SWIFT_STRING = "SWIFT";
    */
    
	//ksh가 수정. 
    public static final String FIXED_LENGTH_STRING = "F";
	public static final String XML_STRING = "X";
	public static final String OBJECT_STRING = "O";
	public static final String MAP_STRING = "M";
    public static final String SWIFT_STRING = "S";
    public static final String CSV_STRING = "C";
    public static final String PARAM_STRING = "P";
    public static final String DELEMITER_STRING = "D";
    public static final String HTML_STRING = "H";
    
	private String type;
	
	protected MessageType(String type) {
		this.type = type;
	}//end of constructor
	
	/**
	 * type 의 값을 리턴합니다.
	 * 
	 * @return type 의 값
	 */
	public String getType() {
		return type;
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(this.getClass().getName()).append("@{");
		buffer.append("type:[").append(this.type).append("]");
		buffer.append("}");
		return buffer.toString();
	}//end of toString()
	
	public static final MessageType FIXED_LENGTH = new MessageType(FIXED_LENGTH_STRING);
	public static final MessageType XML = new MessageType(XML_STRING);
	public static final MessageType OBJECT = new MessageType(OBJECT_STRING);
	public static final MessageType MAP = new MessageType(MAP_STRING);
	public static final MessageType CSV = new MessageType(CSV_STRING);
	public static final MessageType PARAM = new MessageType(PARAM_STRING);
	public static final MessageType DELEMITER = new MessageType(DELEMITER_STRING);
	public static final MessageType HTML = new MessageType(HTML_STRING);
	
	/**
	 * 주어진 문자열 타입에 해당하는 MessageType 객체를 리턴합니다.
	 * 
	 * 주어진 문자열 타입을 지원하지 않는다면, 기본으로 PLAIN_TEXT 를 리턴합니다.
	 * 
	 * @param type 문자열 형태의 message-type
	 * @return MessageType 객체
	 */
	public static MessageType getType(String type) {
		if (FIXED_LENGTH.getType().equals(type)) {
			return FIXED_LENGTH;
		} else if (XML.getType().equals(type)) {
			return XML;
		} else if (OBJECT.getType().equals(type)) {
			return OBJECT;
		} else if (MAP.getType().equals(type)) {
			return MAP;
		} else if (CSV.getType().equals(type)) {
			return CSV;
		} else if (PARAM.getType().equals(type)) {
			return PARAM;
		} else if (DELEMITER.getType().equals(type)) {
			return DELEMITER;
		} else if (HTML.getType().equals(type)) {
			return HTML;
		}else {
			return FIXED_LENGTH;
		}//end of if else
	}//end of getType()

}// end of MessageType.java