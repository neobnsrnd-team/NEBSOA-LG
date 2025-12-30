/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.util;

import nebsoa.common.log.LogManager;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 성능 향상을 위해 Pooling을 쉽게 사용 할 수 있도록 만든 클래스 입니다.
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
 * $Log: Pool.java,v $
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
 * Revision 1.1  2008/01/22 05:58:17  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:01  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2006/06/17 10:37:45  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public final class Pool  
{
	private Object pool[];

    private int max=10;    
    
    private int current=-1;
    
    private String poolName="";

    public Pool(String name,int max) {
    	this.poolName = name;
    	this.max = max;
    	pool = new Object[max];
    }
    
    public Pool(int max) {
        this("",max);
    }

	/**
	* pool size가 10인 pool 생성
	*/
	public Pool() {
        pool = new Object[max];
    }




    /**
     * Add the object to the pool, silent nothing if the pool is full
     */
    public Object put(Object o) 
    {
        int idx=-1;
     
        synchronized( this ) {
            if( current < max - 1 ){
                idx = ++current;
            }else {
				return o;
			}
            if( idx >= 0 ) {
                pool[idx] = o;
                LogManager.debug(poolName+" POOL에 잔여 객체 수 : "+(idx+1));
                return null;
            }else{
            	return o;
            }
            
        }
    }

    /**
     * Get an object from the pool,만약 클래스를 지정하지 않은 경우
	 pool에 객체가 없으면 null이 return되며 클래스를 지정한 경우 객체를 생성하여 return
     */
    public  Object get() {
        int idx = -1;
        synchronized( this ) {
            if( current >= 0 ) {
                idx = current;
                current--;
                LogManager.debug("잔여 객체 수 : "+(current));
                return pool[idx];
            }
        }
        return null;

    }

    /** 
	Return the size of the pool
    */
    public int getMax() {
        return max;
    }
    
    public int getCurrent(){
    	return current;
    }
}
