/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.engine.message;

import java.util.Vector;

import nebsoa.common.util.DataMap;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 메세지 Structure와 관련된 유틸리티 클래스
 * 
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
 * $Log: MessageStructureUtil.java,v $
 * Revision 1.1  2018/01/15 03:39:48  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:15  cvs
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
 * Revision 1.1  2008/08/04 08:54:51  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.2  2008/07/04 15:59:01  김승희
 * getInstance() 메소드 수정
 *
 * Revision 1.1  2008/01/22 05:58:20  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:05  안경아
 * *** empty log message ***
 *
 * Revision 1.10  2006/08/11 07:50:55  김승희
 * Message Structure 로딩 방법 수정 -> 건별 로딩
 *
 * Revision 1.9  2006/06/30 07:17:26  김승희
 * *** empty log message ***
 *
 * Revision 1.8  2006/06/17 10:12:09  김승희
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public class MessageStructureUtil {
	
	private MessageStructureUtil(){
		messageStructureDependentList = new Vector();
	}
	
	private static MessageStructureUtil instance = new MessageStructureUtil();
	
	/**
	 *  Message Structure ID에서 메시지 ID와 기관 ID 사이의 구분자
	 */
	public static final String delimeter = "@";
	
	/**
	 *  Message Structure에 종속적인 객체 리스트
	 *  (Message Structure가 로딩되면 따라서 재로딩되어야 할 객체 리스트)
	 */
	private Vector messageStructureDependentList;
	
	public static MessageStructureUtil getInstance(){
		//if(instance==null) instance = new MessageStructureUtil();
		return instance;
	}
	
	public Vector getMessageStructureDependentList(){
		return messageStructureDependentList;
	}
	
	/** Message Structure ID 형태를 만들어 리턴한다.
	 * @param messageId 메시지 ID
	 * @param orgId 기관 ID
	 * @return Message Structure ID
	 */
	public static String getMessageStructureId(String messageId, String orgId){
		StringBuffer sb = new StringBuffer(35);
		return sb.append(messageId).append(delimeter).append(orgId).toString();
	}
	
	/**
	 * @param messageStructure 메세지 구조
	 * @return
	 */
	public static DataMap generateDataMap(MessageStructure messageStructure){
		DataMap dataMap = new DataMap();
		return constructDataMap(dataMap, messageStructure);
	}
	
	/**
	 * messageStructure의 구조에 맞는 새 DataMap을 리턴한다.
	 * 인자로 들어오는 DataMap에 동일 key가 있을 경우는 그 값을 그대로 할당해준다.
	 * (부모메시지 structure 포함)
	 * 
	 * @param dataMap DataMap
	 * @param messageStructure 메세지 구조
	 * @return DataMap messageStructure에 맞게 key가 할당된 DataMap
	 */
	public static DataMap constructDataMap(DataMap oldDataMap, MessageStructure messageStructure){
		DataMap newDataMap = (DataMap)oldDataMap.clone();
		//DataMap newDataMap = new DataMap();
		
		//새구조로 변환할 때 원래 dataMap의 메시지 필드는 제거해 줘야 하나?(지금은 현재 안 하고 있음)
		return constructTotalDataMap(oldDataMap, newDataMap, messageStructure);
		
	}

	private static DataMap constructTotalDataMap(DataMap oldDataMap, DataMap newDataMap, MessageStructure messageStructure){
		
		if(messageStructure.getParent()!=null){
			constructTotalDataMap(oldDataMap, newDataMap, messageStructure.getParent());
		}
		int size = messageStructure.size();
		if(size==0) return newDataMap;
		MessageField messageField = null;
		
		for(int i=0; i<size; i++){
			messageField = messageStructure.getField(i);
			if(messageField!=null){
				newDataMap.put(messageField.getName(), oldDataMap.get(messageField.getName()));
			}
		}
		return newDataMap;
	}
	
	private static MessageFieldMappingMap emptyMessageFieldMappingMap = null;
	public static MessageFieldMappingMap getEmptyMessageFieldMappingMap(){
		if(emptyMessageFieldMappingMap==null){
			emptyMessageFieldMappingMap = new MessageFieldMappingMap();
		}
		return emptyMessageFieldMappingMap;
	}
	
	public static void main(String[] args){
		/*System.setProperty("SPIDER_HOME", "d:/serverside/spider");
		DataMap dataMap = new DataMap();
		dataMap.put("고객명", "홍길동");
		dataMap.put("직업코드", "A");
		dataMap.put("연령", "29");
		dataMap.put("고객구분코드", "01");
		dataMap.put("eeeeeeeeeee", "01");
		String[] fieldNames1 = {"신탁계좌번호","신탁금액"};
		ArrayList accList1 = new ArrayList();
		accList1.add(new Object[]{"신탁1","200000"});
		accList1.add(new Object[]{"신탁2","300000"});

		DataSet dataSet1 = new DataSet(fieldNames1, accList1);
		dataMap.put(MessageField.BEGIN_LOOP+"C", dataSet1);
		
		MessageStructure structure = MessageStructurePool.getInstance().getMessageStructure("ST_001@STD");
		DataMap newDataMap = constructDataMap(dataMap, structure);
		System.out.println("dataMap : " + newDataMap);*/
		
		MessageStructure trgStructure = MessageStructurePool.getInstance().getMessageStructure("MT001_I@STD");
		MessageStructure trgStructure2 = MessageStructurePool.getInstance().getMessageStructure("MT001_O@STD");
		MessageFieldMappingMap fieldMappings = trgStructure.getFieldMappingMap();
		System.out.println("fieldMappings: " + fieldMappings);
		fieldMappings.put("1","2","3","4",null);
		MessageFieldMappingMap fieldMappings2 = trgStructure2.getFieldMappingMap();
		System.out.println("fieldMappings2: " + fieldMappings2);
	}
}
