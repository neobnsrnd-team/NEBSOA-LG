/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;

import nebsoa.common.startup.StartupContext;

/*******************************************************************
 * <pre>
 * 1.설명 
 * encript, decript string
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
 * $Log: Encrypter.java,v $
 * Revision 1.1  2018/01/15 03:39:49  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:30  cvs
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
 * Revision 1.1  2008/01/22 05:58:18  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:01  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2006/11/09 08:40:08  이종원
 * 암복호화 코드 정리
 *
 * Revision 1.1  2006/10/30 08:56:25  이종원
 * 암복호화  library 및 함수 추가 (spider_home/config/security/nebsoa.key 파일필요)
 *
 * Revision 1.2  2006/06/17 10:37:45  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class Encrypter{
    /**
     * 파일암호화에 쓰이는 버퍼 크기 지정
     */
    public static final int kBufferSize = 8192;
    public static Key key = null;
    public static final String keyFile = "nebsoa.key";
    public static final String keyFileDir = 
        StartupContext.SPIDER_HOME+"/config/security";


    /**
     * 지정된 비밀키를 가지고 오는 메서드
     * @return  Key 비밀키 클래스
     * @exception Exception
     */
    private static Key getKey() throws Exception{
        if(key != null){
            return key;
        }else{
            return loadKey();
        }
    }
    static Object dummy = new Object();
    private static Key loadKey() throws Exception{
        if(key == null){
            synchronized (dummy) {
                String encClass=PropertyManager.getProperty("default","EncrypterKeyClassName"
                        ,"nebsoa.common.util.DefaultEncrypterKey");
                EncrypterKey encKey = (EncrypterKey) Class.forName(encClass).newInstance();
                System.out.println("EncrypterKeyClass::"+encKey.getClass().getName());
                key = encKey.getKey();
            }
        }
        return key;
    }
 
    /**
     * 문자열 대칭 암호화
     * @param   str  비밀키 암호화를 희망하는 문자열
     * @return  String  암호화된 ID
     * @exception Exception
     */
    public static String encrypt(String str)
    throws Exception{
        if ( str == null || str.length() == 0 ) return "";
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(javax.crypto.Cipher.ENCRYPT_MODE,getKey());
        String amalgam = str;
  
        byte[] inputBytes1 = amalgam.getBytes("UTF8");
        byte[] outputBytes1 = cipher.doFinal(inputBytes1);
        String outputStr1 = java.util.Base64.getEncoder().encodeToString(outputBytes1);
        return outputStr1;
    }
 
    /**
     * 문자열 대칭 복호화
     * @param   codedID  비밀키 복호화를 희망하는 문자열
     * @return  String  복호화된 ID
     * @exception Exception
     */
    public static String decrypt(String codedID)
    throws Exception{
        if ( codedID == null || codedID.length() == 0 ) return "";
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(javax.crypto.Cipher.DECRYPT_MODE, getKey());

        byte[] inputBytes1 = java.util.Base64.getDecoder().decode(codedID);
        byte[] outputBytes2 = cipher.doFinal(inputBytes1);
  
        String strResult = new String(outputBytes2,"UTF8");
        return strResult;
    }
 
    /**
     * 파일 대칭 암호화
     * @param   infile 암호화을 희망하는 파일명
     * @param   outfile 암호화된 파일명
     * @exception Exception
     */
    public static void encryptFile(String infile, String outfile)throws Exception {
        File inFile = new File(infile);
        if(!inFile.exists()){
            throw new FileNotFoundException(infile);
        }

        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(javax.crypto.Cipher.ENCRYPT_MODE,getKey());

        FileInputStream in = null;
        FileOutputStream fileOut =null;
        CipherOutputStream out = null;

        try{
            in = new FileInputStream(infile);
            fileOut = new FileOutputStream(outfile);
            out = new CipherOutputStream(fileOut, cipher);

            byte[] buffer = new byte[kBufferSize];
            int length;
            while((length = in.read(buffer)) != -1){
                out.write(buffer,0,length);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
            if(out != null){
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
    }
    /**
     * 파일 대칭 복호화
     * @param   infile 복호화을 희망하는 파일명
     * @param   outfile 복호화된 파일명
     * @throws Exception 
     * @exception Exception
     */
    public static void decryptFile(String infile, String outfile) throws Exception
    {
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE,getKey());
 
        FileInputStream in=null;
        FileOutputStream fileOut;
        CipherOutputStream out =null;
        try {
            in = new FileInputStream(infile);
            fileOut = new FileOutputStream(outfile);
            out = new CipherOutputStream(fileOut, cipher);
            byte[] buffer = new byte[kBufferSize];
            int length;
            while((length = in.read(buffer)) != -1){
                out.write(buffer,0,length);
            }
        } catch (Exception e) {
            throw e;
        }finally{
            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
            if(out != null){
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
    }
    
    public static void main(String[] ars) 
    throws Exception {

        String result=Encrypter.encrypt("이종원");
        System.out.println(result);
        System.out.println(Encrypter.decrypt(result));
 
    }
    
    public static class KeyGen{
        /**
         * 비밀키 생성메소드.
         * key 파일이 새로 생성 되면 기존 데이터를 복호화 할 수 없으므로 함부로 생성하지않는다.
         * @return  void
         * @exception java.io.IOException,java.security.NoSuchAlgorithmException
         */
        static File makekey() throws Exception{

            File keySaveFile = new File(keyFileDir,keyFile);
            KeyGenerator generator = KeyGenerator.getInstance("DES");
            generator.init(new SecureRandom());
            Key key = generator.generateKey();
            String encKey=StringUtil.encodeObject(key);
            
            FileOutputStream out = null;
            try{
                System.out.println("write key ----------");
                out = new FileOutputStream(keySaveFile);
                out.write(encKey.getBytes());
                System.out.println("write key ----ok....");
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                if(out != null){  
                    try{
                        out.close();
                    }catch(Exception e ){}
                }
            }
            return keySaveFile;
        } 
    }
    /**
     * encrypter 구현할 때 상속받아 구현할 메소드 정의한 interface
     * @author 이종원
     *
     */
    public interface EncrypterKey{
        public Key getKey() throws Exception;
    }

}


