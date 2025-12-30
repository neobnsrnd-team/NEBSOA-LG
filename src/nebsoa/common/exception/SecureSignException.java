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
 * XecureSign 로직 수행 중 발생하는 예외상황에 대한 Exception 클래스 입니다.
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
 * $Log: SecureSignException.java,v $
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
 * Revision 1.2  2007/01/09 11:43:32  김성균
 * serialVersionUID 추가
 *
 * Revision 1.1  2007/01/09 11:40:57  김성균
 * SecureSignException 패키지 변경
 *
 * Revision 1.1  2007/01/09 04:39:56  이종원
 * 최초작성
 *
 * </pre>
 ******************************************************************/
public class SecureSignException extends UserException {
    
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 4208061844471719592L;

    /**
     * @param errorCode
     */
    public SecureSignException(String errorCode) {
        super(errorCode, "전자서명오류");
    }
    
    /**
     * @param errorCode
     * @param msg
     */
    public SecureSignException(String errorCode, String msg) {
        super(errorCode, msg);
    }
    
    /**
     * @param ex
     */
    public SecureSignException(Throwable ex) {
        super(ex);
    }
}
