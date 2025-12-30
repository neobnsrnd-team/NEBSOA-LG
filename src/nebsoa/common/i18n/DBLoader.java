/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.i18n;

import java.util.Map;

import nebsoa.common.Constants;
import nebsoa.common.cache.Loadable;
import nebsoa.common.jdbc.DBManager;
import nebsoa.common.jdbc.DBResultSet;

/*******************************************************************
 * <pre>
 * 1.설명 
 * DB로 부터 LABEL을 로딩하는 클래스
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
 * $Log: DBLoader.java,v $
 * Revision 1.1  2018/01/15 03:39:54  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:36  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:27  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:29  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:35  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.2  2007/11/30 09:46:53  안경아
 * DB NAME 지정
 *
 * Revision 1.1  2007/11/26 08:38:06  안경아
 * *** empty log message ***
 *
 * Revision 1.4  2006/06/19 04:10:16  안경아
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class DBLoader implements Loadable {
    
    /**
     * DB로부터 LABEL을 로드하기 위한 SQL
     */
    private static final String LOAD_SQL = 
          "      SELECT LABEL_ID "
        + "\r\n,       LABEL_TEXT "
        + "\r\n  FROM  FWK_LOCALE_LABEL "    // 언어별LABEL
        + "\r\n  WHERE LOCALE_CODE = ? "     // 언어코드
        ;

    public void load(String localeCode, Map dataMap) {
        Object[] params = {
        		localeCode
        };
        DBResultSet rs = DBManager.executePreparedQuery(Constants.SPIDER_DB, LOAD_SQL, params);
        while (rs.next()) {
        	dataMap.put(rs.getString(1), rs.getString(2));
        }
    }
}
