/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.monitor.profiler;

import java.text.DecimalFormat;

/*******************************************************************
 * <pre>
 * 1.설명 
 * WAS의 일련의 수의 여러가지 통계를 모아 계산합니다.
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
 * $Log: Stats.java,v $
 * Revision 1.1  2018/01/15 03:39:51  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:07  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:25  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:28  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:54  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:31  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:51  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2006/06/17 10:58:12  오재훈
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class Stats implements java.io.Serializable, Comparable
{

    final private String name;

    private static final DecimalFormat FORMAT = new DecimalFormat("########.##");

    private int sumTotalValue = 0;
    private int sumSquareValues = 0;
    private int count = 0;   
    private int high = Integer.MIN_VALUE;
    private int low = Integer.MAX_VALUE;    
    private int observations = 0;

    public Stats(String name) {
        this.name = name;
    }

    public Stats(Stats copy) {
        this.name = copy.name;
        this.sumTotalValue = copy.sumTotalValue;
        this.sumSquareValues = copy.sumSquareValues;
        this.count = copy.count;
        this.high = copy.high;
        this.low = copy.low;
        this.observations = copy.observations;
    }

    public String getName() {
        return name;
    }

    /**
      * Order by getTotal(), getMean(), getHigh(), getLow(), and getName()
      */
    public int compareTo(Object o) {
        if (o == null)
            return 1;
        if (this == o)
            return 0;
        try {
            Stats so = (Stats) o;
            int ret = so.sumTotalValue - sumTotalValue;
            if (ret != 0)
                return ret;

            double dret = so.getMean() - getMean();
            if (dret < 0)
                return -1;
            else if (dret > 0)
                return 1;

            ret = so.high - high;
            if (ret != 0)
                return ret;
            
            ret = so.low - low;
            if (ret != 0)
                return ret;

            ret = so.sumSquareValues - sumSquareValues;
            if (ret != 0)
                return ret;

            ret = so.count - count;
            if (ret != 0)
                return ret;

            ret = so.observations - observations;
            if (ret != 0)
                return ret;

            return name.compareTo(so.name);

        } catch (ClassCastException ce) {
            return Stats.class.getName().compareTo(o.getClass().getName());
        }
    }

    public boolean equals(Object o) {
        return compareTo(o) == 0;
    }

    public int hashCode() {
        return name.hashCode();
    }

    /**
      * Produce statistics that represent these values combined with
      * those values. 
      */
    public void add(Stats other) {
        sumTotalValue += other.sumTotalValue;
        sumSquareValues += other.sumSquareValues;
        count += other.count;
        observations += other.observations;
        low = Math.min(low,other.low);
        high = Math.max(high, other.high);
    }


    /**
     * Record an observation of a single event
     */
    public void observe(int value) {
        observe(1,value);
    }

    /**
     * Record an observation of a repeated event
     */
    public void observe(int quantity, int value) {          
        count += quantity;
        sumTotalValue += quantity * value;
        sumSquareValues += quantity * value * value;                      
        if (value > high) {
            high = value;
        }
        if (value < low) {
            low = value;
        }
        observations++;     
    }           
   

    /**
     * How many ticks these statistics are based on. If there 
     * were fewer ticks available than asked for, this will 
     * be less than the requested number.
     */
    public int getCount() {
        return count;
    }

    /** mean price */
    public double getMean() {
        return (count != 0) ? ((double) sumTotalValue / (double) count) : 0;
    }

    /** mean squared deviation of price */
    public double getVariation() {
        if (count == 0)
            return 0.0;
        double avg = getMean();
        double avgSquared = count * avg * avg;
        double variation =  
            (sumSquareValues - 2.0 * avg * sumTotalValue + avgSquared)
                /count;       
        return variation;
    }

    /** square root of the variation, the standard deviation */
    public double getDeviation() {
        return Math.sqrt(getVariation());
    }

    /** highest price observed */
    public int getHigh() {
        return high;
    }

    /** lowest price observed */
    public int getLow() {
        return low;
    }

    /** volume: total number of events observed. this will differ 
      * from getCount if in observe(quantity,value) quantity 
      * was not always one: getCount will return the sum of 
      * all the quantities, getObservations will return the number of 
      * times observe() was called.
      */
    public int getObservations() {
        return observations;
    }

    /** total: sumTotalValue */
    public int getTotal() {
        return sumTotalValue;
    }

    /**
     * events * deviation: how much "interesting" 
     * activity there has been over the period, measuring
     * the number of times the observed value changed 
     * rather than the volume of the change.
     */
    public double getVolatility() {
        return getDeviation() * getObservations();
    }

    /**
     * natural logarithm of volatility, or 0 if volatility is 0.
     */
    public double getLogVolatility() {
        double v = getVolatility();
        return (v > 0) ? Math.log(v) : 0;
    }

    public String toString() {
        return name + ": " + getTotal() + " (" + FORMAT.format(getMean()) + " * " + getCount() + "; "
            + getLow() + "-" + getHigh() + " ~" + FORMAT.format(getDeviation()) + ")";
    }

    
}
