/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.monitor;

import java.util.HashMap;
import java.util.Iterator;

import nebsoa.common.Context;
import nebsoa.common.log.LogManager;

/******************************************************************
 * <pre>
 *  1.설명 
 *  request가 처리 되는 동안 Thread 내에서 어떤 루틴을 수행하는지 모니터링 
 *  하는 클래스 
 *  
 *  2.사용법
 *  
 *  <font color='red'>
 *  3.주의사항
 *  메모리 leak이 나지 않도록 로직 종료 시점에 clear를 잘 해주어야 한다.
 *  </font>
 * </pre>
 * 
 * @author 이종원
 * @version 1.0
 ******************************************************************
 * $Log: ContextLogger.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:29  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:27  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:29  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:55  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:33  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:44  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2007/10/01 09:04:07  김성균
 * ThreadLocal에 Context 적용
 *
 * Revision 1.1  2007/09/27 08:09:21  이종원
 * ThreadLocal관련 기능 추가
 *
 ******************************************************************/
public class ContextLogger  {
    
    /**
     * Comment for <code>CONTEXT</code>
     */
    private static final String CONTEXT = "Context";
    
    private static SpiderThreadLocal monitor = new SpiderThreadLocal();

    public static HashMap getMap() {
        return (HashMap) monitor.get();
    }
    
    public static void clear() {
        monitor.clear();
    }

    public static void putContext(Context context) {
        getMap().put(CONTEXT, context);
    }
    
    public static Context getContext() {
        return (Context) getMap().get(CONTEXT);
    }
    
    public static HashMap dumpData(){
        HashMap data = (HashMap) getMap().clone();
        return data;
    }
    
    public static void traceCurrentRequest(){
        HashMap data = dumpData();
        Iterator i = data.values().iterator();
        while (i.hasNext()) {
            Object obj = i.next();
            LogManager.debug("[" + Thread.currentThread().getName() + "]" + obj);
        }
    }
}
 
