package test.spidertest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.commons.lang.CharUtils;

import nebsoa.common.Context;
import nebsoa.common.jdbc.DBHandler;
import nebsoa.common.log.LogManager;

public class BatchFile {

	public ArrayList insertData = new ArrayList();
	public int insertCnt = 0;
	
	public void fileRead(File file)
	{

		FileReader fr = null; 
		BufferedReader br = null;
		String line = "";
		
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			
			while( (line = br.readLine()) != null)
			{
				//파싱
				try{
					addDataArray(line);
				}catch(Exception e){
					//배치 로그에 남기고 계속 진행.
				}

				
				//DB Insert
				try{
					if(insertCnt == 100){

						for(int i = 0 ; i < 100 ; i++){
							String[] temp = (String[]) insertData.get(i);

							LogManager.debug("####i="+i+" temp0="+temp[0]+", temp1="+temp[1]+", temp2="+temp[2]
							                 +", temp3="+temp[3]+", temp4="+temp[4]+", temp5="+temp[5]+", temp6="
							                 +temp[6]+", temp7="+temp[7]+", temp8="+temp[8]+", temp9="+temp[9]
							                 +", temp10="+temp[10]+", temp11="+temp[11]+", temp12="+temp[12]+", temp13="
							                 +temp[13]+", temp14="+temp[14]+", temp15="+temp[15]);

						}
						
						executeBatch();
						
						// TODO 배치 에러가 나면 수행되지 않는 문제 발생. 
						insertData.clear();
						insertCnt = 0;

					}
				}catch(Exception e){
					//배치 로그에 남기고 계속 진행.
				}
				
			}
			
			try {
			
				if( (insertCnt % 100) != 0){
					for(int i = 0 ; i < insertCnt ; i++){
						String[] temp = (String[]) insertData.get(i);
						LogManager.debug("####i="+i+" temp0="+temp[0]+", temp1="+temp[1]+", temp2="+temp[2]
					                 +", temp3="+temp[3]+", temp4="+temp[4]+", temp5="+temp[5]+", temp6="
					                 +temp[6]+", temp7="+temp[7]+", temp8="+temp[8]+", temp9="+temp[9]
					                 +", temp10="+temp[10]+", temp11="+temp[11]+", temp12="+temp[12]+", temp13="
					                 +temp[13]+", temp14="+temp[14]+", temp15="+temp[15]);
	
	
					}
					executeBatch();

				}

			}catch(Exception e){
				//배치 로그에 남기고 계속 진행.
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally
		{
			try {
				if(fr != null)
					fr.close();
				if(br != null)
					br.close();
			} catch (IOException e) {
			}
		}
	}
	
	
	/**
	 * 라인 해석하기
	 * @param contents
	 */
	public void addDataArray(String contents)
	{

			String [] fromData = contents.split("\\|");
			String [] toData = new String[16];
			
			if( fromData[0].length() != 8 || !CharUtils.isAsciiNumeric(fromData[0].charAt(0)) ) {
				return;
			}

			//3번째 데이타는 쓰레기임.
//			fromData[0] = fromData[0];//거래발생일
//			fromData[1] = fromData[1].length() > 6 ? fromData[1].substring(0,6) : fromData[1];//거래시간
//			fromData[2] = fromData[2].length() > 3 ? fromData[2].substring(0,3) : fromData[2];//서버구분
//			fromData[4] = fromData[4].length() > 8 ? fromData[4].substring(0,8) : fromData[4];//일련번호
//			fromData[5] = fromData[5].length() > 4 ? fromData[5].substring(0,4) : fromData[5];//송수신구분
//			fromData[6] = fromData[6].length() > 2 ? fromData[6].substring(0,2) : fromData[6];//매체코드
//			fromData[7] = fromData[7].length() > 4 ? fromData[7].substring(0,4) : fromData[7];//부점코드
//			fromData[8] = fromData[8].length() > 3 ? fromData[8].substring(0,3) : fromData[8];//단말번호
//			fromData[9] = fromData[9].length() > 11 ? fromData[9].substring(0,11) : fromData[9];//거래코드
//			fromData[10] = fromData[10].length() > 8 ? fromData[10].substring(0,8) : fromData[10];//에러코드
//			fromData[11] = fromData[11].length() > 28 ? fromData[11].substring(0,28) : fromData[11];//거래로그번호
//			fromData[12] = fromData[12].length() > 13 ? fromData[12].substring(0,13) : fromData[12];//주민등록번호
//			fromData[13] = fromData[13].length() > 1 ? fromData[13].substring(0,1) : fromData[13];//고객구분
//			fromData[14] = fromData[14].length() > 10 ? fromData[14].substring(0,10) : fromData[14];//전자금융 ID
//			fromData[15] = fromData[15].length() > 15 ? fromData[15].substring(0,15) : fromData[15];//IP
//			fromData[16] = fromData[16].length() > 4000 ? fromData[16].substring(0,4000) : fromData[16];//입출력데이터

			toData[0] = fromData[0];//거래발생일
			toData[1] = fromData[1].length() > 6 ? fromData[1].substring(0,6) : fromData[1];//거래시간
			toData[2] = fromData[2].length() > 3 ? fromData[2].substring(0,3) : fromData[2];//서버구분
			toData[3] = fromData[4].length() > 8 ? fromData[4].substring(0,8) : fromData[4];//일련번호
			toData[4] = fromData[5].length() > 4 ? fromData[5].substring(0,4) : fromData[5];//송수신구분
			toData[5] = fromData[6].length() > 2 ? fromData[6].substring(0,2) : fromData[6];//매체코드
			toData[6] = fromData[7].length() > 4 ? fromData[7].substring(0,4) : fromData[7];//부점코드
			toData[7] = fromData[8].length() > 3 ? fromData[8].substring(0,3) : fromData[8];//단말번호
			toData[8] = fromData[9].length() > 11 ? fromData[9].substring(0,11) : fromData[9];//거래코드
			toData[9] = fromData[10].length() > 8 ? fromData[10].substring(0,8) : fromData[10];//에러코드
			toData[10] = fromData[11].length() > 28 ? fromData[11].substring(0,28) : fromData[11];//거래로그번호
			toData[11] = fromData[12].length() > 13 ? fromData[12].substring(0,13) : fromData[12];//주민등록번호
			toData[12] = fromData[13].length() > 1 ? fromData[13].substring(0,1) : fromData[13];//고객구분
			toData[13] = fromData[14].length() > 10 ? fromData[14].substring(0,10) : fromData[14];//전자금융 ID
			toData[14] = fromData[15].length() > 15 ? fromData[15].substring(0,15) : fromData[15];//IP
			toData[15] = fromData[16].length() > 4000 ? fromData[16].substring(0,4000) : fromData[16];//입출력데이터			

			insertData.add(toData);
			insertCnt++;
	}

	

	public int[] executeBatch(){
		
		int [] updateCounts = null;

		try {
	    
	        String sql = "INSERT INTO IBSLOG VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'',?)";

	        /*updateCounts =*/ DBHandler.executeBatch(new Context(),null,sql, insertData , 100 );
	        
	    
//	        String[] data = null;
//
//	        for (int i=0; i < insertData.size() ; i++) {
//	            data = (String[]) insertData.get(i);
//	        	
//	        	pstmt.setString(1, data[0]);
//	            pstmt.setString(2, data[1]);
//	            pstmt.setString(3, data[2]);
//	            pstmt.setString(4, data[4]);
//	            pstmt.setString(5, data[5]);
//	            pstmt.setString(6, data[6]);
//	            pstmt.setString(7, data[7]);
//	            pstmt.setString(8, data[8]);
//	            pstmt.setString(9, data[9]);
//	            pstmt.setString(10, data[10]);
//	            pstmt.setString(11, data[11]);
//	            pstmt.setString(12, data[12]);
//	            pstmt.setString(13, data[13]);
//	            pstmt.setString(14, data[14]);
//	            pstmt.setString(15, data[15]);
//	            pstmt.setString(16, data[16]);
//	            
//	            pstmt.addBatch();
//	        }
//	    
//	        // batch 실행
//	        updateCounts = pstmt.executeBatch();
	    
	        processUpdateCounts(updateCounts);
	    
	    } catch (Exception e) {

	    	LogManager.debug("####BatchUpdateException=>"+e.getMessage());


	    	// Not all of the statements were successfully executed
//	        updateCounts = e.getUpdateCounts();
	    
	        // Some databases will continue to execute after one fails.
	        // If so, updateCounts.length will equal the number of batched statements.
	        // If not, updateCounts.length will equal the number of successfully executed statements
	        processUpdateCounts(updateCounts);
	    
	    //꼭 로그를 남겨야 할 이유가 있을 경우.
	    } finally {
	    	
	    }
	    return updateCounts;
	}	    

	
	public void processUpdateCounts(int[] updateCounts) {
        for (int i=0; i<updateCounts.length; i++) {
	    	LogManager.debug("####updateCounts : "+updateCounts[i]);	    	
        	if (updateCounts[i] >= 0) {
    	    	LogManager.debug("####Successfully executed : "+i);	    	
                // Successfully executed; the number represents number of affected rows
            } else if (updateCounts[i] == Statement.SUCCESS_NO_INFO) {
    	    	LogManager.debug("####number of affected rows not available : "+i);
                // Successfully executed; number of affected rows not available
            } else if (updateCounts[i] == Statement.EXECUTE_FAILED) {
    	    	LogManager.debug("####Failed to execute : "+i);
                // Failed to execute
            }
        }
    }

	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long sTime = System.currentTimeMillis();

//		File file = new File("D:\\testSpace\\TestSrc\\testfiles\\msgfiles\\MESSAGE_CA21-2006_09_13_14.log");
		File file = new File("D:\\testSpace\\TestSrc\\testfiles\\msgfiles\\MESSAGE_CA21-2006_09_20_18.log");
//		File file = new File("D:\\testSpace\\TestSrc\\testfiles\\msgfiles\\msg.txt");

		BatchFile bf = new BatchFile();

		bf.fileRead(file);

		System.out.println("#### 걸린 시간 ="+(System.currentTimeMillis()-sTime)/1000);
	}
}
