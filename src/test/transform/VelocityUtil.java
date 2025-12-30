/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package test.transform;


/*******************************************************************
 * <pre>
 * 1.설명 
 *  xml로 된 전문 layout을 읽어서 전문 파싱 처리하는데 있어 
 *  전문 템플릿 관련 유틸리티  클래스입니다.
 * 
 * 2.사용법
 *
 * 3.주의사항
 * </font>
 *
 * @author $Author: cvs $
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 
 * $Log: VelocityUtil.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:25  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:26  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:29  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2007/11/26 08:39:16  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2006/06/29 04:01:35  이종원
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class VelocityUtil {
    
    public VelocityUtil() {
    }//end of constructor
    
    public Integer toInt(String number) {
        return Integer.valueOf(number);
    }//end of toInt()

    public Double toDouble(String number) {
        return Double.valueOf(number);
    }//end of toDouble()

}// end of VelocityUtil.java