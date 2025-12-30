/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.engine.adapter.tcp;


/*******************************************************************
 * <pre>
 * 1.설명 
 * 전문을 수신하여 그 기관에 맞는 파싱 및 Biz App호출 및 응답 처리하는
 * 흐름을 정의 하였다.
 * 1. doPreProcess -
 * 2. doProcess -
 * 3. doPostProcess 순으로 호출 된다.
 * 
 * @param messageContext
 * @param dataMap
 * @return 처리결과가 담겨 있는 DataMap
 * 
 * 2.사용법
 * 
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author 이종원
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 
 * $Log: BaseOrgInHandler.java,v $
 * Revision 1.1  2018/01/15 03:39:50  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:52  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:24  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:27  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:53  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:28  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:39:05  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2006/10/02 18:32:34  이종원
 * BaseOrgHandler상속
 *
 * Revision 1.1  2006/10/02 14:26:57  이종원
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public abstract class BaseOrgInHandler extends BaseOrgHandler {
    
	private String bizAppId;

    public String getBizAppId() {
        return bizAppId;
    }

    public void setBizAppId(String bizAppid) {
        this.bizAppId = bizAppid;
    }
}
