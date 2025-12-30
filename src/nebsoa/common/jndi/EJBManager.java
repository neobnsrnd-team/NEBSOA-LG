/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.jndi;

import java.util.HashMap;

import javax.ejb.EJBHome;
import javax.naming.Context;
import javax.naming.NamingException;

import nebsoa.common.exception.ParamException;
import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.PropertyManager;

/*******************************************************************
 * <pre>
 * 1.설명 
 * EJB HOME 객체를 얻기 위한  메소드 모음 클래스
 * 한번 LOOKUP한 EJB HOME는 캐쉬 해 둔다.
 * 
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
 * $Log: EJBManager.java,v $
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
 * Revision 1.1  2008/08/04 08:54:52  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.3  2008/04/08 04:46:07  김승희
 * 메소드 추가
 *
 * Revision 1.2  2008/02/21 07:21:12  이종원
 * EJB_CACHE_THRESHOLD - 지정시간 이상 호출이 없으면 cache된 EJB 삭제 기능추가
 *
 * Revision 1.1  2008/01/22 05:58:25  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:59  안경아
 * *** empty log message ***
 *
 * Revision 1.8  2007/03/07 07:26:27  김성균
 * *** empty log message ***
 *
 * Revision 1.7  2007/03/07 04:27:06  김성균
 * 일부 로그 INFO 레벨로 변경
 *
 * Revision 1.6  2006/12/18 06:14:39  김성균
 * *** empty log message ***
 *
 * Revision 1.5  2006/10/21 08:43:48  이종원
 * cache에 저장 key 수정
 *
 * Revision 1.4  2006/07/05 17:29:41  이종원
 * *** empty log message ***
 *
 * Revision 1.3  2006/06/26 19:24:48  이종원
 * *** empty log message ***
 *
 * Revision 1.2  2006/06/19 04:10:16  안경아
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class EJBManager {

    /**
     * 이전에 한번 찾은 EJB 객체를 캐쉬 해 둡니다.
     */
    private static HashMap ejbTable = new HashMap();
    
    /**
     * 방화벽에서 오래동안 ejb이 없을 시 ejb 통신 세션을 invalidate시켰을 수 있으므로
     * 특정 시간이 지나면 cache를 clear하도록 수정 
     */
    private static HashMap ejbAccessTable = new HashMap();

    /**
     * cache 되어 있는 EJB HOME 객체를 얻어 내는 메소드
     * cache된 객체가 없으면 JNDI에 접근하여 새롭게 생성하여 담아 둔다.
     * @param homeName
     * @return  EJBHome
     * @throws ParamException 
     */
    public static EJBHome lookup(String homeName,String serverInfo) throws ParamException {
        if (homeName == null) {
            throw new ParamException("EJBHome name");
        }
        
        //check last access time
        Long lastAccessTime=(Long) ejbAccessTable.get((serverInfo == null ? "default" : serverInfo) + "::" + homeName);
        int ejbCacheThreshold=PropertyManager.getIntProperty("was_config","EJB_CACHE_THRESHOLD",120);
        if(ejbCacheThreshold<1000) ejbCacheThreshold=ejbCacheThreshold*1000;
        if(lastAccessTime!=null){
        	if(System.currentTimeMillis()-lastAccessTime.longValue() > ejbCacheThreshold){ //해당시간 동안  거래가 없었으면..
        		removeCache(homeName, serverInfo);        		
        	}
        }
        //Object ejbHome = ejbTable.get(homeName);
        Object ejbHome = ejbTable.get((serverInfo == null ? "default" : serverInfo) + "::" + homeName);
        if (ejbHome == null) {
            LogManager.info("캐쉬되지 않은 EJB Home:["+homeName +"] serverInfo : "+serverInfo);
            ejbHome = lookupEJBHome(homeName, serverInfo);
            ejbTable.put((serverInfo == null ? "default" : serverInfo) + "::" + homeName, ejbHome);
        } else {
            LogManager.info("캐쉬된 EJB Home : " + (serverInfo == null ? "default" : serverInfo) + "::" + homeName);
        }
        
        ejbAccessTable.put((serverInfo == null ? "default" : serverInfo) + "::" + homeName, 
        		Long.valueOf(System.currentTimeMillis()));

        return (EJBHome) ejbHome;
    }
    
    /**
     * cache 되어 있는 EJB HOME 객체를 얻어 내는 메소드
     * cache된 객체가 없으면 JNDI에 접근하여 새롭게 생성하여 담아 둔다.
     * @param homeName
     * @param providerURL InitialContext 생성 시 사용할 providerURL
     * @param contextFactory InitialContext 생성 시 사용할 contextFactory명
     * @return
     * @throws ParamException
     */
    public static EJBHome lookup(String homeName, String providerURL, String contextFactory) throws ParamException {
        if (homeName == null) {
            throw new ParamException("EJBHome name");
        }
        //Object ejbHome = ejbTable.get(homeName);
        Object ejbHome = ejbTable.get(providerURL + "::" + homeName);
        if (ejbHome == null) {
            LogManager.info("캐쉬되지 않은 EJB Home:["+homeName +"] providerURL : "+providerURL);
            ejbHome = lookupEJBHome(homeName, providerURL, contextFactory);
            ejbTable.put(providerURL + "::" + homeName, ejbHome);
        } else {
            LogManager.info("캐쉬된 EJB Home : " + providerURL + "::" + homeName);
        }
        return (EJBHome) ejbHome;
    }
    
    /**
     * EJB Home을 lookup하여 리턴한다. 
     * @param homeName ejb jndi name
     * @param providerURL InitialContext 생성 시 사용할 providerURL
     * @param contextFactory InitialContext 생성 시 사용할 contextFactory명
     * @return
     */
    private static Object lookupEJBHome(String homeName, String providerURL, String contextFactory) {
    	Context context = null;
        context = JNDIManager.getInitialContext(providerURL, contextFactory);
            
        try {
            LogManager.debug("homeName >>>" + homeName);
            return (EJBHome) context.lookup(homeName);
        } catch (NamingException e1) {
            removeCache(homeName, providerURL);
            LogManager.error(e1);
            throw new SysException("FRS00014", "등록되지 않은 EJB입니다.");
        } finally {
            try {
                if (context != null) {
                    context.close();
                    context = null;
                }
            } catch (Exception e) {
            }
        }
	}

	/**
     * cache 되어 있는 EJB HOME 객체를 없애고 새로  얻어 내는 메소드
     * cache된 객체가 있으면 clear한다.
     * @param homeName
     * @return  EJBHome
     * @throws ParamException 
     */
    public static EJBHome lookupNew(String homeName,String serverInfo) throws ParamException {
        if (homeName == null) {
            throw new ParamException("EJB Bean Name");
        }
        removeCache(homeName, serverInfo);
        return lookup(homeName, serverInfo);
    }
    
    /**
     * cache 되어 있는 EJB HOME 객체를 없애고 새로  얻어 내는 메소드
     * cache된 객체가 있으면 clear한다.
     * @param homeName
     * @param providerURL InitialContext 생성 시 사용할 providerURL
     * @param contextFactory InitialContext 생성 시 사용할 contextFactory명
     * @return
     * @throws ParamException
     */
    public static EJBHome lookupNew(String homeName, String providerURL, String contextFactory) throws ParamException {
        if (homeName == null) {
            throw new ParamException("EJB Bean Name");
        }
        removeCache(homeName, providerURL);
        return lookup(homeName, providerURL, contextFactory);
    }
    
    /**
     * cache 되어 있는 EJB HOME 객체를 얻어 내는 메소드
     * cache된 객체가 없으면 JNDI에 접근하여 새롭게 생성하여 담아 둔다.
     * @param homeName
     * @return  EJBHome
     * @throws ParamException 
     */
    public static EJBHome lookup(String homeName) throws ParamException {
        return lookup( homeName,null);
    }

    /**
     * JNDI에 접근하여 EJB HOME 객체를 얻어 내는 메소드
     * @param homeName
     * @return  EJBHome
     */
    public static EJBHome lookupEJBHome(String homeName, String serverInfo) {
        Context context = null;
        context = JNDIManager.getInitialContext(serverInfo);
            
        try {
            LogManager.debug("homeName >>>" + homeName);
            return (EJBHome) context.lookup(homeName);
        } catch (NamingException e1) {
            removeCache(homeName, serverInfo);
            LogManager.error(e1);
            throw new SysException("FRS00014", "등록되지 않은 EJB입니다.");
        } finally {
            try {
                if (context != null) {
                    context.close();
                    context = null;
                }
            } catch (Exception e) {
            }
        }
    }
    
    public static void removeCache(String homeName,String serverInfo){
        Object ejbHome = ejbTable.get((serverInfo==null?"default":serverInfo)+"::"+homeName);
        if(ejbHome != null) ejbTable.remove((serverInfo==null?"default":serverInfo)+"::"+homeName);
        Object lastAccessTime = ejbAccessTable.get((serverInfo==null?"default":serverInfo)+"::"+homeName);
        if(lastAccessTime != null)ejbAccessTable.remove((serverInfo==null?"default":serverInfo)+"::"+homeName);
    }
}
