/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.util;

import java.io.Reader;
import java.io.StringWriter;

import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;

import nebsoa.common.log.LogManager;

/*******************************************************************
 * <pre>
 * 1.설명 
 * XML 과 관련된 유틸리티 메소드
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
 * $Log: XmlUtil.java,v $
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
 * Revision 1.1  2007/11/26 08:38:02  안경아
 * *** empty log message ***
 *
 * Revision 1.4  2006/06/17 10:37:45  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class XmlUtil {
    
    /**
     * 객체 생성을 방지하기 위한 private 생성자
     *
     */
    private XmlUtil() {        
    }//end of constructor

    /**
     * <pre>
     * 주어진 객체를 XML 의 문자열로 변환하여 리턴합니다.
     * (Object -> XML)
     * 
     * 본 메소드는 jibx API 를 사용하여 OXB(Object-XML Binding)을 수행합니다.
     * </pre>
     * @param obj XML 로 변환할 데이터를 포함하고 있는 객체
     * @return XML 로 변환된 문자열 형태의 데이터
     */
    public static final String object2Xml(Object obj) {
        StringWriter writer = null;
        try {
        	
            IBindingFactory bf = BindingDirectory.getFactory(obj.getClass());
            IMarshallingContext context = bf.createMarshallingContext();
            context.setIndent(4);
            writer = new StringWriter();
            context.marshalDocument(obj, "EUC-KR", null, writer);
            writer.flush();
            return writer.getBuffer().toString();
        } catch (JiBXException e) {
            LogManager.error(e);
            throw new RuntimeException(e);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }//end if
            } catch (Exception e) {}//ignored
        }//end try catch
    }//end of object2Xml()
    
    /**
     * <pre>
     * 주어진 Reader 로부터 XML 데이터를 읽어들여서
     * 그와 매핑되는 클래스의 객체를 생성하여 리턴합니다.
     * (XML -> Object)
     * 
     * 본 메소드는 jibx API 를 사용하여 OXB(Object-XML Binding)을 수행합니다.
     * </pre>
     * @param reader XML 데이터를 읽어들이기 위한 Reader
     * @param clazz XML 과 매핑되는 클래스
     * @return XML 로부터 생성된 객체
     */
    public static final Object toObject(Reader reader, Class clazz) {
        try {
            IBindingFactory bf = BindingDirectory.getFactory(clazz);
            IUnmarshallingContext context = bf.createUnmarshallingContext();
            return context.unmarshalDocument(reader, null);
        } catch (JiBXException e) {
            LogManager.error(e);
            throw new RuntimeException(e);
        }//end try catch
    }//end of toObject()
    
    /**
     * 다음은 자체 테스트 코드를 포함하고 있는 메소드 입니다.
     * 
     * @param args 테스트를 하기 위해서 사용되는 인자 값 들의 리스트
     */
    public static void main(String [] args) {
        java.util.Map map = new java.util.HashMap();
        map.put("a","b");
        map.put("c","d");
        System.out.println(object2Xml(map));
        
    }//end of main()
    
}// end of XmlUtil.java