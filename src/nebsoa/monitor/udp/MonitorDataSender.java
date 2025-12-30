/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
package nebsoa.monitor.udp;

import java.net.*;
import java.nio.*;
import java.nio.channels.*;

import nebsoa.common.log.LogManager;
/*******************************************************************
 * <pre>
 * 1.설명 
 * UDP기반으로 Agent가 모니터링 데이터를 전송. 
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
 * $Log: MonitorDataSender.java,v $
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
 * Revision 1.1  2008/01/22 05:58:35  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:39:12  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2006/09/11 14:13:49  이종원
 * className refactoring
 *
 * </pre>
 ******************************************************************/
public class MonitorDataSender {
    
    public static void sendData(String[] args) throws Exception {

        String adminServerIp="localhost"; 
            //PropertyManager.getProperty("monitor","ADMIN_IP");
        int adminServerPort=22500;
            //PropertyManager.getIntProperty("monitor","ADMIN_PORT",22500);
        
        ByteBuffer byteBuf=ByteBuffer.allocate(8);
//        byteBuf.order(ByteOrder.LITTLE_ENDIAN);
        LongBuffer longBuf=byteBuf.asLongBuffer();

        DatagramChannel dc=DatagramChannel.open();
        DatagramSocket socket=dc.socket();
        LogManager.debug("UDP Socket bounded?"+socket.isBound());
        dc.connect(new InetSocketAddress(adminServerIp, adminServerPort));
        LogManager.debug("UDP Socket bounded?"+socket.isBound());

        int i=0;
        while(i++<200){
            long time=System.currentTimeMillis();
            System.out.println(time);
            longBuf.put(0, time);
            dc.write(byteBuf);
            byteBuf.clear();
            Thread.sleep(100);
        }
    }
    /**
     * test 메소드.
     * 2005. 9. 11.  이종원 작성
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception
    {
        sendData(null);
    }    

}
