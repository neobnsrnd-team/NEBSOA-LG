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
import java.util.HashMap;

import org.apache.commons.collections.map.MultiKeyMap;

import nebsoa.common.Constants;
import nebsoa.common.exception.SysException;
import nebsoa.common.jdbc.DBManager;
import nebsoa.common.jdbc.DBResultSet;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.DataMap;
import nebsoa.common.util.DbTypeUtil;
import nebsoa.common.util.QueryManager;
import nebsoa.management.ManagementObject;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 메시지 코드 정보를 로드하여 캐싱하는 풀 클래스
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
 * $Log: MessageCodePool.java,v $
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
 * Revision 1.1  2008/01/22 05:58:20  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.5  2008/01/09 10:07:42  홍윤석
 * Mysql일 경우 추가
 *
 * Revision 1.4  2007/12/28 05:49:02  안경아
 * *** empty log message ***
 *
 * Revision 1.3  2007/12/12 08:08:59  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2007/11/30 09:46:54  안경아
 * DB NAME 지정
 *
 * Revision 1.1  2007/11/26 08:38:05  안경아
 * *** empty log message ***
 *
 * Revision 1.12  2006/11/14 08:15:40  안경아
 * *** empty log message ***
 *
 * Revision 1.11  2006/08/03 01:36:29  김승희
 * ManagementObject 적용
 *
 * Revision 1.10  2006/08/01 06:59:13  김승희
 * 우선순위를 적용한 다 대 다 맵핑 구현
 *
 * Revision 1.9  2006/07/04 09:31:29  김승희
 * 패키지 변경
 *
 * Revision 1.8  2006/07/04 08:39:03  김승희
 * 참조 클래스 패키지 변경
 *
 * Revision 1.7  2006/06/21 08:41:30  김승희
 * 코드 검증 추가
 *
 * Revision 1.6  2006/06/17 10:12:09  김승희
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public class MessageCodePool extends ManagementObject {
	
	private static MessageCodePool instance = new MessageCodePool();
	
	private MultiKeyMap stdCodePool;
	private MultiKeyMap orgCodepool;	
	private static HashMap LOAD_ALL_CODE_MAP = new HashMap();
	
	private boolean isLoaded = false;
	
	private MessageCodePool() {
		this.stdCodePool = new MultiKeyMap();
		this.orgCodepool = new MultiKeyMap();
	}//end of constructor
	
	public static MessageCodePool getInstance() {
		if(instance==null) instance = new MessageCodePool();
		LOAD_ALL_CODE_MAP.put(Integer.valueOf(DbTypeUtil.ORACLE).toString(), instance.LOAD_ALL_CODE_ORA_MAPPING);
		LOAD_ALL_CODE_MAP.put(Integer.valueOf(DbTypeUtil.DB2).toString(), instance.LOAD_ALL_CODE_DB2_MAPPING);
		LOAD_ALL_CODE_MAP.put(Integer.valueOf(DbTypeUtil.MY_SQL).toString(), instance.LOAD_ALL_CODE_MYSQL_MAPPING);
		if(!instance.isLoaded) instance.loadAll();
		return instance;
	}//end of getInstance()
	
	/**
	 * 모든 코드 정보와 매핑정보를 조회하는 쿼리
	 * 주의 : order by 절에 의해 매핑 우선순위(PRIORITY)가 적용되므로 제거하지 말아야 한다.
	 * 현재 PRIORITY는 값이 작을 수록 우선순위가 높다. --> 따라서 desc 정렬 되어 있음.
	 */
	private String LOAD_ALL_CODE_ORA_MAPPING = 
		/* "\n SELECT O.ORG_ID, C.CODE_GROUP_ID,                       "             
		+"\n O.ORG_CODE, C.CODE, C.CODE_NAME                         "             
		+"\n FROM FWK_CODE C,                                        "             
		+"\n (SELECT 'STD' ORG_ID, CODE_GROUP_ID, CODE, CODE ORG_CODE   "             
		+"\n FROM FWK_CODE                                           "             
		+"\n UNION ALL                                               "             
		+"\n SELECT ORG_ID, CODE_GROUP_ID, CODE, ORG_CODE            "             
		+"\n FROM FWK_ORG_CODE) O                                    "             
		+"\n WHERE                                                   "             
		+"\n C.CODE_GROUP_ID = O.CODE_GROUP_ID                       "             
		+"\n AND C.CODE = O.CODE                                     ";*/
		
		 "\r\n SELECT O.ORG_ID, C.CODE_GROUP_ID,                       "  
		+"\r\n O.ORG_CODE, C.CODE, C.CODE_NAME,                        "  
		+"\r\n CASE O.PRIORITY WHEN NULL THEN '1'                      "  
		+"\r\n ELSE O.PRIORITY                                         "  
		+"\r\n END PRIORITY                                            "  
		+"\r\n FROM FWK_CODE C,                                        "  
		+"\r\n (SELECT 'STD' ORG_ID, CODE_GROUP_ID, CODE,              "  
		+"\r\n 		  CODE ORG_CODE, '1' PRIORITY                      "  
		+"\r\n  FROM FWK_CODE                                          "  
		+"\r\n  UNION ALL                                              "  
		+"\r\n  SELECT ORG_ID, CODE_GROUP_ID, CODE, ORG_CODE, PRIORITY "  
		+"\r\n  FROM FWK_ORG_CODE                                      "  
		+"\r\n ) O                                                     "  
		+"\r\n WHERE                                                   "  
		+"\r\n C.CODE_GROUP_ID = O.CODE_GROUP_ID                       "  
		+"\r\n AND C.CODE = O.CODE                                     "
	    +"\r\n ORDER BY ORG_ID, CODE_GROUP_ID, PRIORITY DESC" ;
	
	private String LOAD_ALL_CODE_DB2_MAPPING =		
		 "\r\n SELECT O.ORG_ID, C.CODE_GROUP_ID,                       "  
		+"\r\n O.ORG_CODE, C.CODE, C.CODE_NAME,                        "  
		+"\r\n COALESCE(O.PRIORITY, '1', O.PRIORITY) PRIORITY		   "  
		+"\r\n FROM FWK_CODE C,                                        "  
		+"\r\n (SELECT 'STD' ORG_ID, CODE_GROUP_ID, CODE,              "  
		+"\r\n 		  CODE ORG_CODE, '1' PRIORITY                      "  
		+"\r\n  FROM FWK_CODE                                          "  
		+"\r\n  UNION ALL                                              "  
		+"\r\n  SELECT ORG_ID, CODE_GROUP_ID, CODE, ORG_CODE, PRIORITY "  
		+"\r\n  FROM FWK_ORG_CODE                                      "  
		+"\r\n ) O                                                     "  
		+"\r\n WHERE                                                   "  
		+"\r\n C.CODE_GROUP_ID = O.CODE_GROUP_ID                       "  
		+"\r\n AND C.CODE = O.CODE                                     "
	    +"\r\n ORDER BY ORG_ID, CODE_GROUP_ID, PRIORITY DESC" ;

	private String LOAD_ALL_CODE_MYSQL_MAPPING =		
		 "\r\n SELECT O.ORG_ID, C.CODE_GROUP_ID,                       "  
		+"\r\n O.ORG_CODE, C.CODE, C.CODE_NAME,                        "  
		+"\r\n COALESCE(O.PRIORITY, '1', O.PRIORITY) PRIORITY		   "  
		+"\r\n FROM FWK_CODE C,                                        "  
		+"\r\n (SELECT 'STD' ORG_ID, CODE_GROUP_ID, CODE,              "  
		+"\r\n 		  CODE ORG_CODE, '1' PRIORITY                      "  
		+"\r\n  FROM FWK_CODE                                          "  
		+"\r\n  UNION ALL                                              "  
		+"\r\n  SELECT ORG_ID, CODE_GROUP_ID, CODE, ORG_CODE, PRIORITY "  
		+"\r\n  FROM FWK_ORG_CODE                                      "  
		+"\r\n ) O                                                     "  
		+"\r\n WHERE                                                   "  
		+"\r\n C.CODE_GROUP_ID = O.CODE_GROUP_ID                       "  
		+"\r\n AND C.CODE = O.CODE                                     "
	    +"\r\n ORDER BY ORG_ID, CODE_GROUP_ID, PRIORITY DESC" ;
	
	public void loadAll() {
		this.orgCodepool.clear();
		this.stdCodePool.clear();
		try {
			
			//XML 모드이면
			if (isXmlMode()) {
				LogManager.info("XML에서 MessageCodePool 정보를 로딩합니다..");
				fromXml();
			} else{
				DBResultSet rs = DBManager.executePreparedQuery(Constants.SPIDER_DB, QueryManager.getQueryToMap(LOAD_ALL_CODE_MAP), new ArrayList());
				MessageCode messageCode = null;
				String orgId = null;
				String codeGroupId = null;
				String orgCode = null;
				String code = null;
				String codeName = null;
				String priority = null;
				
				for(int i=0; rs.next(); i++) {
					orgId = rs.getString("ORG_ID");
					codeGroupId = rs.getString("CODE_GROUP_ID");
					orgCode = rs.getString("ORG_CODE");
					code = rs.getString("CODE");
					codeName = rs.getString("CODE_NAME");
					priority = rs.getString("PRIORITY");
					
					messageCode = new MessageCode(orgId, codeGroupId, orgCode, code, codeName, priority);
	
					stdCodePool.put(orgId, codeGroupId, orgCode, messageCode);
					orgCodepool.put(orgId, codeGroupId, code, messageCode);
					
				}//end while
			}
			isLoaded = true;
		} catch (Exception e) {
			throw new SysException("코드 정보를 로드하는 중 오류가 발생하였습니다.\n" + e.getMessage());
		}//end try catch
        
	}//end of loadAll()
		  
	/**
	 * 표준코드로 기관 코드를 얻어온다.
	 * 기관 코드를 알기 위해서는 리턴받은 객체의 getOrgCode를 호출해야 한다.
	 *  
	 * @param orgId 대상 기관 ID
	 * @param codeGroup 코드그룹
	 * @param code 표준 코드값
	 * @return MessageCode 객체
	 */
	public MessageCode getOrgCode(String orgId, String codeGroup, String code) {
       
		return (MessageCode)this.orgCodepool.get(orgId, codeGroup, code);
        
	}//end of getMessageStructure()
    
	/**
	 * 기관코드로 표준 코드를 얻어온다.
	 * 표준 코드를 알기 위해서는 리턴받은 객체의 getCode를 호출해야 한다.
	 * 
	 * @param orgId 기관 ID
	 * @param codeGroup 코드그룹
	 * @param orgCode 기관 코드값
	 * @return
	 */
	public MessageCode getStdCode(String orgId, String codeGroup, String orgCode) {
	       
		return (MessageCode)this.stdCodePool.get(orgId, codeGroup, orgCode);
		        
	}//end of getMessageStructure()
	
	/**
	 * 올바른 코드 값인지 리턴한다.
	 * @param orgId 기관 ID
	 * @param codeGroup 코드그룹
	 * @param code 코드
	 * @return true 또는 false
	 */
	public boolean isValidCode(String orgId, String codeGroup, String code){
		if(orgId.equals(MessageConstants.ORG_STD)){
			return orgCodepool.containsKey(orgId, codeGroup, code);
		}else{
			return stdCodePool.containsKey(orgId, codeGroup, code);
		}
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
		instance = (MessageCodePool)obj;
		
	}
	
    /**
     * 리로딩
     * ManagementObject 메소드 오버라이딩
     */
    public static void reloadAll(DataMap map) {
	    LogManager.debug("MessageCodePool을 리로딩합니다..");
	    //instance가 null이면 getInstance()를 호출해서 객체 생성 및 로딩을 한다.
	    //이미 생성되어 있으면 다시 로딩하는 함수만 호출한다.
	    getInstance();
	    instance.toXml();
	    
    }
    
    public static void main(String [] args){
        System.setProperty("SPIDER_HOME","d:/serverside/spider");
                
        MessageCode m = MessageCodePool.getInstance().getOrgCode("HOST", "00003", "03");
        MessageCode m2 = MessageCodePool.getInstance().getStdCode("HOST", "00003", "3000");
        MessageCode m3 = MessageCodePool.getInstance().getStdCode("HOST", "00003", "5000");
        MessageCode m4 = MessageCodePool.getInstance().getStdCode("HOST", "00003", "1000");
        
        MessageCode m5 = MessageCodePool.getInstance().getOrgCode("HOST", "00002", "2");
        MessageCode m6 = MessageCodePool.getInstance().getOrgCode("HOST", "00002", "3");
        
        MessageCode m7 = MessageCodePool.getInstance().getStdCode("HOST", "00002", "02");
        
        System.out.println(m.getOrgCode() + ":" + m.getCodeName());
        System.out.println(m2.getCode() + ":" + m2.getCodeName());
        System.out.println(m3.getCode() + ":" + m3.getCodeName());
        System.out.println(m4.getCode() + ":" + m4.getCodeName());
        
        System.out.println(m5.getOrgCode() + ":" + m5.getCodeName());
        System.out.println(m6.getOrgCode() + ":" + m6.getCodeName());
        
        System.out.println(m7.getCode() + ":" + m7.getCodeName());
        
        System.out.println(MessageCodePool.getInstance().isValidCode("HOST", "00003", "1000"));
        System.out.println(MessageCodePool.getInstance().isValidCode("HOST", "00003", "3000"));
        System.out.println(MessageCodePool.getInstance().isValidCode("HOST", "00003", "5000"));
        System.out.println(MessageCodePool.getInstance().isValidCode("HOST", "00003", "4000"));
        System.out.println(MessageCodePool.getInstance().isValidCode("STD", "00003", "03"));
        System.out.println(MessageCodePool.getInstance().isValidCode("STD", "00002", "3"));
    }



}// end of MessageStructurePool.java