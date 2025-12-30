/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.jdbc;

import java.sql.ResultSet;

import nebsoa.common.collection.DataSet;
import nebsoa.common.exception.DBException;

/*******************************************************************
 * <pre>
 * 1.설명 
 * <code>DBResultSet</code>은 Select문의 처리결과를 저장하는 클래스
 * JDBC의 ResultSet Class와 사용법이 거의 유사합니다.
 * DBHandler의 DBResultSet executeQuery(String sql, Object data[])와
 * DBResultSet executeQuery(String sql) 메소드는 입력된 쿼리문을 수행한 후
 * 결과를 <code>DBResultSet</code>형으로 리턴합니다.<br>
 * 
 * 2.사용법
 * <code>DBResultSet</code>은 쿼리의 결과를 모두 메모리에 저장합니다. 그러므로 쿼리의 결과가 매우
 * 큰 쿼리에는 적합하지 않을 수 있습니다.
 * 
 * <code>DBResultSet</code>의 getXXX()의 종류와 DB Data 타입과의 관계는 다음과 같습니다.<br>
 * <table width="600" border="1">
 *   <tr>
 *     <td width="118">&nbsp;</td>
 *     <td width="65">
 *      <div align="center">CHAR</div>
 *     </td>
 *     <td width="67">
 *       <div align="center">VACHAR<br>
 *         VARCHAR2</div>
 *     </td>
 *     <td width="42">
 *       <div align="center">NUMBER</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">DATE</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">LONG</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">RAW</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">LONG RAW</div>
 *     </td>
 *   </tr>
 *   <tr>
 *     <td width="118">GetString</td>
 *     <td width="65">
 *       <div align="center">○</div>
 *     </td>
 *     <td width="67">
 *       <div align="center">○</div>
 *     </td>
 *     <td width="42">
 *       <div align="center">○</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">○</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">○</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">△</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">△</div>
 *     </td>
 *   </tr>
 *   <tr>
 *     <td width="118">getObject</td>
 *     <td width="65">
 *       <div align="center">○</div>
 *     </td>
 *     <td width="67">
 *       <div align="center">○</div>
 *     </td>
 *     <td width="42">
 *       <div align="center">○</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">○</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">○</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">△</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">△</div>
 *     </td>
 *   </tr>
 *   <tr>
 *     <td width="118">getByte</td>
 *     <td width="65">
 *       <div align="center">△</div>
 *     </td>
 *     <td width="67">
 *       <div align="center">△</div>
 *     </td>
 *     <td width="42">
 *       <div align="center">○</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">Ｘ</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">△</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">Ｘ</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">Ｘ</div>
 *     </td>
 *   </tr>
 *   <tr>
 *     <td width="118">getShort</td>
 *     <td width="65">
 *       <div align="center">△</div>
 *     </td>
 *     <td width="67">
 *       <div align="center">△</div>
 *     </td>
 *     <td width="42">
 *       <div align="center">○</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">Ｘ</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">△</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">Ｘ</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">Ｘ</div>
 *     </td>
 *   </tr>
 *   <tr>
 *     <td width="118">getInt</td>
 *     <td width="65">
 *       <div align="center">△</div>
 *     </td>
 *     <td width="67">
 *       <div align="center">△</div>
 *     </td>
 *     <td width="42">
 *       <div align="center">○</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">Ｘ</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">△</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">Ｘ</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">Ｘ</div>
 *     </td>
 *   </tr>
 *   <tr>
 *     <td width="118">getLong</td>
 *     <td width="65">
 *       <div align="center">△</div>
 *     </td>
 *     <td width="67">
 *       <div align="center">△</div>
 *     </td>
 *     <td width="42">
 *       <div align="center">○</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">Ｘ</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">△</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">Ｘ</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">Ｘ</div>
 *     </td>
 *   </tr>
 *   <tr>
 *     <td width="118">getFloat</td>
 *     <td width="65">
 *       <div align="center">△</div>
 *     </td>
 *     <td width="67">
 *       <div align="center">△</div>
 *     </td>
 *     <td width="42">
 *       <div align="center">○</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">Ｘ</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">△</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">Ｘ</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">Ｘ</div>
 *     </td>
 *   </tr>
 *   <tr>
 *     <td width="118">getDouble</td>
 *     <td width="65">
 *       <div align="center">△</div>
 *     </td>
 *     <td width="67">
 *       <div align="center">△</div>
 *     </td>
 *     <td width="42">
 *       <div align="center">○</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">Ｘ</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">△</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">Ｘ</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">Ｘ</div>
 *     </td>
 *   </tr>
 *   <tr>
 *     <td width="118">getBigDecimal</td>
 *     <td width="65">
 *       <div align="center">△</div>
 *     </td>
 *     <td width="67">
 *       <div align="center">△</div>
 *     </td>
 *     <td width="42">
 *       <div align="center">○</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">Ｘ</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">△</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">Ｘ</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">Ｘ</div>
 *     </td>
 *   </tr>
 *   <tr>
 *     <td width="118">getBoolean</td>
 *     <td width="65">
 *       <div align="center">△</div>
 *     </td>
 *     <td width="67">
 *       <div align="center">△</div>
 *     </td>
 *     <td width="42">
 *       <div align="center">○</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">Ｘ</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">△</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">Ｘ</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">Ｘ</div>
 *     </td>
 *   </tr>
 *   <tr>
 *     <td width="118">getDate</td>
 *     <td width="65">
 *       <div align="center">△</div>
 *     </td>
 *     <td width="67">
 *       <div align="center">△</div>
 *     </td>
 *     <td width="42">
 *       <div align="center">Ｘ</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">○</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">△</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">Ｘ</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">Ｘ</div>
 *     </td>
 *   </tr>
 *   <tr>
 *     <td width="118">getTimestamp</td>
 *     <td width="65">
 *       <div align="center">△</div>
 *     </td>
 *     <td width="67">
 *       <div align="center">△</div>
 *     </td>
 *     <td width="42">
 *       <div align="center">Ｘ</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">○</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">△</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">Ｘ</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">Ｘ</div>
 *     </td>
 *   </tr>
 *   <tr>
 *     <td width="118">getBytes</td>
 *     <td width="65">
 *       <div align="center">Ｘ</div>
 *     </td>
 *     <td width="67">
 *       <div align="center">Ｘ</div>
 *     </td>
 *     <td width="42">
 *       <div align="center">Ｘ</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">Ｘ</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">○</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">○</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">○</div>
 *     </td>
 *   </tr>
 *   <tr>
 *     <td width="118">getBinaryStream</td>
 *     <td width="65">
 *       <div align="center">Ｘ</div>
 *     </td>
 *     <td width="67">
 *       <div align="center">Ｘ</div>
 *     </td>
 *     <td width="42">
 *       <div align="center">Ｘ</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">Ｘ</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">○</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">○</div>
 *     </td>
 *     <td width="64">
 *       <div align="center">○</div>
 *     </td>
 *   </tr>
 * </table>
 * <br>
 * ○ - 권장 사항, △ - 가능하지만 권장사항은 아님, Ｘ -사용 불가
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
 * $Log: DBResultSet.java,v $
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
 * Revision 1.5  2006/06/19 04:10:16  안경아
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class DBResultSet extends DataSet{
	public DBResultSet(ResultSet rset) throws DBException {
		super(rset);
	}
	public DBResultSet(ResultSet rset, int skipCount) throws DBException {
		super(rset, skipCount);
	}
}
