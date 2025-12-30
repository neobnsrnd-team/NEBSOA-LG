/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.management.agent;

import java.io.FileNotFoundException;

import nebsoa.common.batch.BatchDetailLogInfo;
import nebsoa.common.exception.ManagementException;
import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.DataMap;
import nebsoa.common.util.DbToXml;
import nebsoa.common.util.PropertyManager;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 관리객체의 Agent 기능을 수행하는 클래스
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
 * $Log: BatchLogAgent.java,v $
 * Revision 1.1  2018/01/15 03:39:49  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:34  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:23  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:27  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.2  2008/10/16 01:58:54  김성균
 * 필요없는 Exception 던지는 부분삭제
 *
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:35  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:45  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/31 05:26:21  김성균
 * 오류처리 추가
 *
 * Revision 1.1  2007/01/15 08:43:59  안경아
 * *** empty log message ***
 *
 * Revision 1.5  2006/11/09 00:46:46  김성균
 * *** empty log message ***
 *
 * Revision 1.4  2006/11/08 07:40:39  김성균
 * *** empty log message ***
 *
 * Revision 1.3  2006/11/08 02:34:07  김성균
 * *** empty log message ***
 *
 * Revision 1.2  2006/11/07 11:04:42  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2006/09/28 05:31:29  김성균
 * *** empty log message ***
 *
 * Revision 1.3  2006/08/16 02:48:54  김성균
 * DataMap argument 추가
 *
 * Revision 1.2  2006/07/26 08:01:58  김성균
 * reflection 으로 처리하도록 수정
 *
 * Revision 1.1  2006/07/21 07:36:54  김성균
 * 최초 등록
 *
 * </pre>
 ******************************************************************/
public class BatchLogAgent {

	/**
     * 테스트클래스를 수행한다. 
	 * @param map
	 * @return map
	 * @throws ManagementException
	 * @throws FileNotFoundException 
	 */
	public static DataMap getBatchLogInfo(DataMap map) {

		String path1 = PropertyManager.getProperty("management", "BATCH_INFO_XML_PATH_1");
		String path2 = PropertyManager.getProperty("management", "BATCH_INFO_XML_PATH_2");
		String path = null;
		if (map.getString("instanceId").indexOf("CA1") == 0)
            path = path1;
        else if (map.getString("instanceId").indexOf("CA2") == 0)
            path = path2;
        
		BatchDetailLogInfo info = null;
        String fileName = map.getParameter("fileName") + ".xml";
        
        try {
            info = (BatchDetailLogInfo) DbToXml.fromXml(path, fileName);
        } catch (FileNotFoundException e) {
            LogManager.error(e.toString(), e);
            throw new SysException("FRB00005", "배치상세로그파일이 존재하지 않습니다.[" + path + fileName + "]");
        }
        
        if (info == null) {
            throw new SysException("FRB00006", "배치상세로그정보가 정상적으로 로딩되지 않았습니다.");
        }
		
		map.put("info", info);        
		return map;
	}
	
	public static void main(String [] args){
		
		String a = "CA21";
		System.out.println("=="+a.indexOf("CA2"));
	}
}
