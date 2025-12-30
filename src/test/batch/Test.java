package test.batch;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Test {
	
	public static void main(String[] args) throws Throwable{
		String ip = "172.17.14.130";
		int port =27103 ;
		Socket s = null;
		
		DataOutputStream dos = null;
		DataInputStream dis = null;
		try{
			System.out.println("socket 연결을 시도합니다.");
			
			s = new Socket(ip, port);
			System.out.println("socket 생성");
			
			dos = new DataOutputStream(s.getOutputStream());
			System.out.println("DataOutputStream 생성");
			
			dis = new DataInputStream(s.getInputStream());
			System.out.println("DataInputStream 생성");
			
			byte[] msg = "ddddddddddddddddddddd".getBytes();
			dos.write(msg);
			
		}catch(Throwable th){
			
		}finally{
			if(dos!=null) {
				dos.close();
			}
			
			if(dis!=null) {
				dis.close();
			}
			if(s!=null){
				s.close();
			}
			
		}
		
		
		
		
		
	}
}
