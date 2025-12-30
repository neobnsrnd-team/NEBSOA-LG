/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.management.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import nebsoa.common.exception.ManagementException;
import nebsoa.common.exception.TestException;
import nebsoa.common.monitor.message.MessageStatisticalObject;
import nebsoa.common.util.DataMap;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 원격지에서 프레임웍을 관리하기 위한 어댑터가 구현해야 인터페이스 클래스이다.
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
 * $Log: ManagementAdapter.java,v $
 * Revision 1.1  2018/01/15 03:39:52  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:20  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:26  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:28  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/10/16 00:09:15  김성균
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public interface ManagementAdapter {
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////
    // 새로운 프로토콜 방식으로 ManagementAdaptor를 구현해야 될 경우 구현해야 될 메소드 정의
    //////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public abstract DataMap doProcess(String wasConfigId, DataMap map) throws ManagementException;

    public abstract DataMap doTest(String wasConfigId, DataMap map) throws TestException;

    public abstract DataMap getBatchLogInfo(String wasConfigId, DataMap map);

    public abstract String getHtmlReport(String wasConfigId, DataMap map);

    public abstract ArrayList getMonitorDatas(String wasConfigId, DataMap map);

    public abstract Object[] getCategorys(String wasConfigId);

    public abstract String doMonitorClear(String wasConfigId);

    public abstract MessageStatisticalObject[] getAllStatisticalObject(String wasConfigId);

    public abstract HashMap getContextMonitorData(String wasConfigId, DataMap map);

    public abstract ArrayList getObjectReport(String wasConfigId, DataMap map);

    public abstract ArrayList getExecutingBatchList(String wasConfigId, DataMap map);

    public abstract DataMap doStopBatch(String wasConfigId, DataMap map);

    public abstract DataMap doForceStart(String wasConfigId, DataMap map);

    public abstract HashMap getSaveContextMonitorData(String wasConfigId);

    public abstract HashMap getWorkingThreadData(String wasConfigId, String threshold);

    public abstract String setLogEnabled(String wasConfigId, String trxId);

    public abstract String setLogDisabled(String wasConfigId, String trxId);

    public abstract String getContextMonitorToStringData(String wasConfigId, String trxId);

    public abstract String stopContextProcess(String wasConfigId, String trxId);

    public abstract DataMap doProcess(DataMap map) throws ManagementException;

    public abstract DataMap doTest(DataMap map) throws TestException;

    public abstract DataMap getBatchLogInfo(DataMap map);

    public abstract String getHtmlReport(DataMap map);

    public abstract ArrayList getMonitorDatas(DataMap map);

    public abstract HashMap getContextMonitorData(DataMap map);

    public abstract ArrayList getObjectReport(DataMap map);

    public abstract ArrayList getExecutingBatchList(DataMap map);

    public abstract DataMap doStopBatch(DataMap map);

    public abstract DataMap doForceStart(DataMap map);

    public abstract Object[] getCategorys();

    public abstract String doMonitorClear();

    public abstract MessageStatisticalObject[] getAllStatisticalObject();

    public abstract String getLocalWasConfig();

    public abstract String setLogEnabled(String trxId);

    public abstract String setLogDisabled(String trxId);

    public abstract String getContextMonitorToStringData(String trxId);

    public abstract String stopContextProcess(String trxId);

}