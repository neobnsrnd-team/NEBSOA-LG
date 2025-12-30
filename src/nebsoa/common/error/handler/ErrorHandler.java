/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.error.handler;

import java.util.List;

import nebsoa.common.collection.DataSet;
import nebsoa.common.exception.SysException;
import nebsoa.common.jdbc.DBHandler;
import nebsoa.common.util.DataMap;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 실질적으로 오류처리를 담당하는 클래스입니다.
 * 오류처리를 담당하는 모든 클래스는 
 * 항상 본 클래스를 상속받아 구현해야 합니다.
 * 
 * 2.사용법
 * 
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
 * $Log: ErrorHandler.java,v $
 * Revision 1.1  2018/01/15 03:39:51  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:05  cvs
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
 * Revision 1.1  2008/08/04 08:54:52  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:28  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:56  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2007/06/20 10:41:46  최수종
 * 오류 핸들러 관련 수정 및 추가
 *
 *
 * </pre>
 ******************************************************************/
public abstract class ErrorHandler {

	/**
	 * 오류 핸들러 추가시 비즈니스 로직을 구현할 메소드
	 * 
	 * @param userParamVaule 사용자용 파라미터 설정값
	 * @param sysParamValue 시스템용 파라미터 설정값
	 */
	public abstract void executeHandler(String userParamVaule, String sysParamValue)
		throws Exception;
	
	
}






