/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.engine.parser;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import nebsoa.common.util.DataMap;
import nebsoa.common.util.StringUtil;
import nebsoa.spiderlink.engine.message.MessageConstants;
import nebsoa.spiderlink.engine.message.MessageField;
import nebsoa.spiderlink.engine.message.MessageStructure;
import nebsoa.spiderlink.engine.message.MessageValueSetter;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

/*******************************************************************
 * <pre>
 * 1.설명 
 * FreeMaker custome method 클래스
 * XML 전문 템플릿 생성 시 사용한다.
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
 * $Log: FreeMarkerMapMethod.java,v $
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
 * Revision 1.2  2007/12/28 08:55:27  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2007/11/26 08:38:00  안경아
 * *** empty log message ***
 *
 * Revision 1.11  2007/07/16 03:46:54  김승희
 * Nested Loop, CDATA Section 처리 관련 변경
 *
 * Revision 1.10  2007/02/01 07:32:41  김승희
 * DataMap에서 꺼낸 데이터를 String으로 Casting-->toString()으로 수정
 *
 * Revision 1.9  2006/11/10 05:47:46  김승희
 * import 정리
 *
 * Revision 1.8  2006/10/28 03:31:26  김승희
 * *** empty log message ***
 *
 * Revision 1.7  2006/08/30 11:23:41  김승희
 * 디폴트 값을 DataMap에도 넣어주도록 수정
 *
 * Revision 1.6  2006/07/21 06:03:46  김승희
 * import 정리
 *
 * Revision 1.5  2006/07/21 05:59:16  김승희
 * remark관련 처리
 *
 * Revision 1.4  2006/06/17 10:12:09  김승희
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public class FreeMarkerMapMethod implements TemplateMethodModelEx  {
	
	protected DataMap dataMap;
	protected MessageStructure structure; 
	
	/**
	 * 로그를 남길 때 원래 값 대신 remark 문자열을 남길 것인지 여부
	 */
	protected boolean isRemarkApplied = false;
	
	public FreeMarkerMapMethod(DataMap dataMap, MessageStructure structure){
		this(dataMap, structure, false);
	}
	
	public FreeMarkerMapMethod(DataMap dataMap, MessageStructure structure, boolean isRemarkApplied){
		this.dataMap = dataMap;
		this.structure = structure;
		this.isRemarkApplied = isRemarkApplied;
	}
	
	public boolean isRemarkApplied() {
		return isRemarkApplied;
	}

	public void setRemarkApplied(boolean isRemarkApplied) {
		this.isRemarkApplied = isRemarkApplied;
	}

	public FreeMarkerMapMethod(){}
	
	public Object exec(List args) throws TemplateModelException {
		
		String retValueStr = null;
		String fieldName = ((SimpleScalar)args.get(0)).toString();
		MessageField field = structure.getFieldByName(fieldName);

		//일반 데이터의 경우
		//args[0] : 필드명
		if(args.size()==1){
			
			//ClassCaseException 방지를 위해 아래와 같이 수정
			//retValueStr = (String)dataMap.get(fieldName);
			
			Object retValue = dataMap.get(fieldName);
			
			retValueStr = retValue==null?"":retValue.toString();
			
	    //loop의 데이터의 경우
		//args[0] : 필드명,  args[1] : 부모(loop Field) alias
		}else{

			SimpleHash simpleHash = (SimpleHash)args.get(1);
			Object retValue = simpleHash.get(fieldName);
			retValueStr = retValue==null?"":retValue.toString();
		}
		
		//디폴트 값 처리
		//입력값 검사에서 이미 dataMap에 상위 전문까지의 디폴트값을 가지고 있으므로 주석처리..
		retValueStr = getSystemDefaultValue(field, retValueStr);

		//Remark가 적용될 경우..
		if(this.isRemarkApplied){
			retValueStr = getValueForLog(field, retValueStr);
		}

		//null 처리
		if("null".equals(retValueStr) || StringUtil.isNull(retValueStr)) retValueStr = "";
		
		//CDATA처리
		//어트리뷰트의 경우는 필드 타입이 CDATA이어도 <![CDATA[ 를 붙이지 않는다.
		if(StringUtil.NVL(field.getFieldTag()).indexOf("@")==-1 
				&& MessageField.CDATA.equals(field.getFieldType())){
			return new StringBuffer(MessageConstants.CDATA_START).append(retValueStr).append(MessageConstants.CDATA_END);
		}else{
			return retValueStr;
		}
	}
	
	private String getSystemDefaultValue(MessageField field, String value){
    	    	
    	return MessageValueSetter.getDefaultValue(field, value, dataMap);
	}
	
	//remark 값으로 로그용 value값을 리턴한다.
	private String getValueForLog(MessageField field, String value){
		//remark가 null이 아니고 value가 null이 아니면 value의 길이만큼 remark를 반복한 값을 리턴한다.
		if(!StringUtil.isNull(field.getRemark())&&!StringUtil.isNull(value)){
			return StringUtils.repeat(field.getRemark(), value.length());
		}else{
			return value;
		}
	}

}
