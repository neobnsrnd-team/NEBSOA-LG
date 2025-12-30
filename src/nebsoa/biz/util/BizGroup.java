/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
 package nebsoa.biz.util;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 업무분류 정보를 로딩하여 담고 있는 클래스
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
 * $Log: BizGroup.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:28  cvs
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
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:33  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2008/01/02 09:39:22  김성균
 * 최초 등록
 *
 * </pre>
 ******************************************************************/
public class BizGroup {
    
    private String bizGroupId; 
    private String bizGroupName; 
    private String bizGroupDesc; 
    private String defaultWorkSpaceId; 
     
    /**
     * @return Returns the bizGroupId.
     */
    public String getBizGroupId() {
        return bizGroupId;
    }

    /**
     * @param bizGroupId The bizGroupId to set.
     */
    public void setBizGroupId(String bizGroupId) {
        this.bizGroupId = bizGroupId;
    }

    /**
     * @return Returns the bizGroupName.
     */
    public String getBizGroupName() {
        return bizGroupName;
    }

    /**
     * @param bizGroupName The bizGroupName to set.
     */
    public void setBizGroupName(String bizGroupName) {
        this.bizGroupName = bizGroupName;
    }

    /**
     * @return Returns the bizGroupDesc.
     */
    public String getBizGroupDesc() {
        return bizGroupDesc;
    }

    /**
     * @param bizGroupDesc The bizGroupDesc to set.
     */
    public void setBizGroupDesc(String bizGroupDesc) {
        this.bizGroupDesc = bizGroupDesc;
    }

    /**
     * @return Returns the defaultWorkSpaceId.
     */
    public String getDefaultWorkSpaceId() {
        return defaultWorkSpaceId;
    }

    /**
     * @param defaultWorkSpaceId The defaultWorkSpaceId to set.
     */
    public void setDefaultWorkSpaceId(String defaultWorkSpaceId) {
        this.defaultWorkSpaceId = defaultWorkSpaceId;
    }

    public BizGroup() {
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString(){
        return ">>> bizGroupId:"+bizGroupId
                +"\n bizGroupName:"+ bizGroupName
                +"\n bizGroupDesc:"+ bizGroupDesc
                +"\n defaultWorkSpaceId:"+ defaultWorkSpaceId
                ;
    }
}
