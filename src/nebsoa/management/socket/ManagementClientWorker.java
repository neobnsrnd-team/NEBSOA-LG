/*
 * Spider Framework
 *
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 *
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.management.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.PropertyManager;

/*******************************************************************
 * <pre>
 * 1.설명
 * ManagementSever 연결과 ManagementContext 송수신을 담당한다.
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
 * $Log: ManagementClientWorker.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:23  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.3  2009/11/05 09:20:39  이정곤
 * *** empty log message ***
 *
 * Revision 1.2  2009/03/09 05:10:20  김보라
 * 프레임웍의 소켓 close 관련 로직 반영
 *
 * Revision 1.1  2008/11/18 11:27:26  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:29  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/10/16 00:08:54  김성균
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class ManagementClientWorker {

    private static ManagementClientWorker instance = new ManagementClientWorker();

    private ManagementClientWorker() {
    }

    public static ManagementClientWorker getInstance() {
        if (instance == null) {
            instance = new ManagementClientWorker();
        }
        return instance;
    }

    public ManagementContext doProcess(ManagementContext mctx) {
    	Socket socket = null;
    	
        try {
	        socket = connect(mctx.getWasConfigId());
	        IOHandler handler = new IOHandler(mctx.getWasConfigId(), socket);
	        handler.write(mctx);
	        mctx = handler.read();
        }
        catch(Exception e) {
    		LogManager.error(e.toString(), e);
        }
        finally {
        	if(socket != null) {
        		try {
        			socket.close();
        		}
        		catch(Exception e) {}
        	}
        }
        return mctx;
    }

    /**
     * @param wasConfig
     * @return
     */
    private Socket connect(String wasConfigId) {
        String host = getServerIp(wasConfigId);
        int port = getServerPort(wasConfigId);

        LogManager.debug("\t  wasConfigId=" + wasConfigId + ",host=" + host + ",port=" + port);

        Socket socket = null;
        try {
        	SocketAddress serverAddr = new InetSocketAddress(host,port);
            socket = new Socket();
            socket.connect(serverAddr, 2000); //2초안에 커넥션 못하면 끈음.
            socket.setKeepAlive(true);
            socket.setTcpNoDelay(true);
            socket.setSoTimeout(60*1000); //1분 기다리게 세팅
        } catch (UnknownHostException e) {
            throw new SysException(wasConfigId + " 서버에 연결을 실패하였습니다.[host=" + host + ",port=" + port + "]");
        } catch (IOException e) {
            throw new SysException(wasConfigId + " 서버에 연결 중 오류가 발생하였습니다.[host=" + host + ",port=" + port + "]");
        }

        LogManager.debug("### " + host + ":" + port + " 서버와 연결되었습니다.");

        return socket;
    }

    /**
     * @return
     */
    private static String getServerIp(String wasConfigId) {
        return PropertyManager.getProperty("was_config", wasConfigId + ".MANAGEMENT_SERVER_IP", "localhost");
    }

    /**
     * @return
     */
    private static int getServerPort(String wasConfigId) {
        return PropertyManager.getIntProperty("was_config", wasConfigId + ".MANAGEMENT_SERVER_PORT", 50005);
    }
}
