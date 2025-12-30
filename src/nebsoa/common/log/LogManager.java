/*******************************************************************
 * Spider Framework Project
 *
 * Copyright (c) 2006-2007 SERVERSIDE Corp. All Rights Reserved.
 ******************************************************************/

package nebsoa.common.log;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import nebsoa.common.Context;
import nebsoa.common.monitor.ContextLogger;
import nebsoa.common.util.PropertyManager;
import nebsoa.common.util.StringUtil;

import org.apache.log4j.Appender;
import org.apache.log4j.Category;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.xml.DOMConfigurator;

/*******************************************************************
 * <pre>
 * 1.설명
 * 로그를 남기는 역할을 합니다.
 * XML Property logger
 * <p><p>
 * log_config.xml파일에 세팅된 정보를 읽어 로그를 남긴다
 * 일자가 바뀌면 자동으로 전날 이름으로 파일이 생성된다.
 *
 * 2.사용법
 * 사용법은 다음과 같다
 * <pre>
 * 개발시 디버깅할  내용: LogManager.debug("설정파일정보","남길 메세지");
 * 운영시 꼭 로깅할 내용: LogManager.info("설정파일정보","남길 메세지");
 * 운영시 에러난    내용: LogManager.error("설정파일정보","남길 메세지",exception객체);
 * 운영시는 info, error만 파일에 로그가 남는다.
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
 * $Log: LogManager.java,v $
 * Revision 1.1  2018/01/15 03:39:48  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:19  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:50  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:23  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:26  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.3  2008/09/12 06:58:21  jwlee
 * 개발자가 디버깅을 하기 쉽도록 uri 도 같이 출력 되도록 수정
 *
 * Revision 1.2  2008/09/12 06:51:59  jwlee
 * 개발자가 디버깅을 하기 쉽도록 uri 도 같이 출력 되도록 수정
 *
 * Revision 1.1  2008/08/04 08:54:51  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.8  2008/07/17 02:45:05  김재범
 * 콘솔창에 로그레벨별 색을 넣어 남기고 파일에 로그를 남길때는 로그내용만 기록하게 수정함.
 *
 * Revision 1.7  2008/04/11 05:56:11  김승희
 * LOG_CONFIG_DECO_YN 속성 최초 로딩 시 한번만 읽어오도록 수정
 *
 * Revision 1.6  2008/04/11 05:52:40  김승희
 * *** empty log message ***
 *
 * Revision 1.5  2008/04/11 05:50:50  김승희
 * 로그에 색깔 넣는 기능 수행시
 * default 프로퍼티로부터 적용여부(LOG_CONFIG_DECO_YN)를 판단하도록 수정
 *
 * Revision 1.4  2008/04/08 04:59:31  오재훈
 * 배경색 적용(DEBUG : 청녹, INFO : 보라 , ERROR : 빨강)
 *
 * Revision 1.3  2008/03/04 09:30:31  김은정
 * password log에 markedData적용
 *
 * Revision 1.2  2008/02/20 04:25:14  김성균
 * fwkDebug() 잘못 호출되는 부분 수정
 *
 * Revision 1.1  2008/01/22 05:58:19  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.2  2007/12/24 09:03:30  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2007/11/26 08:39:07  안경아
 * *** empty log message ***
 *
 * Revision 1.49  2007/11/02 09:18:24  jwlee
 * 프로세스 로그를 실시간 on/off및  프로세스 강제 종료 기능 추가
 *
 * Revision 1.48  2007/10/31 08:12:37  jwlee
 * compile 오류 처리
 *
 * Revision 1.47  2007/10/31 07:07:46  jwlee
 * Thread에 저장된 사용자 정보로 Context 출력 기능 추가
 *
 * Revision 1.5  2007/10/26 06:15:05  신정섭
 * 현재 운영중인 프로세스 모니터링 추가 관련
 *
 * Revision 1.4  2007/10/11 08:20:35  신정섭
 * 특정 사용자 로그 출력 하도록 기능 수정
 *
 * Revision 1.3  2007/07/19 13:34:36  김성수
 * 인덱스로그
 *
 * Revision 1.1  2007/06/15 04:57:57  안경아
 * *** empty log message ***
 *
 * Revision 1.36  2007/04/26 00:25:24  안경아
 * *** empty log message ***
 *
 * Revision 1.35  2007/03/15 00:42:55  김성균
 * 에러일 경우 로그레벨 ERROR로 수정
 *
 * Revision 1.34  2007/03/07 01:10:58  김성균
 * 로그요청한 프로그램 및 라인 출력하기 위해서 수정
 *
 * Revision 1.33  2007/03/06 08:56:13  김성균
 * 로그요청한 프로그램 및 라인 출력하기 위해서 수정
 *
 * Revision 1.32  2007/02/26 11:38:24  이종원
 * *** empty log message ***
 *
 * Revision 1.31  2007/02/26 11:29:02  이종원
 * LogManager자체 에러 출력 로직 추가
 *
 * Revision 1.30  2007/02/08 13:05:26  김성균
 * FILE_LOG_MODE 가 true일 경우에도 로그설정정보 읽어오도록 수정
 *
 * Revision 1.29  2007/02/06 05:40:53  김성균
 * isDebugEnabled() 메소드 추가
 *
 * Revision 1.28  2007/01/11 04:54:55  김성균
 * ErrorResponseException일 경우 trace 제외 수정
 *
 * Revision 1.27  2007/01/10 05:40:14  김성균
 * ErrorResponseException일 경우 trace 제외
 *
 * Revision 1.26  2007/01/04 08:54:04  이종원
 * 세션 종료 오류 인경우 trace생략
 *
 * Revision 1.25  2006/11/15 06:12:29  이종원
 * *** empty log message ***
 *
 * Revision 1.24  2006/11/10 08:43:09  이종원
 * getLoggers추가
 *
 * Revision 1.23  2006/10/21 14:28:07  김성균
 * *** empty log message ***
 *
 * Revision 1.22  2006/10/21 06:50:02  김성균
 * *** empty log message ***
 *
 * Revision 1.21  2006/10/13 03:53:47  이종원
 * getLogger update
 *
 * Revision 1.20  2006/10/03 05:11:01  이종원
 * *** empty log message ***
 *
 * Revision 1.19  2006/09/22 09:11:11  이종원
 * 기능update
 *
 * Revision 1.18  2006/08/19 02:02:59  김성균
 * *** empty log message ***
 *
 * Revision 1.17  2006/07/17 11:42:08  김성균
 * *** empty log message ***
 *
 * Revision 1.16  2006/07/10 04:23:23  김성균
 * *** empty log message ***
 *
 * Revision 1.15  2006/07/04 11:31:54  이종원
 * *** empty log message ***
 *
 * Revision 1.14  2006/06/21 04:34:15  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class LogManager {

	private static HashMap categoryHash = new HashMap();

    public static boolean CONSOLE_MODE = false;

    public static boolean FILE_LOG_MODE = true;

    public static boolean LOG_CONFIG_DECO_YN = false;

	static {
        try {
            String logFilePath = null;
            //try{
                logFilePath = PropertyManager.getProperty("default","LOG_CONFIG_FILE_PATH");
//            }catch(Exception e){
//                System.out.println("fail to find LOG_CONFIG_FILE_PATH... set Default Value..");
//                logFilePath=Constants.APP_HOME_DIR+"/properties/log_config.xml";
//            }

    		FILE_LOG_MODE = PropertyManager.getBooleanProperty("default","LOG.FILE_MODE","ON");
    		System.out.println("LOG_CONFIG_FILE-->"+logFilePath);
    		LOG_CONFIG_DECO_YN = PropertyManager.getBooleanProperty("default","LOG_CONFIG_DECO_YN","N");
    		System.out.println("LOG_CONFIG_DECO_YN-->"+LOG_CONFIG_DECO_YN);

    		init(logFilePath);
        } catch (Exception e) {
            CONSOLE_MODE = true;
            FILE_LOG_MODE = false;
            LOG_CONFIG_DECO_YN = false;
            Appender consoleAppender = new ConsoleAppender(new PatternLayout("%m%n"));
            Logger logger = Logger.getRootLogger();
            logger.setLevel(Level.DEBUG);
            logger.addAppender(consoleAppender);
            categoryHash.put("CONSOLE", logger);
            console("로그 설정을 로드하는데 실패하였습니다.");
            console("로그출력을 System.out으로 설정 합니다.");
        }
	}

	public static boolean isConsoleMode(){
		return PropertyManager.getBooleanProperty("default","LOG.CONSOLE_MODE","ON");
	}

	/**
     * 로그파일이 어디에 있는지를 알아 내어 로그남기는 클래스를 생성해낸다
     */
	public static void init(String logFilePath) {
		System.out.println("\n\t##############################################");
		System.out.println("\t#");
		System.out.println("\t# LOG.CONSOLE_MODE = [" + isConsoleMode() + "]");
		System.out.println("\t# LOG.FILE_MODE = [" + FILE_LOG_MODE + "]");
		System.out.println("\t# LOG_CONFIG_FILE_PATH = [" + logFilePath + "]");
		System.out.println("\t# LOG_CONFIG_DECO_YN = [" + LOG_CONFIG_DECO_YN + "]");
		System.out.println("\t#");
		System.out.println("\t##############################################\n");

        /*
		if (!FILE_LOG_MODE) {
            return;
        }
        */

		DOMConfigurator.configure(logFilePath);

		Enumeration catList = org.apache.log4j.LogManager.getCurrentLoggers();
        while (catList.hasMoreElements()) {
            Category category = (Category) catList.nextElement();
            String categoryName = category.getName();
            categoryHash.put(categoryName, category);
        }
	}

    public static void destroy(){
    	org.apache.log4j.LogManager.shutdown();
    }

	/**
	 * @param c
	 * @return
	 */
	public static Logger getLogger(Class c) {
		return org.apache.log4j.LogManager.getLogger(c);
	}

	/**
	 * @param name
	 * @return
	 */
	public static Logger getLogger(String name) {
		if (name == null) {
			return Logger.getLogger("DEBUG");
		} else {
			Logger logger = (Logger) categoryHash.get(name);
			if (logger == null) {
                System.out.println("log config file에 등록되지 않은 로그 정보 사용중 >>>" + name);

				// throw new SysException("log cinfig file에서 매치 되는 로그 모듈을 찾지 못함");
				try {
					Logger.getLogger("ERROR").error(
							"log config file에서 매치 되는 로그 모듈을 찾지 못함:[" + name+"]"
                            ,new Exception("LogManager 사용 오류:"+name));
				} catch (Exception e) {
                    e.printStackTrace();
				}
				return Logger.getRootLogger();
			}
			return logger;
		}
	}

	public static void fwkDebug(String categoryName, String msg) {
        console(msg);
        if (FILE_LOG_MODE) {
            getLogger(categoryName).log("LogManager", Level.DEBUG, msg, null);
        }
    }

    public static void debug(String categoryName, String msg) {

    	String decoratedMessage = appendDecoration(Level.DEBUG, msg);
        console(decoratedMessage);
        if (FILE_LOG_MODE) {
            try{
                Context ctx = ContextLogger.getContext();
                if(ctx != null){

                	//2008.07.14 콘솔창에는 Decoration(로그색)을 추가하지만 실제 파일에는 추가하지 않는다.(주석처리)
                	//decoratedMessage = appendDecoration(Level.DEBUG, appendContextInfo(msg));
                	decoratedMessage = appendContextInfo(msg);

                	if(ctx.isLogEnabled()){
                        getLogger(categoryName).log("LogManager", Level.ERROR, decoratedMessage, null);
                    }else{
                        getLogger(categoryName).log("LogManager", Level.DEBUG, decoratedMessage, null);
                    }

                    if(ctx.isForceShutdown()){
                		//error(ctx.getTrxSerNo()+"|"+ctx.getUserId()+"|관리자에 의해 강제 종료 시킵니다");
                    	//위처럼 하면 error 메소드에서 다시 한번 컨텍스트 정보를 붙이므로 아래와 같이 수정한다.
                    	error("관리자에 의해 강제 종료 시킵니다");
                		throw new Error(ctx.getTrxSerNo()+"|"+ctx.getUserId()+"|관리자에 의해 강제 종료");
                	}
                }else{
                	//2008.07.14 콘솔창에는 Decoration(로그색)을 추가하지만 실제 파일에는 추가하지 않는다.(decoratedMessage => msg 인자값 수정)
                    getLogger(categoryName).log("LogManager", Level.DEBUG, msg, null);
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void debugProperty(String key, Object value) {
        debug((String)null, key + " : " + value);
    }

    public static void infoProperty(String key, Object value) {
        info((String)null, key + " : " + value);
    }

    public static void errorProperty(String key, Object value) {
        error((String)null, key + " : " + value);
    }

    public static void infoLP(String categoryName, String msg) {
        info(categoryName, sharpLine + "\n " + sharpLine2 + msg);
    }

    public static void infoP(String categoryName, String msg) {
        info(categoryName, sharpLine2 + " " + msg);
    }

    public static void info(String categoryName, String msg) {
    	//msg = "[45m "+msg;
    	//console(msg+" [0m");

    	String decoratedMessage = appendDecoration(Level.INFO, msg);
    	console(decoratedMessage);

        if (FILE_LOG_MODE) {

            try{
                Context ctx = ContextLogger.getContext();
                if(ctx != null){
                	if(!"MESSAGE".equals(categoryName)
                			&& !"USER_ACCESS".equals(categoryName)
                			&& !"SQL".equals(categoryName)
                			&& !"HEXA_MSG".equals(categoryName)){
                		//msg=ctx.getTrxSerNo()+"|"+ctx.getUserId()+"|"+msg;
                		//2008.07.14 콘솔창에는 Decoration(로그색)을 추가하지만 실제 파일에는 추가하지 않는다.(주석처리)
                		//decoratedMessage = appendDecoration(Level.INFO, appendContextInfo(msg));
                		decoratedMessage = appendContextInfo(msg);
                	}
                	//msg += " [0m";
                	//2008.07.14 콘솔창에는 Decoration(로그색)을 추가하지만 실제 파일에는 추가하지 않는다.(decoratedMessage => msg 인자값 변경)
                    if(ctx.isLogEnabled()){
                        getLogger(categoryName).log("LogManager", Level.ERROR, msg, null);
                    }else{
                        getLogger(categoryName).log("LogManager", Level.INFO, msg, null);
                    }

                    if(ctx.isForceShutdown()){
                		error(ctx.getTrxSerNo()+"|"+ctx.getUserId()+"|관리자에 의해 강제 종료 시킵니다");
                		throw new Error(ctx.getTrxSerNo()+"|"+ctx.getUserId()+"|관리자에 의해 강제 종료");
                	}
                }else{
                	//msg += " [0m";
            		//2008.07.14 콘솔창에는 Decoration(로그색)을 추가하지만 실제 파일에는 추가하지 않는다.(decoratedMessage => msg 인자값 변경)
                    getLogger(categoryName).log("LogManager", Level.INFO, msg, null);
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }



//            getLogger(categoryName).info(msg);
        }// end if
    }

    public static void error(String categoryName, String msg) {

    	String decoratedMessage = appendDecoration(Level.ERROR, msg);
        console("================== ERROR ====================\n" + decoratedMessage);

        if (FILE_LOG_MODE) {
    		//2008.07.14 콘솔창에는 Decoration(로그색)을 추가하지만 실제 파일에는 추가하지 않는다.(주석처리)
        	//decoratedMessage = appendDecoration(Level.ERROR, appendContextInfo(msg));
        	decoratedMessage = appendContextInfo(msg);
            getLogger(categoryName).log("LogManager", Level.ERROR, decoratedMessage, null);

        }//end if
    }

	/**
	 * 인자로 받은 메시지 앞에 Context 정보를 붙여 리턴한다.
	 * Context가 null일 경우 원래 메시지를 그대로 리턴한다.
	 *
	 * Context 정보는 주로 거래추적번호 | 사용자ID | 의 형태이다.
	 * @param msg
	 * @return context 정보가 붙은 메시지
	 */
	private static String appendContextInfo(String msg) {
		try{
		    Context ctx = ContextLogger.getContext();
		    if(ctx != null){
		        msg = new StringBuffer(ctx.getTrxSerNo()).append("|").append(ctx.getUserId()).append("|").append(ctx.getUri()).append("|").append(msg).toString();
		    }
		}catch(Exception e){
		    e.printStackTrace();
		}
		return msg;
	}

	/**
     * 콘솔에 에러로그를 남기고, 파일로그모드가 설정되어 있을경우 파일에도 로그를 남긴다.
	 * @param categoryName
	 * @param msg
	 * @param e
	 */
	public static void error(String categoryName, String msg, Throwable e) {

        e.printStackTrace(System.err);

        String decoratedMessage = appendDecoration(Level.ERROR, msg);
        /*try{
            Context ctx = ContextLogger.getContext();
            if(ctx != null){
            	decoratedMessage = appendDecoration(Level.ERROR,
            			new StringBuffer().append(ctx.getTrxSerNo()).append("|").append(ctx.getUserId()).append("|").append(msg).toString());
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }*/
        //위 주석 부분을 아래와 같이 수정
        decoratedMessage = appendDecoration(Level.ERROR, appendContextInfo(msg));

        console("================== ERROR ====================\n" + e.getMessage()+"("+decoratedMessage+")");

        if (categoryName == null) {
            categoryName = "ERROR";
        }

		//2008.07.14 콘솔창에는 Decoration(로그색)을 추가하지만 실제 파일에는 추가하지 않는다.(decoratedMessage => msg 인자값 변경)
        if (FILE_LOG_MODE) {
            if (e instanceof nebsoa.common.exception.LoginException
                    || (e != null && e.getCause() instanceof nebsoa.spiderlink.exception.ErrorResponseException)) {
                getLogger(categoryName).error(msg);
            } else {
                try{
                    getLogger(categoryName).error(msg, e);
                }catch(Throwable ex){
                    System.err.println("$$$$$$$$$$$$$ LogManager 수행중 에러 :");
                    ex.printStackTrace();
                    System.err.println("\t################################# 출력하려던 오류 메시지 " + decoratedMessage
                          +  "\n\t#########################################");
                }
            }
        }
	}

    static String sharpLine = "\t##################################################";

    static String sharpLine2 = "\t# ";

    /**
     * 강조하여 출력한다.
     */
    public  static void debugP(Object msg){
        debug((String)null,sharpLine2+getString(msg));
    }

    /**
     * 강조하여 출력한다.
     */
    public  static void debugLP(Object msg){
        debug((String)null,sharpLine+"\n "+sharpLine2+" "+getString(msg));
    }

	public  static void fwkDebug(Object msg){
		fwkDebug((String)null,getString(msg));
	}

    public  static void debug(Object msg){
        debug((String)null,getString(msg));
    }

    public  static void debug(String msg){
        debug((String)null,msg==null?"null":msg);
    }

    /**
     * 강조하여 출력한다.
     */
    public static void infoP(Object msg) {
        info((String)null, sharpLine2 + getString(msg));
    }

    /**
     * 강조하여 출력한다.
     */
    public static void infoLP(Object msg) {
        info((String)null, sharpLine + "\n " + sharpLine2 + getString(msg));
    }

    public static void info(Object msg) {
        info((String)null, getString(msg));
    }

    public static void error(Object msg) {
        error(null, getString(msg));
    }

    public static void error(Throwable e) {
        error(null, e.getMessage(), e);
    }

	public static void error(Object msg, Throwable e) {
        error(null, getString(msg), e);
    }

    public static String getString(Object obj) {
        if (obj == null) {
            return "null";
        }
        if (obj instanceof String) {
            return (String) obj;
        } else {
            return obj.toString();
        }
    }

    public static void debug(Context ctx, String categoryName, String msg) {
    	debug(categoryName, msg);
    }

    public static void debug(Context ctx,  String msg) {
    	debug(msg);
    }

    public static void info(Context ctx, String categoryName, String msg) {
    	info(categoryName, msg);
    }

    public static void info(Context ctx,  String msg) {
    	info(msg);
    }

    /**
     * 2006. 07. 17 김성균
     * 콘솔창에 로그 메시지를 Log4j를 사용하도록 수정...
     * @param message
     */
    public static void console(String message) {
        if (isConsoleMode()) {
            getLogger("CONSOLE").log("LogManager", Level.DEBUG, message, null);
        }
    }

	/**
     * 2006. 07. 17 김성균
     * 콘솔창에 로그 메시지를 Log4j를 사용하도록 수정...
     * @param message
     */
    public static void console(Object message) {
        if (isConsoleMode())
            getLogger("CONSOLE").log("LogManager", Level.DEBUG, message, null);
    }

	/**
     * 현재 request객체에 담겨 있는 값을 일괄 출력
     */
    public static void debugRequest(HttpServletRequest request) {

		fwkDebug(request.getRequestURI()+"의 입력된 Request Parameter 정보");
		Enumeration e = request.getParameterNames();
		String debugStr=null;
		while(e.hasMoreElements()){
			String paramName = (String)e.nextElement();
			String[] paramValues = request.getParameterValues(paramName);
			if(paramValues.length ==0 ){
			    fwkDebug(paramName+":null");
			}else{
			    debugStr = paramName+"[";
			    for(int i=0;i<paramValues.length;i++){
			    	String value = StringUtil.maskedData(paramName,paramValues[i]);
			        //debugStr = debugStr+paramValues[i]+",";
			    	debugStr = debugStr+value+",";
			    }
			    fwkDebug(debugStr+"]");
			}
		}
	}


	public static void main(String[] args){
		/*System.setProperty("NEBSOA_HOME","C:/hanabank");
	    java.util.ArrayList arr = new ArrayList();
		arr.add("홍길동");
		arr.add("성춘향");
		debugProperty("이름", arr);
        LogManager.debug("MESSAGE","TEST...");

        Logger logger = LogManager.getLogger("DEBUG");
        Level level = logger.getLevel();
        if(level==null){
            level = Level.DEBUG;
        }
        System.out.println("Level:"+level.toString());
        Enumeration e=logger.getAllAppenders();
        while(e.hasMoreElements()){
            Appender apnd=(Appender)e.nextElement();
            System.out.println(apnd.getName());
        }*/
		Context ctx = new Context();
		ctx.setTrxSerNo("123445678");
		ctx.setUserId("GUEST");
		ContextLogger.putContext(ctx);
		LogManager.debug("11111");
		LogManager.info("11111");
		LogManager.error("11111");
	}

    /**
     * Return the native Logger instance we are using.
     */
    public static HashMap getLoggers() {
        return categoryHash;
    }

    /**
     * Check whether the Log4j Logger used is enabled for <code>DEBUG</code> priority.
     * @param name
     */
    public static boolean isDebugEnabled(String name) {
        return LogManager.getLogger(name).isDebugEnabled();
    }

    /**
     * Check whether the Log4j Logger used is enabled for <code>DEBUG</code> priority.
     */
    public static boolean isDebugEnabled() {
        return Logger.getRootLogger().isDebugEnabled();
    }

    /**
     * 로그레벨에 따라 메시지 앞뒤에 특정 문자열을 붙여서 리턴한다.
     *
     * @param logLevel
     * @param msg
     * @return
     */
    private static String appendDecoration(Level logLevel, String msg){
    	try{
		    Context ctx = ContextLogger.getContext();
		    if(ctx != null){
		        msg = new StringBuffer(ctx.getTrxSerNo()).append("|").append(ctx.getUserId()).append("|").append(ctx.getUri()).append("|").append(msg).toString();
		    }
		}catch(Exception e){
		    e.printStackTrace();
		}
    	if(LOG_CONFIG_DECO_YN){
	    	if(logLevel == Level.DEBUG ){
	    		msg = new StringBuffer("[46m ").append(msg).append(" [0m").toString();
	    	}else if(logLevel == Level.INFO){
	    		msg = new StringBuffer("[45m ").append(msg).append(" [0m").toString();
	    	}else if(logLevel == Level.ERROR){
	    		msg = new StringBuffer("[41m ").append(msg).append(" [0m").toString();
	    	}
    	}

    	return msg;

    }
}
