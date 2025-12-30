/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.util;

import java.util.HashMap;

import nebsoa.common.sql.QueryLoader;
import nebsoa.common.sql.XmlQueryLoader;
import nebsoa.common.startup.StartupContext;

/*******************************************************************
 * <pre>
 * 1.설명 
 * SQL 정보를 관리합니다.
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
 * $Log: QueryManager.java,v $
 * Revision 1.1  2018/01/15 03:39:49  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:32  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:23  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:26  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:50  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:18  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.3  2007/12/03 00:50:55  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2007/11/30 09:46:53  안경아
 * DB NAME 지정
 *
 * Revision 1.1  2007/11/26 08:38:01  안경아
 * *** empty log message ***
 *
 * Revision 1.18  2006/11/29 05:26:14  최수종
 * *** empty log message ***
 *
 * Revision 1.17  2006/11/02 02:51:52  김성균
 * *** empty log message ***
 *
 * Revision 1.16  2006/11/02 02:51:25  김성균
 * *** empty log message ***
 *
 * Revision 1.15  2006/10/24 12:28:39  김성균
 * *** empty log message ***
 *
 * Revision 1.14  2006/07/25 11:58:45  김성균
 * DBLoader 사용하지 않도록 변경
 *
 * Revision 1.13  2006/06/17 10:37:45  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class QueryManager { //extends PropertyManager {

    private static String sqlLoadType = StartupContext.getPropertyType();
    
    /**
     * @return  QueryLoader
     */
    private static QueryLoader getInstance() {
		return XmlQueryLoader.getInstance();
        /*
		if (sqlLoadType.equals("DB")) {
			return DBQueryLoader.getInstance();
		} else {
			return XmlQueryLoader.getInstance();
		}
        */
	}
    
	/**
	 * 쿼리를 얻어온다. 
     * 신규로 추가된 쿼리는 별도의 reload없이 읽어온다. 
     * 단, 수정된 쿼리를 반영하기 위해서는loadAll()메소드를 호출하여 다시 캐쉬해야 한다.
	 * 
	 * @param sqlGroupId
	 *            SQL그룹ID
	 * @param sqlId
	 *            SQLID
	 * @return SQL
	 */
	public static String getQuery(int dbType, String sqlGroupId, String sqlId) {
        return getInstance().getQuery(dbType, sqlGroupId, sqlId);
    }
	
	/**
	 * 쿼리를 얻어온다. 
     * 신규로 추가된 쿼리는 별도의 reload없이 읽어온다. 
     * 단, 수정된 쿼리를 반영하기 위해서는loadAll()메소드를 호출하여 다시 캐쉬해야 한다.
	 * 
	 * @param sqlGroupId
	 *            SQL그룹ID
	 * @param sqlId
	 *            SQLID
	 * @return SQL
	 */
	public static String getQuery(String sqlGroupId, String sqlId) {
        return getInstance().getQuery(sqlGroupId, sqlId);
    }
	
	/**
	 * 모든 쿼리를 다시 캐쉬한다.
	 * @param map
	 */
	public static void reloadAll(DataMap map) {
		getInstance().loadAll();
	}
	
	/**
	 * 모든 쿼리를 다시 캐쉬한다.
	 */
	public static void reloadAll() {
		getInstance().loadAll();
	}	
	
	public static String getQueryToMap(HashMap map){		
		return (String)map.get(Integer.valueOf(DbTypeUtil.getDbType()).toString());
	}
}
