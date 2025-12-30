package test.format;

import nebsoa.common.util.FormatUtil;


public class NumberFormatting {

    /**
     * 2006. 12. 15.  KOREAN 작성
     * @param args 
     */
    public static void main(String[] args) {
        String str=FormatUtil.longPadding(12345,9);
        System.out.println(str);
        System.out.println(FormatUtil.lPadding("abc",8,'0'));

    }

}
