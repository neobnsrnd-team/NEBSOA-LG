/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
 package nebsoa.util.lockpool;

/*******************************************************************
 * <pre>
 * 1.설명 
 * WORKSPACE 정보를 로딩하여 담고 있는 클래스
 * 
 * 2.사용법
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author $Author: cvs $
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 
 * $Log: WorkSpace.java,v $
 * Revision 1.1  2018/01/15 03:39:50  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:51  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:24  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:27  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:52  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:25  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2008/01/02 09:40:21  김성균
 * 최초 등록
 *
 * </pre>
 ******************************************************************/
public class WorkSpace {
    
    private String workSpaceId; 
    private String workSpaceName; 
    private int    threadCount; 
    
    public WorkSpace() {
    }
    
    /**
     * @return Returns the workSpaceId.
     */
    public String getWorkSpaceId() {
        return workSpaceId;
    }

    /**
     * @param workSpaceId The workSpaceId to set.
     */
    public void setWorkSpaceId(String workSpaceId) {
        this.workSpaceId = workSpaceId;
    }

    /**
     * @return Returns the workSpaceName.
     */
    public String getWorkSpaceName() {
        return workSpaceName;
    }

    /**
     * @param workSpaceName The workSpaceName to set.
     */
    public void setWorkSpaceName(String workSpaceName) {
        this.workSpaceName = workSpaceName;
    }
    
    /**
     * @return Returns the threadCount.
     */
    public int getThreadCount() {
        return threadCount;
    }

    /**
     * @param threadCount The threadCount to set.
     */
    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString(){
        return ">>> workSpaceId:"+workSpaceId
                +"\n workSpaceName:"+ workSpaceName
                +"\n threadCount:"+ threadCount
                ;
    }


}
