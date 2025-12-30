/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.collection;

import java.io.Serializable;

import nebsoa.common.util.DataMap;
import nebsoa.spiderlink.engine.message.MessageField;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateSequenceModel;

/*******************************************************************
 * <pre>
 * 1.설명 
 * FreeMarker에서 사용할 DataSet 클래스
 * 
 * 
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
 * $Log: FreeMarkerDataSet.java,v $
 * Revision 1.1  2018/01/15 03:39:52  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:08  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:25  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:28  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:51  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:18  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:39:05  안경아
 * *** empty log message ***
 *
 * Revision 1.4  2006/06/19 04:10:16  안경아
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class FreeMarkerDataSet extends DataSet implements TemplateHashModel, TemplateSequenceModel, Serializable {
	
	//private DataSet dataSet;
	
	public FreeMarkerDataSet(DataSet dataSet){
		super(dataSet);
		//this.dataSet = dataSet;
	}
	
	public TemplateModel get(int index) throws TemplateModelException {
		super.next();
		super.absolute(index+1);
		DataMap datamap = super.toDataMap();

		return new SimpleHash(datamap);
		
	}

	public int size(){
		return super.getRowCount();
		
	}
	
	/**
	 * DataSet을 FreeMarkerDataSet 타입으로 바꾸어 리턴한다. 
	 * DataSet안에 DataSet이 들어있는 경우도 모두 FreeMarkerDataSet 바꾸어 준다.
	 * @param dataSet
	 * @return TemplateModel
	 */
	public static TemplateModel transform(DataSet dataSet) {
		if(dataSet!=null){
			dataSet.beforeFirst();
			while(dataSet.next() ){
				DataSet nestedDataSet = null;
				Object[] data = dataSet.findObjectArray();
				for(int i=0; i<data.length; i++){
					if(data[i] instanceof DataSet){
						nestedDataSet = (DataSet)data[i];
						transform(nestedDataSet);
						data[i] = new FreeMarkerDataSet(nestedDataSet);
					}
				}
			}
			return  new FreeMarkerDataSet(dataSet);
		}else{
			return TemplateModel.NOTHING;
		}
		//return  new FreeMarkerDataSet(dataSet);
		
	}
	
	public String toString(){
		return this.getClass().getName()+":"+super.toString();
	}

	/* 현재 row에서 인자 key에 해당하는 value를 리턴한다.
	 * @see freemarker.template.TemplateHashModel#get(java.lang.String)
	 */
	public TemplateModel get(String key) throws TemplateModelException {

		Object obj = super.getObject(key);
		
		if(obj==null){
			return TemplateModel.NOTHING;
		}else if(key.startsWith(MessageField.BEGIN_LOOP) && obj instanceof FreeMarkerDataSet){
			return (FreeMarkerDataSet)obj;
		}else{
			return new SimpleScalar(obj.toString());
		}
		
	}

	public boolean isEmpty() throws TemplateModelException {
		if(super.getRowCount()<=0) return true;
		else return false;
	}
}
