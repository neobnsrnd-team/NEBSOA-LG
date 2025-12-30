/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package test.dbtoxml;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.DataMap;
import nebsoa.common.util.DbToXml;

import com.thoughtworks.xstream.XStream;

/*******************************************************************
 * <pre>
 * 1.설명 
 * TODO DbToXmlTest 클래스에 대한 주석 넣기
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
 * $Log: D2X.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:36  cvs
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
 * Revision 1.2  2008/02/20 00:42:48  오재훈
 * *** empty log message ***
 *
 * Revision 1.1  2007/11/26 08:38:56  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2007/03/30 09:55:29  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class D2X extends TestCase {
    
    DataMap map = new DataMap();

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}
    
	public void testDbToXml() throws Exception {
        String xml = null;
        map.put("1", "1");
        map.put("2", "._~!@#$%^&*()./,'+\"<>-=_ ");
        map.put("3", null);
//	    DbToXml.toXml("test2.xml", map);
	    xml = DbToXml.toXmlString(map);
        LogManager.debug(xml);
        LogManager.debug("===========================================");
        
//	    map = (DataMap) DbToXml.fromXml("test3.xml");
        Reader reader = new FileReader(new File("D:/Project/nebsoa/config/operation/test8.xml"));
        char[] xmlChar = new char[10000];
        reader.read(xmlChar);
        xml = new String(xmlChar);
	    map = (DataMap) DbToXml.fromXmlString(xml);
        LogManager.debug(map);
//        LogManager.debug(xml);
	}
    
    /*
    public void testXml() throws Exception {
        SAXBuilder builder = new SAXBuilder();
        Reader reader = new FileReader(new File("D:/Project/nebsoa/config/operation/test.xml"));
//        Document doc = builder.build(new File("D:/Project/nebsoa/config/operation/test.xml"));
        Document doc = builder.build(reader);
        Element element = doc.getRootElement();
        Element el = (Element) element.getChild("map").getChildren("int").get(1);
        int keyCount = Integer.parseInt(el.getText()) - 1;
        el.setText(keyCount+"");
        System.out.println(element.getChild("map").getChildren("int").get(1));
        System.out.println(el.getText());
    }
    */
    
    final public void testKeySetOfHashMapCanBeSerialized() {
        final Map map = new HashMap();
        map.put("JUnit", null);
        final XStream xstream = new XStream();
        final String xml = xstream.toXML(map.keySet());
        final Collection collection = (Collection) xstream.fromXML(xml);
        assertNotNull(collection);
        assertEquals(1, collection.size());
        assertEquals("JUnit", collection.iterator().next());
    }

    final public void testValueSetOfHashMapCanBeSerialized() {
        final Map map = new HashMap();
        map.put(Boolean.TRUE, "JUnit");
        final XStream xstream = new XStream();
        final String xml = xstream.toXML(map.values());
        final Collection collection = (Collection) xstream.fromXML(xml);
        assertNotNull(collection);
        assertEquals(1, collection.size());
        assertEquals("JUnit", collection.iterator().next());
    }

    final public void testEntrySetOfHashMapCanBeSerialized() {
        final Map map = new HashMap();
        map.put(Boolean.TRUE, "JUnit");
        final XStream xstream = new XStream();
        final String xml = xstream.toXML(map.entrySet());
        final Collection collection = (Collection) xstream.fromXML(xml);
        assertNotNull(collection);
        assertEquals(1, collection.size());
        final Map.Entry entry = (Map.Entry) collection.iterator().next();
        assertEquals(Boolean.TRUE, entry.getKey());
        assertEquals("JUnit", entry.getValue());
    }

	
}
