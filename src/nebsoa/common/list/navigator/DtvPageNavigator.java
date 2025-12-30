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

import nebsoa.common.log.LogManager;

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
 * $Log: DtvPageNavigator.java,v $
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
 * Revision 1.4  2006/08/14 01:58:40  오재훈
 * a href='javascipt....' -> a href='#' onClick='javascipt:....' 로 변경
 * 사유 : A 태그가 걸린 상태에서 이중 서브밋 체크를 하였을 경우 전에 했던 submit의 겨로가가 리턴되지않고 페이지가 먹통이 되는 상황이 발생함.
 *
 * Revision 1.3  2006/06/19 04:10:16  안경아
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class DtvPageNavigator implements Navigator{
	
	private static DtvPageNavigator instance;
	int total;				// 총 게시물 수
	int totalPage;
    int currentPage;		// 현재 페이지
    double displayCount=10.0;	// 페이지 게시물 수
    double pageGroupSize=10.0;	// 페이지 리스트 사이즈
    

	String url;
	String delemeter="&";
			
	String beginImg = "<img src='images/skt/com/begin.gif' border='0'>" ;
	String endImg   = "<img src='images/skt/com/end.gif' border='0'>" ;
	String prevImg  = "<img src='images/skt/com/prev.gif' border='0'>" ;
	String nextImg  = "<img src='images/skt/com/next.gif' border='0'>" ;
	
	String pageParamName="page";

	boolean autoMakeUrl=true;

	boolean isEncripted;


	//************** template에서 사용하기 위해 추가
	HttpServletRequest request;
	HttpServletResponse response;
	String pagingStr;

	public DtvPageNavigator() {};

	public DtvPageNavigator(HttpServletRequest request,
							HttpServletResponse response,
							int total,
							int currentPage,
							int displayCount,
							String url,
							boolean isEncripted){
		
		this.request = request;
		this.response = response;
		this.total = total;
		this.currentPage = currentPage;
		this.displayCount = displayCount;
		this.url = url;

		this.isEncripted=isEncripted;

		pagingStr = doStartTag();
	}

	

	/**
	 *  @param request
	 *  @param total
	 *  @param currentPage
	 *  @param displayCount
	 *  @param url
	 *  @param isEncripted
	 * 새로운 프레임웍에 맞게 추가 goSearch(pageNum)형태로 생성
	 */
	public DtvPageNavigator(int total,int currentPage,int displayCount,String url){	    
	    this(null,null,total,currentPage,displayCount,url,false);
	}
	
	public DtvPageNavigator(HttpServletRequest request,
		int total,int currentPage,int displayCount,
		String url,boolean isEncripted){
		
		this(request,null,total,currentPage,displayCount,url,isEncripted);
	}
	
	public DtvPageNavigator(HttpServletRequest request,
						 int total,int currentPage,int displayCount,
						 String url){
		
		this(request,null,total,currentPage,displayCount,url,false);
	}

	public DtvPageNavigator(HttpServletRequest request,
						 int total,int currentPage,String url){
		
		this(request,null,total,currentPage,10,url,false);
	}
	
	
	/**
     * DtvPageNavigator 인스턴스 생성
     * 
     * @return DtvPageNavigator 인스턴스
     */
	public static synchronized DtvPageNavigator getInstance() {
		if(instance == null) {
            instance = new DtvPageNavigator();
        }
        return instance;
	}
	
	//페이징 된 문자열을 리턴한다.
	public String getPagingStr(){
		return pagingStr;
	}
	
	public String getBeforeLink(){
	    makePageLink();//계산이 안되었다면 수행시킴
	    
	    if(currentPage <= 1){
	        return "&nbsp;&nbsp;";
	    }else{
	        return makeLinkWithPageParam(currentPage-1,
	                prevImg);
	    }
	}
	
	public String getNextLink(){
	    makePageLink();//계산이 안되었다면 수행시킴
	    
	    if(currentPage == totalPage){
	        return "";
	    }else{
	        return makeLinkWithPageParam(currentPage+1,
	                nextImg);
	    }
	}
	
	//end of //*******  template에서 사용하기 위해 추가 
		
    public String doStartTag() {//throws JspException
        return makePageLink();
	}
    
    boolean isMade=false;
	public String makePageLink() 
    {
	    if(!isMade)
	    {
	        isMade=true;
	    }
	    else
	    { //이미 계산 했으면 skip
	        return pagingStr;
	    }
		
		int startPage;		// 페이지 리스트 그룹시작번
		int endPage;		// 페이지 리스트 그룹 끝번

		try {
			//if(total==0) return SKIP_BODY;
			if(total==0) return "";
/*			
			if(autoMakeUrl){
				HttpServletRequest request = 
					(HttpServletRequest)pageContext.getRequest();
				url = makeUrl(request);
			}
*/
            //소수점 계산을 위해 double type으로 선언 
			totalPage = (int)(Math.ceil((total/displayCount))); 
			
            //현재 페이지가 속한 페이지 그룹
			int currentPageGroup = (int)(Math.ceil(currentPage/(pageGroupSize)));
			
			//전체 페이지 그룹
			int totalPageGroup =
				(int)(Math.ceil(totalPage/(pageGroupSize)));
			startPage = (int)((currentPageGroup-1) * pageGroupSize+1);
			endPage   = (int)(currentPageGroup * pageGroupSize);
			
			//무조건 더할 것이 아니라....토탈값보다 크면 안된다.         
			if (endPage>totalPage)endPage = totalPage ;

			//출력을 위한 버퍼
			StringBuffer buf = new StringBuffer();
			
			
			buf.append( "<style type=text/css>")
			.append("a:link {color: #555555; font-family:돋움; font-size:13px; text-decoration: none} ")
			.append("a:visited {color: #555555; font-family:돋움; font-size:13px; text-decoration: none} ")
			.append("a:hover {color: #555555; font-family:돋움; font-size:13px; text-decoration: underline;} ")
			.append(".page {font-size:13px; color: #555555}")
			.append(".page_b {color: #FF0000; font-family:돋움; font-size:13px; font-weight:bold; text-decoration: none} ")
			.append("</style>");
            
			//처음 페이지로 이동 이미지
			buf.append(makeLinkWithPageParam(1, beginImg));
			
			// 이전 그룹 페이지로 이동 이미지
			if (currentPageGroup > 1)
            {
				int prevPage = (int) ((currentPageGroup - 2) * pageGroupSize + 1);
                
				buf.append(makeLinkWithPageParam(prevPage, prevImg));
			}
            else
            {
                buf.append(makeLinkWithPageParam(1, prevImg));
            }
			
			buf.append("&nbsp;&nbsp;");
			
			// 페이징
            for (int i = startPage; i <= endPage; i++)
            {
            	if (i == currentPage)
                {
                    buf.append("<font class=page_b>" + i + "</font>");
                }
            	else
                {
                    buf.append(makeLinkWithPageParam(i, (i) + ""));  
                }          	
                if (i != endPage) buf.append("&nbsp;");
                
            } // end for

            buf.append("&nbsp;&nbsp;");
			
            // 다음 그룹 페이지로 이동 이미지 
            if (currentPageGroup < totalPageGroup)
            {
				int nextPage = (int) (currentPageGroup * pageGroupSize + 1);
                
				buf.append(makeLinkWithPageParam(nextPage, nextImg));
			}            
            else
            {
				buf.append(makeLinkWithPageParam(endPage, nextImg));
            }
			
            //마지막 페이지로 이동 이미지
            buf.append(makeLinkWithPageParam(totalPage, endImg));
			
			return buf.toString();			
        }
		catch(Exception e) {
			//throw new JspTagException(e.getMessage());
			LogManager.error("paging 처리 에서 에러"+e.getMessage());
			return "Paging Err";
        }
        //return SKIP_BODY;
    }

    public String makeLinkWithPageParam(int pageNum, String displayStr) {
        
        return "<a class=page href='#' onclick='javascript:goSearch(\""+url
        	+"\",\""+pageNum+"\")'>"+displayStr+"</a>";
//		StringBuffer buf = new StringBuffer();
//		//TODO 차후 수정
//		if(isEncripted){
//			buf.append("<a onClick='return XecureLink(this);' href=").append(url).append(delemeter).append(pageParamName).append("=").append(pageNum).append(">").append(displayStr).append("</a>");
//		}else{
//			buf.append("<a href=").append(url).append(delemeter).append(pageParamName).append("=").append(pageNum).append(">").append(displayStr).append("</a>");
//		}
//		try{
//			return StringUtil.replace(StringUtil.replace(buf.toString(),"?&","?"),"&&","&");
//		}catch(Exception ex){
//			return buf.toString();
//		}
    } 

 

    public void setPrevImg(String prevImg) {
        this.prevImg = prevImg;
    }

    public void setNextImg(String nextImg) {
        this.nextImg = nextImg;
    }

    public void setBeginImg(String beginImg) {
        this.beginImg = beginImg;
    }

    public void setEndImg(String endImg) {
        this.endImg = endImg;
    }
    
	public void setUrl(String url){
		this.url = url;
		if(url.indexOf("?") == -1 ) delemeter="?";
		autoMakeUrl =false;
	}

	public void setPageParamName(String pageParamName){
		this.pageParamName = pageParamName;
	}

	public void setPageGroupSize(String pageGroupSize) {
		try{
	        this.pageGroupSize = Integer.parseInt(pageGroupSize);
		}catch(NumberFormatException e){
			this.pageGroupSize=10;
		}
    }

	public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

	public void setTotal(int total) {
        this.total = total;
    }

	public void setDisplayCount(String displayCount) {
		try{
	        this.displayCount = Integer.parseInt(displayCount);
		}catch(NumberFormatException e){
			System.out.println("Numberformat exception...."+displayCount);
			System.out.println(e.getMessage());
			this.displayCount =10;
		}
    }
	
/*
	public void setPageGroupSize(int pageGroupSize) {
        this.pageGroupSize = pageGroupSize;
    }

	public void setDisplayCount(double displayCount) {
        this.displayCount = displayCount;
    }
*/

	/*===================================
	 * 
	 * paging 과 관련되어 getPagingStr() 메소드가
	 * UI 부분을 포함하고 있는 부분을 분리하기 위해서
	 * PageNavigator 내부의 변수에 접근할 수 있도록
	 * getter 메소드들을 추가함.
	 * 
	 * -Helexis 2005.11.19
	 * 
	 ===================================*/
	
	/**
	 * 현재 페이지를 리턴합니다.
	 * 
	 * @return 현재 페이지
	 * @author Helexis
	 * @since 2005.11.19
	 */
	public int getCurrentPage() {
		return this.currentPage;
	}//end of getCurrentPage()
}
