/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.monitor.profiler;

import java.io.Serializable;

import nebsoa.common.util.SimpleStack;

/*******************************************************************
 * <pre>
 * 1.설명 
 * A Profile manages profiler objects, and is the root object that you
 * create in order to profile a system. Use this object to obtain
 * Profile objects with which to trace; and also to obtain a CallGraph
 * showing the results of the profiling.
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
 * $Log: Profile.java,v $
 * Revision 1.1  2018/01/15 03:39:52  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:22  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:26  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:29  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:52  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:27  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:22  안경아
 * *** empty log message ***
 *
 * Revision 1.5  2006/07/10 06:05:58  김성균
 * serialVersionUID 추가
 *
 * Revision 1.4  2006/06/21 09:42:47  이종원
 * *** empty log message ***
 *
 * Revision 1.3  2006/06/17 10:37:45  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class Profile implements Serializable
{
   /**
	 * Comment for <code>serialVersionUID</code>
	 */
   private static final long serialVersionUID = 2559825515337789480L;

   private final SimpleStack _stack = new SimpleStack();

   private final ActiveProfiler[] _profilers;
   private int _profilersIdx = 0;

   private final int _samplingRate;
   private int _sampleCount = 0;

   /**
     * Record time is how long we are to record profiles. After the
     * specified amount of record time we will discard old profiles.
     * One in every samplingRate Profilers created by this category
     * will be an ActiveProfiler. If samplingRate is zero, all never be
     * active; if it is 1 it will always be active.
     * Size specifies how many profilers we will keep; after this many
     * profilers are discarded.
     */
   public Profile(int samplingRate, int size)
   {
      _samplingRate = samplingRate;
      _profilers = new ActiveProfiler[size];
   }

   public String toString() {
      return "Profile("
         +  _samplingRate  + ","  + _profilers.length  + ")";
   }


   /**
     * Instantiate a new profile. If a null object is returned then
     * no profiling is to be done. The returned object will be the 
     * root of a profile stack trace. Call its start() and stop()
     * methods to record timing data. 
     * <P>
     * Concurrency: this method is thread-safe. You may call it from
     * multiple threads. 
     */
   synchronized public Profiler newProfiler() {
      if ((_samplingRate == 0) || (++_sampleCount < _samplingRate)) {
         return NullProfiler.INSTANCE;
      }
      _sampleCount = 0;

      ActiveProfiler p = (ActiveProfiler) _stack.pop();
      if (p != null) {
          p.reset();
      } else {
         p = new ActiveProfiler(this);
      }
      return p;
   }

   /**
     * Add the profiler to the record queue, and clean out any 
     * profilers that have been hanging around for too long.
     */
   final synchronized protected void record(ActiveProfiler p) {
       _stack.push(_profilers[_profilersIdx]);
       _profilers[_profilersIdx++] = p;
       if (_profilersIdx >= _profilers.length) {
           _profilersIdx = 0;
       }
   }



    public ActiveProfiler[] getProfilers() {
        return _profilers;
    }
}

