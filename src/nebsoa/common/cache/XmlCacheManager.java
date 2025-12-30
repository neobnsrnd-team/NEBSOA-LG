/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.cache;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import nebsoa.common.exception.SysException;
import nebsoa.common.jdbc.DBManager;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.PropertyManager;
import nebsoa.common.util.StringUtil;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 서블릿 엔진 기동시 cache-regist.xml을 읽어서 각 cache할 DATA를 읽어 들이는 클래스
 * 
 * 
 * 
 * 2.사용법
 * 먼저 WEB-INF/cache_register.xml에 등록한다.등록법은 파일 참조.
 * 캐쉬된 데이타는 DBResultSet 내지는 FormBean타입을 담고 있는 ArrayList를 얻어 내어 사용한다
 * 예1)ArrayList mapList = CacheManager.getInstance().getList("my_key");
 *     
 * 예2)ArrayList list = CacheManager.getInstance().getList("emp");
 *     EmpForm form = null;
 * 	   for(int i=0;i<list.size();i++){
 * 		   form = (EmpForm)list.get(0);
 * 		   ....
 * 	   }
 * 	cf : list.add(....)하면 캐쉬에도 추가 된다
 *      
 * 예3)DB에 데이타가 INSERT, UPDATE,DELETE등의 작업으로 변경되어
 *     모든 데이타를 리로딩 하고 싶으면
 * 	   CacheManager.getInstance().reloadAll();
 * 	   일부만 리로딩 하고 싶으면
 * 	   CacheManager.getInstance().reload("emp");
 *      
 * 예4)FormBean타입의 객체가 생성된후 그 객체에 추가적인 작업을 하고 싶으면
 * 	   (예: emp가 생성되면서 empBean안에 권한값을 db에서 읽어 와서 roles같은 변수에 담고 싶다면...)
 * 	   Cacheable 인터페이스를 상속받고 그 안에 있는 processPrepare라는 함수를 overriding하면 된다.
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
 * $Log: XmlCacheManager.java,v $
 * Revision 1.1  2018/01/15 03:39:49  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:29  cvs
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
 * Revision 1.1  2008/08/04 08:54:51  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:23  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:37:38  안경아
 * *** empty log message ***
 *
 * Revision 1.9  2006/11/02 08:24:01  최수종
 * *** empty log message ***
 *
 * Revision 1.8  2006/10/19 12:58:22  최수종
 * 캐쉬 메소드 추가
 *
 * Revision 1.7  2006/06/19 04:10:16  안경아
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class XmlCacheManager  extends DefaultHandler   {

	private static XmlCacheManager instance;

	private Hashtable cachePool;

	private Hashtable sqlList;

	static final String xmlFile = "cache_register.xml";

	public static XmlCacheManager getInstance() {
        if (instance == null)
            instance = new XmlCacheManager();
        
        return instance;
    }

    public XmlCacheManager() {
        cachePool = new Hashtable();
        sqlList = new Hashtable();
        instance = this;
        processReadXml();
    }

    public Hashtable getSqlList() {
        return sqlList;
    }

    public void setSqlList(Hashtable sqlList) {
        this.sqlList = sqlList;
    }

    /**
     * @param string
     * @return
     */
    public Object get(String key) {
        return cachePool.get(key);
    }
    
    /**
     * 캐쉬 결과를 ArrayList 형식으로 리턴합니다.
     * 
	 * 이 메소드는 기존의 Map로 캐쉬된 객체를 ArrayList로 
	 * 캐스팅하여 같은 이름으로 다시 캐쉬합니다. 
	 * 기존 캐쉬된 객체가 ArrayList라면 그대로 리턴합니다.
     * 
     * @param key
     * @return
     */
	public ArrayList getList(String key){
        
        Object obj = null;
		try {

            obj = cachePool.get(key);
            if(obj instanceof Map)
            {
            	ArrayList arr = null;
            	Map map = (Map)obj;
            	Iterator it = map.keySet().iterator();
            	while(it.hasNext())
            	{
            		arr.add(map.get(it.next()));
            	}
            	
            	cachePool.put(key, arr);
            	obj = arr;
            }
        } catch (ClassCastException e) {
            throw new SysException("잘못된 형변환 - ResultSet type의 객체가 아닙니다.");
        }
        if (obj == null) {
            throw new SysException(key + "라는 이름으로 캐쉬 된 객체가 없습니다.");
        }
        
        return (ArrayList)obj;
	}
	
	/**
	 * 캐쉬 결과를 Map 형식으로 리턴합니다.
	 * 이 메소드는 기존의 ArrayList로 캐쉬된 객체를 Map으로 
	 * 캐스팅하여 같은 이름으로 다시 캐쉬합니다.
	 * 
	 * 
	 * @param cachedKey 캐쉬명 
	 * @param key 캐쉬할 map의 key (Key는 DB 테이블의 컬럼명으로써, 중복데이타가 없는 컬럼이어야 함)
	 * @return Map타입의 캐쉬 목록
	 */
	public Map getMap(String cachedKey, String key){
		
        Object obj = null;
		try {
            obj = cachePool.get(cachedKey);
            
			if(obj instanceof ArrayList)
			{
				Map map = new TreeMap();
			    ArrayList arr = getList(cachedKey);
			    Map data = null;
			    
			    for(int i=0;i<arr.size();i++){
			        data = (Map)arr.get(i);
			        
			        if(map.containsKey(data.get(key)))
			        {
			        	throw new SysException("Key : ["+data.get(key)+"] - 중복된 키값이 존재합니다. 중복된 값이 없는 컬럼을 키로 지정해 주십시요.");
			        }
			        
			        map.put(data.get(key), data);
			    }
				cachePool.put(cachedKey, map);
				obj = map;
			}
		} catch (ClassCastException e) {
			throw new SysException("잘못된 형변환 - Map type의 객체가 아닙니다.");
	    } catch (SysException e) {
	        throw new SysException(e);
	    }		
        if (obj == null) {
            throw new SysException(key + "라는 이름으로 캐쉬 된 객체가 없습니다.");
        }		

	    return (Map)obj;
	}	
	
	/**
	 * 캐쉬 결과를 Map 형식으로 리턴합니다.
	 * 
	 * @param cachedKey 
	 * @param key
	 * @param value
	 * @return
	 */
	public Map getAsSingleMap(String cachedKey,String key,String value){
		Map map = new TreeMap();

	    ArrayList arr = getList(cachedKey);
	    Map data = null;
	    
	    for(int i=0;i<arr.size();i++){
	        data = (Map)arr.get(i);

	        map.put(data.get(key), StringUtil.NVL(data.get(value)));
	    }
	    return map;
	}

	public void put(String key,Object obj){
		cachePool.put(key,obj);
	}

	public void reloadAll(){
		cachePool = new Hashtable();
		sqlList = new Hashtable();
		processReadXml();
	}

	public void reload(String key){
		String value = (String) sqlList.get(key);
        
        if (value == null)
            throw new SysException("리로딩 실패 : 등록되지 않은 키 값-" + key);
        
        String[] values = StringUtil.toArray(value, "@");
        if (values == null || values.length != 3) {
            throw new SysException(
                    "리로딩 실패 : cacheManager의 sqlList에 잘못된 값이 들어 있습니다." + value);
        }
        
        String clazz = values[0];
        String sql = values[1];
        String db = values[2];

        if ("null".equals(clazz))
            clazz = null;
        if ("null".equals(db))
            db = null;

        processCacheData(key, clazz, sql, db);
	}

    public void reloadBean(String key) {
        Object obj = cachePool.get(key);
        if (obj == null) {
            throw new SysException("등록 되어 있지 않은 객체 리로딩 요청 ");
        }
        if (obj instanceof Cache) {
            Cache cache = (Cache) obj;
            cache.reload();
        } else {
            throw new SysException("Cacheable을 상속 받은 클래스만 리로딩 가능합니다.");
        }
    }

	public String toString() {
        return "캐쉬된  객체목록\n"
                + StringUtil.replace(StringUtil.replaceAll(
                        cachePool.toString(), ",", "\n--->"), "{", "--->");
    }

    public void processReadXml() {
        String filePath = PropertyManager.getProperty("default",
                "CACHE_REGIST_XML_FILE_PATH");
        doParse(filePath);
    }

    public void doParse(String filePath) {

        try {
            File file = new File(filePath);

            if (!file.exists()) {
                LogManager.error(filePath + " file을 찾을 수 없습니다");
                return;
            }
            LogManager.debug("cache 설정 파일을 찾았습니다--" + file.getName());

            SAXParserFactory factory = SAXParserFactory.newInstance();
            // factory.setValidating(true);
            SAXParser parser = factory.newSAXParser();

            parser.parse(new InputSource(new FileReader(file)), this);

        } catch (Exception e) {
            LogManager.error(filePath + ":" + e.getMessage());
        }
    }

    /**
     * 문서 읽기 시작
     */
    public void startDocument() {
    }

    /**
     * 문서 읽기 시작
     */
    public void endDocument() {
        String debug_cache_log = PropertyManager.getProperty("default",
                "DEBUG_CACHE_LOG", "true");
        if ("true".equals(debug_cache_log)) {
            LogManager.debug(this);
        }
    }

    public void startElement(String uri, String localName, String tagName,
            Attributes attributes) {
        if (!"module".equals(tagName)) {
            return;
        }

        String clazz = attributes.getValue("class");
        String key = attributes.getValue("name");
        String sql = attributes.getValue("sql");
        String db = attributes.getValue("db");

        LogManager.info("xml에서 읽은 값");
        LogManager.info("class-->" + clazz);
        LogManager.info("key-->" + key);
        LogManager.info("sql-->" + sql);
        LogManager.info("db-->" + db);

        sqlList.put(key, clazz + "@" + sql + "@" + db);
        LogManager.info("sql등록-key-->" + key + ",value-->" + clazz + "@" + sql
                + "@" + db);

        processCacheData(key, clazz, sql, db);
    }

	/**
     * 데이타를 읽어서 캐쉬한다.
     * class이름이 지정되면 그 클래스 객체를 데이타 건수 만큼 생성하여 담고,
     * 그렇지 않은 경우는 DBResultSet 형태로 담는다
     */
    public void processCacheData(String key, String clazz, String sql, String db) {

        if (StringUtil.isNull(clazz)) {
            ArrayList arr = DBManager.executeQueryToMapList(db, sql);
            cachePool.put(key, arr);
            LogManager.debug("add to cache ArrayList of Map.객체.");
            return;
        }

        LogManager.debug("add to cache VO TYPE-->" + clazz);
        Class cls = null;
        try {
            cls = Class.forName(clazz);
        } catch (Exception e) {
            LogManager.error("class Not found..." + clazz);
            return;
        }
        ArrayList arr = DBManager.executeQueryToBeanList(db, sql, cls);
        cachePool.put(key, arr);
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
}

