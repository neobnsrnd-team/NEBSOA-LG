/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
 
package nebsoa.common.exception;

import javax.servlet.ServletRequest;

import nebsoa.common.log.LogManager;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 필수 입력 정보가 입력되지 않았음을 알릴 때 사용하는 exception 클래스
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
 * $Log: ParamException.java,v $
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
 * Revision 1.7  2006/11/03 13:09:55  김성균
 * *** empty log message ***
 *
 * Revision 1.6  2006/08/17 12:08:21  김성균
 * NestedException 처리변경
 *
 * Revision 1.5  2006/07/28 05:36:04  이종원
 * ExceptionTracer적용
 *
 * Revision 1.4  2006/07/05 17:30:57  이종원
 * 상위 변경 ParamException-> RuntimeException으로 수정
 *
 * Revision 1.3  2006/07/05 17:05:22  이종원
 * 오류코드 정의
 *
 * Revision 1.2  2006/06/19 04:10:16  안경아
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class ParamException extends SysException {
	
	/**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -8745595578912453439L;
    
    /**
	* 필수 입력 정보가 입력되지 않았음을 알릴 때 사용하는 에러.
	*/
	public ParamException(String paramName){
		super("입력 필드명 : "+paramName);
	}
    
	/**
	* 필수 입력 정보가 입력되지 않았음을 알릴 때 사용하는 에러.
	* 디버깅을 쉽게 하도록 request를 넘겨주면 도스 화면에 모든 request정보를 출력한다
	*/
	public ParamException(String paramName,ServletRequest request){
		super("입력 필드명 : "+paramName);
		LogManager.debug(request);
	}
    
    public String getErrorCode() {
        return "FRM00001";
    }

}