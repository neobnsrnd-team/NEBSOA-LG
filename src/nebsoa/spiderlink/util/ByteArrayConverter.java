/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.util;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 바이너리 데이터 변환 유틸리티 클래스
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
 * $Log: ByteArrayConverter.java,v $
 * Revision 1.1  2018/01/15 03:39:52  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:09  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:25  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:28  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:33  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:39:08  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2006/06/17 10:22:59  김승희
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public class ByteArrayConverter {
    
    private ByteArrayConverter() {
    }
    
    public static final byte[] shortToByte(short s) {
        byte[] dest = new byte[2];
        dest[1] = (byte) (s & 0xff);
        dest[0] = (byte) ((s >> 8) & 0xff);
        return dest;
    }
    
    public static final byte[] intToByte(int i) {
        byte[] dest = new byte[4];
        dest[3] = (byte) (i & 0xff);
        dest[2] = (byte) ((i >> 8) & 0xff);
        dest[1] = (byte) ((i >> 16) & 0xff);
        dest[0] = (byte) ((i >> 24) & 0xff);
        return dest;
    }
    
    public static final byte[] longToByte(long l) {
        byte[] dest = new byte[8];
        dest[7] = (byte) (l & 0xff);
        dest[6] = (byte) ((l >> 8) & 0xff);
        dest[5] = (byte) ((l >> 16) & 0xff);
        dest[4] = (byte) ((l >> 24) & 0xff);
        dest[3] = (byte) ((l >> 32) & 0xff);
        dest[2] = (byte) ((l >> 40) & 0xff);
        dest[1] = (byte) ((l >> 48) & 0xff);
        dest[0] = (byte) ((l >> 56) & 0xff);
        return dest;
    }
    
    public static final short getShort(byte[] src, int offset) {
        return (short) (((src[offset] & 0xff) << 8) | (src[offset + 1] & 0xff));
    }
    
    public static final int getInt(byte[] src, int offset) {
        return ((src[offset] & 0xff) << 24)
            | ((src[offset + 1] & 0xff) << 16)
            | ((src[offset + 2] & 0xff) << 8)
            | (src[offset + 3] & 0xff);
    }
    
    public static final long getLong(byte[] src, int offset) {
        return ((long) getInt(src, offset) << 32) | ((long) getInt(src, offset + 4) & 0xffffffffL);
    }
    
    public static final byte[] setShort(byte[] dest, int offset, short s) {
        dest[offset + 1] = (byte) (s & 0xff);
        dest[offset] = (byte) ((s >> 8) & 0xff);
        return dest;
    }
    
    public static final byte[] setInt(byte[] dest, int offset, int i) {
        dest[offset + 3] = (byte) (i & 0xff);
        dest[offset + 2] = (byte) ((i >> 8) & 0xff);
        dest[offset + 1] = (byte) ((i >> 16) & 0xff);
        dest[offset] = (byte) ((i >> 24) & 0xff);
        return dest;
    }
    
    public static final byte[] setLong(byte[] dest, int offset, long l) {
        setInt(dest, offset, (int) (l >> 32));
        setInt(dest, offset + 4, (int) (l & 0xffffffffL));
        return dest;
    }
}
