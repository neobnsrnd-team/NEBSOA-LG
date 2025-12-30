/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
package nebsoa.spiderlink.engine.adapter.async;

import nebsoa.common.util.DataMap;
import nebsoa.spiderlink.context.MessageContext;

/*******************************************************************
 * <pre>
 * 1.설명 
 * Async Message Handler 최상위 인터페이스
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
 * $Log: AsyncMessageHandler.java,v $
 * Revision 1.1  2018/01/15 03:39:52  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:16  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:26  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:28  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.2  2008/09/08 03:04:38  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/08/04 08:54:54  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/04/08 04:42:52  김승희
 * 신규추가
 *
 * Revision 1.1  2008/04/08 02:32:43  shkim
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public interface AsyncMessageHandler{
		
	/**
	 * 응답 전문 수신 이후 처리를 한다.
	 * 응답 전문은 messageContext.getResult()로 얻어낼 수 있다.
	 * 
	 * @param messageContext
	 * @return
	 * @throws Throwable
	 */
	public DataMap doAfterReceivingResponse(MessageContext messageContext) throws Throwable;
	
	/**
	 * 현재 처리 중인 DataMap을 리턴한다.
	 * @return
	 */
	public DataMap getDataMap();
	
	/**
	 * 현재 처리 중인 DataMap을 설정한다.
	 * 2008.09.02 김영석 추가
	 */
	public void setDataMap(DataMap dataMap);
	
}
