/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.jdbc.types;

/*******************************************************************
 * <pre>
 * 1.설명 
 * JDBC드라이버의 Oracle Type 상수들을 정의한 클래스입니다.
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
 * $Log: OracleTypes.java,v $
 * Revision 1.1  2018/01/15 03:39:54  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:40  cvs
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
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:34  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:37:45  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2006/11/27 06:25:04  최수종
 * *** empty log message ***
 *
 * Revision 1.1  2006/11/27 06:13:11  최수종
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public abstract class OracleTypes
{
	/*
	 * 본 클래스는 oracle 10g 드라이버의  OracleTypes.java와 동일합니다.
	 */
	
    public OracleTypes()
    {
    }

    public static final int BIT = -7;
    public static final int TINYINT = -6;
    public static final int SMALLINT = 5;
    public static final int INTEGER = 4;
    public static final int BIGINT = -5;
    public static final int FLOAT = 6;
    public static final int REAL = 7;
    public static final int DOUBLE = 8;
    public static final int NUMERIC = 2;
    public static final int DECIMAL = 3;
    public static final int CHAR = 1;
    public static final int VARCHAR = 12;
    public static final int LONGVARCHAR = -1;
    public static final int DATE = 91;
    public static final int TIME = 92;
    public static final int TIMESTAMP = 93;
    /**
     * @deprecated Field TIMESTAMPNS is deprecated
     */
    public static final int TIMESTAMPNS = -100;
    public static final int TIMESTAMPTZ = -101;
    public static final int TIMESTAMPLTZ = -102;
    public static final int INTERVALYM = -103;
    public static final int INTERVALDS = -104;
    public static final int BINARY = -2;
    public static final int VARBINARY = -3;
    public static final int LONGVARBINARY = -4;
    public static final int ROWID = -8;
    public static final int CURSOR = -10;
    public static final int BLOB = 2004;
    public static final int CLOB = 2005;
    public static final int BFILE = -13;
    public static final int STRUCT = 2002;
    public static final int ARRAY = 2003;
    public static final int REF = 2006;
    public static final int OPAQUE = 2007;
    public static final int JAVA_STRUCT = 2008;
    public static final int JAVA_OBJECT = 2000;
    public static final int PLSQL_INDEX_TABLE = -14;
    public static final int BINARY_FLOAT = 100;
    public static final int BINARY_DOUBLE = 101;
    public static final int NULL = 0;
    public static final int NUMBER = 2;
    public static final int RAW = -2;
    public static final int OTHER = 1111;
    public static final int FIXED_CHAR = 999;
    public static final int DATALINK = 70;
    public static final int BOOLEAN = 16;
    private static final String _Copyright_2003_Oracle_All_Rights_Reserved_ = null;
    public static final boolean TRACE = false;
    public static final boolean PRIVATE_TRACE = false;
    public static final String BUILD_DATE = "051228";

}


