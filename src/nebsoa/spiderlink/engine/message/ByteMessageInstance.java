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

import org.apache.commons.collections.MapIterator;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;

import nebsoa.common.log.LogManager;

/*******************************************************************
 * <pre>
 * 1.설명 
 * byte[]형태의 메세지 인스턴스를 나태내는 클래스
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
 * $Log: ByteMessageInstance.java,v $
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
 * Revision 1.2  2008/10/29 01:32:22  김보라
 * 디폴트 생성자 추가
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
 * Revision 1.21  2006/11/23 08:21:41  김승희
 * 메소드 추가
 *
 * Revision 1.20  2006/08/30 05:30:14  김승희
 * 메시지 byte[] 길이 늘이는 메소드 추가
 *
 * Revision 1.19  2006/07/13 03:03:38  김승희
 * 로그 remark 처리에 따른 변경
 *
 * Revision 1.18  2006/07/11 06:21:35  김승희
 * *** empty log message ***
 *
 * Revision 1.17  2006/06/26 10:22:05  김승희
 * 주석 수정
 *
 * Revision 1.16  2006/06/19 11:57:36  김승희
 * MessageInstance --> ByteMessageInstance로 수정
 *
 * Revision 1.15  2006/06/17 10:12:09  김승희
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public class ByteMessageInstance extends MessageInstance {

    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -2740543296201106875L;
	static int serverUid=7777777;
    private byte[] logData;
    
    public ByteMessageInstance() {
    	
    }
    
    public ByteMessageInstance(byte[] _data){
    	data=_data;
        
    }
  
    public ByteMessageInstance(byte[] header, byte[] body){
    	this(ArrayUtils.addAll(header, body));
    }

	public ByteMessageInstance(int totalLength){        

    	byte[] data = new byte[totalLength];
        setData(data);
        
        logData = new byte[totalLength];

    }
    
    public byte[] getBytes(){
        return (byte[]) data;
    }
    
    public void setBytes(byte[] subBytes, int offset, int length){
    	byte[] currBytes = getBytes();
    	System.arraycopy(subBytes, 0, currBytes, offset, length);
    	this.setData(currBytes);
    }
    
    /**
     * 현재 처리중인 위치
     */
    int offset;
    
    public void setOffset(int offsetIndex){
        offset=offsetIndex;
    }
    
    public int getOffset(){        
        return offset;
    }
    
	/* 현재 메시지의 length를 리턴합니다.
	 * @see nebsoa.spiderlink.engine.message.MessageInstance#getCurrentMessageLength()
	 */
	public int getCurrentMessageLength() {
		int parentMessageLength = 0;
		MapIterator iter = messageLengthMap.mapIterator();
		while(iter.hasNext()){
			parentMessageLength+=MapUtils.getIntValue(messageLengthMap, iter.next(), 0);
		}
		
		return (offset - parentMessageLength);
	}
	
    /**
     *  offset의 크기 만큼 data의 배열 크기를 잘라낸다.
     */
    public void trimData(){
    	byte[] byteData = this.getBytes();
    	  	
    	if(offset<byteData.length){
    		byte[] newData = new byte[offset];
    		System.arraycopy(byteData, 0, newData, 0, offset);
    		data = newData;
    		
    		//로그 데이터
    		newData = new byte[offset];
    		System.arraycopy(logData, 0, newData, 0, offset);
    		logData = newData;
    	}
    }
    
    public String toString() {
    	StringBuffer sb = new StringBuffer();
    	sb.append(" ==================== Message ===================== \n");
    	sb.append("시작"+ new String(getBytes())+"끝\n");
    	sb.append("길이 :"+ getBytes().length);
    	sb.append(" \n==================== Message ===================== ");
    	return sb.toString();
    }
    
	/**
	 * 생성된 message bytes와 MessageInstance를 비교해서 부족한 만큼 MessageInstance의 크기를 늘인다 
	 * @param newBytes
	 * @throws IOException
	 */
	public void adjustMessageLength(byte[] newBytes) throws IOException {
				
		byte[] messageInstanceBytes = this.getBytes();
		
		//XML,Map 데이터의 경우 MessageStrucutre를 만들 때 길이를 알 수 없으므로 
		//XML,Map 생성이 끝난 뒤에 길이를 비교해서 모자라는 만큼 MessageInstance의 길이를 늘인다.
		int lengthDiffer = newBytes.length - (messageInstanceBytes.length-offset);
		if(lengthDiffer>0){
			LogManager.debug("MessageInstance의 남은 크기: " + (messageInstanceBytes.length-offset) + " :: MessageInstance offset : " + offset);
			LogManager.debug("생성된 메시지의 크기: " + newBytes.length);
			messageInstanceBytes = ArrayUtils.addAll(messageInstanceBytes, new byte[lengthDiffer]); 
			this.setData(messageInstanceBytes);
			LogManager.debug("증가된 후의 MessageInstance의 크기: " + messageInstanceBytes.length);
			
			//로그 데이터
			//LogManager.debug("로그데이터 크기 :" + logData.length);
			
			logData = ArrayUtils.addAll(logData, new byte[lengthDiffer]); 
			LogManager.debug("증가된 후의 로그데이터 크기 :" + logData.length);
    		
		}
		
	}

	public byte[] getLogData() {
		return logData;
	}

	public void setLogData(byte[] logData) {
		this.logData = logData;
	}
	
	/**
	 * 현재 Message byte[]의 길이를 lenth만큼 늘인다.
	 * @param length
	 */
	public void extendMessageLength(int length){
		byte[] msgBytes = this.getBytes();
		byte[] logBytes = this.getLogData();
		this.data = ArrayUtils.addAll(msgBytes, new byte[length]);
		this.logData = ArrayUtils.addAll(logBytes, new byte[length]);
	}

}
