/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.async;

import java.io.Serializable;

import nebsoa.common.startup.StartupContext;
import nebsoa.common.util.PropertyManager;
import nebsoa.common.util.UIDGenerator;

/*******************************************************************
 * <pre>
 * 1.설명 
 * AsyncMessageBizClient, AsyncMessageClient를 호출한 Action, Biz의 wrapping 클래스
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
 * $Log: AsyncCaller.java,v $
 * Revision 1.1  2018/01/15 03:39:54  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:37  cvs
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
 * Revision 1.1  2008/04/08 04:42:52  김승희
 * 신규추가
 *
 * Revision 1.1  2008/04/08 02:32:43  shkim
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/

public class AsyncCaller implements Serializable{
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 5714826484164076753L;
	
	/**
	 * caller 인스턴스
	 */
	transient private Object caller;
	
	/**
	 * 결과
	 */
	private Object result;
	
	/**
	 *  caller 식별자
	 */
	private Identifier identifier;
	
	/**
	 *  caller 유형 - ACTION
	 */
	public static final String ACTION_TYPE = "ACTION";
	
	/**
	 *  caller 유형 - BIZ
	 */
	public static final String BIZ_TYPE = "BIZ";
	
	/**
	 *  caller 유형
	 */
	private String type;
	

	public AsyncCaller(Object caller, String type){
		this.caller = caller;
		
		//uid 생성
		String uid = this.hashCode() + "_" + UIDGenerator.generateUID();
		Identifier identifier = new Identifier(StartupContext.getInstanceId(), uid, type);
		
		this.identifier = identifier;
	}
	
	public Object getCaller() {
		return caller;
	}
	public void setCaller(Object caller) {
		this.caller = caller;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
	
	public Object getUid() {
		return identifier.getUid();
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	
	public String getWasId() {
		return identifier.getWasId();
	}
	
	/**
	 *	caller 식별자 
	 *
	 */
	public class Identifier implements Serializable{
		/**
		 * serialVersionUID
		 */
		private static final long serialVersionUID = 3206920555756663769L;
		
		/**
		 * caller가 속한 WAS의 ID
		 */
		private String wasId;
		
		/**
		 * caller의 uid
		 */
		private Object uid;
		
		/**
		 * 응답 was 찾기 모드(수동 : manual, 자동 : auto)
		 */
		private String wasSearchMode;
		
		/**
		 * 응답 was 찾기 모드가 수동일 때 사용하는 providerUrl
		 */
		private String providerUrl;
		
		/**
		 * 응답 was 찾기 모드가 수동일 때 사용하는 initial context factory명
		 */
		private String contextFactory;

		//생성자
		public Identifier(String wasId, Object uid, String type){
			this.wasId = wasId;
			this.uid = uid;
			
			this.wasSearchMode = PropertyManager.getProperty("async", type + ".RESPONSE_WAS_SEARCH_MODE", "auto");
			this.providerUrl = PropertyManager.getProperty("async", type + ".PROVIDER_URL", null);
			this.contextFactory = PropertyManager.getProperty("async", type + ".INITIAL_CONTEXT_FACTORY", null);
		}
		
		public boolean isAutoWasSearch(){
			return "auto".equalsIgnoreCase(this.wasSearchMode);
		}
				
		public String getWasSearchMode() {
			return wasSearchMode;
		}
		public void setWasSearchMode(String wasSearchMode) {
			this.wasSearchMode = wasSearchMode;
		}
		public String getProviderUrl() {
			return providerUrl;
		}
		public void setProviderUrl(String providerUrl) {
			this.providerUrl = providerUrl;
		}
		public String getContextFactory() {
			return contextFactory;
		}
		public void setContextFactory(String contextFactory) {
			this.contextFactory = contextFactory;
		}
		public String getWasId() {
			return wasId;
		}
		public void setWasId(String wasId) {
			this.wasId = wasId;
		}
		public Object getUid() {
			return uid;
		}
		public void setUid(Object uid) {
			this.uid = uid;
		}
	}

	public Identifier getIdentifier() {
		return identifier;
	}

	public void setIdentifier(Identifier identifier) {
		this.identifier = identifier;
	}

}
