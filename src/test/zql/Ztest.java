package test.zql;

import java.io.ByteArrayInputStream;
import java.util.Vector;

import Zql.ZExp;
import Zql.ZFromItem;
import Zql.ZQuery;
import Zql.ZSelectItem;
import Zql.ZStatement;
import Zql.ZqlParser;

public class Ztest {

    public static void main(String args[]) throws Exception {
//        String sql = "SELECT ANTIQUEOWNERS.OWNERLASTNAME, ANTIQUEOWNERS.OWNERFIRSTNAME FROM ANTIQUEOWNERS, ANTIQUES WHERE ANTIQUES.BUYERID = ANTIQUEOWNERS.OWNERID AND ANTIQUES.ITEM = 'Chair';";
        String sql = "SELECT DECODE(TEST, 'A', 'B') B FROM ANTIQUEOWNERS, ANTIQUES WHERE ANTIQUES.BUYERID = ANTIQUEOWNERS.OWNERID AND ANTIQUES.ITEM = 'Chair';";

        ZqlParser p = new ZqlParser();

        p.initParser(new ByteArrayInputStream(sql.getBytes()));
        ZStatement st = p.readStatement();
        System.out.println(st.toString()); // Display the statement

        ZQuery q = (ZQuery) st;
        Vector v = q.getSelect();
        for (int i = 0; i < v.size(); i++) {
            ZSelectItem it = (ZSelectItem) v.elementAt(i);
            System.out.println("col=" + it.getColumn() + ",agg="
                    + it.getAggregate());
        }
        v = q.getFrom();
        for (int i = 0; i < v.size(); i++) {
            ZFromItem it = (ZFromItem) v.elementAt(i);
            System.out.println("table=" + it.getTable());
        }
        ZExp where = q.getWhere();
        System.out.println("where=" + where);
    }
}
