package nebsoa.monitor.wasmonitor;

public class ThreadInfo {
    String serverName;
    String queueName;
    String name;
    String status;
    String currentRequest;
    long currentRequestStartTime;
    
    public String getCurrentRequest() {
        return currentRequest;
    }
    public void setCurrentRequest(String currentRequest) {
        this.currentRequest = currentRequest;
    }
    public long getCurrentRequestStartTime() {
        return currentRequestStartTime;
    }
    public void setCurrentRequestStartTime(long currentRequestStartTime) {
        this.currentRequestStartTime = currentRequestStartTime;
    }
    public String getServerName() {
        return serverName;
    }
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    
    public double workTime(){
        return (System.currentTimeMillis()-currentRequestStartTime)/1000.0;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getQueueName() {
        return queueName;
    }
    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }
}
