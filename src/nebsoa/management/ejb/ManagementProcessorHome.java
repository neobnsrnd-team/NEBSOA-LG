/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.management.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 원격으로 객체들을 관리하기 위한 ManagementAgent 실행을 대리하는 ManagementProcessorEJB
 * 의 Home interface
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
 * $Log: ManagementProcessorHome.java,v $
 * Revision 1.1  2018/01/15 03:39:50  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:40  cvs
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
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:34  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:39:13  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2006/07/21 07:36:54  김성균
 * 최초 등록
 *
 * </pre>
 ******************************************************************/
public interface ManagementProcessorHome extends EJBHome {
	
	/**
	 * ManagementProcessorEJB 객체를 생성하여 리턴합니다.
	 * 
	 * @return 생성된 ManagementProcessorEJB 객체
	 * @throws RemoteException BizProcessorEJB 객체 생성 중 발생하는 Exception
	 * @throws CreateException BizProcessorEJB 객체 생성 중 발생하는 Exception
	 */
	public ManagementProcessorEJB create() throws RemoteException, CreateException;

}