/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.service.exception;

import nebsoa.biz.exception.BizException;
import nebsoa.common.exception.UserException;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 서비스 수행 중 발생하는 예외상황에 대한 Exception 클래스 입니다.
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
 * $Log: ServiceException.java,v $
 * Revision 1.1  2018/01/15 03:39:49  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:34  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:23  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:27  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.4  2008/03/03 05:57:08  김성균
 * NestedException이 BizException일 경우 처리 수정
 *
 * Revision 1.3  2008/03/03 05:18:46  김성균
 * 주석 추가
 *
 * Revision 1.2  2008/02/15 04:45:15  김성균
 * 서비스 수행 로그 메시지 정리
 *
 * Revision 1.1  2008/01/22 05:58:28  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/12/31 04:35:34  최수종
 * nebsoa.service 패키지 추가
 *
 * Revision 1.2  2007/12/21 05:15:55  김성균
 * BizException 상속 받도록 수정
 *
 * Revision 1.1  2007/12/18 03:16:00  김성균
 * 서비스관련 패키지 최초등록
 *
 * </pre>
 ******************************************************************/
public class ServiceException extends UserException {
    
	/**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 5462443013563679041L;

    /**
     * @param errorCode
     */
    public ServiceException(String errorCode) {
        super(errorCode, "ServiceException");
    }
    
    /**
     * @param errorCode
     * @param msg
     */
    public ServiceException(String errorCode, String msg) {
        super(errorCode, msg);
    }
    
    /**
     * @param ex
     */
    public ServiceException(Throwable ex) {
        super(ex);
    }
    
    /**
     * 서비스에서 발생한 예외정보를 얻어온다.
     * NestedException이 BizException일 경우 처리를 위해서 재정의 한다.
     */
    public Throwable getCause() {
    	Throwable cause = super.getCause();
    	if (cause instanceof BizException && cause.getCause() != null) {
    		cause = cause.getCause();
    	}
    	return cause;
    }
}
