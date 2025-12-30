/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.property;

import java.io.File;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import nebsoa.common.Constants;
import nebsoa.common.exception.SysException;
import nebsoa.common.file.FileManager;
import nebsoa.common.jdbc.DBConfig;
import nebsoa.common.log.LogManager;


/*******************************************************************
 * <pre>
 * 1.설명 
 * XmlProperty Load하는 클래스
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
 * $Log: XmlPropertyLoader.java,v $
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
 * Revision 1.1  2007/11/26 08:38:22  안경아
 * *** empty log message ***
 *
 * Revision 1.9  2007/07/30 08:36:14  김성균
 * *** empty log message ***
 *
 * Revision 1.8  2007/07/17 12:52:43  이종원
 * *** empty log message ***
 *
 * Revision 1.7  2006/12/11 08:11:09  이종원
 * PropertyItem을 Hashtable로 수정
 *
 * Revision 1.6  2006/12/05 05:50:45  이종원
 * PropertyItem에 cacheYn추가
 *
 * Revision 1.5  2006/10/20 06:49:22  이종원
 * *** empty log message ***
 *
 * Revision 1.4  2006/07/04 13:46:13  김성균
 * properties 경로 변경
 *
 * Revision 1.3  2006/06/17 10:37:45  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class XmlPropertyLoader extends PropertyLoader {

    private static String filePath = Constants.APP_HOME_DIR + "/config/properties";

    private long lastModified;

    private String fileName;

    public XmlPropertyLoader(String propertyGroupId) {
        super(propertyGroupId);

        if (propertyGroupId == null) {
            throw new SysException("로딩할 프로퍼티 파일을 지정하세요");
        }

        this.fileName = getPropertyFileName(propertyGroupId);
        load();
    }

    public XmlPropertyLoader(String propertyGroupId, Properties prop) {
        super(propertyGroupId);
        
        if (propertyGroupId == null) {
            throw new SysException("로딩할 프로퍼티 파일을 지정하세요");
        }

        this.fileName = getPropertyFileName(propertyGroupId);

        Enumeration e = prop.keys();
        PropertyItem item = null;
        String propertyKey = null;
        String propertyValue = null;

        while (e.hasMoreElements()) {
            propertyKey = (String) e.nextElement();
            propertyValue = prop.getProperty(propertyKey);
            item = new PropertyItem(propertyKey, propertyValue, "");
            propertyCache.put(propertyKey, item);
            System.out.println("--->KEY : " + propertyKey + ",VALUE:" + propertyValue);
        }
        store();
    }
    
    private String getPropertyFileName(String fileName) {
        if (fileName.indexOf(".properties") == -1) {
            fileName = fileName + ".properties";
        }

        if (!fileName.endsWith(".xml")) {
            fileName = fileName + ".xml";
        }
        
        return fileName;
    }

    public void load() {
        File file;
        Document doc;
        Node root;
        
        try {
            file = new File(filePath, this.fileName);

            if (!file.exists()) {
                System.out.println("<Property>파일이 존재 하지 않습니다 (" + file.getAbsolutePath() + ")");
                return;
            }   
            /*
             * cache 사용 하지 않을 경우에는 무조건 로드 합니다.
             */
            if (usePropertyCache == true) {
                long lastModifedTime = file.lastModified();
                if (lastModified >= lastModifedTime) {
                    //System.out.println("<Property>를 로딩 skip 합니다.(" + file.getAbsolutePath() + ")");                
                    return;
                }
                lastModified = lastModifedTime;
            }

            /*
             * Map 에 있는 데이터를 클리어 하지 않으므로, 리로드 하면 이전의 키 값이 바뀐 놈들은 
             * 데이터가 그냥 그대로 남는다. 
             * 따라서, clear 하고 다시 로드 하도록 수정함.
             * 
             * 2005.12.06 - Helexis
             */
            synchronized(propertyCache){
                System.out.println("<Property>를 로딩  합니다.(" + file.getAbsolutePath() + ")");                
                propertyCache.clear();
    
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                doc = db.parse(file);
    
                // Root Node를 추출한다.
                root = (Node) doc.getDocumentElement();
                NodeList nl = root.getChildNodes();
                Node childNode = null;
                Element propertyElement = null;
                String propertyKey = null;
                PropertyItem propertyItem = null;
                for (int i = 0; i < nl.getLength(); i++) {
                    childNode = nl.item(i);
                    if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                        propertyItem = new PropertyItem();
                        propertyElement = (Element) childNode;
                        propertyKey = propertyElement.getAttribute("key");
                        
                        NamedNodeMap nodemap = propertyElement.getAttributes();
                        int attributeCount = nodemap.getLength();
                        Attr attr=null;
                        Node node=null;
                        String name=null;
                        String value=null;
                        for(int x=0;x<attributeCount;x++){
                            attr=null;
                            name=null;
                            value=null;

                            node = nodemap.item(x);
                            if(node.getNodeType() == Node.ATTRIBUTE_NODE){
                                attr = (Attr) node; 
                                name=attr.getName();
                                value=attr.getValue();
                                propertyItem.put(name,value);
                            }
                        }
                        
                        propertyCache.put(propertyKey, propertyItem);
                        //System.out.println(propertyItem.toString());
                    }
                }
            }
        } catch (Exception e) {
            LogManager.error("property file load 실패\n" + e.getMessage());
        }
    }

    /* (non-Javadoc)
     * @see nebsoa.common.property.PropertyLoader#store()
     */
    public void store() {
        StringBuffer buf = new StringBuffer();

        buf.append("<?xml version=\"1.0\" encoding=\"EUC-KR\"?>");
        buf.append("\n");
        buf.append("<!-- " + propertyGroupId + " property -->");
        buf.append("\n");
        buf.append("<properties>");
        Set keySet = propertyCache.keySet();
        Iterator i = keySet.iterator();
        // Enumeration e = this.elements(); //properties 상속 받았을 때
        PropertyItem item = null;
        // while (e.hasMoreElements()){
        String key = null;
        while (i.hasNext()) {
            // item = (PropertyItem)e.nextElement();
            key = (String) i.next();
            item = (PropertyItem) propertyCache.get(key);
            buf.append(item.toXml());
        }
        buf.append("\n");
        buf.append("</properties>");

        FileManager.updateFile(filePath, fileName, buf.toString(),true);

        System.out.println("#########<Property> " + filePath + "의 " + fileName
                + " 저장됨.");

        if (fileName.indexOf("db.") > -1) {
            DBConfig.reloadAll();
        }
    }

}
