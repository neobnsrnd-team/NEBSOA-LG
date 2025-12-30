/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.service.exception;

import nebsoa.common.exception.SysException;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 서비스 수행 중 서비스엔진에서 발생하는 예외상황에 대한 Exception 클래스 입니다.
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
 * $Log: ServiceSysException.java,v $
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
 * Revision 1.1  2008/01/22 05:58:28  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/12/31 04:35:34  최수종
 * nebsoa.service 패키지 추가
 *
 * Revision 1.2  2007/12/27 03:29:07  최수종
 * *** empty log message ***
 *
 * Revision 1.1  2007/12/24 02:09:15  김성균
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class ServiceSysException extends SysException {
    
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 6511573453022397998L;

    /**
     * @param errorCode
     */
    public ServiceSysException(String errorCode) {
        super(errorCode, "ServiceSysException");
    }
    
    /**
     * @param errorCode
     * @param msg
     */
    public ServiceSysException(String errorCode, String msg) {
        super(errorCode, msg);
    }
    
    /**
     * @param ex
     */
    public ServiceSysException(Throwable ex) {
        super(ex);
    }
    
    public ServiceSysException(String errorCode, Throwable e) {
        super(errorCode, e);
    }    
}
