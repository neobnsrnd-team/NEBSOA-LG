/*******************************************************************
 * 외환은행 CLS 연계 프로젝트
 *
 * Copyright (c) 2003-2004 by COMAS, Inc.
 * All rights reserved.
 * @file : EAIProcessor.java
 * @author : 이종원
 * @date : 2004. 9. 2.
 * @설명 : 
 * <pre>
 * 
 * </pre>
 *******************************************************************
 * $Id: EAIProcessor.java,v 1.1 2018/01/15 03:39:52 cvs Exp $
 * $Log: EAIProcessor.java,v $
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
 * Revision 1.1  2007/11/26 08:37:50  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2006/06/29 04:02:09  이종원
 * *** empty log message ***
 *
 * Revision 1.2  2004/10/15 08:37:21  helexis
 * formatting 이후 재 commit
 *
 * Revision 1.3  2004/09/10 06:55:34  ljw
 * *** empty log message ***
 *
 * Revision 1.2  2004/09/02 13:11:21  ljw
 * *** empty log message ***
 *
 * Revision 1.1  2004/09/02 06:00:19  ljw
 * *** empty log message ***
 *    
 ******************************************************************/
package test.spiderlink.message;

import javax.ejb.EJBHome;

import nebsoa.common.exception.SysException;
import nebsoa.common.jndi.EJBManager;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.DataMap;

/**
 * 2004. 9. 2. 이종원
 * 설명 :
 * <pre>
 * eai로 부터 수신한 메세지를 각 업무에 맞게 처리 하기 위한 클래스
 * </pre> 
 */
public abstract class EAIProcessor implements Cloneable {

    /**
     * 
     * 2004. 9. 10. 이종원 작성
     * @param vo
     * @return
     * 설명:eai전문 처리 모듈이 상속 받아서 구현해야 할 메소드 입니다.
     */
    public abstract EAIMessage execute(EAIMessage vo);

    /**
     * EJB
     */
    //TODO public static ControllerEJBHome home;

    /**
     * 2004. 9. 2. 이종원 작성
     * 설명: 전문 처리 EJB컴포넌트를 접근한다.
     */
    public static void getControllerHome() {
        EJBHome obj = EJBManager.lookup("ControllerEJB", "STPA");
        //home = (ControllerEJBHome) obj;
    }

    /**
     *  
     * 부모 클래스로 부터 물려 받은 함수 재정의
     * @see java.lang.Object#clone()
     * 설명 :객체를 복제 . 성능향상을 위해 만듬
     */
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            LogManager.error(e.getMessage(), e);
            throw new SysException("EAIProcessor 생성 실패");
        }
    }

    /**
     * 2004. 9. 2. 이종원 작성
     * @param vo
     * @return
     * 설명: 전문 처리 엔진으로 전문을 전송한다.
     */
    public static DataMap processBizFlow(DataMap vo) {
/*
        if (home == null) {
            getControllerHome();
        }
        ControllerEJB ejb = null;
        try {
            ejb = home.create();
            vo = ejb.doProcess(vo);
            ejb.remove();
        } catch (Exception e) {
            LogManager.error(e.getMessage());
            home = null;
        }
    */    
        return vo;
    }

    /**
     * 2004. 9. 2. 이종원 작성
     * @param vo
     * @return
     * 설명: 전문 처리 엔진으로 eai 전문을 전송한다.
     
     public static EAIMessage processEAIBizFlow(EAIMessage msg){
     if(home == null){
     getControllerHome();
     }
     ControllerEJB ejb = null; 
     try {
     ejb = home.create();
     EAIMessage vo = new EAIMessage();
     vo.setEAIMessage(msg);
     vo = ejb.doEAIProcess(vo);
     msg = vo.getEAIMessage();
     ejb.remove();
     } catch (Exception e) {
     LogManager.error(e.getMessage(),e);
     home = null;
     msg = null;
     }
     return msg;
     }
     */

}