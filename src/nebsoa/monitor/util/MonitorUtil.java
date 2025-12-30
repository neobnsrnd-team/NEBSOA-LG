package nebsoa.monitor.util;

import nebsoa.common.util.PropertyManager;
import nebsoa.monitor.MonitorConstants;

/*******************************************************************
 * <pre>
 * 1.설명 
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
 * $Log: MonitorUtil.java,v $
 * Revision 1.1  2018/01/15 03:39:54  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:41  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:52  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:27  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:29  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:54  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/04/07 07:24:28  홍윤석
 * 최초등록
 *
 *
 *
 *
 * </pre>
 ******************************************************************/

public class MonitorUtil {
	
	/**
	 * monitor 데이터를 instance별 수집 및 전송 여부
	 * @param propertyId
	 * @param compareStr
	 * @return
	 */
	
	public static boolean isGatheringMode(String propertyId, String compareStr) {
	   	
	   	boolean isGathering = false;
	   	
    	String[] propertyList = PropertyManager.getProperties(MonitorConstants.MONITOR_CONFIG_FILE,
	    			propertyId, ",", null);
	    	
	    if(propertyList != null){
			for(int i = 0; i < propertyList.length; i++){
				if(compareStr.equals(propertyList[i])){
					isGathering = true;
					break;
				}
			}
    	}	
	    
	    return isGathering;    
   }
	 
	/**
	* monitorCategory별 전송 및 수집 사용 여부  
	* @return
	*/
	
	public static boolean isUseMode (String monitorCategory){
		return PropertyManager.getBooleanProperty(MonitorConstants.MONITOR_CONFIG_FILE, 
				monitorCategory + MonitorConstants.KEY_USE_MODE);
	}
	 
}
