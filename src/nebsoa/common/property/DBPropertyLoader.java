/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.property;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import nebsoa.common.exception.DBException;
import nebsoa.common.startup.DBUtil;
import nebsoa.common.startup.StartupContext;

/*******************************************************************
 * <pre>
 * 1.설명 
 * DB로 부터 프로퍼티를 로딩하는 클래스
 * 
 * 2.사용법
 * 
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author $Author: cvs $
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 
 * $Log: DBPropertyLoader.java,v $
 * Revision 1.1  2018/01/15 03:39:52  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:15  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:26  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:28  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:34  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:22  안경아
 * *** empty log message ***
 *
 * Revision 1.12  2006/06/17 10:37:45  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class DBPropertyLoader extends PropertyLoader {
    
    /**
     * 로컬의 IP 주소
     */
    public static String INSTANCE_ID;
    
    public DBPropertyLoader(String propertyGroupId) {
        super(propertyGroupId);
        load();
    }

    /**
     * DB로부터 프로퍼티를 로드하기 위한 SQL
     */
    private static final String PROPERTY_LOAD_SQL = 
          "   SELECT B.PROPERTY_GROUP_ID, "
        + "     B.PROPERTY_ID, "
        + "     DECODE(A.PROPERTY_VALUE, NULL, B.DEFAULT_VALUE, A.PROPERTY_VALUE) PROPERTY_VALUE, "
        + "     PROPERTY_DESC, "
        + "     CACHE_YN "
        + "   FROM INSTANCE_PROPERTY A, "
        + "     PROPERTY B, "
        + "     PROPERTY_GROUP C "
        + "   WHERE B.PROPERTY_GROUP_ID = ? "
        + "     AND A.INSTANCE_ID(+) = ? "
        + "     AND A.PROPERTY_GROUP_ID(+) = B.PROPERTY_GROUP_ID "
        + "     AND A.PROPERTY_ID(+) = B.PROPERTY_ID "
        + "     AND B.PROPERTY_GROUP_ID = C.PROPERTY_GROUP_ID ";

    /**
     * (non-Javadoc)
     * 
     * @see serverside.common.property.PropertyLoader#load()
     */
    public void load() {
        QueryRunner run = new QueryRunner(DBUtil.getDataSource());
        Object[] params = {
                propertyGroupId,StartupContext.getInstanceId()
        };
        ResultSetHandler rsHandler = new ResultSetHandler() {
            public Object handle(ResultSet rs) throws SQLException {
                while (rs.next()) {
                    setProperty(
                            rs.getString("PROPERTY_ID"),
                            rs.getString("PROPERTY_VALUE"),
                            rs.getString("PROPERTY_DESC"),
                            false
                    );
                    setUsePropertyCache(rs.getString("CACHE_YN").equals("Y"));
                }
                return null;
            }
        };

        try {
            run.query(PROPERTY_LOAD_SQL, params, rsHandler);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException(e);
        }
    }

    /**
     * DB에 프로퍼티를 갱신하기 위한 SQL 
     */
    private static final String PROPERTY_UPDATE_SQL = 
              " UPDATE PROPERTY "
            + " SET " 
            + "  PROPERTY_DESC=?, " 
            + "  DEFAULT_VALUE=? " 
            + " WHERE PROPERTY_GROUP_ID=? "
            + " AND   PROPERTY_ID=? ";
    
    /**
     * (non-Javadoc)
     * 
     * @see serverside.common.property.PropertyLoader#store()
     */
    public void store() {
        
        Collection c = propertyCache.values();
        Iterator i = c.iterator();
        PropertyItem propertyItem = null;
        
        QueryRunner run = new QueryRunner(DBUtil.getDataSource());
        
        Object[] params = new Object[4]; 
        params[2] = this.propertyGroupId;
        
        try {
            while(i.hasNext()) {
                propertyItem = (PropertyItem) i.next();
                params[0] = propertyItem.getDesc();
                params[1] = propertyItem.getValue();
                params[3] = propertyItem.getKey();
                run.update(PROPERTY_UPDATE_SQL, params);
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }
    
    /**
     * DB에 프로퍼티를 저장하기 위한 SQL 
     */
    private static final String PROPERTY_STORE_SQL = 
              " INSERT INTO PROPERTY "
            + " ( " 
            + "  PROPERTY_GROUP_ID,"
            + "  PROPERTY_ID, " 
            + "  PROPERTY_DESC, " 
            + "  DEFAULT_VALUE " 
            + " ) " 
            + " VALUES (" 
            + "  ?, "
            + "  ?, "
            + "  ?, "
            + "  ? "
            + " ) ";
    
    /* (non-Javadoc)
     * @see nebsoa.common.property.PropertyLoader#insert(nebsoa.common.property.PropertyItem)
     */
    public void insert(PropertyItem propertyItem) {
        
        QueryRunner run = new QueryRunner(DBUtil.getDataSource());
        
        Object[] params = new Object[4]; 
        params[0] = this.propertyGroupId;
        params[1] = propertyItem.getKey();
        params[2] = propertyItem.getDesc();
        params[3] = propertyItem.getValue();
        
        try {
            run.update(PROPERTY_STORE_SQL, params);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    /**
     * DB에서 프로퍼티를 삭제하기 위한 SQL 
     */
    private static final String PROPERTY_DELETE_SQL = 
              " DELETE FROM PROPERTY "
            + " WHERE PROPERTY_GROUP_ID=? "
            + " AND   PROPERTY_ID=? ";
    
    /* (non-Javadoc)
     * @see nebsoa.common.property.PropertyLoader#delete(java.lang.String)
     */
    public void delete(String key) {
        
        QueryRunner run = new QueryRunner(DBUtil.getDataSource());
        
        Object[] params = new Object[2]; 
        params[0] = this.propertyGroupId;
        params[1] = key;
        
        try {
            run.update(PROPERTY_DELETE_SQL, params);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }
}
