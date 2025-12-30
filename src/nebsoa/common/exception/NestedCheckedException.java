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

import nebsoa.common.util.ExceptionTracer;


/*******************************************************************
 * <pre>
 * 1.설명 
 * Nested Exception을 처리하는  exception 클래스
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
 * $Log: NestedCheckedException.java,v $
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
 * Revision 1.4  2006/12/06 08:48:31  김성균
 * 주석수정
 *
 * Revision 1.3  2006/08/24 12:20:00  김성균
 * *** empty log message ***
 *
 * Revision 1.2  2006/08/17 12:11:43  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2006/08/17 12:08:21  김성균
 * NestedException 처리변경
 *
 * </pre>
 ******************************************************************/
public class NestedCheckedException extends Exception {
	
	/**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -8745595578912453439L;
    
    /** Root cause of this nested exception */
    private Throwable cause;
    
    public NestedCheckedException() {
    }
    
    /**
     * Construct a <code>NestedCheckedException</code> with the specified detail message.
     * @param msg the detail message
     */
    public NestedCheckedException(String msg) {
        super(msg);
    }
    
    /**
     * Construct a <code>NestedCheckedException</code> with the Throwable object 
     * and nested exception.
     * @param ex the nested exception
     */
    public NestedCheckedException(Throwable ex) {
        this.cause = ex;
    }

    /**
     * Construct a <code>NestedCheckedException</code> with the specified detail message
     * and nested exception.
     * @param msg the detail message
     * @param ex the nested exception
     */
    public NestedCheckedException(String msg, Throwable ex) {
        super(msg);
        this.cause = ex;
    }
    
    /**
     * Return the nested cause, or null if none.
     */
    public Throwable getCause() {
        return (this.cause == this ? null : this.cause);
    }
    
    public String getTrace() {
        if (cause != null) {
            return ExceptionTracer.getTraceString(cause);
        } else {
            return ExceptionTracer.getTraceString(this);
        }
    }
    
    public void printStackTrace() {
        if (cause != null) {
            System.err.println(getTrace());
        } else {
            super.printStackTrace();
        }
    }

    public void printStackTrace(PrintWriter pw) {
        if (cause != null) {
            pw.println(getTrace());
        } else {
            super.printStackTrace(pw);
        }
    }

    public void printStackTrace(PrintStream pw) {
        if (cause != null) {
            pw.println(getTrace());
        } else {
            super.printStackTrace(pw);
        }
    }

    /**
     * @param cause The cause to set.
     */
    public void setCause(Throwable cause) {
        this.cause = cause;
    }
}