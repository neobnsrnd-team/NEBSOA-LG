/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package test.monitor;

import nebsoa.monitor.client.MonitorAgentManager;


public class MonitorTest {
	
	public static void main(String[] args) {
		// 전문처리 데모 데이타 생성
		new MessageDemo().start();
		
		// 모니터링 데이타 전송
		MonitorAgentManager.getInstance().init();
	}
}
