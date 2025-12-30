/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.sql;


/*******************************************************************
 * <pre>
 * 1.설명 
 * DB SQL Query 정보를 로딩하기 위한 로더객체가 구현해야 되는 인터페이스 
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
 * $Log: QueryLoader.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:30  cvs
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
 * Revision 1.1  2008/01/22 05:58:36  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.2  2007/11/30 09:46:53  안경아
 * DB NAME 지정
 *
 * Revision 1.1  2007/11/26 08:39:05  안경아
 * *** empty log message ***
 *
 * Revision 1.6  2007/01/22 20:11:33  이종원
 * sql group id와 파일명이 다른 경우 디버깅 추가
 *
 * Revision 1.5  2006/06/17 10:37:45  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public interface QueryLoader {
    
    /**
     * sql 쿼리문을 가져온다.
     * 
     * @param sqlGroupId
     * @param sqlId
     * @return
     */
    public String getQuery(String sqlGroupId, String sqlId);
 
    public String getQuery(int dbType, String sqlGroupId, String sqlId);
    
	/**
	 * 모든 쿼리문을 가져와 캐쉬한다.
	 *
	 */
	public void loadAll();
   
}

