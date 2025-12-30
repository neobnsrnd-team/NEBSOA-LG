/*******************************************************************
 * 프로젝트 공통모듈
 *
 * Copyright (c) 2003-2004 by serverside.co.kr, Inc.
 * All rights reserved.
 * @file : MonitorLogger.java
 * @author : 이종원
 * @date : 2004. 10. 29.
 * @설명 : 
 * <pre>
 * 로그를 남기는 역할을 합니다.
 * </pre>
 *******************************************************************
 * $Log: MonitorLogger.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:28  cvs
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
 * Revision 1.1  2008/08/04 08:54:52  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.2  2008/02/14 06:14:28  오재훈
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:24  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:07  안경아
 * *** empty log message ***
 *
 * Revision 1.3  2006/11/21 08:11:44  이종원
 * 최초등록
 *
 *    
 ******************************************************************/
package nebsoa.plugin.monitor.util;

/**
 *
 * 로그를 남기는 역할을 합니다.
 * @author 이종원
 */

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;


/**
 * <p>
 * 남길 데이터를 log_config.xml파일에 세팅된 정보를 읽어  해당 파일이나, 콘솔에 로그를 남긴다
 * 파일 로그인 경우 일자가 바뀌면 자동으로 전날 이름으로 파일이 생성된다.
 * 사용법은 다음과 같다 
 * <pre>
 * 개발시 디버깅할  내용: MonitorLogger.debug("설정파일정보","남길 메세지");
 * 운영시 꼭 로깅할 내용: MonitorLogger.info("설정파일정보","남길 메세지");
 * 운영시 에러난    내용: MonitorLogger.error("설정파일정보","남길 메세지",exception객체);
 * 운영시는 info, error만 파일에 로그가 남는다.
 * </pre>
 * @author  이종원
 * @version 1.00, 2003.02.01
 * @since   log4j-1.2.7
 */

public class MonitorLogger {


	private static Hashtable categoryHash = new Hashtable();

	public static boolean CONSOLE_MODE=true;
	
	public static boolean FILE_LOG_MODE = true;

	
	public static boolean isConsoleMode(){
		return false;
        //return PropertyManager.getBooleanProperty("default","LOG.CONSOLE_MODE","ON");		
	}

    static{
        init();
    }

	/**
	* 로그파일이 어디에 있는지를 알아 내어 로그남기는 클래스를 생성해낸다
	*/
	public static void init() {
		//default는 기업은행 config 파일 경로임
        String logFilePath=
            System.getProperty("NEBSOA_HOME","/was61/nebsoa_home")+"/config/properties/log_config.xml";

        System.out.println("\n\t##############################################");
		System.out.println("\t#");
		System.out.println("\t# NEBSOA MONITOR LOG CONSOLE_MODE = [" + isConsoleMode() + "]");
		System.out.println("\t# NEBSOA MONITOR LOG FILE_MODE = [" + FILE_LOG_MODE + "]");
		System.out.println("\t# LOG_CONFIG_FILE_PATH = [" + logFilePath + "]");
		System.out.println("\t#");
		System.out.println("\t##############################################\n");

		if (!FILE_LOG_MODE) {
			return;
		}//end if
		DOMConfigurator.configure(logFilePath);

		Enumeration catList = Category.getCurrentCategories();
		while (catList.hasMoreElements())
		{
			Category category = (Category) catList.nextElement();
			String categoryName = category.getName();

			categoryHash.put(categoryName, category);
			//System.out.println("category Name = " + category.getName());
		}
	}
    
    

	public static Logger getLogger(String name) {
		if(name==null){
			return (Logger)Logger.getRootLogger();
		}else{
			Logger logger = (Logger) categoryHash.get(name);
			if(logger==null){
				//throw new SysException("log cinfig file에서 매치 되는 로그 모듈을 찾지 못함");
				try{
					Logger.getLogger("ERROR").error("log config file에서 매치 되는 로그 모듈을 찾지 못함:"+name);
				}catch(Exception e){
					System.out.println("log cinfig file에서 매치 되는 로그 모듈을 찾지 못함:"+name);
				}
				return (Logger)Logger.getRootLogger();
			}
			return logger;
		}
        //return (Logger)Logger.getRootLogger();
	}

	public  static void debug(String categoryName,String msg){
		console(msg);
		if (FILE_LOG_MODE) {
			getLogger(categoryName).debug(msg);
		}//end if
	}

	public  static void debugProperty(String key, Object value){
		debug(null, key + " : " + value);
	}
	
	public  static void infoProperty(String key, Object value){
	    info(null, key + " : " + value);
	}
	
	public  static void errorProperty(String key, Object value){
	    error(null, key + " : " + value);
	}
	
	public  static void info(String categoryName,String msg){
		console(msg);
		if (FILE_LOG_MODE) {
			getLogger(categoryName).info(msg);
		}//end if
	}

	public  static void error(String categoryName,String msg){
		console("================== ERROR ====================\n"
		        +msg);
		if (FILE_LOG_MODE) {
			getLogger(categoryName).error("##ERROR:"+msg);
		}//end if
	}

	public  static void error(String categoryName,String msg,Throwable e){
		e.printStackTrace(System.err);
		console("================== ERROR ====================\n"+e.getMessage());
		console(msg);
		if(categoryName == null) {
			categoryName = "ERROR";
		}
		if (FILE_LOG_MODE) {
			getLogger(categoryName).error("##ERROR:"+msg,e);
		}//end if
	}
	
	public  static void debug(Object msg){
		debug(null,getString(msg));
	}
	
	public  static void info(Object msg){
		info(null,getString(msg));
	}

	public  static void error(Object msg){
		error(null,getString(msg));
	}

	public  static void error(Object msg, Throwable e){
		error(null,getString(msg),e);
	}

	public static String getString(Object obj){
		if(obj==null){
			return "null";
		}
		if(obj instanceof String){
			return (String)obj;
		}else{
			return obj.toString();
		}
	}
	public static void console(String message){
		if(isConsoleMode())System.out.println(message);
	}

	public static void console(Object message){
		if(isConsoleMode())System.out.println(message);
	}

	/**
	* 현재 request객체에 담겨 있는 값을 일괄 출력
	*/
	public static void debugRequest(HttpServletRequest request){

		debug(request.getRequestURI()+"의 입력된 Request Parameter 정보");
		Enumeration e = request.getParameterNames();
		String debugStr=null;
		while(e.hasMoreElements()){
			String paramName = (String)e.nextElement();
			String[] paramValues = request.getParameterValues(paramName);
			if(paramValues.length ==0 ){
			    debug(paramName+":null");
			}else{
			    debugStr = paramName+"[";
			    for(int i=0;i<paramValues.length;i++){
			        debugStr = debugStr+paramValues[i]+",";
			    }
			    debug(debugStr+"]");
			}
		}
	}
	
	public static void main(String[] args){
	    java.util.ArrayList arr = new ArrayList();
		arr.add("홍길동");
		arr.add("성춘향");
		debugProperty("이름", arr);
	}
}
