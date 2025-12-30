/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.engine.message.proxy;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.collections.ArrayStack;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import nebsoa.common.collection.DataSet;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.DataMap;
import nebsoa.common.util.StringUtil;
import nebsoa.spiderlink.engine.message.MessageField;
import nebsoa.spiderlink.engine.message.MessageStructure;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 대응답 XML message를 파싱을 담당하는 클래스
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
 * $Log: ProxyXMLMessageParseHandler.java,v $
 * Revision 1.1  2018/01/15 03:39:54  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:37  cvs
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
 * Revision 1.1  2008/08/04 08:54:51  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:23  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/12/18 08:30:09  김승희
 * 최초 등록
 *
 *
 * </pre>
 ******************************************************************/
public class ProxyXMLMessageParseHandler extends DefaultHandler
{
	private DataMap dataMap = null;
	private ProxyXMLMessageParseDelegator delegator = null;
	private MessageStructure structure = null;
	private String strXPath = new String(); 
	private boolean booleanStart = false;
	private String fieldNM;

	private String rootElement;
	
	private ArrayStack loopStack = new ArrayStack(0);
	private Map delegatorMap = new HashMap();
	private ArrayStack delegatorStack = new ArrayStack(0);
    
    private String tagName=null;
    private StringBuffer buf=null;
	
	public ProxyXMLMessageParseHandler(DataMap dataMap, MessageStructure structure, String rootElement)
	{
		this.dataMap = dataMap;
		this.structure = structure;
		this.rootElement = rootElement;
	}
    
    public void doParse(String xmlString) throws ParserConfigurationException, SAXException, IOException {
        
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        InputSource inputSource = new InputSource(new StringReader(xmlString));
        parser.parse(inputSource, this);
        
    }

	public void startElement(String uri, String localName, String tagName, 
			 Attributes attributes) {
		
		this.tagName = tagName;
		strXPath += "/"+tagName;
        
        buf = new StringBuffer();
		
		/*MessageField mField = structure.getFieldByFieldTag(StringUtils.removeStart(strXPath, "/"+rootElement));
		if(mField!=null) { 
			fieldNM = mField.getName();
		}else{
			fieldNM = null;
		}*/
        
        //태그이름이 곧 필드명
        fieldNM = tagName;
        
		if(delegator!=null){
					
			//이전 Loop 데이터 담기
			if(!StringUtil.contains(strXPath, delegator.getLoopFieldTag())){
				
				processLoopData();
			}
		
		}
		
		if(fieldNM.startsWith(MessageField.BEGIN_LOOP))
		{	 										
			
			String loopFieldTag = strXPath;
			String loopFieldName = fieldNM;

			delegator = getXMLMessageParseDelegator(loopFieldName);
			
			//LOOP Field를 만났을 때 해당 Loop이 처음이면(delegator==null)
			//delegator를 새로 만들어서 Map과 stack에 넣고 
			//한번 나왔던 Loop이면 Map에서 꺼내서 stack에 넣는다.
			if(delegator!=null){
				delegator.clear();
				if(delegatorStack.peek()!=delegator){
					delegatorStack.push(delegator);
					loopStack.push(Boolean.valueOf(true));
				}
			}else{
			
				LogManager.debug("새 Loop["+ loopFieldName+"]시작.. ");
				delegator = new ProxyXMLMessageParseDelegator(structure, rootElement);
				delegatorMap.put(loopFieldName, delegator);
				delegatorStack.push(delegator);
				delegator.setLoopFieldName(loopFieldName);
				delegator.setLoopFieldTag(loopFieldTag);
				
				loopStack.push(Boolean.valueOf(true));
			}
			
			if(fieldNM!=null)
			{
			   delegator.setFieldName(fieldNM);
			}
		}
		
		//attributes 입력은 지원하지 않는다.
		/*if(attributes != null)
		{	
			String name = null;
			for(int i=0; i<attributes.getLength(); i++)
			{	
								
				strXPath += "@"+attributes.getQName(i);
				if(isLoop())	delegator.setValue(strXPath, attributes.getValue(attributes.getQName(i)));
				else{
					name = structure.getFieldByFieldTag(StringUtils.removeStart(strXPath, "/"+rootElement)).getName();
					dataMap.put(name, attributes.getValue(attributes.getQName(i)));
				}
				strXPath = strXPath.substring(0, strXPath.lastIndexOf("@"));	
			}
		}*/
		
		booleanStart = true;
	}
	

	public void characters(char[] ch, int start, int length) {
		
        if (buf != null) {
            buf.append(new String(ch, start, length));
        }

        if (booleanStart) {	
			String value = buf.toString();

            if (value != null) value = value.trim();

            String maskXml = StringUtil.maskedData(fieldNM, value);
						
			if (isLoop()) {
                if (!value.equals("")) {
                    LogManager.debug("LOOP VALUE=" + maskXml);
                    delegator.setValue(strXPath, value);
                }
            } else {
                if (!value.equals("") && fieldNM != null) {
                    LogManager.debug("NON LOOP VALUE[" + fieldNM + "]=" + maskXml);
                    dataMap.put(fieldNM, value);
                }
            }
		}
	}
	
	public void endElement(String uri, String localName, String tagName) {
		
        buf=null;
		
        if(isLoop())
		{	if(strXPath.equals(delegator.getLoopFieldTag()) )
			{					
				delegator.endRow();
			}
			else if(!StringUtil.contains(strXPath, delegator.getLoopFieldTag()))
			{				
				processLoopData();
				
				//Loop 태그들이 하나의 태그로 감싸지지 않은 경우 처리를 위해서 추가함.
				if(isLoop())
				{	if(strXPath.equals(delegator.getLoopFieldTag()) )
					{					
						delegator.endRow();
					}
				}
			}
		}
	
		strXPath = strXPath.substring(0, strXPath.lastIndexOf("/"));		
		booleanStart = false;

	}

	private void processLoopData() {
		DataSet set = delegator.makeDataSet();	
		if(set!=null) set.initRow();
		loopStack.pop();
		//현재 loopFieldName의 이름을 담아 놓는다.
		String loopFieldName = delegator.getLoopFieldName();
		delegator = (ProxyXMLMessageParseDelegator)delegatorStack.pop(); //현재 거를 없애고
		//초기화 한다.
		delegator.init();
		
		//그 전 거를 꺼낸다.
		if(delegatorStack.isEmpty())delegator = null;
		else delegator = (ProxyXMLMessageParseDelegator)delegatorStack.peek(); 
						
		if(delegator==null){
			dataMap.put(loopFieldName, set);
			LogManager.debug(loopFieldName + "을  dataMap에 담음");
		}
		else{
			delegator.setDataSetValue(loopFieldName, set);
			LogManager.debug(loopFieldName + "을  " + delegator.getLoopFieldName() +"에 담음");
		}
	}
	
	public DataMap getDataMap()
	{
		return dataMap;
	}

	private ProxyXMLMessageParseDelegator getXMLMessageParseDelegator(String loopName){
		ProxyXMLMessageParseDelegator delegator = (ProxyXMLMessageParseDelegator)delegatorMap.get(loopName); 
		return delegator;
	}
	
	private boolean isLoop(){
		if(loopStack.isEmpty() || loopStack.peek()==null) return false;
		else return true;
		//return ((Boolean)loopStack.peek()).booleanValue();
	}


}
