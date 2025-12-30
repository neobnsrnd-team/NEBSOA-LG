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
 * $Log: ProfileTest2.java,v $
 * Revision 1.1  2018/01/15 03:39:51  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:07  cvs
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
public class ProfileTest2
{

    public static void main (String arg[])
    {
        try
        {
            Profile prof = new Profile(1,100);
            Profiler test = prof.newProfiler();
            test.startEvent("Main---");
            new ProfileTest2().new First(test).execute();
            test.stopEvent();

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


	class First{
		Profiler prof;
		First(Profiler p){
			prof=p;			
		}
		void execute() throws InterruptedException{
			prof.startEvent(this.getClass().getName());			
			System.out.println(this.getClass().getName()+"execute...");
			//Thread.sleep(100);
			new Second(prof).execute();
			Thread.sleep(1000);
			new Second2(prof).execute();
			prof.stopEvent();
		}
	}
	
	class Second{
		Profiler prof;
		Second(Profiler p){
			prof=p;			
		}
		void execute() throws InterruptedException{
			prof.startEvent(this.getClass().getName());
			System.out.println(this.getClass().getName()+"execute...");
//			Thread.sleep(50);
			new Third(prof).execute();
			prof.stopEvent();
		}
	}
	
	class Third{
		Profiler prof;
		Third(Profiler p){
			prof=p;			
		}
		void execute() throws InterruptedException{
			prof.startEvent(this.getClass().getName());
			System.out.println(this.getClass().getName()+"execute...");
			//Thread.sleep(200);
			prof.stopEvent();
			Thread.sleep(200);
		}
	}
	
	
	class Second2{
		Profiler prof;
		Second2(Profiler p){
			prof=p;			
		}
		void execute() throws InterruptedException{
			prof.startEvent(this.getClass().getName());
			System.out.println(this.getClass().getName()+"execute...");
			Thread.sleep(1000);
			new Third(prof).execute();
			new Third2(prof).execute();
			prof.stopEvent();
		}
	}
	
	class Third2{
		Profiler prof;
		Third2(Profiler p){
			prof=p;			
		}
		void execute() throws InterruptedException{
			prof.startEvent(this.getClass().getName());
			System.out.println(this.getClass().getName()+"execute...");
			Thread.sleep(2000);
			prof.stopEvent();
		}
	}
}
		

