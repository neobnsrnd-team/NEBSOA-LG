/*
 * Spider Framework
 *
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 *
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.socketpool;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogConstants;
import nebsoa.common.log.LogManager;
import nebsoa.spiderlink.connector.io.writer.TcpMessageWriter;
import nebsoa.spiderlink.context.MessageContext;

/**
 * worker Socket
 * @author 이종원
 */
/*******************************************************************
 * <pre>
 * 1.설명
 * SocketPool 에서 관리되는 Socket 객체
 *
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
 * $Log: WorkerSocket.java,v $
 * Revision 1.1  2018/01/15 03:39:51  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:57  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.3  2010/01/12 10:45:22  유정수
 * error로그 추가
 *
 * Revision 1.2  2009/12/29 10:03:05  유정수
 * worksocket에 messageContext set/get
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
 * Revision 1.25  2007/03/24 04:52:24  최수종
 * 상속받은 클래스에서 접근 가능하도록 하기 위해
 * 멤버변수의 접근지정자를 default -> protected 로 변경
 *
 * Revision 1.24  2007/03/23 08:57:24  최수종
 * SocketFactory를 상속받아 구현시,
 * WorkerSocket의 INPUT_ONLY, OUTPUT_ONLY, IN_OUT 변수를
 * 상속받은 클래스가 사용해야 되므로 PUBLIC으로 변경
 *
 * Revision 1.23  2007/03/23 08:34:26  최수종
 * 접근지시자 기존 소스의(default)로 수정.
 * 필요한 변수만 protected로 변경함.
 *
 * Revision 1.22  2007/03/22 09:31:57  최수종
 * 명시적으로 public 선언으로 수정
 *
 * Revision 1.21  2007/01/29 07:43:09  이종원
 * *** empty log message ***
 *
 * Revision 1.20  2007/01/29 07:31:52  이종원
 * *** empty log message ***
 *
 * Revision 1.19  2007/01/29 07:23:01  이종원
 * *** empty log message ***
 *
 * Revision 1.18  2007/01/02 04:25:35  이종원
 * setError 수정
 *
 * Revision 1.17  2007/01/02 04:19:57  이종원
 * socket setStopped 수정
 *
 * Revision 1.16  2007/01/02 04:15:25  이종원
 * socket close하는 로직 추가
 *
 * Revision 1.15  2006/11/10 05:44:20  김승희
 * MSG_ERROR 로그 --> DEBUG로 수정
 *
 * Revision 1.14  2006/11/10 04:12:37  이종원
 * LOG CONFIG수정
 *
 * Revision 1.13  2006/10/16 02:01:45  이종원
 * Throwable cause변수 추가
 *
 * Revision 1.12  2006/10/16 01:48:27  이종원
 * *** empty log message ***
 *
 * Revision 1.11  2006/10/16 01:46:41  이종원
 * *** empty log message ***
 *
 * Revision 1.10  2006/10/13 09:10:19  이종원
 * *** empty log message ***
 *
 * Revision 1.9  2006/10/13 08:11:08  이종원
 * isClosed update
 *
 * Revision 1.8  2006/10/03 09:12:44  이종원
 * refactoring
 *
 * Revision 1.7  2006/10/03 07:39:17  이종원
 * socket에 writer장착
 *
 * Revision 1.6  2006/09/29 08:37:19  이종원
 * *** empty log message ***
 *
 * Revision 1.5  2006/09/29 08:36:15  이종원
 * 소켓 풀링 일 경우 바로 다는 부분에 대한 보완
 *
 * Revision 1.4  2006/09/28 14:03:03  이종원
 * *** empty log message ***
 *
 * Revision 1.3  2006/09/28 13:59:54  이종원
 * *** empty log message ***
 *
 * Revision 1.2  2006/09/28 13:52:41  이종원
 * *** empty log message ***
 *
 * Revision 1.1  2006/09/27 10:22:53  이종원
 * 디렉토리변경
 *
 * Revision 1.13  2006/09/27 09:20:06  이종원
 * *** empty log message ***
 *
 * Revision 1.12  2006/09/27 09:15:06  이종원
 * *** empty log message ***
 *
 * Revision 1.11  2006/09/27 08:41:18  이종원
 * *** empty log message ***
 *
 * Revision 1.10  2006/09/27 08:08:24  이종원
 * log양 줄임
 *
 * Revision 1.9  2006/09/27 03:07:01  이종원
 * socket count하는 로직 수정 및 read bug수정
 *
 * Revision 1.8  2006/09/26 10:25:41  이종원
 * pooling socket update
 *
 * Revision 1.7  2006/09/26 09:16:48  이종원
 * 풀링 사용할 경우 close하는 로직 보완
 *
 * Revision 1.6  2006/09/23 00:52:33  이종원
 * *** empty log message ***
 *
 * Revision 1.5  2006/09/19 12:50:55  이종원
 * update
 *
 * Revision 1.4  2006/09/19 12:47:06  이종원
 * update
 *
 * Revision 1.3  2006/09/18 10:43:54  이종원
 * async 처리를 위해 기능 update
 *
 * Revision 1.2  2006/06/17 10:16:52  이종원
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class WorkerSocket {
	static Object dummy = new Object();
	public static final int INPUT_ONLY=1;
	public static final int OUTPUT_ONLY=2;
	public static final int IN_OUT=3;

	private MessageContext messageContext;
    /**
     * 이 WorkerSocket instance가 남길 로그 설정
     */
	protected String logConfig=LogConstants.SPIDERLINK;
    /**
     * 이 WorkerSocket instance가  관리하는  socket
     */
	protected Socket socket;
    /**
     * 이 WorkerSocket instance가  관리하는  DataInputStream
     */
	protected DataInputStream dataInputStream;

    /**
     * 이 WorkerSocket instance가  관리하는  DataOupputStream
     */
	protected DataOutputStream dataOutputStream;
    /**
     * 이 WorkerSocket instance가  pool로 부터 나와 일을 시작한 시간
     */
	long workStartTime;

    private TcpMessageWriter messageWriter;

    /**
     * 이 WorkerSocket instance가  작업을 마치고 pool로 들어간 시간
     */
    long workEndTime;

    /**
     * 이 WorkerSocket instance가  생성된 시간
     */
    long createTime=System.currentTimeMillis();

    /**
     * destroy timeout threshold
     */
    long destroyThreshold;

   /**
    * 해당 소켓이 유효한지를 판단하는 flag 변수
    * 장애 여부나, 사용시간이 초과 되었는지를  판단 하기 위해 사용
    */
   private boolean stopped = false;

   /**
    * 현재 수행중인 job이 있는지 여부,<font color='red'>수행중인 것이 없을 때 true이다.</font>
    */
   private boolean done = true;

   /**
    * 기대되는 수신 전문  건수.
    */
   private int remains=0;
   /**
    * socket factory : need to destroy this object.
    * @author 이종원.

   //SocketFactory factory;
    *
    */

   /**
    * The object to synchronize upon for notifying the completion of task.
    */
   private Object syncObject = null;


   /**
    * The pool being used. We use this if we need to return the object back to the
    * pool. If this is not set, we assume that the client will take care of returning
    * the object back to the pool.
    */
   private SocketPool pool = null;
   /**
    * 이 socket을 통하여 in / out을 모두 처리 할 것인지(3),input만 처리 할 것인지(1)
    * output만 처리 할 것인지(2) 설정
    */
   private int ioType =IN_OUT;

   private String createdIndex;

   static int socketCreateCount=0;
   static int currentOpenSocketCount=0;
   public WorkerSocket(Socket socket,String logConfig) {
       this.socket = socket;
       if(logConfig != null){
           this.logConfig=logConfig;
       }
       workEndTime=System.currentTimeMillis();
       workStartTime=workEndTime;
       synchronized(dummy){
           createdIndex=++socketCreateCount +"번째 Socket";
           currentOpenSocketCount++;
       }
   }
   /**
    * @param pool The pool to set.
    */
   public void setPool(SocketPool pool) {
      this.pool = pool;
   }
   public SocketPool getPool() {
       return pool;
   }
   /**
    * @return Returns the done.
    */
   public boolean isDone() {
      return done;
   }
   /**
    * @param done The done to set.
    */
   public void setDone(boolean done) {
      this.done = done;
//      if(done && destroyOnDone){
//          LogManager.debug("Destroy on done setted... so close me..."+this);
//          destroy();
//      }
   }

//   public void destroy(){
//      this.close();
       //아래와 같이 코딩 할경우 socket의 active count를 계속 감소시켜
       //numActive값이 음수가 나온다.
//       if(pool != null && !pool.closed()){
//           synchronized(pool){
//               try {
//                   pool.invalidateObject(this);
//               } catch (Exception e) {
//                   LogManager.error("fail to invalidate Me:"+this+":"+e.toString(),e);
//               }
//           }
//       }else{
//           this.close();
//       }
       //if(pool != null && !pool.closed()) pool.decreaseSocketCount();
//   }


   /**
    * @return Returns the stopped.
    */
   public boolean isStopped() {
      if( stopped) return true;
      return (getSocket() ==null|| getSocket().isClosed()
              ||getSocket().isInputShutdown()
              ||getSocket().isOutputShutdown());
   }
   /**
    * @param stopped The stopped to set.
    */
   public void setStopped(boolean stopped) {
      this.stopped = stopped;
   }

   public void stop(){
       setStopped(true);
   }

   /**
    * @param stopped The stopped to set.
    */
   public void setError(boolean flag) {
       //LogManager.debug("set error true called..");
       setStopped(true);
       close();
   }

   /**
    * 유효한지 체크하는 메소드 (stopped와 반대의 값을 리턴함.)
    */
   public boolean isValid() {
       return !this.stopped;
   }
   /**
    * @return Returns the syncObject.
    */
   public Object getSyncObject() {
      return syncObject;
   }
   /**
    * @param syncObject The syncObject to set.
    */
   public void setSyncObject(Object syncObject) {
      this.syncObject = syncObject;
   }



   /**
    * reset the memebers to service next request.
    */
   public void reset() {
       done=true;
       this.syncObject = null;
   }

   public void close(){
       stopped=true;
       closeStream();
       if(socket != null){
           try {
               socket.close();
               logSocketInfo();
               socket=null;
           } catch (IOException e) {
               LogManager.error(logConfig,e.toString(),e);
           }
       }
   }

   public  void logSocketInfo() {
       long endTime=System.currentTimeMillis();
       double workTime=(endTime-createTime)/1000.0;
       synchronized(dummy){
           currentOpenSocketCount--;
       }
       LogManager.info("Socket close...["+this+"]"+createdIndex
               +",사용시간:"+workTime+"초, 현재 open된SocketCount:"+currentOpenSocketCount);
   }

   public void closeStream(){

       if(dataOutputStream != null){
           //LogManager.debug("close dataOutputStream");
           try {
               dataOutputStream.close();
           } catch (IOException e) {
               LogManager.error(logConfig,e.getMessage(),e);
           }finally{
               dataOutputStream=null;
           }
       }

       if(dataInputStream != null){
           //LogManager.debug("close dataInputStream");
           try {
               dataInputStream.close();
           } catch (IOException e) {
               LogManager.error(logConfig,e.getMessage(),e);
           }finally{
               dataInputStream=null;
           }
       }
   }

   public void openStream() throws IOException{
       if(ioType==OUTPUT_ONLY || ioType==IN_OUT){
           openOutputStream();
       }
       if(ioType==INPUT_ONLY || ioType==IN_OUT){
           openInputStream();
       }

   }
   protected void openOutputStream() throws IOException {
       if(dataOutputStream == null){
               dataOutputStream = new DataOutputStream(socket.getOutputStream());
               //LogManager.debug("OPEN OUTPUT STREAM OK");
       }
   }
   protected void openInputStream() throws IOException {
       if(dataInputStream == null){
               dataInputStream = new DataInputStream(socket.getInputStream());
               //LogManager.debug("OPEN INPUT STREAM OK");
       }
   }
    public int getIoType() {
        return ioType;
    }
    public void setIoType(int ioType) {
        this.ioType = ioType;
    }
    public long getWorkEndTime() {
        return workEndTime;
    }
    public void setWorkEndTime() {
        this.workEndTime = System.currentTimeMillis();
    }
    public long getWorkStartTime() {
        return workStartTime;
    }
    public void setWorkStartTime() {
        this.workStartTime = System.currentTimeMillis();
    }

    public long getExeTime(){
        //long exeTime=workEndTime-workStartTime;
        //return exeTime;
        return System.currentTimeMillis()-workStartTime;
    }

    public String toString(){
        if(socket != null) return socket.toString();
        return "destroyed worker Socket";
        //+" ioType["+ioType
        //+"] createTime["+new Date(createTime)+"] lastExeTime["+new Date(workEndTime)+"]";
    }
    public long getCreateTime() {
        return createTime;
    }
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
    public DataInputStream getDataInputStream() {
        return dataInputStream;
    }
    public void setDataInputStream(DataInputStream dataInputStream) {
        this.dataInputStream = dataInputStream;
    }
    public DataOutputStream getDataOutputStream() {
        return dataOutputStream;
    }
    public void setDataOutputStream(DataOutputStream dataOutputStream) {
        this.dataOutputStream = dataOutputStream;
    }
    public String getLogConfig() {
        return logConfig;
    }
    public void setLogConfig(String logConfig) {
        this.logConfig = logConfig;
    }
    public Socket getSocket() {
        return socket;
    }
    public void setSocket(Socket socket) {
        this.socket = socket;
    }
    public int getRemains() {
        return remains;
    }
    public void setRemains(int remains) {
        this.remains = remains;
    }
    /**
     * increaseRemains value
     * 2006. 9. 18.  이종원 작성
     * @param remains
     */
    public synchronized void increaseRemains() {
        this.remains++;
    }
    /**
     * decreaseRemains value
     * 2006. 9. 18.  이종원 작성
     * @param remains
     */
    public synchronized void decreaseRemains() {
        this.remains--;
        if(remains == 0){
            this.setDone(true);
        }
    }

    public void setWorkEndTime(long workEndTime) {
        this.workEndTime = workEndTime;
    }
    public void setWorkStartTime(long workStartTime) {
        this.workStartTime = workStartTime;
    }

    boolean destroyOnDone=false;
    public void destroyOnDone(boolean b) {
        this.destroyOnDone=b;
    }
    public long getDestroyThreshold() {
        return destroyThreshold;
    }
    public void setDestroyThreshold(long destroyThreshold) {
        if(destroyThreshold<1000) destroyThreshold=destroyThreshold*1000;
        this.destroyThreshold = destroyThreshold;
    }
//    public void setFactory(SocketFactory factory) {
//        this.factory = factory;
//    }
//    public SocketFactory getFactory() {
//        return factory;
//    }
    public TcpMessageWriter getMessageWriter() {
        return messageWriter;
    }

    public void setMessageWriter(TcpMessageWriter writer) {
        if(messageWriter==null){
            this.messageWriter = writer;
        }else{
            throw new SysException("Already messagewirter is setted..");
        }
    }

    public void stopWithException(Throwable e) {
        this.stop();
        this.cause = e;
        LogManager.info("DEBUG",this+" Worker Stopped:원인::"+e.toString());
        LogManager.error(this+" Worker Stopped:원인::"+e,e);
    }

    Throwable cause;


    public Throwable getCause() {
        return cause;
    }
    public void setCause(Throwable cause) {
        this.cause = cause;
    }

    public MessageContext getMessageContext() {
		return messageContext;
	}
	public void setMessageContext(MessageContext messageContext) {
		this.messageContext = messageContext;
	}

}