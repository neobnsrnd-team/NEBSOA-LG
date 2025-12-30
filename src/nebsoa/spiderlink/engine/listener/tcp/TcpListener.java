/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
package nebsoa.spiderlink.engine.listener.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;
import nebsoa.spiderlink.connector.constants.SyncType;
import nebsoa.spiderlink.connector.listener.protocol.TcpInputMessageHandler;
import nebsoa.spiderlink.connector.listener.util.ShutdownCommander;
import nebsoa.spiderlink.engine.Lifecycle;
import nebsoa.spiderlink.engine.listener.BaseListener;
import nebsoa.spiderlink.engine.listener.ListenerType;
import nebsoa.util.threadpool.ThreadPool;
import nebsoa.util.threadpool.ThreadPoolManager;
import nebsoa.util.threadpool.WorkerThread;


/*******************************************************************
 * <pre>
 * 1.설명 
 * TCP 전문을 수신하는 리스너를 만들 때 기본이 되는 클래스.
 * 
 * 2.사용법
 * 생성자 참조.
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author $Author: cvs $
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 
 * $Log: TcpListener.java,v $
 * Revision 1.1  2018/01/15 03:39:52  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:15  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:26  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:28  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:54  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:31  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:39:14  안경아
 * *** empty log message ***
 *
 * Revision 1.13  2006/10/03 09:12:44  이종원
 * refactoring
 *
 * Revision 1.12  2006/10/03 00:25:39  이종원
 * *** empty log message ***
 *
 * Revision 1.11  2006/10/02 06:39:17  이종원
 * *** empty log message ***
 *
 * Revision 1.10  2006/09/27 08:24:52  이종원
 * *** empty log message ***
 *
 * Revision 1.9  2006/08/28 00:54:39  김승희
 * ap 패키지, 클래스명 변경에 따른 수정
 *
 * Revision 1.8  2006/08/23 11:58:32  안경아
 * *** empty log message ***
 *
 * Revision 1.7  2006/08/22 10:11:17  안경아
 * *** empty log message ***
 *
 * Revision 1.6  2006/08/18 11:58:01  안경아
 * *** empty log message ***
 *
 * Revision 1.5  2006/08/18 08:28:40  안경아
 * *** empty log message ***
 *
 * Revision 1.4  2006/08/04 05:15:06  안경아
 * *** empty log message ***
 *
 * Revision 1.3  2006/08/01 14:00:16  이종원
 * code정리
 *
 * Revision 1.2  2006/08/01 00:30:09  김승희
 * Lifecycle 패키지 변경
 *
 * Revision 1.1  2006/07/31 11:17:12  이종원
 * 최초작성
 *
 * </pre>
 ******************************************************************/
public class TcpListener extends BaseListener implements Lifecycle{

    
    protected ServerSocket serverSocket;
    
    protected String handlerName;

    protected String ip;
    
    protected int listenPort;

    protected SyncType syncType;

    protected int resetCount;

	protected boolean exit=false;
	
   
    ThreadPool threadPool;
    
    protected int threadCount=5; 
    
    protected ShutdownCommander shutdownCommander;


    /**
     * 생성자
     */
    public TcpListener(String name,String ip,int listenPort, 
            SyncType syncType, int threadCount,String handlerName,
            String logConfig) {
        super(name,ListenerType.LISTENER_TCP,logConfig);
        this.ip=ip;
        this.listenPort = listenPort;
        this.syncType = syncType;
        if(threadCount >0){
            this.threadCount=threadCount;
        }   
        this.handlerName=handlerName;
        LogManager.debug(name+" Listener instance created");
    }
    
    public void init() {
//      occurs java.lang.IllegalThreadStateException
//    	try{
//    		setDaemon(true);
//    	}catch(Exception e){
//    		LogManager.error(e.toString());
//    	}
        
        if(exit){
            return;
        }
        
        if(threadPool==null){            
            threadPool=ThreadPoolManager.getInstance()
                .makeThreadPool(name+"-Listener",10);
            
            LogManager.info(logConfig,
                    name+"Listenr Thread Pool생성(갯수:"
                    +threadCount+")");
        }
        
        try {
            if (serverSocket == null || serverSocket.isClosed()) {
                LogManager.debug(logConfig,name+ " LISTENER OPEN PORT : ["
                        + listenPort + "]");
             
                serverSocket = new ServerSocket(listenPort,50);
                 
                LogManager.info(logConfig,name+" Listener가 IP:"+ip
                        +",Port:"+ listenPort + "에서 start 되었습니다.");
                
                resetCount++;
                if (resetCount > 1) {
                    LogManager.info(logConfig, resetCount
                            + "번째 RESET 작업이 수행되었습니다.");
                }
            }
        } catch (IOException e) {
            LogManager.error(logConfig,name+"Listener를 start하지 못하였습니다\n"+e, e);
            serverSocket = null;
        }
    }

    public void service() {
    	LogManager.debug("TcpListener service() exit="+exit);
        if(exit){
            return;
        }
        Socket socket = null;
        int ii=0;
    	
    	LogManager.debug("TcpListener serverSocket="+serverSocket);        
    	LogManager.debug("TcpListener serverSocket.isClosed()="+serverSocket.isClosed());
    	
    	while (!exit && serverSocket != null && !serverSocket.isClosed()) {
        	try {        
                socket = serverSocket.accept();
                LogManager.debug(logConfig,name+ "Listener가 소켓을 열었습니다.");
                WorkerThread thread= (WorkerThread) threadPool.borrowObject();
                LogManager.debug(logConfig,name
                        + "Listener가 threadPool로 부터 thread를 얻었습니다."
                        +"\n threadpool상태 "+threadPool.getInfo());                
                TcpInputMessageHandler handler= 
                    createHandler(this,socket,syncType,logConfig);

                thread.execute(handler);
                LogManager.debug(logConfig,name
                        + "Listener가 thread.execute(handler)를 호출 하였습니다.");
                
                
            } catch (IOException e) {
                LogManager.error(logConfig,e.getMessage(),e);
                LogManager.info(logConfig,"TRY TO RESET...");
                init();
            } catch (Exception e) {
                LogManager.error(e.getMessage(),e);
            } catch (Throwable e) {
                exit=true;
                LogManager.error(e.getMessage(),e);                
            }
        }
        
        
    }
    
    private TcpInputMessageHandler createHandler(TcpListener listener, 
             Socket socket, SyncType syncType2, String logConfig2) throws IOException {
        TcpInputMessageHandler handler=null;
        try {
            handler=(TcpInputMessageHandler) Class.forName(handlerName).newInstance();
//            handler.setOwner(listener);
            handler.setSocket(socket);
//            handler.setSyncType(syncType2);
            handler.setLogConfig(logConfig2);
            
        } catch (InstantiationException e1) {
            e1.printStackTrace();
            throw new SysException("[CANN'T MAKE INSTANCE 생성자 체크 :"+handlerName+"]");
        } catch (IllegalAccessException e1) {
            throw new SysException("[CANN'T MAKE INSTANCE 생성자 PUBLIC 인지 체크 :"+handlerName+"]");
        }catch(ClassNotFoundException e){
            throw new SysException("[CLASS_NOT_FOUND:"+e+"]");
        }
        return handler;
    }


    /**
     * close all
     */
    public void destroy() {
        if(stopped){
            LogManager.debug("종료를 완료 하였으므로 skip destroy...");
        }
    	LogManager.info(logConfig,name+" Listener를 종료합니다.=========================");
    	exit = true;
    	//마지막에 accept하며 기다리는 녀석을 없앤다.
        if(shutdownCommander != null){
            shutdownCommander.shutdown();
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e2) {
            LogManager.error(e2.toString());
        }
        if(threadPool != null){
        	int count=0;
            //쓰레드가 현재 수행을 마쳤으면..
        	while(threadPool.isRunning() && count<10 ){
                LogManager.info(logConfig,name+" Listener에 아직 "+
                        threadPool.getNumActive()
                        +"개의 수행중인 쓰레드가 있어 기다립니다..최대 "
                        +(10-count)+"초 까지 기다림.");
                count++;
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                }
            } 

            try {
                LogManager.debug(threadPool.getName()+"thread Pool을 close합니다.");
                threadPool.close();
                LogManager.debug(threadPool.getName()+"thread Pool을 ThreadPoolManager에서 제거합니다.");
                ThreadPoolManager.getInstance().close(threadPool.getName());
                threadPool=null;
            } catch (Exception e1) {
                LogManager.error(logConfig,e1.getMessage(),e1);
            }
        }
        
    	try {
            if (serverSocket != null) {
            	LogManager.info(logConfig,name+" Listener ServerSocket을 close 시작.");
            	serverSocket.close();
                serverSocket = null;
                LogManager.info(logConfig,name+" Listener ServerSocket을 close 종료.OK ");
            }            
        } catch (IOException e) {
            LogManager.error(logConfig,e);
        }
        stopped=true;
    }





    public String getIp() {
        return ip;
    }





    public void setIp(String ip) {
        this.ip = ip;
    }





    public int getListenPort() {
        return listenPort;
    }





    public void setListenPort(int listenPort) {
        this.listenPort = listenPort;
    }





    public ServerSocket getServerSocket() {
        return serverSocket;
    }





    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }





    public ShutdownCommander getShutdownCommander() {
        return shutdownCommander;
    }





    public void setShutdownCommander(ShutdownCommander shutdownCommander) {
        this.shutdownCommander = shutdownCommander;
    }





    public SyncType getSyncType() {
        return syncType;
    }





    public void setSyncType(SyncType syncType) {
        this.syncType = syncType;
    }





    public ThreadPool getThreadPool() {
        return threadPool;
    }





    public void setThreadPool(ThreadPool threadPool) {
        this.threadPool = threadPool;
    }

}