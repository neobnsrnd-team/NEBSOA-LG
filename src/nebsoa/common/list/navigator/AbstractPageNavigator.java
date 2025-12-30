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
 * page navigator 클래스를 properties에서 읽어와 동적으로 생성할 때 사용하는 상위 인터페이스
 * 기존에 개발된 소스를 수정하지 않기 위해 Navigator를 수정하지 않고 AbstractPageNavigator를 새로 만든 것임.
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
 * $Log: AbstractPageNavigator.java,v $
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
 * Revision 1.1  2007/11/26 08:37:41  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2007/03/01 07:35:17  김승희
 * 최초 등록
 *
 *
 * </pre>
 ******************************************************************/
public interface AbstractPageNavigator extends Navigator {

	public abstract void setRequest(HttpServletRequest request);
	
	public abstract HttpServletRequest getRequest();
	
	public abstract void setResponse(HttpServletResponse response);
	
	public abstract HttpServletResponse getResponse();
	
	public abstract void setEncripted(boolean isEncripted);
	
	public abstract boolean isEncripted();
	
	public abstract void setPagingStr(String pagingStr);
	
	public abstract String getPagingStr();
}
