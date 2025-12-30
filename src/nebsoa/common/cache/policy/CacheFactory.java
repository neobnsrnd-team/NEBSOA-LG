/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.cache.policy;

import java.util.HashMap;

import nebsoa.common.exception.SysException;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 캐쉬객체를 생성하는 Factory
 * 
 * 
 * 2.사용법
 * LfuCache cache = CacheFactory.getInstance().getLfuCache("test",timeout,size);
 * cache.put("xxx","yyy"); 
 * LruCache cache2 = CacheFactory.getInstance().getLruCache("test2",timeout,size);
 * cache2.put("xxx","yyy"); 
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author $Author: cvs $
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 
 * $Log: CacheFactory.java,v $
 * Revision 1.1  2018/01/15 03:39:52  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:16  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:26  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:28  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:53  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:28  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:37:43  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2007/07/17 15:58:23  이종원
 * 최초작성
 *
 * Revision 1.5  2006/06/19 04:10:16  안경아
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class CacheFactory extends HashMap{
    
    /**
     * 싱글톤을 위한 인스턴스 변수
     */
    private static CacheFactory instance;

    static {
        instance = new CacheFactory();
    }

    /**
     * @return  CacheMap 인스턴스
     */
    public static CacheFactory getInstance() {
        if (instance == null) {
            instance = new CacheFactory();
        }
        return instance;
    }
    
    private CacheFactory(){
        
    }
    
    public synchronized LRUCache  getLruCache(String name, long timeout, int size){
        Object obj = get("LRU:"+name);
        if(obj != null){
            if(obj instanceof LRUCache) {
                return (LRUCache)obj;
            }else{
                throw new SysException("Object is not LfuCache Type but "
                        +obj.getClass().getName());
            }
        }else{
            LRUCache cache = new LRUCache(name, timeout, size);
            put("LRU:"+name, cache);
            return cache;
        }
    }
    
    public synchronized LFUCache  getLfuCache(String name, long timeout, int size){
        Object obj = get("LFU:"+name);
        if(obj != null){
            if(obj instanceof LFUCache) {
                return (LFUCache)obj;
            }else{
                throw new SysException("Object is not LFUCache Type but "
                        +obj.getClass().getName());
            }
        }else{
            LFUCache cache = new LFUCache(name, timeout, size);
            put("LFU:"+name, cache);
            return cache;
        }
    }
    
    public synchronized FIFOCache  getFifoCache(String name, long timeout, int size){
        Object obj = get("FIFO:"+name);
        if(obj != null){
            if(obj instanceof FIFOCache) {
                return (FIFOCache)obj;
            }else{
                throw new SysException("Object is not FIFOCache Type but "
                        +obj.getClass().getName());
            }
        }else{
            FIFOCache cache = new FIFOCache(name, timeout, size);
            put("FIFO:"+name, cache);
            return cache;
        }
    }
    
    public static void main(String[] args) {
        LRUCache cache = CacheFactory.getInstance().getLruCache("test",100000,2);
        cache.addObject("aaa","AAA");
        System.out.println(cache.getObject("aaa"));
        cache.addObject("bbb","BBB");
        cache.addObject("ccc","CCC");
        System.out.println(cache.getObject("aaa"));
        System.out.println(cache.getObject("bbb"));
        
        System.out.println("===========LFU =====================");
        
        LFUCache cache2 = CacheFactory.getInstance().getLfuCache("test2",100000,2);
        cache2.addObject("aaa","AAA");
        System.out.println(cache2.getObject("aaa"));
        cache2.addObject("bbb","BBB");
        System.out.println(cache2.getObject("aaa"));
        System.out.println(cache2.getObject("bbb"));
        System.out.println(cache2.getObject("aaa")); //aaa를 많이 사용하니까 bbb가 먼저 없어 진다.
        cache2.addObject("ccc","CCC");
        System.out.println(cache2.getObject("aaa"));
        System.out.println(cache2.getObject("bbb"));
        System.out.println(cache2.getObject("ccc"));
        
        
        System.out.println("=========== FIFO =====================");
       
        FIFOCache cache3 = CacheFactory.getInstance().getFifoCache("test3",100000,2);
        cache3.addObject("aaa","AAA");
        System.out.println(cache3.getObject("aaa"));
        cache3.addObject("bbb","BBB");
        cache3.addObject("ccc","CCC");
        System.out.println(cache3.getObject("aaa"));
        System.out.println(cache3.getObject("bbb"));
        
    }
}
