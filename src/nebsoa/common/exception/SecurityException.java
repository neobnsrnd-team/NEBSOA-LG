/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.exception;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 보안상 이슈가 있음을 나타내는 exception 클래스
 * 
 * 
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
 * $Log: SecurityException.java,v $
 * Revision 1.1  2018/01/15 03:39:50  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:40  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:24  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:27  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:50  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:18  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:04  안경아
 * *** empty log message ***
 *
 * Revision 1.3  2006/08/17 12:08:21  김성균
 * NestedException 처리변경
 *
 * Revision 1.2  2006/07/28 05:36:04  이종원
 * ExceptionTracer적용
 *
 * Revision 1.1  2006/07/14 04:20:10  이종원
 * 최초 작성
 *
 *
 * </pre>
 ******************************************************************/ 
public class SecurityException extends UserException{
	
	/**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -2915149796533100700L;

    public SecurityException(String errorCode,String msg){
		super(errorCode,msg);
	}

	public SecurityException(){
		super("FRX00005","요청하신 페이지는 보안상 서비스 불가 합니다.");
	}

}