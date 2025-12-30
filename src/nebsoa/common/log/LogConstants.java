/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.log;


/*******************************************************************
 * <pre>
 * 1.설명 
 * LOG설정에 대한 상수 정의 
 * 
 * 2.사용법
 * 
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author 이종원
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 
 * $Log: LogConstants.java,v $
 * Revision 1.1  2018/01/15 03:39:48  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:19  cvs
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
 * Revision 1.1  2008/08/04 08:54:51  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:19  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:39:07  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2006/11/10 04:11:56  이종원
 * 최초작성
 *
 * </pre>
 ******************************************************************/
public class LogConstants {
    /**
     * DEBUG Level Log output (file or console)
     */
    public static final String DEBUG = "DEBUG";
    /**
     * ERROR Level Log output
     */
    public static final String ERROR = "ERROR";
    /**
     * DB_ERROR Log output 
     */
    public static final String DB_ERROR = "DB_ERROR";  	
    /**
     * MONITOR Log output 
     */
    public static final String MONITOR = "MONITOR";   
    /**
     * USER_ACCESS Log output 
     */
    public static final String USER_ACCESS = "USER_ACCESS";   
    /**
     * ADMIN_ACCESS Log output 
     */
    public static final String ADMIN_ACCESS = "ADMIN_ACCESS";   
    /**
     * MESSAGE Log output 
     */
    public static final String MESSAGE = "MESSAGE";   
    /**
     * MESSAGE ERROR Log output 
     */
    public static final String MSG_ERROR = "MSG_ERROR";   
    /**
     * SPIDERLINK DEBUG Log output 
     */
    public static final String SPIDERLINK = "SPIDERLINK";   
    /**
     * BATCH Log output 
     */
    public static final String BATCH = "BATCH";   
    /**
     * SCHEDULE Log output 
     */
    public static final String SCHEDULE = "SCHEDULE";   
    /**
     * EMAIL Log output 
     */
    public static final String EMAIL = "EMAIL";  
    /**
     * SECURE_SIGN(전자서명) Log output 
     */
    public static final String SECURE_SIGN = "SECURE_SIGN"; 
    /**
     * MIGRATION Log output 
     */
    public static final String MIGRATION = "MIGRATION"; 
    /**
     * DAEMON Log output 
     */
    public static final String DAEMON = "DAEMON";
}
