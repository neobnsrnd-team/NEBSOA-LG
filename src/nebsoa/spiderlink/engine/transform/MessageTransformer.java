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

import org.apache.commons.lang.StringUtils;

import nebsoa.common.collection.DataSet;
import nebsoa.common.exception.InvalidNullException;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.DataMap;
import nebsoa.common.validator.NotNullValidator;
import nebsoa.spiderlink.engine.message.LoopField;
import nebsoa.spiderlink.engine.message.MessageField;
import nebsoa.spiderlink.engine.message.MessageFieldMapping;
import nebsoa.spiderlink.engine.message.MessageFieldMappingMap;
import nebsoa.spiderlink.engine.message.MessageStructure;
import nebsoa.spiderlink.engine.message.MessageStructurePool;
import nebsoa.spiderlink.engine.message.MessageStructureUtil;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 매핑정보에 따라 표준 전문과 기관 전문 상호간 전문을 변환한다.
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
 * $Log: MessageTransformer.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:34  cvs
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
 * Revision 1.4  2007/12/21 05:10:55  김승희
 * 루프 데이터 맵핑 버그 수정
 *
 * Revision 1.3  2007/12/21 02:28:55  김승희
 * 루프 데이터 맵핑 버그 수정
 *
 * Revision 1.2  2007/12/18 08:30:24  김승희
 * CodeMapper로 일부 로직 이동
 *
 * Revision 1.1  2007/11/26 08:39:05  안경아
 * *** empty log message ***
 *
 * Revision 1.14  2007/03/07 06:40:40  안경아
 * *** empty log message ***
 *
 * Revision 1.13  2007/01/12 09:29:49  안경아
 * *** empty log message ***
 *
 * Revision 1.12  2006/10/17 07:16:10  이종원
 * Not null check
 *
 * Revision 1.11  2006/07/10 07:39:22  김승희
 * InvalidMappingCodeException 로 변경
 *
 * Revision 1.10  2006/07/07 05:33:12  김승희
 * 예외 처리 관련 수정
 *
 * Revision 1.9  2006/07/04 09:31:29  김승희
 * 패키지 변경
 *
 * Revision 1.8  2006/07/04 08:39:03  김승희
 * 참조 클래스 패키지 변경
 *
 * Revision 1.7  2006/07/03 11:19:24  김승희
 * 매핑코드 없을 때 에러 발생
 *
 * Revision 1.6  2006/06/27 05:15:50  김승희
 * 불필요한 log 삭제
 *
 * Revision 1.5  2006/06/26 02:03:50  김승희
 * END LOOP 관련 처리
 *
 * Revision 1.4  2006/06/21 05:11:13  김승희
 * "STD" --> OrgConstants.STD 변경
 *
 * Revision 1.3  2006/06/17 10:12:09  김승희
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public class MessageTransformer {
	
	
	/**
	 * 메시지를 변환하여 DataMap의 형태로 리턴한다.
	 * @param targetMessageId 타겟 메시지 ID
	 * @param targetOrgId 타겟 기관 ID
	 * @param srcMessageId 소스 메시지 ID
	 * @param srcOrgId 소스 기관 ID
	 * @param dataMap 원본 Datamap
	 * @return 변환된 값이 들어 있는 DataMap
	 * @throws MessageTransformException
	 */
	public static DataMap transform(String targetMessageId, String targetOrgId, String srcMessageId, 
			String srcOrgId, DataMap dataMap) throws MessageTransformException {
		String srcStructureId = MessageStructureUtil.getMessageStructureId(srcMessageId, srcOrgId);
		String trgStructureId = MessageStructureUtil.getMessageStructureId(targetMessageId, targetOrgId);
		LogManager.info(" ★★" + srcStructureId +" 메시지를 " + trgStructureId +"로 변환합니다.");
		
		MessageStructure srcStructure = MessageStructurePool.getInstance().getMessageStructure(srcStructureId);
		MessageStructure trgStructure = MessageStructurePool.getInstance().getMessageStructure(trgStructureId);
		
		//target 메시지에 맞는 키구조를 셋팅한다.
		DataMap newDataMap = MessageStructureUtil.constructDataMap(dataMap, trgStructure);
		
		//DataMap을 복제한다.
		//DataMap newDataMap = (DataMap)dataMap.clone();
				
		//현재 처리할 실제 Message Strucutre의 맵핑 정보를 구한다.-이 안에 부모메시지의 매핑 정보도 전부 들어있다.
		MessageFieldMappingMap fieldMappings = trgStructure.getFieldMappingMap();
		try {
            transformMessage(targetMessageId, srcStructure, trgStructure, dataMap, newDataMap, fieldMappings);
        } catch (InvalidNullException e) {
            LogManager.error(e.getMessage(),e);
            throw new MessageTransformException(e.getMessage());
        }
		
		return newDataMap;
	}
	
	private static void transformMessage(
            String baseTrgMessageID, 
            MessageStructure srcStructure, 
            MessageStructure trgStructure, 
            DataMap originalDataMap, 
            DataMap transformedDataMap, 
			MessageFieldMappingMap fieldMappings) throws InvalidNullException{
		
        NotNullValidator.validate(baseTrgMessageID,"변환 Target전문 ID is null");
        NotNullValidator.validate(srcStructure,"변환 Source 전문 정보 is null");
        NotNullValidator.validate(trgStructure,"변환 Target전문 정보 is null");
        NotNullValidator.validate(originalDataMap,"변환 Source DataMap is null");
        NotNullValidator.validate(transformedDataMap,"변환 Target DataMap is null");
        
		if(trgStructure.getParentId()!=null){
			transformMessage(baseTrgMessageID, srcStructure, trgStructure.getParent(), originalDataMap, transformedDataMap, fieldMappings);
		}
				
		MessageField messageField = null;
		for(int i=0; i<trgStructure.size(); i++){
			
			messageField = trgStructure.getField(i);
			if(messageField==null) continue;
			
			//LogManager.debug(" ★변환 타겟 필드 " + trgStructure.getStructureId() + " ["+i+"] : " + messageField.getName());
			//반복필드가 있을 때 
			if(messageField instanceof LoopField){
				transformedDataMap.put(messageField.getName(), tranformLoopMessage(baseTrgMessageID, fieldMappings, 
						originalDataMap, (LoopField)messageField, transformedDataMap, srcStructure, trgStructure));
			//더미 필드, END LOOP 제외
			}else if(!MessageField.DUMMY_USE.equals(messageField.getUseMode()) && !messageField.getName().startsWith(MessageField.END_LOOP)){
				transformedDataMap.put(messageField.getName(), tranformMessage(baseTrgMessageID, fieldMappings, 
						originalDataMap, messageField, transformedDataMap, srcStructure, trgStructure));
			}

		}

	}
	/**
	 * Loop field의 메시지를 변환한다.
	 * Loop field의 child field의 변환 까지 수행한다.
	 * 
	 * @param baseTrgMessageID 기본 타겟 메시지 ID
	 * @param fieldMappings MessageFieldMappingMap 타겟 메시지의 필드 매핑 정보 
	 * @param dataMap 원본 DataMap 
	 * @param messageField LoopField
	 * @param transformedDataMap 변환된 메시지가 담길 DataMap
	 * @param srcStructure 소스 메시지 Structure
	 * @param trgStructure 타겟 메시지 Structure
	 * @return 반복 데이터를 가지고 있는 Object(DataSet)
	 */
	private static Object tranformLoopMessage(String baseTrgMessageID, MessageFieldMappingMap fieldMappings,
			DataMap dataMap, LoopField messageField, DataMap transformedDataMap, MessageStructure srcStructure, MessageStructure trgStructure){
				
		//_BeginLoop_먼저 처리
		Object srcLoopFieldName = tranformMessage(baseTrgMessageID, fieldMappings, dataMap, messageField, transformedDataMap, srcStructure, trgStructure);
		DataSet dataSet = (DataSet)dataMap.get(srcLoopFieldName);
		
		int loopCnt = dataSet==null?0:dataSet.getRowCount();
		//LogManager.debug(messageField.getName() +" 반복 필드 시작 [총 "+loopCnt+"건] :: ");

		ArrayList nestedFieldList = messageField.getChildList();
		
		//looping 되는 row의 갯수 만큼만 일단  매핑한다.(건수가 fixed 된 경우는 marshall 시에 고려 한다..)
		//필드 이름 array
		String[] nestedFieldNameArray = new String[messageField.getChildCount()];
				
		MessageField nestedField = null;
		ArrayList newNestedFieldList = new ArrayList(loopCnt);
		Object[] loopFieldRow = null; 
		MessageFieldMapping messageFieldMapping = null;
		
		if(dataSet!=null){
			dataSet.beforeFirst();
			DataMap oneRowDataMap = new DataMap();
			boolean isFirst = true;
			while(dataSet.next()){
				loopFieldRow = new Object[messageField.getChildCount()];
				dataSet.toDataMap(oneRowDataMap);
				
				for(int i=0; i<messageField.getChildCount(); i++){
					nestedField = (MessageField)nestedFieldList.get(i);
					messageFieldMapping = fieldMappings.get(srcStructure.getOrgId(), trgStructure.getOrgId(), baseTrgMessageID, nestedField.getName());
					if(isFirst){
						nestedFieldNameArray[i] = nestedField.getName();
						
					}

					if(nestedField instanceof LoopField){
						
						loopFieldRow[i] = tranformLoopMessage(baseTrgMessageID, fieldMappings,
								oneRowDataMap, (LoopField)nestedField, transformedDataMap, srcStructure, trgStructure);
					}else{
						loopFieldRow[i] = tranformMessage(messageFieldMapping, oneRowDataMap, nestedField, transformedDataMap, srcStructure, trgStructure);
						
					}
				}
				newNestedFieldList.add(loopFieldRow);
				isFirst = false;
			}
			return new DataSet(nestedFieldNameArray, newNestedFieldList);
		}else{
			return null;
		}
		
		//transformedDataMap.put(messageField.getName(), newDataSet);
		
		
	}
	
	private static Object tranformMessage(String baseTrgMessageID, MessageFieldMappingMap fieldMappings, 
											DataMap dataMap, MessageField field, DataMap transformedDataMap, 
											MessageStructure srcStructure, MessageStructure trgStructure){
		
		MessageFieldMapping messageFieldMapping = fieldMappings.get(srcStructure.getOrgId(), trgStructure.getOrgId(), baseTrgMessageID, field.getName());
		return tranformMessage(messageFieldMapping, dataMap, field, transformedDataMap, srcStructure, trgStructure);
	}
	
	private static Object tranformMessage(MessageFieldMapping messageFieldMapping, 
										  DataMap dataMap, MessageField field, DataMap transformedDataMap, 
										  MessageStructure srcStructure, MessageStructure trgStructure){	
		Object transformedValue = null;		

		//1. 매핑정보가 없을 때
		//TODO 매핑정보가 없으면 source datamap에 있는 key가 동일한 값을 그대로 사용
		if(messageFieldMapping==null){
			
			/*
			 * 2007.12.17 ksh 수정 (중복 코드 제거) 
			//코드 필드의 경우 
			if(field.isCodeField()){
				//2007.12.17 ksh 수정
				//transformedValue = getMappingCodeValue(srcStructure, trgStructure, field, dataMap.getString(field.getName()));
				transformedValue = CodeMapper.getMappingCodeValue(srcStructure.getOrgId(), trgStructure.getOrgId(), field, dataMap.getString(field.getName()));
				LogManager.debug("CODE 필드  : " + field.getName() +", 값 :" + transformedValue);
				return transformedValue;
			
			}else{
				if(field instanceof LoopField){
					return field.getName();
				}else{
					//return transformedValue;
					return dataMap.get(field.getName());
				}
			}
			*/
			
			return getMappingVlaue(dataMap, field, srcStructure, trgStructure, field.getName(), false);
		
		//2. 매핑정보가 있을 때
		}else{
			
			String mappingExpr = messageFieldMapping.getMappingExpression();
			LogManager.debug(field.getName() +"의 매핑 정보가 존재합니다. [" + mappingExpr +"]");
			//LoopField의 경우에는 매핑 필드 이름을 리턴한다.
			if(field instanceof LoopField){
				transformedValue = MessageMappingExpressionUtil.getMappingFieldName(mappingExpr);
			}else{
			
				//2.1 단순 매핑일 때
				if(!MessageMappingExpressionUtil.isFunction(mappingExpr)){
					String mappingField = MessageMappingExpressionUtil.getMappingFieldName(mappingExpr);
					transformedValue = getMappingVlaue(dataMap, field, srcStructure, trgStructure, mappingField, true);
				
				//2.2 mapping expression이 사용되었을 때
				}else{
					String[] mappingfieldNames = MessageMappingExpressionUtil.getMappingFieldNames(mappingExpr);
					String tempString = "";
					for(int i=0; i<mappingfieldNames.length; i++){
						if(mappingfieldNames[i]==null) continue;
						transformedValue = getMappingVlaue(dataMap, field, srcStructure, trgStructure, mappingfieldNames[i], true);
						tempString = transformedValue==null?"":transformedValue.toString();
						
						//숫자 필드가 아닌 경우는 앞 뒤로 ""을 붙여준다.
						if(!field.getType().equals(MessageField.NUM_FIELD)){
							tempString = "\"" + tempString + "\"";
						}
						mappingExpr = StringUtils.replace(mappingExpr, "{"+mappingfieldNames[i]+"}" , tempString);
						
					}
					return MessageMappingExpressionUtil.exec(mappingExpr);
				}
			}
			return transformedValue;
		}
	}

	private static Object getMappingVlaue(DataMap dataMap, MessageField field, MessageStructure srcStructure, MessageStructure trgStructure, String mappingField, boolean isDebugLog) {
		Object transformedValue;
		String mappingValue = dataMap.getString(mappingField);
		
		//반복 필드일 때
		if(field instanceof LoopField){
			if(isDebugLog) LogManager.debug("반복  필드 : " +  field.getName() +", 값 :" + mappingField);
			return mappingField;
		}
		//코드 필드이면서 코드변환여부가 true인 경우 
		else if(field.isCodeField()&& field.isCodeMapping()){
			//2007.12.17 ksh 수정
			//transformedValue = getMappingCodeValue(srcStructure, trgStructure, field, mappingValue);
			transformedValue = CodeMapper.getMappingCodeValue(srcStructure.getOrgId(), trgStructure.getOrgId(), field, mappingValue);
			LogManager.debug("CODE 필드  : " + field.getName() +", 값 :" + transformedValue);
			return transformedValue;
			
		//일반 필드일 때
		}else{
			if(isDebugLog) LogManager.debug("일반  필드 : " +  field.getName() +", 값 :" + mappingValue);
			return mappingValue;
			
		}
		
	}
}