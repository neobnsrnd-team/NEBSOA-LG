package nebsoa.spiderlink.connector.io.writer.impl;

import java.io.DataOutputStream;
import java.io.IOException;
/*******************************************************************
 * <pre>
 * 1.설명 
 * 전문 길이를 파악 한 후 그 길이 값을 4byte int 값으로 write한하
 * 전문을 전송 한다. 
 * 전문길이에 앞에 write한 길이는 포함 되지 않는다.
 * 만약 앞에 보내는 4byte도 전문 길이에 포함 하고자 한다면
 * setAddLength(true);를 호출하면 된다.
 * 즉 전문이 data부가 100 byte 인데 총 전문길이 4byte포함하면 104 byte이므로
 * 전문 길이를 104 byte라고 알려 주어야 할 경우에는 위의 메소드를 사용한다.
 * 2.사용법
 * IntLengthDataWriter writer =  new IntLengthDataWriter();
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
 * $Log: IntLengthMessageWriter.java,v $
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
 * Revision 1.1  2006/09/18 08:30:25  이종원
 * 최초작성
 *
 * </pre>
 ******************************************************************/
public class IntLengthMessageWriter extends ShortLengthMessageWriter {

    public IntLengthMessageWriter(DataOutputStream out) throws IOException{
        super(out);
    }
    
    public IntLengthMessageWriter(){

    }
    
    /**
     * return 4 : 전문 길이를 표현하는 부분의 길이수를 리턴. int 이므로 4byte
     * 2006. 9. 18.  이종원작성
     * @return
     */
    public int getDataTypeLength() {
        return 4;
    }

}
