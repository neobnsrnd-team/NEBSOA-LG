/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import nebsoa.common.Constants;
import nebsoa.common.log.LogManager;


/*******************************************************************
 * <pre>
 * 1.설명 
 * 쉘 이나 배치 프로그램, exe를 실행시키는 클래스 
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
 * $Log: ShellCommander.java,v $
 * Revision 1.1  2018/01/15 03:39:49  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:32  cvs
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
 * Revision 1.1  2007/11/26 08:38:03  안경아
 * *** empty log message ***
 *
 * Revision 1.5  2007/03/07 02:53:45  김성균
 * 일부 로그 INFO 레벨로 변경
 *
 * Revision 1.4  2006/07/11 09:08:21  김성균
 * *** empty log message ***
 *
 * Revision 1.3  2006/07/07 13:34:45  이종원
 * *** empty log message ***
 *
 * Revision 1.2  2006/06/17 10:37:45  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class ShellCommander {

    /**
     * Comment for <code>LOG_TAIL_SIZE</code>
     */
    static String LOG_TAIL_SIZE = "-20";

    static {
        try {
            LOG_TAIL_SIZE = "-"
                    + PropertyManager.getProperty("default", "LOG_TAIL_SIZE","30")
                            .trim();
        } catch (Exception e) {
        }
    }

    //compile java source
    public final static String[] WINDOW_ANT_COMPILE_COMMAND 
    	= {"ant.bat","-buildfile",Constants.APP_HOME_DIR+"/build.xml","compile" };
    //compile java source
    public final static String[] UNIX_ANT_COMPILE_COMMAND 
    = {"ant","-buildfile",Constants.APP_HOME_DIR+"/build.xml","compile" };


    /**
     * 2004. 10. 5. 이종원 작성
     * @param cmds
     * @return
     * 설명:인자가 하나인 쉘을 실행 시킨다. 메세지 캡쳐를 한다.
     */
    public static String exec(String cmds) {
        String[] cmdAndArgs = { cmds };
        return exec(cmdAndArgs,true);
    }
    
    /**
     * 2004. 10. 5. 이종원 작성
     * @param cmds
     * @param wait : 수행이 종료 될 때 까지 기다릴지 여부.
     * @return
     * 설명:인자가로 전달된  쉘을 실행 시킨다. 해당 쉘이 끝날 때 까지 기다리며, 
     * 종료 시킨 후 메세지 캡쳐를 하여 리턴  한다.
     */
    public static String exec(String[] cmds) {
        
        return exec(cmds,true);
    }
    /**
     * 2004. 10. 5. 이종원 작성
     * @param cmds
     * @param wait : 수행이 종료 될 때 까지 기다릴지 여부.
     * @return
     * 설명:인자가 하나인 쉘을 실행 시킨다. 메세지 캡쳐를 한다.
     */
    public static String exec(String cmds,boolean wait) {
        String[] cmdAndArgs = { cmds };
        return exec(cmdAndArgs,wait);
    }

    /**
     * 주어진 명령어를 실행시킵니다.
     * wait가 true일 때는 결과 를 받아내며, 프로세스를 종료 시킵니다.
     * 2004. 10. 5. 이종원 작성
     * @param cmd 쉘 커맨드
     * @param captureMessage 메시지 캡쳐를 할 것인지 여부.
     * @return 실행 결과 메세지
     * 설명: 쉘을 실행 시킵니다.
     */
    public static String exec(String[] cmd ,boolean wait) {
        String cmdStr = "";
        for (int i = 0; i < cmd.length; i++) {
        	if (i != 0) {
        		cmdStr = cmdStr + " " + cmd[i];
        	} else {
        		cmdStr = cmd[i];
        	}
        }

        LogManager.info(cmdStr + "를 실행 합니다 ##################");

        StringBuffer resultMsg = new StringBuffer(1024*10);
        Runtime runtime = Runtime.getRuntime();
        Process targetProcess = null;
        
        BufferedReader inbr=null;
        try {
            targetProcess = runtime.exec(cmd);
            //메시지 캡쳐를 원한경우 라면....
            if(wait){
                inbr = new BufferedReader(new InputStreamReader(
                        targetProcess.getInputStream()));
                String line = null;
                while ((line = inbr.readLine()) != null) {
                    resultMsg.append(line + "\r\n");
                }//end while
                int result=0;
                try {
                    result=targetProcess.waitFor();
                    if(result==0){
                        resultMsg.append("\r\n요청 처리 완료");
                        LogManager.info(cmdStr + "를 정상 완료하였습니다.========");
                    }else{
                        resultMsg.append("\r\n요청 처리 실패:결과코드["+result+"]");
                        LogManager.error(cmdStr + "를 실패하였습니다. 결과코드:["+result+"]");
                    }
                } catch(Exception e) {
                    LogManager.error(e.getMessage());
                }
            }

        } catch (Exception e) {
            LogManager.error(e.getMessage());
            resultMsg.append(e.getMessage());
            
        }finally{
            if(inbr != null){
                try{
                    inbr.close();
                }catch(Exception e){}
            }
            if(wait){
                targetProcess.destroy();
            }
        }
        return resultMsg.toString().trim();
    }
    
 
    
    /**
     * 주어진 명령어를 실행시키고 바로 리턴합니다.
     * 프로세스가 종료 될 때까지 기다리지 않습니다.
     * 
     * 주의) 이 명령어를 계속 실행 시키면 새로운 프로세스가 계속해서 생겨나므로,
     * CPU-Usage 가 100% 로 컴퓨터가 심하게 느려질 수 있습니다.
     * 
     * @param cmd 실행시킬 명령어
     * @author Helexis
     * @since 2005.11.24
     */
    public static String execNoWait(String[] cmd) {
        return exec(cmd,false);
    }//end of execAndForget()
    
    
    public static String compile(){
        if(System.getProperty("os.name").indexOf("Window") > -1){
            return ShellCommander.exec(WINDOW_ANT_COMPILE_COMMAND);
        }else{
            return ShellCommander.exec(UNIX_ANT_COMPILE_COMMAND);
        }
    }

    /**
     * 2004. 10. 5. 이종원 작성
     * @param args
     * @throws Exception
     * 설명:잘 되는지 테스트 해 본다.
     */
    public static void main(String[] args) throws Exception {
        //LogManager.debug(System.getProperties());
//        LogManager.debug(compile());
        
//    	System.out.println(ShellCommander.execAndWait(new String[]{"ipconfig"}));
        
        System.out.println(ShellCommander.exec("ipconfig"));
    	
    	// C:/Program Files/ImageMagick-6.2.5-Q16/convert.exe 변환시킬파일명 -resize 120x120 변환저장할파일명
    	
//    	String command = "\"C:/Program Files/ImageMagick-6.2.5-Q16/convert.exe\" \"C:/Documents and Settings/Administrator/Desktop/spider닷컴/image/10322g01.eps\" -resize 120x120 \"C:/Documents and Settings/Administrator/Desktop/spider닷컴/image/10322g01.gif\"";
    	/**
    	String [] commandArgs = {
    		"C:/Program Files/ImageMagick-6.2.5-Q16/convert.exe",
    		"C:/Documents and Settings/Administrator/Desktop/spider닷컴/image/10322g01.eps",
    		"-resize",
    		"120x120",
    		"C:/Documents and Settings/Administrator/Desktop/spider닷컴/image/10322g01.gif"
    	};
        **/
        
        String [] commandArgs = {
                "explorer",
                "http://yahoo.co.kr"
        };
    	
    	System.out.println(ShellCommander.execNoWait(commandArgs));
    	
    	
//    	command = "\"C:/Program Files/ImageMagick-6.2.5-Q16/convert.exe\" \"C:/Documents and Settings/Administrator/Desktop/spider닷컴/image/102-pc062.tif\" -resize 120x120 \"C:/Documents and Settings/Administrator/Desktop/spider닷컴/image/102-pc062.gif\"";
//    	
//    	String [] commandArgs2 = {
//    		"C:/Program Files/ImageMagick-6.2.5-Q16/convert.exe",
//    		"C:/Documents and Settings/Administrator/Desktop/spider닷컴/image/102-pc062.tif",
//    		"-resize",
//    		"120x120",
//    		"C:/Documents and Settings/Administrator/Desktop/spider닷컴/image/102-pc062.gif"
//    	};
//    	
//    	System.out.println(ShellCommander.execAndWait(commandArgs2));
    }
}