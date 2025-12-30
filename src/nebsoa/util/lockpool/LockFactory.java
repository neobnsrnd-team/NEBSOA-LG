/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.util.lockpool;

import org.apache.commons.pool.PoolableObjectFactory;

/*******************************************************************
 * <pre>
 * 1.설명 
 * Lock를 생성하는 factory이며 org.apache.commons.pool.PoolableObjectFactory를
 * wrapping하여 사용한다.
 * 
 * 2.사용법
 * LockFactory factory = new LockFactory();
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
 * $Log: LockFactory.java,v $
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
 * Revision 1.1  2007/05/01 06:40:35  이종원
 * 최초작성
 *
 *
 * </pre>
 ******************************************************************/
public class LockFactory implements PoolableObjectFactory {
    

    public LockFactory(){

    }

 
    /**
     * make Lock Object
     */
    public Object makeObject() throws Exception{
        return new Lock();
    }


   /**
    * validate Lock Object : 무조건 return true
    */
    public boolean validateObject(Object obj) {
        return true;
    }

   /**
    * do nothing
    */
   public void activateObject(Object obj) {

   }

   /**
    * do nothing
    */
    public void passivateObject(Object obj){}

    /**
     * do nothing
     */
    public void destroyObject(Object arg0) {}
}