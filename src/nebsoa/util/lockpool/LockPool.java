/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.util.lockpool;

import java.util.NoSuchElementException;

import org.apache.commons.pool.impl.GenericKeyedObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;

import nebsoa.common.exception.TooManyRequestException;
import nebsoa.common.log.LogManager;

/*******************************************************************
 * <pre>
 * 1.설명 
 * LockPool을 구현한 클래스이며,  org.apache.commons.pool.impl.GenericObjectPool을
 * wrapping하여 사용한다.
 * 
 * 2.사용법
 * LockPool pool = LockPoolManager.getInstance().getPool(id);
 *  if(pool==null){
 *      LockFactory factory = new LockFactory();
 *      pool=LockPoolManager.getInstance().makeLockPool(
 *              id,factory,maxLockCount);
 *  }
 *  
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author 이종원
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 
 * $Log: LockPool.java,v $
 * Revision 1.1  2018/01/15 03:39:50  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:51  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:24  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:27  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:52  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:25  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:38  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2007/05/01 07:39:58  김성균
 * Lock 처리 기능 추가
 *
 * Revision 1.1  2007/05/01 06:40:35  이종원
 * 최초작성
 *
 *
 * </pre>
 ******************************************************************/
public class LockPool extends GenericObjectPool {
    
    String name;
    LockFactory factory;
    

   /**
    * default로 Lock이부족 할 때 오류를 내는 Lock 풀을  리턴
    */
    public LockPool(LockFactory factory, int maxActive) {
         super(factory,maxActive,GenericKeyedObjectPool.WHEN_EXHAUSTED_FAIL,0);
         this.factory=factory;
     }



   
   /**
    * 최대 허용 Lock 수(max Active):인자로 받은 값. 
    * 요청 폭주시 반응 : 인자로 받은 값 (기다림:1, 에러유발:0, 풀사이즈 증가:2  중 기다림 세팅 )
    * 기다림일 경우 기다리는 시간(밀리세컨드 ): 인자로 받은 값을 세팅하여 Lock 풀을 만든다.
    * <font color='red'><b>만약 기다림 시간이 200(0.2초  보다 작다면 초단위로 세팅했다고 가정하여,
    * 1000을 곱하여 세팅한다.</b></font>
    * 2004.09 이종원 작성.
    */
    public LockPool(LockFactory factory, int maxActive, 
            int maxWaitTime) {
        super(factory,maxActive,GenericKeyedObjectPool.WHEN_EXHAUSTED_BLOCK,
               (maxWaitTime<200?maxWaitTime*1000:maxWaitTime));
        super.setTestOnReturn(true);
        this.factory=factory;
    }
   
    public Lock getLock() throws Exception {
        Lock lock= null;
        try{
            lock=(Lock)super.borrowObject();
            lock.setName(this.name);
            lock.setDone(false);
            return lock;
        }catch(NoSuchElementException e){
            throw new TooManyRequestException(
                    "요청 처리가 지연되고 있습니다.["+name+"-최대허용갯수:"+this.getMaxActive()+"]");
        }
    }

   /* (non-Javadoc)
    * @see org.apache.commons.pool.ObjectPool#returnObject(java.lang.Object)
    */
    public void returnLock(Lock lock) throws Exception {
        if(lock==null) return;
        lock.setDone(true);
        super.returnObject(lock);
        LogManager.debug(this.getInfo());
    }

    public boolean closed(){
        return super.isClosed();
    }

    public String toString(){
        return getInfo();
    }
    
    public String getInfo() {
        if(isClosed()) return name+" socket pool is closed...";
        return "\tLockPool :["+name
        +"] 수행중인 프로세스:["+getNumActive()
        +"] 생성가능 여분 프로세스 :["+getNumIdle()+"]";
    }
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LockFactory getFactory() {
        return factory;
    }

    public void setFactory(LockFactory factory) {
        this.factory = factory;
    }
}