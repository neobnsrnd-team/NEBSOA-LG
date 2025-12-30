/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.management.agent;

import java.util.ArrayList;
import java.util.HashMap;

import nebsoa.common.error.ErrorManager;
import nebsoa.common.exception.ManagementException;
import nebsoa.common.jdbc.SqlMapClientConfig;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.DataMap;
import nebsoa.common.util.MethodInvoker;
import nebsoa.common.util.PropertyManager;
import nebsoa.common.util.ShellCommander;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 관리객체의 Agent 기능을 수행하는 클래스
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
 * $Log: ManagementAgent.java,v $
 * Revision 1.1  2018/01/15 03:39:49  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:34  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:23  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:27  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.3  2008/04/30 07:54:46  오재훈
 * SQL 릴로드시 Ibatis계열 쿼리도 릴로드 되게 수정.
 *
 * Revision 1.2  2008/03/19 08:06:43  김승희
 * property를 다중으로 추가, 수정, 삭제하는 기능 추가
 *
 * Revision 1.1  2008/01/22 05:58:35  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:45  안경아
 * *** empty log message ***
 *
 * Revision 1.22  2007/08/17 12:12:29  최수종
 * 오류핸들러맵핑정보 캐쉬 저장 처리
 *
 * Revision 1.21  2007/07/16 05:36:29  오재훈
 * 배치 종료, 배시 수동 실행 추가
 *
 * Revision 1.20  2007/06/15 02:47:59  김승희
 * import 제거
 *
 * Revision 1.19  2007/06/14 08:14:28  김승희
 * 버그 수정
 *
 * Revision 1.18  2007/06/13 01:55:37  김승희
 * 인덱스 로그 조회 부분 수정
 *
 * Revision 1.17  2007/06/12 07:53:01  김승희
 * 인덱스 로그 search 추가
 *
 * Revision 1.16  2007/04/18 01:56:28  안경아
 * *** empty log message ***
 *
 * Revision 1.15  2007/04/18 01:43:31  안경아
 * *** empty log message ***
 *
 * Revision 1.14  2007/04/05 13:25:07  안경아
 * *** empty log message ***
 *
 * Revision 1.13  2007/01/17 08:36:03  김성균
 * 로그 설정 변경하는 부분 추가
 *
 * Revision 1.12  2006/12/29 05:11:26  김성균
 * *** empty log message ***
 *
 * Revision 1.11  2006/12/13 06:11:46  안경아
 * *** empty log message ***
 *
 * Revision 1.10  2006/11/08 05:16:28  오재훈
 * *** empty log message ***
 *
 * Revision 1.9  2006/11/04 06:02:25  김성균
 * *** empty log message ***
 *
 * Revision 1.8  2006/11/04 05:55:59  김성균
 * *** empty log message ***
 *
 * Revision 1.7  2006/11/02 06:45:06  김성균
 * *** empty log message ***
 *
 * Revision 1.6  2006/10/27 05:57:00  김성균
 * *** empty log message ***
 *
 * Revision 1.5  2006/10/24 02:47:46  김성균
 * *** empty log message ***
 *
 * Revision 1.4  2006/10/08 07:28:10  김성균
 * *** empty log message ***
 *
 * Revision 1.3  2006/08/16 02:48:54  김성균
 * DataMap argument 추가
 *
 * Revision 1.2  2006/07/26 08:01:58  김성균
 * reflection 으로 처리하도록 수정
 *
 * Revision 1.1  2006/07/21 07:36:54  김성균
 * 최초 등록
 *
 * </pre>
 ******************************************************************/
public class ManagementAgent {
    
    /**
     * Management 명령어를 수행한다.
	 * @param map
	 * @return map
	 * @throws ManagementException
	 */
	public static DataMap doProcess(DataMap map) throws ManagementException {
        try {
            String gubun = map.getParameter("gubun", "");
            
            if ("property_reload_all".equals(gubun)) {
                PropertyManager.reload();
            } else if ("property_reload".equals(gubun)) {
                PropertyManager.reload(map.getParameter("propName"));
            } else if ("property_set".equals(gubun)) {
                PropertyManager.setProperty(
                        map.getParameter("propName"),
                        map.getParameter("key"),
                        (HashMap) map.get("propertyMap")
                );
            } else if ("property_remove".equals(gubun)) {
                PropertyManager.removeProperty(
                        map.getParameter("propName"),
                        map.getParameter("key")
                );
           
            //property를 다중으로 추가, 수정, 삭제할 때 (ksh 추가 2008.03.19)
            //propertyMapList에 여러건에 해당하는 Map을 담아야 한다.
            //그 Map에 'command'란 key로 insert, update, remove중 하나를 담아서 추가, 수정, 삭제를 구분한다.
            } else if("property_multi_modify".equals(gubun)){
            	PropertyManager.modifyProperties(
                        map.getParameter("propName"),
                        (ArrayList)map.get("propertyMapList")
                );
            	
            } else if ("log_search".equals(gubun)) {
                map.put("RESULT", ShellCommander.exec((String []) map.get("shell")));
            } else if ("log_ap_sql_search".equals(gubun)) {
                map.put("AP_RESULT", ShellCommander.exec((String []) map.get("apShell")));
                map.put("SQL_RESULT", ShellCommander.exec((String []) map.get("sqlShell")));                    
            } else if ("log_config_level".equals(gubun)) {
                String level = map.getParameter("level");
                String loggerName = map.getParameter("loggerName");
                Logger logger = LogManager.getLogger(loggerName);
                Level loglevel = null;
                if (level.equals("ERROR")) {
                    loglevel = Level.ERROR;
                } else if (level.equals("INFO")) {
                    loglevel = Level.INFO;
                } else if (level.equals("DEBUG")) {
                    loglevel = Level.DEBUG;
                } else if (level.equals("OFF")) {
                    loglevel = Level.OFF;
                } else {
                    throw new Exception("Not allowed");
                }
                logger.setLevel(loglevel);
            } else if ("log_config_additivity".equals(gubun)) {
                String additivity = map.getParameter("additivity");
                String loggerName = map.getParameter("loggerName");
                Logger logger = LogManager.getLogger(loggerName);
                if ("Y".equals(additivity)) {
                    logger.setAdditivity(true);
                } else if ("N".equals(additivity)) {
                    logger.setAdditivity(false);
                }
            
            //인덱스 파일로부터 로그를 검색하는 부분 추가
            //_$search_class 이란 이름으로 호출할 클래스의 풀네임이 있어야 한다.
            } else if ("index_message_log_search".equals(gubun)) {
                           	
                MethodInvoker mi = new MethodInvoker();
                mi.setStaticMethod(map.getString("_$search_class")+".getInstance");
                mi.setArguments(null);
                mi.prepare();
                Object obj = mi.invoke();
                
                mi.setTargetObject(obj);
                mi.setArguments(new Object[] { map });
                mi.setTargetMethod("search");
                mi.prepare();
                map.put("RESULT", mi.invoke());
            	//map.put("RESULT", MethodInvokerUtil.invokeSingleton(map.getString("_$search_class"), "search", map));
                		
            //ScheduleManager 및 BatchManager Reload
            } else if ("batch_reload".equals(gubun)) {
                ExecutingBatchAgent.doReload();
                
            // 장애처리 핸들러 정보 Reload
            } else if ("error_handler_reload".equals(gubun)) {
            	ErrorManager.getInstance().reload();

            } else if ("sql".equals(gubun)) {

                //ibatis 쿼리 정보 Reload 추가
                SqlMapClientConfig.reloadAll();

            	//기본 COMMAND 파라미터에 있던 QueryManager.reloadAll() 수행
            	MethodInvoker mi = new MethodInvoker();
                mi.setStaticMethod(getCommand(map));
                mi.setArguments(new Object[] {map});
                mi.prepare();
                map = (DataMap) mi.invoke();                 

            } else {
                MethodInvoker mi = new MethodInvoker();
                mi.setStaticMethod(getCommand(map));
                mi.setArguments(new Object[] {map});
                mi.prepare();
                map = (DataMap) mi.invoke();
            }
        } catch (ClassNotFoundException e) {
            throw new ManagementException("해당하는 클래스를 찾지 못했습니다.");
        } catch (RuntimeException e) {
            throw e;
        } catch (Throwable e) {
            throw new ManagementException(e);
        }
        
		return map;
	}
    
    /**
     * 명령어를 얻어온다.
     * @param map
     * @return
     */
    private static String getCommand(DataMap map) {
        return map.getParameter("COMMAND");
    }
}
