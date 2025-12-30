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
 * 요구(Request)인지 응답(Response)를 구분하는 Type을 나타내는 클래스
 * 
 * 요구: Q(Request) ,응답  : S(Response)
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
 * $Log: ReqResType.java,v $
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
 * Revision 1.1  2006/09/16 04:32:52  이종원
 * 이동
 *
 * Revision 1.1  2006/09/15 10:01:26  이종원
 * 최초작성
 *
 * </pre>
 ******************************************************************/
public class ReqResType implements Serializable {
	
	public static final String REQUEST_TYPE = "Q";
	public static final String RESPONSE_TYPE = "S";
	
	private String type;
	
	public ReqResType(String type) {
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
		buffer.append("요구(Q),응답(S)구분:"+this.getClass().getName()).append("@{");
		buffer.append("type:[").append(this.type).append("]");
		buffer.append("}");
		return buffer.toString();
	}//end of toString()
	
	public static final ReqResType REQUEST 
        = new ReqResType(REQUEST_TYPE);
	public static final ReqResType RESPONSE 
        = new ReqResType(RESPONSE_TYPE);
 	
	/**
	 * 주어진 문자열 타입에 해당하는  Type 객체를 리턴합니다.
	 * 
	 * @param type 문자열 형태의 I/O
	 * @return AdapterListenerType 객체
	 */
	public static ReqResType getType(String type) {
		if (REQUEST.getType().equals(type)) {
			return REQUEST;
		} else if (RESPONSE.getType().equals(type)) {
			return RESPONSE;
		} else {
			throw new IllegalArgumentException(
                    "지원되지 않는Request Response Type 입니다.[" + type + "]");
		}//end of if else
	}//end of getType()

}// end 