/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
package nebsoa.logserver.dblog;

import java.lang.reflect.Method;

import nebsoa.logserver.LogProcessorContext;
import nebsoa.logserver.LogReceiveWorker;
import nebsoa.logserver.LogReceiver;

/*******************************************************************
 * <pre>
 * 1.설명 
 * MQ로부터 로그 메시지를 수신하여 DB에 저장하는 쓰레드
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
 * $Log: DBLogWorker.java,v $
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
 * Revision 1.1  2007/09/21 08:22:30  김승희
 * 최초 등록
 *
 *
 *
 * </pre>
 ******************************************************************/
public class DBLogWorker extends LogReceiveWorker {
	
	AbstractDBLogger dbLogger;
	
	/**
	 * 생성자
	 * @param logReceiver 
	 * @param index 
	 */
	public DBLogWorker(LogReceiver logReceiver, int index){
		super(logReceiver, index);
	}
	
	/* (non-Javadoc)
	 * @see nebsoa.logserver.LogReceiveWorker#init()
	 */
	public void init() throws Exception{
		
		//DBLogger를 구하여 셋팅한다.
		Class dbLoggerClass = Class.forName(LogProcessorContext.dbLogger);
		Method method = dbLoggerClass.getMethod("getInstance", null);
		dbLogger = (AbstractDBLogger)method.invoke(dbLoggerClass, null);
		
		super.init();
	}

	/* (non-Javadoc)
	 * @see nebsoa.logserver.LogReceiveWorker#getName()
	 */
	public String getName(){
		return "DBLogWorker[" + index + "]";
	}

	/* (non-Javadoc)
	 * @see nebsoa.logserver.LogReceiveWorker#processLog(java.lang.Object[])
	 */
	protected void processLog(Object[] logs) throws Exception {
		dbLogger.add(logs, index);
	}
}