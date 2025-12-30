/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package test.uuid;

/*******************************************************************
 * <pre>
 * 1.설명 
 * Apache-AXIS 에서 사용하는 UUID generator 를 가져옴.
 * 아래는 Apache-AXIS 에 설명되어 있는 내용이다.
 * 
 * A Universally Unique Identifier (UUID) is a 128 bit number generated
 * according to an algorithm that is garanteed to be unique in time and space
 * from all other UUIDs. It consists of an IEEE 802 Internet Address and
 * various time stamps to ensure uniqueness. For a complete specification,
 * see ftp://ietf.org/internet-drafts/draft-leach-uuids-guids-01.txt [leach].
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
 * $Log: UUIDGen.java,v $
 * Revision 1.1  2018/01/15 03:39:50  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:44  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:24  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:27  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:55  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2007/11/26 08:38:55  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2006/11/21 05:50:22  이종원
 * *** empty log message ***
 *
 * Revision 1.2  2006/06/17 10:37:45  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public interface UUIDGen {
	
	/**
	 * UUID 를 리턴합니다.
	 * 
	 * @return 생성된 uuid
	 */
	public String nextUUID();

}// end of UUIDGen.java