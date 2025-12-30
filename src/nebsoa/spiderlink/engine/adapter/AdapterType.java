/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.engine.adapter;

import java.io.Serializable;

/*******************************************************************
 * <pre>
 * 1.설명 
 * Adapter 의 타입을 나타내는 type-safed-enumeration 클래스
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
 * $Log: AdapterType.java,v $
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
 * Revision 1.2  2006/06/17 09:15:36  김승희
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public class AdapterType implements Serializable {
	
	public static final String INPUT_TCP_SERVER_STRING = "INPUT_TCP_SERVER";
	public static final String INPUT_TCP_CLIENT_STRING = "INPUT_TCP_CLIENT";
	public static final String INPUT_FILE_STRING = "INPUT_FILE";
	public static final String INPUT_JMS_STRING = "INPUT_JMS";
	public static final String OUTPUT_JMS_STRING = "OUTPUT_JMS";
	
	public static final String OUTPUT_TCP_CLIENT_STRING = "OUTPUT_TCP_CLIENT";
	
	private String type;
	
	private AdapterType(String type) {
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
	
	public static final AdapterType INPUT_TCP_SERVER = new AdapterType(INPUT_TCP_SERVER_STRING);
	public static final AdapterType INPUT_TCP_CLIENT = new AdapterType(INPUT_TCP_CLIENT_STRING);
	public static final AdapterType INPUT_FILE = new AdapterType(INPUT_FILE_STRING);
	public static final AdapterType INPUT_JMS = new AdapterType(INPUT_JMS_STRING);
	public static final AdapterType OUTPUT_JMS = new AdapterType(OUTPUT_JMS_STRING);
	
	public static final AdapterType OUTPUT_TCP_CLIENT = new AdapterType(OUTPUT_TCP_CLIENT_STRING);
	
	/**
	 * 주어진 문자열 타입에 해당하는 AdapterType 객체를 리턴합니다.
	 * 
	 * 주어진 문자열 타입을 지원하지 않는다면, 기본으로 PLAIN_TEXT 를 리턴합니다.
	 * 
	 * @param type 문자열 형태의 Adapter-type
	 * @return AdapterType 객체
	 */
	public static AdapterType getType(String type) {
		if (INPUT_TCP_SERVER.getType().equals(type)) {
			return INPUT_TCP_SERVER;
		} else if (INPUT_TCP_CLIENT.getType().equals(type)) {
			return INPUT_TCP_CLIENT;
		} else if (INPUT_FILE.getType().equals(type)) {
			return INPUT_FILE;
		} else if (INPUT_JMS.getType().equals(type)) {
			return INPUT_JMS;
		} else if (OUTPUT_JMS.getType().equals(type)) {
			return OUTPUT_JMS;
		} else if (OUTPUT_TCP_CLIENT.getType().equals(type)) {
			return OUTPUT_TCP_CLIENT;
		} else {
			throw new IllegalArgumentException("지원되지 않는 Adapter-Type 입니다.[" + type + "]");
		}//end of if else
	}//end of getType()

}// end of AdapterType.java