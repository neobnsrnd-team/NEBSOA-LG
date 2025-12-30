/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;

import nebsoa.common.log.LogManager;



/*******************************************************************
 * <pre>
 * 1.설명 
 * Ftp로 파일을 전송하는 유틸리티.
 * 
 * 2.사용법
 * 
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author 이종원
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 
 * $Log: FtpUtil.java,v $
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
 * Revision 1.1  2007/11/26 08:38:04  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2006/10/09 05:33:34  이종원
 * *** empty log message ***
 *
 * Revision 1.1  2006/10/09 05:24:59  이종원
 * 최초작성
 *
 * </pre>
 ******************************************************************/
public class FtpUtil {
	
    String ip;
    int port;
    String userId;
    String pw;
    String encoding="UTF-8";
    
    public FtpUtil(String ip,int port,String userId,String pw){
        this.ip = ip;
        this.port = port;
        this.userId = userId;
        this.pw = pw;
    }
    
    public void upload(String localDir, String fileName, 
            String remoteDir) throws Exception {
        FTPClient ftp = null;
        try {
            ftp = new FTPClient();
            ftp.setControlEncoding(encoding);
            
            ftp.setDefaultPort(port);
            ftp.connect(ip);
            ftp.login(userId,pw);

            ftp.changeWorkingDirectory(remoteDir);
            
            
            File uploadFile = new File(localDir,fileName );
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(uploadFile);
                boolean isSuccess = ftp.storeFile(fileName, fis);
                if (isSuccess) {
                    LogManager.info("####### ftp 업로드 성공:"
                            +uploadFile.getAbsolutePath());
                }
            } catch(IOException ex) {
                LogManager.error("ERROR","####### ftp 업로드 실패 "+ex.getMessage());
                ex.printStackTrace();
            } finally {
                if (fis != null) try { fis.close(); } catch(IOException ex) {}
            }
            ftp.logout();
//            System.out.println("^^^^^^^^^^^^^^^^^^^^FTP END");
        } catch (SocketException e) {
            LogManager.info("####### ftp 업로드 실패 "+e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            LogManager.info("####### ftp 업로드 실패 "+e.getMessage());
            e.printStackTrace();
        } finally {
            if (ftp != null && ftp.isConnected()) {
                try { ftp.disconnect(); } catch (IOException e) {}
            }
        }
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        if(encoding != null)
        this.encoding = encoding;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
