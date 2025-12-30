/*
 * Spider Framework
 *
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 *
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.engine.message;

import java.io.Serializable;

import nebsoa.common.util.HexUtil;
import nebsoa.common.util.StringUtil;

/*******************************************************************
 * <pre>
 * 1.설명
 * 메시지 필드를 나타내는 클래스
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
 * $Log: MessageField.java,v $
 * Revision 1.1  2018/01/15 03:39:48  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:16  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:50  yshong
 * *** empty log message ***
 *
 * Revision 1.2  2009/10/01 08:08:19  jglee
 * FIXED DataType 처리 추가
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
 * Revision 1.4  2007/12/28 05:49:02  안경아
 * *** empty log message ***
 *
 * Revision 1.3  2007/12/18 08:30:51  김승희
 * CODE_MAPPING_YN 필드 추가
 *
 * Revision 1.2  2007/12/12 08:08:58  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2007/11/26 08:38:05  안경아
 * *** empty log message ***
 *
 * Revision 1.28  2007/03/16 01:41:23  김승희
 * field type 추가
 *
 * Revision 1.27  2007/03/09 06:04:51  김승희
 * field type 추가
 *
 * Revision 1.26  2007/03/02 11:59:38  김승희
 * filler 에 0x로 시작되는 문자열이 들어온 경우 헥사 값으로 치환해서 저장
 *
 * Revision 1.25  2006/10/19 13:34:18  이종원
 * field log mode 로딩 추가
 *
 * Revision 1.24  2006/07/25 01:42:03  김승희
 * import 정리
 *
 * Revision 1.23  2006/06/20 12:24:26  김승희
 * required_yn 속성 추가
 *
 * Revision 1.22  2006/06/17 10:12:09  김승희
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public class MessageField implements Serializable {
	/**
     * 현재 이 필드가 loop필드 안에 들어 간 필드 인지 여부
	 */
    boolean isNested;
    /*
     * 현재 이 필드가 포함 된  loop필드
     */
    LoopField parent;

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -1621986026878735226L;
    /**
     * loop의 시작을 알리는 항목의 key값
     */
    public static final String BEGIN_LOOP = "_BeginLoop_";

    /**
     * loop의 끝을  알리는 항목의 key값
     */
    public static final String END_LOOP = "_EndLoop_";

    /**
     * 문자열
	 */
	public static final String CHR_FIELD = "C";

    /**
     * EBCDIC 문자열
	 */
	public static final String KOREAN_FIELD = "K";

    /**
     * 숫자
     */
	public static final String NUM_FIELD = "N";
    /**
     * BINARY 숫자
     */
    public static final String BINARY_FIELD = "B";
    /**
     * HEXA
     */
    public static final String HEXA_FIELD = "H";

    /**
     * FIXED
     */
    public static final String FIXED_FIELD = "F";


    /**
     * USE_MODE - 시스템
     */
    public static final String SYSTEM_USE = "S";
    /**
     * USE_MODE - 유저
     */
    public static final String USER_USE = "U";
    /**
     * USE_MODE - 더미
     */
    public static final String DUMMY_USE = "D";

    /**
     * ALIGN - 왼쪽 정렬
     */
    public static final String LEFT_ALIGN = "L";

    /**
     * ALIGN - 오른쪽 정렬
     */
    public static final String RIGHT_ALIGN = "R";

    /**
     * 필드 타입 - XML CDATA
     */
    public static final String CDATA = "CDATA";

	public static final String sysEncoding = System.getProperty("file.encoding");


    protected String name;
    protected String type; //Data Type [C,N,B,H 따위]
    protected int scale;
    protected int length;
    protected int sortOrder;
	protected int offset;
    protected String defaultValue;
    protected String testValue;
    protected String remark;
    protected char filler;
    protected String fillerType;
	protected String useMode;
	protected String align;
	protected String codeGroup;
	protected String fieldTag;
	protected boolean required;
    private boolean logMode; //log여부
    protected String fieldType;
    protected boolean isCodeMapping; //코드변환여부

	public MessageField(String name, String dataType, String fieldType, int length, int scale, int sortOrder, int offset, String useMode, String align,
            String codeGroup, String fieldTag, boolean required,boolean logMode, boolean isCodeMapping) {
		this(name,dataType,fieldType,length,scale,sortOrder,offset,null,null,null,null, useMode, align, codeGroup, fieldTag, required, logMode,isCodeMapping);
	}//end of construcctor

	public MessageField(String name, String dataType, String fieldType, int length, int scale, int sortOrder, int offset,
            String defaultValue, String testValue, String remark,String _filler, String useMode, String align,
            String codeGroup, String fieldTag, boolean required,boolean logMode, boolean isCodeMapping) {
        this.sortOrder = sortOrder;
        this.length = length;
        this.name = name;
        this.offset = offset;
        this.scale = scale;
        this.type = dataType;
        this.fieldType = fieldType;
        this.defaultValue=defaultValue;
        this.testValue=testValue;
        this.remark=remark;
        this.useMode = useMode;
        this.align = align;
        this.codeGroup = codeGroup;
        this.fieldTag = fieldTag;
        this.required = required;
        this.logMode = logMode;
        this.isCodeMapping = isCodeMapping;
        setFiller(_filler);
    }

    public String toString(){
        if(isNested)
            return "\n\tsortOrder["+sortOrder+"]Name["+name+"] length["+length+"]offset["+offset+"]";
        else
            return "sortOrder["+sortOrder+"]Name["+name+"] length["+length+"]offset["+offset+"]";
    }

	/**
     * 별표로 리마크 해야 하는 필드인지 여부
     * @return
	 */
    public boolean isSecretField(){
        return "*".equals(remark);
    }
    /**
	 * sortOrder 의 값을 리턴합니다.
	 *
	 * @return sortOrder 의 값
	 */
	public int getSortOrder() {
		return sortOrder;
	}

	/**
	 * length 의 값을 리턴합니다.
	 *
	 * @return length 의 값
	 */
	public int getLength() {
		return length;
	}

	/**
	 * name 의 값을 리턴합니다.
	 *
	 * @return name 의 값
	 */
	public String getName() {
		return name;
	}

	/**
	 * offset 의 값을 리턴합니다.
	 *
	 * @return offset 의 값
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * scale 의 값을 리턴합니다.
	 *
	 * @return scale 의 값
	 */
	public int getScale() {
		return scale;
	}

	/**
	 * type 의 값을 리턴합니다.
	 *
	 * @return type 의 값
	 */
	public String getType() {
		return type;
	}

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTestValue() {
        return testValue;
    }

    public void setTestValue(String testValue) {
        this.testValue = testValue;
    }

    public void setSortOrder(int index) {
        this.sortOrder = index;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isNested() {
        return isNested;
    }

    public void setNested(boolean isNested) {
        this.isNested = isNested;
    }

    public LoopField getParent() {
        return parent;
    }

    public void setParent(LoopField parent) {
        if(parent != null){
            setNested(true);
            this.parent = parent;
        }
    }

    public char getFiller() {
        return filler;
    }

    public void setFiller(String _filler) {

        if(_filler == null || _filler.length()==0 || _filler.equals("null")){
            if (MessageField.CHR_FIELD.equals(getType())) {
                filler=' ';
            }else if (MessageField.NUM_FIELD.equals(getType())) {
                filler='0';
            }else if (MessageField.BINARY_FIELD.equals(getType())) {
                filler='0';
            }else if (MessageField.HEXA_FIELD.equals(getType())) {
                filler='0';
            }else{
                filler=' ';
            }
            //filler가 char로 표현된 것인지 헥사로 표현된 것인지 구분하기 위한 값
            fillerType = CHR_FIELD;

        //filler가 헥사값으로 표현된 경우(0x로 시작해야한다.)
        }else if(_filler.startsWith("0x") || _filler.startsWith("0X")){
        	this.filler = (char)HexUtil.parseInt(_filler, 2, 2, 16);
        	//filler가 char로 표현된 것인지 헥사로 표현된 것인지 구분하기 위한 값
        	fillerType = HEXA_FIELD;

        }else{
            this.filler = _filler.charAt(0);
            //filler가 char로 표현된 것인지 헥사로 표현된 것인지 구분하기 위한 값
            fillerType = CHR_FIELD;

        }
    }


	public String getUseMode() {
		return useMode;
	}

	public void setUseMode(String useMode) {
		this.useMode = useMode;
	}

	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public String getCodeGroup() {
		return codeGroup;
	}

	public void setCodeGroup(String codeGroup) {
		this.codeGroup = codeGroup;
	}

	public String getFieldTag() {
		return fieldTag;
	}

	public void setFieldTag(String fieldTag) {
		this.fieldTag = fieldTag;
	}

	/**
	 * @return 코드 값이 들어가는 필드인지 여부를 리턴한다.
	 */
	public boolean isCodeField(){
		return !StringUtil.isNull(this.getCodeGroup());
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

    public boolean isLogMode() {
        return logMode;
    }

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public String getFillerType() {
		return fillerType;
	}

	public void setFillerType(String fillerType) {
		this.fillerType = fillerType;
	}

	public boolean isCodeMapping() {
		return isCodeMapping;
	}

	public void setCodeMapping(boolean isCodeMapping) {
		this.isCodeMapping = isCodeMapping;
	}


}// end of MessageField.java