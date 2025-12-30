/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.exception;

import nebsoa.common.Context;
import nebsoa.common.util.PropertyManager;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 사용자가 로그인 하지 않았음을 알리기 위한 exception 클래스
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
 * $Log: LoginException.java,v $
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
 * Revision 1.8  2007/01/04 09:48:40  이종원
 * 로그인 세션 종료 로깅 시 요청한 url도 같이 남김
 *
 * Revision 1.7  2006/10/18 02:47:32  김성균
 * *** empty log message ***
 *
 * Revision 1.6  2006/08/17 12:08:21  김성균
 * NestedException 처리변경
 *
 * Revision 1.5  2006/07/28 05:36:04  이종원
 * ExceptionTracer적용
 *
 * Revision 1.4  2006/07/05 17:05:22  이종원
 * 오류코드 정의
 *
 * Revision 1.3  2006/06/19 04:10:16  안경아
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class LoginException extends NestedCheckedException implements SpiderException {
	
	/**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -4396749071015974789L;
    
    private String loginPage = PropertyManager.getProperty("web_config","LOGIN_PAGE");
	
	public LoginException(){
		super("로그인 하지 않았거나 세션이 종료 되었습니다.");
	}
	
	/*
	 * 기존 LoginException 메소드 메세지 처리 수정
	 * (주어진 메세지로 에러 메세지 발생)
	 * 
	 * 2006.05.30 - sjChoi 수정
	 */
	public LoginException(String msg){
		super(msg);
	}
	
	/*
	 * 기존 LoginException 메소드 메세지 처리 수정
	 * 
	 * 2006.05.30 - sjChoi 수정
	 */
	public LoginException(String msg, String loginPage){
	    this(null, msg, loginPage);
	}

    public LoginException(Context ctx, String msg, String loginPage) {
        super(msg+(ctx==null?"":"(UserInfo:"+ctx.toString()+")"));
        this.loginPage = loginPage;
    }

    public String getErrorCode() {
        return "FRU00001";
    }
    
    public String getLoginPage() {
        return loginPage;
    }
}
