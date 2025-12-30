/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.property;

import java.util.Map;
import java.util.TreeMap;

import nebsoa.common.exception.SysException;


/*******************************************************************
 * <pre>
 * 1.설명 
 * 프로퍼티를 로딩하기 위한 최상위 클래스
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
 * $Log: PropertyLoader.java,v $
 * Revision 1.1  2018/01/15 03:39:52  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:15  cvs
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
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:35  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:23  안경아
 * *** empty log message ***
 *
 * Revision 1.7  2006/12/05 05:50:45  이종원
 * PropertyItem에 cacheYn추가
 *
 * Revision 1.6  2006/09/09 08:22:30  이종원
 * getLongProperty추가
 *
 * Revision 1.5  2006/06/17 10:37:45  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public abstract class PropertyLoader {
	
    /**
     * 프로퍼티 그룹 아이디
     */
    protected String propertyGroupId;
    
	/**
	 * 캐쉬하고 있는 프로퍼티
	 */
	protected Map propertyCache;
    
	/**
	 * 프로퍼티 캐쉬 여부
	 */
    protected boolean usePropertyCache = true;
	
	/**
	 * 주어진 이름에 해당하는 프로퍼티 로더를 생성합니다.
	 * 
	 * @param propertyGroupId 프로퍼티 그룹 아이디
	 */
	public PropertyLoader(String propertyGroupId) {
		this.propertyGroupId = propertyGroupId;
		this.propertyCache = new TreeMap();
	}
    /**
     * 주어진 프로퍼티 키에 해당하는 프로퍼티 Item객체를 얻어 낸다.
     * 해당 프로퍼티가 존재하지 않을 경우, exception을 리턴합니다.
     * 
     * @param key 프로퍼티 키
     * @return PropertyItem
     */
    public PropertyItem getPropertyItem(String key) {
        PropertyItem propertyItem;
        propertyItem = (PropertyItem) this.propertyCache.get(key);
        return propertyItem;
    }
	
	/**
	 * 주어진 프로퍼티 키에 해당하는 프로퍼티 문자열 값을 리턴합니다.
	 * 해당 프로퍼티 키가 존재하지 않을 경우, 기본 값을 리턴합니다.
	 * 
	 * @param key 프로퍼티 키
	 * @param defaultValue 기본 프로퍼티 값
	 * @return 프로퍼티 값
	 */
    public String getProperty(String key, String defaultValue) {
    	PropertyItem item =(PropertyItem)this.propertyCache.get(key);
    	if (item == null) {
    		return defaultValue;
    	} else {
    		String value = item.getValue();
    		if (value==null || value.length() == 0) {
    			return null;
    		} else {
    			return value;
    		}//end if else
    	}//end if else
    }//end of getProperty()
    
 


    /**
     * 주어진 프로퍼티 키에 해당하는 프로퍼티 long 형 값을 리턴합니다.
     * 해당 프로퍼티 키가 존재하지 않을 경우, 기본 값을 리턴합니다.
     * 
     * @param key 프로퍼티 키
     * @param defaultValue 기본 프로퍼티 값
     * @return 프로퍼티 값
     */
    public long getLongProperty(String key, long defaultValue) {
        PropertyItem item = (PropertyItem)this.propertyCache.get(key);
        if (item == null) {
            return defaultValue;
        } else {
            try {
                return Long.parseLong(item.getValue());
            } catch (NumberFormatException e) {
                throw new SysException("FRM00004","NumberFormatException:Property:[" + key + "],value: ["+item.getValue()+"]");
            }//end try catch
        }//end if else
    }//end of getLongProperty()
    
    /**
	 * 주어진 프로퍼티 키에 해당하는 프로퍼티 int 형 값을 리턴합니다.
	 * 해당 프로퍼티 키가 존재하지 않을 경우, 기본 값을 리턴합니다.
     * 
	 * @param key 프로퍼티 키
	 * @param defaultValue 기본 프로퍼티 값
	 * @return 프로퍼티 값
     */
    public int getIntProperty(String key, int defaultValue) {
    	PropertyItem item = (PropertyItem)this.propertyCache.get(key);
    	if (item == null) {
    		return defaultValue;
    	} else {
    	    try {
    	        return Integer.parseInt(item.getValue());
    	    } catch (NumberFormatException e) {
    	        throw new SysException("FRM00004","NumberFormatException:Property:[" + key + "],value: ["+item.getValue()+"]");
    	    }//end try catch
    	}//end if else
    }//end of getIntProperty()
    
    /**
	 * 주어진 프로퍼티 키에 해당하는 프로퍼티 double 형 값을 리턴합니다.
	 * 해당 프로퍼티 키가 존재하지 않을 경우, 기본 값을 리턴합니다.
     * 
	 * @param key 프로퍼티 키
	 * @param defaultValue 기본 프로퍼티 값
	 * @return 프로퍼티 값
     */
    public double getDobuleProperty(String key, double defaultValue) {
    	PropertyItem item = (PropertyItem)this.propertyCache.get(key);
    	if (item == null) {
    		return defaultValue;
    	} else {
    	    try {
    	        return Double.parseDouble(item.getValue());
    	    } catch (NumberFormatException e) {
    	        throw new SysException("ProprtyLoader.getDobuleProperty 에서 오류 발생 - " + key + " 로 얻어 온 값을 double 형으로 변환 할  수 없습니다.");
    	    }//end try catch
    	}//end if else
    }//end of getDoubleProperty()

    public String getProperty(String key) {
        return getProperty(key, null);
    }

    public Object setProperty(String key, String value) {
        return setProperty(key, value, null);
    }

    public Object setProperty(String key, String value, boolean isSave) {
        return setProperty(key, value, null, isSave);
    }

    public Object setProperty(String key, String value, String desc) {
        return setProperty(key, value, desc, true);
    }
    
    /**
     * 프로퍼티를 항목을 캐쉬하고 저장한다.
     * @param key 
     * @param value
     * @param desc
     * @param isSave
     * @return
     */
    public Object setProperty(String key, String value, String desc,
            boolean isSave) {
        if (key == null) {
            throw new SysException("설정 할 속성 key 값이 전달 되지 않았습니다.");
        }
        
        if (value == null || value.equals("null")) {
            value = "";
        }
        
        PropertyItem item = (PropertyItem) this.propertyCache.get(key);
        
        if (item == null) {
            item = new PropertyItem(key, value, desc);
            this.propertyCache.put(key, item);
            // FIXME: XmlPropertyLoader인 경우 현재 아무것도 하지 않는다.
            if (isSave) insert(item);
        } else {
            item.setKey(key);
            item.setValue(value);
            item.setDesc(desc);
        }

        if (isSave) {
            store();
        }

        return item;
    }
    
    public void removeProperty(String key) {
        propertyCache.remove(key);
        delete(key);
        store();
    }
    
    /**
     * usePropertyCache 의 값을 리턴합니다.
     * 
     * @return usePropertyCache 의 값
     */
    public boolean isUsePropertyCache() {
        return usePropertyCache;
    }

    /**
     * usePropertyCache 에 값을 세팅합니다.
     * 
     * @param usePropertyCache usePropertyCache 에 값을 세팅하기 위한 인자 값
     */
    public void setUsePropertyCache(boolean usePropertyCache) {
        this.usePropertyCache = usePropertyCache;
    }
    
    /**
     * @return Returns the propertyCache.
     */
    public Map getPropertyCache() {
        return propertyCache;
    }

    /**
     * @param propertyCache The propertyCache to set.
     */
    public void setPropertyCache(Map propertyCache) {
        this.propertyCache = propertyCache;
    }

	/**
	 * 프로퍼티를 리로드 합니다.
	 *
	 */
    public void reload() {
        load();
    }
	
	/**
	 * 프로퍼티를 로드합니다.
	 *
	 */
	public abstract void load();
	
	/**
	 * 프로퍼티를 저장합니다.
	 *
	 */
	public abstract void store();
    
	/**
	 * 프로퍼티를 수정합니다.
	 *
	 */
	public void insert(PropertyItem propertyItem) {}
    
	/**
	 * 프로퍼티를 삭제합니다.
	 *
	 */
	public void delete(String key) {}
    
}// end of PropertyLoader.java
