/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.engine.message;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import nebsoa.common.Constants;
import nebsoa.common.exception.SysException;
import nebsoa.common.jdbc.DBManager;
import nebsoa.common.jdbc.DBResultSet;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.DataMap;
import nebsoa.common.util.DbToXml;
import nebsoa.common.util.PropertyManager;
import nebsoa.common.util.QueryManager;
import nebsoa.management.ManagementObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 메시지 정보를 로드하여 캐싱하는 풀 클래스
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
 * $Log: MessageStructurePool.java,v $
 * Revision 1.1  2018/01/15 03:39:48  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:16  cvs
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
 * Revision 1.5  2008/07/07 10:05:03  김승희
 * 주석 변경
 *
 * Revision 1.4  2008/07/07 09:15:25  김승희
 * 1. 로딩하는 쿼리를 별도 클래스로 추출
 * 2. FWK_MESSAGE 정보 만을 일괄 로딩하는 메소드 추가
 *
 * Revision 1.3  2008/07/04 13:41:19  김은정
 * *** empty log message ***
 *
 * Revision 1.2  2008/05/13 02:46:24  김승희
 * LOAD_ALL_MESSAGE_FILED, LOAD_ALL_PRE_LOAD_MESSAGE_FILED 조회 쿼리에 누락된 필드 추가 (LOG_YN, CODE_MAPPING_YN)
 *
 * Revision 1.1  2008/01/22 05:58:20  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.9  2008/01/08 07:57:21  김재범
 * MySql 일경우 추가
 *
 * Revision 1.8  2007/12/24 09:01:46  안경아
 * *** empty log message ***
 *
 * Revision 1.7  2007/12/18 08:30:51  김승희
 * CODE_MAPPING_YN 필드 추가
 *
 * Revision 1.6  2007/11/30 10:24:10  안경아
 * *** empty log message ***
 *
 * Revision 1.5  2007/11/30 09:46:54  안경아
 * DB NAME 지정
 *
 * Revision 1.4  2007/11/29 01:44:24  안경아
 * *** empty log message ***
 *
 * Revision 1.3  2007/11/28 01:29:03  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2007/11/27 07:32:38  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2007/11/26 08:38:04  안경아
 * *** empty log message ***
 *
 * Revision 1.60  2007/03/19 07:42:14  김승희
 * 메시지 건별로 로딩시 여러 요청이 동시에 들어올 경우 멤버변수 predicate 공유 문제 처리
 *
 * Revision 1.59  2007/03/09 06:04:51  김승희
 * field type 추가
 *
 * Revision 1.58  2006/11/02 01:50:16  안경아
 * *** empty log message ***
 *
 * Revision 1.57  2006/10/19 13:34:18  이종원
 * field log mode 로딩 추가
 *
 * Revision 1.56  2006/10/12 13:00:52  안경아
 * *** empty log message ***
 *
 * Revision 1.55  2006/09/13 01:26:50  김승희
 * XML모드인데 사전로딩여부가 true일 때는 로딩하지 않도록 수정
 *
 * Revision 1.54  2006/09/06 11:13:56  김승희
 * 사전로딩여부 전문 로딩 구현
 *
 * Revision 1.53  2006/08/28 13:01:17  김성균
 * reloadAll()에 DataMap 인자로 받도록 추가
 *
 * Revision 1.52  2006/08/16 05:57:07  김승희
 * 로깅 추가
 *
 * Revision 1.51  2006/08/16 05:25:57  김승희
 * ManagementObject 기능 구현
 *
 * Revision 1.50  2006/08/11 07:50:55  김승희
 * Message Structure 로딩 방법 수정 -> 건별 로딩
 *
 * Revision 1.49  2006/08/10 05:25:10  김승희
 * 로딩시간 로깅
 *
 * Revision 1.48  2006/08/03 01:36:37  김승희
 * *** empty log message ***
 *
 * Revision 1.47  2006/08/02 09:45:33  김승희
 * reload 함수 수정
 *
 * Revision 1.46  2006/08/02 09:34:04  김승희
 * ManagementObject  적용에 따른 변경
 *
 * Revision 1.45  2006/07/19 07:48:29  김승희
 * Remark 필드 존재 여부 추가
 *
 * Revision 1.44  2006/07/11 07:13:50  김승희
 * *** empty log message ***
 *
 * Revision 1.43  2006/07/11 06:22:39  김승희
 * *** empty log message ***
 *
 * Revision 1.42  2006/07/11 01:56:38  김승희
 * REMARK 필드 추가
 *
 * Revision 1.41  2006/06/20 12:24:26  김승희
 * required_yn 속성 추가
 *
 * Revision 1.40  2006/06/17 10:12:09  김승희
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public class MessageStructurePool extends ManagementObject {
	
	/**
	 * ManagementObject에서 리로딩 toXML, fromXML에서 사용하는 message id의 key
	 */
	public static final String MSG_POOL_MESSAGE_ID = "_$MSG_POOL_MESSAGE_ID";
	
	/**
	 * ManagementObject에서 리로딩 toXML, fromXML에서 사용하는 기관 id의 key
	 */
	public static final String MSG_POOL_ORG_ID = "_$MSG_POOL_ORG_ID";

	
	private static MessageStructurePool instance = new MessageStructurePool();
	
	private Hashtable pool; //전문 + 필드 정보 캐쉬 
	
	private Hashtable structurePool; //전문 정보 임시 보관 캐쉬
	
	private boolean isLoaded = false;
	
	private PredicateImpl predicate;
	
	
	private MessageStructurePool() {
		this.pool = new Hashtable();
		structurePool = new Hashtable();
		predicate = new PredicateImpl();

		
	}//end of constructor
	
	public static MessageStructurePool getInstance() {
		if(instance==null) instance = new MessageStructurePool();
		
		//객체 생성시 매번 전체를 로딩하는 로직은 막는다.
		//구체적인 MessageStructure를 구하는 순간에 MessageStructure를 별 로딩하도록한다.
		//if(!instance.isLoaded) instance.loadAll();
		
		return instance;
		
	}//end of getInstance()
	
	
	/**
	 *  FWK_MESSAGE 정보를 모두 로딩하여 pool에 저장한다. (FWK_MESSAGE_FIELD 정보 제외..)
	 */
	public synchronized void loadAllMessageStructure(){
		long startTime = System.currentTimeMillis();
		DBResultSet allMessageStructureSet = DBManager.executePreparedQuery(Constants.SPIDER_DB, QueryManager.getQueryToMap(MessageStructureQuery.LOAD_ALL_MESSAGE_ID_MAP), new Object[]{});
		String structureId = null;
		MessageStructure structure = null;
		
		synchronized(structurePool){
			//저장된 전문 정보를 모두 지우고 새로 채운다.
			structurePool.clear();
			while(allMessageStructureSet.next()){
				structureId = MessageStructureUtil.getMessageStructureId(allMessageStructureSet.getString("MESSAGE_ID"), allMessageStructureSet.getString("ORG_ID"));
				structure = loadStructure(structureId, allMessageStructureSet);
				structurePool.put(structureId, structure);
			}
		}
		
		LogManager.debug("전문 structure 정보 로딩 시간:" + (System.currentTimeMillis()-startTime) +"ms");
		
	}
	
	/**
	 * 모든 Message Structure 정보를 로딩한다.
	 */
	public void loadAll(){
		loadAll(false);
	}

	/**
	 * Message Structure 정보를 로딩한다.
	 * XML 모드이면 XML에서 로딩한다.
	 * preLoadYn이 true이면 사전로딩여부가 'Y'인 메시지만 로딩하고
	 * false이면 모든 메시지를 로딩한다. 
	 * @param preLoadYn 사전로딩여부
	 */
	public synchronized void loadAll(boolean preLoadYn) {
        
		this.pool.clear();
		long startTime1 = 0, startTime2 = 0;
		
		
		try {
			//XML 모드이면
			if (isXmlMode()) {
			
				//XML모드인데 사전로딩여부가 true일 때는 로딩하지 않는다..
				//현재 전문 정보를 XML로 저장할 때 structure ID별 파일로 나누어 저장하기 때문에
				//사전로딩여부가 true인 전문을 일괄 로딩한다는 것이 불가능하다.
				if(preLoadYn){
					LogManager.info("XML모드이기 때문에 사전로딩여부가 true인 전문 일괄 로딩작업을 하지 않습니다.");
					return;
				}
				startTime1 = System.currentTimeMillis();
				startTime2 = startTime1;
				
				LogManager.info("XML에서 MessageStructurePool 정보를 로딩합니다..");
				fromXml();
				
			//DB 모드이면
			} else{
				String loadMessageIdSql = null;
				String loadMessageFieldSql = null;
				String loadMessageMappingSql = null;
				//사전로딩여부에 따라 조회하는 쿼리가 달라진다.
				if(preLoadYn){
					loadMessageIdSql = QueryManager.getQueryToMap(MessageStructureQuery.LOAD_ALL_PRE_LOAD_MESSAGE_ID_MAP);
					loadMessageFieldSql = MessageStructureQuery.LOAD_ALL_PRE_LOAD_MESSAGE_FILED;
					loadMessageMappingSql = MessageStructureQuery.LOAD_ALL_PRE_LOAD_MESSAGE_FIELD_MAPPING;
				}else{
					loadMessageIdSql = QueryManager.getQueryToMap(MessageStructureQuery.LOAD_ALL_MESSAGE_ID_MAP);
					loadMessageFieldSql = MessageStructureQuery.LOAD_ALL_MESSAGE_FILED;
					loadMessageMappingSql = MessageStructureQuery.LOAD_ALL_MESSAGE_FIELD_MAPPING;
				}
				startTime1 = System.currentTimeMillis();
				ArrayList param = new ArrayList();
				DBResultSet rs = DBManager.executePreparedQuery(Constants.SPIDER_DB, loadMessageIdSql, param);
				List fieldMapList = DBManager.executePreparedQueryToMapList(Constants.SPIDER_DB, loadMessageFieldSql, param);
				List mappingMapList = DBManager.executePreparedQueryToMapList(Constants.SPIDER_DB, loadMessageMappingSql, param);
				
				startTime2 = System.currentTimeMillis();
				String structureId = null;
				MessageStructure structure=null;
				
				while(rs.next()) {
	
					structureId = MessageStructureUtil.getMessageStructureId(rs.getString("MESSAGE_ID"), rs.getString("ORG_ID"));
										
					//1. Message Strucutre 정보를 구성한다.
					structure = loadStructure(structureId, rs);
					
					//2. 필드 정보와 매핑 정보를 structure id 별로 담는다.
					structure = loadField(fieldMapList, mappingMapList, structure, true);
					
					
				}//end while
			}
			
			isLoaded = true;
			long end = System.currentTimeMillis();
			LogManager.debug("MessageStructure 로딩 elapsed time (DB쿼리 시간 포함): " + (end-startTime1));
			LogManager.debug("MessageStructure 로딩 elapsed time (DB쿼리 시간 제외): " + (end-startTime2));
			
		} catch (Exception e) {
			LogManager.error("메시지 정보를 로드하는 중 오류가 발생하였습니다 : " + e.toString(), e);
			throw new SysException("메시지 정보를 로드하는 중 오류가 발생하였습니다.\n" + e.getMessage());
		}//end try catch
        
	}//end of loadAll()
	
	
	/**
	 * 특정 전문 정보 하나를 로딩한다. 
	 * XML 모드이면 XML에서 로딩한다.
	 * 디폴트로 리로딩요청여부가 true로 설정된다. (모든 정보를 전부 새로 읽어온다..)
	 * 
	 * @param orgId 기관 ID
	 * @param messageId 전문 ID
	 */
	public void load(String orgId, String messageId) {
		load(orgId, messageId, true);
	}
	
	/**
	 * 특정 전문 정보 하나를 로딩한다. 
	 * XML 모드이면 XML에서 로딩한다.
	 * isReloadRequest가 true인 경우는 전문 정보(FWK_MESSAGE)도 DB에서 새로 조회한다.
	 * isReloadRequest가 false인 경우는 전문 정보(FWK_MESSAGE)는 우선 structurePool에서 가져오고 없을 경우 DB에서 조회한다.
	 * 
	 * @param orgId 기관 ID
	 * @param messageId 전문 ID
	 * @param isReloadRequest 리로딩요청여부
	 */
	void load(String orgId, String messageId, boolean isReloadRequest) {
        
		long startTime1 = 0, startTime2 = 0;
		try {
			LogManager.info("isXmlMode()="+isXmlMode());
			MessageStructure structure=null;
			//XML 모드이면
			if (isXmlMode()) {
				//Message Structure ID 별 파일에서 읽어오는 것으로 수정
				fromXml(orgId, messageId);
			} else{
				startTime1 = System.currentTimeMillis();
				ArrayList param = new ArrayList();
				param.add(orgId);
				param.add(messageId);
				//DBResultSet rs = DBManager.executePreparedQuery(Constants.SPIDER_DB, QueryManager.getQueryToMap(MessageStructureQuery.LOAD_MESSAGE_ID_MAP), param);
				List fieldMapList = DBManager.executePreparedQueryToMapList(Constants.SPIDER_DB, MessageStructureQuery.LOAD_MESSAGE_FILED, param);
				List mappingMapList = DBManager.executePreparedQueryToMapList(Constants.SPIDER_DB, MessageStructureQuery.LOAD_MESSAGE_FIELD_MAPPING, param);
				
				
				String structureId = MessageStructureUtil.getMessageStructureId(messageId, orgId);
				
				//리로딩 요청이 아니고, structurePool에서 꺼낸 객체가 null이 아닌 경우
				if(!isReloadRequest && (structure = (MessageStructure)structurePool.remove(structureId))!=null){
					startTime2 = System.currentTimeMillis();
					//1. Message Strucutre 객체를 structurePool에서 꺼낸 후 structurePool로부터 제거한다.
					//위 if 문에서 수행
					
					//2. 필드 정보와 매핑 정보를 structure id 별로 담는다.
					structure = loadField(fieldMapList, mappingMapList, structure, false);
				
				}else{
					DBResultSet rs = DBManager.executePreparedQuery(Constants.SPIDER_DB, QueryManager.getQueryToMap(MessageStructureQuery.LOAD_MESSAGE_ID_MAP), param);
					startTime2 = System.currentTimeMillis();
					if(rs.next()) {
						//1. Message Strucutre를 생성하고 정보를 구성한다.
						structure = loadStructure(structureId, rs);
						
						//2. 필드 정보와 매핑 정보를 structure id 별로 담는다.
						structure = loadField(fieldMapList, mappingMapList, structure, false);
					}
				}
				
			}
			//Message Structure 로딩에 따라 재로딩해야 하는 object를 로딩시킨다..
			Vector list = MessageStructureUtil.getInstance().getMessageStructureDependentList();
			if(list!=null){
				for(int i=0; i<list.size(); i++){
					Loadable loadable = (Loadable)list.get(i);
					LogManager.debug(loadable.getClass().getName() +"을(를) load합니다.");
					if(loadable!=null) loadable.load(structure);
				}
			}
			if (!isXmlMode()) {
				long end = System.currentTimeMillis();
				LogManager.debug("MessageStructure 로딩 elapsed time (DB쿼리 시간 포함): " + (end-startTime1));
				LogManager.debug("MessageStructure 로딩 elapsed time (DB쿼리 시간 제외): " + (end-startTime2));
			}
		} catch (Exception e) {
            LogManager.error("메시지 정보를 로드하는 중 오류가 발생하였습니다 : " + e.toString(),e);
			throw new SysException("메시지 정보를 로드하는 중 오류가 발생하였습니다.\n" + e.getMessage());
		}//end try catch
        
	}//end of loadAll()

	/**
	 * FWK_MESSAGE 정보를 MessageStructure에 담는다. (필드 정보 제외)
	 * @param structureId
	 * @param rs
	 * @return
	 */
	private MessageStructure loadStructure(String structureId, DBResultSet rs) {
		
		//1. MessageStructure를 생성한다.
		MessageStructure structure = new MessageStructure(structureId);
		
		//2. 부모 메시지 structure ID 값을 넣는다.
		structure.setParentId(rs.getString("PARENT_MESSAGE_ID"));
		
		//3. 메시지 타입을 넣는다.
		structure.setMessageType(MessageType.getType(rs.getString("MESSAGE_TYPE")));
		
		//4. 기관 ID를 넣는다.
		structure.setOrgId(rs.getString("ORG_ID"));
		
		//5. 메세지 ID를 넣는다.
		structure.setMessageId(rs.getString("MESSAGE_ID"));
		
		//6. XML ROOT TAG를 넣는다.
		structure.setXmlRootTag(rs.getString("XML_ROOT_TAG"));
		
		//7. Remark 필드 존재 여부를 넣는다.
		structure.setRemark(rs.getInt("IS_REMARK")==1?true:false);
		
		//8. LOG_LEVEL를 넣는다.
		structure.setLogLevel(rs.getString("LOG_LEVEL"));
		
		return structure;
	}
	
	private class PredicateImpl implements Predicate{
		private String key;
		
		public PredicateImpl(){}
		
		public void setKey(String key){
			this.key = key;
		}
		
		public boolean evaluate(Object input){
			Map inputMap = (Map)input;
			
			if(inputMap.get("STRUCTURE_ID").equals(key)) return true;
			else return false;
		}
	}
	
	/**
	 * 인자로 받은 MessageStructure에 필드 정보와 맵핑 정보를 로딩한 후 pool에 넣는다.
	 * 
	 * @param fieldMapList 필드 정보 리스트
	 * @param mappingMapList 매핑 정보 리스트
	 * @param structureId 메시지 structure ID
	 * @param loadAll 메시지 일괄 로딩 여부(2개 이상의 메시지를 동시에 로딩 중인지 건건이 로딩 중인 지 여부) 
	 * @return MessageStructure
	 */
	private MessageStructure loadField(List fieldMapList, List mappingMapList, MessageStructure structure, boolean loadAll) {
        Collection col = null;
        //MessageStructure structure = new MessageStructure(structureId);
        String structureId = structure.getStructureId();
        
        try {
        		Iterator iter = null;
        		if(loadAll){
        			predicate.setKey(structureId);
        			col = CollectionUtils.select(fieldMapList, (Predicate)predicate);
        			fieldMapList.removeAll(col);
        		
        			iter = col.iterator();
        			structure.setSize(col.size());
        			
        		}else{
        			iter = fieldMapList.iterator();
        			structure.setSize(fieldMapList.size());
        		}
        		
        		//1. 필드 정보 로딩
			    Map fieldMap = null;
	            MessageField field = null;
	            String fieldName = null;
	            int fieldLength = 0;
	            int fieldScale = 0;
	           	String dataType = null;
	           	String defaultValue= null;
	           	String testValue= null;
	           	String remark= null;
	           	String filler= null;	
	           	int sortOrder = 0;
	           	String useMode = null;
	           	String align = null;
	           	String codeGroup = null;
	           	String fieldTag = null;
	           	boolean required = false;
                boolean logMode = false;
                String fieldType = null;
                boolean isCodeMapping = false;
			    while(iter.hasNext()){
			    	fieldMap = (HashMap)iter.next();
			    	fieldName = (String)fieldMap.get("MESSAGE_FIELD_ID");
			    			    	
					fieldLength =(BigDecimal)fieldMap.get("DATA_LENGTH")==null?0:((BigDecimal)fieldMap.get("DATA_LENGTH")).intValue();
					fieldScale =(BigDecimal)fieldMap.get("SCALE")==null?0:((BigDecimal)fieldMap.get("SCALE")).intValue(); 
					dataType = (String)fieldMap.get("DATA_TYPE");
	                defaultValue=(String)fieldMap.get("DEFAULT_VALUE");
	                fieldType = (String)fieldMap.get("FIELD_TYPE");
	                
	                testValue=(String)fieldMap.get("TEST_VALUE");
	                remark=(String)fieldMap.get("REMARK");
	                filler=(String)fieldMap.get("FILLER");
	                useMode=(String)fieldMap.get("USE_MODE");
	                align = (String)fieldMap.get("ALIGN");
	                codeGroup = (String)fieldMap.get("CODE_GROUP");
	                fieldTag = (String)fieldMap.get("FIELD_TAG");
	                required = "Y".equals((String)fieldMap.get("REQUIRED_YN"))?true:false;
                    logMode = "Y".equals((String)fieldMap.get("LOG_YN"))?true:false;
	                sortOrder = (BigDecimal)fieldMap.get("SORT_ORDER")==null?0:((BigDecimal)fieldMap.get("SORT_ORDER")).intValue();
	                isCodeMapping = "Y".equals((String)fieldMap.get("CODE_MAPPING_YN"))?true:false;
	                
	                field = structure.addField(fieldName, sortOrder, fieldLength, fieldScale,
	                		dataType, fieldType, defaultValue,testValue,remark,filler,useMode,align,codeGroup,fieldTag, required, logMode, isCodeMapping);

			    }
			    
			    //2. 매핑 정보 로딩
			    if(loadAll){
			    	col = CollectionUtils.select(mappingMapList, (Predicate)predicate);
			    	mappingMapList.removeAll(col);
			    	iter = col.iterator();
			 	}else{
        			iter = mappingMapList.iterator();
        		}
			    
			    fieldMap = null;
			    field = null;
			    
			    String structure_id = null;
			    String trgOrgId = null;
			    String trgMessageId = null;
			    String trgMessageFieldId = null;
			    String srcOrgId = null;
			    String srcMessageId = null;
			    String mappingExpression = null;
			   	
			    //필드 매핑 정보가 없더라도 빈 MessageFieldMappingMap를 만들어야 한다.
			    //MessageTransformation에서 매핑정보가 없더라도 코드 매핑을 하기 위함.
			   	MessageFieldMappingMap messageFieldMappings = MessageStructureUtil.getEmptyMessageFieldMappingMap();

			   	for(int i=0; iter.hasNext(); i++){
			    	if(i==0) messageFieldMappings = new MessageFieldMappingMap();
			   		fieldMap = (HashMap)iter.next();
			    	
			    	structure_id = (String)fieldMap.get("STRUCTURE_ID");     
			    	trgOrgId = (String)fieldMap.get("TRG_ORG_ID");         
			    	trgMessageId = (String)fieldMap.get("TRG_MESSAGE_ID");     
			    	trgMessageFieldId = (String)fieldMap.get("TRG_MESSAGE_FIELD_ID"); 
			    	srcOrgId =  (String)fieldMap.get("SRC_ORG_ID");         
			    	srcMessageId = (String)fieldMap.get("SRC_MESSAGE_ID");      
			    	mappingExpression = (String)fieldMap.get("MAPPING_EXPR"); 
			    		                
	                messageFieldMappings.put(srcOrgId, trgOrgId, trgMessageId, trgMessageFieldId, 
	                		new MessageFieldMapping(
	                				structure_id, trgOrgId, trgMessageId,                   
	                				trgMessageFieldId, srcOrgId, srcMessageId,  
	                				mappingExpression     
	                		));
	                
			    }
			
			    structure.setFieldMappingMap(messageFieldMappings);    			    
			    this.pool.put(structureId, structure);
			    return structure;
			    
		} catch (Throwable e) {
            //e.printStackTrace();
			throw new SysException("메시지 정보를 로드하는 중 오류가 발생하였습니다.\n" + e.getMessage());
		}//end try catch
	}//end of loadStructure()
	
	/**
	 * 특정 Message Structure ID에 해당하는 MessageStructure를 리턴한다.
	 * @param structureId Message Structure ID
	 * @return MessageStructure
	 */
	public MessageStructure getMessageStructure(String structureId) {
        
		return getMessageStructure(structureId, false);

	}//end of getMessageStructure()
    
	/**
	 * 특정 Message Structure ID에 해당하는 MessageStructure를 리턴한다.
	 * forceReloading값이 true이면 무조건 다시 MessageStructure 정보를 로딩하여 리턴하고
	 * false이면 풀에 없는 경우만 재로딩하여 리턴한다.
	 * 
	 * @param structureId Message Structure ID
	 * @param forceReloading 강제 리로딩 여부
	 * @return MessageStructure
	 */
	public MessageStructure getMessageStructure(String structureId, boolean forceReloading) {
		//전체 로딩하지 않고 structure별(기관-메시지별)로 로딩한다.
		//if(!isLoaded) loadAll();
		
		MessageStructure structure =  (MessageStructure)this.pool.get(structureId);
		
		//강제 리로딩 여부가 true인 경우(캐쉬된 structure가 존재하더라도 다시 로딩한다.) 
		if(forceReloading){
			String id[] = StringUtils.split(structureId, MessageStructureUtil.delimeter);
			load(id[1], id[0]);
			return (MessageStructure)this.pool.get(structureId);
			
		}else if(structure==null){
			String id[] = StringUtils.split(structureId, MessageStructureUtil.delimeter);
			load(id[1], id[0], false);
			return (MessageStructure)this.pool.get(structureId);
			
		}else{
			return structure;
			
		}
 
	}
	
	/**
	 * 풀에 담긴 Message Structure ID를 Iterator로 리턴한다.
	 * @return Iterator
	 */
	public Iterator getMessageStructureIDIterator(){
		if(!isLoaded) loadAll();
		Set keySet = this.pool.keySet();
		if(keySet==null) return null;
		else return keySet.iterator();
	}
	
	/* (non-Javadoc)
	 * @see nebsoa.management.ManagementObject#getManagementObject()
	 */
	public Object getManagementObject() {
		return getInstance();
	}

	/* (non-Javadoc)
	 * @see nebsoa.management.ManagementObject#setManagementObject(java.lang.Object)
	 */
	public void setManagementObject(Object obj) {
		instance = (MessageStructurePool) obj;
		
	}
	
    /**
     * 전체 리로딩
     * ManagementObject 메소드 오버라이딩
     */
    public static void reloadAll(DataMap dataMap) {
	    LogManager.info("MessageStructurePool을 전체 리로딩합니다..");
	    //instance가 null이면 getInstance()를 호출해서 객체 생성 및 로딩을 한다.
	    //이미 생성되어 있으면 다시 로딩하는 함수만 호출한다.
	    if(instance==null) getInstance();
	    else instance.loadAll();
	    
    }
    
    /**
     * Message Structure ID 별 리로딩
     * ManagementObject 메소드 오버라이딩
     */
    public static void reload(DataMap dataMap) {
    	String orgId = dataMap.getString(MSG_POOL_ORG_ID);
    	String messageId = dataMap.getString(MSG_POOL_MESSAGE_ID);
    	if(orgId==null || messageId==null) 
    		throw new IllegalArgumentException("기관ID 또는  MessageID가 존재하지 않습니다. [기관ID=" + orgId +", MessageID=" + messageId +"]");
    	
    	LogManager.info(new StringBuffer("MessageStructurePool을 리로딩합니다[").append(messageId).append("@").append(orgId).append("]").toString());
	    //instance가 null이면 getInstance()를 호출해서 객체 생성 및 로딩을 한다.
	    //이미 생성되어 있으면 다시 로딩하는 함수만 호출한다.
    	
	    if(instance==null) getInstance();
	    instance.load(orgId, messageId);
	    instance.toXml(dataMap);
	    
    }
    
    /**
     * 특정 Message Structure 객체를 XML로 write한다.
     * 인자로 넘어가는 DataMap에 대상이되는 Message Structure의 기관 ID와 Message ID가 담겨있어야 한다.
     * 기관 ID의 key :  _$MSG_POOL_ORG_ID
     * Message ID : _$MSG_POOL_MESSAGE_ID
     *  
     * @param dataMap
     */
    public void toXml(DataMap dataMap) {
        boolean isXmlSaveMode = PropertyManager.getBooleanProperty(
                "management", "OPERATION_INFO_XML_SAVE_MODE", "OFF");
        LogManager.debug("운영정보파일 저장모드:" + isXmlSaveMode);
        if (isXmlSaveMode) {    	
	    	String orgId = dataMap.getString(MSG_POOL_ORG_ID);
	    	String messageId = dataMap.getString(MSG_POOL_MESSAGE_ID);
	    	if(orgId==null || messageId==null) 
	    		throw new IllegalArgumentException("기관ID 또는  MessageID가 존재하지 않습니다. [기관ID=" + orgId +", MessageID=" + messageId +"]");
	    	
	    	String structureId = MessageStructureUtil.getMessageStructureId(messageId, orgId);
	    	Object structure = this.pool.get(structureId);
	    	if(structure==null) throw new SysException("structure 객체가 null입니다. [structureId="+ structureId +"]");
	    	
	    	DbToXml.toXml(getXmlFileName(orgId, messageId), this.pool.get(structureId));
        }
    }
    public static void toXmlConfig(DataMap dataMap){
    	LogManager.debug("##### MessageStructurePool.toXmlConfig()########");
    	getInstance().toXml(dataMap);
    }
    /**
     * 특정 Message Structure 객체를 XML로부터 읽어온다.
     * 인자로 넘어가는 DataMap에 대상이되는 Message Structure의 기관 ID와 Message ID가 담겨있어야 한다.
     * 기관 ID의 key :  _$MSG_POOL_ORG_ID
     * Message ID : _$MSG_POOL_MESSAGE_ID
     * 
     * @param dataMap
     * @throws FileNotFoundException
     */
    public void fromXml(DataMap dataMap) throws FileNotFoundException {
    	String orgId = dataMap.getString(MSG_POOL_ORG_ID);
    	String messageId = dataMap.getString(MSG_POOL_MESSAGE_ID);
    	fromXml(orgId, messageId);
    }
    
    private void fromXml(String orgId, String messageId) throws FileNotFoundException {
    	LogManager.debug("XML로부터 Message Structure 정보를 로딩합니다.");
    	if(orgId==null || messageId==null) 
    		throw new IllegalArgumentException("기관ID 또는  MessageID가 존재하지 않습니다. [기관ID=" + orgId +", MessageID=" + messageId +"]");
    	
    	String structureId = MessageStructureUtil.getMessageStructureId(messageId, orgId);
    	this.pool.put(structureId, DbToXml.fromXml(getXmlFileName(orgId, messageId)));
    }
    
    /**
     * Message Structure 객체가 저장될 XML 파일명을 리턴한다.
     * 
     * @param orgId 대상 Message Structure의 기관 ID
     * @param messageId 대상 Message Structure의 Message ID
     * @return XML 파일명
     */
    protected String getXmlFileName(String orgId, String messageId) {
    	return new StringBuffer(getXmlFileName()).append("_").append(messageId).append("_").append(orgId).append(".xml").toString();
    }
    
    /**
     * 풀 정보를 모두 clear 한다.
     */
    public void clear(){
    	this.pool.clear();
    }
}// end of MessageStructurePool.java