/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.plugin.monitor;

import java.io.Serializable;

import nebsoa.plugin.monitor.util.MonitorLogger;


/*******************************************************************
 * <pre>
 * 1.설명 
 * Profile 대상 클래스가 구현해야 될 인터페이스
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
 * $Log: TimeProfiler.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:35  cvs
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
 * Revision 1.1  2008/01/22 05:58:34  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:46  안경아
 * *** empty log message ***
 *
 * Revision 1.4  2006/12/26 01:06:35  김성균
 * destroy()에서 수치 오류 수정(시간 계산시 1000으로 안나눔)
 *
 * Revision 1.3  2006/12/22 11:35:47  정보균
 * 수치 오류 수정(시간 계산시 1000으로 안나눔)
 *
 * Revision 1.2  2006/11/21 08:01:45  이종원
 * Log Class Update
 *
 * Revision 1.1  2006/11/15 09:54:27  이종원
 * refactoring
 *
 * Revision 1.3  2006/06/17 10:37:45  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class TimeProfiler implements Serializable{
    
    long begin = System.currentTimeMillis();
    String name="no-name";
    long start =0;
    public TimeProfiler(){
    }
    /**
     * Start timing an event called 'name'. You *MUST* call stop()
     * later in order to get valid results.
     */
    public void startEvent (String name){
        this.name = name;
        start = System.currentTimeMillis();
    }

    /**
     * Stop timing the last event started. You *MUST* have called
     * start() prior to this in order to get coherent results.
     * @exception IllegalStateException if stopEvent called too many times
     */
    public void stopEvent (){
        long end = System.currentTimeMillis();
        double term=(end*1.0-start)/1000;
        if(end-start > 1000){
            MonitorLogger.info(">>>성능점검SQL:"+name+":"+(term)+"초");
        }else{
            MonitorLogger.debug(">>>SQL:"+name+":"+(term)+"초");
        }
        start = end;
    }

    /**
     * Terminate profiling, releasing any resources allocated to
     * this profile and forwarding any collected statistics back
     * to the ProfileSystem. It is expected that implementations
     * will use this method to recycle the profile objects
     * themselves for efficiency.
     */
    public void destroy(){
        long end = System.currentTimeMillis();
        double term=(end*1.0-begin)/1000;
        if(end-start > 1000){
            MonitorLogger.info(">>>성능점검구간:"+name+" total:"+(term)+"초");
        }
    }
}
