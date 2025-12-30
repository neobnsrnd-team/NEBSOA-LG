/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.exception;


/*******************************************************************
 * <pre>
 * 1.설명 
 * 거래 수행 중 거래가 준재하지 않을 경우 발생하는 예외상황에 대한 Exception 클래스 입니다.
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
 * $Log: TrxNotFoundException.java,v $
 * Revision 1.1  2018/01/15 03:39:51  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:54  cvs
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
 * Revision 1.1  2008/09/19 07:02:18  youngseokkim
 * 거래제한관련 클래스 추가
 *
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class TrxNotFoundException extends MessageSysException {
    
    /**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 5085100133359678698L;
	private static final String ERROR_CODE = "FRM00012";
    
    /**
     * @param serviceId
     */
    public TrxNotFoundException(String trxId) {
        super(ERROR_CODE, "거래 정보를 찾을 수 없습니다.(TrxId:"+trxId+")");
    }
}
