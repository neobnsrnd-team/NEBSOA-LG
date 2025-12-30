package test.ibatis;

import java.sql.SQLException;

import com.ibatis.sqlmap.client.SqlMapClient;

import nebsoa.common.exception.DBException;
import nebsoa.common.jdbc.DBHandler;
import nebsoa.common.jdbc.IbatisTxManager;
import nebsoa.common.jdbc.SqlMapClientConfig;
import nebsoa.common.jdbc.TxServiceFactory;
import nebsoa.common.jdbc.context.DBContext;
import nebsoa.common.util.DataMap;

public class TransactionTest {

	public static void main(String[] args) {
		
		TransactionTest tranTest = new TransactionTest();
//        for(int i = 0 ; i < 2; i++) {
//    		new Thread(tranTest.new TestThread("a1"+i,"샘플"+i), "쓰레드"+i ).start();        	
//        }
        
        for(int i = 0 ; i < 4; i++) {
    		new Thread(tranTest.new TestThread2("a1"+i,"샘플"+i), "쓰레드"+i ).start();        	
        }
	}
	

    class TestThread implements Runnable {
        
    	String userId;
    	String userName;
    	
    	public TestThread(String userid, String userName) {
    		this.userId = userid;
    		this.userName = userName;
    	}
    	
        public void run() {
			SqlMapClient sqlMapper = SqlMapClientConfig.getSqlMapInstance(null);

			try {
				sqlMapper.startTransaction();
				DataMap map = new DataMap();

				map.put("userId", userId);
				map.put("userName", userName);
				map.put("descr", "테스터desc");
				map.put("count", "0");
				map.put("loc", "KO");
				sqlMapper.insert("IbatisSample.insertIbatisSample",map);
				
				if(userName.endsWith("0")) {
					Thread.currentThread().sleep(1000);
				} else if(userName.endsWith("1")){
					map.put("userId", userId+"test");					
				} else if(userName.endsWith("2")){
					map.put("userId", userId+"test");					
				} else {
					map.put("userId", userId+"test");					
				}

				map.put("userName", userName);
				sqlMapper.insert("IbatisSample.insertIbatisSample",map);
//	            sqlMapper.update("IbatisSample.updateIbatisSample",map);
				sqlMapper.commitTransaction();

			} catch (SQLException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				try {
					System.out.println("### endTr ###");
					sqlMapper.endTransaction();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

        }
    }
    
    
    
    
    class TestThread2 implements Runnable {
        
    	String userId;
    	String userName;
    	
    	public TestThread2(String userid, String userName) {
    		this.userId = userid;
    		this.userName = userName;
    	}

    	public void run() {

    		System.out.println("####"+Thread.currentThread().getName()+" START ###");			
    		IbatisTxManager ibstx = (IbatisTxManager)TxServiceFactory.getTxService(null, 1);

			try {
				ibstx.begin();
				
				DataMap map = new DataMap();

				map.put("userId", userId);
				map.put("userName", userName);
				map.put("descr", "테스터desc");
				map.put("count", "0");
				map.put("loc", "KO");
				
				DBContext ctx1 = new DBContext("IbatisSample", "insertIbatisSample", map);
				DBHandler.insert(ibstx, ctx1);
				
				if(userName.endsWith("0")) {
					Thread.currentThread().sleep(1000);
				} else if(userName.endsWith("1")){
					map.put("userId", userId+"1");					
				} else if(userName.endsWith("2")){
					map.put("userId", userId+"2");					
				} else {
					map.put("userId", userId+"3");					
				}

				map.put("userName", userName);
		    	DBContext ctx2 = new DBContext("IbatisSample", "insertIbatisSample", map);

		    	DBHandler.insert(ibstx, ctx2);
//	            DBHandler.update(ibstx,ctx2);

		    	ibstx.commit();
			} catch (DBException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				ibstx.end();
				System.out.println("### endTr ###");
	    		System.out.println("####"+Thread.currentThread().getName()+" END ###");			

			}

        }
    }
    
    
    
}
