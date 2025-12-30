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
 * TestException 클래스
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
 * $Log: TestException.java,v $
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
 * Revision 1.3  2006/11/09 00:32:12  김성균
 * *** empty log message ***
 *
 * Revision 1.2  2006/11/09 00:30:37  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2006/09/28 05:30:07  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class TestException extends NestedCheckedException implements SpiderException {
	
    private static final long serialVersionUID = -1556704997443031235L;

    public TestException(String msg) {
		super(msg);
	}

	public TestException() {
		super("처리 중 오류가 발생하였습니다.");
	}
    
	public TestException(Throwable ex) {
		super(ex);
	}
    
    public String getErrorCode(){
    	//FIXME : 오류코드 수정
        return "FRS00001";
    }
}
