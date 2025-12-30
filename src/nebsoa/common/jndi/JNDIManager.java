/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.jndi;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.UserTransaction;

import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.PropertyManager;

/*******************************************************************
 * <pre>
 * 1.설명 
 * J2EE SERVER의 환경 setting 정보를 가지고 있는 CONTEXT를 얻어 내는 클래스
 * was_config.properties파일에서 INITIAL_CONTEXT_FACTORY, PROVIDER_URL정보를 읽어 오며
 * 없을 시 기본 정보를 읽어 오도록 세팅해 두었다.
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
 * $Log: JNDIManager.java,v $
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
 * Revision 1.3  2008/04/08 04:46:07  김승희
 * 메소드 추가
 *
 * Revision 1.2  2008/01/29 06:49:35  신상수
 * jboss의 URL_PKG_PREFIXES 설정 추가
 *
 * Revision 1.1  2008/01/22 05:58:25  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:59  안경아
 * *** empty log message ***
 *
 * Revision 1.5  2007/03/07 02:53:45  김성균
 * 일부 로그 INFO 레벨로 변경
 *
 * Revision 1.4  2006/12/18 06:14:39  김성균
 * *** empty log message ***
 *
 * Revision 1.3  2006/10/24 13:14:19  이종원
 * lookup 시 발생하는 오류 메시지 수정
 *
 * Revision 1.2  2006/06/19 04:10:16  안경아
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class JNDIManager  {
	
	/**
	 * J2EE SERVER의 환경 default setting 정보를 가지고 있는 CONTEXT를 얻어 낸다.
	 * was_config.properties파일에서 default.xxx로 세팅된 정보를 찾는다.
	 * @return Context
	 * @throws Exception
	 */
	static public Context getInitialContext() {
		return getInitialContext(null);
	}	
  
	/** 
	 * J2EE SERVER의 환경 setting 정보를 가지고 있는 CONTEXT를 얻어 낸다.
	 * was_config.properties파일에서 configName.xxx로 세팅된 정보를 찾는다.
	 * @return Context
	 * @throws Exception
	 */
	static public Context getInitialContext(String configName) {		
		
        String config = "default";
        
        if (configName != null) {
            config = configName;
        }

        Properties prop = new Properties();

        String contextFactory = PropertyManager.getProperty("was_config", config + ".INITIAL_CONTEXT_FACTORY", null);
        
        if (contextFactory != null) {
            LogManager.info("INITIAL_CONTEXT_FACTORY : " + contextFactory);
            prop.put(Context.INITIAL_CONTEXT_FACTORY, contextFactory);
        }
		
		String providerUrl = PropertyManager.getProperty("was_config", config + ".PROVIDER_URL", null);
        
        if (providerUrl != null) {
            LogManager.info("PROVIDER_URL : " + providerUrl);
            prop.put(Context.PROVIDER_URL, providerUrl);
        }
        
        /**
         * JBoss EJB 설정에 필요한 부분 추가
         * was_config.properties.xml에 설정된 값을 읽어 들인다.
         * 설정된 값이 없을 시 널 처리
         */
    	String urlPrefixes = PropertyManager.getProperty("was_config", config + ".URL_PKG_PREFIXES",null);
    	
    	if(urlPrefixes != null)
    	{
    	    LogManager.info("URL_PKG_PREFIXES : " + urlPrefixes);
    	    prop.put(Context.URL_PKG_PREFIXES, urlPrefixes);
    	}   
    	
    	return createInitialContext(configName, prop);

	}
	private static Context createInitialContext(String configName, Properties prop) {
		Context context = null;    	
        try {
            if (prop.size() > 0) {
                LogManager.info("jndi properties : " + prop.toString());
                context = new InitialContext(prop);
            } else {
                context = new InitialContext();
            }
            LogManager.info("context >>>" + context.getEnvironment());
        } catch (NamingException e) {
            throw new SysException("FRS00013",
                    "WAS 서버에 접근할 수 없습니다(WAS장애 이거나, WAS CONFIG 설정 파일 오류입니다).");
        }
        
        return context;
	}
	
	/**
	 * J2EE SERVER의 환경 setting 정보를 가지고 있는 CONTEXT를 얻어 낸다.
	 * @param providerUrl 
	 * @param contextFactory
	 * @return
	 */
	static public Context getInitialContext(String providerUrl, String contextFactory) {
		
		Properties prop = new Properties();
		
		if (providerUrl != null) {
            LogManager.info("PROVIDER_URL : " + providerUrl);
            prop.put(Context.PROVIDER_URL, providerUrl);
        }
		
		if (contextFactory != null) {
            LogManager.info("INITIAL_CONTEXT_FACTORY : " + contextFactory);
            prop.put(Context.INITIAL_CONTEXT_FACTORY, contextFactory);
        }
		
		return createInitialContext(providerUrl, prop);
	}
	
    public static UserTransaction getUserTransaction(String configName){
        InitialContext context = null;
        UserTransaction tx = null;
        try {
            context = (InitialContext) getInitialContext(configName);
            tx = (UserTransaction)context.lookup("javax.transaction.UserTransaction");
        } catch (Exception e1) {
            LogManager.error(e1.getMessage(),e1);
        }finally{
            if(context != null){
                try {
                    context.close();
                } catch (NamingException e) {
                    LogManager.error(e.getMessage(),e);
                }
            }
        }
        return tx;
    }

    public static UserTransaction getUserTransaction() {
        return getUserTransaction(null);
    }
}
