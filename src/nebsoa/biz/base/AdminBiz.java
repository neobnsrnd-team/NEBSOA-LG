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
import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.DataMap;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 관리자 페이지의  비즈니스 로직을 수행하기 위한 클래스가 상속받아야 되는 최상위 클래스이며,
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
 * $Log: AdminBiz.java,v $
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
 * Revision 1.2  2007/12/27 08:36:29  최수종
 * *** empty log message ***
 *
 * Revision 1.1  2007/01/22 11:51:21  이종원
 * 최초작성
 *
 * Revision 1.5  2006/10/30 12:41:08  김성균
 * *** empty log message ***
 *
 * Revision 1.4  2006/10/24 02:48:23  김성균
 * *** empty log message ***
 *
 * Revision 1.3  2006/08/23 11:40:53  김성균
 * Profiler 수정
 *
 * Revision 1.2  2006/07/31 00:30:54  김성균
 * doProcess() 추가
 *
 * Revision 1.1  2006/07/17 11:42:08  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2006/07/05 02:07:01  이종원
 * 최초작성
 *
 *
 * </pre>
 ******************************************************************/
public abstract class AdminBiz extends Biz {
    
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
            } finally {
            	stopProfilerEvent(map.getContext());
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
        return map;
    }

}
