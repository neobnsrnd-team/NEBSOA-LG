/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.monitor;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 모니터링 관련 상수를 정의한다. 
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
 * $Log: MonitorConstants.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:31  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:27  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:29  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.4  2008/04/07 07:19:03  홍윤석
 * 모니터링 suffix 추가
 *
 * Revision 1.3  2008/03/19 06:54:01  김성균
 * 모니터링 카테고리 상수 추가
 *
 * Revision 1.2  2008/03/19 05:58:33  김성균
 * 모니터링 카테고리 상수 추가
 *
 * Revision 1.1  2008/03/18 01:08:20  김성균
 * 최초 등록
 *
 * </pre>
 ******************************************************************/
public class MonitorConstants {

	//모니터링 properties파일 
	public static final String MONITOR_CONFIG_FILE = "monitor";
	
	/**
	 * WAS 인스턴스 
	 */
	public static final String WAS_ID = "WAS_ID";
	
	/**
	 * 모니터링 항목 
	 */
	public static final String MONITOR_CATEGORY = "MONITOR_CATEGORY";
	
	/**
	 * 모니터링 데이타 
	 */
	public static final String MONITOR_OBJECT = "MONITOR_OBJECT";
	
	/**
	 * 기관별 전문처리 모니터링 
	 */
	public static final String MESSAGE_ORG = "MESSAGE_ORG";
	
	/**
	 * 주요거래 모니터링 
	 */
	public static final String MESSAGE_MAIN_TRX = "MESSAGE_MAIN_TRX";
	
	/**
	 * 타행수취 은행별 모니터링 
	 */
	public static final String MESSAGE_BANK = "MESSAGE_BANK";

	/**
	 * 메모리 모니터링 
	 */
	public static final String MEMORY = "MEMORY";
	
	/**
	 * 쓰레드 모니터링 
	 */
	public static final String THREAD = "THREAD";
	
	/**
	 * CPU 모니터링 
	 */
	public static final String CPU = "CPU";
	
	/**
	 * 모니터링 SUFFIX
	 */
	//모니터링 사용여부
	public static final String KEY_USE_MODE = ".USE_MODE";
	//모니터링 agent class
	public static final String KEY_MONITOR_AGENT_CLASS = ".MONITOR_AGENT_CLASS";
	//모니터링 주기
	public static final String KEY_PERIOD = ".PERIOD";
	//instance별 실행 여부
	public static final String KEY_INSTANCE_LIST = ".INSTANCE_LIST";
	//모니터링 주요거래 리스트
	public static final String KEY_TRX_ID_LIST = ".TRX_ID_LIST";	
	
	/**
	 * <code>MonitorDataVo</code>객체 생성 시간 
	 */
	public static final Object TIME = "TIME";
	
}
