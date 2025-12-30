/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.acl;

import java.io.Serializable;

import nebsoa.common.log.LogManager;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 사용자에 대한 권한 ID 및 권한 NAME을 저장하고 있는 클래스입니다. 
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
 * $Log: Role.java,v $
 * Revision 1.1  2018/01/15 03:39:51  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:05  cvs
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
 * Revision 1.1  2008/08/04 08:54:51  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:23  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:39  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2007/01/31 13:26:40  이종원
 * 최초작성
 *
 * </pre>
 ******************************************************************/
public class Role implements Serializable {
    public String roleId;
    public String roleName;
    
    /**
     * @return Returns the roleId.
     */
    public String getRoleId() {
        return roleId;
    }
    /**
     * @param roleId The roleId to set.
     */
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
    /**
     * @return Returns the roleName.
     */
    public String getRoleName() {
        return roleName;
    }
    /**
     * @param roleName The roleName to set.
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    
    public boolean equals(Object obj){
        if(obj==null) return false;
        if(!(obj instanceof Role)) return false;
        if(this.roleId==null) return false;
        Role compare = (Role)obj;
        return this.roleId.equals(compare.getRoleId());
    }
    
    public boolean inRole(Role[] roles){
        if(roles==null) {
            LogManager.debug("Role[] is null...");
            return false;
        }
        for(int i=0;i<roles.length;i++){
            if(this.equals(roles[i])) return true;
        }
        return false;
    }
}
