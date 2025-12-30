/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.sql;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import nebsoa.common.Constants;
import nebsoa.common.exception.DBException;
import nebsoa.common.exception.SysException;
import nebsoa.common.jdbc.DBManager;
import nebsoa.common.jdbc.DBResultSet;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.DbTypeUtil;
import nebsoa.common.util.PropertyManager;
import nebsoa.common.util.StringUtil;

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
 * $Log: DBQueryLoader.java,v $
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
 * Revision 1.1  2007/11/26 08:39:04  안경아
 * *** empty log message ***
 *
 * Revision 1.6  2006/06/17 10:37:45  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class DBQueryLoader implements QueryLoader {
    
    private static DBQueryLoader instance;
    
	/**
	 * SQL 캐쉬풀 
	 */
	private static Map queryPool;
	
	/**
	 * SQL 캐쉬여부
	 */
	private static boolean isCaching = false;
	
	static 
	{
	    isCaching = PropertyManager.getBooleanProperty("db", "SQL_CACHING_MODE", "ON");
	}
	
	/*
	 public static String getQuery(String fileName, String key){
	 LogManager.debug("fileName: "+fileName +",query Name :"+key);
	 return getProperty(fileName,key);
	 }
	 */
	
	/**
     * DBQueryLoader 인스턴스 생성
     * 
     * @return DBQueryLoader 인스턴스
     */
    public static synchronized DBQueryLoader getInstance() {
        if(instance == null) {
            instance = new DBQueryLoader();
        }
        
        if (isCaching) {
			queryPool = new HashMap();
			instance.loadAll();
		}        
        return instance;
    }
	
	/**
	 * 쿼리를 얻어온다.
	 * 
	 * @param sqlGroupId
	 *            SQL그룹ID
	 * @param sqlId
	 *            SQLD
	 * @return SQL
	 */
	public String getQuery(String sqlGroupId, String sqlId) {
		String key = sqlGroupId + "@" + sqlId;
		String query = null;
		
		if (isCaching) {
			query = getQuery(key); 
			
			/*
			 * 캐쉬된 쿼리 결과가 없다면 직접 DB에서 쿼리를 찾아온다.
			 * DB에서 찾아온 쿼리 결과값은 캐쉬 테이블에도 저장된다.
			 * 신규로 쿼리문을 추가할 경우에는 별도로 reload할 필요없음.(그러나, 기존 쿼리문이 수정된 것이라면,
		     * loadAll() 메소드를 호출하여 다시 캐쉬해야한다.)
			 * 
			 * 2006.05.11 - sjChoi 수정
			 */
			if(query == null)
			{
			    query = load(sqlGroupId, sqlId, isCaching);
			}
			
		} else {
			query = load(sqlGroupId, sqlId, isCaching);
		}
		
		if (query == null) {
			throw new SysException("SQL 정보가 존재하지 않습니다.");
		}
		return query;
	}
	
	/**
	 * 쿼리를 얻어온다.
	 * 
	 * @param sqlGroupId
	 *            SQL그룹ID
	 * @param sqlId
	 *            SQLD
	 * @return SQL
	 */
	public String getQuery(int dbType, String sqlGroupId, String sqlId) {
		return getQuery(dbType+sqlGroupId, sqlId);
	}	
	/**
	 * 쿼리풀로부터 쿼리를 얻어온다.
	 * 
	 * @param key
	 *            쿼리풀에 등록된 키값
	 * @return SQL
	 */
	private static String getQuery(String key) {
		String query = (String) queryPool.get(key);
		return query;
	}
	
	private static final String SELECT_SQL =
			"SELECT SQL FROM SQL WHERE DB_TYPE=? AND SQL_GROUP_ID=? AND SQLID=?";
	
	/**
	 * DB에서 특정 쿼리문을 가져온다.
	 * 
	 * @param sqlGroupId
	 * @param sqlId
	 * @return
	 */
	private static String load(String sqlGroupId, String sqlId, boolean isCaching) {
		Object[] param = {
				Integer.valueOf(DbTypeUtil.getDbType(Constants.SPIDER_DB)),
				sqlGroupId,
				sqlId
		};
		DBResultSet drs = DBManager.executePreparedQuery(Constants.SPIDER_DB, SELECT_SQL, param);
		String sql = null;
		if (drs.next()) {
			sql = drs.getString("SQL");
			
			/*
			 * 캐쉬여부에 따라 select한 query문을 캐쉬함.
			 * 
			 * 2006.05.11 - sjChoi 수정
			 */
			if(isCaching)
			{
			    queryPool.put(drs.getString(1), drs.getString(2));
			}
		}
		if (StringUtil.isNull(sql)) {
			throw new DBException(sqlGroupId + "@" + sqlId + "==>SQL 정보가 존재하지 않습니다.");
		}
		return sql;
	}	
	


	private static final String SELECT_ALL_SQL =
			"SELECT DB_TYPE||'@'||SQL_GROUP_ID||'@'||SQLID, SQL FROM SQL";
	
	/**
	 * DB에서 모든 쿼리문을 가져와 캐쉬한다.
	 *
	 */
	public void loadAll() {
		if (queryPool == null) {
			queryPool = new HashMap();
		}
		DBResultSet drs = DBManager.executeQuery(Constants.SPIDER_DB, SELECT_ALL_SQL);
		LogManager.debug(drs);
		while (drs.next()) {
			queryPool.put(drs.getString(1), drs.getString(2));
		}
	}
	
	public static void main(String args[]) {
		//System.out.println(DBQueryLoader.getQuery("POC", "DUAL"));
	}

}
