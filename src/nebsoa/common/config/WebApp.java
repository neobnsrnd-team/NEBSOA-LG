/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.config;


/*******************************************************************
 * <pre>
 * 1.설명 
 * WEB_APP 클래스 
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
 * $Log: WebApp.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:26  cvs
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
 * Revision 1.1  2008/08/04 08:54:52  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/02/14 10:30:33  오재훈
 * WebAppMapping 클래스가 ap단으로 이동됨
 *
 * Revision 1.1  2008/01/22 05:58:28  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:18  안경아
 * *** empty log message ***
 *
 * Revision 1.4  2006/08/02 01:50:05  김성균
 * 사용하지 않는 정보 삭제
 *
 * Revision 1.3  2006/06/22 05:16:35  김성균
 * *** empty log message ***
 *
 * Revision 1.2  2006/06/22 05:14:43  김성균
 * bizAppID => bizAppId로 변경
 *
 * Revision 1.1  2006/06/21 13:49:25  김성균
 * 최초 등록
 *
 * </pre>
 ******************************************************************/
public class WebApp {
	
	/**
	 * WEB_APP_ID
	 */
	private String webAppId;
	
	/**
	 * WEB_APP_명 
	 */
	private String webAppName;
	
	/**
	 * 업무처리APP_ID
	 */
//	private String bizAppId;
	
	/**
	 * VALIDATOR_APP명 
	 */
//	private String validatorAppName;
	
	/**
	 * @return Returns the validatorAppName.
	 */
	/*public String getValidatorAppName() {
		return validatorAppName;
	}*/

	/**
	 * @param validatorAppName The validatorAppName to set.
	 */
	/*public void setValidatorAppName(String validatorAppName) {
		this.validatorAppName = validatorAppName;
	}*/

	/**
	 * @return Returns the webAppId.
	 */
	public String getWebAppId() {
		return webAppId;
	}

	/**
	 * @param webAppId The webAppId to set.
	 */
	public void setWebAppId(String webAppId) {
		this.webAppId = webAppId;
	}

	/**
	 * @return Returns the webAppName.
	 */
	public String getWebAppName() {
		return webAppName;
	}

	/**
	 * @param webAppName The webAppName to set.
	 */
	public void setWebAppName(String webAppName) {
		this.webAppName = webAppName;
	}

	/**
	 * @return Returns the bizAppId.
	 */
	/*public String getBizAppId() {
		return bizAppId;
	}*/

	/**
	 * @param bizAppId The bizAppId to set.
	 */
	/*public void setBizAppId(String bizAppId) {
		this.bizAppId = bizAppId;
	}*/
	
}
