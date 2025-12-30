package nebsoa.common.util;

import java.util.HashMap;

import nebsoa.common.Constants;
import nebsoa.common.jdbc.DBManager;
import nebsoa.common.jdbc.DBResultSet;

public class MenuUtil {

	private static HashMap urlPositionMap = new HashMap();
	
	
	/**
	 * 해당 url이 위치하는 메뉴의 포지션을 리턴합니다.
	 * @param url 포지션을 찾고자 하는 url
	 * @return 해당 url의 최상위 메뉴부터 해당 url까지의 위치값. 
	 */
	public static String getPagePosition(String url){
			String urlPosition = (String) urlPositionMap.get(url);
			if(urlPosition == null)
			{
				urlPosition = loadPosition(url);
			}
			
		return urlPosition;
	}

	/**
	 * 최 하위 메뉴의 모든 경로 알아오는 쿼리 -by 보균 2006-08-28
	 * SELECT MENU_ID, SUBSTR(SYS_CONNECT_BY_PATH(MENU_NAME, ' > '), 4) MENU_NAME
		FROM   FWK_MENU A
		WHERE  CONNECT_BY_ISLEAF = '1'
		START WITH PRIOR_MENU_ID = '*' AND DISPLAY_YN = 'Y' AND USE_YN = 'Y'
		CONNECT BY PRIOR MENU_ID = PRIOR_MENU_ID;
	 */
	
	/**
	 * 해당 url로 최상위 위치에서부터 현 url 위치가지 알아오는 url
	 */
	private static final String POSITION_SQL = ""; 
	
	/**
	 * cache 된 url 정보가 없을때 디비에서 데이타 로딩.
	 * @param url
	 * @return
	 */
	public static String loadPosition(String url){
		String retUrl = "";
		DBResultSet rs = DBManager.executePreparedQuery(Constants.SPIDER_DB, POSITION_SQL, new Object[]{url});
		while(rs.next()){
			retUrl = rs.getString(0);
			urlPositionMap.put(url,retUrl);
		}
		return retUrl;
	}
}
