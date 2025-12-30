/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.io;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/*******************************************************************
 * <pre>
 * 1.설명 
 * byte [] 을 기반으로 데이터를 읽을 수 있도록 해주는 InputStream 클래스
 * 
 * - 다음과 같은 특징이 있습니다.
 * 
 * 1. java.nio.ByteOrder 를 지원합니다.
 * 		: 기본으로는 ByteOrder.BIG_ENDIAN 으로 동작하며, 설정에 따라서 ByteOrder.LITTLE_ENDIAN 으로도 동작합니다.
 * 2. primitive-data-type 에 대한 read 가 가능 합니다.
 * 		: java.io.DataInputStream 과 동일하게 동작하는 메소드들을 제공합니다.
 * 3. 자주 사용되는 유용한 형태의 유틸리티 메소드들을 제공합니다.
 * 		: 특정 delimiter 까지 읽을 수 있는 메소드를 제공합니다.
 * - 본 클래스는 아래 나열된 3가지의 클래스를 기반으로 생성되었습니다.
 * 		: java.io.ByteArrayInputStream - byte []  활용 및 java.io.InputStream 에서 정의된 기본 메소드에 대한 구현
 * 		: java.io.DataInputStream - primitive-data-type 에 대한 read 부분의 구헌
 * 		: org.apache.commons.io.EndianUtils - BIG-ENDIAN <-> LITTLE-ENDIAN 간의 swap 에 대한 구현
 * 
 * 2.사용법
 * 
 * <font color="red">
 * 3.주의사항
 * 객체 생성 시에 제공된 원본 byte []  의 데이터가 변경되면 동일하게 적용됩니다.
 * </font>
 *
 * @author $Author: cvs $
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 
 * $Log: ByteArrayDataInputStream.java,v $
 * Revision 1.1  2018/01/15 03:39:54  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:40  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:52  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:27  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:29  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:36  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:39:07  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2006/06/19 04:10:16  안경아
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class ByteArrayDataInputStream extends InputStream implements DataInput {
	
	/**
	 * 사용될 byte-order 를 나타냅니다.
	 */
	private ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
	
	/**
	 * BIG_ENDIAN 여부
	 */
	private boolean bigEndian = true;
	
	/**
	 * 현재 위치
	 */
	private int position;
	/**
	 * 원본 데이터
	 */
	private byte [] data;
	
	/**
	 * 최종 mark 위치
	 */
	private int mark;
	
	/**
	 * 데이터 사이즈
	 */
	private int count;

	/**
	 * 주어진 데이터를 사용하여 객체를 생성합니다.
	 * 
	 * @param data stream 내에서 사용될 데이터를 포함하는 byte [] 객체
	 */
	public ByteArrayDataInputStream(byte [] data) {
		this.data = data;
		this.position = 0;
		this.count = data.length;
	}//end of constructor
	
	/**
	 * 주어진 데이터를 사용하여 객체를 생성합니다.
	 * 데이터를 읽을 경우, 주어진 ByteOrder 를 사용하게 됩니다.
	 * 
	 * @param data stream 내에서 사용될 데이터를 포함하는 byte [] 객체
	 * @param byteOrder byte-order 를 나타내는 java.nio.ByteOrder 객체
	 */
	public ByteArrayDataInputStream(byte [] data, ByteOrder byteOrder) {
		this(data);
		this.byteOrder = byteOrder;
		this.bigEndian = ByteOrder.BIG_ENDIAN == byteOrder ? true : false;
	}//end of constructor	

	/**
	 * 읽기 가능한 바이트 수를 리턴합니다.
	 * 
	 * @return 읽기 가능한 바이트 수
	 * @throws IOException 읽기 가능한 바이트 수를 가져오는 중 발생하는 Exception
	 * @see java.io.InputStream#available()
	 */
	public int available() throws IOException {
		return this.count - this.position;
	}//end of available()

	/**
	 * 지정된 위치로 position 을 이동합니다.
	 * 만약 주어진 위치가 byte []  의 크기보다 클 경우,
	 * position 을 byte [] 의 맨 마지막으로 이동합니다.
	 * 
	 * @param readlimit
	 * @see java.io.InputStream#mark(int)
	 */
	public void mark(int readlimit) {
		this.mark = this.position;
	}//end of mark()

	/**
	 * mark 지원여부를 리턴합니다.
	 * 
	 * @return mark 지원여부
	 * @see java.io.InputStream#markSupported()
	 */
	public boolean markSupported() {
		return true;
	}//end of markSupported()
	
	/**
	 * 1byte 데이터를 읽어서 리턴합니다.
	 * 
	 * @return 읽은 데이터
	 * @throws IOException 읽는 중 발생하는 Exception
	 * @see java.io.InputStream#read()
	 */
	public synchronized int read() throws IOException {
		return (this.position < this.count) ? (this.data[this.position++] & 0xff) : -1;
	}//end of read()
	
	/**
	 * 주어진 byte [] 에 데이터를 담습니다.
	 * 
	 * @param b 데이터를 담기 위한 byte [] 객체
	 * @param off 데이터를 담기 시작 할 offset
	 * @param len 데이터 size
	 * @return 담긴 데이터 size
	 * @see java.io.InputStream#read(byte[], int, int)
	 */
    public synchronized int read(byte [] b, int off, int len) {
    	if (b == null) {
			throw new NullPointerException();
		} else if ((off < 0) || (off > b.length) || (len < 0) || ((off + len) > b.length) || ((off + len) < 0)) {
			throw new IndexOutOfBoundsException();
		}//end if else
		if (this.position >= this.count) {
			return -1;
		}//end if
		if (this.position + len > count) {
			len = this.count - this.position;
		}//end if
		if (len <= 0) {
			return 0;
		}//end if
		System.arraycopy(this.data, this.position, b, off, len);
		this.position += len;
		return len;
	}//end of read()

	/**
	 * 현재 position 을 최종 mark() 메소드가 호출된 시점으로 초기화 합니다. mark() 메소드가 한번도 호출되지 않았을
	 * 경우에는 position 을 0 으로 변경합니다.
	 * 
	 * @throws IOException
	 *             reset 하는 중 발생하는 Exception
	 * @see java.io.InputStream#reset()
	 */
	public synchronized void reset() throws IOException {
		this.position = this.mark;
	}//end of reset()

	/**
	 * 주어진 크기만큼 skip 합니다.
	 * 
	 * @param n skip 하고자 하는 크기
	 * @return 실제 skip 된 크기
	 * @throws IOException skip 하는 중 발생하는 Exception
	 * @see java.io.InputStream#skip(long)
	 */
	public synchronized long skip(long n) throws IOException {
		if (this.position + n > this.count) {
		    n = this.count - this.position;
		}//end if
		if (n < 0) {
		    return 0;
		}//end if
		this.position += n;
		return n;
	}//end of skip()
	
	/**
	 * byte-order 를 리턴합니다.
	 * 
	 * @return byte-order 를 나타내는 ByteOrder 객체
	 */
	public ByteOrder order() {
		return this.byteOrder;
	}//end of order()
	
	/**
	 * 주어진 크기만큼 skip 합니다.
	 * 
	 * @param n skip 하고자 하는 크기
	 * @return 실제 skip 된 크기
	 * @throws IOException skip 하는 중 발생하는 Exception
	 * @see java.io.DataInput#skipBytes(int)
	 */
	public synchronized int skipBytes(int n) throws IOException {
		int total = 0;
		int cur = 0;
		while ((total<n) && ((cur = (int) skip(n-total)) > 0)) {
		    total += cur;
		}//end while
		return total;
	}//end of skipBytes()

	/**
	 * 주어진 byte [] 에 데이터를 읽어서 채웁니다.
	 * 
	 * @param b 데이터를 채울 byte [] 객체
	 * @throws IOException 데이터를 읽는 중 발생하는 Exception
	 * @see java.io.DataInput#readFully(byte[])
	 */
	public synchronized void readFully(byte [] b) throws IOException {
		readFully(b, 0, b.length);
	}//end of readFully()

	/**
	 * 주어진 byte [] 에 데이터를 읽어서 채웁니다.
	 * 
	 * @param b 데이터를 채울 byte [] 객체
	 * @param off 읽어서 채우기 시작 할 offset
	 * @param len 읽을 데이터의 크기
	 * @throws IOException 데이터를 읽는 중 발생하는 Exception
	 * @see java.io.DataInput#readFully(byte[], int, int)
	 */
	public synchronized void readFully(byte [] b, int off, int len) throws IOException {
		if (len < 0)
		    throw new IndexOutOfBoundsException();
		int n = 0;
		while (n < len) {
		    int count = read(b, off + n, len - n);
		    if (count < 0)
		    	throw new EOFException();
		    n += count;
		}//end whlie
	}//end of readFully()

	/**
     * 개행문자까지 데이터를 읽어서 문자열로 리턴합니다.
     * 
     * @return 개행문자까지 읽어들인 문자열 데이터
     * @throws IOException 데이터를 읽는 중 발생하는 Exception
	 * @see java.io.DataInput#readLine()
	 */
	public synchronized String readLine() throws IOException {
    	byte [] lineBuffer = new byte[128];
    	byte [] buf = new byte[128];

		int room = buf.length;
		int offset = 0;
		int c;

		loop: while (true) {
			switch (c = read()) {
				case -1:
				case '\n':
					break loop;
	
				case '\r':
					int c2 = read();
					if ((c2 != '\n') && (c2 != -1)) {
				    	this.position--;
					}//end if
					break loop;
	
				default:
					if (--room < 0) {
						buf = new byte[offset + 128];
						room = buf.length - offset - 1;
						System.arraycopy(lineBuffer, 0, buf, 0, offset);
						lineBuffer = buf;
					}//end if
					buf[offset++] = (byte) c;
					break;
			}//end switch case
		}//end while
		if ((c == -1) && (offset == 0)) {
			return null;
		}//end if
		return new String(buf, 0, offset);
	}//end of readLine()

	/**
	 * 현재 본 메소드는 지원하지 않습니다.
	 * 
	 * @throws UnsupportedOperationException 구현되지 않았으므로 항상 throws 됨
	 * @see java.io.DataInput#readUTF()
	 */
	public String readUTF() throws IOException {
        throw new UnsupportedOperationException("Operation not supported: readUTF()" );
	}//end of readUTF()
	
	/**
	 * 특정 문자열이 나올 때 까지 읽은 문자열 데이터를 리턴합니다.
	 * 
	 * @param delim 데이터의 끝을 나타내는 문자열
	 * @return 읽어들인 문자열 데이터
	 * @throws IOException 데이터를 읽는 중 발생하는 Exception
	 */
    public synchronized String readToDelim(String delim) throws IOException {
    	byte [] bDelim0 = delim.getBytes();
    	
    	// byte 는 음수가 될 수 있으므로, int 로 변환해서 비교해야 한다.
    	int [] bDelim = new int[bDelim0.length];
    	for (int i = 0; i < bDelim0.length; i++) {
    		bDelim[i] = bDelim0[i] & 0xff;
    	}//end for
    	
    	int bDelimCount = bDelim.length;
    	
    	byte [] lineBuffer = new byte[128];
    	byte [] buf = new byte[128];

		int room = buf.length;
		int offset = 0;
		int c;

		loop : while (true) {
			c = read();
			if (c == -1) {
				break;
			} else if (c == bDelim[0]) {
				if (bDelimCount == 1) {
					break;
				} else {
					// 현재 위치 기억
					mark(0);
					for (int i = 1; i < bDelimCount; i++) {
						int nextC = read();
						if (nextC == bDelim[i]) {
							continue;
						} else {
							// 버퍼에 데이터 넣고서,
							if (--room < 0) {
								buf = new byte[offset + 128];
								room = buf.length - offset - 1;
								System.arraycopy(lineBuffer, 0, buf, 0, offset);
								lineBuffer = buf;
							}//end if
							buf[offset++] = (byte) c;
							
							// 원래 위치로 되돌린다.
							reset();
							
							continue loop;
						}//end if
					}//end for
					break;
				}//end if else
			} else {
				if (--room < 0) {
					buf = new byte[offset + 128];
					room = buf.length - offset - 1;
					System.arraycopy(lineBuffer, 0, buf, 0, offset);
					lineBuffer = buf;
				}//end if
				buf[offset++] = (byte) c;
			}//end if else
		}//end while
		if ((c == -1) && (offset == 0)) {
			return null;
		}//end if
		return new String(buf, 0, offset);
	}//end of readToDelim()
    
    /**
     * 주어진 크기 만큼을 읽어서 문자열을 리턴합니다.
     * 
     * @param size 데이터를 읽을 크기
     * @return 해당 크기 만큼의 문자열
     * @throws IOException 데이터를 읽는 중 발생하는 Exception
     */
    public synchronized String readString(int size) throws IOException {
    	String s = new String(this.data, this.position, size);
    	this.position += size;
    	return s;
    }//end of readString()
	
	
	
	/*----------------------------------
	 * 
	 * primitive-data read 관련 Method
	 * 
	 ----------------------------------*/
	
	/**
	 * 데이터를 읽어서 boolean 값을 리턴합니다.
	 * 
	 * @return boolean 값
	 * @throws IOException 데이터를 읽는 중 발생하는 Exception
	 */
    public final boolean readBoolean() throws IOException {
		int ch = read();
		if (ch < 0) 
			throw new EOFException();
		return (ch != 0);
	}//end of readBoolean()

    /**
     * 데이터를 읽어서 byte 를 리턴합니다.
     * 
     * @return byte 값
     * @throws IOException 데이터를 읽는 중 발생하는 Exception
     */
    public final byte readByte() throws IOException {
		int ch = read();
		if (ch < 0)
			throw new EOFException();
		return (byte) (ch);
	}//end of readByte()
    
    /**
     * 데이터를 읽어서 unsigned-byte 를 리턴합니다.
     * 
     * @return unsigned-byte 값
     * @throws IOException 데이터를 읽는 중 발생하는 Exception
     */
	public final int readUnsignedByte() throws IOException {
		int ch = read();
		if (ch < 0)
			throw new EOFException();
		return ch;
	}//end of readUnsignedByte()

    /**
     * 데이터를 읽어서 short 를 리턴합니다.
     * 
     * @return short 값
     * @throws IOException 데이터를 읽는 중 발생하는 Exception
     */
	public final short readShort() throws IOException {
		return this.bigEndian ? readShortB() : readShortL();
	}//end of readShortB()

    /**
     * 데이터를 읽어서 unsigned-short 를 리턴합니다.
     * 
     * @return unsigned-short 값
     * @throws IOException 데이터를 읽는 중 발생하는 Exception
     */
	public final int readUnsignedShort() throws IOException {
		return this.bigEndian ? readUnsignedShortB() : readUnsignedShortL();
	}//end of readUnsignedShortB()

    /**
     * 데이터를 읽어서 char 를 리턴합니다.
     * 
     * @return char 값
     * @throws IOException 데이터를 읽는 중 발생하는 Exception
     */
	public final char readChar() throws IOException {
		
		/*
		 * 지금 현재는 무조건 2바이트를 읽게 되어 있다. (DataInputStream 기본 구현)
		 * 이것을 데이터에 따라서 1바이트 혹은 2바이트로 읽도록 수정해야 할 것이다.
		 */
		
		int ch1 = read();
		int ch2 = read();
		if ((ch1 | ch2) < 0)
			throw new EOFException();
		return (char) ((ch1 << 8) + (ch2 << 0));
	}//end of readCharB()

    /**
     * 데이터를 읽어서 int 를 리턴합니다.
     * 
     * @return int 값
     * @throws IOException 데이터를 읽는 중 발생하는 Exception
     */
	public final int readInt() throws IOException {
		return this.bigEndian ? readIntB() : readIntL();
	}//end of readIntB()
	
    /**
     * 데이터를 읽어서 unsigned-int 를 리턴합니다.
     * 
     * @return unsigned-int 값
     * @throws IOException 데이터를 읽는 중 발생하는 Exception
     */
	public final long readUnsignedInt() throws IOException {
		return this.bigEndian ? readUnsignedIntB() : readUnsignedIntL();
	}//end of readUnsignedInt()

    /**
     * 데이터를 읽어서 long 을 리턴합니다.
     * 
     * @return long 값
     * @throws IOException 데이터를 읽는 중 발생하는 Exception
     */
	public final long readLong() throws IOException {
		return this.bigEndian ? readLongB() : readLongL();
	}//end of readLongB()

    /**
     * 데이터를 읽어서 float 을 리턴합니다.
     * 
     * @return float 값
     * @throws IOException 데이터를 읽는 중 발생하는 Exception
     */
	public final float readFloat() throws IOException {
		return this.bigEndian ? readFloatB() : readFloatL();
	}//end of readFloat()

    /**
     * 데이터를 읽어서 double 을 리턴합니다.
     * 
     * @return double 값
     * @throws IOException 데이터를 읽는 중 발생하는 Exception
     */
	public final double readDouble() throws IOException {
		return this.bigEndian ? readDoubleB() : readDoubleL();
	}//end of readDouble()
	

	/*----------------------------------
	 * 
	 * BIG_ENDIAN 관련 Method
	 * 
	 ----------------------------------*/
	
	private final short readShortB() throws IOException {
		int ch1 = read();
		int ch2 = read();
		if ((ch1 | ch2) < 0)
			throw new EOFException();
		return (short) ((ch1 << 8) + (ch2 << 0));
	}//end of readShortB()

	private final int readUnsignedShortB() throws IOException {
		int ch1 = read();
		int ch2 = read();
		if ((ch1 | ch2) < 0)
			throw new EOFException();
		return (ch1 << 8) + (ch2 << 0);
	}//end of readUnsignedShortB()

	private final int readIntB() throws IOException {
		int ch1 = read();
		int ch2 = read();
		int ch3 = read();
		int ch4 = read();
		if ((ch1 | ch2 | ch3 | ch4) < 0)
			throw new EOFException();
		return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
	}//end of readIntB()
	
	private final long readUnsignedIntB() throws IOException {
		int ch1 = read();
		int ch2 = read();
		int ch3 = read();
		int ch4 = read();
		if ((ch1 | ch2 | ch3 | ch4) < 0)
			throw new EOFException();
		return (long) ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
	}//end of readUnsignedIntB()

	private final long readLongB() throws IOException {
		return (((long) read() << 56)
				+ ((long) (read() & 255) << 48)
				+ ((long) (read() & 255) << 40)
				+ ((long) (read() & 255) << 32)
				+ ((long) (read() & 255) << 24)
				+ ((read() & 255) << 16) + ((read() & 255) << 8) + ((read() & 255) << 0));
	}//end of readLongB()

	private final float readFloatB() throws IOException {
		return Float.intBitsToFloat(readIntB());
	}//end of readFloatB()

	private final double readDoubleB() throws IOException {
		return Double.longBitsToDouble(readLongB());
	}//end of readDoubleB()
	
	
	/*----------------------------------
	 * 
	 * LITTLE_ENDIAN 관련 Method
	 * 
	 ----------------------------------*/
	
	private final short readShortL() throws IOException {
        return (short) (((read() & 0xff) << 0) + ((read() & 0xff) << 8));
	}// end of readShortL()

	private final int readUnsignedShortL() throws IOException {
        return (int) (((read() & 0xff) << 0) + ((read() & 0xff) << 8));
	}// end of readUnsignedShortL()

	private final int readIntL() throws IOException {
        return (int) (((read() & 0xff) << 0) + ((read() & 0xff) << 8) + ((read() & 0xff) << 16) + ((read() & 0xff) << 24));
	}// end of readIntL()
	
	private final long readUnsignedIntL() throws IOException {
        return (long) (((read() & 0xff) << 0) + ((read() & 0xff) << 8) + ((read() & 0xff) << 16) + ((read() & 0xff) << 24));
	}// end of readUnsignedIntL()

	private final long readLongL() throws IOException {
        long low = (long) (((read() & 0xff) << 0) + ((read() & 0xff) << 8) + ((read() & 0xff) << 16) + ((read() & 0xff) << 24));
		long high = (long) (((read() & 0xff) << 0) + ((read() & 0xff) << 8) + ((read() & 0xff) << 16) + ((read() & 0xff) << 24));
		return low + (high << 32);
	}// end of readLongL()

	private final float readFloatL() throws IOException {
		return Float.intBitsToFloat(readIntL());
	}// end of readFloatL()

	private final double readDoubleL() throws IOException {
		return Double.longBitsToDouble(readLongL());
	}//end of readDoubleL()
	
	
	
	
	
	
	
	/*----------------------------------
	 * 
	 * 테스트용 main() Method
	 * 
	 ----------------------------------*/
	
	
	public static void main(String [] args) throws Exception {
		System.out.println("=============== testPositive() start ===============");
		testPositive();
		System.out.println("=============== testPositive() end ===============\n\n");
		System.out.println("=============== testNegative() start ===============");
		testNegative();
		System.out.println("=============== testNegative() end ===============\n\n");
		System.out.println("=============== testReadLine() start ===============");
		testReadLine();
		System.out.println("=============== testReadLine() end ===============\n\n");
		System.out.println("=============== testReadToDelim() start ===============");
		testReadToDelim();
		System.out.println("=============== testReadToDelim() end ===============\n\n");
		System.out.println("=============== testReadString() start ===============");
		testReadString();
		System.out.println("=============== testReadString() end ===============\n\n");
	}//end of main()
	
	private static void testPositive() throws Exception {
		ByteBuffer buffer = ByteBuffer.allocate(15);
		buffer.put((byte)1);
		buffer.putShort((short)1600);
		buffer.putInt(75565);
		buffer.putLong(999999999999999l);
		
		byte [] data = buffer.array();
		
		System.out.println("=============== ByteArrayDataInputStream ===============");
		ByteArrayDataInputStream in = new ByteArrayDataInputStream(data);
		System.out.println(in.readByte());
		System.out.println(in.readShort());
		System.out.println(in.readInt());
		System.out.println((long)in.readLong());
		
		System.out.println("=============== DataInputStream ===============");
		DataInputStream din = new DataInputStream(new ByteArrayInputStream(data));
		System.out.println(din.readByte());
		System.out.println(din.readShort());
		System.out.println(din.readInt());
		System.out.println((long)din.readLong());
	}//end of testPositive()
	
	private static void testNegative() throws Exception {
		ByteBuffer buffer = ByteBuffer.allocate(15);
		buffer.put((byte)-1);
		buffer.putShort((short)-1600);
		buffer.putInt(-75565);
		buffer.putLong(-999999999999999l);
		
		byte [] data = buffer.array();
		
		System.out.println("=============== ByteArrayDataInputStream ===============");
		ByteArrayDataInputStream in = new ByteArrayDataInputStream(data);
		System.out.println(in.readByte());
		System.out.println(in.readShort());
		System.out.println(in.readInt());
		System.out.println((long)in.readLong());
		
		System.out.println("=============== DataInputStream ===============");
		DataInputStream din = new DataInputStream(new ByteArrayInputStream(data));
		System.out.println(din.readByte());
		System.out.println(din.readShort());
		System.out.println(din.readInt());
		System.out.println((long)din.readLong());
	}//end of testNegative()
	
	private static void testReadLine() throws Exception {
		String test = "저는 \r\n조만희 입니다. \r\n왕입니\r\n다요~~";
		
		ByteArrayDataInputStream in = new ByteArrayDataInputStream(test.getBytes());
		
		System.out.println(in.readLine());
		System.out.println(in.readLine());
		System.out.println(in.readLine());
		System.out.println(in.readLine());
	}//end of testReadToDelim()
	
	private static void testReadToDelim() throws Exception {
		String test = "저는 \r\n조만희 입니다. \r\n왕입니\r\n다요~~";
		
		ByteArrayDataInputStream in = new ByteArrayDataInputStream(test.getBytes());
		
		System.out.println(in.readToDelim("는 "));
		System.out.println(in.readToDelim(". "));
		System.out.println(in.readToDelim("다요"));
	}//end of testReadToDelim()
	
	private static void testReadString() throws Exception {
		String test = "저는 조만희 입니다. 왕입니다요~~";
		
		ByteArrayDataInputStream in = new ByteArrayDataInputStream(test.getBytes());
		
		System.out.println(in.readString(4));
		System.out.println(in.readString(3));
		System.out.println(in.readString(5));
		System.out.println(in.readString(8));
		System.out.println(in.readString(12));
	}//end of testReadString()
	
}// end of ByteArrayDataInputStream.java