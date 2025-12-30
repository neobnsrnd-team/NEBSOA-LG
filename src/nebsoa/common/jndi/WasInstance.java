/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
package nebsoa.common.jndi;
/*******************************************************************
 * <pre>
 * 1.설명 
 * WasInstance 테이블의 정보를 로딩하여 담고 있는 클래스
 * 프레임웍 초기화 시에 생성 된다.
 * 주요 용도는 특정 Biz나 SpiderLink연계 엔진이 해당 was에서 수행되고 있는지 정보를 
 * client로하여금 알게하여 해당 service를 reote혹은 local에서 call하게 하기 위한 용도로 쓰인다.
 * 
 * 2.사용법
 * WasInstance 테이블 필드 참조.
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author $Author: cvs $
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 
 * $Log: WasInstance.java,v $
 * Revision 1.1  2018/01/15 03:39:52  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:21  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:26  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:28  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:52  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:25  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:59  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2006/06/20 04:31:06  이종원
 * *** empty log message ***
 *
 * Revision 1.2  2006/06/20 02:51:04  이종원
 * toString 추가
 *
 * Revision 1.1  2006/06/19 13:48:02  이종원
 * 최초작성
 *
 * </pre>
 ******************************************************************/

public class WasInstance {
    /**
     * instanceId
     */
    String instanceId;
    /**
     * instanceName
     */
    String instanceName;
    /**
     * wasConfigId
     */
    WasConfig wasConfig;
    
    public String getInstanceId() {
        return instanceId;
    }
    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
    public String getInstanceName() {
        return instanceName;
    }
    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }
    public WasConfig getWasConfig() {
        return wasConfig;
    }
    public void setWasConfig(WasConfig wasConfig) {
        this.wasConfig = wasConfig;
    }
    public String toString(){
        return ">>> instanceId:"+instanceId
            +", instanceName:"+instanceName
            +",\n\t wasConfig:"+wasConfig;
    }
}
