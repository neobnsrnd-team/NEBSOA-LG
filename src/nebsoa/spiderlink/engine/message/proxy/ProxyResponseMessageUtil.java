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

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import nebsoa.common.exception.SysException;
import nebsoa.common.util.DataMap;
import nebsoa.common.util.IOUtils;
import nebsoa.spiderlink.context.MessageContext;
import nebsoa.spiderlink.engine.message.MessageStructure;
import nebsoa.spiderlink.engine.message.MessageStructurePool;
import nebsoa.spiderlink.engine.message.MessageStructureUtil;
/*******************************************************************
 * <pre>
 * 1.설명 
 * 대응답 데이터 처리 유틸
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
 * $Log: ProxyResponseMessageUtil.java,v $
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
 * Revision 1.3  2007/12/28 05:49:02  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2007/12/21 06:31:03  김승희
 * *** empty log message ***
 *
 * Revision 1.1  2007/12/18 08:30:09  김승희
 * 최초 등록
 *
 *
 * </pre>
 ******************************************************************/
public class ProxyResponseMessageUtil {
	
	public static String ROOT_TAG = "PROXY";
	
	public static String PROXY_RES_TYPE_REAL = "R"; //대응답 데이터 타입 - 실데이터 형식
	
	public static String PROXY_RES_TYPE_MAPPING_XML = "M"; //대응답 데이터 타입 - 필드명 맵핑 XML
	
	private ProxyResponseMessageUtil(){}
	
	/**
	 * 대응답 데이터를 DataMap에 필드명=value로 셋팅합니다.
	 * @param messageContext
	 * @param dataMap
	 */
	public static DataMap populateProxyData(MessageContext messageContext, DataMap dataMap){
		String resData = null;
		try {
			resData = getProxyDataString(messageContext);
			
			MessageStructure messageStructure = MessageStructurePool.getInstance().getMessageStructure(
					MessageStructureUtil.getMessageStructureId(messageContext.getResMessageId(), messageContext.getOrgId()));
			parseProxyData(messageStructure, resData, dataMap);
						
		}catch(Throwable e){
			throw new SysException("대응답데이터 추출에 오류가 발생하였습니다.(" + e +")");
		}
		return dataMap;
	}

	/**
	 * MessageContext로 부터 대응답데이터를 리턴한다.
	 * MessageContext에 저장된 대응답데이터가 byte[] 타입인 경우 압축된 데이터로 간주하여 압축을 풀어서 리턴한다.
	 * 
	 * @param messageContext
	 * @return
	 * @throws IOException
	 */
	public static Object getProxyData(MessageContext messageContext) throws IOException {
		Object resDataObj = messageContext.getProxyResData();
		
		if(resDataObj==null){
			throw new SysException("대응답데이터가 존재하지 않습니다.");
			
		}else if(resDataObj instanceof byte[]){
			return IOUtils.decompress((byte[])resDataObj);
			
		}else{
			return resDataObj;
		}
	}
		
	/**
	 * 대응답데이터를 문자열로 만들어 리턴한다.
	 * @param messageContext
	 * @return
	 * @throws IOException
	 */
	private static String getProxyDataString(MessageContext messageContext) throws IOException {
		Object resDataObj = getProxyData(messageContext);
		
		if(resDataObj instanceof String){
			return (String)resDataObj;
			
		}else if(resDataObj instanceof byte[]){
			return new String((byte[])resDataObj);
			
		}else{
			return resDataObj.toString();
		}
	}
	
	/**
	 * 대응답 데이터 문자열을 파싱하여 DataMap에 담는다.
	 * @param messageStructure
	 * @param proxyData
	 * @param dataMap
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private static void parseProxyData(MessageStructure messageStructure, String proxyData, DataMap dataMap) throws ParserConfigurationException, SAXException, IOException{
		
		ProxyXMLMessageParseHandler parseHandler = new ProxyXMLMessageParseHandler(dataMap, messageStructure, ROOT_TAG);
		parseHandler.doParse(proxyData);

	}
	
	/**
	 * 대응답데이터 여부를 리턴한다.
	 * @param messageContext
	 * @return
	 */
	public static boolean isProxy(MessageContext messageContext){
		if("Y".equals(messageContext.getProxyResYn()) )return true;
		return false;
	}
	
	/**
	 * 대응답데이터 타입이 Mapping XML 형식인지 여부를 리턴한다.
	 * @param messageContext
	 * @return
	 */
	public static boolean isProxyTypeMappingXML(MessageContext messageContext){
		return ProxyResponseMessageUtil.PROXY_RES_TYPE_MAPPING_XML.equals(messageContext.getTrxMessage().getProxyResType());
	}
	
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException{
		MessageStructure messageStructure = MessageStructurePool.getInstance().getMessageStructure(
				MessageStructureUtil.getMessageStructureId("테스트거래_RES", "KSH"));
		DataMap dataMap = new DataMap();
		StringBuffer sb = new StringBuffer();
		sb.append("<PROXY>");
		sb.append("<USER_ID>김승희</USER_ID>");
		sb.append("<_BeginLoop_List>");
		sb.append("<거래금액>20000</거래금액>");
		sb.append("<거래유형코드>1</거래유형코드>");
		sb.append("<결과메세지>정상 처리되었습니다.</결과메세지>");
		sb.append("</_BeginLoop_List>");
		sb.append("<_BeginLoop_List>");
		sb.append("<거래금액>30000</거래금액>");
		sb.append("<거래유형코드>2</거래유형코드>");
		sb.append("<결과메세지>정상 처리되었습니다.</결과메세지>");
		sb.append("</_BeginLoop_List>");
		sb.append("</PROXY>");
			
		
		String str = sb.toString();
		
		parseProxyData(messageStructure, str, dataMap);
		System.out.println(dataMap);
	}
}
