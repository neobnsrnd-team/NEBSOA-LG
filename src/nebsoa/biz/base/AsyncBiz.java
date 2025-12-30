/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
package nebsoa.biz.base;

import nebsoa.common.util.DataMap;

/*******************************************************************
 * <pre>
 * 1.설명 
 * Async 메시지 처리를 수행하는 Biz 인터페이스 
 * AsyncMessageClient를 통해 요청 전문을 전송하는 Biz는 이 인터페이스를 implement하면 
 * Async로 응답 결과(또는 발생한 Exception)을 전달받을 수 있다. 
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
 * $Log: AsyncBiz.java,v $
 * Revision 1.1  2018/01/15 03:39:51  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:03  cvs
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
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/04/08 04:42:52  김승희
 * 신규추가
 *
 * </pre>
 ******************************************************************/
public interface AsyncBiz {
	
	/**
	 * 응답 결과를 전달 받아 필요한 처리를 한 후 리턴한다.
	 * @param dataMap 응답 결과가 담겨 있는 DataMap
	 * @return 처리 결과를 담은 DataMap
	 */
	public DataMap doResponse(DataMap dataMap);
	
	/**
	 * 메시지 처리에서 발생한 Exception을 전달 받아 처리를 한 후 원하는 데이터를 리턴한다.
	 * 만약 Exception으로 처리하고 싶다면 Exception을 리턴하고, 
	 * Exception으로 처리하고 싶지 않다면 특정 객체 또는 null을 리턴한다.
	 * @param throwable 발생한 Exception 객체
	 * @return
	 */
	public Object handleException(Throwable throwable);
}
