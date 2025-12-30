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

/*******************************************************************
 * <pre>
 * 1.설명 
 * 파일 정보를 가지고 있는 클래스
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
 * $Log: TargetItem.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:30  cvs
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
 * Revision 1.1  2008/01/22 05:58:26  오재훈
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
public class TargetItem {
	
	private long lastModified;
	private long fileSize;
	private File file;
	
	public TargetItem(long size, long modified, File file) {
		fileSize = size;
		lastModified = modified;
		this.file = file;
	}//end of constructor

	/**
	 * fileSize 의 값을 리턴합니다.
	 * 
	 * @return fileSize 의 값
	 */
	public long getFileSize() {
		return fileSize;
	}

	/**
	 * fileSize 에 값을 세팅합니다.
	 * 
	 * @param fileSize fileSize 에 값을 세팅하기 위한 인자 값
	 */
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	/**
	 * lastModified 의 값을 리턴합니다.
	 * 
	 * @return lastModified 의 값
	 */
	public long getLastModified() {
		return lastModified;
	}

	/**
	 * lastModified 에 값을 세팅합니다.
	 * 
	 * @param lastModified lastModified 에 값을 세팅하기 위한 인자 값
	 */
	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}

	/**
	 * file 의 값을 리턴합니다.
	 * 
	 * @return file 의 값
	 */
	public File getFile() {
		return file;
	}

}// end of TargetItem.java