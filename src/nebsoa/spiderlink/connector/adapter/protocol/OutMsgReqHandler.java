/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.connector.adapter.protocol;

import java.io.IOException;

import nebsoa.spiderlink.context.MessageContext;
import nebsoa.spiderlink.engine.adapter.TimeoutException;
import nebsoa.spiderlink.socketpool.WorkerSocket;


/*******************************************************************
 * <pre>
 * 1.설명 
 * 기동 요구 전문 처리 핸들러를 만들 때 상속 받을 클래스.
 * 
 * 2.사용법
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
 * $Log: OutMsgReqHandler.java,v $
 * Revision 1.1  2018/01/15 03:39:51  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:06  cvs
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
 * Revision 1.1  2007/11/26 08:39:09  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2006/10/03 09:12:44  이종원
 * refactoring
 *
 * Revision 1.2  2006/09/27 10:23:19  이종원
 * socket pool 디렉토리변경
 *
 * Revision 1.1  2006/09/16 13:10:55  이종원
 * 어느정도 완성된 초안 작성
 *
 * Revision 1.1  2006/09/16 04:32:52  이종원
 * 이동
 *
 * </pre>
 ******************************************************************/
public interface OutMsgReqHandler {

	public void handleRequest(MessageContext ctx,WorkerSocket socket)
        throws IOException, TimeoutException, Exception;
}
