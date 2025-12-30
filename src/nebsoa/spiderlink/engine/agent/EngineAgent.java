/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.engine.agent;

import nebsoa.common.Constants;
import nebsoa.common.exception.SysException;
import nebsoa.common.jdbc.DBManager;
import nebsoa.common.jdbc.TxManager;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.DataMap;
import nebsoa.common.util.FormatUtil;
import nebsoa.management.client.ManagementClient;
/*******************************************************************
 * <pre>
 * 1.설명 
 * 운영화면의 연계 관련 정보 변경시 Message Engine 정보를 새로 로딩하는 클래스 
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
 * $Log: EngineAgent.java,v $
 * Revision 1.1  2018/01/15 03:39:54  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:36  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.3  2009/11/19 11:04:06  이정곤
 * *** empty log message ***
 *
 * Revision 1.2  2009/11/05 09:20:53  이정곤
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:27  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:29  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:36  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.2  2007/11/30 09:46:54  안경아
 * DB NAME 지정
 *
 * Revision 1.1  2007/11/26 08:39:17  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2006/10/02 08:00:52  김승희
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public class EngineAgent {
	
	/**
	 * 전문처리핸들러의 STOP YN을 업데이트하는 SQL
	 */
	public static String UPDATE_MESSAGE_HANDLER_STOP_YN_SQL = 
		 "\r\n\t\t UPDATE FWK_MESSAGE_HANDLER SET "
		+"\r\n\t\t 		STOP_YN=?                   "
		+"\r\n\t\t 		,LAST_UPDATE_DTIME=?        "
		+"\r\n\t\t 		,LAST_UPDATE_USER_ID=?      "
		+"\r\n\t\t 		 WHERE ORG_ID=?             "
		+"\r\n\t\t 		 AND TRX_TYPE=?             "
		+"\r\n\t\t 		 AND IO_TYPE=?              "
		+"\r\n\t\t 		 AND OPER_MODE_TYPE=?       ";
	
	/**
	 * 시스템의 STOP YN을 업데이트하는 SQL
	 */
	public static String UPDATE_SYSTEM_STOP_YN_SQL = 
		 "\r\n\t\t UPDATE FWK_SYSTEM SET "
		+"\r\n\t\t 		  STOP_YN=?      "
		+"\r\n\t\t 		 WHERE GW_ID=?   "
		+"\r\n\t\t 		 AND SYSTEM_ID=? ";
		
	public static void controlMessageHandler(String[] wasIDs, DataMap dataMap){
		
		LogManager.info("전문처리 핸들러의 중지여부[" + dataMap.getString("stopYn") +"]를 반영합니다.. ");
		Object[] param = new Object[]{
				 dataMap.getString("stopYn")
				,FormatUtil.getToday("yyyyMMddHHmmss") 
				,dataMap.getString("lastUpdateUserId")
				,dataMap.getString("orgId")
				,dataMap.getString("trxType")
				,dataMap.getString("ioType")
				,dataMap.getString("operModeType")
        };
		TxManager tx = new TxManager(Constants.SPIDER_DB);
		try{
			tx.begin();
			//DB 반영
			DBManager.executePreparedTxUpdate(tx, UPDATE_MESSAGE_HANDLER_STOP_YN_SQL, param);
			
			//MessageEngineContext 반영
			//인자로 받은 WAS ID 별로 ManagementClient를 반복호출한다.
			if(wasIDs!=null){
								
				dataMap.put("COMMAND", "nebsoa.spiderlink.context.MessageEngineContext.reloadMessageHandlerStopYn");
				for(int i=0; i<wasIDs.length; i++){
					LogManager.info(wasIDs[i]+"의 MessageEngineContext에 반영합니다.");
					ManagementClient.doProcess(wasIDs[i], dataMap);
				}
			}
	    	tx.commit();
		}catch(Throwable th){
			tx.rollback();
			throw new SysException("전문처리 핸들러의 중지여부 반영에 실패했습니다. DB는 원복하였으나 각 WAS의 Message Engine에는 이미 반영되었을 수  있으므로 반드시 재시도하십시오.", th);
		}finally{
			tx.end();
		}
	}
	
	public static void controlGWSystem(String[] wasIDs, DataMap dataMap){
		
		LogManager.info(dataMap.getString("gwId") +"-" + dataMap.getString("systemId") + "시스템과 GWConnector의 중지여부[" + dataMap.getString("stopYn") +"]를 반영합니다.. ");
		Object[] param = new Object[]{
				dataMap.getString("stopYn")
				,dataMap.getString("gwId")
				,dataMap.getString("systemId")
        };
		TxManager tx = new TxManager();
		try{
			tx.begin();
			//DB 반영
			DBManager.executePreparedTxUpdate(tx, UPDATE_SYSTEM_STOP_YN_SQL, param);
			
			//인자로 받은 WAS ID 별로 ManagementClient를 반복호출한다.
			if(wasIDs!=null){
								
				//MessageEngineContext 반영
				dataMap.put("COMMAND", "nebsoa.spiderlink.context.MessageEngineContext.reloadSystemStopYn");
				for(int i=0; i<wasIDs.length; i++){
					LogManager.info(wasIDs[i]+"의 MessageEngineContext에 반영합니다.");
					ManagementClient.doProcess(wasIDs[i], dataMap);
				}
				//GWConnector 객체에 반영
				dataMap.put("COMMAND", "nebsoa.spiderlink.engine.GatewayManager.controlGWConnector");
				for(int i=0; i<wasIDs.length; i++){
					LogManager.info(wasIDs[i]+"의 GatewayConnector에 반영합니다.");
					ManagementClient.doProcess(wasIDs[i], dataMap);
				}
			}
	    	tx.commit();
		}catch(Throwable th){
			tx.rollback();
			throw new SysException("GW System 중지여부 반영에 실패했습니다. DB는 원복하였으나 각 WAS의 Message Engine에는 이미 반영되었을 수  있으므로 반드시 재시도하십시오.", th);
		}finally{
			tx.end();
		}
	}
	
	public static void controlGatewaySystem(String[] wasIDs, DataMap dataMap){
		
		LogManager.info(dataMap.getString("gwId") +"-" + dataMap.getString("systemId") + "시스템과 GatewayConnector의 중지여부[" + dataMap.getString("stopYn") +"]를 반영합니다.. ");
		
		try{
			
			//인자로 받은 WAS ID 별로 ManagementClient를 반복호출한다.
			if(wasIDs!=null){
								
				//MessageEngineContext 반영
				dataMap.put("COMMAND", "nebsoa.spiderlink.context.MessageEngineContext.reloadSystemStopYn");
				for(int i=0; i<wasIDs.length; i++){
					LogManager.info(wasIDs[i]+"의 MessageEngineContext에 반영합니다.");
						ManagementClient.doProcess(wasIDs[i], dataMap);
				}
				//GWConnector 객체에 반영
				dataMap.put("COMMAND", "nebsoa.spiderlink.engine.GatewayManager.controlGWConnector");
				for(int i=0; i<wasIDs.length; i++){
					LogManager.info(wasIDs[i]+"의 GatewayConnector에 반영합니다.");
						ManagementClient.doProcess(wasIDs[i], dataMap);
				}
			}
		}catch(Throwable th){
			throw new SysException("GW System 중지여부 반영에 실패했습니다.", th);
		}
	}
}
