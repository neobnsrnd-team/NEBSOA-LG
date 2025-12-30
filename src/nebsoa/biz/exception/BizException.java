/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.biz.exception;

import nebsoa.common.exception.UserException;
import nebsoa.common.util.DataMap;
import nebsoa.spiderlink.exception.MessageException;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 비지니스 로직 수행 중 발생하는 예외상황에 대한 Exception 클래스 입니다.
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
 * $Log: BizException.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:35  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:27  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:29  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.3  2008/09/10 10:15:50  ejkim
 * dataMap인자로 받는 생성자에 dataMap초기화
 *
 * Revision 1.2  2008/08/06 02:13:02  isjoo
 * dataMap 인자로 받는 생성자 추가
 *
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.5  2008/05/21 02:51:25  김재범
 * *** empty log message ***
 *
 * Revision 1.4  2008/04/24 00:43:06  김재범
 * *** empty log message ***
 *
 * Revision 1.3  2008/03/18 10:46:24  김은정
 * *** empty log message ***
 *
 * Revision 1.2  2008/03/18 04:12:34  김은정
 * constructor추가 (targetURL)
 *
 * Revision 1.1  2008/01/22 05:58:35  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:55  안경아
 * *** empty log message ***
 *
 * Revision 1.5  2006/12/06 08:55:30  김성균
 * *** empty log message ***
 *
 * Revision 1.4  2006/08/01 01:41:36  김성균
 * 오류코드 처리
 *
 * Revision 1.3  2006/07/31 05:12:11  김성균
 * 기관오류코드 추가
 *
 * Revision 1.2  2006/06/17 10:37:45  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class BizException extends UserException {
    
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 4062279525686791278L;
    private String targetURL;
    private DataMap dataMap;
    
    public DataMap getDataMap(){
    	return this.dataMap;
    }
    
    public BizException() {    
    }
    
    /**
     * @param errorCode
     */
    public BizException(String errorCode) {
        super(errorCode, nebsoa.common.exception.ExceptionConstants.BIZ_EXCEPTION);
    }
    
    /**
     * @param errorCode
     * @param msg
     */
    public BizException(String errorCode, String msg) {
        super(errorCode, msg);
    }
    
    /**
     * @param errorCode
     * @param msg
     * @param targetURL
     */
    public BizException(String errorCode, String msg, String targetURL ) {
        super(errorCode, msg);
        this.targetURL = targetURL; 
        if(targetURL == null) this.targetURL = "";
    }

    /**
     * @param ex
     */
    public BizException(Throwable ex) {
        super(ex);
    }
    /* 
     * =============================================
     * DataMap을 인자로 받는 생성자 추가 
     * MessageException으로 부터 넘어온 Map 정보를 알기위해.
     * =============================================
     * /
     
    /**
     * @param errorCode
     */
    public BizException(String errorCode, DataMap dataMap) {
        this(errorCode, nebsoa.common.exception.ExceptionConstants.BIZ_EXCEPTION);
        this.dataMap = dataMap;
    }
    
    /**
     * @param errorCode
     * @param msg
     */
    public BizException(String errorCode, String msg, DataMap dataMap) {
        this(errorCode, msg);
        this.dataMap = dataMap;
        
    }
    
    /**
     * @param errorCode
     * @param msg
     * @param targetURL
     */
    public BizException(String errorCode, String msg, String targetURL, DataMap dataMap) {
    	this(errorCode, msg, targetURL);
        this.dataMap = dataMap;
    }

    /**
     * @param ex
     */
    public BizException(Throwable ex, DataMap dataMap) {
        this(ex);
        this.dataMap = dataMap;
    }
    
    /**
     * 기관에서 발생시킨 에러코드를 리턴한다.
     * @return 기관 에러코드
     */
    public String getOrgErrorCode(){
        String orgErrorCode = null;
        MessageException me = null;
        if (getCause() != null && getCause() instanceof MessageException) {
            me = (MessageException) getCause();
            orgErrorCode = me.getOrgErrorCode();
        }
        return orgErrorCode;
    }
    
    public String getTargetURL(){
    	return targetURL;
    }
}
