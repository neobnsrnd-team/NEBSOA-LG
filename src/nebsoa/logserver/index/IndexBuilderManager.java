/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
package nebsoa.logserver.index;

import java.io.IOException;
import java.util.ArrayList;

import nebsoa.common.log.LogManager;
import nebsoa.logserver.LogProcessorContext;
/*******************************************************************
 * <pre>
 * 1.설명 
 * IndexBuilder를 종료시키고 주기적으로 flush 시키는 클래스. 
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
 * $Log: IndexBuilderManager.java,v $
 * Revision 1.1  2018/01/15 03:39:50  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:43  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:24  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:27  김성균
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
 * Revision 1.6  2007/09/03 15:33:18  shkim
 * *** empty log message ***
 *
 * Revision 1.5  2007/08/30 16:50:05  shkim
 * *** empty log message ***
 *
 * Revision 1.4  2007/08/30 11:23:34  김승희
 * *** empty log message ***
 *
 * Revision 1.3  2007/08/24 07:57:12  김승희
 * 주석 수정
 *
 * Revision 1.2  2007/08/24 07:00:51  김승희
 * *** empty log message ***
 *
 *
 * </pre>
 ******************************************************************/
public class IndexBuilderManager {
	
	private static IndexBuilderManager instance;
	
	private static Object dummy = new Object();
	
	public static IndexBuilderManager getInstance(){
		if(instance==null){
			synchronized(dummy){
				if(instance==null) instance = new IndexBuilderManager();
			}
		}
		return instance;
	}
	
	private ArrayList indexBuilderList = new ArrayList();
	
	public boolean isStarted;

	private IndexBuilderChecker indexBuilderChecker;
	
	private IndexBuilderManager(){}
	
	public void add(MemoryIndexBuilder indexBuilder){
		indexBuilderList.add(indexBuilder);
	}
	
	public MemoryIndexBuilder get(int index){
		return (MemoryIndexBuilder)(indexBuilderList.get(index));
	}
	
	public int size(){
		return indexBuilderList.size();
	}
	
	public synchronized void start(){
		isStarted = true;
		startIndexBuilderChecker();
	}
	
	
	public void flushIndexBuilders(){
		for(int i=0; i<size(); i++){
			long lastSendTime = get(i).getLastFlushTime();
			if(System.currentTimeMillis()-lastSendTime>=LogProcessorContext.indexBuilderManagerFlushTimeThreshold){
				//RamIndexBuilder를  flush시킨다.
				try {
					if(!get(i).isOnFlush()) get(i).flush();
				} catch (Exception e) {
					LogManager.error(LogProcessorContext.logCategory, e);
				}
			}
		}
	}
	
	public synchronized void destory(){
		
		isStarted = false;
		
		try{
			stopIndexBuilderChecker();
		}catch(Throwable th){}
		
		LogManager.info(LogProcessorContext.logCategory, "RamIndexBuilder를 종료합니다..");
		//1. RamIndexBuilder를 종료시킨다.
		for(int i=0; i<size(); i++){
			try {
				get(i).destroy();
				LogManager.info(LogProcessorContext.logCategory, "RamIndexBuilder["+i +"]를 종료하였습니다..");
			} catch (Exception e) {
				LogManager.error(LogProcessorContext.logCategory, e);
			}
		}
		indexBuilderList.clear();
		LogManager.info(LogProcessorContext.logCategory, "FileIndexBuilder를 종료합니다..");
        //2. FileIndexBuilder를 종료시킨다.
		try {
			FileIndexBuilder.getInstance().destroy();
			LogManager.info(LogProcessorContext.logCategory, "FileIndexBuilder를 종료하였습니다..");
		} catch (IOException e) {
			LogManager.error(LogProcessorContext.logCategory, e);
		}
		
	}
	
	private class IndexBuilderChecker implements Runnable{
		
		private boolean exit;
		
		public void run() {
			
			while(!exit && isStarted){

				try {
					Thread.sleep(5*1000);
					flushIndexBuilders();
				} catch (InterruptedException e) {}
			}
			LogManager.info(LogProcessorContext.logCategory, "IndexBuilderChecker를 종료합니다.");

		}

		public void stop(){
			this.exit = true;
		}
	}
	
	/**
	 * IndexBuilderChecker를 시작시킨다.
	 */
	private void startIndexBuilderChecker(){
		try {
			indexBuilderChecker = new IndexBuilderChecker();
			Thread th = new Thread(indexBuilderChecker);
			th.setName("[IndexBuilderManagerThread]");
			th.start();
			LogManager.info(LogProcessorContext.logCategory, "IndexBuilderChecker를 시작합니다.");
		} catch (Exception e) {
			LogManager.error(LogProcessorContext.logCategory, "IndexBuilderChecker 시작 중 오류 발생", e);
		
		}
	}
	
	/**
	 * IndexBuilderChecker를 종료시킨다.
	 */
	private void stopIndexBuilderChecker(){
		if(indexBuilderChecker!=null){
			indexBuilderChecker.stop();
		}
	}
}
