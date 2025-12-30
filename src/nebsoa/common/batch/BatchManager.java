/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.batch;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import nebsoa.common.log.LogManager;
import nebsoa.common.schedule.ScheduleManager;
import nebsoa.common.startup.StartupContext;
import nebsoa.common.util.DataMap;
import nebsoa.common.util.FormatUtil;
import nebsoa.common.util.PropertyManager;

import org.apache.commons.lang.StringUtils;

/*******************************************************************
 * <pre>
 * 1.설명 
 * Batch 프로그램 관리 클래스
 * 
 * 2.사용법
 *
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
 * $Log: BatchManager.java,v $
 * Revision 1.1  2018/01/15 03:39:50  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:47  cvs
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
 * Revision 1.2  2008/09/16 06:47:32  youngseokkim
 * PROPERTY 변수 값 수정
 *
 * Revision 1.1  2008/08/04 08:54:53  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.5  2008/06/24 07:21:05  김영석
 * BATCH에 로그 남을수 있도록 수정
 *
 * Revision 1.4  2008/05/15 01:23:35  오재훈
 * *** empty log message ***
 *
 * Revision 1.3  2008/05/14 08:35:35  김영석
 * registAllBatch()에서 모든 스케줄 등록시 에러 발생하면
 * throws 하진 않고 로그 남기도록 수정
 *
 * Revision 1.2  2008/04/03 05:54:12  김희원
 * forceStart 메소드
 *
 * setWasInstanceId(System.getProperty("SPIDER_INSTANCE_ID")); -> StartupContext.getInstanceId()로 변경
 *
 * Revision 1.1  2008/01/22 05:58:28  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:58  안경아
 * *** empty log message ***
 *
 * Revision 1.13  2007/07/16 05:35:27  오재훈
 * *** empty log message ***
 *
 * Revision 1.12  2007/03/07 02:53:45  김성균
 * 일부 로그 INFO 레벨로 변경
 *
 * Revision 1.11  2006/11/02 00:30:30  오재훈
 * *** empty log message ***
 *
 * Revision 1.10  2006/10/30 05:00:44  오재훈
 * *** empty log message ***
 *
 * Revision 1.9  2006/10/25 02:08:38  오재훈
 * main으로 실행하면 배치 인포를 받지 않기 위해서 재실행가능여부를 parameter로 받게 수정.
 *
 * Revision 1.8  2006/10/24 14:21:39  오재훈
 * *** empty log message ***
 *
 * Revision 1.7  2006/09/28 05:40:44  오재훈
 * 재실행 여부 = N이면 배치 기준일에 실행되었으면 Exception발생
 *
 * Revision 1.6  2006/09/14 04:21:49  오재훈
 * *** empty log message ***
 *
 * Revision 1.5  2006/09/13 13:09:40  오재훈
 * 배치 수동 실행까지 적용.
 *
 * Revision 1.4  2006/09/12 09:04:31  오재훈
 * *** empty log message ***
 *
 * Revision 1.3  2006/09/12 00:42:42  오재훈
 * 작업중
 *
 * Revision 1.2  2006/09/09 07:30:16  오재훈
 * *** empty log message ***
 *
 *
 *
 * </pre>
 ******************************************************************/

public class BatchManager {

	
	private static BatchManager instance;

	//현재 실행중인 배치 인스턴스 풀
	private static HashMap executeBatchPool;
	
	/**
	 * WAS 기동시 WebInitializer에 의해서 초기화 하는 부분
	 */
	private BatchManager() {
		init();
	}
	
	/**
	 * 싱글톤 객체 생성
	 * @return
	 */
	public static BatchManager getInstance(){

		if(instance == null)
		{
			instance = new BatchManager();
		}
		return instance;
	}
	
	/**
	 * BatchManager 초기화
	 */
	public void init(){
		if(executeBatchPool == null) 
		{
			executeBatchPool = new HashMap();
		}
		registAllBatch();
	}

	/**
	 * BatchManager Reload
	 */
	public void reload(){
		executeBatchPool = null; 
		init();
	}
	
	
	/**
	 * 실행중인 배치목록 리턴
	 * @return
	 */
	public HashMap getExecuteBatchMap(){
		return executeBatchPool;
	}
	
	/**
	 * 실행중인 배치 리스트에서 조회
	 * @param key
	 * @return
	 */
	public BaseBatchJob getExecuteBatch(String key){
		BaseBatchJob job = (BaseBatchJob) executeBatchPool.get(key);
		return job;
	}
	
	/**
	 * 실행중인 배치 목록에 추가
	 * @param key
	 * @param job
	 */
	public synchronized void setExecuteBatch(String key,BaseBatchJob job) {

		if( getExecuteBatch(key) != null)
		{
			throw new BatchException("FRB00001","이미 실행중입니다.");
		} else
		{
			executeBatchPool.put(key,job);
		}
	}	
	
	/**
	 * 실행중인 배치 목록에서 제거
	 * @param key
	 */
	public void removeExecuteBatch(String key){
		executeBatchPool.remove(key);
	}
	
	
	/**
	 * 스케쥴러에 등록된 배치 등록
	 *
	 */
	public void registAllBatch(){
		
		BatchLoader batchLoader = new BatchLoader();
		
		HashMap hashMap = batchLoader.getWasBatch();
		Set setBatchKey = hashMap.keySet();
		Iterator itKey = setBatchKey.iterator();
		
		String batchAppId = "";
		String batchClassName = "";
		String cronText = "";
		BatchInfo batchInfo; 
		
		while(itKey.hasNext()){
			batchAppId = (String)itKey.next();
			
			batchInfo = (BatchInfo) hashMap.get(batchAppId);
			batchClassName = batchInfo.getBatchAppFileName();
			cronText = batchInfo.getCronText();

			
			//default.properties.xml에 BATCH_MODE 설정으로 스케쥴 등록 여부 결정
			if("Y".equals(PropertyManager.getProperty("default","BATCH_MODE","N"))){	
				//크론 표현식이 없을경우 배치 스케쥴러에 등록하지 않음
				if(cronText != null && cronText.length() != 0){
					try {
						//배치프로그램ID,배치 그룹명,실행프로그램 명,Cron표현식
						ScheduleManager.getInstance().regist(batchAppId,"BATCH",batchClassName,cronText);
					}
					catch(Exception e) {
						LogManager.error("BATCH", "스케줄 등록 도중 에러가 발생하였습니다. [batchAppId : " 
								+ batchAppId + "] - " + e.getMessage());
					}
				}
			}						
		}
	}

	
	/**
	 * 새로운 배치 등록시 해당 WAS Instance에 ejb로 이 메소드를 호출하여 배치 등록
	 * @param regBatchAppId
	 */
	public void regist(String regBatchAppId){
		
		BatchInfo batchInfo;
		
		batchInfo = BatchLoader.getBatchInfo(regBatchAppId);

		//default.properties.xml에 BATCH_MODE 설정으로 스케쥴 등록 여부 결정
		if("Y".equals(PropertyManager.getProperty("default","BATCH_MODE","N"))){
			//크론 표현식이 없을경우 배치 스케쥴러에 등록하지 않음
			if( batchInfo.getCronText() != null && batchInfo.getCronText().length() != 0 ){
				ScheduleManager.getInstance().regist(batchInfo.getBathcAppId(),"BATCH",
						batchInfo.getBatchAppFileName(),batchInfo.getCronText());
			}
		}
	}
	
	/**
	 * 배치 삭제시 해당 배치 프로그램이 스케쥴에 걸려 있다면 삭제.
	 * @param batchAppId : 삭제한 배치 ID
	 */
	public void unRegist(String batchAppId) {

		if(ScheduleManager.getInstance().isStarted(batchAppId)) {
			ScheduleManager.getInstance().unregist(batchAppId);
		}
	}
	
	
	public static final String BATCH_ID = "batchAppId";//배치 APP ID
	public static final String PROPERTY = "property";//배치 APP 파라미터
	public static final String STD_DATE = "stdDate";//배치 기준일자
	public static final String EXECUTE_USER_ID = "userId";//실행자 ID
	
	/**
	 * 배치프로그램 강제 실행
	 * @param batchAppId
	 */
	public void forceStart(DataMap map){

		LogManager.info("♣♣♣♣♣♣♣♣♣♣♣♣ BaseBatchJob forceStart ♣♣♣♣♣♣♣♣♣");
		
		String batchAppId = map.getParameter(BATCH_ID);
		String standardDate = map.getParameter(STD_DATE);
		String userId = map.getParameter(EXECUTE_USER_ID);
		String property = map.getParameter(PROPERTY,"");
		
		BatchInfo bInfo = BatchLoader.getBatchInfo(batchAppId);
				
		BatchContext bCtx = new BatchContext();

    	bCtx.setBatchId(batchAppId);  	
    	//bCtx.setWasInstanceId(System.getProperty("SPIDER_INSTANCE_ID"));
    	bCtx.setWasInstanceId(StartupContext.getInstanceId());
    	bCtx.setStartDate(FormatUtil.getToday("yyyyMMddHHmmss"));//시작일시
    	bCtx.setStandardDate(standardDate);
    	bCtx.setExeUserId(userId);

    	DataMap batchMap = bCtx.getDataMap();

    	if(property.trim().length()!=0 && property != null)
    	{
        	String[] tokenString = StringUtils.splitByWholeSeparator(property, ";");
    		String[] keyValueUnit = null;

    		for(int i=0; tokenString!=null && i<tokenString.length; i++){
    			keyValueUnit = StringUtils.splitByWholeSeparator(tokenString[i], "=");
    			
    			if(keyValueUnit!=null && keyValueUnit.length>=2){
    				bCtx.setParam(keyValueUnit[0], keyValueUnit[1]);
    			}
    		}
    	}
    	
//    	bCtx.setDataMap(batchMap);

    	
		try {

			BaseBatchJob pBatch = (BaseBatchJob) Class.forName(bInfo.getBatchAppFileName()).newInstance();

			//만약 실행중인 배치일경우 실행중 메세지 리턴. 실행중이 아닐경우 실행인스턴스 맵에 add
			setExecuteBatch(batchAppId,pBatch);

			pBatch.execute(bCtx,bInfo.getRetryableYn());//배치 실행
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally{
			//add했던 인스턴스 해제
			removeExecuteBatch(batchAppId);
		}
		
		LogManager.info("♣♣♣♣♣♣♣♣♣♣♣♣ BaseBatchJob force Start End ♣♣♣♣♣♣♣♣♣");
		
	}

	/**
	 * 배치프로그램 강제 종료
	 * @param batchAppId
	 */
	public void forceStop(String batchAppId){

		BaseBatchJob stopJob = getExecuteBatch(batchAppId);
		if(stopJob == null) 
		{
			throw new BatchException("FRB00002","이미 종료한 배치프로그램입니다.");
		}
		stopJob.setStateCode(stopJob.BATCH_STATE_OF_KILLED);
	}
	
}
