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

/*******************************************************************
 * <pre>
 * 1.설명 
 * The ProfileEvent interface describes an event that took place
 * in the system. All that is recorded about the event is its name,
 * when it started, and when it stopped.
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
 * $Log: ProfileEvent.java,v $
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
 * Revision 1.4  2006/07/10 06:05:58  김성균
 * serialVersionUID 추가
 *
 * Revision 1.3  2006/06/17 10:37:45  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
final public class ProfileEvent implements Serializable
{

    /**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3579239455471757253L;

	/**
     * The name of this event
     */
    public String name;

    /**
     * Milliseconds since Jan 1, 1970 that this event started
     */
    public long start;

    /**
     * Milliseconds that this event lasted for
     */
    public int duration;

    /**
     * How many levels down the "call stack" is this event? In other
     * words, how many events enclose it.
     */
    public int depth;

    /**
     *  Create a new profile event with null and 0 values
     */
    public ProfileEvent ()
    {
        this(null, 0, 0, 0);
    }

    /**
     * Create a new profile event with the supplied values
     */
    public ProfileEvent (String name, int start, int duration, int depth)
    {
        this.name = name;
        this.start = start;
        this.duration = duration;
        this.depth = depth;
    }

}

