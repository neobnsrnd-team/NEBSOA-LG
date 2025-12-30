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
import nebsoa.common.log.LogManager;
import nebsoa.common.monitor.message.MessageStatisticalObject;
import nebsoa.common.util.DataMap;
import nebsoa.management.agent.BatchLogAgent;
import nebsoa.management.agent.ExecutingBatchAgent;
import nebsoa.management.agent.ManagementAgent;
import nebsoa.management.agent.MonitorAgent;
import nebsoa.management.agent.TestAgent;
import nebsoa.management.socket.ManagementClientWorker;
import nebsoa.management.socket.ManagementContext;

/*******************************************************************
 * <pre>
 * 1.설명 
 * Socket으로 ManagermentAgent 호출을 대리해 주기 위한 어댑터이다. 
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
 * $Log: SocketManagementAdapter.java,v $
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
public class SocketManagementAdapter extends AbstractManagementAdapter {
    
    private ManagementClientWorker getManagementWorker() {
        return ManagementClientWorker.getInstance();
    }
    
    public DataMap doProcess(String wasConfigId, DataMap map) throws ManagementException {     
        if (isLocalWas(wasConfigId)) {
            map = ManagementAgent.doProcess(map);
        } else {
            ManagementContext mctx = sendReceive(wasConfigId, "doProcess", map);
            Exception e = mctx.getException();
            if (e instanceof ManagementException) {
                throw (ManagementException) e;
            }
            map = (DataMap) mctx.getDataObject();
        }
        return map;
    }

    public DataMap doTest(String wasConfigId, DataMap map) throws TestException {     
        if (isLocalWas(wasConfigId)) {
            map = TestAgent.doTest(map);
        } else {
            ManagementContext mctx = sendReceive(wasConfigId, "doTest", map);
            Exception e = mctx.getException();
            if (e instanceof TestException) {
                throw (TestException) e;
            }
            map = (DataMap) mctx.getDataObject();
        }
        return map; 
    }
    
    public DataMap getBatchLogInfo(String wasConfigId, DataMap map) {     
        if (isLocalWas(wasConfigId)) {
            map = BatchLogAgent.getBatchLogInfo(map);
        } else {
            ManagementContext mctx = sendReceive(wasConfigId, "getBatchLogInfo", map);
            map = (DataMap) mctx.getDataObject();
        }
        return map; 
    }
    
    public String getHtmlReport(String wasConfigId, DataMap map) { 
        String result = null;
        if (isLocalWas(wasConfigId)) {
            result = MonitorAgent.getHtmlReport(map);
        } else {
            ManagementContext mctx = sendReceive(wasConfigId, "getHtmlReport", map);
            result = (String) mctx.getDataObject();
        }
        return result; 
    }
    
    public ArrayList getMonitorDatas(String wasConfigId, DataMap map) {     
        ArrayList result = null;
        if (isLocalWas(wasConfigId)) {
            result = MonitorAgent.getMonitorDatas(map);
        } else {
            ManagementContext mctx = sendReceive(wasConfigId, "getMonitorDatas", map);
            result = (ArrayList) mctx.getDataObject();
        }
        return result; 
    }
    
    public Object[] getCategorys(String wasConfigId) {     
        Object[] result = null;
        
        if (isLocalWas(wasConfigId)) {
            result = MonitorAgent.getCategorys();
        } else {
            ManagementContext mctx = sendReceive(wasConfigId, "getCategorys", null);
            result = (Object[]) mctx.getDataObject();
        }
    	
        return result;
    }
    
    public String doMonitorClear(String wasConfigId) {     
        String result = null;
        if (isLocalWas(wasConfigId)) {
            result = MonitorAgent.doMonitorClear();
        } else {
            ManagementContext mctx = sendReceive(wasConfigId, "doMonitorClear", null);
            result = (String) mctx.getDataObject();
        }
    	
        return result;
    }
    
    public MessageStatisticalObject[] getAllStatisticalObject(String wasConfigId) {     
        MessageStatisticalObject[] result = null;
        
        if (isLocalWas(wasConfigId)) {
            result = MonitorAgent.getAllStatisticalObject();
        } else {
            ManagementContext mctx = sendReceive(wasConfigId, "getAllStatisticalObject", null);
            result = (MessageStatisticalObject[]) mctx.getDataObject();
        }
        
        return result;
    }
        
    public HashMap getContextMonitorData(String wasConfigId, DataMap map) {     
        HashMap result = null;
        
        if (isLocalWas(wasConfigId)) {
            result = MonitorAgent.getContextMonitorData(map);
        } else {
            ManagementContext mctx = sendReceive(wasConfigId, "getContextMonitorData", map);
            result = (HashMap) mctx.getDataObject();
        }
        
        return result;
    }

    public ArrayList getObjectReport(String wasConfigId, DataMap map) {     
    	ArrayList result = null;
    	
    	if (isLocalWas(wasConfigId)) {
            result = MonitorAgent.getObjectReport(map);
        } else {
            ManagementContext mctx = sendReceive(wasConfigId, "getObjectReport", map);
            result = (ArrayList) mctx.getDataObject();
        }
        
        return result;
    }
    
    public ArrayList getExecutingBatchList(String wasConfigId, DataMap map) {     
    	ArrayList result = null;
    	
    	if (isLocalWas(wasConfigId)) {
            result = ExecutingBatchAgent.getExecutingBatchList();
        } else {
            ManagementContext mctx = sendReceive(wasConfigId, "getExecutingBatchList", map);
            result = (ArrayList) mctx.getDataObject();
        }
        
        return result;
    }
    
    public DataMap doStopBatch(String wasConfigId, DataMap map) {
        if (isLocalWas(wasConfigId)) {
            ExecutingBatchAgent.doStopBatch(map);
        } else {
            sendReceive(wasConfigId, "doStopBatch", map);
        }
        
        return map;
    }
    
    public DataMap doForceStart(String wasConfigId, DataMap map) { 
        
        if (isLocalWas(wasConfigId)) {
            ExecutingBatchAgent.doForceStart(map);
        } else {
            sendReceive(wasConfigId, "doForceStart", map);
        }
        
        return map;
    }
    
    public HashMap getSaveContextMonitorData(String wasConfigId) {     
    	HashMap result = null;
    	
    	if (isLocalWas(wasConfigId)) {
            result = MonitorAgent.getSaveContextMonitorData();
        } else {
            ManagementContext mctx = sendReceive(wasConfigId, "getSaveContextMonitorData", null);
            result = (HashMap) mctx.getDataObject();
        }
        
        return result;
    }
    
    public HashMap getWorkingThreadData(String wasConfigId, String threshold) {     
        HashMap result = null;
        
        if (isLocalWas(wasConfigId)) {
            result = MonitorAgent.getWorkingThreadData(threshold);
        } else {
            ManagementContext mctx = sendReceive(wasConfigId, "getWorkingThreadData", threshold);
            result = (HashMap) mctx.getDataObject();
        }
        
        return result;
    }   
 
    public String setLogEnabled(String wasConfigId, String trxId) {     
        String result = null;
        
        if (isLocalWas(wasConfigId)) {
            result = MonitorAgent.setLogEnabled(trxId);
        } else {
            ManagementContext mctx = sendReceive(wasConfigId, "setLogEnabled", trxId);
            result = (String) mctx.getDataObject();
        }
        
        return result;
    } 

    public String setLogDisabled(String wasConfigId, String trxId) {     
        String result = null;
        
        if (isLocalWas(wasConfigId)) {
            result = MonitorAgent.setLogDisabled(trxId);
        } else {
            ManagementContext mctx = sendReceive(wasConfigId, "setLogDisabled", trxId);
            result = (String) mctx.getDataObject();
        }
    	
        return result;
    }
    
    public String getContextMonitorToStringData(String wasConfigId, String trxId) {     
        String result = null;
        
        if (isLocalWas(wasConfigId)) {
            result = MonitorAgent.getContextMonitorToStringData(trxId);
        } else {
            ManagementContext mctx = sendReceive(wasConfigId, "getContextMonitorToStringData", trxId);
            result = (String) mctx.getDataObject();
        }
        
        return result;
    }
    
    public String stopContextProcess(String wasConfigId, String trxId) {
        String result = null;
        
        if (isLocalWas(wasConfigId)) {
            result = MonitorAgent.stopContextProcess(trxId);
        } else {
            ManagementContext mctx = sendReceive(wasConfigId, "stopContextProcess", trxId);
            result = (String) mctx.getDataObject();
        }
        
        return result;
    }

    /**
     * @param wasConfigId
     * @param command
     * @param data
     * @return
     */
    private ManagementContext sendReceive(String wasConfigId, String command, Object data) { 
        ManagementContext mctx = new ManagementContext(wasConfigId, command, data);
        mctx = getManagementWorker().doProcess(mctx);
        Exception e = mctx.getException();
        if (e != null) {
            LogManager.error("ManagementAgent 수행 중 오류가 발생했습니다." + e.toString(), e);
	        if (e instanceof RuntimeException) {
	            throw (RuntimeException) e;
	        }
        }
        return mctx;
    }
}

