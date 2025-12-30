package nebsoa.common.jdbc;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import nebsoa.common.Constants;
import nebsoa.common.exception.DBException;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.PropertyManager;
import nebsoa.common.util.StringUtil;

//import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

/**
 * com.ibatis.sqlmap.client.SqlMapClient 의 초기화를 담당.
 * 최초 생성시 ibs.properties.xml의 DEFAULT_DATASOURCE_FILENAME 의 파일명으로 SqlMapClient 생성
 * 
 * @author ohjae
 * 
 */
public class SqlMapClientConfig {

	private static String filePath = Constants.APP_HOME_DIR
			+ "/config/sql";

	private static HashMap sqlHashMap = new HashMap();;

	// SqlMapClient config파일 접두어
	private static String preFixConfigFileName = PropertyManager.getProperty("dbconfig", "SQLMAP_PREFIX_FILENAME","sqlmap");

	// SqlMapClient의 기본 config파일
	private static String defaultDBName = PropertyManager.getProperty("dbconfig", "DEFAULT_SQLMAP_FILENAME","default");

	private SqlMapClientConfig() {
	}


	public static SqlMapClient getSqlMapInstance(String dbName) {
		
		if(StringUtil.isNull(dbName) ) {
			dbName = defaultDBName;
		} else {
			//dbconfig.properties.xml 파일에 dbName으로 매핑되어 있는 sqlmap config 파일명
			dbName = PropertyManager.getProperty("dbconfig", dbName+"_SQLMAP_FILENAME");
			
		}
		
		SqlMapClient retSqlMapClient = (SqlMapClient) sqlHashMap.get(dbName);

		if(retSqlMapClient == null) {
			synchronized(sqlHashMap){
				retSqlMapClient = loadConfig(dbName);
				sqlHashMap.put(dbName, retSqlMapClient);
			}
		}
		
		return retSqlMapClient;
	}


	/**
	 * 해당 Config File명으로 SqlMapClient 객체를 생성합니다.
	 * @param configName
	 * @return
	 */
	private static SqlMapClient loadConfig(String dbName) {
		SqlMapClient sqlMapClient;
		try {			
			Reader reader = new FileReader(new File(filePath
					+ File.separator + preFixConfigFileName + "." + dbName+".xml"));
	
			sqlMapClient = SqlMapClientBuilder.buildSqlMapClient(reader);
			reader.close();
			LogManager.debug("####"+preFixConfigFileName + "." + dbName+".xml load success ####");
		} catch (Exception e) {
			LogManager.error("####SqlMapClientConfig ERROR! ERROR FILENANE="+filePath
					+ File.separator + preFixConfigFileName + "." + dbName+".xml "+e.getMessage()+" ####");
			throw new DBException("FRD99998",e.getMessage());
		}
		return sqlMapClient;
	}
	
	/**
	 * 전체 SqlMapClient Reload
	 *
	 */
	public static void reloadAll(){
		LogManager.debug(" ##### SqlMapClientConfig.reload 수행합니다. ####");
		HashMap tempMap = (HashMap)sqlHashMap.clone();
		Set keySet = tempMap.keySet();
		Iterator keyIterator = keySet.iterator();
		while(keyIterator.hasNext()){
			String key = (String)keyIterator.next();
			LogManager.debug(" ##### SqlMapClientConfig.reload 수행 key->"+key+"####");
			reload(key);
		}
	}
	
	/**
	 * 인자로 넘어온 DBNAME의 sqlMapClient reload
	 * @param dbName
	 */
	public static void reload(String dbName){
		sqlHashMap.remove(dbName);
		sqlHashMap.put(dbName, loadConfig(dbName));
	}
	
}
