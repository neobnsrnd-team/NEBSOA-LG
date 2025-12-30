/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.connector.util;

import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;

/*******************************************************************
 * <pre>
 * 1.설명 
 * Fixed Length형태의 헤더를 가진 전문일 경우 length와 offset정보를 
 * 기준으로 하여 헤더 정보와 body를 추출하는 클래스
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
 * $Log: DefaultHeaderParser.java,v $
 * Revision 1.1  2018/01/15 03:39:52  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:10  cvs
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
 * Revision 1.1  2008/08/04 08:54:54  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:31  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:39:09  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2006/10/10 07:12:32  이종원
 * validate method추가
 *
 * Revision 1.1  2006/10/03 09:12:44  이종원
 * refactoring
 *
 * Revision 1.2  2006/08/01 14:00:01  이종원
 * code정리
 *
 * Revision 1.1  2006/08/01 10:01:11  이종원
 * *** empty log message ***
 *
 * Revision 1.2  2006/06/17 08:38:33  이종원
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public class DefaultHeaderParser implements HeaderParser {
	
	transient private byte [] data;

    protected int msgIdOffset;
    protected int msgIdLength;
    protected int msgUIDOffset;
    protected int msgUIDLength;
    protected int msgGroupIdOffset;
    protected int msgGroupIdLength;
    protected int msgBodyOffset;
    
    /**
     * 전문 그룹이 관리될 필요가  있는 경우 
     * 전문 id, 전문 고유번호,전문 그룹id, body부 시작 점을 인자로 전달하여 객체 생성.
     * @param msgIdOffset
     * @param msgIdLength
     * @param msgUIDOffset
     * @param msgUIDLength
     * @param msgGroupIdOffset
     * @param msgGroupIdLength
     * @param msgBodyOffset
     */
	public DefaultHeaderParser(int msgIdOffset,int msgIdLength
            ,int msgUIDOffset,int msgUIDLength
            ,int msgGroupIdOffset,int msgGroupIdLength
            ,int msgBodyOffset) {
        this.msgIdOffset=msgIdOffset;
        this.msgIdLength=msgIdLength;
        this.msgUIDOffset=msgUIDOffset;
        this.msgUIDLength=msgUIDLength;
        this.msgGroupIdOffset=msgGroupIdOffset;
        this.msgGroupIdLength=msgGroupIdLength;
        this.msgBodyOffset=msgBodyOffset;
        
	}//end of constructor
    /**
     * 전문 그룹이 관리될 필요가 없는경우 
     * 전문 id, 전문 고유번호, body부 시작 점을 인자로 전달하여 객체 생성.
     * @param msgIdOffset
     * @param msgIdLength
     * @param msgUIDOffset
     * @param msgUIDLength
     * @param msgBodyOffset
     */
    public DefaultHeaderParser(int msgIdOffset,int msgIdLength
            ,int msgUIDOffset,int msgUIDLength            
            ,int msgBodyOffset) {
        this(msgIdOffset,msgIdLength,
                msgUIDOffset,msgUIDLength,0,0,msgBodyOffset);
        
    }//end of constructor
	
	public String getMsgId() {
        validate(data,getMsgIdOffset(),getMsgIdLength());
		byte [] msgId = new byte[getMsgIdLength()];
		System.arraycopy(this.data, getMsgIdOffset(), msgId, 0, getMsgIdLength());
		return new String(msgId);
	}//end of getMsgId()
    
    public String getMessageUID() { 
        validate(data,getMsgUIDOffset(),getMsgUIDLength());
        //LogManager.debug(new String(getData())+"\n\t==>getMsgUIDOffset():"+getMsgUIDOffset()+",getMsgUIDLength():"+getMsgUIDLength());
        byte [] msgUID = new byte[getMsgUIDLength()];
        System.arraycopy(this.data, getMsgUIDOffset(), msgUID, 0, getMsgUIDLength());
        return new String(msgUID);
    }//end of getMsgId()
	/**
     * 전문 그룹 id가 없는 경우 null 리턴
     * 없는 경우에 대한 설정 
     * MsgGroupIdLength()==0 ||getMsgGroupIdOffset()<=0 하면 된다. 
     * 2006. 7. 28.  이종원 작성
     * @return
	 */
	public String getMsgGroupId() {
        
        if(getMsgGroupIdLength()==0 ||getMsgGroupIdOffset()<=0 ) return null;
        
        validate(data,getMsgGroupIdOffset(),getMsgGroupIdLength());

		byte [] msgGroupId = new byte[getMsgGroupIdLength()];
		System.arraycopy(this.data, getMsgGroupIdOffset(), msgGroupId, 0, getMsgGroupIdLength());
		return new String(msgGroupId);
	}//end of getMsgGroupId()
	
    /**
     * 파싱 하려는 데이터의 길이가 offset보다 작지는 않은지 validation을 수행합니다.
     * 2006. 10. 10.  이종원 작성
     * @param data2
     * @param msgGroupIdOffset2
     * @param msgGroupIdLength2
     */
	private void validate(byte[] data, int offset, int length) {
        if(data == null){
            LogManager.error(this.getClass().getName()
                    +"=> messageData is null.cann't getMessageGroupId()");
            throw new SysException("data is null");
        }
        
        if(data.length < offset+length){
            throw new SysException(
                    "data length is shorter than Offset+length\n(data length:"
                    +data.length+",Offset:"+offset+",length:"+length+")");
        }
        
    }
    /**
	 * data 에 값을 세팅합니다.
	 * 
	 * @param data data 에 값을 세팅하기 위한 인자 값
	 */
	public void setData(byte[] data) {
		this.data = data;
	}

    public int getMsgBodyOffset() {
        return msgBodyOffset;
    }

    public void setMsgBodyOffset(int msgBodyOffset) {
        this.msgBodyOffset = msgBodyOffset;
    }

    public int getMsgGroupIdLength() {
        return msgGroupIdLength;
    }

    public void setMsgGroupIdLength(int msgGroupIdLength) {
        this.msgGroupIdLength = msgGroupIdLength;
    }

    public int getMsgGroupIdOffset() {
        return msgGroupIdOffset;
    }

    public void setMsgGroupIdOffset(int msgGroupIdOffset) {
        this.msgGroupIdOffset = msgGroupIdOffset;
    }

    public int getMsgIdLength() {
        return msgIdLength;
    }

    public void setMsgIdLength(int msgIdLength) {
        this.msgIdLength = msgIdLength;
    }

    public int getMsgIdOffset() {
        return msgIdOffset;
    }

    public void setMsgIdOffset(int msgIdOffset) {
        this.msgIdOffset = msgIdOffset;
    }

    public byte[] getData() {
        return data;
    }
    
    public byte[] getBody() {
        int bodyLength=getData().length-getMsgBodyOffset();
        byte [] body = new byte[bodyLength];
        System.arraycopy(this.data, getMsgBodyOffset(), body, 0, bodyLength);
        return body;
    }
    
    public byte[] getHeader() {
        byte [] header = new byte[getMsgBodyOffset()];
        System.arraycopy(this.data, 0, header, 0, getMsgBodyOffset());
        return header;
    }
    
    public Object clone() throws CloneNotSupportedException{
        return super.clone();
    }


    public HeaderParser cloneHeaderParser() throws CloneNotSupportedException {
        return (HeaderParser) super.clone();
    }

    public int getMsgUIDLength() {
        return msgUIDLength;
    }

    public void setMsgUIDLength(int msgUIDLength) {
        this.msgUIDLength = msgUIDLength;
    }

    public int getMsgUIDOffset() {
        return msgUIDOffset;
    }

    public void setMsgUIDOffset(int msgUIDOffset) {
        this.msgUIDOffset = msgUIDOffset;
    }

}// end of DefaultHeaderDataExtractor.java