/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.management.socket;

import java.io.Serializable;

/*******************************************************************
 * <pre>
 * 1.설명 
 * SocketManagementAdapter에서 명령어 및 송수신객체의 VO 역할을 하는 클래스
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
 * $Log: ManagementContext.java,v $
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
public class ManagementContext implements Serializable {
    
    private static final long serialVersionUID = 2792708908586588663L;

    private String wasConfigId;
    
    private String command;

    private Object dataObject;
    
    private Exception exception;

    /**
     * @param string
     * @param map
     */
    public ManagementContext(String wasConfigId, String command, Object dataObject) {
        this.wasConfigId = wasConfigId;
        this.command = command;
        this.dataObject = dataObject;
    }

    /**
     * @return Returns the wasConfigId.
     */
    public String getWasConfigId() {
        return wasConfigId;
    }

    /**
     * @param wasConfigId The wasConfigId to set.
     */
    public void setWasConfigId(String wasConfigId) {
        this.wasConfigId = wasConfigId;
    }

    /**
     * @return Returns the command.
     */
    public String getCommand() {
        return command;
    }

    /**
     * @param command The command to set.
     */
    public void setCommand(String command) {
        this.command = command;
    }

    /**
     * @return Returns the dataObject.
     */
    public Object getDataObject() {
        return dataObject;
    }

    /**
     * @param dataObject The dataObject to set.
     */
    public void setDataObject(Object dataObject) {
        this.dataObject = dataObject;
    }

    /**
     * @return Returns the exception.
     */
    public Exception getException() {
        return exception;
    }

    /**
     * @param exception The exception to set.
     */
    public void setException(Exception exception) {
        this.exception = exception;
    }
    
    public String toString() {
        return "[wasConfigId=" + wasConfigId 
                + ",command=" + command 
                + ",dataObject=" + dataObject 
                + ",exception=" + exception
                + "]";
    }
}
