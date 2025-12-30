/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
package nebsoa.spiderlink.connector.io.writer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;

/*******************************************************************
 * <pre>
 * 1.설명 
 * TCP 전문을 송신하는 클래스를 만들 때 기본이 되는 클래스.
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
 * $Log: TcpMessageWriter.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:34  cvs
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
 * Revision 1.1  2007/11/26 08:39:14  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2006/10/03 09:12:44  이종원
 * refactoring
 *
 * Revision 1.4  2006/09/18 08:30:50  이종원
 * stream받는 생성자 추가
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
public abstract class TcpMessageWriter implements MessageWriter{
    Exception monitor;
    protected Socket socket;
    protected String logConfig;
    protected DataOutputStream dataOutputStream;
    /**
     * create new DataOutputStream
     * @param socket
     * @param logConfig
     * @throws IOException
     */
    protected TcpMessageWriter(Socket socket,String logConfig) throws IOException{
        monitor = new Exception("닫지 않은 스트림이 있습니다.");
        this.socket =socket;
        this.logConfig=logConfig;
        init();
    }
    /**
     * do not create dataoutputstream(do not call init method).
     * use received stream
     * @param DataOutputStream 
     * @param logConfig
     * @throws IOException
     */
    protected TcpMessageWriter(DataOutputStream out,String logConfig) throws IOException{
        monitor = new Exception("닫지 않은 스트림이 있습니다.");
        this.dataOutputStream =out;
        this.logConfig=logConfig;
    }
    
    /**
     * do not create dataoutputstream(do not call init method).
     * use received stream
     * @param DataOutputStream 
     * @throws IOException
     */
    protected TcpMessageWriter(DataOutputStream out) throws IOException{
        this(out,"DEBUG");
    }
    
    /**
     * no stream on create time. You must set the stream later...).
     */
    protected TcpMessageWriter(){}
    
    public void init() {
        try {
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            LogManager.error(e.getMessage(),e);
            throw new SysException((Throwable)e);
        }
        LogManager.debug(logConfig,"opened Output stream");
    }
    
    
    public void close(){
        if(dataOutputStream != null){
            try {
                dataOutputStream.close();
                dataOutputStream=null;
            } catch (IOException e) {
                
            }
        }
    }
    
    public void finalize(){
        if(dataOutputStream != null){
            LogManager.error(logConfig, monitor.toString(),monitor);
        }
        close();
    }
    
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

    public DataOutputStream getDataOutputStream() {
        return dataOutputStream;
    }

    public void setDataOutputStream(DataOutputStream dataOutputStream) {
        this.dataOutputStream = dataOutputStream;
    }
}
