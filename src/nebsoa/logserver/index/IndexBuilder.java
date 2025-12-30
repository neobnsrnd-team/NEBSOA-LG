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

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.FSDirectory;

import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.FormatUtil;
import nebsoa.common.util.PropertyManager;

/*******************************************************************
 * <pre>
 * 1.설명 
 * lucene을 이용하여 Index 파일을 생성하는 클래스 
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
 * $Log: IndexBuilder.java,v $
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
 * Revision 1.1  2007/06/15 02:05:07  김승희
 * 프로젝트 변경 신규 커밋
 *
 * Revision 1.2  2007/06/13 09:46:34  신경학
 * 에러로그 남기는 로직
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
 * Revision 1.3  2007/06/12 05:29:12  신경학
 * 설정값 Property에서 읽어오도록 수정
 *
 * Revision 1.2  2007/06/11 08:55:55  신경학
 * Exception 수정
 *
 * Revision 1.1  2007/06/11 08:24:25  shkim
 * 패키지 변경
 *
 * Revision 1.4  2007/06/07 13:01:17  신경학
 * 주석 처리
 *
 * Revision 1.3  2007/06/07 13:00:46  신경학
 * 최초 등록
 *
 * </pre>
 ******************************************************************/

public class IndexBuilder {
	private static Object dummy = new Object();
	private static IndexBuilder instance;
	private static IndexWriter writer = null;
	public static String FieldName = PropertyManager.getProperty("logserver","FIELD_NAME");
	private static String defaultDir = PropertyManager.getProperty("logserver","MESSAGE_LOG_INDEX_DIRECTORY");
	
	//Indexing 속도에 영향을 주는 옵션 
	private static int DEFAULT_MERGE_FACTOR = PropertyManager.getIntProperty("logserver","DEFAULT_MERGE_FACTOR"  );
	private static int DEFAULT_MERGE_DOCS = PropertyManager.getIntProperty("logserver","DEFAULT_MERGE_DOCS", Integer.MAX_VALUE);
	//private static int DEFAULT_BUFFERED_DOCS = PropertyManager.getIntProperty("logserver","DEFAULT_BUFFERED_DOCS");
	
	//Optimize 주기 설정 
	private static int MIN_OPTIMIZE_CNT = PropertyManager.getIntProperty("logserver","MIN_OPTIMIZE_CNT");
	private static int optimizeCnt = 0;
	
	//RAMDirectory사용시 설정 값
	private static int COUNT_RAM_DOCX = 0;
	private static int MAX_BUFFER_DOCS=10000;
	
	
	public static IndexBuilder getInstance(){
		if(instance == null){
			synchronized(dummy){
				instance = new IndexBuilder(); 
			}
		}
		return instance;
	}
	
	private IndexWriter setOption(IndexWriter writer){
		writer.setMergeFactor(DEFAULT_MERGE_FACTOR); //메모리에 저장할 Document의 개수와 여러 개의 색인 내 세그먼트 병합 빈도수 조절 = 색인 시 사용되는 램의 양을 조절 ?
		writer.setMaxMergeDocs(DEFAULT_MERGE_DOCS); //세그먼트당 문서의 최대 갯수 -> merge결정 
		//writer.setMaxBufferedDocs(DEFAULT_BUFFERED_DOCS);//세그먼트 생성 전까지 버퍼링 하는 Doc의 수 
		
		return writer;
	}
	
	private IndexWriter getWriter() throws IOException{
		String indexDir = defaultDir + FormatUtil.getToday();
		FSDirectory directory = null;
		
		synchronized(this){
			directory = FSDirectory.getDirectory(indexDir);
		}
		
		if(directory.fileExists("segments.gen")){
			//LogManager.debug("Index Directory가 이미 존재합니다. ");
			directory.clearLock(IndexWriter.WRITE_LOCK_NAME);
			writer = new IndexWriter(directory, new StandardAnalyzer(), false);
		} else {
			//LogManager.debug("Index Directory를 생성합니다. ");
			writer = new IndexWriter(directory, new StandardAnalyzer(), true);
		}
		
		setOption(writer);
		return writer;
	}
	
	public Document addDocument(Document doc, String logString){
		
		doc.add(new Field("messageLog",logString, Field.Store.YES, Field.Index.TOKENIZED));
		
		return doc;
		
	}
	
	public void printIndexingOption(){
		LogManager.debug(" IndexFile optimizing executed approximately every "+ MIN_OPTIMIZE_CNT + "documents.");
		LogManager.debug(" Indexing MERGE_FACTOR : "+ DEFAULT_MERGE_FACTOR);
		LogManager.debug(" Indexing MERGE_DOCS : "+ DEFAULT_MERGE_DOCS);
		//LogManager.debug(" Indexing BUFFERED_DOCS : "+ DEFAULT_BUFFERED_DOCS);
	}
	
	/**
	 * Object배열을 받아서 Object단위로 Index를 생성 한다. 
	 * 대략 MIN_OPTIMIZE_CNT 에 설정된 값만큼의 Document가 추가로 Indexing 되었을 때
	 * 색인 최적화(Optimize)를 수행한다. 
	 * @param originalLog
	 * @return
	 * @throws Exception
	 */
	public int build(Object[] originalLog) throws Exception {
		int docCount = 0;
		long start = System.currentTimeMillis();
		try{		
			getWriter();
			int size = originalLog.length;	
			for(int i=0; i<size; i++){
				if(originalLog[i]==null){
					continue;
				}
				String logString = (String)originalLog[i];
				Document doc = new Document();
				addDocument(doc, logString);
				writer.addDocument(doc);
			}
			
			//MIN_OPTIMIZE_CNT 건당 Optimize를 실행한다. 
			optimizeCnt+=size;
			if(optimizeCnt >= MIN_OPTIMIZE_CNT){
				long optStart = System.currentTimeMillis();
				writer.optimize();
				long optEnd = System.currentTimeMillis();
				LogManager.debug(" Execute Optimizing By Added "+optimizeCnt+" documents took "+ (optEnd - optStart) + " milliseconds.");
				optimizeCnt = 0;
			}
			
			docCount = writer.docCount();
			
		} catch(Exception e){
			LogManager.error("Indexing 도중 에러가 발생 했습니다. : " + e);
			throw new SysException("Indexing 도중 에러가 발생 했습니다. : "+ e.getMessage());	
		} finally{
			if(writer != null){
				try{
					writer.close();
				} catch(IOException e){
					
				}	
			}
		}
		
		long end = System.currentTimeMillis();
		end = end - start;
		LogManager.debug(" Indexing "+docCount+" documents took "+(end)+" milliseconds.");
		
		return docCount;

	}
	
	public static void main(String[] args) throws Exception{
		
		int indexCnt = 0;
		int totalIndexDocs = 10;
		int size = 1000;
		String testLog = "20061204|130400|CA1|20061204130400CA112074209281|60504015|Send|IB|6400|B62|login|||8409242017728||SCHLOAN01||0000000000H2        00000006340000000367SP2P  20061204        IB01    2006120413040014860504015   IB00010000                  172.16.1.146   00000000  1200612042006120413040014                AN1D          00000000000000000000000000000000000Y00000130400210030B2006                       IB6400B62       000000000001000000000000000108003001I6000                00000000                                                                                            8409242017728 SCHLOAN01 006011113333                           0                                    8409242017728                                ********************************                |";
		String testMessage = "|20061204130400CA112074209281|60504015|Send|IB|6400|B62|login|||8409242017728||SCHLOAN01||0000000000H2        00000006340000000367SP2P  20061204        IB01    2006120413040014860504015   IB00010000                  172.16.1.146   00000000  1200612042006120413040014                AN1D          00000000000000000000000000000000000Y00000130400210030B2006                       IB6400B62       000000000001000000000000000108003001I6000                00000000                                                                                            8409242017728 SCHLOAN01 006011113333                           0                                    8409242017728                                ********************************                |";
		String tempTestMessage = "";
		try{
			long start = System.currentTimeMillis();
			
			for(int i=0; i<totalIndexDocs; i++){
				Object[] originalLog = new Object[size];
				for(int j=0; j<size; j++){
					tempTestMessage = "20061204|130400|YY" + (i*j + j) + testMessage;
					originalLog[j] = tempTestMessage;
				}
				indexCnt = IndexBuilder.getInstance().build(originalLog);
				//indexCnt = RAMlucene(testLogArry, fileNameArry);
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
