/*
 * Spider Framework
 *
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 *
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.file;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Date;

import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.IOUtils;
import nebsoa.common.util.StringUtil;

/*******************************************************************
 * <pre>
 * 1.설명
 * 파일을 관리하기 위한 클래스
 *  1.디렉토리 생성 기능.
 *    -dot(.)으로 구분 된것도 자동 생성 된다. (예:/a.b.c 는 /a/b/c폴더가 만들어 진다)
 *  2.updateFile메소드를 이용하여 파일을 update 할 수 있다.
 *    -맨끝에 boolean type의 백업 여부를 세팅 할 수 있다.
 *    -물론, 기존 파일이 없으면 새로 생성 된다.
 *    -return 값은 없다.(void)
 *  3.saveFile메소드를 이용하여 파일을 저장 할 수 있다.
 *    -기존 파일이 있으면 저장하고자 하는 파일이름 뒤에[_숫자]붙어 생성된다.
 *    -return 값은 새로 생성된 파일 명이다.
 *
 * 2.사용법
 *  &lt;예제&gt;
 *  	    updateFile(&quot;/temp&quot;,&quot;update.txt&quot;,&quot;123123123123&quot;); //백업 받지 않는다.
 * 	    updateFile(&quot;/temp&quot;,&quot;update2.txt&quot;,&quot;123123123123&quot;,true); //백업 받는다.
 *
 * 	    saveFile(&quot;/temp&quot;,&quot;save.txt&quot;,&quot;123123123123&quot;); //덮어 쓰지 않는다.
 * 	    saveFile(&quot;/temp&quot;,&quot;save_xxx.txt&quot;,&quot;123123123123&quot;,true); //덮어 쓴다
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
 * $Log: FileManager.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:31  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.2  2009/12/31 05:59:44  jglee
 * keb 버젼으로 refactoring
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
 * Revision 1.2  2008/03/19 05:29:10  정미화
 * 2008.03.19  정미화 수정.
 *             	 * jsp->flex로 전환 작업을 하면서 여러건을 한꺼번에 수정,삭제,등록을 할때
 *             	 * 백업 파일명이 중복되어서 에러가 발생하여서 아래와 같이
 *             	 * 백업 파일명이 중복되면 새로운 파일명을 생성하도록 수정함.
 *
 * Revision 1.1  2008/01/22 05:58:26  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:26  안경아
 * *** empty log message ***
 *
 * Revision 1.6  2007/04/24 02:46:21  김성균
 * 경로명이 아래와 같을 경우도 부모디렉토리 포함하여 디렉토리 생성하도록 수정
 * ex) /project/spider/generated/src/patent/app.adminmgt.trxbox
 *
 * Revision 1.5  2006/10/09 08:19:30  이종원
 * *** empty log message ***
 *
 * Revision 1.4  2006/10/09 05:08:26  이종원
 * copy로직 추가
 *
 * Revision 1.3  2006/08/24 06:40:47  이종원
 * NIO를 사용하여 파일 이동 구현
 *
 * Revision 1.2  2006/06/19 04:10:16  안경아
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class FileManager {

    /**
     * 2004. 11. 16. 이종원 작성
     *
     * @param dirStr
     * @return 설명:디렉토리를 생성한다.<br>
     *         -dot(.)으로 구분 된것도 자동 생성 된다. (예:/a.b.c 는 /a/b/c폴더가 만들어 진다)
     */
    public static File mkdir(String dirStr) {

        dirStr = StringUtil.replaceAll(dirStr, File.separator, ".");

        String[] dirs = StringUtil.toArray(dirStr, ".");

        // File dir = new File("/");
        File dir = null;

        for (int i = 0; i < dirs.length; i++) {
            dir = mkdir(dir, dirs[i]);
        }

        return dir;
    }

    /**
     * 2004. 11. 16. 이종원 작성
     *
     * @param dir :
     *            parent dir name
     * @param dirName :
     *            child dir name
     * @return 설명:해당 부모 디렉토리 및에 하위 디렉토리를 생성한다
     */
    public static File mkdir(File dir, String dirName) {
        File f = new File(dir, dirName);
        if (!f.exists()) {
            boolean result = f.mkdirs();
            if (result) {
                return f;
            } else {
                LogManager.error(f + " 디렉토리 생성 실패 ");
                return dir;
            }
        } else {
            return f;
        }
    }

    /**
     * @param dir
     * @param fileName
     * @param data
     *            설명:backup받지 않고 update or 저장
     */
    public static void updateFile(String filePath, String fileName, String data) {
        updateFile(filePath, fileName, data, false);
    }

    /**
     * @param dir
     * @param fileName
     * @param data
     * @param backup
     *            설명:backup설정 여부에 따라 백업 처리 및 update or 저장
     */
    public static void updateFile(String filePath, File file, String data,
            boolean backup) {
        updateFile(filePath, file.getName(), data, backup);
    }

    /**
     * @param dir
     * @param fileName
     * @param data
     * @param backup
     *            설명:backup설정 여부에 따라 백업 처리 및 update or 저장
     */
    public static void updateFile(String filePath, String fileName,
            String data, boolean backup) {
        updateFile(filePath, fileName, data.getBytes(), backup);
    }

    /**
     * @param dir
     * @param fileName
     * @param data
     *            설명:backup받지 않고 update or 저장
     */
    public static void updateFile(String filePath, String fileName, byte[] data) {
        updateFile(filePath, fileName, data, false);
    }

    /**
     * @param dir
     * @param fileName
     * @param data
     * @param backup
     *            설명:backup설정 여부에 따라 백업 처리 및 update or 저장
     */
    public static void updateFile(String filePath, File file, byte[] data,
            boolean backup) {
        updateFile(filePath, file.getName(), data, backup);
    }

    /**
     * @param dir
     * @param fileName
     * @param data
     * @param backup
     *            설명:backup설정 여부에 따라 백업 처리 및 update or 저장
     */
    public static void updateFile(String filePath, String fileName,
            byte[] data, boolean backup) {
        BufferedOutputStream bufWriter = null;
        File tempFile = null;
        File file = new File(filePath, fileName);

        String backupFileName = null;

        if (backup && file.exists()) {
            backupFileName = getBackupFileName(fileName);
            if (!file.renameTo(new File(filePath, backupFileName))) {
                throw new SysException("기존 파일을 백업 받는 데 실패 하였습니다.");
            }
        }

        try {
            tempFile = new File(filePath, fileName);

            bufWriter = new BufferedOutputStream(new FileOutputStream(tempFile));

            bufWriter.write(data);
            bufWriter.flush();
        } catch (Exception e) {
            String recovered = "";
            try {
                File backupFile = new File(filePath, backupFileName);
                backupFile.renameTo(new File(filePath, fileName));
            } catch (Exception ex) {
                LogManager.error("원본 파일 복구 실패:" + ex.getMessage());
                recovered = "원본 파일 복구 실패\n";
            }
            LogManager.error(recovered + fileName + "저장실패\n" + e.getMessage());
            throw new SysException(recovered + fileName + "파일 저장 실패.", e);
        } finally {
            try {
                if (bufWriter != null)
                    bufWriter.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     *
     * 2004. 11. 16. 이종원 작성
     *
     * @param fileName
     * @return 설명:백업 받을 파일 이름을 생성한다. 기본적으로 날짜시간분,초를 확장자 앞에 붇여 준다.
     */
    public static String getBackupFileName(String fileName) {
        int extIdx = fileName.lastIndexOf(".");
        if (extIdx > 0) {
            fileName = fileName.substring(0, extIdx) + "_"
                    + StringUtil.formatDate("yyyyMMddHHmmss", new Date())
                    + fileName.substring(extIdx);
        } else {
            fileName = fileName + "_"
                    + StringUtil.formatDate("yyyyMMddHHmmss", new Date());
        }
        return fileName;
    }

    /**
     * @param dir
     * @param fileName
     * @param data
     * @return 새로 생성된 파일명이 리턴된다. 설명:overwrite하지 않고 신규 저장
     *
     */
    public static String saveFile(String filePath, String fileName, String data) {
        return saveFile(filePath, fileName, data, false);
    }

    /**
     * @param dir
     * @param fileName
     * @param data
     * @param overwrite
     * @return 새로 생성된 파일명이 리턴된다. 설명:overwrite설정 여부에 따라 처리 및 저장
     */
    public static String saveFile(String filePath, File file, String data,
            boolean overwrite) {
        return saveFile(filePath, file.getName(), data, overwrite);
    }

    /**
     * @param dir
     * @param fileName
     * @param data
     * @param overwrite
     * @return 새로 생성된 파일명이 리턴된다. 설명:overwrite설정 여부에 따라 처리 및 저장
     */
    public static String saveFile(String filePath, String fileName,
            String data, boolean overwrite) {
        return saveFile(filePath, fileName, data.getBytes(), overwrite, false);
    }

    /**
     * @param dir
     * @param fileName
     * @param data
     * @return 새로 생성된 파일명이 리턴된다. 설명:default로 overwrite 않고 신규 저장
     */
    public static String saveFile(String filePath, String fileName, byte[] data, boolean overwrite) {
        return saveFile(filePath, fileName, data, overwrite, false);
    }

    /**
     * @param dir
     * @param fileName
     * @param data
     * @return 새로 생성된 파일명이 리턴된다. 설명:default로 overwrite 않고 신규 저장
     */
    public static String saveFile(String filePath, String fileName, byte[] data) {
        return saveFile(filePath, fileName, data, false, false);
    }

    /**
     * @param dir
     * @param fileName
     * @param data
     * @param overwrite
     * @return 새로 생성된 파일명이 리턴된다. 설명:overwrite설정 여부에 따라 처리 및 저장
     */
    public static String saveFile(String filePath, File file, byte[] data,
            boolean overwrite) {
        return saveFile(filePath, file.getName(), data, overwrite, false);
    }

    /**
     *
     * @param dir
     * @param fileName
     * @param data
     * @param overwrite
     * @return 새로 생성된 파일명이 리턴된다. 설명:overwrite설정 여부에 따라 처리 및 저장
     */
    public static String saveFile(String filePath, File file, byte[] data,
            boolean overwrite, boolean isAddData) {
        return saveFile(filePath, file.getName(), data, overwrite, isAddData);
    }


    /**
     * 파일로 저장
     * @param dir
     * @param fileName
     * @param data
     * @param overwrite
     * @param isAddData 기존 데이타에 add할지 여부를 결정(add: true, 신규: false)
     * @return 새로 생성된 파일명이 리턴된다. 설명:overwrite설정 여부에 따라 처리 및 저장
     */
    public static String saveFile(String filePath, String fileName,
            byte[] data, boolean overwrite, boolean isAddData) {
        BufferedOutputStream bufWriter = null;
        File tempFile = null;
        File file = new File(filePath, fileName);

        // updateFile과는 반대이다.
        if (!overwrite && file.exists()) {
            fileName = getNewFileName(filePath, fileName);
        }

        try {
            tempFile = new File(filePath, fileName);

            bufWriter = new BufferedOutputStream(new FileOutputStream(tempFile, isAddData));

            bufWriter.write(data);
            bufWriter.flush();
        } catch (Exception e) {
            LogManager.error(fileName + "저장실패\n" + e.getMessage());
            throw new SysException(fileName + "파일 저장 실패.", e);
        } finally {
            try {
                if (bufWriter != null)
                    bufWriter.close();
            } catch (Exception e) {
            }
        }
        return fileName;
    }

    /**
     *
     * 2004. 11. 16. 이종원 작성
     *
     * @param fileName
     * @return 중복되지 않는 파일명이 리턴된다. 설명:신규 파일 이름을 생성한다. 기본적으로 [_숫자]를 확장자 앞에 붇여 준다.
     */
    public static String getNewFileName(String filePath, String fileName) {
        int extIdx = fileName.lastIndexOf(".");
        boolean exists = true;
        File newFile = null;
        String newFileName = null;
        int i = 1;
        while (exists) {
            if (extIdx > 0) {
                newFileName = fileName.substring(0, extIdx) + "_" + (i++)
                        + fileName.substring(extIdx);
            } else {
                newFileName = fileName + "_" + (i++);
            }

            newFile = new File(filePath, newFileName);
            exists = newFile.exists();
        }
        return newFileName;
    }

	/**
	 * 파일로 부터 byte [] 의 형태로 데이터를 읽어서 리턴한다.
	 *
	 * @param dirName 데이터를 읽어들일 디렉토리 경로
	 * @param fileName 데이터를 읽어들일 파일 명
	 * @return 읽어들인 byte [] 형태의 데이터
	 * @throws IOException 파일로 부터 데이터를 읽는 도중 발생하는 Exception
	 */
	public static byte [] readByteFromFile(String dirName, String fileName) throws IOException {
	    FileInputStream fis = null;
	    try {
	        fis = new FileInputStream(new File(dirName, fileName));
	        return IOUtils.readData(fis);
	    } catch (IOException e) {
	        throw e;
	    } finally {
	    	IOUtils.close(fis);
	    }//end try catch finally
	}//end readByteFromFile()

	/**
	 * 파일로 부터 String 의 형태로 데이터를 읽어서 리턴한다.
	 *
	 * @param dirName 데이터를 읽어들일 디렉토리 경로
	 * @param fileName 데이터를 읽어들일 파일 명
	 * @return 데이터와 데이터의 크기가 세팅된 ReceiveData 객체
	 * @throws IOException 파일로 부터 데이터를 읽는 도중 발생하는 Exception
	 */
	public static String readStringFromFile(String dirName, String fileName) throws IOException {
	    FileReader fr = null;
	    try {
	        fr = new FileReader(new File(dirName, fileName));
	        return IOUtils.readData(fr);
	    } catch (IOException e) {
	        throw e;
	    } finally {
	    	IOUtils.close(fr);
	    }//end try catch finally
	}//end readByteFromFile()



    /**
     * move file To target Dir. 네트웍 드라이버로 연결된 디렉토리 간에는 불가능하다.
     */
    public static String copyFile(String filePath, String fileName,
            FileInputStream in, boolean overwrite) throws Exception {
        LogManager.fwkDebug("=== start save file ===="+filePath+"/"+fileName);

        File file = new File(filePath, fileName);

        // updateFile과는 반대이다.
        if (!overwrite && file.exists()) {

            fileName = getNewFileName(filePath, fileName);
            file = new File(filePath, fileName);
            LogManager.fwkDebug("    저장할 파일이름과 동일한 이름이 있어 변경합니다.->"+fileName);
        }

        file.createNewFile();

        FileChannel fromChannel = null;
        FileChannel toChannel = null;

        try {
            //fromChannel = new FileInputStream(this.file).getChannel();
            fromChannel = in.getChannel();
            toChannel = new FileOutputStream(file).getChannel();
            long fileSize = file.length();
            long transferredSize =fromChannel.transferTo(0, fileSize, toChannel);
            if (fileSize != transferredSize) {
                throw new SysException("FRS00090","Fail to move file");
            }
            return fileName;
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                if (in != null) in.close();
            } catch (Exception e) {}//ignored
            try {
                if (fromChannel != null) fromChannel.close();
            } catch (Exception e) {}//ignored
            try {
                if (toChannel != null) toChannel.close();
            } catch (Exception e) {}//ignored
        }//end try catch finally
    }//end of moveToBankDir()

    public static void main(String[] args) throws Exception {
        updateFile("/temp", "update.txt", "123123123123"); //백업 받지 않는다.
        updateFile("/temp", "update2.txt", "123123123123", true); //백업 받는다.

        saveFile("/temp", "save.txt", "123123123123"); //덮어 쓰지 않는다.
        saveFile("/temp", "save_xxx.txt", "123123123123", true); //덮어 쓴다

        //FileInputStream fin = new FileInputStream("c:\\AUTOEXEC.BAT");
        copyFile("c:\\temp","UPLOAD_TEST.BAT","c:\\temp2",false);
        //copyFile("c:\\temp","UPLOAD_TEST.BAT",fin,true);
    }
    /**
     * copy file To target Dir. overwrite가 true이면 기존 파일이 있을 때 덮어 쓰고 ,
     * false이면 새이름으로 저장한다. 새이름 붙이는 로직은 기존파일_1.확장자 로 처리 된다.
     */
    public static String copyFile(String srcDir, String fileName,
            String targetDir, boolean overwrite) throws Exception {
        LogManager.fwkDebug("=== start copy file ===="+srcDir+"/"+fileName);
        File src = new File(srcDir, fileName);
        if(!src.exists() || src.isDirectory()){
            LogManager.error("file is not exist or the file is directory.."+src);
            return null;
        }

        File targetDirectory = new File(targetDir);
        if(!targetDirectory.exists()){
            throw new SysException("Directory not exist:"+targetDir);
        }
        if(!targetDirectory.canWrite()){
            throw new SysException("the Directory not writable:"+targetDir);
        }

        File file = new File(targetDir, fileName);

        // updateFile과는 반대이다.
        if (!overwrite && file.exists()) {

            fileName = getNewFileName(targetDir, fileName);
            file = new File(targetDir, fileName);
            LogManager.fwkDebug("    저장할 파일이름과 동일한 이름이 있어 변경합니다.->"+fileName);
        }

        file.createNewFile();

        FileInputStream fin=null;
        FileOutputStream fout = null;
        byte[] buf = null;
        long fileSize = src.length();
        if(fileSize > 1024*1024*512){
            buf = new byte[1024*512];
        }else  if(fileSize > 1024*1024){
            buf = new byte[1024*100];
        }else{
            buf = new byte[1024*10];
        }
        int len=-1;
        try {
            fin = new FileInputStream(src);
            fout = new FileOutputStream(file);
            while((len =fin.read(buf))!= -1){
                fout.write(buf,0,len);
            }
            fout.flush();
            return fileName;
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                if (fin != null) fin.close();
            } catch (Exception e) {}//ignored
            try {
                if (fout != null) fout.close();
            } catch (Exception e) {}//ignored
        }//end try catch finally
    }//end ()

    /**
     * copy file To target Dir. 이미 파일이 존재하면 카피로직을 skip합니다..
     */
    public static String copyFileOnlyNotExist(String srcDir, String fileName,
            String targetDir) throws Exception {
        LogManager.fwkDebug("=== start copy file ===="+srcDir+"/"+fileName);
        File src = new File(srcDir, fileName);
        if(!src.exists() || src.isDirectory()){
            LogManager.error("file is not exist or the file is directory.."+src);
            return null;
        }

        File targetDirectory = new File(targetDir);
        if(!targetDirectory.exists()){
            throw new SysException("Directory not exist:"+targetDir);
        }
        if(!targetDirectory.canWrite()){
            throw new SysException("the Directory not writable:"+targetDir);
        }

        File file = new File(targetDir, fileName);

        // updateFile과는 반대이다.
        if (file.exists()) {
            LogManager.fwkDebug("저장할 파일이름과 동일한 이름이 있어 SKIP->"+fileName);
            return fileName;
        }

        file.createNewFile();

        FileInputStream fin=null;
        FileOutputStream fout = null;
        byte[] buf = null;
        long fileSize = src.length();
        if(fileSize > 1024*1024*512){
            buf = new byte[1024*512];
        }else  if(fileSize > 1024*1024){
            buf = new byte[1024*100];
        }else{
            buf = new byte[1024*10];
        }
        int len=-1;
        try {
            fin = new FileInputStream(src);
            fout = new FileOutputStream(file);
            while((len =fin.read(buf))!= -1){
                fout.write(buf,0,len);
            }
            fout.flush();
            return fileName;
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                if (fin != null) fin.close();
            } catch (Exception e) {}//ignored
            try {
                if (fout != null) fout.close();
            } catch (Exception e) {}//ignored
        }//end try catch finally
    }//end


    /**
     * 해당 경로의 파일 삭제
     *
     * @param filePath
     * @param fileName
     * @throws Exception
     */
    public static boolean deleteFile(String filePath, String fileName) throws Exception {
    	LogManager.fwkDebug("=== start delete file ===="+ filePath +"/"+ fileName);

    	File src = new File(filePath, fileName);
        if(!src.exists() || src.isDirectory()){
            LogManager.error("file is not exist or the file is directory..["+src+"]");
            return false;
        }

        return src.delete();

    }
}
