/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */


package nebsoa.util.async;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.PropertyManager;

/*******************************************************************
 * <pre>
 * 1.설명 
 * <code>AsyncQueueManager</code>는 시스템에서 사용 하는 모든 thread config를 관리합니다.
 * <code>AsyncQueueManager</code>는
 * 
 * <code>AsyncQueueManager</code>의 getconfig("key이름")을 이용하여
 * config객체를  얻을 수 있습니다. 
 * 
 * 2.사용법
 * <blockquote>
 * 
 *  AsyncQueue myQueueConfig = AsyncQueueManager.getInstance().getconfig("myQueueConfig");
 *  if(myQueueConfig==null){
 *      myQueueConfig = AsyncQueueManager.getInstance().makeAsyncQueue(name, queue갯수, queue size, 
 *      consumerClassName, timeout, consumerPolling주기);
 *  }
 *  
 *  만약 등록된  AsyncQueue이 없을 시자동  생성하게 하려면 
 *  AsyncQueue myQueueConfig = AsyncQueueManager.getInstance().getconfig("myQueueConfig",true);
 * </blockquote>
 * 
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author 이종원
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 
 * $Log: AsyncQueueManager.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:25  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:26  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:29  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:54  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.2  2008/07/01 08:33:44  이종원
 * 주석정리
 *
 * Revision 1.1  2008/02/26 01:40:53  이종원
 * 최초작성
 *
 * </pre>
 ******************************************************************/
public class AsyncQueueManager {
	
    private static final String PROPERTY_NAME = "queue";
	
    static Object dummy = new Object();
    public static AsyncQueueManager instance=null;
    /**
     * AsyncQueue을  캐쉬 해 놓기 위한 HashMap입니다. 
     */
    private HashMap AsyncQueueHash;
    
    
    /**
     * 싱글톤으로 처리되도록 구현한 메소드
     * 2002. 10. 21.  이종원 작성
     * @return
     */
    public static AsyncQueueManager getInstance(){
        if(instance==null){
            synchronized(dummy){
                instance = new AsyncQueueManager();
            }
        }
        return instance;
    }
    
    private AsyncQueueManager(){
        if(AsyncQueueHash != null){
            try{
                closeAll();
            }catch(Exception e){}
        }
        AsyncQueueHash = getAsyncQueueHash();
        
        prepareAll();
    }

    
    private void prepareAll() {
/**
		if(isDBMode()){
			loadAllFromDB();
		}else{
			loadAllFromFile();
		}
**/		
	}
    
    /**
     * queue생성 정보를 db로 부터 얻을 것인지 queue.properties.xml 로 부터 얻을 것인지
     */ 
    private boolean isDBMode(){
    	return PropertyManager.getBooleanProperty(PROPERTY_NAME, "CONFIG_DB_MODE","false");
    }

	private QueueConfig loadFromFile(String configId) {
		
		int queueCount=PropertyManager.getIntProperty(PROPERTY_NAME,configId+".Queue.Count",1);
		int queueSize=PropertyManager.getIntProperty(PROPERTY_NAME,configId+".Queue.Size",256);
		long producerWaitTimeout=PropertyManager.getIntProperty(PROPERTY_NAME,configId+".Producer.Timeout",1*1000);
		long consumerWaitTimeout=PropertyManager.getIntProperty(PROPERTY_NAME,configId+".Consumer.Timeout",30*1000);
		
		long sleepTime=PropertyManager.getIntProperty(PROPERTY_NAME,configId+".Consumer.PollingInterval",0);
		boolean bulk = PropertyManager.getBooleanProperty(PROPERTY_NAME, configId+".Consumer.IsBulk","false");
		String consumerName=PropertyManager.getProperty(PROPERTY_NAME,configId+".Consumer.Class",null);
		ConsumerFactory factory=null;
		if(consumerName==null){
			String consumerFactoryName =PropertyManager.getProperty(PROPERTY_NAME,configId+".ConsumerFactory.Class",null);
			if(consumerFactoryName==null){
				throw new SysException("ConsumerClassName, 혹은 ConsumerFactoryName중에 하나는 정의 해야 합니다");
			}else{
				try {
					factory = (ConsumerFactory) Class.forName(consumerFactoryName).newInstance();
				} catch (Exception e) {
					throw new SysException(configId+" 큐의 consumerFactoryName 설정 오류 입니다."+ e.toString(),e);
				} 
			}
		}
		
		if(consumerName != null){
			return makeAsyncQueue(configId,
		    		queueCount, queueSize, producerWaitTimeout,consumerWaitTimeout, 
					sleepTime, consumerName ,bulk );
		}else{
			return makeAsyncQueue(configId,
		    		queueCount, queueSize,producerWaitTimeout, consumerWaitTimeout, 
					sleepTime, factory ,bulk );
		}
		
		
	}

	private QueueConfig loadFromDB(String configId) {
		// TODO Auto-generated method stub
		return null;
		
	}

	private HashMap getAsyncQueueHash(){
        if(AsyncQueueHash == null){
            AsyncQueueHash = new HashMap();
        }
        return AsyncQueueHash;
    }

    
    /**
     * 등록된 AsyncQueue 얻어 옵니다 .
     * <font color='red'>만약 등록된 AsyncQueue이 없을때는 null을 return 합니다.
     * 없을경우 자동 생성을 하고 싶다면 boolean인자를 받는 메소드를 사용합니다.
     * </font>
     */
    public QueueConfig getQueueConfig(String id) {
    	QueueConfig config = (QueueConfig) getAsyncQueueHash().get(id);
        if (config == null) {
            LogManager.debug(id +" queue config  is null");
        }
        return config;
    }
    /**
     * 등록된 AsyncQueue 얻어 옵니다 .
     * <font color='red'>만약 등록된 AsyncQueue이 없을때는 null을 return 합니다.
     * 없을경우 자동 생성을 하고 싶다면 boolean인자를 받는 메소드를 사용합니다.
     * </font>
     */
    public QueueConfig getQueueConfig(String id,boolean autoCreate) {
    	QueueConfig config = (QueueConfig) getAsyncQueueHash().get(id);
        if (config == null) {
            LogManager.debug(id +" queue config  is null now loading");
            if(autoCreate){
            	if(isDBMode()){
            		config = loadFromDB(id);
            	}else{
            		config = loadFromFile(id);
            	}
            }
        }
        return config;
    }

    

    
    /**
     * config을 close합니다.
     */
    public void closeAll() {
        if(AsyncQueueHash==null) return;
        
        Set set = getAsyncQueueHash().keySet();
        if (set.isEmpty())
            return;
        QueueConfig config=null;
        Object[] key = set.toArray();
        for(int i=0; i<key.length; i++){
        	config = (QueueConfig) getAsyncQueueHash().get(key[i]);
        	try {
                if(config != null && config.isRunning()) config.setRunning(false);
            } catch (Throwable e) {
                e.printStackTrace();
            }finally{
                //getAsyncQueueHash().remove(key[i]);
            }
        }
        
        try{
            getAsyncQueueHash().clear();
        }catch(Exception e){}
    }
    
    /**
     * thread config을 close합니다.
     */
    public void close(String configId) {
      
    	QueueConfig config= (QueueConfig) getAsyncQueueHash().get(configId);
        if(config != null && config.isRunning()) {       
            try {
                LogManager.infoLP(configId+"queue config 을 close합니다.");
                config.setRunning(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        getAsyncQueueHash().remove(configId);
    }
    
    /**
     * ConsumerFactory를 이용하여 AsyncQueueConfig 를 생성하는 메소드
     * @param configId key값
     * @param queueCount QueueConfig안에 생성될 queue갯수
     * @param queueSize  생성된 queue의 버퍼크기
     * @param consumerWaitTimeout consumer blocking timeout
     * @param sleepTime  폴링 주기
     * @param factory  Consumer를 생성할 factory
     * @param bulk   Consumer가 한건씩 처리할 것인지(false) 다건씩 처리(true) 할 것인지여부
     * @return
     */
    public QueueConfig makeAsyncQueue(String configId,
    		int queueCount, int queueSize,long producerWaitTimeout,
    		long consumerWaitTimeout, 
			long sleepTime, ConsumerFactory factory ,boolean bulk ) {
        if (configId == null) {
            throw new SysException("configId is null");
        }

        QueueConfig config = (QueueConfig) getAsyncQueueHash().get(configId);
 
        if (config == null) {
        	
        	if(consumerWaitTimeout<1000) LogManager.info("MONITOR",configId+"큐의 timeout값이 작습니다 >> "+consumerWaitTimeout);
        	
            config = new QueueConfig( queueCount,  queueSize, producerWaitTimeout,
            		consumerWaitTimeout, 
        			 sleepTime,  factory , bulk);
            config.setName(configId);
            getAsyncQueueHash().put(configId,config);
            LogManager.debug(configId+" AsyncQueue config을 생성하였습니다:"+config);
        }
        
        return config;
    }
    /**
     * Consumer class name을 이용하여 AsyncQueueConfig 를 생성하는 메소드
     * @param configId key값
     * @param queueCount QueueConfig안에 생성될 queue갯수
     * @param queueSize  생성된 queue의 버퍼크기
     * @param consumerWaitTimeout consumer blocking timeout
     * @param sleepTime  폴링 주기
     * @param consumerClassName consumerClassName
     * @param bulk  Consumer가 한건씩 처리할 것인지(false) 다건씩 처리(true) 할 것인지여부
     * @return
     */
    public QueueConfig makeAsyncQueue(String configId,
    		int queueCount, int queueSize,long producerWaitTimeout, 
    		long consumerWaitTimeout, 
			long sleepTime, String consumerClassName ,boolean bulk ) {
        if (configId == null) {
            throw new SysException("configId is null");
        }

        QueueConfig config = (QueueConfig) getAsyncQueueHash().get(configId);
 
        if (config == null) {
        	
        	if(consumerWaitTimeout<1000) LogManager.info("MONITOR",configId+"큐의 timeout값이 작습니다 >> "+consumerWaitTimeout);
        	
            config = new QueueConfig( queueCount,  queueSize, producerWaitTimeout,
            		consumerWaitTimeout, 
        			 sleepTime,  consumerClassName , bulk);
            config.setName(configId);
            getAsyncQueueHash().put(configId,config);
            LogManager.debug(configId+" AsyncQueue config을 생성하였습니다:"+config);
        }
        
        return config;
    }



    public void logAsyncQueueState() {
        Set set = getAsyncQueueHash().keySet();
        if (set.isEmpty())
            return;
        Iterator i = set.iterator();
        QueueConfig config=null;
        while (i.hasNext()) {
            config = (QueueConfig) getAsyncQueueHash().get(i.next());
            try {
                System.out.println(config.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }      
    }
    
    public void finalize(){
        getInstance().closeAll();
    }

}
