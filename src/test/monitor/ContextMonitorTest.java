package test.monitor;

import nebsoa.common.Context;
import nebsoa.common.monitor.ContextMonitorData;
import nebsoa.common.monitor.ContextMonitorManager;

public class ContextMonitorTest {
    public static void main(String[] args){
    	
    	System.setProperty("SPIDER_HOME", "D:/project/ibkproject");
    	System.setProperty("SPIDER_HOME_LOG", "D:/project/ibkproject/logs");
    	System.setProperty("SPIDER_INSTANCE_ID", "CA11");
    	
        int j=0;
        for(int i=0;i<=1;i++){
        
            ContextMonitorData data = new ContextMonitorData("login.jsp"+j);
            data.setWasId("CT12");
            data.setMaxtraceid("12");

    
            data.startStep(Context.STEP_ACTION);
                sleep(100);
                data.startActivity(Context.ACTIVITY_LOOKUP);
                    sleep(10);
                data.stopActivity();
                //BIZ-1
                data.startStep(Context.STEP_BIZ);
                    sleep(200);
                    data.startActivity(Context.ACTIVITY_DB_READ);
                        sleep(20);
                    data.stopActivity();
                data.endStep(Context.STEP_BIZ);
                
                data.startActivity(Context.ACTIVITY_LOOKUP);
                    sleep(100);
                data.stopActivity();
            
            
                //BIZ-2
                data.startStep(Context.STEP_BIZ);
                    data.startActivity(Context.ACTIVITY_DB_READ);
                    sleep(5000);
                    data.stopActivity();
                    data.startActivity(Context.ACTIVITY_DB_WRITE);
                        sleep(100);
                    data.stopActivity();
                    data.startActivity(Context.ACTIVITY_MESSAGE);
                        sleep(1000);
                    data.stopActivity();
                data.endStep(Context.STEP_BIZ);
                
                
                data.startActivity(Context.ACTIVITY_DB_WRITE);
                    sleep(1000);
                data.stopActivity();
            data.endStep(Context.STEP_ACTION);
            
            data.startStep(Context.STEP_RESPONSE);
                sleep(300);
            data.endStep(Context.STEP_RESPONSE);
            
            data.finish();
            
            ContextMonitorManager.getInstance().addContextMonitorData(data);
        }
        
        
        System.out.println(ContextMonitorManager.getInstance().getData());
        
        ContextMonitorManager.getInstance().saveReport();
        
        System.out.println("######################1");
        
    }
    
    static void sleep(long time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
        }
    }
}
