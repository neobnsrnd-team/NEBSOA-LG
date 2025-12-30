/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
package nebsoa.spiderlink.connector.io.reader;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;

/*******************************************************************
 * <pre>
 * 1.설명 
 * TCP 전문을 수신하는 클래스를 만들 때 기본이 되는 클래스.
 * 
 * 2.사용법
 * 생성자 참조.
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author $Author: cvs $
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 
 * $Log: TcpMessageReader.java,v $
 * Revision 1.1  2018/01/15 03:39:51  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:59  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:24  김성균
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
 * Revision 1.1  2007/11/26 08:39:09  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2006/10/03 09:12:44  이종원
 * refactoring
 *
 * Revision 1.6  2006/09/22 01:41:30  이종원
 * update constructor
 *
 * Revision 1.5  2006/09/18 08:30:50  이종원
 * stream받는 생성자 추가
 *
 * Revision 1.4  2006/08/01 14:00:27  이종원
 * *** empty log message ***
 *
 * Revision 1.3  2006/08/01 10:04:12  이종원
 * *** empty log message ***
 *
 * Revision 1.2  2006/08/01 04:26:32  이종원
 * 단순정리
 *
 * Revision 1.1  2006/07/31 11:17:45  이종원
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public abstract class TcpMessageReader 
            implements MessageReader{
    
    protected Socket socket;
    protected String logConfig;
    protected DataInputStream dataInputStream;
    
    private Exception monitor;
    
    protected TcpMessageReader(Socket socket,String logConfig) throws IOException{
        this.socket =socket;
        this.logConfig=logConfig;
        init();
    }
    
    protected TcpMessageReader(DataInputStream in ,String logConfig) throws IOException{
        setup(in, logConfig);
    }
    
   
    private void setup(DataInputStream in ,String logConfig) throws IOException {
        this.logConfig=logConfig;
        if(in == null ) throw new IOException("InputStream is null");
        this.dataInputStream = in;
        monitor=new Exception();        
    }

    protected TcpMessageReader(DataInputStream in ) throws IOException{
        this(in, "DEBUG");
    }
    
    protected TcpMessageReader(InputStream in ,String logConfig) throws IOException{
        if(in instanceof DataInputStream){
            setup((DataInputStream)in, logConfig);
        }else{
            DataInputStream din = new DataInputStream(in);
            setup(din, logConfig);
        }
    }
    
   
    protected TcpMessageReader(InputStream in ) throws IOException{
        this(in, "DEBUG");
    }
    /** 
     * default constructor: do nothing
     */
    protected TcpMessageReader(){
        
    }
    public void init() {
        try {
            if (dataInputStream == null){
                if(socket == null || socket.isClosed()){
                    throw new SysException("FRS911112","Socket is not valid");
                }
                this.dataInputStream = new DataInputStream(socket.getInputStream());
                monitor=new Exception();
            }
            LogManager.debug(logConfig,"opened Input stream@"+this);
        } catch (IOException e) {
            LogManager.error(e.getMessage(),e);
            throw new SysException(e);
        }
    }
    
    
    public void close(){
        if(dataInputStream != null){
            try {
                dataInputStream.close();
                LogManager.debug(logConfig,"close Input stream@"+this);
                dataInputStream=null;
            } catch (IOException e) {
                
            }
        }
    }
    
    public void finalize(){
        if(dataInputStream != null){
            LogManager.error(logConfig,"닫지 않은 inputStream이 있습니다.",monitor);
        }
        close();
    }
    /**
     * 상속 받는 클래스에서 구현 할 메소드 
     * 2006. 9. 18. 이종원 작성
     * @see nebsoa.spiderlink.connector.io.reader.MessageReader#readMessage() 재정의
     */
    public abstract byte[] readMessage() throws IOException;

    public String getLogConfig() {
        return logConfig;
    }

    public void setLogConfig(String logConfig) {
        this.logConfig = logConfig;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public DataInputStream getDataInputStream() {
        return dataInputStream;
    }

    public void setDataInputStream(DataInputStream dataInputStream) {
        this.dataInputStream = dataInputStream;
    }
}
