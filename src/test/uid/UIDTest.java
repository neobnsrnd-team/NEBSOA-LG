/**
 * 
 */
package test.uid;

import java.rmi.server.UID;

import nebsoa.common.util.UIDGenerator;

/**
 * @author 이종원
 *
 */
public class UIDTest {

    /**
     * uid test.
     * 2006. 9. 6.  이종원 작성
     * @param args 
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
//        //for(int i=0;i<100;i++){
//            UID  uid = new UID();
//            System.out.println(uid);
//        //}
//        for(int i=0;i<5;i++){
//            System.out.println(System.currentTimeMillis()/1000);
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//               
//            }
//        }
        System.out.println(3600 * 24);
        System.out.println(3600 * 24*365);
        System.out.println(System.currentTimeMillis());
        System.out.println(System.currentTimeMillis()/10);
        System.out.println((System.currentTimeMillis()/10)%1000000000);
        System.out.println((System.currentTimeMillis()/10)%100000000);
        
        System.out.println("100분의 일초를 1000,0000으로 나눈값:"+(System.currentTimeMillis()/10)%10000000);
        
        System.out.println((System.currentTimeMillis()/100)%10000000000L);
        
//        for(int i=0;i<10;i++){
//            new UIDTestThread2().start();
//        }
        for(int i=0;i<100;i++){
            System.out.println("100분의 일초를 1000,0000으로 나눈값:"+(System.currentTimeMillis()/10)%10000000);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
            }
        }
    }
    
    static long i=(System.currentTimeMillis()/10)%10000000;
    
    static Object lock=new Object();

    /**
     * 당일에 한하여 유일한 7자리 정수를 리턴 한다.
     * 단 시스템 일자를 강제로 뒤로 세팅하는 일이 없어야 하며, 
     * 리부팅 시간이 1초 이상 걸린다는 전제이어야 한다.
     * 2005. 9. 6.  이종원 작성
     * @return
     */
    public static String generateDailyUID(){
        synchronized(lock){
            long var = (++i)+10000000;
            System.out.println("var"+var);
            return ((var+"").substring(1));
        }
    }
    static class UIDTestThread2 extends Thread{
        public void run(){
            for(int i=0;i<10;i++){
                System.out.println(UIDTest.generateDailyUID());
            }
        }
    }

}
