/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.property;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 프로퍼티의 값을 포함하고 있는 클래스
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
 * $Log: PropertyItem.java,v $
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
 * Revision 1.1  2008/01/22 05:58:34  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:22  안경아
 * *** empty log message ***
 *
 * Revision 1.8  2007/07/30 08:36:14  김성균
 * *** empty log message ***
 *
 * Revision 1.7  2006/12/11 09:19:34  이종원
 * PropertyItem을 Hashtable로 수정
 *
 * Revision 1.6  2006/12/11 09:19:18  이종원
 * PropertyItem을 Hashtable로 수정
 *
 * Revision 1.5  2006/12/11 08:11:09  이종원
 * PropertyItem을 Hashtable로 수정
 *
 * Revision 1.4  2006/12/05 05:50:45  이종원
 * PropertyItem에 cacheYn추가
 *
 * Revision 1.3  2006/06/17 10:37:45  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class PropertyItem extends Hashtable {

    
    public PropertyItem() {

    }
    
    public PropertyItem(String key, String value, String desc) {
        put("key",key);
        put("value",value);
        put("desc",desc);
    }

    /**
     * desc 의 값을 set.
     */
    public void setDesc(String desc) {
        put("desc",desc);
    }
    
    /**
     * key 의 값을 set.
     */
    public void setKey(String key) {
        put("key",key);
    }
    
    /**
     * value 의 값을 set.
     */
    public void setValue(String value) {
        put("value",value);
    }
    
    /**
     * isCache 의 값을 리턴합니다.
     * 
     * @return key 의 값
     */
    public void setCacheYn(boolean isCache) {
        put("cache_yn",isCache?"true":"false");
    }


    /**
     * desc 의 값을 리턴합니다.
     * 
     * @return desc 의 값
     */
    public String getDesc() {
        return  (String) get("desc");
    }



    /**
     * key 의 값을 리턴합니다.
     * 
     * @return key 의 값
     */
    public String getKey() {
        return (String) get("key");
    }
    


    /**
     * value 의 값을 리턴합니다.
     * 
     * @return value 의 값
     */
    public String getValue() {
        return (String) (get("value")==null?"":get("value"));
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        //System.out.println(toXml());
        return "PropertyItem::"+super.toString();
    }

    /**
     */
    public String toXml() {
        Set set = keySet();
        Iterator i = set.iterator();
        String name = null;
        String value = null;
        StringBuffer buf = new StringBuffer("\n\t<Property");
        int x = 0;
        while (i.hasNext()) {
            name = null;
            value = null;
            name = (String) i.next();
            if (name == null)
                continue;
            value = (String) this.get(name);
            if (value == null)
                value = "";
            if (x++ == 0) {
                buf.append(" " + name + "=\"" + value + "\"");
            } else {
                buf.append("\r\n\t\t " + name + "=\"" + value + "\"");
            }
        }
        buf.append(" />");
        return buf.toString();
    }
    
    /**
     * name,value, desc를 제외한 데이터 폼 html생성
     */
    public String toEtcHtmlForm() {
        Set set = keySet();
        Iterator i = set.iterator();
        String name = null;
        String value = null;
        StringBuffer buf = new StringBuffer("");
        while (i.hasNext()) {
            name = null;
            value = null;
            name = (String) i.next();
            if (name == null)
                continue;
            if (name.equals("key") || name.equals("value")
                    || name.equals("desc"))
                continue;
            value = (String) this.get(name);
            if (value == null)
                value = "";
            buf.append("\r\n\t <br>"+name+" : <input name='"+name+"' value='"+value+"' />");
        }
        return buf.toString();
    }
    
    /**
     * name,value, desc 포함한 데이터 폼 출력 html생성
     */
    public String toHtmlForm() {
        Set set = keySet();
        Iterator i = set.iterator();
        String name = null;
        String value = null;
        StringBuffer buf = new StringBuffer("");
        while (i.hasNext()) {
            name = null;
            value = null;
            name = (String) i.next();
            if (name == null)
                continue;
            value = (String) this.get(name);
            if (value == null)
                value = "";
            buf.append("\r\n\t <br>"+name+" : <input name='"+name+"' value='"+value+"' />");
        }
        return buf.toString();
    }

    public boolean isCacheYn() {
        String cacheYn = (String) get("cache_yn");
        if (cacheYn == null) {
            return false;
        }
        return  cacheYn.equalsIgnoreCase("true")
                || cacheYn.equalsIgnoreCase("on")
                || cacheYn.equalsIgnoreCase("yes");
    }
   
}// end of PropertyItem.java
