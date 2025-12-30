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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;

/*******************************************************************
 * <pre>
 * 1.설명
 * ManagementContext를 송수신하기 위한 ObjectStream을 핸들링하는 클래스입니다.
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
 * $Log: IOHandler.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:23  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
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
public class IOHandler {

    private Socket sock = null;
    private String wasId = null;

    private ObjectInputStream ois = null;
    private ObjectOutputStream oos = null;

    boolean isClosed;

    public IOHandler(String wasId, Socket sock) {
        this.wasId = wasId;
    	this.sock = sock;
    	openStream();
    	LogManager.debug(this + "이 서비스 초기화 되었습니다.");
    }

    public void openStream() {
        if (oos == null) {
            oos = openOutputStream();
        }
        if (ois == null) {
            ois = openInputStream();
        }
    }

    public ObjectInputStream openInputStream() {
        if (sock == null || sock.isClosed()) {
            throw new SysException(this + "socket not opened...");
        }
        try {
            ois = new ObjectInputStream(sock.getInputStream());
        } catch (IOException e) {
            LogManager.error(e.toString(), e);
            throw new SysException(this + "FAIL TO OPEN INPUT STREAM");
        }
        return ois;
    }

    public ObjectOutputStream openOutputStream() {
        if (sock == null || sock.isClosed()) {
            throw new SysException(this + "socket not opened...");
        }
        try {
            oos = new ObjectOutputStream(sock.getOutputStream());
        } catch (IOException e) {
            LogManager.error(e.toString(), e);
            throw new SysException(this + "FAIL TO OPEN OUTPUT STREAM");
        }
        return oos;
    }

    /**
     * ManagementContext를 송신한다.
     * @param mctx
     */
    public void write(ManagementContext mctx) {
        if (oos == null) {
            throw new SysException(this + "OUTPUT STREAM이 열려 있지 않습니다.");
        }
        try {
			LogManager.debug(this + ">>>>> 송신:[" + mctx + "]");
			oos.writeObject(mctx);
		} catch (IOException e) {
		    LogManager.error("ManagementAdapter에서 데이터 송신 중 오류:" + e.toString(), e);
		    throw new SysException("ManagementAdapter에서 데이터 송신 중 오류가 발생하였습니다.[ManagementContext=" + mctx + "]");
		}
    }

    /**
     * ManagementContext를 수신한다.
     * @return
     */
    public ManagementContext read() {
        if (ois == null) {
            throw new SysException(this + "INPUT STREAM이 열려 있지 않습니다.");
        }

        ManagementContext mctx = null;

        try {
            mctx = (ManagementContext) ois.readObject();
            LogManager.debug(this + "<<<<< 수신[" + mctx + "]");
        } catch (IOException e) {
            LogManager.error("ManagementAdapter에서 데이터 수신 중 오류:" + e.toString(), e);
            throw new SysException("ManagementAdapter에서 데이터 수신 중 오류가 발생하였습니다.[ManagementContext=" + mctx + "]");
        } catch (ClassNotFoundException e) {
            LogManager.error("ManagementAdapter에서 데이터 수신 중 오류:" + e.toString(), e);
            throw new SysException("ManagementAdapter에서 데이터 수신 중 오류:ClassNotFoundException[ManagementContext=" + mctx + "]");
        }
        return mctx;
    }

    public void close() {
		try {
			if (ois != null) {
				ois.close();
			}
		} catch (Exception e) {
		}

		try {
			if (oos != null) {
				oos.close();
			}
		} catch (Exception e) {
		}

		try {
			if (sock != null) {
				sock.close();
			}
		} catch (Exception e) {
		}
		ois = null;
		oos = null;
		sock = null;
		isClosed = true;

		LogManager.debug(this + "이 서비스 종료 되었습니다.");
	}

	/**
	 * @return Returns the dis.
	 */
	public ObjectInputStream getOis() {
		return ois;
	}

	/**
	 * @param ois The ois to set.
	 */
	public void setDis(ObjectInputStream ois) {
		this.ois = ois;
	}

	/**
	 * @return Returns the oos.
	 */
	public ObjectOutputStream getOos() {
		return oos;
	}

	/**
	 * @param oos The oos to set.
	 */
	public void setOos(ObjectOutputStream oos) {
		this.oos = oos;
	}

	public String toString() {
	    return "[" + wasId +"]";
	}
}


