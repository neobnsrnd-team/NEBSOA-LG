package test.spiderlink.message;

/**
 *
 * <p>Title: EAI관련 상수들을 정의한 클래스(인터페이스) 입니다.</p>
 * <p>Description: 상수들을 정의한 클래스(인터페이스) 입니다.</p>
 * <p>Copyright:Comas.co.kr Copyright (c) 2004</p>
 * <p>Company: Comas</p>
 * @author 이종원
 * @version 1.0
 */
public interface EAIConstants {
    /**  차세대외화자금-> CLS  MT300 등 송수신 */
    String EIRI005164 = "EIRI005164";

    /**  CLS-> 차세대외화자금 MT396 등 송수신*/
    String EIRI005165 = "EIRI005165";

    /**  CLS-> 차세대외국환 Our,Their 기표데이터*/
    String EIRI005166 = "EIRI005166";

    /**  CLS-> 차세대외국환 원화 Pay-Out 정보*/
    String EIRI005167 = "EIRI005167";

    /**  차세대외국환-> CLS 원화 기표처리 통지전문*/
    String EIRI005168 = "EIRI005168";

    /**  CLS-> 차세대외신 MT298 등 송신*/
    String EIRI005169 = "EIRI005169";

    /**  차세대외신-> CLS 원화관련 MT298 등 수신*/
    String EIRI005170 = "EIRI005170";

    /**  차세대외국환-> CLS 환율정보*/
    String EIRI005171 = "EIRI005171";

    /**  CLS-> 차세대총계정 총계정 기표데이터*/
    String EIRI004767 = "EIRI004767";

    /**  CLS-> TANDEM 금융결제원 앞 송신전문*/
    String EIRT007101 = "EIRT007101";

    /**  TANDEM-> CLS 금융결제원의 송신전문*/
    String EIRT007301 = "EIRT007301";

    /**
     * Timeout check call
     */
    String EIRG000001 = "EIRG000001";

}