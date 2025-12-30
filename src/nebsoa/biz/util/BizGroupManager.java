/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.biz.util;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import nebsoa.common.exception.SysException;
import nebsoa.common.jdbc.DBManager;
import nebsoa.common.util.DataMap;
import nebsoa.management.ManagementObject;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 업무뷴류 정보를 찾아 오는 유틸리티.
 * 2.사용법
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author $Author: cvs $
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * $Log: BizGroupManager.java,v $
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
 * Revision 1.2  2008/04/25 04:25:06  김승희
 * 동시 쓰레드 요청 문제 처리
 *
 * Revision 1.1  2008/01/22 05:58:34  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2008/01/02 09:39:22  김성균
 * 최초 등록
 *
 * </pre>
 ******************************************************************/
public class BizGroupManager extends ManagementObject {
    
    private static Object dummy = new Object();
    
    private static BizGroupManager instance = null;
    
    private static boolean isInitialized  = false;
    
    /**
     * 업무분류 정보를 가지고 있는 객체들의 풀 
     */
    private Map bizGroupPool;
    
    /**
     * 싱글톤 처리 
     */
    private BizGroupManager(){ 
    	loadAll();
    }
    
    /**
     * 싱글톤 객체 얻어오기
     * 인스턴스 생성하고 데이타 로딩...
     * @return
     */
    public static BizGroupManager getInstance() {
        if (instance == null) {
            synchronized (dummy) {
            	if(!isInitialized){
	                instance = new BizGroupManager();
	                //instance.loadAll();
	                instance.toXml();
	                isInitialized = true;
            	}
            }
        }
        return instance;
    }
    
    
    /**
     * 업무분류 정보를 로딩한다.
     */
    private void loadAll(){
        if (isXmlMode()) {
            try {
                fromXml();
            } catch (FileNotFoundException e) {
                throw new SysException("XML 파일을 찾을 수 없습니다.");
            }
        } else {
            loadBizGroupPool();
        }
    }
    
    /**
     * biz group loading sql
     */
    private static final String BIZ_GROUP_LOAD_SQL =
        "SELECT BIZ_GROUP_ID "
        +"\n, BIZ_GROUP_NAME "
        +"\n, BIZ_GROUP_DESC "
        +"\n, DEFAULT_WORK_SPACE_ID "
        +"\n FROM FWK_BIZ_GROUP ";
    
    /**
     * Load biz group information from FWK_BIZ_GROUP table
     */
    private void loadBizGroupPool() {
        
        ArrayList list = DBManager.executePreparedQueryToBeanList(
                BIZ_GROUP_LOAD_SQL, new Object[]{}, BizGroup.class);
        
        bizGroupPool = new HashMap();
        BizGroup bizGroup = null;

        for (int index = 0, size = list.size(); index < size; index++) {
            bizGroup = (BizGroup) list.get(index);
            bizGroupPool.put(bizGroup.getBizGroupId(), bizGroup);
        }
    }
    
    /**
     * @param bizGroupId
     * @return
     */
    public BizGroup getBizGroup(String bizGroupId) {
        return (BizGroup) bizGroupPool.get(bizGroupId);
    }
    
    /**
     * 전체리로딩 
     */
    public static void reloadAll(DataMap map) {
        getInstance().loadAll();
        getInstance().toXml();
    }
    
    /* (non-Javadoc)
     * @see nebsoa.management.ManagementObject#getInstance()
     */
    public Object getManagementObject() {
        return instance;
    }
    
    /* (non-Javadoc)
     * @see nebsoa.management.ManagementObject#setInstance(java.lang.Object)
     */
    public void setManagementObject(Object obj) {
        instance = (BizGroupManager) obj;
    }
}
