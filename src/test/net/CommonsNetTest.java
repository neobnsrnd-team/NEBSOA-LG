package test.net;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

public class CommonsNetTest {
	
	public static void main(String[] args) {
		download();
//		upload();
	}
	
	public static void download() {
		FTPClient ftp = null;
	    try {
	        ftp = new FTPClient();
	        ftp.setControlEncoding("UTF-8");
	        
	        ftp.setDefaultPort(2000);
	        ftp.connect("172.16.1.172");
	        ftp.login("root", "root");
//	        ftp.changeWorkingDirectory("configuration");
	        
	        FTPFile[] files = ftp.listFiles();

	        for (int i = 0; files.length > 0; i++) {
	        	files[i].getName();
	            System.out.println(files[i].getName());
	        }
	
	        File f = new File("C:\\", "config.ini");
	        FileOutputStream fos = null;
	        try {
	            fos = new FileOutputStream(f);
	            boolean isSuccess = ftp.retrieveFile("config.ini", fos);
	            if (isSuccess) {
	                // 다운로드 성공
	            	System.out.println("SUCCESS");
	            } else {
	                // 다운로드 실패
	            	System.out.println("FAIL");
	            }
	        } catch(IOException ex) {
	            System.out.println(ex.getMessage());
	        } finally {
	            if (fos != null) try { fos.close(); } catch(IOException ex) {}
	        }
	        ftp.logout();
	    } catch (SocketException e) {
	        System.out.println("Socket:"+e.getMessage());
	    } catch (IOException e) {
	        System.out.println("IO:"+e.getMessage());
	    } finally {
	        if (ftp != null && ftp.isConnected()) {
	            try { ftp.disconnect(); } catch (IOException e) {}
	        }
	    }
	}
	
	public static void upload() {
		FTPClient ftp = null;
	    try {
	        ftp = new FTPClient();
	        ftp.setControlEncoding("UTF-8");
	        
	        ftp.setDefaultPort(2000);
	        ftp.connect("172.16.1.172");
	        ftp.login("root", "root");
//	        ftp.changeWorkingDirectory("/configuration");
	        
	        File uploadFile = new File("C:\\", "config2.ini");
	        FileInputStream fis = null;
	        try {
	            fis = new FileInputStream(uploadFile);
	            boolean isSuccess = ftp.storeFile(uploadFile.getName(), fis);
	            if (isSuccess) {
	                System.out.println("업로드 성공");
	            }
	        } catch(IOException ex) {
	            System.out.println(ex.getMessage());
	        } finally {
	            if (fis != null) try { fis.close(); } catch(IOException ex) {}
	        }
	        ftp.logout();
	    } catch (SocketException e) {
	        System.out.println("Socket:"+e.getMessage());
	    } catch (IOException e) {
	        System.out.println("IO:"+e.getMessage());
	    } finally {
	        if (ftp != null && ftp.isConnected()) {
	            try { ftp.disconnect(); } catch (IOException e) {}
	        }
	    }

	}
}
