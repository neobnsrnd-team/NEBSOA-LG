/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.schedule;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 스케줄러에 의해서 사용되는 상수들을 모아 놓은 클래스
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
 * $Log: ScheduleConstants.java,v $
 * Revision 1.1  2018/01/15 03:39:52  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:11  cvs
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
 * Revision 1.1  2008/01/22 05:58:35  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:39:14  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2006/06/17 10:37:45  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public final class ScheduleConstants {

    /** 스케줄과 관련된 configuration 이 있는 파일 */
    public static final String SCHEDULER_CONFIG_FILE = "schedule";
    
    
    /** 스케줄 리스트에서 각각의 스케줄을 구분할 구분자 */
    public static final String DELIM = "_";
    
    /** 스케줄러에 사용되는 Job 프로퍼티 */
    public static final String PROPERTY = "PROPERTY";
    
    /** 각 스케줄의 Job Class 에 대한 suffix */
    public static final String JOB_CLASS_SUFFIX = "_JOB_CLASS";
    /** 각 스케줄의 Cron Expression 에 대한 suffix */
    public static final String CRON_EXPRESSION_SUFFIX = "_CRON_EXPRESSION";
    
    /**
     * 객체 생성의 방지를 위한 private 생성자
     *
     */
    private ScheduleConstants() {        
    }//end of constructor
    
}// end of ScheduleConstants.java