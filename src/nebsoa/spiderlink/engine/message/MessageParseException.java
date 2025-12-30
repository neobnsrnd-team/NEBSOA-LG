/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.engine.message;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 메시지 파싱(marshall/unmarshall) 중 발생하는 Exception
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
 * $Log: MessageParseException.java,v $
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
 * Revision 1.1  2007/11/26 08:38:04  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2006/06/17 10:12:09  김승희
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public class MessageParseException extends RuntimeException {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 8650122652976157330L;
	
	/**
	 * 기본 메시지로 초기화 작업을 수행하는 생성자
	 */
	public MessageParseException() {
		this("Message Parsing  중 오류가 발생하였습니다.");
	}//end of constructor

	/**
	 * 오류 메시지를 세팅하는 생성자
	 * 
	 * @param msg 오류 메시지
	 */
	public MessageParseException(String msg) {
		super(msg);
	}//end of constructor

	/**
	 * 오류를 세팅하는 생성자
	 * 
	 * @param throwable Throwable 객체
	 */
	public MessageParseException(Throwable throwable) {
		super(throwable);
	}//end of constructor

	/**
	 * 오류 메시지와 오류를 세팅하는 생성자
	 * 
	 * @param msg 오류 메시지
	 * @param throwable Throwable 객체
	 */
	public MessageParseException(String msg, Throwable throwable) {
		super(msg, throwable);
	}//end of constructor

}// end of MessageParseException.java