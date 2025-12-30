/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.list.navigator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 자동으로 페이징 처리 해 주는 클래스의 아키텍쳐 설계 interface 
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
 * $Log: Navigator.java,v $
 * Revision 1.1  2018/01/15 03:39:48  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:18  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:50  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:23  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:26  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:34  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:37:42  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2006/06/19 04:10:16  안경아
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public interface Navigator {
	
	public String getPagingStr();	
	
	public String getBeforeLink();
	
	public String getNextLink();
	
    public String doStartTag();

	public String makePageLink();

	public String makeLinkWithPageParam(int pageNum, String displayStr);

    public void setPrevImg(String prevImg);
    
    public void setNextImg(String nextImg);

    public void setBeginImg(String beginImg);

    public void setEndImg(String endImg);
    
	public void setUrl(String url);

	public void setPageParamName(String pageParamName);

	public void setPageGroupSize(String pageGroupSize);

	public void setCurrentPage(int currentPage);
	
	public void setTotal(int total);

	public void setDisplayCount(String displayCount);

	public int getCurrentPage();
}
