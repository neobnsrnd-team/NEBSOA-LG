package nebsoa.spiderlink.connector.constants;

public class ConnectorConstants {
    /**
     * SocketPool에 처음 생성할 소켓 갯수를 지정하는 KEY:[socket_init_count]
     */
    public static final String SOCKET_INIT_COUNT = "socket_init_count";
    /**
     * Socket IO Protocol설정 정보를 지정하는 KEY:[io_protocol].
     * SYNC, ASYNC, NO_ACK 중에 하나를 지정한다.
     */
    public static final String IO_PROTOCOL = "io_protocol";
    /**
     * Async adapter를 만들 때 thread형태로 수신 처리만 하는 클래스를 지정하는 KEY:[input_message_handler]
     */
    public static final String INPUT_MESSAGE_HANDLER = "input_message_handler";

    
    /**
     * NO-ACK로 받은 전문을 NO_ACK_RESPONSE Adapter를 통해 전송 할 때 단순 전송 처리만을 
     * 담당하는 TcpMessageWriter를 지정하는 KEY:[output_message_handler]
     */
    public static final String OUT_MESSAGE_HANDLER = "output_message_handler";

    /**
     * ASYNC_READER를 얻기 위한 KEY:[async_reader]
     */
    public static final String ASYNC_READER = "async_reader";
    /**
     * SocketPool이 허용하는 최대 갯수로 소켓이 만들어 져 사용중일 때
     * 최대로 기다리는 시간 값을 알기 위한 KEY값 .[max_wait_when_busy]
     */
    public static final String MAX_WAIT_WHEN_BUSY = "max_wait_when_busy";
    /**
     * SocketPool이 허용하는 최대 갯수를  알기 위한 KEY값 .[socket_max_count]
     
    public static final String SOCKET_MAX_COUNT = "socket_max_count";
    */
    /**
     * SocketPool로 부터 리턴 받을 때 test를 수행 할 것인지를 얻기 위한 KEY:[test_on_borrow]
     
    public static String TEST_ON_BORROW="test_on_borrow";
    */
    /**
     * SocketPool USE_POOLING 여부를 얻기 위한 KEY.[use_pooling]
     */
    public static String USE_POOLING="use_pooling";
    /**
     * SocketPool refresh interval_threadhold 여부를 얻기 위한 KEY.[reconnect_interval]
     
    public static String RECONNECT_INTERVAL="reconnect_interval";
    */
    /**
     * QUEUE SIZE를 얻기 위한 KEY.[queue_size]
     */
    public static String QUEUE_SIZE="queue_size";
    
    /**
     * TIMEOUT를 얻기 위한 KEY. [timeout]
     */
    public static String TIMEOUT="timeout";
    
    /**
     * Socket connect TIMEOUT를 얻기 위한 KEY. [socket_connect_timeout]
     */
    public static String CONNECT_TIMEOUT="socket_connect_timeout";
    
    /**
     * Socket이 일정시간 요청이 없을 때 자동 CLOSE하는 
     * Expire threshold 를 얻기 위한 KEY. [socket_expire_threshold]
     */
    public static String SOCKET_EXPIRE_THRESHOLD="socket_expire_threshold";
    
    /**
     * SocketPool reset 주기를 얻기 위한 KEY. [socket_reset_interval] 
     */
    public static String SOCKET_RESET_INTERVAL="socket_reset_interval";
    
}   
    
