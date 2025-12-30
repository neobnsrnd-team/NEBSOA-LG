/*******************************************************************
 * 외환은행 CLS 연계 프로젝트
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
 * $Id: EAIConnector.java,v 1.1 2018/01/15 03:39:52 cvs Exp $
 * $Log: EAIConnector.java,v $
 * Revision 1.1  2018/01/15 03:39:52  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:11  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:25  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:28  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:51  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.2  2008/02/20 00:42:47  오재훈
 * *** empty log message ***
 *
 * Revision 1.1  2007/11/26 08:37:45  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2006/06/29 04:02:09  이종원
 * *** empty log message ***
 *
 * Revision 1.7  2004/11/24 03:03:22  helexis
 * timeout 에 대한 Exception throws 구문 추가
 *
 * Revision 1.6  2004/11/02 05:06:57  ljw
 * *** empty log message ***
 *
 * Revision 1.5  2004/11/02 03:42:20  ljw
 * 별거 없음
 *
 * Revision 1.4  2004/10/25 05:43:56  ljw
 * EOF처리 추가
 *
 * Revision 1.3  2004/10/15 08:50:26  helexis
 * 불필요한 import 정리
 *
 * Revision 1.2  2004/10/15 08:37:21  helexis
 * formatting 이후 재 commit
 *
 * Revision 1.20  2004/09/21 02:51:32  ljw
 * *** empty log message ***
 *
 * Revision 1.19  2004/09/10 08:25:08  ljw
 * *** empty log message ***
 *
 * Revision 1.18  2004/09/10 07:55:33  helexis
 * *** empty log message ***
 *
 * Revision 1.17  2004/09/10 06:56:33  ljw
 * *** empty log message ***
 *
 * Revision 1.16  2004/09/09 06:39:29  ljw
 * 총계정 연계
 *
 * Revision 1.15  2004/09/02 06:24:03  ljw
 * *** empty log message ***
 *
 * Revision 1.14  2004/09/02 01:19:38  ljw
 * *** empty log message ***
 *
 * Revision 1.13  2004/09/01 11:35:14  ljw
 * *** empty log message ***
 *
 * Revision 1.12  2004/09/01 06:14:24  ljw
 * *** empty log message ***
 *
 * Revision 1.11  2004/09/01 06:10:55  ljw
 * *** empty log message ***
 *
 * Revision 1.10  2004/09/01 05:45:51  ljw
 * *** empty log message ***
 *    
 ******************************************************************/
package test.spiderlink.message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;

/**
 * 2004. 9. 1. 이종원
 * 설명 :
 * <pre>
 * eai 소켓 통신을 담당한다.
 * </pre> 
 */
public class EAIConnector {
    public Socket socket = null;

    public DataInputStream dis = null;

    public DataOutputStream dos = null;

    String logConfig;

    boolean isClosed;

    Exception monitorException;

    /**
     * 인자로 받은 SOCKET을 이용하여  in, out stream을 연다.
     * Listener에서 전문 수신시에 사용하는 방식이다.
     */
    public EAIConnector(Socket socket) throws SysException {
        this(socket, "KEB_EAI_RECV");
    }

    /**
     * 인자로 받은 SOCKET을 이용하여  in, out stream을 연다.
     * Listener에서 전문 수신시에 사용하는 방식이다.
     * @param socket
     * @param logConfig
     */
    public EAIConnector(Socket socket, String logConfig) {
        this.logConfig = logConfig;
        this.socket = socket;
        openStream();
        monitorException = new Exception();
    }

    /**
     * SOCKET과 in, out stream을 연다.
     * cls에서 전문 전송시에 사용하는 방식이다.
     */
    public EAIConnector(String ip, int port) throws SysException {
        this.logConfig = "EAI_SEND";
        socket = connect(ip, port);
        openStream();
        monitorException = new Exception();
    }

    /**
     * 
     * 2004. 8. 31. 이종원 작성
     * @param data
     * @return String : 응답 받은 데이타
     * 설명: eai로 요청 전문을 보내고 리턴을 받아 낸다.
     * @throws Exception 
     */
    public EAIMessage sendRequest(EAIMessage msg) throws Exception {
        this.socket.setSoTimeout(msg.getField30() * 1000);
        byte[] data = msg.marshall();
        LogManager.info(logConfig, "EAI 전송전문[" + msg + "]");
        writeFully(data);
        byte[] recv = readEaiData();
        EAIMessage recvMsg = new EAIMessage();
        recvMsg.unmarshall(recv);
        LogManager.info(logConfig, "EAI 응답수신[" + recvMsg + "]");
        return recvMsg;
    }

    /**
     * 2004. 8. 31. 이종원 작성
     * @return
     * 설명: 처음 10byte를 읽어 전체 길이를 알아 낸후 그 길이 만큼 읽어 온다.
     * 맨 끝에 0x1f가 있는지 화면에 출력도 해 준다.
     */
    public byte[] readEaiData() throws SocketTimeoutException, EOFException {

        byte[] buf = null;
        String message = null;
        try {
            byte[] lenBuf = new byte [10];
            readFully(lenBuf);
            String lenStr = new String(lenBuf);
            int len = Integer.parseInt(lenStr);

            if (len > 0) {
                buf = new byte [len];
                System.arraycopy(lenBuf, 0, buf, 0, 10);

                dis.readFully(buf, 10, len - 10);

                message = new String(buf, 0, buf.length);

                LogManager.info(logConfig, "수신전문[" + buf.length + "]byte\n["
                        + new String(buf) + "]");
                return buf;
            } else {
                throw new SysException("EAI 데이타 읽기 실패:읽을 데이타가 없습니다.");
            }
        } catch (EOFException e) {            
            throw e;
        } catch (SocketTimeoutException e) {
            throw e;
        } catch (Exception e) {
            LogManager.error(logConfig, e.getMessage(), e);
            throw new SysException("EAI 데이타 읽기 실패" + e.getMessage());
        }
        //return message.getBytes();
    }

    /**	
     public byte[] mergeData(byte[] first, byte[] second){
     if(first==null && second == null){
     return new byte[0];
     }
     if(first == null){
     return second;
     }
     if(second == null){
     return first;
     }

     byte[] merged = new byte[first.length+ second.length];
     System.arraycopy(first,0,merged,0,first.length);
     System.arraycopy(second,0,merged,first.length,second.length);
     return merged;
     }
     */
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
     * 2004. 8. 31. 이종원 작성
     * @return
     * 설명: INPUT STREAM을 연다.
     */
    public DataInputStream openInputStream() {
        if (socket == null || socket.isClosed()) {
            throw new SysException("socket not opened...");
        }
        try {
            dis = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            LogManager.error(e.getMessage(), e);
            throw new SysException("FAIL TO OPEN INPUT STREAM");
        }
        return dis;
    }

    /**
     * 
     * 2004. 8. 31. 이종원 작성
     * @return
     * 설명: OUTPUT STREAM을 연다.
     */
    public DataOutputStream openOutputStream() {
        if (socket == null || socket.isClosed()) {
            throw new SysException("socket not opened...");
        }
        try {
            dos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            LogManager.error(e.getMessage(), e);
            throw new SysException("FAIL TO OPEN OUTPUT STREAM");
        }
        return dos;
    }

    /**
     * 
     * 2004. 8. 31. 이종원 작성
     * @param ip
     * @param port
     * @return
     * @throws SysException
     * 설명: SOCKET을 연다.
     */
    public Socket connect(String ip, int port) throws SysException {
        Socket socket;
        try {
            socket = new Socket(ip, port);
        } catch (UnknownHostException e) {
            LogManager.error("알수없는 호스트:" + e.getMessage(), e);
            throw new SysException("알수 없는 호스트 :" + ip + ":" + port);
        } catch (IOException e) {
            LogManager.error(e.getMessage(), e);
            throw new SysException("소켓을 열지 못하였습니다:" + ip + ":" + port);
        }
        return socket;
    }

    /**
     * 
     * 2004. 8. 31. 이종원 작성
     * @param bSend
     * @return
     * @throws SysException
     * 설명: 해당 바이트의 데이터를 전송한다.
     */
    public int writeFully(byte[] data) throws SysException {
        if (dos == null) {
            throw new SysException("OUTPUT STREAM이 열려 있지 않습니다.");
        }
        try {
            LogManager.info(logConfig, "송신전문[" + data.length + "]byte\n["
                    + new String(data) + "]");
            dos.write(data, 0, data.length);
            dos.flush();
        } catch (Exception e) {
            throw new SysException(e.getMessage());
        }
        return data.length;
    }

    /**
     * 
     * 2004. 8. 31. 이종원 작성
     * @param buf
     * @return
     * @throws SysException
     * 설명: 해당 바이트에 데이터를 읽어 들인다.
     */
    public byte[] readFully(byte[] buf) throws IOException {
        if (dis == null) {
            throw new SysException("INPUT STREAM이 열려 있지 않습니다.");
        }
        try {
            dis.readFully(buf);
        } catch (IOException e) {
            LogManager.error(e.getMessage(), e);
            throw e; 
        }
        return buf;
    }

    /**
     * 
     * 2004. 8. 31. 이종원 작성
     * @param buf
     * @return
     * @throws SysException
     * 설명: 해당 길이 만큼의 데이터를 읽어 들인다.
     */
    public byte[] readFully(int len) throws IOException {
        byte[] buf = new byte [len];
        return readFully(buf);
    }

    /**
     * 데이타 길이를(writeShort) 전송하고 데이타를 전송 한다
     */
    public int writeShort(byte[] bSend) throws SysException {
        if (dos == null) {
            throw new SysException("OUTPUT STREAM이 열려 있지 않습니다.");
        }
        try {
            dos.writeShort((short) bSend.length);
            dos.write(bSend, 0, bSend.length);
            dos.flush();
        } catch (Exception e) {
            throw new SysException(e.getMessage());
        }
        return bSend.length + 2;
    }

    /**
     * 먼저 데이타 길이를 읽고 후에 데이타를 받아 온다.
     */
    public byte[] readShort() throws SysException {
        try {
            short len = dis.readShort();
            byte[] buf = new byte [len];
            return readFully(buf);
        } catch (Exception e) {
            throw new SysException(e.getMessage());
        }
    }

    /**
     * 타임아웃을 설정한다.
     */
    public void setTimeout(int nSec) {
        try {
            socket.setSoTimeout(nSec);
        } catch (Exception e) {
            throw new SysException("setTimeout error:" + e.getMessage());
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
            if (socket != null) {
                socket.close();
            }
        } catch (Exception e) {
        }
        dis = null;
        dos = null;
        socket = null;
        isClosed = true;

    }

    public void finalize() {
        if (!isClosed) {
            LogManager.error("ERROR", "SOCKET을 닫지 않은 프로그램이 있습니다.",
                    monitorException);
            close();
        }
    }

}