/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.monitor.profiler;

import nebsoa.common.monitor.profiler.Profile;
import nebsoa.common.monitor.profiler.ProfileEvent;
import nebsoa.common.monitor.profiler.Profiler;

/*******************************************************************
 * <pre>
 * 1.설명 
 * A Profile test case
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
 * $Log: ProfileTest.java,v $
 * Revision 1.1  2018/01/15 03:39:52  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:08  cvs
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
 * Revision 1.1  2008/01/22 05:58:31  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:53  안경아
 * *** empty log message ***
 *
 * Revision 1.3  2006/06/17 10:58:12  오재훈
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class ProfileTest
{

    public static void main (String arg[])
    {
        try
        {
            Profile prof = new Profile(1,10);
            Profiler test = prof.newProfiler();
            test.startEvent("1");
            Thread.sleep(50);
            test.startEvent("1.1");
            Thread.sleep(50);
            test.startEvent("1.1.1");
            Thread.sleep(50);
            test.stopEvent();
            Thread.sleep(50);
            test.stopEvent();
            test.startEvent("1.2");
            Thread.sleep(50);
            test.stopEvent();
            Thread.sleep(50);
            test.startEvent("1.3");
            Thread.sleep(50);
            test.stopEvent();
            Thread.sleep(50);
            test.stopEvent();
            Thread.sleep(50);

            test.startEvent("2");
            Thread.sleep(50);
            test.startEvent("2.1");
            Thread.sleep(50);
            test.stopEvent();
            Thread.sleep(50);
            test.startEvent("2.2");
            Thread.sleep(50);
            test.stopEvent();
            Thread.sleep(50);
            test.stopEvent();
            Thread.sleep(50);

            test.startEvent("3");
            Thread.sleep(50);
            test.startEvent("3.1");
            Thread.sleep(50);
            test.stopEvent();
            Thread.sleep(50);
            test.startEvent("3.2");
            Thread.sleep(50);
            test.startEvent("3.2.1");
            Thread.sleep(50);
            test.stopEvent();
            Thread.sleep(50);
            test.startEvent("3.2.2");
            Thread.sleep(50);
            test.startEvent("3.2.2.1");
            Thread.sleep(50);
            test.stopEvent();
            Thread.sleep(50);
            test.stopEvent();
            Thread.sleep(50);
            test.stopEvent();
            Thread.sleep(50);
            test.stopEvent();
            Thread.sleep(50);

            test.startEvent("4");
            Thread.sleep(50);
            test.stopEvent();
            Thread.sleep(50);

            System.out.println("-- META --");
            System.out.println(test.toString());

            System.out.println("-- EVENTS --");
            java.util.Iterator i = test.getEvents();
            int num = 0;
            while (i.hasNext())
            {
                ProfileEvent wme = (ProfileEvent) i.next();
                System.out.println(num++ + "\t" + wme.depth + "\t" + wme.name + "\t"
                                   + wme.start + "\t" + wme.duration);
            }

            System.out.println("-- CALLGRAPH --");
            test.destroy();
            // CallGraph class not available - commented out
            // CallGraph cg = CallGraph.getCallGraph(prof);
            // System.out.println(cg);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

