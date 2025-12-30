/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.util;

import java.net.InetAddress;
import java.security.GeneralSecurityException;

import nebsoa.common.log.LogManager;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 메일를 전송하기 위한 정보를 담고 있는 클래스
 * 
 * 2.사용법
 * 
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author $Author: cvs $
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 
 * $Log: LCManager.java,v $
 * Revision 1.1  2018/01/15 03:39:49  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:31  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:23  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:26  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:50  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:16  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:04  안경아
 * *** empty log message ***
 *
 * Revision 1.6  2007/10/31 07:03:53  jwlee
 * 특정 ip 대역을 체크하는 기능 추가
 *
 * Revision 1.5  2007/07/15 13:20:16  이종원
 * 성능튜닝
 *
 * Revision 1.4  2007/07/15 13:19:09  이종원
 * 성능튜닝
 *
 * Revision 1.3  2006/10/16 09:03:07  이종원
 * lc update
 *
 * Revision 1.2  2006/06/17 10:37:45  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class LCManager {
	static String errorReason="";
    static boolean isFirst=true;
	public static boolean verify() throws Exception {
	    return valid;
	}
	
	public static void printInvalidLicenseMessage(final String message){
	    new Thread(){
	        public void run(){
	            for(int i=1;i<60;i++){	                
	                LogManager.error("유효하지 않은  spider License 입니다.");
	                if(message != null){
		                LogManager.error("원인:"+message);
	                }
	                LogManager.error((61-i)/6+"분 후에 System이 종료 됩니다.");
	                try {
                        sleep(10*1000);
                    } catch (InterruptedException e) {
                    }
	            }
	            System.exit(0);
	        }
	    }.start();
	}
  
  	public static int getLeftDay(){
  		return 9999;
  	}
  	
  	public static boolean hasFeature(String feature,String expect){
  	    return true;

  
  	}
  	
  	public static boolean hasFeature(String feature){
  	    return hasFeature(feature, null);
  	}
  	
  	public static String getFeature(String feature){
  	    return "all";
  	}
  	static boolean valid=true;
    
  	public static void lc() throws Exception{
  	    if(valid) return;
        if(!verify()){
            LogManager.error("LICENSE IS NOT VALID");
            throw new Error("license is not valid");
//            while(true){
//                A a = new A();
//            }
            //new A();
        }
  	}
  	
  	public static void lc(String feature) throws Exception{
        if(verify()){
            if(!hasFeature(feature)){
                LogManager.error("LICENSE HAS NO FEATURE..."+feature);
                throw new Error("license is not valid");
                //new A();
//                while(true){
//                    A a = new A();
//                }
            }
        }else{
            throw new Error("license is not valid");
//            while(true){
//                A a = new A();
//            }
            //new A();
        }
  	}


  	public static void main(String[] args) throws Exception {
  	    System.out.println(LCManager.verify());
  	    System.out.println(LCManager.getLeftDay());
  	    System.out.println(LCManager.hasFeature("Jgen"));
  	    System.out.println(LCManager.hasFeature("SpiderWeb"));
  	    //LCManager.lc();
  	    LCManager.lc("JGEN");
  	}
  	
  	static class A{
  	    A(){
  	        LogManager.error("INVALID LICENSE");
  	        //B b = new B();
  	    }
  	}
  	static class B{
  	    B(){
  	        LogManager.error("INVALID LICENSE..");
  	        A a = new A();
  	    }
  	}
  	static class c{
  	    void a(){
  	    }
  	}
  	class d{
  	    void a(){
  	    }
  	}
  	static class e{
  	    void a(){
  	    }
  	}
  	class f{
  	    void a(){
  	    }
  	}
  	class g{
  	    void a(){
  	    }
  	}  	
}
