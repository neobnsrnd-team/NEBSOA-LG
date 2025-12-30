/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.monitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import nebsoa.common.cache.policy.CacheFactory;
import nebsoa.common.cache.policy.LFUCache;
import nebsoa.common.file.FileManager;
import nebsoa.common.log.LogManager;
import nebsoa.common.startup.StartupContext;
import nebsoa.common.util.PropertyManager;
import nebsoa.common.util.StringUtil;

import org.shiftone.cache.Cache;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 시스템에서 관리되고 있는 모니터링 대상을 관리하는 클래스
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
 * $Log: SysMonitor.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:29  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:27  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:29  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:55  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.6  2008/07/04 12:32:23  김은정
 * addMonitorData에 try~catch추가
 *
 * Revision 1.5  2008/03/21 09:03:43  김성균
 * Cache 저장된 Map을 clone()해서 가져오도록 처리 : java.util.ConcurrentModificationException 때문에...
 *
 * Revision 1.4  2008/03/20 06:23:09  김희원
 * getMonitorDatas 추가
 *
 * Revision 1.3  2008/03/20 06:13:24  김희원
 * Flex용 성능모니터링 getMonitorDatas 추가
 *
 * Revision 1.2  2008/03/20 01:52:34  김성균
 * Cache에 addObject()시 동기화 처리 추가
 *
 * Revision 1.1  2008/01/22 05:58:33  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:44  안경아
 * *** empty log message ***
 *
 * Revision 1.12  2007/10/09 04:17:15  안경아
 * *** empty log message ***
 *
 * Revision 1.11  2007/07/31 10:04:06  김성균
 * Cache 알고리즘 적용
 *
 * Revision 1.10  2007/06/06 10:12:03  최수종
 * 성능모니터링 결과를 ArrayList형식으로 리턴하는 메소드 추가
 *
 * Revision 1.9  2007/06/05 10:39:24  이종원
 * monitor data clear로직 추가
 *
 * Revision 1.8  2007/05/03 10:15:20  이종원
 * 평균 수행시간 계산 로직 수정
 *
 * Revision 1.7  2006/12/12 08:08:44  김성균
 * *** empty log message ***
 *
 * Revision 1.6  2006/11/29 01:51:19  오재훈
 * *** empty log message ***
 *
 * Revision 1.5  2006/10/08 07:27:43  김성균
 * *** empty log message ***
 *
 * Revision 1.4  2006/07/05 04:08:07  이종원
 * *** empty log message ***
 *
 * Revision 1.3  2006/06/17 10:37:45  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class SysMonitor {

	private static Object dummy = new Object();

	/**
	 *  monitorCategory
	 */
	public static HashMap monitorCategory = new HashMap();//new Hashtable();

	/**
	static{
	    new HtmlReportBuilder().start();
	}
	 **/    

	/**
	 * get monitoring data of 해당 Category
	 */
	public static Cache getCategory(String category) {
		if (monitorCategory == null) {
			monitorCategory = new HashMap();
		}
		Cache cache = (Cache) monitorCategory.get(category);
		if (cache == null) {
			cache = CacheFactory.getInstance().getLfuCache(category,getMonitoringCacheTimerout(),getMonitoringCacheSize());
			monitorCategory.put(category, cache);
		}
		return cache;
	}

	/**
	 * get monitoring data of 해당 Category (getCategory와 동일)
	 */
	public static Cache getCategoryData(String category) {
		return getCategory(category);
	}

	/**
	 * clear 해당 Category data
	 */
	public static void clearCategoryData(String category) {
		Cache data = getCategory(category);
		if (data != null)
			data.clear();
	}

	/**
	 * clear all Category data
	 */
	public static void clearAllCategoryData() {
		if (monitorCategory != null)
			monitorCategory.clear();
	}

	/**
	 * 해당 카테고리에서 해당 통계 데이타를 담고 있는 개체 리턴
	 */
	public static MonitorData getObject(String category, String key) {
		return (MonitorData) (getCategory(category).getObject(key));
	}

	/**
	 * 해당 카테고리에서 해당 통계 데이타 update
	 */
	public static void addMonitorData(MonitorData data, boolean isAvgCalc) {
		Cache cache = getCategory(data.getCategory());
		try{
			MonitorData existData = (MonitorData) cache.getObject(data.getInfo());
			if (existData == null) {
				existData = data;
				synchronized(cache) {
					cache.addObject(data.getInfo(), existData);
				}
			}
			existData.update(data.getExeTime(), isAvgCalc);
			if(dumpTime()){
				Iterator categoryList = getCategoryList();
				if(categoryList != null){
					while(categoryList.hasNext()){
						String category = (String)categoryList.next();			
						dumpToFile(category);
						//clear 한다.
						clearCategoryData(category);	
					}
				}
			}
		}catch(Exception e){
			LogManager.error("모니터링 데이타 수집중 오류 : " + e.toString());
		}
	}

	static boolean dumpTime(){return false;}

	private static void dumpToFile(String category){
//		BufferedWriter bw = null;
		String seperator = ", ";
		try{			
			//bw = new BufferedWriter(new FileWriter(getFile(), true));
			LFUCache categoryData = (LFUCache)getCategoryData(category);
			Hashtable ht = (Hashtable) categoryData.getCacheMap();
			Set set = ((Map) ht.clone()).keySet();
			Iterator i = set.iterator();
			Object key = null;
			MonitorData monitorData = null;
			StringBuffer data = new StringBuffer();
			if(i.hasNext()){
				key = i.next();
				monitorData = (MonitorData)categoryData.getObject(key);
				data.append("INSERT INTO FWK_MONITOR_REPORT (MON_DATE, MON_TYPE, TIME_LINE, WAS_ID, MON_ID, EXE_CNT, TOT_TIME, MIN_TIME, MAX_TIME, AVG_TIME) VALUES (");
				data.append(addSingleQuotation("")+seperator);//MON_DATE
				data.append(addSingleQuotation(category)+seperator);//MON_TYPE
				data.append(addSingleQuotation("")+seperator);//TIME_LINE
				data.append(addSingleQuotation(StartupContext.getInstanceId())+seperator);//WAS_ID				
				data.append(addSingleQuotation(monitorData.getInfo())+seperator);//MON_ID
				data.append(monitorData.getExeCount()+seperator);//EXE_CNT
				data.append(monitorData.getTotalExeTime()+seperator);//TOT_TIME
				data.append(monitorData.getMinExeTime()+seperator);//MIN_TIME
				data.append(monitorData.getMaxExeTime()+seperator);//MAX_TIME
				data.append((1.0*monitorData.getTotalExeTime()/monitorData.getAvgCount())+");\n");//AVG_TIME
				//bw.write(data.toString());
				LogManager.info("MONITOR_DATA", data.toString());
			}			

		}catch (Exception e) {
			LogManager.error("ERROR", "Monitor data dumpToFile fail:"+e.getMessage(), e);
			e.printStackTrace();
		}
//		finally{
//		try {
//		if (bw != null) {
//		bw.flush();
//		bw.close();
//		}
//		} catch (IOException e) {
//		LogManager.error("ERROR", "Monitor data dumpToFile close fail:"+e.getMessage(), e);
//		}
//		}
	}

	private static String addSingleQuotation(String s){
		return "'"+s+"'";
	}

//	private static File getFile(){
//	String fileDirName = PropertyManager.getProperty("monitor", "MONITOR_DUMP_DATA_DIR_"+StartupContext.getInstanceId(), "/temp");
//	File fileDir = new File(fileDirName);
//	if(!fileDir.exists()){
//	if(!fileDir.mkdir()){
//	LogManager.error("monitor dump 디렉토리가 존재하지 않으며, 생성하지 못하였습니다."+fileDir);
//	}else{
//	LogManager.info("monitor dump 디렉토리가 존재하지 않아서 새로  생성하였습니다."+fileDir);
//	}
//	}
//	File file = new File(fileDir+ "/fileName" + ".sql");

//	return file;
//	}

	/**
	 * 해당 카테고리에서 해당 통계 데이타 update
	 */
	public static void addMonitorData(MonitorData data){
		addMonitorData(data, true);
	}

	/**
	 * HtmlReport
	 */
	public static String getHtmlReport(String category) {
		LFUCache cache = (LFUCache) getCategory(category);
		Hashtable ht = (Hashtable) cache.getCacheMap();
		Set set = ((Map) ht.clone()).keySet();
		if (set.isEmpty())
			return "No Monitor Data";
		Iterator i = set.iterator();
		MonitorData data = null;
		Object key = null;
		if (i.hasNext()) {
			key = i.next();
			data = (MonitorData) cache.getObject(key);
		}
		StringBuffer buf = new StringBuffer(MonitorData.getHtmlHeader());
		buf.append(data.getHtmlReport());
		while (i.hasNext()) {
			key = i.next();
			data = (MonitorData) cache.getObject(key);
			buf.append(data.getHtmlReport());

		}
		buf.append(MonitorData.getHtmlTail());
		return buf.toString();
	}

	/**
	 * FlexReport
	 */
	public static ArrayList getMonitorDatas(String category) {
		LFUCache cache = (LFUCache) getCategory(category);
		Hashtable ht = (Hashtable) cache.getCacheMap();
		Set set = ((Map) ht.clone()).keySet();
		Iterator i = set.iterator();
		MonitorData data = null;
		Object key = null;

		ArrayList list = new ArrayList();

		list.add(data.getHtmlReport());

		while (i.hasNext()) {
			key = i.next();
			data = (MonitorData) cache.getObject(key);
			list.add(data);
		}
		return list;
	}

	/**
	 * 특정 시간 이상, 특정 문자열 포함한 항목을 ArrayList로 추출
	 */
	public static ArrayList getMonitorDatas(String category, String filteredString,
			long minValue) {

		LFUCache cache = (LFUCache) getCategory(category);
		Hashtable ht = (Hashtable) cache.getCacheMap();
		Set set = ((Map) ht.clone()).keySet();

		Iterator i = set.iterator();
		MonitorData data = null;
		Object key = null;
		ArrayList list = new ArrayList();
		while (i.hasNext()) {
			key = i.next();
			data = (MonitorData) cache.getObject(key);
			if (data.isAvgOver(minValue) && data.containsString(filteredString)) {
				list.add(data);
			}
		}
		return list;
	}




	/**
	 * 특정 시간 이상, 특정 문자열 포함한 항목을 HtmlReport로 추출
	 */
	public static String getHtmlReport(String category, String filteredString,
			long minValue) {

		LFUCache cache = (LFUCache) getCategory(category);
		Hashtable ht = (Hashtable) cache.getCacheMap();
		Set set = ((Map) ht.clone()).keySet();
		if(set.isEmpty()) 
		{	
			return MonitorData.getHtmlHeader()+"\r\n\t<tr>\r\n\t\t<td class='tableAText01C' colspan=5>No Monitor Data\r\n\t\t</td>\r\n\t</tr>\r\n</tbody>\r\n</table>\r\n";	
		}

		Iterator i = set.iterator();
		MonitorData data = null;
		Object key = null;
		StringBuffer buf = new StringBuffer(MonitorData.getHtmlHeader());
		while (i.hasNext()) {
			key = i.next();
			data = (MonitorData) cache.getObject(key);
			if (data.isAvgOver(minValue) && data.containsString(filteredString)) {
				buf.append(data.getHtmlReport());
			}
		}
		buf.append(MonitorData.getHtmlTail());
		return buf.toString();
	}

	/**
	 * return category list
	 * @return
	 */
	public static Iterator getCategoryList(){
		if(monitorCategory == null){
			monitorCategory = new HashMap();
		}
		return  monitorCategory.keySet().iterator();
	}

	/**
	 * return category list
	 * @return
	 */
	public static Object[] getCategorys() {
		if(monitorCategory == null){
			monitorCategory = new HashMap();
		}
		return  monitorCategory.keySet().toArray();
	}

	/**
	 * test of SysMonitor class
	 * @param args
	 */
	public static void main(String[] args) {
		for(int i=0;i<10;i++){
			MonitorData test1 = new MonitorData("Database","SELECT * FROM EMP"+(i%5),i*1);
			SysMonitor.addMonitorData(test1);
		}
		for(int i=0;i<100;i++){
			MonitorData test1 = new MonitorData("Message","SELECT * FROM TEST"+(i%10),i*100);
			SysMonitor.addMonitorData(test1);
		}

		System.out.println(getHtmlReport("Database","EMP",0));	

		System.out.println(getHtmlReport("Message","TEST",0));  

		Iterator i = getCategoryList();
		while(i.hasNext()){
			System.out.println(i.next()+"-->key");
		}
	}

	static long lastReportTime = System.currentTimeMillis();

	/**
	 * 
	 * <p>Title : nebsoa.common.monitor.HtmlReportBuilder</p>
	 * <p>Created Date & Time : 2005. 8. 30. 오후 9:44:21</p>
	 * <p>Description : HTML REPORT BUILDER
	 * </p>
	 * 
	 * <p>Copyright : www.nebsoa.co.kr</p>
	 * @author 이종원
	 */
	public static class HtmlReportBuilder extends Thread{

		public void run(){
			LogManager.debug("HtmlReportBuilder().start();");
			boolean reportMode=false;
			String reportSaveMode=PropertyManager.getProperty("monitor","REPORT_SAVE_MODE","false");
			if("true".equals(reportSaveMode)||"ON".equals(reportSaveMode)){
				reportMode=true;
			}else{
				return;
			}

			long reportBuildTerm = PropertyManager.getIntProperty("monitor","REPORT_SAVE_TERM");
			reportBuildTerm = reportBuildTerm*1000*60;

			String reportSaveDir=PropertyManager.getProperty("monitor","REPORT_SAVE_DIR");	        
			if(reportSaveDir==null)reportSaveDir=System.getProperty("SERVERSIDE_HOME")+"/logs";

			LogManager.debug("report Builder Info==>\r\n\t주기:"+(reportBuildTerm/60000.0)+" 분\nreportMode:"
					+reportMode+",\n저장 디렉토리:"+reportSaveDir);
			while(reportMode){
				Iterator i = getCategoryList();

				while(i.hasNext()){ 
					String category = (String)(i.next());
					LogManager.debug("TUNNING_LIST","CategoryList:"+category);
					String htmlReportData= getHtmlReport(category,null,2000);	
					FileManager.updateFile(reportSaveDir,category,htmlReportData,true );
				}
				try {
					sleep(reportBuildTerm);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}


	/**
	 * 특정 시간 이상, 특정 문자열 포함한 항목을 ObjectReport로 추출
	 */
	public static ArrayList getObjectReport(String category,
			String filteredString,long minValue){

		LFUCache cache = (LFUCache) getCategory(category);
		Hashtable ht = (Hashtable) cache.getCacheMap();
		Set set = ((Map) ht.clone()).keySet();
		if(set.isEmpty()) 
		{	
			return null;	
		}

		Iterator i = set.iterator();
		MonitorData data = null;
		Object key = null;
//		if(i.hasNext()){		
//		key = i.next();
//		data = (MonitorData) dataHash.get(key);
//		}

		ArrayList arrayList = new ArrayList();
		HashMap hm = null;
		StringBuffer dataInfo = new StringBuffer();

//		buf.append(data.getHtmlReport());
		while(i.hasNext()){
			key = i.next();
			data = (MonitorData) cache.getObject(key);
			if(data.isAvgOver(minValue) && data.containsString(filteredString)){				
				//buf.append(data.getHtmlReport());

				hm = new HashMap();

				if(StringUtil.isNull(data.etcInfo))
				{
					hm.put("INFO", data.getInfo());
					hm.put("EXE_COUNT", Integer.valueOf(data.getExeCount()));
					hm.put("TOTAL_EXE_SECOND", data.getTotalExeSecond());
					hm.put("MAX_EXE_SECOND", data.getMaxExeSecond());
					hm.put("MIN_EXE_SECOND", data.getMinExeSecond());
					hm.put("AVG_EXE_SECOND", data.getAvgExeSecond());
				}
				else
				{
					dataInfo.append(data.getInfo());
					dataInfo.append("(");
					dataInfo.append(data.etcInfo);
					dataInfo.append(")");

					hm.put("INFO", dataInfo.toString());
					hm.put("EXE_COUNT", Integer.valueOf(data.getExeCount()));
					hm.put("TOTAL_EXE_SECOND", data.getTotalExeSecond());
					hm.put("MAX_EXE_SECOND", data.getMaxExeSecond());
					hm.put("MIN_EXE_SECOND", data.getMinExeSecond());
					hm.put("AVG_EXE_SECOND", data.getAvgExeSecond());		

					dataInfo.delete(0, dataInfo.length());
				}

				arrayList.add(hm);
			}			
		}

		return arrayList;
	}	

	protected static int getMonitoringCacheSize() {
		return PropertyManager.getIntProperty("monitor", "MONITORING_CACHE_SIZE", 1000);    
	}

	protected static long getMonitoringCacheTimerout() {
		return PropertyManager.getLongProperty("monitor", "MONITORING_CACHE_TIMEOUT",  60 * 60 * 24 * 1000);
	}

}
