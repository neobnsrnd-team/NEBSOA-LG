/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.schedule;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import nebsoa.common.log.LogManager;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 스케줄러에 의해서 실행되는 Quartz 의 Job 을 구현한 클래스
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
 * $Log: SimpleJob.java,v $
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
 * Revision 1.1  2007/11/26 08:39:13  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2006/06/17 10:37:45  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public abstract class SimpleJob implements Job {

    /**
     * 스케줄러에 의해서 실행되는 실제 구현부를 포함하는 메소드
     * configuration 에 정의된 클래스를 실행시킵니다.
     * 
     * @param context 스케줄러 실행에 필요한 정보를 포함하는 JobExecutionContext
     * @throws org.quartz.JobExecutionException 스케줄러 실행 중 발생하는 Exception
     * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
     */
    public void execute(JobExecutionContext context) throws JobExecutionException {
        
        try {
        	JobContext jContext = new JobContext();
        	
        	jContext.setJobName(context.getJobDetail().getName());
        	jContext.setJobClass(context.getJobDetail().getJobClass());
        	jContext.setDescription(context.getJobDetail().getDescription());
        	
        	jContext.setPreviousFireTime(context.getPreviousFireTime());
        	jContext.setFireTime(context.getFireTime());
        	jContext.setNextFireTime(context.getNextFireTime());
        	
        	
            execute(jContext);
        } catch (Exception e) {
            LogManager.error("\t##### 다음의 이유로 스케줄 실행 중 오류가 발생하였습니다. [" + e.getMessage() + "]");
        }//end try catch
        
    }//end of execute()
    
    /**
     * 스케줄러에 의해서 실행될 내역을 기술합니다.
     * 
     * (본 메소드는 SimpleJob 클래스의 execute(JobExecutionContext context) 메소드에 의해서 호출됩니다.)
     * 
     * @param context Job 에 대한 설정을 포함하는 JobContext 객체
     *
     */
    public void execute(JobContext context) {
    	execute();
    }//end of execute()
    
    /**
     * 스케줄러에 의해서 실행될 내역을 기술합니다.
     * 
     * 이전 버전과의 호환성을 위해서 남겨 놓았습니다.
     * 
     * (본 메소드는 SimpleJob 클래스의 execute(JobContext context) 메소드에 의해서 호출됩니다.)
     */
    public void execute() {    	
    }//end of execute()

}// end of SimpleJob.java