/*
 * Spider Framework
 *
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 *
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.jdbc;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.sql.CallableStatement;
import java.sql.Clob;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nebsoa.common.Context;
import nebsoa.common.collection.DataSet;
import nebsoa.common.collection.MapDataSet;
import nebsoa.common.exception.DBException;
import nebsoa.common.exception.SysException;
import nebsoa.common.jdbc.context.DBContext;
import nebsoa.common.jdbc.types.SqlTypes;
import nebsoa.common.log.LogManager;
import nebsoa.common.monitor.ContextLogger;
import nebsoa.common.monitor.MonitorData;
import nebsoa.common.monitor.SysMonitor;
import nebsoa.common.monitor.profiler.Profiler;
import nebsoa.common.util.DataMap;
import nebsoa.common.util.PropertyManager;
import nebsoa.common.util.StringUtil;
import nebsoa.common.util.ZqlUtil;

import org.apache.commons.beanutils.BeanUtils;

import com.ibatis.sqlmap.engine.execution.BatchException;

/*******************************************************************
 * <pre>
 * 1.설명
 * db와 연동하기위한 유용한 메소드 모음 클래스.
 * connection을 얻어 query를 수행 후 자동으로 connection을 close해 줍니다.
 * select문장 일 경우 ResultSet과 사용법이 유사한 DataSet이 return됩니다.
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
 * $Log: DBHandler.java,v $
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
 * Revision 1.4  2008/10/15 08:20:07  youngseokkim
 * executeBatch 실행시 SQLException 발생하면
 * NextException 까지 로그 남기도록 수정
 *
 * Revision 1.3  2008/10/15 03:59:52  jwlee
 * //tx DB NAME 과 DBCTX의 DBNAME 이 같은지 일관성 체크 함수 추가
 * checkDbNameConfig(tx, dbCtx);
 *
 * Revision 1.2  2008/09/25 13:54:28  jglee
 * executeBatch 실행시 paramList값 출력 처리
 *
 * Revision 1.1  2008/08/04 08:54:54  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.7  2008/07/25 01:30:18  김재범
 * executeTxBatch 메서드 추가
 *
 * Revision 1.6  2008/07/17 07:11:13  김영석
 * executeBatch()에서 com.ibatis.sqlmap.engine.execution.BatchException발생시
 * DBException을 throw 하도록 수정
 *
 * Revision 1.5  2008/07/04 12:31:47  김은정
 * startEvent에 message추가
 *
 * Revision 1.4  2008/02/26 02:08:15  김재범
 * *** empty log message ***
 *
 * Revision 1.3  2008/02/11 02:49:59  오재훈
 * *** empty log message ***
 *
 * Revision 1.2  2008/01/25 09:06:36  오재훈
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:30  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.13  2008/01/21 04:02:43  안경아
 * *** empty log message ***
 *
 * Revision 1.12  2007/12/18 06:16:31  오재훈
 * *** empty log message ***
 *
 * Revision 1.11  2007/12/18 06:02:09  오재훈
 * *** empty log message ***
 *
 * Revision 1.10  2007/12/18 05:57:00  오재훈
 * *** empty log message ***
 *
 * Revision 1.9  2007/12/18 05:20:12  오재훈
 * Ibatis executeBatch 추가. 인자는 ArrayList 또는 DataSet임
 *
 * Revision 1.8  2007/12/13 07:33:29  오재훈
 * *** empty log message ***
 *
 * Revision 1.7  2007/12/12 05:11:21  오재훈
 * *** empty log message ***
 *
 * Revision 1.6  2007/12/11 06:41:29  이종원
 * TxService Interface추가
 *
 * Revision 1.5  2007/12/10 01:45:46  오재훈
 * *** empty log message ***
 *
 * Revision 1.4  2007/12/07 02:33:53  오재훈
 * *** empty log message ***
 *
 * Revision 1.3  2007/12/06 05:36:19  오재훈
 * ibatis용 트랜잭션 쿼리 추가.
 *
 * Revision 1.2  2007/12/05 05:36:59  오재훈
 * IBatis 쿼리 메소드 추가
 *
 * Revision 1.1  2007/11/26 08:37:43  안경아
 * *** empty log message ***
 *
 * Revision 1.30  2007/10/04 05:41:02  김성균
 * ContextLogger 적용
 *
 * Revision 1.29  2007/09/19 10:05:57  이종원
 * cxt.startEvent버그 수정
 *
 * Revision 1.28  2007/09/17 03:36:28  오재훈
 * *** empty log message ***
 *
 * Revision 1.27  2007/07/17 09:19:04  오재훈
 * DataMap을 인자로 받아서 수행하는 로직 추가
 *
 * Revision 1.26  2007/05/19 11:20:23  이종원
 * Profile Log간소화
 *
 * Revision 1.25  2007/04/02 06:49:40  안경아
 * *** empty log message ***
 *
 * Revision 1.24  2007/04/02 06:28:25  안경아
 * *** empty log message ***
 *
 * Revision 1.23  2007/04/02 05:37:41  안경아
 * *** empty log message ***
 *
 * Revision 1.22  2007/04/02 02:00:06  안경아
 * *** empty log message ***
 *
 * Revision 1.20  2007/03/15 00:50:01  김성균
 * 일반 Exception일 경우 로그남겨지는 카테고리 ERROR로 변경
 *
 * Revision 1.19  2007/03/07 02:53:45  김성균
 * 일부 로그 INFO 레벨로 변경
 *
 * Revision 1.18  2007/01/17 02:10:35  김성균
 * 오타 수정
 *
 * Revision 1.17  2007/01/16 06:05:53  안경아
 * *** empty log message ***
 *
 * Revision 1.16  2007/01/12 09:29:49  안경아
 * *** empty log message ***
 *
 * Revision 1.15  2007/01/04 11:25:26  안경아
 * *** empty log message ***
 *
 * Revision 1.14  2006/12/15 06:23:53  최수종
 * 프로시져 update 메소드 추가(프로시져 관련 트랜잭션 관리 메소드도 추가)
 *
 * Revision 1.13  2006/11/27 06:20:23  최수종
 * CallableStatement 관련 메소드 추가(프로시져 결과 리턴)
 *
 * Revision 1.11  2006/10/23 11:33:10  김성균
 * *** empty log message ***
 *
 * Revision 1.10  2006/10/23 07:05:49  김성균
 * *** empty log message ***
 *
 * Revision 1.9  2006/10/03 09:28:57  오재훈
 * executePreparedQuery( ctx,null, sql, data, resultSetType, resultSetConcurrency, 0); ->executePreparedQuery( ctx,dbNamel, sql, data, resultSetType, resultSetConcurrency, 0);
 * 으로 수정
 *
 * Revision 1.8  2006/09/05 10:38:51  김성균
 * *** empty log message ***
 *
 * Revision 1.7  2006/09/05 05:21:45  김성균
 * *** empty log message ***
 *
 * Revision 1.6  2006/09/05 04:52:43  김성균
 * *** empty log message ***
 *
 * Revision 1.5  2006/06/21 13:56:15  이종원
 * *** empty log message ***
 *
 * Revision 1.4  2006/06/21 12:25:49  이종원
 * *** empty log message ***
 *
 * Revision 1.3  2006/06/21 09:43:03  이종원
 * DBHanderl추가
 *
 * Revision 1.17  2006/06/19 04:10:16  이종원
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class DBHandler {

 //   static public Hashtable colNamesHash = new Hashtable();
    static public int openedConnectionCount = 0;
    static public int openedStatementCount = 0;
    static public int openedResultSetCount = 0;


    public static boolean isQueryMonitorMode() {
        return PropertyManager.getBooleanProperty("db",
            "QUERY_MONITOR_MODE", "true");
    }


    /**
     * 해당하는 db에 접속하여 DB Connection를 얻어 내는 함수 입니다.
     */
    public static Connection getConnection(Context ctx, String dbName)
            throws DBException {
        Connection con = DBConfig.getInstance(dbName).getConnection(ctx);

        if (con != null) {
            openedConnectionCount++;
            return con;
        } else {
            throw new DBException("DB CONNECT FAIL");
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
     */
    public static PreparedStatement makeTxPreparedStatement(TxManager txMgr, String sql, Object data[]) throws DBException {
        debugSQL(null, sql, data);

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
            LogManager.error(e.getMessage(),e);
            e.printStackTrace();
            throw new SysException(e.toString());
        }
    }


    /**
         close Connection
     */
    public static void close(Connection con) {
        try {

            if (con != null) {
                if (con.getAutoCommit() == false) {
                    Exception e = new Exception(
                            "close시점에 getAutoCommit false인 connection이 발견되었습니다.");
                    LogManager.error("MONITOR", e.toString(), e);
                    con.setAutoCommit(true);
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
         ResultSet의 데이타를 DataSet에 담아 메모리로 데이타를 가져온다.
         자동으로 ResultSet은 닫힌다.
     */
    public static DataSet toMemory(ResultSet rs) throws DBException {
        return new DataSet(rs);
    }

    /**
     * ResultSet의 데이타를 DataSet에 담아 메모리로 데이타를 가져온다.
     * 자동으로 ResultSet은 닫힌다.
     * sybase를 위하여 만듬.
     */
    public static DataSet toMemory(ResultSet rs, int skipCount) throws DBException {
       return new DataSet(rs, skipCount);
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
    public static DataSet executeCallableQuery(Context ctx,String sql, Object data[]) throws DBException {
        return executeCallableQuery(ctx,null, sql, data, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    }

    /**
     * CallableStatement를 사용하여 프로시져를 호출하고 Select 결과를 리턴 시켜주는 메소드
     *
     * @param sql
     * @param paramList
     * @return
     * @throws DBException
     */
    public static DataSet executeCallableQuery(Context ctx,String sql, ArrayList paramList) throws DBException {
        return executeCallableQuery( ctx,null, sql, paramList.toArray(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
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
    public static DataSet executeCallableQuery(Context ctx,String dbName, String sql, Object data[]) throws DBException {
        return executeCallableQuery( ctx,dbName, sql, data, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
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
    public static DataSet executeCallableQuery(Context ctx,String dbName, String sql, ArrayList paramList) throws DBException {
        return executeCallableQuery( ctx,dbName, sql, paramList.toArray(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
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
    public static DataSet executeCallableQuery(Context ctx,String dbName, String sql,
            ArrayList paramList, int resultSetType, int resultSetConcurrency) throws DBException {
        return executeCallableQuery( ctx,dbName, sql, paramList.toArray(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
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
    public static DataSet executeCallableQuery(Context ctx,String dbName, String sql,
        Object data[], int resultSetType, int resultSetConcurrency) throws DBException {
        ctx = ContextLogger.getContext();

        debugSQL(ctx, sql, data);
        startProfilerEvent(ctx,sql);

        long start=System.currentTimeMillis();
        Connection con = null;
        CallableStatement cstmt = null;
        ResultSet rs = null;

        if(ctx != null){
            ctx.startActivity(Context.ACTIVITY_DB_READ);
        }

        try {


        	con = getConnection(ctx,dbName);

            cstmt = callableStatement(con, sql, resultSetType, resultSetConcurrency);

            //pstmt.setQueryTimeout(5);
            setValues(cstmt, data);
            rs = cstmt.executeQuery();
            openedResultSetCount++;
            startProfilerEvent(ctx,"Data fetch");
            DataSet rset = toMemory(rs);
            stopProfilerEvent(ctx);
            if(isQueryMonitorMode()){
                long end=System.currentTimeMillis();
                SysMonitor.addMonitorData( new MonitorData("Database",sql,end-start));
            }
            debugSqlLog(ctx, sql, data, null);


        	return rset;
        }
        catch (SQLException e) {
            logSQL(sql, data);
            debugSqlLog(ctx, sql, data, e);
            LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
            throw new DBException(dbName,e);
        }
        catch (Exception e) {
            logSQL(sql, data);
            debugSqlLog(ctx, sql, data, e);
            LogManager.error(e.getMessage(),e);
            throw new SysException(e.toString());
        }
        finally {
            close(rs);
            close(cstmt);
            close(con);
            stopProfilerEvent(ctx);
            if(ctx != null){
                ctx.stopActivity();
            }

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
	public static int executeCallableUpdate(Context ctx,String sql, Object data[])
		throws DBException {
	    return executeCallableUpdate( ctx,null, sql, data);
	}

	/**
	 * CallableStatement를 사용하여 insert,delete, update 결과를 리턴 시켜주는 메소드
	 *
	 * @param sql
	 * @param paramList
	 * @return
	 * @throws DBException
	 */
	public static int executeCallableUpdate(Context ctx,String sql, ArrayList paramList)
		throws DBException {
	    return executeCallableUpdate( ctx,null, sql, paramList.toArray());
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
	public static int executeCallableUpdate(Context ctx,String dbName, String sql,
	        ArrayList paramList) throws DBException {
	    return executeCallableUpdate( ctx,dbName, sql, paramList.toArray());
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
	public static int executeCallableUpdate(Context ctx,String dbName, String sql,
	                                       Object data[]) throws DBException {
	   ctx = ContextLogger.getContext();

	   debugSQL(ctx, sql, data);
       startProfilerEvent(ctx,sql);
	   long start=System.currentTimeMillis();
	   Connection con = null;
	   CallableStatement cstmt = null;
	   if(ctx != null){
	       ctx.startActivity(Context.ACTIVITY_DB_WRITE);
       }

   	   try {

	       con = getConnection(ctx,dbName);

	       cstmt = callableStatement(con, sql);

	       //pstmt.setQueryTimeout(5);
	       setValues(cstmt, data);
	       int result = cstmt.executeUpdate();
	       if(isQueryMonitorMode()){
	           long end=System.currentTimeMillis();
	           SysMonitor.addMonitorData( new MonitorData("Database",sql,end-start));
	       }
           debugSqlLog(ctx, sql, data, null);
           return result;
	   }
	   catch (SQLException e) {
	       logSQL(sql, data);
           debugSqlLog(ctx, sql, data, e);
	       LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
	       throw new DBException(dbName,e);
	   }
	   catch (Exception e) {
	       logSQL(sql, data);
           debugSqlLog(ctx, sql, data, e);
	       e.printStackTrace();
	       LogManager.error(e.getMessage(),e);
	       throw new SysException(e.toString());
	   }
	   finally {
	       close(cstmt);
	       close(con);
           stopProfilerEvent(ctx);
           if(ctx != null){
               ctx.stopActivity();
           }

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
	public static int executeCallableTxUpdate(Context ctx, TxManager txMgr, String sql,
	        ArrayList paramList) throws DBException {
	    return executeCallableTxUpdate(ctx, txMgr, sql, paramList.toArray());
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
	public static int executeCallableTxUpdate(Context ctx, TxManager txMgr, String sql,
			Object data[]) throws DBException {
	   ctx = ContextLogger.getContext();

	   debugSQL(ctx, sql, data);
       startProfilerEvent(ctx,sql);
	   long start=System.currentTimeMillis();
	   Connection con = null;
	   CallableStatement cstmt = null;

	   if(ctx !=null){
	       ctx.startActivity(Context.ACTIVITY_DB_WRITE);
       }

   	   try {

	       con = txMgr.getConnection();
	       cstmt = callableStatement(con, sql);

	       setValues(cstmt, data);
	       int result = cstmt.executeUpdate();
	       if(isQueryMonitorMode()){
	           long end=System.currentTimeMillis();
	           SysMonitor.addMonitorData( new MonitorData("Database",sql,end-start));
	       }
           debugSqlLog(ctx, sql, data, null);
	       return result;
	   }
	   catch (SQLException e) {
	       logSQL(sql, data);
           debugSqlLog(ctx, sql, data, e);
	       LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
	       throw new DBException(e);
	   }
	   catch (Exception e) {
	       logSQL(sql, data);
           debugSqlLog(ctx, sql, data, e);
	       e.printStackTrace();
	       LogManager.error(e.getMessage(),e);
	       throw new SysException(e.toString());
	   }
	   finally {
	       close(cstmt);
	       stopProfilerEvent(ctx);
	       if(ctx != null){
	           ctx.stopActivity();
           }

	   }
	}

    /**
         쿼리를  수행하고 DataSet을 커넥션은 자동을 닫힙니다.
     */
    public static DataSet executeQuery(Context ctx,String dbName, String sql) throws
        DBException {
        if (sql == null)return null;
        ctx = ContextLogger.getContext();
        debugSQL(ctx, sql, null);
        startProfilerEvent(ctx,sql);
        long start=System.currentTimeMillis();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        DataSet data = null;
        if(ctx != null){
            ctx.startActivity(Context.ACTIVITY_DB_READ);
        }
    	try {
            con = DBManager.getConnection(ctx,dbName);
            stmt = createStatement(con);
            rs = stmt.executeQuery(sql);
            openedResultSetCount++;
            startProfilerEvent(ctx,"Data fetch");
            data = DBManager.toMemory(rs);
            stopProfilerEvent(ctx);
            if(isQueryMonitorMode()){
                long end=System.currentTimeMillis();
                SysMonitor.addMonitorData( new MonitorData("Database",sql,end-start));
            }
            debugSqlLog(ctx, sql, null, null);
        }
        catch (SQLException e) {
            logSQL(sql, null);
            debugSqlLog(ctx, sql, null, e);
            LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
            throw new DBException(dbName,e);
        }
        finally {
            DBManager.close(rs);
            DBManager.close(stmt);
            DBManager.close(con);
            stopProfilerEvent(ctx);
            if(ctx != null){
                ctx.stopActivity();
            }

        }
        return data;
    }

    /**
         쿼리를  수행하고 DataSet을 커넥션은 자동을 닫힙니다.
     */
    public static DataSet executeQuery(Context ctx,String sql) throws DBException {
        return executeQuery(ctx,null, sql);
    }

    /**
         쿼리를  수행하고 커넥션은 자동을 닫힙니다.
     */
    public static int executeUpdate(Context ctx,String sql) throws DBException {
        return executeUpdate( ctx,null, sql);
    }

    /**
         쿼리를  수행하고 커넥션은 자동을 닫힙니다.
     */
    public static int executeUpdate(Context ctx,String dbName, String sql) throws
        DBException {
        if (sql == null)return -1;
        ctx = ContextLogger.getContext();
        debugSQL(ctx, sql, null);
        startProfilerEvent(ctx,sql);
        long start=System.currentTimeMillis();
        Connection con = null;
        Statement stmt = null;
        if(ctx != null){
            ctx.startActivity(Context.ACTIVITY_DB_WRITE);
        }
        try {

            con = DBManager.getConnection(ctx,dbName);
            stmt = createStatement(con);
            int result= stmt.executeUpdate(sql);
            if(isQueryMonitorMode()){
                long end=System.currentTimeMillis();
                SysMonitor.addMonitorData( new MonitorData("Database",sql,end-start));
            }
            debugSqlLog(ctx, sql, null, null);
            return result;
        }
        catch (SQLException e) {
            logSQL(sql, null);
            debugSqlLog(ctx, sql, null, e);
            LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
            throw new DBException(dbName,e);
        }
        finally {
            DBManager.close(stmt);
            DBManager.close(con);
            stopProfilerEvent(ctx);
            if(ctx != null){
                ctx.stopActivity();
            }
        }
    }

	/**
	 * 쿼리를  수행하고 커넥션은 자동을 닫힙니다.
	 * 처리건수가 0이면 DBException을 던진다.

	public static int executeUpdateByPK(Context ctx,String dbName, String sql) throws
	   DBException {
	   if (sql == null)return -1;
	   debugSQL(ctx, sql, null);
       long start=System.currentTimeMillis();
	   Connection con = null;
	   Statement stmt = null;
	   try {
	       con = DBManager.getConnection(ctx,dbName);
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
    public static DataSet executePreparedQuery(Context ctx,String sql, Object data[]) throws DBException {
        return executePreparedQuery(ctx,null, sql, data, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    }

    /**
     * PreparedStatement를 사용하여 Select 쿼리를 간단히 수행 시켜주는 메소드
     */
    public static DataSet executePreparedQuery(Context ctx,String dbName, String sql,
    		Object data[]) throws DBException {
    	/*
    	 * 2006.04.26 - sjChoi 추가
    	 */
        return executePreparedQuery(ctx,dbName, sql, data, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    }

    /**
     * PreparedStatement를 사용하여 Select 쿼리를 간단히 수행 시켜주는 메소드
     */
    public static DataSet executePreparedQuery(Context ctx,String sql, Object data[],
    		int resultSetType, int resultSetConcurrency) throws DBException {
    	/*
    	 * 2006.04.26 - sjChoi 추가
    	 */
        return executePreparedQuery( ctx,null, sql, data, resultSetType, resultSetConcurrency);
    }

    /**
     * PreparedStatement를 사용하여 Select 쿼리를 간단히 수행 시켜주는 메소드
     */
    public static DataSet executePreparedQuery(Context ctx,String dbName, String sql, Object data[],
    		int resultSetType, int resultSetConcurrency) throws DBException {
    	/*
    	 * 2006.04.26 - sjChoi 추가
    	 */
        return executePreparedQuery( ctx,dbName, sql, data, resultSetType, resultSetConcurrency, 0);
    }

    /**
     * PreparedStatement를 사용하여 Select 쿼리를 간단히 수행 시켜주는 메소드
     */
    public static DataSet executePreparedQuery(Context ctx,String dbName, String sql,
        Object data[], int resultSetType, int resultSetConcurrency, int skipCount) throws DBException {
        ctx = ContextLogger.getContext();

        debugSQL(ctx, sql, data);
        startProfilerEvent(ctx,sql);
        long start=System.currentTimeMillis();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        if(ctx != null){
            ctx.startActivity(Context.ACTIVITY_DB_READ);
        }
        try {

            con = getConnection(ctx,dbName);

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
            startProfilerEvent(ctx,"Data fetch");
            DataSet rset = toMemory(rs, skipCount);
            stopProfilerEvent(ctx);
            if(isQueryMonitorMode()){
                long end=System.currentTimeMillis();
                SysMonitor.addMonitorData( new MonitorData("Database",sql,end-start));
            }
            debugSqlLog(ctx, sql, data, null);
            return rset;
        }
        catch (SQLException e) {
            logSQL(sql, data);
            debugSqlLog(ctx, sql, data, e);
            LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
            throw new DBException(dbName,e);
        }
        catch (Exception e) {
            logSQL(sql, data);
            debugSqlLog(ctx, sql, data, e);
            LogManager.error(e.getMessage(),e);
            throw new SysException(e.toString());
        }
        finally {
            close(rs);
            close(pstmt);
            close(con);
            stopProfilerEvent(ctx);
            if(ctx !=null){
                ctx.stopActivity();
            }
        }
    }

    /**
     * PreparedStatement를 사용하여 insert,delete, update 쿼리를 간단히 수행 시켜주는 메소드
     * @return
     */
    public static int executePreparedUpdate(Context ctx, String sql, List params) {
        return executePreparedUpdate(ctx,null, sql, params.toArray());

    }
    /**
     * PreparedStatement를 사용하여 insert,delete, update 쿼리를 간단히 수행 시켜주는 메소드
     */
    public static int executePreparedUpdate(Context ctx,String sql, Object data[]) throws
        DBException {
        return executePreparedUpdate(ctx,null, sql, data);
    }

    /**
     * PreparedStatement를 사용하여 insert,delete, update 쿼리를 간단히 수행 시켜주는 메소드
     */
    public static int executePreparedUpdate(Context ctx,String sql, DataMap dataMap) throws
        DBException {

    	Map map ;
    	Object[] param = null;
    	ArrayList preparedSet = null;
		if( sql.toLowerCase().indexOf("update") != -1 ){

	    	try {
	    		map = ZqlUtil.getUpdate(sql+";");
	    	}
	    	catch (Exception e) {
	            logSQL(sql, null);
	            debugSqlLog(ctx, sql, null, e);
	            LogManager.error(e.getMessage(),e);
	            throw new SysException(e.toString());
	        }

	    	preparedSet = (ArrayList)map.get("preparedSet");//update 문일 경우 perparedSet 절 처리
	    	ArrayList where = (ArrayList)map.get("where");//update 문일 경우 where 절 처리

	    	preparedSet.addAll(where);


		} else if(sql.toLowerCase().indexOf("insert") != -1){
	    	try {
	    		map = ZqlUtil.getInsert(sql+";");
	    	}
	    	catch (Exception e) {
	            logSQL(sql, null);
	            debugSqlLog(ctx, sql, null, e);
	            LogManager.error(e.getMessage(),e);
	            throw new SysException(e.toString());
	        }

	    	preparedSet = (ArrayList)map.get("preparedColumns");//insert 문일 경우 cloumn이름 절만 처리

		} else {
	    	try {
	    		map = ZqlUtil.getUpdate(sql+";");
	    	}
	    	catch (Exception e) {
	            logSQL(sql, null);
	            debugSqlLog(ctx, sql, null, e);
	            LogManager.error(e.getMessage(),e);
	            throw new SysException(e.toString());
	        }

	    	preparedSet = (ArrayList)map.get("where");//delete 문일 경우 where 절만 처리

		}

    	param = new Object[preparedSet.size()];

    	for(int i = 0 ; i < preparedSet.size() ; i++){
			param[i] = dataMap.get(StringUtil.db2attr((String)preparedSet.get(i)));
		}


        return executePreparedUpdate(ctx,null, sql, param);
    }

    /**
         PreparedStatement를 사용하여 insert,delete, update 쿼리를 간단히 수행 시켜주는 메소드
     */
    public static int executePreparedUpdate(Context ctx,String dbName, String sql,
                                            Object data[]) throws DBException {

        ctx = ContextLogger.getContext();
        debugSQL(ctx, sql, data);
        startProfilerEvent(ctx,sql);
        long start=System.currentTimeMillis();
        Connection con = null;
        PreparedStatement pstmt = null;
        if(ctx != null){
            ctx.startActivity(Context.ACTIVITY_DB_WRITE);
        }
        try {
            con = getConnection(ctx,dbName);

            pstmt = prepareStatement(con, sql);

            //pstmt.setQueryTimeout(5);
            setValues(pstmt, data);
            int result = pstmt.executeUpdate();
            if(isQueryMonitorMode()){
                long end=System.currentTimeMillis();
                SysMonitor.addMonitorData( new MonitorData("Database",sql,end-start));
            }
            debugSqlLog(ctx, sql, data, null);
            return result;
        }
        catch (SQLException e) {
            logSQL(sql, data);
            debugSqlLog(ctx, sql, data, e);
            LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
            throw new DBException(dbName,e);
        }
        catch (Exception e) {
            logSQL(sql, data);
            debugSqlLog(ctx, sql, data, e);
            e.printStackTrace();
            LogManager.error(e.getMessage(),e);
            throw new SysException(e.toString());
        }
        finally {
            close(pstmt);
            close(con);
            stopProfilerEvent(ctx);
            if(ctx != null){
                ctx.stopActivity();
            }
        }
    }

    /**
     * PreparedStatement를 사용하여 insert,delete, update 쿼리를 간단히 수행 시켜주는 메소드
     * 처리건수가 0이면 DBException을 던진다.
	 */
	public static int executePreparedUpdateByPK(Context ctx,String dbName, String sql,
	                                       Object data[]) throws DBException {

	   ctx = ContextLogger.getContext();
	   debugSQL(ctx, sql, data);
       startProfilerEvent(ctx,sql);
       long start=System.currentTimeMillis();
	   Connection con = null;
	   PreparedStatement pstmt = null;
	   if(ctx != null){
	       ctx.startActivity(Context.ACTIVITY_DB_WRITE);
       }
	   try {
	       con = getConnection(ctx,dbName);

	       pstmt = prepareStatement(con, sql);

	       //pstmt.setQueryTimeout(5);
	       setValues(pstmt, data);

	       int result = pstmt.executeUpdate();
           if( result<=0) throw new DBException("처리 건수가 0건입니다.");
           if(isQueryMonitorMode()){
               long end=System.currentTimeMillis();
               SysMonitor.addMonitorData( new MonitorData("Database",sql,end-start));
           }
           debugSqlLog(ctx, sql, data, null);
           return result;
	   }
	   catch (SQLException e) {
	       logSQL(sql, data);
	       debugSqlLog(ctx, sql, data, e);
	       LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
	       throw new DBException(dbName,e);
	   }
	   catch (Exception e) {
	       logSQL(sql, data);
	       debugSqlLog(ctx, sql, data, e);
	       e.printStackTrace();
	       LogManager.error(e.getMessage(),e);
	       throw new SysException(e.toString());
	   }
	   finally {
	       close(pstmt);
	       close(con);
           stopProfilerEvent(ctx);
           if(ctx != null){
               ctx.stopActivity();
           }
	   }
	}

    /**
         PreparedStatement를 사용하여 insert,delete, update 쿼리를 간단히 수행 시켜주는 메소드
         -- 해당 에러코드와 일치하는 경우는 staticTrace를 프린트하지 않는다.
     */
    public static int executePreparedUpdateWithoutTrace(Context ctx,String sql, Object data[], int errCode) throws
        DBException {
        ctx = ContextLogger.getContext();
        debugSQL(ctx, sql, data);
        startProfilerEvent(ctx,sql);
        long start=System.currentTimeMillis();
        Connection con = null;
        PreparedStatement pstmt = null;
        if(ctx != null){
            ctx.startActivity(Context.ACTIVITY_DB_WRITE);
        }
        try {
            con = getConnection(ctx);

            pstmt = prepareStatement(con, sql);

            //pstmt.setQueryTimeout(5);
            setValues(pstmt, data);
            int result =  pstmt.executeUpdate();
            if(isQueryMonitorMode()){
                long end=System.currentTimeMillis();
                SysMonitor.addMonitorData( new MonitorData("Database",sql,end-start));
            }
            debugSqlLog(ctx, sql, data, null);
            return result;
        }
        catch (SQLException e) {

            if(e.getErrorCode() != errCode ) {
                logSQL(sql, data);
                debugSqlLog(ctx, sql, data, e);
                LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
            }
            throw new DBException(e);
        }
        catch (Exception e) {
            logSQL(sql, data);
            debugSqlLog(ctx, sql, data, e);
            LogManager.error(e.getMessage(),e);
            throw new SysException(e.toString());
        }
        finally {
            close(pstmt);
            close(con);
            stopProfilerEvent(ctx);
            if(ctx != null){
                ctx.stopActivity();
            }
        }
    }

    private static void setValues(PreparedStatement stmt, Object data[]) throws
        Exception {
		DBManager.setValues(stmt,data);
    }

    /**
     * 바인딩 변수에 넣을 파라미터값을 설정
     *
     * @param stmt CallableStatement객체
     * @param data 바인딩 변수에 넣을 파라미터값들
     * @return ArrayList
     * @throws Exception
     */
    private static ArrayList setValues(CallableStatement stmt, Object data[]) throws
        Exception {

    	return DBManager.setValues(stmt,data);
    }

    /**
     * DataSet을 Bean을 담고 있는 ArrayList형태로 변환
     */
    public static ArrayList toList(DataSet rs, Class clazz) {
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
                //LogManager.debug("비어 있는 객체 생성 성공");
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
            //LogManager.debug("조회된 data\n" + list.toString());
            return list;

        }
        catch (Exception e) {
            LogManager.error(e.getMessage(),e);
            throw new SysException("데이타  생성에 실패하였습니다\n원인-->" + e.getMessage());
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
    public static Map toMap(DataSet rs) {

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
     * DataSet을 Bean을 담고 있는 ArrayList형태로 변환
     */
    public static ArrayList toMapList(DataSet rs) {
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
                    if (metaData.getColumnType(i + 1) != Types.CLOB) {
                        map.put(columnNames[i], rs.getObject(columnNames[i]));
                    } else {
                        // read content column from CLOB TYPE
                        map.put(columnNames[i], dumpClob((Clob) rs.getClob(columnNames[i])));
                    }
                }
                list.add(map);
            }

            // LogManager.debug("조회된 data\n" + list.toString());
            return list;
        } catch (Exception e) {
            LogManager.error("ERROR", "해당 Bean 객체에 default 생성자 및 setXXX함수가 있는지 확인 하세요",e);
            throw new SysException("데이타  생성에 실패하였습니다\n원인-->" + e.getMessage());
        }
    }

    /**
     * CLOB 컬럼의 데이타를 String으로 읽어온다.
     *
     * @param clob
     * @return @throws
     *         Exception
     */
    public static String dumpClob(Clob clob) throws Exception {
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

    /**
     * CLOB Type 의 글내용을 스트림을 통해 DB에 넣는다.
     *
     * @param txMgr
     * @param query
     * @param data
     * @throws DBException
     */
    public static void writeClob(TxManager txMgr, String sql, Object[] data, String lobData)
            throws DBException {
        if (sql == null)
            return;

        debugSQL(null, sql, null);
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = txMgr.getConnection();
            pstmt = prepareStatement(con, sql);
            setValues(pstmt, data);
            rs = pstmt.executeQuery(sql);
            if (rs.next()) {
                Clob clob = (Clob) rs.getClob(1);
                Writer writer = clob.setCharacterStream(1);
//                Writer writer = clob.getCharacterOutputStream();
                writer.write(lobData); // write clob.
                writer.close();
            }

        } catch (IOException e) {
            LogManager.error("ERROR", e.getMessage());
            throw new DBException("CLOB 데이타를 write 하는 도중 에러가 발생했습니다.");
        } catch (SQLException e) {
            logSQL(sql, data);
            LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
            throw new DBException(e);
        } catch (Exception e) {
            logSQL(sql, data);
            LogManager.error(e.getMessage(),e);
            throw new SysException(e.toString());
        } finally {
            close(rs);
            close(pstmt);
        }
    }


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
            LogManager.error("ERROR", "해당 Bean 객체에 default 생성자 및 setXXX함수가 있는지 확인 하세요",e);
            throw new SysException("데이타 객체 생성에 실패하였습니다\n원인-->" + e.getMessage());
        }

    }
    public static void debugSQL(Context ctx, String sql, Object[] data) {
        if(!isQueryMonitorMode()) return;
        String loggingSql = sql;
        if (data != null) {
            loggingSql = getLoggingSql(ctx, sql, data);
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

    public static String getLoggingSql(Context ctx, String sql, Object[] data) {
        String loggingSql = sql;
        try {
            if (data != null) {
                for (int i = 0; i < data.length; i++) {
                    loggingSql = StringUtil.replace(loggingSql, "?",
                        "'" + data[i].toString().replace('?','^') + "'");
                }
                loggingSql = loggingSql.replace('^','?');
                if(ctx != null)
                {
//                	LogManager.info("SQL", ctx.getUserId()+"|"+ctx.getTrxSerNo()+"|"+(loggingSql.replaceAll("\r"," ")).replaceAll("\n"," "));
                }
                return "Prepared sql : " + sql + "\n 값넣은 sql :" + loggingSql;
            }else{
            	return sql;
            }
        }
        catch (Exception e) {}

        return loggingSql;
    }


    public static void debugSqlLog(Context ctx, String sql, Object[] data, Exception ex) {
        String loggingSql = sql;
        try {
            if(ctx != null)
            {
	            if (data != null) {
	                for (int i = 0; i < data.length; i++) {
	                    loggingSql = StringUtil.replace(loggingSql, "?",
	                        "'" + data[i].toString().replace('?','^') + "'");
	                }
	                loggingSql = loggingSql.replace('^','?');

	            }
	            String errorMessage = "";
	            if(ex!=null){
	            	if(ex instanceof SQLException) errorMessage = "ERROR_CODE:"+((SQLException) ex).getErrorCode()+",ERROR_REASON:"+ex.getMessage();
	            	else errorMessage = "ERROR_REASON:"+ex.getMessage();
	            }

	            LogManager.info("SQL", ctx.getUserId()+"|"+ctx.getTrxSerNo()+"|"+errorMessage.trim()+"|"+(loggingSql.replaceAll("\r"," ")).replaceAll("\n"," "));

            }
        }
        catch (Exception e) {}

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
    public static ArrayList executeQueryToMapList(Context ctx,String sql) throws
        DBException {
        return executeQueryToMapList(ctx, null, sql);
    }

    /**
     * 해당 쿼리에 대하여 결과를 Objec[]을 포함하는 ArrayList로 반환하는 함수
     * DB가 디폴트 지정인 경우
     * @param sql String
     * @throws DBException
     * @return ArrayList
     */
    public static ArrayList executeQueryToArrayList(Context ctx,String sql) throws
        DBException {
        return executeQueryToArrayList(ctx,null, sql);
    }

    /**
     * DB와 쿼리에 대한 결과를 Map을 포함하는 ArrayList타입으로 반환하는 함수
     * @param dbName String
     * @param sql String
     * @throws DBException
     * @return ArrayList
     */
    public static ArrayList executeQueryToMapList(Context ctx,String dbName, String sql) throws
        DBException {

        ctx = ContextLogger.getContext();

        debugSQL(ctx, sql, null);
        startProfilerEvent(ctx,sql);
        long start=System.currentTimeMillis();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        ArrayList dataArr = null;
        if(ctx != null){
            ctx.startActivity(Context.ACTIVITY_DB_READ);
        }
        try {

            con = getConnection(ctx,dbName);
            stmt = createStatement(con);
            rs = stmt.executeQuery(sql);

            openedResultSetCount++;
            startProfilerEvent(ctx,"Data fetch");
            dataArr = toMapList(rs);
            stopProfilerEvent(ctx);
            if(isQueryMonitorMode()){
            long end=System.currentTimeMillis();
            SysMonitor.addMonitorData( new MonitorData("Database",sql,end-start));
            }
            debugSqlLog(ctx, sql, null, null);
            return dataArr;
        }
        catch (SQLException e) {
            logSQL(sql, null);
            debugSqlLog(ctx, sql, null, e);
            LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
            throw new DBException(dbName,e);
        }
        catch (Exception e) {
            logSQL(sql, null);
            debugSqlLog(ctx, sql, null, e);
            LogManager.error(e.getMessage(),e);
            throw new SysException(e.toString());
        }
        finally {
            close(rs);
            close(stmt);
            close(con);
            stopProfilerEvent(ctx);
            if(ctx != null){
                ctx.stopActivity();
            }
        }
    }

    /**
     * DB와 쿼리에 대한 결과를 Object[]을 포함하는 ArrayList타입으로 반환하는 함수
     * @param dbName String
     * @param sql String
     * @throws DBException
     * @return ArrayList
     */
    public static ArrayList executeQueryToArrayList(Context ctx,String dbName, String sql) throws
        DBException {
        ctx = ContextLogger.getContext();
        debugSQL(ctx, sql, null);
        startProfilerEvent(ctx,sql);
        long start=System.currentTimeMillis();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        ArrayList dataArr = null;
        if(ctx != null){
            ctx.startActivity(Context.ACTIVITY_DB_READ);
        }
        try {

            con = getConnection(ctx,dbName);
            stmt = createStatement(con);
            rs = stmt.executeQuery(sql);

            openedResultSetCount++;
            startProfilerEvent(ctx,"Data fetch");
            dataArr = toArrayList(rs);
            stopProfilerEvent(ctx);
            if(isQueryMonitorMode()){
            long end=System.currentTimeMillis();
            SysMonitor.addMonitorData( new MonitorData("Database",sql,end-start));
            }
            debugSqlLog(ctx, sql, null, null);

            return dataArr;
        }
        catch (SQLException e) {
            logSQL(sql, null);
            debugSqlLog(ctx, sql, null, e);
            LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
            throw new DBException(dbName,e);
        }
        catch (Exception e) {
            logSQL(sql, null);
            debugSqlLog(ctx, sql, null, e);
            LogManager.error(e.getMessage(),e);
            throw new SysException(e.toString());
        }
        finally {
            close(rs);
            close(stmt);
            close(con);
            stopProfilerEvent(ctx);
            if(ctx != null){
                ctx.stopActivity();
            }
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
    public static ArrayList executePreparedQueryToMapList(Context ctx,String sql,
        Object data[]) throws DBException {
        return executePreparedQueryToMapList(ctx, null, sql, data);
    }

    /**
     * 쿼리와 파라미터를 받아 Obejct[]을 포함하는 ArrayList로 반환하는 함수
     * DB가 디폴트인 경우
     * @param sql String
     * @param data Object[]
     * @throws DBException
     * @return ArrayList
     */
    public static ArrayList executePreparedQueryToArrayList(Context ctx,String sql,
        Object data[]) throws DBException {
        return executePreparedQueryToArrayList(ctx, null, sql, data);
    }

    /**
     * 쿼리와 파라미터를 받아 Map을 포함하는 ArrayList로 반환하는 함수
     * @param dbName String
     * @param sql String
     * @param data Object[]
     * @throws DBException
     * @return ArrayList
     */
    public static ArrayList executePreparedQueryToMapList(Context ctx,String dbName,
        String sql, Object data[]) throws DBException {
        ctx = ContextLogger.getContext();

        debugSQL(ctx, sql, data);
        startProfilerEvent(ctx,sql);
        long start=System.currentTimeMillis();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList dataArr = null;
        if(ctx != null){
            ctx.startActivity(Context.ACTIVITY_DB_READ);
        }
        try {
            con = getConnection(ctx,dbName);
            pstmt = prepareStatement(con, sql);

            //pstmt.setQueryTimeout(5);
            setValues(pstmt, data);
            rs = pstmt.executeQuery();
            openedResultSetCount++;
            startProfilerEvent(ctx,"Data fetch");
            dataArr = toMapList(rs);
            stopProfilerEvent(ctx);
            if(isQueryMonitorMode()){
                long end=System.currentTimeMillis();
                SysMonitor.addMonitorData( new MonitorData("Database",sql,end-start));
            }
            debugSqlLog(ctx, sql, data, null);
            return dataArr;
        }
        catch (SQLException e) {
            logSQL(sql, data);
            debugSqlLog(ctx, sql, data, e);
            LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
            throw new DBException(dbName,e);
        }
        catch (Exception e) {
            logSQL(sql, data);
            debugSqlLog(ctx, sql, data, e);
            LogManager.error(e.getMessage(),e);
            throw new SysException(e.toString());
        }
        finally {
            close(rs);
            close(pstmt);
            close(con);
            stopProfilerEvent(ctx);
            if(ctx!= null){
                ctx.stopActivity();
            }
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
    public static ArrayList executePreparedQueryToArrayList(Context ctx,String dbName,
        String sql, Object data[]) throws DBException {
        ctx = ContextLogger.getContext();

        debugSQL(ctx, sql, data);
        startProfilerEvent(ctx,sql);
        long start=System.currentTimeMillis();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList dataArr = null;
        if(ctx != null){
            ctx.startActivity(Context.ACTIVITY_DB_READ);
        }
        try {
            con = getConnection(ctx,dbName);
            pstmt = prepareStatement(con, sql);

            //pstmt.setQueryTimeout(5);
            setValues(pstmt, data);
            rs = pstmt.executeQuery();
            openedResultSetCount++;
            startProfilerEvent(ctx,"Data fetch");
            dataArr = toArrayList(rs);
            stopProfilerEvent(ctx);
            if(isQueryMonitorMode()){
            long end=System.currentTimeMillis();
            SysMonitor.addMonitorData( new MonitorData("Database",sql,end-start));
            }
            debugSqlLog(ctx, sql, data, null);
            return dataArr;
        }
        catch (SQLException e) {
            logSQL(sql, data);
            debugSqlLog(ctx, sql, data, e);
            LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
            throw new DBException(dbName,e);
        }
        catch (Exception e) {
            logSQL(sql, data);
            debugSqlLog(ctx, sql, data, e);
            LogManager.error(e.getMessage(),e);
            throw new SysException(e.toString());
        }
        finally {
            close(rs);
            close(pstmt);
            close(con);
            stopProfilerEvent(ctx);
            if(ctx != null){
                ctx.stopActivity();
            }
        }
    }

    /**
     * 쿼리에 대한 결과(단독)를 Map형태로 반환하는 함수
     * DB가 디폴트인 경우
     * @param sql String
     * @throws DBException
     * @return Map
     */
    public static Map executeQueryToMap(Context ctx,String sql) throws DBException {
        return executeQueryToMap(ctx,null, sql);
    }

    /**
     * 쿼리에 대한 결과(단독)를 Map형태로 반환하는 함수
     * @param sql String
     * @throws DBException
     * @return Map
     */
    public static Map executeQueryToMap(Context ctx,String dbName, String sql) throws
        DBException {
        ArrayList data = executeQueryToMapList(ctx,dbName, sql);
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
    public static Map executePreparedQueryToMap(Context ctx,String sql, Object[] param) throws
        DBException {
        return executePreparedQueryToMap(ctx,null, sql, param);
    }

    /**
     * 쿼리를  수행하고 ArrayList 리턴 커넥션은 자동을 닫힙니다.
     */
    public static Map executePreparedQueryToMap(Context ctx,String dbName, String sql,
                                                Object[] data) throws
        DBException {
        ctx = ContextLogger.getContext();

        debugSQL(ctx, sql, data);
        startProfilerEvent(ctx,sql);
        long start=System.currentTimeMillis();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Map dataMap = null;
        if(ctx != null){
            ctx.startActivity(Context.ACTIVITY_DB_READ);
        }
        try {
            con = getConnection(ctx,dbName);
            pstmt = prepareStatement(con, sql);
            setValues(pstmt, data);
            rs = pstmt.executeQuery();
            openedResultSetCount++;
            startProfilerEvent(ctx,"Data fetch");
            dataMap = toMap(rs);
            stopProfilerEvent(ctx);
            if(isQueryMonitorMode()){
            long end=System.currentTimeMillis();
            SysMonitor.addMonitorData( new MonitorData("Database",sql,end-start));
            }
            debugSqlLog(ctx, sql, data, null);
            return dataMap;
        }
        catch (SQLException e) {
            logSQL(sql, data);
            debugSqlLog(ctx, sql, data, e);
            LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
            throw new DBException(dbName,e);
        }
        catch (Exception e) {
            logSQL(sql, data);
            debugSqlLog(ctx, sql, data, e);
            LogManager.error(e.getMessage(),e);
            throw new SysException(e.toString());
        }
        finally {
            close(rs);
            close(pstmt);
            close(con);
            stopProfilerEvent(ctx);
            if(ctx != null){
                ctx.stopActivity();
            }
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
    public static ArrayList executeQueryToBeanList(Context ctx,String dbName, String sql,
        Class clazz) throws DBException {
        if (sql == null)return null;
        ctx = ContextLogger.getContext();

        debugSQL(ctx, sql, null);
        startProfilerEvent(ctx,sql);
        long start=System.currentTimeMillis();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        ArrayList data = null;

        if(ctx != null){
            ctx.startActivity(Context.ACTIVITY_DB_READ);
        }
        try {
            con = DBManager.getConnection(ctx,dbName);
            stmt = createStatement(con);
            rs = stmt.executeQuery(sql);
            openedResultSetCount++;
            startProfilerEvent(ctx,"Data fetch");
            data = toList(rs, clazz);
            stopProfilerEvent(ctx);
            if(isQueryMonitorMode()){
            long end=System.currentTimeMillis();
            SysMonitor.addMonitorData( new MonitorData("Database",sql,end-start));
            }
            debugSqlLog(ctx, sql, null, null);
        }
        catch (SQLException e) {
            LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
            logSQL(sql, null);
            debugSqlLog(ctx, sql, null, e);
            throw new DBException(dbName,e);
        }
        finally {
            DBManager.close(rs);
            DBManager.close(stmt);
            DBManager.close(con);
            stopProfilerEvent(ctx);
            if(ctx !=null){
                ctx.stopActivity();
            }
        }
        return data;
    }

    /**
     * 쿼리를  수행하고 ArrayList 리턴 커넥션은 자동을 닫힙니다.
     */
    public static Object executeQueryToBean(Context ctx,String sql, Class clazz) throws
        DBException {
        return executeQueryToBean(ctx,null, sql, clazz);
    }

    /**
     * 쿼리를  수행하고 ArrayList 리턴 커넥션은 자동을 닫힙니다.
     */
    public static Object executeQueryToBean(Context ctx,String dbName, String sql,
                                            Class clazz) throws DBException {
        ArrayList data = executeQueryToBeanList(ctx,dbName, sql, clazz);
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
    public static Object executePreparedQueryToBean(Context ctx,String sql,
        Object[] param, Class clazz) throws DBException {
        return executePreparedQueryToBean(ctx,null, sql, param, clazz);
    }

    /**
     * 쿼리를  수행하고 ArrayList 리턴 커넥션은 자동을 닫힙니다.
     */
    public static Object executePreparedQueryToBean(Context ctx,String dbName, String sql,
        Object[] param, Class clazz) throws DBException {
        ArrayList data = executePreparedQueryToBeanList(ctx,dbName, sql, param,
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
    public static ArrayList executeQueryToBeanList(Context ctx,String sql, Class clazz) throws
        DBException {
        return executeQueryToBeanList(null, sql, clazz);
    }

    /**
         PreparedStatement를 사용하여 Select 쿼리를 간단히 수행 시켜주는 메소드
     */
    public static ArrayList executePreparedQueryToBeanList(Context ctx,String dbName,
        String sql, Object data[],
        Class clazz) throws DBException {
        ctx = ContextLogger.getContext();

        debugSQL(ctx, sql, data);
        startProfilerEvent(ctx,sql);
        long start=System.currentTimeMillis();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        if(ctx != null){
            ctx.startActivity(Context.ACTIVITY_DB_READ);
        }
        try {
            con = getConnection(ctx,dbName);
            pstmt = prepareStatement(con, sql);

            //pstmt.setQueryTimeout(5);
            setValues(pstmt, data);
            rs = pstmt.executeQuery();
            openedResultSetCount++;
            startProfilerEvent(ctx,"Data fetch");
            ArrayList list = toList(rs, clazz);
            stopProfilerEvent(ctx);
            if(isQueryMonitorMode()){
            long end=System.currentTimeMillis();
            SysMonitor.addMonitorData( new MonitorData("Database",sql,end-start));
            }
            debugSqlLog(ctx, sql, data, null);
            return list;
        }
        catch (SQLException e) {
            logSQL(sql, data);
            debugSqlLog(ctx, sql, data, e);
            LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
            throw new DBException(dbName,e);
        }
        catch (Exception e) {
            logSQL(sql, data);
            debugSqlLog(ctx, sql, data, e);
            LogManager.error(e.getMessage(),e);
            throw new SysException(e.toString());
        }
        finally {
            close(rs);
            close(pstmt);
            close(con);
            stopProfilerEvent(ctx);
            if(ctx != null){
                ctx.stopActivity();
            }
        }
    }

    /**
         PreparedStatement를 사용하여 Select 쿼리를 간단히 수행 시켜주는 메소드
     */
    public static ArrayList executePreparedQueryToBeanList(Context ctx,String sql,
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
    public static DataSet executePreparedTxQuery(Context ctx,TxManager txMgr, String sql, Object data[]) throws DBException {
       ctx = ContextLogger.getContext();

       debugSQL(ctx, sql, data);
       startProfilerEvent(ctx,sql);
       long start=System.currentTimeMillis();
       Connection con = null;
       PreparedStatement pstmt = null;
       ResultSet rs = null;
       if(ctx != null){
           ctx.startActivity(Context.ACTIVITY_DB_READ);
       }
       try {
           con = txMgr.getConnection();
           pstmt = prepareStatement(con, sql);

           //pstmt.setQueryTimeout(5);
           setValues(pstmt, data);
           rs = pstmt.executeQuery();

           openedResultSetCount++;
           startProfilerEvent(ctx,"Data fetch");
           DataSet rset = toMemory(rs);
           stopProfilerEvent(ctx);
           if(isQueryMonitorMode()){
            long end=System.currentTimeMillis();
           SysMonitor.addMonitorData( new MonitorData("Database",sql,end-start));
            }
           debugSqlLog(ctx, sql, data, null);
           return rset;
       } catch (SQLException e) {
           logSQL(sql, data);
           debugSqlLog(ctx, sql, data, e);
           LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
           throw new DBException(e);
       } catch (Exception e) {
           logSQL(sql, data);
           debugSqlLog(ctx, sql, data, e);
           LogManager.error(e.getMessage(),e);
           throw new SysException(e.toString());
       } finally {
           close(rs);
           close(pstmt);
           stopProfilerEvent(ctx);
           if(ctx != null){
               ctx.stopActivity();
           }
       }//end try catch finally
    }//end of executePreparedTxQuery()

    /**
     * 쿼리를  수행하고 DataSet을 커넥션은 자동을 닫힙니다.
     *
     * 동일 트랜잭션 내에서 update 한 결과에 대하여 select 하기 위해
     * 같은 Connection 으로 질의를 수행한다.
     *
     * @author 이종원
     * @since 2004.10.05
     */
    public static DataSet executeTxQuery(Context ctx,TxManager txMgr, String sql) throws DBException {

       ctx = ContextLogger.getContext();
       debugSQL(ctx, sql, null);
       startProfilerEvent(ctx,sql);
       long start=System.currentTimeMillis();
       Connection con = null;
       Statement stmt = null;
       ResultSet rs = null;
       if(ctx != null){
           ctx.startActivity(Context.ACTIVITY_DB_READ);
       }
       try {
           con = txMgr.getConnection();
           stmt = createStatement(con);

           rs = stmt.executeQuery(sql);
           openedResultSetCount++;
           startProfilerEvent(ctx,"Data fetch");
           DataSet rset = DBManager.toMemory(rs);
           stopProfilerEvent(ctx);
           if(isQueryMonitorMode()){
            long end=System.currentTimeMillis();
           SysMonitor.addMonitorData( new MonitorData("Database",sql,end-start));
            }
           debugSqlLog(ctx, sql, null, null);
           return rset;
       } catch (SQLException e) {
           logSQL(sql, null);
           LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
           throw new DBException(e);
       } catch (Exception e) {
           logSQL(sql, null);
           LogManager.error(e.getMessage(),e);
           throw new SysException(e.toString());
       } finally {
           close(rs);
           close(stmt);
           stopProfilerEvent(ctx);
           if(ctx != null){
               ctx.stopActivity();
           }
       }//end try catch finally
    }//end of executeTxQuery()


    /**
     트렌젝션 관리를 위해 하나의 커넥션을 공유하여 PreparedStatement를 사용하여 insert,delete, update 쿼리를 간단히 수행 시켜주는 메소드
     */
    public static  int executePreparedTxUpdate(Context ctx,TxManager txMgr , String sql, Object data[]) throws DBException {

        ctx = ContextLogger.getContext();

        debugSQL(ctx, sql,data);
        startProfilerEvent(ctx,sql);

        long start=System.currentTimeMillis();
        Connection con = null;
        PreparedStatement pstmt = null;
        if(ctx != null){
            ctx.startActivity(Context.ACTIVITY_DB_WRITE);
        }
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
            debugSqlLog(ctx, sql, data, null);
            return result;
        } catch(SQLException e) {
            //txMgr.rollback(); //개발자가 책임지고 rollback 한다.
            logSQL(sql,data);
            debugSqlLog(ctx, sql, data, e);
            LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
            throw new DBException(e);
        } catch(Exception e) {
            logSQL(sql,data);
            debugSqlLog(ctx, sql, data, e);
            LogManager.error(e.getMessage(),e);
            throw new SysException(e.toString());
        } finally {
            close(pstmt);
            stopProfilerEvent(ctx);
            if(ctx != null){
                ctx.stopActivity();
            }
        }
    }

    /**
     * 트렌젝션 관리를 위해 하나의 커넥션을 공유하여 PreparedStatement를 사용하여 insert,delete, update 쿼리를 간단히 수행 시켜주는 메소드
     * 처리건수가 0이면 DBException을 던진다.
     */
   public static  int executePreparedTxUpdateByPK(TxManager txMgr , String sql, Object data[]) throws DBException {

       debugSQL(null, sql,data);
       startProfilerEvent(txMgr.ctx,sql);

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
           LogManager.error(e.getMessage(),e);
           throw new SysException(e.toString());
       } finally {
           close(pstmt);
           stopProfilerEvent(txMgr.ctx);
       }
   }
    /**
     * 트렌젝션 관리를 위해 하나의 커넥션을 공유하여 Statement를 사용하여
     * insert,delete, update 쿼리를 간단히 수행 시켜주는 메소드
     */
    public static  int executeTxUpdate(Context ctx,TxManager txMgr , String sql ) throws DBException {

        ctx = ContextLogger.getContext();

    	debugSQL(ctx, sql,null);
        startProfilerEvent(ctx,sql);

        long start=System.currentTimeMillis();
    	Connection con = null;
    	Statement pstmt = null;
    	if(ctx !=null){
    	    ctx.startActivity(Context.ACTIVITY_DB_WRITE);
        }
    	try {
    		con = txMgr.getConnection();
    		pstmt = createStatement(con);

    		int result =  pstmt.executeUpdate(sql);
            if(isQueryMonitorMode()){
            long end=System.currentTimeMillis();
            SysMonitor.addMonitorData( new MonitorData("Database",sql,end-start));
            }
            debugSqlLog(ctx, sql, null, null);
            return result;
    	} catch(SQLException e) {
    		//txMgr.rollback(); //개발자가 책임지고 rollback 한다.
    		logSQL(sql,null);
    		debugSqlLog(ctx, sql, null, e);
    		LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
    		throw new DBException(e);
    	} catch(Exception e) {
    		logSQL(sql,null);
    		debugSqlLog(ctx, sql, null, e);
    		LogManager.error(e.getMessage(),e);
    		throw new SysException(e.toString());
    	} finally {
    		close(pstmt);
            stopProfilerEvent(ctx);
            if(ctx != null){
                ctx.stopActivity();
            }
    	}
    }

    /**
     * 트렌젝션 관리를 위해 하나의 커넥션을 공유하여 Statement를 사용하여
     * insert,delete, update 쿼리를 간단히 수행 시켜주는 메소드
     * 처리건수가 0이면 DBException을 던진다.
     */
    public static  int executeTxUpdateByPK(TxManager txMgr , String sql ) throws DBException {

    	debugSQL(null, sql,null);
        startProfilerEvent(txMgr.ctx,"Data fetch");

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
    		LogManager.error(e.getMessage(),e);
    		throw new SysException(e.toString());
    	} finally {
    		close(pstmt);
            stopProfilerEvent(txMgr.ctx);
    	}
    }
    /**
     트렌젝션 관리를 위해 하나의 커넥션을 공유하여 PreparedStatement를 사용하여 insert,delete, update 쿼리를 간단히 수행 시켜주는 메소드
     -- 해당 에러코드와 일치하는 경우는 staticTrace를 프린트하지 않는다.
     */
    public static  int executePreparedTxUpdateWithoutTrace(TxManager txMgr , String sql, Object data[], int errCode) throws DBException {

        debugSQL(null, sql,data);
        startProfilerEvent(txMgr.ctx,sql);

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
            LogManager.error(e.getMessage(),e);
            throw new SysException(e.toString());
        } finally {
            close(pstmt);
            stopProfilerEvent(txMgr.ctx);
        }
    }

    /**
    * default db에 한번에 10건씩 batch 처리를 한다.
    **/
    public  static void executeBatch(Context ctx,String sql, ArrayList paramList){
        executeBatch(ctx,null, sql, paramList);
     }
    /**
     * 해당 db에 한번에 10건씩 batch 처리를 한다.
     **/
    public  static void executeBatch(Context ctx,String dbName,String sql, ArrayList paramList){
        executeBatch(ctx,dbName, sql, paramList,10);
    }

    /**
    *  해당 db에 한번에 executeUnitCount건씩 batch 처리를 한다.
    **/
    public  static void executeBatch(Context ctx,String dbName,String sql, ArrayList paramList,int executeUnitCount){

        ctx = ContextLogger.getContext();

        debugSQL(ctx, sql,null);

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

        ctx = ContextLogger.getContext();
        startProfilerEvent(ctx,"Batch:"+sql);
        if(ctx != null){
            ctx.startActivity(Context.ACTIVITY_DB_WRITE);
        }
        try {
            txMgr = new TxManager(ctx,dbName);
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
            debugSqlLog(ctx, sql, data, null);
        } catch(SQLException e) {
            txMgr.rollback();
            logSQL(sql,data);
            debugSqlLog(ctx, sql, data, e);
            LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
            throw new DBException(dbName,e);
        } catch(Exception e) {
            txMgr.rollback();
            logSQL(sql,data);
            debugSqlLog(ctx, sql, data, e);
            LogManager.error(e.getMessage(),e);
            throw new SysException(e.toString());
        } finally {
            close(pstmt);
            txMgr.end();
            stopProfilerEvent(ctx);
            if(ctx != null){
                ctx.stopActivity();
            }
        }
    }
    /**
    * 10건씩 batch 처리를 한다.
    **/
    public static void executeTxBatch(TxManager txMgr,String sql, ArrayList paramList){
        executeTxBatch(txMgr, null, sql, paramList);
     }

    /**
     * 10건씩 batch 처리를 한다.
     **/
     public static void executeTxBatch(TxManager txMgr,String dbName,String sql, ArrayList paramList){
         executeTxBatch(txMgr, dbName, sql, paramList,10);
      }

     /**
      * 해당 db에 한번에 executeUnitCount만큼   batch 처리를 한다.
      */
     public static void executeTxBatch(TxManager txMgr, DBContext dbCtx, String sql , DataSet dataSet, int executeUnitCount) {
     	LogManager.debug("##### "+dataSet.getClass().getName()+" #####");

     	if(dataSet instanceof DataSet) {
         	ArrayList list = new ArrayList();

     		while(dataSet.next()){
     			//DataMap map = dataSet.toDataMap();
     			Object obj = dataSet.getCurrentRow();
     			list.add(obj);
     		}
     		executeTxBatch(txMgr, dbCtx.getDbName(), sql , list ,executeUnitCount);
     	}

     }

    /**
    * 지정된 건수 단위로  batch 처리를 한다.
    **/
    public  static void executeTxBatch(TxManager txMgr,String dbName,
            String sql, ArrayList paramList, int executeUnitCount){

        debugSQL(null, sql,null);

        int paramSize = paramList.size();
        Object[] data = null;
        if(paramSize==0) {
            LogManager.info("수행할 파라미터에 값이 업습니다.");
            return ;
        }

        startProfilerEvent(txMgr.ctx,"Batch:"+sql);

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
            LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage()
            		+", next Exception:"+(e.getNextException()==null?"":e.getNextException().getMessage()),e);
            throw new DBException(dbName,e);
        } catch(Exception e) {
            //txMgr.rollback();
            logSQL(sql,data);
            LogManager.error(e.getMessage(),e);
            throw new SysException(e.toString());
        } finally {
            close(pstmt);
            stopProfilerEvent(txMgr.ctx);
        }
    }

    /**
     * 2004. 10. 29. 이종원 작성
     * @param sql
     * @param paramList
     * @return
     * 설명:ArrayList형태로 파라미터를 받아서 쿼리 수행
     */
    public static DataSet executePreparedQuery(Context ctx,
            String sql, ArrayList paramList) {
        return executePreparedQuery(ctx,sql,paramList.toArray());
    }

    /**
     * 2004. 10. 29. 이종원 작성
     * @param sql
     * @param paramList
     * @return
     * 설명:ArrayList형태로 파라미터를 받아서 쿼리 수행
     */
    public static DataSet executePreparedQuery(Context ctx,String dbName,
            String sql, ArrayList paramList) {
        return executePreparedQuery(ctx,dbName,sql,paramList.toArray(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    }

    /**
     * 2004. 10. 29. 이종원 작성
     * @param Context
     * @param sql
     * @param DataMap
     * @return
     * 설명:DataMap형태로 파라미터를 받아서 쿼리 수행
     */
    public static DataSet executePreparedQuery(Context ctx,
            String sql, DataMap map) {

        return executePreparedQuery(ctx,null,sql,map);
    }

    /**
     * 2004. 10. 29. 이종원 작성
     * @param Context
     * @param sql
     * @param DataMap
     * @return
     * 설명:DataMap형태로 파라미터를 받아서 쿼리 수행
     */
    public static DataSet executePreparedQuery(Context ctx,String dbName,
            String sql, DataMap dataMap) {
    	Map map ;

    	try {
    		map = ZqlUtil.getSelect(sql+";");
    	}
    	catch (Exception e) {
            logSQL(sql, null);
            debugSqlLog(ctx, sql, null, e);
            LogManager.error(e.getMessage(),e);
            throw new SysException(e.toString());
        }
    	ArrayList where = (ArrayList)map.get("where");//select 문일 경우 where 절만 처리

    	Object[] param = new Object[where.size()];

		for(int i = 0 ; i < where.size() ; i++){
			param[i] = dataMap.get(StringUtil.db2attr((String)where.get(i)));
		}

        return executePreparedQuery(ctx,dbName,sql,param, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    }



    /**
     * 2004. 10. 29. 이종원 작성
     * @param sql
     * @param paramList
     * @return
     * 설명:ArrayList형태로 파라미터를 받아서 쿼리 수행
     */
    public static ArrayList executePreparedQueryToMapList(
            Context ctx,String sql, ArrayList paramList) {
        return executePreparedQueryToMapList(ctx,sql,paramList.toArray());
    }

    /**
     * @param sql
     * @param paramList
     * @return
     * 설명:ArrayList형태로 파라미터를 받아서 쿼리 수행
     */
    public static ArrayList executePreparedQueryToArrayList(
            Context ctx, String sql, ArrayList paramList) {
        return executePreparedQueryToArrayList(ctx,sql,paramList.toArray());
    }

    /**
     * 2004. 10. 29. 이종원 작성
     * @param sql
     * @param paramList
     * @return
     * 설명:ArrayList형태로 파라미터를 받아서 쿼리 수행
     */
    public static ArrayList executePreparedQueryToMapList(Context ctx,String dbName,
			String sql, ArrayList paramList) {
        return executePreparedQueryToMapList(ctx,dbName,sql,paramList.toArray());
    }

    /**
     * @param sql
     * @param paramList
     * @return
     * 설명:ArrayList형태로 파라미터를 받아서 쿼리 수행
     */
    public static ArrayList executePreparedQueryToArrayList(Context ctx,String dbName,
			String sql, ArrayList paramList) {
        return executePreparedQueryToArrayList(ctx,dbName,sql,paramList.toArray());
    }

    /**
     * 2004. 10. 29. 이종원 작성
     * @param sql
     * @param paramList
     * @return
     * 설명:ArrayList형태로 파라미터를 받아서 쿼리 수행
     */
    public static Map executePreparedQueryToMap(
            Context ctx,String sql, ArrayList paramList) {
        return executePreparedQueryToMap(ctx,sql,paramList.toArray());
    }

    /**
     * 2004. 10. 29. 이종원 작성
     * @param sql
     * @param paramList
     * @return
     * 설명:ArrayList형태로 파라미터를 받아서 쿼리 수행
     */
    public static Map executePreparedQueryToMap(Context ctx,String dbName,
														  String sql, ArrayList paramList) {
        return executePreparedQueryToMap(ctx,dbName,sql,paramList.toArray());
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

        //debugSQL(ctx, sql,data);
        startProfilerEvent(txMgr.ctx,"Batch:"+sql);

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
	                pstmt.addBatch();
	                //resultCount[i] = pstmt.executeUpdate();
                }catch(SQLException se){
                    if(isDupAllowed && se.getErrorCode()==2627){
                        LogManager.info("key 중복이 발생하여 해당 건은 실행하지 않고 통과하였습니다. (" + Arrays.asList(data).toString() + ")");
                    }else{
                        throw se;
                    }
                }
            }

            resultCount = pstmt.executeBatch();
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
            LogManager.error(e.getMessage(),e);
            throw new SysException(e.toString());
        } finally {
            close(pstmt);
            stopProfilerEvent(txMgr.ctx);
        }
    }

    private static void startProfilerEvent(Context ctx,String sql){
        if(ctx ==null) return;
        Profiler prof = ctx.getProfiler();
        if(prof != null){
            prof.startEvent("Query수행  : " + getTempSql(sql));
        }
    }

    private static void stopProfilerEvent(Context ctx){
        if(ctx ==null) return;
        Profiler prof = ctx.getProfiler();
        if(prof != null){
            prof.stopEvent();
        }
    }

    public static String getTempSql(String sql){
        if(sql==null) return "( null sql...)";
        if(sql.length()>40){
            return sql.substring(0,40)+"...";
        }else{
            return sql;
        }
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
    public static DataSet executeCallableCursorQuery(Context ctx, String sql,
            ArrayList data) throws DBException {
    	return executeCallableCursorQuery(ctx, null, sql, data);
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
    public static DataSet executeCallableCursorQuery(Context ctx, String dbName, String sql,
            ArrayList data) throws DBException {
    	return executeCallableCursorQuery(ctx, dbName, sql, data.toArray(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
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
    public static DataSet executeCallableCursorQuery(Context ctx, String sql,
            Object data[]) throws DBException {
    	return executeCallableCursorQuery(ctx, null, sql, data);
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
    public static DataSet executeCallableCursorQuery(Context ctx, String dbName, String sql,
            Object data[]) throws DBException {
    	return executeCallableCursorQuery(ctx, dbName, sql, data, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
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
    public static DataSet executeCallableCursorQuery(Context ctx, String dbName, String sql,
        Object data[], int resultSetType, int resultSetConcurrency) throws DBException {
        ctx = ContextLogger.getContext();

        debugSQL(ctx, sql, data);
        startProfilerEvent(ctx,sql);

        long start=System.currentTimeMillis();
        Connection con = null;
        CallableStatement cstmt = null;
        ResultSet rs = null;
        if(ctx != null){
            ctx.startActivity(Context.ACTIVITY_DB_READ);
        }
        try {
            con = getConnection(ctx,dbName);

            cstmt = callableStatement(con, sql, resultSetType, resultSetConcurrency);

            ArrayList arrayList = setValues(cstmt, data);
            int outputPosition = ((Integer)arrayList.get(0)).intValue();

            cstmt.execute();
            rs = (ResultSet)cstmt.getObject(outputPosition);

            openedResultSetCount++;
            startProfilerEvent(ctx,"Data fetch");

            DataSet rset = toMemory(rs);

            stopProfilerEvent(ctx);
            if(isQueryMonitorMode()){
                long end=System.currentTimeMillis();
                SysMonitor.addMonitorData( new MonitorData("Database",sql,end-start));
            }
            debugSqlLog(ctx, sql, data, null);
            return rset;
        }
        catch (SQLException e) {
            logSQL(sql, data);
            debugSqlLog(ctx, sql, data, e);
            LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
            throw new DBException(dbName,e);
        }
        catch (Exception e) {
            logSQL(sql, data);
            debugSqlLog(ctx, sql, data, e);
            LogManager.error(e.getMessage(),e);
            throw new SysException(e.toString());
        }
        finally {
            close(rs);
            close(cstmt);
            close(con);
            stopProfilerEvent(ctx);
            if(ctx != null){
                ctx.stopActivity();
            }
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
    public static Map executeCallableObjectQuery(Context ctx, String sql,
            ArrayList data) throws DBException {
    	return executeCallableObjectQuery(ctx, null, sql, data);
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
    public static Map executeCallableObjectQuery(Context ctx, String dbName, String sql,
            ArrayList data) throws DBException {
    	return executeCallableObjectQuery(ctx, dbName, sql, data.toArray(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
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
    public static Map executeCallableObjectQuery(Context ctx, String sql,
            Object data[]) throws DBException {
    	return executeCallableObjectQuery(ctx, null, sql, data);
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
    public static Map executeCallableObjectQuery(Context ctx, String dbName, String sql,
            Object data[]) throws DBException {
    	return executeCallableObjectQuery(ctx, dbName, sql, data, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
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
    public static Map executeCallableObjectQuery(Context ctx, String dbName, String sql,
        Object data[], int resultSetType, int resultSetConcurrency) throws DBException {
        ctx = ContextLogger.getContext();

        debugSQL(ctx, sql, data);
        startProfilerEvent(ctx,sql);

        long start=System.currentTimeMillis();
        Connection con = null;
        CallableStatement cstmt = null;
        ResultSet rs = null;

        HashMap hm = new HashMap();
        if(ctx != null){
            ctx.startActivity(Context.ACTIVITY_DB_READ);
        }
        try {
            con = getConnection(ctx,dbName);

            cstmt = callableStatement(con, sql, resultSetType, resultSetConcurrency);

            ArrayList arrayList = setValues(cstmt, data);

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
                    startProfilerEvent(ctx,"Data fetch");

            		obj = toMemory(rs);

            		stopProfilerEvent(ctx);
            	}

            	hm.put(parameterName, obj);
            }

            if(isQueryMonitorMode()){
                long end=System.currentTimeMillis();
                SysMonitor.addMonitorData( new MonitorData("Database",sql,end-start));
            }
            debugSqlLog(ctx, sql, data, null);
            return hm;
        }
        catch (SQLException e) {
            logSQL(sql, data);
            debugSqlLog(ctx, sql, data, e);
            LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
            throw new DBException(dbName,e);
        }
        catch (Exception e) {
            logSQL(sql, data);
            debugSqlLog(ctx, sql, data, e);
            LogManager.error(e.getMessage(),e);
            throw new SysException(e.toString());
        }
        finally {
            close(rs);
            close(cstmt);
            close(con);
            stopProfilerEvent(ctx);
            if(ctx != null){
                ctx.stopActivity();
            }
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
    public static Map executeCallableObjectUpdate(Context ctx, String sql,
            ArrayList data) throws DBException {
    	return executeCallableObjectUpdate(ctx, null, sql, data.toArray());
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
    public static Map executeCallableObjectUpdate(Context ctx, String sql,
            Object data[]) throws DBException {
    	return executeCallableObjectUpdate(ctx, null, sql, data);
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
	public static Map executeCallableObjectUpdate(Context ctx, String dbName,
			String sql, Object data[]) throws DBException {
	   ctx = ContextLogger.getContext();

	   debugSQL(ctx, sql, data);
       startProfilerEvent(ctx,sql);
	   long start=System.currentTimeMillis();
	   Connection con = null;
	   CallableStatement cstmt = null;
	   ResultSet rs = null;
	   HashMap hm = new HashMap();
	   if(ctx != null){
	       ctx.startActivity(Context.ACTIVITY_DB_WRITE);
       }
	   try {
	       con = getConnection(ctx,dbName);

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
					startProfilerEvent(ctx,"Data fetch");

					obj = toMemory(rs);

					stopProfilerEvent(ctx);
				}

				hm.put(parameterName, obj);
           }

	       if(isQueryMonitorMode()){
	           long end=System.currentTimeMillis();
	           SysMonitor.addMonitorData( new MonitorData("Database",sql,end-start));
	       }
	       debugSqlLog(ctx, sql, data, null);
	       return hm;
	   }
	   catch (SQLException e) {
	       logSQL(sql, data);
	       debugSqlLog(ctx, sql, data, e);
	       LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
	       throw new DBException(dbName,e);
	   }
	   catch (Exception e) {
	       logSQL(sql, data);
	       debugSqlLog(ctx, sql, data, e);
	       e.printStackTrace();
	       LogManager.error(e.getMessage(),e);
	       throw new SysException(e.toString());
	   }
	   finally {
		   close(rs);
	       close(cstmt);
	       close(con);
           stopProfilerEvent(ctx);
           if(ctx != null){
               ctx.stopActivity();
           }
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
    public static Map executeCallableObjectTxUpdate(Context ctx, TxManager txMgr,
    		String sql, ArrayList data) throws DBException {
    	return executeCallableObjectTxUpdate(ctx, txMgr, sql, data.toArray());
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
	public static Map executeCallableObjectTxUpdate(Context ctx, TxManager txMgr,
			String sql, Object data[]) throws DBException {
	    ctx = ContextLogger.getContext();

	   debugSQL(ctx, sql, data);
       startProfilerEvent(ctx,sql);
	   long start=System.currentTimeMillis();
	   Connection con = null;
	   CallableStatement cstmt = null;
	   ResultSet rs = null;
	   HashMap hm = new HashMap();
	   if(ctx != null){
	       ctx.startActivity(Context.ACTIVITY_DB_WRITE);
       }
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
					startProfilerEvent(ctx,"Data fetch");

					obj = toMemory(rs);

					stopProfilerEvent(ctx);
				}

				hm.put(parameterName, obj);
           }

	       if(isQueryMonitorMode()){
	           long end=System.currentTimeMillis();
	           SysMonitor.addMonitorData( new MonitorData("Database",sql,end-start));
	       }
	       debugSqlLog(ctx, sql, data, null);
	       return hm;
	   }
	   catch (SQLException e) {
	       logSQL(sql, data);
	       debugSqlLog(ctx, sql, data, e);
	       LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
	       throw new DBException(e);
	   }
	   catch (Exception e) {
	       logSQL(sql, data);
	       debugSqlLog(ctx, sql, data, e);
	       e.printStackTrace();
	       LogManager.error(e.getMessage(),e);
	       throw new SysException(e.toString());
	   }
	   finally {
		   close(rs);
	       close(cstmt);
           stopProfilerEvent(ctx);
           if(ctx != null){
               ctx.stopActivity();
           }
	   }
	}





    /**
	 * 일반적인 DataSet 리스트 쿼리를 조회하는데 사용합니다.(IBATIS 이용)
     *
	 * @param DBContext
     * @return DataSet
     * @throws DBException
     */
	public static DataSet executeQuery(DBContext dbCtx) throws DBException {

	   Context ctx = dbCtx.getMap().getContext();
	   ctx = ContextLogger.getContext();
//	   debugSQL(ctx, sql, null);
	   startProfilerEvent(ctx , dbCtx.getSqlUniqueId());
	   long start=System.currentTimeMillis();
	   DataSet data = null;
	   if(ctx != null){
	       ctx.startActivity(Context.ACTIVITY_DB_READ);
	   }
		try {
	       openedResultSetCount++;

	       //dbCtx.getMap()이 null일 경우 null로 셋팅되어 처리됨.
	       ArrayList retList = (ArrayList)SqlMapClientConfig.getSqlMapInstance(dbCtx.getDbName()).queryForList(dbCtx.getSqlUniqueId(),dbCtx.getMap());

	       startProfilerEvent(ctx,"Data fetch");

           data = new MapDataSet(retList);

	       stopProfilerEvent(ctx);
	       if(isQueryMonitorMode()){
	           long end=System.currentTimeMillis();
	           SysMonitor.addMonitorData( new MonitorData("Database",dbCtx.getSqlUniqueId(),end-start));
	       }
//	       debugSqlLog(ctx, sql, null, null);
	   }
	   catch (SQLException e) {
//	       logSQL(sql, null);
//	       debugSqlLog(ctx, sql, null, e);
	       LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
	       throw new DBException(dbCtx.getDbName(),e);
	   }
	   finally {
	       stopProfilerEvent(ctx);
	       if(ctx != null){
	           ctx.stopActivity();
	       }
	   }
	   return data;
	}


    /**
	 * 일반적인 DataSet 리스트 쿼리를 조회하는데 사용합니다. skip할 행과 최대 결과수를 추가 인자로 받습니다.(IBATIS 이용)
     *
	 * @param DBContext
	 * @param skipResult : The number of results to ignore
	 * @param maxResult : The maximum number of results to return
     * @return DataSet
     * @throws DBException
     */
	public static DataSet executeQuery(DBContext dbCtx,int skipResult, int maxResult) throws DBException {

	   Context ctx = dbCtx.getMap().getContext();
	   ctx = ContextLogger.getContext();
//		   debugSQL(ctx, sql, null);
	   startProfilerEvent(ctx , dbCtx.getSqlUniqueId());
	   long start=System.currentTimeMillis();
	   DataSet data = null;
	   if(ctx != null){
	       ctx.startActivity(Context.ACTIVITY_DB_READ);
	   }
		try {
	       openedResultSetCount++;
	       //dbCtx.getMap()이 null일 경우 null로 셋팅되어 처리됨.
	       ArrayList retList = (ArrayList)SqlMapClientConfig.getSqlMapInstance(dbCtx.getDbName()).queryForList(dbCtx.getSqlUniqueId(),dbCtx.getMap(),skipResult,maxResult);

	       startProfilerEvent(ctx,"Data fetch");
           data = new MapDataSet(retList);

	       stopProfilerEvent(ctx);
	       if(isQueryMonitorMode()){
	           long end=System.currentTimeMillis();
	           SysMonitor.addMonitorData( new MonitorData("Database",dbCtx.getSqlUniqueId(),end-start));
	       }
//		       debugSqlLog(ctx, sql, null, null);
	   }
	   catch (SQLException e) {
//		       logSQL(sql, null);
//		       debugSqlLog(ctx, sql, null, e);
	       LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
	       throw new DBException(dbCtx.getDbName(),e);
	   }
	   finally {
	       stopProfilerEvent(ctx);
	       if(ctx != null){
	           ctx.stopActivity();
	       }

	   }
	   return data;
	}



    /**
	 * 조회 결과가 1개일때만 사용합니다. 다수의 조회 결과 레코드가 발생하면 에러를 발생합니다.(IBATIS 이용)
     *
	 * @param DBContext
     * @return Object : 한개의 결과 객체
     * @throws DBException
     */
	public static Object executeQueryToBean(DBContext dbCtx) throws DBException {

	   Context ctx = dbCtx.getMap().getContext();
	   ctx = ContextLogger.getContext();
//			   debugSQL(ctx, sql, null);
	   startProfilerEvent(ctx , dbCtx.getSqlUniqueId());
	   long start=System.currentTimeMillis();
	   Object data = null;
	   if(ctx != null){
	       ctx.startActivity(Context.ACTIVITY_DB_READ);
	   }
		try {
	       openedResultSetCount++;

	       //dbCtx.getMap()이 null일 경우 null로 셋팅되어 처리됨.
	       data = SqlMapClientConfig.getSqlMapInstance(dbCtx.getDbName()).queryForObject(dbCtx.getSqlUniqueId(),dbCtx.getMap());

	       if(isQueryMonitorMode()){
	           long end=System.currentTimeMillis();
	           SysMonitor.addMonitorData( new MonitorData("Database",dbCtx.getSqlUniqueId(),end-start));
	       }
//			       debugSqlLog(ctx, sql, null, null);
	   }
	   catch (SQLException e) {
//			       logSQL(sql, null);
//			       debugSqlLog(ctx, sql, null, e);
	       LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
	       throw new DBException(dbCtx.getDbName(),e);
	   }
	   finally {
		   stopProfilerEvent(ctx);
	       if(ctx != null){
	           ctx.stopActivity();
	       }

	   }
	   return data;
	}


    /**
	 * 조회 결과가 1개일때만 사용합니다. 다수의 조회 결과 레코드가 발생하면 에러를 발생합니다.(IBATIS 이용)
     *
	 * @param DBContext
	 * @param resultObject : return Object(xml에 선언한 resultClass와 동일해야 정상 작동)
     * @return Object
     * @throws DBException
     */
	public static Object executeQueryToBean(DBContext dbCtx , Object resultObject) throws DBException {

	   Context ctx = dbCtx.getMap().getContext();
	   ctx = ContextLogger.getContext();
//				   debugSQL(ctx, sql, null);
	   startProfilerEvent(ctx , dbCtx.getSqlUniqueId());
	   long start=System.currentTimeMillis();
	   Object data = null;
	   if(ctx != null){
	       ctx.startActivity(Context.ACTIVITY_DB_READ);
	   }
		try {
	       openedResultSetCount++;

	       data = SqlMapClientConfig.getSqlMapInstance(dbCtx.getDbName()).queryForObject(dbCtx.getSqlUniqueId(),dbCtx.getMap(),resultObject);

	       if(isQueryMonitorMode()){
	           long end=System.currentTimeMillis();
	           SysMonitor.addMonitorData( new MonitorData("Database",dbCtx.getSqlUniqueId(),end-start));
	       }
//				       debugSqlLog(ctx, sql, null, null);
	   }
	   catch (SQLException e) {
//				       logSQL(sql, null);
//				       debugSqlLog(ctx, sql, null, e);
	       LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
	       throw new DBException(dbCtx.getDbName(),e);
	   }
	   finally {
		   stopProfilerEvent(ctx);
	       if(ctx != null){
	           ctx.stopActivity();
	       }

	   }
	   return data;
	}



    /**
	 * 쿼리를 수행하고 결과 데이타를 Map형태로 반환합니다.(IBATIS 이용)
	 *
	 * ex : public class A {
	 *          String id;
	 *          String name;
	 *      };
	 * 이 클래스가 해당 xml의 SQL문에 returnMap으로 property 및 column이 지정되어 있고, 해당 select문의 resultMap에 지정되어야 한다.
	 *
	 * 리턴되는 데이타 : id=1,name=tester | id=2,name=tester2 일때
	 *
	 *      Map map = DBHandler.executeQueryForMap(DBContext,"id");
	 *      System.out.println(map) => {1=A@3eca90,2=@2aaa80}
     *
     * 주의 : key로 지정된 값이 같은 경우 마지막 결과만 리턴됨
	 * @param DBContext
	 * @param keyProp : return되는 Map에서 key값으로 사용할 객체의 프로퍼티(주로 PK가 사용됨)
     * @return Map 이 Map에는 keyProp가 key이고 value는 리턴되는 한 Row의 Map이다.
     * @throws DBException
     */
	public static Map executeQueryToMap(DBContext dbCtx , String keyProp) throws DBException {

	    Context ctx = dbCtx.getMap().getContext();
	    ctx = ContextLogger.getContext();
//				   debugSQL(ctx, sql, null);
	    startProfilerEvent(ctx , dbCtx.getSqlUniqueId());
	    long start=System.currentTimeMillis();
	    Map data = null;
	    if(ctx != null){
	       ctx.startActivity(Context.ACTIVITY_DB_READ);
	    }
		try {
	       openedResultSetCount++;
	       //Ibatis 자체적으로 map에 map을 리스트로 만들어서 리턴
//	       startProfilerEvent(ctx,"Data fetch");

	       data = SqlMapClientConfig.getSqlMapInstance(dbCtx.getDbName()).queryForMap(dbCtx.getSqlUniqueId() , dbCtx.getMap(), keyProp);

//	       stopProfilerEvent(ctx);
	       if(isQueryMonitorMode()){
	           long end=System.currentTimeMillis();
	           SysMonitor.addMonitorData( new MonitorData("Database",dbCtx.getSqlUniqueId(),end-start));
	       }
//				       debugSqlLog(ctx, sql, null, null);
	   }
	   catch (SQLException e) {
//				       logSQL(sql, null);
//				       debugSqlLog(ctx, sql, null, e);
	       LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
	       throw new DBException(dbCtx.getDbName(),e);
	   }
	   finally {
	       stopProfilerEvent(ctx);
	       if(ctx != null){
	           ctx.stopActivity();
	       }
	   }
	   return data;
	}


    /**
	 * 쿼리를 수행하고 결과데이타를 Map형태로 반환합니다.(IBATIS 이용)
	 *
	 * ex : public class A {
	 *          String id;
	 *          String name;
	 *      };
	 * 이 클래스가 해당 xml의 SQL문에 returnMap으로 property 및 column이 지정되어 있고, 해당 select문의 resultMap에 지정되어야 한다.
	 *
	 * 리턴되는 데이타 : id=1,name=tester | id=2,name=tester2 일때
	 *
	 *      Map map = DBHandler.executeQueryForMap(DBContext,"id","name");
	 *      System.out.println(map) => {1=tester,2=tester2}
     *
     * 주의 : key로 지정된 값이 같은 경우 마지막 결과만 리턴됨
	 * @param DBContext
	 * @param keyProp : return되는 Map에서 key값으로 사용할 객체의 프로퍼티(주로 PK가 이용됨)
     * @return Map 이 Map에는 keyProp가 key이고 value는 valueProp의 리턴값이다.
     * @throws DBException
     */
	public static Map executeQueryToMap(DBContext dbCtx , String keyProp ,String valueProp) throws DBException {

	    Context ctx = dbCtx.getMap().getContext();
	    ctx = ContextLogger.getContext();
//				   debugSQL(ctx, sql, null);
	    startProfilerEvent(ctx , dbCtx.getSqlUniqueId());
	    long start=System.currentTimeMillis();
	    Map data = null;
	    if(ctx != null){
	       ctx.startActivity(Context.ACTIVITY_DB_READ);
	    }
		try {
	       openedResultSetCount++;
	       //Ibatis 자체적으로 map에 map을 리스트로 만들어서 리턴
//	       startProfilerEvent(ctx,"Data fetch");

	       data = SqlMapClientConfig.getSqlMapInstance(dbCtx.getDbName()).queryForMap(dbCtx.getSqlUniqueId(),dbCtx.getMap(), keyProp, valueProp);

//	       stopProfilerEvent(ctx);
	       if(isQueryMonitorMode()){
	           long end=System.currentTimeMillis();
	           SysMonitor.addMonitorData( new MonitorData("Database",dbCtx.getSqlUniqueId(),end-start));
	       }
//				       debugSqlLog(ctx, sql, null, null);
	   }
	   catch (SQLException e) {
//				       logSQL(sql, null);
//				       debugSqlLog(ctx, sql, null, e);
	       LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
	       throw new DBException(dbCtx.getDbName(),e);
	   }
	   finally {
	       stopProfilerEvent(ctx);
	       if(ctx != null){
	           ctx.stopActivity();
	       }
	   }
	   return data;
	}




    /**
	 * Insert 쿼리를 수행하고 커넥션은 자동을 닫힙니다.(IBATIS 이용)
     *
	 * @param DBContext
     * @return Object : The primary key of the newly inserted row. This might be automatically generated by the RDBMS,
     * or selected from a sequence table or other source
     * @throws DBException
     */
	public static Object insert(DBContext dbCtx) throws DBException {

	    Context ctx = dbCtx.getMap().getContext();
	    ctx = ContextLogger.getContext();
//				   debugSQL(ctx, sql, null);
		startProfilerEvent(ctx , dbCtx.getSqlUniqueId());
	    long start=System.currentTimeMillis();
	    Object data = null;
	    if(ctx != null){
	       ctx.startActivity(Context.ACTIVITY_DB_WRITE);
	    }
		try {
	       openedResultSetCount++;

	       data = SqlMapClientConfig.getSqlMapInstance(dbCtx.getDbName()).insert(dbCtx.getSqlUniqueId(),dbCtx.getMap());

	       if(isQueryMonitorMode()){
	           long end=System.currentTimeMillis();
	           SysMonitor.addMonitorData( new MonitorData("Database",dbCtx.getSqlUniqueId(),end-start));
	       }
//				       debugSqlLog(ctx, sql, null, null);
	   }
	   catch (SQLException e) {
//				       logSQL(sql, null);
//				       debugSqlLog(ctx, sql, null, e);
	       LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
	       throw new DBException(dbCtx.getDbName(),e);
	   }
	   finally {
	       stopProfilerEvent(ctx);
	       if(ctx != null){
	           ctx.stopActivity();
	       }
	   }
	   return data;
	}


    /**
	 * Delete 쿼리를 수행하고 커넥션은 자동을 닫힙니다.(IBATIS 이용)
     *
	 * @param DBContext
     * @return int : The number of rows effected.
     * @throws DBException
     */
	public static int delete(DBContext dbCtx) throws DBException {

	   return update(dbCtx);
	}



    /**
	 * Update 쿼리를 수행하고 커넥션은 자동을 닫힙니다.(IBATIS 이용)
     *
	 * @param DBContext
     * @return int : The number of rows effected.
     * @throws DBException
     */
	public static int update(DBContext dbCtx) throws DBException {

	    Context ctx = dbCtx.getMap().getContext();
	    ctx = ContextLogger.getContext();
//				   debugSQL(ctx, sql, null);
		startProfilerEvent(ctx , dbCtx.getSqlUniqueId());
	    long start=System.currentTimeMillis();
	    int data;
	    if(ctx != null){
	       ctx.startActivity(Context.ACTIVITY_DB_WRITE);
	    }
		try {
	       openedResultSetCount++;

	       data = SqlMapClientConfig.getSqlMapInstance(dbCtx.getDbName()).update(dbCtx.getSqlUniqueId(),dbCtx.getMap());

	       if(isQueryMonitorMode()){
	           long end=System.currentTimeMillis();
	           SysMonitor.addMonitorData( new MonitorData("Database",dbCtx.getSqlUniqueId(),end-start));
	       }
//				       debugSqlLog(ctx, sql, null, null);
	   }
	   catch (SQLException e) {
//				       logSQL(sql, null);
//				       debugSqlLog(ctx, sql, null, e);
	       LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
	       throw new DBException(dbCtx.getDbName(),e);
	   }
	   finally {
	       stopProfilerEvent(ctx);
	       if(ctx != null){
	           ctx.stopActivity();
	       }
	   }
	   return data;
	}


	/**
	 * 해당 db에 한번에 100건씩 batch 처리를 한다.
	 *
	 * @param dbCtx
	 * @param dataSet
	 */
    public static void executeBatch(DBContext dbCtx , DataSet dataSet) {
    	executeBatch( dbCtx , dataSet,100);//기본 100건씩 수행
    }


	/**
	 * 해당 db에 한번에 100건씩 batch 처리를 한다.
	 *
	 * @param dbCtx
	 * @param dataSet
	 * @param executeUnitCount 실행할 배치 카운트
	 */
    public static void executeBatch(DBContext dbCtx , DataSet dataSet, int executeUnitCount) {
    	LogManager.debug("##### "+dataSet.getClass().getName()+" #####");

    	if (dataSet instanceof MapDataSet) {
        	executeBatch( dbCtx , dataSet.getData(),executeUnitCount);
    	} else if(dataSet instanceof DataSet) {
        	ArrayList list = new ArrayList();

    		while(dataSet.next()){
    			DataMap map = dataSet.toDataMap();
    			list.add(map);
    		}
        	executeBatch( dbCtx , list ,executeUnitCount);
    	}

    }


	/**
	 * 해당 db에 한번에 100건씩 batch 처리를 한다.
	 *
	 * @param dbCtx
	 * @param paramList 리스트의 세부 요소는 map 객체이다.
	 */
    public static void executeBatch(DBContext dbCtx , ArrayList paramList) {
    	executeBatch( dbCtx , paramList,100);//기본 100건씩 수행
    }


	/**
	 * 해당 db에 한번에 executeUnitCount건씩 batch 처리를 한다.
	 *
	 * @param dbCtx
	 * @param paramList 리스트의 세부 요소는 map 객체이다.
	 * @param executeUnitCount 실행할 배치 카운트
	 */
    public static void executeBatch(DBContext dbCtx , ArrayList paramList,int executeUnitCount) {

        Context ctx = ContextLogger.getContext();

//        debugSQL(ctx, sql,null);

        long start=System.currentTimeMillis();

        IbatisTxManager txMgr = null;
        int paramSize = paramList.size();
        Object data = null;
        if(paramSize==0) {
            LogManager.info("수행할 파라미터에 값이 업습니다.");
            return ;
        }

        ctx = ContextLogger.getContext();
        startProfilerEvent(ctx,"Batch:"+dbCtx.getSqlUniqueId());
        if(ctx != null){
            ctx.startActivity(Context.ACTIVITY_DB_WRITE);
        }
        try {
            txMgr = new IbatisTxManager(dbCtx.getDbName());
            txMgr.begin();

            txMgr.getSqlMapClient().startBatch();

            for(int i=0;i<paramSize;i++){

                data = paramList.get(i);
            	txMgr.getSqlMapClient().update(dbCtx.getSqlUniqueId(),data);

                if(i%executeUnitCount==(executeUnitCount-1)){
                	txMgr.getSqlMapClient().executeBatch();
                    txMgr.getSqlMapClient().startBatch();
                }
            }

            txMgr.getSqlMapClient().executeBatch();

            txMgr.commit();

            if(isQueryMonitorMode()){
	            long end=System.currentTimeMillis();
	            SysMonitor.addMonitorData( new MonitorData("Database",dbCtx.getSqlUniqueId(),end-start));
            }
            LogManager.debug(" ##### BATCH SUCCESS ##### ");

        } catch(SQLException e) {
            txMgr.rollback();
            LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
            LogManager.error("DB_ERROR","NEXT_ERROR_CODE:"+e.getNextException().getErrorCode()+",REASON:"+e.getNextException().getMessage());
            throw new DBException(dbCtx.getDbName(),e);
        } catch(Exception e) {
            txMgr.rollback();
            LogManager.error(e.getMessage(),e);
            throw new SysException(e.toString());
        } finally {
            txMgr.end();
            stopProfilerEvent(ctx);
            if(ctx != null){
                ctx.stopActivity();
            }
        }
    }

    /**
     * executeBatch 처리시 paramList의 값 print
     * @param paramList
     */
    public static void debugParam(ArrayList paramList){
    	LogManager.debug("### 파라미터 정보를 출력 시작 ###");
    	LogManager.debug("### 파라미터 LIST count : " + paramList.size());
    	int cnt;
    	Object data=null;
    	StringBuffer buf = new StringBuffer(2000);
        String key = null;
        Object item = null;

    	for(cnt=0; cnt<paramList.size(); cnt++){
    		data = paramList.get(cnt);
	    	LogManager.debug("### LIST[" +cnt+ "] Class : " + data.getClass().getName());
	        if(data instanceof DataMap){
	            Set keySet = ((DataMap)data).keySet();
	            Iterator i = keySet.iterator();
	            while(i.hasNext())
	            {
	                key = (String)i.next();
	                item = ((DataMap)data).get(key);
	                buf.append(key + "=" + getStrParam(item));
	            }
	        	buf.append("end of DataMap info ============================\n");
	    	}else if(data instanceof ArrayList){
	    		int i;
	    		for(i=0; i<((ArrayList)data).size(); i++){
	    			item = ((ArrayList)data).get(i);
	    			buf.append("["+ i +"]=" + getStrParam(item));
	    		}
	    		buf.append("end of ArrayList info ============================\n");
	    	}else{
	    		buf.append(key + "=[" + item + "] ClassName:" + item.getClass().getName() + "\n");
	    	}
	        System.out.println(buf.toString());
	        buf.delete(0, buf.length());
    	}

    	LogManager.debug("### 파라미터 정보를 출력 종료 ###");
    }

    /**
     * Object의 값과 type 리턴
     * @param item
     * @return
     */
    private static String getStrParam(Object item){
    	if(item == null)
            return "null\n";
        else if(item instanceof String) {
            return "[" + item + "][" + item.getClass().getName() +", " + item.toString().length()+ "]\n";
        }
        else if(item instanceof Integer || item instanceof Long
               || item instanceof Double || item instanceof java.lang.Float
               || item instanceof Boolean)
            return "[" + item  + "][" + item.getClass().getName() + "]\n";
        else if(item instanceof String[])
        {
            String str[] = (String[])item;
            StringBuffer buf = new StringBuffer();
            buf.append("[");
            int j;
            for(j = 0; j < str.length; j++)
                buf.append(str[j] + ',');

            buf.append("] Array Size:" + j + " \n");
            return buf.toString();
        } else
        {
            return "[" + item + "] ClassName:" + item.getClass().getName() + "\n";
        }
    }


	/**
	 * 해당 db에 한번에 100건씩 batch 처리를 한다.
	 *
	 * @param dbCtx
	 * @param dataSet
	 */
    public static void executeBatch(IbatisTxManager tx,DBContext dbCtx , DataSet dataSet) {
    	executeBatch( tx, dbCtx , dataSet,100);//기본 100건씩 수행
    }


	/**
	 * 해당 db에 한번에 100건씩 batch 처리를 한다.
	 *
	 * @param dbCtx
	 * @param dataSet
	 * @param executeUnitCount 실행할 배치 카운트
	 */
    public static void executeBatch(IbatisTxManager tx,DBContext dbCtx
    		, DataSet dataSet, int executeUnitCount) {

    	//tx DBNAME과 DBCTX의 DBNAME일관성 체크 함수 추가
		checkDbNameConfig(tx, dbCtx);

    	LogManager.debug("##### executeBatch에 전달된 DataSetClass: "
    			+dataSet.getClass().getName()+" #####");

    	if (dataSet instanceof MapDataSet) {
        	executeBatch( tx, dbCtx , dataSet.getData(),executeUnitCount);
    	} else if(dataSet instanceof DataSet) {
        	ArrayList list = new ArrayList();

    		while(dataSet.next()){
    			DataMap map = dataSet.toDataMap();
    			list.add(map);
    		}
        	executeBatch( tx, dbCtx , list ,executeUnitCount);
    	}

    }


	/**
	 * 해당 db에 한번에 100건씩 batch 처리를 한다.
	 *
	 * @param dbCtx
	 * @param paramList 리스트의 세부 요소는 map 객체이다.
	 */
    public static void executeBatch(IbatisTxManager tx,DBContext dbCtx , ArrayList paramList) {
    	executeBatch(tx, dbCtx , paramList,100);//기본 100건씩 수행
    }


	/**
	 * 해당 db에 한번에 executeUnitCount건씩 batch 처리를 한다.
	 *
	 * @param dbCtx
	 * @param paramList 리스트의 세부 요소는 map 객체이다.
	 * @param executeUnitCount 실행할 배치 카운트
	 */
    public static void executeBatch(IbatisTxManager tx,DBContext dbCtx , ArrayList paramList,int executeUnitCount) {
		//tx DBNAME과 DBCTX의 DBNAME일관성 체크 함수 추가
		checkDbNameConfig(tx, dbCtx);

        Context ctx = ContextLogger.getContext();

//        debugSQL(ctx, sql,null);

        long start=System.currentTimeMillis();

        int paramSize = paramList.size();
        Object data = null;
        if(paramSize==0) {
            LogManager.info("수행할 파라미터에 값이 업습니다.");
            return ;
        }

        if(PropertyManager.getBooleanProperty("default","LOG.CONSOLE_MODE","ON")){
	        LogManager.debug("### executeBatch 파라미터값 체크 ###");
	        debugParam(paramList);
        }

        ctx = ContextLogger.getContext();
        startProfilerEvent(ctx,"Batch:"+dbCtx.getSqlUniqueId());
        if(ctx != null){
            ctx.startActivity(Context.ACTIVITY_DB_WRITE);
        }
        try {

            tx.getSqlMapClient().startBatch();

            for(int i=0;i<paramSize;i++){

                data = paramList.get(i);
            	tx.getSqlMapClient().update(dbCtx.getSqlUniqueId(),data);

                if(i%executeUnitCount==(executeUnitCount-1)){
                    LogManager.debug("### BATCH EXECUTE ###");
                	tx.getSqlMapClient().executeBatch();
                    tx.getSqlMapClient().startBatch();
                }
            }

            LogManager.debug("### BATCH EXECUTE ####");
            tx.getSqlMapClient().executeBatch();

            if(isQueryMonitorMode()){
	            long end=System.currentTimeMillis();
	            SysMonitor.addMonitorData( new MonitorData("Database",dbCtx.getSqlUniqueId(),end-start));
            }
            LogManager.debug(" ##### BATCH SUCCESS ##### ");

        } catch(SQLException e) {
//            txMgr.rollback(); 개발자가 책임지고 rollback
            LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
            LogManager.error("DB_ERROR","NEXT_ERROR_CODE:"+e.getNextException().getErrorCode()+",REASON:"+e.getNextException().getMessage());
            throw new DBException(dbCtx.getDbName(),e);
        } catch(Exception e) {
//            txMgr.rollback();개발자가 책임지고 rollback
            LogManager.error(e.toString()+":호출하신 쪽에서  rollback 처리를 하셔야 합니다." ,e);
            throw new SysException(e.toString());
        } finally {
//            txMgr.end();개발자가 책임지고 end
            stopProfilerEvent(ctx);
            if(ctx != null){
                ctx.stopActivity();
            }
        }
    }


    /**
	 * 트랜잭션으로 묶을 필요가 있을 경우에 사용합니다.
	 * 쿼리를 수행하고 커넥션은 IBSTxManager.commit() IBSTxManager.end()에서 닫힘니다.(IBATIS 이용)
     *
	 * @param DBContext
     * @return DataSet
     * @throws DBException
     */
	public static DataSet executeQuery(IbatisTxManager tx,DBContext dbCtx) throws DBException {
		//tx DBNAME과 DBCTX의 DBNAME일관성 체크 함수 추가
		checkDbNameConfig(tx, dbCtx);

		Context ctx = dbCtx.getMap().getContext();
		ctx = ContextLogger.getContext();
		//	   debugSQL(ctx, sql, null);
		startProfilerEvent(ctx , dbCtx.getSqlUniqueId());
		long start=System.currentTimeMillis();
		DataSet data = null;
		if(ctx != null){
		    ctx.startActivity(Context.ACTIVITY_DB_READ);
		}
		try {
	       openedResultSetCount++;

	       //dbCtx.getMap()이 null일 경우 null로 셋팅되어 처리됨.
	       ArrayList retList = (ArrayList) tx.getSqlMapClient().queryForList(dbCtx.getSqlUniqueId(),dbCtx.getMap());

	       startProfilerEvent(ctx,"Data fetch");

           data = new MapDataSet(retList);

	       stopProfilerEvent(ctx);
	       if(isQueryMonitorMode()){
	           long end=System.currentTimeMillis();
	           SysMonitor.addMonitorData( new MonitorData("Database",dbCtx.getSqlUniqueId(),end-start));
	       }
//	       debugSqlLog(ctx, sql, null, null);
	   }
	   catch (SQLException e) {
//	       logSQL(sql, null);
//	       debugSqlLog(ctx, sql, null, e);
	       LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
	       throw new DBException(dbCtx.getDbName(),e);
	   }
	   finally {
	       stopProfilerEvent(ctx);
	       if(ctx != null){
	           ctx.stopActivity();
	       }
	   }
	   return data;
	}


    /**
	 * 트랜잭션으로 묶을 필요가 있을 경우에 사용합니다.
	 * 쿼리를 수행하고 커넥션은IBSTxManager.commit() IBSTxManager.end()에서 닫힘니다.(IBATIS 이용)
     *
	 * @param DBContext
	 * @param skipResult : The number of results to ignore
	 * @param maxResult : The maximum number of results to return
     * @return DataSet
     * @throws DBException
     */
	public static DataSet executeQuery(IbatisTxManager tx,DBContext dbCtx,int skipResult, int maxResult) throws DBException {
		//tx DBNAME과 DBCTX의 DBNAME일관성 체크 함수 추가
		checkDbNameConfig(tx, dbCtx);

		Context ctx = dbCtx.getMap().getContext();
		ctx = ContextLogger.getContext();
//		   debugSQL(ctx, sql, null);
		startProfilerEvent(ctx , dbCtx.getSqlUniqueId());
		long start=System.currentTimeMillis();
		DataSet data = null;
		if(ctx != null){
			ctx.startActivity(Context.ACTIVITY_DB_READ);
		}
		try {
	       openedResultSetCount++;
	       //dbCtx.getMap()이 null일 경우 null로 셋팅되어 처리됨.
	       ArrayList retList = (ArrayList)tx.getSqlMapClient().queryForList(dbCtx.getSqlUniqueId(),dbCtx.getMap(),skipResult,maxResult);

	       startProfilerEvent(ctx,"Data fetch");
           data = new MapDataSet(retList);

	       stopProfilerEvent(ctx);
	       if(isQueryMonitorMode()){
	           long end=System.currentTimeMillis();
	           SysMonitor.addMonitorData( new MonitorData("Database",dbCtx.getSqlUniqueId(),end-start));
	       }
//		       debugSqlLog(ctx, sql, null, null);
	   }
	   catch (SQLException e) {
//		       logSQL(sql, null);
//		       debugSqlLog(ctx, sql, null, e);
	       LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
	       throw new DBException(dbCtx.getDbName(),e);
	   }
	   finally {
	       stopProfilerEvent(ctx);
	       if(ctx != null){
	           ctx.stopActivity();
	       }

	   }
	   return data;
	}



    /**
	 * 트랜잭션으로 묶을 필요가 있을 경우에 사용합니다.
	 * 쿼리를 수행하고 커넥션은 IBSTxManager.commit() IBSTxManager.end()에서 닫힘니다.(IBATIS 이용)
	 * 조회 결과가 1개일때만 사용합니다. 다수의 조회 결과 레코드가 발생하면 에러를 발생합니다.
     *
	 * @param DBContext
     * @return Object : 한개의 결과 객체
     * @throws DBException
     */
	public static Object executeQueryToBean(IbatisTxManager tx,DBContext dbCtx) throws DBException {
		//tx DBNAME과 DBCTX의 DBNAME일관성 체크 함수 추가
		checkDbNameConfig(tx, dbCtx);

		Context ctx = dbCtx.getMap().getContext();
	   	ctx = ContextLogger.getContext();
//			   debugSQL(ctx, sql, null);
	   	startProfilerEvent(ctx , dbCtx.getSqlUniqueId());
	   	long start=System.currentTimeMillis();
	   	Object data = null;
	   	if(ctx != null){
	       	ctx.startActivity(Context.ACTIVITY_DB_READ);
	   	}
		try {
	       openedResultSetCount++;

	       //dbCtx.getMap()이 null일 경우 null로 셋팅되어 처리됨.
	       data = tx.getSqlMapClient().queryForObject(dbCtx.getSqlUniqueId(),dbCtx.getMap());

	       if(isQueryMonitorMode()){
	           long end=System.currentTimeMillis();
	           SysMonitor.addMonitorData( new MonitorData("Database",dbCtx.getSqlUniqueId(),end-start));
	       }
//			       debugSqlLog(ctx, sql, null, null);
	   }
	   catch (SQLException e) {
//			       logSQL(sql, null);
//			       debugSqlLog(ctx, sql, null, e);
	       LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
	       throw new DBException(dbCtx.getDbName(),e);
	   }
	   finally {
		   stopProfilerEvent(ctx);
	       if(ctx != null){
	           ctx.stopActivity();
	       }

	   }
	   return data;
	}


    /**
	 * 트랜잭션으로 묶을 필요가 있을 경우에 사용합니다.
	 * 쿼리를 수행하고 커넥션은 IBSTxManager.commit() IBSTxManager.end()에서 닫힘니다.(IBATIS 이용)
	 * 조회 결과가 1개일때만 사용합니다. 다수의 조회 결과 레코드가 발생하면 에러를 발생합니다.
     *
	 * @param DBContext
	 * @param resultObject : return Object(xml에 선언한 resultClass와 동일해야 정상 작동..)
     * @return Object
     * @throws DBException
     */
	public static Object executeQueryToBean(IbatisTxManager tx,DBContext dbCtx , Object resultObject) throws DBException {
		//tx DBNAME과 DBCTX의 DBNAME일관성 체크 함수 추가
		checkDbNameConfig(tx, dbCtx);

		Context ctx = dbCtx.getMap().getContext();
		ctx = ContextLogger.getContext();
//				   debugSQL(ctx, sql, null);
		startProfilerEvent(ctx , dbCtx.getSqlUniqueId());
		long start=System.currentTimeMillis();
		Object data = null;
		if(ctx != null){
			ctx.startActivity(Context.ACTIVITY_DB_READ);
		}
		try {
	       openedResultSetCount++;


	       data = tx.getSqlMapClient().queryForObject(dbCtx.getSqlUniqueId(),dbCtx.getMap(),resultObject);

	       if(isQueryMonitorMode()){
	           long end=System.currentTimeMillis();
	           SysMonitor.addMonitorData( new MonitorData("Database",dbCtx.getSqlUniqueId(),end-start));
	       }
//				       debugSqlLog(ctx, sql, null, null);
	   }
	   catch (SQLException e) {
//				       logSQL(sql, null);
//				       debugSqlLog(ctx, sql, null, e);
	       LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
	       throw new DBException(dbCtx.getDbName(),e);
	   }
	   finally {
		   stopProfilerEvent(ctx);
	       if(ctx != null){
	           ctx.stopActivity();
	       }

	   }
	   return data;
	}



    /**
	 * 트랜잭션으로 묶을 필요가 있을 경우에 사용합니다.
	 * 쿼리를 수행하고 커넥션은 IBSTxManager.commit() IBSTxManager.end()에서 닫힘니다.(IBATIS 이용)
	 *
	 * ex : public class A {
	 *          String id;
	 *          String name;
	 *      };
	 * 이 클래스가 해당 xml의 SQL문에 returnMap으로 property 및 column이 지정되어 있고, 해당 select문의 resultMap에 지정되어야 한다.
	 *
	 * 리턴되는 데이타 : id=1,name=tester | id=2,name=tester2 일때
	 *
	 *      Map map = DBHandler.executeQueryForMap(DBContext,"id");
	 *      System.out.println(map) => {1=A@3eca90,2=@2aaa80}
     *
	 * @param DBContext
	 * @param keyProp : return되는 Map에서 key값으로 사용할 객체의 프로퍼티
     * @return Map 이 Map에는 keyProp가 key이고 value는 리턴되는 한 Row의 Map이다.
     * @throws DBException
     */
	public static Map executeQueryToMap(IbatisTxManager tx,DBContext dbCtx , String keyProp) throws DBException {
		//tx DBNAME과 DBCTX의 DBNAME일관성 체크 함수 추가
		checkDbNameConfig(tx, dbCtx);

	    Context ctx = dbCtx.getMap().getContext();
	    ctx = ContextLogger.getContext();
//				   debugSQL(ctx, sql, null);
	    startProfilerEvent(ctx , dbCtx.getSqlUniqueId());
	    long start=System.currentTimeMillis();
	    Map data = null;
	    if(ctx != null){
	       ctx.startActivity(Context.ACTIVITY_DB_READ);
	    }
		try {
	       openedResultSetCount++;

	       data = tx.getSqlMapClient().queryForMap(dbCtx.getSqlUniqueId() , dbCtx.getMap(), keyProp);

	       if(isQueryMonitorMode()){
	           long end=System.currentTimeMillis();
	           SysMonitor.addMonitorData( new MonitorData("Database",dbCtx.getSqlUniqueId(),end-start));
	       }
//				       debugSqlLog(ctx, sql, null, null);
	   }
	   catch (SQLException e) {
//				       logSQL(sql, null);
//				       debugSqlLog(ctx, sql, null, e);
	       LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
	       throw new DBException(dbCtx.getDbName(),e);
	   }
	   finally {
	       stopProfilerEvent(ctx);
	       if(ctx != null){
	           ctx.stopActivity();
	       }
	   }
	   return data;
	}


    /**
	 * 트랜잭션으로 묶을 필요가 있을 경우에 사용합니다.
	 * 쿼리를 수행하고 커넥션은 IBSTxManager.commit() IBSTxManager.end()에서 닫힘니다.(IBATIS 이용)
	 *
	 * ex : public class A {
	 *          String id;
	 *          String name;
	 *      };
	 * 이 클래스가 해당 xml의 SQL문에 returnMap으로 property 및 column이 지정되어 있고, 해당 select문의 resultMap에 지정되어야 한다.
	 *
	 * 리턴되는 데이타 : id=1,name=tester | id=2,name=tester2 일때
	 *
	 *      Map map = DBHandler.executeQueryForMap(DBContext,"id","name");
	 *      System.out.println(map) => {1=tester,2=tester2}
     *
	 * @param DBContext
	 * @param keyProp : return되는 Map에서 key값으로 사용할 객체의 프로퍼티
     * @return Map 이 Map에는 keyProp가 key이고 value는 valueProp의 리턴값이다.
     * @throws DBException
     */
	public static Map executeQueryToMap(IbatisTxManager tx,DBContext dbCtx , String keyProp ,String valueProp) throws DBException {
		//tx DBNAME과 DBCTX의 DBNAME일관성 체크 함수 추가
		checkDbNameConfig(tx, dbCtx);

	    Context ctx = dbCtx.getMap().getContext();
	    ctx = ContextLogger.getContext();
//				   debugSQL(ctx, sql, null);
	    startProfilerEvent(ctx , dbCtx.getSqlUniqueId());
	    long start=System.currentTimeMillis();
	    Map data = null;
	    if(ctx != null){
	       ctx.startActivity(Context.ACTIVITY_DB_READ);
	    }
		try {
	       openedResultSetCount++;

	       data = tx.getSqlMapClient().queryForMap(dbCtx.getSqlUniqueId(),dbCtx.getMap(), keyProp, valueProp);

	       if(isQueryMonitorMode()){
	           long end=System.currentTimeMillis();
	           SysMonitor.addMonitorData( new MonitorData("Database",dbCtx.getSqlUniqueId(),end-start));
	       }
//				       debugSqlLog(ctx, sql, null, null);
	   }
	   catch (SQLException e) {
//				       logSQL(sql, null);
//				       debugSqlLog(ctx, sql, null, e);
	       LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
	       throw new DBException(dbCtx.getDbName(),e);
	   }
	   finally {
	       stopProfilerEvent(ctx);
	       if(ctx != null){
	           ctx.stopActivity();
	       }
	   }
	   return data;
	}




    /**
	 * 트랜잭션으로 묶일 필요가 있을 경우에 사용합니다.
	 * Insert 쿼리를 수행하고 커넥션은 IbatisTxManager.commit() IbatisTxManager.end()에서 닫힘니다.(IBATIS 이용)
     *
	 * @param DBContext
     * @return Object : The primary key of the newly inserted row. This might be automatically generated by the RDBMS,
     * or selected from a sequence table or other source
     * @throws DBException
     */
	public static Object insert(IbatisTxManager tx,DBContext dbCtx) throws DBException {
		//tx DBNAME과 DBCTX의 DBNAME일관성 체크 함수 추가
		checkDbNameConfig(tx, dbCtx);

	    Context ctx = dbCtx.getMap().getContext();
	    ctx = ContextLogger.getContext();
//				   debugSQL(ctx, sql, null);
		startProfilerEvent(ctx , dbCtx.getSqlUniqueId());
	    long start=System.currentTimeMillis();
	    Object data = null;
	    if(ctx != null){
	       ctx.startActivity(Context.ACTIVITY_DB_WRITE);
	    }
		try {
	       openedResultSetCount++;

	       data = tx.getSqlMapClient().insert(dbCtx.getSqlUniqueId(),dbCtx.getMap());

	       if(isQueryMonitorMode()){
	           long end=System.currentTimeMillis();
	           SysMonitor.addMonitorData( new MonitorData("Database",dbCtx.getSqlUniqueId(),end-start));
	       }
//				       debugSqlLog(ctx, sql, null, null);
	   }
	   catch (SQLException e) {
//				       logSQL(sql, null);
//				       debugSqlLog(ctx, sql, null, e);
	       LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
	       throw new DBException(dbCtx.getDbName(),e);
	   }
	   finally {
	       stopProfilerEvent(ctx);
	       if(ctx != null){
	           ctx.stopActivity();
	       }
	   }
	   return data;
	}


    /**
	 * 트랜잭션으로 묶일 필요가 있을 경우에 사용합니다.
	 * Delete 쿼리를 수행하고 커넥션은 IBatisTxManager.commit() IBatisTxManager.end()에서 닫힘니다.(IBATIS 이용)
     *
	 * @param DBContext
     * @return int : The number of rows effected.
     * @throws DBException
     */
	public static int delete(IbatisTxManager tx,DBContext dbCtx) throws DBException {

	   return update(tx,dbCtx);
	}



    /**
     * 트랜잭션으로 묶일 필요가 있을 경우에 사용합니다.
	 * Update 쿼리를 수행하고 커넥션은 IBSTxManager.commit() IBSTxManager.end()에서 닫힘니다.(IBATIS 이용)
	 *
	 * @param DBContext
     * @return int : The number of rows effected.
     * @throws DBException
     */
	public static int update(IbatisTxManager tx,DBContext dbCtx) throws DBException {
		//tx DBNAME과 DBCTX의 DBNAME일관성 체크 함수 추가
		checkDbNameConfig(tx, dbCtx);

	    Context ctx = dbCtx.getMap().getContext();
	    ctx = ContextLogger.getContext();
//				   debugSQL(ctx, sql, null);
		startProfilerEvent(ctx , dbCtx.getSqlUniqueId());
	    long start=System.currentTimeMillis();
	    int data;
	    if(ctx != null){
	       ctx.startActivity(Context.ACTIVITY_DB_WRITE);
	    }
		try {
	       openedResultSetCount++;

	       data = tx.getSqlMapClient().update(dbCtx.getSqlUniqueId(),dbCtx.getMap());

	       if(isQueryMonitorMode()){
	           long end=System.currentTimeMillis();
	           SysMonitor.addMonitorData( new MonitorData("Database",dbCtx.getSqlUniqueId(),end-start));
	       }
//				       debugSqlLog(ctx, sql, null, null);
	   }
	   catch (SQLException e) {
//				       logSQL(sql, null);
//				       debugSqlLog(ctx, sql, null, e);
	       LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
	       throw new DBException(dbCtx.getDbName(),e);
	   }
	   finally {
	       stopProfilerEvent(ctx);
	       if(ctx != null){
	           ctx.stopActivity();
	       }
	   }
	   return data;
	}

	private static void checkDbNameConfig(IbatisTxManager tx, DBContext dbCtx){
		if(!tx.getDBName().equals(dbCtx.getDbName())){
    		throw new SysException("FRD00009",
    				"ibatis TxManager DB Name("+tx.getDBName()
    				+")과 DBContext의 DBName("+dbCtx.getDbName()+")이 일치하지 않습니다. ");
    	}
	}




    public static void main(String[] args){

//    	String errorMessage = null;
//    	byte[] bufr = new byte[]{
//    			(byte)0xa1, 0x20, (byte)0xc0, 0x47, (byte)0xb9, (byte)0xe8, (byte)0xb5, (byte)0xcb, (byte)0xb4, (byte)0xcf, (byte)0xb4, (byte)0xd9, 0x0d, 0x0a
//    	};
/*    	if( bufr[bufr.length-2] == 0x0d  &&  bufr[bufr.length-1] == 0x0a ){
    		System.out.println("here");
    		byte[] newBufr = new byte[bufr.length-2];
    		System.arraycopy(bufr, 0, newBufr, 0, bufr.length-2);
    		errorMessage = new String(newBufr);
    	}*/
//    	System.out.println("error:"+StringUtil.NVL(errorMessage).trim()+"|");
//    	System.out.println("error:"+new String(bufr).trim()+"|");


		DataMap map = new DataMap();
		map.put("codeGroupId", "CM30140");
//		map.put("code", "0");
		map.put("asdf", "0");

		Context ctx = new Context();
		map.setContext(ctx);

		DBContext dbctx = new DBContext("FwkCodeTable","selectCodeByCodeGroupId",map);

		DataSet rs = DBHandler.executeQuery(dbctx);

		if( rs.getRowCount() > -0 ) {
			while(rs.next()) {
				System.out.print("####"+rs.getString("CODE_GROUP_ID"));
				System.out.print("#"+rs.getString("CODE"));
				System.out.print("#"+rs.getString("CODE_NAME"));
				System.out.print("#"+rs.getString("CODE_DESC"));
				System.out.print("#"+rs.getString("SORT_ORDER"));
				System.out.println(rs.getString("USE_YN")+"####");

			}
		}


		rs = DBHandler.executeQuery(dbctx,1,5);

		if( rs.getRowCount() > -0 ) {
			while(rs.next()) {
				System.out.print("####"+rs.getString("CODE_GROUP_ID"));
				System.out.print("#"+rs.getString("CODE"));
				System.out.print("#"+rs.getString("CODE_NAME"));
				System.out.print("#"+rs.getString("CODE_DESC"));
				System.out.print("#"+rs.getString("SORT_ORDER"));
				System.out.println(rs.getString("USE_YN")+"####");

			}
		}


    }


}
