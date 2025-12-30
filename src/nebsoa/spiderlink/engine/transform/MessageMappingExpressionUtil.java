/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.engine.transform;

import org.apache.commons.lang.StringUtils;

import nebsoa.common.script.ScriptEngine;
import nebsoa.common.script.ScriptEngineFactory;
import nebsoa.common.script.ScriptEngineType;
import nebsoa.common.util.StringUtil;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 메시지 변환 관련 유틸리티 클래스 
 * 
 * 2.사용법
 * 멤버변수 scriptEngine의 타입을 바꾸어 다른 스트립팅엔진을 사용할 수 있다.
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
 * $Log: MessageMappingExpressionUtil.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:35  cvs
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
 * Revision 1.1  2008/01/22 05:58:21  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:39:05  안경아
 * *** empty log message ***
 *
 * Revision 1.5  2006/06/17 10:12:09  김승희
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public class MessageMappingExpressionUtil {
	
	public static final String FUNCTION_PREFIX = "=";
	private static ScriptEngineFactory scriptEngineFactory = ScriptEngineFactory.getInstance();
	private static ScriptEngine scriptEngine = scriptEngineFactory.getScriptEngine(ScriptEngineType.BSH);
	
	private MessageMappingExpressionUtil(){}
	
	public static boolean isFunction(String mappingExpr){
		if(mappingExpr==null) return false;
		if(mappingExpr.trim().startsWith(FUNCTION_PREFIX)){
			return true;
		}else{
			return false;
		}
	}
	
	public static Object exec(String expression){
		expression = StringUtils.substringAfter(expression, FUNCTION_PREFIX);
		return scriptEngine.eval(expression);
	}
	
		
	public static String getMappingFieldName(String mappingExpr){
		
		if(StringUtil.isNull(mappingExpr)){
			throw new MessageTransformException("mapping expression이 존재하지 않습니다.");
		}
		
		mappingExpr = mappingExpr.trim();
		return StringUtils.substringBetween(mappingExpr, "{", "}"); 
					
	}
	
	public static String[] getMappingFieldNames(String mappingExpr){
		mappingExpr = StringUtils.substringBeforeLast(mappingExpr, "}");
		
		String[] tokens = StringUtils.split(mappingExpr, "}");
		String[] fieldNames = new String[tokens.length];
		for(int i=0; i<tokens.length; i++){
			fieldNames[i] = getMappingFieldName(tokens[i]+"}");
		}
		return fieldNames;
	}
	
	public static void main(String[] args){
		String expression = "=2222222222+3333333333333+44444444444";
		String[] returnStr = getMappingFieldNames(expression);
		
		for(int i=0; i<returnStr.length; i++){
			System.out.println(i+":" +returnStr[i]);
		}
	}
}	
