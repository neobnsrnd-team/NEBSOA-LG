/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.monitor.wasmonitor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import nebsoa.common.startup.StartupContext;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 실행되고 있는 WAS의 설정 정보를 가져오는 클래스
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
 * $Log: Config.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:32  cvs
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
 * Revision 1.1  2008/08/04 08:54:52  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:27  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:37:59  안경아
 * *** empty log message ***
 *
 * Revision 1.5  2007/07/17 05:05:00  이종원
 * *** empty log message ***
 *
 * Revision 1.4  2006/11/21 07:53:08  이종원
 * *** empty log message ***
 *
 * Revision 1.3  2006/06/19 11:52:00  이종원
 * source코드 정리
 *
 * Revision 1.2  2006/06/17 10:46:08  오재훈
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class Config
{
    static String logDir=StartupContext.SPIDER_HOME+"/";

    public Config()
    {
        configProp = new Properties();
    }

    public void loadConfigs()
    {
        try
        {
            System.out.println("Was Monitor Config: loadConfigs()...");
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("ejbmonitor.properties");
            configProp.load(inputStream);
            configProp.list(System.out);
            inputStream.close();
            ejbServer = configProp.getProperty("ejb_server", "t3://localhost:7001");
            ejbUser = configProp.getProperty("ejb_user", "weblogic");
            ejbPass = configProp.getProperty("ejb_pass", "weblogic");
            ejbCtxFactory = configProp.getProperty("ejb_factory", "weblogic.jndi.WLInitialContextFactory");
            ejbDomain = configProp.getProperty("ejb_domain", "mydomain");
            threads = configProp.getProperty("no_threads", "10");
            interval = configProp.getProperty("refresh_interval", "60");
            auto = configProp.getProperty("auto_refresh", "true");
            testCases = configProp.getProperty("testcases", "");
            logFile = configProp.getProperty("logfile", logDir+"ejbmonitor.log");
        }
        catch(FileNotFoundException fe)
        {
            fe.printStackTrace();
            loadDefaults();
        }
        catch(IOException ie)
        {
            ie.printStackTrace();
            loadDefaults();
        }
        try
        {
            noThreads = Integer.parseInt(threads);
        }
        catch(NumberFormatException nfe)
        {
            noThreads = 1;
        }
        try
        {
            rfhInterval = Integer.parseInt(interval);
        }
        catch(NumberFormatException nfe1)
        {
            rfhInterval = 60;
        }
        autoRefresh = false;//Boolean.valueOf(auto).booleanValue();
    }

    private void loadDefaults()
    {
        //System.out.println("load default config...");
        //JOptionPane.showMessageDialog(frame, "Could not locate configuration file. Loading Default Properties...");
        ejbServer = "t3://localhost:8001";
        ejbUser = "weblogic";
        ejbPass = "weblogic";
        ejbCtxFactory = "weblogic.jndi.WLInitialContextFactory";
        ejbDomain = "mydomain";
        threads = "10";
        interval = "60";
        auto = "false";
        testCases = "";
        logFile =  logDir+"ejbmonitor.log";
    }


    public String toString(){
        StringBuffer buf = new StringBuffer();
        buf.append("\n ejbServer:"+ejbServer);
        buf.append("\n ejbUser:"+ejbUser);
        buf.append("\n ejbPass:"+ejbPass);
        buf.append("\n ejbCtxFactory:"+ejbCtxFactory);
        buf.append("\n ejbDomain:"+ejbDomain);
        buf.append("\n logFile:"+logFile);

        return buf.toString();
    }

    private Properties configProp;
    private String threads;
    private String interval;
    private String auto;
    public static String ejbServer;
    public static String ejbUser;
    public static String ejbPass;
    public String ejbCtxFactory;
    public static String ejbDomain;
    public static int noThreads;
    public int rfhInterval;
    public boolean autoRefresh;
    public static String testCases;
    public static String logFile;
}
