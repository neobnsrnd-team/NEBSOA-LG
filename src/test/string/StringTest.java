package test.string;

import nebsoa.common.collection.DataSet;
import nebsoa.common.jdbc.DBManager;

public class StringTest {
    public static void main(String[] args) throws Exception {
        DataSet ds = DBManager.executeQuery("select * from fwk_locale_label");
        long s = System.currentTimeMillis();
        System.out.println("s = " + s);
        /*
        String str = ds.toString();
        if (str.indexOf("잔액") != -1) {
            System.out.println("find");
        }
        */
        int i = 0;
        for (; ds.next(); i++) {
            if ("잔액".equals(ds.getString("LABEL_TEXT"))) {
                System.out.println("find" + i);
            }
        }
        long e = System.currentTimeMillis();
        System.out.println("e = " + e);
        long execTime = e - s;
        System.out.println("exec count = " + i);
        System.out.println("exec time = " + execTime);
    }
}
