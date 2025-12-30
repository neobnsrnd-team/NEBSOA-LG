/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.management.ejb;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

import nebsoa.common.exception.ManagementException;
import nebsoa.common.exception.TestException;
import nebsoa.common.monitor.message.MessageStatisticalObject;
import nebsoa.common.util.DataMap;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 원격으로 객체들을 관리하기 위한 ManagementAgent 실행을 대리하는 Processor EJB
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
 * $Log: ManagementProcessor.java,v $
 * Revision 1.1  2018/01/15 03:39:50  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:40  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:24  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:27  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.2  2008/10/16 01:58:54  김성균
 * 필요없는 Exception 던지는 부분삭제
 *
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.3  2008/04/07 00:34:16  오재훈
 * 모니터링 정보 추가(외환은행버전과 병합)
 *
 * Revision 1.2  2008/03/20 06:07:30  김성균
 * 성능모니터링 데이타를 ArrayList로 가져오는 기능 추가
 *
 * Revision 1.1  2008/01/22 05:58:34  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:39:12  안경아
 * *** empty log message ***
 *
 * Revision 1.10  2007/09/17 05:51:17  안경아
 * *** empty log message ***
 *
 * Revision 1.9  2007/07/16 05:36:56  오재훈
 * 배치 종료 추가
 *
 * Revision 1.8  2007/07/04 05:05:32  오재훈
 * 실행중 배치 원격 명령 관련
 *
 * Revision 1.7  2007/06/06 10:12:03  최수종
 * 성능모니터링 결과를 ArrayList형식으로 리턴하는 메소드 추가
 *
 * Revision 1.6  2007/01/15 08:43:59  안경아
 * *** empty log message ***
 *
 * Revision 1.5  2006/10/10 08:27:27  김성균
 * *** empty log message ***
 *
 * Revision 1.4  2006/10/08 07:28:27  김성균
 * *** empty log message ***
 *
 * Revision 1.3  2006/09/28 05:30:27  김성균
 * *** empty log message ***
 *
 * Revision 1.2  2006/07/26 08:00:41  김성균
 * 호출인자를 DataMap만 받도록 수정
 *
 * Revision 1.1  2006/07/21 07:36:54  김성균
 * 최초 등록
 *
 * </pre>
 ******************************************************************/
public interface ManagementProcessor {
	
    /**
     * ManagementAgent의 수행을 호출합니다.
     */
    public DataMap doProcess(DataMap map) throws RemoteException, ManagementException;
    
    /**
     * BatchLogAgent 수행을 호출합니다.
     */
    public DataMap getBatchLogInfo(DataMap map) throws RemoteException;
    
    /**
     * TestAgent의 수행을 호출합니다.
     */
    public DataMap doTest(DataMap map) throws RemoteException, TestException;
    
    /**
     * MonitorAgent의 수행을 호출합니다.
     */
    public String getHtmlReport(DataMap map) throws RemoteException;
    
    /**
     * MonitorAgent의 수행을 호출합니다.
     */
    public ArrayList getMonitorDatas(DataMap map) throws RemoteException;
    
    /**
     * MonitorAgent의 수행을 호출합니다.
     */
    public Object[] getCategorys() throws RemoteException;
    
    /**
     * MonitorAgent의 수행을 호출합니다.
     */
    public MessageStatisticalObject[] getAllStatisticalObject() throws RemoteException;
    
	/**
     * 모니터링 결과의 ArrayList 객체로 리턴합니다.
     * 
     * @return ArrayList
     */
    public ArrayList getObjectReport(DataMap map) throws RemoteException;
    
    /**
     * Monitor data clear 수행을 호출합니다.
     */
    public String doMonitorClear() throws RemoteException;

    /**
     * 모니터링 결과의 ArrayList 객체로 리턴합니다.
     * 
     * @return ArrayList
     */
    public HashMap getContextMonitorData(DataMap map) throws RemoteException;
    
    /**
     * 실행 중인 배치 목록을 가져옵니다.
     * @return
     * @throws RemoteException
     */
    public ArrayList getExecutingBatchList() throws RemoteException;

    /**
     * 실행중인 배치 프로그램의 정지 플래그를 셋팅합니다.
     * @param map
     * @return
     * @throws RemoteException
     */
    public DataMap doStopBatch(DataMap map) throws RemoteException;
    
    
    /**
     * 배치 프로그램 수동 실행
     * @param map
     * @return
     * @throws RemoteException
     */
    public void doForceStart(DataMap map) throws RemoteException;

    /**
     * Context 모니터링 저장할 결과를 가져온다.(가져온후 clear)
     */
    public HashMap getSaveContextMonitorData() throws RemoteException;
    
    /**
     * Context 모니터링 결과의 HashMap 객체로 리턴합니다.
     * 
     * @return HashMap
     */
    public HashMap getWorkingThreadData(String threshold) throws RemoteException;   

    /**
     * 프로세스를 강제 종료 시킵니다.
     * 
     * @return HashMap
     */
    public String setLogEnabled(String trxId) throws RemoteException; 
    
    /**
     * 프로세스를 강제 종료 시킵니다.
     * 
     * @return HashMap
     */
    public String setLogDisabled(String trxId) throws RemoteException; 
    
    /**
     * Context 모니터링 ToString 결과를 String 객체로 리턴합니다.
     * 
     * @return HashMap
     */
    public String getContextMonitorToStringData(String trxId) throws RemoteException;   
    
    /**
     * 프로세스를 강제 종료 시킵니다.
     * 
     * @return HashMap
     */
    public String stopContextProcess(String trxId) throws RemoteException;       
    
}
