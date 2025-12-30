/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.cache;

import nebsoa.common.util.DataMap;
import nebsoa.management.ManagementObject;

/*******************************************************************
 * <pre>
 * 1.설명 
 * XmlCache 정보를 리로드 하는 클래스 
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
 * $Log: XmlCacheReloader.java,v $
 * Revision 1.1  2018/01/15 03:39:49  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:30  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:23  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:26  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:51  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:23  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:37:38  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2007/07/16 11:25:19  홍윤석
 * 수정
 *
 *
 * </pre>
 ******************************************************************/
public class XmlCacheReloader extends ManagementObject{
	

	private static Object dummy = new Object();
	private static XmlCacheReloader instance;
    
	private XmlCacheReloader(){

	}
    
	/**
	 * @return CodeUtil
	 */
	public static XmlCacheReloader getInstance(){
		if(instance == null){
			synchronized(dummy ){
				instance = new XmlCacheReloader();
			}
		}
		return instance;
	}


    /** 전체리로딩 */
    public static void reloadAll(DataMap map) {
    	
    	XmlCacheManager.getInstance().reloadAll();
    	
    }
    
	public Object getManagementObject() {
		return instance;
	}

	public void setManagementObject(Object obj) {
		instance = (XmlCacheReloader) obj;
	}
}
