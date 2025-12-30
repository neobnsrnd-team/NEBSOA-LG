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

import org.apache.commons.lang.StringUtils;

import nebsoa.common.collection.DataSet;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.DataMap;
import nebsoa.common.util.EbcdicUtil;
import nebsoa.common.util.FormatUtil;
import nebsoa.common.util.HexUtil;
import nebsoa.common.util.PropertyManager;
import nebsoa.common.util.StringUtil;
import nebsoa.spiderlink.engine.message.ByteMessageInstance;
import nebsoa.spiderlink.engine.message.LoopField;
import nebsoa.spiderlink.engine.message.MessageConstants;
import nebsoa.spiderlink.engine.message.MessageField;
import nebsoa.spiderlink.engine.message.MessageParseException;
import nebsoa.spiderlink.engine.message.MessageStructure;
import nebsoa.spiderlink.engine.message.SystemValueKeywordPool;
import nebsoa.spiderlink.util.ByteArrayConverter;

/*******************************************************************
 * <pre>
 * 1.설명
 * Fixed Length 메시지를 DataMap으로 변환한다.
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
 * $Log: FixedLengthMessageParser.java,v $
 * Revision 1.1  2018/01/15 03:39:49  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:38  cvs
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
 * Revision 1.2  2008/05/08 11:20:07  오재훈
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:19  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.11  2008/01/10 05:45:44  안경아
 * *** empty log message ***
 *
 * Revision 1.10  2008/01/10 05:27:22  안경아
 * *** empty log message ***
 *
 * Revision 1.9  2008/01/10 03:25:42  안경아
 * *** empty log message ***
 *
 * Revision 1.8  2008/01/09 09:59:28  안경아
 * 루프count를 얻어오기 위하여 변경
 *
 * Revision 1.7  2007/12/28 07:00:02  안경아
 * *** empty log message ***
 *
 * Revision 1.6  2007/12/28 05:49:02  안경아
 * *** empty log message ***
 *
 * Revision 1.5  2007/12/26 11:30:26  안경아
 * *** empty log message ***
 *
 * Revision 1.4  2007/12/24 09:00:59  안경아
 * *** empty log message ***
 *
 * Revision 1.3  2007/12/17 01:53:12  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2007/12/12 08:08:56  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2007/11/26 08:38:01  안경아
 * *** empty log message ***
 *
 * Revision 1.26  2007/01/22 06:21:30  안경아
 * *** empty log message ***
 *
 * Revision 1.25  2007/01/22 06:13:28  안경아
 * *** empty log message ***
 *
 * Revision 1.24  2007/01/22 05:29:43  김승희
 * _BeginLoop_가 사이즈가 0인 경우 처리
 *
 * Revision 1.23  2006/11/24 02:50:39  김승희
 * LogManager.info --> LogManager.debug 으로 수정
 *
 * Revision 1.22  2006/11/23 08:21:23  김승희
 * 수신 데이터 remark 처리를 위한 변경
 *
 * Revision 1.21  2006/11/23 04:04:47  김승희
 * LogManager  로그의 remark 처리
 *
 * Revision 1.20  2006/10/19 14:02:00  이종원
 * *** empty log message ***
 *
 * Revision 1.19  2006/10/19 13:48:09  이종원
 * log Mode에 따라 로깅 처리 부분 수정
 *
 * Revision 1.18  2006/10/19 13:34:36  이종원
 * *** empty log message ***
 *
 * Revision 1.17  2006/09/12 04:58:52  김승희
 * 마지막 필드의 자리수 모자르게 들어오는 경우 처리
 *
 * Revision 1.16  2006/08/25 05:18:08  김승희
 * 전체 메시지 길이가 정의된 필드 길이보다 작을 때 break 처리
 * (instance의 offset이 length보다 같거나 클 때 처리)
 *
 * Revision 1.15  2006/07/25 01:39:57  김승희
 * 참조 클래스 패키지 변경
 *
 * Revision 1.14  2006/07/21 06:03:46  김승희
 * import 정리
 *
 * Revision 1.13  2006/07/05 07:03:31  김승희
 * 주석 수정
 *
 * Revision 1.12  2006/06/26 11:31:10  김승희
 * number 타입의 경우 trim()
 *
 * Revision 1.11  2006/06/19 11:57:36  김승희
 * MessageInstance --> ByteMessageInstance로 수정
 *
 * Revision 1.10  2006/06/17 10:12:09  김승희
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public class FixedLengthMessageParser implements MessageParser {

	private static FixedLengthMessageParser messageParser = new FixedLengthMessageParser();
	private FixedLengthMessageParser(){}
	public static FixedLengthMessageParser getInstance(){
		return messageParser;
	}

	public DataMap parse(MessageStructure structure, ByteMessageInstance instance, DataMap dataMap) {

      int _size=structure.size();
      //ByteMessageInstance byteMessageInstance = (ByteMessageInstance)instance;

      MessageField field = null;
      Object value = null;
      for (int index=0; index<_size;index++ ) {
    	  //실제 정의된 필드 길이만큼 전문이 들어오지 않을 때를 대비한 로직
    	  if(instance.getBytes().length <= instance.getOffset()){
              LogManager.debug(structure.getMessageId()+"::메세지의 끝을 만나 break합니다.데이타 길이부 :"
                      +instance.getBytes().length+",OFFSET:"+instance.getOffset());
              break;
          }

          field = structure.getField(index);
          if(field==null) continue;

          if(field instanceof LoopField)
        	  value = parseLoopField(instance, (LoopField)field, dataMap);
          else {

        	  value = parseField(instance, field, dataMap);
          }

          if(MessageField.USER_USE.equals(field.getUseMode()) || field instanceof LoopField){
        	  DataMap userMap = (DataMap)dataMap.get(MessageConstants.USER_DATAMAP);
        	  if(userMap != null){
        		  userMap.put(field.getName(), value);
            	  dataMap.put(MessageConstants.USER_DATAMAP, userMap);
        	  }
          }

          dataMap.put(field.getName(), value);
      }

      return dataMap;

    }

    private Object parseField(ByteMessageInstance instance, MessageField field, DataMap dataMap){

        Object value = parseByteField(field, instance.getBytes(), instance.getOffset());

        String remarkChar = field.getRemark();
        if(!StringUtil.isNull(remarkChar)){
        	instance.setBytes(StringUtils.repeat(remarkChar, field.getLength()).getBytes(),
        					  instance.getOffset(), field.getLength());
        }

        instance.setOffset(instance.getOffset()+field.getLength());

        if(field.isLogMode()){
        	if(!StringUtil.isNull(remarkChar))
        		 LogManager.debug("현재 offset:"+instance.getOffset()+","+field.getName()+":****");
        	else LogManager.debug("현재 offset:"+instance.getOffset()+","+field.getName()+":"+value);
        }
        return value;
    }

    private Object parseLoopField(ByteMessageInstance instance, LoopField field, DataMap dataMap){

    	//나 자신을 먼저 처리 하고...
    	Object loopingCount = null;

//    	_BeginLoop_가 사이즈가 0인 경우는
		if(field.getLength()==0){
			//maxOccurs가 0이고 default value가 0이 아니면 (즉 최대반복건수를 개발자가 0으로 세팅하지 않고 필드명을 세팅한 경우 )
			//default value로 적힌 필드명으로 반복 횟수로 간주한다.
			if(field.getMaxOccurs()==0 && !StringUtil.isNull(field.getDefaultValue())
					&& !"0".equals(field.getDefaultValue())){
				loopingCount = dataMap.getString(field.getDefaultValue());
				LogManager.debug("반복회수를 ["+field.getDefaultValue()+"]의 value["+loopingCount+"]로 간주합니다.");
			//dataType이 문자열이고 maxOccurs가 0보다 크면 maxOccurs를 반복횟수로 간주
			}else{
				loopingCount = Integer.toString(field.getMaxOccurs());
				LogManager.debug("반복회수를 최대 반복건수 ["+loopingCount+"]로 간주합니다.");
			}
//		_BeginLoop_가 사이즈를 갖는다면 해당 필드의 값이 반복횟수이다.
		}else{
			loopingCount = parseByteField(field, instance.getBytes(), instance.getOffset());
			instance.setOffset(instance.getOffset()+field.getLength());
		}



        if(field.isLogMode()){
            LogManager.debug("Loop_offset:"+instance.getOffset()+","+field.getName()
                    +":"+loopingCount+"번 반복 수행 시작");
        }

        //TODO childDataCount를 전문 값으로 바로 알 수 없을 때 처리 필요...
        int childDataCount= Integer.parseInt((String) loopingCount);
        MessageField nestedField = null;

        String[] childFieldNames = new String[field.getChildCount()];
        ArrayList loopFieldList = new ArrayList(childDataCount);
        Object[] row = null;
        int i=0;
        for( ;i<childDataCount;i++){

        	//실제 loop count 만큼 데이터가 들어오지 않을 때를 대비한 로직
        	if(instance.getBytes().length <= instance.getOffset()){
                LogManager.debug("메세지의 끝을 만나 break합니다.데이타 길이부 :"
                        +instance.getBytes().length+",OFFSET:"+instance.getOffset());
                break;
            }
        	if(field.isLogMode()){
        	    LogManager.debug("["+(i+1)+"]번째 루프 parsing");
            }
            row = new Object[field.getChildCount()];
            loopFieldList.add(row);
            for(int j=0;j<field.getChildCount();j++){
                if(field.isLogMode()){
                    LogManager.debug("["+(i+1)+"]["+(j+1)+"]번째 필드 parsing");
                }
                nestedField = field.getChild(j);
                if(i==0){
                    childFieldNames[j] = nestedField.getName();
                }

                if(nestedField instanceof LoopField){
                	row[j] = parseLoopField(instance, (LoopField)nestedField, dataMap);
                }else{
                	//실제 정의된 필드 길이만큼 전문이 들어오지 않을 때를 대비한 로직
              	    if(instance.getBytes().length <instance.getOffset()){
                        LogManager.debug("["+(i+1)+"]번째 루프 parsing 중 메세지의 끝을 만나 break합니다.데이타 길이부 :"
                                +instance.getBytes().length+",OFFSET:"+instance.getOffset());
                        continue;
                    }
                	row[j] = parseField(instance, nestedField, dataMap);
                }

            }
            if(field.isLogMode()){
                LogManager.debug("["+(i+1)+"]번째 루프 끝 ==================");
            }
        }
        DataSet loopDataSet = new DataSet(childFieldNames, loopFieldList);
        if(field.isLogMode()){
            LogManager.debug("END Loop_offset:"+instance.getOffset()+","+field.getName()
                    +",예상Loop:"+loopingCount+",실제수행Loop:"+i);
        }

        return loopDataSet;
    }

    public Object parseByteField(MessageField field, byte[] inBuf, int offSet){

      //필드 길이
      int fieldLength = field.getLength();

      //전문 마지막 필드의 길이가 실제 정의된 것 보다 작게 들어올 경우 대비 : 실제 들어온 길이를 필드 길이로 삼는다.
      if((inBuf.length - offSet)<field.getLength()){
    	  LogManager.debug("예상보다 작은 길이 전문 수신 >>> 원래 필드 길이 : "
                  + fieldLength + ", 실제 남은 길이 : " + (inBuf.length - offSet));
    	  fieldLength = inBuf.length - offSet;
      }

      String value=null;

      if (MessageField.CHR_FIELD.equals(field.getType())) {
          //LogManager.debug("CHAR FIELD PARSING:["+field.getName()+"]");

          value = FormatUtil.trim(new String(inBuf, offSet, fieldLength));
          //filler 제거
          if(MessageField.RIGHT_ALIGN.equals(field.getAlign())){
        	  value = StringUtil.removeLeftFiller(value, field.getFiller());
          }else{
        	  value = StringUtil.removeRightFiller(value, field.getFiller());
          }
      } else if (MessageField.NUM_FIELD.equals(field.getType())) {
          //LogManager.debug("NUMBER FIELD PARSING:["+field.getName()+"]");
          //1. scale 처리
          if(field.getScale()==0){
              value = new String(inBuf, offSet, fieldLength);
          }else if(field.getScale()>0){
              StringBuffer buffer=new StringBuffer();
              buffer.setLength(0);
              buffer.append(new String(inBuf, offSet, fieldLength-field.getScale()));
              buffer.append(".");
              buffer.append(new String(inBuf, offSet+fieldLength-field.getScale(), field.getScale()));
              value=buffer.toString();
          }else{
        	  throw new MessageParseException("필드의 scale 값은  0보다 작을 수 없습니다. [name=" + field.getName() +", scale=" + field.getScale() +"]");
          }
          value = value.trim();
          //2. filler 제거
          //성능을 위해 filler가 0일 때는 제거하지 않는다.(DataMap에서 데이터 추출시 제거할 수 있음)
          //2008-05-08 수정 오재훈 왼쪽 정렬에 필러가 0이면 Exception 발생
//          if(field.getFiller()!='0'){
          if(MessageField.LEFT_ALIGN.equals(field.getAlign())){
        	  if(field.getFiller()=='0'){
        		  throw new MessageParseException("숫자 필드의 왼쪽 정렬 방식에 삽입문자는 0이 될수 없습니다. 전문 등록 정보를 확인하여 주십시요. [name=" + field.getName());
        	  }
        	  value = StringUtil.removeRightFiller(value, field.getFiller());
          }else{//오른쪽 정렬일 경우
        	  //데이타가 소수점이 있는 데이타일 수도 있으므로 필러가 0이 아니면 왼쪽 필러 제거
        	  if(field.getFiller()!='0'){
        		  value = StringUtil.removeLeftFiller(value, field.getFiller());
        	  }
        	  //2008-05-08 오재훈 spiderlink.properties.xml에서 NUMERIC_UNPADDING_YN 값을 읽어서 true이면 왼쪽의 0을 제거함. 디폴트 false
        	  if(PropertyManager.getBooleanProperty("spiderlink","NUMERIC_UNPADDING_YN")){
        		  value = StringUtil.parseNumberType(value);
        	  }
          }
//          }

      }else if (MessageField.HEXA_FIELD.equals(field.getType())) {

          //TODO 헥사 처리
          value=HexUtil.toHexString(inBuf,offSet,fieldLength);
          //LogManager.debug("HEXA FIELD PARSING:["+field.getName()+"]["+value+"]");

      }else if (MessageField.BINARY_FIELD.equals(field.getType())) {
          //LogManager.debug("BINARY FIELD PARSING:["+field.getName()+"]");
          try {
              if(fieldLength==4){
                  value=ByteArrayConverter.getInt(inBuf,offSet)+"";
              }else if (fieldLength==2){
                  value=ByteArrayConverter.getShort(inBuf,offSet)+"";
              }
          } catch (Exception e) {
              e.printStackTrace();
              return "0";
          }
      }else if (MessageField.FIXED_FIELD.equals(field.getType())) {//2009-10-01 이종건 추가

    	  //전문 파싱시 field type이 fixed면 공백 및 filler값을 삭제하지 않고 전문 내용을 그대로 파싱
    	  value = new String(inBuf, offSet, fieldLength);
      }else if (MessageField.KOREAN_FIELD.equals(field.getType())) {
    	  byte[] ebcArr = new byte[fieldLength];
    	  System.arraycopy(inBuf, offSet, ebcArr, 0, fieldLength);
    	  //전각을 반각으로..
          value =StringUtil.convertDBCS(new String(ebcArr));
      }

      return value;
   }
    public static void main(String[] args){
        String s = "00000344434111";
        byte[] inBuf = s.getBytes();
        StringBuffer buffer=new StringBuffer();
        buffer.setLength(0);
        buffer.append( new String(inBuf, 0, inBuf.length - 2) );
        buffer.append(".");
        buffer.append(new String(inBuf, 0+inBuf.length-2, 2));
        String value=buffer.toString();
        System.out.println(value);
        //2. filler 제거
        if(MessageField.LEFT_ALIGN.equals("R")){
      	  value = StringUtil.removeRightFiller(value, '0');
        }else{
      	  value = StringUtil.removeLeftFiller(value, '0');
        }
        System.out.println(value);
    }

 }
