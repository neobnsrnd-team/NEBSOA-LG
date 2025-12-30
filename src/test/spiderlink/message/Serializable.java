/*******************************************************************
 * 외환은행 CLS 연계 프로젝트
 *
 * Copyright (c) 2003-2004 by COMAS, Inc.
 * All rights reserved.
 *******************************************************************
 * @(#)Serializable.java	1.0	2004. 7. 13. 오후 6:59:39		조만희
 *
 ******************************************************************/

package test.spiderlink.message;

/**
 * <p>Title : com.keb.stpa.engine.msg.Serializable</p>
 * <p>Created Date & Time : 2004. 7. 13. 오후 6:59:39</p>
 * <p>Description : 객체의 전송을 위한 전문형태로의 직렬와 및 역직렬화 메소드를 포함하는 인터페이스</p>
 * <p>Copyright : (c) 2003-2004 by COMAS, Inc.</p>
 * <p>Company : COMAS, Inc.</p>
 * @author Cho Man-hee (e-mail : helexis@empal.com)
 * @version 1.0
 */
public interface Serializable extends java.io.Serializable {

    /**
     * 객체를 전문의 형태로 직렬화 합니다.
     * 
     * @return byte [] 형태로 직렬화 된 객체
     */
    public byte[] marshall();

    /**
     * 전문의 형태를 객체의 형태로 역직렬화 합니다.
     * 
     * @param marshalledData byte [] 형태로 직렬화 된 객체
     * @throws Exception 
     */
    public void unmarshall(byte[] marshalledData) throws Exception;

}// end of Serializable.java