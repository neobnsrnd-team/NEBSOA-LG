/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.util;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;

import nebsoa.common.exception.SysException;


/*******************************************************************
 * <pre>
 * 1.설명 
 * 문자열 Formatting Utility
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
 * $Log: FormatUtil.java,v $
 * Revision 1.1  2018/01/15 03:39:49  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:31  cvs
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
 * Revision 1.1  2008/08/04 08:54:50  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.3  2008/06/05 09:58:35  김은정
 * getFormattedMoney(String) 수정 - 소수점이 있는 값일 경우 formatting 소수점 값으로 refutn되게 수정
 * getFormattedIndexMoney(String), getFormattedIndexMoney(String, long) - 신규추가
 *
 * Revision 1.2  2008/06/05 01:23:58  오재훈
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:18  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:01  안경아
 * *** empty log message ***
 *
 * Revision 1.24  2006/11/03 01:19:49  오재훈
 * *** empty log message ***
 *
 * Revision 1.23  2006/11/03 00:53:23  오재훈
 * *** empty log message ***
 *
 * Revision 1.22  2006/11/02 13:07:15  오재훈
 * *** empty log message ***
 *
 * Revision 1.21  2006/11/02 12:21:51  오재훈
 * *** empty log message ***
 *
 * Revision 1.20  2006/11/02 03:40:04  오재훈
 * *** empty log message ***
 *
 * Revision 1.19  2006/10/30 12:53:40  오재훈
 * *** empty log message ***
 *
 * Revision 1.18  2006/10/27 06:29:09  오재훈
 * *** empty log message ***
 *
 * Revision 1.17  2006/10/24 11:27:02  오재훈
 * *** empty log message ***
 *
 * Revision 1.16  2006/10/20 12:11:00  이종원
 * 자리수가 큰 경우에 대한 doublePadding로직 수정
 *
 * Revision 1.15  2006/10/14 03:55:30  김성균
 * trim 버그 수정
 *
 * Revision 1.14  2006/09/27 03:47:10  오재훈
 * makeNumeric() 메소드에서 스트링에는 . 이 없고, 포맷에만 . 이 있을경우 추가.
 *
 * Revision 1.13  2006/09/14 07:35:40  오재훈
 * *** empty log message ***
 *
 * Revision 1.12  2006/09/14 07:11:23  오재훈
 * *** empty log message ***
 *
 * Revision 1.11  2006/09/14 07:08:08  오재훈
 * *** empty log message ***
 *
 * Revision 1.10  2006/09/04 09:11:31  안경아
 * *** empty log message ***
 *
 * Revision 1.9  2006/08/24 11:54:30  안경아
 * *** empty log message ***
 *
 * Revision 1.8  2006/08/01 04:12:01  오재훈
 * *** empty log message ***
 *
 * Revision 1.7  2006/06/17 10:37:45  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class FormatUtil {

    /**
    * 오늘 일자를 12자리(년월일시분:yyyyMMddHHmm)의 문자열로 돌려준다. <BR><BR>
    *
    * 사용예) getToday()<BR>
    * 결 과 ) 200209302210<BR><BR>
    *
    * @return String
    */
    public static String getToday()
    {
        return getToday( "yyyyMMdd" );
    }
    /**
    * 오늘 일자를 지정된 Format의 날짜 표현형식으로 돌려준다. <BR><BR>
    *
    * 사용예) getToday("yyyy/MM/dd hh:mm a")<BR>
    * 결 과 ) 2002/09/30 10:10 오후<BR><BR>
    *
    * Format은 J2SE의 SimpleDateFormat의 Documentation을 참고한다.
    *
    * @param pOutformat String
    * @return String
    */
    public static String getToday( String pOutformat )
    {	
    	//ksh 수정
        /*SimpleDateFormat pOutformatter =  new SimpleDateFormat ( pOutformat, java.util.Locale.KOREA );

        String rDateString = null;
        Date vDate = new Date();

        try
        {
            rDateString = pOutformatter.format( vDate );

        }
        catch( Exception e ) {}

        return rDateString;*/
        
        return DateFormatUtils.format(System.currentTimeMillis(), pOutformat, java.util.Locale.KOREA);
    }


	/**
	* 일자를 지정된 Format의 날짜 표현형식으로 돌려준다. <BR><BR>
	*
	* 사용예) getFormattedDate(new Date(),"yyyy/MM/dd hh:mm a")<BR>
	* 결 과 ) 2002/09/30 10:10 오후<BR><BR>
	*
	* Format은 J2SE의 SimpleDateFormat의 Documentation을 참고한다.
	*
	* @param pOutformat String
	* @return String
	*/
	public static String getFormattedDate( Date vDate,String pOutformat )
	{
		//ksh 수정
		return DateFormatUtils.format(vDate, pOutformat, java.util.Locale.KOREA);
		/*SimpleDateFormat pOutformatter =  new SimpleDateFormat ( pOutformat, java.util.Locale.KOREA );

		String rDateString = null;
		
		try
		{
			rDateString = pOutformatter.format( vDate );

		}
		catch( Exception e ) {}

		return rDateString;*/
	}
	
	/**
	* 일자를 지정된 Format의 날짜 표현형식으로 돌려준다. <BR><BR>
	*
	* 사용예) getFormattedDate(System.currentTimeMillis(),"yyyy/MM/dd hh:mm a")<BR>
	* 결 과 ) 2002/09/30 10:10 오후<BR><BR>
	*
	* Format은 J2SE의 SimpleDateFormat의 Documentation을 참고한다.
	*
	* @param pOutformat String
	* @return String
	*/
	public static String getFormattedDate( long time ,String pOutformat )
	{
		//ksh 수정
		return DateFormatUtils.format(time, pOutformat, java.util.Locale.KOREA);
		/*SimpleDateFormat pOutformatter =  new SimpleDateFormat ( pOutformat, java.util.Locale.KOREA );

		String rDateString = null;
		Date vDate = new Date(time);
		try
		{
			rDateString = pOutformatter.format( vDate );

		}
		catch( Exception e ) {}

		return rDateString;*/
	}
    /**
     * 입력된 Format의 문자열을 지정된 Format의 날짜 표현형식으로 돌려준다. <BR><BR>
     *
     * 사용예) getFormattedDate("200102042345","yyyyMMddhhmm","yyyy/MM/dd hh:mm a")<BR>
     * 결 과 ) 2002/09/30 11:45 오후<BR><BR>
     *
     * Format은 J2SE의 SimpleDateFormat의 Documentation을 참고한다.
     *
     * @param pIndate String
     * @param pInformat String
     * @param pOutformat String
     * @return String
     */
    public static String getFormattedDate( String pIndate, String pInformat, String pOutformat )
    {
            
    	if("00000000".equals(pIndate)) return "";
    	
    		//ksh 수정 ::DateFormatUtils이용
    	
    		SimpleDateFormat pInformatter =  new SimpleDateFormat ( pInformat, java.util.Locale.KOREA );
            //SimpleDateFormat pOutformatter =  new SimpleDateFormat ( pOutformat, java.util.Locale.KOREA );

            String rDateString = "";
            Date vDate = null;

            try
            {
                    vDate = pInformatter.parse( pIndate );
                    //rDateString = pOutformatter.format( vDate );
                    rDateString = DateFormatUtils.format(vDate, pOutformat, java.util.Locale.KOREA );
            }
            catch( Exception e )
            {
                    rDateString = pIndate;
            }

            return rDateString;
    }

    /**
    * 오늘 일자 기준으로 diff만큼 더한(뺀) 일를 지정된 Format의 날짜 표현형식으로 돌려준다. <BR><BR>
    *
    * 사용예) getEvalDate( "20021001", -1 )<BR>
    * 결 과 ) 2002/09/30<BR><BR>
    *
    * Format은 J2SE의 SimpleDateFormat의 Documentation을 참고한다.
    *
    * @param date String
    * @param diff int
    * @return String
    */
    public static String getEvalDate( String date, int diff )
    {
        //String evalDate = null;

        //SimpleDateFormat formatter = new SimpleDateFormat( "yyyyMMdd" );
        Calendar today = Calendar.getInstance();

        today.set( Integer.parseInt( date.substring( 0, 4 ) ), Integer.parseInt( date.substring( 4, 6 ) ) - 1, Integer.parseInt( date.substring( 6 ) ) );
        today.add( Calendar.DATE, diff );
        
        //ksh 수정
        //evalDate = formatter.format( today.getTime() );
        //return evalDate;
        return DateFormatUtils.format(today.getTime(), "yyyyMMdd");
    }

    
    /**
     * 전달받은 문자열을 금전형식으로 돌려준다.
     * null,"" 이면 "" 리턴
     * 작성일자 : 2008. 06. 05 
     * 작성자 : kfetus
     * 설명 : TODO 메소드 설명
     * @param pInstr
     * @return
     */
    public static String getDefaultFormattedMoney( String pInstr )
    {
        if(pInstr ==null || pInstr.equals("") || pInstr.equals("null")){
            return "";
        }   	
    		pInstr = parseSignedAmount(pInstr)+"";
            String rStr = getFormattedMoney( Long.parseLong( pInstr ) );

            return rStr;
    }

    /**
     * 전달받은 문자열을 금전형식으로 돌려준다.
     * 숫자가 아닌 값이 들어오면 입력값을 그대로 돌려준다.
	 * 소수점이 있는 문자열일 경우 소수점이 있는 금전형식으로 돌려준다. <BR><BR>
     * 사용예) getFormattedMoney("200102042345")<BR>
     * 결 과 ) 200,102,042,345<BR><BR>
     *
     * @param pInstr String
     * @return String
     */
	public static String getFormattedMoney( String pInstr )
	{
		if(pInstr ==null || pInstr.equals("") || pInstr.equals("null")){
			return null;
		}   	

		int index = pInstr.indexOf('.');
		String tempStr = null;
		if(index != -1){
			tempStr = pInstr.substring(index, pInstr.length());
			pInstr = pInstr.substring(0, index);
		}

		pInstr = parseSignedAmount(pInstr)+"";
		String rStr = getFormattedMoney( Long.parseLong( pInstr ) );

		if(index != -1){
			rStr = rStr + tempStr;
		}

		return rStr;
	}    
    
    /**
     * 문자열을 두번째 인자만큼 뒤에서 소수점으로 처리해서 실수형을 금전형식을 리턴
     * 맨 앞자리에 부호가 - 일 경우 -리턴. 양수면 삭제후 리턴 문자열에 소수점이 없을때만 사용
     * 사용예) 
     * getIndexFloatMoney("1234000,2) 결과 : 12,340
     * getIndexFloatMoney("+1234010,2) 결과 : 12,340.1
     * getIndexFloatMoney("-1234078,2) 결과 : -12,340.78
     * 
     * @param str : String
     * @param len : Stirng 맨 뒤 몇자리부터 소수점이다.
     * @return String
     */
    public static String getIndexFloatMoney(String str, int len){
    	String ret = new String();
    	str = str.trim();
    	if(str == null || str.length()==0) return null;
    	if(str.length() < len) return str;
    	
    	String high = str.substring(0,str.length()-len);
    	String low = str.substring(str.length()-len);
 
    	high = getFormattedMoney(high);
    	low = trimRZero(low);

    	if(low.length() != 0) 
    	{
    		ret = high+"."+low; 
    	}else {
    		ret = high;
    	}
    	return ret;
    }
    
	/**
	 * 문자열을 두번째 인자만큼 뒤에서 소수점으로 처리해서 실수형을 금전형식을 리턴
	 * 맨 앞자리에 부호가 - 일 경우 -리턴. 양수면 삭제후 리턴 문자열에 소수점이 없을때만 사용
	 * 사용예) 
	 * getIndexFloatMoney("1234000,2) 결과 : 12,340.00
	 * getIndexFloatMoney("+1234010,2) 결과 : 12,340.10
	 * getIndexFloatMoney("-1234078,2) 결과 : -12,340.78
	 * 
	 * @param str : String
	 * @param len : Stirng 맨 뒤 몇자리부터 소수점이다.
	 * @return String
	 */
	public static String getFormattedIndextMoney(String str){
		return getFormattedIndextMoney(str, 2);
	}

	public static String getFormattedIndextMoney(String str, int len){
		str = str.trim();
		if(str == null || str.length()==0) return null;
		if(str.length() < len) return str;

		int index = str.indexOf('.');
		if(index != -1){
			str = getFormattedMoney(str);
		}else{
			String high = str.substring(0,str.length()-len);
			String low = str.substring(str.length()-len);
			high = getFormattedMoney(high);

			str = high+"."+low; 
		}
		return str;
	}	    
    
    /**
     * 지정된 Format으로 전달 받은 문자열을 돌려준다. 입력받은 전달 받은 문자열이 숫자일때 해당된다.
     * 숫자가 아닌 값이 들어오면 입력값을 그대로 돌려준다.<BR><BR>
     *
     * 사용예) getFormattedNumber("200102042345","'$'####,####0")<BR>
     * 결 과 ) $20,01020,42345<BR><BR>
     *
     * Format은 J2SE의 MessageFormat의 Documentation을 참고한다.
     *
     * @param pInstr String
     * @param pInformat String
     * @return String
     */
    public static String getFormattedNumber( String pInstr, String pInformat )
    {
            String rStr = pInstr;

            try
            {
                    Object[] testArgs = {Long.valueOf(pInstr)};
                    MessageFormat form = new MessageFormat( "{0,number,"+pInformat+"}" );
                    rStr =  form.format( testArgs );
            } catch ( Exception e ) {}

            return rStr;
    }

    /**
     * 지정된 Format으로 전달 받은 문자열을 돌려준다. <BR><BR>
     *
     * 사용예) getFormattedText( "7104041055727",  "######-#######")<BR>
     * 결 과 ) 710404-1055727<BR><BR>
     * 사용예) getFormattedText( "7104041055727",  "******-*######")<BR>
     * 결 과 ) ******-*055727<BR><BR>
     *
     * 그대로 사용할 문자는 '#'으로 표현된다. 그 외의 문자열은 아무기호나 상관없다.
     * 그러므로 '#'자체는 사용이 불가능하다.
     * 입력받은 문자가 Format보다 길다면 Format길이 이후의 문자열은 잘리게 된다.
     * Format이 입력받은 문자열보다 길다면 문자열만큼만 출력된다.
     *
     * @param pInstr String
     * @param pInformat String
     * @return String
     */
    public static String getFormattedText( String pInstr, String pInformat )
    {
            StringBuffer rStr = new StringBuffer();

            try {
                    for ( int i = 0, j = 0 ; pInstr != null && !pInstr.equals("") && i < pInformat.length() ; i ++ ) {
                            if ( pInformat.charAt(i) == '#' ) {
                                    rStr.append( pInstr.charAt( j ) );
                                    j++;
                            } else if ( pInformat.charAt(i) == '*' ) {
                                    rStr.append( pInformat.charAt( i ) );
                                    j++;
                            }
                            else {
                                    rStr.append( pInformat.charAt( i ) );
                            }
                    }
            }
            catch ( Exception e ) {};

            return rStr.toString().trim();
    }

    /**
    * 전달받은 문자열을 금전형식으로 돌려준다.
    * 숫자가 아닌 값이 들어오면 입력값을 그대로 돌려준다.<BR><BR>
    *
    * 사용예) getFormattedMoney(200102042345)<BR>
    * 결 과 ) 200,102,042,345<BR><BR>
    *
    * @param pInstr long
    * @return String
    */
    public static String getFormattedMoney( long pInstr )
    {
        String rStr = "" + pInstr;

         try
         {
             Object[] testArgs = {Long.valueOf(rStr)};
             MessageFormat form = new MessageFormat( "{0,number,###,###,###,##0}" );
             rStr = form.format( testArgs );
         }
         catch ( Exception e ) {}

         return rStr;
        }

    /**
    * 전달받은 숫자를 지정된 형태로 출력한다.
    * 숫자가 아닌 값이 들어오면 입력값을 그대로 돌려준다.<BR><BR>
    *
    * 사용예) getFormattedNumber(1, "00000")<BR>
    * 결 과 ) "00001"<BR><BR>
    *
    * @param pInstr long
    * @return String
    */
    public static String getFormmatedNumber( long num, String format ) {
        StringBuffer formattedNum = new StringBuffer();
        String strNum = "" + num;

        try {
            for ( int i=0 ; i < format.length()-strNum.length(); i++ ) {
                formattedNum.append(format.charAt(i));
            }
            formattedNum.append(strNum);
        } catch ( Exception e ) {};

        return formattedNum.toString();
    }

    /**
     * 전달받은 문자열을 금전형식으로 돌려준다.
     * 숫자가 아닌 값이 들어오면 입력값을 그대로 돌려준다.<BR><BR>
     *
     * 사용예) getFormattedMoney(200102042345)<BR>
     * 결 과 ) 200,102,042,345<BR><BR>
     *
     * @param pInstr double
     * @return String
     */
    public static String getFormattedMoney(double pInstr)
    {
        String rStr = "" + pInstr;

        try
        {
            Object[] testArgs = {Double.valueOf(rStr)};
            MessageFormat form = new MessageFormat( "{0,number,###,###,###,##0}" );
            rStr = form.format( testArgs );
        }
        catch ( Exception e ) {}

        return rStr;
    }

    /**
     * 특정 스트링내의 일정한 pattern subString을 replace 문자열로
     *	대치한다.
     *
     * 사용예) replace("2002-02-10", "-", "/")<BR>
     * 결 과 ) "2002/02/10"<BR><BR>
     *
     * @return String
     */
    public static String replace(String str, String pattern, String replace) {
        int s = 0, e = 0;
        StringBuffer result = new StringBuffer();

        while ((e = str.indexOf(pattern, s)) >= 0) {
            result.append(str.substring(s, e));
            result.append(replace);
            s = e+pattern.length();
        }

        result.append(str.substring(s));
        return result.toString();
    }
    



        /**
        * 해당문자열을 ltrim, rtrim 한다. <BR><BR>
        *
        * 사용예) trim("  spaces  " )<BR>
        * 결 과 ) "spaces"<BR><BR>
        *
        * @param pInstr String
        * @return String trim된 문자열
        */
       /*
    public static String trim(String pInstr) {
        int st = 0;
        char[] val = pInstr.toCharArray();
        int count = val.length;
        int len = count;

        while ((st < len) && ((val[st] <= ' ') || (val[st] == '　') ) ){
            st++;
        }
        while ((st < len) && ((val[len - 1] <= ' ') || (val[len-1] == '　'))){
            len--;
        }

        return ((st > 0) || (len < count)) ? pInstr.substring(st, len) : pInstr ;

    }
*/
    /**
    * LT Type을 BIC Type을 변환한다. <BR><BR>
    *
    * 사용예) makeLt2Bic("KOEXKRS0X" )<BR>
    * 결 과 ) "KOEXKRS0"<BR><BR>
    *
    * @param pInstr String
    * @return String trim된 문자열
    */
    public static String makeLt2Bic(String input){
      String ret = input;
      if(input.length()>11) {
        ret = input.substring(0,8) + input.substring(9,12);
      }
      return ret;
    }

    /**
       * BIC Type을 LT Type을 변환한다. <BR><BR>
       *
       * 사용예) makeBic2Lt("KOEXKRS0X", "A" )<BR>
       * 결 과 ) "KOEXKRS0A"<BR><BR>
       *
       * @param pInstr String
       * @return String trim된 문자열
       */
       public static String makeBic2Lt(String input, String terminalCode){

         String ret = input;

         if( input==null )
             return "";

         if( input.length()==8 ){
             ret = input +terminalCode+"XXX";
         }else if(input.length()==11) {
             ret = input.substring(0,8) + terminalCode + input.substring(8);
         }else{
             ret = input.substring(0,8) + terminalCode + "XXX";
         }

         return ret;

       }

    /**
     * 지정된 문자열로 주어진 크기 만큼의 길이를 가진 문자열로
     * 초기화 된 문자열을 리턴합니다.<BR><BR>
     *
     * 사용예) makeTemplateString(5, 'A')<BR>
     * 결 과 ) "AAAAA"<BR><BR>
     *
     * @param size int 형태의 초기화 할 문자열의 길이
     * @param initialChar 초기화에 사용 할 문자
     * @return 지정된 문자열로 주어진 크기만큼 초기화 된 문자열
     */
    public static String makeTemplateString(int size, char initialChar) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < size; i++) {
            buffer.append(initialChar);
        }//end for
        return buffer.toString();
    }//end of makeTemplateString()

    /**
     * 주어진 문자열의 길이(getBytes() 결과 길이)가
     * 주어진 크기보다 작으면 padding char 를 부족한 길이만큼 왼쪽에 추가합니다.<BR>
     * 단, 주어진 문자열의 길이가 주어진 크기보다 클 경우,
     * 문자열의 맨 앞 부분부터 주어진 길이만큼만 잘려진 문자열이 리턴됩니다.<BR><BR>
     *
     * 사용예) lPadding("A", 3, '0')<BR>
     * 결 과 ) "00A"<BR><BR>
     * 사용예) lPadding("ABCD", 3, '0')<BR>
     * 결 과 ) "ABC"<BR><BR>
     *
     * @param src padding 하기 위한 문자열
     * @param size padding 하기 위한 길이
     * @param paddingChar padding 할 문자
     * @return padding 이 완료된 문자열
     */
	public static String lPadding(String src, int size, char paddingChar) {

		// 성능 개선을 위하여, 아래와 같은 공통 호출과 관련된 부분을,
		// 동일하게 복사하였음.

		int srcLength = 0;
		byte [] srcBytes;
		if (src == null) {
			StringBuffer result = new StringBuffer();
			for (int i = 0; i < size; i++) {
				result.append(paddingChar);
			}//end for
			return result.toString();
		} else {
            srcBytes = src.getBytes();
			srcLength = srcBytes.length;
		}//end if else
		if (size == srcLength) {
			return src;
		} else if (size < srcLength) {
            return new String(srcBytes, 0, size);
		}//end if else
		int paddingCount = size - srcLength;
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < paddingCount; i++) {
			result.append(paddingChar);
		}//end for
		result.append(src);
		return result.toString();
	}//end of lPadding()

    /**
     * 주어진 문자열의 길이(getBytes() 결과 길이)가
     * 주어진 크기보다 작으면 padding char 를 부족한 길이만큼 오른쪽에 추가합니다.<BR><BR>
     *
     * 사용예) lPadding("A", 3, '0')<BR>
     * 결 과 ) "A00"<BR><BR>
     * 사용예) lPadding("ABCD", 3, '0')<BR>
     * 결 과 ) "ABC"<BR><BR>
     *
     * @param src padding 하기 위한 문자열
     * @param size padding 하기 위한 길이
     * @param paddingChar padding 할 문자
     * @return padding 이 완료된 문자열
     */
	public static String rPadding(String src, int size, char paddingChar) {

		// 성능 개선을 위하여, 아래와 같은 공통 호출과 관련된 부분을,
		// 동일하게 복사하였음.

		int srcLength = 0;
		byte [] srcBytes;
		if (src == null) {
			StringBuffer result = new StringBuffer();
			for (int i = 0; i < size; i++) {
				result.append(paddingChar);
			}//end for
			return result.toString();
		} else {
            srcBytes = src.getBytes();
			srcLength = srcBytes.length;
		}//end if else
		if (size == srcLength) {
			return src;
		} else if (size < srcLength) {        
			return new String(srcBytes, 0, size);
		}//end if else
		int paddingCount = size - srcLength;
		StringBuffer result = new StringBuffer();
		result.append(src);
		for (int i = 0; i < paddingCount; i++) {
			result.append(paddingChar);
		}//end for
		return result.toString();
	}//end of rPadding()
	
	
	/**
	 * padding 처리
	 * @param src
	 * @return
	 */
	public static String padding(String src, int size, char paddingChar,String rightOrLeft){
		if("left".equals(rightOrLeft)){
			return lPadding(src, size, paddingChar);
		}else{
			return rPadding(src, size, paddingChar);
		}
	}
	
	/**
	 * space padding 처리
	 * @param src
	 * @return
	 */
	public static String padding(String src, int size, String rightOrLeft){
		char paddingChar=' ';
		if("left".equals(rightOrLeft)){
			return lPadding(src, size, paddingChar);
		}else{
			return rPadding(src, size, paddingChar);
		}
	}
	
	/**
	 * default로 left에 '0'을 padding 처리
	 * @param src
	 * @return
	 */
	public static String numberPadding(String src, int size){
		char paddingChar='0';
		return lPadding(src, size, paddingChar);		
	}
	
	/**
	 * default로 left에 '0'을 padding 처리
	 * @param src
	 * @return
	 */
	public static String longPadding(long num, int size){
		String src = Long.toString(num);
		char paddingChar='0';
		return lPadding(src, size, paddingChar);
		
	}
	
	/**
	 * default로 left에 '0'을 padding 처리. 
	 * <font color=red>소수점이 들어가지 않는다.</font>
	 * @param src
	 * @param size 전체 자리수 
	 * @param sosuSize 소수점 이하 자리수 
	 * @return
	 */
	public static String doublePadding(double num, int size,int sosuSize){
		return doublePadding(num,size,sosuSize,'0');
	}
	/**
	 * default로 left에 '0'을 padding 처리. 
	 * <font color=red>소수점이 들어가지 않는다.</font>
	 * @param src
	 * @param size 전체 자리수 
	 * @param sosuSize 소수점 이하 자리수 
	 * @return
	 */
	public static String doublePadding(double num, int size,int sosuSize,char paddingChar){
		String src = new BigDecimal(num).toString();
		String[] splited = StringUtil.splitDouble(src);
		if(splited[0].length() > size-sosuSize){
			throw new SysException("정수부에 해당하는 값보다 표현식의 자리수가 작으면 안됩니다.");
		}
		String longPart = lPadding(splited[0],size-sosuSize,paddingChar);
		String sosuPart = rPadding(splited[1],sosuSize,paddingChar);		
		return longPart+sosuPart;
	}
	/**
	 * default로 left에 '0'을 padding 처리. 
	 * <font color=red>소수점도 들어 간다.</font>
	 * @param src
	 * @param size 전체 자리수 
	 * @param sosuSize 소수점 이하 자리수 
	 * @return
	 */
	public static String doubleDotPadding(double num, int size,int sosuSize){
		return doubleDotPadding( num,  size, sosuSize, '0');
	}
	
	/**
	 * default로 left에 '0'을 padding 처리. 
	 * <font color=red>소수점도 들어 간다.</font>
	 * @param src
	 * @param size 전체 자리수 
	 * @param sosuSize 소수점 이하 자리수 
	 * @return
	 */
	public static String doubleDotPadding(double num, int size,int sosuSize,char paddingChar){
        String src = new BigDecimal(num).toString();
		String[] splited = StringUtil.splitDouble(src);
		if(splited[0].length() > size-sosuSize-1){
			throw new SysException("정수부에 해당하는 값보다 표현식의 자리수가 작으면 안됩니다.");
		}
		String longPart = lPadding(splited[0],size-sosuSize-1,paddingChar);
		String sosuPart = rPadding(splited[1],sosuSize,paddingChar);		
		return longPart+"."+sosuPart;
	}
	/**
	 * 주어진 문자열에 0x1F(US:UNIT SEPARATOR) 를 맨 뒤에 더해서 리턴합니다.
	 * 
	 * @param src 대상 문자열
	 * @return 맨 뒤에 0x1F 가 추가된 문자열
	 */
	public static String append0x1f(String src) {
	    return new String(append0x1f(src.getBytes()));
	}//end of append0x1f()
	
	/**
	 * 주어진 byte [] 에 0x1F(US:UNIT SEPARATOR) 를 맨 뒤에 더해서 리턴합니다.
	 * 
	 * 참고) String 클래스의 trim() 을 사용해도 control character 는 제거 가능하나,
	 * 실제 제거하려는 control character0x1F(US:UNIT SEPARATOR) 가 아닌 다른 대상이 제거 될 수 있으므로,
	 * 별도의 메소드를 제공함.
	 * 
	 * @param src 대상 byte []
	 * @return 맨 뒤에 0x1F 가 추가된 byte []
	 */
	public static byte [] append0x1f(byte [] src) {
	    byte [] result = new byte[src.length + 1];
	    
	    System.arraycopy(src, 0, result, 0, src.length);
	    result[src.length] = 0x1f;
	    
	    return result;
	}//end of append0x1f()
	
	/**
	 * 주어진 문자열의 맨 뒤에 있는 0x1F(US:UNIT SEPARATOR) 를 제거해서 리턴합니다.
	 * 
	 * @param src 대상 문자열
	 * @return 맨 뒤에 0x1F 가 제거된 문자열
	 */
	public static String removeTail0x1f(String src) {
	    return new String(removeTail0x1f(src.getBytes()));
	}//end of removeTail0x1f()
	
	/**
	 * 주어진 byte [] 의 맨 뒤에 있는 0x1F(US:UNIT SEPARATOR) 를 제거해서 리턴합니다.
	 * 
	 * @param src 대상 byte []
	 * @return 맨 뒤에 0x1F 가 제거된 byte []
	 */
	public static byte [] removeTail0x1f(byte [] src) {
	    if (src[src.length - 1] != 0x1f) {
	        return src;
	    }//end if
	    
	    byte [] result = new byte[src.length - 1];	    
	    System.arraycopy(src, 0, result, 0, src.length - 1);	    
	    return result;
	}//end of removeTail0x1f()

	/**
	 * 오른쪽만 trim합니다.
	 * @param str String 입력 문자열
	 * @return String 오른쪽만 trim한 리턴값
	 */
	public static String TRIM( String str ){
	    String tmpStr="";
	
	    if( str == null ) return "";
	
	    tmpStr = str.trim();
	
	    return tmpStr;
	}
	
	/**
	 * 년월일이나 년월일시분초의 날짜 문자열을 받아서 long형 시간으로 변환하여 리턴한다. 
	 * 
	 * @param dateArg 날짜 스트링 
	 * @param dateFormat 날짜 스트링의 포맷 형식 (예) yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static long getTimeInMillis(String dateArg, String dateFormat){
	    
	    Calendar cal = Calendar.getInstance();
	    String dateStr = getFormattedDate(dateArg, dateFormat, "yyyyMMddHHmmssSSS");
	    
	    int year 	= Integer.parseInt(dateStr.substring(0,4));
	    int month 	= Integer.parseInt(dateStr.substring(4,6))-1;
	    int date 	= Integer.parseInt(dateStr.substring(6,8));
	    int hour 	= Integer.parseInt(dateStr.substring(8,10));
        int minute 	= Integer.parseInt(dateStr.substring(10,12));
        int second 	= Integer.parseInt(dateStr.substring(12,14));
        int milliSecond 	= Integer.parseInt(dateStr.substring(14));
	    
	    cal.set(Calendar.YEAR, year);
	    cal.set(Calendar.MONTH, month);
	    cal.set(Calendar.DATE, date);
	    cal.set(Calendar.HOUR_OF_DAY, hour);
	    cal.set(Calendar.MINUTE, minute);
	    cal.set(Calendar.SECOND, second);
	    cal.set(Calendar.MILLISECOND, milliSecond);
	    
	    return cal.getTimeInMillis(); 
	    
	}
	
	/**
	 * 년월일이나 년월일시분초의 날짜 문자열을 받아서 long형 시간으로 변환하여 리턴한다. 
	 * 
	 * @param dateArg 날짜 스트링 
	 * @return
	 */
	public static long getTimeInMillis(String dateArg){
	    int len = dateArg.length();
	    if (len == 8) {
	        
	        return getTimeInMillis(dateArg, "yyyyMMdd");
	    } else if (len == 14){
	        return getTimeInMillis(dateArg, "yyyyMMddHHmmss");
	    } else {
	        throw new IllegalArgumentException("날짜 포맷은 'yyyyMMdd', 'yyyyMMddHHmmss' 두 가지만 가능합니다.");
	    }//end if else	    
	}//end of getTimeInMillis()
	
	
	/**
	 * 날짜를 나타내는 문자열을 인자로 받아서 java.sql.Date 객체로 변환하여 리턴한다.
	 * 
	 * @param dateArg : yyyyMMdd 또는 yyyyMMddHHmmss 포맷의 날짜를 나타내는 문자열 
	 * @return java.sql.Date 객체
	 */
	public static Timestamp getDate(String dateArg){
	    return new Timestamp(getTimeInMillis(dateArg));
	}

	public final static int SIZE_OVER = 100;
	public final static int TYPE_MISMATCH = 99;
	public final static int DATE_INCORRECT = 98;
	public final static int SPACE_INPUT = 97;
	public final static int OK = 0;
	public final static int FAIL = -1;


	/**
	 * 매 월의 일 수 
	 */
	private static int maxdd[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
    




	/**
	 * double형을 long형으로 바꾸어 준다.반올림한 값을 얻는다.
	 *
	 * @param d double
	 * @return long
	 */
	public static long dtol(double d){
		double dtmp = Math.rint(d);
		return (long)dtmp;
	} // End of dtol


	/**
	 * double형을 long형으로 바꾸어 준다.절사할까? 최충열
	 *
	 * @param d double
	 * @return long
	 */

	public static long dtoltrim(double d){
		double dtmp = Math.floor(d);
		return (long)dtmp;
	} // End of dtol
    

	public static long dtoltrim_round(double d){
		double dtmp = Math.round(d);
		return (long)dtmp;
	} // End of dtol


	/**
	 * 주어진 숫자만큼의 공백으로 채워진 문자열을 반환한다.
	 *
	 * @param count int
	 * @return String
	 * @see #getZero
	 */ 
	public static String getString(int count) {

		StringBuffer str = new StringBuffer("") ;
		for(int i=0 ; i<count ; i++) {
			str.append(" ") ;
		}

		return str.toString();
	} // End of String



	/**
	 * 주어진 문자열이 주어진 길이를 가진 오른쪽 정렬 형식이 되도록
	 * 문자열 앞에 공백문자을 삽입한다.
	 * CICS COMMAREA에서 오른쪽 정렬된 문자열 필드를 얻기위해 사용한다.
	 *
	 * @param str String 오른쪽 정렬할 문자열
	 * @param count int  원하는 문자열의 길이
	 * @return String    공백문자 삽입에 의해 정렬된 문자열
	 * @see #getZero
	 */ 
	public static  String getString(String str,int count) {
		if(str != null ){
			for(int i=0 ; i<count ; i++) {
				if(str.length() == count)break;
	//          if(str.length() < count)str=" "+str;
				if(str.length() < count)str=str+" ";
				if(str.length() > count)str=str.substring(0,count);
			}
		}
		else str = getString(count);

		return str;

	} // End of getString


	/**
	 * 주어진 정렬방향에 맞게 주어진 문자열 크기가 되도록 공백문자를 삽입한다.
	 * CICS COMMAREA에서 정렬된 문자열 필드를 얻기위해 사용한다.
	 *
	 * @param str String    정렬할 문자열
	 * @param count int     원하는 문자열의 크기
	 * @param align String  정렬 방향
	 *                      왼쪽은 "LEFT", 그외는 오른쪽 정렬
	 * @return String       정렬된 문자열
	 * @see #getZero
	 */ 
	public static String getString(String str,int count, String align) {
		if(str != null ){
			for(int i=0 ; i<count ; i++) {
				if(str.length() == count)break;
				if (align.equals("LEFT")) //왼쪽 
					if(str.length() < count)str=str+" ";
				else           //오른쪽 
					if(str.length() < count)str=" "+str;
				if(str.length() > count)str=str.substring(0,count);
			}
		}
		else str = getString(count);

		return str;

	} // End of getString


	/**
	 * 주어진 크기만큼 '0'으로 채워진 문자열을 반환한다.
	 *
	 * @param count int 문자열의 크기
	 * @return String   주어진 크기만큼 '0'으로 채워진 문자열
	 * @see #getString
	 */ 
	public static String getZero(int count) {

		StringBuffer str = new StringBuffer() ;
		for(int i=0 ; i<count ; i++) {
			str.append("0") ;
		}

		return str.toString();

	} // End of getZero



	/**
	 * 주어진 문자열을 오른쪽 정렬하기 위하여
	 * 주어진 문자열 크기가 되도록 문자열 앞에 '0'을 삽입한다.
	 * CICS COMMAREA에서 오른쪽 정렬된 숫자 필드를 얻기 위해 사용한다.
	 *
	 * @param str String
	 * @param count int
	 * @return String
	 * @see #getString
	 */ 
	public static String getZero(String str,int count) {
		if(str != null ){
			for(int i=0 ; i<count ; i++) {
				if(str.length() == count)break;
				if(str.length() < count)str="0"+str;
				if(str.length() > count)str=str.substring(0,count);
			}
		}
		else str = getZero(count);

		return str;
	} // End of getZero


	/**
	 * 주어진 문자열에서 주어진 문자열 패턴을 제거하여 준다.
	 *
	 * @param target String 소스 문자열
	 * @param del String    제거할 문자열 패턴
	 * @return String       문자열 패턴이 제거된 문자열
	 */
	public static String delString(String target,String del) {
		String retval = new String();
		int strlen ;
		int idx = 0;
		int i = 0;

		if(target == null)
			return target;

		try{
			strlen = target.length();
			if(strlen == 0 )
				return target;
			while( idx < strlen ){
				idx = target.indexOf(del, i);
				if(idx < 0){
					idx = strlen;
				}
				if(idx > strlen){
					idx = strlen;
				}
				retval += target.substring(i, idx);
				i = idx+del.length();
			}
		}catch(java.lang.StringIndexOutOfBoundsException e){
			e.printStackTrace();
		}catch(java.lang.Exception e){
			e.printStackTrace();
		}

		return retval;
	} // End of delString


	/**
	 * 금액 필드와 같이 문자열에 천단위 표시(,)가 있는 경우
	 * 금액을 숫자로 처리하기 위해서 문자열에서 쉼표(,)가 제거되어야 한다.
	 * 이런 경우에 금액 필드로 처리된 문자열을 매개변수로 하여 
	 * 쉼포(,)가 제거된 문자열을 반환한다.
	 *
	 * @param target String 천단위 표시가 포함된 문자열
	 * @return String       천단위 표시가 제거된 문자열
	 * @see #getComma
	 */ 
	public static String delString(String target) {

		return delString(target,","); 

	} // End of delString
    

	/**
	 * 주어진 연도가 윤년인지 여부를 판단한다.
	 *
	 * @param year int  년도
	 * @return boolean  윤년인지 아닌지 여부
	 */ 
	public static  boolean isLeapYear( int year) {
		if( (year % 4 == 0) && ( year % 100 != 0 ) || ( year % 400 == 0) ){
			return true;
		}else {
			return false;
		}
	} // End of isLeapYear

	
	/**
	 * 주어진 문자열의 뒷부분에 있는 '0'문자를 제거한다.
	 * 호스트로부터 전달받은 실수형 정수의 소수점 자리중 맨 하위값이 0일 경우 삭제하는데 사용한다.
	 *
	 * @param str String    뒤에 0이 채워진 실수형 문자열
	 * @return String       뒤에 채워진 '0'이 제거된 문자열
	 */ 
	public static String trimRZero(String str) {
		String retval = new String();

		try{
			retval = str.trim();
			if (retval == null || retval.equals("") || retval.length() == 0){
				return "";
			}
			
			if( retval.substring(retval.length()-1).equals("0")){
				for(int i= retval.length(); i > 0 ; i--){
					if(retval.substring(retval.length()-1).equals("0") ){
						retval = retval.substring(0,retval.length()-1);
					}else{
						break;
					}
				}
			}

		}catch(Exception e){
			System.err.println("error :trimRZero() in UtilFmt : "+e.toString());
		}
		return retval;  
	} 
	
	
	/**
	 * 주어진 문자열의 앞부분에 있는 '0'문자를 제거한다.
	 * 호스트로부터 전달받은 CICS COMMAREA의 숫자 필드인 경우에 앞부분에 채워진 '0'을 제거하기위해 사용될 수 있다.
	 *
	 * @param str String    앞에 0이 채워진 (오른쪽 정렬된) 문자열
	 * @return String       앞에 채워진 '0'이 제거된 문자열
	 */ 
	public static String trimZero(String str) {
		String retval = new String();

		try{
			retval = str.trim();
			if (retval == null || retval.equals("") || retval.length() == 0){
				return "";
			}
			if( retval.substring(0,1).equals("0")){
				int cnt = retval.length();
				for(int i= 0; i< cnt ; i++){
					if(retval.substring(0,1).equals("0") ){
						retval = retval.substring(1);
					}else{
						break;
					}
				}
			}

		}catch(Exception e){
			System.err.println("error :trimZero() in UtilFmt : "+e.toString());
		}

		return retval;  

	} // End of trimZero


	/**
	 * 주어진 문자열의 뒷부분 2자리를 소숫점 이하로 처리하기 위해 
	 * 문자열의 뒷부분 2자리 바로 앞에 소숫점(.)을 삽입한다.
	 *
	 * @param target String 정수와 소수가 구별되지 않은 문자열
	 * @return String       소숫점이 삽입된 문자열
	 */
	public static String makeRate(String target) {
		target = trimZero(target.trim());
		int len = target.length();
		if(len == 0 || target.equals("") || target == null){
			return "";
		}
		if(len <=2 ){
			return "0." +target;
		}else{
			return target.substring(0,len-2) + "." + target.substring(len-2);
		}

	} // End of makeRate

	// 소수점 구분없는 숫자로 된 이율에 대해....
	// 끝 float_length자리만큼을 소수점 이하로 처리하고 '.'를 삽입함...
	public static String makeRate(String target,int float_length) {
		target = trimZero(target.trim());
		int len = target.length();
		if(len == 0 || target.equals("") || target == null){
			return "";
		}
		if(len <= float_length ){
			return "0." +target;
		}else{
			return ( target.substring(0,len-float_length) + "."
					 + target.substring(len-float_length) );
		}             
	}

	/**
	 * 숫자 문자열을 삭제할 길이만큼 삭제 후 주어진 형식으로 형식화하여 반환한다.
	 * 주의 : 문자열에 소수점이 입력되면 안됨.
	 * @param target String 소스 문자열
	 * @param fmt String    문자열 형식
	 * @param cutLength int 문자열 뒤부터 삭제할 길이
	 * @return String       형식화된 문자열
	 */ 
	public static String subStrMakeFormat(String target, String fmt,int cutLength){
		
		if(target == null || target.length() == 0) 
			return "";
		
		if(cutLength < 0 ) 
			return target;
		
		String ret = "";
		if(target.length() > cutLength ) {
			ret = target.substring(0,target.length()-cutLength);
		} else {
			ret = target;
		}
		
		ret = makeFormat(ret,fmt);
		return ret;
	}
	
	/**
	 * 숫자 문자열을 주어진 형식으로 형식화하여 반환한다.
	 *
	 * @param target String 소스 문자열
	 * @param fmt String    문자열 형식
	 * @return String       형식화된 문자열
	 */ 
	public static String makeFormat(String target, String fmt) {
		String retval = new String();
		String sign = "";

		try{
			fmt = trimZero(fmt.trim());
			char first = fmt.charAt(0);

			if( first == 'd' || first == 'D') {
				if(target.length() != 6){
					throw new java.lang.Exception("Date Incorrect");
				}
				retval = makeDate(target);
			}else{
				switch(first){
					case 'S' :
					case 's' :
						 sign = "S";
					case '9' :
						if(!"0".equals(target))
							target = trimZero(target.trim());
						
						if (target == null || target.equals("") || target.length() == 0){
							return "0";
						}
//						if (target.length() > 0 || target.indexOf('-') > 0 ){
//							return "-1";
//						}
						retval = makeNumeric(target, fmt, sign);
						break;
					default : 
						retval = "";
				}
			}
		}catch(java.lang.Exception e){
			e.printStackTrace();
		//  log.printLog(e.toString());
		}

		return retval;

	} // End of makeFormat



	/**
	 * float형을 주어진 형식으로 형식화하여 문자열로 반환한다.
	 *
	 * @param target float  형식화할 float
	 * @param fmt String    문자열 형식
	 * @return String       형식화된 문자열
	 */ 
	public static String makeFormat(float target, String fmt){
		return makeFormat( String.valueOf(target), fmt);
	}


	/**
	 * float형을 "9.99"형식으로 형식화하여 문자열로 반환한다.
	 *
	 * @param target float  형식화할 float
	 * @return String       형식화된 문자열
	 */ 
	public static String makeFormat(float target){
		return makeFormat( String.valueOf(target), "9.99");
	}


	/**
	 * double형을 주어진 형식으로 형식화하여 문자열로 반환한다.
	 *
	 * @param target double 형식화할 double
	 * @param fmt String    문자열 형식
	 * @return String       형식화된 문자열
	 */ 
	public static String makeFormat(double target, String fmt){
		return makeFormat( Double.toString(target), fmt);
	}

	/**
	 * double형을 "9.99"형식으로 형식화하여 문자열로 반환한다.
	 *
	 * @param target double 형식화할 double
	 * @return String       형식화된 문자열
	 */ 
	public static  String makeFormat(double target){
		return makeFormat( String.valueOf(target), "9.99");
	}


	/**
	 * double형을 "9" 형식으로 형식화하여 문자열로 반환한다.
	 *
	 * @param target double 형식화할 double
	 * @return String       형식화된 문자열
	 */ 
	public static String makeFormat(long target){
		return makeFormat( String.valueOf(target), "9");
	}

	/**
	 * 숫자 문자열에 소숫점이 있으면 "9.99" 형식으로, 없으면 "9" 형식으로 형식화하여 반환한다.
	 *
	 * @param target String 소스 문자열
	 * @return String       형식화된 문자열
	 */ 
	public static String makeFormat(String target){
		if(target.indexOf('.') > -1 ){
			return makeFormat( target, "9.99");
		}else{
			return makeFormat( target, "9");
		}
	}

    
	/**
	 * 숫자 문자열을 주어진 형식으로 형식화하여 반환한다.
	 *
	 * @param target String 소스 문자열
	 * @param code int      문자열 형식
	 *                      "9" 형식일 때 1
	 *                      "9.99" 형식일 때 2
	 * @return String       형식화된 문자열
	 */ 
	public static String makeFormat(String target, int code){
		if (code == 1) {
			return makeFormat( target,"9") ;
		} else if( code == 2) {
			return makeFormat( target,"9.99");
		} else {
			return "unknown code";
		}
	}

    

	/**
	 * 주어진 구분자(Delimiter)가 삽입된 날짜형식의 문자열을 반환한다.
	 *
	 * @param date String   날짜 형식의 문자열
	 *                      구분자가 포함되었거나 포함되지 않았거나 상관없음.
	 * @param fmt String    구분자(Delimiter)
	 * @return String       주어진 구분자(Delimiter)가 삽입된 날짜 형식의 문자열
	 */
	public static String makeDate(String date, String fmt) {

		String yy = new String();
		String mm = new String();
		String dd = new String();
		String retval = date;
		if(retval.indexOf(fmt) == -1)
		{
			try{
				if(date.length() == 6){
					yy = date.substring(0,2);
					mm = date.substring(2,4);
					dd =  date.substring(4,6);
					retval = yy + fmt + mm + fmt + dd;
				}else if(date.length() == 8){
					yy = date.substring(0,4);
					mm = date.substring(4,6);
					dd =  date.substring(6,8);
					retval = yy + fmt + mm + fmt + dd;
				}
			}catch(java.lang.StringIndexOutOfBoundsException e){
				e.printStackTrace();
			}
		}   
		return retval;

	} // End of makeDate


	/**
	 * 슬래쉬 (/)가 구분자(Delimiter)로 삽입된 날짜형식의 문자열을 반환한다.
	 *
	 * @param date String   날짜 형식의 문자열
	 *                      구분자가 포함되었거나 포함되지 않았거나 상관없음.
	 * @return String       슬래쉬(/)가 삽입된 날짜 형식의 문자열
	 */
	public static String makeDate(String date){
		return makeDate(date,"/");
	}


	/**
	 * 주어진 문자열이 지정된 형식에 맞는 데이터인지 확인한다.
	 * CGI로 부터의 이주를 돕기 위한 메소드이다.
	 *
	 * @param src String 형식에 맞는지 확인할 문자열
	 * @param fmt String 데이터 형식
	 * @return int  0(OK) 이면 형식에 맞는 데이터,
	 *              그렇지 않으면 형식에 맞지 않는 데이터이다. 
	 *              오류 코드는 다음과 같다.
	 *              지정된 크기를 초과한 경우: SIZE_OVER
	 *              형식 불일치: TYPE_MISMATCH
	 *              날짜 형식 불일치: DATE_INCORRECT
	 *              주어진 형식을 알 수 없음: FAIL
	 * @deprecated  CGI와의 호환을 위해 지원
	 */
	public static int chkDataFormat(String src, String fmt) {
		char sign = ' ';
		int yy, mm, dd;
		try{    
		switch(fmt.charAt(0)){
			case 'x' : // 문자열
			case 'X' :
				if( src.length() > fmt.length() ){
				for( int i= 0; i< src.length() ; i++){
					if(src.charAt(i) < ' '){
						return TYPE_MISMATCH ;
					}
				}
				}
				break;
			case 's' : // 부호가 있는 숫자 문자열
			case 'S' :
				sign = 'S';
			case '9' : // 숫자 문자열
				if( src.length() > fmt.length() ){
					return SIZE_OVER ;
				}
				for( int i= 0; i< src.length() ; i++){
					switch(src.charAt(i) ){
						case '-' :
						case '+' :
							if( sign != 'S'){
								return TYPE_MISMATCH;
							}
							break;
						case ',' :
						case '.' :
							break;
						default :
							if( !Character.isDigit(src.charAt(i)) ){
								return TYPE_MISMATCH;
							}
							break;
					}
				}
				break;
			case 'd' : // 날짜
			case 'D' :
				if( src.length() !=6 ){
					return DATE_INCORRECT;
				}
				yy = Integer.valueOf(src.substring(0,2)).intValue();
				mm = Integer.valueOf(src.substring(2,4)).intValue();
				dd = Integer.valueOf(src.substring(4,6)).intValue();

				if(yy < 10 ){
					yy += 100;
				}               
				if( isLeapYear(1900 + yy) ) {
					maxdd[1] +=1;
				}
				if( yy < 0 || yy > 199 || mm <= 0 || mm > 12 ||
					dd <= 0 || dd > maxdd[mm-1] ){
						return DATE_INCORRECT;
				}
				break;
			default : // 기타
				return FAIL;
		}
		}catch(Exception e){
			e.printStackTrace();
			return FAIL;
		}
		return OK;
	} // End of chkDataFormat


	/**
	 * 필요시 부호 처리를 하고나서
	 * 주어진 숫자형식에 맞는 문자열로 소숫점 처리한 후에
	 * 천단위 표시(,)를 삽입한다.
	 *
	 * @param target String 대상 문자열
	 * @param fmt String    형식 문자열
	 * @param sign String   부호있는 숫자문자열인지 여부
	 * @return String       금액 필드 형식의 문자열
	 */
	private static String makeNumeric(String target, String fmt, String sign) {
	//Log log = new Log("/db2/nwww/src/test/nf.log");
		String fract = "";
		String retval = "";
		String tmp = "";

//log.printLog("target >> " + target);

  try {
		// COMMAREA 에서 음수가 들어오는 경우에만 ....활용됨...
		// 일반적으로 알파벳을 숫자로 바꾸는 경우에는 안됨..주의...
		// HOST에선 마지막 문자가 음수를 결정함...
		/*************************************************
		J => -1  } => -0   예) 12J => -121
		K => -2
		L => -3
		M => -4
		N => -5
		O => -6
		P => -7
		Q => -8
		R => -9
		*************************************************/
		int target_size = target.length();
		String last_char = target.substring(target_size - 1, target_size);
		if ( last_char.equals("J")) {
			target = "-" + target.substring(0,target_size -1)+"1";
		} else if ( last_char.equals("K")) {
			target = "-" + target.substring(0,target_size -1)+"2";
		} else if ( last_char.equals("L")) {
			target = "-" + target.substring(0,target_size -1)+"3";
		} else if ( last_char.equals("M")) {
			target = "-" + target.substring(0,target_size -1)+"4";
		} else if ( last_char.equals("N")) {
			target = "-" + target.substring(0,target_size -1)+"5";
		} else if ( last_char.equals("O")) {
			target = "-" + target.substring(0,target_size -1)+"6";
		} else if ( last_char.equals("P")) {
			target = "-" + target.substring(0,target_size -1)+"7";
		} else if ( last_char.equals("Q")) {
			target = "-" + target.substring(0,target_size -1)+"8";
		} else if ( last_char.equals("R")) {
			target = "-" + target.substring(0,target_size -1)+"9";
		} else if ( last_char.equals("}")) {
			target = "-" + target.substring(0,target_size -1)+"0";
		}       

		// 요기까지 새롭게 추가된 부분....

		// sign 출력 여부 결정
		float target_value = 0;
		try { 
		  target_value = Float.valueOf(target).floatValue(); 
		} catch(Exception float_error) {
		  target_value = 0;  
		} 
		if( target_value < 0 ){
				sign = "-";
				target = target.substring(1);
		}else if( sign == "S") {
				sign = "+";
		}else{
				sign = ""; // no sign
		}

		//소수점 이하 처리 결정
		int pidx = target.indexOf('.');
		int fpidx = fmt.indexOf('.');

		if( pidx >= 0 && fpidx >= 0) {
			if(pidx == 0){  
				tmp = "0";
			}else{
				tmp = target.substring(0, pidx);//소수점 이상
			}
			if(pidx+1 > target.length()){
				pidx = pidx-1;
			}
			if(fpidx+1 > fmt.length()){
				fpidx = fpidx-1;
			}
			fract = "." + makeFract(target.substring(pidx+1), fmt.substring(fpidx+1) );

//log.printLog("74 : fract " + fract);
		}else if( pidx >=0 && fpidx < 0){ //소수점 이하 절삭
			tmp = target.substring(0,pidx);

		}else if( pidx < 0 && fpidx >= 0){ //소수점 이하 절삭
//			int len = (fmt.length()-1) - fpidx;
			tmp = target;
			fract = "." + makeFract("", fmt.substring(fpidx+1) );
		}else{
			tmp = target;
		}

		retval = getComma(tmp);
//log.printLog("returned retval >> " + retval);
	}catch(Exception e){
		//log.printLog(e.toString());
		e.printStackTrace();
	}
		return sign + retval + fract;

	} // End of makeNumeric
            

	/**
	 * 주어진 문자열에 천단위 표시(,)를 삽입한다. 
	 *
	 * @param target String 숫자 문자열
	 * @return String       천단위 표시(,)가 삽입된 문자열
	 * @see #delString
	 */
	public static String getComma(String target){
	//  Log log = new Log("/db2/nwww/src/test/nf.log");
		if(target == null){
			return "";
		}
		StringBuffer tmp = new StringBuffer(target);
		int len = tmp.length();
		int cnt = len/3;

	//','가 필요없는 작은수
		if( len <=3){
			return target;
		}

		try{
			for(int i=1; i<= cnt ; i++){
				int offset = len -i*3;
				if( offset !=0){
					tmp.insert(offset,','); 
				}
			}   
		}catch(Exception e){
			//log.printLog(e.toString());
			e.printStackTrace();
		}

//	log.printLog("getComma() : retval >> " + tmp.toString());

		return  tmp.toString();

	} // End of getComma
    

	/**
	 * 주어진 문자열에서 주어진 형식 자릿수 만큼 소숫점이하 값을 얻는다.
	 *
	 * @param pnum String   숫자 문자열
	 * @param fnum String   자릿수 형식
	 * @return String       소숫점 이하에 해당하는 문자열
	 */
	private static String makeFract(String pnum, String fnum){
	//  Log log = new Log("/db2/nwww/src/test/nf.log");
		int plen= pnum.length();
		int flen = fnum.length();
		try{
			if(plen == flen) {
				return pnum;
			}else if(plen < flen){
				String retval = pnum;
				for(int i=plen ; i<flen ; i++){
					retval = retval +"0";
				}
				return retval;
			}else{
				return pnum.substring(0,flen);
			}       
		}catch(Exception e){
			e.printStackTrace();
			//log.printLog(e.toString());
		}
		return "";

	} // End of makeFract


	/**
	 * 주어진 서블릿 요청에서 원하는 이름의 매개변수를 찾아 그 값을 반환한다.
	 * 이 때, 해당 매개변수가 없거나 빈 문자열이면, 기본값을 반환한다.
	 *
	 * @param req javax.servlet.http.HttpServletRequest 서블릿 요청
	 * @param name String   매개변수의 이름 
	 * @param init String   매개변수의 기본값 
	 * @see javax.servlet.http.HttpServletRequest.getParameter
	 */
	public static String getParameter(javax.servlet.http.HttpServletRequest req,
								String name, String init)
	{
		String value = req.getParameter(name);

		if( value != null){
			if( value.equals(""))   
				return init;
			else
				return value;
		}else {
			return init;
		}

	} // End of getParameter



	/**
	 * 주어진 서블릿 요청에서 원하는 이름의 매개변수를 찾아 우측 정렬된(앞부분에 공백이 채워진) 값을 반환한다.
	 * 이 때, 해당 매개변수가 없거나 빈 문자열이면,
	 * 주어진 길이만큼 공백문자롤 채운 문자열을 반환한다.
	 *
	 * @param req javax.servlet.http.HttpServletRequest 서블릿 요청
	 * @param name String   매개변수의 이름 
	 * @param init String   매개변수의 기본값 
	 * @see javax.servlet.http.HttpServletRequest.getParameter
	 */
	public static String getParameter(javax.servlet.http.HttpServletRequest req, String name, int len) {

		String value = "";

		value = req.getParameter(name);
		if( value == null )
			value = getString(len);
		else if ( value.length() != len )
			value = getString(value, len);

		return value;
	} // End of getParameter


	/**
	 * 주어진 날짜 형식의 문자열에서 "yyyy-MM-dd" 형식의 문자열을 반환한다.
	 * 주어진 문자열이 날짜 형식이 아닌 경우 예외를 발생한다.
	 *
	 * @param strText String 날짜 형식의 문자열
	 * @return String        "yyyy-MM-dd" 형식의 문자열
	 * @exception IllegalArgumentException 주어진 문자열이 날짜 형식이 아닌 경우
	 */
	public static String parseYYYYMMDD (String strText)
					throws IllegalArgumentException
	{
		String strReturn = new String();

		try {

			SimpleDateFormat dateChecker = new SimpleDateFormat("yyyy-MM-dd");

			if ( strText.length() == 10 ) {

				if ( strText.charAt(4) == strText.charAt(7) ) {
					strReturn = strText.substring(0, 4)
						+ "-" + strText.substring(5, 7)
						+ "-" + strText.substring(8, 10);

				} else {
					throw new IllegalArgumentException();
				}

			} else if (strText.length() == 8) {

				strReturn = strText.substring(0, 4)
					+ "-" + strText.substring(4, 6)
					+ "-" + strText.substring(6, 8);

			} else if ( strText.length() == 0 ) {

				strReturn = "1111-11-11";

			} else {
				throw new IllegalArgumentException();
			} // End of if-else

			dateChecker.parse(strReturn, new ParsePosition (0) );

		} catch (java.lang.Exception ex) {

			throw new IllegalArgumentException(ex.toString());

		} // End of try-catch


		return strReturn;

	} // End of parseYYYYMMDD



	/**
	 * 주어진 계좌번호에 하이픈(-)이 있는 경우, 대쉬를 제거하고
	 * 숫자가 아닌 문자가 포함(하이픈 제외)된 경우 예외를 발생한다.
	 *
	 * @param strText String 계좌번호
	 * @return String        하이픈(-)이 제거된 계좌번호
	 * @exception IllegalArgumentException 숫자가 아닌 문자(하이픈 제외)가 포함된 경우
	 */
	public static String parseGjNo (String strText)
					throws IllegalArgumentException
	{
		String strReturn = new String();

		try {

			if ( strText.indexOf("-") >= 0 ) {
				strReturn = delString(strText, "-");
			} else {
				strReturn = strText;
			}

			for (int nDash = 0; nDash < strReturn.length(); nDash++) {

				if ( (strReturn.charAt(nDash) < '0')
					|| (strReturn.charAt(nDash) > '9') )
				{
					throw new IllegalArgumentException();
				} // End of if

			} // End of for

		} catch (java.lang.Exception ex) {

			throw new IllegalArgumentException(ex.toString());

		} // End of try-catch

		return strReturn;

	} // End of parseGjNo

	/**
	 * 주어진 문자배열의 전/반각 여부에 관계없이 양 끝의 공백문자를 제거한다.<br>
	 * String 클래스의 trim 메소드는 전각 공백문자를 trim하지 못한다.<br>
	 * '　'(전각문자 스페이스)와 - String 클래스의 trim 메소드에서처럼 -
	 * ' '(스페이스)보다 작거나 같은 문자인 경우 공백문자로 간주하여 trim한다.
	 *
	 * @param szTxt String      소스 문자배열
	 * @return java.lang.String trim한 문자열
	 */
	public static String trim(String strTxt) {
        if(strTxt==null) return "";
		char [] szTxt = strTxt.toCharArray();
		int len = szTxt.length;
		int st = 0;
		while ( (st < len) && 
				((szTxt[st] <= ' ') || (szTxt[st] == '　')) ) {
			st++;
		}
		while ( (st < len) && 
				((szTxt[len - 1] == '　') || (szTxt[len - 1] <= ' ')) ) {
			len--;
		}
        
		if ((st > 0) || (len < szTxt.length)) {
			if (st == len)
				return new String("");
			else            
				return new String(szTxt, st, (len-st));
		} else {
			return new String(szTxt);
		}
	} // End of trim


	/**
	 * 주어진 문자배열의 전/반각 스페이스를 제거한다.<br>
	 * 즉, 문자열에서 모든 전각/반간 스페이스가 제거된
	 * 새로운 문자열을 String을 반환한다..
	 *
	 * @param szTxt String      소스 문자배열
	 * @return java.lang.String trim한 문자열
	 */
	public static String deleteAllSpace(String strTxt) {
		if (strTxt == null) {
			strTxt = "";
		}  
		int origin_length = strTxt.length();
        
		if ( origin_length == 0 ) {
			return new String("");
		} else {
			char source[] = strTxt.toCharArray();
			int  source_length = source.length;
			StringBuffer target = new StringBuffer();
			for (int i=0; i< source_length;i++) {
				if ((source[i] != ' ') &&
					(source[i] != ' ')) {
					target = target.append(source,i,1);
				}
			}
			String newString = target.toString();
			if (newString == null ) {
			   newString = "";
			}          
   
			return newString;
		}
	}

	/**
	 * 주어진 서블릿 요청에서 원하는 이름의 매개변수를 찾아 그 값을 반환한다.
	 * 이 때, 해당 매개변수가 없거나 빈 문자열이면, 기본값을 반환한다.
	 *
	 * @param name String   매개변수의 이름 
	 * @param init String   매개변수의 기본값 
	 */
	public static String chkNull(String strVal, String init)
	{

		if( strVal != null){
			if( strVal.equals(""))  
				return init;
			else
				return strVal;
		}else {
			return init;
		}

	} // End of chkNull  최충열 만듬...2001년 12월 26일

	
	/**
	 * 테스트 코드를 포함하는 main() 메소드
	 * @param args 실행에 필요한 인자 리스트
	 * @throws Exception main() 메소드 수행 시 발생하는 Exception
	 */
	public static void main(String [] args) throws Exception {
		System.out.println(getToday("HHmmss"));
		System.out.println(getToday("yyyyMMddHHmmss"));
		System.out.println("["+doublePadding(123.45678,16,2)+"]"); //[012345]
		System.out.println("["+doubleDotPadding(123.45678,16,2)+"]"); //[123.45]
		
		System.out.println("["+doubleDotPadding(123.45678,10,2,' ')+"]"); //[    123.45]
		
		System.out.println(new Timestamp(getTimeInMillis("20050420181010", "yyyyMMddHHmmss")));
		System.out.println("["+trim(new String("   123   456  "))+"]");
        System.out.println("["+trim(null)+"]");
//	    String today = FormatUtil.getToday();
//	    System.out.println("######### " + getDate(today));
//	    
//	    System.out.println(new Timestamp(getTimeInMillis("20041010", "yyyyMMdd")));
//	    
//	    
//	    
//	    System.out.println(getTimeInMillis("2004-01", "yyyy-MM")); 
//	    
//	    String testString = "AAA";
//	    String sString = FormatUtil.append0x1f(testString);
//	    System.out.println("source String : " + StringUtil.printFormattedToHexString(testString) + " -> appendedString : " + StringUtil.printFormattedToHexString(sString));
//	    System.out.println("appendedString String : " + StringUtil.printFormattedToHexString(sString) + " -> remove appendedString : " + StringUtil.printFormattedToHexString(sString.trim()));
//	    System.out.println("appendedString String : " + StringUtil.printFormattedToHexString(sString) + " -> remove appendedString : " + StringUtil.printFormattedToHexString(FormatUtil.removeTail0x1f(sString)));
//	    
//	    System.out.println("FormatUtil.lPadding(testString, 10, '0') : [" + FormatUtil.lPadding(testString, 10, '0'));
//	    System.out.println("FormatUtil.rPadding(testString, 10, '0') : [" + FormatUtil.rPadding(testString, 10, '0'));
//        
//        testString = "중복된 거래고유번호입니다.|BIC코드:NACFKRS0XXX|거래고유번호:FS04ZZ090702";
//        System.out.println(FormatUtil.rPadding(testString, 60, ' '));
	
        System.out.println(doublePadding(123456789012345.012,17,2));
        System.out.println(doubleDotPadding(12345678901234.012,17,2));
    }//end of main()
	/**
	 * +- 기호가 있는 금액을 parsing한다 
	 * @param amount 금액
	 * @return
	 */
	public static long parseSignedAmount(String amount) {
		char sign = amount.charAt(0);
		if (sign == '-' || sign == '+') {
			String value = amount.substring(1);
			long retValue = Long.parseLong(value);
			if (sign == '-')
				retValue = - retValue;
			
			return retValue;
		}
		return Long.parseLong(amount);
	}
}
