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

import nebsoa.common.base.BaseSessionBean;
import nebsoa.common.util.DataMap;
import nebsoa.common.util.MethodInvokerUtil;
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
 * $Log: RPCBean.java,v $
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
 * Revision 1.3  2008/04/08 04:46:07  김승희
 * 메소드 추가
 *
 * Revision 1.2  2008/02/14 09:01:05  김성균
 * MethodInvokerUtil 패키지 리팩토링
 *
 * Revision 1.1  2008/01/22 05:58:29  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:25  안경아
 * *** empty log message ***
 *
 * Revision 1.8  2007/06/12 07:52:34  김승희
 * 리턴 타입 관련 변경
 *
 * Revision 1.7  2007/05/09 06:58:59  김승희
 * MethodInvokerUtil 사용하도록 수정
 *
 * Revision 1.6  2007/03/21 20:04:23  이종원
 * Profiler추가
 *
 * Revision 1.5  2007/02/07 07:15:11  김승희
 * Exception 처리 변경
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
 * Revision 1.1  2007/01/29 09:50:53  이종원
 * 최초작성
 *
 * </pre>
 ******************************************************************/
public class RPCBean extends  BaseSessionBean implements RPC {
	

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 6237409295796732748L;
	
	/**
	 * remote의 logic 호출합니다.
	 * @throws Exception 
	 */
    public DataMap doRPC(String className,String methodName,DataMap map)throws Exception   {
        return (DataMap)MethodInvokerUtil.invoke(className, methodName, map);
	       
    }
		
    
	/**
	 * Singleton패턴을 구현한 모듈을 호출합니다.
	 * 반드시 해당 모듈은 getInstace 라는 명으로 instance를 return 하는 메소드를 구현해야 합니다.
	 * 
	 * remote의 logic 호출합니다.
	 * @throws Exception 
	 */
    public DataMap doSingletonRPC(String className,String methodName,DataMap map)throws Exception   {
        return (DataMap)MethodInvokerUtil.invokeSingleton(className, methodName, map);
    }


	/* (non-Javadoc)
	 * @see spider.biz.ejb.RPC#doRPC(java.lang.String, java.lang.String, java.lang.Class[], java.lang.Object[])
	 */
	public Object doRPC(String className, String methodName, Class[] argumentClass, Object[] argumentValue)
			throws RemoteException, Exception {
		return MethodInvokerUtil.invoke(className, methodName, argumentClass, argumentValue);
	}

	
}