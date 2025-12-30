/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.list;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 리스트 조회형 쿼리로 변환하는 클래스의 아키텍쳐 설계 interface 
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
 * $Log: ListQueryBuilder.java,v $
 * Revision 1.1  2018/01/15 03:39:51  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:59  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:24  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:28  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:51  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:21  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.2  2007/11/30 02:43:23  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2007/11/26 08:37:38  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2006/06/19 04:10:16  안경아
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public interface ListQueryBuilder {
    
    /**
     * 원문 쿼리를 받아서 리스트 조회형 쿼리로 변환합니다.
     * 
     * @param originalQuery 원문 쿼리
     * @return 리스트 조회형으로 변환된 쿼리
     */
    public String buildQuery(String originalQuery);

    /**
     * 원문 쿼리를 받아서 리스트 조회형 쿼리로 변환합니다.
     * 
     * @param originalQuery 원문 쿼리
     * @return 리스트 조회형으로 변환된 쿼리
     */
    public String buildQuery(String originalQuery, int fetchFirst);
    
}// end of ListQueryBuilder.java