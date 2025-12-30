/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.util;

import java.util.HashMap;



/*******************************************************************
 * <pre>
 * 1.설명 
 * 코드그룹 클래스 
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
 * $Log: CodeGroup.java,v $
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
 * Revision 1.1  2007/11/26 08:38:01  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2007/02/07 08:17:58  김성균
 * hasCode() 메소드 추가
 *
 * Revision 1.1  2006/07/12 05:42:07  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class CodeGroup {
	
	/**
	 * 코드그룹ID
	 */
	public String codeGroupId;
	
	/**
	 * 코드풀
	 */
	public HashMap codePool = new HashMap();
	
	/**
	 * @return Returns the code.
	 */
	public String getCode(String code) {
		return (String) codePool.get(code);
	}

	/**
	 * @param code The code to set.
	 */
	public void addCode(String code, String codeName) {
		codePool.put(code, codeName);
	}

	/**
	 * @return Returns the codeGroupId.
	 */
	public String getCodeGroupId() {
		return codeGroupId;
	}

	/**
	 * @param codeGroupId The codeGroupId to set.
	 */
	public void setCodeGroupId(String codeGroupId) {
		this.codeGroupId = codeGroupId;
	}
    
    /**
     * code 포함여부를 리턴한다.
     * @param code 코드
     * @return
     */
    public boolean hasCode(String code) {
        return codePool.containsKey(code);
    }
}
