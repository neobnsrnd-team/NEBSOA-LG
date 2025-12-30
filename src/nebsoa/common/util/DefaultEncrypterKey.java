/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.util;

import java.security.Key;

import nebsoa.common.util.Encrypter.EncrypterKey;

/*******************************************************************
 * <pre>
 * 1.설명 
 * spider framework에서 default로제공하는 encrypter key 
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
 * $Log: DefaultEncrypterKey.java,v $
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
 * Revision 1.1  2007/11/26 08:38:01  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2006/11/09 08:40:08  이종원
 * 암복호화 코드 정리
 *
 * Revision 1.1  2006/11/09 08:33:52  이종원
 * 최초작성
 *
 * </pre>
 ******************************************************************/
public class DefaultEncrypterKey implements EncrypterKey{
    
    public Key getKey() throws Exception{
        return (Key) StringUtil.decodeObject(
                "rO0ABXNyAB5jb20uc3VuLmNyeXB0by5wcm92aWRlci5ERVNLZXlrNJw12hVomAIAAVsAA2tleXQAAltCeHB1cgACW0Ks8xf4BghU4AIAAHhwAAAACP3744PIDomo");
    }
}
