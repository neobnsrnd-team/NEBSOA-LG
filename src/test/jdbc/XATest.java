package test.jdbc;

import nebsoa.common.jdbc.DBManager;
import nebsoa.common.jdbc.DBResultSet;
import nebsoa.common.jdbc.XATxManager;

public class XATest {

    public void execute(String empno,String ename, String commit){
        XATxManager tx=null;
        try{
            //
            tx = new XATxManager();
            tx.begin();
            DBResultSet rs = DBManager.executeQuery("SELECT COUNT(*) FROM EMP");
            if(rs.next()) System.out.println("emp건수 : "+rs.getInt(1));
            
            Object[] param = new Object[]{empno,ename};
            DBManager.executePreparedUpdate("INSERT INTO EMP (EMPNO,ENAME,HIREDATE) VALUES "
                    +"( ?,?,sysdate)",param);
            if(!"true".equals(commit)){
                throw new Exception("강제 rollback");
            }
            tx.commit();
            rs = DBManager.executeQuery("SELECT COUNT(*) FROM EMP");
            if(rs.next()) System.out.println("commit 후 emp건수 : "+rs.getInt(1));
           
        }catch(Exception e){
            tx.rollback();
            DBResultSet rs = DBManager.executeQuery("SELECT COUNT(*) FROM EMP");
            if(rs.next()) System.out.println("rollback 후 emp건수 : "+rs.getInt(1));
        }finally{
            tx.end();
        }
    }
    
    public static void main(String[] args){
        XATxManager tx=null;
        try{
            //
            tx = new XATxManager();
            tx.begin();
            DBResultSet rs = DBManager.executeQuery("SELECT COUNT(*) FROM FWK_MESSAGE");
            if(rs.next()) System.out.println("FWK_MESSAGE건수  : "+rs.getInt(1));
            
            tx.commit();
           
        }catch(Exception e){
            tx.rollback();
            DBResultSet rs = DBManager.executeQuery("SELECT COUNT(*) FROM FWK_MESSAGE");
            if(rs.next()) System.out.println("rollback 후 emp건수 : "+rs.getInt(1));
        }finally{
            tx.end();
        }
    }
}
