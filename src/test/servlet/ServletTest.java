/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package test.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ServletTest extends HttpServlet { // RequestProcessor{
	
	public ServletTest() {
	}

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
	    String requestURI=request.getRequestURI();
	    PrintWriter out = response.getWriter();
        out.println("<br> request URI:"+requestURI);
        out.println("<br> locale:"+request.getLocale());
        out.println("<br> referer:"+request.getHeader("REFERER"));
        out.println("<br> context_path:"+request.getContextPath());
        out.println("<br> remote addr:"+request.getRemoteAddr());
	}
}
