/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.engine.parser;

import java.io.StringWriter;
import java.util.Iterator;
import java.util.Locale;

import nebsoa.common.collection.DataSet;
import nebsoa.common.collection.FreeMarkerDataSet;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.DataMap;
import nebsoa.spiderlink.engine.message.ByteMessageInstance;
import nebsoa.spiderlink.engine.message.MessageParseException;
import nebsoa.spiderlink.engine.message.MessageStructure;
import nebsoa.spiderlink.engine.message.MessageType;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

/*******************************************************************
 * <pre>
 * 1.설명 
 * DataMap의 값을 가지고 XML 메시지를 생성한다.
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
 * $Log: XMLMessageGenerator.java,v $
 * Revision 1.1  2018/01/15 03:39:49  cvs
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
 * Revision 1.1  2007/11/26 08:38:01  안경아
 * *** empty log message ***
 *
 * Revision 1.20  2007/07/16 03:45:04  김승희
 * 주석 정리
 *
 * Revision 1.19  2006/11/23 04:05:29  김승희
 * 소스 정리
 *
 * Revision 1.18  2006/11/22 07:24:30  김승희
 * 닫는 태그 처리 부분 수정
 *
 * Revision 1.17  2006/11/22 07:06:02  김승희
 * *** empty log message ***
 *
 * Revision 1.16  2006/08/11 07:50:55  김승희
 * Message Structure 로딩 방법 수정 -> 건별 로딩
 *
 * Revision 1.15  2006/07/21 05:59:16  김승희
 * remark관련 처리
 *
 * Revision 1.14  2006/06/19 11:57:36  김승희
 * MessageInstance --> ByteMessageInstance로 수정
 *
 * Revision 1.13  2006/06/17 10:12:09  김승희
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public class XMLMessageGenerator implements MessageGenerator {

	private static XMLMessageGenerator messageGenerator = new XMLMessageGenerator();
	
	private XMLTemplateLoader myXMLTemplateLoader;
	
	private Configuration cfg;
	
	//XML 선언부
	public static final String XML_STARTER = "<?xml version=\"1.0\" encoding=\"EUC-KR\"?>";
	
	private XMLMessageGenerator(){
		myXMLTemplateLoader = new XMLTemplateLoader();
		
		//전체 템플릿을 로딩하지 않는다.
		//myXMLTemplateLoader.loadAll();
		cfg = new Configuration();
		cfg.setObjectWrapper(new DefaultObjectWrapper());  
		cfg.loadBuiltInEncodingMap();
		cfg.setDefaultEncoding("EUC-KR"); 
		cfg.setEncoding(Locale.KOREA, "EUC-KR");
		
		///// 절대로 바꾸지 마시오!!!! by ksh ///////
		cfg.setLocalizedLookup(false);
		////////////////////////////////////////
		
		cfg.setTemplateLoader(myXMLTemplateLoader);
	
	}
	
	public static XMLMessageGenerator getInstance(){
		return messageGenerator;
	}
	public ByteMessageInstance generate(MessageStructure structure,
			ByteMessageInstance instance, DataMap dataMap) {
		try{
			
			//DataMap안에 담긴 DataSet을 FreeMaker에서 인식 가능한 FreeMarkerDataSet 형태로 바꾼다.
			transformFreeMarkerDataSet(dataMap);
			
			//DataMap에서 value를 꺼낼 FreeMaker 커스텀 메소드를 지정한다.
			FreeMarkerMapMethod freeMarkerMapMethod = new FreeMarkerMapMethod(dataMap, structure);
			dataMap.put(XMLTemplateMaker.METHOD_NAME, freeMarkerMapMethod);   
			
			Template template = cfg.getTemplate(structure.getStructureId());
			StringWriter stringWriter = new StringWriter();
			
			//템플릿과 dataMap의 값 merge...
			template.process(dataMap, stringWriter);
			stringWriter.flush();
			//생성된 xmlString
			String xmlString = stringWriter.toString();
			
			//Root 엘리먼트 처리
			xmlString = doRootElement(structure, xmlString, instance);
			
			//MessageInstance에 생성된 XML byte[]로 write...
			byte[] xmlBytes = xmlString.getBytes();
			byte[] logXmlBytes = null;
			
			//Remark 필드가 있을 경우에만 로그용 XML을 생성한다.
			if(structure.isRemark()){
				//로그 생성을 위한 merge
				stringWriter = new StringWriter();
				//remark 적용여부를 true로 한다.
				freeMarkerMapMethod.setRemarkApplied(true);
				dataMap.put(XMLTemplateMaker.METHOD_NAME, freeMarkerMapMethod);   
				template.process(dataMap, stringWriter);
				stringWriter.flush();
				//생성된 로그 xmlString
				String logXmlString = stringWriter.toString();
				logXmlString = doRootElement(structure, logXmlString, instance);
				logXmlBytes = logXmlString.getBytes();
			
			//Remark 필드가 아닌 경우에는 실제 메시지가 로그용 메시지가 된다. 
			}else{
				logXmlBytes = xmlBytes;
			}
			
			LogManager.debug("structure ID[" + structure.getStructureId() +"] XML 생성");
			
			//MessageInstance에 길이 조정.
			instance.adjustMessageLength(xmlBytes);
			//원본 xml
			System.arraycopy(xmlBytes, 0, instance.getBytes(), instance.getOffset(), xmlBytes.length);
			//로그
			System.arraycopy(logXmlBytes, 0, instance.getLogData(), instance.getOffset(), logXmlBytes.length);
			//offset 조정
			instance.setOffset(instance.getOffset() + xmlBytes.length);
			
			return instance;
		
		}catch(MessageParseException me){
			throw me;
		}catch(Throwable th){
			//th.printStackTrace();
			throw new MessageParseException("XML 생성중 오류가 발생하였습니다. : " + th);
		}finally{
			//FreeMarkerMapMathod 제거..
			dataMap.remove(XMLTemplateMaker.METHOD_NAME);
		}
	}

	/**
	 * XML의 ROOT 엘리먼트 및 선언부 처리를 한다.
	 * @param structure
	 * @param xmlString
	 * @param byteMessageInstance
	 * @return 처리된 XML
	 */
	private String doRootElement(MessageStructure structure, String xmlString, ByteMessageInstance byteMessageInstance) {
		
		String startRootTag = new StringBuffer().append("<").append(structure.getXmlRootTag()).append(">").toString();
		String endRootTag = new StringBuffer().append("</").append(structure.getXmlRootTag()).append(">").toString();
		
		StringBuffer xmlStringBuffer = new StringBuffer(xmlString);
		//일단 무조건 닫는 태그를 끝에 붙인다.
		xmlStringBuffer.append(endRootTag);
		
		//부모 메시지가 있을 때
		if(structure.getParentId()!=null){
			MessageStructure parentStructure = structure.getParent();
			//부모 메시지 타입이 XML일 때
			if(parentStructure.getMessageType().getType().equals(MessageType.XML_STRING)){
				//부모 메시지 끝이 닫는 태그이면  부모 메시지의 닫는 태그 제거(offset을 줄인다.)
				String endMsg = new String(byteMessageInstance.getBytes(), (byteMessageInstance.getOffset()-endRootTag.getBytes().length), endRootTag.getBytes().length);
				if(endMsg.equals(endRootTag)){
					byteMessageInstance.setOffset(byteMessageInstance.getOffset()-endRootTag.getBytes().length);
				}
				
			}else{
				//여는 태그를 앞에 붙임.
				xmlStringBuffer.insert(0, XML_STARTER+startRootTag);
			}
			
		//부모 메시지가 없을 때
		}else{
			//여는 태그를 앞에 붙임.
			xmlStringBuffer.insert(0, XML_STARTER+startRootTag);
		}
		return xmlStringBuffer.toString();
	}
	
	private void transformFreeMarkerDataSet(DataMap dataMap) {
		Iterator iter = dataMap.keySet().iterator();
		String key = null;
		Object obj = null;
		while(iter.hasNext()){
			key = (String)iter.next();
			obj = dataMap.get(key);
			if(obj instanceof DataSet){

				dataMap.put(key, FreeMarkerDataSet.transform((DataSet)obj));
			}
		}
	}
	

}
