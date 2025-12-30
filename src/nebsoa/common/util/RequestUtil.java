/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.util;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import nebsoa.common.cache.XmlCacheManager;
import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;


/*******************************************************************
 * <pre>
 * 1.설명 
 * HttpServletRequest로 부터 해당 커맨드의 메소드를 찾아 내어 호출
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
 * $Log: RequestUtil.java,v $
 * Revision 1.1  2018/01/15 03:39:49  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:30  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:23  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:26  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:50  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.2  2008/02/21 07:23:18  오재훈
 * *** empty log message ***
 *
 * Revision 1.1  2008/02/14 04:34:29  오재훈
 * web.login.Authenticator, Authorizor 클래스들과 web.session.SessionManager,UserInfo 들이 삭제되고 common.acl 패키지의 클래스들로 대체되었습니다.
 * web.util.Request.Util 클래스도 common.util.RequestUtil로 옴겨졌습니다.
 *
 * Revision 1.1  2008/01/22 05:58:33  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:57  안경아
 * *** empty log message ***
 *
 * Revision 1.6  2006/07/05 22:15:08  이종원
 * *** empty log message ***
 *
 * Revision 1.5  2006/07/05 12:51:42  이종원
 * *** empty log message ***
 *
 * Revision 1.4  2006/07/03 07:42:43  김성균
 * isFromMi() 메소드 deprecated 시킴
 *
 * Revision 1.3  2006/06/23 07:32:05  김성균
 * getUrlPath() 추가
 *
 * Revision 1.2  2006/06/17 08:10:26  오재훈
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class RequestUtil {
	
	public static  Method getMethod(Object obj, HttpServletRequest request){

		// 요청된 메소드를 찾고 없으면 default로 list 메소드를 지정 합니다.
		String method = getParameter(request,"method","execute");
		LogManager.debug("요청된 메소드 ------>"+method);
	
		Method actionMethod;
		try {
			actionMethod =
					obj.getClass().getMethod(method,
					new Class[]{DataMap.class});
		} catch (NoSuchMethodException e) {
			throw new SysException("메소드를 찾을 수 없습니다: " + method);
		}
		return actionMethod;
	}
	
	
	public static String getParameter(HttpServletRequest request,String paramName,String defaultValue){
		return getParameter(request,paramName,defaultValue);	
	}
	
//	public static String getParameter(HttpServletRequest request,String paramName){
//		return getParameter(request,paramName,null);	
//	}
	
	
	/**
	request로 부터 파라미터 값을 얻어 낼때 한글변환 시켜 얻어낸다
	@param  request   HttpServletRequest .
	@param  paramName  파라미터 이름.
	@return  얻어낸 라파미터 값
	*/
	public static String getParameter(HttpServletRequest request, String paramName){
        return StringUtil.toKo(request.getParameter(paramName));
    }



   	/**
	request로 부터 파라미터 값을 얻어 낼때 한글변환 시켜 얻어낸다
	@param  request   HttpServletRequest .
	@param  paramName  파라미터 이름.
	@return  얻어낸 라파미터 값의 배열
	*/
    public static String[] getParameterValues(HttpServletRequest request,
	String paramName){
        String array[] = request.getParameterValues(paramName);
        if(array == null)
            return null;
        for(int i = 0; i < array.length; i++)
            array[i] = StringUtil.toKo(array[i]);

        return array;
    }
    
    /**
     * 보안이 적용되는 페이지인지를 검사한다.
     * 
     * @param request
     * @return 보안이 적용되어야 하는 페이지이면 <code>true</code>, 아니면 <code>false</code>.
     */
    public static boolean isSecretPage(HttpServletRequest request) {
		//무조건 return false;로 해 두었음.
        int x=0;
        if(x==0) return false;
        String uri = request.getRequestURI();
		String contextPath=request.getContextPath();
        uri=StringUtil.replace(uri,contextPath,"");
        
        /*
		if (uri.startsWith("/")) {
			if (!uri.startsWith(request.getContextPath())) {
				uri = request.getContextPath() + uri;
			}
		}
        */
		LogManager.debug(uri+"############## 암호화 대상여부 체크");
        ArrayList list = XmlCacheManager.getInstance().getList("ENCRYPTED_MENU");
        for (int i = 0; i < list.size(); i++) {
            Map map = (Map) list.get(i);
            if (map.containsValue(uri)) {
                LogManager.debug("\t#######암호화 대상 입니다.");
                return true;
            }
        }
        LogManager.debug("암호화 대상이 아닙니다.");
        return false;
    }
	
	/**
	 * 설명: 마이플랫폼으로 xml데이타를 보내주기 위한 체크 로직
	 * 2006. 03. 09. 최수종 작성
	 * @return xml데이타로 보내줘야 되면 true, 아니면 false
	 * @deprecated
	 */
	public static boolean isFromMi(HttpServletRequest request){
	    String mi = request.getParameter("_search_caller") == null ? "" : request.getParameter("_search_caller");
	    
		if(mi.equals("mi")){
			return true;
		}else{
			return false;
		}
	}	
	
	/**
	 * 경로 부분을 제외한 path 정보를 구한다.
	 * @param request
	 * @return
	 */
	public static String getUrlPath(HttpServletRequest request) {
		String requestURI = request.getRequestURI();
		String path = requestURI.substring(requestURI.lastIndexOf("/") + 1);
		return path;
	}
	
	/**
	 * Contest Name을 리턴
	 * @param request
	 * @return
	 */
	public static String getContextName(HttpServletRequest request) {
		String contextPath = request.getContextPath();
		String contextName = contextPath.substring(1, contextPath.length());
		return contextName;
	}
}
