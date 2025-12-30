/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.service.context;

import java.util.Map;

import nebsoa.service.exception.ServiceComponentNotFoundException;
import nebsoa.service.info.Component;
import nebsoa.service.info.Service;
import nebsoa.spiderlink.context.TrxMessage;



/*******************************************************************
 * <pre>
 * 1.설명 
 * 서비스 호출시 서비스정보를 담고 있는 context 클래스
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
 * $Log: ServiceContext.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:27  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.2  2009/09/14 09:01:43  isjoo
 * 컴포넌트 정보 리로드시 버그 수정
 *
 * Revision 1.1  2008/11/18 11:27:26  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:29  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.2  2008/02/14 09:26:48  김성균
 * 서비스 정보 로딩 관련 버그 수정
 *
 * Revision 1.1  2008/01/22 05:58:35  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.4  2008/01/07 01:40:59  김성균
 * 생성자 변경
 *
 * Revision 1.2  2008/01/02 09:46:01  김성균
 * 특정 서비스 리로딩 기능 추가
 *
 * Revision 1.7  2007/12/31 02:55:55  김성균
 * 주석 추가
 *
 * Revision 1.6  2007/12/28 08:44:29  최수종
 * 버그 수정
 *
 * Revision 1.4  2007/12/28 07:10:49  김성균
 * nebsoa.service.info 패키지 변경에 의한 수정
 *
 * Revision 1.3  2007/12/24 08:47:47  김성균
 * TrxMessage 추가
 *
 * Revision 1.2  2007/12/24 05:58:57  김성균
 * serviceId, service 인스턴스변수 추가
 *
 * Revision 1.1  2007/12/24 02:10:00  김성균
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class ServiceContext {
    
	/**
     * 서비스
     */
    private Service service;
    
    /**
     * 거래전문 
     */
    private TrxMessage trxMessage;
    
    /**
     * 현재 처리중인 서비스순서 
     */
    private int currentServiceIndex = -1;
    
    /**
     * 서비스 컴포넌트 갯수 
     */
    private int serviceComponentCount;
	
	/**
	 * @param service
	 */
	public ServiceContext(Service service){
	    this.service = service;
	    this.serviceComponentCount = service.getServiceComponentListSize();
	}

    /**
     * @return Returns the serviceId.
     */
    public String getServiceId() {
        return service.getServiceId();
    }

    /**
     * @return Returns the service.
     */
    public Service getService() {
        return service;
    }

    /**
     * @return Returns the trxMessage.
     */
    public TrxMessage getTrxMessage() {
        return trxMessage;
    }

    /**
     * @param trxMessage The trxMessage to set.
     */
    public void setTrxMessage(TrxMessage trxMessage) {
        this.trxMessage = trxMessage;
    }
    
    /**
     * 다음 컴포넌트정보를 리턴합니다.
     * 
     * @return 컴포넌트정보
     */
    public Component nextComponent() throws ServiceComponentNotFoundException {
        //synchronized(this){//synchronize 할 이유가 없어 보여 지운다. 이종원.

            if (currentServiceIndex >= serviceComponentCount - 1) {
                throw new ServiceComponentNotFoundException(currentServiceIndex);
            }
        //}

        return service.getComponent(++currentServiceIndex);
    }
    
    /**
     * 서비스순서에 해당하는 후행조건식정보를 리턴합니다.
     * 
     * @return 후행조건식정보
     */
    public String getCurrentPostCondition() {
        return service.getPostCondition(currentServiceIndex);
    }
    
    /**
     * 서비스순서에 해당하는 컴포넌트의 파라미터정보(Map)를 리턴합니다.
     * 
     * @return 파라미터정보(Map)
     */
    public Map getCurrentParameterMap() {
        return service.getParameterMap(currentServiceIndex);
    }
    
    /**
     * 다음 컴포넌트 존재 여부
     * @return
     */
    public boolean hasNextComponent() {
        return (currentServiceIndex < serviceComponentCount -1) ? true : false; 
    }
}
