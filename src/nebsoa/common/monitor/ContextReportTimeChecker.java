/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.monitor;

import java.util.Date;

import nebsoa.common.log.LogManager;
import nebsoa.common.util.PropertyManager;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 시스템에서 관리되고 있는 모니터링 데이터를 Report로 생성하는 클래스
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
 * $Log: ContextReportTimeChecker.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:29  cvs
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
 * Revision 1.1  2008/08/04 08:54:55  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:33  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:44  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2007/09/17 05:12:27  이종원
 * *** empty log message ***
 *
 * Revision 1.3  2007/09/17 04:59:21  이종원
 * 최초 생성은 skip하도록 수정
 *
 * Revision 1.2  2007/09/17 04:41:34  이종원
 * 최초 생성은 skip하도록 수정
 *
 * Revision 1.1  2007/09/17 01:40:27  이종원
 * try~catch추가
 *
 * </pre>
 ******************************************************************/
public class ContextReportTimeChecker{
	
	public static long lastReportTime = getFirstReportTime();
    public static long nextReportTime = lastReportTime;
    public static long reportCreateTerm = getReportCreateTerm();
    
    /**
     * Report생성 모드인지 체크하는 메소드. default value is false.
     * to enable this attribute set "REPORT_SAVE_MODE" (in "monitor" property file) ON.
     * 2007. 9. 11.  이종원 작성
     * @return
     */
    public static boolean isSaveReportMode(){
        boolean mode= PropertyManager.getBooleanProperty("monitor","REPORT_SAVE_MODE","false"); 
LogManager.debug("report저장 여부 : "+mode);
        return mode;
    }
    /**
     * Report생성 주기 (설정파일은 분단위로 해 두면, 자동으로 millisecond로 계산한다.).
     * 2007. 9. 11.  이종원 작성
     * @return
     */
    public static long getReportCreateTerm(){
        long term = PropertyManager.getIntProperty("monitor","REPORT_SAVE_TERM",60);
        if(term < 10000) term = term*1000*60;
        LogManager.debug("report저장 주기 : "+term+"["+(term/(1000*60))+"분]");
        return term;
    }
    
    /**
     * was 기동하지마자 도래 하신 시간은 첫번째는 skip하게 하기 위한 변수.
     */
    static boolean isFirst=true;
    /**
     * Report생성 주기가 되었는지 여부.
     * 2007. 9. 11.  이종원 작성
     * @return
     */
    public static boolean isReportCreateTime(){
        //if(!isReportMode()) return false; report생성은 안하더라도 데이터는 지워야 하니까..
        long now = System.currentTimeMillis();
//        LogManager.debug("현재시간 : "+now);
//        LogManager.debug("이전레포트 생성시간 : "+lastReportTime);
//        LogManager.debug("다음레포트 생성시간 : "+nextReportTime);
//        LogManager.debug("레포트 생성 주기  : "+(reportCreateTerm/60000)+"분");
        if(now>nextReportTime){
            if(isFirst){
                setNextReportTime();
                isFirst=false;
                return false;
            }
            
            LogManager.debug("레포트 생성 주기가 되었습니다."+
                    ((now-nextReportTime)/(1000*60))+"분 경과.");    
            
            setNextReportTime();
            return true;
        }else{
            if(i++ > 5000){ // 로그 출력 시간을 줄이기 위해. 5000번 마다 출력.
                i=0;
                LogManager.debug("남은주기  : "+((nextReportTime-now)/60000.0)+"분");
            }

            return false;
        }
    }
    
    static int i=0;
    
    /**
     * Report생성시각을 정각으로 맞추기 위해 지금시간이 정각인지.. 
     * 아니라면 보정해 주는 메소드.
     * 2007. 9. 11.  이종원 작성
     * @return
     */
    public static long getFirstReportTime(){
        long now = System.currentTimeMillis();
        Date d = new Date();
        int min = d.getMinutes();
        LogManager.debug("현재시각의 분 :"+min);
        long nextTime= now - min*1000*60;
        return nextTime;
    }
    
    public static void setNextReportTime(){
        nextReportTime = System.currentTimeMillis()+getReportCreateTerm()+10*1000;
        lastReportTime = System.currentTimeMillis();
        LogManager.debug("다음 레포트 생성 시간을 "+new Date(nextReportTime)+
                "로 세팅하였습니다");
    }
}
