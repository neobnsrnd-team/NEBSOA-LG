/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
 package nebsoa.service.info;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import nebsoa.biz.base.Biz;
import nebsoa.biz.exception.BizException;
import nebsoa.biz.util.BizInfo;
import nebsoa.biz.util.BizManager;
import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;
import nebsoa.service.exception.ServiceSysException;


/*******************************************************************
 * <pre>
 * 1.설명 
 * 서비스 컴포넌트 파라미터 테이블의 정보를 로딩하여 담고 있는 클래스
 * 
 * 2.사용법
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author $Author: cvs $
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 
 * $Log: Component.java,v $
 * Revision 1.1  2018/01/15 03:39:52  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:14  cvs
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
 * Revision 1.1  2008/08/04 08:54:55  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.3  2008/02/15 04:45:16  김성균
 * 서비스 수행 로그 메시지 정리
 *
 * Revision 1.2  2008/02/14 09:26:48  김성균
 * 서비스 정보 로딩 관련 버그 수정
 *
 * Revision 1.1  2008/01/22 05:58:32  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/12/31 04:35:34  최수종
 * nebsoa.service 패키지 추가
 *
 * Revision 1.8  2007/12/28 08:21:40  최수종
 * getComponentClass(Object obj)메소드에서
 * 컴포넌트 유형이 Biz클래스 유형인 경우에 대한 로직 수정
 *
 * Revision 1.7  2007/12/28 05:45:09  최수종
 * *** empty log message ***
 *
 * Revision 1.6  2007/12/27 07:20:21  최수종
 * *** empty log message ***
 *
 * Revision 1.5  2007/12/27 05:50:25  최수종
 * *** empty log message ***
 *
 * Revision 1.4  2007/12/26 07:31:55  최수종
 * *** empty log message ***
 *
 * Revision 1.3  2007/12/26 06:27:02  김성균
 * 컴포넌트 클래스 얻어오는 메소드 추가
 *
 * Revision 1.2  2007/12/24 04:14:30  김성균
 * 서비스관련 테이블 컬럼명 변경
 *
 * Revision 1.1  2007/12/24 02:09:28  김성균
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class Component {
    
	private static Object dummy = new Object();
    
    /**
     * 컴포넌트 생성 유형 : Clone 생성 유형 
     */
    private static final String COMPONENT_CREATE_TYPE_CLONE = "C";

    /**
     * 컴포넌트 유형
     */
    public static final String COMPONENT_TYPE_BIZ = "B";  // Biz 유형 클래스
    public static final String COMPONENT_TYPE_COMPONENT = "C";  // 사용자 정의 클래스
    public static final String COMPONENT_TYPE_DB = "D";  // Framework에서 이미 정의된 DB연동 Biz클래스
    public static final String COMPONENT_TYPE_MSG = "M";  // Framework에서 이미 정의된 Msg연동 Biz클래스
    
    
    private String componentId;
    private String componentName;
    private String componentDesc;
    private String componentType;
    private String componentClassName;
    private String componentMethodName;
    private String componentCreateType;
    
    private Object clazz;
    
    /**
     * @return Returns the componentId.
     */
    public String getComponentId() {
        return componentId;
    }

    /**
     * @param componentId The componentId to set.
     */
    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    /**
     * @return Returns the componentName.
     */
    public String getComponentName() {
        return componentName;
    }

    /**
     * @param componentName The componentName to set.
     */
    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    /**
     * @return Returns the componentDesc.
     */
    public String getComponentDesc() {
        return componentDesc;
    }

    /**
     * @param componentDesc The componentDesc to set.
     */
    public void setComponentDesc(String componentDesc) {
        this.componentDesc = componentDesc;
    }

    /**
     * @return Returns the componentType.
     */
    public String getComponentType() {
        return componentType;
    }

    /**
     * @param componentType The componentType to set.
     */
    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    /**
     * @return Returns the componentClassName.
     */
    public String getComponentClassName() {
        return componentClassName;
    }

    /**
     * @param componentClassName The componentClassName to set.
     */
    public void setComponentClassName(String componentClassName) {
        this.componentClassName = componentClassName;
    }

    /**
     * @return Returns the componentMethodName.
     */
    public String getComponentMethodName() {
        return componentMethodName;
    }

    /**
     * @param componentMethodName The componentMethodName to set.
     */
    public void setComponentMethodName(String componentMethodName) {
        this.componentMethodName = componentMethodName;
    }
    
    /**
     * @return Returns the componentCreateType.
     */
    public String getComponentCreateType() {
        return componentCreateType;
    }

    /**
     * @param componentCreateType The componentCreateType to set.
     */
    public void setComponentCreateType(String componentCreateType) {
        this.componentCreateType = componentCreateType;
    }
    
    /**
     * 컴포넌트 클래스를 얻어온다. 
     * @return
     */
    public Object getComponentClass() {
    	
        synchronized (dummy) {
			if (clazz == null) {
				clazz = getComponentClass(componentClassName);
			}
		}
        
        // 컴포넌트 생성 유형에 따른 처리
        if (COMPONENT_CREATE_TYPE_CLONE.equalsIgnoreCase(componentCreateType)) {  // Clone유형인 경우
            return getCloneObject(clazz);
        } else {  // Singleton유형인 경우
            return clazz;
        }
    }
    
    /**
     * 컴포넌트 유형별로 설정값 추가
     * (Biz유형의 클래스인 경우에는 bizInfo객체를 생성하여 Biz클래스에 설정함)
     * 
     * @param componentClassName
     * @return
     */
    private Object getComponentClass(String componentClassName) {
    	
    	Object obj = forName(componentClassName);

    	// 컴포넌트 유형에 따른 처리(컴포넌트 타입이 Biz 클래스유형인 경우)
		if (obj instanceof Biz) {
			try {
				Biz biz = (Biz) obj;
				BizInfo bizInfo = BizManager.getInstance().getBizInfo(componentClassName);
				biz.setBizInfo(bizInfo);
			} catch (BizException e) {
				throw new ServiceSysException(e);
			}
		}

		return obj;
    }
    
    /**
	 * 컴포넌트를 복제한다.
	 * 
	 * @param o
	 * @return
	 */
    public Object getCloneObject(Object o) {
        String className = o.getClass().getName();
    	LogManager.debug(className + ".clone()");
        Object cloneObject = null;
        try {
            Class clazz = Class.forName(className);
            Method m = clazz.getMethod("clone", null);
            cloneObject = m.invoke(o, null);
        } catch (ClassNotFoundException e) {
        	throw new SysException("[CLASS_NOT_FOUND:" + e.getMessage() + "]");
        } catch (SecurityException e) {
        	throw new SysException("[SECURITY_VIOLATION:" + className + "]");
        } catch (NoSuchMethodException e) {
        	throw new SysException("[METHOD_NOT_FOUND:clone]");
        } catch (IllegalArgumentException e) {
        	throw new SysException("[ILLEGAL_ARGUMENT_EXCEPTION]");
        } catch (IllegalAccessException e) {
        	throw new SysException("[CANN'T MAKE INSTANCE 생성자 PUBLIC 인지 체크 :" + className + "]");
        } catch (InvocationTargetException e) {
        	throw new SysException("[CLONE_INVOKE_ERROR]");
        }
        return cloneObject;
    }
    
    /**
     * 컴포넌트 인스턴스를 생성한다.
     * @param className
     * @return
     */
    public Object forName(String className) {
        try {
        	LogManager.debug(className + ".newInstance()");
			return Class.forName(className).newInstance();
		} catch (ClassNotFoundException e) {
			throw new SysException("[CLASS_NOT_FOUND:" + e.getMessage() + "]");
		} catch (InstantiationException e1) {
			throw new SysException("[CANN'T MAKE INSTANCE 생성자 체크 :" + className + "]");
		} catch (IllegalAccessException e1) {
			throw new SysException("[CANN'T MAKE INSTANCE 생성자 PUBLIC 인지 체크 :" + className + "]");
		}
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
    public String toString(){
        return ">>> componentId:"+componentId
                +"\n componentName:"+ componentName
                +"\n componentDesc:"+ componentDesc
                +"\n componentType:"+ componentType
                +"\n componentClassName:"+ componentClassName
                +"\n componentMethodName:"+ componentMethodName
                +"\n componentCreateType:"+ componentCreateType
                ;
    }
}
