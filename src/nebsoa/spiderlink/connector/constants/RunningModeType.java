/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.connector.constants;

import java.io.Serializable;

/*******************************************************************
 * <pre>
 * 1.설명 
 * RunningModeType을 나타내는 클래스
 * 개발 D , 운영 : R, Test:T
 * 2.사용법
 * static 상수로 사용
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author 이종원
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 
 * $Log: RunningModeType.java,v $
 * Revision 1.1  2018/01/15 03:39:52  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:09  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:25  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:28  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:51  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:19  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:25  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2006/10/03 09:12:44  이종원
 * refactoring
 *
 * Revision 1.1  2006/09/16 04:32:52  이종원
 * 이동
 *
 * Revision 1.1  2006/09/15 07:44:28  이종원
 * 최초작성
 *
 *
 * </pre>
 ******************************************************************/
public class RunningModeType implements Serializable {
	
	public static final String REAL_TYPE = "R";
	public static final String DEV_TYPE = "D";
    public static final String TEST_TYPE = "T";
	
	private String type;
	
	public RunningModeType(String type) {
		this.type = type;
	}//end of constructor
	
	/**
	 * type 의 값을 리턴합니다.
	 * 
	 * @return type 의 값
	 */
	public String getType() {
		return type;
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("운영환경모드:"+this.getClass().getName()).append("@{");
		buffer.append("type:[").append(this.type).append("]");
		buffer.append("}");
		return buffer.toString();
	}//end of toString()
	
	public static final RunningModeType REAL = new RunningModeType(REAL_TYPE);
	public static final RunningModeType DEV = new RunningModeType(DEV_TYPE);
    public static final RunningModeType TEST = new RunningModeType(TEST_TYPE);
	
	/**
	 * 주어진 문자열 타입에 해당하는 SyncType 객체를 리턴합니다.
	 * 
	 * 주어진 문자열 타입을 지원하지 않는다면, 기본으로 PLAIN_TEXT 를 리턴합니다.
	 * 
	 * @param type 문자열 형태의 Adapter-type
	 * @return SyncType 객체
	 */
	public static RunningModeType getType(String type) {
		if (REAL.getType().equals(type)) {
			return REAL;
		} else if (DEV.getType().equals(type)) {
			return DEV;
        } else if (TEST.getType().equals(type)) {
            return TEST;            
		} else {
			throw new IllegalArgumentException(
                    "지원되지 않는RunningModeType 입니다.[" + type + "]");
		}//end of if else
	}//end of getType()

}// end 