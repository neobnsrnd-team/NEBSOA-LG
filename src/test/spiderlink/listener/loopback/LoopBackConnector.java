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
 * $Id: LoopBackConnector.java,v 1.1 2018/01/15 03:39:48 cvs Exp $
 * $Log: LoopBackConnector.java,v $
 * Revision 1.1  2018/01/15 03:39:48  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:15  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:50  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:22  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:26  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.2  2008/02/20 00:42:49  오재훈
 * *** empty log message ***
 *
 * Revision 1.1  2007/11/26 08:39:14  안경아
 * *** empty log message ***
 *
 * Revision 1.11  2006/10/10 07:15:24  이종원
 * *** empty log message ***
 *
 * Revision 1.10  2006/10/03 05:05:47  이종원
 * *** empty log message ***
 *
 * Revision 1.9  2006/09/27 10:25:54  이종원
 * *** empty log message ***
 *
 * Revision 1.8  2006/09/26 07:44:21  이종원
 * *** empty log message ***
 *
 * Revision 1.7  2006/09/18 10:58:18  이종원
 * test
 *
 * Revision 1.6  2006/09/18 06:04:52  이종원
 * async 기능 추가
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
package test.spiderlink.listener.loopback;

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
public class LoopBackConnector {
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
    public LoopBackConnector(Socket socket) throws SysException {
        this(socket, "MESSAGE_TEST");
    }

    /**
     * 인자로 받은 SOCKET을 이용하여  in, out stream을 연다.
     * Listener에서 전문 수신시에 사용하는 방식이다.
     * @param socket
     * @param logConfig
     */
    public LoopBackConnector(Socket socket, String logConfig) {
        this.logConfig = logConfig;
        this.socket = socket;
        
        openStream();
        monitorException = new Exception();
    }

    /**
     * SOCKET과 in, out stream을 연다.
     * cls에서 전문 전송시에 사용하는 방식이다.
     */
    public LoopBackConnector(String ip, int port) throws SysException {
        this.logConfig = "MESSAGE_TEST";
        socket = connect(ip, port);
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
                throw new SysException("EAI 데이타 읽기 실패:읽을 데이타가 없습니다.");
            }
        } catch (EOFException e) {            
            throw e;
        } catch (SocketTimeoutException e) {
            throw e;
        } catch (Exception e) {
            System.out.println( e.getMessage());
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
            System.out.println(e.getMessage());
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
            System.out.println(e.getMessage());
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
        if (dis == null) {
            throw new SysException("INPUT STREAM이 열려 있지 않습니다.");
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

    static int i=10;
    /**
     * 데이타 길이를(writeShort) 전송하고 데이타를 전송 한다
     */
    public void writeShort(final byte[] bSend) throws SysException {
        if (dos == null) {
            System.out.println("output stream is null.");
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//            }
//            try{
//                socket.close();
//                socket=null;
//            }catch(Exception e){
//                
//            }
            return;
            //throw new SysException("OUTPUT STREAM이 열려 있지 않습니다.");
        }
       
//        if(System.currentTimeMillis() /2==0){
//            System.out.println("randem 하게 닫는다....");
//            try {
//                if (dos != null) {
//                    dos.close();
//                    dos=null;
//                }
//            } catch (Exception e) {
//            }
//
//            return;
//        }
        
//        new Thread(){
//            public void run(){
//                System.out.println("async write thread started..");
//                try {
//                    if(i==0){
//                        i=10;
//                    }
//                    int local=i--;
//                    System.out.println(i+">>try to send "+new String(bSend)
//                            +"\nsleep for ["+local+"] seconds");
//                    Thread.sleep(local);
//                    System.out.println(local+">>now send "+new String(bSend));
//                } catch (InterruptedException e1) {
//                    
//                }
//                try {
//                    dos.writeShort((short) bSend.length);
//                    dos.write(bSend, 0, bSend.length);
//                    dos.flush();
//                } catch (Exception e) {
//                    throw new SysException(e.getMessage());
//                }
//            }
//        }.start();
        
        try {
            dos.writeShort((short) bSend.length);
            dos.write(bSend, 0, bSend.length);
            dos.flush();

        } catch (IOException e) {
        }
     
    }

    /**
     * 먼저 데이타 길이를 읽고 후에 데이타를 받아 온다.
     * @throws IOException 
     */
    public byte[] readShort() throws IOException {
        try {
            short len = dis.readShort();
            byte[] buf = new byte [len];
            return readFully(buf);
        } catch (IOException e) {
            throw e;
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
            System.out.println("close socket...");
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
        byte[] recv=readShort();
        System.out.println("수신 ["+new String(recv)+"]");
        writeShort(recv);
    }
    
    public static class WriteThread extends Thread{
        
        public WriteThread(DataOutputStream out,String[]  data){
            this.out = out;
            this.data=data;
        }
        DataOutputStream out;
        String[] data;
        public void start(){
//        public void run(){
            if(out != null){
                for(int i=0;i<data.length;i++){
                    try {
                        byte[] buf = data[i].getBytes();
//                        System.out.println("try to write["+new String(data[i])+"]");
//                        sleep(1000);
                        //out.writeShort(buf.length);
                        out.write(buf);
                        out.flush();
                        System.out.println("finish write["+new String(data[i])+"]");
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }
                }
                
            }
//            try {
//                System.out.println("close ----------- out----------");
//                out.close();
//                
//            } catch (IOException e) {
//            }
        }
        
    }
    
    public static void main(String args[])throws Exception{
        Socket s = new Socket ("localhost",18102);
        s.setSoTimeout(1000);
        DataInputStream in = new DataInputStream(s.getInputStream());
        DataOutputStream out = new DataOutputStream(s.getOutputStream());
        String[] array = new String[1];
        String test="0000000900";
        for(int i=1;i<9;i++){
            for(int j=0;j<10;j++){
                test = test+i;
            }
        }
        StringBuffer testbuf = new StringBuffer(test);
        for(int i=0;i<10;i++){
            testbuf.append(test);
        }
        array[0]=testbuf.toString();
        System.out.println("test length:"+test.length());
        new WriteThread(out,array).start();
        byte[] buf = null;
        Thread.sleep(200);
//        s.getOutputStream().close();
        try{
//        for(int i=0;i<array.length;i++){
//            System.out.println("isBound:"+s.isBound()+
//                    "isClosed:"+s.isClosed()+
//                    "isConnected:"+s.isConnected()+
//                    "isInputShutdown:"+s.isInputShutdown()+
//                    "isOutputShutdown:"+s.isOutputShutdown());
//            int len = in.readShort();
//            buf = new byte[len];
//            in.readFully(buf);
//            System.out.println(new String(buf));
//        }
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            if(in != null){
                System.out.println("input stream is not null:"+in);
            }
            in.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        out.close();
        s.close();
    }

}