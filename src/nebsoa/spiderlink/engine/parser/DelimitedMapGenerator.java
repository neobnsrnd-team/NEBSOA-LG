/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.engine.parser;

import org.apache.commons.lang.StringUtils;

import nebsoa.common.collection.DataSet;
import nebsoa.common.util.DataMap;
import nebsoa.common.util.StringUtil;
import nebsoa.spiderlink.engine.message.ByteMessageInstance;
import nebsoa.spiderlink.engine.message.LoopField;
import nebsoa.spiderlink.engine.message.MessageField;
import nebsoa.spiderlink.engine.message.MessageParseException;
import nebsoa.spiderlink.engine.message.MessageStructure;
import nebsoa.spiderlink.engine.message.MessageValueSetter;

/*******************************************************************
 * <pre>
 * 1.설명 
 * DataMap의 값을 가지고 구분자(^)로 분리된 key=value 형태의 메시지를 생성한다.
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
 * $Log: DelimitedMapGenerator.java,v $
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
 * Revision 1.14  2006/11/10 05:47:46  김승희
 * import 정리
 *
 * Revision 1.13  2006/10/28 03:31:26  김승희
 * *** empty log message ***
 *
 * Revision 1.12  2006/08/30 11:23:41  김승희
 * 디폴트 값을 DataMap에도 넣어주도록 수정
 *
 * Revision 1.11  2006/07/21 05:59:16  김승희
 * remark관련 처리
 *
 * Revision 1.10  2006/07/13 03:03:38  김승희
 * 로그 remark 처리에 따른 변경
 *
 * Revision 1.9  2006/07/11 06:21:35  김승희
 * *** empty log message ***
 *
 * Revision 1.8  2006/06/26 10:48:30  김승희
 * 디폴트 값 셋팅 부분 추가
 *
 * Revision 1.7  2006/06/23 07:17:14  김승희
 * null  처리
 *
 * Revision 1.6  2006/06/20 05:41:47  김승희
 * 상수 처리
 *
 * Revision 1.5  2006/06/19 11:57:36  김승희
 * MessageInstance --> ByteMessageInstance로 수정
 *
 * Revision 1.4  2006/06/17 10:12:09  김승희
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public class DelimitedMapGenerator implements MessageGenerator {
	
	private static DelimitedMapGenerator messageGenerator = new DelimitedMapGenerator();
	public static DelimitedMapGenerator getInstance(){
		return messageGenerator;
	}
	public static final String LOOP_START = "LOOP=";
	public static final String LOOP_END = "LOOPEND=";
	public static final String LINE_START = "LS=";
	public static final String LINE_END = "LE=";
	public static final String DELIMITER = "^";
	public static final String EQUAL ="=";
	
	public ByteMessageInstance generate(MessageStructure structure,
			ByteMessageInstance instance, DataMap dataMap) {
		//로그 remark 필드 존재 여부
		boolean isRemark = structure.isRemark();
		
		StringBuffer messageBuffer = new StringBuffer();
		StringBuffer logBuffer = null;
		if(isRemark) logBuffer = new StringBuffer();

		try{
			MessageField field = null;
			int loopSequence = 0;
			Object value = null;
			for(int i=0; i<structure.size(); i++){
				field = structure.getField(i);
				if(field==null) continue;
				//Loop 처리 
				if(field instanceof LoopField){
					LoopField loopField = (LoopField)field;
					MessageField nestedField = null;
					DataSet dataSet  = (DataSet)dataMap.get(MessageField.BEGIN_LOOP+(++loopSequence));
					
					if(dataSet!=null){
						messageBuffer.append(LOOP_START).append(dataSet.size()).append(DELIMITER);
						if(isRemark) logBuffer.append(LOOP_START).append(dataSet.size()).append(DELIMITER);
						dataSet.beforeFirst();
						
						for(int index=1; dataSet.next(); index++){
							messageBuffer.append(LINE_START).append(index).append(DELIMITER);
							if(isRemark) logBuffer.append(LINE_START).append(index).append(DELIMITER);
							for(int j=0; j<loopField.getChildCount(); j++){
								nestedField = loopField.getChild(j);
								if(StringUtil.isNull(nestedField.getFieldTag()))
									throw new MessageParseException(structure.getStructureId() +"-"+nestedField.getName()+"의 Field Tag 정보가 없습니다.");
								value = dataSet.getObject(nestedField.getName());
								//value = getDefaultValue(dataMap, field, value);
								value = MessageValueSetter.getDefaultValue(field, value, dataMap);
								
								//LOOP 데이터 값 셋팅
								messageBuffer.append(nestedField.getFieldTag()).append(EQUAL).append(value).append(DELIMITER);
								//로그 remark 처리
								if(isRemark){
									value = getLogValue(field, value);
									logBuffer.append(nestedField.getFieldTag()).append(EQUAL).append(value).append(DELIMITER);
								}
							}
							messageBuffer.append(LINE_END).append(index).append(DELIMITER);
							if(isRemark) logBuffer.append(LINE_END).append(index).append(DELIMITER);
						}
						messageBuffer.append(LOOP_END).append(dataSet.size()).append(DELIMITER);
						if(isRemark) logBuffer.append(LOOP_END).append(dataSet.size()).append(DELIMITER);
					//DataSet이 null인 경우
					}else{
						messageBuffer.append(LOOP_START).append(0).append(DELIMITER);
						if(isRemark) logBuffer.append(LOOP_START).append(0).append(DELIMITER);
						
						messageBuffer.append(LOOP_END).append(0).append(DELIMITER);
						if(isRemark) logBuffer.append(LOOP_END).append(0).append(DELIMITER);
					}
					
				}else if(field.getName().startsWith(MessageField.END_LOOP)){
					//do nothing...
				}else{
					if(StringUtil.isNull(field.getFieldTag()))
						throw new MessageParseException(structure.getStructureId() +"-"+field.getName()+"의 Field Tag 정보가 없습니다.");
					
					value = dataMap.get(field.getName());
					//value = getDefaultValue(dataMap, field, value);
					//시스템 /디폴트값 처리
					value = MessageValueSetter.getDefaultValue(field, value, dataMap);
					//일반 데이터 값 셋팅
					messageBuffer.append(field.getFieldTag()).append(EQUAL).append(value).append(DELIMITER);
					//로그 remark 처리
					if(isRemark){
						value = getLogValue(field, value);
						logBuffer.append(field.getFieldTag()).append(EQUAL).append(value).append(DELIMITER);
					}
				}
			}
			String messageString = messageBuffer.toString();
						
			/// Map Message 생성 완료. MessageInstance에 담기 ///
			byte[] messageBytes = messageString.getBytes(); //실제 메시지
			byte[] logBytes = null; //로그로 남길  데이터
			//remark여부가 false이면 실제 전송 메시지가 log 메시지가 된다.
			if(isRemark) logBytes = logBuffer.toString().getBytes(); 
			else logBytes = messageBytes;
			
			//MessageInstance 길이 조정.
			instance.adjustMessageLength(messageBytes);
			System.arraycopy(messageBytes, 0, instance.getBytes(), instance.getOffset(), messageBytes.length);

			//로그 데이터 MessageInstance에 담기
			System.arraycopy(logBytes, 0, instance.getLogData(), instance.getOffset(), logBytes.length);
			
			//offset 재설정 
			instance.setOffset(instance.getOffset() + messageBytes.length);
			
			return instance;
		}catch(MessageParseException me){
			throw me;
		}catch(Throwable th){
			th.printStackTrace();
			throw new MessageParseException("Map Message 생성 중 오류가 발생하였습니다. " + th.toString());
		}
	}

	/**
	 * 해당 필드가 remark를 갖고 있을 경우 원래 값(value) 대신에 remark를 입력된 값만큼 반복해서 리턴한다.
	 * remark가 없거나 원래 값(value)이 null 또는 ""일 경우는 원래 값을 그대로 리턴한다.
	 * 
	 * @param field MessageField
	 * @param value 필드에 할당된 값
	 * @return log로 출력될 값
	 */
	private Object getLogValue(MessageField field, Object value) {
		
		//remark가 null이 아니고 value가 null이 아닐 때 로그에 remark로 대체 한다.
		if(!StringUtil.isNull(field.getRemark())&& !StringUtil.isNull(value.toString())){
			value = StringUtils.repeat(field.getRemark(), value.toString().length());
		}
		
		return value;
	}

/*	private Object getDefaultValue(DataMap dataMap, MessageField field, Object value) {
		if(value==null) value = "";
		
		//시스템 입력 값 셋팅 (값이 있더라도 overwrite한다.)
		if(SystemValueKeywordPool.getInstance().contains(field.getDefaultValue())){
			value = SystemValueKeywordPool.getInstance().get(field.getDefaultValue()).getValue(dataMap);
			//시스템 값을 다시 dataMap에 넣어준다.
            dataMap.put(field.getName(), value);
            
		//디폴트 값  셋팅.(값이 ""이거나 null이고 디폴트 값이 "" 또는 null이 아닐 때)
		}else if(StringUtil.isNull(value.toString()) || value.equals("null")){                  
		    if(!StringUtil.isNull(field.getDefaultValue())) {
		    	value=field.getDefaultValue();
		    	//시스템 값을 다시 dataMap에 넣어준다.
	            dataMap.put(field.getName(), value);
		    }
		}
		
		return value;
	}*/

}
