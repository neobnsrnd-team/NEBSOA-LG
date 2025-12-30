/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
package nebsoa.logserver.dblog;

import org.apache.commons.lang.StringUtils;

import nebsoa.logserver.LogContext;
/*******************************************************************
* <pre>
* 1.설명 
* 디폴트 DB 로거  
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
* $Log: DefaultDBLogger.java,v $
* Revision 1.1  2018/01/15 03:39:51  cvs
* *** empty log message ***
*
* Revision 1.1  2016/04/15 02:22:57  cvs
* neo cvs init
*
* Revision 1.1  2011/07/01 02:13:51  yshong
* *** empty log message ***
*
* Revision 1.1  2008/11/18 11:27:24  김성균
* *** empty log message ***
*
* Revision 1.1  2008/11/18 11:01:28  김성균
* LGT Gateway를 위한 프로젝트로 분리
*
* Revision 1.1  2008/08/04 08:54:54  youngseokkim
* *** empty log message ***
*
* Revision 1.1  2008/01/22 05:58:32  오재훈
* 패키지 리펙토링
*
* Revision 1.2  2007/12/17 01:49:02  김승희
* 로그 서버 서버 분리에 따른 수정
*
* Revision 1.2  2007/10/02 07:56:59  김승희
* 최초등록
*
* Revision 1.1  2007/09/21 08:22:30  김승희
* 최초 등록
*
*
* </pre>
******************************************************************/
public class DefaultDBLogger extends AbstractDBLogger{
	
	private static DefaultDBLogger instance;
	private static Object dummy=new Object();
	
	public static DefaultDBLogger getInstance() throws Exception{
	     if(instance==null){
	          synchronized (dummy) {
	              if(instance==null) instance = new DefaultDBLogger();
	          }
	     }
	     return instance;
	}
	protected DefaultDBLogger() throws Exception{
		super();
	}
	
	public static final String INSERT_SQL =
		 "  INSERT INTO FWK_MESSAGE_LOG_DATA (   "    
		+"\r\n    TRX_DATE, TRX_TIME, TRX_TRACE_ID, "    
		+"\r\n    SERVER_ID, IO_TYPE, REQ_RES_TYPE, "    
		+"\r\n    TRX_ID, MESSAGE_ID, ORG_ID,       "    
		+"\r\n    USER_ID, USER_SSN, RESULT_CODE,   "    
		+"\r\n    LOG_DATA_1, LOG_DATA_2)           "    
		+"\r\n VALUES ( ?, ?, ?, ?, ?, ?,           "    
		+"\r\n          ?, ?, ?, ?, ?, ?, ?, ?)     "  ;  
	

	public String getInsertSQL() {
		return INSERT_SQL;
	}

	public Object[] getInsertParamArray(LogContext logContext) {
		return new String[]{logContext.getTrxDate(), logContext.getTrxTime(), logContext.getTrxTraceId(), 
				 logContext.getServerId(), logContext.getIoType(), logContext.getReqResType(),
				 logContext.getTrxId(), logContext.getMessageId(), logContext.getOrgId(), 
				 logContext.getUserId(), logContext.getUserSsn(), logContext.getResultCode(), 
				 logContext.getLogData1(), logContext.getLogData2()};
	}
	
	public LogContext parseLog(LogContext logContext, String formattedLog) {
		
		String[] logToke = StringUtils.splitPreserveAllTokens(formattedLog, "|");
		 logContext.setTrxDate(logToke[0]);
		 logContext.setTrxTime(logToke[1]);
		 logContext.setTrxTraceId(logToke[3]);
		 logContext.setServerId(logToke[2]);
		 logContext.setIoType(logToke[6]);
		 logContext.setReqResType(logToke[5].equals("Send")?"Q":"S");
		 logContext.setTrxId(logToke[9]);
		 logContext.setMessageId(logToke[9] + (logToke[5].equals("Send")?"_REQ":"_RES"));
		 logContext.setOrgId(logToke[11]);
		 logContext.setUserId(logToke[14]);
		 logContext.setUserSsn(logToke[12]);
		 logContext.setResultCode(logToke[10]);
		 
		 //로그 데이터가 4000bytes를 넘어가는 경우 logdata1, logdata2로 잘라서 넣는다.
		 //최대 8000bytes까지 넣는다.
		 byte[] logBytes = logToke[16].getBytes();
		 if(logBytes.length>4000){
			 logContext.setLogData1(new String(logBytes, 0, 4000));
			 if(logBytes.length>8000){
				 logContext.setLogData2(new String(logBytes, 4000, 4000));
			 }else{
				 logContext.setLogData2(new String(logBytes, 4000, logBytes.length-4000));
			 }
		 }else{
			 logContext.setLogData1(logToke[16]);
		 }
		
		 return logContext;
	}

}
