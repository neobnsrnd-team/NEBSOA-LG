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
import java.security.Key;

import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.Encrypter.EncrypterKey;

/*******************************************************************
 * <pre>
 * 1.설명 
 * spider framework에서 default로제공하는 file기반의 encrypter key 
 * 
 * 2.사용법
 * 
 * <font color="red">
 * 3.주의사항
 *   이 key를 가지고 데이타를 암호화 한  이후에는 key를  수정하면 복호화가 안된다.
 * </font>
 *
 * @author 이종원
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 
 * $Log: FileEncrypterKey.java,v $
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
 * Revision 1.1  2006/11/09 08:40:08  이종원
 * 암복호화 코드 정리
 *
 * Revision 1.1  2006/11/09 08:33:52  이종원
 * 최초작성
 *
 * </pre>
 ******************************************************************/
public class FileEncrypterKey implements EncrypterKey{

    String keyFileDir=Encrypter.keyFileDir;
    String keyFile=Encrypter.keyFile;
    
    public FileEncrypterKey(){
        
    }
    
    public Key getKey() throws Exception{
        Key key = null;
        File file = new File(keyFileDir,keyFile);
        if(!file.exists()){
            throw new SysException("암복호화 key정보를 담고 있는 파일을 찾을 수 없습니다:"+file);
        }
        if(file.exists()){
            FileInputStream fin=null;      
            try{
                byte[] buf = new byte[1024];
                fin = new FileInputStream(file);
                int len = fin.read(buf);
                String encKey = new String(buf,0,len);
                key = (Key)StringUtil.decodeObject(encKey); 
                return key;
            }catch(Exception e){
                LogManager.error("spider 암복호화 모듈울 로딩하지 못함.",e);
                throw e;
            }finally{
                if(fin != null){
                    try{
                        fin.close();
                    }catch(Exception e){}
                }
            }
        }else{
            throw new Exception("암호키객체를 생성할 수 없습니다.");
        }
        
    }
}
