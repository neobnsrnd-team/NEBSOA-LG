/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
package nebsoa.spiderlink.connector.listener.util;

import nebsoa.spiderlink.engine.listener.BaseListener;
import nebsoa.spiderlink.engine.listener.tcp.TcpListener;



/*******************************************************************
 * <pre>
 * 1.설명 
 * TCP 리스너를 종료 시킬 때 맨 마지막에 serversocket.accept()하고 있는 
 * 것을 없애기 위하여 사용되는 클래스.
 * 대상 시스템 별로 사전에 정해진 shutdown 명령어를 write한다.
 *   
 * 2.사용법
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author 이종원
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 
 * $Log: ShutdownCommander.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:32  cvs
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
 * Revision 1.1  2008/08/04 08:54:54  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:35  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:39:09  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2006/10/03 09:12:44  이종원
 * refactoring
 *
 * Revision 1.1  2006/08/01 14:00:16  이종원
 * code정리
 *
 *
 * </pre>
 ******************************************************************/
public abstract class ShutdownCommander {

    protected TcpListener listener;
    /**
     * 생성자
     */
    public ShutdownCommander(TcpListener listener) {
        this.listener=listener;
    }
    
    public abstract void shutdown();

    public BaseListener getListener() {
        return listener;
    }

    public void setListener(TcpListener listener) {
        this.listener = listener;
    }
}