/*******************************************************************
 * 외환은행  연계 프로젝트
 *
 * Copyright (c) 2003-2004 by Serverside, Inc.
 * All rights reserved.
 *******************************************************************
 * $Id: EAIMessage.java,v 1.1 2018/01/15 03:39:52 cvs Exp $
 * 
 * $Log: EAIMessage.java,v $
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
 * Revision 1.2  2008/02/20 00:42:47  오재훈
 * *** empty log message ***
 *
 * Revision 1.1  2007/11/26 08:37:46  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2006/06/29 04:02:09  이종원
 * *** empty log message ***
 *
 *******************************************************************/

package test.spiderlink.message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nebsoa.common.util.FormatUtil;

/**
 * <p>Title : com.keb.stpa.engine.msg.EAIMessage</p>
 * <p>Created Date & Time : 2006. 6. 28. 오후 10:47:49</p>
 * <p>Description : EAI 헤더를 포함한 전문을 나타내는 클래스</p>
 * <p>Copyright : (c) 2003-2004 by COMAS, Inc.</p>
 * <p>Company : COMAS, Inc.</p>
 * @author Jongwon Lee (e-mail : serverside@empal.com)
 * @version 1.0
 */
public class EAIMessage extends MessageBase {    
    
    /** 총전문길이(EAI 채널헤더) [size : 10, layout order number : 1] */
    private long field1;
    
    /** 전문길이(EAI 채널헤더) [size : 10, layout order number : 2] */
    private long field2;
    
    /** sync구분(EAI 채널헤더) [size : 1, layout order number : 3] */
    private String field3;
    
    /** header구분(EAI 채널헤더) [size : 5, layout order number : 4] */
    private String field4;
    
    /** 거래일자(EAI 채널헤더) [size : 8, layout order number : 5] */
    private String field5 = FormatUtil.getToday("yyyyMMdd");
    
    /** 송신서버(EAI 채널헤더) [size : 8, layout order number : 6] */
    private String field6;
    
    /** 수신서버(EAI 채널헤더) [size : 8, layout order number : 7] */
    private String field7;
    
    /** 송신서버일련번호(EAI 채널헤더) [size : 28, layout order number : 8] */
    private String field8;
    
    /** 전문구분(EAI 채널헤더) [size : 6, layout order number : 9] */
    private String field9;
    
    /** 전문처리순번(EAI 채널헤더) [size : 4, layout order number : 10] */
    private long field10;
    
    /** 서비스명(EAI 채널헤더) [size : 16, layout order number : 11] */
    private String field11;
    
    /** 단말매체(EAI 채널헤더) [size : 2, layout order number : 12] */
    private String field12;
    
    /** 단말IP(EAI 채널헤더) [size : 15, layout order number : 13] */
    private String field13;
    
    /** 거래일련번호(정보계 처리순번)(EAI 채널헤더) [size : 4, layout order number : 14] */
    private long field14;
    
    /** 가상거래순번(ucrt data 중 대량 처리용)(EAI 채널헤더) [size : 4, layout order number : 15] */
    private long field15;
    
    /** 거래플래그(EAI 채널헤더) [size : 2, layout order number : 16] */
    private String field16;
    
    /** 요구응답구분(EAI 채널헤더) [size : 1, layout order number : 17] */
    private String field17;
    
    /** 영업일자(EAI 채널헤더) [size : 8, layout order number : 18] */
    private String field18 = FormatUtil.getToday("yyyyMMdd");
    
    /** 업무서버송신일시(EAI 채널헤더) [size : 16, layout order number : 19] */
    private String field19 = FormatUtil.getToday("yyyyMMddHHmmssSS");
    
    /** 업무서버응답일시(EAI 채널헤더) [size : 16, layout order number : 20] */
    private String field20 = FormatUtil.getToday("yyyyMMddHHmmssSS");
    
    /** 메시지형식(EAI 채널헤더) [size : 1, layout order number : 21] */
    private String field21;
    
    /** 메시지변환(EAI 채널헤더) [size : 1, layout order number : 22] */
    private String field22;
    
    /** 처리결과(EAI 채널헤더) [size : 1, layout order number : 23] */
    private String field23;
    
    /** 인터페이스환경(EAI 채널헤더) [size : 1, layout order number : 24] */
    private String field24;
    
    /** EAI거래코드(EAI 채널헤더) [size : 10, layout order number : 25] */
    private String field25;
    
    /** EAI일련번호(EAI 채널헤더) [size : 35, layout order number : 26] */
    private String field26;
    
    /** 서버COMMIT여부(EAI 채널헤더) [size : 1, layout order number : 27] */
    private String field27;
    
    /** EAI오류코드(EAI 채널헤더) [size : 5, layout order number : 28] */
    private String field28 = "00000";
    
    /** 거래발생시각(EAI 채널헤더) [size : 6, layout order number : 29] */
    private String field29 = FormatUtil.getToday("HHmmss");
    
    /** Timeout시간(EAI 채널헤더) [size : 3, layout order number : 30] */
    private int field30 = 240;
    
    /** 잔여기준시간(EAI 채널헤더) [size : 3, layout order number : 31] */
    private int field31 = 240;
    
    /** 통합전문일련번호 [size : 14, layout order number : 32] */
    private String field32;
    
    /** Reserved(EAI 채널헤더) [size : 14, layout order number : 33] */
    private String field33;
    
    /** 매체코드(EAI 전문헤더) [size : 2, layout order number : 34] */
    private String field34 = "LS";
    
    /** 부점코드(EAI 전문헤더) [size : 4, layout order number : 35] */
    private String field35;
    
    /** 단말번호(EAI 전문헤더) [size : 3, layout order number : 36] */
    private String field36 = "000";
    
    /** 직번(EAI 전문헤더) [size : 7, layout order number : 37] */
    private String field37;
    
    /** 메시지통번(EAI 전문헤더) [size : 4, layout order number : 38] */
    private String field38;
    
    /** 미들웨이통번(EAI 전문헤더) [size : 4, layout order number : 39] */
    private String field39;
    
    /** 출력메시지갯수(EAI 전문헤더) [size : 3, layout order number : 40] */
    private int field40;
    
    /** 기관코드(EAI 전문헤더) [size : 1, layout order number : 41] */
    private String field41 = "1";
    
    /** 다음출력대상구분(EAI 전문헤더) [size : 2, layout order number : 42] */
    private String field42;
    
    /** 시스템FLAG(EAI 전문헤더) [size : 8, layout order number : 43] */
    private byte [] field43 = {(byte)0x02, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00};
    
    /** 계정갱신모드구분(EAI 전문헤더) [size : 1, layout order number : 44] */
    private String field44 = "0";
    
    /** 승인FLAG(EAI 전문헤더) [size : 1, layout order number : 45] */
    private byte [] field45 = {(byte)0x00};
    
    /** 승인응답구분(EAI 전문헤더) [size : 1, layout order number : 46] */
    private String field46 = "0";
    
    /** 책임자1승인응답구분(EAI 전문헤더) [size : 1, layout order number : 47] */
    private String field47 = "0";
    
    /** 책임자2승인응답구분(EAI 전문헤더) [size : 1, layout order number : 48] */
    private String field48 = "0";
    
    /** 거래코드(화면번호)(EAI 전문헤더) [size : 9, layout order number : 49] */
    private String field49 = "321004002";
    
    /** 거래SUB코드(EAI 전문헤더) [size : 2, layout order number : 50] */
    private String field50 = "LS";
    
    /** 거래로그건수(EAI 전문헤더) [size : 3, layout order number : 51] */
    private int field51;
    
    /** 자동화기기버전(EAI 전문헤더) [size : 8, layout order number : 52] */
    private String field52;
    
    /** Reserved(EAI 전문헤더) [size : 8, layout order number : 53] */
    private String field53;

    /**
     * EAIMessage 의 기본 생성자
     */
    public EAIMessage() {
    }//end of constructor
    

    /**
     * 전문으로의 객체 직렬화 작업을 수행합니다.
     * 
     * @return 객체의 직렬화 된 byte []
     * @see com.keb.stpa.engine.msg.Serializable#marshall()
     */
    public byte [] marshall() {
        if (this.data == null) {
            this.data = HeaderConstants.EMPTY_BYTE_ARRAY;
        }//end if
        
        byte [] marshalledData = new byte[341 + this.data.length];
        
        int index = 0;
        
        byte [] bfield1 = FormatUtil.lPadding(Long.toString(340 + this.data.length + 1), 10, HeaderConstants.ZERO_CHAR).getBytes();
        System.arraycopy(bfield1, 0, marshalledData, index, bfield1.length);
        index += 10;
        byte [] bfield2 = FormatUtil.lPadding(Long.toString(107 + this.data.length + 1), 10, HeaderConstants.ZERO_CHAR).getBytes();
        System.arraycopy(bfield2, 0, marshalledData, index, bfield2.length);
        index += 10;
        byte [] bfield3 = FormatUtil.rPadding(this.field3, 1, HeaderConstants.SPACE_CHAR).getBytes();       
        System.arraycopy(bfield3, 0, marshalledData, index, bfield3.length);
        index += 1;
        byte [] bfield4 = FormatUtil.rPadding(this.field4, 5, HeaderConstants.SPACE_CHAR).getBytes();       
        System.arraycopy(bfield4, 0, marshalledData, index, bfield4.length);
        index += 5;
        byte [] bfield5 = FormatUtil.rPadding(this.field5, 8, HeaderConstants.SPACE_CHAR).getBytes();       
        System.arraycopy(bfield5, 0, marshalledData, index, bfield5.length);
        index += 8;
        byte [] bfield6 = FormatUtil.rPadding(this.field6, 8, HeaderConstants.SPACE_CHAR).getBytes();       
        System.arraycopy(bfield6, 0, marshalledData, index, bfield6.length);
        index += 8;
        byte [] bfield7 = FormatUtil.rPadding(this.field7, 8, HeaderConstants.SPACE_CHAR).getBytes();       
        System.arraycopy(bfield7, 0, marshalledData, index, bfield7.length);
        index += 8;
        byte [] bfield8 = FormatUtil.rPadding(this.field8, 28, HeaderConstants.SPACE_CHAR).getBytes();      
        System.arraycopy(bfield8, 0, marshalledData, index, bfield8.length);
        index += 28;
        byte [] bfield9 = FormatUtil.rPadding(this.field9, 6, HeaderConstants.SPACE_CHAR).getBytes();       
        System.arraycopy(bfield9, 0, marshalledData, index, bfield9.length);
        index += 6;
        byte [] bfield10 = FormatUtil.lPadding(Long.toString(this.field10), 4, HeaderConstants.ZERO_CHAR).getBytes();       
        System.arraycopy(bfield10, 0, marshalledData, index, bfield10.length);
        index += 4;
        byte [] bfield11 = FormatUtil.rPadding(this.field11, 16, HeaderConstants.SPACE_CHAR).getBytes();        
        System.arraycopy(bfield11, 0, marshalledData, index, bfield11.length);
        index += 16;
        byte [] bfield12 = FormatUtil.rPadding(this.field12, 2, HeaderConstants.SPACE_CHAR).getBytes();     
        System.arraycopy(bfield12, 0, marshalledData, index, bfield12.length);
        index += 2;
        byte [] bfield13 = FormatUtil.rPadding(this.field13, 15, HeaderConstants.SPACE_CHAR).getBytes();        
        System.arraycopy(bfield13, 0, marshalledData, index, bfield13.length);
        index += 15;
        byte [] bfield14 = FormatUtil.lPadding(Long.toString(this.field14), 4, HeaderConstants.ZERO_CHAR).getBytes();       
        System.arraycopy(bfield14, 0, marshalledData, index, bfield14.length);
        index += 4;
        byte [] bfield15 = FormatUtil.lPadding(Long.toString(this.field15), 4, HeaderConstants.ZERO_CHAR).getBytes();       
        System.arraycopy(bfield15, 0, marshalledData, index, bfield15.length);
        index += 4;
        byte [] bfield16 = FormatUtil.rPadding(this.field16, 2, HeaderConstants.SPACE_CHAR).getBytes();     
        System.arraycopy(bfield16, 0, marshalledData, index, bfield16.length);
        index += 2;
        byte [] bfield17 = FormatUtil.rPadding(this.field17, 1, HeaderConstants.SPACE_CHAR).getBytes();     
        System.arraycopy(bfield17, 0, marshalledData, index, bfield17.length);
        index += 1;
        byte [] bfield18 = FormatUtil.rPadding(this.field18, 8, HeaderConstants.SPACE_CHAR).getBytes();     
        System.arraycopy(bfield18, 0, marshalledData, index, bfield18.length);
        index += 8;
        byte [] bfield19 = FormatUtil.rPadding(this.field19, 16, HeaderConstants.SPACE_CHAR).getBytes();        
        System.arraycopy(bfield19, 0, marshalledData, index, bfield19.length);
        index += 16;
        byte [] bfield20 = FormatUtil.rPadding(this.field20, 16, HeaderConstants.SPACE_CHAR).getBytes();        
        System.arraycopy(bfield20, 0, marshalledData, index, bfield20.length);
        index += 16;
        byte [] bfield21 = FormatUtil.rPadding(this.field21, 1, HeaderConstants.SPACE_CHAR).getBytes();     
        System.arraycopy(bfield21, 0, marshalledData, index, bfield21.length);
        index += 1;
        byte [] bfield22 = FormatUtil.rPadding(this.field22, 1, HeaderConstants.SPACE_CHAR).getBytes();     
        System.arraycopy(bfield22, 0, marshalledData, index, bfield22.length);
        index += 1;
        byte [] bfield23 = FormatUtil.rPadding(this.field23, 1, HeaderConstants.SPACE_CHAR).getBytes();     
        System.arraycopy(bfield23, 0, marshalledData, index, bfield23.length);
        index += 1;
        byte [] bfield24 = FormatUtil.rPadding(this.field24, 1, HeaderConstants.SPACE_CHAR).getBytes();     
        System.arraycopy(bfield24, 0, marshalledData, index, bfield24.length);
        index += 1;
        byte [] bfield25 = FormatUtil.rPadding(this.field25, 10, HeaderConstants.SPACE_CHAR).getBytes();        
        System.arraycopy(bfield25, 0, marshalledData, index, bfield25.length);
        index += 10;
        byte [] bfield26 = FormatUtil.rPadding(this.field26, 35, HeaderConstants.SPACE_CHAR).getBytes();        
        System.arraycopy(bfield26, 0, marshalledData, index, bfield26.length);
        index += 35;
        byte [] bfield27 = FormatUtil.rPadding(this.field27, 1, HeaderConstants.SPACE_CHAR).getBytes();     
        System.arraycopy(bfield27, 0, marshalledData, index, bfield27.length);
        index += 1;
        byte [] bfield28 = FormatUtil.rPadding(this.field28, 5, HeaderConstants.SPACE_CHAR).getBytes();     
        System.arraycopy(bfield28, 0, marshalledData, index, bfield28.length);
        index += 5;
        byte [] bfield29 = FormatUtil.rPadding(this.field29, 6, HeaderConstants.SPACE_CHAR).getBytes();     
        System.arraycopy(bfield29, 0, marshalledData, index, bfield29.length);
        index += 6;
        byte [] bfield30 = FormatUtil.lPadding(Long.toString(this.field30), 3, HeaderConstants.ZERO_CHAR).getBytes();       
        System.arraycopy(bfield30, 0, marshalledData, index, bfield30.length);
        index += 3;
        byte [] bfield31 = FormatUtil.lPadding(Long.toString(this.field31), 3, HeaderConstants.ZERO_CHAR).getBytes();       
        System.arraycopy(bfield31, 0, marshalledData, index, bfield31.length);
        index += 3;
        byte [] bfield32 = FormatUtil.rPadding(this.field32, 14, HeaderConstants.SPACE_CHAR).getBytes();        
        System.arraycopy(bfield32, 0, marshalledData, index, bfield32.length);
        index += 14;
        byte [] bfield33 = FormatUtil.rPadding(this.field33, 14, HeaderConstants.SPACE_CHAR).getBytes();        
        System.arraycopy(bfield33, 0, marshalledData, index, bfield33.length);
        index += 14;
        byte [] bfield34 = FormatUtil.rPadding(this.field34, 2, HeaderConstants.SPACE_CHAR).getBytes();     
        System.arraycopy(bfield34, 0, marshalledData, index, bfield34.length);
        index += 2;
        byte [] bfield35 = FormatUtil.rPadding(this.field35, 4, HeaderConstants.SPACE_CHAR).getBytes();     
        System.arraycopy(bfield35, 0, marshalledData, index, bfield35.length);
        index += 4;
        byte [] bfield36 = FormatUtil.rPadding(this.field36, 3, HeaderConstants.SPACE_CHAR).getBytes();     
        System.arraycopy(bfield36, 0, marshalledData, index, bfield36.length);
        index += 3;
        byte [] bfield37 = FormatUtil.rPadding(this.field37, 7, HeaderConstants.SPACE_CHAR).getBytes();     
        System.arraycopy(bfield37, 0, marshalledData, index, bfield37.length);
        index += 7;
        byte [] bfield38 = FormatUtil.rPadding(this.field38, 4, HeaderConstants.SPACE_CHAR).getBytes();     
        System.arraycopy(bfield38, 0, marshalledData, index, bfield38.length);
        index += 4;
        byte [] bfield39 = FormatUtil.rPadding(this.field39, 4, HeaderConstants.SPACE_CHAR).getBytes();     
        System.arraycopy(bfield39, 0, marshalledData, index, bfield39.length);
        index += 4;
        byte [] bfield40 = FormatUtil.lPadding(Long.toString(this.field40), 3, HeaderConstants.ZERO_CHAR).getBytes();       
        System.arraycopy(bfield40, 0, marshalledData, index, bfield40.length);
        index += 3;
        byte [] bfield41 = FormatUtil.rPadding(this.field41, 1, HeaderConstants.SPACE_CHAR).getBytes();     
        System.arraycopy(bfield41, 0, marshalledData, index, bfield41.length);
        index += 1;
        byte [] bfield42 = FormatUtil.rPadding(this.field42, 2, HeaderConstants.SPACE_CHAR).getBytes();     
        System.arraycopy(bfield42, 0, marshalledData, index, bfield42.length);
        index += 2;
        System.arraycopy(field43, 0, marshalledData, index, field43.length);
        index += 8;     
        byte [] bfield44 = FormatUtil.rPadding(this.field44, 1, HeaderConstants.SPACE_CHAR).getBytes();     
        System.arraycopy(bfield44, 0, marshalledData, index, bfield44.length);
        index += 1;
        System.arraycopy(field45, 0, marshalledData, index, field45.length);
        index += 1;     
        byte [] bfield46 = FormatUtil.rPadding(this.field46, 1, HeaderConstants.SPACE_CHAR).getBytes();     
        System.arraycopy(bfield46, 0, marshalledData, index, bfield46.length);
        index += 1;
        byte [] bfield47 = FormatUtil.rPadding(this.field47, 1, HeaderConstants.SPACE_CHAR).getBytes();     
        System.arraycopy(bfield47, 0, marshalledData, index, bfield47.length);
        index += 1;
        byte [] bfield48 = FormatUtil.rPadding(this.field48, 1, HeaderConstants.SPACE_CHAR).getBytes();     
        System.arraycopy(bfield48, 0, marshalledData, index, bfield48.length);
        index += 1;
        byte [] bfield49 = FormatUtil.rPadding(this.field49, 9, HeaderConstants.SPACE_CHAR).getBytes();     
        System.arraycopy(bfield49, 0, marshalledData, index, bfield49.length);
        index += 9;
        byte [] bfield50 = FormatUtil.rPadding(this.field50, 2, HeaderConstants.SPACE_CHAR).getBytes();     
        System.arraycopy(bfield50, 0, marshalledData, index, bfield50.length);
        index += 2;
        byte [] bfield51 = FormatUtil.lPadding(Long.toString(this.field51), 3, HeaderConstants.ZERO_CHAR).getBytes();       
        System.arraycopy(bfield51, 0, marshalledData, index, bfield51.length);
        index += 3;
        byte [] bfield52 = FormatUtil.rPadding(this.field52, 8, HeaderConstants.SPACE_CHAR).getBytes();     
        System.arraycopy(bfield52, 0, marshalledData, index, bfield52.length);
        index += 8;
        byte [] bfield53 = FormatUtil.rPadding(this.field53, 8, HeaderConstants.SPACE_CHAR).getBytes();     
        System.arraycopy(bfield53, 0, marshalledData, index, bfield53.length);
        index += 8;
    
        System.arraycopy(this.data, 0, marshalledData, index, this.data.length);
        
        this.header = new String(marshalledData, 0, 340);
        
        marshalledData[marshalledData.length - 1] = 0x1f;
        return marshalledData;
    }//end of marshall()

    /**
     * 전문에서 객체로의 역직렬화 작업을 수행합니다.
     * 
     * @param marshalledData 객체의 직렬화 된 byte []
     * @throws Exception 
     * @see com.keb.stpa.engine.msg.Serializable#unmarshall(byte[])
     */
    public void unmarshall(byte [] marshalledData) throws Exception {
        if (marshalledData == null) {
            throw new Exception("Unmarshall 하려는 데이터가 null 입니다.");
        } else if (341 > marshalledData.length) {
            throw new Exception("Unmarshall 하려는 데이터의 길이[" + marshalledData.length + " bytes]는 341 bytes 이상이어야 합니다.");
        }//end if else
        
        this.header = new String(marshalledData, 0, 340);
    
        int index = 0;
        StringBuffer buffer = new StringBuffer();
        
        try {
            
        this.field1 = Long.parseLong(new String(marshalledData, index, 10));
        index += 10;
        this.field2 = Long.parseLong(new String(marshalledData, index, 10));
        index += 10;
        this.field3 = new String(marshalledData, index, 1);
        index += 1;
        this.field4 = new String(marshalledData, index, 5);
        index += 5;
        this.field5 = new String(marshalledData, index, 8);
        index += 8;
        this.field6 = new String(marshalledData, index, 8);
        index += 8;
        this.field7 = new String(marshalledData, index, 8);
        index += 8;
        this.field8 = new String(marshalledData, index, 28);
        index += 28;
        this.field9 = new String(marshalledData, index, 6);
        index += 6;
        this.field10 = Long.parseLong(new String(marshalledData, index, 4));
        index += 4;
        this.field11 = new String(marshalledData, index, 16);
        index += 16;
        this.field12 = new String(marshalledData, index, 2);
        index += 2;
        this.field13 = new String(marshalledData, index, 15);
        index += 15;
        this.field14 = Long.parseLong(new String(marshalledData, index, 4));
        index += 4;
        this.field15 = Long.parseLong(new String(marshalledData, index, 4));
        index += 4;
        this.field16 = new String(marshalledData, index, 2);
        index += 2;
        this.field17 = new String(marshalledData, index, 1);
        index += 1;
        this.field18 = new String(marshalledData, index, 8);
        index += 8;
        this.field19 = new String(marshalledData, index, 16);
        index += 16;
        this.field20 = new String(marshalledData, index, 16);
        index += 16;
        this.field21 = new String(marshalledData, index, 1);
        index += 1;
        this.field22 = new String(marshalledData, index, 1);
        index += 1;
        this.field23 = new String(marshalledData, index, 1);
        index += 1;
        this.field24 = new String(marshalledData, index, 1);
        index += 1;
        this.field25 = new String(marshalledData, index, 10);
        index += 10;
        this.field26 = new String(marshalledData, index, 35);
        index += 35;
        this.field27 = new String(marshalledData, index, 1);
        index += 1;
        this.field28 = new String(marshalledData, index, 5);
        index += 5;
        this.field29 = new String(marshalledData, index, 6);
        index += 6;
        this.field30 = Integer.parseInt(new String(marshalledData, index, 3));
        index += 3;
        this.field31 = Integer.parseInt(new String(marshalledData, index, 3));
        index += 3;
        this.field32 = new String(marshalledData, index, 14);
        index += 14;
        this.field33 = new String(marshalledData, index, 14);
        index += 14;
        this.field34 = new String(marshalledData, index, 2);
        index += 2;
        this.field35 = new String(marshalledData, index, 4);
        index += 4;
        this.field36 = new String(marshalledData, index, 3);
        index += 3;
        this.field37 = new String(marshalledData, index, 7);
        index += 7;
        this.field38 = new String(marshalledData, index, 4);
        index += 4;
        this.field39 = new String(marshalledData, index, 4);
        index += 4;
        this.field40 = Integer.parseInt(new String(marshalledData, index, 3));
        index += 3;
        this.field41 = new String(marshalledData, index, 1);
        index += 1;
        this.field42 = new String(marshalledData, index, 2);
        index += 2;
        System.arraycopy(marshalledData, index, this.field43, 0, 8);
        index += 8;
        this.field44 = new String(marshalledData, index, 1);
        index += 1;
        System.arraycopy(marshalledData, index, this.field45, 0, 1);
        index += 1;
        this.field46 = new String(marshalledData, index, 1);
        index += 1;
        this.field47 = new String(marshalledData, index, 1);
        index += 1;
        this.field48 = new String(marshalledData, index, 1);
        index += 1;
        this.field49 = new String(marshalledData, index, 9);
        index += 9;
        this.field50 = new String(marshalledData, index, 2);
        index += 2;
        this.field51 = Integer.parseInt(new String(marshalledData, index, 3));
        index += 3;
        this.field52 = new String(marshalledData, index, 8);
        index += 8;
        this.field53 = new String(marshalledData, index, 8);
        index += 8;

        } catch (NumberFormatException e) {
            throw new Exception("데이터가 전문 포맷에 맞지 않습니다. [" + index + "번째 index 는 Numeric field 입니다.]");
        } catch (Exception e) {
            throw new Exception("데이터가 전문 포맷에 맞지 않습니다. [" + index + "번째 index 가 오류 입니다.]");
        }//end try catch

        int bdl = marshalledData.length - index - 1;
        byte [] bData = new byte[bdl];
        System.arraycopy(marshalledData, index, bData, 0, bdl);
        this.data = bData;
        // 전문이 valid 한지 확인합니다. (필수항목의 유무)
        // invalid 한 전문의 경우, InvalidMessageException 을 throw 합니다.
        valid();
    }//end of unmarshall()
    
      
    /**
     * 총전문길이(EAI 채널헤더) 을(를) 리턴합니다.
     * [size : 10, layout order number : 1]
     *
     * @return 총전문길이(EAI 채널헤더)
     */
    public long getField1() {
        return this.field1;
    }//end of getField1()
    
    /**
     * 총전문길이(EAI 채널헤더) 을(를) 세팅합니다.
     * [size : 10, layout order number : 1]
     *
     * @param field1 총전문길이(EAI 채널헤더)
     */
    public void setField1(long field1) {
        this.field1 = field1;
    }//end of setField1()
    
    /**
     * 전문길이(EAI 채널헤더) 을(를) 리턴합니다.
     * [size : 10, layout order number : 2]
     *
     * @return 전문길이(EAI 채널헤더)
     */
    public long getField2() {
        return this.field2;
    }//end of getField2()
    
    /**
     * 전문길이(EAI 채널헤더) 을(를) 세팅합니다.
     * [size : 10, layout order number : 2]
     *
     * @param field2 전문길이(EAI 채널헤더)
     */
    public void setField2(long field2) {
        this.field2 = field2;
    }//end of setField2()
    
    /**
     * sync구분(EAI 채널헤더) 을(를) 리턴합니다.
     * [size : 1, layout order number : 3]
     *
     * @return sync구분(EAI 채널헤더)
     */
    public String getField3() {
        return this.field3;
    }//end of getField3()
    
    /**
     * sync구분(EAI 채널헤더) 을(를) 세팅합니다.
     * [size : 1, layout order number : 3]
     *
     * @param field3 sync구분(EAI 채널헤더)
     */
    public void setField3(String field3) {
        this.field3 = field3;
    }//end of setField3()
    
    /**
     * header구분(EAI 채널헤더) 을(를) 리턴합니다.
     * [size : 5, layout order number : 4]
     *
     * @return header구분(EAI 채널헤더)
     */
    public String getField4() {
        return this.field4;
    }//end of getField4()
    
    /**
     * header구분(EAI 채널헤더) 을(를) 세팅합니다.
     * [size : 5, layout order number : 4]
     *
     * @param field4 header구분(EAI 채널헤더)
     */
    public void setField4(String field4) {
        this.field4 = field4;
    }//end of setField4()
    
    /**
     * 거래일자(EAI 채널헤더) 을(를) 리턴합니다.
     * [size : 8, layout order number : 5]
     *
     * @return 거래일자(EAI 채널헤더)
     */
    public String getField5() {
        return this.field5;
    }//end of getField5()
    
    /**
     * 거래일자(EAI 채널헤더) 을(를) 세팅합니다.
     * [size : 8, layout order number : 5]
     *
     * @param field5 거래일자(EAI 채널헤더)
     */
    public void setField5(String field5) {
        this.field5 = field5;
    }//end of setField5()
    
    /**
     * 송신서버(EAI 채널헤더) 을(를) 리턴합니다.
     * [size : 8, layout order number : 6]
     *
     * @return 송신서버(EAI 채널헤더)
     */
    public String getField6() {
        return this.field6;
    }//end of getField6()
    
    /**
     * 송신서버(EAI 채널헤더) 을(를) 세팅합니다.
     * [size : 8, layout order number : 6]
     *
     * @param field6 송신서버(EAI 채널헤더)
     */
    public void setField6(String field6) {
        this.field6 = field6;
    }//end of setField6()
    
    /**
     * 수신서버(EAI 채널헤더) 을(를) 리턴합니다.
     * [size : 8, layout order number : 7]
     *
     * @return 수신서버(EAI 채널헤더)
     */
    public String getField7() {
        return this.field7;
    }//end of getField7()
    
    /**
     * 수신서버(EAI 채널헤더) 을(를) 세팅합니다.
     * [size : 8, layout order number : 7]
     *
     * @param field7 수신서버(EAI 채널헤더)
     */
    public void setField7(String field7) {
        this.field7 = field7;
    }//end of setField7()
    
    /**
     * 송신서버일련번호(EAI 채널헤더) 을(를) 리턴합니다.
     * [size : 28, layout order number : 8]
     *
     * @return 송신서버일련번호(EAI 채널헤더)
     */
    public String getField8() {
        return this.field8;
    }//end of getField8()
    
    /**
     * 송신서버일련번호(EAI 채널헤더) 을(를) 세팅합니다.
     * [size : 28, layout order number : 8]
     *
     * @param field8 송신서버일련번호(EAI 채널헤더)
     */
    public void setField8(String field8) {
        this.field8 = field8;
    }//end of setField8()
    
    /**
     * 전문구분(EAI 채널헤더) 을(를) 리턴합니다.
     * [size : 6, layout order number : 9]
     *
     * @return 전문구분(EAI 채널헤더)
     */
    public String getField9() {
        return this.field9;
    }//end of getField9()
    
    /**
     * 전문구분(EAI 채널헤더) 을(를) 세팅합니다.
     * [size : 6, layout order number : 9]
     *
     * @param field9 전문구분(EAI 채널헤더)
     */
    public void setField9(String field9) {
        this.field9 = field9;
    }//end of setField9()
    
    /**
     * 전문처리순번(EAI 채널헤더) 을(를) 리턴합니다.
     * [size : 4, layout order number : 10]
     *
     * @return 전문처리순번(EAI 채널헤더)
     */
    public long getField10() {
        return this.field10;
    }//end of getField10()
    
    /**
     * 전문처리순번(EAI 채널헤더) 을(를) 세팅합니다.
     * [size : 4, layout order number : 10]
     *
     * @param field10 전문처리순번(EAI 채널헤더)
     */
    public void setField10(long field10) {
        this.field10 = field10;
    }//end of setField10()
    
    /**
     * 서비스명(EAI 채널헤더) 을(를) 리턴합니다.
     * [size : 16, layout order number : 11]
     *
     * @return 서비스명(EAI 채널헤더)
     */
    public String getField11() {
        return this.field11;
    }//end of getField11()
    
    /**
     * 서비스명(EAI 채널헤더) 을(를) 세팅합니다.
     * [size : 16, layout order number : 11]
     *
     * @param field11 서비스명(EAI 채널헤더)
     */
    public void setField11(String field11) {
        this.field11 = field11;
    }//end of setField11()
    
    /**
     * 단말매체(EAI 채널헤더) 을(를) 리턴합니다.
     * [size : 2, layout order number : 12]
     *
     * @return 단말매체(EAI 채널헤더)
     */
    public String getField12() {
        return this.field12;
    }//end of getField12()
    
    /**
     * 단말매체(EAI 채널헤더) 을(를) 세팅합니다.
     * [size : 2, layout order number : 12]
     *
     * @param field12 단말매체(EAI 채널헤더)
     */
    public void setField12(String field12) {
        this.field12 = field12;
    }//end of setField12()
    
    /**
     * 단말IP(EAI 채널헤더) 을(를) 리턴합니다.
     * [size : 15, layout order number : 13]
     *
     * @return 단말IP(EAI 채널헤더)
     */
    public String getField13() {
        return this.field13;
    }//end of getField13()
    
    /**
     * 단말IP(EAI 채널헤더) 을(를) 세팅합니다.
     * [size : 15, layout order number : 13]
     *
     * @param field13 단말IP(EAI 채널헤더)
     */
    public void setField13(String field13) {
        this.field13 = field13;
    }//end of setField13()
    
    /**
     * 거래일련번호(정보계 처리순번)(EAI 채널헤더) 을(를) 리턴합니다.
     * [size : 4, layout order number : 14]
     *
     * @return 거래일련번호(정보계 처리순번)(EAI 채널헤더)
     */
    public long getField14() {
        return this.field14;
    }//end of getField14()
    
    /**
     * 거래일련번호(정보계 처리순번)(EAI 채널헤더) 을(를) 세팅합니다.
     * [size : 4, layout order number : 14]
     *
     * @param field14 거래일련번호(정보계 처리순번)(EAI 채널헤더)
     */
    public void setField14(long field14) {
        this.field14 = field14;
    }//end of setField14()
    
    /**
     * 가상거래순번(ucrt data 중 대량 처리용)(EAI 채널헤더) 을(를) 리턴합니다.
     * [size : 4, layout order number : 15]
     *
     * @return 가상거래순번(ucrt data 중 대량 처리용)(EAI 채널헤더)
     */
    public long getField15() {
        return this.field15;
    }//end of getField15()
    
    /**
     * 가상거래순번(ucrt data 중 대량 처리용)(EAI 채널헤더) 을(를) 세팅합니다.
     * [size : 4, layout order number : 15]
     *
     * @param field15 가상거래순번(ucrt data 중 대량 처리용)(EAI 채널헤더)
     */
    public void setField15(long field15) {
        this.field15 = field15;
    }//end of setField15()
    
    /**
     * 거래플래그(EAI 채널헤더) 을(를) 리턴합니다.
     * [size : 2, layout order number : 16]
     *
     * @return 거래플래그(EAI 채널헤더)
     */
    public String getField16() {
        return this.field16;
    }//end of getField16()
    
    /**
     * 거래플래그(EAI 채널헤더) 을(를) 세팅합니다.
     * [size : 2, layout order number : 16]
     *
     * @param field16 거래플래그(EAI 채널헤더)
     */
    public void setField16(String field16) {
        this.field16 = field16;
    }//end of setField16()
    
    /**
     * 요구응답구분(EAI 채널헤더) 을(를) 리턴합니다.
     * [size : 1, layout order number : 17]
     *
     * @return 요구응답구분(EAI 채널헤더)
     */
    public String getField17() {
        return this.field17;
    }//end of getField17()
    
    /**
     * 요구응답구분(EAI 채널헤더) 을(를) 세팅합니다.
     * [size : 1, layout order number : 17]
     *
     * @param field17 요구응답구분(EAI 채널헤더)
     */
    public void setField17(String field17) {
        this.field17 = field17;
    }//end of setField17()
    
    /**
     * 영업일자(EAI 채널헤더) 을(를) 리턴합니다.
     * [size : 8, layout order number : 18]
     *
     * @return 영업일자(EAI 채널헤더)
     */
    public String getField18() {
        return this.field18;
    }//end of getField18()
    
    /**
     * 영업일자(EAI 채널헤더) 을(를) 세팅합니다.
     * [size : 8, layout order number : 18]
     *
     * @param field18 영업일자(EAI 채널헤더)
     */
    public void setField18(String field18) {
        this.field18 = field18;
    }//end of setField18()
    
    /**
     * 업무서버송신일시(EAI 채널헤더) 을(를) 리턴합니다.
     * [size : 16, layout order number : 19]
     *
     * @return 업무서버송신일시(EAI 채널헤더)
     */
    public String getField19() {
        return this.field19;
    }//end of getField19()
    
    /**
     * 업무서버송신일시(EAI 채널헤더) 을(를) 세팅합니다.
     * [size : 16, layout order number : 19]
     *
     * @param field19 업무서버송신일시(EAI 채널헤더)
     */
    public void setField19(String field19) {
        this.field19 = field19;
    }//end of setField19()
    
    /**
     * 업무서버응답일시(EAI 채널헤더) 을(를) 리턴합니다.
     * [size : 16, layout order number : 20]
     *
     * @return 업무서버응답일시(EAI 채널헤더)
     */
    public String getField20() {
        return this.field20;
    }//end of getField20()
    
    /**
     * 업무서버응답일시(EAI 채널헤더) 을(를) 세팅합니다.
     * [size : 16, layout order number : 20]
     *
     * @param field20 업무서버응답일시(EAI 채널헤더)
     */
    public void setField20(String field20) {
        this.field20 = field20;
    }//end of setField20()
    
    /**
     * 메시지형식(EAI 채널헤더) 을(를) 리턴합니다.
     * [size : 1, layout order number : 21]
     *
     * @return 메시지형식(EAI 채널헤더)
     */
    public String getField21() {
        return this.field21;
    }//end of getField21()
    
    /**
     * 메시지형식(EAI 채널헤더) 을(를) 세팅합니다.
     * [size : 1, layout order number : 21]
     *
     * @param field21 메시지형식(EAI 채널헤더)
     */
    public void setField21(String field21) {
        this.field21 = field21;
    }//end of setField21()
    
    /**
     * 메시지변환(EAI 채널헤더) 을(를) 리턴합니다.
     * [size : 1, layout order number : 22]
     *
     * @return 메시지변환(EAI 채널헤더)
     */
    public String getField22() {
        return this.field22;
    }//end of getField22()
    
    /**
     * 메시지변환(EAI 채널헤더) 을(를) 세팅합니다.
     * [size : 1, layout order number : 22]
     *
     * @param field22 메시지변환(EAI 채널헤더)
     */
    public void setField22(String field22) {
        this.field22 = field22;
    }//end of setField22()
    
    /**
     * 처리결과(EAI 채널헤더) 을(를) 리턴합니다.
     * [size : 1, layout order number : 23]
     *
     * @return 처리결과(EAI 채널헤더)
     */
    public String getField23() {
        return this.field23;
    }//end of getField23()
    
    /**
     * 처리결과(EAI 채널헤더) 을(를) 세팅합니다.
     * [size : 1, layout order number : 23]
     *
     * @param field23 처리결과(EAI 채널헤더)
     */
    public void setField23(String field23) {
        this.field23 = field23;
    }//end of setField23()
    
    /**
     * 인터페이스환경(EAI 채널헤더) 을(를) 리턴합니다.
     * [size : 1, layout order number : 24]
     *
     * @return 인터페이스환경(EAI 채널헤더)
     */
    public String getField24() {
        return this.field24;
    }//end of getField24()
    
    /**
     * 인터페이스환경(EAI 채널헤더) 을(를) 세팅합니다.
     * [size : 1, layout order number : 24]
     *
     * @param field24 인터페이스환경(EAI 채널헤더)
     */
    public void setField24(String field24) {
        this.field24 = field24;
    }//end of setField24()
    
    /**
     * EAI거래코드(EAI 채널헤더) 을(를) 리턴합니다.
     * [size : 10, layout order number : 25]
     *
     * @return EAI거래코드(EAI 채널헤더)
     */
    public String getField25() {
        return this.field25;
    }//end of getField25()
    
    /**
     * EAI거래코드(EAI 채널헤더) 을(를) 세팅합니다.
     * [size : 10, layout order number : 25]
     *
     * @param field25 EAI거래코드(EAI 채널헤더)
     */
    public void setField25(String field25) {
        this.field25 = field25;
    }//end of setField25()
    
    /**
     * EAI일련번호(EAI 채널헤더) 을(를) 리턴합니다.
     * [size : 35, layout order number : 26]
     *
     * @return EAI일련번호(EAI 채널헤더)
     */
    public String getField26() {
        return this.field26;
    }//end of getField26()
    
    /**
     * EAI일련번호(EAI 채널헤더) 을(를) 세팅합니다.
     * [size : 35, layout order number : 26]
     *
     * @param field26 EAI일련번호(EAI 채널헤더)
     */
    public void setField26(String field26) {
        this.field26 = field26;
    }//end of setField26()
    
    /**
     * 서버COMMIT여부(EAI 채널헤더) 을(를) 리턴합니다.
     * [size : 1, layout order number : 27]
     *
     * @return 서버COMMIT여부(EAI 채널헤더)
     */
    public String getField27() {
        return this.field27;
    }//end of getField27()
    
    /**
     * 서버COMMIT여부(EAI 채널헤더) 을(를) 세팅합니다.
     * [size : 1, layout order number : 27]
     *
     * @param field27 서버COMMIT여부(EAI 채널헤더)
     */
    public void setField27(String field27) {
        this.field27 = field27;
    }//end of setField27()
    
    /**
     * EAI오류코드(EAI 채널헤더) 을(를) 리턴합니다.
     * [size : 5, layout order number : 28]
     *
     * @return EAI오류코드(EAI 채널헤더)
     */
    public String getField28() {
        return this.field28;
    }//end of getField28()
    
    /**
     * EAI오류코드(EAI 채널헤더) 을(를) 세팅합니다.
     * [size : 5, layout order number : 28]
     *
     * @param field28 EAI오류코드(EAI 채널헤더)
     */
    public void setField28(String field28) {
        this.field28 = field28;
    }//end of setField28()
    
    /**
     * 거래발생시각(EAI 채널헤더) 을(를) 리턴합니다.
     * [size : 6, layout order number : 29]
     *
     * @return 거래발생시각(EAI 채널헤더)
     */
    public String getField29() {
        return this.field29;
    }//end of getField29()
    
    /**
     * 거래발생시각(EAI 채널헤더) 을(를) 세팅합니다.
     * [size : 6, layout order number : 29]
     *
     * @param field29 거래발생시각(EAI 채널헤더)
     */
    public void setField29(String field29) {
        this.field29 = field29;
    }//end of setField29()
    
    /**
     * Timeout시간(EAI 채널헤더) 을(를) 리턴합니다.
     * [size : 3, layout order number : 30]
     *
     * @return Timeout시간(EAI 채널헤더)
     */
    public int getField30() {
        return this.field30;
    }//end of getField30()
    
    /**
     * Timeout시간(EAI 채널헤더) 을(를) 세팅합니다.
     * [size : 3, layout order number : 30]
     *
     * @param field30 Timeout시간(EAI 채널헤더)
     */
    public void setField30(int field30) {
        this.field30 = field30;
    }//end of setField30()
    
    /**
     * 잔여기준시간(EAI 채널헤더) 을(를) 리턴합니다.
     * [size : 3, layout order number : 31]
     *
     * @return 잔여기준시간(EAI 채널헤더)
     */
    public int getField31() {
        return this.field31;
    }//end of getField31()
    
    /**
     * 잔여기준시간(EAI 채널헤더) 을(를) 세팅합니다.
     * [size : 3, layout order number : 31]
     *
     * @param field31 잔여기준시간(EAI 채널헤더)
     */
    public void setField31(int field31) {
        this.field31 = field31;
    }//end of setField31()
    
    /**
     * 통합전문일련번호 을(를) 리턴합니다.
     * [size : 14, layout order number : 32]
     *
     * @return 통합전문일련번호
     */
    public String getField32() {
        return this.field32;
    }//end of getField32()
    
    /**
     * 통합전문일련번호 을(를) 세팅합니다.
     * [size : 14, layout order number : 32]
     *
     * @param field32 통합전문일련번호
     */
    public void setField32(String field32) {
        this.field32 = field32;
    }//end of setField32()
    
    /**
     * Reserved(EAI 채널헤더) 을(를) 리턴합니다.
     * [size : 14, layout order number : 33]
     *
     * @return Reserved(EAI 채널헤더)
     */
    public String getField33() {
        return this.field33;
    }//end of getField33()
    
    /**
     * Reserved(EAI 채널헤더) 을(를) 세팅합니다.
     * [size : 14, layout order number : 33]
     *
     * @param field33 Reserved(EAI 채널헤더)
     */
    public void setField33(String field33) {
        this.field33 = field33;
    }//end of setField33()
    
    /**
     * 매체코드(EAI 전문헤더) 을(를) 리턴합니다.
     * [size : 2, layout order number : 34]
     *
     * @return 매체코드(EAI 전문헤더)
     */
    public String getField34() {
        return this.field34;
    }//end of getField34()
    
    /**
     * 매체코드(EAI 전문헤더) 을(를) 세팅합니다.
     * [size : 2, layout order number : 34]
     *
     * @param field34 매체코드(EAI 전문헤더)
     */
    public void setField34(String field34) {
        this.field34 = field34;
    }//end of setField34()
    
    /**
     * 부점코드(EAI 전문헤더) 을(를) 리턴합니다.
     * [size : 4, layout order number : 35]
     *
     * @return 부점코드(EAI 전문헤더)
     */
    public String getField35() {
        return this.field35;
    }//end of getField35()
    
    /**
     * 부점코드(EAI 전문헤더) 을(를) 세팅합니다.
     * [size : 4, layout order number : 35]
     *
     * @param field35 부점코드(EAI 전문헤더)
     */
    public void setField35(String field35) {
        this.field35 = field35;
    }//end of setField35()
    
    /**
     * 단말번호(EAI 전문헤더) 을(를) 리턴합니다.
     * [size : 3, layout order number : 36]
     *
     * @return 단말번호(EAI 전문헤더)
     */
    public String getField36() {
        return this.field36;
    }//end of getField36()
    
    /**
     * 단말번호(EAI 전문헤더) 을(를) 세팅합니다.
     * [size : 3, layout order number : 36]
     *
     * @param field36 단말번호(EAI 전문헤더)
     */
    public void setField36(String field36) {
        this.field36 = field36;
    }//end of setField36()
    
    /**
     * 직번(EAI 전문헤더) 을(를) 리턴합니다.
     * [size : 7, layout order number : 37]
     *
     * @return 직번(EAI 전문헤더)
     */
    public String getField37() {
        return this.field37;
    }//end of getField37()
    
    /**
     * 직번(EAI 전문헤더) 을(를) 세팅합니다.
     * [size : 7, layout order number : 37]
     *
     * @param field37 직번(EAI 전문헤더)
     */
    public void setField37(String field37) {
        this.field37 = field37;
    }//end of setField37()
    
    /**
     * 메시지통번(EAI 전문헤더) 을(를) 리턴합니다.
     * [size : 4, layout order number : 38]
     *
     * @return 메시지통번(EAI 전문헤더)
     */
    public String getField38() {
        return this.field38;
    }//end of getField38()
    
    /**
     * 메시지통번(EAI 전문헤더) 을(를) 세팅합니다.
     * [size : 4, layout order number : 38]
     *
     * @param field38 메시지통번(EAI 전문헤더)
     */
    public void setField38(String field38) {
        this.field38 = field38;
    }//end of setField38()
    
    /**
     * 미들웨이통번(EAI 전문헤더) 을(를) 리턴합니다.
     * [size : 4, layout order number : 39]
     *
     * @return 미들웨이통번(EAI 전문헤더)
     */
    public String getField39() {
        return this.field39;
    }//end of getField39()
    
    /**
     * 미들웨이통번(EAI 전문헤더) 을(를) 세팅합니다.
     * [size : 4, layout order number : 39]
     *
     * @param field39 미들웨이통번(EAI 전문헤더)
     */
    public void setField39(String field39) {
        this.field39 = field39;
    }//end of setField39()
    
    /**
     * 출력메시지갯수(EAI 전문헤더) 을(를) 리턴합니다.
     * [size : 3, layout order number : 40]
     *
     * @return 출력메시지갯수(EAI 전문헤더)
     */
    public int getField40() {
        return this.field40;
    }//end of getField40()
    
    /**
     * 출력메시지갯수(EAI 전문헤더) 을(를) 세팅합니다.
     * [size : 3, layout order number : 40]
     *
     * @param field40 출력메시지갯수(EAI 전문헤더)
     */
    public void setField40(int field40) {
        this.field40 = field40;
    }//end of setField40()
    
    /**
     * 기관코드(EAI 전문헤더) 을(를) 리턴합니다.
     * [size : 1, layout order number : 41]
     *
     * @return 기관코드(EAI 전문헤더)
     */
    public String getField41() {
        return this.field41;
    }//end of getField41()
    
    /**
     * 기관코드(EAI 전문헤더) 을(를) 세팅합니다.
     * [size : 1, layout order number : 41]
     *
     * @param field41 기관코드(EAI 전문헤더)
     */
    public void setField41(String field41) {
        this.field41 = field41;
    }//end of setField41()
    
    /**
     * 다음출력대상구분(EAI 전문헤더) 을(를) 리턴합니다.
     * [size : 2, layout order number : 42]
     *
     * @return 다음출력대상구분(EAI 전문헤더)
     */
    public String getField42() {
        return this.field42;
    }//end of getField42()
    
    /**
     * 다음출력대상구분(EAI 전문헤더) 을(를) 세팅합니다.
     * [size : 2, layout order number : 42]
     *
     * @param field42 다음출력대상구분(EAI 전문헤더)
     */
    public void setField42(String field42) {
        this.field42 = field42;
    }//end of setField42()
    
    /**
     * 시스템FLAG(EAI 전문헤더) 을(를) 리턴합니다.
     * [size : 8, layout order number : 43]
     *
     * @return 시스템FLAG(EAI 전문헤더)
     */
    public byte [] getField43() {
        return this.field43;
    }//end of getField43()
    
    /**
     * 시스템FLAG(EAI 전문헤더) 을(를) 세팅합니다.
     * [size : 8, layout order number : 43]
     *
     * @param field43 시스템FLAG(EAI 전문헤더)
     */
    public void setField43(byte [] field43) {
        this.field43 = field43;
    }//end of setField43()
    
    /**
     * 계정갱신모드구분(EAI 전문헤더) 을(를) 리턴합니다.
     * [size : 1, layout order number : 44]
     *
     * @return 계정갱신모드구분(EAI 전문헤더)
     */
    public String getField44() {
        return this.field44;
    }//end of getField44()
    
    /**
     * 계정갱신모드구분(EAI 전문헤더) 을(를) 세팅합니다.
     * [size : 1, layout order number : 44]
     *
     * @param field44 계정갱신모드구분(EAI 전문헤더)
     */
    public void setField44(String field44) {
        this.field44 = field44;
    }//end of setField44()
    
    /**
     * 승인FLAG(EAI 전문헤더) 을(를) 리턴합니다.
     * [size : 1, layout order number : 45]
     *
     * @return 승인FLAG(EAI 전문헤더)
     */
    public byte [] getField45() {
        return this.field45;
    }//end of getField45()
    
    /**
     * 승인FLAG(EAI 전문헤더) 을(를) 세팅합니다.
     * [size : 1, layout order number : 45]
     *
     * @param field45 승인FLAG(EAI 전문헤더)
     */
    public void setField45(byte [] field45) {
        this.field45 = field45;
    }//end of setField45()
    
    /**
     * 승인응답구분(EAI 전문헤더) 을(를) 리턴합니다.
     * [size : 1, layout order number : 46]
     *
     * @return 승인응답구분(EAI 전문헤더)
     */
    public String getField46() {
        return this.field46;
    }//end of getField46()
    
    /**
     * 승인응답구분(EAI 전문헤더) 을(를) 세팅합니다.
     * [size : 1, layout order number : 46]
     *
     * @param field46 승인응답구분(EAI 전문헤더)
     */
    public void setField46(String field46) {
        this.field46 = field46;
    }//end of setField46()
    
    /**
     * 책임자1승인응답구분(EAI 전문헤더) 을(를) 리턴합니다.
     * [size : 1, layout order number : 47]
     *
     * @return 책임자1승인응답구분(EAI 전문헤더)
     */
    public String getField47() {
        return this.field47;
    }//end of getField47()
    
    /**
     * 책임자1승인응답구분(EAI 전문헤더) 을(를) 세팅합니다.
     * [size : 1, layout order number : 47]
     *
     * @param field47 책임자1승인응답구분(EAI 전문헤더)
     */
    public void setField47(String field47) {
        this.field47 = field47;
    }//end of setField47()
    
    /**
     * 책임자2승인응답구분(EAI 전문헤더) 을(를) 리턴합니다.
     * [size : 1, layout order number : 48]
     *
     * @return 책임자2승인응답구분(EAI 전문헤더)
     */
    public String getField48() {
        return this.field48;
    }//end of getField48()
    
    /**
     * 책임자2승인응답구분(EAI 전문헤더) 을(를) 세팅합니다.
     * [size : 1, layout order number : 48]
     *
     * @param field48 책임자2승인응답구분(EAI 전문헤더)
     */
    public void setField48(String field48) {
        this.field48 = field48;
    }//end of setField48()
    
    /**
     * 거래코드(화면번호)(EAI 전문헤더) 을(를) 리턴합니다.
     * [size : 9, layout order number : 49]
     *
     * @return 거래코드(화면번호)(EAI 전문헤더)
     */
    public String getField49() {
        return this.field49;
    }//end of getField49()
    
    /**
     * 거래코드(화면번호)(EAI 전문헤더) 을(를) 세팅합니다.
     * [size : 9, layout order number : 49]
     *
     * @param field49 거래코드(화면번호)(EAI 전문헤더)
     */
    public void setField49(String field49) {
        this.field49 = field49;
    }//end of setField49()
    
    /**
     * 거래SUB코드(EAI 전문헤더) 을(를) 리턴합니다.
     * [size : 2, layout order number : 50]
     *
     * @return 거래SUB코드(EAI 전문헤더)
     */
    public String getField50() {
        return this.field50;
    }//end of getField50()
    
    /**
     * 거래SUB코드(EAI 전문헤더) 을(를) 세팅합니다.
     * [size : 2, layout order number : 50]
     *
     * @param field50 거래SUB코드(EAI 전문헤더)
     */
    public void setField50(String field50) {
        this.field50 = field50;
    }//end of setField50()
    
    /**
     * 거래로그건수(EAI 전문헤더) 을(를) 리턴합니다.
     * [size : 3, layout order number : 51]
     *
     * @return 거래로그건수(EAI 전문헤더)
     */
    public int getField51() {
        return this.field51;
    }//end of getField51()
    
    /**
     * 거래로그건수(EAI 전문헤더) 을(를) 세팅합니다.
     * [size : 3, layout order number : 51]
     *
     * @param field51 거래로그건수(EAI 전문헤더)
     */
    public void setField51(int field51) {
        this.field51 = field51;
    }//end of setField51()
    
    /**
     * 자동화기기버전(EAI 전문헤더) 을(를) 리턴합니다.
     * [size : 8, layout order number : 52]
     *
     * @return 자동화기기버전(EAI 전문헤더)
     */
    public String getField52() {
        return this.field52;
    }//end of getField52()
    
    /**
     * 자동화기기버전(EAI 전문헤더) 을(를) 세팅합니다.
     * [size : 8, layout order number : 52]
     *
     * @param field52 자동화기기버전(EAI 전문헤더)
     */
    public void setField52(String field52) {
        this.field52 = field52;
    }//end of setField52()
    
    /**
     * Reserved(EAI 전문헤더) 을(를) 리턴합니다.
     * [size : 8, layout order number : 53]
     *
     * @return Reserved(EAI 전문헤더)
     */
    public String getField53() {
        return this.field53;
    }//end of getField53()
    
    /**
     * Reserved(EAI 전문헤더) 을(를) 세팅합니다.
     * [size : 8, layout order number : 53]
     *
     * @param field53 Reserved(EAI 전문헤더)
     */
    public void setField53(String field53) {
        this.field53 = field53;
    }//end of setField53()
    

    
    /**
     * com.keb.stpa.engine.msg.EAIMessage 의 프로퍼티 값을
     * 일정한 형식의 문자열로 변환하여 리턴합니다.
     * 
     * @return 프로퍼티의 내용을 포함하는 문자열
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append("===== EAIMessage.toString() start =====\n");
        buffer.append("총전문길이(EAI 채널헤더)").append("=[").append(this.field1).append("]");
        buffer.append("\n");
        buffer.append("전문길이(EAI 채널헤더)").append("=[").append(this.field2).append("]");
        buffer.append("\n");
        buffer.append("sync구분(EAI 채널헤더)").append("=[").append(this.field3).append("]");
        buffer.append("\n");
        buffer.append("header구분(EAI 채널헤더)").append("=[").append(this.field4).append("]");
        buffer.append("\n");
        buffer.append("거래일자(EAI 채널헤더)").append("=[").append(this.field5).append("]");
        buffer.append("\n");
        buffer.append("송신서버(EAI 채널헤더)").append("=[").append(this.field6).append("]");
        buffer.append("\n");
        buffer.append("수신서버(EAI 채널헤더)").append("=[").append(this.field7).append("]");
        buffer.append("\n");
        buffer.append("송신서버일련번호(EAI 채널헤더)").append("=[").append(this.field8).append("]");
        buffer.append("\n");
        buffer.append("전문구분(EAI 채널헤더)").append("=[").append(this.field9).append("]");
        buffer.append("\n");
        buffer.append("전문처리순번(EAI 채널헤더)").append("=[").append(this.field10).append("]");
        buffer.append("\n");
        buffer.append("서비스명(EAI 채널헤더)").append("=[").append(this.field11).append("]");
        buffer.append("\n");
        buffer.append("단말매체(EAI 채널헤더)").append("=[").append(this.field12).append("]");
        buffer.append("\n");
        buffer.append("단말IP(EAI 채널헤더)").append("=[").append(this.field13).append("]");
        buffer.append("\n");
        buffer.append("거래일련번호(정보계 처리순번)(EAI 채널헤더)").append("=[").append(this.field14).append("]");
        buffer.append("\n");
        buffer.append("가상거래순번(ucrt data 중 대량 처리용)(EAI 채널헤더)").append("=[").append(this.field15).append("]");
        buffer.append("\n");
        buffer.append("거래플래그(EAI 채널헤더)").append("=[").append(this.field16).append("]");
        buffer.append("\n");
        buffer.append("요구응답구분(EAI 채널헤더)").append("=[").append(this.field17).append("]");
        buffer.append("\n");
        buffer.append("영업일자(EAI 채널헤더)").append("=[").append(this.field18).append("]");
        buffer.append("\n");
        buffer.append("업무서버송신일시(EAI 채널헤더)").append("=[").append(this.field19).append("]");
        buffer.append("\n");
        buffer.append("업무서버응답일시(EAI 채널헤더)").append("=[").append(this.field20).append("]");
        buffer.append("\n");
        buffer.append("메시지형식(EAI 채널헤더)").append("=[").append(this.field21).append("]");
        buffer.append("\n");
        buffer.append("메시지변환(EAI 채널헤더)").append("=[").append(this.field22).append("]");
        buffer.append("\n");
        buffer.append("처리결과(EAI 채널헤더)").append("=[").append(this.field23).append("]");
        buffer.append("\n");
        buffer.append("인터페이스환경(EAI 채널헤더)").append("=[").append(this.field24).append("]");
        buffer.append("\n");
        buffer.append("EAI거래코드(EAI 채널헤더)").append("=[").append(this.field25).append("]");
        buffer.append("\n");
        buffer.append("EAI일련번호(EAI 채널헤더)").append("=[").append(this.field26).append("]");
        buffer.append("\n");
        buffer.append("서버COMMIT여부(EAI 채널헤더)").append("=[").append(this.field27).append("]");
        buffer.append("\n");
        buffer.append("EAI오류코드(EAI 채널헤더)").append("=[").append(this.field28).append("]");
        buffer.append("\n");
        buffer.append("거래발생시각(EAI 채널헤더)").append("=[").append(this.field29).append("]");
        buffer.append("\n");
        buffer.append("Timeout시간(EAI 채널헤더)").append("=[").append(this.field30).append("]");
        buffer.append("\n");
        buffer.append("잔여기준시간(EAI 채널헤더)").append("=[").append(this.field31).append("]");
        buffer.append("\n");
        buffer.append("통합전문일련번호").append("=[").append(this.field32).append("]");
        buffer.append("\n");
        buffer.append("Reserved(EAI 채널헤더)").append("=[").append(this.field33).append("]");
        buffer.append("\n");
        buffer.append("매체코드(EAI 전문헤더)").append("=[").append(this.field34).append("]");
        buffer.append("\n");
        buffer.append("부점코드(EAI 전문헤더)").append("=[").append(this.field35).append("]");
        buffer.append("\n");
        buffer.append("단말번호(EAI 전문헤더)").append("=[").append(this.field36).append("]");
        buffer.append("\n");
        buffer.append("직번(EAI 전문헤더)").append("=[").append(this.field37).append("]");
        buffer.append("\n");
        buffer.append("메시지통번(EAI 전문헤더)").append("=[").append(this.field38).append("]");
        buffer.append("\n");
        buffer.append("미들웨이통번(EAI 전문헤더)").append("=[").append(this.field39).append("]");
        buffer.append("\n");
        buffer.append("출력메시지갯수(EAI 전문헤더)").append("=[").append(this.field40).append("]");
        buffer.append("\n");
        buffer.append("기관코드(EAI 전문헤더)").append("=[").append(this.field41).append("]");
        buffer.append("\n");
        buffer.append("다음출력대상구분(EAI 전문헤더)").append("=[").append(this.field42).append("]");
        buffer.append("\n");
        buffer.append("시스템FLAG(EAI 전문헤더)").append("=[").append(this.field43).append("]");
        buffer.append("\n");
        buffer.append("계정갱신모드구분(EAI 전문헤더)").append("=[").append(this.field44).append("]");
        buffer.append("\n");
        buffer.append("승인FLAG(EAI 전문헤더)").append("=[").append(this.field45).append("]");
        buffer.append("\n");
        buffer.append("승인응답구분(EAI 전문헤더)").append("=[").append(this.field46).append("]");
        buffer.append("\n");
        buffer.append("책임자1승인응답구분(EAI 전문헤더)").append("=[").append(this.field47).append("]");
        buffer.append("\n");
        buffer.append("책임자2승인응답구분(EAI 전문헤더)").append("=[").append(this.field48).append("]");
        buffer.append("\n");
        buffer.append("거래코드(화면번호)(EAI 전문헤더)").append("=[").append(this.field49).append("]");
        buffer.append("\n");
        buffer.append("거래SUB코드(EAI 전문헤더)").append("=[").append(this.field50).append("]");
        buffer.append("\n");
        buffer.append("거래로그건수(EAI 전문헤더)").append("=[").append(this.field51).append("]");
        buffer.append("\n");
        buffer.append("자동화기기버전(EAI 전문헤더)").append("=[").append(this.field52).append("]");
        buffer.append("\n");
        buffer.append("Reserved(EAI 전문헤더)").append("=[").append(this.field53).append("]");
        buffer.append("\n");
        buffer.append("data").append("=[").append((this.data == null) ? "" : new String(this.data)).append("]");
        buffer.append("\n===== EAIMessage.toString() end =====");
        
        return buffer.toString();
    }//end of toString()
    
    /**
     * 전문의 길이 정보를 포함하는 정보 클래스
     * 
     */
    public static class EAIMessageInfo {
    
        /** 전문의 필드 순서별 길이 정보를 가지고 있는 List */
        public static final List INFO_LIST;
        
        static {
            List tempList = new ArrayList();
            tempList.add(Integer.valueOf(10));
            tempList.add(Integer.valueOf(10));
            tempList.add(Integer.valueOf(1));
            tempList.add(Integer.valueOf(5));
            tempList.add(Integer.valueOf(8));
            tempList.add(Integer.valueOf(8));
            tempList.add(Integer.valueOf(8));
            tempList.add(Integer.valueOf(28));
            tempList.add(Integer.valueOf(6));
            tempList.add(Integer.valueOf(4));
            tempList.add(Integer.valueOf(16));
            tempList.add(Integer.valueOf(2));
            tempList.add(Integer.valueOf(15));
            tempList.add(Integer.valueOf(4));
            tempList.add(Integer.valueOf(4));
            tempList.add(Integer.valueOf(2));
            tempList.add(Integer.valueOf(1));
            tempList.add(Integer.valueOf(8));
            tempList.add(Integer.valueOf(16));
            tempList.add(Integer.valueOf(16));
            tempList.add(Integer.valueOf(1));
            tempList.add(Integer.valueOf(1));
            tempList.add(Integer.valueOf(1));
            tempList.add(Integer.valueOf(1));
            tempList.add(Integer.valueOf(10));
            tempList.add(Integer.valueOf(35));
            tempList.add(Integer.valueOf(1));
            tempList.add(Integer.valueOf(5));
            tempList.add(Integer.valueOf(6));
            tempList.add(Integer.valueOf(3));
            tempList.add(Integer.valueOf(3));
            tempList.add(Integer.valueOf(14));
            tempList.add(Integer.valueOf(14));
            tempList.add(Integer.valueOf(2));
            tempList.add(Integer.valueOf(4));
            tempList.add(Integer.valueOf(3));
            tempList.add(Integer.valueOf(7));
            tempList.add(Integer.valueOf(4));
            tempList.add(Integer.valueOf(4));
            tempList.add(Integer.valueOf(3));
            tempList.add(Integer.valueOf(1));
            tempList.add(Integer.valueOf(2));
            tempList.add(Integer.valueOf(8));
            tempList.add(Integer.valueOf(1));
            tempList.add(Integer.valueOf(1));
            tempList.add(Integer.valueOf(1));
            tempList.add(Integer.valueOf(1));
            tempList.add(Integer.valueOf(1));
            tempList.add(Integer.valueOf(9));
            tempList.add(Integer.valueOf(2));
            tempList.add(Integer.valueOf(3));
            tempList.add(Integer.valueOf(8));
            tempList.add(Integer.valueOf(8));
            
            // 불변 리스트로 만듭니다.
            INFO_LIST = Collections.unmodifiableList(tempList);
        }//end of static
        
        /**
         * 전문의 총 길이를 리턴합니다.
         * 
         * @return 전문의 총 길이
         */
        public static final int getTotalSize() {
            return 341;
        }//end of getTotalSize()
        
        /**
         * 시작 인덱스부터 종료 인덱스 까지의 전문 길이를 리턴합니다.
         *
         * @param startIndex 시작 인덱스
         * @param endIndex 종료 인덱스
         * @return 시작 인덱스부터 종료 인덱스 까지의 전문 길이
         */
        public static final int getSize(int startIndex, int endIndex) {
            int strIdx = startIndex - 1;
            int size = 0;
            for (int i = strIdx; i < endIndex; i++) {
                size += ((Integer)INFO_LIST.get(i)).intValue();
            }//end for
            return size;
        }//end of getSize()
        
        /**
         * 시작 인덱스부터 마지막 필드 까지의 전문 길이를 리턴합니다.
         *
         * @param startIndex 시작 인덱스
         * @return 시작 인덱스부터 마지막 필드 까지의 전문 길이
         */
        public static final int getSize(int startIndex) {
            return getSize(startIndex, INFO_LIST.size());
        }//end of getSize()
        
        /**
         * 전문의 특정 필드의 전문 길이를 리턴합니다.
         *
         * @param fieldName 전문 필드명
         * @return 특정 필드의 전문 길이
         */
        public static final int getFieldSize(int field) {
            Integer fieldSize = (Integer)INFO_LIST.get(field);
            return fieldSize.intValue();
        }//end of getSize()
        
    }//end of static-inner-class EAIMessageInfo
    
    /** 
     * 응답 받은 데이터가 에러인지 리턴합니다.
     * 
     * @return 에러 여부
     * @see com.keb.stpa.engine.msg.MessageBase#isError()
     */
    public boolean isError() {
        return !"00000".equals(this.field28);
    }//end of isError()

    /** 
     * 응답 받은 데이터의 에러코드를 리턴합니다.
     * 
     * @return 에러 코드
     * @see com.keb.stpa.engine.msg.MessageBase#getErrorCode()
     */
    public String getErrorCode() {
        return this.field28;
    }//end of getErrorCode()

    /** 
     * 응답 받은 데이터의 에러 메시지를 리턴합니다.
     * 
     * @return 에러 메시지
     * @see com.keb.stpa.engine.msg.MessageBase#getErrorReason()
     */
    public String getErrorReason() {
        return super.getErrorReason("com.keb.stpa.engine.msg.EAIMessage", this.field28);
    }//end of getErrorReason()
    
    /**
     * 대상 source 의 특정 필드를 주어진 데이터로 교체합니다.
     * 
     * @param source byte []  형태의 EAIMessage 데이터
     * @param order 주어진 데이터로 교체하려는 필드의 순번
     * @param replaceData 교체하려는 데이터
     * @return 주어진 데이터로 교체된 source
     */
    public static byte [] setSpecificField(byte [] source, int order, byte [] replaceData) {
        System.arraycopy(replaceData, 0, source, EAIMessageInfo.getSize(1, order - 1), replaceData.length);
        return source;
    }//end of setSpecificField()
    
    /**
     * 주어진 전문의 valid 여부를 체크합니다.
     * 
     * @throws InvalidMessageException 주어진 전문의 필수 항목의 필드들이 세팅되어 있지 않을 경우 발생하는 RuntimeException
     * @see com.keb.stpa.engine.msg.MessageBase#valid()
     */
    public void valid() {    
    }//end of valid()
    
    /**
     * 테스트 코드를 포함하는 메소드 입니다.
     * 
     * @param args 테스트에 필요한 인자 값들
     */
    public static void main(String [] args) {
    }//end of main()
    
}// end of EAIMessage.java