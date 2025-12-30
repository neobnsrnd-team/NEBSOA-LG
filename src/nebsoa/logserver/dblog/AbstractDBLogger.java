/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
package nebsoa.logserver.dblog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import nebsoa.common.exception.SysException;
import nebsoa.common.jdbc.DBManager;
import nebsoa.common.log.LogManager;
import nebsoa.logserver.LogContext;
import nebsoa.logserver.LogProcessorContext;
/*******************************************************************
* <pre>
* 1.설명 
* DB에 로그를 남기는 최상위 추상 클래스 
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
* $Log: AbstractDBLogger.java,v $
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
public abstract class AbstractDBLogger {
	
	/**
	 * 객체 순번 
	 */
	protected int index;
	
	/**
	 * 로그 테이블에 로그를 insert한다.
	 * @param logs
	 */
	public void add(Object[] logs) {
		add(logs, 0);
	}
	
	/**
	 * 로그 테이블에 로그를 insert한다.
	 * @param logs 로그 데이터
	 * @param index 객체 순번
	 */
	public void add(Object[] logs, int index) {
		if(logs==null) return;
		//로그 파싱
		long startTime = System.currentTimeMillis();
		ArrayList logList = parseLog(logs);
		long elapsedTime = System.currentTimeMillis()-startTime;
		
		if(elapsedTime>1000){
			LogManager.info(LogProcessorContext.logCategory, "[" + index + "]log parsing time:" + elapsedTime);
		}else if(elapsedTime>500){
			LogManager.debug(LogProcessorContext.logCategory, "[" + index + "]log parsing time:" + elapsedTime);
		}
		
		//로그 insert
		startTime = System.currentTimeMillis();
		executeInsert(getInsertSQL(), logList);
		
		elapsedTime = System.currentTimeMillis()-startTime;
		if(elapsedTime>LogProcessorContext.dbInsertThreshold){
			LogManager.info(LogProcessorContext.logCategory, "[" + index + "]log insert time:" + elapsedTime);
		}else if(elapsedTime>1000){
			LogManager.debug(LogProcessorContext.logCategory, "[" + index + "]log insert time:" + elapsedTime);
		}
	}
	
	/**
	 * DB에 로그를 insert한다.
	 * 디폴트 executeBatch를 사용하도록 구현되어 있다.
	 * @param sql
	 * @param paramList
	 */
	protected void executeInsert(String sql, ArrayList paramList){
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try{
			con = DBManager.getConnection(LogProcessorContext.getDBName());
			con.setAutoCommit(false);
						
			pstmt = DBManager.prepareStatement(con, sql);
			Object[] data = null;
			int paramSize = paramList.size();
			 for(int i=0;i<paramSize;i++){
	             pstmt.clearParameters();
	             data =(Object[]) paramList.get(i);
	             for(int j=0; j<data.length; j++){
	            	 pstmt.setString(j+1, (String)data[j]);
	             }
	             pstmt.addBatch();
	         }
	         pstmt.executeBatch();
	         pstmt.clearParameters();
	         con.commit();
        } catch(Exception e) {
        	try {
				con.rollback();
			} catch (SQLException e1) {
				LogManager.error(LogProcessorContext.logCategory, e1.toString(),e1); 
			}
            LogManager.error(LogProcessorContext.logCategory, e.toString(),e); 
            throw new SysException(e.toString());
        } finally {
        	try {
				con.setAutoCommit(true);
			} catch (SQLException e) {
				LogManager.error(LogProcessorContext.logCategory, e.toString(),e); 
			}
        	DBManager.close(pstmt);
        	DBManager.close(con);
        }
	}
	/**
	 * 로그를 파싱하여 ArrayList에 담는다.
	 * @param logs
	 * @return
	 */
	protected ArrayList parseLog(Object[] logs) {
		
		ArrayList paramList = new ArrayList();
		
		LogContext logContext = new LogContext();
		for(int i=0; i<logs.length; i++){
			 if(logs[i]==null) continue;
			 
			 logContext.clear();
			 logContext = parseLog(logContext, (String)logs[i]);
			 paramList.add(getInsertParamArray(logContext));
			 
		}
		return paramList;
	}
	
	
	protected AbstractDBLogger(){}
	
	/**
	 * LogContext에 들어있는 값을 사용하여 insert시 바인드할 값 배열을 만들어 리턴한다.
	 * 리턴되는 배열의 크기는 getInsertSQL()로 얻어낸 SQL의 바인드변수(?)갯수와 같아야 한다.
	 * 
	 * @param logContext
	 * @return
	 */
	public abstract Object[] getInsertParamArray(LogContext logContext);
	
	/**
	 * DB 테이블에 insert할 SQl문을 리턴한다.
	 * 실제 테이블명에 맞는 SQL문을 리턴하도록 구현해야 한다.
	 * @return insert SQL문
	 */
	public abstract String getInsertSQL();
	
	
	/**
	 * 포맷팅된 로그를 파싱하여 인자로 받은 LogContext에 셋팅하여 리턴한다.
	 * @param logContext
	 * @param formattedLog
	 * @return
	 */
	public abstract LogContext parseLog(LogContext logContext, String formattedLog);
	
	/**
	 * 포맷팅된 로그를 파싱하여 새로 생성된 LogContext에 셋팅하여 리턴한다.
	 * @param logContext
	 * @param formattedLog
	 * @return
	 */
	public LogContext parse(String formattedLog){
		return parseLog(new LogContext(), formattedLog);
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
