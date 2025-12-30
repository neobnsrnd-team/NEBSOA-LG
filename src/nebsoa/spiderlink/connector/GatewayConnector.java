/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
package nebsoa.spiderlink.connector;

import java.util.NoSuchElementException;

import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogConstants;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.StringUtil;
import nebsoa.spiderlink.connector.constants.ConnectorConstants;
import nebsoa.spiderlink.connector.constants.RunningModeType;
import nebsoa.spiderlink.connector.constants.SyncType;
import nebsoa.spiderlink.context.Gateway;
import nebsoa.spiderlink.context.MessageContext;
import nebsoa.spiderlink.engine.Lifecycle;
import nebsoa.spiderlink.exception.ConnectException;

/*******************************************************************
 * <pre>
 * 1.설명 
 * Gateway(시스템 그룹)에 딸려 있는 system 정보를 담는  클래스
 * Gateway안에 운영(R), 개발(D), TEST(T)시스템이 있고, 그 안에 
 * 여러 IP/Port로 서비스 되고 있는 시스템 하나식에 대하여 객체가 만들어 진다. 
 * (예: host접근시 eai Gateway를 거치며, 
 * 운영서버 2대, 개발 서버 1대, 테스트 서버 1대
 * 라고 한다면 4개의 instance가 만들어 진다.
 * 여기서 의미하는 객수는 서버 갯수가 아니라 대상 시스템의 ip/port의 갯수이다.
 * 하나의 서버에서 여러 port에서 서비스 한다면 그에 따라 GatewaySystem이 만들어진다.
 * Listener인 경우도 마찬가지 이다. 
 * 내부 시스템이 해당 gateway로 부터 메시지를 수신 하는 경우에 Listener를 만드는데, 
 * 그 listener가 이 클래스로 표현 된다. 
 * 2.사용법
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
 * $Log: GatewayConnector.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:34  cvs
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
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.2  2008/03/04 07:39:07  김승희
 * systemId 추가
 *
 * Revision 1.1  2008/01/22 05:58:34  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:39:08  안경아
 * *** empty log message ***
 *
 * Revision 1.39  2007/02/21 06:32:01  이종원
 * getProperty(key, defaultValue)
 * getIntProperty(key, defaultValue) 메소드 추가
 *
 * Revision 1.38  2007/02/21 02:07:05  김승희
 * keb.fwk.spiderlink.exception.ConnectException -->
 * nebsoa.spiderlink.exception.ConnectException 로 수정
 *
 * Revision 1.37  2007/01/08 05:28:18  이종원
 * *** empty log message ***
 *
 * Revision 1.36  2007/01/08 02:23:06  안경아
 * *** empty log message ***
 *
 * Revision 1.35  2006/11/10 05:13:33  이종원
 * stopped 상태인 경우의 처리 추가
 *
 * Revision 1.34  2006/11/10 04:12:53  이종원
 * LOG CONFIG수정
 *
 * Revision 1.33  2006/11/10 01:57:48  이종원
 * LOG를 MSG_ERR에서 DEBUG로 변경
 *
 * Revision 1.32  2006/10/13 12:09:46  이종원
 * getLogConfig()추가
 *
 * Revision 1.31  2006/10/13 05:40:26  이종원
 * log 설정 파일 변경
 *
 * Revision 1.30  2006/10/13 05:31:02  이종원
 * *** empty log message ***
 *
 * Revision 1.29  2006/10/13 05:30:47  이종원
 * *** empty log message ***
 *
 * Revision 1.28  2006/10/13 02:48:58  이종원
 * *** empty log message ***
 *
 * Revision 1.27  2006/10/03 09:12:44  이종원
 * refactoring
 *
 * Revision 1.26  2006/10/03 03:09:31  이종원
 * *** empty log message ***
 *
 * Revision 1.25  2006/10/02 20:08:27  이종원
 * *** empty log message ***
 *
 * Revision 1.24  2006/10/02 20:06:02  이종원
 * usePooling메소드 구현
 *
 * Revision 1.23  2006/10/02 20:05:41  이종원
 * usePooling메소드 구현
 *
 * Revision 1.22  2006/10/02 01:56:26  이종원
 * *** empty log message ***
 *
 * Revision 1.21  2006/10/02 01:55:25  이종원
 * *** empty log message ***
 *
 * Revision 1.20  2006/09/28 02:18:00  김승희
 * *** empty log message ***
 *
 * Revision 1.19  2006/09/28 01:28:41  김승희
 * NoSuchElementException 처리 --> ConnectException으로 다시 던진다.
 *
 * Revision 1.18  2006/09/27 13:21:49  이종원
 * *** empty log message ***
 *
 * Revision 1.17  2006/09/27 13:02:55  이종원
 * *** empty log message ***
 *
 * Revision 1.16  2006/09/27 12:29:11  이종원
 * *** empty log message ***
 *
 * Revision 1.15  2006/09/27 11:32:10  이종원
 * *** empty log message ***
 *
 * Revision 1.14  2006/09/27 11:13:50  이종원
 * *** empty log message ***
 *
 * Revision 1.13  2006/09/27 07:40:46  이종원
 * update
 *
 * Revision 1.12  2006/09/26 08:25:32  이종원
 * setStopped에서 init or destroy호출 하게 구현
 *
 * Revision 1.11  2006/09/19 06:10:27  김승희
 * property trim
 *
 * Revision 1.10  2006/09/19 02:15:33  김승희
 * isError(장애여부) 변수 추가
 *
 * Revision 1.9  2006/09/16 13:10:55  이종원
 * 어느정도 완성된 초안 작성
 *
 * Revision 1.8  2006/09/16 09:47:36  이종원
 * iotype, reqres구분 삭제
 *
 * Revision 1.7  2006/09/16 09:44:33  이종원
 * update
 *
 * Revision 1.6  2006/09/16 07:06:42  김승희
 * *** empty log message ***
 *
 * Revision 1.5  2006/09/16 06:41:51  이종원
 * execute정의
 *
 * Revision 1.4  2006/09/16 06:37:26  이종원
 * set/get정리
 *
 * Revision 1.3  2006/09/16 06:35:27  이종원
 * toString구현
 *
 * Revision 1.2  2006/09/16 06:28:50  이종원
 * 상속이 용이 하도록 default 생성자로 생성자 변경
 *
 * Revision 1.1  2006/09/16 04:32:52  이종원
 * 이동
 *
 *
 * </pre>
 ******************************************************************/
public abstract class GatewayConnector implements Lifecycle{
	/**
     * 해당 System의 Gateway  
	 */
    protected Gateway owner;
    
    protected RunningModeType runningMode;
    
    /**
     * 중지 여부 . default valus is true(stopped)
     */
    protected boolean stopped=true;
    
    /**
     * 장애 여부
     */
    protected boolean isError;
    
    /**
     * ip
     */
    protected String ip;
    
    /**
     * port.
     */
    protected int port;
    
    /**
     * 커넥터의 System ID(FWK_SYSTEM 테이블의 SYSTEM_ID)
     */
    protected String systemId;
    
    /**
     * LogFile명
     */
    protected String logConfig=LogConstants.SPIDERLINK;
	
	
    //MessageProcessorFactory factory;
    /**
     * 생성자.
     * owner, 기동 수동 구분, 요구 응답 구분, 운영/개발/테스트 구분, 정지 여부
     */
	public GatewayConnector(){
    }
    
    boolean initialized=false;
    /**
     * Listener인지 Adapter인지 판단하여 해당 하는 로직 수행.
     * 2006. 9. 15.  KOREAN 작성
     */
    public void initialize(Gateway owner,   
            RunningModeType mode,
            String ip,
            int port,
            boolean stopped){
        this.owner = owner;
        this.runningMode = mode;
        this.stopped=stopped;
        this.ip=ip;
        this.port=port;
        
        if(owner == null){
            throw new SysException("No Gateway info ");
        }
        
        if(runningMode == null){
            throw new SysException("No Running Mode info ");
        }
        
        initialized=true;
        
        if(isStopped()){
            LogManager.info(toString()+"GatewayConnector는  중지 상태 입니다.");
            return;
        }
        
        init();
    }    
    
    
    public abstract void init();
    
    
    public void handleMessage(MessageContext ctx) throws Exception{
        if(isStopped()){
            LogManager.error(toString()+" GatewayConnector는  중지 상태 입니다.");
            ctx.setResult(
                    new SysException("FRM20007",
                            "해당 시스템 GatewayConnector는  중지 상태 입니다.\n"+toString()
                            +"\n설정을 확인하거나, 해당 시스템의 상태를 확인 하시기 바랍니다 "));
            return;
        }
        long now = System.currentTimeMillis();
        try{
        	execute(ctx);
        	
        //ksh : 소켓풀로부터 소켓을 얻지 못한 경우 connectException을 던진다.
        }catch(NoSuchElementException e){
            long end = System.currentTimeMillis();
        	throw new ConnectException(e.getMessage()
                    +"\n >>> (해당 시스템("+getOwner().getGwName()+")으로의 요청이 많거나,Gateway 설정 정보에 \n >>> Thread갯수가 값이 너무 적을 수 있습니다-현재 설정된 쓰레드 갯수 :"
                    +getOwner().getThreadCount()
                    +",Wait Time="+((end-now)/1000.0)+"초)", e);	
        }
    }
    
    /**
     * GatewayConnector를 상속 받아 구현 된 클래스가
     * 메시지 처리 로직을 구현할 부분
     * 2006. 9. 16.  이종원 작성
     * @param ctx
     */
    protected void execute(MessageContext ctx) 
        throws Exception{
        throw new SysException("Not Implemented Exception ");
    }
 	/**
	 * key값으로 속성에 등록되어 있는 해당 value를 리턴한다.
     * 내부적으로 owner(GateWay)속성을 return 한다.
	 * @param key
	 * @return key에 해당하는 value
	 */
	public String getProperty(String key){
		String value = owner.getProperty(key);
		if(value!=null) value = value.trim();
		return value;
	}
    
    /**
     * key값으로 속성에 등록되어 있는 해당 value를 리턴한다.
     * 내부적으로 owner(GateWay)속성을 return 한다.
     * 해당 값이 없으면 defaultValue를 리턴한다.
     * @param key
     * @param defaultValue
     * @return key에 해당하는 value. null이면 defaultValue return
     */
    public String getProperty(String key, String defaultValue){
        String value = getProperty(key);
        if(!StringUtil.isNull(value)) return value;
        return defaultValue;
    }
    
    public String getSystemId() {
		return systemId;
	}


	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}
	
	
    /**
     * key값으로 속성에 등록되어 있는 해당 value를 int형태 리턴한다.
     * @param key
     * @return key에 해당하는 value
     */
    public int getIntProperty(String key){
        return Integer.parseInt(getProperty(key));
    }
    
    /**
     * key값으로 속성에 등록되어 있는 해당 value를 int형태 리턴한다.
     * @param key
     * @param defaultValue
     * @return key에 해당하는 value, null이면 defaultValue return
     */
    public int getIntProperty(String key, int defaultValue){
        String value = getProperty(key);
        if(!StringUtil.isNull(value)) return Integer.parseInt(value);
        return defaultValue;
    }
	
	public String getGwId() {
		return owner.getGwId();
	}

	public void setOwner(Gateway owner) {
		this.owner = owner;
	}

	public String getGwName() {
		return owner.getGwName();
	}


	public String getGwProperties() {
		return owner.getGwProperties();
	}
	
	public int getWorkerCount() {
		return owner.getThreadCount();
	}
    public String getName() {
        return toString();
    }
    public RunningModeType getRunningMode() {
        return runningMode;
    }
    public void setRunningMode(RunningModeType runningMode) {
        this.runningMode = runningMode;
    }
    public boolean isStopped() {
        return stopped;
    }
    /**
     * true를 세팅하면 destroy가 불리면서 socket pool을 close한다.
     * false를 세팅하면 init()이 호출 된다.
     * 2006. 9. 26.  이종원 작성
     * @param stopped
     */
    public void setStopped(boolean stopped) {
        this.stopped = stopped;
        if(stopped==true){
            destroy();
        }else{
            init();
        }
    }
    
    public void stop(){
        stopped=true;
    }
    
    public Gateway getOwner() {
        return owner;
    }
    
    public String toString(){
        try{
            if(!isError()){
                return "Gateway:"+owner.getGwName()
                +"IP:"+ip+",Port:"+port +",중지여부 :"+stopped
                +",장애여부 :"+isError()
                +",Class:"+getClass().getName();
            }else{
                return ">>>>ERROR!!상태>>> Gateway:"+owner.getGwName()
                +"IP:"+ip+",Port:"+port +",중지여부 :"+stopped
                +",장애여부 :"+isError()
                +",Class:"+getClass().getName();
            }
        }catch(Exception e){
            return "";
        }
    }
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public int getPort() {
        return port;
    }
    public void setPort(int port) {
        this.port = port;
    }


    public boolean isInitialized() {
        return initialized;
    }


    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }
    /**
     * System connector는 ip/port에 unique하므로 ip+"_"+port값을 리턴하도록 구현.
     * 2006. 9. 16.  이종원 작성
     * @return  ip+"_"+port
     */
    public String getId() {
        
        return getIp()+"_"+getPort();
    }


	public boolean isError() {
        //LogManager.infoLP(this+"의 IS ERROR:"+isError);
		return isError;
	}


	public void setError(boolean isError) {
//        LogManager.error("setError("+isError+")", new Exception("setError call"));
		this.isError = isError;
	}
    
    public boolean usePooling(){
        if(!isInitialized()){
            throw new SysException("Not initialized..");
        }
        return getOwner().getPropertyMap().getBoolean(
                ConnectorConstants.USE_POOLING);
    }
    
    public boolean isSync(){
        if(!isInitialized()){
            throw new SysException("Not initialized..");
        }
        return SyncType.isSync(getOwner().getPropertyMap().getString(
                ConnectorConstants.IO_PROTOCOL));
    }
    
    public boolean isAsync(){
        if(!isInitialized()){
            throw new SysException("Not initialized..");
        }
        return SyncType.isAsync(getOwner().getPropertyMap().getString(
                ConnectorConstants.IO_PROTOCOL));
    }
    
    public boolean isNoAck(){
        if(!isInitialized()){
            throw new SysException("Not initialized..");
        }
        return SyncType.isNoAck(getOwner().getPropertyMap().getString(
                ConnectorConstants.IO_PROTOCOL));
    }


    public String getLogConfig() {
        return logConfig;
    }


    public void setLogConfig(String logConfig) {
        this.logConfig = logConfig;
    }

}
