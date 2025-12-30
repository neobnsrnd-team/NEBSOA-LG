/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.management;

import java.io.FileNotFoundException;

import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.DataMap;
import nebsoa.common.util.DbToXml;
import nebsoa.common.util.PropertyManager;

/*******************************************************************
 * <pre>
 * 1.설명 
 * ManagementAgent로 관리하거나 DB to XML 관리가 필요한 클래스는 상속 받도록 한다. 
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
 * $Log: ManagementObject.java,v $
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
 * Revision 1.1  2008/08/04 08:54:52  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:25  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:56  안경아
 * *** empty log message ***
 *
 * Revision 1.11  2007/03/07 07:38:30  김성균
 * 일부 로그 INFO 레벨로 변경
 *
 * Revision 1.10  2006/11/16 11:21:03  김성균
 * *** empty log message ***
 *
 * Revision 1.9  2006/10/21 06:49:40  김성균
 * *** empty log message ***
 *
 * Revision 1.8  2006/10/20 12:13:07  김성균
 * *** empty log message ***
 *
 * Revision 1.7  2006/08/29 10:51:48  김성균
 * *** empty log message ***
 *
 * Revision 1.6  2006/08/29 08:25:56  최수종
 * isXmlMode() 소스 수정
 *
 * Revision 1.5  2006/08/28 12:59:08  김성균
 * *** empty log message ***
 *
 * Revision 1.4  2006/08/16 02:48:27  김성균
 * reload() 추가
 *
 * Revision 1.3  2006/08/02 08:11:02  김승희
 * ManagementObject  메소드명 변경
 *
 * Revision 1.2  2006/07/26 08:01:02  김성균
 * reloadAll() 추가
 *
 * Revision 1.1  2006/07/25 02:25:08  김성균
 * 최초 등록
 *
 * </pre>
 ******************************************************************/
abstract public class ManagementObject {

	/**
	 * 관리객체 인스턴스를 얻어오도록 상속받는 클래스에서 구현해야 한다.
	 * @return		관리객체 인스턴스
	 */
	abstract public Object getManagementObject();
	
	/**
	 * 관리객체 인스턴스를 설정하도록 상속받는 클래스에서 구현해야 한다.
	 * @param obj	관리객체 인스턴스
	 */
	abstract public void setManagementObject(Object obj);
    
    /**
     * 관리객체 정보를 리로딩하도록 상속받는 클래스에서 구현해야 한다. 
     */
    public static void reloadAll(DataMap map) {
        throw new SysException("FRS00020", "reloadAll() 메소드가 재정의 되지 않았습니다.");
    }
    
    /**
     * 관리객체 정보를 리로딩하도록 상속받는 클래스에서 구현해야 한다. 
     */
    public static void reload(DataMap map) {
        throw new SysException("FRS00020", "reload() 메소드가 재정의 되지 않았습니다.");
    }
    
	/**
	 * XML 파일명을 생성한다.
	 * @return	XML 파일명을 리턴한다.
	 */
	protected String getXmlFileName() {
		String classFullName = this.getClass().getName();
		String className = classFullName.substring(classFullName.lastIndexOf(".")+1);
		String xmlFileKey = className.toUpperCase() + "_XML";
        LogManager.info("운영정보파일키:" + xmlFileKey);
		String xmlFileName = PropertyManager.getProperty("management", xmlFileKey);
        LogManager.info("운영정보파일명:" + xmlFileName);
		return xmlFileName;
	}
	
	/**
	 * 운영모드가 XML 모드인지 검사
	 * @return	운영모드가 XML 모드이면 TRUE
	 */
	protected static boolean isXmlMode() {
		
		String isXmlMode = PropertyManager.getProperty("default",
                "DATA_LOAD_TYPE", "DB");

        if ("XML".equalsIgnoreCase(isXmlMode)) {
            return true;
        } else {
            return false;
        }
	}
	
	/**
	 * 관리객체의 내용을 XML로 변환하여 파일로 저장한다.
	 */
	public void toXml() {
        if (isXmlMode()) {
            return;
        }
        boolean isXmlSaveMode = PropertyManager.getBooleanProperty(
                "management", "OPERATION_INFO_XML_SAVE_MODE", "OFF");
        LogManager.info("운영정보파일 저장모드:" + isXmlSaveMode);
        if (isXmlSaveMode) {
            DbToXml.toXml(getXmlFileName(), getManagementObject());
            LogManager.info("운영정보파일이 저장되었습니다.");
        }
	}

	/**
	 * XML 파일을 관리객체로 읽어들인다.
	 * @throws FileNotFoundException
	 */
	public void fromXml() throws FileNotFoundException {
		setManagementObject(DbToXml.fromXml(getXmlFileName()));
	}
	
}