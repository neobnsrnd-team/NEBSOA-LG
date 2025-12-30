/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.management.socket;

import java.net.ServerSocket;

import nebsoa.common.util.DataMap;
import nebsoa.management.agent.BatchLogAgent;
import nebsoa.management.agent.ExecutingBatchAgent;
import nebsoa.management.agent.ManagementAgent;
import nebsoa.management.agent.MonitorAgent;
import nebsoa.management.agent.TestAgent;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 요청(command)에 따라서 해당모듈을 호출해 주는 클래스입니다. 
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
 * $Log: AgentExecutor.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:23  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:26  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:29  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/10/16 00:08:54  김성균
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class AgentExecutor {
    
    private static AgentExecutor instance = new AgentExecutor();

    protected ServerSocket serverSocket;

    private AgentExecutor() {
    }
    
    public static AgentExecutor getInstance() {
        if (instance == null) {
            instance = new AgentExecutor();
        }
        return instance;
    }
    
    public ManagementContext execute(ManagementContext mctx) {
        String command = mctx.getCommand();
        Object paramObject = mctx.getDataObject();
        Object returnObject = null;
        
        try {
            if ("doProcess".equals(command)) {
                returnObject = ManagementAgent.doProcess((DataMap) paramObject);
            } else if ("doTest".equals(command)) {
                returnObject = TestAgent.doTest((DataMap) paramObject);
            } else if ("getBatchLogInfo".equals(command)) {
                returnObject = BatchLogAgent.getBatchLogInfo((DataMap) paramObject);
            } else if ("getHtmlReport".equals(command)) {
                returnObject = MonitorAgent.getHtmlReport((DataMap) paramObject);
            } else if ("getMonitorDatas".equals(command)) {
                returnObject = MonitorAgent.getMonitorDatas((DataMap) paramObject);
            } else if ("getMonitorDatas".equals(command)) {
                returnObject = MonitorAgent.getCategorys();
            } else if ("doMonitorClear".equals(command)) {
                returnObject = MonitorAgent.doMonitorClear();
            } else if ("getAllStatisticalObject".equals(command)) {
                returnObject = MonitorAgent.getAllStatisticalObject();
            } else if ("getContextMonitorData".equals(command)) {
                returnObject = MonitorAgent.getContextMonitorData((DataMap) paramObject);
            } else if ("getObjectReport".equals(command)) {
                returnObject = MonitorAgent.getObjectReport((DataMap) paramObject);
            } else if ("getExecutingBatchList".equals(command)) {
                returnObject = ExecutingBatchAgent.getExecutingBatchList();
            } else if ("doStopBatch".equals(command)) {
                returnObject = ExecutingBatchAgent.doStopBatch((DataMap) paramObject);
            } else if ("doForceStart".equals(command)) {
                ExecutingBatchAgent.doForceStart((DataMap) paramObject);
            } else if ("getSaveContextMonitorData".equals(command)) {
                returnObject = MonitorAgent.getSaveContextMonitorData();
            } else if ("getWorkingThreadData".equals(command)) {
                returnObject = MonitorAgent.getWorkingThreadData((String) paramObject);
            } else if ("setLogEnabled".equals(command)) {
                returnObject = MonitorAgent.setLogEnabled((String) paramObject);
            } else if ("setLogDisabled".equals(command)) {
                returnObject = MonitorAgent.setLogDisabled((String) paramObject);
            } else if ("setLogDisabled".equals(command)) {
                returnObject = MonitorAgent.setLogDisabled((String) paramObject);
            } else if ("getContextMonitorToStringData".equals(command)) {
                returnObject = MonitorAgent.getContextMonitorToStringData((String) paramObject);
            } else if ("stopContextProcess".equals(command)) {
                returnObject = MonitorAgent.stopContextProcess((String) paramObject);
            }
            mctx.setDataObject(returnObject);
        } catch (Exception e) {
            mctx.setException(e);
        }
        return mctx;
    }
}

