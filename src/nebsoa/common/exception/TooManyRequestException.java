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
 * 설정된 갯수 이상의 요청을 받았을 때 프레임웍에서 예외 처리시 발생하는 Exception
 * 
 * 
 * 
 * 2.사용법
 * SysException의 하위 클래스 이므로 SysException과 동일하게 사용.
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
 * $Log: TooManyRequestException.java,v $
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
 * Revision 1.3  2007/05/01 08:03:17  김성균
 * *** empty log message ***
 *
 * Revision 1.2  2007/05/01 06:40:57  이종원
 * 최초작성
 *
 * Revision 1.1  2007/05/01 06:32:26  이종원
 * 최초작성
 *
 * </pre>
 ******************************************************************/
public class TooManyRequestException extends SysException {
	
	/**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 2184946215580805475L;
    
    static String defaultMessage="해당 서비스 사용자가 많습니다. 잠시후 이용하여 주십시오.";
	
	public TooManyRequestException(String msg){
        super("FRS99911",msg);
	}

	
	public TooManyRequestException(String errCode , String msg){
        super(errCode,msg);
	}
	
	public TooManyRequestException(){
        super("FRS99911",defaultMessage);
    }
}