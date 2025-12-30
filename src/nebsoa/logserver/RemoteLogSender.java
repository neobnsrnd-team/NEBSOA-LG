/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
package nebsoa.logserver;

import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.ejb.CreateException;

import nebsoa.biz.exception.BizException;
import nebsoa.common.jndi.EJBManager;
import nebsoa.common.log.LogManager;
import nebsoa.logserver.ejb.RemoteLogSenderEJB;
import nebsoa.logserver.ejb.RemoteLogSenderHome;
/*******************************************************************
 * <pre>
 * 1.설명 
 * ejb를 통하여 리모트의 로그 서버로 로그를 전송하는 woker를 관리하는 클래스
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
 * $Log: RemoteLogSender.java,v $
 * Revision 1.1  2018/01/15 03:39:51  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:58  cvs
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
 * Revision 1.1  2008/08/04 08:54:52  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:27  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.2  2008/01/17 09:40:16  김승희
 * 로그 서버 장애 시 장애 상태 체크 looping 로직에 interval 추가
 *
 * Revision 1.2  2008/01/17 09:33:43  shkim
 * 로그 서버 장애 시 장애 상태 체크 looping 로직에 interval 추가
 *
 * Revision 1.1  2007/12/06 04:57:09  shkim
 * 최초 등록
 *
 *
 *
 * </pre>
 ******************************************************************/
public class RemoteLogSender{
	
	private ArrayList logSendWorkerList = new ArrayList();

	private nebsoa.logserver.RemoteLogSender.ConnectionManager connectionManager;
	
	private static Object dummy=new Object();
	private static RemoteLogSender instance;
	
	private boolean isError;
	
	/**  
     * RemoteLogSender EJB 의 JNDI 이름 
     */
    static final String ejbName = "nebsoa.logserver.ejb.RemoteLogSenderEJB";
    
	public static RemoteLogSender getInstance(){
	   if(instance==null){
	       synchronized (dummy) {
	    	   if(instance==null) instance = new RemoteLogSender();
	       }
	    }
	    return instance;
	}
	    
	private RemoteLogSender(){
		
	}
	
	public void stop() {
		RemoteLogSendWorker logSendWorker = null;
		for(int i=0; i<logSendWorkerList.size(); i++){
			logSendWorker = (RemoteLogSendWorker)logSendWorkerList.get(i);
			logSendWorker.destroy();
			LogManager.info(LogProcessorContext.logCategory, i +"번 째 LogSendWorker를 종료하였습니다.");
		}
		logSendWorkerList.clear();
		
		stopConnectionManager();
	}

	public void start() {
		for(int i=0; i<LogProcessorContext.logSendWorkerCount; i++){
			try {
				RemoteLogSendWorker logSendWorker = new RemoteLogSendWorker(this, i);
				logSendWorkerList.add(logSendWorker);

				Thread th = new Thread(logSendWorker);
				th.start();
			} catch (Exception e) {
				LogManager.error(LogProcessorContext.logCategory, "LogSendWorker[" + i +"] 시작 중 오류 발생", e);
			}
		}
		
		startConnectionManager();
	}
	
    
	/**
	 * @author ksh
	 * 커넥션 재생성, 메시지 flush를 담당하는 쓰레드
	 */
	private class ConnectionManager implements Runnable{
		
		private boolean exit;

		public void run() {
			
			while(!exit){
				
				//에러 상태일 때는 EJB Home cache를 제거후 다시 획득하려고 시도한다.
				if(isError){
					try {
						testEJB();
						setError(false);
						notifyError(false);
					} catch (Exception e) {
						setError(true);
						notifyError(true);
						LogManager.error(LogProcessorContext.logCategory, "LogServer ConnectionManager initialization[로그서버 커넥션 checking] failed..[" + e +"]");
					}
				}
				synchronized(connectionManager){
					try {
						//Thread.sleep(LogProcessorContext.logSenderConnectionCheckInterval);
						connectionManager.wait(LogProcessorContext.logSenderConnectionCheckInterval);
					} catch (InterruptedException e) {}
				}
			}
			LogManager.info(LogProcessorContext.logCategory, "ConnectionManager를 종료하였습니다.");

		}

		private void testEJB() throws RemoteException, CreateException {
			EJBManager.removeCache(ejbName, LogProcessorContext.remoteLogSenderWasId);
			getEJB();
		}
		
		public void stop(){
			LogManager.info(LogProcessorContext.logCategory, "ConnectionManager를 종료합니다..");
			this.exit = true;
			synchronized(connectionManager){
				connectionManager.notifyAll();
			}
		}
	}
	/**
	 * ConnectionManager 쓰레드를 시작시킨다.
	 */
	private void startConnectionManager(){
		try {
			connectionManager = new ConnectionManager();

			Thread th = new Thread(connectionManager);
			th.start();			
			LogManager.info(LogProcessorContext.logCategory, "ConnectionManager를 시작합니다.");
			
		} catch (Exception e) {
			LogManager.error("ConnectionManager 시작 중 오류 발생", e);
		}
	}
	
	/**
	 * ConnectionManager 쓰레드를 중지시킨다.
	 */
	private void stopConnectionManager(){
		if(connectionManager!=null){
			connectionManager.stop();
		}
	}

	public void setError(boolean isError) {
		this.isError = isError;
	}
	
	public void notifyError(boolean isError){
		RemoteLogSendWorker logSendWorker = null;
		for(int i=0; i<logSendWorkerList.size(); i++){
			logSendWorker = (RemoteLogSendWorker)logSendWorkerList.get(i);
			logSendWorker.setError(isError);
		}
		LogManager.info(LogProcessorContext.logCategory, "logSendWorker로 isError상태[" + isError +"] 전달");
	}
	
	
    /**
     * @param wasConfig
     * @return
     * @throws RemoteException
     * @throws CreateException
     * @throws BizException 
     */
    public static RemoteLogSenderEJB getEJB() throws RemoteException, CreateException{
    	RemoteLogSenderHome home = getEJBHome(true);

    	RemoteLogSenderEJB bean;
        try {
            bean = home.create();              
        } catch (CreateException e) {
            home = getEJBHome(false);
            return home.create();            
        }
        return bean;
    }

    /**
     * @param wasConfig
     * @param useCache
     * @return
     * @throws BizException 
     */
    
    private static RemoteLogSenderHome getEJBHome(boolean useCache) {
        
    	String wasConfig = LogProcessorContext.remoteLogSenderWasId;
    	if (!useCache) {
            EJBManager.removeCache(ejbName, wasConfig);
        }
    	
    	RemoteLogSenderHome home = 
            (RemoteLogSenderHome) EJBManager.lookup(ejbName, wasConfig);
        
        return home;
    }

}
