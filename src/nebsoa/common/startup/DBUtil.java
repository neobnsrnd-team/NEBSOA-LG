/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.startup;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import nebsoa.common.exception.DBException;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 프레임웍 구동시 필요한 config 정보를 로딩하기 위한 유틸 클래스
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
 * $Log: DBUtil.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:34  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:27  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:29  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:34  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:39:16  안경아
 * *** empty log message ***
 *
 * Revision 1.7  2006/06/17 10:37:45  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class DBUtil {
    
    public static DataSource getDataSource() {
        return getDataSource(StartupContext.getJndiName());
    }
    
    public static DataSource getDataSource(String jndiPath) {
        InitialContext ctx;
        Properties prop = new Properties();
        prop.put(Context.INITIAL_CONTEXT_FACTORY, StartupContext.getInitialContextFactory());
        prop.put(Context.PROVIDER_URL, StartupContext.getProviderUrl());

        try {
            ctx = new InitialContext(prop);
            return (DataSource) ctx.lookup(jndiPath);
        } catch (NamingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * WAS의 JNDI명을 이용해서 DB Connection을 구해서 돌려준다.
     * 
     * @return Connection
     */
    public static Connection getConnection() {
        DataSource dataSource;
        Connection conn;
        try {
            dataSource = getDataSource();
            conn = dataSource.getConnection();
        } catch (SQLException sqle) {
            throw new DBException("SQLException while getting connection: "
                    + sqle.getMessage());
        } catch (NullPointerException e) {
            throw new DBException("SQLException while getting connection: "
                    + e.getMessage());
        }
        return conn;
    }
   
}
