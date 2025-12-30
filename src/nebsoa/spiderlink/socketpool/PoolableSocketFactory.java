/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.socketpool;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import nebsoa.common.log.LogManager;
import nebsoa.common.util.StringUtil;
import nebsoa.spiderlink.connector.constants.ConnectorConstants;

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
 * $Log: PoolableSocketFactory.java,v $
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
 * Revision 1.1  2008/08/04 08:54:52  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:24  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:37:41  안경아
 * *** empty log message ***
 *
 * Revision 1.4  2006/10/03 09:12:44  이종원
 * refactoring
 *
 * Revision 1.3  2006/09/28 14:00:17  이종원
 * *** empty log message ***
 *
 * Revision 1.2  2006/09/27 10:47:13  이종원
 * *** empty log message ***
 *
 * Revision 1.1  2006/09/27 10:22:53  이종원
 * 디렉토리변경
 *
 * Revision 1.6  2006/09/27 09:15:06  이종원
 * *** empty log message ***
 *
 * Revision 1.5  2006/09/27 08:41:18  이종원
 * *** empty log message ***
 *
 * Revision 1.4  2006/09/27 08:08:24  이종원
 * log양 줄임
 *
 * Revision 1.3  2006/09/27 03:07:01  이종원
 * socket count하는 로직 수정 및 read bug수정
 *
 * Revision 1.2  2006/09/23 01:41:27  이종원
 * config map으로 변경
 *
 * Revision 1.1  2006/09/23 00:52:33  이종원
 * *** empty log message ***
 *
 * Revision 1.3  2006/09/19 12:47:06  이종원
 * update
 *
 * Revision 1.2  2006/09/18 10:56:58  이종원
 * async socket factory
 *
 * Revision 1.1  2006/09/18 10:44:10  이종원
 * 최초작성
 *
 *
 * </pre>
 ******************************************************************/
public class PoolableSocketFactory extends SocketFactory {
    
    
    public PoolableSocketFactory(String ip,int port, int timeout, int ioType, String logConfig){
        super(ip,port,timeout,ioType,logConfig);
    }
    
    public PoolableSocketFactory(String ip,int port, int timeout, int ioType){
        this(ip, port,timeout, ioType, "DEBUG");
    }
    
    public PoolableSocketFactory(String ip,int port, int timeout){
        this(ip, port, timeout, 3,"DEBUG");
    }
    public PoolableSocketFactory(String ip,int port, int timeout, String logConfig){
        this(ip, port, timeout, 3, logConfig);
    }
    
    public PoolableSocketFactory(String ip,int port,String logConfig){
        this(ip, port, 30000, logConfig);
    }

   /***
    * makeObject
    * @throws IOException 
    */
   public Object makeObject() throws IOException {
       Socket socket=null;
       try {
           socket = new Socket(ip,port);
           socket.setKeepAlive(true);
           socket.setSoTimeout(this.timeout);

           PoolableWorkerSocket worker = new PoolableWorkerSocket(socket,logConfig);
           if(pool != null){
               worker.setPool(pool);
           }
           //POOLING을 사용하는 소켓 이므로...
           //TODO 자동 close... SETTING 기본 3분
           worker.setDestroyThreshold(StringUtil.parseLong(
                   (String)(config.get(ConnectorConstants.SOCKET_EXPIRE_THRESHOLD))
                   ,3*60*1000));
        
           //worker.setIoType(ioType);
           worker.setIoType(WorkerSocket.IN_OUT);
           worker.openStream();
           LogManager.debug(logConfig,"SOCKET OPENED :IP["+ip
                   +"] PORT["+port+"] Timeout["+(timeout/1000.0)+"초]");
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
//        LogManager.debug("destroyObject called.....");
        if (obj instanceof WorkerSocket) {
            WorkerSocket worker = (WorkerSocket) obj;
//            int i=0;
//            while(worker.getRemains()>0 && i++ <3){
//                LogManager.debug("Wait while worker finish work...");
//                try {
//                    Thread.sleep(3*1000);
//                } catch (InterruptedException e) {
//                }
//            }
            worker.close();
        }
    }
}