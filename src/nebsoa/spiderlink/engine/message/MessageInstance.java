/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.engine.message;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.map.HashedMap;

import nebsoa.common.util.UIDGenerator;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 메시지의 객체의 상위 클래스
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
 * $Log: MessageInstance.java,v $
 * Revision 1.1  2018/01/15 03:39:48  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:16  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:50  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:22  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:26  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:51  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:20  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:04  안경아
 * *** empty log message ***
 *
 * Revision 1.9  2006/07/31 09:00:55  김승희
 * getGatewayHeaderUid 메소드 삭제
 *
 * Revision 1.8  2006/06/19 11:57:36  김승희
 * MessageInstance --> ByteMessageInstance로 수정
 *
 * Revision 1.7  2006/06/17 10:12:09  김승희
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public abstract class MessageInstance implements Serializable{
    
	protected Object data;
    protected HashedMap messageLengthMap = new HashedMap(5);
    protected int uid;
    
    public MessageInstance() {

    }
    public MessageInstance(Object data) {
        this.data = data;
    }
    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }
    
    public int getMessageLength(Object messageId){
    	return MapUtils.getIntValue(messageLengthMap, messageId);
    }
    
    public void setMessageLength(Object messageId, int length){
    	messageLengthMap.put(messageId, Integer.valueOf(length));
    }
    
    /**
     * 현재 메시지의 length를 리턴합니다.
     * @return 메시지 length
     */
    public abstract int getCurrentMessageLength();
	
    public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;

	}

	public void writeTo(OutputStream os) throws IOException {

		os.write((byte[])data);
	}
	
	/*public int getGatewayHeaderUid() {
		return uid;
	}*/
	
    
}
