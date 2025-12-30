/*
 * Spider Framework
 *
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 *
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.engine.parser;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang.StringUtils;

import nebsoa.common.collection.DataSet;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.DataMap;
import nebsoa.common.util.EbcdicUtil;
import nebsoa.common.util.FormatUtil;
import nebsoa.common.util.HexUtil;
import nebsoa.common.util.StringUtil;
import nebsoa.spiderlink.engine.message.ByteMessageInstance;
import nebsoa.spiderlink.engine.message.LoopField;
import nebsoa.spiderlink.engine.message.MessageField;
import nebsoa.spiderlink.engine.message.MessageStructure;
import nebsoa.spiderlink.engine.message.MessageValueSetter;
import nebsoa.spiderlink.util.ByteArrayConverter;

/*******************************************************************
 * <pre>
 * 1.설명
 * DataMap의 값을 가지고 Fixed Length 메시지를 생성한다.
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
 * $Log: FixedLengthMessageGenerator.java,v $
 * Revision 1.1  2018/01/15 03:39:49  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:39  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.2  2009/10/01 08:08:19  jglee
 * FIXED DataType 처리 추가
 *
 * Revision 1.1  2008/11/18 11:27:23  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:27  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:51  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:19  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.7  2008/01/03 08:24:48  안경아
 * *** empty log message ***
 *
 * Revision 1.6  2007/12/28 08:55:27  안경아
 * *** empty log message ***
 *
 * Revision 1.5  2007/12/28 05:49:02  안경아
 * *** empty log message ***
 *
 * Revision 1.4  2007/12/17 01:53:12  안경아
 * *** empty log message ***
 *
 * Revision 1.3  2007/12/12 09:02:54  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2007/12/12 08:08:57  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2007/11/26 08:37:59  안경아
 * *** empty log message ***
 *
 * Revision 1.32  2007/07/16 03:43:05  김승희
 * 바이너리 필드, 헥사 필드 로그 처리(임의의 값으로 replace)
 *
 * Revision 1.31  2007/03/16 01:41:44  김승희
 * field type 으로 filler 처리
 *
 * Revision 1.30  2007/03/09 06:26:39  김승희
 * 다양한 filler처리 가능하도록 수정 (헥사값 수용)
 *
 * Revision 1.29  2006/12/21 07:49:48  김성균
 * *** empty log message ***
 *
 * Revision 1.28  2006/11/24 02:50:39  김승희
 * LogManager.info --> LogManager.debug 으로 수정
 *
 * Revision 1.27  2006/11/23 04:04:47  김승희
 * LogManager  로그의 remark 처리
 *
 * Revision 1.26  2006/11/10 05:47:46  김승희
 * import 정리
 *
 * Revision 1.25  2006/10/28 03:49:58  김승희
 * *** empty log message ***
 *
 * Revision 1.24  2006/10/28 03:46:28  김승희
 * *** empty log message ***
 *
 * Revision 1.23  2006/10/28 03:31:26  김승희
 * *** empty log message ***
 *
 * Revision 1.22  2006/10/19 13:59:49  이종원
 * log 수정
 *
 * Revision 1.21  2006/08/30 11:23:41  김승희
 * 디폴트 값을 DataMap에도 넣어주도록 수정
 *
 * Revision 1.20  2006/08/30 05:30:41  김승희
 * loop data의 최대반복 횟수 모를 경우 처리
 *
 * Revision 1.19  2006/07/25 01:39:57  김승희
 * 참조 클래스 패키지 변경
 *
 * Revision 1.18  2006/07/11 06:21:35  김승희
 * *** empty log message ***
 *
 * Revision 1.17  2006/06/26 02:03:50  김승희
 * END LOOP 관련 처리
 *
 * Revision 1.16  2006/06/20 10:25:53  김승희
 * 디버그 로그 추가
 *
 * Revision 1.15  2006/06/19 11:57:36  김승희
 * MessageInstance --> ByteMessageInstance로 수정
 *
 * Revision 1.14  2006/06/17 10:12:09  김승희
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public class FixedLengthMessageGenerator implements MessageGenerator {

	private static FixedLengthMessageGenerator messageGenerator = new FixedLengthMessageGenerator();
	private FixedLengthMessageGenerator(){}
	public static FixedLengthMessageGenerator getInstance(){
		return messageGenerator;
	}
	public ByteMessageInstance generate(MessageStructure structure,
			ByteMessageInstance instance, DataMap dataMap) {

    	int _size=structure.size();
    	//ByteMessageInstance byteInstance = (ByteMessageInstance)instance;
    	DataSet loopDataSet = null;
        for (int index=0; index<_size;index++ ) {
            MessageField field = structure.getField(index);
            if(field==null) continue;

            if(field instanceof LoopField){
            	loopDataSet = (DataSet)dataMap.get(field.getName());
                if(loopDataSet!=null) loopDataSet.beforeFirst();
                instance = makeLoopMessage(instance, (LoopField)field, loopDataSet, dataMap);
            }else
            	instance = makeMessage(instance, field, dataMap);

        }

		return instance;
	}



    private ByteMessageInstance makeMessage(ByteMessageInstance instance, MessageField field, DataMap dataMap){
    	//더미필드와 END LOOP는  마샬하지 않는다.
    	if(MessageField.DUMMY_USE.equals(field.getUseMode())
				|| field.getName().startsWith(MessageField.END_LOOP)) return instance;

        makeMessage(field, instance.getBytes(), instance.getLogData(), instance.getOffset(), dataMap.getString(field.getName()), dataMap);
        instance.setOffset(instance.getOffset()+field.getLength());
        return instance;
    }

    private ByteMessageInstance makeLoopMessage(ByteMessageInstance instance, LoopField field, DataSet loopDataSet, DataMap dataMap){

    	int loopCnt = loopDataSet==null?0:loopDataSet.getRowCount();
    	int maxOccurs= field.getMaxOccurs();
        int remainedLoopCnt =0;

        //_BeginLoop_필드가 더미필드 인 경우 최대 건수(필드의 디폴트값)만큼 looping을 돌면서 marshall한다.
    	if(MessageField.DUMMY_USE.equals(field.getUseMode())){
    		remainedLoopCnt = maxOccurs;

   		//_BeginLoop_필드가 더미필드가 아닌 경우 looping의 실제 크기만큼 looping을 돌면서 marshall한다.
    	}else{

    		makeMessage(field, instance.getBytes(), instance.getLogData(),
                    instance.getOffset(), Integer.toString(loopCnt), dataMap);
            instance.setOffset(instance.getOffset()+field.getLength());
            remainedLoopCnt = loopCnt;

            //최대 반복 횟수를 모를 경우 (_BeginLoop_의 디폴트값이 0또는 null일 때)
            //실제 반복횟수만큼 MessageInstance의 길이를 늘인다.
            int maxLoopCount = 0;
            try{
            	maxLoopCount = StringUtil.isNull(field.getDefaultValue())?0:Integer.parseInt(field.getDefaultValue());
            }catch(Throwable th){
            	//무시한다..
            }
            int extendLength = 0;
            int childLength = 0;
            if(maxLoopCount<=0){
            	for(int i=0, cnt = field.getChildCount(); i<cnt; i++){
            		childLength+=field.getChild(i).getLength();
            	}
            	extendLength = loopCnt*childLength;
            	instance.extendMessageLength(extendLength);
            }

    	}
        if(field.isLogMode()){
            LogManager.debug("Loop_offset:"+instance.getOffset()
                +","+field.getName()+": 실제 데이터 크기 " + loopCnt
                +"번 반복 수행 (디폴트 반복 회수:" + +remainedLoopCnt+")");
        }


        ArrayList nestedFieldList = field.getChildList();
        MessageField nestedField = null;
        DataSet nestedLoopDataSet = null;
        if(loopDataSet!=null){
        	loopDataSet.beforeFirst();

        	while(loopDataSet.next()){
        		remainedLoopCnt--;
        		for(int i=0; i<nestedFieldList.size(); i++){
        			nestedField = (MessageField)nestedFieldList.get(i);

        			//loop 안의 loop 처리
        			if(nestedField instanceof LoopField){

        				nestedLoopDataSet = (DataSet)loopDataSet.getObject(nestedField.getName());
        				makeLoopMessage(instance, (LoopField)nestedField, nestedLoopDataSet, dataMap);

        			}else{
        				makeMessage(nestedField, instance.getBytes(), instance.getLogData(), instance.getOffset(), loopDataSet.getString(nestedField.getName()), dataMap);
                        instance.setOffset(instance.getOffset()+nestedField.getLength());
        			}

        		}
        	}
        	loopDataSet.beforeFirst();
        }
        //남은 looping count 만큼 filler를 채워 marshall한다.
        int j=0;
        for( ; j<remainedLoopCnt; j++){
        	for(int i=0; i<nestedFieldList.size(); i++){
    			nestedField = (MessageField)nestedFieldList.get(i);

    			//loop 안의 loop 처리
    			if(nestedField instanceof LoopField){

    				nestedLoopDataSet = null;
    				makeLoopMessage(instance, (LoopField)nestedField, nestedLoopDataSet, dataMap);

    			}else{
    				makeMessage(nestedField, instance.getBytes(), instance.getLogData(), instance.getOffset(), null, dataMap);
                    instance.setOffset(instance.getOffset()+nestedField.getLength());
    			}

    		}
        }

        if(field.isLogMode()){
            LogManager.debug("END Loop , 수행 Loop횟수 :"+j);
        }
        return instance;
    }

    /**
     * 전문을 해석하여 데이타를 추출 합니다.
     * @param dataMap
     * @param buf
     */
    protected void makeMessage(MessageField field, byte[] inBuf, byte[] logBuf, int offSet, String value, DataMap dataMap){

    	//시스템 입력값, 디폴트 값 처리
    	//입력값 검사에서 이미 dataMap에 상위 전문까지의 디폴트값을 가지고 있으므로 주석처리..
    	//value = MessageValueSetter.getDefaultValue(field, value, dataMap);

        makeMessageByFieldType(field, inBuf, logBuf, offSet, value);
    }

	private byte[] makeMessageByFieldType(MessageField field, byte[] inBuf, byte[] logBuf, int offSet, String value) {
		byte [] valueArray = null;
		if (MessageField.CHR_FIELD.equals(field.getType())) {

			//filler가 빈공백 또는 !이면.. (기존 개발 사이트에 영향을 주지 않기 위해 별도 처리)
			//모니터링 이후 빠져야할 부분..
			//if(field.getFiller()==' ' || field.getFiller()=='!' || field.getFiller()=='0'){

			//filler가 char로 표현된 경우..
			if(field.getFillerType().equals(MessageField.CHR_FIELD)){

				if(MessageField.RIGHT_ALIGN.equals(field.getAlign())){
	            	value = FormatUtil.lPadding(value, field.getLength(), field.getFiller());
	            //char 타입인 경우 디폴트로 Left 정렬로 간주한다.
	            }else{
	            	value = FormatUtil.rPadding(value, field.getLength(), field.getFiller());
	            }

	            valueArray = value.getBytes();

	            // 한글때문에 길이만큼 패딩처리 되지 않았을 경우 한번 더 패딩한다.
	            if (valueArray.length < field.getLength()) {
		            if(MessageField.RIGHT_ALIGN.equals(field.getAlign())){
		            	value = FormatUtil.lPadding(value, field.getLength(), field.getFiller());
		            }else{
		            	value = FormatUtil.rPadding(value, field.getLength(), field.getFiller());
		            }
		            valueArray = value.getBytes();
	            }

	            if(field.isLogMode()){
	                if(!StringUtil.isNull(field.getRemark()))
	                	 LogManager.debug(field.getName()+" [****]");
	                else LogManager.debug(field.getName()+" [" + value +"]");
	            }

	            System.arraycopy(valueArray, 0, inBuf, offSet, field.getLength());

	        //filler가 hexa로 표현된 경우
			}else{

				//로깅
	            if(field.isLogMode()){
	                if(!StringUtil.isNull(field.getRemark()))
	                	 LogManager.debug(field.getName()+" [****]");
	                else LogManager.debug(field.getName()+" [" + value +"]");
	            }

				valueArray = value.getBytes();

				//필드에 입력된 값이 필드 길이와 같거나 클 때
				if(valueArray.length>=field.getLength()){
					System.arraycopy(valueArray, 0, inBuf, offSet, field.getLength());

				//필드 길이보다 입력값이 작을 때(정렬 방식에 따라 filler를 채운다.)
				}else{

                    //오른쪽 정렬
					if(MessageField.RIGHT_ALIGN.equals(field.getAlign())){

						System.arraycopy(valueArray, 0, inBuf, offSet + (field.getLength() - valueArray.length), valueArray.length);
						Arrays.fill(inBuf, offSet, offSet + (field.getLength() - valueArray.length), (byte)field.getFiller());

					//왼쪽 정렬
					}else{
						System.arraycopy(valueArray, 0, inBuf, offSet, valueArray.length);
						Arrays.fill(inBuf, valueArray.length+offSet, offSet+field.getLength(), (byte)field.getFiller());
					}

				}
			}

        } else if (MessageField.NUM_FIELD.equals(field.getType())) {
            if(StringUtil.isNull(value)){
                value="0";
            }
            //1. scale 처리
            String[] splitValue = StringUtil.splitDouble(value);
            splitValue[1] = FormatUtil.rPadding(splitValue[1], field.getScale(), '0');
            value =  (splitValue[0].equals("0")?"":splitValue[0]) + splitValue[1];

            //2. filler처리
            if(MessageField.LEFT_ALIGN.equals(field.getAlign())){
            	value=FormatUtil.rPadding(value, field.getLength(), field.getFiller());
            }else{
            	value=FormatUtil.lPadding(value, field.getLength(), field.getFiller());
            }
            if(field.isLogMode()){
                if(!StringUtil.isNull(field.getRemark()))
                	 LogManager.debug(field.getName()+" [****]");
                else LogManager.debug(field.getName()+" [" + value +"]");
            }
            valueArray = value.getBytes();
            System.arraycopy(valueArray, 0, inBuf, offSet, field.getLength());

        }else if (MessageField.HEXA_FIELD.equals(field.getType())) {
            if(field.isLogMode()){
                LogManager.debug(field.getName()+" [" + value +"]");
            }
            //TODO 헥사 처리
            valueArray = HexUtil.parseBytes(value, 16) ;
            System.arraycopy(valueArray, 0, inBuf, offSet, field.getLength());
        }else if (MessageField.BINARY_FIELD.equals(field.getType())) {
            if(StringUtil.isNull(value)){
                value="0";
            }
            if(field.isLogMode()){
                LogManager.debug(field.getName()+" [BINARY_FIELD:" + value +"]");
            }

            if(field.getLength()==4){
                ByteArrayConverter.setInt(inBuf,offSet,Integer.parseInt(value));
            }else if(field.getLength()==2){
                ByteArrayConverter.setShort(inBuf,offSet,Short.parseShort(value));
            }else if(field.getLength()==8){
                ByteArrayConverter.setLong(inBuf,offSet,Long.parseLong(value));
            }
            //EBCDIC 문자 처리
        }else if (MessageField.KOREAN_FIELD.equals(field.getType())) {

			if(MessageField.RIGHT_ALIGN.equals(field.getAlign())){
            	value = FormatUtil.lPadding(value, field.getLength(), field.getFiller());
            //char 타입인 경우 디폴트로 Left 정렬로 간주한다.
            }else{
            	value = FormatUtil.rPadding(value, field.getLength(), field.getFiller());
            }

            valueArray = value.getBytes();

            // 한글때문에 길이만큼 패딩처리 되지 않았을 경우 한번 더 패딩한다.
            if (valueArray.length < field.getLength()) {
            	//value =  StringUtil.convertASCIIFromEBCDIC(value);
	            if(MessageField.RIGHT_ALIGN.equals(field.getAlign())){
	            	value = FormatUtil.lPadding(value, field.getLength(), field.getFiller());
	            }else{
	            	value = FormatUtil.rPadding(value, field.getLength(), field.getFiller());
	            }
//	            value = StringUtil.convertEBCDICFromASCII(value);
//	            valueArray = value.getBytes();
            }
			//반각->전각으로
            value = StringUtil.getDBCS(value);
            valueArray=value.getBytes();

            if(field.isLogMode()){
                if(!StringUtil.isNull(field.getRemark()))
                	 LogManager.debug(field.getName()+" [****]");
                else LogManager.debug(field.getName()+" [" + value +"]");
            }

            byte[] spaceByte = new byte[]{0x20, 0x20};
            System.arraycopy(valueArray, 0, inBuf, offSet, field.getLength()-2);
            //끝에 2byte는 공백으로
            System.arraycopy(spaceByte, 0, inBuf, (offSet+field.getLength()-2), 2);

        // FIXED 속성 추가 2009-10-01 이종건
        }else if (MessageField.FIXED_FIELD.equals(field.getType())) {

			if(MessageField.RIGHT_ALIGN.equals(field.getAlign())){
            	value = FormatUtil.lPadding(value, field.getLength(), field.getFiller());
            //char 타입인 경우 디폴트로 Left 정렬로 간주한다.
            }else{
            	value = FormatUtil.rPadding(value, field.getLength(), field.getFiller());
            }
            valueArray = value.getBytes();
            // 한글때문에 길이만큼 패딩처리 되지 않았을 경우 한번 더 패딩한다.
            if (valueArray.length < field.getLength()) {
	            if(MessageField.RIGHT_ALIGN.equals(field.getAlign())){
	            	value = FormatUtil.lPadding(value, field.getLength(), field.getFiller());
	            }else{
	            	value = FormatUtil.rPadding(value, field.getLength(), field.getFiller());
	            }
	            valueArray = value.getBytes();
            }
            if(field.isLogMode()){
                if(!StringUtil.isNull(field.getRemark()))
                	 LogManager.fwkDebug(field.getName()+" [****]");
                else LogManager.fwkDebug(field.getName()+" [" + value +"]");
            }
            System.arraycopy(valueArray, 0, inBuf, offSet, field.getLength());

        }
		//로그 data
		//remark가 없으면
		try{
			if(MessageField.BINARY_FIELD.equals(field.getType())){
				//do noting..
				//BINARY_FIELD는 로그 클래스에서 임의의 값을 넣는다.
				valueArray = StringUtils.repeat("0", field.getLength()).getBytes();
				System.arraycopy(valueArray, 0, logBuf, offSet, field.getLength());
			}else if(MessageField.HEXA_FIELD.equals(field.getType())){
				valueArray = new byte[field.getLength()];
				Arrays.fill(valueArray, (byte)0x30);
				System.arraycopy(valueArray, 0, logBuf, offSet, field.getLength());
			}else if(MessageField.KOREAN_FIELD.equals(field.getType())){
				System.arraycopy(valueArray, 0, logBuf, offSet, field.getLength()-2);
				System.arraycopy(new byte[]{0x20, 0x20}, 0, logBuf, (offSet+field.getLength()-2), 2);
			}else if(StringUtil.isNull(field.getRemark())){
				System.arraycopy(valueArray, 0, logBuf, offSet, field.getLength());
			}else{
				//remark가 있으면 길이만큼 remark를 쓴다.
				valueArray = StringUtils.repeat(field.getRemark(), field.getLength()).getBytes();
				System.arraycopy(valueArray, 0, logBuf, offSet, field.getLength());
			}
		}catch(Throwable th){
			//로그 데이터 생성 실패가 전문 처리에 영향을 주지 않도록 한다.
		}
        return inBuf;
	}

	public static void main(String[] args){
		byte[] valueArr = "한글입니다.".getBytes();
		byte[] inBuf = new byte[8];
		byte[] zero = new byte[]{0x20, 0x20};
		System.arraycopy(valueArr, 0, inBuf, 0, 6);
		System.arraycopy(zero, 0, inBuf, 6, 2);
		System.out.println("'"+new String(inBuf)+"'");

	}
}
