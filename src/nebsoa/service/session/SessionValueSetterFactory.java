package nebsoa.service.session;

import java.util.HashMap;

import nebsoa.biz.factory.BizFactory;
import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.PropertyManager;

/*******************************************************************************
 * <pre>
 *  1.설명 
 *  SessionValueSetterFactory 서비스 헤더 정보(Session 정보) 셋팅 클래스 로딩
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
 *  $Log: SessionValueSetterFactory.java,v $
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
 *  Revision 1.2  2008/02/21 07:24:06  오재훈
 *  *** empty log message ***
 *
 *  Revision 1.1  2008/02/14 04:34:29  오재훈
 *  web.login.Authenticator, Authorizor 클래스들과 web.session.SessionManager,UserInfo 들이 삭제되고 common.acl 패키지의 클래스들로 대체되었습니다.
 *  web.util.Request.Util 클래스도 common.util.RequestUtil로 옴겨졌습니다.
 *
 * </pre>
 ******************************************************************************/
public class SessionValueSetterFactory {

	private static Object dummy=new Object();
	
	private static SessionValueSetterFactory instance = null;
	
	private HashMap sessionValueSetter;
		
	private static final String SESSION_HEADER_SETTER_CLASS_NAME = ".SESSION_HEADER_SETTER_CLASS";
		
	
	/**
	 * 생성자
	 */
	private SessionValueSetterFactory(){
		
	}
	
    /**
     * 작성일자 : 2008. 02. 20 
     * 작성자 : kfetus
     * 설명 : 싱글톤을 위한 처리
     * @return
     */
    public synchronized static SessionValueSetterFactory getInstance() {
    	if (instance == null) {
            synchronized(dummy){
                instance = new SessionValueSetterFactory();
                instance.sessionValueSetter = new HashMap();
            }
    	}
    	return instance;
    }

    /**
     * 작성일자 : 2008. 02. 20 
     * 작성자 : kfetus
     * 설명 : 컨텍스트 별 SessionValueSetter 클래스 반환
     * @return
     */    
	public SessionValueSetter getSessionValueSetter(String contextName) {
		
		if(instance.sessionValueSetter.get(contextName) == null) {
			SessionValueSetter setter;
			String classFullName="";
			try {
				synchronized (dummy) {
					//context_name+.SESSION_HEADER_SETTER_CLASS 값을 service.properties.xml에서 찾음.
					classFullName = PropertyManager.getProperty("service", 
							contextName+SESSION_HEADER_SETTER_CLASS_NAME,"nebsoa.service.session.SessionValueSetter");
					
					LogManager.debug("SessionValueSetter 클래스 명=" + classFullName );
					setter = (SessionValueSetter)Class.forName(classFullName).newInstance();
					
					instance.sessionValueSetter.put(contextName, setter);
				}
			} catch (Exception e) {
				LogManager.error("SessionValueSetter 생성에 실패했습니다.[" + classFullName +"]::" + e.toString());
				throw new SysException(e);
			}	
			return setter;
			
		} else {
			return (SessionValueSetter) instance.sessionValueSetter.get(contextName);
		}
		
	}
	
}
