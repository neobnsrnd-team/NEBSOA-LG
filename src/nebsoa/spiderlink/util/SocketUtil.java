/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;

/*******************************************************************
 * <pre>
 * 1.설명 
 * Socket 유틸리티
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
 * $Log: SocketUtil.java,v $
 * Revision 1.1  2018/01/15 03:39:52  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:09  cvs
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
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:33  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:39:08  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2006/07/05 07:17:44  김승희
 * logConfig 수정
 *
 * Revision 1.1  2006/06/17 10:22:59  김승희
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public class SocketUtil {
	public Socket socket = null;
	public DataInputStream dis = null;
	public DataOutputStream dos = null;
    int timeout;
	
	//String logConfig= "MESSAGE";
    String logConfig= "DEBUG";
    
	boolean isClosed;
	Exception monitorException;
	
	/**
	 * 인자로 받은 SOCKET을 이용하여  inputStream, outputStream stream을 연다.
	 * Listener에서 전문 수신시에 사용하는 방식이다.
	 */
	public SocketUtil(Socket socket) throws SysException {
		this(socket,null);
	}
	/**
	 * 인자로 받은 SOCKET을 이용하여  inputStream, outputStream stream을 연다.
	 * Listener에서 전문 수신시에 사용하는 방식이다.
	 * @param socket
	 * @param logConfig
	 */
	public SocketUtil(Socket socket, String logConfig) {
		if(logConfig != null) this.logConfig = logConfig;
		this.socket =socket;
		openStream();
		monitorException =  new Exception();
	}
	
	/**
	 * SOCKET과 inputStream, outputStream stream을 연다.
	 * 
	 */
	public SocketUtil(String ip, int port) throws SysException {
		this(ip,port,null);
	}
    
    /**
     * SOCKET과 inputStream, outputStream stream을 연다.
     * 지정된 timeout을 세팅 한다.
     */
    public SocketUtil(String ip, int port, int timeout) throws SysException {
        this(ip,port,timeout, null);
    }
	
    /**
     * SOCKET과 inputStream, outputStream stream을 연다.
     * 지정된 logConfig를 사용한다.
     */
    public SocketUtil(String ip, int port, String logConfig) throws SysException {
        this(ip,port,0, logConfig);
    }
    
    /**
	 * SOCKET과 inputStream, outputStream stream을 연다.
	 * cls에서 전문 전송시에 사용하는 방식이다.
	 */
	public SocketUtil(String ip, int port,int timeout, String logConfig) throws SysException {
		if(logConfig != null) this.logConfig = logConfig;
		socket =connect(ip,port, timeout);
		openStream();
		monitorException =  new Exception();
	}	


	/**
	 * 2004. 8. 31. 이종원 작성
	 * @return
	 * 설명: 처음 10byte를 읽어 전체 길이를 알아 낸후 그 길이 만큼 읽어 온다.
	 * 맨 끝에 0x1f가 있는지 화면에 출력도 해 준다.
	 */
	public byte[] pushBackReadData(int startIndex,int _len) {
		
		byte[] buf = null;
		
		try {
			byte[] lenBuf =new byte[_len];			
			readFully(startIndex+_len);
			String lenStr = new String(lenBuf);
			int len=0;
			if(startIndex==0){			
				len = Integer.parseInt(lenStr);
			}else{
				len = Integer.parseInt(lenStr.substring(startIndex));
			}
			
			if (len > 0) {
				buf = new byte[len];
				System.arraycopy(lenBuf,0,buf,0, startIndex+_len);

				dis.readFully(buf,startIndex+_len,len-(startIndex+_len));
				
                LogManager.debug(logConfig,"읽은 내용["+buf.length+"]byte\n["+new String(buf)+"]");

				return buf;
			}else{
				throw new SysException("데이타 읽기 실패 :읽을 데이타가 없습니다.");
			}
		}catch(Exception e){
			LogManager.error(logConfig,e.getMessage(),e);
			throw new SysException("데이타 읽기 실패"+e.getMessage());
		}
		//return message.getBytes();
	}

	/**
	 * 
	 * 2004. 8. 31. 이종원 작성
	 * 
	 * 설명: input , output stream을 모두 연다.
	 */
	public void openStream(){
		if(dis == null){
			dis = openInputStream();
		}
		if(dos == null){
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
		if(socket == null || socket.isClosed() ){
			throw new SysException("socket not opened...");
		}
		try {
			dis = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			LogManager.error(e.getMessage(),e);
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
	public DataOutputStream openOutputStream(){
		if(socket == null || socket.isClosed() ){
			throw new SysException("socket not opened...");
		}
		try {
			dos = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			LogManager.error(e.getMessage(),e);
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
	public Socket connect(String ip, int port, int timeout) throws SysException{	
		Socket socket;
		try {
			socket = new Socket(ip,port);
            socket.setKeepAlive(true);
            socket.setSoTimeout(timeout);
		} catch (UnknownHostException e) {
			LogManager.error("알수없는 호스트:"+e.getMessage(),e);
			throw new SysException("알수 없는 호스트 :"+ip+":"+port);
		} catch (IOException e) {
			LogManager.error(e.getMessage(),e);
			throw new SysException("소켓을 열지 못하였습니다:"+ip+":"+port);
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
		if(dos==null){
			throw new SysException("OUTPUT STREAM이 열려 있지 않습니다.");
		}
		try {
			LogManager.info(logConfig,"송신전문["+data.length+"]byte\n["+new String(data)+"]");
			dos.write(data, 0, data.length);
			dos.flush();
		}catch (Exception e) {
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
	public byte[] readFully(byte[] buf) throws SysException {
		if(dis==null){
			throw new SysException("INPUT STREAM이 열려 있지 않습니다.");
		}
		try {
			dis.readFully(buf);
		} catch (IOException e) {
			LogManager.error(e.getMessage(),e);
			throw new SysException(e.getMessage());
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
	public byte[] readFully(int len) throws SysException {
		byte[] buf = new byte[len];
		return readFully(buf);
	}
	
	/**
	 * 데이타 길이를(writeShort) 전송하고 데이타를 전송 한다
	 */
	public int writeShort(byte[] bSend) throws SysException {
		if(dos==null){
			throw new SysException("OUTPUT STREAM이 열려 있지 않습니다.");
		}
		try {
			dos.writeShort( (short) bSend.length);
			dos.write(bSend, 0, bSend.length);
			dos.flush();
		}
		catch (Exception e) {
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
			byte[] buf = new byte[len];
			return readFully(buf);
		}
		catch (Exception e) {
			throw new SysException(e.getMessage());
		}
	}
	/**
	 * 데이타 길이를(writeShort) 전송하고 데이타를 전송 한다
	 */
	public int writeInt(byte[] bSend) throws SysException {
		if(dos==null){
			throw new SysException("OUTPUT STREAM이 열려 있지 않습니다.");
		}
		try {
			dos.writeInt( bSend.length);
			dos.write(bSend, 0, bSend.length);
			dos.flush();
		}
		catch (Exception e) {
			throw new SysException(e.getMessage());
		}
		return bSend.length + 4;
	}


	/**
	 * 먼저 데이타 길이를 읽고 후에 데이타를 받아 온다.
	 */
	public byte[] readInt() throws SysException {
		try {
			int len = dis.readInt();
			byte[] buf = new byte[len];
			return readFully(buf);
		}
		catch (Exception e) {
			throw new SysException(e.getMessage());
		}
	}
	/**
	 * 타임아웃을 설정한다.
	 */
	public void setTimeout(int nSec) {
		try {
			socket.setSoTimeout(nSec);
		}
		catch (Exception e) {
			throw new SysException("setTimeout error:"+e.getMessage());
		}
	}

	public void close() {
		try {
			if (dis != null) {
				dis.close();
			}
		}catch (Exception e) {
		}

		try {
			if (dos != null) {
				dos.close();
				
			}
		}catch (Exception e) {
		}

		try {
			if (socket != null) {
				socket.close();
			}
		}catch (Exception e) {
		}
		dis = null;
		dos = null;
		socket = null;
		isClosed = true;
		
	}
	

	public void finalize() {
		if(!isClosed){
			LogManager.error("ERROR","SOCKET을 닫지 않은 프로그램이 있습니다.",monitorException);
			close();
		}
	}
	
}
