/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.engine.parser;

import nebsoa.common.log.LogManager;
import nebsoa.common.util.DataMap;
import nebsoa.common.util.StringUtil;
import nebsoa.spiderlink.engine.message.ByteMessageInstance;
import nebsoa.spiderlink.engine.message.MessageParseException;
import nebsoa.spiderlink.engine.message.MessageStructure;

import org.apache.commons.lang.StringUtils;

/*******************************************************************
 * <pre>
 * 1.설명 
 * MessageStructure에 따라 XML message를 파싱하여 DataMap의 key=value 형태로 변환한다.
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
 * $Log: XMLMessageParser.java,v $
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
 * Revision 1.2  2008/03/12 04:54:50  김승희
 * <루트태그 /> 인 경우 오류 수정
 *
 * Revision 1.1  2008/01/22 05:58:19  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:00  안경아
 * *** empty log message ***
 *
 * Revision 1.19  2007/02/06 05:48:13  김성균
 * DEBUG 출력시 비밀번호관련 XML데이타 마스킹 처리
 *
 * Revision 1.18  2006/10/19 14:06:43  이종원
 * *** empty log message ***
 *
 * Revision 1.17  2006/07/07 11:21:46  김승희
 * stackTrace 삭제
 *
 * Revision 1.16  2006/07/05 07:12:05  김승희
 * 소스 정리
 *
 * Revision 1.15  2006/06/21 01:45:05  이종원
 * 코드 정리
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
public class XMLMessageParser implements MessageParser {

	private static XMLMessageParser messageParser = new XMLMessageParser();

    private XMLMessageParser() {
    }

    public static XMLMessageParser getInstance() {
        return messageParser;
    }
    
	public DataMap parse(MessageStructure structure, ByteMessageInstance instance, DataMap dataMap) {
		LogManager.debug("********XMLMessageParser parse()***********");
		
		if(StringUtil.isNull(structure.getXmlRootTag()))
			throw new MessageParseException(structure.getStructureId() + "의 XML ROOT TAG가 지정되지 않았습니다");
		
		StringBuffer endRootBuffer = new StringBuffer().append("</").append(structure.getXmlRootTag());
		String endRootTagClip = endRootBuffer.toString(); //마지막 '>'가 없는 end root tag (예) </ROOT_TAG
		String endRootTag = endRootBuffer.append(">").toString();
		
		try{
			
			XMLMessageParseHandler handler = new XMLMessageParseHandler(dataMap, structure);
			//ByteMessageInstance byteMessageInstance = (ByteMessageInstance)instance;
			
			//XML 파싱을 위해서 앞뒤에 붙은 다른 포맷 데이터를 잘라낸다.
			String message = new String(instance.getBytes(), instance.getOffset(), instance.getBytes().length-instance.getOffset());
			
			//XML문서 안에 "</루트태그" 가 없는 경우, 즉 내용이 오직 <루트태그 />인 경우 <루트태그 />를 <루트태그></루트태그>로 치환한다.
			if(message.indexOf(endRootTagClip)==-1){
				String regex = "(<" + structure.getXmlRootTag() + "\\s*?/>)";
								
				String startTag = new StringBuffer().append("<").append(structure.getXmlRootTag()).append(">").toString();
				message = message.replaceAll(regex, startTag + endRootTag);

			}
			
			message = StringUtils.substringBefore(message, endRootTagClip) + endRootTag;
            
			String replace_xmlContents = message;
            if (!StringUtil.isNull(message) && LogManager.isDebugEnabled()) {
                replace_xmlContents = StringUtil.maskXml(replace_xmlContents, "password>");
                replace_xmlContents = StringUtil.maskXml(replace_xmlContents, "pass>");
                replace_xmlContents = StringUtil.maskXml(replace_xmlContents, "pwd>");
                replace_xmlContents = StringUtil.maskXml(replace_xmlContents, "비밀번호>");
            }
			LogManager.debug("XML 파싱 전 message :" + replace_xmlContents);

            handler.doParse(message);
            // LogManager.debug("XML 파싱 후 message :" + handler.getDataMap());
            return handler.getDataMap();

        } catch (Throwable e) {
            // e.printStackTrace();
            throw new MessageParseException("XML Parsing 중 오류가 발생하였습니다. : " + e);
        }
    }
    
}
