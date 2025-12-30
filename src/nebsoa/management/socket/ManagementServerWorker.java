/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.management.socket;

import java.net.Socket;

import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;

/*******************************************************************
 * <pre>
 * 1.설명 
 * ManagementClient 요청을 쓰레드로 수행하기 위한 클래스입니다. 
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
 * $Log: ManagementServerWorker.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:23  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
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
public class ManagementServerWorker extends Thread {
    
    private boolean exit = false;
    
    private Socket socket;

    public ManagementServerWorker(Socket socket) {
    	this.socket = socket;
    }

    public void run() {
        
        LogManager.debug("ManagementServer> ManagementServerWorker 수행됨:" + socket);
        
        IOHandler ioHandler = null; 
		while (!exit) {
			try {
			    ioHandler = new IOHandler("ManagementServer", socket);
                ManagementContext mctx = ioHandler.read();
                mctx = AgentExecutor.getInstance().execute(mctx);
                ioHandler.write(mctx);
                exit = true;
			} catch (SysException e) {
			    LogManager.error("ManagementServer에서 송수신 중 오류입니다.", e);
				exit = true;
			} catch (Exception e) {
			    LogManager.error("ManagementServer에서 처리 중 에러입니다.", e);
				exit = true;
			}
		}
		ioHandler.close();
	}
}

