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
 * String을 unicode로 encode / decode하는 메소드 구현
 * 
 * 2.사용법
 * 
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author 이종원
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 
 * $Log: UnicodeEncoder.java,v $
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
 * Revision 1.1  2008/01/22 05:58:18  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:04  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2006/11/09 07:25:54  이종원
 * Unicode encode/ decode추가
 *
 * Revision 1.1  2006/11/09 07:24:15  이종원
 * Unicode encode/ decode추가
 *
 * </pre>
 ******************************************************************/
public class UnicodeEncoder {

    private UnicodeEncoder (){}
    private static StringBuffer charAccumulator = new StringBuffer(2);

    public static String encode(String s) {
        StringBuffer sb = new StringBuffer();
        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++) {
          if (chars[i] <= 127) {
            sb.append(chars[i]);
            continue;
          }
          int codePoint = Integer.MIN_VALUE;
          if (isLowSurrogate(chars[i]) || isHighSurrogate(chars[i])) {
            charAccumulator.append(chars[i]);
          } else {
            codePoint = chars[i];
          }
          if (charAccumulator.length() == 2) {
            codePoint = toCodePoint(charAccumulator.charAt(0), charAccumulator.charAt(1));
            charAccumulator.setLength(0);
          }
          if (charAccumulator.length() == 0) {
            sb.append(encode(codePoint));
          }
        }
        return sb.toString();
    }

    public static String encode(int c) {
        StringBuffer sb = new StringBuffer(10);
        sb.append(Integer.toHexString(c));
        while (sb.length() < 4) {
          sb.insert(0, '0');
        }
        sb.insert(0, "\\u");
        return sb.toString();
    }
  
  /*
   * Copied from JDK 1.4 Properties
   * Converts encoded &#92;uxxxx to unicode chars
   * and changes special saved chars to their original forms
   */
    public static String decode(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
    
        for (int x=0; x<len; ) {
          aChar = theString.charAt(x++);
          if (aChar == '\\') {
            aChar = theString.charAt(x++);
            if (aChar == 'u') {
              // Read the xxxx
              int value=0;
              for (int i=0; i<4; i++) {
                aChar = theString.charAt(x++);
                switch (aChar) {
                  case '0': case '1': case '2': case '3': case '4':
                  case '5': case '6': case '7': case '8': case '9':
                    value = (value << 4) + aChar - '0';
                    break;
                  case 'a': case 'b': case 'c':
                  case 'd': case 'e': case 'f':
                    value = (value << 4) + 10 + aChar - 'a';
                    break;
                  case 'A': case 'B': case 'C':
                  case 'D': case 'E': case 'F':
                    value = (value << 4) + 10 + aChar - 'A';
                    break;
                  default:
                    throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
                }
              }
              outBuffer.append((char)value);
            } else {
              if (aChar == 't') aChar = '\t';
              else if (aChar == 'r') aChar = '\r';
              else if (aChar == 'n') aChar = '\n';
              else if (aChar == 'f') aChar = '\f';
              outBuffer.append(aChar);
            }
          } else {
            outBuffer.append(aChar);
          }
        }
        return outBuffer.toString();
    }

  // the following are copied from Java 5

    private static boolean isHighSurrogate(char ch) {
        return ch >= MIN_HIGH_SURROGATE && ch <= MAX_HIGH_SURROGATE;
    }

    private static boolean isLowSurrogate(char ch) {
        return ch >= MIN_LOW_SURROGATE && ch <= MAX_LOW_SURROGATE;
    }

    private static int toCodePoint(char high, char low) {
        return ((high - MIN_HIGH_SURROGATE) << 10)
        + (low - MIN_LOW_SURROGATE) + MIN_SUPPLEMENTARY_CODE_POINT;
    }

    private static final char MIN_HIGH_SURROGATE = '\uD800';
    private static final char MAX_HIGH_SURROGATE = '\uDBFF';
    private static final char MIN_LOW_SURROGATE  = '\uDC00';
    private static final char MAX_LOW_SURROGATE  = '\uDFFF';
    private static final int MIN_SUPPLEMENTARY_CODE_POINT = 0x010000;

    public static void main(String[] args){
        String org = "hello이종원nice to meet you!!";
        String encoded = UnicodeEncoder.encode(org);
        System.out.println(encoded);
        String decoded = UnicodeEncoder.decode("hello\uc774\uc885\uc6d0nice to meet you!!");
        System.out.println("Decoded==>"+decoded);
        System.out.println("org is equal decoded==>"+(org.equals(decoded)));
    }
}
