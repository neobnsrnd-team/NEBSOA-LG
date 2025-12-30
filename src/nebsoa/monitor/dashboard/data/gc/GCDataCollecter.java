/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.monitor.dashboard.data.gc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import nebsoa.common.log.LogManager;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 메모리 GC정보를 수집하는 클래스를 구현할 때 상속받을 상위 클래스.
 * 전체적인 로직의 흐름을 정의한 template이 된다.
 *  
 * 
 * 2.사용법
 * 수집하는 데이터는 WAS_ID, 발생시간, 전체 메모리, FREE된 메모리, 메모리 회수율 이다. 
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author 이종원
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 
 * $Log: GCDataCollecter.java,v $
 * Revision 1.1  2018/01/15 03:39:51  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:06  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:25  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:28  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/03/04 01:57:31  이종원
 * 최초작성
 *
 * </pre>
 ******************************************************************/
public abstract class GCDataCollecter implements Runnable{
	protected String wasInstanceId;
	protected int refreshCount=0;
	protected String filePath;
	BufferedReader reader;
	
	public GCDataCollecter(String wasInstanceId,String filePath){
		this.wasInstanceId = wasInstanceId;
		this.filePath = filePath;
	}
	
	public void run(){
		//stream을 오픈하고 현재 쓰여진 최종 위치로 이동한다.
		openFileReader();
		//지금부터 읽으면 된다.
		while(reader != null ){
			String line = null;
			try {
				 line = reader.readLine();
				 if(line != null) {
					 LogManager.debug("ReadLine:"+line);
					 parseLine(line);
				 } else {
					 try {
						 Thread.sleep(10*1000);
					 } catch (InterruptedException e) {
						 // TODO Auto-generated catch block
						 e.printStackTrace();
					 }
					 LogManager.debug("line is null");
				 }
			} catch (IOException e) {				
				e.printStackTrace();
				refresh();
			}
			
		}
	}

	/**
	 * Was장비 별로 구현 할 메소드
	 * @param line
	 */
	public abstract void parseLine(String line) ;
	
	
	private void refresh() {
		if(reader != null){
			try {
				reader.close();
				reader=null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//1분간 쉰다.
		try {
			Thread.sleep(1*60*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		LogManager.debug("Refresh Count : "+ ++refreshCount );
		openFileReader();
	}


	public void openFileReader(){
		File file = new File(filePath);
		if(!file.exists()){
			System.err.println("file not exist.."+file.toString());
			reader=null;
			return ;
		}
		
		if(file.isDirectory()){
			System.err.println("file is Directory.. cann't read....."+file.toString());
			reader=null;
			return ;
		}
		
		if(!file.canRead()){
			System.err.println("file is not readable... check permitting.."+file.toString());
			reader=null;
			return ;
		}
		
		try {
			reader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			System.err.println("file not exist.."+file.toString());
			reader=null;
			return ;
		}
		LogManager.debug("Length of file is "+file.length());
		// go to the end of file
		try {
			reader.skip(file.length());
		} catch (IOException e) {
			e.printStackTrace();
			if(reader != null)
				try {
					reader.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			reader = null;
			return ;
		}
		LogManager.debug("go to the end of file ok... ");

	}

}