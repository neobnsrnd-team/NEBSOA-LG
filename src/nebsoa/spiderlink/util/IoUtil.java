/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.Channel;
import java.util.Map;
import java.util.Properties;

import nebsoa.common.util.PropertyManager;

/*******************************************************************
 * <pre>
 * 1.설명 
 * IO 와 관련된 유틸리티들이 포함되어 있는 클래스
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
 * $Log: IoUtil.java,v $
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
 * Revision 1.5  2006/11/17 08:49:44  이종원
 * 전문 길이가 허용 한계치를 넘는지 검사하는 isOverflowData메소드 추가
 *
 * Revision 1.4  2006/11/17 08:43:41  이종원
 * MESSAGE_MAX_LENGTH 추가-전문 길이 제한로직
 *
 * Revision 1.3  2006/11/17 08:33:11  이종원
 * 전문 길이가 허용 한계치를 넘는지 검사하는 isOverflowData메소드 추가
 *
 * Revision 1.2  2006/11/17 08:17:45  이종원
 * skipStream 기능 추가
 *
 * Revision 1.1  2006/06/17 10:22:59  김승희
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public class IoUtil {
	
    /**
     * 객체 생성을 방지하기 위한 private 생성자
     *
     */
    private IoUtil() {        
    }//end of constructor
    
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
	 * Channel 의 close() 메소드를 호출하여 자원을 System에 반납한다.
     * 
	 * @param channel close할 Channel 객체
     */
    public static void close(Channel channel) {
		//InputStream close 하기..
		try {
			if (channel != null) {
				channel.close();
			}//end if
		} catch (Exception e) {}//ignoread
    }//end of close()
    
    /**
	 * Socket 의 close() 메소드를 호출하여 자원을 System에 반납한다.
     * 
	 * @param socket close할 Socket 객체
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
	 * @param socket close할 Socket 객체
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
	 * @param socket close할 Socket 객체
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
	 * 파일로 부터 byte [] 의 형태로 데이터를 읽어서 리턴한다.
	 * 
	 * @param fileName 데이터를 읽어들일 파일 명
	 * @return 읽어들인 byte [] 형태의 데이터
	 * @throws IOException 파일로 부터 데이터를 읽는 도중 발생하는 Exception
	 */
	public static byte [] readByteFromFile(String fileName) throws IOException {
	    try {
	        return readByteFromFile(new File(fileName));
	    } catch (IOException e) {
	        throw e;
	    }//end try catch finally
	}//end readByteFromFile()
	
	/**
	 * 파일로 부터 byte [] 의 형태로 데이터를 읽어서 리턴한다.
	 * 
	 * @param fileName 데이터를 읽어들일 파일 명
	 * @return 읽어들인 byte [] 형태의 데이터
	 * @throws IOException 파일로 부터 데이터를 읽는 도중 발생하는 Exception
	 */
	public static byte [] readByteFromFile(File file) throws IOException {
	    InputStream in = null;
	    try {
	        in = new FileInputStream(file);
	        return readData(in);
	    } catch (IOException e) {
	        throw e;
	    } finally {
	        close(in);
	    }//end try catch finally
	}//end readByteFromFile()
	
	/**
	 * 파일로 부터 String 의 형태로 데이터를 읽어서 리턴한다.
	 * 
	 * @param fileName 데이터를 읽어들일 파일 명
	 * @return 데이터와 데이터의 크기가 세팅된 ReceiveData 객체
	 * @throws IOException 파일로 부터 데이터를 읽는 도중 발생하는 Exception
	 */
	public static String readStringFromFile(String fileName) throws IOException {
	    FileReader fr = null;
	    try {
	        fr = new FileReader(new File(fileName));
	        return readData(fr);
	    } catch (IOException e) {
	        throw e;
	    } finally {
	        close(fr);
	    }//end try catch finally
	}//end readByteFromFile()

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

		BufferedInputStream bis = null;
		
		try {
		    bis = new BufferedInputStream(input);
		    
			//버퍼에 받아온 데이터의 사이즈
			int rSize = bis.read(buffer);
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
				rSize = bis.read(buffer);
			}//end while

			//최종적으로 리턴할 데이터를 만든다.
			byte [] returnData = new byte[markIdx];
			System.arraycopy(tReceiveData, 0, returnData, 0, markIdx);
			
			return returnData;
		} catch (IOException e) {
		    throw e;
		} finally {
		    close(bis);
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
     * 주어진 length만큼 stream data를 skip하기 위한 메소드.
     * @param DataInputStream stream
     * @param length skip할 길이
     * @throws IOException
     */
    public static final void skipStream(InputStream in,  int len) throws IOException {
        byte buf[]=new byte[1024];
        if (len < 0){
            throw new IndexOutOfBoundsException();
        }
        int totalRead = 0;
        int restLen=len;
        while (totalRead < len) {
            int count = in.read(buf, 0 , restLen>1024?1024:restLen);
            if (count < 0)
            throw new EOFException();
            totalRead += count;
            restLen -= count;
            System.out.println("skiplen:"+len+",totalRead:"+totalRead+",rest:"+restLen);
        }
    }
    
    /**
     * 처리대상 데이터가 한계치를 초과 하는지 체크.
     * @param dataLength (처리 대상 대이터 길이)
     * @throws IOException
     */
    public static final boolean isOverflowData(int dataLength){
        return dataLength> PropertyManager.getIntProperty("spiderlink","MESSAGE_MAX_LENGTH",10000000);
    }   
    /**
     * 처리대상 데이터가 한계치를 초과 하는지 체크.
     * @param dataLength문자열 (처리 대상 대이터 길이문자열-String)
     * @throws IOException
     */
    public static final boolean isOverflowData(String dataLength){
        return Integer.parseInt(dataLength)> PropertyManager.getIntProperty("spiderlink","MESSAGE_MAX_LENGTH",10000000);
    }   
    
    public static void main(String[] args) throws IOException{
        File testFile = new File("c:/work/spider/javadoc.xml");
        FileInputStream fin = new FileInputStream(testFile);
        int lenth=(int) testFile.length();
        System.out.println("testFileLen:"+testFile.length());
        skipStream(fin,lenth/2);
        while(fin.available()>0){
            System.out.print((char)fin.read());
        }
        fin.close();
       
        fin = new FileInputStream(testFile);
        skipStream(fin,1000);
        while(fin.available()>0){
            System.out.print((char)fin.read());
        }
        fin.close();
       
    }
}// end of IoUtil.java