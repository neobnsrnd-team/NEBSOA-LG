/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.biz.ejb;

import nebsoa.biz.base.Biz;
import nebsoa.biz.exception.BizException;
import nebsoa.biz.factory.BizFactory;
import nebsoa.biz.util.BizInfo;
import nebsoa.biz.util.BizManager;
import nebsoa.common.Context;
import nebsoa.common.base.BaseSessionBean;
import nebsoa.common.log.LogManager;
import nebsoa.common.monitor.ContextLogger;
import nebsoa.common.util.DataMap;
import nebsoa.common.util.PropertyManager;
import nebsoa.common.util.StringUtil;

/*******************************************************************
 * <pre>
 * 1.설명 
 * Business logic의 non-Transactional한  수행을 대리하는
 * BizProcessor EJB의 Stateless-SessionBean 클래스 
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
 * $Log: BizProcessorBean.java,v $
 * Revision 1.1  2018/01/15 03:39:51  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:05  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:25  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:28  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:54  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:29  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:24  안경아
 * *** empty log message ***
 *
 * Revision 1.11  2007/11/02 07:28:05  안경아
 * *** empty log message ***
 *
 * Revision 1.10  2007/10/31 06:45:24  안경아
 * *** empty log message ***
 *
 * Revision 1.9  2007/10/04 05:22:21  김성균
 * DataMap에 ThreadLocal의 Context 객체 넣어서 리턴하도록 수정
 *
 * Revision 1.8  2007/10/02 09:13:50  김성균
 * ContextLogger 적용
 *
 * Revision 1.7  2007/03/07 02:40:17  김성균
 * BizInfo 못 찾았을 경우 예외처리 통일
 *
 * Revision 1.6  2006/11/10 02:40:15  김성균
 * *** empty log message ***
 *
 * Revision 1.5  2006/08/22 01:48:19  김성균
 * *** empty log message ***
 *
 * Revision 1.4  2006/08/02 05:35:51  김성균
 * BizInfo 사용하도록...
 *
 * Revision 1.3  2006/06/20 10:43:20  이종원
 * *** empty log message ***
 *
 * Revision 1.2  2006/06/17 10:37:45  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class BizProcessorBean extends  BaseSessionBean implements BizProcessor {
	
	/**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 5182740270870239068L;

    /**
	 * Business logic 을 non-Transactional으로 호출합니다.
	 */
    public DataMap doBizProcess(String bizId,DataMap map)throws BizException  {
        BizInfo bizInfo = BizManager.getInstance().getBizInfo(bizId);
        Biz biz = BizFactory.getInstance().getBiz(bizInfo);
        
        // Tier 분리시에도 Context 객체를 유지하기 위해서 ThreadLocal에 넣는다.
        ContextLogger.putContext(map.getContext());
        
        map = biz.doProcess(map);
        
        Context ctx = ContextLogger.getContext();
        if(ctx != null){        	
	        // Tier 분리시에도 Context 객체를 유지하기 위해서 ThreadLocal에서 꺼내서 Map에 넣는다.
	        map.setContext(ContextLogger.getContext());
	        int threshold = PropertyManager.getIntProperty("monitor", "PROFILE_THRESHOLD", 10);
	        long now = System.currentTimeMillis();
	        if(ctx.getMonitor() != null && (now-ctx.getMonitor().getStartTime())>threshold*1000){
	        	if(!StringUtil.contains(ctx.getUri(),"admin"))
	        		LogManager.info("PROFILE", ContextLogger.getContext().getMonitor().toStringBiz());
	        }
        }
        return map;
    }
}
