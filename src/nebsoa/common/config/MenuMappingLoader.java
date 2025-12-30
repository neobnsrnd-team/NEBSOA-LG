/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.config;

import java.util.ArrayList;
import java.util.Map;

import nebsoa.common.exception.SysException;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.PropertyManager;
import nebsoa.common.util.StringUtil;


/*******************************************************************
 * <pre>
 * 1.설명 
 * 메뉴 정보를 Load하는 Interface 입니다.
 * 
 * 2.사용법
 * MenuMapping 인터페이스를 implements하여 각 추상 메소드를 구현하면 됩니다.
 * 또한, web_config.property.xml 파일에서 MenuMapping.class 프로퍼티 키의 Value값을
 * MenuMapping 인터페이스를 구현한 클래스(패키지명 포함)명으로 설정해 주십시요. 
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
 * $Log: MenuMappingLoader.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:26  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:26  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:29  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:52  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/02/14 10:30:33  오재훈
 * WebAppMapping 클래스가 ap단으로 이동됨
 *
 * Revision 1.2  2008/02/14 04:34:29  오재훈
 * web.login.Authenticator, Authorizor 클래스들과 web.session.SessionManager,UserInfo 들이 삭제되고 common.acl 패키지의 클래스들로 대체되었습니다.
 * web.util.Request.Util 클래스도 common.util.RequestUtil로 옴겨졌습니다.
 *
 * Revision 1.1  2008/01/22 05:58:28  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:13  안경아
 * *** empty log message ***
 *
 * Revision 1.5  2006/09/30 07:29:09  최수종
 * *** empty log message ***
 *
 * Revision 1.4  2006/09/14 08:33:17  최수종
 * 로직 수정
 *
 * Revision 1.3  2006/09/13 01:32:14  최수종
 * 로직 수정
 *
 * Revision 1.2  2006/09/12 11:53:12  최수종
 * 로직 수정
 *
 * Revision 1.1  2006/09/12 10:58:16  최수종
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public class MenuMappingLoader {
	
	private static MenuMapping menuMapping;
	
	/**
	 * MenuMapping 인터페이스를 구현한 클래스를 Load하는 메소드.
	 * 
	 * @return MenuMapping
	 */
	public synchronized static MenuMapping getMenuMapping()
	{
	    if(menuMapping != null)
	    {
	    	return menuMapping;
	    }
	    
        String menuMappingClassName = 
        	PropertyManager.getProperty("web_config", "MenuMapping.class", null);
        
        if(!StringUtil.isNull(menuMappingClassName))
        {
            try 
            {
            	menuMapping = (MenuMapping) Class.forName(menuMappingClassName).newInstance();
            	
            	LogManager.debug(menuMappingClassName+" 인스턴스 생성 ########################");
            	
            } 
            catch (InstantiationException e) 
            {
               throw new SysException("[CANN'T MAKE INSTANCE 생성자 체크 :"+menuMappingClassName+"]");
            } 
            catch (IllegalAccessException e) 
            {
               throw new SysException("[CANN'T MAKE INSTANCE 생성자 PUBLIC 인지 체크 :"+menuMappingClassName+"]");
            }
            catch(ClassNotFoundException e)
            {
               throw new SysException("[CLASS_NOT_FOUND:"+e.getMessage()+"]");
            }
        }
        else
        {
        	menuMapping = null;
        }

	    return menuMapping;
	}
	
}





