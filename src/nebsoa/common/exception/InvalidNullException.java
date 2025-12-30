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
 * 입력 정보가 Null임을 알릴 때 사용하는 exception 클래스
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
 * $Log: InvalidNullException.java,v $
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
 * Revision 1.3  2007/01/31 02:29:32  김성균
 * 주석정리
 *
 * Revision 1.2  2007/01/12 10:39:40  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2007/01/12 09:29:49  안경아
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class InvalidNullException extends NestedCheckedException implements SpiderException {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 7370068875744003713L;

    /**
     * @param paramValue
     */
    public InvalidNullException(String paramValue) {
        super("Invalid Null Data:" + (paramValue == null ? "Null" : paramValue));
    }

    /**
     * @param paramName
     * @param paramValue
     */
    public InvalidNullException(String paramName, String paramValue) {
        super("[" + paramName + "] Invalid Null Data:"
                + (paramValue == null ? "Null" : paramValue));
    }

    /* (non-Javadoc)
     * @see nebsoa.common.exception.SpiderException#getErrorCode()
     */
    public String getErrorCode() {
        return "FRM00007";
    }
}