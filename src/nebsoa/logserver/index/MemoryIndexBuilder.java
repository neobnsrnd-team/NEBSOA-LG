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

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.RAMDirectory;

import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.PropertyManager;
import nebsoa.logserver.LogProcessorContext;

/*******************************************************************
 * <pre>
 * 1.설명 
 * Memory 상에서 Indexing을 수행하는 클래스  
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
 * $Log: MemoryIndexBuilder.java,v $
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
 * Revision 1.11  2007/08/31 05:29:33  김승희
 * *** empty log message ***
 *
 * Revision 1.10  2007/08/30 16:50:05  shkim
 * *** empty log message ***
 *
 * Revision 1.9  2007/08/30 11:23:27  김승희
 * *** empty log message ***
 *
 * Revision 1.7  2007/08/27 10:39:19  김승희
 * *** empty log message ***
 *
 * Revision 1.6  2007/08/24 07:57:12  김승희
 * 주석 수정
 *
 * Revision 1.5  2007/08/24 07:00:51  김승희
 * *** empty log message ***
 *
 * Revision 1.4  2007/08/21 06:01:01  신경학
 * lastFlushTime 추가
 *
 * Revision 1.3  2007/08/21 05:29:04  신경학
 * 수정
 *
 * Revision 1.2  2007/08/21 05:16:38  신경학
 * 수정
 *
 * Revision 1.1  2007/08/21 05:12:54  신경학
 * 최초 등록
 *
 *
 * </pre>
 ******************************************************************/

public class MemoryIndexBuilder {
	
	/* Indexing 속도에 영향을 주는 옵션 */
	private static int DEFAULT_MERGE_FACTOR = PropertyManager.getIntProperty("logserver","DEFAULT_MERGE_FACTOR"  );
	private static int DEFAULT_MERGE_DOCS = PropertyManager.getIntProperty("logserver","DEFAULT_MERGE_DOCS", Integer.MAX_VALUE);
	
	
	/* RAMDirectory 설정 값 */
	private int COUNT_RAM_DOCX = 0;
	private static int MIN_BUILD_RAM_DOCX = 3999; // 설정값 이상의 Document를 Memory상에서 Indexing 하면 IndexBuilder 호출 
	//private static int MAX_BUFFER_DOCS=3999;
	
	private RAMDirectory ramDir;
	private IndexWriter ramWriter = null;
	
	private int MAX_RAM_SIZE_IN_BYTES = 11864640; //최대 메모리.
	private static Analyzer analyzer;
	
	private int index;
	private boolean isRunning;
	private boolean isStop;
	private long lastFlushTime = System.currentTimeMillis(); 
	
	private static long overIndexingMills = 1 * 60 * 1000;  // indexing 시간이 overIndexingMills 를 초과할 경우에는 info Level로 로그를 남긴다.
	
	private String name;
	
	private boolean isOnFlush = false;
	/**
	 * 
	 * @param writer
	 * @return
	 */
	private IndexWriter setOption(IndexWriter writer){
		writer.setMergeFactor(DEFAULT_MERGE_FACTOR); //메모리에 저장할 Document의 개수와 여러 개의 색인 내 세그먼트 병합 빈도수 조절 = 색인 시 사용되는 램의 양을 조절 ?
		writer.setMaxMergeDocs(DEFAULT_MERGE_DOCS); //세그먼트당 문서의 최대 갯수 -> merge결정 
		//writer.setMaxBufferedDocs(MAX_BUFFER_DOCS); //MAX Documents 수 제어 -> RAM Writer 에서 의미가 있나? 
		return writer;
	}
	
	
	public MemoryIndexBuilder(int index) throws Exception{
		this.index = index;
		this.name = "MemoryIndexBuilder[" + index + "]";
		init();
	}
	
	private void init() throws Exception{
		
		setMaxRamSize(); 
		ramDir = new RAMDirectory();
		
		analyzer = AnalyzerFactory.getInstance().getAnalyzer();
		
		try{
			ramWriter = new IndexWriter(ramDir, analyzer, true);
		} catch (IOException e){
			throw new SysException("RAM Writer 생성에 실패하였습니다. [ " + e.getMessage()+" ]" );
		}
		
		setOption(ramWriter);
	}


	private void setMaxRamSize() {
		this.MAX_RAM_SIZE_IN_BYTES = ((((index+2) % 2)+1) * 512 * 1024) + (2 * 1024 * 1024);
	}
	
	private Document addDocument(Document doc, String logString) throws Exception{
		
		// OUT OF MEMORY Exception 방지 
		if(getRamDirectorySizeInBytes() > MAX_RAM_SIZE_IN_BYTES){
			LogManager.debug(getName()+" ******* OUT OF MEMORY FLUSH CALLED .   " );
			LogManager.debug(getName()+" ******* ramDirectory size In Bytes / MAX_RAM_SIZE_IN_BYTES  " + getRamDirectorySizeInBytes() + " / " + MAX_RAM_SIZE_IN_BYTES );
			flush();
		}
		
		doc.add(new Field("messageLog",logString, Field.Store.YES, Field.Index.TOKENIZED));
		COUNT_RAM_DOCX++;
		
		return doc;
		
	}
	
	/**
	 * Memory에서 Indexing한 정보를 File에 쓰도록 BufferedIndexBuilder를 호출한다. 
	 * MAX_RAM_SIZE_IN_BYTES으로 설정된 값 이상의 Memory를 차지하고 있을 때,
	 * 일정 시간 동안 flush가 일어나지 않았을 때 호출된다. 
	 * @throws Exception
	 */
	 synchronized void flush() throws Exception{
		if(isOnFlush()) return;
		setOnFlush(true);
		lastFlushTime = System.currentTimeMillis();
		try{
			if(COUNT_RAM_DOCX>0){
				LogManager.debug(Thread.currentThread().getName() +":" + getName()+" flush() Called . ram doc count: " +  COUNT_RAM_DOCX );
				ramWriter.flush();
				
				FileIndexBuilder.getInstance().build(ramDir);
				COUNT_RAM_DOCX = 0;
				
				ramWriter.close();
			
				try{
					ramWriter = new IndexWriter(ramDir, analyzer , true);
					
				} catch(IOException e){
					LogManager.error(LogProcessorContext.logCategory, e);
					throw new SysException(getName()+"RAM Writer 생성에 실패하였습니다. [ " + e.getMessage()+" ]" );
				}
				
				LogManager.debug(getName()+" :: RAMIndexWriter Closed .  :: ");
			}
		}finally{
			setOnFlush(false);
		}
	}
	
	public boolean isOnFlush() {
		return isOnFlush;
	}


	public void setOnFlush(boolean isOnFlush) {
		this.isOnFlush = isOnFlush;
	}


	/**
	 * Indexing할 로그 Object배열을 받아 
	 * Memory상에서 Indexing 작업을 수행한다. 
	 * @param originalLog
	 * @param index
	 * @return
	 * @throws Exception
	 */
	public synchronized int build(Object[] originalLog) throws Exception {
		
		if(!isStop){
			isRunning = true;
			
			LogManager.debug(getName()+" : ramBuilder Start . "  );
			
			int docCount = 0;
			long start = System.currentTimeMillis();
			int size;
		
			try{
				size = originalLog.length;	
				
				LogManager.debug(getName()+" @@@@@@@ Adding Documents Start .  Ram Size = "+ ramDir.sizeInBytes()  );
				for(int i=0; i<size; i++){
					if(originalLog[i]==null){
						continue;
					}
					String logString = (String)originalLog[i];
					Document doc = new Document();
					doc = addDocument(doc, logString);
					ramWriter.addDocument(doc);
					
				}
				LogManager.debug(getName()+" @@@@@@@ Adding Documents End .  Ram Size = "+ ramDir.sizeInBytes()  );
				
				/*//MIN_BUILD_RAM_DOCX건 이상 RAM 상에 Indexing되어 있을 때 File에 쓴다. 
				if(COUNT_RAM_DOCX > MIN_BUILD_RAM_DOCX){
					flush();
					System.out.println("MemoryIndexBuilder["+index+"] @@@@@@@ Flush End .  Ram Size = "+ ramDir.sizeInBytes()  );
				}
				else {
					System.out.println("MemoryIndexBuilder["+index+"] @@@@@@@ BufferedIndexBuilder NOT Called . : " + COUNT_RAM_DOCX  );
				}*/
				
				docCount = ramWriter.docCount();
				
				long end = System.currentTimeMillis();
				end = end - start;
				
				String logString = getName()+" : Indexing "+ size + " documents in Memory took "+end+" milliseconds.";
				printIndexLog(logString, end);
				LogManager.debug(getName()+" @@@@@@@ Current Doc Count In RAMWriter is " +  docCount + " . " );
				
				return docCount;
				
			} catch(Exception e){
				StackTraceElement[] s = e.getStackTrace();
				for(int i=0; i<s.length; i++){
					LogManager.error(LogProcessorContext.logCategory, s[i].toString() +"..." + s[i].getLineNumber());
				}
				LogManager.error(LogProcessorContext.logCategory, getName() + "Ram Indexing 도중 에러가 발생 했습니다. : " + e);
				LogManager.error(LogProcessorContext.logCategory, e);
				throw new SysException("Ram Indexing 도중 에러가 발생 했습니다. : "+ e.getMessage());	
			} finally{
				
				isRunning = false;
			}	
			
		} else {
			return 0;
		}

	}

	public void destroy() throws Exception{
		
		flushMemoryByStatus();
		
		if(ramWriter != null){
			try{
				ramWriter.close();
			} catch(IOException e){
			} 
		}
		
		
	}
	
	
	public void flushMemoryByStatus() throws Exception {
		
		synchronized(this){
			while(isRunning && !isStop){
				try{
					wait(2000);
				} catch (InterruptedException e){
				}
			}
			
			isStop = true;
		}
		
		if(COUNT_RAM_DOCX > 0){
			flush();
		}
		
	}
	
	/**
	 * 현재 RAMDirectory의 Memory 사용량을 Byte단위로 가져옴 
	 * @return
	 */
	public long getRamDirectorySizeInBytes() {
		return ramDir.sizeInBytes();
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
			LogManager.info(LogProcessorContext.logCategory, getName() +":[인덱싱 수행시간 초과 - Memory] ###" +logMessage );
		} else {
			LogManager.debug(logMessage);
		}
		
	}

	/**
	 * 마지막으로 파일에 쓰기를 시도한 시간. 
	 * @return
	 */
	public long getLastFlushTime() {
		return lastFlushTime;
	}
	
	public String getName(){
		return name;
	}
}
