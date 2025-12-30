/*******************************************************************
 * 외환은행 CLS 연계 프로젝트
 *
 * Copyright (c) 2003-2004 by COMAS, Inc.
 * All rights reserved.
 * @file : EAIMessageUtil.java
 * @author : 이종원
 * @date : 2004. 9. 1.
 * @설명 : 
 * <pre>
 * 
 * </pre>
 *******************************************************************
 * $Id: EAIUtil.java,v 1.1 2018/01/15 03:39:52 cvs Exp $
 * $Log: EAIUtil.java,v $
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
 * Revision 1.2  2008/02/20 00:42:48  오재훈
 * *** empty log message ***
 *
 * Revision 1.1  2007/11/26 08:37:49  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2006/06/29 04:02:09  이종원
 * *** empty log message ***
 *
 * Revision 1.6  2004/11/12 05:24:48  ljw
 * *** empty log message ***
 *
 * Revision 1.5  2004/11/02 02:22:33  ljw
 * %S라고 된 부분을 해당 서비스 라는 문구로 수정
 *
 * Revision 1.4  2004/11/01 01:01:37  helexis
 * 중복되는 이름을 가진 클래스들을 다른 이름으로 Refactoring 함.
 *  - com.comas.stpa.controller.model.AllianceVO -> AllianceValueObject
 *  - com.comas.stpa.common.resource.Constants -> ResourceConstants
 *  - com.comas.stpa.util.PropertyManager -> PropertyMgr
 *  - com.keb.stpa.common.util.STPStringUtil -> STPStringUtility
 *
 * Revision 1.3  2004/10/25 01:27:29  ljw
 * EAI 전문길이 수정 311->321 byte
 *
 * Revision 1.2  2004/10/15 08:37:21  helexis
 * formatting 이후 재 commit
 *
 * Revision 1.16  2004/10/11 06:45:15  ljw
 * MAIN EAI SERVER가 접속 안될 때 
 * SECONDARY EAI SERVER에 자동 접속하는 기능 추가
 *
 * Revision 1.15  2004/10/01 08:11:36  ljw
 * *** empty log message ***
 *
 * Revision 1.14  2004/09/17 06:35:31  helexis
 * EAI 전문 헤더 포맷 오류 체크로직 관련 로직 추가
 *
 * Revision 1.13  2004/09/14 03:03:07  ljw
 * *** empty log message ***
 *
 * Revision 1.12  2004/09/14 03:01:23  ljw
 * *** empty log message ***
 *
 * Revision 1.11  2004/09/14 01:54:14  ljw
 * eai connector얻어 오는 부분 수정
 *
 * Revision 1.10  2004/09/10 06:58:10  ljw
 * makeResponseMessage
 * makeErrorResponseMessage
 * 기능 추가
 *
 * Revision 1.9  2004/09/09 10:14:39  helexis
 * 인터페이스 관련 코드 부분 수정
 *
 * Revision 1.8  2004/09/09 01:34:04  helexis
 * EAI 거래코드 세팅부분 변경
 *
 * Revision 1.7  2004/09/08 06:38:54  ljw
 * *** empty log message ***
 *
 * Revision 1.6  2004/09/03 04:43:43  ljw
 * *** empty log message ***
 *
 * Revision 1.5  2004/09/02 11:14:10  ljw
 * *** empty log message ***
 *
 * Revision 1.4  2004/09/02 10:38:37  ljw
 * *** empty log message ***
 *
 * Revision 1.3  2004/09/01 10:38:29  ljw
 * *** empty log message ***
 *
 * Revision 1.2  2004/09/01 10:23:15  ljw
 * eai error code추가
 *
 * Revision 1.1  2004/09/01 03:01:06  ljw
 * *** empty log message ***
 *
 * Revision 1.4  2004/09/01 02:38:44  ljw
 * *** empty log message ***
 *
 * Revision 1.3  2004/09/01 02:32:06  ljw
 * *** empty log message ***
 *
 * Revision 1.2  2004/09/01 02:15:09  ljw
 * *** empty log message ***
 *
 * Revision 1.1  2004/09/01 01:58:05  ljw
 * eai message factory utililty
 *    
 ******************************************************************/
package test.spiderlink.message;

import java.util.HashMap;

import nebsoa.common.cache.CacheMap;
import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.FormatUtil;
import nebsoa.common.util.PropertyManager;
import nebsoa.common.util.StringUtil;

/**
 * 2004. 9. 1. 이종원
 * 설명 :
 * <pre>
 * 
 * </pre> 
 */
public class EAIUtil {
    /**
     * 
     * 2004. 9. 1. 이종원 작성
     * @param eaiServiceCode
     * @return
     * 설명: 메세지 객체를 리턴 해 준다.
     */
    public static EAIMessage getEAIMessage(String eaiServiceCode, String msgNo) {
        EAIMessage msg = new EAIMessage();
        if (EAIConstants.EIRT007101.equals(eaiServiceCode)) {
            msg.setField3("A"); //비동기식
        } else {
            msg.setField3("S"); //동기식
        }

        msg.setField4("KES");
        msg.setField5(FormatUtil.getToday("yyyyMMdd"));
        msg.setField6("LS01");

        if (msgNo != null && msgNo.length() > 15) {
            msg.setField8(msgNo); //unique key
        } else {
            msg.setField8(FormatUtil.getToday("yyyyMMddHHmmssSS")); //unique key
        }
        msg.setField18(FormatUtil.getToday("yyyyMMdd"));
        msg.setField19(FormatUtil.getToday("yyyyMMddHHmmssSS"));
        msg.setField21("A"); //아스키
        msg.setField22("A");
        //		//T:TEST,  R:REAL
        //    	msg.setField24(PropertyMgr.getProperty("default","RUNNING_MODE"));

        /*
         * 각 서비스 별로, 
         * '운영[R], 테스트[T], 개발[D]' 모드를 변경 가능하도록
         * 아래의 형태로 바꾸었다.
         * 
         * 2004.09.09 - Helexis
         */
        /** 
         * 2004.11.12 - 이종원
         * TIMEOUT전문은 총계정만 해당하므로 
         * TIMEOUT전문 전송일 경우 총계정 모드를 공유하여 사용한다. 
         */
        msg.setField24(PropertyManager.getProperty("connector", 
                (eaiServiceCode.equals(EAIConstants.EIRG000001)?
			    EAIConstants.EIRI004767:eaiServiceCode)+ "_RUNNING_MODE"));
         
        // EAI 서비스 코드를 세팅합니다.
        msg.setField25(eaiServiceCode);
        return msg;
    }

    /**
     * 
     * 2004. 9. 1. 이종원 작성
     * @param eaiServiceCode
     * @return
     * 설명: msgNo를 기본값으로 세팅된 메세지 객체를 리턴 해 준다.
     */
    public static EAIMessage getEAIMessage(String eaiServiceCode) {
        return getEAIMessage(eaiServiceCode, null);
    }

    /**
     * 
     * 2004. 9. 14. 이종원 작성
     * @param eaiServiceCode
     * @return
     * 설명:해당 서비스의 running mode(D,T,R)에 따라 해당하는 커넥터를 얻어 온다.
     * 속성 파일에 EIRI00XXXX_RUNNING_MODE값이 있어야 하며, 
     * EAI_IP_T, EAI_IP_D,EAI_IP_R 및 포트 KEB_EAI_SEND_PORT_T,KEB_EAI_SEND_PORT_D
     * KEB_EAI_SEND_PORT_R, KFTC_EAI_SEND_PORT_T, KFTC_EAI_SEND_PORT_D, 
     * KFTC_EAI_SEND_PORT_R 가 설정 되어 있어야 한다.
     */
    public static EAIConnector getEAIConnector(String eaiServiceCode) {
        String runningMode = PropertyManager.getProperty("connector",
                eaiServiceCode + "_RUNNING_MODE");

        if (runningMode == null) {
            throw new SysException(eaiServiceCode + "_RUNNING_MODE 설정이 없습니다.");
        }

        CacheMap cache = CacheMap.getInstance();
        String mode = (String) cache.get("EAI_IP_" + runningMode);

        if (mode == null || mode.equals("")) {
            mode = null;
        }

        EAIConnector connector = null;
        try {
            connector = getEAIConnector(eaiServiceCode, mode);
            cache.put("EAI_IP_" + runningMode, mode == null ? "" : "_SECOND");
        } catch (Exception e) {
            LogManager.info("예비 EAI 서버에 연결을 시도합니다.");
            try {
                connector = getEAIConnector(eaiServiceCode,
                        mode == null ? "_SECOND" : null);
                cache.put("EAI_IP_" + runningMode, mode == null ? "_SECOND"
                        : "");
            } catch (Exception ex) {
                LogManager.error("예비 EAI 서버 연결에도 실패하였습니다.");
            }
        }
        if (connector == null) {
            throw new SysException("EAI와 연결하지 못하였습니다");
        }

        return connector;

    }

    public static EAIConnector getEAIConnector(String eaiServiceCode,
            String mode) {
        String runningMode = PropertyManager.getProperty("connector",
                eaiServiceCode + "_RUNNING_MODE");

        if (runningMode == null) {
            throw new SysException(eaiServiceCode + "_RUNNING_MODE 설정이 없습니다.");
        }

        String eaiIp = null;
        int eaiPort = 0;
        String suffix = "";
        if (mode == null) {
            suffix = "_SECOND";
        }

        eaiIp = PropertyManager.getProperty("connector", "EAI_IP_"
                + runningMode + suffix);

        if (EAIConstants.EIRT007101.equals(eaiServiceCode)) {
            eaiPort = Integer.parseInt(PropertyManager.getProperty("connector",
                    "KFTC_EAI_SEND_PORT_" + runningMode + suffix));
        } else {
            eaiPort = Integer.parseInt(PropertyManager.getProperty("connector",
                    "KEB_EAI_SEND_PORT_" + runningMode + suffix));
        }

        LogManager.debug("MODE:" + runningMode + suffix + ",IP:" + eaiIp
                + ",PORT:" + eaiPort);
        EAIConnector connector = new EAIConnector(eaiIp, eaiPort);

        return connector;
    }

    /**
     * 
     * 2004. 9. 1. 이종원 작성
     * @return KEBEAIConnector
     * 설명: KEBEAIConnector 를 리턴 한다
     
     public static EAIConnector getKEBEAIConnector(){
     String eaiIp = PropertyMgr.getProperty("connector","EAI_IP");
     int eaiPort= Integer.parseInt(PropertyMgr.getProperty("connector","KEB_EAI_SEND_PORT"));		
     return new EAIConnector(eaiIp,eaiPort);
     }
     */
    /**
     * 
     * 2004. 9. 1. 이종원 작성
     * @return KFTCEAIConnector
     * 설명: KFTCEAIConnector를 리턴 한다.
     
     public static EAIConnector getKFTCEAIConnector(){
     String eaiIp = PropertyMgr.getProperty("connector","EAI_IP");
     int eaiPort= Integer.parseInt(PropertyMgr.getProperty("connector","KFTC_EAI_SEND_PORT"));
     return new EAIConnector(eaiIp,eaiPort);
     }
     */

    /**
     * ERROR CODE 및 원인 설명정보를 담는 MAP
     */
    static HashMap errReason = new HashMap();

    static {
        /**정상인 경우 송신서버로 리턴할 때 사용함*/
        errReason.put("00000", "정상");
        /**2PC의 경우에 Async로 처리를 요청하는 경우에 해당함.*/
        errReason.put("EE003", "거래코드와 SYNC구분이 맞지 않습니다.");
        /**EAI 메타 DB에 데이터가 존재하지 않는 경우로 전문의 거래코드가 잘못 설정된 경우 또는 EAI 메타 DB에 해당 데이터가 존재하지 않는 경우*/
        errReason.put("EE007", "해당 서비스에 대한 거래정보를 찾지 못했습니다.");
        /**데이터 자체가 NULL인 경우*/
        errReason.put("EE009", "데이터가 없습니다.");
        /**File Record 크기가 정의된 크기와 같지 않은 경우*/
        errReason.put("EE010", "파일 레코드 크기가 맞지 않습니다.");
        /**데이터의 자리수가 안 맞거나, 숫자 format 오류 등으로 파일 데이터 파싱에 실패한 경우*/
        errReason.put("EE011", "파일 데이터 파싱에 실패하였습니다.");
        /**데이터의 자리수가 안 맞거나, 숫자 format 오류 등으로 전문 데이터 파싱에 실패한 경우*/
        errReason.put("EE012", "전문 데이터 파싱에 실패하였습니다.");
        /**입력전문의 데이터 크기가 EAI table에 정의된 데이터 크기와 다른 경우*/
        errReason.put("EE013", "전문 데이터 길이가 맞지 않습니다.");
        /**입력전문의 채널헤더 크기가 정의된 채널헤더 크기보다 작은 경우*/
        errReason.put("EE014", "채널헤더가 맞지 않습니다.");
        /**입력전문의 전문헤더 크기가 정의된 채널헤더 크기보다 작은 경우*/
        errReason.put("EE015", "전문헤더가 맞지 않습니다.");
        /**처리할 수 없는 유형의 거래코드인 경우*/
        errReason.put("EE016", "처리할 수 없는 거래코드입니다.");
        /**기타 에러로 로그를 확인해야 하는 오류*/
        errReason.put("EE099", "EAI 로그를 확인하시기 바랍니다.");
        /**턱시도 서비스 호출에 실패한 경우*/
        errReason.put("EE101", "턱시도 서비스 호출에 실패했습니다.");
        /**턱시도 서비스 파라미터가 잘못된 경우*/
        errReason.put("EE102", "턱시도 서비스 파라미터가 잘못되었습니다.");
        /**티맥스 서비스 호출에 실패한 경우*/
        errReason.put("EE103", "티맥스 서비스 호출에 실패했습니다.");
        /**티맥스 서비스 파라미터가 잘못된 경우*/
        errReason.put("EE104", "티맥스 서비스 파라미터가 잘못되었습니다.");
        /**턱시도 오류코드 : TPESVCFAIL*/
        errReason.put("EE105", "서비스 수행에 실패했습니다.");
        /**턱시도 오류코드 : TPESVRDOWN*/
        errReason.put("EE106", "턱시도 서버가 다운되었습니다.");
        /**턱시도 오류코드 : TPESYSTEM*/
        errReason.put("EE107", "턱시도 시스템 오류입니다.");
        /**턱시도 오류코드 : TPETRAN*/
        errReason.put("EE108", "턱시도 트랜잭션 오류입니다.");
        /**턱시도 오류코드 : TPESVCERR*/
        errReason.put("EE109", "턱시도 서비스 오류입니다.");
        /**턱시도 오류코드 : TPETIME*/
        errReason.put("EE110", "턱시도 호출 시 타임아웃이 발생했습니다.");
        /**턱시도 오류코드 : TPENOENT*/
        errReason.put("EE111", "해당 서비스가 존재하지 않습니다.");
        /**턱시도 수동 서버와 연결에 실패한 경우*/
        errReason.put("EE113", "턱시도 연결에 실패했습니다.");
        /**소켓 연결 오류가 발생한 경우*/
        errReason.put("EE201", "소켓 연결 오류가 발생했습니다.");
        /**노츠 컨넥터 연결 오류가 발생한 경우*/
        errReason.put("EE202", "노츠 컨넥터 연결 오류가 발생했습니다.");
        /**전문을 보내고 응답을 받는 도중 오류가 발생한 경우 : Broken Pipe, Connection reset by peer*/
        errReason.put("EE203", "응답 대기 중 연결이 끊어졌습니다.");
        /**리턴 전문의 SYNC구분 필드값이 S, A, N이 아닌 경우*/
        errReason.put("EE204", "리턴 전문의 SYNC구분 필드 값이 잘못되었습니다.");
        /**연결 도중에 연결이 끊어진 경우 : Connection Failed*/
        errReason.put("EE205", "연결에 실패했습니다.");
        /**응답을 기다리던 중 지정된 TimeOut 내에 응답이 오지 않은 경우*/
        errReason.put("EE206", "응답 대기 중 타임아웃이 발생했습니다.");
        /**연결 시도하다가 TimeOut이 발생한 경우*/
        errReason.put("EE207", "연결 도중에 타임아웃이 발생했습니다.");
        /**리턴 전문의 총전문길이 필드에 설정된 값과 수신한 전문의 실제 길이가 다른 경우*/
        errReason.put("EE208", "리턴 전문의 데이터 크기가 설정값과 다릅니다.");
        /**FTP user ID 및 비밀번호가 잘못된 경우*/
        errReason.put("EE335", "FTP 로그인 오류가 발생했습니다.");
        /**Read할 파일 또는 디렉토리가 없거나 읽기 권한이 없는 경우*/
        errReason.put("EE336", "FTP 파일 읽기 오류가 발생했습니다.");
        /**Write할 디렉토리가 없거나 쓰기 권한이 없는 경우*/
        errReason.put("EE337", "FTP 파일 쓰기 오류가 발생했습니다.");
        /**FTP 작업도중 네트워크에 장애가 발생한 경우*/
        errReason.put("EE338", "FTP 네트워크 오류가 발생했습니다.");
        /**SQL 연산 중에 오류가 발생한 경우*/
        errReason.put("EE413", "SQL 문을 처리하는 데 오류가 발생했습니다.");
        /**DB 연결 시 네트웍 장애, 혹은 DB 정보의 오류등으로 인해 연결이 되지 않은 경우*/
        errReason.put("EE414", "DB 연결 오류가 발생했습닏다.");
        /**SNA 오류. 송신한 데이터가 처리하기 어려운 경우에 주로 발생하며 LU장애, PU 장애등을 포함한 SNA 통신에러*/
        errReason.put("EE501", "SNA negative response received");
        /**SNA 오류 LU0 컨넥터의 데몬 프로세스가 SIGTERM 신호를 받은 경우. 운영자가 임의로 종료시키는 경우가 대부분*/
        errReason.put("EE502", "SIGTERM signal received");
        /**SNA 오류 호스트의 OUPUT Queue에 정상적으로 처리되지 못한 데이터가 쌓일 경우. 정상적인 환경에서는 거의 발생되지 않으며 주로 테스트 또는 개발 중에 발생. 이경우 정상적인 트랜잭션 흐름과는 다르게 LU0 컨넥터로 데이터가 전달될 수 있으며 이 때 발생되는 오류*/
        errReason.put("EE503", "OUTPUT Queue data is corrupted");
        /**SNA 오류 호스트로 트랜재션을 전송하기 전 혹은 전송받은 데이터를 처리하는 과정의 오류. EAI 내부 오류에 해당. 대체로 TCP/IP 통신 오류나 헤더 데이터의 문제 및 그에 상응하는 후속절차의 오류등을 포함.*/
        errReason.put("EE504", "SNA Processing error");
        /**EAI에서 디버깅 용으로 사용하는 코드*/
        errReason.put("EE900", "정상");
        /**전문 내에 특정 필드가 없는 경우*/
        errReason.put("EE901", "해당 서비스 필드가 존재하지 않습니다.");
        /**매핑 정보가 존재하지 않는 경우*/
        errReason.put("EE902", "해당 서비스의 매핑규칙이 없습니다.");
        /**데이터 구조 정보가 없는 경우*/
        errReason.put("EE903", "해당 서비스의 데이터구조가 없습니다.");
        /**호출 서비스 정보가 없는 경우*/
        errReason.put("EE904", "해당 서비스의 서비스정보가 없습니다.");
        /**테이블 정보가 없는 경우*/
        errReason.put("EE905", "해당 서비스의 테이블정보가 없습니다.");
        /**거래코드가 없거나, 관련 정보가 없는 경우*/
        errReason.put("EE906", "해당 서비스의 거래정보가 없습니다.");
        /**파일 송수신 시 필요한 파일 정보를 찾지 못할 때*/
        errReason.put("EE907", "해당 서비스의 파일정보가 없습니다.");
        /**필드의 값이 NUMBER인 경우에만 체크 가능*/
        errReason.put("EE908", "해당 서비스의 검증에 실패했습니다.");
        /**파일 송수신시 FTP로 연결하기 위한 정보가 없는 경우*/
        errReason.put("EE909", "해당 서비스의 FTP 연결정보가 없습니다.");
        /**타임아웃*/
        errReason.put("EE910", "타임아웃");
        /**지원하지 않는 인코딩 타입인 경우*/
        errReason.put("EE911", "인코딩 오류");
        /**EAI 내에서 프로그램간의 호출 관계에서 호출 대상 프로그램이 존재하지 않을 경우( 기동하지 않은 경우)*/
        errReason.put("EE912", "연결 대상 프로세스가 실행중이지 않습니다.");
        /**프로그램의 오류등으로 인해 잘못된 이벤트를 수신한 경우*/
        errReason.put("EE913", "잘못된 이벤트");
        /**롤백이나 커밋 등의 오류로 인해 프로세스 모델의 CONTEXT가 존재하지 않는 경우*/
        errReason.put("EE916", "CONTEXT가 없습니다.");
        /**EAI에서 처리한 적이 없는 거래의 경우*/
        errReason.put("EE917", "처리된 내용이 없습니다.");
        /**FTP와 관련된 EAI 모델에서 오류가 발생한 경우*/
        errReason.put("EE937", "FTP 작업 도중 오류가 발생했습니다.");
        /**처리결과 조회 거래 시 수동서버로부터 응답을 받지 못한 경우*/
        errReason.put("EE938", "수동서버에서 처리중입니다.");
        /***/
        errReason.put("EE941", "세션키 생성시 오류입니다");
        /***/
        errReason.put("EE942", "암호화 오류입니다.");
        /***/
        errReason.put("EE943", "복호화 오류입니다.");
        /**EAI 로그를 통해 확인해야 할 오류*/
        errReason.put("EE999", "EAI로그를 확인하시기 바랍니다.");

    }

    /**
     * EAI 에러 코드에 해당하는 설명을 리턴 한다.
     * @param errCode
     * @return err message
     */
    public static String getErrorReason(String errCode) {
        String errStr = (String) errReason.get(errCode);
        if (errStr == null)
            errStr = "알수 없는 상태코드(" + errCode + ")";
        return errStr;
    }

    /**
     * 2004. 9. 2. 이종원 작성
     * @param eaiServiceCode
     * @return
     * 설명:eaiServiceCode를 가지고 해당 전문을 처리 할 수 있는 EAIProcessor를 얻어온다.
     */
    public static EAIProcessor getEAIPorcessor(String eaiServiceCode) {
        CacheMap cache = CacheMap.getInstance();
        Object obj = cache.get(eaiServiceCode + "Processor");
        EAIProcessor processor = null;
        if (obj == null) {
            try {
                obj = Class.forName(
                        "com.keb.stpa.engine.processor." + eaiServiceCode
                                + "Processor").newInstance();
                cache.put(eaiServiceCode + "Processor", obj);
            } catch (InstantiationException e) {
                LogManager.error(e.getMessage(), e);
                throw new SysException("EAIPorcessor 생성 실패");
            } catch (IllegalAccessException e) {
                LogManager.error(e.getMessage(), e);
                throw new SysException("EAIPorcessor is Not Public");
            } catch (ClassNotFoundException e) {
                LogManager.error(e.getMessage(), e);
                throw new SysException(e.getMessage());
            } catch (Exception e) {
                LogManager.error(e.getMessage(), e);
                throw new SysException(e.getMessage());
            }
        }
        processor = (EAIProcessor) obj;

        return (EAIProcessor) processor.clone();
    }

    /**
     * 2004. 9. 10. 이종원 작성
     * @param recvData
     * 설명: eai수신 메세지로 부터 응답 메세지를 생성하여 리턴
     * 처리결과를 정상(1)으로 세팅 해 주며, 서버 응답 일시를 생성하여 넣어 준다.
     * @throws Exception 
     */
    public static EAIMessage makeResponseMessage(byte[] recvData) throws Exception {
        byte[] respData = new byte [321];
        System.arraycopy(recvData, 0, respData, 0, 320);
        respData[320] = 0x1f;
        EAIMessage responseMsg = new EAIMessage();
        responseMsg.unmarshall(respData);

        //수신 서버 응답 일시
        responseMsg.setField20(FormatUtil.getToday("yyyyMMddHHmmssSS"));
        //처리 결과 : 정상
        responseMsg.setField23("1");
        return responseMsg;
    }

    /**
     * 2004. 9. 10. 이종원 작성
     * @param recvData
     * 설명: eai수신 메세지로 부터 응답 메세지를 생성하여 리턴
     * 처리결과를 에러(9)으로 세팅 해 주며, 서버 응답 일시를 생성하여 넣어 준다.
     * @throws Exception 
     */
    public static EAIMessage makeErrorResponseMessage(byte[] recvData) throws Exception {
        EAIMessage responseMsg = makeResponseMessage(recvData);
        responseMsg.setField23("9");
        return responseMsg;
    }

    /**
     * 수신한 데이터와 오류 메시지를 받아서
     * 수신한 데이터의 EAI 헤더를 제외한 부분에 오류 메시지를 세팅하여 
     * 응답 데이터를 리턴합니다.
     * 
     * @param recvData EAI 헤더를 포함하는 수신 데이터
     * @param errorMessage 오류 메시지
     * @return 오류 메시지가 세팅된 EAI 응답 데이터
     * @since 2004.09.17
     * @author Helexis
     */
    public static byte[] makeFormatErrorResponseMessage(byte[] recvData,
            String errorMessage) {
        recvData = EAIMessage.setSpecificField(recvData, 20, FormatUtil
                .getToday("yyyyMMddHHmmssSS").getBytes());
        recvData = EAIMessage.setSpecificField(recvData, 23, "9".getBytes());

        errorMessage = FormatUtil.rPadding(errorMessage, recvData.length - 321,
                ' ');
        byte[] bErrorMessage = errorMessage.getBytes();
        System.arraycopy(bErrorMessage, 0, recvData, 320, bErrorMessage.length);

        return recvData;
    }//end of makeFormatErrorResponseMessage()

    public static void main(String[] args) {
        byte[] tb = new byte [400];
        tb[399] = 0x1f;
        tb = makeFormatErrorResponseMessage(tb, "극악무도한 오류발생");

        System.out.println(StringUtil.printHexString(tb, 20));
    }//end of main()

}