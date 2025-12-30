package test.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import nebsoa.common.jdbc.DBManager;

public class ConnectionCloseTest {
	public static void main(String[] args) throws Exception {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
		try	{
			con = DBManager.getConnection();
			String sql = "select * from fwk_menu where menu_id = 'fdsfdes1'";
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
			while (rs.next()) {
				System.out.println("--------------------------");
			}
            
            pstmt.close();
            System.out.println("pstmt close");
            rs.close();
            System.out.println("rs close");
            con.close();
            System.out.println("con close");
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
		    if (rs != null) {
                rs.close();
				System.out.println("rs close");
            }
		    if (pstmt != null) {
                pstmt.close();
				System.out.println("pstmt close");
            }
		    if (con != null) {
                con.close();
				System.out.println("con close");
            }
		    System.out.println("finally complete");
		}
	}
}
