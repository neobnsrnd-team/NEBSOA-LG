/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package test.monitor;

import nebsoa.common.log.LogManager;
import nebsoa.common.monitor.SysMonitorReportMaker;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 시스템에서 관리되고 있는 모니터링 데이터를 Report로 생성하는 클래스TEST
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
 * $Log: SysMonitorReportMakerTest.java,v $
 * Revision 1.1  2018/01/15 03:39:54  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:37  cvs
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
 * Revision 1.1  2008/08/04 08:54:54  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.2  2008/02/20 00:42:49  오재훈
 * *** empty log message ***
 *
 * Revision 1.1  2007/11/26 08:38:53  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2007/09/17 04:40:59  이종원
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class SysMonitorReportMakerTest {
	
	/**
	 * test of SysMonitor class
	 * @param args
	 */
	public static void main(String[] args) {
//		for(int i=0;i<10;i++){
//			MonitorData test1 = new MonitorData("Database","SELECT * FROM EMP"+(i%5),i*1);
//			SysMonitor.addMonitorData(test1);
//		}
//		for(int i=0;i<100;i++){
//			MonitorData test1 = new MonitorData("Message","SELECT * FROM TEST"+(i%10),i*100);
//			SysMonitor.addMonitorData(test1);
//		}
//		
//		System.out.println(SysMonitor.getHtmlReport("Database","EMP",0));	
//        
//        System.out.println(SysMonitor.getHtmlReport("Message","TEST",0));  
//		
//		Iterator i = SysMonitor.getCategoryList();
//		while(i.hasNext()){
//			System.out.println(i.next()+"-->key");
//		}
        
      for(int i=0;i<10;i++){
          if(SysMonitorReportMaker.isReportCreateTime()){
              //report생성 로직 수행 -->동시 수행 이슈 없도록 singleton and  synronize...
              LogManager.debug("\t--------------->가짜로 report생성 완료");
          }
          try {
            Thread.sleep(30*1000);
        } catch (InterruptedException e) {
        }
      }
  
	}
	

}
