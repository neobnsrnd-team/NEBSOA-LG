/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.connector.adapter;

import java.io.EOFException;
import java.io.IOException;

import nebsoa.common.log.LogManager;
import nebsoa.spiderlink.connector.adapter.protocol.OutMsgReqHandler;
import nebsoa.spiderlink.context.MessageContext;
import nebsoa.spiderlink.engine.adapter.TimeoutException;
import nebsoa.spiderlink.queue.ProcessingMessagePool;
import nebsoa.spiderlink.socketpool.SocketPool;
import nebsoa.spiderlink.socketpool.WorkerSocket;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 타기관으로 TCP 전문 전송을 <font color='red'>ASync 처리</font>
 * 하는 class만들 때 상속 받을 클래스 
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
 * $Log: AsyncTcpGatewayAdapter.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:26  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:26  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:29  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:52  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.3  2008/07/09 07:38:33  김영석
 * 무조건 연결이 안된다고 에러로 마킹 할 수는 없다.
 * 현재 사용되는 소켓이 것이 하나라도 있으면 에러가 아닌것으로 처리 되도록 수정
 *
 * Revision 1.2  2008/03/11 01:26:00  김승희
 * 응답 없는 거래 처리
 *
 * Revision 1.1  2008/01/22 05:58:25  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.3  2008/01/16 02:50:39  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2008/01/15 05:14:24  이종원
 * 해당 시스템에 소켓을 열지 못할 때 에러로 마킹
 *
 * Revision 1.2  2007/06/28 02:07:52  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2007/06/15 04:57:57  안경아
 * *** empty log message ***
 *
 * Revision 1.3  2007/03/07 06:40:40  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2006/10/13 09:02:09  이종원
 * Logging MSG_ERROR로 수정
 *
 * Revision 1.1  2006/10/03 09:12:44  이종원
 * refactoring
 *
 * Revision 1.5  2006/09/28 14:08:19  이종원
 * *** empty log message ***
 *
 * Revision 1.4  2006/09/27 10:23:19  이종원
 * socket pool 디렉토리변경
 *
 * Revision 1.3  2006/09/27 07:40:46  이종원
 * update
 *
 * Revision 1.2  2006/09/23 01:29:00  이종원
 * 기능update
 *
 * Revision 1.1  2006/09/16 13:10:55  이종원
 * 어느정도 완성된 초안 작성
 *
 * Revision 1.1  2006/09/16 12:09:08  이종원
 * 어느정도 완성된 초안 작성
 *
 * </pre>
 ******************************************************************/
public abstract class AsyncTcpGatewayAdapter 
    extends AbstractTcpGatewayAdapter 
        implements OutMsgReqHandler{

    public AsyncTcpGatewayAdapter() {

    }

    /**
     * 전문을 전송(handleRequest)하고 끝난다.
     * 수신은 비동기로 socket에 내장된 reader가 처리 한다.
     * 비동기 방식으로 수신( handleResponse) 하도록 흐름만 구현하였다.
     * 실제 주고 받는 부분에 대하여는 상속 받은 클래스가책임지고   
     * handleRequest  handleResponse를 구현 하여야 한다.
     * <font color='red'>handleResponse는 THREAD로 수행 된다.</font>
     * 2004. 9. 16. 이종원 작성
     * @throws Exception 
     * @see nebsoa.spiderlink.connector.GatewayConnector#execute(nebsoa.spiderlink.context.MessageContext) 재정의
     */

    protected void execute(MessageContext ctx) throws Exception {
        WorkerSocket socket;
        try{
            socket=getWorkerSocket();
        } catch (Exception e) {
//        	setError(true); -- getWorkerSocket에서 체크 하니까 여기서는 무시하자.
            ctx.setResult(e);
            //TODO throw 해야 하지 return 할지...(sync니까.. throw..했다.)
            throw e;
            //return;
        }
        try{
            try{
                LogManager.infoLP("전문 전송 ["+ctx.toString()+"]");
                                
                //응답 메시지가 없는 유형의 전문인 경우는 아래를 생략한다.
                if(!ctx.getTrxMessage().isResMessageNone()){
                	//대기중인 전문으로 등록  한다.
                	ProcessingMessagePool.getInstance().put(ctx);
                }
                
                //request를 처리한다.
                handleRequest(ctx,socket);
                
                //async로 읽어 들일 전문 갯수가 하나 늘어 났음을 알린다.
                socket.increaseRemains();
                //TODO 상세 예외 처리를 구현 클래스에서 할지 여기서 할지는 ... 
            } catch (EOFException e) {
                LogManager.error(logConfig,ctx+"전문 송신 에러 "+e.toString());
                socket.close();
                socket=null;
                ctx.setResult(e);
                //TODO throw 해야 하지 return 할지...(sync니까.. throw..했다.)
                throw e;
                //return;

            } catch (TimeoutException e) {
                LogManager.error(logConfig,ctx+"전문 송신 에러 "+e.toString());
                ctx.setResult(e);
                //TODO throw 해야 하지 return 할지...(sync니까.. throw..했다.)
                throw e;
                //return;

            } catch (IOException e) {
                LogManager.error(logConfig,ctx+"전문 송신 에러 "+e.toString());
                ctx.setResult(e);
                //TODO throw 해야 하지 return 할지...(sync니까.. throw..했다.)
                throw e;
                //return;
            } catch (Exception e) {
                LogManager.error(logConfig,ctx+"전문 송신 에러 "+e.toString());
                ctx.setResult(e);
                //TODO throw 해야 하지 return 할지...(sync니까.. throw..했다.)
                throw e;
                //return;
            }
        
        }finally{
            if(socket != null ){
                SocketPool pool = ((SocketPool) socket.getPool());
                if(pool != null &&!pool.closed()){
                    pool.returnObject(socket);
                }else{
                    // socket pool을 안쓰는 녀석...??
                    socket.close();
                }
            }
        }
    }

}// end 