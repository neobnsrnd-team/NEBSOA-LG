package nebsoa.plugin.monitor.util;
/*******************************************************************
 * <pre>
 * 1.설명 
 * ExceptionTrace를 String으로 처리하여 ejb tier에서 발생한 trace도 화면까지
 * 전달 해 주는  클래스 
 * 
 * 2.사용법 
 * <font color="red">
 * try{
 *   ~~~
 * } catch (Exception e) {
 *     dataMap.put("ExceptionTrace", ExceptionTracer.getTraceString(e));
 *     //기타 exception 처리 
 *     throw new UserException("909090",ExceptionTracer.getTraceString(e));
 * }
 * 3.주의사항
 * </font>
 *
 * @author $Author: cvs $
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * $Log: ExceptionFormatter.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:29  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:26  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:29  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:52  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:24  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:07  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2006/11/15 09:55:55  이종원
 * 최초작성
 *
 * Revision 1.4  2006/08/19 01:50:11  김성균
 * *** empty log message ***
 *
 * Revision 1.3  2006/07/28 05:49:20  이종원
 * ExceptionTracer 포멧 수정
 *
 * Revision 1.2  2006/07/28 05:38:59  이종원
 * *** empty log message ***
 *
 * Revision 1.1  2006/07/28 05:15:57  이종원
 * 최초작성
 *
 *
 * </pre>
 ******************************************************************/
public class ExceptionFormatter {

    /**
     * trace depth를 10으로 설정
     * @param ex
     * @return
     */
    public static String getTraceString(Throwable ex) {
        return getTraceString(ex, 10);
    }
    
    /**
     * trace string을 생성한다.
     * @param ex
     * @param traceDepth
     * @return
     */
    public static String getTraceString(Throwable ex, int traceDepth) {
        StringBuffer traceStr = new StringBuffer(1024);
        traceStr.append(ex.toString() + " TraceInfo");
        StackTraceElement[] trace = ex.getStackTrace();
        
        int endIndex = traceDepth;

        if (trace.length < traceDepth) {
            endIndex = trace.length;
        }

        for (int i = 0; i < endIndex; i++) {
            traceStr = traceStr.append("\n\tat " + trace[i]);
        }

        return traceStr.toString().trim();
    }
    

    
    static class A{
        public void go() throws Exception{
            new B().come();
        }
    }
    
    static class B{
        public void come() throws Exception{
            int i=0;
            if (i==0) throw new Exception("aaaaa");
        }
    }

}
