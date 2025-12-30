/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
 
package test.refactoring;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import nebsoa.common.util.StringUtil;

 /*******************************************************************
 * <pre>
 * 1.설명 
 * 자동으로 리팩토링 해 주는 클래스
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
 * $Log: SQLRefactoringProcessor.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:35  cvs
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
 * Revision 1.1  2008/08/04 08:54:53  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.2  2008/02/20 00:42:48  오재훈
 * *** empty log message ***
 *
 * Revision 1.1  2007/11/26 08:38:34  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2007/01/22 20:39:43  이종원
 * *** empty log message ***
 *
 * Revision 1.1  2007/01/22 13:32:28  이종원
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/


public class SQLRefactoringProcessor {

    public static void main(String[] args){
        File src = new File("/refactoring/src");
        File target= new File ("/refactoring/target");
        updateDir(src,target);
    }
    
    
    public static void updateDir(File src, File target){
        File[] files = src.listFiles();
        if(files==null) return;
        if(!target.exists()) {
            target.mkdir();
            System.out.println("디렉토리 생성ok:"+target);
        }
        for(int i=0;i<files.length;i++){
            if(files[i].isDirectory()){
                if(files[i].getName().equals("CVS")) continue;
                updateDir(files[i],new File(target,files[i].getName()) );
            }else{
                //java file만...
                if(files[i].getName().endsWith("Sql.xml")){
                    updateFile(src,files[i].getName(),target,null,null);
                }
            }
        }
    }
    
    public static void updateFile(File src, String file, File target, 
            String from, String to){
        BufferedReader in = null;
        BufferedWriter out = null;
        try{
            System.out.println(src+", file:"+file+", target:"+target);
            in = new BufferedReader(
                    new FileReader(new File(src,file)));
            String newFileName=file;
            if(file.endsWith("A.java")){
                newFileName=StringUtil.replace(file,"A.java","Action.java");
            }
            out = new BufferedWriter(
                    new FileWriter(new File(target,newFileName)));
            String line=null;
            while((line = in.readLine())!= null){
                if(line.indexOf("keb.")> -1){
                    line = StringUtil.replaceAll(line,"keb.","nebsoa.");
                }
                if(line.indexOf("KEB")> -1){
                    line = StringUtil.replaceAll(line,"KEB","Serverside");
                }
                if(from != null){
                    if(line.indexOf(from)> -1){
                        line = StringUtil.replaceAll(line,from, to);
                    }
                }
                out.write(line);
                out.write("\r\n");
            }
            out.flush();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if(in != null)
                try {
                    in.close();
                } catch (IOException e) {
                }
            if(out != null)
                try {
                    out.close();
                } catch (IOException e) {
                }
        }
    }
}