/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.engine.message;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 메시지와 관련된 상수 정의 클래스
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
 * $Log: MessageConstants.java,v $
 * Revision 1.1  2018/01/15 03:39:48  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:16  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:50  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:22  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:26  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:51  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:20  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.2  2007/12/26 11:30:26  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2007/11/26 08:38:04  안경아
 * *** empty log message ***
 *
 * Revision 1.17  2007/07/16 03:41:59  김승희
 * CDATA 상수 추가
 *
 * Revision 1.16  2006/07/10 07:13:36  김승희
 * 인터페이스환경 추가
 *
 * Revision 1.15  2006/07/04 09:31:29  김승희
 * 패키지 변경
 *
 * Revision 1.14  2006/06/19 08:03:59  김승희
 * TRX_TIME_MILLIS  추가
 *
 * Revision 1.13  2006/06/17 10:12:09  김승희
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public class MessageConstants {

    /**
     * 기관을 나타내는 키 값
     */
    public static final String ORG_ID = "ORG_ID";

    /**
     * 요청 메시지 ID 를 나타내는 키 값
     */
    public static final String REQ_MESSAGE_ID = "REQ_MESSAGE_ID";

    /**
     * 응답 메시지 ID 를 나타내는 키 값
     */
    public static final String RES_MESSAGE_ID = "RES_MESSAGE_ID";

    /**
     * 전송할 메시지를 나타내는 키 값
     */
    public static final String MESSAGE_TO_SEND = "__me$$age_to_send";

    /**
     * 수신한 메시지를 나타내는 키 값
     */
    public static final String MESSAGE_RECEIVED = "__me$$age_received";

    /**
     * 메시지를 나타내는 키 값
     */
    public static final String MESSAGE = "__me$$age";

    /**
     * error code
     */
    public static final String 처리결과 = "처리결과";

    /**
     * 전자금융 error code
     */
    public static final String 전자금융에러코드 = "전자금융에러코드";

    /**
     * 에러메세지 error code
     */
    public static final String 에러메시지 = "에러메시지";
    
    /**
     * 송신
     */
    public static final String SEND = "S";
    
    /**
     * 수신 
     */
    public static final String RECEIVE = "R";
    
    /**
     * GW UID
     */
    public static final String UID = "_$GW_UID";
    
    /**
     * trx time 
     */
    public static final String TRX_TIME_MILLIS= "_$TRX_TIME_MILLIS";
    
    /**
     * 인터페이스환경 
     * R : Real 운영시스템
     * T : Test 시스템
     * D : 개발 시스템
     */
    public static final String 인터페이스환경 = "_$인터페이스환경";
    
    /**
     * 내부 기관
     */
    public static final String ORG_STD = "STD";
    
    /**
     * CDATA Section 시작부
     */
    public static final String CDATA_START = "<![CDATA[";
    
    /**
     * CDATA Section 종료부
     */
    public static final String CDATA_END = "]]>";
    
	public static final String USER_DATAMAP = "_$$USER_MAP";	
}// end of MessageConstants.java
