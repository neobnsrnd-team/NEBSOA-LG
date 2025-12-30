/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.engine.parser;

import java.lang.reflect.Method;

import nebsoa.common.exception.SysException;
import nebsoa.common.util.DataMap;
import nebsoa.common.util.MethodInvoker;
import nebsoa.common.util.PropertyManager;
import nebsoa.common.util.StringUtil;
import nebsoa.spiderlink.engine.message.MessageType;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 메시지 타입에 따라 Message Generator를 생성해주는 Factory 클래스
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
 * $Log: MessageGeneratorFactory.java,v $
 * Revision 1.1  2018/01/15 03:39:49  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:38  cvs
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
 * Revision 1.6  2007/01/25 05:11:32  김승희
 * spiderlink에서 paser/generator 찾을 때 postfix를 사용하도록 수정
 *
 * Revision 1.5  2007/01/22 08:40:14  김승희
 * spiderlink.properties에서 parser/generator 클래스명 읽어와서 생성가능 하도록 수정
 *
 * Revision 1.4  2006/08/02 09:34:04  김승희
 * ManagementObject  적용에 따른 변경
 *
 * Revision 1.3  2006/06/17 10:12:09  김승희
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public class MessageGeneratorFactory {
	
	private static final String POSTFIX = "_Generator";
	
	public static MessageGenerator getMessageGenerator(MessageType messageType) {
		
		if(messageType.getType().equals(MessageType.FIXED_LENGTH_STRING)){
    		return FixedLengthMessageGenerator.getInstance();
    	}else if(messageType.getType().equals(MessageType.XML_STRING)){
    		return XMLMessageGenerator.getInstance();
    	}else if(messageType.getType().equals(MessageType.MAP_STRING)){
    		return DelimitedMapGenerator.getInstance();
    	}else{
    		String messageGenerator = PropertyManager.getProperty("spiderlink", messageType.getType()+POSTFIX, null);
    		if(StringUtil.isNull(messageGenerator)){
    			throw new SysException("지원되지 않는 Message Type입니다. ["+messageType.getType()+"]");
    		}else{
    			try{
	    			Class messageGeneratorClass = Class.forName(messageGenerator);
	    			Method method = messageGeneratorClass.getMethod("getInstance", null);
	    			return (MessageGenerator)method.invoke(messageGeneratorClass, null);
    			}catch(Exception e){
    				throw new SysException("MessageGenerator 생성에 실패했습니다.["+messageType.getType()+"], 원인:" + e);
    			}
    			
    		}
    	}
		
		/*if(messageType==MessageType.FIXED_LENGTH){
    		return FixedLengthMessageGenerator.getInstance();
    	}else if(messageType==MessageType.XML){
    		return XMLMessageGenerator.getInstance();
    	}else if(messageType==MessageType.MAP){
    		return DelimitedMapGenerator.getInstance();
    	}else{
    		throw new SysException("지원되지 않는 Message Type입니다. ["+messageType.getType()+"]");
    	}*/
		
	}
}
