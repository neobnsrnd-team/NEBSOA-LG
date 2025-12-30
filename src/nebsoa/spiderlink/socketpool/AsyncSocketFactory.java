/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.socketpool;

import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import nebsoa.common.log.LogManager;
import nebsoa.common.util.StringUtil;
import nebsoa.spiderlink.connector.constants.ConnectorConstants;
import nebsoa.spiderlink.connector.io.reader.AsyncMessageReader;

/*******************************************************************
 * <pre>
 * 1.설명 
 * Async read를 수행 하는 Socket를 생성하는 factory이다.
 * 지정된  AsyncReader가 있어야 한다.
 * 
 * 
 * 2.사용법
 * SocketFactory factory = new AsyncSocketFactory(ip,port,timeout,"DEBUG");
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
 * $Log: AsyncSocketFactory.java,v $
 * Revision 1.1  2018/01/15 03:39:51  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:57  cvs
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
 * Revision 1.1  2007/11/26 08:37:40  안경아
 * *** empty log message ***
 *
 * Revision 1.7  2007/01/02 04:46:01  이종원
 * *** empty log message ***
 *
 * Revision 1.6  2006/10/16 03:46:03  이종원
 * default log file 수정
 *
 * Revision 1.5  2006/10/16 03:39:01  이종원
 * *** empty log message ***
 *
 * Revision 1.4  2006/10/16 01:46:36  이종원
 * *** empty log message ***
 *
 * Revision 1.3  2006/10/03 09:12:44  이종원
 * refactoring
 *
 * Revision 1.2  2006/09/27 10:47:13  이종원
 * *** empty log message ***
 *
 * Revision 1.1  2006/09/27 10:22:53  이종원
 * 디렉토리변경
 *
 * Revision 1.10  2006/09/27 09:15:06  이종원
 * *** empty log message ***
 *
 * Revision 1.9  2006/09/27 08:41:18  이종원
 * *** empty log message ***
 *
 * Revision 1.8  2006/09/27 03:07:01  이종원
 * socket count하는 로직 수정 및 read bug수정
 *
 * Revision 1.7  2006/09/23 02:27:42  이종원
 * *** empty log message ***
 *
 * Revision 1.6  2006/09/23 01:42:24  이종원
 * config map으로 변경
 *
 * Revision 1.5  2006/09/23 01:27:09  이종원
 * 변경없음
 *
 * Revision 1.4  2006/09/23 00:52:33  이종원
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class AsyncSocketFactory extends SocketFactory {
    
    
    public AsyncSocketFactory(String ip,int port, int timeout, int ioType, String logConfig){
        super(ip,port,timeout,ioType,logConfig);
    }
    
    public AsyncSocketFactory(String ip,int port, int timeout, int ioType){
        this(ip, port,timeout, ioType, null);
    }
    
    public AsyncSocketFactory(String ip,int port, int timeout){
        this(ip, port, timeout, 3,null);
    }
    public AsyncSocketFactory(String ip,int port, int timeout, String logConfig){
        this(ip, port, timeout, 3, logConfig);
    }
    
    public AsyncSocketFactory(String ip,int port,String logConfig){
        this(ip, port, 30000, logConfig);
    }
    

    
   /***
    * makeObject
    * @throws Exception 
    */
   public Object makeObject() throws Exception {
       Socket socket=null;
       try {
           socket = new Socket(ip,port);
           socket.setKeepAlive(true);
           socket.setSoTimeout(this.timeout);
           AsyncWorkerSocket worker = new AsyncWorkerSocket(socket,logConfig);
           if(pool != null){
               worker.setPool(pool);
           }
           //POOLING을 사용하는 소켓 이므로...
           //TODO 자동 close... SETTING 기본 3분
           worker.setDestroyThreshold(StringUtil.parseLong(
                   (String)(config.get(
                   ConnectorConstants.SOCKET_EXPIRE_THRESHOLD)),3*60*1000));
           AsyncMessageReader reader=getReader();
           reader.setWorker(worker);
           worker.setReader(reader);
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
       }catch (Exception e){
           if(socket !=null){
               try{
                   socket.close();
               }catch(Exception ex){}
           }
           LogManager.error(logConfig,e.toString());
           throw e;
       }
   }

    public AsyncMessageReader getReader() throws InstantiationException,
       IllegalAccessException, ClassNotFoundException {
       return (AsyncMessageReader) 
           Class.forName((String)(config.get(
               ConnectorConstants.ASYNC_READER))).newInstance();
    }

    /**
    *  destroyObject
    */
    public void destroyObject(Object obj) {
        //LogManager.debug("destroyObject called.....");
        if (obj instanceof AsyncWorkerSocket) {
            AsyncWorkerSocket worker = (AsyncWorkerSocket) obj;
            worker.destroy();
        }
    }
}