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
 * DataMap의 데이터를 Message Strucutre 정보에 따라 byte[]로 변환한다.
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
 * $Log: ByteMarshaller.java,v $
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
 * Revision 1.1  2007/11/26 08:37:59  안경아
 * *** empty log message ***
 *
 * Revision 1.28  2007/03/20 02:02:11  안경아
 * *** empty log message ***
 *
 * Revision 1.27  2006/11/22 07:24:44  김승희
 * *** empty log message ***
 *
 * Revision 1.26  2006/08/08 07:14:45  안경아
 * *** empty log message ***
 *
 * Revision 1.25  2006/07/21 06:03:46  김승희
 * import 정리
 *
 * Revision 1.24  2006/06/21 05:26:18  김승희
 * UID 부분 수정
 *
 * Revision 1.23  2006/06/20 01:38:51  김승희
 * UID 넣는 부분 수정
 *
 * Revision 1.22  2006/06/19 11:57:36  김승희
 * MessageInstance --> ByteMessageInstance로 수정
 *
 * Revision 1.21  2006/06/17 10:12:09  김승희
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public class ByteMarshaller {

 

	/**
	 * 메시지를 marshall합니다.
	 * @param messageId 메시지 ID
	 * @param orgId 기관 ID
	 * @param dataMap DataMap
	 * @return byte[] marshall된 메시지
	 * @throws MessageParseException
	 */
	public static ByteMessageInstance marshall(String messageId, String orgId, DataMap dataMap) throws MessageParseException {
		
		String structureId = MessageStructureUtil.getMessageStructureId(messageId, orgId);
		MessageStructure structure = MessageStructurePool.getInstance().getMessageStructure(structureId);

        int totalLength = structure.getTotalLength();
        ByteMessageInstance instance = new ByteMessageInstance(totalLength);
        
        //dataMap에 담긴 UID를 ByteMessageInstance에 넣는다.
        Integer uid = (Integer)dataMap.get(MessageConstants.UID);
        if(uid!=null) instance.setUid(uid.intValue());
                
        instance = marshallMessage(structure, instance, dataMap);
        instance.trimData();
        
        return instance;
        
	}//end of marshall()
	
	private static ByteMessageInstance marshallMessage(MessageStructure structure, ByteMessageInstance instance, DataMap dataMap){
		MessageStructure parentStructure = structure.getParent();
		if(parentStructure!=null){
			instance = marshallMessage(parentStructure, instance, dataMap);
		}
		
		//message generate
        MessageGenerator messageGenerator = MessageGeneratorFactory.getMessageGenerator(structure.getMessageType());
        instance = messageGenerator.generate(structure, instance, dataMap);
        
        //메시지 length 셋팅.
        int messageLength = instance.getCurrentMessageLength();
        instance.setMessageLength(structure.getMessageId(), messageLength);
        LogManager.info(structure.getMessageId() +" 메시지 길이 : " + messageLength);
        return instance;
        
	}

	public static void main(String[] args)
	{
		String fl="00000005360000000269SP2P  20060807        IB01    200608071840101940000913    IB00040000                  172.16.2.1               1200608072006080718401019                AN1D                                             Y00000184010210030                          IB6400B62       ^Bb]\221^Bb]\221000100^B0000026020800100000                cu_2602080q01                                 ^]^CT^_        71121218870761            ^_^^";
		DataMap d = new DataMap();
		d.put("비밀번호C4", "333333");
		ByteMessageInstance b = marshall("VI06302006B", "ERP", d);
		System.out.println("==========================");
		System.out.println(new String(b.getBytes()));
		System.out.println("==========================");
		System.out.println(new String(b.getLogData()));
	}
}