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
 * sync 타입을 나타내는 type-safed-enumeration 클래스
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
 * $Log: SyncType.java,v $
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
 * Revision 1.6  2006/10/02 14:46:42  이종원
 * util method 추가
 *
 * Revision 1.5  2006/10/02 14:44:26  이종원
 * *** empty log message ***
 *
 * Revision 1.4  2006/08/18 08:28:40  안경아
 * *** empty log message ***
 *
 * Revision 1.3  2006/08/01 14:06:43  이종원
 * *** empty log message ***
 *
 * Revision 1.2  2006/07/28 12:48:46  이종원
 * NO_ACK추가
 *
 * Revision 1.1  2006/06/17 10:11:12  김승희
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public class SyncType implements Serializable {
	
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -4287832836001354431L;
	
	public static final String SYNC_TYPE = "SYNC";
	public static final String ASYNC_TYPE = "ASYNC";
    public static final String NOACK_TYPE = "NO_ACK";
	
	private String type;
	
	public SyncType(String type) {
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
		buffer.append(this.getClass().getName()).append("@{");
		buffer.append("type:[").append(this.type).append("]");
		buffer.append("}");
		return buffer.toString();
	}//end of toString()
	
	public static final SyncType SYNC = new SyncType(SYNC_TYPE);
	public static final SyncType ASYNC = new SyncType(ASYNC_TYPE);
    public static final SyncType NOACK = new SyncType(NOACK_TYPE);
	
	/**
	 * 주어진 문자열 타입에 해당하는 SyncType 객체를 리턴합니다.
	 * 
	 * 주어진 문자열 타입을 지원하지 않는다면, 기본으로 PLAIN_TEXT 를 리턴합니다.
	 * 
	 * @param type 문자열 형태의 Adapter-type
	 * @return SyncType 객체
	 */
	public static SyncType getType(String type) {
		if (SYNC.getType().equals(type)) {
			return SYNC;
		} else if (ASYNC.getType().equals(type)) {
			return ASYNC;
        } else if (NOACK.getType().equals(type)) {
            return NOACK;            
		} else {
			throw new IllegalArgumentException("지원되지 않는 Sync Type 입니다.[" + type + "]");
		}//end of if else
	}//end of getType()
    
    public static void validate(String type){
        getType(type);
    }
    
    public static boolean isSync(String type){
        validate(type);
        return SYNC_TYPE.equals(type);
    }
    
    public static  boolean isAsync(String type){
        validate(type);
        return ASYNC_TYPE.equals(type);
    }
    
    public static boolean isNoAck(String type){
        validate(type);
        return NOACK_TYPE.equals(type);
    }

}// end of SyncType.java