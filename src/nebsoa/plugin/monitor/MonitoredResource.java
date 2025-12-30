package nebsoa.plugin.monitor;

import nebsoa.plugin.monitor.util.ExceptionFormatter;
import nebsoa.plugin.monitor.util.MonitorLogger;



public class MonitoredResource {
    
    public static String logConfig=null;
    public long startTime = 0;
    public String category = null;
    public String info = null;
    public String sql = null;
    
    public int stackIndex=5;
    
    protected boolean isClosed;
    ResourceLeakException ex;
    /**
     *로그를 남길지 여부를 판단하는 threshold : 밀리세컨드 단위로 세팅한다.
     */
    public long threshold=1000;
    
    public MonitoredResource(long startTime, String category, String info, 
    		String sql,int stackIndex) {
        logConfig = System.getProperty("SPIDER_MONITOR_LOG","MONITOR");
        this.startTime = startTime;
        this.category = category;
        this.info = info;
        this.sql = sql;
        this.stackIndex = stackIndex;
        
        ex = new ResourceLeakException(category,info+":HashCode("+this.hashCode()+")");
    }
	public MonitoredResource( long startTime ,String category, String info, String sql) {
		this(startTime,category,info,sql,5);
	}    
	public MonitoredResource( String category, String info, String sql) {
		this(System.currentTimeMillis(),category,info,sql,5);
	}
	
	public MonitoredResource( String category, String info) {
		this(System.currentTimeMillis(),category,info,"",5);
	}
	public MonitoredResource( String category, String info,String sql,int stackIndex) {
		this(System.currentTimeMillis(),category,info,sql,stackIndex);
	}
	public MonitoredResource( String category, String info,int stackIndex) {
		this(System.currentTimeMillis(),category,info,"",stackIndex);
	}
    
    public int hashCode(){
    	return (category+info+sql).hashCode();
    }
    
    public boolean equals(Object obj){
    	if(obj == null ) return false;
    	if(obj instanceof MonitoredResource){
    		return this.hashCode() == obj.hashCode();
    	}
    	return false;
    }
    
	public void finalize(){
		if(!isClosed){
			error(category+"의 "+info+" 자원을 반납안함.",ex);
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
	 * closeResource
	 */
	public void closeResource() {
		try {
            isClosed = true;
            long endTime = System.currentTimeMillis();
            if(endTime-startTime > threshold){
            	info(info+"자원 사용시간 : " +((endTime-startTime)/1000.0)
                        +"초\n "+getCaller());
            }
        } catch (RuntimeException e) {
//            e.printStackTrace();
        }
	}
	
    private String getCaller(){
        return ExceptionFormatter.getTraceString(new Exception("자원사용시간초과모니터"), stackIndex);
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
    
    public static void info(Object obj){
        MonitorLogger.info(logConfig,obj==null?"null":obj.toString());
    }
    
    public static void error(Object obj){
        MonitorLogger.error(logConfig,obj==null?"null":obj.toString());
    }
    
    public static void info(Object obj,Throwable t){
        MonitorLogger.error(logConfig,obj==null?"null":obj.toString(),t);
    }
    
    public static void info(Exception t){
        MonitorLogger.error(logConfig,t.toString(),t);
    }
    
    public static void error(Object obj,Throwable t){
        MonitorLogger.error(logConfig,obj==null?"null":obj.toString(),t);
    }
    
    public static void info(double obj){
        MonitorLogger.info(logConfig,obj+"");
    }
    
    public static void error(double obj){
        MonitorLogger.error(logConfig,obj+"");
    }
    
    public static String getSimpleString(String sql){
        if(sql==null) return "( null sql...)";
        if(sql.length()>60){
            return sql.substring(0,60)+"...";
        }else{
            return sql;
        }
    }
}