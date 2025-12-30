/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
package nebsoa.spiderlink.connector.listener.protocol;

import nebsoa.spiderlink.connector.listener.DefaultTcpGatewayListener;



/*******************************************************************
 * <pre>
 * 1.설명 
 * 동시에 여러 전문을 수신하여 처리하기 위한 multi 처리  클래스를 만들 때 상속 받을 
 * 클래스 .실제로  Thread는 아니며, thread pool을 사용하여 동작한다.
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
 * $Log: InputMessageHandler.java,v $
 * Revision 1.1  2018/01/15 03:39:51  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:54  cvs
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
 * Revision 1.1  2008/01/22 05:58:24  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:39:14  안경아
 * *** empty log message ***
 *
 * Revision 1.3  2007/03/02 11:58:32  김승희
 * handlerUID 추가
 *
 * Revision 1.2  2006/10/10 05:24:12  이종원
 * *** empty log message ***
 *
 * Revision 1.1  2006/10/03 09:12:44  이종원
 * refactoring
 *
 * Revision 1.9  2006/10/03 08:49:43  이종원
 * *** empty log message ***
 *
 * Revision 1.8  2006/10/02 23:44:30  이종원
 * *** empty log message ***
 *
 * Revision 1.7  2006/10/02 06:40:14  이종원
 * *** empty log message ***
 *
 * Revision 1.6  2006/09/29 09:05:10  이종원
 * *** empty log message ***
 *
 * Revision 1.5  2006/08/04 05:15:06  안경아
 * *** empty log message ***
 *
 * Revision 1.4  2006/08/01 14:00:01  이종원
 * code정리
 *
 * Revision 1.3  2006/08/01 10:25:44  이종원
 * *** empty log message ***
 *
 * Revision 1.2  2006/08/01 10:01:11  이종원
 * *** empty log message ***
 *
 * Revision 1.1  2006/07/31 11:06:42  이종원
 * 최초작성
 *
 * </pre>
 ******************************************************************/
public abstract class InputMessageHandler implements Runnable{
//, Lifecycle {
    
    protected DefaultTcpGatewayListener listener;
    
    public InputMessageHandler(){

    }
    
    protected boolean exit=false;
    
    /**
     *  현재 핸들러의 UID
     */
    protected String handlerUID;
    
    public void setHandlerUID(String handlerUID){
    	this.handlerUID = handlerUID;
    }
    
    public String getHandlerUID(){
    	return this.handlerUID;
    }
    
    public void stop(){
        exit=true;
    }

    /**
     * prepare to start service...
     * 2006. 9. 29.  이종원 작성
     */
    public abstract void init();

    /**
     * 전문을 받아 처리 하는 로직을 구현할 부분
     * 2006. 8. 1. 이종원 작성
     * @see java.lang.Runnable#run() 재정의
     */    
    public abstract void handleInputMessage();
    

    /**
     * 전문처리를 종료하는 로직을 구현할 부분.
     * 2004. 8. 1.  이종원 작성
     */
    public abstract void destroy();
 
    public DefaultTcpGatewayListener getListener() {
        return listener;
    }
    public void setListener(DefaultTcpGatewayListener listener) {
        this.listener = listener;
        listener.addHandler(this);
    }
}
