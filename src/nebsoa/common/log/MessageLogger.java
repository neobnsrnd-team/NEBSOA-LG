/*
 * Nebsoa Framework
 * 
 * Copyright (c) 2008-2009 IBK Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 IBK 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.log;

import nebsoa.common.util.DataMap;
import nebsoa.service.context.ServiceContext;
import nebsoa.spiderlink.context.MessageContext;

/*******************************************************************
 * <pre>
 * 1.설명 
 * Message Log를 위한  abstract Delegator Class
 * messageLog method는 필요에 따라 overloading가능하다. 
 * 이 abstract class를 상속받는 delegator class에서 필요한 method만 overriding할수 있도록 
 * abstract method가 아닌 method로 overloading해야 한다. 
 * 
 * 2.사용법
 * 
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author : 김은정 
 * @version
 * @작성 일자 : 2008. 05. 06
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 *
 * $Log: MessageLogger.java,v $
 * Revision 1.1  2018/01/15 03:39:48  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:19  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:50  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:23  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:26  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:51  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.3  2008/05/09 02:12:53  김은정
 * *** empty log message ***
 *
 * Revision 1.2  2008/05/08 10:16:41  김은정
 * *** empty log message ***
 *
 * Revision 1.1  2008/05/07 06:20:34  김은정
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
abstract public class MessageLogger {

    public void serviceReqLog(ServiceContext serviceContext, DataMap dataMap){}
    
    public void serviceResLog(ServiceContext serviceContext, DataMap dataMap){}
    
    public void messageLog(MessageContext messageContext, DataMap dataMap, byte[] messageBytes, String reqResType, String resultCode, DataMap argMap){}
    
    public void messageLog(MessageContext messageContext, DataMap dataMap, byte[] messageBytes, String reqResType, String resultCode, String channelGubun){}    

    public void log(String fileName, String data) {
		LogManager.info(fileName, data);
	}
}
