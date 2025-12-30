package nebsoa.spiderlink.connector.io.writer.impl;

import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.SocketTimeoutException;

import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;
import nebsoa.spiderlink.connector.io.writer.TcpMessageWriter;
/*******************************************************************
 * <pre>
 * 1.설명 
 * 인자로 받은 전문을 단순  전송 한다. 
 * 2.사용법
 * SimpleMessageWriter writer =  new SimpleMessageWriterWriter();
 * writer.setStream(stream);
 * writer.writeMessage(data);
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
 * $Log: SimpleMessageWriter.java,v $
 * Revision 1.1  2018/01/15 03:39:51  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:01  cvs
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
 * Revision 1.1  2008/01/22 05:58:35  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:18  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2006/10/03 09:12:44  이종원
 * refactoring
 *
 * Revision 1.2  2006/10/03 00:36:02  이종원
 * flush추가
 *
 * Revision 1.1  2006/09/18 08:30:25  이종원
 * 최초작성
 *
 * </pre>
 ******************************************************************/
public class SimpleMessageWriter extends TcpMessageWriter {

    public SimpleMessageWriter(DataOutputStream out) throws IOException{
        super(out);
    }
    
    public SimpleMessageWriter(){

    }
    
   
    /**
     * 인자로 받은 내용을 단순 전송 한다. 
     * 2006. 9. 18. 이종원 작성
     */
    public void writeMessage(byte[] data) throws IOException {
        if(data == null || data.length==0){
            throw new SysException("FRM91113","data is null(No data to send)");
        }
        if(dataOutputStream == null){
            throw new EOFException("DataOutputStrea is null ");
        }
        try{
            dataOutputStream.write(data,0,data.length);
            dataOutputStream.flush();
        }catch(SocketTimeoutException e){
            LogManager.error(e.toString(),e);
            throw e;
        }catch(EOFException e){
            LogManager.error("스트림의 끝을 만났습니다:"+e.toString(),e);
            close();
            throw e;
        }catch(IOException e){
            close();
            throw e;
        }catch(RuntimeException e){
            close();
            throw e;
        }
        
    }
}
