package nebsoa.service.session;

import nebsoa.common.log.LogManager;
import nebsoa.common.util.DataMap;

/*******************************************************************************
 * <pre>
 *  1.설명 
 *  SessionValueSetter 서비스 헤더 정보(Session 정보) 셋팅 Default 클래스
 *  각 사이트 마다 이 클래스를 상속 받아 구현 후 service.properties.xml에 해당 클래스를 기술하여야 한다.
 *  2.사용법
 *  
 *  &lt;font color=&quot;red&quot;&gt;
 *  3.주의사항
 *  &lt;/font&gt;
 * 
 *  @author : 오재훈
 *  @version
 *  @작성 일자 : 2008. 02. 11
 * ******************************************************************
 *  - 변경이력 (버전/변경일시/작성자)
 *  
 *  $Log: SessionValueSetter.java,v $
 *  Revision 1.1  2018/01/15 03:39:54  cvs
 *  *** empty log message ***
 *
 *  Revision 1.1  2016/04/15 02:23:40  cvs
 *  neo cvs init
 *
 *  Revision 1.1  2011/07/01 02:13:51  yshong
 *  *** empty log message ***
 *
 *  Revision 1.1  2008/11/18 11:27:27  김성균
 *  *** empty log message ***
 *
 *  Revision 1.1  2008/11/18 11:01:29  김성균
 *  LGT Gateway를 위한 프로젝트로 분리
 *
 *  Revision 1.1  2008/08/04 08:54:52  youngseokkim
 *  *** empty log message ***
 *
 *  Revision 1.1  2008/02/14 04:34:29  오재훈
 *  web.login.Authenticator, Authorizor 클래스들과 web.session.SessionManager,UserInfo 들이 삭제되고 common.acl 패키지의 클래스들로 대체되었습니다.
 *  web.util.Request.Util 클래스도 common.util.RequestUtil로 옴겨졌습니다.
 *
 * </pre>
 ******************************************************************************/
public class SessionValueSetter {
	
	public SessionValueSetter(){
		LogManager.debug("######### CREATE Default! SessionValueSetter CLASS  ##########");

	}

	public DataMap setSystemKeyword(nebsoa.common.acl.UserSession userInfo, DataMap map) {

		LogManager.debug("######### Default! SessionValueSetter.setSystemKeyword()  ##########");
		
		/**
		 * 서비스 헤더 정보 셋팅
		 */
		map.put("USER_ID", userInfo.getUserId());// 로그인여부
		map.put("로그인여부", "Y");// 로그인여부
		return map;
	}
	
}
