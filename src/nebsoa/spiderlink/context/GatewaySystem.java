/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.context;

import java.io.Serializable;


/*******************************************************************
 * <pre>
 * 1.설명 
 * Gateway System 클래스 
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
 * $Log: GatewaySystem.java,v $
 * Revision 1.1  2018/01/15 03:39:49  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:29  cvs
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
 * Revision 1.1  2008/08/04 08:54:52  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/03/04 07:29:38  김승희
 * 최초 등록
 *
 *
 * </pre>
 ******************************************************************/
public class GatewaySystem implements Serializable{
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -5410015784455130669L;

	private String gwId;
	
	private String systemId;
	
	private String operModeType;
	
	private String ip;
	
	private String port;
	
	private String stopYn;
	
	public GatewaySystem(){}
	
	public GatewaySystem(String gwId, String systemId, String operModeType, String ip,
			String port, String stopYn) {
		super();
		this.gwId = gwId;
		this.systemId = systemId;
		this.operModeType = operModeType;
		this.ip = ip;
		this.port = port;
		this.stopYn = stopYn;
	}

	public String getGwId() {
		return gwId;
	}

	public void setGwId(String gwId) {
		this.gwId = gwId;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getOperModeType() {
		return operModeType;
	}

	public void setOperModeType(String operModeType) {
		this.operModeType = operModeType;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getStopYn() {
		return stopYn;
	}

	public void setStopYn(String stopYn) {
		this.stopYn = stopYn;
	}	
}
