/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.engine.message.proxy;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import nebsoa.common.collection.DataSet;
import nebsoa.common.util.StringUtil;
import nebsoa.spiderlink.engine.message.LoopField;
import nebsoa.spiderlink.engine.message.MessageField;
import nebsoa.spiderlink.engine.message.MessageStructure;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 대응답 XML parsing 시 Loop 데이터를 처리하는 클래스
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
 * $Log: ProxyXMLMessageParseDelegator.java,v $
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
 * Revision 1.1  2008/08/04 08:54:51  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:23  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/12/18 08:30:08  김승희
 * 최초 등록
 *
 *
 * </pre>
 ******************************************************************/
public class ProxyXMLMessageParseDelegator
{
	private String fieldName;
	private MessageStructure structure;
	private String[] fieldNames;
	private Object[] rows;
	private ArrayList dataList = new ArrayList();
	private DataSet set;
	private String rootElement;
	private String loopFieldName;
	private String loopFieldTag;
	
    ProxyXMLMessageParseDelegator(MessageStructure structure, String rootElement) {
    	this.structure = structure;
    	this.rootElement = rootElement;
    }
    
    public void clear(){
    	fieldName = null;
    	fieldNames = null;
    	rows = null;
    	set = null;
    }
    
    public void init(){
    	fieldName = null;
    	fieldNames = null;
    	rows = null;
    	set = null;
    	dataList = new ArrayList();
    }
    
    private boolean isFieldNameSetted(){
    	if(fieldNames!=null) return true;
    	else return false;
    }
    
	public void setFieldName(String name)
	{
		this.fieldName = name;
		if(!isFieldNameSetted()){
			makeFieldNames();
		}
		rows = new Object[fieldNames.length];
		
	}

	private void makeFieldNames()
	{
		
		MessageField field = null;
		LoopField loopField= null;
		for(int i=0; i<structure.size(); i++)
		{
			field = structure.getField(i);
			if(field==null) continue;
			if(field instanceof LoopField){
				if(fieldName.equals(field.getName()))
				{
					loopField= (LoopField)field;
					break;
				}else{
					loopField = ((LoopField)((LoopField)field).getMessageFieldByName(fieldName));
					if(loopField!=null) break;
				}
			}
			
		}
		List tempFiedList = new ArrayList();
		if(loopField!=null){
			MessageField child = null;
			for(int j=0; j<loopField.getChildCount(); j++)
			{
				child = loopField.getChild(j);					
				if(!StringUtil.contains(child.getName(), MessageField.END_LOOP))
				{
					tempFiedList.add(child.getName());
				}
			}
			fieldNames = new String[tempFiedList.size()];
			tempFiedList.toArray(fieldNames);

		}else{
			//TODO 이 때 exception ??
		}

	}

	public void setValue(String strXPath, Object value)
	{
		
		//String fieldName = structure.getFieldByFieldTag(StringUtils.removeStart(strXPath, "/"+rootElement)).getName();
		String fieldName = StringUtils.substringAfterLast(strXPath, "/");
						
		for(int i=0; i<fieldNames.length; i++)
		{
			if(fieldName.equals(fieldNames[i]))
			{
				rows[i] = value;
				break;
			}
		}
		
	}
    
	public void setDataSetValue(String loopFieldName, Object dataSet)
	{	
		
		for(int i=0; i<fieldNames.length; i++)
		{	

			if(loopFieldName.equals(fieldNames[i]))
			{
				rows[i] = dataSet;
				break;
			}
		}
		
	}

	public void endRow()
	{	
		/*LogManager.debug(this.getLoopFieldName()+"에 rows put");
		for(int i=0; i<rows.length; i++){
			LogManager.debug(this.getLoopFieldName() + " row["+i+"]="+rows[i]);
		}*/
		dataList.add(rows);
	}
	
	public DataSet makeDataSet()
	{		
		set = new DataSet(fieldNames, dataList);
		return set;
	}

	public String getLoopFieldName() {
		return loopFieldName;
	}

	public void setLoopFieldName(String loopFieldName) {
		this.loopFieldName = loopFieldName;
	}

	public String getLoopFieldTag() {
		return loopFieldTag;
	}

	public void setLoopFieldTag(String loopFieldTag) {
		this.loopFieldTag = loopFieldTag;
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("["+this.getLoopFieldName()+"]");
		sb.append(set!=null?set.toString():"");
		return sb.toString();
	}

}
