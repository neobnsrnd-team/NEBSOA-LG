/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
 package nebsoa.service.util;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 은행상태 정보를 로딩하여 담고 있는 클래스
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
 * $Log: BankStatus.java,v $
 * Revision 1.1  2018/01/15 03:39:50  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:49  cvs
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
 * Revision 1.1  2008/08/04 08:54:51  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:18  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2008/01/07 01:33:46  김성균
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class BankStatus {
    
    private String bankCode; 
    private String bankName; 
    private String stableYn; 
    private String timeout; 
     
    public BankStatus() {
    }
    
    /**
     * @return Returns the bankCode.
     */
    public String getBankCode() {
        return bankCode;
    }

    /**
     * @param bankCode The bankCode to set.
     */
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    /**
     * @return Returns the bankName.
     */
    public String getBankName() {
        return bankName;
    }

    /**
     * @param bankName The bankName to set.
     */
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    /**
     * @return Returns the stableYn.
     */
    public boolean isStable() {
        return "Y".equalsIgnoreCase(stableYn);
    }

    /**
     * @param stableYn The stableYn to set.
     */
    public void setStableYn(String stableYn) {
        this.stableYn = stableYn;
    }

    /**
     * @return Returns the timeout.
     */
    public String getTimeout() {
        return timeout;
    }

    /**
     * @param timeout The timeout to set.
     */
    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString(){
        return ">>> bankCode:"+bankCode
                +"\n bankName:"+ bankName
                +"\n stableYn:"+ stableYn
                +"\n timeout:"+ timeout
                ;
    }
}
