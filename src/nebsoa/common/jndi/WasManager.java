/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.jndi;

import java.io.FileNotFoundException;
import java.util.HashMap;

import nebsoa.common.Constants;
import nebsoa.common.exception.SysException;
import nebsoa.common.jdbc.DBManager;
import nebsoa.common.jdbc.DBResultSet;
import nebsoa.common.log.LogManager;
import nebsoa.common.startup.StartupContext;
import nebsoa.common.util.DataMap;
import nebsoa.management.ManagementObject;

/*******************************************************************
 * <pre>
 * 1.설명 
 * was instace의 정보를 담고 있는 vo 클래스(wasConfig와 was instance를 로딩하고 관리한다.
 * 최초 로딩 될때 자동으로 기동 되됨. 개발자들이 직접 사용할 일은 없음.
 * 2.사용법
 * 해당 서비스가 local에서 서비즈 중인지 판단하고자 한다면 
 * boolean isLocal = WasInstanceManager.getInstance().isLocal("test_was_config_id");
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author $Author: cvs $
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * $Log: WasManager.java,v $
 * Revision 1.1  2018/01/15 03:39:52  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:21  cvs
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
 * Revision 1.1  2008/08/04 08:54:52  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:25  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.2  2007/11/30 09:46:53  안경아
 * DB NAME 지정
 *
 * Revision 1.1  2007/11/26 08:38:59  안경아
 * *** empty log message ***
 *
 * Revision 1.7  2006/10/30 01:44:13  김성균
 * *** empty log message ***
 *
 * Revision 1.6  2006/06/28 12:48:35  김성균
 * loadWasConfigInfo() 버그 수정
 *
 * Revision 1.5  2006/06/20 09:28:06  이종원
 * *** empty log message ***
 *
 * Revision 1.4  2006/06/20 04:55:38  이종원
 * *** empty log message ***
 *
 * Revision 1.3  2006/06/20 04:51:07  이종원
 * *** empty log message ***
 *
 * Revision 1.2  2006/06/20 04:40:43  이종원
 * *** empty log message ***
 *
 * Revision 1.1  2006/06/20 04:31:06  이종원
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class WasManager extends ManagementObject {
    private static WasManager instance;

    public static WasManager getInstance() {
        if (instance == null) {
            instance = new WasManager();
        }
        return instance;
    }

    HashMap instanceMap;

    HashMap configMap;

    WasInstance myInstance;

    WasConfig myConfig;

    private WasManager() {
        loadAll();
    }
    
    /**
     * WAS설정 정보를 로딩한다.
     */
    private void loadAll(){
        if (isXmlMode()) {
            try {
                fromXml();
            } catch (FileNotFoundException e) {
                throw new SysException("XML 파일을 찾을 수 없습니다.");
            }
        } else {
	        loadWasConfigInfo();
	        loadWasInstanceInfo();
        }
        loadMyInstance();
    }
    
    private void loadMyInstance() {
        Object obj = instanceMap.get(StartupContext.getInstanceId());
        if (obj != null) {
            myInstance = (WasInstance) obj;
        }
        LogManager.debug("MY Instance Info : " + myInstance);
        if (myInstance != null) {
            myConfig = myInstance.getWasConfig();
        }
    }
    
    /**
     * 해당 서비스가 로컬 머신에서 같이 서비스 되고  있는지 판단해 주는 메소드
     * 2006. 6. 20.  이종원 작성
     * @param wasConfigId
     * @return
     */
    public boolean isLocal(String wasConfigId){
        LogManager.debug("인자로 전달된 was config id : "+wasConfigId);
        if(wasConfigId==null){
            LogManager.debug("인자로 전달된 was config id is null : return false"); 
            return false;
        }
        if(myConfig==null) {
            if(wasConfigId.equals(StartupContext.getInstanceId())){
                LogManager.debug("myWasConfig is null but equal myInstanceId and argument : return true");
                return true;
            }
            LogManager.debug("myWasConfig is null : return false"); 
            return false;
        }
        return myConfig.getWasConfigId().equals(wasConfigId);
    }

    /**
     * 
     * 2006. 6. 20.  이종원 작성
     * @param wasConfigId
     * @return wasConfigId로 등록되어 있는 WasConfig를 리턴합니다.
     */
    public WasConfig getWasConfig(String wasConfigId){
        return (WasConfig) configMap.get(wasConfigId);
    }
    /**
     * 
     * 2006. 6. 20.  이종원 작성
     * @return local instance의  WasConfig를 리턴합니다.
     */   
    public WasConfig getLocalWasConfig(){
        return myConfig;
    }
    
    /**
     * 
     * 2006. 6. 20.  이종원 작성
     * @return local instance의  WasConfig를 리턴합니다.
     */   
    public String getLocalWasConfigId(){
        if(myConfig == null){
            return StartupContext.getInstanceId();
        }
        return myConfig.getWasConfigId();
    }
    
    
    
    /**
     * was config loading sql
     */
    static String WAS_CONFIG_LOAD_SQL=
        "SELECT WAS_CONFIG_ID, WAS_CONFIG_NAME,CONTEXT_FACTORY,PROVIDER_URL"
        +"\n FROM FWK_WAS_CONFIG";
    /**
     * loadWasConfigInfo from FWK_WAS_CONFIG table
     * 2006. 6. 20.  이종원 작성
     */
    public void loadWasConfigInfo(){
        if(configMap == null){
            configMap = new HashMap();
        }else{
            configMap.clear();
        }
        DBResultSet rs = DBManager.executeQuery(Constants.SPIDER_DB, WAS_CONFIG_LOAD_SQL);
        LogManager.debug("was config info==========\n"+rs);
        WasConfig cfg = null;
        
        while(rs.next()){            
        	cfg = new WasConfig();
            configMap.put(rs.getString("WAS_CONFIG_ID"),cfg);
            cfg.setWasConfigId(rs.getString("WAS_CONFIG_ID"));
            cfg.setWasConfigName(rs.getString("WAS_CONFIG_NAME"));
            cfg.setContextFactory(rs.getString("CONTEXT_FACTORY"));
            cfg.setPrividerUrl(rs.getString("PROVIDER_URL"));            
        }
        
        LogManager.debug(">>> 등록된 was config\n"+configMap);
    }
     
    static String WAS_INSTANCE_LOAD_SQL=
        "SELECT INSTANCE_ID, INSTANCE_NAME,INSTANCE_DESC,WAS_CONFIG_ID"
        +"\n FROM FWK_WAS_INSTANCE";
    /**
     * loadWasInstanceInfo from FWK_WAS_INSTANCE table
     * 2006. 6. 20.  이종원 작성
     */
    public void loadWasInstanceInfo(){
        if(instanceMap == null){
            instanceMap = new HashMap();
        }else{
            instanceMap.clear();
        }
        DBResultSet rs = DBManager.executeQuery(Constants.SPIDER_DB, WAS_INSTANCE_LOAD_SQL);
        LogManager.debug("was instance info==========\n"+rs);
        WasInstance was=null;
        
        while(rs.next()){
            was= new WasInstance();
            instanceMap.put(rs.getString("INSTANCE_ID"),was);
            was.setInstanceId(rs.getString("INSTANCE_ID"));
            was.setInstanceName(rs.getString("INSTANCE_NAME"));
            was.setWasConfig(
                    (WasConfig) configMap.get(rs.getString("WAS_CONFIG_ID")));
            
        }
        LogManager.debug(">>> 등록된 was instance\n"+instanceMap);
    }
    
    /**
     * 전체리로딩 
     */
    public static void reloadAll(DataMap map) {
        getInstance().loadAll();
        getInstance().toXml();
    }

    /* (non-Javadoc)
     * @see nebsoa.management.ManagementObject#getInstance()
     */
    public Object getManagementObject() {
        return instance;
    }
    
    /* (non-Javadoc)
     * @see nebsoa.management.ManagementObject#setInstance(java.lang.Object)
     */
    public void setManagementObject(Object obj) {
        instance = (WasManager) obj;
    }
    
    public static void main(String[] args){
        WasManager mgr = WasManager.getInstance();
        System.out.println(mgr.isLocal("CW00"));
        System.out.println(mgr.isLocal("CW01"));
        System.out.println(mgr.isLocal("CMS_TEST"));
    }
}