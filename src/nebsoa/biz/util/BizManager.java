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
import java.util.HashMap;

import nebsoa.biz.exception.BizException;
import nebsoa.biz.factory.BizFactory;
import nebsoa.common.exception.SysException;
import nebsoa.common.jdbc.DBManager;
import nebsoa.common.jdbc.DBResultSet;
import nebsoa.common.jndi.WasManager;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.CodeUtil;
import nebsoa.common.util.DataMap;
import nebsoa.common.util.PropertyManager;
import nebsoa.management.ManagementObject;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 특정 biz id 를 인자로 해당 서비스가 수행되고 있는 was instace의 정보를 찾아 오는 유틸리티.
 * 2.사용법
 * 해당 서비스가 local에서 서비즈 중인지 판단하고자 한다면 
 * WasConfig config = BizManager.getInstance().isLocal("wasConfigId");
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author $Author: cvs $
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * $Log: BizManager.java,v $
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
 * Revision 1.1  2007/11/26 08:38:35  안경아
 * *** empty log message ***
 *
 * Revision 1.24  2007/10/30 08:26:44  김성균
 * BIZ 맵핑정보 로드를 설정에 의해서 처리하도록 수정
 *
 * Revision 1.23  2007/07/02 09:20:28  youngseokkim
 * 로그여부 추가
 *
 * Revision 1.22  2007/06/28 01:52:34  홍윤석
 * 실행큐 코드이름을 가져오는 쿼리를 코드유틸로 변경
 *
 * Revision 1.21  2007/06/26 01:03:11  홍윤석
 * preTrxId --> queName으로 수정
 *
 * Revision 1.20  2007/06/08 11:29:47  최수종
 * *** empty log message ***
 *
 * Revision 1.19  2007/03/07 02:42:07  김성균
 * BizInfo 못 찾았을 경우 예외처리 통일
 *
 * Revision 1.18  2007/01/22 18:49:53  이종원
 * web_config파일에 BIZ.USE_DB_MAPPING 설정 추가
 * 외환은행의영향도를 고려하여 default값을 true로 세팅하였다
 *
 * Revision 1.17  2006/11/16 10:29:51  김성균
 * *** empty log message ***
 *
 * Revision 1.16  2006/11/16 09:22:13  김성균
 * *** empty log message ***
 *
 * Revision 1.15  2006/10/20 11:40:01  김성균
 * *** empty log message ***
 *
 * Revision 1.14  2006/10/20 09:06:55  김성균
 * *** empty log message ***
 *
 * Revision 1.13  2006/10/20 07:11:11  김성균
 * *** empty log message ***
 *
 * Revision 1.12  2006/10/18 08:29:15  김성균
 * *** empty log message ***
 *
 * Revision 1.11  2006/08/22 01:59:32  김성균
 * *** empty log message ***
 *
 * Revision 1.10  2006/08/22 01:49:18  김성균
 * *** empty log message ***
 *
 * Revision 1.9  2006/08/03 08:52:21  김성균
 * *** empty log message ***
 *
 * Revision 1.8  2006/08/03 05:18:35  김성균
 * ManagementObject 구현
 *
 * Revision 1.7  2006/07/07 02:41:36  김성균
 * BizInfo 찾아보고 없으면 로딩을 다시한번 하도록 수정
 *
 * Revision 1.6  2006/06/29 00:09:09  김성균
 * WAS설정정보 가져오는 SQL 수정
 *
 * Revision 1.5  2006/06/26 02:52:13  김성균
 * *** empty log message ***
 *
 * Revision 1.4  2006/06/23 09:33:20  김성균
 * 업무도메인/업무분류 DB 변경 반영
 *
 * Revision 1.3  2006/06/22 08:25:03  김성균
 * *** empty log message ***
 *
 * Revision 1.2  2006/06/21 03:00:50  김성균
 * BIZ접근허용APP 부분 삭제
 *
 * Revision 1.1  2006/06/20 10:54:53  이종원
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class BizManager extends ManagementObject {
    
    private static Object dummy = new Object();
    
    private static BizManager instance = null;
    
    /**
     * 데이타를 캐쉬 해 놓기 위한 저장소 
     */
    protected HashMap bizMap;
    
    /**
     * 싱글톤 처리 
     */
    private BizManager(){ 
        bizMap = new HashMap();
    }
    
    /**
     * 싱글톤 객체 얻어오기
     * 인스턴스 생성하고 데이타 로딩...
     * @return
     */
    public static BizManager getInstance() {
        if (instance == null) {
            synchronized (dummy) {
                instance = new  BizManager();
                boolean useDBMapping = 
                    PropertyManager.getBooleanProperty(
                            "web_config","BIZ.USE_DB_MAPPING","true");
                if (useDBMapping) {
                    instance.loadAll();
                    instance.toXml();
                }
            }
        }
        return instance;
    }
    
    /**
     * BIZ_APP 정보를 로딩한다.
     */
    private void loadAll(){
        if (isXmlMode()) {
            try {
                fromXml();
            } catch (FileNotFoundException e) {
                throw new SysException("XML 파일을 찾을 수 없습니다.");
            }
        } else {
            loadBizConfigInfo();        
        }
    }
    
    /**
     * 해당 서비스가 로컬 머신에서 같이 서비스 되고  있는지 판단해 주는 메소드
     * 2006. 6. 20.  이종원 작성
     * @param wasConfigId
     * @return
     */
    public boolean isLocal(String wasConfigId){
        return WasManager.getInstance().isLocal(wasConfigId);
    }
       
    /**
     * biz config loading sql
     */
    private static String BIZ_CONFIG_LOAD_SQL=
        "SELECT BIZ_APP_ID "
        +"\n, BIZ_APP_NAME "
        +"\n, DUP_CHECK_YN "
        +"\n, QUE_NAME "
        +"\n, LOG_YN "
        +"\n FROM FWK_BIZ_APP ";
    
    /**
     * loadBizConfigInfo from FWK_BIZ_APP, FWK_WAS_SERVICE table
     * 2006. 6. 20.  이종원 작성
     */
    private void loadBizConfigInfo(){
    	
        DBResultSet rs = DBManager.executePreparedQuery(BIZ_CONFIG_LOAD_SQL,new String[]{});
        //LogManager.debug(rs);
        
        if (rs.getRowCount() > 0) {
            if (bizMap == null) {
                bizMap = new HashMap();
            } else {
                bizMap.clear();
            }
        }
        BizInfo biz = null;

        while (rs.next()) {
            biz = new BizInfo();
            biz.setBizAppId(rs.getString("BIZ_APP_ID"));
            biz.setBizAppName(rs.getString("BIZ_APP_NAME"));
            biz.setDupCheck("Y".equals(rs.getString("DUP_CHECK_YN")));
            biz.setQueName(CodeUtil.getCodeName(PropertyManager.getProperty("default", "QUENAME_CODE_GROUP_ID"),rs.getString("QUE_NAME")));
            biz.setLogYn("Y".equals(rs.getString("LOG_YN")));
            bizMap.put(rs.getString("BIZ_APP_ID"), biz);
        }
    }
    
    /**
     * bizId에 해당하는 BIZ_APP 정보를 얻어온다.
     * 
     * @param bizId
     * @return
     */
    public BizInfo getBizInfo(String bizId) throws BizException {
        BizInfo info = (BizInfo) bizMap.get(bizId);
        if (info != null) {
            LogManager.debug("bizId:" + bizId + " 에 해당하는 정보를 찾았습니다.");
            LogManager.debug(info);
        } else {
            boolean useDBMapping = 
                PropertyManager.getBooleanProperty(
                        "web_config","BIZ.USE_DB_MAPPING","true");
            if (useDBMapping) { // default가 db mapping 사용이다.
                throw new BizException("FRS00007", "BIZ Class 정보를 찾을 수 없습니다.(BizId:"+bizId+")");
            } else {
                return BizFactory.getInstance().getTempBiz(bizId);
            }
        }
        return info;
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
        instance = (BizManager) obj;
    }
}
