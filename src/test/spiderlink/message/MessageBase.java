/*******************************************************************
 * 외환은행 CLS 연계 프로젝트
 *
 * Copyright (c) 2003-2004 by COMAS, Inc.
 * All rights reserved.
 * @file : MessageBase.java
 * @author : 이종원
 * @date : 2004. 9. 1.
 * @설명 : 모든 메세지 클래스가 기본적으로 같추어야 할 로직 define
 * <pre>
 * 
 * </pre>
 *******************************************************************
 * $Id: MessageBase.java,v 1.1 2018/01/15 03:39:52 cvs Exp $
 * $Log: MessageBase.java,v $
 * Revision 1.1  2018/01/15 03:39:52  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:11  cvs
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
 * Revision 1.2  2008/02/20 00:42:48  오재훈
 * *** empty log message ***
 *
 * Revision 1.1  2007/11/26 08:37:46  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2006/06/29 04:02:09  이종원
 * *** empty log message ***
 *
 * Revision 1.2  2004/10/15 08:37:14  helexis
 * formatting 이후 재 commit
 *
 * Revision 1.11  2004/09/21 11:03:37  helexis
 * valid() 추상 메소드 추가
 *
 * Revision 1.10  2004/09/14 03:55:55  ljw
 * *** empty log message ***
 *
 * Revision 1.9  2004/09/08 01:31:20  ljw
 * *** empty log message ***
 *
 * Revision 1.8  2004/09/01 13:03:12  helexis
 * getErrorReason() 메소드 시그니처 변경 및 기본구현
 *
 * Revision 1.7  2004/09/01 12:06:11  ljw
 * *** empty log message ***
 *
 * Revision 1.6  2004/09/01 12:03:11  ljw
 * *** empty log message ***
 *
 * Revision 1.5  2004/09/01 11:58:40  ljw
 * *** empty log message ***
 *
 * Revision 1.4  2004/09/01 11:53:56  ljw
 * getHeader
 * getBody추가
 *
 * Revision 1.3  2004/09/01 10:37:45  ljw
 * *** empty log message ***
 *
 * Revision 1.2  2004/09/01 10:12:39  ljw
 * *** empty log message ***
 *
 * Revision 1.1  2004/09/01 10:09:51  ljw
 * MessageBase틀 작성
 *    
 ******************************************************************/
package test.spiderlink.message;

import nebsoa.common.log.LogManager;

/**
 * 2004. 9. 1. 이종원
 * 설명 :
 * <pre>
 * 
 * </pre> 
 */
public abstract class MessageBase implements Serializable {
    /**
     * Unmarshall처리가 되었는지 : byte 데이타를 Unmarshall 하고 나서 true로 세팅 
     */
    protected boolean isUnmarshalled = false;

    /**
     * Header 부분
     */
    protected String header;

    /**
     * body 부분
     */
    protected byte[] data;

    /**
     * 2004. 9. 1. 이종원 작성
     * @return
     * 설명:Message에서 Header를 리턴
     * @throws Exception 
     */
    public String getHeader() throws Exception {
        if (header == null) {
            throw new Exception("Message Header is Null");
        }
        return header;
    }

    /**
     * 2004. 9. 1. 이종원 작성
     * @return
     * 설명:Message에서 body 를 리턴 : data부를 가지고 String 객체 생성
     */
    public String getBody() {
        if (data == null) {
            LogManager.debug("MESSAGE BODY IS NULL....");
            return "";
        }
        return new String(data);
    }

    /**
     * 
     * 2004. 9. 1. 이종원 작성
     * @return
     * 설명:데이터 부를 얻어 옵니다.
     */
    public byte[] getData() {
        if (data == null) {
            LogManager.debug("MESSAGE BODY IS NULL....");
        }
        return data;
    }

    /**
     * 전문 데이터를 세팅합니다.
     *
     * @param data 전문 데이터
     */
    public void setData(byte[] data) {
        this.data = data;
    }//end of setData()

    /**
     * 
     * 2004. 9. 1. 이종원 작성
     * @return
     * 설명: ERROR인지 체크 하는 함수 
     * 전문별로 에러코드가 틀리므로 해당 메시지 클래스에서 구현
     */
    public abstract boolean isError();

    /**
     * 
     * 2004. 9. 1. 이종원 작성
     * @return
     * 설명:에러 코드를 리턴 한다.
     */
    public abstract String getErrorCode();

    /**
     * 
     * 2004. 9. 1. 이종원 작성
     * @return
     * 설명: 에러 원인 설명 메세지를 리턴 한다.
     */
    public String getErrorReason(String prefix, String errorCode) {
        if (prefix.equals("com.keb.stpa.engine.msg.EAIMessage")) {
            return EAIUtil.getErrorReason(errorCode);
        }
        return prefix + "메세지의 에러코드[" + errorCode + "]는 세팅되지 않음";
    }//end of getErrorReason()

    /** 
     * 부모 클래스로 부터 물려 받은 함수 재정의
     * @see com.keb.stpa.engine.msg.Serializable#marshall()
     * 설명 : 내부 데이타를  byte[]로 파싱 
     */
    public abstract byte[] marshall();

    /** 
     * 부모 클래스로 부터 물려 받은 함수 재정의
     * @throws Exception 
     * @see com.keb.stpa.engine.msg.Serializable#unmarshall(byte[])
     * 설명 : 수신한 전문 데이타 byte[]을 내부 데이타로 파싱 
     */
    public abstract void unmarshall(byte[] marshalledData) throws Exception;

    /**
     * 주어진 전문이 valid 한지를 확인합니다.
     * 
     * @throws InvalidMessageException 주어진 전문의 필수 항목의 필드들이 세팅되어 있지 않을 경우 발생하는 RuntimeException
     */
    public abstract void valid();
}