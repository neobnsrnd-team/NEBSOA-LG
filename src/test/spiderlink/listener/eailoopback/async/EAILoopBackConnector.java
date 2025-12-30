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
 * $Id: EAILoopBackConnector.java,v 1.1 2018/01/15 03:39:50 cvs Exp $
 * $Log: EAILoopBackConnector.java,v $
 * Revision 1.1  2018/01/15 03:39:50  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:45  cvs
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
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.2  2008/02/20 00:42:48  오재훈
 * *** empty log message ***
 *
 * Revision 1.1  2007/11/26 08:39:09  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2006/10/03 09:12:44  이종원
 * refactoring
 *
 * Revision 1.1  2006/06/29 13:28:44  이종원
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
package test.spiderlink.listener.eailoopback.async;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import nebsoa.common.exception.SysException;

/**
 * 2004. 9. 1. 이종원
 * 설명 :
 * <pre>
 * eai 소켓 통신을 담당한다.
 * </pre> 
 */
public class EAILoopBackConnector {
    public Socket sourceSocket;
    public Socket targetSocket = null;
    
    public DataInputStream srcdis = null;
    public DataOutputStream srcdos = null;

    public DataInputStream dis = null;
    public DataOutputStream dos = null;

    String logConfig;

    boolean isClosed;

    Exception monitorException;




    /**
     * 인자로 받은 소켓으로 부터 읽어서 새로운 소켓으로 내보낸다. 
     * SOCKET과 in, out stream을 연다.
     * cls에서 전문 전송시에 사용하는 방식이다.
     */
    public EAILoopBackConnector(Socket socket,String ip, int port,String logConfig) throws SysException {
        sourceSocket=socket;
        
        this.logConfig = logConfig;
        System.out.println("open target socket...");
        targetSocket = connect(ip, port);
        System.out.println("open source and target stream...");
        openStream();
        System.out.println("open source and target stream...ok");
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

                srcdis.readFully(buf, 10, len - 10);


                System.out.println( "수신전문[" + buf.length + "]byte\n["
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
            e.printStackTrace();
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
        if (srcdis == null) {
            srcdis = openSrcInputStream();
        }
        if (srcdos == null) {
            srcdos = openSrcOutputStream();
        }
        
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
        if (targetSocket == null || targetSocket.isClosed()) {
            throw new SysException("socket not opened...");
        }
        try {
            dis = new DataInputStream(targetSocket.getInputStream());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new SysException("FAIL TO OPEN INPUT STREAM");
        }
        return dis;
    }
    
    public DataInputStream openSrcInputStream() {
        if (sourceSocket == null || sourceSocket.isClosed()) {
            throw new SysException("socket not opened...");
        }
        try {
            srcdis = new DataInputStream(sourceSocket.getInputStream());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new SysException("FAIL TO OPEN INPUT STREAM");
        }
        return srcdis;
    }

    /**
     * 
     * 2004. 8. 31. 이종원 작성
     * @return
     * 설명: OUTPUT STREAM을 연다.
     */
    public DataOutputStream openOutputStream() {
        if (targetSocket == null || targetSocket.isClosed()) {
            throw new SysException("socket not opened...");
        }
        try {
            dos = new DataOutputStream(targetSocket.getOutputStream());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new SysException("FAIL TO OPEN OUTPUT STREAM");
        }
        return dos;
    }
    
    public DataOutputStream openSrcOutputStream() {
        if (sourceSocket == null || sourceSocket.isClosed()) {
            throw new SysException("socket not opened...");
        }
        try {
            srcdos = new DataOutputStream(sourceSocket.getOutputStream());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new SysException("FAIL TO OPEN OUTPUT STREAM");
        }
        return srcdos;
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
        System.out.println("--------------- try to connect -------------");
        Socket socket;
        try {
            socket = new Socket(ip, port);
        } catch (UnknownHostException e) {
            System.out.println("알수없는 호스트:" + e.getMessage());
            throw new SysException("알수 없는 호스트 :" + ip + ":" + port);
        } catch (IOException e) {
            System.out.println(e.getMessage());
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
            System.out.println( "송신전문[" + data.length + "]byte\n["
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
        if (srcdis == null) {
            throw new SysException("INPUT STREAM이 열려 있지 않습니다.");
        }
        try {
            srcdis.readFully(buf);
        } catch (IOException e) {
            System.out.println(e.getMessage());
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
            targetSocket.setSoTimeout(nSec);
        } catch (Exception e) {
            throw new SysException("setTimeout error:" + e.getMessage());
        }
    }

    public void close() {
        try {
            if (srcdis != null) {
                srcdis.close();
            }
        } catch (Exception e) {
        }

        try {
            if (srcdos != null) {
                srcdos.close();

            }
        } catch (Exception e) {
        }
        try {
            if (sourceSocket != null) {
                sourceSocket.close();
            }
        } catch (Exception e) {
        }
        srcdis = null;
        srcdos = null;
        sourceSocket = null;
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
            if (targetSocket != null) {
                targetSocket.close();
            }
        } catch (Exception e) {
        }
        dis = null;
        dos = null;
        targetSocket = null;
        isClosed = true;

    }

    public void finalize() {
        if (!isClosed) {
            System.out.println( "SOCKET을 닫지 않은 프로그램이 있습니다.");
            close();
        }
    }

    public void loopBack2() throws IOException {
        int data = -1;
        System.out.print("전문 수신[");
        try {
            while((data=dis.read()) != -1){
                System.out.print((char)data);
                dos.write(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        System.out.print("]");        
    }
    
    public void loopBack() throws IOException {
        byte[] recv=readEaiData();
        writeFully(recv);
    }
    
    public static void main(String args[])throws Exception{
        Socket s = new Socket ("localhost",18102);
        DataInputStream in = new DataInputStream(s.getInputStream());
        DataOutputStream out = new DataOutputStream(s.getOutputStream());
        String test="0000000090";
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
                test = test+i;
            }
        }
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