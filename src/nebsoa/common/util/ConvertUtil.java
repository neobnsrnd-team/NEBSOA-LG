/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.util;

import java.util.BitSet;

import org.apache.commons.lang.StringUtils;


/*******************************************************************
 * <pre>
 * 1.설명 
 * 데이터 conversion 과 관련된 유틸리티 클래스
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
 * $Log: ConvertUtil.java,v $
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
 * Revision 1.1  2008/01/22 05:58:17  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:01  안경아
 * *** empty log message ***
 *
 * Revision 1.4  2006/07/25 01:37:53  김승희
 * 참조 클래스 패키지 변경
 *
 * Revision 1.3  2006/07/25 01:11:40  김승희
 * 헥사값 -->비트 표현식, 비트 표현식-->헥사값 치환 메소드 추가
 *
 * Revision 1.2  2006/06/17 10:37:45  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public final class ConvertUtil {

    /** 2진수 */
    private static final int BINARY_RADIX = 2;
    /** 8진수 */
    private static final int OCTAL_RADIX = 8;
    /** 10진수 */
    private static final int DECIMAL_RADIX = 10;
    /** 16진수 */
    private static final int HEX_RADIX = 16;
    
    // 아래는 unsigned data type 들을 위한 bit mask 이다.
    /** 1 바이트 unsigned data type 을 위한 byte bit-mask */
    private static final byte BYTE_BIT_MASK = (byte)0x7f;
    /** 2 바이트 unsigned data type 을 위한 short bit-mask */
    private static final short SHORT_BIT_MASK = (short)0x7fff;
    /** 4 바이트 unsigned data type 을 위한 int bit-mask */
    private static final int INT_BIT_MASK = 0x7fffffff;
    /** 8 바이트 unsigned data type 을 위한 long bit-mask */
    private static final long LONG_BIT_MASK = 0x7fffffffffffffffl;
    
    // 아래는 sign-bit 가 1 일 경우 결과 값을 보정해주는 보정계수들 이다.
    /** unsigned-char 의 sign-bit 를 보정하기 위한 보정계수 */
    private static final short BYTE_COMPENSATOR = Byte.MAX_VALUE + 1;
    /** unsigned-int[32bit] 의 sign-bit 를 보정하기 위한 보정계수 */
    private static final int SHORT_COMPENSATOR = Short.MAX_VALUE + 1;
    /** unsigned-int[64bit] or unsigned-long[32bit] 의 sign-bit 를 보정하기 위한 보정계수 */
    private static final long INT_COMPENSATOR = Integer.MAX_VALUE + 1;
    /** unsigned-long[64bit] 의 sign-bit 를 보정하기 위한 보정계수 */
    private static final double LONG_COMPENSATOR = Long.MAX_VALUE + 1;
    
    /*
     * 아래는 호스트 시스템(메인프레임)에서 사용하는,
     * 한글을 제어하기 위한 제어문자를 나타낸다.
     * 2004.06.30 - 이종원
     */
    /** 제어문자 SO(Shift Out) [0x0e] */
    private static final byte CONTROL_CHAR_SO = 0x0e;
    /** 제어문자 SI(Shift In) [0x0f] */
    private static final byte CONTROL_CHAR_SI = 0x0f;
        
    /**
     * 유틸리티 클래스의 객체생성을 방지한다.
     */
    private ConvertUtil() {
    }//end of constructor
    
    /**
     * byte type 의 Unsigned-Char 를 int type 의 데이터로 리턴한다.
     * 
     * @param uchar 변환하려는 byte [] type 의 Unsigned-Char
     * @return 변환된 int type 의 데이터
     */
    public static int uchar2Int(byte uchar) {
        if (uchar >= 0) {
            return uchar; 
        }//end if
        uchar &= BYTE_BIT_MASK;
        return uchar + BYTE_COMPENSATOR;
    }//end of uchar2Int()   
    
    /**
     * int type 의 데이터를 byte [] type 의 Unsigned-Char 데이터로 리턴한다.
     * 
     * @param srcUchar 변환하려는 int type 의 데이터
     * @return 변환된 byte type 의 Unsigned-Char
     */
    public static byte int2Uchar(int srcUchar) {
        if (srcUchar < 0 || srcUchar > 255) {
            throw new IllegalArgumentException("Unsigned Char's range must be between 0 and 255 !");
        }//end if
        return (byte)srcUchar;
    }//end of int2Uchar()   
    
    /**
     * int type 의 데이터를 byte [] type 의 Unsigned-Char 데이터로 리턴한다.
     * 
     * @param srcUchar 변환하려는 int type 의 데이터
     * @return 변환된 byte [] type 의 Unsigned-Char
     */
    public static byte [] int2Uchar2(int srcUchar) {
        if (srcUchar < 0 || srcUchar > 65535) {
            throw new IllegalArgumentException("Unsigned Char's range must be between 0 and 65535 !");
        }//end if
        byte [] convertResult = new byte[2];
        convertResult[0] = (byte)(srcUchar >>> 8);
        convertResult[1] = (byte)(srcUchar & 0xff);
        return convertResult;
    }//end of int2Uchar2()

    /**
     * byte [] type 의 Unsigned-Char 를 int type 의 데이터로 리턴한다.
     * 
     * @param uchar 변환하려는 byte [] type 의 Unsigned-Char
     * @return 변환된 int type 의 데이터
     */
    public static int uchar2Int2(byte [] uchar) {
        if (uchar.length != 2) {
            throw new IllegalArgumentException("Unsigned Char's byte [] length must be 2 bytes !");
        }//end if
        int i1 = uchar2Int(uchar[0]);
        int i2 = uchar2Int(uchar[1]);
        i1 <<= 8;
        return i1 | i2;
    }//end of uchar2Int2()
    
    /**
     * 8 바이트의 문자열 형태로 구성된 16진수 long(4 bytes) 값을 Java 의 long(8 bytes) 으로 변환한다.
     * 
     * @param hexString 8 바이트의 문자열 16진수 값
     * @return Java 의 long 타입 숫자 값
     */
    public static long hexString2Long(byte [] hexString) {
        if (hexString.length != 8) {
            throw new IllegalArgumentException("Hex String byte [] length must be 8 bytes !");
        }//end if
        return Long.parseLong(new String(hexString), HEX_RADIX);
    }//end of hexString2Long()
    
    /**
     * long 타입의 문자열을 받아서 8 바이트의 문자열 형태로 구성된 16진수 long(4 bytes) 값으로 변환한다.
     * 
     * @param hexValue Java 의 long 타입 숫자 값
     * @return hexString 8 바이트의 문자열 16진수 값
     */
    public static byte [] long2HexString(long hexValue) {
        if (hexValue < 0 || hexValue > 4294967296L) {
            throw new IllegalArgumentException("Hex Value's range must be between 0 and 4294967296 !");
        }//end if
        String hexString = Long.toHexString(hexValue);
        return FormatUtil.lPadding(hexString, 8, '0').getBytes();
    }//end of long2HexString()
    
    /**
     * 주어진 byte []  형태의 데이터를 BCD 타입으로 변환한다.
     * 
     * @param source 변환하려는 byte [] 데이터
     * @return BCD 형태로 변환된 문자열
     */
    public static String convertBcd(byte [] source) {
        StringBuffer convertData = new StringBuffer();
        
        for (int i = 0; i < source.length; i++) {
            String temp = Integer.toHexString(source[i]);
            int tempLength = temp.length();
            /*
             * 아래는 헥사 값이 한자리 일 경우, 두 자리인 규격에 맞추도록
             * '0'을 붙인다. 이때, 값이 아예 없는 경우는 없으므로, 그건 신경 안쓴다.
             * 
             * 값(길이)은 항상 1과 같거나 크므로....
             */
            if (tempLength == 1) {
                convertData.append("0").append(temp);
            } else {
                convertData.append(temp.substring(tempLength - 2, tempLength));
            }//end if           
        }//end for
        
        return convertData.toString();
    }//end of convertBcd()
    
    /**
     * 주어진 BCD 형태의 문자열을 byte []  로 변환한다. 
     * 
     * @param source 변환하려는 문자열
     * @return byte [] 형태로 변환된 데이터
     */
    public static byte [] convertBcd(String source) {
        int sLength = source.length();
        
        if (sLength % 2 != 0) {
            throw new IllegalArgumentException("Input String length must be an even number !!");
        }//end if
        
        byte [] convertData = new byte[sLength / 2];
        
        for (int i = 0; i < sLength; i = i + 2) {
            convertData[i/2] = (byte)Short.parseShort(source.substring(i, i + 2), HEX_RADIX);
        }//end for
        
        return convertData;
    }//end of convertBcd()
    
    /**
     * 주어진 한글과 크기를 가지고,
     * SO/SI 를 맨 앞 바이트와 맨 뒷 바이트에 붙여서 리턴한다.
     * 
     * @param korean 호스트용 한글로 변환할 문자열
     * @param size 변환할 필드의 크기
     * @return 호스트용 한글로 변환된 문자열
     */
    public static byte [] convertHostKorean(String korean, int size) {
        byte [] returnData = new byte[size];
        
        // 아래는 제어문자를 맨 앞 바이트와 맨 뒷 바이트에 추가하는 부분이다.
        returnData[0] = CONTROL_CHAR_SO;
        returnData[size - 1] = CONTROL_CHAR_SI;
        
        // 아래는 주어진 한글에, 길이에 맞게 공백문자를 패딩하는 부분이다.
        byte [] bKorean = FormatUtil.rPadding(korean, size - 2, ' ').getBytes();
        
        // 한글을 리턴할 byte [] 에 붙여 넣는다.
        System.arraycopy(bKorean, 0, returnData, 1, bKorean.length);
        
        return returnData;
    }//end of convertHostKorean()
    
    /**
     * 주어진 BitSet 객체로부터 각각의 bit 정보를 추출하여
     * 해당 정보를 포함하는 byte [] 객체를 리턴합니다.
     * 
     * @param bits 변환하려는 대상 BitSet 객체
     * @return 변환된 결과 byte []
     */
    public static byte [] toBytes(BitSet bits) {
        int bitSetLength = bits.length();
        int size = (bitSetLength % 8 == 0) ? bitSetLength / 8 : bitSetLength / 8 + 1;
        byte [] bytes = new byte[size];
        for (int i=0, loopCount = bits.length(); i < loopCount; i++) {
            if (bits.get(i)) {
                bytes[bytes.length - i / 8 - 1] |= 1 << (i % 8);
            }//end if
        }//end for
        return bytes;
    }//end of toBytes()
    
    /**
     * 주어진 byte [] 로부터 각각의 bit 정보를 추출하여
     * 해당 정보를 포함하는 BitSet 객체를 리턴합니다.
     * 
     * @param bytes 변환하려는 대상 byte []
     * @return 변환된 결과 BitSet 객체
     */
    public static BitSet toBitSets(byte [] bytes) {
        BitSet bits = new BitSet();
        for (int i=0, loopCount = bytes.length * 8; i < loopCount; i++) {
            if ((bytes[bytes.length - i / 8 - 1] & (1 << (i % 8))) > 0) {
                bits.set(i);
            }//end if
        }//end for
        return bits;
    }//end of toBitSets()
    
    /**
     * 주어진 BitSet 객체로부터 각각의 bit 정보를 추출하여
     * 해당 정보를 포함하는 byte 를 리턴합니다.
     * 
     * @param srcBit 변환하려는 대상 BitSet 객체
     * @return 변환된 결과 byte
     */
    public static byte toByte(BitSet srcBit) {
        return toBytes(srcBit)[0];
    }//end of toByte()
    
    /**
     * 주어진 byte 로부터 각각의 bit 정보를 추출하여
     * 해당 정보를 포함하는 BitSet 객체를 리턴합니다.
     * 
     * @param srcByte 변환하려는 대상 byte
     * @return 변환된 결과 BitSet 객체
     */
    public static BitSet toBitSet(byte srcByte) {
        byte [] srcBytes = {srcByte};
        return toBitSets(srcBytes);
    }//end of toBitSet()
    
    /**
     * 주어진 int [] 형태의 bit set index 정보를 가지고
     * 한 byte 내의 특정 bit 를 set 한 결과를 리턴합니다.
     * 
     * 예) 만약, 한 바이트 내의 2, 6 번 비트를 셋하고 싶은 경우
     * int bitIdx = {2, 6};
     * byte result = ConvertUtil.bitSet(bitIdx);
     * 
     * 결과 : 68 [= 01000100]
     * 
     * @param bitSetIndex set 하려는 bit index
     * @return 특정 bit index 가 set 된 byte
     */
    public static byte bitSet(int [] bitSetIndex) {
        BitSet bits = new BitSet();
        for (int i = 0; i < bitSetIndex.length; i++) {
            bits.set(bitSetIndex[i]);
        }//end for
        return toByte(bits);
    }//end of bitSet()
    
    /**
     * BitSet 내부의 bit 정보를 문자열 형태로 리턴합니다.
     * 
     * @param bits 문자열 형태로 변환하려는 대상 BitSet 객체
     * @return 변환된 결과 문자열
     */
    public static String printBitSet(BitSet bits) {
        StringBuffer buffer = new StringBuffer();
        //buffer.append("BitSet : [");
        int flag = 1;
        for (int i = 0, loopCount = bits.length(); i < loopCount; i++) {
            buffer.append(bits.get(i) ? "1" : "0");
            if (bits.length() / 8 != flag  && (i + 1) % 8 == 0) {
                buffer.append(" ");
                flag++;
            }//end if
        }//end for
        //buffer.append("]");
        return buffer.toString();
    }//end of printBitSet()

    
	/** 
	 * 0 또는 1로 각 비트를 표현한 문자열 배열을 Hexa 값 문자열로 치환하여 리턴한다.
	 * <pre>
	 * 예)
	 * String[] bitExpression = {"01000000", "00000000"};
	 * String hexString = ConvertUtil.convertBitToHex(bitExpression);
	 * 
	 * --> hexString : "0200"
	 * </pre>
	 * 
	 * @param bitExpression
	 * @return Hexa 값 문자열
	 */
	public static String convertBitToHex(String[] bitExpression){
		if(bitExpression==null) return null;
		
		int length = bitExpression.length;
		BitSet[] bitSet = new BitSet[length];
		for(int i=0; i<length; i++){
			bitSet[i] = new BitSet();
		}
		for(int i=0; i<bitExpression.length; i++){
			
			if(bitExpression[i]==null) bitExpression[i] = "00000000";
			else bitExpression[i] = StringUtils.rightPad(bitExpression[i], 8, "0");
			
			for(int j=0; j<8; j++){
				if(bitExpression[i].charAt(j)=='1'){
					bitSet[i].set(j);
				}
			}
		}
		byte[] bytes = new byte[length];
		
		for(int i=0; i<length; i++){
			if(!bitSet[i].isEmpty()) bytes[i] = ConvertUtil.toByte(bitSet[i]);
		}
		return HexUtil.toHexString(bytes);
	}
	
	/**
	 * Hexa 값을 표현한 문자열을 1byte단위로 잘라서 0 또는 1로 표현된 bit단뒤 표현식으로 치환하여 리턴한다.
	 * <pre>
	 * 예)
	 * String hexString = "0200";
	 * String[] bitExpression = ConvertUtil.convertHexToBit(hexString);
	 * 
	 * --> bitExpression[0] : "01000000"
	 *     bitExpression[1] : "00000000"
	 * </pre>
	 * @param hexString
	 * @return bit단위로 치환된 문자열 배열
	 */
	public static String[] convertHexToBit(String hexString){
		if(hexString==null) return null;
		int length = hexString.length()/2;
		String[] bitExpression = new String[length];
	
		for(int i=0,  j=-1; i+2<=hexString.length(); i+=2){
			j++;
			byte[] bytes = HexUtil.fromHexString(hexString.substring(i, (i+2)));
			BitSet bitSet = ConvertUtil.toBitSets(bytes);
			if(!bitSet.isEmpty())
				bitExpression[j] = StringUtils.rightPad(ConvertUtil.printBitSet(bitSet), 8, "0");
			else bitExpression[j] = "00000000";
				
		}
		
		return bitExpression;
	}
	
    // 이 메인 메소드는 테스트를 위해서 작성되었습니다.
    public static void main(String [] args) {
        
        /*======================================================================
         * 여기서 부터는 uChar2Int() 메소드와 int2UChar(), toByteArray(), fromByteArray()
         * 테스트 코드가 있습니다.
         ======================================================================*/
        final int uCharSize = 8;
        BitSet ucharBitSet = new BitSet(uCharSize);
        ucharBitSet.set(0, uCharSize, true);
        ConvertUtil.printBitSet(ucharBitSet);
        System.out.println("BitSet : " + ucharBitSet);
        byte [] uchar = ConvertUtil.toBytes(ucharBitSet);
        int data = ConvertUtil.uchar2Int(uchar[0]);
        System.out.println("Convert Data (uchar -> int) : " + data);
        byte result = ConvertUtil.int2Uchar(data);
        System.out.println("Convert Data (int -> byte) : " + result);
        byte [] resultByte = { result, result, result, result };
        ucharBitSet = ConvertUtil.toBitSets(resultByte);
        System.out.println(ConvertUtil.printBitSet(ucharBitSet));
        System.out.println("BitSet : " + ucharBitSet);
        
        System.out.println("조만희".length());
        System.out.println("조만희".getBytes().length);
        
        System.out.println("abc".length());
        System.out.println("abc".getBytes().length);
        
        int [] intArray = {1, 2, 3, 4};
        BitSet bs = new BitSet();
        bs.set(intArray[0]);
        bs.set(intArray[1]);
        bs.set(intArray[2]);
        bs.set(intArray[3]);
        
        System.out.println("ConvertUtil.printBitSet(bs) : " + ConvertUtil.printBitSet(bs));
        
        byte b11 = 21;
        BitSet bs2 = toBitSet(b11);
        System.out.println("ConvertUtil.printBitSet(bs) : " + ConvertUtil.printBitSet(bs2));
        System.out.println("bs2 : " + toByte(bs2));
        
        int [] bitIdx = {2, 6};
        byte b22 = bitSet(bitIdx);
        
        System.out.println("\t##### bitSet(bitIdx) : " + b22);
        System.out.println("\t##### bitSet(bitIdx) : " + Integer.toBinaryString(b22));
        
        /*======================================================================
         * 여기서 부터는 uChar2Int2() 메소드와 int2UChar2()
         * 테스트 코드가 있습니다.
         ======================================================================*/       
        byte b1 = (byte)0x10;
        byte b2 = (byte)0xf0;
        
        short s1 = (short)0x10f0;
        
        System.out.println("byte : 10 : " + b1);
        System.out.println("byte : f0 : " + b2);
        
        System.out.println("short : 10f0 : " + s1);
        
        byte [] bb1 = {b1, b2};
        
        System.out.println("uchar2Int2() : " + uchar2Int2(bb1));
        
        byte [] bb2 = int2Uchar2(s1);
        
        System.out.println("byte : bb2[0] : " + bb2[0]);
        System.out.println("byte : bb2[1] : " + bb2[1]);
        
        
        /*======================================================================
         * 여기서 부터는 hexString2Long() 메소드와 long2HexString()
         * 테스트 코드가 있습니다.
         ======================================================================*/
         
         System.out.println("-------- 1st Test --------");
         
         String testHexString = "1a2a3a4a";
         
         long hex = hexString2Long(testHexString.getBytes());
         
         System.out.println("hexString2Long() : [testHexString:" + testHexString + "->hex:" + hex + "]");
         
         testHexString = new String(long2HexString(hex));
         
        System.out.println("long2HexString() : [hex:" + hex + "->testHexString:" + testHexString + "]");
        
        
        System.out.println("-------- 2nd Test --------");
        
        testHexString = "00003a4a";
         
        hex = hexString2Long(testHexString.getBytes());
         
        System.out.println("hexString2Long() : [testHexString:" + testHexString + "->hex:" + hex + "]");
         
        testHexString = new String(long2HexString(hex));
         
       System.out.println("long2HexString() : [hex:" + hex + "->testHexString:" + testHexString + "]");
       
       /*======================================================================
        * 여기서 부터는 convertBcd() 메소드의
        * 테스트 코드가 있습니다.
        ======================================================================*/
         
        System.out.println("-------- BCD Test1 --------");
         
        byte [] bcdSource = "가나다라".getBytes();
         
        String bcdResult = convertBcd(bcdSource);
        
        System.out.println("convertBcd() : [source:" + new String(bcdSource) + "->result:" + bcdResult + "]");
         
        bcdSource = convertBcd(bcdResult);
         
       System.out.println("convertBcd() : [result:" + bcdResult + "->source:" + new String(bcdSource) + "]");
       
       System.out.println("-------- BCD Test2 --------");
         
       byte [] bcd2 = {15};
         
       String bcdR2 = convertBcd(bcd2);
        
       System.out.println("convertBcd() : [source:" + bcd2[0] + "->result:" + bcdR2 + "]");
         
       bcd2 = convertBcd(bcdR2);
         
      System.out.println("convertBcd() : [result:" + bcdR2 + "->source:" + bcd2[0] + "]");
      
      String testData = "504b0304140004000800a955d1303893deeb1f0000002700000004000000616161610d87c109000010825692ba47edbfd805822a5f3a709855827735b50e81160f504b01021400140004000800a955d1303893deeb1f0000002700000004000000000000000100200000000000000061616161504b0506000000000100010032000000410000000000";
      
      
      /*======================================================================
       * 여기서 부터는 convertHostKorean() 메소드의
       * 테스트 코드가 있습니다.
       ======================================================================*/
      
      String testText = "한국 외환은행";
      byte [] testByte = convertHostKorean(testText, 15);
      
      System.out.println("convertHostKorean() : [" + new String(testByte) + "]");
        
    }//end of main()
    
}// end of ConvertUtil.java