/*
 * Spider Framework
 *
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 *
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
package nebsoa.common.jdbc;

import java.sql.SQLException;

import nebsoa.common.exception.DBException;
import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.PropertyManager;

import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * DataBase 트랜잭션 작업시 사용합니다.
 *
 * 사용법 :
 *
 * DBContext ctx1 = new DBContext(sqlGroupId, sqlId, map)
 * DBContext ctx2 = new DBContext(sqlGroupId2, sqlId2, map2)
 * IbatisTxManager ibstx = new IbatisTxManager()
 * try {
 * 		ibstx.begin();
 *  	DBHandler.insert(ibstx,ctx1);
 *  	DBHandler.insert(ibstx,ctx2);
 *  	ibstx.commit();
 * } catch() {
 *  	에러 처리
 * } finally {
 * 		ibstx.end();
 * }
 *
 * @author ohjae
 *
 */
public class IbatisTxManager implements TxService {

	private SqlMapClient sqlMap;

	private String dbName;

    int txMode=TX_NONE; //Not started yet.

    boolean alreadyRollbacked = false;

    boolean alreadyCommitted = false;
    //trace를 위한 Exception 추가
    Exception ex;

    /**
	 * default DB를 사용합니다. 가급적 사용하지 않도록 합니다.
	 */
	public IbatisTxManager() {
		this(null);
	}

	/**
	 * 특정 DB를 사용합니다.
	 * @param dbName 사용할 DB 이름
	 */
	public IbatisTxManager(String dbName) {
		if(dbName==null){
			dbName = PropertyManager.getProperty(
					"dbconfig", "DEFAULT_SQLMAP_FILENAME","default");
		}
		LogManager.debug("TransactionManager를 생성합니다.Xml Config Name::"+dbName);
		this.dbName = dbName;
		this.sqlMap = SqlMapClientConfig.getSqlMapInstance(dbName);
		ex = new Exception("Transaction을 정상 종료하지 않았습니다");
	}

	/**
	 * 현재 사용하고 있는 SqlMapClient 반환
	 * @return
	 */
	public SqlMapClient getSqlMapClient() {
		return sqlMap;
	}


	/**
	 * 트랜잭션의 시작
	 *
	 * @throws DBException
	 */
	public void begin() throws DBException {

		try {
			sqlMap.startTransaction();
			txMode=TX_BEGIN;
			LogManager.info("##########트랜젝션을 시작 합니다.########");
		} catch (SQLException e) {
			LogManager.error("##########트랜젝션 시작을 실패 하였습니다.########\n"
					+ e.toString(), e);
			end();
			throw new DBException(e);
		} catch(Exception e){
			LogManager.error("##########트랜젝션 시작을 실패 하였습니다.########\n"+e.toString(),e);
			end();
			throw new SysException(e);
		}
	}

	/**
	 * 트랜잭션 작업이 에러 없이 진행되었다면 커밋
	 *
	 * @throws DBException
	 */
	public void commit() throws DBException {
		LogManager.info("##########트랜젝션 commit 전 validation check 합니다.########");
		isValid();
		try {
			sqlMap.commitTransaction();
			txMode=TX_COMMIT;
			alreadyCommitted=true;
			LogManager.info("##########트랜젝션을 commit 합니다.########");
		}catch(SQLException e){
			txMode=TX_COMMIT_FAIL;
			LogManager.info("##########트랜젝션을 commit 실패.########");
			LogManager.error(e.getMessage(),e);
            throw new DBException(e);
		} finally{
			end();
		}
	}

	/**
	 * finally 절에서 트랜잭션 종료에 따른 자원 반환 작업 commit이 안되었다면 rollback함.
	 *
	 * @throws DBException
	 */
	public void end() {
		if(sqlMap==null) return;
		try {
			sqlMap.endTransaction();

			if(txMode==TX_NONE){
				LogManager.error("##########트랜잭션이 시작도 되지 않고  종료########");
			}else{
				if(alreadyCommitted && !alreadyRollbacked){
					LogManager.info("##########트랜잭션이 commit 후 안정적으로 종료되었습니다.########");
				}else if(!alreadyCommitted && alreadyRollbacked){
					LogManager.info("##########트랜잭션이 rollback 후 안정적으로 종료되었습니다.########");
				}else if(!alreadyCommitted && !alreadyRollbacked){
					LogManager.error("##########트랜잭션이 비정상적으로 종료 되어 강제 rollback 종료되었습니다.########"
							, new Exception("IBATIS TX 비정상 종료-강제 rollback 처리"));
				}
			}
			txMode=TX_END;

		} catch (SQLException e) {
			txMode=TX_END;
            LogManager.error("########## 트랜잭션 종료 실패 ########");
            LogManager.error(e.toString(),e);
		} finally{
			sqlMap=null;
		}
	}

	/**
	 * 트랜잭션 처리중 에러 발생시 rollback
	 */
	public void rollback(){
		LogManager.info("##########트랜젝션 rollback 전 validation check 합니다.########");
		isValid();

		LogManager.debug("##########트랜잭션을 ROLLBACK합니다.########");
		txMode=TX_ROLLBACK;
		alreadyRollbacked=true;
		end();
	}

	/**
	 * 트랜잭션의 정합성 체크함수이지만 IBATIS에서는 지원되지 않는다.
	 */
	public boolean isValid(){
		if(alreadyCommitted){
			LogManager.error("트렌젝션이 이미 커밋 되었습니다."
					+dbName+",sqlClient:"+sqlMap);
			return false;
		}
		if(alreadyRollbacked) {
			LogManager.error("트렌젝션이 이미 롤백 되었습니다."
					+dbName+",sqlClient:"+sqlMap);
			return false;
		}

		if(txMode == TX_NONE) {
			LogManager.error("트렌젝션이 아직 시작되지 않았습니다."
					+dbName+",sqlClient:"+sqlMap);
			return false;
		}
		if(sqlMap==null ) {
			LogManager.error("sqlMap 커넥션을 가지고 있지 않습니다.");
			return false;
		}
		LogManager.debug("트랜잭션 정상 진행중");
		return true;
	}

	public String getDBName() {
		return dbName;
	}


	public SqlMapClient getSqlMap() {
		return sqlMap;
	}

	public void finalize(){
		if(txMode != TX_END && txMode != 0){
			LogManager.error(ex.toString(),ex);
			LogManager.error("TX_MODE : "+txMode);
			end();
		}
	}

}
