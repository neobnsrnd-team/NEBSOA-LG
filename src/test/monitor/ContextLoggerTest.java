package test.monitor;

import java.util.Random;

import nebsoa.common.Context;
import nebsoa.common.monitor.ContextLogger;

public class ContextLoggerTest {
    
    private static Random seedRandom = new Random();
    
    private static int generateRandomNumber() {
        int randomNum = seedRandom.nextInt(5) * 1000;
        return randomNum;
    }
    
    public static void sleep_for_a_while() throws InterruptedException {
        Thread.sleep(generateRandomNumber());
    }
    
    public static void main(String[] args) {
        ContextLoggerTest test = new ContextLoggerTest();
        int testThreadSize = 100;
        for (int i = 0; i < testThreadSize; i++) {
            new Thread(test.new TestThread("T" + i), "쓰레드-T-" + i).start();
        }
    }

    // 구분을 위해 고유 값 threadIdValue 을 갖는 Thread 로 변경
    class TestThread implements Runnable {
        private String threadIdValue = null;
        
        public TestThread(String threadIdValue) {
            this.threadIdValue = threadIdValue;
        }

        public void run() {
            try {
                sleep_for_a_while();
                Context ctx = new Context();
                ctx.setUserId(threadIdValue);
                ContextLogger.putContext(ctx);
                sleep_for_a_while();
                ContextLogger.traceCurrentRequest();
            } catch (InterruptedException x) {
                // do nothing
            }
        }
    }
}
