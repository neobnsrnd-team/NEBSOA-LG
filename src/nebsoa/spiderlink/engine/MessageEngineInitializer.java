/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.engine;

import nebsoa.spiderlink.context.MessageEngineContext;

/*******************************************************************
 * <pre>
 * 1.설명 
 * MessageEngine 정보를 로딩하고 GatewayConnector를 생성을 담당하는 클래스
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
 * $Log: MessageEngineInitializer.java,v $
 * Revision 1.1  2018/01/15 03:39:50  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:45  cvs
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
 * Revision 1.1  2008/08/04 08:54:52  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:28  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:39:11  안경아
 * *** empty log message ***
 *
 * Revision 1.3  2006/11/23 00:40:25  김승희
 * initialize 메소드 호출로 수정
 *
 * Revision 1.2  2006/11/22 11:33:32  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2006/09/18 07:24:13  김승희
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public class MessageEngineInitializer {
	
	 private static Object dummy=new Object();
	    
	 private static MessageEngineInitializer instance;
	 
	 public static MessageEngineInitializer getInstance(){
	    if(instance==null){
	        synchronized (dummy) {
	            instance = new MessageEngineInitializer();
	        }
	    }
	    return instance;
	 }
	 
	 private MessageEngineInitializer(){
		 
	 }
	 
	 public void init(){
		 //1. 메세지 엔진 컨텍스트를 생성하고 정보를 모두 로딩한다.
		 MessageEngineContext.initialize();
		 
		 //2. GatewayConnector를 생성한다..
		 GatewayManager.getInstance().createGatewayConnector();
	 }
	 
 
	 public static void main(String[] args){
		 MessageEngineInitializer.getInstance().init();
	 }
}
