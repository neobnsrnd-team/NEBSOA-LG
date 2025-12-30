/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.context;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nebsoa.common.Constants;
import nebsoa.common.jdbc.DBManager;
import nebsoa.common.jdbc.DBResultSet;
import nebsoa.common.log.LogManager;
import nebsoa.service.info.Service;

import org.apache.commons.collections.map.MultiKeyMap;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 메시지 엔진 context에 기관 시스템, 송수신 정보를 담는다.
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
 * $Log: MessageEngineContextLoader.java,v $
 * Revision 1.1  2018/01/15 03:39:49  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:29  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:23  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:26  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.2  2008/09/19 07:01:39  youngseokkim
 * 거래제한 관련 로직 수정
 *
 * Revision 1.1  2008/08/04 08:54:52  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.6  2008/06/20 02:13:03  김영석
 * 접근허용자 리로딩 로직 추가
 *
 * Revision 1.5  2008/05/19 11:24:17  김승희
 * 대응답데이터 필드(ProxtResData)의 데이터 타입을 String에서 Object로 변경
 *
 * Revision 1.4  2008/03/19 02:19:44  김승희
 * FWK_TRX_MESSAGE 테이블의 MULTI_RES_YN  필드 삭제에 따른 변경
 *
 * Revision 1.3  2008/03/11 01:25:40  김승희
 * 응답메시지 타입 (응답 메시지 갯수 구분) 필드 로딩 시 추가
 *
 * Revision 1.2  2008/03/04 07:37:31  김승희
 * 리스너-응답 커넥터 매핑 정보 로딩 관련 추가
 *
 * Revision 1.1  2008/01/22 05:58:25  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.9  2008/01/14 08:15:05  안경아
 * *** empty log message ***
 *
 * Revision 1.8  2008/01/11 10:40:21  안경아
 * 다중응답전문 처리
 *
 * Revision 1.7  2008/01/04 06:10:26  안경아
 * *** empty log message ***
 *
 * Revision 1.6  2007/12/28 05:49:02  안경아
 * *** empty log message ***
 *
 * Revision 1.5  2007/12/26 11:30:26  안경아
 * *** empty log message ***
 *
 * Revision 1.4  2007/12/24 09:02:31  안경아
 * *** empty log message ***
 *
 * Revision 1.3  2007/12/18 08:31:08  김승희
 * PROXY_RES_TYPE 필드 추가
 *
 * Revision 1.2  2007/11/30 09:46:55  안경아
 * DB NAME 지정
 *
 * Revision 1.1  2007/11/26 08:38:09  안경아
 * *** empty log message ***
 *
 * Revision 1.20  2007/06/07 10:37:19  이종원
 * 접근허용사용자 정보 로딩하는 부분 수정
 *  -> data가 잘못 등록된 경우 오류 발생하는 로직 때문에 try~catch로 처리
 *
 * Revision 1.18  2006/10/02 11:22:36  이종원
 * listener가 전문 수신시 해당 거래 매칭되는 매핑 정보 캐쉬하는 로직 구현
 *
 * Revision 1.17  2006/09/19 06:10:53  김승희
 * 중지여부가 Y인 시스템도 읽어온다.
 *
 * Revision 1.16  2006/09/18 07:25:28  김승희
 * GW 속성 추가에 따른 수정
 *
 * Revision 1.15  2006/09/13 04:47:40  김승희
 * *** empty log message ***
 *
 * Revision 1.14  2006/08/31 09:12:54  김승희
 * 핸들러, GW, 시스템 정보 리로딩 구현
 *
 * Revision 1.13  2006/08/25 01:26:58  김승희
 * 시스템 관련 테이블 변경에 따른 수정
 *
 * Revision 1.11  2006/07/28 12:55:47  이종원
 * *** empty log message ***
 *
 * Revision 1.10  2006/07/27 08:02:32  김승희
 * 기관송수신프로토콜, 기관 시스템 테이블 변경에 따른 수정
 *
 * Revision 1.9  2006/06/28 04:17:21  김승희
 * FWK_MESSAGE_INOUT --> FWK_ORG_PROTOCOL 테이블명 변경
 *
 * Revision 1.8  2006/06/22 04:48:07  김성균
 * BIZ_ID를 BIZ_APP_ID로 변경
 *
 * Revision 1.7  2006/06/21 13:47:57  김성균
 * 거래중지 처리부분 추가
 *
 * Revision 1.6  2006/06/20 10:26:14  김승희
 * SQL 수정
 *
 * Revision 1.5  2006/06/20 08:46:30  김승희
 * 기관 시스템 정보 관련 수정
 *
 * Revision 1.4  2006/06/20 04:53:09  김승희
 * STD_RES_MESSAGE_ID 추가
 *
 * Revision 1.3  2006/06/20 04:16:05  김성균
 * Trx, Org, TrxMessage 정보 로딩하는 부분 추가
 *
 * Revision 1.1  2006/06/19 13:45:55  김성균
 * *** empty log message ***
 *
 * Revision 1.3  2006/06/19 06:25:20  김승희
 * *** empty log message ***
 *
 *
 * </pre>
 ******************************************************************/
public class MessageEngineContextLoader {
	
	/**
	 * 모든 기관 시스템, 송수신 정보를 조회하는 쿼리 
	 *//*
    private static final String LOAD_ALL_STSTEM_INOUT = 
    	 "\r\n SELECT                                              "
    	+"\r\n S.ORG_ID,                                           "
    	+"\r\n S.TRX_TYPE,                                         "
    	+"\r\n S.IO_TYPE,                                          "
    	+"\r\n M.PROTOCOL_HANDLER,                                 "
    	+"\r\n M.SYNCH_ASYNCH_TYPE,                                "
    	+"\r\n S.REQ_RES_TYPE,                                     "
    	+"\r\n S.OPER_MODE_TYPE,                                   "
    	+"\r\n S.ORG_SYSTEM_SEQ,                                   "
    	+"\r\n S.IP,                                               "
    	+"\r\n S.PORT,                                             "
    	+"\r\n S.THREAD_COUNT,                                     "
    	+"\r\n S.PROPERTIES                                        "
    	+"\r\n FROM FWK_ORG_PROTOCOL M,                            "
    	+"\r\n FWK_ORG_SYSTEM S                                    "
    	+"\r\n WHERE M.ORG_ID = S.ORG_ID                           "
    	+"\r\n AND M.TRX_TYPE = S.TRX_TYPE                         "
    	+"\r\n AND M.IO_TYPE = S.IO_TYPE                           "
    	+"\r\n ORDER BY                                            "
    	+"\r\n S.ORG_ID, S.TRX_TYPE, S.IO_TYPE,                    "
    	+"\r\n S.REQ_RES_TYPE, S.OPER_MODE_TYPE, S.ORG_SYSTEM_SEQ  ";*/

    private static final String LOAD_ALL_GATEWAY_TRANSPORT_INFO = 
    	 "\r\n SELECT                                "                                     
    	+"\r\n    T.ORG_ID, T.TRX_TYPE, T.IO_TYPE AS TRANS_IO,   "                                     
    	+"\r\n    T.REQ_RES_TYPE, G.GW_ID, G.GW_NAME,"                                     
    	+"\r\n    G.THREAD_COUNT, G.GW_PROPERTIES, G.GW_APP_NAME, G.IO_TYPE GATE_IO  "                                     
    	+"\r\n FROM FWK_TRANSPORT T,                 "                                     
    	+"\r\n FWK_GATEWAY G                         "                                     
    	+"\r\n WHERE T.GW_ID = G.GW_ID               " ;

    /**
     * Gateway 정보와
     * 기관별, 거래유형별, 기동수동구분별, 요구응답별 Gateway 매핑정보(FWK_TRANSPORT)를 로딩한다.
     * @param context
     */
    public static void loadGateway(MessageEngineContext context){
    	Object[] params = {};
        DBResultSet rs = DBManager.executePreparedQuery(Constants.SPIDER_DB, LOAD_ALL_GATEWAY_TRANSPORT_INFO, params);
        MultiKeyMap gatewayInfoMap = context.getGatewayInfoMap();
        Map gateWayMap = context.getGateWayMap();
        
        gatewayInfoMap.clear();
        gateWayMap.clear();
        
        Gateway gateway = null;
        String gwId = null;
        while (rs.next()) {
        	gwId = rs.getString("GW_ID");
        	if((gateway = (Gateway)gateWayMap.get(gwId))==null){
        		gateway = new Gateway();
        		gateway.setGwId(gwId);
        		gateway.setGwName(rs.getString("GW_NAME"));
        		gateway.setThreadCount(rs.getInt("THREAD_COUNT"));
        		gateway.setGwProperties(rs.getString("GW_PROPERTIES"));
        		gateway.setGwAppName(rs.getString("GW_APP_NAME"));
        		gateway.setIoType(rs.getString("GATE_IO"));
        		gateWayMap.put(gwId, gateway);
        	}
        	gatewayInfoMap.put(rs.getString("ORG_ID"), rs.getString("TRX_TYPE"), rs.getString("TRANS_IO"), rs.getString("REQ_RES_TYPE"),
        			gateway);
        }
    }
    
    private static final String LOAD_ALL_SYSTEM = 
   	 "\r\n SELECT                               "                    
   	+"\r\n    GW_ID, SYSTEM_ID, OPER_MODE_TYPE, "                    
   	+"\r\n    IP, PORT, STOP_YN           		"                    
   	+"\r\n FROM FWK_SYSTEM                      ";
    //+"\r\n WHERE STOP_YN != 'Y'                 ";
    
    /**
     * GW_ID, 운영모드 구분에 따른 시스템 리스트를 로딩한다.
     * @param context
     */
    public static void loadSystem(MessageEngineContext context){
    	Object[] params = {};
        DBResultSet rs = DBManager.executePreparedQuery(Constants.SPIDER_DB, LOAD_ALL_SYSTEM, params);
        
        String gwId = null;
        String systemId = null;
        String operModeType = null;
        String ip = null;
        String port = null;
        String stopYn = null;
       
        MultiKeyMap systemInfoMap = context.getSystemInfoMap();
        systemInfoMap.clear();
        
        OrgSystemList systemList = null;
        Map gateWayMap = context.getGateWayMap();
        
        while (rs.next()) {
        	gwId = rs.getString("GW_ID");
        	systemId = rs.getString("SYSTEM_ID");
        	operModeType = rs.getString("OPER_MODE_TYPE");
        	ip = rs.getString("IP");
        	port = rs.getString("PORT");
        	stopYn = rs.getString("STOP_YN");
        	
        	if((systemList = (OrgSystemList)systemInfoMap.get(gwId, operModeType))==null){
        		
        		systemList = new OrgSystemList();
        		systemList.setGateway((Gateway)gateWayMap.get(gwId));
        	}
        	systemList.addOrgSystem(gwId, systemId, operModeType, ip, port, stopYn);
        	systemInfoMap.put(gwId, operModeType, systemList);
        }
        
    }
    

    
/*    *//**
	 * 모든 기관, 송수신 정보를 조회하는 쿼리 
	 *//*
    private static final String LOAD_ALL_ORG_PROTOCOL = 
    	   "\r\n SELECT   "
    	 + "\r\n ORG_ID,   "
    	 + "\r\n TRX_TYPE,   "
    	 + "\r\n IO_TYPE, "
    	 + "\r\n PROTOCOL_HANDLER,   "
    	 + "\r\n SYNCH_ASYNCH_TYPE  "
    	 + "\r\n FROM FWK_ORG_PROTOCOL "
    	 ;

    
    public static void loadMesssageInout(MessageEngineContext context){
    	Object[] params = {};
        DBResultSet rs = DBManager.executePreparedQuery(LOAD_ALL_ORG_PROTOCOL, params);
        String orgId = null;
        String trxType = null;
        String ioType = null;
        MultiKeyMap messageInoutMap = context.getOrgProtocol();
        OrgProtocol orgProtocol = null;
        
        while (rs.next()) {
        	orgId = rs.getString("ORG_ID");
        	trxType = rs.getString("TRX_TYPE");
        	ioType = rs.getString("IO_TYPE");
        	
        	orgProtocol = new OrgProtocol();
        	orgProtocol.setOrgId(orgId);
        	orgProtocol.setTrxType(trxType);
        	orgProtocol.setIoType(ioType);
        	orgProtocol.setProtocolHandler(rs.getString("PROTOCOL_HANDLER"));
        	orgProtocol.setSynchAsynchType(rs.getString("SYNCH_ASYNCH_TYPE"));
    		
    		messageInoutMap.put(orgId, trxType, ioType, orgProtocol);
        }
    }*/
    
 
    /**
     * DB로부터  거래 정보를 로드하기 위한 SQL
     */
    private static final String LOAD_ALL_TRX = 
    	  "\r\n SELECT TRX_ID "
    	+ "\r\n   ,BIZ_GROUP_ID  "
    	+ "\r\n   ,OPER_MODE_TYPE  "
    	+ "\r\n   ,TRX_STOP_YN  "
    	+ "\r\n   ,TRX_NAME  "
    	+ "\r\n   ,TRX_DESC  "
    	+ "\r\n   ,TRX_TYPE "
    	+ "\r\n   ,BIZDAY_TRX_YN "
    	+ "\r\n   ,BIZDAY_TRX_START_TIME "
    	+ "\r\n   ,BIZDAY_TRX_END_TIME "
    	+ "\r\n   ,SATURDAY_TRX_YN "
    	+ "\r\n   ,SATURDAY_TRX_START_TIME "
    	+ "\r\n   ,SATURDAY_TRX_END_TIME "
    	+ "\r\n   ,HOLIDAY_TRX_YN "
    	+ "\r\n   ,HOLIDAY_TRX_START_TIME "
    	+ "\r\n   ,HOLIDAY_TRX_END_TIME "
    	+ "\r\n FROM FWK_TRX "
        ;

    public static void loadTrx(MessageEngineContext context) {
    	Object[] params = {};
        String trxId = null;
        
        Map trxInfoMap = context.getTrxInfoMap();
        DBResultSet rs = DBManager.executePreparedQuery(Constants.SPIDER_DB, LOAD_ALL_TRX, params);
        Trx trx = null;
        
        while (rs.next()) {
        	trxId = rs.getString("TRX_ID");
        	
        	trx = new Trx();
        	trx.setTrxId(trxId);
        	trx.setBizAppId(rs.getString("BIZ_GROUP_ID"));
        	trx.setOperModeType(rs.getString("OPER_MODE_TYPE"));
        	trx.setTrxStopYn(rs.getString("TRX_STOP_YN"));
        	trx.setTrxName(rs.getString("TRX_NAME"));
        	trx.setTrxDesc(rs.getString("TRX_DESC"));
        	trx.setTrxType(rs.getString("TRX_TYPE"));
        	trx.setBizdayTrxYn(rs.getString("BIZDAY_TRX_YN"));
        	trx.setBizdayTrxStartTime(rs.getString("BIZDAY_TRX_START_TIME"));
        	trx.setBizdayTrxEndTime(rs.getString("BIZDAY_TRX_END_TIME"));
        	trx.setSaturdayTrxYn(rs.getString("SATURDAY_TRX_YN"));
        	trx.setSaturdayTrxStartTime(rs.getString("SATURDAY_TRX_START_TIME"));
        	trx.setSaturdayTrxEndTime(rs.getString("SATURDAY_TRX_END_TIME"));
        	trx.setHolidayTrxYn(rs.getString("HOLIDAY_TRX_YN"));
        	trx.setHolidayTrxStartTime(rs.getString("HOLIDAY_TRX_START_TIME"));
        	trx.setHolidayTrxEndTime(rs.getString("HOLIDAY_TRX_END_TIME"));
        	
        	trxInfoMap.put(trxId, trx);
        }
    }
    
    /**
     * DB로부터  거래 접근허용사용자ID 정보를 로드하기 위한 SQL
     */
    private static final String LOAD_ALL_ACCESS_USER = 
    	  "\r\n SELECT TRX_ID "
    	+ "\r\n   ,CUST_USER_ID  "
    	+ "\r\n FROM FWK_ACCESS_USER "
    	+ "\r\n WHERE USE_YN = 'Y' AND GUBUN_TYPE = 'T' "
        ;
    /**
     * 거래 일시 중지 일 경우 접근 허용된 사용자를 로딩하는 로직. 
     * 2006. 7. 28.  이종원 작성
     * @param context
     */
    public static void loadAccessUser(MessageEngineContext context) {
        
        Map trxInfoMap = context.getTrxInfoMap();
        DBResultSet rs = DBManager.executeQuery(Constants.SPIDER_DB, LOAD_ALL_ACCESS_USER);
 
        Trx trx = null;
        String trxId = null;        

        while (rs.next()) {
            //data가 잘못 등록된 경우 오류 발생하는 로직 때문에 try~catch로 처리
            try{
            	trxId = rs.getString("TRX_ID");
                if(trxId != null){
                    trx = (Trx) trxInfoMap.get(trxId);
                    if(trx != null){
                        trx.addCustUserId(rs.getString("CUST_USER_ID"));
                    }
                }
            }catch(Exception e){
                LogManager.error(e.toString(),e);
            }
        }
    }
    /**
     * 거래 일시 중지 일 경우 접근 허용된 사용자를 리로딩하는 로직. 
     * 2008. 6. 20.  김영석 작성
     * @param context
     */
    public static void reloadAccessUser(MessageEngineContext context) {

        Map trxInfoMap = context.getTrxInfoMap();
        DBResultSet rs = DBManager.executeQuery(Constants.SPIDER_DB, LOAD_ALL_ACCESS_USER);
 
        Trx trx = null;
        String trxId = null;

        Set keySet = trxInfoMap.keySet();
        Iterator itr = keySet.iterator();
        
        // 리로드 하기 위해 현재 거래 접근허용자 정보 clear
        while (itr.hasNext()) {
            String childKey = (String) itr.next();
            trx = (Trx) trxInfoMap.get(childKey);
            List custUserIds = trx.getCustUserIds();
            custUserIds.clear();
        }
        
        while (rs.next()) {
            //data가 잘못 등록된 경우 오류 발생하는 로직 때문에 try~catch로 처리
            try{
            	trxId = rs.getString("TRX_ID");
                if(trxId != null){
                    trx = (Trx) trxInfoMap.get(trxId);
                    if(trx != null){
                    	List custUserIds = trx.getCustUserIds();
                    	
                    	// 중복일 경우 skip
                    	if(!custUserIds.contains(rs.getString("CUST_USER_ID"))) {                    	
                    		trx.addCustUserId(rs.getString("CUST_USER_ID"));
                    	}
                    }
                }
            }catch(Exception e){
                LogManager.error(e.toString(),e);
            }
        }
    }
    
    /**
     * DB로부터  기관 정보를 로드하기 위한 SQL
     */
    private static final String LOAD_ALL_ORG = 
    	  "\r\n SELECT ORG_ID "
    	+ "\r\n   ,ORG_NAME  "
    	+ "\r\n   ,ORG_DESC  "
    	+ "\r\n   ,START_TIME  "
    	+ "\r\n   ,END_TIME  "
    	+ "\r\n   ,XML_ROOT_TAG  "
    	+ "\r\n FROM FWK_ORG "
        ;

    public static void loadOrg(MessageEngineContext context) {
    	Object[] params = {};
        String orgId = null;
        
        Map orgInfoMap = context.getOrgInfoMap();
        DBResultSet rs = DBManager.executePreparedQuery(Constants.SPIDER_DB, LOAD_ALL_ORG, params);
        Org org = null;
        
        while (rs.next()) {
        	orgId = rs.getString("ORG_ID");
        	
        	org = new Org();
        	org.setOrgId(orgId);
        	org.setOrgName(rs.getString("ORG_NAME"));
        	org.setOrgDesc(rs.getString("ORG_DESC"));
        	org.setStartTime(rs.getString("START_TIME"));
        	org.setEndTime(rs.getString("END_TIME"));
        	org.setXmlRootTag(rs.getString("XML_ROOT_TAG"));
    		
        	orgInfoMap.put(orgId, org);
        }
    }
    
    /**
     * DB로부터  거래별전문 정보를 로드하기 위한 SQL
     */
    private static final String LOAD_ALL_TRX_MESSAGE = 
    	  "\r\n SELECT TRX_ID "
    	+ "\r\n   ,ORG_ID  "
    	+ "\r\n   ,IO_TYPE  "
    	+ "\r\n   ,MESSAGE_ID  "
    	+ "\r\n   ,STD_MESSAGE_ID  "
    	+ "\r\n   ,RES_MESSAGE_ID  "
    	+ "\r\n   ,STD_RES_MESSAGE_ID "
    	+ "\r\n   ,PROXY_RES_YN "
    	+ "\r\n   ,PROXY_RES_DATA "
    	+ "\r\n   ,PROXY_RES_TYPE "
    	+ "\r\n   ,HEX_LOG_YN "
    	+ "\r\n   ,RES_TYPE_FIELD_ID "
    	+ "\r\n   ,MULTI_RES_TYPE "
    	+ "\r\n FROM FWK_TRX_MESSAGE "
        ;

    public static void loadTrxMessage(MessageEngineContext context) {
    	Object[] params = {};
        String trxId = null;
        String orgId = null;
        String ioType = null;
        
        MultiKeyMap trxMessageInfoMap = context.getTrxMessageInfoMap();
        DBResultSet rs = DBManager.executePreparedQuery(Constants.SPIDER_DB, LOAD_ALL_TRX_MESSAGE, params);
        TrxMessage trxMessage = null;
        
        while (rs.next()) {
        	trxId = rs.getString("TRX_ID");
        	orgId = rs.getString("ORG_ID");
        	ioType = rs.getString("IO_TYPE");
        	
        	trxMessage = new TrxMessage();
        	trxMessage.setTrx(context.getTrx(trxId));
        	trxMessage.setOrg(context.getOrg(orgId));
        	trxMessage.setIoType(ioType);
        	trxMessage.setMessageId(rs.getString("MESSAGE_ID"));
        	trxMessage.setStdMessageId(rs.getString("STD_MESSAGE_ID"));
        	trxMessage.setResMessageId(rs.getString("RES_MESSAGE_ID"));
        	trxMessage.setStdResMessageId(rs.getString("STD_RES_MESSAGE_ID"));
        	trxMessage.setProxyResYn(rs.getString("PROXY_RES_YN"));
        	trxMessage.setProxyResData(rs.getObject("PROXY_RES_DATA"));
        	trxMessage.setProxyResType(rs.getString("PROXY_RES_TYPE"));
        	trxMessage.setHexLogYn(rs.getString("HEX_LOG_YN"));
        	trxMessage.setResTypeFieldId(rs.getString("RES_TYPE_FIELD_ID"));
        	trxMessage.setMultiResType(rs.getString("MULTI_RES_TYPE"));
        	        	
        	trxMessageInfoMap.put(trxId, orgId, ioType, trxMessage);
        }
    }
    
    /**
	 * 모든 MESSAGE_HANDLER 조회하는 쿼리
	 */
    private static final String LOAD_ALL_MESSAGE_HANDLER = 
    	   "\r\n SELECT   "
    	 + "\r\n ORG_ID,   "
    	 + "\r\n TRX_TYPE,   "
    	 + "\r\n IO_TYPE, "
    	 + "\r\n OPER_MODE_TYPE,   "
    	 + "\r\n STOP_YN,   "
    	 + "\r\n HANDLER   "
    	 + "\r\n FROM FWK_MESSAGE_HANDLER " ;

    public static void loadMesssageHandler(MessageEngineContext context){
    	Object[] params = {};
        DBResultSet rs = DBManager.executePreparedQuery(Constants.SPIDER_DB, LOAD_ALL_MESSAGE_HANDLER, params);
        String orgId = null;
        String trxType = null;
        String ioType = null;
        String operModeType = null;
        MultiKeyMap messageHandlerMap = context.getMessageHandlerMap();
        messageHandlerMap.clear();
        MessageHandler messageHandler = null;
        
        while (rs.next()) {
        	orgId = rs.getString("ORG_ID");
        	trxType = rs.getString("TRX_TYPE");
        	ioType = rs.getString("IO_TYPE");
        	operModeType = rs.getString("OPER_MODE_TYPE");
        	
        	messageHandler = new MessageHandler();
        	messageHandler.setOrgId(orgId);
        	messageHandler.setTrxType(trxType);
        	messageHandler.setIoType(ioType);
        	messageHandler.setOperModeType(operModeType);
        	messageHandler.setStopYn(rs.getString("STOP_YN"));
        	messageHandler.setMessageHandler(rs.getString("HANDLER"));
        	    		
    		messageHandlerMap.put(orgId, trxType, ioType, operModeType, messageHandler);
        }
    }
    
    /**
     * LISTENER가 요청 접수 시 어떤 거래(전문)에 해당하는지 조회하는 쿼리
     */
    private static final String LOAD_ALL_INPUT_MESSAGE_TRX_MAPPING = 
           "\r\n SELECT  GW_ID,REQ_ID_CODE, "
         + "\r\n TRX_ID,   "
         + "\r\n ORG_ID,   "
         + "\r\n IO_TYPE, "
         + "\r\n BIZ_APP_ID "
         + "\r\n FROM FWK_LISTENER_TRX_MESSAGE " ;
    /**
     * LISTENER가 요청 접수 시 해당 요청이 어떤 거래(전문)에 해당하는지에 대한 정보를 로딩 하여 cache처리.
     */
    public static void loadInputMesssageTrxMapping(
            MessageEngineContext context){
        Object[] params = {};
        DBResultSet rs = DBManager.executePreparedQuery(Constants.SPIDER_DB, LOAD_ALL_INPUT_MESSAGE_TRX_MAPPING, params);
        String gwId = null;
        String reqIdCode = null;
        String orgId = null;
        String trxId = null;
        String ioType = null;
        String bizAppId = null;
        MultiKeyMap messageTrxMappingMap = context.getMessageTrxMappingMap();
        messageTrxMappingMap.clear();
        InputMessageTrxMapper messageTrxMapper = null;
        
        while (rs.next()) {
            gwId = rs.getString("GW_ID");
            reqIdCode = rs.getString("REQ_ID_CODE");
            orgId = rs.getString("ORG_ID");
            trxId = rs.getString("TRX_ID");
            ioType = rs.getString("IO_TYPE");
            bizAppId = rs.getString("BIZ_APP_ID");
            
            messageTrxMapper = new InputMessageTrxMapper();
            messageTrxMapper.setGwId(gwId);
            messageTrxMapper.setReqIdCode(reqIdCode);
            messageTrxMapper.setOrgId(orgId);
            messageTrxMapper.setTrxId(trxId);
            messageTrxMapper.setIoType(ioType);
            messageTrxMapper.setBizAppId(bizAppId);
                        
            messageTrxMappingMap.put(gwId, reqIdCode, messageTrxMapper);
        }
    }
	
    
    /**
     * WAS LISTENER MAPPING 조회하는 쿼리
     */
    private static final String LOAD_ALL_WAS_LISTENER_MAPPING = 
           "\r\n SELECT  INSTANCE_ID, "
         + "\r\n GW_ID,   "
         + "\r\n SYSTEM_ID "
         + "\r\n FROM FWK_WAS_LISTENER " ;
    /**
     * LISTENER가 요청 접수 시 해당 요청이 어떤 거래(전문)에 해당하는지에 대한 정보를 로딩 하여 cache처리.
     */
    public static void loadWasListener(MessageEngineContext context){
        Object[] params = {};
        DBResultSet rs = DBManager.executePreparedQuery(Constants.SPIDER_DB, LOAD_ALL_WAS_LISTENER_MAPPING, params);
        MultiKeyMap wasListenerMap = context.getWasListenerMap();
        wasListenerMap.clear();
        
        while (rs.next()) {
            wasListenerMap.put(rs.getString("GW_ID"), rs.getString("SYSTEM_ID"), rs.getString("INSTANCE_ID"), rs.getString("INSTANCE_ID"));
        }
    }
    
    /**
     * 다수 응답 전문을 조회하는 쿼리
     */
    private static final String LOAD_MULTI_RES_MESSAGE = 
           "\r\n SELECT  TRX_ID, "
         + "\r\n ORG_ID,   "
         + "\r\n IO_TYPE, "
         + "\r\n RES_TYPE_FIELD_VALUE, "
         + "\r\n MESSAGE_ID "
         + "\r\n FROM FWK_MULTI_RES_MESSAGE " ;
    /**
     * LISTENER가 요청 접수 시 해당 요청이 어떤 거래(전문)에 해당하는지에 대한 정보를 로딩 하여 cache처리.
     */
    public static void loadMultiResMessage(MessageEngineContext context){
        Object[] params = {};
        DBResultSet rs = DBManager.executePreparedQuery(Constants.SPIDER_DB, LOAD_MULTI_RES_MESSAGE, params);
        MultiKeyMap multiResMessageMap = context.getMultiResMessageMap();
        multiResMessageMap.clear();
        
        while (rs.next()) {
        	multiResMessageMap.put(rs.getString("TRX_ID"), rs.getString("ORG_ID"), rs.getString("IO_TYPE"), rs.getString("RES_TYPE_FIELD_VALUE"), rs.getString("MESSAGE_ID"));
        }
    } 
    
    /**
     * 리스너 - 응답커넥터 맵핑 정보 로딩 SQL
     */
    private static final String LOAD_ALL_LISTENER_CONNECTOR_MAPPING = 
    	 "  SELECT "                                                                   
    	+"\r\n     M.LISTENER_GW_ID, M.LISTENER_SYSTEM_ID, M.IDENTIFIER, "              
    	+"\r\n     M.CONNECTOR_GW_ID, M.CONNECTOR_SYSTEM_ID, "           
    	+"\r\n     S.IP, S.PORT, S.STOP_YN, S.OPER_MODE_TYPE "                               
    	+"\r\n  FROM FWK_LISTENER_CONNECTOR_MAPPING M, FWK_SYSTEM S "                        
    	+"\r\n  WHERE M.CONNECTOR_GW_ID = S.GW_ID AND M.CONNECTOR_SYSTEM_ID = S.SYSTEM_ID "  ;
    
    /**
     * 리스너 - 응답커넥터 맵핑 정보를 로딩한다.
     * @param context
     */
    public static void loadListenerConnectorMapping(MessageEngineContext context){
    	try{
	    	Object[] params = {};
	        DBResultSet rs = DBManager.executePreparedQuery(Constants.SPIDER_DB, LOAD_ALL_LISTENER_CONNECTOR_MAPPING, params);
	        String listenerGwId = null;
	        String listenerSystemId = null;
	        String connectorGwId = null;
	        String connectorSystemId = null;
	        String identifier = null;
	        String ip = null;
	        String port = null;
	        String stopYn = null;
	        String operModeType = null;
	         
	        MultiKeyMap listenerConnectorMapping = context.getListenerConnectorMappingMap();
	        listenerConnectorMapping.clear();
	                
	        while (rs.next()) {
	        	listenerGwId = rs.getString("LISTENER_GW_ID");
	        	listenerSystemId = rs.getString("LISTENER_SYSTEM_ID");
	        	identifier = rs.getString("IDENTIFIER");
	        	
	        	connectorGwId = rs.getString("CONNECTOR_GW_ID");
	        	connectorSystemId = rs.getString("CONNECTOR_SYSTEM_ID");
	        	ip = rs.getString("IP");
	        	port = rs.getString("PORT");
	        	stopYn = rs.getString("STOP_YN");
	        	operModeType = rs.getString("OPER_MODE_TYPE");
	        	
	        	listenerConnectorMapping.put(listenerGwId, listenerSystemId, identifier, 
	        		new GatewaySystem(connectorGwId, connectorSystemId, operModeType, ip, port, stopYn));
	        }
    	}catch(Throwable th){
    		LogManager.error("FWK_LISTENER_CONNECTOR_MAPPING 테이블로부터 리스너-커넥터 맵핑 정보 로딩을 실패 하였습니다.");
    		LogManager.error(th);
    		
    	}
    }
	   
}