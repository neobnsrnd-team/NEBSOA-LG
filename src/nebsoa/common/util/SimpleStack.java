/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.util;

import java.io.Serializable;


/*******************************************************************
 * <pre>
 * 1.설명 
 * This is a simple stack. I would have liked to use java.util.Stack, but
 * I need a stack that returns "null" when the Stack is empty, rather than
 * throwing an exception.
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
 * $Log: SimpleStack.java,v $
 * Revision 1.1  2018/01/15 03:39:49  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:30  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:23  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:26  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:50  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:18  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:03  안경아
 * *** empty log message ***
 *
 * Revision 1.4  2006/07/10 06:05:25  김성균
 * *** empty log message ***
 *
 * Revision 1.3  2006/06/17 10:37:45  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
final public class SimpleStack implements Serializable
{

    /**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3810193907096222660L;
	private Object[] _stack;
    private int _count = 0;

    public void push (final Object o)
    {
        if (o == null)
        {
            return;
        }
        ensureCapacity(_count + 1);
        _stack[_count] = o;
        _count++;
    }

    public Object pop ()
    {
        if (_count == 0)
        {
            return null;
        }
        _count--;
        Object ret = _stack[_count];
        _stack[_count] = null;
        return ret;
    }

    public int size ()
    {
        return _count;
    }

    public boolean isEmpty ()
    {
        return (_count == 0);
    }

    public void clear ()
    {
        for (int i = 0; i < _stack.length; i++)
        {
            _stack[i] = null;
        }
        _count = 0;
    }

    public void ensureCapacity (int numElems)
    {
        if (_stack == null)
        {
            _stack = new Object[numElems];
        }
        else if (numElems > _stack.length)
        {
            Object[] newStack = new Object[numElems];
            System.arraycopy(_stack, 0, newStack, 0, _stack.length);
            _stack = newStack;
        }
    }

    static public void main (String arg[])
    {
        SimpleStack ss = new SimpleStack();
        for (int i = 0; i < arg.length; i++)
        {
            System.out.println("Pushing: " + arg[i]);
            ss.push(arg[i]);
        }
        System.out.println("Number of elements on stack: " + ss.size());
        Object o;
        while ((o = ss.pop()) != null)
        {
            System.out.println("pop: " + o);
        }
        System.out.println("Number of elements on stack: " + ss.size());
    }
}
