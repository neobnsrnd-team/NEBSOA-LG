/*******************************************************************
 * <pre>
 * 1.설명 
 * Transaction 처리 Utility
 * 
 * 2.사용법
 * 
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author instructor
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * $Log: DbTxUtil.java,v $
 * Revision 1.1  2018/01/15 03:39:49  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:31  cvs
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
 * Revision 1.5  2008/09/17 12:24:13  jglee
 * thread 서비스 처리시 에러 로그 처리
 *
 * Revision 1.4  2008/09/10 07:48:36  jglee
 * 트랜잭션 file로그처리에서 db로그처리로 수정
 *
 * Revision 1.3  2008/09/08 13:19:07  jglee
 * rollbackTransaction시 에러 처리 수정
 *
 * Revision 1.2  2008/09/08 12:24:36  jglee
 * Transaction 로그 처리 추가
 *
 * Revision 1.1  2008/09/01 13:01:11  jglee
 * DataMap 에 begin, commit, rollback Transaction 처리
 *
 *
 * </pre>
 ******************************************************************/ 
package nebsoa.common.util;

import java.sql.SQLException;

import nebsoa.biz.base.ErrorManageBiz;
import nebsoa.biz.exception.BizException;
import nebsoa.common.Context;
import nebsoa.common.collection.DataSet;
import nebsoa.common.error.ErrorManager;
import nebsoa.common.exception.AuthFailException;
import nebsoa.common.exception.DBException;
import nebsoa.common.exception.LoginException;
import nebsoa.common.exception.ParamException;
import nebsoa.common.exception.SpiderException;
import nebsoa.common.exception.SysException;
import nebsoa.common.exception.UserException;
import nebsoa.common.jdbc.DBHandler;
import nebsoa.common.jdbc.DBManager;
import nebsoa.common.jdbc.IbatisTxManager;
import nebsoa.common.jdbc.TxServiceFactory;
import nebsoa.common.jdbc.context.DBContext;
import nebsoa.common.log.LogManager;
import nebsoa.service.context.ServiceContext;
import nebsoa.service.info.Component;
import nebsoa.spiderlink.exception.MessageException;

public class DbTxUtil {

//	private static final String errorCode 				= "FRD10001";	// 유효하지 않은 입력값
	private static final String errorCode_noneExist 		= "FRD10002";	// IbatisTxManager 존재하지 않음.
	private static final String errorCode_noBeginTran 	= "FRD10003";	// 트랜젝션이 시작되지 않음.
	private static final String errorCode_duplicate 		= "FRD10004";	// 트랜잭션이 중복 or 이미 실행중
	private static final String errorCode_errCreate 		= "FRD10005";	// 트랜잭션 생성 오류
	private static final String errorCode_rollbackDev 	= "FRD10006";	// 개발자가 rollback처리
	private static final String errorCode_rollbackComp 	= "FRD10007";	// 컴포넌트에서 rollback처리
	private static final String errorCode_noneRelation 	= "FRD10008";	// 트랜잭션 이외에서 에러발생
	private static final String errorCode_Title = "[트랜잭션]시스템에러";	
	
	private static final String BEGIN_TRAN = "BEGIN_TRAN";
	private static final String END_TRAN = "END_TRAN";
	private static final String TRAN_PREFIX = "$TRAN_";

	private static final String errorTitle_thread = "[백그라운드]서비스처리에러";
	private static final String errorCode_thread = "FRD12001";

	/**
	 * DataMap에 TxManager(IbatisTxManager) 세팅 처리
	 * @param map
	 * @return
	 * @throws DBException
	 */
	public static DataMap beginTransaction(DataMap map) throws DBException {
		IbatisTxManager tx = null;
		LogManager.debug("### beginTransaction 처리 시작 ###");
		
		String DB_NAME = map.getString("DB_NAME");
		LogManager.debug("*** DB_NAME : " + DB_NAME);
		
		try{
			LogManager.debug("*** 중복된 beginTransaction 을 체크합니다");
			LogManager.debug("*** transactionMode : " + map.isTransactionMode());
			LogManager.debug("*** TxManager : " + tx);
			if(map.getTxManager() != null){
				//if(map.isTransactionMode()){
					LogManager.debug("*** 이전 transaction을 commit합니다");
					commitTransaction(map);
				//}
				//throw new DBException(errorCode_duplicate);
			}

			LogManager.debug("*** transaction을 생성합니다");
			tx = (IbatisTxManager) TxServiceFactory.getTxService(DB_NAME, 1);
			if(tx == null){
				LogManager.debug("### TxManager 생성 오류 ###");
				throw new DBException(errorCode_errCreate);
			}

			tx.begin();
			map.setTxManager(tx);
			LogManager.debug("*** beginTransaction Owner : " + map.getTransactionOwner());
			LogManager.debug("*** transactionMode : " + map.isTransactionMode());

		}catch(DBException e){
			LogManager.debug("### beginTransaction 처리중 에러 발생 ###");
			LogManager.debug(e);
			rollbackTransaction(map,e);	
			throw e;
		}catch(Exception ex){
			LogManager.debug("### beginTransaction 처리중 에러 발생 ###");
			LogManager.debug(ex);
			rollbackTransaction(map,ex);	
			throw new DBException(ex.toString());
		}finally{

		}
		LogManager.debug("### beginTransaction 처리 종료 ###");
		return map;
	}
	
	/**
	 * DataMap의 TxManager로 commitTransaction 처리
	 * @param map
	 * @return
	 * @throws DBException
	 */
	public static DataMap commitTransaction(DataMap map) throws DBException {
		IbatisTxManager tx = null;
		LogManager.debug("### commitTransaction 처리 시작 ###");
		try{
			LogManager.debug("*** transaction 존재유무를 확인합니다");
			if(map.getTxManager()==null || !map.isTransactionMode()){
				LogManager.debug("*** transactionMode : " + map.isTransactionMode());
				LogManager.debug("*** transaction이 존재하지 않습니다");
				LogManager.debug("### commitTransaction 종료 ###");
				return map;
			}
			
			LogManager.debug("*** commitTransaction Owner : " + map.getTransactionOwner());
			LogManager.debug("*** transactionMode : " + map.isTransactionMode());

			tx = getTx(map);
			tx.commit();
		}catch(DBException e){
			LogManager.debug("### commitTransaction 처리중 에러 발생 ###");
			LogManager.debug(e.getMessage());
			LogManager.debug(e);
			rollbackTransaction(map,e);
			throw e;
		}catch(Exception ex){
			LogManager.debug("### commitTransaction 처리중 에러 발생 ###");
			LogManager.debug(ex.getMessage());
			LogManager.debug(ex);
			rollbackTransaction(map,ex);	
			throw new DBException(ex.toString());
		}finally{
			if(tx != null){
				tx.end();
				map.setTxManager(null);
			}
			LogManager.debug("### commitTransaction 처리 완료 상태 ###");
			LogManager.debug("*** transactionMode : " + map.isTransactionMode());
			LogManager.debug("*** transaction Owner : " + map.getTransactionOwner());
		}
		LogManager.debug("### commitTransaction 종료 ###");
				
		return map;
	}
	
	/**
	 * DataMap의 TxManager로 rollbackTransaction 처리
	 * @param map
	 * @return
	 * @throws DBException
	 */
	public static DataMap rollbackTransaction(DataMap map) throws DBException {
		return rollbackTransaction(map, null);
	}
	
	/**
	 * DataMap의 TxManager로 rollbackTransaction 처리
	 * @param map
	 * @return
	 * @throws DBException
	 */
	public static DataMap rollbackTransaction(DataMap map, Throwable ep) throws DBException {
		IbatisTxManager tx = null;
		LogManager.debug("### rollbackTransaction 처리 시작 ###");
		try{
			LogManager.debug("*** transaction 존재유무를 확인합니다");
			if(map.getTxManager()==null || !map.isTransactionMode()){
				LogManager.debug("*** transactionMode : " + map.isTransactionMode());
				LogManager.debug("*** transaction이 존재하지 않습니다");
				return map;
			}
			//logging 처리
			//getTransactionLog(map);
			tx = getTx(map);
			tx.rollback();
		}catch(DBException e){
			LogManager.debug(e);
			throw e;
		}catch(Exception ex){
			LogManager.debug(ex);
			throw new DBException(ex.toString());
		}finally{
			if(tx != null){
				tx.end();
			}
			loggerTran(map,ep);
			map.setTxManager(null);
			
			LogManager.debug("### rollbackTransaction 처리 완료 상태 ###");
			LogManager.debug("*** transactionMode : " + map.isTransactionMode());
			LogManager.debug("*** transaction Owner : " + map.getTransactionOwner());
		}
		LogManager.debug("### rollbackTransaction 처리 종료 ###");
				
		return map;
	}
	
	/**
	 * trsansaction 모드 상태 리턴
	 * @param dbCtx
	 * @return IbatisTxManager
	 * @throws DBException
	 */
	public static boolean isTransactionMode(DBContext dbCtx) throws DBException{
		DataMap map = dbCtx.getMap();
		if(map.isTransactionMode() && map.getTransactionStatus().equals(BEGIN_TRAN)){
			return true;
		}
		return false;
	}
	
	/**
	 * DBContext의 DataMap에서 IbatisTxManager를 얻어 리턴
	 * @param dbCtx
	 * @return IbatisTxManager
	 * @throws DBException
	 */
	public static IbatisTxManager getTx(DBContext dbCtx) throws DBException {
		IbatisTxManager tx;
		DataMap map = dbCtx.getMap();
		tx = getTx(map);
		return tx;
	}
	
	/**
	 * DataMap 에 있는 IbatisTxManager를 리턴 처리
	 * @param map
	 * @return IbatisTxManager
	 * @throws DBException
	 */
	public static IbatisTxManager getTx(DataMap map) throws DBException{
		IbatisTxManager tx;
		try{
			if(map.getTxManager()==null){
				LogManager.debug("### getTx ERROR ===  errorCode_noneExist ###");
				throw new DBException(errorCode_noneExist);
			}
			if(!map.isTransactionMode()){
				LogManager.debug("### getTx ERROR ===  errorCode_noBeginTran ###");
				throw new DBException(errorCode_noBeginTran);
			}
			tx = map.getTxManager();
		}catch(DBException e){
			throw e;
		}
		return tx;
	}
	
	/**
	 * 컴포넌트정보 dataMap에 저장(Transaction처리용)
     * @param dataMap
     * @param serviceContext
     * @param component
     * @param idx
     * @return
     * @throws DBException
     */
    public static DataMap setTransactionLog(DataMap dataMap, ServiceContext serviceContext, Component component) throws DBException {
    	dataMap.put(TRAN_PREFIX + "SERIVCE_ID",serviceContext.getServiceId());
    	dataMap.put(TRAN_PREFIX + "COMPONENT_ID",component.getComponentId());
        return dataMap;
    }
    
	/**
	 * Transaction Log 처리.
	 * @param dataMap
	 * @return 
	 */
    public static void getTransactionLog(DataMap dataMap) throws DBException {
    	LogManager.info("SVC_TRAN","서비스 ID : " + dataMap.get(TRAN_PREFIX + "SERIVCE_ID")
    					+ "|컴포넌트 ID : " + dataMap.get(TRAN_PREFIX + "COMPONENT_ID")
    					+ "|거래일련번호 : " + dataMap.getContext().getTrxSerNo()
    					+ "|트랜잭션모드: " + dataMap.isTransactionMode()
    					+ "|트랜잭션 Owner : " + dataMap.getTransactionOwner()
    	);
    }
    
    /**
	 * Transaction Log Message리턴 처리.
	 * @param dataMap
	 * @return 
	 */
    public static String getLogMsg(DataMap dataMap, String gubun) throws DBException {
    	String msg = "서비스ID:" + StringUtil.NVL((String)dataMap.get(TRAN_PREFIX + "SERIVCE_ID"),"");
    	msg+= " | 컴포넌트ID:" + StringUtil.NVL((String)dataMap.get(TRAN_PREFIX + "COMPONENT_ID"),"");
    	msg+= " | 거래일련번호:" + StringUtil.NVL((dataMap.getContext()==null)?"":(String)dataMap.getContext().getTrxSerNo(),"");
		if(gubun.equals("TRAN")){
	    	msg+= " | 트랜잭션모드:" + StringUtil.NVL((String)(dataMap.isTransactionMode()+""),"");
			msg+= " | 트랜잭션OWNER: " + StringUtil.NVL((String)dataMap.getTransactionOwner(),"");
		}
    	LogManager.debug(msg);
    	return StringUtil.NVL(msg,"");
    }
    
    private static final String ERROR_HIS_INSERT_SQL = ErrorManageBiz.ERROR_HIS_INSERT_SQL;
    
    /**
     * 트랜잭션 관련 에러 유무 체크
     * @param e
     * @return
     * @throws DBException
     */
    protected static boolean isTransactionError(Throwable e) throws DBException{
    	if(e==null) return false;
    	String errorCode = ((DBException) e).getErrorCode();
    	if(StringUtil.isNull(errorCode)) return false;
    		
    	if(errorCode.equals(errorCode_noneExist) 
    			|| errorCode.equals(errorCode_noBeginTran)
    			|| errorCode.equals(errorCode_duplicate)
    			|| errorCode.equals(errorCode_errCreate)
    			|| errorCode.equals(errorCode_rollbackDev)
 				|| errorCode.equals(errorCode_rollbackComp)){
    		return true;
    	}else{
    		return false;
    	}
    }
    
    /**
     * rollback 처리시 FWK_ERROR_HIS 테이블에 현재 서비스,컴포넌트정보 등록
     * @param map
     * @param e
     * @throws DBException
     */
    public static void loggerTran(DataMap map, Throwable e) throws DBException{
		LogManager.debug("### 서비스,컴포넌트정보 히스토리 저장 시작 ###");
		if(map==null) return;
    	try {
            String errorTime = null;
            String errorCode = null;
            String errorMessage = null;
            String errorTitle = null;
            String trxSerNo = null;
            String userId = null;
            String errorCause = null;
            
    		errorTime = FormatUtil.getFormattedDate(System.currentTimeMillis(), "yyyyMMddHHmmss");
			errorTitle = errorCode_Title;

			if(e==null){	//메소드 호출로 rollback처리시
				if(map.getTransactionOwner().equals("D")){
    				errorCode = errorCode_rollbackDev;
    			}else if(map.getTransactionOwner().equals("C")){
    				errorCode = errorCode_rollbackComp;
    			}else{
    				errorCode = errorCode_noneRelation;
    			}
    		}else{	//Exception발생시
    			if(e instanceof DBException){
    				if(isTransactionError(e)){
        				errorCode =  ((DBException) e).getErrorCode();
        				errorTitle = errorCode_Title;
        				errorCause = ((DBException) e).getMessage();
    				}else{
        				errorCode = errorCode_noneRelation;
        				errorTitle = "시스템 장애";
            			errorCause = e.toString();
    				}
    			}else{
    				errorCode = errorCode_noneRelation;
    				errorTitle = "시스템 장애";
    				errorCause = e.toString();
    			}
    		}

    		Context ctx = map.getContext();
            if (ctx != null) {
                trxSerNo = ctx.getTrxSerNo();
                userId = ctx.getUserId();
            }
            
            errorMessage = getLogMsg(map, "TRAN");
            if (errorCause != null) { 
                errorMessage += " | " + errorCause;
            }

            if (StringUtil.isNull(trxSerNo)) {
                trxSerNo = "NO_DATA";
            }
            
            if (StringUtil.isNull(userId)) {
                userId = "NO_DATA";
            }
            
            if (errorMessage.getBytes().length > 500) {
                byte[] msg = errorMessage.getBytes();
                errorMessage = new String(msg, 0, 500);
            }
            
            LogManager.info(" ############## [실시간 저장 오류정보] ###################");
        	LogManager.info(" ### USER_ID = " + userId);
        	LogManager.info(" ### TRX_SER_NO = " + trxSerNo);
        	LogManager.info(" ### ERROR_TIME = " + errorTime);
        	LogManager.info(" ### ERROR_CODE = " + errorCode);
        	LogManager.info(" ### ERROR_MESSAGE = " + errorMessage);
        	LogManager.info(" #####################################################");
            
            Object[] param = {
                     errorCode
                    ,trxSerNo
                    ,userId
                    ,errorMessage
                    ,errorTime
            };
            
            DBManager.executePreparedUpdate(ERROR_HIS_INSERT_SQL, param);
            
        } catch (Exception ex) {
        	LogManager.debug("### 서비스,컴포넌트정보 히스토리 저장시 오류 ###");
            LogManager.error("ERROR", e.toString(), ex);
        }
		LogManager.debug("### 서비스,컴포넌트정보 히스토리 저장 완료 ###");
    }
     
	/**
     * thread로 서비스 처리중 에러 발생시 FWK_ERROR_HIS 테이블에 등록
     * @param map
     * @param e
     * @throws DBException
     */
    public static void loggerThread(String serviceId, DataMap map, Throwable e) throws DBException{
		LogManager.debug("### 백그라운드 서비스,컴포넌트정보 히스토리 저장 시작 ###");
		if(map==null) return;
    	try {
            String errorTime = null;
            String errorCode = null;
            String errorMessage = null;
            String errorTitle = null;
            String trxSerNo = null;
            String userId = null;
            String errorCause = null;
            
    		errorTime = FormatUtil.getFormattedDate(System.currentTimeMillis(), "yyyyMMddHHmmss");
			errorTitle = errorTitle_thread;
			errorCode = errorCode_thread;
			errorCause = e.toString();

    		Context ctx = map.getContext();
            if (ctx != null) {
                trxSerNo = ctx.getTrxSerNo();
                userId = ctx.getUserId();
            }
            
            errorMessage = getLogMsg(map, "THREAD");
            if (errorCause != null) { 
                errorMessage += " | " + errorCause;
            }

            if (StringUtil.isNull(trxSerNo)) {
                trxSerNo = "NO_DATA";
            }
            
            if (StringUtil.isNull(userId)) {
                userId = "NO_DATA";
            }
            
            if (errorMessage.getBytes().length > 500) {
                byte[] msg = errorMessage.getBytes();
                errorMessage = new String(msg, 0, 500);
            }
            
            LogManager.info(" ############## [백그라운드 저장 오류정보] ###################");
        	LogManager.info(" ### TRX_SER_NO = " + trxSerNo);
        	LogManager.info(" ### ERROR_TIME = " + errorTime);
        	LogManager.info(" ### ERROR_CODE = " + errorCode);
        	LogManager.info(" ### ERROR_MESSAGE = " + errorMessage);
        	LogManager.info(" #####################################################");
            
            Object[] param = {
                     errorCode
                    ,trxSerNo
                    ,userId
                    ,errorMessage
                    ,errorTime
            };
            
            DBManager.executePreparedUpdate(ERROR_HIS_INSERT_SQL, param);
            
        } catch (Exception ex) {
        	LogManager.debug("### 백그라운드 히스토리 저장시 오류 ###");
            LogManager.error("ERROR", e.toString(), ex);
        }
		LogManager.debug("### 백그라운드 서비스,컴포넌트정보 히스토리 저장 완료 ###");
    }
    
    
	public static void main(String[] args) throws DBException {

		DataMap map = new DataMap();
		IbatisTxManager tx = null;
		try {
			beginTransaction(map);
			tx = getTx(map);
			LogManager.debug("map.isTransactionMode()==="+map.isTransactionMode());
			
			int resultCnt = 0;
			map.put("WORK_SPACE_ID", "WSGUN");
			map.put("WORK_SPACE_NAME", "WS_NAMEGUN5");
			map.put("THREAD_COUNT", 10);
			
		//	DataMap map2 = new DataMap();
		//	LogManager.debug("@@@@ 1 @@@ Transaction없는 count");
		//	ServiceReturnTestBiz.countTest(map2);
			
			DBContext dbCtx = new DBContext("service.workspace.WorkSpace", "DELETE_SQL", null, map);
			resultCnt = DBHandler.update(tx, dbCtx);
			LogManager.debug("@@@@ 2 @@@ Transaction count");
			countTest(map);
		//	LogManager.debug("@@@@ 2-1 @@@ Transaction없는 count");
		//	ServiceReturnTestBiz.countTest(map2);
			
			DBContext dbCtx2 = new DBContext("service.workspace.WorkSpace", "INSERT_SQL", null, map);
			resultCnt = DBHandler.update(tx, dbCtx2);
			LogManager.debug("@@@@ 3 @@@ Transaction count");
			countTest(map);
		//	LogManager.debug("@@@@ 3-1 @@@ Transaction없는 count");
		//	ServiceReturnTestBiz.countTest(map2);
			
			commitTransaction(map);
		} catch (Exception e) {
			rollbackTransaction(map);
		} finally {
			if(tx != null)
				tx.end();
		}
	}
	
	private static DataMap countTest(DataMap map) throws BizException {
		//LogManager.debug("ServiceReturnTestBiz countTest() 시작");
		DBContext dbCtx = new DBContext("service.workspace.WorkSpace", "COUNT_SQL", null, map);
		DataSet ds;
		if(map.isTransactionMode()){
			IbatisTxManager tx  = map.getTxManager();
			ds = DBHandler.executeQuery(tx, dbCtx);
		}else{
			ds = DBHandler.executeQuery(dbCtx);
		}
		//LogManager.debug("ds =========== " + ds);
		if(ds != null){
			ds.initRow();
			ds.next();
			LogManager.debug("count =========== " + ds.getString("data"));
		//	LogManager.debug("ServiceReturnTestBiz countTest() 종료");
		}else{
			LogManager.debug("ds =========== NULL!!!!!! >> " + ds);
		}
		return map;
	}
	
}
