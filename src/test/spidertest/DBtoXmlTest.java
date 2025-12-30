package test.spidertest;

import java.io.File;
import java.io.IOException;

import nebsoa.common.collection.DataSet;
import nebsoa.common.file.FileManager;
import nebsoa.common.jdbc.DBManager;
import nebsoa.common.jdbc.DBResultSet;
import nebsoa.common.util.DataMap;
import nebsoa.common.util.DbToXml;

public class DBtoXmlTest {

	static String toXml="";
	static String fromXml="";
	static String readFile = "";

	public static void readFile(){
		FileManager fm = new FileManager();

		try {
			readFile = fm.readStringFromFile("D:\\ServerSide\\java\\nebsoa\\nebsoa\\src\\test\\nebsoatest","datamapxml.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(readFile);
	}

	
	public static DataSet selectList(){

		String QUERY_SQL = " SELECT " +
		"BATCH_APP_ID , INSTANCE_ID " +
		"FROM FWK_BATCH_HIS";
		DataSet ds = DBManager.executeQuery(QUERY_SQL);
		System.out.println(ds);

		transXml(ds);		
		
		return ds;		
	}
	

	public static DataMap dataMapTest(){
		
		DataMap dm = new DataMap();
		
		dm.put("1","a1");
		dm.put("2","a2");
		dm.put("3","a3");
		dm.put("4","a4");
		dm.put("5","a5");
		dm.put("6","a6");
		dm.put("7","a7");
		dm.put("8","a8");
		dm.put("9","a9");
		dm.put("10","a10");

		transXml(dm);		

		return dm;
	}
	
	public static void transXml(Object obj) {

		DbToXml dx = new DbToXml();
		String xml = dx.toXmlString(obj);
		System.out.println("#########"+xml);

		
	}
	
	
	public static void testToXmlDataMap(){
		
		DbToXml dx = new DbToXml();
		
		DataMap dm = new DataMap();
		dm.put("1","1");
		dm.put("2","2");
		dm.put("3","3");
		dm.put("4","4");
		dm.put("5","5");
		dm.put("test","test");
		
		toXml = dx.toXmlString(dm);
		System.out.println("xml"+toXml);
	}
	
	public static void testFromXmlDataMap(){
		DbToXml dx = new DbToXml();
		DataMap dm = new DataMap();
		try {
//			dm = (DataMap)dx.fromXmlString(toXml);
			dm = (DataMap)dx.fromXmlString(readFile);
			System.out.println("datamap.toString()"+dm.toString());
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] arg) {
	
		System.setProperty("SPIDER_LOG_HOME","D:\\ServerSide\\java\\nebsoa\\nebsoa\\logs");
//		readFile();
		
//		testToXmlDataMap();
//		testFromXmlDataMap();	
		selectList();
		dataMapTest();
	}
}