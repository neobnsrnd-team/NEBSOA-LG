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
import java.util.Map;

import nebsoa.common.log.LogManager;
import nebsoa.common.startup.StartupContext;
import nebsoa.common.util.ConfigMap;
import nebsoa.common.util.StringUtil;

import org.apache.commons.collections.map.MultiKeyMap;
import org.apache.commons.lang.StringUtils;

/*******************************************************************
 * <pre>
 * 1.설명 
 * Gateway(시스템 그룹) 클래스 
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
 * $Log: Gateway.java,v $
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
 * Revision 1.1  2008/01/22 05:58:24  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.3  2008/01/04 06:10:26  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2007/12/28 05:49:02  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2007/11/26 08:38:09  안경아
 * *** empty log message ***
 *
 * Revision 1.8  2006/10/02 16:55:57  이종원
 * propertyMap을 ConfigMap 으로 수정
 *
 * Revision 1.7  2006/10/02 16:49:39  이종원
 * propertyMap을 ConfigMap 으로 수정
 *
 * Revision 1.6  2006/10/02 16:48:39  이종원
 * propertyMap을 ConfigMap 으로 수정
 *
 * Revision 1.5  2006/09/29 08:00:21  김승희
 * 속성 로딩 부분 수정
 *
 * Revision 1.4  2006/09/23 02:16:43  이종원
 * *** empty log message ***
 *
 * Revision 1.3  2006/09/18 01:03:23  김승희
 * gwAppName 추가
 *
 * Revision 1.2  2006/09/16 07:49:00  이종원
 * getIntProperty추가
 *
 * Revision 1.1  2006/08/25 01:24:54  김승희
 * 시스템 관련 테이블 변경에 따른 수정
 *
 *
 * </pre>
 ******************************************************************/
public class Gateway implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 8689136072033303072L;

	private String gwId;

	private String gwName;

	private int threadCount;

	private String gwProperties;

	private String gwAppName;

	private ConfigMap propertyMap;

	private String ioType;

	public String getIoType() {
		return ioType;
	}


	public void setIoType(String ioType) {
		this.ioType = ioType;
	}


	public Gateway(){
		propertyMap = new ConfigMap();
	}


	private void loadPropertyMap(String properties){

		String[] tokenString = StringUtils.splitByWholeSeparator(properties, ";");
		String[] keyValueUnit = null;
		for(int i=0; tokenString!=null && i<tokenString.length; i++){
			keyValueUnit = StringUtils.splitByWholeSeparator(tokenString[i], "=");
			if(keyValueUnit!=null && keyValueUnit.length>=2){
				propertyMap.put(keyValueUnit[0].trim(), keyValueUnit[1]);
			}
		}

	}

	/**
	 * key값으로 속성에 등록되어 있는 해당 value를 리턴한다.
	 * @param key
	 * @return key에 해당하는 value
	 */
	public String getProperty(String key){
		return (String)this.propertyMap.get(key);
	}

	/**
	 * key값으로 속성에 등록되어 있는 해당 value를 int형태 리턴한다.
	 * @param key
	 * @return key에 해당하는 value
	 */
	public int getIntProperty(String key){
		return Integer.parseInt(getProperty(key));
	}

	public String getGwId() {
		return gwId;
	}

	public void setGwId(String gwId) {
		this.gwId = gwId;
	}

	public String getGwName() {
		return gwName;
	}

	public void setGwName(String gwName) {
		this.gwName = gwName;
	}

	public String getGwProperties() {
		return gwProperties;
	}

	public void setGwProperties(String gwProperties) {
		this.gwProperties = gwProperties;
		loadPropertyMap(gwProperties);
	}

	public int getThreadCount() {
		return threadCount;
	}

	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}


	public String getGwAppName() {
		return gwAppName;
	}


	public void setGwAppName(String gwAppName) {
		this.gwAppName = gwAppName;
	}


	public ConfigMap getPropertyMap() {
		return propertyMap;
	}


	public void setPropertyMap(Map propertyMap) {
		if(this.propertyMap==null){
			propertyMap = new ConfigMap(propertyMap);
		}else{
			this.propertyMap.putAll(propertyMap);
		}
	}

	public boolean isMyConnector(Gateway gateway, String systemId){
		boolean result = false;
		//Was Listener 매핑 정보를 가지고온다.
		//2008.07.24 kimeunjung - connector에도 instance적용 
		MultiKeyMap wasListenerMap = MessageEngineContext.getContext().getWasListenerMap();
		String instance_id = StringUtil.NVL(StartupContext.getInstanceId());
		String wasId = (String) wasListenerMap.get(gateway.getGwId(), systemId, instance_id);
		result = (instance_id.equals(wasId))?true:false;
		LogManager.debug("######## gateway ioType=I , wasId="+wasId+" , instance id="+StartupContext.getInstanceId()+" then isWasListener="+result + " ,gateway Id="+gateway.getGwId());

		return result;   		 

		/*
    	if("O".equals(gateway.getIoType())) {
    		LogManager.debug("######## gateway ioType=O then isWasListener=true");
    		return true;
    	}else{
   		 //Was Listener 매핑 정보를 가지고온다.
    		MultiKeyMap wasListenerMap = MessageEngineContext.getContext().getWasListenerMap();
    		String instance_id = StringUtil.NVL(StartupContext.getInstanceId());
    		String wasId = (String) wasListenerMap.get(gateway.getGwId(), systemId, instance_id);
    		result = (instance_id.equals(wasId))?true:false;
    		LogManager.debug("######## gateway ioType=I , wasId="+wasId+" , instance id="+StartupContext.getInstanceId()+" then isWasListener="+result);
    		return result;   		 
    	} 
		 */   	
	}

}
