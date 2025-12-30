/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.engine.message;

import java.util.ArrayList;

import nebsoa.common.log.LogManager;
import nebsoa.common.util.StringUtil;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 반복 필드를 나타내는 클래스
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
 * $Log: LoopField.java,v $
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
 * Revision 1.1  2008/01/22 05:58:20  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.5  2008/01/10 03:25:42  안경아
 * *** empty log message ***
 *
 * Revision 1.4  2008/01/09 09:59:28  안경아
 * 루프count를 얻어오기 위하여 변경
 *
 * Revision 1.3  2007/12/18 08:30:51  김승희
 * CODE_MAPPING_YN 필드 추가
 *
 * Revision 1.2  2007/12/12 09:46:47  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2007/11/26 08:38:04  안경아
 * *** empty log message ***
 *
 * Revision 1.24  2007/03/09 06:04:51  김승희
 * field type 추가
 *
 * Revision 1.23  2007/03/07 11:47:40  김승희
 * loop 필드의 타입이 BINARY_FIELD이면 그대로 유지 그게 아니면 NUM_FIELD 타입으로 넣는다.
 * 원래는 무조건 NUM_FIELD 타입으로 넣게 되어 있었음.
 *
 * Revision 1.22  2006/10/19 13:34:18  이종원
 * field log mode 로딩 추가
 *
 * Revision 1.21  2006/06/20 12:24:26  김승희
 * required_yn 속성 추가
 *
 * Revision 1.20  2006/06/17 10:12:09  김승희
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public class LoopField extends MessageField {
    
	/**
     * 자식  필드 목록
     */
    ArrayList childList ;
       
    /**
     * 최대 반복 건수.
     */
    private int maxOccurs;

   
    public LoopField(String name, String dataType, String fieldType, int length, int scale, int sortOrder, int offset,
            String defaultValue, String testValue, String remark,
            String filler, String useMode, String align, 
            String codeGroup, String fieldTag, boolean required,boolean logMode,boolean isCodeMapping) {

    	//2007. 3. 7 김승희 수정
    	//loop 필드의 데이터 타입이 BINARY_FIELD이면 그대로 유지 그게 아니면 NUM_FIELD 타입으로 넣는다.
    	//원래는 무조건 NUM_FIELD 타입으로 넣게 되어 있었음.
    	
    	super(name, (BINARY_FIELD.equals(dataType)?dataType:NUM_FIELD), fieldType, length, scale, sortOrder, offset, defaultValue, testValue, remark, filler, 
                useMode, align, codeGroup, fieldTag, required,logMode,isCodeMapping);
    	
        if(StringUtil.isNull(defaultValue)) maxOccurs = 0;
        else {
        	try{
        		maxOccurs = Integer.parseInt(defaultValue);
        	}catch(NumberFormatException e){
				LogManager.debug("_loopField에 숫자가 아닌 값:"+defaultValue);
				maxOccurs = 0;
			}        		
        }
        childList = new ArrayList();
    }
    
    public String toString(){
        return "Loop Field == sortOrder["+sortOrder+"]Name["+name+"] length["+length+"]offset["+offset+"]"
        +"Child Field : "+childList.size()+":"+childList;
    }
    
    /**
     * 자식 필드를 등록 한다.
     * @param field
     */
    public void addChild(MessageField field){
        if(field == null) {
            LogManager.debug("Input Null child...");
            return;
        }
        childList.add(field);
        field.setParent(this);
    }
    /**
     * 자식 엘리먼트 갯수를 리턴
     * @return
     */
    public int getChildCount(){
        return childList.size();
    }
    
    public MessageField getChild(int i) {
        return (MessageField) childList.get(i);
    }

    /**
     * 최대 반복 건수
     * @return
     */
    public int getMaxOccurs() {
        return maxOccurs;
    }

    public int sumMaxOccurs(int totalMaxOccurs){
    	
    	if(this.getParent()!=null) totalMaxOccurs+=this.getParent().sumMaxOccurs(totalMaxOccurs);
    	totalMaxOccurs+=this.getMaxOccurs();
    	return totalMaxOccurs;
    }
    
    
    /**
     * 최대 반복 건수
     * @param maxOccurs
     */
    public void setMaxOccurs(int maxOccurs) {
        this.maxOccurs = maxOccurs;
    }

	public ArrayList getChildList() {
		return childList;
	}

	public void setChildList(ArrayList childList) {
		this.childList = childList;
	}
	
	public MessageField getMessageFieldByFieldTag(String fieldag){
		int cnt = this.getChildCount();
		MessageField field = null;
		MessageField retField = null;
		for(int i=0; i<cnt; i++){
			field = this.getChild(i);
			if(fieldag.equals(field.getFieldTag())) return field;
			else if(field instanceof LoopField){
				retField = ((LoopField)field).getMessageFieldByFieldTag(fieldag);
				if(retField!=null) {
					return retField;
				}
			}
		}
		return null;
	}
	
	public MessageField getMessageFieldByName(String name){
		int cnt = this.getChildCount();
		MessageField field = null;
		MessageField retField = null;
		for(int i=0; i<cnt; i++){
			field = this.getChild(i);
			
			if(name.equals(field.getName())){
				return field;
			}
			else if(field instanceof LoopField){
				retField = ((LoopField)field).getMessageFieldByName(name);
		
				if(retField!=null) {
					return retField;
				}
			}
		}
		return null;
	}
}// end of MessageField.java