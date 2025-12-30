/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.engine.parser;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import nebsoa.common.collection.DataSet;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.DataMap;
import nebsoa.common.util.StringUtil;
import nebsoa.spiderlink.engine.message.ByteMessageInstance;
import nebsoa.spiderlink.engine.message.LoopField;
import nebsoa.spiderlink.engine.message.MessageField;
import nebsoa.spiderlink.engine.message.MessageParseException;
import nebsoa.spiderlink.engine.message.MessageStructure;
import nebsoa.spiderlink.engine.message.MessageStructurePool;
import nebsoa.spiderlink.engine.message.MessageType;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 구분자(^)로 분리된 key=value 형태의 메시지를 DataMap으로 변환한다.
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
 * $Log: DelimitedMapParser.java,v $
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
 * Revision 1.1  2007/11/26 08:38:00  안경아
 * *** empty log message ***
 *
 * Revision 1.13  2006/11/23 04:05:29  김승희
 * 소스 정리
 *
 * Revision 1.12  2006/10/19 14:16:30  이종원
 * Log 주석 처리
 *
 * Revision 1.11  2006/08/18 05:24:12  김승희
 * Map 타입 부모 메시지 파싱 시 바디 메시지 이중 파싱 제거 처리
 *
 * Revision 1.10  2006/07/21 06:03:46  김승희
 * import 정리
 *
 * Revision 1.9  2006/07/07 11:21:46  김승희
 * stackTrace 삭제
 *
 * Revision 1.8  2006/06/26 10:26:37  김승희
 * 버그 수정
 *
 * Revision 1.7  2006/06/20 05:41:47  김승희
 * 상수 처리
 *
 * Revision 1.6  2006/06/19 11:57:36  김승희
 * MessageInstance --> ByteMessageInstance로 수정
 *
 * Revision 1.5  2006/06/17 10:12:09  김승희
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public class DelimitedMapParser implements MessageParser {
	
	private static DelimitedMapParser messageParser = new DelimitedMapParser();
	private DelimitedMapParser(){}
	public static DelimitedMapParser getInstance(){
		return messageParser;
	}
	public static final String LOOP_START = "LOOP";
	public static final String LOOP_END = "LOOPEND";
	public static final String LINE_START = "LS";
	public static final String LINE_END = "LE";
	public static final String DELIMITER = "^";
	public static final String EQUAL ="=";
	
	public DataMap parse(MessageStructure structure, ByteMessageInstance instance,
			DataMap dataMap) {
		BufferedReader reader = null;
		try{
			//ByteMessageInstance byteMessageInstance = (ByteMessageInstance)instance;
			//String message = new String(byteMessageInstance.getBytes(), byteMessageInstance.getOffset(), byteMessageInstance.getBytes().length-byteMessageInstance.getOffset());
			//reader = new BufferedReader(new StringReader(message));
			reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(instance.getBytes(), instance.getOffset(), instance.getBytes().length-instance.getOffset())));
			String messageLine = null;
			
			String[] mapTokens = null;
			String key = null;
			String value=null;
			LoopData loopData = null;
			int loopSequence = 0;
			boolean isLoop = false;
			
			Map tagNameMap = structure.getFieldTagNameMap();
			
			//부모가 Message Structurer가 Map 타입인 경우 한꺼번에 파싱한다.
			MessageStructure parentStructure = structure.getParent();
			if(parentStructure!=null && parentStructure.getMessageType().getType().equals(MessageType.MAP_STRING)){
				tagNameMap.putAll(parentStructure.getFieldTagNameMap());
			}
			
			while((messageLine=reader.readLine())!=null){
				key = null;value=null;
				mapTokens = StringUtils.splitByWholeSeparator(messageLine, DELIMITER);
				
				for(int i=0; i<mapTokens.length; i++){
					key = StringUtils.substringBefore(mapTokens[i], EQUAL);
					value = StringUtils.substringAfter(mapTokens[i], EQUAL);
					if(structure.isLogMode()){
						LogManager.debug("[" + key +"] " + value);
                    }
					if(!StringUtil.isNull(key)){
						//반복데이터이면
						if(LOOP_START.equals(key)){
							loopSequence++;
							int loopCount = Integer.parseInt(value);
							//loop 개수가 1이상인 경우만 처리
							if(loopCount>0){
								LoopField field = (LoopField)structure.getFieldByName(MessageField.BEGIN_LOOP+loopSequence);
								if(field!=null){
									//loopData = new LoopData(structure, loopCount, loopSequence);
									loopData = new LoopData(field, loopCount);
									isLoop = true;
								}
							}else{
								isLoop = false;
							}
							
						}else if(LOOP_END.equals(key)){
							if(isLoop)dataMap.put(MessageField.BEGIN_LOOP+loopSequence, loopData.toDataSet());
						
							isLoop = false;
						}else if(isLoop){
							if(key.equals(LINE_START)){
								loopData.nextRow();
							}else if(key.equals(LINE_END)){
								loopData.initColumn();
							}else{
								//데이터 넣는 부분 (Loop 데이터)
								//key로 넘어온 Field Tag를 가지고 해당 Field Name의 Index를 가져온다..
								Object obj = loopData.fieldTagIndexMap.get(key);
								if(obj!=null){
									loopData.getCurrRow()[((Integer)obj).intValue()]= value;
								}
							}
						}else{
							//데이터 넣는 부분(Non-Loop 데이터)
//							//key로 넘어온 Field Tag를 가지고 해당 Field Name을 얻어와 dataMap의 키로 사용한다.
							if(tagNameMap.get(key)!=null)
								dataMap.put(tagNameMap.get(key), value);
						}
					}
				}
			}
			return dataMap;
		}catch(Throwable e){
        	//e.printStackTrace();
        	throw new MessageParseException("Delimited Map Parsing 중 오류가 발생하였습니다. : " + e);
        }finally{
        	if(reader!=null){
        		try {
					reader.close();
				} catch (IOException e) {
					
				}
        	}
        }
	}

/*	private void setLoopFieldName(MessageStructure structure, LoopData loopData, int loopSequence) {
		//필드명 만들기
		LoopField field = (LoopField)structure.getFieldByName(MessageField.BEGIN_LOOP+loopSequence);
		if(field==null){
			
			throw new MessageParseException(MessageField.BEGIN_LOOP+loopSequence + "에 해당하는 Loop Field 정보가 MessageStructure에 없습니다.");
		}
		MessageField nestedField = null;
		loopData.fieldNames = new String[field.getChildCount()];
		loopData.fieldTagIndexMap = new HashMap(field.getChildCount());
		for(int j=0; j<field.getChildCount(); j++){
			nestedField = field.getChild(j);
			loopData.fieldNames[j] = nestedField.getName();
			loopData.fieldTagIndexMap.put(nestedField.getFieldTag(), Integer.valueOf(j));
		}
		
	}*/
	
	private void setLoopFieldName(LoopField field, LoopData loopData) {
		//필드명 만들기
		
		MessageField nestedField = null;
		loopData.fieldNames = new String[field.getChildCount()];
		loopData.fieldTagIndexMap = new HashMap(field.getChildCount());
		for(int j=0; j<field.getChildCount(); j++){
			nestedField = field.getChild(j);
			loopData.fieldNames[j] = nestedField.getName();
			loopData.fieldTagIndexMap.put(nestedField.getFieldTag(), Integer.valueOf(j));
		}
		
	}
	
	private class LoopData{
		String[] fieldNames;
		Map fieldTagIndexMap;
		String[][] rows;
		int rowIndex = -1;		
		int columnIndex = -1;	
		
		/*public LoopData(MessageStructure structure, int loopCount, int loopSequence){
			setLoopFieldName(structure, this, loopSequence);
			rows = new String[loopCount][fieldNames.length];
	
		}*/
		public LoopData(LoopField field, int loopCount){
			setLoopFieldName(field, this);
			rows = new String[loopCount][fieldNames.length];
	
		}
		
		public String[] getCurrRow(){
			return rows[rowIndex];
		}
		
		public void nextRow(){
			rowIndex++;
		}
		
		public void initColumn(){
			columnIndex = -1;	
		}
		
		public int getNextColumnIndex(){
			columnIndex++;
			return columnIndex;
		}
		
		public DataSet toDataSet(){
			ArrayList arrayList = new ArrayList(Arrays.asList(rows));
			DataSet dataSet = new DataSet(fieldNames, arrayList);
			dataSet.beforeFirst();
			return dataSet;
		}
	}

}
