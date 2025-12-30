/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.biz.base;

import nebsoa.biz.exception.BizException;
import nebsoa.biz.util.BizInfo;
import nebsoa.common.Constants;
import nebsoa.common.Context;
import nebsoa.common.base.Processor;
import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.DataMap;
import nebsoa.common.util.MethodInvoker;

/*******************************************************************************
 * <pre>
 *  1.설명 
 *  비즈니스 로직을 수행하기 위한 클래스가 상속받아야 되는 최상위 클래스이며,
 *  프레임웍 내부적으로 생성되고 호출는 클래스 입니다.
 *  
 *  2.사용법
 *  개발자는 아래의 메소드에 비즈니스 로직을 구현합니다.
 *  abstract public DataMap execute(DataMap map) throws BizException;
 *  수행할 로직을 처리하기 위한 데이타는 DataMap 객체로 부터 얻어오며,
 *  예외사항이 발생하면 BizException을  던지도록 구현합니다.
 *  
 *  &lt;font color=&quot;red&quot;&gt;
 *  3.주의사항
 *  &lt;/font&gt;
 * 
 *  @author $Author: cvs $
 *  @version
 * ******************************************************************
 *  - 변경이력 (버전/변경일시/작성자)
 *  
 *  $Log: Biz.java,v $
 *  Revision 1.1  2018/01/15 03:39:51  cvs
 *  *** empty log message ***
 *
 *  Revision 1.1  2016/04/15 02:23:03  cvs
 *  neo cvs init
 *
 *  Revision 1.1  2011/07/01 02:13:51  yshong
 *  *** empty log message ***
 *
 *  Revision 1.1  2008/11/18 11:27:25  김성균
 *  *** empty log message ***
 *
 *  Revision 1.1  2008/11/18 11:01:28  김성균
 *  LGT Gateway를 위한 프로젝트로 분리
 *
 *  Revision 1.1  2008/08/04 08:54:56  youngseokkim
 *  *** empty log message ***
 *
 *  Revision 1.2  2008/02/14 09:02:55  김성균
 *  프로파일 관련 정리
 *
 *  Revision 1.1  2008/01/22 05:58:34  오재훈
 *  패키지 리펙토링
 *
 *  Revision 1.2  2007/12/31 04:34:22  최수종
 *  bizDefaultMethodName 상수, clone(), executeService() 메소드 추가
 *
 *  Revision 1.24  2007/12/28 05:45:45  최수종
 *  executeService() 추가. 서비스엔진에서 execute 메소드 invoke시 사용
 *
 *  Revision 1.23  2007/12/27 08:36:29  최수종
 *  *** empty log message ***
 *
 *  Revision 1.22  2007/12/26 06:57:57  최수종
 *  *** empty log message ***
 *
 *  Revision 1.21  2007/02/01 12:22:27  이종원
 *  SpiderComponent를 Processor로 리팩토링
 *
 *  Revision 1.20  2006/10/30 12:42:28  김성균
 *  *** empty log message ***
 *
 *  Revision 1.19  2006/10/24 02:47:12  김성균
 *  *** empty log message ***
 *
 *  Revision 1.18  2006/08/23 11:41:25  김성균
 *  Profiler 수정
 * 
 *  Revision 1.17  2006/08/22 01:48:48  김성균
 *  *** empty log message ***
 * 
 *  Revision 1.16  2006/08/03 05:19:39  김성균
 *  접근CLIENT체크여부 주석처리
 * 
 *  Revision 1.15  2006/08/01 01:40:52  김성균
 *  Serializable 삭제
 * 
 *  Revision 1.14  2006/07/31 00:39:51  김성균
 *  *** empty log message ***
 * 
 *  Revision 1.13  2006/07/27 09:31:18  김성균
 *  TARGET_METHOD 추가
 * 
 *  Revision 1.12  2006/07/26 07:14:59  김성균
 *  doInvoke() 수정
 * 
 *  Revision 1.11  2006/07/26 06:20:02  김성균
 *  TARGET_METHOD 추가
 * 
 *  Revision 1.10  2006/06/29 13:33:13  김성균
 *  *** empty log message ***
 * 
 *  Revision 1.9  2006/06/26 02:53:49  김성균
 *  메소드 호출 처리 부분 추가
 * 
 *  Revision 1.8  2006/06/20 10:52:36  이종원
 *  *** empty log message ***
 * 
 *  Revision 1.7  2006/06/17 10:37:45  김성균
 *  *** empty log message ***
 * 
 * </pre>
 ******************************************************************************/
public abstract class Biz implements Processor, Cloneable {
	
	// Biz 유형 클래스의 Bussiness 로직을 수행하는 기본 메소드명
	public static final String BIZ_DEFAULT_METHOD_NAME = "execute";
	
	/**
	 * Biz클래스 복사 수행
	 */
	public Object clone() throws CloneNotSupportedException {
		Object obj = super.clone();

		return obj;
	}
	
    /**
     * 수행할 메소드명
     */
    protected static String TARGET_METHOD = Constants.TARGET_METHOD;

    protected BizInfo bizInfo;

    /**
     * <pre>
     *  <font color=red> 비즈니스 로직을 처리하는 메소드이며,
     *  개발자가 구현해야 할 메소드이다.
     *  </font>
     * </pre>
     * 
     * @param map
     * @throws BizException
     */
    abstract protected DataMap execute(DataMap map) throws BizException;
    

    /**
     * @param map
     * @return
     */
    protected final DataMap doInvoke(DataMap map) throws Throwable {
        String targetMethod = map.getParameter(Constants.TARGET_METHOD);
        MethodInvoker mi = new MethodInvoker();
        mi.setTargetObject(this);
        mi.setTargetMethod(targetMethod);
        mi.setArguments(new Object[] { map });
        mi.prepare();
        return (DataMap) mi.invoke();
    }

    /**
     * <pre>
     *  framework 내부에서 호출됩니다.
     *  하위 클래스에서 비즈니스 로직을 구현한 오버라이딩 된 메소드를 호출하도록
     *  Template-Method 패턴으로 구현되어 있습니다.
     *  
     *  또한, 본 메소드를 하위 클래스에서 오버라이딩 하지 못하도록 final 로 선언되어 있습니다.
     * </pre>
     * 
     * @param map
     * @return
     * @throws BizException
     */
    public DataMap doProcess(DataMap map) throws BizException {
    	LogManager.debug("process()...");

        try {
        	startProfilerEvent(map.getContext(), this.getClass().getName() + "::doProcess");
            try {
            	String targetMethod = map.getParameter(Constants.TARGET_METHOD, null);
            	startProfilerEvent(map.getContext(), this.getClass().getName() + "::" + (targetMethod == null ? "execute" : targetMethod));
                if (targetMethod == null || BIZ_DEFAULT_METHOD_NAME.equals(targetMethod)) {
                    map = execute(map);
                } else {
                    map = doInvoke(map);
                }
            } catch (BizException e) {
                throw e;
            } catch (RuntimeException e) {
                throw e;
            } catch (Throwable e) {
                throw new SysException(e);
            } finally {
            	stopProfilerEvent(map.getContext());
            }
        } finally {
        	stopProfilerEvent(map.getContext());
        }
        return map;
    }
    
    protected void startProfilerEvent(Context ctx, String eventMessage) {
    	if (ctx != null) {
    		ctx.startProfilerEvent(eventMessage);
    	}
    }
    
    protected void stopProfilerEvent(Context ctx) {
    	if (ctx != null) {
    		ctx.stopProfilerEvent();
    	}
    }
    
    public BizInfo getBizInfo() {
        return bizInfo;
    }

    public void setBizInfo(BizInfo bizInfo) {
        this.bizInfo = bizInfo;
    }

}
