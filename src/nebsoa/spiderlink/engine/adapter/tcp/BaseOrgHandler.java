/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.engine.adapter.tcp;

import nebsoa.common.util.DataMap;
import nebsoa.spiderlink.context.MessageContext;
import nebsoa.spiderlink.exception.MessageException;

/*******************************************************************
 * <pre>
 * 1.설명 
 * Host로 메시지를 송수신 받는 핸들러.
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
 * $Log: BaseOrgHandler.java,v $
 * Revision 1.1  2018/01/15 03:39:50  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:52  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:24  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:27  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:53  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:28  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:39:05  안경아
 * *** empty log message ***
 *
 * Revision 1.6  2006/10/02 18:32:34  이종원
 * BaseOrgHandler상속
 *
 * Revision 1.1  2006/10/02 14:26:57  이종원
 * *** empty log message ***
 *
 * Revision 1.4  2006/09/26 08:48:07  김승희
 * *** empty log message ***
 *
 * Revision 1.3  2006/07/28 09:30:11  김승희
 * exception 처리 변경
 *
 * Revision 1.2  2006/07/06 08:40:21  김승희
 * exception 처리 관련 수정
 *
 * Revision 1.1  2006/07/04 09:31:29  김승희
 * 패키지 변경
 *
 * Revision 1.1  2006/07/04 08:35:25  김승희
 * 패키지 변경
 *
 * Revision 1.6  2006/07/03 12:36:15  김승희
 * *** empty log message ***
 *
 * Revision 1.5  2006/06/23 04:38:41  김승희
 * *** empty log message ***
 *
 * Revision 1.4  2006/06/20 04:58:05  김승희
 * DataMap 리턴하도록 메소드 변경
 *
 * Revision 1.3  2006/06/19 13:46:17  김성균
 * *** empty log message ***
 *
 * Revision 1.2  2006/06/19 08:04:45  김승희
 * 메소드 이름 변경
 *
 * Revision 1.1  2006/06/19 07:49:09  김승희
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public abstract class BaseOrgHandler {
	
    /**
     * 특정 기관으로 전문을 송신 처리 하는 흐름을 정의 하였다.
     * 1. doPreProcess -
     * 2. doProcess -
     * 3. doPostProcess 순으로 호출 된다.
     * 
     * @param messageContext
     * @param dataMap
     * @return 처리결과가 담겨 있는 DataMap
     */
    public DataMap process(MessageContext messageContext, DataMap dataMap) throws MessageException {
        dataMap = doPreProcess(messageContext, dataMap);
        dataMap = doProcess(messageContext, dataMap);
        dataMap = doPostProcess(messageContext, dataMap);
                
        return dataMap;
    }
    
	protected abstract DataMap doPreProcess(MessageContext messageContext, DataMap dataMap) throws MessageException;

	
	protected abstract DataMap doPostProcess(MessageContext messageContext, DataMap dataMap) throws MessageException;
	
	
	protected abstract DataMap doProcess(MessageContext messageContext, DataMap dataMap) throws MessageException;
}
