/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
package nebsoa.logserver.index;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;

import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.FormatUtil;
import nebsoa.common.util.PropertyManager;
import nebsoa.logserver.LogProcessorContext;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 메모리상의 Indexing 정보를 받아 File로 남기는 로직을 수행하는 클래스 
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
 * $Log: FileIndexBuilder.java,v $
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
 * Revision 1.2  2007/12/06 04:51:09  shkim
 * 요일별/날짜별 폴더에 인덱스 파일을 생성하도록 수정
 * 다른 날짜의 폴더는 삭제한다.
 *
 * Revision 1.1  2007/09/21 08:22:30  김승희
 * 최초 등록
 *
 * Revision 1.8  2007/08/30 16:50:05  shkim
 * *** empty log message ***
 *
 * Revision 1.7  2007/08/27 10:39:19  김승희
 * *** empty log message ***
 *
 * Revision 1.6  2007/08/23 09:12:18  신경학
 * 로그 수정
 *
 * Revision 1.5  2007/08/21 06:01:55  신경학
 * 수정
 *
 * Revision 1.4  2007/08/21 05:29:04  신경학
 * 수정
 *
 * Revision 1.3  2007/08/21 05:16:12  신경학
 * *** empty log message ***
 *
 * Revision 1.2  2007/08/21 05:14:34  신경학
 * 수정
 *
 * Revision 1.1  2007/08/21 05:14:00  신경학
 * 최초 등록
 *
 *
 * </pre>
 ******************************************************************/

public class FileIndexBuilder {
	
	private static Object dummy = new Object();
	private static FileIndexBuilder instance;
	private IndexWriter writer = null;
	public  static String FieldName = PropertyManager.getProperty("logserver","FIELD_NAME");
	private static String defaultDir = PropertyManager.getProperty("logserver","MESSAGE_LOG_INDEX_DIRECTORY");
	private static String indexDir = "";
	
	//Indexing 속도에 영향을 주는 옵션 
	private static int DEFAULT_MERGE_FACTOR = PropertyManager.getIntProperty("logserver","DEFAULT_MERGE_FACTOR"  );
	private static int DEFAULT_MERGE_DOCS = PropertyManager.getIntProperty("logserver","DEFAULT_MERGE_DOCS", Integer.MAX_VALUE);
	//private static int DEFAULT_BUFFERED_DOCS = PropertyManager.getIntProperty("logserver","DEFAULT_BUFFERED_DOCS");
	
	//Optimize 주기 설정 
	private static int MIN_OPTIMIZE_CNT = PropertyManager.getIntProperty("logserver","MIN_OPTIMIZE_CNT");
	private static int optimizeCnt = 0;
	
	private static boolean isRunning = false;
	private Analyzer analyzer;
	
	private static long overIndexingMills = 5 * 60 * 1000;  // indexing 시간이 overIndexingMills 를 초과할 경우에는 info Level로 로그를 남긴다.
	
	public static FileIndexBuilder getInstance(){
		if(instance == null){
			synchronized(dummy){
				if(instance == null) instance = new FileIndexBuilder(); 
			}
		}
		return instance;
	}
	
	public FileIndexBuilder(){
		analyzer = AnalyzerFactory.getInstance().getAnalyzer();
	}
	
	
	private IndexWriter setOption(){
		writer.setMergeFactor(DEFAULT_MERGE_FACTOR); //메모리에 저장할 Document의 개수와 여러 개의 색인 내 세그먼트 병합 빈도수 조절 = 색인 시 사용되는 램의 양을 조절 ?
		writer.setMaxMergeDocs(DEFAULT_MERGE_DOCS); //세그먼트당 문서의 최대 갯수 -> merge결정 
		//writer.setMaxBufferedDocs(DEFAULT_BUFFERED_DOCS);//세그먼트 생성 전까지 버퍼링 하는 Doc의 수 
		
		return writer;
	}
	
	
	private void createWriter() throws IOException{
		//오늘 요일
		int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
		//오늘일자(yyyyMMdd)
		String today = FormatUtil.getToday();
		
		FSDirectory directory = null;
		
		//요일별 폴더 안에 다시 일자별 폴더 생성
		indexDir = defaultDir + dayOfWeek + File.separator + today;
		
		//요일별 폴더 안의 다른 일자 폴더를 삭제한다.
		deleteDirectory(dayOfWeek, today);
		
		synchronized(this){
			directory = FSDirectory.getDirectory(indexDir);
		}
		
		if(directory.fileExists("segments.gen")){
			//LogManager.debug("Index Directory가 이미 존재합니다. ");
			directory.clearLock(IndexWriter.WRITE_LOCK_NAME);
			writer = new IndexWriter(directory, analyzer , false);
		} else {
			//LogManager.debug("Index Directory를 생성합니다. ");
			writer = new IndexWriter(directory, analyzer , true);
		}
		
	}

	/**
	 * 현재 요일 폴더에서 오늘 일자가 아닌 폴더를 전부 삭제한다.
	 * @param dayOfWeek 현재 요일
	 * @param today 오늘 일자
	 */
	private void deleteDirectory(int dayOfWeek, String today) {
		long startTime = System.currentTimeMillis();
		File dir = new File(defaultDir + dayOfWeek);
		if(dir.exists()){
			File[] subDir = dir.listFiles();
			if(subDir!=null){
				for(int i=0; i<subDir.length; i++){
					//오늘 날짜 이름이 아닌 폴더 안의 모든 파일을 삭제한다.
					LogManager.debug(LogProcessorContext.logCategory, "### subDir[" + i +"].getName():" + subDir[i].getName());
					if(!subDir[i].getName().equals(today)){
						//인덱스 파일 삭제
						File[] subFile = subDir[i].listFiles();
						if(subFile!=null){
							for(int j=0; j<subFile.length; j++){
								subFile[j].delete();
							}
						}
						//폴더 삭제
						subDir[i].delete();
					}
				}
			}
		}
		
		LogManager.info(LogProcessorContext.logCategory, "인덱스 로그 디렉토리 삭제 소요 시간:" + (System.currentTimeMillis()-startTime));
	}
	
	private void getWriter() throws IOException{
		
		//최초 생성 
		if("".equals(indexDir)){
			LogManager.debug(LogProcessorContext.logCategory, " 최초 IndexWriter 를 생성합니다. ");
			createWriter();
			
		/*} else if(!FormatUtil.getToday().equals(indexDir.substring(defaultDir.length()))){
			//다른 날에 쌓이는 로그. writer 생성. 
			LogManager.debug(LogProcessorContext.logCategory, " 이전 일자의 IndexWriter 를 Close합니다.  ");
			if(writer!=null) writer.close();
			LogManager.debug(LogProcessorContext.logCategory, " 일별 File Directory 생성을 시작합니다. " + FormatUtil.getToday());
			createWriter();
			
		} */
		
		//요일별 폴더 안에 다시 일자별 폴더가 생성된다. (예) c:/index/log_2/20071205
		} else if(!FormatUtil.getToday().equals(indexDir.substring(indexDir.lastIndexOf(File.separator)+1))){
			//다른 날에 쌓이는 로그. writer 생성. 
			LogManager.debug(LogProcessorContext.logCategory, " 이전 일자의 IndexWriter 를 Close합니다.  ");
			if(writer!=null) writer.close();
			LogManager.info(LogProcessorContext.logCategory, " 일별 File Directory 생성을 시작합니다. " + FormatUtil.getToday());
			createWriter();
		}
			
		setOption();
	}
	
	public Document addDocument(Document doc, String logString){
		
		doc.add(new Field("messageLog",logString, Field.Store.YES, Field.Index.TOKENIZED));
		
		return doc;
		
	}
	
	public void printIndexingOption(){
		LogManager.debug(LogProcessorContext.logCategory, " IndexFile optimizing executed approximately every "+ MIN_OPTIMIZE_CNT + "documents.");
		LogManager.debug(LogProcessorContext.logCategory, " Indexing MERGE_FACTOR : "+ DEFAULT_MERGE_FACTOR);
		LogManager.debug(LogProcessorContext.logCategory, " Indexing MERGE_DOCS : "+ DEFAULT_MERGE_DOCS);
		//LogManager.debug(" Indexing BUFFERED_DOCS : "+ DEFAULT_BUFFERED_DOCS);
	}
	
	
	/**
	 * Index 정보를 가지고 있는 RAMDirectory 를 받아서 파일에 쓴다. 
	 * 대략 MIN_OPTIMIZE_CNT 에 설정된 값만큼의 Document가 추가로 Indexing 되었을 때
	 * 색인 최적화(Optimize)를 수행한다. 
	 * @param originalLog
	 * @return
	 * @throws Exception
	 */
	public synchronized int build(RAMDirectory ramDir) throws Exception {
		
		isRunning = true;
		
		int docCount = 0;
		long start = System.currentTimeMillis();
		
		try{
			getWriter();
			
			LogManager.debug(LogProcessorContext.logCategory, "####### Flush Buffer in RAM Directory to File . ramDir = "+ramDir);
			
			Directory[] ramDirs = {ramDir};
			writer.addIndexesNoOptimize(ramDirs);
			//writer.addIndexes(ramDirs); 
			
			//MIN_OPTIMIZE_CNT 건당 Optimize를 실행한다. 
			/*optimizeCnt+=size;
			if(optimizeCnt >= MIN_OPTIMIZE_CNT){
				long optStart = System.currentTimeMillis();
				writer.optimize();
				long optEnd = System.currentTimeMillis();
				LogManager.debug(" Execute Optimizing By Added "+optimizeCnt+" documents took "+ (optEnd - optStart) + " milliseconds.");
				optimizeCnt = 0;
			}*/
			
			docCount = writer.docCount();
			long end = System.currentTimeMillis();
			end = end - start;
			
			String logMessage = "####### Total Indexing "+docCount+" documents took "+(end)+" milliseconds.";
			printIndexLog(logMessage, end);
			
			return docCount;
			
		} catch(Exception e){
			LogManager.error(LogProcessorContext.logCategory, "Indexing 도중 에러가 발생 했습니다. : " + e);
			LogManager.error(LogProcessorContext.logCategory, e);
			throw new SysException("Indexing 도중 에러가 발생 했습니다. : "+ e.getMessage());	
		} finally{
			
			isRunning = false; 
		}
		
		

	}
	
	/**
	 * Indexing 작업중이면 작업이 완료될때까지 기다린다. - 2초간격 
	 * 모두 처리 후 종료 
	 * @throws IOException
	 */
	public void destroy() throws IOException{
		
		LogManager.debug(LogProcessorContext.logCategory, " ############# FileIndexingBuilder Destroy Called " );
		
	    synchronized(this){
    		while(isRunning){
    			try { 
    				LogManager.debug(LogProcessorContext.logCategory, " ############# Waiting For FileIndexing Finished.. ");
    				wait(2000);
    			} catch(InterruptedException e){
    			}
    		}
	    	
	    }
	    
		if(writer != null){
			writer.close();
		}
		
		LogManager.debug(LogProcessorContext.logCategory, " ############# FileIndexingBuilder End "  );
		
	}
	
	
	/**
	 * Indexin수행 시간이 일정시간(overIndexingMills)을 초과하면 
	 * info Level로 로그를 남기고 , 
	 * 초과하지 않은 경우엔 debug Level로 로그를 남긴다.  
	 * @param logMessage
	 * @param indexMills
	 */
	public void printIndexLog(String logMessage, long indexMills){
		
		if(indexMills >= overIndexingMills ){
			LogManager.info(LogProcessorContext.logCategory, "[인덱싱 수행시간 초과 - File]"+ Thread.currentThread().getName() +":" + logMessage );
		} else {
			LogManager.debug(LogProcessorContext.logCategory, Thread.currentThread().getName() +":"+logMessage);
		}
		
	}
	
	public static void main(String[] args) throws Exception{
		
		indexDir = PropertyManager.getProperty("logserver","MESSAGE_LOG_INDEX_DIRECTORY")+"4"+File.separator+"20071212";
		String s = indexDir.substring(indexDir.lastIndexOf(File.separator));
		System.out.println(s);
		
		if(true) return;
		int indexCnt = 0;
		int totalIndexDocs = 1;
		int size = 1000;
		String testLog = "20061204|130400|CA1|20061204130400CA112074209281|60504015|Send|IB|6400|B62|login|||8409242017728||SCHLOAN01||0000000000H2        00000006340000000367SP2P  20061204        IB01    2006120413040014860504015   IB00010000                  172.16.1.146   00000000  1200612042006120413040014                AN1D          00000000000000000000000000000000000Y00000130400210030B2006                       IB6400B62       000000000001000000000000000108003001I6000                00000000                                                                                            8409242017728 SCHLOAN01 006011113333                           0                                    8409242017728                                ********************************                |";
		String testMessage = "|20061204130400CA112074209281|60504015|Send|IB|6400|B62|login|||8409242017728||SCHLOAN01||0000000000H2        00000006340000000367SP2P  20061204        IB01    2006120413040014860504015   IB00010000                  172.16.1.146   00000000  1200612042006120413040014                AN1D          00000000000000000000000000000000000Y00000130400210030B2006                       IB6400B62       000000000001000000000000000108003001I6000                00000000                                                                                            8409242017728 SCHLOAN01 006011113333                           0                                    8409242017728                                ********************************                |";
		String longLog = "0000000000H2        00000006340000000367SP2P  20061204        IB01    2006120413040014860504015   IB00010000                  172.16.1.146   00000000  1200612042006120413040014                AN1D          00000000000000000000000000000000000Y00000130400210030B2006                       IB6400B62       000000000001000000000000000108003001I6000                00000000                                                                                            8409242017728 SCHLOAN01 006011113333                           0                                    8409242017728                                ********************************                0000000000H2        00000006340000000367SP2P  20061204        IB01    2006120413040014860504015   IB00010000                  172.16.1.146   00000000  1200612042006120413040014                AN1D          00000000000000000000000000000000000Y00000130400210030B2006                       IB6400B62       000000000001000000000000000108003001I6000                00000000                                                                                            8409242017728 SCHLOAN01 006011113333                           0                                    8409242017728                                ********************************                0000000000H2        00000006340000000367SP2P  20061204        IB01    2006120413040014860504015   IB00010000                  172.16.1.146   00000000  1200612042006120413040014                AN1D          00000000000000000000000000000000000Y00000130400210030B2006                       IB6400B62       000000000001000000000000000108003001I6000                00000000                                                                                            8409242017728 SCHLOAN01 006011113333                           0                                    8409242017728                                ********************************                0000000000H2        00000006340000000367SP2P  20061204        IB01    2006120413040014860504015   IB00010000                  172.16.1.146   00000000  1200612042006120413040014                AN1D          00000000000000000000000000000000000Y00000130400210030B2006                       IB6400B62       000000000001000000000000000108003001I6000                00000000                                                                                            8409242017728 SCHLOAN01 006011113333                           0                                    8409242017728                                ********************************                0000000000H2        00000006340000000367SP2P  20061204        IB01    2006120413040014860504015   IB00010000                  172.16.1.146   00000000  1200612042006120413040014                AN1D          00000000000000000000000000000000000Y00000130400210030B2006                       IB6400B62       000000000001000000000000000108003001I6000                00000000                                                                                            8409242017728 SCHLOAN01 006011113333                           0                                    8409242017728                                ********************************                0000000000H2        00000006340000000367SP2P  20061204        IB01    2006120413040014860504015   IB00010000                  172.16.1.146   00000000  1200612042006120413040014                AN1D          00000000000000000000000000000000000Y00000130400210030B2006                       IB6400B62       000000000001000000000000000108003001I6000                00000000                                                                                            8409242017728 SCHLOAN01 006011113333                           0                                    8409242017728                                ********************************                0000000000H2        00000006340000000367SP2P  20061204        IB01    2006120413040014860504015   IB00010000                  172.16.1.146   00000000  1200612042006120413040014                AN1D          00000000000000000000000000000000000Y00000130400210030B2006                       IB6400B62       000000000001000000000000000108003001I6000                00000000                                                                                            8409242017728 SCHLOAN01 006011113333                           0                                    8409242017728                                ********************************                0000000000H2        00000006340000000367SP2P  20061204        IB01    2006120413040014860504015   IB00010000                  172.16.1.146   00000000  1200612042006120413040014                AN1D          00000000000000000000000000000000000Y00000130400210030B2006                       IB6400B62       000000000001000000000000000108003001I6000                00000000                                                                                            8409242017728 SCHLOAN01 006011113333                           0                                    8409242017728                                ********************************                0000000000H2        00000006340000000367SP2P  20061204        IB01    2006120413040014860504015   IB00010000                  172.16.1.146   00000000  1200612042006120413040014                AN1D          00000000000000000000000000000000000Y00000130400210030B2006                       IB6400B62       000000000001000000000000000108003001I6000                00000000                                                                                            8409242017728 SCHLOAN01 006011113333                           0                                    8409242017728                                ********************************                0000000000H2        00000006340000000367SP2P  20061204        IB01    2006120413040014860504015   IB00010000                  172.16.1.146   00000000  1200612042006120413040014                AN1D          00000000000000000000000000000000000Y00000130400210030B2006                       IB6400B62       000000000001000000000000000108003001I6000                00000000                                                                                            8409242017728 SCHLOAN01 006011113333                           0                                    8409242017728                                ********************************                0000000000H2        00000006340000000367SP2P  20061204        IB01    2006120413040014860504015   IB00010000                  172.16.1.146   00000000  1200612042006120413040014                AN1D          00000000000000000000000000000000000Y00000130400210030B2006                       IB6400B62       000000000001000000000000000108003001I6000                00000000                                                                                            8409242017728 SCHLOAN01 006011113333                           0                                    8409242017728                                ********************************                |";
		String tempTestMessage = "";
		try{
			long start = System.currentTimeMillis();
			
			/*TestDestroy testDestroy = new TestDestroy();
			Thread t1 = new Thread(testDestroy);
			
			t1.start();*/
			
			for(int i=0; i<totalIndexDocs; i++){
				Object[] originalLog = new Object[size];
				for(int j=0; j<size; j++){
					tempTestMessage = "20061204|130400|YY" + (i*j + j) + testMessage + longLog;
					originalLog[j] = tempTestMessage;
				}
				//indexCnt = FileIndexBuilder.getInstance().build(originalLog);
				//indexCnt = IndexBuilder.getInstance().RAMlucene(originalLog);
			}
			
			
			
			long end = System.currentTimeMillis();
			
			LogManager.debug(" MIN_OPTIMIZE_CNT : "+ MIN_OPTIMIZE_CNT);
			LogManager.debug(" Indexing MERGE_FACTOR : "+ DEFAULT_MERGE_FACTOR);
			LogManager.debug(" Indexing MERGE_DOCS : "+ DEFAULT_MERGE_DOCS);
			//LogManager.debug(" Indexing BUFFERED_DOCS : "+ DEFAULT_BUFFERED_DOCS);
			end = end - start;
			LogManager.debug(" Indexing Total "+indexCnt+" Files took "+(end)+" milliseconds.("+(end/1000/60)+"min.)");
			
		} catch(Exception e){
			e.printStackTrace();
		}	
	}
	
}









	