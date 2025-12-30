/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

import nebsoa.common.Context;
import nebsoa.common.util.ExceptionTracer;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 프로그램상의 exception(System Exception)을 대표하는 exception 클래스
 * 
 * 
 * 
 * 2.사용법
 * RuntimeException을 상속받았으므로 throws선언 없이 사용가능합니다.
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
 * $Log: SysException.java,v $
 * Revision 1.1  2018/01/15 03:39:50  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:41  cvs
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
 * Revision 1.18  2007/01/30 02:27:16  김성균
 * 생성자 정리
 *
 * Revision 1.17  2007/01/12 06:47:02  김성균
 * Context 정보가 존재하면 메시지 출력하도록 수정
 *
 * Revision 1.16  2007/01/04 09:36:45  이종원
 *  생성자 추가
 *
 * Revision 1.15  2006/12/11 02:53:14  김성균
 * *** empty log message ***
 *
 * Revision 1.14  2006/11/27 05:01:29  김승희
 * private-->protected 로 변경
 *
 * Revision 1.13  2006/08/17 12:08:21  김성균
 * NestedException 처리변경
 *
 * Revision 1.12  2006/08/07 05:00:49  김성균
 * *** empty log message ***
 *
 * Revision 1.11  2006/08/01 14:05:08  이종원
 * *** empty log message ***
 *
 * Revision 1.10  2006/08/01 00:30:35  김승희
 * 생성자 추가
 *
 * Revision 1.9  2006/07/28 09:30:11  김승희
 * exception 처리 변경
 *
 * Revision 1.8  2006/07/28 05:45:08  이종원
 * *** empty log message ***
 *
 * Revision 1.7  2006/07/28 05:36:04  이종원
 * ExceptionTracer적용
 *
 * Revision 1.6  2006/07/05 17:05:22  이종원
 * 오류코드 정의
 *
 * Revision 1.5  2006/06/19 04:10:16  안경아
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class SysException extends RuntimeException implements SpiderException {
    
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 8772062669952148175L;
    
    /** Root cause of this nested exception */
    protected Throwable cause;
    
    /**
     * Comment for <code>errorCode</code>
     */
    protected String errorCode;
    
    /**
     * 디폴트 생성자이며, 오류코드는 FRS99999로 초기화 한다. 
     */
    public SysException() {
        this("FRS99999", "시스템 장애 입니다.");
    }

    /**
     * @param message
     */
    public SysException(String message) {
        this("FRS99999", message);
    }

    /**
     * @param errorCode
     * @param message
     */
    public SysException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
    
    /**
     * @param ctx
     * @param message
     */
    public SysException(Context ctx, String message) {
        this(ctx, "FRS99999", message);
    }
    
    /**
     * @param ctx
     * @param errorCode
     * @param message
     */
    public SysException(Context ctx, String errorCode, String message) {
        super(message + (ctx == null ? "" : "(UserInfo:" + ctx.toString() + ")"));
        this.errorCode = errorCode;
    }
    
    /**
     * @param e
     */
    public SysException(Throwable e) {
        this(null, "FRS99999", null, e);
    }

    /**
     * @param errorCode
     * @param e
     */
    public SysException(String errorCode, Throwable e) {
        this(null, errorCode, null, e);
    }
    
    /**
     * @param errorCode
     * @param message
     * @param e
     */
    public SysException(String errorCode, String message, Throwable e) {
        this(null, errorCode, message, e);
    }
    
    /**
     * @param ctx
     * @param e
     */
    public SysException(Context ctx, Throwable e) {
        this(ctx, "FRS99999", null, e);
    }
    
    /**
     * @param ctx
     * @param msg
     * @param e
     */
    public SysException(Context ctx, String message, Throwable e) {
        this(ctx, "FRS99999", message, e);
    }
    
    /**
     * @param ctx
     * @param errorCode
     * @param message
     * @param e
     */
    public SysException(Context ctx, String errorCode, String message, Throwable e) {
        super(message == null ? e.toString() : message + "::" + e.toString() + ((ctx == null) ? "" : "(UserInfo:" + ctx.toString() + ")"));
        this.errorCode = errorCode;
        cause = e;
    }
    
    /**
     * Return the nested cause, or null if none.
     */
    public Throwable getCause() {
        return (this.cause == this ? null : this.cause);
    }
    
    /* (non-Javadoc)
     * @see nebsoa.common.exception.SpiderException#getErrorCode()
     */
    public String getErrorCode() {
        return errorCode;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Throwable#getMessage()
     */
    public String getMessage(){
        return super.getMessage();
    }
    
    /* (non-Javadoc)
     * @see nebsoa.common.exception.SpiderException#getTrace()
     */
    public String getTrace() {
        if (cause != null) {
            return ExceptionTracer.getTraceString(cause);
        } else {
            return ExceptionTracer.getTraceString(this);
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Throwable#printStackTrace()
     */
    public void printStackTrace() {
        if (cause != null) {
            System.err.println(getTrace());
        } else {
            super.printStackTrace();
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Throwable#printStackTrace(java.io.PrintWriter)
     */
    public void printStackTrace(PrintWriter pw) {
        if (cause != null) {
            pw.println(getTrace());
        } else {
            super.printStackTrace(pw);
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Throwable#printStackTrace(java.io.PrintStream)
     */
    public void printStackTrace(PrintStream pw) {
        if (cause != null) {
            pw.println(getTrace());
        } else {
            super.printStackTrace(pw);
        }
    }
}