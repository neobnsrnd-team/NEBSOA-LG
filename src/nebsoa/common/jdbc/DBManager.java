/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.jdbc;

import java.io.InputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nebsoa.common.Context;
import nebsoa.common.collection.DataSet;
import nebsoa.common.collection.MapDataSet;
import nebsoa.common.exception.DBException;
import nebsoa.common.exception.SysException;
import nebsoa.common.jdbc.context.DBContext;
import nebsoa.common.jdbc.types.OracleTypes;
import nebsoa.common.jdbc.types.SqlTypes;
import nebsoa.common.log.LogManager;
import nebsoa.common.monitor.MonitorData;
import nebsoa.common.monitor.SysMonitor;
import nebsoa.common.startup.StartupContext;
import nebsoa.common.util.DataMap;
import nebsoa.common.util.PropertyManager;
import nebsoa.common.util.StringUtil;

import org.apache.commons.beanutils.BeanUtils;

/*******************************************************************
 * <pre>
 * 1.설명 
 * db와 연동하기위한 유용한 메소드 모음 클래스
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
 * $Log: DBManager.java,v $
 * Revision 1.1  2018/01/15 03:39:49  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:35  cvs
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
 * Revision 1.1  2008/08/04 08:54:54  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.4  2008/07/17 02:43:30  김재범
 * executeBatch에 dataSet을 인자로 넘겨주는 함수 추가
 *
 * Revision 1.3  2008/01/31 04:37:37  김성균
 * 디버그 정보 주석 처리
 *
 * Revision 1.2  2008/01/25 09:06:36  오재훈
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:29  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:37:44  안경아
 * *** empty log message ***
 *
 * Revision 1.34  2007/07/13 17:20:40  이종원
 * *** empty log message ***
 *
 * Revision 1.33  2007/05/02 00:37:53  안경아
 * *** empty log message ***
 *
 * Revision 1.32  2007/04/27 05:51:00  이종원
 * setValue 로그 수정
 *
 * Revision 1.31  2007/04/27 05:50:34  이종원
 * setValue 로그 수정
 *
 * Revision 1.30  2007/03/16 09:10:12  김성균
 * error 로그 변경
 *
 * Revision 1.29  2007/03/15 00:41:51  김성균
 * 일반 Exception일 경우 로그남겨지는 카테고리 ERROR로 변경
 *
 * Revision 1.28  2007/03/07 01:20:40  김성균
 * 일부 로그 INFO 레벨로 변경
 *
 * Revision 1.27  2006/12/15 06:23:53  최수종
 * 프로시져 update 메소드 추가(프로시져 관련 트랜잭션 관리 메소드도 추가)
 *
 * Revision 1.26  2006/11/28 04:55:06  최수종
 * *** empty log message ***
 *
 * Revision 1.25  2006/11/27 06:24:51  최수종
 * *** empty log message ***
 *
 * Revision 1.24  2006/11/27 06:18:04  최수종
 * 프로시져 바인딩 변수 설정 메소드 추가
 *
 * Revision 1.23  2006/11/10 08:41:41  이종원
 * 바인드할 파라미터가 없습니다라는 문구 제거
 *
 * Revision 1.22  2006/11/10 08:38:49  이종원
 * 바인드할 파라미터가 없습니다라는 문구 제거
 *
 * Revision 1.21  2006/10/23 07:05:49  김성균
 * *** empty log message ***
 *
 * Revision 1.20  2006/10/21 08:30:25  김성균
 * *** empty log message ***
 *
 * Revision 1.19  2006/08/29 00:32:44  오재훈
 * dbName을 null로 넘기던것을 수정
 *
 * Revision 1.18  2006/06/21 09:43:03  이종원
 * DBHanderl추가
 *
 * Revision 1.17  2006/06/19 04:10:16  안경아
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class DBManager {

 //   static public Hashtable colNamesHash = new Hashtable();
    static public int openedConnectionCount = 0;
    static public int openedStatementCount = 0;
    static public int openedResultSetCount = 0;


    public static boolean isQueryMonitorMode() {
        return PropertyManager.getBooleanProperty("db",
            "QUERY_MONITOR_MODE", "true");    
    }

    /**
         해당하는 db에 접속하여 DB Connection를 얻어 내는 함수 입니다.
     */
    public static Connection getConnection(String dbName) throws DBException {
        return getConnection(null,dbName);
    }
    
    /**
     * DEFAULT로 설정된  db에 접속하여 DB Connection를 얻어 내는 함수 입니다.
     */
    public static Connection getConnection() throws DBException {
        String dbName=null;
        return getConnection(dbName);
    }
    
    /**
     *해당하는 db에 접속하여 DB Connection를 얻어 내는 함수 입니다.
     */
    public static Connection getConnection(Context ctx,String dbName) throws DBException {
       Connection con = DBConfig.getInstance(dbName).getConnection(ctx);
    
       if (con != null) {
           openedConnectionCount++;
           return con;
       }
       else {
           throw new DBException("DB CONNECT FIAL");
       }
    }
    
    /**
    * DEFAULT로 설정된  db에 접속하여 DB Connection를 얻어 내는 함수 입니다.
    */
    public static Connection getConnection(Context ctx) throws DBException {
      return getConnection(ctx,null);
    }

    /**
         해당하는 connection을 이용하여  createStatement 얻어 내는 함수 입니다.
     */
    public static Statement createStatement(Connection con) throws SQLException,
        DBException {
        Statement stmt = null;
        if (con != null) {
            stmt = con.createStatement();
            openedStatementCount++;
            return stmt;
        }
        else {
            throw new DBException("con is null at ..createStatement ");
        }
    }

	/**
	 * 해당하는 connection을 이용하여 PreparedStatement 얻어 내는 함수 입니다.
	 * 
	 * @param con
	 * @param sql
	 * @param resultSetType
	 * @param resultSetConcurrency
	 * @return
	 * @throws SQLException
	 * @throws DBException
	 */
    public static PreparedStatement prepareStatement(Connection con, String sql) throws
        SQLException, DBException {
        PreparedStatement stmt = null;
        if (con != null) {
            stmt = con.prepareStatement(sql);
            openedStatementCount++;
            return stmt;
        }
        else {
            throw new DBException("con is null at ..createStatement ");
        }
    }
    

	/**
	 * 해당하는 connection을 이용하여 PreparedStatement 얻어 내는 함수 입니다.
	 * 
	 * @param con Connection 객체
	 * @param sql 쿼리문
	 * @param resultSetType ResultSet Type
	 * @param resultSetConcurrency ResultSet Concurrency 
	 * @return PreparedStatement 객체
	 * @throws SQLException
	 * @throws DBException
	 */
    public static PreparedStatement prepareStatement(Connection con, String sql, int resultSetType, int resultSetConcurrency) throws
        SQLException, DBException {
        
        /*
         * 2006.04.26 - sjChoi 추가
         */
        PreparedStatement stmt = null;
        if (con != null) {
            stmt = con.prepareStatement(sql, resultSetType, resultSetConcurrency);
            openedStatementCount++;
            return stmt;
        }
        else {
            throw new DBException("con is null at ..createStatement ");
        }
    }
    /**
     * 해당하는 connection을 이용하여 CallableStatement 얻어 내는 함수 입니다.
     * 
     * @param con
     * @param sql
     * @param resultSetType
     * @param resultSetConcurrency
     * @param resultSetHoldability
     * @return
     * @throws SQLException
     * @throws DBException
     */
    public static CallableStatement callableStatement(Connection con, String sql, 
            int resultSetType, int resultSetConcurrency, int resultSetHoldability) 
        throws SQLException, DBException 
    {
        /*
         * 2006.05.25 - sjChoi 추가
         */
        CallableStatement cstmt = null;
        if (con != null) {
            cstmt = con.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
            openedStatementCount++;
            return cstmt;
        }
        else {
            throw new DBException("con is null at ..callableStatement ");
        }
    }    
    
    /**
     * 해당하는 connection을 이용하여 CallableStatement 얻어 내는 함수 입니다.
     * 
     * @param con
     * @param sql
     * @param resultSetType
     * @param resultSetConcurrency
     * @return
     * @throws SQLException
     * @throws DBException
     */
    public static CallableStatement callableStatement(Connection con, String sql, 
            int resultSetType, int resultSetConcurrency) 
        throws SQLException, DBException 
    {
        /*
         * 2006.05.25 - sjChoi 추가
         */
        CallableStatement cstmt = null;
        if (con != null) {
            cstmt = con.prepareCall(sql, resultSetType, resultSetConcurrency);
            openedStatementCount++;
            return cstmt;
        }
        else {
            throw new DBException("con is null at ..callableStatement ");
        }
    }    
    
    
    /**
     * 해당하는 connection을 이용하여 CallableStatement 얻어 내는 함수 입니다.
     * 
     * @param con
     * @param sql
     * @return
     * @throws SQLException
     * @throws DBException
     */
    public static CallableStatement callableStatement(Connection con, String sql) 
        throws SQLException, DBException 
    {
        /*
         * 2006.05.25 - sjChoi 추가
         */
        CallableStatement cstmt = null;
        if (con != null) {
            cstmt = con.prepareCall(sql);
            openedStatementCount++;
            return cstmt;
        }
        else {
            throw new DBException("con is null at ..callableStatement ");
        }
    }
    /**
     * PreparedStatement를 생성하고 파라미터를 셋팅한 후 리턴한다.
     * 
     * @param txMgr TxManager
     * @param sql 쿼리문
     * @param data 파라미터 
     * @return PreparedStatement
     * @throws DBException
     
    public static PreparedStatement makeTxPreparedStatement(TxManager txMgr, String sql, Object data[]) throws DBException {
        debugSQL(sql, data);
        
        Connection con = null;
        PreparedStatement pstmt = null;
        
        try {
          con = txMgr.getConnection();
          pstmt = prepareStatement(con, sql);
          setValues(pstmt, data);
          return pstmt;
        
        } catch (SQLException e) {
            logSQL(sql, data);
            LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
            e.printStackTrace();
            throw new DBException(e);
        } catch (Exception e) {
            logSQL(sql, data);
            LogManager.error("DB_ERROR",e.toString(),e); LogManager.error(e.toString(),e);
            e.printStackTrace();
            throw new SysException(e.toString());
        }
    }
*/


    /**
         close Connection
     */
    public static void close(Connection con) {
        try {
            if (con != null) {
                try {
                    if(con.getAutoCommit()==false){
                        Exception e = new Exception("close시점에 getAutoCommit false인 connection이 발견되었습니다.");
                        LogManager.error("MONITOR", e.toString(), e);
                        con.setAutoCommit(true);
                    }
                } catch (SQLException e) {
                }
                con.close();
                openedConnectionCount--;
            }
            else {
                LogManager.info("Connection is NULL");
            }
        }
        catch (Exception e) {
            LogManager.error("DB_ERROR", "Fail Connetion Close" + e.getMessage());
        }
    }

    /**
         close Statement
     */
    public static void close(Statement stmt) {
        try {
            if (stmt != null) stmt.close();
            openedStatementCount--;
        }
        catch (Exception e) {}
    }

    /**
         close ResultSet
     */
    public static void close(ResultSet rs) {
        try {
            if (rs != null) rs.close();
            openedResultSetCount--;
        }
        catch (Exception e) {}
    }

    /**
         close Connection , Statement , ResultSet
     */
    public static void close(Connection con, Statement stmt, ResultSet rs) {
        try {
            close(rs);
            close(stmt);
            close(con);
        }
        catch (Exception e) {}
    }

    /**
         ResultSet의 데이타를 DBResultSet에 담아 메모리로 데이타를 가져온다.
         자동으로 ResultSet은 닫힌다.
     */
    public static DBResultSet toMemory(ResultSet rs) throws DBException {
        return new DBResultSet(rs);
    }
    
    /**
     * ResultSet의 데이타를 DBResultSet에 담아 메모리로 데이타를 가져온다.
     * 자동으로 ResultSet은 닫힌다.
     * sybase를 위하여 만듬.
     */
    public static DBResultSet toMemory(ResultSet rs, int skipCount) throws DBException {
       return new DBResultSet(rs, skipCount);
    }  
    
    ////////////////////////////////////
    
    
    /**
     * CallableStatement를 사용하여 프로시져를 호출하고 Select 결과를 리턴 시켜주는 메소드
     * 
     * @param sql 쿼리문
     * @param data 파라미터
     * @return
     * @throws DBException
     */
    public static DBResultSet executeCallableQuery(String sql, Object data[]) throws DBException {
        return executeCallableQuery(null, sql, data, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    } 

    /**
     * CallableStatement를 사용하여 프로시져를 호출하고 Select 결과를 리턴 시켜주는 메소드
     * 
     * @param sql
     * @param paramList
     * @return
     * @throws DBException
     */
    public static DBResultSet executeCallableQuery(String sql, ArrayList paramList) throws DBException {
        return executeCallableQuery(null, sql, paramList.toArray(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    }   
    
    /**
     * CallableStatement를 사용하여 프로시져를 호출하고 Select 결과를 리턴 시켜주는 메소드
     * 
     * @param dbName
     * @param sql
     * @param data
     * @return
     * @throws DBException
     */
    public static DBResultSet executeCallableQuery(String dbName, String sql, Object data[]) throws DBException {
        return executeCallableQuery(dbName, sql, data, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    } 

    /**
     * CallableStatement를 사용하여 프로시져를 호출하고 Select 결과를 리턴 시켜주는 메소드
     * 
     * @param dbName
     * @param sql
     * @param paramList
     * @return
     * @throws DBException
     */
    public static DBResultSet executeCallableQuery(String dbName, String sql, ArrayList paramList) throws DBException {
        return executeCallableQuery(dbName, sql, paramList.toArray(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    }    
    
    /**
     * CallableStatement를 사용하여 프로시져를 호출하고 Select 결과를 리턴 시켜주는 메소드
     * 
     * @param dbName
     * @param sql
     * @param paramList
     * @param resultSetType
     * @param resultSetConcurrency
     * @return
     * @throws DBException
     */
    public static DBResultSet executeCallableQuery(String dbName, String sql,
            ArrayList paramList, int resultSetType, int resultSetConcurrency) throws DBException {
        return executeCallableQuery(dbName, sql, paramList.toArray(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    }
    
    /**
     * CallableStatement를 사용하여 프로시져를 호출하고 Select 결과를 리턴 시켜주는 메소드
     * 
     * @param dbName
     * @param sql
     * @param data
     * @param resultSetType
     * @param resultSetConcurrency
     * @return
     * @throws DBException
     */
    public static DBResultSet executeCallableQuery(String dbName, String sql,
        Object data[], int resultSetType, int resultSetConcurrency) throws DBException {

        debugSQL(sql, data);
        long start=System.currentTimeMillis();
        Connection con = null;
        CallableStatement cstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection(dbName);
            
            cstmt = callableStatement(con, sql, resultSetType, resultSetConcurrency);

            //pstmt.setQueryTimeout(5);
            setValues(cstmt, data);
            rs = cstmt.executeQuery();
            openedResultSetCount++;
            
            DBResultSet rset = toMemory(rs);
            
            if(isQueryMonitorMode()){
                long end=System.currentTimeMillis();
                SysMonitor.addMonitorData( new MonitorData("Database",sql,end-start)); 
            } 
            return rset;
        }
        catch (SQLException e) {
            logSQL(sql, data);
            LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
            throw new DBException(dbName,e);
        }
        catch (Exception e) {
            logSQL(sql, data);
            LogManager.error("DB_ERROR",e.toString(),e); LogManager.error(e.toString(),e);
            throw new SysException(e.toString());
        }
        finally {
            close(rs);
            close(cstmt);
            close(con);
        }
    }
    

    /**
     * CallableStatement를 사용하여 insert,delete, update 결과를 리턴 시켜주는 메소드
     * 
     * @param sql
     * @param data
     * @return
     * @throws DBException
     */
	public static int executeCallableUpdate(String sql, Object data[]) 
		throws DBException {
	    return executeCallableUpdate(null, sql, data);
	} 
	
	/**
	 * CallableStatement를 사용하여 insert,delete, update 결과를 리턴 시켜주는 메소드
	 * 
	 * @param sql
	 * @param paramList
	 * @return
	 * @throws DBException
	 */
	public static int executeCallableUpdate(String sql, ArrayList paramList) 
		throws DBException {
	    return executeCallableUpdate(null, sql, paramList.toArray());
	}	
    
	/**
	 * CallableStatement를 사용하여 insert,delete, update 결과를 리턴 시켜주는 메소드
	 * 
	 * @param dbName
	 * @param sql
	 * @param paramList
	 * @return
	 * @throws DBException
	 */
	public static int executeCallableUpdate(String dbName, String sql,
	        ArrayList paramList) throws DBException {
	    return executeCallableUpdate(dbName, sql, paramList.toArray());
	}	
	    
    /**
     * CallableStatement를 사용하여 insert,delete, update 결과를 리턴 시켜주는 메소드
     * 
     * @param dbName
     * @param sql
     * @param data
     * @return
     * @throws DBException
     */
	public static int executeCallableUpdate(String dbName, String sql,
	                                       Object data[]) throws DBException {
	
	   debugSQL(sql, data);
	   long start=System.currentTimeMillis();
	   Connection con = null;
	   CallableStatement cstmt = null;
	
	   try {
	       con = getConnection(dbName);
	
	       cstmt = callableStatement(con, sql);
	
	       //pstmt.setQueryTimeout(5);
	       setValues(cstmt, data);
	       int result = cstmt.executeUpdate();
	       if(isQueryMonitorMode()){
	           long end=System.currentTimeMillis();
	           SysMonitor.addMonitorData( new MonitorData("Database",sql,end-start)); 
	       } 
	       return result;
	   }
	   catch (SQLException e) {
	       logSQL(sql, data);
	       LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
	       throw new DBException(dbName,e);
	   }
	   catch (Exception e) {
	       logSQL(sql, data);
	       e.printStackTrace();
	       LogManager.error("DB_ERROR",e.toString(),e); LogManager.error(e.toString(),e);
	       throw new SysException(e.toString());
	   }
	   finally {
	       close(cstmt);
	       close(con);
	   }
	}    
	
	/**
	 * 트렌젝션 관리를 위해 하나의 커넥션을 공유하며,
	 * CallableStatement를 사용하여 insert,delete, update 결과를 리턴 시켜주는 메소드
	 * 
	 * @param dbName
	 * @param sql
	 * @param paramList
	 * @return
	 * @throws DBException
	 */
	public static int executeCallableTxUpdate(TxManager txMgr, String sql,
	        ArrayList paramList) throws DBException {
	    return executeCallableTxUpdate(txMgr, sql, paramList.toArray());
	}	
	
    /**
     * 트렌젝션 관리를 위해 하나의 커넥션을 공유하며, 
     * CallableStatement를 사용하여 insert,delete, update 결과를 리턴 시켜주는 메소드
     * 
     * @param dbName
     * @param sql
     * @param data
     * @return
     * @throws DBException
     */
	public static int executeCallableTxUpdate(TxManager txMgr, String sql,
			Object data[]) throws DBException {
	
	   debugSQL(sql, data);
	   long start=System.currentTimeMillis();
	   Connection con = null;
	   CallableStatement cstmt = null;
	
	   try {
	       con = txMgr.getConnection();
	       cstmt = callableStatement(con, sql);
	
	       //pstmt.setQueryTimeout(5);
	       setValues(cstmt, data);
	       int result = cstmt.executeUpdate();
	       if(isQueryMonitorMode()){
	           long end=System.currentTimeMillis();
	           SysMonitor.addMonitorData( new MonitorData("Database",sql,end-start)); 
	       } 
	       return result;
	   }
	   catch (SQLException e) {
	       logSQL(sql, data);
	       LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
	       throw new DBException(e);
	   }
	   catch (Exception e) {
	       logSQL(sql, data);
	       e.printStackTrace();
	       LogManager.error("DB_ERROR",e.toString(),e); LogManager.error(e.toString(),e);
	       throw new SysException(e.toString());
	   }
	   finally {
	       close(cstmt);
	   }
	} 	
    
  

    /**
         쿼리를  수행하고 DBResultSet을 커넥션은 자동을 닫힙니다.
     */
    public static DBResultSet executeQuery(String dbName, String sql) throws
        DBException {
        if (sql == null)return null;
        debugSQL(sql, null);
        long start=System.currentTimeMillis();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        DBResultSet data = null;
        try {

            con = DBManager.getConnection(dbName);
            stmt = createStatement(con);
            rs = stmt.executeQuery(sql);
            openedResultSetCount++;
            data = DBManager.toMemory(rs);
            if(isQueryMonitorMode()){
                long end=System.currentTimeMillis();
                SysMonitor.addMonitorData( new MonitorData("Database",sql,end-start)); 
            } 
        }
        catch (SQLException e) {
            logSQL(sql, null);
            LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
            throw new DBException(dbName,e);
        }
        finally {
            DBManager.close(rs);
            DBManager.close(stmt);
            DBManager.close(con);
        }
        return data;
    }

    /**
         쿼리를  수행하고 DBResultSet을 커넥션은 자동을 닫힙니다.
     */
    public static DBResultSet executeQuery(String sql) throws DBException {
        return executeQuery(null, sql);
    }

    /**
         쿼리를  수행하고 커넥션은 자동을 닫힙니다.
     */
    public static int executeUpdate(String sql) throws DBException {
        return executeUpdate(null, sql);
    }

    /**
         쿼리를  수행하고 커넥션은 자동을 닫힙니다.
     */
    public static int executeUpdate(String dbName, String sql) throws
        DBException {
        if (sql == null)return -1;
        debugSQL(sql, null);
        long start=System.currentTimeMillis();
        Connection con = null;
        Statement stmt = null;
        try {
            con = DBManager.getConnection(dbName);
            stmt = createStatement(con);
            int result= stmt.executeUpdate(sql);
            if(isQueryMonitorMode()){
                long end=System.currentTimeMillis();
                SysMonitor.addMonitorData( new MonitorData("Database",sql,end-start)); 
            } 
            return result;
        }
        catch (SQLException e) {
            logSQL(sql, null);
            LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
            throw new DBException(dbName,e);
        }
        finally {
            DBManager.close(stmt);
            DBManager.close(con);
        }
    }
    
	/**
	 * 쿼리를  수행하고 커넥션은 자동을 닫힙니다.
	 * 처리건수가 0이면 DBException을 던진다.
	 *
	public static int executeUpdateByPK(String dbName, String sql) throws
	   DBException {
	   if (sql == null)return -1;
	   debugSQL(sql, null);
       long start=System.currentTimeMillis();
	   Connection con = null;
	   Statement stmt = null;
	   try {
	       con = DBManager.getConnection(dbName);
	       stmt = createStatement(con);
	       int result = stmt.executeUpdate(sql);
           if( result<=0) throw new DBException("처리 건수가 0건입니다.");
           if(isQueryMonitorMode()){
               long end=System.currentTimeMillis();
               SysMonitor.addMonitorData( new MonitorData("Database",sql,end-start)); 
           } 
           return result;
	   }
	   catch (SQLException e) {
	       logSQL(sql, null);
	       LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
	       throw new DBException(e);
	   }
	   finally {
	       DBManager.close(stmt);
	       DBManager.close(con);
	   }
	} 
    */  

    /**
     * PreparedStatement를 사용하여 Select 쿼리를 간단히 수행 시켜주는 메소드
     */
    public static DBResultSet executePreparedQuery(String sql, Object data[]) throws DBException {
        return executePreparedQuery(null, sql, data, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    }

    /**
     * PreparedStatement를 사용하여 Select 쿼리를 간단히 수행 시켜주는 메소드
     */
    public static DBResultSet executePreparedQuery(String dbName, String sql, 
    		Object data[]) throws DBException {
    	/*
    	 * 2006.04.26 - sjChoi 추가
    	 */
        return executePreparedQuery(dbName, sql, data, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    }    
    
    /**
     * PreparedStatement를 사용하여 Select 쿼리를 간단히 수행 시켜주는 메소드
     */
    public static DBResultSet executePreparedQuery(String sql, Object data[], 
    		int resultSetType, int resultSetConcurrency) throws DBException {
    	/*
    	 * 2006.04.26 - sjChoi 추가
    	 */
        return executePreparedQuery(null, sql, data, resultSetType, resultSetConcurrency);
    }    

    /**
     * PreparedStatement를 사용하여 Select 쿼리를 간단히 수행 시켜주는 메소드
     */
    public static DBResultSet executePreparedQuery(String dbName, String sql, Object data[], 
    		int resultSetType, int resultSetConcurrency) throws DBException {
    	/*
    	 * 2006.04.26 - sjChoi 추가
    	 */
        return executePreparedQuery(dbName, sql, data, resultSetType, resultSetConcurrency, 0);
    }     
    
    /**
     * PreparedStatement를 사용하여 Select 쿼리를 간단히 수행 시켜주는 메소드
     */
    public static DBResultSet executePreparedQuery(String dbName, String sql,
        Object data[], int resultSetType, int resultSetConcurrency, int skipCount) throws DBException {

        debugSQL(sql, data);
        long start=System.currentTimeMillis();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection(dbName);
            
            /*
             * ResultSet의 ResultSetType, ResultSetConcurrency 설정 추가
             * 2006.04.26 - sjChoi 수정
             */
            pstmt = prepareStatement(con, sql, resultSetType, resultSetConcurrency);

            //pstmt.setQueryTimeout(5);
            setValues(pstmt, data);
            rs = pstmt.executeQuery();
            openedResultSetCount++;
            
            /*
             * skip할 행에 대한 매개변수 추가
             * 2006.04.26 - sjChoi 수정
             */
            DBResultSet rset = toMemory(rs, skipCount);
            
            if(isQueryMonitorMode()){
                long end=System.currentTimeMillis();
                SysMonitor.addMonitorData( new MonitorData("Database",sql,end-start)); 
            } 
            return rset;
        }
        catch (SQLException e) {
            logSQL(sql, data);
            LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
            throw new DBException(dbName,e);
        }
        catch (Exception e) {
            logSQL(sql, data);
            LogManager.error("DB_ERROR",e.toString(),e); LogManager.error(e.toString(),e);
            throw new SysException(e.toString());
        }
        finally {
            close(rs);
            close(pstmt);
            close(con);
        }
    }
    /**
     * PreparedStatement를 사용하여 insert,delete, update 쿼리를 간단히 수행 시켜주는 메소드
     * @return 
     */
    public static int executePreparedUpdate(String sql, List params) {
        return executePreparedUpdate(null, sql, params.toArray());
        
    }
    /**
     * PreparedStatement를 사용하여 insert,delete, update 쿼리를 간단히 수행 시켜주는 메소드
     */
    public static int executePreparedUpdate(String sql, Object data[]) throws
        DBException {
        return executePreparedUpdate(null, sql, data);
    }

    /**
         PreparedStatement를 사용하여 insert,delete, update 쿼리를 간단히 수행 시켜주는 메소드
     */
    public static int executePreparedUpdate(String dbName, String sql,
                                            Object data[]) throws DBException {

        debugSQL(sql, data);
        long start=System.currentTimeMillis();
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection(dbName);

            pstmt = prepareStatement(con, sql);

            //pstmt.setQueryTimeout(5);
            setValues(pstmt, data);
            int result = pstmt.executeUpdate();
            if(isQueryMonitorMode()){
                long end=System.currentTimeMillis();
                SysMonitor.addMonitorData( new MonitorData("Database",sql,end-start)); 
            } 
            return result;
        }
        catch (SQLException e) {
            logSQL(sql, data);
            LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
            throw new DBException(dbName,e);
        }
        catch (Exception e) {
            logSQL(sql, data);
            LogManager.error("DB_ERROR",e.toString(),e); LogManager.error(e.toString(),e);
            throw new SysException(e.toString());
        }
        finally {
            close(pstmt);
            close(con);
        }
    }

    /**
     * PreparedStatement를 사용하여 insert,delete, update 쿼리를 간단히 수행 시켜주는 메소드
     * 처리건수가 0이면 DBException을 던진다.
	 */
	public static int executePreparedUpdateByPK(String dbName, String sql,
	                                       Object data[]) throws DBException {
	
	   debugSQL(sql, data);
       long start=System.currentTimeMillis();
	   Connection con = null;
	   PreparedStatement pstmt = null;
	
	   try {
	       con = getConnection(dbName);
	
	       pstmt = prepareStatement(con, sql);
	
	       //pstmt.setQueryTimeout(5);
	       setValues(pstmt, data);
	       
	       int result = pstmt.executeUpdate();
           if( result<=0) throw new DBException("처리 건수가 0건입니다.");
           if(isQueryMonitorMode()){
               long end=System.currentTimeMillis();
               SysMonitor.addMonitorData( new MonitorData("Database",sql,end-start)); 
           } 
           return result;
	   }
	   catch (SQLException e) {
	       logSQL(sql, data);
	       LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
	       throw new DBException(dbName,e);
	   }
	   catch (Exception e) {
	       logSQL(sql, data);
	       LogManager.error("DB_ERROR",e.toString(),e); LogManager.error(e.toString(),e);
	       throw new SysException(e.toString());
	   }
	   finally {
	       close(pstmt);
	       close(con);
	   }
	}    
    
    /**
         PreparedStatement를 사용하여 insert,delete, update 쿼리를 간단히 수행 시켜주는 메소드
         -- 해당 에러코드와 일치하는 경우는 staticTrace를 프린트하지 않는다.
     */
    public static int executePreparedUpdateWithoutTrace(String sql, Object data[], int errCode) throws
        DBException {
        debugSQL(sql, data);
        long start=System.currentTimeMillis();
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();

            pstmt = prepareStatement(con, sql);

            //pstmt.setQueryTimeout(5);
            setValues(pstmt, data);
            int result =  pstmt.executeUpdate();
            if(isQueryMonitorMode()){
                long end=System.currentTimeMillis();
                SysMonitor.addMonitorData( new MonitorData("Database",sql,end-start)); 
            } 
            return result;
        }
        catch (SQLException e) {

            if(e.getErrorCode() != errCode ) {
                logSQL(sql, data);
                LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
            }
            throw new DBException(e);
        }
        catch (Exception e) {
            logSQL(sql, data);
            LogManager.error("DB_ERROR",e.toString(),e); LogManager.error(e.toString(),e);
            throw new SysException(e.toString());
        }
        finally {
            close(pstmt);
            close(con);
        }
    }

    static void setValues(PreparedStatement stmt, Object data[]) throws
        Exception {
        if (data == null || data.length==0){
        	//LogManager.debug("바인드 할 파라미터가 없습니다.");
            return;
        }
        try{
            for (int i = 0; i < data.length; i++) {
            	
            	/*
            	 * sybase의 경우, Number형이 아닌 Integer형으로 set을 해야하는 경우가 있기 때문에 
            	 * Integer 타입을 별도의 if문으로 분리함
            	 * 
            	 * 2006.04.26 - sjChoi 수정
            	 */
            	if (data[i] instanceof Integer) {
            		stmt.setInt(i + 1, Integer.parseInt(data[i].toString()));
            	}
                else if (data[i] instanceof Number) {
                    stmt.setBigDecimal(i + 1, new BigDecimal(data[i].toString()));
                }
                else if (data[i] instanceof String) {
                    if ( ( (String) data[i]).getBytes().length < 2000) {
                        stmt.setString(i + 1, (String) data[i]);
                    }
                    else {
                        stmt.setCharacterStream(i + 1,
                                                new StringReader( (String) data[
                            i]),
                                                ( (String) data[i]).length());
                    }
                }
                else if (data[i] instanceof java.sql.Date) {
                    stmt.setDate(i + 1, (java.sql.Date) data[i]);
                }
                else if (data[i] instanceof java.sql.Timestamp) {
                    stmt.setTimestamp(i + 1, (java.sql.Timestamp) data[i]);
                }
                else if (data[i] instanceof byte[]) {
                    stmt.setBytes(i + 1, (byte[]) data[i]);
                }
                else if (data[i] instanceof InputStream) {
                    stmt.setBinaryStream(i + 1, (InputStream) data[i],
                                         ( (InputStream) data[i]).available());
                }
                else if (data[i] instanceof Boolean) {
                    stmt.setBoolean(i + 1, ( (Boolean) data[i]).booleanValue());
                }
                else if (data[i] instanceof Null) {
                    stmt.setNull(i + 1, ( (Null) data[i]).type);
                }
                else if (data[i] == null) {
                    throw new SysException((i+1)+"번째 인수가 null입니다.");
                }else {
                	throw new SysException((i+1)+"번째 인수는 지원되지 않는 타입 입니다.\n"
                			+data[i].getClass().getName());
                }
            }
        }catch(Exception e){
            LogManager.error("ERROR", "DBManager.setValue error::"+e.toString(), e);
            throw e;
        }
    }

    /**
     * DBResultSet을 Bean을 담고 있는 ArrayList형태로 변환
     */
    public static ArrayList toList(DBResultSet rs, Class clazz) {
        ArrayList list = new ArrayList();
        try {
            Map map = new HashMap();
            while (rs.next()) {
                Object bean = null;
                bean = clazz.newInstance();
                for (int i = 1; i <= rs.getColumnCount(); i++) {
                    String attrName = StringUtil.db2attr(rs.getColumnName(i));
                    map.put(attrName, rs.getObject(i));
                }
                BeanUtils.populate(bean, map);

                list.add(bean);
                //LogManager.debug("추가될  data-->"+map);
                map.clear();
            }

        }
        catch (Exception e) {
            LogManager.error("ERROR", "해당 Bean 객체에 default 생성자 및 setXXX함수가 있는지 확인 하세요");
            throw new SysException("객체 생성에 실패하였습니다\n원인-->" + e.getMessage());
        }
        return list;
    }

    /**
     * ResultSet을 Bean을 담고 있는 ArrayList형태로 변환
     */
    public static ArrayList toList(ResultSet rs, Class clazz) {

        if (rs == null)throw new DBException("ResultSet is null");
        ResultSetMetaData metaData = null;
        try {

            String[] columnNames = null;

            metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            columnNames = new String[columnCount];

            for (int i = 0; i < columnCount; i++) {
                columnNames[i] = metaData.getColumnName(i + 1);
            }

            Map map = new HashMap();

            ArrayList list = new ArrayList(10);

            while (rs.next()) {
                Object bean = null;
                bean = clazz.newInstance();
//                LogManager.debug("비어 있는 객체 생성 성공");
                for (int i = 0; i < columnNames.length; i++) {

                    if(rs.getObject(columnNames[i]) == null){
//                		LogManager.debug("NULL....SKIP SET TO MAP");
                        continue;
                    }

                    String attrName = StringUtil.db2attr(columnNames[i]);
//                    LogManager.debug("Name-->"+attrName);

                    map.put(attrName,rs.getObject(columnNames[i]) );
//                    LogManager.debug("Value-->"+rs.getObject(columnNames[i]));
                }

//                LogManager.debug("data-->" + map);

                BeanUtils.populate(bean, map);

                list.add(bean);
                //LogManager.debug("추가될  data-->"+map);
                map.clear();
            }
//            LogManager.debug("조회된 data\n" + list.toString());
            return list;

        }
        catch (Exception e) {
            LogManager.error("DB_ERROR",e.toString(),e); LogManager.error(e.toString(),e);
            throw new SysException("데이타 객체 생성에 실패하였습니다\n원인-->" + e.getMessage());
        }

    }

    /**
     * ResultSet을 Bean을 담고 있는  변환
     */
    public static Map toMap(ResultSet rs) {

        if (rs == null)throw new DBException("ResultSet is null");
        ResultSetMetaData metaData = null;
        try {

            String[] columnNames = null;

            metaData = rs.getMetaData();

            int columnCount = metaData.getColumnCount();

            columnNames = new String[columnCount];
            for (int j = 0; j < columnCount; j++) {
                columnNames[j] = metaData.getColumnName(j + 1);
            }

            Map map = new HashMap();
            if (rs.next()) {

                for (int p = 0; p < columnNames.length; p++) {
                    String attrName = columnNames[p];
                    map.put(attrName, rs.getObject(columnNames[p]));
                }
            }else{
                LogManager.debug("toMap의 생성 데이타 없습니다.") ;
            }
            LogManager.debug("조회된 data\n" + map.toString());
            return map;
        }
        catch (Exception e) {
            throw new SysException("데이타 Map 생성에 실패하였습니다\n원인-->" + e.getMessage());
        }
    }

    /**
     * ResultSet을 Bean을 담고 있는 ArrayList형태로 변환
     */
    public static Map toMap(DBResultSet rs) {

        if (rs == null)throw new DBException("ResultSet is null");

        try {

            String[] columnNames = null;

            int columnCount = rs.getColumnCount();
            columnNames = new String[columnCount];

            Map map = new DataMap();
            if (rs.next()) {
                for (int i = 0; i < columnNames.length; i++) {
                    String attrName = rs.getColumnName(i);
                    map.put(attrName, rs.getObject(i));
                }
            }
            LogManager.debug("조회된 data\n" + map.toString());
            return map;
        }
        catch (Exception e) {
            throw new SysException("데이타 Map 생성에 실패하였습니다\n원인-->" + e.getMessage());
        }
    }

    /**
     * DBResultSet을 Bean을 담고 있는 ArrayList형태로 변환
     */
    public static ArrayList toMapList(DBResultSet rs) {
        ArrayList list = new ArrayList();
        try {
            Map map = null;
            while (rs.next()) {
                map = new DataMap();
                for (int i = 1; i <= rs.getColumnCount(); i++) {
                    String attrName = StringUtil.db2attr(rs.getColumnName(i));
                    map.put(attrName, rs.getObject(i));
                }
                list.add(map);
            }

        }
        catch (Exception e) {
            throw new SysException("멥 저장에 실패하였습니다\n원인-->" + e.getMessage());
        }
        return list;
    }

    /**
     * ResultSet을 Map을 담고 있는 ArrayList형태로 변환
     */
    public static ArrayList toMapList(ResultSet rs) {

        if (rs == null)
            throw new DBException("ResultSet is null");
        ResultSetMetaData metaData = null;
        try {
            String[] columnNames = null;

            metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            columnNames = new String[columnCount];

            for (int i = 0; i < columnCount; i++) {
                columnNames[i] = metaData.getColumnName(i + 1);
            }

            Map map = null;
            ArrayList list = new ArrayList(10);

            while (rs.next()) {
                map = new DataMap();
                for (int i = 0; i < columnNames.length; i++) {
                    map.put(columnNames[i], rs.getObject(columnNames[i]));
                    /*
                    if (metaData.getColumnType(i + 1) != Types.CLOB) {
                        map.put(columnNames[i], rs.getObject(columnNames[i]));
                    } else {
                        // read content column from CLOB TYPE
                        map.put(columnNames[i], dumpClob((CLOB) rs.getClob(columnNames[i])));
                    }
                    */
                }
                list.add(map);
            }

            // LogManager.debug("조회된 data\n" + list.toString());
            return list;
        } catch (Exception e) {
            LogManager.error("ERROR", "해당 Bean 객체에 default 생성자 및 setXXX함수가 있는지 확인 하세요");
            throw new SysException("데이타 객체 생성에 실패하였습니다\n원인-->" + e.getMessage());
        }
    }
    
    /**
     * CLOB 컬럼의 데이타를 String으로 읽어온다.
     * 
     * @param clob
     * @return @throws
     *         Exception
     */
    /*
    public static String dumpClob(CLOB clob) throws Exception {
        if (clob == null)
            return null;

        StringBuffer clobString = new StringBuffer();
        Reader in = clob.getCharacterStream();
        char[] buffer = new char[1024];
        int length = 0;

        while ((length = in.read(buffer, 0, 1024)) != -1) {
            clobString.append(buffer, 0, length);
        }

        in.close();
        return clobString.toString();
    }
    */

    /**
     * ResultSet을 Object[]로 담고 있는 ArrayList형태로 변환
     */
    public static ArrayList toArrayList(ResultSet rs) {

        if (rs == null)throw new DBException("ResultSet is null");
        ResultSetMetaData metaData = null;
        try {
            String[] columnNames = null;

            metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            columnNames = new String[columnCount];

            for (int i = 0; i < columnCount; i++) {
                columnNames[i] = metaData.getColumnName(i + 1);
            }

            ArrayList tempArr = new ArrayList(10);
            ArrayList list = new ArrayList(10);

            while (rs.next()) {
                tempArr.clear();
                for (int i = 0; i < columnNames.length; i++) {
                    //map.put(columnNames[i], rs.getObject(columnNames[i]));
                    tempArr.add(rs.getObject(columnNames[i]));
                }
                list.add(tempArr.toArray());
            }
            
            //LogManager.debug("조회된 data\n" + list.toString());
            return list;

        }
        catch (Exception e) {
            //LogManager.error("해당 Bean 객체에 default 생성자 및 setXXX함수가 있는지 확인 하세요");
            throw new SysException("데이타 객체 생성에 실패하였습니다\n원인-->" + e.getMessage());
        }

    }
    public static void debugSQL(String sql, Object[] data) {
        if(!isQueryMonitorMode()) return;
        
        String loggingSql = sql;
        if (data != null) {
            loggingSql = getReplacedSql(sql, data);
        }
        LogManager.info(loggingSql);
    }

    public static void logSQL(String sql, Object[] data) {
        String loggingSql = sql;
        if (data != null) {
            loggingSql = getReplacedSql(sql, data);
        }
        LogManager.info("DB_ERROR", loggingSql);
    }

    public static String getReplacedSql(String sql, Object[] data) {
        String loggingSql = sql;
        try {
            if (data != null) {
                for (int i = 0; i < data.length; i++) {
                    loggingSql = StringUtil.replace(loggingSql, "?",
                        "'" + data[i].toString().replace('?','^') + "'");
                }
                loggingSql = "Prepared sql : " + sql + "\n 값넣은 sql :" +
                    loggingSql.replace('^','?');
            }

        }
        catch (Exception e) {}

        return loggingSql;
    }

    /*
     *-------------------------------------------
     * 외환은행에서 사용하는 메소드들(TrustForm)
     *-------------------------------------------
     */

    /**
     * 해당 쿼리에 대하여 결과를 Map을 포함하는 ArrayList로 반환하는 함수
     * DB가 디폴트 지정인 경우
     * @param sql String
     * @throws DBException
     * @return ArrayList
     */
    public static ArrayList executeQueryToMapList(String sql) throws
        DBException {
        return executeQueryToMapList(null, sql);
    }

    /**
     * 해당 쿼리에 대하여 결과를 Objec[]을 포함하는 ArrayList로 반환하는 함수
     * DB가 디폴트 지정인 경우
     * @param sql String
     * @throws DBException
     * @return ArrayList
     */
    public static ArrayList executeQueryToArrayList(String sql) throws
        DBException {
        return executeQueryToArrayList(null, sql);
    }
    
    /**
     * DB와 쿼리에 대한 결과를 Map을 포함하는 ArrayList타입으로 반환하는 함수
     * @param dbName String
     * @param sql String
     * @throws DBException
     * @return ArrayList
     */
    public static ArrayList executeQueryToMapList(String dbName, String sql) throws
        DBException {

        debugSQL(sql, null);
        long start=System.currentTimeMillis();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        ArrayList dataArr = null;

        try {
            con = getConnection(dbName);
            stmt = createStatement(con);
            rs = stmt.executeQuery(sql);

            openedResultSetCount++;

            dataArr = toMapList(rs);
            if(isQueryMonitorMode()){
            long end=System.currentTimeMillis();
            SysMonitor.addMonitorData( new MonitorData("Database",sql,end-start)); 
            } 
            return dataArr;
        }
        catch (SQLException e) {
            logSQL(sql, null);
            LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
            throw new DBException(dbName,e);
        }
        catch (Exception e) {
            logSQL(sql, null);
            LogManager.error("DB_ERROR",e.toString(),e); LogManager.error(e.toString(),e);
            throw new SysException(e.toString());
        }
        finally {
            close(rs);
            close(stmt);
            close(con);
        }
    }

    /**
     * DB와 쿼리에 대한 결과를 Object[]을 포함하는 ArrayList타입으로 반환하는 함수
     * @param dbName String
     * @param sql String
     * @throws DBException
     * @return ArrayList
     */
    public static ArrayList executeQueryToArrayList(String dbName, String sql) throws
        DBException {

        debugSQL(sql, null);
        long start=System.currentTimeMillis();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        ArrayList dataArr = null;

        try {
            con = getConnection(dbName);
            stmt = createStatement(con);
            rs = stmt.executeQuery(sql);

            openedResultSetCount++;

            dataArr = toArrayList(rs);
            if(isQueryMonitorMode()){
            long end=System.currentTimeMillis();
            SysMonitor.addMonitorData( new MonitorData("Database",sql,end-start)); 
            } 
            return dataArr;
        }
        catch (SQLException e) {
            logSQL(sql, null);
            LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
            throw new DBException(dbName,e);
        }
        catch (Exception e) {
            logSQL(sql, null);
            LogManager.error("DB_ERROR",e.toString(),e); LogManager.error(e.toString(),e);
            throw new SysException(e.toString());
        }
        finally {
            close(rs);
            close(stmt);
            close(con);
        }
    }
    /**
     * 쿼리와 파라미터를 받아 Map을 포함하는 ArrayList로 반환하는 함수
     * DB가 디폴트인 경우
     * @param sql String
     * @param data Object[]
     * @throws DBException
     * @return ArrayList
     */
    public static ArrayList executePreparedQueryToMapList(String sql,
        Object data[]) throws DBException {
        return executePreparedQueryToMapList(null, sql, data);
    }

    /**
     * 쿼리와 파라미터를 받아 Obejct[]을 포함하는 ArrayList로 반환하는 함수
     * DB가 디폴트인 경우
     * @param sql String
     * @param data Object[]
     * @throws DBException
     * @return ArrayList
     */
    public static ArrayList executePreparedQueryToArrayList(String sql,
        Object data[]) throws DBException {
        return executePreparedQueryToArrayList(null, sql, data);
    }
    
    /**
     * 쿼리와 파라미터를 받아 Map을 포함하는 ArrayList로 반환하는 함수
     * @param dbName String
     * @param sql String
     * @param data Object[]
     * @throws DBException
     * @return ArrayList
     */
    public static ArrayList executePreparedQueryToMapList(String dbName,
        String sql, Object data[]) throws DBException {

        debugSQL(sql, data);
        long start=System.currentTimeMillis();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList dataArr = null;
        try {
            con = getConnection(dbName);
            pstmt = prepareStatement(con, sql);

            //pstmt.setQueryTimeout(5);
            setValues(pstmt, data);
            rs = pstmt.executeQuery();
            openedResultSetCount++;
            dataArr = toMapList(rs);
            if(isQueryMonitorMode()){
            long end=System.currentTimeMillis();
            SysMonitor.addMonitorData( new MonitorData("Database",sql,end-start)); 
            } 
            return dataArr;
        }
        catch (SQLException e) {
            logSQL(sql, data);
            LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
            throw new DBException(dbName,e);
        }
        catch (Exception e) {
            logSQL(sql, data);
            LogManager.error("DB_ERROR",e.toString(),e); LogManager.error(e.toString(),e);
            throw new SysException(e.toString());
        }
        finally {
            close(rs);
            close(pstmt);
            close(con);
        }
    }

    /**
     * 쿼리와 파라미터를 받아 Object[]을 포함하는 ArrayList로 반환하는 함수
     * @param dbName String
     * @param sql String
     * @param data Object[]
     * @throws DBException
     * @return ArrayList
     */
    public static ArrayList executePreparedQueryToArrayList(String dbName,
        String sql, Object data[]) throws DBException {

        debugSQL(sql, data);
        long start=System.currentTimeMillis();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList dataArr = null;
        try {
            con = getConnection(dbName);
            pstmt = prepareStatement(con, sql);

            //pstmt.setQueryTimeout(5);
            setValues(pstmt, data);
            rs = pstmt.executeQuery();
            openedResultSetCount++;
            dataArr = toArrayList(rs);
            if(isQueryMonitorMode()){
            long end=System.currentTimeMillis();
            SysMonitor.addMonitorData( new MonitorData("Database",sql,end-start)); 
            } 
            return dataArr;
        }
        catch (SQLException e) {
            logSQL(sql, data);
            LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
            throw new DBException(dbName,e);
        }
        catch (Exception e) {
            logSQL(sql, data);
            LogManager.error("DB_ERROR",e.toString(),e); LogManager.error(e.toString(),e);
            throw new SysException(e.toString());
        }
        finally {
            close(rs);
            close(pstmt);
            close(con);
        }
    }
    
    /**
     * 쿼리에 대한 결과(단독)를 Map형태로 반환하는 함수
     * DB가 디폴트인 경우
     * @param sql String
     * @throws DBException
     * @return Map
     */
    public static Map executeQueryToMap(String sql) throws DBException {
        return executeQueryToMap(null, sql);
    }

    /**
     * 쿼리에 대한 결과(단독)를 Map형태로 반환하는 함수
     * @param sql String
     * @throws DBException
     * @return Map
     */
    public static Map executeQueryToMap(String dbName, String sql) throws
        DBException {
        ArrayList data = executeQueryToMapList(dbName, sql);
        if (data.size() == 0) {
            LogManager.info("조회된 데이타가 없습니다");
            return null;
        }
        else {
            return (Map) (data.get(0));
        }
    }

    /**
     * 쿼리를  수행하고 ArrayList 리턴 커넥션은 자동을 닫힙니다.
     */
    public static Map executePreparedQueryToMap(String sql, Object[] param) throws
        DBException {
        return executePreparedQueryToMap(null, sql, param);
    }

    /**
     * 쿼리를  수행하고 ArrayList 리턴 커넥션은 자동을 닫힙니다.
     */
    public static Map executePreparedQueryToMap(String dbName, String sql,
                                                Object[] data) throws
        DBException {

        debugSQL(sql, data);
        long start=System.currentTimeMillis();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Map dataMap = null;
        try {
            con = getConnection(dbName);
            pstmt = prepareStatement(con, sql);
            setValues(pstmt, data);
            rs = pstmt.executeQuery();
            openedResultSetCount++;
            dataMap = toMap(rs);
            if(isQueryMonitorMode()){
            long end=System.currentTimeMillis();
            SysMonitor.addMonitorData( new MonitorData("Database",sql,end-start)); 
            } 
            return dataMap;
        }
        catch (SQLException e) {
            logSQL(sql, data);
            LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
            throw new DBException(dbName,e);
        }
        catch (Exception e) {
            logSQL(sql, data);
            LogManager.error("DB_ERROR",e.toString(),e); LogManager.error(e.toString(),e);
            throw new SysException(e.toString());
        }
        finally {
            close(rs);
            close(pstmt);
            close(con);
        }
    }

    /*
     * -----------------------------------------
     * 이하 외환은행에서 사용되지 않는 메소드들
     * -----------------------------------------
     */

    /**
     * 쿼리를  수행하고 ArrayList 리턴 커넥션은 자동을 닫힙니다.
     */
    public static ArrayList executeQueryToBeanList(String dbName, String sql,
        Class clazz) throws DBException {
        if (sql == null)return null;

        debugSQL(sql, null);
        long start=System.currentTimeMillis();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        ArrayList data = null;
        try {
            con = DBManager.getConnection(dbName);
            stmt = createStatement(con);
            rs = stmt.executeQuery(sql);
            openedResultSetCount++;
            data = DBManager.toList(rs, clazz);
            if(isQueryMonitorMode()){
            long end=System.currentTimeMillis();
            SysMonitor.addMonitorData( new MonitorData("Database",sql,end-start)); 
            } 
        }
        catch (SQLException e) {
            logSQL(sql, null);
            LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
            throw new DBException(dbName,e);
        }
        finally {
            DBManager.close(rs);
            DBManager.close(stmt);
            DBManager.close(con);
        }
        return data;
    }

    /**
     * 쿼리를  수행하고 ArrayList 리턴 커넥션은 자동을 닫힙니다.
     */
    public static Object executeQueryToBean(String sql, Class clazz) throws
        DBException {
        return executeQueryToBean(null, sql, clazz);
    }

    /**
     * 쿼리를  수행하고 ArrayList 리턴 커넥션은 자동을 닫힙니다.
     */
    public static Object executeQueryToBean(String dbName, String sql,
                                            Class clazz) throws DBException {
        ArrayList data = executeQueryToBeanList(dbName, sql, clazz);
        if (data.size() == 0) {
            LogManager.info("조회된 데이타가 없습니다");
            return null;
        }
        else {
            return data.get(0);
        }
    }

    /**
     * 쿼리를  수행하고 ArrayList 리턴 커넥션은 자동을 닫힙니다.
     */
    public static Object executePreparedQueryToBean(String sql,
        Object[] param, Class clazz) throws DBException {
        return executePreparedQueryToBean(null, sql, param, clazz);
    }

    /**
     * 쿼리를  수행하고 ArrayList 리턴 커넥션은 자동을 닫힙니다.
     */
    public static Object executePreparedQueryToBean(String dbName, String sql,
        Object[] param, Class clazz) throws DBException {
        ArrayList data = executePreparedQueryToBeanList(dbName, sql, param,
            clazz);
        if (data.size() == 0) {
            LogManager.info("조회된 데이타가 없습니다");
            return null;
        }
        else {
            return data.get(0);
        }
    }

    /**
     * 쿼리를  수행하고 ArrayList 리턴 커넥션은 자동을 닫힙니다.
     */
    public static ArrayList executeQueryToBeanList(String sql, Class clazz) throws
        DBException {
        return executeQueryToBeanList(null, sql, clazz);
    }

    /**
         PreparedStatement를 사용하여 Select 쿼리를 간단히 수행 시켜주는 메소드
     */
    public static ArrayList executePreparedQueryToBeanList(String dbName,
        String sql, Object data[],
        Class clazz) throws DBException {

        debugSQL(sql, data);
        long start=System.currentTimeMillis();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection(dbName);
            pstmt = prepareStatement(con, sql);

            //pstmt.setQueryTimeout(5);
            setValues(pstmt, data);
            rs = pstmt.executeQuery();
            openedResultSetCount++;
            ArrayList list = toList(rs, clazz);
            if(isQueryMonitorMode()){
            long end=System.currentTimeMillis();
            SysMonitor.addMonitorData( new MonitorData("Database",sql,end-start)); 
            } 
            return list;
        }
        catch (SQLException e) {
            logSQL(sql, data);
            LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
            throw new DBException(dbName,e);
        }
        catch (Exception e) {
            logSQL(sql, data);
            LogManager.error("DB_ERROR",e.toString(),e); LogManager.error(e.toString(),e);
            throw new SysException(e.toString());
        }
        finally {
            close(rs);
            close(pstmt);
            close(con);
        }
    }

    /**
         PreparedStatement를 사용하여 Select 쿼리를 간단히 수행 시켜주는 메소드
     */
    public static ArrayList executePreparedQueryToBeanList(String sql,
        Object data[], Class clazz) throws DBException {
        return executePreparedQueryToBeanList(null, sql, data, clazz);
    }
    
    
    /**
     * PreparedStatement를 사용하여 Select 쿼리를 간단히 수행 시켜주는 메소드
     * 
     * 동일 트랜잭션 내에서 update 한 결과에 대하여 select 하기 위해
     * 같은 Connection 으로 질의를 수행한다.
     * 
     * @author 이종원
     * @since 2004.10.05
     */
    public static DBResultSet executePreparedTxQuery(TxManager txMgr, String sql, Object data[]) throws DBException {
    
       debugSQL(sql, data);
       long start=System.currentTimeMillis();
       Connection con = null;
       PreparedStatement pstmt = null;
       ResultSet rs = null;
    
       try {
           con = txMgr.getConnection();
           pstmt = prepareStatement(con, sql);
    
           //pstmt.setQueryTimeout(5);
           setValues(pstmt, data);
           rs = pstmt.executeQuery();
           openedResultSetCount++;
           DBResultSet rset = toMemory(rs);
           if(isQueryMonitorMode()){
            long end=System.currentTimeMillis();
           SysMonitor.addMonitorData( new MonitorData("Database",sql,end-start)); 
            } 
           return rset;
       } catch (SQLException e) {
           logSQL(sql, data);
           LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
           throw new DBException(e);
       } catch (Exception e) {
           logSQL(sql, data);
           LogManager.error("DB_ERROR",e.toString(),e); LogManager.error(e.toString(),e);
           throw new SysException(e.toString());
       } finally {
           close(rs);
           close(pstmt);
       }//end try catch finally
    }//end of executePreparedTxQuery()
    
    /**
     * 쿼리를  수행하고 DBResultSet을 커넥션은 자동을 닫힙니다.
     * 
     * 동일 트랜잭션 내에서 update 한 결과에 대하여 select 하기 위해
     * 같은 Connection 으로 질의를 수행한다.
     * 
     * @author 이종원
     * @since 2004.10.05
     */
    public static DBResultSet executeTxQuery(TxManager txMgr, String sql) throws DBException {
       
       debugSQL(sql, null);
       long start=System.currentTimeMillis();
       Connection con = null;
       Statement stmt = null;
       ResultSet rs = null;
       
       try {
    
           con = txMgr.getConnection();
           stmt = createStatement(con);
           
           rs = stmt.executeQuery(sql);
           openedResultSetCount++;
           DBResultSet rset = DBManager.toMemory(rs);
           if(isQueryMonitorMode()){
            long end=System.currentTimeMillis();
           SysMonitor.addMonitorData( new MonitorData("Database",sql,end-start)); 
            } 
           return rset;
       } catch (SQLException e) {
           logSQL(sql, null);
           LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
           throw new DBException(e);
       } catch (Exception e) {
           logSQL(sql, null);
           LogManager.error("DB_ERROR",e.toString(),e); LogManager.error(e.toString(),e);
           throw new SysException(e.toString());
       } finally {
           close(rs);
           close(stmt);
       }//end try catch finally
    }//end of executeTxQuery()
    

    /**
     트렌젝션 관리를 위해 하나의 커넥션을 공유하여 PreparedStatement를 사용하여 insert,delete, update 쿼리를 간단히 수행 시켜주는 메소드
     */
    public static  int executePreparedTxUpdate(TxManager txMgr , String sql, Object data[]) throws DBException {

        debugSQL(sql,data);
        long start=System.currentTimeMillis();
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = txMgr.getConnection();
            pstmt = prepareStatement(con,sql);

            //pstmt.setQueryTimeout(5);
            setValues(pstmt, data);
            int result =  pstmt.executeUpdate();
            if(isQueryMonitorMode()){
            long end=System.currentTimeMillis();
            SysMonitor.addMonitorData( new MonitorData("Database",sql,end-start)); 
            } 
            return result;
        } catch(SQLException e) {
            //txMgr.rollback(); //개발자가 책임지고 rollback 한다.
            logSQL(sql,data);
            LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
            throw new DBException(e);
        } catch(Exception e) {
            logSQL(sql,data);
            LogManager.error("DB_ERROR",e.toString(),e); LogManager.error(e.toString(),e);
            throw new SysException(e.toString());
        } finally {
            close(pstmt);
        }
    }
    
    /**
     * 트렌젝션 관리를 위해 하나의 커넥션을 공유하여 PreparedStatement를 사용하여 insert,delete, update 쿼리를 간단히 수행 시켜주는 메소드
     * 처리건수가 0이면 DBException을 던진다.
     */
   public static  int executePreparedTxUpdateByPK(TxManager txMgr , String sql, Object data[]) throws DBException {

       debugSQL(sql,data);
       long start=System.currentTimeMillis();
       Connection con = null;
       PreparedStatement pstmt = null;

       try {
           con = txMgr.getConnection();
           pstmt = prepareStatement(con,sql);

           //pstmt.setQueryTimeout(5);
           setValues(pstmt, data);
           int result = pstmt.executeUpdate();
           if( result<=0) throw new DBException("처리 건수가 0건입니다.");
           if(isQueryMonitorMode()){
            long end=System.currentTimeMillis();
           SysMonitor.addMonitorData( new MonitorData("Database",sql,end-start)); 
            } 
           return result;
           
       } catch(SQLException e) {
           //txMgr.rollback(); //개발자가 책임지고 rollback 한다.
           logSQL(sql,data);
           LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
           throw new DBException(e);
       } catch(Exception e) {
           logSQL(sql,data);
           LogManager.error("DB_ERROR",e.toString(),e); LogManager.error(e.toString(),e);
           throw new SysException(e.toString());
       } finally {
           close(pstmt);
       }
   }
    /**
     * 트렌젝션 관리를 위해 하나의 커넥션을 공유하여 Statement를 사용하여 
     * insert,delete, update 쿼리를 간단히 수행 시켜주는 메소드
     */
    public static  int executeTxUpdate(TxManager txMgr , String sql ) throws DBException {

    	debugSQL(sql,null);
        long start=System.currentTimeMillis();
    	Connection con = null;
    	Statement pstmt = null;

    	try {
    		con = txMgr.getConnection();
    		pstmt = createStatement(con);

    		int result =  pstmt.executeUpdate(sql);
            if(isQueryMonitorMode()){
            long end=System.currentTimeMillis();
            SysMonitor.addMonitorData( new MonitorData("Database",sql,end-start)); 
            } 
            return result;
    	} catch(SQLException e) {
    		//txMgr.rollback(); //개발자가 책임지고 rollback 한다.
    		logSQL(sql,null);
    		LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
    		throw new DBException(e);
    	} catch(Exception e) {
    		logSQL(sql,null);
    		LogManager.error("DB_ERROR",e.toString(),e); LogManager.error(e.toString(),e);
    		throw new SysException(e.toString());
    	} finally {
    		close(pstmt);
    	}
    }
    
    /**
     * 트렌젝션 관리를 위해 하나의 커넥션을 공유하여 Statement를 사용하여 
     * insert,delete, update 쿼리를 간단히 수행 시켜주는 메소드
     * 처리건수가 0이면 DBException을 던진다.
     */
    public static  int executeTxUpdateByPK(TxManager txMgr , String sql ) throws DBException {

    	debugSQL(sql,null);
        long start=System.currentTimeMillis();
    	Connection con = null;
    	Statement pstmt = null;

    	try {
    		con = txMgr.getConnection();
    		pstmt = createStatement(con);

    		int result = pstmt.executeUpdate(sql);
            if( result<=0) throw new DBException("처리 건수가 0건입니다.");
            if(isQueryMonitorMode()){
            long end=System.currentTimeMillis();
            SysMonitor.addMonitorData( new MonitorData("Database",sql,end-start)); 
            } 
            return result;
            
    	} catch(SQLException e) {
    		//txMgr.rollback(); //개발자가 책임지고 rollback 한다.
    		logSQL(sql,null);
    		LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
    		throw new DBException(e);
    	} catch(Exception e) {
    		logSQL(sql,null);
    		LogManager.error("DB_ERROR",e.toString(),e); LogManager.error(e.toString(),e);
    		throw new SysException(e.toString());
    	} finally {
    		close(pstmt);
    	}
    }
    /**
     트렌젝션 관리를 위해 하나의 커넥션을 공유하여 PreparedStatement를 사용하여 insert,delete, update 쿼리를 간단히 수행 시켜주는 메소드
     -- 해당 에러코드와 일치하는 경우는 staticTrace를 프린트하지 않는다.
     */
    public static  int executePreparedTxUpdateWithoutTrace(TxManager txMgr , String sql, Object data[], int errCode) throws DBException {

        debugSQL(sql,data);
        long start=System.currentTimeMillis();
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = txMgr.getConnection();
            pstmt = prepareStatement(con,sql);

            //pstmt.setQueryTimeout(5);
            setValues(pstmt, data);
            int result =  pstmt.executeUpdate();
            if(isQueryMonitorMode()){
            long end=System.currentTimeMillis();
            SysMonitor.addMonitorData( new MonitorData("Database",sql,end-start)); 
            } 
            return result;
        } catch(SQLException e) {
            //txMgr.rollback(); //개발자가 책임지고 rollback 한다.
            if(e.getErrorCode()!=errCode) {
                logSQL(sql,data);
                LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
            }
            throw new DBException(e);
        } catch(Exception e) {
            logSQL(sql,data);
            LogManager.error("DB_ERROR",e.toString(),e); LogManager.error(e.toString(),e);
            throw new SysException(e.toString());
        } finally {
            close(pstmt);
        }
    }

    /**
    * default db에 한번에 10건씩 batch 처리를 한다.
    **/
    public  static void executeBatch(String sql, ArrayList paramList){
        executeBatch(null, sql, paramList);
     }
    /**
     * 해당 db에 한번에 10건씩 batch 처리를 한다.
     **/
    public  static void executeBatch(String dbName,String sql, ArrayList paramList){
        executeBatch(dbName, sql, paramList,10);
    }

    /**
	 * 해당 db에 한번에 100건씩 batch 처리를 한다. 
	 * 
	 * @param dbName
	 * @param sql
	 * @param dataSet 
	 * @param executeUnitCount 실행할 배치 카운트
	 */
    public static void executeBatch(String dbName, String sql , DataSet dataSet, int executeUnitCount) {
    	LogManager.debug("##### "+dataSet.getClass().getName()+" #####");
    	
    	if(dataSet instanceof DataSet) {
        	ArrayList list = new ArrayList();
        	
    		while(dataSet.next()){
    			//DataMap map = dataSet.toDataMap();
    			Object obj = dataSet.getCurrentRow();	
    			list.add(obj);
    		}
        	executeBatch( dbName, sql , list ,executeUnitCount);    		
    	}

    }
    
    /**
    *  해당 db에 한번에 executeUnitCount건씩 batch 처리를 한다.
    **/
    public  static void executeBatch(String dbName,String sql, ArrayList paramList,int executeUnitCount){

        debugSQL(sql,null);
        long start=System.currentTimeMillis();
        Connection con = null;
        PreparedStatement pstmt = null;
        TxManager txMgr=null;
        int paramSize = paramList.size();
        Object[] data = null;
        if(paramSize==0) {
            LogManager.info("수행할 파라미터에 값이 업습니다.");
            return ;
        }
        
        try {
            txMgr = new TxManager(dbName);
            txMgr.begin();
            con = txMgr.getConnection();
            pstmt = prepareStatement(con,sql);
            boolean hasNext=false;
            for(int i=0;i<paramSize;i++){
                pstmt.clearParameters();
                data =(Object[]) paramList.get(i);
                setValues(pstmt, data);
                pstmt.addBatch();
                hasNext=true;
                if(i%executeUnitCount==(executeUnitCount-1)){
                    pstmt.executeBatch();
                    hasNext=false;
                }
            }
            if(hasNext){
                pstmt.executeBatch();
            }
            txMgr.commit();
            if(isQueryMonitorMode()){
	            long end=System.currentTimeMillis();
	            SysMonitor.addMonitorData( new MonitorData("Database",sql,end-start)); 
            }      
        } catch(SQLException e) {
            txMgr.rollback();
            logSQL(sql,data);
            LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
            throw new DBException(dbName,e);
        } catch(Exception e) {
            txMgr.rollback();
            logSQL(sql,data);
            LogManager.error("DB_ERROR",e.toString(),e); LogManager.error(e.toString(),e);
            throw new SysException(e.toString());
        } finally {
            close(pstmt);
            txMgr.end();
        }
    }
    
    
    public  static void executeCallableBatch(String dbName,String sql, ArrayList paramList,int executeUnitCount){

        debugSQL(sql,null);
        long start=System.currentTimeMillis();
        Connection con = null;
 	    CallableStatement cstmt = null;
        TxManager txMgr=null;
        int paramSize = paramList.size();
        Object[] data = null;
        if(paramSize==0) {
            LogManager.info("수행할 파라미터에 값이 업습니다.");
            return ;
        }
        
        try {
            txMgr = new TxManager(dbName);
            txMgr.begin();
            con = txMgr.getConnection();
 	        cstmt = callableStatement(con, sql);
            boolean hasNext=false;
            for(int i=0;i<paramSize;i++){
            	cstmt.clearParameters();
                data =(Object[]) paramList.get(i);
                setValues(cstmt, data);
                cstmt.addBatch();
                hasNext=true;
                if(i%executeUnitCount==(executeUnitCount-1)){
                	cstmt.executeBatch();
                    hasNext=false;
                }
            }
            if(hasNext){
            	cstmt.executeBatch();
            }
            txMgr.commit();
            if(isQueryMonitorMode()){
	            long end=System.currentTimeMillis();
	            SysMonitor.addMonitorData( new MonitorData("Database",sql,end-start)); 
            }      
        } catch(SQLException e) {
            txMgr.rollback();
            logSQL(sql,data);
            LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
            throw new DBException(dbName,e);
        } catch(Exception e) {
            txMgr.rollback();
            logSQL(sql,data);
            LogManager.error("DB_ERROR",e.toString(),e); LogManager.error(e.toString(),e);
            throw new SysException(e.toString());
        } finally {
            close(cstmt);
            txMgr.end();
        }
    }
    
    /**
    * 10건씩 batch 처리를 한다.
    **/
    public static void executeTxBatch(TxManager txMgr,String sql, ArrayList paramList){
        executeTxBatch(txMgr,null, sql, paramList);
     }
    
    /**
     * 10건씩 batch 처리를 한다.
     **/
     public static void executeTxBatch(TxManager txMgr,String dbName,String sql, ArrayList paramList){
         executeTxBatch(txMgr,dbName, sql, paramList,10);
      }
     
    /**
     * 지정된 건수마다 batch 처리를 한다.
     **/
     public static void executeTxBatch(TxManager txMgr,String sql, ArrayList paramList, int executeUnitCount){
         executeTxBatch(txMgr,null, sql, paramList,executeUnitCount);
      }


    /**
    * 지정된 건수 단위로  batch 처리를 한다.
    **/
    public  static void executeTxBatch(TxManager txMgr,String dbName,
            String sql, ArrayList paramList, int executeUnitCount){

        debugSQL(sql,null);

        

        int paramSize = paramList.size();
        Object[] data = null;
        if(paramSize==0) {
            LogManager.info("수행할 파라미터에 값이 업습니다.");
            return ;
        }
        long start=System.currentTimeMillis();
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            boolean hasNext=false;
            con = txMgr.getConnection();
            pstmt = prepareStatement(con,sql);
            for(int i=0;i<paramSize;i++){
                pstmt.clearParameters();
                data =(Object[]) paramList.get(i);
                setValues(pstmt, data);
                pstmt.addBatch();
                hasNext=true;
                if(i % executeUnitCount == (executeUnitCount-1)){
                    pstmt.executeBatch();
                    hasNext=false;
                }
            }
            if(hasNext){
                pstmt.executeBatch();
            }
            if(isQueryMonitorMode()){
	            long end=System.currentTimeMillis();
	            SysMonitor.addMonitorData( new MonitorData("Database",sql,end-start)); 
            } 
        } catch(SQLException e) {
            //txMgr.rollback(); //개발자가 책임지고 rollback
            logSQL(sql,data);
            LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
            throw new DBException(dbName,e);
        } catch(Exception e) {
            //txMgr.rollback();
            logSQL(sql,data);
            LogManager.error("DB_ERROR",e.toString(),e); LogManager.error(e.toString(),e);
            throw new SysException(e.toString());
        } finally {
            close(pstmt);
        }
    }

    /**
     * 2004. 10. 29. 이종원 작성
     * @param sql
     * @param paramList
     * @return
     * 설명:ArrayList형태로 파라미터를 받아서 쿼리 수행
     */
    public static DBResultSet executePreparedQuery(
            String sql, ArrayList paramList) {
        return executePreparedQuery(sql,paramList.toArray());
    }
    
    /**
     * 2004. 10. 29. 이종원 작성
     * @param sql
     * @param paramList
     * @return
     * 설명:ArrayList형태로 파라미터를 받아서 쿼리 수행
     */
    public static DBResultSet executePreparedQuery(String dbName,
            String sql, ArrayList paramList) {
        return executePreparedQuery(dbName,sql,paramList.toArray(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    }
    
    /**
     * 2004. 10. 29. 이종원 작성
     * @param sql
     * @param paramList
     * @return
     * 설명:ArrayList형태로 파라미터를 받아서 쿼리 수행
     */
    public static ArrayList executePreparedQueryToMapList(
            String sql, ArrayList paramList) {
        return executePreparedQueryToMapList(sql,paramList.toArray());
    }
    
    /**
     * @param sql
     * @param paramList
     * @return
     * 설명:ArrayList형태로 파라미터를 받아서 쿼리 수행
     */
    public static ArrayList executePreparedQueryToArrayList(
            String sql, ArrayList paramList) {
        return executePreparedQueryToArrayList(sql,paramList.toArray());
    }
    
    /**
     * 2004. 10. 29. 이종원 작성
     * @param sql
     * @param paramList
     * @return
     * 설명:ArrayList형태로 파라미터를 받아서 쿼리 수행
     */
    public static ArrayList executePreparedQueryToMapList(String dbName,
			String sql, ArrayList paramList) {
        return executePreparedQueryToMapList(dbName,sql,paramList.toArray());
    }
    
    /**
     * @param sql
     * @param paramList
     * @return
     * 설명:ArrayList형태로 파라미터를 받아서 쿼리 수행
     */
    public static ArrayList executePreparedQueryToArrayList(String dbName,
			String sql, ArrayList paramList) {
        return executePreparedQueryToArrayList(dbName,sql,paramList.toArray());
    }
    
    /**
     * 2004. 10. 29. 이종원 작성
     * @param sql
     * @param paramList
     * @return
     * 설명:ArrayList형태로 파라미터를 받아서 쿼리 수행
     */
    public static Map executePreparedQueryToMap(
            String sql, ArrayList paramList) {
        return executePreparedQueryToMap(sql,paramList.toArray());
    }
    
    /**
     * 2004. 10. 29. 이종원 작성
     * @param sql
     * @param paramList
     * @return
     * 설명:ArrayList형태로 파라미터를 받아서 쿼리 수행
     */
    public static Map executePreparedQueryToMap(String dbName,
														  String sql, ArrayList paramList) {
        return executePreparedQueryToMap(dbName,sql,paramList.toArray());
    }
    
    /**
     * 배치로 sql을 실행한다. (ms-sql용)
     * 
     * @param txMgr TxManager
     * @param sql 쿼리문
     * @param params 파리미터[]을 담고 있는 ArrayList
     * @param isDupAllowed insert시 키 중복 에러가 났을 경우 exception을 발생안할건지 여부
     * 		   exception 발생 안 할 경우 true, 할 경우 false.
     * @return 처리건수
     */
    public static int[] executeTxPreparedUpdateBatch(TxManager txMgr, String sql, List params, boolean isDupAllowed){
        
        //debugSQL(sql,data);
        long start=System.currentTimeMillis();
        Connection con = null;
        PreparedStatement pstmt = null;
        Object[] data = null;
        int size = params.size();
   
        int[] resultCount = new int[size];
        
        try {
            con = txMgr.getConnection();
            pstmt = prepareStatement(con, sql);
            
            for(int i=0; i<size; i++){
                resultCount[i] = 0;
                try{
	                data = (Object[])params.get(i);
	                pstmt.clearParameters();
	                setValues(pstmt, data);
                    //TODO ...추후 확인
	                //pstmt.addBatch();
	                resultCount[i] = pstmt.executeUpdate();
                }catch(SQLException se){
                    if(isDupAllowed && se.getErrorCode()==2627){
                        LogManager.info("key 중복이 발생하여 해당 건은 실행하지 않고 통과하였습니다. (" + Arrays.asList(data).toString() + ")");
                    }else{
                        throw se;
                    }
                }
            }
            if(isQueryMonitorMode()){
            long end=System.currentTimeMillis();
            SysMonitor.addMonitorData( new MonitorData("Database",sql,end-start)); 
            } 
            return resultCount;
            
        } catch(SQLException e) {
            //txMgr.rollback(); //개발자가 책임지고 rollback 한다.
            
            logSQL(sql,data);
            LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
            throw new DBException(e);
           
        } catch(Exception e) {
            logSQL(sql,data);
            LogManager.error("DB_ERROR",e.toString(),e); LogManager.error(e.toString(),e);
            throw new SysException(e.toString());
        } finally {
            close(pstmt);
        }
    }
    
    /**
     * 바인딩 변수에 넣을 파라미터값을 설정
     * 
     * @param stmt CallableStatement객체
     * @param data 바인딩 변수에 넣을 파라미터값들
     * @return
     * @throws Exception
     */
    public static ArrayList setValues(CallableStatement stmt, Object data[]) throws
        Exception {
        if (data == null || data.length==0){
        	LogManager.info("바인드 할 파라미터가 없습니다.");
            return null;
        }
        
        ArrayList arrayList = new ArrayList();
    	int outputPosition = 0;
    	int type = -999999999;
    	
        try{
        	
            for (int i = 0; i < data.length; i++) {
            	
            	/*
            	 * 아래 조건절의 처리 순서 바꾸지 말것
            	 */
            	if (data[i] instanceof Integer) {
            		stmt.setInt(i + 1, Integer.parseInt(data[i].toString()));
            	}
                else if (data[i] instanceof Number) {
                    stmt.setBigDecimal(i + 1, new BigDecimal(data[i].toString()));
                }
                else if (data[i] instanceof String) {
                    if ( ( (String) data[i]).getBytes().length < 2000) {
                        stmt.setString(i + 1, (String) data[i]);
                    }
                    else {
                        stmt.setCharacterStream(i + 1,
                            new StringReader( (String) data[i]),
                            	( (String) data[i]).length());
                    }
                }
                else if (data[i] instanceof java.sql.Date) {
                    stmt.setDate(i + 1, (java.sql.Date) data[i]);
                }
                else if (data[i] instanceof java.sql.Timestamp) {
                    stmt.setTimestamp(i + 1, (java.sql.Timestamp) data[i]);
                }
                else if (data[i] instanceof byte[]) {
                    stmt.setBytes(i + 1, (byte[]) data[i]);
                }
                else if (data[i] instanceof InputStream) {
                    stmt.setBinaryStream(i + 1, (InputStream) data[i],
                                         ( (InputStream) data[i]).available());
                }
                else if (data[i] instanceof Boolean) {
                    stmt.setBoolean(i + 1, ( (Boolean) data[i]).booleanValue());
                }
                else if (data[i] instanceof Null) {
                    stmt.setNull(i + 1, ( (Null) data[i]).type);
                }

                else if (data[i] instanceof SqlTypes) {
                	type = ((SqlTypes)data[i]).getType();
                	
                	/*
                	 * 프로시져가 CURSOR 타입을 리턴할 경우, 
                	 * oracle 드라이버의 OracleTypes.CURSOR를
                	 * 사용하여 결과를 얻는다.
                	 * 
                	 * TODO oracle이 아닌 기타 DB라면, 해당 드라이버의 CURSOR 타입을
                	 * 추가해야 한다.
                	 * 
                	 * 기타 DB일 경우에 대해서는 미 테스트.
                	 */
                	if(type == OracleTypes.CURSOR) {
                		stmt.registerOutParameter(i + 1, OracleTypes.CURSOR);
                	}
                	else if(type == Types.ARRAY) {
                		stmt.registerOutParameter(i + 1, Types.ARRAY);
                	}  
                	else if(type == Types.BIGINT) {
                		stmt.registerOutParameter(i + 1, Types.BIGINT);
                	}
                	else if(type == Types.BINARY) {
                		stmt.registerOutParameter(i + 1, Types.BINARY);
                	}
                	else if(type == Types.BIT) {
                		stmt.registerOutParameter(i + 1, Types.BIT);
                	}
                	else if(type == Types.BLOB) {
                		stmt.registerOutParameter(i + 1, Types.BLOB);
                	}
                	else if(type == Types.BOOLEAN) {
                		stmt.registerOutParameter(i + 1, Types.BOOLEAN);
                	}
                	else if(type == Types.CHAR) {
                		stmt.registerOutParameter(i + 1, Types.CHAR);
                	}
                	else if(type == Types.CLOB) {
                		stmt.registerOutParameter(i + 1, Types.CLOB);
                	}
                	else if(type == Types.DATALINK) {
                		stmt.registerOutParameter(i + 1, Types.DATALINK);
                	}
                	else if(type == Types.DATE) {
                		stmt.registerOutParameter(i + 1, Types.DATE);
                	}
                	else if(type == Types.DECIMAL) {
                		stmt.registerOutParameter(i + 1, Types.DECIMAL);
                	}
                	else if(type == Types.DISTINCT) {
                		stmt.registerOutParameter(i + 1, Types.DISTINCT);
                	}
                	else if(type == Types.DOUBLE) {
                		stmt.registerOutParameter(i + 1, Types.DOUBLE);
                	}
                	else if(type == Types.FLOAT) {
                		stmt.registerOutParameter(i + 1, Types.FLOAT);
                	}
                	else if(type == Types.INTEGER) {
                		stmt.registerOutParameter(i + 1, Types.INTEGER);
                	}
                	else if(type == Types.JAVA_OBJECT) {
                		stmt.registerOutParameter(i + 1, Types.JAVA_OBJECT);
                	}
                	else if(type == Types.LONGVARBINARY) {
                		stmt.registerOutParameter(i + 1, Types.LONGVARBINARY);
                	}
                	else if(type == Types.LONGVARCHAR) {
                		stmt.registerOutParameter(i + 1, Types.LONGVARCHAR);
                	}
                	else if(type == Types.NULL) {
                		stmt.registerOutParameter(i + 1, Types.NULL);
                	}
                	else if(type == Types.NUMERIC) {
                		stmt.registerOutParameter(i + 1, Types.NUMERIC);
                	}
                	else if(type == Types.OTHER) {
                		stmt.registerOutParameter(i + 1, Types.OTHER);
                	}
                	else if(type == Types.REAL) {
                		stmt.registerOutParameter(i + 1, Types.REAL);
                	}
                	else if(type == Types.REF) {
                		stmt.registerOutParameter(i + 1, Types.REF);
                	}
                	else if(type == Types.SMALLINT) {
                		stmt.registerOutParameter(i + 1, Types.SMALLINT);
                	}
                	else if(type == Types.STRUCT) {
                		stmt.registerOutParameter(i + 1, Types.STRUCT);
                	}
                	else if(type == Types.TIME) {
                		stmt.registerOutParameter(i + 1, Types.TIME);
                	}
                	else if(type == Types.TIMESTAMP) {
                		stmt.registerOutParameter(i + 1, Types.TIMESTAMP);
                	}
                	else if(type == Types.TINYINT) {
                		stmt.registerOutParameter(i + 1, Types.TINYINT);
                	}
                	else if(type == Types.VARBINARY) {
                		stmt.registerOutParameter(i + 1, Types.VARBINARY);
                	}
                	else if(type == Types.VARCHAR) {
                		stmt.registerOutParameter(i + 1, Types.VARCHAR);
                	}
                	else {
                		throw new SysException(i+"번째 인수 정의되지 않은 타입\n"
                    			+data[i].getClass().getName()+" Types의 상수 : "+type);
                	}

                	outputPosition = i+1;     
                    
                    arrayList.add(Integer.valueOf(outputPosition));
                }
                else {
                	throw new SysException(i+"번째 인수 알수 없는 타입\n"
                			+data[i].getClass().getName());
                }
            }
        }catch(Exception e){
            LogManager.error("ERROR", "DBManager.setValue error", e);
            throw e;
        }
        return arrayList;
    }

    /**
	 * CallableStatement를 사용하여 프로시져를 호출하고 Cursor 결과를 리턴 시켜주는 메소드
	 * 
	 * @param ctx
	 * @param sql
	 * @param data
	 * @return
	 * @throws DBException
	 */
    public static DataSet executeCallableCursorQuery(String sql,
            ArrayList data) throws DBException {
    	return executeCallableCursorQuery(null, sql, data);
    }	

	/**
	 * CallableStatement를 사용하여 프로시져를 호출하고 Cursor 결과를 리턴 시켜주는 메소드
	 * 
	 * @param ctx
	 * @param sql
	 * @param data
	 * @return
	 * @throws DBException
	 */
    public static DataSet executeCallableCursorQuery(String dbName, String sql,
            ArrayList data) throws DBException {
    	return executeCallableCursorQuery(dbName, sql, data.toArray(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    }
    
	/**
	 * CallableStatement를 사용하여 프로시져를 호출하고 Cursor 결과를 리턴 시켜주는 메소드
	 * 
	 * @param ctx
	 * @param sql
	 * @param data
	 * @return
	 * @throws DBException
	 */
    public static DataSet executeCallableCursorQuery(String sql,
            Object data[]) throws DBException {
    	return executeCallableCursorQuery(null, sql, data);
    }	
	
    /**
     * CallableStatement를 사용하여 프로시져를 호출하고 Cursor 결과를 리턴 시켜주는 메소드
     * 
     * @param ctx
     * @param dbName
     * @param sql
     * @param data
     * @return
     * @throws DBException
     */
    public static DataSet executeCallableCursorQuery(String dbName, String sql,
            Object data[]) throws DBException {
    	return executeCallableCursorQuery(dbName, sql, data, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    }

    	
    /**
     * CallableStatement를 사용하여 프로시져를 호출하고 1개의 Cursor 결과를 리턴 시켜주는 메소드
     * 
     * @param dbName
     * @param sql
     * @param data
     * @param resultSetType
     * @param resultSetConcurrency
     * @return DataSet
     * @throws DBException
     */
    public static DataSet executeCallableCursorQuery(String dbName, String sql,
        Object data[], int resultSetType, int resultSetConcurrency) throws DBException {
        
        debugSQL(sql, data);
        
        long start=System.currentTimeMillis();
        Connection con = null;
        CallableStatement cstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection(dbName);
            
            cstmt = callableStatement(con, sql, resultSetType, resultSetConcurrency);

            ArrayList arrayList = DBManager.setValues(cstmt, data);
            int outputPosition = ((Integer)arrayList.get(0)).intValue();

            cstmt.execute();
            rs = (ResultSet)cstmt.getObject(outputPosition);
            
            openedResultSetCount++;
            
            DataSet rset = toMemory(rs);
            
            if(isQueryMonitorMode()){
                long end=System.currentTimeMillis();
                SysMonitor.addMonitorData( new MonitorData("Database",sql,end-start)); 
            } 
            return rset;
        }
        catch (SQLException e) {
            logSQL(sql, data);
            LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
            throw new DBException(dbName,e);
        }
        catch (Exception e) {
            logSQL(sql, data);
            LogManager.error("DB_ERROR",e.toString(),e); LogManager.error(e.toString(),e);
            throw new SysException(e.toString());
        }
        finally {
            close(rs);
            close(cstmt);
            close(con);
        }
    }   
    
	/**
     * CallableStatement를 사용하여 프로시져를 호출하고 Cursor 및 
     * 기타 object 결과를 모두 리턴 시켜주는 메소드
	 * 
	 * @param ctx
	 * @param sql
	 * @param data
	 * @return
	 * @throws DBException
	 */
    public static Map executeCallableObjectQuery(String sql,
            ArrayList data) throws DBException {
    	return executeCallableObjectQuery(null, sql, data);
    }	

	/**
     * CallableStatement를 사용하여 프로시져를 호출하고 Cursor 및 
     * 기타 object 결과를 모두 리턴 시켜주는 메소드
	 * 
	 * @param ctx
	 * @param sql
	 * @param data
	 * @return
	 * @throws DBException
	 */
    public static Map executeCallableObjectQuery(String dbName, String sql,
            ArrayList data) throws DBException {
    	return executeCallableObjectQuery(dbName, sql, data.toArray(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    }
    
	/**
     * CallableStatement를 사용하여 프로시져를 호출하고 Cursor 및 
     * 기타 object 결과를 모두 리턴 시켜주는 메소드
	 * 
	 * @param ctx
	 * @param sql
	 * @param data
	 * @return
	 * @throws DBException
	 */
    public static Map executeCallableObjectQuery(String sql,
            Object data[]) throws DBException {
    	return executeCallableObjectQuery(null, sql, data);
    }	
	
    /**
     * CallableStatement를 사용하여 프로시져를 호출하고 Cursor 및 
     * 기타 object 결과를 모두 리턴 시켜주는 메소드
     * 
     * @param ctx
     * @param dbName
     * @param sql
     * @param data
     * @return
     * @throws DBException
     */
    public static Map executeCallableObjectQuery(String dbName, String sql,
            Object data[]) throws DBException {
    	return executeCallableObjectQuery(dbName, sql, data, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    }    

    /**
     * CallableStatement를 사용하여 프로시져를 호출하고 Cursor 및 
     * 기타 object 결과를 모두 리턴 시켜주는 메소드
     * 
     * @param dbName
     * @param sql
     * @param data
     * @param resultSetType
     * @param resultSetConcurrency
     * @return Map
     * @throws DBException
     */
    public static Map executeCallableObjectQuery(String dbName, String sql,
        Object data[], int resultSetType, int resultSetConcurrency) throws DBException {
        
        debugSQL(sql, data);
                
        long start=System.currentTimeMillis();
        Connection con = null;
        CallableStatement cstmt = null;
        ResultSet rs = null;
        
        HashMap hm = new HashMap();
        try {
            con = getConnection(dbName);
            
            cstmt = callableStatement(con, sql, resultSetType, resultSetConcurrency);
            
            ArrayList arrayList = DBManager.setValues(cstmt, data);

            cstmt.execute();
            
            int arrayListSize = arrayList.size();
            if(arrayListSize <= 0)
            {
            	throw new SysException("Procedure의 실행 결과 데이타가 저장된 object가 존재하지 않습니다.");
            }
            
            
            int outputPosition = 0;
            String parameterName = null;
            Object obj = null;
            
//            LogManager.debug("arrayListSize:"+arrayListSize);
            
            for(int i=0 ; i<arrayListSize ; i++)
            {
            	outputPosition = ((Integer)arrayList.get(i)).intValue();
            	
            	parameterName = ((SqlTypes)data[outputPosition-1]).getParameterName();
            	
            	if(parameterName.equals(SqlTypes.NO_NAME))
            	{
            		throw new SysException("생성자 SqlTypes(String parameterName, int type)으로 생성해 주십시요.");
            	}
            	
//            	LogManager.debug("outputPosition: "+outputPosition);
//            	LogManager.debug("parameterName: "+parameterName);
//            	LogManager.debug("cstmt.getObject(cursorPosition): "+cstmt.getObject(outputPosition));
            	
            	obj = cstmt.getObject(outputPosition);
            	if(obj instanceof ResultSet)
            	{
            		rs = (ResultSet)obj;
            		
            		openedResultSetCount++;
                    
            		obj = toMemory(rs);
            		
            	}
            	
            	hm.put(parameterName, obj);
            }
            
            if(isQueryMonitorMode()){
                long end=System.currentTimeMillis();
                SysMonitor.addMonitorData( new MonitorData("Database",sql,end-start)); 
            } 
            
            return hm;
        }
        catch (SQLException e) {
            logSQL(sql, data);
            LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
            throw new DBException(dbName,e);
        }
        catch (Exception e) {
            logSQL(sql, data);
            LogManager.error("DB_ERROR",e.toString(),e); LogManager.error(e.toString(),e);
            throw new SysException(e.toString());
        }
        finally {
            close(rs);
            close(cstmt);
            close(con);
        }
    }     
    
	/**
     * CallableStatement를 사용하여 insert,delete, update후 여러 결과값을 리턴 시켜주는 메소드
	 * 
	 * @param ctx
	 * @param sql
	 * @param data
	 * @return
	 * @throws DBException
	 */
    public static Map executeCallableObjectUpdate(String sql,
            ArrayList data) throws DBException {
    	return executeCallableObjectUpdate(null, sql, data.toArray());
    }	

   
	/**
     * CallableStatement를 사용하여 insert,delete, update후 여러 결과값을 리턴 시켜주는 메소드
	 * 
	 * @param ctx
	 * @param sql
	 * @param data
	 * @return
	 * @throws DBException
	 */
    public static Map executeCallableObjectUpdate(String sql,
            Object data[]) throws DBException {
    	return executeCallableObjectUpdate(null, sql, data);
    }	
	
   
    
    /**
     * CallableStatement를 사용하여 insert,delete, update후 여러 결과값을 리턴 시켜주는 메소드
     * 
	 * @param ctx 
     * @param dbName
     * @param sql
     * @param data
     * @return
     * @throws DBException
     */
	public static Map executeCallableObjectUpdate(String dbName, 
			String sql, Object data[]) throws DBException {
	
	   debugSQL(sql, data);
	   long start=System.currentTimeMillis();
	   Connection con = null;
	   CallableStatement cstmt = null;
	   ResultSet rs = null;
	   HashMap hm = new HashMap();
	   
	   try {
	       con = getConnection(dbName);
	
	       cstmt = callableStatement(con, sql);
	
	       ArrayList arrayList = setValues(cstmt, data);

	       cstmt.executeUpdate();
	       
           int arrayListSize = arrayList.size();
           if(arrayListSize <= 0)
           {
        	   throw new SysException("Procedure의 실행 결과 데이타가 저장된 object가 존재하지 않습니다.");
           }	       
           int outputPosition = 0;
           String parameterName = null;
           Object obj = null;
           
//           LogManager.debug("arrayListSize:"+arrayListSize);
           
           for(int i=0 ; i<arrayListSize ; i++)
           {
				outputPosition = ((Integer)arrayList.get(i)).intValue();
				
				parameterName = ((SqlTypes)data[outputPosition-1]).getParameterName();
				
				if(parameterName.equals(SqlTypes.NO_NAME))
				{
					throw new SysException("생성자 SqlTypes(String parameterName, int type)으로 생성해 주십시요.");
				}
				
//           	LogManager.debug("outputPosition: "+outputPosition);
//           	LogManager.debug("parameterName: "+parameterName);
//           	LogManager.debug("cstmt.getObject(cursorPosition): "+cstmt.getObject(outputPosition));
				
				obj = cstmt.getObject(outputPosition);
				if(obj instanceof ResultSet)
				{
					rs = (ResultSet)obj;
						
					openedResultSetCount++;
					   
					obj = toMemory(rs);
				}
				
				hm.put(parameterName, obj);
           }	       
	       
	       if(isQueryMonitorMode()){
	           long end=System.currentTimeMillis();
	           SysMonitor.addMonitorData( new MonitorData("Database",sql,end-start)); 
	       } 
	       
	       return hm;
	   }
	   catch (SQLException e) {
	       logSQL(sql, data);
	       LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
	       throw new DBException(dbName,e);
	   }
	   catch (Exception e) {
	       logSQL(sql, data);
	       LogManager.error("DB_ERROR",e.toString(),e); LogManager.error(e.toString(),e);
	       throw new SysException(e.toString());
	   }
	   finally {
		   close(rs);
	       close(cstmt);
	       close(con);
	   }
	}     
	
	
	/**
	 * 트렌젝션 관리를 위해 하나의 커넥션을 공유하며, 
     * CallableStatement를 사용하여 insert,delete, update후 여러 결과값을 리턴 시켜주는 메소드
	 * 
	 * @param ctx
	 * @param txMgr
	 * @param sql
	 * @param data
	 * @return
	 * @throws DBException
	 */
    public static Map executeCallableObjectTxUpdate(TxManager txMgr,
    		String sql, ArrayList data) throws DBException {
    	return executeCallableObjectTxUpdate(txMgr, sql, data.toArray());
    }	

   
    /**
     * 트렌젝션 관리를 위해 하나의 커넥션을 공유하며, 
     * CallableStatement를 사용하여 insert,delete, update후 여러 결과값을 리턴 시켜주는 메소드
     * 
	 * @param ctx 
     * @param txMgr
     * @param sql
     * @param data
     * @return
     * @throws DBException
     */
	public static Map executeCallableObjectTxUpdate(TxManager txMgr, 
			String sql, Object data[]) throws DBException {
	
	   debugSQL(sql, data);
	   long start=System.currentTimeMillis();
	   Connection con = null;
	   CallableStatement cstmt = null;
	   ResultSet rs = null;
	   HashMap hm = new HashMap();
	   
	   try {
	       con = txMgr.getConnection();
	
	       cstmt = callableStatement(con, sql);
	
	       ArrayList arrayList = setValues(cstmt, data);

	       cstmt.executeUpdate();
	       
           int arrayListSize = arrayList.size();
           if(arrayListSize <= 0)
           {
        	   throw new SysException("Procedure의 실행 결과 데이타가 저장된 object가 존재하지 않습니다.");
           }	       
           int outputPosition = 0;
           String parameterName = null;
           Object obj = null;
           
//           LogManager.debug("arrayListSize:"+arrayListSize);
           
           for(int i=0 ; i<arrayListSize ; i++)
           {
				outputPosition = ((Integer)arrayList.get(i)).intValue();
				
				parameterName = ((SqlTypes)data[outputPosition-1]).getParameterName();
				
				if(parameterName.equals(SqlTypes.NO_NAME))
				{
					throw new SysException("생성자 SqlTypes(String parameterName, int type)으로 생성해 주십시요.");
				}
				
//           	LogManager.debug("outputPosition: "+outputPosition);
//           	LogManager.debug("parameterName: "+parameterName);
//           	LogManager.debug("cstmt.getObject(cursorPosition): "+cstmt.getObject(outputPosition));
				
				obj = cstmt.getObject(outputPosition);
				if(obj instanceof ResultSet)
				{
					rs = (ResultSet)obj;
						
					openedResultSetCount++;
					   
					obj = toMemory(rs);
				}
				
				hm.put(parameterName, obj);
           }	       
	       
	       if(isQueryMonitorMode()){
	           long end=System.currentTimeMillis();
	           SysMonitor.addMonitorData( new MonitorData("Database",sql,end-start)); 
	       } 
	       
	       return hm;
	   }
	   catch (SQLException e) {
	       logSQL(sql, data);
	       LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
	       throw new DBException(e);
	   }
	   catch (Exception e) {
	       logSQL(sql, data);
	       LogManager.error("DB_ERROR",e.toString(),e); LogManager.error(e.toString(),e);
	       throw new SysException(e.toString());
	   }
	   finally {
		   close(rs);
	       close(cstmt);
	   }
	}
    
    public static void main(String[] args){
    
/*        ArrayList list = DBManager.executeQueryToArrayList("select * from code where code_group = '1008'");
        for (int i=0; i<list.size(); i++){
            Object[] objArr = (Object[])list.get(i);
            for(int j=0; j<objArr.length; j++){
                System.out.print(objArr[j]+ "\t");
            }
            System.out.println();
        }*/
        
        
        /*
        TxManager txManager = new TxManager();
        txManager.begin();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        
        try{
            String sql = "insert into test_table (seq, name, addr) values (?,?,?)";
            List params = new ArrayList();
            params.add(new Object[]{"5", "성춘향5", "남원1"});
            
            DBManager.executeTxPreparedUpdateBatch(txManager, sql, params, true);
            
            pstmt = DBManager.makeTxPreparedStatement(txManager, 
                    	"select * from test_table where seq > ?", new Object[]{"1"});
            rs = pstmt.executeQuery();
            if(rs!=null){
                while(rs.next()){
                    
	                System.out.println("seq : " + rs.getString(1));
	                System.out.println("name : " + rs.getString(2));
	                System.out.println("addr : " + rs.getString(3));
                }
            }
            
            txManager.commit();
        
        }catch(Exception e){
            e.printStackTrace();
            txManager.rollback();
        }finally{
            try{
                if(rs!=null) rs.close();
            }catch(Exception e){}
            try{
                if(pstmt!=null) pstmt.close();
            }catch(Exception e){}
            
            txManager.end();
        }
         */
    	
        System.setProperty("SPIDER_HOME", "C:/PMH_TEMP/workspace/spider");
    	StartupContext.setPropertyType("XML");
    	
    	/*
        String sql = "exec PROC_MY ?, ? ";
        Object[] param = new Object[]{Integer.valueOf(5), Integer.valueOf(1)};

        DBResultSet rs = DBManager.executePreparedQuery(sql, param);
        */


//        String sql2 = "{call PROC_MY(?, ?)}";
//        Object[] param = new Object[]{Integer.valueOf(10), Integer.valueOf(2)};
        //DBResultSet rs = DBManager.executeCallableQuery(sql2, param);
//      System.out.println("rs =>"+rs);


    	Object[] param = new Object[]{Integer.valueOf(7)};
        String sql2 = "{call PROC_UP(?)}";
//        String sql2 = "{call PROC_UPD(?)}";
        int i = DBManager.executeCallableUpdate(sql2, param);
        System.out.println("i =>"+i);
    }
}
