/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.queue;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 채널 서버 연동 쓰레드 Queue
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
 * $Log: BlockingQueue.java,v $
 * Revision 1.1  2018/01/15 03:39:54  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:36  cvs
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
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:33  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:39:16  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2006/09/22 22:56:26  이종원
 * 최초작성
 *
 * Revision 1.1  2006/09/15 08:13:16  김승희
 * 패키지 변경
 *
 * Revision 1.1  2006/08/28 00:53:53  김승희
 * 패키지, 클래스명 변경
 *
 * Revision 1.1  2006/07/04 13:48:39  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2006/07/04 08:44:14  김승희
 * 패키지 변경
 *
 * Revision 1.2  2006/06/17 09:15:10  김승희
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public class BlockingQueue {
    Object[] elements;
    Object lock;
    int maxSize;
    int size = 0;
    int head = 0;
    int tail = 0;

    /** Constructor. 
     */
    public BlockingQueue(int maxSize) {
        this(null, maxSize);
    }

    /** Constructor. 
     */
    public BlockingQueue(Object lock, int maxSize) {
        this.maxSize = maxSize;
        if (maxSize == 0)
            this.maxSize = 255;
        elements = new Object[this.maxSize];
        this.lock = lock == null ? elements : lock;
    }

    public void clear() {
        synchronized (lock) {
            size = 0;
            head = 0;
            tail = 0;
        }
    }

    public int size() {
        return size;
    }

    public int maxSize() {
        return maxSize;
    }

    /** Put object in queue.
     * @param o Object
     */
    public void put(Object o) throws InterruptedException {
        synchronized (lock) {
            while (size == maxSize)
                lock.wait();

            elements[tail] = o;
            if (++tail == maxSize)
                tail = 0;
            size++;
            lock.notify();
        }
    }

    /** Put object in queue.
     * @param timeout If timeout expires, throw InterruptedException
     * @param o Object
     * @exception InterruptedException Timeout expired or otherwise interrupted
     */
    public void put(Object o, int timeout) throws InterruptedException {
        synchronized (lock) {
            if (size == maxSize) {
                lock.wait(timeout);
                if (size == maxSize)
                    throw new InterruptedException("Timed out");
            }

            elements[tail] = o;
            if (++tail == maxSize)
                tail = 0;
            size++;
            lock.notify();
        }
    }

    /** Get object from queue.
     * Block if there are no objects to get.
     * @return The next object in the queue.
     */
    public Object get() throws InterruptedException {
        synchronized (lock) {
            while (size == 0)
                lock.wait();

            Object o = elements[head];
            elements[head] = null;
            if (++head == maxSize)
                head = 0;
            if (size == maxSize)
                lock.notifyAll();
            size--;
            return o;
        }
    }

    /** Get from queue.
     * Block for timeout if there are no objects to get.
     * @param timeoutMs the time to wait for a job
     * @return The next object in the queue, or null if timedout.
     */
    public Object get(int timeoutMs) throws InterruptedException {
        synchronized (lock) {
            if (size == 0 && timeoutMs != 0)
                lock.wait((long) timeoutMs);

            if (size == 0)
                return null;

            Object o = elements[head];
            elements[head] = null;
            if (++head == maxSize)
                head = 0;

            if (size == maxSize)
                lock.notifyAll();
            size--;

            return o;
        }
    }

    /** Peek at the  queue.
     * Block  if there are no objects to peek.
     * @return The next object in the queue, or null if timedout.
     */
    public Object peek() throws InterruptedException {
        synchronized (lock) {
            if (size == 0)
                lock.wait();

            if (size == 0)
                return null;

            Object o = elements[head];
            return o;
        }
    }

    /** Peek at the  queue.
     * Block for timeout if there are no objects to peek.
     * @param timeoutMs the time to wait for a job
     * @return The next object in the queue, or null if timedout.
     */
    public Object peek(int timeoutMs) throws InterruptedException {
        synchronized (lock) {
            if (size == 0)
                lock.wait((long) timeoutMs);

            if (size == 0)
                return null;

            Object o = elements[head];
            return o;
        }
    }
}
