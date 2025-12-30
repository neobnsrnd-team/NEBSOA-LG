/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.biz.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
/*******************************************************************
 * <pre>
 * 1.설명 
 * remoate api call facade ejb
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
 * $Log: RPCHome.java,v $
 * Revision 1.1  2018/01/15 03:39:51  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:05  cvs
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
 * Revision 1.1  2008/08/04 08:54:54  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:29  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:24  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2007/01/29 09:43:02  이종원
 * 최초작성
 *
 * </pre>
 ******************************************************************/
public interface RPCHome extends EJBHome {
	
	/**
	 * RPCEJB 객체를 생성하여 리턴합니다.
	 * 
	 * @return 생성된 RPCEJB 객체
	 * @throws RemoteException EJB 객체 생성 중 발생하는 Exception
	 * @throws CreateException EJB 객체 생성 중 발생하는 Exception
	 */
	public RPCEJB create() throws RemoteException, CreateException;

}