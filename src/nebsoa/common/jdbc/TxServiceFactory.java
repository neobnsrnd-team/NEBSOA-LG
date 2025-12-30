/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
package nebsoa.common.jdbc;

import nebsoa.common.exception.SysException;
import nebsoa.common.util.PropertyManager;
/*******************************************************************
 * <pre>
 * 1.설명 
 * 여러 쿼리를 한번에 수행할 때 하나의 트랜젝션으로 처리하기 위해 사용되는 TxService
 * 객처를 얻어내는 메소드를 제공하는 클래스
 * 
 * 2.사용법
 *  <B>TxService txSvc = TxServiceFactory.getTxService(dbName, txType);</B>
 *                     // TxServiceFactory.getDefaultTxService(dbName);
 *                     // TxServiceFactory.getJDBCTxService(dbName);
 *                     // TxServiceFactory.getXATxService(dbName);
 *                     // TxServiceFactory.getIbatisTxService(dbName);
 *                     
 *  
 *  <B>txSvc.begin();</B>
 *  try{
 *		insert...
 *      update...
 * 	 }
 * 	 <B>txSvc.commit();</B>
 *  }catch(Exception e){
 *  	LogManager.error(e.getMessage());
 *  	<B>txSvc.rollback();</B> 
 *  }finally{	
 *  	<B>txSvc.end();</B>
 *  }
 *   
 * <font color="red">
 * 3.주의사항 
 * TxManager의 finallize메소드는 개발자가 호출해서는 안됩니다
 * </font>
 *
 * @author 이종원
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 
 * $Log: TxServiceFactory.java,v $
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
 * Revision 1.1  2008/08/04 08:54:54  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:31  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/12/11 06:41:28  이종원
 * TxService Interface추가
 *
 * </pre>
 ******************************************************************/
public class TxServiceFactory {
	
	static int TX_BMT_DEFAULT=0;
	static int TX_BMT_IBATIS=1;
	static int TX_CMT_DEFAULT=2;
	/**
	 * 프레임웍에서 설정한  TX SERVICE IMPL 객체가 리턴 된다.
	 * 설정 방법은 db.properties.xml에 TX_SERVICE_TYPE 값에 따라 달라진다.
	 * 0 : 고전적인 jdbc tx 관리 객체 리턴
	 * 1 : ibatis tx manager리턴
	 * 2 ; 분산 DB 처리 가능한 XA TxManager리턴.
	 * @param dbName
	 * @return
	 */
	public static TxService getDefaultTxService(String dbName){
		return getTxService(dbName,
				PropertyManager.getIntProperty("db","TX_SERVICE_TYPE",0));
	}
	
	/**
	 * RETURN DEFAULT TX SERVICE IMPL OBJECT (TXManager)
	 * @param dbName
	 * @return
	 */
	public static TxService getJDBCTxService(String dbName){
		return getTxService(dbName,0);
	}
	
	/**
	 * if 0 then equal to getDefaultTxService
	 * if 1 then equal to getIbatisTxService
	 * if 2 then equal to getXATxService
	 * @param dbName
	 * @param type
	 * @return
	 */
	public static TxService getTxService(String dbName, int type){
		if(0== type) return new TxManager(dbName);
		if(1== type) return new IbatisTxManager(dbName);
		if(2== type) return new XATxManager(dbName);
		else throw new SysException("Invalid  'TX type' parameter");
	}
	/**
	 * return was managed tx manager impl (XATxMamanger)
	 * @param dbName
	 * @return
	 */
	public static TxService getXATxService(String dbName){
		return getTxService(dbName,TX_CMT_DEFAULT);
	}
	/**
	 * return bean managed tx manager ibatis impl (IBSTxMamanger)
	 * @param dbName
	 * @return
	 */
	public static TxService getIbatisTxService(String dbName){
		return getTxService(dbName,1);
	}

}
