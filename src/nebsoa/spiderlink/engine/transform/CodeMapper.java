/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.engine.transform;

import java.util.ArrayList;

import nebsoa.common.collection.DataSet;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.DataMap;
import nebsoa.common.util.StringUtil;
import nebsoa.spiderlink.engine.message.InvalidMappingCodeException;
import nebsoa.spiderlink.engine.message.LoopField;
import nebsoa.spiderlink.engine.message.MessageCode;
import nebsoa.spiderlink.engine.message.MessageCodePool;
import nebsoa.spiderlink.engine.message.MessageConstants;
import nebsoa.spiderlink.engine.message.MessageField;
import nebsoa.spiderlink.engine.message.MessageStructure;
import nebsoa.spiderlink.engine.message.MessageStructurePool;
import nebsoa.spiderlink.engine.message.MessageStructureUtil;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 표준<->기관 상호 간 코드 매핑을 처리한다.
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
 * $Log: CodeMapper.java,v $
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
 * Revision 1.2  2008/07/25 05:14:15  김은정
 * codemapping로직 수행시 value값이 있는 경우만 수행하도록 수정.
 *
 * Revision 1.1  2008/01/22 05:58:21  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.3  2007/12/24 09:00:08  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2007/12/21 05:10:55  김승희
 * 루프 데이터 맵핑 버그 수정
 *
 * Revision 1.1  2007/12/18 08:30:09  김승희
 * 최초 등록
 *
 *
 * </pre>
 ******************************************************************/
public class CodeMapper {
		
	
	/**
	 * 기관코드로 바꾼 데이터맵을 리턴한다.
	 * @param messageId
	 * @param orgId
	 * @param dataMap
	 * @return DataMap
	 * @throws MessageTransformException
	 */
	public static DataMap mapToOrgCode(String messageId, String orgId, DataMap dataMap) throws MessageTransformException {
		
		MessageStructure messageStructure = MessageStructurePool.getInstance().getMessageStructure(MessageStructureUtil.getMessageStructureId(messageId, orgId));
		doMapping(messageStructure, dataMap, false);
		
		return dataMap;

	}
	
	/**
	 * 표준코드로 바꾼 데이터맵을 리턴한다.
	 * @param messageId
	 * @param orgId
	 * @param dataMap
	 * @return DataMap
	 * @throws MessageTransformException
	 */
	public static DataMap mapToStdCode(String messageId, String orgId, DataMap dataMap) throws MessageTransformException {
		
		MessageStructure messageStructure = MessageStructurePool.getInstance().getMessageStructure(MessageStructureUtil.getMessageStructureId(messageId, orgId));
		doMapping(messageStructure, dataMap, true);
		
		return dataMap;

	}

	private static void doMapping(MessageStructure messageStructure, DataMap dataMap, boolean isToSTD) {
		if(messageStructure.getParentId()!=null && !"".equals(messageStructure.getParentId())){
			doMapping(messageStructure.getParent(), dataMap, isToSTD);
		}
				
		MessageField messageField = null;
		for(int i=0; i<messageStructure.size(); i++){
			
			messageField = messageStructure.getField(i);
			if(messageField==null) continue;
		
			//반복필드가 있을 때 
			if(messageField instanceof LoopField){
				dataMap.put(messageField.getName(), 
							doLoopFieldMapping(messageStructure, dataMap, (LoopField)messageField, isToSTD));
			//더미 필드, END LOOP 제외
			}else if(!MessageField.DUMMY_USE.equals(messageField.getUseMode()) && !messageField.getName().startsWith(MessageField.END_LOOP)){
				dataMap.put(messageField.getName(), 
						doFieldMapping(messageStructure, dataMap, messageField, isToSTD));
			}

		}
	}
	
	private static Object doLoopFieldMapping(MessageStructure messageStructure, DataMap dataMap, LoopField messageField, boolean isToSTD){
		
		//_BeginLoop_ 자체 처리
		Object LoopFieldName = doFieldMapping(messageStructure, dataMap, messageField, isToSTD);
		DataSet dataSet = (DataSet)dataMap.get(LoopFieldName);
		
		int loopCnt = dataSet==null?0:dataSet.getRowCount();
		
		ArrayList nestedFieldList = messageField.getChildList();
		
		//looping 되는 row의 갯수 만큼만 일단  매핑한다.(건수가 fixed 된 경우는 marshall 시에 고려 한다..)
		//필드 이름 array
		String[] nestedFieldNameArray = new String[messageField.getChildCount()];
				
		MessageField nestedField = null;
		ArrayList newNestedFieldList = new ArrayList(loopCnt);
		Object[] loopFieldRow = null; 
				
		if(dataSet!=null){
			dataSet.beforeFirst();
			DataMap oneRowDataMap = new DataMap();
			boolean isFirst = true;
			while(dataSet.next()){
				loopFieldRow = new Object[messageField.getChildCount()];
				dataSet.toDataMap(oneRowDataMap);
				
				for(int i=0; i<messageField.getChildCount(); i++){
					nestedField = (MessageField)nestedFieldList.get(i);
					
					if(isFirst){
						nestedFieldNameArray[i] = nestedField.getName();
					}

					if(nestedField instanceof LoopField){
						loopFieldRow[i] = doLoopFieldMapping(messageStructure,	oneRowDataMap, (LoopField)nestedField, isToSTD);
					}else{
						loopFieldRow[i] = doFieldMapping(messageStructure, oneRowDataMap, nestedField, isToSTD);
					}
				}
				newNestedFieldList.add(loopFieldRow);
				isFirst = false;
			}
			return new DataSet(nestedFieldNameArray, newNestedFieldList);
		}else{
			return null;
		}

	}

	/**
	 * 매핑 코드 값을 리턴한다.
	 * 소스 기관이 표준이거나 타겟 기관이 표준일 때만 가능한다.
	 * @param srcOrgId 소스 기관 ID
	 * @param trgOrgId 타겟 기관 ID
	 * @param field 코드 메시지 필드
	 * @param mappingValue 코드값
	 * @return String 매핑된 코드 (표준 코드 또는 특정 기관 코드)
	 */
	public static String getMappingCodeValue(String srcOrgId, String trgOrgId, MessageField field, String mappingValue) {
		String transformedValue = null;
		//source org id 가 STD이면 
		if(srcOrgId.equals(MessageConstants.ORG_STD)){
			
			MessageCode mCode = MessageCodePool.getInstance().getOrgCode(trgOrgId, field.getCodeGroup(), mappingValue);
			LogManager.debug(field.getName() + "의 변환전 코드값 :" + mappingValue + ", 변환후 코드값 :" + (mCode==null?"없음":mCode.getOrgCode()));
			
			//해당하는 코드 값이 없을 때 exception 발생
			if(mCode==null){
				throw new InvalidMappingCodeException("기관ID [" + MessageConstants.ORG_STD +"] 코드 [" + mappingValue +"]에 해당하는 기관 ID [" + trgOrgId +"]의 코드가 없습니다.");
			}
			transformedValue = mCode.getOrgCode();
			/*//해당하는 코드 값이 없을 때는 원래 값을 그대로 넣는다.
			transformedValue = mCode==null?mappingValue:mCode.getOrgCode();*/

		//target org id 가 STD이면 
		}else if(trgOrgId.equals(MessageConstants.ORG_STD)){
			MessageCode mCode = MessageCodePool.getInstance().getStdCode(srcOrgId, field.getCodeGroup(), mappingValue);
			LogManager.debug(field.getName() + "의 변환전 코드값 :" + mappingValue + ", 변환후 코드값 :" + (mCode==null?"없음":mCode.getCode()));
			if(mCode==null){
				throw new InvalidMappingCodeException("기관ID [" + srcOrgId +"] 코드 [" + mappingValue +"]에 해당하는 기관 ID [" + MessageConstants.ORG_STD +"]의 코드가 없습니다.");
			}
			transformedValue = mCode.getCode();
			/*transformedValue = mCode==null?mappingValue:mCode.getCode();*/
		}else{
			throw new MessageTransformException("지원되지 않는 기간관 코드 변환입니다. 코드 변환은 표준코드<-->기관코드만 가능합니다.");
		}
		
		return transformedValue;
	}
	
	private static Object doFieldMapping(MessageStructure messageStructure, DataMap dataMap, MessageField field, boolean isToSTD){	
		
		String value = dataMap.getString(field.getName());
		//반복 필드 일 때는 이름을 리턴한다.
		if(field instanceof LoopField){
			return field.getName();

		//코드 필드이면서 코드변환여부가 true인 경우 경우 
	    }else if(field.isCodeField() && field.isCodeMapping() && !StringUtil.isNull(value)){
			Object transformedValue = null;
			
			if(isToSTD){
				transformedValue = getMappingCodeValue(messageStructure.getOrgId(), MessageConstants.ORG_STD, field, dataMap.getString(field.getName()));
			}else{
				transformedValue = getMappingCodeValue(MessageConstants.ORG_STD, messageStructure.getOrgId(), field, dataMap.getString(field.getName()));
			}
			LogManager.debug("CODE 필드 [" + field.getName() +"] 값 :" + transformedValue);
			
			return transformedValue;
			
		}else{
			
			return dataMap.get(field.getName());
			
		}

	}

}
