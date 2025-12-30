/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
 package nebsoa.biz.util;


/*******************************************************************
 * <pre>
 * 1.설명 
 * Biz application이 등록된 FWK_BIZ_APP 테이블 및 연관 테이블의 정보를 로딩하여 담고 있는 클래스
 * 프레임웍 초기화 시에 생성 된다.
 * 주요 용도는 특정 Biz나 SpiderLink연계 엔진이 해당 was에서 수행되고 있는지 정보를 
 * client로하여금 알게하여 해당 service를 reote혹은 local에서 call하게 하기 위한 용도로 쓰인다.
 * 
 * 2.사용법
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author $Author: cvs $
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 
 * $Log: BizInfo.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:28  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:26  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:29  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:33  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:34  안경아
 * *** empty log message ***
 *
 * Revision 1.10  2007/07/08 09:52:36  김성균
 * 로그여부 설정기능 추가
 *
 * Revision 1.9  2007/07/02 09:20:28  youngseokkim
 * 로그여부 추가
 *
 * Revision 1.8  2007/06/26 01:03:11  홍윤석
 * preTrxId --> queName으로 수정
 *
 * Revision 1.7  2006/08/22 01:49:18  김성균
 * *** empty log message ***
 *
 * Revision 1.6  2006/08/03 05:18:09  김성균
 * 사용하지않는 정보 삭제
 *
 * Revision 1.5  2006/07/25 09:12:14  김성균
 * *** empty log message ***
 *
 * Revision 1.4  2006/06/26 02:51:51  김성균
 * method명 추가
 *
 * Revision 1.3  2006/06/23 09:33:20  김성균
 * 업무도메인/업무분류 DB 변경 반영
 *
 * Revision 1.2  2006/06/22 08:25:03  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2006/06/20 10:54:53  이종원
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class BizInfo {
    
    private String bizAppId;

    private String bizAppName;

    private boolean dupCheck;
    
    private String queName;
    
    private boolean logYn; 
    

    public BizInfo() {
    }

    public String getBizAppId() {
        return bizAppId;
    }

    public void setBizAppId(String bizAppId) {
        this.bizAppId = bizAppId;
    }

    public String getBizAppName() {
        return bizAppName;
    }

    public void setBizAppName(String bizAppName) {
        this.bizAppName = bizAppName;
    }

    public boolean isDupCheck() {
        return dupCheck;
    }

    public void setDupCheck(boolean dupCheck) {
        this.dupCheck = dupCheck;
    }
    
    public boolean isLogEnabled() {
        return logYn;
    }

    public void setLogYn(boolean logYn) {
        this.logYn = logYn;
    }
    
    /**
     * @return Returns the queName.
     */    
	public String getQueName() {
		return queName;
	}
	
    /**
     * @param queName The queName to set.
     */
	public void setQueName(String queName) {
		this.queName = queName;
	}    

    public String toString(){
        return ">>> bizAppId;"+bizAppId
                +"\n bizAppName;"+ bizAppName
                +"\n 이중처리체크여부;"+ dupCheck
                +"\n 실행큐 이름;"+ queName
                +"\n 로그여부;"+ logYn
                ;
    }


}
