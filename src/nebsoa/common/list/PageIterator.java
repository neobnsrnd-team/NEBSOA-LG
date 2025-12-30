/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.list;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 페이징 처리를 하기 위한 클래스.
 * 
 * 
 * 
 * 2.사용법
 * JSP 페이지에서의 사용법은 아래와 같습니다.
 * 
 * &lt;%
 * 	PageIterator pageIter = (PageIterator)dataMap.get("pageIter");
 * 	String prevLink = pageIter.getPreviousGroupPageLink();
 * 	if (prevLink == null) {
 * 		out.println("&lt;img src='" + request.getContextPath() + "/img/btn_prev.gif' width='11' height='11' border='0' align='absmiddle' style='margin-bottom:2'&gt;&amp;nbsp;");
 * 	} else {
 * 		out.println("&lt;a href='" + prevLink + "'&gt;&lt;img src='" + request.getContextPath() + "/img/btn_prev.gif' width='11' height='11' border='0' align='absmiddle' style='margin-bottom:2'&gt;&lt;/a&gt;&amp;nbsp;");
 * 	}//end if else
 * 	for ( ; pageIter.hasNextPage(); ) {
 * 		int pageNum = pageIter.nextPage();
 * 		
 * 		if (pageNum == pageIter.getCurrentPage()) {
 * 			out.print("&lt;b class='red'&gt;" + pageNum + "&lt;/b&gt;");
 * 		} else {
 * 			out.print("&lt;a href='" + pageIter.getCurrentPageLink() + "'&gt;" + pageNum + "&lt;/a&gt;");
 * 		}//end if else
 * 		
 * 		out.println(pageIter.hasNextPage() ? " | " : " &amp;nbsp;");
 * 	}//end for
 * 	String nextLink = pageIter.getNextGroupPageLink();
 * 	if (nextLink == null) {
 * 		out.println("&lt;img src='" + request.getContextPath() + "/img/btn_next.gif' width='11' height='11' border='0' align='absmiddle' style='margin-bottom:2'&gt;");
 * 	} else {
 * 		out.println("&lt;a href='" + nextLink + "'&gt;&lt;img src='" + request.getContextPath() + "/img/btn_next.gif' width='11' height='11' border='0' align='absmiddle' style='margin-bottom:2'&gt;&lt;/a&gt;");
 * 	}//end if else
 * %&gt;
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
 * $Log: PageIterator.java,v $
 * Revision 1.1  2018/01/15 03:39:51  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:59  cvs
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
 * Revision 1.2  2006/06/19 04:10:16  안경아
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class PageIterator {
	
	// TODO 페이징 데코레이팅 정보와 실제 페이징 정보를 분리하도록 한다!!
	
	/*----------------------------------
	 * 
	 * paging member properties
	 * 
	 ----------------------------------*/
	
	/**
	 * 총 게시물 수
	 */
	private int total;	
	/**
	 * 총 페이지 수
	 */
	private int totalPage;
	/**
	 * 현재 페이지
	 */
	private int currentPage;
	/**
	 * 페이지 당 게시물 수
	 */
	private double displayCount;
	/**
	 * 페이지 그룹 당 페이지 수
	 */
	private int pageGroupSize;
	
	/**
	 * URL
	 */
	private String url;
	
	/**
	 * 페이징하는 문자열에 대한 버퍼
	 */
	private StringBuffer pageBuffer;
	
	
	
	/*----------------------------------
	 * 
	 * paging default constants
	 * 
	 ----------------------------------*/
	
	/**
	 * 페이지 당 게시물 수의 기본 크기
	 */
	private static final double DEFAULT_DISPLAY_ITEM_COUNT_PER_PAGE = 10.0;
	/**
	 * 보여질 페이지 리스트 사이즈의 기본 크기
	 */
	private static final int DEFAULT_DISPLAY_PAGE_COUNT_PER_PAGE_GROUP = 10;
	
	
	
	/*----------------------------------
	 * 
	 * paging constructor
	 * 
	 ----------------------------------*/
	
	/**
	 * 페이징 작업에 대한 초기화 작업을 실시하는 생성자
	 * 
	 * @param total 총 게시물 수
	 * @param currentPage 현재 페이지
	 * @param url URL
	 */
	public PageIterator(int total, int currentPage, String url) {		
		this(total, currentPage, url, DEFAULT_DISPLAY_ITEM_COUNT_PER_PAGE, DEFAULT_DISPLAY_PAGE_COUNT_PER_PAGE_GROUP);	
	}//end of constructor
	
	/**
	 * 페이징 작업에 대한 초기화 작업을 실시하는 생성자
	 * 
	 * @param total 총 게시물 수
	 * @param currentPage 현재 페이지
	 * @param url URL
	 * @param displayItemCountPerPage 페이지 당 게시물 수
	 * @param displayPageCountPerPageGroup 페이지 그룹 당 페이지 수
	 */
	public PageIterator(int total, int currentPage, String url, double displayItemCountPerPage, int displayPageCountPerPageGroup) {
		
		this.total = total;
		this.currentPage = currentPage;
		this.url = url;
		
		this.displayCount = displayItemCountPerPage;
		this.pageGroupSize = displayPageCountPerPageGroup;
		
		initialize();
		
	}//end of constructor
	
	
	
	/*----------------------------------
	 * 
	 * core initialize Method
	 * 
	 ----------------------------------*/
	
	/**
	 * 초기화 작업을 수행합니다.
	 *
	 */
	private void initialize() {
		
		// 전체 페이지 수 계산
		this.totalPage = calculateTotlaPage(this.total, this.displayCount);
		
        // 현재 페이지가 속한 페이지 그룹
		this.currentPageGroup = calculateCurrentPageGroup(currentPage, this.pageGroupSize);
		
		// 마지막 페이지 그룹
		this.lastPageGroup = calculateLastPageGroup(totalPage, this.pageGroupSize);
		
		// 현재 페이지 그룹의 시작 페이지
		this.startPageInCurrentPageGroup = this.currentPageGroup * this.pageGroupSize + 1;
		
		// 현재 페이지 그룹의 마지막 페이지
		this.endPageInCurrentPageGroup = this.currentPageGroup == lastPageGroup ? this.totalPage : this.currentPageGroup * this.pageGroupSize + this.pageGroupSize;
		
		
//		System.out.println("currentPageGroup : " + this.currentPageGroup);
//		System.out.println("lastPageGroup : " + this.lastPageGroup);
//		System.out.println("startPageInCurrentPageGroup : " + this.startPageInCurrentPageGroup);
//		System.out.println("endPageInCurrentPageGroup : " + this.endPageInCurrentPageGroup);
		
		this.pageIndex = this.startPageInCurrentPageGroup;
		
		/*
		 * 페이징하는 html 관련 코드를 버퍼링 할 객체
		 */
		this.pageBuffer = new StringBuffer();
		
	}//end of initialize()
	
	
	
	
	/*----------------------------------
	 * 
	 * paging getter Method
	 * 
	 ----------------------------------*/
	
	/**
	 * 시작 페이지
	 */
	private int startPageInCurrentPageGroup;
	/**
	 * 끝 페이지
	 */
	private int endPageInCurrentPageGroup;
	
	/**
	 * 현재 페이지 번호
	 */
	private int pageIndex;
	
	/**
	 * 현재 페이지 그룹
	 */
	private int currentPageGroup;
	
	/**
	 * 마지막 페이지 그룹
	 */
	private int lastPageGroup;
	
	/**
	 * 다음 페이지가 있으면 true 를 리턴합니다.
	 * 
	 * @return 다음 페이지가 있으면 true
	 */
	public boolean hasNextPage() {
		return this.pageIndex <= this.endPageInCurrentPageGroup;
	}//end of hasNextPage()
	
	/**
	 * 다음 페이지 번호를 리턴합니다.
	 * 
	 * @return 다음 페이지 번호
	 */
	public int nextPage() {
		return this.pageIndex++;
	}//end of nextPage()
	
	/**
	 * 현재 페이지의 링크를 리턴합니다.
	 * 
	 * @return 페이지 링크
	 */
	public String getCurrentPageLink() {
		return "javascript:goSearch(\"" + this.url + "\",\"" + (this.pageIndex - 1) + "\")'";
	}//end of getCurrentPageLink()
	
	/**
	 * 이전 페이지 링크를 리턴합니다.
	 * 만약, page 가 0 과 같거나 작다면 null 을 리턴합니다.
	 * 
	 * @return 이전 페이지 링크
	 */
	public String getPreviousPageLink() {
		int prevPage = this.currentPage - 1;
		if (prevPage <= 0) {
			return null;
		} else {
			return makePageLink(this.url, prevPage);
		}//end if else		
	}//end of getPreviousPageLink()
	
	/**
	 * 다음 페이지 링크를 리턴합니다.
	 * 만약, page 가 맨 마지막 페이지라면 null 을 리턴합니다.
	 * 
	 * @return 다음 페이지 링크
	 */
	public String getNextPageLink() {
		if (this.currentPage == this.totalPage) {
			return null;
		} else {
			return makePageLink(this.url, this.currentPage + 1);
		}//end if else
	}//end of getNextPageLink()
	
	/**
	 * 이전 페이지 그룹의 페이지 링크를 리턴합니다.
	 * 만약, 현재 페이지 그룹이 최초 페이지 그룹이라면 null 을 리턴합니다.
	 * 
	 * @return 이전 페이지 그룹의 페이지 링크
	 */
	public String getPreviousGroupPageLink() {
		if (this.currentPageGroup == 0) {
			return null;
		} else {
			return makePageLink(this.url, this.startPageInCurrentPageGroup - 1);
		}//end if else
	}//end of getPreviousGroupPageLink()
	
	/**
	 * 다음 페이지 그룹의 페이지 링크를 리턴합니다.
	 * 만약, 현재 페이지 그룹이 마지막 페이지 그룹이라면 null 을 리턴합니다.
	 * 
	 * @return 다음 페이지 그룹의 페이지 링크
	 */
	public String getNextGroupPageLink() {
		if (this.currentPageGroup == this.lastPageGroup) {
			return null;
		} else {
			return makePageLink(this.url, this.endPageInCurrentPageGroup + 1);
		}//end if else
	}//end of getNextGroupPageLink()
	
	
	/**
	 * currentPage 의 값을 리턴합니다.
	 * 
	 * @return currentPage 의 값
	 */
	public int getCurrentPage() {
		return currentPage;
	}

	/**
	 * currentPageGroup 의 값을 리턴합니다.
	 * 
	 * @return currentPageGroup 의 값
	 */
	public int getCurrentPageGroup() {
		return currentPageGroup;
	}

	/**
	 * displayCount 의 값을 리턴합니다.
	 * 
	 * @return displayCount 의 값
	 */
	public double getDisplayCount() {
		return displayCount;
	}

	/**
	 * endPageInCurrentPageGroup 의 값을 리턴합니다.
	 * 
	 * @return endPageInCurrentPageGroup 의 값
	 */
	public int getEndPageInCurrentPageGroup() {
		return endPageInCurrentPageGroup;
	}

	/**
	 * lastPageGroup 의 값을 리턴합니다.
	 * 
	 * @return lastPageGroup 의 값
	 */
	public int getLastPageGroup() {
		return lastPageGroup;
	}

	/**
	 * pageGroupSize 의 값을 리턴합니다.
	 * 
	 * @return pageGroupSize 의 값
	 */
	public int getPageGroupSize() {
		return pageGroupSize;
	}

	/**
	 * pageIndex 의 값을 리턴합니다.
	 * 
	 * @return pageIndex 의 값
	 */
	public int getPageIndex() {
		return pageIndex;
	}

	/**
	 * startPageInCurrentPageGroup 의 값을 리턴합니다.
	 * 
	 * @return startPageInCurrentPageGroup 의 값
	 */
	public int getStartPageInCurrentPageGroup() {
		return startPageInCurrentPageGroup;
	}

	/**
	 * total 의 값을 리턴합니다.
	 * 
	 * @return total 의 값
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * totalPage 의 값을 리턴합니다.
	 * 
	 * @return totalPage 의 값
	 */
	public int getTotalPage() {
		return totalPage;
	}

	/**
	 * url 의 값을 리턴합니다.
	 * 
	 * @return url 의 값
	 */
	public String getUrl() {
		return url;
	}
	
	
	/**
	 * 페이지를 초기화 합니다.
	 *
	 */
	public void init() {
		this.pageIndex = this.startPageInCurrentPageGroup;
		this.pageBuffer.setLength(0);
	}//end of init()
	
	
	
	/**
	 * 페이지 버퍼에 주어진 문자열을 붙입니다.
	 * 
	 * @param s 붙일 문자열
	 * @return PageIterator 객체
	 */
	public PageIterator append(String s) {
		this.pageBuffer.append(s);
		return this;
	}//end of append()
	
	/**
	 * 페이지 버퍼에 주어진 숫자를 붙입니다.
	 * 
	 * @param i 붙일 int 형 데이터
	 * @return PageIterator 객체
	 */
	public PageIterator append(int i) {
		this.pageBuffer.append(i);
		return this;
	}//end of append()
	
	/**
	 * 페이지 버퍼에 주어진 숫자를 붙입니다.
	 * 
	 * @param d 붙일 double 형 데이터
	 * @return PageIterator 객체
	 */
	public PageIterator append(double d) {
		this.pageBuffer.append(d);
		return this;
	}//end of append()
	
	/**
	 * 페이지 버퍼에 주어진 문자를 붙입니다.
	 * 
	 * @param c 붙일 char 형 데이터
	 * @return PageIterator 객체
	 */
	public PageIterator append(char c) {
		this.pageBuffer.append(c);
		return this;
	}//end of append()
	
	/**
	 * 페이지 버퍼에 주어진 boolean 형 데이터를 붙입니다.
	 * 
	 * @param b 붙일 boolean 형 데이터
	 * @return PageIterator 객체
	 */
	public PageIterator append(boolean b) {
		this.pageBuffer.append(b);
		return this;
	}//end of append()
	
	
	
	/**
	 * 페이징 된 버퍼에 있는 문자열을 리턴합니다.
	 * 
	 * @return 페이징 된 문자열
	 */
	public String getPageString() {
		return this.pageBuffer.toString();
	}//end of getPageString()
	
	
	
	/*----------------------------------
	 * 
	 * static-util Method
	 * 
	 ----------------------------------*/
	

	/**
	 * 주어진 URL 과 page 를 가지고 page-link 를 만들어서 리턴합니다.
	 * 
	 * @param url 페이지 링크를 걸 URL
	 * @param page 페이지
	 * @return page-link
	 */
	private static String makePageLink(String url, int page) {
		return "javascript:goSearch(\"" + url + "\",\"" + page + "\")'";
	}//end of makePageLink()
	
	
	/**
	 * 주어진 값을 가지고 전체 페이지 수를 계산합니다.
	 * 
	 * @param total 전체 게시물 수
	 * @param displayItemCountPerPage 페이지 당 게시물 수
	 * @return 전체 페이지 수
	 */
	private static int calculateTotlaPage(int total, double displayItemCountPerPage) {
		return (int)(Math.ceil((total / displayItemCountPerPage))); 
	}//end of calculateTotlaPage()
	
	/**
	 * 주어진 값을 가지고 현재 페이지 그룹을 계산합니다.
	 * 페이지 그룹은 0 부터 시작합니다.
	 * 
	 * @param currentPage 현재 페이지
	 * @param displayPageCountPerPageGroup 페이지 그룹 당 페이지 수
	 * @return 현재 페이지의 페이지 그룹
	 */
	private static int calculateCurrentPageGroup(int currentPage, int displayPageCountPerPageGroup) {
		boolean flag = currentPage % displayPageCountPerPageGroup == 0;
		int currentPageGroup = currentPage / displayPageCountPerPageGroup;
		return flag ? currentPageGroup - 1 : currentPageGroup; 
	}//end of calculateTotlaPage()
	
	/**
	 * 마지막 페이지 그룹을 계산합니다.
	 * 
	 * @param totalPage 전체 페이지 수
	 * @param displayPageCountPerPageGroup 페이지 그룹 당 페이지 수
	 * @return 마지막 페이지 그룹
	 */
	private static int calculateLastPageGroup(int totalPage, int displayPageCountPerPageGroup) {
		return totalPage / displayPageCountPerPageGroup; 
	}//end of calculateLastPageGroup()
	

	
	/*----------------------------------
	 * 
	 * test 용 main() Method
	 * 
	 ----------------------------------*/
	
	/**
	 * 테스트 코드가 들어 있습니다.
	 * 
	 * @param args 테스트 코드를 위한 argument
	 */
	public static void main(String[] args) {
		System.out.println(calculateTotlaPage(101, 10.0));
		System.out.println(calculateTotlaPage(0, 10.0));
		System.out.println(calculateTotlaPage(9, 10.0));
		
		System.out.println(calculateCurrentPageGroup(9, 10));
		
		System.out.println("=====================================");
		
		PageIterator pageIter = new PageIterator(223, 12, "a", 20, 10);
		
		String prevLink = pageIter.getPreviousGroupPageLink();
		if (prevLink == null) {
			System.out.println("<img src='../img/btn_prev.gif' width='11' height='11' border='0' align='absmiddle' style='margin-bottom:2'>&nbsp;");
		} else {
			System.out.println("<a href='" + prevLink + "'><img src='../img/btn_prev.gif' width='11' height='11' border='0' align='absmiddle' style='margin-bottom:2'></a>&nbsp;");
		}//end if else
		
		
		for ( ; pageIter.hasNextPage(); ) {
			int page = pageIter.nextPage();
			
			if (page == pageIter.currentPage) {
				System.out.print("<b class='red'>" + page + "</b>");
			} else {
				System.out.print("<a href='" + pageIter.getCurrentPageLink() + "'>" + page + "</a>");
			}//end if else
			
			System.out.println(pageIter.hasNextPage() ? " | " : " &nbsp;");
			
		}//end for
		
		String nextLink = pageIter.getNextGroupPageLink();
		if (nextLink == null) {
			System.out.println("<img src='../img/btn_prev.gif' width='11' height='11' border='0' align='absmiddle' style='margin-bottom:2'>&nbsp;");
		} else {
			System.out.println("<a href='" + nextLink + "'><img src='../img/btn_prev.gif' width='11' height='11' border='0' align='absmiddle' style='margin-bottom:2'></a>");
		}//end if else
		
		System.out.println("=====================================");

	}//end of main()

}// end of PageIterator.java