/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.config;

import java.util.HashMap;
import java.util.Map;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 메뉴 정보를 Load하는 클래스입니다.
 * 
 * 2.사용법
 * MenuMapping 인터페이스를 implements하여 아래의 메소드 및 각 추상 메소드를 구현하면 됩니다.
 * 
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
 * $Log: MenuMapping.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:26  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:26  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:29  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:52  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/02/14 10:30:33  오재훈
 * WebAppMapping 클래스가 ap단으로 이동됨
 *
 * Revision 1.1  2008/01/22 05:58:28  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:18  안경아
 * *** empty log message ***
 *
 * Revision 1.6  2006/09/14 08:34:30  최수종
 * 주석 수정
 *
 * Revision 1.5  2006/09/14 08:33:17  최수종
 * 로직 수정
 *
 * Revision 1.4  2006/09/13 07:03:26  최수종
 * 로직 수정
 *
 * Revision 1.3  2006/09/13 01:32:14  최수종
 * 로직 수정
 *
 * Revision 1.2  2006/09/12 11:53:12  최수종
 * 로직 수정
 *
 * Revision 1.1  2006/09/12 10:58:16  최수종
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public interface MenuMapping {
	
	/**
	 * 메뉴 정보를 load하여 Map에 담는다.
	 */
	public void loadMenuInfo();
	
	/**
	 * 메뉴 정보를 다시 load한다.
	 *
	 */
	public void reloadMenuInfo();
	
	
	/**
	 * @return menuInfoMap(메뉴 정보를 담고 있는 Map객체)를 리턴한다.
	 */
	public Map getMenuInfoMap(); 
	
	/**
	 * 요청 URI에 따른 Action클래스 명을 리턴한다.
	 * @return
	 */
	public String getClassName(String uri);
	
	/**
	 * 메뉴 URI에 해당하는  메뉴ID를 얻어온다.
	 * @param uri
	 * @return
	 */
	public String getMenuId(String uri);	
	
	
	/**
	 * 메뉴정보가 담긴 Map에서 uri에 해당하는 메뉴 시퀀스값을 리턴한다.
	 * 
	 * @param uri
	 * @return int
	 */
	public int getMenuSeq(String uri);	
	
	/**
	 * URI에 해당하는 등록된 메뉴(Action클래스)가 있는지 검사
	 * @param uri
	 * @return
	 */
	public boolean containsMenuUrl(String uri);

}
