package nebsoa.service.exception;

import java.io.Serializable;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 서비스 엔진에서 발생시키는 에러코드를 정의하는 클래스 입니다.
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
 * $Log: ServiceErrorCode.java,v $
 * Revision 1.1  2018/01/15 03:39:49  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:35  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:23  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:27  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.2  2008/10/29 09:29:33  youngseokkim
 * 에러코드 수정
 *
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.5  2008/06/23 05:28:32  김영석
 * 서비스 접근허용자 에러코드 추가
 *
 * Revision 1.4  2008/05/19 04:45:22  김영석
 * 서비스 옵션 체크 로직 변경
 *
 * Revision 1.3  2008/03/13 02:05:25  김영석
 * 로그인 상태 체크 로직 추가
 *
 * Revision 1.2  2008/03/03 04:23:26  김성균
 * 사용하지 않는 에러코드 삭제
 *
 * Revision 1.1  2008/01/22 05:58:28  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.3  2008/01/07 03:44:49  김성균
 * 오류코드 추가
 *
 * Revision 1.2  2008/01/04 02:50:49  김성균
 * 로그인필수여부 체크기능 추가
 *
 * Revision 1.1  2007/12/31 04:35:34  최수종
 * nebsoa.service 패키지 추가
 *
 * </pre>
 ******************************************************************/
public class ServiceErrorCode implements Serializable {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -3708854058653802140L;

	/**
     * 서비스제공시간 체크 오류코드 
     */
    public static final String TIME_CHECK_ERROR = "FRU00013";
    
    /**
     * 영업일 체크 오류코드
     */
    public static final String BIZ_DAY_CHECK_ERROR = "FRC00001";
    
	/**
     * 타행상태체크시 은행코드가 널일경우 오류코드 
     */
    public static final String BANK_CODE_ERROR = "FRC00002";

	/**
     * 해당은행코드의 상태를 찾을 수 없는 경우 또는 타행상태체크시 STABLE_YN 이 'N'일 경우 오류코드
     */
    public static final String BANK_STATUS_CHECK_ERROR = "FRC00003";

	/**
     * 로그인 상태가 아닐 경우 오류코드 
     */
    public static final String LOGIN_CHECK_ERROR = "FRC00004";

	/**
     * 공휴일 테이블에 금일 데이터 미존재시 오류코드 
     */
    public static final String BIZDAY_NOT_FOUND_ERROR = "FRC00005";
    
    /**
     * 서비스 미존재시 오류코드
     */
    public static final String SERVICE_NOT_FOUND_ERROR = "FRC00006";

    /**
     * 사용할 수 없는 서비스 오류코드 
     */
    public static final String SERVICE_ENABLE_CHECK_ERROR = "FRC00007";

    /**
     * DataMap에서 전자서명 메시지 키를 찾을 수 없는 서비스 오류코드
     */
    public static final String SIGNED_MSG_KEY_NOT_FOUND_ERROR = "FRC00008";

    /**
     * DataMap에서 전자서명 메시지 값이 NULL일 경우 서비스 오류코드
     */
    public static final String SIGNED_MSG_KEY_NULL_ERROR = "FRC00009";

    /**
     * 중지된 서비스 오류코드
     */
    public static final String SERVICE_STOPPED_ERROR = "FRC00010";
    
	/**
	 * 기타 Runtime exception
	 */
	public static final String RUNTIME_ERROR = "FRM99999";
    
}
