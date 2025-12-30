/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.util;

import nebsoa.common.Constants;
import nebsoa.common.util.StringUtil;


/**
 * <p>Title : nebsoa.common.jgen.DbTypeUtil</p>
 * <p>Created Date & Time : 2005. 3. 2. 오후 2:32:26</p>
 * <p>Description : 
 * </p>
 * 
 * <p>Copyright : www.solidframework.org</p>
 * 
 * @author Cho Man-hee (e-mail : helexis@empal.com)
 * @version 1.0
 */
/*******************************************************************
 * <pre>
 * 1.설명 
 * 데이타베이스 타입정보를 관리하기 위한 Utility 클래스
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
 * $Log: DbTypeUtil.java,v $
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
 * Revision 1.2  2008/01/25 09:27:17  오재훈
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:17  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.4  2007/12/03 02:10:21  안경아
 * *** empty log message ***
 *
 * Revision 1.3  2007/11/30 09:46:53  안경아
 * DB NAME 지정
 *
 * Revision 1.2  2007/11/28 01:29:35  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2007/11/26 08:38:01  안경아
 * *** empty log message ***
 *
 * Revision 1.4  2007/02/09 07:53:55  김승희
 * Altibase DB Type 추가에 따른 변경
 *
 * Revision 1.3  2006/06/17 10:37:45  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class DbTypeUtil {
    
    /**
     * Oracle 데이터베이스 타입을 나타내는 문자열 상수
     */
    public static final String ORACLE_TYPE = "OracleSchemaBrowser";
    /**
     * Mysql 데이터베이스 타입을 나타내는 문자열 상수
     */
    public static final String MY_SQL_TYPE = "MySqlSchemaBrowser";
    /**
     * MS-SQL 데이터베이스 타입을 나타내는 문자열 상수
     */
    public static final String MS_SQL_TYPE = "MsSqlSchemaBrowser";

    /**
     * Sybase 데이터베이스 타입을 나타내는 문자열 상수
     */
    public static final String SYBASE_TYPE = "SybaseSchemaBrowser";    
    
    /**
     * Altibase 데이터베이스 타입을 나타내는 문자열 상수
     */
    public static final String ALTIBASE_TYPE = "AltibaseSchemaBrowser";

    /**
     * DB2 데이터베이스 타입을 나타내는 문자열 상수
     */
    public static final String DB2_TYPE = "DB2SchemaBrowser";
    
    /**
     * Oracle 데이터베이스 타입을 나타내는 상수
     */
    public static final int ORACLE = 0;
    /**
     * Mysql 데이터베이스 타입을 나타내는 상수
     */
    public static final int MY_SQL = 1;
    /**
     * MS-SQL 데이터베이스 타입을 나타내는 상수
     */
    public static final int MS_SQL = 2;
    /**
     * SYBASE 데이터베이스 타입을 나타내는 상수
     */
    public static final int SYBASE = 3;
    /**
     * ALTIBASE 데이터베이스 타입을 나타내는 상수
     */
    public static final int ALTIBASE = 4;
    /**
     * DB2 데이터베이스 타입을 나타내는 상수
     */
    public static final int DB2 = 5;    
    
    
    /**
     * Oracle 데이터베이스 타입의 sql 디렉토리를 나타내는 상수
     */
    public static final String ORACLE_SQL_DIR = "oracle";
    /**
     * Mysql 데이터베이스 타입의 sql 디렉토리를 나타내는 상수
     */
    public static final String MYSQL_SQL_DIR = "mysql";
    /**
     * MS-SQL 데이터베이스 타입의 sql 디렉토리를 나타내는 상수
     */
    public static final String MSSQL_SQL_DIR = "mssql";
    /**
     * SYBASE 데이터베이스 타입의 sql 디렉토리를 나타내는 상수
     */
    public static final String SYBASE_SQL_DIR = "sybase";
    /**
     * ALTIBASE 데이터베이스 타입의 sql 디렉토리를 나타내는 상수
     */
    public static final String ALTIBASE_SQL_DIR = "altibase";
    /**
     * DB2 데이터베이스 타입의 sql 디렉토리를 나타내는 상수
     */
    public static final String DB2_SQL_DIR = "db2";    
    
    /**
     * 기본 데이터베이스의 UUID 관련 Object 를 리턴합니다.
     * 
     * @return UUID Object 를 나타내는 문자열
     */
    public static String getUuidGen() {
        return getUuidGen(getDefaultDbTypeName());
    }//end of getUuidGen()
    
    /**
     * 기본 데이터베이스의 현재일자를 가져오는 function 을 리턴합니다.
     * 
     * @return 현재 일자를 나타내는 function
     */
    public static String getSystemDate() {
    	return getSystemDate(getDefaultDbTypeName());
    }//end of getSystemDate()
    
    /**
     * 기본 데이터베이스에 대한 데이터베이스 타입을 리턴합니다.
     * 
     * @return 데이터베이스 타입을 나타내는 상수
     */
    public static int getDbType() {
        return getDbType(Constants.SPIDER_DB);
    }//end of getDbType()
    
    /**
     * 기본 데이터베이스 타입의 문자열을 리턴합니다.
     * 
     * @return 기본 데이터베이스 타입을 나타내는 문자열
     */
    public static String getDefaultDbTypeName() {
        final String dbPropertyName = "db";
        return PropertyManager.getProperty(dbPropertyName, "DEFAULT_DB", "");
    }//end of getDefaultDbTypeName()    
    
    /**
     * 특정 데이터베이스의 UUID 관련 Object 를 리턴합니다.
     * 
     * @param dbName 데이터베이스 설정 명
     * @return UUID Object 를 나타내는 문자열
     */
    public static String getUuidGen(String dbName) {
        switch(getDbType(dbName)) {
        	case ORACLE:
        	    return "SEQUENCE";
        	case MY_SQL:
        	case MS_SQL:
        	case SYBASE:        		
        	    return "''";
        	default:
        	    return null;
        }//end switch case
    }//end of getUuidGen()
    
    /**
     * 특정 데이터베이스의 현재일자를 가져오는 function 을 리턴합니다.
     * 
     * @param dbName 데이터베이스 설정 명
     * @return 현재 일자를 나타내는 function
     */
    public static String getSystemDate(String dbName) {
        switch(getDbType(dbName)) {
        	case ORACLE:
        	    return "SYSDATE";
        	case MY_SQL:
        	    return "NOW()";
        	case MS_SQL:
        	    return "GETDATE()";
        	case SYBASE:
        	    return "GETDATE()";     
        	case ALTIBASE:
        	    return "SYSDATE";
        	default:
        	    return null;
        }//end switch case
    }//end of getSystemDate()
    
    /**
     * 주어진 이름의 데이터베이스에 대한 데이터베이스 타입을 리턴합니다.
     * 
     * @param dbName 데이터베이스 설정 명
     * @return 데이터베이스 타입을 나타내는 상수
     */
    public static int getDbType(String dbName) {
        final String dbPropertyName = "db";
        
        if( StringUtil.isNull(dbName) ) 
        	dbName = getDefaultDbTypeName();
        
        String dbType = PropertyManager.getProperty(dbPropertyName, dbName + ".JDBC_DBMS_TYPE", "");
        if (ORACLE_TYPE.equals(dbType)) {
            return ORACLE;
        } else if (MY_SQL_TYPE.equals(dbType)) {
            return MY_SQL;
        } else if (MS_SQL_TYPE.equals(dbType)) {
            return MS_SQL;
        } else if (SYBASE_TYPE.equals(dbType)) {
            return SYBASE;      
        } else if (ALTIBASE_TYPE.equals(dbType)) {
            return ALTIBASE; 
        } else if (DB2_TYPE.equals(dbType)) {
            return DB2;             
        } else {
            return -1;
        }//end if else
    }//end of getDbType()

}// end of DbTypeUtil.java