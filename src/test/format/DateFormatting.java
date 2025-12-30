package test.format;

import org.apache.commons.lang.time.DateFormatUtils;

public class DateFormatting {

    /**
     * 2006. 12. 15.  KOREAN 작성
     * @param args 
     */
    public static void main(String[] args) {
        String date=DateFormatUtils.format(System.currentTimeMillis(),"yyyyMMdd|HHmmss");
        System.out.println(date);
        String time=DateFormatUtils.format(System.currentTimeMillis(),"HHmmss");
        System.out.println(time);

    }

}
