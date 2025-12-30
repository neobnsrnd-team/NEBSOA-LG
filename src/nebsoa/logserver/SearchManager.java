/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
package nebsoa.logserver;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.BooleanQuery.TooManyClauses;

import nebsoa.common.log.LogManager;
import nebsoa.common.util.DataMap;
import nebsoa.common.util.FormatUtil;
import nebsoa.common.util.PropertyManager;
import nebsoa.logserver.index.AnalyzerFactory;
import nebsoa.logserver.index.IndexBuilder;

/*******************************************************************
 * <pre>
 * 1.설명 
 * lucene의 Index File을 이용하여 검색어에 대해 조회하는 클래스. 
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
 * $Log: SearchManager.java,v $
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
 * Revision 1.2  2007/12/17 01:48:35  김승희
 * 로그 서버 서버 분리에 따른 수정
 *
 * Revision 1.7  2007/12/06 04:49:13  shkim
 * 요일별/날짜별 폴더에서 로그를 검색하도록 변경
 *
 * Revision 1.6  2007/09/21 08:22:38  김승희
 * *** empty log message ***
 *
 * Revision 1.5  2007/08/23 09:12:18  신경학
 * 로그 수정
 *
 * Revision 1.4  2007/08/21 05:31:37  신경학
 * 수정
 *
 * Revision 1.3  2007/08/21 05:23:17  신경학
 * 수행시간에 따른 로그 레벨 변경 로직
 *
 * Revision 1.2  2007/08/21 05:19:39  신경학
 * *** empty log message ***
 *
 * Revision 1.1  2007/06/15 02:05:07  김승희
 * 프로젝트 변경 신규 커밋
 *
 * Revision 1.6  2007/06/14 08:44:14  shkim
 * *** empty log message ***
 *
 * Revision 1.5  2007/06/14 08:33:52  shkim
 * *** empty log message ***
 *
 * Revision 1.4  2007/06/14 08:28:56  shkim
 * 수정
 *
 * Revision 1.3  2007/06/14 07:40:48  신경학
 * 수정
 *
 * Revision 1.2  2007/06/13 09:45:46  신경학
 * SysException -> LogSearchException
 *
 * Revision 1.1  2007/06/13 05:29:29  shkim
 * 패키지 변경
 *
 * Revision 1.5  2007/06/13 02:10:41  신경학
 * 수정
 *
 * Revision 1.4  2007/06/12 09:22:13  신경학
 * 해당 일자로 폴더 생성
 *
 * Revision 1.3  2007/06/12 05:29:24  신경학
 * 수정
 *
 * Revision 1.2  2007/06/11 08:55:55  신경학
 * Exception 수정
 *
 * Revision 1.1  2007/06/11 08:24:25  shkim
 * 패키지 변경
 *
 * Revision 1.3  2007/06/08 08:50:46  신경학
 * 에러처리 수정
 *
 * Revision 1.2  2007/06/07 13:02:05  신경학
 * 최초 등록
 *
 * </pre>
 ******************************************************************/

public class SearchManager {
	private static Object dummy = new Object();
	private static SearchManager instance;
	private static String defaultDir = PropertyManager.getProperty("logserver","MESSAGE_LOG_INDEX_DIRECTORY");
	public static String field = IndexBuilder.FieldName;
	public static final String searchLine = "_$search_line";
	
	private static Analyzer analyzer;
	private static long overSearchingMills = 5 * 1000;  // 검색 시간이 overSearchingMills 를 초과할 경우에는 info Level로 로그를 남긴다.
	
	public static SearchManager getInstance(){
		if(instance == null){
			synchronized(dummy){
				instance = new SearchManager(); 
			}
		}
		return instance;
	}
	
	public SearchManager(){
		analyzer = AnalyzerFactory.getInstance().getAnalyzer();
	}
	
	public void printSearchResult(ArrayList list){
		for(int i=0; i<list.size(); i++){
		LogManager.debug("array : " + (String)list.get(i));
		}
	}
	
	private IndexReader getReader() throws Exception{
		//인덱스 폴더
		//String indexDir = defaultDir + FormatUtil.getToday();
		
		//오늘 요일
		int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
		//오늘일자(yyyyMMdd)
		String today = FormatUtil.getToday();
		String indexDir = defaultDir + dayOfWeek + File.separator + today;
		
		IndexReader reader = IndexReader.open(indexDir);
		return reader;
	
	}
	
	/**
	 * 검색을 수행합니다. 
	 * @param line
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	public synchronized ArrayList search(String line) throws Exception{
		LogManager.debug("### search called.. ");
		ArrayList list = new ArrayList();
		IndexReader reader = null;
		try{
			long start = new Date().getTime();
			reader = getReader();
			Searcher searcher = new IndexSearcher(reader);
			
			
			QueryParser parser = new QueryParser(field, analyzer);
			Query query = parser.parse(line);
			LogManager.debug("Searching for: " + query.toString(field));
			Hits hits = searcher.search(query);
			//Explanation expl = new Explanation();
			
			for(int i=0; i<hits.length(); i++){
				//expl = searcher.explain(query, hits.id(i));
				//LogManager.debug(" expl : "+ doc.get("expl")); 
				Document doc = hits.doc(i);
				list.add(doc.get(field));
			}

			long end = new Date().getTime();
			
			LogManager.debug(hits.length() + " total matching documents");
			
			end = end - start;
			String logMessage = " Searching '"+ line + "' Using Index took "+end+" milliseconds.";
			printIndexLog(logMessage , end);
						
		} catch(Exception e){
			if(e instanceof TooManyClauses){
				LogManager.error(" Keyword의 검색어가 충분하지 않습니다. : " + e);
				throw new LogSearchException(" Keyword의 검색어가 충분하지 않습니다. ");
			}
			if(e instanceof ParseException){
				LogManager.error(" 검색어가 올바르지 않습니다. : " + e);
				throw new LogSearchException(" 검색어가 올바르지 않습니다. ");
			}
			if(e instanceof FileNotFoundException){
				LogManager.error(" Index파일을 찾을 수 없습니다. : " + e);
				throw new LogSearchException(" Index파일을 찾을 수 없습니다. ");
			}
			e.printStackTrace();
			LogManager.error(" 검색 도중 에러가 발생하였습니다. : " + e);
			throw new LogSearchException(" 검색 도중 에러가 발생하였습니다. ");
			
		}finally{
			if(reader != null){
				try{
				reader.close();
				} catch(IOException e){
					reader.close();
				} 
			}
		}
		
		return list;

	}
	
	public ArrayList search(DataMap dataMap) throws Exception{
		LogManager.debug("### search(DataMap dataMap) called..  " );
		return search(dataMap.getString(this.searchLine));
	}
	
	
	/**
	 * 검색 소요 시간이 일정시간(overIndexingMills)을 초과하면 
	 * info Level로 로그를 남기고 , 
	 * 초과하지 않은 경우엔 debug Level로 로그를 남긴다.  
	 * @param logMessage
	 * @param indexMills
	 */
	public void printIndexLog(String logMessage, long searchMills){
		
		if(searchMills >= overSearchingMills ){
			LogManager.info(LogProcessorContext.logCategory, "[검색 수행시간 초과] ###" + logMessage );
		} else {
			LogManager.debug(logMessage);
		}
		
	}
	
	
	public static void main(String[] args) throws Exception {
		
		SearchManager sm = SearchManager.getInstance();
		ArrayList list = sm.search("ABCDEFG0");
		sm.printSearchResult(list);
	}
	

}
