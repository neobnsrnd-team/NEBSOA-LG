/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package test.time;

public class Timer {

    long start;
    long before;

    long total;

    public String module=null;

    public Timer(){}
    
    public Timer(String name){
        this.module=name;
    }
    
    public void begin(){
        begin(module);
    }

    public void begin(String module){
        this.module = module;
        start = System.currentTimeMillis();
        before = start;
        total=0;
    }


    public long showTime(String step){
        long now =  System.currentTimeMillis(); 
        long gap = now - before;
        total = total+gap;
        String logData = module+"###step "+step+" :"+(gap/1000.0)+"초, total:"+(total/1000.0)+"초";
        System.out.println(logData);
        //////////여기를 주석 푸세요..
//        LogManager.error("TUNNING_LIST",logData);        
        before = now;
        return now;
    }

 //사용법 입니다.
    public static void main(String[] args){
        Timer timer = new Timer("게시판 상세보기");
        timer.begin();
        try{
            Thread.sleep(1000);
        }catch(Exception e){ }
        timer.showTime("권한체크");
        try{
            Thread.sleep(200);
        }catch(Exception e){ }
        timer.showTime("db access");
        //dataMap에 넣어 jpf나 bean, dao등에서 전달 해 가며 쓰기 위해서
        //아래 처럼 하시면 됩니다.
        // dataMap.put("_timer_", timer);
        // Timer timer =(Timer) dataMap.get("_timer_");
        try{
            Thread.sleep(500);
        }catch(Exception e){ }
        timer.showTime("last step");
    }
};