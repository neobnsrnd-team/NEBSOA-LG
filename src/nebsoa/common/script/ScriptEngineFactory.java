/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.script;

import java.util.HashMap;
import java.util.Map;

/*******************************************************************
 * <pre>
 * 1.설명 
 * ScriptEngine 객체를 캐싱하고 주어진 타입에 대한 ScriptEngine 객체를 반환하는 팩토리 클래스
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
 * $Log: ScriptEngineFactory.java,v $
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
public class ScriptEngineFactory {
	
	/**
	 * Singleton 사용을 위한 자기 자신의 객체
	 */
	private static ScriptEngineFactory instance = new ScriptEngineFactory();
	/**
	 * 각 스크립트 언어 별 스크립트 엔진의 cache
	 */
	private Map scriptEngineMap;
	
	/**
	 * Singleton 사용을 위한 private 생성자
	 *
	 */
	private ScriptEngineFactory() {
		this.scriptEngineMap = new HashMap();
	}//end of constructor
	
	/**
	 * Singleton 사용을 위한 instance 리턴 메소드
	 * 
	 * @return ScriptEngineFactory 객체
	 */
	public static ScriptEngineFactory getInstance() {
		return instance;
	}//end of getInstance()
	
	/**
	 * 주어진 스크립트 엔진 타입에 대한 ScriptEngine 객체를 리턴합니다.
	 * 
	 * @param type 스크립트 엔진 타입
	 * @return 주어진 스크립트 엔진 타입에 대한 ScriptEngine 객체
	 */
	public ScriptEngine getScriptEngine(ScriptEngineType type) {
		if (this.scriptEngineMap.containsKey(type)) {
			return (ScriptEngine)this.scriptEngineMap.get(type);
		} else {
			ScriptEngine engine = new ScriptEngine(type);
			
			this.scriptEngineMap.put(type, engine);
			
			return engine;
		}//end if else
	}//end of getScriptEngine()

}// end of ScriptEngineFactory.java