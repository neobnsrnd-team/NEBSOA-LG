/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.log;

import java.util.HashMap;
import java.util.Map;

import nebsoa.common.Constants;
import nebsoa.common.jdbc.DBManager;

/**
 * 거래에 대한 로깅을 수행합니다.
 * @author kyun
 */
public class ServiceLogManager {
	
	private static Map logPool = new HashMap(); 
	
	private ServiceLogManager() { }
	
    public static void addLog(ServiceLog serviceLog) {
    	String trxId = serviceLog.getTrxId();
    	logPool.put(trxId, serviceLog);
	}
    
    private static String INSERT_MESSAGE_LOG = 
        "\r\n INSERT INTO MESSAGE_LOG ( "
      + "\r\n MESSAGE_TRX_ID, "
      + "\r\n SEND_RECEIVE_CLASS, "
      + "\r\n MESSAGE_ID, "
      + "\r\n MESSAGE_TRX_DEALING_DTIME, "
      + "\r\n MESSAGE_TRX_DATA, "
      + "\r\n ORG_ID "
      + "\r\n ) "
      + "\r\n VALUES(?,?,?,?,?,?) ";
    
    public static void writeLog(MessageLog log) {
        try {
	        DBManager.executePreparedUpdate(Constants.SPIDER_DB, INSERT_MESSAGE_LOG, log.toArray());
        } catch (Exception e) {
			LogManager.error("메시지로그 생성 실패");
        }
	}
    
    private static String INSERT_TRX_LOG = 
        "\r\n INSERT INTO FWK_TRX_LOG ( "
      + "\r\n TRX_DT, "
      + "\r\n TRX_SER_NO, "
      + "\r\n USER_ID, "
      + "\r\n TRX_ID, "
      + "\r\n REPROCESS_FREQ, "
      + "\r\n START_DTIME, "
      + "\r\n END_DTIME, "
      + "\r\n RESULT_CODE "
      + "\r\n ) "
      + "\r\n VALUES(?,?,?,?,?,?,?,?) ";
    
    public static void writeLog(ServiceLog log) {
        try {
	        DBManager.executePreparedUpdate(Constants.SPIDER_DB, INSERT_TRX_LOG, log.toArray());
        } catch (Exception e) {
			LogManager.error("거래로그 생성 실패");
        }
	}
    
    public static void writeLog(String trxId) {
    	ServiceLog serviceLog = (ServiceLog) logPool.get(trxId);
    	if (serviceLog == null) {
    		return;
    	}
    	
        try {
	        DBManager.executePreparedUpdate(Constants.SPIDER_DB, INSERT_TRX_LOG, serviceLog.toArray());
        } catch (Exception e) {
			LogManager.error("거래로그 생성 실패");
        } finally {
        	logPool.remove(trxId);
        }
	}
    
}