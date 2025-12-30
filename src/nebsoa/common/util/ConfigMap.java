/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.util;

import java.io.Serializable;
import java.util.Map;


/*******************************************************************
 * <pre>
 * 1.설명 
 * key=value;key=value; 형태로 만들어진 config정보를 쉽게 로딩하고,
 * 사용하기 위해 만들어진 유틸리티입니다. 
 * 사용법은 HashMap과 동일하며, 추가적인 편리한 getXXXX메소드를 가지고 있음.
 * DataMap과 유사함.
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
 * $Log: ConfigMap.java,v $
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
 * Revision 1.1  2007/11/26 08:38:02  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2006/10/02 16:47:26  이종원
 * *** empty log message ***
 *
 * Revision 1.1  2006/10/02 16:47:04  이종원
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class ConfigMap extends DataMap implements Serializable {
    
	public ConfigMap() {
	}
    
    /**
     * Map을 인자로 해서 DataMap 생성
     * @param map
     */
    public ConfigMap(Map map) {
        super(map);
    }

}


