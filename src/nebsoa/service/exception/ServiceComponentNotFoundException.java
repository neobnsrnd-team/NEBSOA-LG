/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.service.exception;


/*******************************************************************
 * <pre>
 * 1.설명 
 * 더이상 존재하지 않는 서비스컴포넌트를 요청 시 발생하는 예외상황에 대한 Exception 클래스 입니다.
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
 * $Log: ServiceComponentNotFoundException.java,v $
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
 * Revision 1.2  2008/02/14 09:10:46  김성균
 * ServiceSysException 상속받도록 수정
 *
 * Revision 1.1  2008/01/22 05:58:28  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2008/01/07 01:04:10  이종원
 * NoLoggerException refactoring
 *
 * Revision 1.1  2007/12/31 04:35:34  최수종
 * nebsoa.service 패키지 추가
 *
 * Revision 1.1  2007/12/26 04:31:29  김성균
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class ServiceComponentNotFoundException extends ServiceSysException {
    
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -6872576627429723358L;
    
    private static final String ERROR_CODE = "";
    
    /**
     * @param index
     */
    public ServiceComponentNotFoundException(int index) {
        super(ERROR_CODE, "The ServiceComponent that no longer exists.(index:"+index+")");
    }
}
