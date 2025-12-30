/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
package nebsoa.spiderlink.engine.listener;

import java.util.ArrayList;
import java.util.Iterator;

import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;
import nebsoa.spiderlink.connector.listener.protocol.InputMessageHandler;
import nebsoa.spiderlink.engine.Lifecycle;
import nebsoa.spiderlink.engine.adapter.AdapterManager;


/*******************************************************************
 * <pre>
 * 1.설명 
 * 리스너를 만들 때 기본이 되는 클래스.
 * 내적으로 
 *   1) AdapterManager에 regist 
 *   2) init
 *   3) service
 *   4) destroy
 *   5) AdapterManager로부터 remove 수행 하므로
 *   추가적으로 adapterMananger에 등록 / 삭제 하는 로직은 필요하지 않다.
 *   
 * 2.사용법
 * 생성자 참조.
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author $Author: cvs $
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 
 * $Log: BaseListener.java,v $
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
 * Revision 1.1  2008/01/22 05:58:34  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:39:11  안경아
 * *** empty log message ***
 *
 * Revision 1.6  2006/10/03 09:12:44  이종원
 * refactoring
 *
 * Revision 1.5  2006/08/01 14:00:01  이종원
 * code정리
 *
 * Revision 1.4  2006/08/01 10:01:11  이종원
 * *** empty log message ***
 *
 * Revision 1.3  2006/08/01 00:30:09  김승희
 * Lifecycle 패키지 변경
 *
 * Revision 1.2  2006/07/31 11:17:12  이종원
 * 최초작성
 *
 * Revision 1.1  2006/07/31 11:06:42  이종원
 * 최초작성
 *
 * </pre>
 ******************************************************************/
public abstract class BaseListener extends Thread implements Lifecycle{

    protected String name ; 
   
    protected ListenerType listenerType;

    protected String logConfig;
    
    /**
     * 수행을 중지 해야 할지 판단하는 변수
     */
    public boolean exit=false;
    /**
     * 수행 중지가 완료 되었는지 여부.
     */
    public boolean stopped=false;
    
    /**
     * MessageHanlder들을 관리하는 ArrayList
     */
    protected ArrayList handlerList;
    

    /**
     * 생성자
     */
    public BaseListener(String name,ListenerType listenerType,
            String logConfig) {
        this.name=name;
        this.listenerType=listenerType;
        this.logConfig = logConfig;
        handlerList=new ArrayList();
    }
    
    public String toString(){
        return name+"@"+getClass().getName()
        +"@"+listenerType.getType();
    }
    
    /**
     * 큰 흐름은 다음과 같다.
     *      register(); //AdapterManager에 자신을 등록
     *      try{
                init(); //초기화 한다.(serversocket open등과 같은 일)
                service();  //client의 접속에 대한 처리를 한다.
                exit=true;  //더이상 처리를 하지못하도록 flag처리
            }finally{
               //AdapterManager에서  자신을 삭제
                AdapterManager.getInstance().close(this);
               //내부적으로  destroy(); 가 호출 되어 열려진 자원을 닫는다.

                //딸려 있는 자식들을 종료 시킨다.
                destroyHandlers();
            } 
     * 2006. 8. 1. 이종원  작성
     * @see java.lang.Runnable#run() 재정의
     */
    public void run(){
        register();
        try{
            init();
            service(); 
            exit=true;            
        }finally{
            exit=true;
            AdapterManager.getInstance().close(this);
            destroyHandlers();
        }
        
    }
    
    
    /**
     * 해당 listener에 딸려 있는 thread를 종료 시킨다.
     * 2006. 8. 1.  KOREAN 작성
     */
    protected void destroyHandlers() {
        
        if(handlerList.isEmpty()) return;
        LogManager.debug(logConfig, 
                name+"Listener에 딸려 있는 thread를 종료 시킵니다");
        Iterator i = handlerList.iterator();
        InputMessageHandler handler=null;
        while(i.hasNext()){
            handler=(InputMessageHandler) i.next();
            removeHandler(handler);
        }  
        LogManager.debug(logConfig, 
                name+"Listener에 딸려 있는 thread를 종료 완료");
    }
    /**
     * AdapterManager에 자신을 등록 한다.
     * AdapterManager.getInstance().register(this)
     * 2004. 8. 1.  이종원 작성
     */
    public void register(){
        AdapterManager.getInstance().register(this);
    }
    /**
     * 이 리스터에 인자로 전달 받은 핸들러를 등록 한다.
     * 이미 등록된 핸들러 일 때 에러를 발생한다.
     * 2004. 8. 1.  이종원 작성
     */
    public void addHandler(InputMessageHandler handler){
        if(handlerList.contains(handler)){
            throw new SysException("이미 등록되어 있는 Handler입니다"+handler);
        }
        handlerList.add(handler);
    }
    
    /**
     * 이 리스터에 인자로 전달 받은 핸들러를 제거 한다.
     * 대상이 등록된 핸들러가 아닐 때에는 skip 한다.
     * 2004. 8. 1.  이종원 작성
     */
    public void removeHandler(InputMessageHandler handler){
        if(!handlerList.contains(handler)){
            LogManager.error(logConfig,"등록되어 있지 않는 Handler입니다"+handler);
            return;
        }
        handler.destroy();
        handlerList.remove(handler);
    }
    
    
    /**
     * listener를 구현 할 때 초기화 작업을 하도록 구현 할 부분.  
     * 2004. 7. 31. 이종원 작성
     * @see nebsoa.spiderlink.engine.Lifecycle#init() 재정의
     */
    public abstract void init();
    
    /**
     * listener를 구현 할 때 요청 처리하는 로직을 구현 할 부분.
     * 필요시 멀티 쓰레딩이나, 모니터링 데이터 수집 등도 여기에 들어 간다.  
     * 2004. 7. 31. 이종원 작성
     * @see nebsoa.spiderlink.engine.Lifecycle#init() 재정의
     */
    public abstract void service();
    
    /**
     * listener를 구현 할 때 마무리  작업을 하도록 구현 할 부분.  
     * 2004. 7. 31. 이종원 작성
     * @see nebsoa.spiderlink.engine.Lifecycle#init() 재정의
     */
    public abstract void destroy();

    public boolean isStopped() {
        return stopped;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }
    
}