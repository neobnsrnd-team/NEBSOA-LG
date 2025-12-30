/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
package nebsoa.common.jdbc.dbcp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import nebsoa.common.log.LogManager;
import nebsoa.common.util.PropertyManager;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDriver;
import org.apache.commons.pool.impl.GenericObjectPool;




/*******************************************************************
 * <pre>
 * 1.설명 
 * DB Connection Pool을 관리하는 클래스입니다.
 * 초기에 DB Connection Pool을 모두 생성하지 않고,
 * DB연결 요청 시점에 풀이 없으면 생성한다.
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
 * $Log: DBCPManager.java,v $
 * Revision 1.1  2018/01/15 03:39:54  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:37  cvs
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
 * Revision 1.3  2008/08/29 14:43:23  최수종
 * *** empty log message ***
 *
 * Revision 1.2  2008/08/29 06:50:52  최수종
 * *** empty log message ***
 *
 * Revision 1.1  2008/08/29 06:41:01  최수종
 * DBCP 라이브러리를 사용한
 * DB커넥션 풀링 로직 추가
 *
 * 
 *
 * </pre>
 ******************************************************************/
public class DBCPManager {
	
	
	// key: dbName, value: jdbc url형식(jdbc:oracle:thin:@127.0.0.1:1521:amrlink)
	private static Map dbNames;  // dbName 목록이 들어 있음.(DB 커넥션풀 명 == dbName)
	
	private static DBCPManager instance;

	
	/**
	 * DBCPManager 인스턴스 얻기
	 */
	public static synchronized DBCPManager getInstance()
	{
		if(instance == null)
		{
			instance = new DBCPManager();
		}
		return instance;
	}
	
	private DBCPManager()
	{
		dbNames = Collections.synchronizedMap(new HashMap());
		init();
	}
	
	/**
	 * DB커넥션 풀 생성 초기화
	 */
	private void init()
	{
	}

	
	/**
	 * DB커넥션 풀로부터  커넥션 객체를
	 * 얻기 위한 메소드 
	 * 
	 * @param dbName
	 * @return
	 */
	public Connection getConnection(String dbName) throws SQLException {	

		try {
	        Connection con = DriverManager.getConnection("jdbc:apache:commons:dbcp:"+dbName);
	        
			return con;
		} catch (SQLException e) {
			throw e;
		}
	}	
	
	
	/**
	 * 개별 DB Connection Pool 생성
	 * (커넥션풀(poolName) 명은 systemID와 동일)
	 * 
	 * @param poolName
	 * @param driver
	 * @param url
	 * @param user
	 * @param pw
	 * @throws Exception
	 */
	public void createConnectionPool(
			String poolName,
			String driver,
			String url,
			String user,
			String pw) throws Exception
	{
		System.out.println("##### DB Connection Pool 준비...");
		
		// Pool 생성
		setDriver(poolName, driver, url, user, pw);
		
		// 커넥션풀 상태 출력
		printDriverStats(poolName);
	}
	
	
	/**
	 * 개별 DB Connection Pool 생성
	 * (커넥션풀 명은 systemID와 동일) 
	 * 
	 * @param poolName
	 * @param jdbcDriver
	 * @param jdbcUrl
	 * @param user
	 * @param password
	 */
	private synchronized void setDriver(
			String poolName,
			String jdbcDriver,
			String jdbcUrl,
			String user,
			String password) throws Exception
	{
		String cacheName = (String)dbNames.get(poolName);
		if(cacheName != null)
		{
			System.out.println("==============================================================");
			System.out.println("##### 이미 생성된 DB Connection Pool입니다. poolName=>"+poolName);
			return;
		}
		
		System.out.println("==============================================================");
		System.out.println("##### DB Connection Pool 생성... poolName=>"+poolName);
		try
		{
			//Class.forName(jdbcDriver);
			
			int maxActive = PropertyManager.getIntProperty("db", poolName+".MAX_ACTIVE", 40);  // 최대 연결수
			int maxIdle = PropertyManager.getIntProperty("db", poolName+".MAX_IDLE", 10);  // 최대 연결 유지수
			int minIdle = PropertyManager.getIntProperty("db", poolName+".MIN_IDLE", 10);  // 최소 연결 유지수
			int maxWait = PropertyManager.getIntProperty("db", poolName+".MAX_WAIT", 5000);  // 연결 요청시도후 대기 시간(1/1000초), DEFAULT는 5초  
			String testSql = PropertyManager.getProperty("db", poolName+".TEST_SQL", "select 1 from dual");  // 연결 테스트 쿼리
			boolean testWhileIdle = PropertyManager.getBooleanProperty("db", poolName+".TEST_WHILE_IDLE", "true");  // TRUE면 비활성화 커넥션을 추출할때 커넥션이 유효한지 검사하여 유효하지 않은 커넥션은 풀에서 제거
			
			// 이 옵션을 양수로 설정 경우, 해당 주기에 커넥션 체크 쓰레드가 돌때, 성능에 영향을 줌(체크하는 동안 커넥션 연결 요청시 락걸림).
			long timeBetweenEvictionRunsMillis = 
				Long.parseLong(PropertyManager.getProperty("db", poolName+".TIME_BETWEEN_EVICTION_RUNS_MILLIS", "-1"));  // default로 -1로 설정함(돌지 않음).  //양수로 설정시 10분(600000 m/s)일 경우, 이 주기로 사용되지 않는 커넥션 추출
			boolean testOnBorrow = 
				PropertyManager.getBooleanProperty("db", poolName+".TEST_ON_BORROW", "false");  // TRUE면 커넥션을 풀에서 얻기전에 유효한 커넥션인지 체크한다.(현재 커넥션이 유효하지 않으면 풀에서 제거하고, 풀에서 다른 커넥션을 얻는다.)  
			
			
			System.out.println("##### DB Connection Pool["+poolName+"] maxActive : "+maxActive);
			System.out.println("##### DB Connection Pool["+poolName+"] maxIdle : "+maxIdle);
			System.out.println("##### DB Connection Pool["+poolName+"] minIdle : "+minIdle);
			System.out.println("##### DB Connection Pool["+poolName+"] maxWait : "+maxWait);
			System.out.println("##### DB Connection Pool["+poolName+"] testSql : "+testSql);
			System.out.println("##### DB Connection Pool["+poolName+"] testWhileIdle : "+testWhileIdle);
			System.out.println("##### DB Connection Pool["+poolName+"] timeBetweenEvictionRunsMillis : "+timeBetweenEvictionRunsMillis);
			System.out.println("##### DB Connection Pool["+poolName+"] testOnBorrow : "+testOnBorrow);
			
			GenericObjectPool connectionPool = new GenericObjectPool(null);
			connectionPool.setMaxActive(maxActive);  // 최대 연결수
			connectionPool.setMaxIdle(maxIdle);  // 최대 연결 유지수
			connectionPool.setMinIdle(minIdle);  // 최소 연결 유지수
			connectionPool.setMaxWait(maxWait);  // 5초
			connectionPool.setTestWhileIdle(testWhileIdle);
			connectionPool.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
			connectionPool.setTestOnBorrow(testOnBorrow);
//			connectionPool.setMinEvictableIdleTimeMillis(120000);
			
			ConnectionFactory connectionFactory = 
				new DriverManagerConnectionFactory(jdbcUrl, user, password);
			
			PoolableConnectionFactory poolableConnectionFactory = 
				new PoolableConnectionFactory(
						connectionFactory,
						connectionPool,
						null,  // stmtPoolFactory
						testSql,  // connection 테스트 쿼리
						false,  // read only 여부
						true);  // auto commit 여부
			
			Class.forName("org.apache.commons.dbcp.PoolingDriver");
	        PoolingDriver driver = (PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");
			
	        driver.registerPool(poolName, connectionPool);
	        
	        
	        dbNames.put(poolName, jdbcUrl);
		}
		catch(Exception e)
		{
//			LogManager.debug("### dbcp 오류:"+e.getMessage());
			
			System.out.println("DB Pool 생성중 오류=>"+e.getMessage());
			e.printStackTrace();
			throw e;
		}
		
	}
	
	
	/**
	 * 개별 DB Connection Pool 상태 출력
	 * 
	 * @param poolName 커넥션풀 명(== dbName)
	 * @throws Exception
	 */
    public void printDriverStats(String poolName) throws SQLException {
        PoolingDriver driver = (PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");
        GenericObjectPool connectionPool = (GenericObjectPool)driver.getConnectionPool(poolName);
        
        LogManager.info("-------------------------------------------");
        LogManager.info("DBCP["+poolName+"] NumActive: " + connectionPool.getNumActive());
        LogManager.info("DBCP["+poolName+"] NumIdle: " + connectionPool.getNumIdle());
    }
    
	/**
	 * 모든 DB Connection Pool 상태 출력
	 * 
	 * @throws Exception
	 */
    public void printAllDriverStats() throws SQLException {
        PoolingDriver driver = (PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");
        
//        HashMap data = null;
//        String systemId = null;
//		for(int i=0; i<dbInfo.size() ; i++)
//		{
//			data = (HashMap)dbInfo.get(i);
//			systemId = (String)data.get("SYSTEM_ID");
//			
//			GenericObjectPool connectionPool = (GenericObjectPool)driver.getConnectionPool(systemId);
//			
//			LogManager.info("-------------------------------------------");
//	        LogManager.info("DBCP["+systemId+"] NumActive: " + connectionPool.getNumActive());
//	        LogManager.info("DBCP["+systemId+"] NumIdle: " + connectionPool.getNumIdle());		
//		}
        
        
        Iterator it = dbNames.keySet().iterator();
        String key = null;
//        String value = null;
        while(it.hasNext())
        {
        	key = (String)it.next();
//        	value = (String)dbNames.get(key);
        	
			GenericObjectPool connectionPool = (GenericObjectPool)driver.getConnectionPool(key);
			
			LogManager.info("-------------------------------------------");
	        LogManager.info("DBCP["+key+"] NumActive: " + connectionPool.getNumActive());
	        LogManager.info("DBCP["+key+"] NumIdle: " + connectionPool.getNumIdle());		
        	
        }
    }
    
    
    /**
     * 개별 DB Connection Pool Close
     * 
     * @param poolName 커넥션풀 명(== systemId)
     * @throws Exception
     */
    public synchronized void shutdownDriver(String poolName) throws Exception {
    	
		String cacheName = (String)dbNames.get(poolName);
		if(cacheName == null)
		{
			System.out.println("==============================================================");
			System.out.println("##### 이미 close된 DB Connection Pool입니다. poolName=>"+poolName);
			return;
		}
    	
        PoolingDriver driver = (PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");
        driver.closePool(poolName);
        
        dbNames.remove(poolName);
    }
    
    /**
     * 모든 DB Connection Pool Close
     * 
     * @param poolName 커넥션풀 명(== systemId)
     * @throws Exception
     */
    public synchronized void shutdownDriverAll() throws Exception {
    	
    	if(dbNames.size() < 1) {
    		return;
    	}
    	
        PoolingDriver driver = (PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");
        
//        HashMap data = null;
//        String systemId = null;
//		for(int i=0; i<dbInfo.size() ; i++)
//		{
//			data = (HashMap)dbInfo.get(i);
//			systemId = (String)data.get("SYSTEM_ID");
//			
//			// pool close.
//	        driver.closePool(systemId);
//		}
        
        Iterator it = dbNames.keySet().iterator();
        String key = null;
//        String value = null;
        while(it.hasNext())
        {
        	key = (String)it.next();
//        	value = (String)dbNames.get(key);
        	
			// pool close.
	        driver.closePool(key);
        }
        
		dbNames.clear();
    }
	
    
//    /**
//     * DB Connection Pool 개별 reload 
//     * @param poolName 
//     */
//    public void reloadConnectionPool(String poolName) throws Exception
//    {
//    	shutdownDriver(poolName);
//    	createConnectionPool(poolName);
//    }
   
    
    /**
     * Map에 담긴 db명 목록 출력
     */
    public void printDBNames()
    {
    	
    	LogManager.info("현재 생성된 커넥션 풀 DB Names: " + dbNames);	
    }
}
