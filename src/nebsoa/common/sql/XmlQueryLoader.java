/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.sql;

import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;
import nebsoa.common.startup.StartupContext;
import nebsoa.common.util.DbTypeUtil;
import nebsoa.common.util.PropertyManager;
import nebsoa.common.util.StringUtil;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 해당 sql문이 저장된 .xml파일 호출시, 해당 xml의 데이터를 cache한다.
 * (쿼리문 or xml파일 추가시 reload가 필요없음)
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
 * $Log: XmlQueryLoader.java,v $
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
 * Revision 1.5  2007/12/04 08:54:48  안경아
 * *** empty log message ***
 *
 * Revision 1.4  2007/12/03 01:13:50  안경아
 * *** empty log message ***
 *
 * Revision 1.3  2007/12/03 01:05:12  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2007/11/30 09:46:53  안경아
 * DB NAME 지정
 *
 * Revision 1.1  2007/11/26 08:39:04  안경아
 * *** empty log message ***
 *
 * Revision 1.15  2007/03/07 02:53:45  김성균
 * 일부 로그 INFO 레벨로 변경
 *
 * Revision 1.14  2007/02/08 06:46:16  최수종
 * 읽어들인 SQL 문자열에 TRIM()을 적용하여 앞, 뒤 공백 제거.
 * (프로시져를 CALL하는 SQL 문자열을 읽어올 경우, 예를들어 문자열이
 * 아래와 같다고 했을때,
 * {CALL XXX_PROCEDURE()}
 * 위의 쿼리 실행시 '{' 앞에 공백이 있으면 부적합한 SQL문으로 인식되므로
 * '{' 앞에 공백제거는 필수임.)
 *
 * Revision 1.13  2007/01/22 20:11:33  이종원
 * sql group id와 파일명이 다른 경우 디버깅 추가
 *
 * Revision 1.12  2006/12/27 05:13:43  김성균
 * *** empty log message ***
 *
 * Revision 1.11  2006/10/17 02:32:15  김성균
 * *** empty log message ***
 *
 * Revision 1.10  2006/07/18 02:55:12  최수종
 * *** empty log message ***
 *
 * Revision 1.9  2006/07/05 22:29:38  오재훈
 * *** empty log message ***
 *
 * Revision 1.8  2006/06/17 10:37:45  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class XmlQueryLoader extends DefaultHandler implements QueryLoader {
    
    private static final String ROOT_DIR = "framework";
	private static XmlQueryLoader instance;
    private static Hashtable cachePool;  // 각각의 xml파일들 저장(key : xml의 <sqlGroup>태그의 sqlGroupId값 , value : sqlList)
    private static Hashtable sqlList;  // xml에 포함되어 있는 각각의 sql문들 저장(key : xml의 <sql>태그의 Id값, value : xml의 sql문)
    private static StringBuffer sqlValue;  // xml <sql>태그안에 들어있는 쿼리문
    private static String sqlGroupId;  // xml <sqlGroup>태그의 ID
    private static String sqlId;  // xml <sql>태그의 ID
    
    private String sqlFileName;
    
    private static String loadTime = "0000-00-00 00:00:00";  // 최종 load 시간

    public XmlQueryLoader() {
    }
    
    /**
     * XmlQueryManager 인스턴스 생성
     * 
     * @return XmlQueryManager 인스턴스
     */
    public static synchronized XmlQueryLoader getInstance() {
        if(instance == null) {
            instance = new XmlQueryLoader();
            
            cachePool = new Hashtable();
            //sqlList = new Hashtable();
            
		    // 처음 load를 수행한 시간을 저장.
	        Calendar nowTime = Calendar.getInstance();
	        loadTime = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss").format(nowTime.getTime());
        }
        return instance;
    }   
    
    /**
     * Load 시간을 얻음.
     * 
     * @return loadTime load 시간
     */
    public String getLoadTime() {
        return loadTime;
    }  
    

	/**
	 * 모든 쿼리문을 가져와 캐쉬한다.
	 * 
	 */
	public void loadAll() {
	    
	    // 마지막으로 load를 수행한 시간을 저장.
        Calendar nowTime = Calendar.getInstance();
        loadTime = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss").format(nowTime.getTime());
        
	    /*
	     * 이 메소드는 사실상
	     * 현재 캐쉬되어있는 모든 목록을 클리어만한다.
	     * 다시 캐쉬를 하는 시점은 새로 쿼리를 요청할 경우에 xml에서
	     * 데이터를 읽어 캐쉬한다.
	     * 
	     */
	    cachePool = null;
	    sqlList = null;
	    sqlValue = null;
	    sqlGroupId = null;
	    sqlId = null;
	    instance = null;
	    
	    LogManager.info("################ 캐쉬 리로드 완료 ################");
	}
	
	
	/**
	 * 해당 xml파일에서 특정 sql문을 가져온다.
	 * 
	 * @param sqlGroupId 
	 * @param sqlId
	 * @return sql문자열
	 */
	public String getQuery(String sqlGroupId, String sqlId) {
	    
	    Hashtable sqlTable = null;
	    String query = null;
	    
	    try
	    {
	        sqlTable = getSqlList(sqlGroupId, sqlId, false);
		    query = (String)sqlTable.get(sqlId);
		    
		    /*
		     * query 결과가 null이면 한번 더 xml파일을 로드함.
		     * 신규로 쿼리문을 추가할 경우에는 별도의 reload 필요없음(그러나, 기존 쿼리문이 수정된 것이라면,
		     * loadAll() 메소드를 호출하여 캐쉬목록을 클리어한 후에 다시 쿼리문을 읽어야한다.)
		     */
		    if(query == null)
		    {
		        sqlTable = getSqlList(sqlGroupId, sqlId, true);
		        query = (String)sqlTable.get(sqlId);
		        
		        /*
			     * 그래도 query가 null이라면, 에러발생
			     */
			    if(query == null)
		        {
		            throw new Exception("해당 쿼리문이 없습니다. \n 요청한 sqlGroupId: "+sqlGroupId+" 요청한 sqlId: "+sqlId);
		        }
		    }
	    }
	    catch(Exception e)
	    {
	        LogManager.error("XmlQueryLoader.getQuery() 에러 : "+e.getMessage());
	    }
	    return query;
	}
	
	/**
	 * 해당 xml파일에서 특정 sql문을 가져온다.
	 * 
	 * @param sqlGroupId 
	 * @param sqlId
	 * @return sql문자열
	 */
	public String getQuery(int dbType, String sqlGroupId, String sqlId) {
	    
	    Hashtable sqlTable = null;
	    String query = null;
	    
	    try
	    {
	        sqlTable = getSqlList(dbType, sqlGroupId, sqlId, false);
		    query = (String)sqlTable.get(sqlId);
		    
		    /*
		     * query 결과가 null이면 한번 더 xml파일을 로드함.
		     * 신규로 쿼리문을 추가할 경우에는 별도의 reload 필요없음(그러나, 기존 쿼리문이 수정된 것이라면,
		     * loadAll() 메소드를 호출하여 캐쉬목록을 클리어한 후에 다시 쿼리문을 읽어야한다.)
		     */
		    if(query == null)
		    {
		        sqlTable = getSqlList(dbType, sqlGroupId, sqlId, true);
		        query = (String)sqlTable.get(sqlId);
		        
		        /*
			     * 그래도 query가 null이라면, 에러발생
			     */
			    if(query == null)
		        {
		            throw new Exception("해당 쿼리문이 없습니다. \n 요청한 sqlGroupId: "+sqlGroupId+" 요청한 sqlId: "+sqlId);
		        }
		    }
	    }
	    catch(Exception e)
	    {
	        LogManager.error("XmlQueryLoader.getQuery() 에러 : "+e.getMessage());
	    }
	    return query;
	    
	}
	
	/**
	 * 해당 XML에서 모든 sql문의 목록을 읽어온다.
	 * 
	 * @param sqlGroupId 
	 * @param sqlId 
	 * @param loadable xml을 읽을건지 결정(true : 다시 xml읽기 | false : xml읽지 않기)
	 * @return sqlTable XML에 정의된 SQL문들의 목록
	 * @throws Exception
	 */
	private Hashtable getSqlList(String sqlGroupId, String sqlId, boolean loadable) throws Exception{
	    
	    Hashtable sqlTable = (Hashtable)cachePool.get(sqlGroupId);
	    
	    if(sqlTable == null || loadable)
	    {
	        // xml데이터 다시 읽어오기
	        processReadXml(sqlGroupId, sqlId);
	        sqlTable = (Hashtable)cachePool.get(sqlGroupId);
	        
	        if(sqlTable == null)
	        {
	            throw new SysException("FRS00016",
                        "sql목록이 없습니다.(GroupId:"+sqlGroupId+",SqlId:"+sqlId+")");
	        }
	    }
	    return sqlTable;
	}

	/**
	 * 해당 XML에서 모든 sql문의 목록을 읽어온다.
	 * 
	 * @param sqlGroupId 
	 * @param sqlId 
	 * @param loadable xml을 읽을건지 결정(true : 다시 xml읽기 | false : xml읽지 않기)
	 * @return sqlTable XML에 정의된 SQL문들의 목록
	 * @throws Exception
	 */
	private Hashtable getSqlList(int dbType, String sqlGroupId, String sqlId, boolean loadable) throws Exception{
	    
	    Hashtable sqlTable = (Hashtable)cachePool.get(sqlGroupId);
	    
	    if(sqlTable == null || loadable)
	    {
	        // xml데이터 다시 읽어오기
	        processReadXml(dbType+sqlGroupId, sqlId);
	        sqlTable = (Hashtable)cachePool.get(sqlGroupId);
	        
	        if(sqlTable == null)
	        {
	            throw new SysException("FRS00016",
                        "sql목록이 없습니다.(GroupId:"+sqlGroupId+",SqlId:"+sqlId+")");
	        }
	    }
	    return sqlTable;
	}
	
	/**
	 * 해당 xml파일이 위치 정의
	 * 
	 * @param sqlGroupId
	 * @param sqlId
	 */
    private void processReadXml(String sqlGroupId, String sqlId) {
        StringBuffer filePath = null;
    	
	    // xml 파일 찾기
	    filePath = new StringBuffer();
	    filePath.append(PropertyManager.getProperty("default", "SQL_LOAD_PATH"));
        filePath.append("/");
        
        if(sqlGroupId.startsWith(DbTypeUtil.ORACLE+"")) filePath.append(ROOT_DIR+"/"+DbTypeUtil.ORACLE_SQL_DIR+"/").append(StringUtil.replaceAll(sqlGroupId.substring(1), ".", "/"));
        else if(sqlGroupId.startsWith(DbTypeUtil.MY_SQL+"")) filePath.append(ROOT_DIR+"/"+DbTypeUtil.MYSQL_SQL_DIR+"/").append(StringUtil.replaceAll(sqlGroupId.substring(1), ".", "/"));
        else if(sqlGroupId.startsWith(DbTypeUtil.MS_SQL+"")) filePath.append(ROOT_DIR+"/"+DbTypeUtil.MSSQL_SQL_DIR+"/").append(StringUtil.replaceAll(sqlGroupId.substring(1), ".", "/"));
        else if(sqlGroupId.startsWith(DbTypeUtil.SYBASE+"")) filePath.append(ROOT_DIR+"/"+DbTypeUtil.SYBASE_SQL_DIR+"/").append(StringUtil.replaceAll(sqlGroupId.substring(1), ".", "/"));
        else if(sqlGroupId.startsWith(DbTypeUtil.ALTIBASE+"")) filePath.append(ROOT_DIR+"/"+DbTypeUtil.ALTIBASE_SQL_DIR+"/").append(StringUtil.replaceAll(sqlGroupId.substring(1), ".", "/"));
        else if(sqlGroupId.startsWith(DbTypeUtil.DB2+"")) filePath.append(ROOT_DIR+"/"+DbTypeUtil.DB2_SQL_DIR+"/").append(StringUtil.replaceAll(sqlGroupId.substring(1), ".", "/"));
        else filePath.append(StringUtil.replaceAll(sqlGroupId, ".", "/"));
        filePath.append("Sql.xml");
        
        doParse(filePath.toString());
    }
    
    /**
	 * 해당 XML파일을 찾아 파싱한다.
	 * 
	 * @param filePath 파일경로+파일명임
	 */
    private void doParse(String filePath) {

        try {
            File file = new File(filePath);

            if (!file.exists()) {
                LogManager.error(filePath + " file을 찾을 수 없습니다");
                return;
            }
            LogManager.info("SQL 파일을 찾았습니다:" + file.getAbsolutePath());

            SAXParserFactory factory = SAXParserFactory.newInstance();
            // factory.setValidating(true);
            SAXParser parser = factory.newSAXParser();
            synchronized(this){
                sqlFileName=file.getAbsolutePath().replace('/','.');
                parser.parse(new InputSource(new FileReader(file)), this);
            }
        } catch (Exception e) {
            LogManager.error(filePath + ":" + e.getMessage());
        }
    }    

	/**
     * 문서 읽기 시작
     */
    public void startDocument() {
        sqlList = new Hashtable();
    }

	/**
     * 문서 읽기 마침
     */
    public void endDocument() {

        sqlValue = null;
        sqlGroupId = null;
        sqlId = null;
        sqlList = null;
    }
	
	/**
	 * xml파일 각 element 읽기 시작
	 */
	public void startElement(String uri, String localName, String tagName,  Attributes attributes) {
		try {
            // <sql>태그의 쿼리문을 저장하기 위한 변수로써 각 element읽기 시작시 항상 초기화
            sqlValue = new StringBuffer();

            if ("sqlGroup".equals(tagName)) {
                sqlGroupId = attributes.getValue("id");
                if(sqlFileName != null && sqlFileName.indexOf(sqlGroupId)==-1){
                    LogManager.info(sqlFileName+"파일에서  SqlGroup ID를 체크 하세요.["+sqlGroupId
                            +"],\n sql group id와 파일명이 다른 경우입니다.");
                }
            } else if ("sql".equals(tagName)) {
                sqlId = attributes.getValue("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogManager.error("startElement에서 " + tagName + " 태그 처리 중 에러\n " + e.getMessage());
        }
	}
	
	/**
	 * xml파일의 <sql></sql> 태그사이의 문자열 얻기
	 */
    public void characters(char ch[], int start, int length) throws SAXException {
        if (sqlValue != null) {
            sqlValue.append(ch, start, length);
        }
    }
    
    /**
     * xml파일 각 element 읽기 마침
     */
    public void endElement(String uri, String localName, String tagName) {

        if (tagName.equals("sqlGroup")) {
            cachePool.put(sqlGroupId, sqlList);
        } else { // tagName == sql
            sqlList.put(sqlId, sqlValue.toString().trim());
        }
    }

	
    /**
     * check xml
     */
    public void warning(SAXParseException exception) throws SAXException {
        LogManager.info("경고 : line - " + exception.getLineNumber());
        LogManager.info(exception.getMessage());
    }

    /**
     * check xml error
     */
    public void error(SAXParseException exception) throws SAXException {
        LogManager.error("DTD 위배오류 : line - " + exception.getLineNumber());
        LogManager.info(exception.getMessage());
    }

    /**
     * check well formed xml
     */
    public void fatalError(SAXParseException exception) throws SAXException {
        LogManager.error("Not Well-formed : line - " + exception.getLineNumber());
        LogManager.info(exception.getMessage());
    }
	
    public static void main(String args[]){
    	//System.setProperty("SPIDER_HOME", "/Project/spider");
    	//StartupContext.setPropertyType("XML");
    	System.out.println("12345".substring(1));
    	System.out.println("############ 시작");
//    	XmlQueryLoader.getInstance().processReadXml("test.table.TestTable", "COUNT_SQL");
//        System.out.println("테스트 getQuery():"+ XmlQueryLoader.getInstance().getQuery("test.table.TestTable", "COUNT_SQL1"));
//        System.out.println("테스트 getQuery():"+ XmlQueryLoader.getInstance().getQuery("test.table.TestTable", "FIND_BY_PAGE_SQL"));
//        System.out.println("테스트 getCacheList():"+ XmlQueryLoader.getInstance().getCacheList());
    	
//    	System.out.println("테스트 getQuery():"+ XmlQueryLoader.getInstance().getQuery("skt.web.admin.bill.FareSystem", "FIND_FOR_UPDATE_FORM_SQL"));
//    	System.out.println("테스트 getQuery():"+ XmlQueryLoader.getInstance().getQuery("skt.web.admin.bill.FareSystem", "CREATE_SQL"));
//    	System.out.println("테스트 getQuery():"+ XmlQueryLoader.getInstance().getQuery("skt.web.admin.bill.FareSystem", "FIND_FOR_UPDATE_FORM_SQL"));
    	System.out.println("테스트 getQuery():"+ XmlQueryLoader.getInstance().getQuery("keb.ibs.um.IUM80000", "FIND_BY_PAGE_SQL"));
    	System.out.println("############ 종료");
    }
}

