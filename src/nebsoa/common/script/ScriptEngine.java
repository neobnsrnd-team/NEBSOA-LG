/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.script;

import javax.script.Bindings;
import javax.script.ScriptEngineManager;
import javax.script.SimpleBindings;

import nebsoa.common.log.LogManager;

/*******************************************************************
 * <pre>
 * 1.설명 
 * script-language 별 로딩이 가능하게 하기 위한 script-engine 클래스.
 * default 설정은 Javascript 로 되어 있으나, Python, Tcl, NetRexx, XSLT Stylesheets,
 * Java(BeanShell), Groovy, JRuby 등 BSF 에서 지원하는 어떠한 script-language도 설정 가능함.
 * script-language 에 대한 자세한 사항은 http://jakarta.apache.org/bsf 에서 찾아 보시길...
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
 * $Log: ScriptEngine.java,v $
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
 * Revision 1.2  2006/06/17 10:37:45  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class ScriptEngine {

	/**
	 * script-engine 의 type
	 */
	private ScriptEngineType type;
	/**
	 * javax.script.ScriptEngine 객체 (JSR-223)
	 */
	private javax.script.ScriptEngine engine;
	/**
	 * 스크립트 변수 바인딩
	 */
	private Bindings bindings;

	/**
	 * script-engine 을 초기화 하기 위한 생성자
	 *
	 * @param type script-engine 의 type
	 */
	ScriptEngine(ScriptEngineType type) {
		this.type = type;
		LogManager.debug("Initialize Script Engine - " + type);
		ScriptEngineManager manager = new ScriptEngineManager();
		this.engine = manager.getEngineByName(type.getLanguage());
		if (this.engine == null) {
			throw new ScriptException("script-engine [" + type.getLanguage() + "] 을 찾을 수 없습니다.");
		}
		this.bindings = new SimpleBindings();
	}//end of constructor

	/**
	 * script-engine 에서 사용할 클래스를 등록합니다.
	 *
	 * @param beanVarName 스크립트 내 변수 명
	 * @param beanObject 스크립트에서 사용할 bean 객체
	 * @param beanClass 스크립트에서 사용할 bean 클래스
	 * @throws ScriptException script-engine 에 bean 을 등록하는 중 발생하는 Exception
	 */
	public void declareBean(String beanVarName, Object beanObject, Class beanClass) throws ScriptException {
		try {
			this.bindings.put(beanVarName, beanObject);
			this.engine.put(beanVarName, beanObject);
		} catch (Exception e) {
			throw new ScriptException("script-engine 에 [" + beanClass.getName() + "] 을 등록하는 중 오류가 발생하였습니다.", e);
		}//end try catch
	}//end of declareBean()

	/**
	 * 주어진 스크립트를 실행합니다.
	 *
	 * @param scriptCode 실행하려는 스크립트 코드
	 * @throws ScriptException 스크립트 실행 중 발생하는 Exception
	 */
	public void exec(String scriptCode) throws ScriptException {
		try {
			this.engine.eval(scriptCode, this.bindings);
		} catch (javax.script.ScriptException e) {
			throw new ScriptException("script-engine 에서 [" + scriptCode + "] 을 실행하는 중 오류가 발생하였습니다.", e);
		}//end try catch
	}//end of exec()

	/**
	 * 주어진 스크립트를 실행하고 결과를 리턴합니다.
	 *
	 * @param scriptCode 실행하려는 스크립트 코드
	 * @return 스크립트 코드의 실행결과
	 * @throws ScriptException 스크립트 실행 중 발생하는 Exception
	 */
	public Object eval(String scriptCode) throws ScriptException {
		try {
			return this.engine.eval(scriptCode, this.bindings);
		} catch (javax.script.ScriptException e) {
			throw new ScriptException("script-engine 에서 [" + scriptCode + "] 을 실행하는 중 오류가 발생하였습니다.", e);
		}//end try catch
	}//end of exec()

	/**
	 * javax.script.ScriptEngine 을 리턴합니다.
	 *
	 * @return javax.script.ScriptEngine 객체
	 */
	public javax.script.ScriptEngine getScriptEngine() {
		return this.engine;
	}//end of getScriptEngine()

}// end of ScriptEngine.java