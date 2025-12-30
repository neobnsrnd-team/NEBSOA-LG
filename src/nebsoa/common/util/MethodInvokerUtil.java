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

import nebsoa.common.Context;
import nebsoa.common.exception.SysException;
/*******************************************************************
 * <pre>
 * 1.설명 
 * 클래스명과 메소드명을 받아 호출을 대리해 주는 유틸리티 클래스
 * 단 해당 메소드는 Argument로 DataMap을 받아야 하며 리턴타입도 DataMap이어야 한다.
 * 
 * 2.사용법
 * 
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author 
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 
 * $Log: MethodInvokerUtil.java,v $
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
 * Revision 1.3  2008/04/08 04:46:07  김승희
 * 메소드 추가
 *
 * Revision 1.2  2008/02/15 02:22:58  김성균
 * 프로파일 관련 정리
 *
 * Revision 1.1  2008/02/14 08:58:52  김성균
 * 패키지 리팩토링
 *
 * Revision 1.1  2008/01/22 05:58:33  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:34  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2007/06/12 07:52:34  김승희
 * 리턴 타입 관련 변경
 *
 * Revision 1.1  2007/05/09 06:58:19  김승희
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public class MethodInvokerUtil {
	
	/**
	 * Singleton 패턴을 적용한 클래스의 경우 instance 얻는 메소드명
	 */
	public static final String GET_INSTANCE_METHOD = "getInstance";

	private MethodInvokerUtil(){}
	
	/**
	 * 객체의 메소드를 호출한다.
	 * @param clazz
	 * @param methodName
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public static Object invoke(Object clazz, String methodName, DataMap map) throws Exception {
		
		Context ctx = map.getContext();
		
		if (ctx != null) {
            ctx.startProfilerEvent("MethodInvokerUtil::"+ methodName+" of "+ clazz.getClass().getName());
        }
		
		try {
			Method method = clazz.getClass().getMethod(methodName, new Class[]{DataMap.class});
			return method.invoke(clazz, new Object[]{map});
		
		} catch (IllegalAccessException e) {
			throw new SysException(e);
		} catch (SecurityException e) {
			throw new SysException(e);
		} catch (NoSuchMethodException e) {
			throw new SysException(e);
		} catch (IllegalArgumentException e) {
			throw new SysException(e);
		} catch (InvocationTargetException e) {
			Throwable th = e.getTargetException();
			if(th instanceof Exception){
				throw (Exception)th ;
			}else{
				throw new SysException(th);
			}
		} catch (Exception e){
			throw e;
		} catch(Throwable e){
			throw new SysException(e);
		} finally{
			if (ctx != null) {
                ctx.stopProfilerEvent();
            } 
        }
	}
	
	/**
	 * 객체를 생성하고 메소드를 호출한다.
	 * @param className
	 * @param methodName
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public static Object invoke(String className, String methodName, DataMap map) throws Exception {
		
		try {
			Object obj = Class.forName(className).newInstance();
			return invoke(obj, methodName, map);
		} catch (InstantiationException e) {
			throw new SysException(e);
		} catch (ClassNotFoundException e) {
			throw new SysException(e);
        }
	}
	
	
	/**
	 * 싱글톤으로 구현한 클래스의 메소드를 호출한다.
	 * @param className
	 * @param methodName
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public static Object invokeSingleton(String className, String methodName, DataMap map) throws Exception {
        try{         
	        Method instanceMethod = Class.forName(className).getMethod(MethodInvokerUtil.GET_INSTANCE_METHOD, null);
			Object obj = instanceMethod.invoke(null, null);
			return invoke(obj, methodName, map);
			
		} catch (IllegalAccessException e) {
			throw new SysException(e);
		} catch (ClassNotFoundException e) {
			throw new SysException(e);
		} catch (IllegalArgumentException e) {
			throw new SysException(e);
		} catch (InvocationTargetException e) {
			Throwable th = e.getTargetException();
			if(th instanceof Exception){
				throw (Exception)th;
			}else{
				throw new SysException(th);
			}
        }
	}
	
	/**
	 * 해당 클래스의 메소드를 호출합니다.
	 * @param className 클래스명
	 * @param methodName 메소드명
	 * @param argumentClass 인자 클래스명 배열
	 * @param argumentValue 인자 값 배열
	 * @return 리턴값
	 * @throws Exception
	 */
	public static Object invoke(String className, String methodName, Class[] argumentClass, Object[] argumentValue) throws Exception {
		
		
		try {
			Class clazz = Class.forName(className);
			Method method = clazz.getMethod(methodName, argumentClass);
			
			if(Modifier.isStatic(method.getModifiers())){
				return method.invoke(clazz, argumentValue);
			}else{
				return method.invoke(Class.forName(className).newInstance(), argumentValue);
			}
					
		} catch (InstantiationException e) {
			throw new SysException(e);
		} catch (IllegalAccessException e) {
			throw new SysException(e);
		} catch (ClassNotFoundException e) {
			throw new SysException(e);
		} catch (SecurityException e) {
			throw new SysException(e);
		} catch (NoSuchMethodException e) {
			throw new SysException(e);
		} catch (IllegalArgumentException e) {
			throw new SysException(e);
		} catch (InvocationTargetException e) {
			Throwable th = e.getTargetException();
			if(th instanceof Exception){
				throw (Exception)th ;
			}else{
				throw new SysException(th);
			}
		} catch (Exception e){
			throw e;
		} catch(Throwable e){
			throw new SysException(e);
		} 
	}
	
}
