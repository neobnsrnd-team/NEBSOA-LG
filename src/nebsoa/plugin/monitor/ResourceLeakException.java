/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.plugin.monitor;


/*******************************************************************
 * <pre>
 * 1.설명 
 *  자원이 정상적으로 반납되지 않은 경우 framework에서 모니터링 하여 발생시키는 exception 
 * 
 * 2.사용법
 *  생성자 참조
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author 이종원
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 
 * $Log: ResourceLeakException.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:35  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:27  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:29  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:34  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:46  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2006/11/21 06:42:48  이종원
 * Exception Trace customizing
 *
 * Revision 1.1  2006/11/21 06:37:14  이종원
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class ResourceLeakException extends Exception {
    public ResourceLeakException(){
        super("Resource Not closed"
                +"\n(created:"+new java.util.Date()+")");
    }
    
    public ResourceLeakException(String category, String name){
        super("Resource Not closed::category-"+category+",name-"+name
                +"\n(created:"+new java.util.Date()+")");
    }
}
