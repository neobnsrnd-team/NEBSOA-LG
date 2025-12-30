/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import nebsoa.common.exception.SysException;
import nebsoa.common.file.FileManager;
import nebsoa.common.log.LogManager;

import com.thoughtworks.xstream.XStream;

/*******************************************************************
 * <pre>
 * 1.설명 
 * DB to XML 동기화를 처리하는 클래스
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
 * $Log: DbToXml.java,v $
 * Revision 1.1  2018/01/15 03:39:49  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:30  cvs
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
 * Revision 1.1  2008/08/04 08:54:50  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:17  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:01  안경아
 * *** empty log message ***
 *
 * Revision 1.8  2007/03/29 05:17:44  김성균
 * XML 검증 및 모니터링 로직 추가
 *
 * Revision 1.7  2006/11/22 02:27:52  김성균
 * *** empty log message ***
 *
 * Revision 1.6  2006/11/20 01:50:41  김성균
 * *** empty log message ***
 *
 * Revision 1.5  2006/11/06 08:39:33  김성균
 * *** empty log message ***
 *
 * Revision 1.4  2006/11/03 09:59:14  김성균
 * *** empty log message ***
 *
 * Revision 1.3  2006/09/04 10:28:10  오재훈
 * xml String타입 변환 추가.
 *
 * Revision 1.2  2006/08/01 10:21:09  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2006/07/25 02:25:25  김성균
 * 최초 등록
 *
 * </pre>
 ******************************************************************/
public class DbToXml {
	
	protected static String DEFAULT_OPERATION_INFO_XML_PATH = 
			PropertyManager.getProperty("management", "OPERATION_INFO_XML_PATH");
	
	/**
	 * 객체의 데이타를 XML로 저장한다.
	 * @param obj	대상객체
	 */
	public static void toXml(String xmlFile, Object obj) {
		toXml(DEFAULT_OPERATION_INFO_XML_PATH, xmlFile, obj);
	}
	
	/**
	 * 객체의 데이타를 XML로 저장한다.
	 * @param xmlPath	XML 파일 경로
	 * @param xmlFile	XML 파일명
	 * @param obj		대상객체
	 */
	public static void toXml(String xmlPath, String xmlFile, Object obj) {
    	XStream xstream = new XStream();
    	String xml = xstream.toXML(obj);
    	StringBuffer buf = new StringBuffer();
    	buf.append("<?xml version=\"1.0\" encoding=\"EUC-KR\"?>\n");
    	buf.append(xml);
    	FileManager.updateFile(xmlPath, xmlFile, buf.toString());
	}
    
	/**
	 * 객체의 데이타를 XML로 리턴한다. 
	 * @param obj		대상객체
	 * @return			XML로 변환된 압축된 byte 배열
	 */
	public static byte[] toXml(Object obj) {
    	XStream xstream = new XStream();
    	String xml = xstream.toXML(obj);
    	StringBuffer buf = new StringBuffer();
    	buf.append("<?xml version=\"1.0\" encoding=\"EUC-KR\"?>\n");
    	buf.append(xml);
//        LogManager.debug("### XML = " + buf.toString());
//        LogManager.debug("### 압축하기 전의 SIZE = " + buf.toString().getBytes().length + " bytes");
        return IOUtils.compress(buf.toString().getBytes());
	}
	
	/**
	 * 객체의 데이타를 XML로 리턴한다. 
     * 저장될 필요가 없는 정보를 삭제한다.
	 * @param obj		대상객체
	 * @return			XML로 변환된 String
	 */
	public static String toXmlString(Object obj) {
    	XStream xstream = new XStream();
    	DataMap saveMap = null;
        
        if (obj instanceof DataMap) {
            DataMap map = (DataMap) obj;
            saveMap = (DataMap) map.clone();
            saveMap.remove("SAFETY_CARD");
            saveMap.remove("sso_token");
            saveMap.remove("JSESSIONID");
            saveMap.setContext(null);
            saveMap.remove(DataMap.USER_INFO);
        }
    	String xml = xstream.toXML(saveMap);
    	StringBuffer buf = new StringBuffer();
    	buf.append("<?xml version=\"1.0\" encoding=\"EUC-KR\"?>\n");
    	buf.append(xml);
        
        String xmlString = buf.toString();
        
        if (isToXmlMonitorMode()) {
            LogManager.debug("XML 검증 수행");
            try {
                fromXmlString(xmlString);
                LogManager.debug("XML is valide");
            } catch (Exception e) {
                LogManager.debug("XML is not valide");
            }
        }
        
        return xmlString;
	}
	
	/**
	 * XML 데이타를 객체의 데이타로 읽어들인다.
	 * @param xmlFile	읽어들일 XML 파일명
	 * @return			XML과 동기화된 객체
	 */
	public static Object fromXml(String xmlFile) throws FileNotFoundException {
		return fromXml(DEFAULT_OPERATION_INFO_XML_PATH, xmlFile);
	}
	
	/**
	 * XML 데이타를 객체의 데이타로 읽어들인다.
	 * @param xmlPath	XML 파일 경로
	 * @param xmlFile	XML 파일명
	 * @return			XML과 동기화된 객체
	 * @throws FileNotFoundException
	 */
	public static Object fromXml(String xmlPath, String xmlFile) throws FileNotFoundException {
		String src = xmlPath + xmlFile;
    	XStream xstream = new XStream();
    	Reader reader = new FileReader(src);
        
        return xstream.fromXML(reader);
	}
    
	/**
	 * XML 데이타를 객체의 데이타로 읽어들인다.
	 * @param data      압축된 데이타 바이트 배열	
	 * @return			XML과 동기화된 객체
	 * @throws IOException
	 */
	public static Object fromXml(byte[] data) throws IOException {
    	XStream xstream = new XStream();
        byte[] decompressData = IOUtils.decompress(data);
        String xml = new String(decompressData);
//        LogManager.debug("### XML = " + xml);
    	return xstream.fromXML(xml);
	}

	/**
	 * XML 데이타를 객체의 데이타로 읽어들인다.
	 * @param data      데이타 스트링	
	 * @return			XML과 동기화된 객체
	 * @throws IOException
	 */
	public static Object fromXmlString(String data) throws IOException {
    	XStream xstream = new XStream();
    	Object obj = null;
        try {
            obj = xstream.fromXML(data);
        } catch (Exception e) {
            LogManager.error("XML_ERROR", e.toString(), e);
            LogManager.error("XML_ERROR", "XML to OBJECT 변환오류\n" + data);
            data = correctXml(data);
            if (data == null) {
                throw new SysException("FRS00016", "XML을 OBJECT로 변환 중 오류가 발생하였습니다.");
            } else {
                obj = xstream.fromXML(data);
            }
        }
    	return obj;
	}

    /**
     * XML 스트링에서 map의 키값 갯수를 실제 저장된 키값 갯수로 보정한다.
     * @param data
     * @return
     * @throws JDOMException
     * @throws IOException
     */
    private static String correctXml(String data) {
        Document doc = null;
        XMLOutputter outp = null;
        String xml = null;
        
        try {
            if (data.indexOf("nebsoa.common.util.DataMap") != -1) {
	            SAXBuilder builder = new SAXBuilder();
	            doc = builder.build(new StringReader(data));
	            Element element = doc.getRootElement();
	            int keyCount = element.getChild("map").getChildren().size() - 3;
	            LogManager.error("XML_ERROR", "XML to DataMap 변환 보정 키값[keyCount=" + keyCount / 2 + "]");
	            Element el = (Element) element.getChild("map").getChildren("int").get(1);
	            el.setText(keyCount/2+"");
	            outp = new XMLOutputter();
	            xml = outp.outputString(doc);
            }
        } catch (NumberFormatException e) {
            LogManager.error("XML_ERROR", e.toString(), e);
        } catch (JDOMException e) {
            LogManager.error("XML_ERROR", e.toString(), e);
        } catch (IOException e) {
            LogManager.error("XML_ERROR", e.toString(), e);
        } catch (Exception e) {
            LogManager.error("XML_ERROR", e.toString(), e);
        }
        
        return xml; 
    }
    
    private static boolean isToXmlMonitorMode() {
        return PropertyManager.getBooleanProperty("monitor", "TO_XML_MONITOR_MODE", "OFF"); 
    }
}
