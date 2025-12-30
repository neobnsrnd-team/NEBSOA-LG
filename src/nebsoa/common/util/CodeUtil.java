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
import java.util.HashMap;

import nebsoa.common.Constants;
import nebsoa.common.exception.SysException;
import nebsoa.common.jdbc.DBManager;
import nebsoa.common.jdbc.DBResultSet;
import nebsoa.management.ManagementObject;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 코드값에 해당하는 코드명을 얻어오기 위한 클래스 
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
 * $Log: CodeUtil.java,v $
 * Revision 1.1  2018/01/15 03:39:49  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:31  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:23  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:26  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:50  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:17  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.2  2007/11/30 09:46:53  안경아
 * DB NAME 지정
 *
 * Revision 1.1  2007/11/26 08:38:01  안경아
 * *** empty log message ***
 *
 * Revision 1.12  2007/02/07 08:18:18  김성균
 * getCodeGroup() 추가
 *
 * Revision 1.11  2006/11/16 10:29:51  김성균
 * *** empty log message ***
 *
 * Revision 1.10  2006/11/16 09:22:13  김성균
 * *** empty log message ***
 *
 * Revision 1.9  2006/10/23 07:06:48  김성균
 * *** empty log message ***
 *
 * Revision 1.8  2006/10/21 05:35:23  안경아
 * *** empty log message ***
 *
 * Revision 1.7  2006/09/28 05:41:20  오재훈
 * 코드 그룹이 없는 코드가 있을경우 무시함.
 *
 * Revision 1.6  2006/08/07 01:27:29  김성균
 * 널 체크
 *
 * Revision 1.5  2006/07/12 05:42:07  김성균
 * *** empty log message ***
 *
 * Revision 1.4  2006/07/04 13:46:28  김성균
 * *** empty log message ***
 *
 * Revision 1.3  2006/06/17 10:37:45  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class CodeUtil extends ManagementObject{
	
	/**
     * 데이타를 캐쉬 해 놓기 위한 저장소 
     */
    protected HashMap codeGroupPool;
	private static Object dummy = new Object();
	private static CodeUtil instance;
    
	private CodeUtil(){
		codeGroupPool = new HashMap();
	}
    
	/**
	 * @return CodeUtil
	 */
	public static CodeUtil getInstance(){
		if(instance == null){
			synchronized(dummy ){
				instance = new CodeUtil();
				instance.loadAll();
                instance.toXml();
			}
		}
		return instance;
	}
	
    
	private void loadAll() {
		if(isXmlMode()){
			try {
				fromXml();
			} catch (FileNotFoundException e) {
				throw new SysException("XML파일을 찾을 수 없습니다.");
			}
		}else{
			loadCodeGroup();
			loadCode();
		}
	}

	/**
     * 코드그룹의 코드에 해당하는 코드값을 리턴한다. 
	 * @param groupId 코드그룹ID
	 * @param code 코드
	 * @return
	 */
	public static String getCodeName(String groupId, String code) {
		CodeGroup codeGroup = (CodeGroup) getInstance().codeGroupPool.get(groupId);
        if (codeGroup == null) {
            return "";
        }
		return codeGroup.getCode(code);
	}
    
	/**
     * 코드그룹의 코드에 해당하는 코드값을 리턴한다. 
	 * @param groupId 코드그룹ID
	 * @param code 코드
	 * @return
	 */
	public CodeGroup getCodeGroup(String groupId) {
		CodeGroup codeGroup = (CodeGroup) codeGroupPool.get(groupId);
        if (codeGroup == null) {
            throw new SysException("CodeGroup is not exist");
        }
		return codeGroup;
	}
    
    /**
     * 코드그룹에서 코드 포함여부를 리턴한다.
     * @param groupId 코드그룹ID
     * @param code 코드
     * @return
     */
    public boolean hasCode(String groupId, String code) {
        return getCodeGroup(groupId).hasCode(code);
    }
	
	/**
     * 코드그룹 정보를 로드하기 위한 SQL
     */
    private static final String LOAD_CODE_GROUP = 
    	  "\r\n SELECT CODE_GROUP_ID "
    	+ "\r\n FROM FWK_CODE_GROUP "
        ;

	/**
     * 코드그룹 정보를 로딩합니다.
     */
    public void loadCodeGroup() {
    	Object[] params = {};
        
        DBResultSet rs = DBManager.executePreparedQuery(Constants.SPIDER_DB, LOAD_CODE_GROUP, params);
        CodeGroup codeGroup = null;
        String codeGroupId = null;
        
        while (rs.next()) {
        	codeGroupId = rs.getString("CODE_GROUP_ID");
        	codeGroup = new CodeGroup();
        	codeGroupPool.put(codeGroupId, codeGroup);
        }
    }
    
	/**
     * CODE 정보를 로드하기 위한 SQL
     */
    private static final String LOAD_CODE = 
    	  "\r\n SELECT CODE_GROUP_ID "
    	+ "\r\n   ,CODE  "
    	+ "\r\n   ,CODE_NAME  "
    	+ "\r\n FROM FWK_CODE"
        ;

    /**
     * 코드 정보를 로딩합니다.
     */
    public void loadCode() {
    	Object[] params = {};
        
        DBResultSet rs = DBManager.executePreparedQuery(Constants.SPIDER_DB, LOAD_CODE, params);
        CodeGroup codeGroup = null;
        String codeGroupId = null;
        
        while (rs.next()) {
        	codeGroupId = rs.getString("CODE_GROUP_ID");
        	codeGroup = (CodeGroup) codeGroupPool.get(codeGroupId);
        	//코드 그룹이 없는 코드가 있을경우 무시함.
        	if(codeGroup != null)
        		codeGroup.addCode(rs.getString("CODE"), rs.getString("CODE_NAME"));
        }
    }

    /** 전체리로딩 */
    public static void reloadAll(DataMap map) {
        getInstance().loadAll();
        getInstance().toXml();
    }
    
	public Object getManagementObject() {
		return instance;
	}

	public void setManagementObject(Object obj) {
		instance = (CodeUtil) obj;
	}
}
