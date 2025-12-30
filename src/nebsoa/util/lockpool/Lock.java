/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.util.lockpool;

import nebsoa.common.log.LogManager;


/*******************************************************************
 * <pre>
 * 1.설명 
 * Pool 에서 관리되는  LOCK 객체
 * 
 * 2.사용법
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
 * $Log: Lock.java,v $
 * Revision 1.1  2018/01/15 03:39:50  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:50  cvs
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
public class Lock{
    
    String name=null;
    
    long startTime;
   
    public Lock() {
        startTime=System.currentTimeMillis();
    }

    public void setDone(boolean done){
        if(!done){    
            startTime=System.currentTimeMillis();
        }else{
            long exeTime=getExeTime();
            if(exeTime > 2000){
                LogManager.info("MONITOR", name+ "수행시간 : "+(exeTime/1000.0)+"초");
            }
        }
    }
    
    public long getExeTime(){
        return System.currentTimeMillis()-startTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String toString() {
        return name;
    }
}