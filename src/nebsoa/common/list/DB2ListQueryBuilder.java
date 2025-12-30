/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.list;

import nebsoa.common.jdbc.DBManager;
import nebsoa.common.log.LogManager;

/*******************************************************************
 * <pre>
 * 1.설명 
 * DB2 용 리스트 쿼리를 생성하는 클래스
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
 * $Log: DB2ListQueryBuilder.java,v $
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
 * Revision 1.1  2008/01/22 05:58:22  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.3  2007/11/30 02:43:23  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2007/11/28 02:23:06  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2007/11/28 01:28:19  안경아
 * *** empty log message ***
 *
 *
 * </pre>
 ******************************************************************/
public class DB2ListQueryBuilder implements ListQueryBuilder {
    
    private String columns = " * ";

    /**
     * 원문 쿼리를 받아서 Oracle 용 리스트 조회형 쿼리로 변환합니다.
     * 
     * @param originalQuery 원문 쿼리
     * @return 리스트 조회형으로 변환된 쿼리
     * @see nebsoa.common.list.ListQueryBuilder#buildQuery(java.lang.String)
     */
    public String buildQuery(String originalQuery, int fetchFirst) {
        StringBuffer query = new StringBuffer();
        if(DBManager.isQueryMonitorMode()){
            LogManager.debug("원문 쿼리 : "+originalQuery);
        }
        //String orderBy ="ORDER BY USER_ID, USER_NAME";
        query.append("SELECT ")
        	.append(columns)
        	.append(" FROM (")
        	.append("\r\n\t SELECT ROW_NUMBER() OVER () as ROW_SEQ,")
        	.append("INNER_TABLE.*")
        	.append("\r\n\t     FROM (\r\n\t\t")
			.append(originalQuery)
			.append("\r\n\t\t FETCH FIRST "+fetchFirst+" ROWS ONLY " )
			.append("\r\n\t\t ) INNER_TABLE " )
			.append("\r\n\t ) INNER ")
        	.append("\r\n WHERE ROW_SEQ BETWEEN ? AND ?");
        return query.toString();
    }//end of buildQuery()

	public String buildQuery(String originalQuery) {
		// TODO Auto-generated method stub
		return null;
	}

}// end of OracleListQueryBuilder.java