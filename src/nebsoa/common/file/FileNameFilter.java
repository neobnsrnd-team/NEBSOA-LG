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
 * 파일을 걸러낼 때 사용하는 클래스
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
 * $Log: FileNameFilter.java,v $
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
public class FileNameFilter implements java.io.FilenameFilter{
	/**
	 * 찾고자 하는 파일명이 ~~로 시작 되는지 검사
	 */
	public static final int START_WITH=0;
	/**
	 * 찾고자 하는 파일명이 ~~를 포함 하는지 검사
	 */
	public static final int INCLUDE=1;
	/**
	 * 찾고자 하는 파일명이 ~~로 끝나는지 검사
	 */
	public static final int END_WITH=2;
	
	private String filterStr = ".properties.xml";
	
	private int filterType=INCLUDE;
	
	/**
	 * 해당 문구가 있는 파일을 모두 골라 냅니다.
	 * 생성자 @param filterStr
	 */
	public FileNameFilter(String filterStr){
		this(filterStr,1);	
	}
	
	/**
	 * 해당 문구가 있는 파일을 모두 골라 냅니다.
	 * 생성자 
	 * @param filterStr 잦고자 하는 문구
	 * @param filterType 필터링 스타일
	 */
	public FileNameFilter(String filterStr,int filterType){
		if(filterStr != null){
			this.filterStr = filterStr;
		}
		this.filterType = filterType;
	}
	public boolean accept(File dir, String name){
		if(filterType==START_WITH){
			return (name.startsWith(filterStr));
		}else if(filterType==INCLUDE){
			return (name.indexOf(filterStr) > -1);
		}else {
			return (name.endsWith(filterStr));
		}
	}



}
