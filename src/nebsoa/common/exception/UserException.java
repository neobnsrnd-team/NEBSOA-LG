/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.exception;


/*******************************************************************************
 * <pre>
 *  1.설명 
 *  사용자를 위한 안내 메세지를 가지고 있는 Exception 클래스
 *  message.properties.xml에서 해당하는 코드의 메세지를 얻어 옵니다.
 *  
 *  
 *  2.사용법
 *  
 *  &lt;font color=&quot;red&quot;&gt;
 *  3.주의사항
 *  &lt;/font&gt;
 * 
 *  @author $Author: cvs $
 *  @version
 * ******************************************************************
 *  - 변경이력 (버전/변경일시/작성자)
 *  
 *  $Log: UserException.java,v $
 *  Revision 1.1  2018/01/15 03:39:50  cvs
 *  *** empty log message ***
 *
 *  Revision 1.1  2016/04/15 02:22:41  cvs
 *  neo cvs init
 *
 *  Revision 1.1  2011/07/01 02:13:51  yshong
 *  *** empty log message ***
 *
 *  Revision 1.1  2008/11/18 11:27:24  김성균
 *  *** empty log message ***
 *
 *  Revision 1.1  2008/11/18 11:01:27  김성균
 *  LGT Gateway를 위한 프로젝트로 분리
 *
 *  Revision 1.1  2008/08/04 08:54:50  youngseokkim
 *  *** empty log message ***
 *
 *  Revision 1.2  2008/04/24 00:43:24  김재범
 *  *** empty log message ***
 *
 *  Revision 1.1  2008/01/22 05:58:18  오재훈
 *  패키지 리펙토링
 *
 *  Revision 1.1  2007/11/26 08:38:04  안경아
 *  *** empty log message ***
 *
 *  Revision 1.8  2006/11/03 02:32:50  김성균
 *  *** empty log message ***
 *
 *  Revision 1.7  2006/08/17 12:08:21  김성균
 *  NestedException 처리변경
 *
 *  Revision 1.6  2006/08/01 01:41:58  김성균
 *  오류코드 관련 수정
 *
 *  Revision 1.5  2006/07/28 05:45:08  이종원
 *  *** empty log message ***
 * 
 *  Revision 1.4  2006/07/28 05:36:04  이종원
 *  ExceptionTracer적용
 * 
 *  Revision 1.3  2006/07/05 17:05:22  이종원
 *  오류코드 정의
 * 
 *  Revision 1.2  2006/06/19 04:10:16  안경아
 *  최초등록
 * 
 * </pre>
 ******************************************************************************/
public class UserException extends NestedCheckedException implements SpiderException {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 6325861247426712263L;

    private String errorCode;

    private String userMessage;

    public UserException() {    	
    }
    
    public UserException(String msg) {
        this("FRU99999", msg);
    }

    public UserException(Throwable ex) {
        super(ex);
        if (ex instanceof SpiderException) {
            errorCode = ((SpiderException) ex).getErrorCode();
            userMessage = ((SpiderException) ex).getMessage();
        }
    }

    public UserException(String code, String msg) {
        super(msg);
        this.errorCode = code;
        this.userMessage = msg;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public String getMessage() {
        return this.userMessage;
    }
}
