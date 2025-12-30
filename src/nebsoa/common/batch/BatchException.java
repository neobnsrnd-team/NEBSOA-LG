/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
package nebsoa.common.batch;

import nebsoa.common.exception.SysException;
/*******************************************************************
 * <pre>
 * 1.설명 
 * 배치 처리 중 발생 한 오류에 대한 정보를 나태내는 Exception클래스 
 * SysException클래스를 상속 받아 만들었으므로 사용법은 SysException과 동일
 * 
 * 2.사용법
 * throw new BatchException("errorcode", "error_message");
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author 이종원
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 
 * $$Log: BatchException.java,v $
 * $Revision 1.1  2018/01/15 03:39:50  cvs
 * $*** empty log message ***
 * $
 * $Revision 1.1  2016/04/15 02:22:47  cvs
 * $neo cvs init
 * $
 * $Revision 1.1  2011/07/01 02:13:51  yshong
 * $*** empty log message ***
 * $
 * $Revision 1.1  2008/11/18 11:27:24  김성균
 * $*** empty log message ***
 * $
 * $Revision 1.1  2008/11/18 11:01:27  김성균
 * $LGT Gateway를 위한 프로젝트로 분리
 * $
 * $Revision 1.1  2008/08/04 08:54:53  youngseokkim
 * $*** empty log message ***
 * $
 * $Revision 1.1  2008/01/22 05:58:28  오재훈
 * $패키지 리펙토링
 * $
 * $Revision 1.1  2007/11/26 08:38:57  안경아
 * $*** empty log message ***
 * $
 * $Revision 1.4  2006/09/28 05:40:44  오재훈
 * $재실행 여부 = N이면 배치 기준일에 실행되었으면 Exception발생
 * $
 * $Revision 1.3  2006/09/12 09:04:31  오재훈
 * $*** empty log message ***
 * $
 * $Revision 1.2  2006/09/09 10:31:15  이종원
 * $최초작성
 * $$
 * </pre>
 ******************************************************************/
public class BatchException extends SysException {
    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -7248170465656769443L;

	public BatchException(String errorCode, String reason){
        super(errorCode,reason);
    }
}
