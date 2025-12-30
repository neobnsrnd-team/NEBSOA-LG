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
import nebsoa.common.Constants;
import nebsoa.common.Context;
import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.DataMap;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 일반적인 비즈니스 로직을 수행하기 위한 클래스가 상속받아야 되는 최상위 클래스이며,
 * 프레임웍 내부적으로 생성되고 호출는 클래스 입니다.
 * 
 * 2.사용법
 * 개발자는 아래의 메소드에 비즈니스 로직을 구현합니다.
 * abstract public DataMap execute(DataMap map) throws BizException;
 * 수행할 로직을 처리하기 위한 데이타는 DataMap 객체로 부터 얻어오며,
 * 예외사항이 발생하면 BizException을  던지도록 구현합니다.
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
 * $Log: BaseBiz.java,v $
 * Revision 1.1  2018/01/15 03:39:51  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:03  cvs
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
 * Revision 1.2  2008/10/16 04:44:32  김성균
 * ctx == null일 경우 처리
 *
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.2  2008/02/14 09:02:55  김성균
 * 프로파일 관련 정리
 *
 * Revision 1.1  2008/01/22 05:58:34  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.2  2007/12/31 04:34:48  최수종
 * bizDefaultMethodName.equals(targetMethod) 체크 로직 추가
 *
 * Revision 1.6  2007/12/27 08:36:29  최수종
 * *** empty log message ***
 *
 * Revision 1.5  2007/10/31 07:07:56  jwlee
 * 로그 수정
 *
 * Revision 1.4  2007/07/08 09:51:58  김성균
 * 로그여부 설정기능 추가
 *
 * Revision 1.3  2007/07/08 05:06:43  최수종
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public abstract class BaseBiz extends Biz {
    
    /**
     * <pre>
     * framework 내부에서 호출됩니다.
     * 하위 클래스에서 비즈니스 로직을 구현한 오버라이딩 된 메소드를 호출하도록
     * Template-Method 패턴으로 구현되어 있습니다.
     * 
     * 또한, 본 메소드를 하위 클래스에서 오버라이딩 하지 못하도록 final 로 선언되어 있습니다.
     * </pre>
     * @param map
     * @return
     * @throws BizException
     */
    public final DataMap doProcess(DataMap map) throws BizException{
    	LogManager.debug("process()...");
        
        boolean isLogModeChange = false;
        
        Context ctx = map.getContext();
        if (ctx != null && ctx.isLogDisabled() && bizInfo.isLogEnabled()) {
            ctx.setLogEnabled(true);
            isLogModeChange = true;
        }
        
        try {
        	startProfilerEvent(ctx, this.getClass().getName() + "::doProcess");
            if (isDuplicateTrxCheck()) {
                LogManager.debug( "이중거래체크...");
                try {
                	startProfilerEvent(ctx, this.getClass().getName() + "::duplicateTrxCheck");
                	duplicateTrxCheck(map);
                } finally {
                	stopProfilerEvent(ctx);
                }
            }

            try {
            	String targetMethod = map.getParameter(Constants.TARGET_METHOD, null);
            	startProfilerEvent(ctx, this.getClass().getName() + "::" + (targetMethod == null ? "execute" : targetMethod));
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
            if (isLogModeChange) {
            	map.getContext().setLogEnabled(false);
            }
        } 
        return map;
    }
    
    /**
     * 이중거래검사 여부
     * @return
     */
    private boolean isDuplicateTrxCheck() {
        if (bizInfo != null) {
            return bizInfo.isDupCheck();
        }
        return false;
    }
    
    /**
     * 이중거래검사를 한다면 상속받는 클래스에서 재정의 되어야 한다.
     * @throws Exception
     */
    protected void duplicateTrxCheck(DataMap map) throws BizException {
        throw new SysException("이중거래검사 로직이 정의되지 않았습니다.");
    }
}
