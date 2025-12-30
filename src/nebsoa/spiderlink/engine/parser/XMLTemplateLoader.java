/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.engine.parser;

import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import nebsoa.common.log.LogManager;
import nebsoa.spiderlink.engine.message.Loadable;
import nebsoa.spiderlink.engine.message.MessageParseException;
import nebsoa.spiderlink.engine.message.MessageStructure;
import nebsoa.spiderlink.engine.message.MessageStructurePool;
import nebsoa.spiderlink.engine.message.MessageStructureUtil;
import nebsoa.spiderlink.engine.message.MessageType;
import freemarker.cache.TemplateLoader;

/*******************************************************************
 * <pre>
 * 1.설명 
 *  FreeMaker 형식의 XML 전문 템플릿의 로딩/캐쉬를 담당하는 클래스
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
 * $Log: XMLTemplateLoader.java,v $
 * Revision 1.1  2018/01/15 03:39:49  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:38  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:23  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:27  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:51  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:19  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:00  안경아
 * *** empty log message ***
 *
 * Revision 1.5  2006/08/11 07:50:55  김승희
 * Message Structure 로딩 방법 수정 -> 건별 로딩
 *
 * Revision 1.4  2006/06/28 01:43:25  김승희
 * exception 처리, log 수정
 *
 * Revision 1.3  2006/06/21 12:26:53  김승희
 * 주석 추가
 *
 * Revision 1.2  2006/06/17 10:12:09  김승희
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public class XMLTemplateLoader implements TemplateLoader, Loadable{
    
	private final Map templates = new HashMap();
    protected XMLTemplateMaker  myXMLTemplateMaker = new XMLTemplateMaker();
    
    public XMLTemplateLoader(){
    	super();
    	//MessageStructure가 로딩될 때 따라서 로딩되는 객체 리스트에 넣는다.
    	MessageStructureUtil.getInstance().getMessageStructureDependentList().add(this);
    }
    
    /* 
     * 해당 Message Structure의 템플릿을 새로 생성한다.
     * (non-Javadoc)
     * @see nebsoa.spiderlink.engine.message.Loadable#load(java.lang.String)
     */
    public void load(MessageStructure structrue){
    	//메시지타입이 XML인 경우 템플릿을 다시 생성한다.
    	if(structrue!=null && structrue.getMessageType().getType().equals(MessageType.XML_STRING)){
    		makeTemplate(structrue.getStructureId());
    	}    	
    }
    
    /**
     * 전체 템플릿을 로딩한다.
     */
/*    public void loadAll(){
    	LogManager.debug(" == XML 템플릿 생성 및 로딩 시작 == ");
    	MessageStructurePool messageStructurePool = MessageStructurePool.getInstance();
    	Iterator structureIDIterator = MessageStructurePool.getInstance().getMessageStructureIDIterator();
    	if(structureIDIterator==null) return;
        String structureID = null;
        while(structureIDIterator.hasNext()){
        	
        	structureID = (String)structureIDIterator.next();
        	//MessageType이 XML인 경우만 템플릿을 만들어 보관한다.
        	if( messageStructurePool.getMessageStructure(structureID).getMessageType()==MessageType.XML)
        		//exception catch를 structureID별로 하여 하나의 structure에서 템플릿 생성 오류가 발생하더라도
        		//다른  structure 템플릿 생성에 영향을 주지 않도록 한다.
        		try{
        			putTemplate(structureID, myXMLTemplateMaker.makeTemplate(structureID));
        		}catch(MessageParseException e){
        			LogManager.error(structureID + " XML 템플릿 생성 오류 ", e);
        		}
        }
        LogManager.debug(" == XML 템플릿 생성 및 로딩 완료 == ");
    }*/
    
    public void putTemplate(String name, String templateSource) {
        putTemplate(name, templateSource, System.currentTimeMillis());
    }
    
    public void putTemplate(String name, String templateSource, long lastModified) {
        templates.put(name, new StringTemplateSource(name, templateSource, lastModified));
    }
    
    public void closeTemplateSource(Object templateSource) {
    }
    
    public Object findTemplateSource(String name) {
    	
    	Object template = templates.get(name);
    	
    	//템플릿이 없으면 생성한다.
    	if(template==null){
    		makeTemplate(name);
    		template = templates.get(name);
    	}
    	return template;
    }
    
    /**
     * XML 템플릿을 생성하여 풀에 넣는다.
     * @param name 템플릿 이름
     * @return 생성한 XML 템플릿 스트링
     */
    public Object makeTemplate(String name){
    	
    	String template = null;
    	try{
    		template = myXMLTemplateMaker.makeTemplate(name);
    		putTemplate(name, template);
    	}catch(MessageParseException e){
			LogManager.error(name + " XML 템플릿 생성 오류 ", e);
		}
    	return template;
    }
    
    /**
     * 해당 템플릿을 풀로부터 제거한다.
     * @param name 템플릿 이름
     */
    public void removeTemplate(String name){
    	templates.remove(name);
    }
    
    public long getLastModified(Object templateSource) {
        return ((StringTemplateSource)templateSource).lastModified;
    }
    
    public Reader getReader(Object templateSource, String encoding) {
        return new StringReader(((StringTemplateSource)templateSource).source);
    }
    
    private static class StringTemplateSource {
        private final String name;
        private final String source;
        private final long lastModified;
        
        StringTemplateSource(String name, String source, long lastModified) {
            if(name == null) {
                throw new IllegalArgumentException("name == null");
            }
            if(source == null) {
                throw new IllegalArgumentException("source == null");
            }
            if(lastModified < -1L) {
                throw new IllegalArgumentException("lastModified < -1L");
            }
            this.name = name;
            this.source = source;
            this.lastModified = lastModified;
        }
        
        public boolean equals(Object obj) {
            if(obj instanceof StringTemplateSource) {
                return name.equals(((StringTemplateSource)obj).name);
            }
            return false;
        }
        
        public int hashCode() {
            return name.hashCode();
        }
    }
}
