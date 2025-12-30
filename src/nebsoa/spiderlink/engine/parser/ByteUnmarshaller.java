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
import nebsoa.spiderlink.engine.message.ByteMessageInstance;
import nebsoa.spiderlink.engine.message.MessageConstants;
import nebsoa.spiderlink.engine.message.MessageParseException;
import nebsoa.spiderlink.engine.message.MessageStructure;
import nebsoa.spiderlink.engine.message.MessageStructurePool;
import nebsoa.spiderlink.engine.message.MessageStructureUtil;

/*******************************************************************
 * <pre>
 * 1.설명 
 * byte[]로 전송받은 데이터를 Message Strucutre 정보에 따라 필드Name=필드value의 DataMap으로 변환한다.
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
 * $Log: ByteUnmarshaller.java,v $
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
 * Revision 1.1  2007/11/26 08:37:59  안경아
 * *** empty log message ***
 *
 * Revision 1.15  2006/11/23 08:21:23  김승희
 * 수신 데이터 remark 처리를 위한 변경
 *
 * Revision 1.14  2006/11/23 04:05:29  김승희
 * 소스 정리
 *
 * Revision 1.13  2006/08/18 05:24:12  김승희
 * Map 타입 부모 메시지 파싱 시 바디 메시지 이중 파싱 제거 처리
 *
 * Revision 1.12  2006/07/03 12:36:15  김승희
 * *** empty log message ***
 *
 * Revision 1.11  2006/06/26 02:03:50  김승희
 * END LOOP 관련 처리
 *
 * Revision 1.10  2006/06/20 05:26:13  김승희
 * 주석 추가
 *
 * Revision 1.9  2006/06/17 10:12:09  김승희
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public class ByteUnmarshaller {

    
	/**
	 * 메시지를 unmarshall합니다.
	 * @param messageId 메시지 ID
	 * @param orgId 기관 ID
	 * @param dataMap DataMap
	 * @return
	 * @throws MessageParseException
	 */
	public static DataMap unmarshall(String messageId, String orgId, DataMap dataMap) throws MessageParseException {
		        
		String structureId = MessageStructureUtil.getMessageStructureId(messageId, orgId);
		MessageStructure structure = MessageStructurePool.getInstance().getMessageStructure(structureId);
        
		byte [] unparsedMessage = (byte [])dataMap.get(MessageConstants.MESSAGE_RECEIVED);
        ByteMessageInstance instance=new ByteMessageInstance(unparsedMessage);
        
        return unmarshallMessage(structure, instance, dataMap, true);

	}//end of unmarshaller()
	
	/**
	 * 상위 메시지는 unmarshall하지 않고 특정 메시지 하나만 unmarshall할 때 사용한다.
	 * 이 경우 ByteMessageInstance의 offset을 기준으로 그 다음부터 unmarshall한다.
	 * 
	 * @param messageId 메시지 ID	
	 * @param orgId 기관 ID
	 * @param instance ByteMessageInstance의
	 * @param dataMap DataMap
	 * @param parentMessageUnMarshall 상위 메시지 unmarshall 여부
	 * @return DataMap
	 * @throws MessageParseException
	 */
	public static DataMap unmarshall(String messageId, String orgId, ByteMessageInstance instance, DataMap dataMap, boolean parentMessageUnMarshall) throws MessageParseException {
		
		String structureId = MessageStructureUtil.getMessageStructureId(messageId, orgId);
		MessageStructure structure = MessageStructurePool.getInstance().getMessageStructure(structureId);
		
        return unmarshallMessage(structure, instance, dataMap, parentMessageUnMarshall);

	}//end of unmarshaller()
	
    private static DataMap unmarshallMessage(MessageStructure structure, ByteMessageInstance instance, DataMap dataMap, boolean parentMessageUnMarshall){
		//부모 메시지를 처리 여부가 true일 때만..
    	if(parentMessageUnMarshall){
	    	MessageStructure parentStructure = structure.getParent();
			//부모 메시지 처리
			if(parentStructure!=null){
				dataMap = unmarshallMessage(parentStructure, instance, dataMap, parentMessageUnMarshall);
			}
		}
    	LogManager.debug("["+ structure.getStructureId()+"]을 unmarshall합니다..");
        MessageParser messageParser = MessageParserFactory.getMessageParser(structure.getMessageType());
        messageParser.parse(structure, instance, dataMap);
        
        //처리가 끝난 메시지 byte를 다시 map에 넣어준다.
        //remark 처리된 메시지를 담기 위함..
        dataMap.put(MessageConstants.MESSAGE_RECEIVED, instance.getBytes());
		
        return dataMap;
    }
    
    public static void main(String[] args){
    	
    	DataMap d = new DataMap();
    	byte[] unparsedMessage = "43369270305394080000이효리              외환은행            01038397199     2006112300000002000000000000020000000000000200000000000002000000000000020000000000000200000000000000000000000000000000000000000018181800000000181818".getBytes();
    	ByteMessageInstance instance=new ByteMessageInstance(unparsedMessage);
		d = unmarshall("VI06302006B", "TANDEM2" , instance, d, false);
		System.out.println("==========================");
		System.out.println(new String((byte[])d.get(MessageConstants.MESSAGE_RECEIVED)));
		System.out.println("==========================");
		
    }
 
}