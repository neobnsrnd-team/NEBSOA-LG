/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
package nebsoa.common.async;

import nebsoa.common.cache.policy.CacheFactory;
import nebsoa.common.cache.policy.FIFOCache;

/*******************************************************************
 * <pre>
 * 1.설명 
 * AsyncCaller Pool 클래스
 * 최대 사이즈와 timeout 시간을 지정하여 해당 수치가 넘으면 pool에서 자동삭제된다.
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
 * $Log: AsyncCallerPool.java,v $
 * Revision 1.1  2018/01/15 03:39:54  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:37  cvs
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
 * Revision 1.1  2008/04/08 04:42:52  김승희
 * 신규추가
 *
 * Revision 1.1  2008/04/08 02:32:43  shkim
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public class AsyncCallerPool {
	
	private static Object dummy=new Object();
	
	private static AsyncCallerPool instance;
	
	public static AsyncCallerPool getInstance(){
	    if(instance==null){
	        synchronized (dummy) {
	            if(instance==null) instance = new AsyncCallerPool();
	        }
	    }
	    return instance;
	}
	
	private FIFOCache callerPool;
	
	private AsyncCallerPool(){
		init();
	}
	
	private synchronized void init(){
		long timeout = 15*60*1000; //15분
		int size = 1000000; //100만
		
		callerPool = CacheFactory.getInstance().getFifoCache("_asyncCallerPool", timeout, size);

	}
	
	/**
	 * AsyncCaller를 pool에 넣는다.
	 * @param caller
	 */
	public void put(AsyncCaller caller){
		callerPool.addObject(caller.getUid(), caller);
	}
	
	/**
	 * AsyncCaller를 pool에서 꺼내 리턴한 후 pool에서 삭제한다.
	 * @param uid
	 * @return
	 */
	public AsyncCaller get(Object uid){
		AsyncCaller asyncCaller = (AsyncCaller)callerPool.getObject(uid);
		callerPool.remove(uid);
		return asyncCaller;
	}
	
	/**
	 * pool의 사이즈(pool에 담긴 AsyncCaller의 갯수)를 리턴한다.
	 * @return
	 */
	public int size(){
		return callerPool.size();
	}
	
	/**
	 * 해당 uid의 AsyncCaller를 pool로부터 삭제한다.
	 * @param uid
	 */
	public void remove(Object uid){
		callerPool.remove(uid);
	}
	
}
