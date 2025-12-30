/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.error;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.collections.map.MultiKeyMap;

import nebsoa.common.Constants;
import nebsoa.common.collection.DataSet;
import nebsoa.common.error.handler.ErrorHandler;
import nebsoa.common.exception.SysException;
import nebsoa.common.jdbc.DBManager;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.DataMap;
import nebsoa.common.util.FormatUtil;
import nebsoa.common.util.PropertyManager;
import nebsoa.common.util.StringUtil;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 오류별 처리 핸들러를 Call하는 클래스입니다.
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
 * $Log: ErrorManager.java,v $
 * Revision 1.1  2018/01/15 03:39:54  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:39  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:27  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:29  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:33  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.3  2008/01/08 06:15:34  김성균
 * INSERT 쿼리 수정
 *
 * Revision 1.2  2007/11/30 09:46:51  안경아
 * DB NAME 지정
 *
 * Revision 1.1  2007/11/26 08:39:08  안경아
 * *** empty log message ***
 *
 * Revision 1.12  2007/09/06 15:08:49  최수종
 * *** empty log message ***
 *
 * Revision 1.11  2007/09/06 11:21:38  최수종
 * *** empty log message ***
 *
 * Revision 1.10  2007/09/06 06:10:45  최수종
 * *** empty log message ***
 *
 * Revision 1.9  2007/09/05 09:37:53  최수종
 * *** empty log message ***
 *
 * Revision 1.8  2007/08/22 02:46:18  최수종
 * *** empty log message ***
 *
 * Revision 1.7  2007/08/20 06:23:14  최수종
 * *** empty log message ***
 *
 * Revision 1.6  2007/08/17 12:12:29  최수종
 * 오류핸들러맵핑정보 캐쉬 저장 처리
 *
 * Revision 1.5  2007/07/19 06:50:19  최수종
 * *** empty log message ***
 *
 * Revision 1.4  2007/07/08 05:06:20  최수종
 * 오류핸들러 컬럼 추가(HANDLE_APP_FILE)
 *
 * Revision 1.3  2007/06/26 05:00:33  최수종
 * *** empty log message ***
 *
 * Revision 1.2  2007/06/26 04:59:58  최수종
 * *** empty log message ***
 *
 * Revision 1.1  2007/06/20 10:41:46  최수종
 * 오류 핸들러 관련 수정 및 추가
 *
 *
 * </pre>
 ******************************************************************/
public class ErrorManager {
	
	private static Object dummy = new Object();

	// 오류별 처리 핸들러 정보 조회
    private static final String ERROR_HANDLE_APP_LIST_SQL = 
		"SELECT A.ERROR_CODE, A.HANDLE_APP_ID, " +
		"B.HANDLE_APP_NAME, A.USER_PARAM_VALUE, " +
		"B.SYS_PARAM_VALUE, B.HANDLE_APP_FILE " +
		"FROM FWK_ERROR_HANDLE_APP A, FWK_HANDLE_APP B " +
		"WHERE A.HANDLE_APP_ID = B.HANDLE_APP_ID " +
		"ORDER BY A.ERROR_CODE ASC ";
	
	// 오류별 처리 핸들러 수행 로그 정보 등록
    private static final String ERROR_HANDLE_APP_INSERT_SQL =     
    	"INSERT INTO FWK_ERROR_HANDLE_HIS ( " +
		"ERROR_CODE, HANDLE_APP_ID, ERROR_SER_NO, " + 
		"ERROR_HANDLE_DTIME, ERROR_HANDLE_RT_CODE, ERROR_HANDLE_TEXT) " + 
		"VALUES ( ?, ?, ?, ?, ?, ?) ";    
    
    private static ErrorManager instance;
    
    private HashMap errorHandlerMap;     // 오류별 처리 핸들러 정보를 담고 있는 맵
    private MultiKeyMap executeTimeMap;  // 각 오류별 처리 핸들러의 수행시간을 담고 있는 맵
    
    /**
     * 싱글톤 처리 
     */
    private ErrorManager(){ 
    	errorHandlerMap = new HashMap();
    	executeTimeMap = new MultiKeyMap();
    }
    
    /**
     * 싱글톤 객체 얻어오기
     * 인스턴스 생성하고 데이타 로딩...
	 * @return
	 */
	public static ErrorManager getInstance() {
		if (instance == null) {
			synchronized (dummy) {
				instance = new ErrorManager();
				instance.loadErrorHandler();
			}
		}
		return instance;
	}
    
    
    /**
	 * 장애 핸들러 정보 reload
	 * 
	 */
    public void reload() {
    	errorHandlerMap.clear();
    	executeTimeMap.clear();
    	loadErrorHandler();
    }
    
    /**
     * 장애 핸들러 정보 load
     *
     */
    private void loadErrorHandler()
    {
    	
		DataSet rs = DBManager.executeQuery(Constants.SPIDER_DB, ERROR_HANDLE_APP_LIST_SQL);
		
		String handleAppName = null;
		String userParamVaule = null;
		String sysParamValue = null;
		String handleAppId = null;
		String handleAppFile = null;
		String errorCode = null;
		
		ArrayList dataList = null;
		String oldId = "";
		
		while(rs.next())
		{
			errorCode = rs.getString("ERROR_CODE");  //오류 코드
			handleAppId = rs.getString("HANDLE_APP_ID");  //오류처리 핸들러 ID
			handleAppName = rs.getString("HANDLE_APP_NAME");  //오류처리 핸들러 이름
			userParamVaule = rs.getString("USER_PARAM_VALUE");  // 사용자용 파라미터 설정
			sysParamValue = rs.getString("SYS_PARAM_VALUE");  // 시스템용 파라미터 설정
			handleAppFile = rs.getString("HANDLE_APP_FILE");  //오류처리 핸들러 클래스명
			
			
			HashMap resultMap = new HashMap();
			resultMap.put("ERROR_CODE", errorCode);
			resultMap.put("HANDLE_APP_ID", handleAppId);
			resultMap.put("HANDLE_APP_NAME", handleAppName);
			resultMap.put("USER_PARAM_VALUE", userParamVaule);
			resultMap.put("SYS_PARAM_VALUE", sysParamValue);
			resultMap.put("HANDLE_APP_FILE", handleAppFile);
			
			
			if(oldId.equals(errorCode))  // 오류코드가 중복된 경우
			{
				dataList.add(resultMap);
			}
			else  // 오류코드가 신규일 경우
			{
				dataList = new ArrayList();
				dataList.add(resultMap);
				
				errorHandlerMap.put(errorCode, dataList);
			}
			
			oldId = errorCode;
		}  // end while
		
		LogManager.debug("@@@@@@ errorHandlerMap ====> "+ errorHandlerMap);

    }
    
    
    
    /**
     * 오류별 처리 핸들러 호출
     * 
     * @param map
     */
	public void execute(DataMap map) throws Exception
	{

		LogManager.debug("@@@@@ ErrorManager start time => "+FormatUtil.getToday("yyyy-MM-dd hh:mm:ss"));
		
		LogManager.debug("@@@@@@ ErrorManager.execute()====> errorCode :"+map.getParameter("errorCode"));
		
		
//		Object[] paramList = {
//				map.getParameter("errorCode")
//		};
//		
//		DataSet rs = DBManager.executePreparedQuery(
//				ERROR_HANDLE_APP_LIST_SQL, paramList);
		
//		String handleAppName = null;
		String userParamVaule = null;
		String sysParamValue = null;
		String handleAppId = null;
		String handleAppFile = null;
		
		// 오류 핸들러 수행 결과 코드
		// 99999999 : 에러, 00000000 : 정상
		String executeResultCode = null;   
		
		// 오류 핸들러 수행 결과 내용
		String executeResultText = null;  
		
		// 오류 코드
		String errorCode = map.getParameter("errorCode");
		
		
		ArrayList data = null; 
		
		if(errorHandlerMap.get(errorCode) == null)
		{
			LogManager.info("@@@@@ 해당 오류코드에 장애 처리 핸들러가 맵핑되어 있지 않습니다. 오류코드 =>["+ errorCode +"]");
			LogManager.debug("@@@@@ ErrorManager end time => "+FormatUtil.getToday("yyyy-MM-dd hh:mm:ss"));
			return;
		}
		else
		{
			data = (ArrayList)errorHandlerMap.get(errorCode);
		}
		
		HashMap resultMap = null;
		long executeTime = 0L;  // 오류 핸들러 수행시간 
		long currentTime = 0L;  // 현재 시간
		long passTime = PropertyManager.getLongProperty("default", "ERROR_HANDLER_EXECUTE_PERIOD", 1800000L);  // 경과 시간( 1800000 (m/s) == 60 * 30 (초) = 30(분) )
		boolean flag = true;
		
		for(int i=0 ; i<data.size() ; i++)
		{
			LogManager.debug("@@@@@ ErrorManager loop start time => "+FormatUtil.getToday("yyyy-MM-dd hh:mm:ss"));
			
			flag = true;
			
			try
			{
				resultMap = (HashMap)data.get(i);
				
				LogManager.debug("@@@@@@ resultMap ====> "+ resultMap);
				
				handleAppId = StringUtil.NVL(resultMap.get("HANDLE_APP_ID"));  //오류처리 핸들러 ID
				userParamVaule = StringUtil.NVL(resultMap.get("USER_PARAM_VALUE"));  // 사용자용 파라미터 설정
				sysParamValue = StringUtil.NVL(resultMap.get("SYS_PARAM_VALUE"));  // 시스템용 파라미터 설정
				handleAppFile = StringUtil.NVL(resultMap.get("HANDLE_APP_FILE"));  //오류처리 핸들러 클래스명
				
				if(executeTimeMap.get(errorCode, handleAppId) != null)
				{
					executeTime = ((Long)executeTimeMap.get(errorCode, handleAppId)).longValue();
					currentTime = System.currentTimeMillis();
					
					LogManager.debug("@@@@@ executeTime ========>"+ executeTime);
					LogManager.debug("@@@@@ currentTime ========>"+ currentTime);
					LogManager.debug("@@@@@ passTime ========>"+ passTime);
					LogManager.debug("@@@@@ currentTime - executeTime ========>"+ (currentTime - executeTime));
					
					// 이전 오류 핸들러 수행후, passTime만큼 경과해야 수행함.(default는 30분) 
					if((currentTime - executeTime) <= passTime)
					{
						flag = false;
						break;
					}
				}
				
				LogManager.debug("@@@@@ executeTimeMap ========>"+ executeTimeMap);
				

				// 오류별 처리 핸들러 인스턴스 생성
				ErrorHandler handler = (ErrorHandler)forName(handleAppFile);
				
				// 오류별 처리 핸들러 수행
				handler.executeHandler(userParamVaule, sysParamValue);
				
				// 수행 결과 처리
				executeResultCode = "00000000";
				executeResultText = "완료";
				
				LogManager.debug("@@@@@@ 오류별 처리 핸들러 ID:["+ handleAppId +"] 수행 완료!!! ");
			}
			catch(Exception e)
			{
				LogManager.error("오류별 처리 핸들러 ID:["+ handleAppId +"] 수행중 에러가 발생했습니다. ");
				
//				StringBuffer errorMsgBuf = new StringBuffer(e.toString());  // 에러메시지
//				
//				
//				Object[] obj = e.getCause().getStackTrace();
//				for(int i=0 ; i<obj.length ; i++)
//				{
//					errorMsgBuf.append("\n");
//					errorMsgBuf.append(obj[i]);
//				}
//				executeResultText = errorMsgBuf.toString();
//				
//				byte[] executeResultByte = executeResultText.getBytes();
//				byte[] executeResultTempByte = new byte[2000];
//				
//				if(executeResultByte.length > 2000)
//				{
//					System.arraycopy(executeResultByte, 0, executeResultTempByte, 0, 2000);
//				}				
//				
				executeResultCode = "99999999";
				executeResultText = e.getMessage();
//				executeResultText = new String(executeResultTempByte);
//				
//				LogManager.debug("@@@@ executeResult : ["+ executeResultText +"]");
//				
//				e.printStackTrace();
				
			}
			finally
			{
				if(flag)
				{
					LogManager.debug("@@@@@ 오류 처리 핸들러 수행 이력 저장 ");
					
					Object[] params = {
							map.getParameter("errorCode"),
							handleAppId,
							map.getParameter("trxSerNo"),
							FormatUtil.getToday("yyyyMMddHHmmss"),
							executeResultCode,
							executeResultText
					};
					
					// DB에 오류별 처리 핸들러 수행 내역 저장
					DBManager.executePreparedUpdate(Constants.SPIDER_DB, 
							ERROR_HANDLE_APP_INSERT_SQL, params);
					
					// 수행 완료된 시점의 시간 저장
					executeTimeMap.put(errorCode, handleAppId, Long.valueOf(System.currentTimeMillis()));
					
				}
				else
				{
					LogManager.debug("@@@@@ 오류 처리 핸들러 수행 이력 저장 skip ");
				}
			}
			
			LogManager.debug("@@@@@ ErrorManager loop end time => "+FormatUtil.getToday("yyyy-MM-dd hh:mm:ss"));
			
		}  // end while
		

		LogManager.debug("@@@@@ ErrorManager end time => "+FormatUtil.getToday("yyyy-MM-dd hh:mm:ss"));
	}
	
	/**
	 * Runtime시에 클래스 Object를 생성하여 리턴한다.
	 * 
	 * @param className
	 * @return
	 */
    public static Object forName(String className){
        try {
            return  Class.forName(className).newInstance();
        } catch (InstantiationException e1) {
           throw new SysException("[CANN'T MAKE INSTANCE 생성자 체크 :"+className+"]");
        } catch (IllegalAccessException e1) {
           throw new SysException("[CANN'T MAKE INSTANCE 생성자 PUBLIC 인지 체크 :"+className+"]");
        }catch(ClassNotFoundException e){
           throw new SysException("[CLASS_NOT_FOUND:"+e.getMessage()+"]");
        }
    }  	
	
	
    
	public static void main(String[] args) {

		try {
			for (int i = 0; i < 2; i++) {
				DataMap map = new DataMap();

				String errorCode = "FRM00011";
				String trxSerNo = "20070906233822CA11206791637" + i;

				map.put("errorCode", errorCode); // 오류코드 
				map.put("trxSerNo", trxSerNo); // 오류 일련번호
				ErrorManager.getInstance().execute(map);
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
	}    
	
}






