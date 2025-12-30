package test.ibatis;

import nebsoa.common.Context;
import nebsoa.common.collection.DataSet;
import nebsoa.common.jdbc.DBHandler;
import nebsoa.common.jdbc.context.DBContext;
import nebsoa.common.util.DataMap;

public class DBHandlerTest {

	
	public static void print(DataSet rs){
		
		System.out.print("#### START  ###");
		
//		System.out.print("#### toHtmlTable="+rs.toHtmlTable()+"###");
		
		if( rs.getRowCount() > -0 ) {
			while(rs.next()) {
				System.out.print("####"+rs.getString("USER_ID"));
				System.out.print("#"+rs.getString("USER_NAME"));
				System.out.print("#"+rs.getString("DESCR"));
				System.out.print("#"+rs.getInt("COUNT"));
				System.out.println("#"+rs.getString("LOC")+"####");
//				if(rs.getRow()==rs.getRowCount()) rs.deleteRow();
				
//				DataMap map = rs.toDataMap();
//				System.out.println("### map="+map+"####");
				
//				DataMap map = new DataMap();
//				rs.toDataMap(map);
//				System.out.println("### map="+map+"####");
			}
		}
		System.out.print("#### END  ###");
	}
	
	/**
	 * 일반적인 쿼리 리스트
	 */
	public static void executeQueryForMapListTest(DBContext dbctx)
	{
		DataSet rs = DBHandler.executeQuery(dbctx);
		print(rs);
	}
	
	/**
	 * 쿼리중 스킵과 최대 조회 결과 수
	 */
	public static void executeQueryForMapListTest(DBContext dbCtx,int skipResult, int maxResult) {
		DataSet rs = DBHandler.executeQuery(dbCtx,skipResult,maxResult);
		print(rs);
	}

	/**
	 * 단 한개의 조회 결과를 가질때
	 */
	public static void executeQueryForObjectTest(DBContext dbCtx) {
		Object obj = DBHandler.executeQueryToBean(dbCtx);
		System.out.println(obj);
	}

	/**
	 * 단 한개의 조회 결과를 가질때
	 * resultObject 가 xml에 선언한 resultClass 와 동일해야 정상 작동함.. 왜 있는건지...
	 */
	public static void executeQueryForObjectTest(DBContext dbCtx, Object resultObject) {
		Object obj = DBHandler.executeQueryToBean(dbCtx, resultObject);
		System.out.println(obj);
	}

	/**
	 * 지정된 keyProp를 map의 key로 해서 처리 결과가 리턴
	 * 	<typeAlias alias="IbatisSample" type="test.ibatis.IbatisSample" />
	 *	<resultMap id="samplevo" class="IbatisSample">
	 *		<result property="userId" column="USER_ID" />
	 *		<result property="userName" column="USER_NAME" />
	 *		<result property="descr" column="DESCR" />
	 *		<result property="count" column="COUNT" />
	 *		<result property="loc" column="LOC" />
	 *	</resultMap>
	 * <select id="searchMap" parameterClass="nebsoa.common.util.DataMap" resultMap="samplevo">
	 */
	public static void executeQueryForMapTest(DBContext dbCtx , String keyProp) {
		Object obj = DBHandler.executeQueryToMap(dbCtx , keyProp);
		System.out.println("##"+obj);
	}

	/**
	 * 지정된 keyProp를 map의 key로 하고 value는 valueProp 에 지정된 값이 셋팅되서 처리 결과가 리턴
	 * 	<typeAlias alias="IbatisSample" type="test.ibatis.IbatisSample" />
	 *	<resultMap id="samplevo" class="IbatisSample">
	 *		<result property="userId" column="USER_ID" />
	 *		<result property="userName" column="USER_NAME" />
	 *		<result property="descr" column="DESCR" />
	 *		<result property="count" column="COUNT" />
	 *		<result property="loc" column="LOC" />
	 *	</resultMap>
	 * <select id="searchMap" parameterClass="nebsoa.common.util.DataMap" resultMap="samplevo">
	 */
	public static void executeQueryForMapTest(DBContext dbCtx , String keyProp ,String valueProp) {
		Object obj = DBHandler.executeQueryToMap(dbCtx , keyProp, valueProp);
		System.out.println(obj);
	}

	/**
	 * INSERT를 수행하고 난 후 특정 selectKey 같은 특정 ibatis 셋팅을 지정했을 경우 해당 값이 리턴. 없으면 null 리턴
	 * @param dbCtx
	 */
	public static void executeQueryForInsertTest(DBContext dbCtx) {
		Object obj = DBHandler.insert(dbCtx );
		System.out.println(obj);
	}

	/**
	 * update 후 처리된 row수를 반환
	 * @param dbCtx
	 */
	public static void executeQueryForUpdateTest(DBContext dbCtx) {
		int count = DBHandler.update(dbCtx );
		System.out.println(count);
	}

	/**
	 * update 후 처리된 row수를 반환
	 * @param dbCtx
	 */
	public static void executeQueryForDeleteTest(DBContext dbCtx) {
		int count = DBHandler.delete(dbCtx );
		System.out.println(count);
	}
	
	/**
	 * MapDataSet 테스트입니다.
	 * @param dbctx
	 */
	public static void dataSetTest(DBContext dbctx) {
		DataSet rs = DBHandler.executeQuery(dbctx);
		
//		System.out.println(rs.relative(3));
		print(rs);
//		System.out.println(rs.previous());
//		System.out.println(rs.first());
//		System.out.println(rs.last());
//		System.out.println(rs.relative(-3));
//		System.out.println(rs.absolute(3));
//		rs.initRow();
//		System.out.println(rs.getRow());
//		System.out.println(rs.beforeFirst());
//		System.out.println(rs.getRow());
//		System.out.println(rs.getRowCount());
//		System.out.println(rs.size());
		print(rs);

	}
	
	public static void main(String[] args) {
		DataMap map = new DataMap();
//		map.put("userId", "tester");
//		map.put("userName", "테스터");
		
		Context ctx = new Context();
		map.setContext(ctx);

//		DBContext dbCtx = new DBContext("IbatisSample","searchList",map);

//		dataSetTest(dbCtx);
		
//		executeQueryForMapListTest(dbCtx);
		
//		executeQueryForMapListTest(dbCtx,1,3);
		
//		executeQueryForObjectTest(dbCtx);
		
//		executeQueryForObjectTest(dbCtx , new test.ibatis.IbatisSample());

		
		DBContext dbCtx2 = new DBContext("IbatisSample","searchMap",map);
		
		executeQueryForMapTest(dbCtx2 , "userId");
		
//		executeQueryForMapTest(dbCtx , "userId", "userName");


//		DataMap map = new DataMap();
//		map.put("userName", "테스터2");
//		map.put("descr", "테스터desc");
//		map.put("count", "0");
//		map.put("loc", "KO");
//		Context ctx = new Context();
//		map.setContext(ctx);
//
//		for(int i = 0 ; i < 5 ; i ++) {//이런식은 개개의 쿼리로 커밋됨.
//			DBContext dbCtx = new DBContext("IbatisSample","insertIbatisSample",map);
//
//			map.put("userId", "aaa"+i);
//			executeQueryForInsertTest(dbCtx);
//		}
		



		
//		DBContext dbCtx = new DBContext("IbatisSample","updateIbatisSample",map);
//		executeQueryForUpdateTest(dbCtx);

//		DBContext dbCtx = new DBContext("IbatisSample","deleteIbatisSample",map);
//		executeQueryForDeleteTest(dbCtx);
	}
}
