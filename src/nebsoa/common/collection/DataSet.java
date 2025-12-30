/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.collection;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import nebsoa.common.exception.DBException;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.DataMap;
import nebsoa.common.util.PropertyManager;
import nebsoa.common.util.StringUtil;

/*******************************************************************
 * <pre>
 * 1.설명 
 * <code>DataSet</code>은 Select문의 처리결과를 저장하는 클래스
 * JDBC의 ResultSet Class와 사용법이 유사합니다.
 * DBHandler의 DBResultSet executeQuery(String sql, Object data[])와
 * DBResultSet executeQuery(String sql) 메소드는 입력된 쿼리문을 수행한 후
 * 결과를 <code>DBResultSet</code>형으로 리턴합니다.<br>
 * 
 * 2.사용법
 * <code>DataSet</code>은 쿼리의 결과를 모두 메모리에 저장합니다. 
 * 그러므로 쿼리의 결과가 매우 큰 쿼리에는 적합하지 않을 수 있습니다.
 * DataSet은 DataSet.next()문을 수행하면 다음 행으로 커서가 이동 된다. 따라서 DataSet의 
 * 결과를 while(DataSet.next())문으로 사용 후 다시 첫번째 행부터  사용하려면 initRow()메소드를
 * 호출하여 커서를 맨 처음으로 이동시켜야 한다.
 * 
 * 
 * <code>DataSet</code>의 getXXX()의 종류와 DB Data 타입과의 관계는 다음과 같습니다.<br>
 * <table width="600" border="1">
 *   <tr>
 *     <td width="118">&nbsp;</td>
 *     <td width="65">
 *       <div align="center">CHAR</div>
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
 * $Log: DataSet.java,v $
 * Revision 1.1  2018/01/15 03:39:52  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:08  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:25  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:28  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:51  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.9  2008/07/25 01:29:08  김재범
 * *** empty log message ***
 *
 * Revision 1.8  2008/06/12 11:12:49  김은정
 * getNVLString의 return값에 trim()추가
 *
 * Revision 1.7  2008/04/24 00:46:06  오재훈
 * getNVLstring메소드 추가.
 *
 * Revision 1.6  2008/03/11 00:11:42  오재훈
 * Append할 경우 Map타입일 경우 추가
 *
 * Revision 1.5  2008/03/10 12:34:27  오재훈
 * MapDataSet을 Append할 경우 추가
 *
 * Revision 1.4  2008/03/10 03:43:18  오재훈
 * toString()수정.
 *
 * Revision 1.3  2008/02/25 07:53:35  김승희
 * 테스트용 main 메소드 삭제
 *
 * Revision 1.2  2008/02/25 07:51:31  김승희
 * addColumn 메소드 추가
 *
 * Revision 1.1  2008/01/22 05:58:18  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.3  2007/12/05 01:35:24  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2007/11/26 08:39:05  안경아
 * *** empty log message ***
 *
 * Revision 1.22  2007/07/23 05:21:51  김성균
 * AJAX 지원 가능하도록 수정
 *
 * Revision 1.21  2006/10/26 02:44:12  김성균
 * *** empty log message ***
 *
 * Revision 1.20  2006/10/20 13:45:22  이종원
 * html로 debug string을 return 하도록 toHtmlTable 메소드 추가
 *
 * Revision 1.19  2006/10/11 09:41:13  오재훈
 * DataSet자체만 clone하면 DataSet이 가지고 있는 내용들은 clone되지 않는 버그 수정
 *
 * Revision 1.18  2006/09/20 03:35:57  이종원
 * debug
 *
 * Revision 1.17  2006/09/11 12:07:28  이종원
 * debug 수정
 *
 * Revision 1.16  2006/08/28 09:19:27  김성균
 * *** empty log message ***
 *
 * Revision 1.15  2006/08/17 06:43:30  김성균
 * *** empty log message ***
 *
 * Revision 1.14  2006/08/12 02:40:05  김성균
 * 주석 추가
 *
 * Revision 1.13  2006/07/19 05:04:50  김성균
 * appendData() 추가
 *
 * Revision 1.12  2006/07/13 00:46:33  오재훈
 * *** empty log message ***
 *
 * Revision 1.11  2006/07/10 05:10:59  김성균
 * *** empty log message ***
 *
 * Revision 1.10  2006/06/21 05:16:34  김승희
 * *** empty log message ***
 *
 * Revision 1.9  2006/06/19 04:10:16  안경아
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class DataSet implements Serializable, Cloneable {

    /**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -4945221090749911725L;

	/**
	 * sort method의 오름차순 정렬 지정자
	 * 
	 * @see #sort
	 */
	public static final int ASC = 1;
	
    /**
	 * sort method의 내림차순 정렬 지정자
	 * 
	 * @see #sort
	 */
	public static final int DESC = -1;

    protected ArrayList data;

	protected String columnNames[];

	protected int columnCount;

	/**
	 * 현재 커서의 위치 
	 */
	protected int row = -1;

    /**
     * <code>DataSet</code> 생성시 data size check수행여부 모드 설정
     * 최대 레코드 건수  모니터링 여부 : monitor 프로퍼티에 DATA_SIZE_MONITOR_MODE 를 true주면 
     * 모니터링이 시작 됨.
     */
    public static boolean isMonitorMode() {
         return PropertyManager.getBooleanProperty("monitor",
                    "DATA_SIZE_MONITOR_MODE", "false");
    }
    
    /**
     * Default constructor
     */
    public DataSet() {}
    
	/**
	 * @param columNames
	 * @throws DataSetException
	 */
	public DataSet(String[] columNames) throws DataSetException{
        this(columNames, new ArrayList());
	}
	
	/**
	 * @param columNames
	 * @param columValueList
	 * @throws DataSetException
	 */
	public DataSet(String[] columNames, ArrayList columValueList) throws DataSetException{
		if(columNames==null){
			throw new DataSetException("DataSet 생성 오류입니다. - column name이 null입니다.");
		}
		
		this.columnNames = columNames;
		this.data = columValueList;
		this.columnCount = this.columnNames.length;
	}
	
    /**
    <code>DataSet</code> 생성자 입니다.
    JDBC ResultSet에서 Row를 얻어옵니다.

    @param  rset  JDBC ResultSet
    @exception  DBException  Database작업이 실패했을 경우.
    */
    public DataSet(ResultSet rset) throws DBException {
    	/*
    	 * skip할 행의 갯수에 대한 매개변수 추가
    	 * 2006.04.26 - sjChoi 수정
    	 */
    	this.setResultSet(rset, 0);
    }  
    
    /**
    <code>DataSet</code> 생성자 입니다.
    JDBC ResultSet에서 특정 Row들만 얻어옵니다.
    단. 스킵할 ROW가 있다면(skipCount > 0), 현재의 ResultSet타입이 ResultSet.TYPE_FORWARD_ONLY(default타입) 가 아니어야 한다.
    스킵할 ROW가 있을 경우, ResultSet 타입이 default타입이라면 Exception 발생.
    
    @param  rset  JDBC ResultSet
    @param  skipCount  Skip할 행의 갯수. 0 이면 첫 행부터.(skip할 행 없음)
    @exception  DBException  Database작업이 실패했을 경우.
    */
    public DataSet(ResultSet rset, int skipCount) throws DBException {
    	/*
    	 * skip할 행의 갯수에 대한 매개변수 추가
    	 * 2006.04.26 - sjChoi 추가
    	 */
    	this.setResultSet(rset, skipCount);
    }     
    
    
    /**
     * 상속을 위한 생성자
     */
    protected DataSet(DataSet dataSet){
    	this(dataSet.columnNames, dataSet.data);
    }
    
	protected void setResultSet(ResultSet rset, int skipCount) throws DBException {
		if (rset == null)
			throw new DBException("ResultSet is null");
		ResultSetMetaData metaData = null;
		try {
			metaData = rset.getMetaData();
			columnCount = metaData.getColumnCount();
			columnNames = new String[columnCount];
			
			for (int i = 0; i < columnCount; i++) {
				columnNames[i] = metaData.getColumnName(i + 1);
			}

			data = new ArrayList(10);

            /*
             * skip할 행의 갯수에 대한 매개변수 추가
             * 단. 현재의 ResultSet타입이 ResultSet.TYPE_FORWARD_ONLY 가 아니어야 한다.
             * default ResultSet사용시 Exception 발생.
             * 
             * 2006.04.26 - sjChoi 수정
             */
            if(skipCount > 0)
            {
            	// 해당 row로 cursor 이동
            	rset.absolute(skipCount);
            }			
			
			while (rset.next()) {
				Object columnObjects[] = new Object[columnCount];
				for (int i = 0; i < columnCount; i++) {
					columnObjects[i] = rset.getObject(i + 1);
					if(columnObjects[i] instanceof Clob){
						columnObjects[i] = ((Clob)columnObjects[i]).getSubString(1, (int)((Clob)columnObjects[i]).length());
					}
				}
				data.add(columnObjects);
			}
			// if(data.size()==0) throw new NoDataException();
			if (isMonitorMode()) {
				// long dataSize = getDataSize();
				// if(dataSize > 64000)

				if (data.size() > PropertyManager.getIntProperty("monitor",
						"RESULTSET_MAX_RECORD_COUNT")) {
					LogManager.error("MONITOR","Too many rows " + data.size(),
							new Exception("Too many rows"));
				}
			}
		} catch (SQLException e) {
			LogManager.error(e.getMessage());
			throw new DBException(e);
		} finally {

		}
	}
    
	/**
	 * DataSet자체만 clone하면 DataSet이 가지고 있는 내용들은 clone되지 않는 버그 수정 2006.10.11 오재훈
	 */
    public Object clone() {
//    	DataSet rs = null;
//		try {
//			Object obj = super.clone();
//			rs = (DataSet) obj;
//			rs.initRow();
//		} catch (CloneNotSupportedException e) {
//			LogManager.error("DataSet을 Clone하던중 에러 발생:" + e.getMessage());
//		}
    	
    	DataSet rs = new DataSet((String[])this.columnNames.clone(),(ArrayList)this.data.clone());
		return rs;
	}

    /**
	 * 검색 결과의 크기를 구합니다.
	 * 
	 * @return 검색 결과의 크기
	 */
	public long getDataSize() {
		if (data == null)
			return 0;
		return data.size();
		// try {
		// ByteArrayOutputStream bo = new ByteArrayOutputStream();
		// ObjectOutputStream oo = new ObjectOutputStream(bo);
		// oo.writeObject(data);
		// oo.flush();
		// return bo.size();
		// } catch(Exception e) {
		// return -1;
		// }
	}

    /**
	 * 지정된 컬럼의 이름을 구합니다.
	 * 
	 * @param index
	 *            컬럼 인덱스
	 * @return 지정된 컬럼의 이름
	 * @exception DataSetException
	 *                적절하지 않은 열인덱스가 입력되었을 경우
	 */
	public String getColumnName(int index) throws DataSetException {
		if (columnNames == null)
			throw new DataSetException("DataSet 생성안됨");
		String tempData = null;

		try {
			tempData = columnNames[index - 1];
		} catch (IndexOutOfBoundsException e) {
			throw new DataSetException("부적합한 열 인덱스::"+e);
		}

		return tempData;
	}

    /**
	 * 검색 결과의 컬럼의 수를 구합니다.
	 * 
	 * @return 컬럼의 수
	 */
	public int getColumnCount() {
		return columnCount;
	}

    /**
	 * 현재 커서를 다음 위치로 이동합니다.
	 * 
	 * @return true - 올바른 커서위치, false - 잘 못된 커서 위치
	 */
	public boolean next() {
		if (data == null)
			return false;

		try {
			if ((row + 1) >= data.size())
				return false;
			else {
				row++;
				return true;
			}
		} catch (Exception e) {
			return false;
		}
	}

    /**
	 * 현재 커서를 이전 위치로 이동합니다.
	 * 
	 * @return true - 올바른 커서위치, false - 잘 못된 커서 위치
	 */
    public boolean previous() {
        if(data == null) return false;

        if(row <= 0) return false;
        else {
            row--;
            return true;
        }
    }

    /**
	 * 현재 커서를 최초 행으로 이동합니다.
	 * 
	 * @return true - 올바른 커서위치, false - 잘 못된 커서 위치
	 */
	public boolean first() {
		if (data == null)
			return false;
		row = 0;
		return true;
	}

    /**
	 * 현재 커서를 마지막 행으로 이동합니다.
	 * 
	 * @return true - 올바른 커서위치, false - 잘 못된 커서 위치
	 */
    public boolean last() {
        if(data == null) return false;

        try {
            row = data.size() - 1;

            return true;
        } catch(Exception e) {
            return false;
        }
    }

    /**
    현재 커서의 위치에서 상태적인 위치로 이동합니다.
    @return  index  상태적인 위치(음수 - 위로, 양수 -  아래로)
    @return  true - 올바른 커서위치, false - 잘 못된 커서 위치
    */
    public boolean relative(int index) {
        if(data == null) return false;

        try {
            if(((row + index) >= data.size()) &&
                ((row + index) < 0)) return false;
            else {
                row += index;

                return true;
            }
        } catch(Exception e) {
            return false;
        }
    }

    /**
    커서를 절대 위치로 이동합니다.
    @return  index  절대적인 위치. 1부터 시작합니다.
    @return  true - 올바른 커서위치, false - 잘 못된 커서 위치
    */
    public boolean absolute(int index) {
        index -= 1;
        if(data == null) return false;

        try {
            if((index >= data.size()) ||
                (index < 0)) return false;
            else {
                row = index;

                return true;
            }
        } catch(Exception e) {
            return false;
        }
    }

    /**
	 * 커서를 초기 위치로 이동합니다.
	 */
	public void initRow() {
		row = -1;
	}

    /**
	 * 커서를 초기 위치로 이동합니다.
	 */
	public boolean beforeFirst() {
		row = -1;
		return true;
	}

    /**
	 * 현재 커서의 위치를 리턴합니다. 초기위치는 1입니다.
	 * 
	 * @return true - 현재의 커서위치
	 */
	public int getRow() {
		return row + 1;
	}

    /**
	 * 검색결과 행의 수를 리턴합니다.
	 * 
	 * @return 검색 결과 갯수
	 */
	public int getRowCount() {
		if (data == null)
			return 0;
		return data.size();
	}

	/**
	 * 검색결과 행의 수를 리턴합니다.
	 * 
	 * @return 검색 결과 갯수
	 */
	public int size() {
		if (data == null)
			return 0;
		return data.size();
	}

    /**
	 * 지정된 컬럼 이름으로 컬럼 인덱스를 얻습니다.
	 * 
	 * @param columnName
	 *            컬럼 이름
	 * @return 컬럼 인덱스
	 * @exception DataSetException
	 *                잘못된 컬럼 인덱스가 입력되었을 경우
	 */
    public int findColumn(String columnName) throws DataSetException {
        if(columnNames == null) throw new DataSetException("DataSet 생성안됨");

        for(int i = 0; i < columnCount; i++)
            if(columnName.equalsIgnoreCase(columnNames[i])) return i + 1;

        return -1;
    }

    Object[] findObjectArray()  throws DataSetException {
        try {
            return (Object[]) data.get(row);
        } catch(NullPointerException e) {
            throw new DataSetException("DataSet이 생성되지 않았음");
        } catch(IndexOutOfBoundsException e) {
            throw new DataSetException("검색결과가 없거나, DataSet.next가 호출되지 않았음");
        }
    }

    /**
    현재 행의 지정된 컬럼의 값을 Object형으로 얻습니다.
    @param index 컬럼 인덱스
    @return  컬럼 값
    @exception DataSetException   잘못된 컬럼 인덱스가 입력되었을 경우
    */
    public Object getObject(int index) throws DataSetException {
        Object temp[] = findObjectArray();

        try {
            return temp[index - 1];
        } catch(IndexOutOfBoundsException e) {
            throw new DataSetException("부적합한 열 인덱스::"+e);
        }
    }

    /**
    현재 행의 지정된 컬럼의 값을 Object형으로 얻습니다.
    @param columnName 컬럼 이름
    @return  컬럼 값
    @exception DBException   잘못된 컬럼 인덱스가 입력되었을 경우
    */
    public Object getObject(String columnName) throws DataSetException {
        int index = findColumn(columnName);
        if(index == -1) {
            //LogManager.info("부적합한 열 이름"+columnName);
            throw new DataSetException("부적합한 열 이름::"+columnName);
        }

        return getObject(index);
    }

    /**
    현재 행의 지정된 컬럼의 값을 String형으로 얻습니다.
    @param index 컬럼 인덱스
    @return  컬럼 값
    @exception DBException   잘못된 컬럼 인덱스가 입력되었을 경우
    */
    public String getString(int index) throws DataSetException {
        Object temp[] = findObjectArray();
        String tempStr = null;

        try {
            Object obj = temp[index - 1];
            if(obj instanceof byte[]) tempStr = new String((byte[]) obj);
            else tempStr = obj.toString();
        } catch(NullPointerException e) {
        } catch(IndexOutOfBoundsException e) {
            throw new DataSetException("부적합한 열 인덱스::"+e);
        }

        return tempStr;
    }

    public String getString(int index,String format) throws DataSetException {
        String str = getString(index);
        return StringUtil.formatDate(format,str);
    }

    /**
    색을 입혀서 현재 행의 지정된 컬럼의 값을 String형으로 얻습니다.
    @param index 컬럼 인덱스
    @return  컬럼 값
    @exception DBException   잘못된 컬럼 인덱스가 입력되었을 경우
    */
    public String getBoldString(int index,String match) throws DataSetException {
        return StringUtil.bold(getString(index),match);
    }

    /**
    색을 입혀서 현재 행의 지정된 컬럼의 값을 String형으로 얻습니다.
    @param index 컬럼 인덱스
    @return  컬럼 값
    @exception DBException   잘못된 컬럼 인덱스가 입력되었을 경우
    */
    public String getBoldString(int index,String format,String match) throws DataSetException {
        return StringUtil.bold(getString(index,format),match);
    }


    /**
     * 현재 행의 지정된 컬럼의 값을 String형으로 얻습니다.
     * 
     * @param columnName 컬럼 이름
     * @return 컬럼 값
     * @exception DataSetException
     *                잘못된 컬럼 인덱스가 입력되었을 경우
     */
    public String getString(String columnName) throws DataSetException {
        int index = findColumn(columnName);
        if (index == -1) {
            LogManager.info("부적합한 열 이름===>" + columnName);
            throw new DataSetException("부적합한 열 이름===>" + columnName);
        }

        return getString(index);
    }
    
	/**
	 * 작성일자 : 2008. 07. 18 
	 * 작성자 : kim jae bum
	 * 설명 : IBK 요청으로 NVL된 String 리턴 메소드 제공.
	 * @param paramName
	 * @return
	 */
	public String getstr(String paramName){
		return getNVLString(paramName);
	}	
    
    /**
     * 현재 행의 지정된 컬럼의 값을 String형으로 얻습니다.
     * null일경우 ""을 리턴합니다.
     * @param columnName 컬럼 이름
     * @return 컬럼 값
     * @exception DataSetException
     *                잘못된 컬럼 인덱스가 입력되었을 경우
     */
    public String getNVLString(String columnName) throws DataSetException {
    	String tempStr = getString(columnName);
    	if(tempStr == null || tempStr.trim().length() == 0 ) tempStr = "";
    	//trim추가 요청 2008-06-12.
    	return tempStr.trim();    
    }

    public String getString(String columnName,String dateFormat) throws DataSetException {
        String str = getString(columnName);
        return StringUtil.formatDate(dateFormat,str);
    }
    /**
    색을 입혀서 현재 행의 지정된 컬럼의 값을 String형으로 얻습니다.
    @param index 컬럼 인덱스
    @return  컬럼 값
    @exception DataSetException   잘못된 컬럼 인덱스가 입력되었을 경우
    */
    public String getBoldString(String columnName,String match) throws DataSetException {
        return StringUtil.bold(getString(columnName),match);
    }

    /**
    색을 입혀서 현재 행의 지정된 컬럼의 값을 String형으로 얻습니다.
    @param index 컬럼 인덱스
    @return  컬럼 값
    @exception DataSetException   잘못된 컬럼 인덱스가 입력되었을 경우
    */
    public String getBoldString(String columnName,String format,String match) throws DataSetException {
        return StringUtil.bold(getString(columnName,format),match);
    }
    /**
    현재 행의 지정된 컬럼의 값을 byte형으로 얻습니다.
    @param index 컬럼 인덱스
    @return  컬럼 값
    @exception DataSetException   잘못된 컬럼 인덱스가 입력되었을 경우, 잘못된 숫자 포맷, 오버플로우
    */
    public byte getByte(int index) throws DataSetException {
        Object temp[] = findObjectArray();
        byte tempByte = (byte) 0;

        try {
            Object obj = temp[index - 1];
            if(obj instanceof java.sql.Timestamp || obj instanceof byte[])
                throw new DataSetException("부적합한 열 유형::"+obj.getClass().getName());

            String value = obj.toString().trim();
            double dvalue = Double.parseDouble(value);
            if(Byte.MAX_VALUE < dvalue || Byte.MIN_VALUE > dvalue)
                throw new DataSetException("숫자 오버플로우");

            tempByte = (byte) dvalue;
        } catch(NullPointerException e) {
        } catch(IndexOutOfBoundsException e) {
            throw new DataSetException("부적합한 열 인덱스::"+e);
        } catch(NumberFormatException e) {
            throw new DataSetException("내부 표기로 변환할 수 없음");
        }

        return tempByte;
    }

    /**
    현재 행의 지정된 컬럼의 값을 byte형으로 얻습니다.
    @param columnName 컬럼 이름
    @return  컬럼 값
    @exception DataSetException   잘못된 컬럼 인덱스가 입력되었을 경우, 잘못된 숫자 포맷, 오버플로우
    */
    public byte getByte(String columnName) throws DataSetException {
        int index = findColumn(columnName);
        if(index == -1) throw new DataSetException("부적합한 열 이름===>"+columnName);

        return getByte(index);
    }

    /**
    현재 행의 지정된 컬럼의 값을 short형으로 얻습니다.
    @param index 컬럼 인덱스
    @return  컬럼 값
    @exception DataSetException   잘못된 컬럼 인덱스가 입력되었을 경우, 잘못된 숫자 포맷, 오버플로우
    */
    public short getShort(int index) throws DataSetException {
        Object temp[] = findObjectArray();
        short tempShort = (short) 0;

        try {
            Object obj = temp[index - 1];
            if(obj instanceof java.sql.Timestamp || obj instanceof byte[])
                throw new DataSetException("부적합한 열 유형::"+obj.getClass().getName());

            String value = obj.toString().trim();
            double dvalue = Double.parseDouble(value);
            if(Short.MAX_VALUE < dvalue || Short.MIN_VALUE > dvalue)
                throw new DataSetException("숫자 오버플로우");

            tempShort = (short) dvalue;
        } catch(NullPointerException e) {
        } catch(IndexOutOfBoundsException e) {
            throw new DataSetException("부적합한 열 인덱스 : "+index);
        } catch(NumberFormatException e) {
            throw new DataSetException("내부 표기로 변환할 수 없음");
        }

        return tempShort;
    }

    /**
    현재 행의 지정된 컬럼의 값을 short형으로 얻습니다.
    @param columnName 컬럼 이름
    @return  컬럼 값
    @exception DataSetException   잘못된 컬럼 인덱스가 입력되었을 경우, 잘못된 숫자 포맷, 오버플로우
    */
    public short getShort(String columnName) throws DataSetException {
        int index = findColumn(columnName);
        if(index == -1){
            //LogManager.info("부적합한 열 이름"+columnName);
            throw new DataSetException("부적합한 열 이름===>"+columnName);
        }

        return getShort(index);
    }

    /**
    현재 행의 지정된 컬럼의 값을 int형으로 얻습니다.
    @param index 컬럼 인덱스
    @return  컬럼 값
    @exception DataSetException   잘못된 컬럼 인덱스가 입력되었을 경우, 잘못된 숫자 포맷, 오버플로우
    */
    public int getInt(int index) throws DataSetException {
        Object temp[] = findObjectArray();
        int tempInt = 0;

        try {
            Object obj = temp[index - 1];
            if(obj instanceof java.sql.Timestamp || obj instanceof byte[])
                throw new DataSetException("부적합한 열 유형::"+obj.getClass().getName());

            String value = obj.toString().trim();
            double dvalue = Double.parseDouble(value);
            if(Integer.MAX_VALUE < dvalue || Integer.MIN_VALUE > dvalue)
                throw new DataSetException("숫자 오버플로우");

            tempInt = (int) dvalue;
        } catch(NullPointerException e) {
        } catch(IndexOutOfBoundsException e) {
            throw new DataSetException("부적합한 열 인덱스: "+index);
        } catch(NumberFormatException e) {
            throw new DataSetException("내부 표기로 변환할 수 없음");
        }

        return tempInt;
    }

    /**
    현재 행의 지정된 컬럼의 값을 int형으로 얻습니다.
    @param columnName 컬럼 이름
    @return  컬럼 값
    @exception DataSetException   잘못된 컬럼 인덱스가 입력되었을 경우, 잘못된 숫자 포맷, 오버플로우
    */
    public int getInt(String columnName) throws DataSetException {
        int index = findColumn(columnName);
        if(index == -1){
            //LogManager.info("부적합한 열 이름"+columnName);
            throw new DataSetException("부적합한 열 이름===>"+columnName);
        }
        return getInt(index);
    }

    /**
    현재 행의 지정된 컬럼의 값을 long형으로 얻습니다.
    @param index 컬럼 인덱스
    @return  컬럼 값
    @exception DataSetException   잘못된 컬럼 인덱스가 입력되었을 경우, 잘못된 숫자 포맷, 오버플로우
    */
    public long getLong(int index) throws DataSetException {
        Object temp[] = findObjectArray();
        long tempLong = (long) 0;

        try {
            Object obj = temp[index - 1];
            if(obj instanceof java.sql.Timestamp || obj instanceof byte[])
                throw new DataSetException("부적합한 열 유형::"+obj.getClass().getName());

            String value = obj.toString().trim();
            double dvalue = Double.parseDouble(value);
            if(Long.MAX_VALUE < dvalue || Long.MIN_VALUE > dvalue)
                throw new DataSetException("숫자 오버플로우");

            tempLong = (long) dvalue;
        } catch(NullPointerException e) {
        } catch(IndexOutOfBoundsException e) {
            throw new DataSetException("부적합한 열 인덱스:"+index);
        } catch(NumberFormatException e) {
            throw new DataSetException("내부 표기로 변환할 수 없음");
        }

        return tempLong;
    }

    /**
    현재 행의 지정된 컬럼의 값을 long형으로 얻습니다.
    @param columnName 컬럼 이름
    @return  컬럼 값
    @exception DataSetException   잘못된 컬럼 인덱스가 입력되었을 경우, 잘못된 숫자 포맷, 오버플로우
    */
    public long getLong(String columnName) throws DataSetException {
        int index = findColumn(columnName);
        if(index == -1){
            //LogManager.info("부적합한 열 이름"+columnName);
            throw new DataSetException("부적합한 열 이름===>"+columnName);
        }
        return getLong(index);
    }

    /**
    현재 행의 지정된 컬럼의 값을 float형으로 얻습니다.
    @param index 컬럼 인덱스
    @return  컬럼 값
    @exception DataSetException   잘못된 컬럼 인덱스가 입력되었을 경우, 잘못된 숫자 포맷
    */
    public float getFloat(int index) throws DataSetException {
        Object temp[] = findObjectArray();
        Float tempFloat = Float.valueOf(0f);

        try {
            Object obj = temp[index - 1];
            if(obj instanceof java.sql.Timestamp || obj instanceof byte[])
                throw new DataSetException("부적합한 열 유형::"+obj.getClass().getName());

            tempFloat = Float.valueOf(obj.toString().trim());
        } catch(NullPointerException e) {
        } catch(IndexOutOfBoundsException e) {
            throw new DataSetException("부적합한 열 인덱스:"+index);
        } catch(NumberFormatException e) {
            throw new DataSetException("내부 표기로 변환할 수 없음");
        }

        return tempFloat.floatValue();
    }

    /**
    현재 행의 지정된 컬럼의 값을 float형으로 얻습니다.
    @param columnName 컬럼 이름
    @return  컬럼 값
    @exception DataSetException   잘못된 컬럼 인덱스가 입력되었을 경우, 잘못된 숫자 포맷
    */
    public float getFloat(String columnName) throws DataSetException {
        int index = findColumn(columnName);
        if(index == -1) {
            //LogManager.info("부적합한 열 이름"+columnName);
            throw new DataSetException("부적합한 열 이름===>"+columnName);
        }

        return getFloat(index);
    }

    /**
    현재 행의 지정된 컬럼의 값을 double형으로 얻습니다.
    @param index 컬럼 인덱스
    @return  컬럼 값
    @exception DataSetException   잘못된 컬럼 인덱스가 입력되었을 경우, 잘못된 숫자 포맷
    */
    public double getDouble(int index) throws DataSetException {
        Object temp[] = findObjectArray();
        Double tempDouble = Double.valueOf(0d);

        try {
            Object obj = temp[index - 1];
            if(obj instanceof java.sql.Timestamp || obj instanceof byte[])
                throw new DataSetException("부적합한 열 유형::"+obj.getClass().getName());

            tempDouble = Double.valueOf(obj.toString().trim());
        } catch(NullPointerException e) {
        } catch(IndexOutOfBoundsException e) {
            throw new DataSetException("부적합한 열 인덱스:"+index);
        } catch(NumberFormatException e) {
            throw new DataSetException("내부 표기로 변환할 수 없음");
        }

        return tempDouble.doubleValue();
    }

    /**
    현재 행의 지정된 컬럼의 값을 double형으로 얻습니다.
    @param columnName 컬럼 이름
    @return  컬럼 값
    @exception DataSetException   잘못된 컬럼 인덱스가 입력되었을 경우, 잘못된 숫자 포맷
    */
    public double getDouble(String columnName) throws DataSetException {
        int index = findColumn(columnName);
        if(index == -1) {
            //LogManager.info("부적합한 열 이름"+columnName);
            throw new DataSetException("부적합한 열 이름===>"+columnName);
        }
        return getDouble(index);
    }

    /**
    현재 행의 지정된 컬럼의 값을 BigDecimal형으로 얻습니다.
    @param index 컬럼 인덱스
    @return  컬럼 값
    @exception DataSetException   잘못된 컬럼 인덱스가 입력되었을 경우, 잘못된 숫자 포맷
    */
    public BigDecimal getBigDecimal(int index) throws DataSetException {
        Object temp[] = findObjectArray();
        BigDecimal tempBigDecimal = new BigDecimal((double) 0);

        try {
            Object obj = temp[index - 1];
            if(obj instanceof java.sql.Timestamp || obj instanceof byte[])
                throw new DataSetException("부적합한 열 유형::"+obj.getClass().getName());

            tempBigDecimal = new BigDecimal(obj.toString().trim());
        } catch(NullPointerException e) {
        } catch(IndexOutOfBoundsException e) {
            throw new DataSetException("부적합한 열 인덱스:"+index);
        } catch(NumberFormatException e) {
            throw new DataSetException("내부 표기로 변환할 수 없음");
        }

        return tempBigDecimal;
    }

    /**
    현재 행의 지정된 컬럼의 값을 BigDecimal형으로 얻습니다.
    @param columnName 컬럼 이름
    @return  컬럼 값
    @exception DataSetException   잘못된 컬럼 인덱스가 입력되었을 경우, 잘못된 숫자 포맷
    */
    public BigDecimal getBigDecimal(String columnName) throws DataSetException {
        int index = findColumn(columnName);
        if(index == -1){
            //LogManager.info("부적합한 열 이름"+columnName);
            throw new DataSetException("부적합한 열 이름===>"+columnName);
        }
        return getBigDecimal(index);
    }

    /**
    현재 행의 지정된 컬럼의 값을 boolean형으로 얻습니다.
    @param index 컬럼 인덱스
    @return  컬럼 값
    @exception DataSetException   잘못된 컬럼 인덱스가 입력되었을 경우, 잘못된 포맷
    */
    public boolean getBoolean(int index) throws DataSetException {
        Object temp[] = findObjectArray();
        Boolean tempBoolean = Boolean.FALSE;

        try {
            Object obj = temp[index - 1];
            if(obj instanceof java.sql.Timestamp || obj instanceof byte[])
                throw new DataSetException("부적합한 열 유형::"+obj.getClass().getName());

            int value = (new BigDecimal(obj.toString().trim())).intValue();
            if(value != 0) tempBoolean = Boolean.TRUE;
        } catch(NullPointerException e) {
        } catch(IndexOutOfBoundsException e) {
            throw new DataSetException("부적합한 열 인덱스:"+index);
        } catch(NumberFormatException e) {
            throw new DataSetException("내부 표기로 변환할 수 없음");
        }

        return tempBoolean.booleanValue();
    }

    /**
    현재 행의 지정된 컬럼의 값을 boolean형으로 얻습니다.
    @param columnName 컬럼 이름
    @return  컬럼 값
    @exception DataSetException   잘못된 컬럼 인덱스가 입력되었을 경우, 잘못된 포맷
    */
    public boolean getBoolean(String columnName) throws DataSetException {
        int index = findColumn(columnName);
        if(index == -1){
            //LogManager.info("부적합한 열 이름"+columnName);
            throw new DataSetException("부적합한 열 이름===>"+columnName);
        }

        return getBoolean(index);
    }

    /**
    현재 행의 지정된 컬럼의 값을 java.sql.Timestamp형으로 얻습니다.
    @param index 컬럼 인덱스
    @return  컬럼 값
    @exception DataSetException   잘못된 컬럼 인덱스가 입력되었을 경우, 잘못된 포맷
    */
    public java.sql.Timestamp getTimestamp(int index) throws DataSetException {
        Object temp[] = findObjectArray();
        java.sql.Timestamp tempTimestamp = null;

        try {
            Object obj = temp[index - 1];
            if(obj instanceof java.math.BigDecimal || obj instanceof byte[])
                throw new DataSetException("부적합한 열 유형::"+obj.getClass().getName());

            tempTimestamp = java.sql.Timestamp.valueOf(obj.toString().trim());
        } catch(NullPointerException e) {
        } catch(IndexOutOfBoundsException e) {
            throw new DataSetException("부적합한 열 인덱스:"+index);
        } catch(NumberFormatException e) {
            throw new DataSetException("내부 표기로 변환할 수 없음");
        }

        return tempTimestamp;
    }

    /**
    현재 행의 지정된 컬럼의 값을 java.sql.Timestamp형으로 얻습니다.
    @param columnName 컬럼 이름
    @return  컬럼 값
    @exception DataSetException   잘못된 컬럼 인덱스가 입력되었을 경우, 잘못된 포맷
    */
    public java.sql.Timestamp getTimestamp(String columnName) throws DataSetException {
        int index = findColumn(columnName);
        if(index == -1){
            //LogManager.info("부적합한 열 이름"+columnName);
            throw new DataSetException("부적합한 열 이름::"+columnName);
        }

        return getTimestamp(index);
    }

    /**
    현재 행의 지정된 컬럼의 값을 java.sql.Date형으로 얻습니다.
    @param index 컬럼 인덱스
    @return  컬럼 값
    @exception DataSetException   잘못된 컬럼 인덱스가 입력되었을 경우, 잘못된 포맷
    */
    public java.sql.Date getDate(int index) throws DataSetException {
        Object temp[] = findObjectArray();
        java.sql.Date tempDate = null;

        try {
            Object obj = temp[index - 1];
            if(obj instanceof java.math.BigDecimal || obj instanceof byte[])
                throw new DataSetException("부적합한 열 유형::"+obj.getClass().getName());

            tempDate = java.sql.Date.valueOf(obj.toString().trim());
        } catch(NullPointerException e) {
        } catch(IndexOutOfBoundsException e) {
            throw new DataSetException("부적합한 열 인덱스::"+e);
        } catch(NumberFormatException e) {
            try {
                java.sql.Timestamp tempTimestamp = java.sql.Timestamp.valueOf(temp[index - 1].toString().trim());
                tempDate = new java.sql.Date(tempTimestamp.getTime());
            } catch(NumberFormatException e2) {
                throw new DataSetException("내부 표기로 변환할 수 없음");
            }
        }

        return tempDate;
    }


    /**
    현재 행의 지정된 컬럼의 값을 java.sql.Date형으로 얻습니다.
    @param columnName 컬럼 이름
    @return  컬럼 값
    @exception DataSetException   잘못된 컬럼 인덱스가 입력되었을 경우, 잘못된 포맷
    */
    public java.sql.Date getDate(String columnName) throws DataSetException {
        int index = findColumn(columnName);
        if(index == -1){
            //LogManager.info("부적합한 열 이름"+columnName);
            throw new DBException("부적합한 열 이름::"+columnName);
        }

        return getDate(index);
    }

    /**
    현재 행의 지정된 컬럼의 값을 byte[]형으로 얻습니다.
    @param index 컬럼 인덱스
    @return  컬럼 값
    @exception DataSetException   잘못된 컬럼 인덱스가 입력되었을 경우
    */
    public byte[] getBytes(int index) throws DataSetException {
        Object temp[] = findObjectArray();
        byte tempBytes[] = null;

        try {
            Object obj = temp[index - 1];
            if(obj instanceof java.math.BigDecimal ||
                obj instanceof java.sql.Timestamp)
                    throw new DataSetException("부적합한 열 유형::"+obj.getClass().getName());

            if(obj instanceof java.lang.String) {
                tempBytes = ((String) obj).getBytes();
            } else {
                tempBytes = new byte[((byte[]) obj).length];
                System.arraycopy((byte[]) obj, 0, tempBytes, 0, tempBytes.length);
            }
        } catch(NullPointerException e) {
        } catch(IndexOutOfBoundsException e) {
            throw new DataSetException("부적합한 열 인덱스::"+e);
        }

        return tempBytes;
    }

    /**
    현재 행의 지정된 컬럼의 값을 byte[]형으로 얻습니다.
    @param columnName 컬럼 이름
    @return  컬럼 값
    @exception DataSetException   잘못된 컬럼 인덱스가 입력되었을 경우
    */
    public byte[] getBytes(String columnName) throws DataSetException {
        int index = findColumn(columnName);
        if(index == -1){
            //LogManager.info("부적합한 열 이름"+columnName);
            throw new DataSetException("부적합한 열 이름::"+columnName);
        }

        return getBytes(index);
    }


    /**
    현재 행의 지정된 컬럼의 값을 InputStream형으로 얻습니다.
    @param index 컬럼 인덱스
    @return  컬럼 값
    @exception DataSetException   잘못된 컬럼 인덱스가 입력되었을 경우
    */
    public InputStream getBinaryStream(int index) throws DataSetException {
        Object temp[] = findObjectArray();
        InputStream tempIs = null;

        try {
            Object obj = temp[index - 1];
            if(obj instanceof java.math.BigDecimal ||
                obj instanceof java.sql.Timestamp)
                    throw new DataSetException("부적합한 열 유형::"+obj.getClass().getName());

            byte tempBytes[];
            if(obj instanceof java.lang.String) {
                tempBytes = ((String) obj).getBytes();
            } else {
                tempBytes = (byte[]) obj;
            }

            tempIs = new ByteArrayInputStream(tempBytes);
        } catch(NullPointerException e) {
        } catch(IndexOutOfBoundsException e) {
            throw new DataSetException("부적합한 열 인덱스::"+e);
        }

        return tempIs;
    }

    /**
    현재 행의 지정된 컬럼의 값을 InputStream형으로 얻습니다.
    @param columnName 컬럼 이름
    @return  컬럼 값
    @exception DataSetException   잘못된 컬럼 인덱스가 입력되었을 경우
    */
    public InputStream getBinaryStream(String columnName) throws DataSetException {
        int index = findColumn(columnName);
        if(index == -1){
            //LogManager.info("부적합한 열 이름"+columnName);
            throw new DataSetException("부적합한 열 이름::"+columnName);
        }

        return getBinaryStream(index);
    }

    /**
    지정된 컬럼값의 총합를 구합니다.
    @param columnName 컬럼 이름
    @return  컬럼값들의 총합
    @exception DataSetException   잘못된 컬럼 인덱스가 입력되었을 경우
    */
    public BigDecimal sum(String columnName) throws DataSetException {
        int index = findColumn(columnName);
        if(index == -1){
            //LogManager.info("부적합한 열 이름"+columnName);
            throw new DataSetException("부적합한 열 이름::"+columnName);
        }

        return sum(index);
    }

    /**
    지정된 컬럼값의 총합를 구합니다.
    @param columnIndex 컬럼 인덱스
    @return  컬럼값들의 총합
    @exception DataSetException   잘못된 컬럼 인덱스가 입력되었을 경우
    */
    public BigDecimal sum(int columnIndex) throws DataSetException {
        BigDecimal temp = new BigDecimal(0);

        try {
            for(int i = 0; i < data.size(); i++) {
                Object values[] = (Object []) data.get(i);

                if(values[columnIndex - 1] == null) continue;
                if(values[columnIndex - 1] instanceof BigDecimal)
                    temp = temp.add((BigDecimal) values[columnIndex - 1]);
                else
                    temp = temp.add(new BigDecimal(values[columnIndex - 1].toString()));
            }
        } catch(IndexOutOfBoundsException e) {
            throw new DataSetException("부적합한 열 인덱스::"+e);
        } catch(NumberFormatException e) {
            throw new DataSetException("부적합한 열 타입::"+e);
        }

        return temp;
    }

    /**
    지정된 컬럼값의 평균을 구합니다.
    @param columnName 컬럼 이름
    @return  컬럼값들의 평균
    @exception DataSetException   잘못된 컬럼 인덱스가 입력되었을 경우
    */
    public BigDecimal ave(String columnName) throws DataSetException {
        int index = findColumn(columnName);
        if(index == -1){
            //LogManager.info("부적합한 열 이름"+columnName);
            throw new DataSetException("부적합한 열 이름::"+columnName);
        }

        return ave(index);
    }

    /**
    지정된 컬럼값의 평균을 구합니다.
    @param columnIndex 컬럼 인뎃스
    @return  컬럼값들의 평균
    @exception DataSetException   잘못된 컬럼 인덱스가 입력되었을 경우
    */
    public BigDecimal ave(int columnIndex) throws DataSetException {
        try {
            BigDecimal temp = sum(columnIndex);

            return temp.divide(new BigDecimal(getRowCount()), 10, BigDecimal.ROUND_HALF_UP);
        } catch(ArithmeticException e) {
            throw new DataSetException("0 으로 나눌수 없음.");
        }

    }

    /**
    지정된 컬럼값의 최대값을 구합니다.
    @param columnName 컬럼 이름
    @return  컬럼값들의 최대값
    @exception DataSetException   잘못된 컬럼 인덱스가 입력되었을 경우
    */
    public BigDecimal max(String columnName) throws DataSetException {
        int index = findColumn(columnName);
        if(index == -1){
            //LogManager.info("부적합한 열 이름"+columnName);
            throw new DataSetException("부적합한 열 이름::"+columnName);
        }

        return max(index);
    }

    /**
    지정된 컬럼값의 최대값을 구합니다.
    @param columnIndex 컬럼 인덱스
    @return  컬럼값들의 최대값
    @exception DataSetException   잘못된 컬럼 인덱스가 입력되었을 경우
    */
    public BigDecimal max(int columnIndex) throws DataSetException {
        BigDecimal temp = null;

        try {
            for(int i = 0; i < data.size(); i++) {
                Object values[] = (Object []) data.get(i);

                if(values[columnIndex - 1] == null) continue;
                if(values[columnIndex - 1] instanceof BigDecimal)
                    if(temp == null) temp = (BigDecimal) values[columnIndex - 1];
                    else temp = temp.max((BigDecimal) values[columnIndex - 1]);
                else
                    if(temp == null) temp = new BigDecimal(values[columnIndex - 1].toString());
                    else temp = temp.max(new BigDecimal(values[columnIndex - 1].toString()));
            }
        } catch(IndexOutOfBoundsException e) {
            throw new DataSetException("부적합한 열 인덱스::"+e);
        } catch(NumberFormatException e) {
            throw new DataSetException("부적합한 열 타입::"+e);
        }

        return temp;
    }

    /**
    지정된 컬럼값의 최소값을 구합니다.
    @param  columnName  컬럼 이름
    @return  컬럼값들의 최소값
    @exception DataSetException   잘못된 컬럼 인덱스가 입력되었을 경우
    */
    public BigDecimal min(String columnName) throws DataSetException {
        int index = findColumn(columnName);
        if(index == -1){
            //LogManager.info("부적합한 열 이름"+columnName);
            throw new DataSetException("부적합한 열 이름::"+columnName);
        }

        return min(index);
    }

    /**
    지정된 컬럼값의 최소값을 구합니다.
    @param  columnIndex 컬럼 인덱스
    @return  컬럼값들의 최소값
    @exception DataSetException   잘못된 컬럼 인덱스가 입력되었을 경우
    */
    public BigDecimal min(int columnIndex) throws DataSetException {
        BigDecimal temp = null;

        try {
            for(int i = 0; i < data.size(); i++) {
                Object values[] = (Object []) data.get(i);

                if(values[columnIndex - 1] == null) continue;
                if(values[columnIndex - 1] instanceof BigDecimal)
                    if(temp == null) temp = (BigDecimal) values[columnIndex - 1];
                    else temp = temp.min((BigDecimal) values[columnIndex - 1]);
                else
                    if(temp == null) temp = new BigDecimal(values[columnIndex - 1].toString());
                    else temp = temp.min(new BigDecimal(values[columnIndex - 1].toString()));
            }
        } catch(IndexOutOfBoundsException e) {
            throw new DataSetException("부적합한 열 인덱스::"+e);
        } catch(NumberFormatException e) {
            throw new DataSetException("부적합한 열 타입::"+e);
        }

        return temp;
    }

    /**
    지정된 컬럼으로 정렬합니다.
    @param columnName  컬럼 이름
    @param m  ASC 작은 순으로 정렬, DESC 큰 순으로 정렬
    @exception DataSetException   잘못된 컬럼 이름이 입력되었을 경우
    */
    public void sort(String columnName, int m) throws DataSetException {
        int index = findColumn(columnName);
        if(index == -1){
            //LogManager.info("부적합한 열 이름"+columnName);
            throw new DataSetException("부적합한 열 이름::"+columnName);
        }

        sort(index, m);
    }

    /**
    지정된 컬럼으로 정렬합니다.
    @param columnIndex  컬럼 인덱스
    @param m  ASC 작은 순으로 정렬, DESC 큰 순으로 정렬
    @exception DataSetException   잘못된 컬럼 인덱스가 입력되었을 경우
    */
    public void sort(int columnIndex, int m) throws DataSetException {
        try {
            SortObject so[] = new SortObject[data.size()];
            for(int i = 0; i < data.size(); i++) {
                Object values[] = (Object []) data.get(i);
                so[i] = new SortObject(i, values[columnIndex - 1]);
            }

            Arrays.sort(so);

            ArrayList temp = new ArrayList(data.size());
            if(m == 1) {
                for(int i = 0; i < so.length; i++)
                    temp.add(data.get(so[i].index));
            } else {
                for(int i = so.length - 1; i >= 0; i--)
                    temp.add(data.get(so[i].index));
            }

            data = temp;
        } catch(IndexOutOfBoundsException e) {
            throw new DataSetException("부적합한 열 인덱스::"+e);
        } catch(ClassCastException e) {
            throw new DataSetException("부적합한 열 타입::"+e);
        }
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("\n");
        for(int i = 0; i < columnCount; i++)
            sb.append("[" + columnNames[i] + "]");
        sb.append("\n");

        for(int i = 0; i < data.size(); i++) {
        	Object obj = data.get(i);
        	if(obj instanceof Map) {
                for(int j = 0; j < columnCount; j++)
                    sb.append(((Map)obj).get(columnNames[j]) + ((j==columnCount-1)?"\t":",\t "));
        	} else {
                Object values[] = (Object []) obj;
                for(int j = 0; j < columnCount; j++)
                    sb.append(values[j] + ((j==columnCount-1)?"\t":",\t "));        		
        	}
            sb.append("\n");
        }

        return sb.toString();
    }
    
    public String toHtmlTable() {
        StringBuffer sb = new StringBuffer(1024*1024);
        sb.append("<table class='bdMode01'  border='0' cellpadding='0' cellspacing='0' >");
        sb.append("\n<tr>");
        for(int i = 0; i < columnCount; i++)
            sb.append("<td>" + columnNames[i] + "</td>");
        sb.append("</tr>");

        for(int i = 0; i < data.size(); i++) {
            sb.append("\n<tr>");
            Object values[] = (Object []) data.get(i);
            for(int j = 0; j < columnCount; j++){
                sb.append("<td>");
                sb.append(values[j]==null?"&nbsp;":values[j]);
                sb.append("</td>");                
            }
            sb.append("</tr>");
        }
        
        sb.append("</table>");

        return sb.toString();
    }
    
    class SortObject implements Comparable {
        int index;
        Object obj;

        SortObject(int index, Object obj) {
            this.index = index;
            this.obj = obj;
        }

        public int compareTo(Object o) {
            SortObject other = (SortObject) o;

            if(other.obj == null) return -1;
            else if(this.obj == null) return 1;

            return ((Comparable) this.obj).compareTo(other.obj);
        }

        public String toString() {
            return index + ", " + obj;
        }
    }
    
    public Object getCurrentRow() {
    	return data.get(row);
    }
    
    /**
     * 현재 커서 위치의 row를 삭제한다.
     * @throws DataSetException
     */
    public void deleteRow() throws DataSetException{
    	try{
    		data.remove(row);
    	    row--;
    	}catch(IndexOutOfBoundsException ex){
    		throw new DataSetException("부적합한 row 인덱스:"+row);
    	}
    }

    /**
     * 현재 커서 위치의 데이터를 DataMap으로 변환하여 리턴한다.
     * @return DataMap
     */
    public DataMap toDataMap() throws DataSetException{
        int i=0;
        try{
	    	Object[] objArr = (Object[]) data.get(row);
	    	DataMap dataMap = new DataMap();
	    	for( ; i<this.columnNames.length; i++){
	    		dataMap.put(columnNames[i], objArr[i]);
	    	}
	    	return dataMap;
    	}catch(IndexOutOfBoundsException ex){
            throw new DataSetException("부적합한 열 인덱스:["+i+"]");
    	}
    }

    /**
     * 현재 커서 위치의 데이터를 인자로 들어오는 DataMap에 담는다.
     * DataMap에 원래 담겨 있던 값들은 모두 clear 된다.
     * 
     * @param dataMap
     * @return DataMap
     * @throws DataSetException
     */
    public void toDataMap(DataMap dataMap) throws DataSetException{
        int i=0;
        try{
	    	Object[] objArr = (Object[]) data.get(row);
	    	dataMap.clear();
	    	for( ; i<this.columnNames.length; i++){
	    		dataMap.put(columnNames[i], objArr[i]);
	    	}
	    	
    	}catch(IndexOutOfBoundsException ex){
    		throw new DataSetException("부적합한 열 인덱스:["+i+"]");
    	}
    }
    
    public String[] getColumnNames(){
    	return this.columnNames;
    }
    
    /**
     * 2006. 07. 19 김성균
     * DataSet에 DataSet을 append 합니다.
     * @param ds    추가하려고 하는 DataSet
     * @return      결과 DataSet
     * @throws DataSetException
     */
    public DataSet appendData(DataSet ds) throws DataSetException {
    	if (ds == null) throw new DataSetException("추가하려고 하는 DataSet 객체가 NULL입니다.");
    	ds.initRow();
        while (ds.next()) {
    		this.data.add(ds.getCurrentRow());
    	}
    	return this;
    }
    
    /**
     * 2006. 07. 19 김성균
     * DataSet에 Data를 append 합니다.
     * 2008. 03. 10 오재훈 MapDataSet을 Append할 경우 추가.
     * @param obj   추가하려고 하는 객체
     * @return      결과 DataSet
     * @throws DataSetException
     */
    public DataSet appendData(Object obj) throws DataSetException {
    	if (obj == null) throw new DataSetException("추가하려고 하는 Data가 NULL입니다.");
    	
    	if(obj instanceof Map){
    		Object[] appendData = new Object[columnCount];
			for (int i = 0; i < columnCount; i++) {
				appendData[i] = ((Map)obj).get(getColumnName(i+1));
			}
			obj = appendData;
    	}
    	this.data.add(obj);

    	return this;
    }
    
    /**
     * 현재 행의 지정된 컬럼의 값을 SET 합니다. 
     * 
     * @param index
     *            컬럼 인덱스
     * @param value
     *            컬럼 데이타
     * @return 컬럼 값
     * @exception DataSetException
     *                잘못된 컬럼 인덱스가 입력되었을 경우
     */
    public void setValue(int index, Object value) throws DataSetException {
        Object temp[] = findObjectArray();

        try {
            temp[index - 1] = value;
        } catch(NullPointerException e) {

        } catch (IndexOutOfBoundsException e) {
            throw new DataSetException("부적합한 열 인덱스::" + e);
        }
    }
    
    /**
     * 현재 행의 지정된 컬럼의 값을 SET 합니다. 
     * 
     * @param columnName
     *            컬럼 이름
     * @param value
     *            컬럼 데이타
     * @return 컬럼 값
     * @exception DataSetException
     *                잘못된 컬럼 인덱스가 입력되었을 경우
     */
    public void setValue(String columnName, Object value) throws DataSetException {
        int index = findColumn(columnName);
        if (index == -1) {
            LogManager.info("부적합한 열 이름===>" + columnName);
            throw new DataSetException("부적합한 열 이름===>" + columnName);
        }
        
        setValue(index, value);
    }
    
    /**
     * @return
     */
    public ArrayList getData() {
        return data;
    }
    
    
    /**
     * 현재 DataSet에 컬럼을 추가하고 각 row마다 해당 컬럼에 대한 디폴트값을 셋팅합니다.
     * 추가되는 컬럼명은 null 또는 공백일 수 없고, 기존 컬럼명과 중복될 수 없습니다.
     * 
     * @param columnName 컬럼명
     * @param defaultValue 디폴트값
     */
    public void addColumn(String columnName, Object defaultValue){
    	if(columnName==null || (columnName = columnName.trim()).length()<=0){
    		throw new DataSetException("Column명은 null이거나 공백일 수 없습니다. ===>[" + columnName +"]");
    	}
    	
    	int index = findColumn(columnName);
        if (index != -1) {
            throw new DataSetException("동일한 Column명이 존재합니다. ===>[" + columnName +"]");
        }
        
        synchronized(this){
        	int rowCount = this.getRowCount();
        	
        	//1. 컬럼 사이즈를 늘이고 컬럼명을 추가한다.
        	this.columnCount = columnCount+1;
	        String[] newColumnNames = new String[columnCount];
	        System.arraycopy(columnNames, 0, newColumnNames, 0, columnCount-1);
	        newColumnNames[columnCount-1] = columnName;
	        this.columnNames = newColumnNames;
	       	
	       	//2. 각각의 row 데이터에 대해서 추가된 컬럼에 대한 디폴트값을 추가한다.
	        //(rowCount가 0보다 큰 경우에만)
	       	Object[] record = null;	 
	       	Object[] newRecord = null;
	       	for(int i=0; i<rowCount; i++){
	       		record = (Object[])data.get(i);
	       		if(record==null) continue;
	       		newRecord  = new Object[columnCount];
		        System.arraycopy(record, 0, newRecord, 0, columnCount-1);
		        newRecord[columnCount-1] = defaultValue;
		        data.set(i, newRecord);
		        record = null;
	       	}
	       	
        }
    }

}

