/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.util;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.collections.map.MultiKeyMap;

import nebsoa.common.Constants;
import nebsoa.common.collection.DataSet;
import nebsoa.common.exception.SysException;
import nebsoa.common.jdbc.DBManager;
import nebsoa.common.jdbc.DBResultSet;
import nebsoa.common.log.LogManager;
import nebsoa.management.ManagementObject;

/*******************************************************************************
 * <pre>
 *  1.설명 
 *  다국어 처리를 위한 Utility 클래스
 *  
 *  2.사용법
 *  
 *  &lt;font color=&quot;red&quot;&gt;
 *  3.주의사항
 *  &lt;/font&gt;
 * 
 *  @author $Author: cvs $
 *  @version
 * ******************************************************************
 *  - 변경이력 (버전/변경일시/작성자)
 *  
 *  $Log: I18NUtil.java,v $
 *  Revision 1.1  2018/01/15 03:39:49  cvs
 *  *** empty log message ***
 *
 *  Revision 1.1  2016/04/15 02:22:32  cvs
 *  neo cvs init
 *
 *  Revision 1.1  2011/07/01 02:13:51  yshong
 *  *** empty log message ***
 *
 *  Revision 1.1  2008/11/18 11:27:23  김성균
 *  *** empty log message ***
 *
 *  Revision 1.1  2008/11/18 11:01:26  김성균
 *  LGT Gateway를 위한 프로젝트로 분리
 *
 *  Revision 1.1  2008/08/04 08:54:50  youngseokkim
 *  *** empty log message ***
 *
 *  Revision 1.2  2008/02/18 02:48:32  오재훈
 *  *** empty log message ***
 *
 *  Revision 1.1  2008/01/22 05:58:17  오재훈
 *  패키지 리펙토링
 *
 *  Revision 1.5  2008/01/09 10:07:42  홍윤석
 *  Mysql일 경우 추가
 *
 *  Revision 1.4  2007/11/30 10:24:10  안경아
 *  *** empty log message ***
 *
 *  Revision 1.3  2007/11/30 09:46:53  안경아
 *  DB NAME 지정
 *
 *  Revision 1.2  2007/11/29 01:44:25  안경아
 *  *** empty log message ***
 *
 *  Revision 1.1  2007/11/26 08:38:01  안경아
 *  *** empty log message ***
 *
 *  Revision 1.40  2007/07/19 01:17:20  이종원
 *  *** empty log message ***
 *
 *  Revision 1.39  2007/03/07 02:53:45  김성균
 *  일부 로그 INFO 레벨로 변경
 *
 *  Revision 1.38  2007/01/25 00:36:11  김성균
 *  언어코드그룹아이디를 default 프로퍼티에서 읽어오도록 수정
 *
 *  Revision 1.37  2006/12/08 08:11:20  김성균
 *  데이타 없을경우 labelId 리턴하도록 처리
 *
 *  Revision 1.36  2006/12/07 02:09:12  안경아
 *  *** empty log message ***
 *
 *  Revision 1.35  2006/11/27 10:58:58  안경아
 *  *** empty log message ***
 *
 *  Revision 1.34  2006/11/27 09:18:23  안경아
 *  *** empty log message ***
 *
 *  Revision 1.33  2006/11/24 01:24:41  안경아
 *  *** empty log message ***
 *
 *  Revision 1.31  2006/11/23 08:04:56  안경아
 *  *** empty log message ***
 *
 *  Revision 1.30  2006/11/16 10:29:51  김성균
 *  *** empty log message ***
 *
 *  Revision 1.29  2006/11/16 09:22:13  김성균
 *  *** empty log message ***
 *
 *  Revision 1.28  2006/11/16 06:36:02  안경아
 *  *** empty log message ***
 *
 *  Revision 1.27  2006/11/16 02:42:53  안경아
 *  *** empty log message ***
 *
 *  Revision 1.25  2006/11/15 11:51:08  안경아
 *  *** empty log message ***
 *
 *  Revision 1.24  2006/11/10 06:17:51  안경아
 *  *** empty log message ***
 *
 *  Revision 1.23  2006/11/09 11:45:13  안경아
 *  *** empty log message ***
 *
 *  Revision 1.22  2006/10/31 04:47:43  오재훈
 *  *** empty log message ***
 *
 *  Revision 1.21  2006/10/21 06:50:46  김성균
 *  *** empty log message ***
 *
 *  Revision 1.20  2006/10/13 03:48:03  오재훈
 *  *** empty log message ***
 * 
 *  Revision 1.19  2006/09/28 13:31:45  김성균
 *  *** empty log message ***
 * 
 *  Revision 1.18  2006/09/27 02:17:38  안경아
 *  *** empty log message ***
 * 
 *  Revision 1.17  2006/09/27 02:07:18  안경아
 *  *** empty log message ***
 * 
 *  Revision 1.16  2006/09/26 07:42:51  안경아
 *  *** empty log message ***
 * 
 *  Revision 1.15  2006/09/26 05:02:47  안경아
 *  *** empty log message ***
 * 
 *  Revision 1.14  2006/09/26 01:48:06  오재훈
 *  *** empty log message ***
 * 
 *  Revision 1.13  2006/09/13 09:07:08  안경아
 *  *** empty log message ***
 * 
 *  Revision 1.12  2006/09/12 12:18:16  안경아
 *  *** empty log message ***
 * 
 *  Revision 1.11  2006/08/23 00:51:55  오재훈
 *  *** empty log message ***
 * 
 *  Revision 1.10  2006/08/22 09:33:59  오재훈
 *  *** empty log message ***
 * 
 *  Revision 1.9  2006/07/25 12:06:29  김성균
 *  StartupContext.getPropertyType() 사용하지 않도록 수정
 * 
 *  Revision 1.8  2006/07/20 11:35:41  오재훈
 *  등록 안된 라벨일 경우 labelid is not registed.. 메세지 출력하고 해당 ID 리턴
 * 
 *  Revision 1.7  2006/07/10 07:00:46  오재훈
 *  조회 결과가 있을때만 캐쉬에 담아둠.
 * 
 *  Revision 1.6  2006/06/30 08:25:46  오재훈
 *  DBLoader를 건드리지 않고 모든 데이타를 로드하기 위해 소스내에서 쿼리해서 로드.
 *  MiplatForm용 라벨 메소드 추가.
 * 
 *  Revision 1.5  2006/06/21 13:51:02  오재훈
 *  LABEL이 등록되지 않았을 경우에 null 리턴
 * 
 *  Revision 1.4  2006/06/17 10:37:45  김성균
 *  *** empty log message ***
 * 
 * </pre>
 ******************************************************************************/
public class I18NUtil extends ManagementObject {

    public static final String DEFAULT_LOCALE_CODE = "KO";

    private static Object dummy = new Object();

    private static I18NUtil instance;

    /**
     * Comment for <code>multiKeyMap</code>
     */
    private MultiKeyMap multiKeyMap;

    /**
     * 데이타를 캐쉬 해 놓기 위한 저장소
     */
    protected HashMap chchePool;

    /**
     * 싱글톤 처리
     */
    private I18NUtil() {
        chchePool = new HashMap();
        multiKeyMap = new MultiKeyMap();
    }

    public static I18NUtil getInstance() {
    	
        if (instance == null) {
            synchronized (dummy) {
                instance = new I18NUtil();
                LOAD_ALL_CODE_LABEL_MAP.put(Integer.valueOf(DbTypeUtil.ORACLE).toString(), instance.LOAD_ALL_CODE_LABEL_ORA_SQL);
                LOAD_ALL_CODE_LABEL_MAP.put(Integer.valueOf(DbTypeUtil.DB2).toString(), instance.LOAD_ALL_CODE_LABEL_DB2_SQL);
                LOAD_ALL_CODE_LABEL_MAP.put(Integer.valueOf(DbTypeUtil.MY_SQL).toString(), instance.LOAD_ALL_CODE_LABEL_MYSQL_SQL);
                instance.loadAll();
                instance.toXml();
            }
        }
        return instance;
    }

    /**
     * 다국어정보를 로딩한다.
     */
    private void loadAll() {
    	
        if (isXmlMode()) {
            try {
                fromXml();
            } catch (FileNotFoundException e) {
                throw new SysException("XML 파일을 찾을 수 없습니다.");
            }
        } else {
            getAllCodeLabelLoader();
            getAllDataLoader();
        }
    }

    public static String getLabel(String labelId) {
        return getLabel(DEFAULT_LOCALE_CODE, labelId);
    }

    /**
     * 다국어 조회
     * 
     * @param localeCode :
     *            지역 (ex : EN,KO,..)
     * @param labelId :
     *            조회가는 라벨 ID
     * @return 조회된 라벨 ID의 Text. 해당 라벨 ID가 없으면 조회한 라벨 ID
     */
    public static String getLabel(String localeCode, String labelId) {
		HashMap dbloadermap = (HashMap) getInstance().chchePool.get(localeCode);
        if (dbloadermap == null) {
            return labelId;
        }
        String orgLabelText = (String)dbloadermap.get(labelId);
        // LABEL이 등록되지 않았을 경우에 조회한 ID 리턴 2006-07-19
        if (StringUtil.isNull(orgLabelText)) {
        	//KO일때는 로그 남기지 않음 2008-02-18
        	if(!DEFAULT_LOCALE_CODE.equals(localeCode))
        		LogManager.debug(" '" + labelId + "' is not registed label Text......");
            
        	return labelId;
        }

/*
        String labelText = "";
        try {
            labelText = new String(orgLabelText.getBytes("UTF-8"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LogManager.error("메시지 인코딩 중 오류 발생...");
        }
        
        return labelText;
*/
        return orgLabelText;
    }

    /**
     * 다국어 조회
     * 
     * @param localeCode :
     *            지역 (ex : EN,KO,..)
     * @param labelId :
     *            조회가는 라벨 ID
     * @param defaultValue
     *            조회시 없으면 리턴받을 디폴트 값
     * @return 조회된 결과값
     */
    public static String getLabel(String localeCode, String labelId,
            String defaultValue) {
		HashMap dbloadermap = (HashMap) getInstance().chchePool.get(localeCode);
        if (dbloadermap == null) {
            return defaultValue;
        }

        String orgLabelText = (String)dbloadermap.get(labelId);

        // LABEL이 등록되지 않았을 경우에 defaultValue 리턴 2006-08-22
        if (StringUtil.isNull(orgLabelText)) {        
        	//KO일때는 로그 남기지 않음 2008-02-18
        	if(!DEFAULT_LOCALE_CODE.equals(localeCode))
        		LogManager.debug(" '" + labelId + "' is not registed label Text......");

            return defaultValue;
        }

        /*
        String labelText = "";
        try {
            labelText = new String(orgLabelText.getBytes("UTF-8"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LogManager.debug("메시지 인코딩 중 오류 발생...");
        }
        return labelText;
        */
        return orgLabelText;
    }

    /**
     * 다국어 코드 조회
     * 
     * @param localeCode :
     *            지역 (ex : EN,KO,..)
     * @param codeGroupId :
     *            코드그룹ID
     * @param code :
     *            코드
     * @return 다국어코드값, 해당 데이타가 없으면 공백스트링 리턴
     */
    public static String getCodeLabel(String localeCode, String codeGroupId,
            String code) {
        //System.out.println("CodeGroup:"+codeGroupId+",Code:"+code);
        if (codeGroupId == null) {
            return "";
        }
        if (code == null) {
            return "";
        }
        return getLabel(localeCode, codeGroupId + code, "");
    }

    /**
     * 지역코드에 해당하는 코드그룹의 코드들을 Map에서 꺼낸다. 없을경우 조회하여 맵에 넣어준다.
     * 
     * @param localeCode :
     *            지역 (ex : EN,KO,..)
     * @param codeGroupId :
     *            조회할 코드 그룹
     * @return 조회된 결과값
     */
    public static DataSet getCodeLabel(String codeGroupId, String localeCode) {
        // 레퍼런스 참조로 클라이언트가 데이타 조작시 변경되는 이유로 복제개체 넘겨줌   
    	if(codeGroupId == null || codeGroupId.equals("")) return null;
        DataSet set = (DataSet) getInstance().multiKeyMap.get(codeGroupId, localeCode);
        
        DataSet retSet = null;
        if (set == null) {
            retSet = getCodeSet(codeGroupId, localeCode);

            if(getInstance().multiKeyMap.get(codeGroupId, localeCode)==null)
            	getInstance().multiKeyMap.put(codeGroupId, localeCode, retSet);
        } else {        	
            retSet = (DataSet) set.clone();            
        }
        retSet.initRow();
        return retSet;
    }

    /**
     * MiPlatform에서 사용하게 되는 라벨 리스트
     * 
     * @param localeCode
     *            (All이면 모두 조회해서 넘김)
     * @return
     */
    public static HashMap getLabelList(String localeCode) {
        if (localeCode.equals("ALL")) {
            return getInstance().chchePool;
        } else {
    		HashMap dbloadermap = (HashMap) getInstance().chchePool.get(localeCode);
            if (dbloadermap == null) {
                return null;
            }
            return dbloadermap;
        }
    }

    /**
     * 등록된 모든 언어별 라벨을 캐쉬함
     */
    private void getAllDataLoader() {
        ArrayList langArr = loadAllLanguageCode();

        for (int i = 0; i < langArr.size(); i++) {
            
            chchePool.put((String) langArr.get(i), dbload((String) langArr.get(i)));
        }
    }

    /**
     * 등록된 모든 코드그룹별 언어별 라벨을 캐쉬함
   
    private void getAllCodeLabelLoader() {
        ArrayList langArr = loadAllLanguageCode();
        ArrayList codeArr = loadAllCode();

        for (int i = 0; i < langArr.size(); i++) {
            for (int j = 0; j < codeArr.size(); j++) {
                DataSet set = getCodeSet((String) codeArr.get(j),
                        (String) langArr.get(i));
                multiKeyMap.put((String) codeArr.get(j), (String) langArr
                        .get(i), set);
            }
        }
    }
  */
    
//    private void getAllCodeLabelLoader() {
    public void getAllCodeLabelLoader() {
        DataSet codeLabel = loadAllCodeLabel();
        
        int i=0;
        String preCodeGroup = "";
        String preLocaleCode = "";
        String codeGroup = "";
        String localeCode = "";
        ArrayList dataList = new ArrayList();       
        
        while(codeLabel.next()) {
        	codeGroup = codeLabel.getString("CODE_GROUP");
        	localeCode = codeLabel.getString("LOCALE_CODE");
        	
        	String[] dataArr = new String[]{codeLabel.getString("CODE"), codeLabel.getString("CODE_NAME"), codeGroup};
        	if(i==0 || (codeGroup.equals(preCodeGroup)&&localeCode.equals(preLocaleCode))){
        		dataList.add(dataArr);
        	}else{
        		
        		DataSet inSet = new DataSet(new String[]{"CODE", "CODE_NAME", "CODE_GROUP"}, dataList);
        		multiKeyMap.put(preCodeGroup, preLocaleCode, inSet);
//        		LogManager.debug("## multiKeyMap.get("+preCodeGroup+", "+preLocaleCode+")="
//        				+(DataSet)multiKeyMap.get(preCodeGroup, preLocaleCode));
        		dataList = new ArrayList();  
        		dataList.add(dataArr);
        	}
        	if(i==codeLabel.getRowCount()-1){
        		DataSet inSet = new DataSet(new String[]{"CODE", "CODE_NAME", "CODE_GROUP"}, dataList);
        		multiKeyMap.put(preCodeGroup, preLocaleCode, inSet);
//        		LogManager.debug("## multiKeyMap.get("+preCodeGroup+", "+preLocaleCode+")="
//        				+(DataSet)multiKeyMap.get(preCodeGroup, preLocaleCode));        		
        	}
        	
        	preCodeGroup = codeGroup;
        	preLocaleCode = localeCode;
        	i++;
        }


    }
    
    /**
     * 언어코드에 해당하는 라벨정보를 로딩한다.
     * 
     * @param localeCode
     * @return

    private static void getDataLoader(String localeCode) {

        if (getInstance().dataLoaderPool == null) {
        	getInstance().dataLoaderPool = new HashMap();
        	getInstance().dataLoaderPool.put(localeCode, loadAllLanguageCode());
        }
    }
         */
    /*
    private static DataLoader getDataLoader(String localeCode) {

        DataLoader dataLoader = (DataLoader) getInstance().chchePool.get(localeCode);
        if (dataLoader == null || dataLoader.isUseCache() == false) {
            dataLoader = new DataLoader(localeCode, getLoader());

            if (dataLoader.getDataMap().size() != 0) {
                getInstance().chchePool.put(localeCode, dataLoader);
            }
        }
        return dataLoader;
    }

    private static Loadable getLoader() {
        Loadable loader = null;
        loader = new DBLoader();
        return loader;
    }
*/
    /**
     * 등록된 언어 코드들 로드.
     */
    private static final String LOAD_LABEL_CODE_SQL = 
            "		SELECT CODE FROM FWK_CODE"
            + "\r\n  WHERE CODE_GROUP_ID = ?";

    /**
     * 모든 언어코드 로드
     * 
     * @param dataMap
     */
    public static ArrayList loadAllLanguageCode() {

        Object[] params = { PropertyManager
                .getProperty("default", "LANGUAGE_CODE_GROUP_ID") };

        DBResultSet rs = DBManager.executePreparedQuery(Constants.SPIDER_DB, LOAD_LABEL_CODE_SQL,
                params);
        ArrayList al = new ArrayList();
        while (rs.next()) {
            al.add(rs.getString(1));
        }
        return al;
    }

    private static HashMap LOAD_ALL_CODE_LABEL_MAP= new HashMap();
    /**
     * 모든 언어별 코드그룹별 
     */
    private final String LOAD_ALL_CODE_LABEL_ORA_SQL = 
    	 " SELECT AA.LOCALE_CODE, AA.CODE, AA.CODE_NAME, AA.CODE_GROUP"
    	+"\r\n   FROM "
    	+"\r\n     (SELECT LOCALE_CODE, SUBSTR(LABEL_ID,8) CODE, LABEL_TEXT CODE_NAME, SUBSTR(LABEL_ID,0,7) CODE_GROUP  "
    	+"\r\n        FROM FWK_LOCALE_LABEL A, FWK_CODE_GROUP B "
    	+"\r\n       WHERE LABEL_ID LIKE CODE_GROUP_ID||'%' AND "
    	+"\r\n             LOCALE_CODE IN (SELECT CODE FROM FWK_CODE WHERE CODE_GROUP_ID = ?)"
    	+"\r\n      ) AA, FWK_CODE BB"
    	+"\r\n  WHERE AA.CODE = BB.CODE"
    	+"\r\n    AND AA.CODE_GROUP = BB.CODE_GROUP_ID"
    	+"\r\n    AND BB.USE_YN='Y'"
    	+"\r\n  ORDER BY AA.LOCALE_CODE, AA.CODE_GROUP, BB.SORT_ORDER, AA.CODE";
    
    private final String LOAD_ALL_CODE_LABEL_DB2_SQL = 
    	" SELECT AA.LOCALE_CODE, AA.CODE, AA.CODE_NAME, AA.CODE_GROUP "
       	+"\r\n   FROM "
       	+"\r\n     (SELECT LOCALE_CODE,  SUBSTR(LABEL_ID,8) CODE, LABEL_TEXT CODE_NAME, SUBSTR(LABEL_ID,1,7) CODE_GROUP "
       	+"\r\n				FROM FWK_LOCALE_LABEL A "
       	+"\r\n			INNER JOIN FWK_CODE_GROUP B  "
       	+"\r\n							ON SUBSTR(LABEL_ID,1,7) = CODE_GROUP_ID "
       	+"\r\n			WHERE LOCALE_CODE IN (SELECT CODE FROM FWK_CODE WHERE CODE_GROUP_ID = ?) "
       	+"\r\n      ) AA "
       	+"\r\n			INNER JOIN FWK_CODE BB "
       	+"\r\n          ON AA.CODE = BB.CODE "
       	+"\r\n         AND AA.CODE_GROUP = BB.CODE_GROUP_ID "
       	+"\r\n    WHERE BB.USE_YN='Y' "
       	+"\r\n  ORDER BY AA.LOCALE_CODE, AA.CODE_GROUP, BB.SORT_ORDER, AA.CODE "; 

    private final String LOAD_ALL_CODE_LABEL_MYSQL_SQL = 
    	" SELECT AA.LOCALE_CODE, AA.CODE, AA.CODE_NAME, AA.CODE_GROUP "
       	+"\r\n   FROM "
       	+"\r\n     (SELECT LOCALE_CODE,  SUBSTR(LABEL_ID,8) CODE, LABEL_TEXT CODE_NAME, SUBSTR(LABEL_ID,1,7) CODE_GROUP "
       	+"\r\n				FROM FWK_LOCALE_LABEL A "
       	+"\r\n			INNER JOIN FWK_CODE_GROUP B  "
       	+"\r\n							ON SUBSTR(LABEL_ID,1,7) = CODE_GROUP_ID "
       	+"\r\n			WHERE LOCALE_CODE IN (SELECT CODE FROM FWK_CODE WHERE CODE_GROUP_ID = ?) "
       	+"\r\n      ) AA "
       	+"\r\n			INNER JOIN FWK_CODE BB "
       	+"\r\n          ON AA.CODE = BB.CODE "
       	+"\r\n         AND AA.CODE_GROUP = BB.CODE_GROUP_ID "
       	+"\r\n    WHERE BB.USE_YN='Y' "
       	+"\r\n  ORDER BY AA.LOCALE_CODE, AA.CODE_GROUP, BB.SORT_ORDER, AA.CODE "; 
    
    /**
     * 모든 언어별 코드그룹별 코드 로드
     * 
     * @param dataMap
     */
    public static DataSet loadAllCodeLabel() {

        Object[] params = { PropertyManager
                .getProperty("default", "LANGUAGE_CODE_GROUP_ID") };

        DBResultSet rs = DBManager.executePreparedQuery(Constants.SPIDER_DB, QueryManager.getQueryToMap(LOAD_ALL_CODE_LABEL_MAP),
                params);
        return rs;
    }
    
    private static final String LOAD_CODE_SQL = 
        "		SELECT CODE_GROUP_ID FROM FWK_CODE_GROUP";

    /**
     * 모든 코드 그룹을 조회
     * 
     * @param dataMap
     */
    public static ArrayList loadAllCode() {

        DBResultSet rs = DBManager.executeQuery(Constants.SPIDER_DB, LOAD_CODE_SQL);
        ArrayList al = new ArrayList();
        while (rs.next()) {
            al.add(rs.getString(1));
        }
        return al;
    }

    /**
     * 등록된 코드그룹별 언어 코드들 로드.
     */
    
    private static final String CODE_GROUP_LABEL_ID_ORA_SQL = 
              "	     SELECT SUBSTR(LABEL_ID,8) CODE, LABEL_TEXT CODE_NAME, SUBSTR(LABEL_ID,0,7) CODE_GROUP "
            + "\r\n  FROM FWK_LOCALE_LABEL "
            + "\r\n  WHERE LABEL_ID LIKE ?  AND LOCALE_CODE=?";
    private static final String CODE_GROUP_LABEL_ID_DB2_SQL = 
        "	     SELECT SUBSTR(LABEL_ID,8) CODE, LABEL_TEXT CODE_NAME, SUBSTR(LABEL_ID,1,7) CODE_GROUP "
      + "\r\n  FROM FWK_LOCALE_LABEL "
      + "\r\n  WHERE LABEL_ID LIKE ?  AND LOCALE_CODE=?";
    /**
     * 언어코드에 해당하는 코드그룹의 코드들을 로드.
     * 
     * @param codeGroupId :
     *            조회할 코드 그룹
     * @param localeCode :
     *            지역 (ex : EN,KO,..)
     * @return
     */
    public static DBResultSet getCodeSet(String codeGroupId, String localeCode) {

        DBResultSet rs = null;
        rs = (DBResultSet) getInstance().multiKeyMap.get(codeGroupId, localeCode);
        if (rs != null) {
            return rs;
        }

        rs = DBManager.executePreparedQuery(Constants.SPIDER_DB, CODE_GROUP_LABEL_ID_DB2_SQL,
                new Object[] { codeGroupId + "%", localeCode });

        return rs;
    }

    /**
     * 전체리로딩
     */
    public static void reloadAll(DataMap map) {
        getInstance().loadAll();
        getInstance().toXml();
    }

    /*
     * (non-Javadoc)
     * 
     * @see nebsoa.management.ManagementObject#getInstance()
     */
    public Object getManagementObject() {
        return instance;
    }

    /* (non-Javadoc)
     * @see nebsoa.management.ManagementObject#setInstance(java.lang.Object)
     */
    public void setManagementObject(Object obj) {
        instance = (I18NUtil) obj;
    }
    
    public static void main(String[] args){
    	I18NUtil instance = getInstance();
/*    	instance.getAllCodeLabelLoader();
    	
        ArrayList langArr = loadAllLanguageCode();
        ArrayList codeArr = loadAllCode();

        for (int i = 0; i < langArr.size(); i++) {
            for (int j = 0; j < codeArr.size(); j++) {
            	DataSet dSet = (DataSet)instance.multiKeyMap.get((String) codeArr.get(j),
                        (String) langArr.get(i));
            	System.out.println("["+(String) codeArr.get(j)+", "+(String) langArr.get(i)+"]="+dSet);
            }
        }    	
*/
    	DataSet set = instance.getCodeLabel("0101050","KO");
    	
        System.out.println(set);
        DataSet set2 = instance.getCodeLabel(null,"KO");
        System.out.println(set2);
        DataSet set3 = instance.getCodeLabel("0101200","KO");
        System.out.println(set3);        
    }
    
    
    /**
     * DB로부터 LABEL을 로드하기 위한 SQL
     */
    private static final String LOAD_SQL = 
          "      SELECT LABEL_ID "
        + "\r\n,       LABEL_TEXT "
        + "\r\n  FROM  FWK_LOCALE_LABEL "    // 언어별LABEL
        + "\r\n  WHERE LOCALE_CODE = ? "     // 언어코드
        ;

    private HashMap dbload(String localeCode) {
        Object[] params = {
        		localeCode
        };
        DBResultSet rs = DBManager.executePreparedQuery(Constants.SPIDER_DB, LOAD_SQL, params);
        HashMap labelPool = new HashMap();
        while (rs.next()) {
        	labelPool.put(rs.getString(1), rs.getString(2));
        }
        return labelPool;
    }
    
    
}
