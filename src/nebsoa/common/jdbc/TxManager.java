/*
 * Spider Framework
 *
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 *
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import nebsoa.common.Context;
import nebsoa.common.exception.DBException;
import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;

/*******************************************************************
 * <pre>
 * 1.설명
 * 여러 쿼리를 한번에 수행할 때 하나의 트랜젝션으로 처리하기 위해 사용되는 클래스
 *
 * 2.사용법
 *  <B>TxManager txManager = new TxManager(dataMap.getContext());</B>
 *  <B>txManager.begin();</B>
 *  try{
 * 	 int result = 0;
 * 	 for (int successCount=0 ; successCount < battingCount && succeeded; successCount++) {
 * 	 // use TxManager with DBManager
 * 	 result = DBHandler.executePreparedTxUpdate(dataMap.getContext(),<B>txManager</B>, sql, obj);
 * 	 }
 * 	 <B>txManager.commit();</B>
 *  }catch(Exception e){
 *  	LogManager.error(e.getMessage());
 *  	<B>txManager.rollback();</B>
 *  }finally{
 *  	<B>txManager.end();</B>
 *  }
 *
 * <font color="red">
 * 3.주의사항
 * TxManager의 finallize메소드는 개발자가 호출해서는 안됩니다
 * </font>
 *
 * @author $Author: cvs $
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 *
 * $Log: TxManager.java,v $
 * Revision 1.1  2018/01/15 03:39:49  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:35  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:23  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:27  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.3  2008/10/15 04:51:10  jwlee
 * Exception Trace기능 , commit 이나 rollback 상태 관리하는 isValid로직 구현
 *
 * Revision 1.2  2008/10/15 04:05:21  jwlee
 * 	private final static int TX_BEGIN=1;
 * 	private final static int TX_COMMIT=2;
 * 	private final static int TX_ROLLBACK=3;
 * 	private final static int TX_END=4;
 * 	private final static int TX_COMMIT_FAIL=5;
 * 	private final static int TX_ROLLBACK_FAIL=6;
 * 변수를 상위 클래스(TxService)로 이동
 *
 * Revision 1.1  2008/08/04 08:54:54  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.2  2008/07/07 05:55:23  김은정
 * end()에 && txMode != 0추가구현
 *
 * Revision 1.1  2008/01/22 05:58:29  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.2  2007/12/11 06:41:29  이종원
 * TxService Interface추가
 *
 * Revision 1.1  2007/11/26 08:37:44  안경아
 * *** empty log message ***
 *
 * Revision 1.12  2007/03/15 05:03:48  김성균
 * end() 가 이미 호출되었을 경우 수행하지 않도록 수정
 *
 * Revision 1.11  2007/03/15 01:40:27  김성균
 * end()호출시 commit or rollback되지 않았을 경우 처리수정
 *
 * Revision 1.10  2007/03/14 01:04:52  김성균
 * end()호출시 commit or rollback되지 않았을 경우 처리
 *
 * Revision 1.9  2007/03/07 02:53:45  김성균
 * 일부 로그 INFO 레벨로 변경
 *
 * Revision 1.8  2006/10/23 14:12:31  이종원
 * connection leak 수정
 *
 * Revision 1.7  2006/08/31 15:48:56  김성균
 * 주석수정
 *
 * Revision 1.6  2006/06/21 12:25:49  이종원
 * *** empty log message ***
 *
 * Revision 1.5  2006/06/21 09:43:03  이종원
 * DBHandler추가
 *
 * Revision 1.4  2006/06/19 04:10:16  안경아
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class TxManager implements TxService {
	String requestUri;

    Context ctx;

    String dbName;

    Connection con;

    int txMode;

    boolean alreadyRollbacked = false;

    boolean alreadyCommitted = false;
    /**
     * default db에 접근하는 TxManager
     */
	public TxManager(){

	}
    /**
     * dbName에 해당하는  db에 접근하는 TxManager
     * @param dbName
     */
	public TxManager(String dbName){
		this(null,dbName);
	}
     /**
     * dbName에 해당하는  db에 접근하는 TxManager
     * @param ctx
     * @param dbName 접근하는 dbName
     */
    public TxManager(Context ctx,String dbName){
        this.dbName = dbName;
        this.ctx = ctx;
        if(ctx != null){
        	requestUri=ctx.getUri();
        }
    }
    /**
     * default db에 접근하는 TxManager
     * @param ctx
     */
    public TxManager(Context ctx){
        this(ctx,null);
    }


    /**
     * 현재 txManager가 사용하고 있는 connection을 return합니다.
     * connection을 공유하기 위한 용도록 사용합니다.
     * 2003. 6. 21.  이종원 작성
     * @return
     * @throws SysException
     */
	public java.sql.Connection getConnection() throws SysException{
		if(isUsable()) return con;
		else throw new SysException("TxManager가 유효하지 않습니다.");
	}
    static int trxId=0;

	/**
     * transaction을 시작 합니다.
     * 2003. 6. 21.  이종원 작성
     * @throws DBException
	 */
	public void begin() throws DBException{
		try{
        /**
            if(ctx != null && ctx.getProfiler() != null){
                ctx.getProfiler().startEvent("transcation_"+ ++trxId+"번째");
            }
        ***/
			con = DBManager.getConnection(ctx,dbName);
			con.setAutoCommit(false);
			txMode=TX_BEGIN;
			LogManager.info("##########트랜젝션을 시작 합니다.########");
		}catch(SQLException e){
			LogManager.error("##########트랜젝션 시작을 실패 하였습니다.########\n"+e.toString(),e);
             end() ;

            throw new DBException(e);
        }catch(Exception e){
            LogManager.error("##########트랜젝션 시작을 실패 하였습니다.########\n"+e.toString(),e);
            end();

            throw new SysException(e);
        }
	}

	/**
	 commit 합니다
	 */
	public void commit() {
		try{
			if(isUsable()) con.commit();
			txMode=TX_COMMIT;
			alreadyCommitted=true;
			LogManager.info("##########트랜젝션을 commit 합니다.########");
		}catch(SQLException e){
			txMode = TX_COMMIT_FAIL;
			LogManager.info("##########트랜젝션을 commit 실패.########");
			LogManager.error(e.getMessage());
		}
		end();
	}

	/**
	 rollback 합니다
	 */
	public void rollback(){
		try{
			if(isUsable()) con.rollback();
			txMode=TX_ROLLBACK;
			alreadyRollbacked = true;
			LogManager.info("##########트랜젝션을 rollback 합니다.########");
		}catch(SQLException e){
			txMode = TX_ROLLBACK_FAIL;
			LogManager.info("##########트랜젝션을 rollback 실패.########");
			LogManager.error(e.getMessage());
		}
		end();
	}


	public boolean isValid(){
		return isUsable();
	}
	/**
	 현재 커넥션이 사용 가능한지 체크
	 */
	private  boolean isUsable(){
		if(alreadyCommitted){
			LogManager.error("트렌젝션이 이미 커밋 되었습니다.");
			return false;
		}
		if(alreadyRollbacked) {
			LogManager.error("트렌젝션이 이미 롤백 되었습니다.");
			return false;
		}

		if(txMode != TX_BEGIN) {
			LogManager.error("트렌젝션이 정상 진행 중이 아닙니다.");
			return false;
		}
		if(con==null ) {
			LogManager.error("커넥션을 가지고 있지 않습니다.");
			return false;
		}
		LogManager.info("트랜잭션 정상 진행중");
		return true;
	}

	/**
     * 현재 커넥션이 사용 가능한지 체크
     */
    public void end() {
	    if (txMode == TX_END) return;

		try {
	        if (txMode != TX_COMMIT && txMode != TX_ROLLBACK && txMode != 0) {
                String msg="commit or rollback 이 호출되지 않아서 강제 rollback 합니다.";
				LogManager.error("MONITOR", "===== 이것은 에러는 아닙니다 ====\n"+msg, new Exception(msg));
	            if (con != null) {
                    con.rollback();
                    txMode = TX_ROLLBACK;
                    alreadyRollbacked = true;
                    LogManager.info("##########트랜젝션을 rollback 합니다.########");
                }
	        }
        } catch (SQLException e) {
            txMode = TX_ROLLBACK_FAIL;
            LogManager.info("##########트랜젝션을 rollback 실패.########");
            LogManager.error(e.getMessage());
        }

        try {
            if (con != null) {
                con.setAutoCommit(true);
            }
        } catch (SQLException e) {
            LogManager.error("con.setAutoCommit(true); 세팅 실패" + e, e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e1) {
                    LogManager.error("트랜잭션 종료중 커넥션 닫기 실패");
                    LogManager.error(e1.getMessage());
                }
                con = null;
            }
        }

		txMode = TX_END;

 		LogManager.info("##########트랜잭션 종료########");
	}

	public void finalize(){
		if(txMode != TX_END && txMode != 0){
			LogManager.error(requestUri+"에서 사용한 트렌젝션을 정상 종료 하지 않았습니다");
			LogManager.error("TX_MODE : "+txMode);
			end();
		}
	}

	///////////////////test txManager////////////////////////
	/**
	 * txManager를 테스트 한다.
	 *
	 * @author 이종원
	 *
	 */
	public static void main(String args[]){
		try{
		TxManager tx = new TxManager();
		tx.begin();
		//DBManager.executeTxUpdate(tx,"INSERT INTO TEST VALUES (TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS'),'AAA')");
		Object[] param = {"999","777"};
		DBManager.executePreparedTxUpdate(tx,"INSERT INTO TEST VALUES (?,?)", param);
		ArrayList paramList  = new ArrayList();
		for(int i=10;i<15;i++){
			paramList.add(new Object[]{i+"BATCH",i+"batch"});
		}
		DBManager.executeTxBatch(tx,"INSERT INTO TEST VALUES (?,?)" ,paramList);
		tx.commit();
		tx.end();
		}catch(Exception e){
			LogManager.debug(e.getMessage());
		}
		LogManager.debug(DBManager.executeQuery("select * from test"));
		System.gc();
		LogManager.debug("hello.....");
	}
}
