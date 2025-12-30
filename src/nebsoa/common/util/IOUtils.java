/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.PushbackInputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;
import java.util.Map;
import java.util.Properties;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/*******************************************************************
 * <pre>
 * 1.설명 
 * io utility
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
 * $Log: IOUtils.java,v $
 * Revision 1.1  2018/01/15 03:39:49  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:30  cvs
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
 * Revision 1.1  2007/11/26 08:38:01  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2006/06/17 10:37:45  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class IOUtils {

    static boolean debug = false;

    public static String getStringFromReader(Reader reader) throws IOException {
        BufferedReader bufIn = new BufferedReader(reader);
        StringWriter swOut   = new StringWriter();
        PrintWriter pwOut    = new PrintWriter(swOut);

        String tempLine;

        while((tempLine = bufIn.readLine()) != null) {
            pwOut.println(tempLine);
        }

        pwOut.flush();

        return swOut.toString();
    }
    
    /**
	 * InputStream 의 close() 메소드를 호출하여 자원을 System에 반납한다.
     * 
	 * @param input close할 InputStream 객체
     */
    public static void close(InputStream input) {
		//InputStream close 하기..
		try {
			if (input != null) {
			    input.close();
			}//end if
		} catch (Exception e) {}//ignoread
    }//end of close()
    
    /**
	 * OutputStream 의 close() 메소드를 호출하여 자원을 System에 반납한다.
     * 
	 * @param output close할 OutputStream 객체
     */
    public static void close(OutputStream output) {
		//OutputStream close 하기..
		try {
			if (output != null) {
			    output.close();
			}//end if
		} catch (Exception e) {} //ignoread
    }//end of close()
    
    /**
	 * Reader 의 close() 메소드를 호출하여 자원을 System에 반납한다.
     * 
	 * @param input close할 Reader 객체
     */
    public static void close(Reader input) {
		//InputStream close 하기..
		try {
			if (input != null) {
			    input.close();
			}//end if
		} catch (Exception e) {}//ignoread
    }//end of close()
    
    /**
	 * Writer 의 close() 메소드를 호출하여 자원을 System에 반납한다.
     * 
	 * @param output close할 Writer 객체
     */
    public static void close(Writer output) {
		//OutputStream close 하기..
		try {
			if (output != null) {
			    output.close();
			}//end if
		} catch (Exception e) {} //ignoread
    }//end of close()
    
    /**
	 * Socket 의 close() 메소드를 호출하여 자원을 System에 반납한다.
     * 
	 * @param s close할 Socket 객체
     */
    public static void close(Socket s) {
		//Socket close 하기..
		try {
			if (s != null) {
			    s.close();
			}//end if
		} catch (Exception e) {}//ignoread
    }//end of close()
    
	/**
	 * InputStream, OutputStream의 close() 메소드를 호출하여 자원을 System에 반납한다.
	 * 
	 * @param input close할 InputStream 객체
	 * @param output close할 OutputStream 객체
	 */
	public static void close(InputStream input, OutputStream output) {
		close(input);
		close(output);
	}//end close()
	
	/**
	 * Reader, Writer close() 메소드를 호출하여 자원을 System에 반납한다.
	 * 
	 * @param input close할 Reader 객체
	 * @param output close할 Writer 객체
	 */
	public static void close(Reader input, Writer output) {
		close(input);
		close(output);
	}//end close()
    
	/**
	 * Socket, InputStream, OutputStream의 close() 메소드를 호출하여 자원을 System에 반납한다.
	 * 
	 * @param s close할 Socket 객체
	 * @param input close할 InputStream 객체
	 * @param output close할 OutputStream 객체
	 */
	public static void close(Socket s, InputStream input, OutputStream output) {
		close(input, output);
		close(s);
	}//end close()
	
	/**
	 * Socket, Reader, Writer close() 메소드를 호출하여 자원을 System에 반납한다.
	 * 
	 * @param s close할 Socket 객체
	 * @param input close할 Reader 객체
	 * @param output close할 Writer 객체
	 */
	public static void close(Socket s, Reader input, Writer output) {
		close(input, output);
		close(s);
	}//end close()
	
    /**
	 * HttpURLConnection 의 disconnect() 메소드를 호출하여 자원을 System에 반납한다.
     * 
	 * @param con disconnect 하려는 HttpURLConnection 객체
     */
    public static void close(HttpURLConnection con) {
		//OutputStream close 하기..
		try {
			if (con != null) {
			    con.disconnect();
			}//end if
		} catch (Exception e) {} //ignoread
    }//end of close()
	
	/**
	 * HttpURLConnection, Reader, Writer close() 메소드를 호출하여 자원을 System에 반납한다.
	 * 
	 * @param con disconnect 할 HttpURLConnection 객체
	 * @param input close할 Reader 객체
	 * @param output close할 Writer 객체
	 */
	public static void close(HttpURLConnection con, Reader input, Writer output) {
		close(input, output);
		close(con);
	}//end close()

	/**
	 * HttpURLConnection, Reader, Writer close() 메소드를 호출하여 자원을 System에 반납한다.
	 * 
	 * @param con disconnect 할 HttpURLConnection 객체
	 * @param input close할 Reader 객체
	 * @param output close할 Writer 객체
	 */
	public static void close(HttpURLConnection con, InputStream input, OutputStream output) {
		close(input, output);
		close(con);
	}//end close()

	/**
	 * ServerSocket의 close() 메소드를 호출하여 자원을 System에 반납한다.
	 * 
	 * @param ss close할 ServerSocket 객체
	 */
	public static void close(ServerSocket ss) {
		//ServerSocket close 하기..
		try {
			if (ss != null) {
			    ss.close();
			}//end if
		} catch (Exception e) {}//ignoread
	}//end close()
	
	/**
	 * Channel close() 메소드를 호출하여 자원을 System에 반납한다.
	 * 
	 * @param ss close할 Channel 객체
	 */
	public static void close(Channel channel) {
		//Channel close 하기..
		try {
			if (channel != null) {
				channel.close();
			}//end if
		} catch (Exception e) {}//ignoread
	}//end of close()
	

	/**
	 * 지정된 파일 이름을 가진 파일에 byte [] 데이터를 저장한다.
	 * 
	 * @param data 저장할 데이터
	 * @param fileName 저장할 파일명
	 * @throws IOException 파일에 저장 중 발생하는 Exception
	 */
	public static void writeByteToFile(byte [] data, String fileName) throws IOException {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(new File(fileName));
			fos.write(data);
			fos.flush();
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (fos != null) fos.close();
			} catch (Exception e) {}//end try catch
		}//end try catch finally
	}//end writeByteToFile()

	/**
	 * 지정된 파일 이름을 가진 파일에 문자열 데이터를 저장한다.
	 * 
	 * @param data 저장할 데이터
	 * @param fileName 저장할 파일명
	 * @throws IOException 파일에 저장 중 발생하는 Exception
	 */
	public static void writeStringToFile(String data, String fileName) throws IOException {
		FileWriter fw = null;
		try {
			fw = new FileWriter(new File(fileName));
			fw.write(data);
			fw.flush();
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (fw != null) fw.close();
			} catch (Exception e) {}//end try catch
		}//end try catch finally
	}//end writeStringToFile()

	/**
	 * HttpURLConnection 으로 부터 Http Header 정보를 받아서 Map 으로 리턴한다.
	 * 
	 * @param con Http Header 정보를 받아올 HttpURLConnection 객체
	 * @return Header 정보가 포함된 Map
	 */
	public static Map getHttpHeader(HttpURLConnection con) {
		return  con.getHeaderFields();
	}//end getHttpHeader()

	/**
	 * 주어진 파일이름에 해당하는 Properties 파일을 읽는다.
	 * 
	 * @param fileName Properties 파일 이름
	 * @return 읽어들인 Properties 객체
	 * @throws java.lang.Exception Properties 파일을 읽는 중 발생하는 Exception
	 */
	public static Properties readProperties(String fileName) throws Exception {
		FileInputStream fis = new FileInputStream(new File(fileName));
		Properties prop = new Properties();
		prop.load(fis);
		return prop;
	}//end readProperties()

	/**
	 * 주어진 Properties 객체를 주어진 파일 명으로 저장한다.
	 * 
	 * @param prop 저장할 Properties 객체
	 * @param fileName 저장할 파일 명
	 * @return 파일이 저장되었으면 true, 그렇지 않으면 false
	 * @throws java.lang.Exception Properties 파일을 저장하는 중 발생하는 Exception
	 */
	public static void writeProperties(Properties prop, String fileName, String header) throws Exception {
		File file = new File(fileName);
		if (!file.exists()) {
			file.createNewFile();
		}//end if
		FileOutputStream fos = new FileOutputStream(file);
		String propertiesHeader = new String(header.getBytes(), "EUC-KR");
		prop.store(fos, propertiesHeader);
	}//end writeProperties()
	
	private static final ByteBuffer BUFFER = ByteBuffer.allocateDirect(4096);
	
	/**
	 * 파일 채널을 사용하여 데이터를 읽어서 리턴합니다.
	 * 
	 * @param channel FileChannel 객체
	 * @return 파일로부터 읽은 데이터
	 * @throws IOException 파일로부터 데이터를 읽는 중 발생하는 Exception
	 */
	public static byte [] readData(FileChannel channel) throws IOException {
		BUFFER.clear();
		
		int size = channel.read(BUFFER);
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		try {
			while (size != -1) {
				BUFFER.rewind();
				byte [] temp = new byte[BUFFER.remaining()];
				BUFFER.get(temp);
				out.write(temp);
				BUFFER.flip();	
				size = channel.read(BUFFER);
			}//end whlie			
			out.flush();			
			return out.toByteArray();
		} finally {
			close(out);
		}//end try finally

	}//end of readData()
	
	/**
	 * 데이터를 읽는 부분에 있어서 공통적인 부분.
	 * 데이터를 읽어들여서 읽어들인 byte [] 을 반환한다.
	 * 
	 * @param input 데이터를 읽어들이기 위한 InputStream 타입의 객체
	 * @return 읽어들인 byte [] 형태의 데이터
	 * @throws IOException 읽기 도중 발생하는 Exception
	 */
	public static byte [] readData(InputStream input) throws IOException {
		return readData(input, 4096, 10240);
	}//end of readData()
    
	/**
	 * 데이터를 읽는 부분에 있어서 공통적인 부분.
	 * 데이터를 읽어들여서 읽어들인 byte [] 을 반환한다.
	 * 
	 * @param input 데이터를 읽어들이기 위한 InputStream 타입의 객체
	 * @param bufferSize 데이터를 읽어들이기 위한 버퍼 사이즈
	 * @param tempBufferSize 데이터를 임시로 저장하기 위한 버퍼 사이즈
	 * @return 읽어들인 byte [] 형태의 데이터
	 * @throws IOException 읽기 도중 발생하는 Exception
	 */
	public static byte [] readData(InputStream input, int bufferSize, int tempBufferSize) throws IOException {
		//읽기 위해서 사용되는 버퍼
		byte [] buffer = new byte[bufferSize];

		//버퍼를 통해서 받아온 데이터를 임시적으로 담아 놓는 곳
		byte [] tReceiveData = new byte[tempBufferSize];
		
		try {
		    
			//버퍼에 받아온 데이터의 사이즈
			int rSize = input.read(buffer);
			//현재까지 받은 데이터의 크기를 나타내는 인덱스
			int markIdx = 0;

			while (rSize != -1) {
				if ((markIdx + rSize) <= tReceiveData.length - 1) {
					System.arraycopy(buffer, 0, tReceiveData, markIdx, rSize);
				} else {
					//임시 버퍼에 안들어 가므로, 임시 버퍼를 담아 놓을 임시 버퍼가 필요하다.
					byte [] temp = tReceiveData;
					//읽어온 데이터를 수용 가능하도록 임시 버퍼의 사이즈를 늘린다.
					tReceiveData = new byte[tReceiveData.length + (tReceiveData.length / 2)];
					//사이즈를 늘렸으므로, 이전의 데이터를 먼저 카피하고,
					System.arraycopy(temp, 0, tReceiveData, 0, markIdx);
					//새로 받아온 데이터도 카피해 넣는다...
					System.arraycopy(buffer, 0, tReceiveData, markIdx, rSize);
				}//end if else
				//받은 데이터의 사이즈만큼 인덱스를 늘린다.
				markIdx += rSize;
				
				if (rSize < bufferSize) {
					break;
				}//end if
				
				//다시 데이터를 받는다...
				rSize = input.read(buffer);
			}//end while

			//최종적으로 리턴할 데이터를 만든다.
			byte [] returnData = new byte[markIdx];
			System.arraycopy(tReceiveData, 0, returnData, 0, markIdx);
			
			return returnData;
		} catch (IOException e) {
		    throw e;
		}//end try catch finally		
	}//end of readData()
	
	/**
	 * 데이터를 읽는 부분에 있어서 공통적인 부분.
	 * 데이터를 읽어들여서 읽어들인 문자열을 반환한다.
	 * 
	 * @param input 데이터를 읽어들이기 위한 Reader 타입의 객체
	 * @return 읽은 문자열
	 * @throws IOException 읽기 도중 발생하는 Exception
	 */
	public static String readData(Reader input) throws IOException {
		BufferedReader br = null;
		StringWriter sw = null;
		PrintWriter out = null;
		
		try {
		    br = new BufferedReader(input);
		    sw = new StringWriter();
		    out = new PrintWriter(sw);
		    String data;
			while ((data = br.readLine()) != null) {
				out.println(data);
			}//end while
			out.flush();
			
			return sw.toString();
		} catch (IOException e) {
		    throw e;
		} finally {
		    close(br);
		    close(out);
		    close(sw);
		}//end try catch finally
	}//end of readData()
	
	/**
	 * 주어진 파일에 주어진 데이터를 write 합니다.
	 * 
	 * @param fileName 파일 명
	 * @param data 문자열 형태의 데이터
	 * @throws IOException write() 하는 중 발생하는 Exception
	 */
	public static void writeData(String fileName, String data) throws IOException {
		BufferedWriter bw = null;
		
		try {
			bw = new BufferedWriter(new FileWriter(fileName));
			bw.write(data);			
		} catch (IOException e) {
		    throw e;
		} finally {
		    close(bw);
		}//end try catch finally
	}//end of writeData()
	
	/**
	 * 주어진 파일에 주어진 데이터를 write 합니다.
	 * 
	 * @param fileName 파일 명
	 * @param data byte [] 형태의 데이터
	 * @throws IOException write() 하는 중 발생하는 Exception
	 */
	public static void writeData(String fileName, byte [] data) throws IOException {
		BufferedOutputStream bo = null;
		
		try {
			bo = new BufferedOutputStream(new FileOutputStream(fileName));
			bo.write(data);			
		} catch (IOException e) {
		    throw e;
		} finally {
		    close(bo);
		}//end try catch finally
	}//end of writeData()
	
	/**
	 * 주어진 InputStream 을 사용하여 특정 문자가 나올 때 까지 읽어서
	 * 읽은 문자열 데이터를 리턴합니다.
	 * 
	 * @param in 데이터를 읽어들일 InputStream 객체
	 * @param etxChar 데이터의 끝을 나타내는 단위 문자
	 * @return InputStream 으로부터 읽어들인 문자열 데이터
	 * @throws IOException 데이터를 읽는 중 발생하는 Exception
	 */
    public static String readToEtxChar(InputStream in, char etxChar) throws IOException {
    	byte [] lineBuffer = new byte[128];
    	byte [] buf = new byte[128];

		int room = buf.length;
		int offset = 0;
		int c;

		loop: while (true) {
			c = in.read();
			
			if (c == -1 || c == etxChar) {
				break loop;
			} else {
				if (--room < 0) {
					buf = new byte[offset + 128];
					room = buf.length - offset - 1;
					System.arraycopy(lineBuffer, 0, buf, 0, offset);
					lineBuffer = buf;
				}//end if
				buf[offset++] = (byte) c;
			}//end if else
		}//end while : loop
		
		if ((c == -1) && (offset == 0)) {
			return null;
		}//end if
		
		return new String(buf, 0, offset);
	}//end of readToEtxChar()
    
    
	/**
	 * 주어진 byte [] 을 사용하여 특정 문자가 나올 때 까지 읽어서
	 * 읽은 문자열 데이터를 리턴합니다.
	 * 
	 * @param datas 데이터를 읽어들일 InputStream 객체
	 * @param etxChar 데이터의 끝을 나타내는 단위 문자
	 * @return InputStream 으로부터 읽어들인 문자열 데이터
	 * @throws IOException 데이터를 읽는 중 발생하는 Exception
	 */
    public static String readToEtxChar(byte [] datas, char etxChar, int index) throws IOException {
    	byte [] lineBuffer = new byte[128];
    	byte [] buf = new byte[128];

		int room = buf.length;
		int offset = 0;

		for (int i = index; i < datas.length; i++) {
			byte b = datas[i];
			
			if (b == etxChar) {
				break;
			} else {
				if (--room < 0) {
					buf = new byte[offset + 128];
					room = buf.length - offset - 1;
					System.arraycopy(lineBuffer, 0, buf, 0, offset);
					lineBuffer = buf;
				}//end if
				buf[offset++] = b;
			}//end if else
		}//end while : loop
		
		if (datas.length <= 0) {
			return null;
		}//end if
		
		return new String(buf, 0, offset);
	}//end of readToEtxChar()
    
    
    /**
     * DataInputStream 에 있는 코드를 copy 하였슴.
     * 
     * @param in 데이터를 읽어들일 InputStream 객체
     * @return InputStream 으로부터 읽어들인 문자열 데이터
     * @throws IOException 데이터를 읽는 중 발생하는 Exception
     */
    public static String readLine(InputStream in) throws IOException {
    	byte [] lineBuffer = new byte[128];
    	byte [] buf = new byte[128];

		int room = buf.length;
		int offset = 0;
		int c;

		loop: while (true) {
			switch (c = in.read()) {
				case -1:
				case '\n':
					break loop;
	
				case '\r':
					int c2 = in.read();
					if ((c2 != '\n') && (c2 != -1)) {
						if (!(in instanceof PushbackInputStream)) {
							in = new PushbackInputStream(in);
						}
						((PushbackInputStream) in).unread(c2);
					}
					break loop;
	
				default:
					if (--room < 0) {
						buf = new byte[offset + 128];
						room = buf.length - offset - 1;
						System.arraycopy(lineBuffer, 0, buf, 0, offset);
						lineBuffer = buf;
					}
					buf[offset++] = (byte) c;
					break;
			}
		}
		if ((c == -1) && (offset == 0)) {
			return null;
		}
		return new String(buf, 0, offset);
	}
    
    /**
     * 주어진 데이터를 압축하여 리턴합니다.
     * 
     * @param src 압축할 데이터
     * @return 압축된 데이터
     */
    public static byte [] compress(byte [] src) {
        // Create the compressor with highest level of compression
        Deflater compressor = new Deflater();
        compressor.setLevel(Deflater.BEST_COMPRESSION);
        
        // Give the compressor the data to compress
        compressor.setInput(src);
        compressor.finish();
        
        // Create an expandable byte array to hold the compressed data.
        // You cannot use an array that's the same size as the orginal because
        // there is no guarantee that the compressed data will be smaller than
        // the uncompressed data.
        ByteArrayOutputStream bos = new ByteArrayOutputStream(src.length);
        
        // Compress the data
        byte[] buf = new byte[1024];
        while (!compressor.finished()) {
            int count = compressor.deflate(buf);
            bos.write(buf, 0, count);
        }//end while
        
        close(bos);
        
        return bos.toByteArray();
    }//end of compress()
    
    /**
     * 주어진 압축된 데이터를 압축을 해제햐아 리턴합니다.
     * 
     * @param compressedData 압축을 해제 할 데이터
     * @return 압축이 해제 된 데이터
     * @throws IOException 압축을 해제하는 중 발생하는 Exception
     */
    public static byte [] decompress(byte [] compressedData) throws IOException {
        // Create the decompressor and give it the data to compress
        Inflater decompressor = new Inflater();
        decompressor.setInput(compressedData);
        
        // Create an expandable byte array to hold the decompressed data
        ByteArrayOutputStream bos = new ByteArrayOutputStream(compressedData.length);
        
        // Decompress the data
        byte[] buf = new byte[1024];
        while (!decompressor.finished()) {
            try {
                int count = decompressor.inflate(buf);
                bos.write(buf, 0, count);
            } catch (DataFormatException e) {
            	throw new IOException(e.getMessage());
            }//end try catch
        }//end while
        
        IOUtils.close(bos);
        
        return bos.toByteArray();
    }//end of decompress()
    
    
    
    
    public static void mian(String [] args) {
    	byte [] test = "1231253463w475waetrdsnts5nydsy5sarv3qvrawcsymrdi,6.8o9t88ercqcqrv  aymmyus43wWVQ3Cq".getBytes();
    	System.out.println(test.length);
    	System.out.println(compress(test).length);
    }//end of main()
    

}
