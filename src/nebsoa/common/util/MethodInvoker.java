/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import nebsoa.common.exception.SysException;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 동적으로 객체의 메소드를 호출하는 클래스
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
 * $Log: MethodInvoker.java,v $
 * Revision 1.1  2018/01/15 03:39:49  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:31  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:23  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:26  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:50  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:17  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:04  안경아
 * *** empty log message ***
 *
 * Revision 1.8  2006/11/02 06:45:30  김성균
 * *** empty log message ***
 *
 * Revision 1.7  2006/10/24 02:48:01  김성균
 * *** empty log message ***
 *
 * Revision 1.6  2006/10/02 04:59:14  오재훈
 * biz에서 invoke할때 에러 발생시 두번 SysExceptin으로 감싸던 버그 수정
 *
 * Revision 1.5  2006/08/21 02:47:25  오재훈
 * *** empty log message ***
 *
 * Revision 1.4  2006/08/21 01:04:03  오재훈
 * invoke 메소드의 에러 처리 구문 임시 수정.
 *
 * Revision 1.3  2006/07/26 08:04:54  김성균
 * setStaticMethod의 setTargetClass()부분 수정
 *
 * Revision 1.2  2006/07/25 02:25:55  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2006/06/26 02:55:18  김성균
 * 최초 등록
 *
 * Revision 1.3  2006/06/17 10:37:45  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class MethodInvoker {
	
	private Class targetClass;

	private Object targetObject;

	private String targetMethod;

	private Object[] arguments;

	private Method methodObject;
	

	/**
	 * @return Returns the targetClass.
	 */
	public Class getTargetClass() {
		return targetClass;
	}

	/**
	 * @param targetClass The targetClass to set.
	 */
	public void setTargetClass(Class targetClass) {
		this.targetClass = targetClass;
	}

	/**
	 * @return Returns the arguments.
	 */
	public Object[] getArguments() {
		return arguments;
	}

	/**
	 * @param arguments The arguments to set.
	 */
	public void setArguments(Object[] arguments) {
		this.arguments = arguments;
	}

	/**
	 * @return Returns the methodObject.
	 */
	public Method getMethodObject() {
		return methodObject;
	}

	/**
	 * @param methodObject The methodObject to set.
	 */
	public void setMethodObject(Method methodObject) {
		this.methodObject = methodObject;
	}

	/**
	 * @return Returns the targetMethod.
	 */
	public String getTargetMethod() {
		return targetMethod;
	}

	/**
	 * @param targetMethod The targetMethod to set.
	 */
	public void setTargetMethod(String targetMethod) {
		this.targetMethod = targetMethod;
	}

	/**
	 * @return Returns the targetObject.
	 */
	public Object getTargetObject() {
		return targetObject;
	}

	/**
	 * Set the target object on which to call the target method.
	 * Only necessary when the target method is not static;
	 * else, a target class is sufficient.
	 * @param targetObject The targetObject to set.
	 */
	public void setTargetObject(Object targetObject) {
		this.targetObject = targetObject;
		if (targetObject != null) {
			this.targetClass = targetObject.getClass();
		}
	}
	
	/**
	 * Set a fully qualified static method name to invoke, 
	 * e.g. "example.MyExampleClass.myExampleMethod". 
	 * Convenient alternative to specifying targetClass and targetMethod.
	 * 
	 * @see #setTargetClass
	 * @see #setTargetMethod
	 */
	public void setStaticMethod(String staticMethod)
			throws ClassNotFoundException {
		int lastDotIndex = staticMethod.lastIndexOf('.');
		if (lastDotIndex == -1 || lastDotIndex == staticMethod.length()) {
			throw new IllegalArgumentException(
					"staticMethod must be a fully qualified class plus method name: "
							+ "e.g. 'example.MyExampleClass.myExampleMethod'");
		}
		String className = staticMethod.substring(0, lastDotIndex);
		String methodName = staticMethod.substring(lastDotIndex + 1);
		/*setTargetClass(Class.forName(className, true, Thread.currentThread()
				.getContextClassLoader()));*/
		setTargetClass(Class.forName(className));
		setTargetMethod(methodName);
	}

	/**
	 * Prepare the specified method.
	 * The method can be invoked any number of times afterwards.
	 */
	public void prepare() {
		if (this.targetClass == null) {
			throw new IllegalArgumentException("Either targetClass or targetObject is required");
		}

		if (this.targetMethod == null) {
			throw new IllegalArgumentException("targetMethod is required");
		}

		if (this.arguments == null) {
			this.arguments = new Object[0];
		}

		Class[] argTypes = new Class[this.arguments.length];
		for (int i = 0; i < this.arguments.length; ++i) {
			argTypes[i] = (this.arguments[i] != null ? this.arguments[i].getClass() : Object.class);
		}

		try {
//			this.methodObject = this.targetObject.getClass().getMethod(this.targetMethod, argTypes);
			this.methodObject = this.targetClass.getMethod(this.targetMethod, argTypes);
		}
		catch (NoSuchMethodException ex) {
			throw new SysException("메소드를 찾을 수 없습니다:" + targetMethod);
		}

		if (this.targetObject == null && !Modifier.isStatic(this.methodObject.getModifiers())) {
			throw new IllegalArgumentException("Target method must not be non-static without a target");
		}
	}
	
	/**
	 * @return
	 */
	public Object invoke() throws Throwable {
		Object result = null;
		try {
			result =  this.methodObject.invoke(this.targetObject, this.arguments);
		} catch (IllegalArgumentException e) {
			throw e;
		} catch (IllegalAccessException e) {
			throw e;
		} catch (InvocationTargetException e) {
			throw e.getTargetException();
		}
		return result;
	}
}
