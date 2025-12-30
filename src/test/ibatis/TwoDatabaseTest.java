package test.ibatis;

import nebsoa.common.Context;
import nebsoa.common.collection.DataSet;
import nebsoa.common.jdbc.DBHandler;
import nebsoa.common.jdbc.context.DBContext;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.DataMap;

public class TwoDatabaseTest {

	public static void print(DataSet rs) {
		if (rs.getRowCount() > -0) {
			while (rs.next()) {
				System.out.print("####" + rs.getString("CODE_GROUP_ID"));
				System.out.print("#" + rs.getString("CODE"));
				System.out.print("#" + rs.getString("CODE_NAME"));
				System.out.print("#" + rs.getString("CODE_DESC"));
				System.out.print("#" + rs.getString("SORT_ORDER"));
				System.out.println("#" + rs.getString("USE_YN") + "####");
			}
		}
	}

	/**
	 * MapDataSet 테스트입니다.
	 * 
	 * @param dbctx
	 */
	public static void dataSetTest(DBContext dbctx) {
		DataSet rs = DBHandler.executeQuery(dbctx);

		print(rs);
	}

	public static void main(String[] args) {
		LogManager.debug("$$$$$$ LOG TEST");
		DataMap map = new DataMap();
		map.put("codeGroupId", "CM20020");
		// map.put("code", "테스터");

		Context ctx = new Context();
		map.setContext(ctx);

		DBContext dbCtx = new DBContext("FwkCodeTable", "selectCodeByCodeGroupId", map);
		DBContext dbCtx2 = new DBContext("FwkCodeTable", "selectCodeByCodeGroupId", "ibk",map);
		
		dataSetTest(dbCtx);
		System.out.println("####### default db end ######");
		dataSetTest(dbCtx2);

		LogManager.debug("$$$$$$ LOG TEST");	
		System.out.println("####### SECOND ######");
		System.out.println("#####################");
		
		dataSetTest(dbCtx);
		System.out.println("####### default db end ######");
		dataSetTest(dbCtx2);

	}
}
