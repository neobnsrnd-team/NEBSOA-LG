/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.monitor.profiler;

import java.util.Iterator;
import java.util.NoSuchElementException;


/*******************************************************************
 * <pre>
 * 1.설명 
 * 
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
 * $Log: NullProfiler.java,v $
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
 * Revision 1.1  2006/06/21 09:42:47  이종원
 * *** empty log message ***
 *
 * Revision 1.3  2006/06/17 10:58:12  오재훈
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class NullProfiler implements Profiler {

    public static final NullProfiler INSTANCE = new NullProfiler();

    private static final Iterator NULL_ITERATOR =
    new Iterator() {
        public boolean hasNext() {
            return false;
        }

        public Object next() throws NoSuchElementException
        {
            throw new NoSuchElementException("No elements");
        }

        public void remove() throws UnsupportedOperationException
        {
            throw new UnsupportedOperationException("No remove");
        }
    };

    public Iterator getEvents() throws IllegalStateException {
        return NULL_ITERATOR;
    }

    public void startEvent(String name) {
        // do nothing
    }

    public void stopEvent() {
        // do nothing
    }
    
	public Profile getProfile() {
		return null;
	}

    public void destroy() {
        // do nothing
    }
    
    public String toString() {
        return "NullProfiler()";
    }

}