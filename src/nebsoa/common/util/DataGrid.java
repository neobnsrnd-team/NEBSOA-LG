/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.util;

/*******************************************************************
 * <pre>
 * 1.설명 
 * grid data출력을 위한 utility
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
 * $Log: DataGrid.java,v $
 * Revision 1.1  2018/01/15 03:39:49  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:31  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:23  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:26  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:50  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:17  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:04  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2006/06/17 10:37:45  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class DataGrid {

    private String[] titles;
    private String[][] data;
    /**
     * @param args
     */
    public DataGrid(){
        
    }

    public String[][] getData() {
        return data;
    }
    public void setData(String[][] data) {
        this.data = data;
    }
    public String[] getTitles() {
        return titles;
    }
    public void setTitles(String[] titles) {
        this.titles = titles;
    }
    
    public String getHtmlTableString(){
        
        
        StringBuffer buf = new StringBuffer();
        buf.append("\n<table border='1' cellspacing='0' cellpadding='0'>");
        
        if(titles!=null){
            buf.append("\n\t<tr>");
            for(int i=0;i<titles.length;i++){
                buf.append("\n\t\t<th>");
                buf.append(titles[i]);
                buf.append("</th>");
            }
            buf.append("\n\t</tr>");
        }
        for(int i=0;i<data.length;i++){
            buf.append("\n\t<tr>");
            for(int j=0;j<data[i].length;j++){
                buf.append("\n\t\t<td>");
                buf.append(data[i][j]==null?"-":data[i][j]);
                buf.append("</td>");
            }
            buf.append("\n\t</tr>");
        }
        buf.append("\n</table>");
        return buf.toString();
    }
    
    public static void main(String[] args) {
        DataGrid grid = new DataGrid();
        grid.setTitles(new String[]{"A","B","C"});
        String[][] data = new String[][]{
                {"1","2","3"},
                {"4","5","6"}
        };
        grid.setData(data);
        System.out.println(grid.getHtmlTableString());

    }

}
