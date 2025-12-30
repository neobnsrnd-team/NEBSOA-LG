/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.engine.adapter;

import java.io.IOException;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 프로토콜 핸들러 인터페이스 
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
 * $Log: ProtocolHandler.java,v $
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
 * Revision 1.1  2006/08/01 14:07:09  이종원
 * *** empty log message ***
 *
 * Revision 1.3  2006/06/17 09:15:10  김승희
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public interface ProtocolHandler {
	
	/**
	 * 요청에 대해서 처리합니다.
	 *
	 */
	public void process();
	
	/**
	 * 요청에 대해서 처리하고 결과를 리턴합니다.
	 * 
	 * @return 요청 처리 결과가 담긴 Message 객체
	 * @throws IOException 요청 처리 중 발생하는 Exception
	 */
	public Object handleRequest() throws IOException;
	
	/**
	 * 응답에 대해서 처리합니다.
	 * 
	 * @param resMsg 요청 처리 결과가 담긴 Message 객체
	 * @throws IOException 응답 처리 중 발생하는 Exception
	 */
	public void handleResponse(Object resMsg) throws IOException;

}// end of ProtocolHandler.java