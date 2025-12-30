/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import nebsoa.common.Constants;
import nebsoa.common.exception.ParamException;
import nebsoa.common.exception.PropertyException;
import nebsoa.common.jdbc.DBConfig;
import nebsoa.common.log.LogManager;
import nebsoa.common.property.PropertyItem;
import nebsoa.common.property.PropertyLoader;
import nebsoa.common.property.XmlPropertyLoader;
import nebsoa.common.startup.StartupContext;

/*******************************************************************
 * <pre>
 * 1.설명 
 * <code>PropertyManager</code>는 공통모듈과 Client 모듈이 사용하는 Property를 관리합니다.
 * <code>PropertyManager</code>는
 * 
 * <code>PropertyManager</code>의 getProperty("key이름")을 이용하여 본인이 설정한
 * Property값을 얻을 수 있습니다. 
 * 
 * <code>PropertyManager</code>의 Property파일의 이름은 properties입니다.
 * 
 * 2.사용법
 * properties파일에 Property를 지정하는 방법은 다음과 같이 사용합니다.
 * <blockquote>
 *  String loglevel = PropertyManager.getProperty(&quot;log&quot;,&quot;loglevel&quot;);
 * 
 *  배열로 받고 싶은 경우
 *  String[] colors = PropertyManager.getPropertyArray(&quot;myprop&quot;,&quot;mycolors&quot;);
 *  getStringArray(String key)
 * 
 *  해당 프로퍼티 파일(bcu/properties/log.properties)이 없거나 파일에 프로퍼티 값이 없으면 propertyException이 발생합니다.
 *  PropertyException이 발행하지 않게 하려면 해당 값이 없을때 사용할 default 값을 지정하면 됩니다.
 *  String loglevel = PropertyManager.getProperty(&quot;log&quot;,&quot;loglevel&quot;,&quot;DEBUG&quot;);
 *  String[] colors = PropertyManager.getPropertyArray(&quot;log&quot;,&quot;loglevel&quot;,new String[]{&quot;red&quot;,&quot;blue&quot;});
 * </blockquote>
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
 * $Log: PropertyManager.java,v $
 * Revision 1.1  2018/01/15 03:39:49  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:31  cvs
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
 * Revision 1.3  2008/10/29 05:43:17  김성균
 * 원격호출시 삭제안되는 문제 수정
 *
 * Revision 1.2  2008/10/23 08:42:22  김성균
 * 원격WAS 수정 안되는 버그 수정
 *
 * Revision 1.1  2008/08/04 08:54:50  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.2  2008/03/19 08:05:51  김승희
 * 변경, 수정, 삭제된 프로퍼티를 여러건 메모리에 반영하고 한번만 파일에 반영하는 메소드 추가
 *
 * Revision 1.1  2008/01/22 05:58:17  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:02  안경아
 * *** empty log message ***
 *
 * Revision 1.24  2007/01/22 14:11:24  이종원
 * getBooleanProperty 로직 정리
 *
 * Revision 1.23  2007/01/17 07:32:33  김성균
 * setProperty 인자의 데이타 타입 수정
 *
 * Revision 1.22  2006/12/19 03:33:44  이종원
 * getInstanceProperty추가
 *
 * Revision 1.21  2006/12/13 08:58:37  안경아
 * *** empty log message ***
 *
 * Revision 1.20  2006/12/13 06:11:46  안경아
 * *** empty log message ***
 *
 * Revision 1.19  2006/12/11 10:19:53  이종원
 * 다중 속성 관리 가능하게 수정
 *
 * Revision 1.18  2006/12/05 05:50:18  이종원
 * getPropertyItem 추가
 *
 * Revision 1.17  2006/11/28 08:36:15  안경아
 * *** empty log message ***
 *
 * Revision 1.16  2006/11/08 08:18:43  김성균
 * *** empty log message ***
 *
 * Revision 1.15  2006/10/21 11:20:10  이종원
 * AUTO RELOADER삭제
 *
 * Revision 1.14  2006/10/20 06:49:47  이종원
 * AutoReloader
 *
 * Revision 1.13  2006/09/09 08:21:32  이종원
 * getLongProperty추가
 *
 * Revision 1.12  2006/07/25 10:03:30  김성균
 * XmlPropertyLoader만 사용하도록 수정
 *
 * Revision 1.11  2006/07/05 04:10:27  이종원
 * *** empty log message ***
 *
 * Revision 1.10  2006/06/17 10:37:45  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class PropertyManager {
    
    static boolean autoReloadMode = false;

    static boolean stopAutoReloader = false;

    static int autoReloadTerm = 60;
    
    /**
     * 프로퍼티값을 캐쉬 해 놓기 위한 Hashtable입니다. 
     * 변경된 값을 리로딩 하려면 reload() or reload(propertyGroupId)을 사용 하세요.
     */
    private static HashMap propertyHash = new HashMap();
    
//    static class Reloader implements Runnable {
//        public void run() {
//            System.out.println(">>>>>>Property AutoReloader started...");
//            while (!stopAutoReloader) {
//                try {
//                    Thread.sleep(autoReloadTerm<1000? autoReloadTerm*1000:autoReloadTerm);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                System.out.println(">>>>>Property AutoReloader running term:" +autoReloadTerm );
//                if (autoReloadMode)
//                    PropertyManager.reload();
//                autoReloadMode = PropertyManager.getBooleanProperty("default",
//                        "PROPERTY.AUTO_RELOAD_MODE", "ON");
//                stopAutoReloader = PropertyManager.getBooleanProperty(
//                        "default", "PROPERTY.STOP_AUTO_RELOADER_DAEMON",
//                        "FALSE");
//                autoReloadTerm = PropertyManager.getIntProperty("default",
//                        "PROPERTY.AUTO_RELOAD_TERM", 60);
//            }
//            System.out.println("Property AutoReloader stopped...");
//        }
//    }
//    ///////////////////////////////////////////////////////////////////
//    static {
//        initialize();
//        initialized=true;
//    }
//
//    static boolean initialized=false;
//    
//    static void initialize() {
//    	autoReloadMode = PropertyManager.getBooleanProperty("default","PROPERTY.AUTO_RELOAD_MODE","TRUE");
//        initialized=true;
//        if (autoReloadMode == true) {
//            
//        	Reloader reloader =  new Reloader();
//        	ThreadPool pool = ThreadPoolManager.getInstance().getPool("PROPERTY",0);
//            try {
//                WorkerThread thread = (WorkerThread) pool.borrowObject();
//                thread.execute(reloader);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }            
//        }
//    }
    
    ////////////////////////////////////////////////////////////////////
    /**
     * 해당 프로퍼티그룹에서 해당 프로퍼티값을 얻어 옵니다 .
     * 해당 프로퍼티가 없을때는 PropertyException을 발생 시킵니다.
     */
    public static String getProperty(String propertyGroupId, String propertyId)
            throws PropertyException {
        
        String propertyValue = getProperty(propertyGroupId, propertyId, null);
        if (propertyValue == null) {
//            LogManager.error(propertyGroupId + "에서 " + propertyId + "찾기 실패");
            System.out.println(propertyGroupId + "에서 " + propertyId + "찾기 실패");
            throw new PropertyException(propertyGroupId, propertyId);
        }
        return propertyValue;
    }

    /**
     * boolean형태로 프로퍼티 값을 얻어 온다.
     * 
     * @param propertyGroupId
     * @param propertyId
     * @param defaultValue
     * @return ON , on, TRUE, true 일때 true가 리턴 된다.
     */
    public static boolean getBooleanProperty(String propertyGroupId,
            String propertyId, String defaultValue) {
        String value = getProperty(propertyGroupId, propertyId, defaultValue);

        if (value == null) value=defaultValue;
        if(value==null) value="false";
        if(value.equalsIgnoreCase("true") || value.equalsIgnoreCase("on") || 
                value.equalsIgnoreCase("y") ||value.equalsIgnoreCase("t")||
                value.equalsIgnoreCase("yes"))
            return true;  
        return false;
    }

    /**
     * boolean형태로 프로퍼티 값을 얻어 온다.
     * 
     * @param propertyGroupId
     * @param propertyId
     * @return ON , on, TRUE, true 일때 true가 리턴 된다.
     */
    public static boolean getBooleanProperty(String propertyGroupId,
            String propertyId) {
        return getBooleanProperty(propertyGroupId, propertyId, null);
    }

    /**
     * 해당 프로퍼티파일에서 해당 프로퍼티값을 얻어 옵니다 .
     * 해당 프로퍼티가 없을때는 지정한 Default값을 return합니다.
     */
    public static String getProperty(String propertyGroupId, String propertyId,
            String defaultValue) {
        PropertyLoader prop = null;
        
        prop = getPropertyLoader(propertyGroupId);

        String value = prop.getProperty(propertyId);
        if (value == null || value.length() == 0) {
            value = defaultValue;
        }

        if (!StringUtil.isNull(value) && value.indexOf("${APP_HOME}") > -1) {
            value = StringUtil.replaceAll(value, "${APP_HOME}", Constants.APP_HOME_DIR);
        }
        
        if (!StringUtil.isNull(value) && value.indexOf("${SPIDER_INSTANCE_ID}") > -1) {
            value = StringUtil.replaceAll(value, "${SPIDER_INSTANCE_ID}", StartupContext.getInstanceId());
        }
        return value;
    }
    
    /**
     * 해당 프로퍼티파일에서 해당 프로퍼티값을 얻어 옵니다 .
     * 해당 프로퍼티가 없을때는 지정한 Default값을 return합니다.
     */
    public static PropertyItem getPropertyItem(String propertyGroupId, String propertyId){

        PropertyLoader prop = getPropertyLoader(propertyGroupId);

        PropertyItem item = prop.getPropertyItem(propertyId);
        if (item == null){
            throw new PropertyException(propertyGroupId,propertyId);
        }
        return item;
    }

    public static int getIntProperty(String propertyGroupId, String propertyId)
            throws PropertyException {
        String propertyValue = getProperty(propertyGroupId, propertyId, null);
        if (propertyValue == null) {
//            LogManager.error(propertyGroupId + "에서 " + propertyId + "찾기 실패");
            System.out.println(propertyGroupId + "에서 " + propertyId + "찾기 실패");
            throw new PropertyException(propertyGroupId, propertyId);
        } else {
            try {
                return Integer.parseInt(propertyValue);
            } catch (NumberFormatException e) {
//                LogManager.error(propertyGroupId + "에서 " + propertyId + "을 찾았으나 int형으로 변환할 수 없습니다.");
                System.out.println(propertyGroupId + "에서 " + propertyId + "을 찾았으나 int형으로 변환할 수 없습니다.");
                throw new PropertyException(propertyGroupId, propertyId);
            }
        }
    }

    public static int getIntProperty(String propertyGroupId, String propertyId, 
            int defaultValue) {
        PropertyLoader prop = getPropertyLoader(propertyGroupId);
        return prop.getIntProperty(propertyId, defaultValue);
    }
    
    public static long getLongProperty(String propertyGroupId, String propertyId)
    throws PropertyException {
        String propertyValue = getProperty(propertyGroupId, propertyId, null);
        if (propertyValue == null) {
//    LogManager.error(propertyGroupId + "에서 " + propertyId + "찾기 실패");
            System.out.println(propertyGroupId + "에서 " + propertyId + "찾기 실패");
            throw new PropertyException(propertyGroupId, propertyId);
        } else {
            try {
                return Long.parseLong(propertyValue);
            } catch (NumberFormatException e) {
                //        LogManager.error(propertyGroupId + "에서 " + propertyId + "을 찾았으나 int형으로 변환할 수 없습니다.");
                System.out.println(propertyGroupId + "에서 " + propertyId + "을 찾았으나 int형으로 변환할 수 없습니다.");
                throw e;
            }
        }
    }
    
    public static long getLongProperty(String propertyGroupId, String propertyId, 
            long defaultValue) {
        PropertyLoader prop = getPropertyLoader(propertyGroupId);
        return prop.getLongProperty(propertyId, defaultValue);
    }

    public static double getDoubleProperty(String propertyGroupId,
            String propertyId) throws PropertyException {
        String propertyValue = getProperty(propertyGroupId, propertyId, null);
        if (propertyValue == null) {
            System.out.println(propertyGroupId + "에서 " + propertyId + "찾기 실패");
            throw new PropertyException(propertyGroupId, propertyId);
        } else {
            try {
                return Double.parseDouble(propertyValue);
            } catch (NumberFormatException e) {
                System.out.println(propertyGroupId + "에서 " + propertyId + "을 찾았으나 long형으로 변환할 수 없습니다.");
                throw e;
            }
        }
    }

    public static double getDoubleProperty(String propertyGroupId,
            String propertyId, double defaultValue) {
        PropertyLoader prop = getPropertyLoader(propertyGroupId);
        return prop.getDobuleProperty(propertyId, defaultValue);
    }

    /**
     * 해당 인스턴스의 프로퍼티파일에서 해당 프로퍼티값을 얻어 옵니다 .
     * 해당 인스턴스의 프로퍼티가 없을때는 지정한 Default값을 return합니다.
     */
    public static String getInstanceProperty(String propertyId,
            String defaultValue) {
        String propertyGroupId = "was_"+StartupContext.getInstanceId();
        return getProperty(propertyGroupId,propertyId,defaultValue);
    }
    
    /**
     * 해당 인스턴스의 프로퍼티파일에서 해당 프로퍼티값을 얻어 옵니다 .
     * 해당 인스턴스의 프로퍼티가 없을때는 PropertyException을 발생 시킵니다.
     */
    public static String getInstanceProperty(String propertyId) {
        String propertyGroupId = "was_"+StartupContext.getInstanceId();
        return getProperty(propertyGroupId,propertyId);
    }
    
    /**
     * 해당 프로퍼티파일에서 해당 프로퍼티값을 배열로 얻어 옵니다.
     * 배열로 만들때 구분자를 인자값으로 지정합니다.
     * 해당 프로퍼티가 없을때는 PropertyException을 발생 시킵니다.
     */
    public static String[] getProperties(String propertyGroupId,
            String propertyId, String del) throws PropertyException {
        String prop = getProperty(propertyGroupId, propertyId);
        return StringUtil.toArray(prop, del);
    }

    /**
     * 해당 프로퍼티파일에서 해당 프로퍼티값을 배열로 얻어 옵니다.
     * 배열로 만들때 구분자를 지정 안하므로 공백문자를 기준으로 잘라 옵니다.
     * 해당 프로퍼티가 없을때는 PropertyException을 발생 시킵니다.
     */
    public static String[] getProperties(String propertyGroupId,
            String propertyId) throws PropertyException {
        return getProperties(propertyGroupId, propertyId, " ");
    }

    /**
     * 해당 프로퍼티파일에서 해당 프로퍼티값을 배열로 얻어 옵니다.
     * 배열로 만들때 지정된 구분자를 사용하여 잘라 옵니다. 
     * 해당 프로퍼티가 없을때는 지정된 default값을 return.
     */
    public static String[] getProperties(String propertyGroupId,
            String propertyId, String del, String[] defaultValues) {
        try {
            return getProperties(propertyGroupId, propertyId, del);
        } catch (Exception e) {
            return defaultValues;
        }
    }

    /**
     * 해당 프로퍼티파일에서 해당 프로퍼티값을 배열로 얻어 옵니다 .
     * 배열로 만들때 구분자를 지정 안하므로 공백문자를 기준으로 잘라 옵니다.
     * 해당 프로퍼티가 없을때는 지정된 default값을 return.
     */
    public static String[] getProperties(String propertyGroupId,
            String propertyId, String[] defaultValues) {
        try {
            return getProperties(propertyGroupId, propertyId, " ");
        } catch (Exception e) {
            return defaultValues;
        }
    }

    /**
     * 모든 프로퍼티를 clear하고 강제 리로딩 합니다
     */
    public static void reset() {
        propertyHash = new HashMap();
    }

    /**
     * 모든 프로퍼티를 리로딩 합니다.
     */
    public static void reload() {
        Set set = propertyHash.keySet();
        if (set.isEmpty())
            return;
        Iterator i = set.iterator();
        while (i.hasNext()) {
            reload((String) i.next());
        }
    }

    /**
     * 해당 프로퍼티만을 리로딩 합니다
     */
    public static void reload(String propertyGroupId) throws PropertyException {
        if (propertyGroupId == null) {
            throw new PropertyException(propertyGroupId);
        }

        PropertyLoader prop = (PropertyLoader) propertyHash.get(propertyGroupId);
        
        // 캐쉬된 프로퍼티가 존재하면  리로딩 한다.
        if (prop == null) {
            prop = getInstancePropertyLoader(propertyGroupId);
        } else {
            if (prop.isUsePropertyCache() == true) {
                prop.reload();
            }
            if (propertyGroupId.indexOf("db") > -1) {
                DBConfig.reloadAll();
            }
        }
    }

    public static void store(String propertyGroupId) throws PropertyException {
        if (propertyGroupId == null) {
            throw new PropertyException(propertyGroupId);
        }
        PropertyLoader prop = (PropertyLoader) propertyHash.get(propertyGroupId);
        prop.store();
    }

    /**
     * 
     * 2004. 8. 26. 이종원 작성
     * 
     * @param propertyGroupId
     * @param key
     * @param value
     * @param desc
     * @return 설명: update or create new Property key, value, description
     */
    public static Object setProperty(String propertyGroupId, String key,
            String value, String desc) {
        return setProperty(propertyGroupId, key, value, desc, true);
    }

    public static Object setProperty(String propertyGroupId, String key,
            String value) {
        return setProperty(propertyGroupId, key, value, null);
    }

    public static Object setProperty(String propertyGroupId, String key,
            String value, String desc, boolean isSave) {
        //PropertyLoader prop = getPropertyLoader(propertyGroupId);
        Hashtable map = new Hashtable();
        map.put("key",key);
        map.put("value",value);
        if(desc != null) map.put("desc",desc);
        return setProperty(propertyGroupId, key, map, isSave);
        //return prop.setProperty(key, value, desc, isSave);
    }
    
    public static Object setProperty(String propertyGroupId, String key, Map data, boolean isSave) {
        if(data==null || data.isEmpty()) return null;
        data.remove("command");
        PropertyLoader prop = getPropertyLoader(propertyGroupId);
        PropertyItem item = prop.getPropertyItem(key);
        if(item ==null) {
            item = new PropertyItem();
            prop.getPropertyCache().put(key,item);
        }
        item.clear();
        item.putAll(data);
        LogManager.debug("data ==>"+data);
        LogManager.debug("add property ==>"+item);
        prop.store();
        return item;
    }
    
    public static Object setProperty(String propertyGroupId, Map data, boolean isSave) {
        if(data==null || data.isEmpty()) return null;
        String key = (String) data.get("key");
        if(key==null){
            throw new ParamException("key");
        }
        
        return setProperty(propertyGroupId,key, data, isSave);
    }
    
    public static Object setProperty(String propertyGroupId, Map data) {
        return setProperty(propertyGroupId, data, true);
    }
    
    public static Object setProperty(String propertyGroupId, String key, Map data) {
        return setProperty(propertyGroupId, key, data, true);
    }

    public static Object setProperty(String propertyGroupId, String key,
            String value, boolean isSave) {
        return setProperty(propertyGroupId, key, value, null, isSave);
    }

    /**
     * remove the Property with key
     * @param propertyGroupId
     * @param key
     */
    public static void removeProperty(String propertyGroupId, String key) {
        PropertyLoader prop = getPropertyLoader(propertyGroupId);
        prop.removeProperty(key);
    }
    
    public static PropertyLoader getPropertyLoader(String propertyGroupId) {
        PropertyLoader prop = (PropertyLoader) propertyHash.get(propertyGroupId);
        if (prop == null || prop.isUsePropertyCache() == false) {
            prop = getInstancePropertyLoader(propertyGroupId);
            propertyHash.put(propertyGroupId, prop);
        }
        return prop;
    }
    
    /**
     * PropertyLoader 객체를 얻는다.
     * @param 	propertyGroupId	프로퍼티그룹ID
     * @return	PropertyLoader 객체
     */
    public static PropertyLoader getInstancePropertyLoader(String propertyGroupId) {
        PropertyLoader prop = null;
        prop = new XmlPropertyLoader(propertyGroupId);
        /*
        String propertyType = StartupContext.getPropertyType();
        if (propertyType.equalsIgnoreCase("DB")) {
            prop = new DBPropertyLoader(propertyGroupId);
        } else if (propertyType.equalsIgnoreCase("XML")) {
            prop = new XmlPropertyLoader(propertyGroupId);
        }
        */
        return prop;
    }

	public static void modifyProperties(String propertyGroupId, ArrayList propertyMapList) {
		if(propertyMapList==null || propertyMapList.isEmpty()) return;
        
		PropertyLoader prop = getPropertyLoader(propertyGroupId);
        PropertyItem item = null;
        HashMap propertyMap = null;
        String key = null; 
        String command = null;
        boolean isStoreNeeded = false;
        
        for(int i=0; i<propertyMapList.size(); i++){
        	propertyMap = (HashMap)propertyMapList.get(i);
        	command = (String)propertyMap.get("command");
        	key = (String)propertyMap.get("key");
        	propertyMap.remove("command");
        	
        	//추가 또는 수정일 때
        	if("insert".equals(command) || "update".equals(command)){
            	item = prop.getPropertyItem(key);
                if(item ==null) {
                    item = new PropertyItem();
                    prop.getPropertyCache().put(key,item);
                }
                item.clear();
                item.putAll(propertyMap);
                LogManager.debug("add or modify property ==>"+item);
                isStoreNeeded = true;
                
            //삭제일 때    
        	}else if("remove".equals(command)){
        		prop.getPropertyCache().remove(key);
        		prop.delete(key);
        		LogManager.debug("delete property ==>"+key);
        		isStoreNeeded = true;
        	}else{
        		LogManager.info("command 값이 없습니다.(insert, update, remove 중 하나가 map에 담겨 있어야 합니다.");
        	}
        	
        	// 원격호출시에 로컬WAS에서 먼저  수행되면 다음 WAS에서 처리가 안되는 문제 때문에 다시 넣어 준다.
        	propertyMap.put("command", command);
        }
        
        if(isStoreNeeded) prop.store();
        
	}

}
