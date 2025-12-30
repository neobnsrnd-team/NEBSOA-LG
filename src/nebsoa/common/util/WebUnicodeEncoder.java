/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
package nebsoa.common.util;


/*******************************************************************
 * <pre>
 * 1.설명 
 * String을 &# 형태의 unicode로 encode / decode하는 메소드 구현
 * 
 * 2.사용법
 *  see encode   , decode method
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author 이종원
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 
 * $Log: WebUnicodeEncoder.java,v $
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
 * Revision 1.1  2008/08/04 08:54:50  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/02/05 02:21:52  이종원
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class WebUnicodeEncoder {

    private WebUnicodeEncoder (){}
    /**
     * 이종원 --> &#51060;&#51333;&#50896; 의 형태로 변경 됨 
     *( 이종원 --&gt; &amp;#51060;&amp;#51333;&amp;#50896; 의 형태로 변경 됨 )
     * @param s
     * @return
     */
    public static String encode(String s) {
    	if(s==null) return null;
    	char[] sArray = s.toCharArray();
    	StringBuffer buf = new StringBuffer();
        int idx = 0;
        for (int i = 0; i < sArray.length; i++) {
        	idx = sArray[i];
        	//TODO 일단 256보다 작은 값은 그냥 붙이게 하였다... 추후 확인하여 범위 수정요망
        	if(idx<256)buf.append(sArray[i]);
        	else{
            	buf.append("&#");
                buf.append((""+idx));
                buf.append(";");
        	}
        }
        return buf.toString();
    }

  
    /**
     * &#51060;&#51333;&#50896; --> 이종원 의 형태로 변경 됨 > 
     *( &amp;#51060;&amp;#51333;&amp;#50896; --&gt; 이종원   의 형태로 변경 됨 )
     * @param encodedString
     * @return
     */
    public static String decode(String encodedString) {
    	if (encodedString == null) return null;
        String [] part = encodedString.split("&#");
        int idx = 0;
        char decodedChar = 0;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < part.length; i++) {
            idx = part[i].indexOf(';');
            if (idx > 0) {
            	int unit;
                String unicodeStr = part[i].substring(0,idx);
                if(unicodeStr.startsWith("x")) {
                	unicodeStr = unicodeStr.substring(1);
                	unit = 16;
                } else {
                	unit = 10;
                }
				decodedChar = (char) Integer.parseInt(unicodeStr, unit);
                part[i] = decodedChar + part[i].substring(idx+1);
            }
            sb.append(part[i]);
        }
        return sb.toString();
    }





    public static void main(String[] args){
        String org = "hello이종원nice to meet you!!";
        String encoded = WebUnicodeEncoder.encode(org);
        System.out.println(encoded);
        String decoded = WebUnicodeEncoder.decode(encoded);
        System.out.println("Decoded==>"+decoded);
        System.out.println("org is equal decoded==>"+(org.equals(decoded)));
    }
}
