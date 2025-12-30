package test.thread;


public class ThreadLoggerTest {
    
    public static class LoggerTestThread extends Thread{
        int i=0;
        public void run(){            
            DebugLogger logger = new DebugLogger();
            for(int i=0;i<10;i++){
                logger.putLog(Thread.currentThread().getName()+"=="+i++);
                try {
                    sleep(10);
                } catch (InterruptedException e) {
                }
            }
            logger.dumpLog();
        }
    }
    
    public static void main(String[] args){
        for(int x=0;x<10;x++){
            new LoggerTestThread().start();
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
    }
}
