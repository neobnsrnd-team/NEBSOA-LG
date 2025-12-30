package nebsoa.spiderlink.socketpool;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.pool.PoolableObjectFactory;

import nebsoa.common.log.LogManager;
import nebsoa.common.util.PropertyManager;

/*******************************************************************
 * <pre>
 * 1.설명 
 * Socket를 생성하는 factory이며 org.apache.commons.pool.PoolableObjectFactory를
 * wrapping하여 사용한다.
 * 
 * 2.사용법
 * SocketFactory factory = new SocketFactory(ip,port,timeout,"DEBUG");
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
 * $Log: SocketFactory.java,v $
 * Revision 1.1  2018/01/15 03:39:51  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:58  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:24  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:28  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:51  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:24  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:37:40  안경아
 * *** empty log message ***
 *
 * Revision 1.19  2007/05/11 05:09:58  이종원
 * socket connect timeout기능 추가(default 5초)
 *
 * Revision 1.18  2007/03/23 08:34:26  최수종
 * 접근지시자 기존 소스의(default)로 수정.
 * 필요한 변수만 protected로 변경함.
 *
 * Revision 1.17  2007/03/22 09:31:57  최수종
 * 명시적으로 public 선언으로 수정
 *
 * Revision 1.16  2006/11/24 09:30:37  이종원
 * log config 수정
 *
 * Revision 1.15  2006/11/10 01:58:46  이종원
 * LOG를 MSG_ERR에서 DEBUG로 변경
 *
 * Revision 1.14  2006/10/20 11:01:17  이종원
 * 전문별 타임아웃값 설정 가능하도록 수정
 *
 * Revision 1.13  2006/10/19 15:10:33  이종원
 * 전문별 타임아웃값 설정 가능하도록 수정
 *
 * Revision 1.12  2006/10/19 15:09:58  이종원
 * 전문별 타임아웃값 설정 가능하도록 수정
 *
 * Revision 1.11  2006/10/19 15:07:00  이종원
 * 전문별 타임아웃값 설정 가능하도록 수정
 *
 * Revision 1.10  2006/10/19 14:50:50  이종원
 * 전문별로 별도 timeout값을 설정 할 수 있게 처리
 *
 * Revision 1.9  2006/10/16 03:46:03  이종원
 * default log file 수정
 *
 * Revision 1.8  2006/10/16 01:06:24  이종원
 * 수행시간 모니터링을 pooling이 아닐때만 수행 하도록 수정
 *
 * Revision 1.7  2006/10/13 09:10:19  이종원
 * *** empty log message ***
 *
 * Revision 1.6  2006/09/28 13:55:25  이종원
 * *** empty log message ***
 *
 * Revision 1.5  2006/09/27 14:12:21  이종원
 * *** empty log message ***
 *
 * Revision 1.4  2006/09/27 13:34:12  이종원
 * *** empty log message ***
 *
 * Revision 1.3  2006/09/27 13:21:57  이종원
 * *** empty log message ***
 *
 * Revision 1.2  2006/09/27 10:47:13  이종원
 * *** empty log message ***
 *
 * Revision 1.1  2006/09/27 10:22:53  이종원
 * 디렉토리변경
 *
 * Revision 1.16  2006/09/27 08:55:01  이종원
 * *** empty log message ***
 *
 * Revision 1.15  2006/09/27 08:41:18  이종원
 * *** empty log message ***
 *
 * Revision 1.14  2006/09/27 03:07:01  이종원
 * socket count하는 로직 수정 및 read bug수정
 *
 * Revision 1.13  2006/09/26 10:25:41  이종원
 * pooling socket update
 *
 * Revision 1.12  2006/09/23 01:41:15  이종원
 * config map으로 변경
 *
 * Revision 1.11  2006/09/23 01:39:10  이종원
 * config map으로 변경
 *
 * Revision 1.10  2006/09/23 00:52:33  이종원
 * *** empty log message ***
 *
 * Revision 1.9  2006/09/19 12:47:06  이종원
 * update
 *
 * Revision 1.8  2006/09/18 10:43:54  이종원
 * async 처리를 위해 기능 update
 *
 * Revision 1.7  2006/09/16 13:11:56  이종원
 * update
 *
 * Revision 1.6  2006/06/26 01:38:42  김승희
 * timeout 관련 수정
 *
 * Revision 1.5  2006/06/26 00:35:17  김승희
 * timeout millisecond 단위로 변경
 *
 * Revision 1.4  2006/06/23 01:51:33  이종원
 * *** empty log message ***
 *
 * Revision 1.3  2006/06/17 10:16:52  오재훈
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class SocketFactory implements PoolableObjectFactory {
    
	protected String logConfig=PropertyManager.getProperty(
            "spiderlink","MSG_MONITOR_LOG","DEBUG");
    /**
     * 접속할 IP
     */
	protected String ip;
    /**
     * 접속할 PORT
     */
	protected int port;
    /**
     * socket timeout : millisecond 단위로 세팅한다. default 180초 이다.
     */
	protected int timeout=180*1000;
    
	protected long tempTimeout=0;
    
    protected long connectTimeout=5000;
   
	protected int ioType=WorkerSocket.IN_OUT;
    /**
     * 이 factory와 연괸된 pool;
     */
    protected SocketPool pool;
    
    protected Map config=new HashMap();

    public SocketFactory(String ip,int port, int timeout, int ioType, String logConfig){
        this.ip=ip;
        if(port > 0)
        this.port=port;
        if(timeout >=0 ){
            if(timeout < 1000){
                timeout = timeout * 1000;
            }
            this.timeout=timeout;
        }
        if(ioType==1 || ioType==2 || ioType==3) this.ioType=ioType;
        if(logConfig != null){
            this.logConfig=logConfig;
        }
        
        //connect timeout use default
        //connectTimeout=3000;
    }
    public SocketFactory(String ip,int port, int timeout, int ioType, 
            String logConfig,Properties config  ){
        this(ip, port,timeout, ioType, logConfig);
        this.config= config;
        //socket connect Timeout
        if(config != null && config.getProperty("socket_connect_timeout") != null){
            try{
                connectTimeout =
                    Integer.parseInt(config.getProperty("socket_connect_timeout"));
            }catch(Exception e){ }
        }
    }
    public SocketFactory(String ip,int port, int timeout, int ioType){
        this(ip, port,timeout, ioType,null);
    }
    public SocketFactory(String ip,int port, int timeout,Properties config  ){
        this(ip, port,timeout);
        this.config= config;
        //socket connect timeout
        if(config != null && config.getProperty("socket_connect_timeout") != null){
            try{
                connectTimeout =
                    Integer.parseInt(config.getProperty("socket_connect_timeout"));
            }catch(Exception e){ }
        }
    }    
    public SocketFactory(String ip,int port, int timeout){
        this(ip, port, timeout, 3,null);
    }
    public SocketFactory(String ip,int port, int timeout, String logConfig){
        this(ip, port, timeout, 3, logConfig);
    }
    /**
     * in/out stream생성 및 timeout 30초가 세팅됨. 
     * @param ip
     * @param port
     * @param logConfig
     */
    public SocketFactory(String ip,int port,String logConfig){
        this(ip, port, 30000, logConfig);
    }
    
    public void setSocketPool(SocketPool pool){
        this.pool = pool;
    }
    
    public SocketPool getSocketPool(){
        if(pool==null){
            LogManager.error(logConfig,"SocketFactory's socket pool is null");
        }
        return pool;
    }
 
   /***
    * makeObject
    * @throws IOException 
 * @throws ClassNotFoundException 
 * @throws IllegalAccessException 
 * @throws InstantiationException 
    */
   public Object makeObject() throws Exception{
       Socket socket=null;
       try {
           SocketAddress serverAddr = new InetSocketAddress(ip,port);
           socket = new Socket();
           socket.connect(serverAddr, 2000);
           socket.setKeepAlive(true);
           long tempTimeoutMillis = getTempTimeout();
           if(tempTimeoutMillis>0){
               LogManager.debug(logConfig,getSocketPool()+">>>별도 타임이웃 설정 :"
                       +(tempTimeoutMillis/1000.0)+"초");
               socket.setSoTimeout((int) tempTimeoutMillis);
           }else{
               socket.setSoTimeout(this.timeout);
           }
//           LogManager.debug(logConfig,"SOCKET OPENED :IP["+ip
//                   +"] PORT["+port+"] Timeout["+(timeout/1000.0)+"초]");
           WorkerSocket worker = new WorkerSocket(socket,logConfig);
           if(pool != null){
               worker.setPool(pool);
           }
           worker.setIoType(ioType);
           worker.openStream();
           return worker;
       }catch (ConnectException e){ 
           LogManager.error(logConfig,e.toString());
           throw e;    
       } catch (UnknownHostException e) {
           LogManager.error(logConfig,e.toString());
           //throw new SysException(e);
           throw e;
       } catch (IOException e) {
           if(socket !=null){
               try{
                   socket.close();
               }catch(Exception ex){}
           }
           LogManager.error(logConfig,e.toString());
           throw e;          
       }     
   }

   /**
    *  destroyObject
    */
   public void destroyObject(Object obj) {
      
      if (obj instanceof WorkerSocket) {
         WorkerSocket worker = (WorkerSocket) obj;
         worker.close();
      }
   }

   /**
    * validate socket
    */
   public boolean validateObject(Object obj) {
       //LogManager.debug("validateObject-----------------");
       if(pool==null || pool.closed()) return false;
       if (obj instanceof WorkerSocket) {
          WorkerSocket worker = (WorkerSocket) obj;
          if(worker.getExeTime() > 2000 && !pool.isUsePooling()){
              LogManager.info(logConfig,"수행 시간 초과: IP["+ip
                      +"] PORT["+port+"] Time["+(worker.getExeTime()/1000.0)+"초]");
          }
          if(pool != null && !pool.isUsePooling()){
              //LogManager.debug("POOLING을 사용하지 않으므로 버립니다.:"+obj);
              return false;
          }          
          
//         if(!worker.isDone()) {  //if the worker is running the previous task, get another one.
//             LogManager.debug(logConfig,"this SocketWorker still working");
//            return false;
//         }
         if (worker.isStopped()) {
            return false;
         } 
         return true;
      }
      return false;
   }

   /**
    * activateObject
    */
   public void activateObject(Object obj) {

   }

   /**
    * passivateObject.go into the pool
    */
    public void passivateObject(Object obj) {

    }
    public Map getConfig() {
        return config;
    }
    public void setConfig(Map config) {
        this.config = config;
    }
    public long getTempTimeout() {
        try{
            return tempTimeout;
        }finally{
            tempTimeout=0;
        }
    }
    public void setTempTimeout(long tempTimeout) {
        if(tempTimeout<0) return;
        if(tempTimeout>0 && tempTimeout<500){
            tempTimeout = tempTimeout*1000;
        }
        this.tempTimeout = tempTimeout;
    }
    
    public void clearTempTimeout() {        
        this.tempTimeout = 0;
    }
    public long getConnectTimeout() {
        return connectTimeout;
    }
    public void setConnectTimeout(long connectTimeout) {
        if(connectTimeout <0 ) return;
        if(connectTimeout <= 1000){ //초단위로 세팅했다고 가정하고 1000곱한다
            connectTimeout = connectTimeout *1000;
        }
        this.connectTimeout = connectTimeout;
    }
}