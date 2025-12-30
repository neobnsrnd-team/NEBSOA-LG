/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.util;

import java.io.*;

/*******************************************************************
 * <pre>
 * 1.설명 
 * base64 encoding decoding 처리
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
 * $Log: Base64.java,v $
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
 * Revision 1.1  2007/11/26 08:38:02  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2006/06/17 10:37:45  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class Base64
{

    public Base64()
    {
    }

    public static String encodeFromFile(String s)
    {
        StringBuffer stringbuffer = new StringBuffer();
        try
        {
            FileInputStream fileinputstream = new FileInputStream(s);
            byte abyte0[] = new byte[54];
            for(int i = 0; (i = fileinputstream.read(abyte0)) > 0;)
            {
                stringbuffer.append(encode(abyte0, i));
                stringbuffer.append("\r\n");
            }

            fileinputstream.close();
        }
        catch(IOException ioexception)
        {
            System.out.println(ioexception);
        }
        return stringbuffer.toString();

    }

    public static String encode(byte abyte0[], int i)
    {
        byte abyte1[] = new byte[i];
        for(int j = 0; j < i; j++)
            abyte1[j] = abyte0[j];

        return encode(abyte1);
    }

    public static String encode(byte abyte0[])
    {
        if(abyte0 == null)
            return null;
        StringBuffer stringbuffer = new StringBuffer();
        int i = 0;
        for(int l = abyte0.length - abyte0.length % 3; i < l;)
        {
            long l1 = ((long)abyte0[i++] & 255L) << 16 | ((long)abyte0[i++] & 255L) << 8 | (long)abyte0[i++] & 255L;
            stringbuffer.append(DIGIT[(int)(l1 >>> 18 & 63L)]);
            stringbuffer.append(DIGIT[(int)(l1 >>> 12 & 63L)]);
            stringbuffer.append(DIGIT[(int)(l1 >>> 6 & 63L)]);
            stringbuffer.append(DIGIT[(int)(l1 & 63L)]);
        }

        if(i < abyte0.length)
        {
            long l2 = ((long)abyte0[i++] & 255L) << 16;
            byte byte0 = 2;
            if(i < abyte0.length)
            {
                l2 |= ((long)abyte0[i] & 255L) << 8;
                byte0 = 1;
            }
            int i1 = 18;
            for(int j = 0; j < 4 - byte0; j++)
            {
                stringbuffer.append(DIGIT[(int)(l2 >>> i1 & 63L)]);
                i1 -= 6;
            }

            for(int k = 0; k < byte0; k++)
                stringbuffer.append('=');

        }
        return stringbuffer.toString();
    }

    public static String encode(String s)
    {
        if(s == null)
            return null;
        else
            return encode(s.getBytes());
    }

    public static long ScanCode(byte byte0)
    {
        int i;
        for(i = 0; i < 64; i++)
            if(DIGIT[i] == byte0)
                break;

        if(i >= 64)
            return byte0 != 61 ? 255L : 254L;
        else
            return (long)i;
    }

    public static String decode(byte abyte0[])
    {
        if(abyte0 == null)
            return null;
        byte abyte1[] = new byte[abyte0.length];
        long l = 0L;
        int i = 0;
        int j = 0;
        for(int k = 0; k < abyte0.length; k++)
            if(ScanCode(abyte0[k]) != 255L)
            {
                if(ScanCode(abyte0[k]) == 254L)
                {
                    l <<= 6;
                    j += 6;
                } else
                {
                    l <<= 6;
                    l |= ScanCode(abyte0[k]);
                    j += 6;
                }
                if(j == 24)
                {
                    abyte1[i++] = (byte)(int)(l >> 16 & 255L);
                    abyte1[i++] = (byte)(int)(l >> 8 & 255L);
                    abyte1[i++] = (byte)(int)(l & 255L);
                    l = 0L;
                    j = 0;
                }
            }

        byte abyte2[] = new byte[i];
        for(int i1 = 0; i1 < i; i1++)
            abyte2[i1] = abyte1[i1];

        return new String(abyte2);
    }

    public static byte[] decodebyte(byte abyte0[])
    {
        if(abyte0 == null)
            return null;
        byte abyte1[] = new byte[abyte0.length];
        long l = 0L;
        int i = 0;
        int j = 0;
        for(int k = 0; k < abyte0.length; k++)
            if(ScanCode(abyte0[k]) != 255L)
            {
                if(ScanCode(abyte0[k]) == 254L)
                {
                    l <<= 6;
                    j += 6;
                } else
                {
                    l <<= 6;
                    l |= ScanCode(abyte0[k]);
                    j += 6;
                }
                if(j == 24)
                {
                    abyte1[i++] = (byte)(int)(l >> 16 & 255L);
                    abyte1[i++] = (byte)(int)(l >> 8 & 255L);
                    abyte1[i++] = (byte)(int)(l & 255L);
                    l = 0L;
                    j = 0;
                }
            }

        byte abyte2[] = new byte[i];
        for(int i1 = 0; i1 < i; i1++)
            abyte2[i1] = abyte1[i1];

        return abyte2;
    }

    public static String decode(String s)
    {
        return decode(s.getBytes());
    }

    public static byte[] decodebyte(String s)
    {
        return decodebyte(s.getBytes());
    }

    public static void main(String args[])
    {
        Base64 base64 = new Base64();
    }

    private static final char DIGIT[] = {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 
        'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 
        'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 
        'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 
        'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 
        'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', 
        '8', '9', '+', '/'
    };

}
