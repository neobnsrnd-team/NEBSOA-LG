/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import nebsoa.common.exception.ReloadFailException;
import nebsoa.common.exception.SysException;
import nebsoa.common.jdbc.DBResultSet;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.StringUtil;

/*******************************************************************
 * <pre>
 * 1.설명 
 * Cache를 관리하는 클래스
 *  Cache하고자 하는 객체가 있을 경우 
 *  Cache cache = new Cache(myName);
 *  cache.setData("this is toCache");
 *  CacheManager.getInstance().put(cache);
 *  로 사용가능하며 
 *  추가적으로 reloading가능하게 하려면 
 *  별도 상속 받은 클래스로 구현한다.
 *  Cache cache = new MyReloadCache(myName);
 *  cache.setReloaderble(true);
 *  cache.setAutoReload(true);
 *  cache.setData("this is toCache");
 *  CacheManager.getInstance().put(cache);
 * 
 * 2.사용법
 *	예1)ArrayList mapList = CacheManager.getInstance().getList("my_key");
 *	예2)ArrayList list = CacheManager.getInstance().getList("emp");
 *	    EmpForm form = null;
 *		for(int i=0;i<list.size();i++){
 *			form = (EmpForm)list.get(0);
 *			....
 *		}
 *		cf : list.add(....)하면 캐쉬에도 추가 된다
 *	예3)DB에 데이타가 INSERT, UPDATE,DELETE등의 작업으로 변경되어
 *	    모든 데이타를 리로딩 하고 싶으면
 *		CacheManager.getInstance().reloadAll();
 *		일부만 리로딩 하고 싶으면
 *		CacheManager.getInstance().reload("emp");
 *	예4)FormBean타입의 객체가 생성된후 그 객체에 추가적인 작업을 하고 싶으면
 *		(예: emp가 생성되면서 empBean안에 권한값을 db에서 읽어 와서 roles같은 변수에 담고 싶다면...)
 *		Cacheable 인터페이스를 상속받고 그 안에 있는 processPrepare라는 함수를 overriding하면 된다.
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
 * $Log: CacheManager.java,v $
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
 * Revision 1.13  2006/10/19 12:58:22  최수종
 * 캐쉬 메소드 추가
 *
 * Revision 1.12  2006/06/19 04:10:16  안경아
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class CacheManager  {

	private static CacheManager instance;

	private HashMap cachePool;

	private HashMap keyPool;

	public static CacheManager getInstance() {
		if (instance == null)
			instance = new CacheManager();
		return instance;
	}

    private CacheManager() {
		cachePool = new HashMap();
		keyPool = new HashMap();
	}
    
    /**
	 * @return Returns the keyPool.
	 */
	public HashMap getKeyPool() {
		return keyPool;
	}

	/**
	 * @param keyPool The keyPool to set.
	 */
	public void setKeyPool(HashMap keyPool) {
		this.keyPool = keyPool;
	}

	/**
	 * Cache된DBResultSet 객체를 DBResultSet으로 리턴
	 * 
	 * @param key
	 * @return
	 */
	public DBResultSet getResultSet(String key) {
		DBResultSet rs = null;
		
		try {
			rs = (DBResultSet) cachePool.get(key);
		} catch (ClassCastException e) {
			throw new SysException("잘못된 형변환 - ResultSet type의 객체가 아닙니다.");
		}
		
		if (rs == null) {
			throw new SysException("등록되지(캐쉬되지) 않은 객체입니다.");
		}
		
		LogManager.debug("캐쉬데이타-->" + rs);
		rs.initRow();
		return rs;
	}
    
    /**
	 * Cache된 ArrayList 객체를 리턴
	 * 
	 * @param key
	 * @return
	 */
	public ArrayList getList(String key) {
		ArrayList arr = null;
		try {
			arr = (ArrayList) cachePool.get(key);
		} catch (ClassCastException e) {
			throw new SysException("잘못된 형변환 - ResultSet type의 객체가 아닙니다.");
		}
		if (arr == null) {
			throw new SysException(key + "라는 이름으로 캐쉬된 객체가 없습니다.");
		}

		return arr;
	}
	
	public Map getSingleMap(String cachedKey,String key,String value){
	    Map map = new TreeMap();
	    ArrayList arr = getList(cachedKey);
	    Map data = null;
	    for(int i=0;i<arr.size();i++){
	        data = (Map)arr.get(i);
	        map.put(data.get(key),data.get(value));
	    }
	    return map;
	}
    
    public Object get(String key) {
		return cachePool.get(key);
	}

	public Object get(Cache key) {
		return cachePool.get(key.getName());
	}

	public void put(String key, Object obj) {
		if (obj == null) {
			throw new SysException("등록할 객체가 없습니다.");
		}
		cachePool.put(key, obj);
		keyPool.put(key, new Cache(key));
	}
    
    public void put(Cache cache, Object obj) {
		if (obj == null) {
			throw new SysException("등록할 객체가 없습니다.");
		}
		cachePool.put(cache.getName(), obj);
		keyPool.put(cache.getName(), cache);
	}
    
    public void put(Cache cache) {
		if (cache.getData() == null) {
			throw new SysException("등록할 객체가 없습니다.");
		}
		cachePool.put(cache.getName(), cache.getData());
		keyPool.put(cache.getName(), cache);
	}

	/**
	 * 모든 캐쉬정보를 리로딩 한다.
	 */
	public void reloadAll() {
        Set set = keyPool.keySet();
        Iterator it = set.iterator();
        String key = null;
        while (it.hasNext()) {
            key = (String) it.next();
            Cache cache = (Cache) keyPool.get(key);
            try {
                Object obj = cache.reload();
                cachePool.put(cache.getName(), obj);
            } catch (ReloadFailException e) {
                System.out.println(e.getMessage());
            }
        }
    }

	public void reload(String key){
        Cache cache = (Cache) keyPool.get(key);
        if(cache.isReloaderable){
            Object obj = cache.reload();
            cachePool.put(key,obj);
        }else{
            throw new ReloadFailException("리로딩 가능한 객체가 아닙니다.");
        }
    }
    
    public void reload(Cache _cache){
        Cache cache = _cache;
        if(cache.isReloaderable){
            Object obj = cache.reload();
            cachePool.put(cache.getName(),obj);
        }else{
            throw new ReloadFailException("리로딩 가능한 객체가 아닙니다.");
        }
    }
    
    public void remove(String key){
        keyPool.remove(key);
        cachePool.remove(key);         
    }
    
    public void remove(Cache cache){
        keyPool.remove(cache.getName());
        cachePool.remove(cache.getName());         
    }

	public String toString() {
		return "캐쉬된  객체목록\n"
				+ StringUtil.replace(StringUtil.replaceAll(
						cachePool.toString(), ",", "\n--->"), "{", "--->");
	}
	
	/**
	 * 캐쉬에서 key값에 해당하는 데이타가 존재한다면 true,
	 * 없다면 false를 리턴한다.
	 * 
	 * @param key
	 * @return true or false
	 */
	public boolean isCache(String key)
	{
		if(get(key) != null)
		{
			return true;
		}
		return false; 
	}	
    
    public static void main(String[] args){
        Cache cache = new Cache("test");
        cache.setData("tetete");
        CacheManager.getInstance().put(cache);
        CacheManager.getInstance().put("c1",cache);
        CacheManager.getInstance().put(new Cache("12345"),"test");
        
        System.out.println("-1-"+CacheManager.getInstance().get("test"));
        System.out.println("-2-"+CacheManager.getInstance().get(new Cache("test")));
        System.out.println("--"+CacheManager.getInstance().get("c1"));
        System.out.println("=="+CacheManager.getInstance().get("12345"));
        CacheManager.getInstance().reloadAll();
    }
}

