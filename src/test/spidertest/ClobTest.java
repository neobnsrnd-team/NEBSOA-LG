package test.spidertest;

import java.io.Reader;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Clob;

import nebsoa.common.Context;
import nebsoa.common.exception.DBException;
import nebsoa.common.exception.SysException;
import nebsoa.common.jdbc.DBHandler;
import nebsoa.common.jdbc.DBManager;
import nebsoa.common.jdbc.TxManager;
import nebsoa.common.log.LogManager;

public class ClobTest {

	public void testClob() {

		Context ctx = new Context();

		// String insSql =" INSERT INTO 전자금융거래내역 ( "
		// +"
		// 기관코드,전자금융채널서비스구분,주민사업자번호,작성자ID,웹CMS일련번호,거래예정일,총건수,전자금융거래XML변환데이터,최종변경일시,최종사용자ID
		// "
		// +" ) VALUES "
		// +"
		// ('9','9','9999999999999','TSTM01','00000000099','20060906','1000',EMPTY_CLOB(),'20060905200010','TSTM01')";

		String sql = "select 전자금융거래XML변환데이터 from 전자금융거래내역 where 웹CMS일련번호 = '06090500028' for update";

		String clobData = "<aaaa>clob test case</aaaa>";
		// String sql ="select 주민사업자번호 from 전자금융거래내역 where 웹CMS일련번호 =
		// '06090500028'";
		// String sql ="select 전자금융거래XML변환데이터 from 전자금융거래내역 where 웹CMS일련번호 =
		// '00000000001'";

		Connection con = null;
		// PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs = null;

		try {
			LogManager.debug("####clob insert start");

			con = DBManager.getConnection(ctx);
			con.setAutoCommit(false);

			// pstmt = con.prepareStatement(insSql);
			// pstmt.executeUpdate();

			// LogManager.debug("####clob insert end");

			pstmt2 = con.prepareStatement(sql);
			rs = pstmt2.executeQuery();
			Clob clob = null;
			if (rs.next()) {
				clob = rs.getClob(1);
			}

			Writer writer = clob.setCharacterStream(1);
			writer.write(clobData); // write
			writer.flush();
			writer.close();

			con.commit();
			con.setAutoCommit(true);
			LogManager.debug("####clob update end");

		} catch (Exception e) {
			try {
				con.rollback();
			} catch (SQLException se) {
				LogManager.error("DB_ERROR", "ERROR_CODE:" + se.getErrorCode()
						+ ",REASON:" + se.getMessage(), se);
				throw new DBException(se);
			}
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				pstmt2.close();
				// pstmt.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void dbhandlerWriteClob() {
		TxManager tx = new TxManager();
		String sql = "select 전자금융거래XML변환데이터 from 전자금융거래내역 where 웹CMS일련번호 = '06090500028' for update";

		try {
			tx.begin();
			DBHandler
					.writeClob(
							tx,
							sql,
							new Object[] {},
							"<?xml version=\"1.0\" encoding=\"euc-kr\" ?><item id=\"Label\" text=\"null\" /></item>");
			// DBHandler.writeClob(tx,sql,new Object[]{},"<?xml version=\"1.0\"
			// encoding=\"euc-kr\" ?><domain><item id=\"KO\"><item
			// id=\"승인응답구분\"><item id=\"Label\" text=\"null\"
			// /></item><d/omain>");

			tx.commit();
		} catch (Throwable e) {
			tx.rollback();
			e.printStackTrace();
			throw new SysException("FRU9993", e.toString());
		} finally {
			tx.end();
		}
	}

	public static void readClob(){
		Context ctx = new Context();
		String sql = "select 전자금융거래XML변환데이터 from 전자금융거래내역 where 웹CMS일련번호 = '06090500028'";


//		DataSet ds = DBHandler.executeQuery(ctx,sql);
//		ds.next();
//		Clob clob = (Clob)ds.getObject(1);

		Connection con = DBHandler.getConnection(ctx);
		PreparedStatement ps = null;
		ResultSet rs = null;
		Clob clob = null;
		
		

        StringBuffer clobString = new StringBuffer();
        try {

    		ps = con.prepareStatement(sql);
    		rs = ps.executeQuery();
    		rs.next();
    		clob = rs.getClob("전자금융거래XML변환데이터");

        	Reader in = clob.getCharacterStream();
	        char[] buffer = new char[1024];
	        int length = 0;
	
	        while ((length = in.read(buffer, 0, 1024)) != -1) {
	            clobString.append(buffer, 0, length);
	        }
	
	        in.close();
        }catch(Exception e){
        	e.printStackTrace();
	    }finally {
			try {
				rs.close();
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		LogManager.debug(clobString.toString());
		
	}
	
	
	public static void main(String[] arg) {
		System.setProperty("SPIDER_LOG_HOME",
				"D:\\ServerSide\\kebcvs\\cmsproject\\logs");
		readClob();
		dbhandlerWriteClob();
		readClob();
		LogManager.debug("#### main end");
	}
}
