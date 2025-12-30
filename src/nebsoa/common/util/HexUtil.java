/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.util;

import java.io.ByteArrayOutputStream;

import nebsoa.common.exception.SysException;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 헥사 값 변환 유틸리티
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
 * $Log: HexUtil.java,v $
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
 * Revision 1.1  2008/01/22 05:58:17  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:03  안경아
 * *** empty log message ***
 *
 * Revision 1.3  2006/09/06 06:01:35  김승희
 * 메소드 추가
 *
 * Revision 1.2  2006/08/08 07:14:45  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2006/07/25 01:38:21  김승희
 * 패키지 변경
 *
 * Revision 1.2  2006/06/17 10:22:59  김승희
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public class HexUtil {

    /** Parse an int from a substring.
     * Negative numbers are not handled.
     * @param s String
     * @param offset Offset within string
     * @param length Length of integer or -1 for remainder of string
     * @param base base of the integer
     * @exception NumberFormatException 
     */
    public static int parseInt(String s, int offset, int length, int base) throws NumberFormatException {
        int value = 0;

        if (length < 0)
            length = s.length() - offset;

        for (int i = 0; i < length; i++) {
            char c = s.charAt(offset + i);

            int digit = c - '0';
            if (digit < 0 || digit >= base || digit >= 10) {
                digit = 10 + c - 'A';
                if (digit < 10 || digit >= base)
                    digit = 10 + c - 'a';
            }
            if (digit < 0 || digit >= base)
                throw new NumberFormatException(s.substring(offset, offset + length));
            value = value * base + digit;
        }
        return value;
    }
    
    public static byte[] parseBytes(String s, int base) {
        byte[] bytes = new byte[s.length() / 2];
        for (int i = 0; i < s.length(); i += 2)
            bytes[i / 2] = (byte) HexUtil.parseInt(s, i, 2, base);
        return bytes;
    }
    
    /** 
     * @param b An ASCII encoded character 0-9 a-f A-F
     * @return The byte value of the character 0-16.
     */
    public static byte convertHexDigit(byte b) {
        if ((b >= '0') && (b <= '9'))
            return (byte) (b - '0');
        if ((b >= 'a') && (b <= 'f'))
            return (byte) (b - 'a' + 10);
        if ((b >= 'A') && (b <= 'F'))
            return (byte) (b - 'A' + 10);
        return 0;
    }

    public static String viewHexString(byte[] b) {
        return viewHexString(b, 0, b.length);
    }

    public static String viewHexString(byte[] b, int offset) {
        return viewHexString(b, offset, b.length - offset);
    }
    
    /**
     * byte[]를 분석할 수 있는 Hexa 값과 문자열값을 표현하는 문자열을 반환한다.
     * @param b
     * @param offset
     * @param length
     * @return
     */
    public static String viewHexString(byte[] b, int offset, int length) {
        if(b == null || offset < 0 || length < 0 || b.length < offset + length) {
            return "IllegalArgument";
        }

        StringBuffer buf = new StringBuffer();

        ByteArrayOutputStream lineBuff = new ByteArrayOutputStream();
        for (int i = offset; i < offset + length; i++) {

            if (i % 16 != 0) {
                buf.append(' ');
            }
            else if ((i != 0) && (i % 16 == 0)) {
                buf.append("    |"); //line
                buf.append(lineBuff.toString()).append("|"); //line
                buf.append('\n');
                lineBuff.reset(); //line
            }
            lineBuff.write(((((b[i] >= 0x00) && (b[i] <= 0x20)) || (b[i] == 0x7F)) ? '.' : b[i]));

            int bi = 0xff & b[i];
            int c = '0' + (bi / 16) % 16;
            if (c > '9')
                c = 'A' + (c - '0' - 10);
            buf.append((char) c);
            c = '0' + bi % 16;
            if (c > '9')
                c = 'A' + (c - '0' - 10);
            buf.append((char) c);
        }
        
        if (lineBuff.size() != 0) {
            int remain = 16 - lineBuff.size();
            for (int j = 0; j < remain; j++) {
                buf.append("   ");
            }
            buf.append("    |");
            buf.append(lineBuff.toString());
            for (int j = 0; j < remain; j++) {
                buf.append(" ");
            }
            buf.append("|");
        }
        return buf.toString();
    }

    public static String toHexString(byte[] b) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            int bi = 0xff & b[i];
            int c = '0' + (bi / 16) % 16;
            if (c > '9')
                c = 'A' + (c - '0' - 10);
            buf.append((char) c);
            c = '0' + bi % 16;
            if (c > '9')
                c = 'A' + (c - '0' - 10);
            buf.append((char) c);
        }
        return buf.toString();
    }

    public static String toHexString(byte[] b, int offset, int length) {
        StringBuffer buf = new StringBuffer();
        for (int i = offset; i < offset + length; i++) {
            int bi = 0xff & b[i];
            int c = '0' + (bi / 16) % 16;
            if (c > '9')
                c = 'A' + (c - '0' - 10);
            buf.append((char) c);
            c = '0' + bi % 16;
            if (c > '9')
                c = 'A' + (c - '0' - 10);
            buf.append((char) c);
        }
        return buf.toString();
    }

    public static byte[] fromHexString(String s) {
        if (s.length() % 2 != 0)
            throw new IllegalArgumentException(s);
        byte[] array = new byte[s.length() / 2];
        for (int i = 0; i < array.length; i++) {
            int b = Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16);
            array[i] = (byte) (0xff & b);
        }
        return array;
    }
    
    /**
     * 헥사코드String으로 변환한다.
     * @param data
     * @param off
     * @param len
     * @return
     */
    public static String byteArrayToHex(byte[] data, int off, int len) { 
    	StringBuffer buf = new StringBuffer(); 
    	for (int i = 0; i < len; i++) { 
    	    byte b = data[off + i]; 
    	    buf.append(intToChar((b >> 4) & 15)); 
    	    buf.append(intToChar(b & 15)); 
    	} 
    	return buf.toString(); 
    } 
    static  char intToChar(int val) { 
        switch (val) {
	        case 0: 
	        return '0'; 
	         
	        case 1: 
	        return '1'; 
	         
	        case 2: 
	        return '2'; 
	         
	        case 3: 
	        return '3'; 
	         
	        case 4: 
	        return '4'; 
	         
	        case 5: 
	        return '5'; 
	         
	        case 6: 
	        return '6'; 
	         
	        case 7: 
	        return '7'; 
	         
	        case 8: 
	        return '8'; 
	         
	        case 9: 
	        return '9'; 
	         
	        case 10: 
	        return 'A'; 
	         
	        case 11: 
	        return 'B'; 
	         
	        case 12: 
	        return 'C'; 
	         
	        case 13: 
	        return 'D'; 
	         
	        case 14: 
	        return 'E'; 
	         
	        case 15: 
	        return 'F'; 
	         
	        default: 
	        throw new SysException("올바르지 못한 값입니다."); 
	         
	    } 
     } 
    
    /**
     * 헥사코드 String을 byte[]로 변환한다.
     * @param value 헥사코드 String
     * @return
     */
    public static  byte[] hexToByteArray(String value) { 
        return hexToByteArray(value, 0, value.length()); 
    } 
     
    /**
     * 헥사코드 String을 byte[]로 변환한다.
     * @param value 헥사코드 String
     * @param offset
     * @param len
     * @return
     */
    public static  byte[] hexToByteArray(String value, int offset, int len) { 
        if ((len & 1) != 0) throw new IllegalArgumentException("Invalid hex string length"); 
        byte[] result = new byte[len / 2]; 
        for (int i = 0; i < len / 2; i++) { 
            char high = value.charAt(offset + (i * 2)); 
            char low = value.charAt(offset + (i * 2) + 1); 
            result[i] = (byte)((charToInt(high) << 4) | charToInt(low)); 
        } 
        return result; 
    } 
     
    static  int charToInt(char ch) { 
        if (ch >= '0' && ch <= '9') return ch - '0'; 
        if (ch >= 'A' && ch <= 'F') return 10 + ch - 'A'; 
        if (ch >= 'a' && ch <= 'f') return 10 + ch - 'a'; 
        throw new SysException("올바르지 못한 값입니다."); 
    }

}
