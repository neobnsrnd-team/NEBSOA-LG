/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.jdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.sql.DataSource;

import nebsoa.common.exception.DBException;
import nebsoa.common.exception.SysException;
import nebsoa.common.jdbc.dbcp.DBCPManager;
import nebsoa.common.jndi.JNDIManager;
import nebsoa.common.log.LogManager;
import nebsoa.common.monitor.profiler.Profile;
import nebsoa.common.monitor.profiler.Profiler;
import nebsoa.common.startup.StartupContext;
import nebsoa.common.util.LCManager;
import nebsoa.common.util.PropertyManager;
import nebsoa.common.util.StringUtil;

/*******************************************************************
 * <pre>
 * 1.설명 
 * DB설정 정보를 가지고 있는 클래스
 * db.properties파일에서 등록된 갯수 만큼 만들어 집니다.
 * (예: oracle.xxxxx, mysql.xxxx를 만나면 각각의 정보를 가진 2개의 객체가 만들어 집니다)
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
 * $Log: DBConfig.java,v $
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
 * Revision 1.2  2008/08/29 06:41:01  최수종
 * DBCP 라이브러리를 사용한
 * DB커넥션 풀링 로직 추가
 *
 * Revision 1.1  2008/08/04 08:54:54  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:31  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:37:44  안경아
 * *** empty log message ***
 *
 * Revision 1.20  2007/03/07 02:53:45  김성균
 * 일부 로그 INFO 레벨로 변경
 *
 * Revision 1.19  2006/11/21 05:43:58  이종원
 * DatabaseMonitorMode 추가
 *
 * Revision 1.18  2006/11/16 12:37:28  오재훈
 * DataBase 연결장애시(DataSource를 얻지 못했을경우)의 에러코드 던짐
 *
 * Revision 1.17  2006/10/24 13:21:23  이종원
 * lookup 시 발생하는 오류 메시지 수정
 *
 * Revision 1.14  2006/07/05 19:34:09  이종원
 * 변경 없음
 *
 * Revision 1.13  2006/07/04 11:50:16  이종원
 * *** empty log message ***
 *
 * Revision 1.12  2006/07/04 11:31:47  이종원
 * *** empty log message ***
 *
 * Revision 1.11  2006/07/03 13:16:25  이종원
 * getDataSource(wasConfig) 메소드 버그 수정
 *
 * Revision 1.10  2006/06/21 09:43:03  이종원
 * DBHanderl추가
 *
 * Revision 1.9  2006/06/19 04:10:16  안경아
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class DBConfig {


    private static Hashtable dbConfigList;
    private static Context context;
    
    static {
        dbConfigList = new Hashtable();
    }
    
    public static String getDefafultDBName() {
        //return StartupContext.getDefaultDBName();
        return PropertyManager.getProperty("db","DEFAULT_DB");
    }



    public static DBConfig getInstance(String dbName) {
        try {
            LCManager.lc();
        } catch (Exception e) {
            throw new Error(e.getMessage());
        }
        if (dbName==null){
            dbName = getDefafultDBName();
        }
        
        Object dbConfig = dbConfigList.get(dbName);
        if(dbConfig ==null) {
            synchronized(dbConfigList){
                dbConfig = new DBConfig(dbName);
                dbConfigList.put(dbName,dbConfig);
            }
        }
        return (DBConfig)dbConfig;
        
    }
    
    public static void reloadAll(){
        dbConfigList = new Hashtable();
    }


    String dbName;
    /**
     * USE_DATASOURCE사용 여부
     */
    boolean USE_DATASOURCE=false;
    String dataSourceName;
    private String dataSourcePrefix;
    private String serverConfig;

    String dbUrl;
    String id;
    private String passwd;
    private String driver;
    private Driver myDriver;
    private int initConnectionSize=2;

    private DataSource dataSource;

    private boolean useDBPool = false;  // DBCP 사용여부(자체 DB커넥션 풀)

    String descOfMe;
    public String toString(){
         return descOfMe;
    }
    
    
    public void loadConfig(){
        
        descOfMe = dbName+":";

        boolean useDataSource = PropertyManager.getBooleanProperty("db",dbName+".USE_DATASOURCE");
        if(useDataSource) {
            USE_DATASOURCE=true;
            dataSourceName = PropertyManager.getProperty("db",dbName+".DATASOURCE.NAME");
            dataSourcePrefix = StringUtil.NVL(PropertyManager.getProperty("db",dbName+".DATASOURCE.PREFIX",""),"");
            
            serverConfig = PropertyManager.getProperty("db",dbName+".WAS_CONFIG_MAPPING","default");
            dataSource = getDataSource(serverConfig);
            descOfMe += dataSourceName;

        }else{
        	// DBCP라이브러리를 이용한 DB Pool을 사용설정.
        	useDBPool = PropertyManager.getBooleanProperty("db", dbName+".POOLABLE", "false");
        	
            driver = PropertyManager.getProperty("db",dbName+".DRIVER");
            dbUrl = PropertyManager.getProperty("db",dbName+".URL");
            id = PropertyManager.getProperty("db",dbName+".ID",null);
            passwd = PropertyManager.getProperty("db",dbName+".PASSWD",null);
            String initConnectionSizeStr = PropertyManager.getProperty("db",dbName+".INIT_CONNECTION_SIZE","3");
            try{
                initConnectionSize = Integer.parseInt(initConnectionSizeStr);
                LogManager.info("INIT CONNECTION 갯수 "+initConnectionSize);
            }catch(Exception e){
                LogManager.error("INIT CONNECTION 갯수가 숫자가 아닙니다");
            }
            descOfMe += dbUrl+":"+id;

            LogManager.info("db url : "+dbUrl+"/"+id+"/"+passwd);
            try{
                //myDriver = (Driver) Class.forName("weblogic.jdbc.pool.Driver").newInstance();
                myDriver = (Driver) Class.forName(driver).newInstance();
                LogManager.info(dbName+" jdbc 드라이버 ok");

            }catch(Exception e){
                LogManager.error(dbName+ "DB Connection 초기 생성 중 에러", e);
                throw new DBException(e.getMessage());
            }
            
            /*
             * DBCP 라이브러리를 이용한 DB커넥션 풀을 사용하기 위한 설정 
             */
            if(useDBPool) {
            	try {
            		// 풀 생성
            		DBCPManager.getInstance().createConnectionPool(dbName, driver, dbUrl, id, passwd);
            	}catch(Exception e) {
                    LogManager.error(dbName+ "DB Connection Pool 초기 생성 중 에러", e);
                    throw new DBException(e.getMessage());
            	}
            }
        }
    }

    private DBConfig(String dbName) throws DBException
    {
        this.dbName = dbName;
        loadConfig();
    }

    public synchronized Connection makeConnection(){
        Connection con = null;
        try{
            LogManager.info(dbUrl+"로 커넥션 시도");
            if(id != null && passwd != null){
                con = DriverManager.getConnection(dbUrl,id,passwd);
                return con;
            }else{
                //return DriverManager.getConnection( dbUrl) ;
                con = myDriver.connect(dbUrl,null);
                return con;
            }
         }catch(SQLException e){
            LogManager.error(dbName+" DB로  커넥션을 얻지 못했습니다"+e.getMessage(),e);
            throw new DBException(e.getMessage());
        }
    }
    
    /**
     * DBCP 라이브러리를 사용하여
     * DB 커넥션을 얻는 메소드.
     * 
     * @param dbName DB명
     * @return
     */
    public Connection makeConnection(
    		String dbName){
        Connection con = null;
        try{
            LogManager.info(dbUrl+"로 커넥션 시도[DBCP사용], dbName:["+dbName+"]");
            
            con = DBCPManager.getInstance().getConnection(dbName);
            
            return con;
         }catch(Exception e){
            LogManager.error(dbName+" DB로  커넥션[DBCP사용]을 얻지 못했습니다"+e.getMessage(),e);
            throw new DBException(e.getMessage());
        }
    }
    

    public static boolean isDBMonitorMode() {
        return PropertyManager.getBooleanProperty("db",
            "DATABASE_MONITOR_MODE", "true");    
    }

   /*
    * getConnection
    */
    public java.sql.Connection getConnection(nebsoa.common.Context context) throws DBException
    {
        try{
            if(context != null && context.getProfiler() != null){
                context.getProfiler().startEvent("DBConnection::getConnection::");
            }
            
            Connection conn=null;
            if(USE_DATASOURCE ==true){
                try{
                    LogManager.info(dataSourceName+" connection Pool 사용 중");
                    if(dataSource==null) {
                        dataSource = getDataSource(serverConfig);
                    }
                    conn=dataSource.getConnection();
                }catch(SQLException e){
                    LogManager.error("DBMANAGER=>"+dataSourceName+"에서 커넥션을 얻지 못했습니다");
                    throw new DBException("FRD00000",e.getMessage());
                }
            }else if(useDBPool) {
            	// DBCP 풀을 사용하는 경우.
           		conn= makeConnection(this.dbName);
            }else {
            	conn= makeConnection();
            }
            
            try {
                if (DBManager.isQueryMonitorMode() &&
                        conn.getAutoCommit() == false) {
                    Exception e = new Exception(
                            "getAutoCommit false인 connection이 발견되었습니다.");
                    LogManager.error("MONITOR", e.toString(), e);
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
            }
            
            return conn;
        }finally{
            if(context != null && context.getProfiler() != null){
                context.getProfiler().stopEvent();
            }
        }
    }
    
    
    /*
     * getConnection
     */
     public java.sql.Connection getConnection() throws DBException, SQLException
     {
         return getConnection(null);
     }



    /*
     * getDataSource
     */
     public DataSource getDataSource(String serverConfig){
        try{
            if(context==null){
                try {
                    context = JNDIManager.getInitialContext(serverConfig);
                } catch (Exception e1) {
                    throw new SysException("WAS 서버에 접근할 수 없습니다(WAS장애 이거나, WAS CONFIG 설정 파일 오류입니다.");
                }
            }

            LogManager.info("DBMANAGER=>DataSource ("+dataSourceName+")을 찾습니다");
            return	(DataSource) context.lookup(dataSourcePrefix+dataSourceName);

        }catch(NamingException e){
            LogManager.error("DBMANAGER=>등록된 DataSource ("+dataSourceName+")을 찾지 못했습니다");
            throw new DBException(e.getMessage());

        }finally{
           try{
               if ( context != null ) {
                   context.close();
                   context=null;
               }
            }catch(Exception e){}
        }
    }
}
