/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.monitor;

import java.io.Serializable;
import java.math.BigDecimal;

import nebsoa.common.log.LogManager;
import nebsoa.common.util.FormatUtil;
import nebsoa.common.util.PropertyManager;
import nebsoa.common.util.StringUtil;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 특정 모듈의 수행 로그(총 수행 횟수, 평균시간, 최대 시간등) 통계를 산출 하는 클래스
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
 * $Log: MonitorData.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:29  cvs
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
 * Revision 1.1  2008/08/04 08:54:55  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.3  2008/03/21 04:39:30  김성균
 * getEtcInfo() 추가
 *
 * Revision 1.2  2008/03/20 09:42:32  김성균
 * 동기화 처리
 *
 * Revision 1.1  2008/01/22 05:58:33  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:43  안경아
 * *** empty log message ***
 *
 * Revision 1.16  2007/06/05 10:39:24  이종원
 * monitor data clear로직 추가
 *
 * Revision 1.15  2007/05/03 10:15:20  이종원
 * 평균 수행시간 계산 로직 수정
 *
 * Revision 1.14  2007/02/01 05:25:26  김성균
 * 주석 정리
 *
 * Revision 1.13  2006/12/15 02:33:13  김성균
 * *** empty log message ***
 *
 * Revision 1.12  2006/12/13 07:13:23  김성균
 * *** empty log message ***
 *
 * Revision 1.11  2006/12/13 05:52:45  김성균
 * *** empty log message ***
 *
 * Revision 1.10  2006/12/12 10:20:46  김성균
 * *** empty log message ***
 *
 * Revision 1.9  2006/12/12 08:08:36  김성균
 * *** empty log message ***
 *
 * Revision 1.8  2006/12/12 07:27:26  김성균
 * *** empty log message ***
 *
 * Revision 1.7  2006/12/12 05:16:42  김성균
 * *** empty log message ***
 *
 * Revision 1.6  2006/11/29 01:51:19  오재훈
 * *** empty log message ***
 *
 * Revision 1.5  2006/11/06 08:19:12  오재훈
 * *** empty log message ***
 *
 * Revision 1.4  2006/06/17 10:37:45  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/ 
public class MonitorData implements Serializable {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 7217289032286925922L;

	/**
	 * Comment for <code>category</code>
	 */
	protected String category = null;

    /**
     * Comment for <code>info</code>
     */
	protected String info = null;

    /**
     * Comment for <code>etcInfo</code>
     */
	protected String etcInfo = null;

    /**
     * Comment for <code>exeTime</code>
     */
	protected double exeTime = 0;

    /**
     * Comment for <code>exeCount</code>
     */
	protected int exeCount;

    /**
     * Comment for <code>avgCount</code>
     */
	protected int avgCount;

    /**
     * Comment for <code>totalExeTime</code>
     */
	protected double totalExeTime;

    /**
     * Comment for <code>maxExeTime</code>
     */
	protected double maxExeTime;

    /**
     * Comment for <code>minExeTime</code>
     */
    public double minExeTime;
    
	public MonitorData(String category, String info,long exeTime) {
        this(category, info, exeTime, null);
	}
    
	public MonitorData(String category, String info,long exeTime, String etcInfo) {
		this.category = category;
        this.info = info;
        this.etcInfo = etcInfo;
        this.exeTime = exeTime;
        int threshold = 2000;
		if(category.equals("Database")){
		    threshold=PropertyManager.getIntProperty("monitor","SQL_EXE_TIME_THRESHOLD", 3000);
		}else if(category.equals("Web")){
		    threshold=PropertyManager.getIntProperty("monitor","WEB_EXE_TIME_THRESHOLD", 3000);
		}else if(category.equals("Message")){
		    threshold=PropertyManager.getIntProperty("monitor","MESSAGE_EXE_TIME_THRESHOLD", 3000);
		}else{
		    threshold=PropertyManager.getIntProperty("monitor","DEFAULT_EXE_TIME_THRESHOLD", 2000);
		}
		if(exeTime >= threshold){
		    LogManager.info("TUNNING_LIST","수행시간초과:"+(exeTime/1000.0)+",category:"+category+","+info);
		}
	}
	
	public void update(double exeTime){
        update(exeTime, true);
	}
    
	/**
     * isAvgCalc가 true일 경우,
     * 총수행시간에 포함하고, 최대,최소수행시간에 적용여부를 판단해서 적용한다.
     * 
     * @param exeTime 수행시간
     * @param isAvgCalc 평균값 계산에 포함여부
     */
    public void update(double exeTime, boolean isAvgCalc) {
        exeCount = exeCount + 1;
        if (isAvgCalc) {
            totalExeTime = totalExeTime + exeTime;
            avgCount = avgCount + 1;
            if (maxExeTime < exeTime) {
                maxExeTime = exeTime;
            }
            if (minExeTime == 0 || minExeTime > exeTime) {
                minExeTime = exeTime;
            }
        }
    }
	
	public String getHtmlReport(){
        if (StringUtil.isNull(etcInfo)) {
      	    return "\t<tr><td>"+info+"</td><td align='right'>"+exeCount+"</td><td align='right'>"+getTotalExeSecond()
		 	+"</td><td align='right'>"+getMaxExeSecond()+"</td><td align='right'>"+getMinExeSecond()+"</td><td align='right'>"+getAvgExeSecond()+"</td></tr>\r\n";
        } else {
            return "\t<tr><td>"+info+"("+etcInfo+")"+"</td><td align='right'>"+exeCount+"</td><td align='right'>"+getTotalExeSecond()
		 	+"</td><td align='right'>"+getMaxExeSecond()+"</td><td align='right'>"+getMinExeSecond()+"</td><td align='right'>"+getAvgExeSecond()+"</td></tr>\r\n";
        }
	}
	
	public String getReport(){
        if (StringUtil.isNull(etcInfo)) {
            return info+"\t"+exeCount+"\t"+getTotalExeSecond()
				+"\t"+getMaxExeSecond()+"\t"+getMinExeSecond()+"\t"+getAvgExeSecond()+"\r\n";
        } else {
            return info+"\t"+etcInfo+"\t"+exeCount+"\t"+getTotalExeSecond()
				+"\t"+getMaxExeSecond()+"\t"+getMinExeSecond()+"\t"+getAvgExeSecond()+"\r\n";
        }
	}
	
	public static String getHtmlHeader(){
		return "<table class='bdMode02' id='sort' border='1' cellpadding='0' cellspacing='0' "
				+" bordercolor='#D6E6F3' style='word-wrap:break-word; word-break:break-all'>"
				+"\r\n\t<col width='510'>"
				+"\r\n\t<col width='50'>"
				+"\r\n\t<col width='50'>"
				+"\r\n\t<col width='50'>"
				+"\r\n\t<col width='50'>"
				+"\r\n\t<col width='50'>"
				+"\r\n\t<thead><tr><td class='tableAHead01'>Detail Info</td><td class='tableAHead01'>Exe Count</td><td class='tableAHead01'>Total Exe Time"
				+"</td><td class='tableAHead01'>MAX Exe Time</td><td class='tableAHead01'>MIN Exe Time</td><td class='tableAHead01'>AVG Exe Time</td></tr>\r\n</thead>\r\n<tbody>";
	}
    
	public static String getHtmlTail(){
		return "</tbody></table>\r\n"
		+"<script>\r\n"
		+" var st = new SortableTable(document.getElementById('sort'),\r\n"
		+"  ['String', 'Number', 'Number', 'Number', 'Number', 'Number']); "
		+" st.sort(5);st.sort(5);" //평균수행시간의 역순으로 조회
		+"</script>";
	}
    
	/**
	 * @return
	 */
	public int getExeCount() {
		return exeCount;
	}

	/**
	 * @return
	 */
	public double getTotalExeTime() {
		return totalExeTime;
	}
	
	/**
	 * @return
	 */
	public String getTotalExeSecond() {
		return format(totalExeTime/1000.0);
	}

	/**
	 * @return
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @return
	 */
	public double getExeTime() {
		return exeTime;
	}

	/**
	 * @return
	 */
	public String getInfo() {
		return info;
	}

	/**
	 * @param i
	 */
	public void setExeCount(int i) {
		exeCount = i;
	}

	/**
	 * @param d
	 */
	public void setTotalExeTime(double d) {
		totalExeTime = d;
	}

	/**
	 * @param string
	 */
	public void setCategory(String string) {
		category = string;
	}

	/**
	 * @param d
	 */
	public void setExeTime(double d) {
		exeTime = d;
	}

	/**
	 * @param string
	 */
	public void setInfo(String string) {
		info = string;
	}
	
	/**
	 * @return Returns the etcInfo.
	 */
	public String getEtcInfo() {
		return etcInfo;
	}

	public static String format(double input){
        if(input < 0.001){
            BigDecimal num = new BigDecimal(input);
            try{
                return (""+num).substring(0,6);
            }catch(Exception e){
                return "0.0005";
            }
        }
		return FormatUtil.makeFormat( input,"9.9999");		
	}
	
//	public int hashCode(){
//		return (category+info).hashCode();
//	}
//	
//	public boolean equals(Object obj){
//		if(obj==null) return false;
//		if(obj instanceof MonitorData){
//			return hashCode()==obj.hashCode();
//		}else{
//			return false;
//		}
//	}

	/**
	 * @return
	 */
	public double getMaxExeTime() {
		return maxExeTime;
	}
    
	/**
	 * @return
	 */
	public double getMinExeTime() {
		return minExeTime;
	}
	
	/**
	 * @return
	 */
	public String getMaxExeSecond() {
		return format(maxExeTime/1000.0);
	}
    
	/**
	 * @return
	 */
	public String getMinExeSecond() {
		return format(minExeTime/1000.0);
	}
	
	/**
	 * @return
	 */
	public String getAvgExeSecond() {
        double data =(1.0*totalExeTime/avgCount)/1000.0;
        if(data*1000.0 > totalExeTime){
            LogManager.info("MONITOR",
                    "성능 모니터링 데이터 오류:totalExeTime"+totalExeTime
                    +",avgCount:"+avgCount);
        }
		return format(((1.0*totalExeTime/avgCount)/1000.0));
	}
	
	/**
	 * 평균 수행 시간이 특정 시간 이상을 초과 하는지 판단
	 * @return boolean 
	 */
	public boolean isAvgOver(long second) {
		return (((1.0*totalExeTime/avgCount)/1000.0)) >= second;
	}
	
	/**
	 * 해당 테이블 명 또는 문자열을 포함하는지 필터링
	 * @return boolean 
	 */
	public boolean containsString(String str) {
		if(StringUtil.isNull(str) || str.length() < 3) return true;
		return info.indexOf(str) > -1;
	}

	/**
	 * @param d
	 */
	public void setMaxExeTime(double d) {
		maxExeTime = d;
	}
    
	/**
	 * @param d
	 */
	public void setMinExeTime(double d) {
		minExeTime = d;
	}
    
    public int getAvgCount() {
        return avgCount;
    }

    public void setAvgCount(int avgCount) {
        this.avgCount = avgCount;
    }
    
    
    
    public static void main(String[] args){
/**
        MonitorData data = new MonitorData("Database","SELECT * FROM EMP",1);
        for(int i=0;i<10;i++){
            data.update(1*i);
            System.out.println(data.getReport());
        }
**/        
        System.out.println(format(5667123.456));
        System.out.println(format(5667123.4));
        System.out.println(format(5667123.4569999));
        
        System.out.println(format(0.00012345));
        System.out.println(format(0.12345));
        System.out.println(format(0.012345));
        System.out.println(format(0.0012345));
        System.out.println(format(0.00012345));

        
        MonitorData data2 = new MonitorData("Database","SELECT * FROM EMP2",1);
        data2.setCategory("111");
        data2.setExeCount(9);
        data2.setTotalExeTime(20);
        data2.setAvgCount(9);
        System.out.println(data2.getAvgExeSecond());
    }


}
