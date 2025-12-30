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

import nebsoa.common.Constants;
import nebsoa.common.exception.SysException;
import nebsoa.common.jdbc.DBManager;
import nebsoa.common.jdbc.DBResultSet;
import nebsoa.common.startup.StartupContext;
import nebsoa.common.util.StringUtil;

/*******************************************************************
 * <pre>
 * 1.설명 
 * DB에 등록된 Batch 프로그램 로딩
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
 * $Log: BatchLoader.java,v $
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
 * Revision 1.1  2008/08/04 08:54:53  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.3  2008/01/23 11:45:35  오재훈
 * *** empty log message ***
 *
 * Revision 1.2  2008/01/23 11:42:16  오재훈
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:28  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.2  2007/11/30 09:46:55  안경아
 * DB NAME 지정
 *
 * Revision 1.1  2007/11/26 08:38:57  안경아
 * *** empty log message ***
 *
 * Revision 1.9  2007/07/16 12:59:35  오재훈
 * *** empty log message ***
 *
 * Revision 1.8  2007/07/16 05:35:27  오재훈
 * *** empty log message ***
 *
 * Revision 1.7  2007/07/04 05:04:24  오재훈
 * *** empty log message ***
 *
 * Revision 1.6  2007/03/14 07:10:29  최수종
 * *** empty log message ***
 *
 * Revision 1.5  2007/02/01 04:58:00  김성균
 * 사용여부가 'Y' 배치프로그램만 로딩하도록 수정
 *
 * Revision 1.4  2006/09/13 13:09:40  오재훈
 * 배치 수동 실행까지 적용.
 *
 * Revision 1.3  2006/09/12 09:04:31  오재훈
 * *** empty log message ***
 *
 * Revision 1.2  2006/09/09 07:30:16  오재훈
 * *** empty log message ***
 *
 *
 *
 * </pre>
 ******************************************************************/
public class BatchLoader {

	/**
	 * 이 클래스를 실행되는 WAS의 이름
	 */
	private static final String wasName = StartupContext.getInstanceId();

	/**
	 * 실행되는 WAS별 모든 배치 프로그램 로딩
	 */
	private static final String LOAD_ALL_WAS_BATCH =
		" SELECT " 
		+" BATCH_APP_ID , BATCH_APP_FILE_NAME , PRE_BATCH_APP_ID , "
		+" CRON_TEXT , RETRYABLE_YN , PER_WAS_YN , IMPORTANT_TYPE , "
		+" PROPERTIES ,TRX_ID , ORG_ID , IO_TYPE "
		+" FROM FWK_BATCH_APP "
		+" WHERE BATCH_APP_ID IN ( "
		+" SELECT BATCH_APP_ID FROM FWK_WAS_EXEC_BATCH "
		+" WHERE INSTANCE_ID = ? AND USE_YN = 'Y' )";

	/**
	 * 로딩이 되지 않은 한개의 WAS별 배치 프로그램 로딩
	 */
	private static final String LOAD_WAS_BATCH = 
		" SELECT " +
		" BATCH_APP_ID , BATCH_APP_FILE_NAME , PRE_BATCH_APP_ID, " +
		" CRON_TEXT , RETRYABLE_YN , PER_WAS_YN , IMPORTANT_TYPE, " +
		" PROPERTIES ,TRX_ID , ORG_ID , IO_TYPE " +
		" FROM FWK_BATCH_APP " +
		" WHERE BATCH_APP_ID = ( " +
		" SELECT BATCH_APP_ID FROM FWK_WAS_EXEC_BATCH " +
		" WHERE INSTANCE_ID = ? AND BATCH_APP_ID = ? )";
	
	/**
	 * 해당 WAS에서 실행될 배치 프로그램 정보를 가지고 있는 맵
	 */
	private static HashMap wasBatch;
	
	
	public HashMap getWasBatch(){
		if(wasBatch == null){
			init();
		}		
		return wasBatch;
	}
	
	public BatchLoader() {
		wasBatch = null;//이 클래스를 new로 선언하면 초기화 된다.
		
		init();
	}

	public void init(){
		if(wasBatch == null)
		{
			wasBatch = new HashMap();
			loadAll();
		}
	}
	
	/**
	 * 이 클래스가 실행되고 있는 WAS의 INSTANCE ID로 등록되어 있는 모든 배치 APP 로딩
	 *
	 */
	public void loadAll() {

		DBResultSet rs = DBManager.executePreparedQuery(Constants.SPIDER_DB, LOAD_ALL_WAS_BATCH , new Object[]{wasName});
		BatchInfo binfo = null;
		
		while(rs.next())
		{
			binfo = new BatchInfo();
			
			binfo.setBathcAppId(StringUtil.NVL(rs.getString("BATCH_APP_ID"),""));
			binfo.setBatchAppFileName(StringUtil.NVL(rs.getString("BATCH_APP_FILE_NAME"),""));
			binfo.setPreBatchAppId(StringUtil.NVL(rs.getString("PRE_BATCH_APP_ID"),""));
			binfo.setCronText(StringUtil.NVL(rs.getString("CRON_TEXT"),""));
			binfo.setRetryableYn(StringUtil.NVL(rs.getString("RETRYABLE_YN"),""));
			binfo.setPerWasYn(StringUtil.NVL(rs.getString("PER_WAS_YN"),""));
			binfo.setImportantType(StringUtil.NVL(rs.getString("IMPORTANT_TYPE"),""));
			binfo.setProperties(StringUtil.NVL(rs.getString("PROPERTIES"),""));
			binfo.setTrxId(StringUtil.NVL(rs.getString("TRX_ID"),""));
			binfo.setOrgId(StringUtil.NVL(rs.getString("ORG_ID"),""));
			binfo.setIoType(StringUtil.NVL(rs.getString("IO_TYPE"),""));
			
			wasBatch.put(binfo.getBathcAppId(),binfo);
		}
	}
	
	
	/**
	 * 배치를 강제로 실행,정지 시키려 할때 해당 배치프로그램의 정보를 가져오기. cache 데이타에 없을경우 DB조회
	 * @param key : 로딩 시키려는 스케쥴 KEY
	 * @return BatchInfo : 로딩된 배치 프로그램
	 */
	public static BatchInfo getBatchInfo(String key){
		if(wasBatch == null)
		{
			wasBatch = new HashMap();
		}
		BatchInfo bi = (BatchInfo)wasBatch.get(key);
		if(bi == null){
			bi = load(key);
		}
		return bi;
	}
	
	
	/**
	 * DB에서 해당 KEY의 배치 APP 정보 로딩
	 * @param key : 배치 KEY
	 * @return BatchInfo : 로딩된 배치 프로그램
	 */
	public static BatchInfo load(String key) {

		DBResultSet rs = DBManager.executePreparedQuery(Constants.SPIDER_DB, LOAD_WAS_BATCH , new Object[]{wasName,key});
		BatchInfo binfo = null;

		if(rs.next()) {
			binfo = new BatchInfo();
			
			binfo.setBathcAppId(StringUtil.NVL(rs.getString("BATCH_APP_ID"),""));
			binfo.setBatchAppFileName(StringUtil.NVL(rs.getString("BATCH_APP_FILE_NAME"),""));
			binfo.setPreBatchAppId(StringUtil.NVL(rs.getString("PRE_BATCH_APP_ID"),""));
			binfo.setCronText(StringUtil.NVL(rs.getString("CRON_TEXT"),""));
			binfo.setRetryableYn(StringUtil.NVL(rs.getString("RETRYABLE_YN"),""));
			binfo.setPerWasYn(StringUtil.NVL(rs.getString("PER_WAS_YN"),""));
			binfo.setImportantType(StringUtil.NVL(rs.getString("IMPORTANT_TYPE"),""));
			binfo.setProperties(StringUtil.NVL(rs.getString("PROPERTIES"),""));
			binfo.setTrxId(StringUtil.NVL(rs.getString("TRX_ID"),""));
			binfo.setOrgId(StringUtil.NVL(rs.getString("ORG_ID"),""));
			binfo.setIoType(StringUtil.NVL(rs.getString("IO_TYPE"),""));

			
			wasBatch.put(binfo.getBathcAppId(),binfo);
		} else {
			throw new SysException(key + "라는 이름으로 등록된 배치프로그램이 없습니다.");
		}
		
		return binfo;
	}

	
	
	
	/**
	 * 현재 수행하고 있는 배치프로그램의 정보를 가져오기. cache 데이타에 없을경우 DB조회
	 * @param key : 로딩 시키려는 스케쥴 KEY
	 * @param reqWasName : 실행하고있는 배치의 실행 WAS INSTANCE 명
	 * @return BatchInfo : 로딩된 배치 프로그램
	 */
	public static BatchInfo getBatchInfo(String key,String reqWasName){
		BatchInfo bi = (BatchInfo)wasBatch.get(key);
		if(bi == null){
			bi = load(key,reqWasName);
		}
		return bi;
	}
	
	
	/**
	 * DB에서 해당 KEY,WAS INSTANCE 의 배치 APP 정보 로딩
	 * @param key : 배치 KEY
	 * @param reqWasName : 실행하고있는 배치의 실행 WAS INSTANCE 명
	 * @return BatchInfo : 로딩된 배치 프로그램
	 */
	public static BatchInfo load(String key , String reqWasName) {

		DBResultSet rs = DBManager.executePreparedQuery(Constants.SPIDER_DB, LOAD_WAS_BATCH , new Object[]{reqWasName,key});
		BatchInfo binfo = null;

		if(rs.next()) {
			binfo = new BatchInfo();
			
			binfo.setBathcAppId(StringUtil.NVL(rs.getString("BATCH_APP_ID"),""));
			binfo.setBatchAppFileName(StringUtil.NVL(rs.getString("BATCH_APP_FILE_NAME"),""));
			binfo.setPreBatchAppId(StringUtil.NVL(rs.getString("PRE_BATCH_APP_ID"),""));
			binfo.setCronText(StringUtil.NVL(rs.getString("CRON_TEXT"),""));
			binfo.setRetryableYn(StringUtil.NVL(rs.getString("RETRYABLE_YN"),""));
			binfo.setPerWasYn(StringUtil.NVL(rs.getString("PER_WAS_YN"),""));
			binfo.setImportantType(StringUtil.NVL(rs.getString("IMPORTANT_TYPE"),""));
			binfo.setProperties(StringUtil.NVL(rs.getString("PROPERTIES"),""));
			binfo.setTrxId(StringUtil.NVL(rs.getString("TRX_ID"),""));
			binfo.setOrgId(StringUtil.NVL(rs.getString("ORG_ID"),""));
			binfo.setIoType(StringUtil.NVL(rs.getString("IO_TYPE"),""));

			
		} else {
			throw new SysException(key + "라는 이름으로 등록된 배치프로그램이 없습니다.");
		}
		
		return binfo;
	}
}
