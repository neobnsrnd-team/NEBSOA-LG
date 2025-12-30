/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.ejb;

import nebsoa.common.base.BaseSessionBean;
import nebsoa.common.util.DataMap;
import nebsoa.spiderlink.engine.MessageEngine;
import nebsoa.spiderlink.exception.MessageException;

/*******************************************************************
 * <pre>
 * 1.설명 
 * MessageEngine 의 동기적 실행을 대리하는 MessageProcessor-Stateless-SessionBean
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
 * $Log: MessageProcessorBean.java,v $
 * Revision 1.1  2018/01/15 03:39:50  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:53  cvs
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
 * Revision 1.1  2008/08/04 08:54:54  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:32  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:07  안경아
 * *** empty log message ***
 *
 * Revision 1.11  2006/08/19 02:02:59  김성균
 * *** empty log message ***
 *
 * Revision 1.10  2006/07/31 08:30:08  김성균
 * reload() 제거
 *
 * Revision 1.9  2006/07/28 09:30:11  김승희
 * exception 처리 변경
 *
 * Revision 1.8  2006/06/20 13:57:46  김성균
 * reloadConfig() 수정
 *
 * Revision 1.7  2006/06/20 04:58:29  김승희
 * EngineContext 주석 처리
 *
 * Revision 1.6  2006/06/20 04:17:13  김성균
 * EngineContext import 부분 삭제
 *
 * Revision 1.5  2006/06/19 11:59:39  김승희
 * BPMException 제거
 *
 * Revision 1.4  2006/06/19 07:34:13  김승희
 * method 변경
 *
 * Revision 1.3  2006/06/17 08:38:33  김승희
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public class MessageProcessorBean extends BaseSessionBean implements MessageProcessor {
	
	/**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -4759670883177050056L;

    /* (non-Javadoc)
	 * @see nebsoa.spiderlink.ejb.MessageProcessor#doSyncProcess(java.lang.String, java.lang.String, java.lang.String, nebsoa.common.util.DataMap)
	 */
	public DataMap doSyncProcess(String trxId, String orgId, String ioType, DataMap dataMap) throws MessageException {
		return MessageEngine.doSyncProcess(trxId, orgId, ioType, dataMap);
	}
}
