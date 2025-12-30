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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.ArrayStack;

import nebsoa.common.log.LogManager;
import nebsoa.common.util.StringUtil;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 전문 정보와 필드 정보를 갖고 있는 클래스
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
 * $Log: MessageStructure.java,v $
 * Revision 1.1  2018/01/15 03:39:48  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:15  cvs
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
 * Revision 1.2  2008/04/17 05:45:17  김은정
 * parentId에 @가 있는 경우고려하여 수정
 *
 * Revision 1.1  2008/01/22 05:58:20  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.4  2007/12/24 09:01:47  안경아
 * *** empty log message ***
 *
 * Revision 1.3  2007/12/18 08:30:51  김승희
 * CODE_MAPPING_YN 필드 추가
 *
 * Revision 1.2  2007/12/18 02:28:52  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2007/11/26 08:38:04  안경아
 * *** empty log message ***
 *
 * Revision 1.41  2007/03/20 02:02:11  안경아
 * *** empty log message ***
 *
 * Revision 1.40  2007/03/09 06:04:51  김승희
 * field type 추가
 *
 * Revision 1.39  2006/10/19 13:34:18  이종원
 * field log mode 로딩 추가
 *
 * Revision 1.38  2006/07/19 07:48:29  김승희
 * Remark 필드 존재 여부 추가
 *
 * Revision 1.37  2006/06/20 12:24:26  김승희
 * required_yn 속성 추가
 *
 * Revision 1.36  2006/06/17 10:12:09  김승희
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public class MessageStructure implements Serializable {
	
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 4004509806734321702L;
	
    /**
     * DUMMY 값 (현재 처리하고 있는 field값을 넣기 위해 사용)
     */    
    int fieldCount=0;
    /**
     * 부모 sturctureId
     */
    String parentId;   
    
    /**
     * structureId
     */
    String structureId;  
    
    /**
     * messageId
     */
    private String messageId;
    
    /**
     * logLevel
     */
    private String logLevel;    
    
    /**
     * Message Info array
     */
    MessageField[] fields=null;
    
    /**
     * 필드 맵핑 정보
     */
    private MessageFieldMappingMap fieldMappings=null;
    
	/**
	 * offset
	 */
	private int offset;
	/**
	 * 메시지의 길이(byte)
	 */
	private int length;
	/**
	 * 필드 갯수
	 */
	private int size;
	
	/**
	 * 메시지가 여러개의 형태로 연결되어 있을 경우,
	 * 연결된 다음 메시지에 대한 메시지 ID 를 가지고 있는 필드 ID
	 */
	private String nextStructureFieldId;

	/**
	 * 메세지 타입
	 */
	private MessageType messageType;
	
	/**
	 * 기관 ID
	 */
	private String orgId;
	
	/**
	 * fieldTag
	 */
	private String fieldTag;
	
	/**
	 * XML ROOT TAG 
	 */
	private String xmlRootTag;
	
	/**
	 * Loop Field의  stack
	 */ 
	private ArrayStack loopFieldStack = new ArrayStack(0);
	
	
	private boolean isRemark;
    
    private boolean isLogMode=true;
	
	public boolean isRemark() {
		return isRemark;
	}


	public void setRemark(boolean isRemark) {
		this.isRemark = isRemark;
	}


	public String getOrgId() {
		return orgId;
	}


	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}


	/**
	 * 초기화 작업을 담당하는 기본 생성자
	 */
	public MessageStructure() {
		
	}//end of constructor
	
	
	/**
	 * @param structrueId MessageStructrue ID ( 메시지ID@기관ID)
	 */
	public MessageStructure(String structrueId) {
		this.structureId = structrueId;
	}//end of constructor
	
	public MessageField getField(int index) throws IndexOutOfBoundsException {
		if (index < 0 || index >= this.size)	{
			throw new IndexOutOfBoundsException(index + " < 0 or index >= "+ this.size);
		}//end if
		return fields[index];
	}//end of getField()
	
	
	/**
	 * 현재 message structure, 부모 message structure 통틀어 필드명에 해당하는 MessageField를 찾아 리턴한다. 
	 * @param fieldName 필드명
	 * @return
	 */
	public MessageField getFieldByName(String fieldName){
		for(int i=0; i<fields.length; i++){
			if(fields[i]==null) continue;
			if(fields[i].getName().equals(fieldName)) return fields[i];
			else if (fields[i] instanceof LoopField){
				LoopField loopField = (LoopField)fields[i];
				MessageField mField = getFieldByName(loopField, fieldName);
				if(mField!=null) return mField;
			}
		}
		if(this.getParentId()!=null){
			return this.getParent().getFieldByName(fieldName);
		}
		
		return null;
		
	}
	
	/**
	 * loopField안에서 fieldName과 동일한 Messagefield를 리턴한다.
	 * @param loopField loopField, fieldName 필드명
	 * @return
	 */
	private MessageField getFieldByName(LoopField loopField, String fieldName){
		MessageField nestedField = null;
		MessageField returnField = null;
		for(int j=0; j<loopField.getChildCount(); j++){
			nestedField = loopField.getChild(j);
			//System.out.println("@@@ nestedField : "+ nestedField.getName());
			if(nestedField.getName().equals(fieldName)){
				return nestedField;
			}
			else if (nestedField instanceof LoopField){
				returnField = getFieldByName((LoopField)nestedField, fieldName);
			}
		}
		return returnField;
	}
	
	/**
	 * fieldTag에 해당하는 MessageField를 찾아 리턴한다. 
	 * @param fieldTag fieldTag 
	 * @return MessageField
	 */
	public MessageField getFieldByFieldTag(String fieldTag){
		LoopField loopField = null;
		MessageField returnField = null;
		for(int i=0; i<fields.length; i++){
			if(fields[i]==null) continue;
			
			if(fieldTag.equals(fields[i].getFieldTag())) return fields[i];
			
			if(fields[i] instanceof LoopField){
				loopField = (LoopField)fields[i];
				returnField = loopField.getMessageFieldByFieldTag(fieldTag);
				if(returnField!=null) return returnField;
			}
			
		}
		
		/*if(this.getParentId()!=null){
			return this.getParent().getFieldByXMLTag(XMLTag);
		}*/
		
		return null;
		
	}
	
	/**
	 * Field Tag를 키로 하여 Field Name을 value로 갖고 있는 Map을 리턴한다.
	 * @return Map
	 */
	public Map getFieldTagNameMap(){
		Map map = new HashMap(this.fields.length);
		for(int i=0; i<fields.length; i++){
			if(fields[i]==null) continue;
			if(fields[i] instanceof LoopField){
				getFieldTagNameMap((LoopField)fields[i], map);
			}else{
				map.put(fields[i].getFieldTag(), fields[i].getName());
			}
						
		}
		return map;
	}
	
	private void getFieldTagNameMap(LoopField loopField, Map map){
		MessageField field = null;
		for(int i=0; i<loopField.getChildCount(); i++){
			field = loopField.getChild(i);
			if(field instanceof LoopField){
				getFieldTagNameMap((LoopField)field, map);
			}
			map.put(field.getFieldTag(), field.getName());
		}

	}
	
	public void setFieldMappingMap(MessageFieldMappingMap messageFieldMappings){
		this.fieldMappings = messageFieldMappings;
	}
	
	public MessageFieldMappingMap getFieldMappingMap(){
		return this.fieldMappings;
	}
	
	public int size() {
		return this.size;
	}//end of size()

    /**
     * 부모를 제외한 자신의 전문 길이를 리턴 합니다.
     * @return
     */
	public int getLength() {
		return length;
	}//end of length()


	/**
	 * nextStructureFieldId 의 값을 리턴합니다.
	 * 
	 * @return nextStructureFieldId 의 값
	 */
	public String getNextStructureFieldId() {
		return nextStructureFieldId;
	}

	/**
	 * nextStructureFieldId 에 값을 세팅합니다.
	 * 
	 * @param nextStructureFieldId nextStructureFieldId 에 값을 세팅하기 위한 인자 값
	 */
	public void setNextStructureFieldId(String nextStructureFieldId) {
		this.nextStructureFieldId = nextStructureFieldId;
	}
    
    /**
     * 필드를 추가합니다.
     * 
     * @param name 필드 명
     * @param length 필드 길이
     * @param scale scale
     * @param dataType 필드 타입
     */
    public void addField(String name, int sortOrder, int length, int scale, String dataType, String fieldType, String useMode, String align, String codeGroup, String fieldTag, boolean required,boolean logMode, boolean isCodeMapping)   {
        addField( name,  sortOrder, length,  scale,  dataType, fieldType, null, null, null,null, useMode, align, codeGroup, fieldTag, required,logMode, isCodeMapping);     
    }//end of addField()
    
        
    /**
     * 필드를 추가합니다.
     * 
     * @param name 필드 명
     * @param length 필드 길이
     * @param scale scale
     * @param dataType 필드 타입
     */
    public MessageField addField(String name, int sortOrder, int length, int scale, String dataType, String fieldType,
          String defaultValue, String testValue, String remark,String filler, String useMode, String align, String codeGroup, String fieldTag, boolean required,boolean logMode, boolean isCodeMapping)   {
    	
    	if(name.startsWith(MessageField.BEGIN_LOOP)){
    		
            return addLoopMessageField(name, sortOrder, length, scale, dataType, fieldType, defaultValue, testValue, remark, filler, useMode, align, codeGroup, fieldTag, required, logMode, isCodeMapping);
    	
    	}else if(name.startsWith(MessageField.END_LOOP)){
    		
    		LoopField loopField = (LoopField)loopFieldStack.pop();
    		                        
            //END_LOOP 에서 loop field의 child field length에 maxlength만큼 곱해서 message structure에 lenth에 넣는다.
            ArrayList childList = loopField.getChildList();
            MessageField mField = null;
            int childFieldLength = 0;
            for(int i=0; i<childList.size(); i++){
            	mField = (MessageField)childList.get(i);
            	childFieldLength+=mField.getLength();
            }
            
            //부모 LoopField의 총  max occurs를 구한다. (sumMaxOccurs-현재 LoopField의 max occurs)
            int totalMaxOccurs = 0;
            totalMaxOccurs = (loopField.sumMaxOccurs(totalMaxOccurs))-loopField.getMaxOccurs();
            //totalMaxOccurs가 0일 때는 1로 한다.
            totalMaxOccurs = totalMaxOccurs==0?1:totalMaxOccurs;
            this.length += totalMaxOccurs*loopField.getMaxOccurs()*childFieldLength;
            
            MessageField field = null;
            
            
            //_EndLoop_는 Message Structure 로 넣지 않아도 될 듯...
            /*
            LoopField lField = null;
            if(loopFieldStack.size()>0&&(lField=(LoopField)loopFieldStack.peek())!=null){
            	field=makeMessageField(name, sortOrder, length, 
                        scale,type,defaultValue,
                        testValue,remark,filler,useMode, align, codeGroup, xmlTag);
            	lField.addChild(field);

            }else{
                field=addMessageField(name, sortOrder, length, 
                        scale,type,defaultValue,
                        testValue,remark,filler,useMode,align, codeGroup, xmlTag);
            }*/
            
            return field;
        }else{
            MessageField field = null;
            LoopField lField = null;
           
            if(loopFieldStack.size()>0&&(lField=(LoopField)loopFieldStack.peek())!=null){
           
            	field=makeMessageField(name, sortOrder, length, 
                        scale,dataType,fieldType,defaultValue,
                        testValue,remark,filler,useMode, align, codeGroup, fieldTag, required,logMode, isCodeMapping);
            	lField.addChild(field);

            }else{
                field=addMessageField(name, sortOrder, length, 
                        scale,dataType,fieldType,defaultValue,
                        testValue,remark,filler,useMode,align, codeGroup, fieldTag, required,logMode, isCodeMapping);
            }

            return field;
        }
    }//end of addField()


	private MessageField addLoopMessageField(String name, int sortOrder, int length, 
			int scale, String dataType, String fieldType, String defaultValue, String testValue, String remark, String filler, 
			String useMode, String align, String codeGroup, String fieldTag, boolean required, boolean logMode, boolean isCodeMapping) {
		LoopField loopField = new LoopField(name, dataType, fieldType, length, 
		        scale, sortOrder, offset, defaultValue,
		        testValue,remark, filler, useMode, align, codeGroup, fieldTag, required, logMode, isCodeMapping);
		LoopField parentLoopField = null;
				
		if(loopFieldStack.size()>0 && (parentLoopField=(LoopField)loopFieldStack.peek())!=null){
			loopFieldStack.push(loopField);
			
			parentLoopField.addChild(loopField);
			
		}else{
			loopFieldStack.push(loopField);
			
			offset += length;
			//ksh 추가 
			this.length += length;
			fields[fieldCount++]=loopField;
		}
		
		return loopField;
	}
    
    public MessageField addMessageField(String name, int sortOrder, int length, int scale, String dataType, String fieldType,
            String defaultValue, String testValue, String remark,String filler,String useMode,String align, 
            String codeGroup, String fieldTag, boolean required,boolean logMode, boolean isCodeMapping){
        MessageField field = makeMessageField(name,sortOrder,length,scale,dataType,fieldType,defaultValue,testValue,
                remark,filler,useMode,align , codeGroup,fieldTag, required,logMode, isCodeMapping);

        fields[fieldCount++]=field;
        offset += length;
        this.length += length;
        return field;
    }
    
    public MessageField makeMessageField(String name, int sortOrder, int length, int scale, String dataType, String fieldType,
            String defaultValue, String testValue, String remark,String filler, String useMode, String align, 
            String codeGroup, String fieldTag, boolean required,boolean logMode, boolean isCodeMapping){
        MessageField field = new MessageField(name, dataType, fieldType, length, 
                scale, sortOrder, offset,defaultValue,
                testValue,remark,filler,useMode,align, codeGroup, fieldTag, required, logMode, isCodeMapping);
        return field;
    }
    
    /**
     * field갯수를 넣어 줍니다. 내부적으로 필드를 담을 배열을 생성하므로 
     * 필요한 시점에만  호출 해야 합니다.
     * @param size
     */
    public void setSize(int size) {
        this.size = size;
        fields=new MessageField[size];
    }
    
    /***
     * 부모 전문 길이 + 자신의 전문 길이를 리턴 합니다.
     * @return
     */
    public int getTotalLength(){
        int parentLength=0;
        if(getParent() != null){
            parentLength=getParent().getTotalLength();
            LogManager.info("ParentLength:"+parentLength+",MyLength:"+getLength()
                    +",total length:"+(parentLength+getLength()));
        }
        
        return parentLength+getLength();
    }
    
    /**
     * 부모 전문과 자신의 전문을 담을 수 있는 byte[]을 리턴 합니다.
     * @return
     */
    public byte[] getBuffer(){
        return new byte[getTotalLength()];
    }
    
    public byte[] parseMessage(){
        return null;
    }    
    
    public MessageStructure getParent(){
        if(parentId==null || "".equals(parentId)) return null;
        /* 기관마다 동일하게 사용되는 공통헤더를 만들지 않기 위하여 공통헤더라는 기관을 새로 생성하고 
           COMMON_HEADER(공통헤더)라는 기관에 속한 ParentId는 'parentId@orgId'형태로 등록된다.  */
        int index = parentId.indexOf("@");
        if (index != -1) {
        	return MessageStructurePool.getInstance().getMessageStructure(parentId);
        }
        return MessageStructurePool.getInstance().getMessageStructure(parentId+"@"+orgId);
    }

    public String getParentId() {
        return parentId;
    }
    
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getStructureId() {
        return structureId;
    }

    public void setStructureId(String name) {
        this.structureId = name;
    }


	public MessageType getMessageType() {
		return messageType;
	}


	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}


	public String getMessageId() {
		return messageId;
	}


	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}


	public String getFieldTag() {
		return fieldTag;
	}


	public void setFieldTag(String fieldTag) {
		this.fieldTag = fieldTag;
	}


	public String getXmlRootTag() {
		return xmlRootTag;
	}


	public void setXmlRootTag(String xmlRootTag) {
		this.xmlRootTag = xmlRootTag;
	}


    public boolean isLogMode() {
        return isLogMode;
    }

    public void setLogMode(String isLogMode) {
        if(isLogMode==null) return;
        this.isLogMode = StringUtil.getBoolean(isLogMode,false);
    }
    
    public void setLogMode(boolean isLogMode) {
        this.isLogMode = isLogMode;
    }

	public String getLogLevel() {
		return logLevel;
	}


	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}


        
   
}// end of MessageStructure.java