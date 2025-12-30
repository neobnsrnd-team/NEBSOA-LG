/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.util;

//import javax.servlet.http.HttpServletRequest;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 성능 측정을 위한 클래스 입니다
 * 사용법은
 * Timer timer = Timer.getTimer("게시판리스트");
 *   //내부적으로 timer.begin();을 호출합니다.
 * .....
 * timer.showTime("db access");
 * ...
 * timer.showTime("출력");
 * ...
 * timer.end();
 * 와 같이 사용하시면 됩니다.
 * 단, 반드시 end를 호출해야 합니다.
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
 * $Log: Timer.java,v $
 * Revision 1.1  2018/01/15 03:39:49  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:32  cvs
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
 * Revision 1.1  2008/01/22 05:58:17  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:03  안경아
 * *** empty log message ***
 *
 * Revision 1.6  2006/07/25 07:33:34  이종원
 * *** empty log message ***
 *
 * Revision 1.5  2006/07/25 06:56:02  이종원
 * *** empty log message ***
 *
 * Revision 1.4  2006/06/17 10:37:45  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class Timer {

//	private static Pool pool = new Pool(10);

	long start;
	long before;

	long total;

	public String module=null;

/*
	public static Timer getTimer(String module){
		Timer t =  (Timer) pool.get();
		if(t ==null) t = new Timer();
		t.begin(module);
		return t;
	}
/**
	public static Timer getTimer(HttpServletRequest request){
		
		return getTimer(request.getRequestURI());
	}

	public static Timer getTimer(){
		return getTimer("");
	}
**/	
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


	public long showTime(){
		return showTime("");
	}

	public long showTime(String step){
		long now =  System.currentTimeMillis();	
        long gap = now - before;
		total = total+gap;
        System.out.println(module+"###step "+step+" :"
                +(gap/1000.0)+"초, total:"+(total/1000.0)+"초");
		before = now;
		return now;
	}

	public void suspend(){
		long now =  System.currentTimeMillis();
		total = total+(now - before);
	}
	public void resume(){
		before =  System.currentTimeMillis();		
	}

	public void end(){
		
		long now = showTime("lastStep");//System.currentTimeMillis();
		total = total+(now - before);
//if((total) > 2000){
		System.out.println(module+"### total ### :" +(total/1000.0)+"초");
//		LogManager.info("TUNNING_LIST",module+"### total ### :" +((now - before)/1000.0)+"초 ");
//}
/*        
        if(isTimerLogMode()) {
            dbLog(total);
        }
        
		pool.put(this);
		this.module = null;
*/		
	}
/*
	static String qry = "INSERT INTO log_for_test (LOG_SEQ,URI,LOG_TIME,LOG_DATE)"
			+" VALUES(log_seq.nextval,?,?,SYSDATE)";

	public void dbLog(long term){
		if(!isTimerLogMode()) return;

		Connection con=null;
		PreparedStatement pstmt=null;
		try{
			
			con = DBManager.getConnection();
			pstmt = DBManager.prepareStatement(con,qry);
			pstmt.setString(1,module);
			pstmt.setLong(2,term);
			pstmt.executeUpdate();

		}catch(Exception e){
			LogManager.error(e.getMessage());
		}finally{
			DBManager.close(pstmt);
			DBManager.close(con);
		}
	}
*/    
    /**
     * 타이머의 수행 내역을 DB에 저장할 것인지 
     * 2003. 7. 25.  이종원 작성
     * @return false is default
     */
	private boolean isTimerLogMode() {
        return false;
        //return PropertyManager.getBooleanProperty("default","TIMER.LOG_MODE","false");
    }

    /**
	* 현재시간이 영업시간인지 여부를 체크 하는 메소드
	
	public static boolean isWorkTime(HttpServletRequest request){
		return true;
	}
*/


	/**
	 * @return Returns the total.
	 */
	public long getTotal() {
		long now =  System.currentTimeMillis();
		System.out.println(module+"### 진행 시간 Total:" +(total/1000.0)+"초");
		total = total+(now - before);
		before = now;
		return total;
	}
	
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
        // dataMap.put("_timer_", timer);
        // Timer timer =(Timer) dataMap.get("_timer_");
        try{
            Thread.sleep(500);
        }catch(Exception e){ }
        timer.showTime("last step");
    }
};