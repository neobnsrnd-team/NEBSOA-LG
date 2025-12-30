/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.jndi;

import nebsoa.common.util.PropertyManager;

/*******************************************************************
 * <pre>
 * 1.설명 
 * remote에서 서비스 하는 was를 찾기 위해 업무 영역과 was정보를 mapping하는 클래스
 * 
 * 
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
 * $Log: DefaultServiceContext.java,v $
 * Revision 1.1  2018/01/15 03:39:52  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:20  cvs
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
 * Revision 1.2  2006/06/19 04:10:16  안경아
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class DefaultServiceContext implements ServiceContext {
    /**
     * 업무 영역
     */
    String bizCategory;
    
    String wasConfig;
    
    DefaultServiceContext(String category){
        this.bizCategory=category;
        wasConfig = PropertyManager.getProperty("biz_config",category+".WAS.MAPPING");
    }
    
    public static ServiceContext LOCAL_CATETORY= new DefaultServiceContext("LOCAL");
    public static ServiceContext ADMIN_CATEGORY= new DefaultServiceContext("ADMIN");

    public String getBizCategory() {
        return bizCategory;
    }
    public void setBizCategory(String bizCategory) {
        this.bizCategory = bizCategory;
    }
    public String getWasConfig() {
        return wasConfig;
    }
    public void setWasConfig(String wasConfig) {
        this.wasConfig = wasConfig;
    }    
    
}
