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
 * 전문 길이를 파악 한 후 그 길이 값을 2byte short 값으로 write한 후 
 * 전문을 전송 한다. 
 * 전문길이에 앞에 write한 길이는 포함 되지 않는다.
 * 만약 앞에 보내는 2byte도 전문 길이에 포함 하고자 한다면
 * setAddLength(true);를 호출하면 된다.
 * 즉 전문이 data부가 100 byte 인데 총 전문길이 2byte포함하면 102 byte이므로
 * 전문 길이를 102 byte라고 알려 주어야 할 경우에는 위의 메소드를 사용한다.
 * 2.사용법
 * ShortLengthDataWriter writer =  new ShortLengthDataWriter();
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
 * $Log: ShortLengthMessageWriter.java,v $
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
 * Revision 1.2  2006/10/03 00:36:36  이종원
 * flush추가
 *
 * Revision 1.1  2006/09/18 08:30:25  이종원
 * 최초작성
 *
 * </pre>
 ******************************************************************/
public class ShortLengthMessageWriter extends TcpMessageWriter {

    public ShortLengthMessageWriter(DataOutputStream out) throws IOException{
        super(out);
    }
    
    public ShortLengthMessageWriter(){

    }
    
    boolean addLength=false;
    
   
    /**
     * dataLength를2byte로 write한 후  전문을  write한다. 
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

            int len=data.length;
            if(isAddLength()){
                len = len+getDataTypeLength();
            }
            if(getDataTypeLength()==2){
                dataOutputStream.writeShort((short)len);
            }else if(getDataTypeLength()==4){
                dataOutputStream.writeInt(len);
            }else{
                throw new IOException("getDataLengthPartLength() error");
            }
            dataOutputStream.write(data,0,len);
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

    public boolean isAddLength() {
        return addLength;
    }

    public void setAddLength(boolean addLength) {
        this.addLength = addLength;
    }
    
    /**
     * return 2 : 전문 길이를 표현하는 부분의 길이수를 리턴.short이므로 2byte
     * 2006. 9. 18.  이종원작성
     * @return
     */
    public int getDataTypeLength() {
        return 2;
    }

}
