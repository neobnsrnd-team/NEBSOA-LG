/*******************************************************************
 * 외환은행 CLS 연계 프로젝트
 *
 * Copyright (c) 2003-2004 by COMAS, Inc.
 * All rights reserved.
 *******************************************************************
 * HeaderConstants.java,2004. 6. 24. 이종원
 *
 * $Id: HeaderConstants.java,v 1.1 2018/01/15 03:39:52 cvs Exp $
 *
 * $Log: HeaderConstants.java,v $
 * Revision 1.1  2018/01/15 03:39:52  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:11  cvs
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
 * Revision 1.1  2008/08/04 08:54:51  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2007/11/26 08:37:45  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2006/06/29 04:02:09  이종원
 * *** empty log message ***
 *
 * Revision 1.8  2004/11/18 07:11:00  helexis
 * 기존의 코드 존재로 인하여 BCP 상태에 대한 코드 변경
 *  - 9997001 -> 9998001
 *  - 9997002 -> 9998002
 *
 * Revision 1.7  2004/11/12 01:38:13  helexis
 * BCP 상태관련 부분 추가
 *
 * Revision 1.6  2004/11/08 08:38:10  jegu
 * <No Comment Entered>
 *
 * Revision 1.5  2004/10/27 02:47:59  jegu
 * <No Comment Entered>
 *
 * Revision 1.4  2004/10/15 11:39:48  helexis
 * source formatting
 *
 * Revision 1.3  2004/10/15 11:33:28  ljw
 * 파일 포맷팅
 *
 * Revision 1.49  2004/10/15 07:19:42  ljw
 * *** empty log message ***
 *
 * Revision 1.48  2004/09/30 08:57:08  ljw
 * EAI_SYS_KFTC FLOW DEFINE
 *
 * Revision 1.47  2004/09/30 05:10:07  ljw
 * *** empty log message ***
 *
 * Revision 1.46  2004/09/20 10:30:08  helexis
 * PIS상세구분코드 추가
 *
 * Revision 1.45  2004/09/20 10:24:33  helexis
 * 잘못 merge 된 데이터를 다시 commit 하면서 서버의 파일을 overwrite 함
 *
 * Revision 1.44  2004/09/20 07:51:00  ljw
 * *** empty log message ***
 *
 * Revision 1.43  2004/09/20 07:49:10  ljw
 * *** empty log message ***
 *
 * Revision 1.42  2004/09/20 06:29:29  ljw
 * 전문생성기관 FD 추가
 * BIZ FLOW ID : FLOW_FD_TO_KFTC 추가
 *
 * Revision 1.40  2004/09/16 07:54:12  helexis
 * 전문의 기 전송여부 추가
 *
 * Revision 1.39  2004/09/13 08:58:33  ljw
 * *** empty log message ***
 *
 * Revision 1.38  2004/09/13 06:00:30  ljw
 * *** empty log message ***
 *
 * Revision 1.37  2004/09/10 00:36:29  helexis
 * EAI 처리결과 정상 코드 추가
 *
 *
 ******************************************************************/
package test.spiderlink.message;

/**
 * @author 이종원
 *
 */
public interface HeaderConstants {
    /**
     * 업무 프로세스 ID
     */
    String BIZ_FLOW_ID = "BIZ_FLOW_ID";

    /**
     * RESULT_MSG
     */
    String RESULT_MSG = "RESULT_MSG";

    String ERROR_FLOW_STEP = "ERROR_FLOW_STEP";

    /**
     * RESULT_CODE
     */
    String RESULT_CODE = "RESULT_CODE";

    String ERROR_UNKOWN = "9991114";

    /**
     * 전문 접수 기관
     * */
    String RECV_ORG = "RECV_ORG";

    /**
     * MT TYPE
     * */
    String MT = "MT";

    /**
     * FLOW가 정상적으로 흘러 감을 의미
     * */
    String FLOW_OK = "9990000";

    ///////////////////////////////////////////////////
    /**
     * 요청인지 응답인지 구분
     */
    String KFTC_REQ_RES_GUBN = "KFTC_REQ_RES_GUBN";

    /**
     * 요청임을 표시
     */
    String KFTC_REQUEST = "2";

    /**
     * 응답임을 표시
     */
    String KFTC_RESPONSE = "4";

    ///////////////////////////////////////////////////

    /**
     * 전문 생성 기관
     */
    String CRE_ORG = "CRE_ORG";

    String KFTC_ORG_MESSAGE = "KFTC_ORG_MESSAGE";

    String RESPONSE_ERR_LENGTH = "9991199";

    String MSG_TEXT1 = "MSG_TEXT1";

    String OUTPUT_TEXT = "OUTPUT_TEXT";

    String FLOW_KFTC_TO_KITS = "KFTC_TO_KITS";

    String FLOW_KITS_TO_KFTC = "KITS_TO_KFTC";

    String MSG_TYPE = "MSG_TYPE";

    String FLOW_TPB_MT300_NEWT = "TPB_300_NEWT";

    String FLOW_TPB_MT300_CANC = "TPB_300_CANC";

    /**
     * EAI를 통하여 금결원에 시스템 관리 전문 전송
     */
    String FLOW_EAI_SYS_KFTC = "EAI_SYS_KFTC";

    String MSG_TEXT = "MSG_TEXT";

    String RES_CD = "RES_CD";

    String IS_ERROR = "IS_ERROR";

    String FLOW_KFTC_SYS_KFTC = "KFTC_SYS_KFTC";

    String FLOW_KFTC_RECON = "KFTC_RECON";

    String FLOW_KEB_SYS_KFTC = "KEB_SYS_KFTC";

    String SENDED_MSG = "SENDED_MSG";

    String FLOW_FX_900_RECV = "FX_900_RECV";

    String SRC_KEY = "SRC_KEY";

    String MSG_HDR = "MSG_HDR";

    String MSG_NO = "MSG_NO";

    String RT_CODE = "RT_CODE";

    String RESEND_YN = "RESEND_YN";

    String RETRY_YN = "RETRY_YN";

    /**
     * 외자서버로부터 시스템 관리 전문 수신 처리하는 플로우ID
     */
    String FLOW_FD_SYS_CLS = "FD_SYS_CLS";

    /**
     * CLS -> 외자서버로 시스템 관리 전문을 송신 처리하는 플로우ID
     */
    String FLOW_CLS_SYS_FD = "CLS_SYS_FD";

    /**
     * 외자서버로부터 300 전문 수신 하여 KFTC로 전송 처리하는 플로우ID
     */
    String FLOW_FD_TO_KFTC = "FD_TO_KFTC";

    String ERR_KFTC_SEND_FAIL = "9991199";

    String TRADE_NO = "TRADE_NO";

    String ERR_MSG_KFTC_SEND_FAIL = "금결원 전송 실패";

    String CRE_DTIME = "CRE_DTIME";

    String ERR_FLOW_STEP = "ERR_FLOW_STEP";

    String B1_BIC = "B1_BIC";

    String B2_BIC = "B2_BIC";

    String RLT_TRADE_NO = "RLT_TRADE_NO";

    String TRADE_TYPE = "TRADE_TYPE";

    String TRACE_NO = "TRACE_NO";

    String SREF = "SREF"; // 참조번호

    String ANSWERS = "ANSWERS"; // 396 응답

    String CURRENCY_OP = "CURRENCY_OP"; //매도통화기호

    String AMOUNT_OP = "AMOUNT_OP"; //매도금액

    String TRADE_DATE = "TRADE_DATE"; //거래작성일

    String CLSTIME = "CLSTIME"; //결제시간

    String FLOW_KFTC_RESPONSE = "KFTC_RESPONSE";

    /**
     * KFTC로 내보낼 응답을 세팅할 KEY 값
     */
    String KFTC_RESPONSE_CODE = "KFTC_RESPONSE_CODE";

    /** 통화 **/
    String CURRENCY = "CURRENCY";

    /** 금액 **/
    String AMOUNT = "AMOUNT";

    /** MT298 에 대한 결제 시간에 대한 통화 및 금액 총 갯수 */
    String TXN_CNT = "TXN_CNT";

    /** 결제 일 **/
    String VALUE_DATE = "VALUE_DATE";

    /** 외신에 전송해야 할 메시지들 **/
    String WANG_SEND_MSGS = "WANG_SEND_MSGS";

    /**
     * 관리자에게 추가적인 정보를 알리기 위해 사용
     */
    String ALERT_MSG = "ALERT_MSG";

    /**
     * TPB 300 ROLL BACK 요청 메세지 인지 여부
     */
    String ROLLBACK_MSG_YN = "ROLLBACK_MSG_YN";

    /** 외신에 전송 성공한 메시지 갯수 **/
    String SUCCESS_COUNT = "SUCCESS_COUNT";

    /**
     * TPB 300 NEWT 전문 ROLL BACK 처리
     */
    String FLOW_TPB_MT300_NEWT_ROLL_BACK = "TPB_300_NEWT_RB";

    /**
     * TPB 300 CANC 전문 ROLL BACK 처리
     */
    String FLOW_TPB_MT300_CANC_ROLL_BACK = "TPB_300_CANC_RB";

    //스왑거래에 대한 구분
    String IO_SWAP = "IO_SWAP";

    //Pay in 스케줄 종류
    String PIS_TYPE = "PIS_TYPE"; //IPIS RPIS

    //차변대변 구분
    String CD_GB = "CD_GB";

    //cls전표 아이디
    String CLS_ID = "CLS_ID";

    //계정 구분
    String ACCT_GB = "ACCT_GB";

    //STEP을 의도적으로 Stop할 경우 'Y'로 셋팅
    String IS_INTEND = "IS_INTEND";

    /** STEP을 의도적으로 Stop할 경우 */
    String INTEND_Y = "Y";

    /** STEP을 의도적으로 Stop하지 않을 경우 */
    String INTEND_N = "N";

    /** 대사의 종류를 구분하기 위한 코드 */
    String RECON_CLASS_CD = "RECON_CLASS_CD";

    /** Reviced-SPIS 대사 */
    String RECON_CLASS_RSPIS = "RECON_CLASS_RSPIS";

    /** Reviced-Pre-Advice */
    String RECON_CLASS_RPREADV = "RECON_CLASS_RPREADV";

    /** 기장일 */
    String ACCT_DATE = "ACCT_DATE";

    /** TPB 메시지 여부 : KITS로 보내지 않기위한 플래그로 사용 */
    String TPB_MSG_YN = "TPB_MSG_YN";

    /** BIZ_DATE */
    String BIZ_DATE = "BIZ_DATE";

    /** WANG_SEND_YN */
    String WANG_SEND_YN = "WANG_SEND_YN";

    /** Pay-In_Call 여부 */
    public static final String IS_PAY_IN_CALL = "IS_PAY_IN_CALL";

    String IO_TYPE = "IO_TYPE";

    public static String OFAC_FILTER_ERROR = "9994503";

    /** 공백 문자를 나타내는 상수 */
    public static final char SPACE_CHAR = '\u0020';

    /** 문자 '0' 를 나타내는 상수 */
    public static final char ZERO_CHAR = '0';

    String KFTC_SEND_DATA = "KFTC_SEND_DATA";

    /** length 가 0 인 빈 byte [] 을 나타내는 상수 */
    public static final byte[] EMPTY_BYTE_ARRAY = new byte [0];

    /** 전문생성기관 타입(KFTC) */
    public static final String CRE_ORG_KFTC = "KFTC";

    /** 전문생성기관 타입(KITS) */
    public static final String CRE_ORG_KITS = "KITS";

    /** 전문생성기관 타입(WANG) */
    public static final String CRE_ORG_WANG = "WANG";

    /** 전문생성기관 타입(FD 차세대 외자, 구 KITS) */
    public static final String CRE_ORG_FD = "FD";

    /** 관리부점코드 */
    public static final String MAN_DEP_CD = "MAN_DEP_CD";

    /**
     * 메세지 마스터에 입력시 중복 체크를 할 것인지 여부.
     * 'N'으로 세팅하면 중복 체크를 안하고 무조건 INSERT한다.
     */
    String DUP_CHECK_YN = "DUP_CHECK_YN";

    /**
     * EAI 처리결과 정상 코드
     *
     * @since 2004.09.09
     * @author Helexis
     */
    public static final String EAI_PROC_SUCCESS_CD = "1";

    /**
     * 기 전송된 전문인지의 여부를 나타내는 KEY
     *
     * @since 2004.09.15
     * @author Helexis
     */
    public static final String IS_ALREADY_SENDED = "IS_ALREADY_SENDED";

    /**
     * 기 전송된 전문임을 나타내는 값
     *
     * @since 2004.09.15
     * @author Helexis
     */
    public static final String IS_ALREADY_SENDED_Y = "IS_ALREADY_SENDED_Y";

    /**
     * 기 전송된 전문임을 나타내는 값
     *
     * @since 2004.09.15
     * @author Helexis
     */
    public static final String IS_ALREADY_SENDED_N = "IS_ALREADY_SENDED_N";

    /**
     * PIS상세구분코드
     *
     * @since 2004.09.20
     * @author 이승헌
     */
    public static final String PIS_CLASS_CD = "PIS_CLASS_CD";

    /**
     * 사용자ID
     * @since 2004.10.27
     * @author 구정은
     */
    public static final String USER_ID = "USER_ID";

    /**
     * 사용자IP
     * @since 2004.11.08
     * @author 구정은
     */
    public static final String USER_IP = "USER_IP";
    
    /**
     * BCP 상태임을 나타내는 코드(IN)
     * 
     * @since 2004.11.12
     * @author Helexis
     */
    public static final String BCP_IN_STATE_CD = "9998001";
    
    /**
     * BCP 상태임을 나타내는 메시지(IN)
     * 
     * @since 2004.11.12
     * @author Helexis
     */
    public static final String BCP_IN_STATE_MSG = "BCP 상태 입니다.";
    
    /**
     * BCP 상태임을 나타내는 코드(OUT)
     * 
     * @since 2004.11.12
     * @author Helexis
     */
    public static final String BCP_OUT_STATE_CD = "9998002";
    
    /**
     * BCP 상태임을 나타내는 메시지(OUT)
     * 
     * @since 2004.11.12
     * @author Helexis
     */
    public static final String BCP_OUT_STATE_MSG = BCP_IN_STATE_MSG;
    
    /**
     * BCP 상태임을 나타내는 메시지(From KITS, IO-SWAP)
     * 
     * @since 2004.11.18
     * @author Helexis
     */
    public static final String BCP_OUT_STATE_MSG_2 = "BCP 상태이므로 출력 승인 요청 처리하였습니다.";

}
