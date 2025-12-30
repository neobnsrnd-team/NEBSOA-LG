/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.exception;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 기관으로부터 데이터 길이가 큰 전문 수신시 발생.
 * 제한 사이즈는 spiderlink.property에 MESSAGE_MAX_LENGTH로 조정 가능.
 * 없을 시 10000000 BYTE .
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
 * $Log: DataOverflowException.java,v $
 * Revision 1.1  2018/01/15 03:39:51  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:53  cvs
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
 * Revision 1.1  2007/07/30 03:00:52  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class DataOverflowException extends CommunicationException {
    
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -2159976110101792325L;

    /**
     * 기본 생성자
     */
    public DataOverflowException() {
        super();
    }

    /**
     * 세부 메시지를 갖는 생성자
     * @param message 세부 메시지
     */
    public DataOverflowException(String message) {
        super(message);
    }

    /**
     * 세부 메시지와 원본 예외를 갖는 생성자
     * 
     * @param message 세부 메시지
     * @param cause 원본 예외
     */
    public DataOverflowException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 원본 예외를 갖는 생성자
     * @param cause 원본 예외
     */
    public DataOverflowException(Throwable cause) {
        super(cause);
    }
}
