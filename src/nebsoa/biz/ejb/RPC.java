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

import nebsoa.common.exception.SysException;
import nebsoa.common.util.DataMap;

/*******************************************************************
 * <pre>
 * 1.설명 
 * remote api call utility facade
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
 * $Log: RPC.java,v $
 * Revision 1.1  2018/01/15 03:39:51  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:04  cvs
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
 * Revision 1.2  2008/04/08 04:46:07  김승희
 * 메소드 추가
 *
 * Revision 1.1  2008/01/22 05:58:29  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:24  안경아
 * *** empty log message ***
 *
 * Revision 1.4  2007/02/05 01:59:35  김승희
 * exception 처리 재변경
 *
 * Revision 1.3  2007/02/05 01:45:52  김승희
 * exception 변경
 *
 * Revision 1.2  2007/02/02 09:39:27  김승희
 * 싱글톤으로 구현한 모듈 호출 가능하도록 수정
 *
 * Revision 1.1  2007/01/29 09:27:19  이종원
 * 최초작성
 *
 * </pre>
 ******************************************************************/
public interface RPC {
	
    /**
     *  remote api call.
     */
    public DataMap doRPC(String className,String methodName, DataMap map) throws RemoteException, Exception ;
    
    /**
     * 싱글톤으로 구현한 클래스의 메소드를 호출한다.
	 * @param className 클래스명
	 * @param methodName 메소드명
	 * @param map DataMap
	 * @return
     * @throws RemoteException
     * @throws Exception
     */
    public DataMap doSingletonRPC(String className,String methodName, DataMap map) throws RemoteException, Exception ;
    
    /**
     * 해당 클래스의 메소드를 호출합니다.
	 * @param className 클래스명
	 * @param methodName 메소드명
	 * @param argumentClass 인자 클래스명 배열
	 * @param argumentValue 인자 값 배열
	 * @return 리턴값
     * @throws RemoteException
     * @throws Exception
     */
    public Object doRPC(String className, String methodName, Class[] argumentClass, Object[] argumentValue ) throws RemoteException, Exception ;
    
}// end of RPC