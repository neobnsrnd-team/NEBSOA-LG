/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import nebsoa.common.Context;
import nebsoa.common.acl.UserSession;
import nebsoa.common.collection.DataSet;
import nebsoa.common.exception.ParamException;
import nebsoa.common.exception.SysException;
import nebsoa.common.jdbc.IbatisTxManager;
import nebsoa.common.log.LogManager;


/*******************************************************************
 * <pre>
 * 1.설명 
 * 요청 파라미터 및 응답 데이타를 담는데 사용되는 유틸리티입니다. 
 * 사용법은 HashMap과 동일하며, 추가적인 기능을 가지고 있음.
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
 * $Log: DataMap.java,v $
 * Revision 1.1  2018/01/15 03:39:49  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:30  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:23  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:26  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.4  2008/09/26 09:37:01  youngseokkim
 * setTransactionOwner() 수정
 *
 * Revision 1.3  2008/09/01 13:00:04  jglee
 * Transaction 관련 메소드 추가
 *
 * Revision 1.2  2008/08/22 05:24:15  youngseokkim
 * DataSet객체를 얻어 클론 객체를 리턴하는
 * getDataSetClone() 메소드 추가
 *
 * Revision 1.1  2008/08/04 08:54:50  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.10  2008/07/25 01:31:06  김재범
 * getarr 메서드 추가
 *
 * Revision 1.9  2008/06/12 11:12:49  김은정
 * getNVLString의 return값에 trim()추가
 *
 * Revision 1.8  2008/05/02 08:45:33  김은정
 * getstr method생성(NVL된 String리턴 메소드-IBK요청)
 *
 * Revision 1.7  2008/03/12 06:00:02  오재훈
 * getNVLString(String a, String defaulValue) 추가
 *
 * Revision 1.6  2008/02/29 06:22:34  오재훈
 * *** empty log message ***
 *
 * Revision 1.5  2008/02/26 04:18:06  이종원
 * IBK 관련 이슈로 웹 요청 시작 일자 및  시간을 두가지 포멧으로 MAP에 저장
 *
 * Revision 1.4  2008/02/21 07:23:06  오재훈
 * 컨텍스트명 추가.
 *
 * Revision 1.3  2008/02/14 04:34:29  오재훈
 * web.login.Authenticator, Authorizor 클래스들과 web.session.SessionManager,UserInfo 들이 삭제되고 common.acl 패키지의 클래스들로 대체되었습니다.
 * web.util.Request.Util 클래스도 common.util.RequestUtil로 옴겨졌습니다.
 *
 * Revision 1.2  2008/01/30 07:26:39  오재훈
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:17  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:01  안경아
 * *** empty log message ***
 *
 * Revision 1.40  2007/06/05 07:05:45  안경아
 * *** empty log message ***
 *
 * Revision 1.39  2007/06/05 06:21:02  안경아
 * *** empty log message ***
 *
 * Revision 1.38  2007/04/10 13:58:03  이종원
 * getParameterValues 로직 수정
 *
 * Revision 1.37  2007/01/17 08:33:43  김성균
 * 비밀번호 관련 필드 마스킹 처리 추가
 *
 * Revision 1.36  2006/11/14 11:34:36  김승희
 * toLog 메소드 수정
 *
 * Revision 1.35  2006/11/02 11:58:31  김성균
 * *** empty log message ***
 *
 * Revision 1.34  2006/10/27 10:24:46  김성균
 * *** empty log message ***
 *
 * Revision 1.33  2006/10/21 07:10:22  김성균
 * *** empty log message ***
 *
 * Revision 1.32  2006/10/20 03:12:50  김성균
 * *** empty log message ***
 *
 * Revision 1.31  2006/10/02 17:13:28  이종원
 * getBoolean추가
 *
 * Revision 1.30  2006/10/02 17:11:52  이종원
 * getBoolean추가
 *
 * Revision 1.29  2006/10/02 16:39:38  이종원
 * *** empty log message ***
 *
 * Revision 1.28  2006/09/29 06:59:11  안경아
 * *** empty log message ***
 *
 * Revision 1.27  2006/09/29 06:56:12  안경아
 * *** empty log message ***
 *
 * Revision 1.26  2006/09/20 06:09:49  최수종
 * 사용자 id, 사용자 명, 주민번호 set(), get() 추가
 *
 * Revision 1.25  2006/09/05 04:02:25  김성균
 * *** empty log message ***
 *
 * Revision 1.24  2006/08/30 04:06:10  김성균
 * *** empty log message ***
 *
 * Revision 1.23  2006/08/30 02:31:36  김성균
 * *** empty log message ***
 *
 * Revision 1.22  2006/08/28 09:32:22  김승희
 * toLog 메소드 추가
 *
 * Revision 1.21  2006/08/08 10:46:49  김성균
 * *** empty log message ***
 *
 * Revision 1.20  2006/08/01 08:41:53  김성균
 * 생성자 추가
 *
 * Revision 1.19  2006/07/13 00:30:32  이종원
 * *** empty log message ***
 *
 * Revision 1.17  2006/07/07 04:37:51  김성균
 * getDataSet()에서 DataSet initRow() 시킨 후 리턴
 *
 * Revision 1.16  2006/07/06 02:20:29  오재훈
 * requestUR 세팅
 *
 * Revision 1.15  2006/07/05 17:12:40  이종원
 * *** empty log message ***
 *
 * Revision 1.14  2006/07/05 17:12:02  이종원
 * 오류코드 정의 : ParamException
 *
 * Revision 1.13  2006/07/05 06:38:02  김성균
 * getDataSet() 메소드 추가
 *
 * Revision 1.12  2006/07/04 07:43:40  이종원
 * debugging, logging을 위한 toString 추가
 *
 * Revision 1.11  2006/07/04 07:05:56  김성균
 * *** empty log message ***
 *
 * Revision 1.10  2006/06/20 04:29:40  김승희
 * clone() 변경 : Context도 복사
 *
 * Revision 1.9  2006/06/19 08:02:17  김승희
 * getLong, put 추가
 *
 * Revision 1.8  2006/06/17 10:37:45  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class DataMap extends HashMap implements Serializable {
    /**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -7517688282979333170L;

	/**
	 * Comment for <code>context</code>
	 */
	private Context context;
    
    /**
     * Comment for <code>BEGIN_LOOP</code>
     */
    public static final String BEGIN_LOOP = "_BeginLoop_";
    
    /**
     * Comment for <code>USER_INFO</code>
     */
    public static final String USER_INFO = PropertyManager.getProperty("web_config", "LOGIN.SESSION.BIND.NAME");

    /**
     * Comment for <code>CONTEXT_NAME 컨텍스트 명. ControllerServlet 레벨에서 셋팅하여야 함.</code>
     */
    public static final String CONTEXT_NAME = "_CONTEXT_NAME";

	/**
	 * Comment for <code>txManager</code>
	 */
	transient private IbatisTxManager txManager;

    /**
     * Comment for <code>BEGIN_TRAN</code>
     */
    private static final String BEGIN_TRAN = "BEGIN_TRAN";

    /**
     * Comment for <code>END_TRAN</code>
     */
    private static final String END_TRAN = "END_TRAN";

	/**
	 * Comment for <code>TRANSACTION_STATUS</code>
	 */
	private String TRANSACTION_STATUS;

	/**
	 * Comment for <code>TRANSACTION_OWNER</code>
	 */
	private String TRANSACTION_OWNER = "NONE";

	/**
	 * 디폴트생성자 - initialCapacity : 500
	 */
	public DataMap() {
        super(500);
	}
    
	/**
	 * @param initialCapacity
	 */
	public DataMap(int initialCapacity) {
        super(initialCapacity);
	}
    
    /**
     * Map을 인자로 해서 DataMap 생성
     * @param map
     */
    public DataMap(Map map) {
        super(map);
    }

	public Object get(String name) {
		Object obj = super.get(name);
		return obj; 
	}	
    
	public void put(String name, int value) {
		super.put(name, Integer.valueOf(value));
	}

	public void put(String name, long value) {
		super.put(name, Long.valueOf(value));
	}

	public void put(String name, float value) {
		super.put(name, Float.valueOf(value));
	}

	public void put(String name, double value) {
		super.put(name, Double.valueOf(value));
	}

	public void put(String name, boolean value) {
		super.put(name, Boolean.valueOf(value));
	}
	
	/**
	 * DataSet Type을 DataMap에 put하는 메소드
	 * @param name
	 * @param ds
	 */
	public void putDataSet(String name , DataSet ds) {
		super.put(BEGIN_LOOP+name, ds);
	}

	
	/**
	 * 작성일자 : 2008. 02. 29 
	 * 작성자 : kfetus
	 * 설명 : 웹캐쉬 요청으로 NVL된 String 리턴 메소드 제공.
	 * @param paramName
	 * @return
	 */
	public String getNVLString(String paramName){
		String value = getString(paramName);
		//trim추가 요청 2008-03-12.
		if(value==null || value.trim().length() == 0 ) return "";
		//trim추가 요청 2008-06-12.
		return value.trim();
	}
	
	/**
	 * 작성일자 : 2008. 05. 02 
	 * 작성자 : kim eun jung
	 * 설명 : IBK 요청으로 NVL된 String 리턴 메소드 제공.
	 * @param paramName
	 * @return
	 */
	public String getstr(String paramName){
		return getNVLString(paramName);
	}	

	/**
	 * 작성일자 : 2008. 02. 29 
	 * 작성자 : kfetus
	 * 설명 : 웹캐쉬 요청으로 NVL된 String 리턴 메소드 제공.
	 * @param paramName
	 * @return
	 */
	public String getNVLString(String paramName,String defaultValue) {
		String value = getString(paramName);
		if(value==null || value.trim().length() == 0 ) return defaultValue;
		//trim 해서 없으면 추가 요청.
		return value;
	}

	
	/**
	 * @param paramName
	 * @return
	 */
	public String getString(String paramName) {
		Object obj = super.get(paramName);
		if(obj==null){
		    //LogManager.debug("DataMap에"+ paramName+"으로 등록된 객체가 없습니다.");
			return null;
		}else if(obj instanceof Collection || obj instanceof Object[]){
//			LogManager.debug(paramName+"의 객체 String 아님:"+obj.getClass().getName());
			throw new SysException(paramName+"으로 등록된 객체는 String 변환 가능 객체 아님:"+obj.getClass().getName());
		}else{
		    return obj.toString();
        }
	}
	
	/**
	 * @param paramName
	 * @return 0 if value is null, else return the value 
	 */
	public int getInt(String paramName) throws NumberFormatException {
	    String value = getString(paramName);
		if(value==null) {
			return 0;
		}
		return Integer.parseInt(value);
	}
	
	/**
	 * @param paramName
	 * @return
	 */
	public long getLong(String paramName) throws NumberFormatException {
	    String value = getString(paramName);
		if(value==null) {
			return 0;
		}
		return Long.parseLong(value);
	}
    
    /**
     * y,Y,Yes,yes,T,true,True,On,on모두 true를 리턴한다.
     * 값이 없거나, 위의 값이 아니면  false를 리턴한다.
     * @param paramName
     * @return
     */
    public boolean getBoolean(String paramName) {
        String value = getString(paramName);
        if(value==null) {
            return false;
        }
        return StringUtil.getBoolean(value,false);
    }
	
	/**
	 * <font color=red>
	 * 해당 하는 파라미터 값을 얻어 옵니다. 
	 * 해당 값이 없을 때 ParamException을 발생 시킵니다.
	 * 따라서 개발자는 해당 값이 파라미터로 전달 된 값이 없을 때 에러 유발을 
	 * 원치 않는 다면 getParameter(String paramName, String defaultValue)
	 * 메소드를 호출 합니다. 이 메소드는 해당 값이 없으면 default값을 리턴 합니다.
	 * 다시 말해 이 메소드는 필수 입력 값을 얻어 내는데 사용하면 편리 합니다.
	 * 즉  필수 입력값이 없으면 파라미터가 없다는 에러를 내어 알려 줍니다.
	 * </font>  
	 * @param paramName
	 * @return String 파라미터값 
	 * @throws ParamException 값이 없을 경우 (개발자가 매번 null check를 하지 않도록 하기 위해)
	 */
	public String getParameter(String paramName) throws ParamException{
	    String str = getString(paramName);
	    if(StringUtil.isNullNotTrim(str)){
	        throw new ParamException(paramName);	
	    }else{
	        return str;
	    }		
	}
	
	/**
	 * Map에서 가져 온다.
	 */
	public String getParameter(String paramName, String defaultValue){
	    String str = getString(paramName);
	    if(StringUtil.isNullNotTrim(str)){
	        return defaultValue;	
	    }else{
	        return (String)str;
	    }
	}	
	
	/**
	 * 작성일자 : 2008. 07. 18 
	 * 작성자 : kim jae bum
	 * 설명 : IBK 요청으로 getParameterValues 메소드 제공.
	 * @param paramName
	 * @return
	 */
	public String[] getarr(String paramName) {
		return getParameterValues(paramName);
	}
	
	/**
	 * Map에서 배열 형태로 가져 온다.
	 * 값이 없으면 에러가 발생하지 않고  null을 리턴한다.
	 */
	public String[] getParameterValues(String paramName){
	    String[] strArr = getStringArray(paramName);
	    return strArr;
	}

	/**
	 * Map에서 int 형태로 가져 온다.
	 * @param paramName
	 * @return
	 * @throws ParamException 값이 없을 경우 (개발자가 매번 null check를 하지 않도록 하기 위해)
	 */
	public int getIntParameter(String paramName) throws ParamException {
		String str = getParameter(paramName);

		int i = 0;
		try {
			i = Integer.parseInt(str);
		} catch (NumberFormatException e) {
			throw new SysException("숫자 형식이 아닙니다--" + str);
		}
		return i;
	}
	
	/**
	 * Map에서 int 형태로 가져 온다.
	 * @param paramName
	 * @param defaultValue
	 * @return
	 * @throws ParamException 값이 없을 경우 (개발자가 매번 null check를 하지 않도록 하기 위해)
	 */
	public int getIntParameter(String paramName, int defaultValue) throws ParamException {
		String str = getParameter(paramName, "");
		if (StringUtil.isNull(str)) {
			return defaultValue;
		}
		return getIntParameter(paramName);
	}	
	
	/**
	 * Map에서 가져 온다. 필수 입력 정보일때 사용 한다.
	 * @throws ParamException 값이 없을 경우 (개발자가 매번 null check를 하지 않도록 하기 위해)
	 */
	public Object getObjectParameter(String paramName) throws ParamException {
		Object obj = get(paramName);
		if (obj == null) {
			throw new ParamException(paramName);
		} else {
			return obj;
		}
	}
	
	/**
	 * Map에서 가져 온다.
	 */
	public Object getObjectParameter(String paramName, Object defaultObj) {
		Object obj = get(paramName);
		if (obj == null) {
			return defaultObj;
		} else {
			return obj;
		}
	}
	
	/**
	 * Map에서 가져 온다.
	 * @throws ParamException 값이 없을 경우 (개발자가 매번 null check를 하지 않도록 하기 위해)
	 */
	public double getDoubleParameter(String paramName) throws ParamException{
	    String str =  getParameter(paramName);

	    double i = 0;
	    try{			
	        i = Double.parseDouble(str);
	    }catch(NumberFormatException e){
	        throw new SysException("숫자 형식이 아닙니다--"+str);
	    }
	    return i;
	}
	/**
	 * request Map에서 가져 온다.
	 */	
	public double getDoubleParameter(String paramName ,double defaultValue){
	    String str =  getParameter(paramName,"");
	    if(StringUtil.isNull(str)){
	        return defaultValue;
	    }
	    double i = 0;
	    try{			
	        i = Double.parseDouble(str);
	    }catch(NumberFormatException e){
	        throw new SysException("숫자 형식이 아닙니다--"+str);
	    }
	    return i;
	}	
	
	/**
	 * 설명: DataMap안에 있는 객체를 array로 얻어 오는 로직.
	 * 2004. 9. 9. 이종원 작성
	 * @param paramName
	 * @return
	 * 
	 */
	public String[] getStringArray(String paramName){
		Object obj = super.get(paramName+"Array");
		String param = null;
		String[] params = null;
		if (obj instanceof String[]){
			return (String[])obj;
        }else if (obj instanceof Collection){
            //collection 인 경우 null리턴.
            return null;
        }else if (obj instanceof DataSet){
            //collection 인 경우 null리턴.
            return null;            
		}else if (obj instanceof Object[]){
            Object[] objArray = (Object[]) obj;
            String[] array = new String[objArray.length];
		    for(int i=0;i<objArray.length;i++){
                try{
                    array[i] = objArray[i].toString();
                }catch(NullPointerException e){
                    array[i]="";
                }
            }
            return array;
        }else if(obj != null){
            String[] array = new String[1];
            array[0]=obj.toString();
            return array;
        }else {
            param = getParameter(paramName, null);
            if (param == null) {
                LogManager.debug("responseMap에"+ paramName+"으로 등록된 객체가 없습니다.");
                return null;
            } else {
                params = new String[1];
                params[0] = param;
                return params;
            }
        } 
	}

	/**
	 * DataMap을 clone한다.
	 * @see java.lang.Object#clone()
	 */
	public Object clone(){
		DataMap dataMap = new DataMap();
		dataMap.putAll((Map)super.clone());
		//Context 객체 변수도 copy
		dataMap.setContext(this.getContext());
		//Transactioin 객체 Copy
		dataMap.setTxManager(this.getTxManager());
		return dataMap;
	}
    
    /**
     * Copies all of the mappings from the specified map to this map
     * These mappings will replace any mappings that
     * this map had for any of the keys currently in the specified map.
     * Context도 복사한다.
     *
     * @param m mappings to be stored in this map.
     * @throws NullPointerException if the specified map is null.
     */
    public void putAll(Map m) {
        putAll(m, true);
    }
    
    /**
     * Copies all of the mappings from the specified map to this map
     * These mappings will replace any mappings that
     * this map had for any of the keys currently in the specified map.
     * 
     * @param m mappings to be stored in this map.
     * @param isContextCopy Context 복사 여부
     */
    public void putAll(Map m, boolean isContextCopy) {
        super.putAll(m);
        if (isContextCopy) {
            DataMap map = (DataMap) m;
            if (map.getContext() != null) {
                setContext(map.getContext());
            }
        }
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
        //IBK 관련 이슈로 웹 요청 시작 일자 및  시간을 두가지 포멧으로 MAP에 저장
        if(context != null){
        	put("REQ_START_DATE",context.getTransactionDate()); //YYYYMMDD FORMAT
        	//YYYY-MM-DD FORMAT
        	put("REQ_START_DATE_10",context.getTransactionDate().substring(0,4)
        			+"-" +context.getTransactionDate().substring(4,6)
        			+"-" +context.getTransactionDate().substring(6));
        	put("REQ_START_TIME",context.getTransactionTime());
        	put("REQ_START_TIME_8",context.getTransactionTime().substring(0,2)
        			+":" +context.getTransactionTime().substring(2,4)
        			+":" +context.getTransactionTime().substring(4));
        }
    }
	
	/*
	public DataSet toDataSet(){
		Set keySet = this.keySet();
		String[] keyNames = new String[keySet.size()];
		keySet.toArray(keyNames);
		
	}*/
    /**
     * debugging을 위한 메소드 추가
     */
    public String toString()
    {
        if(isEmpty()){
            return "DataMap\uC5D0 \uC800\uC7A5\uB41C \uC815\uBCF4\uAC00 \uC5C6\uC2B5\uB2C8\uB2E4";
        }
        StringBuffer buf = new StringBuffer(2000);
        Set keySet = super.keySet();
        Iterator i = keySet.iterator();
        String key = null;
        Object item = null;
        while(i.hasNext()) 
        {
            key = (String)i.next();
            item = get(key);
            if(item == null)
                buf.append(key + "=null\n");
            else
            if(item instanceof String) { 
	            if (key.indexOf("비밀번호") != -1 ) {
	                item = "****";
	            }
	            else if (key.toUpperCase().indexOf("PASSWORD") != -1 ) {
	                item = "****";
	            }
	            else if (key.toUpperCase().indexOf("PWD") != -1  ) {
	                item = "****";
	            }
	            else if (key.indexOf("이메일결재메시지") != -1  ){
	                item = "****";
	            }
	            
                buf.append(key + "=[" + item + "]\n");
            }
            else
            if(item instanceof Integer || item instanceof Long 
                   || item instanceof Double || item instanceof java.lang.Float 
                   || item instanceof Boolean)
                buf.append(key + "=[" + item + "]\n");
            else
            if(item instanceof String[])
            {
                String data[] = (String[])item;
                buf.append(key + "=[");
                int j;
                for(j = 0; j < data.length; j++)
                    buf.append(data[j] + ',');
                
                buf.append("] Array Size:" + j + " \n");
            } else
            {
                buf.append(key + "=[" + item + "] ClassName:" + item.getClass().getName() + "\n");
            }
        }
        buf.append("end of DataMap info ============================\n");
        return buf.toString();
    }

    public String getDataMapInfo()
    {
        return toString();
    }
    
    /**
     * DataSet 객체를  얻어오기 위해서 사용한다. 
     * @param loopKey
     * @return
     */
    public DataSet getDataSet(String loopKey) {
    	DataSet dataSet = (DataSet) get(BEGIN_LOOP + loopKey);
    	if (dataSet != null) {
    		dataSet.initRow();
    	}
    	return dataSet;
    }

    /**
     * 원래 DataSet 객체를 얻어와서 클론 객체를 리턴한다. 
     * @param loopKey
     * @return
     */
    public DataSet getDataSetClone(String loopKey) {
    	DataSet dataSet = (DataSet)((DataSet)get(BEGIN_LOOP + loopKey)).clone();
    	if (dataSet != null) {
    		dataSet.initRow();
    	}
    	return dataSet;
    }
    
    
    public static void main(String args[])
    {
        DataMap map = new DataMap();
        map.put("AAA", "BBB");
        map.put("BBB", Integer.valueOf(12345));
        map.put("ARRAY", new String[] {
            "x", "y", "z"
        });
        map.put("비밀번호", "2222");
        map.put("eeeddddDeee", "<43242>");
        map.put("이메일결재메시지eeee", "3333");
        System.out.println(map);
        System.out.println("_-------------------------------------------");
        System.out.println(map.toLog());
    }

	public String getRequestURI() {
		return (String) get("URI");
	}
	
	public void setRequestURI(String uri) {
		put("URI",uri);
	}
	
    /**
     * logging을 위한 메소드
     */
    public String toLog()
    {
        if(isEmpty()){
            return "DataMap\uC5D0 \uC800\uC7A5\uB41C \uC815\uBCF4\uAC00 \uC5C6\uC2B5\uB2C8\uB2E4";
        }
        StringBuffer buf = new StringBuffer(2000);
        Set keySet = super.keySet();
        Iterator i = keySet.iterator();
        String key = null;
        Object item = null;
        while(i.hasNext()) 
        {
            key = (String)i.next();
            item = get(key);
            if(item == null)
            {   
            	buf.append(key + "=null" +"<BR>");
            }
            else if(item instanceof String) 
            {
                
            	if (key.indexOf("비밀번호") != -1 ) {
            		item = "****";
                }
                else if (key.toUpperCase().indexOf("PASSWORD") != -1 ) {
                    item = "****";
                }
                else if (key.toUpperCase().indexOf("PWD") != -1  ) {
                    item = "****";
                }
                else if (key.indexOf("이메일결재메시지") != -1  ){
                	item = "****";
                }
                
                item = StringUtil.escapeXml(item.toString());
                
                buf.append(key + "=[" + item + "]<BR>");
            } 
            else if(item instanceof String[])
            {
                String data[] = (String[])item;
                buf.append(key + "=[");
                int j;
                for(j = 0; j < data.length; j++)
                    buf.append(data[j] + ',');

                buf.append("] Array Size:" + j + " <BR>");
            }
          /*  else if()
            {
            }*/
            else
            {
                buf.append(key + "=[" + item + "] ClassName:" + item.getClass().getName() + "<BR>");
            }
        }
        //buf.append("end of DataMap info ============================\n");
        return buf.toString();
    }
    
    /**
     * 사용자 ID 설정
     * @param userId 사용자 ID
     */
    public void setUserId(String userId)
    {
    	put(UserSession.USER_ID, userId);
    }    
    
    /**
     * 사용자 ID 얻기
     * @return 사용자 ID
     */
    public String getUserId()
    {
    	return (String)get(UserSession.USER_ID);
    }
    
    /**
     * 사용자 명 설정
     * @param userName 사용자 명
     */
    public void setUserName(String userName)
    {
    	put(UserSession.USER_NAME, userName);
    }    
    
    /**
     * 사용자 명 얻기
     * @return 사용자 명
     */
    public String getUserName()
    {
    	return (String)get(UserSession.USER_NAME);
    }    
    
    /**
     * 사용자 주민번호 설정
     * @param userName 사용자 주민번호
     */
    public void setJuminNo(String juminNo)
    {
    	put(UserSession.JUMIN_NO, juminNo);
    }    
    
    /**
     * 사용자 주민번호 얻기
     * @return 사용자 주민번호
     */
    public String getJuminNo()
    {
    	return (String)get(UserSession.JUMIN_NO);
    }      
    

    /**
     * CONTEXT_NAME 값을 얻어 옵니다.
     * @return 컨텍스트 명
     */
    public String getContextName() {
        return getString(CONTEXT_NAME);
    }

    /**
     * CONTEXT_NAME을 세팅합니다.
     * @param 컨텍스트 명
     */
    public void setContextName(String contextName) {
        put(CONTEXT_NAME, contextName);
    }

	/**
	 * IbatisTxManager 얻기
	 * @return the tx
	 */
	public IbatisTxManager getTxManager() {
		return txManager;
	}

	/**
	 * IbatisTxManager를 세팅
	 * @param IbatisTxManager
	 */
	public void setTxManager(IbatisTxManager tx) {
		this.txManager = tx;
		if(tx != null){
			setBeginTran();
			setTransactionOwner(this.getTransactionOwner());
		}else{
			setEndTran();
			setTransactionOwner("NONE");
		}
	}    
	
	/**
	 * transaction상태값 세팅
	 */
	public void setBeginTran(){
		TRANSACTION_STATUS = BEGIN_TRAN;
	}
	
	/**
	 * transaction상태값 세팅
	 */
	public void setEndTran(){
		TRANSACTION_STATUS = END_TRAN;
	}

	/**
	 * transaction상태값 리턴
	 */
	public String getTransactionStatus(){
		return StringUtil.NVL(TRANSACTION_STATUS,"");
	}
	
	/**
	 * transaction owner 세팅
	 */
	public void setTransactionOwner(String val){
		if("".equals(StringUtil.NVL(val))){
			TRANSACTION_OWNER = "NONE";
		}else{
			TRANSACTION_OWNER = val;
		}
	}

	/**
	 * transaction owner 리턴
	 */
	public String getTransactionOwner(){
		return	TRANSACTION_OWNER;
	}

	/**
	 * transaction상태 리턴
	 */
	public boolean isTransactionMode(){
		if(txManager == null){
			return false;
		}else{
			return true;
		}
	}

}


