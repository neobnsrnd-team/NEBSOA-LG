/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
 package nebsoa.service.info;

import java.util.HashMap;
import java.util.Map;



/*******************************************************************
 * <pre>
 * 1.설명 
 * 서비스 프로세스 정보를 로딩하여 담고 있는 클래스
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
 * $Log: ServiceComponent.java,v $
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
 * Revision 1.1  2008/02/14 09:26:48  김성균
 * 서비스 정보 로딩 관련 버그 수정
 *
 * Revision 1.1  2008/01/22 05:58:32  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/12/31 04:35:34  최수종
 * nebsoa.service 패키지 추가
 *
 * Revision 1.1  2007/12/28 07:10:49  김성균
 * nebsoa.service.info 패키지 변경에 의한 수정
 *
 * </pre>
 ******************************************************************/
public class ServiceComponent {
    
    /**
     * 후행조건식
     */
	// Exception 발생시 현재 컴포넌트 skip하고 다음 컴포넌트 진행 
    public static final String EXCEPTION_SKIP = "EXCEPTION_SKIP";
    
    // Exception 발생시 다음 컴포넌트를 수행하지 않고 즉시 중단(default값)
    public static final String EXCEPTION_NOT_SKIP = "EXCEPTION_NOT_SKIP";
    
    private int serviceSeqNo;
	
    private Component component; 
    
    private Map parameterMap;
    
    private String postCondition; 
    
    public ServiceComponent() {
        parameterMap = new HashMap();
    }
    
    /**
	 * @return Returns the serviceSeqNo.
	 */
	public int getServiceSeqNo() {
		return serviceSeqNo;
	}

	/**
	 * @param serviceSeqNo The serviceSeqNo to set.
	 */
	public void setServiceSeqNo(int serviceSeqNo) {
		this.serviceSeqNo = serviceSeqNo;
	}

	/**
     * @return Returns the component.
     */
    public Component getComponent() {
        return component;
    }

    /**
     * @param component The component to set.
     */
    public void setComponent(Component component) {
        this.component = component;
    }
    
    public void putParameter(String paramKey, String paramValue) {
        parameterMap.put(paramKey, paramValue);
    }
    
    /**
     * @return Returns the parameterMap.
     */
    public Map getParameterMap() {
        return parameterMap;
    }

    /**
     * @return Returns the postCondition.
     */
    public String getPostCondition() {
        return postCondition;
    }

    /**
     * @param postCondition The postCondition to set.
     */
    public void setPostCondition(String postCondition) {
        this.postCondition = postCondition;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString(){
        return ">>> componentId:"+component.getComponentId()
                +"\n postCondition:"+ postCondition
                ;
    }


}
