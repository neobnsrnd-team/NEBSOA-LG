/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.util.async;

import nebsoa.common.exception.NestedCheckedException;
import nebsoa.common.exception.SpiderException;

/*******************************************************************
 * <pre>
 * 1.설명 
 * Async Queue와 관련 된  exception 클래스
 * 
 * 
 * 
 * 2.사용법
 * 
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author 이종원
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 
 * $Log: AsyncQueueTimeoutException.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:24  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:26  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:29  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:54  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/02/26 01:40:53  이종원
 * 최초작성
 *
 * </pre>
 ******************************************************************/
public class AsyncQueueTimeoutException extends NestedCheckedException implements SpiderException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String errorCode="FRS99004";
	
	public AsyncQueueTimeoutException(){
		super("AsyncQueue Exception");
	}
	

	public AsyncQueueTimeoutException(String msg){
		super(msg);
	}
	


    public AsyncQueueTimeoutException(String errorCode, String message) {
        super(message);
        if(errorCode != null)
        this.errorCode = errorCode;
    }
    
    public AsyncQueueTimeoutException(String message, Exception e) {
        super(message,e);
    }
    
    public AsyncQueueTimeoutException( Exception e) {
        super(e);
    }
    
    public AsyncQueueTimeoutException(String errorCode,String message, Exception e) {
        super(message,e);
        if(errorCode != null)
            this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
