/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.connector.adapter;

import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;

import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.StringUtil;
import nebsoa.spiderlink.connector.constants.ConnectorConstants;
import nebsoa.spiderlink.connector.io.writer.TcpMessageWriter;
import nebsoa.spiderlink.context.MessageContext;
import nebsoa.spiderlink.engine.adapter.TimeoutException;
import nebsoa.spiderlink.socketpool.SocketPool;
import nebsoa.spiderlink.socketpool.WorkerSocket;

/*******************************************************************
 * <pre>
 * 1.설명 
 * NO-ACK LISTENER가 수신한 전문에 대하여 처리를 완료하고,
 * 타기관으로 TCP 전문 전송을 <font color='red'>NoAck로 처리</font>
 * 하는 class를 만들 때 상속 받을 클래스. 
 *  <font color='red'>NoAck란 열려진 소켓으로 받거나, 보내기만 하는 경우를 얘기함.
 * 하위 클래스에서는 protocol에 맞게 write만 하면 되므로, createWriter()만 구현
 * 하면 된다.
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
 * $Log: NoAckResTcpGatewayAdapter.java,v $
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
 * Revision 1.2  2008/03/04 07:44:14  김승희
 * no ack 관련 수정
 *
 * Revision 1.1  2008/01/22 05:58:25  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.2  2008/01/16 10:29:04  이종원
 * 해당 시스템에 소켓을 열지 못할 때 에러로 마킹
 *
 * Revision 1.1  2007/11/26 08:38:24  안경아
 * *** empty log message ***
 *
 * Revision 1.3  2007/03/07 06:40:40  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2006/10/13 11:57:53  이종원
 * *** empty log message ***
 *
 * Revision 1.1  2006/10/03 09:12:44  이종원
 * refactoring
 *
 * Revision 1.2  2006/10/03 07:52:15  이종원
 * MessageWriter생성을 설정에서 읽어 올수도 있게 수정
 *
 * Revision 1.1  2006/10/03 07:38:49  이종원
 * 최초작성
 *
 * </pre>
 ******************************************************************/
public class NoAckResTcpGatewayAdapter extends AbstractTcpGatewayAdapter {

    public NoAckResTcpGatewayAdapter() {

    }

    /**
     * NO-ACK 방식으로 전문을 전송(handleRequest)하고 끝내도록 흐름만 구현하였다.
     * 실제 보내는 부분에 대하여는 상속 받은 클래스가 createWriter만 구현 하면 된다.
     * 2006. 9. 16. 이종원 작성
     * @throws Exception 
     * @see nebsoa.spiderlink.connector.GatewayConnector#execute(nebsoa.spiderlink.context.MessageContext) 재정의
     */
    protected void execute(MessageContext ctx) throws Exception {
        WorkerSocket socket = null;

        try{
            socket=getWorkerSocket();
            //보내고자 하는 것이 혹시 byte[]이 아니라 다른 형태인지..확인..
            Object obj = ctx.getResult();
            if(!(obj instanceof byte[])){
                throw new Exception("ResultMessage is not byte[ ].It's Exception.. check logic...");
            }
            byte[] responseMessage =(byte[]) obj;

            TcpMessageWriter writer = socket.getMessageWriter();
            if(writer ==null){
                writer = createMessageWriter(socket.getDataOutputStream());
                if(writer.getDataOutputStream() == null){
                    writer.setDataOutputStream(socket.getDataOutputStream());
                }
                socket.setMessageWriter(writer);
            }
            LogManager.infoLP("비동기 응답 전문 전송 ["+ctx.toString()+"]");
                  //  +new String(responseMessage)+"]");
            writer.writeMessage(responseMessage);
            //TODO 상세 예외 처리를 구현 클래스에서 할지 여기서 할지는 ... 
        } catch (EOFException e) {
            LogManager.error("비동기 응답 전문 송신 에러 "+e.toString());
            if(socket != null) socket.stop();
            throw e;
        } catch (TimeoutException e) {
            LogManager.error("비동기 응답 전문 송신 에러 "+e.toString());
            if(socket != null) socket.stop();
            throw e;
        } catch (java.net.SocketException e) {
           	//setError(true); -- getWorkerSocket에서 체크 하니까 여기서는 무시하자.
            LogManager.error("비동기 응답 전문 송신 에러 "+e.toString());
            if(socket != null)socket.stop();
            throw e;    	
        } catch (IOException e) {
            LogManager.error("비동기 응답 전문 송신 에러 "+e.toString());
            if(socket != null)socket.stop();
            throw e;

        } catch (Exception e) {
            LogManager.error("비동기 응답 전문 송신 에러 "+e.toString());
            throw e;
        } catch (Throwable e) {
            LogManager.error(e.getMessage(),e);
            throw new SysException(e);
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
    /**
     * 하위 클래스에서 구현해야 할 메소드입니다.
     * 구현하지 않을 경우 Gateway설정 중에 output_message_handler값을 찾아
     * 객체를 생성합니다. 최적화 하려면 상속받아 구현하면 됩니다. 
     */
    protected TcpMessageWriter createMessageWriter(
            DataOutputStream dataOutputStream) throws Exception {
        String writerName = getProperty(ConnectorConstants.OUT_MESSAGE_HANDLER);
        if(StringUtil.isNull(writerName)){
            throw new SysException("implement createMessageWriter or set ["
                    +ConnectorConstants.OUT_MESSAGE_HANDLER+"] parameter");            
        }
        TcpMessageWriter writer = (TcpMessageWriter) forName(writerName);
        writer.setDataOutputStream(dataOutputStream);
        return writer;
        
    }
}// end 