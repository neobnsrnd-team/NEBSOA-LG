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
import nebsoa.common.startup.StartupContext;
import nebsoa.common.util.DataMap;
import nebsoa.common.util.PropertyManager;
import nebsoa.common.util.StringUtil;

/*******************************************************************
 * <pre>
 * 1.설명 
 * ManagementAdapter를 구현한 최상위 추상클래스이다.
 * 원격지에서 프레임웍을 관리하기 위한 어댑터가 상속받아서 구현해야 되는 클래스이다.
 * 프로토콜별로 어댑터를 구현하기 위해서 필요한 클래스이다.
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
 * $Log: AbstractManagementAdapter.java,v $
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
public abstract class AbstractManagementAdapter implements ManagementAdapter {

    public DataMap doProcess(DataMap map) throws ManagementException {
        return doProcess(getLocalWasConfig(), map);
    }

    public DataMap doTest(DataMap map) throws TestException {
        return doTest(getLocalWasConfig(), map);
    }

    public DataMap getBatchLogInfo(DataMap map) {
        return getBatchLogInfo(getLocalWasConfig(), map);
    }

    public String getHtmlReport(DataMap map) {
        return getHtmlReport(getLocalWasConfig(), map);
    }

    public ArrayList getMonitorDatas(DataMap map) {
        return getMonitorDatas(getLocalWasConfig(), map);
    }

    public HashMap getContextMonitorData(DataMap map) {
        return getContextMonitorData(getLocalWasConfig(), map);
    }

    public ArrayList getObjectReport(DataMap map) {
        return getObjectReport(getLocalWasConfig(), map);
    }

    public ArrayList getExecutingBatchList(DataMap map) {
        return getExecutingBatchList(getLocalWasConfig(),map);
    }

    public DataMap doStopBatch(DataMap map) {
        return doStopBatch(getLocalWasConfig(),map);
    }

    public DataMap doForceStart(DataMap map) {
        return doForceStart(getLocalWasConfig(),map);
    }

    public Object[] getCategorys() {
        return getCategorys(getLocalWasConfig());
    }

    public String doMonitorClear() {
        String wasConfig = PropertyManager.getProperty("was_config", "ALL_WAS_CONFIG");
        String wasConfigs[] = StringUtil.split(wasConfig, ",");
        String result = "0";
        if(wasConfigs != null)
        {
            for(int i = 0; i < wasConfigs.length; i++)
            	result = doMonitorClear(wasConfigs[i]);
    
        } 
        return result;	
    }

    public MessageStatisticalObject[] getAllStatisticalObject() {
        return getAllStatisticalObject(getLocalWasConfig());
    }

    /**
     * 호출한 client와 같은 머신에서 수행 된다고 판단하여 null return
     * @return null (use same was instance)
     */
    public String getLocalWasConfig() {
        return null;
    }
    
    /**
     * 호출한 client와 같은 머신에서 수행되는지를 판단한다.
     * @param wasConfigId
     * @return
     */
    public boolean isLocalWas(String wasConfigId) {
        return (wasConfigId == null || StartupContext.getInstanceId().equals(wasConfigId));
    }

    public String setLogEnabled(String trxId) {
        return setLogEnabled(getLocalWasConfig(), trxId);
    }

    public String setLogDisabled(String trxId) {
        return setLogDisabled(getLocalWasConfig(), trxId);
    }

    public String getContextMonitorToStringData(String trxId) {
        return getContextMonitorToStringData(getLocalWasConfig(), trxId);
    }

    public String stopContextProcess(String trxId) {
        return stopContextProcess(getLocalWasConfig(), trxId);
    }
}


