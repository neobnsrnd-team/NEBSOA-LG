/*
 * Spider Framework
 *
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 *
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.biz.base;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import nebsoa.biz.exception.BizException;
import nebsoa.common.Context;
import nebsoa.common.error.ErrorManager;
import nebsoa.common.exception.AuthFailException;
import nebsoa.common.exception.DBException;
import nebsoa.common.exception.LoginException;
import nebsoa.common.exception.ParamException;
import nebsoa.common.exception.SpiderException;
import nebsoa.common.exception.SysException;
import nebsoa.common.exception.UserException;
import nebsoa.common.jdbc.DBManager;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.DataMap;
import nebsoa.common.util.FormatUtil;
import nebsoa.common.util.PropertyManager;
import nebsoa.common.util.StringUtil;
import nebsoa.spiderlink.exception.MessageException;

/*******************************************************************************
 * <pre>
 * 1.설명
 * 오류가 발생했을 경우 처리를 한다.
 *
 * 2.사용법
 *
 * &lt;font color=&quot;red&quot;&gt;
 * 3.주의사항
 * &lt;/font&gt;
 *
 * @author $Author: cvs $
 * @version
 * *****************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 *
 * $Log: ErrorManageBiz.java,v $
 * Revision 1.1  2018/01/15 03:39:51  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:03  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.2  2009/11/13 12:46:38  jglee
 * stackTraceToString 추가
 *
 * Revision 1.1  2008/11/18 11:27:25  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:28  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.2  2008/09/10 07:46:51  jglee
 * ERROR_HIS_INSERT_SQL public 처리(트랜잭션 rollback시 사용)
 *
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:34  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:54  안경아
 * *** empty log message ***
 *
 * Revision 1.4  2007/08/17 12:12:29  최수종
 * 오류핸들러맵핑정보 캐쉬 저장 처리
 *
 * Revision 1.3  2007/07/08 05:06:20  최수종
 * 오류핸들러 컬럼 추가(HANDLE_APP_FILE)
 *
 * Revision 1.2  2007/06/20 10:41:46  최수종
 * 오류 핸들러 관련 수정 및 추가
 *
 * Revision 1.1  2007/06/07 10:51:01  김성균
 * *** empty log message ***
 *
 *
 *
 * </pre>
 ******************************************************************************/
public class ErrorManageBiz extends BaseBiz {

    private static int MAX_LENGTH = PropertyManager.getIntProperty("default", "ERROR_TRACE_MAX_LENGTH", 4000);

	public static final String ERROR_HIS_INSERT_SQL =
        "\r\n INSERT INTO FWK_ERROR_HIS (                "
       +"\r\n    ERROR_CODE, ERROR_SER_NO, CUST_USER_ID, "
       +"\r\n    ERROR_MESSAGE, ERROR_OCCUR_DTIME, ERROR_URL, ERROR_TRACE)       "
       +"\r\n VALUES ( ?, ?, ?, ?, ?, ?, ?)                    ";



	protected DataMap execute(DataMap map) throws BizException {

		LogManager.debug("@@@@@ ErrorManageBiz start time => "
				+ FormatUtil.getToday("yyyy-MM-dd hh:mm:ss"));

		try {
			String errorTime = null;
			String errorCode = null;
			String errorMessage = null;
			String errorTitle = null;

			String trxSerNo = null;
			String userId = null;

			Context ctx = map.getContext();
			if (ctx != null) {
				trxSerNo = ctx.getTrxSerNo();
				userId = ctx.getUserId();
			}

			errorTime = map.getString("$ERROR_TIME$");
			Throwable e = (Throwable) map.get("$ERROR_EXCEPTION$");

			LogManager.debug("$ERROR_TIME$ = " + errorTime);
			LogManager.debug("$ERROR_EXCEPTION$ = " + e.toString());

			errorMessage = e.getMessage();
			if (errorMessage == null) {
				errorMessage = e.toString();
			}

			if (e instanceof LoginException) {
				errorTitle = "로그인 에러";
				return map;
			} else if (e instanceof AuthFailException) {
				errorTitle = "권한 인증 실패";
				errorMessage = errorMessage + " | " + ctx.getUri();
			} else if (e instanceof NullPointerException) {
				errorTitle = "시스템 장애 (Null)";
			} else if (e instanceof UserException) {
				errorTitle = "다음 사항을 확인하세요";
			} else if (e instanceof ParamException) {
				errorTitle = "필수 입력 정보 누락";
			} else if (e instanceof DBException) {
				errorTitle = "시스템 장애(DB)";
			} else if (e instanceof SysException) {
				errorTitle = "시스템 장애";
			} else if (e instanceof SQLException) {
				errorTitle = "시스템 장애(DB)";
			} else if (e instanceof ClassCastException) {
				errorTitle = "ClassCastException:Application의 버전이나, 타입 다름.";
			} else if (e instanceof RuntimeException) {
				errorTitle = "Application Runtime오류.";
			} else if (e instanceof Exception) {
				errorTitle = "다음 사항을 확인하세요";
			} else {
				errorTitle = "시스템 장애";
			}

			if (e instanceof SpiderException) {
				Throwable cause = e.getCause();
				if (cause == null) {
					LogManager.debug("Nested exception 없을 경우 : " + e);
					errorCode = ((SpiderException) e).getErrorCode();
				} else if (cause instanceof SpiderException) {
					LogManager.debug("Nested exception 있을 경우 : " + cause);
					errorCode = ((SpiderException) cause).getErrorCode();
				} else {
					LogManager
							.debug("Nested exception 있지만 SpiderException 아닌 경우 : "
									+ cause);
					errorCode = ((SpiderException) e).getErrorCode();
				}
				LogManager
						.debug("### SpiderException errorCode = " + errorCode);
			}

			if (e instanceof UserException) {
				MessageException me = null;
				if (e.getCause() != null
						&& e.getCause() instanceof MessageException) {
					me = (MessageException) e.getCause();
					String orgId = me.getOrgId();
					String trxId = me.getTrxId();
					String orgErrorCode = me.getOrgErrorCode();
					String orgErrorMessage = me.getErrorMessage();

					if (!StringUtil.isNull(orgErrorCode)) {
						errorCode = orgErrorCode;
						errorMessage = orgErrorMessage;
					}
					if (!StringUtil.isNull(orgId)) {
						errorMessage = errorMessage + " | " + orgId;
					}
					if (!StringUtil.isNull(trxId)) {
						errorMessage = errorMessage + " | " + trxId;
					}
					LogManager.debug("### MessageException errorCode = "
							+ errorCode);
				}
			}

			if (StringUtil.isNull(errorCode)) {
				errorCode = PropertyManager.getProperty("default",
						"DEFAULT_ERROR_CODE", "FRS99999");
			}

			if (StringUtil.isNull(trxSerNo)) {
				trxSerNo = "NO_DATA";
			}

			if (StringUtil.isNull(userId)) {
				userId = "NO_DATA";
			}

			if (errorMessage.getBytes().length > 500) {
				byte[] msg = errorMessage.getBytes();
				errorMessage = new String(msg, 0, 500);
			}

			LogManager
					.info(" ############## [실시간 저장 오류정보] ###################");
			LogManager.info(" ### USER_ID = " + userId);
			LogManager.info(" ### TRX_SER_NO = " + trxSerNo);
			LogManager.info(" ### ERROR_TIME = " + errorTime);
			LogManager.info(" ### ERROR_CODE = " + errorCode);
			LogManager.info(" ### ERROR_MESSAGE = " + errorMessage);
			LogManager
					.info(" #####################################################");

			Object[] param = { errorCode, trxSerNo, userId, errorMessage,
					errorTime };

			DBManager.executePreparedUpdate(ERROR_HIS_INSERT_SQL, param);

			// 오류별 처리 핸들러 호출 실행 가능 여부
			boolean isErrorSave = PropertyManager.getBooleanProperty("default",
					"ERROR_HANDLER_CALL", "FALSE");
			if (isErrorSave) {
				// 오류별 처리 핸들러 호출
				DataMap errorMap = new DataMap();
				errorMap.put("errorCode", errorCode); // 오류코드
				errorMap.put("trxSerNo", trxSerNo); // 오류 일련번호
				ErrorManager.getInstance().execute(errorMap);
			}

		} catch (Exception e) {
			LogManager.error("ERROR", e.toString(), e);
		}

		LogManager.debug("@@@@@ ErrorManageBiz end time => "
				+ FormatUtil.getToday("yyyy-MM-dd hh:mm:ss"));

		return map;
	}

	/**
	 *
	 * 작성일자 : 2008. 12. 12 작성자 : 김영석 설명 : Throwable의 trace를 추출하여 DB컬럼 길이에 맞게 자른
	 * 스트링을 리턴
	 *
	 * @param th
	 * @return trace문자열
	 */
	public static String stackTraceToString(Throwable th) {
		Throwable rootTh = ExceptionUtils.getRootCause(th);
		ByteArrayOutputStream b = null;
		PrintStream p = null;
		String allStackTrace = null;
		StringBuffer stackTrace = new StringBuffer();
		byte[] tempTrace = null;

		// root cause가 있으면 Throwable을 ROOT Throwable로 교체
		th = rootTh == null ? th : rootTh;

		try {
			b = new ByteArrayOutputStream();
			p = new PrintStream(b);
			th.printStackTrace(p);
			allStackTrace = b.toString().trim();

			/*
			 * 'spider.', 'keb.', 'jsp_servlet.' 로 시작되는 패키지 에러만 로그로 남긴다.
			 * 2007.11.05 추가
			 */
			String[] temp = StringUtils.splitPreserveAllTokens(allStackTrace,
					'\n');
			for (int i = 0; i < temp.length; i++) {
				if (i == 0) {
					stackTrace.append(temp[i]);// .append("\n");
				} else if (temp[i].indexOf("nebsoa.") > -1) {
					stackTrace.append(temp[i]);// .append("\n");
				} else if (temp[i].indexOf("lgt.") > -1) {
					stackTrace.append(temp[i]);// .append("\n");
				} else if (temp[i].indexOf("jsp_servlet.") > -1) {
					stackTrace.append(temp[i]).append("\n");
				}
			}
			byte[] byteTrace = stackTrace.toString().getBytes();
			int length = byteTrace.length > MAX_LENGTH ? MAX_LENGTH
					: byteTrace.length;
			tempTrace = new byte[length];
			System.arraycopy(byteTrace, 0, tempTrace, 0, length);
		} catch (Exception ex) {
			LogManager.error(ex);
		} finally {
			try {
				if (p != null)
					p.close();
				if (b != null)
					b.close();
			} catch (Exception ept) {
				LogManager.error(ept);
			}
		}
		return (new String(tempTrace)).trim();
	}
}
