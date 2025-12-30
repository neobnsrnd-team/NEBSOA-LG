
package nebsoa.spiderlink.engine.parser;

/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.dom.DOMComment;
import org.dom4j.dom.DOMDocument;
import org.dom4j.dom.DOMElement;

import nebsoa.common.log.LogManager;
import nebsoa.common.util.StringUtil;
import nebsoa.spiderlink.engine.message.LoopField;
import nebsoa.spiderlink.engine.message.MessageField;
import nebsoa.spiderlink.engine.message.MessageParseException;
import nebsoa.spiderlink.engine.message.MessageStructure;
import nebsoa.spiderlink.engine.message.MessageStructurePool;

/*******************************************************************
 * <pre>
 * 1.설명 
 * FreeMaker 형식의 XML 전문 템플릿을 생성한다.
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
 * $Log: XMLTemplateMaker.java,v $
 * Revision 1.1  2018/01/15 03:39:50  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:39  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:23  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:27  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:51  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:19  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:00  안경아
 * *** empty log message ***
 *
 * Revision 1.20  2007/07/16 03:46:54  김승희
 * Nested Loop, CDATA Section 처리 관련 변경
 *
 * Revision 1.19  2007/03/09 06:23:01  김승희
 * field type이 CDATA이면 escape하지 않도록 처리
 *
 * Revision 1.18  2006/09/07 11:13:40  김승희
 * *** empty log message ***
 *
 * Revision 1.17  2006/09/07 11:13:01  김승희
 * _BeginLoop_ alias 변경
 *
 * Revision 1.16  2006/08/16 05:46:11  김승희
 * *** empty log message ***
 *
 * Revision 1.15  2006/08/16 05:45:49  김승희
 * _BeginLoop_이 없을 때 처리
 *
 * Revision 1.14  2006/08/11 07:50:55  김승희
 * Message Structure 로딩 방법 수정 -> 건별 로딩
 *
 * Revision 1.13  2006/06/28 01:44:35  김승희
 * log 제거
 *
 * Revision 1.12  2006/06/28 01:43:25  김승희
 * exception 처리, log 수정
 *
 * Revision 1.11  2006/06/26 02:03:50  김승희
 * END LOOP 관련 처리
 *
 * Revision 1.10  2006/06/22 02:47:59  김승희
 * 상수 처리
 *
 * Revision 1.9  2006/06/22 02:17:56  김승희
 * LOOP의 이름이 없을 때는 임시로 부여하는 로직 추가
 *
 * Revision 1.8  2006/06/17 10:12:09  김승희
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public class XMLTemplateMaker {

	//현재 처리중인 Element
	private Element currElement;
	
	//임시로 쓰는 rootElement
	private static final String ROOT_ELEMENT = "ROOT";
	
	//임시로 쓰는 LOOP 이름
	private static final String LOOP_NAME = "_LOOP";
	
	//XML 선언부
	private static final String XML_START_STR = "<?xml version=\"1.0\" encoding=\"EUC-KR\"?>";
	
	//root Element 여는 태그
	private static final String ROOT_ELEMENT_START_TAG = "<"+ROOT_ELEMENT+">";
	
	//root Element 닫는 태그
	private static final String ROOT_ELEMENT_END_TAG = "</"+ROOT_ELEMENT+">";
	
	    
	/**
	 * DataMap에서 value를 꺼낼 FreeMaker 커스텀 메소드의 alias 이름
	 * 
	 * @see nebsoa.spiderlink.engine.parser.FreeMarkerMapMethod.exec
	 */
	public static final String METHOD_NAME = "getObject"; 
	
	/**
	 * 메시지 structure ID에 해당하는 XML Template를 생성한다.
	 * @param structureID
	 * @return XML Template String
	 */
	public String makeTemplate(String structureID){
		
		try{
			//strucure 가져오기
			LogManager.debug(structureID + " XML Template 생성 시작..");
			MessageStructure strucure = MessageStructurePool.getInstance().getMessageStructure(structureID);
			DOMDocument xmlDoc = new DOMDocument();
			xmlDoc.setRootElement(new DOMElement(ROOT_ELEMENT));
			xmlDoc.setXMLEncoding("EUC-KR");
			currElement = xmlDoc.getRootElement();
	
			MessageField field = null;
			String fieldTag = null;
			
			for(int i=0; i<strucure.size(); i++){
				field = strucure.getField(i);
				if(field==null) continue;
				fieldTag = strucure.getFieldTag();
				parseField(xmlDoc, field);
			}
			
		    String xmlTemplateString = setFreeMarkerDirective(xmlDoc.asXML());
		    	    
	        LogManager.debug(structureID +"의 XML Template");
	        LogManager.debug(xmlTemplateString);
	
			/*OutputFormat format = OutputFormat.createPrettyPrint();
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out, "euc-kr"));
		    XMLWriter writer = new XMLWriter(bw, format);
		    writer.write( xmlDoc );
		    writer.flush();*/

		    return xmlTemplateString;
		}catch(Throwable th){
			th.printStackTrace();
			throw new MessageParseException(structureID + "의 XML Template 생성 중 오류가 발생하였습니다. XML 설정을 확인하세요. [" + th +"]");
		}

	}
	private String setFreeMarkerDirective(String xmlTemplate){
		//1. XML 주석으로 되어 있는 loop 표시를 FreeMaker <#list>표시로 치환.
		xmlTemplate = StringUtils.replace(xmlTemplate, "<!--end", "</#");
		xmlTemplate = StringUtils.replace(xmlTemplate, "<!--", "<#");
		xmlTemplate = StringUtils.replace(xmlTemplate, "-->", ">");
		
		//2. XML 선언부 /루트 엘리먼트 제거(전체 메시지 완성 후 붙임)
	    xmlTemplate = StringUtils.removeStart(xmlTemplate, XML_START_STR).trim();
	    xmlTemplate = StringUtils.removeStart(xmlTemplate, ROOT_ELEMENT_START_TAG);
	    xmlTemplate = StringUtils.removeEnd(xmlTemplate, ROOT_ELEMENT_END_TAG);

		return xmlTemplate;
	}
	
	private void parseField(DOMDocument xmlDoc, MessageField field){
		
		
		
		if(field instanceof LoopField){
			makeLoopElement(xmlDoc, (LoopField)field);
			
		}else if(MessageField.DUMMY_USE.equals(field.getUseMode()) 
				|| field.getName().startsWith(MessageField.END_LOOP)){
			return;
	    }else{
			makeElement(xmlDoc, field);
		}

	}
	private void makeElement(DOMDocument xmlDoc, MessageField field){
		makeElement(xmlDoc, field, "");
	}
	private void makeElement(DOMDocument xmlDoc, MessageField field, String parentFieldName) {
		String xPath = StringUtil.NVL(field.getFieldTag());
		String fieldName = field.getName();

		int index = -1;
		Element el = null;
		
		//attribute가 없으면
		if((index=xPath.indexOf("@"))==-1){
			el = getElementByXPath(xmlDoc, currElement, "/" + ROOT_ELEMENT+xPath);
			el.setText(getText(field, parentFieldName));
        
		//attribute가 있으면
		}else{
			String xPathWithoutAttr = xPath.substring(0, index);
			String attr = xPath.substring(index+1);
			el = getElementByXPath(xmlDoc, currElement, "/" + ROOT_ELEMENT + xPathWithoutAttr);
			el.addAttribute(attr, getText(field, parentFieldName));
		}
		
	}
	private void makeLoopElement(DOMDocument xmlDoc, LoopField field) {
		makeLoopElement(xmlDoc, field, ""); 
	}
	private void makeLoopElement(DOMDocument xmlDoc, LoopField field, String parentLooName) {
		
		String xPath = field.getFieldTag();
		String fieldName = field.getName();
		String prefixName = StringUtils.removeStart(fieldName, MessageField.BEGIN_LOOP);
		//_BeginLoop_을 잘라내고 나머지 이름앞에 _LOOP을 붙인다.
		prefixName = LOOP_NAME + prefixName;

		
		//parentLooName이 있을 때는 끝에 .을 붙인다.
		//if(!StringUtil.isNull(parentLooName))parentLooName+=".";
		
		//////////////////위 두줄을 아래와 같이 수정 by ksh 2007.05.23 /////////////
		// Loop 안에 Loop가 있을 경우 바깥 Loop의 alias로 안의 Loop를 정의해야 됨.
		// 즉 <# list _BeginLoop_list?if_exists as Looplist > .... 안에 nested Loop가 있는 경우
		// <# list _BeginLoop_list._BeginLoop_list2?if_exists as Looplist2>가 아니라
		// <# list Looplist._BeginLoop_list2?if_exists as Looplist2> 로 만들어야 됨.
		if(!StringUtil.isNull(parentLooName)){
			//_BeginLoop_을 잘라내고 나머지 이름 앞에 _LOOP을 붙이고 뒤에 .을 붙인다.
			parentLooName= LOOP_NAME + StringUtils.removeStart(parentLooName, MessageField.BEGIN_LOOP)+".";
		}
				
		DOMElement el = getElementByXPath(xmlDoc, currElement, "/" + ROOT_ELEMENT + xPath);
		String loopDirective = new StringBuffer("list ").append(parentLooName).append(fieldName).append("?if_exists as ").append(prefixName).toString();
		
		el.getParentNode().insertBefore(new DOMComment(loopDirective), el);
		el.getParent().addComment("endlist");
		
		ArrayList childList = field.getChildList();
		int size = childList.size();
		MessageField nestedField = null;
		String nestedFieldName = null;
		
		for(int i=0; i<size; i++){
			nestedField = (MessageField)childList.get(i);
			nestedFieldName = nestedField.getName();
			//loop안의 loop 처리
			if(nestedFieldName.startsWith(MessageField.BEGIN_LOOP)){
				parentLooName = fieldName;
				makeLoopElement(xmlDoc, (LoopField)nestedField, parentLooName);
					
			}else if(nestedFieldName.startsWith(MessageField.END_LOOP)){
				//Do nothing..
			}else{
				makeElement(xmlDoc, nestedField, prefixName);
			}
				
		}
	
	}
	
	private DOMElement getElementByXPath(DOMDocument xmlDoc, Element element, String xPath){
		Node node = xmlDoc.selectSingleNode(xPath);
		
		String parentXPath = getParentXPath(xPath);
				
		if(node==null){
			element = getElementByXPath(xmlDoc, element, parentXPath);
			
			String elementName = getEndElementName(xPath);
			DOMElement newElement = new DOMElement(elementName);

			element.add(newElement);
			this.currElement = newElement;
			return newElement;
				
		}else{
			DOMElement domElement = (DOMElement)node;
			this.currElement = domElement;
			return domElement;
		}
				
	}
	
	private String getParentXPath(String xPath){
		
		String[] nodeName = xPath.split("/");
		StringBuffer sb = new StringBuffer();
		for(int i=1; i<nodeName.length-1; i++){
			sb.append("/");
			sb.append(nodeName[i]);
		}

		return sb.toString();		
	}
	
	private String getEndElementName(String xPath){
		String[] nodeName = xPath.split("/");
		return nodeName[nodeName.length-1];
	}
	
	private String getText(MessageField field){
		String fieldName = field.getName();
		String escape = getEscape(field);
		StringBuffer sb = new StringBuffer();
		//sb.append("${").append(METHOD_NAME).append("(\'").append(fieldName).append("\')?html}");
		sb.append("${").append(METHOD_NAME).append("(\'").append(fieldName).append("\')").append(escape).append("}");
		return sb.toString();
	}
	
		
	private String getText(MessageField field, String dataModelName){
				
		if(StringUtil.isNull(dataModelName)) return getText(field);
		
		String fieldName = field.getName();
		String escape = getEscape(field);
		StringBuffer sb = new StringBuffer();
		//sb.append("${").append(METHOD_NAME).append("(\'")
		//.append(fieldName).append("\', ").append(dataModelName).append(")?html}");
		
		sb.append("${").append(METHOD_NAME).append("(\'")
		.append(fieldName).append("\', ").append(dataModelName).append(")").append(escape).append("}");
		return sb.toString();
	}
	
	//escape 방식을 리턴한다.
	private String getEscape(MessageField field) {
		
		//XML 필드가 엘리먼트의 경우 필드 타입이 CDATA일 때는 escaping하지 않는다.
		//어트리뷰트의 경우는 필드 타입이 CDATA이어도 escaping한다.
		if(StringUtil.NVL(field.getFieldTag()).indexOf("@")==-1 
				&& MessageField.CDATA.equals(field.getFieldType())){
			return "";
		}else{
			return "?html";
		}
		
	}
}
