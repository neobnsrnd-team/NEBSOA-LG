/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.monitor;

import nebsoa.common.log.LogManager;
import nebsoa.common.util.ExceptionTracer;
import nebsoa.common.util.FormatUtil;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 특정 시간 이상 수행되거나, close(반납)하지 않는 자원에 대한 관리, 모니터링을 위한 클래스
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
 * $Log: MonitoredObject.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:29  cvs
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
 * Revision 1.1  2008/08/04 08:54:55  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:33  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:44  안경아
 * *** empty log message ***
 *
 * Revision 1.5  2006/07/28 05:19:28  이종원
 * ExceptionTracer적용
 *
 * Revision 1.4  2006/06/17 10:37:45  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class MonitoredObject {
    public long startTime = 0;
    public String category = null;
    public String info = null;
    public String sql = null;
    
    public int stackIndex=5;
    
    protected boolean isClosed;
    Exception ex;
    /**
     *로그를 남길지 여부를 판단하는 threshold : 밀리세컨드 단위로 세팅한다.
     */
    public long threshold=3000;
    
    public MonitoredObject(long startTime, String category, String info, 
    		String sql,int stackIndex) {
        this.startTime = startTime;
        this.category = category;
        this.info = info;
        this.sql = sql;
        this.stackIndex = stackIndex;
        
        ex = new Exception();
    }
	public MonitoredObject( long startTime ,String category, String info, String sql) {
		this(startTime,category,info,sql,5);
	}    
	public MonitoredObject( String category, String info, String sql) {
		this(System.currentTimeMillis(),category,info,sql,5);
	}
	
	public MonitoredObject( String category, String info) {
		this(System.currentTimeMillis(),category,info,"",5);
	}
	public MonitoredObject( String category, String info,String sql,int stackIndex) {
		this(System.currentTimeMillis(),category,info,sql,stackIndex);
	}
	public MonitoredObject( String category, String info,int stackIndex) {
		this(System.currentTimeMillis(),category,info,"",stackIndex);
	}
    
    public int hashCode(){
    	return (category+info+sql).hashCode();
    }
    
    public boolean equals(Object obj){
    	if(obj == null ) return false;
    	if(obj instanceof MonitoredObject){
    		return this.hashCode() == obj.hashCode();
    	}
    	return false;
    }
    
	public void finalize(){
		if(!isClosed){
			LogManager.error("MONITOR",FormatUtil.getFormattedDate(startTime,"yyyy-MM-dd hh:mm:ss")
				+"에 다음 부분에서 얻은 ["+category+"의 "+info+"]자원을 반납안함.\r\n[심각]"
					+getCaller());			
		}
	}
    
    
    
	/**
	 * @return
	 */
	public boolean getClosed() {
		return isClosed;
	}

	/**
	 * @param b
	 */
	public void setClosed(boolean b) {
		isClosed = b;
	}
	
	/**
	 * @param b
	 */
	public void closeResource() {
		isClosed = true;
		long endTime = System.currentTimeMillis();
		if(endTime-startTime > threshold){
			LogManager.error("MONITOR","[심각]"+FormatUtil.getFormattedDate(startTime,
			"yyyy-MM-dd hh:mm:ss")+"초에  ["+category+"의 "+info+"]자원 사용시간 : " +((endTime-startTime)/1000.0)+"초\n"
			+getCaller()+"\n"+sql);
		}else{
			//LogManager.debug(getCaller()+"에서 ["+category+"의 "
			//			+info+"]자원 사용시간 : " +((endTime-startTime)/1000.0)+"초");
		}
	}
	
	private String getCaller(){
		return ExceptionTracer.getTraceString(ex, stackIndex);
	}

    /**
     * @return Returns the threshold.
     */
    public long getThreshold() {
        return threshold;
    }
    /**
     * @param threshold The threshold to set.
     */
    public void setThreshold(long threshold) {
        this.threshold = threshold;
    }
}