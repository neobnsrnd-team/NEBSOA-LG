/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
package nebsoa.logserver;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 로그 조회시 발생하는 exception
 * 
 * 2.사용법
 *  *  
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author $Author: cvs $
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 
 * $Log: LogSearchException.java,v $
 * Revision 1.1  2018/01/15 03:39:51  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:59  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:24  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:28  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:52  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:27  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.2  2007/12/17 01:48:35  김승희
 * 로그 서버 서버 분리에 따른 수정
 *
 * Revision 1.1  2007/06/15 02:05:07  김승희
 * 프로젝트 변경 신규 커밋
 *
 * Revision 1.1  2007/06/13 09:42:15  shkim
 * 최초 등록
 *
 *
 * </pre>
 ******************************************************************/
public class LogSearchException extends RuntimeException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -4917218450443699536L;
		
	public LogSearchException(){}
	
	public LogSearchException(String errorMessage){
		super(errorMessage);
	}

}
