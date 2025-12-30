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
 * Procedure호출시 각각의 리턴 결과에 대한 Sql Type 정보를 담고 있는 클래스입니다.
 * 
 * 2.사용법
 * 
 * 		private static final String sql = "{ call procedureTest(?, ?, ?, ?, ?) }";		
 * 
 *		ArrayList paramList = new ArrayList();
 *		
 *		paramList.add("userId");
 *		paramList.add("userName");
 *		paramList.add(new SqlTypes("STATUS_CODE", OracleTypes.NUMBER)); // 프로시져 결과의 컬럼명 및 데이타 타입(number)을 설정
 *		paramList.add(new SqlTypes("STATUS_NAME", OracleTypes.VARCHAR)); // 프로시져 결과의 컬럼명 및 데이타 타입(varchar)을 설정
 *		paramList.add(new SqlTypes("RESULT", OracleTypes.CURSOR)); // 프로시져 결과의 컬럼명 및 데이타 타입(cursor)을 설정
 *		
 *		Map resultMap = DBHandler.executeCallableObjectQuery(dataMap.getContext(), sql, paramList);
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
 * $Log: SqlTypes.java,v $
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
 * Revision 1.2  2006/12/08 07:48:16  최수종
 * 설명 추가
 *
 * Revision 1.1  2006/11/27 06:13:11  최수종
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public class SqlTypes {
	
	private int type;  // 프로시져 호출후 결과값의 sql type
	
	private String parameterName;  // 프로시져 호출후 결과값의 key명
	
	public static final String NO_NAME = "NO_NAME";
	
	/**
	 * 생성자
	 * @param parameterName
	 * @param type
	 */
	public SqlTypes(String parameterName, int type) 
	{
		this.type = type;
		this.parameterName = parameterName;
	}
	
	/**
	 * 생성자
	 * @param type
	 */
	public SqlTypes(int type) 
	{
		this.type = type;
		this.parameterName = NO_NAME;
	}	

	/**
	 * parameterName 값을 리턴합니다.
	 * @return
	 */
	public String getParameterName()
	{
		return parameterName;
	}
	
	/**
	 * type 값을 리턴합니다.
	 * @return
	 */
	public int getType()
	{
		return type;
	}

}

