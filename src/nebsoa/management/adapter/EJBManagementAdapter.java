/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.management.adapter;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.ejb.CreateException;

import nebsoa.common.exception.ManagementException;
import nebsoa.common.exception.SysException;
import nebsoa.common.exception.TestException;
import nebsoa.common.jndi.EJBManager;
import nebsoa.common.log.LogManager;
import nebsoa.common.monitor.message.MessageStatisticalObject;
import nebsoa.common.util.DataMap;
import nebsoa.management.ejb.ManagementProcessorEJB;
import nebsoa.management.ejb.ManagementProcessorHome;

/*******************************************************************
 * <pre>
 * 1.설명 
 * EJB로 ManagermentAgent 호출을 대리해 주기 위한 어댑터이다. 
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
 * $Log: EJBManagementAdapter.java,v $
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
public class EJBManagementAdapter extends AbstractManagementAdapter {
    
    /**  
     * ManagementProcessorEJB 의 JNDI 이름 
     */
    private static final String managementEjbName = "nebsoa.management.ejb.ManagementProcessorEJB";
	
    public DataMap doProcess(String wasConfigId, DataMap map) throws ManagementException {     
        
        ManagementProcessorEJB bean = null; 
        
        try {
            bean = getEJB(wasConfigId);
            map = bean.doProcess(map);
        } catch (CreateException e) {
            throw new SysException(map.getContext(), "FRS00004", "CreateException in ManagementClient - 원격관리서비스를 호출할 수 없습니다.");
        } catch (RemoteException e) {
            Throwable th = e.getCause();
            if (th == null) {
                throw new SysException(map.getContext(), "FRS00004", "RemoteException in ManagementClient - 원격관리서비스를 호출 하는 중 오류 발생.");
            } else if (th instanceof RuntimeException) {
                throw (RuntimeException) th;
            } else {
                throw new SysException(map.getContext(), "FRS00004", th);
            }
        }
        return map;
    }

    public DataMap doTest(String wasConfigId, DataMap map) throws TestException {     
        
    	ManagementProcessorEJB bean = null; 
    	
        try {
            bean = getEJB(wasConfigId);
            map = bean.doTest(map);
        } catch (CreateException e) {
            throw new SysException(map.getContext(), "FRS00004", "CreateException in ManagementClient - 원격관리서비스를 호출할 수 없습니다.");
        } catch (RemoteException e) {
            Throwable th = e.getCause();
            if (th == null) {
                throw new SysException(map.getContext(), "FRS00004", "RemoteException in ManagementClient - 원격관리서비스를 호출 하는 중 오류 발생.");
            } else if (th instanceof RuntimeException) {
                throw (RuntimeException) th;
            } else {
                throw new SysException(map.getContext(), "FRS00004", th);
            }
        }
        return map;
    }
    
    public DataMap getBatchLogInfo(String wasConfigId, DataMap map) {     
        
    	ManagementProcessorEJB bean = null; 
    	
        try {
            bean = getEJB(wasConfigId);
            map = bean.getBatchLogInfo(map);
        } catch (CreateException e) {
            throw new SysException(map.getContext(), "FRS00004", "CreateException in ManagementClient - 원격관리서비스를 호출할 수 없습니다.");
        } catch (RemoteException e) {
            Throwable th = e.getCause();
            if (th == null) {
                throw new SysException(map.getContext(), "FRS00004", "RemoteException in ManagementClient - 원격관리서비스를 호출 하는 중 오류 발생.");
            } else if (th instanceof RuntimeException) {
                throw (RuntimeException) th;
            } else {
                throw new SysException(map.getContext(), "FRS00004", th);
            }
        }
        return map;
    }
    
    public String getHtmlReport(String wasConfigId, DataMap map) {     
        
    	ManagementProcessorEJB bean = null; 
        String htmlReport = "";
    	
        try {
            bean = getEJB(wasConfigId);
            htmlReport = bean.getHtmlReport(map);
        } catch (CreateException e) {
            throw new SysException(map.getContext(), "FRS00004", "CreateException in ManagementClient - 원격관리서비스를 호출할 수 없습니다.");
        } catch (RemoteException e) {
            Throwable th = e.getCause();
            if (th == null) {
                throw new SysException(map.getContext(), "FRS00004", "RemoteException in ManagementClient - 원격관리서비스를 호출 하는 중 오류 발생.");
            } else if (th instanceof RuntimeException) {
                throw (RuntimeException) th;
            } else {
                throw new SysException(map.getContext(), "FRS00004", th);
            }
        }
        return htmlReport;
    }
    
    public ArrayList getMonitorDatas(String wasConfigId, DataMap map) {     
        
    	ManagementProcessorEJB bean = null; 
        ArrayList monitorDataList = null;
    	
        try {
            bean = getEJB(wasConfigId);
            monitorDataList = bean.getMonitorDatas(map);
        } catch (CreateException e) {
            throw new SysException(map.getContext(), "FRS00004", "CreateException in ManagementClient - 원격관리서비스를 호출할 수 없습니다.");
        } catch (RemoteException e) {
            Throwable th = e.getCause();
            if (th == null) {
                throw new SysException(map.getContext(), "FRS00004", "RemoteException in ManagementClient - 원격관리서비스를 호출 하는 중 오류 발생.");
            } else if (th instanceof RuntimeException) {
                throw (RuntimeException) th;
            } else {
                throw new SysException(map.getContext(), "FRS00004", th);
            }
        }
        return monitorDataList;
    }
    
    public Object[] getCategorys(String wasConfigId) {     
        
    	ManagementProcessorEJB bean = null; 
        Object[] categorys = null;
    	
        try {
            bean = getEJB(wasConfigId);
            categorys = bean.getCategorys();
        } catch (CreateException e) {
            throw new SysException("FRS00004", "CreateException in ManagementClient - 원격관리서비스를 호출할 수 없습니다.");
        } catch (RemoteException e) {
            Throwable th = e.getCause();
            if (th == null) {
                throw new SysException("FRS00004", "RemoteException in ManagementClient - 원격관리서비스를 호출 하는 중 오류 발생.");
            } else if (th instanceof RuntimeException) {
                throw (RuntimeException) th;
            } else {
                throw new SysException("FRS00004", th);
            }
        }
        return categorys;
    }
    
    public String doMonitorClear(String wasConfigId) {     
        
    	ManagementProcessorEJB bean = null; 
        String result = "0";
    	
        try {
            bean = getEJB(wasConfigId);
            result = bean.doMonitorClear();
        } catch (CreateException e) {
            throw new SysException("FRS00004", "CreateException in ManagementClient - 원격관리서비스를 호출할 수 없습니다.");
        } catch (RemoteException e) {
            Throwable th = e.getCause();
            if (th == null) {
                throw new SysException("FRS00004", "RemoteException in ManagementClient - 원격관리서비스를 호출 하는 중 오류 발생.");
            } else if (th instanceof RuntimeException) {
                throw (RuntimeException) th;
            } else {
                throw new SysException("FRS00004", th);
            }
        }
        return result;
    }
    
    public MessageStatisticalObject[] getAllStatisticalObject(String wasConfigId) {     
        
        ManagementProcessorEJB bean = null; 
        MessageStatisticalObject[] objects = null;
        
        try {
            bean = getEJB(wasConfigId);
            objects = bean.getAllStatisticalObject();
        } catch (CreateException e) {
            throw new SysException("FRS00004", "CreateException in ManagementClient - 원격관리서비스를 호출할 수 없습니다.");
        } catch (RemoteException e) {
            LogManager.error(e);
            throw new SysException("FRS0004",e.getMessage());
        }
        return objects;
    }
        
    /**
     * @param wasConfig
     * @return
     * @throws RemoteException
     * @throws CreateException
     */
    ManagementProcessorEJB getEJB(String wasConfig) throws RemoteException, CreateException{
        
        if (wasConfig == null) {
            wasConfig = getLocalWasConfig();
        }
        
    	ManagementProcessorHome home = getEJBHome(wasConfig, false);

        ManagementProcessorEJB bean = null; 
        try {
            bean = home.create();
        } catch (CreateException e) {
            home = getEJBHome(wasConfig, false);
            bean = home.create();
        }
        if (bean == null) throw new SysException(managementEjbName + " 생성에 실패하였습니다.");
        return bean;
    }

    /**
     * @param wasConfig
     * @param useCache
     * @return
     */
    private ManagementProcessorHome getEJBHome(String wasConfig,boolean useCache) {
        if (!useCache) {
            EJBManager.removeCache(managementEjbName, wasConfig);
        }
        ManagementProcessorHome home = 
            (ManagementProcessorHome) EJBManager.lookup(managementEjbName, wasConfig);
        return home;
    }
    
    public HashMap getContextMonitorData(String wasConfigId, DataMap map) {     
        
    	ManagementProcessorEJB bean = null; 
        HashMap returnMap = null;
    	
        try {
            bean = getEJB(wasConfigId);
            returnMap = bean.getContextMonitorData(map);
        } catch (CreateException e) {
            throw new SysException(map.getContext(), "FRS00004", "CreateException in ManagementClient - 원격관리서비스를 호출할 수 없습니다.");
        } catch (RemoteException e) {
            Throwable th = e.getCause();
            if (th == null) {
                throw new SysException(map.getContext(), "FRS00004", "RemoteException in ManagementClient - 원격관리서비스를 호출 하는 중 오류 발생.");
            } else if (th instanceof RuntimeException) {
                throw (RuntimeException) th;
            } else {
                throw new SysException(map.getContext(), "FRS00004", th);
            }
        }
        return returnMap;
    }

    public ArrayList getObjectReport(String wasConfigId, DataMap map) {     
        
    	ManagementProcessorEJB bean = null; 
    	ArrayList arrayList = null;
    	
        try {
            bean = getEJB(wasConfigId);
            arrayList = bean.getObjectReport(map);
        } catch (CreateException e) {
            throw new SysException(map.getContext(), "FRS00004", "CreateException in ManagementClient - 원격관리서비스를 호출할 수 없습니다.");
        } catch (RemoteException e) {
            Throwable th = e.getCause();
            if (th == null) {
                throw new SysException(map.getContext(), "FRS00004", "RemoteException in ManagementClient - 원격관리서비스를 호출 하는 중 오류 발생.");
            } else if (th instanceof RuntimeException) {
                throw (RuntimeException) th;
            } else {
                throw new SysException(map.getContext(), "FRS00004", th);
            }
        }
        return arrayList;
    }
    
    public ArrayList getExecutingBatchList(String wasConfigId, DataMap map) {     
        
    	ManagementProcessorEJB bean = null; 
    	ArrayList al = null;
        try {
            bean = getEJB(wasConfigId);
            al = bean.getExecutingBatchList();

        } catch (CreateException e) {
            throw new SysException(map.getContext(), "FRS00004", "CreateException in ManagementClient - 원격관리서비스를 호출할 수 없습니다.");
        } catch (RemoteException e) {
            Throwable th = e.getCause();
            if (th == null) {
                throw new SysException(map.getContext(), "FRS00004", "RemoteException in ManagementClient - 원격관리서비스를 호출 하는 중 오류 발생.");
            } else if (th instanceof RuntimeException) {
                throw (RuntimeException) th;
            } else {
                throw new SysException(map.getContext(), "FRS00004", th);
            }
        }
        return al;
    }
    
    public DataMap doStopBatch(String wasConfigId, DataMap map) {     
        
    	ManagementProcessorEJB bean = null; 
        try {
            bean = getEJB(wasConfigId);
            map = bean.doStopBatch(map);
            
        } catch (CreateException e) {
            throw new SysException(map.getContext(), "FRS00004", "CreateException in ManagementClient - 원격관리서비스를 호출할 수 없습니다.");
        } catch (RemoteException e) {
            Throwable th = e.getCause();
            if (th == null) {
                throw new SysException(map.getContext(), "FRS00004", "RemoteException in ManagementClient - 원격관리서비스를 호출 하는 중 오류 발생.");
            } else if (th instanceof RuntimeException) {
                throw (RuntimeException) th;
            } else {
                throw new SysException(map.getContext(), "FRS00004", th);
            }
        }
        return map;
    }
    
    public DataMap doForceStart(String wasConfigId, DataMap map) {     
        
    	ManagementProcessorEJB bean = null; 
        try {
            bean = getEJB(wasConfigId);
            bean.doForceStart(map);
            
        } catch (CreateException e) {
            throw new SysException(map.getContext(), "FRS00004", "CreateException in ManagementClient - 원격관리서비스를 호출할 수 없습니다.");
        } catch (RemoteException e) {
            Throwable th = e.getCause();
            if (th == null) {
                throw new SysException(map.getContext(), "FRS00004", "RemoteException in ManagementClient - 원격관리서비스를 호출 하는 중 오류 발생.");
            } else if (th instanceof RuntimeException) {
                throw (RuntimeException) th;
            } else {
                throw new SysException(map.getContext(), "FRS00004", th);
            }
        }
        return map;
    }
    
    public HashMap getSaveContextMonitorData(String wasConfigId) {     
        
    	ManagementProcessorEJB bean = null; 
    	HashMap result = null;
    	
        try {
            bean = getEJB(wasConfigId);
            result = bean.getSaveContextMonitorData();
        } catch (CreateException e) {
            throw new SysException("FRS00004", "CreateException in ManagementClient - 원격관리서비스를 호출할 수 없습니다.");
        } catch (RemoteException e) {
            Throwable th = e.getCause();
            if (th == null) {
                throw new SysException("FRS00004", "RemoteException in ManagementClient - 원격관리서비스를 호출 하는 중 오류 발생.");
            } else if (th instanceof RuntimeException) {
                throw (RuntimeException) th;
            } else {
                throw new SysException("FRS00004", th);
            }
        }
        return result;
    }   
    
    public HashMap getWorkingThreadData(String wasConfigId, String threshold) {     
        
    	ManagementProcessorEJB bean = null; 
        HashMap returnMap = null;
    	
        try {
            bean = getEJB(wasConfigId);
            returnMap = bean.getWorkingThreadData(threshold);
        } catch (CreateException e) {
            throw new SysException("FRS00004", "CreateException in ManagementClient - 원격관리서비스를 호출할 수 없습니다.");
        } catch (RemoteException e) {
            Throwable th = e.getCause();
            if (th == null) {
                throw new SysException("FRS00004", "RemoteException in ManagementClient - 원격관리서비스를 호출 하는 중 오류 발생.");
            } else if (th instanceof RuntimeException) {
                throw (RuntimeException) th;
            } else {
                throw new SysException("FRS00004", th);
            }
        }
        return returnMap;
    }   
 
    public String setLogEnabled(String wasConfigId, String trxId) {     
        
    	ManagementProcessorEJB bean = null; 
        String result = null;
    	
        try {
            bean = getEJB(wasConfigId);
            result = bean.setLogEnabled(trxId);
        } catch (CreateException e) {
            throw new SysException("FRS00004", "CreateException in ManagementClient - 원격관리서비스를 호출할 수 없습니다.");
        } catch (RemoteException e) {
            Throwable th = e.getCause();
            if (th == null) {
                throw new SysException("FRS00004", "RemoteException in ManagementClient - 원격관리서비스를 호출 하는 중 오류 발생.");
            } else if (th instanceof RuntimeException) {
                throw (RuntimeException) th;
            } else {
                throw new SysException("FRS00004", th);
            }
        }
        return result;
    } 

    public String setLogDisabled(String wasConfigId, String trxId) {     
        
    	ManagementProcessorEJB bean = null; 
        String result = null;
    	
        try {
            bean = getEJB(wasConfigId);
            result = bean.setLogDisabled(trxId);
        } catch (CreateException e) {
            throw new SysException("FRS00004", "CreateException in ManagementClient - 원격관리서비스를 호출할 수 없습니다.");
        } catch (RemoteException e) {
            Throwable th = e.getCause();
            if (th == null) {
                throw new SysException("FRS00004", "RemoteException in ManagementClient - 원격관리서비스를 호출 하는 중 오류 발생.");
            } else if (th instanceof RuntimeException) {
                throw (RuntimeException) th;
            } else {
                throw new SysException("FRS00004", th);
            }
        }
        return result;
    } 
    
    public String getContextMonitorToStringData(String wasConfigId, String trxId) {     
        
    	ManagementProcessorEJB bean = null; 
        String result = null;
    	
        try {
            bean = getEJB(wasConfigId);
            result = bean.getContextMonitorToStringData(trxId);
        } catch (CreateException e) {
            throw new SysException("FRS00004", "CreateException in ManagementClient - 원격관리서비스를 호출할 수 없습니다.");
        } catch (RemoteException e) {
            Throwable th = e.getCause();
            if (th == null) {
                throw new SysException("FRS00004", "RemoteException in ManagementClient - 원격관리서비스를 호출 하는 중 오류 발생.");
            } else if (th instanceof RuntimeException) {
                throw (RuntimeException) th;
            } else {
                throw new SysException("FRS00004", th);
            }
        }
        return result;
    }   
    
    public String stopContextProcess(String wasConfigId, String trxId) {     
        
    	ManagementProcessorEJB bean = null; 
        String result = null;
    	
        try {
            bean = getEJB(wasConfigId);
            result = bean.stopContextProcess(trxId);
        } catch (CreateException e) {
            throw new SysException("FRS00004", "CreateException in ManagementClient - 원격관리서비스를 호출할 수 없습니다.");
        } catch (RemoteException e) {
            Throwable th = e.getCause();
            if (th == null) {
                throw new SysException("FRS00004", "RemoteException in ManagementClient - 원격관리서비스를 호출 하는 중 오류 발생.");
            } else if (th instanceof RuntimeException) {
                throw (RuntimeException) th;
            } else {
                throw new SysException("FRS00004", th);
            }
        }
        return result;
    }       
}

