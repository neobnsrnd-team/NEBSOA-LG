package nebsoa.common.util;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import nebsoa.common.log.LogManager;
import Zql.ZConstant;
import Zql.ZDelete;
import Zql.ZExp;
import Zql.ZExpression;
import Zql.ZFromItem;
import Zql.ZInsert;
import Zql.ZQuery;
import Zql.ZSelectItem;
import Zql.ZStatement;
import Zql.ZUpdate;
import Zql.ZqlParser;

public class ZqlUtil {

	private static Object dummy = new Object();

	private static ZqlUtil instance;

	private static ArrayList whereList = null;

	private ZqlUtil() {
	}

	public static ZqlUtil getInstance() {
		if (instance == null) {
			synchronized (dummy) {
				instance = new ZqlUtil();
			}
		}
		return instance;
	}

	/**
	 * SELECT 쿼리의 SELECT의 ITEM, FROM의 TABLE, WHERE의 prepared ITEM 리턴 사용법 : map =
	 * ZqlUtil.getSelect(query); map.get("select");, map.get("from");,
	 * map.get("where"); map.get("where")는 바인드변수 처리된 것만 가져옴.
	 * 
	 * @param query
	 * @return
	 * @throws Exception
	 */
	public static HashMap getSelect(String query) throws Exception {
		HashMap map = new HashMap();

		ZqlParser parser = new ZqlParser();
		parser.initParser(new ByteArrayInputStream(query.getBytes()));
		ZStatement st = parser.readStatement();
		ZQuery q = (ZQuery) st;

		ArrayList selectList = new ArrayList();
		ArrayList fromList = new ArrayList();
		whereList = new ArrayList();

		// select
		Vector sv = q.getSelect();
		for (int i = 0; i < sv.size(); i++) {
			ZSelectItem it = (ZSelectItem) sv.elementAt(i);
			selectList.add(it.getColumn());
		}

		// from
		Vector fv = q.getFrom();
		for (int i = 0; i < fv.size(); i++) {
			ZFromItem it = (ZFromItem) fv.elementAt(i);
			fromList.add(it.getTable());
		}

		// where
		ZExpression w = (ZExpression) q.getWhere();
		if (w != null) {
			Hashtable meta = new Hashtable();
			Vector from = q.getFrom();
			for (int i = 0; i < from.size(); i++) {
				ZFromItem fi = (ZFromItem) from.elementAt(i);
				String alias = fi.getAlias();
				if (alias == null)
					alias = fi.getTable();
				meta.put(alias.toUpperCase(), fi.getTable());
			}
			handleWhere(w, meta);
		}

		map.put("select", selectList);
		map.put("from", fromList);
		map.put("where", whereList);

		return map;
	}

	/**
	 * INSERT 쿼리의 INSERT의 COLUMN, TABLE, VALUES 리턴 사용법 : map =
	 * ZqlUtil.getInsert(query); map.get("columns"); : INSERT 쿼리의 모든 column을 가져옴
	 * map.get("table"); : INSERT 쿼리의 table을 가져옴 map.get("values"); : INSERT 쿼리의
	 * 모든 values를 가져옴 map.get("preparedColumns") : INSERT 쿼리의 prepared 해당
	 * column만 가져옴
	 * 
	 * @param query
	 * @return
	 * @throws Exception
	 */
	public static HashMap getInsert(String query) throws Exception {
		HashMap map = new HashMap();

		ZqlParser parser = new ZqlParser();
		parser.initParser(new ByteArrayInputStream(query.getBytes()));
		ZStatement st = parser.readStatement();
		ZInsert q = (ZInsert) st;

		ArrayList valuesList = new ArrayList();
		ArrayList columnList = new ArrayList();
		ArrayList preparedColumnList = new ArrayList();

		// table
		String table = q.getTable();

		// all columns
		Vector cv = q.getColumns();
		for (int i = 0; i < cv.size(); i++) {
			columnList.add(cv.elementAt(i));
		}

		// all values
		Vector vv = q.getValues();
		for (int i = 0; i < vv.size(); i++) {
			valuesList.add(vv.elementAt(i));
		}

		// prepared columns
		for (int i = 0; i < vv.size(); i++) {
			ZExp v = (ZExp) vv.elementAt(i);
			if (isPreparedColumn(v)) {
				preparedColumnList.add(cv.elementAt(i));
			}
		}

		map.put("table", table);
		map.put("columns", columnList);
		map.put("values", valuesList);
		map.put("preparedColumns", preparedColumnList);

		return map;
	}

	/**
	 * UPDATE 쿼리의 UPDATE TABLE, SET COLUMN, WHERE 리턴 사용법 : map =
	 * ZqlUtil.getUpdate(query); map.get("table"); : UPDATE 쿼리의 table을 가져옴
	 * map.get("preparedSet"); : UPDATE 쿼리의 set절의 prepared에 해당하는 column만 가져옴
	 * map.get("set"); : UPDATE 쿼리의 set절의 모든 column만 가져옴 map.get("where"); :
	 * UPDATE 쿼리 where절의 prepared에 해당하는 column만 가져옴
	 * 
	 * @param query
	 * @return
	 * @throws Exception
	 */
	public static HashMap getUpdate(String query) throws Exception {
		HashMap map = new HashMap();

		ZqlParser parser = new ZqlParser();
		parser.initParser(new ByteArrayInputStream(query.getBytes()));
		ZStatement st = parser.readStatement();
		ZUpdate q = (ZUpdate) st;

		ArrayList setList = new ArrayList();
		ArrayList preparedSetList = new ArrayList();
		whereList = new ArrayList();

		// table
		String table = q.getTable();

		// set & prepared set
		Hashtable set = q.getSet();
		Vector sv = q.getColumnUpdate();
		for (int i = 0; i < sv.size(); i++) {
			String col = (String) sv.get(i);
			ZExp e = (ZExp) set.get(col);
			// prepared set
			if (isPreparedColumn(e)) {
				preparedSetList.add(col);
			}
			// set
			setList.add(col);
		}

		ZExpression w = (ZExpression) q.getWhere();
		if (w != null) {
			Hashtable meta = new Hashtable(1);
			meta.put(table, table);
			handleWhere(w, meta);
		}

		map.put("table", table);
		map.put("set", setList);
		map.put("preparedSet", preparedSetList);
		map.put("where", whereList);

		return map;
	}

	/**
	 * DELETE 쿼리의 DELETE TABLE, WHERE 리턴 사용법 : map = ZqlUtil.getDelete(query);
	 * map.get("table"); : DELETE 쿼리의 table을 가져옴 map.get("where"); : DELETE 쿼리
	 * where절의 prepared에 해당하는 column만 가져옴
	 * 
	 * @param query
	 * @return
	 * @throws Exception
	 */
	public static HashMap getDelete(String query) throws Exception {
		HashMap map = new HashMap();

		ZqlParser parser = new ZqlParser();
		parser.initParser(new ByteArrayInputStream(query.getBytes()));
		ZStatement st = parser.readStatement();
		ZDelete q = (ZDelete) st;

		whereList = new ArrayList();

		// table
		String table = q.getTable();

		// where
		ZExpression w = (ZExpression) q.getWhere();
		if (w != null) {
			Hashtable meta = new Hashtable(1);
			meta.put(table, table);
			handleWhere(w, meta);
		}

		map.put("table", table);
		map.put("where", whereList);

		return map;
	}

	public static void handleWhere(ZExp e, Hashtable meta) throws Exception {
		if (!(e instanceof ZExpression))
			return;
		ZExpression w = (ZExpression) e;

		Vector operands = w.getOperands();
		if (operands == null)
			return;

		String prepared = null;
		for (int i = 0; i < operands.size(); i++) {
			if (isPreparedColumn((ZExp) operands.elementAt(i))) {
				prepared = ((ZConstant) operands.elementAt(0)).getValue();
				if (operands.size() != 2) {
					throw new Exception("ERROR in where clause ?? found:"
							+ w.toString());
				}
				break;
			}
		}

		if (prepared != null) {
			boolean noalias = false;
			String tbl = null;

			int pos = prepared.lastIndexOf('.');
			if (pos > 0) {
				tbl = prepared.substring(0, pos);
				prepared = prepared.substring(pos + 1);

				if ((pos = tbl.lastIndexOf('.')) > 0) {
					tbl = tbl.substring(pos + 1);
					noalias = true;
				}
			}

			// Now tbl is the table name or null, prepared is the column name
			// Note tbl may be an alias
			// (like in "select * from mytable t where t.mykey=1", the table
			// name is
			// "mytable", not "t")

			if (!noalias) {
				// If tbl is an alias, resolve it
				if (tbl != null)
					tbl = (String) meta.get(tbl.toUpperCase());
			}

			if (tbl == null && meta.size() == 1) {
				Enumeration keys = meta.keys();
				tbl = (String) keys.nextElement();
			}
			whereList.add(prepared);
		} else { // No prepared column, go further analyzing the expression
			for (int i = 0; i < operands.size(); i++) {
				handleWhere(w.getOperand(i), meta); // WARNING - Recursive
													// call...
			}
		}
	}

	public static boolean isPreparedColumn(ZExp v) {
		return (v instanceof ZExpression && ((ZExpression) v).getOperator()
				.equals("?"));
	}

	public static void main(String args[]) throws Exception {
		// String query = "SELECT A, B, C FROM ABC WHERE A='A' AND B=? AND C=?
		// AND D=? AND E=? AND F=? AND G=?;";
		// String query = "INSERT INTO ABC(A, B, C, D, E, F, G, H, I)
		// VALUES('1', '2', '3', '4', '5', '6', '7', '8', '9');";
		String query = "UPDATE ABC SET A=?, B=?, C=?, D=?, E=?, F=? , G=?, H=? WHERE A=? AND B=? AND C=?;";
		// String query = "DELETE FROM ABC WHERE A=? AND B=? AND C=? AND D=? AND
		// E=? AND F=? AND G=?;";

		Map map = getUpdate(query);

		System.out.println(map);
	}
}
