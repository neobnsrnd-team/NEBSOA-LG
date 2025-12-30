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

import org.apache.commons.lang.StringUtils;

import nebsoa.common.collection.DataSet;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.DataMap;
import nebsoa.spiderlink.engine.message.ByteMessageInstance;
import nebsoa.spiderlink.engine.message.LoopField;
import nebsoa.spiderlink.engine.message.MessageField;
import nebsoa.spiderlink.engine.message.MessageParseException;
import nebsoa.spiderlink.engine.message.MessageStructure;

/*******************************************************************
 * <pre>
 * 1.설명 
 * CSV 구분자(,)로 분리된 형태의 메시지를 DataMap으로 변환한다.
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
 * $Log: CsvMessageParser.java,v $
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
 * Revision 1.1  2007/11/26 08:38:00  안경아
 * *** empty log message ***
 *
 * Revision 1.5  2006/10/19 14:20:40  이종원
 * logMode일 때만 로그 출력
 *
 * Revision 1.4  2006/09/02 06:18:17  김성균
 * *** empty log message ***
 *
 * Revision 1.3  2006/08/31 09:49:57  김성균
 * *** empty log message ***
 *
 * Revision 1.2  2006/08/29 09:37:28  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2006/08/29 04:35:14  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class CsvMessageParser implements MessageParser {
	
	private static CsvMessageParser messageParser = new CsvMessageParser();
    
	private CsvMessageParser(){}
    
	public static CsvMessageParser getInstance(){
		return messageParser;
	}
    
    public static final String DELIMITER = ",";

	public DataMap parse(MessageStructure structure, ByteMessageInstance instance,
			DataMap dataMap) {
		BufferedReader reader = null;
        
		try {
			reader = new BufferedReader(
                        new InputStreamReader(
                        new ByteArrayInputStream(instance.getBytes(), 
                                instance.getOffset(), 
                                instance.getBytes().length - instance.getOffset())));
            
            MessageField field = null;
            
            int _size = structure.size();
            for (int index = 0; index < _size; index++) {
                field = structure.getField(index);
                DataSet ds = null;
                if (field == null) continue;
                if (field instanceof LoopField) {
                    ds = new DataSet(getLoopFieldName((LoopField) field));
                } else {
                    continue;
                }
                
                String messageLine = null;
                String[] csvTokens = null;

                while ((messageLine = reader.readLine()) != null) {
                    if(field.isLogMode()){
                        LogManager.debug("###line=" + messageLine);
                    }
                    if (messageLine.trim().startsWith("<")) {
                        continue;
                    }
                    csvTokens = StringUtils.splitPreserveAllTokens(messageLine, DELIMITER);
                    ds.appendData(csvTokens);
                }
                dataMap.put(field.getName(), ds);
                if(field.isLogMode()){
                    LogManager.debug("###DataSet=" + ds);
                }
            }
                  
			return dataMap;
        } catch (Throwable e) {
            e.printStackTrace();
            throw new MessageParseException(
                    "CSV Parsing 중 오류가 발생하였습니다. : " + e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }
	}

	/**
     * 필드명 만들기
	 * @param field
	 * @param loopData
	 */
	private String[] getLoopFieldName(LoopField field) {
        MessageField nestedField = null;
        String[] fieldNames = new String[field.getChildCount()];
        for (int j = 0; j < field.getChildCount(); j++) {
            nestedField = field.getChild(j);
            fieldNames[j] = nestedField.getName();
        }
        return fieldNames;
    }
	
}
