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

import javax.transaction.UserTransaction;

import nebsoa.common.exception.DBException;
import nebsoa.common.exception.SysException;
import nebsoa.common.jndi.JNDIManager;
import nebsoa.common.log.LogManager;
import nebsoa.common.startup.StartupContext;
import nebsoa.common.util.PropertyManager;

/*******************************************************************
 * <pre>
 * 1.설명
 * 분산 트렌젝션을 지원하는 DB에서 여러 쿼리를 한번에 수행할 때
 * 하나의 트랜젝션으로 처리하기 위해 사용되는 클래스
 *
 *
 * 2.사용법
 *  <B>XATxManager xaTxManager = DBManager.getXATxManager();</B>
 *  try{
 *   <B>xaTxManager.begin();</B>
 * 	 int result = 0;
 * 	 for (int successCount=0 ; successCount < battingCount && succeeded; successCount++) {
 * 	 // use XATxManager with DBManager
 * 	 result = DBManager.executePreparedUpdate( sql, obj);
 * 	 }
 * 	 <B>xaTxManager.commit();</B>
 *  }catch(Exception e){
 *  	LogManager.error(e.getMessage());
 *  	<B>xaTxManager.rollback();</B>
 *  }finally{
 *  	<B>xaTxManager.end();</B>
 *  }
 *
 * <font color="red">
 * 3.주의사항
 * XATxManager의 finallize메소드는 개발자가 호출해서는 안됩니다
 * </font>
 *
 * @author $Author: cvs $
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 *
 * $Log: XATxManager.java,v $
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
 * Revision 1.1  2008/01/22 05:58:31  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.2  2007/12/11 06:41:28  이종원
 * TxService Interface추가
 *
 * Revision 1.1  2007/11/26 08:37:44  안경아
 * *** empty log message ***
 *
 * Revision 1.14  2007/03/07 02:53:45  김성균
 * 일부 로그 INFO 레벨로 변경
 *
 * Revision 1.13  2006/10/23 14:12:31  이종원
 * connection leak 수정
 *
 * Revision 1.12  2006/07/12 06:37:01  안경아
 * *** empty log message ***
 *
 * Revision 1.11  2006/07/04 11:47:55  이종원
 * *** empty log message ***
 *
 * Revision 1.10  2006/07/04 11:31:47  이종원
 * *** empty log message ***
 *
 * Revision 1.9  2006/06/19 04:10:16  안경아
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class XATxManager implements TxService {

	String wasName;
	Connection con;
	int  txMode;

    UserTransaction ut =null;

	boolean alreadyRollbacked=false;
	boolean alreadyCommitted=false;

    Exception ex;

	public XATxManager(){
		//wasName=StartupContext.getDefaultDBName();
        String defaultDB=PropertyManager.getProperty("db","DEFAULT_DB");
        boolean useDataSource=PropertyManager.getBooleanProperty(
                "db",defaultDB+".USE_DATASOURCE");
        if(useDataSource){
            wasName=PropertyManager.getProperty(
                    "db",defaultDB+".WAS_CONFIG_MAPPING");
        }else{
            throw new DBException("DataSource not supported(Set "
                    +defaultDB+".USE_DATASOURCE to true");
        }
        ex = new Exception("Transaction을 정상 종료하지 않았습니다");
	}

	public XATxManager(String wasName){
		this.wasName = wasName;
	}



	public void begin() throws DBException{
		try{
            ut = JNDIManager.getUserTransaction(wasName);
            ut.begin();
            txMode=TX_BEGIN;
			LogManager.info("########## [분산 트랜젝션]을 시작 합니다.########");
		}catch(Exception e){
            ut = null;
			LogManager.error(e.getMessage()
                    +"\n##########[분산 트랜젝션] 시작을 실패 하였습니다.########");
			throw new SysException(e.getMessage());
		}
	}

	/**
	 commit 합니다
	 */
	public void commit() {
		try{
            if(isUsable()) ut.commit();
			txMode=TX_COMMIT;
			alreadyCommitted=true;
			LogManager.info("##########[분산 트랜젝션] commit 합니다.########");
		}catch(Exception e){
			txMode = TX_COMMIT_FAIL;
			LogManager.error(e.getMessage()+"##########[분산 트랜젝션] commit 실패.########",e);
		}
		end();
	}

	/**
	 rollback 합니다
	 */
	public void rollback(){
		try{
			if(isUsable()) ut.rollback();
			txMode=TX_ROLLBACK;
			alreadyRollbacked = true;
			LogManager.info("##########[분산 트랜젝션] rollback 합니다.########");
		}catch(Exception e){
			txMode = TX_ROLLBACK_FAIL;
			LogManager.error(e.getMessage()+"##########트랜젝션을 rollback 실패.########",e);
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
        if(ut == null){
            LogManager.error("[분산트렌젝션]을 얻지 못하였습니다.");
            return false;
        }
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

		LogManager.info("트랜잭션 정상 진행중");
		return true;
	}

	/**
	 현재 커넥션이 사용 가능한지 체크
	 */
	public void end() {

		if(txMode==TX_END) return;
		txMode=TX_END;

	}

	public void finalize(){
		if(txMode != TX_END && txMode != 0){
			LogManager.error(ex.toString(),ex);
			LogManager.error("TX_MODE : "+txMode);
			end();
		}
	}

	///////////////////test XATxManager////////////////////////
	/**
	 * XATxManager를 테스트 한다.
	 *
	 * @author 이종원
	 *
	 */
	public static void main(String args[]){
		try{
    		XATxManager tx = new XATxManager();
    		tx.begin();
    		//DBManager.executeTxUpdate(tx,"INSERT INTO TEST VALUES (TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS'),'AAA')");
    		Object[] param = {"999","777"};
    		DBManager.executePreparedUpdate("INSERT INTO TEST VALUES (?,?)", param);
    		ArrayList paramList  = new ArrayList();
    		for(int i=10;i<15;i++){
    			paramList.add(new Object[]{i+"BATCH",i+"batch"});
    		}
    		DBManager.executeBatch("INSERT INTO TEST VALUES (?,?)" ,paramList);
    		tx.commit();
    		tx.end();
		}catch(Exception e){
			LogManager.debug(e.getMessage());
		}

        try{
            XATxManager tx = new XATxManager();
            tx.begin();
            //DBManager.executeTxUpdate(tx,"INSERT INTO TEST VALUES (TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS'),'AAA')");
            Object[] param = {"888","9898"};
            DBManager.executePreparedUpdate("INSERT INTO TEST VALUES (?,?)", param);
            ArrayList paramList  = new ArrayList();
            for(int i=10;i<15;i++){
                paramList.add(new Object[]{i+"BATCH",i+"batch"});
            }
            DBManager.executeBatch("INSERT INTO TEST VALUES (?,?)" ,paramList);
            tx.rollback();
            tx.end();
        }catch(Exception e){
            LogManager.debug(e.getMessage());
        }
		LogManager.debug(DBManager.executeQuery("select * from test"));
		System.gc();
		LogManager.debug("hello.....");
	}
}
