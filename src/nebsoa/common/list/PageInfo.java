/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.list;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import nebsoa.common.util.DataMap;
import nebsoa.common.util.StringUtil;

/*******************************************************************
 * <pre>
 * 1.설명 
* 이 페이지로 전달된 정보를 유지 하기위해 사용되는 클래스
* 리스트에서 detail뷰로 간뒤 다시 리스트로 올때 정보를 유지하기 위해 사용된다.
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
 * $Log: PageInfo.java,v $
 * Revision 1.1  2018/01/15 03:39:51  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:00  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:25  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:28  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:51  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.3  2008/03/19 05:27:53  김은정
 * getPageParamAsHidden(DataMap map) modify - 값이 ""일 경우에도 hidden tag가 생성되도록 수정
 *
 * Revision 1.2  2008/03/14 08:36:19  김은정
 * getPageParamAsHidden(DataMap map) modify
 *
 * Revision 1.1  2008/01/22 05:58:21  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:37:38  안경아
 * *** empty log message ***
 *
 * Revision 1.6  2007/04/10 13:57:26  이종원
 * Array잘래 내는 로직 위치 변경
 *
 * Revision 1.5  2007/04/10 10:07:01  김성균
 * String[]로 등록된 키값일 경우 DataMap에서 키값 찾지 못하는 오류 수정
 *
 * Revision 1.4  2006/06/19 04:10:16  안경아
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class PageInfo {

	ArrayList pagingParamList;

    /** PAGE KEY */
    public static String KEY_PAGE = "page";

    /** DISPLAY_COUNT KEY */
    public static String KEY_DISPLAY_COUNT = "displayCount";

    /** Command KEY */
    public static String KEY_COMMAND = "cmd";

    /** SEARCH_PREFIX KEY */
    public static String KEY_SEARCH_PREFIX = "_search_";
	
	public PageInfo() {
        pagingParamList = new ArrayList();
    }

    public void addParamList(String key) {
        pagingParamList.add(key);
    }

    /**
     * request로 전달된 페이징 처리 관련 파라미터 데이타를 hidden tag String으로 리턴
     * @param request
     * @return html hidden tag string
     */
	public String getPageParam(HttpServletRequest request) {
        StringBuffer buf = new StringBuffer();
        Enumeration e = request.getParameterNames();
        boolean isFirst = true;
        while (e.hasMoreElements()) {
            String paramName = (String) e.nextElement();

            if (!isPagingParam(paramName)) {
                continue;
            }

            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues != null && paramValues.length > 0) {
                for (int i = 0; i < paramValues.length; i++) {
                    if (!StringUtil.isNull(paramValues[i])) {
                        if (isFirst) {
                            buf.append("?");
                        } else {
                            buf.append("&");
                        }
                        buf.append(paramName);
                        buf.append("=");
                        buf.append(paramValues[i]);
                        isFirst = false;
                    }
                }
            }
        }

        return buf.toString();
	}

    /**
     * request로 전달된 페이징 처리 관련 파라미터 데이타를 hidden tag String으로 리턴
     * @param request
     * @return html hidden tag string
     */
	public String getPageParamAsHidden(HttpServletRequest request) {
        return getPageParamAsHidden(request, false);
    }
	
    /**
     * request로 전달된 페이징 처리 관련 파라미터 데이타를 hidden tag String으로 리턴
     * @param request
	 * @param isPageNoExclude 파라미터중 페이지 번호를 제외할지 여부(true : 제외, false : 제외않함)
     * @return html hidden tag string
     */
	public String getPageParamAsHidden(HttpServletRequest request, boolean isPageParam) {
		StringBuffer buf = new StringBuffer();
        Enumeration e = request.getParameterNames();
        while (e.hasMoreElements()) {
            String paramName = (String) e.nextElement();

            if (!isPagingParam(paramName, isPageParam)) {
                continue;
            }

            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues != null && paramValues.length > 0) {
                for (int i = 0; i < paramValues.length; i++) {
                    if (!StringUtil.isNull(paramValues[i])) {
                        buf.append("\r\n<input type='hidden' ");
                        buf.append("name='" + paramName + "' ");
                        buf.append("value='" + paramValues[i] + "' />");
                    }
                }
            }

        }

        return buf.toString();
	}	

	/**
     * DataMap으로 전달된 페이징 처리 관련 파라미터 데이타를 hidden tag String으로 리턴
	 * @param map
	 * @return html hidden tag string
	 */
	public  String getPageParamAsHidden(DataMap map) {
		StringBuffer buf = new StringBuffer();
        Set set = map.keySet();
        Iterator it = set.iterator();
        while (it.hasNext()) {
            String paramName = (String) it.next();
            
            if(paramName==null) continue;
            
            /** String[]로 등록된 키값일 경우 "Array" 제외한 키값으로 찾도록 수정 */
            int index = paramName.lastIndexOf("Array");
            if (index != -1) {
                paramName = paramName.substring(0, index);
            }else{ //hidden tag가 중복으로 생성되어 Array가 붙은 name만 생성되도록 수정 
            	continue;
            }
            
            if (!isPagingParam(paramName, false)) {
                continue;
            }
 
			String[] paramValues = map.getParameterValues(paramName);
            if (paramValues != null && paramValues.length > 0) {
                for (int i = 0; i < paramValues.length; i++) {
//                    if (!StringUtil.isNull(paramValues[i])) {
                	 if(paramValues[i] != null) {//paramValues[i]값이 ""일 경우에도 hidden tag가 생성되도록 수정
                        buf.append("\r\n<input type='hidden' ");
                        buf.append("name='" + paramName + "' ");
                        buf.append("value='" + paramValues[i] + "' />");
                    }
                }
            }
		}
		return buf.toString();
	}
    
    
    
	/**
	 * 파라미터로 넘길 것인지 확인.
	 * @param paramName
	 * @return
	 */
	private boolean isPagingParam(String paramName) {
	    return isPagingParam(paramName, false);
	}	
	
	/**
	 * 파라미터로 넘길 것인지 확인.
	 * @param paramName
	 * @param isPageNoExclude 파라미터중 페이지 번호를 제외할지 여부(true : 제외, false : 제외않함)
	 * @return 페이징 처리 파라미터 여부
	 */
	private boolean isPagingParam(String paramName, boolean isPageNoExclude) {
        boolean isPagingParam = false;
        
        if (!StringUtil.isNull(paramName)
                && (KEY_PAGE.equals(paramName) && !isPageNoExclude)
                || KEY_COMMAND.equals(paramName)
                || KEY_DISPLAY_COUNT.equals(paramName) 
                || paramName.startsWith(KEY_SEARCH_PREFIX)
                || pagingParamList.contains(paramName))
            isPagingParam = true;
        
        return isPagingParam;
	}
}
