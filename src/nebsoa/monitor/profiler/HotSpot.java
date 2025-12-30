/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.monitor.profiler;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 
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
 * $Log: HotSpot.java,v $
 * Revision 1.1  2018/01/15 03:39:52  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:08  cvs
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
public class HotSpot implements Comparable {

    final String name;

    int total;

    int count;

    HotSpot(String name, int total, int count) {
        this.name = name;
        this.total = total;
        this.count = count;
    }

    public String getName() { 
        return name;
    }

    public int getTotal() {
        return total;
    } 

    public int getCount() {
        return count;
    }

    public int compareTo(Object o) {
        if (o == null)
            return 1;
        if (o == this)
            return 0;

        HotSpot ho = (HotSpot) o;
        int ret = ho.total - total;
        if (ret != 0)
            return ret;
        ret = ho.count - count;
        if (ret != 0)
            return ret;
        return name.compareTo(ho.name);
    }

    public boolean equals(Object o) {
        return compareTo(o) == 0;
    }

    public int hashCode() {
        return name.hashCode();
    }

}

