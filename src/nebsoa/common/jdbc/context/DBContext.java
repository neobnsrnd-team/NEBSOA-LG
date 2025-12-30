package nebsoa.common.jdbc.context;

import nebsoa.common.util.DataMap;
import nebsoa.common.util.PropertyManager;

/**
 * DB 쿼리를 위한 기본 정보를 셋팅합니다.
 * sqlGroupId : sqlMap XML의  namespace값을 지정합니다.
 * sqlId : sqlMap XML의 select,insert,update,delete 의 id 를 지정합니다.
 * dbName : 사용하고자 하는 DB명을 지정합니다. 지정하지 않으면 default DB를 사용합니다.
 * map : sql에 파라미터로 넣고자하는 데이타를 넣습니다. 넣지 않으면 null이 셋팅됩니다.
 * className : 호출하는 클래스명을 지정합니다. 넣지 않으면 null이 셋팅 됩니다.
 * @author ohjae
 *
 */
public class DBContext {

	private String sqlGroupId;
	private String sqlId;
	private String dbName;
	private DataMap map;
	private String className;

	public DBContext() {}

	/**
	 * Default DB로 셋팅하여 해당 쿼리 정보를 셋팅
	 * @param sqlGroupId
	 * @param sqlId
	 */
	public DBContext(String sqlGroupId, String sqlId) {
		this(sqlGroupId, sqlId, (String)null, (DataMap)null, (Class)null);
	}

	/**
	 * 쿼리 정보와 바인딩할 데이타 셋팅
	 * @param sqlGroupId
	 * @param sqlId
	 * @param map DataMap
	 */
	public DBContext(String sqlGroupId, String sqlId, DataMap map) {
		this(sqlGroupId, sqlId, (String)null, map, (Class)null);
	}

	/**
	 * 쿼리 정보와 사용할 DB 셋팅
	 * @param sqlGroupId
	 * @param sqlId
	 * @param dbName null이면 default DB 사용
	 */
	public DBContext(String sqlGroupId, String sqlId, String dbName) {
		this(sqlGroupId, sqlId, dbName, (DataMap)null, (Class)null);
	}

	/**
	 * 쿼리 정보와 사용할 DB, 바인딩할 데이타 셋팅
	 * @param sqlGroupId
	 * @param sqlId
	 * @param dbName null이면 default DB 사용
	 * @param map DataMap
	 */
	public DBContext(String sqlGroupId, String sqlId, String dbName, DataMap map) {
		this(sqlGroupId, sqlId, dbName, map, (Class)null);
	}

//================================================================================================
		/**
		 * Default DB로 셋팅하여 해당 쿼리 정보를 셋팅
		 * @param sqlGroupId
		 * @param sqlId
		 * @param clazz
		 */
		public DBContext(String sqlGroupId, String sqlId, Class clazz) {
			this(sqlGroupId, sqlId, (String)null, (DataMap)null, clazz);
		}


		/**
		 * 쿼리 정보와 바인딩할 데이타 셋팅
		 * @param sqlGroupId
		 * @param sqlId
		 * @param map DataMap
		 * @param clazz
		 */
		public DBContext(String sqlGroupId, String sqlId, DataMap map, Class clazz) {
			this(sqlGroupId, sqlId, (String)null, map, clazz);
		}

		/**
		 * 쿼리 정보와 사용할 DB 셋팅
		 * @param sqlGroupId
		 * @param sqlId
		 * @param dbName null이면 default DB 사용
		 * @param clazz
		 */
		public DBContext(String sqlGroupId, String sqlId, String dbName, Class clazz) {
			this(sqlGroupId, sqlId, dbName, (DataMap)null, clazz);
		}

//================================================================================================

	/**
	 * 쿼리 정보와 사용할 DB, 바인딩할 데이타 셋팅
	 * @param sqlGroupId
	 * @param sqlId
	 * @param dbName null이면 default DB 사용
	 * @param map DataMap
	 */
	public DBContext(String sqlGroupId, String sqlId, String dbName, DataMap map, Class clazz) {
		this.sqlGroupId = sqlGroupId;
		this.sqlId = sqlId;
		if(dbName==null){
			dbName = PropertyManager.getProperty(
					"dbconfig", "DEFAULT_SQLMAP_FILENAME","default");
		}
		this.dbName = dbName;
		this.map = map;
		if(clazz != null){
			this.className = clazz.getName();
		}else{
			this.className = (String)null;
		}
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public void setMap(DataMap map) {
		this.map = map;
	}

	public void setSqlGroupId(String sqlGroupId) {
		this.sqlGroupId = sqlGroupId;
	}

	public void setSqlId(String sqlId) {
		this.sqlId = sqlId;
	}

	public void setClassName(String className){
		this.className = className;
	}

	public String getDbName() {
		return dbName;
	}

	public DataMap getMap() {
		return map;
	}

	public String getSqlGroupId() {
		return sqlGroupId;
	}

	public String getSqlId() {
		return sqlId;
	}

	public String getSqlUniqueId() {
		if(sqlGroupId != null){
			return sqlGroupId+"."+sqlId;
		} else
		return sqlId;
	}
	public String getClassName(){
		return className;
	}
}
