/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package test.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 닫혀 지지 않은 커넥션 및 자원을 모니터링 잘 하는 지 테스트하는 클래스
 * 
 * 
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
 * $Log: JdbcPluginDriverTest.java,v $
 * Revision 1.1  2018/01/15 03:39:51  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:55  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:24  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:27  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:52  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.2  2008/02/20 00:42:47  오재훈
 * *** empty log message ***
 *
 * Revision 1.1  2007/11/26 08:38:30  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2006/11/21 07:49:11  이종원
 * *** empty log message ***
 *
 * Revision 1.1  2006/11/15 09:55:34  이종원
 * *** empty log message ***
 *
 * Revision 1.5  2006/06/19 04:10:16  안경아
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class JdbcPluginDriverTest {

	public static void main(String[] args) throws SQLException, InterruptedException, InstantiationException, IllegalAccessException, ClassNotFoundException {
	    
//        System.setProperty("SPIDER_HOME","/KEB/cmsproject");
//        System.setProperty("SPIDER_LOG_HOME","/KEB/cmsproject/logs");
//        System.setProperty("SPIDER_INSTANCE_ID","CA11");
        
        Class.forName("nebsoa.plugin.monitor.jdbc.OracleDriver").newInstance();
		//Connection con = DBManager.getConnection();
	    Connection con = DriverManager.getConnection(
	            "jdbc:oracle:thin:@211.115.70.148:1521:serverside","SPIDER","SPIDER99");
		//con.setAutoCommit(false) ; //con.setAutocommit(false) 를 하지 않은 곳에서 commit을 호출 할 수 없습니다.
		try {
			con.commit();
		} catch (SQLException e) {
			//LogManager.error(e.getMessage());
		}
		try {
			con.rollback();
		} catch (SQLException e) {
			//LogManager.error(e.getMessage());
		}
		con.setAutoCommit(false);
		con.close();
		
		con=null;
		
//start		check report file make or not....
		for(int x=0;x<2;x++){
			///////////////////////////////////////
			// 커넥션을 닫지 않은 곳을 찾아 냅니다.
			for(int i=0; i< 1;i++){
				con = DriverManager.getConnection(
                        "jdbc:oracle:thin:@211.115.70.148:1521:serverside","SPIDER","SPIDER99");				PreparedStatement stmt = con.prepareStatement("SELECT sysdate from dual");
				Statement stmt2 = con.createStatement();
				stmt2.executeQuery("SELECT "+i+" from dual");
				//System.out.println("PreparedStatement:"+stmt.getClass().getName());
				con=null;
                if(i/3==0){
					Thread.sleep(3000);
					stmt.close();
				}
	//			else if(i/4==0){
	//				Thread.sleep(3000);
	//				stmt.close();
	//			}
				
	//			try {
	//				con.close();
	//				con.createStatement();
	//				
	//			} catch (SQLException e1) {
	//				e1.printStackTrace();
	//			}
			}
			for(int i=0;i<3;i++){
				System.gc();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {				
					e1.printStackTrace();
				}
			}
		}//end of check report file make or not....
	}
}
