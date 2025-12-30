package nebsoa.spiderlink.engine.message;

import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.DataMap;
import nebsoa.common.util.PropertyManager;

public class SystemValueKeywordPoolFactory {
	
	private static Object dummy=new Object();
	
	public static SystemValueKeywordPool poolInstance = null;
	
	public static String systemKeywordPool_ClassFullName = null;
	
	public static final String SYSTEM_KEYWORD_POOL_NAME = "MESSAGE_SYSTEM_KEYWORD_POOL_NAME";
	
	static{
		systemKeywordPool_ClassFullName = PropertyManager.getProperty("default", SYSTEM_KEYWORD_POOL_NAME);
	}
	public static SystemValueKeywordPool getSystemValueKeywordPool(){
		
		if(poolInstance==null){
			
			try {
				synchronized (dummy) {
					if(systemKeywordPool_ClassFullName==null){
						throw new SysException("SystemKeywordPool의 Class Name이 지정되지 않았습니다.");
					}
					poolInstance = (SystemValueKeywordPool)Class.forName(systemKeywordPool_ClassFullName).newInstance();
				}
			} catch (Throwable e) {
				LogManager.error("SystemKeyWordPool 생성에 실패했습니다.[" + systemKeywordPool_ClassFullName +"]::" + e.toString());
				throw new SysException(e);
			}
				
		}
		
		return poolInstance;
		
	}
	private SystemValueKeywordPoolFactory(){}
	
	/**
	 * SystemKeywordPool class이름을 지정한다.
	 * @param fullClassName
	 */
	public static void setSystemKeywordPoolClassName(String fullClassName){
		systemKeywordPool_ClassFullName = fullClassName;
	}
	
	public static void main(String[] args) throws Throwable{
		SystemValueKeywordPoolFactory.setSystemKeywordPoolClassName("keb.fwk.spiderlink.message.KebSystemValueKeywordPool");
		System.out.println(getSystemValueKeywordPool().get("_$인터페이스환경").getValue(new DataMap()));
		System.out.println(getSystemValueKeywordPool().get("_$SYSDATE").getValue(new DataMap()));
		
	}
}
