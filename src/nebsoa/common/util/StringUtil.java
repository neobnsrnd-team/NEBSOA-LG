/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.commons.lang.time.DateFormatUtils;

import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;
import java.util.Base64;


/*******************************************************************
 * <pre>
 * 1.설명 
 * 문자열 처리를 위한 utility 모음
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
 * $Log: StringUtil.java,v $
 * Revision 1.1  2018/01/15 03:39:49  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:33  cvs
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
 * Revision 1.3  2008/09/30 11:08:53  ejkim
 * *** empty log message ***
 *
 * Revision 1.2  2008/09/30 11:07:50  ejkim
 * lentgh param으로 받아서 전각처리하는 메소드 추가
 * getDBCS(String str, int length)
 *
 * Revision 1.1  2008/08/04 08:54:50  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.3  2008/05/08 11:20:08  오재훈
 * *** empty log message ***
 *
 * Revision 1.2  2008/02/18 02:22:10  김재범
 * NVLSPACE(String , Stirng) 추가.
 *
 * Revision 1.1  2008/01/22 05:58:17  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.3  2007/12/12 08:25:31  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2007/12/11 11:16:32  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2007/11/26 08:38:02  안경아
 * *** empty log message ***
 *
 * Revision 1.33  2007/03/15 02:32:28  최수종
 * *** empty log message ***
 *
 * Revision 1.32  2007/03/13 05:52:20  최수종
 * *** empty log message ***
 *
 * Revision 1.31  2007/02/06 06:29:14  김성균
 * maskedData() 메소드 추가
 *
 * Revision 1.30  2007/02/06 05:41:58  김성균
 * XML스트링에서 비밀번호 관련태그를  마스크 처리하는 메소드 추가
 *
 * Revision 1.29  2007/02/01 10:08:08  최수종
 * *** empty log message ***
 *
 * Revision 1.28  2006/12/05 05:50:00  이종원
 * getBoolean update
 *
 * Revision 1.27  2006/11/09 08:40:45  이종원
 * 암복호화 코드 정리
 *
 * Revision 1.26  2006/11/09 07:25:54  이종원
 * Unicode encode/ decode추가
 *
 * Revision 1.25  2006/11/09 07:24:15  이종원
 * Unicode encode/ decode추가
 *
 * Revision 1.24  2006/11/08 07:48:42  김성균
 * *** empty log message ***
 *
 * Revision 1.23  2006/11/02 13:25:49  김성균
 * *** empty log message ***
 *
 * Revision 1.22  2006/10/30 08:56:25  이종원
 * 암복호화  library 및 함수 추가 (spider_home/config/security/nebsoa.key 파일필요)
 *
 * Revision 1.21  2006/10/16 08:40:37  최수종
 * HTML태그를 그대로 브라우져에 보여주는 메소드 추가
 *
 * Revision 1.20  2006/10/03 08:30:34  최수종
 * html코드 웹에 그대로 보여주는 메소드 추가
 *
 * Revision 1.19  2006/09/22 08:26:44  이종원
 * getBoolean수정
 *
 * Revision 1.18  2006/09/18 02:39:25  이종원
 * parseInt, parseLong, parseDouble등 추가
 *
 * Revision 1.17  2006/09/14 07:07:05  김승희
 * *** empty log message ***
 *
 * Revision 1.16  2006/09/14 05:03:37  김승희
 * *** empty log message ***
 *
 * Revision 1.15  2006/09/14 05:00:56  김승희
 * *** empty log message ***
 *
 * Revision 1.14  2006/09/11 00:22:03  안경아
 * *** empty log message ***
 *
 * Revision 1.13  2006/09/04 09:11:31  안경아
 * *** empty log message ***
 *
 * Revision 1.12  2006/08/30 00:44:19  김성균
 * *** empty log message ***
 *
 * Revision 1.11  2006/08/24 11:54:30  안경아
 * *** empty log message ***
 *
 * Revision 1.10  2006/08/24 04:17:02  안경아
 * *** empty log message ***
 *
 * Revision 1.9  2006/08/11 02:14:09  김승희
 * exception 변경
 *
 * Revision 1.8  2006/08/10 09:29:40  김승희
 * formatSignedNumber, parseSignedNumber 메소드 추가
 *
 * Revision 1.7  2006/06/17 10:37:45  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class StringUtil {


	public static final int ALIGN_CENTER = 1;
	public static final int ALIGN_LEFT = 2;
	public static final int ALIGN_RIGHT = 3;
	/**
	 DB 상의 character를 varchar2와 비교하기 위해 만들어짐...
	 DB 상에서의(스키마에서의) charater 허용치 만큼 공백을 채워줌...
	 str: DB에서 가지온 문자
	 maxlen: DB 상에서의(스키마에서의) charater 허용치
	 align: charater 허용치에 맞추기 위해 공백을 채워주는 방향...

	 **/
	public static String appendSpace(String str,
						int maxlen, int align) throws Exception {
		switch(align) {
			case ALIGN_CENTER :
				int spacelen = maxlen - str.getBytes().length;
				int r = spacelen % 2;
				if(r == 1) {
					int len = (spacelen - 1) / 2 ;
					str = spacing("", len , 1) + str;
					str = spacing(str, len + 1, 1);
				}
				else {
					int len = spacelen / 2 ;
					String spacestr = spacing("", len , 1);
					str = spacestr + str + spacestr;
				}
				break;
			case ALIGN_LEFT:
				str = spacing(str, maxlen - str.getBytes().length, 1);
				break;
			case ALIGN_RIGHT:
				str = spacing("", maxlen - str.getBytes().length, 1) + str;
				break;
		}
		return str;
	}

	public static String spacing(String str, int maxlen, int cur){
		StringBuffer result = new StringBuffer(str);
		int i = cur;
		while(maxlen >= i) {
			result = result.append(" ");
			i++;
		}
		return result.toString();
	}

        /**
         * 특정문자열의 크기 및 원하는 형태로 특정 값을 왼쪽에 채워서 리턴한다.
         */
    public static String leftPadWith(String source, int totLen, char pad) {
        String rtnString = new String();
        try {
            String padStr = new String();
            byte[] byteSrc = source.getBytes();
            int byteLen = totLen - byteSrc.length;

            if (byteLen < 0)
                throw new Exception("totLen가 입력 스트링보다 작습니다!!");

            for (int i = 0; i < byteLen; i++) {
                padStr = padStr + pad;
            }
            rtnString = padStr + source;
        } catch (Exception e) {
            LogManager.error("StringUtil.leftPadWith Error" + e.getMessage());
            return null;
        }
        return rtnString;
    }

    /**
     * 특정문자열의 크기 및 원하는 형태로 특정 값을 오른쪽에 채워서 리턴한다.
     */
    public static String rightPadWith(String source, int totLen, char pad) {
        String rtnString = new String();
        try {
            String padStr = new String();
            byte[] byteSrc = source.getBytes();
            int byteLen = totLen - byteSrc.length;

            if (byteLen < 0)
                throw new Exception("totLen가 입력 스트링보다 작습니다!!");

            for (int i = 0; i < byteLen; i++) {
                padStr = padStr + pad;
            }
            rtnString = source + padStr;
        } catch (Exception e) {
            LogManager.error("StringUtil.rightPadWith Error" + e.getMessage());
            return null;
        }
        return rtnString;
    }


    /**
     * 한글 변환하는 함수입니다. default.properties 프로퍼티 파일에서 STRING_CONVERT_MODE값으로 한글 변환
     * 할 지 여부를 판단하는 값을 지정할 수 있습니다
     */
    public static String toKo(String str) {
        if (str == null || str.length() == 0)
            return null;
        try {
            String flag = PropertyManager.getProperty("default",
                    "STRING_CONVERT_MODE", "true");
            if ("true".equals(flag)) {
                return new String(str.getBytes("8859_1"), "ksc5601");
            } else {
                return str;
            }
        } catch (Exception e) {
            LogManager.error("StringUtil.toKo Error" + e.getMessage());
            return null;
        }
    }

    /**
	str로 입력된 문자열에서 모든 src 문자열을 찾아서 des로 대치합니다.
	@param  str  원본 문자열.
	@param  src  찾고자 하는 문자열
	@param  des  대치하고자 하는 문자열
	@return  변환된 문자열
	*/
	public static String replaceAll(String str, String src, String des) throws SysException {
		StringBuffer sb = new StringBuffer(str.length());
		int startIdx = 0;
		int oldIdx = 0;
		while(true) {
			startIdx = str.indexOf(src, startIdx);
			if(startIdx == -1) {
				sb.append(str.substring(oldIdx));
				break;
			}

			sb.append(str.substring(oldIdx, startIdx));
			sb.append(des);

			startIdx += src.length();
			oldIdx = startIdx;
		}

		return sb.toString();
	}

	/**
	str로 입력된 문자열에서 처음 만나는  src 문자열을 찾아서 des로  대치합니다.
	최초로 발견된 문자열만을 대치합니다.
	@param  str  원본 문자열.
	@param  src  찾고자 하는 문자열
	@param  des  대치하고자 하는 문자열
	@return  변환된 문자열
	*/
	public static String replace(String str, String src, String des) throws SysException {
		StringBuffer sb = new StringBuffer(str.length());

		int startIdx = str.indexOf(src);
		if(startIdx == -1) return str;

		sb.append(str.substring(0, startIdx));
		sb.append(des);
		sb.append(str.substring(startIdx + src.length()));

		return sb.toString();
	}
	/**
	str로 입력된 문자열을 구분자를 기준으로 잘라서 배열을 만들어 리턴.
	@param  str  원본 문자열.
	@param  del  구분자 문자열
	@return  String 배열
	*/
	public static String[] toArray(String str, String del) {
		if(str == null) return null;

		StringTokenizer st = new StringTokenizer(str, del);
		String[] names = new String[st.countTokens()];
		for(int i = 0; i < names.length; i++) {
			names[i] = st.nextToken().trim();
		}

		return names;
	}
	/**
	str로 입력된 문자열을 공백문자를 기준으로 잘라서 배열을 만들어 리턴.
	@param  str  원본 문자열.
	@return  String 배열
	*/
	public static String[] toArray(String str)  {
		return toArray(str,null);
	}


//   	/**
//	request로 부터 파라미터 값을 얻어 낼때 한글변환 시켜 얻어낸다
//	@param  request   HttpServletRequest .
//	@param  paramName  파라미터 이름.
//	@return  얻어낸 라파미터 값
//	*/
//	public static String getParameter(HttpServletRequest request,
//    String paramName){
//        return toKo(request.getParameter(paramName));
//    }
//
//
//
//   	/**
//	request로 부터 파라미터 값을 얻어 낼때 한글변환 시켜 얻어낸다
//	@param  request   HttpServletRequest .
//	@param  paramName  파라미터 이름.
//	@return  얻어낸 라파미터 값의 배열
//	*/
//    public static String[] getParameterValues(HttpServletRequest request,
//	String paramName){
//        String array[] = request.getParameterValues(paramName);
//        if(array == null)
//            return null;
//        for(int i = 0; i < array.length; i++)
//            array[i] = toKo(array[i]);
//
//        return array;
//    }

	/**
	입력받은 문자열이 null이거나 "" 인지 판단하여 null이면 true 값이 있으면 false리턴
	@param  str   java.lang.String .
	@return  boolean value (if null or "" ==>return true, not null==>return false )
	*/
    public static boolean isNull(String str){
        if(str == null || str.trim().length()==0) return true;
        return false;
    }
    
    /**
     * 입력받은 문자열이 null이거나 ""(empty-String) 인지 판단하여 null 이면 true 값이 있으면 false 리턴 합니다.
     * 주어진 데이터에 대해서 trim() 하지 않습니다.
     * 
     * @param str 체크 대상 문자열
     * @return null 혹은 empty-String 이면 true, 아니면 false
     * @since 2005.11.02
     * @author Helexis
     */
    public static boolean isNullNotTrim(String str) {
        if(str == null || str.length() == 0) return true;
        return false;
    }//end of isNullNotTrim()


	/**
	조회나, 수정form에 뿌려 줄때 값이 null이면 화면에 null이라고 찍히는 것을 없애기 위해사용한다
	*/
	public static String NVL(Object obj){
		if(obj != null) return obj.toString();
		else return "";
	}

	/**
	값이 null이면 화면에 default값 리턴
	*/
	public static String NVL(String obj, String deflt){
		if(obj != null) return obj.toString();
		else return deflt;
	}
	
	public static String NVLSPACE(String obj, String deflt){
		if(obj != null && !obj.equals("")) return obj.toString();
		else return deflt;
	}
	/**
	값이 null이면 화면에 default값 리턴
	*/
	public static int NVL(Object obj,int deflt){
		if(obj != null) {
			try{
				return Integer.parseInt(obj.toString());
			}catch(NumberFormatException e){
				LogManager.error("StringUtil.NVL에 숫자가 아닌 값"+obj);
				return 0;
			}
		}
		else return deflt;
	}
	
	public static double NVL(Object obj,double deflt){
		if(obj != null) {
			try{
				return Double.parseDouble(obj.toString());
			}catch(NumberFormatException e){
				LogManager.error("StringUtil.NVL에 숫자가 아닌 값"+obj);
				return 0;
			}
		}
		else return deflt;
	}
	
	/**
	 * obj1이 null이거나 ""이면 obj2를 리턴한다.
	 * 
	 * @param obj1 
	 * @param obj2
	 * @return
	 */
	public static Object NVL(String obj1, Object obj2){
	    return StringUtil.isNull(obj1)?obj2:obj1;
	}

	public static String NVLSPACE(Object obj, String deflt) {
		if(obj != null && !obj.toString().equals("")) return obj.toString();
		else return deflt;
	}
	/**
	\r\n을 &lt;br&gt;로 바꿔주는 함수
	*/
	public static String BR(String str) throws Exception{
		if(str == null) return str;
		else return replaceAll(str,"\n" , "<br>");
	}
	/**
	\r\n을 &lt;br&gt;로 바꿔주는 함수
	*/
	public static String br(String str) throws Exception{
		return BR(str);
	}
	/**
	검색결과를 찾아 브라우져에 뿌릴 때 해당하는 문자에  색깔을 입혀 준다
    컬러를 지정하지 않으면 default.properties파일에서 	HIGHLIGHT_COLOR 값을 읽어 사용 합니다
	*/
	public static String highlight(String str, String word, String color) {
		return bold(str,word,color);
	}
	/**
	검색결과를 찾아 브라우져에 뿌릴 때 해당하는 문자에  색깔을 입혀 준다
    컬러를 지정하지 않으면 default.properties파일에서 	HIGHLIGHT_COLOR 값을 읽어 사용 합니다
	*/
	public static String highlight(String str, String word) {
		return highlight(str,word,null);
	}
	/**
	검색결과를 찾아 브라우져에 뿌릴 때 해당하는 문자에  색깔을 입혀 준다
    컬러를 지정하지 않으면 default.properties파일에서 	HIGHLIGHT_COLOR 값을 읽어 사용 합니다
	*/
	public static String bold(String str, String word) {
		return bold(str,word,null);
	}
	/**
	검색결과를 찾아 브라우져에 뿌릴 때 해당하는 문자에  색깔을 입혀 준다
    컬러를 지정하지 않으면 default.properties파일에서 	HIGHLIGHT_COLOR 값을 읽어 사용 합니다
	*/
	public static String bold(String str, String word, String color) {
		if(str == null) return str;
		if(color==null) color=PropertyManager.getProperty("default","HIGHLIGHT_COLOR","RED");
		try{
			return replaceAll(str,word , "<B><font color='"+color+"'>"+word+"</font></B>");
		}catch(Exception e){
			return str;
		}
	}

	/**
	str으로 입력된 스트링을 format에서 지정한 form으로 포멧팅함.
	*/
	public  static  String formatString(String format, String str) {
		if(format == null) return str;
		if(str == null) return null;

		try {
			if(format.equals("Won")) {
				double number = Double.parseDouble(str);
				NumberFormat form = NumberFormat.getCurrencyInstance();
				str = form.format(number);
			}else if(format.equals("Dollar")) {
				double number = Double.parseDouble(str);
				NumberFormat form = NumberFormat.getCurrencyInstance(Locale.US);
				str = form.format(number);
			}else if(format.equals("Percent")) {
				double number = Double.parseDouble(str);
				NumberFormat form = NumberFormat.getPercentInstance();
				str = form.format(number);
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
			//LogManager.error(e.getMessage());
		}

		return str;
	}

//	/**
//	 * 주어진 서블릿 요청에서 원하는 이름의 매개변수를 찾아 그 값을 반환한다.
//	 * 이 때, 해당 매개변수가 없거나 빈 문자열이면, 기본값을 반환한다.
//	 *
//	 * @param req javax.servlet.http.HttpServletRequest 서블릿 요청
//	 * @param name String	매개변수의 이름
//	 * @param init String	매개변수의 기본값
//	 * @see javax.servlet.http.HttpServletRequest.getParameter
//	 */
//	public static String getParameter(javax.servlet.http.HttpServletRequest req,
//								String name, String init)
//	{
//        String value = getParameter(req,name);
//
//		if( !isNull(value)){
//			return value;
//		}else {
//			return init;
//		}
//
//    } // End of getParameter


	/**
	System.out.println(formatDate( <br>
			"yyyy년mm월dd일","1981-02-22 00:00:00.0"));과 같이 사용
	*/
	public  static  String formatDate(String format, String str) {
		//Date(yyyy-MM-dd)에서 yyyy-MM-dd부분을 얻어옴..
		if(str==null) return "";
		if(format==null) return str;

		java.util.Date dd = null;
		try {
			//입력된 스트링이 날짜만 가지고 있으면...
			dd = java.sql.Date.valueOf(str);
		} catch(IllegalArgumentException e) {
			try {
				//입력된 스트링이 날짜와 시간을 가지고 있으면...
				dd = (java.util.Date) java.sql.Timestamp.valueOf(str);
			} catch(IllegalArgumentException e2) {
				//Date나 Timestamp로 변환할 수 없는 형태의 스트링이 입력되면... 예로 yyyyMMdd
				//LogManager.error(e2,"Date나 Timestamp로 변환할 수 없는 형태의 스트링이 입력");
			}
		}

		//ksh 수정
		//SimpleDateFormat formatter = new SimpleDateFormat (format);
		//return  formatter.format(dd);
		
		return DateFormatUtils.format(dd, format);
	}

	/**
	System.out.println(formatDate( <br>
			"yyyy년mm월dd일",new java.util.Date()));과 같이 사용
	*/
	public static String formatDate(String format ,java.util.Date date)
    {

		//SimpleDateFormat simpledateformat = new SimpleDateFormat(format);
		//String str = simpledateformat.format(date);
		//return str;
		return DateFormatUtils.format(date, format);

    }

	public static NumberFormat nf = NumberFormat.getInstance();

	public static String formatString(String str) throws Exception{
		if(str==null) throw new Exception("포멧할 문자열이 없습니다");
		try{
			return nf.format(Long.parseLong(str));
		}catch(NumberFormatException e){
			throw new Exception("숫자 형식이 아닙니다(입력값:"+str);
		}
	}

	public static String formatString(long ln) throws Exception{
		return nf.format(ln);
	}

	/**
	percent 계산한후 기호 붙임 3.14 는 314% 가 된다.
	*/
	public  static  String toPercent(String str) {
		return formatString("Percent",str);
	}
	public  static  String toPercent(double num) {
		return formatString("Percent",num+"");
	}
	/**
	숫자 앞에 원화 기호를 붙임
	*/
	public  static  String toWon(String str) {
		return formatString("Won",str);
	}
	public  static  String toWon(double num) {
		return formatString("Won",num+"");
	}
	/**
	숫자 앞에 달라 기호를 붙임 4는 $4.00이 된다
	*/
	public  static  String toDollar(String str) {
		return formatString("Dollar",str);
	}
	public  static  String toDollar(double num) {
		return formatString("Dollar",num+"");
	}

	/**
	yyyymmdd->yyyy-mm-dd
	*/
	public static String yyyy_mm_dd(String s)
		throws Exception
	{
		if(s==null || s.length() != 8 ){
			throw new Exception ("날짜 변환함수에 잘못된 값이 입력되었습니다--"+s);
		}
		return s.trim().substring(0, 4) + "-" + s.trim().substring(4, 6) + "-" + s.trim().substring(6);
	}
	/**
	*
	*  중간에 SPACE가 있으면 제거시킴
	*/
	public static String getCompressString(String str)
	{
//		String resultStr = "";
//		int index = 0;
//
//		for( int strLen = str.length(); index < strLen ; index++)
//		{
//			 if( str.charAt(index) != ' ' && str.charAt(index) != '\t') resultStr += str.charAt(index);
//		}
//
//		return resultStr;

	    /*
	     * 위의 소스를 아래의 형태로 변경함
	     * 2004.07.30 - 이종원
	     */
	    final char SPACE = ' ';
	    final char TAB = '\t';

		StringBuffer buffer = new StringBuffer();

		for (int i = 0, strLength = str.length(); i < strLength; i++) {
		    char temp = str.charAt(i);
		    if (temp != SPACE && temp != TAB) {
		        buffer.append(temp);
		    }//end if
		}//end for

		return buffer.toString();
	}


	/**
	*
	*숫자 및 영문자등에 대한 전각문자(2Byte)로 변환
	*
	*/
	public static String getDBCS(String str)
	{
//		String result = "";
//		String curStr;
//		char   curChar;
//
//	    str = getCompressString(str);
//		int len = str.length();
//
//		for( int index = 0; index < len; index++)
//		{
//			curStr = str.substring(index,index+1);
//			curChar = ( curStr.getBytes().length == 1 ) ? (char)((65248) + curStr.charAt(0)) : curStr.charAt(0);
//			result    += curChar;
//		 }
//
//	   	 return result;

	    /*
	     * 위의 소스를 아래의 형태로 변경함
	     * 2004.07.30 - 이종원
	     */
	    //str = StringUtil.getCompressString(str);

		StringBuffer buffer = new StringBuffer();

		for (int i = 0, strLength = str.length(); i < strLength; i++) {
		    char temp = str.charAt(i);
		    if(temp==0x20) buffer.append((char)0x3000);
		    else buffer.append((temp < 256) ? (char)(65248 + temp) : temp);
		}//end for

		return buffer.toString();
	}
	
	/**
	*
	*숫자 및 영문자등에 대한 전각문자(2Byte)로 변환
	* lentgh param으로 받음
	*
	*/

	public static String getDBCS(String str, int length){

		StringBuffer buffer = new StringBuffer();

		for (int i = 0, strLength = str.length(); i < strLength; i++) {
		    char temp = str.charAt(i);
		    if(temp==0x20) buffer.append((char)0x3000);
		    else buffer.append((temp < 256) ? (char)(65248 + temp) : temp);
		}//end for

		int  bufferLen = buffer.toString().getBytes().length;

		if(bufferLen < length){
			for(int a=0; a<length - bufferLen; a++){
				buffer.append((char)0x3000);
				int nextBufferLen = buffer.toString().getBytes().length;
				if(nextBufferLen >= length) break;
			}
		}
		
		return buffer.toString();
	}		

	/**
	db컬럼을 자바의 beans의 set/get 패턴으로 변환해 주는 함수 (예:EMP_NO로 되어 있으면 empNo로 변환)
	*/
	public static String db2attr(String str){
		if(str==null) return null;
		str = str.toLowerCase();

		StringBuffer buf=null;

		int matchIdx = str.indexOf("_");
		if(matchIdx <1){
			return str;
		}else{
			char[] data = str.toCharArray() ;
			buf= new StringBuffer();
			boolean toUpper = false; //이전 문자가 _인지 판단 flag
			for(int i=0;i<data.length;i++){
				char ch = data[i];
				if(ch=='_'){
					toUpper=true;
					continue;
				}else{
					if(toUpper){
						ch = Character.toUpperCase(ch);
						toUpper=false;
					}
					buf.append(ch);
				}
			}
		}

		return buf.toString();
	}



/*
 *   삼성 카드 관련 유틸 모음 ------------------------------------------
 */

    /**
    *<p>  Date  : 2003.05.26
    *<p>  Description  : 문자열 내에 특수문자를 html문자로 바꿔준다
    *<p>  @author 전용현
    *  @param String 문자열
    *  @return String 문자열
    */
    public static String getHTMLCode(String text) {

        if( text == null || text.equals("") )
            return "";

        StringBuffer sb = new StringBuffer(text);
        char ch;

        for ( int i = 0; i < sb.length(); i++ ) {
            ch = sb.charAt(i);
            if ( ch == '<' ) {
                sb.replace(i, i+1, "&lt;");
                i += 3;
            } else if ( ch == '>' ) {
                sb.replace(i, i+1, "&gt;");
                i += 3;
            } else if ( ch == '&' ) {
                sb.replace(i, i+1, "&amp;");
                i += 4;
            } else if ( ch == '\'' ) {
                sb.replace(i, i+1, "&#39;");
                i += 4;
            } else if ( ch == '"' ) {
                sb.replace(i, i+1, "&quot;");
                i += 5;
            } else if ( ch == ' ' ) {
                sb.replace(i, i+1, "&nbsp;");
                i += 5;
            } else if ( ch == '\r' && sb.charAt(i+1) == '\n' ){
                sb.replace(i, i+2, "<BR>");
                i += 3;
            } else if ( ch == '\n' ){
                sb.replace(i, i+1, "<BR>");
                i += 3;
            }
        }
        return sb.toString();
    }


    /**
    *<p>  Date  : 2003.05.26
    *<p>  Description  : input type에 value로 쓸때 사용 ', " 만 변환시킴
    *<p>  @author 전용현
    *  @param String 문자열
    *  @return String 문자열
    */
    public static String getInputCode(String text) {

        if( text == null || text.equals("") )
            return "";

        StringBuffer sb = new StringBuffer(text);
        char ch;

        for ( int i = 0; i < sb.length(); i++ ) {
            ch = sb.charAt(i);
            if ( ch == '\'' ) {
                sb.replace(i, i+1, "&#39;");
                i += 4;
            } else if ( ch == '"' ) {
                sb.replace(i, i+1, "&quot;");
                i += 5;
            }
        }
        return sb.toString();
    }

    /**
    *<p>  Date  : 2003.05.27
    *<p>  Description  : 적립률 포맷을 맞춤 ( 03.40 -> 3.4 )
    *<p>  @author 전용현
    *  @param String 문자열
    *  @return String 문자열
    */
    public static String getRate(String rate) {

        try {
            double dbl = Double.valueOf(rate).doubleValue();

            if( dbl == 0 )
                return "0";

            return Double.toString(dbl);
        }catch(Exception e){
            return rate;
        }
    }

    /**
    *<p>  Date  : 2003.05.27
    *<p>  Description  : 전화번호 포맷을 맞춤 ( 032 244 58571232 -> 032-244-5857 )
    *<p>  @author 전용현
    *  @param String 문자열
    *  @return String 문자열
    */
    public static String getTelno(String telno) {

        try {
            //지역번호가 없는경우 '-'를 뺌
            String loc = telno.substring(0, 4).trim();
            loc += loc.equals("") ? "":"-";

            return loc + telno.substring(4, 8).trim() + "-" + telno.substring(8, 12).trim();
        }catch(Exception e){
            return telno;
        }
    }

    /**
    *<p>  Date  : 2003.07.11
    *<p>  Description  : 전화번호 포맷을 맞춤 ( 032 244 58571232 -> 032) 244-5857 )
    *<p>  @author 정유석
    *  @param String 문자열
    *  @return String 문자열
    */
    public static String getTelno2(String telno) {

        try {
            //지역번호가 없는경우 '-'를 뺌
            String loc = telno.substring(0, 4).trim();
            loc += loc.equals("") ? "":") ";

            return loc + telno.substring(4, 8).trim() + "-" + telno.substring(8, 12).trim();
        }catch(Exception e){
            return telno;
        }
    }


	/**
	 * @param line
	 * @param string
	 * @return
	 */

	public static ArrayList toArrayList(String str, String del) {
		if(str == null) return null;

		StringTokenizer st = new StringTokenizer(str, del);

		ArrayList arr = new ArrayList();
		while (st.hasMoreTokens()) {
			String temp = st.nextToken();
			if(temp != null){
				temp = temp.trim();
			}
			arr.add(temp);
		}
		return arr;

	}

	/**
	 * 일반 문자열을 xml 안에 넣기 위해 엔티티를 변환 한다.
	 * @param str
	 * @return
	 */
	public static String escapeXml(String str){
		str = str.replaceAll("&", "&amp;");	//이걸 아래에 쓰면 잘못된다.
		str = str.replaceAll("<", "&lt;");
		str = str.replaceAll(">", "&gt;");
		str = str.replaceAll("\"", "&quot;");
		str = str.replaceAll("'", "&apos;");
		return str;
	}

    /**
    값이 null 또는 ""이면 화면에 default값 리턴-구정은
    */
    public static String DFLTNVL(Object obj,String deflt){
        if(obj != null && !obj.equals("") ) return obj.toString();
        else return deflt;
    }

//    /** Regular Expression 파싱을 위한 클래스 */
//    private static final Perl5Util REGEXP_UTIL = new Perl5Util();
//    /** 파싱 결과를 임시적으로 담기 위한 Collection */
//    private static final List TEMP_LIST = new ArrayList();
//
//    /**
//     * 주어진 문자열을 주어진 구분자 단위로 나눈 배열을 리턴합니다.<BR>
//     * <BR>
//     * <FONT COLOR='RED'>내부적으로 Jakarta ORO 의 Regular Expression 을 사용하여 구분하므로,
//     * 구분자는 Regular Expresssion 에 적합한 식이 와야 합니다.</FONT>
//     * <BR><BR>
//     * <PRE>
//     * 사용 예) 구분자가 Regular Expression 에서 사용되는 특정 문자가 포함되지 않은 경우
//     *
//     * String [] result = split("ABC&DEF&GHI", "&");
//     * for (int i = 0; i &lt; result.length; i++) {
//     *      System.out.println("[" + result[i] + "]");
//     * }//end for
//     *
//     * 출력결과 )
//     * [ABC]
//     * [DEF]
//     * [GHI]
//     *
//     * 사용 예) 구분자가 Regular Expression 에서 사용되는 특정 문자가 포함된 경우('|', '^' 등의 문자)
//     * 이때에는 해당 문자 앞에 '\'를 붙여야 합니다.
//     *
//     * String [] result = split("ABC||DEF||GHI", "\\|\\|");
//     * for (int i = 0; i &lt; result.length; i++) {
//     *      System.out.println("[" + result[i] + "]");
//     * }//end for
//     *
//     * 출력결과 )
//     * [ABC]
//     * [DEF]
//     * [GHI]
//     *
//     * 사용 예) 구분자가 반복되어 나타나는 경우(구분자 사이에 데이터가 존재하지 않는 경우)
//     *
//     * String [] result = split("ABC&&GHI", "&");
//     * for (int i = 0; i &lt; result.length; i++) {
//     *      System.out.println("[" + result[i] + "]");
//     * }//end for
//     *
//     * 출력결과 )
//     * [ABC]
//     * []
//     * [GHI]
//     *
//     * </PRE>
//     * <BR>
//     * <FONT COLOR='RED'>성능향상을 위해서 Regular Expression 패턴 파싱을 위한 객체를 static final 의 상수로 선언하고 있습니다.
//     * 또한, 파싱 결과를 담기 위한 List 도 temp 로 담고 있으므로,
//     * multi-thread 로 접근 시 동기화를 유지하기 위하여 'synchronized' 메소드로 구현되었습니다.</FONT>
//     * <BR><BR>
//     *
//     * @param src 나누려는 대상 문자열
//     * @param delimExp 구분자를 표현하는 Regular Expression
//     * @return 구분자에 의해서 구분 된 문자열의 배열
//     * @author 이종원
//     * @version 2004.07.22
//     */
//    public synchronized static String [] split(String src, String delimExp) {
//        TEMP_LIST.clear();
//        REGEXP_UTIL.split(TEMP_LIST, "/" + delimExp + "/", src, Perl5Util.SPLIT_ALL);
//        String [] splitedString = new String[TEMP_LIST.size()];
//        TEMP_LIST.toArray(splitedString);
//        return splitedString;
//    }//end of split()
    

    /**
     * 주어진 문자열을 주어진 구분자 단위로 나눈 배열을 리턴합니다.<BR>
     * <BR>
     * <FONT COLOR='RED'>내부적으로 Regular Expression 을 사용하여 구분하므로,
     * 구분자는 Regular Expresssion 에 적합한 식이 와야 합니다.</FONT>
     * <BR><BR>
     * <PRE>
     * 사용 예) 구분자가 Regular Expression 에서 사용되는 특정 문자가 포함되지 않은 경우
     *
     * String [] result = split("ABC&DEF&GHI", "&");
     * for (int i = 0; i &lt; result.length; i++) {
     *      System.out.println("[" + result[i] + "]");
     * }//end for
     *
     * 출력결과 )
     * [ABC]
     * [DEF]
     * [GHI]
     *
     * 사용 예) 구분자가 Regular Expression 에서 사용되는 특정 문자가 포함된 경우('|', '^' 등의 문자)
     * 이때에는 해당 문자 앞에 '\'를 붙여야 합니다.
     *
     * String [] result = split("ABC||DEF||GHI", "\\|\\|");
     * for (int i = 0; i &lt; result.length; i++) {
     *      System.out.println("[" + result[i] + "]");
     * }//end for
     *
     * 출력결과 )
     * [ABC]
     * [DEF]
     * [GHI]
     *
     * 사용 예) 구분자가 반복되어 나타나는 경우(구분자 사이에 데이터가 존재하지 않는 경우)
     *
     * String [] result = split("ABC&&GHI", "&");
     * for (int i = 0; i &lt; result.length; i++) {
     *      System.out.println("[" + result[i] + "]");
     * }//end for
     *
     * 출력결과 )
     * [ABC]
     * []
     * [GHI]
     *
     * </PRE>
     * <BR><BR>
     *
     * @param src 나누려는 대상 문자열
     * @param delimExp 구분자를 표현하는 Regular Expression
     * @return 구분자에 의해서 구분 된 문자열의 배열
     * @author 조만희
     * @version 2006.02.15
     */
    public static String [] split(String src, String delimExp) {
        return src.split(delimExp);
    }//end of split()

    /**
     * 구분자에 의해 분리된 string을 분리하여 공백을 없앤후 string array 로 만든다.
     *
     * @param str 구분자에 의해 분리된 string
     * @param delim 구분자
     * @return 분리된 string array
     * @exception UtilException error occurs
     */
     public static String[] splitTrim(String str, String delim) throws Exception {
       StringTokenizer st = new StringTokenizer(str, delim);
       String[] strArr = null;

       try {
         int count = st.countTokens();
         strArr = new String[count];
         for(int i=0; st.hasMoreTokens(); i++) {
           strArr[i] = st.nextToken().trim();
         }
       }
       catch( NoSuchElementException nsee) {
         throw new Exception(nsee);
       }
       return strArr;
    }
     
    /**
     * '%' 를 문자열의 앞 뒤로 붙여줍니다.
     * (쿼리의 조건절 중에서 LIKE 문에 사용)
     *
     * @param src 대상 문자열
     * @return 대상 문자열의 앞뒤로 '%'가 붙은 문자열
     */
    public static String appendPercentChar(String src) {
        StringBuffer buffer = new StringBuffer();
        buffer.append('%').append(src).append('%');
        return buffer.toString();
    }//end of appendLikeChar()


    /**
     *	byte[] ret = HashUtil.digest("MD5", "abcd".getBytes());
     *  처럼 호출
     */
    public static byte[] digest(String alg, byte[] input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(alg);
        return md.digest(input);
    }

    /**
     * 사용자 비밀번호 등을 암호화 한다. 기본적으로 복호화가 불가능한 hash 알고리즘으로 암호화 한다.
     * @param inputValue
     * @return
     * @throws Exception
     */
    public static String digest(String inputValue)  {
    	String result = null;
        if( inputValue == null ) {
        	LogManager.info("암호화 하는 곳에 NULL VALUE 입력됨");
        	return null;
        }
        byte[] ret;
		try {
			ret = digest("MD5", inputValue.getBytes());
			result = base64encode(ret);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
        return result;
    }
    /**
     *	Base64Encoding을 수행한다. binany in ascii out
     *
     *	@param encodeBytes encoding할 byte array
     *	@return encoding 된 String
     */
    public static String base64encode(byte[] encodeBytes) {
        return Base64.getEncoder().encodeToString(encodeBytes);
    }

    /**
     *	Base64Decoding 수행한다. binany out ascii in
     *
     *	@param strDecode decoding할 String
     *	@return decoding 된 byte array
     */
    public static byte[] base64decode(String strDecode) {
        return Base64.getDecoder().decode(strDecode);
    }

	/**
	 * 주어진 문자열을 출력을 위해 포맷팅 된 Hex String 의 문자열로 변환하여 리턴합니다.
	 *
	 * @param target 대상 문자열
	 * @return 출력을 위해 포맷팅 된 Hex String 의 형태로 변환된 문자열
	 */
	public static String printFormattedToHexString(String target) {
	    return printFormattedToHexString(target.getBytes());
	}//end of printFormattedToHexString()

	/**
	 * 주어진 bute [] 을 출력을 위해 포맷팅 된 Hex String 의 문자열로 변환하여 리턴합니다.
	 *
	 * @param target 대상 byte []
	 * @return 출력을 위해 포맷팅 된 Hex String 의 형태로 변환된 byte []
	 */
	public static String printFormattedToHexString(byte [] target) {
	    StringBuffer buffer = new StringBuffer("[");

	    for (int i = 0; i < target.length; i++) {
	        buffer.append(byte2HexString(target[i]));
	        if (i != target.length - 1) {
	            buffer.append(",");
	        }//end if
	    }//end for

	    buffer.append("]");
	    return buffer.toString();
	}//end of printFormattedToHexString()


    /**
     * 주어진 문자열을 출력을 위해 포맷팅 된 Hex String 의 문자열로 변환하여 리턴합니다.
     *
     * @param target 대상 문자열
     * @param lineShowCharSize 한 라인에 출력할 문자열의 갯수
     * @return 일정한 형식으로 포맷팅 된 Hex String
     */
    public static String printHexString(String target, int lineShowCharSize) {
        return printHexString(target.getBytes(), lineShowCharSize);
    }//end of printHexString()

    /**
     * <pre>
     * 주어진 bute [] 을 출력을 위해 포맷팅 된 Hex String 의 문자열로 변환하여 리턴합니다.
     *
     * 주의!) 이 메소드는 성능상의 문제가 있을 수 있으므로, 디버깅 모드에서만 사용합니다!!
     *
     * 예)
     * String testHexString = "ABC\r\nDEFGHI\r\nJKL\r\nMNOPQ\r\nRSTU\r\nVWXYZ\r\n01234\r\n56\r\n789조만희";
     * System.out.println(printHexString(testHexString, 10));
     * 
     * ==========================================================
     * Source bytes Length : [58]
     * ==========================================================
     * [   Row Number] 01 02 03 04 05 06 07 08 09 10 | [    DATA]
     * ==========================================================
     * [000001-000010] 41 42 43 0D 0A 44 45 46 47 48 | ABC..DEFGH
     * [000011-000020] 49 0D 0A 4A 4B 4C 0D 0A 4D 4E | I..JKL..MN
     * [000021-000030] 4F 50 51 0D 0A 52 53 54 55 0D | OPQ..RSTU.
     * [000031-000040] 0A 56 57 58 59 5A 0D 0A 30 31 | .VWXYZ..01
     * [000041-000050] 32 33 34 0D 0A 35 36 0D 0A 37 | 234..56..7
     * [000051-000058] 38 39 C1 B6 B8 B8 C8 F1       | 89조만희
     * ==========================================================
     * </pre>
     *
     * @param target 대상 byte []
     * @param lineShowCharSize 한 라인에 출력할 문자열의 갯수
     * @return 일정한 형식으로 포맷팅 된 Hex String
     */
    public static String printHexString(byte [] target, int lineShowCharSize) {

        byte [] cloneTarget = new byte[target.length];

        // 아래는 Control Character 일 경우, '.' 문자로 replace 하는 로직이다.
        for (int i = 0; i < cloneTarget.length; i++) {
            cloneTarget[i] = (target[i] >= 0 && target[i] < 32) ? 46 : target[i];
        }//end for

        StringBuffer buffer = new StringBuffer();

        buffer.append(prettyPrint(lineShowCharSize));
        buffer.append("Source bytes Length : [").append(target.length).append("]\n");
        buffer.append(prettyPrint(lineShowCharSize));

        buffer.append("[   Row Number]");
        for (int i = 1; i <= lineShowCharSize; i++) {
            buffer.append(" ").append(FormatUtil.lPadding(Integer.toString(i), 2, '0'));
        }//end for
        buffer.append(" | [").append(FormatUtil.lPadding("DATA]", lineShowCharSize - 1, ' '));
        buffer.append("\n");

        buffer.append(prettyPrint(lineShowCharSize));

        int printCount = target.length % lineShowCharSize;
        printCount = printCount == 0 ? target.length / lineShowCharSize : target.length / lineShowCharSize + 1;

        for (int i = 0; i < printCount; i++) {
            buffer.append("[").append(FormatUtil.lPadding(Integer.toString(i * lineShowCharSize + 1), 6, '0')).append("-");
            if (i == printCount) {
                buffer.append(FormatUtil.lPadding(Integer.toString(target.length), 6, '0'));
            } else {
                buffer.append(FormatUtil.lPadding(Integer.toString((i + 1) * lineShowCharSize), 6, '0'));
            }//end if else
            buffer.append("]");

            for (int j = 0; j < lineShowCharSize; j++) {
                if (i * lineShowCharSize + j == target.length) {
                    break;
                }//end if
                buffer.append(" ").append(byte2HexString(target[i * lineShowCharSize + j]));
            }//end for

            if (target.length % lineShowCharSize != 0 && i == (printCount - 1)) {
                int appendCount = (lineShowCharSize - (target.length % lineShowCharSize)) * 3;
                buffer.append(FormatUtil.rPadding("", appendCount, ' '));
                buffer.append(" | ");
                buffer.append(new String(cloneTarget, i * lineShowCharSize, target.length % lineShowCharSize));
            } else {
                buffer.append(" | ");
                buffer.append(new String(cloneTarget, i * lineShowCharSize, lineShowCharSize));
            }//end if else

            buffer.append("\n");

        }//end for

        buffer.append(prettyPrint(lineShowCharSize));

        return buffer.toString();
    }//end of printHexString()

    /**
     * 이쁘게 포맷팅하기 위한 메소드
     *
     * @param lineShowCharSize 한 라인에 출력할 문자열의 갯수
     * @return 이쁘게 포맷팅 된 String
     */
    private static String prettyPrint(int lineShowCharSize) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("===============");
        for (int i = 1; i <= lineShowCharSize; i++) {
            buffer.append("===");
        }//end for
        buffer.append("===");
        for (int i = 1; i <= lineShowCharSize; i++) {
            buffer.append("=");
        }//end for
        buffer.append("\n");
        return buffer.toString();
    }//end of prettyPrint()

    /**
     * 주어진 bute 를 출력을 위해 포맷팅 된 Hex String 의 문자열로 변환하여 리턴합니다.
     *
     * @param target 대상 byte
     * @return 출력을 위해 포맷팅 된 Hex String 의 형태로 변환된 byte
     */
    public static String byte2HexString(byte target) {
        int i = (target < 0) ? target & 127 + Byte.MAX_VALUE + 1 : target;
        String targetString = Integer.toHexString(i).toUpperCase();
        return FormatUtil.lPadding(targetString, 2, '0');
    }//end of byte2HexString()


	/**
	 * 주어진 문자열을 Hex String 의 문자열로 변환하여 리턴합니다.
	 *
	 * @param target 대상 문자열
	 * @return Hex String 의 형태로 변환된 문자열
	 */
	public static String toHexString(String target) {
	    return toHexString(target.getBytes());
	}//end of toHexString()

	/**
	 * 주어진 bute [] 을 Hex String 의 문자열로 변환하여 리턴합니다.
	 *
	 * @param target 대상 byte []
	 * @return Hex String 의 형태로 변환된 byte []
	 */
	public static String toHexString(byte [] target) {
	    StringBuffer buffer = new StringBuffer();

	    for (int i = 0; i < target.length; i++) {
	        buffer.append(Integer.toHexString(target[i] & 0xff));
	    }//end for

	    return buffer.toString();
	}//end of toHexString()

	/** 소수점을 나타내는 상수 */
	public static final String FLOATING_POINT = ".";
	/** '0'을 나타내는 상수 */
	public static final String ZERO_CHAR = "0";

	/**
	 * 주어진 부동소수를 정수부[0]와 소수부[1]로 나누어 리턴합니다.
	 *
	 * @param target 부동소수
	 * @return 정수부[0]와 소수부[1]로 나뉜 문자열의 배열
	 */
	public static final String [] splitDouble(double target) {
	    return splitDouble(Double.toString(target));
	}//end of splitDouble()

	/**
	 * 주어진 부동소수형 문자를 정수부[0]와 소수부[1]로 나누어 리턴합니다.
	 *
	 * @param target 부동소수형 문자
	 * @return 정수부[0]와 소수부[1]로 나뉜 문자열의 배열
	 */
	public static final String [] splitDouble(String target) {
	    String [] splitResult = new String[2];
        // 금액 부가 소수점이 없을 경우가 있으므로,
        // 그러한 경우에 대해서 대비하도록 만들었다.

        int dotIndex = target.indexOf(FLOATING_POINT);

        if (dotIndex == 0) {
            splitResult[0] = ZERO_CHAR;
            splitResult[1] = target.substring(dotIndex + 1);
        } else if (dotIndex != -1) {
            splitResult[0] = target.substring(0, dotIndex);
            if (target.endsWith(FLOATING_POINT)) {
                splitResult[1] = ZERO_CHAR;
            } else {
                splitResult[1] = target.substring(dotIndex + 1);
            }//end if else
        } else {
            splitResult[0] = target;
            splitResult[1] = ZERO_CHAR;
        }//end if else

	    return splitResult;
	}//end of splitDouble()

	/** 정수부의 자리수가 소수부의 자리수를 포함하는 경우 */
	public static final boolean CONTAIN_FLOAT_SIZE = true;
	/** 정수부의 자리수가 소수부의 자리수를 포함하지 않는 경우 */
	public static final boolean NOT_CONTAIN_FLOAT_SIZE = false;

	/**
	 * 주어진 문자열을 주어진 소수부 자릿수 구분자를 가지고
	 * 정수부의 자릿수와 소수부의 자릿수를 구분하여 리턴합니다.
	 *
	 * @param target 정수부와 소수부의 자릿수를 포함하는 문자열
	 * @param pointingDelim 정수부와 소수부를 구분하는 문자열
	 * @return
	 */
	public static final int [] splitFloatSize(String target, String pointingDelim, boolean isContainFloatSize) {
	    int [] splitResult = new int[2];
	    String [] splitString = StringUtil.split(StringUtil.getCompressString(target), pointingDelim);

	    splitResult[1] = Integer.parseInt(splitString[1]);
	    splitResult[0] = isContainFloatSize ? Integer.parseInt(splitString[0]) - splitResult[1] : Integer.parseInt(splitString[0]);
	    return splitResult;
	}//end of splitFloatSize()
	
	/**
	 * 전각문자를 반각문자로 변환합니다.
	 * 
	 * @param str 전각문자가 포함된 문자열
	 * @return 전각문자가 반각문자로 변환된 문자열
	 * @author Helexis
	 * @since 2005.05.17
	 */
	public static String convertDBCS(String str) {
	    if (str == null || str.trim().length() == 0) {
	        return "";
	    }//end if
		StringBuffer buffer = new StringBuffer();
		for (int i = 0, strLength = str.length(); i < strLength; i++) {
		    char temp = str.charAt(i);
		    /*
		     * 전각문자에서 영문자 혹은 숫자 들은 변환이 이루어지지만,
		     * 2 bytes 공백의 경우에는 변환이 이루어지지 않으므로 0x3000 으로 비교하여 변환함.
		     * 
		     * 2005.05.17 - Helexis
		     */
		    buffer.append((temp >= 65248 && temp < 65248 + 256) ? (char)(temp - 65248) : (temp == 0x3000) ? 0x20 : temp);
		}//end for

		return buffer.toString();
	}//end of convertDBCS()
	
	/**
	 * ASCII 문자를 EBCDIC 문자로
	 * 
	 * @param ASCII 문자열
	 * @return EBCDIC 문자열
	 * @author Helexis
	 * @since 2005.05.17
	 */
	public static String convertEBCDICFromASCII(String str) {
		return str;
	}//end of convertDBCS()
	
	/**
	 * EBCDIC 문자를 ASCII 문자로
	 * 
	 * @param EBCDIC 문자열
	 * @return ASCII 문자열
	 * @author Helexis
	 * @since 2005.05.17
	 */
	public static String convertASCIIFromEBCDIC(String str) {
	    return str;
	}//end of convertDBCS()
	/**
	 * 전각문자를 반각문자로 변환한 후, trim 을 수행한 결과를 리턴합니다.
	 * 
	 * @param str 전각문자가 포함된 문자열
	 * @return 전각문자가 반각문자로 변환된 후, trim 된 문자열
	 * @author Helexis
	 * @since 2005.05.17
	 */
	public static String convertDBCSAndTrim(String str) {
	    return convertDBCS(str).trim();
	}//end of convertDBCSAndTrim()
	
	/**
	 * @param list
	 * @return
	 */
	public static String getAppendedString(List list) {
		if (list == null || list.size() == 0) {
			LogManager.debug("리스트 사이즈가 0 입니다");
			return null;
		}
		Iterator i = list.iterator();
		i.hasNext();
		StringBuffer buf = new StringBuffer("'" + i.next() + "'");
		while (i.hasNext()) {
			buf.append(",'" + i.next() + "'");
		}
		return buf.toString();
	}
	
	/**
	 * 원본 String의 오른쪽에 붙어 있는 filler를 제거하여 리턴한다.
	 * @param source 원본 String
	 * @param filler 
	 * @return
	 */
	public static String removeRightFiller(String source, char filler){
		
		if(StringUtil.isNull(source)) return source;
		if(source.charAt(source.length()-1)!=filler) return source;

		for(int i=source.length()-1; i>=0; i--){
			if(source.charAt(i)==filler) source = source.substring(0, i);
			else break;
		}
		return source;
		
	}
	
	/**
	 * 원본 String의 왼쪽에 붙어 있는 filler를 제거하여 리턴한다.
	 * @param source 원본 String
	 * @param filler 
	 * @return
	 */
	public static String removeLeftFiller(String source, char filler){
		if(StringUtil.isNull(source)) return source;
		if(source.charAt(0)!=filler) return source;

		for(int i=0; i<source.length();){
	
			if(source.charAt(i)==filler){
				source = source.substring(i+1, source.length());
			}
			else break;
		}
		return source;
	}
	
	/**
	 * JDK1.5의 contains 함수를 지원하기 위해서...
	 * @param src
	 * @param s
	 * @return
	 */
	public static boolean contains(String src, String s) {
        return src.indexOf(s) > -1;
    }
	
	
	/**
	 * + / - 부호가 붙은 숫자 스트링을 소수점 처리를 해서 총길이 만큼 앞자리에 0을 붙여서 리턴한다.
	 * 앞자리에 + 기호가 없으면 +를 붙여서 처리한다.
	 * <pre>
	 * (예)
	 * String formattedNumber1 = formatSignedNumber("+222.40888", 10, 3);
	 * formattedNumber1 --> "+000222408"
	 *
	 * String formattedNumber2 = formatSignedNumber("222444", 10, 3);
	 * formattedNumber2 --> "+222444000"
	 * 
	 * String formattedNumber3 = formatSignedNumber("-222", 10, 5);
	 * formattedNumber3 --> "-022200000"
	 * 
	 * </pre>
	 * @param number 숫자 문자열
	 * @param totalLength 전체길이(부호포함 길이)
	 * @param scale 소수점 자리수 (0이상)
	 * @return IllegalArgumentException - scale이 0보다 작을 때
	 */
	public static String formatSignedNumber(String number, int totalLength, int scale){
		if(number==null) return null;
		if(scale<0){
			throw new IllegalArgumentException("SCALE 값은 0 이상 이어야 합니다.");
		}
		number = number.trim();
		if(number.charAt(0)!='+' && number.charAt(0)!='-') number = "+" + number;
		
				
		//1. scale 처리
        String[] splitValue = StringUtil.splitDouble(number);
        splitValue[1] = FormatUtil.rPadding(splitValue[1], scale, '0');
        number = (splitValue[0].equals("0")?"":splitValue[0]) + splitValue[1];
        
        //2. filler처리
        String filledNumber = FormatUtil.lPadding(number.substring(1), totalLength-1, '0');
        return number.substring(0, 1) + filledNumber;
       
	}
	
	/*public static String formatSignedNumber(double number, int totalLength, int scale){
		return formatSignedNumber(Double.toString(number), totalLength, scale);
		
	}*/

	/**
	 * + / - 부호가 붙은 숫자 스트링에서 앞자리 filler 0 과 소수점 처리를 제거하여 리턴한다.
	 * 	 * 
	 *  <pre>
	 *  (예)
	 *  String number1 = parseSignedNumber("+000222408", 3, false);
	 *  --> "222.408"
	 *  
	 *  String number1 = parseSignedNumber("+000222408", 3, true);
	 *  --> "222.408"
	 *  
     *	String number2 = parseSignedNumber("00022240800", 2, false);
     *  --> "222408.00"
     *  
     *  String number2 = parseSignedNumber("00022240800", 2, true);
     *  --> "222408"
     *  
     *	String number3 = parseSignedNumber("-000222408", 5, false));
     *  --> "-2.22408"
     *  
     *  String number3 = parseSignedNumber("-000222408", 5, true));
     *  --> "-2.22408"
     *  </pre>
     *  
	 * @param foramttedSignedNumber 포맷된 형태의 숫자 스트링
	 * @param scale 적용된 스케일 값 (0이상)
	 * @param trimZeroAfterDot 소수부 뒤의 0을 잘라내고 리턴할 지 여부
	 * @return IllegalArgumentException :scale이 0보다 작을 때, 첫번째 인자(숫자 문자열) 길이가  SCALE 값보다 작을 때
	 */
	public static String parseSignedNumber(String foramttedSignedNumber, int scale, boolean trimZeroAfterDot){
		if(foramttedSignedNumber==null) return null;
		if(scale<0){
			throw new IllegalArgumentException("SCALE 값은 0 이상 이어야 합니다.");
		}
		if(foramttedSignedNumber.length()<=scale){
			throw new IllegalArgumentException("첫번째 인자(숫자 문자열) 길이가  SCALE 값보다 커야 합니다.");
		}
		String number = foramttedSignedNumber.trim();
		if(number.charAt(0)!='+' && number.charAt(0)!='-') number = "+" + number;
		char sign = number.charAt(0);
		
		number = number.substring(1);
		
		//1. scale 처리
        if(scale>0){
            StringBuffer buffer=new StringBuffer();
            buffer.setLength(0);
            buffer.append(number.substring(0, number.length()-scale));
            buffer.append("."); 
            buffer.append(number.substring(number.length()-scale));
                       
            number=buffer.toString();
        }
        
        number = number.trim();
        
        //2. filler 제거 
        number = StringUtil.removeLeftFiller(number, '0');
        //맨 앞이 .이면 앞에 0을 붙인다.
    	if(number.charAt(0)=='.'){
    		number = "0" + number;
    	}
        
        //filler를 제거한 후 빈 공백만 남으면 "0"을 리턴한다.
        if(number.equals("")) return "0";
        
        //3. trimZeroAfterDot가 true이면 소수부 뒤의 0을 잘라낸다.
        if(trimZeroAfterDot){
        	//뒤에 붙은 0을 잘라낸다.
        	number = StringUtil.removeRightFiller(number, '0');
        	//맨 뒤가 .이면 잘라낸다.
        	if(number.charAt(number.length()-1)=='.'){
        		number = number.substring(0, number.length()-1);
        	}
        }
        
        //filler를 제거한 후 빈 공백만 남으면 "0"을 리턴한다.
        if(number.equals("")) return "0";
        
        //부호가 -일 경우에만 부호를 붙여 리턴한다.
        if(sign=='-') return sign + number;
        else return number;
        
	}
	

	
	/**
	 * 작성일자 : 2008. 05. 08 
	 * 작성자 : kfetus
	 * 설명 : 숫자 타입(순수 숫자,소숫점이 있는 숫자)이 입력으로 들어오면 좌측의 0값을 제거함.
	 * @param num
	 * @return 좌측 0이 제거된 스트링.
	 * "0000000000000" -> 0
	 * "0900000000010" -> 900000000010
	 * "0000000001000" -> 1000
	 * "09.0000000010" -> 9.0000000010
	 * "00000000.1000" -> 0.1000
	 * "0000000001.00" -> 1.00
	 * "00000000.1001" -> 0.1001
	 * "000000001.001" -> 1.001
	 * "000000001001." -> 1001
	 * "000000000000." -> 0
	 */
	public static String parseNumberType(String num){
		
		if(StringUtil.isNull(num)) return num;
		
		if( num.indexOf(".") > 0){
			String preValue = num.substring(0,num.indexOf("."));
			String postValue = num.substring(num.indexOf(".")+1);
			preValue =removeLeftFiller(preValue,'0');
			if(preValue.length()==0) preValue="0";
			if( postValue.length()!=0 ){
				num = preValue+"."+postValue;
			} else {
				num = preValue;
			}
		} else {
			num=removeLeftFiller(num,'0');
		}
		if(num.length()==0) num="0";
		return num;
	}
	
	
	
    public static void main(String args[]){
//    	String[] list = {"0000000000000"
//				,"0900000000010"
//				,"0000000001000"
//				,"09.0000000010"
//				,"00000000.1000"
//				,"0000000001.00"
//				,"00000000.1001"
//				,"000000001.001"
//				,"000000001001."
//				,"000000000000."};
//    	for(int i =0;i<list.length;i++) {
//    		parseNumberType(list[i]);
//    	}
//    	String s = "안경아   abcd";
//    	System.out.println("["+s+(char)0x20+(char)0x20+"]");
//    	s = getDBCS(s);
//    	 System.out.println("반각을 전각으로 :"+s);
//    	 System.out.println("전각을 반각으로 :"+convertDBCS(s));
//    	System.out.println(formatSignedNumber("   222.40888 ", 10, 3));
//    	System.out.println(formatSignedNumber("222444", 10, 3));
//    	System.out.println(formatSignedNumber(" -222", 10, 5));
//    	System.out.println("=====parse=====");
//    	System.out.println(parseSignedNumber("+000222408  ", 3, true));
//    	System.out.println(parseSignedNumber("   000222408010 ", 2, false));
//    	System.out.println(parseSignedNumber("   000222408010 ", 2, true));
//    	System.out.println(parseSignedNumber("-000222408", 5, true));
//    	System.out.println(parseSignedNumber("-000222408000300", 5, true));
//    	System.out.println(parseSignedNumber("-0100000", 5, true));
//    	System.out.println(parseSignedNumber("-0100000", 5, false));
//    	System.out.println(parseSignedNumber("-0003000", 5, true));
//    	if(true) return;
//    	
//    	String s = "this is xml < aaa > \" & ";
//    	System.out.println(escapeXml(s));
//
//    	/*==================================================
//    	 * 여기서 부터는 getDBCS() 메소드의 테스트 코드 입니다.
//    	 ==================================================*/
    	//String a = "a a	a조 만	희";
//    	String a = "12345";
//    	System.out.println("StringUtil.getDBCS(a) : [" + StringUtil.getDBCS(a) + "]");
//
//    	System.out.println("StringUtil.getDBCS(a).getBytes().length : [" + StringUtil.getDBCS(a).getBytes().length + "]");

    	String b = "12345";
    	int len = 20;
    	System.out.println("StringUtil.getDBCS(b) : [" + StringUtil.getDBCS(b,len) + "]");

    	System.out.println("StringUtil.getDBCS(b,len).getBytes().length : [" + StringUtil.getDBCS(b,len).getBytes().length + "]");

    	
//    	/*==================================================
//    	 * 여기서 부터는 getCompressString() 메소드의 테스트 코드 입니다.
//    	 ==================================================*/
//    	int count = 10000;
//    	long startTime = System.currentTimeMillis();
//    	for (int i = 0; i < count; i++) {
//    	    StringUtil.getCompressString(a);
//    	}//end for
//    	System.out.println(count + "회 반복시 소요시간 : [" + (System.currentTimeMillis() - startTime) + "] ms");
//
//    	/////////////MESSAGE DIGEST TEST /////////////////////////////
//    	System.out.println("kebcls is digested to : ["+ digest("kebcls")+"]");
//    	System.out.println("test123 is digested to : ["+ digest("test123")+"]");
//
//
//    	/*==================================================
//    	 * 여기서 부터는 splitDouble() 메소드의 테스트 코드 입니다.
//    	 ==================================================*/
//		String tFloat = "123.456";
//		String [] sResult = splitDouble(tFloat);
//		System.out.println("[" + tFloat + "] => 정수부 : [" + sResult[0] + "], 소수부 : [" + sResult[1] + "]");
//
//		tFloat = "123.";
//		sResult = splitDouble(tFloat);
//		System.out.println("[" + tFloat + "] => 정수부 : [" + sResult[0] + "], 소수부 : [" + sResult[1] + "]");
//
//		tFloat = "123";
//		sResult = splitDouble(tFloat);
//		System.out.println("[" + tFloat + "] => 정수부 : [" + sResult[0] + "], 소수부 : [" + sResult[1] + "]");
//
//		tFloat = ".456";
//		sResult = splitDouble(tFloat);
//		System.out.println("[" + tFloat + "] => 정수부 : [" + sResult[0] + "], 소수부 : [" + sResult[1] + "]");
//
//		double tDouble = 123.456;
//		String [] dResult = splitDouble(tDouble);
//		System.out.println("[" + tDouble + "] => 정수부 : [" + dResult[0] + "], 소수부 : [" + dResult[1] + "]");
//
//		tDouble = 123.;
//		dResult = splitDouble(tDouble);
//		System.out.println("[" + tDouble + "] => 정수부 : [" + dResult[0] + "], 소수부 : [" + dResult[1] + "]");
//
//		tDouble = 123;
//		dResult = splitDouble(tDouble);
//		System.out.println("[" + tDouble + "] => 정수부 : [" + dResult[0] + "], 소수부 : [" + dResult[1] + "]");
//
//		tDouble = .456;
//		dResult = splitDouble(tDouble);
//		System.out.println("[" + tDouble + "] => 정수부 : [" + dResult[0] + "], 소수부 : [" + dResult[1] + "]");
//
//    	/*==================================================
//    	 * 여기서 부터는 splitFloatSize() 메소드의 테스트 코드 입니다.
//    	 ==================================================*/
//		String target = " 10, 5 ";
//		int [] sr = StringUtil.splitFloatSize(target, ",", StringUtil.CONTAIN_FLOAT_SIZE);
//		System.out.println("CONTAIN_FLOAT_SIZE [" + target + "] => 정수부 길이 : [" + sr[0] + "], 소수부 길이 : [" + sr[1] + "]");
//
//		sr = StringUtil.splitFloatSize(target, ",", StringUtil.NOT_CONTAIN_FLOAT_SIZE);
//		System.out.println("NOT_CONTAIN_FLOAT_SIZE [" + target + "] => 정수부 길이 : [" + sr[0] + "], 소수부 길이 : [" + sr[1] + "]");
//
//
//        /*==================================================
//         * 여기서 부터는 splitFloatSize() 메소드의 테스트 코드 입니다.
//         ==================================================*/
//        String testHexString = "ABC\r\nDEFGHI\r\nJKL\r\nMNOPQ\r\nRSTU\r\nVWXYZ\r\n01234\r\n56\r\n789조만희";
//        System.out.println(printHexString(testHexString, 10));
//        System.out.println("["+digest("01692251993")+"]");
//        
//        String fileName = "\\../../test.txt";
//        System.out.println(contains(fileName, ".."));
    }
    
    public static String fillZero(String str, int len) {
		str = str.trim();
		int numLen = str.length();
		String strTmp = "";
		for ( int i=numLen ; i<len ; i++ ) {
				strTmp += "0";
		}
		return strTmp + str;
	}
    /**
     * 문자열의 원하는 위치(상대적의미 위치)마다 원하는 문자를 넣어준다.
     *
     * @param basicStr 원본 문자열
     * @param inx 원하는 위치
     * @param oldStr 바꿀 문자열
     * @param newStr 바뀔 문자열
     * @return String 바뀐 문자열
     * @exception UtilException error occurs
     */
    public static String makeListStr(String basicStr, int inx, String oldStr, String newStr) throws Exception {

	    StringBuffer returnStr = new StringBuffer();

	    try {
	      for(int gg=0;gg<basicStr.length();gg=gg+inx){

	        returnStr.append(replace(getHTMLCode(basicStr.substring(gg, ((gg+inx) > basicStr.length()) ? basicStr.length() : gg+inx )), oldStr, newStr));
	        returnStr.append("<br>");
	      }
	    }
	    catch( Exception e ) {
	      throw new Exception("exception occurs in StringUtil.makeListStr : " + e.getMessage());
	    }
	    return returnStr.substring(0, returnStr.length()-4);
    }  
	  
    /**
     * String 내의 mark된 부분을 Vector로 넘어온 String으로 대치시킨다. default
     * mark로 "#"을 사용한다.
     *
     * @param str mark를 갖고 있는 String
     * @param replacedStr mark 부분을 대치할 문자를 담고 있는 Vector
     * @return 분리된 string array
     */
    public static String replaceString(String str, Vector replacedStr) {
    	return replaceString(str, replacedStr, "#");
    }
	  
    /**
     * String 내의 mark된 부분을 Vector로 넘어온 String으로 대치시킨다.
     *
     * @param str mark를 갖고 있는 String
     * @param replacedStr mark 부분을 대치할 문자를 담고 있는 Vector
     * @param markStr mark로 사용할 String
     * @return 분리된 string array
     */
    public static String replaceString(String str, Vector replacedStr, String markStr) {

	    // -1 is not necesarry as the sql statement will not start with flagStr
	    int    previous = 0;
	    int    current = 0;
	    int    length;

	    String endString = null;
	    String newStatement = new String(str);

	    for (int i = 0; i < replacedStr.size(); i++) {
	      current = newStatement.indexOf(markStr, previous);
	      String o = (String)replacedStr.elementAt(i);

	      if (o != null) {
	        length = newStatement.length();
	        endString = newStatement.substring(current + 1, length);
	        newStatement = newStatement.substring(0, current).concat(o);
	        newStatement = newStatement.concat(endString);
	      }
	      previous = current + 1;
	    }

	    return newStatement;
    }
	  
	  
    /**
     * 카드번호에 ' '를 붙인다.
     *
     * @param cardNum 원본 카드번호
     * @return 변환된 카드번호
     * @exception UtilException error occurs
     */
    public static String toCardFormat(String cardNum) throws Exception {
	    if( cardNum.length() != 16 )
	      throw new Exception("Length of card number must be 16.");

	    return toAnyFormat(cardNum, 4, ' ');
    }
	  
    /**
     * string 에 지정된 위치마다 원하는 문자를 넣는다.(  ex. toAnyFormat(money, 3, ','); ==> 200,000)
     *
     * @param src 변환하고자 하는 문자열
     * @param len appendChar가 들어갈 반복적 길이
     * @param appendChar 넣고자 하는 문자
     * @return 변화된 문자열
     * @exception UtilException error occurs
     */
    private static String toAnyFormat(String src, int len, char appendChar) throws Exception {

	    char[] arySrc = src.toCharArray();
	    int lenSrc = arySrc.length;
	    int lenTgt = lenSrc + ((lenSrc - 1) / len);
	    char[] aryTgt = new char[lenTgt];

	    String target = null;

	    for(int i=lenSrc-1, j=lenTgt-1, k=0; i >= 0; i--, j--, k++) {
	        if( k != 0 && (k % len) == 0 ) aryTgt[j--] = appendChar;
	        aryTgt[j] = arySrc[i];
	    }
	    target = new String(aryTgt);

	    return target;
    }

    /**
     * string array를 구분자로 구분하여 string으로 만든다.
     *
     * @param strArr string array
     * @param delim 구분자
     * @return 구분자에 의해 분리된 string
     * @exception UtilException error occurs
     */
    public static String arrayToString(String[] strArr, String delim) throws Exception {
	    String str = "" ;

	      for ( int i = 0 ; i < strArr.length ; i++ ) {
	        if ( i == 0 ) {
	          str += strArr[i] ;
	        }
	        else {
	          str += delim + strArr[i] ;
	        }
	       }


	    return str;
    }
	  

    /**
     * 숫자를 받아서 원하는 위치에서 잘라준다.(소수점 자리수 지정)
     *
     * @param String money 숫자
     * @param int inx 소수점 아래의 개수
     * @return String 원한는 소수점 아래의 개수가 맞춰진 String
     * @exception UtilException error occurs
   	 */
    public static String strCut(String money, int inx) throws Exception {
    	return roundOrCut(money, inx, 2);
    }

    /**
     * 숫자를 받아서 원하는 위치에서 잘라준다.(소수점 자리수 지정)및 ','를 찍어준다.
     *
     * @param String money 숫자
     * @param int inx 소수점 아래의 개수
     * @return String 원한는 소수점 아래의 개수가 맞춰진 String
     * @exception UtilException error occurs
     */
	public static String strCut2(String money, int inx) throws Exception {
	    String str = roundOrCut(money, inx, 2);
	    int ii = str.indexOf(".");
	    if(ii == -1)
	      return str;
	    else{
	      return FormatUtil.getComma(str);
	    }
	}
	  
	  /**
	   * 숫자를 받아서 반올림, 원하는 소수점 아래자리까지, 원하는 소수점 아래 및 원본데이타가 원하는 소수점 보다 길다면 원본에 맞게끔.
	   *
	   * @param String money 숫자
	   * @param int inx 반올림이나 잘르기를 원하는 소수점 아래 위치
	   * @param boolean 반올림 할것인지 아닌지 또는 원하는 소수점보다 큰것은 display할 것인지(1은 반올림을 한다. 2는 반올림을 안한다. 3은 원하는 소수점 아래크기보다 큰 금액은 display한다.)
	   * @return String 적용된 숫자 String
	   * @exception UtilException error occurs
	   */
	  public static String roundOrCut(String money, int inx, int round) throws Exception {
	    StringBuffer sb = new StringBuffer();
	    if(money == null && money.trim().equals(""))
	      money = "0";
	    money = replace(money, " ", "");
	    int dotinx = money.indexOf(".");
	    if(dotinx == -1){
	      if(inx > 1){
	        for(int xx=0;xx<inx;xx++){
	          sb.append("0");
	        }
	        return money + "." + sb.toString();
	      }else{
	        return money;
	      }
	    }

	    String major = money.substring(0, dotinx);
	    if(major.equals(""))
	      major = "0";
	    String minor = money.substring(dotinx+1, money.length());

	    if(inx > minor.length()){
	      for(int xx=minor.length();xx<inx;xx++){
	        sb.append("0");
	      }
	      return major + "." + minor + sb.toString();
	    }else{
	      if(round == 1){
	        if(Integer.parseInt(minor.substring(inx,inx+1)) > 4 ){
	          major = major + "." + minor;
	          sb.append("0.");
	          for(int xx=0;xx<inx-1;xx++){
	            sb.append("0");
	          }
	          sb.append("1");

	          money = ((new BigDecimal(major)).add(new BigDecimal(sb.toString()))).toString();
	          dotinx = money.indexOf(".");
	          if(inx == 0)
	            return money.substring(0,dotinx);
	          else
	            return money.substring(0,dotinx+1) + money.substring(dotinx+1, dotinx+inx+1);
	        }else{
	          return major +  ((inx == 0) ? "" : "." + minor.substring(0, inx) );
	        }
	      }else if(round == 2){
	        minor = minor.substring(0, inx);
	        return major +  ((inx == 0) ? "" : "." + minor );
	      }else if(round == 3){
	        return major + "." + minor;
	      }else{
	        return money;
	      }
	    }
	  }
	  
	  
	//SWIFT 허용 문자 검증
    public static String swiftCheck(String str) {
        String code = "";
        int index = 0;


        StringBuffer returnStr = new StringBuffer();

        for (int i = 0; i < str.length(); i++) {
            char cString = str.charAt(i);

            if ( (cString > 47 && cString < 58) || // 숫자   11
                (cString > 64 && cString < 91) || // 알파벳 11
                (cString > 96 && cString < 123) || // 알파벳 11
                (cString == 32) || // space 11
                (cString == 44) || // ','   11
                (cString == 45) || // '-'   11
                (cString == 46) || // '.'   11
                (cString == 47)) { // '/'   11

                returnStr.append(cString);

            } else {

                returnStr.append(" ");
            }

        }

			return returnStr.toString();
    }
	    
	    

		/**
	     * <PRE>
	     * Function   : 숫자타입으로 들어온 string에 대해 소수몇째 자리까지 끊어주는 것.
	     * Comment    :
	     * @param     strType  String
	     * @return    String
	     * </PRE>
	     */
    public static String belowDot(String s, int i)
    {
	        String s1 = s;
	        int j = i;
	        int k = 0;
	        boolean flag = false;
	        StringBuffer stringbuffer = new StringBuffer();
	        for(int i1 = 0; i1 < s1.length(); i1++)
	        {
	            if(!s1.substring(i1, i1 + 1).equals("."))
	                continue;
	            k = i1;
	            break;
	        }

	        if(k == 0)
	        {
	            stringbuffer.append(s1);
	            if (j > 0) {
	            	stringbuffer.append(".");
	            }
	            for(int j1 = 0; j1 < j; j1++)
	                stringbuffer.append("0");

	        } else
	        {
	          if (j == 0) {
	            stringbuffer.append(s1.substring(0, k));
	          } else {
	            int l = s1.length() - (k + 1);
	            if(l >= j)
	            {
	                s1 = s1.substring(0, k + 1 + j);
	                stringbuffer.append(s1);
	            } else
	            {
	                stringbuffer.append(s1);
	                for(int k1 = 0; k1 < j - l; k1++)
	                    stringbuffer.append("0");

	            }
	        }
	        }
	        return stringbuffer.toString();
    }

	    /**
	     * <PRE>
	     * Function   : 숫자타입으로 들어온 string에 대해 소수몇째 자리까지 끊어주는 것.
	     * Comment    :
	     * @param     strType  String
	     * @return    String
	     * </PRE>
	     */
	    public static String numRound(String s, int i)
	    {
	        String s1 = s;
	        int j = i;
	        int k = 0;
	        boolean flag = false;
	        StringBuffer stringbuffer = new StringBuffer();
	        for(int i1 = 0; i1 < s1.length(); i1++)
	        {
	            if(!s1.substring(i1, i1 + 1).equals("."))
	                continue;
	            k = i1;
	            break;
	        }

	        if(k == 0)
	        {
	            stringbuffer.append(s1);
	            if (j > 0) {
	            	stringbuffer.append(".");
	            }
	            for(int j1 = 0; j1 < j; j1++)
	                stringbuffer.append("0");

	        } else
	        {
	          if (j == 0) {
	            stringbuffer.append(s1.substring(0, k));
	          } else {
	            int l = s1.length() - (k + 1);
	            if(l >= j)
	            {
	                s1 = s1.substring(0, k + 1 + j);
	                stringbuffer.append(s1);
	            } else
	            {
	                stringbuffer.append(s1);
	                for(int k1 = 0; k1 < j - l; k1++)
	                    stringbuffer.append("0");

	            }
	        }
	        }
	        return stringbuffer.toString();
	    }
	    
		public static final int EMPTY = 0;
		public static final int NBSP = 1;
		
		public static String getString(int intValue) {
			return Integer.toString(intValue);
		}
		
		public static String getString(long longValue) {
			return Long.toString(longValue);
		}
		
		public static String getString(float floatValue) {
			return Float.toString(floatValue);
		}
		
		public static String getString(double doubleValue) {
			return Double.toString(doubleValue);
		}
		
		public static String getString(String str) {
			return getString(str, EMPTY);
		}
		
		public static String getString(String str, int mode) {
			if (mode == EMPTY) {
				return str == null ? "" : str;
			} else if (mode == NBSP) {
				return str == null ? "&nbsp;" : str;
			} else {
				return str == null ? "" : str;
			}
		}	
		

		  /**
		   * 금액에 합산을 구해준다.
		   *
		   * @param money1 금액
		   * @param money2 금액
		   * @return 합산된 금액
		   * @exception UtilException error occurs
		   */
		  public static String addMoney(String money1, String money2) throws Exception {
		    BigDecimal returnMoney = null;
		      money1 = money1.trim();
		      money2 = money2.trim();
		      returnMoney = (new BigDecimal((money1 == null || money1.equals("")) ? "0" : money1 )).add(new BigDecimal((money2 == null || money2.equals("")) ? "0" : money2 ));

		    return returnMoney.toString();
		  }


		  /**
		   * 금액에 곱셈을 구해준다.
		   *
		   * @param money1 금액
		   * @param money2 금액
		   * @return 합산된 금액
		   * @exception UtilException error occurs
		   */
		  public static String multiply(String money1, String money2) throws Exception {
		    BigDecimal returnMoney = null;

		      money1 = (money1 == null || money1.equals("")) ? "0" : money1.trim();
		      money2 = (money2 == null || money2.equals("")) ? "0" : money2.trim();
		      returnMoney = (new BigDecimal(money1)).multiply(new BigDecimal(money2));

		    return returnMoney.toString();
		  }

		  /**
		   * 금액에 나눗셈을 구해준다.
		   *
		   * @param money1 금액
		   * @param money2 금액
		   * @return 합산된 금액
		   * @exception UtilException error occurs
		   */
		  public static String divide(String money1, String money2) throws Exception {
		    BigDecimal returnMoney = null;

		      money1 = (money1 == null || money1.equals("")) ? "0" : money1.trim();
		      money2 = (money2 == null || money2.equals("")) ? "0" : money2.trim();
		      returnMoney = (new BigDecimal(money1)).divide(new BigDecimal(money2), BigDecimal.ROUND_HALF_UP);

		    return returnMoney.toString();
		  }
		  
		  /**
		   * 금액에 에 합산을 구해준다.(여러개의 금액 처리)
		   *
		   * @param moneys[] 금액 배열
		   * @return 합산된 금액
		   * @exception UtilException error occurs
		   */
		  public static String addMoneyArray(String[] moneys) throws Exception {
		    BigDecimal returnMoney = null;
		      returnMoney = new BigDecimal((moneys[0].trim() == null || (moneys[0].trim()).equals("")) ? "0" : moneys[0].trim());
		      for(int gg=1;gg<moneys.length;gg++){
		        returnMoney = (returnMoney).add(new BigDecimal((moneys[gg].trim() == null || (moneys[gg].trim()).equals("")) ? "0" : moneys[gg].trim()));
		      }

		    return returnMoney.toString();
		  }

		  /**
		   * 금액에 감산을 구해준다.
		   *
		   * @param money1 금액 (감산대상)
		   * @param money2 금액 (감산금액)
		   * @return 감산된 금액
		   * @exception UtilException error occurs
		   */
		  public static String subtractMoney(String money1, String money2) throws Exception {
		    BigDecimal returnMoney = null;
		      money1 = money1.trim();
		      money2 = money2.trim();
		      returnMoney = (new BigDecimal((money1 == null || money1.equals("")) ? "0" : money1)).subtract(new BigDecimal((money2 == null || money2.equals("")) ? "0" : money2));
		    return returnMoney.toString();
		  }
		  
		  
  /**
   * 소수점 아래 몇자리 이하 절삭.
   *
   * @param num 숫자
   * @param place 자리수
   * @return 절삭된 숫자
   */
    public static double getCutNumber(String num, String place) {
	   return Math.floor( NVL(num,0) * Math.pow(10,Integer.parseInt(place,10)) ) / Math.pow(10,Integer.parseInt(place,10)); 
	}

    public static boolean getBoolean(String value, boolean defaultValue) {
        if(value ==null ) return defaultValue;
        if(value.equalsIgnoreCase("true") || value.equalsIgnoreCase("on") || 
                value.equalsIgnoreCase("y") ||value.equalsIgnoreCase("t")||
                value.equalsIgnoreCase("yes"))
            return true;  
        return false;
    }
    
    public static int parseInt(String value,int defaultValue) {
        if(value ==null ) return defaultValue;
        return Integer.parseInt(value); 
    }
    
    public static long parseLong(String value,long defaultValue) {
        if(value ==null ) return defaultValue;
        return Long.parseLong(value); 
    }
    
    public static double parseDouble(String value,double defaultValue) {
        if(value ==null ) return defaultValue;
        return Double.parseDouble(value); 
    }
    
    /**
     * HTML 태그가 포함된 문자열을 태그를 적용하지 않고
     * 그대로 태그까지 웹화면에 보여주려 할때 사용하는 메소드입니다. 
     * 
     * @param str
     * @return
     */
    public static String getHtmlCodeView(String str)
    {
//    	StringBuffer resultStr = new StringBuffer();
//    	resultStr.append("<xmp>");
//    	resultStr.append(str);
//    	resultStr.append("</xmp>");
    	
		str = str.replaceAll("&", "&amp;");	//이걸 아래에 쓰면 잘못된다.
		str = str.replaceAll("<", "&lt;");
		str = str.replaceAll(">", "&gt;");
		str = str.replaceAll("\"", "&quot;");
		str = str.replaceAll("'", "&#39;");
		
    	return str;
    }
    /**
     * encrypt string.
     * SPIDER_HOME/config/security/nebsoa.key파일이 있어야 함.
     * 2006. 10. 30.  이종원 작성
     * @param str
     * @return
     * @throws Exception
     */
    public static String encrypt(String str) throws Exception{
        return Encrypter.encrypt(str);
    }
    /**
     * decrypt string.
     * SPIDER_HOME/config/security/nebsoa.key파일이 있어야 함.
     * 2006. 10. 30.  이종원 작성
     * @param str
     * @return
     * @throws Exception
     */
    public static String decrypt(String str) throws Exception{
        return Encrypter.decrypt(str);
   }
    
    /**
     * encrypt file.
     * SPIDER_HOME/config/security/nebsoa.key파일이 있어야 함.
     * 2006. 10. 30.  이종원 작성
     * @param 대상파일과 결과 파일경로
     * @return
     * @throws Exception
     */
    public void encryptFile(String srcFilePath,
            String targetFilePath) throws Exception{
        Encrypter.encryptFile(srcFilePath,targetFilePath);
    }
    /**
     * decrypt file.
     * SPIDER_HOME/config/security/nebsoa.key파일이 있어야 함.
     * 2006. 10. 30.  이종원 작성
     * @param 대상파일과 결과 파일경로
     * @return
     * @throws Exception
     */
    public void decryptFile(String srcFilePath,
            String targetFilePath) throws Exception{
        Encrypter.decryptFile(srcFilePath,targetFilePath);
    }
    
    /**
     * unicode로  encode
     * @return unicode로 변환된 문자열 
     */
    public String enUnicode(String str){
        return UnicodeEncoder.encode(str);
    }
    
    /**
     * unicode로  encoding 된 문자열을 원래대로 복원
     * @return 복원된 문자열 
     */
    public String deUnicode(String str){
        return UnicodeEncoder.decode(str);
    }
    
    /**
     * 객체를 string으로 변환
     * 2002. 11. 9.  이종원 작성
     * @param obj
     * @return
     * @throws Exception
     */
    public static String encodeObject(Object obj) throws Exception{
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(obj);
        oos.close();
        byte[] keyArr = bos.toByteArray();
        String encoded = Base64.getEncoder().encodeToString(keyArr);
        return encoded;
    }
    
    /**
     * string으로 변환 된 객체를 다시 복원
     * 2002. 11. 9.  이종원 작성
     * @param String
     * @return decodedObject
     * @throws Exception
     */
    public static Object decodeObject(String str) throws Exception{
        ObjectInputStream in = null;
        try{
            byte[] b = Base64.getDecoder().decode(str);
            ByteArrayInputStream bis = new ByteArrayInputStream(b);
            in = new ObjectInputStream(bis);
            Object obj = in.readObject();
            return obj;
        }finally{
            if(in != null){
                try{
                    in.close();
                }catch(Exception e){}
            }
        }
    }
    
    /**
     * XML스트링에서 비밀번호 관련태그를  마스크 처리하기 위해 사용
     * @param xmlContents XML 스트링
     * @param find 검색태그
     * @return maskedXml
     */
    public static String maskXml(String xmlContents, String find) {
        int startIndex;
        int endIndex;
        String maskedXml = xmlContents;
        if (StringUtil.contains(xmlContents.toLowerCase(), find)) {
            startIndex = xmlContents.toLowerCase().indexOf(find);
            if (startIndex >= 0) {
                endIndex = xmlContents.toLowerCase().indexOf("</", startIndex);
                if (endIndex < 0) {
                   endIndex = startIndex + find.length() + 10; 
                }
                maskedXml = xmlContents.substring(0, startIndex + find.length())+"****"+xmlContents.substring(endIndex);
            }
        }
        return maskedXml;
    }
    
    public static final String MASK_VALUE = "****";
    
    /**
     * 디버그 모드일경우 비밀번호관련 필드를 마스킹 처리한다.
     * @param key
     * @param value
     * @return
     */
    public static String maskedData(String key, String value) {
        boolean isMasked = false;
        
        if (isNull(key) || isNull(value) || !LogManager.isDebugEnabled()) {
            return value;
        }
        
        if (key.indexOf("비밀번호") != -1) {
            isMasked = true;
        } else if (key.toUpperCase().indexOf("PASSWORD") != -1) {
            isMasked = true;
        } else if (key.toUpperCase().indexOf("PWD") != -1) {
            isMasked = true;
        } else if (key.indexOf("이메일결재메시지") != -1) {
            isMasked = true;
        }
        
        return isMasked ? MASK_VALUE : value;
    }
    
    
    
	/**
	 * 배열의 값들을 'a', 'b', 'c' 형식의 문자열로 변경한다.
	 * (사용예, Sql 쿼리문의 In절 조건문의 문자열을 생성하는 경우)
	 * 
	 * "SELECT * " +
	 * "FROM DUAL " +
	 * "WHERE TEMP IN ("+ getInQueryString(List) +") ";
	 * 
	 * @param list 
	 * @return 문자열
	 */
    public static String getInQueryString(Object[] list)
    {
    	if(list == null || list.length == 0) {
    		//LogManager.debug("리스트 사이즈가 0 입니다");
    		return null;
    	}
    	
    	StringBuffer buf= new StringBuffer("'"+list[0]+"'");
    	for(int i=1 ; i<list.length ; i++)
    	{
    		buf.append(",'"+list[i]+"'");
    	}

		return buf.toString();
    }
    
    /**
	 * List 형식의 값들을 'a', 'b', 'c' 형식의 문자열로 변경한다.
	 * (사용예, Sql 쿼리문의 In절 조건문의 문자열을 생성하는 경우) 
	 * 
	 * "SELECT * " +
	 * "FROM DUAL " +
	 * "WHERE TEMP IN ("+ getInQueryString(List) +") ";
     * 
     * @param list
     * @return 문자열
     */
    public static String getInQueryString(List list)
    {
		return getInQueryString(list.toArray());
    }     
}
