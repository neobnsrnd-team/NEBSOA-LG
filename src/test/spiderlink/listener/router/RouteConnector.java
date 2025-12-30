/*******************************************************************
 * nebsoa link 프로젝트 
 *
 * Copyright (c) 2003-2004 by COMAS, Inc.
 * All rights reserved.
 * @file : EAIConnector.java
 * @author : 이종원
 * @date : 2004. 9. 1.
 * @설명 : 
 * <pre>
 * 
 * </pre>
 *******************************************************************
 * $Id: RouteConnector.java,v 1.1 2018/01/15 03:39:54 cvs Exp $
 * $Log: RouteConnector.java,v $
 * Revision 1.1  2018/01/15 03:39:54  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:40  cvs
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
 * Revision 1.2  2008/02/20 00:42:47  오재훈
 * *** empty log message ***
 *
 * Revision 1.1  2007/11/26 08:37:37  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2007/07/15 10:04:19  이종원
 * *** empty log message ***
 *
 * Revision 1.1  2006/07/04 11:34:26  이종원
 * *** empty log message ***
 *
 * Revision 1.5  2006/03/08 02:18:24  jwlee
 * *** empty log message ***
 *
 * Revision 1.4  2006/03/07 15:29:38  jwlee
 * *** empty log message ***
 *
 * Revision 1.3  2006/03/06 07:36:33  jwlee
 * *** empty log message ***
 *
 * Revision 1.2  2006/03/05 21:03:17  jwlee
 * *** empty log message ***
 *
 * Revision 1.1  2006/03/05 20:27:16  jwlee
 * *** empty log message ***
 *
 * Revision 1.7  2004/11/24 03:03:22  helexis
 * timeout 에 대한 Exception throws 구문 추가
 *
 ******************************************************************/
package test.spiderlink.listener.router;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * </pre> 
 */
public class RouteConnector {
    public Socket recvSocket = null;
    public Socket sendSocket = null;

    public DataInputStream dis = null;

    public DataOutputStream dos = null;

    String logConfig;

    boolean isClosed;

    Exception monitorException;


    /**
     * 인자로 받은 SOCKET을 이용하여  in, out stream을 연다.
     * Listener에서 전문 수신시에 사용하는 방식이다.
     * @param source
     * @param logConfig
     */
    public RouteConnector(Socket source,Socket target,
            String logConfig) {
        this.logConfig = logConfig;
        this.recvSocket = source;
        this.sendSocket = target;
        openStream();
        monitorException = new Exception();
    }




    /**
     * 2004. 8. 31. 이종원 작성
     * @return
     * 설명: 처음 10byte를 읽어 전체 길이를 알아 낸후 그 길이 만큼 읽어 온다.
     * 맨 끝에 0x1f가 있는지 화면에 출력도 해 준다.
     */
    public byte[] readEaiData() throws SocketTimeoutException, EOFException {

        byte[] buf = null;
        try {
            byte[] lenBuf = new byte [10];
            readFully(lenBuf);
            String lenStr = new String(lenBuf);
            int len = Integer.parseInt(lenStr);

            if (len > 0) {
                buf = new byte [len];
                System.arraycopy(lenBuf, 0, buf, 0, 10);

                dis.readFully(buf, 10, len - 10);


                System.out.println( "수신전문[" + buf.length + "]byte\n["
                        + new String(buf) + "]");
                return buf;
            } else {
                throw new RuntimeException("EAI 데이타 읽기 실패:읽을 데이타가 없습니다.");
            }
        } catch (EOFException e) {            
            throw e;
        } catch (SocketTimeoutException e) {
            throw e;
        } catch (Exception e) {
            System.out.println( e.getMessage());
            throw new RuntimeException("EAI 데이타 읽기 실패" + e.getMessage());
        }
        //return message.getBytes();
    }

    /**
     * 
     * 2004. 8. 31. 이종원 작성
     * 
     * 설명: input , output stream을 모두 연다.
     */
    public void openStream() {
        if (dis == null) {
            dis = openInputStream();
        }
        if (dos == null) {
            dos = openOutputStream();
        }
    }

    /**
     * 
     * 설명: INPUT STREAM을 연다.
     */
    public DataInputStream openInputStream() {
        if (recvSocket == null || recvSocket.isClosed()) {
            throw new RuntimeException("socket not opened...");
        }
        try {
            dis = new DataInputStream(recvSocket.getInputStream());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("FAIL TO OPEN INPUT STREAM");
        }
        return dis;
    }

    /**
     * 설명: OUTPUT STREAM을 연다.
     */
    public DataOutputStream openOutputStream() {
        if (sendSocket == null || sendSocket.isClosed()) {
            throw new RuntimeException("send socket not opened...");
        }
        try {
            dos = new DataOutputStream(sendSocket.getOutputStream());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("FAIL TO OPEN OUTPUT STREAM");
        }
        return dos;
    }

    /**
     * 
     * 설명: 해당 바이트의 데이터를 전송한다.
     */
    public int writeFully(byte[] data) throws RuntimeException {
        if (dos == null) {
            throw new RuntimeException("OUTPUT STREAM이 열려 있지 않습니다.");
        }
        try {
            System.out.println( "송신전문[" + data.length + "]byte\n["
                    + new String(data) + "]");
            dos.write(data, 0, data.length);
            dos.flush();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return data.length;
    }

    /**
     * 
     * 설명: 해당 바이트에 데이터를 읽어 들인다.
     */
    public byte[] readFully(byte[] buf) throws IOException {
        if (dis == null) {
            throw new RuntimeException("INPUT STREAM이 열려 있지 않습니다.");
        }
        try {
            dis.readFully(buf);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw e; 
        }
        return buf;
    }

    /**
     * 설명: 해당 길이 만큼의 데이터를 읽어 들인다.
     */
    public byte[] readFully(int len) throws IOException {
        byte[] buf = new byte [len];
        return readFully(buf);
    }

    /**
     * 데이타 길이를(writeShort) 전송하고 데이타를 전송 한다
     */
    public int writeShort(byte[] bSend) throws RuntimeException {
        if (dos == null) {
            throw new RuntimeException("OUTPUT STREAM이 열려 있지 않습니다.");
        }
        try {
            dos.writeShort((short) bSend.length);
            dos.write(bSend, 0, bSend.length);
            dos.flush();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return bSend.length + 2;
    }

    /**
     * 먼저 데이타 길이를 읽고 후에 데이타를 받아 온다.
     */
    public byte[] readInt() throws RuntimeException {
        try {
            int len = dis.readInt();
            byte[] buf = new byte [len];
            return readFully(buf);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 타임아웃을 설정한다.
     */
    public void setTimeout(int nSec) {
        try {
            recvSocket.setSoTimeout(nSec);
        } catch (Exception e) {
            throw new RuntimeException("setTimeout error:" + e.getMessage());
        }
    }

    public void close() {
        try {
            if (dis != null) {
                dis.close();
            }
        } catch (Exception e) {
        }

        try {
            if (dos != null) {
                dos.close();

            }
        } catch (Exception e) {
        }

        try {
            if (recvSocket != null) {
                recvSocket.close();
            }
        } catch (Exception e) {
        }
        
        try {
            if (sendSocket != null) {
                sendSocket.close();
            }
        } catch (Exception e) {
        }
        dis = null;
        dos = null;
        recvSocket = null;
        sendSocket = null;
        isClosed = true;

    }

    public void route() throws IOException {
        byte[] recv = new byte[80];
        readFully(recv);
        System.out.println("==> 수신 ["+new String(recv)+"]");
        String name="이종원";
        byte[] temp = name.getBytes();
        System.arraycopy(temp,0,recv,50,temp.length);
        System.out.println("<== 송신 ["+new String(recv)+"]");
        writeFully(recv);
        System.out.println("송신완료 =========================");
    }
    
    public static void main(String args[])throws Exception{
        Socket s = new Socket ("localhost",10001);
        DataInputStream in = new DataInputStream(s.getInputStream());
        DataOutputStream out = new DataOutputStream(s.getOutputStream());
        String test="1234567890";
        byte[] data = test.getBytes();
        byte[] recv=new byte[10];
        for(int i=0;i<data.length;i++){
            out.write(data[i]);
            recv[i]=(byte) in.read();
        }
        in.close();
        out.close();
        s.close();
        System.out.println(new String(recv));       
    }

}