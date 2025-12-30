/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.TimeZone;

import org.apache.commons.lang.time.DateFormatUtils;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 일자 처리를 위한 유요한 메소드 모음
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
 * $Log: DateUtil.java,v $
 * Revision 1.1  2018/01/15 03:39:49  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:32  cvs
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
 * Revision 1.1  2008/01/22 05:58:18  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:01  안경아
 * *** empty log message ***
 *
 * Revision 1.5  2006/09/11 00:22:03  안경아
 * *** empty log message ***
 *
 * Revision 1.4  2006/09/05 08:35:35  안경아
 * *** empty log message ***
 *
 * Revision 1.3  2006/06/17 10:37:45  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class DateUtil
{
	private static String MSG_ALERT="EXPIRED..";
	private static String EX_GMT_TO_DATE = "gmtToDate: invalid gmt date string!";
	private static String EX_SQL_COMP1 = "getSQLComp (1 date) : illegal operator value! Allowed values are =,>,<,<=,>=,<>";
	private static String EX_SQL_COMP2 = "getSQLComp (2 dates) : illegal operator value! Allowed values are ><,>=<,><=,>=<=";
	private static String EX_GET_MONTH_NAME = "getMonthName : month value must be between 1 and 12!";
	private static String EX_GET_ELAPSED_TIME = "getElapsedTime : begin date must be inferior to end date !";
	private static String EX_GET_DAY_NAME = "getDayName: day value must be between 1 and 7!";
	private static String EX_FORMAT_MONTH = "Invalid month name";
	private static String EX_DATE_TO_GMT = "dateToGmt: unknown RFC format";

    public DateUtil()
    {
        
        
    }

    public static Date addDate(Date date, Date date1)
    {
        if(evaluationOver())
        {
            return new Date(0L);
        }
        else
        {
            long l = date.getTime();
            long l1 = date1.getTime();
            long l2 = l + l1;
            return new Date(l2);
        }
    }

    public static Date addDay(Date date, long l)
    {
        if(evaluationOver())
        {
            return new Date(0L);
        }
        else
        {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(5, (int)l);
            Date date1 = calendar.getTime();
            return date1;
        }
    }
	/**
	 * 원하는 날짜를 더한 Date String을 리턴한다.
	 *
	 * @param basicDate String(YYYYMMDD  or  YYYY/MM/DD)
	 * @param addDay 더할 일자
	 * @return 더해진 String(YYYYMMDD)
	 */
	public static String addDay(String basicDate, int addDay) throws Exception{
    basicDate = checkDateType(basicDate);
    if(basicDate.equals(""))
      return "";
    Calendar cld = Calendar.getInstance();
    cld.set(Integer.parseInt(basicDate.substring(0,4)),
            Integer.parseInt(basicDate.substring(4,6))-1,
            Integer.parseInt(basicDate.substring(6,8)));

    cld.add(Calendar.DATE, addDay);

    basicDate = Integer.toString(cld.get(Calendar.YEAR));
    basicDate = basicDate + ((Integer.toString(cld.get(Calendar.MONTH)+1)).length() == 1 ? "0" + Integer.toString(cld.get(Calendar.MONTH)+1) : Integer.toString(cld.get(Calendar.MONTH)+1));
    basicDate = basicDate + ((Integer.toString(cld.get(Calendar.DAY_OF_MONTH))).length() == 1 ? "0" + Integer.toString(cld.get(Calendar.DAY_OF_MONTH)) : Integer.toString(cld.get(Calendar.DAY_OF_MONTH)));

	  return  basicDate;
  }
	
	  /**
	   * string이 날짜 형식이 맞는지 검사한다.
		 * @param String (YYYYMMDD, YYYY/MM/DD)
		 * @return String (YYYYMMDD)
		 */
	  public static String checkDateType(String dateStr) throws Exception{
	    dateStr = StringUtil.replace(dateStr, "/", "");
	    if( dateStr.length() == 0 || dateStr.length() != 8 )
	      return "";
	    for(int jj=0;jj<dateStr.length();jj++){
	      if(dateStr.charAt(jj) < '0' || dateStr.charAt(jj) > '9')
	        return "";
	    }

	    int year  = Integer.parseInt(dateStr.substring(0, 4));
	    int month = Integer.parseInt(dateStr.substring(4, 6));
	    int day   = Integer.parseInt(dateStr.substring(6, 8));

	    if ( year < 1970)
	      return "";
	    if ( month > 12 || month < 1 )
	      return "";
	    if ( day > getEndDayOfMonth(year, month) || day < 1 )
	      return "";

	    return dateStr;
	  }	
	  
	  /**
	   * 해당달의 마지막 날자를 얻는다.
	   * @param month 마지막 날짜를 얻고자 하는 달. 1, 2, 3 ... , 12
	   * @return 해당달의 마지막 날짜
	   */

	  public static int getEndDayOfMonth(int year, int month){
	    int[] monarr = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
	    if(((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0))
	    monarr[1] = 29;
	    return monarr[month-1];
	  }
	  
	  /**
		 * 다음날의 Date object를 리턴한다.
		 *
		 * @param basicDate 기본 Date object
		 * @return 더해진 Date object
		 */
	  public static Date nextAddDay(Date basicDate) throws Exception{
	    return addDay(basicDate, 1);
	  }

		/**
		 * 다음날의 Date String을 리턴한다.
		 *
		 * @param basicDate String(YYYYMMDD  or  YYYY/MM/DD)
		 * @return 더해진 String(YYYYMMDD)
		 */
	  public static String nextAddDay(String basicDate) throws Exception{
	    return addDay(basicDate, 1);
	  }
	  
	  /**
	   * 두 날짜의 날수를 계산해 준다.
	   *
	   * @param from Date Object
	   * @param to  Date Object
	   * @return from day와 to day의 차이를 리턴한다.(int)
	   */
	    public static int numberOfDays(Date fromDay, Date toDay){
	      long lFrom= fromDay.getTime();
	      long lTo = toDay.getTime();

	      return (int) ((lTo - lFrom)/ 1000 / 60 / 60 / 24);
	    }
	    
	    /**
	     * 두 날짜의 일수를 계산해 준다.
	     *
	     * @param from Date String
	     * @param to  Date String
	     * @return from day와 to day의 차이를 리턴한다.(int)
	     */
	      public static int numberOfDays(String fromDay, String toDay){
	        Calendar cldFrom = Calendar.getInstance();
	        Calendar cldTo   = Calendar.getInstance();
	        cldFrom.set(Integer.parseInt(fromDay.substring(0,4)),Integer.parseInt(fromDay.substring(4,6))-1,Integer.parseInt(fromDay.substring(6,8)));
	        cldTo.set(Integer.parseInt(toDay.substring(0,4)),Integer.parseInt(toDay.substring(4,6))-1,Integer.parseInt(toDay.substring(6,8)));
	        long lFrom= cldFrom.getTime().getTime();
	        long lTo = cldTo.getTime().getTime();

	        return (int) ((lTo - lFrom)/ 1000 / 60 / 60 / 24);
	      }
	      
	      /**
	  	 * 원하는 달의 마지막 날을 리턴한다.
	  	 *
	  	 * @param requestMonth 기본 원하는 달(int)
	  	 * @return 마지막 날짜(int)
	  	 */
	  	public static int getLastDayOfMonth(int requestMonth)	{
	  		/*
	  		 * 지금 현재의 Date 객체를 생성한다.
	  		 */
	  		Calendar currentDate = Calendar.getInstance();

	  		int year = currentDate.get(Calendar.YEAR)+1900;
	      int date = currentDate.get(Calendar.DAY_OF_MONTH);

	      /*
	       * 다음달 1일의 Date 객체를 생성한다.
	       */
	      Calendar nextMonthFirstDay = Calendar.getInstance();
	      nextMonthFirstDay.set(year, requestMonth, 1);


	  		/*
	       * nextMonthFirstDay.getTime()하면 1970년 1월 1일 부터 다음달의 1일까지를
	       * milliseconds로 계산한 값이 나옵니다.
	       * 거기서 86400000(하루의 milliseconds값)을 빼준다면
	       * 이달의 1970년 1월 1일부터 이달의 마지막 날까지의
	       * milliseconds 값이 나오겠죠?
	       */
	      long l = nextMonthFirstDay.getTime().getTime() - 86400000;


	      /*
	       * 이번달으 마지막 날로 새로운 Date를 생성합니다.
	       */
	      Calendar currentMonthLastDay = Calendar.getInstance();
	      currentMonthLastDay.setTime( new Date(l) );


	      /*
	       * 새로 생성된 객체의 날짜를 받으면 이번달의 마지막날
	       */
	  		return currentMonthLastDay.get(Calendar.DAY_OF_MONTH);
	  	}
    public static Date addDuration(Date date, int i, int j)
    {
        if(evaluationOver())
        {
            return new Date(0L);
        }
        else
        {
            Date date1 = date;
            date1 = addMonth(date1, j);
            date1 = addYear(date1, i);
            return date1;
        }
    }

    public static Date addDuration(Date date, int i, int j, int k)
    {
        if(evaluationOver())
        {
            return new Date(0L);
        }
        else
        {
            Date date1 = date;
            date1 = addDay(date1, k);
            date1 = addMonth(date1, j);
            date1 = addYear(date1, i);
            return date1;
        }
    }

    public static Date addDuration(Date date, int i, int j, int k, int l)
    {
        if(evaluationOver())
        {
            return new Date(0L);
        }
        else
        {
            Date date1 = date;
            date1 = addHour(date1, l);
            date1 = addDay(date1, k);
            date1 = addMonth(date1, j);
            date1 = addYear(date1, i);
            return date1;
        }
    }

    public static Date addDuration(Date date, int i, int j, int k, int l, int i1)
    {
        if(evaluationOver())
        {
            return new Date(0L);
        }
        else
        {
            Date date1 = date;
            date1 = addMinute(date1, i1);
            date1 = addHour(date1, l);
            date1 = addDay(date1, k);
            date1 = addMonth(date1, j);
            date1 = addYear(date1, i);
            return date1;
        }
    }

    public static Date addDuration(Date date, int i, int j, int k, int l, int i1, int j1)
    {
        if(evaluationOver())
        {
            return new Date(0L);
        }
        else
        {
            Date date1 = date;
            date1 = addSecond(date1, j1);
            date1 = addMinute(date1, i1);
            date1 = addHour(date1, l);
            date1 = addDay(date1, k);
            date1 = addMonth(date1, j);
            date1 = addYear(date1, i);
            return date1;
        }
    }

    public static Date addHour(Date date, long l)
    {
        if(evaluationOver())
        {
            return new Date(0L);
        }
        else
        {
            long l1 = date.getTime();
            long l2 = l * 60L * 60L * 1000L;
            long l3 = l1 + l2;
            Date date1 = new Date(l3);
            return date1;
        }
    }

    public static Date addMinute(Date date, long l)
    {
        if(evaluationOver())
        {
            return new Date(0L);
        }
        else
        {
            long l1 = date.getTime();
            long l2 = l * 60L * 1000L;
            long l3 = l1 + l2;
            Date date1 = new Date(l3);
            return date1;
        }
    }

    public static Date addMonth(Date date, int i)
    {
        if(evaluationOver())
            return new Date(0L);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int j = calendar.get(2);
        j += i;
        int k = j / 12;
        j %= 12;
        calendar.set(2, j);
        if(k != 0)
        {
            int l = calendar.get(1);
            calendar.set(1, k + l);
        }
        return calendar.getTime();
    }

    public static Date addSecond(Date date, long l)
    {
        if(evaluationOver())
        {
            return new Date(0L);
        }
        else
        {
            long l1 = date.getTime();
            long l2 = l * 1000L;
            long l3 = l1 + l2;
            Date date1 = new Date(l3);
            return date1;
        }
    }

    public static Date addYear(Date date, int i)
    {
        if(evaluationOver())
        {
            return new Date(0L);
        }
        else
        {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int j = calendar.get(1);
            calendar.set(1, i + j);
            return calendar.getTime();
        }
    }

    public static long dateDiff(String s, Date date, Date date1)
    {
        if(evaluationOver())
            return 0L;
        byte byte1 = 0;
        int i = 1;
        Date date2;
        Date date3;
        if(date.getTime() > date1.getTime())
        {
            i = -1;
            date2 = date1;
            date3 = date;
        }
        else
        {
            date2 = date;
            date3 = date1;
        }
        byte byte0;
        if(s.equals("yyyy"))
            byte0 = 1;
        else
        if(s.equals("m"))
            byte0 = 2;
        else
        if(s.equals("d"))
            byte0 = 5;
        else
        if(s.equals("y"))
            byte0 = 5;
        else
        if(s.equals("w"))
            byte0 = 4;
        else
        if(s.equals("ww"))
            byte0 = 3;
        else
        if(s.equals("h"))
        {
            byte0 = 5;
            byte1 = 11;
        }
        else
        if(s.equals("n"))
        {
            byte0 = 5;
            byte1 = 12;
        }
        else
        if(s.equals("s"))
        {
            byte0 = 5;
            byte1 = 13;
        }
        else
        {
            return -1L;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date2);
        long l = 0L;
        calendar.add(byte0, 1);
        for(Date date4 = calendar.getTime(); date4.getTime() <= date3.getTime();)
        {
            calendar.add(byte0, 1);
            date4 = calendar.getTime();
            l++;
        }

        if(byte1 == 11 || byte1 == 12 || byte1 == 13)
        {
            calendar.setTime(date2);
            calendar.add(byte0, (int)l);
            switch(byte1)
            {
            case 11: // '\013'
                l *= 24L;
                break;

            case 12: // '\f'
                l = l * 24L * 60L;
                break;

            case 13: // '\r'
                l = l * 24L * 60L * 60L;
                break;

            }
            calendar.add(byte1, 1);
            for(Date date6 = calendar.getTime(); date6.getTime() <= date3.getTime();)
            {
                calendar.add(byte1, 1);
                date6 = calendar.getTime();
                l++;
            }

        }
        return l * (long)i;
    }

    public static long dateDiff(Date date, Date date1)
    {
        if(evaluationOver())
        {
            return 0L;
        }
        else
        {
            long l = date.getTime();
            long l1 = date1.getTime();
            return l1 - l;
        }
    }

    public static String dateToGMT(Date date, String s)
        throws Exception
    {
        if(evaluationOver())
            return MSG_ALERT;
        String s1 = "";
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int i = calendar.get(7);
        int j = calendar.get(2);
        String s2 = "";
        String s3 = "";
        switch(i)
        {
        case 1: // '\001'
            s2 = "Sunday";
            break;

        case 2: // '\002'
            s2 = "Monday";
            break;

        case 3: // '\003'
            s2 = "Tuesday";
            break;

        case 4: // '\004'
            s2 = "Wednesday";
            break;

        case 5: // '\005'
            s2 = "Thursday";
            break;

        case 6: // '\006'
            s2 = "Friday";
            break;

        case 7: // '\007'
            s2 = "Saturday";
            break;

        }
        switch(j)
        {
        case 0: // '\0'
            s3 = "Jan";
            break;

        case 1: // '\001'
            s3 = "Feb";
            break;

        case 2: // '\002'
            s3 = "Mar";
            break;

        case 3: // '\003'
            s3 = "Apr";
            break;

        case 4: // '\004'
            s3 = "May";
            break;

        case 5: // '\005'
            s3 = "Jun";
            break;

        case 6: // '\006'
            s3 = "Jul";
            break;

        case 7: // '\007'
            s3 = "Aug";
            break;

        case 8: // '\b'
            s3 = "Sep";
            break;

        case 9: // '\t'
            s3 = "Oct";
            break;

        case 10: // '\n'
            s3 = "Nov";
            break;

        case 11: // '\013'
            s3 = "Dec";
            break;

        }
        if(s.equals("RFC822") || s.equals("RFC1123"))
        {
            String s5 = "'" + s2.substring(0, 3) + "', dd '" + s3 + "' yyyy HH:mm:ss 'GMT'";
            s1 = formatDate(date, s5);
        }
        else
        if(s.equals("RFC850") || s.equals("RFC1036"))
        {
            String s6 = "'" + s2 + "', dd-'" + s3 + "'-yy HH:mm:ss 'GMT'";
            s1 = formatDate(date, s6);
        }
        else
        if(s.equals("ASCTIME"))
        {
            String s7 = "'" + s2.substring(0, 3) + " " + s3 + "' d HH:mm:ss yyyy";
            s1 = formatDate(date, s7);
        }
        else
        {
            throw new Exception(EX_DATE_TO_GMT);
        }
        return s1;
    }

    public static String dateToSQL(Date date)
    {
        if(evaluationOver())
        {
            return MSG_ALERT;
        }
        else
        {
            String s = "#" + formatDate(date, "M/d/yyyy H:m:s") + "#";
            return s;
        }
    }

    private static boolean evaluationOver()
    {	
        return false;
    }

    public static String formatDate(Date date, String s)
    {
        if(evaluationOver())
        {
            return MSG_ALERT;
        }
        else
        {	
        	//ksh 수정
            /*SimpleDateFormat simpledateformat = new SimpleDateFormat(s);
            String s1 = simpledateformat.format(date);
            return s1;*/
            return DateFormatUtils.format(date, s);
        }
    }

    private static int formatMonth(String s)
        throws Exception
    {
        if(s.equalsIgnoreCase("jan"))
            return 0;
        if(s.equalsIgnoreCase("feb"))
            return 1;
        if(s.equalsIgnoreCase("mar"))
            return 2;
        if(s.equalsIgnoreCase("apr"))
            return 3;
        if(s.equalsIgnoreCase("may"))
            return 4;
        if(s.equalsIgnoreCase("jun"))
            return 5;
        if(s.equalsIgnoreCase("jul"))
            return 6;
        if(s.equalsIgnoreCase("aug"))
            return 7;
        if(s.equalsIgnoreCase("sep"))
            return 8;
        if(s.equalsIgnoreCase("oct"))
            return 9;
        if(s.equalsIgnoreCase("nov"))
            return 10;
        if(s.equalsIgnoreCase("dec"))
            return 11;
        else
            throw new Exception(EX_FORMAT_MONTH);
    }



    public static int getDay(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(5);
    }

    public static String getDayName(int i)
        throws Exception
    {
        if(evaluationOver())
            return MSG_ALERT;
        if(i >= 1 && i <= 7)
        {
            Calendar calendar = Calendar.getInstance();
            calendar.set(7, i);
            return formatDate(calendar.getTime(), "EEEE");
        }
        else
        {
            throw new Exception(EX_GET_DAY_NAME);
        }
    }

    public static String getDayName(Date date)
    {
        if(evaluationOver())
            return MSG_ALERT;
        else
            return formatDate(date, "EEEE");
    }

    public static int getElapsedTime(Date date, Date date1, Date date2)
        throws Exception
    {
        if(evaluationOver())
            return 0;
        long l = date.getTime();
        long l1 = date1.getTime();
        long l2 = date2.getTime();
        if(l > l1)
            throw new Exception(EX_GET_ELAPSED_TIME);
        if(l2 <= l)
            return 0;
        if(l2 >= l1)
        {
            return 100;
        }
        else
        {
            long l3 = (100L * dateDiff(date, date2)) / dateDiff(date, date1);
            return (int)l3;
        }
    }

    public static Date getGMTDate()
    {
        if(evaluationOver())
        {
            return new Date(0L);
        }
        else
        {
            Date date = new Date();
            long l = date.getTime();
            long l1 = getServerGMTOffset() * 60 * 1000;
            Date date1 = new Date(l - l1);
            return date1;
        }
    }

    public static int getGMTHour()
    {
        if(evaluationOver())
        {
            return 0;
        }
        else
        {
            Date date = getGMTDate();
            int i = Integer.parseInt(formatDate(date, "H"));
            return i;
        }
    }

    public static long getGMTOffset(Date date)
    {
        if(evaluationOver())
        {
            return 0L;
        }
        else
        {
            Date date1 = getGMTDate();
            long l = date1.getTime();
            long l1 = date.getTime();
            double d = l1 - l;
            double d1 = d / 1000D;
            double d2 = Math.round(d1) / 60L;
            return Math.round(d2);
        }
    }

    public static int getMonth(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(2) + 1;
    }

    public static String getMonthName(int i)
        throws Exception
    {
        if(evaluationOver())
            return MSG_ALERT;
        i--;
        if(i >= 0 && i <= 11)
        {
            Calendar calendar = Calendar.getInstance();
            calendar.set(5, 1);
            calendar.set(2, i);
            return formatDate(calendar.getTime(), "MMMM");
        }
        else
        {
            throw new Exception(EX_GET_MONTH_NAME);
        }
    }

    public static String getMonthName(Date date)
    {
        if(evaluationOver())
            return MSG_ALERT;
        else
            return formatDate(date, "MMMM");
    }

    public static Hashtable getRemainTime(Date date)
    {
        Date date1 = new Date();
        Hashtable hashtable = new Hashtable();
        if(evaluationOver())
            return hashtable;
        long l = date.getTime() - date1.getTime();
        int i = 0;
        int j = 0;
        int k = 0;
        int i1 = 0;
        int j1 = 0;
        int k1 = 0;
        int l1 = 0;
        byte byte0;
        if(l < 0L)
        {
            byte0 = -1;
            l = -l;
        }
        else
        {
            byte0 = 1;
        }
        Date date2 = new Date(date1.getTime() + l);
        i = (int)dateDiff("yyyy", date1, date2);
        date2 = addYear(date2, -i);
        if((dateDiff("m", date1, date2) < 0L || dateDiff("d", date1, date2) < 0L || dateDiff("h", date1, date2) < 0L || dateDiff("n", date1, date2) < 0L || dateDiff("s", date1, date2) < 0L) && i != 0)
        {
            i--;
            date2 = addYear(date2, 1);
        }
        j = (int)dateDiff("m", date1, date2);
        date2 = addMonth(date2, -j);
        if((dateDiff("d", date1, date2) < 0L || dateDiff("h", date1, date2) < 0L || dateDiff("n", date1, date2) < 0L || dateDiff("s", date1, date2) < 0L) && j != 0)
        {
            j--;
            date2 = addMonth(date2, 1);
        }
        k = (int)dateDiff("d", date1, date2);
        date2 = addDay(date2, -k);
        if((dateDiff("h", date1, date2) < 0L || dateDiff("n", date1, date2) < 0L || dateDiff("s", date1, date2) < 0L) && k != 0)
        {
            k--;
            date2 = addDay(date2, 1L);
        }
        i1 = (int)dateDiff("h", date1, date2);
        date2 = addHour(date2, -i1);
        if((dateDiff("n", date1, date2) < 0L || dateDiff("s", date1, date2) < 0L) && i1 != 0)
        {
            i1--;
            date2 = addHour(date2, 1L);
        }
        j1 = (int)dateDiff("n", date1, date2);
        date2 = addMinute(date2, -j1);
        if(dateDiff("s", date1, date2) < 0L && j1 != 0)
        {
            j1--;
            date2 = addMinute(date2, 1L);
        }
        k1 = (int)dateDiff("s", date1, date2);
        date2 = addSecond(date2, -k1);
        l1 = (int)(date2.getTime() - date1.getTime());
        if(l1 < 0 && k1 != 0)
        {
            k1--;
            date2 = addSecond(date2, 1L);
        }
        hashtable.put("year", Integer.valueOf(i * byte0));
        hashtable.put("month", Integer.valueOf(j * byte0));
        hashtable.put("day", Integer.valueOf(k * byte0));
        hashtable.put("hour", Integer.valueOf(i1 * byte0));
        hashtable.put("minute", Integer.valueOf(j1 * byte0));
        hashtable.put("second", Integer.valueOf(k1 * byte0));
        return hashtable;
    }

    public static int getServerGMTOffset()
    {
        if(evaluationOver())
        {
            return 0;
        }
        else
        {
            Calendar calendar = Calendar.getInstance();
            int i = calendar.get(0);
            int j = calendar.get(1);
            int k = calendar.get(2);
            int l = calendar.get(5);
            int i1 = calendar.get(7);
            int j1 = calendar.get(14);
            int k1 = Calendar.getInstance().getTimeZone().getOffset(i, j, k, l, i1, j1);
            return k1 / 1000 / 60;
        }
    }

    public static String getServerTimeZone()
    {
        if(evaluationOver())
        {
            return MSG_ALERT;
        }
        else
        {
            Calendar calendar = Calendar.getInstance();
            TimeZone timezone = calendar.getTimeZone();
            return timezone.getID();
        }
    }

    public static String getSQLComp(String s, String s1, Date date)
        throws Exception
    {
        if(evaluationOver())
            return MSG_ALERT;
        if(s1.equals("=") || s1.equals("<") || s1.equals("<=") || s1.equals(">") || s1.equals(">=") || s1.equals("<>"))
        {
            String s3 = s + s1 + "'" + dateToSQL(date) + "'";
            return s3;
        }
        else
        {
            throw new Exception(EX_SQL_COMP1);
        }
    }

    public static String getSQLComp(String s, String s1, Date date, Date date1)
        throws Exception
    {
        if(evaluationOver())
            return MSG_ALERT;
        String s4 = "";
        String s5 = "";
        if(s1.equals("><"))
        {
            s4 = ">";
            s5 = "<";
        }
        if(s1.equals(">=<"))
        {
            s4 = ">=";
            s5 = "<";
        }
        if(s1.equals("><="))
        {
            s4 = ">";
            s5 = "<=";
        }
        if(s1.equals(">=<="))
        {
            s4 = ">=";
            s5 = "<=";
        }
        if(s1.equals("><") || s1.equals(">=<") || s1.equals("><=") || s1.equals(">=<="))
        {
            String s3 = s + s4 + "'" + dateToSQL(date) + "' AND " + s + s5 + "'" + dateToSQL(date1) + "'";
            return s3;
        }
        else
        {
            throw new Exception(EX_SQL_COMP2);
        }
    }

    public static int getWeek(Date date)
    {
        if(evaluationOver())
        {
            return 0;
        }
        else
        {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int i = calendar.get(3);
            return i;
        }
    }

    public static int getWeekDay(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(7);
    }

    public static int getYear(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int i = calendar.get(1);
        int j = calendar.get(0);
        if(j == 0)
            return -1 * i;
        else
            return i;
    }

    public static Date gmtToDate(String s)
        throws Exception
    {
        if(evaluationOver())
            return new Date(0L);
        int i = 0;
        int j = 0;
        String s5 = "";
        int k = 0;
        int l = 0;
        int i1 = 0;
        int j1 = 0;
        for(int i3 = 0; i3 < s.length(); i3++)
            if(s.charAt(i3) == ' ')
                i++;

        if(i == 5)
        {
            int k1 = 1 + s.indexOf(" ");
            int j2 = s.indexOf(" ", k1);
            j = Integer.parseInt(s.substring(k1, j2));
            k1 = 1 + j2;
            j2 = s.indexOf(" ", k1);
            s5 = s.substring(k1, j2);
            k1 = 1 + j2;
            j2 = s.indexOf(" ", k1);
            k = Integer.parseInt(s.substring(k1, j2));
            k1 = 1 + j2;
            j2 = s.indexOf(":", k1);
            l = Integer.parseInt(s.substring(k1, j2));
            k1 = 1 + j2;
            j2 = s.indexOf(":", k1);
            i1 = Integer.parseInt(s.substring(k1, j2));
            k1 = 1 + j2;
            j2 = s.indexOf(" ", k1);
            j1 = Integer.parseInt(s.substring(k1, j2));
        }
        else
        if(i == 3)
        {
            int l1 = 1 + s.indexOf(" ");
            int k2 = s.indexOf("-", l1);
            j = Integer.parseInt(s.substring(l1, k2));
            l1 = 1 + k2;
            k2 = s.indexOf("-", l1);
            s5 = s.substring(l1, k2);
            l1 = 1 + k2;
            k2 = s.indexOf(" ", l1);
            k = Integer.parseInt(s.substring(l1, k2));
            if(k < 70)
                k += 2000;
            else
                k += 1900;
            l1 = 1 + k2;
            k2 = s.indexOf(":", l1);
            l = Integer.parseInt(s.substring(l1, k2));
            l1 = 1 + k2;
            k2 = s.indexOf(":", l1);
            i1 = Integer.parseInt(s.substring(l1, k2));
            l1 = 1 + k2;
            k2 = s.indexOf(" ", l1);
            j1 = Integer.parseInt(s.substring(l1, k2));
        }
        else
        if(i == 4)
        {
            int i2 = 1 + s.indexOf(" ");
            int l2 = s.indexOf(" ", i2);
            s5 = s.substring(i2, l2);
            i2 = 1 + l2;
            l2 = s.indexOf(" ", i2);
            j = Integer.parseInt(s.substring(i2, l2));
            i2 = 1 + l2;
            l2 = s.indexOf(":", i2);
            l = Integer.parseInt(s.substring(i2, l2));
            i2 = 1 + l2;
            l2 = s.indexOf(":", i2);
            i1 = Integer.parseInt(s.substring(i2, l2));
            i2 = 1 + l2;
            l2 = s.indexOf(" ", i2);
            j1 = Integer.parseInt(s.substring(i2, l2));
            i2 = 1 + l2;
            l2 = s.length();
            k = Integer.parseInt(s.substring(i2, l2));
        }
        else
        {
            throw new Exception(EX_GMT_TO_DATE);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(1, k);
        calendar.set(2, formatMonth(s5));
        calendar.set(5, j);
        calendar.set(11, l);
        calendar.set(12, i1);
        calendar.set(13, j1);
        return calendar.getTime();
    }




    public static boolean isLeap(int i)
    {
        if(evaluationOver())
            return false;
        boolean flag = i % 4 == 0;
        boolean flag1 = i % 100 == 0;
        boolean flag2 = i % 400 == 0;
        return flag && (!flag1 || flag2);
    }


    public static Date secondsTo(long l)
    {
        if(evaluationOver())
            return new Date(0L);
        else
            return new Date(l * 1000L);
    }

    public static Date strToDate(String s)
    {
        if(evaluationOver())
            return new Date(0L);
        try
        {
            DateFormat dateformat = DateFormat.getDateInstance();
            Date date = dateformat.parse(s);
            return date;
        }
        catch(Exception exception)
        {
            return null;
        }
    }

    public static Date strToDate(String s, String s1)
    {
        if(evaluationOver())
            return new Date(0L);
        SimpleDateFormat simpledateformat = new SimpleDateFormat(s1);
        try
        {
            Date date = simpledateformat.parse(s);
            return date;
        }
        catch(Exception exception)
        {
            return null;
        }
    }

    public static Date subDate(Date date, Date date1)
    {
        if(evaluationOver())
        {
            return new Date(0L);
        }
        else
        {
            long l = date.getTime();
            long l1 = date1.getTime();
            long l2 = l - l1;
            return new Date(l2);
        }
    }


	/**
	 * 현재 시간을 리턴 해준다.(서버의)
	 *
	 * @return 더해진 String(HHMM)
	 */
	public static String getNowTime() throws Exception{
    String nowTime = "";
    Calendar cld = Calendar.getInstance();
    cld.setTime(new Date());

    nowTime = ((Integer.toString(cld.get(Calendar.HOUR_OF_DAY))).length() == 1 ? "0" + Integer.toString(cld.get(Calendar.HOUR_OF_DAY)) : Integer.toString(cld.get(Calendar.HOUR_OF_DAY)));
    nowTime = nowTime + ((Integer.toString(cld.get(Calendar.MINUTE))).length() == 1 ? "0" + Integer.toString(cld.get(Calendar.MINUTE)) : Integer.toString(cld.get(Calendar.MINUTE)));

	  return  nowTime;
  }

  /**
   * 현재 시간을 리턴 해준다.(서버의)
   *
   * @return 더해진 String(HHMMss)
   */
  public static String getNowTimeSec() throws Exception{
  String nowTime = "";
  Calendar cld = Calendar.getInstance();
  cld.setTime(new Date());

  nowTime = ((Integer.toString(cld.get(Calendar.HOUR_OF_DAY))).length() == 1 ? "0" + Integer.toString(cld.get(Calendar.HOUR_OF_DAY)) : Integer.toString(cld.get(Calendar.HOUR_OF_DAY)));
  nowTime = nowTime + ((Integer.toString(cld.get(Calendar.MINUTE))).length() == 1 ? "0" + Integer.toString(cld.get(Calendar.MINUTE)) : Integer.toString(cld.get(Calendar.MINUTE)));
  nowTime = nowTime + ((Integer.toString(cld.get(Calendar.SECOND))).length() == 1 ? "0" + Integer.toString(cld.get(Calendar.SECOND)) : Integer.toString(cld.get(Calendar.SECOND)));
    return  nowTime;
}
  
	public static String timestampToString(java.sql.Timestamp time) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return time == null ? "" : dateFormat.format((java.util.Date)time);
	}
}
