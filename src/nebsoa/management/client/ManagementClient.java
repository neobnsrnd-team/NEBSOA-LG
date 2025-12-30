/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.management.client;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import nebsoa.common.exception.ManagementException;
import nebsoa.common.exception.SysException;
import nebsoa.common.exception.TestException;
import nebsoa.common.log.LogManager;
import nebsoa.common.monitor.message.MessageStatisticalObject;
import nebsoa.common.util.DataMap;
import nebsoa.common.util.PropertyManager;
import nebsoa.management.adapter.EJBManagementAdapter;
import nebsoa.management.adapter.ManagementAdapter;
import nebsoa.management.adapter.SocketManagementAdapter;

/*******************************************************************
 * <pre>
 * 1.설명 
 * ManagermentAgent 호출을 대리해 주기 위한 Client
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
 * $Log: ManagementClient.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:34  cvs
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
 * Revision 1.2  2008/10/16 02:00:14  김성균
 * SOCKET 으로도 사용가능하도록 기능 추가 : delegator 및 Adapter 패턴 적용
 *
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.4  2008/04/07 10:34:13  김희원
 * doForceStart 메소드 수정
 *
 * Revision 1.3  2008/04/07 00:34:16  오재훈
 * 모니터링 정보 추가(외환은행버전과 병합)
 *
 * Revision 1.2  2008/03/20 06:07:30  김성균
 * 성능모니터링 데이타를 ArrayList로 가져오는 기능 추가
 *
 * Revision 1.1  2008/01/22 05:58:35  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:39:16  안경아
 * *** empty log message ***
 *
 * Revision 1.13  2007/09/17 05:51:17  안경아
 * *** empty log message ***
 *
 * Revision 1.12  2007/07/16 05:36:45  오재훈
 * 배치 종료 추가
 *
 * Revision 1.11  2007/07/04 05:05:32  오재훈
 * 실행중 배치 원격 명령 관련
 *
 * Revision 1.10  2007/06/06 10:12:03  최수종
 * 성능모니터링 결과를 ArrayList형식으로 리턴하는 메소드 추가
 *
 * Revision 1.9  2007/01/30 04:39:55  김성균
 * 예외처리 부분 수정
 *
 * Revision 1.8  2007/01/15 08:43:59  안경아
 * *** empty log message ***
 *
 * Revision 1.7  2006/11/07 11:04:16  김성균
 * *** empty log message ***
 *
 * Revision 1.6  2006/10/21 08:46:54  김성균
 * *** empty log message ***
 *
 * Revision 1.5  2006/10/10 08:27:17  김성균
 * *** empty log message ***
 *
 * Revision 1.4  2006/10/08 07:28:19  김성균
 * *** empty log message ***
 *
 * Revision 1.3  2006/09/28 05:31:29  김성균
 * *** empty log message ***
 *
 * Revision 1.2  2006/07/26 08:03:23  김성균
 * 호출인자로 DataMap만 받도록 수정
 *
 * Revision 1.1  2006/07/21 07:36:54  김성균
 * 최초 등록
 *
 * </pre>
 ******************************************************************/
public class ManagementClient {
	
    private static final String COMMUNICATION_METHOD_EJB = "EJB";
    private static final String COMMUNICATION_METHOD_SOCKET = "SOCKET";
    
    private static ManagementAdapter delegator;
    
    static {
        String protocol = PropertyManager.getProperty("management", "COMMUNICATION_METHOD", COMMUNICATION_METHOD_EJB);
        if (COMMUNICATION_METHOD_EJB.equals(protocol)) {
            delegator = new EJBManagementAdapter();
        } else if (COMMUNICATION_METHOD_SOCKET.equals(protocol)) {
            delegator = new SocketManagementAdapter();
        } else {
            LogManager.error("management.properties.xml 파일의 COMMUNICATION_METHOD 설정정보 오류입니다.");
        }
    }
    
    private static ManagementAdapter getManagementAdapter() {
        if (delegator == null) {
            throw new SysException("ManagementAdapter is null");
        }
        return delegator;
    }
    
    /**
     * ManagermentAgent를 호출합니다.
     * 
     * @return Object 실행이 완료된 결과를 담고 있는 객체
     * @throws ManagementException 
     */
    public static DataMap doProcess(DataMap map) throws ManagementException {
        return getManagementAdapter().doProcess(map);
    }
        
    public static DataMap doProcess(String wasConfigId, DataMap map) throws ManagementException {     
        return getManagementAdapter().doProcess(wasConfigId, map);
    }
    
    /**
     * TestAgent를 호출합니다.
     * 
     * @return Object 실행이 완료된 결과를 담고 있는 객체
     * @throws TestException 
     */
    public static DataMap doTest(DataMap map) throws TestException {
        return getManagementAdapter().doTest(map);
    }
        
    public static DataMap doTest(String wasConfigId, DataMap map) throws TestException {     
        return getManagementAdapter().doTest(wasConfigId, map);
    }
    
    /**
     * BatchLogAgent를 호출합니다.
     * 
     * @return Object 실행이 완료된 결과를 담고 있는 객체
     * @throws TestException 
     */
    public static DataMap getBatchLogInfo(DataMap map) {
        return getManagementAdapter().getBatchLogInfo(map);
    }
        
    public static DataMap getBatchLogInfo(String wasConfigId, DataMap map) 
            throws ManagementException, FileNotFoundException {     
        
        return getManagementAdapter().getBatchLogInfo(wasConfigId, map);
    }
    
    /**
     * MonitorAgent를 호출합니다.
     * 
     * @return Object 실행이 완료된 결과를 담고 있는 객체
     */
    public static String getHtmlReport(DataMap map) {
        return getManagementAdapter().getHtmlReport(map);
    }
        
    public static String getHtmlReport(String wasConfigId, DataMap map) {     
        return getManagementAdapter().getHtmlReport(wasConfigId, map);
    }
    
    /**
     * MonitorAgent를 호출합니다.
     * 
     * @return Object 실행이 완료된 결과를 담고 있는 객체
     */
    public static ArrayList getMonitorDatas(DataMap map) {
        return getManagementAdapter().getMonitorDatas(map);
    }
        
    public static ArrayList getMonitorDatas(String wasConfigId, DataMap map) {     
        return getManagementAdapter().getMonitorDatas(wasConfigId, map);
    }
    
    
    /**
     * MonitorAgent를 호출합니다.
     * 
     * @return Object 실행이 완료된 결과를 담고 있는 객체
     */
    public static Object[] getCategorys() {
        return getManagementAdapter().getCategorys();
    }
        
    public static Object[] getCategorys(String wasConfigId) {     
        return getManagementAdapter().getCategorys(wasConfigId);
    }
    
    public static String doMonitorClear(){
        return getManagementAdapter().doMonitorClear();	
    }
    
    public static String doMonitorClear(String wasConfigId) {     
        return getManagementAdapter().doMonitorClear(wasConfigId);	
    }
    
    /**
     * MonitorAgent를 호출합니다.
     * 
     * @return Object 실행이 완료된 결과를 담고 있는 객체
     */
    public static MessageStatisticalObject[] getAllStatisticalObject() {
        return getManagementAdapter().getAllStatisticalObject();
    }
        
    public static MessageStatisticalObject[] getAllStatisticalObject(String wasConfigId) {     
        return getManagementAdapter().getAllStatisticalObject(wasConfigId);
    }
        
    public static HashMap getContextMonitorData(DataMap map) {
        return getManagementAdapter().getContextMonitorData(map);
    }
        
    public static HashMap getContextMonitorData(String wasConfigId, DataMap map) {     
        return getManagementAdapter().getContextMonitorData(wasConfigId, map);
    }

    /**
     * MonitorAgent를 호출합니다.
     * 
     * @return Object 실행이 완료된 결과를 담고 있는 객체
     */
    public static ArrayList getObjectReport(DataMap map) {
        return getManagementAdapter().getObjectReport(map);
    }
        
    public static ArrayList getObjectReport(String wasConfigId, DataMap map) {     
        return getManagementAdapter().getObjectReport(wasConfigId, map);
    }
    
    /**
     * ExecutingBatchAgent를 호출합니다.
     * 
     * @return map
     * @throws TestException 
     */
    public static ArrayList getExecutingBatchList(DataMap map) {
        return getManagementAdapter().getExecutingBatchList(map);
    }
        
    public static ArrayList getExecutingBatchList(String wasConfigId, DataMap map) 
            throws ManagementException, FileNotFoundException {     
        
        return getManagementAdapter().getExecutingBatchList(wasConfigId, map);
    }
    
    /**
     * ExecutingBatchAgent를 호출합니다.
     * 
     * @return map
     * @throws TestException 
     */
    public static DataMap doStopBatch(DataMap map) {
        return getManagementAdapter().doStopBatch(map);
    }
        
    public static DataMap doStopBatch(String wasConfigId, DataMap map) 
            throws ManagementException, FileNotFoundException {     
        
        return getManagementAdapter().doStopBatch(wasConfigId, map);
    }
    
    /**
     * ExecutingBatchAgent를 호출합니다.
     * DataMap에 있는 BATCH_ID를 수동 실행 합니다.
     * @return map
     * @throws TestException 
     */
    public static DataMap doForceStart(DataMap map) {
        return getManagementAdapter().doForceStart(map);
    }
        
    public static DataMap doForceStart(String wasConfigId, DataMap map) 
            throws ManagementException, FileNotFoundException {     
        
        return getManagementAdapter().doForceStart(wasConfigId, map);
    }
    
    public static HashMap getSaveContextMonitorData(String wasConfigId) {     
        return getManagementAdapter().getSaveContextMonitorData(wasConfigId);
    }   
    
    public static HashMap getWorkingThreadData(String wasConfigId, String threshold) {     
        return getManagementAdapter().getWorkingThreadData(wasConfigId, threshold);
    }   
 
    public static String setLogEnabled(String trxId) {
        return getManagementAdapter().setLogEnabled(trxId);
    }
        
    public static String setLogEnabled(String wasConfigId, String trxId) {     
        return getManagementAdapter().setLogEnabled(wasConfigId, trxId);
    } 

    public static String setLogDisabled(String trxId) {
        return getManagementAdapter().setLogDisabled(trxId);
    }
        
    public static String setLogDisabled(String wasConfigId, String trxId) {     
        return getManagementAdapter().setLogDisabled(wasConfigId, trxId);
    } 
    
    public static String getContextMonitorToStringData(String trxId) {
        return getManagementAdapter().getContextMonitorToStringData(trxId);
    }
        
    public static String getContextMonitorToStringData(String wasConfigId, String trxId) {     
        return getManagementAdapter().getContextMonitorToStringData(wasConfigId, trxId);
    }   
    
    public static String stopContextProcess(String trxId) {
        return getManagementAdapter().stopContextProcess(trxId);
    }
        
    public static String stopContextProcess(String wasConfigId, String trxId) {     
        return getManagementAdapter().stopContextProcess(wasConfigId, trxId);
    }       
}

