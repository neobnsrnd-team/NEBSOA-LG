package nebsoa.spiderlink.connector.io.reader;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;
import nebsoa.spiderlink.exception.ConnectException;
/*******************************************************************
 * <pre>
 * 1.설명
 * socket pooling을 위하여 아무런 로직이 없이 연결 상대방이 닫히는지만
 * 모니터링 하는 용도의 AsyncReader 클래스
 * 성능이 좋지 않으므로 데이터 량이 많은 곳에서는 사용하지 않도록 한다..
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
 * $Log: PipedAsyncMessageReader.java,v $
 * Revision 1.1  2018/01/15 03:39:51  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:59  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2009/12/30 02:02:00  jglee
 * package refactoring
 *
 * Revision 1.1.2.2  2009/08/13 14:08:16  jwlee
 * import 문장에 keb.fwk.등이 있어 수정함.
 *
 * Revision 1.1.2.1  2009/08/13 08:39:13  jglee
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public class PipedAsyncMessageReader extends AsyncMessageReader {
	PipedOutputStream pos;
    public PipedAsyncMessageReader() {
    }


    /**
     * 바꿔치기 한다.
     */
    public void setDataInputStream(DataInputStream in) {
        if(dataInputStream != null){
            throw new SysException("Stream is asigned.. already..");
        }

        this.dataInputStream = (DataInputStream) in;


        pos=new PipedOutputStream();

        // 2008-8-27 spiderFramework merge start
		   // TODO keb에 문제없이 돌아가는지 확인 필요.
        PipedInputStream pis;
		try {
			pis = new SpiderPipedInputStream(pos);
		} catch (IOException e) {
			LogManager.error(e.toString(),e);
			throw new SysException(e);
		}
        // 2008-8-27 spiderFramework merge end

        DataInputStream fake = new DataInputStream(pis);
        worker.setDataInputStream(fake);

        LogManager.debug("Socket InputStream 바꾸기 성공");

    }

    public void handleInputMessage() throws IOException {

    	if (dataInputStream == null) {
            throw new ConnectException("INPUT STREAM이 열려 있지 않습니다.");
        }


        byte[] buf = new byte[1024*5];
        int len=-1;
        try{
	        while(!worker.isStopped()){
	            len=dataInputStream.read(buf);
	            if(len != -1){
	                pos.write(buf,0,len);
	                //LogManager.debug("PoolableSocketReader Read:"+new String(buf,0,len));
	            }else{
	                LogManager.infoLP("READ EOF.... SO STOP READER");
	                worker.stop();
	                throw new EOFException("end of stream");
	            }
	        }
        }finally{
        	closePipedStream();
        }
    }

    // 2008-8-27 spiderFramework merge start
	// TODO keb에 문제없이 돌아가는지 확인 필요.
   	/**
   	 * 버퍼 용량이 큰 Pipe stream이다. jdk 기본 1024 byte를 socket 기본 버퍼의 2배(16K)로 교체
   	 */
   	static class SpiderPipedInputStream extends PipedInputStream{

   		public SpiderPipedInputStream(){

   		}

   		public SpiderPipedInputStream(PipedOutputStream source) throws IOException{
   			super(source);
   			// buffer가 큰 놈으로 교체
   			super.buffer = new byte[1024*16];
   		}
   	}

   	private void closePipedStream() {
        if(dataInputStream!= null){
            LogManager.fwkDebug("close Original input Stream ..."+this);
            try {
            	dataInputStream.close();
            } catch (Exception e) {

            }finally{
            	dataInputStream=null;
            }
        }
        if(pos != null){
            LogManager.fwkDebug("close Pipe Stream ...");
            try {
                pos.flush();
            } catch (Exception e) {

            }
            try {
                pos.close();
            } catch (Exception e) {

            }finally{
                pos=null;
            }
        }
        worker.stop();
   }
}

