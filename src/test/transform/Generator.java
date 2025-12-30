/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
package test.transform;

import java.text.SimpleDateFormat;

/*******************************************************************
 * <pre>
 * 1.설명 
 * Generator는 xml로 된 전문 layout을 읽어서 전문 파싱 처리  클래스를 생성해 줍니다.
 * 
 * 2.사용법
 *
 * 3.주의사항
 * </font>
 *
 * @author $Author: cvs $
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 
 * $Log: Generator.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:25  cvs
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
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.2  2008/02/20 00:42:49  오재훈
 * *** empty log message ***
 *
 * Revision 1.1  2007/11/26 08:39:16  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2006/07/04 13:49:18  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2006/06/29 04:01:35  이종원
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class Generator {

    //TODO 각자의 환경에 맞게 바꾸어 줍니다.(Eclipse project home경로)
    private static final String OUTPUT_DIR = "/work/nebsoa";

    private SimpleDateFormat format = new SimpleDateFormat("yyyy. M. d. a h:m:s");

    public Generator() {
    }//end of constructor

    public void generate(String xmlFile) {
    	/*
        Writer writer = null;

        try {
            Velocity.init(OUTPUT_DIR + "/src/test/transform/template/velocity.properties");

            SAXBuilder builder;
            Document root = null;

            try {
                builder = new SAXBuilder();
                InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(xmlFile);
                root = builder.build(is);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }//end try catch

            String totalFieldSize = Integer.toString(((Double)XPath.newInstance("sum(//field/@size) + sum(//tail/@size)").selectSingleNode(root)).intValue());
            String headerSize = Integer.toString(((Double)XPath.newInstance("sum(//field/@size)").selectSingleNode(root)).intValue());
            Boolean tailExist = (Boolean)XPath.newInstance("contains(//tail/@size, '1')").selectSingleNode(root);
            double containMK = Double.parseDouble(XPath.newInstance("count(//field[not(not(string-length(@match-key)))])").selectSingleNode(root).toString());

            VelocityContext context = new VelocityContext();
            context.put("util", new VelocityUtil());
            context.put("root", root);
            context.put("date", format.format(new Date()));
            context.put("totalFieldSize", totalFieldSize);
            context.put("tailExist", tailExist);
            context.put("containMK", (containMK > 0) ? "true" : "false");
            context.put("headerSize", headerSize);

            Template template = Velocity.getTemplate("src/test/transform/template/bean-template.vsl");

            writer = new BufferedWriter(new OutputStreamWriter(System.out));
            template.merge(context, writer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.flush();
                    writer.close();
                } catch (Exception ee) {
                    System.out.println("Exception : " + ee);
                }//end try catch
            }//end if
        }//end try catch finally
        */
    }//end of generate()

    public static void main(String [] args) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            //LogManager.error(e.getMessage(),e);
            //throw e;
        }
        //new Generator().generate("test/transform/config/FXO202.xml");
        
//    	new Generator().generate("test/transform/config/FDI001.xml");
//        new Generator().generate("test/transform/config/account_send_msg_gen.xml");
//        new Generator().generate("test/transform/config/eai_header.xml");
        new Generator().generate("test/transform/config/eai_tandem_header.xml");
//        new Generator().generate("test/transform/config/wang-incomming-header.xml");
//        new Generator().generate("test/transform/config/wang-outgoing-header.xml");
//        new Generator().generate(args[0]);
    }//end of main()

}// end of Generator.java
