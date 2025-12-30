/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.jdbc;

import java.sql.*;

/*******************************************************************
 * <pre>
 * 1.설명 
 * <code>Null</code>은 DB에  널값을 넣기 위해 사용하는 Dao 클래스
 * DBManager에서 PreparedStatement.setNull를 위해 사용 됩니다.
 * 
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
 * $Log: Null.java,v $
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
 * Revision 1.1  2008/08/04 08:54:54  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:29  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:37:44  안경아
 * *** empty log message ***
 *
 * Revision 1.3  2006/09/06 03:58:20  최수종
 * *** empty log message ***
 *
 * Revision 1.2  2006/06/19 04:10:16  안경아
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class Null {
	public int type;
	
	/**
	DB CHAR형의 컬럼에 NULL값
	*/
	public static final Null CHAR = new Null("CHAR");
	
	/**
	DB의 VARCHAR2형의 컬럼에 NULL값
	*/
	public static final Null VARCHAR2 = new Null("VARCHAR2");

	/**
	DB의 VARCHAR형의 컬럼에 NULL값
	*/
	public static final Null VARCHAR = new Null("VARCHAR");
	
	/**
	DB의 NUMBER형의 컬럼에 NULL값
	*/
	public static final Null NUMBER = new Null("NUMBER");
	
	/**
	DB의 DATE형의 컬럼에 NULL값
	*/
	public static final Null DATE = new Null("DATE");
	
	/**
	DB의 LONG형의 컬럼에 NULL값
	*/
	public static final Null LONG = new Null("LONG");
	
	/**
	DB의 RAW형의 컬럼에 NULL값
	*/
	public static final Null RAW = new Null("RAW");
	
	/**
	DB의 LONG RAW형의 컬럼에 NULL값
	*/
	public static final Null LONG_RAW = new Null("LONG RAW");

	/**
	other type NULL값
	*/
	public static final Null OTHER = new Null("");
	
	public Null(String type) {
		if(type.equalsIgnoreCase("VARCHAR2") || type.equalsIgnoreCase("VARCHAR")||type.equalsIgnoreCase("String")) this.type = Types.VARCHAR;
		else if(type.equalsIgnoreCase("CHAR")) this.type = Types.CHAR;
		else if(type.equalsIgnoreCase("NUMBER")) this.type = Types.NUMERIC;
		else if(type.equalsIgnoreCase("DATE")|| type.equalsIgnoreCase("Date")|| type.equalsIgnoreCase("date")) this.type = Types.DATE;
		else if(type.equalsIgnoreCase("LONG")) this.type = Types.LONGVARCHAR;
		else if(type.equalsIgnoreCase("RAW")) this.type = Types.VARBINARY;
		else if(type.equalsIgnoreCase("LONG RAW")) this.type = Types.LONGVARBINARY;
		else if(type.equalsIgnoreCase("VARCHAR")) this.type = Types.VARCHAR;
		else this.type = Types.OTHER;
	}
}
