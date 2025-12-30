/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.file;

import java.io.File;
import java.util.Map;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 리스닝할 파일의 초기화 작업을 처리하는 클래스
 * 
 * 
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
 * $Log: Target.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:31  cvs
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
 * Revision 1.1  2008/08/04 08:54:52  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:27  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:26  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2006/06/19 04:10:16  안경아
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class Target {
	
	/**
	 * 하위 디렉토리 검사 여부
	 */
	private boolean recursive;
	
	private File targetFile;
	private Map fileList;
	
	private FileStatusChangeListener listener;
	
	private boolean file;
	
	/**
	 * 리스닝할 대상에 대한 초기화 작업을 수행합니다.
	 * 
	 * @param targetFile
	 * @param list
	 * @param listener
	 * @param recursive
	 */
	public Target(File targetFile, Map list, FileStatusChangeListener listener, boolean recursive) {
		this.targetFile = targetFile;
		this.fileList = list;
		this.listener = listener;
		this.file = targetFile.isFile();
		
		this.recursive = recursive;
	}//end of constructor

	/**
	 * targetFile 의 값을 리턴합니다.
	 * 
	 * @return targetFile 의 값
	 */
	public File getTargetFile() {
		return targetFile;
	}

	/**
	 * fileList 의 값을 리턴합니다.
	 * 
	 * @return fileList 의 값
	 */
	public Map getFileList() {
		return fileList;
	}

	/**
	 * listener 의 값을 리턴합니다.
	 * 
	 * @return listener 의 값
	 */
	public FileStatusChangeListener getListener() {
		return listener;
	}

	/**
	 * file 의 값을 리턴합니다.
	 * 
	 * @return file 의 값
	 */
	public boolean isFile() {
		return file;
	}

	/**
	 * recursive 의 값을 리턴합니다.
	 * 
	 * @return recursive 의 값
	 */
	public boolean isRecursive() {
		return recursive;
	}

	/**
	 * recursive 에 값을 세팅합니다.
	 * 
	 * @param recursive recursive 에 값을 세팅하기 위한 인자 값
	 */
	public void setRecursive(boolean recursiveFlag) {
		this.recursive = recursiveFlag;
	}
	
}// end of Target.java