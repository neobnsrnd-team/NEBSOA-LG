/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.service.util;

import java.io.FileNotFoundException;
import java.util.Map;

import nebsoa.common.exception.SysException;
import nebsoa.common.util.BankStatusCheckable;
import nebsoa.common.util.DataMap;
import nebsoa.common.util.PropertyManager;
import nebsoa.management.ManagementObject;


/*******************************************************************
 * <pre>
 * 1.설명 
 * 은행상태 정보를 찾아 오는 유틸리티.
 * 2.사용법
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author $Author: cvs $
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * $Log: BankStatusManager.java,v $
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
 * Revision 1.3  2008/10/30 01:30:22  shkim
 * 상태가 등록되지 않은 은행코드인 경우 stable 상태로 간주하도록 수정
 *
 * Revision 1.2  2008/09/19 07:00:55  youngseokkim
 * 불필요 import 제거
 *
 * Revision 1.1  2008/08/04 08:54:51  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.5  2008/05/19 04:45:22  김영석
 * 서비스 옵션 체크 로직 변경
 *
 * Revision 1.4  2008/03/11 02:22:32  김영석
 * 클래스명 얻어와 해당 클래스로 생성하도록 수정
 *
 * Revision 1.3  2008/03/10 05:30:20  김영석
 * *** empty log message ***
 *
 * Revision 1.2  2008/03/06 09:01:12  김영석
 * 클래스명 얻어와 해당 클래스로 생성하도록 수정
 *
 * Revision 1.1  2008/01/22 05:58:18  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2008/01/07 01:33:46  김성균
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class BankStatusManager extends ManagementObject {
    
    private static Object dummy = new Object();
    
    private static BankStatusManager instance;

    /**
     * 영업일 정보를 로딩하는 대리 클래스 
     */
    private static BankStatusCheckable bankStatusCheckDelegator;
    
    
    /**
     * 은행상태 정보를 가지고 있는 객체들의 풀 
     */
    private Map bankStatusPool;
    
    /**
     * 싱글톤 처리 
     */
    private BankStatusManager() {
    	
        String className = null;
        
        try {
            className = PropertyManager.getProperty("default","BANK_STATUS_CHECK_CLASS_NAME", "nebsoa.common.util.BankStatusChecker");
            bankStatusCheckDelegator = (BankStatusCheckable) Class.forName(className).newInstance();
        }
        catch (InstantiationException e1) {
           throw new SysException("[CANN'T MAKE INSTANCE 생성자 체크 :"+className+"]");
        }
        catch (IllegalAccessException e1) {
           throw new SysException("[CANN'T MAKE INSTANCE 생성자 PUBLIC 인지 체크 :"+className+"]");
        }
        catch(ClassNotFoundException e){
           throw new SysException("[CLASS_NOT_FOUND:"+e.getMessage()+"]");
        }
    }
    
    /**
     * 싱글톤 객체 얻어오기
     * 인스턴스 생성하고 데이타 로딩...
     * @return
     */
    public static BankStatusManager getInstance() {
        if (instance == null) {
            synchronized (dummy) {
                instance = new BankStatusManager();
                instance.loadAll();
                instance.toXml();
            }
        }
        return instance;
    }
    
    /**
     * 은행상태 정보를 로딩한다.
     */
    private void loadAll(){
        if (isXmlMode()) {
            try {
                fromXml();
            } catch (FileNotFoundException e) {
                throw new SysException("XML 파일을 찾을 수 없습니다.");
            }
        } else {
        	bankStatusPool = bankStatusCheckDelegator.loadBankStatusPool();
        }
    }
    
    /**
     * @param bankCode
     * @return
     */
    public boolean isStable(String bankCode) {
        /*
    	return getBankStatus(bankCode).isStable();
    	*/
    	//BankStatus가 null인 경우는 stable로 간주한다.
    	BankStatus getBankStatus = getBankStatus(bankCode);
    	return getBankStatus==null?true:getBankStatus.isStable();
    }
    
    /**
     * @param bankCode
     * @return
     */
    public BankStatus getBankStatus(String bankCode) {
        /*
    	BankStatus bankStatus = (BankStatus) bankStatusPool.get(bankCode);
        if (bankStatus == null) {
            throw new BankStatusNotFoundException(bankCode);
        }
        return bankStatus;
        */
        return (BankStatus) bankStatusPool.get(bankCode);
    }
    
    /**
     * 전체리로딩 
     */
    public static void reloadAll(DataMap map) {
        getInstance().loadAll();
        getInstance().toXml();
    }
    
    /* (non-Javadoc)
     * @see nebsoa.management.ManagementObject#getInstance()
     */
    public Object getManagementObject() {
        return instance;
    }
    
    /* (non-Javadoc)
     * @see nebsoa.management.ManagementObject#setInstance(java.lang.Object)
     */
    public void setManagementObject(Object obj) {
        instance = (BankStatusManager) obj;
    }
}
