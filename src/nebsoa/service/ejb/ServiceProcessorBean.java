/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.service.ejb;

import nebsoa.common.Context;
import nebsoa.common.base.BaseSessionBean;
import nebsoa.common.monitor.ContextLogger;
import nebsoa.common.monitor.WorkingThreadMonitorManager;
import nebsoa.common.util.DataMap;
import nebsoa.service.engine.ServiceEngine;
import nebsoa.service.exception.ServiceException;

/*******************************************************************
 * <pre>
 * 1.설명 
 * Service의 수행을 대리하는 ServiceProcessor EJB의 Stateless-SessionBean 클래스 
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
 * $Log: ServiceProcessorBean.java,v $
 * Revision 1.1  2018/01/15 03:39:52  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:20  cvs
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
 * Revision 1.1  2008/08/04 08:54:53  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.5  2008/04/01 11:28:22  오재훈
 * WorkingThreadMonitorManager에 Context 모니터 데이타 삽입 로직 추가.
 *
 * Revision 1.4  2008/02/14 09:27:30  김성균
 * 예외처리 및 프로파일관련 수정
 *
 * Revision 1.2  2008/01/24 05:35:04  최수종
 * Exception 처리 수정
 *
 * Revision 1.1  2008/01/22 05:58:28  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/12/31 04:35:34  최수종
 * nebsoa.service 패키지 추가
 *
 * Revision 1.3  2007/12/31 02:52:37  김성균
 * 서비스ID 인자로 추가
 *
 * Revision 1.2  2007/12/21 05:06:03  김성균
 * serviceId : 메소드 인자에서 제외
 *
 * Revision 1.1  2007/12/18 03:16:00  김성균
 * 서비스관련 패키지 최초등록
 *
 * </pre>
 ******************************************************************/
public class ServiceProcessorBean extends BaseSessionBean implements ServiceProcessor {
	
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -8975709971942031468L;

    /* (non-Javadoc)
     * @see nebsoa.service.ejb.ServiceProcessor#doServiceProcess(java.lang.String, nebsoa.common.util.DataMap)
     */
    public DataMap doServiceProcess(String serviceId, DataMap map) throws ServiceException  {
    	Context ctx = map.getContext();

        if(ctx != null){
            // Tier 분리시에도 Context 객체를 유지하기 위해서 ThreadLocal에 넣는다.
            ContextLogger.putContext(map.getContext());
            
        	// Thread monitor Manager에 넣어 준다.
        	WorkingThreadMonitorManager.getInstance().addContext(ctx);
        }
        try{
	        map = ServiceEngine.doSyncProcess(serviceId, map);
	        if (ctx != null) {
	            // Tier 분리시에도 Context 객체를 유지하기 위해서 ThreadLocal에서 꺼내서 Map에 넣는다.
	            map.setContext(ContextLogger.getContext());
	        }
	        return map;
        }finally{
            if(ctx != null){
            	// Thread monitor Manager에 넣어 준다.
            	WorkingThreadMonitorManager.getInstance().removeContext(ctx);
            }
        }
    }
}
