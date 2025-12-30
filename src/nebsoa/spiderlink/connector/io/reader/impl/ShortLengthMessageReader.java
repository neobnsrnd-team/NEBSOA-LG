package nebsoa.spiderlink.connector.io.reader.impl;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.SocketTimeoutException;

import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;
import nebsoa.spiderlink.exception.DataOverflowException;
import nebsoa.spiderlink.util.IoUtil;
/*******************************************************************
 * <pre>
 * 1.설명 
 * 최초 2byte를 읽어  전문 길이를 파악한 후 
 * 해당 길이만큼  읽어  데이터를 BYTE[]로 리턴한다.
 * 앞에 읽은 2byte는 합쳐 지지 않는다.
 * 앞에 2byte도 전문길이에 포함 되었는지 여부를 세팅 하려면
 * setDataLengthCounted(true);를 호출하면 앞에 읽은 2byte도 전문길이로 
 * 계산되어 읽어들인다.
 * 2.사용법
 * ShortLengthDataReader reader = ShortLengthDataReader();
 * reader.setStream(stream);
 * byte[] data = reader.readMessage();
 * 
 * <font color="red">
 * 3.주의사항
 * 앞에 읽은 2byte는 합쳐 지지 않는다.
 * </font>
 *
 * @author 이종원
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 
 * $Log: ShortLengthMessageReader.java,v $
 * Revision 1.1  2018/01/15 03:39:49  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:34  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:23  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:27  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:34  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:18  안경아
 * *** empty log message ***
 *
 * Revision 1.4  2007/07/30 03:07:30  김성균
 * keb package 의존성 삭제
 *
 * Revision 1.3  2006/11/17 09:55:53  이종원
 * 전문 길이 제한 로직 추가
 *
 * Revision 1.2  2006/10/10 07:19:35  이종원
 * EOF Exception의 경우 exception trace생략하도록 수정
 *
 * Revision 1.1  2006/10/03 09:12:44  이종원
 * refactoring
 *
 * Revision 1.2  2006/09/18 10:53:20  이종원
 * update
 *
 * Revision 1.1  2006/09/18 08:30:25  이종원
 * 최초작성
 *
 * </pre>
 ******************************************************************/
public class ShortLengthMessageReader extends StringLengthMessageReader {


    public ShortLengthMessageReader(DataInputStream in, String log){
        super(in,log);
        setLengthPartCounted(false);
    }
    
    public ShortLengthMessageReader(DataInputStream in){
        this(in, "DEBUG");
        setLengthPartCounted(false);
    }
    
    public ShortLengthMessageReader(){
        setLengthPartCounted(false);
    }
    
    /**
     * short type이므로 2 리턴.
     * 2006. 9. 18.  이종원 작성
     * @return
     */
    public int getDataTypeLength(){
        return 2;
    }
    /**
     * dataLength를 보고 전문 길이를 파악 한 후 해당 길이만큼 전문을 read한다. 
     * 2006. 9. 18. 이종원 작성
     * @see nebsoa.spiderlink.connector.io.reader.MessageReader#readMessage() 재정의
     */
    public byte[] readMessage() throws IOException {
        
        if(dataInputStream == null){
            throw new EOFException("DataInputStrea is null ");
        }
        try{

            short len=dataInputStream.readShort();

            byte[] buf = null; //전체 데이터.

            if (len > 0) {
                
                /// 매우 큰 전문이 왔을 경우 시스테 보호를 위해 skip ////
                if(IoUtil.isOverflowData(len)){
                    try {
                        IoUtil.skipStream(dataInputStream, len - (isLengthPartCounted()?getDataTypeLength():0));
                        throw new DataOverflowException();
                    } catch (IOException e) {
                        throw new DataOverflowException();
                    }
                }
                
                
                if(isLengthPartCounted()){
                    buf = new byte [len-getDataTypeLength()];
                }else{
                    buf = new byte [len];
                }
                dataInputStream.readFully(buf,0,len);
                return buf;
            } else {
                throw new SysException("데이타 읽기 실패:읽을 데이타가 없습니다.");
            }
        }catch(SocketTimeoutException e){
            LogManager.error(e.toString(),e);
            throw e;
        }catch(EOFException e){
            LogManager.error("스트림의 끝을 만났습니다:"+e.toString());
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
