/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.list;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nebsoa.common.exception.SysException;
import nebsoa.common.list.navigator.AbstractPageNavigator;
import nebsoa.common.list.navigator.DefaultPageNavigator;
import nebsoa.common.list.navigator.DtvPageNavigator;
import nebsoa.common.list.navigator.KebPageNavigator;
import nebsoa.common.list.navigator.Navigator;
import nebsoa.common.util.PropertyManager;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 자동으로 페이징 처리 해 주는 클래스
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
 * $Log: PageNavigator.java,v $
 * Revision 1.1  2018/01/15 03:39:51  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:00  cvs
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
 * Revision 1.1  2008/08/04 08:54:51  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:22  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:37:38  안경아
 * *** empty log message ***
 *
 * Revision 1.8  2007/03/01 07:32:05  김승희
 * pageNavigator의 클래스명을 properties에서 읽어와 동적으로 생성하도록 수정
 *
 * Revision 1.7  2006/10/19 01:00:52  오재훈
 * *** empty log message ***
 *
 * Revision 1.6  2006/09/20 07:10:41  오재훈
 * kebPageNavigator 클래스 추가.
 *
 * Revision 1.5  2006/06/19 04:10:16  안경아
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class PageNavigator {

	private static Navigator instance;
	
	//프로퍼티에 설정된 PageNavigator Class Path를 읽는다.
	private static String path = PropertyManager.getProperty("web_config", "PAGING.CLASS", "DefaultPageNavigator");
	
	//DTV_PORTAL용 PageNavigator 경로
	private static String dtvPath = "DtvPageNavigator";
	
//	DTV_PORTAL용 PageNavigator 경로
	private static String kebPath = "KebPageNavigator";	
	
	/**
	 * 프로퍼티 파일에 설정된 클래스에 해당하는 PageNavigator를 사용한다.
	 * 프로퍼티 파일이 없거나 정의되어 있지 않으면 DefaultPageNavigator 사용
	 */
	private static synchronized Navigator getInstance(HttpServletRequest request,
										HttpServletResponse response,
										int total,
										int currentPage,
										int displayCount,
										String url,
										boolean isEncripted){
//		path = 	"DefaultPageNavigator";
		if(path.equals("DefaultPageNavigator"))
		{
			instance = new DefaultPageNavigator(request,
					 	   response,
					 	   total,
					 	   currentPage,
					 	   displayCount,
					 	   url,
					 	   isEncripted);
		}
		else if(path.equals(dtvPath))
		{
			instance = new DtvPageNavigator(request,
				 	   response,
				 	   total,
				 	   currentPage,
				 	   displayCount,
				 	   url,
				 	   isEncripted);
		}
		else if(path.equals(kebPath))
		{
			instance = new KebPageNavigator(request,
				 	   response,
				 	   total,
				 	   currentPage,
				 	   displayCount,
				 	   url,
				 	   isEncripted);
		
		//그 외의 경우는 path를 Navigator를 구현한 클래스의 풀클래스명으로 간주하여 reflection으로 생성한다.
	    //단 이 경우 그 클래스는 Naigator가 아닌 AbstractPageNavigator을 상속받아야 한다.
		}else{
			
    		try{
	    		Class NavigatorClass = Class.forName(path);
	    		AbstractPageNavigator navigator = (AbstractPageNavigator)NavigatorClass.newInstance();
	    		navigator.setRequest(request);
	    		navigator.setResponse(response);
	    		navigator.setTotal(total);
	    		navigator.setCurrentPage(currentPage);
	    		navigator.setDisplayCount(String.valueOf(displayCount));
	    		navigator.setUrl(url);
	    		navigator.setEncripted(isEncripted);
	    		navigator.setPagingStr(navigator.doStartTag());
	    		
	    		instance = (Navigator)navigator;
	    		
    		}catch(Throwable e){
    			throw new SysException("PageNaivator 생성에 실패했습니다.["+path+"], 원인:" + e);
    		}
    			
    		
		}

		return instance;
	}
	public PageNavigator(HttpServletRequest request,
							HttpServletResponse response,
							int total,
							int currentPage,
							int displayCount,
							String url,
							boolean isEncripted){
		
		getInstance(request, response, total, currentPage, displayCount, url, isEncripted);
	}
	
	public PageNavigator(int total,	int currentPage, int displayCount, String url){
		
		getInstance(null, null, total, currentPage,	displayCount, url, false);
	}
	
	public PageNavigator(HttpServletRequest request, int total,int currentPage, String url) {
		getInstance(request, null, total, currentPage, 10, url, false);
	}

	
	
	
	
	public String getPagingStr(){
		return instance.getPagingStr();
	}
	
	public String getBeforeLink(){
	    return instance.getBeforeLink();
	}
	
	public String getNextLink(){	    
	    return instance.getNextLink();
	}
	
    public String doStartTag() {
        return instance.makePageLink();
	}

    public String makePageLink() 
    {
	    return instance.makePageLink();
    }

    public String makeLinkWithPageParam(int pageNum, String displayStr) {
    	return instance.makeLinkWithPageParam(pageNum, displayStr);
    } 

 

    public void setPrevImg(String prevImg) {
        instance.setPrevImg(prevImg);
    }

    public void setNextImg(String nextImg) {
        instance.setNextImg(nextImg);
    }

    public void setBeginImg(String beginImg) {
        instance.setBeginImg(beginImg);
    }

    public void setEndImg(String endImg) {
        instance.setEndImg(endImg);
    }
    
	public void setUrl(String url){
		instance.setUrl(url);
	}

	public void setPageParamName(String pageParamName){
		instance.setPageParamName(pageParamName);
	}

	public void setPageGroupSize(String pageGroupSize) {
		instance.setPageGroupSize(pageGroupSize);
    }

	public void setCurrentPage(int currentPage) {
        instance.setCurrentPage(currentPage);
    }

	public void setTotal(int total) {
        instance.setTotal(total);
    }

	public void setDisplayCount(String displayCount) {
		instance.setDisplayCount(displayCount);
    }
	
	public int getCurrentPage() {
		return instance.getCurrentPage();
	}
}
