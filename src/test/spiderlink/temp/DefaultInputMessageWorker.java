/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package test.spiderlink.temp;

import java.io.IOException;

import nebsoa.spiderlink.connector.listener.worker.InputMessageWorker;


/**
 * worker 
 * @author 이종원
 */
/*******************************************************************
 * <pre>
 * 1.설명 
 * Sync Type과 Async Type의 요청을 thread mode나 non-thread mode로 
 * 처리 하는 worker. 흐름은 아래와 같다.
 *  dataMap = handler.process( messageContext,  dataMap);
 *  owner.sendResponse((byte[]) messageContext.getResult());
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
 * $Log: DefaultInputMessageWorker.java,v $
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
 * Revision 1.1  2008/08/04 08:54:51  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.2  2008/02/20 00:42:48  오재훈
 * *** empty log message ***
 *
 * Revision 1.1  2007/11/26 08:37:59  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2006/10/03 09:12:44  이종원
 * refactoring
 *
 * Revision 1.1  2006/10/03 03:05:15  이종원
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class DefaultInputMessageWorker extends InputMessageWorker{
   
    public DefaultInputMessageWorker(){
        
    }

    
    /**
     * 요청 받은 전문을 처리한다.
     * ioProtocol이  SYNC일 때와 ASYNC일 때 사용  된다.
     * 2006. 10. 2.  이종원 작성
     * @throws Throwable 
     * @throws IOException 
     */
    public void doProcess() throws IOException, Throwable {
        dataMap = handler.process( messageContext,  dataMap);
        owner.sendResponse((byte[]) messageContext.getResult());
    }
}