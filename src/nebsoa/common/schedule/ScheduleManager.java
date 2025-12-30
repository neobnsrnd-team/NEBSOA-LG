/*
 * Spider Framework
 *
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 *
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.schedule;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerMetaData;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import nebsoa.common.exception.PropertyException;
import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;
import nebsoa.common.property.PropertyLoader;
import nebsoa.common.startup.StartupContext;
import nebsoa.common.util.PropertyManager;

/*******************************************************************
 * <pre>
 * 1.설명
 * 스케줄을 관리하는 클래스
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
 * $Log: ScheduleManager.java,v $
 * Revision 1.1  2018/01/15 03:39:52  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:11  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.2  2009/11/03 08:31:15  jglee
 * schedule.property파일을 was 인스턴스별로 읽어 오도록 수정
 * 한대의 서버에 여러개의 인스턴스가 실행되기 때문에 인스턴스별로 분리 처리
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
 * Revision 1.1  2007/11/26 08:39:13  안경아
 * *** empty log message ***
 *
 * Revision 1.10  2007/10/12 01:54:37  안경아
 * *** empty log message ***
 *
 * Revision 1.9  2007/10/09 04:17:41  안경아
 * *** empty log message ***
 *
 * Revision 1.8  2007/09/05 06:10:47  안경아
 * *** empty log message ***
 *
 * Revision 1.7  2007/09/05 06:10:28  안경아
 * *** empty log message ***
 *
 * Revision 1.6  2006/10/19 08:15:38  오재훈
 * refresh()메소드에서 스케쥴러 이중 등록 및 이중 스타트 수정
 *
 * Revision 1.5  2006/10/18 02:34:25  최수종
 * *** empty log message ***
 *
 * Revision 1.4  2006/09/12 00:44:42  오재훈
 * 스케쥴 매니저 초기화시 초기 정보 로드.
 * 배치아이디,그룹명,크론표현식,클래스명으로 스케쥴에 등록하는 메소드 추가.
 *
 * Revision 1.3  2006/06/17 10:37:45  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class ScheduleManager {

    /** singleton 구현을 위한 자신의 instance */
    private static ScheduleManager instance = new ScheduleManager();


    /** Quartz Scheduler 를 사용하기 위한 Scheduler */
    private Scheduler scheduler;

    /**
     * Singleton 구현을 위한 private constructor
     *
     */
    private ScheduleManager() {
        init();
    }//end of constructor

    /**
     * ScheduleManager 의 인스턴스를 리턴한다.
     *
     * @return ScheduleManager 의 인스턴스
     */
    public static final ScheduleManager getInstance() {
        return instance;
    }//end of getInstance()

    /**
     * 스케줄러를 초기화 합니다.
     *
     */
    private synchronized void init() {
        if (scheduler != null && !isShutdown()) {
            unregistAll();
            shutdown();
        }//end if
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();

            registAllSchedulesInConfiguration();
            start();
        } catch (SchedulerException e) {
            throw new SysException("\t##### Can not initialize ScheduleManager. [" + e.getMessage() + "]");
        }//end try catch
    }//end init()

    /**
     * 스케줄러를 새롭게 합니다.
     *
     */
    public synchronized void refresh() {
        init();
//        registAllSchedulesInConfiguration();
//        start();
    }//end of refresh()

    /**
     * 주어진 키값에 해당하는 스케줄을 등록합니다.
     *
     * @param propertyKey 스케줄을 식별하기 위한 키
     */
    public synchronized void regist(String propertyKey) {
        try {
      //      String jobClass = PropertyManager.getProperty(ScheduleConstants.SCHEDULER_CONFIG_FILE, propertyKey + ScheduleConstants.JOB_CLASS_SUFFIX);
      //      String cronExpression = PropertyManager.getProperty(ScheduleConstants.SCHEDULER_CONFIG_FILE, propertyKey + ScheduleConstants.CRON_EXPRESSION_SUFFIX);
            String jobClass = PropertyManager.getProperty(ScheduleConstants.SCHEDULER_CONFIG_FILE+"_"+StartupContext.getInstanceId(), propertyKey + ScheduleConstants.JOB_CLASS_SUFFIX);
            String cronExpression = PropertyManager.getProperty(ScheduleConstants.SCHEDULER_CONFIG_FILE+"_"+StartupContext.getInstanceId(), propertyKey + ScheduleConstants.CRON_EXPRESSION_SUFFIX);

            /*
             * - JobDetail 생성
             *
             * JobDetail 생성 시의 argument 는 다음과 같다.
             *
             * 1. Job Name
             * 2. Job Group Name
             * 3. Job Class
             */
            JobDetail job = new JobDetail(propertyKey, propertyKey, Class.forName(jobClass));

            /*
             * - CronTrigger 생성
             *
             * Trigger 생성 시의 argument 는 다음과 같다.
             *
             * 1. Trigger Name
             * 2. Trigger Group Name
             * 3. Job Name
             * 4. Job Group Name
             * 5. Cron Expression
             */
            Trigger trigger = new CronTrigger(propertyKey, propertyKey, propertyKey, propertyKey, cronExpression);
            // 아래는 trigger 가 misfire 되었을 경우에 대한 처리를 설정한다.
            trigger.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);

            // 스케줄을 스케줄러에게 등록한다.
            scheduler.scheduleJob(job, trigger);

        } catch (PropertyException e) {
        	LogManager.info("\t##### ScheduleManager.regist() PropertyException [" + e.getMessage() + "].");
            throw new SysException(e.getMessage());
        } catch (SchedulerException e) {
        	LogManager.info("\t##### ScheduleManager.regist() SchedulerException [" + e.getMessage() + "].");
            throw new SysException(e.getMessage());
        } catch (ClassNotFoundException e) {
        	LogManager.info("\t##### ScheduleManager.regist() ClassNotFoundException [" + e.getMessage() + "].");
            throw new SysException(e.getMessage());
        } catch (ParseException e) {
        	LogManager.info("\t##### ScheduleManager.regist() ParseException [" + e.getMessage() + "].");
            throw new SysException(e.getMessage());
        }//end try catch

        LogManager.info("\t##### Successfully regist schedule [" + propertyKey + "].");
    }//end of regist()



    /**
     * 스케쥴 키, 스케쥴 그룹 명 , 실행할 클래스 명 , 크론 표현식으로 스케쥴 등록
     * 2006-09-09 jhoh 추가
     * @param scheduleKey : 스케쥴에 등록할 키
     * @param groupName : 실행 할 스케쥴 그룹 명
     * @param className : 실행 할 클래스 명
     * @param cronExpression : 실행주기(크론표현식)
     */
    public synchronized void regist(String scheduleKey, String groupName , String className , String cronExpression) {
        try {

            /*
             * - JobDetail 생성. JobDetail 생성 시의 argument 는 다음과 같다.
             * new JobDetail(Job Name, Job Group Name, Job Class);
             */
            JobDetail job = new JobDetail(scheduleKey, groupName, Class.forName(className));

            /*
             * - CronTrigger 생성. Trigger 생성 시의 argument 는 다음과 같다.
             * new CronTrigger(Trigger Name, Trigger Group Name, Job Name, Job Group Name, Cron Expression);
             */
            Trigger trigger = new CronTrigger(scheduleKey, groupName, scheduleKey, groupName, cronExpression);
            // 아래는 trigger 가 misfire 되었을 경우에 대한 처리를 설정한다.
            trigger.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);

            // 스케줄을 스케줄러에게 등록한다.
            scheduler.scheduleJob(job, trigger);

        } catch (PropertyException e) {
        	e.printStackTrace();
            throw new SysException(e.getMessage());
        } catch (SchedulerException e) {
        	e.printStackTrace();
        	throw new SysException(e.getMessage());
        } catch (ClassNotFoundException e) {
        	e.printStackTrace();
        	throw new SysException(e.getMessage());
        } catch (ParseException e) {
        	e.printStackTrace();
        	throw new SysException(e.getMessage());
        }//end try catch

        LogManager.info("\t##### Successfully regist scheduleKey = " + scheduleKey + " , className = "+className);
    }//end of regist()


    /**
     * configuration file (schedule.properties.xml) 에 정의된 모든 스케줄을 등록합니다.
     *
     */
    public synchronized void registAllSchedulesInConfiguration() {
        LogManager.info("\t##### Regist all schedules in configuration file.");
        Iterator schedules = null;
        try {
            schedules = loadAllSchedules();
        } catch (PropertyException e) {
            throw new SysException(e.getMessage());
        }//end try catch

        for ( ; schedules.hasNext(); ) {
            regist((String)schedules.next());
        }//end for
        LogManager.info("\t##### Successfully regist all schedules in configuration file.");
    }//end of registAllSchedulesInConfiguration()

    /**
     * configuration file 로부터 스케줄 리스트를 로드하여 리턴합니다.
     *
     * @return Iterator 형태의 로드된 스케줄 리스트
     */
    private Iterator loadAllSchedules() {
        PropertyLoader prop = null;
    //    prop = PropertyManager.getPropertyLoader(ScheduleConstants.SCHEDULER_CONFIG_FILE);
        prop = PropertyManager.getPropertyLoader(ScheduleConstants.SCHEDULER_CONFIG_FILE
        		+ "_"+StartupContext.getInstanceId());

        Map scheduleList = new HashMap();
        Iterator iter = prop.getPropertyCache().keySet().iterator();
        while (iter.hasNext()) {
            String key = (String)iter.next();
            if (key != null && key.endsWith(ScheduleConstants.JOB_CLASS_SUFFIX)) {
                scheduleList.put(key.substring(0, key.lastIndexOf(ScheduleConstants.JOB_CLASS_SUFFIX)), null);
            } else if (key != null && key.endsWith(ScheduleConstants.CRON_EXPRESSION_SUFFIX)) {
                scheduleList.put(key.substring(0, key.lastIndexOf(ScheduleConstants.CRON_EXPRESSION_SUFFIX)), null);
            }//end if
        }//end for
        return scheduleList.keySet().iterator();
    }//end of loadAllSchedules()

    /**
     * 등록된 스케줄을 해지 시킵니다.
     *
     * @param propertyKey 스케줄을 식별하기 위한 키
     */
    public synchronized void unregist(String propertyKey) {
        try {
            scheduler.deleteJob(propertyKey, propertyKey);
            LogManager.info("\t##### Successfully unregist schedule [" + propertyKey + "].");
        } catch (SchedulerException e) {
            throw new SysException("\t##### Can not unregist schedule, [" + propertyKey + "]. [" + e.getMessage() + "]");
        }//end try catch
    }//end of unregist()

    /**
     * 등록된 스케줄을 해지 시킵니다.
     *
     * @param propertyKey 스케줄을 식별하기 위한 키
     * @param groupName 스케줄 그룹
     */
    public synchronized void unregist(String propertyKey, String groupName) {
        try {
            scheduler.deleteJob(propertyKey, groupName);
            LogManager.info("\t##### Successfully unregist schedule ["+groupName+"][" + propertyKey + "].");
        } catch (SchedulerException e) {
            throw new SysException("\t##### Can not unregist schedule, ["+groupName+"][" + propertyKey + "]. [" + e.getMessage() + "]");
        }//end try catch
    }//end of unregist()

    /**
     * 등록된 모든 스케줄을 해지 시킵니다.
     *
     */
    public synchronized void unregistAll() {
        String [] groups;
        try {
        	groups = scheduler.getJobGroupNames();

            for (int i = 0; i < groups.length; i++) {
            	String [] jobs = scheduler.getJobNames(groups[i]);
            	for(int j=0; j<jobs.length; j++){
            		unregist(jobs[j], groups[i]);
            	}
            }//end for
            LogManager.info("\t##### Completely unregist all schedules.");
        } catch (SchedulerException e) {
            throw new SysException("\t##### Can not unregist all schedules. [" + e.getMessage() + "]");
        }//end try catch

    }//end of unregistAll()

    /**
     * 등록된 스케줄이 shutdown 되었는지를 리턴합니다.
     *
     * @return shutdown 여부
     */
    public synchronized boolean isShutdown() {
        if (this.scheduler == null) {
            return true;
        }//end if
        try {
            SchedulerMetaData metadata = scheduler.getMetaData();
            return metadata.isShutdown();
        } catch (SchedulerException e) {
            throw new SysException(e.getMessage());
        }//end try catch
    }//end of isShutdown()

    /**
     * 스케줄러를 shutdown 시킵니다.
     * 모든 등록된 스케줄이 shutdown 됩니다.
     *
     */
    public synchronized void shutdown() {
        try {
            scheduler.shutdown();
            LogManager.info("\t##### Successfully shutdown schedule.");
        } catch (SchedulerException e) {
            throw new SysException("\t##### Can not shutdown schedule. [" + e.getMessage() + "]");
        }//end try catch
    }//end of shutdown()

    /**
     * 등록된 스케줄이 start 되었는지를 리턴합니다.
     *
     * @param propertyKey 스케줄을 식별하기 위한 키
     * @return shutdown 여부
     */
    public synchronized boolean isStarted(String propertyKey) {
        try {
            SchedulerMetaData metadata = scheduler.getMetaData();

            return metadata.isStarted();
        } catch (SchedulerException e) {
            throw new SysException(e.getMessage());
        }//end try catch
    }//end of isStarted()

    /**
     * 등록된 스케줄을 start 시킵니다.
     *
     * @param propertyKey 스케줄을 식별하기 위한 키
     */
    public synchronized void start() {
        try {
            scheduler.start();
            LogManager.info("\t##### Successfully start scheduler.");
        } catch (SchedulerException e) {
            throw new SysException("\t##### Can not start scheduler. [" + e.getMessage() + "]");
        }//end try catch
    }//end of start()

    /**
     * 스케줄러가 pause 되었는지를 리턴합니다.
     *
     * @return pause 여부
     */
    public synchronized boolean isPaused() {
        try {
            return scheduler.isPaused();
        } catch (SchedulerException e) {
            throw new SysException(e.getMessage());
        }//end try catch
    }//end of isPaused()

    /**
     * 등록된 스케줄을 pause 시킵니다.
     *
     * @param propertyKey 스케줄을 식별하기 위한 키
     */
    public synchronized void pause(String propertyKey) {
        try {
            scheduler.pauseJob(propertyKey, propertyKey);
            LogManager.info("\t##### Completely pause schedule, [" + propertyKey + "].");
        } catch (SchedulerException e) {
            throw new SysException("\t##### Can not pause schedule, [" + propertyKey + "]. [" + e.getMessage() + "]");
        }//end try catch
    }//end of pause()

    /**
     * 등록된 스케줄을 pause 시킵니다.
     *
     * @param propertyKey 스케줄을 식별하기 위한 키
     */
    public synchronized void pause(String propertyKey, String groupName) {
        try {
            scheduler.pauseJob(propertyKey, groupName);
            LogManager.info("\t##### Completely pause schedule, [" + groupName + "][" + propertyKey + "].");
        } catch (SchedulerException e) {
            throw new SysException("\t##### Can not pause schedule, [" + groupName + "][" + propertyKey + "]. [" + e.getMessage() + "]");
        }//end try catch
    }//end of pause()

    /**
     * 등록된 모든 스케줄을 pause 시킵니다.
     *
     */
    public synchronized void pauseAll() {
        try {
            scheduler.pauseAll();
            LogManager.info("\t##### Completely pause all schedules.");
        } catch (SchedulerException e) {
            throw new SysException("\t##### Can not pause all schedules. [" + e.getMessage() + "]");
        }//end try catch
    }//end of pauseAll()

    /**
     * 등록된 스케줄을 resume 시킵니다.
     *
     * @param propertyKey 스케줄을 식별하기 위한 키
     */
    public synchronized void resume(String propertyKey, String groupName) {
        try {
            scheduler.resumeJob(propertyKey, groupName);
            LogManager.info("\t##### Completely resume schedule, [" + propertyKey + "].");
        } catch (SchedulerException e) {
            throw new SysException("\t##### Can not resume schedule, [" + propertyKey + "]. [" + e.getMessage() + "]");
        }//end try catch
    }//end of resume()
    /**
     * 등록된 스케줄을 resume 시킵니다.
     *
     * @param propertyKey 스케줄을 식별하기 위한 키
     */
    public synchronized void resume(String propertyKey) {
        try {
            scheduler.resumeJob(propertyKey, propertyKey);
            LogManager.info("\t##### Completely resume schedule, [" + propertyKey + "].");
        } catch (SchedulerException e) {
            throw new SysException("\t##### Can not resume schedule, [" + propertyKey + "]. [" + e.getMessage() + "]");
        }//end try catch
    }//end of resume()
    /**
     * 등록된 모든 스케줄을 resume 시킵니다.
     *
     */
    public synchronized void resumeAll() {
        try {
            scheduler.resumeAll();
            LogManager.info("\t##### Completely resume all schedules.");
        } catch (SchedulerException e) {
            throw new SysException("\t##### Can not resume all schedules. [" + e.getMessage() + "]");
        }//end try catch
    }//end of resumeAll()

    public static void main(String [] args) throws Exception {
        ScheduleManager sm = ScheduleManager.getInstance();

        Iterator schedules = sm.loadAllSchedules();

        System.out.println("=============== Load All Schedules ===============");

        for ( ; schedules.hasNext(); ) {
            System.out.println(schedules.next());
        }//end for

        System.out.println("=============== Regist All Schedules ===============");
        sm.regist("SAMPLE");


//        sm.registAllSchedulesInConfiguration();
        sm.start();

        Thread.sleep(1000);

        sm.regist("SAMPLE2");

        sm.pause("SAMPLE");

//        sm.pause("SAMPLE2");

        Thread.sleep(15000);

        sm.resume("SAMPLE");
        sm.pause("SAMPLE2");

        Thread.sleep(15000);

        sm.resume("SAMPLE2");

        Thread.sleep(15000);

        sm.unregistAll();

        sm.shutdown();

    }//end of main()

}// end of ScheduleManager.java