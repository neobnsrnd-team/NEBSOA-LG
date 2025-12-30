package nebsoa.common.list.navigator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nebsoa.common.log.LogManager;
import nebsoa.common.util.PropertyManager;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 관리자 사이트용 페이지 네비게이터 
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
 * $Log: AdmPageNavigator.java,v $
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
 * Revision 1.4  2007/10/09 06:16:14  안경아
 * *** empty log message ***
 *
 * Revision 1.3  2007/09/13 06:29:27  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2007/04/06 05:21:28  이종원
 * *** empty log message ***
 *
 * Revision 1.1  2006/11/13 11:18:23  오재훈
 * ejb 환경에서 관리자 화면의 페이지 네비게이터.
 *
 * Revision 1.1  2006/11/13 17:00:00  오재훈
 * 관리자 사이트용 페이징 유틸
 *
 *
 * </pre>
 ******************************************************************/
public class AdmPageNavigator {
	int total;				// 총 게시물 수
	int totalPage;
    int currentPage;		// 현재 페이지
    double displayCount=10.0;	// 페이지 게시물 수
    double pageGroupSize=10.0;	// 페이지 리스트 사이즈
    
	String url;
	String delemeter="&";
	
	static final String sbeginImg;
	static final String sendImg;
	static final String sprevImg;
	static final String snextImg;
	static final String admContext = PropertyManager.getProperty("web_config", "ADMIN_CONTEXT", 
            PropertyManager.getProperty("web_config", "ADMIN.CONTEXT_PATH","admin" ));//어드민 컨텍스트 명
	
	static {
		Object obj = "<img src="+admContext+PropertyManager.getProperty("web_config", "PAGE.BEGIN.IMAGE", "/images/icon/begin.gif")+" border='0' style='cursor:hand' >";

		sbeginImg = (obj == null) ? " [첫페이지] " : obj.toString();
		obj = "<img src="+admContext+PropertyManager.getProperty("web_config", "PAGE.END.IMAGE", "/images/icon/end.gif")+" border='0' style='cursor:hand' >";
		sendImg = (obj == null) ? " [끝페이지] " : obj.toString();
		obj = "<img src="+admContext+PropertyManager.getProperty("web_config", "PAGE.PREV.IMAGE", "/images/icon/prev.gif")+" border='0' style='cursor:hand' >";
		sprevImg = (obj == null) ? " [이전그룹] " : obj.toString();
		obj = "<img src="+admContext+PropertyManager.getProperty("web_config", "PAGE.NEXT.IMAGE", "/images/icon/next.gif")+" border='0' style='cursor:hand' >";
		snextImg = (obj == null) ? " [다음그룹] " : obj.toString();
	}//end of static
	
	String beginImg = sbeginImg;
	String endImg = sendImg;
	String prevImg = sprevImg;
	String nextImg = snextImg;
	
	String pageParamName="page";

	boolean autoMakeUrl=true;

	boolean isEncripted;


	//************** template에서 사용하기 위해 추가
	HttpServletRequest request;
	HttpServletResponse response;
	String pagingStr;
	
	public AdmPageNavigator(){};
	
	public AdmPageNavigator(HttpServletRequest request,
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
	public AdmPageNavigator(int total,int currentPage,int displayCount,String url){	    
	    this(null,null,total,currentPage,displayCount,url,false);
	}
	
	public AdmPageNavigator(HttpServletRequest request,
		int total,int currentPage,int displayCount,
		String url,boolean isEncripted){
		
		this(request,null,total,currentPage,displayCount,url,isEncripted);
	}
	
	public AdmPageNavigator(HttpServletRequest request,
						 int total,int currentPage,int displayCount,
						 String url){
		
		this(request,null,total,currentPage,displayCount,url,false);
	}

	public AdmPageNavigator(HttpServletRequest request,
						 int total,int currentPage,String url){
		
		this(request,null,total,currentPage,10,url,false);
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
	                "<font style='text_orange'>Go "+(currentPage-1)+" Page</font>");
	    }
	}
	
	public String getNextLink(){
	    makePageLink();//계산이 안되었다면 수행시킴
	    
	    if(currentPage == totalPage){
	        return "";
	    }else{
	        return makeLinkWithPageParam(currentPage+1,
	                "<font style='text_orange'>Go "+(currentPage+1)+" Page</font>");
	    }
	}
	
	//end of //*******  template에서 사용하기 위해 추가 
		
    public String doStartTag() {//throws JspException
        return makePageLink();
	}
    
    boolean isMade=false;
	public String makePageLink() 
    {
	    if(!isMade) {
	        isMade=true;
	    }else{ //이미 계산 했으면 skip
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
			int currentPageGroup =
				(int)(Math.ceil(currentPage/(pageGroupSize)));
			//전체 페이지 그룹
			int totalPageGroup =
				(int)(Math.ceil(totalPage/(pageGroupSize)));
			startPage = (int)((currentPageGroup-1) * pageGroupSize+1);
			endPage   = (int)(currentPageGroup * pageGroupSize);
			//무조건 더할 것이 아니라....토탈값보다 크면 안된다.         
			if (endPage>totalPage)endPage = totalPage ;

			//출력을 위한 버퍼
			StringBuffer buf = new StringBuffer();
//			buf.append( "<style type=text/css>")
//			.append("a:link {  font-size: 9pt; color: #003399; text-decoration: none} ")
//			.append("a:visited {  font-size: 9pt; color: #003399; text-decoration: none} ")
//			.append("a:hover {  font-size: 9pt; color: #0066CC; text-decoration: underline} ")
//			.append("a:active {  font-size: 9pt; color: #0066CC; text-decoration: none} ")
//			.append("</style>");
			buf.append("<table width='215' height='19' border='0' cellpadding='0' cellspacing='0'>");
			buf.append("<tr align='center'>");

			if(currentPageGroup > 2) {
				buf.append("<td width='15'>");
				buf.append(makeLinkWithPageParamNospace(1,beginImg));
				buf.append("</td>");
			}
			if(currentPageGroup > 1) {
				int prevPage =(int)((currentPageGroup-2)*pageGroupSize+1);
				buf.append("<td width='15'>");				
				buf.append(	makeLinkWithPageParamNospace(prevPage,prevImg));
				buf.append("</td>");
			}
			buf.append("<td width='145'>");
            for(int i=startPage; i<=endPage ;i++){
            	if(i == startPage){
            		//buf.append("&nbsp;");
            	}
            	if (i==currentPage) buf.append("<"+PropertyManager.getProperty("web_config", "PAGE.CURRENTPAGE.START.TAG", "B")+">").append(i).append("</"+PropertyManager.getProperty("web_config", "PAGE.CURRENTPAGE.END.TAG", "B")+">");
            	else buf.append(makeLinkWithPageParam(i, (i)+""));
            	if(i != endPage){
            		buf.append(PropertyManager.getProperty("web_config", "PAGE.DELIMETER.IMAGE", "|"));
            	}else {
            	//	buf.append("&nbsp;");
            	}
            	
            } // end for
			buf.append("</td>");
            if(currentPageGroup < totalPageGroup ){
				int nextPage = (int)( currentPageGroup*pageGroupSize+1 );
				buf.append("<td width='15'>");
				buf.append(	makeLinkWithPageParamNospace(nextPage, nextImg));
				buf.append("</td>");
			}
           
			if((currentPageGroup +1) < totalPageGroup ){
				buf.append("<td width='15'>");
				buf.append(makeLinkWithPageParamNospace(totalPage, endImg));
				buf.append("</td>");				
			}
			buf.append("</tr>");
			buf.append("</table>");
            //pageContext.getOut().println(buf.toString());
			return buf.toString();
        } catch(Exception e) {
			//throw new JspTagException(e.getMessage());
			LogManager.error("paging 처리 에서 에러"+e.getMessage());
			return "Paging Err";
        }
        //return SKIP_BODY;
    }
/*
	public int doEndTagssssss() {
		init();  //instance pooling때문에 이전처리한 데이타가 남아 있는 경우를 위해 초기환 한다.
		return EVAL_PAGE;
	}

	protected void init(){
		displayCount=10.0;	
		pageGroupSize=10.0;	
	
		delemeter="&";
		beginImg="<img src='/images/list/begin.gif' border='0' />";
		endImg="<img src='/images/list/end.gif' border='0' />";
		prevImg="<img src='/images/list/prev.gif' border='0' />";
		nextImg="<img src='/images/list/next.gif' border='0' />";
		pageParamName="page";

		autoMakeUrl=true;
		url="";
	}
*/
/*
	protected String makeUrl(HttpServletRequest request){
		//현재 페이지의 url을 생성한다.
		
		String url = request.getRequestURI();
		StringBuffer buf = new StringBuffer(url);
		buf.append("?");
		Enumeration e = request.getParameterNames();
		while(e.hasMoreElements()){
			String paramName = (String)e.nextElement();
			if(paramName.equals(pageParamName)) continue;  //페이지 세팅은 밑에서 하니까 넘어간다.
			String[] paramValues = 	
				request.getParameterValues(paramName);
			for(int i=0;i<paramValues.length;i++){
				buf.append(paramName)
					.append("=").append(paramValues[i]).append("&");
			}
		}
		return buf.toString();
	}
*/

    public String makeLinkWithPageParam(int pageNum, String displayStr) {
        
        return "&nbsp;<a href='javascript:goSearch(\""+url
        	+"\",\""+pageNum+"\")'>"+displayStr+"</a>";
    } 

    public String makeLinkWithPageParamNospace(int pageNum, String displayStr) {
        
        return "<a href='javascript:goSearch(\""+url
        	+"\",\""+pageNum+"\")'>"+displayStr+"</a>";
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
