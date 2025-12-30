/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.connector.constants;

import java.io.Serializable;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 요청을 보내는 기동 인지 요청을 받는 수동인지를 구분하는 Type을 나타내는 클래스
 * 
 * 기동 : O ,수동 : I
 * 2.사용법
 * static 상수로 사용
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author 이종원
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 
 * $Log: IOType.java,v $
 * Revision 1.1  2018/01/15 03:39:52  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:09  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:25  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:28  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:51  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:19  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:25  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2006/10/03 09:12:44  이종원
 * refactoring
 *
 * Revision 1.1  2006/09/16 07:06:42  김승희
 * *** empty log message ***
 *
 * Revision 1.1  2006/09/16 04:32:52  이종원
 * 이동
 *
 * Revision 1.1  2006/09/15 09:52:40  이종원
 * CLASS이름 수정
 *
 * Revision 1.1  2006/09/15 07:51:12  이종원
 * 최초작성
 *
 * Revision 1.1  2006/09/15 07:44:28  이종원
 * 최초작성
 *
 *
 * </pre>
 ******************************************************************/
public class IOType implements Serializable {
	
	public static final String REQUEST_SENDER_TYPE = "O";
	public static final String REQUEST_RECEIVER_TYPE = "I";
	
	private String type;
	
	public IOType(String type) {
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
		buffer.append("기동(O),수동(I)구분:"+this.getClass().getName()).append("@{");
		buffer.append("type:[").append(this.type).append("]");
		buffer.append("}");
		return buffer.toString();
	}//end of toString()
	
	public static final IOType REQUEST_SENDER 
        = new IOType(REQUEST_SENDER_TYPE);
	public static final IOType REQUEST_RECEIVER 
        = new IOType(REQUEST_RECEIVER_TYPE);
 	
	/**
	 * 주어진 문자열 타입에 해당하는  Type 객체를 리턴합니다.
	 * 
	 * @param type 문자열 형태의 I/O
	 * @return AdapterListenerType 객체
	 */
	public static IOType getType(String type) {
		if (REQUEST_SENDER.getType().equals(type)) {
			return REQUEST_SENDER;
		} else if (REQUEST_RECEIVER.getType().equals(type)) {
			return REQUEST_RECEIVER;
		} else {
			throw new IllegalArgumentException(
                    "지원되지 않는Request IO Type 입니다.[" + type + "]");
		}//end of if else
	}//end of getType()

}// end 