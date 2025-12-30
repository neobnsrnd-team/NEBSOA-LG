/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.list;

import java.sql.ResultSet;
import java.util.ArrayList;

import nebsoa.common.Context;
import nebsoa.common.collection.DataSet;
import nebsoa.common.jdbc.DBHandler;
import nebsoa.common.util.DbTypeUtil;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 리스트 출력을 위한 paging query를 수행 하는 클래스
 * 
 * 
 * 
 * 2.사용법
 * 
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author 이종원
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 
 * $Log: ListQueryHandler.java,v $
 * Revision 1.1  2018/01/15 03:39:51  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:00  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:24  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:28  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:51  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:22  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:37:38  안경아
 * *** empty log message ***
 *
 * Revision 1.4  2007/01/17 02:11:26  김성균
 * import optimize
 *
 * Revision 1.3  2006/09/20 03:41:56  이종원
 * import optimize
 *
 * Revision 1.2  2006/07/05 22:05:24  오재훈
 * *** empty log message ***
 *
 * Revision 1.1  2006/07/05 21:53:18  오재훈
 * ListManager의 DBHandler 타입 추가.
 *
 * Revision 1.5  2006/06/19 04:10:16  안경아
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class ListQueryHandler {
    
    String columns="*";
    
    private ListQueryBuilder queryBuilder;
    
//    static ListQueryBuilder queryBuilder;
//    
//    static {      
//        int dbType = DbTypeUtil.getDbType();
//        
//        switch (dbType) {
//        	case DbTypeUtil.ORACLE:
//        	    queryBuilder = new OracleListQueryBuilder();
//        	    break;
//        	case DbTypeUtil.MY_SQL:
//        	    queryBuilder = new MySqlListQueryBuilder();
//        	    break;        
//        }//end switch case
//    }//end of static
    
    public ListQueryHandler() {
        int dbType = DbTypeUtil.getDbType();

        switch (dbType) {
        	case DbTypeUtil.ORACLE:
        	    queryBuilder = new OracleListQueryBuilder();
        	    break;
        	case DbTypeUtil.MY_SQL:
        	    queryBuilder = new MySqlListQueryBuilder();
        	    break; 
        	case DbTypeUtil.SYBASE:
        	    queryBuilder = new SybaseListQueryBuilder();
        	    break;    
       	    
        }//end switch case
    }//end of constructor
    
    public ListQueryHandler(String dbName) {
        int dbType = DbTypeUtil.getDbType(dbName);
        
        switch (dbType) {
        	case DbTypeUtil.ORACLE:
        	    queryBuilder = new OracleListQueryBuilder();
        	    break;
        	case DbTypeUtil.MY_SQL:
        	    queryBuilder = new MySqlListQueryBuilder();
        	    break;
        	case DbTypeUtil.SYBASE:
        	    queryBuilder = new SybaseListQueryBuilder();
        	    break;
        }//end switch case
    }//end of constructor
    /**
     * 리스트 출력을 위한 paging query를 수행   
     */    
    public DataSet executeListQuery(String dbName, String sql,
            ArrayList param, int pageNum, int displayCount) {
    	return executeListQuery(null,dbName, sql,param, pageNum, displayCount);
    }
    /**
     * 리스트 출력을 위한 paging query를 수행   
     */
    public DataSet executeListQuery(Context ctx,String dbName, String sql,
    	            ArrayList param, int pageNum, int displayCount) {	
        if (queryBuilder instanceof MySqlListQueryBuilder) {
	        param.add(Integer.valueOf((pageNum - 1) * displayCount));// 시작 record
	        param.add(Integer.valueOf(displayCount));// record count 
	        
	        return DBHandler.executePreparedQuery(ctx,dbName, queryBuilder
	                .buildQuery(sql), param.toArray());
	        
	    /*
	     * 오라클의 리스트쿼리 수정으로
	     * 파라미터의 순서가 바뀌어서 수정함
	     * 
	     * 2006.01.07 - Helexis
	     */
        } else if (queryBuilder instanceof OracleListQueryBuilder) {
        	param.add(Integer.valueOf(pageNum * displayCount));// 끝 record
        	param.add(Integer.valueOf((pageNum - 1) * displayCount + 1));// 시작 record
        	
        	return DBHandler.executePreparedQuery(ctx,dbName, queryBuilder
	                .buildQuery(sql), param.toArray());
        	
        /*
         * Sybase의 페이징은 sql쿼리가 아닌 java소스에서 구현
         * 
         * 2006.04.25 - sjChoi 추가
         */	
        } else if (queryBuilder instanceof SybaseListQueryBuilder) {
        	param.add(0, Integer.valueOf(pageNum * displayCount));// 끝 reocord
        	
        	int skipCount = (pageNum - 1) * displayCount;  // skip할 행의 수
        	
        	return DBHandler.executePreparedQuery(ctx,dbName, queryBuilder
	                .buildQuery(sql), param.toArray(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, skipCount);
        } else {
	        param.add(Integer.valueOf((pageNum - 1) * displayCount + 1));// 시작 record
	        param.add(Integer.valueOf(pageNum * displayCount));// 끝 record
	        
	        return DBHandler.executePreparedQuery(ctx,dbName, queryBuilder
	                .buildQuery(sql), param.toArray());
        }
    }
    
    /**
     * 리스트 출력을 위한 paging query를 수행   
     * @param sql
     * @param param
     * @param pageNum
     * @param displayCount
     * @return
     * 설명: default db를 사용하여 list query 수행
     */
    public DataSet executeListQuery(String sql,ArrayList param,
										int pageNum ,int displayCount){
    	String dbName=null;
        return executeListQuery(dbName, sql, param, pageNum , displayCount);
    }
    
    /**
     * 리스트 출력을 위한 paging query를 수행   
     * @param sql
     * @param param
     * @param pageNum
     * @param displayCount
     * @return
     * 설명: default db를 사용하여 list query 수행
     */
    public DataSet executeListQuery(Context ctx,String sql,ArrayList param,
										int pageNum ,int displayCount){
    	
        return executeListQuery(ctx,null, sql, param, pageNum , displayCount);
    }
    /**
     * 리스트 출력을 위한 paging query를 수행   
     * @param sql
     * @param param
     * @param pageNum
     * @return
     * 설명: default db사용 및 한페이지에 10건 display
     */
    public DataSet executeListQuery(String sql,ArrayList param, int pageNum){
    	String dbName=null;
        return executeListQuery(dbName, sql, param, pageNum , 10);
    }
    /**
     * 
     * 2004. 10. 29. 이종원 작성
     * @param sql
     * @param param
     * @param pageNum
     * @return
     * 설명: default db사용 및 한페이지에 10건 display
     */
    public DataSet executeListQuery(Context ctx,String sql,ArrayList param, int pageNum){
    	String dbName=null;
        return executeListQuery(ctx,dbName, sql, param, pageNum , 10);
    }
    /**
     * 리스트 출력을 위한 paging executeCountQuery 수행   
     * @param sql
     * @param param
     * @return
     * 설명:execute Count Query
     */
    public static int executeCountQuery(String sql,ArrayList param){
    	String dbName=null;
        return executeCountQuery(dbName, sql, param);
    }
    
    
    /**
     * 리스트 출력을 위한 paging executeCountQuery 수행   
     * @param sql
     * @param param
     * @return
     * 설명:execute Count Query
     */
    public static int executeCountQuery(Context ctx,String sql,ArrayList param){
    	String dbName=null;
        return executeCountQuery(ctx,dbName, sql, param);
    }
    
    /**
     * 리스트 출력을 위한 paging executeCountQuery 수행   
     * @param dbName
     * @param sql
     * @param param
     * @return
     * 설명:execute Count Query to selected db
     */
    private static int executeCountQuery(String dbName, String sql, ArrayList param) {
    	return executeCountQuery(null,dbName,sql, param);
    }
    
    /**
     * 리스트 출력을 위한 paging executeCountQuery 수행   
     * @param dbName
     * @param sql
     * @param param
     * @return
     * 설명:execute Count Query to selected db
     */
    private static int executeCountQuery(Context ctx,String dbName, String sql, ArrayList param) {
        DataSet rs = DBHandler.executePreparedQuery(ctx,dbName,sql,param.toArray());
        rs.next();
        return rs.getInt(1);
    }
    /**
     * @return columns의 값을 얻어 옵니다.
     */
    public String getColumns() {
        return columns;
    }

    /**
     * @param columns The columns을 세팅합니다.
     */
    public void setColumns(String columns) {
        this.columns = columns;
    }

    
    // 테스트
    public static void main(String [] args) throws Exception
    {
//    	System.setProperty("SERVERSIDE_HOME", "C:/PMH_TEMP/workspace/spider");
    	ListQueryHandler listManager = new ListQueryHandler();
    	
    	String query = "SELECT Article_No, Ext_No, Ref_No, Seq_No, Depth_Level, Article_Title, Article_Content FROM testdb.Notice_Board ORDER BY Article_No DESC";
    	int page = 1;  // 현재 페이지 넘버
    	int displayCount = 10;  // 보여줄 ROW 수
    	ArrayList paramList = new ArrayList();
    	
        DataSet rs = listManager.executeListQuery(query,paramList,
        		page,displayCount);
        
        System.out.print("rs :"+rs);
    }
}
