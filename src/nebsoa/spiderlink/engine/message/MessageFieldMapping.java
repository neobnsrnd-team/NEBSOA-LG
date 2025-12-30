/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.engine.message;

import java.io.Serializable;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 메시지 매핑 정보를 담고있는 클래스
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
 * $Log: MessageFieldMapping.java,v $
 * Revision 1.1  2018/01/15 03:39:48  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:16  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:50  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:22  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:26  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:51  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:20  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:05  안경아
 * *** empty log message ***
 *
 * Revision 1.5  2006/06/17 10:12:09  김승희
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public class MessageFieldMapping implements Serializable {
	
	private String structure_id;
	
	private String trgOrgId;
	
	private String trgMessageId;
	
	private String trgMessageFieldId;
	
	private String srcOrgId;
	
	private String srcMessageId;
			
	private String mappingExpression;
	
	public MessageFieldMapping(){}
		
	public MessageFieldMapping(String structure_id, String trgOrgId, String trgMessageId,  
			String trgMessageFieldId, String srcOrgId, String srcMessageId,  
			String mappingExpression) {
		
		this.structure_id=        structure_id;
		this.trgOrgId=            trgOrgId;
		this.trgMessageId=        trgMessageId;
		this.trgMessageFieldId=   trgMessageFieldId;
		this.srcOrgId=            srcOrgId;
		this.srcMessageId=        srcMessageId;
		this.mappingExpression=   mappingExpression;
	}

	public String getMappingExpression() {
		return mappingExpression;
	}

	public void setMappingExpression(String mappingExpression) {
		this.mappingExpression = mappingExpression;
	}

	public String getSrcMessageId() {
		return srcMessageId;
	}

	public void setSrcMessageId(String srcMessageId) {
		this.srcMessageId = srcMessageId;
	}

	public String getSrcOrgId() {
		return srcOrgId;
	}

	public void setSrcOrgId(String srcOrgId) {
		this.srcOrgId = srcOrgId;
	}

	public String getStructure_id() {
		return structure_id;
	}

	public void setStructure_id(String structure_id) {
		this.structure_id = structure_id;
	}

	public String getTrgMessageFieldId() {
		return trgMessageFieldId;
	}

	public void setTrgMessageFieldId(String trgMessageFieldId) {
		this.trgMessageFieldId = trgMessageFieldId;
	}

	public String getTrgMessageId() {
		return trgMessageId;
	}

	public void setTrgMessageId(String trgMessageId) {
		this.trgMessageId = trgMessageId;
	}

	public String getTrgOrgId() {
		return trgOrgId;
	}

	public void setTrgOrgId(String trgOrgId) {
		this.trgOrgId = trgOrgId;
	}

}