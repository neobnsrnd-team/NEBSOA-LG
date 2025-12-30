/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.schedule;

import java.util.Date;

/*******************************************************************
 * <pre>
 * 1.설명 
 * Job 에 대한 설정을 포함하는 객체
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
 * $Log: JobContext.java,v $
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
public class JobContext {
	
	/**
	 * 실행시각
	 */
	private Date fireTime;
	/**
	 * 이전 실행시각
	 */
	private Date previousFireTime;
	/**
	 * 다음 실행시각
	 */
	private Date nextFireTime;
	
	/**
	 * Job 이름
	 */
	private String jobName;
	/**
	 * Job 클래스
	 */
	private Class jobClass;
	/**
	 * Job 설명
	 */
	private String description;
	/**
	 * description 의 값을 리턴합니다.
	 * 
	 * @return description 의 값
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * description 에 값을 세팅합니다.
	 * 
	 * @param description description 에 값을 세팅하기 위한 인자 값
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * fireTime 의 값을 리턴합니다.
	 * 
	 * @return fireTime 의 값
	 */
	public Date getFireTime() {
		return fireTime;
	}
	/**
	 * fireTime 에 값을 세팅합니다.
	 * 
	 * @param fireTime fireTime 에 값을 세팅하기 위한 인자 값
	 */
	public void setFireTime(Date fireTime) {
		this.fireTime = fireTime;
	}
	/**
	 * jobClass 의 값을 리턴합니다.
	 * 
	 * @return jobClass 의 값
	 */
	public Class getJobClass() {
		return jobClass;
	}
	/**
	 * jobClass 에 값을 세팅합니다.
	 * 
	 * @param jobClass jobClass 에 값을 세팅하기 위한 인자 값
	 */
	public void setJobClass(Class jobClass) {
		this.jobClass = jobClass;
	}
	/**
	 * jobName 의 값을 리턴합니다.
	 * 
	 * @return jobName 의 값
	 */
	public String getJobName() {
		return jobName;
	}
	/**
	 * jobName 에 값을 세팅합니다.
	 * 
	 * @param jobName jobName 에 값을 세팅하기 위한 인자 값
	 */
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	/**
	 * nextFireTime 의 값을 리턴합니다.
	 * 
	 * @return nextFireTime 의 값
	 */
	public Date getNextFireTime() {
		return nextFireTime;
	}
	/**
	 * nextFireTime 에 값을 세팅합니다.
	 * 
	 * @param nextFireTime nextFireTime 에 값을 세팅하기 위한 인자 값
	 */
	public void setNextFireTime(Date nextFireTime) {
		this.nextFireTime = nextFireTime;
	}
	/**
	 * previousFireTime 의 값을 리턴합니다.
	 * 
	 * @return previousFireTime 의 값
	 */
	public Date getPreviousFireTime() {
		return previousFireTime;
	}
	/**
	 * previousFireTime 에 값을 세팅합니다.
	 * 
	 * @param previousFireTime previousFireTime 에 값을 세팅하기 위한 인자 값
	 */
	public void setPreviousFireTime(Date previousFireTime) {
		this.previousFireTime = previousFireTime;
	}

}// end of JobContext.java