/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.context;

import java.util.ArrayList;
import java.util.List;


/*******************************************************************
 * <pre>
 * 1.설명 
 * 기관 시스템 리스트 클래스 
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
 * $Log: OrgSystemList.java,v $
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
 * Revision 1.2  2008/07/10 06:28:55  김승희
 * 특정 인덱스의 system으로부터 getNextSystem을 찾는 메소드 추가
 *
 * Revision 1.1  2008/01/22 05:58:24  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:08  안경아
 * *** empty log message ***
 *
 * Revision 1.10  2006/10/02 04:50:06  김승희
 * *** empty log message ***
 *
 * Revision 1.9  2006/09/28 12:54:12  이종원
 * change synchronized block
 *
 * Revision 1.8  2006/09/19 06:11:30  김승희
 * 중지된 시스템이 목록에 포함되는 것에 따른 수정
 *
 * Revision 1.7  2006/08/25 08:56:11  김승희
 * *** empty log message ***
 *
 * Revision 1.6  2006/08/25 01:24:54  김승희
 * 시스템 관련 테이블 변경에 따른 수정
 *
 * Revision 1.5  2006/07/28 02:03:52  김승희
 * *** empty log message ***
 *
 * Revision 1.4  2006/07/28 02:02:32  김승희
 * 속성 읽어오는 메소드 추가
 *
 * Revision 1.3  2006/07/27 08:02:32  김승희
 * 기관송수신프로토콜, 기관 시스템 테이블 변경에 따른 수정
 *
 * Revision 1.2  2006/06/20 08:46:12  김승희
 * *** empty log message ***
 *
 * Revision 1.1  2006/06/20 08:45:09  김승희
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public class OrgSystemList extends ArrayList{
		
	private static final long serialVersionUID = 1441775112142404640L;
	private int nextSystemIndex = -1;
	
	private int availableSystemCount = 0;
	
	/**
	 * 이 시스템 리스트들이 속해있는 Gateway
	 */
	private Gateway gateway;
	
	/**
	 * 다음 기관 시스템정보를 리턴합니다.
	 * 
	 * @return 기관 시스템정보
	 */
	public OrgSystem getNextSystem(){
		int systemIndex = 0;
		
		synchronized(this){
		    nextSystemIndex++;
		    if(nextSystemIndex>=this.size())nextSystemIndex = 0;
		    systemIndex = nextSystemIndex;
        }
		
		OrgSystem system = (OrgSystem)get(systemIndex);
		
		return system;
	}
	
	/**
	 * 해당 인덱스의 다음 인덱스에 해당하는 기관 시스템정보를 리턴합니다.
	 * 인덱스가 시스템 리스트 사이즈를 초과한 경우 다시 0부터 시작합니다.
	 * 
	 * @return 기관 시스템정보
	 */
	public OrgSystem getNextSystem(int startSystemIndex){
		
		startSystemIndex++;
		
		if(startSystemIndex>=this.size() || startSystemIndex <0) startSystemIndex = 0;
		
		return (OrgSystem)get(startSystemIndex);
	}
	
	public boolean hasAvailSystem(){
		if(availableSystemCount>0) return true;
		else return false;
	}
	
	public void addOrgSystem(String gwId, String systemId, String operModeType, String ip, String port, String stopYn){
		if("N".equals(stopYn)) availableSystemCount++;
		this.add(new OrgSystem(gwId, systemId, operModeType, ip, port, stopYn, this));
	}
	
	
	/**
	 * OrgSystem을 리스트에 추가할 때 인덱스를 OrgSystem에 부여하기 위해서 ArrayList의 add 메소드를 오버로딩하였다.
	 * @param orgSystem
	 * @return
	 */
	private boolean add(OrgSystem orgSystem){
		orgSystem.setIndex(this.size());
		return super.add(orgSystem);
	}
	
	public class OrgSystem{
		
		private String gwId;
		
		private String systemId;
		
		private String operModeType;
		
		private String ip;
		
		private String port;
		
		private String stopYn;

		private OrgSystemList orgSystemList;
		
		private int index;
		
		public OrgSystem(String gwId, String systemId, String operModeType, String ip, String port, String stopYn, OrgSystemList orgSystemList) {
			this.gwId = gwId;
			this.systemId = systemId;
			this.operModeType = operModeType;
			this.ip = ip;
			this.port = port;
			this.stopYn = stopYn;
			this.orgSystemList = orgSystemList;
		}
		
		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
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

		public String getSystemId() {
			return systemId;
		}

		public void setSystemId(String systemId) {
			this.systemId = systemId;
		}

		public String getStopYn() {
			return stopYn;
		}

		public void setStopYn(String systemStatus) {
			this.stopYn = systemStatus;
		}

		public String getGwId() {
			return gwId;
		}

		public void setGwId(String gwId) {
			this.gwId = gwId;
		}

		public String getOperModeType() {
			return operModeType;
		}

		public void setOperModeType(String operModeType) {
			this.operModeType = operModeType;
		}

		public List getOrgSystemList() {
			return orgSystemList;
		}

		
		public OrgSystem getNextSystem(){
			return orgSystemList.getNextSystem();
		}
		
		public Gateway getGateway() {
			return orgSystemList.getGateway();
		}
		
		/**
		 * 중지상태가 아닌 시스템 갯수를 리턴한다.
		 * @return availableSystemCount
		 */
		public int getSystemCount(){
			return orgSystemList.availableSystemCount;
		}
	}

	public Gateway getGateway() {
		return gateway;
	}

	public void setGateway(Gateway gateway) {
		this.gateway = gateway;
	}

}
