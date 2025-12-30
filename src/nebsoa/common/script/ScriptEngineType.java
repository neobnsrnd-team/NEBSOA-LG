/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.script;

import java.util.ArrayList;
import java.util.List;

/*******************************************************************
 * <pre>
 * 1.설명 
 * script 의 type 을 나타내는 type-safed-enumeration 클래스
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
 * $Log: ScriptEngineType.java,v $
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
 * Revision 1.1  2008/01/22 05:58:33  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:37:37  안경아
 * *** empty log message ***
 *
 * Revision 1.4  2006/06/17 10:37:45  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class ScriptEngineType {
	
	/**
	 * 모든 script 타입을 나타내는 상수
	 */
	private static final List ALL_TYPES = new ArrayList();
	
	/**
	 * script-language로 javascript 를 나타내는 상수 (JSR-223)
	 */
	private static final String SCRIPT_LANGUAGE_JAVASCRIPT = "javascript";
	/**
	 * JSR-223 에서 사용하는 javascript 용 Engine 이름
	 */
	private static final String SCRIPT_ENGINE_JAVASCRIPT = "javascript";
	/**
	 * javascript 용 파일 확장
	 */
	private static final String [] SCRIPT_EXTENSION_JAVASCRIPT = new String[] {"js"};

	/**
	 * script-language로 groovy 를 나타내는 상수
	 */
	private static final String SCRIPT_LANGUAGE_GROOVY = "groovy";
	/**
	 * JSR-223 에서 사용하는 groovy 용 Engine 이름
	 */
	private static final String SCRIPT_ENGINE_GROOVY = "groovy";
	/**
	 * groovy 용 파일 확장
	 */
	private static final String [] SCRIPT_EXTENSION_GROOVY = new String[] {"groovy", "gy"};

	/**
	 * script-language로 bsh 를 나타내는 상수
	 * Note: BeanShell은 JSR-223에서 "beanshell" 또는 "bsh"로 등록됨
	 */
	private static final String SCRIPT_LANGUAGE_BSH = "beanshell";
	/**
	 * JSR-223 에서 사용하는 bsh 용 Engine 이름
	 */
	private static final String SCRIPT_ENGINE_BSH = "beanshell";
	/**
	 * bsh 용 파일 확장
	 */
	private static final String [] SCRIPT_EXTENSION_BSH = new String[] {"bsh"};

	/**
	 * script-language로 jython 를 나타내는 상수
	 */
	private static final String SCRIPT_LANGUAGE_JYTHON = "python";
	/**
	 * JSR-223 에서 사용하는 jython 용 Engine 이름
	 */
	private static final String SCRIPT_ENGINE_JYTHON = "python";
	/**
	 * jython 용 파일 확장
	 */
	private static final String [] SCRIPT_EXTENSION_JYTHON = new String[] {"py"};
	
	/**
	 * script-language
	 */
	private String language;
	/**
	 * script-engine
	 */
	private String engine;
	/**
	 * script 파일 확장자
	 */
	private String [] extension;
	
	/**
	 * script-language 및 script-engine 을 초기화 하기 위한 생성자
	 * 
	 * @param language script-language
	 * @param engine script-engine
	 * @param extension script 파일 확장자
	 */
	private ScriptEngineType(String language, String engine, String [] extension) {
		this.language = language;
		this.engine = engine;
		this.extension = extension;
		ALL_TYPES.add(this);
	}//end of constructor
	
	public String getEngine() {
		return engine;
	}//end of getEngine()

	public void setEngine(String engine) {
		this.engine = engine;
	}//end of setEngine()

	public String getLanguage() {
		return language;
	}//end of getLanguage()

	public void setLanguage(String language) {
		this.language = language;
	}//end of setLanguage()
	
	public String[] getExtension() {
		return extension;
	}//end of getExtension()

	public void setExtension(String[] extension) {
		this.extension = extension;
	}//end of setExtension()
	
	/**
	 * script-engine-type 의 속성을 문자열 형태로 변환하여 리턴합니다.
	 * 
	 * @return 속성의 문자열
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("language : [").append(this.language);
		buffer.append("] / engine : [").append(this.engine);
		buffer.append("] / extension [");
		if (this.extension != null) {
			for (int i = 0; i < this.extension.length; i++) {
				buffer.append(this.extension[i]);
				buffer.append((i != this.extension.length - 1) ? ":" : "");
			}//end for
		}//end if
		buffer.append("]");
		return buffer.toString();
	}//end of toString()
	
	/**
	 * javascript 를 나타내는 상수
	 */
	public static final ScriptEngineType JAVASCRIPT = new ScriptEngineType(SCRIPT_LANGUAGE_JAVASCRIPT, SCRIPT_ENGINE_JAVASCRIPT, SCRIPT_EXTENSION_JAVASCRIPT);
	/**
	 * groovy 를 나타내는 상수
	 */
	public static final ScriptEngineType GROOVY = new ScriptEngineType(SCRIPT_LANGUAGE_GROOVY, SCRIPT_ENGINE_GROOVY, SCRIPT_EXTENSION_GROOVY);
	/**
	 * bsh 를 나타내는 상수
	 */
	public static final ScriptEngineType BSH = new ScriptEngineType(SCRIPT_LANGUAGE_BSH, SCRIPT_ENGINE_BSH, SCRIPT_EXTENSION_BSH);
	/**
	 * jython 를 나타내는 상수
	 */
	public static final ScriptEngineType JYTHON = new ScriptEngineType(SCRIPT_LANGUAGE_JYTHON, SCRIPT_ENGINE_JYTHON, SCRIPT_EXTENSION_JYTHON);
	
	/**
	 * 사용 가능한 모든 ScriptEngineType 의 배열을 리턴합니다.
	 * 
	 * @return ScriptEngineType 의 배열
	 */
	public static ScriptEngineType [] getAllScriptTypes() {
		return (ScriptEngineType [])ALL_TYPES.toArray();
	}//end of getAllScriptTypes()
	
	/**
	 * 주어진 문자열 타입에 해당하는 ScriptEngineType 객체를 리턴합니다.
	 * 
	 * 주어진 문자열 타입을 지원하지 않는다면, 기본으로 JAVASCRIPT 를 리턴합니다.
	 * 
	 * @param type 문자열 형태의 script-type
	 * @return ScriptEngineType 객체
	 */
	public static ScriptEngineType getEngineType(String type) {
		if (JAVASCRIPT.getLanguage().equals(type)) {
			return JAVASCRIPT;
		} else if (JAVASCRIPT.getLanguage().equals(type)) {
			return GROOVY;
		} else if (BSH.getLanguage().equals(type)) {
			return BSH;
		} else if (JYTHON.getLanguage().equals(type)) {
			return JYTHON;
		}else {
			return JAVASCRIPT;
		}//end of if else
	}//end of getEngineType()

}// end of ScriptEngineType.java