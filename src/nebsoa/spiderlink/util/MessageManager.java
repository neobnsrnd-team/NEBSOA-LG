package nebsoa.spiderlink.util;

import nebsoa.common.Constants;
import nebsoa.common.util.BizDayCheckUtil;
import nebsoa.spiderlink.exception.MessageException;


/*******************************************************************
 * <pre>
 * 1.설명 
 * trxId 를 인자로 해당 거래의 정보를 찾아 오는 유틸리티.
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
 * $Log: MessageManager.java,v $
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
 * Revision 1.1  2008/09/19 07:02:34  youngseokkim
 * 거래제한관련 클래스 추가
 *
 * Revision 1.1  2008/08/04 08:54:51  youngseokkim
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/

public class MessageManager {
	private MessageManager() {
	}
	

    /**
     * 영업일, 토요일, 공휴일별 거래 가능여부와 거래 가능시간을 체크한다.
     * @param serviceId
     */
    public static void checkTrxEnableTime(String trxId) throws MessageException {
    	BizDayCheckUtil.getInstance().isTrxEnableTime(trxId);
    }
}
