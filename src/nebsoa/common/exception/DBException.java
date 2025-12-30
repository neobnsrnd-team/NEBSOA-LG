/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
 
package nebsoa.common.exception; 

import java.sql.SQLException;

import nebsoa.common.log.LogManager;
import nebsoa.common.util.DbTypeUtil;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 프로그램상의 database exception을 표현하는 클래스
 * 
 * 
 * 
 * 2.사용법
 * SQLEXCEPTION과 유사 하게,또는 SQL EXCEPTION의 WRAPPER로  사용 합니다.
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
 * $Log: DBException.java,v $
 * Revision 1.1  2018/01/15 03:39:50  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:41  cvs
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
 * Revision 1.1  2008/08/04 08:54:50  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.5  2008/02/29 08:00:09  오재훈
 * 특정 에러가 아닐경우 에러코드를 FRD99999으로 리턴하게 수정
 *
 * Revision 1.4  2008/02/26 08:41:22  오재훈
 * *** empty log message ***
 *
 * Revision 1.3  2008/01/31 05:20:56  오재훈
 * *** empty log message ***
 *
 * Revision 1.2  2008/01/25 09:06:14  오재훈
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:18  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:04  안경아
 * *** empty log message ***
 *
 * Revision 1.9  2006/11/16 12:37:28  오재훈
 * DataBase 연결장애시(DataSource를 얻지 못했을경우)의 에러코드 던짐
 *
 * Revision 1.8  2006/08/17 12:08:21  김성균
 * NestedException 처리변경
 *
 * Revision 1.7  2006/07/28 05:36:04  이종원
 * ExceptionTracer적용
 *
 * Revision 1.6  2006/07/14 01:48:45  이종원
 * *** empty log message ***
 *
 * Revision 1.5  2006/07/05 17:05:22  이종원
 * 오류코드 정의
 *
 * Revision 1.4  2006/06/19 04:10:16  안경아
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class DBException extends SysException {
	
	/**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1987898644684920379L;
    
    public final static String DUPPLICATE="중복 되는 값이 있습니다.";
	public final static String PARENT_NOT_FOUND="참조하려는 값이 존재 하지 않습니다.";
	public final static String CHILD_EXIST="해당 데이타를 참조하는 값이 있으므로 삭제(수정) 할 수 없습니다.";   
    public final static String TOO_LARGE="입력한 값이 너무 큽니다.";
    
    public final static String DUPPLICATE_CODE="FRD00001";
    public final static String PARENT_NOT_FOUND_CODE="FRD02291";
    public final static String CHILD_EXIST_CODE="FRD02292";
    public final static String TOO_LARGE_CODE="FRD12899";
    
    public final static String ERROR_ETC="FRD99999";
	
	int errCode = 0;
	String errorCode = null;
	String errMessage;
	String dbName;
	
	protected boolean dupplicate = false;
	protected boolean parentNotFount = false;
	protected boolean childExist = false;
	protected boolean tooLarge = false;
	
	public DBException(){
		errMessage = "";
	}

	public DBException(String msg){
		errMessage = msg;
	}

	
	public DBException(String errCode , String msg){
		errorCode = errCode;
		errMessage = msg;
	}

	public DBException(String errCode , String msg , String dbname){
		errorCode = errCode;
		errMessage = msg;
		dbName = dbname;
	}

		
	public DBException (SQLException e){

		this(null , e);        
	}

	public DBException (String dbname , SQLException e) {
        LogManager.info("DB_ERROR","SQL ERROR => DBNAME : "+dbname+", ERRORCODE : "+e.getErrorCode()+", ERRORMESSAGE : "+e.getMessage()+"##");
		dbName = dbname;
        errMessage = e.getMessage();
        errorCode = e.getErrorCode()+"";
        
        if( DbTypeUtil.ORACLE == DbTypeUtil.getDbType(dbName)){//중복
            if (errMessage.indexOf("ORA-00001") >= 0) {
            	dupplicate = true;  
            	errorCode = DUPPLICATE_CODE;
            	errMessage = DUPPLICATE;
            }else if (errMessage.indexOf("ORA-02291") >= 0) { //foreign key not found
            	parentNotFount = true;
            	errorCode = PARENT_NOT_FOUND_CODE;
            	errMessage = PARENT_NOT_FOUND;
            }else if (errMessage.indexOf("ORA-02292") >= 0) { // cann't delete because child exist
            	childExist = true;
            	errorCode = CHILD_EXIST_CODE;
            	errMessage = CHILD_EXIST;
            }else if (errMessage.indexOf("ORA-12899") >= 0) { // cann't INSERT because too large Value
            	tooLarge = true;
            	errorCode = TOO_LARGE_CODE;
            	errMessage = "입력된 값이 너무 큽니다:"+ errMessage.substring(
                        errMessage.lastIndexOf(".")+1)
                        .replaceFirst("actual","입력된 값의 길이")
                        .replaceFirst("maximum","최대입력가능길이");
            }
        } else if( DbTypeUtil.MY_SQL == DbTypeUtil.getDbType(dbName)){

        } else if( DbTypeUtil.MS_SQL == DbTypeUtil.getDbType(dbName)){
        	
        } else if( DbTypeUtil.SYBASE == DbTypeUtil.getDbType(dbName)){
        	
        } else if( DbTypeUtil.ALTIBASE == DbTypeUtil.getDbType(dbName)){
        	
        } else if( DbTypeUtil.DB2 == DbTypeUtil.getDbType(dbName)){
            if ("-803".equals(errorCode) ) {
            	dupplicate = true;  
            	errorCode = DUPPLICATE_CODE;
            	errMessage = DUPPLICATE;
//            }else if (errMessage.indexOf("ORA-02291") >= 0) { //foreign key not found
//            	parentNotFount = true;
//            	errorCode = PARENT_NOT_FOUND_CODE;
//            	errMessage = PARENT_NOT_FOUND;
//            }else if (errMessage.indexOf("ORA-02292") >= 0) { // cann't delete because child exist
//            	childExist = true;
//            	errorCode = CHILD_EXIST_CODE;
//            	errMessage = CHILD_EXIST;
            }else if ("-302".equals(errorCode)) { // cann't INSERT because too large Value
            	tooLarge = true;
            	errorCode = TOO_LARGE_CODE;
            	errMessage = "입력된 값이 너무 큽니다.";
            }	
        }
        
        if(!errorCode.equals(DUPPLICATE_CODE) && !errorCode.equals(PARENT_NOT_FOUND_CODE) && 
        		!errorCode.equals(CHILD_EXIST_CODE) && !errorCode.equals(TOO_LARGE_CODE) ) {
        	errorCode = ERROR_ETC;
        }


        
	}

	public String getMessage() {
		return errMessage;
	}
	
	public String getErrorCode(){
		if (errorCode != null){
        	return errorCode;
        } else {
            return ERROR_ETC;
        }
	}


	public boolean isChildExist() {
		return childExist;
	}


	public boolean isDupplicate() {
		return dupplicate;
	}


	public boolean isParentNotFount() {
		return parentNotFount;
	}


	public boolean isTooLarge() {
		return tooLarge;
	}
}