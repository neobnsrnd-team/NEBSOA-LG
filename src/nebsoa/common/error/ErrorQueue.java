/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.error;

import java.util.List;
import java.util.Vector;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 오류코드 정보를 얻어오기 위한 클래스 
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
 * $Log: ErrorQueue.java,v $
 * Revision 1.1  2018/01/15 03:39:54  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:38  cvs
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
 * Revision 1.1  2007/11/26 08:39:09  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2006/11/01 12:05:32  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class ErrorQueue {
    
    private static Object dummy = new Object();
    
    private static ErrorQueue instance;

	private List msgQueue;
    
    /**
     * 싱글톤 처리 
     */
    private ErrorQueue(){
        msgQueue = new Vector();
    }
    
    public static ErrorQueue getInstance() {
        if (instance == null) {
            synchronized (dummy) {
                instance = new ErrorQueue();
            }
        }
        return instance;
    }

	/**
	 * Inserts the specified element into this queue, if possible.
	 * 
	 * @param msg
	 * @return
	 */
	public boolean offer(Object msg) {
		return msgQueue.add(msg);
	}

	/**
	 * Retrieves, but does not remove, the head of this queue, returning null if
	 * this queue is empty.
	 * 
	 * @return
	 */
	public Object peek() {
		if (msgQueue.size() == 0) {
			return null;
		}
		return (Object) msgQueue.get(0);
	}

	/**
	 * Retrieves and removes the head of this queue, or null if this queue is
	 * empty.
	 * 
	 * @return
	 */
	public Object poll() {
		if (msgQueue.size() == 0) {
			return null;
		}
		return (Object) msgQueue.remove(0);
	}

}
