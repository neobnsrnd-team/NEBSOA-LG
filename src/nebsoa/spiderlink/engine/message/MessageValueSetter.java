/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.engine.message;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;

import nebsoa.common.collection.DataSet;
import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.DataMap;
import nebsoa.common.util.StringUtil;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 전문 디폴트 값을 할당해주는 클래스
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
 * $Log: MessageValueSetter.java,v $
 * Revision 1.1  2018/01/15 03:39:48  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:17  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:50  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:22  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:26  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.3  2008/10/10 05:51:12  youngseokkim
 * info 로그 메시지 수정
 *
 * Revision 1.2  2008/08/04 09:46:45  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.4  2008/07/08 01:01:54  김재범
 * 코드 유효성 체크 부분에서 "false".equals(field.getCodeGroup()) 추가.
 *
 * Revision 1.3  2008/07/07 11:31:00  김은정
 * LogManager.info추가 
 *
 * Revision 1.2  2008/04/17 05:45:17  김은정
 * parentId에 @가 있는 경우고려하여 수정
 *
 * Revision 1.1  2008/01/22 05:58:20  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.10  2008/01/10 03:25:42  안경아
 * *** empty log message ***
 *
 * Revision 1.9  2008/01/09 09:59:28  안경아
 * 루프count를 얻어오기 위하여 변경
 *
 * Revision 1.8  2008/01/09 07:22:26  안경아
 * revision
 *
 * Revision 1.4  2007/12/26 11:30:26  안경아
 * *** empty log message ***
 *
 * Revision 1.3  2007/12/24 09:01:47  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2007/12/12 07:50:18  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2007/11/26 08:38:04  안경아
 * *** empty log message ***
 *
 * Revision 1.21  2007/03/09 06:11:42  김승희
 * 코드 검증 시 표준에러코드 사용 여부 추가
 *
 * Revision 1.20  2007/02/06 06:31:27  김성균
 * 마스킹 처리부분 StringUtil.maskedData()로 리팩토링
 *
 * Revision 1.19  2007/02/06 06:07:26  김성균
 * DEBUG 출력시 비밀번호관련필드데이타 마스킹 처리
 *
 * Revision 1.18  2006/11/27 08:24:40  김승희
 * SystemKeywordPool 클래스 Factory방식으로 생성
 *
 * Revision 1.15  2006/11/08 09:47:43  김승희
 * checkValue 수정
 *
 * Revision 1.14  2006/10/28 05:57:40  김승희
 * *** empty log message ***
 *
 * Revision 1.13  2006/10/28 03:31:26  김승희
 * *** empty log message ***
 *
 * Revision 1.12  2006/08/10 02:07:04  김승희
 * Message Structure가 null일 때 Exception 처리
 *
 * Revision 1.11  2006/07/07 05:33:12  김승희
 * 예외 처리 관련 수정
 *
 * Revision 1.10  2006/06/22 01:33:17  김승희
 * 중첩 loop 버그 수정
 *
 * Revision 1.9  2006/06/21 12:29:54  김승희
 * *** empty log message ***
 *
 * Revision 1.8  2006/06/21 08:41:30  김승희
 * 코드 검증 추가
 *
 * Revision 1.7  2006/06/21 05:11:42  김승희
 * 디폴트 value 셋팅 및 입력값 검사 추가
 *
 * Revision 1.6  2006/06/17 10:12:09  김승희
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public class MessageValueSetter {
	
		
	/*public static String getDefaultFieldValue(DataMap dataMap, String defaultValueKey){
				
		//default value가 시스템 키워드인경우
		if(SystemValueKeywordPool.getInstance().contains(defaultValueKey)){
			return SystemValueKeywordPool.getInstance().get(defaultValueKey).getValue(dataMap);
		}else{
			return defaultValueKey;
		}
	}*/
	
	/**
	 * MessageStructure의 필드 정보를 기반으로 DataMap에 담긴 값을 검증하고, 디폴트 값을 셋팅한다.
	 *  - 필드의 REQUIRED_YN이 Y이면서 USE_MODE가 U인 경우 값이 없으면 exception을 던진다.
	 *  - 코드 필드인데 올바른 코드 값이 들어오지 않은 경우 exception을 던진다.
	 *  - 그 외의 경우 값이 없으면 디폴트 값을 셋팅한다.
	 *  (값이 없다는 것은 null 또는 공백 스트링을 뜻한다.)
	 * 
	 * @param messageId 메시지 ID
	 * @param orgId 기관 ID
	 * @param dataMap DataMap
	 * @return 디폴트 값이 셋팅된 DataMap
	 */
	public static DataMap checkMessageFieldValue(String messageId, String orgId, DataMap dataMap) {
		return checkMessageFieldValue(messageId, orgId, dataMap, false);
       
	}
	
	/**
	 * MessageStructure의 필드 정보를 기반으로 DataMap에 담긴 값을 검증하고, 디폴트 값을 셋팅한다.
	 *  - 필드의 REQUIRED_YN이 Y이면서 USE_MODE가 U인 경우 값이 없으면 exception을 던진다.
	 *  - 코드 필드인데 올바른 코드 값이 들어오지 않은 경우 exception을 던진다.
	 *  - 그 외의 경우 값이 없으면 디폴트 값을 셋팅한다.
	 *  (값이 없다는 것은 null 또는 공백 스트링을 뜻한다.)
	 * 
	 * 표준 에러코드 사용여부가 true이면 기관에러코드로 검증하지 않고 표준에러코드로 검증한다.
	 * 
	 * @param messageId 메시지 ID
	 * @param orgId 기관 ID
	 * @param dataMap DataMap
	 * @param useSTDCode 표준에러코드 사용여부
	 * @return 디폴트 값이 셋팅된 DataMap
	 */
	public static DataMap checkMessageFieldValue(String messageId, String orgId, DataMap dataMap, boolean useSTDCode) {
        /* 기관마다 동일하게 사용되는 공통헤더를 만들지 않기 위하여 공통헤더라는 기관을 새로 생성하고 
        COMMON_HEADER(공통헤더)라는 기관에 속한 ParentId는 'parentId@orgId'형태로 등록된다.  */
        int idx = messageId.indexOf("@");
		String msgId = null;
        if (idx != -1) { //@가 없으면 
        	msgId = messageId;
        }else{
        	msgId = MessageStructureUtil.getMessageStructureId(messageId, orgId);
        }
		LogManager.info("checkMessageFieldValue 전문정보     기관ID : " + orgId + ", 전문ID : " + messageId );
		
		MessageStructure structure = MessageStructurePool.getInstance().getMessageStructure(msgId);
		
		if(structure==null) throw new SysException("Message Structure error :: 메시지ID["+messageId+"] 기관ID[" + orgId +"]에 해당하는 전문 필드 정보가 없습니다.");
		if(structure.getParent() != null) checkMessageFieldValue(structure.getParentId(), orgId, dataMap, useSTDCode);
		int _size=structure.size();
    	DataSet loopDataSet = null;
        for (int index=0; index<_size;index++ ) {
            MessageField field = structure.getField(index);
            if(field==null) continue;
            
            if(field instanceof LoopField){
            	loopDataSet = (DataSet)dataMap.get(field.getName());
                if(loopDataSet!=null) loopDataSet.beforeFirst();
                dataMap.put(field.getName(), checkLoopValue((LoopField)field, loopDataSet, dataMap, orgId, useSTDCode));
            }else 
            	dataMap.put(field.getName(), checkValue(field, dataMap.getString(field.getName()), dataMap, orgId, useSTDCode));
        }
       
		return dataMap;
	}
	//일반 필드 처리
    private static String checkValue(MessageField field, String value, DataMap dataMap, String orgId, boolean useSTDCode){
    	
        String fieldNM = field.getName();
        String mask = StringUtil.maskedData(fieldNM, value);
        
        if (LogManager.isDebugEnabled()) {
            LogManager.debug(fieldNM + "[UseMode:" + field.getUseMode() +", DefaultValue:" + field.getDefaultValue() + "] 입력값="+mask);
        }
        
    	value = getDefaultValue(field, value, dataMap);
    	
    	if(StringUtil.isNull(value)){
    		//Required 필드인데 값이 없으면 exception을 던진다..
    		if(field.isRequired()){
    			throw new InvalidInputException(fieldNM +": 필수 입력 항목입니다.");
    		}
    	}
    		
    	//코드 필드의 경우 올바른 코드값이 들어왔는지 체크한다.
    	
    	//"false".equals(field.getCodeGroup()) 추가. 20080704 김재범
    	if(!"false".equals(field.getCodeGroup()) && field.isCodeField() && !StringUtil.isNull(value)){
    		String codeOrgId = orgId;
    		//2007. 3. 9 추가
    		//표준에러코드 사용여부가 true이면 코드를 찾을 때 기관을 STD로 찾는다.
    		if(useSTDCode) codeOrgId = MessageConstants.ORG_STD;
    		if(!MessageCodePool.getInstance().isValidCode(codeOrgId, field.getCodeGroup(), value)){
    			throw new InvalidCodeException("유효한 코드 값이 아닙니다. [기관ID:" + orgId +", 코드그룹:"+ field.getCodeGroup() +", 입력된 값:" + value +"]");
    		}
		}
    	
    	return value;
    }

	public static String getDefaultValue(MessageField field, Object valueObj, DataMap dataMap) {
		if(valueObj==null) valueObj = "";
		String value = valueObj.toString();
		
    	//시스템 키워드 입력 값 셋팅 (값이 있더라도 overwrite한다.)
    	if(SystemValueKeywordPoolFactory.getSystemValueKeywordPool().contains(field.getDefaultValue())){
    		String keyword = field.getDefaultValue();

    		//시스템 키워드가 _$FIXED로 시작하는 경우는 앞의 _$FIXED=를 제거한 나머지 값을 디폴트 값으로 리턴한다. 
    		if(keyword.startsWith(SystemValueKeywordPool.FIXED_KEYWORD)){
    			value = StringUtils.removeStart(keyword, SystemValueKeywordPool.FIXED_KEYWORD+"=").trim();
    		//시스템 키워드가 _$MAP로 시작하는 경우는 _$MAP.을 제외한 값을 키로 하여 dataMap에서 값을 넣어준다.
    		}else if(keyword.startsWith(SystemValueKeywordPool.MAP_KEYWORD)){
    			value = dataMap.getString(StringUtils.removeStart(keyword, SystemValueKeywordPool.MAP_KEYWORD+".").trim());
    		//그 외의 경우는 SystemValueKeywordPool의 SystemValue 객체로 부터 얻어낸다.
    		}else
    			value = SystemValueKeywordPoolFactory.getSystemValueKeywordPool().get(keyword).getValue(dataMap);
    		
    		//시스템 값을 다시 dataMap에 넣어준다.
    		dataMap.put(field.getName(), value);
    	

    	//USE MODE가 System(S)인 경우는 값이 있더라도 무조건 디폴트 값으로 overwrite한다.
    	}else if(MessageField.SYSTEM_USE.equals(field.getUseMode())){
    		value=field.getDefaultValue();
    		if(value==null)value = "";
            //디폴트 값을 다시 dataMap에 넣어준다.
            dataMap.put(field.getName(), value);
            
            if(field.isLogMode()) LogManager.debug("USE_MODE[S] :: " + field.getName() + 
            		"의 입력값[" + valueObj + "]을 디폴트값[" + value +"]으로 overwrite합니다.");

        	//USE MODE가 System(U)인 경우는 dataMap에 값을 put
    	}else if(MessageField.USER_USE.equals(field.getUseMode())){
            if(StringUtil.isNull(value) || value.equals("null")){
            	value=field.getDefaultValue();
            	if(value==null)value = "";
            }
            //datMap 값을 dataMap에 넣어준다.
            dataMap.put(field.getName(), value);        
            
            if(field.isLogMode()) LogManager.debug("USE_MODE[U] :: " + field.getName() + 
            		"의 입력값[" + valueObj + "]으로 set합니다.");

    		    		
    	//USE MODE가 System(S), User(U), 시스템키워드 사용하지 않은 경우  디폴트 값  셋팅.(값이 ""이거나 null인 경우에만)
    	}else if(StringUtil.isNull(value) || value.equals("null")){ 
            value=field.getDefaultValue();
            if(value==null)value = "";
            //디폴트 값을 다시 dataMap에 넣어준다.
            dataMap.put(field.getName(), value);
        }
		return value;
	}
	
    //Loop 필드 처리
    private static DataSet checkLoopValue(LoopField field, DataSet loopDataSet, DataMap dataMap, String orgId, boolean useSTDCode){
                
        ArrayList nestedFieldList = field.getChildList(); 
        MessageField nestedField = null;
        DataSet nestedLoopDataSet = null;        
        if(loopDataSet!=null){
        	loopDataSet.beforeFirst();
        	//필드 이름 array
    		String[] nestedFieldNameArray = new String[field.getChildCount()];
        	ArrayList dataList = new ArrayList();
        	Object[] dataRow = null;
        	boolean isFirst = true;
        	while(loopDataSet.next()){
        		dataRow = new Object[nestedFieldList.size()];
        		
        		for(int i=0; i<nestedFieldList.size(); i++){
        			nestedField = (MessageField)nestedFieldList.get(i);
        			if(isFirst){
						nestedFieldNameArray[i] = nestedField.getName();
					}

        			//loop 안의 loop 처리
        			if(nestedField instanceof LoopField){
        				nestedLoopDataSet = (DataSet)loopDataSet.getObject(nestedFieldNameArray[i]);
        				dataRow[i] = checkLoopValue((LoopField)nestedField, nestedLoopDataSet, dataMap, orgId, useSTDCode);
        			
        			}else{
		
        				dataRow[i] = checkValue(nestedField, loopDataSet.getString(nestedFieldNameArray[i]), dataMap, orgId, useSTDCode);
        			}
        			                    
        		}
        		dataList.add(dataRow);
        		isFirst = false;
        	}
        	//loopDataSet.beforeFirst();
        	return new DataSet(nestedFieldNameArray, dataList);
        }else{
        	return null;
        }
  
    }

}
