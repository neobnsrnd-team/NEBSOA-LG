package test.thread;
/**
The Java 1.2 release introduced the concept of thread local variables as a standard class. Previously, they existed in the sun.server.util package of the Java WebServer product. Now, you get them in the java.lang.ThreadLocal and java.lang.InheritableThreadLocal classes. These classes permit individual threads to have independent copies of variables. With ThreadLocal, only a particular thread sees the variable, with InheritableThreadLocal a thread and all its descendents can see it. 
Here's an example, taken from my Mastering Java 2 book. 
**/

public class ThreadLocalTest1 implements Runnable {
    static private class MyThreadLocal extends ThreadLocal {
        protected Object initialValue() {
            System.out.println("##### initialValue called...");
            return Double.valueOf(Math.random() * 1000.0);
        }
    }
    static ThreadLocal threadLocal = new MyThreadLocal();
    static int counter = 0;
    private ThreadLocalTest1() {
        System.out.println("##### ThreadLocalTest1 생성자......");
        counter++;
    }
    public void run() {
        ThreadLocalTest1 myLTV = new ThreadLocalTest1();
        displayValues();
        try {
            Thread.currentThread().sleep (
                ((Double)threadLocal.get()).longValue());
            myLTV.displayValues();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void displayValues() {
        System.out.println (threadLocal.get() + "\t" + counter + 
            "\t" + Thread.currentThread().getName());
    }
    public static void main (String args[]) {
        ThreadLocalTest1 ltv = new ThreadLocalTest1();
        ltv.displayValues();
        for (int i=0;i<5;i++) {
            Thread t = new Thread (ltv);
            t.start();
        }
    }
}


