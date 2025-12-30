/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
package nebsoa.common.monitor.message;

import java.io.Serializable;

/******************************************************************
 * <pre>
 *  1.설명 
 *  전문 수신 시간, 성공/실패 여부, 수행 시간 등의 모니터링 정보를 차트로 표시하기 위한 데이터를 저장한 클래스
 *  
 *  <p>
 *  기관 코드, 성공 건수, 실패 건수, 평균수행 기간의 정보를 담고 있습니다.
 *  </p>
 *  
 *  2.사용법
 *  
 *  <font color='red'>
 *  3.주의사항
 *  </font>
 * </pre>
 * 
 * @author $Author: cvs $
 * @version 1.2
 ******************************************************************
 ******************************************************************/
public class MessageStatisticalObject implements Serializable, Comparable {

	private static final long serialVersionUID = 8746182520147262739L;

	private int successCount;

	private int failCount;

	private double averageExecuteTime;

	private String info;

	/**
	 * Dummy 생성자
	 */
	public MessageStatisticalObject() {
		this("", 0, 0, 0.0);
	}

	/**
	 * Dummy 생성자
	 * @param info 기관코드
	 */
	public MessageStatisticalObject(String info) {
		this(info, 0, 0, 0.0);
	}

	/**
	 * 생성자
	 * @param info 기관코드
	 * @param successCount 성공건수
	 * @param failCount 실패건수
	 * @param averageExecuteTime 평균수행시간
	 */
	public MessageStatisticalObject(String info, int successCount,
			int failCount, double averageExecuteTime) {
		this.info = info;
		this.successCount = successCount;
		this.failCount = failCount;
		this.averageExecuteTime = averageExecuteTime;
	}

	public double getAverageExecuteTime() {
		return averageExecuteTime;
	}

	public int getFailCount() {
		return failCount;
	}

	public int getSuccessCount() {
		return successCount;
	}

	public String getInfo() {
		return info;
	}

	public int compareTo(Object o) {
		if (o == null)
			return -1;

		if (getClass() != o.getClass())
			return -1;

		return info.compareTo(((MessageStatisticalObject) o).getInfo());
	}
}
