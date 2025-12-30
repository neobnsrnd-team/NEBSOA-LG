package nebsoa.spiderlink.connector.io.reader;

import java.io.IOException;

import nebsoa.common.log.LogManager;
import nebsoa.spiderlink.exception.ConnectException;
/*******************************************************************
 * <pre>
 * 1.설명
 * socket pooling을 위하여 아무런 로직이 없이 연결 상대방이 닫히는지만
 * 모니터링 하는 용도의 AsyncReader 클래스
 * NO_ACK의 pooling인 경우 등에서 AysncReader로 사용한다.
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
 * $Log: DefaultAsyncMessageReader.java,v $
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
 * Revision 1.1.2.1  2009/08/13 08:39:03  jglee
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public class DefaultAsyncMessageReader extends AsyncMessageReader {

    public DefaultAsyncMessageReader() {
    }

    public void handleInputMessage() throws IOException {

    	if (dataInputStream == null) {
            throw new ConnectException("INPUT STREAM이 열려 있지 않습니다.");
        }

    	int data = dataInputStream.read();
		LogManager.info("MONITOR", worker+"예상치 못한 READ DATA:"+((char)data));

    	while(!worker.isStopped()){
    		data = dataInputStream.read();
    		LogManager.info("MONITOR", worker+"예상치 못한 READ DATA:"+((char)data));
    		try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
}
