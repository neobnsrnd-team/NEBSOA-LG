package nebsoa.common.collection;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import nebsoa.common.collection.DataSet.SortObject;
import nebsoa.common.exception.DBException;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.DataMap;
import nebsoa.common.util.PropertyManager;
import nebsoa.common.util.StringUtil;

/**
 * IBATIS 용 DataSet입니다. 기존의 DataSet과 사용방법은 동일합니다.
 * @author $Author: cvs $
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 
 * $Log: MapDataSet.java,v $
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
 * Revision 1.9  2008/06/19 04:39:37  김은정
 * getNVLString의 return값에 trim추가
 *
 * Revision 1.8  2008/04/24 00:46:06  오재훈
 * getNVLstring메소드 추가.
 *
 * Revision 1.7  2008/03/10 03:40:50  오재훈
 * clone(), 생성자(Stringp[]) 추가
 *
 * Revision 1.6  2008/02/25 07:54:06  김승희
 * 테스트용 main 메소드 삭제
 *
 * Revision 1.5  2008/02/25 07:52:58  김승희
 * addColumn 메소드 추가
 * sort, avg, min, max 메소드에서 컬럼명을 인자로 받는 메소드 동작하도록 수정
 *
 * Revision 1.4  2008/02/14 04:46:31  오재훈
 * *** empty log message ***
 *
 */
public class MapDataSet  extends DataSet {

	/**
	 * SID
	 */
	private static final long serialVersionUID = -3144702816342333903L;
	
	private static final String notSupportErrCode = "FRS00019";
	private static final String notSupportErrMsg = "현재 사용하고 있는 DataSet에서는 SELECT문 순서와 RESULT결과의 순서의 동일함을 보장하지 못해서 지원되지 않습니다.";
	
	/**
	 * Map 리스트를 가지고 있는 ArrayList로 DataSet 생성
	 * @param list
	 * @throws DataSetException
	 */
	public MapDataSet (ArrayList list) throws DataSetException {
		this.data = list;

		/**
		 * 이 클래스에서는 columnCount가 필요 없을듯. getString(int i) 가 안되기 때문임.
		 * columnNames는 clone 메소드때문에 필요함. 그러나 getColumnName(int i)도 무의미함.
		 */
		if(list.size() > 0)
		{
			Object obj = list.get(0);
			if(! (obj instanceof java.util.Map)){
				data = list = covertToMap(list);
			}
			Map map = (java.util.Map)list.get(0);
			this.columnCount=map.size();
			columnNames = new String[columnCount];
			
			Set keySet = map.keySet();
			Iterator it = keySet.iterator();
			int i = 0;
			while(it.hasNext()){
				columnNames[i] = (String)it.next();
				i++; 
			}
		}
		
		if (isMonitorMode()) {
			// long dataSize = getDataSize();
			// if(dataSize > 64000)

			if (data.size() > PropertyManager.getIntProperty("monitor",
					"RESULTSET_MAX_RECORD_COUNT")) {
				LogManager.error("MONITOR","Too many rows " + data.size(),
						new Exception("Too many rows"));
			}
		}
	}

	public MapDataSet(String[] columNames) throws DataSetException{
        super(columNames, new ArrayList());
	}
	
    public Object clone() {    	
    	MapDataSet rs = new MapDataSet((ArrayList)this.data.clone());
		return rs;
	}
	
	
	private ArrayList covertToMap(ArrayList list) {
		if(list == null) return null;
		ArrayList list2 = new ArrayList(list.size());
		for(int i=0;i<list.size();i++){
			HashMap map = new HashMap();
			map.put("data",list.get(i)==null?"null":list.get(i).toString());
			list2.add(map);
		}
		return list2;
		
	}




	/**
	 * 현재 사용하고 있는 DataSet에서는 SELECT문 순서와 RESULT결과의 순서의 동일함을 보장하지 못해서 지원되지 않음.
	 * @exception DataSetException 지원하지 않는 메소드
	 */
	public String getColumnName(int index) throws DataSetException {
		throw new DataSetException(notSupportErrCode,notSupportErrMsg);
	}

    /**
	 * 현재 사용하고 있는 DataSet에서는 SELECT문 순서와 RESULT결과의 순서의 동일함을 보장하지 못해서 지원되지 않음.
	 * @exception DataSetException 지원하지 않는 메소드
	 */
    public int findColumn(String columnName) throws DataSetException {
		throw new DataSetException(notSupportErrCode,notSupportErrMsg);
    }
	
    /**
     * 현재 커서 위치 Map을 ArrayList에서 반환
     * @return
     * @throws DataSetException
     */
    protected Map findObjectMap() throws DataSetException {
        try {
            return (Map) data.get(row);
        } catch(NullPointerException e) {
            throw new DataSetException("DataSet이 생성되지 않았음");
        } catch(IndexOutOfBoundsException e) {
            throw new DataSetException("검색결과가 없거나, DataSet.next가 호출되지 않았음");
        }
    }

    /**
	 * 현재 사용하고 있는 DataSet에서는 SELECT문 순서와 RESULT결과의 순서의 동일함을 보장하지 못해서 지원되지 않음.
	 * @exception DataSetException 지원하지 않는 메소드
    */
    public Object getObject(int index) throws DataSetException {
		throw new DataSetException(notSupportErrCode,notSupportErrMsg);
		
    }

    /**
     * 현재 행의 지정된 컬럼의 값을 Object형으로 얻습니다.
     * @param columnName 컬럼 이름
     * @return  컬럼 값
     * @exception DBException   잘못된 컬럼 명이 입력되었을 경우
     */
    public Object getObject(String columnName) throws DataSetException {
    	Map map = findObjectMap();
    	
		if (!map.containsKey(columnName) && !map.containsKey(columnName.toUpperCase()) ) {
			LogManager.info("부적합한 열 이름===>" + columnName);
			throw new DataSetException("부적합한 열 이름===>" + columnName);
		}
		
		Object value = map.get(columnName);
		if(value==null){
			return map.get(columnName.toUpperCase());
		} else return value;
		
    }


    /**
	 * 현재 사용하고 있는 DataSet에서는 SELECT문 순서와 RESULT결과의 순서의 동일함을 보장하지 못해서 지원되지 않음.
	 * @exception DataSetException 지원하지 않는 메소드
     */
    public String getString(int index) throws DataSetException {
    	throw new DataSetException(notSupportErrCode,notSupportErrMsg);
    }

    /**
     * 현재 행의 지정된 컬럼의 값을 String형으로 얻습니다.
     * 
     * @param columnName 컬럼 이름
     * @return 컬럼 값
     * @exception DataSetException 잘못된 컬럼 명이 입력되었을 경우
     */
	public String getString(String columnName) throws DataSetException {
		String tempStr = null;
		
		Object obj = getObject(columnName);

		if (obj instanceof byte[])
			tempStr = new String((byte[]) obj);
		else if(obj != null)
			tempStr = obj.toString();

		return tempStr;
	}

    /**
     * 현재 행의 지정된 컬럼의 값을 String형으로 얻습니다.
     * null일 경우 "" 를 리턴합니다.
     * @param columnName 컬럼 이름
     * @return 컬럼 값
     * @exception DataSetException 잘못된 컬럼 명이 입력되었을 경우
     */
	public String getNVLString(String columnName) throws DataSetException {
		String tempStr = getString(columnName);
		if (tempStr == null) tempStr = "";
    	//trim추가 요청 2008-06-19.
    	return tempStr.trim();    
	}
	
    /**
	 * 현재 사용하고 있는 DataSet에서는 SELECT문 순서와 RESULT결과의 순서의 동일함을 보장하지 못해서 지원되지 않음.
	 * @exception DataSetException 지원하지 않는 메소드
     */
    public String getString(int index,String format) throws DataSetException {
    	throw new DataSetException(notSupportErrCode,notSupportErrMsg);
    }

    /**
     * 현재 행의 지정된 컬럼의 값을 날짜형식으로 얻습니다.
     * 
     * @param columnName 컬럼 이름
     * @param dateFormat yyyy년mm월dd일
     * @return 컬럼 값
     * @exception DataSetException 잘못된 컬럼 명이 입력되었을 경우
     */
    public String getString(String columnName,String dateFormat) throws DataSetException {
        String str = getString(columnName);
        return StringUtil.formatDate(dateFormat,str);
    }

    /**
	 * 현재 사용하고 있는 DataSet에서는 SELECT문 순서와 RESULT결과의 순서의 동일함을 보장하지 못해서 지원되지 않음.
	 * @exception DataSetException 지원하지 않는 메소드
     */
    public String getBoldString(int index,String match) throws DataSetException {
    	throw new DataSetException(notSupportErrCode,notSupportErrMsg);
    }

    /**
	 * 현재 사용하고 있는 DataSet에서는 SELECT문 순서와 RESULT결과의 순서의 동일함을 보장하지 못해서 지원되지 않음.
	 * @exception DataSetException 지원하지 않는 메소드
     */
    public String getBoldString(int index,String format,String match) throws DataSetException {
    	throw new DataSetException(notSupportErrCode,notSupportErrMsg);
    }

    /**
     * 색을 입혀서 현재 행의 지정된 컬럼의 값을 String형으로 얻습니다.
     * @param columnName 컬럼 이름
     * @param match
     * @return  컬럼 값
     * @exception DBException   잘못된 컬럼 명이 입력되었을 경우
     */
    public String getBoldString(String columnName,String match) throws DataSetException {
        return StringUtil.bold(getString(columnName),match);
    }

    /**
     * 색을 입혀서 날짜 형식의 현재 행의 지정된 컬럼의 값을 String형으로 얻습니다.
     * @param columnName 컬럼 이름
     * @param format
     * @param match
     * @return  컬럼 값
     * @exception DBException   잘못된 컬럼 명이 입력되었을 경우
     */
    public String getBoldString(String columnName,String format,String match) throws DataSetException {
        return StringUtil.bold(getString(columnName,format),match);
    }
    
    /**
	 * 현재 사용하고 있는 DataSet에서는 SELECT문 순서와 RESULT결과의 순서의 동일함을 보장하지 못해서 지원되지 않음.
	 * @exception DataSetException 지원하지 않는 메소드
     */
    public byte getByte(int index) throws DataSetException {
    	throw new DataSetException(notSupportErrCode,notSupportErrMsg);
    }

    /**
     * 현재 행의 지정된 컬럼의 값을 byte형으로 얻습니다.
     * @param columnName 컬럼 이름
     * @return  컬럼 값
     * @exception DataSetException   잘못된 컬럼 명이 입력되었을 경우, 잘못된 숫자 포맷, 오버플로우
     */
    public byte getByte(String columnName) throws DataSetException {

		byte tempByte = (byte) 0;
		
        try {
    		Object obj = getObject(columnName);

    		if(obj instanceof java.sql.Timestamp || obj instanceof byte[])
                throw new DataSetException("부적합한 열 유형::"+obj.getClass().getName());

            String value = obj.toString().trim();
            double dvalue = Double.parseDouble(value);
            if(Byte.MAX_VALUE < dvalue || Byte.MIN_VALUE > dvalue)
                throw new DataSetException("숫자 오버플로우");

            tempByte = (byte) dvalue;
        } catch(NullPointerException e) {
        } catch(IndexOutOfBoundsException e) {
            throw new DataSetException("부적합한 열 인덱스::"+e.getMessage());
        } catch(NumberFormatException e) {
            throw new DataSetException("내부 표기로 변환할 수 없음");
        }

        return tempByte;
    }

    /**
	 * 현재 사용하고 있는 DataSet에서는 SELECT문 순서와 RESULT결과의 순서의 동일함을 보장하지 못해서 지원되지 않음.
	 * @exception DataSetException 지원하지 않는 메소드
     */
    public short getShort(int index) throws DataSetException {
    	throw new DataSetException(notSupportErrCode,notSupportErrMsg);
    }

    /**
     * 현재 행의 지정된 컬럼의 값을 short형으로 얻습니다.
     * @param columnName 컬럼 이름
     * @return  컬럼 값
     * @exception DataSetException   잘못된 컬럼 명이 입력되었을 경우, 잘못된 숫자 포맷, 오버플로우
     */
    public short getShort(String columnName) throws DataSetException {

    	short tempShort = (short) 0;
		

        try {
    		Object obj = getObject(columnName);

    		if(obj instanceof java.sql.Timestamp || obj instanceof byte[])
                throw new DataSetException("부적합한 열 유형::"+obj.getClass().getName());

            String value = obj.toString().trim();
            double dvalue = Double.parseDouble(value);
            if(Short.MAX_VALUE < dvalue || Short.MIN_VALUE > dvalue)
                throw new DataSetException("숫자 오버플로우");

            tempShort = (short) dvalue;
        } catch(NullPointerException e) {
        } catch(IndexOutOfBoundsException e) {
            throw new DataSetException("부적합한 열 인덱스 : "+e.getMessage());
        } catch(NumberFormatException e) {
            throw new DataSetException("내부 표기로 변환할 수 없음");
        }

        return tempShort;
    }
    
    /**
	 * 현재 사용하고 있는 DataSet에서는 SELECT문 순서와 RESULT결과의 순서의 동일함을 보장하지 못해서 지원되지 않음.
	 * @exception DataSetException 지원하지 않는 메소드
     */
    public int getInt(int index) throws DataSetException {
    	throw new DataSetException(notSupportErrCode,notSupportErrMsg);
    }

    /**
     * 현재 행의 지정된 컬럼의 값을 int형으로 얻습니다.
     * @param columnName 컬럼 이름
     * @return  컬럼 값
     * @exception DataSetException   잘못된 컬럼 명이 입력되었을 경우, 잘못된 숫자 포맷, 오버플로우
    */
    public int getInt(String columnName) throws DataSetException {
    	
        int tempInt = 0;

        try {
    		Object obj = getObject(columnName);

    		if(obj instanceof java.sql.Timestamp || obj instanceof byte[])
                throw new DataSetException("부적합한 열 유형::"+obj.getClass().getName());

            String value = obj.toString().trim();
            double dvalue = Double.parseDouble(value);
            if(Integer.MAX_VALUE < dvalue || Integer.MIN_VALUE > dvalue)
                throw new DataSetException("숫자 오버플로우");

            tempInt = (int) dvalue;
        } catch(NullPointerException e) {
        } catch(IndexOutOfBoundsException e) {
            throw new DataSetException("부적합한 열 인덱스: "+e.getMessage());
        } catch(NumberFormatException e) {
            throw new DataSetException("내부 표기로 변환할 수 없음");
        }

        return tempInt;
    }

    /**
	 * 현재 사용하고 있는 DataSet에서는 SELECT문 순서와 RESULT결과의 순서의 동일함을 보장하지 못해서 지원되지 않음.
	 * @exception DataSetException 지원하지 않는 메소드
     */
    public long getLong(int index) throws DataSetException {
    	throw new DataSetException(notSupportErrCode,notSupportErrMsg);
    }

    /**
     * 현재 행의 지정된 컬럼의 값을 long형으로 얻습니다.
     * @param columnName 컬럼 이름
     * @return  컬럼 값
     * @exception DataSetException   잘못된 컬럼 명이 입력되었을 경우, 잘못된 숫자 포맷, 오버플로우
     */
    public long getLong(String columnName) throws DataSetException {

        long tempLong = (long) 0;

        try {
    		Object obj = getObject(columnName);

            if(obj instanceof java.sql.Timestamp || obj instanceof byte[])
                throw new DataSetException("부적합한 열 유형::"+obj.getClass().getName());

            String value = obj.toString().trim();
            double dvalue = Double.parseDouble(value);
            if(Long.MAX_VALUE < dvalue || Long.MIN_VALUE > dvalue)
                throw new DataSetException("숫자 오버플로우");

            tempLong = (long) dvalue;
        } catch(NullPointerException e) {
        } catch(IndexOutOfBoundsException e) {
            throw new DataSetException("부적합한 열 인덱스:"+e.getMessage());
        } catch(NumberFormatException e) {
            throw new DataSetException("내부 표기로 변환할 수 없음");
        }

        return tempLong;
    }

    /**
	 * 현재 사용하고 있는 DataSet에서는 SELECT문 순서와 RESULT결과의 순서의 동일함을 보장하지 못해서 지원되지 않음.
	 * @exception DataSetException 지원하지 않는 메소드
     */
    public float getFloat(int index) throws DataSetException {
    	throw new DataSetException(notSupportErrCode,notSupportErrMsg);    	
    }

    /**
     * 현재 행의 지정된 컬럼의 값을 float형으로 얻습니다.
     * @param columnName 컬럼 이름
     * @return  컬럼 값
     * @exception DataSetException   잘못된 컬럼 명이 입력되었을 경우, 잘못된 숫자 포맷
     */
    public float getFloat(String columnName) throws DataSetException {
        Float tempFloat = Float.valueOf(0f);

        try {
    		Object obj = getObject(columnName);

    		tempFloat = Float.valueOf(obj.toString().trim());
        } catch(NullPointerException e) {
        } catch(IndexOutOfBoundsException e) {
            throw new DataSetException("부적합한 열 인덱스:"+e.getMessage());
        } catch(NumberFormatException e) {
            throw new DataSetException("내부 표기로 변환할 수 없음");
        }

        return tempFloat.floatValue();
    }
    
    /**
	 * 현재 사용하고 있는 DataSet에서는 SELECT문 순서와 RESULT결과의 순서의 동일함을 보장하지 못해서 지원되지 않음.
	 * @exception DataSetException 지원하지 않는 메소드
     */
    public double getDouble(int index) throws DataSetException {
    	throw new DataSetException(notSupportErrCode,notSupportErrMsg);    	
    }

    /**
     * 현재 행의 지정된 컬럼의 값을 double형으로 얻습니다.
     * @param columnName 컬럼 이름
     * @return  컬럼 값
     * @exception DataSetException   잘못된 컬럼 명이 입력되었을 경우, 잘못된 숫자 포맷
     */
    public double getDouble(String columnName) throws DataSetException {
        Double tempDouble = Double.valueOf(0d);


        try {
    		Object obj = getObject(columnName);

    		if(obj instanceof java.sql.Timestamp || obj instanceof byte[])
                throw new DataSetException("부적합한 열 유형::"+obj.getClass().getName());

            tempDouble = Double.valueOf(obj.toString().trim());
        } catch(NullPointerException e) {
        } catch(IndexOutOfBoundsException e) {
            throw new DataSetException("부적합한 열 인덱스:"+e.getMessage());
        } catch(NumberFormatException e) {
            throw new DataSetException("내부 표기로 변환할 수 없음");
        }

        return tempDouble.doubleValue();
    }

    /**
	 * 현재 사용하고 있는 DataSet에서는 SELECT문 순서와 RESULT결과의 순서의 동일함을 보장하지 못해서 지원되지 않음.
	 * @exception DataSetException 지원하지 않는 메소드
     */
    public BigDecimal getBigDecimal(int index) throws DataSetException {
    	throw new DataSetException(notSupportErrCode,notSupportErrMsg);
    }

    /**
    현재 행의 지정된 컬럼의 값을 BigDecimal형으로 얻습니다.
    @param columnName 컬럼 이름
    @return  컬럼 값
    @exception DataSetException   잘못된 컬럼 명 입력되었을 경우, 잘못된 숫자 포맷
    */
    public BigDecimal getBigDecimal(String columnName) throws DataSetException {

        BigDecimal tempBigDecimal = BigDecimal.ZERO;

        try {
    		Object obj = getObject(columnName);

    		if(obj instanceof java.sql.Timestamp || obj instanceof byte[])
                throw new DataSetException("부적합한 열 유형::"+obj.getClass().getName());

            tempBigDecimal = new BigDecimal(obj.toString().trim());
        } catch(NullPointerException e) {
        } catch(IndexOutOfBoundsException e) {
            throw new DataSetException("부적합한 열 인덱스:"+e.getMessage());
        } catch(NumberFormatException e) {
            throw new DataSetException("내부 표기로 변환할 수 없음");
        }
        return tempBigDecimal;
    }

    /**
	 * 현재 사용하고 있는 DataSet에서는 SELECT문 순서와 RESULT결과의 순서의 동일함을 보장하지 못해서 지원되지 않음.
	 * @exception DataSetException 지원하지 않는 메소드
     */
    public boolean getBoolean(int index) throws DataSetException {
    	throw new DataSetException(notSupportErrCode,notSupportErrMsg);
    }

    /**
     * 현재 행의 지정된 컬럼의 값을 boolean형으로 얻습니다.
     * @param columnName 컬럼 이름
     * @return  컬럼 값
     * @exception DataSetException   잘못된 컬럼 명이 입력되었을 경우, 잘못된 포맷
     */
    public boolean getBoolean(String columnName) throws DataSetException {

        Boolean tempBoolean = Boolean.FALSE;

        try {
    		Object obj = getObject(columnName);

            if(obj instanceof java.sql.Timestamp || obj instanceof byte[])
                throw new DataSetException("부적합한 열 유형::"+obj.getClass().getName());

            int value = (new BigDecimal(obj.toString().trim())).intValue();
            if(value != 0) tempBoolean = Boolean.TRUE;
        } catch(NullPointerException e) {
        } catch(IndexOutOfBoundsException e) {
            throw new DataSetException("부적합한 열 인덱스:"+e.getMessage());
        } catch(NumberFormatException e) {
            throw new DataSetException("내부 표기로 변환할 수 없음");
        }

        return tempBoolean.booleanValue();

    }
    
    /**
	 * 현재 사용하고 있는 DataSet에서는 SELECT문 순서와 RESULT결과의 순서의 동일함을 보장하지 못해서 지원되지 않음.
	 * @exception DataSetException 지원하지 않는 메소드
     */
    public java.sql.Timestamp getTimestamp(int index) throws DataSetException {
    	throw new DataSetException(notSupportErrCode,notSupportErrMsg);
    }

    /**
     * 현재 행의 지정된 컬럼의 값을 java.sql.Timestamp형으로 얻습니다.
     * @param columnName 컬럼 이름
     * @return  컬럼 값
     * @exception DataSetException   잘못된 컬럼 명이 입력되었을 경우, 잘못된 포맷
     */
    public java.sql.Timestamp getTimestamp(String columnName) throws DataSetException {
        java.sql.Timestamp tempTimestamp = null;

        try {
    		Object obj = getObject(columnName);
            if(obj instanceof java.math.BigDecimal || obj instanceof byte[])
                throw new DataSetException("부적합한 열 유형::"+obj.getClass().getName());

            tempTimestamp = java.sql.Timestamp.valueOf(obj.toString().trim());
        } catch(NullPointerException e) {
        } catch(IndexOutOfBoundsException e) {
            throw new DataSetException("부적합한 열 인덱스:"+e.getMessage());
        } catch(NumberFormatException e) {
            throw new DataSetException("내부 표기로 변환할 수 없음");
        }

        return tempTimestamp;
    }
    
    /**
	 * 현재 사용하고 있는 DataSet에서는 SELECT문 순서와 RESULT결과의 순서의 동일함을 보장하지 못해서 지원되지 않음.
	 * @exception DataSetException 지원하지 않는 메소드
     */
    public java.sql.Date getDate(int index) throws DataSetException {
    	throw new DataSetException(notSupportErrCode,notSupportErrMsg);
    }


    /**
     * 현재 행의 지정된 컬럼의 값을 java.sql.Date형으로 얻습니다.
     * @param columnName 컬럼 이름
     * @return  컬럼 값
     * @exception DataSetException   잘못된 컬럼 명이 입력되었을 경우, 잘못된 포맷
     */
    public java.sql.Date getDate(String columnName) throws DataSetException {

        java.sql.Date tempDate = null;

        try {
    		Object obj = getObject(columnName);

    		if(obj instanceof java.math.BigDecimal || obj instanceof byte[])
                throw new DataSetException("부적합한 열 유형::"+obj.getClass().getName());

            tempDate = java.sql.Date.valueOf(obj.toString().trim());
        } catch(NullPointerException e) {
        } catch(IndexOutOfBoundsException e) {
            throw new DataSetException("부적합한 열 인덱스::"+e);
        } catch(NumberFormatException e) {
            try {
                java.sql.Timestamp tempTimestamp = java.sql.Timestamp.valueOf(getObject(columnName).toString().trim());
                tempDate = new java.sql.Date(tempTimestamp.getTime());
            } catch(NumberFormatException e2) {
                throw new DataSetException("내부 표기로 변환할 수 없음");
            }
        }
        return tempDate;

    }

    /**
	 * 현재 사용하고 있는 DataSet에서는 SELECT문 순서와 RESULT결과의 순서의 동일함을 보장하지 못해서 지원되지 않음.
	 * @exception DataSetException 지원하지 않는 메소드
     */
    public byte[] getBytes(int index) throws DataSetException {
    	throw new DataSetException(notSupportErrCode,notSupportErrMsg);
    }

    /**
     * 현재 행의 지정된 컬럼의 값을 byte[]형으로 얻습니다.
     * @param columnName 컬럼 이름
     * @return  컬럼 값
     * @exception DataSetException   잘못된 컬럼 명이 입력되었을 경우
     */
    public byte[] getBytes(String columnName) throws DataSetException {

        byte tempBytes[] = null;

        try {
    		Object obj = getObject(columnName);

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
            throw new DataSetException("부적합한 열 인덱스::"+e.getMessage());
        }

        return tempBytes;
    }
    
    /**
	 * 현재 사용하고 있는 DataSet에서는 SELECT문 순서와 RESULT결과의 순서의 동일함을 보장하지 못해서 지원되지 않음.
	 * @exception DataSetException 지원하지 않는 메소드
     */
    public InputStream getBinaryStream(int index) throws DataSetException {
    	throw new DataSetException(notSupportErrCode,notSupportErrMsg);
    }

    /**
     * 현재 행의 지정된 컬럼의 값을 InputStream형으로 얻습니다.
     * @param columnName 컬럼 이름
     * @return  컬럼 값
     * @exception DataSetException   잘못된 컬럼 명이 입력되었을 경우
     */
    public InputStream getBinaryStream(String columnName) throws DataSetException {

        InputStream tempIs = null;

        try {
    		Object obj = getObject(columnName);

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
	 * 현재 사용하고 있는 DataSet에서는 SELECT문 순서와 RESULT결과의 순서의 동일함을 보장하지 못해서 지원되지 않음.
	 * @exception DataSetException 지원하지 않는 메소드
     */
    public BigDecimal sum(int columnIndex) throws DataSetException {
    	throw new DataSetException(notSupportErrCode,notSupportErrMsg);
    }

    /**
     * 지정된 컬럼값의 총합를 구합니다.
     * @param columnName 컬럼 이름
     * @return  컬럼값들의 총합
     * @exception DataSetException   잘못된 컬럼 명이 입력되었을 경우
     */
    public BigDecimal sum(String columnName) throws DataSetException {
        
    	if(!findColumnNames(columnName)){
            throw new DataSetException("부적합한 열 이름::"+columnName);
        }
        
    	BigDecimal temp = new BigDecimal(0);

        try {
            for(int i = 0; i < data.size(); i++) {
            	Map map = (Map) data.get(i);

                if(map.get(columnName) == null) continue;
                if(map.get(columnName) instanceof BigDecimal)
                    temp = temp.add((BigDecimal) map.get(columnName));
                else
                    temp = temp.add(new BigDecimal(map.get(columnName).toString()));
            }
        } catch(IndexOutOfBoundsException e) {
            throw new DataSetException("부적합한 열 인덱스::"+e);
        } catch(NumberFormatException e) {
            throw new DataSetException("부적합한 열 타입::"+e);
        }

        return temp;
    }

    /**
	 * 현재 사용하고 있는 DataSet에서는 SELECT문 순서와 RESULT결과의 순서의 동일함을 보장하지 못해서 지원되지 않음.
	 * @exception DataSetException 지원하지 않는 메소드
     */
    public BigDecimal ave(int columnIndex) throws DataSetException {
    	throw new DataSetException(notSupportErrCode,notSupportErrMsg);
    }
    
    /**
     * 지정된 컬럼값의 평균을 구합니다.
     * @param columnName 컬럼 이름
     * @return  컬럼값들의 평균
     * @exception DataSetException   잘못된 컬럼 명이 입력되었을 경우
     */
    public BigDecimal ave(String columnName) throws DataSetException {

    	if(!findColumnNames(columnName)){
            throw new DataSetException("부적합한 열 이름::"+columnName);
        }

    	try {
            BigDecimal temp = sum(columnName);

            return temp.divide(new BigDecimal(getRowCount()), 10, BigDecimal.ROUND_HALF_UP);
        } catch(ArithmeticException e) {
            throw new DataSetException("0 으로 나눌수 없음.");
        }
    }

    /**
	 * 현재 사용하고 있는 DataSet에서는 SELECT문 순서와 RESULT결과의 순서의 동일함을 보장하지 못해서 지원되지 않음.
	 * @exception DataSetException 지원하지 않는 메소드
     */
    public BigDecimal max(int columnIndex) throws DataSetException {
    	throw new DataSetException(notSupportErrCode,notSupportErrMsg);
    }
    
    /**
     * 지정된 컬럼값의 최대값을 구합니다.
     * @param columnName 컬럼 이름
     * @return  컬럼값들의 최대값
     * @exception DataSetException   잘못된 컬럼 명이 입력되었을 경우
     */
    public BigDecimal max(String columnName) throws DataSetException {

    	if(!findColumnNames(columnName)){
            throw new DataSetException("부적합한 열 이름::"+columnName);
        }

        BigDecimal temp = null;

        try {
            for(int i = 0; i < data.size(); i++) {
            	Map map = (Map) data.get(i);

                if(map.get(columnName) == null) continue;
                if(map.get(columnName) instanceof BigDecimal)
                    if(temp == null) temp = (BigDecimal) map.get(columnName);
                    else temp = temp.max((BigDecimal) map.get(columnName));
                else
                    if(temp == null) temp = new BigDecimal(map.get(columnName).toString());
                    else temp = temp.max(new BigDecimal(map.get(columnName).toString()));
            }
        } catch(IndexOutOfBoundsException e) {
            throw new DataSetException("부적합한 열 인덱스::"+e);
        } catch(NumberFormatException e) {
            throw new DataSetException("부적합한 열 타입::"+e);
        }

        return temp;
    }

    /**
	 * 현재 사용하고 있는 DataSet에서는 SELECT문 순서와 RESULT결과의 순서의 동일함을 보장하지 못해서 지원되지 않음.
	 * @exception DataSetException 지원하지 않는 메소드
     */
    public BigDecimal min(int columnIndex) throws DataSetException {
    	throw new DataSetException(notSupportErrCode,notSupportErrMsg);
    }

    /**
     * 지정된 컬럼값의 최소값을 구합니다.
     * @param  columnName  컬럼 이름
     * @return  컬럼값들의 최소값
     * @exception DataSetException   잘못된 컬럼 명이 입력되었을 경우
    */
    public BigDecimal min(String columnName) throws DataSetException {
        
        if(!findColumnNames(columnName)){
            throw new DataSetException("부적합한 열 이름::"+columnName);
        }

        BigDecimal temp = null;

        try {
            for(int i = 0; i < data.size(); i++) {
            	Map map = (Map) data.get(i);

                if(map.get(columnName) == null) continue;
                if(map.get(columnName) instanceof BigDecimal)
                    if(temp == null) temp = (BigDecimal) map.get(columnName);
                    else temp = temp.min((BigDecimal) map.get(columnName));
                else
                    if(temp == null) temp = new BigDecimal(map.get(columnName).toString());
                    else temp = temp.min(new BigDecimal(map.get(columnName).toString()));
            }
        } catch(IndexOutOfBoundsException e) {
            throw new DataSetException("부적합한 열 인덱스::"+e);
        } catch(NumberFormatException e) {
            throw new DataSetException("부적합한 열 타입::"+e);
        }

        return temp;
    }
    
    /**
	 * 현재 사용하고 있는 DataSet에서는 SELECT문 순서와 RESULT결과의 순서의 동일함을 보장하지 못해서 지원되지 않음.
	 * @exception DataSetException 지원하지 않는 메소드
     */
    public void sort(int columnIndex, int m) throws DataSetException {
    	throw new DataSetException(notSupportErrCode,notSupportErrMsg);
    }
    
    /**
     * 지정된 컬럼으로 정렬합니다.
     * @param columnName  컬럼 이름
     * @param m  ASC 작은 순으로 정렬, DESC 큰 순으로 정렬
     * @exception DataSetException   잘못된 컬럼 명이 입력되었을 경우
    */
    public void sort(String columnName, int m) throws DataSetException {

    	if(!findColumnNames(columnName)){
            throw new DataSetException("부적합한 열 이름::"+columnName);
        }

        try {
            SortObject so[] = new SortObject[data.size()];
            for(int i = 0; i < data.size(); i++) {
            	Map map = (Map) data.get(i);
                so[i] = new SortObject(i, map.get(columnName));
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

    /**
     * ArrayList내의 HashMap의 내용을 반환
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("\n");
        for(int i = 0; i < columnCount; i++)
            sb.append("[" + columnNames[i] + "]");
        sb.append("\n");

        for(int i = 0; i < data.size(); i++) {
            Map map = (Map) data.get(i);
            for(int j = 0; j < columnCount; j++)
                sb.append(map.get(columnNames[j]) + ((j==columnCount-1)?"\t":",\t "));
            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * HTML의 Table형식의 스트링 리턴
     */
    public String toHtmlTable() {
        StringBuffer sb = new StringBuffer(1024*1024);
        sb.append("<table class='bdMode01'  border='0' cellpadding='0' cellspacing='0' >");
        sb.append("\n<tr>");
        for(int i = 0; i < columnCount; i++)
            sb.append("<td>" + columnNames[i] + "</td>");
        sb.append("</tr>");

        for(int i = 0; i < data.size(); i++) {
            sb.append("\n<tr>");
            Map map = (Map) data.get(i);
            for(int j = 0; j < columnCount; j++){
                sb.append("<td>");
                sb.append(map.get(columnNames[j])==null?"&nbsp;":map.get(columnNames[j]));
                sb.append("</td>");                
            }
            sb.append("</tr>");
        }
        
        sb.append("</table>");

        return sb.toString();
    }

    /**
     * 현재 커서 위치의 데이터를 DataMap으로 변환하여 리턴한다.
     * @return DataMap
     */
    public DataMap toDataMap() throws DataSetException {
        int i=0;
        try{
	    	Map map = (Map) data.get(row);
	    	DataMap dataMap = new DataMap(map);
//	    	for( ; i<this.columnNames.length; i++){
//	    		dataMap.put(columnNames[i], objArr[i]);
//	    	}
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
	    	Map map = (Map) data.get(row);
	    	dataMap.clear();
	    	for( ; i<this.columnNames.length; i++){
	    		dataMap.put(columnNames[i], map.get(columnNames[i]));
	    	}
	    	
    	}catch(IndexOutOfBoundsException ex){
    		throw new DataSetException("부적합한 열 인덱스:["+i+"]");
    	}
    }

    /**
	 * 현재 사용하고 있는 DataSet에서는 SELECT문 순서와 RESULT결과의 순서의 동일함을 보장하지 못해서 지원되지 않음.
	 * @exception DataSetException 지원하지 않는 메소드
     */
    public void setValue(int index, Object value) throws DataSetException {
    	throw new DataSetException(notSupportErrCode,notSupportErrMsg);
    }
    
    /**
     * 현재 행의 지정된 컬럼의 값을 SET 합니다. 
     * 
     * @param columnName 컬럼 이름
     * @param value 컬럼 데이타
     * @return 컬럼 값
     * @exception DataSetException 잘못된 컬럼 명이 입력되었을 경우
     */
    public void setValue(String columnName, Object value) throws DataSetException {
        
        if(!findColumnNames(columnName)){
        	LogManager.info("부적합한 열 이름===>" + columnName);
            throw new DataSetException("부적합한 열 이름===>" + columnName);
        }
        Map map = findObjectMap();

        try {
        	map.put(columnName, value);
        } catch(NullPointerException e) {

        } catch (IndexOutOfBoundsException e) {
            throw new DataSetException("부적합한 열 인덱스::" + e);
        }
    }
    
    
    /**
     * 현재 DataSet에 컬럼을 추가하고 각 row마다 해당 컬럼에 대한 디폴트값을 셋팅합니다.
     * 추가되는 컬럼명은 null 또는 공백일 수 없고, 기존 컬럼명과 중복될 수 없습니다.
     * 
     * @param columnName 컬럼명
     * @param defaultValue 디폴트값
     */
    public void addColumn(String columnName, Object defaultValue){
    	
    	int rowCount = this.getRowCount();
    	if(rowCount<=0){
    		return;
    	}
    	
    	if(columnName==null || (columnName = columnName.trim()).length()<=0){
    		throw new DataSetException("Column명은 null이거나 공백일 수 없습니다. ===>[" + columnName +"]");
    	}
    	
    	if(findColumnNames(columnName)){
            throw new DataSetException("동일한 Column명이 존재합니다. ===>[" + columnName +"]");
        }
    	
        synchronized(this){
        	        	
        	//1. 컬럼 사이즈를 늘이고 컬럼명을 추가한다.
        	this.columnCount = columnCount+1;
	        String[] newColumnNames = new String[columnCount];
	        System.arraycopy(columnNames, 0, newColumnNames, 0, columnCount-1);
	        newColumnNames[columnCount-1] = columnName;
	        this.columnNames = newColumnNames;
	        
	        //2. 각각의 row 데이터에 대해서 추가된 컬럼에 대한 디폴트값을 추가한다.
	        //(rowCount가 0보다 큰 경우에만)
	        Map record = null;	 
		    for(int i=0; i<rowCount; i++){
		       record = (Map)data.get(i);
		       if(record==null) continue;
		       record.put(columnName, defaultValue);
		       data.set(i, record);
	        }
        }//end of synchronized
    }
    
    
    /**
     * 현재 DataSet에 인자로 넘어온 컬럼명이 존재하는지 리턴한다.
     * @param columnName
     * @return columnName 존재 여부
     * @throws DataSetException
     */
    private boolean findColumnNames(String columnName) throws DataSetException {
        if(columnNames == null) throw new DataSetException("DataSet 생성안됨");
        for(int i = 0; i < columnCount; i++)
            if(columnName.equalsIgnoreCase(columnNames[i])) return true;
        return false;
    }

}
