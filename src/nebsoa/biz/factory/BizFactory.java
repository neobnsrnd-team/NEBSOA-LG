/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.biz.factory;

import nebsoa.biz.base.Biz;
import nebsoa.biz.util.BizInfo;
import nebsoa.biz.util.BizManager;
import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.StringUtil;

/**
 * Biz 클래스 팩토리 객체
 * @author Administrator
 */
/*******************************************************************
 * <pre>
 * 1.설명 
 * 메일를 전송하기 위한 정보를 담고 있는 클래스
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
 * $Log: BizFactory.java,v $
 * Revision 1.1  2018/01/15 03:39:54  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:40  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:27  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:29  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:36  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:39:09  안경아
 * *** empty log message ***
 *
 * Revision 1.17  2007/06/26 01:03:11  홍윤석
 * preTrxId --> queName으로 수정
 *
 * Revision 1.16  2007/03/07 02:42:25  김성균
 * 사용하지 않는 메소드 삭제
 *
 * Revision 1.15  2007/01/22 18:48:50  이종원
 * *** empty log message ***
 *
 * Revision 1.14  2006/08/22 02:04:23  김성균
 * *** empty log message ***
 *
 * Revision 1.13  2006/08/22 01:48:34  김성균
 * *** empty log message ***
 *
 * Revision 1.12  2006/08/03 05:18:59  김성균
 * BizInfo의 사용하지 않는 메소드 삭제
 *
 * Revision 1.11  2006/07/28 05:48:20  김성균
 * *** empty log message ***
 *
 * Revision 1.10  2006/07/04 12:09:55  김승희
 * getTempBiz 메소드의 마지막 addWasConfig("AP") --> addWasConfig("CMS_AP") 로 수정
 *
 * Revision 1.9  2006/06/26 14:21:11  김성균
 * *** empty log message ***
 *
 * Revision 1.8  2006/06/22 08:58:58  김성균
 * 디폴트 BizInfo 사용하도록 수정
 *
 * Revision 1.7  2006/06/22 05:16:51  김성균
 * *** empty log message ***
 *
 * Revision 1.6  2006/06/20 10:55:32  이종원
 * *** empty log message ***
 *
 * Revision 1.5  2006/06/20 10:54:44  이종원
 * *** empty log message ***
 *
 * Revision 1.4  2006/06/20 10:52:54  이종원
 * *** empty log message ***
 *
 * Revision 1.3  2006/06/17 10:37:45  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class BizFactory {
	static Object dummy = new Object();
	/**
	 * Comment for <code>instance</code>
	 */
	private static BizFactory instance;
    
    private BizFactory() {
    }
    
    /**
     * 싱글톤을 위한 처리
     * @return
     */
    public synchronized static BizFactory getInstance() {
    	if (instance == null) {
            synchronized(dummy){
                instance = new BizFactory();
            }
    	}
    	return instance;
    }
    
    /**
     * Biz class instance를 얻어온다.
     * @param className biz class full name
     * @return 해당하는 biz class instance 
     */
    public Biz getBiz(BizInfo bizInfo) {
    	String bizAppName = bizInfo.getBizAppName();
    	if (StringUtil.isNull(bizAppName)) {
    		throw new SysException("업무처리APP명이 존재하지 않습니다.");
    	}
        Biz biz= (Biz) forName(bizAppName);
        biz.setBizInfo(bizInfo);
        return biz;
    }

    /**
     * BIZ_APP 정보가 없을 경우 임시로 설정한다.
     * @param bizId
     * @return
     */
    public BizInfo getTempBiz(String bizId) {
        BizInfo bizInfo = new BizInfo();
        bizInfo.setBizAppId(bizId);
        bizInfo.setBizAppName(bizId); //id와 프로그램명을 동일하게 설정.
        bizInfo.setDupCheck(false);
        bizInfo.setQueName(null);
        return bizInfo;
    }
    
    public Object forName(String className){
        try {
            return  Class.forName(className).newInstance();
        } catch (InstantiationException e1) {
           throw new SysException("[CANN'T MAKE INSTANCE 생성자 체크 :"+className+"]");
        } catch (IllegalAccessException e1) {
           throw new SysException("[CANN'T MAKE INSTANCE 생성자 PUBLIC 인지 체크 :"+className+"]");
        }catch(ClassNotFoundException e){
           throw new SysException("[CLASS_NOT_FOUND:"+e.getMessage()+"]");
        }
    }   
}
