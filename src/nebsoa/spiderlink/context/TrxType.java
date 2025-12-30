/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
package nebsoa.spiderlink.context;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 거래 유형 클래스
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
 * $Log: TrxType.java,v $
 * Revision 1.1  2018/01/15 03:39:49  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:28  cvs
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
 * Revision 1.2  2008/09/08 03:04:38  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/08/04 08:54:52  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/04/08 04:42:52  김승희
 * 신규추가
 *
 *
 * </pre>
 ******************************************************************/

public class TrxType {
	
	/**
	 * 온라인
	 */
	public static final String ONLINE = "1";
	
	/**
	 * 배치
	 */
	public static final String BATCH = "2";
	
	/**
	 * Async 온라인
	 */
	public static final String ASYNC = "3";

	/**
	 * Async 비온라인
	 */
	public static final String ASYNC_NONE_ONLINE = "4";
	
	
	
	public static boolean isAsync(String trxType){
		return ASYNC.equals(trxType);
	}
	
	public static boolean isOnline(String trxType){
		return ONLINE.equals(trxType);
	}
	
	public static boolean isBatch(String trxType){
		return BATCH.equals(trxType);
	}
	
	public static boolean isAsyncNoneOnline(String trxType){
		return ASYNC_NONE_ONLINE.equals(trxType);
	}
	
}
