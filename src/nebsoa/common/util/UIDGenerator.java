/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.util;

import nebsoa.common.exception.SysException;
import nebsoa.common.jdbc.DBManager;
import nebsoa.common.jdbc.DBResultSet;
import nebsoa.common.startup.StartupContext;

/*******************************************************************
 * <pre>
 * 1.설명 
 *  - 일단위 uid 생성 및 uid생성 함수 제공
 * 2.사용법
 *  - 일단위 uid : UIDGenerator.nextDailyUID();
 *  -      uid : UIDGenerator.nextUID();
 * <font color="red">
 * 3.주의사항
 * 단, 시간을 강제로 앞뒤로 옮겨서는 안된다.
 * </font>
 *
 * @author $Author: cvs $
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 
 * $Log: UIDGenerator.java,v $
 * Revision 1.1  2018/01/15 03:39:49  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:32  cvs
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
 * Revision 1.1  2008/08/04 08:54:50  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:18  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.2  2008/01/03 01:37:30  이종원
 * *** empty log message ***
 *
 * Revision 1.1  2007/11/26 08:38:02  안경아
 * *** empty log message ***
 *
 * Revision 1.16  2007/07/14 14:29:41  이종원
 * key기반으로 uuid 로직 test
 *
 * Revision 1.15  2007/07/14 14:15:40  이종원
 * key기반으로 uuid.properties.xml에서 sql을 읽어와서 리턴하도록 로직 수정
 *
 * Revision 1.14  2006/12/14 03:55:25  안경아
 * *** empty log message ***
 *
 * Revision 1.13  2006/12/14 03:35:15  안경아
 * *** empty log message ***
 *
 * Revision 1.12  2006/09/11 06:29:15  이종원
 * nextDailyUid 6, 7 , 8 자리 형태로 리턴하도록 구현 수정
 *
 * Revision 1.10  2006/09/11 05:43:21  김승희
 * *** empty log message ***
 *
 * Revision 1.9  2006/09/11 05:37:03  김승희
 * *** empty log message ***
 *
 * Revision 1.8  2006/09/11 05:35:50  김승희
 * *** empty log message ***
 *
 * Revision 1.7  2006/09/11 05:01:12  이종원
 * nextDailyUid, nextUid구현 수정
 *
 * Revision 1.6  2006/09/09 08:21:52  이종원
 * nextDailyUid, nextUid구현 수정
 *
 * Revision 1.5  2006/09/08 09:53:05  이종원
 * nextDailyUid, nextUid구현
 *
 * Revision 1.4  2006/07/04 11:32:08  이종원
 * *** empty log message ***
 *
 * Revision 1.3  2006/06/17 10:37:45  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class UIDGenerator {

    private static final short MAX_COUNT = 1000;
    private static final long ONE_SECOND = 1000; // in milliseconds(1 second)
    private static final int MASK = 0x70000000; 
    
    private static Object mutex = new Object();
    private static short lastCount = 0;
    private static long lastTime = System.currentTimeMillis();
    
    /**
     * 10자리의 Integer범위내에서 UID를 생성한다.
     * @return uid 
     */
    public static final int generateUID() {
        synchronized (mutex) {
            if (lastCount == MAX_COUNT) {
                boolean done = false;
                while (!done) {
                    long time = System.currentTimeMillis();
                    if (time < lastTime + ONE_SECOND) {
                        // pause for a second to wait for time to change
                        try {
                            Thread.sleep(ONE_SECOND);
                        }
                        catch (java.lang.InterruptedException e) {
                        } // ignore exception
                        continue;
                    }
                    else {
                        lastTime = time;
                        lastCount = 0;
                        done = true;
                    }
                }
            }
            else {
                lastTime = System.currentTimeMillis();
            }
            lastCount++;
            
            return (((((int) lastTime) + lastCount) & Integer.MAX_VALUE) | MASK);
        }
    }
    
    private static Object lock2=new Object();
    
    private static long lastDailyUid7L=0;
    /**
     * 당일에 한하여 유일한 7자리 문자열을 리턴 한다.
     * 단 시스템 일자를 강제로 미래, 과거로 세팅하는 일이 없어야 하며,
     * 2005. 9. 6.  이종원 작성
     * @return
     */
    public static String nextDailyUID7L(){
        synchronized(lock2){
            if(!initialized) init();
            long var = ((System.currentTimeMillis()/10)%10000000);
            
            if(var <= lastDailyUid7L){
                if(lastDailyUid7L < 9999999 ){
                    var = ++lastDailyUid7L;
                }else {
                    var= ++dailyUidCycleValue7L;
                    if(dailyUidCycleValue7L == lastDailyUid7L){
                        dailyUidCycleValue7L=0;
                        lastDailyUid7L=0;
                    }
                }
            }else{
                lastDailyUid7L=var;
            }
            return ((10000000+var)+"").substring(1);
        }
    }
    
    private static Object lock3=new Object();
    /**
     * 당일에 한하여 유일한 6자리 문자열을 리턴 한다.
     * 단 시스템 일자를 강제로 미래, 과거로 세팅하는 일이 없어야 하며,
     * 2005. 9. 6.  이종원 작성
     * @return
     */   
    public static String nextDailyUID6L(){
        synchronized(lock3){
            long var = ((System.currentTimeMillis()/100)%1000000);
                      
            if(var <= lastDailyUid6L){
                if(lastDailyUid6L <dailyUidMaxValue6L ){
                    var = ++lastDailyUid6L;
                }else {
                    var=++dailyUidCycleValue6L;
                    if(dailyUidCycleValue6L == lastDailyUid6L){
                        //처음부터 다시 시작
                        dailyUidCycleValue6L = 0;
                        lastDailyUid6L=0;
                    }
                }
            }else{
                lastDailyUid6L=var;
            }
            return ((1000000+var)+"").substring(1);
        }
    }
    
    private static int dailyUidCycleValue6L = 0;
    private static int dailyUidMaxValue6L = 999999;
    private static long lastDailyUid6L = 0;
    
    private static Object lock=new Object();
    
    private static long lastDailyUid8L=0;
    
    private static long dailyUidMaxValue8L=99999999;
    /**
     * 당일에 한하여 유일한 8자리 문자열을 리턴 한다.
     * 단 시스템 일자를 강제로 미래, 과거로 세팅하는 일이 없어야 하며,
     * 일일 생성 하는 횟수가 9,999,999회 이하 이어야한다.
     * 최소값을 지정하기 위하여는 instanceId와 동일한 property file에 
     * DAILY_UID_MIN_VALUE에 값을 세팅하면 된다.
     * 최대값을 지정하기 위하여는 instanceId와 동일한 property file에 
     * DAILY_UID_MAX_VALUE에 값을 세팅하면 된다.
     *   
     * 2005. 9. 6.  이종원 작성
     * @return
     */
    public static String nextDailyUID8L(){
        synchronized(lock){
            
            getToday();
            
            if(!initialized) init();
            long var = ((System.currentTimeMillis())%100000000);
//            if(var < 1000000){
//                var = var+10000000;
//            }
            var = var+ dailyUidMinValue;
            //System.out.println(var);
//            if(var >=dailyUidMaxValue){
           	if(var >=dailyUidMaxValue8L){
                var=dailyUidMaxValue;
            }
            //System.out.println("-->"+var);
            if(var <= lastDailyUid8L){
                if(lastDailyUid8L <dailyUidMaxValue8L ){
                    var= ++lastDailyUid8L;
                    //System.out.println("aa");
                }else {
                    var=++dailyUidCycleValue8L;
                    if(dailyUidCycleValue8L == lastDailyUid8L){
                        dailyUidCycleValue8L=dailyUidMinValue;
                        lastDailyUid8L=dailyUidMinValue;
                    }
                    
                    //System.out.println("bb");
                }
            }else{
                lastDailyUid8L=var;
                //System.out.println("cc");
            }
            return (var+"");//.substring(1);
        }
    }
    
    
    static int x=0;
    /**
     * 당일에 한하여 유일한 8자리 문자열을 리턴 한다.
     * 단 시스템 일자를 강제로 미래, 과거로 세팅하는 일이 없어야 하며,
     * 일일 생성 하는 횟수가 9,999,999회 이하 이어야한다.
     * 최소값을 지정하기 위하여는 instanceId와 동일한 property file에 
     * DAILY_UID_MIN_VALUE에 값을 세팅하면 된다.
     * 최대값을 지정하기 위하여는 instanceId와 동일한 property file에 
     * DAILY_UID_MAX_VALUE에 값을 세팅하면 된다.
     *   
     * 2005. 9. 6.  이종원 작성
     * @return
     */
    public static String nextDailyUID9L(){
        synchronized(lock){
            String var = nextDailyUID8L();
            return (var+""+ (++x % 10) );
        }
    }
   
    static long dailyUidMinValue;
    static long dailyUidMaxValue;
    
    static long dailyUidCycleValue8L;
    static long dailyUidCycleValue7L;
    
    static boolean initialized=false;
    
    private static void init(){
        todayStr=FormatUtil.getToday("yyMMdd");
        dailyUidMinValue = PropertyManager.getIntProperty(
                "was_"+StartupContext.getInstanceId(),
                "DAILY_UID_MIN_VALUE",10000000);
//        dailyUidMaxValue=dailyUidMinValue+8682634;
        dailyUidMaxValue =-10+ PropertyManager.getLongProperty(
                "was_"+StartupContext.getInstanceId(),
                "DAILY_UID_MAX_VALUE",10000000);
        initUid();
        initialized=true;
    }
    
    private static void initUid(){
        System.out.println("UID를 초기화 합니다.");

        lastDailyUid8L=dailyUidMinValue;
        dailyUidCycleValue8L=dailyUidMinValue;

        lastDailyUid7L=0;
        dailyUidCycleValue7L=0;

        lastDailyUid6L=0;
        dailyUidCycleValue6L = 0;
    }
    
    static String todayStr=FormatUtil.getToday("yyMMdd");
    
    /**
     * 유일한 14자리 문자열을 리턴 한다.(yyMMdd+uid 8자리)
     * 단 시스템 일자를 강제로 미래, 과거로 세팅하는 일이 없어야 하며,
     * 2005. 9. 6.  이종원 작성
     * @return
     */
    public static String nextUID(){
        
        return getToday()+nextDailyUID7L();        
    }
    
    public static String getToday(){
        String today=FormatUtil.getToday("yyMMdd");
        if(today.compareTo(todayStr)>0){
            todayStr=today;
            initUid();
        }
        return today;
    }
    
    /**
     * uuid.properties.xml에서  등록된 key값을 기준으로 uid를 생성한다.
     * 2005. 7. 14.  이종원  작성
     * @param key
     * @param param 필요시에만 전달.
     * @return
     */
    public static String  nextUID(String key,Object[] param){
        
        String dbName=PropertyManager.getProperty(
                "uuid",key+".DB_NAME",null);
        String sql=PropertyManager.getProperty(
                "uuid",key+".UID_SQL");
        DBResultSet rs = DBManager.executePreparedQuery(
                dbName, sql,param);
        if(rs != null && rs.next()){
            return rs.getString(1);
        }else{
            throw new SysException("FRS00040","유효하지 않은 UID KEY:"+key);
        }
    }

    static class UIDTestThread extends Thread{
        public void run(){
            for(int i=0;i<100;i++){
                System.out.println(UIDGenerator.nextDailyUID9L());
            }
        }
    }
    
    static class UIDTestThread2 extends Thread{
        public void run(){
            for(int i=0;i<10;i++){
                System.out.println(UIDGenerator.nextUID());
            }
        }
    }
    
    static class UIDTestThread3 extends Thread{
        public void run(){
            for(int i=0;i<10;i++){
                System.out.println(UIDGenerator.nextDailyUID6L());
            }
        }
    }
    
    public static void main(String[] a){
//        long start=System.currentTimeMillis();
//        for(int i=0;i<10;i++)
//            System.out.println(UIDGenerator.generateUID());
//        long end=System.currentTimeMillis();
//        System.out.println(end-start);
//        
//        start=System.currentTimeMillis();
//        for(int i=0;i<10;i++)
//            System.out.println(UIDGenerator.generateUID2());
//        end=System.currentTimeMillis();
//        System.out.println(end-start);
//        for(int i=0;i<100;i++){
//            new UIDTestThread().start();
//        }
//        try {
//            Thread.sleep(3000);
//            System.out.println("------------------");
//        } catch (InterruptedException e) {
//        }
//        for(int i=0;i<3;i++){
//            new UIDTestThread().start();
//        }
//        try {
//            Thread.sleep(10000);
//            System.out.println("------------------");
//        } catch (InterruptedException e) {
//        }
//        for(int i=0;i<3;i++){
//            new UIDTestThread().start();
//        }
//        
//        for(int i=0;i<100;i++){
//            new UIDTestThread2().start();
//        }
//        try {
//            Thread.sleep(3000);
//            System.out.println("------------------");
//        } catch (InterruptedException e) {
//        }
//        for(int i=0;i<3;i++){
//            new UIDTestThread2().start();
//        }
//        
//        try {
//            Thread.sleep(10000);
//            System.out.println("------------------");
//        } catch (InterruptedException e) {
//        }
//        for(int i=0;i<3;i++){
//            new UIDTestThread2().start();
//        }
//        
//        for(int i=0;i<3;i++){
//            new UIDTestThread3().start();
//        }
//        
//        try {
//            Thread.sleep(10000);
//            System.out.println("------------------");
//        } catch (InterruptedException e) {
//        }
//        for(int i=0;i<3;i++){
//            new UIDTestThread3().start();
//        }
        
        //System.out.println("대량이체UID==>"+nextUID("대량이체", null));
//    	for (int i=0;i<100;i++)
//    	System.out.println(nextDailyUID7L());
//    	
//    	for (int i=0;i<100;i++)
//        	System.out.println(nextDailyUID8L());
    	for (int i=0;i<100;i++)
        	System.out.println(nextDailyUID9L());
    	
    }
}
