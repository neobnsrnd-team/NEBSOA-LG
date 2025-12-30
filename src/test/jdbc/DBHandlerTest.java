package test.jdbc;

import java.io.Writer;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import nebsoa.common.exception.DBException;
import nebsoa.common.exception.SysException;
import nebsoa.common.jdbc.DBManager;
import nebsoa.common.log.LogManager;

public class DBHandlerTest {

public static void main(String[] arg){

		System.setProperty("SPIDER_LOG_HOME", "D:\\ServerSide\\kebcvs\\cmsproject\\logs");
	
		String sql ="select 전자금융거래XML변환데이터 from 전자금융거래내역 where 웹CMS일련번호 = '06090500028' for update";
//		String sql ="select 주민사업자번호 from 전자금융거래내역 where 웹CMS일련번호 = '06090500028'";

		Connection con = null;
		Statement pstmt = null;
		ResultSet rs = null;

       try {
           con = DBManager.getConnection();
           
           pstmt = con.createStatement();
// setValues(pstmt, data);
           rs = pstmt.executeQuery(sql);

           if (rs.next()) {
               Clob clob = (Clob) rs.getClob(1);
                   Writer writer = clob.setCharacterStream(1);
// BufferedWriter writer = new BufferedWriter(clob.setCharacterStream(0));
               writer.write("<aaaa>QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ</aaaa>"); // write
																									// clob.
               writer.close();
           }
          
           rs.next();
//           LogManager.debug("@@@@"+rs.getString(1));
//       } catch (IOException e) {
//           LogManager.error(e.getMessage());
//           throw new DBException("CLOB 데이타를 write 하는 도중 에러가 발생했습니다.");
       } catch (SQLException e) {
           LogManager.error("DB_ERROR","ERROR_CODE:"+e.getErrorCode()+",REASON:"+e.getMessage(),e);
           throw new DBException(e);
       } catch (Exception e) {
           LogManager.error(e.getMessage(),e);
           throw new SysException(e.toString());
       } finally {
//           try {
//        	   rs.close();
//	           pstmt.close();
//           } catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

       }
    	
// DBHandler.writeClob(txManager,CLOB_sql,param4,"QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ");
	}}
