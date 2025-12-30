package test.thread;

import java.util.ArrayList;
import java.util.List;

/**
Using ThreadLocal to simplify debug logging
Other applications for ThreadLocal in which pooling would not 
be a useful alternative include storing or accumulating per-thread 
context information for later retrieval. 
For example, suppose you wanted to create a facility for managing 
debugging information in a multithreaded application. 
You could accumulate debugging information in a thread-local 
container as shown by the DebugLogger class in Listing 4. 
At the beginning of a unit of work, you empty the container, 
and when an error occurs, you query the container to retrieve all 
the debugging information that has been generated so far by this 
unit of work.


Listing 4. Using ThreadLocal for managing a per-thread debugging log
Throughout your code, you can call DebugLogger.put(), 
saving information about what your program is doing, 
and you can easily retrieve the debugging information relevant 
to a particular thread later when necessary 
(such as when an error has occurred). 
This technique is a lot more convenient and efficient than 
simply dumping everything to a log file and then trying to sort out 
which log records come from which thread (and worrying about contention 
for the logging object between threads.)

ThreadLocal is also useful in servlet-based applications or 
any multithreaded server application in which the unit of work is 
an entire request, because then a single thread will be used during 
the entire course of handling the request. You can use ThreadLocal 
variables to store any sort of per-request context information 
using the per-thread-singleton technique described earlier.
*/
public class DebugLogger {
    public DebugLogger(){
        System.out.println("DebugLogger created..."+Thread.currentThread().getName());
    }
    private static class ThreadLocalList extends ThreadLocal {
        public Object initialValue() {
            System.out.println("\t ThreadLocalList initialValue..."+Thread.currentThread().getName());
            return new ArrayList();
        }

        public List getList() { 
            return (List) super.get(); 
        }
    }

    private ThreadLocalList list = new ThreadLocalList();
    private static String[] stringArray = new String[0];

    public void clear() {
        list.getList().clear();
    }

    public void putLog(String text) {
        System.out.println("\t putLog..."+text+"::"+Thread.currentThread().getName());
        list.getList().add(text);
    }

    public String[] getLog() {
        return (String[]) list.getList().toArray(stringArray);
    }
    
    public void dumpLog(){
        String[] logdata = getLog();
        System.out.println("\t dumpLog..."+Thread.currentThread().getName());
        for(int i=0;i<logdata.length;i++){
            System.out.println(logdata[i]);
        }
    }
}
 
