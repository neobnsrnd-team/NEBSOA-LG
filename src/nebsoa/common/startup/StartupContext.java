/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.startup;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import nebsoa.common.exception.SysException;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 프레임웍을 초기화 하기 위한 설정을 관리하는 클래스
 * was start시에 <font color='red> SPIDER_INSTANCE_ID와 SPIDER_HOME 
 * 디렉토리,(SPIDER_LOG_HOME 디렉토리)</font>를 명시해야 한다.
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
 * $Log: StartupContext.java,v $
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
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.2  2008/01/22 07:36:50  오재훈
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:34  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:39:16  안경아
 * *** empty log message ***
 *
 * Revision 1.21  2007/09/05 11:34:22  김승희
 * startup.properties 파일이 없을 때 exception 내는 부분 삭제
 *
 * Revision 1.20  2006/11/20 07:03:42  이종원
 * Source code정리
 *
 * Revision 1.19  2006/10/30 09:59:24  이종원
 * runningMode제거
 *
 * Revision 1.18  2006/08/29 11:07:24  김성균
 * SPIDER_LOG_HOME 추가
 *
 * Revision 1.17  2006/07/28 07:04:25  이종원
 * runningMode(T,D,R)체크 추가
 *
 * Revision 1.16  2006/07/25 12:00:20  김성균
 * 사용하지 않는 정보를 표시하지 않도록 수정
 *
 * Revision 1.15  2006/07/10 00:19:36  김성균
 * *** empty log message ***
 *
 * Revision 1.14  2006/07/04 11:52:29  이종원
 * *** empty log message ***
 *
 * Revision 1.13  2006/07/04 11:38:43  이종원
 * *** empty log message ***
 *
 * Revision 1.12  2006/07/04 11:32:01  이종원
 * *** empty log message ***
 *
 * Revision 1.11  2006/06/20 04:31:56  이종원
 * *** empty log message ***
 *
 * Revision 1.10  2006/06/17 10:37:45  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class StartupContext {

//    /**
//     * 운영모드 중에 개발모드에 해당하는 상수 : D
//     */
//    static public String RUNNING_MODE_DEV="D";
//    /**
//     * 운영모드 중에 Test모드에 해당하는 상수 : T
//     */
//    static public String RUNNING_MODE_TEST="T";
//    /**
//     * 운영모드 중에 리얼환경 모드에 해당하는 상수 : R
//     */
//    static public String RUNNING_MODE_REAL="R";
    
	/**
	 * property가 DB Mode일 때  DB CONFIG를 접근하기 위한 JNDI_NAME 
	 */
	private static String jndiName;
	
    /**
	 *  property가 DB Mode일 때 DB CONFIG를 접근하기 위한 <code>Context.INITIAL_CONTEXT_FACTORY</code> 값
     */
    private static String initialContextFactory;
    
    /**
	 * property가 DB Mode일 때 DB CONFIG를 접근하기 위한 <code>Context.PROVIDER_URL</code> 값
     */
    private static String providerUrl;
    
    /**
	 * SPIDER FRAMEWORK이 수행 되는 INSTANCE_ID
     */
    private static String instanceId;
    
    /**
	 * 프레임웍 CONFIG 정보 타입 (XML, DB중 하나)
     */
    private static String propertyType;
    
    /**
     * SPIDER_HOME DIR 
     */
    public static String SPIDER_HOME;
    
    /**
     * SPIDER_LOG_HOME DIR 
     */
    public static String SPIDER_LOG_HOME;
    
    /**
     * SPIDER_RUNNING_MODE (D:개발,T:Test,R:운영모드) 
     */
//    public static String runningMode;
    
	/**
	 * 로그설정 및 초기화 작업을 수행합니다.
	 */
	static {
		loadConfig();
	}

	/**
	 * 프레임웍 초기화를 위해서 사용하는 정보를 로드합니다.
	 */
	static private void loadConfig() {
		System.out.println("\t#################################");
		System.out.println("\t# 프레임웍 초기화 정보를 로드합니다.");
		
        try {
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("startup.properties");
            /*if (inputStream == null) {
            	throw new SysException("startup.properties 파일을 찾을 수 없습니다.");
            }*/
            Properties config = new Properties();
            if(inputStream != null) config.load(inputStream);
            jndiName = config.getProperty("db.jndi.name");
            initialContextFactory = config.getProperty("db.initial.context.factory");
            providerUrl = config.getProperty("db.provider.url");
            
            propertyType = config.getProperty("property.type");
            
            SPIDER_HOME=System.getProperty("NEBSOA_HOME");
            if(SPIDER_HOME==null){
                String spiderHome=config.getProperty("nebsoa.home.dir");
                if(spiderHome==null){
                    throw new Error("NEBSOA_HOME IS NOT SETTED !!");
                }
                SPIDER_HOME=spiderHome;
                System.setProperty("NEBSOA_HOME",SPIDER_HOME);                
            }
            
            SPIDER_LOG_HOME=System.getProperty("NEBSOA_LOG_HOME");
            if(SPIDER_LOG_HOME==null){
                String spiderLogHome=config.getProperty("nebsoa.log.dir");
                if(spiderLogHome==null){
                    throw new Error("NEBSOA_LOG_HOME IS NOT SETTED !!");
                }
                SPIDER_LOG_HOME=spiderLogHome;
                System.setProperty("NEBSOA_LOG_HOME",SPIDER_LOG_HOME);                
            }
            
            instanceId = System.getProperty("NEBSOA_INSTANCE_ID");
            if(instanceId==null){
                instanceId=config.getProperty("nebsoa.instance.id");
                if(instanceId==null){
                    throw new Error("NEBSOA_INSTANCE ID IS NOT SETTED !!");
                }
                System.setProperty("NEBSOA_INSTANCE_ID",instanceId);                
            }
            
//            runningMode = System.getProperty("SPIDER_RUNNING_MODE","D");
//            if(!"R".equals(runningMode) && !"T".equals(runningMode)
//                    && !"D".equals(runningMode)) {
//                throw new Error("운영모드 설정이 잘못 되었습니다.["+runningMode+"]");
//            }
            
            System.out.println("\t# NEBSOA_HOME = " + SPIDER_HOME);
            System.out.println("\t# NEBSOA_LOG_HOME = " + SPIDER_LOG_HOME);
            System.out.println("\t# instanceId = " + instanceId);
//            System.out.println("\t# RUNNING MODE = " +
//                    (runningMode.equals("D")?"개발모드":
//                        (runningMode.equals("T")?"Test모드":
//                            (runningMode.equals("R")?"운영모드":"알수 없음"))));
            //System.out.println("\t# propertyType = " + propertyType);
        } catch (IOException e) {
            e.printStackTrace();
            throw new SysException("프레임웍 초기화  중 오류가 발생하였습니다.");
        }

		System.out.println("\t# 프레임웍 초기화  정보 로드를 완료하였습니다.");
        System.out.println("\t#################################");
	}

	/**
	 * @return Returns the initialContextFactory.
	 */
	public static String getInitialContextFactory() {
		return initialContextFactory;
	}

	/**
	 * @param initialContextFactory The initialContextFactory to set.
	 */
	public static void setInitialContextFactory(String initialContextFactory) {
		StartupContext.initialContextFactory = initialContextFactory;
	}

	/**
	 * @return Returns the instanceId.
	 */
	public static String getInstanceId() {
		return instanceId;
	}

	/**
	 * @param instanceId The instanceId to set.
	 */
	public static void setInstanceId(String instanceId) {
		StartupContext.instanceId = instanceId;
	}

	/**
	 * @return Returns the jndiName.
	 */
	public static String getJndiName() {
		return jndiName;
	}

	/**
	 * @param jndiName The jndiName to set.
	 */
	public static void setJndiName(String jndiName) {
		StartupContext.jndiName = jndiName;
	}

	/**
	 * @return Returns the propertyType.
	 */
	public static String getPropertyType() {
		return propertyType;
	}

	/**
	 * @param propertyType The propertyType to set.
	 */
	public static void setPropertyType(String propertyType) {
		StartupContext.propertyType = propertyType;
	}

	/**
	 * @return Returns the providerUrl.
	 */
	public static String getProviderUrl() {
		return providerUrl;
	}

	/**
	 * @param providerUrl The providerUrl to set.
	 */
	public static void setProviderUrl(String providerUrl) {
		StartupContext.providerUrl = providerUrl;
	}
	
	public static void main(String[] args) {
		Properties config = new Properties();
		System.out.println(config.getProperty("ee"));
	}

//    public static String getRunningMode() {
//        return runningMode;
//    }
//
//    public static void setRunningMode(String runningMode) {
//        StartupContext.runningMode = runningMode;
//    }	
}