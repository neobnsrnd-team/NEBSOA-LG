/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
package nebsoa.common.util;

import nebsoa.common.util.ebcdic.Ebc2Ksc1;
import nebsoa.common.util.ebcdic.Ebc2Ksc2;
import nebsoa.common.util.ebcdic.Ebc2Ksc3;
import nebsoa.common.util.ebcdic.Ebc2Ksc4;
import nebsoa.common.util.ebcdic.Ksc2Ebc1;
import nebsoa.common.util.ebcdic.Ksc2Ebc2;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 호스트와의 한글 변환 처리(ebcdic - extended binary coded decimal interchange code)를 
 * 위한 유틸리티 클래스
 * 2.사용법
	<pre>
		byte[] test = "안녕. hello world AAABBB 반갑습니다.".getBytes();

		// EBCDIC로 바꾼다.
		byte[] result = ksc2ebc(test);

		// 다시 한글로 바꾼다.
		byte[] result1 = ebc2ksc(result); *  
   </pre>
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author 이종원
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 
 * $Log: EbcdicUtil.java,v $
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
 * Revision 1.1  2007/12/17 01:20:27  이종원
 * ebcdic 처리를 위한 유틸리티
 *
 * </pre>
 ******************************************************************/
public class EbcdicUtil {

	/** 1 Byte asc2ebc code table */
	static byte ASC2EBC[] = {
		// 0x00 - 0x7f
		(byte)0x00,(byte)0x01,(byte)0x02,(byte)0x03,(byte)0x37,(byte)0x05,(byte)0x2e,(byte)0x2f,(byte)0x16,(byte)0x09,(byte)0x25,(byte)0x0b,(byte)0x0c,(byte)0x0d,(byte)0x0e,(byte)0x0f,	//
		(byte)0x10,(byte)0x11,(byte)0x12,(byte)0x13,(byte)0x3c,(byte)0x3d,(byte)0x32,(byte)0x26,(byte)0x18,(byte)0x19,(byte)0x3f,(byte)0x27,(byte)0x1c,(byte)0x1d,(byte)0x1e,(byte)0x1f,	//
		(byte)0x40,(byte)0x5a,(byte)0x7f,(byte)0x7b,(byte)0x5b,(byte)0x6c,(byte)0x50,(byte)0x7d,(byte)0x4d,(byte)0x5d,(byte)0x5c,(byte)0x4e,(byte)0x6b,(byte)0x60,(byte)0x4b,(byte)0x61,	//  !"#$%&'()*+,-./
		(byte)0xf0,(byte)0xf1,(byte)0xf2,(byte)0xf3,(byte)0xf4,(byte)0xf5,(byte)0xf6,(byte)0xf7,(byte)0xf8,(byte)0xf9,(byte)0x7a,(byte)0x5e,(byte)0x4c,(byte)0x7e,(byte)0x6e,(byte)0x6f,	// 0123456789:;<=>?
		(byte)0x7c,(byte)0xc1,(byte)0xc2,(byte)0xc3,(byte)0xc4,(byte)0xc5,(byte)0xc6,(byte)0xc7,(byte)0xc8,(byte)0xc9,(byte)0xd1,(byte)0xd2,(byte)0xd3,(byte)0xd4,(byte)0xd5,(byte)0xd6,	// @ABCDEFGHIJKLMNO
		(byte)0xd7,(byte)0xd8,(byte)0xd9,(byte)0xe2,(byte)0xe3,(byte)0xe4,(byte)0xe5,(byte)0xe6,(byte)0xe7,(byte)0xe8,(byte)0xe9,(byte)0x70,(byte)0xe0,(byte)0xa0,(byte)0xb0,(byte)0x6d,	// PQRSTUVWXYZ[\]^_
		(byte)0x79,(byte)0x81,(byte)0x82,(byte)0x83,(byte)0x84,(byte)0x85,(byte)0x86,(byte)0x87,(byte)0x88,(byte)0x89,(byte)0x91,(byte)0x92,(byte)0x93,(byte)0x94,(byte)0x95,(byte)0x96,	// `abcdefghijklmno
		(byte)0x97,(byte)0x98,(byte)0x99,(byte)0xa2,(byte)0xa3,(byte)0xa4,(byte)0xa5,(byte)0xa6,(byte)0xa7,(byte)0xa8,(byte)0xa9,(byte)0xc0,(byte)0x6a,(byte)0xd0,(byte)0xa1,(byte)0x07,	// pqrstuvwxyz{|}~
	};

	/** 1 Byte ebc2ksc code table */
	static byte EBC2ASC[] = {
		// 0x00 - 0xff
		0x00,0x01,0x02,0x03,0x04,0x05,0x06,0x7f,0x08,0x09,0x0a,0x0b,0x0c,0x0d,0x0e,0x0f,		//        
		0x10,0x11,0x12,0x13,0x14,0x15,0x08,0x17,0x18,0x19,0x1a,0x1b,0x1c,0x1d,0x1e,0x1f,		//
		(byte)0x80,0x21,0x22,0x23,0x24,0x0a,0x17,0x1b,0x28,0x29,0x2a,0x2b,0x2c,0x05,0x06,0x07,	// ?"#$   ()*+,
		0x30,0x31,0x16,0x33,0x34,0x35,0x36,0x04,0x38,0x39,0x3a,0x3b,0x14,0x15,0x3e,0x1a,		// 01 3456 89:;  >
		0x20,0x1a,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x5b,0x2e,0x3c,0x28,0x2b,0x7c,		//           [.<(+|
		0x26,0x1a,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x21,0x24,0x2a,0x29,0x3b,0x5e,		// &         !$*);^
		0x2d,0x2f,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x7c,0x2c,0x25,0x5f,0x3e,0x3f,		// -/        |,%_>?
		0x5b,0x1a,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x60,0x3a,0x23,0x40,0x27,0x3d,0x22,		// [        `:#@'="
		0x5d,0x61,0x62,0x63,0x64,0x65,0x66,0x67,0x68,0x69,0x00,0x00,0x00,0x00,0x00,0x00,		// ]abcdefghi
		0x1a,0x6a,0x6b,0x6c,0x6d,0x6e,0x6f,0x70,0x71,0x72,0x00,0x00,0x00,0x00,0x00,0x00,		//  jklmnopqr
		0x5d,0x7e,0x73,0x74,0x75,0x76,0x77,0x78,0x79,0x7a,0x00,0x00,0x00,0x00,0x00,0x00,		// ]~stuvwxyz
		0x5e,0x1a,0x5c,0x1a,0x1a,0x1a,0x1a,0x1a,0x1a,0x1a,0x00,0x00,0x00,0x1a,0x1a,0x1a,		// ^ \
		0x7b,0x41,0x42,0x43,0x44,0x45,0x46,0x47,0x48,0x49,0x1a,0x1a,0x1a,0x1a,0x1a,0x1a,		// {ABCDEFGHI
		0x7d,0x4a,0x4b,0x4c,0x4d,0x4e,0x4f,0x50,0x51,0x52,0x1a,0x1a,0x1a,0x1a,0x1a,0x1a,		// }JKLMNOPQR
		0x5c,0x1a,0x53,0x54,0x55,0x56,0x57,0x58,0x59,0x5a,0x1a,0x1a,0x1a,0x1a,0x1a,0x1a,		// \ STUVWXYZ
		0x30,0x31,0x32,0x33,0x34,0x35,0x36,0x37,0x38,0x39,0x1a,0x1a,0x1a,0x1a,0x1a,(byte)0xff,	// 0123456789     ?
	};

	/**
	* 객체 생성을 제한하기위해 private로 처리
	 */
	private EbcdicUtil()
	{
	}
	
	/**
    * 
	 EBCDIC 를 KSC로 변환한다.

	 EBCDIC 한글은 0x0e, 0x0f로 구분되어 있어야 한다.

	 input : 0x0e, EBCDIC 한글 코드, 0x0f
	 output : KSC 한글 코드

	 @param byte[] codes 변환할 EBCDIC 데이터
	 @return byte[] KSC로 변환한 데이터
	 */
	public static byte[] ebc2ksc(byte[] codes)
	{
		byte[] 	cvt;
		int		cvtPt;
		int		i;

		cvt = new byte[(codes.length)];
		cvtPt = 0;

		for(i = 0; i < codes.length; i++)
		{
			if( codes[i] == 0x0e ) // 한글 부분 : 2 byte conversion
			{
//				cvt[cvtPt++] = 0x0e; // 0x0e 그대로 변경 유지
				i++;
				while( codes[i] != 0x0f )
				{
					short h = (short)codes[i++];
					h = (short)(h & 0x00ff);
					short l = (short)codes[i++];
					l = (short)(l & 0x00ff);

					if (0x40 <= h && h <= 0xd4 && 0x40 <= l && l <= 0xff) {
						int pt = ((h - 0x40) * 192) + (l - 0x40);
						int table = pt / 8640;
						int t_pt = pt % 8640;

						char	cKsc = 0;

						switch( table )
						{
							case	0:
								cKsc = Ebc2Ksc1.EBC2KSC[t_pt];
								break;
							case	1:
								cKsc = Ebc2Ksc2.EBC2KSC[t_pt];
								break;
							case	2:
								cKsc = Ebc2Ksc3.EBC2KSC[t_pt];
								break;
							case	3:
								cKsc = Ebc2Ksc4.EBC2KSC[t_pt];
								break;
						}

						char temp = (char)(cKsc & 0x00FF);  // Low Bytes
						cvt[cvtPt+1] = (byte)temp;
						cKsc = (char)(cKsc >> 8);	  // High Bytes
						temp = (char)(cKsc & 0x00FF);
						cvt[cvtPt] = (byte)temp;
						cvtPt += 2;
					} else {
						cvt[cvtPt++] = 0x00;
						cvt[cvtPt++] = 0x00;
					}
				}
//				cvt[cvtPt++] = 0x0f;  // 0x0f는 그대로 유지, pointer i는 for loop에서 증가
			}
			else  // 1 byte code conversion
			{
				short pt = (short)codes[i];
				pt = (short)(pt & 0x00ff);
				cvt[cvtPt++] = (byte)EBC2ASC[pt];
			}
		}
		byte[] toReturn = new byte[cvtPt];
		System.arraycopy(cvt, 0, toReturn, 0, cvtPt);
		return toReturn;
	}
	
	public static byte ebc2ksc2(byte codes)
	{
		short pt = (short)codes;
		pt = (short)(pt & 0x00ff);
		return (byte)EBC2ASC[pt];
	}
	
	/**
	 	KSC 한글을 EBCDIC 한글로 변환한다.

	 	EBCDIC 한글은 0x0e로 시작하여 0x0f로 끝난다.

	 	input : 영문자 + 한글
	 	output : 영문자 0x0e 한글 0x0f

		@ param byte[] codes EBCDIC 한글로 변환할 KSC 한글
		@ retrun byte[] EBCDIC로 변환된 한글

	 */
	public static byte[] ksc2ebc( byte[] codes )
	{
		byte[] cvt;
		int		cvtPt;
		int		i;
		boolean	bHan;

		// 최악의 경우 코드가 2배로 늘어난다.
		cvt = new byte[2 * codes.length];
		cvtPt = 0;
		bHan = false;

		for (i = 0; i < codes.length; i++)
		{
			short h = (short)codes[i];
			h = (short)(h & 0x00ff);

			if( h >= 0x80 ) // 한글 부분 : 2 byte conversion
			{
				if (bHan == false)
				{
					bHan = true;
					cvt[cvtPt++] = 0x0e;
				}
				i++;	// 한글일땐 2 번째 Byte 조합 처리
				short l = (short)codes[i];
				l = (short)(l & 0x00ff);

				if (0xa1 <= h && h <= 0xfe && 0xa0 <= l && l <= 0xff) {
					int pt = ((h - 0xa1) * 96) + (l - 0xa0);
					int table = pt / 4800;
					int t_pt = pt % 4800;

					char	cEbc = 0;
					switch( table )
					{
						case	0:
							cEbc = Ksc2Ebc1.KSC2EBC[t_pt];
							break;
						case	1:
							cEbc = Ksc2Ebc2.KSC2EBC[t_pt];
							break;
					}
					char temp = (char)(cEbc & 0x00FF);  // Low Bytes
					cvt[cvtPt+1] = (byte)temp;
					cEbc = (char)(cEbc >> 8);	  // High Bytes
					temp = (char)(cEbc & 0x00FF);
					cvt[cvtPt] = (byte)temp;
					cvtPt += 2;
				}
				else {
					cvt[cvtPt++] = 0x00;
					cvt[cvtPt++] = 0x00;
				}
			}
			else  // 1 byte code conversion
			{
				if (bHan == true)
				{
					bHan = false;
					cvt[cvtPt++] = 0x0f;
				}
				cvt[cvtPt++] = (byte)ASC2EBC[h];
			}
		}
		if (bHan == true)
		{
			bHan = false;
			cvt[cvtPt++] = 0x0f;
		}
		byte[] toReturn = new byte[cvtPt];
		System.arraycopy(cvt, 0, toReturn, 0, cvtPt);
		return toReturn;
	}
	/**
	Test main routine

	*/
	public static void main(String[] args)
	{
		byte[] test = "안녕. hello world AAABBB 반갑습니다.".getBytes();

		// EBCDIC로 바꾼다.
		byte[] result = ksc2ebc(test);

		for (int i = 0; i < result.length; i++)
		{
			String ebcdic = Integer.toString((char)result[i] & 0x00ff, 16).toUpperCase();

			if (ebcdic.length() == 2)
				System.out.print(ebcdic + " ");
			else
				System.out.print("0" + ebcdic + " ");
		}
		System.out.println();
		// 다시 한글로 바꾼다.
		byte[] result1 = ebc2ksc(result);
		String han = new String(result1);
		System.out.println(han);
	}
}
