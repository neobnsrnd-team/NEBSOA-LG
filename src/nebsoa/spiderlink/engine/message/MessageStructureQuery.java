/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
package nebsoa.spiderlink.engine.message;

import java.util.HashMap;

import nebsoa.common.util.DbTypeUtil;

/*******************************************************************
 * <pre>
 * 1.설명 
 * MessageStructurePool 정보 로딩에 사용되는 DB 쿼리 문을 담은 클래스 
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
 * $Log: MessageStructureQuery.java,v $
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
 * Revision 1.1  2008/07/07 09:14:36  김승희
 * 최초 등록
 *
 *
 *
 * </pre>
 ******************************************************************/
class MessageStructureQuery {
	
	
	public static HashMap LOAD_ALL_MESSAGE_ID_MAP = new HashMap();
	public static HashMap LOAD_ALL_PRE_LOAD_MESSAGE_ID_MAP = new HashMap();
	public static HashMap LOAD_MESSAGE_ID_MAP = new HashMap();
	
	
	static{
		setLoadQuery();
	}
	//DB 타입 별로 전문 정보 로딩하는 쿼리를 쿼리맵에 세팅한다. 
	private static void setLoadQuery() {
		LOAD_ALL_MESSAGE_ID_MAP.put(Integer.valueOf(DbTypeUtil.ORACLE).toString(), LOAD_ALL_MESSAGE_ID_ORA);
		LOAD_ALL_MESSAGE_ID_MAP.put(Integer.valueOf(DbTypeUtil.DB2).toString(), LOAD_ALL_MESSAGE_ID_DB2);
		LOAD_ALL_MESSAGE_ID_MAP.put(Integer.valueOf(DbTypeUtil.MY_SQL).toString(), LOAD_ALL_MESSAGE_ID_MYSQL);
		LOAD_ALL_PRE_LOAD_MESSAGE_ID_MAP.put(Integer.valueOf(DbTypeUtil.ORACLE).toString(), LOAD_ALL_PRE_LOAD_MESSAGE_ID_ORA);
		LOAD_ALL_PRE_LOAD_MESSAGE_ID_MAP.put(Integer.valueOf(DbTypeUtil.DB2).toString(), LOAD_ALL_PRE_LOAD_MESSAGE_ID_DB2);
		LOAD_ALL_PRE_LOAD_MESSAGE_ID_MAP.put(Integer.valueOf(DbTypeUtil.MY_SQL).toString(), LOAD_ALL_PRE_LOAD_MESSAGE_ID_MYSQL);
		LOAD_MESSAGE_ID_MAP.put(Integer.valueOf(DbTypeUtil.ORACLE).toString(), LOAD_MESSAGE_ID_ORA);
		LOAD_MESSAGE_ID_MAP.put(Integer.valueOf(DbTypeUtil.DB2).toString(), LOAD_MESSAGE_ID_DB2);
		LOAD_MESSAGE_ID_MAP.put(Integer.valueOf(DbTypeUtil.MY_SQL).toString(), LOAD_MESSAGE_ID_MYSQL);
	}
	
	
	/**
	 * 모든 메시지 조회(ORACLE)
	 */	
	static final String LOAD_ALL_MESSAGE_ID_ORA = 
		 "\r\n SELECT                                                                 " 
		+"\r\n M.ORG_ID,                                                              " 
		+"\r\n M.MESSAGE_ID,                                                          " 
		+"\r\n M.MESSAGE_NAME,                                                        " 
		+"\r\n M.MESSAGE_TYPE,                                                        " 
		+"\r\n M.PARENT_MESSAGE_ID,                                                   " 
		+"\r\n O.XML_ROOT_TAG,                                                        " 
		+"\r\n NVL(R.CNT, 0) IS_REMARK,                                               "
		+"\r\n LOG_LEVEL                                                			  "		
		+"\r\n FROM                                                                   " 
		+"\r\n FWK_MESSAGE M, FWK_ORG O,                                              " 
		+"\r\n (SELECT M.MESSAGE_ID, M.ORG_ID, 1 CNT                                  " 
		+"\r\n     FROM FWK_MESSAGE M WHERE EXISTS                                    " 
		+"\r\n 	(SELECT * FROM FWK_MESSAGE_FIELD F WHERE F.REMARK                     " 
		+"\r\n 	IS NOT NULL AND M.MESSAGE_ID = F.MESSAGE_ID AND M.ORG_ID = F.ORG_ID ) " 
		+"\r\n ) R                                                                    " 
		+"\r\n WHERE M.ORG_ID = O.ORG_ID                                              " 
		+"\r\n AND M.MESSAGE_ID = R.MESSAGE_ID(+)                                     " 
		+"\r\n AND M.ORG_ID = R.ORG_ID(+)                                             " ;

	/**
	 * PRE_LOAD_YN이 Y인 모든 메시지 조회(ORACLE)
	 */
	static final String LOAD_ALL_PRE_LOAD_MESSAGE_ID_ORA = 
		 "\r\n SELECT                                                                 " 
		+"\r\n M.ORG_ID,                                                              " 
		+"\r\n M.MESSAGE_ID,                                                          " 
		+"\r\n M.MESSAGE_NAME,                                                        " 
		+"\r\n M.MESSAGE_TYPE,                                                        " 
		+"\r\n M.PARENT_MESSAGE_ID,                                                   " 
		+"\r\n O.XML_ROOT_TAG,                                                        " 
		+"\r\n NVL(R.CNT, 0) IS_REMARK,                                               "
		+"\r\n LOG_LEVEL                                                			  "		
		+"\r\n FROM                                                                   " 
		+"\r\n FWK_MESSAGE M, FWK_ORG O,                                              " 
		+"\r\n (SELECT M.MESSAGE_ID, M.ORG_ID, 1 CNT                                  " 
		+"\r\n     FROM FWK_MESSAGE M WHERE EXISTS                                    " 
		+"\r\n 	(SELECT * FROM FWK_MESSAGE_FIELD F WHERE F.REMARK                     " 
		+"\r\n 	IS NOT NULL AND M.MESSAGE_ID = F.MESSAGE_ID AND M.ORG_ID = F.ORG_ID ) " 
		+"\r\n ) R                                                                    " 
		+"\r\n WHERE M.ORG_ID = O.ORG_ID                                              " 
		+"\r\n AND M.MESSAGE_ID = R.MESSAGE_ID(+)                                     " 
		+"\r\n AND M.ORG_ID = R.ORG_ID(+)                                             " 
		+"\r\n AND M.PRE_LOAD_YN = 'Y'                                             	  " ;

	/**
	 * 특정 기관의 특정 메시지 조회(ORACLE)
	 */
	static final String LOAD_MESSAGE_ID_ORA = 
		 "\r\n SELECT                                                                 " 
		+"\r\n M.ORG_ID,                                                              " 
		+"\r\n M.MESSAGE_ID,                                                          " 
		+"\r\n M.MESSAGE_NAME,                                                        " 
		+"\r\n M.MESSAGE_TYPE,                                                        " 
		+"\r\n M.PARENT_MESSAGE_ID,                                                   " 
		+"\r\n O.XML_ROOT_TAG,                                                        " 
		+"\r\n NVL(R.CNT, 0) IS_REMARK,                                               "
		+"\r\n LOG_LEVEL                                                			  "
		+"\r\n FROM                                                                   " 
		+"\r\n FWK_MESSAGE M, FWK_ORG O,                                              " 
		+"\r\n (SELECT M.MESSAGE_ID, M.ORG_ID, 1 CNT                                  " 
		+"\r\n     FROM FWK_MESSAGE M WHERE EXISTS                                    " 
		+"\r\n 	(SELECT * FROM FWK_MESSAGE_FIELD F WHERE F.REMARK                     " 
		+"\r\n 	IS NOT NULL AND M.MESSAGE_ID = F.MESSAGE_ID AND M.ORG_ID = F.ORG_ID ) " 
		+"\r\n ) R                                                                    " 
		+"\r\n WHERE M.ORG_ID = O.ORG_ID                                              " 
		+"\r\n AND M.MESSAGE_ID = R.MESSAGE_ID(+)                                     " 
		+"\r\n AND M.ORG_ID = R.ORG_ID(+)                                             " 
	    +"\r\n AND M.ORG_ID = ? AND M.MESSAGE_ID = ?                                  ";

	
	/**
	 * 모든 메시지 조회(DB2)
	 */
	static final String LOAD_ALL_MESSAGE_ID_DB2 = 
		 "\r\n SELECT                                                                 " 
		+"\r\n M.ORG_ID,                                                              " 
		+"\r\n M.MESSAGE_ID,                                                          " 
		+"\r\n M.MESSAGE_NAME,                                                        " 
		+"\r\n M.MESSAGE_TYPE,                                                        " 
		+"\r\n M.PARENT_MESSAGE_ID,                                                   " 
		+"\r\n O.XML_ROOT_TAG,                                                        " 
		+"\r\n COALESCE(R.CNT, 0) IS_REMARK,                                          "
		+"\r\n LOG_LEVEL                                                			  "		
		+"\r\n FROM                                                                   " 
		+"\r\n FWK_MESSAGE M                                                               " 
		+"\r\n INNER JOIN FWK_ORG O ON M.ORG_ID = O.ORG_ID                                 " 
		+"\r\n LEFT OUTER JOIN                                                             " 
		+"\r\n (SELECT M.MESSAGE_ID, M.ORG_ID, 1 CNT                                       " 
		+"\r\n     FROM FWK_MESSAGE M WHERE EXISTS                                         " 
		+"\r\n        (SELECT * FROM FWK_MESSAGE_FIELD F WHERE F.REMARK                    " 
		+"\r\n        IS NOT NULL AND M.MESSAGE_ID = F.MESSAGE_ID AND M.ORG_ID = F.ORG_ID )" 
		+"\r\n ) R                                                                         " 
		+"\r\n ON M.MESSAGE_ID = R.MESSAGE_ID                                              " 
		+"\r\n AND M.ORG_ID = R.ORG_ID                                                     ";
	
	/**
	 * PRE_LOAD_YN이 Y인 모든 메시지 조회(DB2)
	*/
	static final String LOAD_ALL_PRE_LOAD_MESSAGE_ID_DB2 = 
		 "\r\n SELECT                                                                 " 
		+"\r\n M.ORG_ID,                                                              " 
		+"\r\n M.MESSAGE_ID,                                                          " 
		+"\r\n M.MESSAGE_NAME,                                                        " 
		+"\r\n M.MESSAGE_TYPE,                                                        " 
		+"\r\n M.PARENT_MESSAGE_ID,                                                   " 
		+"\r\n O.XML_ROOT_TAG,                                                        " 
		+"\r\n COALESCE(R.CNT, 0) IS_REMARK,                                          "
		+"\r\n LOG_LEVEL                                                			  "		
		+"\r\n FROM                                                                   " 
		+"\r\n FWK_MESSAGE M                                                               " 
		+"\r\n INNER JOIN FWK_ORG O ON M.ORG_ID = O.ORG_ID                                 " 
		+"\r\n LEFT OUTER JOIN                                                             " 
		+"\r\n (SELECT M.MESSAGE_ID, M.ORG_ID, 1 CNT                                       " 
		+"\r\n     FROM FWK_MESSAGE M WHERE EXISTS                                         " 
		+"\r\n        (SELECT * FROM FWK_MESSAGE_FIELD F WHERE F.REMARK                    " 
		+"\r\n        IS NOT NULL AND M.MESSAGE_ID = F.MESSAGE_ID AND M.ORG_ID = F.ORG_ID )" 
		+"\r\n ) R                                                                         " 
		+"\r\n ON M.MESSAGE_ID = R.MESSAGE_ID                                              " 
		+"\r\n AND M.ORG_ID = R.ORG_ID                                                     " 
		+"\r\n WHERE M.PRE_LOAD_YN = 'Y'                                             	  " ;
	 
	/**
	 * 특정 기관의 특정 메시지 조회(DB2)
	 */
	static final String LOAD_MESSAGE_ID_DB2 = 
		 "\r\n SELECT                                                                 " 
		+"\r\n M.ORG_ID,                                                              " 
		+"\r\n M.MESSAGE_ID,                                                          " 
		+"\r\n M.MESSAGE_NAME,                                                        " 
		+"\r\n M.MESSAGE_TYPE,                                                        " 
		+"\r\n M.PARENT_MESSAGE_ID,                                                   " 
		+"\r\n O.XML_ROOT_TAG,                                                        " 
		+"\r\n COALESCE(R.CNT, 0) IS_REMARK,                                          "
		+"\r\n LOG_LEVEL                                                			  "		
		+"\r\n FROM                                                                   " 
		+"\r\n FWK_MESSAGE M                                                               " 
		+"\r\n INNER JOIN FWK_ORG O ON M.ORG_ID = O.ORG_ID                                 " 
		+"\r\n LEFT OUTER JOIN                                                             " 
		+"\r\n (SELECT M.MESSAGE_ID, M.ORG_ID, 1 CNT                                       " 
		+"\r\n     FROM FWK_MESSAGE M WHERE EXISTS                                         " 
		+"\r\n        (SELECT * FROM FWK_MESSAGE_FIELD F WHERE F.REMARK                    " 
		+"\r\n        IS NOT NULL AND M.MESSAGE_ID = F.MESSAGE_ID AND M.ORG_ID = F.ORG_ID )" 
		+"\r\n ) R                                                                         " 
		+"\r\n ON M.MESSAGE_ID = R.MESSAGE_ID                                              " 
		+"\r\n AND M.ORG_ID = R.ORG_ID                                                     " 
	    +"\r\n WHERE M.ORG_ID = ? AND M.MESSAGE_ID = ?                                  ";
	
	
	
	/**
	 * 모든 메시지 조회(MYSQL)
	 */
	static final String LOAD_ALL_MESSAGE_ID_MYSQL = 
		 "\r\n SELECT                                                                 " 
		+"\r\n M.ORG_ID,                                                              " 
		+"\r\n M.MESSAGE_ID,                                                          " 
		+"\r\n M.MESSAGE_NAME,                                                        " 
		+"\r\n M.MESSAGE_TYPE,                                                        " 
		+"\r\n M.PARENT_MESSAGE_ID,                                                   " 
		+"\r\n O.XML_ROOT_TAG,                                                        " 
		+"\r\n COALESCE(R.CNT, 0) IS_REMARK,                                          "
		+"\r\n LOG_LEVEL                                                			  "		
		+"\r\n FROM                                                                   " 
		+"\r\n FWK_MESSAGE M                                                               " 
		+"\r\n INNER JOIN FWK_ORG O ON M.ORG_ID = O.ORG_ID                                 " 
		+"\r\n LEFT OUTER JOIN                                                             " 
		+"\r\n (SELECT M.MESSAGE_ID, M.ORG_ID, 1 CNT                                       " 
		+"\r\n     FROM FWK_MESSAGE M WHERE EXISTS                                         " 
		+"\r\n        (SELECT * FROM FWK_MESSAGE_FIELD F WHERE F.REMARK                    " 
		+"\r\n        IS NOT NULL AND M.MESSAGE_ID = F.MESSAGE_ID AND M.ORG_ID = F.ORG_ID )" 
		+"\r\n ) R                                                                         " 
		+"\r\n ON M.MESSAGE_ID = R.MESSAGE_ID                                              " 
		+"\r\n AND M.ORG_ID = R.ORG_ID                                                     ";
	
	/**
	 * PRE_LOAD_YN이 Y인 모든 메시지 조회(MYSQL)
	*/
	static final String LOAD_ALL_PRE_LOAD_MESSAGE_ID_MYSQL = 
		 "\r\n SELECT                                                                 " 
		+"\r\n M.ORG_ID,                                                              " 
		+"\r\n M.MESSAGE_ID,                                                          " 
		+"\r\n M.MESSAGE_NAME,                                                        " 
		+"\r\n M.MESSAGE_TYPE,                                                        " 
		+"\r\n M.PARENT_MESSAGE_ID,                                                   " 
		+"\r\n O.XML_ROOT_TAG,                                                        " 
		+"\r\n COALESCE(R.CNT, 0) IS_REMARK,                                          "
		+"\r\n LOG_LEVEL                                                			  "		
		+"\r\n FROM                                                                   " 
		+"\r\n FWK_MESSAGE M                                                               " 
		+"\r\n INNER JOIN FWK_ORG O ON M.ORG_ID = O.ORG_ID                                 " 
		+"\r\n LEFT OUTER JOIN                                                             " 
		+"\r\n (SELECT M.MESSAGE_ID, M.ORG_ID, 1 CNT                                       " 
		+"\r\n     FROM FWK_MESSAGE M WHERE EXISTS                                         " 
		+"\r\n        (SELECT * FROM FWK_MESSAGE_FIELD F WHERE F.REMARK                    " 
		+"\r\n        IS NOT NULL AND M.MESSAGE_ID = F.MESSAGE_ID AND M.ORG_ID = F.ORG_ID )" 
		+"\r\n ) R                                                                         " 
		+"\r\n ON M.MESSAGE_ID = R.MESSAGE_ID                                              " 
		+"\r\n AND M.ORG_ID = R.ORG_ID                                                     " 
		+"\r\n WHERE M.PRE_LOAD_YN = 'Y'                                             	  " ;
	 
	/**
	 * 특정 기관의 특정 메시지 조회(MYSQL)
	 */
	static final String LOAD_MESSAGE_ID_MYSQL = 
		 "\r\n SELECT                                                                 " 
		+"\r\n M.ORG_ID,                                                              " 
		+"\r\n M.MESSAGE_ID,                                                          " 
		+"\r\n M.MESSAGE_NAME,                                                        " 
		+"\r\n M.MESSAGE_TYPE,                                                        " 
		+"\r\n M.PARENT_MESSAGE_ID,                                                   " 
		+"\r\n O.XML_ROOT_TAG,                                                        " 
		+"\r\n COALESCE(R.CNT, 0) IS_REMARK,                                          "
		+"\r\n LOG_LEVEL                                                			  "		
		+"\r\n FROM                                                                   " 
		+"\r\n FWK_MESSAGE M                                                               " 
		+"\r\n INNER JOIN FWK_ORG O ON M.ORG_ID = O.ORG_ID                                 " 
		+"\r\n LEFT OUTER JOIN                                                             " 
		+"\r\n (SELECT M.MESSAGE_ID, M.ORG_ID, 1 CNT                                       " 
		+"\r\n     FROM FWK_MESSAGE M WHERE EXISTS                                         " 
		+"\r\n        (SELECT * FROM FWK_MESSAGE_FIELD F WHERE F.REMARK                    " 
		+"\r\n        IS NOT NULL AND M.MESSAGE_ID = F.MESSAGE_ID AND M.ORG_ID = F.ORG_ID )" 
		+"\r\n ) R                                                                         " 
		+"\r\n ON M.MESSAGE_ID = R.MESSAGE_ID                                              " 
		+"\r\n AND M.ORG_ID = R.ORG_ID                                                     " 
		+"\r\n WHERE M.ORG_ID = ? AND M.MESSAGE_ID = ?                                  ";
	
	/**
     * 모든 Message Mapping 정보를 읽어오는 쿼리
     */
    static final String LOAD_ALL_MESSAGE_FIELD_MAPPING = 
    	  "   SELECT                                           "
    	+ "\n TRG_MESSAGE_ID ||'@'|| TRG_ORG_ID STRUCTURE_ID,  "
    	+ "\n TRG_ORG_ID,                                      "
    	+ "\n TRG_MESSAGE_ID,                                  "
    	+ "\n TRG_MESSAGE_FIELD_ID,           				   "
    	+ "\n SRC_ORG_ID,                                      "
    	+ "\n SRC_MESSAGE_ID,                                  "
    	+ "\n MAPPING_EXPR                                     "
    	+ "\n FROM                                             "
    	+ "\n FWK_MESSAGE_FIELD_MAPPING                        ";
    
    /**
     * PRE_LOAD_YN이 'Y'인 모든 Message Mapping 정보를 읽어오는 쿼리
     */
    static final String LOAD_ALL_PRE_LOAD_MESSAGE_FIELD_MAPPING = 
    	  "   SELECT                                             " 
    	+ "\n F.TRG_MESSAGE_ID ||'@'|| F.TRG_ORG_ID STRUCTURE_ID," 
    	+ "\n F.TRG_ORG_ID,                                      " 
    	+ "\n F.TRG_MESSAGE_ID,                                  " 
    	+ "\n F.TRG_MESSAGE_FIELD_ID,           				 " 
    	+ "\n F.SRC_ORG_ID,                                      " 
    	+ "\n F.SRC_MESSAGE_ID,                                  " 
    	+ "\n F.MAPPING_EXPR                                     " 
    	+ "\n FROM                                               " 
    	+ "\n FWK_MESSAGE_FIELD_MAPPING F, FWK_MESSAGE M         " 
    	+ "\n WHERE F.TRG_ORG_ID = M.ORG_ID                      " 
    	+ "\n AND F.TRG_MESSAGE_ID = M.MESSAGE_ID                " 
    	+ "\n AND M.PRE_LOAD_YN = 'Y'                            ";
    
    /**
     * 특정 기관, 메시지의 Message Mapping 정보를 읽어오는 쿼리
     * 
     */
    static final String LOAD_MESSAGE_FIELD_MAPPING = 
  	  "   SELECT                                           "
  	+ "\n TRG_MESSAGE_ID ||'@'|| TRG_ORG_ID STRUCTURE_ID,  "
  	+ "\n TRG_ORG_ID,                                      "
  	+ "\n TRG_MESSAGE_ID,                                  "
  	+ "\n TRG_MESSAGE_FIELD_ID,           				   "
  	+ "\n SRC_ORG_ID,                                      "
  	+ "\n SRC_MESSAGE_ID,                                  "
  	+ "\n MAPPING_EXPR                                     "
  	+ "\n FROM                                             "
  	+ "\n FWK_MESSAGE_FIELD_MAPPING                        "
    + "\n WHERE TRG_ORG_ID = ? AND TRG_MESSAGE_ID = ?      ";
    
    /**
     * 모든 Message Field 정보를 읽어오는 쿼리
     * 주의 : 로직상 order by 절이 중요!!
     */
    static final String LOAD_ALL_MESSAGE_FILED = 
    	 "\n SELECT                                     " 
    	+"\n MESSAGE_ID ||'@'||ORG_ID STRUCTURE_ID,     " 
    	+"\n ORG_ID,                                    " 
    	+"\n MESSAGE_ID,                                " 
    	+"\n SORT_ORDER,                                " 
    	+"\n MESSAGE_FIELD_ID,                          " 
    	+"\n DATA_TYPE,                                 " 
    	+"\n DATA_LENGTH,                               " 
    	+"\n SCALE,                                     " 
    	+"\n ALIGN,                                     " 
    	+"\n FILLER,                                    " 
    	+"\n FIELD_TYPE,                                " 
    	+"\n REQUIRED_YN,                               " 
    	+"\n CODE_GROUP,                                " 
    	+"\n DEFAULT_VALUE,                             " 
    	+"\n TEST_VALUE,                                " 
    	+"\n USE_MODE,                                  " 
    	+"\n FIELD_TAG,                                 " 
    	+"\n REMARK,                                    " 
    	+"\n LOG_YN,                                    " 
        +"\n CODE_MAPPING_YN                            "    
    	+"\n FROM FWK_MESSAGE_FIELD                     " 
        +"\n ORDER BY STRUCTURE_ID, ORG_ID, MESSAGE_ID, SORT_ORDER ";

    /**
     * PRE_LOAD_YN이 'Y'인 모든 Message Field 정보를 읽어오는 쿼리
     * 주의 : 로직상 order by 절이 중요!!
     */
    static final String LOAD_ALL_PRE_LOAD_MESSAGE_FILED = 
    	 "\n SELECT                                     "            
    	+"\n F.MESSAGE_ID ||'@'||F.ORG_ID STRUCTURE_ID,   "        
    	+"\n F.ORG_ID,                                    "          
    	+"\n F.MESSAGE_ID,                                "          
    	+"\n F.SORT_ORDER,                                "          
    	+"\n F.MESSAGE_FIELD_ID,                          "          
    	+"\n F.DATA_TYPE,                                 "          
    	+"\n F.DATA_LENGTH,                               "          
    	+"\n F.SCALE,                                     "          
    	+"\n F.ALIGN,                                     "          
    	+"\n F.FILLER,                                    "          
    	+"\n F.FIELD_TYPE,                                "          
    	+"\n F.REQUIRED_YN,                               "          
    	+"\n F.CODE_GROUP,                                "          
    	+"\n F.DEFAULT_VALUE,                             "          
    	+"\n F.TEST_VALUE,                                "          
    	+"\n F.USE_MODE,                                  "          
    	+"\n F.FIELD_TAG,                                 "          
    	+"\n F.REMARK,                                    "          
        +"\n F.LOG_YN,                                    " 
        +"\n F.CODE_MAPPING_YN                            "         
    	+"\n FROM FWK_MESSAGE_FIELD F, FWK_MESSAGE M      "          
    	+"\n WHERE F.ORG_ID = M.ORG_ID					  "          
    	+"\n AND F.MESSAGE_ID = M.MESSAGE_ID			  "
    	+"\n AND M.PRE_LOAD_YN = 'Y'   			          "
    	+"\n ORDER BY STRUCTURE_ID, ORG_ID, MESSAGE_ID, SORT_ORDER ";

    /**
     * 특정 기관, 메시지의 Message Field 정보 조회
     * 주의 : 로직상 order by 절이 중요!!
     */
    static final String LOAD_MESSAGE_FILED = 
   	 "\n SELECT                                     " 
   	+"\n MESSAGE_ID ||'@'||ORG_ID STRUCTURE_ID,     " 
   	+"\n ORG_ID,                                    " 
   	+"\n MESSAGE_ID,                                " 
   	+"\n SORT_ORDER,                                " 
   	+"\n MESSAGE_FIELD_ID,                          " 
   	+"\n DATA_TYPE,                                 " 
   	+"\n DATA_LENGTH,                               " 
   	+"\n SCALE,                                     " 
   	+"\n ALIGN,                                     " 
   	+"\n FILLER,                                    " 
   	+"\n FIELD_TYPE,                                " 
   	+"\n REQUIRED_YN,                               " 
   	+"\n CODE_GROUP,                                " 
   	+"\n DEFAULT_VALUE,                             " 
   	+"\n TEST_VALUE,                                " 
   	+"\n USE_MODE,                                  " 
   	+"\n FIELD_TAG,                                 " 
   	+"\n REMARK,                                    " 
    +"\n LOG_YN,                                    " 
    +"\n CODE_MAPPING_YN                            "         
   	+"\n FROM FWK_MESSAGE_FIELD                     " 
   	+"\n WHERE ORG_ID = ? AND MESSAGE_ID = ?        " 
    +"\n ORDER BY STRUCTURE_ID, ORG_ID, MESSAGE_ID, SORT_ORDER ";
}
