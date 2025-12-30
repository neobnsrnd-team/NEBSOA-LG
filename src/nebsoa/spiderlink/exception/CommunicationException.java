/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.exception;

import org.apache.commons.lang.exception.NestableRuntimeException;

import nebsoa.common.exception.SpiderException;
import nebsoa.common.util.ExceptionTracer;



/*******************************************************************
 * <pre>
 * 1.설명 
 * 메시지 송수신 중 발생한 Exception
 * 메시지 송신 실패, 메시지 수신 실패를 모두 포함한다.
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
 * $Log: CommunicationException.java,v $
 * Revision 1.1  2018/01/15 03:39:51  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:54  cvs
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
 * Revision 1.1  2008/08/04 08:54:52  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:26  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:39:01  안경아
 * *** empty log message ***
 *
 * Revision 1.3  2006/07/28 09:30:11  김승희
 * exception 처리 변경
 *
 * Revision 1.2  2006/07/06 08:40:21  김승희
 * exception 처리 관련 수정
 *
 * Revision 1.1  2006/07/03 12:36:15  김승희
 * *** empty log message ***
 *
 *
 * </pre>
 ******************************************************************/
public class CommunicationException extends NestableRuntimeException implements SpiderException{
	
	protected String errorCode;
	
    /**
     * 기본 생성자
     */
    public CommunicationException() {
        super();
    }

    public CommunicationException(String errorCode, String message){
    	super(message);
    	this.errorCode = errorCode;
    }
    /**
     * 세부 메시지를 갖는 생성자
     * @param message 세부 메시지
     */
    public CommunicationException(String message) {
        super(message);
    }

    /**
     * 세부 메시지와 원본 예외를 갖는 생성자
     * 
     * @param message 세부 메시지
     * @param cause 원본 예외
     */
    public CommunicationException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public CommunicationException(String errorCode, String message, Throwable cause){
    	super(message, cause);
    	this.errorCode = errorCode;
    }
    
    /**
     * 원본 예외를 갖는 생성자
     * @param cause 원본 예외
     */
    public CommunicationException(Throwable cause) {
        super(cause);
    }
    
    public String getErrorCode(){
    	return this.errorCode;
    }
    Throwable nestedException;
    
    public String getTrace() {
        if(nestedException != null){
            return ExceptionTracer.getTraceString(nestedException);
        }else{
            return ExceptionTracer.getTraceString(this);
        }
    }
    public void setNestedException(Throwable e) {
        nestedException=e;
    }
}
