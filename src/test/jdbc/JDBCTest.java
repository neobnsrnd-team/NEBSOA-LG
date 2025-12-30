package test.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCTest {
	public static void main(String[] args) {
        /* Oracle */
        String className = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@172.17.14.14:1521:IBS";
		String user = "ibsdb";
		String passwd = "ibsdb0308";

		Connection con = null;
        PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;

		try	{
            // 1. JDBC Driver Loading
			Class.forName(className);
			System.out.println("Driver loading ok!!!");
            // 2. Connection 을 얻어온다.
			con = DriverManager.getConnection(url, user, passwd);
			System.out.println("Connection ok!!!");
            // 3. Statement 객체를 생성한다.(쿼리를 수행하는 역할)
			sql = "select * from ibslog where rownum < 10";
            pstmt = con.prepareStatement(sql);
//            pstmt.setString(1, "MANAGER");
            rs = pstmt.executeQuery();
            // 4. 쿼리를 실행한다.
            rs = pstmt.executeQuery();
			while (rs.next()) {
				System.out.println("empno : " + rs.getString(1));
				System.out.println("--------------------------");
			}
		} catch (ClassNotFoundException ex) {
			System.out.println("Driver loading is failed...");
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try	{
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (con != null)   con.close();
			} catch (SQLException ex) { }
		}
	}
}
